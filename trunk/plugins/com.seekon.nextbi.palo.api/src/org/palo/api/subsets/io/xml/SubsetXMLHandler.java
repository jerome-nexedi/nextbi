/*     */ package org.palo.api.subsets.io.xml;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashMap;
/*     */ import java.util.Stack;
/*     */ import javax.xml.validation.TypeInfoProvider;
/*     */ import org.palo.api.Database;
/*     */ import org.palo.api.Dimension;
/*     */ import org.palo.api.PaloAPIException;
/*     */ import org.palo.api.subsets.Subset2;
/*     */ import org.palo.api.subsets.SubsetFilter;
/*     */ import org.palo.api.subsets.filter.AliasFilter;
/*     */ import org.palo.api.subsets.filter.AttributeFilter;
/*     */ import org.palo.api.subsets.filter.DataFilter;
/*     */ import org.palo.api.subsets.filter.HierarchicalFilter;
/*     */ import org.palo.api.subsets.filter.PicklistFilter;
/*     */ import org.palo.api.subsets.filter.SortingFilter;
/*     */ import org.palo.api.subsets.filter.TextFilter;
/*     */ import org.palo.api.subsets.impl.SubsetHandlerImpl;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.helpers.DefaultHandler;
/*     */ 
/*     */ public class SubsetXMLHandler extends DefaultHandler
/*     */ {
/*  71 */   private Stack<String> absPath = new Stack();
/*     */   private SubsetFilterHandler filter;
/*     */   private Subset2 subset;
/*     */   private String version;
/*     */   private final SubsetHandlerImpl subsetHandler;
/*     */   private final int type;
/*     */   private final String defName;
/*  78 */   private final HashMap<String, Class<? extends SubsetFilterHandler>> filterHandlers = new HashMap();
/*  79 */   private final StringBuffer value = new StringBuffer();
/*     */ 
/*     */   public SubsetXMLHandler(TypeInfoProvider typeProvider, SubsetHandlerImpl subsetHandler, String defName, int type) {
/*  82 */     this.type = type;
/*  83 */     this.defName = defName;
/*  84 */     this.subsetHandler = subsetHandler;
/*  85 */     this.filterHandlers.put("/subset/alias_filter", AliasFilterHandler.class);
/*  86 */     this.filterHandlers.put("/subset/text_filter", TextFilterHandler.class);
/*  87 */     this.filterHandlers.put("/subset/attribute_filter", AttributeFilterHandler.class);
/*  88 */     this.filterHandlers.put("/subset/data_filter", DataFilterHandler.class);
/*  89 */     this.filterHandlers.put("/subset/hierarchical_filter", HierarchicalFilterHandler.class);
/*  90 */     this.filterHandlers.put("/subset/picklist_filter", PicklistHandler.class);
/*  91 */     this.filterHandlers.put("/subset/sorting_filter", SortingFilterHandler.class);
/*     */   }
/*     */ 
/*     */   public final Subset2 getSubset() {
/*  95 */     return this.subset;
/*     */   }
/*     */ 
/*     */   public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
/*     */   {
/* 100 */     super.startElement(uri, localName, qName, attributes);
/* 101 */     this.value.delete(0, this.value.length());
/* 102 */     this.absPath.push(qName);
/* 103 */     String xPath = getXPath();
/* 104 */     if (xPath.equals("/subset")) {
/* 105 */       this.subset = createSubset(attributes);
/*     */     }
/* 107 */     if (this.filterHandlers.containsKey(xPath))
/* 108 */       this.filter = createFilter(xPath);
/* 109 */     else if (this.filter != null)
/* 110 */       this.filter.enter(xPath);
/*     */   }
/*     */ 
/*     */   public void endElement(String uri, String localName, String qName) throws SAXException
/*     */   {
/* 115 */     super.endElement(uri, localName, qName);
/* 116 */     String xPath = getXPath();
/* 117 */     if (xPath.equals("/subset/indent/value")) {
/* 118 */       if ((this.version != null) && (this.version.equals("1.0rc2"))) {
/* 119 */         this.subset.setIndent(2);
/*     */       } else {
/* 121 */         String indent = this.value.toString();
/*     */         try {
/* 123 */           this.subset.setIndent(Integer.parseInt(indent));
/*     */         } catch (Exception e) {
/* 125 */           this.subset.setIndent(2);
/*     */         }
/*     */       }
/*     */     }
/* 129 */     else if (this.filter != null)
/* 130 */       if (xPath.equals(this.filter.getXPath())) {
/* 131 */         this.subset.add(this.filter.createFilter(this.subset.getDimension()));
/* 132 */         this.filter = null;
/*     */       } else {
/* 134 */         this.filter.leave(xPath, this.value.toString());
/*     */       }
/* 136 */     if (this.absPath.size() > 0)
/* 137 */       this.absPath.pop();
/*     */   }
/*     */ 
/*     */   public void characters(char[] ch, int start, int length)
/*     */     throws SAXException
/*     */   {
/* 143 */     this.value.append(ch, start, length);
/*     */   }
/*     */ 
/*     */   public void processingInstruction(String target, String data)
/*     */     throws SAXException
/*     */   {
/* 149 */     if (target.equals("palosubset")) {
/* 150 */       this.version = data.substring(9, data.length() - 1).trim();
/*     */     }
/* 152 */     super.processingInstruction(target, data);
/*     */   }
/*     */ 
/*     */   public static final double getDouble(String value)
/*     */   {
/* 159 */     value = value.trim();
/* 160 */     if ((value == null) || (value.equals("")))
/* 161 */       return 0.0D;
/* 162 */     return Double.parseDouble(value);
/*     */   }
/*     */ 
/*     */   public static final boolean getBoolean(String value) {
/* 166 */     if ((value == null) || (value.equals("")))
/* 167 */       return false;
/* 168 */     return Boolean.parseBoolean(value);
/*     */   }
/*     */ 
/*     */   public static final int getInteger(String value) {
/* 172 */     value = value.trim();
/* 173 */     if ((value == null) || (value.equals("")))
/* 174 */       return 0;
/* 175 */     return Integer.parseInt(value);
/*     */   }
/*     */ 
/*     */   public static final String getFilterXMLExpression(SubsetFilter filter) {
/* 179 */     String xmlStr = null;
/* 180 */     switch (filter.getType())
/*     */     {
/*     */     case 32:
/* 182 */       xmlStr = AttributeFilterHandler.getPersistenceString(
/* 183 */         (AttributeFilter)filter);
/* 184 */       break;
/*     */     case 8:
/* 186 */       xmlStr = DataFilterHandler.getPersistenceString(
/* 187 */         (DataFilter)filter);
/* 188 */       break;
/*     */     case 2:
/* 190 */       xmlStr = HierarchicalFilterHandler.getPersistenceString(
/* 191 */         (HierarchicalFilter)filter);
/* 192 */       break;
/*     */     case 4:
/* 194 */       xmlStr = PicklistHandler.getPersistenceString(
/* 195 */         (PicklistFilter)filter);
/* 196 */       break;
/*     */     case 16:
/* 198 */       xmlStr = SortingFilterHandler.getPersistenceString(
/* 199 */         (SortingFilter)filter);
/* 200 */       break;
/*     */     case 1:
/* 202 */       xmlStr = TextFilterHandler.getPersistenceString(
/* 203 */         (TextFilter)filter);
/* 204 */       break;
/*     */     case 64:
/* 206 */       xmlStr = AliasFilterHandler.getPersistenceString(
/* 207 */         (AliasFilter)filter);
/* 208 */       break;
/*     */     default:
/* 210 */       throw new PaloAPIException(
/* 211 */         "Couldn't store subset! Unsupported filter '" + 
/* 212 */         filter.getClass().getName() + "'!");
/*     */     }
/*     */ 
/* 215 */     return xmlStr;
/*     */   }
/*     */ 
/*     */   private final String getXPath()
/*     */   {
/* 224 */     Enumeration allPaths = this.absPath.elements();
/* 225 */     StringBuffer path = new StringBuffer();
/* 226 */     while (allPaths.hasMoreElements()) {
/* 227 */       path.append("/");
/* 228 */       path.append((String)allPaths.nextElement());
/*     */     }
/* 230 */     return path.toString();
/*     */   }
/*     */ 
/*     */   private final SubsetFilterHandler createFilter(String xPath) {
/* 234 */     Class filter = (Class)this.filterHandlers.get(xPath);
/* 235 */     if (filter != null) {
/*     */       try {
/* 237 */         SubsetFilterHandler filterHandler = 
/* 238 */           (SubsetFilterHandler)filter.getConstructor(new Class[0]).newInstance(new Object[0]);
/* 239 */         filterHandler.setSubsetVersion(this.version);
/* 240 */         return filterHandler;
/*     */       }
/*     */       catch (Exception e) {
/* 243 */         e.printStackTrace();
/*     */       }
/*     */     }
/* 246 */     return null;
/*     */   }
/*     */ 
/*     */   private final Subset2 createSubset(Attributes attributes)
/*     */     throws SAXException
/*     */   {
/* 252 */     String id = attributes.getValue("id");
/* 253 */     if (!isValidId(id))
/* 254 */       throw new SAXException("No subset id defined!");
/* 255 */     String srcDimensionId = attributes.getValue("sourceDimensionId");
/*     */ 
/* 260 */     String name = attributes.getValue("name");
/*     */ 
/* 264 */     Database database = this.subsetHandler.getDimension().getDatabase();
/*     */ 
/* 267 */     Dimension dimension = this.subsetHandler.getDimension();
/* 268 */     if (!this.defName.equals(name))
/* 269 */       name = this.defName;
/* 270 */     Subset2 subset = this.subsetHandler.create(id, name, dimension.getDefaultHierarchy(), this.type);
/*     */ 
/* 284 */     return subset;
/*     */   }
/*     */ 
/*     */   private final boolean isValidId(String id)
/*     */   {
/* 289 */     return (id != null) && (!id.equals(""));
/*     */   }
/*     */ }

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name:     org.palo.api.subsets.io.xml.SubsetXMLHandler
 * JD-Core Version:    0.5.4
 */