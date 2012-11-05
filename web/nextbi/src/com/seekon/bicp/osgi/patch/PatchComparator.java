package com.seekon.bicp.osgi.patch;

import java.util.Comparator;

import org.osgi.framework.wiring.BundleRevision;

public interface PatchComparator extends Comparator<BundleRevision>{

  static final String MANIFEST_KEY_COMPARE = "Bundle-SymbolicName";
}
