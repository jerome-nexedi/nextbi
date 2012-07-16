package com.tensegrity.palo.xmla;

public class XMLAExecuteProperties extends XMLAProperties {
  private String axisFormat = "TupleFormat";

  public XMLAExecuteProperties() {
    setFormat("Multidimensional");
    setContent("Data");
  }

  public String getAxisFormat() {
    return this.axisFormat;
  }

  public void setAxisFormat(String paramString) {
    this.axisFormat = paramString;
  }

  protected String getPropertyListXML(String paramString) {
    StringBuffer localStringBuffer = new StringBuffer("\n" + paramString
      + "<PropertyList>");
    if ((getDataSourceInfo() != null) && (getDataSourceInfo().trim().length() > 0))
      localStringBuffer.append("\n" + paramString + elementIndent
        + "<DataSourceInfo>" + getDataSourceInfo().trim() + "</DataSourceInfo>");
    if ((getCatalog() != null) && (getCatalog().trim().length() > 0))
      localStringBuffer.append("\n" + paramString + elementIndent + "<Catalog>"
        + getCatalog().trim() + "</Catalog>");
    if ((getFormat() != null) && (getFormat().trim().length() > 0))
      localStringBuffer.append("\n" + paramString + elementIndent + "<Format>"
        + getFormat().trim() + "</Format>");
    if ((getContent() != null) && (getContent().trim().length() > 0))
      localStringBuffer.append("\n" + paramString + elementIndent + "<Content>"
        + getContent().trim() + "</Content>");
    if ((this.axisFormat != null) && (this.axisFormat.trim().length() > 0))
      localStringBuffer.append("\n" + paramString + elementIndent + "<AxisFormat>"
        + this.axisFormat.trim() + "</AxisFormat>");
    localStringBuffer.append("\n" + paramString + "</PropertyList>");
    return localStringBuffer.toString();
  }
}

/*
 * Location:
 * D:\server\apache-tomcat-5.5.20\webapps\Palo-Pivot\WEB-INF\lib\paloxmla.jar
 * Qualified Name: com.tensegrity.palo.xmla.XMLAExecuteProperties JD-Core
 * Version: 0.5.4
 */