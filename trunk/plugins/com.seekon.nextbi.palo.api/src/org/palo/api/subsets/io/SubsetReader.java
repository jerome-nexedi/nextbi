/*     */ package org.palo.api.subsets.io;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.URL;
/*     */ import javax.xml.parsers.SAXParser;
/*     */ import javax.xml.parsers.SAXParserFactory;
/*     */ import javax.xml.transform.stream.StreamSource;
/*     */ import javax.xml.validation.Schema;
/*     */ import javax.xml.validation.SchemaFactory;
/*     */ import javax.xml.validation.TypeInfoProvider;
/*     */ import javax.xml.validation.Validator;
/*     */ import javax.xml.validation.ValidatorHandler;
/*     */ import org.palo.api.Database;
/*     */ import org.palo.api.Dimension;
/*     */ import org.palo.api.PaloAPIException;
/*     */ import org.palo.api.exceptions.PaloIOException;
/*     */ import org.palo.api.subsets.Subset2;
/*     */ import org.palo.api.subsets.impl.SubsetHandlerImpl;
/*     */ import org.palo.api.subsets.io.xml.SubsetXMLHandler;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.XMLReader;
/*     */ import org.xml.sax.helpers.DefaultHandler;
/*     */ import org.xml.sax.helpers.XMLReaderFactory;
/*     */ 
/*     */ class SubsetReader
/*     */ {
/*  78 */   private static SubsetReader instance = new SubsetReader();
/*     */ 
/*  80 */   static final SubsetReader getInstance() { return instance; }
/*     */ 
/*     */ 
/*     */   final Subset2 fromXML(SubsetHandlerImpl handler, String name, InputStream input, int type)
/*     */     throws PaloIOException
/*     */   {
/* 101 */     Subset2 subset = null;
/*     */     try {
/* 103 */       String path2xsd = getXSD(input);
/* 104 */       if (path2xsd == null) {
/* 105 */         String dbName = handler.getDimension().getDatabase().getName();
/* 106 */         throw new PaloAPIException("Unknown subset version!\nDatabase: " + 
/* 107 */           dbName + "\nSubset: " + name);
/*     */       }
/*     */ 
/* 110 */       if (!isValid(input, path2xsd)) {
/* 111 */         String dbName = handler.getDimension().getDatabase().getName();
/* 112 */         throw new PaloAPIException("Not a valid subset xml definition!\nDatabase: " + 
/* 113 */           dbName + "\nSubset: " + name);
/*     */       }
/*     */ 
/* 116 */       StreamSource xml = new StreamSource(input);
/* 117 */       SchemaFactory sf = 
/* 118 */         SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
/* 119 */       Schema xsd = sf.newSchema(
/* 120 */         new StreamSource(SubsetReader.class.getResourceAsStream(path2xsd)));
/* 121 */       SubsetReaderErrorHandler errHandler = 
/* 122 */         new SubsetReaderErrorHandler();
/* 123 */       ValidatorHandler vHandler = xsd.newValidatorHandler();
/* 124 */       vHandler.setErrorHandler(errHandler);
/* 125 */       TypeInfoProvider typeProvider = vHandler.getTypeInfoProvider();
/* 126 */       SubsetXMLHandler subset2Handler = 
/* 127 */         new SubsetXMLHandler(typeProvider, handler, name, type);
/* 128 */       vHandler.setContentHandler(subset2Handler);
/*     */ 
/* 130 */       XMLReader parser = XMLReaderFactory.createXMLReader();
/* 131 */       parser.setContentHandler(vHandler);
/* 132 */       parser.parse(new InputSource(xml.getInputStream()));
/* 133 */       subset = subset2Handler.getSubset();
/*     */     }
/*     */     catch (SAXException e) {
/* 136 */       throw new PaloIOException("XML Exception during subset loading!", e);
/*     */     } catch (IOException e) {
/* 138 */       throw new PaloIOException("IOException during subset loading!", e);
/*     */     } catch (Exception e) {
/* 140 */       throw new PaloIOException("Exception during subset loading!", e);
/*     */     }
/* 142 */     return subset;
/*     */   }
/*     */ 
/*     */   private final boolean isValid(InputStream input, String path2xsd) throws SAXException, IOException
/*     */   {
/* 147 */     StreamSource xml = new StreamSource(input);
/*     */ 
/* 149 */     SchemaFactory sf = 
/* 150 */       SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
/* 151 */     Schema xsd = sf.newSchema(
/* 152 */       new StreamSource(SubsetReader.class.getResourceAsStream(path2xsd)));
/*     */ 
/* 154 */     SubsetReaderErrorHandler errHandler = new SubsetReaderErrorHandler();
/* 155 */     Validator validator = xsd.newValidator();
/* 156 */     validator.setErrorHandler(errHandler);
/*     */ 
/* 158 */     validator.validate(xml);
/*     */ 
/* 160 */     if (input.markSupported())
/* 161 */       input.reset();
/* 162 */     return !errHandler.hasErrors();
/*     */   }
/*     */ 
/*     */   private final String getSubsetVersion(InputStream input) throws IOException {
/* 166 */     final String[] version = { "" };
/*     */     try
/*     */     {
/* 169 */       SAXParserFactory sF = SAXParserFactory.newInstance();
/* 170 */       SAXParser parser = null;
/* 171 */       DefaultHandler annotationHandler = new DefaultHandler()
/*     */       {
/*     */         public void processingInstruction(String target, String data) throws SAXException {
/* 174 */           if (target.equals("palosubset")) {
/* 175 */             version[0] = 
/* 176 */               data.substring(9, data.length() - 1).trim();
/*     */           }
/* 178 */           super.processingInstruction(target, data);
/*     */         }
/*     */       };
/* 182 */       parser = sF.newSAXParser();
/* 183 */       parser.parse(input, annotationHandler);
/*     */     }
/*     */     catch (Exception localException)
/*     */     {
/*     */     }
/* 188 */     if (input.markSupported())
/* 189 */       input.reset();
/* 190 */     return version[0];
/*     */   }
/*     */ 
/*     */   private final String getXSD(InputStream input) throws IOException {
/* 194 */     String version = getSubsetVersion(input);
/* 195 */     StringBuffer path = new StringBuffer();
/*     */ 
/* 197 */     path.append("xml/schemas/subset_");
/* 198 */     path.append(version);
/* 199 */     path.append(".xsd");
/* 200 */     String path2xsd = path.toString();
/*     */ 
/* 202 */     URL rsc = SubsetReader.class.getResource(path2xsd);
/* 203 */     if (rsc == null)
/* 204 */       return null;
/* 205 */     return path2xsd;
/*     */   }
/*     */ }

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name:     org.palo.api.subsets.io.SubsetReader
 * JD-Core Version:    0.5.4
 */