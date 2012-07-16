package org.palo.api;

public abstract interface ServerInfo
{
  public static final String SECURITY_INFO_PROPERTY = "SecurityInfoProperty";
  public static final String BUILD_NUMBER_PROPERTY = "BuildNumberProperty";
  public static final String MINOR_VERSION_PROPERTY = "MinorVersionProperty";
  public static final String MAJOR_VERSION_PROPERTY = "MajorVersionProperty";
  public static final String DESCRIPTION_PROPERTY = "DescriptionProperty";

  public abstract String getName();

  public abstract String getVersion();

  public abstract String getType();

  public abstract String[] getPropertyIds();

  public abstract String getProperty(String paramString);
}

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name:     org.palo.api.ServerInfo
 * JD-Core Version:    0.5.4
 */