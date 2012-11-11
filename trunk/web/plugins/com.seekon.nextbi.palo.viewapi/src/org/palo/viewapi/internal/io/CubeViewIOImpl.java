/*    */package org.palo.viewapi.internal.io;

/*    */
/*    */import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.palo.api.Cube;
import org.palo.api.exceptions.PaloIOException;
import org.palo.viewapi.AuthUser;
import org.palo.viewapi.CubeView;
import org.palo.viewapi.View;

/*    */
/*    */class CubeViewIOImpl extends CubeViewIO
/*    */{
  /*    */protected final CubeView viewFromXML(AuthUser user, View view, Cube cube,
    String xml, CubeViewReader reader)
  /*    */throws PaloIOException
  /*    */{
    /* 60 */CubeView cView = null;
    /*    */try
    /*    */{
      /* 63 */ByteArrayInputStream bin = new ByteArrayInputStream(
      /* 64 */xml.getBytes("UTF-8"));
      /*    */try {
        /* 66 */cView = reader.fromXML(user, view, cube, bin);
        /*    */} finally {
        /* 68 */bin.close();
        /*    */}
      /*    */} catch (PaloIOException e) {
      /* 71 */throw e;
      /*    */}
    /*    */catch (Exception localException) {
      /*    */}
    /* 75 */return cView;
    /*    */}

  /*    */
  /*    */protected final String toXML(CubeView view, CubeViewWriter writer) {
    /*    */try {
      /* 80 */ByteArrayOutputStream bout = new ByteArrayOutputStream();
      /*    */try {
        /* 82 */writer.toXML(bout, view);
        /* 83 */return bout.toString("UTF-8");
        /*    */} finally {
        /* 85 */bout.close();
        /*    */}
      /*    */}
    /*    */catch (Exception localException) {
      /*    */}
    /* 90 */return null;
    /*    */}
  /*    */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi
 * .jar Qualified Name: org.palo.viewapi.internal.io.CubeViewIOImpl JD-Core
 * Version: 0.5.4
 */