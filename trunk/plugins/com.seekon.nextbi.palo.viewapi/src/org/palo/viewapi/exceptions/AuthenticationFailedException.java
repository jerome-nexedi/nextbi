/*    */package org.palo.viewapi.exceptions;

/*    */
/*    */public class AuthenticationFailedException extends Exception
/*    */{
  /*    */private static final long serialVersionUID = 200805291800L;

  /*    */
  /*    */public AuthenticationFailedException(String msg)
  /*    */{
    /* 54 */super(msg);
    /*    */}

  /*    */
  /*    */public AuthenticationFailedException(String msg, Throwable cause)
  /*    */{
    /* 65 */super(msg, cause);
    /*    */}
  /*    */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi
 * .jar Qualified Name:
 * org.palo.viewapi.exceptions.AuthenticationFailedException JD-Core Version:
 * 0.5.4
 */