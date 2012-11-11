/*     */package org.palo.viewapi.internal;

/*     */
/*     */import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.palo.viewapi.Group;
import org.palo.viewapi.Role;
import org.palo.viewapi.User;
import org.palo.viewapi.internal.dbmappers.MapperRegistry;

/*     */
/*     */public final class GroupImpl extends DomainObjectImpl
/*     */implements Group
/*     */{
  /*     */private String name;

  /*     */private String descr;

  /* 59 */private final Set<String> roles = new HashSet();

  /* 60 */private final Set<String> users = new HashSet();

  /*     */
  /*     */public GroupImpl(String id)
  /*     */{
    /* 64 */super(id);
    /*     */}

  /*     */private GroupImpl(Builder builder) {
    /* 67 */super(builder.id);
    /* 68 */this.descr = builder.description;
    /* 69 */this.name = builder.name;
    /*     */
    /* 71 */this.roles.addAll(builder.roles);
    /* 72 */setUsers(builder.users);
    /*     */}

  /*     */
  /*     */public final String getDescription() {
    /* 76 */return this.descr;
    /*     */}

  /*     */
  /*     */public final String getName() {
    /* 80 */return this.name;
    /*     */}

  /*     */
  /*     */public final List<Role> getRoles() {
    /* 84 */IRoleManagement roleMgmt =
    /* 85 */MapperRegistry.getInstance().getRoleManagement();
    /* 86 */List allRoles = new ArrayList();
    /* 87 */for (String id : this.roles)
      /*     */try {
        /* 89 */Role role = (Role) roleMgmt.find(id);
        /* 90 */if ((role != null) && (!allRoles.contains(role)))
          /* 91 */allRoles.add(role);
        /*     */}
      /*     */catch (SQLException localSQLException) {
        /*     */}
    /* 95 */return allRoles;
    /*     */}

  /*     */public final List<String> getRoleIDs() {
    /* 98 */return new ArrayList(this.roles);
    /*     */}

  /*     */
  /*     */public final List<User> getUsers() {
    /* 102 */IUserManagement usrMgmt = MapperRegistry.getInstance()
      .getUserManagement();
    /* 103 */List allUsers = new ArrayList();
    /* 104 */for (String id : this.users)
      /*     */try {
        /* 106 */User user = (User) usrMgmt.find(id);
        /* 107 */if ((user != null) && (!allUsers.contains(user)))
          /* 108 */allUsers.add(user);
        /*     */}
      /*     */catch (SQLException localSQLException) {
        /*     */}
    /* 112 */return allUsers;
    /*     */}

  /*     */public final List<String> getUserIDs() {
    /* 115 */return new ArrayList(this.users);
    /*     */}

  /*     */
  /*     */public final boolean hasMember(User user) {
    /* 119 */return this.users.contains(user.getId());
    /*     */}

  /*     */
  /*     */final void add(User user)
  /*     */{
    /* 126 */this.users.add(user.getId());
    /*     */}

  /*     */final void add(Role role) {
    /* 129 */this.roles.add(role.getId());
    /*     */}

  /*     */final void remove(User user) {
    /* 132 */this.users.remove(user.getId());
    /*     */}

  /*     */final void remove(Role role) {
    /* 135 */this.roles.remove(role.getId());
    /*     */}

  /*     */final void setDescription(String descr) {
    /* 138 */this.descr = descr;
    /*     */}

  /*     */final void setName(String name) {
    /* 141 */this.name = name;
    /*     */}

  /*     */final void setRoles(List<Role> roles) {
    /* 144 */this.roles.clear();
    /* 145 */if (roles != null)
      /* 146 */for (Role role : roles)
        /* 147 */this.roles.add(role.getId());
    /*     */}

  /*     */
  /*     */final void setUsers(List<String> users) {
    /* 151 */this.users.clear();
    /* 152 */if (users != null)
      /* 153 */this.users.addAll(users);
    /*     */}

  /*     */
  /*     */public static final class Builder
  /*     */{
    /*     */private final String id;

    /*     */private String name;

    /*     */private String description;

    /* 165 */private final List<String> roles = new ArrayList();

    /* 166 */private final List<String> users = new ArrayList();

    /*     */
    /*     */public Builder(String id) {
      /* 169 */AccessController.checkAccess(Group.class);
      /* 170 */this.id = id;
      /*     */}

    /*     */
    /*     */public Builder description(String description) {
      /* 174 */this.description = description;
      /* 175 */return this;
      /*     */}

    /*     */public Builder name(String name) {
      /* 178 */this.name = name;
      /* 179 */return this;
      /*     */}

    /*     */public Builder roles(List<String> roles) {
      /* 182 */this.roles.clear();
      /* 183 */if (roles != null)
        /* 184 */this.roles.addAll(roles);
      /* 185 */return this;
      /*     */}

    /*     */public Builder users(List<String> users) {
      /* 188 */this.users.clear();
      /* 189 */if (users != null)
        /* 190 */this.users.addAll(users);
      /* 191 */return this;
      /*     */}

    /*     */
    /*     */public Group build() {
      /* 195 */return new GroupImpl(this);
      /*     */}
    /*     */
  }
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi
 * .jar Qualified Name: org.palo.viewapi.internal.GroupImpl JD-Core Version:
 * 0.5.4
 */