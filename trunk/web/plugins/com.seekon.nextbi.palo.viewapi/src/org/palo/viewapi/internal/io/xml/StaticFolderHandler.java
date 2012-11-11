/*     */package org.palo.viewapi.internal.io.xml;

/*     */
/*     */import org.palo.api.PaloAPIException;
import org.palo.viewapi.AuthUser;
import org.palo.viewapi.View;
import org.palo.viewapi.internal.StaticFolder;
import org.palo.viewapi.services.ServiceProvider;
import org.xml.sax.Attributes;

/*     */
/*     */public class StaticFolderHandler
/*     */implements IXMLHandler
/*     */{
  /*     */public static final String XPATH = "/folder/staticFolder";

  /*     */private FolderXMLHandler xmlHandler;

  /*     */
  /*     */public StaticFolderHandler(FolderXMLHandler xmlHandler)
  /*     */{
    /* 59 */this.xmlHandler = xmlHandler;
    /*     */}

  /*     */
  /*     */static final View parseSourceView(FolderXMLHandler xmlHandler,
    String viewId) {
    /* 63 */AuthUser user = xmlHandler.getUser();
    /*     */try {
      /* 65 */return ServiceProvider.getViewService(user).getView(viewId);
      /*     */} catch (Exception localException) {
      /*     */}
    /* 68 */return null;
    /*     */}

  /*     */
  /*     */public void enter(String path, Attributes attributes) {
    /* 72 */if ((!path.startsWith("/folder/")) || (!path.endsWith("staticFolder")))
      /*     */return;
    /* 74 */String id = attributes.getValue("id");
    /* 75 */if ((id == null) || (id.equals(""))) {
      /* 76 */throw new PaloAPIException("StaticFolderHandler: no id defined!");
      /*     */}
    /*     */
    /* 79 */String name = attributes.getValue("name");
    /* 80 */if (name == null) {
      /* 81 */throw new PaloAPIException("StaticFolderHandler: no name specified!");
      /*     */}
    /*     */
    /* 84 */String source = attributes.getValue("source");
    /* 85 */View sourceView = null;
    /* 86 */if (source != null) {
      /* 87 */sourceView = parseSourceView(this.xmlHandler, source);
      /*     */}
    /*     */
    /* 91 */StaticFolder f = StaticFolder.internalCreate(
    /* 92 */this.xmlHandler.getCurrentParent(), id, name);
    /* 93 */if (sourceView != null) {
      /* 94 */f.setSourceObject(sourceView);
      /*     */}
    /* 96 */this.xmlHandler.pushParent(f);
    /*     */}

  /*     */
  /*     */public String getXPath()
  /*     */{
    /* 101 */return "/folder/staticFolder";
    /*     */}

  /*     */
  /*     */public void leave(String path, String value) {
    /* 105 */if ((path.startsWith("/folder/")) && (path.endsWith("staticFolder")))
      /* 106 */this.xmlHandler.popParent();
    /*     */}
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi
 * .jar Qualified Name: org.palo.viewapi.internal.io.xml.StaticFolderHandler
 * JD-Core Version: 0.5.4
 */