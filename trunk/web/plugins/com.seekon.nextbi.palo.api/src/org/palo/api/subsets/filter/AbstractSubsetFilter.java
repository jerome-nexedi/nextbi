/*     */package org.palo.api.subsets.filter;

/*     */
/*     */import java.util.HashMap; /*     */
import org.palo.api.Dimension; /*     */
import org.palo.api.Hierarchy; /*     */
import org.palo.api.subsets.Subset2; /*     */
import org.palo.api.subsets.SubsetFilter; /*     */
import org.palo.api.subsets.filter.settings.FilterSetting;

/*     */
/*     */abstract class AbstractSubsetFilter
/*     */implements SubsetFilter
/*     */{
  /* 59 */protected final HashMap<Integer, EffectiveFilter> effectiveFilters = new HashMap();

  /*     */protected final Hierarchy hierarchy;

  /*     */private Subset2 subset;

  /*     */
  /*     */AbstractSubsetFilter(Dimension dimension)
  /*     */{
    /* 65 */this.hierarchy = dimension.getDefaultHierarchy();
    /*     */}

  /*     */
  /*     */AbstractSubsetFilter(Hierarchy hierarchy) {
    /* 69 */this.hierarchy = hierarchy;
    /*     */}

  /*     */
  /*     */public final void add(EffectiveFilter filter) {
    /* 73 */this.effectiveFilters.put(new Integer(filter.getType()), filter);
    /*     */}

  /*     */
  /*     */public final void remove(EffectiveFilter filter) {
    /* 77 */this.effectiveFilters.remove(new Integer(filter.getType()));
    /*     */}

  /*     */
  /*     */public final void reset() {
    /* 81 */this.effectiveFilters.clear();
    /* 82 */getSettings().reset();
    /*     */}

  /*     */
  /*     */public final Subset2 getSubset() {
    /* 86 */return this.subset;
    /*     */}

  /*     */
  /*     */public final void bind(Subset2 subset) {
    /* 90 */this.subset = subset;
    /* 91 */getSettings().bind(subset);
    /* 92 */markDirty();
    /*     */}

  /*     */
  /*     */public final void unbind() {
    /* 96 */this.subset = null;
    /* 97 */getSettings().unbind();
    /*     */}

  /*     */
  /*     */protected final void markDirty() {
    /* 101 */if (this.subset != null)
      /* 102 */this.subset.modified();
    /*     */}

  /*     */
  /*     */public final Dimension getDimension() {
    /* 106 */return this.hierarchy.getDimension();
    /*     */}

  /*     */
  /*     */public final Hierarchy getHierarchy() {
    /* 110 */return this.hierarchy;
    /*     */}

  /*     */
  /*     */public final void adapt(SubsetFilter from) {
    /* 114 */if (!(from instanceof AbstractSubsetFilter)) {
      /* 115 */return;
      /*     */}
    /* 117 */AbstractSubsetFilter fromFilter = (AbstractSubsetFilter) from;
    /* 118 */reset();
    /*     */
    /* 120 */this.effectiveFilters.putAll(fromFilter.effectiveFilters);
    /*     */
    /* 122 */getSettings().adapt(fromFilter.getSettings());
    /*     */}
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name: org.palo.api.subsets.filter.AbstractSubsetFilter JD-Core
 * Version: 0.5.4
 */