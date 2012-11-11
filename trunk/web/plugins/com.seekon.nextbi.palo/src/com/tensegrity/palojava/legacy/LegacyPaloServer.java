/*
 * (c) Tensegrity Software 2007
 * All rights reserved
 */
package com.tensegrity.palojava.legacy;

import com.tensegrity.palojava.DbConnection;
import com.tensegrity.palojava.PaloException;
import com.tensegrity.palojava.PaloServer;
import com.tensegrity.palojava.ServerInfo;
import com.tensegrity.palojava.impl.ServerInfoImpl;

/**
 * <code>LegacyPaloServer</code>
 * <p>
 * An implementation of the <code>PaloServer</code> interface which loads 
 * required libraries and instantiate a legacy connection.
 * </p>
 * 
 * @author Arnd Houben
 * @version $Id: LegacyPaloServer.java,v 1.1 2007/04/11 16:45:38 ArndHouben Exp $
 */
public class LegacyPaloServer implements PaloServer {

  private final ServerInfo srvInfo;

  private final LegacyConnection connection;

  private boolean hasInit;

  public LegacyPaloServer(String server, String service) {
    String version = "";
    try {
      System.loadLibrary("libpalo" + version);
    } catch (Throwable t) {
      t.printStackTrace();
      // ignore
      System.out.println("loading libpalo failed");
    }
    try {
      System.loadLibrary("palojava" + version);
    } catch (Throwable t) {
      t.printStackTrace();
      // ignore
      System.out.println("loading palojava failed");
    }
    connection = new LegacyConnection(server, service);
    init();
    srvInfo = new ServerInfoImpl(1, 1, 1, 1, 0, 2, true);
  }

  public DbConnection connect() {
    return connection;
  }

  public final void disconnect() {
    connection.disconnect();
  }

  public final ServerInfo getInfo() {
    return srvInfo;
  }

  public final boolean login(String username, String password) {
    return connection.login(username, password);
  }

  public final void ping() {
    throw new PaloException("LegacyPaloServer#ping(): NOT IMPLEMENTED!!");
    // TODO Auto-generated method stub

  }

  /**
   * Releases any resources associated with the internal used library 
   * functions.
   */
  public final synchronized void destroy() {
    if (!hasInit)
      return;
    destroy0();
    hasInit = false;
  }

  //--------------------------------------------------------------------------
  // PRIVATE METHODS
  //
  /**
   * Initializes this <code>Palo</code> instance if it was not initialized
   * before.
   */
  private final synchronized void init() {
    if (hasInit)
      return;
    if (init0() != 1) {
      throw new PaloException("failed to initialize native palo-library.");
    }
    hasInit = true;
  }

  //--------------------------------------------------------------------------
  // native stubs
  private native int init0();

  private native void destroy0();
}
