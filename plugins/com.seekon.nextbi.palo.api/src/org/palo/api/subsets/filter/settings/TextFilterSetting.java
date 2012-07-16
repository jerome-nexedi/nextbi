/*     */package org.palo.api.subsets.filter.settings;

/*     */
/*     */import java.util.HashSet; /*     */
import org.palo.api.subsets.Subset2;

/*     */
/*     */public class TextFilterSetting extends AbstractFilterSettings
/*     */{
  /*     */private ObjectParameter expressions;

  /* 55 */private BooleanParameter extended = new BooleanParameter();

  /*     */
  /*     */public TextFilterSetting()
  /*     */{
    /* 61 */this.expressions = new ObjectParameter();
    /* 62 */this.extended.setValue(true);
    /* 63 */this.expressions.setValue(new HashSet());
    /*     */}

  /*     */
  /*     */public final void addExpression(String expr)
  /*     */{
    /* 72 */HashSet _expressions = (HashSet) this.expressions.getValue();
    /* 73 */_expressions.add(expr);
    /* 74 */markDirty();
    /*     */}

  /*     */
  /*     */public final void removeExpression(String expr)
  /*     */{
    /* 82 */HashSet _expressions = (HashSet) this.expressions.getValue();
    /* 83 */_expressions.remove(expr);
    /* 84 */markDirty();
    /*     */}

  /*     */
  /*     */public final void setExpressions(ObjectParameter expressions)
  /*     */{
    /* 94 */Object value = expressions.getValue();
    /* 95 */if (value instanceof HashSet)
      /* 96 */copyExpressions((HashSet) value);
    /* 97 */getExpressions().bind(this.subset);
    /*     */}

  /*     */
  /*     */public final ObjectParameter getExpressions()
  /*     */{
    /* 105 */return this.expressions;
    /*     */}

  /*     */
  /*     */public final BooleanParameter getExtended()
  /*     */{
    /* 114 */return this.extended;
    /*     */}

  /*     */
  /*     */public final void setExtended(boolean extended)
  /*     */{
    /* 123 */this.extended.setValue(extended);
    /*     */}

  /*     */
  /*     */public final void setExtended(BooleanParameter extended)
  /*     */{
    /* 131 */this.extended = extended;
    /* 132 */extended.bind(this.subset);
    /*     */}

  /*     */
  /*     */public final void reset()
  /*     */{
    /* 137 */this.extended.setValue(false);
    /* 138 */HashSet _expressions = (HashSet) this.expressions.getValue();
    /* 139 */_expressions.clear();
    /* 140 */markDirty();
    /*     */}

  /*     */
  /*     */public final void adapt(FilterSetting from) {
    /* 144 */if (!(from instanceof TextFilterSetting))
      /* 145 */return;
    /* 146 */TextFilterSetting setting = (TextFilterSetting) from;
    /*     */
    /* 148 */HashSet newExpressions =
    /* 149 */(HashSet) setting.getExpressions().getValue();
    /* 150 */copyExpressions(newExpressions);
    /* 151 */setExtended(setting.getExtended().getValue().booleanValue());
    /*     */}

  /*     */
  /*     */public final void bind(Subset2 subset) {
    /* 155 */super.bind(subset);
    /*     */
    /* 157 */this.expressions.bind(subset);
    /* 158 */this.extended.bind(subset);
    /*     */}

  /*     */public final void unbind() {
    /* 161 */super.unbind();
    /*     */
    /* 163 */this.expressions.unbind();
    /* 164 */this.extended.unbind();
    /*     */}

  /*     */
  /*     */private final void copyExpressions(HashSet<String> newExpressions) {
    /* 168 */HashSet _expressions = (HashSet) this.expressions.getValue();
    /* 169 */_expressions.clear();
    /* 170 */_expressions.addAll(newExpressions);
    /* 171 */markDirty();
    /*     */}
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name: org.palo.api.subsets.filter.settings.TextFilterSetting
 * JD-Core Version: 0.5.4
 */