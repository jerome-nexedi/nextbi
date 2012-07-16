/*     */ package com.tensegrity.palojava.impl;
/*     */ 
/*     */ import com.tensegrity.palojava.PropertyInfo;
/*     */ import java.util.LinkedHashSet;
/*     */ 
/*     */ public class PropertyInfoImpl
/*     */   implements PropertyInfo
/*     */ {
/*     */   public static final int TYPE_NUMERIC = 1;
/*     */   public static final int TYPE_STRING = 2;
/*     */   public static final int TYPE_BOOLEAN = 3;
/*     */   private final LinkedHashSet<PropertyInfo> children;
/*     */   private String id;
/*     */   private String value;
/*     */   private PropertyInfo parent;
/*     */   private int type;
/*     */   private boolean readOnly;
/*     */ 
/*     */   public PropertyInfoImpl(String id, String value, PropertyInfo parent, int type, boolean readOnly)
/*     */   {
/*  61 */     this.children = new LinkedHashSet();
/*  62 */     this.id = id;
/*  63 */     this.value = value;
/*  64 */     this.parent = parent;
/*  65 */     this.type = type;
/*  66 */     this.readOnly = readOnly;
/*     */   }
/*     */ 
/*     */   public void addChild(PropertyInfo child)
/*     */   {
/*  73 */     this.children.add(child);
/*     */   }
/*     */ 
/*     */   public void clearChildren()
/*     */   {
/*  80 */     this.children.clear();
/*     */   }
/*     */ 
/*     */   public int getChildCount()
/*     */   {
/*  87 */     return this.children.size();
/*     */   }
/*     */ 
/*     */   public PropertyInfo[] getChildren()
/*     */   {
/*  94 */     return (PropertyInfo[])this.children.toArray(new PropertyInfo[0]);
/*     */   }
/*     */ 
/*     */   public PropertyInfo getParent()
/*     */   {
/* 101 */     return this.parent;
/*     */   }
/*     */ 
/*     */   public String getValue()
/*     */   {
/* 108 */     return this.value;
/*     */   }
/*     */ 
/*     */   public void removeChild(PropertyInfo child)
/*     */   {
/* 115 */     if (!this.readOnly)
/* 116 */       this.children.remove(child);
/*     */   }
/*     */ 
/*     */   public void setValue(String newValue)
/*     */   {
/* 124 */     if (!this.readOnly)
/* 125 */       this.value = newValue;
/*     */   }
/*     */ 
/*     */   public String getId()
/*     */   {
/* 133 */     return this.id;
/*     */   }
/*     */ 
/*     */   public int getType()
/*     */   {
/* 140 */     return this.type;
/*     */   }
/*     */ 
/*     */   public boolean isReadOnly() {
/* 144 */     return this.readOnly;
/*     */   }
/*     */ 
/*     */   public PropertyInfo getChild(String id) {
/* 148 */     for (PropertyInfo info : this.children) {
/* 149 */       if (info.getId().equals(id)) {
/* 150 */         return info;
/*     */       }
/*     */     }
/* 153 */     return null;
/*     */   }
/*     */ 
/*     */   public boolean canBeModified() {
/* 157 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean canCreateChildren() {
/* 161 */     return true;
/*     */   }
/*     */ }

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.paloapi\lib\palo.jar
 * Qualified Name:     com.tensegrity.palojava.impl.PropertyInfoImpl
 * JD-Core Version:    0.5.4
 */