package com.seekon.bicp.web.deployer;

import org.eclipse.equinox.http.servlet.ExtendedHttpService;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

public final class HttpServiceFactory {

  public static ExtendedHttpService getHttpService() {
    BundleContext bundleContext = Activator.instance.bundleContext;
    ServiceReference reference = bundleContext
      .getServiceReference("org.osgi.service.http.HttpService");
    ExtendedHttpService httpService = (ExtendedHttpService) bundleContext
      .getService(reference);
    return httpService;
  }
}
