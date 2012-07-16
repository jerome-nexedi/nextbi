/*
// $Id: //open/mondrian/testsrc/main/mondrian/test/clearview/HangerDimensionTest.java#2 $
// This software is subject to the terms of the Eclipse Public License v1.0
// Agreement, available at the following URL:
// http://www.eclipse.org/legal/epl-v10.html.
// Copyright (C) 2007-2009 Julian Hyde and others
// All Rights Reserved.
// You must accept the terms of that agreement to use this software.
//
*/
package mondrian.test.clearview;

import junit.framework.*;

import mondrian.test.*;

/**
 * <code>HangerDimensionTest</code> tests the extended syntax of Order
 * function. See { @link
 * http://pub.eigenbase.org/wiki/MondrianOrderFunctionExtension } for
 * syntax rules.
 * MDX queries and their expected results are maintained separately in
 * HangerDimensionTest.ref.xml file.If you would prefer to see them as inlined
 * Java string literals, run ant target "generateDiffRepositoryJUnit" and
 * then use file HangerDimensionTestJUnit.java which will be generated in
 * this directory.
 *
 * @author Khanh Vu
 * @version $Id: //open/mondrian/testsrc/main/mondrian/test/clearview/HangerDimensionTest.java#2 $
 */
public class HangerDimensionTest extends ClearViewBase {

    public HangerDimensionTest() {
        super();
    }

    public HangerDimensionTest(String name) {
        super(name);
    }

    public DiffRepository getDiffRepos() {
        return getDiffReposStatic();
    }

    private static DiffRepository getDiffReposStatic() {
        return DiffRepository.lookup(HangerDimensionTest.class);
    }

    public static TestSuite suite() {
        return constructSuite(getDiffReposStatic(), HangerDimensionTest.class);
    }

}

// End HangerDimensionTest.java
