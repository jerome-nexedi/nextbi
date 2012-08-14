package org.jasig.portal.portlet.dao.jpa;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(PortletDescriptorKeyImpl.class)
public abstract class PortletDescriptorKeyImpl_ {

  public static volatile SingularAttribute<PortletDescriptorKeyImpl, String> webAppName;

  public static volatile SingularAttribute<PortletDescriptorKeyImpl, String> portletName;

  public static volatile SingularAttribute<PortletDescriptorKeyImpl, Boolean> frameworkPortlet;

}
