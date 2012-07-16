package com.tensegrity.palo.xmla;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.StringTokenizer;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;

public class XMLDocumentWriter
{
  PrintWriter out;

  public XMLDocumentWriter(PrintWriter paramPrintWriter)
  {
    this.out = paramPrintWriter;
  }

  public void close()
  {
    if (this.out == null)
      return;
    this.out.close();
  }

  public String write(Node paramNode)
  {
    return write(paramNode, "");
  }

  public String write(Node paramNode, String paramString)
  {
    StringBuffer localStringBuffer = new StringBuffer();
    Object localObject1;
    Object localObject2;
    switch (paramNode.getNodeType())
    {
    case 9:
      localObject1 = (Document)paramNode;
      if (this.out != null)
        this.out.println(paramString + "<?xml version='1.0'?>");
      localStringBuffer.append(paramString + "<?xml version='1.0'?>\n");
      for (localObject2 = ((Document)localObject1).getFirstChild(); ; localObject2 = ((Node)localObject2).getNextSibling())
      {
        if (localObject2 == null)
        	return (String)(String)removeEmptyLines(localStringBuffer.toString());
        
        localStringBuffer.append(write((Node)localObject2, paramString) + "\n");
      }
    case 10:
      localObject1 = (DocumentType)paramNode;
      if (this.out != null)
        this.out.println("<!DOCTYPE " + ((DocumentType)localObject1).getName() + ">");
      localStringBuffer.append("<!DOCTYPE " + ((DocumentType)localObject1).getName() + ">\n");
      break;
    case 1:
      localObject1 = (Element)paramNode;
      if (this.out != null)
        this.out.print(paramString + "<" + ((Element)localObject1).getTagName());
      localStringBuffer.append(paramString + "<" + ((Element)localObject1).getTagName());
      localObject2 = ((Element)localObject1).getAttributes();
      for (int i = 0; i < ((NamedNodeMap)localObject2).getLength(); ++i)
      {
      	Node localNode = ((NamedNodeMap)localObject2).item(i);
        if (this.out != null)
          this.out.print(" " + localNode.getNodeName() + "='" + fixup(localNode.getNodeValue()) + "'");
        localStringBuffer.append(" " + localNode.getNodeName() + "='" + fixup(localNode.getNodeValue()) + "'");
      }
      if (this.out != null)
        this.out.println(">");
      localStringBuffer.append(">\n");
      String str = paramString + "    ";
      for (Node localNode = ((Element)localObject1).getFirstChild(); localNode != null; localNode = localNode.getNextSibling())
        localStringBuffer.append(write(localNode, str) + "\n");
      if (this.out != null)
        this.out.println(paramString + "</" + ((Element)localObject1).getTagName() + ">");
      localStringBuffer.append(paramString + "</" + ((Element)localObject1).getTagName() + ">\n");
      break;
    case 3:
      localObject1 = (Text)paramNode;
      localObject2 = ((Text)localObject1).getData().trim();
      if ((localObject2 == null) || (((String)localObject2).length() <= 0))
      	return (String)(String)removeEmptyLines(localStringBuffer.toString());
      if (this.out != null)
        this.out.println(paramString + fixup((String)localObject2));
      localStringBuffer.append(paramString + fixup((String)localObject2) + "\n");
      break;
    case 7:
      localObject1 = (ProcessingInstruction)paramNode;
      if (this.out != null)
        this.out.println(paramString + "<?" + ((ProcessingInstruction)localObject1).getTarget() + " " + ((ProcessingInstruction)localObject1).getData() + "?>");
      localStringBuffer.append(paramString + "<?" + ((ProcessingInstruction)localObject1).getTarget() + " " + ((ProcessingInstruction)localObject1).getData() + "?>\n");
      break;
    case 5:
      if (this.out != null)
        this.out.println(paramString + "&" + paramNode.getNodeName() + ";");
      localStringBuffer.append(paramString + "&" + paramNode.getNodeName() + ";\n");
      break;
    case 4:
      localObject1 = (CDATASection)paramNode;
      if (this.out != null)
        this.out.println(paramString + "<" + "![CDATA[" + ((CDATASection)localObject1).getData() + "]]" + ">");
      localStringBuffer.append(paramString + "<![CDATA[" + ((CDATASection)localObject1).getData() + "]]>\n");
      break;
    case 8:
      localObject1 = (Comment)paramNode;
      if (this.out != null)
        this.out.println(paramString + "<!--" + ((Comment)localObject1).getData() + "-->");
      localStringBuffer.append(paramString + "<!--" + ((Comment)localObject1).getData() + "-->\n");
      break;
    case 2:
    case 6:
    default:
      System.err.println("Ignoring node: " + paramNode.getClass().getName());
    }
    return (String)(String)removeEmptyLines(localStringBuffer.toString());
  }

  private String removeEmptyLines(String paramString)
  {
    StringTokenizer localStringTokenizer = new StringTokenizer(paramString, "\n");
    StringBuffer localStringBuffer = new StringBuffer();
    while (localStringTokenizer.hasMoreTokens())
    {
      String str = localStringTokenizer.nextToken();
      if (str.trim().length() != 0)
        localStringBuffer.append(str + "\n");
    }
    return localStringBuffer.toString();
  }

  String fixup(String paramString)
  {
    StringBuffer localStringBuffer = new StringBuffer();
    int i = paramString.length();
    for (int j = 0; j < i; ++j)
    {
      char c = paramString.charAt(j);
      switch (c)
      {
      case '<':
        localStringBuffer.append("&lt;");
        break;
      case '>':
        localStringBuffer.append("&gt;");
        break;
      case '&':
        localStringBuffer.append("&amp;");
        break;
      case '"':
        localStringBuffer.append("&quot;");
        break;
      case '\'':
        localStringBuffer.append("&apos;");
        break;
      default:
        localStringBuffer.append(c);
      }
    }
    return localStringBuffer.toString();
  }
}

/* Location:           D:\server\apache-tomcat-5.5.20\webapps\Palo-Pivot\WEB-INF\lib\paloxmla.jar
 * Qualified Name:     com.tensegrity.palo.xmla.XMLDocumentWriter
 * JD-Core Version:    0.5.4
 */