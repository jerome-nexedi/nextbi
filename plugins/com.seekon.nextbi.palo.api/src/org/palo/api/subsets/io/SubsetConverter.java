/*     */package org.palo.api.subsets.io;

/*     */
/*     */import java.io.PrintStream; /*     */
import java.util.ArrayList; /*     */
import java.util.Arrays; /*     */
import java.util.List; /*     */
import org.palo.api.Attribute; /*     */
import org.palo.api.Dimension; /*     */
import org.palo.api.Element; /*     */
import org.palo.api.ElementNode; /*     */
import org.palo.api.Hierarchy; /*     */
import org.palo.api.PaloAPIException; /*     */
import org.palo.api.Subset; /*     */
import org.palo.api.SubsetState; /*     */
import org.palo.api.exceptions.PaloIOException; /*     */
import org.palo.api.ext.subsets.SubsetHandlerRegistry; /*     */
import org.palo.api.subsets.Subset2; /*     */
import org.palo.api.subsets.SubsetFilter; /*     */
import org.palo.api.subsets.filter.AliasFilter; /*     */
import org.palo.api.subsets.filter.PicklistFilter; /*     */
import org.palo.api.subsets.filter.SortingFilter; /*     */
import org.palo.api.subsets.filter.TextFilter; /*     */
import org.palo.api.subsets.filter.settings.AliasFilterSetting; /*     */
import org.palo.api.subsets.filter.settings.PicklistFilterSetting; /*     */
import org.palo.api.subsets.filter.settings.SortingFilterSetting; /*     */
import org.palo.api.subsets.filter.settings.TextFilterSetting;

/*     */
/*     */class SubsetConverter
/*     */{
  /*     */final void convert(Subset[] subsets, int type, boolean remove)
  /*     */throws PaloIOException
  /*     */{
    /* 95 */ArrayList failedSubsets = new ArrayList();
    /* 96 */for (Subset subset : subsets) {
      /*     */try {
        /* 98 */Subset2 newSubset = toNewSubset(subset, type);
        /* 99 */newSubset.save();
        /*     */} catch (PaloIOException e) {
        /* 101 */failedSubsets.add(subset.getName());
        /*     */}
      /* 103 */if (remove) {
        /* 104 */Dimension dimension = subset.getDimension();
        /*     */try {
          /* 106 */dimension.removeSubset(subset);
          /*     */} catch (PaloAPIException pex) {
          /* 108 */System.err.println("failed to remove legacy subset '" +
          /* 109 */subset.getName() + "'!");
          /*     */}
        /*     */}
      /*     */}
    /* 113 */if (!failedSubsets.isEmpty()) {
      /* 114 */PaloIOException paloEx =
      /* 115 */new PaloIOException("Some subset transformation failed!");
      /* 116 */paloEx.setData(failedSubsets.toArray(new String[0]));
      /* 117 */throw paloEx;
      /*     */}
    /*     */}

  /*     */
  /*     */private final Subset2 toNewSubset(Subset subset, int type)
    throws PaloIOException
  /*     */{
    /* 123 */String name = subset.getName();
    /*     */
    /* 125 */Dimension dimension = subset.getDimension();
    /* 126 */org.palo.api.subsets.SubsetHandler subHandler = dimension
      .getSubsetHandler();
    /*     */
    /* 128 */Subset2 newSubset = null;
    /* 129 */String subId = subHandler.getSubsetId(name, type);
    /* 130 */if (subId == null)
      /* 131 */newSubset = subHandler.addSubset(name, type);
    /*     */else {
      /* 133 */newSubset = subHandler.getSubset(subId, type);
      /*     */}
    /* 135 */if (newSubset != null)
    /*     */{
      /* 144 */SubsetState state = subset.getActiveState();
      /* 145 */String stateId = state.getId();
      /* 146 */if (stateId.equals("hierarchical")) {
        /* 147 */addHierarchicalFilter(newSubset, subset);
        /*     */}
      /* 149 */else if (stateId.equals("regularexpression"))
      /*     */{
        /* 151 */Attribute alias = state.getSearchAttribute();
        /* 152 */if (alias != null) {
          /* 153 */newSubset.add(createAliasFilter(subset, alias.getId()));
          /*     */}
        /*     */
        /* 157 */newSubset.add(createTextFilter(subset, state));
        /*     */
        /* 159 */SubsetFilter sortFilter =
        /* 160 */newSubset.getFilter(16);
        /* 161 */if (sortFilter == null) {
          /* 162 */sortFilter = new SortingFilter(subset.getDimension());
          /* 163 */newSubset.add(sortFilter);
          /*     */}
        /* 165 */SortingFilterSetting settings =
        /* 166 */(SortingFilterSetting) sortFilter.getSettings();
        /* 167 */settings.setSortCriteria(0);
        /* 168 */settings.setHierarchicalMode(1);
        /*     */}
      /* 170 */else if (stateId.equals("flat"))
      /*     */{
        /* 172 */org.palo.api.ext.subsets.SubsetHandler handler =
        /* 173 */SubsetHandlerRegistry.getInstance().getHandler(subset, "flat");
        /* 174 */List nodes = (handler != null) ?
        /* 175 */handler.getVisibleRootNodesAsList() : new ArrayList();
        /* 176 */Element[] elements = new Element[nodes.size()];
        /* 177 */for (int i = 0; i < elements.length; ++i) {
          /* 178 */elements[i] = ((ElementNode) nodes.get(i)).getElement();
          /*     */}
        /*     */
        /* 181 */newSubset.add(createPickList(subset, elements));
        /*     */}
      /*     */}
    /* 184 */return newSubset;
    /*     */}

  /*     */
  /*     */private final TextFilter createTextFilter(Subset subset,
    SubsetState state) {
    /* 188 */TextFilter txtFilter = new TextFilter(subset.getDimension());
    /* 189 */TextFilterSetting tfInfo = txtFilter.getSettings();
    /* 190 */tfInfo.addExpression(state.getExpression());
    /* 191 */tfInfo.setExtended(true);
    /* 192 */return txtFilter;
    /*     */}

  /*     */
  /*     */private final PicklistFilter createPickList(Subset subset,
    Element[] elements) {
    /* 196 */PicklistFilter pickList = new PicklistFilter(subset.getDimension());
    /* 197 */PicklistFilterSetting plInfo = pickList.getSettings();
    /* 198 */for (int i = 0; i < elements.length; ++i)
      /* 199 */plInfo.addElement(elements[i].getId());
    /* 200 */plInfo.setInsertMode(3);
    /* 201 */return pickList;
    /*     */}

  /*     */
  /*     */private final AliasFilter createAliasFilter(Subset subset, String alias) {
    /* 205 */AliasFilterSetting setting = new AliasFilterSetting();
    /* 206 */setting.setAlias(2, alias);
    /* 207 */return new AliasFilter(subset.getDimension(), setting);
    /*     */}

  /*     */
  /*     */private final void addHierarchicalFilter(Subset2 newSubset, Subset subset) {
    /* 211 */SubsetState state = subset.getState("hierarchical");
    /*     */
    /* 213 */SortingFilter sortFilter = new SortingFilter(subset.getDimension());
    /* 214 */sortFilter.getSettings().setHierarchicalMode(1);
    /* 215 */Element[] elements = state.getVisibleElements();
    /*     */
    /* 217 */sort(elements);
    /* 218 */PicklistFilter pickList = createPickList(subset, elements);
    /* 219 */pickList.getSettings().setInsertMode(3);
    /* 220 */newSubset.add(sortFilter);
    /* 221 */newSubset.add(pickList);
    /*     */}

  /*     */
  /*     */private final void sort(Element[] elements) {
    /* 225 */if (elements.length == 0) {
      /* 226 */return;
      /*     */}
    /* 228 */Hierarchy hierarchy = elements[0].getHierarchy();
    /* 229 */ElementComparator comparator = new ElementComparator(hierarchy);
    /* 230 */Arrays.sort(elements, comparator);
    /*     */}
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name: org.palo.api.subsets.io.SubsetConverter JD-Core Version:
 * 0.5.4
 */