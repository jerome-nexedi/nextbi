package org.palo.api;

import com.tensegrity.palojava.DatabaseInfo;
import org.palo.api.subsets.SubsetStorageHandler;

public abstract interface Database extends PaloObject
{
  public abstract String getName();

  public abstract Connection getConnection();

  public abstract void reload();

  public abstract void startBatchUpdate();

  public abstract void endBatchUpdate();

  public abstract int getDimensionCount();

  public abstract Dimension getDimensionAt(int paramInt);

  public abstract Dimension[] getDimensions();

  public abstract Dimension[] getDimensions(int paramInt);

  public abstract Dimension getDimensionByName(String paramString);

  public abstract Dimension getDimensionById(String paramString);

  public abstract int getCubeCount();

  public abstract Cube getCubeAt(int paramInt);

  public abstract Cube[] getCubes();

  public abstract Cube[] getCubes(int paramInt);

  public abstract Cube getCubeByName(String paramString);

  public abstract Cube getCubeById(String paramString);

  public abstract Dimension addDimension(String paramString);

  public abstract Dimension addUserInfoDimension(String paramString);

  public abstract void removeDimension(Dimension paramDimension);

  public abstract Cube addCube(String paramString, Dimension[] paramArrayOfDimension);

  public abstract Cube addUserInfoCube(String paramString, Dimension[] paramArrayOfDimension);

  public abstract void removeCube(Cube paramCube);

  public abstract boolean save();

  public abstract Cube addCube(VirtualCubeDefinition paramVirtualCubeDefinition);

  public abstract String parseRule(Cube paramCube, String paramString1, String paramString2);

  public abstract boolean isSystem();

  public abstract SubsetStorageHandler getSubsetStorageHandler();

  public abstract boolean supportsNewSubsets();

  /** @deprecated */
  public abstract Rights getRights();

  public abstract DatabaseInfo getInfo();

  public abstract void rename(String paramString);
}

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name:     org.palo.api.Database
 * JD-Core Version:    0.5.4
 */