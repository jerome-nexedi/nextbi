/*     */package org.palo.viewapi.internal;

/*     */
/*     */import java.sql.SQLException; /*     */
import java.util.ArrayList; /*     */
import java.util.List; /*     */
import org.palo.api.Connection; /*     */
import org.palo.api.Cube; /*     */
import org.palo.api.Database; /*     */
import org.palo.api.exceptions.PaloIOException; /*     */
import org.palo.viewapi.Account; /*     */
import org.palo.viewapi.AuthUser; /*     */
import org.palo.viewapi.CubeView; /*     */
import org.palo.viewapi.PaloConnection; /*     */
import org.palo.viewapi.Right; /*     */
import org.palo.viewapi.Role; /*     */
import org.palo.viewapi.User; /*     */
import org.palo.viewapi.View; /*     */
import org.palo.viewapi.exceptions.NoAccountException; /*     */
import org.palo.viewapi.exceptions.OperationFailedException; /*     */
import org.palo.viewapi.internal.cubeview.CubeViewFactory; /*     */
import org.palo.viewapi.internal.io.CubeViewIO; /*     */
import org.palo.viewapi.services.ViewService;

/*     */
/*     */final class ViewServiceImpl extends InternalService
/*     */implements ViewService
/*     */{
  /*     */ViewServiceImpl(AuthUser user)
  /*     */{
    /* 69 */super(user);
    /*     */}

  /*     */
  /*     */public final View createView(String name, Cube cube, AuthUser authUser,
    String sessionId, String externalId)
  /*     */throws OperationFailedException
  /*     */{
    /* 75 */AccessController.checkPermission(Right.CREATE, this.user);
    /*     */try
    /*     */{
      /* 78 */Account forAccount = getAccount(cube);
      /* 79 */ViewImpl view = new ViewImpl(null);
      /* 80 */view.setName(name);
      /* 81 */view.setOwner(this.user);
      /*     */try
      /*     */{
        /* 84 */view.setAccount(authUser, forAccount, sessionId);
        /*     */} catch (PaloIOException e) {
        /* 86 */throw new OperationFailedException(e.getMessage(), e);
        /*     */}
      /* 88 */view.setCube(cube.getId());
      /* 89 */view.setDatabase(cube.getDatabase().getId());
      /*     */
      /* 91 */Role ownerRole = (Role) getRoleManagement().findByName("OWNER");
      /* 92 */if (ownerRole != null) {
        /* 93 */this.user.add(ownerRole);
        /*     */}
      /*     */
      /* 97 */getViewManagement().insert(view);
      /*     */
      /* 101 */CubeView cubeView = CubeViewFactory.createView(view, cube, authUser,
        externalId);
      /* 102 */view.setDefinition(CubeViewIO.toXML(cubeView));
      /* 103 */return view;
      /*     */} catch (SQLException e) {
      /* 105 */throw new OperationFailedException("Failed to create view", e);
      /*     */}
    /*     */}

  /*     */
  /*     */public final void delete(View view) throws OperationFailedException {
    /* 110 */AccessController.checkPermission(Right.DELETE, view, this.user);
    /*     */try
    /*     */{
      /* 113 */getViewManagement().delete(view);
      /*     */} catch (SQLException e) {
      /* 115 */throw new OperationFailedException("Failed to delete view", e);
      /*     */}
    /*     */}

  /*     */
  /*     */public final void save(View view) throws OperationFailedException {
    /* 120 */AccessController.checkPermission(Right.WRITE, view, this.user);
    /*     */try
    /*     */{
      /* 125 */CubeView cubeView = view.getCubeView();
      /* 126 */if (cubeView != null) {
        /* 127 */String xml = CubeViewIO.toXML(cubeView);
        /* 128 */ViewImpl _view = (ViewImpl) view;
        /* 129 */_view.setDefinition(xml, false);
        /*     */}
      /* 131 */getViewManagement().update(view);
      /*     */} catch (SQLException e) {
      /* 133 */throw new OperationFailedException("Failed to save view", e);
      /*     */}
    /*     */}

  /*     */
  /*     */public final List<View> getViews(Account forAccount) {
    /* 138 */AccessController.checkPermission(Right.READ, this.user);
    /* 139 */IViewManagement viewMgmt = getViewManagement();
    /* 140 */List result = new ArrayList();
    /*     */try {
      /* 142 */List<View> views = viewMgmt.findViews(forAccount);
      /* 143 */for (View view : views)
        /* 144 */if (view.isOwner(this.user))
          /* 145 */result.add(view);
        /*     */else
          /* 147 */for (Role role : view.getRoles())
            /* 148 */if (this.user.hasRole(role)) {
              /* 149 */result.add(view);
              /* 150 */break;
              /*     */}
      /*     */}
    /*     */catch (SQLException localSQLException)
    /*     */{
      /*     */}
    /* 156 */return result;
    /*     */}

  /*     */
  /*     */public final boolean hasViews(Account forAccount) {
    /* 160 */AccessController.checkPermission(Right.READ, this.user);
    /* 161 */IViewManagement viewMgmt = getViewManagement();
    /*     */try {
      /* 163 */return viewMgmt.hasViews(forAccount);
      /*     */} catch (SQLException localSQLException) {
      /*     */}
    /* 166 */return false;
    /*     */}

  /*     */
  /*     */public final View getView(String id) {
    /* 170 */AccessController.checkPermission(Right.READ, this.user);
    /* 171 */ViewImpl view = null;
    /*     */try {
      /* 173 */view = (ViewImpl) getViewManagement().find(id);
      /*     */} catch (SQLException localSQLException) {
      /*     */}
    /* 176 */return view;
    /*     */}

  /*     */
  /*     */public final boolean doesViewExist(String viewName, Cube forCube) {
    /* 180 */return getViewByName(viewName, forCube) != null;
    /*     */}

  /*     */
  /*     */public final View getViewByName(String name, Cube cube)
  /*     */{
    /* 185 */AccessController.checkPermission(Right.READ, this.user);
    /*     */try
    /*     */{
      /* 188 */Account account = getAccount(cube);
      /* 189 */return getViewManagement().findByName(name, cube, account);
      /*     */} catch (SQLException localSQLException) {
      /*     */}
    /* 192 */return null;
    /*     */}

  /*     */
  /*     */public final void add(Role role, View toView)
    throws OperationFailedException
  /*     */{
    /* 197 */if (!toView.hasRole(role)) {
      /* 198 */AccessController.checkPermission(Right.WRITE, toView, this.user);
      /* 199 */ViewImpl view = (ViewImpl) toView;
      /* 200 */view.add(role);
      /*     */try {
        /* 202 */getViewManagement().update(view);
        /*     */}
      /*     */catch (SQLException e) {
        /* 205 */view.remove(role);
        /* 206 */throw new OperationFailedException("Failed to modify view", e);
        /*     */}
      /*     */}
    /*     */}

  /*     */
  /*     */public final void remove(Role role, View fromView)
    throws OperationFailedException
  /*     */{
    /* 213 */if (fromView.hasRole(role)) {
      /* 214 */AccessController.checkPermission(Right.WRITE, fromView, this.user);
      /*     */
      /* 216 */ViewImpl view = (ViewImpl) fromView;
      /* 217 */view.remove(role);
      /*     */try {
        /* 219 */getViewManagement().update(view);
        /*     */}
      /*     */catch (SQLException e) {
        /* 222 */view.add(role);
        /* 223 */throw new OperationFailedException("Failed to modify view", e);
        /*     */}
      /*     */}
    /*     */}

  /*     */
  /*     */public final void setCube(String id, View ofView) {
    /* 229 */AccessController.checkPermission(Right.WRITE, ofView, this.user);
    /* 230 */ViewImpl view = (ViewImpl) ofView;
    /* 231 */view.setCube(id);
    /*     */}

  /*     */
  /*     */public final void setDatabase(String id, View ofView) {
    /* 235 */AccessController.checkPermission(Right.WRITE, ofView, this.user);
    /* 236 */ViewImpl view = (ViewImpl) ofView;
    /* 237 */view.setDatabase(id);
    /*     */}

  /*     */
  /*     */public final void setDefinition(String xml, View ofView) {
    /* 241 */AccessController.checkPermission(Right.WRITE, ofView, this.user);
    /* 242 */ViewImpl view = (ViewImpl) ofView;
    /* 243 */view.setDefinition(xml);
    /*     */}

  /*     */
  /*     */public final void setName(String name, View ofView) {
    /* 247 */AccessController.checkPermission(Right.WRITE, ofView, this.user);
    /* 248 */ViewImpl view = (ViewImpl) ofView;
    /* 249 */view.setName(name);
    /*     */}

  /*     */
  /*     */public final void setOwner(User owner, View ofView) {
    /* 253 */AccessController.checkPermission(Right.WRITE, ofView, this.user);
    /* 254 */((ViewImpl) ofView).setOwner(owner);
    /*     */}

  /*     */
  /*     */public final void setAccount(Account acc, View ofView) {
    /* 258 */((ViewImpl) ofView).setAccount(acc);
    /*     */}

  /*     */
  /*     */private final Account getAccount(Cube cube) throws SQLException {
    /* 262 */Connection paloCon = cube.getDatabase().getConnection();
    /* 263 */PaloConnection connection = getConnectionManagement().findBy(
    /* 264 */paloCon.getServer(), paloCon.getService());
    /* 265 */return getAccount(connection, paloCon.getUsername());
    /*     */}

  /*     */
  /*     */private final Account getAccount(PaloConnection forConnection,
    String login) throws SQLException
  /*     */{
    /* 270 */Account account = getAccountManagement().findBy(login, forConnection);
    /* 271 */if (account == null)
      /* 272 */throw new NoAccountException(this.user, forConnection, "User '" +
      /* 273 */this.user.getLastname() + "' has no account on '" +
      /* 274 */forConnection.getHost() + "'");
    /* 275 */return account;
    /*     */}

  /*     */
  /*     */public View createViewAsSubobject(String name, Cube cube,
    AuthUser authUser, String sessionId, String externalId)
  /*     */throws OperationFailedException
  /*     */{
    /* 282 */AccessController.checkPermission(Right.CREATE, this.user);
    /*     */try
    /*     */{
      /* 285 */Account forAccount = getAccount(cube);
      /* 286 */ViewImpl view = new ViewImpl(null);
      /* 287 */view.setName(name);
      /* 288 */view.setOwner(this.user);
      /*     */try
      /*     */{
        /* 291 */view.setAccount(authUser, forAccount, sessionId);
        /*     */} catch (PaloIOException e) {
        /* 293 */throw new OperationFailedException(e.getMessage(), e);
        /*     */}
      /* 295 */view.setCube(cube.getId());
      /* 296 */view.setDatabase(cube.getDatabase().getId());
      /*     */
      /* 298 */Role ownerRole = (Role) getRoleManagement().findByName("OWNER");
      /* 299 */if (ownerRole != null) {
        /* 300 */this.user.add(ownerRole);
        /*     */}
      /*     */
      /* 304 */CubeView cubeView = CubeViewFactory.createView(view, cube, authUser,
        externalId);
      /* 305 */view.setDefinition(CubeViewIO.toXML(cubeView));
      /* 306 */return view;
      /*     */} catch (SQLException e) {
      /* 308 */throw new OperationFailedException("Failed to create view", e);
      /*     */}
    /*     */}
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi
 * .jar Qualified Name: org.palo.viewapi.internal.ViewServiceImpl JD-Core
 * Version: 0.5.4
 */