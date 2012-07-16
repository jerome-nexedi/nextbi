package org.palo.api;

import com.tensegrity.palojava.RuleInfo;

public abstract interface Rule
{
  public abstract String getId();

  public abstract Cube getCube();

  public abstract void setDefinition(String paramString);

  public abstract String getDefinition();

  public abstract long getTimestamp();

  public abstract void setComment(String paramString);

  public abstract String getComment();

  public abstract void setExternalIdentifier(String paramString);

  public abstract void setExternalIdentifier(String paramString, boolean paramBoolean);

  public abstract String getExternalIdentifier();

  public abstract void useExternalIdentifier(boolean paramBoolean);

  public abstract void update(String paramString1, String paramString2, boolean paramBoolean, String paramString3);

  public abstract void update(String paramString1, String paramString2, boolean paramBoolean1, String paramString3, boolean paramBoolean2);

  public abstract boolean isActive();

  public abstract void setActive(boolean paramBoolean);

  public abstract RuleInfo getInfo();
}

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name:     org.palo.api.Rule
 * JD-Core Version:    0.5.4
 */