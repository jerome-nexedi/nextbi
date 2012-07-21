/*     */package org.palo.api.subsets.io;

/*     */
/*     */import java.util.Comparator; /*     */
import java.util.HashMap; /*     */
import org.palo.api.Element; /*     */
import org.palo.api.Hierarchy;

/*     */
/*     */class ElementComparator
/*     */implements Comparator<Element>
/*     */{
  /* 237 */private final HashMap<String, Integer> lookup = new HashMap();

  /*     */
  /*     */public ElementComparator(Hierarchy hierarchy) {
    /* 240 */fillLookUpTable(hierarchy);
    /*     */}

  /*     */public int compare(Element o1, Element o2) {
    /* 243 */if ((o1 != null) && (o2 != null)) {
      /* 244 */int p1 = ((Integer) this.lookup.get(o1.getName())).intValue();
      /* 245 */int p2 = ((Integer) this.lookup.get(o2.getName())).intValue();
      /* 246 */if (p1 < p2)
        /* 247 */return -1;
      /* 248 */if (p1 > p2)
        /* 249 */return 1;
      /*     */}
    /* 251 */return 0;
    /*     */}

  /*     */
  /*     */private final void fillLookUpTable(Hierarchy hierarchy) {
    /* 255 */this.lookup.clear();
    /* 256 */Element[] elInOrder = hierarchy.getElementsInOrder();
    /* 257 */for (int i = 0; i < elInOrder.length; ++i)
      /* 258 */this.lookup.put(elInOrder[i].getName(), new Integer(i));
    /*     */}
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name: org.palo.api.subsets.io.ElementComparator JD-Core Version:
 * 0.5.4
 */