/*    */package org.palo.api.impl.subsets;

/*    */
/*    */import java.util.HashMap; /*    */
import org.palo.api.Database; /*    */
import org.palo.api.Subset; /*    */
import org.palo.api.impl.xml.BaseXMLHandler; /*    */
import org.palo.api.impl.xml.IPaloEndHandler; /*    */
import org.palo.api.impl.xml.IPaloStartHandler; /*    */
import org.xml.sax.SAXException;

/*    */
/*    */class SubsetXMLHandler extends BaseXMLHandler
/*    */{
  /* 56 */private final HashMap handlers = new HashMap();

  /*    */private XMLSubsetHandler xmlHandler;

  /*    */
  /*    */SubsetXMLHandler(Database database, String key)
  /*    */{
    /* 60 */this.handlers.put("legacy", new XMLSubsetHandlerLegacy(database, key));
    /* 61 */this.handlers.put("1.0", new XMLSubsetHandler1_0(database));
    /* 62 */this.handlers.put("1.1", new XMLSubsetHandler1_1(database));
    /*    */}

  /*    */
  /*    */public final void processingInstruction(String target, String data)
  /*    */throws SAXException
  /*    */{
    /* 68 */if (target.equals("palosubset")) {
      /* 69 */String version = data.substring(9, data.length() - 1).trim();
      /* 70 */useHandler((XMLSubsetHandler) this.handlers.get(version));
      /*    */}
    /* 72 */super.processingInstruction(target, data);
    /*    */}

  /*    */
  /*    */final void useLegacy()
  /*    */{
    /* 79 */clearAllHandlers();
    /* 80 */useHandler((XMLSubsetHandler) this.handlers.get("legacy"));
    /*    */}

  /*    */
  /*    */final Subset getSubset()
  /*    */{
    /* 88 */return this.xmlHandler.getSubset();
    /*    */}

  /*    */
  /*    */private final void useHandler(XMLSubsetHandler xmlHandler) {
    /* 92 */this.xmlHandler = xmlHandler;
    /* 93 */IPaloStartHandler[] startHandlers = xmlHandler.getStartHandlers();
    /* 94 */for (int i = 0; i < startHandlers.length; ++i)
      /* 95 */putStartHandler(startHandlers[i].getPath(), startHandlers[i]);
    /* 96 */IPaloEndHandler[] endHandlers = xmlHandler.getEndHandlers();
    /* 97 */for (int i = 0; i < endHandlers.length; ++i)
      /* 98 */putEndHandler(endHandlers[i].getPath(), endHandlers[i]);
    /*    */}
  /*    */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name: org.palo.api.impl.subsets.SubsetXMLHandler JD-Core Version:
 * 0.5.4
 */