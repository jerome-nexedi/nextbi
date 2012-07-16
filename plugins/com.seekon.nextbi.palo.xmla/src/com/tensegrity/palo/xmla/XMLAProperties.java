package com.tensegrity.palo.xmla;

public class XMLAProperties
{
  public static String elementIndent = "  ";
  private String dataSourceInfo;
  private String catalog;
  private String format;
  private String content;
  private int compatibility = 0;
  private int beginRange = this.endRange = -1;
  private int endRange;

  public XMLAProperties()
  {
    setFormat("Tabular");
    setContent("SchemaData");
  }

  public String getDataSourceInfo()
  {
    return this.dataSourceInfo;
  }

  public void setDataSourceInfo(String paramString)
  {
    this.dataSourceInfo = paramString;
  }

  public String getCatalog()
  {
    return this.catalog;
  }

  public void setCatalog(String paramString)
  {
    this.catalog = paramString;
  }

  public int getBeginRange()
  {
    return this.beginRange;
  }

  public void setBeginRange(int paramInt)
  {
    this.beginRange = paramInt;
  }

  public int getEndRange()
  {
    return this.endRange;
  }

  public void setEndRange(int paramInt)
  {
    this.endRange = paramInt;
  }

  public String getFormat()
  {
    return this.format;
  }

  public void setFormat(String paramString)
  {
    this.format = paramString;
  }

  public String getContent()
  {
    return this.content;
  }

  public void setContent(String paramString)
  {
    this.content = paramString;
  }

  public void setCompatibility(int paramInt)
  {
    this.compatibility = paramInt;
  }

  public int getCompatibility()
  {
    return this.compatibility;
  }

  protected String getPropertyListXML(String paramString)
  {
    StringBuffer localStringBuffer = new StringBuffer("\n" + paramString + "<PropertyList>");
    if ((this.dataSourceInfo != null) && (this.dataSourceInfo.trim().length() > 0))
      localStringBuffer.append("\n" + paramString + elementIndent + "<DataSourceInfo>" + this.dataSourceInfo.trim() + "</DataSourceInfo>");
    if (this.compatibility > 0)
      localStringBuffer.append("\n" + paramString + elementIndent + "<DbpropMsmdMDXCompatibility>" + this.compatibility + "</DbpropMsmdMDXCompatibility>");
    if ((this.catalog != null) && (this.catalog.trim().length() > 0))
      localStringBuffer.append("\n" + paramString + elementIndent + "<Catalog>" + this.catalog.trim() + "</Catalog>");
    if ((this.format != null) && (this.format.trim().length() > 0))
      localStringBuffer.append("\n" + paramString + elementIndent + "<Format>" + this.format.trim() + "</Format>");
    if ((this.content != null) && (this.content.trim().length() > 0))
      localStringBuffer.append("\n" + paramString + elementIndent + "<Content>" + this.content.trim() + "</Content>");
    if (this.beginRange >= 0)
      localStringBuffer.append("\n" + paramString + elementIndent + "<BeginRange>" + this.beginRange + "</BeginRange>");
    if (this.endRange >= 0)
      localStringBuffer.append("\n" + paramString + elementIndent + "<EndRange>" + this.endRange + "</EndRange>");
    localStringBuffer.append("\n" + paramString + "</PropertyList>");
    return localStringBuffer.toString();
  }

  public String getXML(String paramString)
  {
    return "\n" + paramString + "<Properties>" + getPropertyListXML(new StringBuilder().append(paramString).append(elementIndent).toString()) + "\n" + paramString + "</Properties>";
  }
}

/* Location:           D:\server\apache-tomcat-5.5.20\webapps\Palo-Pivot\WEB-INF\lib\paloxmla.jar
 * Qualified Name:     com.tensegrity.palo.xmla.XMLAProperties
 * JD-Core Version:    0.5.4
 */