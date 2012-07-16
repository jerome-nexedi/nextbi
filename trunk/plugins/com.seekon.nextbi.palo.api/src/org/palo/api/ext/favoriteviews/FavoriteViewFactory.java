/*    */package org.palo.api.ext.favoriteviews;

/*    */
/*    */import org.palo.api.Connection; /*    */
import org.palo.api.CubeView;

/*    */
/*    */public abstract class FavoriteViewFactory
/*    */{
  /*    */private static FavoriteViewFactory instance;
  /*    */
  /*    */static
  /*    */{
    /*    */try
    /*    */{
      /* 64 */instance =
      /* 65 */(FavoriteViewFactory) Class.forName(
        "org.palo.api.ext.favoriteviews.impl.FavoriteViewFactoryImpl").newInstance();
      /*    */} catch (Exception e) {
      /* 67 */e.printStackTrace();
      /*    */}
    /*    */}

  /*    */
  /*    */public static FavoriteViewFactory getInstance() {
    /* 72 */return instance;
    /*    */}

  /*    */
  /*    */public abstract FavoriteView createFavoriteView(String paramString,
    CubeView paramCubeView);

  /*    */
  /*    */public abstract FavoriteView createFavoriteView(String paramString,
    CubeView paramCubeView, int paramInt);

  /*    */
  /*    */public abstract FavoriteViewsFolder createFolder(String paramString,
    Connection paramConnection);

  /*    */
  /*    */public abstract FavoriteViewsFolder createFolder(String paramString,
    Connection paramConnection, int paramInt);
  /*    */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name: org.palo.api.ext.favoriteviews.FavoriteViewFactory JD-Core
 * Version: 0.5.4
 */