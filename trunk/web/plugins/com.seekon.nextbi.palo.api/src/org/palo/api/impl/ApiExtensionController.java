/*     */package org.palo.api.impl;

/*     */
/*     */import java.util.Map; /*     */
import org.palo.api.Cube; /*     */
import org.palo.api.CubeView; /*     */
import org.palo.api.Database; /*     */
import org.palo.api.Property; /*     */
import org.palo.api.impl.views.CubeViewManager; /*     */
import org.palo.api.persistence.PaloPersistenceException;

/*     */
/*     */class ApiExtensionController
/*     */{
  /* 64 */private static final ApiExtensionController instance = new ApiExtensionController();

  /*     */
  /* 73 */private final AbstractController viewManager = CubeViewManager
    .getInstance();

  /*     */
  /*     */static final ApiExtensionController getInstance()
  /*     */{
    /* 67 */return instance;
    /*     */}

  /*     */
  /*     */final CubeView createCubeView(String id, String name, Cube srcCube,
    Property[] properties)
  /*     */{
    /*     */Object[] params;
    /* 99 */if (properties == null) {
      /* 100 */params = new Object[] { id, name, srcCube };
      /*     */} else {
      /* 102 */params = new Object[3 + properties.length];
      /* 103 */params[0] = id;
      /* 104 */params[1] = name;
      /* 105 */params[2] = srcCube;
      /* 106 */for (int i = 0; i < properties.length; ++i) {
        /* 107 */params[(i + 3)] = properties[i];
        /*     */}
      /*     */}
    /* 110 */return (CubeView) this.viewManager.create(CubeView.class, params);
    /*     */}

  /*     */
  /*     */final CubeView loadView(Database database, String id)
  /*     */throws PaloPersistenceException
  /*     */{
    /* 132 */return (CubeView) this.viewManager.load(database, id);
    /*     */}

  /*     */
  /*     */final void loadViews(Database database, Map cubeId2viewId, Map views)
  /*     */throws PaloPersistenceException
  /*     */{
    /* 143 */this.viewManager.load(database, cubeId2viewId, views);
    /*     */}

  /*     */
  /*     */final void delete(CubeView view)
  /*     */{
    /* 171 */this.viewManager.delete(view);
    /*     */}
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name: org.palo.api.impl.ApiExtensionController JD-Core Version:
 * 0.5.4
 */