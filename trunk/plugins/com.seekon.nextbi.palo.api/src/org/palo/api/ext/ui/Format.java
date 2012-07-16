package org.palo.api.ext.ui;

public abstract interface Format
{
  public abstract int getPriority();

  public abstract String getNumberFormatPattern();

  public abstract ColorDescriptor getBackGroundColor();

  public abstract FontDescriptor getFont();

  public abstract ColorDescriptor getFontColor();
}

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name:     org.palo.api.ext.ui.Format
 * JD-Core Version:    0.5.4
 */