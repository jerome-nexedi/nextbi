package com.seekon.bicp.springsupport.web.servlet;

import javax.servlet.ServletContext;

import org.springframework.context.ApplicationContext;
import org.springframework.osgi.web.context.support.OsgiBundleXmlWebApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

public class OsgiSupportDispatcherServlet extends DispatcherServlet {

  private static final long serialVersionUID = 5001619869890003091L;

  @Override
  protected WebApplicationContext createWebApplicationContext(
    ApplicationContext parent) {
    ServletContext servletContext = getServletContext();
    if (parent == null && servletContext != null) {
      String bundleName = servletContext.getInitParameter("bundle.name");
      parent = (ApplicationContext) servletContext.getAttribute("context_"
        + bundleName);
    }

    if (!(parent instanceof OsgiBundleXmlWebApplicationContext)) {
      throw new RuntimeException(
        "parent must be the instance of OsgiBundleXmlWebApplicationContext.");
    }

    OsgiBundleXmlWebApplicationContext _parent = (OsgiBundleXmlWebApplicationContext) parent;
    OsgiBundleXmlWebApplicationContext wac = new OsgiBundleXmlWebApplicationContext();
    wac.setParent(_parent);
    wac.setConfigLocation(getContextConfigLocation());
    wac.setBundleContext(_parent.getBundleContext());
    wac.setServletContext(_parent.getServletContext());

    configureAndRefreshWebApplicationContext(wac);

    return wac;
  }

}
