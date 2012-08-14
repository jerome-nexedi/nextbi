package org.jasig.portal.events.aggr.dao.jpa;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.joda.time.MonthDay;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(QuarterDetailImpl.class)
public abstract class QuarterDetailImpl_ {

  public static volatile SingularAttribute<QuarterDetailImpl, Long> id;

  public static volatile SingularAttribute<QuarterDetailImpl, MonthDay> start;

  public static volatile SingularAttribute<QuarterDetailImpl, Integer> quarterId;

  public static volatile SingularAttribute<QuarterDetailImpl, MonthDay> end;

}
