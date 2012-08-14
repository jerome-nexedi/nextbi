package org.jasig.portal.io.xml.eventaggr;

import javax.xml.bind.annotation.XmlRegistry;

/**
 * This object contains factory methods for each Java content interface and Java
 * element interface generated in the org.jasig.portal.io.xml.eventaggr package.
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
   * schema derived classes for package: org.jasig.portal.io.xml.eventaggr
   * 
   */
  public ObjectFactory() {
  }

  /**
   * Create an instance of {@link ExternalEventAggregationConfiguration }
   * 
   */
  public ExternalEventAggregationConfiguration createExternalEventAggregationConfiguration() {
    return new ExternalEventAggregationConfiguration();
  }

  /**
   * Create an instance of {@link ExternalAggregatedGroupConfig }
   * 
   */
  public ExternalAggregatedGroupConfig createExternalAggregatedGroupConfig() {
    return new ExternalAggregatedGroupConfig();
  }

  /**
   * Create an instance of {@link ExternalAggregatedGroupMapping }
   * 
   */
  public ExternalAggregatedGroupMapping createExternalAggregatedGroupMapping() {
    return new ExternalAggregatedGroupMapping();
  }

  /**
   * Create an instance of {@link ExternalAggregatedIntervalConfig }
   * 
   */
  public ExternalAggregatedIntervalConfig createExternalAggregatedIntervalConfig() {
    return new ExternalAggregatedIntervalConfig();
  }

  /**
   * Create an instance of {@link ExternalQuarterDetail }
   * 
   */
  public ExternalQuarterDetail createExternalQuarterDetail() {
    return new ExternalQuarterDetail();
  }

  /**
   * Create an instance of {@link ExternalTermDetail }
   * 
   */
  public ExternalTermDetail createExternalTermDetail() {
    return new ExternalTermDetail();
  }

}
