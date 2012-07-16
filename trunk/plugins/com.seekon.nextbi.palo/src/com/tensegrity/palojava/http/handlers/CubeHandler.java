/*     */package com.tensegrity.palojava.http.handlers;

/*     */
/*     */import com.tensegrity.palojava.CubeInfo; /*     */
import com.tensegrity.palojava.DatabaseInfo; /*     */
import com.tensegrity.palojava.DimensionInfo; /*     */
import com.tensegrity.palojava.ElementInfo; /*     */
import com.tensegrity.palojava.LockInfo; /*     */
import com.tensegrity.palojava.PaloException; /*     */
import com.tensegrity.palojava.RuleInfo; /*     */
import com.tensegrity.palojava.http.HttpConnection; /*     */
import com.tensegrity.palojava.http.builders.CubeInfoBuilder; /*     */
import com.tensegrity.palojava.http.builders.InfoBuilderRegistry; /*     */
import com.tensegrity.palojava.http.builders.LockInfoBuilder; /*     */
import com.tensegrity.palojava.http.builders.RuleInfoBuilder; /*     */
import com.tensegrity.palojava.impl.CubeInfoImpl; /*     */
import java.io.IOException;

/*     */
/*     */public class CubeHandler extends HttpHandler
/*     */{
  /*     */private static final String CLEAR_AREA_PREFIX = "/cube/clear?database=";

  /*     */private static final String CREATE_PREFIX = "/cube/create?database=";

  /*     */private static final String DELETE_PREFIX = "/cube/destroy?database=";

  /*     */private static final String INFO_PREFIX = "/cube/info?database=";

  /*     */private static final String LOAD_PREFIX = "/cube/load?database=";

  /*     */private static final String RENAME_PREFIX = "/cube/rename?database=";

  /*     */private static final String SAVE_PREFIX = "/cube/save?database=";

  /*     */private static final String UNLOAD_PREFIX = "/cube/unload?database=";

  /*     */private static final String RULES_PREFIX = "/cube/rules?database=";

  /*     */private static final String LOCK_PREFIX = "/cube/lock?database=";

  /*     */private static final String LIST_LOCKS_PREFIX = "/cube/locks?database=";

  /*     */private static final String ROLLBACK_PREFIX = "/cube/rollback?database=";

  /*     */private static final String COMMIT_PREFIX = "/cube/commit?database=";

  /*     */private static final String CONVERT_PREFIX = "/cube/convert?database=";

  /* 93 */private static final CubeHandler instance = new CubeHandler();

  /*     */private final InfoBuilderRegistry builderReg;

  /*     */
  /*     */static final CubeHandler getInstance(HttpConnection connection)
  /*     */{
    /* 95 */instance.use(connection);
    /* 96 */return instance;
    /*     */}

  /*     */
  /*     */private CubeHandler()
  /*     */{
    /* 104 */this.builderReg = InfoBuilderRegistry.getInstance();
    /*     */}

  /*     */
  /*     */public final CubeInfo clear(CubeInfo cube, ElementInfo[][] area,
    boolean complete)
  /*     */throws IOException
  /*     */{
    /* 112 */if ((area == null) || (area.length == 0))
      /* 113 */complete = true;
    /* 114 */DatabaseInfo database = cube.getDatabase();
    /* 115 */StringBuffer query = new StringBuffer();
    /* 116 */query.append("/cube/clear?database=");
    /* 117 */query.append(database.getId());
    /* 118 */query.append("&cube=");
    /* 119 */query.append(cube.getId());
    /* 120 */if (complete) {
      /* 121 */query.append("&complete=1");
      /*     */} else {
      /* 123 */String paths = getAreaString(area);
      /* 124 */query.append("&area=");
      /* 125 */query.append(paths);
      /*     */}
    /* 127 */String[][] response = request(query.toString());
    /*     */
    /* 129 */if (response[0].length < 7) {
      /* 130 */String[] _response = new String[8];
      /* 131 */System.arraycopy(response, 0, _response, 0, 6);
      /* 132 */_response[7] = Integer.toString(cube.getType());
      /* 133 */_response[8] = Integer.toString(cube.getToken());
      /* 134 */response[0] = _response;
      /*     */}
    /* 136 */CubeInfoBuilder cubeBuilder = this.builderReg.getCubeBuilder();
    /* 137 */return cubeBuilder.create(database, response[0]);
    /*     */}

  /*     */
  /*     */public final CubeInfo create(DatabaseInfo database, String name,
    DimensionInfo[] dimensions)
  /*     */throws IOException
  /*     */{
    /* 144 */String idStr = getIdString(dimensions);
    /* 145 */StringBuffer query = new StringBuffer();
    /* 146 */query.append("/cube/create?database=");
    /* 147 */query.append(database.getId());
    /* 148 */query.append("&new_name=");
    /* 149 */query.append(encode(name));
    /* 150 */query.append("&dimensions=");
    /* 151 */query.append(idStr);
    /* 152 */String[][] response = request(query.toString());
    /* 153 */CubeInfoBuilder cubeBuilder = this.builderReg.getCubeBuilder();
    /* 154 */return cubeBuilder.create(database, response[0]);
    /*     */}

  /*     */
  /*     */public final CubeInfo create(DatabaseInfo database, String name,
    DimensionInfo[] dimensions, int type)
  /*     */throws IOException
  /*     */{
    /* 160 */String idStr = getIdString(dimensions);
    /* 161 */StringBuffer query = new StringBuffer();
    /* 162 */query.append("/cube/create?database=");
    /* 163 */query.append(database.getId());
    /* 164 */query.append("&new_name=");
    /* 165 */query.append(encode(name));
    /* 166 */query.append("&dimensions=");
    /* 167 */query.append(idStr);
    /* 168 */query.append("&type=");
    /* 169 */query.append(type);
    /* 170 */String[][] response = request(query.toString());
    /* 171 */CubeInfoBuilder cubeBuilder = this.builderReg.getCubeBuilder();
    /* 172 */return cubeBuilder.create(database, response[0]);
    /*     */}

  /*     */
  /*     */public final boolean destroy(CubeInfo cube) throws IOException
  /*     */{
    /* 177 */DatabaseInfo database = cube.getDatabase();
    /* 178 */StringBuffer request = new StringBuffer();
    /* 179 */request.append("/cube/destroy?database=");
    /* 180 */request.append(database.getId());
    /* 181 */request.append("&cube=");
    /* 182 */request.append(cube.getId());
    /* 183 */String[][] response = request(request.toString());
    /* 184 */return response[0][0].equals("1");
    /*     */}

  /*     */
  /*     */public final int convert(CubeInfo cube, int type) throws IOException {
    /* 188 */DatabaseInfo database = cube.getDatabase();
    /* 189 */StringBuffer request = new StringBuffer();
    /* 190 */request.append("/cube/convert?database=");
    /* 191 */request.append(database.getId());
    /* 192 */request.append("&cube=");
    /* 193 */request.append(cube.getId());
    /* 194 */request.append("&type=");
    /* 195 */request.append(type);
    /* 196 */String[][] response = request(request.toString());
    /* 197 */if (response[0].length < 9) {
      /* 198 */throw new PaloException(
        "Not enough information to create cube info data.");
      /*     */}
    /* 200 */return Integer.parseInt(response[0][7]);
    /*     */}

  /*     */
  /*     */public final CubeInfo getInfo(DatabaseInfo database, String cubeId)
  /*     */throws IOException
  /*     */{
    /* 206 */StringBuffer query = new StringBuffer();
    /* 207 */query.append("/cube/info?database=");
    /* 208 */query.append(database.getId());
    /* 209 */query.append("&cube=");
    /* 210 */query.append(cubeId);
    /* 211 */String[][] response = request(query.toString());
    /* 212 */CubeInfoBuilder cubeBuilder = this.builderReg.getCubeBuilder();
    /* 213 */return cubeBuilder.create(database, response[0]);
    /*     */}

  /*     */
  /*     */public final RuleInfo[] getRules(CubeInfo cube) throws IOException {
    /* 217 */DatabaseInfo database = cube.getDatabase();
    /* 218 */StringBuffer query = new StringBuffer();
    /* 219 */query.append("/cube/rules?database=");
    /* 220 */query.append(database.getId());
    /* 221 */query.append("&cube=");
    /* 222 */query.append(cube.getId());
    /* 223 */String[][] response = request(query.toString());
    /* 224 */RuleInfo[] rules = new RuleInfo[response.length];
    /* 225 */RuleInfoBuilder ruleBuilder = this.builderReg.getRuleBuilder();
    /* 226 */for (int i = 0; i < response.length; ++i) {
      /* 227 */rules[i] = ruleBuilder.create(cube, response[i]);
      /*     */}
    /* 229 */return rules;
    /*     */}

  /*     */
  /*     */public final boolean load(CubeInfo cube)
  /*     */throws IOException
  /*     */{
    /* 259 */DatabaseInfo database = cube.getDatabase();
    /* 260 */StringBuffer query = new StringBuffer();
    /* 261 */query.append("/cube/load?database=");
    /* 262 */query.append(database.getId());
    /* 263 */query.append("&cube=");
    /* 264 */query.append(cube.getId());
    /* 265 */String[][] response = request(query.toString());
    /* 266 */return response[0][0].equals("1");
    /*     */}

  /*     */
  /*     */public final CubeInfo reload(CubeInfo cube) throws IOException {
    /* 270 */DatabaseInfo database = cube.getDatabase();
    /* 271 */StringBuffer query = new StringBuffer();
    /* 272 */query.append("/cube/info?database=");
    /* 273 */query.append(database.getId());
    /* 274 */query.append("&cube=");
    /* 275 */query.append(cube.getId());
    /* 276 */String[][] response = request(query.toString());
    /* 277 */CubeInfoBuilder cubeBuilder = this.builderReg.getCubeBuilder();
    /* 278 */cubeBuilder.update((CubeInfoImpl) cube, response[0]);
    /* 279 */return cube;
    /*     */}

  /*     */
  /*     */public final void rename(CubeInfo cube, String newName)
    throws IOException
  /*     */{
    /* 284 */DatabaseInfo database = cube.getDatabase();
    /* 285 */StringBuffer query = new StringBuffer();
    /* 286 */query.append("/cube/rename?database=");
    /* 287 */query.append(database.getId());
    /* 288 */query.append("&cube=");
    /* 289 */query.append(cube.getId());
    /* 290 */query.append("&new_name=");
    /* 291 */query.append(newName);
    /* 292 */String[][] response = request(query.toString());
    /* 293 */CubeInfoBuilder cubeBuilder = this.builderReg.getCubeBuilder();
    /* 294 */cubeBuilder.update((CubeInfoImpl) cube, response[0]);
    /*     */}

  /*     */
  /*     */public final boolean save(CubeInfo cube) throws IOException
  /*     */{
    /* 299 */DatabaseInfo database = cube.getDatabase();
    /* 300 */StringBuffer query = new StringBuffer();
    /* 301 */query.append("/cube/save?database=");
    /* 302 */query.append(database.getId());
    /* 303 */query.append("&cube=");
    /* 304 */query.append(cube.getId());
    /* 305 */String[][] response = request(query.toString());
    /* 306 */return response[0][0].equals("1");
    /*     */}

  /*     */
  /*     */public final boolean unload(CubeInfo cube) throws IOException
  /*     */{
    /* 311 */DatabaseInfo database = cube.getDatabase();
    /* 312 */StringBuffer query = new StringBuffer();
    /* 313 */query.append("/cube/unload?database=");
    /* 314 */query.append(database.getId());
    /* 315 */query.append("&cube=");
    /* 316 */query.append(cube.getId());
    /* 317 */String[][] response = request(query.toString());
    /* 318 */return response[0][0].equals("1");
    /*     */}

  /*     */
  /*     */public final CubeInfo getAttributeCube(DimensionInfo dimension)
    throws IOException {
    /* 322 */DatabaseInfo database = dimension.getDatabase();
    /* 323 */StringBuffer query = new StringBuffer();
    /* 324 */query.append("/cube/info?database=");
    /* 325 */query.append(database.getId());
    /* 326 */query.append("&cube=");
    /* 327 */query.append(dimension.getAttributeCube());
    /* 328 */String[][] response = request(query.toString());
    /* 329 */CubeInfoBuilder cubeBuilder = this.builderReg.getCubeBuilder();
    /* 330 */return cubeBuilder.create(database, response[0]);
    /*     */}

  /*     */
  /*     */public final LockInfo requestLock(CubeInfo cube,
    ElementInfo[][] coordinates)
  /*     */throws IOException
  /*     */{
    /* 336 */DatabaseInfo database = cube.getDatabase();
    /* 337 */StringBuffer query = new StringBuffer();
    /* 338 */query.append("/cube/lock?database=");
    /* 339 */query.append(database.getId());
    /* 340 */query.append("&cube=");
    /* 341 */query.append(cube.getId());
    /*     */
    /* 343 */query.append("&area=");
    /* 344 */query.append(getAreaString(coordinates));
    /* 345 */String[][] response = request(query.toString());
    /*     */
    /* 347 */LockInfoBuilder lockBuilder = this.builderReg.getLockBuilder();
    /* 348 */return lockBuilder.create(cube, response[0]);
    /*     */}

  /*     */
  /*     */public final LockInfo[] listLocks(CubeInfo cube) throws IOException
  /*     */{
    /* 353 */DatabaseInfo database = cube.getDatabase();
    /* 354 */StringBuffer query = new StringBuffer();
    /* 355 */query.append("/cube/locks?database=");
    /* 356 */query.append(database.getId());
    /* 357 */query.append("&cube=");
    /* 358 */query.append(cube.getId());
    /* 359 */String[][] response = request(query.toString());
    /*     */
    /* 361 */LockInfo[] locks = new LockInfo[response.length];
    /* 362 */LockInfoBuilder lockBuilder = this.builderReg.getLockBuilder();
    /* 363 */for (int i = 0; i < locks.length; ++i) {
      /* 364 */locks[i] = lockBuilder.create(cube, response[i]);
      /*     */}
    /* 366 */return locks;
    /*     */}

  /*     */
  /*     */public final boolean rollback(CubeInfo cube, LockInfo lock, int steps)
    throws IOException
  /*     */{
    /* 371 */DatabaseInfo database = cube.getDatabase();
    /* 372 */StringBuffer query = new StringBuffer();
    /* 373 */query.append("/cube/rollback?database=");
    /* 374 */query.append(database.getId());
    /* 375 */query.append("&cube=");
    /* 376 */query.append(cube.getId());
    /* 377 */query.append("&lock=");
    /* 378 */query.append(lock.getId());
    /* 379 */if (steps > -1) {
      /* 380 */query.append("&steps=");
      /* 381 */query.append(steps);
      /*     */}
    /* 383 */String[][] response = request(query.toString());
    /* 384 */return response[0][0].equals("1");
    /*     */}

  /*     */
  /*     */public final boolean commit(CubeInfo cube, LockInfo lock)
    throws IOException {
    /* 388 */DatabaseInfo database = cube.getDatabase();
    /* 389 */StringBuffer query = new StringBuffer();
    /* 390 */query.append("/cube/commit?database=");
    /* 391 */query.append(database.getId());
    /* 392 */query.append("&cube=");
    /* 393 */query.append(cube.getId());
    /* 394 */query.append("&lock=");
    /* 395 */query.append(lock.getId());
    /* 396 */String[][] response = request(query.toString());
    /* 397 */return response[0][0].equals("1");
    /*     */}

  /*     */
  /*     */private final String getAreaString(ElementInfo[][] area)
  /*     */{
    /* 421 */StringBuffer ids = new StringBuffer();
    /* 422 */int lastCoordinate = area.length - 1;
    /* 423 */for (int i = 0; i < area.length; ++i) {
      /* 424 */ids.append(getIdString(area[i], ":"));
      /* 425 */if (i < lastCoordinate)
        /* 426 */ids.append(",");
      /*     */}
    /* 428 */return ids.toString();
    /*     */}
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.paloapi\lib\palo.jar
 * Qualified Name: com.tensegrity.palojava.http.handlers.CubeHandler JD-Core
 * Version: 0.5.4
 */