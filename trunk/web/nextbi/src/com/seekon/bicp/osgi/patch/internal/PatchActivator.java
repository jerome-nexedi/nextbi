package com.seekon.bicp.osgi.patch.internal;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.hooks.weaving.WeavingHook;

/**
 * 
 * 完成补丁hook服务的注册
 *
 */
public class PatchActivator implements BundleActivator {

  private ServiceReference<WeavingHook> serviceReference = null;

  @Override
  public void start(BundleContext conext) throws Exception {
    serviceReference = conext.registerService(WeavingHook.class,
      new DefaultPatchWeavingHook(), null).getReference();
  }

  @Override
  public void stop(BundleContext conext) throws Exception {
    conext.ungetService(serviceReference);
  }

}
