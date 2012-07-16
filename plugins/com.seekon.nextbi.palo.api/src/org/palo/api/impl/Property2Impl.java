/*     */package org.palo.api.impl;

/*     */
/*     */import com.tensegrity.palojava.DbConnection; /*     */
import com.tensegrity.palojava.PropertyInfo; /*     */
import java.io.PrintStream; /*     */
import java.util.ArrayList; /*     */
import java.util.LinkedHashSet; /*     */
import org.palo.api.Connection; /*     */
import org.palo.api.Property2;

/*     */
/*     */public class Property2Impl
/*     */implements Property2
/*     */{
  /*     */private PropertyInfo propertyInfo;

  /*     */private Property2 parent;

  /*     */private final LinkedHashSet<Property2> children;

  /*     */
  /*     */static final Property2Impl create(Property2 parent,
    PropertyInfo propertyInfo)
  /*     */{
    /* 56 */return new Property2Impl(parent, propertyInfo);
    /*     */}

  /*     */
  /*     */static final Property2Impl create(Connection con, String id,
    String value, Property2 parent, int type, boolean readOnly)
  /*     */{
    /*     */PropertyInfo par;
    /* 62 */if (parent == null)
      /* 63 */par = null;
    /*     */else {
      /* 65 */par = ((Property2Impl) parent).getPropInfo();
      /*     */}
    /* 67 */PropertyInfo prop = ((ConnectionImpl) con).getConnectionInternal()
    /* 68 */.createNewProperty(id, value, par, type, readOnly);
    /* 69 */return new Property2Impl(parent, prop);
    /*     */}

  /*     */
  /*     */private Property2Impl(Property2 parent, PropertyInfo propertyInfo) {
    /* 73 */this.parent = parent;
    /* 74 */this.propertyInfo = propertyInfo;
    /* 75 */this.children = new LinkedHashSet();
    /*     */}

  /*     */
  /*     */public void addChild(Property2 child)
  /*     */{
    /* 82 */this.children.add(child);
    /*     */}

  /*     */
  /*     */public void clearChildren()
  /*     */{
    /* 89 */if (!this.propertyInfo.isReadOnly())
      /* 90 */this.children.clear();
    /*     */}

  /*     */
  /*     */public int getChildCount()
  /*     */{
    /* 98 */return this.children.size();
    /*     */}

  /*     */
  /*     */public Property2[] getChildren()
  /*     */{
    /* 105 */return (Property2[]) this.children.toArray(new Property2[0]);
    /*     */}

  /*     */
  /*     */public Property2 getParent()
  /*     */{
    /* 112 */return this.parent;
    /*     */}

  /*     */
  /*     */public String getValue()
  /*     */{
    /* 119 */return this.propertyInfo.getValue();
    /*     */}

  /*     */
  /*     */public void removeChild(Property2 child)
  /*     */{
    /* 126 */if (!this.propertyInfo.isReadOnly())
      /* 127 */this.children.remove(child.getId());
    /*     */}

  /*     */
  /*     */public void setValue(String newValue)
  /*     */{
    /* 135 */if (!this.propertyInfo.isReadOnly())
      /* 136 */this.propertyInfo.setValue(newValue);
    /*     */}

  /*     */
  /*     */public String getId()
  /*     */{
    /* 144 */return this.propertyInfo.getId();
    /*     */}

  /*     */
  /*     */public String getName()
  /*     */{
    /* 151 */return this.propertyInfo.getId();
    /*     */}

  /*     */
  /*     */public boolean isReadOnly() {
    /* 155 */return this.propertyInfo.isReadOnly();
    /*     */}

  /*     */
  /*     */PropertyInfo getPropInfo() {
    /* 159 */return this.propertyInfo;
    /*     */}

  /*     */
  /*     */public String getChildValue(String childId) {
    /* 163 */for (Property2 prop : this.children) {
      /* 164 */if (prop.getId().equals(childId)) {
        /* 165 */return prop.getValue();
        /*     */}
      /*     */}
    /* 168 */return "";
    /*     */}

  /*     */
  /*     */public Property2[] getChildren(String childId) {
    /* 172 */ArrayList kids = new ArrayList();
    /* 173 */for (Property2 prop : this.children) {
      /* 174 */if (prop.getId().equals(childId)) {
        /* 175 */kids.add(prop);
        /*     */}
      /*     */}
    /* 178 */return (Property2[]) kids.toArray(new Property2[0]);
    /*     */}

  /*     */
  /*     */final void clearCache() {
    /* 182 */System.err.println("clearCache: nothing todo for Property2...");
    /*     */}

  /*     */
  /*     */public boolean canBeModified() {
    /* 186 */return this.propertyInfo.canBeModified();
    /*     */}

  /*     */
  /*     */public boolean canCreateChildren() {
    /* 190 */return this.propertyInfo.canCreateChildren();
    /*     */}

  /*     */
  /*     */public int getType() {
    /* 194 */return this.propertyInfo.getType();
    /*     */}
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name: org.palo.api.impl.Property2Impl JD-Core Version: 0.5.4
 */