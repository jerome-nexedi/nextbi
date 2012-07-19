package org.eclipse.equinox.http.servlet.internal;

import java.util.Hashtable;

import javax.servlet.ServletContext;

import org.eclipse.equinox.http.servlet.ExtendedHttpService;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.http.HttpService;

public final class HttpServiceController {
  /**
   * Name of the Framework property indicating whether the servlet context
   * attributes of the ServletContext objects created for each HttpContext used
   * to register servlets and resources share their attributes or not. By
   * default (if this property is not specified or it's value is not
   * <code>true</code> (case-insensitive)) servlet context attributes are not
   * shared. To have servlet context attributes shared amongst servlet context
   * and also with the ServletContext provided by the servlet container ensure
   * setting the property as follows:
   * 
   * <pre>
   * org.apache.felix.http.shared_servlet_context_attributes = true
   * </pre>
   * <p>
   * <b>WARNING:</b> Only set this property if absolutely needed (for example
   * you implement an HttpSessionListener and want to access servlet context
   * attributes of the ServletContext to which the HttpSession is linked).
   * Otherwise leave this property unset.
   */
  private static final String FELIX_HTTP_SHARED_SERVLET_CONTEXT_ATTRIBUTES = "org.apache.felix.http.shared_servlet_context_attributes";

  private static final String[] HTTP_SERVICES_CLASSES = new String[] {
    HttpService.class.getName(), ExtendedHttpService.class.getName() };

  private final BundleContext bundleContext;

  private final Hashtable<String, Object> serviceProps;

  private final boolean sharedContextAttributes;

  private ServiceRegistration serviceReg;

  public HttpServiceController(BundleContext bundleContext) {
    this.bundleContext = bundleContext;
    this.serviceProps = new Hashtable<String, Object>();

    this.sharedContextAttributes = getBoolean(FELIX_HTTP_SHARED_SERVLET_CONTEXT_ATTRIBUTES);
  }

  public void setProperties(Hashtable<String, Object> props) {
    this.serviceProps.clear();
    this.serviceProps.putAll(props);

    if (this.serviceReg != null) {
      this.serviceReg.setProperties(this.serviceProps);
    }
  }

  public void register(ProxyServlet proxy, ServletContext servletContext) {

    HttpServiceFactory factory = new HttpServiceFactory(proxy);
    this.serviceReg = this.bundleContext.registerService(HTTP_SERVICES_CLASSES, factory,
      this.serviceProps);
  }

  public void unregister() {
    if (this.serviceReg == null) {
      return;
    }

    try {
      this.serviceReg.unregister();
    } finally {
      this.serviceReg = null;
    }
  }

  private boolean getBoolean(final String property) {
    String prop = this.bundleContext.getProperty(property);
    return (prop != null) ? Boolean.valueOf(prop).booleanValue() : false;
  }
}
