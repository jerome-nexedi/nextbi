/*    */package org.palo.api.ext.favoriteviews.impl;

/*    */
/*    */import org.palo.api.Connection; /*    */
import org.palo.api.CubeView; /*    */
import org.palo.api.ext.favoriteviews.FavoriteView; /*    */
import org.palo.api.ext.favoriteviews.FavoriteViewFactory; /*    */
import org.palo.api.ext.favoriteviews.FavoriteViewsFolder;

/*    */
/*    */public class FavoriteViewFactoryImpl extends FavoriteViewFactory
/*    */{
  /*    */public FavoriteView createFavoriteView(String name, CubeView view)
  /*    */{
    /* 64 */return new FavoriteViewImpl(name, view);
    /*    */}

  /*    */
  /*    */public FavoriteView createFavoriteView(String name, CubeView view,
    int position)
  /*    */{
    /* 72 */return new FavoriteViewImpl(name, view, position);
    /*    */}

  /*    */
  /*    */public FavoriteViewsFolder createFolder(String name, Connection con)
  /*    */{
    /* 79 */return new FavoriteViewsFolderImpl(name, con);
    /*    */}

  /*    */
  /*    */public FavoriteViewsFolder createFolder(String name, Connection con,
    int position)
  /*    */{
    /* 87 */return new FavoriteViewsFolderImpl(name, con, position);
    /*    */}
  /*    */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name: org.palo.api.ext.favoriteviews.impl.FavoriteViewFactoryImpl
 * JD-Core Version: 0.5.4
 */