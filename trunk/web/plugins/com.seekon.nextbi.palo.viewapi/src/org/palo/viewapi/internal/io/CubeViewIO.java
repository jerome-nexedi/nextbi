/*    */package org.palo.viewapi.internal.io;

/*    */
/*    */import org.palo.api.Cube; /*    */
import org.palo.api.exceptions.PaloIOException; /*    */
import org.palo.viewapi.AuthUser; /*    */
import org.palo.viewapi.CubeView; /*    */
import org.palo.viewapi.View;

/*    */
/*    */public abstract class CubeViewIO
/*    */{
  /* 52 */private static final CubeViewIO instance = new CubeViewIOImpl();

  /*    */
  /*    */public static final String toXML(CubeView view)
  /*    */{
    /* 56 */return instance.toXML(view, CubeViewWriter.getInstance());
    /*    */}

  /*    */
  /*    */public static final CubeView fromXML(AuthUser user, View view, Cube cube,
    String xml) throws PaloIOException {
    /* 60 */return instance.viewFromXML(user,
    /* 61 */view, cube, xml, CubeViewReader.getInstance());
    /*    */}

  /*    */
  /*    */protected abstract String toXML(CubeView paramCubeView,
    CubeViewWriter paramCubeViewWriter);

  /*    */
  /*    */protected abstract CubeView viewFromXML(AuthUser paramAuthUser,
    View paramView, Cube paramCube, String paramString,
    CubeViewReader paramCubeViewReader)
  /*    */throws PaloIOException;
  /*    */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi
 * .jar Qualified Name: org.palo.viewapi.internal.io.CubeViewIO JD-Core Version:
 * 0.5.4
 */