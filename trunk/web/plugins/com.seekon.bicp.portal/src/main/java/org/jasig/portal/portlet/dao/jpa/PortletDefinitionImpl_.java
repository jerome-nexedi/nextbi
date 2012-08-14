package org.jasig.portal.portlet.dao.jpa;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.MapAttribute;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(PortletDefinitionImpl.class)
public abstract class PortletDefinitionImpl_ {

  public static volatile SingularAttribute<PortletDefinitionImpl, Long> entityVersion;

  public static volatile SingularAttribute<PortletDefinitionImpl, Integer> publisherId;

  public static volatile SingularAttribute<PortletDefinitionImpl, Date> expirationDate;

  public static volatile SingularAttribute<PortletDefinitionImpl, Integer> resourceTimeout;

  public static volatile SingularAttribute<PortletDefinitionImpl, Integer> expirerId;

  public static volatile SingularAttribute<PortletDefinitionImpl, Integer> approverId;

  public static volatile SingularAttribute<PortletDefinitionImpl, PortletTypeImpl> portletType;

  public static volatile SetAttribute<PortletDefinitionImpl, PortletEntityImpl> portletEntities;

  public static volatile SingularAttribute<PortletDefinitionImpl, PortletDescriptorKeyImpl> portletDescriptorKey;

  public static volatile MapAttribute<PortletDefinitionImpl, String, PortletLocalizationData> localizations;

  public static volatile SingularAttribute<PortletDefinitionImpl, String> title;

  public static volatile SingularAttribute<PortletDefinitionImpl, Long> internalPortletDefinitionId;

  public static volatile SingularAttribute<PortletDefinitionImpl, Integer> actionTimeout;

  public static volatile SingularAttribute<PortletDefinitionImpl, String> description;

  public static volatile SingularAttribute<PortletDefinitionImpl, Date> approvalDate;

  public static volatile SingularAttribute<PortletDefinitionImpl, String> name;

  public static volatile MapAttribute<PortletDefinitionImpl, String, PortletDefinitionParameterImpl> parameters;

  public static volatile SingularAttribute<PortletDefinitionImpl, PortletPreferencesImpl> portletPreferences;

  public static volatile SingularAttribute<PortletDefinitionImpl, Integer> renderTimeout;

  public static volatile SingularAttribute<PortletDefinitionImpl, Date> publishDate;

  public static volatile SingularAttribute<PortletDefinitionImpl, String> fname;

  public static volatile SingularAttribute<PortletDefinitionImpl, Integer> timeout;

  public static volatile SingularAttribute<PortletDefinitionImpl, Integer> eventTimeout;

}
