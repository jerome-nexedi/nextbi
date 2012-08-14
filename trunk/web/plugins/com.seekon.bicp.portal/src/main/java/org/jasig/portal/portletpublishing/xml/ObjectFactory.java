package org.jasig.portal.portletpublishing.xml;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

/**
 * This object contains factory methods for each Java content interface and Java
 * element interface generated in the org.jasig.portal.portletpublishing.xml
 * package.
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

  private final static QName _SingleChoiceParameterInput_QNAME = new QName(
    "https://source.jasig.org/schemas/uportal/portlet-publishing",
    "single-choice-parameter-input");

  private final static QName _SingleTextPreferenceInput_QNAME = new QName(
    "https://source.jasig.org/schemas/uportal/portlet-publishing",
    "single-text-preference-input");

  private final static QName _SingleChoicePreferenceInput_QNAME = new QName(
    "https://source.jasig.org/schemas/uportal/portlet-publishing",
    "single-choice-preference-input");

  private final static QName _ParameterInput_QNAME = new QName(
    "https://source.jasig.org/schemas/uportal/portlet-publishing", "parameter-input");

  private final static QName _MultiChoicePreferenceInput_QNAME = new QName(
    "https://source.jasig.org/schemas/uportal/portlet-publishing",
    "multi-choice-preference-input");

  private final static QName _SingleTextParameterInput_QNAME = new QName(
    "https://source.jasig.org/schemas/uportal/portlet-publishing",
    "single-text-parameter-input");

  private final static QName _PreferenceInput_QNAME = new QName(
    "https://source.jasig.org/schemas/uportal/portlet-publishing",
    "preference-input");

  private final static QName _MultiTextPreferenceInput_QNAME = new QName(
    "https://source.jasig.org/schemas/uportal/portlet-publishing",
    "multi-text-preference-input");

  /**
   * Create a new ObjectFactory that can be used to create new instances of
   * schema derived classes for package: org.jasig.portal.portletpublishing.xml
   * 
   */
  public ObjectFactory() {
  }

  /**
   * Create an instance of {@link SingleChoicePreferenceInput }
   * 
   */
  public SingleChoicePreferenceInput createSingleChoicePreferenceInput() {
    return new SingleChoicePreferenceInput();
  }

  /**
   * Create an instance of {@link SingleTextPreferenceInput }
   * 
   */
  public SingleTextPreferenceInput createSingleTextPreferenceInput() {
    return new SingleTextPreferenceInput();
  }

  /**
   * Create an instance of {@link SingleChoiceParameterInput }
   * 
   */
  public SingleChoiceParameterInput createSingleChoiceParameterInput() {
    return new SingleChoiceParameterInput();
  }

  /**
   * Create an instance of {@link MultiTextPreferenceInput }
   * 
   */
  public MultiTextPreferenceInput createMultiTextPreferenceInput() {
    return new MultiTextPreferenceInput();
  }

  /**
   * Create an instance of {@link SingleTextParameterInput }
   * 
   */
  public SingleTextParameterInput createSingleTextParameterInput() {
    return new SingleTextParameterInput();
  }

  /**
   * Create an instance of {@link MultiChoicePreferenceInput }
   * 
   */
  public MultiChoicePreferenceInput createMultiChoicePreferenceInput() {
    return new MultiChoicePreferenceInput();
  }

  /**
   * Create an instance of {@link PortletPublishingDefinition }
   * 
   */
  public PortletPublishingDefinition createPortletPublishingDefinition() {
    return new PortletPublishingDefinition();
  }

  /**
   * Create an instance of {@link Step }
   * 
   */
  public Step createStep() {
    return new Step();
  }

  /**
   * Create an instance of {@link Parameter }
   * 
   */
  public Parameter createParameter() {
    return new Parameter();
  }

  /**
   * Create an instance of {@link Preference }
   * 
   */
  public Preference createPreference() {
    return new Preference();
  }

  /**
   * Create an instance of {@link Option }
   * 
   */
  public Option createOption() {
    return new Option();
  }

  /**
   * Create an instance of {@link JAXBElement }{@code <}
   * {@link SingleChoiceParameterInput }{@code >}
   * 
   */
  @XmlElementDecl(namespace = "https://source.jasig.org/schemas/uportal/portlet-publishing", name = "single-choice-parameter-input", substitutionHeadNamespace = "https://source.jasig.org/schemas/uportal/portlet-publishing", substitutionHeadName = "parameter-input")
  public JAXBElement<SingleChoiceParameterInput> createSingleChoiceParameterInput(
    SingleChoiceParameterInput value) {
    return new JAXBElement<SingleChoiceParameterInput>(
      _SingleChoiceParameterInput_QNAME, SingleChoiceParameterInput.class, null,
      value);
  }

  /**
   * Create an instance of {@link JAXBElement }{@code <}
   * {@link SingleTextPreferenceInput }{@code >}
   * 
   */
  @XmlElementDecl(namespace = "https://source.jasig.org/schemas/uportal/portlet-publishing", name = "single-text-preference-input", substitutionHeadNamespace = "https://source.jasig.org/schemas/uportal/portlet-publishing", substitutionHeadName = "preference-input")
  public JAXBElement<SingleTextPreferenceInput> createSingleTextPreferenceInput(
    SingleTextPreferenceInput value) {
    return new JAXBElement<SingleTextPreferenceInput>(
      _SingleTextPreferenceInput_QNAME, SingleTextPreferenceInput.class, null, value);
  }

  /**
   * Create an instance of {@link JAXBElement }{@code <}
   * {@link SingleChoicePreferenceInput }{@code >}
   * 
   */
  @XmlElementDecl(namespace = "https://source.jasig.org/schemas/uportal/portlet-publishing", name = "single-choice-preference-input", substitutionHeadNamespace = "https://source.jasig.org/schemas/uportal/portlet-publishing", substitutionHeadName = "preference-input")
  public JAXBElement<SingleChoicePreferenceInput> createSingleChoicePreferenceInput(
    SingleChoicePreferenceInput value) {
    return new JAXBElement<SingleChoicePreferenceInput>(
      _SingleChoicePreferenceInput_QNAME, SingleChoicePreferenceInput.class, null,
      value);
  }

  /**
   * Create an instance of {@link JAXBElement }{@code <}
   * {@link ParameterInputType }{@code >}
   * 
   */
  @XmlElementDecl(namespace = "https://source.jasig.org/schemas/uportal/portlet-publishing", name = "parameter-input")
  public JAXBElement<ParameterInputType> createParameterInput(
    ParameterInputType value) {
    return new JAXBElement<ParameterInputType>(_ParameterInput_QNAME,
      ParameterInputType.class, null, value);
  }

  /**
   * Create an instance of {@link JAXBElement }{@code <}
   * {@link MultiChoicePreferenceInput }{@code >}
   * 
   */
  @XmlElementDecl(namespace = "https://source.jasig.org/schemas/uportal/portlet-publishing", name = "multi-choice-preference-input", substitutionHeadNamespace = "https://source.jasig.org/schemas/uportal/portlet-publishing", substitutionHeadName = "preference-input")
  public JAXBElement<MultiChoicePreferenceInput> createMultiChoicePreferenceInput(
    MultiChoicePreferenceInput value) {
    return new JAXBElement<MultiChoicePreferenceInput>(
      _MultiChoicePreferenceInput_QNAME, MultiChoicePreferenceInput.class, null,
      value);
  }

  /**
   * Create an instance of {@link JAXBElement }{@code <}
   * {@link SingleTextParameterInput }{@code >}
   * 
   */
  @XmlElementDecl(namespace = "https://source.jasig.org/schemas/uportal/portlet-publishing", name = "single-text-parameter-input", substitutionHeadNamespace = "https://source.jasig.org/schemas/uportal/portlet-publishing", substitutionHeadName = "parameter-input")
  public JAXBElement<SingleTextParameterInput> createSingleTextParameterInput(
    SingleTextParameterInput value) {
    return new JAXBElement<SingleTextParameterInput>(
      _SingleTextParameterInput_QNAME, SingleTextParameterInput.class, null, value);
  }

  /**
   * Create an instance of {@link JAXBElement }{@code <}
   * {@link PreferenceInputType }{@code >}
   * 
   */
  @XmlElementDecl(namespace = "https://source.jasig.org/schemas/uportal/portlet-publishing", name = "preference-input")
  public JAXBElement<PreferenceInputType> createPreferenceInput(
    PreferenceInputType value) {
    return new JAXBElement<PreferenceInputType>(_PreferenceInput_QNAME,
      PreferenceInputType.class, null, value);
  }

  /**
   * Create an instance of {@link JAXBElement }{@code <}
   * {@link MultiTextPreferenceInput }{@code >}
   * 
   */
  @XmlElementDecl(namespace = "https://source.jasig.org/schemas/uportal/portlet-publishing", name = "multi-text-preference-input", substitutionHeadNamespace = "https://source.jasig.org/schemas/uportal/portlet-publishing", substitutionHeadName = "preference-input")
  public JAXBElement<MultiTextPreferenceInput> createMultiTextPreferenceInput(
    MultiTextPreferenceInput value) {
    return new JAXBElement<MultiTextPreferenceInput>(
      _MultiTextPreferenceInput_QNAME, MultiTextPreferenceInput.class, null, value);
  }

}
