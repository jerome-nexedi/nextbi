/*      */package com.tensegrity.palojava.http;

/*      */
/*      */import com.tensegrity.palojava.PaloException; /*      */
import java.io.IOException; /*      */
import java.net.ConnectException; /*      */
import java.util.TimerTask;

/*      */
/*      */class ConnectionTimerTask extends TimerTask
/*      */{
  /*      */private HttpConnection httpConnection;

  /*      */
  /*      */public ConnectionTimerTask(HttpConnection httpConnection)
  /*      */{
    /* 1415 */this.httpConnection = httpConnection;
    /*      */}

  /*      */
  /*      */public void run()
  /*      */{
    /*      */try {
      /* 1421 */if (this.httpConnection.isConnected()) {
        /* 1422 */this.httpConnection.ensureConnection();
        /* 1423 */this.httpConnection.ping();
        /*      */} else {
        /* 1425 */this.httpConnection.reconnect();
        /*      */}
      /*      */} catch (ConnectException cex) {
      /* 1428 */this.httpConnection.serverDown();
      /*      */} catch (IOException localIOException) {
      /*      */}
    /*      */catch (PaloException pex) {
      /* 1432 */this.httpConnection.serverDown();
      /*      */}
    /*      */}
  /*      */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.paloapi\lib\palo.jar
 * Qualified Name: com.tensegrity.palojava.http.ConnectionTimerTask JD-Core
 * Version: 0.5.4
 */