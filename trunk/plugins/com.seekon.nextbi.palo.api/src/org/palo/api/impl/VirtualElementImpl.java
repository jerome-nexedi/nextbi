/*     */package org.palo.api.impl;

/*     */
/*     */import com.tensegrity.palojava.ElementInfo; /*     */
import java.util.ArrayList; /*     */
import java.util.List; /*     */
import org.palo.api.Attribute; /*     */
import org.palo.api.Consolidation; /*     */
import org.palo.api.Dimension; /*     */
import org.palo.api.Element; /*     */
import org.palo.api.Hierarchy;

/*     */
/*     */class VirtualElementImpl
/*     */implements Element
/*     */{
  /*     */private final Element sourceElement;

  /*     */private final boolean isFlat;

  /*     */private final VirtualDimensionImpl vdim;

  /*     */private final List parents;

  /*     */private final List children;

  /*     */private final List consolidations;

  /*     */
  /*     */VirtualElementImpl(VirtualDimensionImpl vdim, boolean isFlat,
    Element sourceElement)
  /*     */{
    /* 67 */this.vdim = vdim;
    /* 68 */this.isFlat = isFlat;
    /* 69 */this.sourceElement = sourceElement;
    /* 70 */this.parents = new ArrayList();
    /* 71 */this.children = new ArrayList();
    /* 72 */this.consolidations = new ArrayList();
    /*     */}

  /*     */
  /*     */Element getSourceElement()
  /*     */{
    /* 77 */return this.sourceElement;
    /*     */}

  /*     */
  /*     */public String getId()
  /*     */{
    /* 82 */return this.sourceElement.getId();
    /*     */}

  /*     */
  /*     */public String getName()
  /*     */{
    /* 87 */return this.sourceElement.getName();
    /*     */}

  /*     */
  /*     */public Dimension getDimension()
  /*     */{
    /* 92 */return this.vdim;
    /*     */}

  /*     */
  /*     */public Hierarchy getHierarchy()
  /*     */{
    /* 98 */return this.sourceElement.getHierarchy();
    /*     */}

  /*     */
  /*     */public int getType()
  /*     */{
    /* 103 */return this.sourceElement.getType();
    /*     */}

  /*     */
  /*     */public void setType(int type)
  /*     */{
    /* 108 */Util.noopWarning();
    /*     */}

  /*     */
  /*     */public String getTypeAsString()
  /*     */{
    /* 113 */return this.sourceElement.getTypeAsString();
    /*     */}

  /*     */
  /*     */public int getDepth()
  /*     */{
    /* 118 */return this.sourceElement.getDepth();
    /*     */}

  /*     */
  /*     */public int getLevel() {
    /* 122 */return this.sourceElement.getLevel();
    /*     */}

  /*     */
  /*     */public final int getPosition()
  /*     */{
    /* 130 */return this.sourceElement.getPosition();
    /*     */}

  /*     */
  /*     */public void rename(String name)
  /*     */{
    /* 136 */Util.noopWarning();
    /*     */}

  /*     */
  /*     */public int getConsolidationCount()
  /*     */{
    /* 141 */return (this.isFlat) ? 0 : this.consolidations.size();
    /*     */}

  /*     */
  /*     */public Consolidation getConsolidationAt(int index)
  /*     */{
    /* 147 */if (this.isFlat) {
      /* 148 */return null;
      /*     */}
    /* 150 */return (Consolidation) this.consolidations.get(index);
    /*     */}

  /*     */
  /*     */public Consolidation[] getConsolidations()
  /*     */{
    /* 159 */if (this.isFlat)
      /* 160 */return null;
    /* 161 */return (Consolidation[]) this.consolidations.toArray(
    /* 162 */new Consolidation[this.consolidations.size()]);
    /*     */}

  /*     */
  /*     */public void updateConsolidations(Consolidation[] consolidations)
  /*     */{
    /* 180 */Util.noopWarning();
    /*     */}

  /*     */
  /*     */final void addConsolidation(Consolidation consolidation)
  /*     */{
    /* 203 */if ((consolidation == null)
      || (this.consolidations.contains(consolidation)))
      /* 204 */return;
    /* 205 */this.consolidations.add(consolidation);
    /* 206 */Element parent = consolidation.getParent();
    /* 207 */if (parent != this)
      /* 208 */return;
    /* 209 */Element child = consolidation.getChild();
    /* 210 */if (!(child instanceof VirtualElementImpl))
      /* 211 */return;
    /* 212 */VirtualElementImpl vElement = (VirtualElementImpl) child;
    /* 213 */this.children.add(vElement);
    /* 214 */vElement.addParent(this);
    /*     */}

  /*     */
  /*     */final void addParent(Element parent) {
    /* 218 */this.parents.add(parent);
    /*     */}

  /*     */
  /*     */public int getParentCount()
  /*     */{
    /* 223 */return (this.isFlat) ? 0 : this.parents.size();
    /*     */}

  /*     */
  /*     */public Element[] getParents()
  /*     */{
    /* 229 */if (this.isFlat)
      /* 230 */return null;
    /* 231 */return (Element[]) this.parents.toArray(new Element[this.parents
      .size()]);
    /*     */}

  /*     */
  /*     */public int getChildCount()
  /*     */{
    /* 243 */return (this.isFlat) ? 0 : this.children.size();
    /*     */}

  /*     */
  /*     */public Element[] getChildren()
  /*     */{
    /* 249 */if (this.isFlat)
      /* 250 */return null;
    /* 251 */return (Element[]) this.children.toArray(new Element[this.children
      .size()]);
    /*     */}

  /*     */
  /*     */public Object getAttributeValue(Attribute attribute)
  /*     */{
    /* 270 */return this.sourceElement.getAttributeValue(attribute);
    /*     */}

  /*     */
  /*     */public Object[] getAttributeValues()
  /*     */{
    /* 275 */return this.sourceElement.getAttributeValues();
    /*     */}

  /*     */
  /*     */public void setAttributeValue(Attribute attribute, Object value) {
    /* 279 */this.sourceElement.setAttributeValue(attribute, value);
    /*     */}

  /*     */
  /*     */public void setAttributeValues(Attribute[] attributes, Object[] values) {
    /* 283 */this.sourceElement.setAttributeValues(attributes, values);
    /*     */}

  /*     */
  /*     */public final ElementInfo getInfo() {
    /* 287 */return ((ElementImpl) this.sourceElement).getInfo();
    /*     */}

  /*     */
  /*     */public boolean equals(Object obj) {
    /* 291 */if (obj instanceof VirtualElementImpl)
      /* 292 */return this.sourceElement
        .equals(((VirtualElementImpl) obj).sourceElement);
    /* 293 */if (obj instanceof ElementImpl) {
      /* 294 */return this.sourceElement.equals(obj);
      /*     */}
    /* 296 */return false;
    /*     */}

  /*     */
  /*     */public boolean canBeModified() {
    /* 300 */return true;
    /*     */}

  /*     */
  /*     */public boolean canCreateChildren() {
    /* 304 */return true;
    /*     */}

  /*     */
  /*     */public final void move(int newPosition) {
    /* 308 */this.sourceElement.move(newPosition);
    /*     */}
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name: org.palo.api.impl.VirtualElementImpl JD-Core Version: 0.5.4
 */