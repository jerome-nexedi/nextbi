/*     */package org.palo.api.impl.views;

/*     */
/*     */import org.palo.api.Axis; /*     */
import org.palo.api.CubeView; /*     */
import org.palo.api.Database; /*     */
import org.palo.api.Dimension; /*     */
import org.palo.api.Hierarchy; /*     */
import org.palo.api.PaloAPIException; /*     */
import org.palo.api.impl.xml.IPaloStartHandler; /*     */
import org.palo.api.utils.ElementPath; /*     */
import org.xml.sax.Attributes;

/*     */
/*     */class CubeViewHandler1_1 extends CubeViewHandler1_0
/*     */{
  /*     */CubeViewHandler1_1(Database database)
  /*     */{
    /* 61 */super(database);
    /*     */}

  /*     */
  /*     */protected void registerStartHandlers() {
    /* 65 */super.registerStartHandlers();
    /*     */
    /* 68 */unregisterStartHandler("view/axis/dimension");
    /*     */
    /* 70 */registerStartHandler(new IPaloStartHandler() {
      /*     */public String getPath() {
        /* 72 */return "view/axis/dimensions";
        /*     */}

      /*     */
      /*     */public void startElement(String uri, String localName, String qName,
        Attributes attributes)
      /*     */{
        /* 77 */String ids = attributes.getValue("ids");
        /* 78 */String[] dimIds = ids.split(",");
        /* 79 */String hIds = attributes.getValue("hierarchyIds");
        /* 80 */String[] hierIds = (String[]) null;
        /* 81 */if (hIds != null) {
          /* 82 */hierIds = hIds.split(",");
          /* 83 */if (hierIds.length != dimIds.length) {
            /* 84 */hierIds = (String[]) null;
            /*     */}
          /*     */}
        /* 87 */for (int i = 0; i < dimIds.length; ++i) {
          /* 88 */Dimension dimension = CubeViewHandler1_1.this.database
            .getDimensionById(dimIds[i]);
          /* 89 */if (dimension == null) {
            /* 90 */CubeViewHandler1_1.this
            /* 95 */.addError("CubeViewReader: unknown dimension id '" +
            /* 91 */dimIds[i] + "' in database '" +
            /* 92 */CubeViewHandler1_1.this.database.getName() + "'!!",
              CubeViewHandler1_1.this.cubeView.getId(),
              /* 93 */CubeViewHandler1_1.this.cubeView,
              CubeViewHandler1_1.this.database, dimIds[i],
              /* 94 */0,
              /* 95 */CubeViewHandler1_1.this.currAxis, 0);
            /*     */}
          /* 97 */if ((dimension != null) && (hierIds != null)) {
            /* 98 */Hierarchy hier = dimension.getHierarchyById(hierIds[i]);
            /* 99 */if (hier == null) {
              /* 100 */CubeViewHandler1_1.this
              /* 105 */.addError("CubeViewReader: unknown hierarchy id '" +
              /* 101 */hierIds[i] + "' in database '" +
              /* 102 */CubeViewHandler1_1.this.database.getName() + "'!!",
                CubeViewHandler1_1.this.cubeView.getId(),
                /* 103 */CubeViewHandler1_1.this.cubeView,
                CubeViewHandler1_1.this.database, hierIds[i],
                /* 104 */0,
                /* 105 */CubeViewHandler1_1.this.currAxis, 0);
              /*     */}
            /* 107 */CubeViewHandler1_1.this.currAxis.add(hier);
            /*     */} else {
            /* 109 */CubeViewHandler1_1.this.currAxis.add(dimension);
            /*     */}
          /*     */}
        /*     */}
      /*     */
    });
    /* 115 */registerStartHandler(new IPaloStartHandler() {
      /*     */public String getPath() {
        /* 117 */return "view/axis/hierarchies";
        /*     */}

      /*     */
      /*     */public void startElement(String uri, String localName, String qName,
        Attributes attributes)
      /*     */{
        /* 122 */String ids = attributes.getValue("ids");
        /* 123 */String hIds = attributes.getValue("hierarchyIds");
        /* 124 */if (ids.indexOf("~~~") != -1) {
          /* 125 */String[] dimHierIds = ids.split(",");
          /* 126 */for (int i = 0; i < dimHierIds.length; ++i) {
            /* 127 */String[] bothIds =
            /* 128 */dimHierIds[i].split("~~~");
            /* 129 */Dimension dimension = CubeViewHandler1_1.this.database
              .getDimensionById(bothIds[0]);
            /* 130 */if (dimension == null) {
              /* 131 */CubeViewHandler1_1.this
              /* 136 */.addError("CubeViewReader: unknown dimension id '" +
              /* 132 */bothIds[0] + "' in database '" +
              /* 133 */CubeViewHandler1_1.this.database.getName() + "'!!",
                CubeViewHandler1_1.this.cubeView.getId(),
                /* 134 */CubeViewHandler1_1.this.cubeView,
                CubeViewHandler1_1.this.database, bothIds[0],
                /* 135 */0,
                /* 136 */CubeViewHandler1_1.this.currAxis, 0);
              /*     */} else {
              /* 138 */Hierarchy hier = dimension.getHierarchyById(bothIds[1]);
              /* 139 */if (hier == null) {
                /* 140 */CubeViewHandler1_1.this
                /* 145 */.addError("CubeViewReader: unknown hierarchy id '" +
                /* 141 */bothIds[1] + "' in database '" +
                /* 142 */CubeViewHandler1_1.this.database.getName() + "'!!",
                  CubeViewHandler1_1.this.cubeView.getId(),
                  /* 143 */CubeViewHandler1_1.this.cubeView,
                  CubeViewHandler1_1.this.database, bothIds[1],
                  /* 144 */0,
                  /* 145 */CubeViewHandler1_1.this.currAxis, 0);
                /*     */}
              /* 147 */CubeViewHandler1_1.this.currAxis.add(hier);
              /*     */}
            /*     */}
          /*     */} else {
          /* 151 */String[] dimIds = ids.split(",");
          /* 152 */String[] hierIds = (String[]) null;
          /* 153 */if (hIds != null) {
            /* 154 */hierIds = hIds.split(",");
            /* 155 */if (hierIds.length != dimIds.length) {
              /* 156 */hierIds = (String[]) null;
              /*     */}
            /*     */}
          /* 159 */for (int i = 0; i < dimIds.length; ++i) {
            /* 160 */Dimension dimension = CubeViewHandler1_1.this.database
              .getDimensionById(dimIds[i]);
            /* 161 */if (dimension == null) {
              /* 162 */CubeViewHandler1_1.this
              /* 167 */.addError("CubeViewReader: unknown dimension id '" +
              /* 163 */dimIds[i] + "' in database '" +
              /* 164 */CubeViewHandler1_1.this.database.getName() + "'!!",
                CubeViewHandler1_1.this.cubeView.getId(),
                /* 165 */CubeViewHandler1_1.this.cubeView,
                CubeViewHandler1_1.this.database, dimIds[i],
                /* 166 */0,
                /* 167 */CubeViewHandler1_1.this.currAxis, 0);
              /*     */}
            /* 169 */else if (hierIds != null) {
              /* 170 */Hierarchy hier = dimension.getHierarchyById(hierIds[i]);
              /* 171 */if (hier == null) {
                /* 172 */CubeViewHandler1_1.this
                /* 177 */.addError("CubeViewReader: unknown hierarchy id '" +
                /* 173 */hierIds[i] + "' in database '" +
                /* 174 */CubeViewHandler1_1.this.database.getName() + "'!!",
                  CubeViewHandler1_1.this.cubeView.getId(),
                  /* 175 */CubeViewHandler1_1.this.cubeView,
                  CubeViewHandler1_1.this.database, hierIds[i],
                  /* 176 */0,
                  /* 177 */CubeViewHandler1_1.this.currAxis, 0);
                /*     */}
              /* 179 */CubeViewHandler1_1.this.currAxis.add(hier);
              /*     */}
            /*     */}
          /*     */}
        /*     */}
      /*     */
    });
    /* 188 */registerStartHandler(new IPaloStartHandler() {
      /*     */public String getPath() {
        /* 190 */return "view/axis/expanded";
        /*     */}

      /*     */
      /*     */public void startElement(String uri, String localName, String qName,
        Attributes attributes)
      /*     */{
        /* 195 */String paths = attributes.getValue("paths");
        /* 196 */Hierarchy[] hierarchies = CubeViewHandler1_1.this.currAxis
          .getHierarchies();
        /*     */try {
          /* 198 */ElementPath expandedPath = ElementPath.restore(hierarchies,
          /* 199 */paths);
          /* 200 */CubeViewHandler1_1.this.currAxis.addExpanded(expandedPath);
          /*     */} catch (PaloAPIException ex) {
          /* 202 */CubeViewHandler1_1.this
          /* 206 */.addError(ex.getMessage(), CubeViewHandler1_1.this.cubeView
            .getId(), CubeViewHandler1_1.this.cubeView,
          /* 203 */((Object[]) ex.getData())[0], ((Object[])
          /* 204 */ex.getData())[
          /* 204 */1].toString(),
          /* 205 */1, CubeViewHandler1_1.this.currAxis,
          /* 206 */2);
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
 * Qualified Name: org.palo.api.impl.views.CubeViewHandler1_1 JD-Core Version:
 * 0.5.4
 */