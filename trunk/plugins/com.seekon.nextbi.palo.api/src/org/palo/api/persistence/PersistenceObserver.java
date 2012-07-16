package org.palo.api.persistence;

public abstract interface PersistenceObserver
{
  public abstract void loadFailed(String paramString, PersistenceError[] paramArrayOfPersistenceError);

  public abstract void loadIncomplete(Object paramObject, PersistenceError[] paramArrayOfPersistenceError);

  public abstract void loadComplete(Object paramObject);

  public abstract void saveFailed(Object paramObject, PersistenceError[] paramArrayOfPersistenceError);

  public abstract void saveIncomplete(Object paramObject, PersistenceError[] paramArrayOfPersistenceError);

  public abstract void saveComplete(Object paramObject);
}

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name:     org.palo.api.persistence.PersistenceObserver
 * JD-Core Version:    0.5.4
 */