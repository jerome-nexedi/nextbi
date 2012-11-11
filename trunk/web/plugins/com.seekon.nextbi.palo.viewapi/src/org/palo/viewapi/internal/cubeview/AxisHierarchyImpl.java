/*     */package org.palo.viewapi.internal.cubeview;

/*     */
/*     */import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;

import org.palo.api.Attribute;
import org.palo.api.Element;
import org.palo.api.ElementNode;
import org.palo.api.Hierarchy;
import org.palo.api.subsets.Subset2;
import org.palo.viewapi.Axis;
import org.palo.viewapi.AxisHierarchy;
import org.palo.viewapi.LocalFilter;
import org.palo.viewapi.Property;

/*     */
/*     */class AxisHierarchyImpl
/*     */implements AxisHierarchy
/*     */{
  /*     */private Axis axis;

  /*     */private final Hierarchy hierarchy;

  /*     */private final LinkedHashSet<Element> selectedElements;

  /*     */private final HashMap<String, Property<?>> properties;

  /*     */private final AxisHierarchyFilter filter;

  /* 65 */private String subsetMissing = null;

  /* 66 */private String aliasMissing = null;

  /*     */
  /*     */AxisHierarchyImpl(Hierarchy hierarchy, Axis axis) {
    /* 69 */this.axis = axis;
    /* 70 */this.hierarchy = hierarchy;
    /* 71 */this.filter = new AxisHierarchyFilter(hierarchy);
    /* 72 */this.properties = new HashMap();
    /* 73 */this.selectedElements = new LinkedHashSet();
    /*     */}

  /*     */
  /*     */public final Axis getAxis() {
    /* 77 */return this.axis;
    /*     */}

  /*     */
  /*     */public final void setAxis(Axis axis) {
    /* 81 */this.axis = axis;
    /*     */}

  /*     */
  /*     */public final void addProperty(Property<?> property) {
    /* 85 */if (property.getId().equals("com.tensegrity.palo.axis.use_alias"))
      /* 86 */this.filter.setAlias((Attribute) property.getValue());
    /* 87 */this.properties.put(property.getId(), property);
    /*     */}

  /*     */
  /*     */public final void addSelectedElement(Element element) {
    /* 91 */this.selectedElements.add(element);
    /*     */}

  /*     */
  /*     */public final Hierarchy getHierarchy() {
    /* 95 */return this.hierarchy;
    /*     */}

  /*     */
  /*     */public final Property<?> getProperty(String id) {
    /* 99 */return (Property) this.properties.get(id);
    /*     */}

  /*     */
  /*     */public final Property<?>[] getProperties() {
    /* 103 */return (Property[]) this.properties.values().toArray(new Property[0]);
    /*     */}

  /*     */
  /*     */public final Element[] getSelectedElements() {
    /* 107 */return (Element[]) this.selectedElements.toArray(new Element[0]);
    /*     */}

  /*     */
  /*     */public final Subset2 getSubset() {
    /* 111 */return this.filter.getSubset();
    /*     */}

  /*     */
  /*     */public final boolean hasSelectedElements() {
    /* 115 */if (this.selectedElements.isEmpty()) {
      /* 116 */return false;
      /*     */}
    /* 118 */for (Element e : this.selectedElements) {
      /* 119 */if (e != null) {
        /* 120 */return true;
        /*     */}
      /*     */}
    /* 123 */return false;
    /*     */}

  /*     */
  /*     */public final void removeProperty(Property<?> property) {
    /* 127 */if (property.getId().equals("com.tensegrity.palo.axis.use_alias"))
      /* 128 */this.filter.setAlias(null);
    /* 129 */this.properties.remove(property.getId());
    /*     */}

  /*     */
  /*     */public final void removeSelectedElement(Element element) {
    /* 133 */this.selectedElements.remove(element);
    /*     */}

  /*     */
  /*     */public final void clearSelectedElements() {
    /* 137 */this.selectedElements.clear();
    /*     */}

  /*     */
  /*     */public final void setSubset(Subset2 subset) {
    /* 141 */this.filter.setSubset(subset);
    /*     */}

  /*     */
  /*     */public final ElementNode[] getRootNodes() {
    /* 145 */List roots = this.filter.applyFilters(getProperty("aliasFormat"));
    /* 146 */return (ElementNode[]) roots.toArray(new ElementNode[0]);
    /*     */}

  /*     */
  /*     */public final LocalFilter getLocalFilter() {
    /* 150 */return this.filter.getLocalFilter();
    /*     */}

  /*     */
  /*     */public void setLocalFilter(LocalFilter localFilter) {
    /* 154 */this.filter.setLocalFilter(localFilter);
    /*     */}

  /*     */public final String getElementNameFor(String elementID) {
    /* 157 */return this.filter.getAliasFor(elementID);
    /*     */}

  /*     */public final boolean contains(Element element) {
    /* 160 */return this.filter.contains(element);
    /*     */}

  /*     */
  /*     */public String getSubsetMissing() {
    /* 164 */return this.subsetMissing;
    /*     */}

  /*     */
  /*     */public void setSubsetMissing(String id) {
    /* 168 */this.subsetMissing = id;
    /*     */}

  /*     */
  /*     */public String getAliasMissing() {
    /* 172 */return this.aliasMissing;
    /*     */}

  /*     */
  /*     */public void setAliasMissing(String id) {
    /* 176 */this.aliasMissing = id;
    /*     */}
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi
 * .jar Qualified Name: org.palo.viewapi.internal.cubeview.AxisHierarchyImpl
 * JD-Core Version: 0.5.4
 */