package org.jasig.portal.fragment.subscribe.dao.jpa;

import java.util.Calendar;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(UserFragmentSubscriptionImpl.class)
public abstract class UserFragmentSubscriptionImpl_ {

  public static volatile SingularAttribute<UserFragmentSubscriptionImpl, Long> entityVersion;

  public static volatile SingularAttribute<UserFragmentSubscriptionImpl, Calendar> creationDate;

  public static volatile SingularAttribute<UserFragmentSubscriptionImpl, String> createdBy;

  public static volatile SingularAttribute<UserFragmentSubscriptionImpl, Long> userFragmentInfoId;

  public static volatile SingularAttribute<UserFragmentSubscriptionImpl, Integer> userId;

  public static volatile SingularAttribute<UserFragmentSubscriptionImpl, Boolean> active;

  public static volatile SingularAttribute<UserFragmentSubscriptionImpl, Calendar> lastUpdatedDate;

  public static volatile SingularAttribute<UserFragmentSubscriptionImpl, String> fragmentOwner;

}
