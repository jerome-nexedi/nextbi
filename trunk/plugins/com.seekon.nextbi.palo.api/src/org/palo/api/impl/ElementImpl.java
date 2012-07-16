/*     */ package org.palo.api.impl;
/*     */ 
/*     */ import com.tensegrity.palojava.DbConnection;
/*     */ import com.tensegrity.palojava.ElementInfo;
/*     */ import com.tensegrity.palojava.PropertyInfo;
/*     */ import com.tensegrity.palojava.loader.ElementLoader;
/*     */ import com.tensegrity.palojava.loader.PropertyLoader;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.palo.api.Attribute;
/*     */ import org.palo.api.ConnectionEvent;
/*     */ import org.palo.api.Consolidation;
/*     */ import org.palo.api.Database;
/*     */ import org.palo.api.Dimension;
/*     */ import org.palo.api.Element;
/*     */ import org.palo.api.Hierarchy;
/*     */ import org.palo.api.Property2;
/*     */ 
/*     */ class ElementImpl
/*     */   implements Element
/*     */ {
/*     */   private final Dimension dimension;
/*     */   private final Hierarchy hierarchy;
/*     */   private final ConnectionImpl connection;
/*     */   private final DbConnection dbConnection;
/*     */   private final ElementInfo elInfo;
/*     */   private final CompoundKey key;
/*     */   private final PropertyLoader propertyLoader;
/*     */   private final Map<String, Property2Impl> loadedProperties;
/*     */ 
/*     */   static final ElementImpl create(ConnectionImpl connection, Dimension dimension, ElementInfo elInfo)
/*     */   {
/*  75 */     return new ElementImpl(connection, dimension, elInfo, null);
/*     */   }
/*     */ 
/*     */   static final ElementImpl create(ConnectionImpl connection, Dimension dimension, ElementInfo elInfo, Hierarchy hier)
/*     */   {
/*  80 */     return new ElementImpl(connection, dimension, elInfo, hier);
/*     */   }
/*     */ 
/*     */   private ElementImpl(ConnectionImpl connection, Dimension dimension, ElementInfo elInfo, Hierarchy hier)
/*     */   {
/*  96 */     this.elInfo = elInfo;
/*  97 */     this.dimension = dimension;
/*  98 */     this.connection = connection;
/*  99 */     this.dbConnection = connection.getConnectionInternal();
/* 100 */     this.key = 
/* 102 */       new CompoundKey(new Object[] { ElementImpl.class, 
/* 101 */       connection, dimension.getDatabase().getId(), dimension.getId(), 
/* 102 */       elInfo.getId() });
/* 103 */     this.loadedProperties = new HashMap();
/* 104 */     this.propertyLoader = this.dbConnection.getTypedPropertyLoader(elInfo);
/* 105 */     if (hier == null)
/* 106 */       this.hierarchy = dimension.getDefaultHierarchy();
/*     */     else
/* 108 */       this.hierarchy = hier;
/*     */   }
/*     */ 
/*     */   public final Object getAttributeValue(Attribute attribute)
/*     */   {
/* 114 */     if (attribute == null)
/* 115 */       return null;
/* 116 */     return attribute.getValue(this);
/*     */   }
/*     */ 
/*     */   public final Object[] getAttributeValues() {
/* 120 */     Attribute[] attributes = getHierarchy().getAttributes();
/* 121 */     Object[] values = new Object[attributes.length];
/* 122 */     for (int i = 0; i < values.length; ++i)
/* 123 */       values[i] = attributes[i].getValue(this);
/* 124 */     return values;
/*     */   }
/*     */ 
/*     */   public final int getChildCount() {
/* 128 */     return this.elInfo.getChildrenCount();
/*     */   }
/*     */ 
/*     */   public final Element[] getChildren() {
/* 132 */     ElementLoader loader = ((HierarchyImpl)this.hierarchy).getElementLoader();
/* 133 */     ElementInfo[] children = loader.getChildren(this.elInfo);
/*     */ 
/* 142 */     ArrayList _children = new ArrayList();
/* 143 */     for (int i = 0; i < children.length; ++i) {
/* 144 */       Element child = this.hierarchy.getElementById(children[i].getId());
/* 145 */       if (!child.equals(this)) {
/* 146 */         _children.add(child);
/*     */       }
/*     */     }
/* 149 */     return (Element[])_children.toArray(new Element[0]);
/*     */   }
/*     */ 
/*     */   public final Consolidation getConsolidationAt(int index)
/*     */   {
/* 154 */     if (getChildCount() == 0)
/* 155 */       return null;
/* 156 */     String childId = this.elInfo.getChildren()[index];
/* 157 */     double weight = this.elInfo.getWeights()[index];
/* 158 */     return ConsolidationImpl.create(this.connection, this, 
/* 159 */       this.hierarchy.getElementById(childId), weight);
/*     */   }
/*     */ 
/*     */   public final int getConsolidationCount() {
/* 163 */     return getChildCount();
/*     */   }
/*     */ 
/*     */   public final Consolidation[] getConsolidations() {
/* 167 */     String[] childrenIds = this.elInfo.getChildren();
/* 168 */     double[] weights = this.elInfo.getWeights();
/* 169 */     Consolidation[] consolidations = new Consolidation[childrenIds.length];
/* 170 */     for (int i = 0; i < childrenIds.length; ++i) {
/* 171 */       consolidations[i] = 
/* 172 */         ConsolidationImpl.create(this.connection, this, this.hierarchy.getElementById(childrenIds[i]), weights[i]);
/*     */     }
/* 174 */     return consolidations;
/*     */   }
/*     */ 
/*     */   public final int getDepth() {
/* 178 */     return this.elInfo.getDepth();
/*     */   }
/*     */ 
/*     */   public final int getLevel() {
/* 182 */     return this.elInfo.getLevel();
/*     */   }
/*     */ 
/*     */   public final int getIndent() {
/* 186 */     return this.elInfo.getIndent();
/*     */   }
/*     */ 
/*     */   public final Dimension getDimension() {
/* 190 */     return this.dimension;
/*     */   }
/*     */ 
/*     */   public final Hierarchy getHierarchy() {
/* 194 */     return this.hierarchy;
/*     */   }
/*     */ 
/*     */   public final String getName() {
/* 198 */     return this.elInfo.getName();
/*     */   }
/*     */ 
/*     */   public final int getParentCount() {
/* 202 */     return this.elInfo.getParentCount();
/*     */   }
/*     */ 
/*     */   public final Element[] getParents() {
/* 206 */     String[] parentIds = this.elInfo.getParents();
/* 207 */     Element[] parents = new Element[parentIds.length];
/* 208 */     for (int i = 0; i < parents.length; ++i) {
/* 209 */       parents[i] = this.hierarchy.getElementById(parentIds[i]);
/*     */     }
/* 211 */     return parents;
/*     */   }
/*     */ 
/*     */   public final int getType()
/*     */   {
/* 216 */     if (this.connection.isLegacy()) {
/* 217 */       return this.elInfo.getType();
/*     */     }
/* 219 */     return infoType2elType(this.elInfo.getType());
/*     */   }
/*     */ 
/*     */   public final String getTypeAsString() {
/* 223 */     switch (getType()) {
/*     */     case 0:
/*     */     default:
/* 226 */       return "Numeric";
/*     */     case 1:
/* 228 */       return "String";
/*     */     case 2:
/* 230 */       return "Consolidated";
/*     */     case 3:
/* 232 */     }return "Rule";
/*     */   }
/*     */ 
/*     */   public final void rename(String name)
/*     */   {
/* 237 */     renameInternal(name, true);
/*     */   }
/*     */ 
/*     */   public final void setAttributeValue(Attribute attribute, Object value) {
/* 241 */     attribute.setValue(this, value);
/*     */   }
/*     */ 
/*     */   public final void setAttributeValues(Attribute[] attributes, Object[] values) {
/* 245 */     for (int i = 0; i < attributes.length; ++i)
/* 246 */       attributes[i].setValue(this, values[i]);
/*     */   }
/*     */ 
/*     */   public final void setType(int type) {
/* 250 */     setTypeInternal(type);
/*     */   }
/*     */ 
/*     */   public final void updateConsolidations(Consolidation[] consolidations) {
/* 254 */     updateConsolidationsInternal(consolidations, true);
/*     */   }
/*     */ 
/*     */   public final String getId() {
/* 258 */     return this.elInfo.getId();
/*     */   }
/*     */ 
/*     */   public final int getPosition() {
/* 262 */     return this.elInfo.getPosition();
/*     */   }
/*     */ 
/*     */   public final void move(int newPosition) {
/* 266 */     this.dbConnection.move(this.elInfo, newPosition);
/* 267 */     ((HierarchyImpl)this.hierarchy).resetElementsCache();
/*     */   }
/*     */ 
/*     */   public final boolean equals(Object other) {
/* 271 */     if (other instanceof ElementImpl) {
/* 272 */       return this.key.equals(((ElementImpl)other).key);
/*     */     }
/* 274 */     return false;
/*     */   }
/*     */ 
/*     */   public final int hashCode() {
/* 278 */     return this.key.hashCode();
/*     */   }
/*     */ 
/*     */   public final String toString() {
/* 282 */     StringBuffer str = new StringBuffer();
/* 283 */     str.append("Element(\"");
/* 284 */     str.append(getName());
/* 285 */     str.append("\")[");
/* 286 */     str.append(getId());
/* 287 */     str.append("]");
/* 288 */     return str.toString();
/*     */   }
/*     */ 
/*     */   public final ElementInfo getInfo() {
/* 292 */     return this.elInfo;
/*     */   }
/*     */ 
/*     */   final void renameInternal(String name, boolean doEvents)
/*     */   {
/* 305 */     String oldName = getName();
/* 306 */     if (name.equals(oldName))
/* 307 */       return;
/* 308 */     this.dbConnection.rename(this.elInfo, name);
/*     */ 
/* 311 */     ((DimensionImpl)getDimension()).reloadRules();
/*     */ 
/* 314 */     if (doEvents)
/* 315 */       fireElementsRenamed(new Element[] { this }, oldName);
/*     */   }
/*     */ 
/*     */   final void reload(boolean doEvents)
/*     */   {
/* 321 */     String oldName = getName();
/* 322 */     int oldType = getType();
/* 323 */     Consolidation[] oldConsolidations = getConsolidations();
/*     */ 
/* 326 */     this.dbConnection.reload(this.elInfo);
/*     */ 
/* 328 */     if (doEvents) {
/* 329 */       Element[] affetecedElements = { this };
/*     */ 
/* 331 */       if (!getName().equals(oldName))
/* 332 */         fireElementsRenamed(affetecedElements, oldName);
/* 333 */       if (getType() != oldType) {
/* 334 */         fireElementsTypeChanged(affetecedElements, oldType);
/*     */       }
/* 336 */       compareConsolidations(oldConsolidations, getConsolidations(), doEvents);
/*     */     }
/*     */   }
/*     */ 
/*     */   final void clearCache() {
/* 341 */     for (Property2Impl property : this.loadedProperties.values()) {
/* 342 */       property.clearCache();
/*     */     }
/* 344 */     this.loadedProperties.clear();
/* 345 */     this.propertyLoader.reset();
/*     */   }
/*     */ 
/*     */   private final void updateConsolidationsInternal(Consolidation[] newConsolidations, boolean doEvents)
/*     */   {
/* 353 */     Consolidation[] oldConsolidations = getConsolidations();
/*     */ 
/* 355 */     String[] children = new String[newConsolidations.length];
/* 356 */     double[] weights = new double[newConsolidations.length];
/* 357 */     for (int i = 0; i < newConsolidations.length; ++i) {
/* 358 */       ElementImpl child = (ElementImpl)newConsolidations[i].getChild();
/* 359 */       weights[i] = newConsolidations[i].getWeight();
/* 360 */       children[i] = child.getId();
/*     */     }
/*     */ 
/* 363 */     int elType = 2;
/* 364 */     if (!this.connection.isLegacy())
/* 365 */       elType = elType2infoType(elType);
/* 366 */     this.dbConnection.update(this.elInfo, elType, children, weights, this.dbConnection.getServerInfo());
/*     */ 
/* 373 */     compareConsolidations(oldConsolidations, newConsolidations, doEvents);
/*     */   }
/*     */ 
/*     */   private final void setTypeInternal(int type) {
/* 377 */     int oldType = getType();
/* 378 */     if (oldType == type) {
/* 379 */       return;
/*     */     }
/* 381 */     boolean typeOk = false;
/* 382 */     switch (type)
/*     */     {
/*     */     case 0:
/* 384 */       typeOk = true;
/* 385 */       break;
/*     */     case 1:
/* 387 */       typeOk = true;
/* 388 */       break;
/*     */     case 2:
/* 390 */       typeOk = true;
/* 391 */       break;
/*     */     case 3:
/* 393 */       typeOk = true;
/* 394 */       break;
/*     */     default:
/* 396 */       typeOk = false;
/*     */     }
/* 398 */     if (!typeOk) {
/* 399 */       return;
/*     */     }
/*     */ 
/* 402 */     if (!this.connection.isLegacy()) {
/* 403 */       type = elType2infoType(type);
/*     */     }
/* 405 */     this.dbConnection.update(
/* 406 */       this.elInfo, type, this.elInfo.getChildren(), this.elInfo.getWeights(), this.dbConnection.getServerInfo());
/*     */ 
/* 408 */     fireElementsTypeChanged(new Element[] { this }, oldType);
/*     */   }
/*     */ 
/*     */   private final void compareConsolidations(Consolidation[] oldConsolidations, Consolidation[] newConsolidations, boolean doEvents)
/*     */   {
/* 413 */     Set oldCons = new LinkedHashSet(Arrays.asList(oldConsolidations));
/* 414 */     Set newCons = new LinkedHashSet(Arrays.asList(newConsolidations));
/* 415 */     Set removed = new LinkedHashSet(oldCons);
/* 416 */     Set added = new LinkedHashSet(newCons);
/*     */ 
/* 418 */     removed.removeAll(newCons);
/* 419 */     added.removeAll(oldCons);
/*     */ 
/* 421 */     if (!removed.isEmpty()) {
/* 422 */       for (Iterator it = removed.iterator(); it.hasNext(); ) {
/* 423 */         Consolidation consolidation = (Consolidation)it.next();
/* 424 */         Element child = consolidation.getChild();
/* 425 */         if (child != null)
/* 426 */           ((ElementImpl)child).reload(false);
/*     */       }
/* 428 */       if (doEvents) {
/* 429 */         fireConsolidationsRemoved(removed.toArray());
/*     */       }
/*     */     }
/* 432 */     if (!added.isEmpty()) {
/* 433 */       for (Iterator it = added.iterator(); it.hasNext(); ) {
/* 434 */         Consolidation consolidation = (Consolidation)it.next();
/* 435 */         Element child = consolidation.getChild();
/* 436 */         if (child != null) {
/* 437 */           ((ElementImpl)child).reload(false);
/*     */         }
/*     */       }
/* 440 */       if (doEvents)
/* 441 */         fireConsolidationsAdded(added.toArray());
/*     */     }
/*     */   }
/*     */ 
/*     */   private final void fireElementsRenamed(Object[] elements, String oldName)
/*     */   {
/* 447 */     ConnectionEvent ev = new ConnectionEvent(this.connection, 
/* 448 */       getDimension(), 
/* 449 */       7, 
/* 450 */       elements);
/* 451 */     ev.oldValue = oldName;
/* 452 */     this.connection.fireEvent(ev);
/*     */   }
/*     */ 
/*     */   private final void fireElementsTypeChanged(Object[] elements, int oldType) {
/* 456 */     ConnectionEvent ev = new ConnectionEvent(
/* 457 */       this.connection, 
/* 458 */       getDimension(), 
/* 459 */       8, 
/* 460 */       elements);
/* 461 */     ev.oldValue = new Integer(oldType);
/* 462 */     this.connection.fireEvent(ev);
/*     */   }
/*     */ 
/*     */   private final void fireConsolidationsAdded(Object[] consolidations) {
/* 466 */     this.connection.fireEvent(
/* 468 */       new ConnectionEvent(this.connection, this, 
/* 467 */       11, 
/* 468 */       consolidations));
/*     */   }
/*     */ 
/*     */   private final void fireConsolidationsRemoved(Object[] consolidations) {
/* 472 */     this.connection.fireEvent(
/* 474 */       new ConnectionEvent(this.connection, this, 
/* 473 */       12, 
/* 474 */       consolidations));
/*     */   }
/*     */ 
/*     */   private final int infoType2elType(int type)
/*     */   {
/* 479 */     switch (type)
/*     */     {
/*     */     case 1:
/* 480 */       return 0;
/*     */     case 2:
/* 481 */       return 1;
/*     */     case 4:
/* 482 */       return 2;
/*     */     case 3:
/* 483 */       return 3;
/*     */     }
/* 485 */     return -1;
/*     */   }
/*     */ 
/*     */   public static final int elType2infoType(int type) {
/* 489 */     switch (type)
/*     */     {
/*     */     case 0:
/* 490 */       return 1;
/*     */     case 1:
/* 491 */       return 2;
/*     */     case 2:
/* 492 */       return 4;
/*     */     case 3:
/* 493 */       return 3;
/*     */     }
/* 495 */     return -1;
/*     */   }
/*     */ 
/*     */   public String[] getAllPropertyIds() {
/* 499 */     return this.propertyLoader.getAllPropertyIds();
/*     */   }
/*     */ 
/*     */   public Property2 getProperty(String id) {
/* 503 */     PropertyInfo propInfo = this.propertyLoader.load(id);
/* 504 */     if (propInfo == null) {
/* 505 */       return null;
/*     */     }
/* 507 */     Property2 property = (Property2)this.loadedProperties.get(propInfo.getId());
/* 508 */     if (property == null) {
/* 509 */       property = createProperty(propInfo);
/*     */     }
/*     */ 
/* 512 */     return property;
/*     */   }
/*     */ 
/*     */   public void addProperty(Property2 property) {
/* 516 */     if (property == null) {
/* 517 */       return;
/*     */     }
/* 519 */     Property2Impl _property = (Property2Impl)property;
/* 520 */     this.propertyLoader.loaded(_property.getPropInfo());
/* 521 */     this.loadedProperties.put(_property.getId(), _property);
/*     */   }
/*     */ 
/*     */   public void removeProperty(String id) {
/* 525 */     Property2 property = getProperty(id);
/* 526 */     if (property == null) {
/* 527 */       return;
/*     */     }
/* 529 */     if (property.isReadOnly()) {
/* 530 */       return;
/*     */     }
/* 532 */     this.loadedProperties.remove(property);
/*     */   }
/*     */ 
/*     */   private void createProperty(Property2 parent, PropertyInfo kid) {
/* 536 */     Property2 p2Kid = Property2Impl.create(parent, kid);
/* 537 */     parent.addChild(p2Kid);
/* 538 */     for (PropertyInfo kidd : kid.getChildren())
/* 539 */       createProperty(p2Kid, kidd);
/*     */   }
/*     */ 
/*     */   private Property2 createProperty(PropertyInfo propInfo)
/*     */   {
/* 544 */     Property2 prop = Property2Impl.create(null, propInfo);
/* 545 */     for (PropertyInfo kid : propInfo.getChildren()) {
/* 546 */       createProperty(prop, kid);
/*     */     }
/* 548 */     return prop;
/*     */   }
/*     */ 
/*     */   public boolean canBeModified()
/*     */   {
/* 553 */     return this.elInfo.canBeModified();
/*     */   }
/*     */ 
/*     */   public boolean canCreateChildren()
/*     */   {
/* 558 */     return this.elInfo.canCreateChildren();
/*     */   }
/*     */ }

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name:     org.palo.api.impl.ElementImpl
 * JD-Core Version:    0.5.4
 */