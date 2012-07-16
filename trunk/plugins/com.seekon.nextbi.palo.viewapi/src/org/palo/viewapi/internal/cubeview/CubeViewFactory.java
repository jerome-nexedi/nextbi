/*    */package org.palo.viewapi.internal.cubeview;

/*    */
/*    */import org.palo.api.Cube; /*    */
import org.palo.viewapi.AuthUser; /*    */
import org.palo.viewapi.CubeView; /*    */
import org.palo.viewapi.View;

/*    */
/*    */public class CubeViewFactory
/*    */{
  /*    */public static final CubeView createView(View forView, Cube cube,
    AuthUser user, String externalId)
  /*    */{
    /* 52 */return new CubeViewImpl(forView, cube, user, externalId);
    /*    */}
  /*    */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi
 * .jar Qualified Name: org.palo.viewapi.internal.cubeview.CubeViewFactory
 * JD-Core Version: 0.5.4
 */