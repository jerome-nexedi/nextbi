/*     */ package org.palo.api.subsets.io.xml;
/*     */ 
/*     */ import org.palo.api.Dimension;
/*     */ import org.palo.api.Element;
/*     */ import org.palo.api.subsets.SubsetFilter;
/*     */ import org.palo.api.subsets.filter.HierarchicalFilter;
/*     */ import org.palo.api.subsets.filter.settings.BooleanParameter;
/*     */ import org.palo.api.subsets.filter.settings.HierarchicalFilterSetting;
/*     */ import org.palo.api.subsets.filter.settings.IntegerParameter;
/*     */ import org.palo.api.subsets.filter.settings.StringParameter;
/*     */ 
/*     */ class HierarchicalFilterHandler extends AbstractSubsetFilterHandler
/*     */ {
/*     */   public static final String XPATH = "/subset/hierarchical_filter";
/*     */   private static final String ELEMENT_VALUE = "/subset/hierarchical_filter/element/value";
/*     */   private static final String ELEMENT_PARAMETER = "/subset/hierarchical_filter/element/parameter";
/*     */   private static final String ABOVE_VALUE = "/subset/hierarchical_filter/above/value";
/*     */   private static final String ABOVE_PARAMETER = "/subset/hierarchical_filter/above/parameter";
/*     */   private static final String EXCLUSIVE_VALUE = "/subset/hierarchical_filter/exclusive/value";
/*     */   private static final String EXCLUSIVE_PARAMETER = "/subset/hierarchical_filter/exclusive/parameter";
/*     */   private static final String HIDE_VALUE = "/subset/hierarchical_filter/hide/value";
/*     */   private static final String HIDE_PARAMETER = "/subset/hierarchical_filter/hide/parameter";
/*     */   private static final String REV_ELEMENT_VALUE = "/subset/hierarchical_filter/revolve_element/value";
/*     */   private static final String REV_ELEMENT_PARAMETER = "/subset/hierarchical_filter/revolve_element/parameter";
/*     */   private static final String REV_COUNT_VALUE = "/subset/hierarchical_filter/revolve_count/value";
/*     */   private static final String REV_COUNT_PARAMETER = "/subset/hierarchical_filter/revolve_count/parameter";
/*     */   private static final String REV_ADD_VALUE = "/subset/hierarchical_filter/revolve_add/value";
/*     */   private static final String REV_ADD_PARAMETER = "/subset/hierarchical_filter/revolve_add/parameter";
/*     */   private static final String LEVEL_START_VALUE = "/subset/hierarchical_filter/level_start/value";
/*     */   private static final String LEVEL_START_PARAMETER = "/subset/hierarchical_filter/level_start/parameter";
/*     */   private static final String LEVEL_END_VALUE = "/subset/hierarchical_filter/level_end/value";
/*     */   private static final String LEVEL_END_PARAMETER = "/subset/hierarchical_filter/level_end/parameter";
/*     */   private final HierarchicalFilterSetting hfInfo;
/*     */ 
/*     */   public HierarchicalFilterHandler()
/*     */   {
/*  83 */     this.hfInfo = new HierarchicalFilterSetting();
/*     */   }
/*     */ 
/*     */   public final String getXPath() {
/*  87 */     return "/subset/hierarchical_filter";
/*     */   }
/*     */ 
/*     */   public final void enter(String path)
/*     */   {
/*     */   }
/*     */ 
/*     */   public final void leave(String path, String value)
/*     */   {
/*  97 */     if (path.equals("/subset/hierarchical_filter/above/value")) {
/*  98 */       this.hfInfo.setAbove(SubsetXMLHandler.getBoolean(value));
/*  99 */     } else if (path.equals("/subset/hierarchical_filter/above/parameter")) {
/* 100 */       BooleanParameter oldParam = this.hfInfo.getAbove();
/* 101 */       this.hfInfo.setAbove(new BooleanParameter(value));
/* 102 */       this.hfInfo.setAbove(oldParam.getValue().booleanValue());
/*     */     }
/* 104 */     else if (path.equals("/subset/hierarchical_filter/exclusive/value")) {
/* 105 */       this.hfInfo.setExclusive(SubsetXMLHandler.getBoolean(value));
/* 106 */     } else if (path.equals("/subset/hierarchical_filter/exclusive/parameter")) {
/* 107 */       BooleanParameter oldParam = this.hfInfo.getExclusive();
/* 108 */       this.hfInfo.setExclusive(new BooleanParameter(value));
/* 109 */       this.hfInfo.setExclusive(oldParam.getValue().booleanValue());
/*     */     }
/* 111 */     else if (path.equals("/subset/hierarchical_filter/element/value")) {
/* 112 */       this.hfInfo.setRefElement(value);
/* 113 */     } else if (path.equals("/subset/hierarchical_filter/element/parameter")) {
/* 114 */       StringParameter oldParam = this.hfInfo.getRefElement();
/* 115 */       this.hfInfo.setRefElement(new StringParameter(value));
/* 116 */       this.hfInfo.setRefElement(oldParam.getValue());
/*     */     }
/* 118 */     else if (path.equals("/subset/hierarchical_filter/hide/value")) {
/* 119 */       this.hfInfo.setHideMode(SubsetXMLHandler.getInteger(value));
/* 120 */     } else if (path.equals("/subset/hierarchical_filter/hide/parameter")) {
/* 121 */       IntegerParameter oldParam = this.hfInfo.getHideMode();
/* 122 */       this.hfInfo.setHideMode(new IntegerParameter(value));
/* 123 */       this.hfInfo.setHideMode(oldParam.getValue().intValue());
/*     */     }
/* 125 */     else if (path.equals("/subset/hierarchical_filter/revolve_element/value")) {
/* 126 */       this.hfInfo.setRevolveElement(value);
/* 127 */     } else if (path.equals("/subset/hierarchical_filter/revolve_element/parameter")) {
/* 128 */       StringParameter oldParam = this.hfInfo.getRevolveElement();
/* 129 */       this.hfInfo.setRevolveElement(new StringParameter(value));
/* 130 */       this.hfInfo.setRevolveElement(oldParam.getValue());
/*     */     }
/* 132 */     else if (path.equals("/subset/hierarchical_filter/revolve_count/value")) {
/* 133 */       this.hfInfo.setRevolveCount(SubsetXMLHandler.getInteger(value));
/* 134 */     } else if (path.equals("/subset/hierarchical_filter/revolve_count/parameter")) {
/* 135 */       IntegerParameter oldParam = this.hfInfo.getRevolveCount();
/* 136 */       this.hfInfo.setRevolveCount(new IntegerParameter(value));
/* 137 */       this.hfInfo.setRevolveCount(oldParam.getValue().intValue());
/*     */     }
/* 139 */     else if (path.equals("/subset/hierarchical_filter/revolve_add/value")) {
/* 140 */       this.hfInfo.setRevolveMode(SubsetXMLHandler.getInteger(value));
/* 141 */     } else if (path.equals("/subset/hierarchical_filter/revolve_add/parameter")) {
/* 142 */       IntegerParameter oldParam = this.hfInfo.getRevolveMode();
/* 143 */       this.hfInfo.setRevolveMode(new IntegerParameter(value));
/* 144 */       this.hfInfo.setRevolveMode(oldParam.getValue().intValue());
/*     */     }
/* 146 */     else if (path.equals("/subset/hierarchical_filter/level_start/value")) {
/* 147 */       if (this.subsetVersion.equals("1.0rc2"))
/* 148 */         this.hfInfo.setStartElement(value);
/*     */       else
/* 150 */         this.hfInfo.setStartLevel(SubsetXMLHandler.getInteger(value));
/* 151 */     } else if (path.equals("/subset/hierarchical_filter/level_start/parameter")) {
/* 152 */       if (this.subsetVersion.equals("1.0rc2")) {
/* 153 */         StringParameter oldParam = this.hfInfo.getStartElement();
/* 154 */         this.hfInfo.setStartElement(new StringParameter(value));
/* 155 */         this.hfInfo.setStartElement(oldParam.getValue());
/*     */       } else {
/* 157 */         IntegerParameter oldParam = this.hfInfo.getStartLevel();
/* 158 */         this.hfInfo.setStartLevel(new IntegerParameter(value));
/* 159 */         this.hfInfo.setStartLevel(oldParam.getValue().intValue());
/*     */       }
/*     */     }
/* 162 */     else if (path.equals("/subset/hierarchical_filter/level_end/value")) {
/* 163 */       if (this.subsetVersion.equals("1.0rc2"))
/* 164 */         this.hfInfo.setEndElement(value);
/*     */       else
/* 166 */         this.hfInfo.setEndLevel(SubsetXMLHandler.getInteger(value));
/* 167 */     } else if (path.equals("/subset/hierarchical_filter/level_end/parameter")) {
/* 168 */       if (this.subsetVersion.equals("1.0rc2")) {
/* 169 */         StringParameter oldParam = this.hfInfo.getEndElement();
/* 170 */         this.hfInfo.setEndElement(new StringParameter(value));
/* 171 */         this.hfInfo.setEndElement(oldParam.getValue());
/*     */       } else {
/* 173 */         IntegerParameter oldParam = this.hfInfo.getEndLevel();
/* 174 */         this.hfInfo.setEndLevel(new IntegerParameter(value));
/* 175 */         this.hfInfo.setEndLevel(oldParam.getValue().intValue());
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public final SubsetFilter createFilter(Dimension dimension) {
/* 181 */     if ((this.hfInfo.doLevelSelection()) && (this.subsetVersion.equals("1.0rc2")))
/*     */     {
/* 183 */       Element start = 
/* 184 */         dimension.getElementById(this.hfInfo.getStartElement().getValue());
/* 185 */       if (start != null) {
/* 186 */         this.hfInfo.setStartLevel(start.getLevel());
/* 187 */         this.hfInfo.setStartElement("");
/*     */       }
/* 189 */       Element end = 
/* 190 */         dimension.getElementById(this.hfInfo.getEndElement().getValue());
/* 191 */       if (end != null) {
/* 192 */         this.hfInfo.setEndLevel(end.getLevel());
/* 193 */         this.hfInfo.setEndElement("");
/*     */       }
/*     */     }
/* 196 */     return new HierarchicalFilter(dimension, this.hfInfo);
/*     */   }
/*     */ 
/*     */   public static final String getPersistenceString(HierarchicalFilter filter) {
/* 200 */     HierarchicalFilterSetting hfInfo = filter.getSettings();
/* 201 */     StringBuffer xmlStr = new StringBuffer();
/* 202 */     xmlStr.append("<hierarchical_filter>\r\n");
/* 203 */     if (hfInfo.doAboveBelowSelection()) {
/* 204 */       xmlStr.append("<element>\r\n");
/* 205 */       xmlStr.append(ParameterHandler.getXML(
/* 206 */         hfInfo.getRefElement()));
/* 207 */       xmlStr.append("</element>\r\n");
/* 208 */       xmlStr.append("<above>\r\n");
/* 209 */       xmlStr.append(ParameterHandler.getXML(
/* 210 */         hfInfo.getAbove()));
/* 211 */       xmlStr.append("</above>\r\n");
/* 212 */       xmlStr.append("<exclusive>\r\n");
/* 213 */       xmlStr.append(ParameterHandler.getXML(
/* 214 */         hfInfo.getExclusive()));
/* 215 */       xmlStr.append("</exclusive>\r\n");
/*     */     }
/* 217 */     if (hfInfo.doHide()) {
/* 218 */       xmlStr.append("<hide>\r\n");
/* 219 */       xmlStr.append(ParameterHandler.getXML(
/* 220 */         hfInfo.getHideMode()));
/* 221 */       xmlStr.append("</hide>\r\n");
/*     */     }
/* 223 */     if (hfInfo.doLevelSelection()) {
/* 224 */       xmlStr.append("<level_start>\r\n");
/* 225 */       xmlStr.append(ParameterHandler.getXML(hfInfo.getStartLevel()));
/* 226 */       xmlStr.append("</level_start>\r\n");
/* 227 */       xmlStr.append("<level_end>\r\n");
/* 228 */       xmlStr.append(ParameterHandler.getXML(hfInfo.getEndLevel()));
/* 229 */       xmlStr.append("</level_end>\r\n");
/*     */     }
/* 231 */     if (hfInfo.doRevolve()) {
/* 232 */       xmlStr.append("<revolve_element>\r\n");
/* 233 */       xmlStr.append(ParameterHandler.getXML(
/* 234 */         hfInfo.getRevolveElement()));
/* 235 */       xmlStr.append("</revolve_element>\r\n");
/*     */ 
/* 237 */       xmlStr.append("<revolve_count>\r\n");
/* 238 */       xmlStr.append(ParameterHandler.getXML(
/* 239 */         hfInfo.getRevolveCount()));
/* 240 */       xmlStr.append("</revolve_count>\r\n");
/*     */ 
/* 242 */       xmlStr.append("<revolve_add>\r\n");
/* 243 */       xmlStr.append(ParameterHandler.getXML(
/* 244 */         hfInfo.getRevolveMode()));
/* 245 */       xmlStr.append("</revolve_add>\r\n");
/*     */     }
/* 247 */     xmlStr.append("</hierarchical_filter>\r\n");
/* 248 */     return xmlStr.toString();
/*     */   }
/*     */ }

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name:     org.palo.api.subsets.io.xml.HierarchicalFilterHandler
 * JD-Core Version:    0.5.4
 */