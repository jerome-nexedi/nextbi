package com.seekon.bicp.springsupport.context;

import org.osgi.framework.BundleContext;
import org.springframework.beans.factory.xml.DefaultNamespaceHandlerResolver;
import org.springframework.beans.factory.xml.DelegatingEntityResolver;
import org.springframework.beans.factory.xml.NamespaceHandlerResolver;
import org.springframework.osgi.context.support.DelegatedEntityResolver;
import org.springframework.osgi.context.support.DelegatedNamespaceHandlerResolver;
import org.springframework.osgi.context.support.TrackingUtil;
import org.springframework.osgi.util.OsgiStringUtils;
import org.springframework.util.Assert;
import org.xml.sax.EntityResolver;

public class OsgiApplicationContextUtil {

  public static NamespaceHandlerResolver createNamespaceHandlerResolver(
    BundleContext bundleContext, String filter, ClassLoader bundleClassLoader) {
    Assert.notNull(bundleContext, "bundleContext is required");
    // create local namespace resolver
    // we'll use the default resolver which uses the bundle local class-loader
    NamespaceHandlerResolver localNamespaceResolver = new DefaultNamespaceHandlerResolver(
      bundleClassLoader);

    // hook in OSGi namespace resolver
    NamespaceHandlerResolver osgiServiceNamespaceResolver = lookupNamespaceHandlerResolver(
      bundleContext, filter, localNamespaceResolver);

    DelegatedNamespaceHandlerResolver delegate = new DelegatedNamespaceHandlerResolver();
    delegate.addNamespaceHandler(localNamespaceResolver,
      "LocalNamespaceResolver for bundle "
        + OsgiStringUtils.nullSafeNameAndSymName(bundleContext.getBundle()));
    delegate.addNamespaceHandler(osgiServiceNamespaceResolver,
      "OSGi Service resolver");

    return delegate;
  }

  /**
   * Similar to
   * {@link #createNamespaceHandlerResolver(BundleContext, ClassLoader, ClassLoader)}
   * , this method creates a special OSGi entity resolver that considers the
   * bundle class path first, falling back to the entity resolver service
   * provided by the Spring DM extender.
   * 
   * @param bundleContext
   *          the OSGi context of which the resolver should be aware of
   * @param filter
   * @param bundleClassLoader
   *          classloader for creating the OSGi namespace resolver proxy
   * @return a OSGi aware entity resolver
   */
  public static EntityResolver createEntityResolver(BundleContext bundleContext,
    String filter, ClassLoader bundleClassLoader) {
    Assert.notNull(bundleContext, "bundleContext is required");
    // create local namespace resolver
    EntityResolver localEntityResolver = new DelegatingEntityResolver(
      bundleClassLoader);

    // hook in OSGi namespace resolver
    EntityResolver osgiServiceEntityResolver = lookupEntityResolver(bundleContext,
      filter, localEntityResolver);

    DelegatedEntityResolver delegate = new DelegatedEntityResolver();
    delegate.addEntityResolver(localEntityResolver,
      "LocalEntityResolver for bundle "
        + OsgiStringUtils.nullSafeNameAndSymName(bundleContext.getBundle()));

    // hook in OSGi namespace resolver
    delegate.addEntityResolver(osgiServiceEntityResolver, "OSGi Service resolver");

    return delegate;
  }

  public static NamespaceHandlerResolver lookupNamespaceHandlerResolver(
    final BundleContext bundleContext, String filter, final Object fallbackObject) {
    return (NamespaceHandlerResolver) TrackingUtil
      .getService(new Class[] { NamespaceHandlerResolver.class }, filter,
        NamespaceHandlerResolver.class.getClassLoader(), bundleContext,
        fallbackObject);
  }

  public static EntityResolver lookupEntityResolver(
    final BundleContext bundleContext, String filter, final Object fallbackObject) {
    return (EntityResolver) TrackingUtil.getService(
      new Class[] { EntityResolver.class }, filter, EntityResolver.class
        .getClassLoader(), bundleContext, fallbackObject);
  }
}
