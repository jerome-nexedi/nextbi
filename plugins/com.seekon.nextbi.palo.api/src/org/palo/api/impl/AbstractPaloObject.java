/*    */package org.palo.api.impl;

/*    */
/*    */import com.tensegrity.palojava.PaloInfo; /*    */
import org.palo.api.PaloObject;

/*    */
/*    */abstract class AbstractPaloObject
/*    */implements PaloObject
/*    */{
  /*    */protected final int getType(PaloInfo info)
  /*    */{
    /* 42 */switch (info.getType())
    /*    */{
    /*    */case 2:
      /* 44 */
      return 8;
      /*    */case 3:
      /* 46 */
      return 16;
      /*    */case 0:
      /* 48 */
      return 2;
      /*    */case 1:
      /* 50 */
      return 4;
      /*    */case 4:
      /* 52 */
      return 32;
      /*    */
    }
    /* 54 */return -1;
    /*    */}

  /*    */
  /*    */protected final int getInfoType(int type) {
    /* 58 */switch (type)
    /*    */{
    /*    */case 8:
      /* 60 */
      return 2;
      /*    */case 2:
      /* 62 */
      return 0;
      /*    */case 4:
      /* 64 */
      return 1;
      /*    */case 16:
      /* 66 */
      return 3;
      /*    */case 32:
      /* 68 */
      return 4;
      /*    */
    }
    /* 70 */return -1;
    /*    */}
  /*    */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name: org.palo.api.impl.AbstractPaloObject JD-Core Version: 0.5.4
 */