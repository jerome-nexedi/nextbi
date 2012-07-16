/*     */package org.palo.viewapi.uimodels.axis;

/*     */
/*     */import java.util.HashSet; /*     */
import org.palo.viewapi.Axis; /*     */
import org.palo.viewapi.AxisHierarchy; /*     */
import org.palo.viewapi.uimodels.axis.events.AxisModelEvent; /*     */
import org.palo.viewapi.uimodels.axis.events.AxisModelListener;

/*     */
/*     */abstract class AbstractAxisModel
/*     */implements AxisModel
/*     */{
  /*     */protected final Axis axis;

  /*     */private final HashSet<AxisModelListener> listeners;

  /*     */
  /*     */AbstractAxisModel(Axis axis)
  /*     */{
    /* 59 */this.axis = axis;
    /* 60 */this.listeners = new HashSet();
    /*     */}

  /*     */
  /*     */public final Axis getAxis() {
    /* 64 */return this.axis;
    /*     */}

  /*     */
  /*     */public void addListener(AxisModelListener listener) {
    /* 68 */this.listeners.add(listener);
    /*     */}

  /*     */public void removeListener(AxisModelListener listener) {
    /* 71 */this.listeners.remove(listener);
    /*     */}

  /*     */
  /*     */protected abstract void init();

  /*     */
  /*     */public final void add(AxisHierarchy hierarchy)
  /*     */{
    /* 80 */this.axis.add(hierarchy);
    /* 81 */init();
    /* 82 */notifyStructureChange();
    /*     */}

  /*     */
  /*     */public final void add(int index, AxisHierarchy hierarchy)
  /*     */{
    /* 87 */AxisHierarchy[] hierarchies = this.axis.getAxisHierarchies();
    /* 88 */index = check(index, hierarchies.length);
    /* 89 */clear(this.axis);
    /* 90 */for (int i = 0; i < index; ++i)
      /* 91 */this.axis.add(hierarchies[i]);
    /* 92 */this.axis.add(hierarchy);
    /* 93 */for (int i = index; i < hierarchies.length; ++i) {
      /* 94 */this.axis.add(hierarchies[i]);
      /*     */}
    /* 96 */init();
    /* 97 */notifyStructureChange();
    /*     */}

  /*     */public final void remove(AxisHierarchy hierarchy) {
    /* 100 */this.axis.remove(hierarchy);
    /* 101 */init();
    /* 102 */notifyStructureChange();
    /*     */}

  /*     */public final void removeAll() {
    /* 105 */this.axis.removeAll();
    /* 106 */init();
    /* 107 */notifyStructureChange();
    /*     */}

  /*     */public final void swap(AxisHierarchy hierarchy1, AxisHierarchy hierarchy2) {
    /* 110 */AxisHierarchy[] hierarchies = this.axis.getAxisHierarchies();
    /* 111 */int index1 = -1;
    /* 112 */int index2 = -1;
    /* 113 */for (int i = 0; i < hierarchies.length; ++i) {
      /* 114 */if ((index1 >= 0) && (index2 >= 0))
        /*     */break;
      /* 116 */if (index1 < 0)
        /* 117 */index1 = (hierarchies[i].equals(hierarchy1)) ? i : -1;
      /* 118 */if (index2 < 0)
        /* 119 */index2 = (hierarchies[i].equals(hierarchy2)) ? i : -1;
      /*     */}
    /* 121 */if ((index1 < 0) || (index2 < 0)) {
      /* 122 */return;
      /*     */}
    /* 124 */AxisHierarchy tmp = hierarchies[index1];
    /* 125 */hierarchies[index1] = hierarchies[index2];
    /* 126 */hierarchies[index2] = tmp;
    /* 127 */clear(this.axis);
    /* 128 */for (int i = 0; i < hierarchies.length; ++i) {
      /* 129 */this.axis.add(hierarchies[i]);
      /*     */}
    /*     */
    /* 132 */init();
    /* 133 */notifyStructureChange();
    /*     */}

  /*     */public final AxisHierarchy[] getAxisHierarchies() {
    /* 136 */return this.axis.getAxisHierarchies();
    /*     */}

  /*     */
  /*     */public final AxisHierarchy getAxisHierarchy(String id) {
    /* 140 */return this.axis.getAxisHierarchy(id);
    /*     */}

  /*     */
  /*     */protected AxisModelEvent notifyWillExpand(AxisItem source) {
    /* 144 */AxisModelEvent event = new AxisModelEvent(source);
    /*     */
    /* 146 */for (AxisModelListener listener : this.listeners) {
      /* 147 */listener.willExpand(event);
      /*     */}
    /* 149 */return event;
    /*     */}

  /*     */protected AxisModelEvent notifyWillCollapse(AxisItem source) {
    /* 152 */AxisModelEvent event = new AxisModelEvent(source);
    /*     */
    /* 154 */for (AxisModelListener listener : this.listeners) {
      /* 155 */listener.willCollapse(event);
      /*     */}
    /* 157 */return event;
    /*     */}

  /*     */
  /*     */protected void notifyExpanded(AxisItem source, AxisItem[][] items) {
    /* 161 */AxisModelEvent event = new AxisModelEvent(source);
    /* 162 */event.setItems(items);
    /*     */
    /* 164 */for (AxisModelListener listener : this.listeners)
      /* 165 */listener.expanded(event);
    /*     */}

  /*     */
  /*     */protected void notifyCollapsed(AxisItem source, AxisItem[][] items)
  /*     */{
    /* 170 */AxisModelEvent event = new AxisModelEvent(source);
    /* 171 */event.setItems(items);
    /*     */
    /* 173 */for (AxisModelListener listener : this.listeners)
      /* 174 */listener.collapsed(event);
    /*     */}

  /*     */
  /*     */protected void notifyStructureChange()
  /*     */{
    /* 180 */AxisModelEvent event = new AxisModelEvent(null);
    /*     */
    /* 182 */for (AxisModelListener listener : this.listeners)
      /* 183 */listener.structureChanged(event);
    /*     */}

  /*     */
  /*     */private final void clear(Axis axis)
  /*     */{
    /* 188 */AxisHierarchy[] hierarchies = axis.getAxisHierarchies();
    /* 189 */for (AxisHierarchy hierarchy : hierarchies)
      /* 190 */axis.remove(hierarchy);
    /*     */}

  /*     */
  /*     */private final int check(int index, int max) {
    /* 194 */if (index < 0)
      /* 195 */index = 0;
    /* 196 */else if (index > max)
      /* 197 */index = max;
    /* 198 */return index;
    /*     */}
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi
 * .jar Qualified Name: org.palo.viewapi.uimodels.axis.AbstractAxisModel JD-Core
 * Version: 0.5.4
 */