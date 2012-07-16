/*     */ package org.palo.api;
/*     */ 
/*     */ public class ConnectionEvent
/*     */ {
/*     */   public static final int CONNECTION_EVENT_DATABASES_ADDED = 0;
/*     */   public static final int CONNECTION_EVENT_DATABASES_REMOVED = 1;
/*     */   public static final int CONNECTION_EVENT_DIMENSIONS_ADDED = 2;
/*     */   public static final int CONNECTION_EVENT_DIMENSIONS_REMOVED = 3;
/*     */   public static final int CONNECTION_EVENT_DIMENSIONS_RENAMED = 4;
/*     */   public static final int CONNECTION_EVENT_ELEMENTS_ADDED = 5;
/*     */   public static final int CONNECTION_EVENT_ELEMENTS_REMOVED = 6;
/*     */   public static final int CONNECTION_EVENT_ELEMENTS_RENAMED = 7;
/*     */   public static final int CONNECTION_EVENT_ELEMENTS_TYPE_CHANGED = 8;
/*     */   public static final int CONNECTION_EVENT_CUBES_ADDED = 9;
/*     */   public static final int CONNECTION_EVENT_CUBES_REMOVED = 10;
/*     */   public static final int CONNECTION_EVENT_CONSOLIDATIONS_ADDED = 11;
/*     */   public static final int CONNECTION_EVENT_CONSOLIDATIONS_REMOVED = 12;
/*     */   public static final int CONNECTION_EVENT_SERVER_STRUCTURE_CHANGED = 13;
/*     */   public static final int CONNECTION_EVENT_SERVER_DOWN = 14;
/*     */   public static final int CONNECTION_EVENT_ATTRIBUTES_ADDED = 15;
/*     */   public static final int CONNECTION_EVENT_ATTRIBUTES_REMOVED = 16;
/*     */   public static final int CONNECTION_EVENT_ATTRIBUTES_CHANGED = 17;
/*     */   public static final int CONNECTION_EVENT_CUBES_RENAMED = 18;
/*     */   public static final int CONNECTION_EVENT_RULES_ADDED = 19;
/*     */   public static final int CONNECTION_EVENT_RULES_REMOVED = 20;
/*     */   public static final int CONNECTION_EVENT_RULES_CHANGED = 21;
/*     */   public static final int CONNECTION_EVENT_DATABASES_RENAMED = 22;
/*     */   private final Connection source;
/*     */   private final Object parent;
/*     */   private final int type;
/*     */   private final Object[] items;
/*     */   public Object oldValue;
/*     */ 
/*     */   public ConnectionEvent(Connection source, Object parent, int type, Object[] items)
/*     */   {
/* 191 */     this.source = source;
/* 192 */     this.parent = parent;
/* 193 */     this.type = type;
/* 194 */     this.items = ((Object[])items.clone());
/*     */   }
/*     */ 
/*     */   public Connection getSource()
/*     */   {
/* 203 */     return this.source;
/*     */   }
/*     */ 
/*     */   public Object getParent()
/*     */   {
/* 212 */     return this.parent;
/*     */   }
/*     */ 
/*     */   public int getType()
/*     */   {
/* 222 */     return this.type;
/*     */   }
/*     */ 
/*     */   public Database[] getDatabases()
/*     */   {
/* 233 */     if ((this.type != 0) && 
/* 234 */       (this.type != 1)) {
/* 235 */       return new Database[0];
/*     */     }
/* 237 */     Database[] dbs = new Database[this.items.length];
/* 238 */     System.arraycopy(this.items, 0, dbs, 0, this.items.length);
/* 239 */     return dbs;
/*     */   }
/*     */ 
/*     */   public Dimension[] getDimensions()
/*     */   {
/* 250 */     if ((this.type != 2) && 
/* 251 */       (this.type != 3) && 
/* 252 */       (this.type != 4)) {
/* 253 */       return new Dimension[0];
/*     */     }
/* 255 */     Dimension[] dims = new Dimension[this.items.length];
/* 256 */     System.arraycopy(this.items, 0, dims, 0, this.items.length);
/* 257 */     return dims;
/*     */   }
/*     */ 
/*     */   public Element[] getElements()
/*     */   {
/* 268 */     if ((this.type != 5) && 
/* 269 */       (this.type != 6) && 
/* 270 */       (this.type != 7) && 
/* 271 */       (this.type != 8)) {
/* 272 */       return new Element[0];
/*     */     }
/* 274 */     Element[] elements = new Element[this.items.length];
/* 275 */     System.arraycopy(this.items, 0, elements, 0, this.items.length);
/* 276 */     return elements;
/*     */   }
/*     */ 
/*     */   public Attribute[] getAttributes()
/*     */   {
/* 286 */     switch (this.type)
/*     */     {
/*     */     case 15:
/*     */     case 16:
/*     */     case 17:
/* 290 */       Attribute[] attributes = new Attribute[this.items.length];
/* 291 */       System.arraycopy(this.items, 0, attributes, 0, this.items.length);
/* 292 */       return attributes;
/*     */     }
/* 294 */     return new Attribute[0];
/*     */   }
/*     */ 
/*     */   public Consolidation[] getConsolidation()
/*     */   {
/* 304 */     if ((this.type != 11) && 
/* 305 */       (this.type != 12)) {
/* 306 */       return new Consolidation[0];
/*     */     }
/* 308 */     Consolidation[] consolidations = new Consolidation[this.items.length];
/* 309 */     System.arraycopy(this.items, 0, consolidations, 0, this.items.length);
/* 310 */     return consolidations;
/*     */   }
/*     */ 
/*     */   public Cube[] getCubes()
/*     */   {
/* 320 */     if ((this.type != 9) && 
/* 321 */       (this.type != 18) && 
/* 322 */       (this.type != 10)) {
/* 323 */       return new Cube[0];
/*     */     }
/* 325 */     Cube[] cubes = new Cube[this.items.length];
/* 326 */     System.arraycopy(this.items, 0, cubes, 0, this.items.length);
/* 327 */     return cubes;
/*     */   }
/*     */ 
/*     */   public final Rule[] getRules()
/*     */   {
/* 336 */     switch (this.type)
/*     */     {
/*     */     case 19:
/*     */     case 20:
/*     */     case 21:
/* 340 */       Rule[] rules = new Rule[this.items.length];
/* 341 */       System.arraycopy(this.items, 0, rules, 0, this.items.length);
/* 342 */       return rules;
/*     */     }
/* 344 */     return new Rule[0];
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 349 */     return super.getClass().getName() + " { type=" + this.type + " }";
/*     */   }
/*     */ }

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name:     org.palo.api.ConnectionEvent
 * JD-Core Version:    0.5.4
 */