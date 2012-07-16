package com.tensegrity.palo.xmla;

import com.tensegrity.palojava.CellInfo;

public class ExtendedCellInfo {
  public CellInfo[] cellInfo;

  public String[][] coordinates;

  public ExtendedCellInfo(CellInfo[] paramArrayOfCellInfo,
    String[][] paramArrayOfString) {
    this.cellInfo = paramArrayOfCellInfo;
    this.coordinates = paramArrayOfString;
  }
}

/*
 * Location:
 * D:\server\apache-tomcat-5.5.20\webapps\Palo-Pivot\WEB-INF\lib\paloxmla.jar
 * Qualified Name: com.tensegrity.palo.xmla.ExtendedCellInfo JD-Core Version:
 * 0.5.4
 */