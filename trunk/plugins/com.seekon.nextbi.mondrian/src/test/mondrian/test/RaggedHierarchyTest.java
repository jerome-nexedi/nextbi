/*
// $Id: //open/mondrian/testsrc/main/mondrian/test/RaggedHierarchyTest.java#22 $
// This software is subject to the terms of the Eclipse Public License v1.0
// Agreement, available at the following URL:
// http://www.eclipse.org/legal/epl-v10.html.
// Copyright (C) 2004-2011 Julian Hyde
// All Rights Reserved.
// You must accept the terms of that agreement to use this software.
*/
package mondrian.test;

import mondrian.spi.Dialect;

/**
 * <code>RaggedHierarchyTest</code> tests ragged hierarchies.
 * <p>
 * I have disabled some tests by prefixing the tests name with "dont_".
 *
 * @author jhyde
 * @since Apr 19, 2004
 * @version $Id: //open/mondrian/testsrc/main/mondrian/test/RaggedHierarchyTest.java#22 $
 */
public class RaggedHierarchyTest extends FoodMartTestCase {
    private void assertRaggedReturns(String expression, String expected) {
        getTestContext().withCube("[Sales Ragged]")
            .assertAxisReturns(expression, expected);
    }

    // ~ The tests ------------------------------------------------------------

    public void testChildrenOfRoot() {
        assertRaggedReturns(
            "[Store].children",
            "[Store].[Canada]\n"
            + "[Store].[Israel]\n"
            + "[Store].[Mexico]\n"
            + "[Store].[USA]\n"
            + "[Store].[Vatican]");
    }

    public void testChildrenOfUSA() {
        assertRaggedReturns(
            "[Store].[USA].children",
            "[Store].[USA].[CA]\n"
            + "[Store].[USA].[OR]\n"
            + "[Store].[USA].[USA].[Washington]\n"
            + "[Store].[USA].[WA]");
    }

    // Israel has one real child, which is hidden, and which has children
    // Haifa and Tel Aviv
    public void testChildrenOfIsrael() {
        assertRaggedReturns(
            "[Store].[Israel].children",
            "[Store].[Israel].[Israel].[Haifa]\n"
            + "[Store].[Israel].[Israel].[Tel Aviv]");
    }

    // disabled: (1) does not work with SmartMemberReader and
    // (2) test returns [null] member
    // Vatican's descendants at the province and city level are hidden
    public void dont_testChildrenOfVatican() {
        assertRaggedReturns(
            "[Store].[Vatican].children",
            "[Store].[Vatican].[Vatican].[null].[Store 17]");
    }

    public void testParentOfHaifa() {
        assertRaggedReturns(
            "[Store].[Israel].[Haifa].Parent", "[Store].[Israel]");
    }

    public void testParentOfVatican() {
        assertRaggedReturns(
            "[Store].[Vatican].Parent", "[Store].[All Stores]");
    }

    // PrevMember must return something at the same level -- a city
    public void testPrevMemberOfHaifa() {
        assertRaggedReturns(
            "[Store].[Israel].[Haifa].PrevMember",
            "[Store].[Canada].[BC].[Victoria]");
    }

    // PrevMember must return something at the same level -- a city
    public void testNextMemberOfTelAviv() {
        assertRaggedReturns(
            "[Store].[Israel].[Tel Aviv].NextMember",
            "[Store].[Mexico].[DF].[Mexico City]");
    }

    public void testNextMemberOfBC() {
        // The next state after BC is Israel, but it's hidden
        assertRaggedReturns(
            "[Store].[Canada].[BC].NextMember",
            "[Store].[Mexico].[DF]");
    }

    public void testLead() {
        assertRaggedReturns(
            "[Store].[Mexico].[DF].Lead(1)",
            "[Store].[Mexico].[Guerrero]");
        assertRaggedReturns(
            "[Store].[Mexico].[DF].Lead(0)",
            "[Store].[Mexico].[DF]");
        // Israel is immediately before Mexico, but is hidden
        assertRaggedReturns(
            "[Store].[Mexico].[DF].Lead(-1)",
            "[Store].[Canada].[BC]");
        assertRaggedReturns(
            "[Store].[Mexico].[DF].Lag(1)",
            "[Store].[Canada].[BC]");
        // Fall off the edge of the world
        assertRaggedReturns(
            "[Store].[Mexico].[DF].Lead(-2)", "");
        assertRaggedReturns(
            "[Store].[Mexico].[DF].Lead(-543)", "");
    }

    // disabled: (1) does not work with SmartMemberReader and (2) test returns
    // [null] member
    public void dont_testDescendantsOfVatican() {
        assertRaggedReturns(
            "Descendants([Store].[Vatican])",
            "[Store].[Vatican]\n"
            + "[Store].[Vatican].[Vatican].[null].[Store 17]");
    }

    // The only child of Vatican at state level is hidden
    public void testDescendantsOfVaticanAtStateLevel() {
        assertRaggedReturns(
            "Descendants([Store].[Vatican], [Store].[Store State])",
            "");
    }

    public void testDescendantsOfRootAtCity() {
        assertRaggedReturns(
            "Descendants([Store], [Store City])",
            "[Store].[Canada].[BC].[Vancouver]\n"
            + "[Store].[Canada].[BC].[Victoria]\n"
            + "[Store].[Israel].[Israel].[Haifa]\n"
            + "[Store].[Israel].[Israel].[Tel Aviv]\n"
            + "[Store].[Mexico].[DF].[Mexico City]\n"
            + "[Store].[Mexico].[DF].[San Andres]\n"
            + "[Store].[Mexico].[Guerrero].[Acapulco]\n"
            + "[Store].[Mexico].[Jalisco].[Guadalajara]\n"
            + "[Store].[Mexico].[Veracruz].[Orizaba]\n"
            + "[Store].[Mexico].[Yucatan].[Merida]\n"
            + "[Store].[Mexico].[Zacatecas].[Camacho]\n"
            + "[Store].[Mexico].[Zacatecas].[Hidalgo]\n"
            + "[Store].[USA].[CA].[Alameda]\n"
            + "[Store].[USA].[CA].[Beverly Hills]\n"
            + "[Store].[USA].[CA].[Los Angeles]\n"
            + "[Store].[USA].[CA].[San Francisco]\n"
            + "[Store].[USA].[OR].[Portland]\n"
            + "[Store].[USA].[OR].[Salem]\n"
            + "[Store].[USA].[USA].[Washington]\n"
            + "[Store].[USA].[WA].[Bellingham]\n"
            + "[Store].[USA].[WA].[Bremerton]\n"
            + "[Store].[USA].[WA].[Seattle]\n"
            + "[Store].[USA].[WA].[Spokane]");
    }

    // no ancestor at the State level
    public void testAncestorOfHaifa() {
        assertRaggedReturns(
            "Ancestor([Store].[Israel].[Haifa], [Store].[Store State])",
            "");
    }

    public void testHierarchize() {
        // Haifa and Tel Aviv should appear directly after Israel
        // Vatican should have no children
        // Washington should appear after WA
        assertRaggedReturns(
            "Hierarchize(Descendants([Store], [Store].[Store City], SELF_AND_BEFORE))",
            "[Store].[All Stores]\n"
            + "[Store].[Canada]\n"
            + "[Store].[Canada].[BC]\n"
            + "[Store].[Canada].[BC].[Vancouver]\n"
            + "[Store].[Canada].[BC].[Victoria]\n"
            + "[Store].[Israel]\n"
            + "[Store].[Israel].[Israel].[Haifa]\n"
            + "[Store].[Israel].[Israel].[Tel Aviv]\n"
            + "[Store].[Mexico]\n"
            + "[Store].[Mexico].[DF]\n"
            + "[Store].[Mexico].[DF].[Mexico City]\n"
            + "[Store].[Mexico].[DF].[San Andres]\n"
            + "[Store].[Mexico].[Guerrero]\n"
            + "[Store].[Mexico].[Guerrero].[Acapulco]\n"
            + "[Store].[Mexico].[Jalisco]\n"
            + "[Store].[Mexico].[Jalisco].[Guadalajara]\n"
            + "[Store].[Mexico].[Veracruz]\n"
            + "[Store].[Mexico].[Veracruz].[Orizaba]\n"
            + "[Store].[Mexico].[Yucatan]\n"
            + "[Store].[Mexico].[Yucatan].[Merida]\n"
            + "[Store].[Mexico].[Zacatecas]\n"
            + "[Store].[Mexico].[Zacatecas].[Camacho]\n"
            + "[Store].[Mexico].[Zacatecas].[Hidalgo]\n"
            + "[Store].[USA]\n"
            + "[Store].[USA].[CA]\n"
            + "[Store].[USA].[CA].[Alameda]\n"
            + "[Store].[USA].[CA].[Beverly Hills]\n"
            + "[Store].[USA].[CA].[Los Angeles]\n"
            + "[Store].[USA].[CA].[San Francisco]\n"
            + "[Store].[USA].[OR]\n"
            + "[Store].[USA].[OR].[Portland]\n"
            + "[Store].[USA].[OR].[Salem]\n"
            + "[Store].[USA].[USA].[Washington]\n"
            + "[Store].[USA].[WA]\n"
            + "[Store].[USA].[WA].[Bellingham]\n"
            + "[Store].[USA].[WA].[Bremerton]\n"
            + "[Store].[USA].[WA].[Seattle]\n"
            + "[Store].[USA].[WA].[Spokane]\n"
            + "[Store].[Vatican]");
    }

    /**
     * Make sure that the numbers are right!
     *
     * <p>The Vatican is the tricky case,
     * because one of the columns is null, so the SQL generator might get
     * confused.
     */
    // disabled: (1) does not work with SmartMemberReader and (2) test returns
    // [null] member
    public void dont_testMeasuresVatican() {
        assertQueryReturns(
            "SELECT {[Measures].[Unit Sales]} ON COLUMNS,\n"
            + " {Descendants([Store].[Vatican])} ON ROWS\n"
            + "FROM [Sales Ragged]",
            "Axis #0:\n"
            + "{}\n"
            + "Axis #1:\n"
            + "{[Measures].[Unit Sales]}\n"
            + "Axis #2:\n"
            + "{[Store].[Vatican]}\n"
            + "{[Store].[Vatican].[Vatican].[null].[Store 17]}\n"
            + "Row #0: 35,257\n"
            + "Row #1: 35,257\n");
    }

    // Make sure that the numbers are right!
    /**
     * disabled: (1) does not work with SmartMemberReader and (2) test returns
     * [null] member?
     */
    public void dont_testMeasures() {
        assertQueryReturns(
            "SELECT {[Measures].[Unit Sales]} ON COLUMNS,\n"
            + " NON EMPTY {Descendants([Store])} ON ROWS\n"
            + "FROM [Sales Ragged]",
            "Axis #0:\n"
            + "{}\n"
            + "Axis #1:\n"
            + "{[Measures].[Unit Sales]}\n"
            + "Axis #2:\n"
            + "{[Store].[All Stores]}\n"
            + "{[Store].[Israel]}\n"
            + "{[Store].[Israel].[Israel].[Haifa]}\n"
            + "{[Store].[Israel].[Israel].[Haifa].[Store 22]}\n"
            + "{[Store].[Israel].[Israel].[Tel Aviv]}\n"
            + "{[Store].[Israel].[Israel].[Tel Aviv].[Store 23]}\n"
            + "{[Store].[USA]}\n"
            + "{[Store].[USA].[CA]}\n"
            + "{[Store].[USA].[CA].[Beverly Hills]}\n"
            + "{[Store].[USA].[CA].[Beverly Hills].[Store 6]}\n"
            + "{[Store].[USA].[CA].[Los Angeles]}\n"
            + "{[Store].[USA].[CA].[Los Angeles].[Store 7]}\n"
            + "{[Store].[USA].[CA].[San Francisco]}\n"
            + "{[Store].[USA].[CA].[San Francisco].[Store 14]}\n"
            + "{[Store].[USA].[OR]}\n"
            + "{[Store].[USA].[OR].[Portland]}\n"
            + "{[Store].[USA].[OR].[Portland].[Store 11]}\n"
            + "{[Store].[USA].[OR].[Salem]}\n"
            + "{[Store].[USA].[OR].[Salem].[Store 13]}\n"
            + "{[Store].[USA].[USA].[Washington]}\n"
            + "{[Store].[USA].[USA].[Washington].[Store 24]}\n"
            + "{[Store].[USA].[WA]}\n"
            + "{[Store].[USA].[WA].[Bellingham]}\n"
            + "{[Store].[USA].[WA].[Bellingham].[Store 2]}\n"
            + "{[Store].[USA].[WA].[Bremerton]}\n"
            + "{[Store].[USA].[WA].[Bremerton].[Store 3]}\n"
            + "{[Store].[USA].[WA].[Seattle]}\n"
            + "{[Store].[USA].[WA].[Seattle].[Store 15]}\n"
            + "{[Store].[USA].[WA].[Spokane]}\n"
            + "{[Store].[USA].[WA].[Spokane].[Store 16]}\n"
            + "{[Store].[Vatican]}\n"
            + "{[Store].[Vatican].[Vatican].[null].[Store 17]}\n"
            + "Row #0: 266,773\n"
            + "Row #1: 13,694\n"
            + "Row #2: 2,203\n"
            + "Row #3: 2,203\n"
            + "Row #4: 11,491\n"
            + "Row #5: 11,491\n"
            + "Row #6: 217,822\n"
            + "Row #7: 49,113\n"
            + "Row #8: 21,333\n"
            + "Row #9: 21,333\n"
            + "Row #10: 25,663\n"
            + "Row #11: 25,663\n"
            + "Row #12: 2,117\n"
            + "Row #13: 2,117\n"
            + "Row #14: 67,659\n"
            + "Row #15: 26,079\n"
            + "Row #16: 26,079\n"
            + "Row #17: 41,580\n"
            + "Row #18: 41,580\n"
            + "Row #19: 25,635\n"
            + "Row #20: 25,635\n"
            + "Row #21: 75,415\n"
            + "Row #22: 2,237\n"
            + "Row #23: 2,237\n"
            + "Row #24: 24,576\n"
            + "Row #25: 24,576\n"
            + "Row #26: 25,011\n"
            + "Row #27: 25,011\n"
            + "Row #28: 23,591\n"
            + "Row #29: 23,591\n"
            + "Row #30: 35,257\n"
            + "Row #31: 35,257\n");
    }

    /**
     * Test case for bug
     * <a href="http://jira.pentaho.com/browse/MONDRIAN-628">MONDRIAN-628</a>,
     * "ClassCastException in Mondrian for query using Sales Ragged cube".
     *
     * <p>Cause was that ancestor yielded a null member, which was a RolapMember
     * but Order required it to be a RolapCubeMember.
     */
    public void testNullMember() {
        assertQueryReturns(
            "With \n"
            + " Set [*NATIVE_CJ_SET] as '[*BASE_MEMBERS_Geography]' \n"
            + " Set [*SORTED_ROW_AXIS] as 'Order([*CJ_ROW_AXIS],Ancestor([Geography].CurrentMember, [Geography].[Country]).OrderKey,BASC,Ancestor([Geography].CurrentMember, [Geography].[State]).OrderKey,BASC,[Geography].CurrentMember.OrderKey,BASC)' \n"
            + " Set [*BASE_MEMBERS_Geography] as '[Geography].[City].Members' \n"
            + " Set [*NATIVE_MEMBERS_Geography] as 'Generate([*NATIVE_CJ_SET], {[Geography].CurrentMember})' \n"
            + " Set [*BASE_MEMBERS_Measures] as '{[Measures].[*ZERO]}' \n"
            + " Set [*CJ_ROW_AXIS] as 'Generate([*NATIVE_CJ_SET], {([Geography].currentMember)})' \n"
            + " Set [*CJ_COL_AXIS] as '[*NATIVE_CJ_SET]' \n"
            + " Member [Measures].[*ZERO] as '0', SOLVE_ORDER=0 \n"
            + " Select \n"
            + " [*BASE_MEMBERS_Measures] on columns, \n"
            + " [*SORTED_ROW_AXIS] on rows \n"
            + " From [Sales Ragged]",
            "Axis #0:\n"
            + "{}\n"
            + "Axis #1:\n"
            + "{[Measures].[*ZERO]}\n"
            + "Axis #2:\n"
            + "{[Geography].[Canada].[BC].[Vancouver]}\n"
            + "{[Geography].[Canada].[BC].[Victoria]}\n"
            + "{[Geography].[Israel].[Israel].[Haifa]}\n"
            + "{[Geography].[Israel].[Israel].[Tel Aviv]}\n"
            + "{[Geography].[Mexico].[DF].[Mexico City]}\n"
            + "{[Geography].[Mexico].[DF].[San Andres]}\n"
            + "{[Geography].[Mexico].[Guerrero].[Acapulco]}\n"
            + "{[Geography].[Mexico].[Jalisco].[Guadalajara]}\n"
            + "{[Geography].[Mexico].[Veracruz].[Orizaba]}\n"
            + "{[Geography].[Mexico].[Yucatan].[Merida]}\n"
            + "{[Geography].[Mexico].[Zacatecas].[Camacho]}\n"
            + "{[Geography].[Mexico].[Zacatecas].[Hidalgo]}\n"
            + "{[Geography].[USA].[USA].[Washington]}\n"
            + "{[Geography].[USA].[CA].[Alameda]}\n"
            + "{[Geography].[USA].[CA].[Beverly Hills]}\n"
            + "{[Geography].[USA].[CA].[Los Angeles]}\n"
            + "{[Geography].[USA].[CA].[San Francisco]}\n"
            + "{[Geography].[USA].[OR].[Portland]}\n"
            + "{[Geography].[USA].[OR].[Salem]}\n"
            + "{[Geography].[USA].[WA].[Bellingham]}\n"
            + "{[Geography].[USA].[WA].[Bremerton]}\n"
            + "{[Geography].[USA].[WA].[Seattle]}\n"
            + "{[Geography].[USA].[WA].[Spokane]}\n"
            + "Row #0: 0\n"
            + "Row #1: 0\n"
            + "Row #2: 0\n"
            + "Row #3: 0\n"
            + "Row #4: 0\n"
            + "Row #5: 0\n"
            + "Row #6: 0\n"
            + "Row #7: 0\n"
            + "Row #8: 0\n"
            + "Row #9: 0\n"
            + "Row #10: 0\n"
            + "Row #11: 0\n"
            + "Row #12: 0\n"
            + "Row #13: 0\n"
            + "Row #14: 0\n"
            + "Row #15: 0\n"
            + "Row #16: 0\n"
            + "Row #17: 0\n"
            + "Row #18: 0\n"
            + "Row #19: 0\n"
            + "Row #20: 0\n"
            + "Row #21: 0\n"
            + "Row #22: 0\n");
    }

    public void testHideIfBlankHidesWhitespace() {
        if (TestContext.instance().getDialect().getDatabaseProduct()
            != Dialect.DatabaseProduct.ORACLE)
        {
            return;
        }
        final TestContext testContext =
            TestContext.instance().createSubstitutingCube(
                "Sales",
                "<Dimension name=\"Gender4\" foreignKey=\"customer_id\">\n"
                + "    <Hierarchy hasAll=\"true\" allMemberName=\"All Gender\" primaryKey=\"customer_id\">\n"
                + "      <Table name=\"customer\"/>\n"
                + "      <Level name=\"Gender\" column=\"gender\" uniqueMembers=\"true\" hideMemberIf=\"IfBlankName\">\n"
                + "         <NameExpression> "
                + " <SQL dialect='generic'> "
                    +           "case \"gender\" "
                    +           "when 'F' then ' ' "
                    +           "when 'M' then 'M' "
                    + " end "
                    + "</SQL> "
                    + "</NameExpression>  "
                    + "      </Level>"
                    + "    </Hierarchy>\n"
                    + "  </Dimension>");
        testContext.assertQueryReturns(
            " select {[Gender4].[Gender].members} "
            + "on COLUMNS "
            + "from sales",
            "Axis #0:\n"
            + "{}\n"
            + "Axis #1:\n"
            + "{[Gender4].[M]}\n"
            + "Row #0: 135,215\n");
    }
}

// End RaggedHierarchyTest.java
