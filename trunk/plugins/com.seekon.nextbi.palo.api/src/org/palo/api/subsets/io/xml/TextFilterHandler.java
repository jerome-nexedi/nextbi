/*     */ package org.palo.api.subsets.io.xml;
/*     */ 
/*     */ import java.util.HashSet;
/*     */ import org.palo.api.Dimension;
/*     */ import org.palo.api.impl.xml.XMLUtil;
/*     */ import org.palo.api.subsets.SubsetFilter;
/*     */ import org.palo.api.subsets.filter.TextFilter;
/*     */ import org.palo.api.subsets.filter.settings.BooleanParameter;
/*     */ import org.palo.api.subsets.filter.settings.ObjectParameter;
/*     */ import org.palo.api.subsets.filter.settings.TextFilterSetting;
/*     */ 
/*     */ class TextFilterHandler extends AbstractSubsetFilterHandler
/*     */ {
/*     */   public static final String ELEMENT_ID = "text_filter";
/*     */   public static final String XPATH = "/subset/text_filter";
/*     */   private static final String EXTENDED_VALUE = "/subset/text_filter/extended/value";
/*     */   private static final String EXTENDED_PARAMETER = "/subset/text_filter/extended/parameter";
/*     */   private static final String EXPRESSION_VALUE = "/subset/text_filter/regexes/value/expression";
/*     */   private static final String EXPRESSION_PARAMETER = "/subset/text_filter/regexes/parameter";
/*     */   private final TextFilterSetting tfInfo;
/*     */ 
/*     */   public TextFilterHandler()
/*     */   {
/*  71 */     this.tfInfo = new TextFilterSetting();
/*     */   }
/*     */ 
/*     */   public final String getXPath() {
/*  75 */     return "/subset/text_filter";
/*     */   }
/*     */ 
/*     */   public final void enter(String path)
/*     */   {
/*     */   }
/*     */ 
/*     */   public final void leave(String path, String value)
/*     */   {
/*  85 */     if (path.equals("/subset/text_filter/regexes/parameter")) {
/*  86 */       ObjectParameter oldExpr = this.tfInfo.getExpressions();
/*  87 */       ObjectParameter newExpr = new ObjectParameter(value);
/*  88 */       newExpr.setValue(oldExpr.getValue());
/*  89 */       this.tfInfo.setExpressions(newExpr);
/*     */     }
/*  91 */     else if (path.equals("/subset/text_filter/regexes/value/expression")) {
/*  92 */       this.tfInfo.addExpression(XMLUtil.dequote(value));
/*  93 */     } else if (path.equals("/subset/text_filter/extended/value")) {
/*  94 */       this.tfInfo.setExtended(Boolean.parseBoolean(value));
/*  95 */     } else if (path.equals("/subset/text_filter/extended/parameter")) {
/*  96 */       BooleanParameter oldParam = this.tfInfo.getExtended();
/*  97 */       this.tfInfo.setExtended(new BooleanParameter(value));
/*  98 */       this.tfInfo.setExtended(oldParam.getValue().booleanValue());
/*     */     }
/*     */   }
/*     */ 
/*     */   public SubsetFilter createFilter(Dimension dimension) {
/* 103 */     return new TextFilter(dimension, this.tfInfo);
/*     */   }
/*     */ 
/*     */   public static final String getPersistenceString(TextFilter filter) {
/* 107 */     TextFilterSetting tfInfo = filter.getSettings();
/* 108 */     ObjectParameter exprParam = tfInfo.getExpressions();
/* 109 */     HashSet<String> expressions = (HashSet)exprParam.getValue();
/* 110 */     if (expressions.isEmpty()) {
/* 111 */       return null;
/*     */     }
/* 113 */     StringBuffer str = new StringBuffer();
/* 114 */     str.append("<text_filter>\r\n");
/* 115 */     str.append("<regexes>\r\n");
/* 116 */     ParameterHandler.addParameter(exprParam, str);
/* 117 */     str.append("<value>\r\n");
/* 118 */     for (String expr : expressions) {
/* 119 */       expr = (expr.length() > 0) ? expr : ".*";
/* 120 */       str.append("<expression>" + XMLUtil.quote(expr) + "</expression>\r\n");
/*     */     }
/* 122 */     str.append("</value>\r\n");
/* 123 */     str.append("</regexes>\r\n");
/* 124 */     str.append("<extended>\r\n");
/* 125 */     str.append(ParameterHandler.getXML(tfInfo.getExtended()));
/* 126 */     str.append("</extended>\r\n");
/* 127 */     str.append("</text_filter>\r\n");
/* 128 */     return str.toString();
/*     */   }
/*     */ }

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name:     org.palo.api.subsets.io.xml.TextFilterHandler
 * JD-Core Version:    0.5.4
 */