package org.palo.viewapi.uimodels.formats;

import org.palo.api.ext.ui.ColorDescriptor;
import org.palo.api.ext.ui.FontDescriptor;

public abstract interface Format
{
  public abstract String getId();

  public abstract String getNumberFormat();

  public abstract void setNumberFormat(String paramString);

  public abstract ColorDescriptor getForegroundColor();

  public abstract void setForegroundColor(ColorDescriptor paramColorDescriptor);

  public abstract ColorDescriptor getBackgroundColor();

  public abstract void setBackgroundColor(ColorDescriptor paramColorDescriptor);

  public abstract FontDescriptor getFontData();

  public abstract void setFontData(FontDescriptor paramFontDescriptor);

  public abstract BorderData[] getBorderData();

  public abstract void setBorderData(BorderData[] paramArrayOfBorderData);

  public abstract void addBorderData(BorderData paramBorderData);

  public abstract TrafficLightData getTrafficLightData();

  public abstract void setTrafficLightData(TrafficLightData paramTrafficLightData);

  public abstract void addTrafficLightData(double paramDouble1, double paramDouble2, ColorDescriptor paramColorDescriptor1, ColorDescriptor paramColorDescriptor2, FontDescriptor paramFontDescriptor);

  public abstract FormatRangeInfo[] getRanges();

  public abstract int getRangeCount();

  public abstract FormatRangeInfo getRangeAt(int paramInt);

  public abstract void addRange(FormatRangeInfo paramFormatRangeInfo);

  public abstract void removeRange(FormatRangeInfo paramFormatRangeInfo);

  public abstract void removeAllRanges();

  public abstract Format copy();
}

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi.jar
 * Qualified Name:     org.palo.viewapi.uimodels.formats.Format
 * JD-Core Version:    0.5.4
 */