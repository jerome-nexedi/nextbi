/*     */package org.palo.viewapi.uimodels.axis;

/*     */
/*     */import org.palo.api.Element;
import org.palo.api.ElementNode;
import org.palo.api.subsets.Subset2;
import org.palo.viewapi.Axis;

/*     */
/*     */public class AxisModelFilter
/*     */{
  /*     */public AxisModelFilter(Axis axis)
  /*     */{
    /*     */}

  /*     */
  /*     */public final void use(Subset2 subset)
  /*     */{
    /*     */}

  /*     */
  /*     */public final boolean accept(AxisItem item)
  /*     */{
    /* 84 */return accept(item.getElement());
    /*     */}

  /*     */
  /*     */public final boolean accept(Element element)
  /*     */{
    /* 93 */boolean accepted = true;
    /*     */
    /* 98 */return accepted;
    /*     */}

  /*     */
  /*     */public final boolean accept(ElementNode element) {
    /* 102 */boolean accepted = true;
    /*     */
    /* 104 */return accepted;
    /*     */}
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi
 * .jar Qualified Name: org.palo.viewapi.uimodels.axis.AxisModelFilter JD-Core
 * Version: 0.5.4
 */