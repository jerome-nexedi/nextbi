package com.tensegrity.palo.xmla;

import com.tensegrity.palo.xmla.builders.BuilderRegistry;
import com.tensegrity.palo.xmla.builders.VariableInfoBuilder;
import com.tensegrity.palojava.DimensionInfo;
import com.tensegrity.palojava.ElementInfo;
import com.tensegrity.palojava.VariableInfo;

public class XMLAVariableInfo implements VariableInfo {
  private final String[] VAR_TYPE_STRING = { "Unknown", "Member", "Numeric",
    "Hierarchy" };

  private final String[] VAR_PROC_TYPE_STRING = { "Unknown", "User input" };

  private final String[] VAR_SELECTION_TYPE_STRING = { "Unknown", "Value",
    "Interval", "Complex" };

  private final String[] VAR_INPUT_TYPE_STRING = { "Optional", "Mandatory",
    "Mandatory not initial", "Unknown" };

  private String name;

  private String id;

  private String uid;

  private String ordinal;

  private int variableType;

  private String dataType;

  private String characterMaxLength;

  private int variableProcessingType;

  private int variableSelectionType;

  private int variableInputType;

  private String referenceDimension;

  private String referenceHierarchy;

  private String defaultLow;

  private String defaultLowCap;

  private String defaultHigh;

  private String defaultHighCap;

  private String description;

  private DimensionInfo elementDimension = null;

  private boolean elementsSet = false;

  private ElementInfo value;

  private ElementInfo from;

  private ElementInfo to;

  private ElementInfo[] selectedElements;

  private boolean hideConsolidations;

  private String textValue;

  public void setName(String paramString) {
    this.name = paramString;
  }

  public void setId(String paramString) {
    this.id = paramString;
  }

  public void setUId(String paramString) {
    this.uid = paramString;
  }

  public void setOrdinal(String paramString) {
    this.ordinal = paramString;
  }

  public void setType(int paramInt) {
    this.variableType = paramInt;
  }

  public void setDataType(String paramString) {
    this.dataType = paramString;
  }

  public void setCharacterMaximumLength(String paramString) {
    this.characterMaxLength = paramString;
  }

  public void setVariableProcessingType(int paramInt) {
    this.variableProcessingType = paramInt;
  }

  public void setSelectionType(int paramInt) {
    this.variableSelectionType = paramInt;
  }

  public void setInputType(int paramInt) {
    this.variableInputType = paramInt;
  }

  public void setReferenceDimension(String paramString) {
    this.referenceDimension = paramString;
  }

  public void setReferenceHierarchy(String paramString) {
    this.referenceHierarchy = paramString;
  }

  public void setDefaultLow(String paramString) {
    this.defaultLow = paramString;
  }

  public void setDefaultLowCap(String paramString) {
    this.defaultLowCap = paramString;
  }

  public void setDefaultHigh(String paramString) {
    this.defaultHigh = paramString;
  }

  public void setDefaultHighCap(String paramString) {
    this.defaultHighCap = paramString;
  }

  public void setDescription(String paramString) {
    this.description = paramString;
  }

  public String getName() {
    return this.name;
  }

  public String getId() {
    return this.id;
  }

  public String getUId() {
    return this.uid;
  }

  public String getOrdinal() {
    return this.ordinal;
  }

  public String getDataType() {
    return this.dataType;
  }

  public String getCharacterMaximumLength() {
    return this.characterMaxLength;
  }

  public int getCharacterProcessingType() {
    return this.variableProcessingType;
  }

  public int getSelectionType() {
    return this.variableSelectionType;
  }

  public int getInputType() {
    return this.variableInputType;
  }

  public String getReferenceDimension() {
    return this.referenceDimension;
  }

  public String getReferenceHierarchy() {
    return this.referenceHierarchy;
  }

  public String getDefaultLow() {
    return this.defaultLow;
  }

  public String getDefaultLowCap() {
    return this.defaultLowCap;
  }

  public String getDefaultHigh() {
    return this.defaultHigh;
  }

  public String getDefaultHighCap() {
    return this.defaultHighCap;
  }

  public String getDescription() {
    return this.description;
  }

  public boolean areElementsSet() {
    return this.elementsSet;
  }

  public void loadVariableElements(XMLAClient paramXMLAClient,
    XMLAConnection paramXMLAConnection, XMLADatabaseInfo paramXMLADatabaseInfo) {
    BuilderRegistry.getInstance().getVariableInfoBuilder().requestVarElements(this,
      paramXMLAConnection, paramXMLADatabaseInfo, paramXMLAClient);
    this.elementsSet = true;
  }

  public void setElementDimension(DimensionInfo paramDimensionInfo) {
    this.elementDimension = paramDimensionInfo;
  }

  public DimensionInfo getElementDimension() {
    return this.elementDimension;
  }

  public String toString() {
    return "XMLA-Variable:            " + this.id + "\n"
      + "Name:                     " + this.name + "\n"
      + "UId:                      " + this.uid + "\n"
      + "Ordinal:                  " + this.ordinal + "\n"
      + "Type:                     " + this.VAR_TYPE_STRING[this.variableType]
      + "\n" + "Datatype:                 " + this.dataType + "\n"
      + "Character Maximum Length: " + this.characterMaxLength + "\n"
      + "Processing Type:          "
      + this.VAR_PROC_TYPE_STRING[this.variableProcessingType] + "\n"
      + "Selection Type:           "
      + this.VAR_SELECTION_TYPE_STRING[this.variableSelectionType] + "\n"
      + "Entry Type:               "
      + this.VAR_INPUT_TYPE_STRING[this.variableInputType] + "\n"
      + "Reference Dimension:      " + this.referenceDimension + "\n"
      + "Reference Hierarchy:      " + this.referenceHierarchy + "\n"
      + "Default Low:              " + this.defaultLow + "\n"
      + "Default Low Cap:          " + this.defaultLowCap + "\n"
      + "Default High:             " + this.defaultHigh + "\n"
      + "Default High Cap:         " + this.defaultHighCap + "\n"
      + "Description:              " + this.description + "\n"
      + "------------------------------------------------------------\n";
  }

  public int getType() {
    return this.variableType;
  }

  public ElementInfo[] getInterval() {
    return new ElementInfo[] { this.from, this.to };
  }

  public ElementInfo getValue() {
    return this.value;
  }

  public void setInterval(ElementInfo paramElementInfo1,
    ElementInfo paramElementInfo2) {
    this.from = paramElementInfo1;
    this.to = paramElementInfo2;
  }

  public void setInterval(String paramString1, String paramString2) {
    this.from = ((XMLADimensionInfo) this.elementDimension)
      .getMemberByIdInternal(paramString1);
    this.to = ((XMLADimensionInfo) this.elementDimension)
      .getMemberByIdInternal(paramString2);
  }

  public void setValue(ElementInfo paramElementInfo) {
    this.value = paramElementInfo;
  }

  public void setValue(String paramString) {
    this.value = ((XMLADimensionInfo) this.elementDimension)
      .getMemberByIdInternal(paramString);
  }

  public void setHideConsolidations(boolean paramBoolean) {
    this.hideConsolidations = paramBoolean;
  }

  public boolean getHideConsolidations() {
    return this.hideConsolidations;
  }

  public String getText() {
    if (this.textValue == null)
      return "";
    return this.textValue;
  }

  public void setText(String paramString) {
    this.textValue = paramString;
  }

  public ElementInfo[] getSelectedElements() {
    return this.selectedElements;
  }

  public void setSelectedElements(String[] paramArrayOfString) {
    if (paramArrayOfString == null) {
      this.selectedElements = null;
      return;
    }
    int i = 0;
    this.selectedElements = new ElementInfo[paramArrayOfString.length];
    for (String str : paramArrayOfString)
      this.selectedElements[(i++)] = ((XMLADimensionInfo) this.elementDimension)
        .getMemberByIdInternal(str);
  }

  public boolean canBeModified() {
    return false;
  }

  public boolean canCreateChildren() {
    return false;
  }
}

/*
 * Location:
 * D:\server\apache-tomcat-5.5.20\webapps\Palo-Pivot\WEB-INF\lib\paloxmla.jar
 * Qualified Name: com.tensegrity.palo.xmla.XMLAVariableInfo JD-Core Version:
 * 0.5.4
 */