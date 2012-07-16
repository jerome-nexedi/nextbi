/*     */ package org.palo.api.impl;
/*     */ 
/*     */ import com.tensegrity.palojava.DbConnection;
/*     */ import com.tensegrity.palojava.PropertyInfo;
/*     */ import com.tensegrity.palojava.RuleInfo;
/*     */ import com.tensegrity.palojava.loader.PropertyLoader;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.palo.api.Cube;
/*     */ import org.palo.api.Property2;
/*     */ import org.palo.api.Rule;
/*     */ 
/*     */ class RuleImpl
/*     */   implements Rule
/*     */ {
/*     */   private final CubeImpl cube;
/*     */   private final RuleInfo ruleInfo;
/*     */   private final DbConnection dbConnection;
/*     */   private final PropertyLoader propertyLoader;
/*     */   private final Map<String, Property2Impl> loadedProperties;
/*     */ 
/*     */   RuleImpl(DbConnection dbConnection, CubeImpl cube, RuleInfo ruleInfo)
/*     */   {
/*  66 */     this.cube = cube;
/*  67 */     this.ruleInfo = ruleInfo;
/*  68 */     this.dbConnection = dbConnection;
/*  69 */     this.loadedProperties = new HashMap();
/*  70 */     this.propertyLoader = dbConnection.getTypedPropertyLoader(ruleInfo);
/*     */   }
/*     */ 
/*     */   public final Cube getCube() {
/*  74 */     return this.cube;
/*     */   }
/*     */ 
/*     */   public final String getDefinition() {
/*  78 */     return this.ruleInfo.getDefinition();
/*     */   }
/*     */ 
/*     */   public final String getId()
/*     */   {
/*  87 */     return this.ruleInfo.getId();
/*     */   }
/*     */ 
/*     */   public final RuleInfo getInfo() {
/*  91 */     return this.ruleInfo;
/*     */   }
/*     */ 
/*     */   public final String getComment() {
/*  95 */     return this.ruleInfo.getComment();
/*     */   }
/*     */ 
/*     */   public final String getExternalIdentifier() {
/*  99 */     return this.ruleInfo.getExternalIdentifier();
/*     */   }
/*     */ 
/*     */   public void setComment(String comment) {
/* 103 */     this.dbConnection.update(this.ruleInfo, this.ruleInfo.getDefinition(), 
/* 104 */       this.ruleInfo.getExternalIdentifier(), this.ruleInfo.useExternalIdentifier(), 
/* 105 */       comment, this.ruleInfo.isActive());
/* 106 */     this.cube.fireRuleChanged(this, comment);
/*     */   }
/*     */ 
/*     */   public void setDefinition(String definition) {
/* 110 */     this.dbConnection.update(this.ruleInfo, definition, 
/* 111 */       this.ruleInfo.getExternalIdentifier(), this.ruleInfo.useExternalIdentifier(), 
/* 112 */       this.ruleInfo.getComment(), this.ruleInfo.isActive());
/* 113 */     this.cube.fireRuleChanged(this, definition);
/*     */   }
/*     */ 
/*     */   public void setExternalIdentifier(String externalId)
/*     */   {
/* 124 */     setExternalIdentifier(externalId, false);
/*     */   }
/*     */ 
/*     */   public void setExternalIdentifier(String externalId, boolean useIt) {
/* 128 */     this.dbConnection.update(this.ruleInfo, this.ruleInfo.getDefinition(), externalId, 
/* 129 */       useIt, this.ruleInfo.getComment(), this.ruleInfo.isActive());
/* 130 */     this.cube.fireRuleChanged(this, externalId);
/*     */   }
/*     */ 
/*     */   public void update(String definition, String externalIdentifier, boolean useIt, String comment)
/*     */   {
/* 135 */     update(definition, externalIdentifier, useIt, comment, isActive());
/*     */   }
/*     */ 
/*     */   public void update(String definition, String externalIdentifier, boolean useIt, String comment, boolean activate) {
/* 139 */     this.dbConnection.update(this.ruleInfo, definition, externalIdentifier, useIt, 
/* 140 */       comment, activate);
/* 141 */     this.cube.fireRuleChanged(this, null);
/*     */   }
/*     */ 
/*     */   public void useExternalIdentifier(boolean useIt) {
/* 145 */     String extId = this.ruleInfo.getExternalIdentifier();
/* 146 */     if ((extId == null) || (extId.equals("")))
/* 147 */       return;
/* 148 */     this.dbConnection.update(this.ruleInfo, this.ruleInfo.getDefinition(), this.ruleInfo.getExternalIdentifier(), useIt, 
/* 149 */       this.ruleInfo.getComment(), this.ruleInfo.isActive());
/*     */   }
/*     */ 
/*     */   public String[] getAllPropertyIds() {
/* 153 */     return this.propertyLoader.getAllPropertyIds();
/*     */   }
/*     */ 
/*     */   public Property2 getProperty(String id) {
/* 157 */     PropertyInfo propInfo = this.propertyLoader.load(id);
/* 158 */     if (propInfo == null) {
/* 159 */       return null;
/*     */     }
/* 161 */     Property2 property = (Property2)this.loadedProperties.get(propInfo.getId());
/* 162 */     if (property == null) {
/* 163 */       property = createProperty(propInfo);
/*     */     }
/*     */ 
/* 166 */     return property;
/*     */   }
/*     */ 
/*     */   public void addProperty(Property2 property) {
/* 170 */     if (property == null) {
/* 171 */       return;
/*     */     }
/* 173 */     Property2Impl _property = (Property2Impl)property;
/* 174 */     this.propertyLoader.loaded(_property.getPropInfo());
/* 175 */     this.loadedProperties.put(_property.getId(), _property);
/*     */   }
/*     */ 
/*     */   public void removeProperty(String id) {
/* 179 */     Property2 property = getProperty(id);
/* 180 */     if (property == null) {
/* 181 */       return;
/*     */     }
/* 183 */     if (property.isReadOnly()) {
/* 184 */       return;
/*     */     }
/* 186 */     this.loadedProperties.remove(property);
/*     */   }
/*     */ 
/*     */   public final boolean isActive() {
/* 190 */     return this.ruleInfo.isActive();
/*     */   }
/*     */ 
/*     */   public final void setActive(boolean activate) {
/* 194 */     this.dbConnection.update(this.ruleInfo, this.ruleInfo.getDefinition(), this.ruleInfo.getExternalIdentifier(), this.ruleInfo.useExternalIdentifier(), 
/* 195 */       this.ruleInfo.getComment(), activate);
/* 196 */     this.cube.fireRuleChanged(this, Boolean.valueOf(activate));
/*     */   }
/*     */ 
/*     */   public final long getTimestamp() {
/* 200 */     return this.ruleInfo.getTimestamp();
/*     */   }
/*     */ 
/*     */   final void clearCache() {
/* 204 */     for (Property2Impl property : this.loadedProperties.values())
/* 205 */       property.clearCache();
/* 206 */     this.loadedProperties.clear();
/* 207 */     this.propertyLoader.reset();
/*     */   }
/*     */   private void createProperty(Property2 parent, PropertyInfo kid) {
/* 210 */     Property2 p2Kid = Property2Impl.create(parent, kid);
/* 211 */     parent.addChild(p2Kid);
/* 212 */     for (PropertyInfo kidd : kid.getChildren())
/* 213 */       createProperty(p2Kid, kidd);
/*     */   }
/*     */ 
/*     */   private Property2 createProperty(PropertyInfo propInfo)
/*     */   {
/* 218 */     Property2 prop = Property2Impl.create(null, propInfo);
/* 219 */     for (PropertyInfo kid : propInfo.getChildren()) {
/* 220 */       createProperty(prop, kid);
/*     */     }
/* 222 */     return prop;
/*     */   }
/*     */ }

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name:     org.palo.api.impl.RuleImpl
 * JD-Core Version:    0.5.4
 */