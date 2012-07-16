/*     */ package org.palo.api;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.LinkedHashSet;
/*     */ 
/*     */ public class MyTest
/*     */ {
/*     */   Element[] e;
/*     */ 
/*     */   public final ElementNode[] getElementsTree()
/*     */   {
/* 168 */     Element[] roots = { this.e[0], this.e[1], this.e[2], this.e[3] };
/*     */ 
/* 170 */     final ArrayList rootnodes = new ArrayList();
/* 171 */     ElementNodeVisitor visitor = new ElementNodeVisitor() {
/*     */       public void visit(MyTest.ElementNode node, MyTest.ElementNode parent) {
/* 173 */         if (parent == null)
/* 174 */           rootnodes.add(node);
/*     */       }
/*     */     };
/* 177 */     if (roots != null) {
/* 178 */       for (int i = 0; i < roots.length; ++i) {
/* 179 */         ElementNode rootNode = new ElementNode(roots[i]);
/* 180 */         traverse(rootNode, visitor);
/*     */       }
/*     */     }
/* 183 */     return (ElementNode[])rootnodes.toArray(new ElementNode[0]);
/*     */   }
/*     */ 
/*     */   public void traverse(Element e, ElementVisitor v) {
/* 187 */     traverse(e, null, v);
/*     */   }
/*     */ 
/*     */   void traverse(Element e, Element p, ElementVisitor v) {
/* 191 */     v.visit(e, p);
/* 192 */     Element[] children = e.getChildren();
/* 193 */     if (children == null)
/* 194 */       return;
/* 195 */     for (int i = 0; i < children.length; ++i)
/* 196 */       traverse(children[i], e, v);
/*     */   }
/*     */ 
/*     */   public void traverse(ElementNode n, ElementNodeVisitor v)
/*     */   {
/* 203 */     traverse(n, null, v);
/*     */   }
/*     */ 
/*     */   void traverse(ElementNode n, ElementNode p, ElementNodeVisitor v)
/*     */   {
/* 208 */     v.visit(n, p);
/* 209 */     Element[] children = n.getElement().getChildren();
/* 210 */     if (children == null)
/* 211 */       return;
/* 212 */     for (int i = 0; i < children.length; ++i)
/*     */     {
/* 214 */       if (children[i] == null)
/*     */         continue;
/* 216 */       ElementNode child = new ElementNode(children[i]);
/* 217 */       n.addChild(child);
/* 218 */       traverse(child, n, v);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void trav(ElementNode[] nodes) {
/* 223 */     if (nodes == null) {
/* 224 */       return;
/*     */     }
/* 226 */     for (ElementNode n : nodes) {
/* 227 */       if (n.getElement().getId().equals("e3")) {
/* 228 */         for (ElementNode nn : n.getChildren()) {
/* 229 */           n.removeChild(nn);
/*     */         }
/*     */       }
/* 232 */       trav(n.getChildren());
/*     */     }
/*     */   }
/*     */ 
/*     */   MyTest() {
/* 237 */     this.e = new Element[4];
/*     */ 
/* 239 */     for (int i = 0; i < 4; ++i) {
/* 240 */       this.e[i] = new Element("localhost", "palo", "d1", "e" + i);
/*     */     }
/* 242 */     this.e[3].addChild(this.e[1]);
/* 243 */     this.e[3].addChild(this.e[2]);
/* 244 */     ElementNode[] nodes = getElementsTree();
/* 245 */     for (ElementNode n : nodes)
/* 246 */       if (n.getElement().getId().equals("e3"))
/* 247 */         for (ElementNode nn : n.getChildren())
/* 248 */           n.removeChild(nn);
/*     */   }
/*     */ 
/*     */   public static void main(String[] args)
/*     */   {
/* 256 */     new MyTest();
/*     */   }
/*     */ 
/*     */   class Element
/*     */   {
/*     */     private final Object[] objs;
/*     */     private ArrayList<Element> kids;
/*     */ 
/*     */     Element(String con, String db, String dim, String id)
/*     */     {
/*  53 */       this.objs = new Object[] { Element.class, con, db, dim, id };
/*  54 */       this.kids = new ArrayList();
/*     */     }
/*     */ 
/*     */     public String getId() {
/*  58 */       return (String)this.objs[4];
/*     */     }
/*     */ 
/*     */     public int hashCode() {
/*  62 */       int hc = 23;
/*  63 */       for (int i = 0; i < this.objs.length; ++i) {
/*  64 */         if (this.objs[i] != null)
/*  65 */           hc += 37 * this.objs[i].hashCode();
/*     */       }
/*  67 */       return hc;
/*     */     }
/*     */ 
/*     */     public Element[] getChildren() {
/*  71 */       return (Element[])this.kids.toArray(new Element[0]);
/*     */     }
/*     */ 
/*     */     public final boolean equals(Object other) {
/*  75 */       if (other instanceof Element) {
/*  76 */         Element e = (Element)other;
/*  77 */         if (this.objs.length == e.objs.length) {
/*  78 */           for (int i = 0; i < this.objs.length; ++i) {
/*  79 */             if (!this.objs[i].equals(e.objs[i])) {
/*  80 */               return false;
/*     */             }
/*     */           }
/*  83 */           return true;
/*     */         }
/*     */       }
/*  86 */       return false;
/*     */     }
/*     */ 
/*     */     public void addChild(Element e) {
/*  90 */       this.kids.add(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   class ElementNode {
/*     */     private ElementNode parent;
/*     */     private final MyTest.Element element;
/*     */     private final LinkedHashSet<ElementNode> children;
/*     */ 
/*     */     ElementNode(MyTest.Element element) {
/* 100 */       this.element = element;
/* 101 */       this.children = new LinkedHashSet();
/*     */     }
/*     */ 
/*     */     public MyTest.Element getElement() {
/* 105 */       return this.element;
/*     */     }
/*     */ 
/*     */     ElementNode getParent() {
/* 109 */       return this.parent;
/*     */     }
/*     */ 
/*     */     public void setParent(ElementNode newParent) {
/* 113 */       this.parent = newParent;
/*     */     }
/*     */ 
/*     */     public final synchronized void addChild(ElementNode child)
/*     */     {
/* 118 */       if (!this.children.contains(child)) {
/* 119 */         child.setParent(this);
/* 120 */         this.children.add(child);
/*     */       }
/*     */     }
/*     */ 
/*     */     public ElementNode[] getChildren() {
/* 125 */       return (ElementNode[])this.children.toArray(new ElementNode[0]);
/*     */     }
/*     */ 
/*     */     public final synchronized void removeChild(ElementNode child)
/*     */     {
/* 130 */       if (this.children.remove(child))
/* 131 */         child.setParent(null);
/*     */     }
/*     */ 
/*     */     public final boolean equals(Object obj)
/*     */     {
/* 137 */       if (!(obj instanceof ElementNode)) {
/* 138 */         return false;
/*     */       }
/* 140 */       ElementNode other = (ElementNode)obj;
/*     */ 
/* 142 */       boolean eq = this.element.equals(other.getElement());
/* 143 */       if ((this.parent != null) && (other.getParent() != null))
/*     */       {
/* 145 */         eq &= this.parent.getElement().equals(other.getParent().getElement());
/*     */       }
/*     */       else
/*     */       {
/* 149 */         eq &= ((this.parent == null) && (other.getParent() == null));
/*     */       }
/*     */ 
/* 152 */       return eq;
/*     */     }
/*     */ 
/*     */     public final int hashCode() {
/* 156 */       int hc = 3;
/* 157 */       hc += 3 * this.element.hashCode();
/* 158 */       if (this.parent != null) {
/* 159 */         hc += 3 * this.parent.hashCode();
/*     */       }
/* 161 */       return hc;
/*     */     }
/*     */   }
/*     */ 
/*     */   static abstract interface ElementNodeVisitor
/*     */   {
/*     */     public abstract void visit(MyTest.ElementNode paramElementNode1, MyTest.ElementNode paramElementNode2);
/*     */   }
/*     */ 
/*     */   public static abstract interface ElementVisitor
/*     */   {
/*     */     public abstract void visit(MyTest.Element paramElement1, MyTest.Element paramElement2);
/*     */   }
/*     */ }

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name:     org.palo.api.MyTest
 * JD-Core Version:    0.5.4
 */