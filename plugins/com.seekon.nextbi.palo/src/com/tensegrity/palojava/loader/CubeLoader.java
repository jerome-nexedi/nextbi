/*     */package com.tensegrity.palojava.loader;

/*     */
/*     */import com.tensegrity.palojava.CubeInfo; /*     */
import com.tensegrity.palojava.DatabaseInfo; /*     */
import com.tensegrity.palojava.DbConnection; /*     */
import com.tensegrity.palojava.DimensionInfo; /*     */
import com.tensegrity.palojava.PaloInfo; /*     */
import java.util.Map;

/*     */
/*     */public abstract class CubeLoader extends PaloInfoLoader
/*     */{
  /*     */protected final DatabaseInfo database;

  /*     */
  /*     */public CubeLoader(DbConnection paloConnection, DatabaseInfo database)
  /*     */{
    /* 61 */super(paloConnection);
    /* 62 */this.database = database;
    /*     */}

  /*     */
  /*     */public abstract String[] getAllCubeIds();

  /*     */
  /*     */public abstract String[] getCubeIds(int paramInt);

  /*     */
  /*     */public abstract String[] getCubeIds(DimensionInfo paramDimensionInfo);

  /*     */
  /*     */public abstract CubeInfo loadByName(String paramString);

  /*     */
  /*     */public final CubeInfo create(String name, DimensionInfo[] dimensions,
    int type)
  /*     */{
    /* 104 */CubeInfo cube = this.paloConnection.addCube(this.database, name,
      dimensions, type);
    /* 105 */loaded(cube);
    /* 106 */return cube;
    /*     */}

  /*     */
  /*     */public final boolean delete(CubeInfo cube)
  /*     */{
    /* 117 */if (this.paloConnection.delete(cube)) {
      /* 118 */removed(cube);
      /* 119 */return true;
      /*     */}
    /* 121 */return false;
    /*     */}

  /*     */
  /*     */public final CubeInfo load(String id)
  /*     */{
    /* 131 */PaloInfo cube = (PaloInfo) this.loadedInfo.get(id);
    /* 132 */if (cube == null) {
      /* 133 */cube = this.paloConnection.getCube(this.database, id);
      /* 134 */loaded(cube);
      /*     */}
    /* 136 */return (CubeInfo) cube;
    /*     */}

  /*     */
  /*     */public final CubeInfo load(int index)
  /*     */{
    /* 145 */String[] cubeIds = getAllCubeIds();
    /* 146 */if ((index < 0) || (index > cubeIds.length - 1))
      /* 147 */return null;
    /* 148 */return load(cubeIds[index]);
    /*     */}
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.paloapi\lib\palo.jar
 * Qualified Name: com.tensegrity.palojava.loader.CubeLoader JD-Core Version:
 * 0.5.4
 */