/*
// $Id: //open/mondrian/testsrc/main/mondrian/rolap/agg/SegmentLoaderTest.java#15 $
// This software is subject to the terms of the Eclipse Public License v1.0
// Agreement, available at the following URL:
// http://www.eclipse.org/legal/epl-v10.html.
// Copyright (C) 2004-2010 Julian Hyde and others
// All Rights Reserved.
// You must accept the terms of that agreement to use this software.
 */
package mondrian.rolap.agg;

import mondrian.olap.MondrianProperties;
import mondrian.rolap.*;
import mondrian.spi.Dialect;
import mondrian.test.SqlPattern;
import mondrian.util.DelegatingInvocationHandler;

import java.lang.reflect.Proxy;
import java.sql.*;
import java.util.*;

/**
 * <p>
 * Test for <code>SegmentLoader</code>
 * </p>
 * 
 * @author Thiyagu
 * @version $Id:
 *          //open/mondrian/testsrc/main/mondrian/rolap/agg/SegmentLoaderTest
 *          .java#15 $
 * @since 06-Jun-2007
 */
public class SegmentLoaderTest extends BatchTestCase {

  protected void setUp() throws Exception {
    super.setUp();
  }

  public void testRollup() throws Exception {
    propSaver.set(MondrianProperties.instance().DisableCaching, true);
    propSaver.set(MondrianProperties.instance().SegmentCache, MockSegmentCache.class
      .getName());
    final String queryOracle = "select \"store\".\"store_country\" as \"c0\", \"time_by_day\".\"the_year\" as \"c1\", sum(\"sales_fact_1997\".\"unit_sales\") as \"m0\" from \"store\" \"store\", \"sales_fact_1997\" \"sales_fact_1997\", \"time_by_day\" \"time_by_day\" where \"sales_fact_1997\".\"store_id\" = \"store\".\"store_id\" and \"store\".\"store_country\" = 'USA' and \"sales_fact_1997\".\"time_id\" = \"time_by_day\".\"time_id\" and \"time_by_day\".\"the_year\" = 1997 group by \"store\".\"store_country\", \"time_by_day\".\"the_year\"";
    executeQuery("select {[Store].[Store Country].Members} on rows, {[Measures].[Unit Sales]} on columns from [Sales]");
    getTestContext().flushSchemaCache();
    assertQuerySqlOrNot(
      getTestContext(),
      "select {[Store].[Store Country].[USA]} on rows, {[Measures].[Unit Sales]} on columns from [Sales]",
      new SqlPattern[] { new SqlPattern(Dialect.DatabaseProduct.ORACLE, queryOracle,
        queryOracle.length()) }, true, true, true);
  }

  public void testLoadWithMockResultsForLoadingSummaryAndDetailedSegments() {
    GroupingSet groupableSetsInfo = getGroupingSetRollupOnGender();

    GroupingSet groupingSetsInfo = getDefaultGroupingSet();
    ArrayList<GroupingSet> groupingSets = new ArrayList<GroupingSet>();
    groupingSets.add(groupingSetsInfo);
    groupingSets.add(groupableSetsInfo);
    SegmentLoader loader = new SegmentLoader() {
      SqlStatement createExecuteSql(GroupingSetsList groupingSetsList,
        List<StarPredicate> compoundPredicateList) {
        return null;
      }

      ResultSet loadData(SqlStatement stmt, GroupingSetsList groupingSetsList)
        throws SQLException {
        return toResultSet(getData(true));
      }
    };
    loader.load(groupingSets, null, null);
    Aggregation.Axis[] axes = groupingSetsInfo.getAxes();
    verifyYearAxis(axes[0]);
    verifyProductFamilyAxis(axes[1]);
    verifyProductDepartmentAxis(axes[2]);
    verifyGenderAxis(axes[3]);
    verifyUnitSalesDetailed(groupingSets.get(0).getSegments().get(0));

    axes = groupingSets.get(0).getAxes();
    verifyYearAxis(axes[0]);
    verifyProductFamilyAxis(axes[1]);
    verifyProductDepartmentAxis(axes[2]);
    verifyUnitSalesAggregate(groupingSets.get(1).getSegments().get(0));
  }

  private ResultSet toResultSet(final List<Object[]> list) {
    final MyDelegatingInvocationHandler handler = new MyDelegatingInvocationHandler(
      list);
    Object o = Proxy.newProxyInstance(null, new Class[] { ResultSet.class,
      ResultSetMetaData.class }, handler);
    handler.resultSetMetaData = (ResultSetMetaData) o;
    return (ResultSet) o;
  }

  private SegmentLoader.RowList toRowList2(List<Object[]> list) {
    final SegmentLoader.RowList rowList = new SegmentLoader.RowList(Collections
      .nCopies(list.get(0).length, SqlStatement.Type.OBJECT));
    for (Object[] objects : list) {
      rowList.createRow();
      for (int i = 0; i < objects.length; i++) {
        Object object = objects[i];
        rowList.setObject(i, object);
      }
    }
    return rowList;
  }

  /**
   * Tests load with mock results for loading summary and detailed segments with
   * null in rollup column.
   */
  public void testLoadWithWithNullInRollupColumn() {
    GroupingSet groupableSetsInfo = getGroupingSetRollupOnGender();

    GroupingSet groupingSetsInfo = getDefaultGroupingSet();
    ArrayList<GroupingSet> groupingSets = new ArrayList<GroupingSet>();
    groupingSets.add(groupingSetsInfo);
    groupingSets.add(groupableSetsInfo);
    SegmentLoader loader = new SegmentLoader() {
      SqlStatement createExecuteSql(GroupingSetsList groupingSetsList,
        List<StarPredicate> compoundPredicateList) {
        return null;
      }

      ResultSet loadData(SqlStatement stmt, GroupingSetsList groupingSetsList)
        throws SQLException {
        return toResultSet(getDataWithNullInRollupColumn(true));
      }
    };
    loader.load(groupingSets, null, null);
    Segment detailedSegment = groupingSets.get(0).getSegments().get(0);
    assertEquals(3, detailedSegment.getCellCount());
  }

  public void testLoadWithMockResultsForLoadingSummaryAndDetailedSegmentsUsingSparse() {
    GroupingSet groupableSetsInfo = getGroupingSetRollupOnGender();

    GroupingSet groupingSetsInfo = getDefaultGroupingSet();
    ArrayList<GroupingSet> groupingSets = new ArrayList<GroupingSet>();
    groupingSets.add(groupingSetsInfo);
    groupingSets.add(groupableSetsInfo);
    SegmentLoader loader = new SegmentLoader() {
      SqlStatement createExecuteSql(GroupingSetsList groupingSetsList,
        List<StarPredicate> compoundPredicateList) {
        return null;
      }

      ResultSet loadData(SqlStatement stmt, GroupingSetsList groupingSetsList)
        throws SQLException {
        return toResultSet(getData(true));
      }

      boolean useSparse(boolean sparse, int n, RowList rows) {
        return true;
      }
    };
    loader.load(groupingSets, null, null);
    Aggregation.Axis[] axes = groupingSetsInfo.getAxes();
    verifyYearAxis(axes[0]);
    verifyProductFamilyAxis(axes[1]);
    verifyProductDepartmentAxis(axes[2]);
    verifyGenderAxis(axes[3]);
    verifyUnitSalesDetailedForSparse(groupingSets.get(0).getSegments().get(0));

    axes = groupingSets.get(0).getAxes();
    verifyYearAxis(axes[0]);
    verifyProductFamilyAxis(axes[1]);
    verifyProductDepartmentAxis(axes[2]);
    verifyUnitSalesAggregateForSparse(groupingSets.get(1).getSegments().get(0));
  }

  public void testLoadWithMockResultsForLoadingOnlyDetailedSegments() {
    GroupingSet groupingSetsInfo = getDefaultGroupingSet();
    ArrayList<GroupingSet> groupingSets = new ArrayList<GroupingSet>();
    groupingSets.add(groupingSetsInfo);
    SegmentLoader loader = new SegmentLoader() {
      SqlStatement createExecuteSql(GroupingSetsList groupingSetsList,
        List<StarPredicate> compoundPredicateList) {
        return null;
      }

      ResultSet loadData(SqlStatement stmt, GroupingSetsList groupingSetsList)
        throws SQLException {
        return toResultSet(getData(false));
      }
    };
    loader.load(groupingSets, null, null);
    Aggregation.Axis[] axes = groupingSetsInfo.getAxes();
    verifyYearAxis(axes[0]);
    verifyProductFamilyAxis(axes[1]);
    verifyProductDepartmentAxis(axes[2]);
    verifyGenderAxis(axes[3]);
    verifyUnitSalesDetailed(groupingSetsInfo.getSegments().get(0));
  }

  public void testProcessDataForGettingGroupingSetsBitKeysAndLoadingAxisValueSet()
    throws SQLException {
    GroupingSet groupableSetsInfo = getGroupingSetRollupOnGender();

    GroupingSet groupingSetsInfo = getDefaultGroupingSet();

    List<GroupingSet> groupingSets = new ArrayList<GroupingSet>();
    groupingSets.add(groupingSetsInfo);
    groupingSets.add(groupableSetsInfo);

    SegmentLoader loader = new SegmentLoader() {
      ResultSet loadData(SqlStatement stmt, GroupingSetsList groupingSetsList)
        throws SQLException {
        return toResultSet(getData(true));
      }
    };
    int axisCount = 4;
    SortedSet<Comparable<?>>[] axisValueSet = loader
      .getDistinctValueWorkspace(axisCount);
    boolean[] axisContainsNull = new boolean[axisCount];

    SegmentLoader.RowList list = loader.processData(null, axisContainsNull,
      axisValueSet, new GroupingSetsList(groupingSets));
    int totalNoOfRows = 12;
    int lengthOfRowWithBitKey = 6;
    assertEquals(totalNoOfRows, list.size());
    assertEquals(lengthOfRowWithBitKey, list.getTypes().size());
    list.first();
    list.next();
    assertEquals(BitKey.Factory.makeBitKey(0), list.getObject(5));

    BitKey bitKeyForSummaryRow = BitKey.Factory.makeBitKey(0);
    bitKeyForSummaryRow.set(0);
    list.next();
    list.next();
    assertEquals(bitKeyForSummaryRow, list.getObject(5));

    SortedSet<Comparable<?>> yearAxis = axisValueSet[0];
    assertEquals(1, yearAxis.size());
    SortedSet<Comparable<?>> productFamilyAxis = axisValueSet[1];
    assertEquals(3, productFamilyAxis.size());
    SortedSet<Comparable<?>> productDepartmentAxis = axisValueSet[2];
    assertEquals(4, productDepartmentAxis.size());
    SortedSet<Comparable<?>> genderAxis = axisValueSet[3];
    assertEquals(2, genderAxis.size());

    assertFalse(axisContainsNull[0]);
    assertFalse(axisContainsNull[1]);
    assertFalse(axisContainsNull[2]);
    assertFalse(axisContainsNull[3]);
  }

  private GroupingSet getGroupingSetRollupOnGender() {
    return getGroupingSet(new String[] { tableTime, tableProductClass,
      tableProductClass }, new String[] { fieldYear, fieldProductFamily,
      fieldProductDepartment }, new String[][] { fieldValuesYear,
      fieldValuesProductFamily, fieldValueProductDepartment }, cubeNameSales,
      measureUnitSales);
  }

  public void testProcessDataForSettingNullAxis() throws SQLException {
    GroupingSet groupingSetsInfo = getDefaultGroupingSet();

    SegmentLoader loader = new SegmentLoader() {
      ResultSet loadData(SqlStatement stmt, GroupingSetsList groupingSetsList)
        throws SQLException {
        return toResultSet(getDataWithNullInAxisColumn(false));
      }
    };
    int axisCount = 4;
    SortedSet<Comparable<?>>[] axisValueSet = loader
      .getDistinctValueWorkspace(axisCount);
    boolean[] axisContainsNull = new boolean[axisCount];
    List<GroupingSet> groupingSets = new ArrayList<GroupingSet>();
    groupingSets.add(groupingSetsInfo);

    loader.processData(null, axisContainsNull, axisValueSet, new GroupingSetsList(
      groupingSets));

    assertFalse(axisContainsNull[0]);
    assertFalse(axisContainsNull[1]);
    assertTrue(axisContainsNull[2]);
    assertFalse(axisContainsNull[3]);
  }

  public void testProcessDataForNonGroupingSetsScenario() throws SQLException {
    GroupingSet groupingSetsInfo = getDefaultGroupingSet();

    SegmentLoader loader = new SegmentLoader() {
      ResultSet loadData(SqlStatement stmt, GroupingSetsList groupingSetsList)
        throws SQLException {
        List<Object[]> data = new ArrayList<Object[]>();
        data.add(new Object[] { "1997", "Food", "Deli", "F", "5990" });
        data.add(new Object[] { "1997", "Food", "Deli", "M", "6047" });
        data.add(new Object[] { "1997", "Food", "Canned_Products", "F", "867" });

        return toResultSet(data);
      }
    };
    List<GroupingSet> groupingSets = new ArrayList<GroupingSet>();
    groupingSets.add(groupingSetsInfo);

    SortedSet<Comparable<?>>[] axisValueSet = loader.getDistinctValueWorkspace(4);
    SegmentLoader.RowList list = loader.processData(null, new boolean[4],
      axisValueSet, new GroupingSetsList(groupingSets));
    int totalNoOfRows = 3;
    assertEquals(totalNoOfRows, list.size());
    int lengthOfRowWithoutBitKey = 5;
    assertEquals(lengthOfRowWithoutBitKey, list.getTypes().size());

    SortedSet<Comparable<?>> yearAxis = axisValueSet[0];
    assertEquals(1, yearAxis.size());
    SortedSet<Comparable<?>> productFamilyAxis = axisValueSet[1];
    assertEquals(1, productFamilyAxis.size());
    SortedSet<Comparable<?>> productDepartmentAxis = axisValueSet[2];
    assertEquals(2, productDepartmentAxis.size());
    SortedSet<Comparable<?>> genderAxis = axisValueSet[3];
    assertEquals(2, genderAxis.size());
  }

  private void verifyUnitSalesDetailed(Segment segment) {
    Double[] unitSalesValues = { null, null, null, null, 1987.0, 2199.0, null, null,
      867.0, 945.0, null, null, null, null, 5990.0, 6047.0, null, null, 368.0,
      473.0, null, null, null, null };
    Iterator<Map.Entry<CellKey, Object>> iterator = segment.getData().iterator();
    int index = 0;
    while (iterator.hasNext()) {
      Map.Entry<CellKey, Object> x = iterator.next();
      assertEquals(unitSalesValues[index++], x.getValue());
    }
  }

  private void verifyUnitSalesDetailedForSparse(Segment segment) {
    List<CellKey> cellKeys = new ArrayList<CellKey>();
    cellKeys.add(CellKey.Generator.newCellKey(new int[] { 0, 2, 1, 0 }));
    cellKeys.add(CellKey.Generator.newCellKey(new int[] { 0, 0, 2, 0 }));
    cellKeys.add(CellKey.Generator.newCellKey(new int[] { 0, 1, 0, 0 }));
    cellKeys.add(CellKey.Generator.newCellKey(new int[] { 0, 2, 1, 1 }));
    cellKeys.add(CellKey.Generator.newCellKey(new int[] { 0, 1, 0, 1 }));
    cellKeys.add(CellKey.Generator.newCellKey(new int[] { 0, 1, 3, 0 }));
    cellKeys.add(CellKey.Generator.newCellKey(new int[] { 0, 0, 2, 1 }));
    cellKeys.add(CellKey.Generator.newCellKey(new int[] { 0, 1, 3, 1 }));
    Double[] unitSalesValues = { 368.0, 1987.0, 867.0, 473.0, 945.0, 5990.0, 2199.0,
      6047.0 };

    Iterator<Map.Entry<CellKey, Object>> iterator = segment.getData().iterator();
    int index = 0;
    while (iterator.hasNext()) {
      Map.Entry<CellKey, Object> x = iterator.next();
      assertEquals(cellKeys.get(index), x.getKey());
      assertEquals(unitSalesValues[index], x.getValue());
      index++;
    }
  }

  private void verifyUnitSalesAggregateForSparse(Segment segment) {
    List<CellKey> cellKeys = new ArrayList<CellKey>();
    cellKeys.add(CellKey.Generator.newCellKey(new int[] { 0, 2, 1 }));
    cellKeys.add(CellKey.Generator.newCellKey(new int[] { 0, 1, 0 }));
    cellKeys.add(CellKey.Generator.newCellKey(new int[] { 0, 1, 3 }));
    cellKeys.add(CellKey.Generator.newCellKey(new int[] { 0, 0, 2 }));
    Double[] unitSalesValues = { 841.0, 1812.0, 12037.0, 4186.0, };

    Iterator<Map.Entry<CellKey, Object>> iterator = segment.getData().iterator();
    int index = 0;
    while (iterator.hasNext()) {
      Map.Entry<CellKey, Object> x = iterator.next();
      assertEquals(cellKeys.get(index), x.getKey());
      assertEquals(unitSalesValues[index], x.getValue());
      index++;
    }
  }

  private void verifyUnitSalesAggregate(Segment segment) {
    Double[] unitSalesValues = { null, null, 4186.0, null, 1812.0, null, null,
      12037.0, null, 841.0, null, null };
    Iterator<Map.Entry<CellKey, Object>> iterator = segment.getData().iterator();
    int index = 0;
    while (iterator.hasNext()) {
      Map.Entry<CellKey, Object> x = iterator.next();
      assertEquals(unitSalesValues[index++], x.getValue());
    }
  }

  public void testGetGroupingBitKey() throws SQLException {
    Object[] data = { "1997", "Food", "Deli", "M", "6047", 0, 0, 0, 0 };
    ResultSet rowList = toResultSet(Collections.singletonList(data));
    assertTrue(rowList.next());
    assertEquals(BitKey.Factory.makeBitKey(4), new SegmentLoader().getRollupBitKey(
      4, rowList, 5));

    data = new Object[] { "1997", "Food", "Deli", null, "12037", 0, 0, 0, 1 };
    rowList = toResultSet(Collections.singletonList(data));
    BitKey key = BitKey.Factory.makeBitKey(4);
    key.set(3);
    assertEquals(key, new SegmentLoader().getRollupBitKey(4, rowList, 5));

    data = new Object[] { "1997", null, "Deli", null, "12037", 0, 1, 0, 1 };
    rowList = toResultSet(Collections.singletonList(data));
    key = BitKey.Factory.makeBitKey(4);
    key.set(1);
    key.set(3);
    assertEquals(key, new SegmentLoader().getRollupBitKey(4, rowList, 5));
  }

  public void testGroupingSetsUtilForMissingGroupingBitKeys() {
    List<GroupingSet> groupingSets = new ArrayList<GroupingSet>();
    groupingSets.add(getDefaultGroupingSet());
    groupingSets.add(getGroupingSetRollupOnGender());
    GroupingSetsList detail = new GroupingSetsList(groupingSets);

    List<BitKey> bitKeysList = detail.getRollupColumnsBitKeyList();
    int columnsCount = 4;
    assertEquals(BitKey.Factory.makeBitKey(columnsCount), bitKeysList.get(0));
    BitKey key = BitKey.Factory.makeBitKey(columnsCount);
    key.set(0);
    assertEquals(key, bitKeysList.get(1));

    groupingSets = new ArrayList<GroupingSet>();
    groupingSets.add(getDefaultGroupingSet());
    groupingSets.add(getGroupingSetRollupOnGenderAndProductFamily());
    bitKeysList = new GroupingSetsList(groupingSets).getRollupColumnsBitKeyList();
    assertEquals(BitKey.Factory.makeBitKey(columnsCount), bitKeysList.get(0));
    key = BitKey.Factory.makeBitKey(columnsCount);
    key.set(0);
    key.set(1);
    assertEquals(key, bitKeysList.get(1));

    assertTrue(new GroupingSetsList(new ArrayList<GroupingSet>())
      .getRollupColumnsBitKeyList().isEmpty());
  }

  private GroupingSet getGroupingSetRollupOnGenderAndProductFamily() {
    return getGroupingSet(new String[] { tableTime, tableProductClass },
      new String[] { fieldYear, fieldProductDepartment }, new String[][] {
        fieldValuesYear, fieldValueProductDepartment }, cubeNameSales,
      measureUnitSales);
  }

  public void testGroupingSetsUtilSetsDetailForRollupColumns() {
    RolapStar.Measure measure = getMeasure(cubeNameSales, measureUnitSales);
    RolapStar star = measure.getStar();
    RolapStar.Column year = star.lookupColumn(tableTime, fieldYear);
    RolapStar.Column productFamily = star.lookupColumn(tableProductClass,
      fieldProductFamily);
    RolapStar.Column productDepartment = star.lookupColumn(tableProductClass,
      fieldProductDepartment);
    RolapStar.Column gender = star.lookupColumn(tableCustomer, fieldGender);

    List<GroupingSet> groupingSets = new ArrayList<GroupingSet>();
    groupingSets.add(getDefaultGroupingSet());
    groupingSets.add(getGroupingSetRollupOnProductDepartment());
    groupingSets.add(getGroupingSetRollupOnGenderAndProductDepartment());
    GroupingSetsList detail = new GroupingSetsList(groupingSets);

    List<RolapStar.Column> rollupColumnsList = detail.getRollupColumns();
    assertEquals(2, rollupColumnsList.size());
    assertEquals(gender, rollupColumnsList.get(0));
    assertEquals(productDepartment, rollupColumnsList.get(1));

    groupingSets.add(getGroupingSetRollupOnGenderAndProductDepartmentAndYear());
    detail = new GroupingSetsList(groupingSets);
    rollupColumnsList = detail.getRollupColumns();
    assertEquals(3, rollupColumnsList.size());
    assertEquals(gender, rollupColumnsList.get(0));
    assertEquals(productDepartment, rollupColumnsList.get(1));
    assertEquals(year, rollupColumnsList.get(2));

    groupingSets.add(getGroupingSetRollupOnProductFamilyAndProductDepartment());
    detail = new GroupingSetsList(groupingSets);
    rollupColumnsList = detail.getRollupColumns();
    assertEquals(4, rollupColumnsList.size());
    assertEquals(gender, rollupColumnsList.get(0));
    assertEquals(productDepartment, rollupColumnsList.get(1));
    assertEquals(productFamily, rollupColumnsList.get(2));
    assertEquals(year, rollupColumnsList.get(3));

    assertTrue(new GroupingSetsList(new ArrayList<GroupingSet>()).getRollupColumns()
      .isEmpty());
  }

  private GroupingSet getGroupingSetRollupOnGenderAndProductDepartment() {
    return getGroupingSet(new String[] { tableProductClass, tableTime },
      new String[] { fieldProductFamily, fieldYear }, new String[][] {
        fieldValuesProductFamily, fieldValuesYear }, cubeNameSales, measureUnitSales);
  }

  private GroupingSet getGroupingSetRollupOnProductFamilyAndProductDepartment() {
    return getGroupingSet(new String[] { tableCustomer, tableTime }, new String[] {
      fieldGender, fieldYear },
      new String[][] { fieldValuesGender, fieldValuesYear }, cubeNameSales,
      measureUnitSales);
  }

  private GroupingSet getGroupingSetRollupOnGenderAndProductDepartmentAndYear() {
    return getGroupingSet(new String[] { tableProductClass },
      new String[] { fieldProductFamily },
      new String[][] { fieldValuesProductFamily }, cubeNameSales, measureUnitSales);
  }

  private GroupingSet getGroupingSetRollupOnProductDepartment() {
    return getGroupingSet(
      new String[] { tableCustomer, tableProductClass, tableTime }, new String[] {
        fieldGender, fieldProductFamily, fieldYear }, new String[][] {
        fieldValuesGender, fieldValuesProductFamily, fieldValuesYear },
      cubeNameSales, measureUnitSales);
  }

  public void testGroupingSetsUtilSetsForDetailForRollupColumns() {
    RolapStar.Measure measure = getMeasure(cubeNameSales, measureUnitSales);
    RolapStar star = measure.getStar();
    RolapStar.Column year = star.lookupColumn(tableTime, fieldYear);
    RolapStar.Column productFamily = star.lookupColumn(tableProductClass,
      fieldProductFamily);
    RolapStar.Column productDepartment = star.lookupColumn(tableProductClass,
      fieldProductDepartment);
    RolapStar.Column gender = star.lookupColumn(tableCustomer, fieldGender);

    List<GroupingSet> groupingSets = new ArrayList<GroupingSet>();
    groupingSets.add(getDefaultGroupingSet());
    groupingSets.add(getGroupingSetRollupOnProductDepartment());
    groupingSets.add(getGroupingSetRollupOnGenderAndProductDepartment());
    GroupingSetsList detail = new GroupingSetsList(groupingSets);

    List<RolapStar.Column> rollupColumnsList = detail.getRollupColumns();
    assertEquals(2, rollupColumnsList.size());
    assertEquals(gender, rollupColumnsList.get(0));
    assertEquals(productDepartment, rollupColumnsList.get(1));

    groupingSets.add(getGroupingSetRollupOnGenderAndProductDepartmentAndYear());
    detail = new GroupingSetsList(groupingSets);
    rollupColumnsList = detail.getRollupColumns();
    assertEquals(3, rollupColumnsList.size());
    assertEquals(gender, rollupColumnsList.get(0));
    assertEquals(productDepartment, rollupColumnsList.get(1));
    assertEquals(year, rollupColumnsList.get(2));

    groupingSets.add(getGroupingSetRollupOnProductFamilyAndProductDepartment());
    detail = new GroupingSetsList(groupingSets);
    rollupColumnsList = detail.getRollupColumns();
    assertEquals(4, rollupColumnsList.size());
    assertEquals(gender, rollupColumnsList.get(0));
    assertEquals(productDepartment, rollupColumnsList.get(1));
    assertEquals(productFamily, rollupColumnsList.get(2));
    assertEquals(year, rollupColumnsList.get(3));

    assertTrue(new GroupingSetsList(new ArrayList<GroupingSet>()).getRollupColumns()
      .isEmpty());
  }

  public void testGroupingSetsUtilSetsForGroupingFunctionIndex() {
    List<GroupingSet> groupingSets = new ArrayList<GroupingSet>();
    groupingSets.add(getDefaultGroupingSet());
    groupingSets.add(getGroupingSetRollupOnProductDepartment());
    groupingSets.add(getGroupingSetRollupOnGenderAndProductDepartment());
    GroupingSetsList detail = new GroupingSetsList(groupingSets);
    assertEquals(0, detail.findGroupingFunctionIndex(3));
    assertEquals(1, detail.findGroupingFunctionIndex(2));

    groupingSets.add(getGroupingSetRollupOnGenderAndProductDepartmentAndYear());
    detail = new GroupingSetsList(groupingSets);
    assertEquals(0, detail.findGroupingFunctionIndex(3));
    assertEquals(1, detail.findGroupingFunctionIndex(2));
    assertEquals(2, detail.findGroupingFunctionIndex(0));

    groupingSets.add(getGroupingSetRollupOnProductFamilyAndProductDepartment());
    detail = new GroupingSetsList(groupingSets);
    assertEquals(0, detail.findGroupingFunctionIndex(3));
    assertEquals(1, detail.findGroupingFunctionIndex(2));
    assertEquals(2, detail.findGroupingFunctionIndex(1));
    assertEquals(3, detail.findGroupingFunctionIndex(0));
  }

  public void testGetGroupingColumnsList() {
    GroupingSet groupingSetsInfo = getDefaultGroupingSet();

    GroupingSet groupableSetsInfo = getGroupingSetRollupOnGender();

    RolapStar.Column[] detailedColumns = groupingSetsInfo.getSegments().get(0).aggregation
      .getColumns();
    RolapStar.Column[] summaryColumns = groupableSetsInfo.getSegments().get(0).aggregation
      .getColumns();
    List<GroupingSet> groupingSets = new ArrayList<GroupingSet>();
    groupingSets.add(groupingSetsInfo);
    groupingSets.add(groupableSetsInfo);

    List<RolapStar.Column[]> groupingColumns = new GroupingSetsList(groupingSets)
      .getGroupingSetsColumns();
    assertEquals(2, groupingColumns.size());
    assertEquals(detailedColumns, groupingColumns.get(0));
    assertEquals(summaryColumns, groupingColumns.get(1));

    groupingColumns = new GroupingSetsList(new ArrayList<GroupingSet>())
      .getGroupingSetsColumns();
    assertEquals(0, groupingColumns.size());
  }

  public void testSetFailOnStillLoadingSegments() {
    List<GroupingSet> groupingSets = new ArrayList<GroupingSet>();
    groupingSets.add(getDefaultGroupingSet());
    new SegmentLoader().setFailOnStillLoadingSegments(new GroupingSetsList(
      groupingSets));

    for (GroupingSet groupingSet : groupingSets) {
      for (Segment segment : groupingSet.getSegments()) {
        assertTrue(segment.isFailed());
      }
    }

    groupingSets = new ArrayList<GroupingSet>();
    groupingSets.add(getDefaultGroupingSet());
    groupingSets.add(getGroupingSetRollupOnGender());
    new SegmentLoader().setFailOnStillLoadingSegments(new GroupingSetsList(
      groupingSets));
    for (GroupingSet groupingSet : groupingSets) {
      for (Segment segment : groupingSet.getSegments()) {
        assertTrue(segment.isFailed());
      }
    }
  }

  private GroupingSet getDefaultGroupingSet() {
    return getGroupingSet(new String[] { tableCustomer, tableProductClass,
      tableProductClass, tableTime }, new String[] { fieldGender,
      fieldProductDepartment, fieldProductFamily, fieldYear }, new String[][] {
      fieldValuesGender, fieldValueProductDepartment, fieldValuesProductFamily,
      fieldValuesYear }, cubeNameSales, measureUnitSales);
  }

  private void verifyYearAxis(Aggregation.Axis axis) {
    Comparable<?>[] keys = axis.getKeys();
    assertEquals(1, keys.length);
    assertEquals("1997", keys[0].toString());
  }

  private void verifyProductFamilyAxis(Aggregation.Axis axis) {
    Comparable<?>[] keys = axis.getKeys();
    assertEquals(3, keys.length);
    assertEquals("Drink", keys[0].toString());
    assertEquals("Food", keys[1].toString());
    assertEquals("Non-Consumable", keys[2].toString());
  }

  private void verifyProductDepartmentAxis(Aggregation.Axis axis) {
    Comparable<?>[] keys = axis.getKeys();
    assertEquals(4, keys.length);
    assertEquals("Canned_Products", keys[0].toString());
  }

  private void verifyGenderAxis(Aggregation.Axis axis) {
    Comparable<?>[] keys = axis.getKeys();
    assertEquals(2, keys.length);
    assertEquals("F", keys[0].toString());
    assertEquals("M", keys[1].toString());
  }

  private List<Object[]> getData(boolean incSummaryData) {
    List<Object[]> data = new ArrayList<Object[]>();
    data.add(new Object[] { "1997", "Food", "Deli", "F", "5990", 0 });
    data.add(new Object[] { "1997", "Food", "Deli", "M", "6047", 0 });
    if (incSummaryData) {
      data.add(new Object[] { "1997", "Food", "Deli", null, "12037", 1 });
    }
    data.add(new Object[] { "1997", "Food", "Canned_Products", "F", "867", 0 });
    data.add(new Object[] { "1997", "Food", "Canned_Products", "M", "945", 0 });
    if (incSummaryData) {
      data.add(new Object[] { "1997", "Food", "Canned_Products", null, "1812", 1 });
    }
    data.add(new Object[] { "1997", "Drink", "Dairy", "F", "1987", 0 });
    data.add(new Object[] { "1997", "Drink", "Dairy", "M", "2199", 0 });
    if (incSummaryData) {
      data.add(new Object[] { "1997", "Drink", "Dairy", null, "4186", 1 });
    }
    data.add(new Object[] { "1997", "Non-Consumable", "Carousel", "F", "368", 0 });
    data.add(new Object[] { "1997", "Non-Consumable", "Carousel", "M", "473", 0 });
    if (incSummaryData) {
      data
        .add(new Object[] { "1997", "Non-Consumable", "Carousel", null, "841", 1 });
    }
    return data;
  }

  private List<Object[]> getDataWithNullInRollupColumn(boolean incSummaryData) {
    List<Object[]> data = new ArrayList<Object[]>();
    data.add(new Object[] { "1997", "Food", "Deli", "F", "5990", 0 });
    data.add(new Object[] { "1997", "Food", "Deli", "M", "6047", 0 });
    data.add(new Object[] { "1997", "Food", "Deli", null, "867", 0 });
    if (incSummaryData) {
      data.add(new Object[] { "1997", "Food", "Deli", null, "12037", 1 });
    }
    return data;
  }

  private List<Object[]> getDataWithNullInAxisColumn(boolean incSummaryData) {
    List<Object[]> data = new ArrayList<Object[]>();
    data.add(new Object[] { "1997", "Food", "Deli", "F", "5990", 0 });
    data.add(new Object[] { "1997", "Food", "Deli", "M", "6047", 0 });
    if (incSummaryData) {
      data.add(new Object[] { "1997", "Food", "Deli", null, "12037", 1 });
    }
    data.add(new Object[] { "1997", "Food", null, "F", "867", 0 });
    return data;
  }

  public static class MyDelegatingInvocationHandler extends
    DelegatingInvocationHandler {
    int row;

    public boolean wasNull;

    ResultSetMetaData resultSetMetaData;

    private final List<Object[]> list;

    public MyDelegatingInvocationHandler(List<Object[]> list) {
      this.list = list;
      row = -1;
    }

    protected Object getTarget() {
      return null;
    }

    public ResultSetMetaData getMetaData() {
      return resultSetMetaData;
    }

    // implement ResultSetMetaData
    public int getColumnCount() {
      return list.get(0).length;
    }

    // implement ResultSetMetaData
    public int getColumnType(int column) {
      return Types.VARCHAR;
    }

    public boolean next() {
      if (row < list.size() - 1) {
        ++row;
        return true;
      }
      return false;
    }

    public Object getObject(int column) {
      return list.get(row)[column - 1];
    }

    public int getInt(int column) {
      final Object o = list.get(row)[column - 1];
      if (o == null) {
        wasNull = true;
        return 0;
      } else {
        wasNull = false;
        return ((Number) o).intValue();
      }
    }

    public double getDouble(int column) {
      final Object o = list.get(row)[column - 1];
      if (o == null) {
        wasNull = true;
        return 0D;
      } else {
        wasNull = false;
        return ((Number) o).doubleValue();
      }
    }

    public boolean wasNull() {
      return wasNull;
    }
  }
}

// End SegmentLoaderTest.java
