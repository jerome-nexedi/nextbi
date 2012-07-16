/*     */ package org.palo.api.impl.views;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.palo.api.Axis;
/*     */ import org.palo.api.CubeView;
/*     */ import org.palo.api.Dimension;
/*     */ import org.palo.api.Element;
/*     */ import org.palo.api.Hierarchy;
/*     */ import org.palo.api.Subset;
/*     */ import org.palo.api.impl.utils.ArrayListInt;
/*     */ import org.palo.api.subsets.Subset2;
/*     */ import org.palo.api.utils.ElementPath;
/*     */ 
/*     */ class AxisImpl
/*     */   implements Axis
/*     */ {
/*  64 */   private final Set<Hierarchy> hiers = new LinkedHashSet();
/*     */ 
/*  66 */   private final Map<Hierarchy, Map<String, Element[]>> expanded = new HashMap();
/*  67 */   private final Set<ElementPath> expandedPaths = new HashSet();
/*  68 */   private final Map<Hierarchy, Map<String, Element[]>> hidden = new HashMap();
/*  69 */   private final Map<Hierarchy, Set<ElementPath>> visiblePaths = new HashMap();
/*  70 */   private final Map<String, String> properties = new HashMap();
/*     */ 
/*  72 */   private final Map<Dimension, Subset> activeSubs = new HashMap();
/*     */ 
/*  74 */   private final Map<Dimension, Subset2> activeSubs2 = new HashMap();
/*     */ 
/*  76 */   private final Map<Hierarchy, Element> selectedElements = new HashMap();
/*     */ 
/*  78 */   private final Map<Hierarchy, Map<String, ArrayListInt>> repetitions = new HashMap();
/*  79 */   private final Map<Dimension, Hierarchy> dimHier = new HashMap();
/*     */   private final String id;
/*     */   private final CubeView view;
/*     */   private String name;
/*  85 */   private String[] data = new String[0];
/*     */ 
/*     */   AxisImpl(String id, String name, CubeView view)
/*     */   {
/*  89 */     this.id = id;
/*  90 */     this.name = name;
/*  91 */     this.view = view;
/*     */   }
/*     */ 
/*     */   public final void add(Dimension dimension) {
/*  95 */     this.hiers.add(dimension.getDefaultHierarchy());
/*  96 */     this.dimHier.put(dimension, dimension.getDefaultHierarchy());
/*     */   }
/*     */ 
/*     */   public final void add(Hierarchy hierarchy) {
/* 100 */     this.hiers.add(hierarchy);
/* 101 */     this.dimHier.put(hierarchy.getDimension(), hierarchy);
/*     */   }
/*     */ 
/*     */   public final void addExpanded(Dimension dimension, Element[] path, int repetition) {
/* 105 */     addExpanded(dimension.getDefaultHierarchy(), path, repetition);
/*     */   }
/*     */ 
/*     */   public final void addExpanded(Hierarchy hierarchy, Element[] path, int repetition) {
/* 109 */     Map paths = (Map)this.expanded.get(hierarchy);
/* 110 */     if (paths == null) {
/* 111 */       paths = new HashMap(path.length);
/* 112 */       this.expanded.put(hierarchy, paths);
/*     */     }
/* 114 */     String key = pathToString(path);
/* 115 */     paths.put(key, path);
/*     */ 
/* 117 */     Map reps = (Map)this.repetitions.get(hierarchy);
/* 118 */     if (reps == null) {
/* 119 */       reps = new HashMap(path.length);
/* 120 */       this.repetitions.put(hierarchy, reps);
/*     */     }
/*     */ 
/* 123 */     ArrayListInt repList = (ArrayListInt)reps.get(key);
/* 124 */     if (repList == null) {
/* 125 */       repList = new ArrayListInt();
/* 126 */       reps.put(key, repList);
/*     */     }
/* 128 */     repList.add(repetition);
/*     */   }
/*     */ 
/*     */   public final void addHidden(Dimension dimension, Element[] path) {
/* 132 */     addHidden(dimension.getDefaultHierarchy(), path);
/*     */   }
/*     */ 
/*     */   public final void addHidden(Hierarchy hierarchy, Element[] path) {
/* 136 */     Map paths = (Map)this.hidden.get(hierarchy);
/* 137 */     if (paths == null) {
/* 138 */       paths = new HashMap(path.length);
/* 139 */       this.hidden.put(hierarchy, paths);
/*     */     }
/* 141 */     String key = pathToString(path);
/* 142 */     paths.put(key, path);
/* 143 */     this.dimHier.put(hierarchy.getDimension(), hierarchy);
/*     */   }
/*     */ 
/*     */   public final void addVisible(ElementPath path) {
/* 147 */     Hierarchy[] hierarchies = path.getHierarchies();
/*     */ 
/* 149 */     Set paths = (Set)this.visiblePaths.get(hierarchies[0]);
/* 150 */     if (paths == null) {
/* 151 */       paths = new HashSet();
/* 152 */       this.visiblePaths.put(hierarchies[0], paths);
/*     */     }
/* 154 */     paths.add(path);
/*     */   }
/*     */ 
/*     */   public final boolean isVisible(ElementPath path) {
/* 158 */     Hierarchy[] hierarchies = path.getHierarchies();
/* 159 */     Set paths = (Set)this.visiblePaths.get(hierarchies[0]);
/* 160 */     if (paths != null) {
/* 161 */       return paths.contains(path);
/*     */     }
/* 163 */     return false;
/*     */   }
/*     */ 
/*     */   public final void addProperty(String id, String value) {
/* 167 */     this.properties.put(id, value);
/*     */   }
/*     */ 
/*     */   public final Subset getActiveSubset(Dimension dimension) {
/* 171 */     return (Subset)this.activeSubs.get(dimension);
/*     */   }
/*     */ 
/*     */   public final Subset2 getActiveSubset2(Dimension dimension) {
/* 175 */     return (Subset2)this.activeSubs2.get(dimension);
/*     */   }
/*     */ 
/*     */   public final Dimension[] getDimensions() {
/* 179 */     Hierarchy[] hierarchies = getHierarchies();
/* 180 */     Dimension[] result = new Dimension[hierarchies.length];
/* 181 */     int i = 0; for (int n = hierarchies.length; i < n; ++i) {
/* 182 */       result[i] = hierarchies[i].getDimension();
/*     */     }
/* 184 */     return result;
/*     */   }
/*     */ 
/*     */   public final Hierarchy getHierarchy(Dimension dim) {
/* 188 */     if (!this.dimHier.containsKey(dim)) {
/* 189 */       return dim.getDefaultHierarchy();
/*     */     }
/* 191 */     return (Hierarchy)this.dimHier.get(dim);
/*     */   }
/*     */ 
/*     */   public final Hierarchy[] getHierarchies() {
/* 195 */     return (Hierarchy[])this.hiers.toArray(new Hierarchy[this.hiers.size()]);
/*     */   }
/*     */ 
/*     */   public final Element[][] getExpanded(Hierarchy hierarchy) {
/* 199 */     Map paths = (Map)this.expanded.get(hierarchy);
/* 200 */     if (paths == null)
/* 201 */       return new Element[0][0];
/* 202 */     Element[][] expPaths = new Element[paths.size()][];
/* 203 */     int i = 0;
/* 204 */     for (Iterator it = paths.values().iterator(); it.hasNext(); ) {
/* 205 */       expPaths[i] = ((Element[])it.next());
/* 206 */       ++i;
/*     */     }
/* 208 */     return expPaths;
/*     */   }
/*     */ 
/*     */   public final Element[][] getExpanded(Dimension dimension) {
/* 212 */     return getExpanded(dimension.getDefaultHierarchy());
/*     */   }
/*     */ 
/*     */   public final int[] getRepetitionsForExpanded(Hierarchy hierarchy, Element[] path)
/*     */   {
/* 217 */     Map reps = (Map)this.repetitions.get(hierarchy);
/* 218 */     if (reps == null) {
/* 219 */       return new int[0];
/*     */     }
/* 221 */     String key = pathToString(path);
/* 222 */     ArrayListInt repList = (ArrayListInt)reps.get(key);
/* 223 */     if (repList == null) {
/* 224 */       return new int[0];
/*     */     }
/* 226 */     return repList.toArray();
/*     */   }
/*     */ 
/*     */   public final int[] getRepetitionsForExpanded(Dimension dimension, Element[] path) {
/* 230 */     return getRepetitionsForExpanded(dimension.getDefaultHierarchy(), path);
/*     */   }
/*     */ 
/*     */   public final Element[][] getHidden(Hierarchy hierarchy) {
/* 234 */     Map paths = (Map)this.hidden.get(hierarchy);
/* 235 */     if (paths == null)
/* 236 */       return new Element[0][0];
/* 237 */     Element[][] hiddenPaths = new Element[paths.size()][];
/* 238 */     int i = 0;
/* 239 */     for (Iterator it = paths.values().iterator(); it.hasNext(); ) {
/* 240 */       hiddenPaths[i] = ((Element[])it.next());
/* 241 */       ++i;
/*     */     }
/* 243 */     return hiddenPaths;
/*     */   }
/*     */ 
/*     */   public final Element[][] getHidden(Dimension dimension) {
/* 247 */     return getHidden(dimension.getDefaultHierarchy());
/*     */   }
/*     */ 
/*     */   public final ElementPath[] getVisiblePaths(Hierarchy hierarchy) {
/* 251 */     Set paths = (Set)this.visiblePaths.get(hierarchy);
/* 252 */     if (paths == null)
/* 253 */       return new ElementPath[0];
/* 254 */     return (ElementPath[])paths.toArray(new ElementPath[paths.size()]);
/*     */   }
/*     */ 
/*     */   public final ElementPath[] getVisiblePaths(Dimension dimension) {
/* 258 */     return getVisiblePaths(dimension.getDefaultHierarchy());
/*     */   }
/*     */ 
/*     */   public final String[] getProperties()
/*     */   {
/* 263 */     return (String[])this.properties.keySet().toArray(new String[this.properties.size()]);
/*     */   }
/*     */ 
/*     */   public final String getPropertyValue(String id) {
/* 267 */     return (String)this.properties.get(id);
/*     */   }
/*     */ 
/*     */   public final Element getSelectedElement(Dimension dimension) {
/* 271 */     return getSelectedElement(dimension.getDefaultHierarchy());
/*     */   }
/*     */ 
/*     */   public final Element getSelectedElement(Hierarchy hierarchy) {
/* 275 */     return (Element)this.selectedElements.get(hierarchy);
/*     */   }
/*     */ 
/*     */   public final void remove(Dimension dimension) {
/* 279 */     remove(dimension.getDefaultHierarchy());
/*     */   }
/*     */ 
/*     */   public final void remove(Hierarchy hier) {
/* 283 */     this.hiers.remove(hier);
/* 284 */     this.hidden.remove(hier);
/* 285 */     this.expanded.remove(hier);
/* 286 */     this.repetitions.remove(hier);
/* 287 */     this.selectedElements.remove(hier);
/* 288 */     this.expandedPaths.clear();
/* 289 */     this.visiblePaths.remove(hier);
/*     */   }
/*     */ 
/*     */   public final void removeExpanded(Hierarchy hierarchy, Element[] path, int repetition) {
/* 293 */     Map paths = (Map)this.expanded.get(hierarchy);
/* 294 */     if (paths == null)
/* 295 */       return;
/* 296 */     String key = pathToString(path);
/* 297 */     paths.remove(key);
/*     */ 
/* 299 */     Map reps = (Map)this.repetitions.get(hierarchy);
/* 300 */     if (reps == null) {
/* 301 */       return;
/*     */     }
/* 303 */     ArrayListInt repList = (ArrayListInt)reps.get(key);
/* 304 */     if (repList == null)
/* 305 */       return;
/* 306 */     repList.remove(repetition);
/*     */   }
/*     */ 
/*     */   public final void removeExpanded(Dimension dimension, Element[] path, int repetition) {
/* 310 */     removeExpanded(dimension.getDefaultHierarchy(), path, repetition);
/*     */   }
/*     */ 
/*     */   public final void removeHidden(Hierarchy hierarchy, Element[] path) {
/* 314 */     Map paths = (Map)this.hidden.get(hierarchy);
/* 315 */     if (paths == null)
/* 316 */       return;
/* 317 */     String key = pathToString(path);
/* 318 */     paths.remove(key);
/*     */   }
/*     */ 
/*     */   public final void removeHidden(Dimension dimension, Element[] path) {
/* 322 */     removeHidden(dimension.getDefaultHierarchy(), path);
/*     */   }
/*     */ 
/*     */   public final void removeVisible(ElementPath path) {
/* 326 */     Hierarchy[] hierarchies = path.getHierarchies();
/*     */ 
/* 328 */     Set paths = (Set)this.visiblePaths.get(hierarchies[0]);
/* 329 */     if (paths != null)
/* 330 */       paths.remove(path);
/*     */   }
/*     */ 
/*     */   public final void removeProperty(String id) {
/* 334 */     this.properties.remove(id);
/*     */   }
/*     */ 
/*     */   public final void setActiveSubset(Dimension dimension, Subset subset) {
/* 338 */     this.activeSubs.put(dimension, subset);
/*     */   }
/*     */ 
/*     */   public final void setActiveSubset2(Dimension dimension, Subset2 subset) {
/* 342 */     this.activeSubs2.put(dimension, subset);
/*     */   }
/*     */ 
/*     */   public final void setSelectedElement(Dimension dimension, Element element)
/*     */   {
/* 347 */     this.selectedElements.put(dimension.getDefaultHierarchy(), element);
/*     */   }
/*     */ 
/*     */   public final void setSelectedElement(Hierarchy hierarchy, Element element) {
/* 351 */     this.selectedElements.put(hierarchy, element);
/*     */   }
/*     */ 
/*     */   public final String getId() {
/* 355 */     return this.id;
/*     */   }
/*     */ 
/*     */   public final synchronized String getName() {
/* 359 */     return this.name;
/*     */   }
/*     */ 
/*     */   public final synchronized void setName(String name) {
/* 363 */     this.name = name;
/*     */   }
/*     */ 
/*     */   public final boolean equals(Object obj) {
/* 367 */     if (obj instanceof AxisImpl) {
/* 368 */       AxisImpl other = (AxisImpl)obj;
/*     */ 
/* 370 */       return (other.getId().equals(this.id)) && 
/* 370 */         (other.view.getId().equals(this.view.getId()));
/*     */     }
/* 372 */     return false;
/*     */   }
/*     */ 
/*     */   public final int hashCode() {
/* 376 */     int hc = 17;
/* 377 */     hc = 37 * hc + this.id.hashCode();
/* 378 */     hc = 37 * hc + this.view.getId().hashCode();
/* 379 */     return hc;
/*     */   }
/*     */ 
/*     */   public final void addExpanded(ElementPath path) {
/* 383 */     this.expandedPaths.add(path);
/*     */   }
/*     */   public final void addExpanded(ElementPath[] paths) {
/* 386 */     this.expandedPaths.addAll(Arrays.asList(paths));
/*     */   }
/*     */ 
/*     */   public final ElementPath[] getExpandedPaths() {
/* 390 */     return 
/* 391 */       (ElementPath[])this.expandedPaths
/* 391 */       .toArray(new ElementPath[this.expandedPaths.size()]);
/*     */   }
/*     */ 
/*     */   public final void removeExpanded(ElementPath path) {
/* 395 */     this.expandedPaths.remove(path);
/*     */   }
/*     */ 
/*     */   final void setData(String key, String data) {
/* 399 */     if (key == null)
/* 400 */       return;
/* 401 */     int index = 0;
/* 402 */     int dataLength = this.data.length;
/* 403 */     while (index < dataLength) {
/* 404 */       if (key.equals(this.data[index]))
/*     */         break;
/* 406 */       index += 2;
/*     */     }
/* 408 */     if (index >= dataLength) {
/* 409 */       String[] newData = new String[dataLength + 2];
/* 410 */       System.arraycopy(this.data, 0, newData, 0, dataLength);
/* 411 */       this.data = newData;
/*     */     }
/* 413 */     this.data[index] = key;
/* 414 */     this.data[(index + 1)] = data;
/*     */   }
/*     */ 
/*     */   public final String getData(String key) {
/* 418 */     if (key != null) {
/* 419 */       int index = 0;
/* 420 */       int dataLength = this.data.length;
/* 421 */       while (index < dataLength) {
/* 422 */         if (key.equals(this.data[index]))
/*     */           break;
/* 424 */         index += 2;
/*     */       }
/* 426 */       if (index < dataLength)
/* 427 */         return this.data[(index + 1)];
/*     */     }
/* 429 */     return null;
/*     */   }
/*     */ 
/*     */   private final String pathToString(Element[] path) {
/* 433 */     StringBuffer _path = new StringBuffer();
/* 434 */     int max = path.length - 1;
/* 435 */     for (int i = 0; i < path.length; ++i) {
/* 436 */       _path.append(path[i]);
/* 437 */       if (i < max)
/* 438 */         _path.append("///");
/*     */     }
/* 440 */     return _path.toString();
/*     */   }
/*     */ }

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name:     org.palo.api.impl.views.AxisImpl
 * JD-Core Version:    0.5.4
 */