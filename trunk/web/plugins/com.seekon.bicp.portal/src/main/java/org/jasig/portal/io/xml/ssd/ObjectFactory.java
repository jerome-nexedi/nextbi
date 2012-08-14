package org.jasig.portal.io.xml.ssd;

import javax.xml.bind.annotation.XmlRegistry;

/**
 * This object contains factory methods for each Java content interface and Java
 * element interface generated in the org.jasig.portal.io.xml.ssd package.
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
   * schema derived classes for package: org.jasig.portal.io.xml.ssd
   * 
   */
  public ObjectFactory() {
  }

  /**
   * Create an instance of {@link ExternalStylesheetDescriptor }
   * 
   */
  public ExternalStylesheetDescriptor createExternalStylesheetDescriptor() {
    return new ExternalStylesheetDescriptor();
  }

  /**
   * Create an instance of {@link ExternalLayoutAttributeDescriptor }
   * 
   */
  public ExternalLayoutAttributeDescriptor createExternalLayoutAttributeDescriptor() {
    return new ExternalLayoutAttributeDescriptor();
  }

  /**
   * Create an instance of {@link ExternalStylesheetParameterDescriptor }
   * 
   */
  public ExternalStylesheetParameterDescriptor createExternalStylesheetParameterDescriptor() {
    return new ExternalStylesheetParameterDescriptor();
  }

  /**
   * Create an instance of {@link ExternalStylesheetData }
   * 
   */
  public ExternalStylesheetData createExternalStylesheetData() {
    return new ExternalStylesheetData();
  }

  /**
   * Create an instance of {@link ExternalOutputPropertyDescriptor }
   * 
   */
  public ExternalOutputPropertyDescriptor createExternalOutputPropertyDescriptor() {
    return new ExternalOutputPropertyDescriptor();
  }

}
