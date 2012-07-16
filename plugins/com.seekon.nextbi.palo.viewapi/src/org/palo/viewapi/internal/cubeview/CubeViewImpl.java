/*     */ package org.palo.viewapi.internal.cubeview;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.palo.api.Cube;
/*     */ import org.palo.api.Database;
/*     */ import org.palo.api.Element;
/*     */ import org.palo.api.Hierarchy;
/*     */ import org.palo.api.PaloAPIException;
/*     */ import org.palo.viewapi.AuthUser;
/*     */ import org.palo.viewapi.Axis;
/*     */ import org.palo.viewapi.AxisHierarchy;
/*     */ import org.palo.viewapi.CubeView;
/*     */ import org.palo.viewapi.Property;
/*     */ import org.palo.viewapi.Right;
/*     */ import org.palo.viewapi.View;
/*     */ import org.palo.viewapi.exceptions.NoPermissionException;
/*     */ import org.palo.viewapi.internal.ViewImpl;
/*     */ import org.palo.viewapi.internal.io.CubeViewReader;
/*     */ import org.palo.viewapi.internal.util.CompoundKey;
/*     */ import org.palo.viewapi.uimodels.formats.Format;
/*     */ import org.palo.viewapi.uimodels.formats.FormatImpl;
/*     */ import org.palo.viewapi.uimodels.formats.FormatRangeInfo;
/*     */ 
/*     */ class CubeViewImpl
/*     */   implements CubeView
/*     */ {
/*  76 */   private final Set<Axis> axes = new HashSet();
/*     */   private final HashMap<String, Property<Object>> properties;
/*     */   private final Map<String, Format> formats;
/*     */   private final List<FormatRangeInfo> formatRanges;
/*     */   private final View view;
/*     */   private final AuthUser authUser;
/*     */   private final Cube srcCube;
/*     */   private final CompoundKey key;
/*     */   private String description;
/*     */ 
/*     */   CubeViewImpl(View view, Cube srcCube, AuthUser user, String externalId)
/*     */   {
/*  97 */     this.view = view;
/*  98 */     this.authUser = user;
/*  99 */     this.srcCube = srcCube;
/*     */ 
/* 101 */     this.properties = new HashMap();
/* 102 */     if (externalId != null) {
/* 103 */       addProperty("paloSuiteID", externalId);
/*     */     }
/* 105 */     this.formats = new LinkedHashMap();
/* 106 */     this.formatRanges = new ArrayList();
/*     */ 
/* 108 */     this.key = 
/* 109 */       new CompoundKey(new Object[] { CubeViewImpl.class, 
/* 109 */       view.getId(), srcCube.getId(), srcCube.getDatabase().getId() });
/*     */   }
/*     */ 
/*     */   private CubeViewImpl(CubeViewImpl cView)
/*     */   {
/* 114 */     this(cView.view, cView.srcCube, cView.authUser, (String)cView.getPropertyValue("paloSuiteID"));
/* 115 */     this.description = cView.description;
/*     */ 
/* 118 */     for (Axis axis : cView.axes) {
/* 119 */       this.axes.add(axis.copy());
/*     */     }
/*     */ 
/* 122 */     for (Format format : cView.formats.values()) {
/* 123 */       Format fmCopy = format.copy();
/* 124 */       this.formats.put(fmCopy.getId(), fmCopy);
/*     */     }
/* 126 */     for (FormatRangeInfo rangeInfo : cView.formatRanges) {
/* 127 */       this.formatRanges.add(rangeInfo.copy());
/*     */     }
/*     */ 
/* 131 */     this.properties.putAll(cView.properties);
/*     */   }
/*     */ 
/*     */   public final Axis addAxis(String id, String name)
/*     */   {
/* 143 */     checkPermission(Right.WRITE);
/* 144 */     AxisImpl axis = new AxisImpl(id, name, this.view);
/* 145 */     if (this.axes.contains(axis))
/* 146 */       throw new PaloAPIException("Axis already exist!");
/* 147 */     this.axes.add(axis);
/* 148 */     return axis;
/*     */   }
/*     */ 
/*     */   public final Format addFormat(String id) {
/* 152 */     checkPermission(Right.WRITE);
/* 153 */     if (id == null) {
/* 154 */       return null;
/*     */     }
/* 156 */     Format format = new FormatImpl(id);
/* 157 */     this.formats.put(id, format);
/* 158 */     return format;
/*     */   }
/*     */ 
/*     */   public final void addFormat(Format format) {
/* 162 */     checkPermission(Right.WRITE);
/* 163 */     if (format == null) {
/* 164 */       return;
/*     */     }
/* 166 */     this.formats.put(format.getId(), format);
/*     */   }
/*     */ 
/*     */   public final Property<Object> addProperty(String id, Object value) {
/* 170 */     checkPermission(Right.WRITE);
/* 171 */     Property prop = new Property(id, value);
/* 172 */     this.properties.put(id, prop);
/* 173 */     return prop;
/*     */   }
/*     */ 
/*     */   public final CubeView copy() {
/* 177 */     return new CubeViewImpl(this);
/*     */   }
/*     */ 
/*     */   public final Axis[] getAxes() {
/* 181 */     return (Axis[])this.axes.toArray(new Axis[this.axes.size()]);
/*     */   }
/*     */ 
/*     */   public final Axis getAxis(String id) {
/* 185 */     for (Iterator it = this.axes.iterator(); it.hasNext(); ) {
/* 186 */       Axis axis = (Axis)it.next();
/* 187 */       if (axis.getId().equals(id))
/* 188 */         return axis;
/*     */     }
/* 190 */     return null;
/*     */   }
/*     */ 
/*     */   public final Cube getCube() {
/* 194 */     return this.srcCube;
/*     */   }
/*     */ 
/*     */   public final String getDescription() {
/* 198 */     return this.description;
/*     */   }
/*     */ 
/*     */   public final Format getFormat(String formatId) {
/* 202 */     return (Format)this.formats.get(formatId);
/*     */   }
/*     */ 
/*     */   public final Format[] getFormats() {
/* 206 */     return (Format[])this.formats.values().toArray(new Format[0]);
/*     */   }
/*     */ 
/*     */   public final String getId() {
/* 210 */     return this.view.getId();
/*     */   }
/*     */ 
/*     */   public Property<Object>[] getProperties()
/*     */   {
/* 215 */     return (Property[])this.properties.values().toArray(new Property[0]);
/*     */   }
/*     */ 
/*     */   public Property<Object> getProperty(String id) {
/* 219 */     return (Property)this.properties.get(id);
/*     */   }
/*     */ 
/*     */   public Object getPropertyValue(String id) {
/* 223 */     Property prop = getProperty(id);
/* 224 */     if (prop == null) {
/* 225 */       return null;
/*     */     }
/* 227 */     return prop.getValue();
/*     */   }
/*     */ 
/*     */   public final boolean hasFormats() {
/* 231 */     return !this.formats.isEmpty();
/*     */   }
/*     */ 
/*     */   public final void removeAllFormats() {
/* 235 */     checkPermission(Right.WRITE);
/* 236 */     this.formats.clear();
/*     */   }
/*     */ 
/*     */   public final void removeAxis(Axis axis) {
/* 240 */     checkPermission(Right.WRITE);
/* 241 */     ((AxisImpl)axis).setView(null);
/* 242 */     this.axes.remove(axis);
/*     */   }
/*     */ 
/*     */   public final void removeFormat(String formatId) {
/* 246 */     checkPermission(Right.WRITE);
/* 247 */     this.formats.remove(formatId);
/*     */   }
/*     */ 
/*     */   public final void removeProperty(String id) {
/* 251 */     checkPermission(Right.WRITE);
/* 252 */     this.properties.remove(id);
/*     */   }
/*     */ 
/*     */   public final void setDescription(String description) {
/* 256 */     checkPermission(Right.WRITE);
/* 257 */     this.description = description;
/*     */   }
/*     */ 
/*     */   public final void setName(String name) {
/* 261 */     checkPermission(Right.WRITE);
/* 262 */     ((ViewImpl)this.view).setName(name);
/*     */   }
/*     */ 
/*     */   public final String getName() {
/* 266 */     return this.view.getName();
/*     */   }
/*     */ 
/*     */   public final Object getDefaultValue(String parameterName) {
/* 270 */     if (parameterName.equals("Element")) {
/* 271 */       for (Axis a : getAxes()) {
/* 272 */         for (Hierarchy h : a.getHierarchies()) {
/* 273 */           if (h.getElementCount() > 0) {
/* 274 */             return h.getElementAt(0);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 279 */     return null;
/*     */   }
/*     */ 
/*     */   public final String[] getParameterNames() {
/* 283 */     return new String[] { "Element" };
/*     */   }
/*     */ 
/*     */   public final Object getParameterValue(String parameterName) {
/* 287 */     if (parameterName.equals("Element")) {
/* 288 */       Property prop = (Property)this.properties.get("VAR_" + parameterName);
/* 289 */       if (prop != null) {
/* 290 */         return prop.getValue();
/*     */       }
/*     */     }
/* 293 */     return null;
/*     */   }
/*     */ 
/*     */   public boolean isParameterized() {
/* 297 */     return this.properties.get("VAR_Element") != null;
/*     */   }
/*     */ 
/*     */   public final void setParameter(String parameterName, Object parameterValue) {
/* 301 */     checkPermission(Right.WRITE);
/* 302 */     if (!parameterName.equals("Element")) {
/* 303 */       return;
/*     */     }
/* 305 */     if (!(parameterValue instanceof Element[])) {
/* 306 */       return;
/*     */     }
/* 308 */     Element[] pars = (Element[])parameterValue;
/* 309 */     Axis a = getAxis("selected");
/* 310 */     if (a == null) {
/* 311 */       return;
/*     */     }
/* 313 */     for (Element e : pars) {
/* 314 */       if (e == null)
/*     */         continue;
/*     */       AxisHierarchy ah;
/* 318 */       if ((ah = a.getAxisHierarchy(e.getHierarchy())) != null) {
/* 319 */         ah.clearSelectedElements();
/* 320 */         ah.addSelectedElement(e);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void addParameterValue(String parameterName, Object parameterValue) {
/* 326 */     if (parameterName.equals("Element")) {
/* 327 */       Object o = getParameterValue(parameterName);
/* 328 */       if (o == null) {
/* 329 */         setParameter(parameterName, parameterValue);
/* 330 */       } else if ((o instanceof Element[]) && (parameterValue instanceof Element)) {
/* 331 */         Element[] result = (Element[])o;
/* 332 */         Element[] nVal = new Element[result.length + 1];
/* 333 */         for (int i = 0; i < result.length; ++i) {
/* 334 */           nVal[i] = result[i];
/*     */         }
/* 336 */         nVal[result.length] = ((Element)parameterValue);
/* 337 */         setParameter(parameterName, nVal);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public final void setParameterNames(String[] parameterNames) {
/* 343 */     checkPermission(Right.WRITE);
/*     */   }
/*     */ 
/*     */   public final boolean equals(Object obj) {
/* 347 */     if (obj instanceof CubeView) {
/* 348 */       CubeViewImpl other = (CubeViewImpl)obj;
/* 349 */       return this.key.equals(other.key);
/*     */     }
/* 351 */     return false;
/*     */   }
/*     */ 
/*     */   public final int hashCode() {
/* 355 */     int hc = 17;
/* 356 */     hc += 23 * this.key.hashCode();
/* 357 */     return hc;
/*     */   }
/*     */ 
/*     */   final void reset()
/*     */   {
/* 367 */     this.axes.clear();
/*     */   }
/*     */ 
/*     */   private final void checkPermission(Right right)
/*     */   {
/* 375 */     if ((!CubeViewReader.CHECK_RIGHTS) || 
/* 376 */       (this.authUser.hasPermission(right, this.view))) return;
/* 377 */     throw new NoPermissionException("Not enough rights!", this.view, 
/* 378 */       this.authUser, Right.WRITE);
/*     */   }
/*     */ }

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi.jar
 * Qualified Name:     org.palo.viewapi.internal.cubeview.CubeViewImpl
 * JD-Core Version:    0.5.4
 */