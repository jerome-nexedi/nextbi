package com.tensegrity.palo.xmla.ext.views;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class SQLConnection {
  private static final String FAV_VTABLE_NAME = "XMLAFavorites";

  private static final String FAV_VTABLE_COL_HOST_NAME = "host";

  private static final String FAV_VTABLE_COL_SERVICE_NAME = "service";

  private static final String FAV_VTABLE_COL_USER_NAME = "user";

  private static final String FAV_VTABLE_COL_VIEW_DESC_NAME = "favoriteViews";

  private static final int FAV_VTABLE_COL_VIEW_DESC_IDX = 4;

  private static final String VTABLE_NAME = "XMLAViews";

  private static final String VTABLE_COL_HOST_NAME = "host";

  private static final String VTABLE_COL_SERVICE_NAME = "service";

  private static final String VTABLE_COL_USER_NAME = "user";

  private static final String VTABLE_COL_DATABASE_ID_NAME = "databaseId";

  private static final String VTABLE_COL_VIEW_ID_NAME = "viewId";

  private static final String VTABLE_COL_VIEW_DESC_NAME = "viewDesc";

  private static final int VTABLE_COL_HOST_IDX = 1;

  private static final int VTABLE_COL_SERVICE_IDX = 2;

  private static final int VTABLE_COL_USER_IDX = 3;

  private static final int VTABLE_COL_DATABASE_ID_IDX = 4;

  private static final int VTABLE_COL_VIEW_ID_IDX = 5;

  private static final int VTABLE_COL_VIEW_DESC_IDX = 6;

  private Connection sqlConnection;

  private boolean viewTableExists;

  private boolean favoriteViewTableExists;

  private static String jdbcDriverName = null;

  private static String jdbcConnectString = null;

  private static String hostName = null;

  private static String userName = null;

  private static String userPassword = null;

  private final String trimHost(String paramString) {
    paramString = paramString.trim();
    if ((paramString.startsWith("http://")) && (paramString.length() > 7))
      paramString = paramString.trim().substring(7);
    return paramString;
  }

  public SQLConnection() {
    if (jdbcDriverName != null)
      initialize(jdbcDriverName);
    if ((jdbcConnectString == null) || (hostName == null) || (userName == null))
      return;
    connect(jdbcConnectString, hostName, userName, userPassword);
  }

  private void shutdown() throws SQLException {
    if (this.sqlConnection == null)
      return;
    try {
      Statement localStatement = this.sqlConnection.createStatement();
      localStatement.execute("SHUTDOWN");
    } catch (Exception localException) {
    }
    this.sqlConnection.close();
  }

  public final boolean connect(String paramString1, String paramString2,
    String paramString3, String paramString4) {
    try {
      this.sqlConnection = doConnect(paramString1, paramString2, paramString3,
        paramString4);
      if (this.sqlConnection != null) {
        this.viewTableExists = viewTableExists(this.sqlConnection);
        this.favoriteViewTableExists = favoriteViewTableExists(this.sqlConnection);
      }
    } catch (SQLException localSQLException) {
      return false;
    }
    return this.sqlConnection != null;
  }

  public final boolean close() {
    if (this.sqlConnection == null)
      return true;
    try {
      shutdown();
      this.sqlConnection = null;
    } catch (SQLException localSQLException) {
      return false;
    }
    return true;
  }

  public final boolean writeView(String paramString1, String paramString2,
    String paramString3, String paramString4, String paramString5,
    String paramString6) {
    if (this.sqlConnection == null)
      return false;
    if (!this.viewTableExists)
      this.viewTableExists = createViewTable(this.sqlConnection);
    if (!this.viewTableExists)
      return false;
    paramString1 = trimHost(paramString1);
    return createOrUpdateView(this.sqlConnection, paramString1, paramString2,
      paramString3, paramString4, paramString5, paramString6);
  }

  public final boolean writeFavoriteViews(String paramString1, String paramString2,
    String paramString3, String paramString4) {
    if (this.sqlConnection == null)
      return false;
    if (!this.favoriteViewTableExists)
      this.favoriteViewTableExists = createFavoriteViewsTable(this.sqlConnection);
    if (!this.favoriteViewTableExists)
      return false;
    paramString1 = trimHost(paramString1);
    return createOrUpdateFavoriteView(this.sqlConnection, paramString1,
      paramString2, paramString3, paramString4);
  }

  public final boolean deleteView(String paramString1, String paramString2,
    String paramString3, String paramString4, String paramString5) {
    paramString1 = trimHost(paramString1);
    return executeQuery("DELETE FROM XMLAViews WHERE host='" + paramString1
      + "' AND " + "service" + "='" + paramString2 + "' AND " + "databaseId" + "='"
      + paramString4 + "' AND " + "viewId" + "='" + paramString5 + "'");
  }

  public final boolean deleteViewTable() {
    boolean bool = executeQuery("DROP TABLE XMLAViews");
    if (bool)
      this.viewTableExists = false;
    return bool;
  }

  public final String loadView(String paramString1, String paramString2,
    String paramString3, String paramString4, String paramString5) {
    if (this.sqlConnection == null)
      return "";
    paramString1 = trimHost(paramString1);
    Statement localStatement = null;
    ResultSet localResultSet = null;
    String str2;
    try {
      localStatement = this.sqlConnection.createStatement();
      String str1 = "SELECT * FROM XMLAViews WHERE host='" + paramString1 + "' AND "
        + "service" + "='" + paramString2 + "' AND " + "databaseId" + "='"
        + paramString4 + "' AND " + "viewId" + "='" + paramString5 + "'";
      localResultSet = localStatement.executeQuery(str1);
      if (localResultSet.next()) {
        str2 = localResultSet.getString(6);
        return str2;
      }
    } catch (SQLException localSQLException3) {
      str2 = "";
      return str2;
    } finally {
      if (localStatement != null)
        try {
          localStatement.close();
        } catch (SQLException localSQLException8) {
        }
      if (localResultSet != null)
        try {
          localResultSet.close();
        } catch (SQLException localSQLException9) {
        }
    }
    return "";
  }

  public final String loadFavoriteView(String paramString1, String paramString2,
    String paramString3) {
    if (this.sqlConnection == null)
      return "";
    paramString1 = trimHost(paramString1);
    Statement localStatement = null;
    ResultSet localResultSet = null;
    String str2;
    try {
      localStatement = this.sqlConnection.createStatement();
      String str1 = "SELECT * FROM XMLAFavorites WHERE host='" + paramString1
        + "' AND " + "service" + "='" + paramString2 + "'";
      localResultSet = localStatement.executeQuery(str1);
      if (localResultSet.next()) {
        str2 = localResultSet.getString(4);
        return str2;
      }
    } catch (SQLException localSQLException3) {
      str2 = "";
      return str2;
    } finally {
      if (localStatement != null)
        try {
          localStatement.close();
        } catch (SQLException localSQLException8) {
        }
      if (localResultSet != null)
        try {
          localResultSet.close();
        } catch (SQLException localSQLException9) {
        }
    }
    return "";
  }

  public final String[] getAllViewIds(String paramString1, String paramString2,
    String paramString3) {
    paramString1 = trimHost(paramString1);
    return getSQLResponseFor("SELECT * FROM XMLAViews WHERE host='" + paramString1
      + "' AND " + "service" + "='" + paramString2 + "'", 5);
  }

  public final String[] getAllViewIds(String paramString1, String paramString2,
    String paramString3, String paramString4) {
    paramString1 = trimHost(paramString1);
    String[] arrayOfString = getSQLResponseFor(
      "SELECT * FROM XMLAViews WHERE host='" + paramString1 + "' AND " + "service"
        + "='" + paramString2 + "' AND " + "databaseId" + "='" + paramString4 + "'",
      5);
    return arrayOfString;
  }

  private final String getCubeIdFromId(String paramString) {
    if ((paramString == null) || (paramString.length() == 0))
      return "";
    String[] arrayOfString = paramString.split("_@_");
    if ((arrayOfString == null) || (arrayOfString.length < 6))
      return "";
    return arrayOfString[3];
  }

  public final String[] getAllViewIds(String paramString1, String paramString2,
    String paramString3, String paramString4, String paramString5) {
    paramString2 = trimHost(paramString2);
    String[] arrayOfString1 = getSQLResponseFor(
      "SELECT * FROM XMLAViews WHERE host='" + paramString2 + "' AND " + "service"
        + "='" + paramString3 + "' AND " + "databaseId" + "='" + paramString5 + "'",
      5);
    ArrayList localArrayList = new ArrayList();
    for (String str : arrayOfString1) {
      if (!getCubeIdFromId(str).equals(paramString1))
        continue;
      localArrayList.add(str);
    }
    return (String[]) localArrayList.toArray(new String[0]);
  }

  public final String[] getAllDatabaseIds(String paramString1, String paramString2,
    String paramString3) {
    paramString1 = trimHost(paramString1);
    return getSQLResponseFor("SELECT databaseId FROM XMLAViews WHERE host='"
      + paramString1 + "' AND " + "service" + "='" + paramString2 + "'", 4);
  }

  private final boolean initialize(String paramString) {
    try {
      Class.forName(paramString);
    } catch (ClassNotFoundException localClassNotFoundException) {
      return false;
    }
    return true;
  }

  private final Connection doConnect(String paramString1, String paramString2,
    String paramString3, String paramString4) throws SQLException {
    return DriverManager.getConnection(paramString1 + ":" + paramString2,
      paramString3, paramString4);
  }

  private final boolean viewTableExists(Connection paramConnection) {
    return executeQuery("SELECT * FROM XMLAViews");
  }

  private final boolean favoriteViewTableExists(Connection paramConnection) {
    return executeQuery("SELECT * FROM XMLAFavorites");
  }

  private final boolean createViewTable(Connection paramConnection) {
    return executeQuery("CREATE TABLE XMLAViews(host VARCHAR, service VARCHAR, user VARCHAR, databaseId VARCHAR, viewId VARCHAR(25) PRIMARY KEY, viewDesc VARCHAR)");
  }

  private final boolean createFavoriteViewsTable(Connection paramConnection) {
    return executeQuery("CREATE TABLE XMLAFavorites(host VARCHAR, service VARCHAR, user VARCHAR, favoriteViews VARCHAR, PRIMARY KEY (host, service, user))");
  }

  private final boolean createOrUpdateView(Connection paramConnection,
    String paramString1, String paramString2, String paramString3,
    String paramString4, String paramString5, String paramString6) {
    Statement localStatement = null;
    paramString1 = trimHost(paramString1);
    try {
      localStatement = paramConnection.createStatement();
      String str = "UPDATE XMLAViews SET viewDesc='" + paramString6 + "' WHERE "
        + "host" + "='" + paramString1 + "' AND " + "service" + "='" + paramString2
        + "' AND " + "user" + "='" + paramString3 + "' AND " + "databaseId" + "='"
        + paramString4 + "' AND " + "viewId" + "='" + paramString5 + "'";
      int i = localStatement.executeUpdate(str);
      int j;
      if (i == 0) {
        str = "INSERT INTO XMLAViews(host, service, user, databaseId, viewId, viewDesc) VALUES ('"
          + paramString1
          + "', '"
          + paramString2
          + "', '"
          + paramString3
          + "', '"
          + paramString4 + "', '" + paramString5 + "', '" + paramString6 + "')";
        i = localStatement.executeUpdate(str);
      }
      if (i == 1) {
        return true;
      }
    } catch (SQLException localSQLException3) {
    } finally {
      if (localStatement != null)
        try {
          localStatement.close();
        } catch (SQLException localSQLException6) {
        }
    }
    return false;
  }

  private final boolean createOrUpdateFavoriteView(Connection paramConnection,
    String paramString1, String paramString2, String paramString3,
    String paramString4) {
    Statement localStatement = null;
    paramString1 = trimHost(paramString1);
    try {
      localStatement = paramConnection.createStatement();
      String str = "UPDATE XMLAFavorites SET favoriteViews='" + paramString4
        + "' WHERE " + "host" + "='" + paramString1 + "' AND " + "service" + "='"
        + paramString2 + "' AND " + "user" + "='" + paramString3 + "'";
      int i = localStatement.executeUpdate(str);
      if (i == 0) {
        str = "INSERT INTO XMLAFavorites(host, service, user, favoriteViews) VALUES ('"
          + paramString1
          + "', '"
          + paramString2
          + "', '"
          + paramString3
          + "', '"
          + paramString4 + "')";
        i = localStatement.executeUpdate(str);
      }
      if (i == 1) {
        return true;
      }
    } catch (SQLException localSQLException3) {
    } finally {
      if (localStatement != null)
        try {
          localStatement.close();
        } catch (SQLException localSQLException6) {
        }
    }
    return false;
  }

  private final boolean executeQuery(String paramString) {
    if (this.sqlConnection == null)
      return false;
    Statement localStatement = null;
    try {
      localStatement = this.sqlConnection.createStatement();
      localStatement.executeQuery(paramString);
    } catch (SQLException localSQLException2) {
      return false;
    } finally {
      if (localStatement != null)
        try {
          localStatement.close();
        } catch (SQLException localSQLException4) {
        }
    }
    return true;
  }

  private final String[] getSQLResponseFor(String paramString, int paramInt) {
    if (this.sqlConnection == null)
      return new String[0];
    Statement localStatement = null;
    ResultSet localResultSet = null;
    String[] arrayOfString;
    try {
      localStatement = this.sqlConnection.createStatement();
      localResultSet = localStatement.executeQuery(paramString);
      ArrayList localArrayList = new ArrayList();
      while (localResultSet.next())
        localArrayList.add(localResultSet.getString(paramInt));
      arrayOfString = (String[]) localArrayList.toArray(new String[0]);
      return arrayOfString;
    } catch (SQLException localSQLException1) {
      arrayOfString = new String[0];
      return arrayOfString;
    } finally {
      if (localStatement != null)
        try {
          localStatement.close();
        } catch (SQLException localSQLException6) {
        }
      if (localResultSet != null)
        try {
          localResultSet.close();
        } catch (SQLException localSQLException7) {
        }
    }
  }

  static {
    jdbcDriverName = System.getProperty("jdbcDriverName");
    jdbcConnectString = System.getProperty("jdbcConnectString");
    hostName = System.getProperty("metaDataSqlHost");
    userName = System.getProperty("metaDataSqlUser");
    userPassword = System.getProperty("metaDataSqlPass");
  }
}

/*
 * Location:
 * D:\server\apache-tomcat-5.5.20\webapps\Palo-Pivot\WEB-INF\lib\paloxmla.jar
 * Qualified Name: com.tensegrity.palo.xmla.ext.views.SQLConnection JD-Core
 * Version: 0.5.4
 */