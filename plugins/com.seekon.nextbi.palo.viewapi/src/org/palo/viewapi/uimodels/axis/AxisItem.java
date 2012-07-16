/*     */package org.palo.viewapi.uimodels.axis;

/*     */
/*     */import java.util.ArrayList; /*     */
import java.util.Collection; /*     */
import java.util.HashMap; /*     */
import java.util.LinkedHashSet; /*     */
import java.util.Set; /*     */
import org.palo.api.Element; /*     */
import org.palo.api.ElementNode; /*     */
import org.palo.api.Hierarchy; /*     */
import org.palo.viewapi.Property;

/*     */
/*     */public class AxisItem
/*     */{
  /*     */public static final String PROPERTY_COLUMN_WIDTH = "com.tensegrity.palo.axisitem.columnwidth";

  /*     */public static final int EXPANDED = 2;

  /*     */public static final int CACHED = 4;

  /* 68 */public int index = -1;

  /*     */
  /* 70 */public int leafIndex = -1;

  /*     */private final ElementNode node;

  /* 75 */private int state = 0;

  /* 76 */private int level = 0;

  /*     */private AxisItem parent;

  /*     */private AxisItem parentInPrevHier;

  /* 80 */private final Set<AxisItem> children = new LinkedHashSet();

  /* 81 */private final Set<AxisItem> rootsInNextHier = new LinkedHashSet();

  /*     */private final HashMap<String, Property<?>> properties;

  /*     */
  /*     */public AxisItem(ElementNode node, int level)
  /*     */{
    /* 85 */this.node = node;
    /* 86 */this.level = level;
    /*     */
    /* 88 */this.properties = new HashMap();
    /* 89 */addProperty(new Property("com.tensegrity.palo.axisitem.columnwidth",
      new Double(100.0D)));
    /*     */}

  /*     */
  /*     */public final Element getElement() {
    /* 93 */return this.node.getElement();
    /*     */}

  /*     */public final ElementNode getElementNode() {
    /* 96 */return this.node;
    /*     */}

  /*     */
  /*     */public final String getName() {
    /* 100 */return this.node.getName();
    /*     */}

  /*     */
  /*     */public final Hierarchy getHierarchy() {
    /* 104 */return getElement().getHierarchy();
    /*     */}

  /*     */
  /*     */public final int getLevel() {
    /* 108 */return this.level;
    /*     */}

  /*     */
  /*     */public final String getPath()
  /*     */{
    /* 118 */StringBuffer path = new StringBuffer();
    /* 119 */if ((this.parentInPrevHier != null) && (this.parent == null)) {
      /* 120 */path.append(this.parentInPrevHier.getPath());
      /* 121 */path.append(":");
      /*     */}
    /* 123 */if (this.parent != null) {
      /* 124 */path.append(this.parent.getPath());
      /* 125 */path.append(",");
      /*     */}
    /* 127 */path.append(getElement().getId());
    /* 128 */return path.toString();
    /*     */}

  /*     */
  /*     */public final void setParent(AxisItem parent)
  /*     */{
    /* 133 */this.parent = parent;
    /*     */}

  /*     */public final AxisItem getParent() {
    /* 136 */return this.parent;
    /*     */}

  /*     */
  /*     */public final void setParentInPreviousHierarchy(AxisItem parentInPrevHier) {
    /* 140 */this.parentInPrevHier = parentInPrevHier;
    /*     */}

  /*     */public final AxisItem getParentInPrevHierarchy() {
    /* 143 */return this.parentInPrevHier;
    /*     */}

  /*     */
  /*     */public final boolean hasChildren()
  /*     */{
    /* 148 */return getEstimatedChildrenCount() > 0;
    /*     */}

  /*     */public final void addChild(AxisItem item) {
    /* 151 */this.children.add(item);
    /*     */}

  /*     */public final void removeChild(AxisItem item) {
    /* 154 */if (this.children != null)
      /* 155 */this.children.remove(item);
    /*     */}

  /*     */
  /*     */public final AxisItem[] getChildren() {
    /* 158 */return (AxisItem[]) this.children.toArray(new AxisItem[0]);
    /*     */}

  /*     */public final void removeChildren() {
    /* 161 */this.children.clear();
    /* 162 */deleteState(4);
    /*     */}

  /*     */public final int getEstimatedChildrenCount() {
    /* 165 */return this.node.getChildren().length;
    /*     */}

  /*     */
  /*     */public final AxisItem[] getRootsInNextHierarchy()
  /*     */{
    /* 171 */return (AxisItem[]) this.rootsInNextHier.toArray(new AxisItem[0]);
    /*     */}

  /*     */public final void addRootInNextHierarchy(AxisItem item) {
    /* 174 */this.rootsInNextHier.add(item);
    /*     */}

  /*     */
  /*     */public final void replaceRootInNextHierarchy(AxisItem item,
    AxisItem[] replacement) {
    /* 178 */ArrayList tmp = new ArrayList(this.rootsInNextHier);
    /* 179 */int index = tmp.indexOf(item);
    /* 180 */for (int i = 0; i < replacement.length; ++i)
      /* 181 */tmp.add(index + i, replacement[i]);
    /* 182 */tmp.remove(item);
    /* 183 */this.rootsInNextHier.clear();
    /* 184 */this.rootsInNextHier.addAll(tmp);
    /*     */}

  /*     */public final void setRootsInNextHierarchy(AxisItem[] roots) {
    /* 187 */this.rootsInNextHier.clear();
    /* 188 */for (AxisItem r : roots)
      /* 189 */this.rootsInNextHier.add(r);
    /*     */}

  /*     */
  /*     */public final void removeRootInNextHierarchy(AxisItem item)
  /*     */{
    /* 194 */this.rootsInNextHier.remove(item);
    /*     */}

  /*     */public final void removeAllRootsInNextHierarchy() {
    /* 197 */this.rootsInNextHier.clear();
    /*     */}

  /*     */public final boolean hasRootsInNextHierarchy() {
    /* 200 */return !this.rootsInNextHier.isEmpty();
    /*     */}

  /*     */
  /*     */public final boolean update()
  /*     */{
    /* 210 */if (hasState(4)) {
      /* 211 */return false;
      /*     */}
    /* 213 */setState(4);
    /*     */
    /* 215 */ElementNode[] children = this.node.getChildren();
    /* 216 */int childIndex = this.index;
    /* 217 */for (ElementNode child : children) {
      /* 218 */AxisItem _child = new AxisItem(child, this.level + 1);
      /* 219 */_child.index = (++childIndex);
      /* 220 */_child.setParent(this);
      /* 221 */addChild(_child);
      /*     */}
    /* 223 */return true;
    /*     */}

  /*     */
  /*     */public final String toString()
  /*     */{
    /* 233 */StringBuffer str = new StringBuffer();
    /* 234 */str.append("AxisItem(\"");
    /* 235 */str.append(getName());
    /* 236 */str.append("\")[");
    /* 237 */str.append(super.hashCode());
    /* 238 */str.append("]");
    /* 239 */return str.toString();
    /*     */}

  /*     */
  /*     */public final void setState(int stateBit)
  /*     */{
    /* 244 */this.state |= stateBit;
    /*     */}

  /*     */public final void deleteState(int stateBit) {
    /* 247 */this.state &= (stateBit ^ 0xFFFFFFFF);
    /*     */}

  /*     */public final boolean hasState(int stateBit) {
    /* 250 */return (this.state & stateBit) >= stateBit;
    /*     */}

  /*     */
  /*     */public final AxisItem copy() {
    /* 254 */AxisItem copy = new AxisItem(this.node, this.level);
    /* 255 */copy.index = this.index;
    /* 256 */copy.state = this.state;
    /* 257 */copy.parent = ((this.parent != null) ? this.parent.copy() : null);
    /* 258 */return copy;
    /*     */}

  /*     */
  /*     */public final void addProperty(Property<?> property) {
    /* 262 */this.properties.put(property.getId(), property);
    /*     */}

  /*     */public final void removeProperty(Property<?> property) {
    /* 265 */this.properties.remove(property.getId());
    /*     */}

  /*     */public final Property<?>[] getProperties() {
    /* 268 */return (Property[]) this.properties.values().toArray(new Property[0]);
    /*     */}

  /*     */public final String[] getPropertyIDs() {
    /* 271 */return (String[]) this.properties.keySet().toArray(new String[0]);
    /*     */}

  /*     */public final Property<?> getProperty(String id) {
    /* 274 */return (Property) this.properties.get(id);
    /*     */}

  /*     */
  /*     */public final boolean equals(Object obj)
  /*     */{
    /* 279 */if (obj instanceof AxisItem) {
      /* 280 */AxisItem other = (AxisItem) obj;
      /* 281 */return (getPath().equals(other.getPath()))
        && (this.index == other.index);
      /*     */}
    /* 283 */return false;
    /*     */}
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi
 * .jar Qualified Name: org.palo.viewapi.uimodels.axis.AxisItem JD-Core Version:
 * 0.5.4
 */