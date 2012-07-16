package com.tensegrity.wpalo.server;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.EventListener;
import java.util.Map;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterRegistration;
import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.SessionCookieConfig;
import javax.servlet.SessionTrackingMode;
import javax.servlet.FilterRegistration.Dynamic;
import javax.servlet.descriptor.JspConfigDescriptor;

public class ServletContextWrapper implements ServletContext {

  private ServletContext servletContext;

  public ServletContextWrapper(ServletContext servletContext) {
    super();
    this.servletContext = servletContext;
  }

  public Object getAttribute(String arg0) {
    return servletContext.getAttribute(arg0);
  }

  public Enumeration getAttributeNames() {
    return servletContext.getAttributeNames();
  }

  public ServletContext getContext(String arg0) {
    return servletContext.getContext(arg0);
  }

  public String getInitParameter(String arg0) {
    return servletContext.getInitParameter(arg0);
  }

  public Enumeration getInitParameterNames() {
    return servletContext.getInitParameterNames();
  }

  public int getMajorVersion() {
    return servletContext.getMajorVersion();
  }

  public String getMimeType(String arg0) {
    return servletContext.getMimeType(arg0);
  }

  public int getMinorVersion() {
    return servletContext.getMinorVersion();
  }

  public RequestDispatcher getNamedDispatcher(String arg0) {
    return servletContext.getNamedDispatcher(arg0);
  }

  public String getRealPath(String arg0) {
    return servletContext.getRealPath(arg0);
  }

  public RequestDispatcher getRequestDispatcher(String arg0) {
    return servletContext.getRequestDispatcher(arg0);
  }

  public URL getResource(String arg0) throws MalformedURLException {
    return servletContext.getResource(arg0);
  }

  public InputStream getResourceAsStream(String arg0) {
    InputStream is = servletContext.getResourceAsStream(arg0);
    if (is == null) {
      is = this.getClass().getResourceAsStream(arg0);
      if (is == null) {
        int pos = arg0.indexOf("/", 2);
        if (pos > 0) {
          is = this.getClass().getResourceAsStream("/war" + arg0.substring(pos));
        }
      }
    }
    return is;
  }

  public Set getResourcePaths(String arg0) {
    return servletContext.getResourcePaths(arg0);
  }

  public String getServerInfo() {
    return servletContext.getServerInfo();
  }

  public Servlet getServlet(String arg0) throws ServletException {
    return servletContext.getServlet(arg0);
  }

  public String getServletContextName() {
    return servletContext.getServletContextName();
  }

  public Enumeration getServletNames() {
    return servletContext.getServletNames();
  }

  public Enumeration getServlets() {
    return servletContext.getServlets();
  }

  public void log(Exception arg0, String arg1) {
    servletContext.log(arg0, arg1);
  }

  public void log(String arg0, Throwable arg1) {
    servletContext.log(arg0, arg1);
  }

  public void log(String arg0) {
    servletContext.log(arg0);
  }

  public void removeAttribute(String arg0) {
    servletContext.removeAttribute(arg0);
  }

  public void setAttribute(String arg0, Object arg1) {
    servletContext.setAttribute(arg0, arg1);
  }

  public String getContextPath() {
    return servletContext.getContextPath();
  }

  public Dynamic addFilter(String arg0, Class<? extends Filter> arg1) {
    return servletContext.addFilter(arg0, arg1);
  }

  public Dynamic addFilter(String arg0, Filter arg1) {
    return servletContext.addFilter(arg0, arg1);
  }

  public Dynamic addFilter(String arg0, String arg1) {
    return servletContext.addFilter(arg0, arg1);
  }

  public void addListener(Class<? extends EventListener> arg0) {
    servletContext.addListener(arg0);
  }

  public void addListener(String arg0) {
    servletContext.addListener(arg0);
  }

  public <T extends EventListener> void addListener(T arg0) {
    servletContext.addListener(arg0);
  }

  public javax.servlet.ServletRegistration.Dynamic addServlet(String arg0,
    Class<? extends Servlet> arg1) {
    return servletContext.addServlet(arg0, arg1);
  }

  public javax.servlet.ServletRegistration.Dynamic addServlet(String arg0,
    Servlet arg1) {
    return servletContext.addServlet(arg0, arg1);
  }

  public javax.servlet.ServletRegistration.Dynamic addServlet(String arg0,
    String arg1) {
    return servletContext.addServlet(arg0, arg1);
  }

  public <T extends Filter> T createFilter(Class<T> arg0) throws ServletException {
    return servletContext.createFilter(arg0);
  }

  public <T extends EventListener> T createListener(Class<T> arg0)
    throws ServletException {
    return servletContext.createListener(arg0);
  }

  public <T extends Servlet> T createServlet(Class<T> arg0) throws ServletException {
    return servletContext.createServlet(arg0);
  }

  public void declareRoles(String... arg0) {
    servletContext.declareRoles(arg0);
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

  public FilterRegistration getFilterRegistration(String arg0) {
    return servletContext.getFilterRegistration(arg0);
  }

  public Map<String, ? extends FilterRegistration> getFilterRegistrations() {
    return servletContext.getFilterRegistrations();
  }

  public JspConfigDescriptor getJspConfigDescriptor() {
    return servletContext.getJspConfigDescriptor();
  }

  public ServletRegistration getServletRegistration(String arg0) {
    return servletContext.getServletRegistration(arg0);
  }

  public Map<String, ? extends ServletRegistration> getServletRegistrations() {
    return servletContext.getServletRegistrations();
  }

  public SessionCookieConfig getSessionCookieConfig() {
    return servletContext.getSessionCookieConfig();
  }

  public boolean setInitParameter(String arg0, String arg1) {
    return servletContext.setInitParameter(arg0, arg1);
  }

  public void setSessionTrackingModes(Set<SessionTrackingMode> arg0)
    throws IllegalStateException, IllegalArgumentException {
    servletContext.setSessionTrackingModes(arg0);
  }

}
