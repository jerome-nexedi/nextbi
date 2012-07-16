/*     */package org.palo.api.ext.favoriteviews.impl;

/*     */
/*     */import org.palo.api.Connection; /*     */
import org.palo.api.Cube; /*     */
import org.palo.api.CubeView; /*     */
import org.palo.api.Database; /*     */
import org.palo.api.ext.favoriteviews.FavoriteView;

/*     */
/*     */public class FavoriteViewImpl
/*     */implements FavoriteView
/*     */{
  /*     */private String name;

  /*     */private CubeView cubeView;

  /*     */private int position;

  /*     */private String databaseId;

  /*     */private String cubeId;

  /*     */private String cubeViewName;

  /*     */
  /*     */public FavoriteViewImpl(String name, CubeView view)
  /*     */{
    /* 99 */this.name = name;
    /* 100 */this.cubeView = view;
    /* 101 */this.position = 0;
    /* 102 */initCubeView();
    /*     */}

  /*     */
  /*     */public FavoriteViewImpl(String name, CubeView view, int position)
  /*     */{
    /* 115 */this.name = name;
    /* 116 */this.cubeView = view;
    /* 117 */this.position = position;
    /* 118 */initCubeView();
    /*     */}

  /*     */
  /*     */private void initCubeView()
  /*     */{
    /* 127 */if (this.cubeView == null) {
      /* 128 */return;
      /*     */}
    /* 130 */this.cubeViewName = this.cubeView.getName();
    /* 131 */Database db = this.cubeView.getCube().getDatabase();
    /* 132 */Cube cube = this.cubeView.getCube();
    /* 133 */this.cubeId = cube.getId();
    /* 134 */this.databaseId = db.getId();
    /*     */}

  /*     */
  /*     */public String getName()
  /*     */{
    /* 141 */return this.name;
    /*     */}

  /*     */
  /*     */public void setName(String newName)
  /*     */{
    /* 150 */this.name = newName;
    /*     */}

  /*     */
  /*     */public int getPosition()
  /*     */{
    /* 158 */return this.position;
    /*     */}

  /*     */
  /*     */public void setPosition(int newPosition)
  /*     */{
    /* 167 */this.position = newPosition;
    /*     */}

  /*     */
  /*     */public CubeView getCubeView()
  /*     */{
    /* 175 */return this.cubeView;
    /*     */}

  /*     */
  /*     */public String getCubeId()
  /*     */{
    /* 183 */return this.cubeId;
    /*     */}

  /*     */
  /*     */public String getDatabaseId()
  /*     */{
    /* 191 */return this.databaseId;
    /*     */}

  /*     */
  /*     */public String getCubeViewName()
  /*     */{
    /* 199 */return this.cubeViewName;
    /*     */}

  /*     */
  /*     */public Connection getConnection()
  /*     */{
    /* 206 */return this.cubeView.getCube().getDatabase().getConnection();
    /*     */}
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name: org.palo.api.ext.favoriteviews.impl.FavoriteViewImpl JD-Core
 * Version: 0.5.4
 */