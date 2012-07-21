/*     */package org.palo.viewapi.internal;

/*     */
/*     */import java.util.Iterator; /*     */
import java.util.List; /*     */
import org.palo.viewapi.AuthUser; /*     */
import org.palo.viewapi.DomainObject; /*     */
import org.palo.viewapi.Group; /*     */
import org.palo.viewapi.GuardedObject; /*     */
import org.palo.viewapi.Right; /*     */
import org.palo.viewapi.Role; /*     */
import org.palo.viewapi.exceptions.NoPermissionException; /*     */
import org.palo.viewapi.internal.dbmappers.MapperRegistry; /*     */
import org.palo.viewapi.internal.io.CubeViewReader; /*     */
import org.palo.viewapi.services.ServiceProvider;

/*     */
/*     */final class AccessController
/*     */{
  /*     */static final boolean isAdmin(AuthUser user)
  /*     */{
    /* 61 */List<Role> roles = user.getRoles();
    /* 62 */for (Role role : roles)
      /* 63 */if ((role.getName().equalsIgnoreCase("admin")) &&
      /* 64 */(role.hasPermission(Right.GRANT)))
        /* 65 */return true;
    /* 66 */return false;
    /*     */}

  /*     */
  /*     */static final boolean hasPermission(Right right, AuthUser user)
  /*     */{
    /* 77 */List<Role> roles = user.getRoles();
    /* 78 */for (Role role : roles) {
      /* 79 */if (role.hasPermission(right))
        /* 80 */return true;
      /*     */}
    /* 82 */for (Group g : user.getGroups()) {
      /* 83 */for (Role r : g.getRoles()) {
        /* 84 */if (r.hasPermission(right)) {
          /* 85 */return true;
          /*     */}
        /*     */}
      /*     */}
    /* 89 */return false;
    /*     */}

  /*     */
  /*     */static final boolean hasPermissionIgnoreOwner(Right right,
    GuardedObject onObj, AuthUser user)
  /*     */{
    /*     */
    /* 103 */if (onObj != null) {
      /* 104 */List roles = onObj.getRoles();
      /* 105 */for (Iterator localIterator1 = roles.iterator(); localIterator1
        .hasNext();) {
        Role role = (Role) localIterator1.next();
        /* 106 */if ((user.hasRole(role)) && (role.hasPermission(right))) {
          /* 107 */return true;
          /*     */}
      }
      /*     */
      /*     */}
    /* 111 */for (Role role : user.getRoles()) {
      /* 112 */if (role.hasPermission(right)) {
        /* 113 */return true;
        /*     */}
      /*     */}
    /* 116 */for (Group g : user.getGroups()) {
      /* 117 */for (Role r : g.getRoles()) {
        /* 118 */if (r.hasPermission(right)) {
          /* 119 */return true;
          /*     */}
        /*     */}
      /*     */}
    /* 123 */return false;
    /*     */}

  /*     */
  /*     */static final boolean hasPermission(Right right, GuardedObject onObj,
    AuthUser user)
  /*     */{
    /* 137 */if ((onObj.isOwner(user)) || (ServiceProvider.isAdmin(user)))
      /* 138 */return true;
    /* 139 */List<Role> roles = onObj.getRoles();
    /* 140 */for (Role role : roles) {
      /* 141 */if ((user.hasRole(role)) && (role.hasPermission(right)))
        /* 142 */return true;
      /*     */}
    /* 144 */return false;
    /*     */}

  /*     */
  /*     */static final void checkPermission(Right right, AuthUser user)
  /*     */{
    /* 154 */if (!hasPermission(right, user)) {
      /* 155 */NoPermissionException exception = new NoPermissionException(
      /* 156 */"User has no permission !!", null, user);
      /* 157 */exception.setRequiredRight(right);
      /* 158 */throw exception;
      /*     */}
    /*     */}

  /*     */
  /*     */static final void checkPermission(Right right, GuardedObject onObj,
    AuthUser user)
  /*     */{
    /* 171 */if (!CubeViewReader.CHECK_RIGHTS) {
      /* 172 */return;
      /*     */}
    /* 174 */if (!hasPermission(right, onObj, user)) {
      /* 175 */NoPermissionException exception = new NoPermissionException(
      /* 176 */"User has no permission to access object!!", onObj, user);
      /* 177 */exception.setRequiredRight(right);
      /* 178 */throw exception;
      /*     */}
    /*     */}

  /*     */
  /*     */static final void checkAccess(Class<? extends DomainObject> forObj) {
    /* 183 */MapperRegistry.checkAccess(forObj);
    /*     */}
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi
 * .jar Qualified Name: org.palo.viewapi.internal.AccessController JD-Core
 * Version: 0.5.4
 */