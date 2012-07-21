/*     */package com.tensegrity.palojava.http.handlers;

/*     */
/*     */import com.tensegrity.palojava.DatabaseInfo; /*     */
import com.tensegrity.palojava.PaloException; /*     */
import com.tensegrity.palojava.ServerInfo; /*     */
import com.tensegrity.palojava.http.HttpConnection; /*     */
import com.tensegrity.palojava.http.builders.DatabaseInfoBuilder; /*     */
import com.tensegrity.palojava.http.builders.InfoBuilderRegistry; /*     */
import com.tensegrity.palojava.http.builders.ServerInfoBuilder; /*     */
import java.io.IOException;

/*     */
/*     */public class ServerHandler extends HttpHandler
/*     */{
  /*     */private static final String LOAD = "/server/load?-";

  /*     */private static final String LIST_NORMAL_DATABASES = "/server/databases?show_normal=1&show_system=0&show_user_info=0";

  /*     */private static final String LIST_SYSTEM_DATABASES = "/server/databases?show_normal=0&show_system=1&show_user_info=0";

  /*     */private static final String LIST_USER_INFO_DATABASES = "/server/databases?show_normal=0&show_system=0&show_user_info=1";

  /*     */private static final String LIST_ALL_DATABASES = "/server/databases?show_normal=1&show_system=1&show_user_info=1";

  /*     */private static final String INFO = "/server/info?-";

  /*     */private static final String SAVE = "/server/save?-";

  /*     */private static final String SHUTDOWN = "/server/shutdown?-";

  /*     */private static final String LOGIN = "/server/login?user=";

  /*     */private static final String LOGOUT = "/server/logout?-";

  /* 73 */private static final ServerHandler instance = new ServerHandler();

  /*     */private final InfoBuilderRegistry builderReg;

  /*     */
  /*     */static final ServerHandler getInstance(HttpConnection connection)
  /*     */{
    /* 75 */instance.use(connection);
    /* 76 */return instance;
    /*     */}

  /*     */
  /*     */private ServerHandler()
  /*     */{
    /* 84 */this.builderReg = InfoBuilderRegistry.getInstance();
    /*     */}

  /*     */
  /*     */public final DatabaseInfo[] getDatabases() throws IOException
  /*     */{
    /* 89 */String[][] response = request("/server/databases?show_normal=1&show_system=1&show_user_info=1");
    /* 90 */DatabaseInfo[] databases = new DatabaseInfo[response.length];
    /*     */
    /* 96 */DatabaseInfoBuilder databaseBuilder =
    /* 97 */this.builderReg.getDatabaseBuilder();
    /* 98 */for (int i = 0; i < databases.length; ++i) {
      /* 99 */databases[i] = databaseBuilder.create(null, response[i]);
      /*     */}
    /*     */
    /* 102 */return databases;
    /*     */}

  /*     */public final DatabaseInfo[] getSystemDatabases() throws IOException {
    /* 105 */String[][] response = request("/server/databases?show_normal=0&show_system=1&show_user_info=0");
    /* 106 */DatabaseInfo[] databases = new DatabaseInfo[response.length];
    /*     */
    /* 112 */DatabaseInfoBuilder databaseBuilder =
    /* 113 */this.builderReg.getDatabaseBuilder();
    /* 114 */for (int i = 0; i < databases.length; ++i) {
      /* 115 */databases[i] = databaseBuilder.create(null, response[i]);
      /*     */}
    /*     */
    /* 118 */return databases;
    /*     */}

  /*     */
  /*     */public final DatabaseInfo[] getUserInfoDatabases() throws IOException {
    /* 122 */String[][] response = request("/server/databases?show_normal=0&show_system=0&show_user_info=1");
    /* 123 */DatabaseInfo[] databases = new DatabaseInfo[response.length];
    /* 124 */DatabaseInfoBuilder databaseBuilder =
    /* 125 */this.builderReg.getDatabaseBuilder();
    /* 126 */for (int i = 0; i < databases.length; ++i) {
      /* 127 */databases[i] = databaseBuilder.create(null, response[i]);
      /*     */}
    /*     */
    /* 130 */return databases;
    /*     */}

  /*     */
  /*     */public final DatabaseInfo[] getNormalDatabases() throws IOException {
    /* 134 */String[][] response = request("/server/databases?show_normal=1&show_system=0&show_user_info=0");
    /* 135 */DatabaseInfo[] databases = new DatabaseInfo[response.length];
    /*     */
    /* 141 */DatabaseInfoBuilder databaseBuilder =
    /* 142 */this.builderReg.getDatabaseBuilder();
    /* 143 */for (int i = 0; i < databases.length; ++i) {
      /* 144 */databases[i] = databaseBuilder.create(null, response[i]);
      /*     */}
    /*     */
    /* 147 */return databases;
    /*     */}

  /*     */
  /*     */public final ServerInfo getInfo()
  /*     */throws IOException
  /*     */{
    /* 153 */String[][] response = request("/server/info?-", false, true);
    /* 154 */if (response.length == 0)
      /* 155 */response = new String[][] { { "" } };
    /* 156 */ServerInfoBuilder srvBuilder = this.builderReg.getServerBuilder();
    /* 157 */return srvBuilder.create(null, response[0]);
    /*     */}

  /*     */
  /*     */public final boolean load() throws IOException
  /*     */{
    /* 162 */String[][] response = request("/server/load?-");
    /* 163 */return response[0][0].equals("1");
    /*     */}

  /*     */
  /*     */public final String[] login(String user, String password)
    throws IOException
  /*     */{
    /* 168 */StringBuffer query = new StringBuffer();
    /* 169 */query.append("/server/login?user=");
    /* 170 */query.append(user);
    /* 171 */query.append("&password=");
    /* 172 */query.append(password);
    /* 173 */String[][] response = request(query.toString(), false, true);
    /* 174 */if (response.length == 0)
      /* 175 */throw new PaloException("Unknown palo server!");
    /* 176 */return response[0];
    /*     */}

  /*     */
  /*     */public final boolean logout() throws IOException
  /*     */{
    /* 181 */String[][] response = request("/server/logout?-");
    /* 182 */return response[0][0].equals("1");
    /*     */}

  /*     */
  /*     */public final boolean save()
  /*     */throws IOException
  /*     */{
    /* 231 */String[][] response = request("/server/save?-");
    /* 232 */return response[0][0].equals("1");
    /*     */}

  /*     */
  /*     */public final boolean shutdown() throws IOException
  /*     */{
    /* 237 */String[][] response = request("/server/shutdown?-");
    /* 238 */return response[0][0].equals("1");
    /*     */}
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.paloapi\lib\palo.jar
 * Qualified Name: com.tensegrity.palojava.http.handlers.ServerHandler JD-Core
 * Version: 0.5.4
 */