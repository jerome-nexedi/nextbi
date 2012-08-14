package org.jasig.portal.portlet.dao.jpa;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(PortalCookieImpl.class)
public abstract class PortalCookieImpl_ {

  public static volatile SingularAttribute<PortalCookieImpl, Long> entityVersion;

  public static volatile SetAttribute<PortalCookieImpl, PortletCookieImpl> portletCookies;

  public static volatile SingularAttribute<PortalCookieImpl, Date> expires;

  public static volatile SingularAttribute<PortalCookieImpl, Long> internalPortalCookieId;

  public static volatile SingularAttribute<PortalCookieImpl, Date> created;

  public static volatile SingularAttribute<PortalCookieImpl, String> value;

}
