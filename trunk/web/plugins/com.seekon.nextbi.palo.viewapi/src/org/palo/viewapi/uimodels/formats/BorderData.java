/*     */package org.palo.viewapi.uimodels.formats;

/*     */
/*     */import org.palo.api.ext.ui.ColorDescriptor;

/*     */
/*     */public class BorderData
/*     */{
  /*     */public static final int VERTICAL_LEFT = 0;

  /*     */public static final int VERTICAL_MIDDLE = 1;

  /*     */public static final int VERTICAL_RIGHT = 2;

  /*     */public static final int HORIZONTAL_TOP = 3;

  /*     */public static final int HORIZONTAL_MIDDLE = 4;

  /*     */public static final int HORIZONTAL_BOTTOM = 5;

  /*     */public static final int LINE_SOLID = 1;

  /*     */public static final int LINE_DASH = 2;

  /*     */public static final int LINE_DOT = 3;

  /*     */public static final int LINE_DASHDOT = 4;

  /*     */public static final int LINE_DASHDOTDOT = 5;

  /*     */private final int lineWidth;

  /*     */private final int lineStyle;

  /*     */private final ColorDescriptor lineColor;

  /*     */private final int linePosition;

  /*     */
  /*     */public BorderData(int width, int style, ColorDescriptor color,
    int position)
  /*     */{
    /* 134 */this.lineWidth = width;
    /* 135 */this.lineStyle = style;
    /* 136 */this.lineColor = color;
    /* 137 */this.linePosition = position;
    /*     */}

  /*     */
  /*     */public int getLineWidth()
  /*     */{
    /* 145 */return this.lineWidth;
    /*     */}

  /*     */
  /*     */public int getLineStyle()
  /*     */{
    /* 153 */return this.lineStyle;
    /*     */}

  /*     */
  /*     */public ColorDescriptor getLineColor()
  /*     */{
    /* 161 */return this.lineColor;
    /*     */}

  /*     */
  /*     */public int getLinePosition()
  /*     */{
    /* 170 */return this.linePosition;
    /*     */}

  /*     */
  /*     */public String toString()
  /*     */{
    /* 177 */StringBuffer buf = new StringBuffer();
    /* 178 */switch (this.lineStyle)
    /*     */{
    /*     */case 1:
      /* 179 */
      buf.append("Solid (");
      break;
    /*     */case 3:
      /* 180 */
      buf.append("Dotted (");
      break;
    /*     */case 2:
      /* 181 */
      buf.append("Dashed (");
      break;
    /*     */case 4:
      /* 182 */
      buf.append("Dash-dot (");
      break;
    /*     */case 5:
      /* 183 */
      buf.append("Dash-dot-dot (");
      break;
    /*     */default:
      /* 184 */
      buf.append("No line (");
      /*     */
    }
    /* 186 */buf.append(this.lineWidth + ") [");
    /* 187 */buf.append(this.lineColor + "] @ ");
    /* 188 */switch (this.linePosition)
    /*     */{
    /*     */case 3:
      /* 189 */
      buf.append("top");
      break;
    /*     */case 4:
      /* 190 */
      buf.append("h-middle");
      break;
    /*     */case 5:
      /* 191 */
      buf.append("bottom");
      break;
    /*     */case 0:
      /* 192 */
      buf.append("left");
      break;
    /*     */case 1:
      /* 193 */
      buf.append("v-middle");
      break;
    /*     */case 2:
      /* 194 */
      buf.append("right");
      /*     */
    }
    /* 196 */return buf.toString();
    /*     */}
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi
 * .jar Qualified Name: org.palo.viewapi.uimodels.formats.BorderData JD-Core
 * Version: 0.5.4
 */