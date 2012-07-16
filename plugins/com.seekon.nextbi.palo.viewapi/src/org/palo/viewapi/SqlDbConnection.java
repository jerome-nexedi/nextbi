/*     */ package org.palo.viewapi;
/*     */ 
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.sql.Connection;
/*     */ import java.sql.DriverManager;
/*     */ import java.sql.SQLException;
/*     */ import java.text.MessageFormat;
/*     */ import java.util.Properties;
/*     */ 
/*     */ public abstract class SqlDbConnection
/*     */   implements DbConnection
/*     */ {
/*     */   protected Connection connection;
/*     */   protected final Properties commands;
/*     */   protected final Properties credentials;
/*     */ 
/*     */   protected SqlDbConnection()
/*     */   {
/*  62 */     this.commands = loadProperties("commands");
/*  63 */     this.credentials = loadProperties("credentials");
/*     */   }
/*     */ 
/*     */   public void connect() throws SQLException
/*     */   {
/*  68 */     if (isConnected()) return;
/*     */     try {
/*  70 */       Class.forName(this.credentials.getProperty("jdbcDriver"));
/*  71 */       this.connection = DriverManager.getConnection(
/*  72 */         this.credentials.getProperty("jdbcURL"), 
/*  73 */         this.credentials.getProperty("userName"), 
/*  74 */         this.credentials.getProperty("userPassword"));
/*     */ 
/*  76 */       initialize();
/*     */     }
/*     */     catch (ClassNotFoundException e) {
/*  79 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void disconnect()
/*     */   {
/*     */     try {
/*  86 */       this.connection.close();
/*     */     }
/*     */     catch (SQLException e) {
/*  89 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   public final Connection getConnection() {
/*  94 */     return this.connection;
/*     */   }
/*     */ 
/*     */   public final Properties getSqlCommands() {
/*  98 */     return this.commands;
/*     */   }
/*     */ 
/*     */   public final boolean isConnected() {
/* 102 */     return this.connection != null;
/*     */   }
/*     */ 
/*     */   protected abstract String getSqlHomeDir();
/*     */ 
/*     */   protected abstract void initialize();
/*     */ 
/*     */   protected Properties loadProperties(String fromFile)
/*     */   {
/* 117 */     String file = getSqlHomeDir() + "/" + fromFile;
/*     */     try {
/* 119 */       Properties props = new Properties();
/* 120 */       InputStream propsIn = super.getClass().getResourceAsStream(file);
/* 121 */       BufferedInputStream bis = new BufferedInputStream(propsIn);
/* 122 */       if (bis != null) {
/* 123 */         props.load(bis);
/* 124 */         bis.close();
/*     */       }
/* 126 */       return props;
/*     */     } catch (IOException e) {
/* 128 */       throw new RuntimeException("Couldn't read properties from '" + file + 
/* 129 */         "' !!");
/*     */     }
/*     */   }
/*     */ 
/*     */   protected final String buildQuery(Properties props, String key, String[] params)
/*     */   {
/* 135 */     String format = props.getProperty(key);
/* 136 */     if (format != null) {
/* 137 */       String result = MessageFormat.format(format, params);
/* 138 */       return result;
/*     */     }
/* 140 */     return null;
/*     */   }
/*     */ }

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi.jar
 * Qualified Name:     org.palo.viewapi.SqlDbConnection
 * JD-Core Version:    0.5.4
 */