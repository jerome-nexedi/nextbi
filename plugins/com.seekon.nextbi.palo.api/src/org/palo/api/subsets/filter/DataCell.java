/*     */package org.palo.api.subsets.filter;

/*     */
/*     */import org.palo.api.Element; /*     */
import org.palo.api.Hierarchy; /*     */
import org.palo.api.subsets.filter.settings.DataCriteria; /*     */
import org.palo.api.subsets.filter.settings.StringParameter;

/*     */
/*     */class DataCell
/*     */{
  /*     */private final Element element;

  /*     */private final Element[][] coordinates;

  /*     */private Object[] values;

  /* 317 */private boolean isString = false;

  /*     */
  /*     */DataCell(Element element, Hierarchy[] cubeHiers, String[][] slices) {
    /* 320 */this.element = element;
    /* 321 */this.coordinates = determineCoordinates(cubeHiers, slices);
    /*     */}

  /*     */
  /*     */final Element getElement() {
    /* 325 */return this.element;
    /*     */}

  /*     */
  /*     */final Element[][] getCoordinates() {
    /* 329 */return this.coordinates;
    /*     */}

  /*     */
  /*     */final boolean isString() {
    /* 333 */return false;
    /*     */}

  /*     */
  /*     */final void setValues(Object[] values) {
    /* 337 */this.values = values;
    /*     */}

  /*     */
  /*     */final boolean fulfills(int cellOperator, DataCriteria criteria) {
    /* 341 */switch (cellOperator)
    /*     */{
    /*     */case 1:
      /* 342 */
      return areAll(criteria);
      /*     */case 4:
      /* 343 */
      return isAny(criteria);
      /*     */case 7:
      /* 345 */
      if (this.values.length < 1)
        /* 346 */return isInCriteriaRegion("", criteria);
      return isInCriteriaRegion(this.values[0].toString(), criteria);
      /*     */case 2:
      /*     */
    case 3:
      /*     */
    case 5:
      /*     */
    case 6:
    }
    Object value = getValue(cellOperator);
    /* 350 */return isInCriteriaRegion(value, criteria);
    /*     */}

  /*     */
  /*     */final Object getValue(int operator)
  /*     */{
    /* 355 */switch (operator) {
    /*     */case 3:
      /* 357 */
      return Double.valueOf(getMax());
      /*     */case 5:
      /* 359 */
      return Double.valueOf(getMin());
      /*     */case 2:
      /* 361 */
      return Double.valueOf(getAvg());
      /*     */case 7:
      /* 363 */
      return (this.values.length > 0) ? this.values[0].toString() : "";
      /*     */case 4:
      /*     */
    case 6:
      /* 366 */
    }
    return Double.valueOf(getSum());
    /*     */}

  /*     */
  /*     */private final boolean isAny(DataCriteria criteria) {
    /* 370 */for (Object value : this.values) {
      /* 371 */if (isInCriteriaRegion(value, criteria))
        /* 372 */return true;
      /*     */}
    /* 374 */return false;
    /*     */}

  /*     */
  /*     */private final boolean areAll(DataCriteria criteria) {
    /* 378 */for (Object value : this.values) {
      /* 379 */if (!isInCriteriaRegion(value, criteria))
        /* 380 */return false;
      /*     */}
    /* 382 */return true;
    /*     */}

  /*     */
  /*     */private final double getSum() {
    /* 386 */double sum = 0.0D;
    /* 387 */for (Object value : this.values)
      /* 388 */if (value instanceof String) {
        /* 389 */sum += getValueFrom(value.toString());
        /*     */}
      /*     */else
      /*     */{
        /* 394 */sum += ((Double) value).doubleValue();
        /*     */}
    /* 396 */return sum;
    /*     */}

  /*     */
  /*     */private final double getMax() {
    /* 400 */double max = 4.9E-324D;
    /* 401 */for (Object value : this.values) {
      /* 402 */double _val = 0.0D;
      /* 403 */if (value instanceof String) {
        /* 404 */_val = getValueFrom(value.toString());
        /*     */}
      /*     */else
      /*     */{
        /* 409 */_val = ((Double) value).doubleValue();
        /* 410 */}
      if (Double.compare(_val, max) > 0)
        /* 411 */max = _val;
      /*     */}
    /* 413 */return max;
    /*     */}

  /*     */
  /*     */private final double getMin() {
    /* 417 */double min = 1.7976931348623157E+308D;
    /* 418 */for (Object value : this.values) {
      /* 419 */double _val = 0.0D;
      /* 420 */if (value instanceof String) {
        /* 421 */_val = getValueFrom(value.toString());
        /*     */}
      /*     */else
      /*     */{
        /* 426 */_val = ((Double) value).doubleValue();
        /* 427 */}
      if (Double.compare(_val, min) < 0)
        /* 428 */min = _val;
      /*     */}
    /* 430 */return min;
    /*     */}

  /*     */
  /*     */private final double getAvg() {
    /* 434 */return getSum() / this.values.length;
    /*     */}

  /*     */
  /*     */private final Element[][] determineCoordinates(Hierarchy[] cubeHiers,
    String[][] slices)
  /*     */{
    /* 441 */for (int i = 0; i < slices.length; ++i) {
      /* 442 */String template = slices[i][0];
      /* 443 */if ((template.equals("*")) || (template.equals("-1")))
        /* 444 */slices[i] = getAllElementIds(cubeHiers[i]);
      /*     */}
    /* 446 */return cartesianProduct(cubeHiers, slices);
    /*     */}

  /*     */
  /*     */private final Element[][] cartesianProduct(Hierarchy[] cubeHiers,
    String[][] slices)
  /*     */{
    /* 466 */int coordsCount = 1;
    /*     */
    /* 468 */for (int i = 0; i < slices.length; ++i)
      coordsCount *= slices[i].length;
    /*     */
    /* 470 */Element[][] coordinates = new Element[coordsCount][];
    /* 471 */while (coordsCount-- > 0) {
      /* 472 */int i = 1;
      /* 473 */int index = 0;
      /* 474 */Element[] coordinate = new Element[slices.length];
      /* 475 */for (String[] slice : slices) {
        /* 476 */int pos = coordsCount / i % slice.length;
        /* 477 */coordinate[index] = cubeHiers[index].getElementById(slice[pos]);
        /* 478 */i *= slice.length;
        /* 479 */if ((!this.isString) &&
        /* 480 */(coordinate[index].getType() == 1)) {
          /* 481 */this.isString = true;
          /*     */}
        /*     */
        /* 484 */++index;
        /*     */}
      /*     */
      /* 487 */coordinates[coordsCount] = coordinate;
      /*     */}
    /*     */
    /* 490 */return coordinates;
    /*     */}

  /*     */
  /*     */private final boolean isInCriteriaRegion(Object val,
    DataCriteria criteria)
  /*     */{
    /* 496 */return (pass(val, criteria.getFirstOperator(),
    /* 495 */criteria.getFirstOperand().getValue())) &&
    /* 496 */(pass(val, criteria.getSecondOperator(),
    /* 497 */criteria.getSecondOperand().getValue()));
    /*     */}

  /*     */private final boolean pass(Object val, String operator, String operand) {
    /* 500 */if ((val == null) || (operand == null) || (operator == null)
      || (operand.length() == 0)) {
      /* 501 */return true;
      /* 503 */}
    if (val instanceof Double)
      ;
    /*     */String valStr;
    /*     */try {
      /* 505 */Double op = new Double(operand);
      /* 506 */Double _val = (Double) val;
      /* 507 */return compare(_val, op, operator);
      /*     */}
    /*     */catch (NumberFormatException ee)
    /*     */{
      /* 512 */valStr = val.toString();
      /* 513 */}
    return compare(valStr, operand, operator);
    /*     */}

  /*     */
  /*     */private final boolean compare(Double d1, Double d2, String operator) {
    /* 517 */int result = d1.compareTo(d2);
    /* 518 */return compare(d1, d2, operator, result);
    /*     */}

  /*     */
  /*     */private final boolean compare(String str1, String str2, String operator) {
    /* 522 */int result = str1.compareTo(str2);
    /* 523 */return compare(str1, str2, operator, result);
    /*     */}

  /*     */
  /*     */private final boolean compare(Object o1, Object o2, String operator,
    int result) {
    /* 527 */if (operator.equals(">"))
      /* 528 */return result > 0;
    /* 529 */if (operator.equals(">="))
      /* 530 */return result >= 0;
    /* 531 */if (operator.equals("<"))
      /* 532 */return result < 0;
    /* 533 */if (operator.equals("<="))
      /* 534 */return result <= 0;
    /* 535 */if (operator.equals("<>")) {
      /* 536 */return !o1.equals(o2);
      /*     */}
    /* 538 */return o1.equals(o2);
    /*     */}

  /*     */
  /*     */private final double getValueFrom(String str) {
    /* 542 */double val = 0.0D;
    /*     */try {
      /* 544 */if (str.length() > 0)
        /* 545 */val = Double.parseDouble(str);
      /*     */}
    /*     */catch (NumberFormatException localNumberFormatException) {
      /*     */}
    /* 549 */return val;
    /*     */}

  /*     */
  /*     */private final String[] getAllElementIds(Hierarchy hierarchy) {
    /* 553 */Element[] elements = hierarchy.getElements();
    /* 554 */String[] ids = new String[elements.length];
    /* 555 */for (int i = 0; i < ids.length; ++i)
      /* 556 */ids[i] = elements[i].getId();
    /* 557 */return ids;
    /*     */}
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name: org.palo.api.subsets.filter.DataCell JD-Core Version: 0.5.4
 */