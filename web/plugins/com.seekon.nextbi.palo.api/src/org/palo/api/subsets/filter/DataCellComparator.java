/*     */package org.palo.api.subsets.filter;

/*     */
/*     */import java.util.Comparator;

/*     */
/*     */class DataCellComparator
/*     */implements Comparator<DataCell>
/*     */{
  /*     */private final int cellOperator;

  /*     */
  /*     */public DataCellComparator(int cellOperator)
  /*     */{
    /* 566 */this.cellOperator = cellOperator;
    /*     */}

  /*     */
  /*     */public int compare(DataCell dc1, DataCell dc2)
  /*     */{
    /* 572 */Object o1 = dc1.getValue(this.cellOperator);
    /* 573 */Object o2 = dc2.getValue(this.cellOperator);
    /*     */
    /* 575 */if ((o1 instanceof Double) && (o2 instanceof Double)) {
      /* 576 */Double d1 = (Double) o1;
      /* 577 */Double d2 = (Double) o2;
      /* 578 */return d1.compareTo(d2);
      /*     */}
    /* 580 */String s1 = o1.toString();
    /* 581 */String s2 = o2.toString();
    /* 582 */return s1.compareTo(s2);
    /*     */}
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name: org.palo.api.subsets.filter.DataCellComparator JD-Core
 * Version: 0.5.4
 */