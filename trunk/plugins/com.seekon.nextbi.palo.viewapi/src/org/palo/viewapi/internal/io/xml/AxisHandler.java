/*     */package org.palo.viewapi.internal.io.xml;

/*     */
/*     */import java.io.PrintStream; /*     */
import org.palo.api.Attribute; /*     */
import org.palo.api.Cube; /*     */
import org.palo.api.Dimension; /*     */
import org.palo.api.Element; /*     */
import org.palo.api.ElementNode; /*     */
import org.palo.api.Hierarchy; /*     */
import org.palo.api.PaloAPIException; /*     */
import org.palo.api.PaloObject; /*     */
import org.palo.api.subsets.Subset2; /*     */
import org.palo.api.subsets.SubsetHandler; /*     */
import org.palo.api.utils.ElementPath; /*     */
import org.palo.viewapi.Axis; /*     */
import org.palo.viewapi.AxisHierarchy; /*     */
import org.palo.viewapi.CubeView; /*     */
import org.palo.viewapi.LocalFilter; /*     */
import org.palo.viewapi.Property; /*     */
import org.palo.viewapi.VirtualElement; /*     */
import org.palo.viewapi.internal.LocalFilterImpl; /*     */
import org.palo.viewapi.internal.VirtualElementImpl; /*     */
import org.palo.viewapi.internal.util.XMLUtil; /*     */
import org.xml.sax.Attributes;

/*     */
/*     */public class AxisHandler
/*     */implements IXMLHandler
/*     */{
  /*     */public static final String XPATH = "/view/axis";

  /*     */private static final String AXIS_HIERARCHY = "/view/axis/axis_hierarchy";

  /*     */private static final String AXIS_EXPANDED_PATHS = "/view/axis/expanded_paths";

  /*     */private static final String AXIS_PROPERTY = "/view/axis/property";

  /*     */private static final String AXIS_HIERARCHY_SUBSET = "/view/axis/axis_hierarchy/subset";

  /*     */private static final String AXIS_HIERARCHY_LOCALFILTER = "/view/axis/axis_hierarchy/localfilter";

  /*     */private static final String AXIS_HIERARCHY_LOCALFILTER_ELEMENT = "/view/axis/axis_hierarchy/localfilter/element";

  /*     */private static final String AXIS_HIERARCHY_SELECTED_ELEMENTS = "/view/axis/axis_hierarchy/selected_elements";

  /*     */private static final String AXIS_HIERARCHY_PROPERTY = "/view/axis/axis_hierarchy/property";

  /*     */private Axis axis;

  /*     */private AxisHierarchy axisHierarchy;

  /*     */private ElementNode currentElementNode;

  /*     */private final CubeView view;

  /*     */
  /*     */public AxisHandler(CubeView view)
  /*     */{
    /* 88 */this.view = view;
    /*     */}

  /*     */
  /*     */public final void enter(String path, Attributes attributes)
  /*     */{
    /* 93 */if ((this.axis == null) && (path.equals("/view/axis")))
    /*     */{
      /* 95 */String id = attributes.getValue("id");
      /* 96 */if ((id == null) || (id.equals(""))) {
        /* 97 */throw new PaloAPIException("AxisHandler: no axis id defined!");
        /*     */}
      /* 99 */String name = attributes.getValue("name");
      /*     */
      /* 101 */this.axis = this.view.addAxis(id, name);
      /* 102 */} else if ((path.equals("/view/axis/property"))
      && (this.axis != null)) {
      /* 103 */String propId = attributes.getValue("id");
      /* 104 */String propValue = attributes.getValue("value");
      /* 105 */if ((propId != null) && (propValue != null)) {
        /* 106 */Property property = new Property(propId, propValue);
        /* 107 */this.axis.addProperty(property);
        /*     */}
      /* 109 */} else if ((path.equals("/view/axis/axis_hierarchy"))
      && (this.axisHierarchy == null))
    /*     */{
      /* 111 */String dimId = attributes.getValue("dimension_id");
      /* 112 */String hierId = attributes.getValue("hierarchy_id");
      /* 113 */if ((dimId == null) || (dimId.equals("")) || (hierId == null) ||
      /* 114 */(hierId.equals("")))
        /* 115 */throw new PaloAPIException(
        /* 116 */"AxisHandler: no valid axis hierarchy defined for axis '" +
        /* 117 */this.axis.getName() + "[" + this.axis.getId() + "]'!");
      /* 118 */Dimension dimension = this.view.getCube().getDimensionById(dimId);
      /* 119 */if (dimension == null)
        /* 120 */throw new PaloAPIException("AxisHandler: unkown dimension id '" +
        /* 121 */dimId + "' in view '" + this.view.getName() +
        /* 122 */"' of cube '" + this.view.getCube().getName() + "'!");
      /* 123 */Hierarchy hierarchy = dimension.getHierarchyById(hierId);
      /* 124 */if (hierarchy == null) {
        /* 125 */throw new PaloAPIException("AxisHandler: unkown hierarchy id '" +
        /* 126 */hierId + "' in view '" + this.view.getName() +
        /* 127 */"' of cube '" + this.view.getCube().getName() +
        /* 128 */"' and dimension '" + dimension.getName() + "'!");
        /*     */}
      /* 130 */this.axisHierarchy = this.axis.add(hierarchy);
      /* 131 */} else if (path.equals("/view/axis/axis_hierarchy/property")) {
      /* 132 */check(this.axisHierarchy);
      /* 133 */String propId = attributes.getValue("id");
      /*     */
      /* 135 */if (propId.equals("com.tensegrity.palo.axis.use_alias")) {
        /* 136 */String aliasId = attributes.getValue("value");
        /* 137 */Attribute alias = this.axisHierarchy.getHierarchy().getAttribute(
        /* 138 */aliasId);
        /* 139 */if (alias != null) {
          /* 140 */Property aliasProperty = new Property(
          /* 141 */"com.tensegrity.palo.axis.use_alias", alias);
          /* 142 */this.axisHierarchy.addProperty(aliasProperty);
          /*     */} else {
          /* 144 */this.axisHierarchy.setAliasMissing(aliasId);
          /*     */}
        /*     */}
      /*     */else {
        /* 148 */Property prop = new Property(propId, attributes.getValue("value"));
        /* 149 */this.axisHierarchy.addProperty(prop);
        /*     */}
      /* 151 */} else if (path.equals("/view/axis/axis_hierarchy/localfilter")) {
      /* 152 */check(this.axisHierarchy);
      /* 153 */LocalFilter localFilter = new LocalFilterImpl();
      /* 154 */this.axisHierarchy.setLocalFilter(localFilter);
      /* 155 */} else if (path
      .startsWith("/view/axis/axis_hierarchy/localfilter/element")) {
      /* 156 */check(this.axisHierarchy);
      /* 157 */String elId = attributes.getValue("id");
      /* 158 */String elName = attributes.getValue("name");
      /* 159 */Element element = (elId != null) ?
      /* 160 */this.axisHierarchy.getHierarchy().getElementById(elId) :
      /* 161 */new VirtualElementImpl(elName, this.axisHierarchy.getHierarchy());
      /*     */try {
        /* 163 */ElementNode elNode = new ElementNode(element);
        /* 164 */if (this.currentElementNode != null)
          /* 165 */this.currentElementNode.forceAddChild(elNode);
        /* 166 */this.currentElementNode = elNode;
        /*     */}
      /*     */catch (NullPointerException localNullPointerException) {
        /*     */}
      /*     */}
    /*     */}

  /*     */
  /*     */public final String getXPath() {
    /* 174 */return "/view/axis";
    /*     */}

  /*     */
  /*     */public final void leave(String path, String value) {
    /* 178 */if (this.axis == null)
      /* 179 */throw new PaloAPIException("AxisHandler: no axis created!");
    /* 180 */value = XMLUtil.dequote(value);
    /* 181 */if (path.equals("/view/axis/axis_hierarchy/subset")) {
      /* 182 */check(this.axisHierarchy);
      /* 183 */Subset2 subset =
      /* 184 */this.axisHierarchy.getHierarchy().getDimension().getSubsetHandler()
        .getSubset(value, 1);
      /* 185 */if (subset == null) {
        /* 186 */this.axisHierarchy.setSubsetMissing(value);
        /*     */}
      /*     */
      /* 191 */this.axisHierarchy.setSubset(subset);
      /* 192 */} else if (path
      .equals("/view/axis/axis_hierarchy/selected_elements")) {
      /* 193 */check(this.axisHierarchy);
      /* 194 */Hierarchy hierarchy = this.axisHierarchy.getHierarchy();
      /*     */
      /* 196 */String[] elIDs = value.split(",");
      /* 197 */for (int i = 0; i < elIDs.length; ++i) {
        /* 198 */Element el = hierarchy.getElementById(elIDs[i]);
        /* 199 */if (el == null)
          /* 200 */System.err.println("AxisHandler: unknown element id '" +
          /* 201 */elIDs[i] + "' in view '" + this.view.getName() +
          /* 202 */"' of cube '" + this.view.getCube().getName() + "'!");
        /* 203 */this.axisHierarchy.addSelectedElement(el);
        /*     */}
      /* 205 */} else if (path
      .startsWith("/view/axis/axis_hierarchy/localfilter/element")) {
      /* 206 */if (this.currentElementNode != null) {
        /* 207 */check(this.axisHierarchy);
        /* 208 */LocalFilter localFilter = this.axisHierarchy.getLocalFilter();
        /* 209 */ElementNode parentNode = this.currentElementNode.getParent();
        /* 210 */if (parentNode == null)
          /* 211 */localFilter.addVisibleElement(this.currentElementNode);
        /* 212 */this.currentElementNode = parentNode;
        /*     */}
      /*     */}
    /* 215 */else if (path.equals("/view/axis/expanded_paths"))
    /*     */{
      /* 217 */this.axis.addExpanded(buildPath(value));
      /* 218 */} else if (path.equals("/view/axis/axis_hierarchy")) {
      /* 219 */this.axis.add(this.axisHierarchy);
      /* 220 */this.axisHierarchy = null;
      /* 221 */} else if (path.equals("/view/axis")) {
      /* 222 */this.axis = null;
      /*     */}
    /*     */}

  /*     */
  /*     */public static final String getPersistenceString(Axis axis) {
    /* 226 */StringBuffer xml = new StringBuffer();
    /*     */
    /* 228 */xml.append("<axis id=\"");
    /* 229 */xml.append(axis.getId());
    /* 230 */xml.append("\" name=\"");
    /* 231 */xml.append(XMLUtil.printQuoted(axis.getName()));
    /* 232 */xml.append("\" >\r\n");
    /*     */
    /* 234 */writeAxisHierarchies(xml, axis);
    /* 235 */writeExpandedPaths(xml, axis);
    /* 236 */writeProperties(xml, axis);
    /*     */
    /* 238 */xml.append("</axis>\r\n");
    /* 239 */return xml.toString();
    /*     */}

  /*     */
  /*     */private static final void writeProperties(StringBuffer xml, Axis axis)
  /*     */{
    /* 244 */for (Property property : axis.getProperties()) {
      /* 245 */xml.append("<property id=\"");
      /* 246 */xml.append(property.getId());
      /* 247 */xml.append("\" value=\"");
      /* 248 */xml.append(property.getValue().toString());
      /* 249 */xml.append("\" />\r\n");
      /*     */}
    /*     */}

  /*     */
  /*     */private final String[] getParts(String txt, String delimiter) {
    /* 253 */return txt.split(delimiter);
    /*     */}

  /*     */
  /*     */private final ElementPath buildPath(String xmlValue) {
    /* 257 */Hierarchy[] hierarchies = this.axis.getHierarchies();
    /* 258 */String[] elsPerDims = getParts(xmlValue, ":");
    /* 259 */ElementPath elPath = new ElementPath();
    /* 260 */for (int i = 0; i < elsPerDims.length; ++i) {
      /* 261 */String[] elIds = getParts(elsPerDims[i], ",");
      /* 262 */Element[] elPart = new Element[elIds.length];
      /* 263 */for (int e = 0; e < elIds.length; ++e) {
        /* 264 */elPart[e] = hierarchies[i].getElementById(elIds[e]);
        /*     */}
      /* 266 */elPath.addPart(hierarchies[i], elPart);
      /*     */}
    /* 268 */return elPath;
    /*     */}

  /*     */
  /*     */private final void check(AxisHierarchy ah) {
    /* 272 */if (ah == null)
      /* 273 */throw new PaloAPIException(
      /* 274 */"AxisHandler: no AxisHierarchy created for view '" +
      /* 275 */this.view.getName() + "' of cube '" +
      /* 276 */this.view.getCube().getName() + "!");
    /*     */}

  /*     */
  /*     */private static final void writeAxisHierarchies(StringBuffer xml,
    Axis axis) {
    /* 280 */AxisHierarchy[] hierarchies = axis.getAxisHierarchies();
    /* 281 */for (AxisHierarchy axisHierarchy : hierarchies)
      /* 282 */writeAxisHierarchy(xml, axisHierarchy);
    /*     */}

  /*     */
  /*     */private static final void writeAxisHierarchy(StringBuffer xml,
    AxisHierarchy axisHierarchy)
  /*     */{
    /* 288 */Hierarchy hierarchy = axisHierarchy.getHierarchy();
    /* 289 */xml.append("<axis_hierarchy dimension_id=\"");
    /* 290 */xml.append(hierarchy.getDimension().getId());
    /* 291 */xml.append("\" hierarchy_id=\"");
    /* 292 */xml.append(hierarchy.getId());
    /* 293 */xml.append("\" >\r\n");
    /*     */
    /* 295 */Subset2 subset = axisHierarchy.getSubset();
    /* 296 */if (subset != null) {
      /* 297 */xml.append("<subset>");
      /* 298 */xml.append(subset.getId());
      /* 299 */xml.append("</subset>\r\n");
      /*     */}
    /*     */
    /* 302 */writeLocalFilter(xml, axisHierarchy.getLocalFilter());
    /*     */
    /* 304 */if (axisHierarchy.hasSelectedElements()) {
      /* 305 */xml.append("<selected_elements>");
      /* 306 */Element[] elements = axisHierarchy.getSelectedElements();
      /* 307 */xml.append(createIdString(elements, ","));
      /* 308 */xml.append("</selected_elements>\r\n");
      /*     */}
    /*     */
    /* 311 */Property[] properties = axisHierarchy.getProperties();
    /* 312 */for (Property prop : properties) {
      /* 313 */if (prop.getId().equals("com.tensegrity.palo.axis.use_alias")) {
        /* 314 */Attribute attr = (Attribute) prop.getValue();
        /* 315 */if (attr != null) {
          /* 316 */xml.append("<property id=\"");
          /* 317 */xml.append(prop.getId());
          xml.append("\"");
          /* 318 */xml.append(" value=\"");
          xml.append(attr.getId());
          /* 319 */xml.append("\"/>\r\n");
          /*     */}
        /*     */} else {
        /* 322 */Object o = prop.getValue();
        /* 323 */String propValue = (o == null) ? "" : o.toString();
        /* 324 */xml.append("<property id=\"");
        /* 325 */xml.append(prop.getId());
        xml.append("\"");
        /* 326 */xml.append(" value=\"");
        xml.append(propValue);
        /* 327 */xml.append("\"/>\r\n");
        /*     */}
      /*     */}
    /*     */
    /* 331 */xml.append("</axis_hierarchy>\r\n");
    /*     */}

  /*     */
  /*     */private static final void writeExpandedPaths(StringBuffer xml, Axis axis) {
    /* 335 */ElementPath[] paths = axis.getExpandedPaths();
    /* 336 */if (paths.length > 0)
      /* 337 */for (ElementPath path : paths) {
        /* 338 */xml.append("<expanded_paths>");
        /* 339 */writePath(xml, path);
        /* 340 */xml.append("</expanded_paths>\r\n");
        /*     */}
    /*     */}

  /*     */
  /*     */private static final String createIdString(PaloObject[] pObjs,
    String delimiter)
  /*     */{
    /* 347 */StringBuffer str = new StringBuffer();
    /* 348 */int lastObj = pObjs.length - 1;
    /* 349 */for (int i = 0; i < pObjs.length; ++i) {
      /* 350 */str.append(XMLUtil.printQuoted(pObjs[i].getId()));
      /* 351 */if (i < lastObj)
        /* 352 */str.append(delimiter);
      /*     */}
    /* 354 */return str.toString();
    /*     */}

  /*     */
  /*     */private static final void writePath(StringBuffer xml, ElementPath path) {
    /* 358 */Dimension[] dimensions = path.getDimensions();
    /* 359 */int lastDim = dimensions.length - 1;
    /* 360 */for (int i = 0; i < dimensions.length; ++i) {
      /* 361 */Element[] elements = path.getPart(dimensions[i]);
      /* 362 */xml.append(createIdString(elements, ","));
      /* 363 */if (i < lastDim)
        /* 364 */xml.append(":");
      /*     */}
    /*     */}

  /*     */
  /*     */private static final void writeLocalFilter(StringBuffer xml,
    LocalFilter localFilter) {
    /* 369 */if (localFilter == null)
      /* 370 */return;
    /* 371 */xml.append("<localfilter>");
    /* 372 */ElementNode[] roots = localFilter.getVisibleElements();
    /* 373 */writeElementHierarchy(xml, roots);
    /* 374 */xml.append("</localfilter>\r\n");
    /*     */}

  /*     */private static final void writeElementHierarchy(StringBuffer xml,
    ElementNode[] nodes) {
    /* 377 */for (ElementNode node : nodes) {
      /* 378 */xml.append("<element");
      /* 379 */Element element = node.getElement();
      /* 380 */if (!(element instanceof VirtualElement)) {
        /* 381 */xml.append(" id=");
        /* 382 */xml.append(XMLUtil.quote(element.getId()));
        /*     */}
      /* 384 */xml.append(" name=");
      /* 385 */xml.append(XMLUtil.quote(element.getName()));
      /* 386 */xml.append(" >");
      /* 387 */if (node.hasChildren())
        /* 388 */writeElementHierarchy(xml, node.getChildren());
      /* 389 */xml.append("</element>");
      /*     */}
    /*     */}
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi
 * .jar Qualified Name: org.palo.viewapi.internal.io.xml.AxisHandler JD-Core
 * Version: 0.5.4
 */