/*
// $Id: //open/mondrian/src/main/mondrian/rolap/agg/SparseSegmentDataset.java#13 $
// This software is subject to the terms of the Eclipse Public License v1.0
// Agreement, available at the following URL:
// http://www.eclipse.org/legal/epl-v10.html.
// Copyright (C) 2002-2002 Kana Software, Inc.
// Copyright (C) 2002-2011 Julian Hyde and others
// All Rights Reserved.
// You must accept the terms of that agreement to use this software.
//
// jhyde, 21 March, 2002
 */
package mondrian.rolap.agg;

import mondrian.olap.Util;
import mondrian.rolap.CellKey;
import mondrian.rolap.SqlStatement;

import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.SortedSet;

/**
 * A <code>SparseSegmentDataset</code> is a means of storing segment values
 * which is suitable when few of the combinations of keys have a value present.
 * 
 * <p>
 * The storage requirements are as follows. Key is 1 word for each dimension.
 * Hashtable entry is 3 words. Value is 1 word. Total space is (4 + d) * v. (May
 * also need hash table to ensure that values are only stored once.)
 * </p>
 * 
 * <p>
 * NOTE: This class is not synchronized.
 * </p>
 * 
 * @author jhyde
 * @since 21 March, 2002
 * @version $Id:
 *          //open/mondrian/src/main/mondrian/rolap/agg/SparseSegmentDataset
 *          .java#13 $
 */
class SparseSegmentDataset implements SegmentDataset {
	final Map<CellKey, Object> values = new HashMap<CellKey, Object>();

	SparseSegmentDataset(Segment segment) {
		Util.discard(segment);
	}

	public Object getObject(CellKey pos) {
		return values.get(pos);
	}

	public boolean isNull(CellKey pos) {
		// cf exists -- calls values.containsKey
		return values.get(pos) == null;
	}

	public int getInt(CellKey pos) {
		throw new UnsupportedOperationException();
	}

	public double getDouble(CellKey pos) {
		throw new UnsupportedOperationException();
	}

	public boolean exists(CellKey pos) {
		return values.containsKey(pos);
	}

	public void put(CellKey key, Object value) {
		values.put(key, value);
	}

	public Iterator<Map.Entry<CellKey, Object>> iterator() {
		return values.entrySet().iterator();
	}

	public double getBytes() {
		// assume a slot, key, and value are each 4 bytes
		return values.size() * 12;
	}

	public void populateFrom(int[] pos, SegmentDataset data, CellKey key) {
		values.put(CellKey.Generator.newCellKey(pos), data.getObject(key));
	}

	public void populateFrom(int[] pos, SegmentLoader.RowList rowList, int column) {
		final Object o = rowList.getObject(column);
		put(CellKey.Generator.newCellKey(pos), o);
	}

	public SqlStatement.Type getType() {
		return SqlStatement.Type.OBJECT;
	}

	public SegmentBody createSegmentBody(
			SortedSet<Comparable<?>>[] axisValueSets, boolean[] nullAxisFlags) {
		return new SparseSegmentBody(values, axisValueSets, nullAxisFlags);
	}
}

// End SparseSegmentDataset.java
