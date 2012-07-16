/*     */ package org.palo.api.impl.subsets;
/*     */ 
/*     */ import org.palo.api.Database;
/*     */ import org.palo.api.Dimension;
/*     */ import org.palo.api.Element;
/*     */ import org.palo.api.Hierarchy;
/*     */ import org.palo.api.PaloAPIException;
/*     */ import org.palo.api.SubsetState;
/*     */ import org.palo.api.impl.xml.IPaloEndHandler;
/*     */ import org.palo.api.impl.xml.IPaloStartHandler;
/*     */ import org.xml.sax.Attributes;
/*     */ 
/*     */ public class XMLSubsetHandlerLegacy extends XMLSubsetHandler
/*     */ {
/*     */   private final String key;
/*     */ 
/*     */   XMLSubsetHandlerLegacy(Database db, String key)
/*     */   {
/*  62 */     super(db);
/*  63 */     this.key = key;
/*     */   }
/*     */ 
/*     */   IPaloStartHandler[] getStartHandlers(final Database database) {
/*  67 */     return new IPaloStartHandler[] { new IPaloStartHandler() {
/*     */       public String getPath() {
/*  69 */         return "subset";
/*     */       }
/*     */ 
/*     */       public void startElement(String uri, String localName, String qName, Attributes attributes)
/*     */       {
/*  74 */         XMLSubsetHandlerLegacy.this.subsetBuilder = new SubsetBuilder();
/*  75 */         XMLSubsetHandlerLegacy.this.subsetBuilder.setId(XMLSubsetHandlerLegacy.this.key);
/*  76 */         String name = attributes.getValue("name");
/*  77 */         XMLSubsetHandlerLegacy.this.subsetBuilder.setName(name);
/*  78 */         XMLSubsetHandlerLegacy.this.subsetBuilder
/*  79 */           .setDescription(attributes.getValue("description"));
/*  80 */         XMLSubsetHandlerLegacy.this.subsetBuilder.setActiveState(
/*  81 */           attributes.getValue("activestrategy"));
/*  82 */         String srcDimId = attributes.getValue("sourceDimensionName");
/*  83 */         Dimension srcDim = database.getDimensionByName(srcDimId);
/*  84 */         if (srcDim == null)
/*  85 */           throw new PaloAPIException("Cannot find source dimension '" + 
/*  86 */             srcDimId + "'!!");
/*  87 */         XMLSubsetHandlerLegacy.this.subsetBuilder.setSourceHierarchy(srcDim.getDefaultHierarchy());
/*     */       }
/*     */     }
/*     */     , new IPaloStartHandler() {
/*     */       public String getPath() {
/*  91 */         return "subset/state";
/*     */       }
/*     */ 
/*     */       public void startElement(String uri, String localName, String qName, Attributes attributes)
/*     */       {
/*  97 */         XMLSubsetHandlerLegacy.this.stateBuilder = new SubsetStateBuilder();
/*  98 */         XMLSubsetHandlerLegacy.this.stateBuilder.setId(attributes.getValue("id"));
/*  99 */         XMLSubsetHandlerLegacy.this.stateBuilder.setName(attributes.getValue("name"));
/*     */       }
/*     */     }
/*     */     , new IPaloStartHandler() {
/*     */       public String getPath() {
/* 103 */         return "subset/regularexpression";
/*     */       }
/*     */ 
/*     */       public void startElement(String uri, String localName, String qName, Attributes attributes)
/*     */       {
/* 109 */         XMLSubsetHandlerLegacy.this.stateBuilder = new SubsetStateBuilder();
/* 110 */         XMLSubsetHandlerLegacy.this.stateBuilder.setId("regularexpression");
/* 111 */         XMLSubsetHandlerLegacy.this.stateBuilder.setName("Regular Expression");
/* 112 */         XMLSubsetHandlerLegacy.this.stateBuilder.setExpression(attributes.getValue("expression"));
/*     */       }
/*     */     }
/*     */     , new IPaloStartHandler() {
/*     */       public String getPath() {
/* 116 */         return "subset/flat";
/*     */       }
/*     */ 
/*     */       public void startElement(String uri, String localName, String qName, Attributes attributes)
/*     */       {
/* 122 */         XMLSubsetHandlerLegacy.this.stateBuilder = new SubsetStateBuilder();
/* 123 */         XMLSubsetHandlerLegacy.this.stateBuilder.setId("flat");
/* 124 */         XMLSubsetHandlerLegacy.this.stateBuilder.setName("Flat");
/*     */       }
/*     */     }
/*     */     , new IPaloStartHandler() {
/*     */       public String getPath() {
/* 128 */         return "subset/flat/element";
/*     */       }
/*     */ 
/*     */       public void startElement(String uri, String localName, String qName, Attributes attributes)
/*     */       {
/* 133 */         if ((XMLSubsetHandlerLegacy.this.stateBuilder == null) || (XMLSubsetHandlerLegacy.this.subsetBuilder == null)) {
/* 134 */           throw new PaloAPIException(
/* 135 */             "Cannot add elements to flat subset state!!");
/*     */         }
/* 137 */         String elementName = attributes.getValue("name");
/* 138 */         Hierarchy srcHier = XMLSubsetHandlerLegacy.this.subsetBuilder.getSourceHierarchy();
/* 139 */         Element element = srcHier.getElementByName(elementName);
/* 140 */         XMLSubsetHandlerLegacy.this.stateBuilder.addElement(element);
/*     */       }
/*     */     }
/*     */     , new IPaloStartHandler() {
/*     */       public String getPath() {
/* 144 */         return "subset/hierarchical";
/*     */       }
/*     */ 
/*     */       public void startElement(String uri, String localName, String qName, Attributes attributes)
/*     */       {
/* 150 */         XMLSubsetHandlerLegacy.this.stateBuilder = new SubsetStateBuilder();
/* 151 */         XMLSubsetHandlerLegacy.this.stateBuilder.setId("hierarchical");
/* 152 */         XMLSubsetHandlerLegacy.this.stateBuilder.setName("Hierarchical");
/*     */       }
/*     */     }
/*     */     , new IPaloStartHandler() {
/*     */       public String getPath() {
/* 156 */         return "subset/hierarchical/element";
/*     */       }
/*     */ 
/*     */       public void startElement(String uri, String localName, String qName, Attributes attributes)
/*     */       {
/* 161 */         if ((XMLSubsetHandlerLegacy.this.stateBuilder == null) || (XMLSubsetHandlerLegacy.this.subsetBuilder == null)) {
/* 162 */           throw new PaloAPIException(
/* 163 */             "Cannot add elements to hierarchical subset state!!");
/*     */         }
/* 165 */         String elementName = attributes.getValue("name");
/* 166 */         Hierarchy srcHier = XMLSubsetHandlerLegacy.this.subsetBuilder.getSourceHierarchy();
/* 167 */         Element element = srcHier.getElementByName(elementName);
/* 168 */         XMLSubsetHandlerLegacy.this.stateBuilder.addElement(element);
/*     */       }
/*     */     }
/*     */      };
/*     */   }
/*     */ 
/*     */   IPaloEndHandler[] getEndHandlers(Database database) {
/* 174 */     return new IPaloEndHandler[] { new IPaloEndHandler() {
/*     */       public String getPath() {
/* 176 */         return "subset/regularexpression";
/*     */       }
/*     */ 
/*     */       public void endElement(String uri, String localName, String qName) {
/* 180 */         if ((XMLSubsetHandlerLegacy.this.subsetBuilder == null) || (XMLSubsetHandlerLegacy.this.stateBuilder == null)) {
/* 181 */           throw new PaloAPIException("Cannot create subset state!!");
/*     */         }
/* 183 */         SubsetState state = XMLSubsetHandlerLegacy.this.stateBuilder.createState();
/* 184 */         XMLSubsetHandlerLegacy.this.subsetBuilder.addState(state);
/* 185 */         XMLSubsetHandlerLegacy.this.stateBuilder = null;
/*     */       }
/*     */     }
/*     */     , new IPaloEndHandler() {
/*     */       public String getPath() {
/* 189 */         return "subset/flat";
/*     */       }
/*     */ 
/*     */       public void endElement(String uri, String localName, String qName) {
/* 193 */         if ((XMLSubsetHandlerLegacy.this.subsetBuilder == null) || (XMLSubsetHandlerLegacy.this.stateBuilder == null)) {
/* 194 */           throw new PaloAPIException("Cannot create subset state!!");
/*     */         }
/* 196 */         SubsetState state = XMLSubsetHandlerLegacy.this.stateBuilder.createState();
/* 197 */         XMLSubsetHandlerLegacy.this.subsetBuilder.addState(state);
/* 198 */         XMLSubsetHandlerLegacy.this.stateBuilder = null;
/*     */       }
/*     */     }
/*     */     , new IPaloEndHandler() {
/*     */       public String getPath() {
/* 202 */         return "subset/hierarchical";
/*     */       }
/*     */ 
/*     */       public void endElement(String uri, String localName, String qName) {
/* 206 */         if ((XMLSubsetHandlerLegacy.this.subsetBuilder == null) || (XMLSubsetHandlerLegacy.this.stateBuilder == null)) {
/* 207 */           throw new PaloAPIException("Cannot create subset state!!");
/*     */         }
/* 209 */         SubsetState state = XMLSubsetHandlerLegacy.this.stateBuilder.createState();
/* 210 */         XMLSubsetHandlerLegacy.this.subsetBuilder.addState(state);
/* 211 */         XMLSubsetHandlerLegacy.this.stateBuilder = null;
/*     */       }
/*     */     }
/*     */      };
/*     */   }
/*     */ }

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name:     org.palo.api.impl.subsets.XMLSubsetHandlerLegacy
 * JD-Core Version:    0.5.4
 */