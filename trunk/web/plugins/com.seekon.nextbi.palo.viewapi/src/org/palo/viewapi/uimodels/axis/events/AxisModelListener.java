package org.palo.viewapi.uimodels.axis.events;

public abstract interface AxisModelListener {
  public abstract void willExpand(AxisModelEvent paramAxisModelEvent);

  public abstract void willCollapse(AxisModelEvent paramAxisModelEvent);

  public abstract void expanded(AxisModelEvent paramAxisModelEvent);

  public abstract void collapsed(AxisModelEvent paramAxisModelEvent);

  public abstract void structureChanged(AxisModelEvent paramAxisModelEvent);
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi
 * .jar Qualified Name: org.palo.viewapi.uimodels.axis.events.AxisModelListener
 * JD-Core Version: 0.5.4
 */