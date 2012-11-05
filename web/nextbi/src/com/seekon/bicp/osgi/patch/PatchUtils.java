package com.seekon.bicp.osgi.patch;

import org.osgi.framework.Bundle;

public class PatchUtils {

	public static String MANIFEST_KEY_IS_PATCH = "Is-Patch";

	public static String MANIFEST_KEY_FRAGMENT_HOST = "Fragment-Host";

	public static boolean isPatch(Bundle bundle) {
		boolean isPatch = false;
		try {
			isPatch = Boolean.valueOf(
					(String) bundle.getHeaders().get(MANIFEST_KEY_IS_PATCH))
					.booleanValue()
					&& (bundle.getHeaders().get(MANIFEST_KEY_FRAGMENT_HOST) != null);
		} catch (Exception e) {
		}
		return isPatch;
	}

}
