/*     */package org.palo.viewapi.internal;

/*     */
/*     */import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.palo.api.Connection;
import org.palo.api.Consolidation;
import org.palo.api.Element;
import org.palo.api.ElementNode;
import org.palo.api.ElementNodeVisitor;
import org.palo.api.Hierarchy;
import org.palo.api.parameters.ParameterProvider;
import org.palo.api.parameters.ParameterReceiver;
import org.palo.api.subsets.Subset2;

/*     */
/*     */public class DynamicFolder extends AbstractExplorerTreeNode
/*     */implements ParameterProvider
/*     */{
  /*     */public static final int DF_TYPE = 1;

  /*     */private String[] parameterNames;

  /*     */private final HashMap<String, Object> parameterValue;

  /*     */private final HashMap<String, Object[]> possibleValues;

  /*     */private Hierarchy sourceHierarchy;

  /*     */private Subset2 sourceSubset;

  /*     */private Element variableElement;

  /* 162 */private DynamicFolder parentDynamicFolder = null;

  /*     */
  /*     */public static DynamicFolder internalCreate(ExplorerTreeNode parent,
    Hierarchy sourceHierarchy, Subset2 sourceSubset, String id, String name)
  /*     */{
    /* 182 */return new DynamicFolder(parent, sourceHierarchy, sourceSubset, id,
      name);
    /*     */}

  /*     */
  /*     */private DynamicFolder(ExplorerTreeNode parent,
    Hierarchy sourceHierarchy, Subset2 sourceSubset, String id, String name)
  /*     */{
    /* 198 */super(id, true);
    /* 199 */this.name = name;
    /* 200 */this.sourceHierarchy = sourceHierarchy;
    /* 201 */this.sourceSubset = sourceSubset;
    /* 202 */setParent(parent);
    /* 203 */if (parent != null) {
      /* 204 */parent.addChild(this);
      /*     */}
    /* 206 */this.parameterNames = new String[0];
    /* 207 */this.possibleValues = new HashMap();
    /* 208 */this.parameterValue = new HashMap();
    /*     */}

  /*     */
  /*     */public DynamicFolder(ExplorerTreeNode parent, Hierarchy sourceHierarchy,
    Subset2 sourceSubset)
  /*     */{
    /* 222 */super("df_", parent);
    /* 223 */this.sourceHierarchy = sourceHierarchy;
    /* 224 */this.sourceSubset = sourceSubset;
    /* 225 */if (parent != null) {
      /* 226 */parent.addChild(this);
      /*     */}
    /* 228 */this.parameterNames = new String[0];
    /* 229 */this.possibleValues = new HashMap();
    /* 230 */this.parameterValue = new HashMap();
    /*     */}

  /*     */
  /*     */public DynamicFolder(ExplorerTreeNode parent, Hierarchy sourceHierarchy,
    Subset2 sourceSubset, String name)
  /*     */{
    /* 247 */super("df_", parent, name);
    /* 248 */this.sourceHierarchy = sourceHierarchy;
    /* 249 */this.sourceSubset = sourceSubset;
    /* 250 */if (parent != null) {
      /* 251 */parent.addChild(this);
      /*     */}
    /* 253 */this.parameterNames = new String[0];
    /* 254 */this.possibleValues = new HashMap();
    /* 255 */this.parameterValue = new HashMap();
    /*     */}

  /*     */
  /*     */DynamicFolder(String id, String name, DynamicFolder parent,
    DynamicFolder original) {
    /* 259 */super(parent, id, name);
    /* 260 */this.sourceHierarchy = original.sourceHierarchy;
    /* 261 */this.sourceSubset = original.sourceSubset;
    /* 262 */this.possibleValues = ((HashMap) original.possibleValues.clone());
    /* 263 */this.parameterNames = ((String[]) original.parameterNames.clone());
    /* 264 */this.children = ((ArrayList) original.children.clone());
    /* 265 */this.parameterValue = new HashMap();
    /* 266 */for (String key : original.parameterValue.keySet())
      /* 267 */this.parameterValue.put(key, original.parameterValue.get(key));
    /*     */}

  /*     */
  /*     */public String[] getParameterNames()
  /*     */{
    /* 305 */return this.parameterNames;
    /*     */}

  /*     */
  /*     */public Object[] getPossibleValuesFor(String parameterName)
  /*     */{
    /* 314 */if (!this.possibleValues.containsKey(parameterName)) {
      /* 315 */return new Object[0];
      /*     */}
    /* 317 */return (Object[]) this.possibleValues.get(parameterName);
    /*     */}

  /*     */
  /*     */public ParameterReceiver getSourceObject()
  /*     */{
    /* 324 */return null;
    /*     */}

  /*     */
  /*     */public void setParameterNames(String[] parameterNames)
  /*     */{
    /* 332 */this.parameterNames = parameterNames;
    /*     */}

  /*     */
  /*     */public void setPossibleValuesFor(String parameterName,
    Object[] possibleValues)
  /*     */{
    /* 340 */this.possibleValues.put(parameterName, possibleValues);
    /*     */}

  /*     */
  /*     */public void setSourceObject(ParameterReceiver sourceObject)
  /*     */{
    /*     */}

  /*     */
  /*     */public Hierarchy getSourceHierarchy()
  /*     */{
    /* 356 */return this.sourceHierarchy;
    /*     */}

  /*     */
  /*     */public Subset2 getSourceSubset()
  /*     */{
    /* 364 */return this.sourceSubset;
    /*     */}

  /*     */
  /*     */public Element getVariableElement()
  /*     */{
    /* 377 */return this.variableElement;
    /*     */}

  /*     */
  /*     */void setVariableElement(Element e)
  /*     */{
    /* 386 */this.variableElement = e;
    /*     */}

  /*     */
  /*     */public Object getDefaultValue(String parameterName)
  /*     */{
    /* 395 */if (this.sourceSubset != null) {
      /* 396 */Element[] els = this.sourceSubset.getElements();
      /* 397 */if ((els != null) && (els.length > 0)) {
        /* 398 */return els[0];
        /*     */}
      /* 400 */return null;
      /*     */}
    /* 402 */if (this.sourceHierarchy.getElementCount() > 0) {
      /* 403 */return this.sourceHierarchy.getElementAt(0);
      /*     */}
    /* 405 */return null;
    /*     */}

  /*     */
  /*     */public Object getParameterValue(String parameterName)
  /*     */{
    /* 413 */if (parameterName.equals("Hierarchy"))
      /* 414 */return this.sourceHierarchy;
    /* 415 */if (parameterName.equals("Subset")) {
      /* 416 */return this.sourceSubset;
      /*     */}
    /* 418 */return this.parameterValue.get(parameterName);
    /*     */}

  /*     */
  /*     */public boolean isParameterized()
  /*     */{
    /* 425 */return true;
    /*     */}

  /*     */
  /*     */public void setParameter(String parameterName, Object parameterValue)
  /*     */{
    /* 432 */if ((parameterName.equals("Hierarchy"))
      && (parameterValue instanceof Hierarchy))
      /* 433 */this.sourceHierarchy = ((Hierarchy) parameterValue);
    /* 434 */else if ((parameterName.equals("Subset"))
      && (parameterValue instanceof Subset2)) {
      /* 435 */this.sourceSubset = ((Subset2) parameterValue);
      /*     */}
    /* 437 */this.parameterValue.put(parameterName, parameterValue);
    /*     */}

  /*     */
  /*     */private final String getElementPath(ElementNode e)
  /*     */{
    /* 451 */StringBuffer buffer = new StringBuffer(e.getElement().getName());
    /* 452 */ElementNode p = e.getParent();
    /* 453 */while (p != null) {
      /* 454 */buffer.insert(0, p.getElement().getName() + "/");
      /* 455 */p = p.getParent();
      /*     */}
    /* 457 */return buffer.toString();
    /*     */}

  /*     */
  /*     */private static void traverse(ElementNode n, ElementNodeVisitor v)
  /*     */{
    /* 462 */traverse(n, null, v);
    /*     */}

  /*     */
  /*     */static void traverse(ElementNode n, ElementNode p, ElementNodeVisitor v)
  /*     */{
    /* 467 */v.visit(n, p);
    /* 468 */Element[] children = n.getElement().getChildren();
    /* 469 */Consolidation[] consolidations = n.getElement().getConsolidations();
    /* 470 */if (children == null)
      /* 471 */return;
    /* 472 */for (int i = 0; i < children.length; ++i)
    /*     */{
      /* 474 */if (children[i] == null)
        /*     */continue;
      /* 476 */ElementNode child = new ElementNode(children[i], consolidations[i]);
      /* 477 */n.forceAddChild(child);
      /* 478 */traverse(child, n, v);
      /*     */}
    /*     */}

  /*     */
  /*     */private final ElementNode[] getAllElementNodes(Subset2 subset) {
    /* 483 */final ArrayList allnodes = new ArrayList();
    /* 484 */ElementNodeVisitor visitor = new ElementNodeVisitor() {
      /*     */public void visit(ElementNode node, ElementNode parent) {
        /* 486 */allnodes.add(node);
        /*     */}
      /*     */
    };
    /* 489 */ElementNode[] roots = subset.getRootNodes();
    /* 490 */if (roots != null) {
      /* 491 */for (int i = 0; i < roots.length; ++i) {
        /* 492 */traverse(roots[i], visitor);
        /*     */}
      /*     */}
    /* 495 */return (ElementNode[]) allnodes.toArray(new ElementNode[0]);
    /*     */}

  /*     */
  /*     */public ExplorerTreeNode[] getCalculatedChildren() {
    /* 499 */if (this.children.size() == 0) {
      /* 500 */return new ExplorerTreeNode[0];
      /*     */}
    /* 502 */if ((this.sourceHierarchy == null) && (this.sourceSubset == null))
      /* 503 */return (ExplorerTreeNode[]) this.children
        .toArray(new ExplorerTreeNode[0]);
    /*     */ElementNode[] elems;
    /* 507 */if (this.sourceSubset == null)
      /* 508 */elems = this.sourceHierarchy.getAllElementNodes();
    /*     */else {
      /* 510 */elems = getAllElementNodes(this.sourceSubset);
      /*     */}
    /* 512 */if ((elems == null) || (elems.length == 0)) {
      /* 513 */return (ExplorerTreeNode[]) this.children
        .toArray(new ExplorerTreeNode[0]);
      /*     */}
    /*     */
    /* 516 */List allKids = new ArrayList();
    /* 517 */for (ExplorerTreeNode kid : this.children) {
      /* 518 */for (ElementNode e : elems)
      /*     */{
        /* 520 */String elementPathName = getElementPath(e);
        /* 521 */String newId = kid.getId() + "_" + e.getElement().getId()
          + elementPathName;
        /* 522 */String newName = kid.getName() + " (" + elementPathName + ")";
        /* 523 */Object o = kid.getParameterValue("Element");
        /* 524 */kid.setParameter("Element", getParameterValue("Element"));
        /* 525 */kid.addParameterValue("Element", o);
        /* 526 */kid.addParameterValue("Element", e.getElement());
        /*     */ExplorerTreeNode n;
        /* 527 */if (kid instanceof FolderElement) {
          /* 528 */n = new FolderElement(newId, newName, this, (FolderElement) kid);
          /*     */}
        /*     */else
        /*     */{
          /* 529 */if (kid instanceof StaticFolder)
            /* 530 */n = new StaticFolder(newId, newName, this, (StaticFolder) kid);
          /*     */else
            /* 532 */n = new DynamicFolder(newId, newName, this,
              (DynamicFolder) kid);
          /*     */}
        /* 534 */kid.setParameter("Element", o);
        /* 535 */allKids.add(n);
        /*     */}
      /*     */}
    /*     */
    /* 539 */return (ExplorerTreeNode[]) allKids.toArray(new ExplorerTreeNode[0]);
    /*     */}

  /*     */
  /*     */void setParentDynamicFolder(DynamicFolder df)
  /*     */{
    /* 626 */this.parentDynamicFolder = df;
    /*     */}

  /*     */
  /*     */public DynamicFolder getParentDynamicFolder() {
    /* 630 */return this.parentDynamicFolder;
    /*     */}

  /*     */
  /*     *//** @deprecated */
  /*     */public final int getChildIndex(ExplorerTreeNode child)
  /*     */{
    /* 651 */return -1;
    /*     */}

  /*     */
  /*     */private final String tag(String tagName, String value)
  /*     */{
    /* 731 */return tagName + "=\"" + value + "\" ";
    /*     */}

  /*     */
  /*     */public String getPersistenceString()
  /*     */{
    /* 738 */StringBuffer result = new StringBuffer("<dynamicFolder ");
    /* 739 */if (getId() != null) {
      /* 740 */result.append("id=\"");
      /* 741 */result.append(getId());
      /* 742 */result.append("\" ");
      /*     */}
    /* 744 */if (this.name != null) {
      /* 745 */result.append("name=\"");
      /* 746 */result.append(modify(this.name));
      /* 747 */result.append("\" ");
      /*     */}
    /*     */
    /* 755 */if (this.sourceHierarchy != null) {
      /* 756 */Connection con = this.sourceHierarchy.getDimension().getDatabase()
        .getConnection();
      /* 757 */result.append(tag("connectionServer", con.getServer()));
      /* 758 */result.append(tag("connectionService", con.getService()));
      /* 759 */result.append(tag("databaseId", this.sourceHierarchy.getDimension()
        .getDatabase().getId()));
      /* 760 */result.append(tag("dimensionId", this.sourceHierarchy.getDimension()
        .getId()));
      /* 761 */result.append(tag("hierarchyId", this.sourceHierarchy.getId()));
      /*     */}
    /* 763 */if (this.sourceSubset != null) {
      /* 764 */result.append("subset=\"");
      /* 765 */result.append(this.sourceSubset.getDimHierarchy().getDimension()
        .getId());
      /* 766 */result.append("@_@");
      /* 767 */result.append(this.sourceSubset.getDimHierarchy().getId());
      /* 768 */result.append("@_@");
      /* 769 */result.append(this.sourceSubset.getId());
      /* 770 */result.append("@_@");
      /* 771 */result.append(this.sourceSubset.getType());
      /* 772 */result.append("\" ");
      /*     */}
    /* 774 */result.append(">\n");
    /* 775 */for (ExplorerTreeNode kid : this.children) {
      /* 776 */result.append(kid.getPersistenceString());
      /*     */}
    /* 778 */result.append("</dynamicFolder>\n");
    /* 779 */return result.toString();
    /*     */}

  /*     */
  /*     */public Hierarchy setSourceHierarchy(Hierarchy sourceHierarchy)
  /*     */{
    /* 790 */Hierarchy old = this.sourceHierarchy;
    /* 791 */this.sourceHierarchy = sourceHierarchy;
    /* 792 */return old;
    /*     */}

  /*     */
  /*     */public Subset2 setSourceSubset(Subset2 sourceSubset) {
    /* 796 */Subset2 old = this.sourceSubset;
    /* 797 */this.sourceSubset = sourceSubset;
    /* 798 */return old;
    /*     */}

  /*     */
  /*     */public int getType() {
    /* 802 */return 1;
    /*     */}
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi
 * .jar Qualified Name: org.palo.viewapi.internal.DynamicFolder JD-Core Version:
 * 0.5.4
 */