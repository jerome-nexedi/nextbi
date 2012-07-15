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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.osgi.service.http.NamespaceException;

public final class HandlerRegistry {
	private final Map<Servlet, List<ServletHandler>> servletMap;
	private final Map<Filter, List<FilterHandler>> filterMap;
	private final Map<String, Servlet> aliasMap;
	private ServletHandler[] servlets;
	private FilterHandler[] filters;

	public HandlerRegistry() {
		this.servletMap = new HashMap<Servlet, List<ServletHandler>>();
		this.filterMap = new HashMap<Filter, List<FilterHandler>>();
		this.aliasMap = new HashMap<String, Servlet>();
		this.servlets = new ServletHandler[0];
		this.filters = new FilterHandler[0];
	}

	public ServletHandler[] getServlets() {
		return this.servlets;
	}

	public FilterHandler[] getFilters() {
		return this.filters;
	}

	public synchronized void addServlet(ServletHandler handler)
			throws ServletException, NamespaceException {
		// //if (this.servletMap.containsKey(servlet)) {
		// //throw new ServletException("Servlet instance already registered");
		// //}

		if (this.aliasMap.containsKey(handler.getAlias())) {
			throw new NamespaceException("Servlet with alias already registered");
		}

		Servlet servlet = handler.getServlet();
		List<ServletHandler> servletHandlerList = this.servletMap.get(servlet);
		if (servletHandlerList == null) {
			servletHandlerList = new ArrayList<ServletHandler>();
		}
		servletHandlerList.add(handler);
		this.servletMap.put(servlet, servletHandlerList);

		handler.init();
		// //this.servletMap.put(handler.getServlet(), handler);
		this.aliasMap.put(handler.getAlias(), handler.getServlet());
		updateServletArray();
	}

	public synchronized void addFilter(FilterHandler handler)
			throws ServletException {
		// //if (this.filterMap.containsKey(filter)) {
		// // throw new ServletException("Filter instance already registered");
		// //}

		Filter filter = handler.getFilter();
		List<FilterHandler> filterHandlerList = this.filterMap.get(filter);
		if (filterHandlerList == null) {
			filterHandlerList = new ArrayList<FilterHandler>();
		}
		filterHandlerList.add(handler);
		this.filterMap.put(filter, filterHandlerList);

		handler.init();
		// //this.filterMap.put(handler.getFilter(), handler);
		updateFilterArray();
	}

	public synchronized void removeServlet(Servlet servlet, final boolean destroy) {
		List<ServletHandler> handlerList = this.servletMap.remove(servlet);
		if (handlerList != null) {
			updateServletArray();
			for (ServletHandler handler : handlerList) {
				this.aliasMap.remove(handler.getAlias());
				if (destroy) {
					handler.destroy();
				}
			}
		}
	}

	public synchronized void removeFilter(Filter filter, final boolean destroy) {
		List<FilterHandler> handlerList = this.filterMap.remove(filter);
		if (handlerList != null) {
			updateFilterArray();
			for (FilterHandler handler : handlerList) {
				if (destroy) {
					handler.destroy();
				}
			}
		}
	}

	public synchronized Servlet getServletByAlias(String alias) {
		return this.aliasMap.get(alias);
	}

	public synchronized void removeAll() {
		for (List<ServletHandler> handlerList : this.servletMap.values()) {
			for(ServletHandler handler : handlerList){
				handler.destroy();
			}
		}

		for (List<FilterHandler> handlerList : this.filterMap.values()) {
			for(FilterHandler handler: handlerList){
				handler.destroy();
			}
		}

		this.servletMap.clear();
		this.filterMap.clear();
		this.aliasMap.clear();

		updateServletArray();
		updateFilterArray();
	}

	private void updateServletArray() {
		List<ServletHandler> allServletHandlerList = new ArrayList<ServletHandler>();
		Collection<List<ServletHandler>> collection = this.servletMap.values();
		if(collection != null){
			Iterator<List<ServletHandler>> iterator = collection.iterator();
			while(iterator.hasNext()){
				List<ServletHandler> tmp = iterator.next();
				if(tmp != null){
					allServletHandlerList.addAll(tmp);
				}
			}
		}
		
		ServletHandler[] tmp = allServletHandlerList.toArray(
				new ServletHandler[allServletHandlerList.size()]);
		Arrays.sort(tmp);
		this.servlets = tmp;
	}

	private void updateFilterArray() {
		List<FilterHandler> allFilterHandlerList = new ArrayList<FilterHandler>();
		Collection<List<FilterHandler>> collection = this.filterMap.values();
		if(collection != null){
			Iterator<List<FilterHandler>> iterator = collection.iterator();
			while(iterator.hasNext()){
				List<FilterHandler> tmp = iterator.next();
				if(tmp != null){
					allFilterHandlerList.addAll(tmp);
				}
			}
		}
		
		FilterHandler[] tmp = allFilterHandlerList.toArray(
				new FilterHandler[allFilterHandlerList.size()]);
		Arrays.sort(tmp);
		this.filters = tmp;
	}
}
