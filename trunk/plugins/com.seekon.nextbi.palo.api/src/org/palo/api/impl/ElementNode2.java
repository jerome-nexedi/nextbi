/*    */package org.palo.api.impl;

/*    */
/*    */import java.util.ArrayList; /*    */
import org.palo.api.Consolidation; /*    */
import org.palo.api.Element; /*    */
import org.palo.api.ElementNode;

/*    */
/*    */class ElementNode2 extends ElementNode
/*    */{
  /*    */ElementNode2(Element element)
  /*    */{
    /* 58 */this(element, null);
    /*    */}

  /*    */
  /*    */ElementNode2(Element element, Consolidation consolidation)
  /*    */{
    /* 67 */this(element, consolidation, -1);
    /*    */}

  /*    */
  /*    */ElementNode2(Element element, Consolidation consolidation, int index)
  /*    */{
    /* 77 */super(element, consolidation, index);
    /*    */}

  /*    */
  /*    */public final synchronized boolean hasChildren()
  /*    */{
    /* 82 */return this.element.getChildCount() > 0;
    /*    */}

  /*    */
  /*    */public final synchronized ElementNode[] getChildren() {
    /* 86 */if ((this.element.getChildCount() > 0) && (this.children.isEmpty()))
    /*    */{
      /* 88 */Element[] _children = this.element.getChildren();
      /* 89 */Consolidation[] consolidations = this.element.getConsolidations();
      /* 90 */for (int i = 0; i < _children.length; ++i) {
        /* 91 */if (_children[i] == null)
          /*    */continue;
        /* 93 */ElementNode child = new ElementNode2(_children[i],
        /* 94 */consolidations[i]);
        /* 95 */forceAddChild(child);
        /*    */}
      /*    */}
    /* 98 */return
    /* 99 */(ElementNode[]) this.children
    /* 99 */.toArray(new ElementNode[this.children.size()]);
    /*    */}
  /*    */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name: org.palo.api.impl.ElementNode2 JD-Core Version: 0.5.4
 */