package com.tensegrity.palo.xmla.loader;

import com.tensegrity.palo.xmla.XMLAConnection;
import com.tensegrity.palo.xmla.XMLAPaloInfo;
import com.tensegrity.palojava.DbConnection;
import com.tensegrity.palojava.PaloInfo;
import com.tensegrity.palojava.PropertyInfo;
import com.tensegrity.palojava.loader.PropertyLoader;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class XMLAPropertyLoader extends PropertyLoader {
  private final PaloInfo paloObject;

  public XMLAPropertyLoader(DbConnection paramDbConnection) {
    super(paramDbConnection);
    this.paloObject = null;
  }

  public XMLAPropertyLoader(DbConnection paramDbConnection, PaloInfo paramPaloInfo) {
    super(paramDbConnection);
    this.paloObject = paramPaloInfo;
  }

  public String[] getAllPropertyIds() {
    HashSet localHashSet = new HashSet();
    String[] arrayOfString1 = new String[0];
    if (this.paloObject == null)
      arrayOfString1 = ((XMLAConnection) this.paloConnection)
        .getAllKnownPropertyIds();
    else if (this.paloObject instanceof XMLAPaloInfo)
      arrayOfString1 = ((XMLAPaloInfo) this.paloObject)
        .getAllKnownPropertyIds(this.paloConnection);
    localHashSet.addAll(this.loadedInfo.keySet());
    for (String str : arrayOfString1)
      localHashSet.add(str);
    return (String[]) localHashSet.toArray(new String[0]);
  }

  public PropertyInfo load(String paramString) {
    Object localObject = (PaloInfo) this.loadedInfo.get(paramString);
    if (localObject == null) {
      if (this.paloObject == null)
        localObject = this.paloConnection.getProperty(paramString);
      else if (this.paloObject instanceof XMLAPaloInfo)
        localObject = ((XMLAPaloInfo) this.paloObject).getProperty(
          this.paloConnection, paramString);
      loaded((PaloInfo) localObject);
    }
    return (PropertyInfo) (PropertyInfo) localObject;
  }

  protected void reload() {
  }
}

/*
 * Location:
 * D:\server\apache-tomcat-5.5.20\webapps\Palo-Pivot\WEB-INF\lib\paloxmla.jar
 * Qualified Name: com.tensegrity.palo.xmla.loader.XMLAPropertyLoader JD-Core
 * Version: 0.5.4
 */