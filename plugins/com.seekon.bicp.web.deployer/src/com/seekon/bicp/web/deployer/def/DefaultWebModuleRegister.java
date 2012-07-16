package com.seekon.bicp.web.deployer.def;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.felix.framework.BundleWiringImpl;
import org.apache.felix.http.api.ExtHttpService;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.eclipse.equinox.jsp.jasper.JspServlet;
import org.osgi.framework.Bundle;
import org.osgi.framework.wiring.BundleWiring;
import org.osgi.service.http.HttpContext;
import org.osgi.service.http.NamespaceException;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.seekon.bicp.http.helper.BundleEntryHttpContext;
import com.seekon.bicp.register.Debug;
import com.seekon.bicp.register.RegisterException;

public class DefaultWebModuleRegister implements WebModuleRegister {

  private class ForwardServlet extends HttpServlet {
    /**
     * 要跳转到的url
     */
    private String url;

    public ForwardServlet(String url) {
      super();
      this.url = url;
    }

    protected void service(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
      req.getRequestDispatcher(url).forward(req, resp);
    }
  }

  private List registeredServletContextListeners = new ArrayList();

  /**
   * 已经注册的Servlet
   */
  private List registeredServlets = new ArrayList();

  /**
   * 已经注册的Filter
   */
  private List registeredFilters = new ArrayList();

  private static Logger log = Logger.getLogger(DefaultWebModuleRegister.class);

  private Bundle bundle;

  private String webFolder;

  private HttpContext httpContext = null;

  private String contextPath;

  private ExtHttpService httpService;

  private boolean registered;

  private ClassLoader webResourceClassLoader = null;

  public DefaultWebModuleRegister(Bundle bundle, String contextPath,
    String webFolder, ExtHttpService httpService) {
    super();
    this.bundle = bundle;
    this.webFolder = webFolder;
    this.contextPath = contextPath;
    this.httpService = httpService;
    this.webResourceClassLoader = ((BundleWiringImpl) bundle
      .adapt(BundleWiring.class)).getClassLoader();// //((BundleHost)
    // bundle).getClassLoader();
  }

  private HttpContext getHttpContext() {
    if (httpContext == null) {
      httpContext = new BundleEntryHttpContext(bundle, webFolder);
    }
    return httpContext;
  }

  public void register() throws RegisterException {
    System.out.println("####Register WebModule: " + contextPath + " (" + bundle
      + ")");
    try {
      Document doc = getWebXmlDocument();
      // //registerListeners(doc);
      registerFilters(doc);
      registerDefault();
      registerServlets(doc);
    } catch (Exception e) {
      throw new RegisterException(e);
    }
    registered = true;
  }

  // public void registerListeners(Document doc) throws RegisterException {
  // try {
  // ClassLoader cl = getWebResourceClassLoader();
  // ClassLoader contextClassLoader = Thread.currentThread()
  // .getContextClassLoader();
  //
  // Dictionary initParameter = getContextParams(doc);
  // List listenerNodes = doc.selectNodes("/web-app/listener");
  // for (Iterator it = listenerNodes.iterator(); it.hasNext();) {
  // Element el = (Element) it.next();
  // String listenerClass = el.selectSingleNode("listener-class")
  // .getStringValue().trim();
  // if (listenerClass != null) {
  // Thread.currentThread().setContextClassLoader(cl);
  // try {
  // try {
  // Object listener = Class.forName(listenerClass, true, cl).newInstance();
  // if (listener instanceof ServletContextListener) {
  // httpService.registerServletContextListeners((ServletContextListener)listener
  // , initParameter, getHttpContext(), this.bundle.getBundleContext());
  // registeredServletContextListeners.add(listener);
  // }else if(listener instanceof ServletRequestListener){
  // httpService.registerServletRequestListener((ServletRequestListener)listener,
  // contextPath);
  // }
  // } catch (Throwable e) {
  // log.error("can't create instance class: " + listenerClass
  // + " at bundle" + bundle.getSymbolicName(), e);
  // }
  // } finally {
  // Thread.currentThread().setContextClassLoader(contextClassLoader);
  // }
  // }
  // }
  // } catch (Throwable e) {
  // throw new RegisterException(e);
  // }
  // }

  /**
   * 注册默认的资源，html，htm，jsp等
   * 
   * @param context
   * @param httpService
   * @param webFolder
   * @throws NamespaceException
   * @throws ServletException
   */
  public void registerDefault() throws RegisterException {
    try {
      httpService.registerResources(contextPath, "/", getHttpContext());

      Servlet adaptedJspServlet = new JspServlet(bundle, webFolder);

      Dictionary dictionary = new Hashtable();
      dictionary.put("bundle.name", bundle.getSymbolicName());
      if (this.contextPath != null && this.contextPath.trim().length() > 0) {
        dictionary.put("Osgi-Context-Path", this.contextPath);
      }

      httpService.registerServlet(contextPath.concat("/*.jsp"), adaptedJspServlet,
        dictionary, getHttpContext());
    } catch (Exception e) {
      throw new RegisterException(e);
    }
  }

  private Document getWebXmlDocument() throws RegisterException {
    try {
      return getSAXReader().read(getWebXmlReader());
    } catch (Throwable e) {
      throw new RegisterException(e);
    }
  }

  /**
   * 注册filter
   * 
   * @param httpService
   * @param webXml
   * @throws Exception
   */
  public void registerFilters(Document doc) throws RegisterException {
    try {
      // if (isChangeApplusContext()) {
      // registerApplusContextAdapterFilter();
      // }
      List filterNodes = doc.selectNodes("/web-app/filter");
      int ranking = 0;

      for (Iterator it = filterNodes.iterator(); it.hasNext();) {
        Element el = (Element) it.next();
        try {
          String filterName = el.selectSingleNode("filter-name").getStringValue()
            .trim();
          String filterClass = el.selectSingleNode("filter-class").getStringValue()
            .trim();

          this.registerFilterWithUrlPattern(doc, filterName, filterClass, el,
            ranking);
          this.registerFilterWithServletPattern(doc, filterName, filterClass, el,
            ranking);
          ranking++;
        } catch (Throwable e) {
          log.error(e);
        }
      }
    } catch (Throwable e) {
      throw new RegisterException(e);
    }
  }

  private void registerFilterWithServletPattern(Document doc, String filterName,
    String filterClass, Element el, int ranking) {
    List servletNameList = doc
      .selectNodes("/web-app/filter-mapping/servlet-name[../filter-name='"
        + filterName + "']");
    if (servletNameList == null || servletNameList.isEmpty()) {
      return;
    }

    Dictionary dictionary = getInitParams(el);
    dictionary.put("filter-name", filterName);

    ClassLoader cl = getWebResourceClassLoader();
    ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();

    try {
      Thread.currentThread().setContextClassLoader(cl);
      try {
        Filter servletFilter = (Filter) Class.forName(filterClass, true, cl)
          .newInstance();
        Iterator iterator = servletNameList.iterator();
        while (iterator.hasNext()) {
          Node servletNameElement = (Node) iterator.next();
          if (servletNameElement != null) {
            String servletName = servletNameElement.getStringValue().trim();
            registerFilter(null, servletName, servletFilter, dictionary, ranking);
          }
        }
      } finally {
        Thread.currentThread().setContextClassLoader(contextClassLoader);
      }
    } catch (Throwable e) {
      log.error("can't create instance class: " + filterClass + " at bundle"
        + bundle, e);
    }
  }

  private void registerFilterWithUrlPattern(Document doc, String filterName,
    String filterClass, Element el, int ranking) {
    List urlPatternList = doc
      .selectNodes("/web-app/filter-mapping/url-pattern[../filter-name='"
        + filterName + "']");
    if (urlPatternList == null || urlPatternList.isEmpty()) {
      return;
    }

    Dictionary dictionary = getInitParams(el);
    dictionary.put("filter-name", filterName);

    ClassLoader cl = getWebResourceClassLoader();
    ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();

    try {
      Thread.currentThread().setContextClassLoader(cl);
      try {
        Filter servletFilter = (Filter) Class.forName(filterClass, true, cl)
          .newInstance();
        Iterator iterator = urlPatternList.iterator();
        while (iterator.hasNext()) {
          Node urlPatternElement = (Node) iterator.next();
          if (urlPatternElement != null) {
            String urlPattern = urlPatternElement.getStringValue().trim();
            if (!urlPattern.startsWith("/")) {
              urlPattern = "/" + urlPattern;
            }
            String filterPath = contextPath.concat(urlPattern);
            if (filterPath.endsWith("/*")) {
              filterPath = filterPath.substring(0, filterPath.length() - 2);
            }
            registerFilter(filterPath, null, servletFilter, dictionary, ranking);
          }
        }
      } finally {
        Thread.currentThread().setContextClassLoader(contextClassLoader);
      }
    } catch (Throwable e) {
      log.error("can't create instance class: " + filterClass + " at bundle"
        + bundle, e);
    }
  }

  public void registerFilter(String filterPath, String servletName,
    Filter servletFilter, Dictionary dictionary, int ranking)
    throws ServletException, NamespaceException {
    httpService.registerFilter(servletFilter, filterPath, dictionary, ranking,
      getHttpContext());
    registeredFilters.add(servletFilter);
  }

  /**
   * 返回web.xml的URL
   * 
   * @return
   */
  protected URL getWebXMLResource() {
    StringBuffer buffer = new StringBuffer();
    if (webFolder.startsWith("/")) {
      buffer.append(webFolder.substring(1));
    } else {
      buffer.append(webFolder);
    }

    if (webFolder.endsWith("/")) {
      buffer.append("WEB-INF/web.xml");
    } else {
      buffer.append("/WEB-INF/web.xml");
    }
    URL resource = bundle.getResource(buffer.toString());
    if (resource == null) {
      throw new IllegalStateException("web.xml not found in bundle " + bundle);
    }
    return resource;
  }

  private Reader getWebXmlReader() {
    BufferedReader br;
    try {
      // 去掉namespace
      br = new BufferedReader(
        new InputStreamReader(getWebXMLResource().openStream()));
      try {
        StringBuffer buf = new StringBuffer();
        for (String line = null; (line = br.readLine()) != null;) {
          buf.append(line);
          buf.append('\n');
        }
        int start = buf.indexOf("<web-app");
        start += 8;
        int end = buf.indexOf(">", start);
        buf.delete(start, end);
        return new StringReader(buf.toString());
      } finally {
        br.close();
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * 注册发布在web.xml中的servlet
   * 
   * @param httpService
   * @param webXml
   * @throws Exception
   */
  public void registerServlets(Document doc) throws RegisterException {
    try {
      List servlets = doc.selectNodes("/web-app/servlet");

      ClassLoader cl = getWebResourceClassLoader();
      ClassLoader contextClassLoader = Thread.currentThread()
        .getContextClassLoader();

      for (Iterator it = servlets.iterator(); it.hasNext();) {
        Element el = (Element) it.next();
        try {
          String servletName = el.selectSingleNode("servlet-name").getStringValue()
            .trim();
          Debug.trace("found servlet " + servletName);
          Node servletClassNode = el.selectSingleNode("servlet-class");

          List/* <Node> */nodeList = doc
            .selectNodes("/web-app/servlet-mapping/url-pattern[../servlet-name='"
              + servletName + "']");
          if (nodeList == null || nodeList.isEmpty()) {
            IllegalConfigurationException e = new IllegalConfigurationException(
              "servlet " + servletName + " mapping not found");
            log.error(e.getMessage(), e);
            e.printStackTrace();
            continue;
          }

          Servlet servlet = null;
          if (servletClassNode != null) {
            String servletClass = servletClassNode.getStringValue().trim();
            Thread.currentThread().setContextClassLoader(cl);
            try {
              try {
                Debug.trace("instance servlet: " + servletClass);
                servlet = (Servlet) bundle.loadClass(servletClass).newInstance();
                if (servlet != null)
                  Debug.trace("success create servlet: " + servlet);

              } catch (Throwable e) {
                System.err.println("can't create instance class: " + servletClass
                  + " at bundle " + bundle);
                e.printStackTrace();
              }
            } finally {
              Thread.currentThread().setContextClassLoader(contextClassLoader);
            }

          } else {
            /*
             * servlet中配置的是jsp-file <servlet>
             * <servlet-name>prnmaindata</servlet-name>
             * <jsp-file>/jsp/platform/prnmaindata.jsp</jsp-file> </servlet>
             */
            Node jspFileNode = el.selectSingleNode("jsp-file");
            if (jspFileNode != null) {
              String jspFile = jspFileNode.getStringValue().trim();
              /*
               * 添加上前缀，如：web.xml中配置的路径为：/jsp/platform/prnmaindata.jsp， 添加上
               * getContextPath()的结果是/gl/jsp/platform/prnmaindata.jsp
               */
              jspFile = contextPath.concat(jspFile);
              servlet = new ForwardServlet(jspFile);
            }
          }

          Dictionary dictionary = getInitParams(el);
          dictionary.put("servlet-name", servletName);

          Iterator/* <Node> */iterator = nodeList.iterator();
          while (iterator.hasNext()) {
            Node urlPatternElement = (Node) iterator.next();
            String urlPattern = urlPatternElement.getStringValue().trim();

            if (!urlPattern.startsWith("/")) {
              urlPattern = "/" + urlPattern;
            }
            /*
             * 添加上contextPath前缀,如：/admin/Servlet1
             */
            String servletPath = contextPath.concat(urlPattern);

            // 去掉/*后缀，如：/BIP/bip/*，应处理成/BIP/bip，否则无法匹配
            if (servletPath.endsWith("/*")) {
              servletPath = servletPath.substring(0, servletPath.length() - 2);
            }

            if (servlet != null) {
              Thread.currentThread().setContextClassLoader(cl);
              try {
                httpService.registerServlet(servletPath, servlet, dictionary,
                  getHttpContext());
                registeredServlets.add(servletPath);
              } finally {
                Thread.currentThread().setContextClassLoader(contextClassLoader);
              }
            }
          }
        } catch (Throwable e) {
          log.error(e, e);
        }
      }
    } catch (Exception e) {
      throw new RegisterException(e);
    }
  }

  private SAXReader getSAXReader() {
    SAXReader saxReader = new SAXReader();
    saxReader.setValidation(false);
    saxReader.setEntityResolver(new EntityResolver() {

      public InputSource resolveEntity(String publicId, String systemId)
        throws SAXException, IOException {
        return new InputSource(new ByteArrayInputStream(new byte[0]));
      }
    });
    return saxReader;
  }

  private Dictionary getInitParams(Element el) {
    Dictionary dictionary = new Hashtable();
    List initParams = el.selectNodes("init-param");
    for (Iterator it1 = initParams.iterator(); it1.hasNext();) {
      Element e = (Element) it1.next();
      String name = e.element("param-name").getText();
      String value = e.element("param-value").getText();
      if (name != null) {
        name = name.trim();
      }
      if (value != null) {
        value = value.trim();
      }
      dictionary.put(name, value);
    }

    dictionary.put("bundle.name", bundle.getSymbolicName());
    if (this.contextPath != null && this.contextPath.trim().length() > 0) {
      dictionary.put("Osgi-Context-Path", this.contextPath);
    }
    return dictionary;
  }

  /**
   * 返回创建web.xml中配置的servlet，filter对应的class的classLoader
   * 
   * @return
   */
  protected ClassLoader getWebResourceClassLoader() {
    return this.webResourceClassLoader;
  }

  /**
   * 注册用于修改ApplusContext key的filter
   * 
   * @param httpService
   * @throws ServletException
   * @throws NamespaceException
   */
  // protected void registerApplusContextAdapterFilter() throws Exception {
  // FilterAdapter applusContextAdapterFilter = new ContextAdapterFilter();
  // registerFilter(contextPath, applusContextAdapterFilter, null);
  // }

  public void unregister() throws RegisterException {
    Debug.trace("UnRegister WebModule: " + contextPath + " (" + bundle + ")");
    httpService.unregister(contextPath);
    httpService.unregister(contextPath.concat("/*.jsp")); //$NON-NLS-1$
    unregisterServlets(httpService);
    unregisterFilters(httpService);
    // //unregisterListeners();
  }

  // protected void unregisterListeners() {
  // while (!this.registeredServletContextListeners.isEmpty()) {
  // ServletContextListener listener = (ServletContextListener)
  // registeredServletContextListeners
  // .remove(0);
  // httpService.unregisterServletContextListeners(listener, httpContext);
  // }
  // httpService.unregisterServletRequestListeners(contextPath);
  // }

  protected void unregisterServlets(final ExtHttpService httpService) {
    while (!registeredServlets.isEmpty()) {
      httpService.unregister(registeredServlets.remove(0).toString());
    }
  }

  /**
   * 删除/反注册后已经注册的Filter
   * 
   * @param httpService
   */
  protected void unregisterFilters(final ExtHttpService httpService) {
    while (!registeredFilters.isEmpty()) {
      Filter filter = (Filter) registeredFilters.remove(0);
      try {
        httpService.unregisterFilter(filter);
      } catch (Throwable e) {
        // e.printStackTrace();
      }
    }
  }

  public boolean isRegistered() {
    return registered;
  }

  /**
   * 
   * @param doc
   * @return
   */
  private Dictionary getContextParams(Document doc) {
    Dictionary dictionary = new Hashtable();
    List initParams = doc.selectNodes("/web-app/context-param");
    for (Iterator it1 = initParams.iterator(); it1.hasNext();) {
      Element e = (Element) it1.next();
      String name = e.element("param-name").getText();
      String value = e.element("param-value").getText();
      if (name != null) {
        name = name.trim();
      }
      if (value != null) {
        value = value.trim();
      }
      dictionary.put(name, value);
    }
    dictionary.put("bundle.name", bundle.getSymbolicName());
    return dictionary;
  }

  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((bundle == null) ? 0 : bundle.hashCode());
    result = prime * result + ((contextPath == null) ? 0 : contextPath.hashCode());
    result = prime * result + ((webFolder == null) ? 0 : webFolder.hashCode());
    return result;
  }

  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    DefaultWebModuleRegister other = (DefaultWebModuleRegister) obj;
    if (bundle == null) {
      if (other.bundle != null)
        return false;
    } else if (!bundle.equals(other.bundle))
      return false;
    if (contextPath == null) {
      if (other.contextPath != null)
        return false;
    } else if (!contextPath.equals(other.contextPath))
      return false;
    if (webFolder == null) {
      if (other.webFolder != null)
        return false;
    } else if (!webFolder.equals(other.webFolder))
      return false;
    return true;
  }
}
