/*     */package org.palo.viewapi.uimodels.axis;

/*     */
/*     */import java.util.Comparator; /*     */
import org.palo.api.Dimension; /*     */
import org.palo.api.utils.ElementPath;

/*     */
/*     */class ElementPathComparator
/*     */implements Comparator<ElementPath>
/*     */{
  /*     */public int compare(ElementPath o1, ElementPath o2)
  /*     */{
    /* 614 */Dimension[] dim1 = o1.getDimensions();
    /* 615 */Dimension[] dim2 = o2.getDimensions();
    /* 616 */int dimCount1 = dim1.length;
    /* 617 */int dimCount2 = dim2.length;
    /* 618 */if (dimCount1 < dimCount2)
      /* 619 */return -1;
    /* 620 */if (dimCount1 > dimCount2) {
      /* 621 */return 1;
      /*     */}
    /* 623 */for (int d = 0; d < dimCount1; ++d) {
      /* 624 */int elCount1 = o1.getPart(dim1[d]).length;
      /* 625 */int elCount2 = o2.getPart(dim2[d]).length;
      /* 626 */if (elCount1 < elCount2)
        /* 627 */return -1;
      /* 628 */if (elCount1 > elCount2)
        /* 629 */return 1;
      /*     */}
    /* 631 */return 0;
    /*     */}
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi
 * .jar Qualified Name: org.palo.viewapi.uimodels.axis.ElementPathComparator
 * JD-Core Version: 0.5.4
 */