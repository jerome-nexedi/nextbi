package com.seekon.framework.osgi.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

public class NativeLibUtils {

	public static void loadBundleNativeLib(Bundle bundle, String nativeLibPath)
			throws IOException {
		String userDir = System.getProperty("user.dir");

		File path = new File(userDir + "/tmp");
		if (!path.exists()) {
			path.mkdir();
		}
		
		String nativeLibName = nativeLibPath;
		int pos = nativeLibPath.lastIndexOf("/");
		if(pos > -1){
			nativeLibName = nativeLibName.substring(pos, nativeLibName.length());
		}
		
		File file = new File(path + nativeLibName);
		if (!file.exists()) {
			file.createNewFile();
			createJNativeLibTempFile(bundle.getBundleContext(), file, nativeLibPath);
		}

		System.load(file.getCanonicalPath());
	}

	private static void createJNativeLibTempFile(BundleContext context,
			File file, String jnativeLibPath) {
		OutputStream os = null;
		InputStream is = null;
		try {
			os = new FileOutputStream(file);
			is = context.getBundle().getResource(jnativeLibPath).openStream();
			if (is != null) {
				byte[] content = new byte[1024];
				int size = is.read(content);
				while (size > 0) {
					os.write(content, 0, size);
					size = is.read(content);
				}
			}

		} catch (Throwable e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (Exception e) {
				}
			}
			if (os != null) {
				try {
					os.flush();
					os.close();
				} catch (Exception e) {
				}
			}
		}
	}
}
