/*     */package org.palo.api.impl.views;

/*     */
/*     */import java.util.ArrayList; /*     */
import java.util.Arrays; /*     */
import java.util.HashMap; /*     */
import java.util.Set; /*     */
import org.palo.api.CubeView; /*     */
import org.palo.api.Database; /*     */
import org.palo.api.impl.xml.BaseXMLHandler; /*     */
import org.palo.api.impl.xml.IPaloEndHandler; /*     */
import org.palo.api.impl.xml.IPaloStartHandler; /*     */
import org.palo.api.persistence.PersistenceError; /*     */
import org.xml.sax.SAXException;

/*     */
/*     */class CubeViewXMLHandler extends BaseXMLHandler
/*     */{
  /* 60 */private final HashMap handlers = new HashMap();

  /*     */private CubeViewHandler xmlHandler;

  /* 62 */private final ArrayList errors = new ArrayList();

  /*     */
  /*     */CubeViewXMLHandler(Database database, String key) {
    /* 65 */registerHandler("legacy",
    /* 66 */new CubeViewHandlerLegacy(database, key));
    /* 67 */registerHandler("1.0", new CubeViewHandler1_0(database));
    /* 68 */registerHandler("1.1", new CubeViewHandler1_1(database));
    /* 69 */registerHandler("1.2", new CubeViewHandler1_2(database));
    /* 70 */registerHandler("1.3", new CubeViewHandler1_3(database));
    /* 71 */registerHandler("1.4", new CubeViewHandler1_4(database));
    /*     */}

  /*     */
  /*     */final boolean hasErrors() {
    /* 75 */return ((this.xmlHandler != null) && (this.xmlHandler.hasErrors()))
      || (!this.errors.isEmpty());
    /*     */}

  /*     */
  /*     */final PersistenceError[] getErrors() {
    /* 79 */if ((this.xmlHandler != null) && (this.xmlHandler.hasErrors()))
      /* 80 */this.errors.addAll(Arrays.asList(this.xmlHandler.getErrors()));
    /* 81 */return (PersistenceError[]) this.errors.toArray(new PersistenceError[
    /* 82 */this.errors.size()]);
    /*     */}

  /*     */
  /*     */final void addViewError(PersistenceError error) {
    /* 86 */if (this.xmlHandler != null)
      /* 87 */this.xmlHandler.addError(error);
    /*     */else
      /* 89 */this.errors.add(error);
    /*     */}

  /*     */
  /*     */final void useLegacy() {
    /* 93 */clearAllHandlers();
    /*     */
    /* 95 */useHandler((CubeViewHandler) this.handlers.get("legacy"));
    /*     */}

  /*     */
  /*     */final void registerHandler(String version, CubeViewHandler handler)
  /*     */{
    /* 106 */this.handlers.put(version, handler);
    /*     */}

  /*     */
  /*     */public final void processingInstruction(String target, String data)
    throws SAXException
  /*     */{
    /* 111 */if (target.equals("paloview")) {
      /* 112 */String version = data.substring(9, data.length() - 1);
      /* 113 */CubeViewHandler handler =
      /* 114 */(CubeViewHandler) this.handlers.get(version);
      /*     */
      /* 116 */if ((handler == null) && (!version.equalsIgnoreCase("legacy")))
      /*     */{
        /* 121 */String[] versions =
        /* 122 */(String[]) this.handlers.keySet()
        /* 122 */.toArray(new String[0]);
        /* 123 */String max = versions[0];
        /* 124 */int maxMajor = Integer.parseInt(max.split("\\.")[0]);
        /* 125 */int maxMinor = Integer.parseInt(max.split("\\.")[1]);
        /* 126 */handler = (CubeViewHandler) this.handlers.get(max);
        /* 127 */for (String vers : versions) {
          /* 128 */if (vers.equals("legacy")) {
            /*     */continue;
            /*     */}
          /* 131 */int major = Integer.parseInt(vers.split("\\.")[0]);
          /* 132 */int minor = Integer.parseInt(vers.split("\\.")[1]);
          /* 133 */if ((maxMajor < major)
            || ((maxMajor == major) && (maxMinor < minor))) {
            /* 134 */maxMajor = major;
            /* 135 */maxMinor = minor;
            /* 136 */handler = (CubeViewHandler) this.handlers.get(vers);
            /*     */}
          /*     */}
        /* 139 */if (Integer.parseInt(version.split("\\.")[0]) > maxMajor) {
          /* 140 */handler = null;
          /*     */}
        /*     */
        /*     */}
      /*     */
      /* 148 */useHandler(handler);
      /*     */}
    /*     */
    /* 157 */super.processingInstruction(target, data);
    /*     */}

  /*     */
  /*     */final CubeView getCubeView()
  /*     */{
    /* 165 */if (this.xmlHandler == null)
      /* 166 */return null;
    /* 167 */return this.xmlHandler.getCubeView();
    /*     */}

  /*     */
  /*     */private final void useHandler(CubeViewHandler xmlHandler) {
    /* 171 */if (xmlHandler == null) {
      /* 172 */throw new RuntimeException("Unsupported palo view version!");
      /*     */}
    /* 174 */this.xmlHandler = xmlHandler;
    /* 175 */IPaloStartHandler[] startHandlers = xmlHandler.getStartHandlers();
    /* 176 */for (int i = 0; i < startHandlers.length; ++i)
      /* 177 */putStartHandler(startHandlers[i].getPath(), startHandlers[i]);
    /* 178 */IPaloEndHandler[] endHandlers = xmlHandler.getEndHandlers();
    /* 179 */for (int i = 0; i < endHandlers.length; ++i)
      /* 180 */putEndHandler(endHandlers[i].getPath(), endHandlers[i]);
    /*     */}
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name: org.palo.api.impl.views.CubeViewXMLHandler JD-Core Version:
 * 0.5.4
 */