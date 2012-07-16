/*     */ package org.palo.viewapi.uimodels.axis;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.LinkedList;
/*     */ import java.util.Set;
/*     */ import org.palo.api.Element;
/*     */ import org.palo.api.ElementNode;
/*     */ import org.palo.api.Hierarchy;
/*     */ import org.palo.api.utils.ElementPath;
/*     */ import org.palo.viewapi.Axis;
/*     */ import org.palo.viewapi.AxisHierarchy;
/*     */ import org.palo.viewapi.uimodels.axis.events.AxisModelEvent;
/*     */ 
/*     */ public class AxisTreeModel extends AbstractAxisModel
/*     */ {
/*     */   private final AxisTreeTraverser traverser;
/*  73 */   protected final Set<AxisItem> roots = new LinkedHashSet();
/*  74 */   private final Set<String> expandedNodes = new LinkedHashSet();
/*     */ 
/*     */   public AxisTreeModel(Axis axis) {
/*  77 */     super(axis);
/*  78 */     this.traverser = new AxisTreeTraverser(axis);
/*     */ 
/*  80 */     init();
/*     */   }
/*     */ 
/*     */   public final Hierarchy[] getHierarchies() {
/*  84 */     return this.axis.getHierarchies();
/*     */   }
/*     */ 
/*     */   public final int getAxisHierarchyCount() {
/*  88 */     return this.axis.getHierarchies().length;
/*     */   }
/*     */ 
/*     */   public final AxisItem[] getRoots() {
/*  92 */     return (AxisItem[])this.roots.toArray(new AxisItem[0]);
/*     */   }
/*     */ 
/*     */   public final AxisItem getItem(ElementPath path)
/*     */   {
/* 106 */     if (path == null) {
/* 107 */       return null;
/*     */     }
/* 109 */     return getItem(path.toString());
/*     */   }
/*     */ 
/*     */   public final AxisItem getItem(final String path) {
/* 113 */     if (path == null)
/* 114 */       return null;
/* 115 */     final AxisItem[] items = new AxisItem[1];
/*     */ 
/* 117 */     AxisTreeTraverser.AxisTreeVisitor visitor = new AxisTreeTraverser.AxisTreeVisitor() {
/*     */       public final boolean visit(AxisItem item) {
/* 119 */         String _path = item.getPath();
/* 120 */         if (path.equals(_path)) {
/* 121 */           items[0] = item;
/* 122 */           return false;
/*     */         }
/* 124 */         return true;
/*     */       }
/*     */     };
/* 128 */     for (AxisItem root : this.roots)
/* 129 */       this.traverser.traverse(root, visitor);
/* 130 */     return items[0];
/*     */   }
/*     */   public final AxisItem getItem(final String path, final int index) {
/* 133 */     if (path == null)
/* 134 */       return null;
/* 135 */     final AxisItem[] items = new AxisItem[1];
/*     */ 
/* 137 */     AxisTreeTraverser.AxisTreeVisitor visitor = new AxisTreeTraverser.AxisTreeVisitor() {
/*     */       public final boolean visit(AxisItem item) {
/* 139 */         String _path = item.getPath();
/* 140 */         if (path.equals(_path)) {
/* 141 */           boolean foundIt = true;
/*     */ 
/* 143 */           if (index != -1)
/* 144 */             foundIt = item.index == index;
/* 145 */           if (foundIt) {
/* 146 */             items[0] = item;
/* 147 */             return false;
/*     */           }
/*     */         }
/* 150 */         return true;
/*     */       }
/*     */     };
/* 154 */     for (AxisItem root : this.roots)
/* 155 */       this.traverser.traverse(root, visitor);
/* 156 */     return items[0];
/*     */   }
/*     */ 
/*     */   public final AxisItem findItem(final String pathExpression, final String hierarchyID)
/*     */   {
/* 161 */     if (pathExpression == null)
/* 162 */       return null;
/* 163 */     final AxisItem[] items = new AxisItem[1];
/*     */ 
/* 165 */     AxisTreeTraverser.AxisTreeVisitor visitor = new AxisTreeTraverser.AxisTreeVisitor() {
/*     */       public final boolean visit(AxisItem item) {
/* 167 */         String _path = item.getPath();
/* 168 */         String _hierID = item.getHierarchy().getId();
/* 169 */         if ((_hierID.equals(hierarchyID)) && 
/* 170 */           (_path.matches(pathExpression))) {
/* 171 */           items[0] = item;
/* 172 */           return false;
/*     */         }
/* 174 */         return true;
/*     */       }
/*     */     };
/* 178 */     for (AxisItem root : this.roots) {
/* 179 */       if (!this.traverser.traverseHierarchiesFirst(root, visitor))
/*     */         break;
/*     */     }
/* 182 */     return items[0];
/*     */   }
/*     */ 
/*     */   public final AxisItem findItem(final String pathExpression, final int index, final String hierarchyID) {
/* 186 */     if (pathExpression == null)
/* 187 */       return null;
/* 188 */     final AxisItem[] items = new AxisItem[1];
/*     */ 
/* 190 */     AxisTreeTraverser.AxisTreeVisitor visitor = new AxisTreeTraverser.AxisTreeVisitor() {
/*     */       public final boolean visit(AxisItem item) {
/* 192 */         String _path = item.getPath();
/* 193 */         String _hierID = item.getHierarchy().getId();
/* 194 */         if ((_hierID.equals(hierarchyID)) && 
/* 195 */           (_path.matches(pathExpression))) {
/* 196 */           boolean foundIt = true;
/*     */ 
/* 198 */           if (index != -1)
/* 199 */             foundIt = item.index == index;
/* 200 */           if (foundIt) {
/* 201 */             items[0] = item;
/* 202 */             return false;
/*     */           }
/*     */         }
/* 205 */         return true;
/*     */       }
/*     */     };
/* 209 */     for (AxisItem root : this.roots) {
/* 210 */       if (!this.traverser.traverseHierarchiesFirst(root, visitor))
/*     */         break;
/*     */     }
/* 213 */     return items[0];
/*     */   }
/*     */ 
/*     */   public final int getMaximumLevel(final Hierarchy hierarchy) {
/* 217 */     final int[] maxLevel = new int[1];
/* 218 */     AxisTreeTraverser.AxisTreeVisitor visitor = new AxisTreeTraverser.AxisTreeVisitor() {
/*     */       public final boolean visit(AxisItem item) {
/* 220 */         if (item.getHierarchy().equals(hierarchy)) {
/* 221 */           int lvl = item.getLevel();
/* 222 */           if (maxLevel[0] < lvl)
/* 223 */             maxLevel[0] = lvl;
/*     */         }
/* 225 */         return true;
/*     */       }
/*     */     };
/* 229 */     for (AxisItem root : this.roots)
/* 230 */       this.traverser.hierarchiesFirst(root, visitor);
/* 231 */     return maxLevel[0];
/*     */   }
/*     */ 
/*     */   public final int getMaximumAbsoluteDepth(final Hierarchy hierarchy)
/*     */   {
/* 241 */     final int[] maxLevel = new int[1];
/* 242 */     AxisTreeTraverser.AxisTreeVisitor visitor = new AxisTreeTraverser.AxisTreeVisitor() {
/*     */       public final boolean visit(AxisItem item) {
/* 244 */         if (item.getHierarchy().equals(hierarchy)) {
/* 245 */           int lvl = item.getElement().getDepth();
/* 246 */           if (maxLevel[0] < lvl)
/* 247 */             maxLevel[0] = lvl;
/*     */         }
/* 249 */         return true;
/*     */       }
/*     */     };
/* 253 */     for (AxisItem root : this.roots)
/* 254 */       this.traverser.hierarchiesFirst(root, visitor);
/* 255 */     return maxLevel[0];
/*     */   }
/*     */ 
/*     */   public final AxisItem getFirstSibling(AxisItem item) {
/* 259 */     return getSiblings(item)[0];
/*     */   }
/*     */ 
/*     */   public final AxisItem getNextSibling(AxisItem item)
/*     */   {
/* 269 */     AxisItem[] siblings = getSiblings(item);
/* 270 */     for (int i = 0; i < siblings.length; ++i) {
/* 271 */       if (siblings[i].equals(item)) {
/* 272 */         int next = i + 1;
/* 273 */         if (next >= siblings.length) break;
/* 274 */         return siblings[next];
/*     */       }
/*     */     }
/*     */ 
/* 278 */     return null;
/*     */   }
/*     */ 
/*     */   public final AxisItem getLastSibling(AxisItem item) {
/* 282 */     AxisItem[] siblings = getSiblings(item);
/* 283 */     return (siblings.length > 0) ? siblings[(siblings.length - 1)] : item;
/*     */   }
/*     */ 
/*     */   public final AxisItem getLastChildInNextHierarchy(AxisItem item) {
/* 287 */     AxisItem lastChild = null;
/* 288 */     AxisItem[] rootsInNextHier = item.getRootsInNextHierarchy();
/* 289 */     if (rootsInNextHier.length > 0) {
/* 290 */       AxisItem lastRoot = rootsInNextHier[(rootsInNextHier.length - 1)];
/*     */ 
/* 292 */       lastChild = getLastChild(lastRoot);
/*     */     }
/* 294 */     return lastChild;
/*     */   }
/*     */ 
/*     */   public final AxisItem getFirstChildInNextHierarchy(AxisItem item) {
/* 298 */     if (item.hasRootsInNextHierarchy())
/* 299 */       return item.getRootsInNextHierarchy()[0];
/* 300 */     return null;
/*     */   }
/*     */ 
/*     */   public final AxisItem[] getSiblings(AxisItem item) {
/* 304 */     AxisItem parent = item.getParent();
/* 305 */     if (parent != null) {
/* 306 */       return parent.getChildren();
/*     */     }
/*     */ 
/* 309 */     parent = item.getParentInPrevHierarchy();
/* 310 */     if (parent != null) {
/* 311 */       return parent.getRootsInNextHierarchy();
/*     */     }
/*     */ 
/* 314 */     return (AxisItem[])this.roots.toArray(new AxisItem[0]);
/*     */   }
/*     */ 
/*     */   public final void expand(AxisItem item)
/*     */   {
/* 319 */     if (item == null) {
/* 320 */       return;
/*     */     }
/*     */ 
/* 323 */     AxisModelEvent ev = notifyWillExpand(item);
/* 324 */     if (ev.doit) {
/* 325 */       AxisItem[] items = expandInternal(item);
/* 326 */       notifyExpanded(item, new AxisItem[][] { items });
/*     */     }
/*     */   }
/*     */ 
/*     */   public final void collapse(AxisItem item) {
/* 331 */     if (item == null) {
/* 332 */       return;
/*     */     }
/*     */ 
/* 335 */     AxisModelEvent ev = notifyWillCollapse(item);
/* 336 */     if (ev.doit) {
/* 337 */       AxisItem[] items = collapseInternal(item);
/* 338 */       notifyCollapsed(item, new AxisItem[][] { items });
/*     */     }
/*     */   }
/*     */ 
/*     */   public void showAllParents(boolean b)
/*     */   {
/* 344 */     if (b)
/* 345 */       showParents();
/*     */     else
/* 347 */       hideParents();
/* 348 */     notifyStructureChange();
/*     */   }
/*     */ 
/*     */   public final void expandAll() {
/* 352 */     expandAllInternal(true);
/*     */   }
/*     */ 
/*     */   public final void expandAll(boolean doItIterative) {
/* 356 */     if (doItIterative)
/* 357 */       expandAllIterative(true);
/*     */     else
/* 359 */       expandAllInternal(true);
/*     */   }
/*     */ 
/*     */   public final void collapseAll() {
/* 363 */     AxisTreeTraverser.AxisTreeVisitor visitor = new AxisTreeTraverser.AxisTreeVisitor() {
/*     */       public final boolean visit(AxisItem item) {
/* 365 */         item.deleteState(2);
/*     */ 
/* 367 */         item.removeChildren();
/* 368 */         return true;
/*     */       }
/*     */     };
/* 371 */     for (AxisItem root : this.roots) {
/* 372 */       this.traverser.traverse(root, visitor);
/*     */     }
/* 374 */     notifyStructureChange();
/*     */   }
/*     */ 
/*     */   public final void refresh() {
/* 378 */     init();
/*     */   }
/*     */   protected final void init() {
/* 381 */     AxisTreeTraverser.AxisTreeHierarchyVisitor visitor = new AxisTreeTraverser.AxisTreeHierarchyVisitor() {
/*     */       public final boolean visit(AxisItem item, AxisItem parentInPrevHier) {
/* 383 */         if (parentInPrevHier != null) {
/* 384 */           parentInPrevHier.addRootInNextHierarchy(item);
/* 385 */           item.setParentInPreviousHierarchy(parentInPrevHier);
/*     */         }
/* 387 */         return true;
/*     */       }
/*     */     };
/* 391 */     this.roots.clear();
/* 392 */     this.expandedNodes.clear();
/*     */ 
/* 394 */     AxisHierarchy[] hierarchies = this.axis.getAxisHierarchies();
/* 395 */     if (hierarchies.length == 0) {
/* 396 */       return;
/*     */     }
/* 398 */     int topHierarchy = 0;
/* 399 */     ElementNode[] rootNodes = hierarchies[topHierarchy].getRootNodes();
/* 400 */     int index = 0;
/* 401 */     for (ElementNode root : rootNodes) {
/* 402 */       AxisItem iRoot = new AxisItem(root, 0);
/* 403 */       iRoot.index = (index++);
/* 404 */       this.roots.add(iRoot);
/* 405 */       this.traverser.createAxisTree(iRoot, visitor);
/*     */     }
/*     */ 
/* 408 */     applyExpanded(this.axis.getExpandedPaths());
/*     */   }
/*     */ 
/*     */   public final String[] getExpandedNodes() {
/* 412 */     return (String[])this.expandedNodes.toArray(new String[0]);
/*     */   }
/*     */ 
/*     */   public final void expandTheseNodes(String[] nodesToExpand) {
/* 416 */     if (nodesToExpand == null) {
/* 417 */       return;
/*     */     }
/* 419 */     final HashSet allExpandedPaths = new HashSet();
/* 420 */     for (String exp : nodesToExpand) {
/* 421 */       allExpandedPaths.add(exp);
/*     */     }
/* 423 */     AxisTreeTraverser.AxisTreeVisitor visitor = new AxisTreeTraverser.AxisTreeVisitor() {
/*     */       public final boolean visit(AxisItem item) {
/* 425 */         if (allExpandedPaths.isEmpty()) {
/* 426 */           return false;
/*     */         }
/* 428 */         if (allExpandedPaths.contains(item.getPath())) {
/* 429 */           AxisTreeModel.this.expandInternal(item);
/* 430 */           allExpandedPaths.remove(item.getPath());
/*     */         }
/* 432 */         return true;
/*     */       }
/*     */     };
/* 435 */     for (AxisItem root : this.roots)
/* 436 */       this.traverser.traverse(root, visitor);
/*     */   }
/*     */ 
/*     */   private final AxisItem getLastChild(AxisItem item)
/*     */   {
/* 444 */     if ((item.hasChildren()) && (item.hasState(2))) {
/* 445 */       AxisItem[] children = item.getChildren();
/* 446 */       return getLastChild(children[(children.length - 1)]);
/*     */     }
/* 448 */     return item;
/*     */   }
/*     */ 
/*     */   private final void applyExpanded(ElementPath[] expanded) {
/* 452 */     if (expanded.length < 1) {
/* 453 */       return;
/*     */     }
/* 455 */     ArrayList<ElementPath> paths = new ArrayList();
/* 456 */     paths.addAll(Arrays.asList(expanded));
/* 457 */     Collections.sort(paths, new ElementPathComparator());
/* 458 */     for (ElementPath path : paths) {
/* 459 */       AxisItem item = getItem(path);
/* 460 */       if (item != null)
/* 461 */         expandInternal(item);
/*     */     }
/*     */   }
/*     */ 
/*     */   private final void hideParents()
/*     */   {
/* 467 */     final ArrayList rootItems = new ArrayList();
/* 468 */     AxisTreeTraverser.AxisTreeHierarchyVisitor visitor = new AxisTreeTraverser.AxisTreeHierarchyVisitor()
/*     */     {
/*     */       public boolean visit(AxisItem item, AxisItem parentInPrevHier) {
/* 471 */         if ((item.hasChildren()) && (item.hasState(2)))
/*     */         {
/* 473 */           if (parentInPrevHier != null) {
/* 474 */             parentInPrevHier.replaceRootInNextHierarchy(item, 
/* 475 */               item.getChildren());
/*     */           }
/*     */         }
/* 478 */         else if (parentInPrevHier == null) {
/* 479 */           rootItems.add(item);
/*     */         }
/* 481 */         return true;
/*     */       }
/*     */     };
/* 484 */     for (AxisItem root : this.roots)
/* 485 */       this.traverser.recreateTree(root, null, visitor);
/* 486 */     if (!rootItems.isEmpty()) {
/* 487 */       this.roots.clear();
/* 488 */       this.roots.addAll(rootItems);
/*     */     }
/*     */   }
/*     */ 
/*     */   private final void showParents() {
/* 492 */     final ArrayList rootItems = new ArrayList();
/* 493 */     AxisTreeTraverser.AxisTreeHierarchyVisitor visitor = new AxisTreeTraverser.AxisTreeHierarchyVisitor()
/*     */     {
/*     */       public boolean visit(AxisItem item, AxisItem parentInPrevHier) {
/* 496 */         if ((item.hasChildren()) && (item.hasState(2)))
/*     */         {
/* 498 */           if (parentInPrevHier != null) {
/* 499 */             parentInPrevHier.replaceRootInNextHierarchy(item, 
/* 500 */               item.getChildren());
/*     */           }
/*     */         }
/* 503 */         else if (parentInPrevHier == null) {
/* 504 */           rootItems.add(item);
/*     */         }
/* 506 */         return true;
/*     */       }
/*     */     };
/* 509 */     for (AxisItem root : this.roots)
/* 510 */       this.traverser.recreateTree(root, null, visitor);
/* 511 */     if (!rootItems.isEmpty()) {
/* 512 */       this.roots.clear();
/* 513 */       this.roots.addAll(rootItems);
/*     */     }
/*     */ 
/* 517 */     this.roots.clear();
/*     */ 
/* 519 */     init();
/*     */   }
/*     */ 
/*     */   private final AxisItem[] expandInternal(AxisItem item) {
/* 523 */     item.setState(2);
/* 524 */     this.expandedNodes.add(item.getPath());
/* 525 */     if (item.update()) {
/* 526 */       AxisItem[] children = item.getChildren();
/*     */ 
/* 528 */       for (AxisItem child : children) {
/* 529 */         if (this.expandedNodes.contains(child.getPath())) {
/* 530 */           expandInternal(child);
/*     */         }
/* 532 */         child.setParentInPreviousHierarchy(
/* 533 */           item.getParentInPrevHierarchy());
/* 534 */         if (item.hasRootsInNextHierarchy()) {
/* 535 */           AxisTreeTraverser.AxisTreeHierarchyVisitor visitor = new AxisTreeTraverser.AxisTreeHierarchyVisitor()
/*     */           {
/*     */             public final boolean visit(AxisItem item, AxisItem parentInPrevHier) {
/* 538 */               if (parentInPrevHier != null) {
/* 539 */                 parentInPrevHier.addRootInNextHierarchy(item);
/* 540 */                 item
/* 541 */                   .setParentInPreviousHierarchy(parentInPrevHier);
/*     */               }
/* 543 */               return true;
/*     */             }
/*     */           };
/* 546 */           this.traverser.createAxisTree(child, visitor);
/*     */         }
/*     */       }
/* 549 */       return children;
/*     */     }
/* 551 */     return item.getChildren();
/*     */   }
/*     */ 
/*     */   private final AxisItem[] collapseInternal(AxisItem item) {
/* 555 */     this.expandedNodes.remove(item.getPath());
/* 556 */     item.deleteState(2);
/* 557 */     AxisItem[] children = item.getChildren();
/*     */ 
/* 560 */     return children;
/*     */   }
/*     */ 
/*     */   private final void expandAllInternal(boolean doEvents)
/*     */   {
/* 565 */     expandAllIterative(doEvents);
/*     */ 
/* 568 */     AxisTreeTraverser.AxisTreeVisitor visitor = new AxisTreeTraverser.AxisTreeVisitor() {
/*     */       public final boolean visit(AxisItem item) {
/* 570 */         if ((item.hasChildren()) && (!item.hasState(2)))
/* 571 */           AxisTreeModel.this.expandInternal(item);
/* 572 */         return true;
/*     */       }
/*     */     };
/* 575 */     for (AxisItem root : this.roots)
/* 576 */       this.traverser.traverse(root, visitor);
/* 577 */     if (doEvents)
/* 578 */       notifyStructureChange();
/*     */   }
/*     */ 
/*     */   private final void expandAllIterative(boolean doEvents) {
/* 582 */     LinkedList stack = new LinkedList();
/*     */ 
/* 586 */     for (AxisItem root : this.roots) {
/* 587 */       stack.add(root);
/*     */     }
/*     */ 
/* 590 */     while (!stack.isEmpty()) {
/* 591 */       AxisItem item = (AxisItem)stack.poll();
/* 592 */       if ((item.hasChildren()) && (!item.hasState(2)))
/* 593 */         expandInternal(item);
/* 594 */       if (item.hasChildren()) {
/* 595 */         for (AxisItem child : item.getChildren()) {
/* 596 */           stack.add(child);
/*     */         }
/*     */       }
/* 599 */       if (item.hasRootsInNextHierarchy()) {
/* 600 */         for (AxisItem root : item.getRootsInNextHierarchy()) {
/* 601 */           stack.add(root);
/*     */         }
/*     */       }
/*     */     }
/* 605 */     if (doEvents)
/* 606 */       notifyStructureChange();
/*     */   }
/*     */ }

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi.jar
 * Qualified Name:     org.palo.viewapi.uimodels.axis.AxisTreeModel
 * JD-Core Version:    0.5.4
 */