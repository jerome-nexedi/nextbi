package com.seekon.bicp.web.deployer;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleException;
import org.osgi.framework.BundleListener;
import org.osgi.framework.FrameworkEvent;
import org.osgi.framework.FrameworkListener;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.springframework.osgi.context.event.OsgiBundleApplicationContextListener;

import com.seekon.osgi.context.event.WebSupportOsgiBundleApplicationContextListener;

public class Activator implements BundleActivator, BundleListener, FrameworkListener {

	static Activator instance = null;
	
	BundleContext bundleContext = null;
	
	private boolean frameworkStarted = false;
	
	private ServiceReference serviceReference = null;

	@Override
	public void start(BundleContext context) throws Exception {
		instance = this;
		instance.bundleContext = context;

		ServiceRegistration sr = context.registerService(
				OsgiBundleApplicationContextListener.class.getName(),
				new WebSupportOsgiBundleApplicationContextListener(), null);
		serviceReference = sr.getReference();
		
		context.addBundleListener(this);
		context.addFrameworkListener(this);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		context.removeFrameworkListener(this);
		context.removeBundleListener(this);
		context.ungetService(serviceReference);
		serviceReference = null;
	}

	@Override
	public void bundleChanged(BundleEvent event) {
		Bundle bundle = event.getBundle();

		switch (event.getType()) {
		case BundleEvent.RESOLVED: {
			if (frameworkStarted && bundle.getHeaders().get("Context-Path") != null) {
				try {
					bundle.start();
				} catch (BundleException e) {
					e.printStackTrace();
				}
			}
			break;
		}
		case BundleEvent.UNRESOLVED: {// TODO: do nothing
			break;
		}
		default:
			break;
		}
	}

	@Override
	public void frameworkEvent(FrameworkEvent event) {
		if (event.getType() == FrameworkEvent.STARTED) {
      frameworkStarted = true;
      Bundle[] bundles = bundleContext.getBundles();
      for(int i = 0; i < bundles.length; i++){
      	Bundle bundle = bundles[i];
      	if(bundle.getState() == Bundle.RESOLVED && bundle.getHeaders().get("Context-Path") != null){
      		try {
  					bundle.start();
  				} catch (BundleException e) {
  					e.printStackTrace();
  				}
      	}
      }
    }
	}

}
