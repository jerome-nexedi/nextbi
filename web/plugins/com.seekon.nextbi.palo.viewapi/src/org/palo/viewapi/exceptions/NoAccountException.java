/*    */package org.palo.viewapi.exceptions;

/*    */
/*    */import org.palo.viewapi.PaloConnection;
import org.palo.viewapi.User;

/*    */
/*    */public class NoAccountException extends RuntimeException
/*    */{
  /*    */private static final long serialVersionUID = 827797655476450856L;

  /*    */private final User user;

  /*    */private final PaloConnection connection;

  /*    */
  /*    */public NoAccountException(User user, PaloConnection connection)
  /*    */{
    /* 60 */this(user, connection, null);
    /*    */}

  /*    */
  /*    */public NoAccountException(User user, PaloConnection connection, String msg) {
    /* 64 */super(msg);
    /* 65 */this.user = user;
    /* 66 */this.connection = connection;
    /*    */}

  /*    */
  /*    */public final User getUser()
  /*    */{
    /* 81 */return this.user;
    /*    */}

  /*    */
  /*    */public final PaloConnection getConnection()
  /*    */{
    /* 90 */return this.connection;
    /*    */}
  /*    */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi
 * .jar Qualified Name: org.palo.viewapi.exceptions.NoAccountException JD-Core
 * Version: 0.5.4
 */