/*     */ package org.palo.viewapi.exporters;
/*     */ 
/*     */ import com.itextpdf.text.BaseColor;
/*     */ import com.itextpdf.text.pdf.PdfContentByte;
/*     */ import org.palo.api.ext.ui.ColorDescriptor;
/*     */ import org.palo.api.ext.ui.FontDescriptor;
/*     */ import org.palo.viewapi.uimodels.formats.BorderData;
/*     */ 
/*     */ class PDFUtils
/*     */ {
/*     */   private static final float LINE_DASH_LEN = 2.0F;
/*     */   private static final float LINE_DOT_LEN = 1.0F;
/* 101 */   private static final float[] LINE_DASH_DOT = { 2.0F, 1.0F };
/* 102 */   private static final float[] LINE_DASH_DOT_DOT = { 2.0F, 1.0F, 1.0F };
/*     */ 
/*     */   static final BaseColor convertColor(ColorDescriptor desc)
/*     */   {
/*  56 */     if (desc == null)
/*  57 */       return null;
/*  58 */     return new BaseColor(desc.getRed(), desc.getGreen(), desc.getBlue());
/*     */   }
/*     */ 
/*     */   static final int convertFontStyle(FontDescriptor desc) {
/*  62 */     if (desc == null)
/*  63 */       return 0;
/*  64 */     int style = (desc.isBold()) ? 1 : 0;
/*  65 */     style |= ((desc.isItalic()) ? 2 : 0);
/*  66 */     style |= ((desc.isUnderlined()) ? 4 : 0);
/*  67 */     return style;
/*     */   }
/*     */ 
/*     */   static final void applyBorderDataLineStyle(PdfContentByte pByte, BorderData bData) {
/*  71 */     if (pByte == null)
/*  72 */       return;
/*  73 */     if (bData == null) {
/*  74 */       pByte.setLineWidth(1.0F);
/*  75 */       pByte.setLineDash(1.0F, 0.0F);
/*  76 */       pByte.setColorStroke(BaseColor.BLACK);
/*  77 */       return;
/*     */     }
/*  79 */     switch (bData.getLineStyle())
/*     */     {
/*     */     case 2:
/*  81 */       pByte.setLineDash(2.0F, 0.0F);
/*  82 */       break;
/*     */     case 4:
/*  84 */       pByte.setLineDash(LINE_DASH_DOT, 0.0F);
/*  85 */       break;
/*     */     case 5:
/*  87 */       pByte.setLineDash(LINE_DASH_DOT_DOT, 0.0F);
/*  88 */       break;
/*     */     case 3:
/*  90 */       pByte.setLineDash(1.0F, 0.0F);
/*     */     }
/*     */ 
/*  93 */     pByte.setLineWidth(bData.getLineWidth() / 1.5F);
/*  94 */     if (bData.getLineColor() != null)
/*  95 */       pByte.setColorStroke((bData.getLineColor() != null) ? convertColor(
/*  96 */         bData.getLineColor()) : 
/*  96 */         BaseColor.BLACK);
/*     */   }
/*     */ }

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi.jar
 * Qualified Name:     org.palo.viewapi.exporters.PDFUtils
 * JD-Core Version:    0.5.4
 */