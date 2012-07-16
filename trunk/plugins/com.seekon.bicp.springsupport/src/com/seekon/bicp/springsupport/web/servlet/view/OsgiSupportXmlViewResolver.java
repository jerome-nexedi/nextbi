package com.seekon.bicp.springsupport.web.servlet.view;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.osgi.web.context.support.OsgiBundleXmlWebApplicationContext;
import org.springframework.web.servlet.view.XmlViewResolver;

public class OsgiSupportXmlViewResolver extends XmlViewResolver{

	private Resource location;
	
  private ConfigurableApplicationContext cachedFactory;

	public void setLocation(Resource location){
		super.setLocation(location);
    this.location = location;
  }
	
	@Override
	protected synchronized BeanFactory initFactory() throws BeansException {
		if (this.cachedFactory != null) {
      return this.cachedFactory;
    }
		
    Resource actualLocation = this.location;
    if (actualLocation == null) {
      actualLocation = getApplicationContext().getResource(DEFAULT_LOCATION);
    }
    
    
    OsgiBundleXmlWebApplicationContext factory = new OsgiBundleXmlWebApplicationContext();
//    GenericWebApplicationContext factory = new GenericWebApplicationContext();
    OsgiBundleXmlWebApplicationContext parent = (OsgiBundleXmlWebApplicationContext)getApplicationContext();
    factory.setParent(parent);
    factory.setServletContext(parent.getServletContext());
    try{
    	factory.setConfigLocation(actualLocation.getURL().toString());
    }catch(Exception e){
    	
    }
//    XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
//    reader.setEnvironment(getApplicationContext().getEnvironment());
//    reader.setEntityResolver(new ResourceEntityResolver(getApplicationContext()));
//    reader.loadBeanDefinitions(actualLocation);
//
    factory.refresh();

    if (isCache()) {
      this.cachedFactory = factory;
    }
    return factory;
	}
	
	@Override
	public void destroy() throws BeansException {
		super.destroy();
		if (this.cachedFactory != null){
      this.cachedFactory.close();
		}
	}
}
