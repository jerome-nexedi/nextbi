/*     */package org.palo.api.subsets.filter;

/*     */
/*     */import java.util.Comparator; /*     */
import org.palo.api.Element; /*     */
import org.palo.api.ElementNode;

/*     */
/*     */class DataSorter
/*     */implements Comparator<ElementNode>
/*     */{
  /*     */private final DataFilter dataFilter;

  /*     */
  /*     */DataSorter(DataFilter dataFilter)
  /*     */{
    /* 438 */this.dataFilter = dataFilter;
    /*     */}

  /*     */
  /*     */public int compare(ElementNode o1, ElementNode o2) {
    /* 442 */Element e1 = o1.getElement();
    /* 443 */Element e2 = o2.getElement();
    /*     */try {
      /* 445 */double d1 = Double.parseDouble(this.dataFilter.getValue(e1));
      /* 446 */double d2 = Double.parseDouble(this.dataFilter.getValue(e2));
      /*     */
      /* 448 */return Double.compare(d1, d2);
      /*     */}
    /*     */catch (NumberFormatException localNumberFormatException)
    /*     */{
      /*     */}
    /* 453 */return this.dataFilter.getValue(e1).compareTo(
      this.dataFilter.getValue(e2));
    /*     */}
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name: org.palo.api.subsets.filter.DataSorter JD-Core Version: 0.5.4
 */