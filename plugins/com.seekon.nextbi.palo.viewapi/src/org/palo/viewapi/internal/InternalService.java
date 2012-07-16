/*    */ package org.palo.viewapi.internal;
/*    */ 
/*    */ import org.palo.viewapi.AuthUser;
/*    */ import org.palo.viewapi.internal.dbmappers.MapperRegistry;
/*    */ 
/*    */ class InternalService
/*    */ {
/*    */   protected final AuthUserImpl user;
/*    */   protected final MapperRegistry mapperReg;
/*    */ 
/*    */   InternalService(AuthUser user)
/*    */   {
/* 54 */     if (user == null)
/* 55 */       throw new NullPointerException("user may not be null");
/* 56 */     this.user = ((AuthUserImpl)user);
/*    */ 
/* 58 */     this.mapperReg = MapperRegistry.getInstance();
/*    */   }
/*    */ 
/*    */   protected final IAccountManagement getAccountManagement() {
/* 62 */     return this.mapperReg.getAccountManagement();
/*    */   }
/*    */   protected final IUserManagement getUserManagement() {
/* 65 */     return this.mapperReg.getUserManagement();
/*    */   }
/*    */ 
/*    */   protected final IGroupManagement getGroupManagement() {
/* 69 */     return this.mapperReg.getGroupManagement();
/*    */   }
/*    */ 
/*    */   protected final IRoleManagement getRoleManagement() {
/* 73 */     return this.mapperReg.getRoleManagement();
/*    */   }
/*    */ 
/*    */   protected final IReportManagement getReportManagement() {
/* 77 */     return this.mapperReg.getReportManagement();
/*    */   }
/*    */ 
/*    */   protected final IViewManagement getViewManagement() {
/* 81 */     return this.mapperReg.getViewManagement();
/*    */   }
/*    */ 
/*    */   protected final IFolderManagement getFolderManagement() {
/* 85 */     return this.mapperReg.getFolderManagement();
/*    */   }
/*    */ 
/*    */   protected final IConnectionManagement getConnectionManagement() {
/* 89 */     return this.mapperReg.getConnectionManagement();
/*    */   }
/*    */ }

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi.jar
 * Qualified Name:     org.palo.viewapi.internal.InternalService
 * JD-Core Version:    0.5.4
 */