/*     */package org.palo.viewapi.internal;

/*     */
/*     */import java.util.HashSet;
import java.util.Set;

import org.palo.api.Connection;
import org.palo.api.Cube;
import org.palo.api.Database;
import org.palo.api.exceptions.PaloIOException;
import org.palo.viewapi.Account;
import org.palo.viewapi.AuthUser;
import org.palo.viewapi.CubeView;
import org.palo.viewapi.PaloAccount;
import org.palo.viewapi.Role;
import org.palo.viewapi.User;
import org.palo.viewapi.View;
import org.palo.viewapi.internal.io.CubeViewIO;

/*     */
/*     */public final class ViewImpl extends GuardedObjectImpl
/*     */implements View
/*     */{
  /*     */private String xml;

  /*     */private String name;

  /*     */private String cube;

  /*     */private String database;

  /*     */private Account account;

  /*     */private CubeView cubeView;

  /* 71 */private boolean isBusy = false;

  /*     */
  /*     */ViewImpl(String id) {
    /* 74 */super(id);
    /*     */}

  /*     */private ViewImpl(Builder builder) {
    /* 77 */super(builder.id);
    /* 78 */this.xml = builder.xml;
    /* 79 */this.name = builder.name;
    /* 80 */this.owner = builder.owner;
    /* 81 */this.cube = builder.cube;
    /* 82 */this.database = builder.database;
    /* 83 */this.account = builder.account;
    /* 84 */for (Role role : builder.roles)
      /* 85 */add(role);
    /*     */}

  /*     */
  /*     */public final synchronized CubeView createCubeView(AuthUser user,
    String sessionId) throws PaloIOException {
    /* 89 */if (this.cubeView == null) {
      /* 90 */while (this.isBusy)
        /*     */try {
          /* 92 */Thread.sleep(1000L);
          /*     */}
        /*     */catch (InterruptedException localInterruptedException) {
        }
      /*     */
      /* 96 */this.isBusy = true;
      /*     */Cube cube;
      /*     */try {
        /* 98 */cube = getPaloCube(sessionId);
        /* 99 */if (cube == null)
          /*     */return null;
        try {
          cubeView = CubeViewIO.fromXML(user, this, cube, xml);
        } catch (PaloIOException e) {
          throw e;
        }

        /*     */}
      /*     */finally {
        /* 106 */this.isBusy = false;
      }
      this.isBusy = false;
      /*     */}
    /*     */
    /* 110 */return this.cubeView;
    /*     */}

  /*     */
  /*     */public final CubeView getCubeView() {
    /* 114 */return this.cubeView;
    /*     */}

  /*     */
  /*     */public final void setCubeView(CubeView cubeView) {
    /* 118 */this.cubeView = cubeView;
    /*     */}

  /*     */
  /*     */public final Account getAccount() {
    /* 122 */return this.account;
    /*     */}

  /*     */
  /*     */public final String getCubeId() {
    /* 126 */return this.cube;
    /*     */}

  /*     */
  /*     */public final String getDatabaseId() {
    /* 130 */return this.database;
    /*     */}

  /*     */
  /*     */public final String getDefinition() {
    /* 134 */return (this.xml != null) ? this.xml : "";
    /*     */}

  /*     */
  /*     */public final String getName() {
    /* 138 */return (this.name != null) ? this.name : "";
    /*     */}

  /*     */
  /*     */public final void setName(String name) {
    /* 142 */this.name = name;
    /*     */}

  /*     */
  /*     */public Object getDefaultValue(String parameterName)
  /*     */{
    /* 157 */return null;
    /*     */}

  /*     */
  /*     */public String[] getParameterNames()
  /*     */{
    /* 170 */return new String[0];
    /*     */}

  /*     */
  /*     */public Object getParameterValue(String parameterName)
  /*     */{
    /* 183 */return null;
    /*     */}

  /*     */
  /*     */public boolean isParameterized()
  /*     */{
    /* 196 */return false;
    /*     */}

  /*     */
  /*     */public void setParameter(String parameterName, Object parameterValue)
  /*     */{
    /*     */}

  /*     */
  /*     */public void addParameterValue(String parameterName, Object parameterValue)
  /*     */{
    /*     */}

  /*     */
  /*     */public void setParameterNames(String[] parameterNames)
  /*     */{
    /*     */}

  /*     */
  /*     */public final boolean equals(Object obj)
  /*     */{
    /* 235 */if (obj instanceof ViewImpl) {
      /* 236 */ViewImpl other = (ViewImpl) obj;
      /* 237 */boolean equal = (getId().equals(other.getId())) &&
      /* 238 */(getName().equals(other.name)) &&
      /* 239 */(this.account.equals(other.account));
      /* 240 */if (this.cube != null)
        /* 241 */equal = (equal) && (this.cube.equals(other.cube));
      /*     */else
        /* 243 */equal = (equal) && (other.cube == null);
      /* 244 */if (this.database != null)
        /* 245 */equal = (equal) && (this.database.equals(other.database));
      /*     */else
        /* 247 */equal = (equal) && (other.database == null);
      /* 248 */return equal;
      /*     */}
    /* 250 */return false;
    /*     */}

  /*     */
  /*     */public final int hashCode() {
    /* 254 */int hc = 17;
    /* 255 */hc += getId().hashCode();
    /* 256 */hc += getName().hashCode();
    /* 257 */if (this.cube != null)
      /* 258 */hc += this.cube.hashCode();
    /* 259 */if (this.database != null)
      /* 260 */hc += this.database.hashCode();
    /* 261 */hc += this.account.hashCode();
    /* 262 */return hc;
    /*     */}

  /*     */
  /*     */public final synchronized void setAccount(AuthUser user,
    Account account, String sessionId)
  /*     */throws PaloIOException
  /*     */{
    /* 269 */Account oldAccount = this.account;
    /* 270 */this.account = account;
    /*     */
    /* 272 */if ((account == null)
      || ((oldAccount != null) && (account.getId().equals(oldAccount.getId()))) ||
      /* 273 */(this.cubeView == null))
      return;
    /* 274 */this.cubeView = null;
    /*     */try {
      /* 276 */this.cubeView = createCubeView(user, sessionId);
      /*     */} finally {
      /* 278 */ConnectionPoolManager.getInstance().disconnect(account, sessionId,
        "ViewImpl.setAccount");
      /*     */}
    /*     */}

  /*     */
  /*     */public final synchronized void setAccount(Account account)
  /*     */{
    /* 284 */this.account = account;
    /*     */}

  /*     */
  /*     */final void setCube(String id) {
    /* 288 */this.cube = id;
    /*     */}

  /*     */final void setDatabase(String id) {
    /* 291 */this.database = id;
    /*     */}

  /*     */final void setDefinition(String xml) {
    /* 294 */setDefinition(xml, true);
    /*     */}

  /*     */final void setDefinition(String xml, boolean invalidateCubeView) {
    /* 297 */if (invalidateCubeView)
      /* 298 */this.cubeView = null;
    /* 299 */this.xml = xml;
    /*     */}

  /*     */
  /*     */private final synchronized Cube getPaloCube(String sessionId)
  /*     */{
    /* 304 */if (!(this.account instanceof PaloAccount)) {
      /* 305 */return null;
      /*     */}
    /* 307 */ServerConnectionPool pool =
    /* 308 */ConnectionPoolManager.getInstance().getPool(this.account, sessionId);
    /*     */
    /* 310 */Connection paloConnection =
    /* 312 */pool.getConnection("ViewImpl.getPaloCube");
    /* 313 */Database db = paloConnection.getDatabaseById(this.database);
    /* 314 */if (db == null) {
      /* 315 */return null;
      /*     */}
    /* 317 */return db.getCubeById(this.cube);
    /*     */}

  /*     */
  /*     */public static final class Builder
  /*     */{
    /*     */private final String id;

    /*     */private String xml;

    /*     */private String name;

    /*     */private User owner;

    /*     */private String cube;

    /*     */private String database;

    /*     */private Account account;

    /* 332 */private Set<Role> roles = new HashSet();

    /*     */
    /*     */public Builder(String id) {
      /* 335 */AccessController.checkAccess(View.class);
      /* 336 */this.id = id;
      /*     */}

    /*     */
    /*     */public Builder definition(String xml) {
      /* 340 */this.xml = xml;
      /* 341 */return this;
      /*     */}

    /*     */public Builder name(String name) {
      /* 344 */this.name = name;
      /* 345 */return this;
      /*     */}

    /*     */public Builder owner(User owner) {
      /* 348 */this.owner = owner;
      /* 349 */return this;
      /*     */}

    /*     */public Builder cube(String cube) {
      /* 352 */this.cube = cube;
      /* 353 */return this;
      /*     */}

    /*     */public Builder database(String database) {
      /* 356 */this.database = database;
      /* 357 */return this;
      /*     */}

    /*     */public Builder account(Account account) {
      /* 360 */this.account = account;
      /* 361 */return this;
      /*     */}

    /*     */public Builder add(Role role) {
      /* 364 */this.roles.add(role);
      /* 365 */return this;
      /*     */}

    /*     */public View build() {
      /* 368 */return new ViewImpl(this);
      /*     */}
    /*     */
  }
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi
 * .jar Qualified Name: org.palo.viewapi.internal.ViewImpl JD-Core Version:
 * 0.5.4
 */