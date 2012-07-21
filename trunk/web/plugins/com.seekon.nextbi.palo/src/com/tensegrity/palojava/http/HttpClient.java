/*     */package com.tensegrity.palojava.http;

/*     */
/*     */import com.tensegrity.palojava.ConnectionInfo; /*     */
import com.tensegrity.palojava.http.handlers.HeaderHandler; /*     */
import java.io.BufferedInputStream; /*     */
import java.io.BufferedOutputStream; /*     */
import java.io.IOException; /*     */
import java.io.InterruptedIOException; /*     */
import java.net.ConnectException; /*     */
import java.net.Socket; /*     */
import java.net.SocketException; /*     */
import java.net.UnknownHostException; /*     */
import java.util.ArrayList;

/*     */
/*     */public class HttpClient
/*     */{
  /* 62 */private static final byte[] CRLF = { 13, 10 };

  /*     */private final HttpConnection httpConnection;

  /*     */private Socket srvConnection;

  /*     */private BufferedOutputStream toServer;

  /*     */private BufferedInputStream fromServer;

  /*     */
  /*     */public HttpClient(HttpConnection httpConnection)
  /*     */{
    /* 83 */this.httpConnection = httpConnection;
    /*     */}

  /*     */
  /*     */final synchronized void reconnect(int timeout)
  /*     */throws UnknownHostException, IOException
  /*     */{
    /* 90 */ConnectionInfo connInfo = this.httpConnection.getInfo();
    /* 91 */int port = 0;
    /*     */try {
      /* 93 */port = Integer.parseInt(connInfo.getPort());
      /*     */} catch (NumberFormatException e) {
      /* 95 */throw new UnknownHostException(
        "Could not connect to Palo Server. Either no port, or a wrong port format, is specified.");
      /*     */}
    /* 97 */this.srvConnection = new Socket(connInfo.getHost(), port);
    /* 98 */this.srvConnection.setSoTimeout(timeout);
    /* 99 */int outSize = Math.min(this.srvConnection.getSendBufferSize(), 2048);
    /* 100 */int inSize = Math.min(this.srvConnection.getReceiveBufferSize(), 2048);
    /* 101 */this.toServer =
    /* 102 */new BufferedOutputStream(this.srvConnection.getOutputStream(),
    /* 102 */outSize);
    /* 103 */this.fromServer =
    /* 104 */new BufferedInputStream(this.srvConnection.getInputStream(),
    /* 104 */inSize);
    /*     */}

  /*     */
  /*     */final synchronized boolean isConnected()
  /*     */{
    /* 114 */return (this.srvConnection != null) &&
    /* 113 */(this.srvConnection.isConnected()) &&
    /* 114 */(!this.srvConnection.isClosed());
    /*     */}

  /*     */
  /*     */final synchronized void disconnect()
  /*     */throws IOException
  /*     */{
    /* 122 */if (this.srvConnection == null)
      /* 123 */return;
    /* 124 */this.srvConnection.close();
    /* 125 */this.srvConnection = null;
    /*     */}

  /*     */
  /*     */protected final synchronized String[] send(String request)
  /*     */throws ConnectException, IOException
  /*     */{
    /* 137 */BoundedInputStream in = null;
    /*     */try
    /*     */{
      /* 140 */this.toServer.write(
      /* 141 */request.getBytes("UTF-8"));
      /* 142 */this.toServer.write(CRLF);
      /* 143 */this.toServer.flush();
      /*     */
      /* 145 */HeaderHandler headerHandler =
      /* 146 */HeaderHandler.getInstance(this.httpConnection);
      /* 147 */headerHandler.parse(this.fromServer);
      /* 148 */int contentLength = headerHandler.getContentLength();
      /* 149 */if (contentLength == -1) {
        /* 150 */throw new ConnectException("No response from palo server!!");
        /*     */}
      /*     */
      /* 153 */in = new BoundedInputStream(this.fromServer, contentLength);
      /* 154 */ArrayList respLines = new ArrayList();
      /*     */while (true) {
        /* 156 */String response = HttpParser.readRawLine(in);
        /* 157 */if (response == null)
          break;
        if (response.trim().length() < 1) {
          /*     */break;
          /*     */}
        /* 160 */respLines.add(response);
        /*     */}
      /*     */String[] arrayOfString1;
      /* 162 */if (headerHandler.getErrorCode() != 200) {
        /* 163 */String[] result = (String[]) respLines
          .toArray(new String[respLines.size()]);
        /* 164 */if ((result != null) && (result.length > 0)) {
          /* 165 */result[0] = ("ERROR" + result[0]);
          /*     */}
        /* 167 */arrayOfString1 = result;
        /*     */return arrayOfString1;
        /*     */}

      /* 171 */return (String[]) respLines.toArray(new String[respLines.size()]);
      /*     */} catch (SocketException se) {
      /* 173 */this.httpConnection.serverDown();
      /* 174 */
      /*     */}
    /*     */catch (InterruptedIOException ie) {
      /*     */}
    /*     */finally {
      /* 179 */if (in != null)
        /* 180 */in.close();
      /*     */}
    return null;
    /*     */}
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.paloapi\lib\palo.jar
 * Qualified Name: com.tensegrity.palojava.http.HttpClient JD-Core Version:
 * 0.5.4
 */