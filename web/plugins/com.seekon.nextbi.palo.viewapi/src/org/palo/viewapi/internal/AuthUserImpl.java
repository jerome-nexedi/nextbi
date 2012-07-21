/*     */package org.palo.viewapi.internal;

/*     */
/*     */import java.io.PrintStream; /*     */
import java.sql.SQLException; /*     */
import java.util.ArrayList; /*     */
import java.util.List; /*     */
import org.palo.viewapi.Account; /*     */
import org.palo.viewapi.AuthUser; /*     */
import org.palo.viewapi.DbConnection; /*     */
import org.palo.viewapi.Group; /*     */
import org.palo.viewapi.GuardedObject; /*     */
import org.palo.viewapi.Right; /*     */
import org.palo.viewapi.Role; /*     */
import org.palo.viewapi.User; /*     */
import org.palo.viewapi.internal.dbmappers.MapperRegistry; /*     */
import org.palo.viewapi.services.Service;

/*     */
/*     */public final class AuthUserImpl extends UserImpl
/*     */implements AuthUser
/*     */{
  /*     */private DbConnection connection;

  /*     */
  /*     */AuthUserImpl(User user)
  /*     */{
    /* 64 */super((UserImpl) user);
    /*     */}

  /*     */
  /*     */final void login(DbConnection connection) {
    /* 68 */this.connection = connection;
    /*     */}

  /*     */
  /*     */final DbConnection getConnection() {
    /* 72 */return this.connection;
    /*     */}

  /*     */
  /*     */public final void logout() {
    /* 76 */this.connection = null;
    /*     */
    /* 79 */System.err.println("AuthUser#logout() NOT IMPLEMENTED YET!!");
    /*     */}

  /*     */
  /*     */public final List<Account> getAccounts() {
    /* 83 */return new ArrayList(this.accounts);
    /*     */}

  /*     */
  /*     */public final List<Group> getGroups() {
    /* 87 */IGroupManagement groupMgmt =
    /* 88 */MapperRegistry.getInstance().getGroupManagement();
    /* 89 */List groups = new ArrayList();
    /* 90 */for (String id : this.groups)
      /*     */try {
        /* 92 */Group group = (Group) groupMgmt.find(id);
        /* 93 */if ((group != null) && (!groups.contains(group)))
          /* 94 */groups.add(group);
        /*     */}
      /*     */catch (SQLException localSQLException) {
        /*     */}
    /* 98 */return groups;
    /*     */}

  /*     */
  /*     */public final List<Role> getRoles() {
    /* 102 */IRoleManagement roleMgmt =
    /* 103 */MapperRegistry.getInstance().getRoleManagement();
    /* 104 */List roles = new ArrayList();
    /* 105 */for (String id : this.roles)
      /*     */try {
        /* 107 */Role role = (Role) roleMgmt.find(id);
        /* 108 */if ((role != null) && (!roles.contains(role)))
          /* 109 */roles.add(role);
        /*     */}
      /*     */catch (SQLException localSQLException) {
        /*     */}
    /* 113 */return roles;
    /*     */}

  /*     */
  /*     */public final boolean hasPermission(Right right, GuardedObject forObj) {
    /* 117 */return AccessController.hasPermission(right, forObj, this);
    /*     */}

  /*     */
  /*     */public final boolean hasPermissionIgnoreOwner(Right right,
    GuardedObject forObj) {
    /* 121 */return AccessController.hasPermissionIgnoreOwner(right, forObj, this);
    /*     */}

  /*     */
  /*     */public final boolean hasPermission(Right right,
    Class<? extends Service> forService)
  /*     */{
    /* 126 */return ServiceProviderImpl.hasPermission(right, forService, this);
    /*     */}
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi
 * .jar Qualified Name: org.palo.viewapi.internal.AuthUserImpl JD-Core Version:
 * 0.5.4
 */