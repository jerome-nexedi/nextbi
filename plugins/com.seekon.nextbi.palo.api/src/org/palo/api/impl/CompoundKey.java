/*    */package org.palo.api.impl;

/*    */
/*    */public class CompoundKey
/*    */{
  /*    */private final Object[] objs;

  /*    */
  /*    */public CompoundKey(Object[] objs)
  /*    */{
    /* 47 */this.objs = objs;
    /*    */}

  /*    */
  /*    */public final int hashCode()
  /*    */{
    /* 54 */int hc = 23;
    /* 55 */for (int i = 0; i < this.objs.length; ++i) {
      /* 56 */if (this.objs[i] != null)
        /* 57 */hc += 37 * this.objs[i].hashCode();
      /*    */}
    /* 59 */return hc;
    /*    */}

  /*    */
  /*    */public final boolean equals(Object obj) {
    /* 63 */if (!(obj instanceof CompoundKey)) {
      /* 64 */return false;
      /*    */}
    /* 66 */CompoundKey other = (CompoundKey) obj;
    /*    */
    /* 68 */if (this.objs.length != other.objs.length) {
      /* 69 */return false;
      /*    */}
    /* 71 */for (int i = 0; i < this.objs.length; ++i) {
      /* 72 */if ((this.objs[i] == null) && (other.objs[i] != null)) {
        /* 73 */return false;
        /*    */}
      /* 75 */if (!this.objs[i].equals(other.objs[i])) {
        /* 76 */return false;
        /*    */}
      /*    */}
    /* 79 */return true;
    /*    */}
  /*    */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name: org.palo.api.impl.CompoundKey JD-Core Version: 0.5.4
 */