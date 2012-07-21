/*     */package org.palo.viewapi.internal.io.xml;

/*     */
/*     */import org.palo.api.Connection; /*     */
import org.palo.api.Database; /*     */
import org.palo.api.Dimension; /*     */
import org.palo.api.Hierarchy; /*     */
import org.palo.api.PaloAPIException; /*     */
import org.palo.api.subsets.Subset2; /*     */
import org.palo.api.subsets.SubsetHandler; /*     */
import org.palo.viewapi.Account; /*     */
import org.palo.viewapi.AuthUser; /*     */
import org.palo.viewapi.Group; /*     */
import org.palo.viewapi.PaloAccount; /*     */
import org.palo.viewapi.PaloConnection; /*     */
import org.palo.viewapi.Role; /*     */
import org.palo.viewapi.View; /*     */
import org.palo.viewapi.internal.FolderElement; /*     */
import org.palo.viewapi.internal.IRoleManagement; /*     */
import org.palo.viewapi.internal.dbmappers.MapperRegistry; /*     */
import org.palo.viewapi.services.ServiceProvider; /*     */
import org.xml.sax.Attributes;

/*     */
/*     */public class FolderElementHandler
/*     */implements IXMLHandler
/*     */{
  /*     */public static final String XPATH = "/folder/folderElement";

  /*     */private FolderXMLHandler xmlHandler;

  /*     */
  /*     */public FolderElementHandler(FolderXMLHandler xmlHandler)
  /*     */{
    /* 70 */this.xmlHandler = xmlHandler;
    /*     */}

  /*     */
  /*     */private final Hierarchy decodeHierarchy(String[] parts)
  /*     */{
    /* 110 */if (parts.length != 6) {
      /* 111 */return null;
      /*     */}
    /* 113 */AuthUser user = this.xmlHandler.getUser();
    /* 114 */for (Account a : user.getAccounts()) {
      /* 115 */if ((!a.getConnection().getHost().equals(parts[1])) ||
      /* 116 */(!a.getConnection().getService().equals(parts[2])) ||
      /* 117 */(!(a instanceof PaloAccount)))
        continue;
      /* 118 */Connection con = ((PaloAccount) a).login();
      /* 119 */Database db = con.getDatabaseById(parts[3]);
      /* 120 */if (db != null) {
        /* 121 */Dimension dim = db.getDimensionById(parts[4]);
        /* 122 */if (dim != null) {
          /* 123 */Hierarchy hier = dim.getHierarchyById(parts[5]);
          /* 124 */if (hier != null) {
            /* 125 */return hier;
            /*     */}
          /*     */}
        /*     */}
      /*     */
      /*     */}
    /*     */
    /* 132 */return null;
    /*     */}

  /*     */
  /*     */private final Subset2 decodeSubset(String[] parts) {
    /* 136 */if (parts.length != 7) {
      /* 137 */return null;
      /*     */}
    /* 139 */AuthUser user = this.xmlHandler.getUser();
    /* 140 */for (Account a : user.getAccounts()) {
      /* 141 */if ((!a.getConnection().getHost().equals(parts[1])) ||
      /* 142 */(!a.getConnection().getService().equals(parts[2])) ||
      /* 143 */(!(a instanceof PaloAccount)))
        continue;
      /* 144 */Connection con = ((PaloAccount) a).login();
      /* 145 */Database db = con.getDatabaseById(parts[3]);
      /* 146 */if (db != null) {
        /* 147 */Dimension dim = db.getDimensionById(parts[4]);
        /* 148 */if (dim != null) {
          /* 149 */Hierarchy hier = dim.getHierarchyById(parts[5]);
          /* 150 */if (hier != null) {
            /* 151 */Subset2 ss =
            /* 152 */hier.getSubsetHandler().getSubset(parts[6], 1);
            /* 153 */if (ss == null) {
              /* 154 */ss = hier.getSubsetHandler().getSubset(parts[6], 0);
              /*     */}
            /* 156 */if (ss != null) {
              /* 157 */return ss;
              /*     */}
            /*     */}
          /*     */}
        /*     */}
      /*     */
      /*     */}
    /*     */
    /* 165 */return null;
    /*     */}

  /*     */
  /*     */private final Object decodeKey(String key) {
    /* 169 */String[] parts = key.split(":");
    /* 170 */if ((parts == null) || (parts.length == 0)) {
      /* 171 */return null;
      /*     */}
    /* 173 */if (parts[0].equals("hierarchy"))
      /* 174 */return decodeHierarchy(parts);
    /* 175 */if (parts[0].equals("subset")) {
      /* 176 */return decodeSubset(parts);
      /*     */}
    /* 178 */return null;
    /*     */}

  /*     */
  /*     */private final boolean hasRole(AuthUser user, Role r) {
    /* 182 */for (Role rr : user.getRoles()) {
      /* 183 */if (rr.getId().equals(r.getId())) {
        /* 184 */return true;
        /*     */}
      /*     */}
    /* 187 */for (Group g : user.getGroups()) {
      /* 188 */for (Role rr : g.getRoles()) {
        /* 189 */if (rr.getId().equals(r.getId())) {
          /* 190 */return true;
          /*     */}
        /*     */}
      /*     */}
    /* 194 */return false;
    /*     */}

  /*     */
  /*     */public void enter(String path, Attributes attributes) {
    /* 198 */if ((!path.startsWith("/folder/"))
      || (!path.endsWith("folderElement")))
      /*     */return;
    /* 200 */String id = attributes.getValue("id");
    /* 201 */if ((id == null) || (id.equals(""))) {
      /* 202 */throw new PaloAPIException("FolderElementHandler: no id defined!");
      /*     */}
    /*     */
    /* 205 */String name = attributes.getValue("name");
    /* 206 */if (name == null) {
      /* 207 */throw new PaloAPIException(
        "FolderElementHandler: no name specified!");
      /*     */}
    /*     */
    /* 211 */String source = attributes.getValue("source");
    /* 212 */View sourceView = null;
    /* 213 */boolean createElement = true;
    /* 214 */if (source != null) {
      /* 215 */createElement = false;
      /* 216 */sourceView = StaticFolderHandler.parseSourceView(this.xmlHandler,
        source);
      /* 217 */AuthUser user = this.xmlHandler.getUser();
      /*     */
      /* 219 */if (sourceView != null) {
        /* 220 */if ((sourceView.isOwner(user)) || (ServiceProvider.isAdmin(user)))
          /* 221 */createElement = true;
        /*     */else
          /*     */try {
            /* 224 */IRoleManagement roleMgmt = MapperRegistry.getInstance()
              .getRoleManagement();
            /* 225 */Role viewerRole = (Role) roleMgmt.findByName("VIEWER");
            /* 226 */Role editorRole = (Role) roleMgmt.findByName("EDITOR");
            /* 227 */if ((viewerRole != null) && (editorRole != null)) {
              /* 228 */boolean hasView = sourceView.hasRole(viewerRole);
              /* 229 */boolean hasEdi = sourceView.hasRole(editorRole);
              /* 230 */if ((!hasView) && (!hasEdi)) {
                /* 231 */createElement = false;
                /*     */} else {
                /* 233 */if ((hasView) && (hasRole(user, viewerRole))) {
                  /* 234 */createElement = true;
                  /*     */}
                /* 236 */if ((hasEdi) && (hasRole(user, editorRole)))
                  /* 237 */createElement = true;
                /*     */}
              /*     */}
            /*     */else {
              /* 241 */createElement = true;
              /*     */}
            /*     */} catch (Exception e) {
            /* 244 */createElement = true;
            /*     */}
        /*     */}
      /*     */else {
        /* 248 */createElement = false;
        /*     */}
      /*     */}
    /*     */
    /* 252 */String book = attributes.getValue("book");
    /*     */
    /* 259 */if (createElement) {
      /* 260 */FolderElement e = FolderElement.internalCreate(
      /* 261 */this.xmlHandler.getCurrentParent(), id, name);
      /* 262 */if (sourceView != null) {
        /* 263 */e.setSourceObject(sourceView);
        /*     */}
      /* 266 */else if (book != null) {
        /* 267 */e.setSourceObjectDescription("book" + book);
        /*     */}
      /* 269 */String mapping = attributes.getValue("mappings");
      /* 270 */if (mapping != null) {
        /* 271 */String[] keyValue = mapping.split(",");
        /* 272 */for (int i = 0; i < keyValue.length; i += 2) {
          /* 273 */String key = keyValue[i].trim();
          /* 274 */if (key.length() != 0) {
            /* 275 */Object o = decodeKey(key);
            /* 276 */if (o != null) {
              /* 277 */String value = keyValue[(i + 1)].trim();
              /* 278 */if (value.length() != 0)
                /* 279 */if (o instanceof Hierarchy)
                  /* 280 */e.setVariableMapping((Hierarchy) o, value);
                /* 281 */else if (o instanceof Subset2)
                  /* 282 */e.setVariableMapping((Subset2) o, value);
              /*     */}
            /*     */}
          /*     */}
        /*     */}
      /*     */}
    /*     */}

  /*     */
  /*     */public String getXPath()
  /*     */{
    /* 294 */return "/folder/folderElement";
    /*     */}

  /*     */
  /*     */public void leave(String path, String value)
  /*     */{
    /*     */}
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi
 * .jar Qualified Name: org.palo.viewapi.internal.io.xml.FolderElementHandler
 * JD-Core Version: 0.5.4
 */