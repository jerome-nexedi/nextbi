/*    */package org.palo.viewapi.exceptions;

/*    */
/*    */public class OperationFailedException extends Exception
/*    */{
  /*    */private static final long serialVersionUID = -6146742614846266279L;

  /*    */
  /*    */public OperationFailedException(String msg)
  /*    */{
    /* 58 */super(msg);
    /*    */}

  /*    */
  /*    */public OperationFailedException(String msg, Throwable cause) {
    /* 62 */super(msg, cause);
    /*    */}
  /*    */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi
 * .jar Qualified Name: org.palo.viewapi.exceptions.OperationFailedException
 * JD-Core Version: 0.5.4
 */