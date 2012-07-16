/*    */ package org.palo.api.impl;
/*    */ 
/*    */ import org.palo.api.Connection;
/*    */ import org.palo.api.Property2;
/*    */ import org.palo.api.Property2Factory;
/*    */ 
/*    */ public class Property2FactoryImpl extends Property2Factory
/*    */ {
/*    */   public final Property2 newProperty(Connection con, String id, String value)
/*    */   {
/* 40 */     return createProperty(con, id, value, null, 2, false);
/*    */   }
/*    */ 
/*    */   public final Property2 newProperty(Connection con, String id, String value, Property2 parent) {
/* 44 */     return createProperty(con, id, value, parent, 2, false);
/*    */   }
/*    */ 
/*    */   public final Property2 newProperty(Connection con, String id, String value, int type) {
/* 48 */     return createProperty(con, id, value, null, type, false);
/*    */   }
/*    */ 
/*    */   public final Property2 newProperty(Connection con, String id, String value, Property2 parent, int type)
/*    */   {
/* 53 */     return createProperty(con, id, value, parent, type, false);
/*    */   }
/*    */ 
/*    */   public final Property2 newReadOnlyProperty(Connection con, String id, String value) {
/* 57 */     return createProperty(con, id, value, null, 2, true);
/*    */   }
/*    */ 
/*    */   public final Property2 newReadOnlyProperty(Connection con, String id, String value, Property2 parent)
/*    */   {
/* 62 */     return createProperty(con, id, value, parent, 2, true);
/*    */   }
/*    */ 
/*    */   public final Property2 newReadOnlyProperty(Connection con, String id, String value, int type) {
/* 66 */     return createProperty(con, id, value, null, type, true);
/*    */   }
/*    */ 
/*    */   public final Property2 newReadOnlyProperty(Connection con, String id, String value, Property2 parent, int type)
/*    */   {
/* 71 */     return createProperty(con, id, value, parent, type, true);
/*    */   }
/*    */ 
/*    */   private final Property2 createProperty(Connection con, String id, String value, Property2 parent, int type, boolean readOnly)
/*    */   {
/* 76 */     return Property2Impl.create(con, id, value, parent, type, readOnly);
/*    */   }
/*    */ }

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name:     org.palo.api.impl.Property2FactoryImpl
 * JD-Core Version:    0.5.4
 */