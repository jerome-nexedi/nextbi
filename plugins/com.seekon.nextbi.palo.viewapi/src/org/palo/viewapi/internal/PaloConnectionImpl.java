/*     */ package org.palo.viewapi.internal;
/*     */ 
/*     */ import org.palo.viewapi.PaloConnection;
/*     */ 
/*     */ public class PaloConnectionImpl extends DomainObjectImpl
/*     */   implements PaloConnection
/*     */ {
/*     */   private int type;
/*     */   private String name;
/*     */   private String host;
/*     */   private String service;
/*     */   private String description;
/*     */ 
/*     */   PaloConnectionImpl(String id)
/*     */   {
/*  55 */     super(id);
/*     */   }
/*     */   private PaloConnectionImpl(Builder builder) {
/*  58 */     super(builder.id);
/*  59 */     this.type = builder.type;
/*  60 */     this.name = builder.name;
/*  61 */     this.host = builder.host;
/*  62 */     this.service = builder.service;
/*  63 */     this.description = builder.description;
/*     */   }
/*     */ 
/*     */   public final String getDescription() {
/*  67 */     return (this.description != null) ? this.description : "";
/*     */   }
/*     */ 
/*     */   public final String getHost() {
/*  71 */     return (this.host != null) ? this.host : "";
/*     */   }
/*     */ 
/*     */   public final String getName() {
/*  75 */     return (this.name != null) ? this.name : "";
/*     */   }
/*     */ 
/*     */   public final String getService() {
/*  79 */     return (this.service != null) ? this.service : "";
/*     */   }
/*     */ 
/*     */   public final int getType() {
/*  83 */     return this.type;
/*     */   }
/*     */ 
/*     */   public final boolean equals(Object obj) {
/*  87 */     if (obj instanceof PaloConnection) {
/*  88 */       PaloConnection other = (PaloConnection)obj;
/*     */ 
/*  92 */       return (getId().equals(other.getId())) && 
/*  90 */         (getType() == other.getType()) && 
/*  91 */         (getHost().equals(other.getHost())) && 
/*  92 */         (getService().equals(other.getService()));
/*     */     }
/*  94 */     return false;
/*     */   }
/*     */ 
/*     */   public final int hashCode() {
/*  98 */     int hc = 17;
/*  99 */     hc += 31 * this.type;
/* 100 */     hc += 31 * getId().hashCode();
/* 101 */     hc += 31 * getHost().hashCode();
/* 102 */     hc += 31 * getService().hashCode();
/* 103 */     return hc;
/*     */   }
/*     */ 
/*     */   final void setDescription(String description)
/*     */   {
/* 110 */     this.description = description;
/*     */   }
/*     */   final void setHost(String host) {
/* 113 */     this.host = host;
/*     */   }
/*     */   final void setName(String name) {
/* 116 */     this.name = name;
/*     */   }
/*     */   final void setService(String service) {
/* 119 */     this.service = service;
/*     */   }
/*     */   final void setType(int type) {
/* 122 */     this.type = type;
/*     */   }
/*     */ 
/*     */   public static final class Builder
/*     */   {
/*     */     private final String id;
/*     */     private int type;
/*     */     private String name;
/*     */     private String host;
/*     */     private String service;
/*     */     private String description;
/*     */ 
/*     */     public Builder(String id)
/*     */     {
/* 139 */       AccessController.checkAccess(PaloConnection.class);
/* 140 */       this.id = id;
/*     */     }
/*     */ 
/*     */     public Builder type(int type) {
/* 144 */       this.type = type;
/* 145 */       return this;
/*     */     }
/*     */     public Builder name(String name) {
/* 148 */       this.name = name;
/* 149 */       return this;
/*     */     }
/*     */     public Builder host(String host) {
/* 152 */       this.host = host;
/* 153 */       return this;
/*     */     }
/*     */     public Builder service(String service) {
/* 156 */       this.service = service;
/* 157 */       return this;
/*     */     }
/*     */     public Builder description(String description) {
/* 160 */       this.description = description;
/* 161 */       return this;
/*     */     }
/*     */     public PaloConnection build() {
/* 164 */       return new PaloConnectionImpl(this);
/*     */     }
/*     */   }
/*     */ }

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi.jar
 * Qualified Name:     org.palo.viewapi.internal.PaloConnectionImpl
 * JD-Core Version:    0.5.4
 */