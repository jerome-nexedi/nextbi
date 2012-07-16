package com.tensegrity.palo.xmla.loader;

import com.tensegrity.palojava.DbConnection;
import com.tensegrity.palojava.DimensionInfo;
import com.tensegrity.palojava.HierarchyInfo;
import com.tensegrity.palojava.loader.HierarchyLoader;
import java.util.Collection;

public class XMLAHierarchyLoader extends HierarchyLoader
{
  private boolean hiersLoaded = false;

  public XMLAHierarchyLoader(DbConnection paramDbConnection, DimensionInfo paramDimensionInfo)
  {
    super(paramDbConnection, paramDimensionInfo);
  }

  public String[] getAllHierarchyIds()
  {
    if (!this.hiersLoaded)
      reload();
    return getLoadedIds();
  }

  public int getHierarchyCount()
  {
    if (!this.hiersLoaded)
      reload();
    return getLoaded().size();
  }

  protected final void reload()
  {
    reset();
    HierarchyInfo[] arrayOfHierarchyInfo1 = this.paloConnection.getHierarchies(this.dimension);
    for (HierarchyInfo localHierarchyInfo : arrayOfHierarchyInfo1)
      loaded(localHierarchyInfo);
    this.hiersLoaded = true;
  }
}

/* Location:           D:\server\apache-tomcat-5.5.20\webapps\Palo-Pivot\WEB-INF\lib\paloxmla.jar
 * Qualified Name:     com.tensegrity.palo.xmla.loader.XMLAHierarchyLoader
 * JD-Core Version:    0.5.4
 */