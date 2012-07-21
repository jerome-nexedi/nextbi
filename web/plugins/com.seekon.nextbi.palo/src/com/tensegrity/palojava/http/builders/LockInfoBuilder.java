/*    */package com.tensegrity.palojava.http.builders;

/*    */
/*    */import com.tensegrity.palojava.LockInfo; /*    */
import com.tensegrity.palojava.PaloException; /*    */
import com.tensegrity.palojava.PaloInfo; /*    */
import com.tensegrity.palojava.impl.LockInfoImpl;

/*    */
/*    */public class LockInfoBuilder
/*    */{
  /*    */public final LockInfo create(PaloInfo parent, String[] response)
  /*    */{
    /* 57 */if (response.length < 4)
      /* 58 */throw new PaloException("Not enough information to create LockInfo!!");
    /*    */try
    /*    */{
      /* 61 */String id = response[0];
      /* 62 */String user = response[2];
      /* 63 */LockInfoImpl lock = new LockInfoImpl(id, user);
      /* 64 */setArea(lock, response[1]);
      /* 65 */if (response[3].length() > 0)
        /* 66 */lock.setSteps(Integer.parseInt(response[3]));
      /* 67 */return lock;
      /*    */} catch (RuntimeException e) {
      /* 69 */throw new PaloException(e.getLocalizedMessage(), e);
      /*    */}
    /*    */}

  /*    */
  /*    */private final void setArea(LockInfoImpl lock, String response) {
    /* 74 */String[] elements = response.split(",");
    /* 75 */String[][] area = new String[elements.length][];
    /* 76 */for (int i = 0; i < area.length; ++i) {
      /* 77 */area[i] = elements[i].split(":");
      /*    */}
    /* 79 */lock.setArea(area);
    /*    */}
  /*    */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.paloapi\lib\palo.jar
 * Qualified Name: com.tensegrity.palojava.http.builders.LockInfoBuilder JD-Core
 * Version: 0.5.4
 */