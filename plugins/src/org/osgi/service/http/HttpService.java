package org.osgi.service.http;

import java.util.Dictionary;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

public abstract interface HttpService {
	public abstract void registerServlet(String paramString,
			Servlet paramServlet, Dictionary paramDictionary,
			HttpContext paramHttpContext) throws ServletException, NamespaceException;

	public abstract void registerResources(String paramString1,
			String paramString2, HttpContext paramHttpContext)
			throws NamespaceException;

	public abstract void unregister(String paramString);

	public abstract HttpContext createDefaultHttpContext();
}