package com.tensegrity.palo.xmla;

import com.tensegrity.palojava.PaloException;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import sun.misc.BASE64Encoder;

public class XMLAClient
{
  public static final boolean IGNORE_VARIABLE_CUBES = Boolean.getBoolean("xmla_ignoreVariableCubes");
  URL url;
  boolean connOpen;
  HttpURLConnection urlConnection;
  private String requestType;
  DocumentBuilder builder;
  private static boolean verbose = false;
  private static boolean shortVerbose = false;
  private static boolean debug = false;
  private static boolean outputToFile = false;
  private static boolean shortOutputToFile = false;
  private static boolean disallowDebug = false;
  private static BufferedWriter writer = null;
  private String username;
  private String password;
  private String server;
  private String service;
  private XMLAServerInfo[] connections;
  private boolean globalIsSAP = false;
  private boolean globalIsSAPSet = false;
  ////private final int timeout;

  public XMLAClient(String paramString1, String paramString2, String paramString3, String paramString4)
    throws ParserConfigurationException
  {
    ////this.timeout = paramInt;
    if ((writer == null) && (((outputToFile) || (shortOutputToFile))))
      try
      {
        writer = new BufferedWriter(new FileWriter("C:\\Users\\PhilippBouillon\\Data\\SoapRequests.txt"));
      }
      catch (IOException localIOException)
      {
      }
    StringBuffer localStringBuffer = new StringBuffer(paramString1);
    if (!paramString1.endsWith("/"))
      localStringBuffer.append("/");
    if (paramString2.startsWith("/"))
      localStringBuffer.append(paramString2.substring(1));
    else
      localStringBuffer.append(paramString2);
    DocumentBuilderFactory localDocumentBuilderFactory = DocumentBuilderFactory.newInstance();
    try
    {
      this.connOpen = false;
      this.username = paramString3;
      this.password = paramString4;
      this.server = paramString1;
      this.service = paramString2;
      this.url = new URL(localStringBuffer.toString());
      this.builder = localDocumentBuilderFactory.newDocumentBuilder();
      this.connections = loadConnections();
    }
    catch (MalformedURLException localMalformedURLException1)
    {
      String str1 = localStringBuffer.toString().trim().toLowerCase();
      String str2 = localStringBuffer.toString().trim();
      int i = 0;
      if ((str1.startsWith("http")) && (str1.length() > 4))
      {
        str1 = str1.substring(4);
        str2 = str2.substring(4);
      }
      int j;
      do
        j = str1.charAt(i);
      while ((((j == 58) || (j == 47) || (j == 92))) && (++i < str1.length()));
      if (i < str1.length())
      {
        str1 = str1.substring(i);
        str2 = str2.substring(i);
      }
      str1 = "http://" + str2;
      try
      {
        this.url = new URL(str1);
        this.builder = localDocumentBuilderFactory.newDocumentBuilder();
        this.connections = loadConnections();
      }
      catch (MalformedURLException localMalformedURLException2)
      {
      }
    }
  }

  public static String getTextFromDOMElement(Node paramNode)
  {
    String str = "";
    if (paramNode.getNodeType() != 1)
      return "";
    NodeList localNodeList = paramNode.getChildNodes();
    int j = localNodeList.getLength();
    if (j > 1)
      for (int i = 0; ; ++i)
      {
        if (i >= j)
          return str;
        if (localNodeList.item(i).getNodeType() != 3)
          continue;
        str = str + localNodeList.item(i).getNodeValue();
      }
    if (j > 0)
      str = localNodeList.item(0).getNodeValue();
    else
      str = "";
    return str;
  }

  public String getUsername()
  {
    return this.username;
  }

  public String getPassword()
  {
    return this.password;
  }

  public String getServer()
  {
    return this.server;
  }

  public String getService()
  {
    return this.service;
  }

  private String getRequestType()
  {
    return (this.requestType == null) ? "DISCOVER_DATASOURCES" : this.requestType;
  }

  private void setRequestType(String paramString)
  {
    this.requestType = paramString;
  }

  private void setDiscoverMimeHeaders()
  {
    this.urlConnection.setRequestProperty("SOAPAction", "\"urn:schemas-microsoft-com:xml-analysis:Discover\"");
    this.urlConnection.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
    this.urlConnection.setRequestProperty("Accept", "application/soap+xml, application/dime, multipart/related, text/*");
  }

  private void setExecuteMimeHeaders()
  {
    this.urlConnection.setRequestProperty("SOAPAction", "\"urn:schemas-microsoft-com:xml-analysis:Execute\"");
    this.urlConnection.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
    this.urlConnection.setRequestProperty("Accept", "application/soap+xml, application/dime, multipart/related, text/*");
  }

  private boolean init()
  {
    if (this.connOpen)
      return true;
    try
    {
      this.urlConnection = ((HttpURLConnection)this.url.openConnection());
      this.urlConnection.setConnectTimeout(10000);
      ////this.urlConnection.setReadTimeout(this.timeout);
      String str = new BASE64Encoder().encode((this.username + ":" + this.password).getBytes());
      this.urlConnection.setRequestProperty("Authorization", "Basic " + str);
      this.urlConnection.setRequestMethod("POST");
      this.urlConnection.setDoInput(true);
      this.urlConnection.setDoOutput(true);
      this.connOpen = true;
    }
    catch (IOException localIOException)
    {
      localIOException.printStackTrace();
      return false;
    }
    return true;
  }

  public synchronized void disconnect()
  {
    if (this.urlConnection != null)
      try
      {
        this.urlConnection.disconnect();
      }
      catch (Exception localException)
      {
      }
    this.connOpen = false;
  }

  public static String xmlEncodeString(String paramString)
  {
    if (paramString == null)
      return "";
    char[] arrayOfChar = paramString.toCharArray();
    int i = 0;
    for (int j = 0; j < arrayOfChar.length; ++j)
      switch (arrayOfChar[j])
      {
      case '"':
      case '&':
      case '\'':
      case '<':
      case '>':
        i = 1;
        break;
      }
    if (i == 0)
      return paramString;
    StringBuffer localStringBuffer = new StringBuffer();
    for (int k = 0; k < arrayOfChar.length; ++k)
      switch (arrayOfChar[k])
      {
      case '&':
        localStringBuffer.append("&amp;");
        break;
      case '"':
        localStringBuffer.append("&quot;");
        break;
      case '\'':
        localStringBuffer.append("&apos;");
        break;
      case '<':
        localStringBuffer.append("&lt;");
        break;
      case '\r':
        localStringBuffer.append("&#xd;");
        break;
      case '>':
        localStringBuffer.append("&gt;");
        break;
      default:
        if (arrayOfChar[k] > '')
        {
          localStringBuffer.append("&#");
          localStringBuffer.append(arrayOfChar[k]);
          localStringBuffer.append(";");
        }
        else
        {
          localStringBuffer.append(arrayOfChar[k]);
        }
      }
    return localStringBuffer.toString();
  }

  public synchronized Document execute(String paramString, XMLAExecuteProperties paramXMLAExecuteProperties)
    throws IOException
  {
    if (!init())
    {
      disconnect();
      return null;
    }
    StringBuffer localStringBuffer = new StringBuffer("");
    localStringBuffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<SOAP-ENV:Envelope\n  xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\"\n  SOAP-ENV:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">\n  <SOAP-ENV:Body>\n    <Execute  xmlns=\"urn:schemas-microsoft-com:xml-analysis\"\n              SOAP-ENV:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">");
    localStringBuffer.append("\n   <Command>\n      <Statement>\n         " + xmlEncodeString(paramString) + "\n      </Statement>" + "\n   </Command>");
    localStringBuffer.append(paramXMLAExecuteProperties.getXML("    "));
    localStringBuffer.append("\n    </Execute>");
    localStringBuffer.append("\n  </SOAP-ENV:Body>");
    localStringBuffer.append("\n</SOAP-ENV:Envelope>");
    setExecuteMimeHeaders();
    Document localDocument = null;
    try
    {
      byte[] arrayOfByte = localStringBuffer.toString().getBytes("UTF8");
      if ((shortVerbose) && (!disallowDebug))
        System.out.println("MDX: " + paramString);
      Object localObject1;
      Object localObject2;
      Object localObject3;
      if ((verbose) && (!disallowDebug))
      {
        System.out.println("\nSending request [" + GregorianCalendar.getInstance().getTime() + "]: " + paramString);
        localObject1 = paramXMLAExecuteProperties.getCatalog();
        localObject2 = paramXMLAExecuteProperties.getDataSourceInfo();
        localObject3 = new StringBuffer();
        if (((String)localObject2).trim().length() != 0)
          ((StringBuffer)localObject3).append(((String)localObject2).trim());
        if (((String)localObject1).trim().length() != 0)
        {
          if (((StringBuffer)localObject3).length() != 0)
            ((StringBuffer)localObject3).append(", ");
          ((StringBuffer)localObject3).append(((String)localObject1).trim());
        }
        System.out.println(localStringBuffer);
      }
      if ((outputToFile) && (!disallowDebug))
        writer.write("\n\nSending SOAP Message (" + GregorianCalendar.getInstance().getTime() + "):\n" + localStringBuffer + "\n");
      if ((debug) && (!disallowDebug))
        System.out.println("Request length == " + localStringBuffer.length());
      writeToStream(arrayOfByte, this.urlConnection.getOutputStream());
      if ((this.urlConnection.getErrorStream() != null) && (verbose) && (!disallowDebug))
      {
        localObject1 = new BufferedReader(new InputStreamReader(this.urlConnection.getErrorStream()));
        while ((localObject2 = ((BufferedReader)localObject1).readLine()) != null)
          System.out.println((String)localObject2);
        ((BufferedReader)localObject1).close();
      }
      else if ((debug) && (!disallowDebug))
      {
        System.out.println("No error stream.");
      }
      try
      {
        localDocument = this.builder.parse(this.urlConnection.getInputStream());
      }
      catch (Exception localException2)
      {
        if ((this.urlConnection.getErrorStream() != null) && (verbose) && (!disallowDebug))
        {
          localObject2 = new BufferedReader(new InputStreamReader(this.urlConnection.getErrorStream()));
          while ((localObject3 = ((BufferedReader)localObject2).readLine()) != null)
            System.out.println((String)localObject3);
          ((BufferedReader)localObject2).close();
        }
        else if (debug)
        {
          System.out.println("No error stream.");
        }
      }
      NodeList localNodeList = localDocument.getElementsByTagName("SOAP-ENV:Fault");
      if ((verbose) && (!disallowDebug))
      {
        localObject2 = new PrintWriter(System.out);
        localObject3 = new XMLDocumentWriter((PrintWriter)localObject2);
        System.out.println("\nRECEIVED SOAP MESSAGE (" + GregorianCalendar.getInstance().getTime() + "):\n");
        ((XMLDocumentWriter)localObject3).write(localDocument.getChildNodes().item(0));
        ((PrintWriter)localObject2).flush();
      }
      if ((outputToFile) && (!disallowDebug))
      {
        writer.write("\n\nReceived SOAP message (" + GregorianCalendar.getInstance().getTime() + "):\n");
        localObject2 = new XMLDocumentWriter(null);
        writer.write(((XMLDocumentWriter)localObject2).write(localDocument.getChildNodes().item(0)));
        writer.flush();
      }
    }
    catch (Exception localException1)
    {
      localException1.printStackTrace();
    }
    finally
    {
      disconnect();
    }
    return (Document)(Document)(Document)localDocument;
  }

  private synchronized Document discover(XMLARestrictions paramXMLARestrictions, XMLAProperties paramXMLAProperties)
    throws IOException
  {
    if (!init())
    {
      disconnect();
      return null;
    }
    StringBuffer localStringBuffer = new StringBuffer("");
    localStringBuffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<SOAP-ENV:Envelope\n  xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\"\n  SOAP-ENV:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">\n  <SOAP-ENV:Body>\n    <Discover xmlns=\"urn:schemas-microsoft-com:xml-analysis\"\n              SOAP-ENV:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">");
    localStringBuffer.append("\n    <RequestType>" + getRequestType() + "</RequestType>");
    localStringBuffer.append(paramXMLARestrictions.getXML("    "));
    localStringBuffer.append(paramXMLAProperties.getXML("    "));
    localStringBuffer.append("\n    </Discover>");
    localStringBuffer.append("\n  </SOAP-ENV:Body>");
    localStringBuffer.append("\n</SOAP-ENV:Envelope>");
    setDiscoverMimeHeaders();
    Document localDocument = null;
    try
    {
      byte[] arrayOfByte = localStringBuffer.toString().getBytes("UTF8");
      if ((shortOutputToFile) && (!disallowDebug))
      {
        String str = "Send [" + getTimeString() + "]: " + getRequestType() + " with " + formatPropsAndRests(paramXMLARestrictions, paramXMLAProperties);
        System.out.println(str);
        writer.write(str + "\n");
        writer.flush();
      }
      if ((shortVerbose) && (!disallowDebug))
        System.out.println("Request: " + getRequestType() + " - " + formatPropsAndRests(paramXMLARestrictions, paramXMLAProperties));
      if ((verbose) && (!disallowDebug))
        System.out.println("\nSENDING SOAP MESSAGE (" + GregorianCalendar.getInstance().getTime() + "):\n" + localStringBuffer);
      if ((outputToFile) && (!disallowDebug))
        writer.write("\n\nSending SOAP message (" + GregorianCalendar.getInstance().getTime() + "):\n" + localStringBuffer);
      Object localObject2;
      Object localObject3;
      try
      {
        writeToStream(arrayOfByte, this.urlConnection.getOutputStream());
      }
      catch (Exception localException2)
      {
        if ((this.urlConnection.getErrorStream() != null) && (verbose) && (!disallowDebug))
        {
          localObject2 = new BufferedReader(new InputStreamReader(this.urlConnection.getErrorStream()));
          while ((localObject3 = ((BufferedReader)localObject2).readLine()) != null)
          {
            if (outputToFile)
            {
              writer.write((String)localObject3 + "\n");
              writer.flush();
            }
            System.out.println((String)localObject3);
          }
          ((BufferedReader)localObject2).close();
        }
      }
      try
      {
        localDocument = this.builder.parse(this.urlConnection.getInputStream());
      }
      catch (Exception localException3)
      {
        if ((this.urlConnection.getErrorStream() != null) && (verbose) && (!disallowDebug))
        {
          localObject2 = new BufferedReader(new InputStreamReader(this.urlConnection.getErrorStream()));
          while ((localObject3 = ((BufferedReader)localObject2).readLine()) != null)
          {
            if (outputToFile)
            {
              writer.write((String)localObject3 + "\n");
              writer.flush();
            }
            System.out.println((String)localObject3);
          }
          ((BufferedReader)localObject2).close();
        }
      }
      Object localObject1;
      if (localDocument != null)
      {
        localObject1 = localDocument.getElementsByTagName("SOAP-ENV:Fault");
        if ((verbose) && (!disallowDebug))
        {
          localObject2 = new PrintWriter(System.out);
          localObject3 = new XMLDocumentWriter((PrintWriter)localObject2);
          System.out.println("\nRECEIVED SOAP MESSAGE (" + GregorianCalendar.getInstance().getTime() + "):\n");
          ((XMLDocumentWriter)localObject3).write(localDocument.getChildNodes().item(0));
          ((PrintWriter)localObject2).flush();
        }
        if ((this.requestType.equals("DBSCHEMA_CATALOGS")) && (localObject1 != null) && (((NodeList)localObject1).getLength() != 0))
          throw new PaloException("Could not list databases. Reason: " + getErrorString((NodeList)localObject1));
      }
      if ((verbose) && (!disallowDebug))
      {
        localObject1 = new PrintWriter(System.out);
        localObject2 = new XMLDocumentWriter((PrintWriter)localObject1);
        System.out.println("\nRECEIVED SOAP MESSAGE (" + GregorianCalendar.getInstance().getTime() + "):\n");
        ((XMLDocumentWriter)localObject2).write(localDocument.getChildNodes().item(0));
        ((PrintWriter)localObject1).flush();
      }
      if ((outputToFile) && (!disallowDebug))
      {
        writer.write("\n\nReceived SOAP message (" + GregorianCalendar.getInstance().getTime() + "):\n");
        localObject1 = new XMLDocumentWriter(null);
        if (localDocument != null)
        {
          writer.write(((XMLDocumentWriter)localObject1).write(localDocument.getChildNodes().item(0)));
          writer.flush();
        }
      }
    }
    catch (PaloException localPaloException)
    {
      throw localPaloException;
    }
    catch (Exception localException1)
    {
      localException1.printStackTrace();
    }
    finally
    {
      disconnect();
    }
    return (Document)(Document)(Document)localDocument;
  }

  private final void parseServerData(XMLAServerInfo paramXMLAServerInfo, Node paramNode)
  {
    String str = paramNode.getNodeName();
    if (str.equals("DataSourceDescription"))
    {
      paramXMLAServerInfo.setDescription(getTextFromDOMElement(paramNode));
    }
    else if (str.equals("URL"))
    {
      paramXMLAServerInfo.setUrl(getTextFromDOMElement(paramNode));
    }
    else if (str.equals("DataSourceInfo"))
    {
      if (!debug)
        return;
      System.out.println("DataSourceInfo == " + getTextFromDOMElement(paramNode));
    }
    else if (str.equals("ProviderName"))
    {
      if (!debug)
        return;
      System.out.println("ProviderName == " + getTextFromDOMElement(paramNode));
    }
    else if (str.equals("ProviderType"))
    {
      if (!debug)
        return;
      System.out.println("ProviderType == " + getTextFromDOMElement(paramNode));
    }
    else if (str.equals("AuthenticationMode"))
    {
      paramXMLAServerInfo.setAuthentication(getTextFromDOMElement(paramNode));
      if (!debug)
        return;
      System.out.println("AuthenticationMode == " + getTextFromDOMElement(paramNode));
    }
    else
    {
      if (!debug)
        return;
      System.out.println("Unknown node name: " + str);
    }
  }

  private XMLAServerInfo[] loadConnections()
  {
    HashSet localHashSet = new HashSet();
    try
    {
      Document localDocument = discoverDataSources(new XMLARestrictions(), new XMLAProperties());
      if (localDocument == null)
        return null;
      NodeList localNodeList1 = localDocument.getElementsByTagName("row");
      int i = 0;
      int j = localNodeList1.getLength();
      while (i < j)
      {
        NodeList localNodeList2 = localNodeList1.item(i).getChildNodes();
        XMLAServerInfo localXMLAServerInfo = null;
        int k = 0;
        int l = 0;
        int i1 = localNodeList2.getLength();
        while (l < i1)
        {
          if (localNodeList2.item(l).getNodeType() == 1)
            if (localNodeList2.item(l).getNodeName().equals("DataSourceName"))
            {
              String str = getTextFromDOMElement(localNodeList2.item(l));
              localXMLAServerInfo = new XMLAServerInfo(str);
              localHashSet.add(localXMLAServerInfo);
            }
            else if (localXMLAServerInfo == null)
            {
              if (debug)
                System.out.println("This should not happen!");
              k = 1;
            }
            else
            {
              parseServerData(localXMLAServerInfo, localNodeList2.item(l));
            }
          ++l;
        }
        if (k != 0)
        {
          l = 0;
          i1 = localNodeList2.getLength();
          while (l < i1)
          {
            if ((localNodeList2.item(l).getNodeType() == 1) && (!localNodeList2.item(l).getNodeName().equals("DataSourceName")))
              parseServerData(localXMLAServerInfo, localNodeList2.item(l));
            ++l;
          }
        }
        ++i;
      }
    }
    catch (IOException localIOException)
    {
      localIOException.printStackTrace();
    }
    catch (NullPointerException localNullPointerException)
    {
      return new XMLAServerInfo[0];
    }
    XMLAServerInfo[] arrayOfXMLAServerInfo = new XMLAServerInfo[localHashSet.size()];
    arrayOfXMLAServerInfo = (XMLAServerInfo[])(XMLAServerInfo[])localHashSet.toArray(arrayOfXMLAServerInfo);
    return arrayOfXMLAServerInfo;
  }

  public boolean isSAP(XMLAServerInfo paramXMLAServerInfo)
  {
    String[] arrayOfString1 = discoverRowsets();
    boolean i = false;
    for (String str : arrayOfString1)
    {
      if (!str.equalsIgnoreCase("SAP_VARIABLES"))
        continue;
      i = true;
      break;
    }
    return i;
  }

  public boolean isSAP()
  {
    if (!this.globalIsSAPSet)
    {
      XMLAServerInfo[] arrayOfXMLAServerInfo = getConnections();
      if ((arrayOfXMLAServerInfo != null) && (arrayOfXMLAServerInfo.length > 0))
      {
        this.globalIsSAP = isSAP(getConnections()[0]);
        this.globalIsSAPSet = true;
      }
    }
    return this.globalIsSAP;
  }

  private String[] discoverRowsets()
  {
    try
    {
      XMLAProperties localXMLAProperties = new XMLAProperties();
      XMLARestrictions localXMLARestrictions = new XMLARestrictions();
      Document localDocument = discoverSchemaRowsets(localXMLARestrictions, localXMLAProperties);
      if (localDocument == null)
        return new String[0];
      NodeList localNodeList1 = localDocument.getElementsByTagName("row");
      if ((localNodeList1 == null) || (localNodeList1.getLength() == 0))
        return new String[0];
      ArrayList localArrayList = new ArrayList();
      int i = 0;
      int j = localNodeList1.getLength();
      while (i < j)
      {
        NodeList localNodeList2 = localNodeList1.item(i).getChildNodes();
        for (int k = 0; k < localNodeList2.getLength(); ++k)
        {
          if (localNodeList2.item(k).getNodeType() != 1)
            continue;
          String str = localNodeList2.item(k).getNodeName();
          if (!str.equals("SchemaName"))
            continue;
          localArrayList.add(getTextFromDOMElement(localNodeList2.item(k)));
        }
        ++i;
      }
      return (String[])localArrayList.toArray(new String[0]);
    }
    catch (IOException localIOException)
    {
    }
    return new String[0];
  }

  public Document discoverDataSources(XMLARestrictions paramXMLARestrictions, XMLAProperties paramXMLAProperties)
    throws IOException
  {
    setRequestType("DISCOVER_DATASOURCES");
    return discover(paramXMLARestrictions, paramXMLAProperties);
  }

  public Document discoverSchemaRowsets(XMLARestrictions paramXMLARestrictions, XMLAProperties paramXMLAProperties)
    throws IOException
  {
    setRequestType("DISCOVER_SCHEMA_ROWSETS");
    return discover(paramXMLARestrictions, paramXMLAProperties);
  }

  public Document getCatalogList(XMLARestrictions paramXMLARestrictions, XMLAProperties paramXMLAProperties)
    throws IOException
  {
    setRequestType("DBSCHEMA_CATALOGS");
    return discover(paramXMLARestrictions, paramXMLAProperties);
  }

  public Document getCubeList(XMLARestrictions paramXMLARestrictions, XMLAProperties paramXMLAProperties)
    throws IOException
  {
    setRequestType("MDSCHEMA_CUBES");
    return discover(paramXMLARestrictions, paramXMLAProperties);
  }

  public Document getSAPVariableList(XMLARestrictions paramXMLARestrictions, XMLAProperties paramXMLAProperties)
    throws IOException
  {
    setRequestType("SAP_VARIABLES");
    return discover(paramXMLARestrictions, paramXMLAProperties);
  }

  public Document getDimensionList(XMLARestrictions paramXMLARestrictions, XMLAProperties paramXMLAProperties)
    throws IOException
  {
    setRequestType("MDSCHEMA_DIMENSIONS");
    return discover(paramXMLARestrictions, paramXMLAProperties);
  }

  public Document getFunctionList(XMLARestrictions paramXMLARestrictions, XMLAProperties paramXMLAProperties)
    throws IOException
  {
    setRequestType("MDSCHEMA_FUNCTIONS");
    return discover(paramXMLARestrictions, paramXMLAProperties);
  }

  public Document getHierarchyList(XMLARestrictions paramXMLARestrictions, XMLAProperties paramXMLAProperties)
    throws IOException
  {
    setRequestType("MDSCHEMA_HIERARCHIES");
    return discover(paramXMLARestrictions, paramXMLAProperties);
  }

  public Document getLevelList(XMLARestrictions paramXMLARestrictions, XMLAProperties paramXMLAProperties)
    throws IOException
  {
    setRequestType("MDSCHEMA_LEVELS");
    return discover(paramXMLARestrictions, paramXMLAProperties);
  }

  public Document getMeasureList(XMLARestrictions paramXMLARestrictions, XMLAProperties paramXMLAProperties)
    throws IOException
  {
    setRequestType("MDSCHEMA_MEASURES");
    return discover(paramXMLARestrictions, paramXMLAProperties);
  }

  public Document getMemberList(XMLARestrictions paramXMLARestrictions, XMLAProperties paramXMLAProperties)
    throws IOException
  {
    setRequestType("MDSCHEMA_MEMBERS");
    return discover(paramXMLARestrictions, paramXMLAProperties);
  }

  public Document getPropertyList(XMLARestrictions paramXMLARestrictions, XMLAProperties paramXMLAProperties)
    throws IOException
  {
    setRequestType("MDSCHEMA_PROPERTIES");
    return discover(paramXMLARestrictions, paramXMLAProperties);
  }

  private void writeToStream(byte[] paramArrayOfByte, OutputStream paramOutputStream)
    throws IOException
  {
    BufferedOutputStream localBufferedOutputStream = null;
    try
    {
      localBufferedOutputStream = new BufferedOutputStream(paramOutputStream);
      localBufferedOutputStream.write(paramArrayOfByte);
      localBufferedOutputStream.flush();
    }
    finally
    {
      try
      {
        if (localBufferedOutputStream != null)
          localBufferedOutputStream.close();
        if (paramOutputStream != null)
          paramOutputStream.close();
      }
      catch (IOException localIOException2)
      {
      }
    }
  }

  public static boolean isVerbose()
  {
    return verbose;
  }

  public static boolean isDebug()
  {
    return debug;
  }

  public XMLAServerInfo[] getConnections()
  {
    return this.connections;
  }

  public static void setVerbose(boolean paramBoolean)
  {
    verbose = paramBoolean;
  }

  public static void setDebug(boolean paramBoolean)
  {
    debug = paramBoolean;
  }

  private String getTimeString()
  {
    int i = GregorianCalendar.getInstance().get(11);
    int j = GregorianCalendar.getInstance().get(12);
    int k = GregorianCalendar.getInstance().get(13);
    int l = GregorianCalendar.getInstance().get(14);
    String str = "";
    if (l < 10)
      str = "00" + l;
    else if (l < 100)
      str = "0" + l;
    else
      str = "" + l;
    return i + ":" + ((j < 10) ? "0" + j : Integer.valueOf(j)) + ":" + ((k < 10) ? "0" + k : Integer.valueOf(k)) + "." + str;
  }

  private String formatPropsAndRests(XMLARestrictions paramXMLARestrictions, XMLAProperties paramXMLAProperties)
  {
    String str1 = paramXMLAProperties.getCatalog();
    String str2 = paramXMLAProperties.getDataSourceInfo();
    StringBuffer localStringBuffer1 = new StringBuffer();
    if ((str2 != null) && (str2.trim().length() != 0))
      localStringBuffer1.append(str2.trim());
    if ((str1 != null) && (str1.trim().length() != 0))
    {
      if (localStringBuffer1.length() != 0)
        localStringBuffer1.append(", ");
      localStringBuffer1.append(str1.trim());
    }
    StringBuffer localStringBuffer2 = new StringBuffer();
    if ((paramXMLARestrictions.getCatalog() != null) && (paramXMLARestrictions.getCatalog().trim().length() != 0))
      localStringBuffer2.append(paramXMLARestrictions.getCatalog().trim() + ", ");
    if ((paramXMLARestrictions.getCubeName() != null) && (paramXMLARestrictions.getCubeName().trim().length() != 0))
      localStringBuffer2.append(paramXMLARestrictions.getCubeName().trim() + ", ");
    if ((paramXMLARestrictions.getDimensionUniqueName() != null) && (paramXMLARestrictions.getDimensionUniqueName().trim().length() != 0))
      localStringBuffer2.append(paramXMLARestrictions.getDimensionUniqueName().trim() + ", ");
    if ((paramXMLARestrictions.getHierarchyUniqueName() != null) && (paramXMLARestrictions.getHierarchyUniqueName().trim().length() != 0))
      localStringBuffer2.append(paramXMLARestrictions.getHierarchyUniqueName().trim() + ", ");
    if ((paramXMLARestrictions.getMemberUniqueName() != null) && (paramXMLARestrictions.getMemberUniqueName().trim().length() != 0))
      localStringBuffer2.append(paramXMLARestrictions.getMemberUniqueName().trim());
    String str3 = localStringBuffer2.toString().trim();
    if (str3.endsWith(","))
      str3 = str3.substring(0, str3.length() - 1).trim();
    return localStringBuffer1.toString() + " [" + str3 + "]";
  }

  public static void printStackTrace(StackTraceElement[] paramArrayOfStackTraceElement, PrintStream paramPrintStream)
  {
  }

  public static String getErrorString(NodeList paramNodeList)
  {
    String str1 = "<Unknown fault code>";
    String str2 = "<No fault string>";
    String str3 = "<No fault actor>";
    String str4 = "<No description>";
    paramNodeList = paramNodeList.item(0).getChildNodes();
    int i = paramNodeList.getLength();
    for (int j = 0; j < i; ++j)
    {
      Node localNode1 = paramNodeList.item(j);
      if (localNode1.getNodeName().equals("faultcode"))
      {
        str1 = getTextFromDOMElement(localNode1);
      }
      else if (localNode1.getNodeName().equals("faultstring"))
      {
        str2 = getTextFromDOMElement(localNode1);
      }
      else if (localNode1.getNodeName().equals("faultactor"))
      {
        str3 = getTextFromDOMElement(localNode1);
      }
      else
      {
        if (!localNode1.getNodeName().equals("detail"))
          continue;
        NodeList localNodeList = localNode1.getChildNodes();
        if ((localNodeList == null) || (localNodeList.getLength() <= 0))
          continue;
        int k = 0;
        int l = localNodeList.getLength();
        while (k < l)
        {
          Node localNode2 = localNodeList.item(k);
          Object localObject;
          if (localNode2.getNodeName().equals("Error"))
          {
            localObject = localNode2.getAttributes();
            Node localNode3 = ((NamedNodeMap)localObject).getNamedItem("Description");
            str4 = "";
            if (localNode3 != null)
              str4 = str4 + "Description:  " + localNode3.getNodeValue() + " \n";
            localNode3 = ((NamedNodeMap)localObject).getNamedItem("ErrorCode");
            if (localNode3 != null)
              str4 = str4 + "ErrorCode:    " + localNode3.getNodeValue() + " \n";
            localNode3 = ((NamedNodeMap)localObject).getNamedItem("HelpFile");
            if (localNode3 != null)
              str4 = str4 + "HelpFile:     " + localNode3.getNodeValue() + " \n";
            localNode3 = ((NamedNodeMap)localObject).getNamedItem("Source");
            if (localNode3 != null)
              str4 = str4 + "Source:       " + localNode3.getNodeValue() + " \n";
          }
          else if (localNode2.getNodeName().startsWith("XA:error"))
          {
            localObject = localNode2.getChildNodes();
            int i1 = 0;
            int i2 = ((NodeList)localObject).getLength();
            while (i1 < i2)
            {
              Node localNode4 = ((NodeList)localObject).item(i1);
              if (localNode4.getNodeName().equals("desc"))
                str4 = getTextFromDOMElement(localNode4);
              ++i1;
            }
          }
          ++k;
        }
      }
    }
    StringBuffer localStringBuffer = new StringBuffer("Error!   \n");
    localStringBuffer.append("Fault code:   " + str1 + " \n");
    localStringBuffer.append("Fault string: " + str2 + " \n");
    localStringBuffer.append("Fault actor:  " + str3 + " \n");
    localStringBuffer.append(str4);
    return (String)localStringBuffer.toString();
  }
}