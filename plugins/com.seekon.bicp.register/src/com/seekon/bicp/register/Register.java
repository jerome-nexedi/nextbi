package com.seekon.bicp.register;

public interface Register {

  /**
   * 进行注册
   * @throws RegisterException
   */
  public void register() throws RegisterException;

  /**
   * 进行返注册
   * @throws RegisterException
   */
  public void unregister() throws RegisterException;

  /**
   * 是否已经注册
   * @return
   */
  public boolean isRegistered();
}
