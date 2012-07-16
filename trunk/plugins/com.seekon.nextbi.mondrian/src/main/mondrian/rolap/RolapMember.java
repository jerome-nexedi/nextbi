/*
// $Id: //open/mondrian/src/main/mondrian/rolap/RolapMember.java#98 $
// This software is subject to the terms of the Eclipse Public License v1.0
// Agreement, available at the following URL:
// http://www.eclipse.org/legal/epl-v10.html.
// Copyright (C) 2001-2002 Kana Software, Inc.
// Copyright (C) 2001-2010 Julian Hyde and others
// All Rights Reserved.
// You must accept the terms of that agreement to use this software.
//
// jhyde, 10 August, 2001
 */

package mondrian.rolap;

import mondrian.olap.*;

/**
 * A <code>RolapMember</code> is a member of a {@link RolapHierarchy}. There are
 * sub-classes for {@link RolapStoredMeasure}, {@link RolapCalculatedMember}.
 * 
 * @author jhyde
 * @since 10 August, 2001
 * @version $Id: //open/mondrian/src/main/mondrian/rolap/RolapMember.java#98 $
 */
public interface RolapMember extends Member, RolapCalculation {
  Object getKey();

  RolapMember getParentMember();

  RolapHierarchy getHierarchy();

  RolapLevel getLevel();

  /** @deprecated will be removed in mondrian-4.0 */
  boolean isAllMember();
}

// End RolapMember.java
