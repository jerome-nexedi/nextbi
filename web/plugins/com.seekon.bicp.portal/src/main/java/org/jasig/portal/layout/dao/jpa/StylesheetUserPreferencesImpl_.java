package org.jasig.portal.layout.dao.jpa;

import javax.annotation.Generated;
import javax.persistence.metamodel.MapAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(StylesheetUserPreferencesImpl.class)
public abstract class StylesheetUserPreferencesImpl_ {

  public static volatile SingularAttribute<StylesheetUserPreferencesImpl, Long> entityVersion;

  public static volatile SingularAttribute<StylesheetUserPreferencesImpl, Long> id;

  public static volatile SingularAttribute<StylesheetUserPreferencesImpl, Integer> profileId;

  public static volatile SingularAttribute<StylesheetUserPreferencesImpl, Integer> userId;

  public static volatile MapAttribute<StylesheetUserPreferencesImpl, String, String> outputProperties;

  public static volatile MapAttribute<StylesheetUserPreferencesImpl, String, String> parameters;

  public static volatile SingularAttribute<StylesheetUserPreferencesImpl, StylesheetDescriptorImpl> stylesheetDescriptor;

  public static volatile MapAttribute<StylesheetUserPreferencesImpl, String, LayoutNodeAttributesImpl> layoutAttributes;

}
