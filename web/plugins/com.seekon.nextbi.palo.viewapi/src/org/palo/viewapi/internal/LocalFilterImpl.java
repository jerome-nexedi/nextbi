/*     */package org.palo.viewapi.internal;

/*     */
/*     */import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.palo.api.Element;
import org.palo.api.ElementNode;
import org.palo.viewapi.LocalFilter;

/*     */
/*     */public class LocalFilterImpl
/*     */implements LocalFilter
/*     */{
  /* 53 */private final List<ElementNode> visibleRoots = new ArrayList();

  /*     */
  /*     */public void addVisibleElement(ElementNode rootNode) {
    /* 56 */this.visibleRoots.add(rootNode);
    /*     */}

  /*     */
  /*     */public void addVisibleElement(ElementNode rootNode, int atIndex) {
    /* 60 */this.visibleRoots.add(atIndex, rootNode);
    /*     */}

  /*     */
  /*     */private final void removeVisibleElement(ElementNode[] roots,
    ElementNode node) {
    /* 64 */if (roots == null) {
      /* 65 */return;
      /*     */}
    /* 67 */for (ElementNode e : roots) {
      /* 68 */if (e.equals(node)) {
        /* 69 */if (e.getParent() == null)
          /* 70 */this.visibleRoots.remove(e);
        /*     */else {
          /* 72 */e.getParent().removeChild(e);
          /*     */}
        /* 74 */return;
        /*     */}
      /* 76 */removeVisibleElement(e.getChildren(), node);
      /*     */}
    /*     */}

  /*     */
  /*     */private final ElementNode findElementNode(ElementNode[] roots,
    ElementNode node) {
    /* 81 */if (roots == null) {
      /* 82 */return null;
      /*     */}
    /* 84 */for (ElementNode e : roots) {
      /* 85 */if (e.equals(node)) {
        /* 86 */return e;
        /*     */}
      /* 88 */ElementNode result = findElementNode(e.getChildren(), node);
      /* 89 */if (result != null) {
        /* 90 */return result;
        /*     */}
      /*     */}
    /* 93 */return null;
    /*     */}

  /*     */
  /*     */public ElementNode findElementNode(ElementNode node) {
    /* 97 */return findElementNode((ElementNode[]) this.visibleRoots
      .toArray(new ElementNode[0]), node);
    /*     */}

  /*     */
  /*     */public void removeVisibleElement(ElementNode rootNode) {
    /* 101 */removeVisibleElement((ElementNode[]) this.visibleRoots
      .toArray(new ElementNode[0]), rootNode);
    /*     */}

  /*     */
  /*     */public void clear() {
    /* 105 */this.visibleRoots.clear();
    /*     */}

  /*     */
  /*     */public ElementNode[] getVisibleElements() {
    /* 109 */return (ElementNode[]) this.visibleRoots.toArray(new ElementNode[0]);
    /*     */}

  /*     */
  /*     */public int indexOf(ElementNode nd) {
    /* 113 */return this.visibleRoots.indexOf(nd);
    /*     */}

  /*     */
  /*     */public boolean hasVisibleElements() {
    /* 117 */return !this.visibleRoots.isEmpty();
    /*     */}

  /*     */
  /*     */public boolean isVisible(final Element element) {
    /* 121 */LocalFilterVisitor visitor = new LocalFilterVisitor()
    /*     */{
      /*     */public boolean visit(ElementNode node) {
        /* 124 */return !node.getElement().equals(element);
        /*     */}
      /*     */
    };
    /* 128 */return !traverse((ElementNode[]) this.visibleRoots
      .toArray(new ElementNode[0]), visitor);
    /*     */}

  /*     */
  /*     */public void setVisibleElements(ElementNode[] rootNodes) {
    /* 132 */this.visibleRoots.clear();
    /* 133 */this.visibleRoots.addAll(Arrays.asList(rootNodes));
    /*     */}

  /*     */
  /*     */private boolean traverse(ElementNode[] nodes, LocalFilterVisitor visitor) {
    /* 137 */boolean goOn = true;
    /* 138 */for (ElementNode node : nodes) {
      /* 139 */goOn = (goOn) && (visitor.visit(node));
      /* 140 */if (!goOn)
        break;
      /* 141 */goOn = (goOn) && (traverse(node.getChildren(), visitor));
      /*     */}
    /*     */
    /* 145 */return goOn;
    /*     */}
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi
 * .jar Qualified Name: org.palo.viewapi.internal.LocalFilterImpl JD-Core
 * Version: 0.5.4
 */