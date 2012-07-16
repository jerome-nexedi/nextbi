/*     */ package org.palo.api.ext.ui.table.impl;
/*     */ 
/*     */ import org.palo.api.ext.ui.ColorDescriptor;
/*     */ import org.palo.api.ext.ui.FontDescriptor;
/*     */ import org.palo.api.ext.ui.Format;
/*     */ 
/*     */ public class FormatBuilder
/*     */ {
/*     */   private ColorDescriptor bgColor;
/*     */   private ColorDescriptor fontColor;
/*     */   private int prio;
/*     */   private String nrFmt;
/*     */   private String name;
/*     */   private int size;
/*     */   private boolean bold;
/*     */   private boolean italic;
/*     */   private boolean underlined;
/*     */ 
/*     */   final void setPriority(String prio)
/*     */   {
/*  61 */     this.prio = (((prio != null) && (prio.length() > 0)) ? 
/*  62 */       Integer.parseInt(prio) : 1);
/*     */   }
/*     */   final void setNumberFormatTemplate(String fmt) {
/*  65 */     this.nrFmt = fmt;
/*     */   }
/*     */   final void setBackGroundColor(String r, String g, String b) {
/*  68 */     this.bgColor = getRGB(r, g, b);
/*     */   }
/*     */ 
/*     */   final void setFontColor(String r, String g, String b) {
/*  72 */     this.fontColor = getRGB(r, g, b);
/*     */   }
/*     */ 
/*     */   final void setFontName(String name) {
/*  76 */     this.name = name;
/*     */   }
/*     */ 
/*     */   final void setFontSize(String size) {
/*  80 */     this.size = (((size != null) && (size.length() > 0)) ? 
/*  81 */       Integer.parseInt(size) : 8);
/*     */   }
/*     */ 
/*     */   final void setBold(String bold) {
/*  85 */     this.bold = ((bold != null) ? "bold".equalsIgnoreCase(bold) : false);
/*     */   }
/*     */ 
/*     */   final void setItalic(String italic) {
/*  89 */     this.italic = ((italic != null) ? 
/*  90 */       "italic".equalsIgnoreCase(italic) : false);
/*     */   }
/*     */ 
/*     */   final void setUnderlined(String underlined) {
/*  94 */     this.underlined = ((underlined != null) ? 
/*  95 */       "underlined".equalsIgnoreCase(underlined) : false);
/*     */   }
/*     */ 
/*     */   Format create()
/*     */   {
/* 101 */     FontDescriptor fd = new FontDescriptor();
/* 102 */     fd.setName(this.name);
/* 103 */     fd.setSize(this.size);
/* 104 */     fd.setBold(this.bold);
/* 105 */     fd.setItalic(this.italic);
/* 106 */     fd.setUnderlined(this.underlined);
/* 107 */     if (this.name == null) {
/* 108 */       fd = null;
/*     */     }
/* 110 */     DefaultFormat fmt = new DefaultFormat();
/* 111 */     fmt.setBgColor(this.bgColor);
/* 112 */     fmt.setFont(fd);
/* 113 */     fmt.setFontColor(this.fontColor);
/* 114 */     fmt.setNumberFormatPattern(this.nrFmt);
/* 115 */     fmt.setPriority(this.prio);
/* 116 */     return fmt;
/*     */   }
/*     */ 
/*     */   private final ColorDescriptor getRGB(String r, String g, String b) {
/* 120 */     int _r = ((r != null) && (r.length() > 0)) ? Integer.parseInt(r) : 255;
/* 121 */     int _g = ((g != null) && (g.length() > 0)) ? Integer.parseInt(g) : 255;
/* 122 */     int _b = ((b != null) && (b.length() > 0)) ? Integer.parseInt(b) : 255;
/* 123 */     return new ColorDescriptor(_r, _g, _b);
/*     */   }
/*     */ }

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name:     org.palo.api.ext.ui.table.impl.FormatBuilder
 * JD-Core Version:    0.5.4
 */