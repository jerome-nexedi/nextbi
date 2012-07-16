package com.tensegrity.palo.xmla;

import com.tensegrity.palojava.DbConnection;
import com.tensegrity.palojava.PaloInfo;
import com.tensegrity.palojava.PropertyInfo;

public abstract interface XMLAPaloInfo extends PaloInfo
{
  public abstract PropertyInfo getProperty(DbConnection paramDbConnection, String paramString);

  public abstract String[] getAllKnownPropertyIds(DbConnection paramDbConnection);
}

/* Location:           D:\server\apache-tomcat-5.5.20\webapps\Palo-Pivot\WEB-INF\lib\paloxmla.jar
 * Qualified Name:     com.tensegrity.palo.xmla.XMLAPaloInfo
 * JD-Core Version:    0.5.4
 */