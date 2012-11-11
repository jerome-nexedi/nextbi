/*     */package org.palo.viewapi.internal.cubeview;

/*     */
/*     */import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.palo.api.Attribute;
import org.palo.api.Element;
import org.palo.api.ElementNode;
import org.palo.api.Hierarchy;
import org.palo.api.subsets.Subset2;
import org.palo.viewapi.LocalFilter;
import org.palo.viewapi.Property;

/*     */
/*     */class AxisHierarchyFilter
/*     */{
  /*     */private Subset2 subset;

  /*     */private Attribute alias;

  /*     */private LocalFilter localFilter;

  /*     */private final Hierarchy hierarchy;

  /*     */
  /*     */public AxisHierarchyFilter(Hierarchy hierarchy)
  /*     */{
    /* 66 */this.hierarchy = hierarchy;
    /*     */}

  /*     */
  /*     */public final void setAlias(Attribute alias) {
    /* 70 */this.alias = alias;
    /*     */}

  /*     */
  /*     */public final Subset2 getSubset() {
    /* 74 */return this.subset;
    /*     */}

  /*     */public final void setSubset(Subset2 subset) {
    /* 77 */this.subset = subset;
    /*     */}

  /*     */
  /*     */public final LocalFilter getLocalFilter() {
    /* 81 */return this.localFilter;
    /*     */}

  /*     */public final void setLocalFilter(LocalFilter localFilter) {
    /* 84 */this.localFilter = localFilter;
    /*     */}

  /*     */
  /*     */public final boolean contains(Element element) {
    /* 88 */if (useLocalFilter())
      /* 89 */return this.localFilter.isVisible(element);
    /* 90 */if (useSubset())
      /* 91 */return this.subset.contains(element);
    /* 92 */return this.hierarchy.getElementById(element.getId()) != null;
    /*     */}

  /*     */
  /*     */public final List<ElementNode> applyFilters(Property aliasFormat) {
    /* 96 */List roots = new ArrayList();
    /* 97 */applySubset(roots);
    /* 98 */applyLocalFilter(roots);
    /* 99 */String af = null;
    /* 100 */if ((aliasFormat != null) && (aliasFormat.getValue() != null)) {
      /* 101 */af = aliasFormat.getValue().toString();
      /*     */}
    /* 103 */applyAlias(roots, af);
    /* 104 */return roots;
    /*     */}

  /*     */
  /*     */private List<ElementNode> hierarchyRoots() {
    /* 108 */return Arrays.asList(this.hierarchy.getElementsTree());
    /*     */}

  /*     */
  /*     */private final void applySubset(List<ElementNode> roots) {
    /* 112 */if (useSubset()) {
      /* 113 */roots.addAll(Arrays.asList(this.subset.getRootNodes()));
      /*     */}
    /*     */else
      /* 116 */roots.addAll(hierarchyRoots());
    /*     */}

  /*     */
  /*     */private final void applyLocalFilter(List<ElementNode> roots)
  /*     */{
    /* 121 */if (!useLocalFilter())
      /* 122 */return;
    /* 123 */roots.clear();
    /* 124 */roots.addAll(Arrays.asList(this.localFilter.getVisibleElements()));
    /*     */}

  /*     */
  /*     */private final String getAliasFormat(String elementName, String alias,
    String format) {
    /* 128 */String result = alias;
    /* 129 */if (format != null) {
      /* 130 */if (format.equals("aliasFormat"))
        /* 131 */result = alias;
      /* 132 */else if (format.equals("elementName"))
        /* 133 */result = elementName;
      /* 134 */else if (format.equals("elementNameDashAlias"))
        /* 135 */result = elementName + " - " + alias;
      /* 136 */else if (format.equals("aliasDashElementName"))
        /* 137 */result = alias + " - " + elementName;
      /* 138 */else if (format.equals("elementNameParenAlias"))
        /* 139 */result = elementName + " (" + alias + ")";
      /* 140 */else if (format.equals("aliasParenElementName"))
        /* 141 */result = alias + " (" + elementName + ")";
      /* 142 */else if (format.equals("elementNameAlias"))
        /* 143 */result = elementName + " " + alias;
      /* 144 */else if (format.equals("aliasElementName")) {
        /* 145 */result = alias + " " + elementName;
        /*     */}
      /*     */}
    /* 148 */return result;
    /*     */}

  /*     */
  /*     */private final void applyAlias(List<ElementNode> roots,
    final String aliasFormat) {
    /* 152 */ElementNodeVisitor visitor = new ElementNodeVisitor() {
      /*     */public ElementNode visit(ElementNode node, ElementNode parent) {
        /* 154 */String alias = AxisHierarchyFilter.this.getAliasFor(node);
        /* 155 */if ((alias != null) && (alias.length() != 0))
          /* 156 */node.setName(AxisHierarchyFilter.this.getAliasFormat(node
            .getElement().getName(), alias, aliasFormat));
        /*     */else
          /* 158 */node.setName(node.getElement().getName());
        /* 159 */return node;
        /*     */}
      /*     */
    };
    /* 162 */for (ElementNode root : roots)
      /* 163 */traverse(root, null, visitor);
    /*     */}

  /*     */
  /*     */private final String getAliasFor(ElementNode node) {
    /* 167 */if (useAlias()) {
      /* 168 */Object value = this.alias.getValue(node.getElement());
      /* 169 */if (value != null)
        /* 170 */return value.toString();
      /*     */}
    /* 172 */return null;
    /*     */}

  /*     */final String getAliasFor(String elementId) {
    /* 175 */Element elem = this.hierarchy.getElementById(elementId);
    /* 176 */if (elem == null) {
      /* 177 */return null;
      /*     */}
    /* 179 */if (!useAlias()) {
      /* 180 */return elem.getName();
      /*     */}
    /* 182 */Object value = this.alias.getValue(elem);
    /* 183 */if (value != null) {
      /* 184 */return value.toString();
      /*     */}
    /* 186 */return null;
    /*     */}

  /*     */
  /*     */private final boolean useAlias() {
    /* 190 */return this.alias != null;
    /*     */}

  /*     */private final boolean useLocalFilter() {
    /* 193 */return (this.localFilter != null)
      && (this.localFilter.hasVisibleElements());
    /*     */}

  /*     */
  /*     */private final boolean useSubset() {
    /* 197 */return this.subset != null;
    /*     */}

  /*     */
  /*     */private final void traverse(ElementNode node, ElementNode parent,
    ElementNodeVisitor visitor)
  /*     */{
    /* 202 */ElementNode newNode = visitor.visit(node, parent);
    /* 203 */ElementNode[] children = node.getChildren();
    /* 204 */if (children == null)
      /* 205 */return;
    /* 206 */for (ElementNode child : children)
      /* 207 */traverse(child, newNode, visitor);
    /*     */}
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi
 * .jar Qualified Name: org.palo.viewapi.internal.cubeview.AxisHierarchyFilter
 * JD-Core Version: 0.5.4
 */