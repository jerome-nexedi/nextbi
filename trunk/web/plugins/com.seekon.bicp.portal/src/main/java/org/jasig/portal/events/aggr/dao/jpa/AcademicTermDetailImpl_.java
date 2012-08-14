package org.jasig.portal.events.aggr.dao.jpa;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.joda.time.DateTime;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(AcademicTermDetailImpl.class)
public abstract class AcademicTermDetailImpl_ {

  public static volatile SingularAttribute<AcademicTermDetailImpl, Long> id;

  public static volatile SingularAttribute<AcademicTermDetailImpl, String> termName;

  public static volatile SingularAttribute<AcademicTermDetailImpl, DateTime> start;

  public static volatile SingularAttribute<AcademicTermDetailImpl, DateTime> end;

}
