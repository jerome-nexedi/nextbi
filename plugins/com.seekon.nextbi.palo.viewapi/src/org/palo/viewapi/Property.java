/*     */package org.palo.viewapi;

/*     */
/*     */import java.util.Collection; /*     */
import java.util.LinkedHashMap; /*     */
import java.util.Map;

/*     */
/*     */public class Property<T>
/*     */{
  /*     */private final String id;

  /*     */private T value;

  /*     */private Property<T> parent;

  /*     */private final Map<String, Property<T>> children;

  /*     */
  /*     */public Property(Property<T> parent, String id, T value)
  /*     */{
    /* 76 */this.children = new LinkedHashMap();
    /* 77 */this.parent = parent;
    /* 78 */this.id = id;
    /* 79 */this.value = value;
    /*     */}

  /*     */
  /*     */public Property(String id, T value)
  /*     */{
    /* 90 */this(null, id, value);
    /*     */}

  /*     */
  /*     */public String getId()
  /*     */{
    /* 98 */return this.id;
    /*     */}

  /*     */
  /*     */public T getValue()
  /*     */{
    /* 106 */return this.value;
    /*     */}

  /*     */
  /*     */public void setValue(T newValue)
  /*     */{
    /* 114 */this.value = newValue;
    /*     */}

  /*     */
  /*     */public Property<T> getParent()
  /*     */{
    /* 122 */return this.parent;
    /*     */}

  /*     */
  /*     */public int getChildCount()
  /*     */{
    /* 130 */return this.children.size();
    /*     */}

  /*     */
  /*     */public Property<T>[] getChildren()
  /*     */{
    /* 140 */return (Property[]) this.children.values().toArray(new Property[0]);
    /*     */}

  /*     */
  /*     */public T getChildValue(String childId)
  /*     */{
    /* 150 */Property<T> prop = (Property) this.children.get(childId);
    /* 151 */if (prop == null) {
      /* 152 */return null;
      /*     */}
    /* 154 */return prop.getValue();
    /*     */}

  /*     */
  /*     */public void addChild(Property<T> child)
  /*     */{
    /* 162 */if (child == null) {
      /* 163 */return;
      /*     */}
    /* 165 */this.children.put(child.getId(), child);
    /*     */}

  /*     */
  /*     */public void removeChild(Property<T> child)
  /*     */{
    /* 173 */if (child == null) {
      /* 174 */return;
      /*     */}
    /* 176 */this.children.remove(child.getId());
    /*     */}

  /*     */
  /*     */public void removeAllChildren()
  /*     */{
    /* 183 */this.children.clear();
    /*     */}
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi
 * .jar Qualified Name: org.palo.viewapi.Property JD-Core Version: 0.5.4
 */