/*    */package org.palo.viewapi.internal.dbmappers;

/*    */
/*    */import java.util.HashMap; /*    */
import java.util.Map; /*    */
import org.palo.viewapi.DomainObject;

/*    */
/*    */class DomainObjectCache
/*    */{
  /*    */private static final boolean USE_CACHE = false;

  /* 54 */protected final Map<String, DomainObject> cache = new HashMap();

  /*    */
  /*    */public final void clear()
  /*    */{
    /* 58 */this.cache.clear();
    /*    */}

  /*    */public final boolean contains(String id) {
    /* 61 */return false;
    /*    */}

  /*    */
  /*    */public final DomainObject get(String id)
  /*    */{
    /* 66 */return null;
    /*    */}

  /*    */
  /*    */public final void add(DomainObject domObj) {
    /*    */}

  /*    */
  /*    */public final void remove(DomainObject domObj) {
    /* 73 */this.cache.remove(domObj.getId());
    /*    */}
  /*    */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi
 * .jar Qualified Name: org.palo.viewapi.internal.dbmappers.DomainObjectCache
 * JD-Core Version: 0.5.4
 */