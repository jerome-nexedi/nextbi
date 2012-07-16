/*     */ package com.tensegrity.palojava.http;
/*     */ 
/*     */ import com.tensegrity.palojava.PaloException;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.net.URLEncoder;
/*     */ import java.util.ArrayList;
/*     */ import java.util.regex.Pattern;
/*     */ 
/*     */ public class HttpParser
/*     */ {
/*     */   public static final String DEFAULT_CHARACTER_ENCODING = "UTF-8";
/*     */   private static final char QUOTE = '"';
/*     */   private static final char TAG_START = '<';
/*     */   private static final char TAG_END = '>';
/*     */   private static final char AMPERSAND = '&';
/*     */   private static final char SEMICOLON = ';';
/*  63 */   private static final Pattern lineEnd = Pattern.compile(";[\r\n]");
/*     */ 
/*     */   public static final String readLine(InputStream in) throws IOException {
/*  66 */     return readLine(in, "UTF-8");
/*     */   }
/*     */ 
/*     */   public static final String readLine(InputStream in, String charset) throws IOException {
/*  70 */     byte[] rawdata = readBytes(in);
/*  71 */     if (rawdata == null) {
/*  72 */       return null;
/*     */     }
/*     */ 
/*  75 */     int len = rawdata.length;
/*  76 */     int offset = 0;
/*  77 */     if ((len > 0) && 
/*  78 */       (rawdata[(len - 1)] == 10)) {
/*  79 */       ++offset;
/*  80 */       if ((len > 1) && 
/*  81 */         (rawdata[(len - 2)] == 13)) {
/*  82 */         ++offset;
/*     */       }
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/*  88 */       return new String(rawdata, 0, len - offset, charset); } catch (UnsupportedEncodingException e) {
/*     */     }
/*  90 */     return new String(rawdata, 0, len - offset);
/*     */   }
/*     */ 
/*     */   public static final byte[] readBytes(InputStream in) throws IOException
/*     */   {
/*  95 */     ByteArrayOutputStream buf = new ByteArrayOutputStream();
/*     */     int ch;
/*  97 */     while ((ch = in.read()) >= 0)
/*     */     {
/*  98 */       buf.write(ch);
/*  99 */       if (ch == 10) {
/*     */         break;
/*     */       }
/*     */     }
/* 103 */     if (buf.size() == 0) {
/* 104 */       return null;
/*     */     }
/* 106 */     return buf.toByteArray();
/*     */   }
/*     */ 
/*     */   public static final String readRawLine(InputStream in) throws IOException {
/* 110 */     ByteArrayOutputStream buf = new ByteArrayOutputStream();
/*     */ 
/* 112 */     int lastCh = -1;
/* 113 */     boolean inQuote = false;
/*     */     int ch;
/* 114 */     while ((ch = in.read()) >= 0)
/*     */     {
/* 116 */       buf.write(ch);
/* 117 */       if (ch == 34) {
/* 118 */         inQuote = !inQuote;
/*     */       }
/* 120 */       if ((ch == 10) && (lastCh == 59) && (!inQuote))
/*     */         break;
/* 122 */       lastCh = ch;
/*     */     }
/* 124 */     if (buf.size() == 0)
/* 125 */       return null;
/*     */     try
/*     */     {
/* 128 */       return new String(buf.toByteArray(), "UTF-8"); } catch (UnsupportedEncodingException e) {
/*     */     }
/* 130 */     return new String(buf.toByteArray());
/*     */   }
/*     */ 
/*     */   protected static final String encode(String paramValue)
/*     */   {
/*     */     try
/*     */     {
/* 142 */       return URLEncoder.encode(paramValue, "UTF-8");
/*     */     } catch (UnsupportedEncodingException e) {
/* 144 */       e.printStackTrace();
/*     */     }
/* 146 */     return paramValue;
/*     */   }
/*     */ 
/*     */   protected static final String encode(Object[] paramValue)
/*     */   {
/* 157 */     StringBuffer res = new StringBuffer();
/* 158 */     int max = paramValue.length - 1;
/* 159 */     for (int i = 0; i < paramValue.length; ++i) {
/* 160 */       res.append(encode(paramValue[i].toString()));
/* 161 */       if (i < max)
/* 162 */         res.append(":");
/*     */     }
/* 164 */     return res.toString();
/*     */   }
/*     */ 
/*     */   public static final String[][] parse(String response, char delim)
/*     */   {
/* 176 */     String[] lines = lineEnd.split(response, 0);
/* 177 */     String[][] res = new String[lines.length][];
/* 178 */     for (int i = 0; i < lines.length; ++i)
/*     */     {
/* 180 */       lines[i] = (lines[i] + ";");
/* 181 */       res[i] = parseLine(lines[i], delim);
/*     */     }
/*     */ 
/* 184 */     return res;
/*     */   }
/*     */ 
/*     */   public static final synchronized String[] parseLine(String str, char delim)
/*     */   {
/* 194 */     ArrayList entries = new ArrayList();
/* 195 */     StringBuffer entry = new StringBuffer();
/* 196 */     char current = ' ';
/*     */ 
/* 198 */     boolean inQuotes = false;
/* 199 */     boolean inField = true;
/* 200 */     boolean inTag = false;
/* 201 */     boolean readAmpersand = false;
/* 202 */     str = str.trim();
/* 203 */     int i = 0; for (int n = str.length(); i < n; ++i) {
/* 204 */       current = str.charAt(i);
/* 205 */       char next = (i + 1 < n) ? str.charAt(i + 1) : ' ';
/*     */ 
/* 207 */       if (inField) {
/* 208 */         if (current == '"') {
/* 209 */           if (!inTag)
/*     */           {
/* 211 */             if (!inQuotes)
/*     */             {
/* 213 */               inQuotes = true;
/* 214 */             } else if (next == '"')
/*     */             {
/* 216 */               entry.append('"');
/* 217 */               ++i;
/*     */             }
/*     */             else {
/* 220 */               inQuotes = false;
/*     */             }
/*     */           }
/*     */           else
/* 224 */             entry.append('"');
/*     */         }
/* 226 */         else if ((current == '<') && (!inQuotes)) {
/* 227 */           if (!inTag) {
/* 228 */             inTag = true;
/* 229 */             entry.append('<');
/*     */           }
/* 231 */         } else if ((current == '>') && (!inQuotes)) {
/* 232 */           if (inTag) {
/* 233 */             inTag = false;
/* 234 */             entry.append('>');
/*     */           }
/* 236 */         } else if ((current == '&') && (!inQuotes)) {
/* 237 */           readAmpersand = true;
/* 238 */           entry.append('&');
/* 239 */         } else if ((current == ';') && (readAmpersand)) {
/* 240 */           readAmpersand = false;
/* 241 */           entry.append(';');
/*     */         }
/* 244 */         else if ((current == delim) && (!inQuotes) && (!inTag))
/*     */         {
/* 247 */           entries.add(entry.toString());
/* 248 */           entry.delete(0, entry.length());
/*     */         }
/*     */         else {
/* 251 */           entry.append(current);
/*     */         }
/*     */ 
/*     */       }
/* 256 */       else if (current == delim) {
/* 257 */         inField = true;
/*     */       }
/*     */     }
/* 260 */     if (entry.length() > 0) {
/* 261 */       entries.add(entry.toString());
/*     */     }
/* 263 */     return (String[])entries.toArray(new String[entries.size()]);
/*     */   }
/*     */ 
/*     */   public static final void checkResponse(String[] response)
/*     */   {
/* 273 */     if (response.length < 1)
/* 274 */       throw new PaloException("No response from server!!");
/* 275 */     if (response[0].startsWith("ERROR")) {
/* 276 */       response[0] = response[0].substring(5);
/* 277 */       String errCode = response[0];
/* 278 */       if ((!PaloErrorCodes.contains(errCode)) || 
/* 279 */         (response.length < 3)) return;
/* 280 */       String errMsg = response[1];
/* 281 */       String errReason = response[2];
/*     */ 
/* 283 */       if (isString(errMsg))
/* 284 */         throw new PaloException(errCode, errMsg, errReason);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static final boolean isString(String str)
/*     */   {
/* 291 */     return !Character.isDigit(str.trim().charAt(0));
/*     */   }
/*     */ }

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.paloapi\lib\palo.jar
 * Qualified Name:     com.tensegrity.palojava.http.HttpParser
 * JD-Core Version:    0.5.4
 */