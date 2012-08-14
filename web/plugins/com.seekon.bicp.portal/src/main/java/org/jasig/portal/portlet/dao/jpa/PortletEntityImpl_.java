package org.jasig.portal.portlet.dao.jpa;

import javax.annotation.Generated;
import javax.persistence.metamodel.MapAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import javax.portlet.WindowState;
import org.jasig.portal.layout.dao.jpa.StylesheetDescriptorImpl;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(PortletEntityImpl.class)
public abstract class PortletEntityImpl_ {

  public static volatile SingularAttribute<PortletEntityImpl, Long> entityVersion;

  public static volatile SingularAttribute<PortletEntityImpl, PortletDefinitionImpl> portletDefinition;

  public static volatile SingularAttribute<PortletEntityImpl, Integer> userId;

  public static volatile SingularAttribute<PortletEntityImpl, String> layoutNodeId;

  public static volatile SingularAttribute<PortletEntityImpl, PortletPreferencesImpl> portletPreferences;

  public static volatile MapAttribute<PortletEntityImpl, StylesheetDescriptorImpl, WindowState> windowStates;

  public static volatile SingularAttribute<PortletEntityImpl, Long> internalPortletEntityId;

}
