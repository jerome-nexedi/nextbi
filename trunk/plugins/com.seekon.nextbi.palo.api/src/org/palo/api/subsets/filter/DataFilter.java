/*     */ package org.palo.api.subsets.filter;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Set;
/*     */ import org.palo.api.Cube;
/*     */ import org.palo.api.Database;
/*     */ import org.palo.api.Dimension;
/*     */ import org.palo.api.Element;
/*     */ import org.palo.api.Hierarchy;
/*     */ import org.palo.api.exceptions.PaloIOException;
/*     */ import org.palo.api.subsets.filter.settings.DataFilterSetting;
/*     */ import org.palo.api.subsets.filter.settings.DoubleParameter;
/*     */ import org.palo.api.subsets.filter.settings.IntegerParameter;
/*     */ import org.palo.api.subsets.filter.settings.StringParameter;
/*     */ 
/*     */ public class DataFilter extends AbstractSubsetFilter
/*     */   implements RestrictiveFilter, EffectiveFilter
/*     */ {
/*  70 */   private final int[] effectiveFilters = { 16 };
/*     */   private final DataFilterSetting setting;
/*  73 */   private final HashMap<Element, DataCell> dataElements = new HashMap();
/*     */ 
/*     */   /** @deprecated */
/*     */   public DataFilter(Dimension dimension, String sourceCube)
/*     */   {
/*  82 */     this(dimension.getDefaultHierarchy(), new DataFilterSetting(sourceCube));
/*     */   }
/*     */ 
/*     */   public DataFilter(Hierarchy hierarchy, String sourceCube)
/*     */   {
/*  91 */     this(hierarchy, new DataFilterSetting(sourceCube));
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public DataFilter(Dimension dimension, DataFilterSetting setting)
/*     */   {
/* 102 */     super(dimension.getDefaultHierarchy());
/* 103 */     this.setting = setting;
/*     */   }
/*     */ 
/*     */   public DataFilter(Hierarchy hierarchy, DataFilterSetting setting)
/*     */   {
/* 113 */     super(hierarchy);
/* 114 */     this.setting = setting;
/*     */   }
/*     */ 
/*     */   public final DataFilter copy() {
/* 118 */     DataFilter copy = 
/* 119 */       new DataFilter(this.hierarchy, this.setting.getSourceCube().getValue());
/* 120 */     copy.getSettings().adapt(this.setting);
/* 121 */     return copy;
/*     */   }
/*     */ 
/*     */   public final int getType() {
/* 125 */     return 8;
/*     */   }
/*     */ 
/*     */   public final void initialize()
/*     */   {
/*     */   }
/*     */ 
/*     */   public final DataFilterSetting getSettings()
/*     */   {
/* 134 */     return this.setting;
/*     */   }
/*     */ 
/*     */   public final void filter(Set<Element> elements)
/*     */   {
/* 140 */     if (elements.isEmpty()) {
/* 141 */       return;
/*     */     }
/*     */ 
/* 144 */     String srcCubeId = this.setting.getSourceCube().getValue();
/* 145 */     if ((srcCubeId == null) || (srcCubeId.equals("")))
/* 146 */       return;
/* 147 */     Database database = this.hierarchy.getDimension().getDatabase();
/* 148 */     Cube srcCube = database.getCubeById(srcCubeId);
/* 149 */     if (srcCube == null) {
/* 150 */       return;
/*     */     }
/*     */ 
/* 153 */     String[][] slice = this.setting.getSlice();
/*     */ 
/* 155 */     int dimIndex = getDimensionIndex(srcCube);
/* 156 */     DataCell[] dataCells = new DataCell[elements.size()];
/* 157 */     ArrayList coordinates = new ArrayList();
/* 158 */     int index = 0;
/* 159 */     ArrayList allHiers = new ArrayList();
/* 160 */     for (Dimension d : srcCube.getDimensions()) {
/* 161 */       allHiers.addAll(Arrays.asList(d.getHierarchies()));
/*     */     }
/* 163 */     Hierarchy[] hierarchies = (Hierarchy[])allHiers.toArray(new Hierarchy[allHiers.size()]);
/* 164 */     for (Element element : elements) {
/* 165 */       slice[dimIndex][0] = element.getId();
/* 166 */       DataCell dataCell = 
/* 167 */         new DataCell(element, hierarchies, slice);
/* 168 */       Element[][] coords = dataCell.getCoordinates();
/* 169 */       coordinates.addAll(Arrays.asList(coords));
/* 170 */       dataCells[(index++)] = dataCell;
/* 171 */       this.dataElements.put(element, dataCell);
/*     */     }
/* 173 */     Object[] values = srcCube.getDataBulk(
/* 174 */       (Element[][])coordinates.toArray(new Element[coordinates.size()][]));
/*     */     
/* 176 */     int k = 0;
/*     */     int l;
/* 177 */     for (l = 0; l < dataCells.length; ++l) {
/* 178 */       int valCount = dataCells[l].getCoordinates().length;
/* 179 */       Object[] cellValues = new Object[valCount];
/* 180 */       int end = k + valCount;
/* 181 */       for (int j = k; j < end; ++j) {
/* 182 */         cellValues[(j % valCount)] = values[j];
/*     */       }
/* 184 */       k = end;
/* 185 */       dataCells[l].setValues(cellValues);
/*     */     }
/*     */ 
/* 188 */     ArrayList sortedValues = 
/* 189 */       new ArrayList(Arrays.asList(dataCells));
/* 190 */     Collections.sort(sortedValues, 
/* 191 */       new DataCellComparator(this.setting.getCellOperator().getValue().intValue()));
/*     */ 
/* 194 */     int top = this.setting.getTop().getValue().intValue();
/* 195 */     if (top < 0) {
/* 196 */       top = elements.size();
/*     */     }
/* 198 */     double upper = this.setting.getUpperPercentage().getValue().doubleValue();
/* 199 */     double lower = this.setting.getLowerPercentage().getValue().doubleValue();
/*     */ 
/* 202 */     int count = 0;
/* 203 */     for (int i = sortedValues.size() - 1; i >= 0; --i) {
/* 204 */       DataCell cell = (DataCell)sortedValues.get(i);
/* 205 */       if (count >= top)
/* 206 */         elements.remove(cell.getElement());
/* 207 */       if (!fulFillsCriteria(cell))
/* 208 */         elements.remove(cell.getElement());
/*     */       else {
/* 210 */         ++count;
/*     */       }
/*     */     }
/*     */ 
/* 214 */     if ((upper > 0.0D) || (lower > 0.0D)) {
/* 215 */       double total = getTotalValue(dataCells);
/* 216 */       if (upper > 0.0D) {
/* 217 */         double partial = 0.0D;
/* 218 */         double bound = total * upper / 100.0D;
/* 219 */         for (int i = sortedValues.size() - 1; i >= 0; --i) {
/* 220 */           DataCell cell = (DataCell)sortedValues.get(i);
/*     */ 
/* 222 */           if (lower < 0.0D) {
/* 223 */             if (partial > bound)
/* 224 */               elements.remove(cell.getElement());
/* 225 */             if (cell.isString())
/*     */               continue;
/* 227 */             partial = partial + 
/* 227 */               ((Double)cell.getValue(this.setting.getCellOperator().getValue().intValue())).doubleValue();
/*     */           } else {
/* 229 */             if (partial < bound)
/* 230 */               elements.remove(cell.getElement());
/* 231 */             if (cell.isString())
/*     */               continue;
/* 233 */             partial = partial + 
/* 233 */               ((Double)cell.getValue(this.setting.getCellOperator().getValue().intValue())).doubleValue();
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/* 238 */       if (lower > 0.0D) {
/* 239 */         double partial = 0.0D;
/* 240 */         double bound = total * lower / 100.0D;
/* 241 */         int i = 0; for (int n = sortedValues.size(); i < n; ++i) {
/* 242 */           DataCell cell = (DataCell)sortedValues.get(i);
/* 243 */           if (upper > 0.0D)
/* 244 */             if (partial < bound)
/* 245 */               elements.remove(cell.getElement());
/* 246 */           else if (partial > bound)
/* 247 */             elements.remove(cell.getElement());
/* 248 */           if (cell.isString())
/*     */             continue;
/* 250 */           partial = partial + 
/* 250 */             ((Double)cell.getValue(this.setting.getCellOperator().getValue().intValue())).doubleValue();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public final int[] getEffectiveFilter()
/*     */   {
/* 258 */     return this.effectiveFilters;
/*     */   }
/*     */ 
/*     */   public final void validateSettings() throws PaloIOException {
/* 262 */     Database database = this.hierarchy.getDimension().getDatabase();
/* 263 */     Cube srcCube = database.getCubeById(this.setting.getSourceCube().getValue());
/* 264 */     if (srcCube == null)
/* 265 */       throw new PaloIOException("DataFilter: unknown source cube with id '" + srcCube + "'!");
/* 266 */     String[][] slice = this.setting.getSlice();
/* 267 */     if (slice == null) {
/* 268 */       throw new PaloIOException("DataFilter: the slice is not set!");
/*     */     }
/* 270 */     if (slice.length != srcCube.getDimensionCount())
/* 271 */       throw new PaloIOException("DataFilter: wrong slice!");
/*     */   }
/*     */ 
/*     */   final String getValue(Element element)
/*     */   {
/* 280 */     DataCell dataElement = (DataCell)this.dataElements.get(element);
/* 281 */     if (dataElement == null) {
/* 282 */       return element.getName();
/*     */     }
/* 284 */     return dataElement.getValue(this.setting.getCellOperator().getValue().intValue()).toString();
/*     */   }
/*     */ 
/*     */   private final double getTotalValue(DataCell[] dataCells) {
/* 288 */     double total = 0.0D;
/* 289 */     for (DataCell cell : dataCells) {
/* 290 */       if (!cell.isString())
/* 291 */         total += ((Double)cell.getValue(this.setting.getCellOperator().getValue().intValue())).doubleValue();
/*     */     }
/* 293 */     return total;
/*     */   }
/*     */ 
/*     */   private final int getDimensionIndex(Cube srcCube) {
/* 297 */     Dimension[] dimensions = srcCube.getDimensions();
/* 298 */     for (int i = 0; i < dimensions.length; ++i) {
/* 299 */       if (dimensions[i].equals(this.hierarchy.getDimension()))
/* 300 */         return i;
/*     */     }
/* 302 */     return -1;
/*     */   }
/*     */ 
/*     */   private final boolean fulFillsCriteria(DataCell cell) {
/* 306 */     return cell.fulfills(
/* 307 */       this.setting.getCellOperator().getValue().intValue(), 
/* 308 */       this.setting.getCriteria());
/*     */   }
/*     */ }

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name:     org.palo.api.subsets.filter.DataFilter
 * JD-Core Version:    0.5.4
 */