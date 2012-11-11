package com.tensegrity.palojava;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

  public void start(BundleContext context) throws Exception {
    try {
      String[] dllFileNameList = new String[] { "palojava.dll", "libpalo.dll" };
      for (int i = 0; i < dllFileNameList.length; i++) {
        installNativeDll(context, dllFileNameList[i]);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void stop(BundleContext conetxt) throws Exception {
  }

  private void installNativeDll(BundleContext context, String dllFileName)
    throws Exception {
    String jreHome = System.getProperty("java.home");
    URL url = context.getBundle().getResource(dllFileName);
    if (url != null) {
      InputStream is = null;
      OutputStream os = null;
      byte[] buffer = new byte[1024];
      try {
        is = url.openStream();
        os = new FileOutputStream(new File(jreHome + "/bin/" + dllFileName));
        for (int len = 0; (len = is.read(buffer)) != -1;)
          os.write(buffer, 0, len);
      } catch (IOException e) {
        e.printStackTrace();
      } finally {
        if (is != null)
          try {
            is.close();
          } catch (Exception localException3) {
          }
        if (os != null)
          try {
            os.close();
          } catch (Exception localException4) {
          }
      }
    }
  }
}
