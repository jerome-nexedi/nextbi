package org.jasig.portal.portlet.dao.jpa;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(PortletCookieImpl.class)
public abstract class PortletCookieImpl_ {

  public static volatile SingularAttribute<PortletCookieImpl, Long> entityVersion;

  public static volatile SingularAttribute<PortletCookieImpl, Long> internalPortletCookieId;

  public static volatile SingularAttribute<PortletCookieImpl, Date> expires;

  public static volatile SingularAttribute<PortletCookieImpl, PortalCookieImpl> portalCookie;

  public static volatile SingularAttribute<PortletCookieImpl, String> name;

  public static volatile SingularAttribute<PortletCookieImpl, Boolean> secure;

  public static volatile SingularAttribute<PortletCookieImpl, String> value;

  public static volatile SingularAttribute<PortletCookieImpl, String> path;

  public static volatile SingularAttribute<PortletCookieImpl, String> domain;

  public static volatile SingularAttribute<PortletCookieImpl, String> comment;

  public static volatile SingularAttribute<PortletCookieImpl, Integer> version;

}
