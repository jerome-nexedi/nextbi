/*     */package org.palo.api.impl;

/*     */
/*     */import java.util.ArrayList; /*     */
import java.util.HashMap; /*     */
import java.util.Map; /*     */
import org.palo.api.Connection; /*     */
import org.palo.api.Database; /*     */
import org.palo.api.Dimension; /*     */
import org.palo.api.Hierarchy; /*     */
import org.palo.api.PaloAPIException; /*     */
import org.palo.api.Subset; /*     */
import org.palo.api.impl.subsets.SubsetPersistence; /*     */
import org.palo.api.persistence.PaloPersistenceException;

/*     */
/*     *//** @deprecated */
/*     */class SubsetStorageHandler
/*     */{
  /*     */private final Database database;

  /*     */private Map<String, Subset> loadedSubsets;

  /*     */
  /*     */SubsetStorageHandler(Database database)
  /*     */{
    /* 62 */this.database = database;
    /*     */}

  /*     */
  /*     */final Subset addSubset(Dimension dimension, String id, String name)
  /*     */{
    /* 69 */return addSubset(dimension.getDefaultHierarchy(), null, name);
    /*     */}

  /*     */
  /*     */final Subset addSubset(Hierarchy hierarchy, String id, String name)
  /*     */{
    /* 74 */return addSubset(hierarchy, null, name);
    /*     */}

  /*     */
  /*     */final Subset addSubset(Dimension dimension, String name) {
    /* 78 */throw new PaloAPIException(
      "Legacy subsets are not supported anymore! Please use new subsets!");
    /*     */}

  /*     */
  /*     */final Subset addSubset(Hierarchy hierarchy, String name) {
    /* 82 */throw new PaloAPIException(
      "Legacy subsets are not supported anymore! Please use new subsets!");
    /*     */}

  /*     */
  /*     */final void removeSubset(Dimension dimension, Subset subset) {
    /* 86 */if ((subset == null)
      || (!subset.getHierarchy().equals(dimension.getDefaultHierarchy()))) {
      /*     */return;
      /*     */}
    /* 89 */SubsetPersistence.getInstance().delete(subset);
    /* 90 */if (this.loadedSubsets != null)
      /* 91 */this.loadedSubsets.remove(subset.getId());
    /*     */}

  /*     */
  /*     */final void removeSubset(Hierarchy hierarchy, Subset subset)
  /*     */{
    /* 96 */if ((subset == null) || (!subset.getHierarchy().equals(hierarchy))) {
      /*     */return;
      /*     */}
    /* 99 */SubsetPersistence.getInstance().delete(subset);
    /* 100 */this.loadedSubsets.remove(subset.getId());
    /*     */}

  /*     */
  /*     */final Subset getSubset(Dimension dimension, String id)
  /*     */throws PaloPersistenceException
  /*     */{
    /* 106 */if (this.loadedSubsets == null) {
      /* 107 */reload();
      /*     */}
    /* 109 */Subset subset = (Subset) this.loadedSubsets.get(id);
    /* 110 */if ((subset != null)
      && (subset.getHierarchy().equals(dimension.getDefaultHierarchy())))
      /* 111 */return subset;
    /* 112 */return null;
    /*     */}

  /*     */
  /*     */final Subset getSubset(Hierarchy hierarchy, String id)
    throws PaloPersistenceException
  /*     */{
    /* 117 */if (this.loadedSubsets.isEmpty()) {
      /* 118 */reload();
      /*     */}
    /* 120 */Subset subset = (Subset) this.loadedSubsets.get(id);
    /* 121 */if ((subset != null) && (subset.getHierarchy().equals(hierarchy)))
      /* 122 */return subset;
    /* 123 */return null;
    /*     */}

  /*     */
  /*     */final Subset[] getSubsets(Dimension dimension)
    throws PaloPersistenceException
  /*     */{
    /* 128 */if (dimension.getDatabase().getConnection().getType() == 3) {
      /* 129 */return new Subset[0];
      /*     */}
    /* 131 */if (this.loadedSubsets == null) {
      /* 132 */reload();
      /*     */}
    /* 134 */ArrayList subsets = new ArrayList();
    /* 135 */for (Subset subset : this.loadedSubsets.values()) {
      /* 136 */if (subset.getHierarchy().equals(dimension.getDefaultHierarchy()))
        /* 137 */subsets.add(subset);
      /*     */}
    /* 139 */return (Subset[]) subsets.toArray(new Subset[subsets.size()]);
    /*     */}

  /*     */
  /*     */final Subset[] getSubsets(Hierarchy hierarchy)
    throws PaloPersistenceException
  /*     */{
    /* 144 */if (hierarchy.getDimension().getDatabase().getConnection().getType() == 3) {
      /* 145 */return new Subset[0];
      /*     */}
    /* 147 */if (this.loadedSubsets == null) {
      /* 148 */reload();
      /*     */}
    /* 150 */ArrayList subsets = new ArrayList();
    /* 151 */for (Subset subset : this.loadedSubsets.values()) {
      /* 152 */if (subset.getHierarchy().equals(hierarchy))
        /* 153 */subsets.add(subset);
      /*     */}
    /* 155 */return (Subset[]) subsets.toArray(new Subset[subsets.size()]);
    /*     */}

  /*     */
  /*     */final void reload() {
    /* 159 */if (this.loadedSubsets == null)
      /* 160 */this.loadedSubsets = new HashMap();
    /*     */else
      /* 162 */this.loadedSubsets.clear();
    /* 163 */reloadAll();
    /*     */}

  /*     */
  /*     */final boolean hasSubsets() {
    /* 167 */return SubsetPersistence.getInstance().hasSubsets(this.database);
    /*     */}

  /*     */
  /*     */private final void reloadAll()
  /*     */{
    /* 174 */Subset[] subsets = SubsetPersistence.getInstance().loadAll(
      this.database);
    /* 175 */for (Subset subset : subsets)
      /* 176 */this.loadedSubsets.put(subset.getId(), subset);
    /*     */}
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name: org.palo.api.impl.SubsetStorageHandler JD-Core Version: 0.5.4
 */