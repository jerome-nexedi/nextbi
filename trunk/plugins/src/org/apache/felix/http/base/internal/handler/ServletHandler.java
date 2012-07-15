/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.felix.http.base.internal.handler;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.felix.http.base.internal.context.ExtServletContext;
import java.io.IOException;
import java.util.Map;

public final class ServletHandler extends AbstractHandler implements
		Comparable<ServletHandler> {
	private final String alias;
	private final Servlet servlet;
	private final ClassLoader registeredContextClassLoader;

	public ServletHandler(ExtServletContext context, Servlet servlet, String alias) {
		super(context);
		this.alias = alias;
		this.servlet = servlet;
		this.registeredContextClassLoader = Thread.currentThread()
				.getContextClassLoader();
	}

	public String getAlias() {
		return this.alias;
	}

	public Servlet getServlet() {
		return this.servlet;
	}

	public void init() throws ServletException {
		String name = null;
		Map<String, String> initParams = getInitParams();
		if (initParams != null) {
			name = initParams.get("servlet-name");
		}
		if (name == null || name.trim().length() == 0) {
			name = "servlet_" + getId();
		}

		ClassLoader original = Thread.currentThread().getContextClassLoader();
		try {
			Thread.currentThread()
					.setContextClassLoader(registeredContextClassLoader);
			ServletConfig config = new ServletConfigImpl(name, getContext(),
					getInitParams());
			this.servlet.init(config);
		} finally {
			Thread.currentThread().setContextClassLoader(original);
		}
	}

	public void destroy() {
		ClassLoader original = Thread.currentThread().getContextClassLoader();
		try {
			Thread.currentThread()
					.setContextClassLoader(registeredContextClassLoader);
			this.servlet.destroy();
		} finally {
			Thread.currentThread().setContextClassLoader(original);
		}
	}

	public boolean matches(String uri) {
		return getMatchedAlias(uri) != null;
	}

	// //TODO:changed 2012-07-10
	public String getMatchedAlias(String uri) {
		if (uri == null && this.alias.equals("/")) {
			return this.alias;
		} else if (this.alias.equals("/") && uri.startsWith(this.alias)) {
			return this.alias;
		} else if (this.alias.equals(uri)) {
			return this.alias;
		} else {

			String extensionAlias = findExtensionAlias(uri);
			uri = uri.substring(0, uri.lastIndexOf('/'));
			boolean match = false;

			// longest path match
			while (uri.length() != 0) {
				if (extensionAlias == null) {
					match = this.alias.equals(uri);
				} else {
					match = this.alias.equals(uri + extensionAlias);
					if (!match) {
						match = this.alias.equals(uri);
					}
				}
				if (match) {
					return uri;
				}

				uri = uri.substring(0, uri.lastIndexOf('/'));
			}
		}
		return null;
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

	public boolean handle(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		final String matchedAlias = this.getMatchedAlias(req.getPathInfo());
		if (matchedAlias != null) {
			doHandle(req, res, matchedAlias);
		}

		return matchedAlias != null;
	}

	private void doHandle(HttpServletRequest req, HttpServletResponse res,
			String matchedAlias) throws ServletException, IOException {
		// set a sensible status code in case handleSecurity returns false
		// but fails to send a response
		res.setStatus(HttpServletResponse.SC_FORBIDDEN);
		if (getContext().handleSecurity(req, res)) {
			// reset status to OK for further processing
			res.setStatus(HttpServletResponse.SC_OK);
			ClassLoader original = Thread.currentThread().getContextClassLoader();
			try {
				Thread.currentThread().setContextClassLoader(
						registeredContextClassLoader);
				this.servlet.service(new ServletHandlerRequest(req, matchedAlias,
						this.servlet), res);
			} finally {
				Thread.currentThread().setContextClassLoader(original);
			}

		}
	}

	public int compareTo(ServletHandler other) {
		return other.alias.length() - this.alias.length();
	}
}
