/*     */package org.palo.viewapi.internal;

/*     */
/*     */import java.io.PrintStream; /*     */
import java.sql.SQLException; /*     */
import org.palo.viewapi.AuthUser; /*     */
import org.palo.viewapi.Role; /*     */
import org.palo.viewapi.User; /*     */
import org.palo.viewapi.exceptions.AuthenticationFailedException; /*     */
import org.palo.viewapi.internal.dbmappers.MapperRegistry; /*     */
import org.palo.viewapi.services.AuthenticationService;

/*     */
/*     */class AuthenticationServiceImpl
/*     */implements AuthenticationService
/*     */{
  /*     */public AuthUser authenticate(String login, String password)
  /*     */throws AuthenticationFailedException
  /*     */{
    /*     */try
    /*     */{
      /* 66 */String pass = AuthUserImpl.encrypt(password);
      /*     */
      /* 68 */IUserManagement usrManager =
      /* 69 */MapperRegistry.getInstance().getUserManagement();
      /*     */
      /* 71 */User user = (User) usrManager.findByName(login);
      /* 72 */if (user == null) {
        /* 73 */throw new AuthenticationFailedException("Unknown user name!");
        /*     */}
      /* 75 */if (!user.getPassword().equals(pass)) {
        /* 76 */throw new AuthenticationFailedException("Wrong password!");
        /*     */}
      /*     */
      /* 79 */AuthUserImpl authUser = new AuthUserImpl(user);
      /*     */
      /* 82 */return authUser;
      /*     */} catch (SQLException e) {
      /* 84 */System.err.println("Error during authentication!");
      /* 85 */throw new AuthenticationFailedException(
      /* 86 */"Error during authentication!", e);
      /*     */}
    /*     */}

  /*     */
  /*     */public AuthUser authenticateHash(String login, String password)
  /*     */throws AuthenticationFailedException
  /*     */{
    /*     */try
    /*     */{
      /* 100 */IUserManagement usrManager = MapperRegistry.getInstance()
      /* 101 */.getUserManagement();
      /*     */
      /* 103 */User user = (User) usrManager.findByName(login);
      /* 104 */if (user == null) {
        /* 105 */throw new AuthenticationFailedException("Unknown user name!");
        /*     */}
      /* 107 */if (!user.getPassword().equals(password)) {
        /* 108 */throw new AuthenticationFailedException("Wrong password!");
        /*     */}
      /*     */
      /* 112 */AuthUserImpl authUser = new AuthUserImpl(user);
      /*     */
      /* 115 */return authUser;
      /*     */} catch (SQLException e) {
      /* 117 */System.err.println("Error during authentication!");
      /* 118 */throw new AuthenticationFailedException(
      /* 119 */"Error during authentication!", e);
      /*     */}
    /*     */}

  /*     */
  /*     */public AuthUser authenticateAdmin() throws SQLException {
    /* 124 */IUserManagement usrManager = MapperRegistry.getInstance()
      .getUserManagement();
    /* 125 */IUserRoleManagement urManager = MapperRegistry.getInstance()
      .getUserRoleAssociation();
    /* 126 */IRoleManagement roleManager = MapperRegistry.getInstance()
      .getRoleManagement();
    /*     */
    /* 128 */Role adminRole = null;
    /* 129 */for (Role r : roleManager.findAll()) {
      /* 130 */if (r.getName().equalsIgnoreCase("admin")) {
        /* 131 */adminRole = r;
        /* 132 */break;
        /*     */}
      /*     */}
    /* 135 */if (adminRole == null) {
      /* 136 */return null;
      /*     */}
    /*     */
    /* 139 */label169: for (String usr : urManager.getUsers(adminRole)) {
      /* 140 */User user = (User) usrManager.find(usr);
      /* 141 */if (user == null)
        continue;
      /*     */try {
        /* 143 */AuthUser authUser = authenticateHash(user.getLoginName(), user
          .getPassword());
        /* 144 */if (authUser == null)
          break label169;
        /* 145 */return authUser;
        /*     */}
      /*     */catch (Throwable localThrowable)
      /*     */{
        /*     */}
      /*     */}
    /* 151 */return null;
    /*     */}
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi
 * .jar Qualified Name: org.palo.viewapi.internal.AuthenticationServiceImpl
 * JD-Core Version: 0.5.4
 */