package org.jasig.portal.permission.dao.jpa;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(PermissionOwnerImpl.class)
public abstract class PermissionOwnerImpl_ {

  public static volatile SingularAttribute<PermissionOwnerImpl, Long> entityVersion;

  public static volatile SingularAttribute<PermissionOwnerImpl, Long> id;

  public static volatile SingularAttribute<PermissionOwnerImpl, String> description;

  public static volatile SingularAttribute<PermissionOwnerImpl, String> name;

  public static volatile SetAttribute<PermissionOwnerImpl, PermissionActivityImpl> activities;

  public static volatile SingularAttribute<PermissionOwnerImpl, String> fname;

}
