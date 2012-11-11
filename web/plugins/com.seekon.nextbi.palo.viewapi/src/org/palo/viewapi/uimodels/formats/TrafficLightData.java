/*     */package org.palo.viewapi.uimodels.formats;

/*     */
/*     */import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import org.palo.api.ext.ui.ColorDescriptor;
import org.palo.api.ext.ui.FontDescriptor;

/*     */
/*     */public class TrafficLightData
/*     */{
  /*     */private final ArrayList<Double> minValues;

  /*     */private final ArrayList<Double> maxValues;

  /*     */private final ArrayList<ColorDescriptor> backgroundColors;

  /*     */private final ArrayList<ColorDescriptor> foregroundColors;

  /*     */private final ArrayList<FontDescriptor> fonts;

  /*     */
  /*     */public TrafficLightData(double[] minVals, double[] maxVals,
    ColorDescriptor[] backgroundColors, ColorDescriptor[] foregroundColors,
    FontDescriptor[] fontDatas)
  /*     */{
    /* 102 */this.minValues = new ArrayList();
    /* 103 */this.maxValues = new ArrayList();
    /* 104 */this.backgroundColors = new ArrayList();
    /* 105 */this.foregroundColors = new ArrayList();
    /* 106 */this.fonts = new ArrayList();
    /*     */
    /* 108 */for (double d : minVals)
      this.minValues.add(Double.valueOf(d));
    /* 109 */for (double d : maxVals)
      this.maxValues.add(Double.valueOf(d));
    /* 110 */this.backgroundColors.addAll(Arrays.asList(backgroundColors));
    /* 111 */this.foregroundColors.addAll(Arrays.asList(foregroundColors));
    /* 112 */this.fonts.addAll(Arrays.asList(fontDatas));
    /*     */}

  /*     */
  /*     */public int getSize()
  /*     */{
    /* 120 */return this.minValues.size();
    /*     */}

  /*     */
  /*     */public double[] getMinValues()
  /*     */{
    /* 128 */double[] result = new double[this.minValues.size()];
    /* 129 */int counter = 0;
    /*     */Double d;
    /* 130 */for (Iterator localIterator = this.minValues.iterator(); localIterator
      .hasNext(); result[(counter++)] = d.doubleValue())
      d = (Double) localIterator.next();
    /* 131 */return result;
    /*     */}

  /*     */
  /*     */public double[] getMaxValues()
  /*     */{
    /* 139 */double[] result = new double[this.maxValues.size()];
    /* 140 */int counter = 0;
    /*     */Double d;
    /* 141 */for (Iterator localIterator = this.maxValues.iterator(); localIterator
      .hasNext(); result[(counter++)] = d.doubleValue())
      d = (Double) localIterator.next();
    /* 142 */return result;
    /*     */}

  /*     */
  /*     */public ColorDescriptor[] getForegroundColors()
  /*     */{
    /* 150 */return (ColorDescriptor[]) this.foregroundColors
      .toArray(new ColorDescriptor[0]);
    /*     */}

  /*     */
  /*     */public ColorDescriptor[] getBackgroundColors()
  /*     */{
    /* 158 */return (ColorDescriptor[]) this.backgroundColors
      .toArray(new ColorDescriptor[0]);
    /*     */}

  /*     */
  /*     */public FontDescriptor[] getFonts()
  /*     */{
    /* 166 */return (FontDescriptor[]) this.fonts.toArray(new FontDescriptor[0]);
    /*     */}

  /*     */
  /*     */public double getMinValueAt(int index)
  /*     */{
    /* 175 */return ((Double) this.minValues.get(index)).doubleValue();
    /*     */}

  /*     */
  /*     */public double getMaxValueAt(int index)
  /*     */{
    /* 184 */return ((Double) this.maxValues.get(index)).doubleValue();
    /*     */}

  /*     */
  /*     */public ColorDescriptor getForegroundColorAt(int index)
  /*     */{
    /* 193 */ColorDescriptor rgb = (ColorDescriptor) this.foregroundColors
      .get(index);
    /* 194 */if (rgb == null) {
      /* 195 */rgb = new ColorDescriptor(0, 0, 0);
      /*     */}
    /* 197 */return rgb;
    /*     */}

  /*     */
  /*     */public ColorDescriptor getBackgroundColorAt(int index)
  /*     */{
    /* 206 */ColorDescriptor rgb = (ColorDescriptor) this.backgroundColors
      .get(index);
    /* 207 */if (rgb == null) {
      /* 208 */rgb = new ColorDescriptor(128, 128, 128);
      /*     */}
    /* 210 */return rgb;
    /*     */}

  /*     */
  /*     */public FontDescriptor getFontAt(int index)
  /*     */{
    /* 219 */FontDescriptor fd = (FontDescriptor) this.fonts.get(index);
    /* 220 */if (fd == null) {
      /* 221 */fd = new FontDescriptor("Segoe UI,9, , , ");
      /*     */}
    /* 223 */return fd;
    /*     */}

  /*     */
  /*     */public int getIndexForValue(double value) {
    /* 227 */int i = 0;
    for (int n = this.minValues.size(); i < n; ++i) {
      /* 228 */if ((((Double) this.minValues.get(i)).doubleValue() <= value)
        && (((Double) this.maxValues.get(i)).doubleValue() >= value)) {
        /* 229 */return i;
        /*     */}
      /*     */}
    /* 232 */return -1;
    /*     */}
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi
 * .jar Qualified Name: org.palo.viewapi.uimodels.formats.TrafficLightData
 * JD-Core Version: 0.5.4
 */