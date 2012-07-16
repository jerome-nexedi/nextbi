package com.seekon.bicp.web.deployer;

import org.apache.felix.http.api.ExtHttpService;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;

public class HttpServiceTracker extends ServiceTracker {

  private BundleContext context = null;

  private ExtHttpService httpService = null;

  public HttpServiceTracker(BundleContext context) throws Exception {
    super(context, context
      .createFilter("(objectClass=org.osgi.service.http.HttpService)"), null);
    this.context = context;
  }

  @Override
  public Object addingService(ServiceReference reference) {
    httpService = (ExtHttpService) this.context.getService(reference);
    startWebBundles();

    return httpService;
  }

  @Override
  public void modifiedService(ServiceReference reference, Object service) {
    super.modifiedService(reference, service);
  }

  @Override
  public void removedService(ServiceReference arg0, Object arg1) {

  }

  public ExtHttpService getHttpService() {
    return httpService;
  }

  /**
   * 当httpService可用的时候启动所有web插件
   */
  private void startWebBundles() {
    Bundle[] bundles = context.getBundles();
    for (Bundle bundle : bundles) {
      if (bundle.getState() == Bundle.RESOLVED
        && bundle.getHeaders().get("Context-Path") != null) {
        try {
          bundle.start();
        } catch (BundleException e) {
          e.printStackTrace();
        }
      }
    }
  }
}
