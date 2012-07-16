/*
// $Id: //open/mondrian/testsrc/main/mondrian/test/PerformanceTest.java#14 $
// This software is subject to the terms of the Eclipse Public License v1.0
// Agreement, available at the following URL:
// http://www.eclipse.org/legal/epl-v10.html.
// Copyright (C) 2009-2011 Julian Hyde
// All Rights Reserved.
// You must accept the terms of that agreement to use this software.
 */
package mondrian.test;

import mondrian.olap.*;
import mondrian.util.Bug;

import org.apache.log4j.Logger;

import java.util.*;

/**
 * Various unit tests concerned with performance.
 * 
 * @author jhyde
 * @since August 7, 2006
 * @version $Id:
 *          //open/mondrian/testsrc/main/mondrian/test/PerformanceTest.java#14 $
 */
public class PerformanceTest extends FoodMartTestCase {
  /**
   * Certain tests are enabled only if logging is enabled at debug level or
   * higher.
   */
  public static final Logger LOGGER = Logger.getLogger(PerformanceTest.class);

  public PerformanceTest(String name) {
    super(name);
  }

  /**
   * Test case for <a href="http://jira.pentaho.com/browse/MONDRIAN-550"> Bug
   * MONDRIAN-550, "Performance bug with NON EMPTY and large axes"</a>.
   */
  public void testBugMondrian550() {
    final TestContext testContext = getBugMondrian550Schema();
    final Statistician statistician = new Statistician("testBugMondrian550");
    for (int i = 0; i < 10; i++) {
      checkBugMondrian550(testContext, statistician);
    }
    statistician.printDurations();
  }

  private void checkBugMondrian550(TestContext testContext, Statistician statistician) {
    long start = System.currentTimeMillis();
    // On my Latitude D630:
    // Takes 137 seconds before bug fixed.
    // Takes 13 seconds after bug fixed.
    // jdk1.6 marmalade 3.2 14036 17,899 12,889 ms
    // jdk1.6 marmalade main 14036 15,845 15,180 ms
    // jdk1.6 marmalade main 14037 TODO ms
    // jdk1.6 marmalade main 14052 14,284 ms first, 1419 +- 12 ms
    final Result result = testContext
      .executeQuery("select NON EMPTY {[Store Name sans All].Members} ON COLUMNS,\n"
        + "  NON EMPTY Hierarchize(Union({[ACC].[All]}, [ACC].[All].Children)) ON ROWS\n"
        + "from [Sales]\n" + "where ([Time].[1997].[Q4], [Measures].[EXP2])");
    statistician.record(start);
    assertEquals(13, result.getAxes()[0].getPositions().size());
    assertEquals(3262, result.getAxes()[1].getPositions().size());
  }

  /**
   * As {@link #testBugMondrian550()} but with tuples on the rows axis.
   */
  public void testBugMondrian550Tuple() {
    final TestContext testContext = getBugMondrian550Schema();
    final Statistician statistician = new Statistician("testBugMondrian550Tuple");
    int n = LOGGER.isDebugEnabled() ? 10 : 2;
    for (int i = 0; i < n; i++) {
      checkBugMondrian550Tuple(testContext, statistician);
    }
    statistician.printDurations();
  }

  private void checkBugMondrian550Tuple(TestContext testContext,
    Statistician statistician) {
    long start = System.currentTimeMillis();
    // On my Latitude D630:
    // Takes 252 seconds before bug fixed.
    // Takes 45 seconds after bug fixed.
    // jdk1.6 marmalade 3.2 14036 14,799 14,986 ms
    // jdk1.6 marmalade main 14036 20,839 20,331 ms
    // jdk1.6 marmalade main 14037 TODO ms
    // jdk1.6 marmalade main 14052 9664 +- 49
    final Result result2 = testContext
      .executeQuery("select NON EMPTY {[Store Name sans All].Members} ON COLUMNS,\n"
        + "  NON EMPTY Hierarchize(Union({[ACC].[All]}, [ACC].[All].Children))\n"
        + "   * [Gender].Children ON ROWS\n" + "from [Sales]\n"
        + "where ([Time].[1997].[Q4], [Measures].[EXP2])");
    statistician.record(start);
    assertEquals(13, result2.getAxes()[0].getPositions().size());
    assertEquals(3263, result2.getAxes()[1].getPositions().size());
  }

  private TestContext getBugMondrian550Schema() {
    return TestContext
      .instance()
      .createSubstitutingCube(
        "Sales",
        "      <Dimension name=\"ACC\" caption=\"Account\" type=\"StandardDimension\" foreignKey=\"customer_id\">\n"
          + "         <Hierarchy hasAll=\"true\" allMemberName=\"All\" primaryKey=\"customer_id\">\n"
          + "            <Table name=\"customer\"/>\n"
          + "            <Level name=\"CODE\" caption=\"Account\" uniqueMembers=\"true\" column=\"account_num\" type=\"String\"/>\n"
          + "         </Hierarchy>\n"
          + "      </Dimension>\n"
          + "      <Dimension name=\"Store Name sans All\" type=\"StandardDimension\" foreignKey=\"store_id\">\n"
          + "         <Hierarchy hasAll=\"false\" primaryKey=\"store_id\">\n"
          + "            <Table name=\"store\" />\n"
          + "            <Level name=\"Store Name\" uniqueMembers=\"true\" column=\"store_number\" type=\"Numeric\" ordinalColumn=\"store_name\"/>\n"
          + "         </Hierarchy>\n" + "      </Dimension>\n",
        "      <CalculatedMember dimension=\"Measures\" name=\"EXP2_4\" formula=\"IIf([ACC].CurrentMember.Level.Ordinal = [ACC].[All].Ordinal, Sum([ACC].[All].Children, [Measures].[Unit Sales]),     [Measures].[Unit Sales])\"/>\n"
          + "      <CalculatedMember dimension=\"Measures\" name=\"EXP2\" formula=\"IIf(0 &#60; [Measures].[EXP2_4], [Measures].[EXP2_4], NULL)\"/>\n");
  }

  /**
   * Test case for <a href="http://jira.pentaho.com/browse/MONDRIAN-641"> Bug
   * MONDRIAN-641</a>, "Large NON EMPTY result performs poorly with
   * ResultStyle.ITERABLE". Runs in ~10 seconds with ResultStyle.LIST, 99+
   * seconds with ITERABLE (on DELL Latitude D630).
   */
  public void testMondrianBug641() {
    if (!Bug.BugMondrian641Fixed) {
      return;
    }
    long start = System.currentTimeMillis();
    Result result = executeQuery("select  non empty  {  crossjoin( customers.[city].members, "
      + "crossjoin( [store type].[store type].members,  "
      + "product.[product name].members)) }" + " on 0 from sales");
    // jdk1.6 marmalade main 14036 287,940 518,349 ms
    printDuration("testBugMondrian641", start);
    assertEquals(51148, result.getAxes()[0].getPositions().size());
  }

  /**
   * Tests performance when an MDX query contains a very large explicit set.
   */
  public void testVeryLargeExplicitSet() {
    final TestContext testContext = getTestContext();
    final Statistician[] statisticians = {
    // jdk1.6 mackerel access main old 5,000 ms
      // jdk1.6 marmalade 3.2 14036 4,376 4,055 ms
      // jdk1.6 marmalade main 14036 4,471 3,589 ms
      // jdk1.6 marmalade main 14037 4,400 ms
      // jdk1.6 marmalade main 14052 5,280 ms
      new Statistician("testVeryLargeExplicitSet: Execute axis"),

      // Execute:
      // first:
      // jdk1.6 mackerel access old 75,000 ms
      // jdk1.6 marmalade main 14036 19,262 18,493 ms
      // jdk1.6 marmalade main 14037 19,000 ms
      // jdk1.6 marmalade 3.2 14036 18,710 19,077 ms
      // jdk1.6 marmalade main 14052 21,739 ms
      //
      // second:
      // jdk1.6 mackerel access old 65,000 ms
      // jdk1.6 marmalade main 14036 526 429 ms
      // jdk1.6 marmalade main 14037 800 400 ms
      // jdk1.6 marmalade 3.2 14036 313 406 ms
      // jdk1.6 marmalade main 14052 577 ms
      new Statistician("testVeryLargeExplicitSet: Execute"),

      // Param query:
      // first:
      // unknown revision mackerel 2,424 ms
      // jdk1.6 marmalade 3.2 14036 34 115 72 ms
      // jdk1.6 marmalade main 14036 66 107 ms
      // jdk1.6 marmalade main 14037 117 ms
      // jdk1.6 marmalade main 14052 47 ms
      //
      // second:
      // unknown revision mackerel 51 ms
      // jdk1.6 marmalade 3.2 14036 18 102 ms
      // jdk1.6 marmalade main 14036 86 105 95 ms
      // jdk1.6 marmalade main 14037 106 ms
      // jdk1.6 marmalade main 14052 21 ms
      new Statistician("testVeryLargeExplicitSet: Param query"), };
    for (int i = 0; i < 10; i++) {
      checkVeryLargeExplicitSet(statisticians, testContext);
    }
    for (Statistician statistician : statisticians) {
      statistician.printDurations();
    }
  }

  private void checkVeryLargeExplicitSet(Statistician[] statisticians,
    TestContext testContext) {
    Result result;
    long start = System.currentTimeMillis();
    final Axis axis = testContext.executeAxis("Customers.Members");
    statisticians[0].record(start);
    final List<Position> positionList = axis.getPositions();
    assertEquals(10407, positionList.size());

    // Take customers 0-2000 and 5000-7000. Using contiguous bursts,
    // Mondrian has a chance to optimize how it reads cells from the
    // database.
    List<Member> memberList = new ArrayList<Member>();
    for (int i = 0; i < positionList.size(); i++) {
      Position position = positionList.get(i);
      ++i;
      if (i < 2000 || i >= 5000 && i < 7000) {
        memberList.add(position.get(0));
      }
    }

    // Build a query with an explcit member list.
    if (LOGGER.isDebugEnabled()) {
      StringBuilder buf = new StringBuilder();
      for (Member member : memberList) {
        if (buf.length() > 0) {
          buf.append(", ");
        }
        buf.append(member);
      }
      final String mdx = "WITH SET [Selected Customers] AS {" + buf + "}\n"
        + "SELECT {[Measures].[Unit Sales],\n"
        + "        [Measures].[Store Sales]} on 0,\n"
        + "  [Selected Customers] on 1\n" + "FROM [Sales]";
      start = System.currentTimeMillis();
      result = testContext.executeQuery(mdx);

      statisticians[1].record(start);
      assertEquals(memberList.size(), result.getAxes()[1].getPositions().size());
    }

    // Much more efficient technique. Use a parameter, and bind to array.
    // Cuts out a lot of parsing, so takes 2.4s as opposed to 65s.
    Query query = testContext.getConnection().parseQuery(
      "WITH SET [Selected Customers]\n"
        + "  AS Parameter('Foo', [Customers], {}, 'Description')\n"
        + "SELECT {[Measures].[Unit Sales],\n"
        + "        [Measures].[Store Sales]} on 0,\n"
        + "  [Selected Customers] on 1\n" + "FROM [Sales]");
    query.setParameter("Foo", memberList);
    start = System.currentTimeMillis();
    result = testContext.getConnection().execute(query);

    statisticians[2].record(start);
    assertEquals(memberList.size(), result.getAxes()[1].getPositions().size());
  }

  /**
   * Test case for <a href="http://jira.pentaho.com/browse/MONDRIAN-639"> Bug
   * MONDRIAN-639, "RolapNamedSetEvaluator anon classes implement Iterable,
   * causing performance regression from 2.4 in FunUtil.count()"</a>.
   */
  public void testBugMondrian639() {
    // unknown revision before fix mac-mini 233,000 ms
    // unknown revision after fix mac-mini 4,500 ms
    // jdk1.6 marmalade 3.2 14036 1,821 1,702 ms
    // jdk1.6 marmalade main 14036 2,185 3,208 1,431 ms
    // jdk1.6 marmalade main 14037 1,801 ms
    // jdk1.6 marmalade main 14052 396 +- 28 ms
    final Statistician statistician = new Statistician("testBugMondrian639");
    for (int i = 0; i < 20; i++) {
      checkBugMondrian639(statistician);
    }
    statistician.printDurations();
  }

  private void checkBugMondrian639(Statistician statistician) {
    long start = System.currentTimeMillis();
    Result result = executeQuery("WITH SET [cjoin] AS "
      + "crossjoin(customers.members, "
      + TestContext.hierarchyName("store type", "store type")
      + ".[store type].members) " + "MEMBER [Measures].[total_available_count] "
      + "AS Format(COUNT([cjoin]), \"#####\") " + "SELECT"
      + "{[cjoin]} ON COLUMNS, " + "{[Measures].[total_available_count]} ON ROWS "
      + "FROM sales");

    statistician.record(start);
    assertEquals(62442, result.getAxes()[0].getPositions().size());
    assertEquals(1, result.getAxes()[1].getPositions().size());
  }

  /**
   * Tests performance of a larger schema with a large number of result cells.
   * Runs in 186 seconds without nonAllPositions array in RolapEvaluator. Runs
   * in 14 seconds when RolapEvaluator.getProperty uses getNonAllMembers. The
   * performance boost gets more significant as the schema size grows.
   */
  public void testBigResultsWithBigSchemaPerforms() {
    if (!LOGGER.isDebugEnabled()) {
      return;
    }
    TestContext testContext = TestContext.instance().createSubstitutingCube("Sales",
      extraGenders(1000), null);
    String mdx = "with "
      + " member [Measures].[one] as '1'"
      + " member [Measures].[two] as '2'"
      + " member [Measures].[three] as '3'"
      + " member [Measures].[four] as '4'"
      + " member [Measures].[five] as '5'"
      + " select "
      + "{[Measures].[one],[Measures].[two],[Measures].[three],[Measures].[four],[Measures].[five]}"
      + " on 0, "
      + "Crossjoin([Customers].[name].members,[Store].[Store Name].members)"
      + " on 1 from sales";
    long start = System.currentTimeMillis();
    testContext.executeQuery(mdx);

    // jdk1.6 marmalade 3.2 14036 23,588 23,426 ms
    // jdk1.6 marmalade main 14036 26,430 27,045 25,497 ms
    // jdk1.6 marmalade main 14037 26,893 ms
    // jdk1.6 marmalade main 14052 29,870 ms
    printDuration("testBigResultsWithBigSchemaPerforms", start);
  }

  private String extraGenders(final int numGenders) {
    final StringBuilder builder = new StringBuilder();
    for (int i = 0; i < numGenders; i++) {
      builder
        .append(String
          .format(
            "<Dimension name=\"Gender%d \" foreignKey=\"customer_id\">"
              + "<Hierarchy hasAll=\"true\" allMemberName=\"All Gender\" primaryKey=\"customer_id\">"
              + "      <Table name=\"customer\"/>"
              + "      <Level name=\"Gender\" column=\"gender\" uniqueMembers=\"true\"/>"
              + "    </Hierarchy>" + "</Dimension>", i));
    }
    return builder.toString();
  }

  /**
   * Runs a query that performs a lot of in-memory calculation.
   * 
   * <p>
   * Timings (branch / change / host / DBMS / jdk / timings (s) / mean):
   * <ul>
   * <li>mondrian-3.2 13366 marmalade oracle jdk1.6 592 588 581 571 avg 583
   * <li>mondrian-3.2 13367 marmalade oracle jdk1.6 643 620 631 671 avg 641
   * <li>mondrian-3.2 13397 marmalade oracle jdk1.6 604 626
   * <li>mondrian-3.2 13467 marmalade oracle jdk1.6 610 574
   * <li>mondrian-3.2 13489 marmalade oracle jdk1.6 565 561 579 596 avg 575
   * <li>mondrian-3.2 13490 marmalade oracle jdk1.6 607 611 581 605 avg 601
   * <li>mondrian-3.2 xxxxx marmalade oracle jdk1.6 562 583 541 522 avg 552
   * <li>mondrian-3.2 14036 marmalade oracle jdk1.6 451 433
   * <li>mondrian 14036 marmalade oracle jdk1.6 598 552
   * <li>mondrian 14037 marmalade oracle jdk1.6 626 596
   * <li>mondrian 14052 marmalade oracle jdk1.6 454
   * </ul>
   */
  public void testInMemoryCalc() {
    if (!LOGGER.isDebugEnabled()) {
      // Test is too expensive to run as part of standard regress.
      // Take 10h on hudson (MySQL)!!!
      return;
    }
    final String result = "Axis #0:\n"
      + "{[Time].[1997].[Q3]}\n"
      + "Axis #1:\n"
      + "{[Measures].[Store Sales]}\n"
      + "{[Measures].[Typical Store Sales]}\n"
      + "{[Measures].[Ratio]}\n"
      + "Axis #2:\n"
      + "{[Product].[Food].[Baked Goods].[Bread].[Sliced Bread].[Modell].[Modell Rye Bread], [Customers].[USA].[OR].[Salem].[Joan Johnson]}\n"
      + "{[Product].[Non-Consumable].[Household].[Plastic Products].[Plastic Utensils].[Denny].[Denny Plastic Knives], [Customers].[USA].[OR].[Lebanon].[Pat Pinkston]}\n"
      + "{[Product].[Food].[Starchy Foods].[Starchy Foods].[Rice].[Shady Lake].[Shady Lake Thai Rice], [Customers].[USA].[CA].[Grossmont].[Anne Silva]}\n"
      + "{[Product].[Food].[Canned Foods].[Canned Soup].[Soup].[Better].[Better Regular Ramen Soup], [Customers].[USA].[CA].[Coronado].[Robert Brink]}\n"
      + "{[Product].[Non-Consumable].[Health and Hygiene].[Bathroom Products].[Mouthwash].[Bird Call].[Bird Call Laundry Detergent], [Customers].[USA].[CA].[Downey].[Eric Renn]}\n"
      + "Row #0: 19.65\n" + "Row #0: 3.12\n" + "Row #0: 6.30\n" + "Row #1: 15.56\n"
      + "Row #1: 2.80\n" + "Row #1: 5.56\n" + "Row #2: 11.24\n" + "Row #2: 2.10\n"
      + "Row #2: 5.35\n" + "Row #3: 11.22\n" + "Row #3: 2.46\n" + "Row #3: 4.56\n"
      + "Row #4: 6.33\n" + "Row #4: 1.71\n" + "Row #4: 3.70\n";
    final String mdx = "with member [Measures].[Typical Store Sales] as\n"
      + "  Max(\n" + "    [Customers].Siblings,\n" + "    Min(\n"
      + "      [Product].Siblings,\n" + "      Avg(\n"
      + "        [Time].Siblings,\n" + "        [Measures].[Store Sales])))\n"
      + "member [Measures].[Ratio] as\n" + "  [Measures].[Store Sales]\n"
      + "   / [Measures].[Typical Store Sales]\n" + "select\n" + "  {\n"
      + "    [Measures].[Store Sales],\n"
      + "    [Measures].[Typical Store Sales],\n" + "    [Measures].[Ratio]\n"
      + "  } on 0,\n" + "  TopCount(\n" + "    Filter(\n"
      + "      NonEmptyCrossJoin(" + "        [Product].[Product Name].Members,\n"
      + "        [Customers].[Name].Members),\n"
      + "      [Measures].[Ratio] > 1.1\n"
      + "      and [Measures].[Store Sales] > 5),\n" + "    5,\n"
      + "    [Measures].[Ratio]) on 1\n" + "from [Sales]\n"
      + "where [Time].[1997].[Q3]";
    final long start = System.currentTimeMillis();
    assertQueryReturns(mdx, result);
    printDuration("in-memory calc", start);
  }

  /**
   * Test case for <a href="http://jira.pentaho.com/browse/MONDRIAN-843"> Bug
   * MONDRIAN-843, where Filter is inefficient.</a>
   */
  public void testBugMondrian843() {
    // On my core i7 laptop:
    // takes 2.5 seconds before bug fixed
    // takes 0.4 seconds after bug fixed
    // jdk1.6 marmalade 3.2 14036 826 ms
    // jdk1.6 marmalade main 14036 4,427 3,894 ms
    // jdk1.6 marmalade main 14037 TODO ms
    // jdk1.6 marmalade main 14052 800 ms
    long start = System.currentTimeMillis();
    executeQuery("WITH SET [filtered] AS "
      + "FILTER({customers.members, customers.members, customers.members, customers.members, customers.members}, [Measures].[Unit Sales] > 100) "
      + "SELECT" + "{[Measures].[Unit Sales]} ON COLUMNS, "
      + "{[filtered]} ON ROWS " + "FROM sales");
    printDuration("testBugMondrian843", start);
  }

  /**
   * Testcase for bug <a
   * href="http://jira.pentaho.com/browse/MONDRIAN-981">MONDRIAN-981, "Poor
   * performance when >=2 hierarchies are access-controlled with
   * rollupPolicy=partial"</a>.
   */
  public void testBugMondrian981() {
    if (!LOGGER.isDebugEnabled()) {
      // Too slow to run as part of standard regress until bug is fixed.
    }
    // To see the cartesian-product nature of this bug, try commenting out
    // various of the following HierarchyGrants.
    // The query runs in about 2s with no access-controlled hierarchies,
    // then appromixately doubles as each is added (48s with 5 hierarchies).
    final TestContext testContext = TestContext
      .instance()
      .create(
        null,
        null,
        null,
        null,
        null,
        "<Role name='Role1'>\n"
          + "  <SchemaGrant access='none'>\n"
          + "    <CubeGrant cube='Sales' access='all'>\n"
          + "      <HierarchyGrant hierarchy='[Store Type]' access='custom' rollupPolicy='partial'>\n"
          + "        <MemberGrant member='[Store Type].[All Store Types]' access='all'/>\n"
          + "        <MemberGrant member='[Store Type].[Supermarket]' access='none'/>\n"
          + "      </HierarchyGrant>\n"
          + "      <HierarchyGrant hierarchy='[Customers]' access='custom' rollupPolicy='partial'>\n"
          + "        <MemberGrant member='[Customers].[All Customers]' access='all'/>\n"
          + "        <MemberGrant member='[Customers].[USA].[CA].[Los Angeles]' access='none'/>\n"
          + "      </HierarchyGrant>\n"
          + "      <HierarchyGrant hierarchy='[Product]' access='custom' rollupPolicy='partial'>\n"
          + "        <MemberGrant member='[Product].[All Products]' access='all'/>\n"
          + "        <MemberGrant member='[Product].[Drink]' access='none'/>\n"
          + "      </HierarchyGrant>\n"
          + "      <HierarchyGrant hierarchy='[Promotion Media]' access='custom' rollupPolicy='partial'>\n"
          + "        <MemberGrant member='[Promotion Media].[All Media]' access='all'/>\n"
          + "        <MemberGrant member='[Promotion Media].[TV]' access='none'/>\n"
          + "      </HierarchyGrant>\n"
          + "      <HierarchyGrant hierarchy='[Education Level]' access='custom' rollupPolicy='partial'>\n"
          + "        <MemberGrant member='[Education Level].[All Education Levels]' access='all'/>\n"
          + "        <MemberGrant member='[Education Level].[Graduate Degree]' access='none'/>\n"
          + "      </HierarchyGrant>\n" + "    </CubeGrant>\n"
          + "  </SchemaGrant>\n" + "</Role>\n");

    testContext
      .withRole("Role1")
      .assertQueryReturns(
        "with member [Measures].[Foo] as\n"
          + "Aggregate([Gender].Members * [Marital Status].Members * [Time].Members)\n"
          + "select from [Sales] where [Measures].[Foo]",
        "Axis #0:\n" + "{[Measures].[Foo]}\n" + "1,184,028");
  }

  private static long printDuration(String desc, long t0) {
    final long t1 = System.currentTimeMillis();
    final long duration = t1 - t0;
    LOGGER.debug(desc + " took " + duration + " millis");
    return duration;
  }

  /**
   * Collects statistics for a test that is run multiple times.
   */
  static class Statistician {
    private final String desc;

    private final List<Long> durations = new ArrayList<Long>();

    public Statistician(String desc) {
      super();
      this.desc = desc;
    }

    private void record(long start) {
      durations.add(printDuration(desc + " iteration #" + (durations.size() + 1),
        start));
    }

    private void printDurations() {
      if (!LOGGER.isDebugEnabled()) {
        return;
      }

      List<Long> coreDurations = durations;
      String durationsString = durations.toString(); // save before sort

      // Ignore the first 3 readings. (JIT compilation takes a while to
      // kick in.)
      if (coreDurations.size() > 3) {
        coreDurations = durations.subList(3, durations.size());
      }
      Collections.sort(coreDurations);
      // Further ignore the max and min.
      List<Long> coreCoreDurations = coreDurations;
      if (coreDurations.size() > 4) {
        coreCoreDurations = coreDurations.subList(1, coreDurations.size() - 1);
      }
      long sum = 0;
      int count = coreCoreDurations.size();
      for (long duration : coreCoreDurations) {
        sum += duration;
      }
      final double avg = ((double) sum) / count;
      double y = 0;
      for (long duration : coreCoreDurations) {
        double x = duration - avg;
        y += x * x;
      }
      final double stddev = Math.sqrt(y / count);
      LOGGER.debug(desc + ": " + durations.get(0) + " first; " + avg + " +- "
        + stddev + "; " + coreDurations.get(0) + " min; "
        + coreDurations.get(coreDurations.size() - 1) + " max; " + durationsString
        + " millis");
    }
  }
}

// End PerformanceTest.java
