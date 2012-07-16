/*     */ package org.palo.api.impl.xml;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.helpers.DefaultHandler;
/*     */ 
/*     */ public class BaseXMLHandler extends DefaultHandler
/*     */ {
/*     */   private Map startHandlers;
/*     */   private Map endHandlers;
/*     */   private ArrayList stack;
/*     */   private boolean prefixPath;
/*     */ 
/*     */   public BaseXMLHandler()
/*     */   {
/*  60 */     this.stack = new ArrayList();
/*  61 */     this.startHandlers = new HashMap();
/*  62 */     this.endHandlers = new HashMap();
/*  63 */     this.prefixPath = false;
/*     */   }
/*     */ 
/*     */   public BaseXMLHandler(boolean prefixPath) {
/*  67 */     this.prefixPath = prefixPath;
/*  68 */     this.stack = new ArrayList();
/*  69 */     this.startHandlers = new HashMap();
/*  70 */     this.endHandlers = new HashMap();
/*     */   }
/*     */ 
/*     */   public void putStartHandler(String path, StartHandler handler)
/*     */   {
/*  75 */     this.startHandlers.put(path, handler);
/*     */   }
/*     */ 
/*     */   public void putEndHandler(String path, EndHandler handler)
/*     */   {
/*  80 */     this.endHandlers.put(path, handler);
/*     */   }
/*     */ 
/*     */   private String getPath()
/*     */   {
/*  85 */     StringBuffer bf = new StringBuffer();
/*  86 */     for (int i = 0; i < this.stack.size(); ++i)
/*     */     {
/*  88 */       bf.append(this.stack.get(i));
/*  89 */       if (i < this.stack.size() - 1)
/*  90 */         bf.append("/");
/*     */     }
/*  92 */     return bf.toString();
/*     */   }
/*     */ 
/*     */   private String getLastPathElement() {
/*  96 */     String lastElement = (String)this.stack.get(this.stack.size() - 1);
/*  97 */     return lastElement;
/*     */   }
/*     */ 
/*     */   public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
/*     */   {
/* 102 */     super.startElement(uri, localName, qName, attributes);
/* 103 */     this.stack.add(qName);
/*     */ 
/* 106 */     if (this.prefixPath)
/*     */     {
/*     */       StartHandler starthandler;
/* 107 */       if ((starthandler = (StartHandler)this.startHandlers.get(getLastPathElement())) != null)
/* 108 */         starthandler.startElement(uri, localName, qName, attributes);
/*     */     }
/*     */     else
/*     */     {
/*     */       StartHandler starthandler;
/* 110 */       if ((starthandler = (StartHandler)this.startHandlers.get(getPath())) != null)
/* 111 */         starthandler.startElement(uri, localName, qName, attributes);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void endElement(String uri, String localName, String qName) throws SAXException
/*     */   {
/* 117 */     super.endElement(uri, localName, qName);
/*     */ 
/* 119 */     if (this.prefixPath)
/*     */     {
/*     */       EndHandler endhandler;
/* 120 */       if ((endhandler = (EndHandler)this.endHandlers.get(getLastPathElement())) != null)
/* 121 */         endhandler.endElement(uri, localName, qName);
/*     */     }
/*     */     else
/*     */     {
/*     */       EndHandler endhandler;
/* 123 */       if ((endhandler = (EndHandler)this.endHandlers.get(getPath())) != null) {
/* 124 */         endhandler.endElement(uri, localName, qName);
/*     */       }
/*     */     }
/* 127 */     if (!this.stack.isEmpty())
/* 128 */       this.stack.remove(this.stack.size() - 1);
/*     */   }
/*     */ 
/*     */   protected final void clearAllHandlers() {
/* 132 */     this.endHandlers.clear();
/* 133 */     this.startHandlers.clear();
/*     */   }
/*     */ }

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name:     org.palo.api.impl.xml.BaseXMLHandler
 * JD-Core Version:    0.5.4
 */