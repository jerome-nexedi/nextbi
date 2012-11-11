/*    */package org.palo.viewapi.internal;

/*    */
/*    */import org.palo.api.Connection;
import org.palo.api.ConnectionConfiguration;
import org.palo.api.ConnectionFactory;

/*    */
/*    */public class PaloConnectionFactory
/*    */implements IConnectionFactory
/*    */{
  /*    */public Connection createConnection(String host, String service,
    String login, String password, String provider)
  /*    */{
    /* 45 */ConnectionConfiguration cc = new ConnectionConfiguration(host, service);
    /* 46 */cc.setUser(login);
    /* 47 */cc.setPassword(password);
    /* 48 */int type = (provider.equalsIgnoreCase("xmla")) ? 3 : 2;
    /* 49 */cc.setType(type);
    /* 50 */cc.setLoadOnDemand(true);
    /* 51 */cc.setTimeout(120000);
    /* 52 */Connection r = ConnectionFactory.getInstance().newConnection(cc);
    /* 53 */return r;
    /*    */}

  /*    */
  /*    */public void initialize(PaloConfiguration cfg)
  /*    */{
    /*    */}
  /*    */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi
 * .jar Qualified Name: org.palo.viewapi.internal.PaloConnectionFactory JD-Core
 * Version: 0.5.4
 */