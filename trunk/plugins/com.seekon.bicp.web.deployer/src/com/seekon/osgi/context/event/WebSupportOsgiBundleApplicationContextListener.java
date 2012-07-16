package com.seekon.osgi.context.event;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.osgi.framework.Bundle;
import org.springframework.osgi.context.event.OsgiBundleApplicationContextEvent;
import org.springframework.osgi.context.event.OsgiBundleApplicationContextListener;
import org.springframework.osgi.context.event.OsgiBundleContextClosedEvent;
import org.springframework.osgi.context.event.OsgiBundleContextRefreshedEvent;
import org.springframework.osgi.web.context.support.OsgiBundleXmlWebApplicationContext;

import com.seekon.bicp.web.deployer.LoadException;
import com.seekon.bicp.web.deployer.ModuleLoader;
import com.seekon.bicp.web.deployer.def.WebAppLoader;

public class WebSupportOsgiBundleApplicationContextListener implements
  OsgiBundleApplicationContextListener {

  private static Logger log = Logger
    .getLogger(WebSupportOsgiBundleApplicationContextListener.class);

  private List<ModuleLoader> moduleLoaders = new ArrayList<ModuleLoader>();

  private List<Bundle> registeredBundles = new ArrayList<Bundle>();

  public WebSupportOsgiBundleApplicationContextListener() {
    super();
    moduleLoaders.add(new WebAppLoader());
  }

  @Override
  public void onOsgiApplicationEvent(OsgiBundleApplicationContextEvent event) {
    if (event instanceof OsgiBundleContextRefreshedEvent) {
      try {
        OsgiBundleXmlWebApplicationContext context = (OsgiBundleXmlWebApplicationContext) event
          .getApplicationContext();
        if (context != null && context.getServletContext() != null) {
          context.getServletContext().setAttribute(
            "context_" + event.getBundle().getSymbolicName(), context);
        }
        registerApplication(event.getBundle());
      } catch (Exception e) {
        log.debug(e);
      }
    }
    if (event instanceof OsgiBundleContextClosedEvent) {
      try {
        unregisterApplication(event.getBundle());
      } catch (Exception e) {
        log.debug(e);
      }
    }
  }

  private void registerApplication(Bundle bundle) throws Exception {
    if (registeredBundles.contains(bundle)) {
      return;
    }

    List succLoaders = new ArrayList();
    for (Iterator it = moduleLoaders.iterator(); it.hasNext();) {
      ModuleLoader loader = (ModuleLoader) it.next();

      try {
        loader.load(bundle);
        succLoaders.add(loader);
      } catch (LoadException e) {
        for (Iterator it1 = succLoaders.iterator(); it1.hasNext();) {
          ModuleLoader l = (ModuleLoader) it1.next();
          try {
            l.unload(bundle);
          } catch (LoadException e1) {
            log.error(e.getMessage(), e);
          }
        }
        log.error(e.getMessage(), e);
        throw e;
      }
    }
    registeredBundles.add(bundle);
  }

  private void unregisterApplication(Bundle bundle) throws Exception {
    if (!registeredBundles.contains(bundle)) {
      return;
    }

    for (Iterator it = moduleLoaders.iterator(); it.hasNext();) {
      ModuleLoader loader = (ModuleLoader) it.next();
      try {
        loader.unload(bundle);
      } catch (LoadException e) {
        log.error(e.getMessage(), e);
      }
    }
    registeredBundles.remove(bundle);
  }
}
