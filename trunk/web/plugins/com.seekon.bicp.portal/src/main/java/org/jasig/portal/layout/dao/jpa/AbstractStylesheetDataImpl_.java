package org.jasig.portal.layout.dao.jpa;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.jasig.portal.layout.om.IStylesheetData.Scope;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(AbstractStylesheetDataImpl.class)
public abstract class AbstractStylesheetDataImpl_ {

  public static volatile SingularAttribute<AbstractStylesheetDataImpl, Long> entityVersion;

  public static volatile SingularAttribute<AbstractStylesheetDataImpl, Scope> scope;

  public static volatile SingularAttribute<AbstractStylesheetDataImpl, String> description;

  public static volatile SingularAttribute<AbstractStylesheetDataImpl, String> name;

  public static volatile SingularAttribute<AbstractStylesheetDataImpl, String> defaultValue;

}
