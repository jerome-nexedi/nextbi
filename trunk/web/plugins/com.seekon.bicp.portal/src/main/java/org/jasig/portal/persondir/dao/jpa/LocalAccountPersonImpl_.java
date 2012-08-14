package org.jasig.portal.persondir.dao.jpa;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(LocalAccountPersonImpl.class)
public abstract class LocalAccountPersonImpl_ {

  public static volatile SingularAttribute<LocalAccountPersonImpl, Date> lastPasswordChange;

  public static volatile SingularAttribute<LocalAccountPersonImpl, Long> entityVersion;

  public static volatile SingularAttribute<LocalAccountPersonImpl, Long> id;

  public static volatile SingularAttribute<LocalAccountPersonImpl, String> name;

  public static volatile CollectionAttribute<LocalAccountPersonImpl, LocalAccountPersonAttributeImpl> attributes;

  public static volatile SingularAttribute<LocalAccountPersonImpl, String> password;

}
