package org.jasig.portal.layout.dao.jpa;

import javax.annotation.Generated;
import javax.persistence.metamodel.MapAttribute;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(StylesheetDescriptorImpl.class)
public abstract class StylesheetDescriptorImpl_ {

  public static volatile SingularAttribute<StylesheetDescriptorImpl, Long> entityVersion;

  public static volatile SingularAttribute<StylesheetDescriptorImpl, Long> id;

  public static volatile SingularAttribute<StylesheetDescriptorImpl, String> urlNodeSyntaxHelperName;

  public static volatile SetAttribute<StylesheetDescriptorImpl, StylesheetUserPreferencesImpl> stylesheetUserPreferences;

  public static volatile SingularAttribute<StylesheetDescriptorImpl, String> description;

  public static volatile SingularAttribute<StylesheetDescriptorImpl, String> name;

  public static volatile MapAttribute<StylesheetDescriptorImpl, String, OutputPropertyDescriptorImpl> outputProperties;

  public static volatile SingularAttribute<StylesheetDescriptorImpl, String> stylesheetResource;

  public static volatile MapAttribute<StylesheetDescriptorImpl, String, StylesheetParameterDescriptorImpl> stylesheetParameters;

  public static volatile MapAttribute<StylesheetDescriptorImpl, String, LayoutAttributeDescriptorImpl> layoutAttributes;

}
