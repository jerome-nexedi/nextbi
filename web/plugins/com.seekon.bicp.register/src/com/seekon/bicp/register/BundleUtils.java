package com.seekon.bicp.register;

import java.util.HashMap;
import java.util.Map;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleEvent;

public final class BundleUtils {
  private static Map<Integer, String> eventStateMap;

  private static Map<Integer, String> bundleStateMap;

  private BundleUtils() {
  }

  static {
    eventStateMap = new HashMap<Integer, String>();
    eventStateMap.put(new Integer(BundleEvent.INSTALLED), "INSTALLED");
    eventStateMap.put(new Integer(BundleEvent.STARTED), "STARTED");
    eventStateMap.put(new Integer(BundleEvent.STOPPED), "STOPPED");
    eventStateMap.put(new Integer(BundleEvent.UPDATED), "UPDATED");
    eventStateMap.put(new Integer(BundleEvent.UNINSTALLED), "UNINSTALLED");
    eventStateMap.put(new Integer(BundleEvent.RESOLVED), "RESOLVED");
    eventStateMap.put(new Integer(BundleEvent.UNRESOLVED), "UNRESOLVED");
    eventStateMap.put(new Integer(BundleEvent.STARTING), "STARTING");
    eventStateMap.put(new Integer(BundleEvent.STOPPING), "STOPPING");
    eventStateMap.put(new Integer(BundleEvent.LAZY_ACTIVATION), "LAZY_ACTIVATION");

    bundleStateMap = new HashMap<Integer, String>();
    bundleStateMap.put(new Integer(Bundle.UNINSTALLED), "UNINSTALLED");
    bundleStateMap.put(new Integer(Bundle.INSTALLED), "INSTALLED");
    bundleStateMap.put(new Integer(Bundle.RESOLVED), "RESOLVED");
    bundleStateMap.put(new Integer(Bundle.STARTING), "STARTING");
    bundleStateMap.put(new Integer(Bundle.STOPPING), "STOPPING");
    bundleStateMap.put(new Integer(Bundle.ACTIVE), "ACTIVE");
    bundleStateMap.put(new Integer(Bundle.START_TRANSIENT), "START_TRANSIENT");
    bundleStateMap.put(new Integer(Bundle.START_ACTIVATION_POLICY),
      "START_ACTIVATION_POLICY");
  }

  public static String getEventStateString(int state) {
    return eventStateMap.get(new Integer(state)).toString();
  }

  public static String getBundleStateString(int state) {
    return bundleStateMap.get(new Integer(state)).toString();
  }

}
