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

import java.io.IOException;
import java.security.AccessController;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import org.eclipse.equinox.http.helper.ContextPathServletAdaptor;
import org.osgi.framework.BundleContext;
import org.osgi.service.http.HttpContext;
import org.osgi.service.http.NamespaceException;

/**
 * The ProxyServlet is the private side of a Servlet that when registered (and init() called) in a servlet container
 * will in-turn register and provide an OSGi Http Service implementation.
 * This class is not meant for extending or even using directly and is purely meant for registering
 * in a servlet container.
 */
public class ProxyServlet extends HttpServlet {
	public static final String BUNDLE_CONTEXT_ATTRIBUTE = BundleContext.class.getName();

	private static final long serialVersionUID = 4117456123807468871L;
	private Map servletRegistrations = new HashMap(); //alias --> servlet registration
	private Map registeredServlets = new HashMap(); //All the servlets objects that have been registered 

	private Map filterRegistrations = new LinkedHashMap(); //filter --> filter registration;
	private ProxyContext proxyContext;

	private Map registeredServletRequestListeners = new LinkedHashMap();

	private final HttpServiceController controller;
	
	public ProxyServlet(HttpServiceController controller){
	  this.controller = controller;
	}
	
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		proxyContext = new ProxyContext(config.getServletContext());
		this.controller.register(this, getServletContext());
	}

	public void destroy() {
	  this.controller.unregister();
		proxyContext.destroy();
		proxyContext = null;
		super.destroy();
	}

	/**
	 * @see HttpServlet#service(ServletRequest, ServletResponse)
	 */
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		proxyContext.initializeServletPath(req);
		String alias = HttpServletRequestAdaptor.getDispatchPathInfo(req);
		System.out.println(alias);
		if (alias == null) {
			alias = "/"; //$NON-NLS-1$
		}

		// perfect match
		if (processAlias(req, resp, alias, null))
			return;

		String extensionAlias = findExtensionAlias(alias);
		alias = alias.substring(0, alias.lastIndexOf('/'));

		// longest path match
		while (alias.length() != 0) {
			if (processAlias(req, resp, alias, extensionAlias))
				return;
			alias = alias.substring(0, alias.lastIndexOf('/'));
		}

		// default handler match
		if (extensionAlias != null)
			extensionAlias = extensionAlias.substring(1); // remove the leading '/'
		if (processAlias(req, resp, "/", extensionAlias)) //Handle '/' aliases //$NON-NLS-1$
			return;
		resp.sendError(HttpServletResponse.SC_NOT_FOUND, "ProxyServlet: " + req.getRequestURI()); //$NON-NLS-1$
	}

	private String findExtensionAlias(String alias) {
		String lastSegment = alias.substring(alias.lastIndexOf('/') + 1);
		int dot = lastSegment.indexOf('.');
		if (dot == -1)
			return null;
		String extension = lastSegment.substring(dot + 1);
		if (extension.length() == 0)
			return null;
		return "/*." + extension; //$NON-NLS-1$
	}

	private boolean processAlias(HttpServletRequest req, HttpServletResponse resp, String alias, String extensionAlias) throws ServletException, IOException {
		ServletRegistration registration = null;
		List matchingFilterRegistrations = Collections.EMPTY_LIST;
		String dispatchPathInfo = HttpServletRequestAdaptor.getDispatchPathInfo(req);
		synchronized (this) {
			if (extensionAlias == null)
				registration = (ServletRegistration) servletRegistrations.get(alias);
			else {
				registration = (ServletRegistration) servletRegistrations.get(alias + extensionAlias);
				if (registration != null) {
					// for regular ServletRegistrations extensions should be handled on the full alias
					if (!(registration.getServlet() instanceof ResourceServlet) && !(registration.getServlet() instanceof ContextPathServletAdaptor))
						alias = HttpServletRequestAdaptor.getDispatchPathInfo(req);
				} else
					registration = (ServletRegistration) servletRegistrations.get(alias);
			}
			if (registration != null) {
				registration.addReference();
				if (!filterRegistrations.isEmpty()) {
					matchingFilterRegistrations = new ArrayList();
					for (Iterator it = filterRegistrations.values().iterator(); it.hasNext();) {
						FilterRegistration filterRegistration = (FilterRegistration) it.next();
						if (dispatchPathInfo != null && filterRegistration.matches(dispatchPathInfo)) {
							matchingFilterRegistrations.add(filterRegistration);
							filterRegistration.addReference();
						}
						if (filterRegistration.matchServletName(registration.getServletName())) {
							matchingFilterRegistrations.add(filterRegistration);
							filterRegistration.addReference();
						}
					}
				}
			}
		}

		if (registration != null) {
			HttpServletRequest wrappedRequest = new HttpServletRequestAdaptor(req, alias, registration.getServlet());
			ServletContext servletContext = wrappedRequest.getSession().getServletContext();
			ServletRequestEvent event = new ServletRequestEvent(servletContext, wrappedRequest);
			Set matchingServletRequestListeners = null;
			if (!registeredServletRequestListeners.isEmpty()) {
				matchingServletRequestListeners = (Set) registeredServletRequestListeners.get(servletContext.getContextPath());
			}

			try {
				if (matchingServletRequestListeners != null && !matchingServletRequestListeners.isEmpty()) {
					Iterator iterator = matchingServletRequestListeners.iterator();
					while (iterator.hasNext()) {
						ServletRequestListener listener = (ServletRequestListener) iterator.next();
						listener.requestInitialized(event);
					}
				}

				if (matchingFilterRegistrations.isEmpty()) {
					registration.service(wrappedRequest, resp);
				} else {
					Collections.sort(matchingFilterRegistrations);
					FilterChain chain = new FilterChainImpl(matchingFilterRegistrations, registration);
					chain.doFilter(wrappedRequest, resp);
				}
			} finally {
				registration.removeReference();
				for (Iterator it = matchingFilterRegistrations.iterator(); it.hasNext();) {
					FilterRegistration filterRegistration = (FilterRegistration) it.next();
					filterRegistration.removeReference();
				}

				if (matchingServletRequestListeners != null && !matchingServletRequestListeners.isEmpty()) {
					Iterator iterator = matchingServletRequestListeners.iterator();
					while (iterator.hasNext()) {
						ServletRequestListener listener = (ServletRequestListener) iterator.next();
						listener.requestDestroyed(event);
					}
				}
			}
			return true;
		}
		return false;
	}

	//Effective unregistration of servlet and resources as defined in HttpService#unregister()
	synchronized void unregister(String alias, boolean destroy) {
		ServletRegistration removedRegistration = (ServletRegistration) servletRegistrations.remove(alias);
		if (removedRegistration != null) {
			registeredServlets.remove(removedRegistration.getServlet());
			try {
				if (destroy) {
					removedRegistration.destroy();
				}
			} finally {
				proxyContext.destroyContextAttributes(removedRegistration.getHttpContext());
			}
		}
	}

	//Effective registration of the servlet as defined HttpService#registerServlet()  
	synchronized void registerServlet(String alias, Servlet servlet, Dictionary initparams, HttpContext httpContext) throws ServletException, NamespaceException {
		checkAlias(alias);
		if (servletRegistrations.containsKey(alias))
			throw new NamespaceException("The alias '" + alias + "' is already in use."); //$NON-NLS-1$//$NON-NLS-2$

		if (servlet == null)
			throw new IllegalArgumentException("Servlet cannot be null"); //$NON-NLS-1$

		if (registeredServlets.containsKey(servlet)) {
			////throw new ServletException("This servlet has already been registered."); //$NON-NLS-1$
			ServletRegistration registration = (ServletRegistration) registeredServlets.get(servlet);
			servletRegistrations.put(alias, registration);
			return;
		}

		ServletRegistration registration = new ServletRegistration(servlet, httpContext);
		ServletContext wrappedServletContext = new ServletContextAdaptor(proxyContext, getServletContext(), httpContext, AccessController.getContext(), initparams);
		ServletConfig servletConfig = new ServletConfigImpl(servlet, initparams, wrappedServletContext);

		boolean initialized = false;
		proxyContext.createContextAttributes(httpContext);
		try {
			registration.init(servletConfig);
			initialized = true;
		} finally {
			if (!initialized)
				proxyContext.destroyContextAttributes(httpContext);
		}
		registeredServlets.put(servlet, registration);
		servletRegistrations.put(alias, registration);
	}

	//Effective registration of the resources as defined HttpService#registerResources()  
	synchronized void registerResources(String alias, String name, HttpContext httpContext) throws NamespaceException {
		checkName(name);
		Servlet resourceServlet = new ResourceServlet(name, httpContext, AccessController.getContext());
		try {
			registerServlet(alias, resourceServlet, null, httpContext);
		} catch (ServletException e) {
			throw new IllegalStateException("Unexpected ServletException throw when registering resources at alias " + alias + "."); //$NON-NLS-1$//$NON-NLS-2$
		}
	}

	private void checkName(String name) {
		if (name == null)
			throw new IllegalArgumentException("Name cannot be null"); //$NON-NLS-1$

		if (name.endsWith("/") && !name.equals("/")) //$NON-NLS-1$ //$NON-NLS-2$
			throw new IllegalArgumentException("Invalid Name '" + name + "'"); //$NON-NLS-1$//$NON-NLS-2$		
	}

	private void checkAlias(String alias) {
		if (alias == null)
			throw new IllegalArgumentException("Alias cannot be null"); //$NON-NLS-1$

		if (!alias.startsWith("/") || (alias.endsWith("/") && !alias.equals("/"))) //$NON-NLS-1$ //$NON-NLS-2$//$NON-NLS-3$
			throw new IllegalArgumentException("Invalid alias '" + alias + "'"); //$NON-NLS-1$//$NON-NLS-2$
	}

	public synchronized void unregisterFilter(Filter filter, boolean destroy) {
		FilterRegistration removedRegistration = (FilterRegistration) filterRegistrations.remove(filter);
		if (removedRegistration != null) {
			try {
				if (destroy)
					removedRegistration.destroy();
			} finally {
				proxyContext.destroyContextAttributes(removedRegistration.getHttpContext());
			}
		}
	}

	public synchronized void registerFilter(String alias, String servletName, Filter filter, Dictionary initparams, HttpContext httpContext) throws ServletException {
		////checkAlias(alias);
		if (filter == null)
			throw new IllegalArgumentException("Filter cannot be null"); //$NON-NLS-1$

		if (filterRegistrations.containsKey(filter)) {
			////throw new ServletException("This filter has already been registered."); //$NON-NLS-1$
			FilterRegistration registration = (FilterRegistration) filterRegistrations.get(filter);
			registration.addURLPattern(alias);
			registration.addMatchServletName(servletName);
			return;
		}

		int filterPriority = findFilterPriority(initparams);
		FilterRegistration registration = new FilterRegistration(filter, httpContext, alias, servletName, filterPriority);
		ServletContext wrappedServletContext = new ServletContextAdaptor(proxyContext, getServletContext(), httpContext, AccessController.getContext());
		FilterConfig filterConfig = new FilterConfigImpl(filter, initparams, wrappedServletContext);

		boolean initialized = false;
		proxyContext.createContextAttributes(httpContext);
		try {
			registration.init(filterConfig);
			initialized = true;
		} finally {
			if (!initialized)
				proxyContext.destroyContextAttributes(httpContext);
		}
		filterRegistrations.put(filter, registration);
	}

	private int findFilterPriority(Dictionary initparams) {
		if (initparams == null)
			return 0;
		String filterPriority = (String) initparams.get("filter-priority"); //$NON-NLS-1$
		if (filterPriority == null)
			return 0;

		try {
			int result = Integer.parseInt(filterPriority);
			if (result >= -1000 && result <= 1000)
				return result;
		} catch (NumberFormatException e) {
			// fall through
		}
		throw new IllegalArgumentException("filter-priority must be an integer between -1000 and 1000 but was: " + filterPriority); //$NON-NLS-1$
	}

	public void registerServletContextListeners(ServletContextListener listener, Dictionary initParameter, HttpContext context, BundleContext bundleContext) {
		proxyContext.createContextAttributes(context);
		ServletContext wrappedServletContext = new ServletContextAdaptor(proxyContext, getServletContext(), context, AccessController.getContext(), initParameter);
		wrappedServletContext.setAttribute(BUNDLE_CONTEXT_ATTRIBUTE, bundleContext);
		listener.contextInitialized(new ServletContextEvent(wrappedServletContext));
	}

	public void unregisterServletContextListeners(ServletContextListener listener, HttpContext context) {
		proxyContext.createContextAttributes(context);
		ServletContext wrappedServletContext = new ServletContextAdaptor(proxyContext, getServletContext(), context, AccessController.getContext());
		listener.contextDestroyed(new ServletContextEvent(wrappedServletContext));
	}

	public void registerServletRequestListener(ServletRequestListener listener, String contextPath) {
		Set listenerList = (Set) this.registeredServletRequestListeners.get(contextPath);
		if (listenerList == null) {
			listenerList = new HashSet();
		}
		listenerList.add(listener);
		this.registeredServletRequestListeners.put(contextPath, listenerList);
	}

	public void unregisterServletRequestListeners(String contextPath) {
		this.registeredServletRequestListeners.remove(contextPath);
	}
}
