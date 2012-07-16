/*     */ package org.palo.api.impl.subsets;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.palo.api.Attribute;
/*     */ import org.palo.api.Element;
/*     */ import org.palo.api.SubsetState;
/*     */ 
/*     */ class SubsetStateImpl
/*     */   implements SubsetState
/*     */ {
/*     */   private final String id;
/*     */   private String name;
/*     */   private String expression;
/*     */   private Attribute searchAttribute;
/*  64 */   private final Set visibleElements = new HashSet();
/*  65 */   private final Map elPaths = new HashMap();
/*  66 */   private final Map positions = new HashMap();
/*     */ 
/*     */   SubsetStateImpl(String id)
/*     */   {
/*  73 */     this.id = id;
/*     */   }
/*     */ 
/*     */   public final synchronized void setExpression(String expression) {
/*  77 */     this.expression = expression;
/*     */   }
/*     */ 
/*     */   public final void addVisibleElment(Element element) {
/*  81 */     addVisibleElement(element, -1);
/*     */   }
/*     */ 
/*     */   public final void addVisibleElement(Element element, int position) {
/*  85 */     addPosition(element, Integer.toString(position));
/*  86 */     this.visibleElements.add(element);
/*     */   }
/*     */ 
/*     */   public final void removeVisibleElement(Element element) {
/*  90 */     this.visibleElements.remove(element);
/*  91 */     this.elPaths.remove(element);
/*  92 */     this.positions.remove(element);
/*     */   }
/*     */ 
/*     */   public final void removeAllVisibleElements() {
/*  96 */     this.visibleElements.clear();
/*  97 */     this.elPaths.clear();
/*  98 */     this.positions.clear();
/*     */   }
/*     */ 
/*     */   public final synchronized String getExpression() {
/* 102 */     return this.expression;
/*     */   }
/*     */ 
/*     */   public final String getId() {
/* 106 */     return this.id;
/*     */   }
/*     */ 
/*     */   public final synchronized void setName(String name) {
/* 110 */     this.name = name;
/*     */   }
/*     */ 
/*     */   public final synchronized String getName() {
/* 114 */     return this.name;
/*     */   }
/*     */ 
/*     */   public final Element[] getVisibleElements() {
/* 118 */     return (Element[])this.visibleElements.toArray(
/* 119 */       new Element[this.visibleElements.size()]);
/*     */   }
/*     */ 
/*     */   public final synchronized Attribute getSearchAttribute() {
/* 123 */     return this.searchAttribute;
/*     */   }
/*     */ 
/*     */   public final synchronized void setSearchAttribute(Attribute searchAttribute) {
/* 127 */     this.searchAttribute = searchAttribute;
/*     */   }
/*     */ 
/*     */   public final String[] getPaths(Element element) {
/* 131 */     Set paths = (Set)this.elPaths.get(element);
/* 132 */     if (paths == null)
/* 133 */       return new String[0];
/* 134 */     return (String[])paths.toArray(new String[paths.size()]);
/*     */   }
/*     */ 
/*     */   public final void addPath(Element element, String path) {
/* 138 */     if (!this.elPaths.containsKey(element))
/* 139 */       this.elPaths.put(element, new HashSet());
/* 140 */     Set paths = (Set)this.elPaths.get(element);
/* 141 */     paths.add(path);
/*     */   }
/*     */ 
/*     */   public final void removePath(Element element, String path) {
/* 145 */     if (!this.elPaths.containsKey(element)) {
/* 146 */       return;
/*     */     }
/* 148 */     Set paths = (Set)this.elPaths.get(element);
/* 149 */     paths.remove(path);
/*     */   }
/*     */ 
/*     */   public final boolean containsPath(Element element, String path) {
/* 153 */     Set paths = (Set)this.elPaths.get(element);
/* 154 */     if (paths == null)
/* 155 */       return false;
/* 156 */     return paths.contains(path);
/*     */   }
/*     */ 
/*     */   public final int[] getPositions(Element element) {
/* 160 */     String[] positions = getPositionsInternal(element);
/* 161 */     int[] _pos = new int[positions.length];
/* 162 */     for (int i = 0; i < positions.length; ++i)
/* 163 */       _pos[i] = Integer.parseInt(positions[i]);
/* 164 */     return _pos;
/*     */   }
/*     */ 
/*     */   public final boolean isVisible(Element element) {
/* 168 */     return this.visibleElements.contains(element);
/*     */   }
/*     */ 
/*     */   final void setPaths(Element element, String paths) {
/* 172 */     if ((paths == null) || (paths.equals("")))
/* 173 */       return;
/* 174 */     String[] _paths = paths.split(":");
/* 175 */     for (int i = 0; i < _paths.length; ++i)
/* 176 */       addPath(element, _paths[i]);
/*     */   }
/*     */ 
/*     */   final String getPathsAsString(Element element)
/*     */   {
/* 181 */     if (!this.elPaths.containsKey(element))
/* 182 */       return null;
/* 183 */     StringBuffer paths = new StringBuffer();
/* 184 */     String[] allPaths = getPaths(element);
/* 185 */     int lastPath = allPaths.length - 1;
/* 186 */     for (int i = 0; i < allPaths.length; ++i) {
/* 187 */       paths.append(allPaths[i]);
/* 188 */       if (i < lastPath)
/* 189 */         paths.append(":");
/*     */     }
/* 191 */     return paths.toString();
/*     */   }
/*     */ 
/*     */   final String getPositionsAsString(Element element) {
/* 195 */     String[] positions = getPositionsInternal(element);
/* 196 */     StringBuffer posStr = new StringBuffer();
/* 197 */     int lastPos = positions.length - 1;
/* 198 */     for (int i = 0; i < positions.length; ++i) {
/* 199 */       posStr.append(positions[i]);
/* 200 */       if (i < lastPos)
/* 201 */         posStr.append(",");
/*     */     }
/* 203 */     return posStr.toString();
/*     */   }
/*     */ 
/*     */   final void setPosition(Element element, String posStr) {
/* 207 */     if ((posStr == null) || (posStr.equals("")))
/* 208 */       return;
/* 209 */     String[] allPositions = posStr.split(",");
/* 210 */     for (int i = 0; i < allPositions.length; ++i)
/* 211 */       addPosition(element, allPositions[i]);
/*     */   }
/*     */ 
/*     */   final String[] getPositionsInternal(Element element)
/*     */   {
/* 216 */     List allPositions = (List)this.positions.get(element);
/* 217 */     if (allPositions != null) {
/* 218 */       return (String[])allPositions.toArray(new String[allPositions.size()]);
/*     */     }
/* 220 */     return new String[0];
/*     */   }
/*     */ 
/*     */   private final void addPosition(Element element, String position) {
/* 224 */     if (position.equals("-1"))
/* 225 */       return;
/* 226 */     List allPositions = (List)this.positions.get(element);
/* 227 */     if (allPositions == null) {
/* 228 */       allPositions = new ArrayList();
/* 229 */       this.positions.put(element, allPositions);
/*     */     }
/* 231 */     if (!allPositions.contains(position))
/* 232 */       allPositions.add(position);
/*     */   }
/*     */ }

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name:     org.palo.api.impl.subsets.SubsetStateImpl
 * JD-Core Version:    0.5.4
 */