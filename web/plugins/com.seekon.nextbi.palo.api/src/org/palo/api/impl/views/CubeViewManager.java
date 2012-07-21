/*     */package org.palo.api.impl.views;

/*     */
/*     */import java.io.ByteArrayOutputStream; /*     */
import java.io.IOException; /*     */
import java.io.UnsupportedEncodingException; /*     */
import java.util.Map; /*     */
import org.palo.api.Connection; /*     */
import org.palo.api.Cube; /*     */
import org.palo.api.CubeView; /*     */
import org.palo.api.Database; /*     */
import org.palo.api.Dimension; /*     */
import org.palo.api.Hierarchy; /*     */
import org.palo.api.Property; /*     */
import org.palo.api.impl.AbstractController; /*     */
import org.palo.api.persistence.PaloPersistenceException;

/*     */
/*     */public class CubeViewManager extends AbstractController
/*     */{
  /* 64 */private static final CubeViewManager instance = new CubeViewManager();

  /*     */private final CubeViewPersistence persister;

  /*     */
  /*     */public static final CubeViewManager getInstance()
  /*     */{
    /* 66 */return instance;
    /*     */}

  /*     */
  /*     */private CubeViewManager()
  /*     */{
    /* 75 */this.persister = CubeViewPersistence.getInstance();
    /*     */}

  /*     */
  /*     */public final void init(Database database)
  /*     */{
    /*     */}

  /*     */
  /*     */public final boolean isViewCube(Cube cube)
  /*     */{
    /* 109 */return this.persister.isViewsCube(cube);
    /*     */}

  /*     */
  /*     */final void save(CubeView view)
  /*     */{
    /* 117 */this.persister.save(view);
    /*     */}

  /*     */
  /*     */final boolean delete(CubeView view)
  /*     */{
    /* 125 */return this.persister.delete(view);
    /*     */}

  /*     */
  /*     */final String getRawDefinition(CubeView view)
  /*     */{
    /* 135 */String def = "";
    /* 136 */ByteArrayOutputStream bout = new ByteArrayOutputStream();
    /*     */try {
      /* 138 */CubeViewWriter.getInstance().toXML(bout, view);
      /*     */try {
        /* 140 */def = bout.toString("UTF-8");
      } catch (UnsupportedEncodingException localUnsupportedEncodingException) {
        /*     */}
      /*     */} finally {
      /*     */try {
        /* 144 */bout.close();
      } catch (IOException localIOException) {
        /*     */}
      /*     */}
    /* 147 */return def;
    /*     */}

  /*     */
  /*     */protected Object create(Class clObject, Object[] args)
  /*     */{
    /* 159 */if ((clObject.isAssignableFrom(CubeView.class)) && (args.length >= 3)) {
      /* 160 */String id = (String) args[0];
      /* 161 */String name = (String) args[1];
      /* 162 */Cube srcCube = (Cube) args[2];
      /* 163 */CubeView view = new CubeViewImpl(id, name, srcCube);
      /*     */
      /* 166 */int i = 3;
      for (int n = args.length; i < n; ++i) {
        /* 167 */view.addProperty((Property) args[i]);
        /*     */}
      /* 169 */return view;
      /*     */}
    /* 171 */return null;
    /*     */}

  /*     */
  /*     */protected boolean delete(Object obj) {
    /* 175 */if (obj instanceof CubeView) {
      /* 176 */return delete((CubeView) obj);
      /*     */}
    /* 178 */return false;
    /*     */}

  /*     */
  /*     */protected Object load(Database db, String id)
    throws PaloPersistenceException {
    /* 182 */return this.persister.load(db, id);
    /*     */}

  /*     */
  /*     */protected void load(Database db, Map cubeId2viewId, Map views)
    throws PaloPersistenceException
  /*     */{
    /* 187 */this.persister.load(db, cubeId2viewId, views);
    /*     */}

  /*     */
  /*     */final void createEnvironment(Database database) {
    /* 191 */if (database.getConnection().getType() == 4) {
      /* 192 */return;
      /*     */}
    /*     */
    /* 195 */Dimension columns = database.getDimensionByName(
    /* 196 */"#viewcolumns");
    /*     */
    /* 198 */if (columns == null) {
      /* 199 */columns = database.addDimension(
      /* 200 */"#viewcolumns");
      /*     */}
    /* 202 */if (columns.getDefaultHierarchy().getElementByName(
    /* 203 */"Def") == null) {
      /* 204 */columns.getDefaultHierarchy().addElement(
      /* 205 */"Def", 1);
      /*     */}
    /*     */
    /* 208 */Dimension rows = database.getDimensionByName(
    /* 209 */"#viewrows");
    /*     */
    /* 211 */if (rows == null) {
      /* 212 */rows = database.addDimension(
      /* 213 */"#viewrows");
      /*     */}
    /*     */
    /* 216 */Cube cubeViews = database.getCubeByName(
    /* 217 */"#views");
    /*     */
    /* 219 */if (cubeViews == null)
      /* 220 */database.addCube("#views",
      /* 221 */new Dimension[] { rows, columns });
    /*     */}
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name: org.palo.api.impl.views.CubeViewManager JD-Core Version:
 * 0.5.4
 */