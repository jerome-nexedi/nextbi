/*    */ package org.palo.api.impl;
/*    */ 
/*    */ import com.tensegrity.palojava.ElementInfo;
/*    */ import java.io.PrintStream;
/*    */ import org.palo.api.Element;
/*    */ 
/*    */ class Util
/*    */ {
/*    */   public static void noopWarning()
/*    */   {
/* 49 */     System.err.println("no-op");
/*    */   }
/*    */ 
/*    */   public static final ElementInfo[] getElementInfos(Element[] elements) {
/* 53 */     int index = 0;
/* 54 */     ElementInfo[] infos = new ElementInfo[elements.length];
/* 55 */     Element[] arrayOfElement = elements; int j = elements.length; for (int i = 0; i < j; ++i) { Element el = arrayOfElement[i];
/* 56 */       infos[(index++)] = ((ElementImpl)el).getInfo(); }
/* 57 */     return infos;
/*    */   }
/*    */ 
/*    */   public static final ElementInfo[][] getElementInfos(Element[][] elements) {
/* 61 */     ElementInfo[][] infos = new ElementInfo[elements.length][];
/* 62 */     for (int index = 0; index < elements.length; ++index)
/* 63 */       infos[index] = getElementInfos(elements[index]);
/* 64 */     return infos;
/*    */   }
/*    */ }

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name:     org.palo.api.impl.Util
 * JD-Core Version:    0.5.4
 */