/*     */package org.palo.api.impl.subsets;

/*     */
/*     */import java.util.Collection; /*     */
import java.util.HashMap; /*     */
import org.palo.api.Attribute; /*     */
import org.palo.api.Database; /*     */
import org.palo.api.Dimension; /*     */
import org.palo.api.Hierarchy; /*     */
import org.palo.api.Subset; /*     */
import org.palo.api.SubsetState; /*     */
import org.palo.api.ext.subsets.states.RegExState;

/*     */
/*     */class SubsetImpl
/*     */implements Subset
/*     */{
  /*     */private final String id;

  /*     */private final Hierarchy srcHierarchy;

  /* 58 */private HashMap states = new HashMap();

  /*     */private String name;

  /*     */private String description;

  /*     */private Attribute alias;

  /*     */private SubsetState activeState;

  /*     */
  /*     */SubsetImpl(String id, String name, Hierarchy srcHierarchy)
  /*     */{
    /* 73 */this.id = id;
    /* 74 */this.name = name;
    /* 75 */this.srcHierarchy = srcHierarchy;
    /*     */}

  /*     */
  /*     */void reset() {
    /* 79 */this.alias = null;
    /* 80 */this.activeState = null;
    /* 81 */this.states.clear();
    /*     */}

  /*     */
  /*     */public void addState(SubsetState state) {
    /* 85 */if (!this.states.containsKey(state.getId()))
      /* 86 */this.states.put(state.getId(), state);
    /*     */}

  /*     */
  /*     */public void removeState(SubsetState state) {
    /* 90 */this.states.remove(state.getId());
    /*     */}

  /*     */
  /*     */public String getDescription() {
    /* 94 */return (this.description == null) ? "" : this.description;
    /*     */}

  /*     */
  /*     */public String getId() {
    /* 98 */return this.id;
    /*     */}

  /*     */
  /*     */public SubsetState getActiveState()
  /*     */{
    /* 103 */if (this.activeState == null)
      /* 104 */setActiveState(new RegExState(".*"));
    /* 105 */return this.activeState;
    /*     */}

  /*     */
  /*     */public SubsetState getState(String id) {
    /* 109 */return (SubsetState) this.states.get(id);
    /*     */}

  /*     */
  /*     */void setActiveState(String stateId) {
    /* 113 */SubsetState state = (SubsetState) this.states.get(stateId);
    /* 114 */setActiveState(state);
    /*     */}

  /*     */
  /*     */public final void setActiveState(SubsetState activeState) {
    /* 118 */if (activeState == null)
      /* 119 */return;
    /* 120 */this.activeState = activeState;
    /*     */
    /* 122 */this.states.put(activeState.getId(), activeState);
    /*     */}

  /*     */public final void setName(String name) {
    /* 125 */this.name = name;
    /*     */}

  /*     */public String getName() {
    /* 128 */return this.name;
    /*     */}

  /*     */
  /*     */public Dimension getDimension()
  /*     */{
    /* 136 */return this.srcHierarchy.getDimension();
    /*     */}

  /*     */
  /*     */public Hierarchy getHierarchy() {
    /* 140 */return this.srcHierarchy;
    /*     */}

  /*     */
  /*     */public SubsetState[] getStates() {
    /* 144 */return (SubsetState[]) this.states.values().toArray(
    /* 145 */new SubsetState[this.states.size()]);
    /*     */}

  /*     */
  /*     */public void setDescription(String description) {
    /* 149 */this.description = description;
    /*     */}

  /*     */
  /*     */public void save() {
    /* 153 */SubsetPersistence.getInstance().save(this);
    /*     */}

  /*     */
  /*     */public void delete() {
    /* 157 */this.srcHierarchy.removeSubset(this);
    /*     */}

  /*     */
  /*     */public Attribute getAlias()
  /*     */{
    /* 162 */return this.alias;
    /*     */}

  /*     */
  /*     */public void setAlias(Attribute alias) {
    /* 166 */this.alias = alias;
    /*     */}

  /*     */
  /*     */public boolean equals(Object obj)
  /*     */{
    /* 171 */if (!(obj instanceof Subset)) {
      /* 172 */return false;
      /*     */}
    /* 174 */Subset other = (Subset) obj;
    /* 175 */if (this.id.equals(other.getId()))
    /*     */{
      /* 177 */Dimension dim = getDimension();
      /* 178 */Dimension otherDim = other.getDimension();
      /* 179 */String dimName = dim.getName();
      /* 180 */String otherDimName = otherDim.getName();
      /* 181 */String dbName = dim.getDatabase().getName();
      /* 182 */String otherDbName = otherDim.getDatabase().getName();
      /* 183 */return (dimName.equals(otherDimName))
        && (dbName.equals(otherDbName));
      /*     */}
    /* 185 */return false;
    /*     */}

  /*     */
  /*     */public int hashCode() {
    /* 189 */int hc = 17;
    /* 190 */Dimension dim = getDimension();
    /* 191 */Database db = dim.getDatabase();
    /* 192 */hc += 31 * db.getName().hashCode();
    /* 193 */hc += 31 * dim.getName().hashCode();
    /* 194 */hc += 31 * this.id.hashCode();
    /* 195 */return hc;
    /*     */}
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name: org.palo.api.impl.subsets.SubsetImpl JD-Core Version: 0.5.4
 */