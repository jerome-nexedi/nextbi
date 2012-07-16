/*     */ package org.palo.api.impl;
/*     */ 
/*     */ import com.tensegrity.palojava.DimensionInfo;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.palo.api.Attribute;
/*     */ import org.palo.api.Consolidation;
/*     */ import org.palo.api.Cube;
/*     */ import org.palo.api.Database;
/*     */ import org.palo.api.Dimension;
/*     */ import org.palo.api.Element;
/*     */ import org.palo.api.ElementNode;
/*     */ import org.palo.api.ElementNodeVisitor;
/*     */ import org.palo.api.Hierarchy;
/*     */ import org.palo.api.Subset;
/*     */ import org.palo.api.VirtualObject;
/*     */ import org.palo.api.subsets.SubsetHandler;
/*     */ 
/*     */ class VirtualDimensionImpl
/*     */   implements Dimension, VirtualObject
/*     */ {
/*     */   private final Dimension sourceDimension;
/*     */   private LinkedHashMap velements2elements;
/*     */   private LinkedHashMap elements2velements;
/*     */   private Map name2element;
/*     */   private Map id2element;
/*  72 */   private final ArrayList rootNodes = new ArrayList();
/*     */   private Element[] elements;
/*     */   private Hierarchy activeHierarchy;
/*     */   private Hierarchy hierarchy;
/*     */   private Object virtualDefinition;
/*     */ 
/*     */   VirtualDimensionImpl(Dimension sourceDimension, Element[] elements, ElementNode[] rootNodes, boolean isFlat, Hierarchy activeHierarchy)
/*     */   {
/*  91 */     this.sourceDimension = sourceDimension;
/*  92 */     this.activeHierarchy = activeHierarchy;
/*  93 */     initElements(elements, rootNodes, isFlat);
/*     */   }
/*     */ 
/*     */   public final String getId() {
/*  97 */     return this.sourceDimension.getId() + "@@" + Integer.toHexString(System.identityHashCode(this));
/*     */   }
/*     */ 
/*     */   public Dimension getSourceDimension()
/*     */   {
/* 102 */     return this.sourceDimension;
/*     */   }
/*     */ 
/*     */   public int getExtendedType()
/*     */   {
/* 107 */     return 1;
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 112 */     return this.sourceDimension.getName() + "@@" + Integer.toHexString(System.identityHashCode(this));
/*     */   }
/*     */ 
/*     */   public Database getDatabase()
/*     */   {
/* 117 */     return this.sourceDimension.getDatabase();
/*     */   }
/*     */ 
/*     */   public final Cube[] getCubes() {
/* 121 */     DatabaseImpl database = (DatabaseImpl)this.sourceDimension.getDatabase();
/* 122 */     return database.getCubes(this.sourceDimension);
/*     */   }
/*     */ 
/*     */   public int getElementCount()
/*     */   {
/* 133 */     return this.elements.length;
/*     */   }
/*     */ 
/*     */   public Element getElementAt(int index)
/*     */   {
/* 139 */     return this.elements[index];
/*     */   }
/*     */ 
/*     */   public Element[] getElements()
/*     */   {
/* 145 */     return (Element[])this.elements.clone();
/*     */   }
/*     */ 
/*     */   public String[] getElementNames()
/*     */   {
/* 151 */     if (this.elements == null)
/* 152 */       return new String[0];
/* 153 */     String[] names = new String[this.elements.length];
/* 154 */     for (int i = 0; i < names.length; ++i)
/*     */     {
/* 156 */       names[i] = this.elements[i].getName();
/*     */     }
/* 158 */     return names;
/*     */   }
/*     */ 
/*     */   public Element getElementByName(String name)
/*     */   {
/* 164 */     return (Element)this.name2element.get(name);
/*     */   }
/*     */ 
/*     */   public Element getElementById(String id)
/*     */   {
/* 169 */     return (Element)this.id2element.get(id);
/*     */   }
/*     */ 
/*     */   public void rename(String name)
/*     */   {
/* 174 */     Util.noopWarning();
/*     */   }
/*     */ 
/*     */   public void addElements(String[] names, int[] types)
/*     */   {
/* 179 */     Util.noopWarning();
/*     */   }
/*     */ 
/*     */   public void updateConsolidations(Consolidation[] cons) {
/* 183 */     Util.noopWarning();
/*     */   }
/*     */ 
/*     */   public void removeConsolidations(Element[] elements) {
/* 187 */     Util.noopWarning();
/*     */   }
/*     */ 
/*     */   public Element[] getRootElements()
/*     */   {
/* 195 */     ArrayList out = new ArrayList();
/* 196 */     int i = 0; for (int n = this.rootNodes.size(); i < n; ++i) {
/* 197 */       ElementNode node = (ElementNode)this.rootNodes.get(i);
/* 198 */       out.add(node.getElement());
/*     */     }
/* 200 */     return (Element[])out.toArray(new Element[0]);
/*     */   }
/*     */ 
/*     */   public Element[] getElementsInOrder()
/*     */   {
/* 267 */     final ArrayList result = new ArrayList();
/*     */ 
/* 277 */     ElementNodeVisitor visitor = new ElementNodeVisitor() {
/*     */       public void visit(ElementNode elementNode, ElementNode parent) {
/* 279 */         result.add(elementNode.getElement());
/*     */       }
/*     */     };
/* 282 */     visitElementTree(visitor);
/* 283 */     return (Element[])result.toArray(new Element[0]);
/*     */   }
/*     */ 
/*     */   public ElementNode[] getElementsTree()
/*     */   {
/* 289 */     return (ElementNode[])this.rootNodes.toArray(new ElementNode[this.rootNodes.size()]);
/*     */   }
/*     */ 
/*     */   public void visitElementTree(ElementNodeVisitor visitor)
/*     */   {
/* 296 */     int i = 0; for (int n = this.rootNodes.size(); i < n; ++i) {
/* 297 */       ElementNode rootNode = (ElementNode)this.rootNodes.get(i);
/* 298 */       traverse(rootNode, null, visitor);
/*     */     }
/*     */   }
/*     */ 
/*     */   public ElementNode[] getAllElementNodes()
/*     */   {
/* 331 */     final ArrayList allnodes = new ArrayList();
/* 332 */     ElementNodeVisitor visitor = new ElementNodeVisitor()
/*     */     {
/*     */       public void visit(ElementNode node, ElementNode parent) {
/* 335 */         allnodes.add(node);
/*     */       }
/*     */     };
/* 338 */     Element[] roots = getRootElements();
/* 339 */     for (int i = 0; i < roots.length; ++i)
/*     */     {
/* 341 */       ElementNode rootNode = new ElementNode(roots[i], null);
/* 342 */       DimensionUtil.traverse(rootNode, visitor);
/*     */     }
/* 344 */     return (ElementNode[])allnodes.toArray(new ElementNode[0]);
/*     */   }
/*     */ 
/*     */   public void dumpElementsTree()
/*     */   {
/* 349 */     Util.noopWarning();
/*     */   }
/*     */ 
/*     */   public Element addElement(String name, int type)
/*     */   {
/* 357 */     Util.noopWarning();
/* 358 */     return null;
/*     */   }
/*     */ 
/*     */   public void removeElement(Element element)
/*     */   {
/* 363 */     Util.noopWarning();
/*     */   }
/*     */ 
/*     */   public void removeElements(Element[] elements) {
/* 367 */     Util.noopWarning();
/*     */   }
/*     */ 
/*     */   public void renameElement(Element element, String name)
/*     */   {
/* 372 */     Util.noopWarning();
/*     */   }
/*     */ 
/*     */   public Consolidation newConsolidation(Element element, Element parent, double weight)
/*     */   {
/* 382 */     Util.noopWarning();
/* 383 */     return null;
/*     */   }
/*     */ 
/*     */   public Attribute addAttribute(String name)
/*     */   {
/* 508 */     return this.sourceDimension.addAttribute(name);
/*     */   }
/*     */ 
/*     */   public void removeAttribute(Attribute attribute) {
/* 512 */     this.sourceDimension.removeAttribute(attribute);
/*     */   }
/*     */ 
/*     */   public void removeAllAttributes() {
/* 516 */     this.sourceDimension.removeAllAttributes();
/*     */   }
/*     */ 
/*     */   public Dimension getAttributeDimension()
/*     */   {
/* 521 */     return this.sourceDimension.getAttributeDimension();
/*     */   }
/*     */ 
/*     */   public Cube getAttributeCube() {
/* 525 */     return this.sourceDimension.getAttributeCube();
/*     */   }
/*     */ 
/*     */   public Attribute[] getAttributes() {
/* 529 */     return this.sourceDimension.getAttributes();
/*     */   }
/*     */ 
/*     */   public Attribute getAttribute(String id) {
/* 533 */     return this.sourceDimension.getAttribute(id);
/*     */   }
/*     */ 
/*     */   public Attribute getAttributeByName(String name) {
/* 537 */     return this.sourceDimension.getAttributeByName(name);
/*     */   }
/*     */ 
/*     */   public boolean isAttributeDimension() {
/* 541 */     return false;
/*     */   }
/*     */ 
/*     */   public Object[] getAttributeValues(Attribute[] attributes, Element[] elements)
/*     */   {
/* 546 */     return this.sourceDimension.getAttributeValues(attributes, elements);
/*     */   }
/*     */ 
/*     */   public void setAttributeValues(Attribute[] attributes, Element[] elements, Object[] values)
/*     */   {
/* 551 */     this.sourceDimension.setAttributeValues(attributes, elements, values);
/*     */   }
/*     */ 
/*     */   public Subset getSubset(String id) {
/* 555 */     return this.sourceDimension.getSubset(id);
/*     */   }
/*     */ 
/*     */   public Subset[] getSubsets() {
/* 559 */     return this.sourceDimension.getSubsets();
/*     */   }
/*     */ 
/*     */   public boolean isSubsetDimension() {
/* 563 */     return false;
/*     */   }
/*     */ 
/*     */   public void removeSubset(Subset subset)
/*     */   {
/* 568 */     this.sourceDimension.removeSubset(subset);
/*     */   }
/*     */ 
/*     */   public Subset addSubset(String name) {
/* 572 */     return this.sourceDimension.addSubset(name);
/*     */   }
/*     */ 
/*     */   public int getMaxDepth()
/*     */   {
/* 580 */     return this.sourceDimension.getMaxDepth();
/*     */   }
/*     */ 
/*     */   public int getMaxLevel()
/*     */   {
/* 588 */     return this.sourceDimension.getMaxLevel();
/*     */   }
/*     */ 
/*     */   private final void initElements(Element[] newElements, ElementNode[] elNodes, boolean isFlat)
/*     */   {
/* 594 */     this.elements = new Element[newElements.length];
/* 595 */     this.name2element = new HashMap();
/* 596 */     this.id2element = new HashMap();
/*     */ 
/* 598 */     this.velements2elements = new LinkedHashMap();
/* 599 */     this.elements2velements = new LinkedHashMap();
/* 600 */     for (int i = 0; i < newElements.length; ++i) {
/* 601 */       VirtualElementImpl velement = new VirtualElementImpl(this, isFlat, 
/* 602 */         newElements[i]);
/* 603 */       this.elements[i] = velement;
/* 604 */       this.velements2elements.put(velement, newElements[i]);
/* 605 */       this.elements2velements.put(newElements[i], velement);
/* 606 */       this.name2element.put(this.elements[i].getName(), this.elements[i]);
/* 607 */       this.id2element.put(this.elements[i].getId(), this.elements[i]);
/*     */     }
/* 609 */     for (int i = 0; i < elNodes.length; ++i)
/* 610 */       checkNode(elNodes[i], null, new LinkedHashMap(), this.rootNodes, isFlat);
/*     */   }
/*     */ 
/*     */   private final void checkNode(ElementNode node, ElementNode parent, Map elNodes2velNodes, List rootNodes, boolean isFlat)
/*     */   {
/* 621 */     Element element = node.getElement();
/* 622 */     Element vElement = (Element)this.elements2velements.get(element);
/* 623 */     if (vElement != null) {
/* 624 */       if (isFlat) {
/* 625 */         ElementNode velementNode = new ElementNode(vElement, null);
/* 626 */         rootNodes.add(velementNode);
/*     */       } else {
/* 628 */         Consolidation vconsolidation = node.getConsolidation();
/* 629 */         if (vconsolidation != null) {
/* 630 */           VirtualElementImpl vparent = 
/* 631 */             (VirtualElementImpl)this.elements2velements
/* 631 */             .get(vconsolidation.getParent());
/*     */ 
/* 633 */           if (vparent != null) {
/* 634 */             Element vchild = 
/* 635 */               (Element)this.elements2velements
/* 635 */               .get(vconsolidation.getChild());
/* 636 */             vconsolidation = new VirtualConsolidationImpl(vparent, 
/* 637 */               vchild, vconsolidation.getWeight());
/* 638 */             vparent.addConsolidation(vconsolidation);
/*     */           } else {
/* 640 */             vconsolidation = null;
/*     */           }
/*     */         }
/* 642 */         ElementNode velNode = new ElementNode(vElement, vconsolidation);
/* 643 */         elNodes2velNodes.put(node, velNode);
/*     */         ElementNode vparent;
/* 645 */         if ((vparent = (ElementNode)elNodes2velNodes.get(parent)) != null) {
/* 646 */           vparent.forceAddChild(velNode);
/* 647 */           velNode.setParent(vparent);
/*     */         } else {
/* 649 */           rootNodes.add(velNode);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 654 */     ElementNode[] children = node.getChildren();
/* 655 */     for (int i = 0; i < children.length; ++i)
/* 656 */       checkNode(children[i], node, elNodes2velNodes, rootNodes, isFlat);
/*     */   }
/*     */ 
/*     */   public boolean isSystemDimension() {
/* 660 */     return this.sourceDimension.isSystemDimension();
/*     */   }
/*     */ 
/*     */   public boolean isUserInfoDimension() {
/* 664 */     return this.sourceDimension.isUserInfoDimension();
/*     */   }
/*     */ 
/*     */   public final void setVirtualDefinition(Object virtualDefinition) {
/* 668 */     this.virtualDefinition = virtualDefinition;
/*     */   }
/*     */   public final Object getVirtualDefinition() {
/* 671 */     return this.virtualDefinition;
/*     */   }
/*     */ 
/*     */   private final void traverse(ElementNode node, ElementNode parent, ElementNodeVisitor visitor) {
/* 675 */     visitor.visit(node, parent);
/* 676 */     ElementNode[] children = node.getChildren();
/* 677 */     for (ElementNode child : children)
/* 678 */       traverse(child, node, visitor);
/*     */   }
/*     */ 
/*     */   public SubsetHandler getSubsetHandler() {
/* 682 */     return this.sourceDimension.getSubsetHandler();
/*     */   }
/*     */ 
/*     */   public boolean canBeModified() {
/* 686 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean canCreateChildren() {
/* 690 */     return true;
/*     */   }
/*     */ 
/*     */   public int getType() {
/* 694 */     return 0;
/*     */   }
/*     */ 
/*     */   public Hierarchy[] getHierarchies() {
/* 698 */     return this.sourceDimension.getHierarchies();
/*     */   }
/*     */ 
/*     */   public String[] getHierarchiesIds() {
/* 702 */     return this.sourceDimension.getHierarchiesIds();
/*     */   }
/*     */ 
/*     */   public Hierarchy getHierarchyAt(int index) {
/* 706 */     return this.sourceDimension.getHierarchyAt(index);
/*     */   }
/*     */ 
/*     */   public Hierarchy getHierarchyById(String id) {
/* 710 */     return this.sourceDimension.getHierarchyById(id);
/*     */   }
/*     */ 
/*     */   public int getHierarchyCount() {
/* 714 */     return this.sourceDimension.getHierarchyCount();
/*     */   }
/*     */ 
/*     */   public Hierarchy getDefaultHierarchy() {
/* 718 */     return this.sourceDimension.getDefaultHierarchy();
/*     */   }
/*     */ 
/*     */   public Hierarchy getHierarchyByName(String name) {
/* 722 */     return this.sourceDimension.getHierarchyByName(name);
/*     */   }
/*     */ 
/*     */   public final void reload(boolean doEvents) {
/* 726 */     this.sourceDimension.reload(doEvents);
/*     */   }
/*     */ 
/*     */   public final DimensionInfo getInfo() {
/* 730 */     return this.sourceDimension.getInfo();
/*     */   }
/*     */ 
/*     */   public void addElements(String[] names, int type, Element[][] children, double[][] weights)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void addElements(String[] names, int[] types, Element[][] children, double[][] weights)
/*     */   {
/*     */   }
/*     */ }

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name:     org.palo.api.impl.VirtualDimensionImpl
 * JD-Core Version:    0.5.4
 */