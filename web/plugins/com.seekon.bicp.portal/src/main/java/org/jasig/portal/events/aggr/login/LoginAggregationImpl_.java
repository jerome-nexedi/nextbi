package org.jasig.portal.events.aggr.login;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.jasig.portal.events.aggr.AggregationInterval;
import org.jasig.portal.events.aggr.dao.jpa.DateDimensionImpl;
import org.jasig.portal.events.aggr.dao.jpa.TimeDimensionImpl;
import org.jasig.portal.events.aggr.groups.AggregatedGroupMappingImpl;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(LoginAggregationImpl.class)
public abstract class LoginAggregationImpl_ {

  public static volatile SingularAttribute<LoginAggregationImpl, Long> id;

  public static volatile SetAttribute<LoginAggregationImpl, String> uniqueUserNames;

  public static volatile SingularAttribute<LoginAggregationImpl, Integer> duration;

  public static volatile SingularAttribute<LoginAggregationImpl, DateDimensionImpl> dateDimension;

  public static volatile SingularAttribute<LoginAggregationImpl, AggregationInterval> interval;

  public static volatile SingularAttribute<LoginAggregationImpl, Integer> uniqueLoginCount;

  public static volatile SingularAttribute<LoginAggregationImpl, Integer> loginCount;

  public static volatile SingularAttribute<LoginAggregationImpl, AggregatedGroupMappingImpl> aggregatedGroup;

  public static volatile SingularAttribute<LoginAggregationImpl, TimeDimensionImpl> timeDimension;

}
