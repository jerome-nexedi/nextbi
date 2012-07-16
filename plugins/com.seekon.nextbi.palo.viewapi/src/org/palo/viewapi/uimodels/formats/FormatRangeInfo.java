/*     */ package org.palo.viewapi.uimodels.formats;
/*     */ 
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Set;
/*     */ import org.palo.api.Dimension;
/*     */ import org.palo.api.Element;
/*     */ 
/*     */ public class FormatRangeInfo
/*     */ {
/*     */   private Element[][] cells;
/*     */   private final LinkedHashMap<Dimension, Integer> levelDims;
/*     */ 
/*     */   public FormatRangeInfo()
/*     */   {
/*  66 */     this.cells = null;
/*  67 */     this.levelDims = null;
/*     */   }
/*     */ 
/*     */   private FormatRangeInfo(FormatRangeInfo info)
/*     */   {
/*  76 */     this.cells = ((Element[][])info.cells.clone());
/*  77 */     this.levelDims = ((LinkedHashMap)info.levelDims.clone());
/*     */   }
/*     */ 
/*     */   public FormatRangeInfo(Element[][] cells)
/*     */   {
/*  86 */     this.cells = ((Element[][])cells.clone());
/*  87 */     this.levelDims = null;
/*     */   }
/*     */ 
/*     */   public FormatRangeInfo(Dimension[] dims, int[] levels)
/*     */   {
/*  99 */     this.cells = null;
/* 100 */     this.levelDims = new LinkedHashMap();
/* 101 */     for (int i = 0; i < dims.length; ++i)
/* 102 */       this.levelDims.put(dims[i], Integer.valueOf(levels[i]));
/*     */   }
/*     */ 
/*     */   public FormatRangeInfo(Dimension[] dims, String description, boolean hasCoordinates)
/*     */   {
/* 117 */     if (hasCoordinates) {
/* 118 */       String[] coords = description.split(";");
/* 119 */       int length = coords.length;
/* 120 */       this.cells = new Element[length][];
/* 121 */       for (int i = 0; i < length; ++i) {
/* 122 */         this.cells[i] = restoreFrom(dims, coords[i]);
/*     */       }
/* 124 */       this.levelDims = null;
/*     */     } else {
/* 126 */       this.cells = null;
/* 127 */       this.levelDims = new LinkedHashMap();
/* 128 */       String[] levels = description.split(";");
/* 129 */       int length = levels.length;
/* 130 */       for (int i = 0; i < length; ++i)
/* 131 */         this.levelDims.put(dims[i], Integer.valueOf(Integer.parseInt(levels[i])));
/*     */     }
/*     */   }
/*     */ 
/*     */   private Element[] restoreFrom(Dimension[] dims, String desc)
/*     */   {
/* 144 */     String[] ids = desc.split(",");
/* 145 */     Element[] r = new Element[ids.length];
/* 146 */     int i = 0; for (int n = ids.length; i < n; ++i) {
/* 147 */       r[i] = dims[i].getElementById(ids[i]);
/*     */     }
/* 149 */     return r;
/*     */   }
/*     */ 
/*     */   public final FormatRangeInfo copy()
/*     */   {
/* 157 */     return new FormatRangeInfo(this);
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 164 */     int sum = 0;
/* 165 */     if (this.cells == null) {
/* 166 */       if (this.levelDims != null)
/* 167 */         for (Dimension d : this.levelDims.keySet()) {
/* 168 */           sum += d.hashCode();
/* 169 */           sum += ((Integer)this.levelDims.get(d)).intValue() * 17;
/*     */         }
/*     */     }
/*     */     else {
/* 173 */       sum += 17;
/* 174 */       for (Element[] coord : this.cells) {
/* 175 */         sum += coord.hashCode();
/*     */       }
/*     */     }
/* 178 */     return sum;
/*     */   }
/*     */ 
/*     */   private final boolean coordsEquals(Element[] p1, Element[] p2)
/*     */   {
/* 189 */     if (p1 == null) {
/* 190 */       return p2 == null;
/*     */     }
/* 192 */     if (p1.length != p2.length) {
/* 193 */       return false;
/*     */     }
/* 195 */     int i = 0; for (int n = p1.length; i < n; ++i) {
/* 196 */       if (!p1[i].equals(p2[i])) {
/* 197 */         return false;
/*     */       }
/*     */     }
/* 200 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object o)
/*     */   {
/* 207 */     if (o == null) {
/* 208 */       return false;
/*     */     }
/* 210 */     if (!(o instanceof FormatRangeInfo)) {
/* 211 */       return false;
/*     */     }
/* 213 */     FormatRangeInfo f = (FormatRangeInfo)o;
/*     */ 
/* 215 */     if ((this.cells == null) && (f.cells != null)) {
/* 216 */       return false;
/*     */     }
/* 218 */     if (this.cells == null) {
/* 219 */       if ((this.levelDims == null) && (f.levelDims != null))
/* 220 */         return false;
/* 221 */       if (this.levelDims == null) {
/* 222 */         return true;
/*     */       }
/* 224 */       for (Dimension d : this.levelDims.keySet()) {
/* 225 */         if (!f.levelDims.containsKey(d)) {
/* 226 */           return false;
/*     */         }
/* 228 */         if (!((Integer)this.levelDims.get(d)).equals(f.levelDims.get(d))) {
/* 229 */           return false;
/*     */         }
/*     */       }
/* 232 */       return true;
/*     */     }
/* 234 */     if (this.cells.length != f.cells.length) {
/* 235 */       return false;
/*     */     }
/* 237 */     for (int i = 0; i < this.cells.length; ++i) {
/* 238 */       if (!coordsEquals(this.cells[i], f.cells[i])) {
/* 239 */         return false;
/*     */       }
/*     */     }
/* 242 */     return true;
/*     */   }
/*     */ 
/*     */   private String getCoordinateString(Element[] e)
/*     */   {
/* 251 */     StringBuffer buffer = new StringBuffer();
/* 252 */     int i = 0; for (int n = e.length; i < n; ++i) {
/* 253 */       buffer.append(e[i].getId());
/* 254 */       if (i < n - 1) {
/* 255 */         buffer.append(",");
/*     */       }
/*     */     }
/* 258 */     return buffer.toString();
/*     */   }
/*     */ 
/*     */   public Element[][] getCells()
/*     */   {
/* 269 */     return this.cells;
/*     */   }
/*     */ 
/*     */   public int getLevel(Dimension d)
/*     */   {
/* 281 */     if ((this.levelDims == null) || (!this.levelDims.containsKey(d))) {
/* 282 */       return -1;
/*     */     }
/* 284 */     return ((Integer)this.levelDims.get(d)).intValue();
/*     */   }
/*     */ 
/*     */   public Dimension[] getDimensions()
/*     */   {
/* 294 */     if (this.levelDims == null) {
/* 295 */       return new Dimension[0];
/*     */     }
/* 297 */     return (Dimension[])this.levelDims.keySet().toArray(new Dimension[0]);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 304 */     StringBuffer buffer = new StringBuffer();
/* 305 */     if (this.cells != null) {
/* 306 */       for (Element[] coord : this.cells) {
/* 307 */         buffer.append(getCoordinateString(coord));
/* 308 */         buffer.append(";");
/*     */       }
/*     */     }
/* 311 */     else if (this.levelDims != null) {
/* 312 */       for (Dimension d : this.levelDims.keySet()) {
/* 313 */         buffer.append(this.levelDims.get(d));
/* 314 */         buffer.append(";");
/*     */       }
/*     */     }
/*     */ 
/* 318 */     if (buffer.length() > 0) {
/* 319 */       return buffer.substring(0, buffer.length() - 1).toString();
/*     */     }
/* 321 */     return "";
/*     */   }
/*     */ }

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi.jar
 * Qualified Name:     org.palo.viewapi.uimodels.formats.FormatRangeInfo
 * JD-Core Version:    0.5.4
 */