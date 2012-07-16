/*    */package org.palo.api.ext.subsets.states;

/*    */
/*    */import org.palo.api.impl.subsets.AbstractSubsetState;

/*    */
/*    */public class RegExState extends AbstractSubsetState
/*    */{
  /*    */public static final String ID = "regularexpression";

  /*    */
  /*    */public RegExState(String initialRegex)
  /*    */{
    /* 53 */super("regularexpression");
    /* 54 */setExpression(initialRegex);
    /*    */}
  /*    */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name: org.palo.api.ext.subsets.states.RegExState JD-Core Version:
 * 0.5.4
 */