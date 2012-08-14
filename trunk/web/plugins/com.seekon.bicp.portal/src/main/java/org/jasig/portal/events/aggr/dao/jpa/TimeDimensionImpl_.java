package org.jasig.portal.events.aggr.dao.jpa;

import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.jasig.portal.events.aggr.login.LoginAggregationImpl;
import org.joda.time.LocalTime;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(TimeDimensionImpl.class)
public abstract class TimeDimensionImpl_ {

  public static volatile SingularAttribute<TimeDimensionImpl, Integer> minute;

  public static volatile SingularAttribute<TimeDimensionImpl, Long> id;

  public static volatile SingularAttribute<TimeDimensionImpl, LocalTime> time;

  public static volatile SingularAttribute<TimeDimensionImpl, Integer> fiveMinuteIncrement;

  public static volatile SingularAttribute<TimeDimensionImpl, Integer> hour;

  public static volatile CollectionAttribute<TimeDimensionImpl, LoginAggregationImpl> loginAggregations;

}
