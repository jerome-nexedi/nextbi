package com.seekon.bicp.osgi.patch.internal;

import java.text.Collator;

import org.osgi.framework.wiring.BundleRevision;

import com.seekon.bicp.osgi.patch.PatchComparator;

public class ResourcePatchComparator implements PatchComparator {

  @Override
  public int compare(BundleRevision o1, BundleRevision o2) {
    return compareWithBundleName(o1.getBundle().getSymbolicName(), o2.getBundle()
      .getSymbolicName());
  }

  protected int compareWithBundleName(String bundleName0, String bundleName1) {
    Collator collator = Collator.getInstance(java.util.Locale.CHINA);
    return collator.compare(bundleName1.substring(bundleName1.lastIndexOf(".")),
      bundleName0.substring(bundleName0.lastIndexOf(".")));// 使用反向排序
  }
}
