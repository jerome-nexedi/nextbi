/*     */ package org.palo.api.subsets.filter.settings;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Set;
/*     */ import org.palo.api.subsets.Subset2;
/*     */ 
/*     */ public class AttributeConstraintsMatrix
/*     */ {
/*     */   private Subset2 subset;
/*     */   private int rowsCount;
/*     */   private final HashMap<String, ArrayList<AttributeConstraint>> attrId2constraint;
/*     */ 
/*     */   public AttributeConstraintsMatrix()
/*     */   {
/*  65 */     this.attrId2constraint = 
/*  66 */       new HashMap();
/*     */   }
/*     */ 
/*     */   public final String[] getAttributeIDs()
/*     */   {
/*  71 */     return (String[])this.attrId2constraint.keySet().toArray(new String[0]);
/*     */   }
/*     */ 
/*     */   public final void addFilterConstraint(AttributeConstraint constraint)
/*     */   {
/*  80 */     ArrayList constraints = 
/*  81 */       getConstraints(constraint.getAttributeId());
/*  82 */     constraints.add(constraint);
/*  83 */     checkRowsCount();
/*  84 */     markDirty();
/*     */   }
/*     */ 
/*     */   public final void removeFilterConstraint(AttributeConstraint constraint)
/*     */   {
/*  93 */     ArrayList constraints = 
/*  94 */       getConstraints(constraint.getAttributeId());
/*  95 */     constraints.remove(constraint);
/*  96 */     checkRowsCount();
/*  97 */     markDirty();
/*     */   }
/*     */ 
/*     */   public final void removeAllFilterConstraints(String attrId)
/*     */   {
/* 107 */     ArrayList constraints = getConstraints(attrId);
/* 108 */     constraints.clear();
/* 109 */     checkRowsCount();
/* 110 */     markDirty();
/*     */   }
/*     */ 
/*     */   public final AttributeConstraint[] getColumn(String attrId)
/*     */   {
/* 121 */     ArrayList constraints = getConstraints(attrId);
/* 122 */     return (AttributeConstraint[])constraints.toArray(new AttributeConstraint[0]);
/*     */   }
/*     */ 
/*     */   public final AttributeConstraint[] getRow(int index)
/*     */   {
/* 131 */     ArrayList row = 
/* 132 */       new ArrayList();
/* 133 */     for (String attrId : this.attrId2constraint.keySet()) {
/* 134 */       ArrayList constraints = 
/* 135 */         (ArrayList)this.attrId2constraint.get(attrId);
/* 136 */       if (constraints.size() > index)
/* 137 */         row.add((AttributeConstraint)constraints.get(index));
/*     */     }
/* 139 */     return (AttributeConstraint[])row.toArray(new AttributeConstraint[row.size()]);
/*     */   }
/*     */ 
/*     */   public final AttributeConstraint[][] getRows()
/*     */   {
/* 147 */     AttributeConstraint[][] rows = new AttributeConstraint[this.rowsCount][];
/* 148 */     for (int i = 0; i < this.rowsCount; ++i)
/* 149 */       rows[i] = getRow(i);
/* 150 */     return rows;
/*     */   }
/*     */ 
/*     */   public final int getRowsCount()
/*     */   {
/* 158 */     return this.rowsCount;
/*     */   }
/*     */ 
/*     */   public final void clear()
/*     */   {
/* 165 */     this.rowsCount = 0;
/* 166 */     this.attrId2constraint.clear();
/* 167 */     markDirty();
/*     */   }
/*     */ 
/*     */   final AttributeConstraint[] getConstraints() {
/* 171 */     ArrayList constraints = 
/* 172 */       new ArrayList();
/* 173 */     for (ArrayList list : this.attrId2constraint.values())
/* 174 */       constraints.addAll(list);
/* 175 */     return (AttributeConstraint[])constraints.toArray(new AttributeConstraint[constraints.size()]);
/*     */   }
/*     */ 
/*     */   final boolean hasConstraints() {
/* 179 */     return !this.attrId2constraint.isEmpty();
/*     */   }
/*     */ 
/*     */   public final void bind(Subset2 subset)
/*     */   {
/* 189 */     this.subset = subset;
/* 190 */     markDirty();
/*     */   }
/*     */ 
/*     */   public final void unbind()
/*     */   {
/* 197 */     this.subset = null;
/*     */   }
/*     */ 
/*     */   private final void markDirty() {
/* 201 */     if (this.subset != null)
/* 202 */       this.subset.modified();
/*     */   }
/*     */ 
/*     */   private final void checkRowsCount() {
/* 206 */     this.rowsCount = 0;
/* 207 */     for (String attrId : this.attrId2constraint.keySet()) {
/* 208 */       ArrayList constraints = 
/* 209 */         (ArrayList)this.attrId2constraint.get(attrId);
/* 210 */       int size = constraints.size();
/* 211 */       if (this.rowsCount < size)
/* 212 */         this.rowsCount = size;
/*     */     }
/*     */   }
/*     */ 
/*     */   private final ArrayList<AttributeConstraint> getConstraints(String attrId) {
/* 217 */     ArrayList constraints = 
/* 218 */       (ArrayList)this.attrId2constraint.get(attrId);
/* 219 */     if (constraints == null) {
/* 220 */       constraints = new ArrayList();
/* 221 */       this.attrId2constraint.put(attrId, constraints);
/*     */     }
/* 223 */     return constraints;
/*     */   }
/*     */ }

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name:     org.palo.api.subsets.filter.settings.AttributeConstraintsMatrix
 * JD-Core Version:    0.5.4
 */