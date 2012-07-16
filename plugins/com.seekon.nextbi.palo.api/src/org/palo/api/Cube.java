package org.palo.api;

import com.tensegrity.palojava.CubeInfo;
import java.math.BigInteger;
import java.text.NumberFormat;
import org.palo.api.exceptions.PaloIOException;
import org.palo.api.persistence.PersistenceObserver;

public abstract interface Cube extends PaloObject
{
  public static final int SPLASHMODE_DISABLED = 0;
  public static final int SPLASHMODE_DEFAULT = 1;
  public static final int SPLASHMODE_BASE_ADD = 2;
  public static final int SPLASHMODE_BASE_SET = 3;
  public static final int SPLASHMODE_UNKNOWN = 4;
  public static final int CUBEEXTENDEDTYPE_REGULAR = 0;
  public static final int CUBEEXTENDEDTYPE_VIRTUAL = 1;

  public abstract int getExtendedType();

  public abstract String getName();

  public abstract Database getDatabase();

  public abstract int getDimensionCount();

  public abstract Dimension getDimensionAt(int paramInt);

  public abstract Dimension[] getDimensions();

  public abstract Dimension getDimensionByName(String paramString);

  public abstract Dimension getDimensionById(String paramString);

  public abstract void commitLog();

  public abstract Object getData(String[] paramArrayOfString);

  public abstract Object[] getDataArray(String[][] paramArrayOfString);

  public abstract Object getData(Element[] paramArrayOfElement);

  public abstract Object[] getDataArray(Element[][] paramArrayOfElement);

  public abstract Object[] getDataBulk(Element[][] paramArrayOfElement);

  public abstract void setData(String[] paramArrayOfString, Object paramObject);

  public abstract void setData(Element[] paramArrayOfElement, Object paramObject);

  public abstract void setDataSplashed(Element[] paramArrayOfElement, Object paramObject);

  /** @deprecated */
  public abstract void setDataSplashed(Element[] paramArrayOfElement, Object paramObject, NumberFormat paramNumberFormat);

  /** @deprecated */
  public abstract void setData(Element[] paramArrayOfElement, Object paramObject, NumberFormat paramNumberFormat);

  public abstract void setDataSplashed(String[] paramArrayOfString, Object paramObject, int paramInt);

  public abstract void setDataSplashed(Element[] paramArrayOfElement, Object paramObject, int paramInt);

  public abstract void setDataArray(Element[][] paramArrayOfElement, Object[] paramArrayOfObject, int paramInt);

  public abstract boolean isAttributeCube();

  public abstract boolean isSubsetCube();

  public abstract boolean isViewCube();

  /** @deprecated */
  public abstract CubeView addCubeView(String paramString1, String paramString2, Property[] paramArrayOfProperty);

  public abstract CubeView addCubeView(String paramString, Property[] paramArrayOfProperty);

  public abstract void removeCubeView(CubeView paramCubeView);

  public abstract CubeView getCubeView(String paramString)
    throws PaloIOException;

  public abstract String[] getCubeViewIds();

  public abstract String getCubeViewName(String paramString);

  public abstract int getCubeViewCount();

  /** @deprecated */
  public abstract CubeView[] getCubeViews();

  public abstract void getCubeViews(PersistenceObserver paramPersistenceObserver);

  public abstract ExportContext getExportContext();

  public abstract ExportContext getExportContext(Element[][] paramArrayOfElement);

  public abstract ExportDataset getDataExport(ExportContext paramExportContext);

  public abstract void addDataArray(Element[][] paramArrayOfElement, Object[] paramArrayOfObject, int paramInt);

  public abstract void setDataArray(Element[][] paramArrayOfElement, Object[] paramArrayOfObject, boolean paramBoolean1, int paramInt, boolean paramBoolean2);

  public abstract Rule[] getRules();

  public abstract Rule addRule(String paramString);

  public abstract Rule addRule(String paramString1, String paramString2, boolean paramBoolean, String paramString3);

  public abstract Rule addRule(String paramString1, String paramString2, boolean paramBoolean1, String paramString3, boolean paramBoolean2);

  public abstract boolean removeRule(Rule paramRule);

  public abstract boolean removeRule(String paramString);

  public abstract Rule getRule(Element[] paramArrayOfElement);

  public abstract boolean isSystemCube();

  public abstract boolean isUserInfoCube();

  public abstract int getType();

  /** @deprecated */
  public abstract void registerViewObserver(PersistenceObserver paramPersistenceObserver);

  /** @deprecated */
  public abstract void unregisterViewObserver(PersistenceObserver paramPersistenceObserver);

  public abstract void rename(String paramString);

  public abstract void clear();

  public abstract void clear(Element[][] paramArrayOfElement);

  public abstract void convert(int paramInt);

  public abstract String[] getAllPropertyIds();

  public abstract Property2 getProperty(String paramString);

  public abstract void addProperty(Property2 paramProperty2);

  public abstract void removeProperty(String paramString);

  public abstract Cell getCell(Element[] paramArrayOfElement);

  public abstract Cell[] getCells(Element[][] paramArrayOfElement);

  public abstract Cell[] getCells(Element[][] paramArrayOfElement, boolean paramBoolean);

  public abstract Cell[] getCellArea(Element[][] paramArrayOfElement);

  public abstract CubeInfo getInfo();

  public abstract BigInteger getCellCount();

  public abstract BigInteger getFilledCellCount();
}

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name:     org.palo.api.Cube
 * JD-Core Version:    0.5.4
 */