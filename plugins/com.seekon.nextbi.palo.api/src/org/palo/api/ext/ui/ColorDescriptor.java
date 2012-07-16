/*     */ package org.palo.api.ext.ui;
/*     */ 
/*     */ public class ColorDescriptor
/*     */ {
/*     */   private int red;
/*     */   private int green;
/*     */   private int blue;
/*     */ 
/*     */   public ColorDescriptor(int red, int green, int blue)
/*     */   {
/*  58 */     this.red = red;
/*  59 */     this.green = green;
/*  60 */     this.blue = blue;
/*     */   }
/*     */ 
/*     */   public final int getBlue()
/*     */   {
/*  69 */     return this.blue;
/*     */   }
/*     */ 
/*     */   public final void setBlue(int blue)
/*     */   {
/*  78 */     this.blue = blue;
/*     */   }
/*     */ 
/*     */   public final int getGreen()
/*     */   {
/*  86 */     return this.green;
/*     */   }
/*     */ 
/*     */   public final void setGreen(int green)
/*     */   {
/*  94 */     this.green = green;
/*     */   }
/*     */ 
/*     */   public final int getRed()
/*     */   {
/* 102 */     return this.red;
/*     */   }
/*     */ 
/*     */   public final void setRed(int red)
/*     */   {
/* 110 */     this.red = red;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object obj)
/*     */   {
/* 115 */     if (obj instanceof ColorDescriptor) {
/* 116 */       ColorDescriptor other = (ColorDescriptor)obj;
/*     */ 
/* 119 */       return (this.red == other.red) && 
/* 118 */         (this.green == other.green) && 
/* 119 */         (this.blue == other.blue);
/*     */     }
/* 121 */     return false;
/*     */   }
/*     */ 
/*     */   public int hashCode() {
/* 125 */     int hc = 17;
/* 126 */     hc += 37 * this.blue;
/* 127 */     hc += 37 * this.green;
/* 128 */     hc += 37 * this.red;
/* 129 */     return hc;
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 133 */     return "Color [" + this.red + ", " + this.green + ", " + this.blue + "]";
/*     */   }
/*     */ }

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name:     org.palo.api.ext.ui.ColorDescriptor
 * JD-Core Version:    0.5.4
 */