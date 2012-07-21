package org.palo.api.impl;

import java.util.Map;
import org.palo.api.Database;
import org.palo.api.persistence.PaloPersistenceException;

public abstract class AbstractController {
  protected abstract Object create(Class paramClass, Object[] paramArrayOfObject);

  protected abstract boolean delete(Object paramObject);

  protected abstract Object load(Database paramDatabase, String paramString)
    throws PaloPersistenceException;

  protected abstract void load(Database paramDatabase, Map paramMap1, Map paramMap2)
    throws PaloPersistenceException;

  protected abstract void init(Database paramDatabase);
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name: org.palo.api.impl.AbstractController JD-Core Version: 0.5.4
 */