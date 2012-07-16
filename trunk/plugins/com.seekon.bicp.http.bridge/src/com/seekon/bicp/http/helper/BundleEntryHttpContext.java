package com.seekon.bicp.http.helper;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.osgi.framework.Bundle;
import org.osgi.service.http.HttpContext;

public class BundleEntryHttpContext implements HttpContext {
  private Bundle bundle;

  private String bundlePath;

  public BundleEntryHttpContext(Bundle bundle) {
    this.bundle = bundle;
  }

  public BundleEntryHttpContext(Bundle b, String bundlePath) {
    this(b);
    if (bundlePath != null) {
      if (bundlePath.endsWith("/")) //$NON-NLS-1$
        bundlePath = bundlePath.substring(0, bundlePath.length() - 1);

      if (bundlePath.length() == 0)
        bundlePath = null;
    }
    this.bundlePath = bundlePath;
  }

  public String getMimeType(String arg0) {
    return null;
  }

  public boolean handleSecurity(HttpServletRequest arg0, HttpServletResponse arg1)
    throws IOException {
    return true;
  }

  public URL getResource(String resourceName) {
    URL resource = null;
    // int lastSlash = resourceName.lastIndexOf('/');
    // if (lastSlash == -1)
    // return null;
    //		
    // String path = resourceName.substring(0, lastSlash);
    // if (path.length() == 0)
    //			path = "/"; //$NON-NLS-1$
    // String file = resourceName.substring(lastSlash + 1);
    // Enumeration entryPaths = bundle.findEntries(path, file, false);
    //		
    // if (entryPaths != null && entryPaths.hasMoreElements())
    // return (URL) entryPaths.nextElement();
    //		
    // return null;
    if (bundlePath != null) {
      if (!resourceName.startsWith("/")) {
        resource = bundle.getResource(bundlePath + "/" + resourceName);
      } else {
        resource = bundle.getResource(bundlePath + resourceName);
      }
    }
    if (resource == null) {
      resource = bundle.getResource(resourceName);// //changed 2011-04-12
    }

    return resource;
  }

  public Set getResourcePaths(String path) {
    Enumeration entryPaths = null;
    if (bundlePath != null) {
      if (!path.startsWith("/")) {
        entryPaths = bundle.findEntries(bundlePath + "/" + path, null, false);
      } else {
        entryPaths = bundle.findEntries(bundlePath + path, null, false);
      }
    }
    if (entryPaths == null) {
      entryPaths = bundle.findEntries(path, null, false);
    }

    if (entryPaths == null)
      return null;

    Set result = new HashSet();
    while (entryPaths.hasMoreElements()) {
      URL entryURL = (URL) entryPaths.nextElement();
      String entryPath = entryURL.getFile();

      if (bundlePath == null)
        result.add(entryPath);
      else
        result.add(entryPath.substring(bundlePath.length()));
    }
    return result;
  }
}
