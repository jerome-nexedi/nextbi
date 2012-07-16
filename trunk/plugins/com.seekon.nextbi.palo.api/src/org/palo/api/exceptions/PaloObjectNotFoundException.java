/*    */package org.palo.api.exceptions;

/*    */
/*    */import org.palo.api.PaloAPIException;

/*    */
/*    */public class PaloObjectNotFoundException extends PaloAPIException
/*    */{
  /*    */private static final long serialVersionUID = -7046454439997280322L;

  /*    */
  /*    */public PaloObjectNotFoundException(String msg)
  /*    */{
    /* 56 */super(msg);
    /*    */}

  /*    */
  /*    */public PaloObjectNotFoundException(String msg, Throwable cause)
  /*    */{
    /* 61 */super(msg, cause);
    /*    */}
  /*    */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name: org.palo.api.exceptions.PaloObjectNotFoundException JD-Core
 * Version: 0.5.4
 */