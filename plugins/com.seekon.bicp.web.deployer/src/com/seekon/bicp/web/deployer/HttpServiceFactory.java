package com.seekon.bicp.web.deployer;

import org.eclipse.equinox.http.servlet.ExtendedHttpService;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

public final class HttpServiceFactory {

  public static ExtendedHttpService getHttpService() {
    BundleContext bundleContext = Activator.instance.bundleContext;
    System.out.println("bundleContext :" + bundleContext);
    ServiceReference reference = bundleContext
      .getServiceReference("org.osgi.service.http.HttpService");
    System.out.println("reference :" + reference);
    ExtendedHttpService httpService = (ExtendedHttpService) bundleContext
      .getService(reference);
    System.out.println("httpService :" + httpService);
    return httpService;
  }
}
