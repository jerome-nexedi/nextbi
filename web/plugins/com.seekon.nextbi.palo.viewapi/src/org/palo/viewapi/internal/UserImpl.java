/*     */package org.palo.viewapi.internal;

/*     */
/*     */import java.security.MessageDigest;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.palo.viewapi.Account;
import org.palo.viewapi.Group;
import org.palo.viewapi.Role;
import org.palo.viewapi.User;
import org.palo.viewapi.internal.dbmappers.MapperRegistry;

import sun.misc.BASE64Encoder;

/*     */
/*     */public class UserImpl extends DomainObjectImpl
/*     */implements User
/*     */{
  /*     */private String firstname;

  /*     */private String lastname;

  /*     */private String login;

  /*     */private String password;

  /* 66 */protected final Set<String> roles = new LinkedHashSet();

  /* 67 */protected final Set<String> groups = new LinkedHashSet();

  /* 68 */protected final Set<Account> accounts = new LinkedHashSet();

  /*     */
  /*     */UserImpl(String id)
  /*     */{
    /* 72 */super(id);
    /*     */}

  /*     */UserImpl(UserImpl user) {
    /* 75 */super(user.getId());
    /* 76 */this.firstname = user.firstname;
    /* 77 */this.lastname = user.lastname;
    /* 78 */this.login = user.login;
    /* 79 */this.password = user.password;
    /*     */
    /* 81 */this.roles.addAll(user.roles);
    /* 82 */this.groups.addAll(user.groups);
    /* 83 */this.accounts.addAll(user.accounts);
    /*     */}

  /*     */private UserImpl(Builder builder) {
    /* 86 */super(builder.id);
    /* 87 */this.firstname = builder.firstname;
    /* 88 */this.lastname = builder.lastname;
    /* 89 */this.login = builder.login;
    /* 90 */this.password = builder.password;
    /*     */
    /* 92 */this.roles.addAll(builder.roles);
    /* 93 */this.groups.addAll(builder.groups);
    /* 94 */this.accounts.addAll(builder.accounts);
    /*     */}

  /*     */
  /*     */public final String getFirstname() {
    /* 98 */return this.firstname;
    /*     */}

  /*     */public final String getLastname() {
    /* 101 */return this.lastname;
    /*     */}

  /*     */public final String getLoginName() {
    /* 104 */return (this.login != null) ? this.login : "";
    /*     */}

  /*     */public final String getPassword() {
    /* 107 */return this.password;
    /*     */}

  /*     */
  /*     */public final boolean isMemberOf(Group group) {
    /* 111 */return this.groups.contains(group.getId());
    /*     */}

  /*     */
  /*     */private final List<Group> getGroupList() {
    /* 115 */IGroupManagement groupMgmt =
    /* 116 */MapperRegistry.getInstance().getGroupManagement();
    /* 117 */List groups = new ArrayList();
    /* 118 */for (String id : this.groups)
      /*     */try {
        /* 120 */Group group = (Group) groupMgmt.find(id);
        /* 121 */if ((group != null) && (!groups.contains(group)))
          /* 122 */groups.add(group);
        /*     */}
      /*     */catch (SQLException localSQLException) {
        /*     */}
    /* 126 */return groups;
    /*     */}

  /*     */
  /*     */public final boolean hasRole(Role role) {
    /* 130 */boolean result = this.roles.contains(role.getId());
    /* 131 */if (!result) {
      /* 132 */for (Group g : getGroupList()) {
        /* 133 */for (Role r : g.getRoles()) {
          /* 134 */if (r.getId().equals(role.getId())) {
            /* 135 */return true;
            /*     */}
          /*     */}
        /*     */}
      /*     */}
    /* 140 */return result;
    /*     */}

  /*     */
  /*     */public final boolean equals(Object obj) {
    /* 144 */if (obj instanceof User) {
      /* 145 */UserImpl other = (UserImpl) obj;
      /*     */
      /* 149 */return (getId().equals(other.getId())) &&
      /* 149 */(getLoginName().equals(other.getLoginName()));
      /*     */}
    /* 151 */return false;
    /*     */}

  /*     */
  /*     */public final int hashCode() {
    /* 155 */int hc = 17;
    /* 156 */hc += 31 * getId().hashCode();
    /* 157 */hc += 31 * getLoginName().hashCode();
    /* 158 */return hc;
    /*     */}

  /*     */
  /*     */public final List<String> getGroupIDs()
  /*     */{
    /* 165 */AccessController.checkAccess(User.class);
    /* 166 */return new ArrayList(this.groups);
    /*     */}

  /*     */public final List<String> getRoleIDs() {
    /* 169 */AccessController.checkAccess(User.class);
    /* 170 */return new ArrayList(this.roles);
    /*     */}

  /*     */public List<Account> getAccounts() {
    /* 173 */AccessController.checkAccess(User.class);
    /* 174 */return new ArrayList(this.accounts);
    /*     */}

  /*     */
  /*     */final void setFirstname(String firstname) {
    /* 178 */this.firstname = firstname;
    /*     */}

  /*     */final void setLastname(String lastname) {
    /* 181 */this.lastname = lastname;
    /*     */}

  /*     */final void setLoginName(String login) {
    /* 184 */this.login = login;
    /*     */}

  /*     */final void setPassword(String password) {
    /* 187 */this.password = password;
    /*     */}

  /*     */final void add(Role role) {
    /* 190 */this.roles.add(role.getId());
    /*     */}

  /*     */final void add(Group group) {
    /* 193 */this.groups.add(group.getId());
    /*     */}

  /*     */final void add(Account account) {
    /* 196 */if (account != null)
      /* 197 */this.accounts.add(account);
    /*     */}

  /*     */
  /*     */final void remove(Account account) {
    /* 200 */this.accounts.remove(account);
    /*     */}

  /*     */final void remove(Role role) {
    /* 203 */this.roles.remove(role.getId());
    /*     */}

  /*     */final void remove(Group group) {
    /* 206 */this.groups.remove(group.getId());
    /*     */}

  /*     */final void setRoles(List<Role> roles) {
    /* 209 */this.roles.clear();
    /* 210 */if (roles != null)
      /* 211 */for (Role role : roles)
        /* 212 */this.roles.add(role.getId());
    /*     */}

  /*     */
  /*     */final void setGroups(List<Group> groups)
  /*     */{
    /* 222 */this.groups.clear();
    /* 223 */if (groups != null)
      /* 224 */for (Group group : groups)
        /* 225 */this.groups.add(group.getId());
    /*     */}

  /*     */
  /*     */final void setAccounts(List<Account> accounts) {
    /* 229 */this.accounts.clear();
    /* 230 */if (accounts != null)
      /* 231 */this.accounts.addAll(accounts);
    /*     */}

  /*     */
  /*     */public static final String encrypt(String pass)
  /*     */{
    /* 237 */if ((pass == null) || (pass.equals(""))) {
      /* 238 */return "";
      /*     */}
    /* 240 */MessageDigest md = null;
    /*     */try {
      /* 242 */md = MessageDigest.getInstance("MD5");
      /* 243 */md.update(pass.getBytes("UTF-8"));
      /* 244 */byte[] raw = md.digest();
      /* 245 */String hash = new BASE64Encoder().encode(raw);
      /* 246 */return hash;
      /*     */}
    /*     */catch (Exception e) {
      /* 249 */e.printStackTrace();
      /*     */}
    /* 251 */return pass;
    /*     */}

  /*     */
  /*     */public static final class Builder
  /*     */{
    /*     */private final String id;

    /*     */private String firstname;

    /*     */private String lastname;

    /*     */private String login;

    /*     */private String password;

    /* 265 */private final Set<String> roles = new LinkedHashSet();

    /* 266 */private final Set<String> groups = new LinkedHashSet();

    /* 267 */private final Set<Account> accounts = new LinkedHashSet();

    /*     */
    /*     */public Builder(String id)
    /*     */{
      /* 271 */AccessController.checkAccess(User.class);
      /* 272 */this.id = id;
      /*     */}

    /*     */
    /*     */public Builder firstname(String firstname) {
      /* 276 */this.firstname = firstname;
      /* 277 */return this;
      /*     */}

    /*     */public Builder lastname(String lastname) {
      /* 280 */this.lastname = lastname;
      /* 281 */return this;
      /*     */}

    /*     */public Builder login(String login) {
      /* 284 */this.login = login;
      /* 285 */return this;
      /*     */}

    /*     */public Builder password(String password) {
      /* 288 */this.password = password;
      /* 289 */return this;
      /*     */}

    /*     */
    /*     */public Builder groups(List<String> groups)
    /*     */{
      /* 296 */this.groups.clear();
      /* 297 */if (groups != null)
        /* 298 */this.groups.addAll(groups);
      /* 299 */return this;
      /*     */}

    /*     */public Builder roles(List<String> roles) {
      /* 302 */this.roles.clear();
      /* 303 */if (roles != null)
        /* 304 */this.roles.addAll(roles);
      /* 305 */return this;
      /*     */}

    /*     */public Builder accounts(List<Account> accounts) {
      /* 308 */this.accounts.clear();
      /* 309 */if (accounts != null)
        /* 310 */this.accounts.addAll(accounts);
      /* 311 */return this;
      /*     */}

    /*     */
    /*     */public Builder add(Account account) {
      /* 315 */this.accounts.add(account);
      /* 316 */return this;
      /*     */}

    /*     */public User build() {
      /* 319 */return new UserImpl(this);
      /*     */}
    /*     */
  }
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi
 * .jar Qualified Name: org.palo.viewapi.internal.UserImpl JD-Core Version:
 * 0.5.4
 */