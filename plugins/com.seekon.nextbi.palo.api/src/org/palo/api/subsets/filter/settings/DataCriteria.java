/*     */package org.palo.api.subsets.filter.settings;

/*     */
/*     */import org.palo.api.subsets.Subset2;

/*     */
/*     */public class DataCriteria
/*     */{
  /*     */public static final String LESSER = "<";

  /*     */public static final String LESSER_EQUAL = "<=";

  /*     */public static final String GREATER = ">";

  /*     */public static final String GREATER_EQUAL = ">=";

  /*     */public static final String NOT_EQUAL = "<>";

  /*     */public static final String EQUAL = "=";

  /* 62 */public static final String[] ALL_OPERATORS = { "<", "<=", ">", ">=",
    "<>", "=" };

  /*     */private int operator1;

  /*     */private int operator2;

  /*     */private StringParameter operand1;

  /*     */private StringParameter operand2;

  /*     */private Subset2 subset;

  /*     */
  /*     */public DataCriteria(String operator, String operand)
  /*     */{
    /* 80 */this.operator1 = getIndex(operator);
    /* 81 */this.operand1 = new StringParameter();
    /* 82 */this.operand2 = new StringParameter();
    /* 83 */this.operand1.setValue(operand);
    /*     */}

  /*     */
  /*     */public final boolean hasSecondOperator()
  /*     */{
    /* 92 */return (this.operand2 != null) && (!this.operand2.equals(""));
    /*     */}

  /*     */
  /*     */public final String getFirstOperator()
  /*     */{
    /* 99 */return ALL_OPERATORS[this.operator1];
    /*     */}

  /*     */
  /*     */public final int getFirstOperatorIndex()
  /*     */{
    /* 107 */return this.operator1;
    /*     */}

  /*     */
  /*     */public final void setFirstOperator(String operator1)
  /*     */{
    /* 114 */this.operator1 = getIndex(operator1);
    /* 115 */markDirty();
    /*     */}

  /*     */
  /*     */public final void setFirstOperator(int index)
  /*     */{
    /* 123 */this.operator1 = index;
    /* 124 */markDirty();
    /*     */}

  /*     */
  /*     */public final String getSecondOperator()
  /*     */{
    /* 131 */return ALL_OPERATORS[this.operator2];
    /*     */}

  /*     */
  /*     */public final int getSecondOperatorIndex()
  /*     */{
    /* 139 */return this.operator2;
    /*     */}

  /*     */
  /*     */public final void setSecondOperator(String operator2)
  /*     */{
    /* 146 */this.operator2 = getIndex(operator2);
    /* 147 */markDirty();
    /*     */}

  /*     */
  /*     */public final void setSecondOperator(int index)
  /*     */{
    /* 155 */this.operator2 = index;
    /* 156 */markDirty();
    /*     */}

  /*     */
  /*     */public final StringParameter getFirstOperand()
  /*     */{
    /* 168 */return this.operand1;
    /*     */}

  /*     */
  /*     */public final void setFirstOperand(String operand1)
  /*     */{
    /* 176 */this.operand1.setValue(operand1);
    /*     */}

  /*     */
  /*     */public final void setFirstOperand(StringParameter operand1) {
    /* 180 */this.operand1 = operand1;
    /* 181 */this.operand1.bind(this.subset);
    /* 182 */markDirty();
    /*     */}

  /*     */
  /*     */public final StringParameter getSecondOperand()
  /*     */{
    /* 193 */return this.operand2;
    /*     */}

  /*     */
  /*     */public final void setSecondOperand(String operand2)
  /*     */{
    /* 201 */this.operand2.setValue(operand2);
    /*     */}

  /*     */public final void setSecondOperand(StringParameter operand2) {
    /* 204 */this.operand2 = operand2;
    /* 205 */this.operand2.bind(this.subset);
    /* 206 */markDirty();
    /*     */}

  /*     */
  /*     */public final void bind(Subset2 subset)
  /*     */{
    /* 215 */this.subset = subset;
    /* 216 */this.operand1.bind(subset);
    /* 217 */this.operand2.bind(subset);
    /* 218 */markDirty();
    /*     */}

  /*     */
  /*     */public final void unbind()
  /*     */{
    /* 225 */this.subset = null;
    /* 226 */this.operand1.unbind();
    /* 227 */this.operand2.unbind();
    /*     */}

  /*     */
  /*     */final DataCriteria copy()
  /*     */{
    /* 235 */DataCriteria copy = new DataCriteria(ALL_OPERATORS[this.operator1],
      this.operand1.getValue());
    /* 236 */copy.operator2 = this.operator2;
    /* 237 */copy.operand2 = this.operand2;
    /* 238 */return copy;
    /*     */}

  /*     */
  /*     */private final int getIndex(String operator) {
    /* 242 */for (int i = 0; i < ALL_OPERATORS.length; ++i) {
      /* 243 */if (ALL_OPERATORS[i].equals(operator))
        /* 244 */return i;
      /*     */}
    /* 246 */throw new RuntimeException("Illegal operator!");
    /*     */}

  /*     */
  /*     */private final void markDirty() {
    /* 250 */if (this.subset != null)
      /* 251 */this.subset.modified();
    /*     */}
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name: org.palo.api.subsets.filter.settings.DataCriteria JD-Core
 * Version: 0.5.4
 */