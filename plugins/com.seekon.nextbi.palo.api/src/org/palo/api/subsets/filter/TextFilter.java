/*     */package org.palo.api.subsets.filter;

/*     */
/*     */import java.util.HashMap; /*     */
import java.util.HashSet; /*     */
import java.util.Set; /*     */
import java.util.regex.Matcher; /*     */
import java.util.regex.Pattern; /*     */
import org.palo.api.Dimension; /*     */
import org.palo.api.Element; /*     */
import org.palo.api.Hierarchy; /*     */
import org.palo.api.exceptions.PaloIOException; /*     */
import org.palo.api.subsets.filter.settings.ObjectParameter; /*     */
import org.palo.api.subsets.filter.settings.TextFilterSetting;

/*     */
/*     */public class TextFilter extends AbstractSubsetFilter
/*     */implements RestrictiveFilter
/*     */{
  /*     */private final TextFilterSetting setting;

  /*     */
  /*     *//** @deprecated */
  /*     */public TextFilter(Dimension dimension)
  /*     */{
    /* 71 */this(dimension.getDefaultHierarchy(), new TextFilterSetting());
    /*     */}

  /*     */
  /*     */public TextFilter(Hierarchy hierarchy)
  /*     */{
    /* 80 */this(hierarchy, new TextFilterSetting());
    /*     */}

  /*     */
  /*     *//** @deprecated */
  /*     */public TextFilter(Dimension dimension, TextFilterSetting setting)
  /*     */{
    /* 91 */super(dimension.getDefaultHierarchy());
    /* 92 */this.setting = setting;
    /*     */}

  /*     */
  /*     */public TextFilter(Hierarchy hierarchy, TextFilterSetting setting)
  /*     */{
    /* 102 */super(hierarchy);
    /* 103 */this.setting = setting;
    /*     */}

  /*     */
  /*     */public final TextFilter copy() {
    /* 107 */TextFilter copy = new TextFilter(this.hierarchy);
    /* 108 */copy.getSettings().adapt(this.setting);
    /* 109 */return copy;
    /*     */}

  /*     */
  /*     */public final TextFilterSetting getSettings() {
    /* 113 */return this.setting;
    /*     */}

  /*     */
  /*     */public final void filter(Set<Element> elements) {
    /* 117 */HashSet newElements = new HashSet();
    /* 118 */ObjectParameter exprParam = this.setting.getExpressions();
    /* 119 */Set expressions = (Set) exprParam.getValue();
    /*     */
    /* 121 */expressions = createParsedExpressions(expressions);
    /*     */
    /* 123 */for (Element element : elements) {
      /* 124 */if (accept(element, expressions))
        /* 125 */newElements.add(element);
      /*     */}
    /* 127 */elements.clear();
    /* 128 */elements.addAll(newElements);
    /*     */}

  /*     */
  /*     */public int getType()
  /*     */{
    /* 134 */return 1;
    /*     */}

  /*     */
  /*     */public final void initialize()
  /*     */{
    /*     */}

  /*     */
  /*     */public final void validateSettings() throws PaloIOException
  /*     */{
    /* 143 */ObjectParameter exprParam = this.setting.getExpressions();
    /* 144 */HashSet expressions = (HashSet) exprParam.getValue();
    /* 145 */if (expressions.isEmpty())
      /* 146 */throw new PaloIOException(
      /* 147 */"TextFilter: At least one expression is required!");
    /*     */}

  /*     */
  /*     */private final boolean accept(Element element, Set<String> expressions) {
    /* 151 */String elName = getValue(element);
    /*     */
    /* 153 */for (String expr : expressions)
    /*     */{
      /* 155 */Pattern p = Pattern.compile(expr);
      /* 156 */Matcher m = p.matcher(elName);
      /* 157 */if (m.find()) {
        /* 158 */return true;
        /*     */}
      /*     */}
    /*     */
    /* 162 */return expressions.isEmpty();
    /*     */}

  /*     */
  /*     */private final String getValue(Element element) {
    /* 166 */if (this.effectiveFilters.containsKey(Integer.valueOf(64))) {
      /* 167 */AliasFilter aliasFilter =
      /* 168 */(AliasFilter) this.effectiveFilters.get(Integer.valueOf(64));
      /* 169 */return aliasFilter.getAlias(element);
      /*     */}
    /* 171 */return element.getName();
    /*     */}

  /*     */
  /*     */private final Set<String> createParsedExpressions(Set<String> expressions) {
    /* 175 */Set _expressions = new HashSet(expressions.size());
    /* 176 */for (String expr : expressions)
      /* 177 */_expressions.add(parseWildcards(expr));
    /* 178 */return _expressions;
    /*     */}

  /*     */private final String parseWildcards(String str) {
    /* 181 */str = str.replaceAll("\\*", ".*");
    /* 182 */str = str.replaceAll("\\?", ".?");
    /* 183 */return str;
    /*     */}
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name: org.palo.api.subsets.filter.TextFilter JD-Core Version: 0.5.4
 */