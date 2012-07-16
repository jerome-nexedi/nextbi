/*     */ package org.palo.api.subsets.io;
/*     */ 
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.OutputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import org.palo.api.Dimension;
/*     */ import org.palo.api.exceptions.PaloIOException;
/*     */ import org.palo.api.subsets.Subset2;
/*     */ import org.palo.api.subsets.SubsetFilter;
/*     */ import org.palo.api.subsets.io.xml.SubsetXMLHandler;
/*     */ 
/*     */ class SubsetWriter
/*     */ {
/*  63 */   private static SubsetWriter instance = new SubsetWriter();
/*     */ 
/*     */   static final SubsetWriter getInstance() {
/*  66 */     return instance;
/*     */   }
/*     */ 
/*     */   final void toXML(OutputStream output, Subset2 subset)
/*     */     throws PaloIOException
/*     */   {
/*     */     try
/*     */     {
/*  80 */       toXMLInternal(output, subset);
/*     */     } catch (Exception e) {
/*  82 */       PaloIOException pex = 
/*  83 */         new PaloIOException("Writing subset to xml failed!", e);
/*  84 */       pex.setData(subset);
/*  85 */       throw pex;
/*     */     }
/*     */   }
/*     */ 
/*     */   private final void toXMLInternal(OutputStream output, Subset2 subset) throws Exception
/*     */   {
/*  91 */     PrintWriter w = new PrintWriter(
/*  92 */       new BufferedWriter(new OutputStreamWriter(output, "UTF-8")));
/*     */     try
/*     */     {
/*  95 */       w.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n");
/*  96 */       w.write("<!--!DOCTYPE subset SYSTEM \"filters.dtd\" -->\r\n");
/*  97 */       w.write("<?palosubset version=\"1.0\"?>\r\n");
/*     */ 
/* 100 */       writeSubsetElement(w, subset);
/*     */ 
/* 102 */       SubsetFilter[] filters = subset.getFilters();
/* 103 */       filters = sort(filters);
/* 104 */       for (int i = 0; i < filters.length; ++i) {
/* 105 */         String xmlExpr = 
/* 106 */           SubsetXMLHandler.getFilterXMLExpression(filters[i]);
/* 107 */         if (xmlExpr != null) {
/* 108 */           w.write(xmlExpr);
/*     */         }
/*     */       }
/* 111 */       w.write("</subset>\r\n");
/*     */     } finally {
/* 113 */       w.close();
/*     */     }
/*     */   }
/*     */ 
/*     */   private final void writeSubsetElement(PrintWriter w, Subset2 subset) {
/* 118 */     String id = subset.getId();
/* 119 */     String srcDimId = subset.getDimension().getId();
/*     */ 
/* 121 */     StringBuffer subsetElement = new StringBuffer();
/* 122 */     subsetElement.append("<subset id=\"");
/* 123 */     subsetElement.append(id);
/* 124 */     subsetElement.append("\" sourceDimensionId=\"");
/* 125 */     subsetElement.append(srcDimId);
/* 126 */     subsetElement.append("\" \r\n");
/*     */ 
/* 152 */     subsetElement.append("xmlns=\"http://www.jedox.com/palo/SubsetXML\">\r\n");
/*     */ 
/* 159 */     int indent = subset.getIndent();
/* 160 */     subsetElement.append("<indent><value>");
/* 161 */     subsetElement.append(indent);
/* 162 */     subsetElement.append("</value></indent>\r\n");
/* 163 */     w.write(subsetElement.toString());
/*     */   }
/*     */ 
/*     */   private final SubsetFilter[] sort(SubsetFilter[] filters)
/*     */   {
/* 170 */     HashMap allFilters = new HashMap();
/* 171 */     for (int i = 0; i < filters.length; ++i)
/* 172 */       allFilters.put(new Integer(filters[i].getType()), filters[i]);
/* 173 */     ArrayList sortedFilters = new ArrayList();
/*     */ 
/* 175 */     addFilter(64, sortedFilters, allFilters);
/* 176 */     addFilter(2, sortedFilters, allFilters);
/* 177 */     addFilter(1, sortedFilters, allFilters);
/* 178 */     addFilter(32, sortedFilters, allFilters);
/* 179 */     addFilter(8, sortedFilters, allFilters);
/* 180 */     addFilter(4, sortedFilters, allFilters);
/* 181 */     addFilter(16, sortedFilters, allFilters);
/* 182 */     return (SubsetFilter[])sortedFilters.toArray(new SubsetFilter[sortedFilters.size()]);
/*     */   }
/*     */ 
/*     */   private final void addFilter(int type, List<SubsetFilter> sorted, HashMap<Integer, SubsetFilter> allFilters)
/*     */   {
/* 187 */     SubsetFilter filter = (SubsetFilter)allFilters.get(new Integer(type));
/* 188 */     if (filter != null)
/* 189 */       sorted.add(filter);
/*     */   }
/*     */ }

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name:     org.palo.api.subsets.io.SubsetWriter
 * JD-Core Version:    0.5.4
 */