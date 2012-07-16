/*     */package org.palo.api.subsets.filter;

/*     */
/*     */import java.util.Comparator; /*     */
import org.palo.api.Element; /*     */
import org.palo.api.ElementNode;

/*     */
/*     */class DefinitionSorter
/*     */implements Comparator<ElementNode>
/*     */{
  /*     */public final int compare(ElementNode node1, ElementNode node2)
  /*     */{
    /* 495 */int pos1 = node1.getElement().getPosition();
    /* 496 */int pos2 = node2.getElement().getPosition();
    /* 497 */if (pos1 > pos2)
      /* 498 */return 1;
    /* 499 */if (pos1 < pos2) {
      /* 500 */return -1;
      /*     */}
    /* 502 */return 0;
    /*     */}
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name: org.palo.api.subsets.filter.DefinitionSorter JD-Core Version:
 * 0.5.4
 */