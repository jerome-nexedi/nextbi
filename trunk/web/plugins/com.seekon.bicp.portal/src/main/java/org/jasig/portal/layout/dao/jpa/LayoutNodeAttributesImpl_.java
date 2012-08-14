package org.jasig.portal.layout.dao.jpa;

import javax.annotation.Generated;
import javax.persistence.metamodel.MapAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(LayoutNodeAttributesImpl.class)
public abstract class LayoutNodeAttributesImpl_ {

  public static volatile SingularAttribute<LayoutNodeAttributesImpl, Long> entityVersion;

  public static volatile SingularAttribute<LayoutNodeAttributesImpl, Long> id;

  public static volatile SingularAttribute<LayoutNodeAttributesImpl, String> nodeId;

  public static volatile MapAttribute<LayoutNodeAttributesImpl, String, String> attributes;

}
