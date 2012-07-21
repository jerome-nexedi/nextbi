package com.tensegrity.palo.xmla.builders;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Resource {
  private static String baseFunctions = null;

  public static final String getBaseFunctions() {
    if (baseFunctions == null)
      baseFunctions = getXMLString("baseFunctions.xml");
    return baseFunctions;
  }

  static final String getXMLString(String paramString) {
    InputStream localInputStream = Resource.class.getResourceAsStream(paramString);
    BufferedReader localBufferedReader = new BufferedReader(new InputStreamReader(
      localInputStream));
    StringBuffer localStringBuffer = new StringBuffer();
    try {
      String str = null;
      while ((str = localBufferedReader.readLine()) != null)
        localStringBuffer.append(str);
    } catch (IOException localIOException3) {
      localIOException3.printStackTrace();
    } finally {
      try {
        localInputStream.close();
      } catch (IOException localIOException4) {
        localIOException4.printStackTrace();
      }
    }
    return localStringBuffer.toString();
  }
}

/*
 * Location:
 * D:\server\apache-tomcat-5.5.20\webapps\Palo-Pivot\WEB-INF\lib\paloxmla.jar
 * Qualified Name: com.tensegrity.palo.xmla.builders.Resource JD-Core Version:
 * 0.5.4
 */