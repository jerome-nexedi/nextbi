/*    */package com.tensegrity.palojava.http.loader;

/*    */
/*    */import com.tensegrity.palojava.DbConnection; /*    */
import com.tensegrity.palojava.DimensionInfo; /*    */
import com.tensegrity.palojava.HierarchyInfo; /*    */
import com.tensegrity.palojava.loader.HierarchyLoader; /*    */
import java.util.Collection;

/*    */
/*    */public class HttpHierarchyLoader extends HierarchyLoader
/*    */{
  /*    */public HttpHierarchyLoader(DbConnection paloConnection,
    DimensionInfo dimension)
  /*    */{
    /* 54 */super(paloConnection, dimension);
    /*    */}

  /*    */
  /*    */public String[] getAllHierarchyIds() {
    /* 58 */if (!this.loaded) {
      /* 59 */reload();
      /* 60 */this.loaded = true;
      /*    */}
    /* 62 */return getLoadedIds();
    /*    */}

  /*    */
  /*    */public int getHierarchyCount() {
    /* 66 */reload();
    /* 67 */return getLoaded().size();
    /*    */}

  /*    */
  /*    */protected final void reload() {
    /* 71 */reset();
    /* 72 */HierarchyInfo[] hierInfos = this.paloConnection
      .getHierarchies(this.dimension);
    /* 73 */for (HierarchyInfo hierInfo : hierInfos)
      /* 74 */loaded(hierInfo);
    /*    */}
  /*    */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.paloapi\lib\palo.jar
 * Qualified Name: com.tensegrity.palojava.http.loader.HttpHierarchyLoader
 * JD-Core Version: 0.5.4
 */