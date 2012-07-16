/*     */ package org.palo.api.impl.subsets;
/*     */ 
/*     */ import com.tensegrity.palojava.PaloException;
/*     */ import java.io.PrintStream;
/*     */ import org.palo.api.Attribute;
/*     */ import org.palo.api.Connection;
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
/*     */ class XMLSubsetHandler1_0 extends XMLSubsetHandler
/*     */ {
/*     */   XMLSubsetHandler1_0(Database database)
/*     */   {
/*  63 */     super(database);
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
/*  74 */         XMLSubsetHandler1_0.this.subsetBuilder = new SubsetBuilder();
/*  75 */         XMLSubsetHandler1_0.this.subsetBuilder.setId(attributes.getValue("id"));
/*  76 */         XMLSubsetHandler1_0.this.subsetBuilder.setName(attributes.getValue("name"));
/*  77 */         XMLSubsetHandler1_0.this.subsetBuilder
/*  78 */           .setDescription(attributes.getValue("description"));
/*  79 */         XMLSubsetHandler1_0.this.subsetBuilder.setActiveState(
/*  80 */           attributes.getValue("activeStateId"));
/*  81 */         String srcDimensionId = attributes
/*  82 */           .getValue("sourceDimensionId");
/*  83 */         Dimension srcDim = database.getDimensionByName(srcDimensionId);
/*  84 */         if (srcDim == null)
/*  85 */           throw new PaloAPIException("Cannot find source dimension '" + 
/*  86 */             srcDimensionId + "'!!");
/*     */         try
/*     */         {
/*  89 */           Hierarchy srcHier = srcDim.getDefaultHierarchy();
/*  90 */           if (!srcDim.getDatabase().getConnection().isLegacy())
/*  91 */             XMLSubsetHandler1_0.this.subsetBuilder.setAlias(XMLSubsetHandler1_0.this
/*  92 */               .getAttributeByName(srcHier, 
/*  92 */               attributes.getValue("alias")));
/*     */         } catch (PaloException pe) {
/*  94 */           System.err
/*  95 */             .println("SubsetReader: cannot read attributes - " + 
/*  96 */             pe.getMessage());
/*     */         }
/*  98 */         XMLSubsetHandler1_0.this.subsetBuilder.setSourceHierarchy(srcDim.getDefaultHierarchy());
/*     */       }
/*     */     }
/*     */     , new IPaloStartHandler() {
/*     */       public String getPath() {
/* 102 */         return "subset/state";
/*     */       }
/*     */ 
/*     */       public void startElement(String uri, String localName, String qName, Attributes attributes)
/*     */       {
/* 108 */         XMLSubsetHandler1_0.this.stateBuilder = new SubsetStateBuilder();
/* 109 */         XMLSubsetHandler1_0.this.stateBuilder.setId(attributes.getValue("id"));
/* 110 */         XMLSubsetHandler1_0.this.stateBuilder.setName(attributes.getValue("name"));
/*     */       }
/*     */     }
/*     */     , new IPaloStartHandler() {
/*     */       public String getPath() {
/* 114 */         return "subset/state/expression";
/*     */       }
/*     */ 
/*     */       public void startElement(String uri, String localName, String qName, Attributes attributes)
/*     */       {
/* 119 */         if (XMLSubsetHandler1_0.this.stateBuilder == null) {
/* 120 */           throw new PaloAPIException(
/* 121 */             "Cannot create SubsetState in node description");
/*     */         }
/* 123 */         XMLSubsetHandler1_0.this.stateBuilder.setExpression(attributes.getValue("expr"));
/*     */       }
/*     */     }
/*     */     , new IPaloStartHandler() {
/*     */       public String getPath() {
/* 127 */         return "subset/state/search";
/*     */       }
/*     */ 
/*     */       public void startElement(String uri, String localName, String qName, Attributes attributes)
/*     */       {
/* 132 */         if (XMLSubsetHandler1_0.this.stateBuilder == null) {
/* 133 */           throw new PaloAPIException(
/* 134 */             "Cannot create SubsetState in node description");
/*     */         }
/* 136 */         Attribute attr = XMLSubsetHandler1_0.this
/* 137 */           .getAttributeByName(XMLSubsetHandler1_0.this.subsetBuilder
/* 137 */           .getSourceHierarchy(), attributes.getValue("attribute"));
/* 138 */         if (attr != null)
/* 139 */           XMLSubsetHandler1_0.this.stateBuilder.setSearchAttribute(attr);
/*     */       }
/*     */     }
/*     */     , new IPaloStartHandler() {
/*     */       public String getPath() {
/* 143 */         return "subset/state/element";
/*     */       }
/*     */ 
/*     */       public void startElement(String uri, String localName, String qName, Attributes attributes)
/*     */       {
/* 148 */         if ((XMLSubsetHandler1_0.this.stateBuilder == null) || (XMLSubsetHandler1_0.this.subsetBuilder == null)) {
/* 149 */           throw new PaloAPIException(
/* 150 */             "Cannot create SubsetState in node element");
/*     */         }
/* 152 */         String elementId = attributes.getValue("id");
/* 153 */         String paths = attributes.getValue("paths");
/* 154 */         String positions = attributes.getValue("pos");
/* 155 */         Hierarchy srcHier = XMLSubsetHandler1_0.this.subsetBuilder.getSourceHierarchy();
/* 156 */         Element element = srcHier.getElementByName(elementId);
/* 157 */         XMLSubsetHandler1_0.this.stateBuilder.addElement(element);
/* 158 */         XMLSubsetHandler1_0.this.stateBuilder.setPaths(element, paths);
/* 159 */         XMLSubsetHandler1_0.this.stateBuilder.setPositions(element, positions);
/*     */       }
/*     */     }
/*     */      };
/*     */   }
/*     */ 
/*     */   IPaloEndHandler[] getEndHandlers(Database database) {
/* 165 */     return new IPaloEndHandler[] { new IPaloEndHandler() {
/*     */       public String getPath() {
/* 167 */         return "subset/state";
/*     */       }
/*     */ 
/*     */       public void endElement(String uri, String localName, String qName) {
/* 171 */         if ((XMLSubsetHandler1_0.this.subsetBuilder == null) || (XMLSubsetHandler1_0.this.stateBuilder == null)) {
/* 172 */           throw new PaloAPIException("Cannot create subset state!!");
/*     */         }
/* 174 */         SubsetState state = XMLSubsetHandler1_0.this.stateBuilder.createState();
/* 175 */         XMLSubsetHandler1_0.this.subsetBuilder.addState(state);
/* 176 */         XMLSubsetHandler1_0.this.stateBuilder = null;
/*     */       }
/*     */     }
/*     */      };
/*     */   }
/*     */ 
/*     */   protected Attribute getAttributeByName(Hierarchy srcHier, String value)
/*     */   {
/* 184 */     if ((srcHier == null) || (srcHier.getDimension().getDatabase().getConnection().isLegacy()))
/* 185 */       return null;
/* 186 */     Attribute[] attrs = srcHier.getAttributes();
/* 187 */     for (int i = 0; i < attrs.length; ++i) {
/* 188 */       if (attrs[i].getName().equals(value)) {
/* 189 */         return attrs[i];
/*     */       }
/*     */     }
/* 192 */     return null;
/*     */   }
/*     */ }

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name:     org.palo.api.impl.subsets.XMLSubsetHandler1_0
 * JD-Core Version:    0.5.4
 */