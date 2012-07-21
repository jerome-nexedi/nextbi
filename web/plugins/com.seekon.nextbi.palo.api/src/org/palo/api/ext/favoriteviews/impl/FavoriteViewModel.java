/*     */package org.palo.api.ext.favoriteviews.impl;

/*     */
/*     */import com.tensegrity.palo.xmla.ext.views.SQLConnection; /*     */
import java.io.ByteArrayInputStream; /*     */
import java.io.PrintStream; /*     */
import javax.xml.parsers.SAXParser; /*     */
import javax.xml.parsers.SAXParserFactory; /*     */
import org.palo.api.Connection; /*     */
import org.palo.api.Cube; /*     */
import org.palo.api.Database; /*     */
import org.palo.api.Dimension; /*     */
import org.palo.api.Hierarchy; /*     */
import org.palo.api.PaloAPIException; /*     */
import org.palo.api.ext.favoriteviews.FavoriteViewFactory; /*     */
import org.palo.api.ext.favoriteviews.FavoriteViewTreeNode;

/*     */
/*     */public class FavoriteViewModel
/*     */{
  /*     */protected final FavoriteViewTreeNode fromXML(String input, Connection con)
  /*     */{
    /* 75 */FavoriteViewXMLHandler defaultHandler = new FavoriteViewXMLHandler(con);
    /*     */
    /* 77 */SAXParserFactory sF = SAXParserFactory.newInstance();
    /* 78 */SAXParser parser = null;
    /*     */try {
      /* 80 */ByteArrayInputStream bin =
      /* 81 */new ByteArrayInputStream(input.getBytes("UTF-8"));
      /* 82 */parser = sF.newSAXParser();
      /* 83 */parser.parse(bin, defaultHandler);
      /* 84 */return defaultHandler.getRoot();
      /*     */} catch (Exception e) {
      /* 86 */e.printStackTrace();
      /* 87 */}
    return null;
    /*     */}

  /*     */
  /*     */public Cube createBookmarkCubeInNewDatabase(Connection con)
  /*     */{
    /* 104 */Cube cube = null;
    /*     */try
    /*     */{
      /* 107 */Database paloDb = con.addDatabase("AdvancedSystem");
      /* 108 */Dimension userDim = paloDb.addDimension("#user");
      /* 109 */Hierarchy userHier = userDim.getDefaultHierarchy();
      /* 110 */userHier.addElement(con.getUsername(), 1);
      /* 111 */Dimension bookmarkDim = paloDb.addDimension("#bookmarkedViews");
      /* 112 */Hierarchy bookmarkHier = bookmarkDim.getDefaultHierarchy();
      /* 113 */bookmarkHier.addElement("Bookmarks", 1);
      /* 114 */cube = paloDb.addCube("#userBookmarks",
      /* 115 */new Dimension[] { userDim, bookmarkDim });
      /*     */} catch (Exception e) {
      /* 117 */e.printStackTrace();
      /*     */}
    /*     */
    /* 120 */return cube;
    /*     */}

  /*     */
  /*     */public Cube createBookmarkCubeInExistingDatabase(Database db)
  /*     */{
    /* 138 */Dimension user = db.getDimensionByName("#user");
    /* 139 */if (user == null)
    /*     */{
      /* 141 */user = db.addDimension("#user");
      /* 142 */user.getDefaultHierarchy().addElement(
        db.getConnection().getUsername(),
        /* 143 */1);
      /*     */}
    /* 147 */else if (user.getDefaultHierarchy().getElementByName(
      db.getConnection().getUsername()) == null) {
      /* 148 */user.getDefaultHierarchy().addElement(
        db.getConnection().getUsername(),
        /* 149 */1);
      /*     */}
    /*     */
    /* 154 */Dimension bookmarks = db.getDimensionByName("#bookmarkedViews");
    /* 155 */if (bookmarks == null)
    /*     */{
      /* 157 */bookmarks = db.addDimension("#bookmarkedViews");
      /* 158 */bookmarks.getDefaultHierarchy().addElement("Bookmarks", 1);
      /*     */}
    /* 161 */else if (bookmarks.getDefaultHierarchy().getElementByName("Bookmarks") == null) {
      /* 162 */bookmarks.getDefaultHierarchy().addElement("Bookmarks",
      /* 163 */1);
      /*     */}
    /*     */
    /* 168 */Cube cube = db.addCube("#userBookmarks", new Dimension[] { user,
    /* 169 */bookmarks });
    /* 170 */return cube;
    /*     */}

  /*     */
  /*     */private final Cube findBookmarksCube(Connection con, boolean create)
  /*     */{
    /* 182 */Database db = null;
    /*     */
    /* 185 */db = con.getDatabaseByName(
    /* 186 */"AdvancedSystem");
    /* 187 */Cube cube = null;
    /* 188 */if ((db == null) && (create))
    /*     */{
      /* 191 */cube = createBookmarkCubeInNewDatabase(con);
      /* 192 */} else if (db != null)
    /*     */{
      /* 194 */cube = db.getCubeByName("#userBookmarks");
      /* 195 */if ((cube == null) && (create))
      /*     */{
        /* 197 */cube = createBookmarkCubeInExistingDatabase(db);
        /*     */}
      /*     */}
    /*     */
    /* 201 */return cube;
    /*     */}

  /*     */
  /*     */public FavoriteViewTreeNode loadFavoriteViews(Connection con)
  /*     */{
    /* 218 */if (con.getType() == 3) {
      /* 219 */SQLConnection sql = new SQLConnection();
      /* 220 */String xmlData = "";
      /*     */try {
        /* 222 */xmlData = sql.loadFavoriteView(con.getServer(), con.getService(),
          con.getUsername());
        /*     */} finally {
        /* 224 */sql.close();
        /*     */}
      /* 226 */if (xmlData.trim().length() > 0) {
        /* 227 */return fromXML(xmlData, con);
        /*     */}
      /* 229 */return null;
      /*     */}
    /*     */
    /* 232 */if (con.getType() == 4) {
      /* 233 */return null;
      /*     */}
    /*     */
    /* 236 */Cube cube = findBookmarksCube(con, false);
    /* 237 */if (cube == null) {
      /* 238 */return new FavoriteViewTreeNode(
      /* 239 */FavoriteViewFactory.getInstance().createFolder("Root", con));
      /*     */}
    /*     */
    /* 243 */Dimension userDim = cube.getDimensionByName("#user");
    /* 244 */if (userDim == null)
    /*     */{
      /* 247 */return new FavoriteViewTreeNode(
      /* 248 */FavoriteViewFactory.getInstance().createFolder("Root", con));
      /*     */}
    /* 250 */if (userDim.getDefaultHierarchy().getElementByName(con.getUsername()) == null) {
      /*     */try {
        /* 252 */userDim.getDefaultHierarchy().addElement(con.getUsername(), 1);
        /*     */} catch (Exception e) {
        /* 254 */throw new PaloAPIException(e.getLocalizedMessage());
        /*     */}
      /*     */}
    /*     */try
    /*     */{
      /* 259 */String xmlData = (String) cube.getData(new String[] {
      /* 260 */con.getUsername(), "Bookmarks" });
      /* 261 */if ((xmlData == null) ||
      /* 262 */(xmlData.length() <= 0))
        return null;
      /* 263 */return fromXML(xmlData, con);
      /*     */}
    /*     */catch (PaloAPIException e)
    /*     */{
      /* 268 */System.err.println("Failed to load favorite view!\nReason: " +
      /* 269 */e.getMessage());
      /*     */}
    /* 271 */return null;
    /*     */}

  /*     */
  /*     */public synchronized void storeFavoriteViews(Connection con,
    FavoriteViewTreeNode root)
  /*     */{
    /* 281 */if (con == null) {
      /* 282 */throw new NullPointerException(
      /* 283 */"Connection to store the bookmark must not be null.");
      /*     */}
    /* 285 */if (!con.isConnected())
    /*     */{
      /* 288 */return;
      /*     */}
    /*     */
    /* 291 */FavoriteViewXMLBuilder builder = new FavoriteViewXMLBuilder(con);
    /* 292 */builder.preOrderTraversal(root);
    /*     */
    /* 294 */String xmlBookmarkText = builder.getResult();
    /* 295 */if (con.getType() == 3) {
      /* 296 */SQLConnection sql = new SQLConnection();
      /*     */try {
        /* 298 */sql.writeFavoriteViews(con.getServer(), con.getService(), con
          .getUsername(), xmlBookmarkText);
        /*     */} finally {
        /* 300 */sql.close();
        /*     */}
      /* 302 */return;
      /*     */}
    /*     */
    /* 305 */if (con.getType() == 4) {
      /* 306 */return;
      /*     */}
    /*     */
    /* 309 */Cube cube = findBookmarksCube(con, true);
    /* 310 */if (cube == null)
    /*     */{
      /* 313 */throw new PaloAPIException(
      /* 314 */"Insufficient rights to store favorite views.");
      /*     */}
    /*     */
    /* 317 */cube.setData(new String[] { con.getUsername(), "Bookmarks" },
    /* 318 */xmlBookmarkText);
    /*     */}
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name: org.palo.api.ext.favoriteviews.impl.FavoriteViewModel JD-Core
 * Version: 0.5.4
 */