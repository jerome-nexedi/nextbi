package com.seekon.nextbi.spreadsheet;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	static String[] nativeLibs = new String[] { "xerces-c_3_0", "icudt42",
			"icuuc42", "chinook.exe", "IEShims", "libeay32", "ssleay32", "ntwdblib",
			"libmysql", "php5ts", "libpalo_ng", "libconnectionpool",
			"palo_wss3", "php_macro_engine"/*, "ui_backend"*/ };

	@Override
	public void start(BundleContext context) throws Exception {
//		boolean isWindows = System.getProperty("os.name").toLowerCase().indexOf(
//				"windows") != -1;
//		String fileExtention = null;
//		if (isWindows) {
//			fileExtention = ".dll";
//		} else {
//			fileExtention = ".so";
//		}
//
//		for (int i = 0; i < nativeLibs.length; i++) {
//			String nativeFileName = nativeLibs[i];
//			if (nativeFileName.indexOf(".") < 0) {
//				nativeFileName += fileExtention;
//			}
//			try {
//				NativeLibUtils.loadBundleNativeLib(context.getBundle(), "/ocx/"
//						+ nativeFileName);
//			} catch (Throwable e) {
//				System.out.println(nativeFileName + ":加载失败");
//				e.printStackTrace();
//			}
//		}
	}

	@Override
	public void stop(BundleContext context) throws Exception {

	}

}
