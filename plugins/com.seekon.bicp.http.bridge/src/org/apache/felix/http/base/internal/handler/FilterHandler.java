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

import java.io.IOException;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.felix.http.base.internal.context.ExtServletContext;

public final class FilterHandler extends AbstractHandler implements
  Comparable<FilterHandler> {
  private final Filter filter;

  // private final Pattern regex;
  private final int ranking;

  private final ClassLoader registeredContextClassLoader;

  private final URLPattern pattern;

  public FilterHandler(ExtServletContext context, Filter filter, String pattern,
    int ranking) {
    super(context);
    this.filter = filter;
    this.ranking = ranking;
    // this.regex = Pattern.compile(pattern);
    this.registeredContextClassLoader = Thread.currentThread()
      .getContextClassLoader();
    this.pattern = this.processURLPattern(pattern);
  }

  public Filter getFilter() {
    return this.filter;
  }

  public void init() throws ServletException {
    String name = null;
    Map<String, String> initParams = getInitParams();
    if (initParams != null) {
      name = initParams.get("filter-name");
    }
    if (name == null || name.trim().length() == 0) {
      name = "filter_" + getId();
    }

    ClassLoader original = Thread.currentThread().getContextClassLoader();
    try {
      Thread.currentThread().setContextClassLoader(registeredContextClassLoader);
      FilterConfig config = new FilterConfigImpl(name, getContext(), initParams);
      this.filter.init(config);
    } finally {
      Thread.currentThread().setContextClassLoader(original);
    }
  }

  public void destroy() {
    ClassLoader original = Thread.currentThread().getContextClassLoader();
    try {
      Thread.currentThread().setContextClassLoader(registeredContextClassLoader);
      this.filter.destroy();
    } finally {
      Thread.currentThread().setContextClassLoader(original);
    }

  }

  public boolean matches(String uri) {
    // assume root if uri is null
    if (uri == null) {
      uri = "/";
    }
    // return this.regex.matcher(uri).matches();

    String prefix = this.pattern.prefix;
    String suffix = this.pattern.suffix;
    if (!uri.startsWith(prefix))
      return false;

    // perfect match
    if (prefix.length() == uri.length())
      return suffix == null;

    // check the next character is a path separator
    if (uri.charAt(prefix.length()) != '/')
      return false;

    // check for an extension match
    if (suffix == null)
      return true;

    return uri.endsWith(suffix) && uri.length() > prefix.length() + suffix.length();
  }

  public void handle(HttpServletRequest req, HttpServletResponse res,
    FilterChain chain) throws ServletException, IOException {
    final boolean matches = matches(req.getPathInfo());
    if (matches) {
      doHandle(req, res, chain);
    } else {
      chain.doFilter(req, res);
    }
  }

  private void doHandle(HttpServletRequest req, HttpServletResponse res,
    FilterChain chain) throws ServletException, IOException {
    if (!getContext().handleSecurity(req, res)) {
      res.sendError(HttpServletResponse.SC_FORBIDDEN);
    } else {
      ClassLoader original = Thread.currentThread().getContextClassLoader();
      try {
        Thread.currentThread().setContextClassLoader(registeredContextClassLoader);
        this.filter.doFilter(req, res, chain);
      } finally {
        Thread.currentThread().setContextClassLoader(original);
      }

    }
  }

  public int compareTo(FilterHandler other) {
    return other.ranking - this.ranking;
  }

  public URLPattern processURLPattern(String alias) {
    if (alias == null || alias.trim().length() == 0) {
      return new URLPattern("/", null);
    }

    String prefix;
    String suffix;
    int lastSlash = alias.lastIndexOf('/');
    String lastSegment = alias.substring(alias.lastIndexOf('/') + 1);
    if (lastSegment.startsWith("*.")) { //$NON-NLS-1$
      prefix = alias.substring(0, lastSlash);
      suffix = lastSegment.substring(1);
    } else {
      prefix = alias.equals("/") ? "" : alias; //$NON-NLS-1$//$NON-NLS-2$
      suffix = null;
    }
    return new URLPattern(prefix, suffix);
  }

  class URLPattern {
    final String prefix;

    final String suffix;

    public URLPattern(String prefix, String suffix) {
      this.prefix = prefix;
      this.suffix = suffix;
    }
  }
}
