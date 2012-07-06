package com.seekon.bicp.register;

import org.apache.log4j.Logger;

public final class Debug {

  public static boolean IS_DEBUG = true;

  private static Logger log = Logger.getLogger(Debug.class);

  static {
    IS_DEBUG = log.isInfoEnabled();
  }

  public static void trace(Object msg) {
    if (IS_DEBUG)
      synchronized (System.out) {
        System.out.print("AppLoader INFO - ");
        System.out.println(msg);
      }
  }

  private Debug() {
  }
}
