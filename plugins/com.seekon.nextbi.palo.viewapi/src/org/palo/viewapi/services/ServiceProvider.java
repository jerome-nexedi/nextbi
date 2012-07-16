/*     */package org.palo.viewapi.services;

/*     */
/*     */import java.sql.SQLException; /*     */
import java.util.List; /*     */
import org.palo.viewapi.AuthUser; /*     */
import org.palo.viewapi.DbConnection; /*     */
import org.palo.viewapi.Group; /*     */
import org.palo.viewapi.Right; /*     */
import org.palo.viewapi.Role; /*     */
import org.palo.viewapi.internal.ServiceProviderImpl;

/*     */
/*     */public abstract class ServiceProvider
/*     */{
  /* 73 */protected static ServiceProvider instance = new ServiceProviderImpl();

  /*     */
  /*     */public static final void initialize(DbConnection connection,
    boolean createDefaultAccounts)
  /*     */{
    /*     */try
    /*     */{
      /* 88 */instance.init(connection, createDefaultAccounts);
      /*     */}
    /*     */catch (SQLException e) {
      /* 91 */throw new RuntimeException(
      /* 92 */"Failed to initialize service provider!", e);
      /*     */}
    /*     */}

  /*     */
  /*     */public static final DbConnection getDbConnection() {
    /* 97 */if (instance == null) {
      /* 98 */return null;
      /*     */}
    /* 100 */return instance.getDbInstanceConnection();
    /*     */}

  /*     */
  /*     */public static final void release(DbConnection connection)
  /*     */{
    /* 108 */instance.releaseIt(connection);
    /*     */}

  /*     */
  /*     */public static final AuthenticationService getAuthenticationService()
  /*     */{
    /* 120 */return instance.getAuthService();
    /*     */}

  /*     */
  /*     */public static final AdministrationService getAdministrationService(
    AuthUser forUser)
  /*     */{
    /* 136 */return instance.getAdminService(forUser);
    /*     */}

  /*     */
  /*     */public static final ReportService getReportService(AuthUser forUser)
  /*     */{
    /* 152 */return instance.getReportServiceFor(forUser);
    /*     */}

  /*     */
  /*     */public static final ViewService getViewService(AuthUser forUser)
  /*     */{
    /* 168 */return instance.getViewServiceFor(forUser);
    /*     */}

  /*     */
  /*     */public static final FolderService getFolderService(AuthUser forUser) {
    /* 172 */return instance.getFolderServiceFor(forUser);
    /*     */}

  /*     */
  /*     */public static final boolean hasPermission(Right right,
    Class<? extends Service> forService, AuthUser user)
  /*     */{
    /* 190 */if (isAdmin(user)) {
      /* 191 */return true;
      /*     */}
    /* 193 */if ((user != null) && (forService != null))
    /*     */{
      /* 196 */if (forService.equals(AdministrationService.class))
        /* 197 */return isAdmin(user);
      /* 198 */if ((forService.equals(ViewService.class)) ||
      /* 199 */(forService.equals(ReportService.class)) ||
      /* 200 */(forService.equals(FolderService.class))) {
        /* 201 */List<Role> roles = user.getRoles();
        /* 202 */for (Role role : roles) {
          /* 203 */if (role.hasPermission(right))
            /* 204 */return true;
          /*     */}
        /* 206 */for (Group g : user.getGroups()) {
          /* 207 */for (Role role : g.getRoles()) {
            /* 208 */if (role.hasPermission(right))
              /* 209 */return true;
            /*     */}
          /*     */}
        /*     */}
      /*     */}
    /* 214 */return false;
    /*     */}

  /*     */
  /*     */protected abstract void releaseIt(DbConnection paramDbConnection);

  /*     */
  /*     */protected abstract AuthenticationService getAuthService();

  /*     */
  /*     */protected abstract AdministrationService getAdminService(
    AuthUser paramAuthUser);

  /*     */
  /*     */protected abstract ReportService getReportServiceFor(
    AuthUser paramAuthUser);

  /*     */
  /*     */protected abstract ViewService getViewServiceFor(AuthUser paramAuthUser);

  /*     */
  /*     */protected abstract FolderService getFolderServiceFor(
    AuthUser paramAuthUser);

  /*     */
  /*     */protected abstract void init(DbConnection paramDbConnection,
    boolean paramBoolean)
  /*     */throws SQLException;

  /*     */
  /*     */protected abstract DbConnection getDbInstanceConnection();

  /*     */
  /*     */public static final boolean isAdmin(AuthUser user)
  /*     */{
    /* 241 */List<Role> roles = user.getRoles();
    /* 242 */for (Role role : roles)
      /* 243 */if ((role.getName().equalsIgnoreCase("admin")) &&
      /* 244 */(role.hasPermission(Right.GRANT)))
        /* 245 */return true;
    /* 246 */for (Group g : user.getGroups()) {
      /* 247 */for (Role role : g.getRoles())
        /* 248 */if ((role.getName().equalsIgnoreCase("admin")) &&
        /* 249 */(role.hasPermission(Right.GRANT)))
          /* 250 */return true;
      /*     */}
    /* 252 */return false;
    /*     */}
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi
 * .jar Qualified Name: org.palo.viewapi.services.ServiceProvider JD-Core
 * Version: 0.5.4
 */