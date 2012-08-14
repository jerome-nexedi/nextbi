package org.jasig.portal.concurrency.locking;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ClusterMutex.class)
public abstract class ClusterMutex_ {

  public static volatile SingularAttribute<ClusterMutex, Long> entityVersion;

  public static volatile SingularAttribute<ClusterMutex, Long> id;

  public static volatile SingularAttribute<ClusterMutex, Date> lockEnd;

  public static volatile SingularAttribute<ClusterMutex, Date> lastUpdate;

  public static volatile SingularAttribute<ClusterMutex, Date> lockStart;

  public static volatile SingularAttribute<ClusterMutex, String> name;

  public static volatile SingularAttribute<ClusterMutex, String> serverId;

  public static volatile SingularAttribute<ClusterMutex, Boolean> locked;

}
