/*    */package org.palo.api.ext.subsets.impl;

/*    */
/*    */import org.palo.api.Hierarchy; /*    */
import org.palo.api.HierarchyFilter; /*    */
import org.palo.api.Subset; /*    */
import org.palo.api.ext.subsets.SubsetHandler; /*    */
import org.palo.api.ext.subsets.SubsetStateHandler;

/*    */
/*    */public class SubsetStateHandlerFactory
/*    */{
  /* 62 */private static final SubsetStateHandlerFactory instance = new SubsetStateHandlerFactory();

  /*    */
  /* 70 */private final DefaultSubsetHandler handler = new DefaultSubsetHandler();

  /*    */
  /*    */public static final SubsetStateHandlerFactory getInstance()
  /*    */{
    /* 64 */return instance;
    /*    */}

  /*    */
  /*    */public final SubsetStateHandler create(String handlerID)
  /*    */{
    /* 75 */if (handlerID.equals("flat"))
      /* 76 */return new FlatStateHandler();
    /* 77 */if (handlerID.equals("hierarchical"))
      /* 78 */return new HierarchicalStateHandler();
    /* 79 */if (handlerID.equals("regularexpression"))
      /* 80 */return new RegExStateHandler();
    /* 81 */return null;
    /*    */}

  /*    */
  /*    */public final SubsetHandler create(SubsetStateHandler stateHandler) {
    /* 85 */Hierarchy hier = stateHandler.getSubset().getHierarchy();
    /* 86 */if (hier == null)
      /* 87 */return null;
    /* 88 */HierarchyFilter filter = stateHandler.createHierarchyFilter(hier);
    /* 89 */if (filter == null) {
      /* 90 */return null;
      /*    */}
    /* 92 */this.handler.use(hier, filter, stateHandler.getSubsetState());
    /* 93 */return this.handler;
    /*    */}
  /*    */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name: org.palo.api.ext.subsets.impl.SubsetStateHandlerFactory
 * JD-Core Version: 0.5.4
 */