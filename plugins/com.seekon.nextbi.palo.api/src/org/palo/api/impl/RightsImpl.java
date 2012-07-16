/*     */package org.palo.api.impl;

/*     */
/*     */import java.util.LinkedHashSet; /*     */
import org.palo.api.Cell; /*     */
import org.palo.api.Cube; /*     */
import org.palo.api.Database; /*     */
import org.palo.api.Dimension; /*     */
import org.palo.api.Element; /*     */
import org.palo.api.PaloObject; /*     */
import org.palo.api.Rights;

/*     */
/*     */public class RightsImpl
/*     */implements Rights
/*     */{
  /*     */public static final String SYSTEM_DATABASE = "System";

  /*     */public static final String DIMENSION_USER = "#_USER_";

  /*     */public static final String DIMENSION_USER_PROPERTIES = "#_USER_PROPERTIES_";

  /*     */public static final String DIMENSION_GROUP = "#_GROUP_";

  /*     */public static final String DIMENSION_ROLE = "#_ROLE_";

  /*     */public static final String DIMENSION_RIGHT_OBJECT = "#_RIGHT_OBJECT_";

  /*     */public static final String CUBE_USER_USER_PROPERTIES = "#_USER_USER_PROPERTIES";

  /*     */public static final String CUBE_USER_GROUP = "#_USER_GROUP";

  /*     */public static final String CUBE_ROLE_RIGHT_OBJECT = "#_ROLE_RIGHT_OBJECT";

  /*     */public static final String CUBE_GROUP_ROLE = "#_GROUP_ROLE";

  /*     */public static final String CUBE_GROUP_CUBE_DATA = "#_GROUP_CUBE_DATA";

  /*     */public static final String CUBE_GROUP_DIMENSION_DATA = "#_GROUP_DIMENSION_DATA_";

  /*     */public static final String CUBE_VIEW_GLOBAL = "#_VIEW_GLOBAL";

  /*     */public static final String CUBE_SUBSET_GLOBAL = "#_SUBSET_GLOBAL";

  /*     */private Database systemDb;

  /*     */private Cube rightsCube;

  /*     */private String[] roles;

  /*     */private String[] groups;

  /*     */private final ConnectionImpl connection;

  /*     */
  /*     */RightsImpl(ConnectionImpl connection)
  /*     */{
    /* 99 */this.connection = connection;
    /*     */}

  /*     */
  /*     */private final void init() {
    /* 103 */Database[] sysDbs = this.connection.getSystemDatabases();
    /* 104 */LinkedHashSet groupList = new LinkedHashSet();
    /* 105 */LinkedHashSet roleList = new LinkedHashSet();
    /*     */
    /* 107 */if ((sysDbs != null) && (sysDbs.length > 0)) {
      /* 108 */this.systemDb = sysDbs[0];
      /* 109 */initializeGroupsAndRoles(this.connection, groupList, roleList);
      /* 110 */this.rightsCube = this.systemDb.getCubeByName("#_ROLE_RIGHT_OBJECT");
      /*     */} else {
      /* 112 */this.systemDb = null;
      /* 113 */this.rightsCube = null;
      /*     */}
    /* 115 */this.groups = ((String[]) groupList.toArray(new String[0]));
    /* 116 */this.roles = ((String[]) roleList.toArray(new String[0]));
    /*     */}

  /*     */
  /*     */public boolean mayDelete(PaloObject object) {
    /* 120 */if (this.connection.getType() == 3) {
      /* 121 */return false;
      /*     */}
    /* 123 */return traverseRights(object, new IRightsTraverser()
    /*     */{
      /*     */public boolean checkData(Object data)
      /*     */{
        /* 127 */return (data.toString().equals("S")) ||
        /* 127 */(data.toString().equals("D"));
        /*     */}
      /*     */
    });
    /*     */}

  /*     */
  /*     */public boolean mayDelete(Class<? extends PaloObject> object) {
    /* 133 */if (this.connection.getType() == 3) {
      /* 134 */return false;
      /*     */}
    /* 136 */return traverseRights(object, new IRightsTraverser()
    /*     */{
      /*     */public boolean checkData(Object data)
      /*     */{
        /* 140 */return (data.toString().equals("S")) ||
        /* 140 */(data.toString().equals("D"));
        /*     */}
      /*     */
    });
    /*     */}

  /*     */
  /*     */public boolean mayRead(PaloObject object) {
    /* 146 */if (this.connection.getType() == 3) {
      /* 147 */return getElementForObjectType(object) != null;
      /*     */}
    /* 149 */return traverseRights(object, new IRightsTraverser()
    /*     */{
      /*     */public boolean checkData(Object data)
      /*     */{
        /* 154 */return (data.toString().equals("S")) ||
        /* 152 */(data.toString().equals("D")) ||
        /* 153 */(data.toString().equals("W")) ||
        /* 154 */(data.toString().equals("R"));
        /*     */}
      /*     */
    });
    /*     */}

  /*     */
  /*     */public boolean mayRead(Class<? extends PaloObject> object) {
    /* 160 */if (this.connection.getType() == 3) {
      /* 161 */return getElementForObjectClass(object) != null;
      /*     */}
    /* 163 */return traverseRights(object, new IRightsTraverser()
    /*     */{
      /*     */public boolean checkData(Object data)
      /*     */{
        /* 168 */return (data.toString().equals("S")) ||
        /* 166 */(data.toString().equals("D")) ||
        /* 167 */(data.toString().equals("W")) ||
        /* 168 */(data.toString().equals("R"));
        /*     */}
      /*     */
    });
    /*     */}

  /*     */
  /*     */public boolean maySplash(PaloObject object) {
    /* 174 */if (this.connection.getType() == 3) {
      /* 175 */return false;
      /*     */}
    /* 177 */return traverseRights(object, new IRightsTraverser() {
      /*     */public boolean checkData(Object data) {
        /* 179 */return data.toString().equals("S");
        /*     */}
      /*     */
    });
    /*     */}

  /*     */
  /*     */public boolean maySplash(Class<? extends PaloObject> object) {
    /* 185 */if (this.connection.getType() == 3) {
      /* 186 */return false;
      /*     */}
    /* 188 */return traverseRights(object, new IRightsTraverser() {
      /*     */public boolean checkData(Object data) {
        /* 190 */return data.toString().equals("S");
        /*     */}
      /*     */
    });
    /*     */}

  /*     */
  /*     */public boolean mayWrite(PaloObject object) {
    /* 196 */if (this.connection.getType() == 3)
      /* 197 */return false;
    /*     */final boolean ignoreW;
    /* 201 */if (object instanceof Cube)
    /*     */{
      /* 202 */if ((((Cube) object).getName().equals("#_VIEW_GLOBAL")) ||
      /* 203 */(((Cube) object).getName().equals("#_SUBSET_GLOBAL")))
        /* 204 */ignoreW = true;
      /*     */else
        /* 206 */ignoreW = false;
      /*     */}
    /*     */else {
      /* 209 */ignoreW = false;
      /*     */}
    /*     */
    /* 212 */return traverseRights(object, new IRightsTraverser()
    /*     */{
      /*     */public boolean checkData(Object data)
      /*     */{
        /* 216 */return (data.toString().equals("S")) ||
        /* 215 */(data.toString().equals("D")) || (
        /* 216 */(data.toString().equals("W")) && (!ignoreW));
        /*     */}
      /*     */
    });
    /*     */}

  /*     */
  /*     */public boolean mayWrite(Class<? extends PaloObject> object) {
    /* 222 */if (this.connection.getType() == 3) {
      /* 223 */return false;
      /*     */}
    /* 225 */return traverseRights(object, new IRightsTraverser()
    /*     */{
      /*     */public boolean checkData(Object data)
      /*     */{
        /* 229 */return (data.toString().equals("S")) ||
        /* 228 */(data.toString().equals("D")) ||
        /* 229 */(data.toString().equals("W"));
        /*     */}
      /*     */
    });
    /*     */}

  /*     */
  /*     */public void allowSplash(String group, PaloObject object) {
    /* 235 */setRight(group, object, "S");
    /*     */}

  /*     */
  /*     */public void allowDelete(String group, PaloObject object) {
    /* 239 */setRight(group, object, "D");
    /*     */}

  /*     */
  /*     */public void allowWrite(String group, PaloObject object) {
    /* 243 */if ((object instanceof Cube) && ((
    /* 244 */(((Cube) object).getName().equals("#_VIEW_GLOBAL")) ||
    /* 245 */(((Cube) object).getName().equals("#_SUBSET_GLOBAL"))))) {
      /* 246 */setRight(group, object, "D");
      /* 247 */return;
      /*     */}
    /*     */
    /* 250 */setRight(group, object, "W");
    /*     */}

  /*     */
  /*     */public void allowRead(String group, PaloObject object) {
    /* 254 */setRight(group, object, "R");
    /*     */}

  /*     */
  /*     */public void preventAccess(String group, PaloObject object) {
    /* 258 */setRight(group, object, "");
    /*     */}

  /*     */
  /*     */public void allowSplash(String role, Class<? extends PaloObject> object) {
    /* 262 */setRight(role, object, "S");
    /*     */}

  /*     */
  /*     */public void allowDelete(String role, Class<? extends PaloObject> object) {
    /* 266 */setRight(role, object, "D");
    /*     */}

  /*     */
  /*     */public void allowWrite(String role, Class<? extends PaloObject> object) {
    /* 270 */setRight(role, object, "W");
    /*     */}

  /*     */
  /*     */public void allowRead(String role, Class<? extends PaloObject> object) {
    /* 274 */setRight(role, object, "R");
    /*     */}

  /*     */
  /*     */public void preventAccess(String role, Class<? extends PaloObject> object) {
    /* 278 */setRight(role, object, "");
    /*     */}

  /*     */
  /*     */private final Element getElementForObjectType(Object object) {
    /* 282 */Dimension d = this.rightsCube.getDimensionByName("#_RIGHT_OBJECT_");
    /* 283 */if (object instanceof Database)
      /* 284 */return d.getElementByName("database");
    /* 285 */if (object instanceof Cube)
      /* 286 */return d.getElementByName("cube");
    /* 287 */if (object instanceof Dimension)
      /* 288 */return d.getElementByName("dimension");
    /* 289 */if (object instanceof Element)
      /* 290 */return d.getElementByName("dimension element");
    /* 291 */if (object instanceof Cell) {
      /* 292 */return d.getElementByName("cell data");
      /*     */}
    /* 294 */return null;
    /*     */}

  /*     */
  /*     */private final Element getElementForObjectClass(
    Class<? extends PaloObject> object) {
    /* 298 */Dimension d = this.rightsCube.getDimensionByName("#_RIGHT_OBJECT_");
    /* 299 */String name = object.getSimpleName();
    /* 300 */if (name.equals("Database"))
      /* 301 */return d.getElementByName("database");
    /* 302 */if (name.equals("Cube"))
      /* 303 */return d.getElementByName("cube");
    /* 304 */if (name.equals("Dimension"))
      /* 305 */return d.getElementByName("dimension");
    /* 306 */if (name.equals("Element"))
      /* 307 */return d.getElementByName("dimension element");
    /* 308 */if (name.equals("Cell")) {
      /* 309 */return d.getElementByName("cell data");
      /*     */}
    /* 311 */return null;
    /*     */}

  /*     */
  /*     */private final Database getDatabaseForObject(PaloObject object) {
    /* 315 */if (object instanceof Cube)
      /* 316 */return ((Cube) object).getDatabase();
    /* 317 */if (object instanceof Element) {
      /* 318 */return ((Element) object).getDimension().getDatabase();
      /*     */}
    /* 320 */return null;
    /*     */}

  /*     */
  /*     */private final void setRight(String role,
    Class<? extends PaloObject> object, String right) {
    /* 324 */if (this.connection.getType() == 3) {
      /* 325 */return;
      /*     */}
    /* 327 */init();
    /* 328 */if (!mayWrite(this.rightsCube)) {
      /* 329 */return;
      /*     */}
    /* 331 */if ((this.systemDb == null) || (this.rightsCube == null)) {
      /* 332 */return;
      /*     */}
    /* 334 */Element objectElement = getElementForObjectClass(object);
    /* 335 */if (objectElement == null) {
      /* 336 */return;
      /*     */}
    /* 338 */if ((right.equals("S")) && (!object.getSimpleName().equals("Cell"))) {
      /* 339 */return;
      /*     */}
    /* 341 */Element roleElement = this.rightsCube.getDimensionAt(0)
      .getElementByName(role);
    /* 342 */if (roleElement == null) {
      /* 343 */return;
      /*     */}
    /* 345 */this.rightsCube.setData(new Element[] { roleElement, objectElement },
      right);
    /*     */}

  /*     */
  /*     */private final void setRight(String group, PaloObject object, String right) {
    /* 349 */if (this.connection.getType() == 3) {
      /* 350 */return;
      /*     */}
    /* 352 */init();
    /* 353 */if ((this.systemDb == null) || (this.rightsCube == null)) {
      /* 354 */return;
      /*     */}
    /* 356 */if ((!(object instanceof Cube)) && (!(object instanceof Element))) {
      /* 357 */return;
      /*     */}
    /*     */
    /* 360 */Database db = getDatabaseForObject(object);
    /* 361 */if (db == null)
      /* 362 */return;
    /*     */Cube cube;
    /* 366 */if (object instanceof Cube) {
      /* 367 */cube = db.getCubeByName("#_GROUP_CUBE_DATA");
      /*     */} else {
      /* 369 */Dimension d = ((Element) object).getDimension();
      /* 370 */cube = db.getCubeByName("#_GROUP_DIMENSION_DATA_" + d.getName());
      /*     */}
    /* 372 */if (cube == null) {
      /* 373 */return;
      /*     */}
    /* 375 */Element objectElement = cube.getDimensionAt(1)
    /* 376 */.getElementByName(object.getName());
    /* 377 */if (objectElement == null) {
      /* 378 */return;
      /*     */}
    /* 380 */Element groupElement = cube.getDimensionAt(0).getElementByName(group);
    /* 381 */if (groupElement == null) {
      /* 382 */return;
      /*     */}
    /*     */
    /* 385 */cube.setData(new Element[] { groupElement, objectElement }, right);
    /*     */}

  /*     */
  /*     */private final boolean traverseRights(Class<? extends PaloObject> object,
    IRightsTraverser traverser)
  /*     */{
    /* 390 */init();
    /* 391 */if ((this.systemDb == null) || (this.rightsCube == null)) {
      /* 392 */return false;
      /*     */}
    /* 394 */Element objectElement = getElementForObjectClass(object);
    /* 395 */if (objectElement == null) {
      /* 396 */return false;
      /*     */}
    /*     */
    /* 399 */return performCheck(objectElement, traverser);
    /*     */}

  /*     */
  /*     */private final boolean traverseRights(PaloObject object,
    IRightsTraverser traverser)
  /*     */{
    /* 404 */init();
    /* 405 */if ((this.systemDb == null) || (this.rightsCube == null)) {
      /* 406 */return false;
      /*     */}
    /* 408 */Element objectElement = getElementForObjectType(object);
    /* 409 */if (objectElement == null) {
      /* 410 */return false;
      /*     */}
    /*     */
    /* 413 */if ((object instanceof Cube) || (object instanceof Element)) {
      /* 414 */boolean[] result = checkCubeOrDimensionRight(object, traverser);
      /* 415 */if (result[0]) {
        /* 416 */return result[1];
        /*     */}
      /*     */}
    /*     */
    /* 420 */return performCheck(objectElement, traverser);
    /*     */}

  /*     */
  /*     */private final boolean performCheck(Element objectElement,
    IRightsTraverser traverser)
  /*     */{
    /* 425 */for (String role : this.roles) {
      /* 426 */Element roleElement = this.rightsCube.getDimensionAt(0)
      /* 427 */.getElementByName(role);
      /* 428 */Object data = this.rightsCube.getData(new Element[] {
      /* 429 */roleElement, objectElement });
      /* 430 */if ((data != null) &&
      /* 431 */(traverser.checkData(data))) {
        /* 432 */return true;
        /*     */}
      /*     */}
    /*     */
    /* 436 */return false;
    /*     */}

  /*     */
  /*     */private final boolean[] checkCubeOrDimensionRight(PaloObject object,
    IRightsTraverser traverser)
  /*     */{
    /* 441 */Database db = getDatabaseForObject(object);
    /* 442 */if (db == null)
      /* 443 */return new boolean[2];
    /*     */Cube cube;
    /* 447 */if (object instanceof Cube) {
      /* 448 */cube = db.getCubeByName("#_GROUP_CUBE_DATA");
      /*     */} else {
      /* 450 */Dimension d = ((Element) object).getDimension();
      /* 451 */cube = db.getCubeByName("#_GROUP_DIMENSION_DATA_" + d.getName());
      /*     */}
    /*     */
    /* 454 */boolean restricted = cube != null;
    /* 455 */if (cube != null) {
      /* 456 */Element objectElement = cube.getDimensionAt(1)
      /* 457 */.getElementByName(object.getName());
      /* 458 */if (objectElement == null) {
        /* 459 */return new boolean[2];
        /*     */}
      /*     */
      /* 462 */for (String group : this.groups) {
        /* 463 */Element groupElement =
        /* 464 */cube.getDimensionAt(0).getElementByName(group);
        /* 465 */Object data = cube.getData(new Element[] {
        /* 466 */groupElement, objectElement });
        /* 467 */if (data != null) {
          /* 468 */if (data.toString().trim().length() != 0) {
            /* 469 */String result = data.toString().trim();
            /* 470 */if ((!result.equals("S")) && (!result.equals("D")) &&
            /* 471 */(!result.equals("W")) && (!result.equals("R")) &&
            /* 472 */(!result.equals("N")))
              /* 473 */restricted = false;
            /*     */}
          /*     */else {
            /* 476 */restricted = false;
            /*     */}
          /* 478 */if (traverser.checkData(data))
            /* 479 */return new boolean[] { true, true };
          /*     */}
        /*     */else {
          /* 482 */restricted = false;
          /*     */}
        /*     */}
      /*     */}
    /* 486 */return new boolean[] { restricted };
    /*     */}

  /*     */
  /*     */private final void initializeGroupsAndRoles(ConnectionImpl connection,
    LinkedHashSet<String> groupList, LinkedHashSet<String> roleList)
  /*     */{
    /* 493 */Cube cube = this.systemDb.getCubeByName("#_USER_GROUP");
    /* 494 */if (cube != null) {
      /* 495 */Element userElement = cube.getDimensionAt(0)
      /* 496 */.getElementByName(connection.getUsername());
      /* 497 */Element[] groupElements = cube.getDimensionAt(1).getElements();
      /* 498 */Object[] groupResult = cube.getDataArray(
      /* 499 */new Element[][] { { userElement }, groupElements });
      /*     */
      /* 501 */int i = 0;
      for (int n = groupResult.length; i < n; ++i) {
        /* 502 */if ((groupResult[i] == null) ||
        /* 503 */(!groupResult[i].toString().equals("1")))
          continue;
        /* 504 */groupList.add(groupElements[i].getName());
        /*     */}
      /*     */
      /* 507 */if (groupList.size() > 0)
        /* 508 */initializeRoles(groupList, roleList);
      /*     */}
    /*     */}

  /*     */
  /*     */private final void initializeRoles(LinkedHashSet<String> groupList,
    LinkedHashSet<String> roleList)
  /*     */{
    /* 515 */Cube cube = this.systemDb.getCubeByName("#_GROUP_ROLE");
    /* 516 */if (cube != null) {
      /* 517 */Element[] groupElements = new Element[groupList.size()];
      /* 518 */Dimension groupDimension = cube.getDimensionAt(0);
      /* 519 */int groupElementSize = 0;
      /* 520 */for (String elId : groupList) {
        /* 521 */groupElements[(groupElementSize++)] =
        /* 522 */groupDimension.getElementByName(elId);
        /*     */}
      /* 524 */Element[] roleElements = cube.getDimensionAt(1).getElements();
      /* 525 */Object[] groupRoleResults = cube.getDataArray(
      /* 526 */new Element[][] { groupElements, roleElements });
      /* 527 */int i = 0;
      for (int n = groupRoleResults.length; i < n; ++i) {
        /* 528 */if ((groupRoleResults[i] == null) ||
        /* 529 */(!groupRoleResults[i].toString().equals("1")))
          continue;
        /* 530 */roleList.add(roleElements[(i / groupElementSize)].getName());
        /*     */}
      /*     */}
    /*     */}

  /*     */
  /*     */static abstract interface IRightsTraverser
  /*     */{
    /*     */public abstract boolean checkData(Object paramObject);
    /*     */
  }
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name: org.palo.api.impl.RightsImpl JD-Core Version: 0.5.4
 */