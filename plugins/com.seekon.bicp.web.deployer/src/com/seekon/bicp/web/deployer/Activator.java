package com.seekon.bicp.web.deployer;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleException;
import org.osgi.framework.BundleListener;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.http.HttpService;
import org.springframework.osgi.context.event.OsgiBundleApplicationContextListener;

import com.seekon.osgi.context.event.WebSupportOsgiBundleApplicationContextListener;

public class Activator implements BundleActivator, BundleListener {

	static Activator instance = null;

	BundleContext bundleContext = null;

	private ServiceReference serviceReference = null;

	private HttpServiceTracker httpServiceTracker = null;

	@Override
	public void start(BundleContext context) throws Exception {
		instance = this;
		instance.bundleContext = context;

		ServiceRegistration sr = context.registerService(
				OsgiBundleApplicationContextListener.class.getName(),
				new WebSupportOsgiBundleApplicationContextListener(), null);
		serviceReference = sr.getReference();

		context.addBundleListener(this);

		httpServiceTracker = new HttpServiceTracker(context);
		httpServiceTracker.open();
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		httpServiceTracker.close();
		context.removeBundleListener(this);
		context.ungetService(serviceReference);
		serviceReference = null;
	}

	@Override
	public void bundleChanged(BundleEvent event) {	
		HttpService httpService = httpServiceTracker.getHttpService();
		if(httpService == null){
			return;
		}
		
		Bundle bundle = event.getBundle();
		switch (event.getType()) {
		case BundleEvent.RESOLVED: {
			if (bundle.getHeaders().get("Context-Path") != null) {
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
}
