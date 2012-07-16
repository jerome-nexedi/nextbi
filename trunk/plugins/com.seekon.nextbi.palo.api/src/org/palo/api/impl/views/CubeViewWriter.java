/*     */ package org.palo.api.impl.views;
/*     */ 
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.PrintStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.io.Writer;
/*     */ import org.palo.api.Axis;
/*     */ import org.palo.api.Cube;
/*     */ import org.palo.api.CubeView;
/*     */ import org.palo.api.Dimension;
/*     */ import org.palo.api.Element;
/*     */ import org.palo.api.Hierarchy;
/*     */ import org.palo.api.Subset;
/*     */ import org.palo.api.impl.xml.XMLUtil;
/*     */ import org.palo.api.subsets.Subset2;
/*     */ import org.palo.api.utils.ElementPath;
/*     */ 
/*     */ class CubeViewWriter
/*     */ {
/*  66 */   private static CubeViewWriter instance = new CubeViewWriter();
/*     */ 
/*  68 */   static CubeViewWriter getInstance() { return instance; }
/*     */ 
/*     */ 
/*     */   public void toXML(OutputStream output, CubeView view)
/*     */   {
/*     */     try
/*     */     {
/*  77 */       toXMLInternal(output, view);
/*     */     } catch (Exception e) {
/*  79 */       System.err
/*  80 */         .println("CubeViewWriter.toXML: " + e.getLocalizedMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   private void toXMLInternal(OutputStream output, CubeView view) throws Exception {
/*  85 */     PrintWriter w = new PrintWriter(
/*  86 */       new BufferedWriter(new OutputStreamWriter(output, "UTF-8")), true);
/*     */     try {
/*  88 */       w.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n");
/*  89 */       w.write("<?paloview version=\"1.4\"?>\r\n");
/*  90 */       w.write("<view\r\n");
/*  91 */       w.write("  id=\"" + XMLUtil.printQuoted(view.getId()) + "\"\r\n");
/*  92 */       w.write("  name=\"" + XMLUtil.printQuoted(view.getName()) + "\"\r\n");
/*  93 */       w.write("  description=\"" + XMLUtil.printQuoted(view.getDescription()) + "\"\r\n");
/*  94 */       w.write("  cube=\"" + XMLUtil.printQuoted(view.getCube().getId()) + "\"\r\n");
/*  95 */       w.write(">\r\n");
/*  96 */       String[] keys = view.getProperties();
/*  97 */       int i = 0; for (int n = keys.length; i < n; ++i) {
/*  98 */         w.write(" <property id=\"" + XMLUtil.printQuoted(keys[i]) + "\" value=\"" + 
/*  99 */           XMLUtil.printQuoted(view.getPropertyValue(keys[i])) + "\"/>\r\n");
/*     */       }
/*     */ 
/* 102 */       Axis[] axes = view.getAxes();
/* 103 */       writeAxes(w, axes);
/* 104 */       w.write("</view>\r\n");
/*     */     }
/*     */     finally
/*     */     {
/* 108 */       w.close();
/*     */     }
/*     */   }
/*     */ 
/*     */   private final void writeAxes(Writer w, Axis[] axes) throws IOException
/*     */   {
/* 114 */     for (int i = 0; i < axes.length; ++i) {
/* 115 */       Axis axis = axes[i];
/* 116 */       w.write("<axis id=\"" + XMLUtil.printQuoted(axis.getId()) + 
/* 117 */         "\" name=\"" + XMLUtil.printQuoted(axis.getName()) + 
/* 118 */         "\">\r\n");
/* 119 */       Hierarchy[] hierarchies = axis.getHierarchies();
/* 120 */       Dimension[] dimensions = axis.getDimensions();
/* 121 */       writeHierarchies(w, hierarchies);
/* 122 */       writeSelectedElements(w, hierarchies, axis);
/* 123 */       writeActiveSubsets(w, dimensions, axis);
/* 124 */       writeExpandedPaths(w, dimensions, axis);
/*     */ 
/* 126 */       writeVisiblePaths(w, hierarchies, axis);
/* 127 */       writeProperties(w, axis);
/* 128 */       w.write("</axis>\r\n");
/*     */     }
/*     */   }
/*     */ 
/*     */   private final void writeHierarchies(Writer w, Hierarchy[] hierarchies)
/*     */     throws IOException
/*     */   {
/* 148 */     if (hierarchies.length == 0)
/* 149 */       return;
/* 150 */     w.write("  <dimensions ids=\"");
/* 151 */     int lastHier = hierarchies.length - 1;
/* 152 */     for (int d = 0; d < hierarchies.length; ++d) {
/* 153 */       w.write(XMLUtil.printQuoted(hierarchies[d].getDimension().getId()));
/* 154 */       if (d < lastHier)
/* 155 */         w.write(",");
/*     */     }
/* 157 */     w.write("\" hierarchyIds=\"");
/* 158 */     for (int d = 0; d < hierarchies.length; ++d) {
/* 159 */       w.write(XMLUtil.printQuoted(hierarchies[d].getId()));
/* 160 */       if (d < lastHier)
/* 161 */         w.write(",");
/*     */     }
/* 163 */     w.write("\" />\r\n");
/*     */   }
/*     */ 
/*     */   private final void writeSelectedElements(Writer w, Hierarchy[] hierarchies, Axis axis)
/*     */     throws IOException
/*     */   {
/* 182 */     for (int d = 0; d < hierarchies.length; ++d) {
/* 183 */       Element element = axis.getSelectedElement(hierarchies[d]);
/* 184 */       if (element != null)
/* 185 */         w.write("  <selected element=\"" + 
/* 186 */           XMLUtil.printQuoted(element.getId()) + 
/* 187 */           "\" dimension=\"" + 
/* 188 */           XMLUtil.printQuoted(hierarchies[d].getDimension().getId()) + 
/* 189 */           "\" />\r\n");
/*     */     }
/*     */   }
/*     */ 
/*     */   private final void writeActiveSubsets(Writer w, Dimension[] dimensions, Axis axis)
/*     */     throws IOException
/*     */   {
/* 196 */     for (int d = 0; d < dimensions.length; ++d) {
/* 197 */       Subset activeSub = axis.getActiveSubset(dimensions[d]);
/* 198 */       if (activeSub != null) {
/* 199 */         w.write("  <active subset=\"" + 
/* 200 */           XMLUtil.printQuoted(activeSub.getId()) + 
/* 201 */           "\" dimension=\"" + 
/* 202 */           XMLUtil.printQuoted(dimensions[d].getId()) + 
/* 203 */           "\" />\r\n");
/*     */       }
/*     */ 
/* 206 */       Subset2 activeSub2 = axis.getActiveSubset2(dimensions[d]);
/* 207 */       if (activeSub2 != null)
/* 208 */         w.write("  <active subset2=\"" + 
/* 209 */           XMLUtil.printQuoted(activeSub2.getId()) + 
/* 210 */           "\" type=\"" + 
/* 211 */           XMLUtil.printQuoted(activeSub2.getType()) + 
/* 212 */           "\" dimension=\"" + 
/* 213 */           XMLUtil.printQuoted(dimensions[d].getId()) + 
/* 214 */           "\" />\r\n");
/*     */     }
/*     */   }
/*     */ 
/*     */   private final void writeExpandedPaths(Writer w, Dimension[] dimensions, Axis axis)
/*     */     throws IOException
/*     */   {
/* 222 */     ElementPath[] paths = axis.getExpandedPaths();
/* 223 */     for (int i = 0; i < paths.length; ++i)
/* 224 */       w.write("  <expanded paths=\"" + 
/* 225 */         XMLUtil.printQuoted(paths[i].toString()) + "\" />\r\n");
/*     */   }
/*     */ 
/*     */   private final void writeVisiblePaths(Writer w, Hierarchy[] hierarchies, Axis axis)
/*     */     throws IOException
/*     */   {
/* 245 */     for (int d = 0; d < hierarchies.length; ++d) {
/* 246 */       ElementPath[] paths = axis.getVisiblePaths(hierarchies[d]);
/* 247 */       for (int i = 0; i < paths.length; ++i)
/* 248 */         w.write("  <visible path=\"" + 
/* 249 */           XMLUtil.printQuoted(paths[i].toString()) + 
/* 250 */           "\" dimension=\"" + 
/* 251 */           XMLUtil.printQuoted(hierarchies[d].getDimension().getId()) + 
/* 252 */           "\" />\r\n");
/*     */     }
/*     */   }
/*     */ 
/*     */   private final void writeProperties(Writer w, Axis axis) throws IOException
/*     */   {
/* 258 */     String[] propIds = axis.getProperties();
/* 259 */     for (int i = 0; i < propIds.length; ++i)
/* 260 */       w.write("  <property id=\"" + XMLUtil.printQuoted(propIds[i]) + 
/* 261 */         "\" value=\"" + 
/* 262 */         XMLUtil.printQuoted(axis.getPropertyValue(propIds[i])) + 
/* 263 */         "\" />\r\n");
/*     */   }
/*     */ }

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name:     org.palo.api.impl.views.CubeViewWriter
 * JD-Core Version:    0.5.4
 */