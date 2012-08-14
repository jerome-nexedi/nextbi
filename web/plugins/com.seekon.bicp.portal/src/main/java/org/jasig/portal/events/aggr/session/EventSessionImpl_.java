package org.jasig.portal.events.aggr.session;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.jasig.portal.events.aggr.groups.AggregatedGroupMappingImpl;
import org.joda.time.DateTime;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(EventSessionImpl.class)
public abstract class EventSessionImpl_ {

  public static volatile SingularAttribute<EventSessionImpl, Long> id;

  public static volatile SetAttribute<EventSessionImpl, AggregatedGroupMappingImpl> groupMappings;

  public static volatile SingularAttribute<EventSessionImpl, DateTime> lastAccessed;

  public static volatile SingularAttribute<EventSessionImpl, String> eventSessionId;

}
