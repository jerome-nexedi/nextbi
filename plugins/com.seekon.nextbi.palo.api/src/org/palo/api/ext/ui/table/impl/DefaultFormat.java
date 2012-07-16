/*    */package org.palo.api.ext.ui.table.impl;

/*    */
/*    */import org.palo.api.ext.ui.ColorDescriptor; /*    */
import org.palo.api.ext.ui.FontDescriptor; /*    */
import org.palo.api.ext.ui.Format;

/*    */
/*    */class DefaultFormat
/*    */implements Format
/*    */{
  /*    */private int priority;

  /*    */private ColorDescriptor bgColor;

  /*    */private ColorDescriptor fontColor;

  /*    */private FontDescriptor font;

  /*    */private String nrFmtPattern;

  /*    */
  /*    */public final ColorDescriptor getBackGroundColor()
  /*    */{
    /* 59 */return this.bgColor;
    /*    */}

  /*    */
  /*    */public final FontDescriptor getFont() {
    /* 63 */return this.font;
    /*    */}

  /*    */
  /*    */public final ColorDescriptor getFontColor() {
    /* 67 */return this.fontColor;
    /*    */}

  /*    */
  /*    */public final String getNumberFormatPattern() {
    /* 71 */return this.nrFmtPattern;
    /*    */}

  /*    */
  /*    */public final int getPriority() {
    /* 75 */return this.priority;
    /*    */}

  /*    */
  /*    */final void setBgColor(ColorDescriptor bgColor)
  /*    */{
    /* 80 */this.bgColor = bgColor;
    /*    */}

  /*    */
  /*    */final void setFont(FontDescriptor font) {
    /* 84 */this.font = font;
    /*    */}

  /*    */
  /*    */final void setFontColor(ColorDescriptor fontColor) {
    /* 88 */this.fontColor = fontColor;
    /*    */}

  /*    */
  /*    */final void setNumberFormatPattern(String nrFmtPattern) {
    /* 92 */this.nrFmtPattern = nrFmtPattern;
    /*    */}

  /*    */
  /*    */final void setPriority(int priority) {
    /* 96 */this.priority = priority;
    /*    */}
  /*    */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name: org.palo.api.ext.ui.table.impl.DefaultFormat JD-Core Version:
 * 0.5.4
 */