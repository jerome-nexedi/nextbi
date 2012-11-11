/*     */package org.palo.viewapi.internal.io.xml;

/*     */
/*     */import java.util.HashMap;
import java.util.Iterator;

import org.palo.api.Connection;
import org.palo.api.Database;
import org.palo.api.Dimension;
import org.palo.api.Hierarchy;
import org.palo.api.PaloAPIException;
import org.palo.api.subsets.Subset2;
import org.palo.viewapi.Account;
import org.palo.viewapi.PaloAccount;
import org.palo.viewapi.PaloConnection;
import org.palo.viewapi.View;
import org.palo.viewapi.internal.DynamicFolder;
import org.palo.viewapi.services.AdministrationService;
import org.palo.viewapi.services.ServiceProvider;
import org.xml.sax.Attributes;

/*     */
/*     */public class DynamicFolderHandler
/*     */implements IXMLHandler
/*     */{
  /*     */public static final String XPATH = "/folder/dynamicFolder";

  /* 66 */public static final HashMap<Account, Connection> conMap = new HashMap();

  /*     */private FolderXMLHandler xmlHandler;

  /*     */
  /*     */public DynamicFolderHandler(FolderXMLHandler xmlHandler)
  /*     */{
    /* 71 */this.xmlHandler = xmlHandler;
    /*     */}

  /*     */
  /*     */public void enter(String path, Attributes attributes) {
    /* 75 */if ((!path.startsWith("/folder/")) || (!path.endsWith("dynamicFolder")))
      /*     */return;
    /* 77 */String id = attributes.getValue("id");
    /* 78 */if ((id == null) || (id.equals(""))) {
      /* 79 */throw new PaloAPIException(
      /* 80 */"DynamicFolderHandler: no id defined!");
      /*     */}
    /*     */
    /* 83 */String name = attributes.getValue("name");
    /* 84 */if (name == null) {
      /* 85 */throw new PaloAPIException(
      /* 86 */"DynamicFolderHandler: no name specified!");
      /*     */}
    /* 88 */String source = attributes.getValue("source");
    /*     */
    /* 90 */View sourceView = null;
    /* 91 */if (source != null) {
      /* 92 */sourceView = StaticFolderHandler.parseSourceView(this.xmlHandler,
      /* 93 */source);
      /*     */}
    /*     */
    /* 96 */String hierarchyId = attributes.getValue("hierarchyId");
    /* 97 */Hierarchy h = null;
    /* 98 */if (hierarchyId != null) {
      /* 99 */String connectionServer = attributes
      /* 100 */.getValue("connectionServer");
      /* 101 */if (connectionServer.startsWith("http://")) {
        /* 102 */connectionServer = connectionServer.substring(7);
        /*     */}
      /* 104 */String connectionService = attributes
      /* 105 */.getValue("connectionService");
      /* 106 */String databaseId = attributes.getValue("databaseId");
      /* 107 */String dimensionId = attributes.getValue("dimensionId");
      /* 108 */Dimension d = null;
      /*     */
      /* 118 */if (d == null)
      /*     */{
        /* 120 */AdministrationService adminService =
        /* 121 */ServiceProvider
          .getAdministrationService(this.xmlHandler.getUser());
        /* 122 */for (PaloConnection pc : adminService.getConnections()) {
          /* 123 */if ((!pc.getHost().equals(connectionServer)) ||
          /* 125 */(!pc.getService()
          /* 125 */.equals(connectionService)))
            /*     */continue;
          /* 127 */Iterator localIterator2 = this.xmlHandler.getUser()
          /* 127 */.getAccounts().iterator();
          /*     */
          /* 152 */while ((h == null) && (localIterator2.hasNext())) {
            /* 127 */Account acc = (Account) localIterator2.next();
            /* 128 */if (!acc.getConnection().equals(pc))
              /*     */continue;
            /* 130 */Connection con = (Connection) conMap.get(acc);
            /* 131 */if ((con == null) && (acc instanceof PaloAccount)) {
              /* 132 */con = ((PaloAccount) acc).login();
              /* 133 */conMap.put(acc, con);
              /*     */}
            /*     */
            /* 141 */Database db = con
            /* 142 */.getDatabaseById(databaseId);
            /* 143 */if (db == null) {
              /*     */continue;
              /*     */}
            /* 146 */Dimension dim = db
            /* 147 */.getDimensionById(dimensionId);
            /* 148 */if (dim == null) {
              /*     */continue;
              /*     */}
            /* 151 */h = dim.getHierarchyById(hierarchyId);
            /*     */}
          /*     */
          /* 158 */if (h != null)
          /*     */{
            /*     */break;
            /*     */}
          /*     */
          /*     */}
        /*     */
        /*     */}
      /*     */
      /*     */}
    /*     */
    /* 169 */String subset = attributes.getValue("subset");
    /* 170 */Subset2 s = null;
    /* 171 */if (subset != null) {
      /* 172 */String[] ids = subset.split("@_@");
      /* 173 */Hierarchy hier = null;
      /*     */
      /* 179 */int type = Integer.parseInt(ids[3]);
      /* 180 */if (hier == null) {
        /* 181 */hier = h;
        /*     */}
      /* 183 */if (hier != null) {
        /* 184 */s = hier.getSubsetHandler().getSubset(ids[2], type);
        /*     */}
      /*     */}
    /*     */
    /* 188 */DynamicFolder f = DynamicFolder.internalCreate(this.xmlHandler
    /* 189 */.getCurrentParent(), h, s, id, name);
    /* 190 */if (sourceView != null) {
      /* 191 */f.setSourceObject(sourceView);
      /*     */}
    /* 193 */this.xmlHandler.pushParent(f);
    /*     */}

  /*     */
  /*     */public String getXPath()
  /*     */{
    /* 198 */return "/folder/dynamicFolder";
    /*     */}

  /*     */
  /*     */public static void clear() {
    /* 202 */conMap.clear();
    /*     */}

  /*     */
  /*     */public void leave(String path, String value) {
    /* 206 */if ((!path.startsWith("/folder/")) ||
    /* 207 */(!path.endsWith("dynamicFolder")))
      return;
    /* 208 */this.xmlHandler.popParent();
    /*     */}
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi
 * .jar Qualified Name: org.palo.viewapi.internal.io.xml.DynamicFolderHandler
 * JD-Core Version: 0.5.4
 */