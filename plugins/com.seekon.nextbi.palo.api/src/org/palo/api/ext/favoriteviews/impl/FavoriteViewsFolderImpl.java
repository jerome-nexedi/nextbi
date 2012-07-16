/*     */package org.palo.api.ext.favoriteviews.impl;

/*     */
/*     */import org.palo.api.Connection; /*     */
import org.palo.api.ext.favoriteviews.FavoriteViewsFolder;

/*     */
/*     */public class FavoriteViewsFolderImpl
/*     */implements FavoriteViewsFolder
/*     */{
  /*     */private String name;

  /*     */private int position;

  /*     */private Connection connection;

  /*     */
  /*     */public FavoriteViewsFolderImpl(String name, Connection con)
  /*     */{
    /* 79 */this.name = name;
    /* 80 */this.connection = con;
    /* 81 */this.position = 0;
    /*     */}

  /*     */
  /*     */public FavoriteViewsFolderImpl(String name, Connection con, int position)
  /*     */{
    /* 92 */this.name = name;
    /* 93 */this.connection = con;
    /* 94 */this.position = position;
    /*     */}

  /*     */
  /*     */public String getName()
  /*     */{
    /* 101 */return this.name;
    /*     */}

  /*     */
  /*     */public void setName(String newName)
  /*     */{
    /* 110 */this.name = newName;
    /*     */}

  /*     */
  /*     */public int getPosition()
  /*     */{
    /* 117 */return this.position;
    /*     */}

  /*     */
  /*     */public void setPosition(int newPosition)
  /*     */{
    /* 126 */this.position = newPosition;
    /*     */}

  /*     */
  /*     */public Connection getConnection()
  /*     */{
    /* 135 */return this.connection;
    /*     */}
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name: org.palo.api.ext.favoriteviews.impl.FavoriteViewsFolderImpl
 * JD-Core Version: 0.5.4
 */