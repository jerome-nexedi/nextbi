/*     */package org.palo.viewapi.exceptions;

/*     */
/*     */import org.palo.viewapi.AuthUser;
import org.palo.viewapi.DomainObject;
import org.palo.viewapi.Right;

/*     */
/*     */public class NoPermissionException extends RuntimeException
/*     */{
  /*     */private static final long serialVersionUID = 4210538360106309250L;

  /*     */private final AuthUser user;

  /*     */private final DomainObject forObj;

  /*     */private Right reqRight;

  /*     */
  /*     */public NoPermissionException(String msg, DomainObject forObj,
    AuthUser user)
  /*     */{
    /* 78 */super(msg);
    /* 79 */this.forObj = forObj;
    /* 80 */this.user = user;
    /*     */}

  /*     */
  /*     */public NoPermissionException(String msg, DomainObject forObj,
    AuthUser user, Right reqRight)
  /*     */{
    /* 94 */super(msg);
    /* 95 */this.forObj = forObj;
    /* 96 */this.user = user;
    /* 97 */this.reqRight = reqRight;
    /*     */}

  /*     */
  /*     */public final AuthUser getUser()
  /*     */{
    /* 106 */return this.user;
    /*     */}

  /*     */
  /*     */public final DomainObject getGuardedObject()
  /*     */{
    /* 115 */return this.forObj;
    /*     */}

  /*     */
  /*     */public final Right getRequiredRight()
  /*     */{
    /* 124 */return this.reqRight;
    /*     */}

  /*     */
  /*     */public final void setRequiredRight(Right right)
  /*     */{
    /* 132 */this.reqRight = right;
    /*     */}
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi
 * .jar Qualified Name: org.palo.viewapi.exceptions.NoPermissionException
 * JD-Core Version: 0.5.4
 */