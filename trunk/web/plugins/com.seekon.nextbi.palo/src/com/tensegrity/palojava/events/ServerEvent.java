package com.tensegrity.palojava.events;

public abstract interface ServerEvent {
  public static final int SERVER_CHANGED = 0;

  public static final int DATABASE_CHANGED = 1;

  public static final int DIMENSION_CHANGED = 2;

  public static final int CUBE_CHANGED = 4;

  public static final int SERVER_DOWN = 8;

  public abstract int getType();
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.paloapi\lib\palo.jar
 * Qualified Name: com.tensegrity.palojava.events.ServerEvent JD-Core Version:
 * 0.5.4
 */