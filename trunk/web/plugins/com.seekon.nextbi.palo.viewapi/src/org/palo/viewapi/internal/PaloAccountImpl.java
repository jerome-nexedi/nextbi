/*    */package org.palo.viewapi.internal;

/*    */
/*    */import org.palo.api.Connection;
import org.palo.api.ConnectionConfiguration;
import org.palo.api.ConnectionFactory;
import org.palo.viewapi.PaloAccount;
import org.palo.viewapi.PaloConnection;

/*    */
/*    */public class PaloAccountImpl extends AccountImpl
/*    */implements PaloAccount
/*    */{
  /*    */PaloAccountImpl(String id, String user)
  /*    */{
    /* 42 */super(id, user);
    /*    */}

  /*    */
  /*    */PaloAccountImpl(AccountImpl.Builder builder) {
    /* 46 */super(builder);
    /*    */}

  /*    */
  /*    */public final void logout()
  /*    */{
    /* 51 */if (this.paloConnection != null) {
      /* 52 */((Connection) this.paloConnection).disconnect();
      /*    */}
    /* 54 */this.paloConnection = null;
    /*    */}

  /*    */
  /*    */public synchronized void setConnection(Connection con) {
    /* 58 */if (isLoggedIn()) {
      /* 59 */return;
      /*    */}
    /* 61 */this.paloConnection = con;
    /*    */}

  /*    */
  /*    */public synchronized Connection login() {
    /* 65 */if (!isLoggedIn()) {
      /* 66 */PaloConnection paloConnection = getConnection();
      /*    */
      /* 68 */ConnectionConfiguration cfg = ConnectionFactory.getInstance()
      /* 69 */.getConfiguration(paloConnection.getHost(),
      /* 70 */paloConnection.getService());
      /* 71 */cfg.setUser(getLoginName());
      /* 72 */cfg.setPassword(getPassword());
      /* 73 */cfg.setType(paloConnection.getType());
      /* 74 */cfg.setLoadOnDemand(true);
      /* 75 */cfg.setTimeout(120000);
      /*    */
      /* 77 */this.paloConnection =
      /* 78 */ConnectionFactory.getInstance().newConnection(cfg);
      /*    */}
    /* 80 */return (Connection) this.paloConnection;
    /*    */}
  /*    */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi
 * .jar Qualified Name: org.palo.viewapi.internal.PaloAccountImpl JD-Core
 * Version: 0.5.4
 */