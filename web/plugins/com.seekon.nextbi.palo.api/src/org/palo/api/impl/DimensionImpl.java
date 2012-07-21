/*      */package org.palo.api.impl;

/*      */
/*      */import com.tensegrity.palojava.DatabaseInfo; /*      */
import com.tensegrity.palojava.DbConnection; /*      */
import com.tensegrity.palojava.DimensionInfo; /*      */
import com.tensegrity.palojava.HierarchyInfo; /*      */
import com.tensegrity.palojava.PropertyInfo; /*      */
import com.tensegrity.palojava.loader.HierarchyLoader; /*      */
import com.tensegrity.palojava.loader.PropertyLoader; /*      */
import java.io.PrintStream; /*      */
import java.util.ArrayList; /*      */
import java.util.HashMap; /*      */
import java.util.Map; /*      */
import org.palo.api.Attribute; /*      */
import org.palo.api.ConnectionEvent; /*      */
import org.palo.api.Consolidation; /*      */
import org.palo.api.Cube; /*      */
import org.palo.api.Database; /*      */
import org.palo.api.Dimension; /*      */
import org.palo.api.Element; /*      */
import org.palo.api.ElementNode; /*      */
import org.palo.api.ElementNodeVisitor; /*      */
import org.palo.api.Hierarchy; /*      */
import org.palo.api.Property2; /*      */
import org.palo.api.Subset; /*      */
import org.palo.api.persistence.PaloPersistenceException; /*      */
import org.palo.api.subsets.SubsetHandler; /*      */
import org.palo.api.subsets.impl.SubsetHandlerImpl;

/*      */
/*      */class DimensionImpl
/*      */implements Dimension
/*      */{
  /*      */private final DimensionInfo dimInfo;

  /*      */private final ConnectionImpl connection;

  /*      */private final DbConnection dbConnection;

  /*      */private final Database database;

  /*      */private final CompoundKey key;

  /*      */private final SubsetStorageHandler legacySubsetsHandler;

  /*      */private final PropertyLoader propertyLoader;

  /*      */private final HierarchyLoader hierarchyLoader;

  /*      */private final Map<String, Property2Impl> loadedProperties;

  /*      */private final SubsetHandler subsetHandler;

  /*      */private final Map<String, HierarchyInfo> hierarchyInfos;

  /*      */private final Map<String, HierarchyImpl> loadedHierarchies;

  /*      */
  /*      */static final DimensionImpl create(ConnectionImpl connection,
    Database database, DimensionInfo dimInfo, boolean doEvents)
  /*      */{
    /* 80 */DimensionImpl dim = new DimensionImpl(connection, database, dimInfo);
    /* 81 */return dim;
    /*      */}

  /*      */
  /*      */private DimensionImpl(ConnectionImpl connection, Database database,
    DimensionInfo dimInfo)
  /*      */{
    /* 110 */this.dimInfo = dimInfo;
    /* 111 */this.connection = connection;
    /* 112 */this.dbConnection = connection.getConnectionInternal();
    /* 113 */this.database = database;
    /*      */
    /* 116 */this.loadedProperties = new HashMap();
    /* 117 */this.propertyLoader = this.dbConnection
      .getTypedPropertyLoader(dimInfo);
    /* 118 */this.hierarchyLoader = this.dbConnection.getHierarchyLoader(dimInfo);
    /*      */
    /* 120 */this.legacySubsetsHandler =
    /* 121 */((DatabaseImpl) database).getLegacySubsetHandler();
    /* 122 */this.loadedHierarchies = new HashMap();
    /* 123 */this.subsetHandler = new SubsetHandlerImpl(this);
    /* 124 */this.hierarchyInfos = new HashMap();
    /*      */
    /* 126 */this.key =
    /* 129 */new CompoundKey(new Object[] { DimensionImpl.class,
    /* 127 */connection,
    /* 128 */dimInfo.getDatabase().getId(),
    /* 129 */dimInfo.getId() });
    /*      */}

  /*      */
  /*      */public final Attribute addAttribute(String name)
  /*      */{
    /* 142 */return getDefaultHierarchy().addAttribute(name);
    /*      */}

  /*      */
  /*      */public final Element addElement(String name, int type)
  /*      */{
    /* 165 */return getDefaultHierarchy().addElement(name, type);
    /*      */}

  /*      */
  /*      */public final void addElements(String[] names, int[] types)
  /*      */{
    /* 179 */getDefaultHierarchy().addElements(names, types);
    /*      */}

  /*      */
  /*      */public final void addElements(String[] names, int type,
    Element[][] children, double[][] weights)
  /*      */{
    /* 193 */getDefaultHierarchy().addElements(names, type, children, weights);
    /*      */}

  /*      */
  /*      */public final void addElements(String[] names, int[] types,
    Element[][] children, double[][] weights) {
    /* 197 */getDefaultHierarchy().addElements(names, types, children, weights);
    /*      */}

  /*      */
  /*      */public final void updateConsolidations(Consolidation[] consolidations) {
    /* 201 */getDefaultHierarchy().updateConsolidations(consolidations);
    /*      */}

  /*      */
  /*      */public final void removeConsolidations(Element[] elements) {
    /* 205 */getDefaultHierarchy().removeConsolidations(elements);
    /*      */}

  /*      */
  /*      */public final Subset addSubset(String name)
  /*      */{
    /* 219 */return this.legacySubsetsHandler.addSubset(this, name);
    /*      */}

  /*      */
  /*      */public final void dumpElementsTree()
  /*      */{
    /* 231 */System.err.println("elementsTree for \"" + getName() + "\" ...");
    /* 232 */ElementNode[] roots = getElementsTree();
    /* 233 */for (int i = 0; i < roots.length; ++i)
      /* 234 */DimensionUtil.traverse(roots[i], new ElementNodeVisitor() {
        /*      */public void visit(ElementNode node, ElementNode parent) {
          /* 236 */int depth = node.getDepth();
          /* 237 */System.err.print("  ");
          /* 238 */for (int j = 0; j < depth; ++j)
            /* 239 */System.err.print("  ");
          /* 240 */System.err.println(node.getElement().getName());
          /*      */}
        /*      */
      });
    /*      */}

  /*      */
  /*      */public final ElementNode[] getAllElementNodes()
  /*      */{
    /* 247 */return getDefaultHierarchy().getAllElementNodes();
    /*      */}

  /*      */
  /*      */public final Cube getAttributeCube()
  /*      */{
    /* 265 */return getDefaultHierarchy().getAttributeCube();
    /*      */}

  /*      */
  /*      */public final Dimension getAttributeDimension()
  /*      */{
    /* 274 */Hierarchy attHier = getDefaultHierarchy().getAttributeHierarchy();
    /* 275 */if (attHier == null) {
      /* 276 */return null;
      /*      */}
    /* 278 */return attHier.getDimension();
    /*      */}

  /*      */
  /*      */public final Object[] getAttributeValues(Attribute[] attributes,
    Element[] elements)
  /*      */{
    /* 287 */return getDefaultHierarchy().getAttributeValues(attributes, elements);
    /*      */}

  /*      */
  /*      */public final Attribute[] getAttributes()
  /*      */{
    /* 302 */return getDefaultHierarchy().getAttributes();
    /*      */}

  /*      */
  /*      */public final Attribute getAttribute(String attrId)
  /*      */{
    /* 315 */return getDefaultHierarchy().getAttribute(attrId);
    /*      */}

  /*      */
  /*      */public final Attribute getAttributeByName(String attrName)
  /*      */{
    /* 325 */return getDefaultHierarchy().getAttributeByName(attrName);
    /*      */}

  /*      */
  /*      */public final Database getDatabase()
  /*      */{
    /* 335 */return this.database;
    /*      */}

  /*      */
  /*      */public final Cube[] getCubes()
  /*      */{
    /* 340 */return ((DatabaseImpl) this.database).getCubes(this);
    /*      */}

  /*      */
  /*      */public final Element getElementAt(int index)
  /*      */{
    /* 349 */return getDefaultHierarchy().getElementAt(index);
    /*      */}

  /*      */
  /*      */public final Element getElementById(String id)
  /*      */{
    /* 378 */return getDefaultHierarchy().getElementById(id);
    /*      */}

  /*      */
  /*      */public final Element getElementByName(String name)
  /*      */{
    /* 405 */return getDefaultHierarchy().getElementByName(name);
    /*      */}

  /*      */
  /*      */public final int getElementCount()
  /*      */{
    /* 424 */return getDefaultHierarchy().getElementCount();
    /*      */}

  /*      */
  /*      */public final String[] getElementNames()
  /*      */{
    /* 434 */return getDefaultHierarchy().getElementNames();
    /*      */}

  /*      */
  /*      */public final Element[] getElements()
  /*      */{
    /* 461 */return getDefaultHierarchy().getElements();
    /*      */}

  /*      */
  /*      */public final Element[] getElementsInOrder()
  /*      */{
    /* 487 */return getDefaultHierarchy().getElementsInOrder();
    /*      */}

  /*      */
  /*      */public final ElementNode[] getElementsTree()
  /*      */{
    /* 504 */return getDefaultHierarchy().getElementsTree();
    /*      */}

  /*      */
  /*      */public final int getExtendedType()
  /*      */{
    /* 532 */return 0;
    /*      */}

  /*      */
  /*      */public final String getName() {
    /* 536 */return this.dimInfo.getName();
    /*      */}

  /*      */
  /*      */public final Element[] getRootElements() {
    /* 540 */return getDefaultHierarchy().getRootElements();
    /*      */}

  /*      */
  /*      */public final Subset getSubset(String id)
  /*      */{
    /*      */try
    /*      */{
      /* 590 */return this.legacySubsetsHandler.getSubset(this, id);
      /*      */} catch (PaloPersistenceException e) {
      /* 592 */e.printStackTrace();
      /*      */}
    /* 594 */return null;
    /*      */}

  /*      */
  /*      */public final Subset[] getSubsets()
  /*      */{
    /*      */try
    /*      */{
      /* 601 */return this.legacySubsetsHandler.getSubsets(this);
      /*      */} catch (PaloPersistenceException e) {
      /* 603 */e.printStackTrace();
      /*      */}
    /* 605 */return new Subset[0];
    /*      */}

  /*      */
  /*      */public final SubsetHandler getSubsetHandler()
  /*      */{
    /* 611 */return this.subsetHandler;
    /*      */}

  /*      */
  /*      */public final boolean isAttributeDimension()
  /*      */{
    /* 638 */return this.dimInfo.getType() == 2;
    /*      */}

  /*      */
  /*      */public final boolean isSubsetDimension() {
    /* 642 */return ((DatabaseImpl) this.database).isSubsetDimension(this);
    /*      */}

  /*      */
  /*      */public final Consolidation newConsolidation(Element element,
    Element parent, double weight)
  /*      */{
    /* 647 */return getDefaultHierarchy().newConsolidation(element, parent, weight);
    /*      */}

  /*      */
  /*      */public final void removeAllAttributes()
  /*      */{
    /* 653 */getDefaultHierarchy().removeAllAttributes();
    /*      */}

  /*      */
  /*      */public final void removeAttribute(Attribute attribute)
  /*      */{
    /* 668 */getDefaultHierarchy().removeAttribute(attribute);
    /*      */}

  /*      */
  /*      */public final void removeElement(Element element)
  /*      */{
    /* 682 */getDefaultHierarchy().removeElement(element);
    /*      */}

  /*      */
  /*      */public final void removeElements(Element[] elements)
  /*      */{
    /* 690 */getDefaultHierarchy().removeElements(elements);
    /*      */}

  /*      */
  /*      */public final void removeSubset(Subset subset)
  /*      */{
    /* 704 */this.legacySubsetsHandler.removeSubset(this, subset);
    /*      */}

  /*      */
  /*      */public final void rename(String name)
  /*      */{
    /* 711 */String oldName = getName();
    /* 712 */Dimension attrDim = getAttributeDimension();
    /* 713 */String oldAttrDimName = (attrDim != null) ? attrDim.getName() : null;
    /*      */
    /* 715 */this.dbConnection.rename(this.dimInfo, name);
    /*      */
    /* 717 */this.dimInfo.setName(name);
    /*      */
    /* 719 */Hierarchy hier = getHierarchyById(this.dimInfo.getId());
    /* 720 */if (hier != null) {
      /* 721 */hier.rename(name);
      /*      */}
    /*      */
    /* 724 */reloadRules();
    /*      */
    /* 726 */fireDimensionsRenamed(new Dimension[] { this, attrDim }, new String[] {
    /* 727 */oldName, oldAttrDimName });
    /*      */}

  /*      */
  /*      */public final void renameElement(Element element, String newName) {
    /* 731 */getDefaultHierarchy().renameElement(element, newName);
    /*      */}

  /*      */
  /*      */public final void setAttributeValues(Attribute[] attributes,
    Element[] elements, Object[] values)
  /*      */{
    /* 736 */getDefaultHierarchy().setAttributeValues(attributes, elements, values);
    /*      */}

  /*      */
  /*      */public final void visitElementTree(ElementNodeVisitor visitor)
  /*      */{
    /* 752 */getDefaultHierarchy().visitElementTree(visitor);
    /*      */}

  /*      */
  /*      */public final String getId()
  /*      */{
    /* 763 */return this.dimInfo.getId();
    /*      */}

  /*      */
  /*      */public final DimensionInfo getInfo() {
    /* 767 */return this.dimInfo;
    /*      */}

  /*      */
  /*      */public final boolean isSystemDimension() {
    /* 771 */return this.dimInfo.getType() == 1;
    /*      */}

  /*      */
  /*      */public final boolean isUserInfoDimension() {
    /* 775 */return this.dimInfo.getType() == 3;
    /*      */}

  /*      */
  /*      */public final int getMaxDepth() {
    /* 779 */return getDefaultHierarchy().getMaxDepth();
    /*      */}

  /*      */
  /*      */public final int getMaxIndent()
  /*      */{
    /* 785 */return this.dimInfo.getMaxIndent();
    /*      */}

  /*      */
  /*      */public final int getMaxLevel()
  /*      */{
    /* 790 */return getDefaultHierarchy().getMaxLevel();
    /*      */}

  /*      */
  /*      */public final boolean equals(Object other)
  /*      */{
    /* 796 */if (other instanceof DimensionImpl) {
      /* 797 */return this.key.equals(((DimensionImpl) other).key);
      /*      */}
    /* 799 */return false;
    /*      */}

  /*      */
  /*      */public final int hashCode() {
    /* 803 */return this.key.hashCode();
    /*      */}

  /*      */
  /*      */public final void reload(boolean doEvents)
  /*      */{
    /* 833 */this.propertyLoader.reset();
    /* 834 */this.hierarchyLoader.reset();
    /*      */
    /* 837 */this.subsetHandler.reset();
    /* 838 */((HierarchyImpl) getDefaultHierarchy()).reload(doEvents);
    /*      */}

  /*      */
  /*      */final void clearCache()
  /*      */{
    /* 914 */((HierarchyImpl) getDefaultHierarchy()).clearCache();
    /*      */
    /* 916 */for (Property2Impl property : this.loadedProperties.values()) {
      /* 917 */property.clearCache();
      /*      */}
    /* 919 */this.loadedProperties.clear();
    /* 920 */this.propertyLoader.reset();
    /*      */
    /* 923 */this.subsetHandler.reset();
    /*      */}

  /*      */
  /*      */final void reloadRules()
  /*      */{
    /* 928 */for (Cube cube : getCubes())
      /* 929 */((CubeImpl) cube).reloadRuleInfos(false);
    /*      */}

  /*      */
  /*      */private final void fireDimensionsRenamed(Object[] dimensions,
    Object oldValue)
  /*      */{
    /* 1091 */ConnectionEvent ev = new ConnectionEvent(getDatabase()
      .getConnection(),
    /* 1092 */getDatabase(),
    /* 1093 */4, dimensions);
    /*      */
    /* 1095 */ev.oldValue = oldValue;
    /* 1096 */this.connection.fireEvent(ev);
    /*      */}

  /*      */
  /*      */public String[] getAllPropertyIds()
  /*      */{
    /* 1110 */return this.propertyLoader.getAllPropertyIds();
    /*      */}

  /*      */
  /*      */public Property2 getProperty(String id) {
    /* 1114 */PropertyInfo propInfo = this.propertyLoader.load(id);
    /* 1115 */if (propInfo == null) {
      /* 1116 */return null;
      /*      */}
    /* 1118 */Property2 property = (Property2) this.loadedProperties.get(propInfo
      .getId());
    /* 1119 */if (property == null) {
      /* 1120 */property = createProperty(propInfo);
      /*      */}
    /*      */
    /* 1123 */return property;
    /*      */}

  /*      */
  /*      */public void addProperty(Property2 property) {
    /* 1127 */if (property == null) {
      /* 1128 */return;
      /*      */}
    /* 1130 */Property2Impl _property = (Property2Impl) property;
    /* 1131 */this.propertyLoader.loaded(_property.getPropInfo());
    /* 1132 */this.loadedProperties.put(_property.getId(), _property);
    /*      */}

  /*      */
  /*      */public void removeProperty(String id) {
    /* 1136 */Property2 property = getProperty(id);
    /* 1137 */if (property == null) {
      /* 1138 */return;
      /*      */}
    /* 1140 */if (property.isReadOnly()) {
      /* 1141 */return;
      /*      */}
    /* 1143 */this.loadedProperties.remove(property);
    /*      */}

  /*      */
  /*      */private void createProperty(Property2 parent, PropertyInfo kid) {
    /* 1147 */Property2 p2Kid = Property2Impl.create(parent, kid);
    /* 1148 */parent.addChild(p2Kid);
    /* 1149 */for (PropertyInfo kidd : kid.getChildren())
      /* 1150 */createProperty(p2Kid, kidd);
    /*      */}

  /*      */
  /*      */private Property2 createProperty(PropertyInfo propInfo)
  /*      */{
    /* 1155 */Property2 prop = Property2Impl.create(null, propInfo);
    /* 1156 */for (PropertyInfo kid : propInfo.getChildren()) {
      /* 1157 */createProperty(prop, kid);
      /*      */}
    /* 1159 */return prop;
    /*      */}

  /*      */
  /*      */public boolean canBeModified() {
    /* 1163 */return this.dimInfo.canBeModified();
    /*      */}

  /*      */
  /*      */public boolean canCreateChildren() {
    /* 1167 */return this.dimInfo.canCreateChildren();
    /*      */}

  /*      */
  /*      */public int getType() {
    /* 1171 */if (this.dimInfo.getType() == 0)
      /* 1172 */return 2;
    /* 1173 */if (this.dimInfo.getType() == 2)
      /* 1174 */return 8;
    /* 1175 */if (this.dimInfo.getType() == 1)
      /* 1176 */return 4;
    /* 1177 */if (this.dimInfo.getType() == 3) {
      /* 1178 */return 16;
      /*      */}
    /* 1180 */return -1;
    /*      */}

  /*      */
  /*      */public Hierarchy getDefaultHierarchy() {
    /* 1184 */if ((this.dimInfo == null)
      || (this.dimInfo.getDefaultHierarchy() == null)) {
      /* 1185 */return null;
      /*      */}
    /* 1187 */return getHierarchyById(this.dimInfo.getDefaultHierarchy().getId());
    /*      */}

  /*      */
  /*      */public Hierarchy[] getHierarchies() {
    /* 1191 */ArrayList hierarchies = new ArrayList();
    /* 1192 */for (String hier : getHierarchiesIds()) {
      /* 1193 */hierarchies.add(getHierarchyById(hier));
      /*      */}
    /* 1195 */return (Hierarchy[]) hierarchies.toArray(new Hierarchy[0]);
    /*      */}

  /*      */
  /*      */public String[] getHierarchiesIds() {
    /* 1199 */String[] ids = new String[this.dimInfo.getHierarchies().length];
    /* 1200 */int counter = 0;
    /* 1201 */for (HierarchyInfo hier : this.dimInfo.getHierarchies()) {
      /* 1202 */ids[(counter++)] = hier.getId();
      /*      */}
    /* 1204 */return ids;
    /*      */}

  /*      */
  /*      */public Hierarchy getHierarchyAt(int index) {
    /* 1208 */return getHierarchies()[index];
    /*      */}

  /*      */
  /*      */public Hierarchy getHierarchyById(String id) {
    /* 1212 */if (!this.loadedHierarchies.containsKey(id)) {
      /* 1213 */HierarchyInfo hierInfo = this.hierarchyLoader.load(id);
      /* 1214 */HierarchyImpl hier =
      /* 1215 */HierarchyImpl.create(this.connection, this, hierInfo, true);
      /* 1216 */this.loadedHierarchies.put(id, hier);
      /*      */}
    /* 1218 */return (Hierarchy) this.loadedHierarchies.get(id);
    /*      */}

  /*      */
  /*      */public final Hierarchy getHierarchyByName(String name) {
    /* 1222 */Hierarchy[] hiers = getHierarchies();
    /* 1223 */for (Hierarchy hier : hiers) {
      /* 1224 */if (hier.getName().equals(name)) {
        /* 1225 */return hier;
        /*      */}
      /*      */}
    /* 1228 */return null;
    /*      */}

  /*      */
  /*      */final void reloadHierarchyInfos()
  /*      */{
    /* 1237 */HierarchyInfo[] _hierInfos =
    /* 1238 */this.dbConnection.getHierarchies(this.dimInfo);
    /* 1239 */this.hierarchyInfos.clear();
    /* 1240 */for (int i = 0; i < _hierInfos.length; ++i)
      /* 1241 */this.hierarchyInfos.put(_hierInfos[i].getId(), _hierInfos[i]);
    /*      */}

  /*      */
  /*      */public int getHierarchyCount()
  /*      */{
    /* 1246 */return this.hierarchyLoader.getHierarchyCount();
    /*      */}
  /*      */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name: org.palo.api.impl.DimensionImpl JD-Core Version: 0.5.4
 */