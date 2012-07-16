/*
// $Id: //open/mondrian/testsrc/main/mondrian/olap/UtilTestCase.java#50 $
// This software is subject to the terms of the Eclipse Public License v1.0
// Agreement, available at the following URL:
// http://www.eclipse.org/legal/epl-v10.html.
// Copyright (C) 2004-2011 Julian Hyde and others
// All Rights Reserved.
// You must accept the terms of that agreement to use this software.
*/
package mondrian.olap;

import junit.framework.TestCase;

import java.util.*;
import java.sql.Driver;

import mondrian.util.*;

/**
 * Tests for methods in {@link mondrian.olap.Util} and, sometimes, classes in
 * the {@code mondrian.util} package.
 *
 * @version $Id: //open/mondrian/testsrc/main/mondrian/olap/UtilTestCase.java#50 $
 */
public class UtilTestCase extends TestCase {
    public UtilTestCase(String s) {
        super(s);
    }

    public void testParseConnectStringSimple() {
        // Simple connect string
        Util.PropertyList properties =
            Util.parseConnectString("foo=x;bar=y;foo=z");
        assertEquals("y", properties.get("bar"));
        assertEquals("y", properties.get("BAR")); // get is case-insensitive
        assertNull(properties.get(" bar")); // get does not ignore spaces
        assertEquals("z", properties.get("foo")); // later occurrence overrides
        assertNull(properties.get("kipper"));
        assertEquals(2, properties.list.size());
        assertEquals("foo=z; bar=y", properties.toString());
    }

    public void testParseConnectStringComplex() {
        Util.PropertyList properties =
            Util.parseConnectString(
                "normalProp=value;"
                + "emptyValue=;"
                + " spaceBeforeProp=abc;"
                + " spaceBeforeAndAfterProp =def;"
                + " space in prop = foo bar ;"
                + "equalsInValue=foo=bar;"
                + "semiInProp;Name=value;"
                + " singleQuotedValue = "
                + "'single quoted value ending in space ' ;"
                + " doubleQuotedValue = "
                + "\"=double quoted value preceded by equals\" ;"
                + " singleQuotedValueWithSemi = 'one; two';"
                + " singleQuotedValueWithSpecials = "
                + "'one; two \"three''four=five'");
        assertEquals(11, properties.list.size());
        String value;
        value = properties.get("normalProp");
        assertEquals("value", value);
        value = properties.get("emptyValue");
        assertEquals("", value); // empty string, not null!
        value = properties.get("spaceBeforeProp");
        assertEquals("abc", value);
        value = properties.get("spaceBeforeAndAfterProp");
        assertEquals("def", value);
        value = properties.get("space in prop");
        assertEquals(value, "foo bar");
        value = properties.get("equalsInValue");
        assertEquals("foo=bar", value);
        value = properties.get("semiInProp;Name");
        assertEquals("value", value);
        value = properties.get("singleQuotedValue");
        assertEquals("single quoted value ending in space ", value);
        value = properties.get("doubleQuotedValue");
        assertEquals("=double quoted value preceded by equals", value);
        value = properties.get("singleQuotedValueWithSemi");
        assertEquals(value, "one; two");
        value = properties.get("singleQuotedValueWithSpecials");
        assertEquals(value, "one; two \"three'four=five");

        assertEquals(
            "normalProp=value;"
            + " emptyValue=;"
            + " spaceBeforeProp=abc;"
            + " spaceBeforeAndAfterProp=def;"
            + " space in prop=foo bar;"
            + " equalsInValue=foo=bar;"
            + " semiInProp;Name=value;"
            + " singleQuotedValue=single quoted value ending in space ;"
            + " doubleQuotedValue==double quoted value preceded by equals;"
            + " singleQuotedValueWithSemi='one; two';"
            + " singleQuotedValueWithSpecials='one; two \"three''four=five'",
            properties.toString());
    }

    public void testConnectStringMore() {
        p("singleQuote=''''", "singleQuote", "'");
        p("doubleQuote=\"\"\"\"", "doubleQuote", "\"");
        p("empty= ;foo=bar", "empty", "");
    }

    /**
     * Test case for bug <a href="http://jira.pentaho.com/browse/MONDRIAN-397">
     * MONDRIAN-397, "Connect string parser gives
     * StringIndexOutOfBoundsException instead of a meaningful error"</a>.
     */
    public void testBugMondrian397() {
        Util.PropertyList properties;

        // ends in semi
        properties = Util.parseConnectString("foo=true; bar=xxx;");
        assertEquals(2, properties.list.size());

        // ends in semi+space
        properties = Util.parseConnectString("foo=true; bar=xxx; ");
        assertEquals(2, properties.list.size());

        // ends in space
        properties = Util.parseConnectString("   ");
        assertEquals(0, properties.list.size());

        // actual testcase for bug
        properties = Util.parseConnectString(
            "provider=mondrian; JdbcDrivers=org.hsqldb.jdbcDriver;"
            + "Jdbc=jdbc:hsqldb:./sql/sampledata;"
            + "Catalog=C:\\cygwin\\home\\src\\jfreereport\\engines\\classic"
            + "\\extensions-mondrian\\demo\\steelwheels.mondrian.xml;"
            + "JdbcUser=sa; JdbcPassword=; ");
        assertEquals(6, properties.list.size());
        assertEquals("", properties.get("JdbcPassword"));
    }

    /**
     * Checks that <code>connectString</code> contains a property called
     * <code>name</code>, whose value is <code>value</code>.
     */
    void p(String connectString, String name, String expectedValue) {
        Util.PropertyList list = Util.parseConnectString(connectString);
        String value = list.get(name);
        assertEquals(expectedValue, value);
    }

    public void testOleDbSpec() {
        p("Provider='MSDASQL'", "Provider", "MSDASQL");
        p("Provider='MSDASQL.1'", "Provider", "MSDASQL.1");

        if (false) {
        // If no Provider keyword is in the string, the OLE DB Provider for
        // ODBC (MSDASQL) is the default value. This provides backward
        // compatibility with ODBC connection strings. The ODBC connection
        // string in the following example can be passed in, and it will
        // successfully connect.
        p(
            "Driver={SQL Server};Server={localhost};Trusted_Connection={yes};"
            + "db={Northwind};", "Provider", "MSDASQL");
        }

        // Specifying a Keyword
        //
        // To identify a keyword used after the Provider keyword, use the
        // property description of the OLE DB initialization property that you
        // want to set. For example, the property description of the standard
        // OLE DB initialization property DBPROP_INIT_LOCATION is
        // Location. Therefore, to include this property in a connection
        // string, use the keyword Location.
        p(
            "Provider='MSDASQL';Location='3Northwind'",
            "Location",
            "3Northwind");
        // Keywords can contain any printable character except for the equal
        // sign (=).
        p(
            "Jet OLE DB:System Database=c:\\system.mda",
            "Jet OLE DB:System Database",
            "c:\\system.mda");
        p(
            "Authentication;Info=Column 5",
            "Authentication;Info",
            "Column 5");
        // If a keyword contains an equal sign (=), it must be preceded by an
        // additional equal sign to indicate that it is part of the keyword.
        p(
            "Verification==Security=True",
            "Verification=Security",
            "True");
        // If multiple equal signs appear, each one must be preceded by an
        // additional equal sign.
        p("Many====One=Valid", "Many==One", "Valid");
        p("TooMany===False", "TooMany=", "False");
        // Setting Values That Use Reserved Characters
        //
        // To include values that contain a semicolon, single-quote character,
        // or double-quote character, the value must be enclosed in double
        // quotes.
        p(
            "ExtendedProperties=\"Integrated Security='SSPI';"
            + "Initial Catalog='Northwind'\"",
            "ExtendedProperties",
            "Integrated Security='SSPI';Initial Catalog='Northwind'");
        // If the value contains both a semicolon and a double-quote character,
        // the value can be enclosed in single quotes.
        p(
            "ExtendedProperties='Integrated Security=\"SSPI\";"
            + "Databse=\"My Northwind DB\"'",
            "ExtendedProperties",
            "Integrated Security=\"SSPI\";Databse=\"My Northwind DB\"");
        // The single quote is also useful if the value begins with a
        // double-quote character.
        p(
            "DataSchema='\"MyCustTable\"'",
            "DataSchema",
            "\"MyCustTable\"");
        // Conversely, the double quote can be used if the value begins with a
        // single quote.
        p(
            "DataSchema=\"'MyOtherCustTable'\"",
            "DataSchema",
            "'MyOtherCustTable'");
        // If the value contains both single-quote and double-quote characters,
        // the quote character used to enclose the value must be doubled each
        // time it occurs within the value.
        p(
            "NewRecordsCaption='\"Company''s \"new\" customer\"'",
            "NewRecordsCaption",
            "\"Company's \"new\" customer\"");
        p(
            "NewRecordsCaption=\"\"\"Company's \"\"new\"\" customer\"\"\"",
            "NewRecordsCaption",
            "\"Company's \"new\" customer\"");
        // Setting Values That Use Spaces
        //
        // Any leading or trailing spaces around a keyword or value are
        // ignored. However, spaces within a keyword or value are allowed and
        // recognized.
        p("MyKeyword=My Value", "MyKeyword", "My Value");
        p("MyKeyword= My Value ;MyNextValue=Value", "MyKeyword", "My Value");
        // To include preceding or trailing spaces in the value, the value must
        // be enclosed in either single quotes or double quotes.
        p("MyKeyword=' My Value  '", "MyKeyword", " My Value  ");
        p("MyKeyword=\"  My Value \"", "MyKeyword", "  My Value ");
        if (false) {
            // (Not supported.)
            //
            // If the keyword does not correspond to a standard OLE DB
            // initialization property (in which case the keyword value is
            // placed in the Extended Properties (DBPROP_INIT_PROVIDERSTRING)
            // property), the spaces around the value will be included in the
            // value even though quote marks are not used. This is to support
            // backward compatibility for ODBC connection strings. Trailing
            // spaces after keywords might also be preserved.
        }
        if (false) {
            // (Not supported)
            //
            // Returning Multiple Values
            //
            // For standard OLE DB initialization properties that can return
            // multiple values, such as the Mode property, each value returned
            // is separated with a pipe (|) character. The pipe character can
            // have spaces around it or not.
            //
            // Example   Mode=Deny Write|Deny Read
        }
        // Listing Keywords Multiple Times
        //
        // If a specific keyword in a keyword=value pair occurs multiple times
        // in a connection string, the last occurrence listed is used in the
        // value set.
        p(
            "Provider='MSDASQL';Location='Northwind';"
            + "Cache Authentication='True';Prompt='Complete';"
            + "Location='Customers'",
            "Location",
            "Customers");
        // One exception to the preceding rule is the Provider keyword. If this
        // keyword occurs multiple times in the string, the first occurrence is
        // used.
        p(
            "Provider='MSDASQL';Location='Northwind'; Provider='SQLOLEDB'",
            "Provider",
            "MSDASQL");
        if (false) {
            // (Not supported)
            //
            // Setting the Window Handle Property
            //
            // To set the Window Handle (DBPROP_INIT_HWND) property in a
            // connection string, a long integer value is typically used.
        }
    }

    /**
     * Unit test for {@link Util#convertOlap4jConnectStringToNativeMondrian}.
     */
    public void testConvertConnectString() {
        assertEquals(
            "Provider=Mondrian; Datasource=jdbc/SampleData;"
            + "Catalog=foodmart/FoodMart.xml;",
            Util.convertOlap4jConnectStringToNativeMondrian(
                "jdbc:mondrian:Datasource=jdbc/SampleData;"
                + "Catalog=foodmart/FoodMart.xml;"));
    }

    public void testQuoteMdxIdentifier() {
        assertEquals(
            "[San Francisco]", Util.quoteMdxIdentifier("San Francisco"));
        assertEquals(
            "[a [bracketed]] string]",
            Util.quoteMdxIdentifier("a [bracketed] string"));
        assertEquals(
            "[Store].[USA].[California]",
            Util.quoteMdxIdentifier(
                Arrays.asList(
                    new Id.Segment("Store", Id.Quoting.QUOTED),
                    new Id.Segment("USA", Id.Quoting.QUOTED),
                    new Id.Segment("California", Id.Quoting.QUOTED))));
    }

    public void testQuoteJava() {
        assertEquals(
            "\"San Francisco\"", Util.quoteJavaString("San Francisco"));
        assertEquals(
            "\"null\"", Util.quoteJavaString("null"));
        assertEquals(
            "null", Util.quoteJavaString(null));
        assertEquals(
            "\"a\\\\b\\\"c\"", Util.quoteJavaString("a\\b\"c"));
    }

    public void testBufReplace() {
        // Replace with longer string. Search pattern at beginning & end.
        checkReplace("xoxox", "x", "yy", "yyoyyoyy");

        // Replace with shorter string.
        checkReplace("xxoxxoxx", "xx", "z", "zozoz");

        // Replace with empty string.
        checkReplace("xxoxxoxx", "xx", "", "oo");

        // Replacement string contains search string. (A bad implementation
        // might loop!)
        checkReplace("xox", "x", "xx", "xxoxx");

        // Replacement string combines with characters in the original to
        // match search string.
        checkReplace("cacab", "cab", "bb", "cabb");

        // Seek string does not exist.
        checkReplace(
            "the quick brown fox", "coyote", "wolf",
            "the quick brown fox");

        // Empty buffer.
        checkReplace("", "coyote", "wolf", "");

        // Empty seek string. This is a bit mean!
        checkReplace("fox", "", "dog", "dogfdogodogxdog");
    }

    private static void checkReplace(
        String original, String seek, String replace, String expected)
    {
        // Check whether the JDK does what we expect. (If it doesn't it's
        // probably a bug in the test, not the JDK.)
        assertEquals(expected, original.replaceAll(seek, replace));

        // Check the StringBuffer version of replace.
        StringBuilder buf = new StringBuilder(original);
        StringBuilder buf2 = Util.replace(buf, 0, seek, replace);
        assertTrue(buf == buf2);
        assertEquals(expected, buf.toString());

        // Check the String version of replace.
        assertEquals(expected, Util.replace(original, seek, replace));
    }

    public void testImplode() {
        List<Id.Segment> fooBar = Arrays.asList(
            new Id.Segment("foo", Id.Quoting.UNQUOTED),
            new Id.Segment("bar", Id.Quoting.UNQUOTED));
        assertEquals("[foo].[bar]", Util.implode(fooBar));

        List<Id.Segment> empty = Collections.emptyList();
        assertEquals("", Util.implode(empty));

        List<Id.Segment> nasty = Arrays.asList(
            new Id.Segment("string", Id.Quoting.UNQUOTED),
            new Id.Segment("with", Id.Quoting.UNQUOTED),
            new Id.Segment("a [bracket] in it", Id.Quoting.UNQUOTED));
        assertEquals(
            "[string].[with].[a [bracket]] in it]",
            Util.implode(nasty));
    }

    public void testParseIdentifier() {
        List<Id.Segment> strings =
                Util.parseIdentifier("[string].[with].[a [bracket]] in it]");
        assertEquals(3, strings.size());
        assertEquals("a [bracket] in it", strings.get(2).name);

        strings =
            Util.parseIdentifier("[Worklog].[All].[calendar-[LANGUAGE]].js]");
        assertEquals(3, strings.size());
        assertEquals("calendar-[LANGUAGE].js", strings.get(2).name);

        // allow spaces before, after and between
        strings = Util.parseIdentifier("  [foo] . [bar].[baz]  ");
        assertEquals(3, strings.size());
        assertEquals("foo", strings.get(0).name);

        // first segment not quoted
        strings = Util.parseIdentifier("Time.1997.[Q3]");
        assertEquals(3, strings.size());
        assertEquals("Time", strings.get(0).name);
        assertEquals("1997", strings.get(1).name);
        assertEquals("Q3", strings.get(2).name);

        // spaces ignored after unquoted segment
        strings = Util.parseIdentifier("[Time . Weekly ] . 1997 . [Q3]");
        assertEquals(3, strings.size());
        assertEquals("Time . Weekly ", strings.get(0).name);
        assertEquals("1997", strings.get(1).name);
        assertEquals("Q3", strings.get(2).name);

        // identifier ending in '.' is invalid
        try {
            strings = Util.parseIdentifier("[foo].[bar].");
            fail("expected exception, got " + strings);
        } catch (IllegalArgumentException e) {
            assertEquals(
                "Expected identifier after '.', "
                + "in member identifier '[foo].[bar].'",
                e.getMessage());
        }

        try {
            strings = Util.parseIdentifier("[foo].[bar");
            fail("expected exception, got " + strings);
        } catch (IllegalArgumentException e) {
            assertEquals(
                "Expected ']', in member identifier '[foo].[bar'",
                e.getMessage());
        }

        try {
            strings = Util.parseIdentifier("[Foo].[Bar], [Baz]");
            fail("expected exception, got " + strings);
        } catch (IllegalArgumentException e) {
            assertEquals(
                "Invalid member identifier '[Foo].[Bar], [Baz]'",
                e.getMessage());
        }
    }

    public void testReplaceProperties() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("foo", "bar");
        map.put("empty", "");
        map.put("null", null);
        map.put("foobarbaz", "bang!");
        map.put("malformed${foo", "groovy");

        assertEquals(
            "abarb",
            Util.replaceProperties("a${foo}b", map));
        assertEquals(
            "twicebarbar",
            Util.replaceProperties("twice${foo}${foo}", map));
        assertEquals(
            "bar at start",
            Util.replaceProperties("${foo} at start", map));
        assertEquals(
            "xyz",
            Util.replaceProperties("x${empty}y${empty}${empty}z", map));
        assertEquals(
            "x${nonexistent}bar",
            Util.replaceProperties("x${nonexistent}${foo}", map));

        // malformed tokens are left as is
        assertEquals(
            "${malformedbarbar",
            Util.replaceProperties("${malformed${foo}${foo}", map));

        // string can contain '$'
        assertEquals("x$foo", Util.replaceProperties("x$foo", map));

        // property with empty name is always ignored -- even if it's in the map
        assertEquals("${}", Util.replaceProperties("${}", map));
        map.put("", "v");
        assertEquals("${}", Util.replaceProperties("${}", map));

        // if a property's value is null, it's as if it doesn't exist
        assertEquals("${null}", Util.replaceProperties("${null}", map));

        // nested properties are expanded, but not recursively
        assertEquals(
            "${foobarbaz}",
            Util.replaceProperties("${foo${foo}baz}", map));
    }

    public void testWildcard() {
        assertEquals(
            ".\\QFoo\\E.|\\QBar\\E.*\\QBAZ\\E",
            Util.wildcardToRegexp(
                Arrays.asList("_Foo_", "Bar%BAZ")));
    }

    public void testCamel() {
        assertEquals(
            "FOO_BAR",
            Util.camelToUpper("FooBar"));
        assertEquals(
            "FOO_BAR",
            Util.camelToUpper("fooBar"));
        assertEquals(
            "URL",
            Util.camelToUpper("URL"));
        assertEquals(
            "URLTO_CLICK_ON",
            Util.camelToUpper("URLtoClickOn"));
        assertEquals(
            "",
            Util.camelToUpper(""));
    }

    public void testParseCommaList() {
        assertEquals(new ArrayList<String>(), Util.parseCommaList(""));
        assertEquals(Arrays.asList("x"), Util.parseCommaList("x"));
        assertEquals(Arrays.asList("x", "y"), Util.parseCommaList("x,y"));
        assertEquals(Arrays.asList("x,y"), Util.parseCommaList("x,,y"));
        assertEquals(Arrays.asList(",x", "y"), Util.parseCommaList(",,x,y"));
        assertEquals(Arrays.asList("x,", "y"), Util.parseCommaList("x,,,y"));
        assertEquals(Arrays.asList("x,,y"), Util.parseCommaList("x,,,,y"));
        // ignore trailing comma
        assertEquals(Arrays.asList("x", "y"), Util.parseCommaList("x,y,"));
        assertEquals(Arrays.asList("x", "y,"), Util.parseCommaList("x,y,,"));
    }

    public void testUnionIterator() {
        final List<String> xyList = Arrays.asList("x", "y");
        final List<String> abcList = Arrays.asList("a", "b", "c");
        final List<String> emptyList = Collections.emptyList();

        String total = "";
        for (String s : UnionIterator.over(xyList, abcList)) {
            total += s + ";";
        }
        assertEquals("x;y;a;b;c;", total);

        total = "";
        for (String s : UnionIterator.over(xyList, emptyList)) {
            total += s + ";";
        }
        assertEquals("x;y;", total);

        total = "";
        for (String s : UnionIterator.over(emptyList, xyList, emptyList)) {
            total += s + ";";
        }
        assertEquals("x;y;", total);

        total = "";
        for (String s : UnionIterator.<String>over()) {
            total += s + ";";
        }
        assertEquals("", total);

        total = "";
        UnionIterator<String> unionIterator =
            new UnionIterator<String>(xyList, abcList);
        while (unionIterator.hasNext()) {
            total += unionIterator.next() + ";";
        }
        assertEquals("x;y;a;b;c;", total);

        if (Util.Retrowoven) {
            // Retrowoven code gives 'ArrayStoreException' when it encounters
            // 'Util.union()' applied to java.util.Iterator objects.
            return;
        }

        total = "";
        for (String s : UnionIterator.over((Iterable<String>) xyList, abcList))
        {
            total += s + ";";
        }
        assertEquals("x;y;a;b;c;", total);
    }

    public void testAreOccurrencesEqual() {
        assertFalse(Util.areOccurencesEqual(Collections.<String>emptyList()));
        assertTrue(Util.areOccurencesEqual(Arrays.asList("x")));
        assertTrue(Util.areOccurencesEqual(Arrays.asList("x", "x")));
        assertFalse(Util.areOccurencesEqual(Arrays.asList("x", "y")));
        assertFalse(Util.areOccurencesEqual(Arrays.asList("x", "y", "x")));
        assertTrue(Util.areOccurencesEqual(Arrays.asList("x", "x", "x")));
        assertFalse(Util.areOccurencesEqual(Arrays.asList("x", "x", "y", "z")));
    }

    /**
     * Tests {@link mondrian.util.ServiceDiscovery}.
     */
    public void testServiceDiscovery() {
        final ServiceDiscovery<Driver>
            serviceDiscovery = ServiceDiscovery.forClass(Driver.class);
        final List<Class<Driver>> list = serviceDiscovery.getImplementor();
        assertFalse(list.isEmpty());

        // Check that discovered classes include AT LEAST:
        // JdbcOdbcDriver (in the JDK),
        // MondrianOlap4jDriver (in mondrian) and
        // XmlaOlap4jDriver (in olap4j.jar).
        List<String> expectedClassNames =
            new ArrayList<String>(
                Arrays.asList(
                    // Usually on the list, but not guaranteed:
                    // "sun.jdbc.odbc.JdbcOdbcDriver",
                    "mondrian.olap4j.MondrianOlap4jDriver",
                    "org.olap4j.driver.xmla.XmlaOlap4jDriver"));
        for (Class<Driver> driverClass : list) {
            expectedClassNames.remove(driverClass.getName());
        }
        if (Util.PreJdk15) {
            // JDK only discovers services from jars in JDK 1.5 and later.
            return;
        }
        assertTrue(expectedClassNames.toString(), expectedClassNames.isEmpty());
    }

    /**
     * Unit test for {@link mondrian.util.ArrayStack}.
     */
    public void testArrayStack() {
        final ArrayStack<String> stack = new ArrayStack<String>();
        assertEquals(0, stack.size());
        stack.add("a");
        assertEquals(1, stack.size());
        assertEquals("a", stack.peek());
        stack.push("b");
        assertEquals(2, stack.size());
        assertEquals("b", stack.peek());
        assertEquals("b", stack.pop());
        assertEquals(1, stack.size());
        stack.add(0, "z");
        assertEquals("a", stack.peek());
        assertEquals(2, stack.size());
        stack.push(null);
        assertEquals(3, stack.size());
        assertEquals(stack, Arrays.asList("z", "a", null));
        String z = "";
        for (String s : stack) {
            z += s;
        }
        assertEquals("zanull", z);
        stack.clear();
        try {
            String x = stack.peek();
            fail("expected error, got " + x);
        } catch (EmptyStackException e) {
            // ok
        }
        try {
            String x = stack.pop();
            fail("expected error, got " + x);
        } catch (EmptyStackException e) {
            // ok
        }
    }

    /**
     * Tests {@link Util#appendArrays(Object[], Object[][])}.
     */
    public void testAppendArrays() {
        String[] a0 = {"a", "b", "c"};
        String[] a1 = {"foo", "bar"};
        String[] empty = {};

        final String[] strings1 = Util.appendArrays(a0, a1);
        assertEquals(5, strings1.length);
        assertEquals(
            Arrays.asList("a", "b", "c", "foo", "bar"),
            Arrays.asList(strings1));

        final String[] strings2 = Util.appendArrays(
            empty, a0, empty, a1, empty);
        assertEquals(
            Arrays.asList("a", "b", "c", "foo", "bar"),
            Arrays.asList(strings2));

        Number[] n0 = {Math.PI};
        Integer[] i0 = {123, null, 45};
        Float[] f0 = {0f};

        final Number[] numbers = Util.appendArrays(n0, i0, f0);
        assertEquals(5, numbers.length);
        assertEquals(
            Arrays.asList((Number) Math.PI, 123, null, 45, 0f),
            Arrays.asList(numbers));
    }

    public void testCanCast() {
        assertTrue(Util.canCast(Collections.EMPTY_LIST, Integer.class));
        assertTrue(Util.canCast(Collections.EMPTY_LIST, String.class));
        assertTrue(Util.canCast(Collections.EMPTY_SET, String.class));
        assertTrue(Util.canCast(Arrays.asList(1, 2), Integer.class));
        assertTrue(Util.canCast(Arrays.asList(1, 2), Number.class));
        assertFalse(Util.canCast(Arrays.asList(1, 2), String.class));
        assertTrue(Util.canCast(Arrays.asList(1, null, 2d), Number.class));
        assertTrue(
            Util.canCast(
                new HashSet<Object>(Arrays.asList(1, null, 2d)),
                Number.class));
        assertFalse(Util.canCast(Arrays.asList(1, null, 2d), Integer.class));
    }

    /**
     * Unit test for {@link Util#parseLocale(String)} method.
     */
    public void testParseLocale() {
        Locale[] locales = {
            Locale.CANADA,
            Locale.CANADA_FRENCH,
            Locale.getDefault(),
            Locale.US,
            Locale.TRADITIONAL_CHINESE,
        };
        for (Locale locale : locales) {
            assertEquals(locale, Util.parseLocale(locale.toString()));
        }
        // Example locale names in Locale.toString() javadoc.
        String[] localeNames = {
            "en", "de_DE", "_GB", "en_US_WIN", "de__POSIX", "fr__MAC"
        };
        for (String localeName : localeNames) {
            assertEquals(localeName, Util.parseLocale(localeName).toString());
        }
    }

    /**
     * Unit test for {@link mondrian.util.LockBox}.
     */
    public void testLockBox() {
        final LockBox box =
            new LockBox();

        final String abc = "abc";
        final String xy = "xy";

        // Register an object.
        final LockBox.Entry abcEntry = box.register(abc);
        assertNotNull(abcEntry);
        assertSame(abc, abcEntry.getValue());
        checkMonikerValid(abcEntry.getMoniker());

        // Register another object
        final LockBox.Entry xyEntry = box.register(xy);
        checkMonikerValid(xyEntry.getMoniker());
        assertNotSame(abcEntry.getMoniker(), xyEntry.getMoniker());

        // Register first object again. Moniker is different. It is a different
        // registration.
        final LockBox.Entry abcEntry1 = box.register(abc);
        checkMonikerValid(abcEntry1.getMoniker());
        assertFalse(abcEntry1.getMoniker().equals(abcEntry.getMoniker()));
        assertSame(abcEntry1.getValue(), abc);

        // Retrieve.
        final LockBox.Entry abcEntry2 = box.get(abcEntry.getMoniker());
        assertNotNull(abcEntry2);
        assertSame(abcEntry2, abcEntry);
        assertNotSame(abcEntry2, abcEntry1);

        // Arbitrary moniker retrieves nothing. (A random moniker might,
        // with very very small probability, happen to match that of one of
        // the two registered entries. However, I know that our generation
        // scheme never generates monikers starting with 'x'.)
        assertNull(box.get("xxx"));

        // Deregister.
        final boolean b = box.deregister(abcEntry2);
        assertTrue(b);
        assertNull(box.get(abcEntry.getMoniker()));

        // Deregister again.
        final boolean b2 = box.deregister(abcEntry2);
        assertFalse(b2);
        assertNull(box.get(abcEntry.getMoniker()));

        // Object is still in entry, even though entry is no longer registered.
        // (Not that it matters, particularly.)
        assertSame(abc, abcEntry.getValue());
        assertNotNull(abcEntry.getMoniker());

        // Other registration of same object still works.
        assertSame(abcEntry1, box.get(abcEntry1.getMoniker()));
        assertSame(abcEntry1.getValue(), abc);

        // Other entry still exists.
        assertNotNull(box.get(xyEntry.getMoniker()));
        assertSame(xyEntry, box.get(xyEntry.getMoniker()));
        assertSame(xyEntry.getValue(), xy);

        // Register again. Moniker is different. (Monikers are never recycled.)
        final LockBox.Entry abcEntry3 = box.register(abc);
        checkMonikerValid(abcEntry3.getMoniker());
        assertFalse(abcEntry3.getMoniker().equals(abcEntry.getMoniker()));
        assertFalse(abcEntry3.getMoniker().equals(abcEntry1.getMoniker()));
        assertFalse(abcEntry3.getMoniker().equals(abcEntry2.getMoniker()));
        assertSame(abcEntry2.getValue(), abc);
    }

    void checkMonikerValid(String moniker) {
        final String digits = "0123456789";
        final String validChars =
            "0123456789"
            + "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
            + "abcdefghijklmnopqrstuvwxyz"
            + "$_";
        assertTrue(moniker.length() > 0);
        // all chars are valid
        for (int i = 0; i < moniker.length(); i++) {
            assertTrue(moniker, validChars.indexOf(moniker.charAt(i)) >= 0);
        }
        // does not start with digit
        assertFalse(moniker, digits.indexOf(moniker.charAt(0)) >= 0);
    }

    public void testCartesianProductList() {
        final CartesianProductList<String> list =
            new CartesianProductList<String>(
                Arrays.asList(
                    Arrays.asList("a", "b"),
                    Arrays.asList("1", "2", "3")));
        assertEquals(6, list.size());
        assertFalse(list.isEmpty());
        checkCartesianListContents(list);

        assertEquals(
            "[[a, 1], [a, 2], [a, 3], [b, 1], [b, 2], [b, 3]]",
            list.toString());

        // One element empty
        final CartesianProductList<String> list2 =
            new CartesianProductList<String>(
                Arrays.asList(
                    Arrays.<String>asList(),
                    Arrays.asList("1", "2", "3")));
        assertTrue(list2.isEmpty());
        assertEquals("[]", list2.toString());
        checkCartesianListContents(list2);

        // Other component empty
        final CartesianProductList<String> list3 =
            new CartesianProductList<String>(
                Arrays.asList(
                    Arrays.asList("a", "b"),
                    Arrays.<String>asList()));
        assertTrue(list3.isEmpty());
        assertEquals("[]", list3.toString());
        checkCartesianListContents(list3);

        // Zeroary
        final CartesianProductList<String> list4 =
            new CartesianProductList<String>(
                Collections.<List<String>>emptyList());
        assertFalse(list4.isEmpty());
//        assertEquals("[[]]", list4.toString());
        checkCartesianListContents(list4);

        // 1-ary
        final CartesianProductList<String> list5 =
            new CartesianProductList<String>(
                Collections.singletonList(
                    Arrays.asList("a", "b")));
        assertEquals("[[a], [b]]", list5.toString());
        checkCartesianListContents(list5);

        // 3-ary
        final CartesianProductList<String> list6 =
            new CartesianProductList<String>(
                Arrays.asList(
                    Arrays.asList("a", "b", "c", "d"),
                    Arrays.asList("1", "2"),
                    Arrays.asList("x", "y", "z")));
        assertEquals(24, list6.size()); // 4 * 2 * 3
        assertFalse(list6.isEmpty());
        assertEquals("[a, 1, x]", list6.get(0).toString());
        assertEquals("[a, 1, y]", list6.get(1).toString());
        assertEquals("[d, 2, z]", list6.get(23).toString());
        checkCartesianListContents(list6);

        final Object[] strings = new Object[6];
        list6.getIntoArray(1, strings);
        assertEquals(
            "[a, 1, y, null, null, null]",
            Arrays.asList(strings).toString());

        CartesianProductList<Object> list7 =
            new CartesianProductList<Object>(
                Arrays.<List<Object>>asList(
                    Arrays.<Object>asList(
                        "1",
                        Arrays.asList("2a", null, "2c"),
                        "3"),
                    Arrays.<Object>asList(
                        "a",
                        Arrays.asList("bb", "bbb"),
                        "c",
                        "d")));
        list7.getIntoArray(1, strings);
        assertEquals(
            "[1, bb, bbb, null, null, null]",
            Arrays.asList(strings).toString());
        list7.getIntoArray(5, strings);
        assertEquals(
            "[2a, null, 2c, bb, bbb, null]",
            Arrays.asList(strings).toString());
        checkCartesianListContents(list7);
    }

    private <T> void checkCartesianListContents(CartesianProductList<T> list) {
        List<List<T>> arrayList = new ArrayList<List<T>>();
        for (List<T> ts : list) {
            arrayList.add(ts);
        }
        assertEquals(arrayList, list);
    }

    public void testFlatList() {
        final List<String> flatAB = Util.flatList("a", "b");
        final List<String> arrayAB = Arrays.asList("a", "b");
        assertEquals(flatAB, flatAB);
        assertEquals(flatAB, arrayAB);
        assertEquals(arrayAB, flatAB);
        assertEquals(arrayAB.hashCode(), flatAB.hashCode());

        final List<String> flatABC = Util.flatList("a", "b", "c");
        final List<String> arrayABC = Arrays.asList("a", "b", "c");
        assertEquals(flatABC, flatABC);
        assertEquals(flatABC, arrayABC);
        assertEquals(arrayABC, flatABC);
        assertEquals(arrayABC.hashCode(), flatABC.hashCode());

        assertEquals("[a, b, c]", flatABC.toString());
        assertEquals("[a, b]", flatAB.toString());

        final List<String> arrayEmpty = Arrays.asList();
        final List<String> arrayA = Collections.singletonList("a");

        // mixed 2 & 3
        final List<List<String>> notAB =
            Arrays.asList(arrayEmpty, arrayA, arrayABC, flatABC);
        for (List<String> strings : notAB) {
            assertFalse(strings.equals(flatAB));
            assertFalse(flatAB.equals(strings));
        }
        final List<List<String>> notABC =
            Arrays.asList(arrayEmpty, arrayA, arrayAB, flatAB);
        for (List<String> strings : notABC) {
            assertFalse(strings.equals(flatABC));
            assertFalse(flatABC.equals(strings));
        }
    }

    /**
     * Unit test for {@link Composite#of(Iterable[])}.
     */
    public void testCompositeIterable() {
        final Iterable<String> beatles =
            Arrays.asList("john", "paul", "george", "ringo");
        final Iterable<String> stones =
            Arrays.asList("mick", "keef", "brian", "bill", "charlie");
        final List<String> empty = Collections.emptyList();

        final StringBuilder buf = new StringBuilder();
        for (String s : Composite.of(beatles, stones)) {
            buf.append(s).append(";");
        }
        assertEquals(
            "john;paul;george;ringo;mick;keef;brian;bill;charlie;",
            buf.toString());

        buf.setLength(0);
        for (String s : Composite.of(empty, stones)) {
            buf.append(s).append(";");
        }
        assertEquals(
            "mick;keef;brian;bill;charlie;",
            buf.toString());

        buf.setLength(0);
        for (String s : Composite.of(stones, empty)) {
            buf.append(s).append(";");
        }
        assertEquals(
            "mick;keef;brian;bill;charlie;",
            buf.toString());

        buf.setLength(0);
        for (String s : Composite.of(empty)) {
            buf.append(s).append(";");
        }
        assertEquals(
            "",
            buf.toString());

        buf.setLength(0);
        for (String s : Composite.of(empty, empty, beatles, empty, empty)) {
            buf.append(s).append(";");
        }
        assertEquals(
            "john;paul;george;ringo;",
            buf.toString());
    }

    /**
     * Unit test for {@link Util#lcidToLocale(short)}.
     */
    public void testLcid() {
        assertEquals("en_US", Util.lcidToLocale((short) 0x0409).toString());
        assertEquals("en_US", Util.lcidToLocale((short) 1033).toString());
        assertEquals("fr", Util.lcidToLocale((short) 0x040c).toString());
        assertEquals("en_GB", Util.lcidToLocale((short) 2057).toString());
    }

    /**
     * Unit test for {@link CombiningGenerator}.
     */
    public void testCombiningGenerator() {
        assertEquals(
            1,
            new CombiningGenerator<String>(Collections.<String>emptyList())
                .size());
        assertEquals(
            1,
            CombiningGenerator.of(Collections.<String>emptyList())
                .size());
        assertEquals(
            "[[]]",
            CombiningGenerator.of(Collections.<String>emptyList()).toString());
        assertEquals(
            "[[], [a]]",
            CombiningGenerator.of(Collections.singletonList("a")).toString());
        assertEquals(
            "[[], [a], [b], [a, b]]",
            CombiningGenerator.of(Arrays.asList("a", "b")).toString());
        assertEquals(
            "[[], [a], [b], [a, b], [c], [a, c], [b, c], [a, b, c]]",
            CombiningGenerator.of(Arrays.asList("a", "b", "c")).toString());

        final List<Integer> integerList =
            Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8);
        int i = 0;
        for (List<Integer> integers : CombiningGenerator.of(integerList)) {
            switch (i++) {
            case 0:
                assertTrue(integers.isEmpty());
                break;
            case 1:
                assertEquals(Arrays.asList(0), integers);
                break;
            case 6:
                assertEquals(Arrays.asList(1, 2), integers);
                break;
            case 131:
                assertEquals(Arrays.asList(0, 1, 7), integers);
                break;
            }
        }
        assertEquals(512, i);

        // Check that can iterate over 2^20 (~ 1m) elements in reasonable time.
        i = 0;
        for (List<String> xx
            : CombiningGenerator.of(Collections.nCopies(20, "x")))
        {
            Util.discard(xx);
            i++;
        }
        assertEquals(1 << 20, i);
    }
}

// End UtilTestCase.java
