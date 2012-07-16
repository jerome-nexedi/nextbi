/*     */ package com.tensegrity.palojava.http.handlers;
/*     */ 
/*     */ import com.tensegrity.palojava.ElementInfo;
/*     */ import com.tensegrity.palojava.PaloException;
/*     */ import com.tensegrity.palojava.PaloInfo;
/*     */ import com.tensegrity.palojava.http.HttpConnection;
/*     */ import com.tensegrity.palojava.http.HttpParser;
/*     */ import java.io.IOException;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.net.ConnectException;
/*     */ import java.net.URLEncoder;
/*     */ 
/*     */ class HttpHandler
/*     */ {
/*     */   private static final String CHARACTER_ENCODING = "UTF-8";
/*     */   private static final String ID_PATH_DELIM = ",";
/*     */   private static final String PATH_DELIM = ":";
/*     */   private static final int PARAMETER_THRESHOLD = 1024;
/*     */   private static final String GET = "GET ";
/*     */   private static final String POST = "POST ";
/*     */   private static final String HTTP_VERSION = " HTTP/1.1";
/*     */   private static final String LINE_END = "\r\n";
/*     */   private static final String SID = "&sid=";
/*     */   protected static final String CUBE_PREFIX = "&cube=";
/*     */   protected static final String SYSTEM_PREFIX = "#_";
/*     */   protected static final String SYSTEM_POSTFIX = "_";
/*     */   protected static final String OK = "1";
/*     */   protected HttpConnection connection;
/*     */ 
/*     */   final synchronized void use(HttpConnection connection)
/*     */   {
/*  81 */     this.connection = connection;
/*     */   }
/*     */ 
/*     */   protected final String[][] request(String query)
/*     */     throws ConnectException, IOException
/*     */   {
/*  94 */     return request(query, false, false);
/*     */   }
/*     */ 
/*     */   protected final synchronized String[][] request(String query, boolean doPost, boolean skipSid)
/*     */     throws ConnectException, IOException
/*     */   {
/* 110 */     if (query.getBytes().length > 1024) {
/* 111 */       doPost = true;
/*     */     }
/*     */ 
/* 114 */     String sid = this.connection.getSID();
/* 115 */     if ((!skipSid) && (((sid == null) || (sid.equals("")))))
/* 116 */       throw new PaloException("No session id defined!!");
/* 117 */     StringBuffer request = new StringBuffer();
/* 118 */     if (doPost)
/* 119 */       request.append("POST ");
/*     */     else
/* 121 */       request.append("GET ");
/* 122 */     request.append(query);
/* 123 */     if (!skipSid) {
/* 124 */       request.append("&sid=");
/* 125 */       request.append(sid);
/*     */     }
/* 127 */     request.append(" HTTP/1.1");
/* 128 */     request.append("\r\n");
/* 129 */     String[] response = this.connection.send(request.toString());
/* 130 */     return parse(response);
/*     */   }
/*     */ 
/*     */   protected final String encode(String paramValue)
/*     */   {
/*     */     try
/*     */     {
/* 141 */       return URLEncoder.encode(paramValue, "UTF-8");
/*     */     } catch (UnsupportedEncodingException e) {
/* 143 */       e.printStackTrace();
/*     */     }
/* 145 */     return paramValue;
/*     */   }
/*     */ 
/*     */   protected final String encode(Object[] paramValue)
/*     */   {
/* 156 */     return encode(paramValue, ':');
/*     */   }
/*     */ 
/*     */   protected final String encode(Object[] paramValue, char delim) {
/* 160 */     StringBuffer res = new StringBuffer();
/* 161 */     int max = paramValue.length - 1;
/* 162 */     for (int i = 0; i < paramValue.length; ++i) {
/* 163 */       String val = paramValue[i].toString();
/* 164 */       if (!(paramValue[i] instanceof Number))
/*     */       {
/* 167 */         val = val.replaceAll("\"", "\"\"");
/* 168 */         val = encode(val);
/* 169 */         res.append("%22");
/* 170 */         res.append(val);
/* 171 */         res.append("%22");
/*     */       } else {
/* 173 */         res.append(encode(val));
/* 174 */       }if (i < max)
/* 175 */         res.append(delim);
/*     */     }
/* 177 */     return res.toString();
/*     */   }
/*     */ 
/*     */   protected final String getIdString(PaloInfo[] infos)
/*     */   {
/* 186 */     return getIdString(infos, ",");
/*     */   }
/*     */ 
/*     */   protected final String getNameString(ElementInfo[] infos) {
/* 190 */     return getNameString(infos, ",");
/*     */   }
/*     */ 
/*     */   protected final String getIdString(PaloInfo[] infos, String idDelimeter) {
/* 194 */     StringBuffer idStr = new StringBuffer();
/* 195 */     int lastId = infos.length - 1;
/* 196 */     for (int i = 0; i < infos.length; ++i) {
/* 197 */       idStr.append(infos[i].getId());
/* 198 */       if (i < lastId)
/* 199 */         idStr.append(idDelimeter);
/*     */     }
/* 201 */     return idStr.toString();
/*     */   }
/*     */ 
/*     */   protected final String getNameString(ElementInfo[] infos, String idDelimeter) {
/* 205 */     StringBuffer idStr = new StringBuffer();
/* 206 */     int lastId = infos.length - 1;
/* 207 */     for (int i = 0; i < infos.length; ++i)
/*     */     {
/* 215 */       idStr.append(infos[i].getName());
/* 216 */       if (i < lastId)
/* 217 */         idStr.append(idDelimeter);
/*     */     }
/* 219 */     return idStr.toString();
/*     */   }
/*     */ 
/*     */   protected final String getIdString(String[] ids) {
/* 223 */     StringBuffer idStr = new StringBuffer();
/* 224 */     int lastId = ids.length - 1;
/* 225 */     for (int i = 0; i < ids.length; ++i) {
/* 226 */       idStr.append(ids[i]);
/* 227 */       if (i < lastId)
/* 228 */         idStr.append(",");
/*     */     }
/* 230 */     return idStr.toString();
/*     */   }
/*     */ 
/*     */   protected final String getPaths(PaloInfo[][] coords)
/*     */   {
/* 239 */     return getPaths(coords, ":");
/*     */   }
/*     */ 
/*     */   protected final String getPaths(PaloInfo[][] coords, String pathDelim) {
/* 243 */     StringBuffer pathStr = new StringBuffer();
/* 244 */     int lastCoordinate = coords.length - 1;
/* 245 */     for (int i = 0; i < coords.length; ++i) {
/* 246 */       pathStr.append(getIdString(coords[i]));
/* 247 */       if (i < lastCoordinate)
/* 248 */         pathStr.append(pathDelim);
/*     */     }
/* 250 */     return pathStr.toString();
/*     */   }
/*     */ 
/*     */   protected final String getWeightString(double[] weights) {
/* 254 */     StringBuffer weightStr = new StringBuffer();
/* 255 */     int lastWeight = weights.length - 1;
/* 256 */     for (int i = 0; i < weights.length; ++i) {
/* 257 */       weightStr.append(weights[i]);
/* 258 */       if (i < lastWeight)
/* 259 */         weightStr.append(",");
/*     */     }
/* 261 */     return weightStr.toString();
/*     */   }
/*     */ 
/*     */   protected final String getWeightString(Double[] weights) {
/* 265 */     StringBuffer weightStr = new StringBuffer();
/* 266 */     int lastWeight = weights.length - 1;
/* 267 */     for (int i = 0; i < weights.length; ++i) {
/* 268 */       weightStr.append(weights[i]);
/* 269 */       if (i < lastWeight)
/* 270 */         weightStr.append(",");
/*     */     }
/* 272 */     return weightStr.toString();
/*     */   }
/*     */ 
/*     */   private final String[][] parse(String[] response)
/*     */   {
/* 279 */     if (response == null) {
/* 280 */       return new String[0][];
/*     */     }
/* 282 */     String[][] res = new String[response.length][];
/* 283 */     for (int i = 0; i < response.length; ++i) {
/* 284 */       String resp = response[i];
/* 285 */       res[i] = HttpParser.parseLine(resp, ';');
/* 286 */       HttpParser.checkResponse(res[i]);
/*     */     }
/* 288 */     return res;
/*     */   }
/*     */ }

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.paloapi\lib\palo.jar
 * Qualified Name:     com.tensegrity.palojava.http.handlers.HttpHandler
 * JD-Core Version:    0.5.4
 */