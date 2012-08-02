package com.seekon.bicp.osgi.bridge.internal;

import java.net.URL;
import java.util.ArrayList;
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
      this.servletContext.log("Installing bundle [" + url + "]");
      Bundle bundle = context.installBundle(url.toExternalForm());
      installed.add(bundle);
    }
    
    Bundle systemBundle = context.getBundle(0);//SystemBundleµÄidÄ¬ÈÏÎª0£¬¡¶osgi.r4.code¡· P92
    if (systemBundle == null || !systemBundle.getLocation().equalsIgnoreCase("System Bundle")) {
      System.out.println("The system bundle is unavailable.");
      return;
    }
    FrameworkWiring fWiring = systemBundle.adapt(FrameworkWiring.class);
    fWiring.resolveBundles(installed);
    for (Bundle bundle : installed) {
      if (!Util.isFragment(bundle.adapt(BundleRevision.class))) {
        String activator = bundle.getHeaders().get("Bundle-Activator");
        if (activator != null && bundle.getState() != Bundle.ACTIVE) {
          try {
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
    for (Object o : this.servletContext.getResourcePaths("/WEB-INF/plugins/")) {
      String name = (String) o;
      if (name.endsWith(".jar")) {
        URL url = this.servletContext.getResource(name);
        if (url != null) {
          list.add(url);
        }
      }
    }

    return list;
  }
}
