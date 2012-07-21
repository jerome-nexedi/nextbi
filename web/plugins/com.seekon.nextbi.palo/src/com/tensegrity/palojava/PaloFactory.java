/*    */package com.tensegrity.palojava;

/*    */
/*    */import com.tensegrity.palojava.http.HttpPaloServer;

/*    */
/*    */public class PaloFactory
/*    */{
  /* 53 */private static final PaloFactory instance = new PaloFactory();

  /*    */
  /*    */public static final PaloFactory getInstance()
  /*    */{
    /* 59 */return instance;
    /*    */}

  /*    */
  /*    */public final PaloServer createServerConnection(String host, String port,
    int srvType, int timeout)
  /*    */{
    /* 78 */switch (srvType)
    /*    */{
    /*    */case 1:
      /* 80 */
      throw new PaloException("Legacy server is not supported anymore!");
      /*    */case 2:
      /* 82 */
      return new HttpPaloServer(host, port, timeout);
      /* 83 */
    }
    throw new PaloException("Unknown server type!");
    /*    */}
  /*    */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.paloapi\lib\palo.jar
 * Qualified Name: com.tensegrity.palojava.PaloFactory JD-Core Version: 0.5.4
 */