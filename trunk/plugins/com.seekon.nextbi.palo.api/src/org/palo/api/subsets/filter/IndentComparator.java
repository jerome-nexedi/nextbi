/*    */package org.palo.api.subsets.filter;

/*    */
/*    */import org.palo.api.Element; /*    */
import org.palo.api.subsets.Subset2;

/*    */
/*    */class IndentComparator
/*    */{
  /*    */private final Subset2 subset;

  /*    */
  /*    */IndentComparator(Subset2 subset)
  /*    */{
    /* 53 */this.subset = subset;
    /*    */}

  /*    */
  /*    */public int compare(Element el, int level)
  /*    */{
    /* 65 */if (this.subset == null) {
      /* 66 */return el.getLevel();
      /*    */}
    /* 68 */int elLevel = -1;
    /*    */
    /* 70 */switch (this.subset.getIndent())
    /*    */{
    /*    */case 2:
      /* 76 */
      elLevel = el.getLevel();
      /* 77 */break;
    /*    */case 3:
      /* 79 */
      elLevel = el.getDepth();
      /* 80 */break;
    /*    */default:
      /* 82 */
      elLevel = el.getDepth() + 1;
      /*    */
    }
    /* 84 */return compare(elLevel, level);
    /*    */}

  /*    */
  /*    */private final int compare(int l1, int l2) {
    /* 88 */if (l1 < l2)
      /* 89 */return -1;
    /* 90 */if (l1 > l2)
      /* 91 */return 1;
    /* 92 */return 0;
    /*    */}
  /*    */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name: org.palo.api.subsets.filter.IndentComparator JD-Core Version:
 * 0.5.4
 */