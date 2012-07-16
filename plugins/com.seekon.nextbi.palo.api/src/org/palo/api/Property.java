/*    */package org.palo.api;

/*    */
/*    *//** @deprecated */
/*    */public class Property
/*    */{
  /*    */private String id;

  /*    */private String value;

  /*    */
  /*    */public Property(String id, String value)
  /*    */{
    /* 64 */this.id = id;
    /* 65 */this.value = value;
    /*    */}

  /*    */
  /*    */public String getId()
  /*    */{
    /* 73 */return this.id;
    /*    */}

  /*    */
  /*    */public String getValue()
  /*    */{
    /* 81 */return this.value;
    /*    */}

  /*    */
  /*    */public void setValue(String newValue)
  /*    */{
    /* 90 */this.value = newValue;
    /*    */}
  /*    */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name: org.palo.api.Property JD-Core Version: 0.5.4
 */