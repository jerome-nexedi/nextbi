/*    */package org.palo.viewapi.internal;

/*    */
/*    */import org.palo.viewapi.WSSAccount;

/*    */
/*    */public class WSSAccountImpl extends AccountImpl
/*    */implements WSSAccount
/*    */{
  /*    */WSSAccountImpl(String id, String user)
  /*    */{
    /* 40 */super(id, user);
    /*    */}

  /*    */
  /*    */WSSAccountImpl(AccountImpl.Builder builder) {
    /* 44 */super(builder);
    /*    */}

  /*    */
  /*    */public final void logout()
  /*    */{
    /*    */}
  /*    */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi
 * .jar Qualified Name: org.palo.viewapi.internal.WSSAccountImpl JD-Core
 * Version: 0.5.4
 */