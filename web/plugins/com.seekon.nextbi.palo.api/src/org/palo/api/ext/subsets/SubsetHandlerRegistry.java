/*     */package org.palo.api.ext.subsets;

/*     */
/*     */import java.util.Collection; /*     */
import java.util.HashMap; /*     */
import java.util.Map; /*     */
import org.palo.api.Subset; /*     */
import org.palo.api.SubsetState; /*     */
import org.palo.api.ext.subsets.impl.SubsetStateHandlerFactory;

/*     */
/*     *//** @deprecated */
/*     */public class SubsetHandlerRegistry
/*     */{
  /* 91 */private static final SubsetHandlerRegistry instance = new SubsetHandlerRegistry();

  /*     */
  /* 103 */private final Map stateHandlers = new HashMap();

  /*     */
  /* 105 */private final SubsetStateHandlerFactory stateFactory = SubsetStateHandlerFactory
    .getInstance();

  /*     */
  /*     */public static final SubsetHandlerRegistry getInstance()
  /*     */{
    /* 97 */return instance;
    /*     */}

  /*     */
  /*     */private SubsetHandlerRegistry()
  /*     */{
    /* 109 */register("flat", this.stateFactory.create("flat"));
    /* 110 */register("hierarchical", this.stateFactory.create("hierarchical"));
    /* 111 */register("regularexpression", this.stateFactory
      .create("regularexpression"));
    /*     */}

  /*     */
  /*     */public final SubsetHandler getHandler(Subset subset)
  /*     */{
    /* 123 */return getHandler(subset, subset.getActiveState());
    /*     */}

  /*     */
  /*     */public final SubsetHandler getHandler(Subset subset,
    SubsetState subsetState)
  /*     */{
    /* 136 */if (subsetState == null) {
      /* 137 */return null;
      /*     */}
    /* 139 */return getHandler(subset, subsetState.getId());
    /*     */}

  /*     */
  /*     */public final SubsetHandler getHandler(Subset subset, String stateId)
  /*     */{
    /* 153 */if ((subset == null) || (stateId == null))
      /* 154 */return null;
    /* 155 */SubsetStateHandler stateHandler =
    /* 156 */(SubsetStateHandler) this.stateHandlers.get(stateId);
    /* 157 */if (stateHandler == null) {
      /* 158 */return null;
      /*     */}
    /*     */
    /* 161 */stateHandler.use(subset, subset.getState(stateId));
    /* 162 */return this.stateFactory.create(stateHandler);
    /*     */}

  /*     */
  /*     */public final SubsetStateHandler getStateHandler(String stateId)
  /*     */{
    /* 175 */return (SubsetStateHandler) this.stateHandlers.get(stateId);
    /*     */}

  /*     */
  /*     */public final SubsetStateHandler[] getAllStateHandler()
  /*     */{
    /* 183 */return (SubsetStateHandler[]) this.stateHandlers.values().toArray(
    /* 184 */new SubsetStateHandler[this.stateHandlers.size()]);
    /*     */}

  /*     */
  /*     */public final void register(String stateId, SubsetStateHandler handler)
  /*     */{
    /* 194 */if (stateId == null)
      /* 195 */return;
    /* 196 */this.stateHandlers.put(stateId, handler);
    /*     */}
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name: org.palo.api.ext.subsets.SubsetHandlerRegistry JD-Core
 * Version: 0.5.4
 */