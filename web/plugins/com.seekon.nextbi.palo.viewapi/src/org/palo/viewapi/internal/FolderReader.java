/*     */package org.palo.viewapi.internal;

/*     */
/*     */import java.io.IOException; /*     */
import java.io.InputStream; /*     */
import javax.xml.transform.stream.StreamSource; /*     */
import org.palo.api.exceptions.PaloIOException; /*     */
import org.palo.viewapi.AuthUser; /*     */
import org.palo.viewapi.internal.io.xml.FolderXMLHandler; /*     */
import org.xml.sax.InputSource; /*     */
import org.xml.sax.SAXException; /*     */
import org.xml.sax.XMLReader; /*     */
import org.xml.sax.helpers.XMLReaderFactory;

/*     */
/*     */class FolderReader
/*     */{
  /* 63 */private static FolderReader instance = new FolderReader();

  /*     */
  /* 65 */static final FolderReader getInstance() {
    return instance;
  }

  /*     */
  /*     */
  /*     */ExplorerTreeNode fromXML(AuthUser user, InputStream input)
  /*     */throws PaloIOException
  /*     */{
    /* 77 */ExplorerTreeNode root = null;
    /* 78 */FolderXMLHandler folderHandler = null;
    /*     */try {
      /* 80 */StreamSource xml = new StreamSource(input);
      /*     */
      /* 82 */folderHandler =
      /* 83 */new FolderXMLHandler(user);
      /*     */
      /* 85 */XMLReader parser = XMLReaderFactory.createXMLReader();
      /* 86 */parser.setContentHandler(folderHandler);
      /* 87 */parser.parse(new InputSource(xml.getInputStream()));
      /* 88 */root = folderHandler.getRoot();
      /*     */}
    /*     */catch (SAXException e) {
      /* 91 */throw new PaloIOException("XML Exception during loading of folder!",
        e);
      /*     */} catch (IOException e) {
      /* 93 */throw new PaloIOException("IOException during loading of folder!", e);
      /*     */} catch (Exception e) {
      /* 95 */throw new PaloIOException("Exception during loading of folder!", e);
      /*     */} finally {
      /* 97 */if (folderHandler != null) {
        /* 98 */folderHandler.clear();
        /* 99 */folderHandler = null;
        /*     */}
      /*     */
      /*     */}
    /*     */
    /* 106 */return root;
    /*     */}
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi
 * .jar Qualified Name: org.palo.viewapi.internal.FolderReader JD-Core Version:
 * 0.5.4
 */