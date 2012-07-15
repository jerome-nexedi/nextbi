package com.seekon.bicp.http.bridge.internal;

import java.util.EventListener;
import java.util.Hashtable;

import javax.servlet.http.HttpServlet;

import org.apache.felix.http.base.internal.AbstractHttpActivator;
import org.apache.felix.http.base.internal.logger.SystemLogger;

public final class BridgeActivator extends AbstractHttpActivator {
	protected void doStart() throws Exception {
		super.doStart();

		Hashtable props = new Hashtable();
		props.put("http.felix.dispatcher", getDispatcherServlet().getClass()
				.getName());
		props.put("service.description", "Dispatcher for bridged request handling");
		props.put("service.vendor", "The Apache Software Foundation");
		getBundleContext().registerService(HttpServlet.class.getName(),
				getDispatcherServlet(), props);

		props = new Hashtable();
		props.put("http.felix.dispatcher", getEventDispatcher().getClass()
				.getName());
		props.put("service.description",
				"Dispatcher for bridged HttpSession events");
		props.put("service.vendor", "The Apache Software Foundation");
		getBundleContext().registerService(EventListener.class.getName(),
				getEventDispatcher(), props);

		SystemLogger.info("Started bridged http service");
	}
}
