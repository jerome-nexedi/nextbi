/*     */ package org.palo.viewapi.internal.dbmappers;
/*     */ 
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.palo.viewapi.DomainObject;
/*     */ import org.palo.viewapi.Report;
/*     */ import org.palo.viewapi.Role;
/*     */ import org.palo.viewapi.User;
/*     */ import org.palo.viewapi.internal.DbService;
/*     */ import org.palo.viewapi.internal.IReportManagement;
/*     */ import org.palo.viewapi.internal.IReportRoleManagement;
/*     */ import org.palo.viewapi.internal.IReportViewManagement;
/*     */ import org.palo.viewapi.internal.IRoleManagement;
/*     */ import org.palo.viewapi.internal.IUserManagement;
/*     */ import org.palo.viewapi.internal.IViewManagement;
/*     */ import org.palo.viewapi.internal.ReportImpl;
import org.palo.viewapi.internal.ReportImpl.Builder;
/*     */ 
/*     */ final class ReportMapper extends AbstractMapper
/*     */   implements IReportManagement
/*     */ {
/*  67 */   private static final String TABLE = DbService.getQuery("Reports.tableName");
/*  68 */   private static final String COLUMNS = DbService.getQuery("Reports.columns");
/*  69 */   private static final String CREATE_TABLE_STMT = DbService.getQuery("Reports.createTable", new String[] { TABLE });
/*  70 */   private static final String FIND_BY_ID_STMT = DbService.getQuery("Reports.findById", new String[] { COLUMNS, TABLE });
/*  71 */   private static final String FIND_BY_NAME_STMT = DbService.getQuery("Reports.findByName", new String[] { COLUMNS, TABLE });
/*  72 */   private static final String FIND_BY_OWNER_STMT = DbService.getQuery("Reports.findByOwner", new String[] { COLUMNS, TABLE });
/*  73 */   private static final String INSERT_STMT = DbService.getQuery("Reports.insert", new String[] { TABLE });
/*  74 */   private static final String UPDATE_STMT = DbService.getQuery("Reports.update", new String[] { TABLE });
/*  75 */   private static final String DELETE_STMT = DbService.getQuery("Reports.delete", new String[] { TABLE });
/*     */ 
/*     */   public final List<Report> findReports(Role role) throws SQLException
/*     */   {
/*  79 */     IReportManagement reportMgmt = 
/*  80 */       MapperRegistry.getInstance().getReportManagement();
/*  81 */     IReportRoleManagement rrAssoc = 
/*  82 */       MapperRegistry.getInstance().getReportRoleAssociation();
/*  83 */     List<String> reports = rrAssoc.getReports(role);
/*  84 */     List allReports = new ArrayList();
/*  85 */     for (String id : reports) {
/*  86 */       Report report = (Report)reportMgmt.find(id);
/*  87 */       if ((report != null) && (!allReports.contains(report)))
/*  88 */         allReports.add(report);
/*     */     }
/*  90 */     return allReports;
/*     */   }
/*     */ 
/*     */   public final List<Report> findReports(User owner) throws SQLException
/*     */   {
/*  95 */     List reports = new ArrayList();
/*  96 */     PreparedStatement stmt = null;
/*  97 */     ResultSet results = null;
/*  98 */     Connection connection = DbService.getConnection();
/*     */     try {
/* 100 */       stmt = connection.prepareStatement(FIND_BY_OWNER_STMT);
/* 101 */       stmt.setString(1, owner.getId());
/* 102 */       results = stmt.executeQuery();
/* 103 */       while (results.next()) {
/* 104 */         Report report = (Report)load(results);
/* 105 */         if (!reports.contains(report))
/* 106 */           reports.add(report);
/*     */       }
/* 108 */       return reports;
/*     */     } finally {
/* 110 */       cleanUp(stmt, results);
/*     */     }
/*     */   }
/*     */ 
/*     */   public final void update(DomainObject obj) throws SQLException {
/* 115 */     PreparedStatement stmt = null;
/* 116 */     Report report = (Report)obj;
/* 117 */     Connection connection = DbService.getConnection();
/*     */     try {
/* 119 */       stmt = connection.prepareStatement(UPDATE_STMT);
/* 120 */       stmt.setString(1, report.getName());
/* 121 */       stmt.setString(2, report.getDescription());
/* 122 */       stmt.setString(3, report.getOwner().getId());
/* 123 */       stmt.setString(4, report.getId());
/* 124 */       stmt.execute();
/* 125 */       handleAssociations(report);
/*     */     } finally {
/* 127 */       cleanUp(stmt);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected final void doInsert(DomainObject obj, PreparedStatement stmt) throws SQLException
/*     */   {
/* 133 */     Report report = (Report)obj;
/* 134 */     stmt.setString(1, report.getName());
/* 135 */     stmt.setString(2, report.getDescription());
/* 136 */     stmt.setString(3, report.getOwner().getId());
/* 137 */     handleAssociations(report);
/*     */   }
/*     */ 
/*     */   protected final DomainObject doLoad(String id, ResultSet result) throws SQLException
/*     */   {
/* 142 */     ReportImpl.Builder reportBuilder = new ReportImpl.Builder(id);
/* 143 */     reportBuilder.name(result.getString(2));
/* 144 */     reportBuilder.description(result.getString(3));
/* 145 */     MapperRegistry mapperReg = MapperRegistry.getInstance();
/*     */ 
/* 147 */     reportBuilder.owner(
/* 148 */       (User)mapperReg.getUserManagement().find(result.getString(4)));
/*     */ 
/* 150 */     IReportRoleManagement rrAssoc = mapperReg.getReportRoleAssociation();
/* 151 */     reportBuilder.roles(rrAssoc.getRoles(id));
/*     */ 
/* 153 */     IReportViewManagement rvAssoc = mapperReg.getReportViewAssociation();
/* 154 */     reportBuilder.views(rvAssoc.getViews(id));
/* 155 */     return reportBuilder.build();
/*     */   }
/*     */ 
/*     */   protected final void deleteAssociations(DomainObject obj)
/*     */     throws SQLException
/*     */   {
/* 171 */     Report report = (Report)obj;
/* 172 */     MapperRegistry mapperReg = MapperRegistry.getInstance();
/*     */ 
/* 175 */     IReportRoleManagement rrAssoc = mapperReg.getReportRoleAssociation();
/* 176 */     rrAssoc.delete(report);
/*     */ 
/* 179 */     IReportViewManagement rvAssoc = mapperReg.getReportViewAssociation();
/* 180 */     rvAssoc.delete(report);
/*     */   }
/*     */ 
/*     */   protected final String deleteStatement() {
/* 184 */     return DELETE_STMT;
/*     */   }
/*     */ 
/*     */   protected final String findStatement() {
/* 188 */     return FIND_BY_ID_STMT;
/*     */   }
/*     */ 
/*     */   protected final String findByNameStatement() {
/* 192 */     return FIND_BY_NAME_STMT;
/*     */   }
/*     */ 
/*     */   protected final String insertStatement() {
/* 196 */     return INSERT_STMT;
/*     */   }
/*     */   protected final String createTableStatement() {
/* 199 */     return CREATE_TABLE_STMT;
/*     */   }
/*     */ 
/*     */   protected final String getTableName() {
/* 203 */     return TABLE;
/*     */   }
/*     */ 
/*     */   private final void handleAssociations(Report report)
/*     */     throws SQLException
/*     */   {
/* 209 */     MapperRegistry mapperReg = MapperRegistry.getInstance();
/*     */ 
/* 211 */     IRoleManagement roleMgmt = mapperReg.getRoleManagement();
/* 212 */     IReportRoleManagement rrAssoc = mapperReg.getReportRoleAssociation();
/*     */ 
/* 214 */     List<String> roles = ((ReportImpl)report).getRoleIDs();
/* 215 */     List<String> savedRoles = rrAssoc.getRoles(report);
/* 216 */     for (String id : roles) {
/* 217 */       if (!savedRoles.contains(id)) {
/* 218 */         rrAssoc.insert(report, roleMgmt.find(id));
/*     */       }
/*     */     }
/* 221 */     savedRoles.removeAll(roles);
/* 222 */     for (String id : savedRoles) {
/* 223 */       rrAssoc.delete(report, roleMgmt.find(id));
/*     */     }
/*     */ 
/* 227 */     IViewManagement viewMgmt = mapperReg.getViewManagement();
/* 228 */     IReportViewManagement rvAssoc = mapperReg.getReportViewAssociation();
/*     */ 
/* 230 */     List<String> views = ((ReportImpl)report).getViewIDs();
/* 231 */     List<String> savedViews = rvAssoc.getViews(report);
/* 232 */     for (String id : views) {
/* 233 */       if (!savedViews.contains(id)) {
/* 234 */         rvAssoc.insert(report, viewMgmt.find(id));
/*     */       }
/*     */     }
/* 237 */     savedViews.removeAll(views);
/* 238 */     for (String id : savedViews)
/* 239 */       rvAssoc.delete(report, viewMgmt.find(id));
/*     */   }
/*     */ }

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi.jar
 * Qualified Name:     org.palo.viewapi.internal.dbmappers.ReportMapper
 * JD-Core Version:    0.5.4
 */