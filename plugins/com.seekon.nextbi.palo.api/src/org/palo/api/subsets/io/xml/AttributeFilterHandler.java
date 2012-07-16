/*     */package org.palo.api.subsets.io.xml;

/*     */
/*     */import org.palo.api.Dimension; /*     */
import org.palo.api.impl.xml.XMLUtil; /*     */
import org.palo.api.subsets.SubsetFilter; /*     */
import org.palo.api.subsets.filter.AttributeFilter; /*     */
import org.palo.api.subsets.filter.settings.AttributeConstraint; /*     */
import org.palo.api.subsets.filter.settings.AttributeConstraintsMatrix; /*     */
import org.palo.api.subsets.filter.settings.AttributeFilterSetting; /*     */
import org.palo.api.subsets.filter.settings.ObjectParameter;

/*     */
/*     */class AttributeFilterHandler extends AbstractSubsetFilterHandler
/*     */{
  /*     */public static final String XPATH = "/subset/attribute_filter";

  /*     */private static final String FILTER_PARAMETER = "/subset/attribute_filter/attribute_filters/parameter";

  /*     */private static final String FILTER_VALUE_COL_ATTRIBUTE = "/subset/attribute_filter/attribute_filters/value/filter_col/attribute";

  /*     */private static final String FILTER_VALUE_COL_ENTRY = "/subset/attribute_filter/attribute_filters/value/filter_col/col_entry";

  /*     */private final AttributeFilterSetting setting;

  /*     */private String attrId;

  /*     */
  /*     */public AttributeFilterHandler()
  /*     */{
    /* 76 */this.setting = new AttributeFilterSetting();
    /*     */}

  /*     */
  /*     */public final String getXPath() {
    /* 80 */return "/subset/attribute_filter";
    /*     */}

  /*     */
  /*     */public final void enter(String path)
  /*     */{
    /*     */}

  /*     */
  /*     */public final void leave(String path, String value)
  /*     */{
    /* 89 */value = XMLUtil.dequote(value);
    /* 90 */if (path.equals("/subset/attribute_filter/attribute_filters/parameter")) {
      /* 91 */ObjectParameter oldFilters = this.setting.getFilterConstraints();
      /* 92 */ObjectParameter newFilters = new ObjectParameter(value);
      /* 93 */newFilters.setValue(oldFilters.getValue());
      /* 94 */this.setting.setFilterConstraints(newFilters);
      /*     */}
    /* 96 */else if (path
      .equals("/subset/attribute_filter/attribute_filters/value/filter_col/attribute")) {
      /* 97 */this.attrId = value;
      /*     */}
    /* 99 */else if (path
      .equals("/subset/attribute_filter/attribute_filters/value/filter_col/col_entry")) {
      /* 100 */AttributeConstraint constraint = parseEntry(this.attrId, value);
      /*     */
      /* 102 */if ((constraint.getValue().equals("")) &&
      /* 103 */(constraint.getOperator().equals("=")))
        /* 104 */constraint.setOperator("None");
      /* 105 */AttributeConstraintsMatrix constraintMatrix = (AttributeConstraintsMatrix) this.setting
      /* 106 */.getFilterConstraints().getValue();
      /* 107 */constraintMatrix.addFilterConstraint(constraint);
      /*     */}
    /*     */}

  /*     */
  /*     */public final SubsetFilter createFilter(Dimension dimension) {
    /* 112 */return new AttributeFilter(dimension, this.setting);
    /*     */}

  /*     */
  /*     */public static final String getPersistenceString(AttributeFilter filter) {
    /* 116 */StringBuffer str = new StringBuffer();
    /* 117 */AttributeFilterSetting setting = filter.getSettings();
    /* 118 */if (setting.hasFilterConsraints()) {
      /* 119 */str.append("<attribute_filter>\r\n<attribute_filters>\r\n");
      /*     */
      /* 121 */ObjectParameter filterConstraints = setting.getFilterConstraints();
      /* 122 */ParameterHandler.addParameter(filterConstraints, str);
      /* 123 */AttributeConstraintsMatrix filterMatrix =
      /* 124 */(AttributeConstraintsMatrix) filterConstraints.getValue();
      /* 125 */str.append("<value>\r\n");
      /* 126 */String[] attrIds = filterMatrix.getAttributeIDs();
      /* 127 */for (String attrId : attrIds) {
        /* 128 */AttributeConstraint[] columns = filterMatrix.getColumn(attrId);
        /* 129 */writeFilterColumn(str, attrId, columns);
        /*     */}
      /*     */
      /* 138 */str.append("</value>\r\n");
      /* 139 */str.append("</attribute_filters>\r\n</attribute_filter>\r\n");
      /*     */}
    /* 141 */return str.toString();
    /*     */}

  /*     */
  /*     */private static final void writeFilterColumn(StringBuffer buf,
    String attrId, AttributeConstraint[] constraints) {
    /* 145 */buf.append("<filter_col>\r\n");
    /*     */
    /* 147 */buf.append("<attribute>");
    /* 148 */buf.append(attrId);
    /* 149 */buf.append("</attribute>\r\n");
    /* 150 */for (AttributeConstraint entry : constraints)
      /* 151 */writeColumnEntry(buf, entry);
    /* 152 */buf.append("</filter_col>\r\n");
    /*     */}

  /*     */
  /*     */private static final void writeColumnEntry(StringBuffer buf, String entry) {
    /* 156 */buf.append("<col_entry>");
    /* 157 */buf.append(XMLUtil.quote(entry));
    /* 158 */buf.append("</col_entry>\r\n");
    /*     */}

  /*     */
  /*     */private static final void writeColumnEntry(StringBuffer buf,
    AttributeConstraint entry)
  /*     */{
    /* 163 */String operator = entry.getOperator();
    /* 164 */String value = entry.getValue();
    /* 165 */if (value == null)
      value = "";
    /*     */
    /* 167 */if (operator.equals("None"))
      /* 168 */value = "";
    /* 169 */else if (!operator.equals("="))
      /* 170 */value = XMLUtil.printQuoted(operator) + value;
    /* 171 */writeColumnEntry(buf, value);
    /*     */}

  /*     */
  /*     */private final AttributeConstraint parseEntry(String attrId, String entry)
  /*     */{
    /* 181 */AttributeConstraint constraint =
    /* 182 */new AttributeConstraint(attrId);
    /*     */
    /* 184 */parseInternal(entry, constraint);
    /* 185 */return constraint;
    /*     */}

  /*     */
  /*     */private final void parseInternal(String entry,
    AttributeConstraint constraint)
  /*     */{
    /* 190 */entry = entry.trim();
    /* 191 */int length = entry.length();
    /* 192 */int cutIndex = Math.min(length, 2) + 1;
    /* 193 */String op = null;
    /*     */do {
      /* 195 */--cutIndex;
      /* 196 */op = entry.substring(0, cutIndex);
      /* 197 */} while ((cutIndex > 0) && (!
    /* 197 */constraint.isOperator(op)));
    /* 198 */if (cutIndex <= 0)
      /* 199 */op = "=";
    /* 200 */constraint.setOperator(op);
    /* 201 */constraint.setValue(entry.substring(cutIndex).trim());
    /*     */}
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name: org.palo.api.subsets.io.xml.AttributeFilterHandler JD-Core
 * Version: 0.5.4
 */