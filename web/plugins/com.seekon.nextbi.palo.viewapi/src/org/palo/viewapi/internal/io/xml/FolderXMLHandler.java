/*     */package org.palo.viewapi.internal.io.xml;

/*     */
/*     */import java.lang.reflect.Constructor;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Stack;

import org.palo.viewapi.AuthUser;
import org.palo.viewapi.internal.ExplorerTreeNode;
import org.palo.viewapi.internal.StaticFolder;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/*     */
/*     */public class FolderXMLHandler extends DefaultHandler
/*     */{
  /* 59 */private Stack<String> absPath = new Stack();

  /* 60 */private final StringBuffer strBuffer = new StringBuffer();

  /*     */
  /* 62 */private final HashMap<String, Class<? extends IXMLHandler>> xmlHandlers = new HashMap();

  /*     */private ExplorerTreeNode root;

  /*     */private IXMLHandler handler;

  /*     */private final AuthUser user;

  /* 67 */private final Stack<ExplorerTreeNode> parents = new Stack();

  /*     */
  /*     */public FolderXMLHandler(AuthUser user) {
    /* 70 */this.user = user;
    /* 71 */this.xmlHandlers.put("/folder/staticFolder", StaticFolderHandler.class);
    /* 72 */this.xmlHandlers.put("/folder/dynamicFolder",
      DynamicFolderHandler.class);
    /* 73 */this.xmlHandlers.put("/folder/folderElement",
      FolderElementHandler.class);
    /*     */}

  /*     */
  /*     */public final ExplorerTreeNode getRoot() {
    /* 77 */if ((this.root != null)
      && (this.root.getName().equals("Invisible Root"))) {
      /* 78 */ExplorerTreeNode[] kids = this.root.getChildren();
      /* 79 */if ((kids != null) && (kids.length > 0)) {
        /* 80 */kids[0].setParent(null);
        /* 81 */return kids[0];
        /*     */}
      /* 83 */return null;
      /*     */}
    /* 85 */return this.root;
    /*     */}

  /*     */
  /*     */public void startElement(String uri, String localName, String qName,
    Attributes attributes)
  /*     */throws SAXException
  /*     */{
    /* 91 */super.startElement(uri, localName, qName, attributes);
    /* 92 */reset(this.strBuffer);
    /* 93 */this.absPath.push(qName);
    /* 94 */String xPath = getXPath();
    /* 95 */if (xPath.equals("/folder")) {
      /* 96 */this.root = new StaticFolder(null, "Invisible Root");
      /* 97 */this.parents.push(this.root);
      /*     */} else {
      /* 99 */String strippedPath = xPath.substring(0, xPath.indexOf("/", 2) + 1);
      /* 100 */strippedPath = strippedPath
        + xPath.substring(xPath.lastIndexOf("/") + 1);
      /* 101 */if (this.xmlHandlers.containsKey(strippedPath)) {
        /* 102 */this.handler = createHandler(strippedPath);
        /*     */}
      /* 104 */if (this.handler != null)
        /* 105 */this.handler.enter(xPath, attributes);
      /*     */}
    /*     */}

  /*     */
  /*     */public void endElement(String uri, String localName, String qName)
  /*     */throws SAXException
  /*     */{
    /* 112 */super.endElement(uri, localName, qName);
    /* 113 */String xPath = getXPath();
    /* 114 */if (xPath.equals("/folder")) {
      /* 115 */if (this.handler != null)
        /* 116 */this.handler.leave(xPath, this.strBuffer.toString());
      /*     */}
    /*     */else {
      /* 119 */String strippedPath = xPath.substring(0, xPath.indexOf("/", 2) + 1);
      /* 120 */strippedPath = strippedPath
        + xPath.substring(xPath.lastIndexOf("/") + 1);
      /* 121 */if (this.xmlHandlers.containsKey(strippedPath)) {
        /* 122 */this.handler = createHandler(strippedPath);
        /*     */}
      /*     */
      /* 125 */if (this.handler != null) {
        /* 126 */this.handler.leave(xPath, this.strBuffer.toString());
        /*     */}
      /*     */}
    /* 129 */if (this.absPath.size() > 0)
      /* 130 */this.absPath.pop();
    /*     */}

  /*     */
  /*     */public void characters(char[] ch, int start, int length)
    throws SAXException
  /*     */{
    /* 135 */this.strBuffer.append(ch, start, length);
    /*     */}

  /*     */
  /*     */public void processingInstruction(String target, String data)
    throws SAXException
  /*     */{
    /* 140 */super.processingInstruction(target, data);
    /*     */}

  /*     */
  /*     */AuthUser getUser() {
    /* 144 */return this.user;
    /*     */}

  /*     */
  /*     */ExplorerTreeNode getCurrentParent() {
    /* 148 */return (ExplorerTreeNode) this.parents.peek();
    /*     */}

  /*     */
  /*     */ExplorerTreeNode popParent() {
    /* 152 */ExplorerTreeNode node = (ExplorerTreeNode) this.parents.pop();
    /* 153 */return node;
    /*     */}

  /*     */
  /*     */void pushParent(ExplorerTreeNode node) {
    /* 157 */this.parents.push(node);
    /*     */}

  /*     */
  /*     */private final void reset(StringBuffer strBuffer)
  /*     */{
    /* 164 */strBuffer.delete(0, strBuffer.length());
    /*     */}

  /*     */
  /*     */private final String getXPath() {
    /* 168 */Enumeration allPaths = this.absPath.elements();
    /* 169 */StringBuffer path = new StringBuffer();
    /* 170 */while (allPaths.hasMoreElements()) {
      /* 171 */path.append("/");
      /* 172 */path.append((String) allPaths.nextElement());
      /*     */}
    /* 174 */return path.toString();
    /*     */}

  /*     */
  /*     */private final IXMLHandler createHandler(String xPath) {
    /* 178 */Class handler = (Class) this.xmlHandlers.get(xPath);
    /* 179 */if (handler != null) {
      /*     */try {
        /* 181 */Constructor constructor =
        /* 182 */handler.getConstructor(new Class[] { FolderXMLHandler.class });
        /* 183 */return (IXMLHandler) constructor
          .newInstance(new Object[] { this });
        /*     */}
      /*     */catch (Exception e) {
        /* 186 */e.printStackTrace();
        /*     */}
      /*     */}
    /* 189 */return null;
    /*     */}

  /*     */
  /*     */public void clear() {
    /* 193 */DynamicFolderHandler.clear();
    /* 194 */this.xmlHandlers.clear();
    /*     */}
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi
 * .jar Qualified Name: org.palo.viewapi.internal.io.xml.FolderXMLHandler
 * JD-Core Version: 0.5.4
 */