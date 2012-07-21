/*     */package com.tensegrity.palojava.http.builders;

/*     */
/*     */import com.tensegrity.palojava.CellInfo; /*     */
import com.tensegrity.palojava.PaloException; /*     */
import com.tensegrity.palojava.PaloInfo; /*     */
import com.tensegrity.palojava.impl.CellInfoImpl;

/*     */
/*     */public class CellInfoBuilder
/*     */{
  /*     */public CellInfo create(PaloInfo parent, String[] response)
  /*     */{
    /* 57 */if (response.length < 3)
    /*     */{
      /* 61 */throw new PaloException(
      /* 62 */"Not enough information to create CellInfo!!");
      /*     */}
    /*     */
    /* 65 */int type = Integer.parseInt(response[0]);
    /* 66 */boolean exists = Boolean.getBoolean(response[1]);
    /* 67 */Object value = response[2];
    /* 68 */if ((type == 1) && (!value.equals("")))
      /* 69 */value = new Double(response[2]);
    /* 70 */CellInfoImpl cell = new CellInfoImpl(type, exists, value);
    /* 71 */setCoordinate(cell, response);
    /* 72 */setRuleId(cell, response);
    /* 73 */return cell;
    /*     */}

  /*     */
  /*     */private final void setCoordinate(CellInfoImpl cell, String[] response) {
    /* 77 */if (response.length <= 3)
      /*     */return;
    /* 79 */String[] pathIds = BuilderUtils.getIDs(response[3]);
    /*     */
    /* 81 */if (pathIds.length > 1)
      /* 82 */cell.setCoordinate(pathIds);
    /*     */}

  /*     */
  /*     */private final void setRuleId(CellInfoImpl cell, String[] response)
  /*     */{
    /* 87 */if (response.length <= 3)
      /*     */return;
    /* 89 */String ruleId = getRuleId(response[3]);
    /* 90 */if ((ruleId == null) && (response.length > 4))
      /* 91 */ruleId = getRuleId(response[4]);
    /* 92 */if (ruleId != null)
      /* 93 */cell.setRule(ruleId);
    /*     */}

  /*     */
  /*     */private final String getRuleId(String response) {
    /* 97 */String[] id = BuilderUtils.getIDs(response);
    /* 98 */if (id.length == 1)
      /* 99 */return id[0];
    /* 100 */return null;
    /*     */}
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.paloapi\lib\palo.jar
 * Qualified Name: com.tensegrity.palojava.http.builders.CellInfoBuilder JD-Core
 * Version: 0.5.4
 */