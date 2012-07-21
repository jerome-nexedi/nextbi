/*     */package org.palo.viewapi.uimodels.formats;

/*     */
/*     */import java.util.ArrayList; /*     */
import org.palo.api.ext.ui.ColorDescriptor; /*     */
import org.palo.api.ext.ui.FontDescriptor;

/*     */
/*     */public class FormatImpl
/*     */implements Format
/*     */{
  /*     */private final String id;

  /*     */private ColorDescriptor backgroundColor;

  /*     */private String numberFormat;

  /*     */private FontDescriptor fontData;

  /*     */private ColorDescriptor foregroundColor;

  /* 51 */private final ArrayList<BorderData> borderData = new ArrayList();

  /* 52 */private final ArrayList<Double> minValues = new ArrayList();

  /* 53 */private final ArrayList<Double> maxValues = new ArrayList();

  /* 54 */private final ArrayList<ColorDescriptor> backgroundValues = new ArrayList();

  /* 55 */private final ArrayList<ColorDescriptor> foregroundValues = new ArrayList();

  /* 56 */private final ArrayList<FontDescriptor> fontValues = new ArrayList();

  /* 57 */private final ArrayList<FormatRangeInfo> ranges = new ArrayList();

  /*     */
  /*     */public FormatImpl(String id, ColorDescriptor backgroundColor,
    String numberFormat, FontDescriptor font, ColorDescriptor fontColor,
    BorderData[] borders, TrafficLightData traffic)
  /*     */{
    /* 64 */this.backgroundColor = backgroundColor;
    /* 65 */this.numberFormat = numberFormat;
    /* 66 */this.fontData = font;
    /* 67 */this.foregroundColor = fontColor;
    /* 68 */setBorderData(borders);
    /* 69 */setTrafficLightData(traffic);
    /* 70 */this.id = id;
    /*     */}

  /*     */
  /*     */private FormatImpl(FormatImpl format)
  /*     */{
    /* 75 */this.backgroundColor = format.backgroundColor;
    /* 76 */this.numberFormat = format.numberFormat;
    /* 77 */this.fontData = format.fontData;
    /* 78 */this.foregroundColor = format.foregroundColor;
    /* 79 */setBorderData(format.getBorderData());
    /* 80 */setTrafficLightData(format.getTrafficLightData());
    /* 81 */this.id = format.id;
    /* 82 */this.ranges.clear();
    /* 83 */this.ranges.addAll(format.ranges);
    /*     */}

  /*     */
  /*     */public FormatImpl(String id) {
    /* 87 */this.id = id;
    /*     */}

  /*     */
  /*     */public String getId() {
    /* 91 */return this.id;
    /*     */}

  /*     */
  /*     */public final void setBackgroundColor(ColorDescriptor backgroundColor) {
    /* 95 */this.backgroundColor = backgroundColor;
    /*     */}

  /*     */
  /*     */public final ColorDescriptor getBackgroundColor() {
    /* 99 */return this.backgroundColor;
    /*     */}

  /*     */
  /*     */public final void setNumberFormat(String numberFormat) {
    /* 103 */this.numberFormat = numberFormat;
    /*     */}

  /*     */
  /*     */public final String getNumberFormat() {
    /* 107 */return this.numberFormat;
    /*     */}

  /*     */
  /*     */public final void setFontData(FontDescriptor fontData) {
    /* 111 */this.fontData = fontData;
    /*     */}

  /*     */
  /*     */public final FontDescriptor getFontData() {
    /* 115 */return this.fontData;
    /*     */}

  /*     */
  /*     */public final void setForegroundColor(ColorDescriptor fontColor) {
    /* 119 */this.foregroundColor = fontColor;
    /*     */}

  /*     */
  /*     */public final ColorDescriptor getForegroundColor() {
    /* 123 */return this.foregroundColor;
    /*     */}

  /*     */
  /*     */public final void setBorderData(BorderData[] borders) {
    /* 127 */if (borders != null)
      /* 128 */for (BorderData f : borders)
        /* 129 */this.borderData.add(f);
    /*     */}

  /*     */
  /*     */public final void addBorderData(BorderData border)
  /*     */{
    /* 135 */this.borderData.add(border);
    /*     */}

  /*     */
  /*     */public final BorderData[] getBorderData() {
    /* 139 */return (BorderData[]) this.borderData.toArray(new BorderData[0]);
    /*     */}

  /*     */
  /*     */public final void addTrafficLightData(double min, double max,
    ColorDescriptor background, ColorDescriptor foreground, FontDescriptor font)
  /*     */{
    /* 144 */this.minValues.add(Double.valueOf(min));
    /* 145 */this.maxValues.add(Double.valueOf(max));
    /* 146 */this.backgroundValues.add(background);
    /* 147 */this.foregroundValues.add(foreground);
    /* 148 */this.fontValues.add(font);
    /*     */}

  /*     */
  /*     */public final void setTrafficLightData(TrafficLightData traffic) {
    /* 152 */this.minValues.clear();
    /* 153 */this.maxValues.clear();
    /* 154 */this.backgroundValues.clear();
    /* 155 */this.foregroundValues.clear();
    /* 156 */this.fontValues.clear();
    /* 157 */if (traffic != null) {
      /* 158 */int i = 0;
      for (int n = traffic.getSize(); i < n; ++i)
        /* 159 */addTrafficLightData(
        /* 160 */traffic.getMinValueAt(i),
        /* 161 */traffic.getMaxValueAt(i),
        /* 162 */traffic.getBackgroundColorAt(i),
        /* 163 */traffic.getForegroundColorAt(i),
        /* 164 */traffic.getFontAt(i));
      /*     */}
    /*     */}

  /*     */
  /*     */public final TrafficLightData getTrafficLightData()
  /*     */{
    /* 170 */if ((this.backgroundValues.size() == 0)
      && (this.foregroundValues.size() == 0) &&
      /* 171 */(this.minValues.size() == 0) && (this.maxValues.size() == 0) &&
      /* 172 */(this.fontValues.size() == 0)) {
      /* 173 */return null;
      /*     */}
    /*     */
    /* 176 */double[] minVals = new double[this.minValues.size()];
    /* 177 */double[] maxVals = new double[this.maxValues.size()];
    /* 178 */for (int i = 0; i < minVals.length; ++i) {
      /* 179 */minVals[i] = ((Double) this.minValues.get(i)).doubleValue();
      /* 180 */maxVals[i] = ((Double) this.maxValues.get(i)).doubleValue();
      /*     */}
    /* 182 */return new TrafficLightData(
    /* 183 */minVals, maxVals, (ColorDescriptor[]) this.backgroundValues
      .toArray(new ColorDescriptor[0]),
    /* 184 */(ColorDescriptor[]) this.foregroundValues
      .toArray(new ColorDescriptor[0]),
    /* 185 */(FontDescriptor[]) this.fontValues.toArray(new FontDescriptor[0]));
    /*     */}

  /*     */
  /*     */public final Format copy() {
    /* 189 */return new FormatImpl(this);
    /*     */}

  /*     */
  /*     */public String toString() {
    /* 193 */StringBuffer result = new StringBuffer("Format Id = " + this.id
      + "\n  Font: ");
    /*     */
    /* 195 */if (this.fontData == null) {
      /* 196 */result.append("<null>\n");
      /*     */} else {
      /* 198 */result.append("\n");
      /* 199 */result.append("    " + this.fontData.getName() + "-"
        + this.fontData.getSize() + "-" + this.fontData.isBold() + "-"
        + this.fontData.isItalic() + "-" + this.fontData.isUnderlined() + "\n");
      /*     */}
    /*     */
    /* 202 */if (this.borderData.size() > 0) {
      /* 203 */result.append("  Border: <");
      /* 204 */boolean first = true;
      /* 205 */for (BorderData border : this.borderData) {
        /* 206 */if (!first) {
          /* 207 */result.append(", ");
          /*     */}
        /* 209 */first = false;
        /* 210 */result.append(border);
        /*     */}
      /* 212 */result.append(">\n");
      /*     */} else {
      /* 214 */result.append("  Border: <null>\n");
      /*     */}
    /*     */
    /* 217 */if (this.numberFormat == null)
      /* 218 */result.append("  NumberFormat: <null>\n");
    /*     */else {
      /* 220 */result.append("  NumberFormat: " + this.numberFormat + "\n");
      /*     */}
    /*     */
    /* 223 */if (this.foregroundColor == null)
      /* 224 */result.append("  Foreground: <null>\n");
    /*     */else {
      /* 226 */result.append("  Foreground: " + this.foregroundColor + "\n");
      /*     */}
    /*     */
    /* 229 */if (this.backgroundColor == null)
      /* 230 */result.append("  Background: <null>\n");
    /*     */else {
      /* 232 */result.append("  Background: " + this.backgroundColor + "\n");
      /*     */}
    /*     */
    /* 235 */if (this.minValues.size() == 0) {
      /* 236 */result.append("  Trafficlight: <none>\n");
      /*     */} else {
      /* 238 */result.append("  Trafficlight:\n");
      /* 239 */TrafficLightData traffic = getTrafficLightData();
      /* 240 */int i = 0;
      for (int n = traffic.getSize(); i < n; ++i) {
        /* 241 */result.append("    Min: " + traffic.getMinValueAt(i) + ", ");
        /* 242 */result.append("    Max: " + traffic.getMaxValueAt(i) + ", ");
        /* 243 */result.append("    Foreground: " + traffic.getForegroundColorAt(i)
          + ", ");
        /* 244 */result.append("    Background: " + traffic.getBackgroundColorAt(i)
          + ", ");
        /* 245 */FontDescriptor font = traffic.getFontAt(i);
        /* 246 */result.append("    Font: ");
        /* 247 */if (font != null)
          /* 248 */result.append(font.getName() + "-" + font.getSize() + "-"
            + font.isBold() + "-" + font.isItalic() + "-" + font.isUnderlined()
            + "\n");
        /*     */else {
          /* 250 */result.append("<null>\n");
          /*     */}
        /*     */}
      /*     */}
    /*     */
    /* 255 */return result.toString();
    /*     */}

  /*     */
  /*     */public FormatRangeInfo[] getRanges() {
    /* 259 */return (FormatRangeInfo[]) this.ranges.toArray(new FormatRangeInfo[0]);
    /*     */}

  /*     */
  /*     */public void addRange(FormatRangeInfo info) {
    /* 263 */this.ranges.add(info);
    /*     */}

  /*     */
  /*     */public FormatRangeInfo getRangeAt(int index) {
    /* 267 */return (FormatRangeInfo) this.ranges.get(index);
    /*     */}

  /*     */
  /*     */public int getRangeCount() {
    /* 271 */return this.ranges.size();
    /*     */}

  /*     */
  /*     */public void removeAllRanges() {
    /* 275 */this.ranges.clear();
    /*     */}

  /*     */
  /*     */public void removeRange(FormatRangeInfo info) {
    /* 279 */this.ranges.remove(info);
    /*     */}
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi
 * .jar Qualified Name: org.palo.viewapi.uimodels.formats.FormatImpl JD-Core
 * Version: 0.5.4
 */