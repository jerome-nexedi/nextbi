/*     */package org.palo.api.utils;

/*     */
/*     */import java.util.ArrayList; /*     */
import java.util.Arrays; /*     */
import java.util.LinkedHashMap; /*     */
import java.util.Map; /*     */
import java.util.Set; /*     */
import org.palo.api.Dimension; /*     */
import org.palo.api.Element; /*     */
import org.palo.api.Hierarchy; /*     */
import org.palo.api.PaloAPIException;

/*     */
/*     */public class ElementPath
/*     */{
  /*     */public static final String ELEMENT_DELIM = ",";

  /*     */public static final String DIMENSION_DELIM = ":";

  /*     */public static final String DIM_HIER_DELIM = "~~~";

  /* 75 */private final Map<Hierarchy, Element[]> parts = new LinkedHashMap<Hierarchy, Element[]>();

  /*     */
  /*     */public ElementPath()
  /*     */{
    /* 82 */init(null);
    /*     */}

  /*     */
  /*     */public ElementPath(Element[] elements)
  /*     */{
    /* 91 */init(elements);
    /*     */}

  /*     */
  /*     */private ElementPath(ElementPath other)
  /*     */{
    /* 96 */Element[] path = other.getComplete();
    /* 97 */init((Element[]) path.clone());
    /*     */}

  /*     */
  /*     */public final void addPart(Dimension dimension, Element[] path)
  /*     */{
    /* 107 */this.parts.put(dimension.getDefaultHierarchy(), path);
    /*     */}

  /*     */
  /*     */public final void addPart(Hierarchy hierarchy, Element[] path) {
    /* 111 */this.parts.put(hierarchy, path);
    /*     */}

  /*     */
  /*     */public final Element[] getComplete() {
    /* 115 */ArrayList elements = new ArrayList();
    /* 116 */Set<Hierarchy> hierarchies = this.parts.keySet();
    /* 117 */for (Hierarchy hierarchy : hierarchies) {
      /* 118 */Element[] els = (Element[]) this.parts.get(hierarchy);
      /* 119 */elements.addAll(Arrays.asList(els));
      /*     */}
    /* 121 */return (Element[]) elements.toArray(new Element[0]);
    /*     */}

  /*     */
  /*     */public final boolean contains(Dimension dimension)
  /*     */{
    /* 131 */return this.parts.containsKey(dimension);
    /*     */}

  /*     */
  /*     */public final boolean contains(Hierarchy hierarchy) {
    /* 135 */return this.parts.containsKey(hierarchy);
    /*     */}

  /*     */
  /*     */public final Dimension[] getDimensions()
  /*     */{
    /* 144 */Hierarchy[] hiers = getHierarchies();
    /* 145 */Dimension[] result = new Dimension[hiers.length];
    /* 146 */int counter = 0;
    /* 147 */for (Hierarchy hier : hiers) {
      /* 148 */result[(counter++)] = hier.getDimension();
      /*     */}
    /* 150 */return result;
    /*     */}

  /*     */
  /*     */public final Hierarchy[] getHierarchies() {
    /* 154 */Set hierarchies = this.parts.keySet();
    /* 155 */return (Hierarchy[]) hierarchies.toArray(new Hierarchy[hierarchies
      .size()]);
    /*     */}

  /*     */
  /*     */public final Element[] getPart(Dimension dimension)
  /*     */throws PaloAPIException
  /*     */{
    /* 168 */return getPart(dimension.getDefaultHierarchy());
    /*     */}

  /*     */
  /*     */public final Element[] getPart(Hierarchy hierarchy)
    throws PaloAPIException {
    /* 172 */if (!this.parts.containsKey(hierarchy))
      /* 173 */throw new PaloAPIException("Unknown hierarchy '" + hierarchy +
      /* 174 */"' for this element path");
    /* 175 */return (Element[]) this.parts.get(hierarchy);
    /*     */}

  /*     */
  /*     */public final String toString() {
    /* 179 */StringBuffer path = new StringBuffer();
    /* 180 */Hierarchy[] hiers = getHierarchies();
    /* 181 */int lastHier = hiers.length - 1;
    /* 182 */for (int i = 0; i < hiers.length; ++i) {
      /* 183 */appendPart(getPart(hiers[i]), path);
      /* 184 */if (i < lastHier)
        /* 185 */path.append(":");
      /*     */}
    /* 187 */return path.toString();
    /*     */}

  /*     */
  /*     */public ElementPath copy()
  /*     */{
    /* 195 */return new ElementPath(this);
    /*     */}

  /*     */
  /*     */public final boolean equals(Object obj) {
    /* 199 */if (obj instanceof ElementPath) {
      /* 200 */ElementPath other = (ElementPath) obj;
      /* 201 */return getFingerPrint().equals(other.getFingerPrint());
      /*     */}
    /* 203 */return false;
    /*     */}

  /*     */
  /*     */public final int hashCode() {
    /* 207 */return getFingerPrint().hashCode();
    /*     */}

  /*     */
  /*     */public static final ElementPath restore(Dimension[] dimensions,
    String pathStr)
  /*     */{
    /* 220 */Hierarchy[] hiers = new Hierarchy[dimensions.length];
    /* 221 */int i = 0;
    for (int n = dimensions.length; i < n; ++i) {
      /* 222 */hiers[i] = dimensions[i].getDefaultHierarchy();
      /*     */}
    /* 224 */return restore(hiers, pathStr);
    /*     */}

  /*     */
  /*     */public static final ElementPath restore(Hierarchy[] hierarchies,
    String pathStr)
  /*     */{
    /* 229 */ElementPath path = new ElementPath();
    /* 230 */String[] parts = pathStr.split(":");
    /* 231 */for (int i = 0; i < parts.length; ++i) {
      /* 232 */path.addPart(hierarchies[i], parts[i].split(","));
      /*     */}
    /* 234 */return path;
    /*     */}

  /*     */
  /*     */private final void init(Element[] elements)
  /*     */{
    /* 241 */if ((elements != null) && (elements.length > 0)) {
      /* 242 */Hierarchy lastHierarchy = elements[0].getHierarchy();
      /* 243 */ArrayList parts = new ArrayList();
      /* 244 */for (Element element : elements) {
        /* 245 */if (element == null) {
          /*     */continue;
          /*     */}
        /* 248 */if (!element.getHierarchy().equals(lastHierarchy)) {
          /* 249 */addPart(lastHierarchy, (Element[]) parts.toArray(new Element[0]));
          /* 250 */lastHierarchy = element.getHierarchy();
          /* 251 */parts.clear();
          /*     */}
        /* 253 */parts.add(element);
        /*     */}
      /* 255 */if (!parts.isEmpty())
        /* 256 */addPart(lastHierarchy, (Element[]) parts.toArray(new Element[0]));
      /*     */}
    /*     */}

  /*     */
  /*     */private final void addPart(Hierarchy hierarchy, String[] elIDs)
  /*     */throws PaloAPIException
  /*     */{
    /* 269 */Element[] elements = new Element[elIDs.length];
    /* 270 */for (int i = 0; i < elIDs.length; ++i) {
      /* 271 */Element element = hierarchy.getElementById(elIDs[i]);
      /* 272 */Element[] allElements = hierarchy.getElements();
      /* 273 */if (element == null) {
        /* 274 */PaloAPIException exception = new PaloAPIException(
          "Could not find element with id '" +
          /* 275 */elIDs[i] + "' in hierarchy '" + hierarchy.getName() +
          /* 276 */"'!!");
        /* 277 */exception.setData(new Object[] { hierarchy, elIDs[i] });
        /* 278 */throw exception;
        /*     */}
      /* 280 */elements[i] = element;
      /*     */}
    /* 282 */this.parts.put(hierarchy, elements);
    /*     */}

  /*     */
  /*     */private final void appendPart(Element[] elements, StringBuffer path)
  /*     */{
    /* 287 */int lastElement = elements.length - 1;
    /* 288 */for (int i = 0; i < elements.length; ++i)
      /* 289 */if (elements[i] != null) {
        /* 290 */path.append(elements[i].getId());
        /* 291 */if (i < lastElement)
          /* 292 */path.append(",");
        /*     */}
    /*     */}

  /*     */
  /*     */private final String getFingerPrint()
  /*     */{
    /* 298 */StringBuffer str = new StringBuffer();
    /* 299 */Hierarchy[] hierarchies = getHierarchies();
    /* 300 */int lastHier = hierarchies.length - 1;
    /* 301 */for (int i = 0; i < hierarchies.length; ++i) {
      /* 302 */str.append("[");
      /* 303 */str.append(hierarchies[i].getId());
      /* 304 */str.append("]");
      /* 305 */Element[] elements = getPart(hierarchies[i]);
      /* 306 */int lastElement = elements.length - 1;
      /* 307 */for (int e = 0; e < elements.length; ++e) {
        /* 308 */str.append(elements[e].getId());
        /* 309 */if (e < lastElement)
          /* 310 */str.append(",");
        /*     */}
      /* 312 */if (i < lastHier)
        /* 313 */str.append(":");
      /*     */}
    /* 315 */return str.toString();
    /*     */}
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name: org.palo.api.utils.ElementPath JD-Core Version: 0.5.4
 */