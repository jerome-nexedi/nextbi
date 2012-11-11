/*     */package org.palo.viewapi.internal;

/*     */
/*     */import org.palo.api.Attribute;
import org.palo.api.Consolidation;
import org.palo.api.Dimension;
import org.palo.api.Element;
import org.palo.api.Hierarchy;
import org.palo.viewapi.VirtualElement;

import com.tensegrity.palojava.ElementInfo;

/*     */
/*     */public class VirtualElementImpl
/*     */implements VirtualElement
/*     */{
  /*     */private String name;

  /*     */private final Hierarchy hierarchy;

  /*     */
  /*     */public VirtualElementImpl(String name, Hierarchy hierarchy)
  /*     */{
    /* 60 */this.name = name;
    /* 61 */this.hierarchy = hierarchy;
    /*     */}

  /*     */
  /*     */public Object getAttributeValue(Attribute attribute) {
    /* 65 */return null;
    /*     */}

  /*     */
  /*     */public Object[] getAttributeValues() {
    /* 69 */return null;
    /*     */}

  /*     */
  /*     */public int getChildCount() {
    /* 73 */return 0;
    /*     */}

  /*     */
  /*     */public Element[] getChildren() {
    /* 77 */return null;
    /*     */}

  /*     */
  /*     */public Consolidation getConsolidationAt(int index) {
    /* 81 */return null;
    /*     */}

  /*     */
  /*     */public int getConsolidationCount() {
    /* 85 */return 0;
    /*     */}

  /*     */
  /*     */public Consolidation[] getConsolidations() {
    /* 89 */return null;
    /*     */}

  /*     */
  /*     */public int getDepth() {
    /* 93 */return 0;
    /*     */}

  /*     */
  /*     */public Dimension getDimension() {
    /* 97 */return this.hierarchy.getDimension();
    /*     */}

  /*     */
  /*     */public Hierarchy getHierarchy() {
    /* 101 */return this.hierarchy;
    /*     */}

  /*     */
  /*     */public int getLevel() {
    /* 105 */return 0;
    /*     */}

  /*     */
  /*     */public String getName() {
    /* 109 */return this.name;
    /*     */}

  /*     */
  /*     */public int getParentCount() {
    /* 113 */return 0;
    /*     */}

  /*     */
  /*     */public Element[] getParents() {
    /* 117 */return null;
    /*     */}

  /*     */
  /*     */public int getPosition() {
    /* 121 */return 0;
    /*     */}

  /*     */
  /*     */public int getType() {
    /* 125 */return 4;
    /*     */}

  /*     */
  /*     */public String getTypeAsString() {
    /* 129 */return "Virtual";
    /*     */}

  /*     */
  /*     */public void move(int newPosition) {
    /*     */}

  /*     */
  /*     */public void rename(String name) {
    /* 136 */this.name = name;
    /*     */}

  /*     */
  /*     */public void setAttributeValue(Attribute attribute, Object value) {
    /*     */}

  /*     */
  /*     */public void setAttributeValues(Attribute[] attributes, Object[] values) {
    /*     */}

  /*     */
  /*     */public void setType(int type) {
    /*     */}

  /*     */
  /*     */public void updateConsolidations(Consolidation[] consolidations) {
    /*     */}

  /*     */
  /*     */public String getId() {
    /* 152 */return "VE:" + Integer.toString(super.hashCode());
    /*     */}

  /*     */
  /*     */public boolean canBeModified() {
    /* 156 */return false;
    /*     */}

  /*     */
  /*     */public boolean canCreateChildren() {
    /* 160 */return false;
    /*     */}

  /*     */
  /*     */public ElementInfo getInfo() {
    /* 164 */return null;
    /*     */}
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi
 * .jar Qualified Name: org.palo.viewapi.internal.VirtualElementImpl JD-Core
 * Version: 0.5.4
 */