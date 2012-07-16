/*     */ package com.tensegrity.palojava.http.handlers;
/*     */ 
/*     */ import com.tensegrity.palojava.http.HttpConnection;
/*     */ import com.tensegrity.palojava.http.HttpParser;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.SocketException;
/*     */ 
/*     */ public class HeaderHandler
/*     */ {
/*  64 */   private static final HeaderHandler instance = new HeaderHandler();
/*     */   private int contentLength;
/*     */   private HttpConnection connection;
/*     */   private int errorCode;
/*     */   private String errorMessage;
/*     */ 
/*     */   public static final HeaderHandler getInstance(HttpConnection connection)
/*     */   {
/*  66 */     instance.use(connection);
/*  67 */     return instance;
/*     */   }
/*     */ 
/*     */   public final void parse(InputStream in)
/*     */     throws NumberFormatException, IOException, SocketException
/*     */   {
/*  96 */     String response = null;
/*     */     while (true) {
/*  98 */       response = HttpParser.readLine(in);
/*  99 */       if (response == null) return; if (response.trim().length() < 1) {
/*     */         return;
/*     */       }
/*     */ 
/* 103 */       if (response.startsWith("HTTP/1.1")) {
/* 104 */         String code = response.substring(9).trim();
/* 105 */         int index = code.indexOf(" ");
/* 106 */         setErrorCode(Integer.parseInt(code.substring(0, index)));
/* 107 */         setErrorMessage(code.substring(index).trim());
/*     */       }
/*     */ 
/* 110 */       if (response.startsWith("Content-Length:"))
/* 111 */         setContentLength(Integer.parseInt(response.substring(16)));
/* 112 */       if (response.startsWith("X-PALO-SV:"));
/* 113 */       this.connection.setServerToken(
/* 114 */         Integer.parseInt(response.substring(11)));
/*     */     }
/*     */   }
/*     */ 
/*     */   private final synchronized void use(HttpConnection connection)
/*     */   {
/* 139 */     this.connection = connection;
/*     */   }
/*     */ 
/*     */   private final synchronized void setErrorCode(int errorCode) {
/* 143 */     this.errorCode = errorCode;
/*     */   }
/*     */ 
/*     */   private final synchronized void setErrorMessage(String errorMessage) {
/* 147 */     this.errorMessage = errorMessage;
/*     */   }
/*     */ 
/*     */   public final synchronized int getContentLength()
/*     */   {
/* 155 */     return this.contentLength;
/*     */   }
/*     */ 
/*     */   public final synchronized int getErrorCode() {
/* 159 */     return this.errorCode;
/*     */   }
/*     */ 
/*     */   public final synchronized String getErrorMessage() {
/* 163 */     return this.errorMessage;
/*     */   }
/*     */ 
/*     */   private final synchronized void setContentLength(int contentLength)
/*     */   {
/* 190 */     this.contentLength = contentLength;
/*     */   }
/*     */ }

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.paloapi\lib\palo.jar
 * Qualified Name:     com.tensegrity.palojava.http.handlers.HeaderHandler
 * JD-Core Version:    0.5.4
 */