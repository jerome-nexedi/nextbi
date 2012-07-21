/*     */package org.palo.api.ext.subsets.impl;

/*     */
/*     */import java.util.ArrayList; /*     */
import java.util.Arrays; /*     */
import java.util.Collections; /*     */
import java.util.Comparator; /*     */
import java.util.HashSet; /*     */
import java.util.List; /*     */
import java.util.Set; /*     */
import org.palo.api.Element; /*     */
import org.palo.api.ElementNode; /*     */
import org.palo.api.Hierarchy; /*     */
import org.palo.api.HierarchyFilter; /*     */
import org.palo.api.SubsetState;

/*     */
/*     */class FlatStateFilter
/*     */implements HierarchyFilter
/*     */{
  /*     */private Hierarchy hierarchy;

  /*     */private final SubsetState subsetState;

  /*     */private final Set elementSet;

  /*     */
  /*     */FlatStateFilter(SubsetState subsetState)
  /*     */{
    /* 200 */this.subsetState = subsetState;
    /* 201 */this.elementSet = new HashSet();
    /* 202 */this.elementSet
      .addAll(Arrays.asList(subsetState.getVisibleElements()));
    /*     */}

  /*     */
  /*     */public void init(Hierarchy hierarchy) {
    /* 206 */this.hierarchy = hierarchy;
    /*     */}

  /*     */
  /*     */public boolean acceptElement(Element element) {
    /* 210 */return this.elementSet.contains(element);
    /*     */}

  /*     */
  /*     */public boolean isFlat() {
    /* 214 */return true;
    /*     */}

  /*     */
  /*     */final void collectRootNodes(List out)
  /*     */{
    /* 219 */Element[] visElements = this.subsetState.getVisibleElements();
    /* 220 */for (int i = 0; i < visElements.length; ++i)
    /*     */{
      /* 225 */Element element = visElements[i];
      /*     */
      /* 227 */int[] positions = this.subsetState.getPositions(element);
      /* 228 */if (positions.length == 0)
      /*     */{
        /* 230 */ElementNode node = new ElementNode(element, null);
        /* 231 */out.add(node);
        /*     */} else {
        /* 233 */for (int j = 0; j < positions.length; ++j) {
          /* 234 */ElementNode node = new ElementNode(element, null,
          /* 235 */positions[j]);
          /* 236 */out.add(node);
          /*     */}
        /*     */}
      /*     */}
    /*     */
    /* 241 */Collections.sort(out, new Comparator()
    /*     */{
      /*     */public int compare(Object o1, Object o2) {
        /* 244 */ElementNode n1 = (ElementNode) o1;
        /* 245 */ElementNode n2 = (ElementNode) o2;
        /* 246 */if ((n1 == null) || (n2 == null))
          /* 247 */return 0;
        /* 248 */return n1.getIndex() - n2.getIndex();
        /*     */}
      /*     */
    });
    /*     */}

  /*     */
  /*     */public ElementNode[] postprocessRootNodes(ElementNode[] rootNodes)
  /*     */{
    /* 256 */ArrayList nodes = new ArrayList();
    /* 257 */Element[] visElements = this.subsetState.getVisibleElements();
    /* 258 */for (int i = 0; i < visElements.length; ++i) {
      /* 259 */String name = visElements[i].getName();
      /* 260 */Element element = this.hierarchy.getElementByName(name);
      /* 261 */if (element == null) {
        /*     */continue;
        /*     */}
      /* 264 */int[] positions = this.subsetState.getPositions(element);
      /* 265 */if (positions.length == 0)
      /*     */{
        /* 267 */ElementNode node = new ElementNode(element, null);
        /* 268 */nodes.add(node);
        /*     */} else {
        /* 270 */for (int j = 0; j < positions.length; ++j) {
          /* 271 */ElementNode node = new ElementNode(element, null,
          /* 272 */positions[j]);
          /* 273 */nodes.add(node);
          /*     */}
        /*     */}
      /*     */}
    /*     */
    /* 278 */Collections.sort(nodes, new Comparator()
    /*     */{
      /*     */public int compare(Object o1, Object o2) {
        /* 281 */ElementNode n1 = (ElementNode) o1;
        /* 282 */ElementNode n2 = (ElementNode) o2;
        /* 283 */if ((n1 == null) || (n2 == null))
          /* 284 */return 0;
        /* 285 */return n1.getIndex() - n2.getIndex();
        /*     */}
      /*     */
    });
    /* 289 */return (ElementNode[]) nodes.toArray(new ElementNode[nodes.size()]);
    /*     */}
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name: org.palo.api.ext.subsets.impl.FlatStateFilter JD-Core
 * Version: 0.5.4
 */