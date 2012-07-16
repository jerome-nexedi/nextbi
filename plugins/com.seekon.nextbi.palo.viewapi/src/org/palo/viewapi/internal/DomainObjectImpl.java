/*    */package org.palo.viewapi.internal;

/*    */
/*    */import org.palo.viewapi.DomainObject;

/*    */
/*    */public class DomainObjectImpl
/*    */implements DomainObject
/*    */{
  /*    */private String id;

  /*    */
  /*    */public DomainObjectImpl(String id)
  /*    */{
    /* 55 */this.id = id;
    /*    */}

  /*    */
  /*    */public final String getId()
  /*    */{
    /* 60 */return this.id;
    /*    */}

  /*    */
  /*    */public final void setId(String id) {
    /* 64 */if ((id == null) || (id.length() < 1))
      /* 65 */throw new IllegalArgumentException("An id cannot be null or empty");
    /* 66 */this.id = id;
    /*    */}
  /*    */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi
 * .jar Qualified Name: org.palo.viewapi.internal.DomainObjectImpl JD-Core
 * Version: 0.5.4
 */