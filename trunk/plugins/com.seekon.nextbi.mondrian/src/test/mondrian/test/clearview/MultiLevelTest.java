/*
// $Id: //open/mondrian/testsrc/main/mondrian/test/clearview/MultiLevelTest.java#3 $
// This software is subject to the terms of the Eclipse Public License v1.0
// Agreement, available at the following URL:
// http://www.eclipse.org/legal/epl-v10.html.
// Copyright (C) 2007-2007 Julian Hyde and others
// All Rights Reserved.
// You must accept the terms of that agreement to use this software.
//
 */
package mondrian.test.clearview;

import junit.framework.*;

import mondrian.test.*;

/**
 * <code>MultiLevelTest</code> is a test suite which tests complex queries
 * against the FoodMart database. MDX queries and their expected results are
 * maintained separately in MultiLevelTest.ref.xml file. If you would prefer to
 * see them as inlined Java string literals, run ant target
 * "generateDiffRepositoryJUnit" and then use file MultiLevelTestJUnit.java
 * which will be generated in this directory.
 * 
 * @author Khanh Vu
 * @version $Id:
 *          //open/mondrian/testsrc/main/mondrian/test/clearview/MultiLevelTest
 *          .java#3 $
 */
public class MultiLevelTest extends ClearViewBase {

  public MultiLevelTest() {
    super();
  }

  public MultiLevelTest(String name) {
    super(name);
  }

  public DiffRepository getDiffRepos() {
    return getDiffReposStatic();
  }

  private static DiffRepository getDiffReposStatic() {
    return DiffRepository.lookup(MultiLevelTest.class);
  }

  public static TestSuite suite() {
    return constructSuite(getDiffReposStatic(), MultiLevelTest.class);
  }

}

// End MultiLevelTest.java
