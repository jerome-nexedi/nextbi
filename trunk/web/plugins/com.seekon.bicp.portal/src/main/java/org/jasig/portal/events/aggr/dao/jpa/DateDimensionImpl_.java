package org.jasig.portal.events.aggr.dao.jpa;

import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.jasig.portal.events.aggr.login.LoginAggregationImpl;
import org.joda.time.LocalDate;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(DateDimensionImpl.class)
public abstract class DateDimensionImpl_ {

  public static volatile SingularAttribute<DateDimensionImpl, Long> id;

  public static volatile SingularAttribute<DateDimensionImpl, String> term;

  public static volatile SingularAttribute<DateDimensionImpl, Integer> month;

  public static volatile SingularAttribute<DateDimensionImpl, Integer> year;

  public static volatile SingularAttribute<DateDimensionImpl, Integer> day;

  public static volatile CollectionAttribute<DateDimensionImpl, LoginAggregationImpl> loginAggregations;

  public static volatile SingularAttribute<DateDimensionImpl, LocalDate> date;

  public static volatile SingularAttribute<DateDimensionImpl, Integer> quarter;

  public static volatile SingularAttribute<DateDimensionImpl, Integer> week;

}
