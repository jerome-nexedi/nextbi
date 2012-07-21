/*     */package org.palo.api.subsets.filter.settings;

/*     */
/*     */import org.palo.api.subsets.Subset2;

/*     */
/*     */public class AttributeFilterSetting extends AbstractFilterSettings
/*     */{
  /*     */private ObjectParameter constraintsParam;

  /*     */
  /*     */public AttributeFilterSetting()
  /*     */{
    /* 62 */this.constraintsParam = new ObjectParameter();
    /* 63 */this.constraintsParam.setValue(new AttributeConstraintsMatrix());
    /*     */}

  /*     */
  /*     */public final void setFilterConstraints(ObjectParameter constraintsParam)
  /*     */{
    /* 74 */Object value = constraintsParam.getValue();
    /* 75 */if (value instanceof AttributeConstraintsMatrix)
      /* 76 */copyFilterConstraints((AttributeConstraintsMatrix) value);
    /*     */}

  /*     */
  /*     */public final ObjectParameter getFilterConstraints()
  /*     */{
    /* 84 */return this.constraintsParam;
    /*     */}

  /*     */
  /*     */public final boolean hasFilterConsraints() {
    /* 88 */AttributeConstraintsMatrix filterMatrix =
    /* 89 */(AttributeConstraintsMatrix) this.constraintsParam.getValue();
    /* 90 */return filterMatrix.hasConstraints();
    /*     */}

  /*     */
  /*     */public void adapt(FilterSetting from) {
    /* 94 */if (!(from instanceof AttributeFilterSetting))
      /* 95 */return;
    /* 96 */AttributeFilterSetting setting = (AttributeFilterSetting) from;
    /* 97 */copyFilterConstraints((AttributeConstraintsMatrix)
    /* 98 */setting.getFilterConstraints().getValue());
    /*     */}

  /*     */
  /*     */public final void bind(Subset2 subset) {
    /* 102 */super.bind(subset);
    /*     */
    /* 104 */this.constraintsParam.bind(subset);
    /* 105 */AttributeConstraintsMatrix filterMatrix =
    /* 106 */(AttributeConstraintsMatrix) this.constraintsParam.getValue();
    /* 107 */if (filterMatrix != null)
      /* 108 */filterMatrix.bind(subset);
    /*     */}

  /*     */
  /*     */public final void unbind() {
    /* 111 */super.unbind();
    /*     */
    /* 113 */this.constraintsParam.unbind();
    /* 114 */AttributeConstraintsMatrix filterMatrix =
    /* 115 */(AttributeConstraintsMatrix) this.constraintsParam.getValue();
    /* 116 */if (filterMatrix != null)
      /* 117 */filterMatrix.unbind();
    /*     */}

  /*     */
  /*     */public void reset() {
    /* 121 */AttributeConstraintsMatrix filterMatrix =
    /* 122 */(AttributeConstraintsMatrix) this.constraintsParam.getValue();
    /* 123 */filterMatrix.clear();
    /*     */}

  /*     */
  /*     */private final void copyFilterConstraints(
    AttributeConstraintsMatrix newConstraintMatrix)
  /*     */{
    /* 128 */AttributeConstraintsMatrix filterMatrix =
    /* 129 */(AttributeConstraintsMatrix) this.constraintsParam.getValue();
    /* 130 */filterMatrix.clear();
    /*     */AttributeConstraint[] arrayOfAttributeConstraint;
    /* 133 */int j = (arrayOfAttributeConstraint = newConstraintMatrix
    /* 133 */.getConstraints()).length;
    int i = 0;
    /*     */
    /* 132 */for (; i < j; ++i) {
      /* 133 */AttributeConstraint constraint = arrayOfAttributeConstraint[i];
      /* 134 */constraint.bind(this.subset);
      /* 135 */filterMatrix.addFilterConstraint(constraint);
      /*     */}
    /*     */}
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name: org.palo.api.subsets.filter.settings.AttributeFilterSetting
 * JD-Core Version: 0.5.4
 */