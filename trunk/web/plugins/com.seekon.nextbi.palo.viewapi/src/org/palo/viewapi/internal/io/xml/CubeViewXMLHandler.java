/*     */package org.palo.viewapi.internal.io.xml;

/*     */
/*     */import java.lang.reflect.Constructor;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Stack;

import org.palo.api.Cube;
import org.palo.viewapi.AuthUser;
import org.palo.viewapi.CubeView;
import org.palo.viewapi.View;
import org.palo.viewapi.internal.cubeview.CubeViewFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/*     */
/*     */public class CubeViewXMLHandler extends DefaultHandler
/*     */{
  /* 60 */private Stack<String> absPath = new Stack();

  /* 61 */private final StringBuffer strBuffer = new StringBuffer();

  /*     */
  /* 63 */private final HashMap<String, Class<? extends IXMLHandler>> xmlHandlers = new HashMap();

  /*     */private final View view;

  /*     */private final Cube srcCube;

  /*     */private final AuthUser user;

  /*     */private CubeView cubeView;

  /*     */private IXMLHandler handler;

  /*     */
  /*     */public CubeViewXMLHandler(AuthUser user, View view, Cube srcCube)
  /*     */{
    /* 71 */this.view = view;
    /* 72 */this.srcCube = srcCube;
    /* 73 */this.user = user;
    /* 74 */this.xmlHandlers.put("/view/axis", AxisHandler.class);
    /* 75 */this.xmlHandlers.put("/view/format", FormatHandler.class);
    /* 76 */this.xmlHandlers.put("/view/property", PropertyHandler.class);
    /*     */}

  /*     */
  /*     */public final CubeView getView() {
    /* 80 */return this.cubeView;
    /*     */}

  /*     */
  /*     */public void startElement(String uri, String localName, String qName,
    Attributes attributes)
  /*     */throws SAXException
  /*     */{
    /* 86 */super.startElement(uri, localName, qName, attributes);
    /* 87 */reset(this.strBuffer);
    /* 88 */this.absPath.push(qName);
    /* 89 */String xPath = getXPath();
    /* 90 */if (xPath.equals("/view")) {
      /* 91 */this.cubeView = createView(this.user, attributes);
      /*     */} else {
      /* 93 */if (this.xmlHandlers.containsKey(xPath)) {
        /* 94 */this.handler = createHandler(xPath);
        /*     */}
      /* 96 */if (this.handler != null)
        /* 97 */this.handler.enter(xPath, attributes);
      /*     */}
    /*     */}

  /*     */
  /*     */public void endElement(String uri, String localName, String qName)
    throws SAXException
  /*     */{
    /* 103 */super.endElement(uri, localName, qName);
    /* 104 */String xPath = getXPath();
    /* 105 */if (xPath.equals("/view"))
      /* 106 */return;
    /* 107 */if (this.handler != null) {
      /* 108 */this.handler.leave(xPath, this.strBuffer.toString());
      /*     */}
    /* 110 */if (this.absPath.size() > 0)
      /* 111 */this.absPath.pop();
    /*     */}

  /*     */
  /*     */public void characters(char[] ch, int start, int length)
    throws SAXException
  /*     */{
    /* 116 */this.strBuffer.append(ch, start, length);
    /*     */}

  /*     */
  /*     */public void processingInstruction(String target, String data)
  /*     */throws SAXException
  /*     */{
    /* 124 */super.processingInstruction(target, data);
    /*     */}

  /*     */
  /*     */private final void reset(StringBuffer strBuffer)
  /*     */{
    /* 132 */strBuffer.delete(0, strBuffer.length());
    /*     */}

  /*     */
  /*     */private final String getXPath() {
    /* 136 */Enumeration allPaths = this.absPath.elements();
    /* 137 */StringBuffer path = new StringBuffer();
    /* 138 */while (allPaths.hasMoreElements()) {
      /* 139 */path.append("/");
      /* 140 */path.append((String) allPaths.nextElement());
      /*     */}
    /* 142 */return path.toString();
    /*     */}

  /*     */
  /*     */private final CubeView createView(AuthUser user, Attributes attributes)
  /*     */{
    /* 155 */return CubeViewFactory.createView(this.view, this.srcCube, user, null);
    /*     */}

  /*     */
  /*     */private final IXMLHandler createHandler(String xPath) {
    /* 159 */Class handler = (Class) this.xmlHandlers.get(xPath);
    /* 160 */if (handler != null) {
      /*     */try {
        /* 162 */Constructor constructor =
        /* 163 */handler.getConstructor(new Class[] { CubeView.class });
        /* 164 */return (IXMLHandler) constructor
          .newInstance(new Object[] { this.cubeView });
        /*     */}
      /*     */catch (Exception e) {
        /* 167 */e.printStackTrace();
        /*     */}
      /*     */}
    /* 170 */return null;
    /*     */}
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi
 * .jar Qualified Name: org.palo.viewapi.internal.io.xml.CubeViewXMLHandler
 * JD-Core Version: 0.5.4
 */