/*     */package com.tensegrity.palojava.loader;

/*     */
/*     */import com.tensegrity.palojava.DatabaseInfo; /*     */
import com.tensegrity.palojava.DbConnection; /*     */
import com.tensegrity.palojava.PaloInfo; /*     */
import java.util.Map;

/*     */
/*     */public abstract class DatabaseLoader extends PaloInfoLoader
/*     */{
  /*     */public DatabaseLoader(DbConnection paloConnection)
  /*     */{
    /* 56 */super(paloConnection);
    /*     */}

  /*     */
  /*     */public abstract int getDatabaseCount();

  /*     */
  /*     */public abstract String[] getAllDatabaseIds();

  /*     */
  /*     */public abstract DatabaseInfo loadByName(String paramString);

  /*     */
  /*     */public final DatabaseInfo create(String name, int type)
  /*     */{
    /* 81 */DatabaseInfo dbInfo = this.paloConnection.addDatabase(name, type);
    /* 82 */loaded(dbInfo);
    /* 83 */return dbInfo;
    /*     */}

  /*     */
  /*     */public final boolean delete(DatabaseInfo dbInfo)
  /*     */{
    /* 94 */if (this.paloConnection.delete(dbInfo)) {
      /* 95 */removed(dbInfo);
      /* 96 */return true;
      /*     */}
    /* 98 */return false;
    /*     */}

  /*     */
  /*     */public final DatabaseInfo load(String id)
  /*     */{
    /* 108 */PaloInfo db = (PaloInfo) this.loadedInfo.get(id);
    /* 109 */if (db == null) {
      /* 110 */db = this.paloConnection.getDatabase(id);
      /* 111 */loaded(db);
      /*     */}
    /* 113 */return (DatabaseInfo) db;
    /*     */}

  /*     */
  /*     */public final DatabaseInfo load(int index)
  /*     */{
    /* 122 */String[] dbIds = getAllDatabaseIds();
    /* 123 */if ((index < 0) || (index > dbIds.length - 1))
      /* 124 */return null;
    /* 125 */return load(dbIds[index]);
    /*     */}
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.paloapi\lib\palo.jar
 * Qualified Name: com.tensegrity.palojava.loader.DatabaseLoader JD-Core
 * Version: 0.5.4
 */