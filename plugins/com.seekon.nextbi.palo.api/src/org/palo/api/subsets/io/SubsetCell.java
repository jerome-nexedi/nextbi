/*    */package org.palo.api.subsets.io;

/*    */
/*    */import org.palo.api.Element;

/*    */
/*    */class SubsetCell
/*    */{
  /*    */private final String id;

  /*    */private String xmlDef;

  /*    */private Element[] coordinate;

  /*    */
  /*    */SubsetCell(String id)
  /*    */{
    /* 62 */this.id = id;
    /*    */}

  /*    */
  /*    */String getSubsetId() {
    /* 66 */return this.id;
    /*    */}

  /*    */
  /*    */void setXmlDef(String xmlDef) {
    /* 70 */this.xmlDef = xmlDef;
    /*    */}

  /*    */
  /*    */void setCoordinate(Element[] coordinate) {
    /* 74 */this.coordinate = coordinate;
    /*    */}

  /*    */
  /*    */Element[] getCoordinate() {
    /* 78 */return this.coordinate;
    /*    */}

  /*    */
  /*    */String getXmlDef() {
    /* 82 */return this.xmlDef;
    /*    */}
  /*    */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name: org.palo.api.subsets.io.SubsetCell JD-Core Version: 0.5.4
 */