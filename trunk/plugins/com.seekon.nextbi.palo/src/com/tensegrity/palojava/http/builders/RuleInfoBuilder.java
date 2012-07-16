/*    */package com.tensegrity.palojava.http.builders;

/*    */
/*    */import com.tensegrity.palojava.CubeInfo; /*    */
import com.tensegrity.palojava.PaloException; /*    */
import com.tensegrity.palojava.PaloInfo; /*    */
import com.tensegrity.palojava.RuleInfo; /*    */
import com.tensegrity.palojava.impl.RuleImpl;

/*    */
/*    */public class RuleInfoBuilder
/*    */{
  /*    */public final RuleInfo create(PaloInfo parent, String[] response)
  /*    */{
    /* 54 */if (response.length < 2)
      /* 55 */throw new PaloException("Not enough information to create RuleInfo");
    /* 56 */CubeInfo cube = (CubeInfo) parent;
    /* 57 */String id = response[0];
    /* 58 */RuleImpl rule = new RuleImpl(cube, id);
    /* 59 */update(rule, response);
    /* 60 */return rule;
    /*    */}

  /*    */
  /*    */public final void update(RuleImpl rule, String[] response) {
    /* 64 */rule.setDefinition(response[1]);
    /*    */
    /* 66 */switch (response.length)
    /*    */{
    /*    */case 6:
      /* 68 */
      rule.setActive(response[5].equals("1"));
      /*    */case 5:
      /* 70 */
      rule.setTimestamp(Long.parseLong(response[4]) * 1000L);
      /*    */case 4:
      /* 72 */
      rule.setComment(response[3]);
      /*    */case 3:
      /* 74 */
      rule.setExternalIdentifier(response[2]);
      /*    */
    }
    /*    */}
  /*    */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.paloapi\lib\palo.jar
 * Qualified Name: com.tensegrity.palojava.http.builders.RuleInfoBuilder JD-Core
 * Version: 0.5.4
 */