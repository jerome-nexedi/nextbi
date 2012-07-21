/*     */package org.palo.viewapi.internal;

/*     */
/*     */import java.sql.SQLException; /*     */
import org.palo.viewapi.AuthUser; /*     */
import org.palo.viewapi.DbConnection; /*     */
import org.palo.viewapi.Right; /*     */
import org.palo.viewapi.exceptions.NoPermissionException; /*     */
import org.palo.viewapi.services.AdministrationService; /*     */
import org.palo.viewapi.services.AuthenticationService; /*     */
import org.palo.viewapi.services.FolderService; /*     */
import org.palo.viewapi.services.ReportService; /*     */
import org.palo.viewapi.services.Service; /*     */
import org.palo.viewapi.services.ServiceProvider; /*     */
import org.palo.viewapi.services.ViewService;

/*     */
/*     */public final class ServiceProviderImpl extends ServiceProvider
/*     */{
  /*     */protected final AdministrationService getAdminService(AuthUser forUser)
  /*     */{
    /* 69 */DbService.checkConnection();
    /* 70 */checkPermission(Right.GRANT, AdministrationService.class, forUser);
    /*     */
    /* 72 */return new AdminServiceImpl(forUser);
    /*     */}

  /*     */
  /*     */protected final AuthenticationService getAuthService() {
    /* 76 */DbService.checkConnection();
    /* 77 */return new AuthenticationServiceImpl();
    /*     */}

  /*     */
  /*     */protected final ReportService getReportServiceFor(AuthUser forUser) {
    /* 81 */DbService.checkConnection();
    /* 82 */checkPermission(Right.READ, ReportService.class, forUser);
    /*     */
    /* 84 */return new ReportServiceImpl(forUser);
    /*     */}

  /*     */
  /*     */protected final ViewService getViewServiceFor(AuthUser forUser) {
    /* 88 */DbService.checkConnection();
    /* 89 */checkPermission(Right.READ, ViewService.class, forUser);
    /*     */
    /* 91 */return new ViewServiceImpl(forUser);
    /*     */}

  /*     */
  /*     */protected final FolderService getFolderServiceFor(AuthUser forUser) {
    /* 95 */DbService.checkConnection();
    /* 96 */checkPermission(Right.READ, FolderService.class, forUser);
    /* 97 */return new FolderServiceImpl(forUser);
    /*     */}

  /*     */
  /*     */protected final void releaseIt(DbConnection connection) {
    /* 101 */DbService.release(connection);
    /*     */}

  /*     */
  /*     */protected final void init(DbConnection connection,
    boolean createDefaultAccounts)
  /*     */throws SQLException
  /*     */{
    /* 109 */DbService.initialize(connection, createDefaultAccounts);
    /*     */}

  /*     */
  /*     */protected final DbConnection getDbInstanceConnection() {
    /* 113 */return DbService.getDbConnection();
    /*     */}

  /*     */
  /*     */private final void checkPermission(Right right,
    Class<? extends Service> serviceClass, AuthUser forUser)
  /*     */{
    /* 118 */if (!hasPermission(right, serviceClass, forUser))
      /* 119 */throw new NoPermissionException(
      /* 120 */"User has no permission to use this service", null,
      /* 121 */forUser, right);
    /*     */}
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi
 * .jar Qualified Name: org.palo.viewapi.internal.ServiceProviderImpl JD-Core
 * Version: 0.5.4
 */