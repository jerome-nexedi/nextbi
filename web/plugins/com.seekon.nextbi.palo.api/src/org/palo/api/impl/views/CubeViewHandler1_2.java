/*    */package org.palo.api.impl.views;

/*    */
/*    */import org.palo.api.CubeView; /*    */
import org.palo.api.Database; /*    */
import org.palo.api.impl.xml.IPaloStartHandler; /*    */
import org.xml.sax.Attributes;

/*    */
/*    */class CubeViewHandler1_2 extends CubeViewHandler1_1
/*    */{
  /*    */CubeViewHandler1_2(Database database)
  /*    */{
    /* 62 */super(database);
    /*    */}

  /*    */
  /*    */protected void registerStartHandlers()
  /*    */{
    /* 72 */super.registerStartHandlers();
    /*    */
    /* 75 */registerStartHandler(new IPaloStartHandler() {
      /*    */public String getPath() {
        /* 77 */return "view/property";
        /*    */}

      /*    */
      /*    */public void startElement(String uri, String localName, String qName,
        Attributes attributes) {
        /*    */try {
          /* 82 */String id = attributes.getValue("id");
          /* 83 */String value = attributes.getValue("value");
          /* 84 */CubeViewHandler1_2.this.cubeView.addProperty(id, value);
          /*    */} catch (Exception e) {
          /* 86 */e.printStackTrace();
          /*    */}
        /*    */}
      /*    */
    });
    /*    */}
  /*    */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name: org.palo.api.impl.views.CubeViewHandler1_2 JD-Core Version:
 * 0.5.4
 */