/*    */package com.tensegrity.palojava.http.builders;

/*    */
/*    */import com.tensegrity.palojava.DatabaseInfo; /*    */
import com.tensegrity.palojava.PaloException; /*    */
import com.tensegrity.palojava.PaloInfo; /*    */
import com.tensegrity.palojava.impl.DatabaseInfoImpl;

/*    */
/*    */public class DatabaseInfoBuilder
/*    */{
  /*    */public final DatabaseInfo create(PaloInfo parent, String[] response)
  /*    */{
    /* 47 */if (response.length < 6) {
      /* 48 */throw new PaloException(
        "Not enough information to create DatabaseInfo!!");
      /*    */}
    /*    */
    /* 51 */String id = response[0];
    /* 52 */int type = Integer.parseInt(response[5]);
    /* 53 */DatabaseInfoImpl info = new DatabaseInfoImpl(id, type);
    /* 54 */update(info, response);
    /* 55 */return info;
    /*    */}

  /*    */
  /*    */public final void update(DatabaseInfoImpl database, String[] response)
  /*    */{
    /* 62 */if (response.length == 4) {
      /* 63 */throw new PaloException(response[0],
        response[2] + ": " + response[3],
        /* 64 */response[2] + ": " + response[3]);
      /*    */}
    /* 66 */if (response.length < 6)
      /* 67 */throw new PaloException(
        "Not enough information to update DatabaseInfo!!");
    /*    */try
    /*    */{
      /* 70 */String name = response[1];
      /* 71 */int dimCount = Integer.parseInt(response[2]);
      /* 72 */int cubeCount = Integer.parseInt(response[3]);
      /* 73 */int status = Integer.parseInt(response[4]);
      /* 74 */int token = Integer.parseInt(response[6]);
      /*    */
      /* 76 */database.setName(name);
      /* 77 */database.setDimensionCount(dimCount);
      /* 78 */database.setCubeCount(cubeCount);
      /* 79 */database.setStatus(status);
      /* 80 */database.setToken(token);
      /*    */} catch (RuntimeException e) {
      /* 82 */throw new PaloException(e.getLocalizedMessage(), e);
      /*    */}
    /*    */}
  /*    */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.paloapi\lib\palo.jar
 * Qualified Name: com.tensegrity.palojava.http.builders.DatabaseInfoBuilder
 * JD-Core Version: 0.5.4
 */