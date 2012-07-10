package com.seekon.bicp.osgi.bridge;

import javax.servlet.ServletContext;

public class GlobalWebContextHelper {

	private static ServletContext t_servletConext;

	public static void setGlobalServletContext(ServletContext sc) {
		t_servletConext = sc;
	}

	public static ServletContext getGlobalServletContext() {
		return t_servletConext;
	}
}
