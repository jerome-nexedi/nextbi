package org.jasig.portal.events.aggr.dao.jpa;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.jasig.portal.events.aggr.IEventAggregatorStatus.ProcessingType;
import org.joda.time.DateTime;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(EventAggregatorStatusImpl.class)
public abstract class EventAggregatorStatusImpl_ {

  public static volatile SingularAttribute<EventAggregatorStatusImpl, Long> entityVersion;

  public static volatile SingularAttribute<EventAggregatorStatusImpl, Long> id;

  public static volatile SingularAttribute<EventAggregatorStatusImpl, DateTime> lastStart;

  public static volatile SingularAttribute<EventAggregatorStatusImpl, DateTime> lastEventDateTime;

  public static volatile SingularAttribute<EventAggregatorStatusImpl, String> serverName;

  public static volatile SingularAttribute<EventAggregatorStatusImpl, ProcessingType> processingType;

  public static volatile SingularAttribute<EventAggregatorStatusImpl, DateTime> lastEnd;

}
