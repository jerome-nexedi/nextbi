package org.jasig.portal.io.xml.user;

import javax.xml.bind.annotation.XmlRegistry;

/**
 * This object contains factory methods for each Java content interface and Java
 * element interface generated in the org.jasig.portal.io.xml.user package.
 * <p>
 * An ObjectFactory allows you to programatically construct new instances of the
 * Java representation for XML content. The Java representation of XML content
 * can consist of schema derived interfaces and classes representing the binding
 * of schema type definitions, element declarations and model groups. Factory
 * methods for each of these are provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

  /**
   * Create a new ObjectFactory that can be used to create new instances of
   * schema derived classes for package: org.jasig.portal.io.xml.user
   * 
   */
  public ObjectFactory() {
  }

  /**
   * Create an instance of {@link ExternalTemplateUser }
   * 
   */
  public ExternalTemplateUser createExternalTemplateUser() {
    return new ExternalTemplateUser();
  }

  /**
   * Create an instance of {@link ExternalUser }
   * 
   */
  public ExternalUser createExternalUser() {
    return new ExternalUser();
  }

  /**
   * Create an instance of {@link Attribute }
   * 
   */
  public Attribute createAttribute() {
    return new Attribute();
  }

  /**
   * Create an instance of {@link UserType }
   * 
   */
  public UserType createUserType() {
    return new UserType();
  }

}
