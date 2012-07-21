/*******************************************************************************
 * Copyright (c) 2005, 2007 Cognos Incorporated, IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Cognos Incorporated - initial API and implementation
 *     IBM Corporation - bug fixes and enhancements
 *******************************************************************************/

package org.eclipse.equinox.http.servlet.internal;

import java.util.Hashtable;

import javax.servlet.Servlet;
import javax.servlet.http.HttpServlet;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class Activator implements BundleActivator {

  private BundleContext context;

  private ServiceRegistration serviceRegistration;

  public void start(BundleContext bundleContext) throws Exception {
    this.context = bundleContext;

    Servlet proxyServlet = new ProxyServlet(new HttpServiceController(bundleContext));
    Hashtable props = new Hashtable();
    props.put("http.felix.dispatcher", proxyServlet.getClass().getName());
    props.put("service.description", "Dispatcher for bridged request handling");
    props.put("service.vendor", "The Apache Software Foundation");
    serviceRegistration = bundleContext.registerService(HttpServlet.class.getName(),
      proxyServlet, props);

    System.out.println("Started bridged http service");
  }

  public void stop(BundleContext bundleContext) throws Exception {
    serviceRegistration.unregister();
    this.context = null;
  }

}
