/*     */ package org.palo.api;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ 
/*     */ public class ElementNode
/*     */ {
/*     */   protected final Element element;
/*     */   private final Consolidation consolidation;
/*     */   private int index;
/*     */   private String name;
/*     */   private ElementNode parent;
/*     */   protected ArrayList<ElementNode> children;
/*     */ 
/*     */   public ElementNode(Element element)
/*     */   {
/*  71 */     this(element, null);
/*     */   }
/*     */ 
/*     */   public ElementNode(Element element, Consolidation consolidation)
/*     */   {
/*  81 */     this(element, consolidation, -1);
/*     */   }
/*     */ 
/*     */   public ElementNode(Element element, Consolidation consolidation, int index)
/*     */   {
/*  92 */     this.element = element;
/*  93 */     this.consolidation = consolidation;
/*  94 */     this.index = index;
/*     */ 
/*  96 */     this.children = new ArrayList(1);
/*  97 */     setName(element.getName());
/*     */   }
/*     */ 
/*     */   public final String getName()
/*     */   {
/* 103 */     return this.name;
/*     */   }
/*     */   public final void setName(String name) {
/* 106 */     this.name = name;
/*     */   }
/*     */ 
/*     */   public final int getIndex()
/*     */   {
/* 114 */     return this.index;
/*     */   }
/*     */ 
/*     */   public final Element getElement()
/*     */   {
/* 122 */     return this.element;
/*     */   }
/*     */ 
/*     */   public final Consolidation getConsolidation()
/*     */   {
/* 131 */     return this.consolidation;
/*     */   }
/*     */ 
/*     */   public final synchronized void setParent(ElementNode parent)
/*     */   {
/* 136 */     this.parent = parent;
/*     */   }
/*     */ 
/*     */   public final synchronized void setChildren(ElementNode[] kids) {
/* 140 */     if (kids == null) {
/* 141 */       return;
/*     */     }
/* 143 */     for (ElementNode k : kids)
/* 144 */       forceAddChild(k);
/*     */   }
/*     */ 
/*     */   public final synchronized ElementNode getParent()
/*     */   {
/* 156 */     return this.parent;
/*     */   }
/*     */ 
/*     */   public final synchronized int getDepth()
/*     */   {
/* 166 */     if (this.parent == null)
/* 167 */       return 0;
/* 168 */     return 1 + this.parent.getDepth();
/*     */   }
/*     */ 
/*     */   public final synchronized void addChild(ElementNode child)
/*     */   {
/* 183 */     if (!this.children.contains(child)) {
/* 184 */       this.children.add(child);
/* 185 */       child.setParent(this);
/* 186 */       if (child.index == -1)
/* 187 */         child.index = (this.children.size() - 1);
/*     */     }
/*     */   }
/*     */ 
/*     */   public final void forceAddChild(ElementNode child) {
/* 192 */     this.children.add(child);
/* 193 */     child.setParent(this);
/* 194 */     if (child.index == -1)
/* 195 */       child.index = (this.children.size() - 1);
/*     */   }
/*     */ 
/*     */   public final void forceAddChild(ElementNode child, int index) {
/* 199 */     this.children.add(index, child);
/* 200 */     child.setParent(this);
/* 201 */     if (child.index == -1)
/* 202 */       child.index = index;
/*     */   }
/*     */ 
/*     */   public final int indexOf(ElementNode child) {
/* 206 */     return this.children.indexOf(child);
/*     */   }
/*     */ 
/*     */   public final synchronized void removeChild(ElementNode child)
/*     */   {
/* 218 */     if (this.children.remove(child))
/* 219 */       child.setParent(null);
/*     */   }
/*     */ 
/*     */   public final synchronized void removeChildren()
/*     */   {
/* 230 */     Iterator allChildren = this.children.iterator();
/* 231 */     while (allChildren.hasNext()) {
/* 232 */       ElementNode child = (ElementNode)allChildren.next();
/* 233 */       child.setParent(null);
/* 234 */       allChildren.remove();
/*     */     }
/*     */   }
/*     */ 
/*     */   public synchronized ElementNode[] getChildren()
/*     */   {
/* 250 */     return (ElementNode[])this.children.toArray(
/* 251 */       new ElementNode[this.children.size()]);
/*     */   }
/*     */ 
/*     */   public int getChildCount() {
/* 255 */     return this.children.size();
/*     */   }
/*     */ 
/*     */   public synchronized boolean hasChildren()
/*     */   {
/* 264 */     return this.children.size() > 0;
/*     */   }
/*     */ 
/*     */   public final String toString()
/*     */   {
/* 272 */     return "ElementNode (" + Integer.toHexString(System.identityHashCode(this)) + "/" + Integer.toHexString(hashCode()) + " " + this.element.toString() + ")";
/*     */   }
/*     */ 
/*     */   public final boolean equals(Object obj)
/*     */   {
/* 277 */     if (!(obj instanceof ElementNode)) {
/* 278 */       return false;
/*     */     }
/* 280 */     ElementNode other = (ElementNode)obj;
/*     */ 
/* 282 */     boolean eq = this.element.equals(other.getElement());
/* 283 */     if ((this.parent != null) && (other.getParent() != null))
/*     */     {
/* 285 */       eq &= this.parent.getElement().equals(other.getParent().getElement());
/*     */     }
/*     */     else
/*     */     {
/* 289 */       eq &= ((this.parent == null) && (other.getParent() == null));
/*     */     }
/*     */ 
/* 292 */     if ((this.index != -1) && (other.index != -1))
/*     */     {
/* 294 */       eq &= this.index == other.index;
/*     */     }
/*     */ 
/* 297 */     return eq;
/*     */   }
/*     */ 
/*     */   public final int hashCode() {
/* 301 */     int hc = 3;
/* 302 */     hc += 3 * this.element.hashCode();
/* 303 */     if (this.parent != null)
/* 304 */       hc += 3 * this.parent.hashCode();
/* 305 */     if (this.index != -1) {
/* 306 */       hc += 3 * this.index;
/*     */     }
/* 308 */     return hc;
/*     */   }
/*     */ }

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name:     org.palo.api.ElementNode
 * JD-Core Version:    0.5.4
 */