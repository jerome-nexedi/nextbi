package com.seekon.bicp.osgi.bridge.internal;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;

import org.apache.felix.framework.util.Util;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.wiring.BundleRevision;
import org.osgi.framework.wiring.FrameworkWiring;

public final class ProvisionActivator implements BundleActivator {
  private final ServletContext servletContext;

  public ProvisionActivator(ServletContext servletContext) {
    this.servletContext = servletContext;
  }

  public void start(BundleContext context) throws Exception {
    servletContext.setAttribute(BundleContext.class.getName(), context);

    List<Bundle> installed = new ArrayList<Bundle>();
    for (URL url : findBundles()) {
      log("Installing bundle [" + url + "]");
      Bundle bundle = context.installBundle(url.toExternalForm());
      installed.add(bundle);
    }

    Bundle systemBundle = context.getBundle(0);
    if (systemBundle == null
      || !systemBundle.getLocation().equalsIgnoreCase("System Bundle")) {
      log("The system bundle is unavailable.");
      return;
    }

    log("start to resolve all installed bundles.");
    FrameworkWiring fWiring = systemBundle.adapt(FrameworkWiring.class);
    fWiring.resolveBundles(installed);
    //    ServiceReference<PackageAdmin> sr = context.getServiceReference(PackageAdmin.class);
    //    context.getService(sr).resolveBundles(installed.toArray(new Bundle[installed.size()]));
    log("resolved all installed bundles.");

    for (Bundle bundle : installed) {
      if (!Util.isFragment(bundle.adapt(BundleRevision.class))) {
        String activator = bundle.getHeaders().get("Bundle-Activator");
        if (activator != null && bundle.getState() != Bundle.ACTIVE) {
          try {
            log("starting bundle:" + bundle.getSymbolicName());
            bundle.start();
          } catch (Throwable e) {
            e.printStackTrace();
          }
        }
      }
    }

  }

  public void stop(BundleContext context) throws Exception {
  }

  private List<URL> findBundles() throws Exception {
    ArrayList<URL> list = new ArrayList<URL>();
    String[] pathes = new String[] { "/WEB-INF/plugins/", "/WEB-INF/patches/" };
    for (String path : pathes) {
      for (Object o : this.servletContext.getResourcePaths(path)) {
        String name = (String) o;
        if (name.endsWith(".jar")) {
          URL url = this.servletContext.getResource(name);
          if (url != null) {
            list.add(url);
          }
        }
      }
    }
    return list;
  }

  private void log(String message) {
    this.servletContext.log(message);
    System.out.println(new Date().toLocaleString() + "  " + message);
  }
}
