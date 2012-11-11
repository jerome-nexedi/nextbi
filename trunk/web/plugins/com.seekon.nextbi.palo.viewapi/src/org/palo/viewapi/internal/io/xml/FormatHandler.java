/*     */package org.palo.viewapi.internal.io.xml;

/*     */
/*     */import org.palo.api.PaloAPIException;
import org.palo.api.ext.ui.ColorDescriptor;
import org.palo.api.ext.ui.FontDescriptor;
import org.palo.viewapi.CubeView;
import org.palo.viewapi.internal.util.XMLUtil;
import org.palo.viewapi.uimodels.formats.BorderData;
import org.palo.viewapi.uimodels.formats.Format;
import org.palo.viewapi.uimodels.formats.FormatImpl;
import org.palo.viewapi.uimodels.formats.FormatRangeInfo;
import org.palo.viewapi.uimodels.formats.TrafficLightData;
import org.xml.sax.Attributes;

/*     */
/*     */public class FormatHandler
/*     */implements IXMLHandler
/*     */{
  /*     */public static final String XPATH = "/view/format";

  /*     */private static final String FORMAT_RANGE = "/view/format/range";

  /*     */private static final String FORMAT_TRAFFICLIGHT = "/view/format/trafficlight";

  /*     */private static final String FORMAT_BORDER = "/view/format/border";

  /*     */private Format format;

  /*     */private final CubeView view;

  /*     */
  /*     */public FormatHandler(CubeView view)
  /*     */{
    /* 68 */this.view = view;
    /*     */}

  /*     */
  /*     */public void enter(String path, Attributes attributes) {
    /* 72 */if ((this.format == null) && (path.equals("/view/format")))
    /*     */{
      /* 74 */String id = attributes.getValue("id");
      /* 75 */if ((id == null) || (id.equals(""))) {
        /* 76 */throw new PaloAPIException("FormatHandler: no format id defined!");
        /*     */}
      /*     */
      /* 80 */String backgroundColor = attributes.getValue("backgroundColor");
      /* 81 */String fontColor = attributes.getValue("fontColor");
      /* 82 */String fontData = attributes.getValue("fontData");
      /* 83 */String numberFormat = attributes.getValue("numberFormat");
      /*     */
      /* 85 */this.format =
      /* 88 */new FormatImpl(id,
      /* 86 */parseRGBString(backgroundColor), numberFormat,
      /* 87 */parseFontString(fontData), parseRGBString(fontColor),
      /* 88 */null, null);
      /*     */
      /* 91 */this.view.addFormat(this.format);
      /* 92 */} else if (path.equals("/view/format/trafficlight")) {
      /* 93 */if (this.format == null) {
        /* 94 */throw new PaloAPIException("FormatHandler: no format created!");
        /*     */}
      /*     */
      /* 97 */String min = attributes.getValue("min");
      /* 98 */String max = attributes.getValue("max");
      /* 99 */String backgroundColor = attributes.getValue("backgroundColor");
      /* 100 */String fontColor = attributes.getValue("fontColor");
      /* 101 */String fontData = attributes.getValue("fontData");
      /*     */try {
        /* 103 */this.format.addTrafficLightData(
        /* 104 */Double.parseDouble(min),
        /* 105 */Double.parseDouble(max),
        /* 106 */parseRGBString(backgroundColor),
        /* 107 */parseRGBString(fontColor),
        /* 108 */parseFontString(fontData));
        /*     */} catch (Exception e) {
        /* 110 */throw new PaloAPIException(
          "FormatHandler: invalid traffic light parameters!");
        /*     */}
      /* 112 */} else if (path.equals("/view/format/border")) {
      /* 113 */if (this.format == null) {
        /* 114 */throw new PaloAPIException("FormatHandler: no format created!");
        /*     */}
      /*     */
      /* 117 */String position = attributes.getValue("position");
      /* 118 */String width = attributes.getValue("width");
      /* 119 */String style = attributes.getValue("style");
      /* 120 */String color = attributes.getValue("color");
      /*     */try {
        /* 122 */this.format.addBorderData(
        /* 126 */new BorderData(Integer.parseInt(width),
        /* 124 */Integer.parseInt(style),
        /* 125 */parseRGBString(color),
        /* 126 */Integer.parseInt(position)));
        /*     */} catch (Exception e) {
        /* 128 */throw new PaloAPIException(
          "FormatHandler: invalid border parameters!");
        /*     */}
      /* 130 */} else if (path.equals("/view/format/range")) {
      /* 131 */if (this.format == null) {
        /* 132 */throw new PaloAPIException("FormatHandler: no format created!");
        /*     */}
      /*     */
      /* 135 */String coords = attributes.getValue("coords");
      /* 136 */String levels = attributes.getValue("levels");
      /* 137 */if (levels != null) {
        /* 138 */this.format.addRange(
        /* 139 */new FormatRangeInfo(this.view.getCube().getDimensions(), levels,
          false));
        /*     */}
      /* 141 */if (coords != null)
        /* 142 */this.format.addRange(
        /* 143 */new FormatRangeInfo(this.view.getCube().getDimensions(), coords,
          true));
      /*     */}
    /*     */}

  /*     */
  /*     */public String getXPath()
  /*     */{
    /* 149 */return "/view/format";
    /*     */}

  /*     */
  /*     */public void leave(String path, String value) {
    /* 153 */if (this.format == null)
      /* 154 */throw new PaloAPIException("FormatHandler: no format created!");
    /*     */}

  /*     */
  /*     */private final ColorDescriptor parseRGBString(String color)
  /*     */{
    /* 160 */if ((color == null) || (color.length() == 0)) {
      /* 161 */return null;
      /*     */}
    /* 163 */String[] res = color.split(",");
    /* 164 */if (res.length != 3)
      /* 165 */return null;
    /*     */try
    /*     */{
      /* 168 */return new ColorDescriptor(Integer.parseInt(res[0]),
      /* 169 */Integer.parseInt(res[1]),
      /* 170 */Integer.parseInt(res[2]));
      /*     */} catch (NumberFormatException e) {
      /* 172 */throw new PaloAPIException("FormatHandler: Invalid color specified.");
      /*     */}
    /*     */}

  /*     */
  /*     */private final FontDescriptor parseFontString(String font) {
    /* 177 */if ((font == null) || (font.length() == 0)) {
      /* 178 */return null;
      /*     */}
    /* 180 */FontDescriptor desc = new FontDescriptor(font);
    /* 181 */return desc;
    /*     */}

  /*     */
  /*     */public static final String getPersistenceString(Format format) {
    /* 185 */StringBuffer xml = new StringBuffer();
    /* 186 */xml.append("<format");
    /* 187 */xml.append(" id=\"" + XMLUtil.printQuoted(format.getId()) + "\"\r\n");
    /* 188 */if (format.getBackgroundColor() != null) {
      /* 189 */xml.append(getColorXML("backgroundColor", format
        .getBackgroundColor()));
      /*     */}
    /* 191 */if (format.getForegroundColor() != null) {
      /* 192 */xml.append(getColorXML("fontColor", format.getForegroundColor()));
      /*     */}
    /* 194 */if (format.getFontData() != null) {
      /* 195 */xml.append("  fontData=\""
        + XMLUtil.printQuoted(format.getFontData().toString()) + "\"\r\n");
      /*     */}
    /* 197 */if ((format.getNumberFormat() != null)
      && (format.getNumberFormat().length() != 0)) {
      /* 198 */xml.append("  numberFormat=\""
        + XMLUtil.printQuoted(format.getNumberFormat()) + "\"\r\n");
      /*     */}
    /* 200 */xml.append(">\r\n");
    /* 201 */String borderData = getBorderXML(format);
    /* 202 */if (borderData.length() > 0) {
      /* 203 */xml.append(borderData);
      /*     */}
    /* 205 */TrafficLightData traffic = format.getTrafficLightData();
    /*     */int n;
    /* 206 */if ((traffic != null) && (traffic.getSize() > 0)) {
      /* 207 */int i = 0;
      for (n = traffic.getSize(); i < n; ++i) {
        /* 208 */xml.append(getTrafficXML(traffic, i));
        /*     */}
      /*     */}
    /*     */
    /* 212 */for (FormatRangeInfo range : format.getRanges()) {
      /* 213 */xml.append(getPersistenceString(range));
      /*     */}
    /* 215 */xml.append("</format>\r\n");
    /*     */
    /* 217 */return xml.toString();
    /*     */}

  /*     */
  /*     */private static final String getPersistenceString(FormatRangeInfo range) {
    /* 221 */StringBuffer xml = new StringBuffer();
    /*     */
    /* 223 */xml.append("    <range\r\n");
    /* 224 */if (range.getCells() != null)
      /* 225 */xml.append("      coords=\"" + XMLUtil.printQuoted(range.toString())
        + "\"\r\n");
    /* 226 */else if (range.getDimensions().length != 0) {
      /* 227 */xml.append("      levels=\"" + XMLUtil.printQuoted(range.toString())
        + "\"\r\n");
      /*     */}
    /* 229 */xml.append("    />\r\n");
    /*     */
    /* 231 */return xml.toString();
    /*     */}

  /*     */
  /*     */private static final String getColorXML(String tagName,
    ColorDescriptor color)
  /*     */{
    /*     */String colorString;
    /* 236 */if (color == null)
      /* 237 */colorString = "0,0,0";
    /*     */else {
      /* 239 */colorString = color.getRed() + "," +
      /* 240 */color.getGreen() + "," +
      /* 241 */color.getBlue();
      /*     */}
    /* 243 */return "  " + tagName + "=\"" + XMLUtil.printQuoted(colorString)
      + "\"\r\n";
    /*     */}

  /*     */
  /*     */private static final String getBorderXML(Format desc) {
    /* 247 */StringBuffer borderXML = new StringBuffer();
    /* 248 */BorderData[] borders = desc.getBorderData();
    /* 249 */if ((borders == null) || (borders.length == 0)) {
      /* 250 */return "";
      /*     */}
    /* 252 */int i = 0;
    for (int n = borders.length; i < n; ++i) {
      /* 253 */BorderData data = borders[i];
      /* 254 */borderXML.append("  <border position=\"" +
      /* 255 */XMLUtil.printQuoted(data.getLinePosition()) + "\"\r\n");
      /* 256 */borderXML.append("         width=\"" +
      /* 257 */XMLUtil.printQuoted(data.getLineWidth()) + "\"\r\n");
      /* 258 */borderXML.append("         style=\"" +
      /* 259 */XMLUtil.printQuoted(data.getLineStyle()) + "\"\r\n");
      /* 260 */borderXML.append(getColorXML("       color", data.getLineColor()));
      /* 261 */borderXML.append("  />\r\n");
      /*     */}
    /* 263 */return borderXML.toString();
    /*     */}

  /*     */
  /*     */private static final String getTrafficXML(TrafficLightData data, int i) {
    /* 267 */StringBuffer trafficXML = new StringBuffer("  <trafficlight ");
    /* 268 */trafficXML.append("min=\""
      + XMLUtil.printQuoted(new StringBuilder().append(data.getMinValueAt(i))
        .toString()) + "\"\r\n");
    /* 269 */trafficXML.append("                max=\""
      + XMLUtil.printQuoted(new StringBuilder().append(data.getMaxValueAt(i))
        .toString()) + "\"\r\n");
    /* 270 */trafficXML.append(getColorXML("              backgroundColor", data
      .getBackgroundColorAt(i)));
    /* 271 */trafficXML.append(getColorXML("              fontColor", data
      .getForegroundColorAt(i)));
    /* 272 */trafficXML.append("                fontData=\"");
    /* 273 */trafficXML.append(XMLUtil.printQuoted(data.getFontAt(i).toString()));
    /* 274 */trafficXML.append("\"\r\n");
    /* 275 */trafficXML.append("  />\r\n");
    /* 276 */return trafficXML.toString();
    /*     */}
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi
 * .jar Qualified Name: org.palo.viewapi.internal.io.xml.FormatHandler JD-Core
 * Version: 0.5.4
 */