package org.jasig.portal.events.handlers.db;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.joda.time.DateTime;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(PersistentPortalEvent.class)
public abstract class PersistentPortalEvent_ {

  public static volatile SingularAttribute<PersistentPortalEvent, DateTime> timestamp;

  public static volatile SingularAttribute<PersistentPortalEvent, Long> id;

  public static volatile SingularAttribute<PersistentPortalEvent, Boolean> aggregated;

  public static volatile SingularAttribute<PersistentPortalEvent, String> userName;

  public static volatile SingularAttribute<PersistentPortalEvent, Class> eventType;

  public static volatile SingularAttribute<PersistentPortalEvent, String> serverId;

  public static volatile SingularAttribute<PersistentPortalEvent, String> eventData;

  public static volatile SingularAttribute<PersistentPortalEvent, String> eventSessionId;

}
