/*    */package com.tensegrity.palojava.http.builders;

/*    */
/*    */import com.tensegrity.palojava.PaloException; /*    */
import com.tensegrity.palojava.PaloInfo; /*    */
import com.tensegrity.palojava.ServerInfo; /*    */
import com.tensegrity.palojava.impl.ServerInfoImpl;

/*    */
/*    */public class ServerInfoBuilder
/*    */{
  /*    */public ServerInfo create(PaloInfo parent, String[] response)
  /*    */throws PaloException
  /*    */{
    /* 57 */if (response.length < 4)
      /* 58 */throw new PaloException(
        "Not enough information to create ServerInfo!!");
    /*    */try
    /*    */{
      /* 61 */int major = Integer.parseInt(response[0]);
      /* 62 */int minor = Integer.parseInt(response[1]);
      /* 63 */int bugfix = Integer.parseInt(response[2]);
      /* 64 */int build = Integer.parseInt(response[3]);
      /* 65 */int encryption = 2;
      /* 66 */int httpsPort = 0;
      /* 67 */if (response.length > 4)
        encryption = Integer.parseInt(response[4]);
      /* 68 */if (response.length > 5)
        httpsPort = Integer.parseInt(response[5]);
      /* 69 */return new ServerInfoImpl(build, bugfix, major, minor, httpsPort,
        encryption, false);
      /*    */} catch (Exception e) {
      /* 71 */throw new PaloException(e.getLocalizedMessage(), e);
      /*    */}
    /*    */}
  /*    */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.paloapi\lib\palo.jar
 * Qualified Name: com.tensegrity.palojava.http.builders.ServerInfoBuilder
 * JD-Core Version: 0.5.4
 */