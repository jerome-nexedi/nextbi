/*     */ package com.tensegrity.palojava.impl;
/*     */ 
/*     */ import com.tensegrity.palojava.ConnectionInfo;
/*     */ import java.util.HashMap;
/*     */ 
/*     */ public class ConnectionInfoImpl
/*     */   implements ConnectionInfo
/*     */ {
/*     */   private final String host;
/*     */   private final String port;
/*     */   private String username;
/*     */   private String password;
/*  56 */   private final HashMap<String, Object> dataMap = new HashMap();
/*     */ 
/*     */   public ConnectionInfoImpl(String host, String port) {
/*  59 */     this(host, port, null, null);
/*     */   }
/*     */ 
/*     */   public ConnectionInfoImpl(String host, String port, String username, String password)
/*     */   {
/*  64 */     this.host = host;
/*  65 */     this.port = port;
/*  66 */     this.username = username;
/*  67 */     this.password = password;
/*     */   }
/*     */ 
/*     */   public final String getHost() {
/*  71 */     return this.host;
/*     */   }
/*     */ 
/*     */   public final synchronized String getPassword() {
/*  75 */     return this.password;
/*     */   }
/*     */ 
/*     */   public final String getPort() {
/*  79 */     return this.port;
/*     */   }
/*     */ 
/*     */   public final synchronized String getUsername() {
/*  83 */     return this.username;
/*     */   }
/*     */ 
/*     */   public final synchronized void setPassword(String password) {
/*  87 */     this.password = password;
/*     */   }
/*     */ 
/*     */   public final synchronized void setUser(String username) {
/*  91 */     this.username = username;
/*     */   }
/*     */ 
/*     */   public final void setData(String id, Object data) {
/*  95 */     this.dataMap.put(id, data);
/*     */   }
/*     */ 
/*     */   public final Object getData(String id) {
/*  99 */     return this.dataMap.get(id);
/*     */   }
/*     */ 
/*     */   public final String toString() {
/* 103 */     StringBuffer str = new StringBuffer();
/* 104 */     str.append(this.host);
/* 105 */     str.append(":");
/* 106 */     str.append(this.port);
/* 107 */     str.append(" user:");
/* 108 */     str.append(this.username);
/*     */ 
/* 111 */     return str.toString();
/*     */   }
/*     */ 
/*     */   public final boolean equals(Object obj) {
/* 115 */     if (obj instanceof ConnectionInfo) {
/* 116 */       ConnectionInfo other = (ConnectionInfo)obj;
/* 117 */       boolean equals = (this.host.equals(other.getHost())) && 
/* 118 */         (this.port.equals(other.getPort()));
/*     */ 
/* 121 */       if (this.username != null)
/* 122 */         equals = (equals) && (this.username.equals(other.getUsername()));
/*     */       else {
/* 124 */         equals = (equals) && (other.getUsername() == null);
/*     */       }
/*     */ 
/* 127 */       if (this.password != null)
/* 128 */         equals = (equals) && (this.password.equals(other.getPassword()));
/*     */       else {
/* 130 */         equals = (equals) && (other.getPassword() == null);
/*     */       }
/* 132 */       return equals;
/*     */     }
/* 134 */     return false;
/*     */   }
/*     */ 
/*     */   public final int hashCode() {
/* 138 */     int hc = 23;
/* 139 */     hc += 37 * this.host.hashCode();
/* 140 */     hc += 37 * this.port.hashCode();
/* 141 */     if (this.username != null)
/* 142 */       hc += 37 * this.username.hashCode();
/* 143 */     if (this.password != null)
/* 144 */       hc += 37 * this.password.hashCode();
/* 145 */     return hc;
/*     */   }
/*     */ }

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.paloapi\lib\palo.jar
 * Qualified Name:     com.tensegrity.palojava.impl.ConnectionInfoImpl
 * JD-Core Version:    0.5.4
 */