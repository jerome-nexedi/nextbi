/*     */package org.palo.api.impl.views;

/*     */
/*     */import java.util.HashMap; /*     */
import java.util.HashSet; /*     */
import java.util.Iterator; /*     */
import java.util.Set; /*     */
import org.palo.api.Axis; /*     */
import org.palo.api.Cube; /*     */
import org.palo.api.CubeView; /*     */
import org.palo.api.PaloAPIException; /*     */
import org.palo.api.Property;

/*     */
/*     */class CubeViewImpl
/*     */implements CubeView
/*     */{
  /*     */private final String id;

  /*     */private final Cube srcCube;

  /* 66 */private final Set axes = new HashSet();

  /*     */private String name;

  /*     */private String description;

  /*     */private HashMap properties;

  /*     */
  /*     */CubeViewImpl(String id, String name, Cube srcCube)
  /*     */{
    /* 94 */this.id = id;
    /* 95 */this.name = name;
    /* 96 */this.srcCube = srcCube;
    /*     */
    /* 98 */this.properties = new HashMap();
    /* 99 */this.description = "";
    /*     */}

  /*     */
  /*     */final void reset()
  /*     */{
    /* 107 */this.axes.clear();
    /*     */}

  /*     */
  /*     */public final Cube getCube()
  /*     */{
    /* 114 */return this.srcCube;
    /*     */}

  /*     */
  /*     */public final synchronized String getDescription()
  /*     */{
    /* 121 */return this.description;
    /*     */}

  /*     */
  /*     */public final synchronized void setDescription(String description)
  /*     */{
    /* 128 */this.description = description;
    /*     */}

  /*     */
  /*     */public final String getId()
  /*     */{
    /* 135 */return this.id;
    /*     */}

  /*     */
  /*     */public final synchronized String getName()
  /*     */{
    /* 142 */return this.name;
    /*     */}

  /*     */
  /*     */public final synchronized void setName(String name)
  /*     */{
    /* 149 */this.name = ((name != null) ? name : "");
    /*     */}

  /*     */
  /*     */public final Axis addAxis(String id, String name)
  /*     */{
    /* 157 */AxisImpl axis = new AxisImpl(id, name, this);
    /* 158 */if (this.axes.contains(axis))
      /* 159 */throw new PaloAPIException("Axis already exist!");
    /* 160 */this.axes.add(axis);
    /* 161 */return axis;
    /*     */}

  /*     */
  /*     */public final Axis[] getAxes()
  /*     */{
    /* 168 */return (Axis[]) this.axes.toArray(new Axis[this.axes.size()]);
    /*     */}

  /*     */
  /*     */public final Axis getAxis(String id)
  /*     */{
    /* 176 */for (Iterator it = this.axes.iterator(); it.hasNext();) {
      /* 177 */Axis axis = (Axis) it.next();
      /* 178 */if (axis.getId().equals(id))
        /* 179 */return axis;
      /*     */}
    /* 181 */return null;
    /*     */}

  /*     */
  /*     */public final void removeAxis(Axis axis)
  /*     */{
    /* 188 */this.axes.remove(axis);
    /*     */}

  /*     */
  /*     */public final void save()
  /*     */{
    /* 195 */CubeViewManager.getInstance().save(this);
    /*     */}

  /*     */
  /*     */public final String getRawDefinition()
  /*     */{
    /* 203 */return CubeViewManager.getInstance().getRawDefinition(this);
    /*     */}

  /*     */
  /*     */public void addProperty(String id, String value)
  /*     */{
    /* 211 */this.properties.put(id, value);
    /*     */}

  /*     */
  /*     */public void addProperty(Property property)
  /*     */{
    /* 218 */if (property == null) {
      /* 219 */return;
      /*     */}
    /* 221 */this.properties.put(property.getId(), property.getValue());
    /*     */}

  /*     */
  /*     */public String[] getProperties()
  /*     */{
    /* 228 */return (String[]) this.properties.keySet().toArray(
    /* 229 */new String[this.properties.size()]);
    /*     */}

  /*     */
  /*     */public String getPropertyValue(String id)
  /*     */{
    /* 237 */return (String) this.properties.get(id);
    /*     */}

  /*     */
  /*     */public void removeProperty(String id)
  /*     */{
    /* 244 */this.properties.remove(id);
    /*     */}

  /*     */
  /*     */public void removeProperty(Property property)
  /*     */{
    /* 251 */if (property == null) {
      /* 252 */return;
      /*     */}
    /* 254 */removeProperty(property.getId());
    /*     */}

  /*     */
  /*     *//** @deprecated */
  /*     */public boolean isHideEmpty()
  /*     */{
    /* 261 */return "true".equals(getPropertyValue("hideEmpty"));
    /*     */}

  /*     */
  /*     *//** @deprecated */
  /*     */public void setHideEmpty(boolean hideEmpty)
  /*     */{
    /* 268 */addProperty("hideEmpty", Boolean.toString(hideEmpty));
    /*     */}
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name: org.palo.api.impl.views.CubeViewImpl JD-Core Version: 0.5.4
 */