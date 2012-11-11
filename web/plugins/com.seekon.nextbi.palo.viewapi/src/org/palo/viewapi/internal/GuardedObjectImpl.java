/*     */package org.palo.viewapi.internal;

/*     */
/*     */import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.palo.viewapi.GuardedObject;
import org.palo.viewapi.Role;
import org.palo.viewapi.User;
import org.palo.viewapi.internal.dbmappers.MapperRegistry;

/*     */
/*     */abstract class GuardedObjectImpl extends DomainObjectImpl
/*     */implements GuardedObject
/*     */{
  /*     */protected User owner;

  /*     */private final Set<String> roles;

  /*     */
  /*     */public GuardedObjectImpl(String id)
  /*     */{
    /* 63 */super(id);
    /* 64 */this.roles = new HashSet();
    /*     */}

  /*     */
  /*     */public final List<Role> getRoles() {
    /* 68 */IRoleManagement roleMgmt =
    /* 69 */MapperRegistry.getInstance().getRoleManagement();
    /* 70 */List allRoles = new ArrayList();
    /* 71 */for (String id : this.roles)
      /*     */try {
        /* 73 */Role role = (Role) roleMgmt.find(id);
        /* 74 */if ((role != null) && (!allRoles.contains(role)))
          /* 75 */allRoles.add(role);
        /*     */}
      /*     */catch (SQLException localSQLException) {
        /*     */}
    /* 79 */return allRoles;
    /*     */}

  /*     */
  /*     */public final boolean hasRole(Role role) {
    /* 83 */return this.roles.contains(role.getId());
    /*     */}

  /*     */
  /*     */public final List<String> getRoleIDs() {
    /* 87 */return new ArrayList(this.roles);
    /*     */}

  /*     */
  /*     */final User getOwnerInternal() {
    /* 91 */return this.owner;
    /*     */}

  /*     */public final User getOwner() {
    /* 94 */return this.owner;
    /*     */}

  /*     */public final boolean isOwner(User user) {
    /* 97 */return (this.owner != null) && (this.owner.equals(user));
    /*     */}

  /*     */
  /*     */protected final void add(Role role)
  /*     */{
    /* 104 */this.roles.add(role.getId());
    /*     */}

  /*     */protected final void setRoles(Set<String> roles) {
    /* 107 */this.roles.clear();
    /* 108 */if (roles != null)
      /* 109 */this.roles.addAll(roles);
    /*     */}

  /*     */
  /*     */final void remove(Role role) {
    /* 113 */this.roles.remove(role.getId());
    /*     */}

  /*     */final void setOwner(User owner) {
    /* 116 */this.owner = owner;
    /*     */}
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi
 * .jar Qualified Name: org.palo.viewapi.internal.GuardedObjectImpl JD-Core
 * Version: 0.5.4
 */