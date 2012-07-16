package org.palo.api;

/** @deprecated */
public abstract interface Rights
{
  public abstract boolean maySplash(PaloObject paramPaloObject);

  public abstract boolean mayDelete(PaloObject paramPaloObject);

  public abstract boolean mayWrite(PaloObject paramPaloObject);

  public abstract boolean mayRead(PaloObject paramPaloObject);

  public abstract boolean maySplash(Class<? extends PaloObject> paramClass);

  public abstract boolean mayDelete(Class<? extends PaloObject> paramClass);

  public abstract boolean mayWrite(Class<? extends PaloObject> paramClass);

  public abstract boolean mayRead(Class<? extends PaloObject> paramClass);

  public abstract void allowSplash(String paramString, PaloObject paramPaloObject);

  public abstract void allowDelete(String paramString, PaloObject paramPaloObject);

  public abstract void allowWrite(String paramString, PaloObject paramPaloObject);

  public abstract void allowRead(String paramString, PaloObject paramPaloObject);

  public abstract void preventAccess(String paramString, PaloObject paramPaloObject);

  public abstract void allowSplash(String paramString, Class<? extends PaloObject> paramClass);

  public abstract void allowDelete(String paramString, Class<? extends PaloObject> paramClass);

  public abstract void allowWrite(String paramString, Class<? extends PaloObject> paramClass);

  public abstract void allowRead(String paramString, Class<? extends PaloObject> paramClass);

  public abstract void preventAccess(String paramString, Class<? extends PaloObject> paramClass);
}

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name:     org.palo.api.Rights
 * JD-Core Version:    0.5.4
 */