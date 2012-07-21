/*     */package org.palo.api.ext.favoriteviews.impl;

/*     */
/*     */import java.util.ArrayList; /*     */
import java.util.Stack; /*     */
import org.palo.api.Connection; /*     */
import org.palo.api.Cube; /*     */
import org.palo.api.CubeView; /*     */
import org.palo.api.Database; /*     */
import org.palo.api.ext.favoriteviews.FavoriteViewTreeNode; /*     */
import org.palo.api.ext.favoriteviews.FavoriteViewsFolder; /*     */
import org.palo.api.impl.xml.BaseXMLHandler; /*     */
import org.palo.api.impl.xml.EndHandler; /*     */
import org.palo.api.impl.xml.StartHandler; /*     */
import org.palo.api.persistence.PersistenceError; /*     */
import org.palo.api.persistence.PersistenceObserverAdapter; /*     */
import org.xml.sax.Attributes;

/*     */
/*     */public class FavoriteViewXMLHandler extends BaseXMLHandler
/*     */{
  /* 70 */private Stack folderStack = new Stack();

  /*     */
  /* 76 */private FavoriteViewTreeNode currentFolder = null;

  /*     */
  /* 81 */private FavoriteViewTreeNode root = null;

  /*     */
  /*     */public FavoriteViewTreeNode getRoot()
  /*     */{
    /* 88 */return this.root;
    /*     */}

  /*     */
  /*     */public CubeView findCubeViewById(Connection connection, String dbId,
    String cubeId, String viewId)
  /*     */{
    /* 107 */Database[] dbs = connection.getDatabases();
    /* 108 */for (int j = 0; j < dbs.length; ++j) {
      /* 109 */if (!dbs[j].getId().equals(dbId))
        /*     */continue;
      /* 111 */Cube c = dbs[j].getCubeById(cubeId);
      /* 112 */if (c != null) {
        /* 113 */final ArrayList<CubeView> views =
        /* 114 */new ArrayList();
        /* 115 */c.getCubeViews(new PersistenceObserverAdapter() {
          /*     */public void loadComplete(Object source) {
            /* 117 */if (source instanceof CubeView)
              /* 118 */views.add((CubeView) source);
            /*     */}

          /*     */
          /*     */public void loadFailed(String sourceId, PersistenceError[] errors)
          /*     */{
            /* 124 */for (PersistenceError error : errors) {
              /* 125 */Object src = error.getSource();
              /* 126 */if ((src != null) && (src instanceof CubeView))
                /* 127 */views.add((CubeView) src);
              /*     */}
            /*     */}

          /*     */
          /*     */public void loadIncomplete(Object source,
            PersistenceError[] errors)
          /*     */{
            /* 133 */if ((source != null) && (source instanceof CubeView))
              /* 134 */views.add((CubeView) source);
            /*     */}
          /*     */
        });
        /* 139 */CubeView view = null;
        /* 140 */for (CubeView v : views) {
          /* 141 */if (viewId.equals(v.getId())) {
            /* 142 */view = v;
            /* 143 */break;
            /*     */}
          /*     */}
        /* 146 */if (view != null) {
          /* 147 */return view;
          /*     */}
        /*     */}
      /*     */}
    /*     */
    /* 152 */return null;
    /*     */}

  /*     */
  /*     */public FavoriteViewXMLHandler(final Connection connection)
  /*     */{
    /* 162 */super(true);
    /*     */
    /* 166 */putStartHandler("folder", new StartHandler()
    /*     */{
      /*     */public void startElement(String uri, String localName, String qName,
        Attributes attributes) {
        /* 169 */String folderName = attributes.getValue("name");
        /* 170 */String position = attributes.getValue("position");
        /* 171 */if ((folderName != null) && (folderName.length() > 0)) {
          /* 172 */int pos = Integer.parseInt(position);
          /* 173 */FavoriteViewsFolder fold =
          /* 174 */new FavoriteViewsFolderImpl(folderName, connection, pos);
          /* 175 */FavoriteViewTreeNode folder = new FavoriteViewTreeNode(fold);
          /* 176 */if (FavoriteViewXMLHandler.this.currentFolder == null) {
            /* 177 */FavoriteViewXMLHandler.this.root = folder;
            /*     */}
          /* 179 */if (FavoriteViewXMLHandler.this.currentFolder != null) {
            /* 180 */FavoriteViewXMLHandler.this.currentFolder.addChild(folder);
            /* 181 */folder.setParent(FavoriteViewXMLHandler.this.currentFolder);
            /*     */}
          /* 183 */FavoriteViewXMLHandler.this.folderStack.push(folder);
          /* 184 */FavoriteViewXMLHandler.this.currentFolder = folder;
          /*     */}
        /*     */}
      /*     */
    });
    /* 191 */putStartHandler("bookmark", new StartHandler()
    /*     */{
      /*     */public void startElement(String uri, String localName, String qName,
        Attributes attributes) {
        /* 194 */String name = attributes.getValue("name");
        /* 195 */String position = attributes.getValue("position");
        /* 196 */String queryName = attributes.getValue("queryName");
        /* 197 */String viewId = null;
        /* 198 */if (queryName == null) {
          /* 199 */viewId = attributes.getValue("viewId");
          /*     */}
        /* 201 */String databaseId = attributes.getValue("databaseID");
        /* 202 */String cubeId = attributes.getValue("cubeID");
        /*     */
        /* 204 */CubeView view =
        /* 205 */FavoriteViewXMLHandler.this.findCubeViewById(connection,
          databaseId, cubeId, viewId);
        /* 206 */FavoriteViewImpl bm = new FavoriteViewImpl(name, view,
        /* 207 */Integer.parseInt(position));
        /* 208 */FavoriteViewTreeNode node = new FavoriteViewTreeNode(bm);
        /* 209 */FavoriteViewXMLHandler.this.currentFolder.addChild(node);
        /* 210 */node.setParent(FavoriteViewXMLHandler.this.currentFolder);
        /*     */}
      /*     */
    });
    /* 217 */putEndHandler("folder", new EndHandler() {
      /*     */public void endElement(String uri, String localName, String qName) {
        /* 219 */if (FavoriteViewXMLHandler.this.folderStack.isEmpty()) {
          /* 220 */FavoriteViewXMLHandler.this.currentFolder = null;
          /* 221 */return;
          /*     */}
        /* 223 */FavoriteViewXMLHandler.this.currentFolder = ((FavoriteViewTreeNode) FavoriteViewXMLHandler.this.folderStack
          .pop());
        /* 224 */if (FavoriteViewXMLHandler.this.folderStack.isEmpty()) {
          /* 225 */FavoriteViewXMLHandler.this.currentFolder = null;
          /*     */}
        /*     */else
        /*     */{
          /* 229 */FavoriteViewXMLHandler.this.currentFolder = ((FavoriteViewTreeNode) FavoriteViewXMLHandler.this.folderStack
            .lastElement());
          /*     */}
        /*     */}
      /*     */
    });
    /*     */}
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name: org.palo.api.ext.favoriteviews.impl.FavoriteViewXMLHandler
 * JD-Core Version: 0.5.4
 */