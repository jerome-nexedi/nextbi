/*    */package org.palo.api.subsets.filter.settings;

/*    */
/*    */public class DoubleParameter extends AbstractParameter
/*    */{
  /*    */private final String name;

  /*    */private Double value;

  /*    */
  /*    */public DoubleParameter()
  /*    */{
    /* 57 */this(null);
    /*    */}

  /*    */
  /*    */public DoubleParameter(String name)
  /*    */{
    /* 66 */this.name = name;
    /* 67 */this.value = new Double(0.0D);
    /*    */}

  /*    */
  /*    */public final String getName() {
    /* 71 */return this.name;
    /*    */}

  /*    */
  /*    */public final Double getValue() {
    /* 75 */return this.value;
    /*    */}

  /*    */
  /*    */public final void setValue(double value) {
    /* 79 */this.value = Double.valueOf(value);
    /* 80 */markDirty();
    /*    */}

  /*    */
  /*    */public final void setValue(Double value)
  /*    */{
    /* 88 */this.value = value;
    /* 89 */markDirty();
    /*    */}
  /*    */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name: org.palo.api.subsets.filter.settings.DoubleParameter JD-Core
 * Version: 0.5.4
 */