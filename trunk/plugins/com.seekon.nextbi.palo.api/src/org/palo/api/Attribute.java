package org.palo.api;

public abstract interface Attribute
{
  public static final int TYPE_STRING = 1;
  public static final int TYPE_NUMERIC = 0;

  public abstract String getId();

  public abstract String getName();

  public abstract void setName(String paramString);

  public abstract Object getValue(Element paramElement);

  public abstract void setValue(Element paramElement, Object paramObject);

  public abstract void setValues(Element[] paramArrayOfElement, Object[] paramArrayOfObject);

  public abstract Object[] getValues(Element[] paramArrayOfElement);

  public abstract boolean hasChildren();

  public abstract void setChildren(Attribute[] paramArrayOfAttribute);

  public abstract void removeChildren(Attribute[] paramArrayOfAttribute);

  public abstract Attribute[] getChildren();

  public abstract Attribute[] getParents();

  public abstract int getType();

  public abstract void setType(int paramInt);
}

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name:     org.palo.api.Attribute
 * JD-Core Version:    0.5.4
 */