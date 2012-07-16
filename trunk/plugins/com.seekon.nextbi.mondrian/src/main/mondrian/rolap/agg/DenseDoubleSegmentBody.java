/*
// $Id: //open/mondrian/src/main/mondrian/rolap/agg/DenseDoubleSegmentBody.java#3 $
// This software is subject to the terms of the Eclipse Public License v1.0
// Agreement, available at the following URL:
// http://www.eclipse.org/legal/epl-v10.html.
// Copyright (C) 2011 Julian Hyde and others
// All Rights Reserved.
// You must accept the terms of that agreement to use this software.
 */
package mondrian.rolap.agg;

import java.util.*;

/**
 * Implementation of a segment body which stores the data inside a dense
 * primitive array of double precision numbers.
 * 
 * @author LBoudreau
 * @version $Id:
 *          //open/mondrian/src/main/mondrian/rolap/agg/DenseDoubleSegmentBody
 *          .java#3 $
 */
class DenseDoubleSegmentBody extends AbstractSegmentBody {
	private static final long serialVersionUID = 5775717165497921144L;
	final double[] data;
	private final int size;
	private final BitSet nullIndicators;

	DenseDoubleSegmentBody(BitSet nullIndicators, double[] dataToSave, int size,
			SortedSet<Comparable<?>>[] axisValueSets, boolean[] nullAxisFlags) {
		super(axisValueSets, nullAxisFlags);
		this.size = size;
		this.data = new double[size];
		System.arraycopy(dataToSave, 0, data, 0, size);
		this.nullIndicators = new BitSet(nullIndicators.length());
		this.nullIndicators.or(nullIndicators);
	}

	public SegmentDataset createSegmentDataset(Segment segment) {
		DenseDoubleSegmentDataset ds = new DenseDoubleSegmentDataset(segment,
				this.size);
		System.arraycopy(data, 0, ds.values, 0, this.size);
		ds.nullIndicators.clear();
		ds.nullIndicators.or(nullIndicators);
		return ds;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("DenseDoubleSegmentBody(size=" + size);
		sb.append(", data=");
		sb.append(Arrays.toString(data));
		sb.append(", nullIndicators=" + nullIndicators);
		sb.append(", axisValueSets=" + Arrays.toString(getAxisValueSets()));
		sb.append(", nullAxisFlags=" + Arrays.toString(getNullAxisFlags()));
		if (getAxisValueSets().length > 0) {
			if (getAxisValueSets()[0].iterator().hasNext()) {
				sb.append(", aVS[0]=" + getAxisValueSets()[0].getClass());
				sb.append(", aVS[0][0]=");
				sb.append(getAxisValueSets()[0].iterator().next().getClass());
			}
		}
		sb.append(")");
		return sb.toString();
	}
}

// End DenseDoubleSegmentBody.java
