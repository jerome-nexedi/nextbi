/*     */ package com.tensegrity.palojava.http;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ 
/*     */ public class BoundedInputStream extends InputStream
/*     */ {
/*     */   private final int maxBytes;
/*     */   private int bytesRead;
/*     */   private boolean isClosed;
/*  49 */   private InputStream inStream = null;
/*     */ 
/*     */   public BoundedInputStream(InputStream inStream, int maxBytes) {
/*  52 */     this.maxBytes = maxBytes;
/*  53 */     this.inStream = inStream;
/*     */   }
/*     */ 
/*     */   public final synchronized int read() throws IOException {
/*  57 */     if (this.isClosed) {
/*  58 */       throw new IOException("InputStream is closed!");
/*     */     }
/*  60 */     if (this.bytesRead >= this.maxBytes) {
/*  61 */       return -1;
/*     */     }
/*  63 */     this.bytesRead += 1;
/*  64 */     return this.inStream.read();
/*     */   }
/*     */ 
/*     */   public final synchronized int read(byte[] b, int off, int len) throws IOException {
/*  68 */     if (this.isClosed) {
/*  69 */       throw new IOException("InputStream is closed!");
/*     */     }
/*  71 */     if (this.bytesRead >= this.maxBytes) {
/*  72 */       return -1;
/*     */     }
/*     */ 
/*  75 */     if (this.bytesRead + len > this.maxBytes) {
/*  76 */       len = this.maxBytes - this.bytesRead;
/*     */     }
/*  78 */     int count = this.inStream.read(b, off, len);
/*  79 */     this.bytesRead += count;
/*  80 */     return count;
/*     */   }
/*     */ 
/*     */   public final int read(byte[] b) throws IOException {
/*  84 */     return read(b, 0, b.length);
/*     */   }
/*     */ 
/*     */   public final synchronized void close() throws IOException {
/*  88 */     if (this.isClosed)
/*     */       return;
/*     */     try {
/*  91 */       byte[] bytes = new byte[1024];
/*  92 */       while (read(bytes) >= 0);
/*     */     }
/*     */     finally
/*     */     {
/*  96 */       this.isClosed = true;
/*     */     }
/*     */   }
/*     */ 
/*     */   public final synchronized long skip(long n) throws IOException
/*     */   {
/* 102 */     long length = Math.min(n, this.maxBytes - this.bytesRead);
/* 103 */     length = this.inStream.skip(length);
/* 104 */     if (length > 0L) {
/* 105 */       this.bytesRead = (int)(this.bytesRead + length);
/*     */     }
/* 107 */     return length;
/*     */   }
/*     */ }

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.paloapi\lib\palo.jar
 * Qualified Name:     com.tensegrity.palojava.http.BoundedInputStream
 * JD-Core Version:    0.5.4
 */