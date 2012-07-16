/*     */ package org.palo.viewapi.internal.cubeview;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import org.palo.api.Hierarchy;
/*     */ import org.palo.api.utils.ElementPath;
/*     */ import org.palo.viewapi.Axis;
/*     */ import org.palo.viewapi.AxisHierarchy;
/*     */ import org.palo.viewapi.Property;
/*     */ import org.palo.viewapi.View;
/*     */ 
/*     */ class AxisImpl
/*     */   implements Axis
/*     */ {
/*     */   private View view;
/*     */   private String name;
/*     */   private final String id;
/*  64 */   private final Set<ElementPath> visiblePaths = new HashSet();
/*  65 */   private final List<ElementPath> expandedPaths = new ArrayList();
/*  66 */   private final HashMap<String, Property<?>> properties = new HashMap();
/*  67 */   private final LinkedHashMap<Hierarchy, AxisHierarchy> hierarchies = new LinkedHashMap();
/*     */ 
/*     */   AxisImpl(String id, String name, View view)
/*     */   {
/*  71 */     this.id = id;
/*  72 */     this.name = name;
/*  73 */     this.view = view;
/*     */   }
/*     */ 
/*     */   private AxisImpl(AxisImpl axis) {
/*  77 */     this.id = axis.id;
/*  78 */     this.name = axis.name;
/*  79 */     this.view = axis.view;
/*  80 */     this.hierarchies.putAll(axis.hierarchies);
/*  81 */     for (ElementPath path : axis.visiblePaths)
/*  82 */       this.visiblePaths.add(path.copy());
/*  83 */     for (ElementPath path : axis.expandedPaths) {
/*  84 */       this.expandedPaths.add(path.copy());
/*     */     }
/*  86 */     this.properties.putAll(axis.properties);
/*     */   }
/*     */ 
/*     */   public final String getId() {
/*  90 */     return this.id;
/*     */   }
/*     */ 
/*     */   public final String getName() {
/*  94 */     return this.name;
/*     */   }
/*     */ 
/*     */   public final void setName(String name) {
/*  98 */     this.name = name;
/*     */   }
/*     */ 
/*     */   public final View getView() {
/* 102 */     return this.view;
/*     */   }
/*     */   final void setView(View view) {
/* 105 */     this.view = view;
/*     */   }
/*     */ 
/*     */   public final Axis copy() {
/* 109 */     return new AxisImpl(this);
/*     */   }
/*     */ 
/*     */   public final AxisHierarchy add(Hierarchy hierarchy) {
/* 113 */     AxisHierarchy axisHierarchy = null;
/* 114 */     if (hierarchy != null) {
/* 115 */       axisHierarchy = new AxisHierarchyImpl(hierarchy, this);
/* 116 */       this.hierarchies.put(axisHierarchy.getHierarchy(), axisHierarchy);
/*     */     }
/* 118 */     return axisHierarchy;
/*     */   }
/*     */   public final void add(AxisHierarchy axisHierarchy) {
/* 121 */     if ((axisHierarchy == null) || (axisHierarchy.getHierarchy() == null))
/* 122 */       return;
/* 123 */     axisHierarchy.setAxis(this);
/* 124 */     this.hierarchies.put(axisHierarchy.getHierarchy(), axisHierarchy);
/*     */   }
/*     */ 
/*     */   public final void remove(AxisHierarchy axisHierarchy) {
/* 128 */     axisHierarchy.setAxis(null);
/* 129 */     this.hierarchies.remove(axisHierarchy.getHierarchy());
/*     */   }
/*     */   public final AxisHierarchy getAxisHierarchy(Hierarchy hierarchy) {
/* 132 */     return (AxisHierarchy)this.hierarchies.get(hierarchy);
/*     */   }
/*     */ 
/*     */   public final AxisHierarchy getAxisHierarchy(String id) {
/* 136 */     for (AxisHierarchy hierarchy : this.hierarchies.values()) {
/* 137 */       if (hierarchy.getHierarchy().getId().equals(id))
/* 138 */         return hierarchy;
/*     */     }
/* 140 */     return null;
/*     */   }
/*     */ 
/*     */   public final AxisHierarchy[] getAxisHierarchies() {
/* 144 */     return (AxisHierarchy[])this.hierarchies.values().toArray(new AxisHierarchy[0]);
/*     */   }
/*     */ 
/*     */   public final Hierarchy[] getHierarchies() {
/* 148 */     return (Hierarchy[])this.hierarchies.keySet().toArray(new Hierarchy[0]);
/*     */   }
/*     */ 
/*     */   public final AxisHierarchy remove(Hierarchy hierarchy) {
/* 152 */     AxisHierarchy axisHierarchy = (AxisHierarchy)this.hierarchies.remove(hierarchy);
/* 153 */     if (axisHierarchy != null)
/* 154 */       axisHierarchy.setAxis(null);
/* 155 */     return axisHierarchy;
/*     */   }
/*     */ 
/*     */   public final void removeAll() {
/* 159 */     for (AxisHierarchy axisHierarchy : this.hierarchies.values())
/* 160 */       axisHierarchy.setAxis(null);
/* 161 */     this.hierarchies.clear();
/*     */ 
/* 163 */     this.visiblePaths.clear();
/* 164 */     this.expandedPaths.clear();
/*     */   }
/*     */ 
/*     */   public final void addExpanded(ElementPath path)
/*     */   {
/* 169 */     this.expandedPaths.add(path);
/*     */   }
/*     */ 
/*     */   public final void addExpanded(ElementPath[] paths) {
/* 173 */     this.expandedPaths.addAll(Arrays.asList(paths));
/*     */   }
/*     */ 
/*     */   public final ElementPath[] getExpandedPaths() {
/* 177 */     return (ElementPath[])this.expandedPaths.toArray(new ElementPath[0]);
/*     */   }
/*     */ 
/*     */   public final void removeExpanded(ElementPath path) {
/* 181 */     this.expandedPaths.remove(path);
/*     */   }
/*     */ 
/*     */   public final void removeAllExpandedPaths() {
/* 185 */     this.expandedPaths.clear();
/*     */   }
/*     */ 
/*     */   public final void addVisible(ElementPath path) {
/* 189 */     this.visiblePaths.add(path);
/*     */   }
/*     */ 
/*     */   public final ElementPath[] getVisiblePaths()
/*     */   {
/* 194 */     return (ElementPath[])this.visiblePaths.toArray(new ElementPath[0]);
/*     */   }
/*     */ 
/*     */   public final boolean isVisible(ElementPath path) {
/* 198 */     return this.visiblePaths.contains(path);
/*     */   }
/*     */ 
/*     */   public final void removeVisible(ElementPath path) {
/* 202 */     this.visiblePaths.remove(path);
/*     */   }
/*     */ 
/*     */   public final void removeAllVisiblePaths() {
/* 206 */     this.visiblePaths.clear();
/*     */   }
/*     */ 
/*     */   public final void addProperty(Property<?> property) {
/* 210 */     this.properties.put(property.getId(), property);
/*     */   }
/*     */ 
/*     */   public final Property<?> getProperty(String id) {
/* 214 */     return (Property)this.properties.get(id);
/*     */   }
/*     */ 
/*     */   public final Property<?>[] getProperties() {
/* 218 */     return (Property[])this.properties.values().toArray(new Property[0]);
/*     */   }
/*     */ 
/*     */   public final void removeProperty(Property<?> property) {
/* 222 */     this.properties.remove(property.getId());
/*     */   }
/*     */ 
/*     */   public final void removeAllProperties() {
/* 226 */     this.properties.clear();
/*     */   }
/*     */ 
/*     */   public final boolean equals(Object obj) {
/* 230 */     if (obj instanceof Axis) {
/* 231 */       return this.id.equals(((Axis)obj).getId());
/*     */     }
/* 233 */     return false;
/*     */   }
/*     */ 
/*     */   public final int hashCode() {
/* 237 */     int hc = 23;
/* 238 */     hc += 37 * this.id.hashCode();
/* 239 */     return hc;
/*     */   }
/*     */ }

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi.jar
 * Qualified Name:     org.palo.viewapi.internal.cubeview.AxisImpl
 * JD-Core Version:    0.5.4
 */