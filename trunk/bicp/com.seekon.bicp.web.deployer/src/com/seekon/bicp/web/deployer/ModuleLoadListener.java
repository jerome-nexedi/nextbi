package com.seekon.bicp.web.deployer;

import java.util.EventObject;

public interface ModuleLoadListener {

  /**
   * 加载前调用
   * @param e
   */
  public void before(EventObject e);

  /**
   * 加载后调用
   * @param e
   */
  public void after(EventObject e);

}
