/*     */ package org.palo.api.subsets.io.xml;
/*     */ 
/*     */ import org.palo.api.Dimension;
/*     */ import org.palo.api.impl.xml.XMLUtil;
/*     */ import org.palo.api.subsets.SubsetFilter;
/*     */ import org.palo.api.subsets.filter.DataFilter;
/*     */ import org.palo.api.subsets.filter.settings.BooleanParameter;
/*     */ import org.palo.api.subsets.filter.settings.DataCriteria;
/*     */ import org.palo.api.subsets.filter.settings.DataFilterSetting;
/*     */ import org.palo.api.subsets.filter.settings.DoubleParameter;
/*     */ import org.palo.api.subsets.filter.settings.IntegerParameter;
/*     */ import org.palo.api.subsets.filter.settings.ObjectParameter;
/*     */ import org.palo.api.subsets.filter.settings.StringParameter;
/*     */ 
/*     */ class DataFilterHandler extends AbstractSubsetFilterHandler
/*     */ {
/*     */   public static final String XPATH = "/subset/data_filter";
/*     */   private static final String CELL_OPERATOR_VALUE = "/subset/data_filter/cell_operator/value";
/*     */   private static final String CELL_OPERATOR_PARAMETER = "/subset/data_filter/cell_operator/parameter";
/*     */   private static final String CRITERIA_PARAM_1_PARAMETER = "/subset/data_filter/criteria/par1/parameter";
/*     */   private static final String CRITERIA_PARAM_1_VALUE = "/subset/data_filter/criteria/par1/value";
/*     */   private static final String CRITERIA_OPERATOR_1 = "/subset/data_filter/criteria/op1";
/*     */   private static final String CRITERIA_PARAM_2_PARAMETER = "/subset/data_filter/criteria/par2/parameter";
/*     */   private static final String CRITERIA_PARAM_2_VALUE = "/subset/data_filter/criteria/par2/value";
/*     */   private static final String CRITERIA_OPERATOR_2 = "/subset/data_filter/criteria/op2";
/*     */   private static final String SRC_CUBE_VALUE = "/subset/data_filter/subcube/source_cube/value";
/*     */   private static final String SRC_CUBE_PARAMETER = "/subset/data_filter/subcube/source_cube/parameter";
/*     */   private static final String SUB_CUBE_NEW_SLICE_DIMENSION = "/subset/data_filter/subcube/dimension_coordinates";
/*     */   private static final String SUB_CUBE_DIMENSION_PARAMETER = "/subset/data_filter/subcube/dimension_coordinates/parameter";
/*     */   private static final String SUB_CUBE_DIMENSION_ELEMENT = "/subset/data_filter/subcube/dimension_coordinates/value/element";
/*     */   private static final String TOP_VALUE = "/subset/data_filter/top/value";
/*     */   private static final String TOP_PARAMETER = "/subset/data_filter/top/parameter";
/*     */   private static final String UPPER_PERC_VALUE = "/subset/data_filter/upper_percentage/value";
/*     */   private static final String UPPER_PERC_PARAMETER = "/subset/data_filter/upper_percentage/parameter";
/*     */   private static final String LOWER_PERC_VALUE = "/subset/data_filter/lower_percentage/value";
/*     */   private static final String LOWER_PERC_PARAMETER = "/subset/data_filter/lower_percentage/parameter";
/*     */   private static final String NO_RULES_VALUE = "/subset/data_filter/no_rules/value";
/*     */   private static final String NO_RULES_PARAMETER = "/subset/data_filter/no_rules/parameter";
/*     */   private final DataFilterSetting setting;
/*     */ 
/*     */   public DataFilterHandler()
/*     */   {
/*  91 */     this(null);
/*     */   }
/*     */ 
/*     */   public DataFilterHandler(String sourceCube) {
/*  95 */     this.setting = new DataFilterSetting(sourceCube);
/*  96 */     this.setting.setUseRules(true);
/*     */   }
/*     */ 
/*     */   public final String getXPath() {
/* 100 */     return "/subset/data_filter";
/*     */   }
/*     */ 
/*     */   public final void enter(String path) {
/* 104 */     if (path.equals("/subset/data_filter/subcube/dimension_coordinates"))
/* 105 */       this.setting.addSlicePart(new ObjectParameter());
/*     */   }
/*     */ 
/*     */   public final void leave(String path, String value)
/*     */   {
/* 110 */     if (path.equals("/subset/data_filter/cell_operator/value")) {
/* 111 */       this.setting.setCellOperator(SubsetXMLHandler.getInteger(value));
/* 112 */     } else if (path.equals("/subset/data_filter/cell_operator/parameter")) {
/* 113 */       IntegerParameter oldParam = this.setting.getCellOperator();
/* 114 */       this.setting.setCellOperator(new IntegerParameter(value));
/* 115 */       this.setting.setCellOperator(oldParam.getValue().intValue());
/*     */     }
/* 122 */     else if (path.equals("/subset/data_filter/criteria/par1/parameter")) {
/* 123 */       DataCriteria criteria = this.setting.getCriteria();
/* 124 */       StringParameter par1 = criteria.getFirstOperand();
/* 125 */       StringParameter newPar1 = new StringParameter(XMLUtil.dequote(value));
/* 126 */       newPar1.setValue(par1.getValue());
/* 127 */       criteria.setFirstOperand(newPar1);
/*     */     }
/* 129 */     else if (path.equals("/subset/data_filter/criteria/par1/value")) {
/* 130 */       DataCriteria criteria = this.setting.getCriteria();
/* 131 */       StringParameter par1 = criteria.getFirstOperand();
/* 132 */       par1.setValue(value);
/*     */     }
/* 134 */     else if (path.equals("/subset/data_filter/criteria/op1")) {
/* 135 */       DataCriteria criteria = this.setting.getCriteria();
/* 136 */       criteria.setFirstOperator(value);
/*     */     }
/* 138 */     else if (path.equals("/subset/data_filter/criteria/par2/parameter")) {
/* 139 */       DataCriteria criteria = this.setting.getCriteria();
/* 140 */       StringParameter par2 = criteria.getSecondOperand();
/* 141 */       StringParameter newPar2 = new StringParameter(XMLUtil.dequote(value));
/* 142 */       newPar2.setValue(par2.getValue());
/* 143 */       criteria.setSecondOperand(newPar2);
/*     */     }
/* 145 */     else if (path.equals("/subset/data_filter/criteria/par2/value")) {
/* 146 */       DataCriteria criteria = this.setting.getCriteria();
/* 147 */       StringParameter par2 = criteria.getSecondOperand();
/* 148 */       par2.setValue(value);
/*     */     }
/* 150 */     else if (path.equals("/subset/data_filter/criteria/op2")) {
/* 151 */       DataCriteria criteria = this.setting.getCriteria();
/* 152 */       criteria.setSecondOperator(value);
/*     */     }
/* 154 */     else if (path.equals("/subset/data_filter/subcube/source_cube/value")) {
/* 155 */       this.setting.setSourceCube(value);
/* 156 */     } else if (path.equals("/subset/data_filter/subcube/source_cube/parameter")) {
/* 157 */       StringParameter oldParam = this.setting.getSourceCube();
/* 158 */       this.setting.setSourceCube(new StringParameter(value));
/* 159 */       this.setting.setSourceCube(oldParam.getValue());
/*     */     }
/* 161 */     else if (path.equals("/subset/data_filter/subcube/dimension_coordinates/parameter")) {
/* 162 */       ObjectParameter sliceDimension = this.setting.getSlicePart();
/* 163 */       sliceDimension.setName(value);
/*     */     }
/* 165 */     else if (path.equals("/subset/data_filter/subcube/dimension_coordinates/value/element")) {
/* 166 */       this.setting.addSliceElement(XMLUtil.dequote(value));
/* 167 */     } else if (path.equals("/subset/data_filter/top/value")) {
/* 168 */       this.setting.setTop(SubsetXMLHandler.getInteger(value));
/* 169 */     } else if (path.equals("/subset/data_filter/top/parameter")) {
/* 170 */       IntegerParameter oldParam = this.setting.getTop();
/* 171 */       this.setting.setTop(new IntegerParameter(value));
/* 172 */       this.setting.setTop(oldParam.getValue().intValue());
/*     */     }
/* 174 */     else if (path.equals("/subset/data_filter/upper_percentage/value")) {
/* 175 */       this.setting.setUpperPercentage(SubsetXMLHandler.getDouble(value));
/* 176 */     } else if (path.equals("/subset/data_filter/upper_percentage/parameter")) {
/* 177 */       DoubleParameter oldParam = this.setting.getUpperPercentage();
/* 178 */       this.setting.setUpperPercentage(new DoubleParameter(value));
/* 179 */       this.setting.setUpperPercentage(oldParam.getValue().doubleValue());
/*     */     }
/* 181 */     else if (path.equals("/subset/data_filter/lower_percentage/value")) {
/* 182 */       this.setting.setLowerPercentage(SubsetXMLHandler.getDouble(value));
/* 183 */     } else if (path.equals("/subset/data_filter/lower_percentage/parameter")) {
/* 184 */       DoubleParameter oldParam = this.setting.getLowerPercentage();
/* 185 */       this.setting.setLowerPercentage(new DoubleParameter(value));
/* 186 */       this.setting.setLowerPercentage(oldParam.getValue().doubleValue());
/*     */     }
/* 188 */     else if (path.equals("/subset/data_filter/no_rules/value")) {
/* 189 */       this.setting.setUseRules(SubsetXMLHandler.getBoolean(value));
/* 190 */     } else if (path.equals("/subset/data_filter/no_rules/parameter")) {
/* 191 */       BooleanParameter oldParam = this.setting.getUseRules();
/* 192 */       this.setting.setUseRules(new BooleanParameter(value));
/* 193 */       this.setting.setUseRules(oldParam.getValue().booleanValue());
/*     */     }
/*     */   }
/*     */ 
/*     */   public final SubsetFilter createFilter(Dimension dimension) {
/* 198 */     return new DataFilter(dimension, this.setting);
/*     */   }
/*     */ 
/*     */   public static final String getPersistenceString(DataFilter filter) {
/* 202 */     DataFilterSetting setting = 
/* 203 */       filter.getSettings();
/* 204 */     StringBuffer xmlStr = new StringBuffer();
/* 205 */     xmlStr.append("<data_filter>\r\n");
/*     */ 
/* 207 */     xmlStr.append("<subcube>\r\n");
/* 208 */     xmlStr.append("<source_cube>\r\n");
/* 209 */     xmlStr.append(ParameterHandler.getXML(setting.getSourceCube()));
/* 210 */     xmlStr.append("</source_cube>\r\n");
/*     */ 
/* 212 */     ObjectParameter[] slice = setting.getSliceParameters();
/* 213 */     if (slice.length > 0) {
/* 214 */       for (int i = 0; i < slice.length; ++i) {
/* 215 */         xmlStr.append("<dimension_coordinates>\r\n");
/*     */ 
/* 217 */         ParameterHandler.addParameter(slice[i], xmlStr);
/*     */ 
/* 219 */         String[] elIDs = (String[])slice[i].getValue();
/* 220 */         xmlStr.append("<value>\r\n");
/* 221 */         for (String id : elIDs) {
/* 222 */           xmlStr.append("<element>");
/* 223 */           xmlStr.append(id);
/* 224 */           xmlStr.append("</element>\r\n");
/*     */         }
/* 226 */         xmlStr.append("</value>\r\n");
/* 227 */         xmlStr.append("</dimension_coordinates>\r\n");
/*     */       }
/*     */     }
/* 230 */     xmlStr.append("</subcube>\r\n");
/*     */ 
/* 233 */     xmlStr.append("<criteria>\r\n");
/* 234 */     DataCriteria criteria = setting.getCriteria();
/* 235 */     if (criteria != null)
/*     */     {
/* 237 */       StringParameter operand = criteria.getFirstOperand();
/* 238 */       xmlStr.append("<par1>");
/* 239 */       xmlStr.append(ParameterHandler.getXML(operand, true));
/*     */ 
/* 241 */       xmlStr.append("</par1>\r\n");
/* 242 */       xmlStr.append("<op1>");
/* 243 */       String operator = criteria.getFirstOperator();
/* 244 */       xmlStr.append(XMLUtil.printQuoted(operator));
/* 245 */       xmlStr.append("</op1>\r\n");
/*     */ 
/* 247 */       operand = criteria.getSecondOperand();
/* 248 */       if ((operand != null) && (operand.getValue() != null)) {
/* 249 */         xmlStr.append("<par2>");
/*     */ 
/* 251 */         xmlStr.append(ParameterHandler.getXML(operand, true));
/* 252 */         xmlStr.append("</par2>\r\n");
/* 253 */         xmlStr.append("<op2>");
/* 254 */         operator = criteria.getSecondOperator();
/* 255 */         xmlStr.append(XMLUtil.printQuoted(operator));
/* 256 */         xmlStr.append("</op2>\r\n");
/*     */       }
/*     */     }
/* 259 */     xmlStr.append("</criteria>\r\n");
/*     */ 
/* 261 */     IntegerParameter top = setting.getTop();
/* 262 */     if (top.getValue().intValue() >= 0) {
/* 263 */       xmlStr.append("<top>\r\n");
/* 264 */       xmlStr.append(ParameterHandler.getXML(top));
/* 265 */       xmlStr.append("</top>\r\n");
/*     */     }
/*     */ 
/* 268 */     DoubleParameter upper = setting.getUpperPercentage();
/* 269 */     if (upper.getValue().doubleValue() >= 0.0D) {
/* 270 */       xmlStr.append("<upper_percentage>\r\n");
/* 271 */       xmlStr.append(ParameterHandler.getXML(upper));
/* 272 */       xmlStr.append("</upper_percentage>\r\n");
/*     */     }
/*     */ 
/* 275 */     DoubleParameter lower = setting.getLowerPercentage();
/* 276 */     if (lower.getValue().doubleValue() >= 0.0D) {
/* 277 */       xmlStr.append("<lower_percentage>\r\n");
/* 278 */       xmlStr.append(ParameterHandler.getXML(lower));
/* 279 */       xmlStr.append("</lower_percentage>\r\n");
/*     */     }
/* 281 */     xmlStr.append("<cell_operator>\r\n");
/* 282 */     xmlStr.append(ParameterHandler.getXML(
/* 283 */       setting.getCellOperator()));
/* 284 */     xmlStr.append("</cell_operator>\r\n");
/*     */ 
/* 287 */     xmlStr.append("<no_rules>\r\n");
/* 288 */     BooleanParameter useRules = setting.getUseRules();
/* 289 */     ParameterHandler.addParameter(useRules, xmlStr);
/* 290 */     xmlStr.append("<value>");
/* 291 */     xmlStr.append((useRules.getValue().booleanValue()) ? "0" : "1");
/* 292 */     xmlStr.append("</value>\r\n");
/* 293 */     xmlStr.append("</no_rules>\r\n");
/*     */ 
/* 295 */     xmlStr.append("</data_filter>\r\n");
/*     */ 
/* 297 */     return xmlStr.toString();
/*     */   }
/*     */ }

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name:     org.palo.api.subsets.io.xml.DataFilterHandler
 * JD-Core Version:    0.5.4
 */