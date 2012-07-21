/*     */package org.palo.api.subsets.io.xml;

/*     */
/*     */import org.palo.api.Dimension; /*     */
import org.palo.api.Element; /*     */
import org.palo.api.subsets.SubsetFilter; /*     */
import org.palo.api.subsets.filter.SortingFilter; /*     */
import org.palo.api.subsets.filter.settings.IntegerParameter; /*     */
import org.palo.api.subsets.filter.settings.SortingFilterSetting; /*     */
import org.palo.api.subsets.filter.settings.StringParameter;

/*     */
/*     */class SortingFilterHandler extends AbstractSubsetFilterHandler
/*     */{
  /*     */public static final String XPATH = "/subset/sorting_filter";

  /*     */private static final String SORTING_CRITERIA_VALUE = "/subset/sorting_filter/sorting_criteria/value";

  /*     */private static final String SORTING_CRITERIA_PARAMETER = "/subset/sorting_filter/sorting_criteria/parameter";

  /*     */private static final String REVERSE_VALUE = "/subset/sorting_filter/reverse/value";

  /*     */private static final String REVERSE_PARAMETER = "/subset/sorting_filter/reverse/parameter";

  /*     */private static final String TYPE_LIMITATION_VALUE = "/subset/sorting_filter/type_limitation/value";

  /*     */private static final String TYPE_LIMITATION_PARAMETER = "/subset/sorting_filter/type_limitation/parameter";

  /*     */private static final String WHOLE_VALUE = "/subset/sorting_filter/whole/value";

  /*     */private static final String WHOLE_PARAMETER = "/subset/sorting_filter/whole/parameter";

  /*     */private static final String ATTRIBUTE_VALUE = "/subset/sorting_filter/attribute/value";

  /*     */private static final String ATTRIBUTE_PARAMETER = "/subset/sorting_filter/attribute/parameter";

  /*     */private static final String LEVEL_ELEMENT_1_0RC2_VALUE = "/subset/sorting_filter/level_element/value";

  /*     */private static final String LEVEL_ELEMENT_1_0RC2_PARAMETER = "/subset/sorting_filter/level_element/parameter";

  /*     */private static final String LEVEL_ELEMENT_VALUE = "/subset/sorting_filter/level/value";

  /*     */private static final String LEVEL_ELEMENT_PARAMETER = "/subset/sorting_filter/level/parameter";

  /*     */private static final String SHOW_DUPLICATES_VALUE = "/subset/sorting_filter/show_duplicates/value";

  /*     */private static final String SHOW_DUPLICATES_PARAMETER = "/subset/sorting_filter/show_duplicates/parameter";

  /*     */private final SortingFilterSetting sfInfo;

  /*     */
  /*     */public SortingFilterHandler()
  /*     */{
    /* 81 */this.sfInfo = new SortingFilterSetting();
    /*     */}

  /*     */
  /*     */public final String getXPath()
  /*     */{
    /* 86 */return "/subset/sorting_filter";
    /*     */}

  /*     */public final void enter(String path) {
    /*     */}

  /*     */
  /*     */public final void leave(String path, String value) {
    /* 92 */if (path.equals("/subset/sorting_filter/sorting_criteria/value")) {
      /* 93 */this.sfInfo.setSortCriteria(SubsetXMLHandler.getInteger(value));
      /* 94 */} else if (path
      .equals("/subset/sorting_filter/sorting_criteria/parameter")) {
      /* 95 */IntegerParameter oldParam = this.sfInfo.getSortCriteria();
      /* 96 */this.sfInfo.setSortCriteria(new IntegerParameter(value));
      /* 97 */this.sfInfo.setSortCriteria(oldParam.getValue().intValue());
      /*     */}
    /* 99 */else if (path.equals("/subset/sorting_filter/reverse/value")) {
      /* 100 */this.sfInfo.setOrderMode(SubsetXMLHandler.getInteger(value));
      /* 101 */} else if (path.equals("/subset/sorting_filter/reverse/parameter")) {
      /* 102 */IntegerParameter oldParam = this.sfInfo.getOrderMode();
      /* 103 */this.sfInfo.setOrderMode(new IntegerParameter(value));
      /* 104 */this.sfInfo.setOrderMode(oldParam.getValue().intValue());
      /*     */}
    /* 106 */else if (path.equals("/subset/sorting_filter/type_limitation/value")) {
      /* 107 */this.sfInfo.setSortTypeMode(SubsetXMLHandler.getInteger(value));
      /* 108 */} else if (path
      .equals("/subset/sorting_filter/type_limitation/parameter")) {
      /* 109 */IntegerParameter oldParam = this.sfInfo.getSortTypeMode();
      /* 110 */this.sfInfo.setSortTypeMode(new IntegerParameter(value));
      /* 111 */this.sfInfo.setSortTypeMode(oldParam.getValue().intValue());
      /*     */}
    /* 113 */else if (path.equals("/subset/sorting_filter/whole/value")) {
      /* 114 */this.sfInfo.setHierarchicalMode(SubsetXMLHandler.getInteger(value));
      /* 115 */} else if (path.equals("/subset/sorting_filter/whole/parameter")) {
      /* 116 */IntegerParameter oldParam = this.sfInfo.getHierarchicalMode();
      /* 117 */this.sfInfo.setHierarchicalMode(new IntegerParameter(value));
      /* 118 */this.sfInfo.setHierarchicalMode(oldParam.getValue().intValue());
      /*     */}
    /* 120 */else if (path.equals("/subset/sorting_filter/attribute/value")) {
      /* 121 */this.sfInfo.setSortAttribute(value);
      /* 122 */} else if (path.equals("/subset/sorting_filter/attribute/parameter")) {
      /* 123 */StringParameter oldParam = this.sfInfo.getSortAttribute();
      /* 124 */this.sfInfo.setSortAttribute(new StringParameter(value));
      /* 125 */this.sfInfo.setSortAttribute(oldParam.getValue());
      /*     */}
    /* 127 */else if (path.equals("/subset/sorting_filter/level_element/value")) {
      /* 128 */this.sfInfo.setSortLevelElement(value);
      /* 129 */} else if (path
      .equals("/subset/sorting_filter/level_element/parameter")) {
      /* 130 */StringParameter oldParm = this.sfInfo.getSortLevelElement();
      /* 131 */this.sfInfo.setSortLevelElement(new StringParameter(value));
      /* 132 */this.sfInfo.setSortLevelElement(oldParm.getValue());
      /*     */}
    /* 134 */else if (path.equals("/subset/sorting_filter/level/value")) {
      /* 135 */this.sfInfo.setSortLevel(SubsetXMLHandler.getInteger(value));
      /* 136 */} else if (path.equals("/subset/sorting_filter/level/parameter")) {
      /* 137 */IntegerParameter oldParm = this.sfInfo.getSortLevel();
      /* 138 */this.sfInfo.setSortLevel(new IntegerParameter(value));
      /* 139 */this.sfInfo.setSortLevel(oldParm.getValue().intValue());
      /*     */}
    /* 141 */else if (path.equals("/subset/sorting_filter/show_duplicates/value")) {
      /* 142 */this.sfInfo.setShowDuplicates(SubsetXMLHandler.getInteger(value));
      /* 143 */} else if (path
      .equals("/subset/sorting_filter/show_duplicates/parameter")) {
      /* 144 */IntegerParameter oldParam = this.sfInfo.getShowDuplicates();
      /* 145 */this.sfInfo.setShowDuplicates(new IntegerParameter(value));
      /* 146 */this.sfInfo.setShowDuplicates(oldParam.getValue().intValue());
      /*     */}
    /*     */}

  /*     */
  /*     */public final SubsetFilter createFilter(Dimension dimension)
  /*     */{
    /* 152 */if ((this.subsetVersion.equals("1.0rc2")) &&
    /* 153 */(this.sfInfo.doSortPerLevel()))
    /*     */{
      /* 155 */Element sortElement = dimension.getElementById(
      /* 156 */this.sfInfo.getSortLevelElement().getValue());
      /* 157 */if (sortElement != null) {
        /* 158 */this.sfInfo.setSortLevel(sortElement.getLevel());
        /* 159 */this.sfInfo.setSortLevelElement("");
        /*     */}
      /*     */}
    /*     */
    /* 163 */return new SortingFilter(dimension, this.sfInfo);
    /*     */}

  /*     */
  /*     */public static final String getPersistenceString(SortingFilter filter) {
    /* 167 */SortingFilterSetting sfInfo = filter.getSettings();
    /* 168 */StringBuffer xmlStr = new StringBuffer();
    /* 169 */xmlStr.append("<sorting_filter>\r\n");
    /*     */
    /* 171 */if (sfInfo.doHierarchy()) {
      /* 172 */xmlStr.append("<whole>\r\n");
      /* 173 */xmlStr.append(
      /* 174 */ParameterHandler.getXML(sfInfo.getHierarchicalMode()));
      /* 175 */xmlStr.append("</whole>\r\n");
      /*     */}
    /*     */
    /* 178 */xmlStr.append("<sorting_criteria>\r\n");
    /* 179 */xmlStr.append(ParameterHandler.getXML(sfInfo.getSortCriteria()));
    /* 180 */xmlStr.append("</sorting_criteria>\r\n");
    /*     */
    /* 182 */if (sfInfo.doSortByAttribute()) {
      /* 183 */xmlStr.append("<attribute>\r\n");
      /* 184 */xmlStr.append(ParameterHandler.getXML(sfInfo.getSortAttribute()));
      /* 185 */xmlStr.append("</attribute>\r\n");
      /*     */}
    /*     */
    /* 188 */if (sfInfo.doSortByType()) {
      /* 189 */xmlStr.append("<type_limitation>\r\n");
      /* 190 */xmlStr.append(ParameterHandler.getXML(sfInfo.getSortTypeMode()));
      /* 191 */xmlStr.append("</type_limitation>\r\n");
      /*     */}
    /*     */
    /* 194 */if (sfInfo.doSortPerLevel()) {
      /* 195 */xmlStr.append("<level>\r\n");
      /* 196 */xmlStr.append(ParameterHandler.getXML(sfInfo.getSortLevel()));
      /* 197 */xmlStr.append("</level>\r\n");
      /*     */}
    /*     */
    /* 200 */if (sfInfo.doReverseOrder()) {
      /* 201 */xmlStr.append("<reverse>\r\n");
      /* 202 */xmlStr.append(
      /* 203 */ParameterHandler.getXML(sfInfo.getOrderMode()));
      /* 204 */xmlStr.append("</reverse>\r\n");
      /*     */}
    /*     */
    /* 207 */xmlStr.append("<show_duplicates>\r\n");
    /* 208 */xmlStr.append(ParameterHandler.getXML(sfInfo.getShowDuplicates()));
    /* 209 */xmlStr.append("</show_duplicates>\r\n");
    /*     */
    /* 211 */xmlStr.append("</sorting_filter>\r\n");
    /* 212 */return xmlStr.toString();
    /*     */}
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name: org.palo.api.subsets.io.xml.SortingFilterHandler JD-Core
 * Version: 0.5.4
 */