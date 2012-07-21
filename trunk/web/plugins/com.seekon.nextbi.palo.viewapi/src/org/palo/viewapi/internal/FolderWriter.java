/*    */package org.palo.viewapi.internal;

/*    */
/*    */import java.io.BufferedWriter; /*    */
import java.io.OutputStream; /*    */
import java.io.OutputStreamWriter; /*    */
import java.io.PrintWriter; /*    */
import org.palo.api.exceptions.PaloIOException;

/*    */
/*    */class FolderWriter
/*    */{
  /* 53 */private static FolderWriter instance = new FolderWriter();

  /*    */
  /* 55 */public static final FolderWriter getInstance() {
    return instance;
  }

  /*    */
  /*    */
  /*    */final void toXML(OutputStream out, ExplorerTreeNode root)
  /*    */throws PaloIOException
  /*    */{
    /*    */try
    /*    */{
      /* 63 */toXMLInternal(out, root);
      /*    */} catch (Exception e) {
      /* 65 */PaloIOException pex =
      /* 66 */new PaloIOException("Writing folder to xml failed!", e);
      /* 67 */pex.setData(root);
      /* 68 */throw pex;
      /*    */}
    /*    */}

  /*    */
  /*    */private final void toXMLInternal(OutputStream output,
    ExplorerTreeNode root) throws Exception
  /*    */{
    /* 74 */PrintWriter w = new PrintWriter(
    /* 75 */new BufferedWriter(new OutputStreamWriter(output, "UTF-8")));
    /*    */try
    /*    */{
      /* 78 */w.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n");
      /* 79 */w.write("<?palofolder version=\"0.1\"?>\r\n");
      /*    */
      /* 81 */w.write("<folder>\r\n");
      /* 82 */if (root != null)
      /*    */{
        /* 84 */w.write(root.getPersistenceString());
        /*    */}
      /* 86 */w.write("</folder>\r\n");
      /*    */} finally {
      /* 88 */w.close();
      /*    */}
    /*    */}
  /*    */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi
 * .jar Qualified Name: org.palo.viewapi.internal.FolderWriter JD-Core Version:
 * 0.5.4
 */