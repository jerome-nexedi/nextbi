package com.seekon.bicp.springsupport.orm.jpa.support;

import javax.persistence.EntityManagerFactory;

import org.springframework.orm.jpa.EntityManagerFactoryUtils;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;
import org.springframework.util.StringUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class OsgiSupportOpenEntityManagerInViewFilter extends
  OpenEntityManagerInViewFilter {

  private String contextAttribute;

  public String getContextAttribute() {
    return contextAttribute;
  }

  public void setContextAttribute(String contextAttribute) {
    this.contextAttribute = contextAttribute;
  }

  @Override
  protected EntityManagerFactory lookupEntityManagerFactory() {
    WebApplicationContext wac = null;
    String attrName = getContextAttribute();
    if (attrName != null) {
      wac = WebApplicationContextUtils.getWebApplicationContext(getServletContext(),
        attrName);
    } else {
      wac = WebApplicationContextUtils
        .getRequiredWebApplicationContext(getServletContext());
    }
    if (wac == null) {
      throw new IllegalStateException(
        "No WebApplicationContext found: no ContextLoaderListener registered?");
    }

    String emfBeanName = getEntityManagerFactoryBeanName();
    String puName = getPersistenceUnitName();
    if (StringUtils.hasLength(emfBeanName)) {
      return (EntityManagerFactory) wac.getBean(emfBeanName,
        EntityManagerFactory.class);
    }
    if ((!StringUtils.hasLength(puName))
      && (wac.containsBean("entityManagerFactory"))) {
      return (EntityManagerFactory) wac.getBean("entityManagerFactory",
        EntityManagerFactory.class);
    }

    return EntityManagerFactoryUtils.findEntityManagerFactory(wac, puName);
  }
}
