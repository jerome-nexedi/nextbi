/*     */package org.palo.api.subsets.io.xml;

/*     */
/*     */import org.palo.api.Dimension; /*     */
import org.palo.api.impl.xml.XMLUtil; /*     */
import org.palo.api.subsets.SubsetFilter; /*     */
import org.palo.api.subsets.filter.AliasFilter; /*     */
import org.palo.api.subsets.filter.settings.AliasFilterSetting; /*     */
import org.palo.api.subsets.filter.settings.StringParameter;

/*     */
/*     */class AliasFilterHandler extends AbstractSubsetFilterHandler
/*     */{
  /*     */public static final String XPATH = "/subset/alias_filter";

  /*     */private static final String ALIAS1_PARAMETER = "/subset/alias_filter/alias1/parameter";

  /*     */private static final String ALIAS1_VALUE = "/subset/alias_filter/alias1/value";

  /*     */private static final String ALIAS2_PARAMETER = "/subset/alias_filter/alias2/parameter";

  /*     */private static final String ALIAS2_VALUE = "/subset/alias_filter/alias2/value";

  /*     */private final AliasFilterSetting setting;

  /*     */
  /*     */public AliasFilterHandler()
  /*     */{
    /* 53 */this.setting = new AliasFilterSetting();
    /*     */}

  /*     */
  /*     */public final String getXPath() {
    /* 57 */return "/subset/alias_filter";
    /*     */}

  /*     */
  /*     */public final void enter(String path) {
    /*     */}

  /*     */
  /*     */public final void leave(String path, String value) {
    /* 64 */value = XMLUtil.dequote(value);
    /* 65 */if (path.equals("/subset/alias_filter/alias1/parameter")) {
      /* 66 */StringParameter alias1 = this.setting.getAlias(1);
      /* 67 */StringParameter alias = new StringParameter(value);
      /* 68 */alias.setValue(alias1.getValue());
      /* 69 */this.setting.setAlias(1, alias);
      /*     */}
    /* 71 */else if (path.equals("/subset/alias_filter/alias1/value")) {
      /* 72 */StringParameter alias1 = this.setting.getAlias(1);
      /* 73 */alias1.setValue(value);
      /*     */}
    /* 75 */if (path.equals("/subset/alias_filter/alias2/parameter")) {
      /* 76 */StringParameter alias2 = this.setting.getAlias(2);
      /* 77 */StringParameter alias = new StringParameter(value);
      /* 78 */alias.setValue(alias2.getValue());
      /* 79 */this.setting.setAlias(2, alias);
      /*     */}
    /* 81 */else if (path.equals("/subset/alias_filter/alias2/value")) {
      /* 82 */StringParameter alias2 = this.setting.getAlias(2);
      /* 83 */alias2.setValue(value);
      /*     */}
    /*     */}

  /*     */
  /*     */public final SubsetFilter createFilter(Dimension dimension) {
    /* 88 */return new AliasFilter(dimension, this.setting);
    /*     */}

  /*     */
  /*     */public static final String getPersistenceString(AliasFilter filter) {
    /* 92 */AliasFilterSetting setting = filter.getSettings();
    /* 93 */StringBuffer str = new StringBuffer();
    /* 94 */str.append("<alias_filter>\r\n");
    /*     */
    /* 96 */StringParameter alias = setting.getAlias(1);
    /* 97 */String aliasId = alias.getValue();
    /* 98 */if ((aliasId != null) && (!aliasId.equals("")))
    /*     */{
      /* 100 */str.append("<alias1>\r\n");
      /* 101 */str.append(ParameterHandler.getXML(alias));
      /* 102 */str.append("</alias1>\r\n");
      /*     */}
    /*     */
    /* 105 */alias = setting.getAlias(2);
    /* 106 */aliasId = alias.getValue();
    /* 107 */if ((aliasId != null) && (!aliasId.equals(""))) {
      /* 108 */str.append("<alias2>\r\n");
      /* 109 */str.append(ParameterHandler.getXML(alias));
      /* 110 */str.append("</alias2>\r\n");
      /*     */}
    /* 112 */str.append("</alias_filter>\r\n");
    /* 113 */return str.toString();
    /*     */}
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name: org.palo.api.subsets.io.xml.AliasFilterHandler JD-Core
 * Version: 0.5.4
 */