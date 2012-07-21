/*    */package org.palo.api.impl;

/*    */
/*    */import com.tensegrity.palojava.LockInfo; /*    */
import org.palo.api.Cell; /*    */
import org.palo.api.Lock;

/*    */
/*    */class LockImpl
/*    */implements Lock
/*    */{
  /*    */private final LockInfo lockInfo;

  /*    */
  /*    */LockImpl(LockInfo lockInfo)
  /*    */{
    /* 55 */this.lockInfo = lockInfo;
    /*    */}

  /*    */
  /*    */public final Cell[] getArea()
  /*    */{
    /* 61 */return null;
    /*    */}

  /*    */
  /*    */public final String getId() {
    /* 65 */return this.lockInfo.getId();
    /*    */}

  /*    */
  /*    */public final int getSteps() {
    /* 69 */return this.lockInfo.getSteps();
    /*    */}

  /*    */
  /*    */final LockInfo getInfo() {
    /* 73 */return this.lockInfo;
    /*    */}

  /*    */
  /*    */public final boolean equals(Object obj) {
    /* 77 */if (obj instanceof Lock) {
      /* 78 */Lock other = (Lock) obj;
      /* 79 */return getId().equals(other.getId());
      /*    */}
    /* 81 */return false;
    /*    */}

  /*    */
  /*    */public final int hashCode() {
    /* 85 */return this.lockInfo.hashCode();
    /*    */}
  /*    */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name: org.palo.api.impl.LockImpl JD-Core Version: 0.5.4
 */