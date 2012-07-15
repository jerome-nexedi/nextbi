package com.seekon.bicp.register;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class Debug {

  public static boolean IS_DEBUG = true;

  private static Log log = LogFactory.getLog(Debug.class);

  static {
    IS_DEBUG = true;
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
