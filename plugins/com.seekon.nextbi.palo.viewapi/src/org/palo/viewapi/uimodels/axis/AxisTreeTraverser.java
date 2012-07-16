/*     */package org.palo.viewapi.uimodels.axis;

/*     */
/*     */import org.palo.api.Element; /*     */
import org.palo.api.ElementNode; /*     */
import org.palo.api.Hierarchy; /*     */
import org.palo.api.subsets.Subset2; /*     */
import org.palo.viewapi.Axis; /*     */
import org.palo.viewapi.AxisHierarchy;

/*     */
/*     */class AxisTreeTraverser
/*     */{
  /*     */private final Axis axis;

  /*     */
  /*     */AxisTreeTraverser(Axis axis)
  /*     */{
    /* 72 */this.axis = axis;
    /*     */}

  /*     */
  /*     */public final void hierarchiesFirst(AxisItem item, AxisTreeVisitor visitor)
  /*     */{
    /* 78 */if (visitor.visit(item)) {
      /* 79 */AxisItem[] roots = item.getRootsInNextHierarchy();
      /* 80 */for (AxisItem root : roots) {
        /* 81 */hierarchiesFirst(root, visitor);
        /*     */}
      /*     */}
    /* 84 */if (item.hasChildren()) {
      /* 85 */AxisItem[] children = item.getChildren();
      /* 86 */for (AxisItem child : children)
        /* 87 */hierarchiesFirst(child, visitor);
      /*     */}
    /*     */}

  /*     */
  /*     */public final boolean traverseHierarchiesFirst(AxisItem item,
    AxisTreeVisitor visitor)
  /*     */{
    /* 93 */boolean goOn = visitor.visit(item);
    /*     */
    /* 95 */if (goOn) {
      /* 96 */AxisItem[] roots = item.getRootsInNextHierarchy();
      /* 97 */for (AxisItem root : roots) {
        /* 98 */goOn = traverseHierarchiesFirst(root, visitor);
        /* 99 */if (!goOn) {
          /*     */break;
          /*     */}
        /*     */}
      /*     */}
    /* 104 */if ((goOn) &&
    /* 106 */(item.hasChildren())) {
      /* 107 */AxisItem[] children = item.getChildren();
      /* 108 */for (AxisItem child : children) {
        /* 109 */goOn = traverseHierarchiesFirst(child, visitor);
        /* 110 */if (!goOn) {
          /*     */break;
          /*     */}
        /*     */}
      /*     */}
    /* 115 */return goOn;
    /*     */}

  /*     */
  /*     */public final void createAxisTree(AxisItem item,
    AxisTreeHierarchyVisitor visitor)
  /*     */{
    /* 122 */AxisHierarchy nxtHierarchy = getNextAxisHierarchy(item);
    /* 123 */if (nxtHierarchy == null)
      /*     */return;
    /* 125 */int index = 0;
    /* 126 */ElementNode[] rootNodes = nxtHierarchy.getRootNodes();
    /* 127 */for (ElementNode root : rootNodes) {
      /* 128 */AxisItem child = new AxisItem(root, 0);
      /* 129 */child.index = (index++);
      /* 130 */visitor.visit(child, item);
      /* 131 */createAxisTree(child, visitor);
      /*     */}
    /*     */}

  /*     */
  /*     */public final boolean traverse(AxisItem item, AxisTreeVisitor visitor)
  /*     */{
    /* 149 */boolean goOn = true;
    /* 150 */if (visitor.visit(item)) {
      /* 151 */if (item.hasChildren()) {
        /* 152 */AxisItem[] children = item.getChildren();
        /* 153 */for (AxisItem child : children) {
          /* 154 */goOn = traverse(child, visitor);
          /* 155 */if (!goOn)
            /*     */break;
          /*     */}
        /*     */}
      /* 159 */if (goOn)
      /*     */{
        /* 161 */AxisItem[] roots = item.getRootsInNextHierarchy();
        /* 162 */for (AxisItem root : roots) {
          /* 163 */goOn = traverse(root, visitor);
          /* 164 */if (!goOn)
            return goOn;
          /*     */}
        /*     */}
      /*     */}
    /*     */else {
      /* 169 */goOn = false;
      /* 170 */}
    return goOn;
    /*     */}

  /*     */
  /*     */public final void recreateTree(AxisItem item, AxisItem parentInPrevHier,
    AxisTreeHierarchyVisitor visitor) {
    /* 174 */if (visitor.visit(item, parentInPrevHier)) {
      /* 175 */if (item.hasChildren()) {
        /* 176 */AxisItem[] children = item.getChildren();
        /* 177 */for (AxisItem child : children) {
          /* 178 */recreateTree(child, parentInPrevHier, visitor);
          /*     */}
        /*     */}
      /* 181 */AxisItem[] roots = item.getRootsInNextHierarchy();
      /* 182 */for (AxisItem root : roots)
        /* 183 */recreateTree(root, item, visitor);
      /*     */}
    /*     */}

  /*     */
  /*     */private final Element[] getRoots(Hierarchy hierarchy)
  /*     */{
    /* 212 */AxisHierarchy axisHierarchy = this.axis.getAxisHierarchy(hierarchy);
    /* 213 */Subset2 subset = axisHierarchy.getSubset();
    /*     */Element[] roots;
    /* 215 */if (subset != null)
    /*     */{
      /* 217 */ElementNode[] rootNodes = subset.getHierarchy();
      /* 218 */roots = new Element[rootNodes.length];
      /* 219 */for (int i = 0; i < rootNodes.length; ++i)
        /* 220 */roots[i] = rootNodes[i].getElement();
      /*     */}
    /*     */else {
      /* 223 */roots = hierarchy.getRootElements();
      /*     */}
    /* 225 */return roots;
    /*     */}

  /*     */
  /*     */private final AxisHierarchy getNextAxisHierarchy(AxisItem item)
  /*     */{
    /* 230 */AxisHierarchy[] axisHierarchies = this.axis.getAxisHierarchies();
    /* 231 */Hierarchy itemHierarchy = item.getHierarchy();
    /* 232 */int nextHier = 0;
    /* 233 */for (int i = 0; i < axisHierarchies.length; ++i) {
      /* 234 */nextHier = i + 1;
      /* 235 */if ((axisHierarchies[i].getHierarchy().equals(itemHierarchy)) &&
      /* 236 */(nextHier < axisHierarchies.length)) {
        /* 237 */return axisHierarchies[nextHier];
        /*     */}
      /*     */}
    /* 240 */return null;
    /*     */}

  /*     */
  /*     */static abstract interface AxisTreeHierarchyVisitor
  /*     */{
    /*     */public abstract boolean visit(AxisItem paramAxisItem1,
      AxisItem paramAxisItem2);
    /*     */
  }

  /*     */
  /*     */static abstract interface AxisTreeVisitor
  /*     */{
    /*     */public abstract boolean visit(AxisItem paramAxisItem);
    /*     */
  }
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi
 * .jar Qualified Name: org.palo.viewapi.uimodels.axis.AxisTreeTraverser JD-Core
 * Version: 0.5.4
 */