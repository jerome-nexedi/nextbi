package org.eclipse.equinox.http.servlet.internal;

import java.io.IOException;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.osgi.service.http.HttpContext;

//This class wraps the filter object registered in the HttpService.registerFilter call, to manage the context classloader when handleRequests are being asked.
public class FilterRegistration extends Registration implements Comparable {

  private static long nextSequenceNumber = 1L;

  private final Filter filter; //The actual filter object registered against the http service. All filter requests will eventually be delegated to it.

  private final HttpContext httpContext; //The context used during the registration of the filter

  private final ClassLoader registeredContextClassLoader;

  private final Set urlPatternSet = new HashSet();

  private final Set servletNameSet = new HashSet();

  //private final String prefix;
  //private final String suffix;
  private final int priority;

  private final long sequenceNumber;

  public FilterRegistration(Filter filter, HttpContext context, String alias,
    String servletName, int priority) {
    this.filter = filter;
    this.httpContext = context;
    registeredContextClassLoader = Thread.currentThread().getContextClassLoader();

    addURLPattern(alias);
    this.addMatchServletName(servletName);

    this.priority = priority;
    synchronized (FilterRegistration.class) {
      this.sequenceNumber = nextSequenceNumber++;
    }
  }

  public void destroy() {
    ClassLoader original = Thread.currentThread().getContextClassLoader();
    try {
      Thread.currentThread().setContextClassLoader(registeredContextClassLoader);
      super.destroy();
      filter.destroy();
    } finally {
      Thread.currentThread().setContextClassLoader(original);
    }
  }

  //Delegate the init call to the actual filter
  public void init(FilterConfig filterConfig) throws ServletException {
    ClassLoader original = Thread.currentThread().getContextClassLoader();
    try {
      Thread.currentThread().setContextClassLoader(registeredContextClassLoader);
      filter.init(filterConfig);
    } finally {
      Thread.currentThread().setContextClassLoader(original);
    }
  }

  //Delegate the handling of the request to the actual filter
  public void doFilter(HttpServletRequest request, HttpServletResponse response,
    FilterChain chain) throws IOException, ServletException {
    ClassLoader original = Thread.currentThread().getContextClassLoader();
    try {
      Thread.currentThread().setContextClassLoader(registeredContextClassLoader);
      if (httpContext.handleSecurity(request, response))
        filter.doFilter(request, response, chain);
    } finally {
      Thread.currentThread().setContextClassLoader(original);
    }
  }

  public Filter getFilter() {
    return filter;
  }

  public HttpContext getHttpContext() {
    return httpContext;
  }

  public boolean matches(String dispatchPathInfo) {
    boolean result = false;
    Iterator iterator = urlPatternSet.iterator();
    while (iterator.hasNext()) {
      URLPattern urlPattern = (URLPattern) iterator.next();
      result = matches(dispatchPathInfo, urlPattern.prefix, urlPattern.suffix);
      if (result) {
        break;
      }
    }
    return result;
  }

  private boolean matches(String dispatchPathInfo, String prefix, String suffix) {
    if (!dispatchPathInfo.startsWith(prefix))
      return false;

    // perfect match
    if (prefix.length() == dispatchPathInfo.length())
      return suffix == null;

    // check the next character is a path separator
    if (dispatchPathInfo.charAt(prefix.length()) != '/')
      return false;

    // check for an extension match
    if (suffix == null)
      return true;

    return dispatchPathInfo.endsWith(suffix)
      && dispatchPathInfo.length() > prefix.length() + suffix.length();
  }

  public int compareTo(Object other) {
    FilterRegistration otherFilterRegistration = (FilterRegistration) other;
    int priorityDifference = priority - otherFilterRegistration.priority;
    if (priorityDifference != 0)
      return -priorityDifference;

    return (sequenceNumber > otherFilterRegistration.sequenceNumber) ? 1 : -1;
  }

  public void addURLPattern(String alias) {
    if (alias == null || alias.trim().length() == 0) {
      return;
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
    urlPatternSet.add(new URLPattern(prefix, suffix));
  }

  public void addMatchServletName(String servletName) {
    if (servletName == null || servletName.trim().length() == 0) {
      return;
    }
    this.servletNameSet.add(servletName);
  }

  public boolean matchServletName(String servletName) {
    return servletNameSet.contains(servletName);
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
