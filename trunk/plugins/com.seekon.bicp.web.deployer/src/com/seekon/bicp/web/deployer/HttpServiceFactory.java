package com.seekon.bicp.web.deployer;

import org.apache.felix.http.api.ExtHttpService;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

public final class HttpServiceFactory {

  public static ExtHttpService getHttpService() {
    BundleContext bundleContext = Activator.instance.bundleContext;
    System.out.println("bundleContext :" + bundleContext);
    ServiceReference reference = bundleContext
      .getServiceReference("org.osgi.service.http.HttpService");
    System.out.println("reference :" + reference);
    ExtHttpService httpService = (ExtHttpService) bundleContext
      .getService(reference);
    System.out.println("httpService :" + httpService);
    return httpService;
  }
}
