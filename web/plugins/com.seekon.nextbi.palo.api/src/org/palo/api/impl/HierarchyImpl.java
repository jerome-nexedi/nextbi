/*     */package org.palo.api.impl;

/*     */
/*     */import com.tensegrity.palojava.DbConnection; /*     */
import com.tensegrity.palojava.DimensionInfo; /*     */
import com.tensegrity.palojava.ElementInfo; /*     */
import com.tensegrity.palojava.HierarchyInfo; /*     */
import com.tensegrity.palojava.PaloException; /*     */
import com.tensegrity.palojava.ServerInfo; /*     */
import com.tensegrity.palojava.loader.ElementLoader; /*     */
import java.util.ArrayList; /*     */
import java.util.Collection; /*     */
import java.util.HashMap; /*     */
import java.util.LinkedHashMap; /*     */
import java.util.List; /*     */
import java.util.Map; /*     */
import java.util.Set; /*     */
import org.palo.api.Attribute; /*     */
import org.palo.api.ConnectionEvent; /*     */
import org.palo.api.Consolidation; /*     */
import org.palo.api.Cube; /*     */
import org.palo.api.Database; /*     */
import org.palo.api.Dimension; /*     */
import org.palo.api.Element; /*     */
import org.palo.api.ElementNode; /*     */
import org.palo.api.ElementNodeVisitor; /*     */
import org.palo.api.Hierarchy; /*     */
import org.palo.api.PaloAPIException; /*     */
import org.palo.api.Subset; /*     */
import org.palo.api.persistence.PaloPersistenceException; /*     */
import org.palo.api.subsets.SubsetHandler;

/*     */
/*     */public class HierarchyImpl
/*     */implements Hierarchy
/*     */{
  /*     */private final HierarchyInfo hierInfo;

  /*     */private final Dimension dimension;

  /*     */private final DbConnection dbConnection;

  /*     */private final Map<String, ElementImpl> loadedElements;

  /*     */private final ElementLoader elLoader;

  /*     */private final ConnectionImpl connection;

  /*     */private final Map<Element, Attribute> attributes;

  /*     */private final Database database;

  /*     */private final CompoundKey key;

  /*     */private final SubsetStorageHandler legacySubsetsHandler;

  /*     */
  /*     */static final HierarchyImpl create(ConnectionImpl connection,
    Dimension dimension, HierarchyInfo hierInfo, boolean doEvents)
  /*     */{
    /* 80 */HierarchyImpl hier = new HierarchyImpl(connection, dimension, hierInfo);
    /* 81 */return hier;
    /*     */}

  /*     */
  /*     */private HierarchyImpl(ConnectionImpl connection, Dimension dimension,
    HierarchyInfo hierInfo)
  /*     */{
    /* 98 */this.hierInfo = hierInfo;
    /* 99 */this.dimension = dimension;
    /* 100 */this.connection = connection;
    /* 101 */this.dbConnection = connection.getConnectionInternal();
    /* 102 */this.attributes = new HashMap();
    /* 103 */this.database = dimension.getDatabase();
    /*     */
    /* 105 */if (connection.getType() == 3)
      /* 106 */this.elLoader = this.dbConnection.getElementLoader(hierInfo);
    /*     */else {
      /* 108 */this.elLoader = this.dbConnection
      /* 109 */.getElementLoader(((DimensionImpl) dimension).getInfo());
      /*     */}
    /* 111 */this.loadedElements = new LinkedHashMap();
    /* 112 */this.key =
    /* 115 */new CompoundKey(new Object[] { HierarchyImpl.class,
    /* 113 */connection,
    /* 114 */dimension.getDatabase().getId(),
    /* 115 */hierInfo.getId() });
    /*     */
    /* 117 */this.legacySubsetsHandler =
    /* 118 */((DatabaseImpl) this.database).getLegacySubsetHandler();
    /*     */}

  /*     */
  /*     */public String getId()
  /*     */{
    /* 123 */return this.hierInfo.getId();
    /*     */}

  /*     */
  /*     */public String getName() {
    /* 127 */return this.hierInfo.getName();
    /*     */}

  /*     */
  /*     */public boolean isNormal()
  /*     */{
    /* 132 */return (this.hierInfo.getType() == 0) &&
    /* 132 */(!this.hierInfo.getName().startsWith("#"));
    /*     */}

  /*     */
  /*     */public void rename(String name) {
    /* 136 */this.hierInfo.rename(name);
    /*     */}

  /*     */
  /*     */public boolean canBeModified() {
    /* 140 */return this.hierInfo.canBeModified();
    /*     */}

  /*     */
  /*     */public boolean canCreateChildren() {
    /* 144 */return this.hierInfo.canCreateChildren();
    /*     */}

  /*     */
  /*     */public int getType() {
    /* 148 */return -1;
    /*     */}

  /*     */
  /*     */public Dimension getDimension() {
    /* 152 */return this.dimension;
    /*     */}

  /*     */
  /*     */public int getElementCount() {
    /* 156 */return this.hierInfo.getElementCount();
    /*     */}

  /*     */
  /*     */public final Element[] getElements() {
    /* 160 */String[] ids = this.elLoader.getAllElementIds();
    /* 161 */ArrayList elements = new ArrayList();
    /* 162 */for (String id : ids) {
      /* 163 */ElementInfo info = this.elLoader.load(id);
      /* 164 */Element element = getElement(info);
      /* 165 */if (element != null)
        /* 166 */elements.add(element);
      /*     */}
    /* 168 */return (Element[]) elements.toArray(new Element[elements.size()]);
    /*     */}

  /*     */
  /*     */private final Element getElement(ElementInfo elInfo) {
    /* 172 */if (elInfo == null)
      /* 173 */return null;
    /* 174 */Element element = (Element) this.loadedElements.get(elInfo.getId());
    /* 175 */if (element == null)
    /*     */{
      /* 177 */element = createElement(elInfo);
      /*     */}
    /* 179 */return element;
    /*     */}

  /*     */
  /*     */public final Element getElementAt(int index) {
    /* 183 */ElementInfo elInfo = this.elLoader.load(index);
    /* 184 */return getElement(elInfo);
    /*     */}

  /*     */
  /*     */public final ElementNode[] getAllElementNodes() {
    /* 188 */final ArrayList allnodes = new ArrayList();
    /* 189 */ElementNodeVisitor visitor = new ElementNodeVisitor() {
      /*     */public void visit(ElementNode node, ElementNode parent) {
        /* 191 */allnodes.add(node);
        /*     */}
      /*     */
    };
    /* 194 */Element[] roots = getRootElements();
    /* 195 */if (roots != null) {
      /* 196 */for (int i = 0; i < roots.length; ++i) {
        /* 197 */ElementNode rootNode = new ElementNode(roots[i], null);
        /* 198 */DimensionUtil.traverse(rootNode, visitor);
        /*     */}
      /*     */}
    /* 201 */return (ElementNode[]) allnodes.toArray(new ElementNode[0]);
    /*     */}

  /*     */
  /*     */private final ElementImpl createElement(ElementInfo elInfo)
  /*     */{
    /* 211 */ElementImpl element = ElementImpl.create(this.connection,
      this.dimension, elInfo, this);
    /* 212 */this.loadedElements.put(element.getId(), element);
    /* 213 */return element;
    /*     */}

  /*     */
  /*     */public final Attribute addAttribute(String name) {
    /* 217 */if (isAttributeHierarchy())
      /* 218 */throw new PaloAPIException(
      /* 219 */"Cannot add attributes to an attribute hierarchy!");
    /*     */try {
      /* 221 */Hierarchy attrHier = getAttributeHierarchy();
      /* 222 */Element attrElement = attrHier.addElement(name,
      /* 223 */1);
      /* 224 */Attribute attribute = addAttribute(attrElement);
      /* 225 */fireAttributesAdded(new Attribute[] { attribute });
      /*     */
      /* 227 */return attribute;
      /*     */} catch (PaloException e) {
      /* 229 */throw new PaloAPIException(
        "Attribute " + name + " already in use!!", e);
      /*     */}
    /*     */}

  /*     */
  /*     */public final void removeAllAttributes() {
    /* 234 */Hierarchy attrHier = getAttributeHierarchy();
    /* 235 */if (attrHier == null)
      /* 236 */return;
    /* 237 */Element[] allAttrs = attrHier.getElements();
    /* 238 */for (int i = 0; i < allAttrs.length; ++i) {
      /* 239 */attrHier.removeElement(allAttrs[i]);
      /*     */}
    /*     */
    /* 242 */fireAttributesRemoved(this.attributes.values().toArray());
    /*     */
    /* 244 */this.attributes.clear();
    /*     */}

  /*     */
  /*     */public final void removeAttribute(Attribute attribute) {
    /* 248 */Hierarchy attrHier = getAttributeHierarchy();
    /* 249 */if (attrHier == null)
      /* 250 */return;
    /* 251 */Element attrElement = attrHier.getElementByName(attribute.getName());
    /* 252 */if (attrElement != null) {
      /* 253 */attrHier.removeElement(attrElement);
      /* 254 */this.attributes.remove(attrElement);
      /*     */
      /* 256 */fireAttributesRemoved(new Attribute[] { attribute });
      /*     */}
    /*     */}

  /*     */
  /*     */public final Attribute[] getAttributes() {
    /* 261 */Hierarchy attrHier = getAttributeHierarchy();
    /* 262 */if (attrHier == null) {
      /* 263 */return new Attribute[0];
      /*     */}
    /* 265 */Element[] attrElements = attrHier.getElements();
    /* 266 */Attribute[] attributes = new Attribute[attrElements.length];
    /* 267 */for (int i = 0; i < attrElements.length; ++i)
      /* 268 */attributes[i] = getAttribute(attrElements[i]);
    /* 269 */return attributes;
    /*     */}

  /*     */
  /*     */public final Attribute getAttribute(String attrId) {
    /* 273 */Hierarchy attrHier = getAttributeHierarchy();
    /* 274 */if (attrHier != null) {
      /* 275 */Element attrElement = attrHier.getElementById(attrId);
      /*     */
      /* 277 */return getAttribute(attrElement);
      /*     */}
    /* 279 */return null;
    /*     */}

  /*     */
  /*     */public final Attribute getAttributeByName(String attrName) {
    /* 283 */Hierarchy attrHier = getAttributeHierarchy();
    /* 284 */if (attrHier != null) {
      /* 285 */Element attrElement = attrHier.getElementByName(attrName);
      /*     */
      /* 287 */return getAttribute(attrElement);
      /*     */}
    /* 289 */return null;
    /*     */}

  /*     */
  /*     */public final Object[] getAttributeValues(Attribute[] attributes,
    Element[] elements) {
    /* 293 */if (attributes.length != elements.length)
      /* 294 */throw new PaloAPIException(
        "The number of attributes and elements has to be equal!");
    /* 295 */CubeImpl attrCube = (CubeImpl) getAttributeCube();
    /* 296 */if (attrCube == null)
      /* 297 */return new Object[0];
    /* 298 */Element[][] coordinates = getCoordinates(attributes, elements);
    /* 299 */return attrCube.getDataBulk(coordinates);
    /*     */}

  /*     */
  /*     */public final Cube getAttributeCube() {
    /* 303 */DimensionInfo dimInfo = ((DimensionImpl) this.dimension).getInfo();
    /* 304 */String attrId = dimInfo.getAttributeCube();
    /*     */
    /* 306 */if ((attrId == null) || (attrId.length() == 0))
      /* 307 */return this.database.getCubeByName("#_" + dimInfo.getName());
    /* 308 */return this.database.getCubeById(attrId);
    /*     */}

  /*     */
  /*     */public final void setAttributeValues(Attribute[] attributes,
    Element[] elements, Object[] values)
  /*     */{
    /* 313 */if ((attributes.length == elements.length) &&
    /* 314 */(attributes.length == values.length)) {
      /* 315 */Cube attrCube = getAttributeCube();
      /* 316 */Element[][] coordinates = getCoordinates(attributes, elements);
      /* 317 */attrCube.setDataArray(
      /* 318 */coordinates, values, 0);
      /*     */
      /* 320 */fireAttributesChanged(attributes);
      /*     */} else {
      /* 322 */throw new PaloAPIException(
      /* 323 */"The number of attributes, elements and values has to be equal!");
      /*     */}
    /*     */}

  /*     */
  /*     */public final Hierarchy getAttributeHierarchy() {
    /* 327 */DimensionInfo dimInfo = ((DimensionImpl) this.dimension).getInfo();
    /* 328 */String attrId = dimInfo.getAttributeDimension();
    /*     */Dimension d;
    /* 331 */if ((attrId == null) || (attrId.length() == 0))
      /* 332 */d = this.database.getDimensionByName("#_" + dimInfo.getName() + "_");
    /*     */else {
      /* 334 */d = this.database.getDimensionById(attrId);
      /*     */}
    /* 336 */if (d == null) {
      /* 337 */return null;
      /*     */}
    /* 339 */return d.getDefaultHierarchy();
    /*     */}

  /*     */
  /*     */public final Element addElement(String name, int type)
  /*     */{
    /* 344 */if (name == null) {
      /* 345 */return null;
      /*     */}
    /* 347 */Element element = addElementInternal(name, type);
    /* 348 */this.dbConnection.reload(((DimensionImpl) this.dimension).getInfo());
    /*     */
    /* 351 */fireElementsAdded(new Element[] { element });
    /* 352 */return element;
    /*     */}

  /*     */
  /*     */public final void addElements(String[] names, int[] types) {
    /* 356 */if (this.dbConnection.getServerInfo().getMajor() >= 3) {
      /* 357 */addElements(names, types, new Element[0][0], new double[0][0]);
      /*     */} else {
      /* 359 */if ((names == null) || (types == null)
        || (names.length != types.length))
        /* 360 */return;
      /* 361 */Element[] _elements = new Element[names.length];
      /* 362 */for (int i = 0; i < names.length; ++i) {
        /* 363 */_elements[i] = addElementInternal(names[i], types[i]);
        /*     */}
      /* 365 */this.dbConnection.reload(((DimensionImpl) this.dimension).getInfo());
      /*     */
      /* 368 */fireElementsAdded(_elements);
      /*     */}
    /*     */}

  /*     */
  /*     */public final void addElements(String[] names, int type,
    Element[][] children, double[][] weights)
  /*     */{
    /*     */try
    /*     */{
      /* 376 */if (!this.connection.isLegacy())
        /* 377 */type = ElementImpl.elType2infoType(type);
      /* 378 */ElementInfo[] newElements = this.elLoader.createBulk(names, type,
      /* 379 */Util.getElementInfos(children), weights);
      /* 380 */List elements = new ArrayList();
      /* 381 */for (ElementInfo el : newElements) {
        /* 382 */if (this.loadedElements.containsKey(el.getId()))
          /* 383 */throw new PaloAPIException("Element '" + el.getName() +
          /* 384 */"' already exists!!");
        /* 385 */elements.add(createElement(el));
        /*     */}
      /*     */
      /* 388 */this.dimension.reload(false);
      /* 389 */fireElementsAdded(elements.toArray());
      /*     */} catch (PaloException pex) {
      /* 391 */throw new PaloAPIException(pex.getMessage(), pex);
      /*     */}
    /*     */}

  /*     */
  /*     */public final void addElements(String[] names, int[] types,
    Element[][] children, double[][] weights)
  /*     */{
    /*     */try
    /*     */{
      /* 399 */if (!this.connection.isLegacy()) {
        /* 400 */int i = 0;
        for (int n = types.length; i < n; ++i) {
          /* 401 */types[i] = ElementImpl.elType2infoType(types[i]);
          /*     */}
        /*     */}
      /* 404 */ElementInfo[] newElements = this.elLoader.createBulk(names, types,
      /* 405 */Util.getElementInfos(children), weights);
      /* 406 */List elements = new ArrayList();
      /* 407 */for (ElementInfo el : newElements) {
        /* 408 */if (this.loadedElements.containsKey(el.getId()))
          /* 409 */throw new PaloAPIException("Element '" + el.getName() +
          /* 410 */"' already exists!!");
        /* 411 */elements.add(createElement(el));
        /*     */}
      /*     */
      /* 414 */this.dimension.reload(false);
      /* 415 */fireElementsAdded(elements.toArray());
      /*     */} catch (PaloException pex) {
      /* 417 */throw new PaloAPIException(pex.getMessage(), pex);
      /*     */}
    /*     */}

  /*     */
  /*     */public final void updateConsolidations(Consolidation[] consolidations)
  /*     */{
    /*     */try {
      /* 424 */int type = 2;
      /* 425 */if (!this.connection.isLegacy()) {
        /* 426 */type = ElementImpl.elType2infoType(type);
        /*     */}
      /* 428 */LinkedHashMap<Element, List> childMap = new LinkedHashMap();
      /* 429 */LinkedHashMap<Element, List> weightMap = new LinkedHashMap();
      /* 430 */for (Consolidation c : consolidations) {
        /* 431 */if (!childMap.containsKey(c.getParent())) {
          /* 432 */childMap.put(c.getParent(), new ArrayList());
          /*     */}
        /* 434 */((List) childMap.get(c.getParent())).add(c.getChild());
        /* 435 */if (!weightMap.containsKey(c.getParent())) {
          /* 436 */weightMap.put(c.getParent(), new ArrayList());
          /*     */}
        /* 438 */((List) weightMap.get(c.getParent())).add(Double.valueOf(c
          .getWeight()));
        /*     */}
      /* 440 */Element[] elements = (Element[]) childMap.keySet().toArray(
        new Element[0]);
      /* 441 */Element[][] children = new Element[childMap.size()][];
      /* 442 */int k = 0;
      /* 443 */for (Element parent : childMap.keySet()) {
        /* 444 */children[(k++)] = ((Element[]) ((List) childMap.get(parent))
          .toArray(new Element[0]));
        /*     */}
      /*     */
      /* 447 */Double[][] weights = new Double[weightMap.size()][];
      /* 448 */k = 0;
      /* 449 */for (Element parent : weightMap.keySet()) {
        /* 450 */weights[(k++)] = ((Double[]) ((List) weightMap.get(parent))
          .toArray(new Double[0]));
        /*     */}
      /*     */
      /* 453 */this.elLoader.replaceBulk(
      /* 454 */Util.getElementInfos(elements), type,
      /* 455 */Util.getElementInfos(children), weights);
      /* 456 */this.dimension.reload(false);
      /*     */} catch (PaloException pex) {
      /* 458 */throw new PaloAPIException(pex.getMessage(), pex);
      /*     */}
    /*     */}

  /*     */
  /*     */public final void removeConsolidations(Element[] elements)
  /*     */{
    /*     */try {
      /* 465 */int type = 0;
      /* 466 */if (!this.connection.isLegacy())
        /* 467 */type = ElementImpl.elType2infoType(type);
      /* 468 */this.elLoader.replaceBulk(
      /* 469 */Util.getElementInfos(elements), type,
      /* 470 */null, null);
      /* 471 */this.dimension.reload(false);
      /*     */} catch (PaloException pex) {
      /* 473 */throw new PaloAPIException(pex.getMessage(), pex);
      /*     */}
    /*     */}

  /*     */
  /*     */public final ElementNode[] getElementsTree() {
    /* 478 */Element[] roots = getRootElements();
    /* 479 */if (Boolean.parseBoolean("wpalo")) {
      /* 480 */ElementNode[] rootNodes = new ElementNode[roots.length];
      /* 481 */for (int i = 0; i < roots.length; ++i)
        /* 482 */rootNodes[i] = new ElementNode2(roots[i]);
      /* 483 */return rootNodes;
      /*     */}
    /*     */
    /* 487 */final ArrayList rootnodes = new ArrayList();
    /* 488 */ElementNodeVisitor visitor = new ElementNodeVisitor() {
      /*     */public void visit(ElementNode node, ElementNode parent) {
        /* 490 */if (parent == null)
          /* 491 */rootnodes.add(node);
        /*     */}
      /*     */
    };
    /* 494 */if (roots != null) {
      /* 495 */for (int i = 0; i < roots.length; ++i) {
        /* 496 */ElementNode rootNode = new ElementNode(roots[i], null);
        /* 497 */DimensionUtil.forceTraverse(rootNode, visitor);
        /*     */}
      /*     */}
    /* 500 */ElementNode[] result = (ElementNode[]) rootnodes
      .toArray(new ElementNode[0]);
    /* 501 */return result;
    /*     */}

  /*     */
  /*     */public final Element[] getRootElements()
  /*     */{
    /* 517 */String[] ids = this.elLoader.getAllElementIds();
    /* 518 */ArrayList roots = new ArrayList();
    /* 519 */for (String id : ids) {
      /* 520 */ElementInfo info = this.elLoader.load(id);
      /*     */
      /* 524 */if (info == null) {
        /*     */continue;
        /*     */}
      /* 527 */if (info.getParentCount() == 0) {
        /* 528 */Element element = getElement(info);
        /* 529 */if (element != null)
          /* 530 */roots.add(element);
        /*     */}
      /*     */}
    /* 533 */return (Element[]) roots.toArray(new Element[roots.size()]);
    /*     */}

  /*     */
  /*     */public final void visitElementTree(ElementNodeVisitor visitor) {
    /* 537 */Element[] roots = getRootElements();
    /* 538 */if (roots != null)
      /* 539 */for (int i = 0; i < roots.length; ++i) {
        /* 540 */ElementNode rootNode = new ElementNode(roots[i], null);
        /* 541 */DimensionUtil.traverse(rootNode, visitor);
        /*     */}
    /*     */}

  /*     */
  /*     */public final void removeElements(Element[] elements)
  /*     */{
    /* 553 */removeElementsInternal(elements);
    /* 554 */this.dbConnection.reload(((DimensionImpl) this.dimension).getInfo());
    /* 555 */fireElementsRemoved(elements);
    /*     */}

  /*     */
  /*     */public final Element getElementById(String id)
  /*     */{
    /*     */try {
      /* 561 */ElementInfo elInfo = this.elLoader.load(id);
      /* 562 */return getElement(elInfo);
      /*     */}
    /*     */catch (PaloException localPaloException) {
      /*     */}
    /* 566 */return null;
    /*     */}

  /*     */
  /*     */public final Element getElementByName(String name) {
    /* 570 */ElementInfo elInfo = this.elLoader.loadByName(name);
    /* 571 */return getElement(elInfo);
    /*     */}

  /*     */
  /*     */public final String[] getElementNames() {
    /* 575 */String[] ids = this.elLoader.getAllElementIds();
    /* 576 */ArrayList names = new ArrayList();
    /* 577 */for (int i = 0; i < ids.length; ++i)
      /*     */try {
        /* 579 */ElementInfo info = this.elLoader.load(ids[i]);
        /* 580 */names.add(info.getName());
        /*     */}
      /*     */catch (PaloException localPaloException)
      /*     */{
        /*     */}
    /* 585 */return (String[]) names.toArray(new String[0]);
    /*     */}

  /*     */
  /*     */public final void renameElement(Element element, String newName) {
    /* 589 */element.rename(newName);
    /*     */}

  /*     */
  /*     */public final Element[] getElementsInOrder() {
    /* 593 */final ArrayList result = new ArrayList();
    /* 594 */DimensionUtil.ElementVisitor visitor =
    /* 595 */new DimensionUtil.ElementVisitor() {
      /*     */public void visit(Element element, Element parent) {
        /* 597 */result.add(element);
        /*     */}
      /*     */
    };
    /* 600 */Element[] roots = getRootElements();
    /* 601 */if (roots != null) {
      /* 602 */for (int i = 0; i < roots.length; ++i)
        /* 603 */DimensionUtil.traverse(roots[i], visitor);
      /*     */}
    /* 605 */return (Element[]) result.toArray(new Element[0]);
    /*     */}

  /*     */
  /*     */public final Consolidation newConsolidation(Element element,
    Element parent, double weight)
  /*     */{
    /* 610 */return ConsolidationImpl.create(this.connection, parent, element,
      weight);
    /*     */}

  /*     */
  /*     */public final void removeElement(Element element) {
    /* 614 */if (removeElementInternal(element)) {
      /* 615 */this.dbConnection.reload(((DimensionImpl) getDimension()).getInfo());
      /* 616 */fireElementsRemoved(new Element[] { element });
      /*     */}
    /*     */}

  /*     */
  /*     */public final int getMaxDepth() {
    /* 621 */return this.hierInfo.getMaxDepth();
    /*     */}

  /*     */
  /*     */public final int getMaxLevel() {
    /* 625 */return this.hierInfo.getMaxLevel();
    /*     */}

  /*     */
  /*     */private final boolean removeElementInternal(Element element) {
    /* 629 */if (this.loadedElements.containsKey(element.getId())) {
      /* 630 */ElementImpl _element = (ElementImpl) element;
      /* 631 */if (this.elLoader.delete(_element.getInfo()))
      /*     */{
        /* 633 */Element[] children = element.getChildren();
        /* 634 */for (int i = 0; i < children.length; ++i)
          /* 635 */((ElementImpl) children[i]).reload(false);
        /* 636 */Element[] parents = element.getParents();
        /* 637 */for (int i = 0; i < parents.length; ++i) {
          /* 638 */((ElementImpl) parents[i]).reload(false);
          /*     */}
        /*     */
        /* 641 */this.loadedElements.remove(element.getId());
        /* 642 */return true;
        /*     */}
      /*     */}
    /* 645 */return false;
    /*     */}

  /*     */
  /*     */private final boolean removeElementsInternal(Element[] elements) {
    /* 649 */ArrayList elInfos = new ArrayList();
    /* 650 */HashMap children = new HashMap();
    /* 651 */HashMap parents = new HashMap();
    /*     */
    /* 653 */for (Element e : elements) {
      /* 654 */elInfos.add(((ElementImpl) e).getInfo());
      /* 655 */children.put(e, e.getChildren());
      /* 656 */parents.put(e, e.getParents());
      /*     */}
    /* 658 */if (this.elLoader.delete((ElementInfo[]) elInfos
      .toArray(new ElementInfo[0])))
    /*     */{
      /* 660 */Set deleted = children.keySet();
      /* 661 */for (Element e : elements)
      /*     */{
        /* 663 */for (Element child : (Element[]) children.get(e))
          /* 664 */reload(child, deleted);
        /* 665 */for (Element parent : (Element[]) parents.get(e)) {
          /* 666 */reload(parent, deleted);
          /*     */}
        /* 668 */this.loadedElements.remove(e.getId());
        /*     */}
      /* 670 */return true;
      /*     */}
    /* 672 */return false;
    /*     */}

  /*     */
  /*     */private final void reload(Element element, Set<Element> deleted) {
    /* 676 */if ((element != null) && (!deleted.contains(element)))
      /* 677 */((ElementImpl) element).reload(false);
    /*     */}

  /*     */
  /*     */private final void fireElementsAdded(Object[] elements)
  /*     */{
    /* 683 */this.connection.fireEvent(
    /* 684 */new ConnectionEvent(this.connection, this,
    /* 684 */5, elements));
    /*     */}

  /*     */
  /*     */private final void fireElementsRemoved(Object[] elements) {
    /* 688 */this.connection.fireEvent(
    /* 689 */new ConnectionEvent(this.connection, this,
    /* 689 */6, elements));
    /*     */}

  /*     */
  /*     */private final Element addElementInternal(String name, int type)
  /*     */{
    /*     */try {
      /* 695 */if (!this.connection.isLegacy())
        /* 696 */type = ElementImpl.elType2infoType(type);
      /* 697 */ElementInfo elInfo =
      /* 698 */this.elLoader.create(name, type, new ElementInfo[0], new double[0]);
      /* 699 */if (this.loadedElements.containsKey(elInfo.getId()))
        /* 700 */throw new PaloAPIException("Element '" + name +
        /* 701 */"' already exists!!");
      /* 702 */Element element = createElement(elInfo);
      /* 703 */return element;
      /*     */} catch (PaloException pex) {
      /* 705 */pex.printStackTrace();
      /* 706 */throw new PaloAPIException(pex.getMessage(), pex);
      /*     */}
    /*     */}

  /*     */
  /*     */private final void fireAttributesAdded(Object[] attributes) {
    /* 711 */this.connection.fireEvent(
    /* 712 */new ConnectionEvent(this.connection, this,
    /* 712 */15, attributes));
    /*     */}

  /*     */
  /*     */private final void fireAttributesChanged(Object[] attributes) {
    /* 716 */this.connection.fireEvent(
    /* 719 */new ConnectionEvent(this.connection, this,
    /* 718 */17,
    /* 719 */attributes));
    /*     */}

  /*     */
  /*     */private final void fireAttributesRemoved(Object[] attributes) {
    /* 723 */this.connection.fireEvent(
    /* 726 */new ConnectionEvent(this.connection, this,
    /* 725 */16,
    /* 726 */attributes));
    /*     */}

  /*     */
  /*     */private final Element[][] getCoordinates(Attribute[] attributes,
    Element[] elements)
  /*     */{
    /* 731 */Element[][] coordinates = new Element[attributes.length][];
    /* 732 */for (int i = 0; i < attributes.length; ++i) {
      /* 733 */coordinates[i] = new Element[2];
      /* 734 */coordinates[i][0] =
      /* 735 */((AttributeImpl) attributes[i]).getAttributeElement();
      /* 736 */coordinates[i][1] = elements[i];
      /*     */}
    /* 738 */return coordinates;
    /*     */}

  /*     */
  /*     */final Attribute getAttribute(Element attrElement) {
    /* 742 */Object attribute = this.attributes.get(attrElement);
    /* 743 */if (attribute == null) {
      /* 744 */attribute = addAttribute(attrElement);
      /*     */}
    /* 746 */return (Attribute) attribute;
    /*     */}

  /*     */
  /*     */private final Attribute addAttribute(Element attrElement) {
    /* 750 */if (attrElement == null) {
      /* 751 */return null;
      /*     */}
    /* 753 */Cube attrCube = getAttributeCube();
    /* 754 */Attribute attribute = AttributeImpl.create(attrElement, attrCube);
    /* 755 */this.attributes.put(attrElement, attribute);
    /* 756 */return attribute;
    /*     */}

  /*     */
  /*     */public final boolean isAttributeHierarchy() {
    /* 760 */return ((DimensionImpl) this.dimension).getInfo().getType() ==
    /* 761 */2;
    /*     */}

  /*     */
  /*     */public final boolean equals(Object other) {
    /* 765 */if (other instanceof HierarchyImpl) {
      /* 766 */return this.key.equals(((HierarchyImpl) other).key);
      /*     */}
    /* 768 */return false;
    /*     */}

  /*     */
  /*     */public final int hashCode() {
    /* 772 */return this.key.hashCode();
    /*     */}

  /*     */
  /*     */public final void reload(boolean doEvents)
  /*     */{
    /* 777 */LinkedHashMap oldElements = new LinkedHashMap(this.loadedElements);
    /* 778 */ArrayList addedElements = new ArrayList();
    /*     */
    /* 781 */this.dbConnection.reload(((DimensionImpl) this.dimension).getInfo());
    /*     */
    /* 783 */this.loadedElements.clear();
    /* 784 */this.elLoader.reset();
    /*     */
    /* 787 */String[] elIDs = this.elLoader.getAllElementIds();
    /* 788 */for (String id : elIDs) {
      /* 789 */ElementImpl element = (ElementImpl) oldElements.get(id);
      /* 790 */if (element == null) {
        /* 791 */ElementInfo info = this.elLoader.load(id);
        /* 792 */element = createElement(info);
        /*     */}
      /*     */else {
        /* 795 */oldElements.remove(id);
        /* 796 */this.loadedElements.put(id, element);
        /*     */}
      /* 798 */element.reload(doEvents);
      /*     */}
    /*     */
    /* 801 */if (doEvents) {
      /* 802 */if (!oldElements.isEmpty()) {
        /* 803 */fireElementsRemoved(oldElements.values().toArray());
        /*     */}
      /* 805 */if (!addedElements.isEmpty())
        /* 806 */fireElementsAdded(addedElements.toArray());
      /*     */}
    /*     */}

  /*     */
  /*     */final void clearCache()
  /*     */{
    /* 812 */for (ElementImpl element : this.loadedElements.values()) {
      /* 813 */element.clearCache();
      /*     */}
    /* 815 */this.loadedElements.clear();
    /* 816 */this.elLoader.reset();
    /*     */
    /* 818 */this.attributes.clear();
    /*     */}

  /*     */
  /*     */final ElementLoader getElementLoader() {
    /* 822 */return this.elLoader;
    /*     */}

  /*     */
  /*     */public Subset addSubset(String name)
  /*     */{
    /* 834 */return this.legacySubsetsHandler.addSubset(this, name);
    /*     */}

  /*     */
  /*     */public Subset getSubset(String id) {
    /*     */try {
      /* 839 */return this.legacySubsetsHandler.getSubset(this, id);
      /*     */} catch (PaloPersistenceException e) {
      /* 841 */e.printStackTrace();
      /*     */}
    /* 843 */return null;
    /*     */}

  /*     */
  /*     */public SubsetHandler getSubsetHandler() {
    /* 847 */return this.dimension.getSubsetHandler();
    /*     */}

  /*     */
  /*     */public Subset[] getSubsets() {
    /*     */try {
      /* 852 */return this.legacySubsetsHandler.getSubsets(this);
      /*     */} catch (PaloPersistenceException e) {
      /* 854 */e.printStackTrace();
      /*     */}
    /* 856 */return new Subset[0];
    /*     */}

  /*     */
  /*     */public void removeSubset(Subset subset) {
    /* 860 */this.legacySubsetsHandler.removeSubset(this, subset);
    /*     */}

  /*     */
  /*     */public final boolean isSubsetHierarchy() {
    /* 864 */return ((DatabaseImpl) this.database)
      .isSubsetDimension(getDimension());
    /*     */}

  /*     */public final String toString() {
    /* 867 */StringBuffer str = new StringBuffer();
    /* 868 */str.append("Hierarchy(\"");
    /* 869 */str.append(getName());
    /* 870 */str.append("\")[");
    /* 871 */str.append(hashCode());
    /* 872 */str.append("]");
    /* 873 */return str.toString();
    /*     */}

  /*     */
  /*     */final void resetElementsCache()
  /*     */{
    /* 881 */this.elLoader.reset();
    /* 882 */this.loadedElements.clear();
    /*     */}
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name: org.palo.api.impl.HierarchyImpl JD-Core Version: 0.5.4
 */