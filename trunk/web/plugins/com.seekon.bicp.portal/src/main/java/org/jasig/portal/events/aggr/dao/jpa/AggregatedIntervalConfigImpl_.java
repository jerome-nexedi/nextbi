package org.jasig.portal.events.aggr.dao.jpa;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.jasig.portal.events.aggr.AggregationInterval;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(AggregatedIntervalConfigImpl.class)
public abstract class AggregatedIntervalConfigImpl_ extends
  org.jasig.portal.events.aggr.dao.jpa.BaseAggregatedDimensionConfigImpl_ {

  public static volatile SingularAttribute<AggregatedIntervalConfigImpl, Long> id;

  public static volatile SetAttribute<AggregatedIntervalConfigImpl, AggregationInterval> includedIntervals;

  public static volatile SetAttribute<AggregatedIntervalConfigImpl, AggregationInterval> excludedIntervals;

  public static volatile SingularAttribute<AggregatedIntervalConfigImpl, Class> aggregatorType;

}
