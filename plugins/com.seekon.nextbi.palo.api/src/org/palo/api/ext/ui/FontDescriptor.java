/*     */ package org.palo.api.ext.ui;
/*     */ 
/*     */ public class FontDescriptor
/*     */ {
/*     */   private int size;
/*     */   private String name;
/*     */   private boolean isBold;
/*     */   private boolean isItalic;
/*     */   private boolean isUnderlined;
/*     */ 
/*     */   public FontDescriptor()
/*     */   {
/*     */   }
/*     */ 
/*     */   public FontDescriptor(String description)
/*     */   {
/*  69 */     parse(description);
/*     */   }
/*     */ 
/*     */   public final boolean isBold()
/*     */   {
/*  79 */     return this.isBold;
/*     */   }
/*     */ 
/*     */   public final void setBold(boolean b)
/*     */   {
/*  88 */     this.isBold = b;
/*     */   }
/*     */ 
/*     */   public final String getName()
/*     */   {
/*  96 */     return this.name;
/*     */   }
/*     */ 
/*     */   public final void setName(String name)
/*     */   {
/* 104 */     this.name = name;
/*     */   }
/*     */ 
/*     */   public final int getSize()
/*     */   {
/* 112 */     return this.size;
/*     */   }
/*     */ 
/*     */   public final void setSize(int size)
/*     */   {
/* 120 */     this.size = size;
/*     */   }
/*     */ 
/*     */   public final boolean isItalic()
/*     */   {
/* 129 */     return this.isItalic;
/*     */   }
/*     */ 
/*     */   public final void setItalic(boolean b)
/*     */   {
/* 138 */     this.isItalic = b;
/*     */   }
/*     */ 
/*     */   public final boolean isUnderlined()
/*     */   {
/* 147 */     return this.isUnderlined;
/*     */   }
/*     */ 
/*     */   public final void setUnderlined(boolean b)
/*     */   {
/* 156 */     this.isUnderlined = b;
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 160 */     StringBuffer fontStr = new StringBuffer();
/* 161 */     fontStr.append(this.name);
/* 162 */     fontStr.append(",");
/* 163 */     fontStr.append(this.size);
/* 164 */     fontStr.append(",");
/* 165 */     fontStr.append((this.isBold) ? "bold" : " ");
/* 166 */     fontStr.append(",");
/* 167 */     fontStr.append((this.isItalic) ? "italic" : " ");
/* 168 */     fontStr.append(",");
/* 169 */     fontStr.append((this.isUnderlined) ? "underlined" : " ");
/* 170 */     fontStr.append(",");
/* 171 */     return fontStr.toString();
/*     */   }
/*     */ 
/*     */   private final void parse(String fontDescription) {
/* 175 */     String[] values = fontDescription.split(",");
/* 176 */     this.name = values[0];
/* 177 */     this.size = Integer.parseInt(values[1]);
/* 178 */     this.isBold = "bold".equals(values[2]);
/* 179 */     this.isItalic = "italic".equals(values[3]);
/* 180 */     this.isUnderlined = "underlined".equals(values[4]);
/*     */   }
/*     */ }

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name:     org.palo.api.ext.ui.FontDescriptor
 * JD-Core Version:    0.5.4
 */