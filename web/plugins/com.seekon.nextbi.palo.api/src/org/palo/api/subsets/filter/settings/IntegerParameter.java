/*    */package org.palo.api.subsets.filter.settings;

/*    */
/*    */public class IntegerParameter extends AbstractParameter
/*    */{
  /*    */private final String name;

  /*    */private Integer value;

  /*    */
  /*    */public IntegerParameter()
  /*    */{
    /* 57 */this(null);
    /*    */}

  /*    */
  /*    */public IntegerParameter(String name)
  /*    */{
    /* 66 */this.name = name;
    /* 67 */this.value = new Integer(0);
    /*    */}

  /*    */
  /*    */public final String getName() {
    /* 71 */return this.name;
    /*    */}

  /*    */
  /*    */public final Integer getValue() {
    /* 75 */return this.value;
    /*    */}

  /*    */
  /*    */public final void setValue(int value)
  /*    */{
    /* 83 */this.value = Integer.valueOf(value);
    /* 84 */markDirty();
    /*    */}

  /*    */
  /*    */public final void setValue(Integer value) {
    /* 88 */this.value = value;
    /* 89 */markDirty();
    /*    */}
  /*    */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name: org.palo.api.subsets.filter.settings.IntegerParameter JD-Core
 * Version: 0.5.4
 */