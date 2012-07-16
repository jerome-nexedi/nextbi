package com.seekon.bicp.web.deployer.def;

import org.osgi.framework.Bundle;
import org.osgi.service.http.HttpService;

public interface WebModuleRegisterHandler {

  /**
   * 将要执行注册时调用
   * @param bundle
   * @param httpService
   * @param contextPath
   */
  public void preRegisterWebModule(Bundle bundle, HttpService httpService,
    String contextPath, WebModuleRegister webModuleRegister);

  /**
   * 一个Web App注册后调用该方法
   * @param bundle
   * @param httpService
   * @param contextPath
   */
  public void didRegisterWebModule(Bundle bundle, HttpService httpService,
    String contextPath, WebModuleRegister webModuleRegister);

}
