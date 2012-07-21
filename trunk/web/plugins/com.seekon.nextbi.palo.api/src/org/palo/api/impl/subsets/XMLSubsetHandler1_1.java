/*     */package org.palo.api.impl.subsets;

/*     */
/*     */import com.tensegrity.palojava.PaloException; /*     */
import java.io.PrintStream; /*     */
import org.palo.api.Connection; /*     */
import org.palo.api.Database; /*     */
import org.palo.api.Dimension; /*     */
import org.palo.api.Element; /*     */
import org.palo.api.Hierarchy; /*     */
import org.palo.api.PaloAPIException; /*     */
import org.palo.api.impl.xml.IPaloStartHandler; /*     */
import org.xml.sax.Attributes;

/*     */
/*     */class XMLSubsetHandler1_1 extends XMLSubsetHandler1_0
/*     */{
  /*     */XMLSubsetHandler1_1(Database database)
  /*     */{
    /* 61 */super(database);
    /*     */}

  /*     */
  /*     */IPaloStartHandler[] getStartHandlers(final Database database) {
    /* 65 */IPaloStartHandler[] oldStartHandlers = super.getStartHandlers(database);
    /* 66 */int oldLength = oldStartHandlers.length;
    /* 67 */IPaloStartHandler[] newStartHandlers = new IPaloStartHandler[oldLength + 2];
    /* 68 */System.arraycopy(oldStartHandlers, 0, newStartHandlers, 0, oldLength);
    /* 69 */newStartHandlers[oldLength] = new IPaloStartHandler() {
      /*     */public String getPath() {
        /* 71 */return "subset";
        /*     */}

      /*     */
      /*     */public void startElement(String uri, String localName, String qName,
        Attributes attributes)
      /*     */{
        /* 76 */XMLSubsetHandler1_1.this.subsetBuilder = new SubsetBuilder();
        /* 77 */XMLSubsetHandler1_1.this.subsetBuilder.setId(attributes
          .getValue("id"));
        /* 78 */XMLSubsetHandler1_1.this.subsetBuilder.setName(attributes
          .getValue("name"));
        /* 79 */XMLSubsetHandler1_1.this.subsetBuilder
        /* 80 */.setDescription(attributes.getValue("description"));
        /* 81 */XMLSubsetHandler1_1.this.subsetBuilder.setActiveState(
        /* 82 */attributes.getValue("activeStateId"));
        /* 83 */String srcDimensionId = attributes
        /* 84 */.getValue("sourceDimensionId");
        /* 85 */Dimension srcDim = database.getDimensionById(srcDimensionId);
        /* 86 */if (srcDim == null)
          /* 87 */throw new PaloAPIException("Cannot find source dimension '" +
          /* 88 */srcDimensionId + "'!!");
        /* 89 */Hierarchy srcHier = srcDim.getDefaultHierarchy();
        /*     */try
        /*     */{
          /* 93 */if (!srcDim.getDatabase().getConnection().isLegacy())
            /* 94 */XMLSubsetHandler1_1.this.subsetBuilder
              .setAlias(XMLSubsetHandler1_1.this
              /* 95 */.getAttributeByName(srcHier,
              /* 95 */attributes.getValue("alias")));
          /*     */} catch (PaloException pe) {
          /* 97 */System.err
          /* 98 */.println("SubsetReader: cannot read attributes - " +
          /* 99 */pe.getMessage());
          /*     */}
        /* 101 */XMLSubsetHandler1_1.this.subsetBuilder.setSourceHierarchy(srcHier);
        /*     */}
      /*     */
    };
    /* 104 */newStartHandlers[(oldLength + 1)] = new IPaloStartHandler()
    /*     */{
      /*     */public String getPath() {
        /* 107 */return "subset/state/element";
        /*     */}

      /*     */
      /*     */public void startElement(String uri, String localName, String qName,
        Attributes attributes)
      /*     */{
        /* 112 */if ((XMLSubsetHandler1_1.this.stateBuilder == null)
          || (XMLSubsetHandler1_1.this.subsetBuilder == null)) {
          /* 113 */throw new PaloAPIException(
          /* 114 */"Cannot create SubsetState in node element");
          /*     */}
        /* 116 */String elementId = attributes.getValue("id");
        /* 117 */String paths = attributes.getValue("paths");
        /* 118 */String positions = attributes.getValue("pos");
        /* 119 */Hierarchy srcHier = XMLSubsetHandler1_1.this.subsetBuilder
          .getSourceHierarchy();
        /* 120 */Element element = srcHier.getElementById(elementId);
        /* 121 */if (element != null) {
          /* 122 */XMLSubsetHandler1_1.this.stateBuilder.addElement(element);
          /* 123 */XMLSubsetHandler1_1.this.stateBuilder.setPaths(element, paths);
          /* 124 */XMLSubsetHandler1_1.this.stateBuilder.setPositions(element,
            positions);
          /*     */}
        /*     */}
      /*     */
    };
    /* 128 */return newStartHandlers;
    /*     */}
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name: org.palo.api.impl.subsets.XMLSubsetHandler1_1 JD-Core
 * Version: 0.5.4
 */