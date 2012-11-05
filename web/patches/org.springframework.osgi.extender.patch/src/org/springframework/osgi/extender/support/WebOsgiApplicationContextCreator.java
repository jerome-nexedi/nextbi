package org.springframework.osgi.extender.support;

import com.seekon.bicp.osgi.bridge.GlobalWebContextHelper;
import org.osgi.framework.BundleContext;
import org.springframework.osgi.context.DelegatedExecutionOsgiBundleApplicationContext;
import org.springframework.osgi.context.support.OsgiBundleXmlApplicationContext;
import org.springframework.osgi.web.context.support.OsgiBundleXmlWebApplicationContext;

public class WebOsgiApplicationContextCreator extends
  DefaultOsgiApplicationContextCreator {

  @Override
  public DelegatedExecutionOsgiBundleApplicationContext createApplicationContext(
    BundleContext bundleContext) throws Exception {

    OsgiBundleXmlApplicationContext context = (OsgiBundleXmlApplicationContext) super
      .createApplicationContext(bundleContext);
    if (context == null) {
      return context;
    }

    OsgiBundleXmlWebApplicationContext result = new OsgiBundleXmlWebApplicationContext();
    result.setConfigLocations(context.getConfigLocations());
    result.setBundleContext(bundleContext);
    result.setServletContext(GlobalWebContextHelper.getGlobalServletContext());
    //result.setPublishContextAsService(context.);

    return result;
  }

}
