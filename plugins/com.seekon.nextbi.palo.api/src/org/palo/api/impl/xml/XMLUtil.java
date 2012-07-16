/*     */ package org.palo.api.impl.xml;
/*     */ 
/*     */ public class XMLUtil
/*     */ {
/*     */   public static final String quote(String str)
/*     */   {
/*  55 */     return "\"" + str + "\"";
/*     */   }
/*     */ 
/*     */   public static final String dequote(String str)
/*     */   {
/*  64 */     if (str.startsWith("\""))
/*  65 */       str = str.substring(1);
/*  66 */     if (str.endsWith("\""))
/*  67 */       str = str.substring(0, str.length() - 1);
/*  68 */     return str;
/*     */   }
/*     */ 
/*     */   public static String dequoteString(String strVal)
/*     */   {
/*  78 */     String newStr = strReplace(strVal, "&#13;", "\r");
/*  79 */     return strReplace(newStr, "&#10;", "\n");
/*     */   }
/*     */ 
/*     */   public static String quoteString(String strVal)
/*     */   {
/*  90 */     String newStr = strVal;
/*  91 */     newStr = strReplace(newStr, "\r", "&#13;");
/*  92 */     newStr = strReplace(newStr, "\n", "&#10;");
/*  93 */     return newStr;
/*     */   }
/*     */ 
/*     */   public static String strReplace(String string, String token, String replaceString)
/*     */   {
/*  98 */     String newStr = string;
/*  99 */     int i = string.indexOf(token);
/* 100 */     while (i > -1)
/*     */     {
/* 102 */       newStr = string.substring(0, i);
/* 103 */       newStr = newStr + replaceString;
/* 104 */       if (string.length() > i + token.length())
/*     */       {
/* 106 */         newStr = newStr + string.substring(i + token.length());
/*     */       }
/* 108 */       string = newStr;
/* 109 */       i = string.indexOf(token);
/*     */     }
/*     */ 
/* 112 */     return newStr;
/*     */   }
/*     */ 
/*     */   public static String printQuoted(int i) {
/* 116 */     return printQuoted(Integer.toString(i));
/*     */   }
/*     */ 
/*     */   public static String printQuoted(String s)
/*     */   {
/* 127 */     s = quoteString(s);
/*     */ 
/* 130 */     StringBuffer bf = new StringBuffer();
/* 131 */     int i = 0; for (int j = s.length(); i < j; ++i)
/*     */     {
/* 133 */       char c = s.charAt(i);
/* 134 */       switch (c)
/*     */       {
/*     */       case '"':
/*     */       case '&':
/*     */       case '<':
/* 139 */         bf.append("&#x");
/* 140 */         bf.append(Integer.toHexString(c));
/* 141 */         bf.append(';');
/* 142 */         break;
/*     */       default:
/* 145 */         bf.append(c);
/*     */       }
/*     */     }
/* 148 */     return bf.toString();
/*     */   }
/*     */ }

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name:     org.palo.api.impl.xml.XMLUtil
 * JD-Core Version:    0.5.4
 */