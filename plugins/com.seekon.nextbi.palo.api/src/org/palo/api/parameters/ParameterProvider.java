package org.palo.api.parameters;

public abstract interface ParameterProvider {
  public abstract ParameterReceiver getSourceObject();

  public abstract void setSourceObject(ParameterReceiver paramParameterReceiver);

  public abstract String[] getParameterNames();

  public abstract void setParameterNames(String[] paramArrayOfString);

  public abstract Object[] getPossibleValuesFor(String paramString);

  public abstract void setPossibleValuesFor(String paramString,
    Object[] paramArrayOfObject);
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name: org.palo.api.parameters.ParameterProvider JD-Core Version:
 * 0.5.4
 */