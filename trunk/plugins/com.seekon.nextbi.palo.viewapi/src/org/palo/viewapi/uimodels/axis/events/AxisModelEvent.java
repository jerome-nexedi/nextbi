/*    */package org.palo.viewapi.uimodels.axis.events;

/*    */
/*    */import org.palo.viewapi.uimodels.axis.AxisItem;

/*    */
/*    */public class AxisModelEvent
/*    */{
  /* 53 */public boolean doit = true;

  /*    */private final AxisItem source;

  /*    */private AxisItem[][] items;

  /*    */
  /*    */public AxisModelEvent(AxisItem source)
  /*    */{
    /* 73 */this.source = source;
    /*    */}

  /*    */
  /*    */public final AxisItem getSource()
  /*    */{
    /* 81 */return this.source;
    /*    */}

  /*    */
  /*    */public final AxisItem[][] getItems()
  /*    */{
    /* 89 */return (this.items == null) ? new AxisItem[0][] : this.items;
    /*    */}

  /*    */
  /*    */public final void setItems(AxisItem[][] items)
  /*    */{
    /* 97 */this.items = ((items == null) ? new AxisItem[0][]
      : (AxisItem[][]) items.clone());
    /*    */}
  /*    */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi
 * .jar Qualified Name: org.palo.viewapi.uimodels.axis.events.AxisModelEvent
 * JD-Core Version: 0.5.4
 */