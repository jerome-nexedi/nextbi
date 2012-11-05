package com.seekon.bicp.osgi.patch;

import com.seekon.bicp.osgi.patch.internal.ResourcePatchComparator;

public class PatchComparatorFactory {

  public static final String PATCH_TYPE_INSTALL = "patch.type.install";

  public static final String PATCH_TYPE_SOURCE = "patch.type.source";

  public static PatchComparator getPatchComparator(String patchType) {
    if(PATCH_TYPE_INSTALL.equals(patchType)){
      return null;//new InstallPatchComparator();
    }else if(PATCH_TYPE_SOURCE.equals(patchType)){
      return new ResourcePatchComparator();
    }
    return null;
  }
}
