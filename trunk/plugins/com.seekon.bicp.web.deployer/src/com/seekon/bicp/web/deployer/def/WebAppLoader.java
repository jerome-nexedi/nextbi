package com.seekon.bicp.web.deployer.def;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.equinox.http.servlet.ExtendedHttpService;
import org.osgi.framework.Bundle;

import com.seekon.bicp.register.Register;
import com.seekon.bicp.register.RegisterException;
import com.seekon.bicp.web.deployer.Activator;
import com.seekon.bicp.web.deployer.HttpServiceFactory;
import com.seekon.bicp.web.deployer.LoadException;
import com.seekon.bicp.web.deployer.ModuleLoader;

public class WebAppLoader implements ModuleLoader {

  private static Logger log = Logger.getLogger(Activator.class);

  /**
   * web资源文件夹名称key，用于插件的MANIFEST.MF
   */
  private static String WEB_FOLDER = "Web-Folder";

  /**
   * 上下文路径key，用于插件的MANIFEST.MF
   */
  private static String CONTEXT_PATH = "Context-Path";

  /**
   * 已经注册的web模块
   */
  private Map/* <Bundle, Register> */webModuleRegisters = new HashMap/*
                                                                       * <Bundle,
                                                                       * Register
                                                                       * >
                                                                       */();

  // private ExtHttpService httpService = null;

  private static final String WEB_MODULE_REGISTER_HANDLER = "WebModule-RegisterHandler";

  private List webModuleRegisterHandlertList = new ArrayList();

  // public void init(BundleContext bundleContext) {
  // Bundle[] bundles = bundleContext.getBundles();
  // for (int i = 0; i < bundles.length; i++) {
  // Bundle bundle = bundles[i];
  // String fragmentHost = bundle.getHeaders().get("Fragment-Host");
  // if (fragmentHost != null)
  // continue;
  //
  // String l = (String) bundle.getHeaders().get(WEB_MODULE_REGISTER_HANDLER);
  // if (l != null) {
  // StringTokenizer stk = new StringTokenizer(l, " \t\n\r\f,");
  // while (stk.hasMoreTokens()) {
  // String className = stk.nextToken();
  // try {
  // Class handleClass = bundle.loadClass(className);
  // webModuleRegisterHandlertList.add(handleClass.newInstance());
  // } catch (Exception e) {
  // log.error(e.getMessage(), e);
  // }
  // }
  // }
  // }
  // }

  public void load(Bundle bundle) throws LoadException {
    String contextPath = getContextPath(bundle);
    if (contextPath != null) {
      if (webModuleRegisters.containsKey(bundle)) {
        System.err.println(bundle + " registered");
        throw new IllegalStateException(bundle + " registered");
      }
      String webFolder = getWebFolder(bundle);

      ExtendedHttpService httpService = HttpServiceFactory.getHttpService();
      if (httpService == null) {
        System.err.println(bundle + " httpService is null");
        return;
      }

      WebModuleRegister register = new DefaultWebModuleRegister(bundle, contextPath,
        webFolder, httpService);
      try {
        for (Iterator it = this.webModuleRegisterHandlertList.iterator(); it
          .hasNext();) {
          WebModuleRegisterHandler handler = (WebModuleRegisterHandler) it.next();
          handler.preRegisterWebModule(bundle, httpService, contextPath, register);
        }

        register.register();
        webModuleRegisters.put(bundle, register);
        for (Iterator it = this.webModuleRegisterHandlertList.iterator(); it
          .hasNext();) {
          WebModuleRegisterHandler handler = (WebModuleRegisterHandler) it.next();
          handler.didRegisterWebModule(bundle, httpService, contextPath, register);
        }
      } catch (RegisterException e) {
        log.error(e.getMessage(), e);
      }
      // //ProxyServlet.getInstance().registerContextPath(contextPath);
    }
  }

  private String getContextPath(Bundle bundle) {
    String path = (String) bundle.getHeaders().get(CONTEXT_PATH);
    if (path != null) {
      if (!path.startsWith("/")) {
        path = "/" + path;
      }
      path = path.trim();
    }
    return path;
  }

  private String getWebFolder(Bundle bundle) {
    String folder = (String) bundle.getHeaders().get(WEB_FOLDER);
    if (folder != null) {
      if (!folder.startsWith("/")) {
        folder = "/" + folder;
      }
      folder = folder.trim();
    }
    return folder;
  }

  public void unload(Bundle bundle) throws LoadException {
    Register register;
    register = (Register) webModuleRegisters.remove(bundle);
    if (register != null)
      try {
        register.unregister();
        // BridgeServlet.getInstance().unregisterContextPath(
        // this.getContextPath(bundle));
      } catch (RegisterException e) {
        throw new LoadException(e);
      }
  }

}
