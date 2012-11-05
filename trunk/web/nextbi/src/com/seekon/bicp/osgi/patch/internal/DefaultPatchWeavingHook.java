package com.seekon.bicp.osgi.patch.internal;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import org.apache.felix.framework.cache.Content;
import org.osgi.framework.hooks.weaving.WeavingHook;
import org.osgi.framework.hooks.weaving.WovenClass;
import org.osgi.framework.wiring.BundleRevision;
import org.osgi.framework.wiring.BundleWiring;

import com.seekon.bicp.osgi.patch.PatchComparatorFactory;
import com.seekon.bicp.osgi.patch.PatchUtils;

/**
 * 
 * 为补丁提供支持的hook，改变类的加载
 *
 */
public class DefaultPatchWeavingHook implements WeavingHook {

  @Override
  public void weave(WovenClass wovenClass) {
    List<BundleRevision> fragments = null;
    BundleWiring bundleWiring = wovenClass.getBundleWiring();
    try {
      Field field = bundleWiring.getClass().getDeclaredField("m_fragments");
      field.setAccessible(true);
      fragments = (List<BundleRevision>) field.get(bundleWiring);
    } catch (Exception e) {
      e.printStackTrace();
    }

    if (fragments == null) {
      return;
    }

    String actualName = wovenClass.getClassName().replace('.', '/') + ".class";
    java.util.Collections.sort(fragments, PatchComparatorFactory
      .getPatchComparator(PatchComparatorFactory.PATCH_TYPE_SOURCE));
    for (BundleRevision fragment : fragments) {
      if (!PatchUtils.isPatch(fragment.getBundle())) {
        continue;
      }

      byte[] bytes = this.getFragmentEntryAsBytes(fragment, actualName);
      if (bytes != null) {
        wovenClass.setBytes(bytes);
        return;
      }
    }
  }

  private byte[] getFragmentEntryAsBytes(BundleRevision fragment, String actualName) {
    byte[] bytes = null;
    try {
      Method method = fragment.getClass().getDeclaredMethod("getContentPath", null);
      method.setAccessible(true);
      List<Content> contentPath = (List<Content>) method.invoke(fragment, null);
      for (int i = 0; (bytes == null) && (i < contentPath.size()); i++) {
        bytes = contentPath.get(i).getEntryAsBytes(actualName);
      }
    } catch (Exception e) {

    }

    return bytes;
  }

}
