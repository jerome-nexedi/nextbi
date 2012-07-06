package com.seekon.bicp.web.deployer;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

public interface ModuleLoader {

  /**
   * 初始化，可以做一些解析工作，如读取配置
   * @param bundleContext
   */
  public void init(BundleContext bundleContext);

  /**
   * 执行加载，
   * @param bundle
   * @throws LoadException
   */
  public void load(Bundle bundle) throws LoadException;

  /**
   * 卸载已加载的模块
   * @param bundle
   * @throws LoadException
   */
  public void unload(Bundle bundle) throws LoadException;
}
