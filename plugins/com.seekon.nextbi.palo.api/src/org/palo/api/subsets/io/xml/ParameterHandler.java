/*     */package org.palo.api.subsets.io.xml;

/*     */
/*     */import org.palo.api.impl.xml.XMLUtil; /*     */
import org.palo.api.subsets.filter.settings.Parameter;

/*     */
/*     */class ParameterHandler
/*     */{
  /*     */static final String getXML(Parameter param)
  /*     */{
    /* 60 */StringBuffer buff = new StringBuffer();
    /* 61 */addParameter(param, buff);
    /* 62 */buff.append("<value>");
    /* 63 */buff.append(param.getValue().toString());
    /* 64 */buff.append("</value>\r\n");
    /* 65 */return buff.toString();
    /*     */}

  /*     */
  /*     */static final String getXML(Parameter param, boolean quoteValue)
  /*     */{
    /* 77 */StringBuffer buff = new StringBuffer();
    /* 78 */addParameter(param, buff);
    /* 79 */buff.append("<value>");
    /* 80 */Object value = param.getValue();
    /* 81 */String strValue = (value != null) ? value.toString() : "";
    /* 82 */if (quoteValue)
      /* 83 */buff.append(XMLUtil.printQuoted(strValue));
    /*     */else
      /* 85 */buff.append(strValue);
    /* 86 */buff.append("</value>\r\n");
    /* 87 */return buff.toString();
    /*     */}

  /*     */
  /*     */static final void addParameter(Parameter param, StringBuffer buff)
  /*     */{
    /* 110 */if (param.getName() != null) {
      /* 111 */buff.append("<parameter>");
      /* 112 */buff.append(param.getName());
      /* 113 */buff.append("</parameter>\r\n");
      /*     */}
    /*     */}
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name: org.palo.api.subsets.io.xml.ParameterHandler JD-Core Version:
 * 0.5.4
 */