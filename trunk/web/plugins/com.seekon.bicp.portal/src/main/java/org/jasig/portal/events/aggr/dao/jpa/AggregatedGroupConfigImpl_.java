package org.jasig.portal.events.aggr.dao.jpa;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.jasig.portal.events.aggr.groups.AggregatedGroupMappingImpl;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(AggregatedGroupConfigImpl.class)
public abstract class AggregatedGroupConfigImpl_ extends
  org.jasig.portal.events.aggr.dao.jpa.BaseAggregatedDimensionConfigImpl_ {

  public static volatile SetAttribute<AggregatedGroupConfigImpl, AggregatedGroupMappingImpl> excludedGroups;

  public static volatile SingularAttribute<AggregatedGroupConfigImpl, Long> id;

  public static volatile SingularAttribute<AggregatedGroupConfigImpl, Class> aggregatorType;

  public static volatile SetAttribute<AggregatedGroupConfigImpl, AggregatedGroupMappingImpl> includedGroups;

}
