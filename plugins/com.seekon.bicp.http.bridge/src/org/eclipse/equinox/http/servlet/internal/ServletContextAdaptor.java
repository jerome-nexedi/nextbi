/*******************************************************************************
 * Copyright (c) 2005, 2007 Cognos Incorporated, IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Cognos Incorporated - initial API and implementation
 *     IBM Corporation - bug fixes and enhancements
 *******************************************************************************/
package org.eclipse.equinox.http.servlet.internal;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.security.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletRegistration;
import javax.servlet.FilterRegistration.Dynamic;
import javax.servlet.descriptor.JspConfigDescriptor;

import org.osgi.service.http.HttpContext;

public class ServletContextAdaptor implements ServletContext {

	private ServletContext servletContext;
	HttpContext httpContext;
	private AccessControlContext acc;
	private ProxyContext proxyContext;
	Dictionary initParameter;

	public ServletContextAdaptor(ProxyContext proxyContext, ServletContext servletContext, HttpContext httpContext, AccessControlContext acc) {
		this(proxyContext, servletContext, httpContext, acc, null);
	}

	public ServletContextAdaptor(ProxyContext proxyContext, ServletContext servletContext, HttpContext httpContext, AccessControlContext acc, Dictionary initParameter) {
		this.servletContext = servletContext;
		this.httpContext = httpContext;
		this.acc = acc;
		this.proxyContext = proxyContext;
		this.initParameter = initParameter;
	}

	/**
	 * 	@see javax.servlet.ServletContext#getResourcePaths(java.lang.String)
	 * 
	 * This method was added in the Servlet 2.3 API however the OSGi HttpService currently does not provide
	 * support for this method in the HttpContext interface. To support "getResourcePaths(...) this
	 * implementation uses reflection to check for and then call the associated HttpContext.getResourcePaths(...)
	 * method opportunistically. Null is returned if the method is not present or fails.
	 */
	public Set getResourcePaths(String name) {
		if (name == null || !name.startsWith("/")) //$NON-NLS-1$
			return null;
		try {
			Method getResourcePathsMethod = httpContext.getClass().getMethod("getResourcePaths", new Class[] {String.class}); //$NON-NLS-1$
			if (!getResourcePathsMethod.isAccessible())
				getResourcePathsMethod.setAccessible(true);
			return (Set) getResourcePathsMethod.invoke(httpContext, new Object[] {name});
		} catch (Exception e) {
			// ignore
		}
		return null;
	}

	public Object getAttribute(String attributeName) {
		Dictionary attributes = proxyContext.getContextAttributes(httpContext);
		////return attributes != null ? attributes.get(attributeName) : null;
		Object value = attributes.get(attributeName);
		if (value == null) {
			return this.servletContext.getAttribute(attributeName);//add
		}
		return value;
	}

	public Enumeration getAttributeNames() {
		Dictionary attributes = proxyContext.getContextAttributes(httpContext);
		return attributes.keys();
	}

	public void setAttribute(String attributeName, Object attributeValue) {
		Dictionary attributes = proxyContext.getContextAttributes(httpContext);
		attributes.put(attributeName, attributeValue);
		this.servletContext.setAttribute(attributeName, attributeValue);//add
	}

	public void removeAttribute(String attributeName) {
		Dictionary attributes = proxyContext.getContextAttributes(httpContext);
		attributes.remove(attributeName);
	}

	public String getMimeType(String name) {
		String mimeType = httpContext.getMimeType(name);
		return (mimeType != null) ? mimeType : servletContext.getMimeType(name);
	}

	public URL getResource(final String name) {
		try {
			return (URL) AccessController.doPrivileged(new PrivilegedExceptionAction() {
				public Object run() throws Exception {
					return httpContext.getResource(name);
				}
			}, acc);
		} catch (PrivilegedActionException e) {
			log(e.getException().getMessage(), e.getException());
		}
		return null;
	}

	public InputStream getResourceAsStream(String name) {
		URL url = getResource(name);
		if (url != null) {
			try {
				return url.openStream();
			} catch (IOException e) {
				log("Error opening stream for resource '" + name + "'", e); //$NON-NLS-1$ //$NON-NLS-2$
			}
		}
		return null;
	}

	public ServletContext getContext(String arg0) {
		return servletContext.getContext(arg0);
	}

	public String getInitParameter(String arg0) {
		if (this.initParameter != null) {
			return (String) this.initParameter.get(arg0);
		}
		return null;
		////return servletContext.getInitParameter(arg0);
	}

	public Enumeration getInitParameterNames() {
		if (this.initParameter != null) {
			return this.initParameter.elements();
		}
		return null;
		////return servletContext.getInitParameterNames();
	}

	public int getMajorVersion() {
		return servletContext.getMajorVersion();
	}

	public int getMinorVersion() {
		return servletContext.getMinorVersion();
	}

	public RequestDispatcher getNamedDispatcher(String arg0) {
		return new RequestDispatcherAdaptor(servletContext.getNamedDispatcher(arg0));
	}

	public String getRealPath(String arg0) {
		return servletContext.getRealPath(arg0);
	}

	public RequestDispatcher getRequestDispatcher(String arg0) {
		String osgiContextPath = this.getInitParameter("Osgi-Context-Path");
		String path = arg0;
		if (osgiContextPath != null && !path.startsWith(osgiContextPath)) {
			path = osgiContextPath + path;
		}
		return new RequestDispatcherAdaptor(servletContext.getRequestDispatcher(path));
	}

	public String getServerInfo() {
		return servletContext.getServerInfo();
	}

	/**@deprecated*/
	public Servlet getServlet(String arg0) throws ServletException {
		return servletContext.getServlet(arg0);
	}

	public String getServletContextName() {
		return servletContext.getServletContextName();
	}

	/**@deprecated*/
	public Enumeration getServletNames() {
		return servletContext.getServletNames();
	}

	/**@deprecated*/
	public Enumeration getServlets() {
		return servletContext.getServlets();
	}

	/**@deprecated*/
	public void log(Exception arg0, String arg1) {
		servletContext.log(arg0, arg1);
	}

	public void log(String arg0, Throwable arg1) {
		servletContext.log(arg0, arg1);
	}

	public void log(String arg0) {
		servletContext.log(arg0);
	}

	
	public Dynamic addFilter(String filterName, Class<? extends Filter> filterClass) {
    return servletContext.addFilter(filterName, filterClass);
  }

  public Dynamic addFilter(String filterName, Filter filter) {
    return servletContext.addFilter(filterName, filter);
  }

  public Dynamic addFilter(String filterName, String className) {
    return servletContext.addFilter(filterName, className);
  }

  public void addListener(Class<? extends EventListener> listenerClass) {
    servletContext.addListener(listenerClass);
  }

  public void addListener(String className) {
    servletContext.addListener(className);
  }

  public <T extends EventListener> void addListener(T t) {
    servletContext.addListener(t);
  }

  public javax.servlet.ServletRegistration.Dynamic addServlet(String servletName,
    Class<? extends Servlet> servletClass) {
    return servletContext.addServlet(servletName, servletClass);
  }

  public javax.servlet.ServletRegistration.Dynamic addServlet(String servletName,
    Servlet servlet) {
    return servletContext.addServlet(servletName, servlet);
  }

  public javax.servlet.ServletRegistration.Dynamic addServlet(String servletName,
    String className) {
    return servletContext.addServlet(servletName, className);
  }

  public <T extends Filter> T createFilter(Class<T> c) throws ServletException {
    return servletContext.createFilter(c);
  }

  public <T extends EventListener> T createListener(Class<T> c)
    throws ServletException {
    return servletContext.createListener(c);
  }

  public <T extends Servlet> T createServlet(Class<T> c) throws ServletException {
    return servletContext.createServlet(c);
  }

  public void declareRoles(String... roleNames) {
    servletContext.declareRoles(roleNames);
  }

  public ClassLoader getClassLoader() {
    return servletContext.getClassLoader();
  }

  public Set<SessionTrackingMode> getDefaultSessionTrackingModes() {
    return servletContext.getDefaultSessionTrackingModes();
  }

  public int getEffectiveMajorVersion() {
    return servletContext.getEffectiveMajorVersion();
  }

  public int getEffectiveMinorVersion() {
    return servletContext.getEffectiveMinorVersion();
  }

  public Set<SessionTrackingMode> getEffectiveSessionTrackingModes() {
    return servletContext.getEffectiveSessionTrackingModes();
  }

  public FilterRegistration getFilterRegistration(String filterName) {
    return servletContext.getFilterRegistration(filterName);
  }

  public Map<String, ? extends FilterRegistration> getFilterRegistrations() {
    return servletContext.getFilterRegistrations();
  }

  public JspConfigDescriptor getJspConfigDescriptor() {
    return servletContext.getJspConfigDescriptor();
  }

  public ServletRegistration getServletRegistration(String servletName) {
    return servletContext.getServletRegistration(servletName);
  }

  public Map<String, ? extends ServletRegistration> getServletRegistrations() {
    return servletContext.getServletRegistrations();
  }

  public SessionCookieConfig getSessionCookieConfig() {
    return servletContext.getSessionCookieConfig();
  }

  public boolean setInitParameter(String name, String value) {
    return servletContext.setInitParameter(name, value);
  }

  public void setSessionTrackingModes(Set<SessionTrackingMode> sessionTrackingModes)
    throws IllegalStateException, IllegalArgumentException {
    servletContext.setSessionTrackingModes(sessionTrackingModes);
  }

  // Added in Servlet 2.5
	public String getContextPath() {
		String contextPath = this.getInitParameter("Osgi-Context-Path");
		if (contextPath != null && contextPath.trim().length() > 0) {
			return contextPath;
		}

		try {
			Method getContextPathMethod = servletContext.getClass().getMethod("getContextPath", null); //$NON-NLS-1$
			String servletPath = proxyContext.getServletPath();
			if (servletPath == null) {
				servletPath = "";
			}
			return (String) getContextPathMethod.invoke(servletContext, null) + servletPath;
		} catch (Exception e) {
			// ignore
		}
		return null;
	}
}
