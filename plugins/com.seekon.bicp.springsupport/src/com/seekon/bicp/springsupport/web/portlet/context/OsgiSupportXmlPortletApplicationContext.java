package com.seekon.bicp.springsupport.web.portlet.context;

import java.io.IOException;
import java.security.AccessController;
import java.security.PrivilegedAction;

import org.osgi.framework.BundleContext;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.NamespaceHandlerResolver;
import org.springframework.beans.factory.xml.ResourceEntityResolver;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.osgi.context.internal.classloader.ClassLoaderFactory;
import org.springframework.osgi.util.internal.BundleUtils;
import org.springframework.util.Assert;
import org.springframework.web.portlet.context.XmlPortletApplicationContext;
import org.xml.sax.EntityResolver;
import org.springframework.osgi.io.OsgiBundleResource;
import org.springframework.osgi.io.OsgiBundleResourcePatternResolver;

import com.seekon.bicp.springsupport.context.OsgiApplicationContextUtil;

public class OsgiSupportXmlPortletApplicationContext extends XmlPortletApplicationContext{

	private BundleContext bundleContext = null;
	
	private ClassLoader classLoader = null;
	
	private ResourcePatternResolver osgiPatternResolver;

	public BundleContext getBundleContext() {
		return bundleContext;
	}

	public void setBundleContext(BundleContext bundleContext) {
		this.bundleContext = bundleContext;
		this.osgiPatternResolver = createResourcePatternResolver();
	}

	@Override
	protected void loadBeanDefinitions(DefaultListableBeanFactory beanFactory)
			throws BeansException, IOException {
		// Create a new XmlBeanDefinitionReader for the given BeanFactory.
		XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(beanFactory);

		// Configure the bean definition reader with this context's
		// resource loading environment.
		beanDefinitionReader.setEnvironment(this.getEnvironment());
		beanDefinitionReader.setResourceLoader(this);
		beanDefinitionReader.setEntityResolver(new ResourceEntityResolver(this));

		final Object[] resolvers = new Object[2];

		AccessController.doPrivileged(new PrivilegedAction() {

			public Object run() {
				BundleContext ctx = getBundleContext();
				String filter = BundleUtils.createNamespaceFilter(ctx);
				resolvers[0] = OsgiApplicationContextUtil.createNamespaceHandlerResolver(ctx, filter, getClassLoader());
				resolvers[1] = OsgiApplicationContextUtil.createEntityResolver(ctx, filter, getClassLoader());
				return null;
			}
		});
		
		beanDefinitionReader.setNamespaceHandlerResolver((NamespaceHandlerResolver) resolvers[0]);
		beanDefinitionReader.setEntityResolver((EntityResolver) resolvers[1]);

		initBeanDefinitionReader(beanDefinitionReader);
		loadBeanDefinitions(beanDefinitionReader);
	}
	
	@Override
	public ClassLoader getClassLoader() {
		if(classLoader == null){
			Assert.notNull(bundleContext, "bundleContext is required");
			classLoader = ClassLoaderFactory.getBundleClassLoaderFor(bundleContext.getBundle());
		}
		return classLoader;
	}
	
	@Override
	public Resource getResource(String location) {
		return (osgiPatternResolver != null ? osgiPatternResolver.getResource(location) : null);
	}

	@Override
	public Resource[] getResources(String locationPattern) throws IOException {
		return (osgiPatternResolver != null ? osgiPatternResolver.getResources(locationPattern) : null);
	}
	
	@Override
	protected Resource getResourceByPath(String path) {
		Assert.notNull(path, "Path is required");
		return new OsgiBundleResource(bundleContext.getBundle(), path);
	}
	
	@Override
	protected ResourcePatternResolver getResourcePatternResolver() {
		return osgiPatternResolver;
	}
	
	protected ResourcePatternResolver createResourcePatternResolver() {
		return new OsgiBundleResourcePatternResolver(bundleContext.getBundle());
	}
}
