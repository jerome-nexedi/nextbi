package org.osgi.service.http;

import java.io.IOException;
import java.net.URL;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract interface HttpContext {
  public static final String REMOTE_USER = "org.osgi.service.http.authentication.remote.user";

  public static final String AUTHENTICATION_TYPE = "org.osgi.service.http.authentication.type";

  public static final String AUTHORIZATION = "org.osgi.service.useradmin.authorization";

  public abstract boolean handleSecurity(HttpServletRequest paramHttpServletRequest,
    HttpServletResponse paramHttpServletResponse) throws IOException;

  public abstract URL getResource(String paramString);

  public abstract String getMimeType(String paramString);
}