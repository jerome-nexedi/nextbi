package org.palo.api.persistence;

public class PersistenceObserverAdapter
  implements PersistenceObserver
{
  public void loadComplete(Object source)
  {
  }

  public void loadFailed(String sourceId, PersistenceError[] errors)
  {
  }

  public void loadIncomplete(Object source, PersistenceError[] errors)
  {
  }

  public void saveComplete(Object source)
  {
  }

  public void saveFailed(Object source, PersistenceError[] errors)
  {
  }

  public void saveIncomplete(Object source, PersistenceError[] errors)
  {
  }
}

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name:     org.palo.api.persistence.PersistenceObserverAdapter
 * JD-Core Version:    0.5.4
 */