package org.palo.api.parameters;

public abstract interface ParameterReceiver
{
  public abstract String[] getParameterNames();

  public abstract void setParameterNames(String[] paramArrayOfString);

  public abstract Object getParameterValue(String paramString);

  public abstract Object getDefaultValue(String paramString);

  public abstract void setParameter(String paramString, Object paramObject);

  public abstract void addParameterValue(String paramString, Object paramObject);

  public abstract boolean isParameterized();
}

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name:     org.palo.api.parameters.ParameterReceiver
 * JD-Core Version:    0.5.4
 */