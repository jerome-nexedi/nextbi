package com.tensegrity.palo.xmla.builders;

public class BuilderRegistry
{
  private static BuilderRegistry instance = new BuilderRegistry();
  private final DatabaseInfoBuilder databaseInfoBuilder = new DatabaseInfoBuilder();
  private final CubeInfoBuilder cubeInfoBuilder = new CubeInfoBuilder();
  private final DimensionInfoBuilder dimensionInfoBuilder = new DimensionInfoBuilder();
  private final ElementInfoBuilder elementInfoBuilder = new ElementInfoBuilder();
  private final RuleInfoBuilder ruleInfoBuilder = new RuleInfoBuilder();
  private final VariableInfoBuilder variableInfoBuilder = new VariableInfoBuilder();

  public static BuilderRegistry getInstance()
  {
    return instance;
  }

  public DatabaseInfoBuilder getDatabaseInfoBuilder()
  {
    return this.databaseInfoBuilder;
  }

  public CubeInfoBuilder getCubeInfoBuilder()
  {
    return this.cubeInfoBuilder;
  }

  public DimensionInfoBuilder getDimensionInfoBuilder()
  {
    return this.dimensionInfoBuilder;
  }

  public ElementInfoBuilder getElementInfoBuilder()
  {
    return this.elementInfoBuilder;
  }

  public RuleInfoBuilder getRuleInfoBuilder()
  {
    return this.ruleInfoBuilder;
  }

  public VariableInfoBuilder getVariableInfoBuilder()
  {
    return this.variableInfoBuilder;
  }
}

/* Location:           D:\server\apache-tomcat-5.5.20\webapps\Palo-Pivot\WEB-INF\lib\paloxmla.jar
 * Qualified Name:     com.tensegrity.palo.xmla.builders.BuilderRegistry
 * JD-Core Version:    0.5.4
 */