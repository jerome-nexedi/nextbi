package org.jasig.portal.portlet.dao.jpa;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(PortletPreferencesImpl.class)
public abstract class PortletPreferencesImpl_ {

  public static volatile SingularAttribute<PortletPreferencesImpl, Long> entityVersion;

  public static volatile SingularAttribute<PortletPreferencesImpl, Long> portletPreferencesId;

  public static volatile ListAttribute<PortletPreferencesImpl, PortletPreferenceImpl> portletPreferences;

}
