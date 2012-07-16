/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.felix.http.base.internal.context;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.EventListener;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.Filter;
import javax.servlet.FilterRegistration;
import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextAttributeEvent;
import javax.servlet.ServletContextAttributeListener;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.SessionCookieConfig;
import javax.servlet.SessionTrackingMode;
import javax.servlet.FilterRegistration.Dynamic;
import javax.servlet.descriptor.JspConfigDescriptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.felix.http.base.internal.handler.RequestDispatcherAdaptor;
import org.apache.felix.http.base.internal.logger.SystemLogger;
import org.apache.felix.http.base.internal.util.MimeTypes;
import org.osgi.framework.Bundle;
import org.osgi.service.http.HttpContext;

public final class ServletContextImpl implements ExtServletContext {
  private final Bundle bundle;

  private final ServletContext context;

  private final HttpContext httpContext;

  private final Map<String, Object> attributes;

  private final ServletContextAttributeListener attributeListener;

  private Dictionary<String, String> initParameter;

  public ServletContextImpl(Bundle bundle, ServletContext context,
    HttpContext httpContext, ServletContextAttributeListener attributeListener,
    boolean sharedAttributes) {
    this.bundle = bundle;
    this.context = context;
    this.httpContext = httpContext;
    this.attributeListener = attributeListener;
    this.attributes = sharedAttributes ? null
      : new ConcurrentHashMap<String, Object>();
  }

  public String getContextPath() {
    // return this.context.getContextPath();
    String contextPath = this.getInitParameter("Osgi-Context-Path");
    if (contextPath != null && contextPath.trim().length() > 0) {
      return contextPath;
    }

    // try {
    //			Method getContextPathMethod = this.context.getClass().getMethod("getContextPath", null); //$NON-NLS-1$
    // String servletPath = httpContext.getServletPath();
    // if (servletPath == null) {
    // servletPath = "";
    // }
    // return (String) getContextPathMethod.invoke(this.context, null) +
    // servletPath;
    // } catch (Exception e) {
    // // ignore
    // }
    return this.context.getContextPath();
  }

  public ServletContext getContext(String uri) {
    return this.context.getContext(uri);
  }

  public int getMajorVersion() {
    return this.context.getMajorVersion();
  }

  public int getMinorVersion() {
    return this.context.getMinorVersion();
  }

  public Set getResourcePaths(String path) {
    Enumeration paths = this.bundle.getEntryPaths(normalizePath(path));
    if ((paths == null) || !paths.hasMoreElements()) {
      return null;
    }

    Set<String> set = new HashSet<String>();
    while (paths.hasMoreElements()) {
      set.add((String) paths.nextElement());
    }

    return set;
  }

  public URL getResource(String path) {
    return this.httpContext.getResource(normalizePath(path));
  }

  public InputStream getResourceAsStream(String path) {
    URL res = getResource(path);
    if (res != null) {
      try {
        return res.openStream();
      } catch (IOException e) {
        // Do nothing
      }
    }

    return null;
  }

  private String normalizePath(String path) {
    if (path == null) {
      return null;
    }

    String normalizedPath = path.trim().replaceAll("/+", "/");
    if (normalizedPath.startsWith("/") && (normalizedPath.length() > 1)) {
      normalizedPath = normalizedPath.substring(1);
    }

    return normalizedPath;
  }

  public RequestDispatcher getRequestDispatcher(String uri) {
    // return null;
    String osgiContextPath = this.getInitParameter("Osgi-Context-Path");
    String path = uri;
    if (osgiContextPath != null && !path.startsWith(osgiContextPath)) {
      path = osgiContextPath + path;
    }
    return new RequestDispatcherAdaptor(this.context.getRequestDispatcher(path));
  }

  public RequestDispatcher getNamedDispatcher(String name) {
    // return null;
    return this.context.getNamedDispatcher(name);
  }

  public String getInitParameter(String name) {
    if (this.initParameter != null) {
      return this.initParameter.get(name);
    }
    return null;
  }

  public Enumeration getInitParameterNames() {
    if (this.initParameter != null) {
      return this.initParameter.elements();
    }
    return Collections.enumeration(Collections.emptyList());
  }

  public Object getAttribute(String name) {
    Object value = null;
    if (this.attributes != null) {
      value = this.attributes.get(name);
    }
    if (value == null) {
      value = this.context.getAttribute(name);
    }
    return value;
  }

  public Enumeration getAttributeNames() {
    return (this.attributes != null) ? Collections.enumeration(this.attributes
      .keySet()) : this.context.getAttributeNames();
  }

  public void setAttribute(String name, Object value) {
    if (value == null) {
      this.removeAttribute(name);
    } else if (name != null) {
      Object oldValue;
      if (this.attributes != null) {
        oldValue = this.attributes.put(name, value);
      } else {
        oldValue = this.context.getAttribute(name);
        this.context.setAttribute(name, value);
      }

      if (oldValue == null) {
        attributeListener.attributeAdded(new ServletContextAttributeEvent(this,
          name, value));
      } else {
        attributeListener.attributeReplaced(new ServletContextAttributeEvent(this,
          name, oldValue));
      }
    }
  }

  public void removeAttribute(String name) {
    Object oldValue;
    if (this.attributes != null) {
      oldValue = this.attributes.remove(name);
    } else {
      oldValue = this.context.getAttribute(name);
      this.context.removeAttribute(name);
    }

    if (oldValue != null) {
      attributeListener.attributeRemoved(new ServletContextAttributeEvent(this,
        name, oldValue));
    }
  }

  @SuppressWarnings("deprecation")
  public Servlet getServlet(String name) throws ServletException {
    return null;
  }

  @SuppressWarnings("deprecation")
  public Enumeration getServlets() {
    return Collections.enumeration(Collections.emptyList());
  }

  @SuppressWarnings("deprecation")
  public Enumeration getServletNames() {
    return Collections.enumeration(Collections.emptyList());
  }

  public void log(String message) {
    SystemLogger.info(message);
  }

  public void log(Exception cause, String message) {
    SystemLogger.error(message, cause);
  }

  public void log(String message, Throwable cause) {
    SystemLogger.error(message, cause);
  }

  public String getServletContextName() {
    return this.context.getServletContextName();
  }

  public String getRealPath(String name) {
    return null;
  }

  public String getServerInfo() {
    return this.context.getServerInfo();
  }

  public String getMimeType(String file) {
    String type = this.httpContext.getMimeType(file);
    if (type != null) {
      return type;
    }

    return MimeTypes.get().getByFile(file);
  }

  public boolean handleSecurity(HttpServletRequest req, HttpServletResponse res)
    throws IOException {
    return this.httpContext.handleSecurity(req, res);
  }

  public void setInitParams(Dictionary<String, String> initParameter) {
    if (this.initParameter == null) {
      this.initParameter = initParameter;
    }
  }

  public Dynamic addFilter(String arg0, Class<? extends Filter> arg1) {
    return context.addFilter(arg0, arg1);
  }

  public Dynamic addFilter(String arg0, Filter arg1) {
    return context.addFilter(arg0, arg1);
  }

  public Dynamic addFilter(String arg0, String arg1) {
    return context.addFilter(arg0, arg1);
  }

  public void addListener(Class<? extends EventListener> arg0) {
    context.addListener(arg0);
  }

  public void addListener(String arg0) {
    context.addListener(arg0);
  }

  public <T extends EventListener> void addListener(T arg0) {
    context.addListener(arg0);
  }

  public javax.servlet.ServletRegistration.Dynamic addServlet(String arg0,
    Class<? extends Servlet> arg1) {
    return context.addServlet(arg0, arg1);
  }

  public javax.servlet.ServletRegistration.Dynamic addServlet(String arg0,
    Servlet arg1) {
    return context.addServlet(arg0, arg1);
  }

  public javax.servlet.ServletRegistration.Dynamic addServlet(String arg0,
    String arg1) {
    return context.addServlet(arg0, arg1);
  }

  public <T extends Filter> T createFilter(Class<T> arg0) throws ServletException {
    return context.createFilter(arg0);
  }

  public <T extends EventListener> T createListener(Class<T> arg0)
    throws ServletException {
    return context.createListener(arg0);
  }

  public <T extends Servlet> T createServlet(Class<T> arg0) throws ServletException {
    return context.createServlet(arg0);
  }

  public void declareRoles(String... arg0) {
    context.declareRoles(arg0);
  }

  public ClassLoader getClassLoader() {
    return context.getClassLoader();
  }

  public Set<SessionTrackingMode> getDefaultSessionTrackingModes() {
    return context.getDefaultSessionTrackingModes();
  }

  public int getEffectiveMajorVersion() {
    return context.getEffectiveMajorVersion();
  }

  public int getEffectiveMinorVersion() {
    return context.getEffectiveMinorVersion();
  }

  public Set<SessionTrackingMode> getEffectiveSessionTrackingModes() {
    return context.getEffectiveSessionTrackingModes();
  }

  public FilterRegistration getFilterRegistration(String arg0) {
    return context.getFilterRegistration(arg0);
  }

  public Map<String, ? extends FilterRegistration> getFilterRegistrations() {
    return context.getFilterRegistrations();
  }

  public JspConfigDescriptor getJspConfigDescriptor() {
    return context.getJspConfigDescriptor();
  }

  public ServletRegistration getServletRegistration(String arg0) {
    return context.getServletRegistration(arg0);
  }

  public Map<String, ? extends ServletRegistration> getServletRegistrations() {
    return context.getServletRegistrations();
  }

  public SessionCookieConfig getSessionCookieConfig() {
    return context.getSessionCookieConfig();
  }

  public boolean setInitParameter(String arg0, String arg1) {
    return context.setInitParameter(arg0, arg1);
  }

  public void setSessionTrackingModes(Set<SessionTrackingMode> arg0)
    throws IllegalStateException, IllegalArgumentException {
    context.setSessionTrackingModes(arg0);
  }

}
