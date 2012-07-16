/*     */package org.palo.api.impl;

/*     */
/*     */import java.util.ArrayList; /*     */
import java.util.Arrays; /*     */
import java.util.List; /*     */
import org.palo.api.Attribute; /*     */
import org.palo.api.ConnectionEvent; /*     */
import org.palo.api.Consolidation; /*     */
import org.palo.api.Cube; /*     */
import org.palo.api.Database; /*     */
import org.palo.api.Dimension; /*     */
import org.palo.api.Element; /*     */
import org.palo.api.Hierarchy; /*     */
import org.palo.api.PaloAPIException;

/*     */
/*     */class AttributeImpl
/*     */implements Attribute
/*     */{
  /*     */private final Cube attrCube;

  /* 74 */private final Element[] coordinates = new Element[2];

  /*     */
  /*     */static final Attribute create(Element attrElement, Cube attrCube)
  /*     */{
    /* 69 */return new AttributeImpl(attrElement, attrCube);
    /*     */}

  /*     */
  /*     */private AttributeImpl(Element attrElement, Cube attrCube)
  /*     */{
    /* 78 */this.attrCube = attrCube;
    /* 79 */this.coordinates[0] = attrElement;
    /*     */}

  /*     */
  /*     */final Element getAttributeElement()
  /*     */{
    /* 89 */return this.coordinates[0];
    /*     */}

  /*     */
  /*     */public final void setChildren(Attribute[] attributes)
  /*     */{
    /* 96 */if (attributes == null) {
      /* 97 */attributes = new Attribute[0];
      /*     */}
    /*     */
    /* 100 */Element attrElement = this.coordinates[0];
    /* 101 */Hierarchy attrHier = attrElement.getHierarchy();
    /* 102 */Consolidation[] consolidations = new Consolidation[attributes.length];
    /* 103 */for (int i = 0; i < consolidations.length; ++i) {
      /* 104 */Element attrChild =
      /* 105 */((AttributeImpl) attributes[i]).getAttributeElement();
      /* 106 */consolidations[i] =
      /* 107 */attrHier.newConsolidation(attrChild, attrElement, 1.0D);
      /*     */}
    /* 109 */attrElement.updateConsolidations(consolidations);
    /* 110 */attrElement.getDimension().reload(false);
    /*     */}

  /*     */
  /*     */public final void removeChildren(Attribute[] attributes) {
    /* 114 */if (attributes == null)
      /* 115 */return;
    /* 116 */List removeChildren = new ArrayList(Arrays.asList(attributes));
    /* 117 */List children = new ArrayList(Arrays.asList(getChildren()));
    /* 118 */if (children.removeAll(removeChildren)) {
      /* 119 */Attribute[] newChildren =
      /* 120 */(Attribute[]) children.toArray(new Attribute[children.size()]);
      /* 121 */setChildren(newChildren);
      /*     */}
    /*     */}

  /*     */
  /*     */public final Attribute[] getChildren()
  /*     */{
    /* 127 */Element attrElement = this.coordinates[0];
    /*     */
    /* 129 */HierarchyImpl attrHier = (HierarchyImpl) attrElement.getHierarchy();
    /*     */
    /* 132 */HierarchyImpl srcHier = (HierarchyImpl) attrHier
      .getAttributeHierarchy();
    /* 133 */if (srcHier == null)
      /* 134 */return new Attribute[0];
    /* 135 */Element[] childElements = attrElement.getChildren();
    /* 136 */Attribute[] attrChildren = new Attribute[childElements.length];
    /* 137 */for (int i = 0; i < childElements.length; ++i) {
      /* 138 */attrChildren[i] = srcHier.getAttribute(childElements[i]);
      /*     */}
    /*     */
    /* 141 */return attrChildren;
    /*     */}

  /*     */
  /*     */public final String getId() {
    /* 145 */return this.coordinates[0].getId();
    /*     */}

  /*     */
  /*     */public final String getName() {
    /* 149 */return getAttributeElement().getName();
    /*     */}

  /*     */
  /*     */public final void setName(String name) {
    /* 153 */((ElementImpl) getAttributeElement()).renameInternal(name, false);
    /*     */
    /* 155 */ConnectionImpl connection = (ConnectionImpl) this.attrCube
      .getDatabase()
      /* 156 */.getConnection();
    /* 157 */connection.fireEvent(
    /* 159 */new ConnectionEvent(connection,
    /* 158 */this, 17,
    /* 159 */new Attribute[] { this }));
    /*     */}

  /*     */
  /*     */public final Attribute[] getParents() {
    /* 163 */DimensionImpl sysDim = (DimensionImpl) this.coordinates[0]
      .getDimension();
    /* 164 */String srcDim = PaloObjects.getLeafName(sysDim.getName());
    /* 165 */Dimension attrDim =
    /* 166 */(DimensionImpl) sysDim.getDatabase().getDimensionByName(srcDim);
    /* 167 */HierarchyImpl attrHier = (HierarchyImpl) attrDim.getDefaultHierarchy();
    /* 168 */Element[] parents = this.coordinates[0].getParents();
    /* 169 */int parentCount = (parents == null) ? 0 : parents.length;
    /* 170 */Attribute[] attrParents = new Attribute[parentCount];
    /* 171 */for (int i = 0; i < parentCount; ++i) {
      /* 172 */attrParents[i] = attrHier.getAttribute(parents[i]);
      /*     */}
    /* 174 */return attrParents;
    /*     */}

  /*     */
  /*     */public final Object getValue(Element element)
  /*     */{
    /* 191 */this.coordinates[1] = element;
    /* 192 */return this.attrCube.getData(this.coordinates);
    /*     */}

  /*     */
  /*     */public final boolean hasChildren() {
    /* 196 */return this.coordinates[0].getChildCount() > 0;
    /*     */}

  /*     */
  /*     */public final void setValue(Element element, Object value) {
    /* 200 */this.coordinates[1] = element;
    /* 201 */this.attrCube.setData(this.coordinates, value);
    /*     */
    /* 203 */ConnectionImpl connection =
    /* 204 */(ConnectionImpl) this.attrCube.getDatabase().getConnection();
    /* 205 */connection.fireEvent(
    /* 208 */new ConnectionEvent(connection, this,
    /* 207 */17,
    /* 208 */new Attribute[] { this }));
    /*     */}

  /*     */
  /*     */public final void setValues(Element[] elements, Object[] values) {
    /* 212 */if (elements.length != values.length)
      /* 213 */throw new PaloAPIException(
        "The number of elements and values has to be equal!");
    /* 214 */Element[][] coords = new Element[elements.length][];
    /* 215 */for (int i = 0; i < coords.length; ++i) {
      /* 216 */coords[i] = new Element[2];
      /* 217 */coords[i][0] = this.coordinates[0];
      /* 218 */coords[i][1] = elements[i];
      /*     */}
    /* 220 */this.attrCube.setDataArray(coords, values, 0);
    /*     */
    /* 222 */ConnectionImpl connection =
    /* 223 */(ConnectionImpl) this.attrCube.getDatabase().getConnection();
    /* 224 */connection.fireEvent(
    /* 227 */new ConnectionEvent(connection, this,
    /* 226 */17,
    /* 227 */new Attribute[] { this }));
    /*     */}

  /*     */
  /*     */public final Object[] getValues(Element[] elements) {
    /* 231 */Element[][] coords = new Element[elements.length][];
    /* 232 */for (int i = 0; i < coords.length; ++i) {
      /* 233 */coords[i] = new Element[2];
      /* 234 */coords[i][0] = this.coordinates[0];
      /* 235 */coords[i][1] = elements[i];
      /*     */}
    /* 237 */return ((CubeImpl) this.attrCube).getDataBulk(coords);
    /*     */}

  /*     */
  /*     */public final int getType() {
    /* 241 */int attrElType = this.coordinates[0].getType();
    /* 242 */switch (attrElType)
    /*     */{
    /*     */case 0:
      /* 243 */
      return 0;
      /*     */case 1:
      /* 244 */
      return 1;
      /*     */
    }
    /* 246 */return -1;
    /*     */}

  /*     */
  /*     */public final void setType(int type) {
    /* 250 */this.coordinates[0].setType(type);
    /*     */}
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name: org.palo.api.impl.AttributeImpl JD-Core Version: 0.5.4
 */