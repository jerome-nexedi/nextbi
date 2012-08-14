package org.jasig.portal.i18n.dao.jpa;

import java.util.Locale;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(MessageImpl.class)
public abstract class MessageImpl_ {

  public static volatile SingularAttribute<MessageImpl, Long> id;

  public static volatile SingularAttribute<MessageImpl, Locale> locale;

  public static volatile SingularAttribute<MessageImpl, String> value;

  public static volatile SingularAttribute<MessageImpl, String> code;

}
