/*    */package org.palo.api.subsets.filter.settings;

/*    */
/*    */public class StringParameter extends AbstractParameter
/*    */{
  /*    */private final String name;

  /*    */private String value;

  /*    */
  /*    */public StringParameter()
  /*    */{
    /* 57 */this(null);
    /*    */}

  /*    */
  /*    */public StringParameter(String name)
  /*    */{
    /* 66 */this.name = name;
    /* 67 */this.value = new String();
    /*    */}

  /*    */
  /*    */public final String getName() {
    /* 71 */return this.name;
    /*    */}

  /*    */
  /*    */public final String getValue() {
    /* 75 */return this.value;
    /*    */}

  /*    */
  /*    */public final void setValue(String value)
  /*    */{
    /* 83 */this.value = value;
    /* 84 */markDirty();
    /*    */}
  /*    */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name: org.palo.api.subsets.filter.settings.StringParameter JD-Core
 * Version: 0.5.4
 */