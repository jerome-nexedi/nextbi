/*     */package org.palo.api.subsets.filter;

/*     */
/*     */import java.util.ArrayList; /*     */
import java.util.Arrays; /*     */
import java.util.Collection; /*     */
import java.util.Collections; /*     */
import java.util.HashMap; /*     */
import java.util.HashSet; /*     */
import java.util.Iterator; /*     */
import java.util.List; /*     */
import java.util.Map; /*     */
import java.util.Set; /*     */
import org.palo.api.Dimension; /*     */
import org.palo.api.Element; /*     */
import org.palo.api.ElementNode; /*     */
import org.palo.api.ElementNodeVisitor; /*     */
import org.palo.api.Hierarchy; /*     */
import org.palo.api.PaloAPIException; /*     */
import org.palo.api.exceptions.PaloIOException; /*     */
import org.palo.api.subsets.filter.settings.IntegerParameter; /*     */
import org.palo.api.subsets.filter.settings.SortingFilterSetting; /*     */
import org.palo.api.subsets.filter.settings.StringParameter; /*     */
import org.palo.api.utils.ElementNodeUtilities;

/*     */
/*     */public class SortingFilter extends AbstractSubsetFilter
/*     */implements StructuralFilter
/*     */{
  /*     */private final SortingFilterSetting setting;

  /* 76 */private final TextualSorter textualSorter = new TextualSorter();

  /*     */
  /*     *//** @deprecated */
  /*     */public SortingFilter(Dimension dimension)
  /*     */{
    /* 85 */this(dimension, new SortingFilterSetting());
    /*     */}

  /*     */
  /*     */public SortingFilter(Hierarchy hierarchy)
  /*     */{
    /* 94 */this(hierarchy, new SortingFilterSetting());
    /*     */}

  /*     */
  /*     *//** @deprecated */
  /*     */public SortingFilter(Dimension dimension, SortingFilterSetting setting)
  /*     */{
    /* 106 */super(dimension);
    /* 107 */this.setting = setting;
    /*     */}

  /*     */
  /*     */public SortingFilter(Hierarchy hierarchy, SortingFilterSetting setting)
  /*     */{
    /* 117 */super(hierarchy);
    /* 118 */this.setting = setting;
    /*     */}

  /*     */
  /*     */public final SortingFilter copy() {
    /* 122 */SortingFilter copy = new SortingFilter(this.hierarchy);
    /* 123 */copy.getSettings().adapt(this.setting);
    /* 124 */return copy;
    /*     */}

  /*     */
  /*     */public final SortingFilterSetting getSettings() {
    /* 128 */return this.setting;
    /*     */}

  /*     */
  /*     */public final List<ElementNode> sort(Set<Element> elements)
  /*     */{
    /* 136 */Set preservedNodes = new HashSet();
    /* 137 */List sortedNodes = new ArrayList();
    /* 138 */HashMap element2node = new HashMap();
    /* 139 */IndentComparator indentFilter = new IndentComparator(getSubset());
    /*     */
    /* 143 */int limitByLvl = -1;
    /* 144 */if (this.setting.doSortPerLevel())
    /*     */{
      /* 149 */limitByLvl = this.setting.getSortLevel().getValue().intValue();
      /*     */}
    /* 151 */boolean limitByType = this.setting.doSortByType();
    /* 152 */boolean sortLeafsOnly = this.setting.getSortTypeMode()
    /* 153 */.getValue().intValue() == 1;
    /*     */
    /* 156 */Element[] hierElements = this.hierarchy.getElements();
    /* 157 */int index = -1;
    /*     */boolean isLeaf;
    /* 158 */for (Element element : hierElements) {
      /* 159 */if (elements.contains(element)) {
        /* 160 */++index;
        /* 161 */ElementNode node = new ElementNode(element, null, index);
        /* 162 */element2node.put(element, node);
        /*     */
        /* 164 */if ((limitByLvl != -1)
          && (indentFilter.compare(element, limitByLvl) != 0))
        /*     */{
          /* 166 */preservedNodes.add(node);
          /*     */}
        /* 169 */else if (limitByType) {
          /* 170 */isLeaf = element.getChildCount() == 0;
          /* 171 */if ((sortLeafsOnly) && (!isLeaf)) {
            /* 172 */preservedNodes.add(node);
            /*     */}
          /* 174 */else if ((!sortLeafsOnly) && (isLeaf))
            /* 175 */preservedNodes.add(node);
          /*     */}
        /*     */else
        /*     */{
          /* 179 */sortedNodes.add(node);
          /*     */}
        /*     */}
      /*     */
      /*     */}
    /*     */
    /* 185 */switch (this.setting.getSortCriteria().getValue().intValue())
    /*     */{
    /*     */case 1:
      /* 187 */
      DataFilter dataFilter =
      /* 188 */(DataFilter) this.effectiveFilters.get(Integer.valueOf(8));
      /* 189 */
      if (dataFilter == null)
        /* 190 */throw new PaloAPIException("No data filter defined!");
      /* 191 */
      Collections.sort(sortedNodes, new DataSorter(dataFilter));
      /* 192 */break;
    /*     */case 2:
      /* 195 */
      Collections.sort(sortedNodes, this.textualSorter);
      /* 196 */break;
    /*     */case 3:
      /* 198 */
      AliasFilter aliasFilter =
      /* 199 */(AliasFilter) this.effectiveFilters.get(Integer.valueOf(64));
      /* 200 */
      if (aliasFilter == null)
        /* 201 */throw new PaloAPIException("No alias filter defined!");
      /* 202 */
      Collections.sort(sortedNodes, new AliasSorter(aliasFilter));
      /* 203 */break;
    /*     */default:
      /* 205 */
      Collections.sort(sortedNodes, new DefinitionSorter());
      /*     */
    }
    /*     */
    /* 209 */if (this.setting.doSortByAttribute()) {
      /* 210 */Collections.sort(sortedNodes,
      /* 211 */new AttributeSorter(this.setting.getSortAttribute().getValue()));
      /*     */}
    /*     */
    /* 215 */if (this.setting.doReverseOrder()) {
      /* 216 */int reverseOrder = this.setting.getOrderMode().getValue().intValue();
      /*     */
      /* 218 */if (reverseOrder == 1) {
        /* 219 */Collections.reverse(sortedNodes);
        /*     */}
      /*     */else {
        /* 222 */ArrayList newNodes = new ArrayList();
        /* 223 */for (Iterator iterator = sortedNodes.iterator(); ((Iterator) iterator)
          .hasNext();) {
          ElementNode node = (ElementNode) ((Iterator) iterator).next();
          /* 224 */insert((ElementNode) node, newNodes);
        }
        /*     */
        /* 226 */sortedNodes.clear();
        /* 227 */sortedNodes.addAll(newNodes);
        /*     */}
      /*     */}
    /*     */
    /* 231 */ElementNode[] allNodes =
    /* 232 */new ElementNode[preservedNodes.size() + sortedNodes.size()];
    /* 233 */for (Object node = preservedNodes.iterator(); ((Iterator) node)
      .hasNext();) {
      ElementNode pNode = (ElementNode) ((Iterator) node).next();
      /* 234 */allNodes[pNode.getIndex()] = pNode;
    }
    /*     */
    /* 236 */index = 0;
    /* 237 */for (Iterator node = sortedNodes.iterator(); ((Iterator) node)
      .hasNext();) {
      ElementNode pNode = (ElementNode) ((Iterator) node).next();
      /* 238 */index = insert(pNode, allNodes, index);
      /* 239 */if (index == -1)
        /*     */break;
    }
    /*     */
    /* 242 */List<ElementNode> nodes =
    /* 243 */new ArrayList(Arrays.asList(allNodes));
    /*     */
    /* 246 */if (this.setting.doHierarchy())
    /*     */{
      /* 247 */boolean bool1 = this.setting.getHierarchicalMode().getValue()
        .intValue() ==
      /* 248 */1;
      /*     */
      /* 250 */Object rootNodes = new ArrayList();
      /*     */
      /* 252 */createHierarchy((List) nodes, element2node, bool1);
      /*     */
      /* 254 */for (ElementNode node : nodes) {
        /* 255 */if (node.getParent() == null) {
          /* 256 */((List) rootNodes).add(node);
          /*     */}
        /*     */
        /*     */}
      /*     */
      /* 286 */((List) nodes).clear();
      /* 287 */((List) nodes).addAll((Collection) rootNodes);
      /*     */}
    /*     */
    /* 290 */return (List<ElementNode>) (List<ElementNode>) (List<ElementNode>) (List<ElementNode>) nodes;
    /*     */}

  /*     */
  /*     */public final void filter(final List<ElementNode> hierarchy,
    Set<Element> elements)
  /*     */{
    /* 295 */if (this.setting.getShowDuplicates().getValue().intValue() != 0)
      /*     */return;
    /* 297 */final HashSet visited = new HashSet();
    /* 298 */ElementNodeVisitor visitor = new ElementNodeVisitor() {
      /*     */public void visit(ElementNode node, ElementNode parent) {
        /* 300 */Element el = node.getElement();
        /* 301 */if (!visited.add(el)) {
          /* 302 */if (parent != null) {
            /* 303 */parent.removeChild(node);
            /*     */}
          /* 305 */hierarchy.remove(node);
          /*     */}
        /*     */}
      /*     */
    };
    /* 310 */ElementNodeUtilities.traverse(
    /* 311 */(ElementNode[]) hierarchy.toArray(new ElementNode[0]), visitor);
    /*     */}

  /*     */
  /*     */public final int getType()
  /*     */{
    /* 316 */return 16;
    /*     */}

  /*     */
  /*     */public final void initialize()
  /*     */{
    /*     */}

  /*     */
  /*     */public final void validateSettings()
  /*     */throws PaloIOException
  /*     */{
    /*     */}

  /*     */
  /*     */private final void insert(ElementNode node, List<ElementNode> nodes)
  /*     */{
    /* 337 */int nodeDepth = node.getElement().getDepth();
    /* 338 */int index = 0;
    /* 339 */for (ElementNode _node : nodes) {
      /* 340 */if (_node.getElement().getDepth() == nodeDepth)
        /*     */break;
      /* 342 */++index;
      /*     */}
    /* 344 */nodes.add(index, node);
    /*     */}

  /*     */
  /*     */private final int insert(ElementNode node, ElementNode[] nodes, int index) {
    /* 348 */for (int i = index; i < nodes.length; ++i) {
      /* 349 */if (nodes[i] == null) {
        /* 350 */nodes[i] = node;
        /* 351 */return ++i;
        /*     */}
      /*     */}
    /* 354 */return -1;
    /*     */}

  /*     */
  /*     */private final void createHierarchy(List<ElementNode> nodes,
    Map<Element, ElementNode> el2node, boolean showChildren)
  /*     */{
    /* 360 */Iterator elNodes = nodes.iterator();
    /* 361 */while (elNodes.hasNext()) {
      /* 362 */ElementNode node = (ElementNode) elNodes.next();
      /*     */
      /* 364 */Element element = node.getElement();
      /* 365 */Element[] parents = element.getParents();
      /* 366 */for (Element parent : parents) {
        /* 367 */ElementNode parentNode = (ElementNode) el2node.get(parent);
        /* 368 */if (parentNode != null)
        /*     */{
          /* 370 */parentNode.addChild(node);
          /*     */}
        /* 373 */else if (!showChildren) {
          /* 374 */elNodes.remove();
          /*     */}
        /*     */else {
          /* 377 */parentNode = getFirstNonNullParent(parent, el2node);
          /* 378 */if (parentNode == null)
            /*     */continue;
          /* 380 */parentNode.addChild(node);
          /*     */}
        /*     */}
      /*     */}
    /*     */}

  /*     */
  /*     */private final ElementNode getFirstNonNullParent(Element el,
    Map<Element, ElementNode> el2node)
  /*     */{
    /* 390 */if (el == null)
      /* 391 */return null;
    /* 392 */ElementNode pNode = null;
    /* 393 */for (Element parent : el.getParents()) {
      /* 394 */pNode = (ElementNode) el2node.get(parent);
      /* 395 */if (pNode != null)
        /*     */break;
      /*     */}
    /* 398 */return pNode;
    /*     */}
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name: org.palo.api.subsets.filter.SortingFilter JD-Core Version:
 * 0.5.4
 */