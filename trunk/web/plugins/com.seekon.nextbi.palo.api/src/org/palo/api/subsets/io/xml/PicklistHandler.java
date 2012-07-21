/*     */package org.palo.api.subsets.io.xml;

/*     */
/*     */import java.util.HashSet; /*     */
import org.palo.api.Dimension; /*     */
import org.palo.api.subsets.SubsetFilter; /*     */
import org.palo.api.subsets.filter.PicklistFilter; /*     */
import org.palo.api.subsets.filter.settings.IntegerParameter; /*     */
import org.palo.api.subsets.filter.settings.ObjectParameter; /*     */
import org.palo.api.subsets.filter.settings.PicklistFilterSetting;

/*     */
/*     */class PicklistHandler extends AbstractSubsetFilterHandler
/*     */{
  /*     */public static final String XPATH = "/subset/picklist_filter";

  /*     */private static final String PICK_TYPE_VALUE = "/subset/picklist_filter/pick_type/value";

  /*     */private static final String PICK_TYPE_PARAMETER = "/subset/picklist_filter/pick_type/parameter";

  /*     */private static final String PICK_ELEM_VALUE = "/subset/picklist_filter/manual_definition/value/pick_elem";

  /*     */private static final String PICK_ELEM_PARAMETER = "/subset/picklist_filter/manual_definition/parameter";

  /*     */private final PicklistFilterSetting plInfo;

  /*     */
  /*     */public PicklistHandler()
  /*     */{
    /* 73 */this.plInfo = new PicklistFilterSetting();
    /*     */}

  /*     */
  /*     */public final String getXPath() {
    /* 77 */return "/subset/picklist_filter";
    /*     */}

  /*     */public final void enter(String path) {
    /*     */}

  /*     */
  /*     */public final void leave(String path, String value) {
    /* 83 */if (path.equals("/subset/picklist_filter/pick_type/value")) {
      /* 84 */this.plInfo.setInsertMode(SubsetXMLHandler.getInteger(value));
      /* 85 */} else if (path.equals("/subset/picklist_filter/pick_type/parameter")) {
      /* 86 */IntegerParameter oldParam = this.plInfo.getInsertMode();
      /* 87 */this.plInfo.setInsertMode(new IntegerParameter(value));
      /* 88 */this.plInfo.setInsertMode(oldParam.getValue().intValue());
      /* 89 */} else if (path
      .equals("/subset/picklist_filter/manual_definition/value/pick_elem")) {
      /* 90 */this.plInfo.addElement(value);
      /* 91 */} else if (path
      .equals("/subset/picklist_filter/manual_definition/parameter")) {
      /* 92 */ObjectParameter oldSelection = this.plInfo.getSelection();
      /* 93 */ObjectParameter newSelection = new ObjectParameter(value);
      /* 94 */newSelection.setValue(oldSelection.getValue());
      /* 95 */this.plInfo.setSelection(newSelection);
      /*     */}
    /*     */}

  /*     */
  /*     */public final SubsetFilter createFilter(Dimension dimension) {
    /* 100 */return new PicklistFilter(dimension, this.plInfo);
    /*     */}

  /*     */
  /*     */public static final String getPersistenceString(PicklistFilter filter) {
    /* 104 */PicklistFilterSetting plInfo = filter.getSettings();
    /* 105 */StringBuffer xmlStr = new StringBuffer();
    /* 106 */xmlStr.append("<picklist_filter>\r\n<manual_definition>\r\n");
    /* 107 */ObjectParameter selection = plInfo.getSelection();
    /* 108 */ParameterHandler.addParameter(selection, xmlStr);
    /* 109 */HashSet<String> pickedElements = (HashSet) selection.getValue();
    /* 110 */xmlStr.append("<value>\r\n");
    /* 111 */for (String picked : pickedElements) {
      /* 112 */xmlStr.append("<pick_elem>");
      /* 113 */xmlStr.append(picked);
      /* 114 */xmlStr.append("</pick_elem>\r\n");
      /*     */}
    /* 116 */xmlStr.append("</value>\r\n");
    /* 117 */xmlStr.append("</manual_definition>\r\n");
    /*     */
    /* 120 */xmlStr.append("<pick_type>\r\n");
    /* 121 */xmlStr.append(ParameterHandler.getXML(
    /* 122 */plInfo.getInsertMode()));
    /* 123 */xmlStr.append("</pick_type>\r\n");
    /*     */
    /* 125 */xmlStr.append("</picklist_filter>\r\n");
    /* 126 */return xmlStr.toString();
    /*     */}
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name: org.palo.api.subsets.io.xml.PicklistHandler JD-Core Version:
 * 0.5.4
 */