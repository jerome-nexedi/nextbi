package com.seekon.nextbi.spreadsheet.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;

public class JsonResponseServlet extends HttpServlet {

  private static final long serialVersionUID = 7545512402639631614L;

  protected void flushJsonResult(HttpServletResponse response, String result)
    throws IOException {
    if (result == null) {
      return;
    }

    String contentType = "application/json; charset=UTF-8";
    response.setContentType(contentType);
    response.setHeader("Content-Length", result.length() + "");
    response.setHeader("Cache-Control", "no-cache, must-revalidate");
    response.setHeader("Content-Type", contentType);

    PrintWriter out = response.getWriter();
    out.println(result);
    out.flush();
  }
}
