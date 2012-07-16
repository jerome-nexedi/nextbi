/*     */package org.palo.api.subsets.filter.settings;

/*     */
/*     */import org.palo.api.subsets.Subset2;

/*     */
/*     */public class AliasFilterSetting extends AbstractFilterSettings
/*     */{
  /*     */private StringParameter alias1;

  /*     */private StringParameter alias2;

  /*     */
  /*     */public AliasFilterSetting()
  /*     */{
    /* 56 */reset();
    /*     */}

  /*     */
  /*     */public final StringParameter getAlias(int number)
  /*     */{
    /* 67 */switch (number)
    /*     */{
    /*     */case 1:
      /* 68 */
      return this.alias1;
      /*     */case 2:
      /* 69 */
      return this.alias2;
      /*     */
    }
    /* 71 */return null;
    /*     */}

  /*     */
  /*     */public final void setAlias(int number, String id)
  /*     */{
    /* 80 */switch (number)
    /*     */{
    /*     */case 1:
      /* 81 */
      this.alias1.setValue(id);
      break;
    /*     */case 2:
      /* 82 */
      this.alias2.setValue(id);
      /*     */
    }
    /*     */}

  /*     */
  /*     */public final void setAlias(int number, StringParameter alias) {
    /* 87 */switch (number)
    /*     */{
    /*     */case 1:
      /* 88 */
      this.alias1 = alias;
      this.alias1.bind(this.subset);
      break;
    /*     */case 2:
      /* 89 */
      this.alias2 = alias;
      this.alias2.bind(this.subset);
      /*     */
    }
    /*     */}

  /*     */
  /*     */public final void reset() {
    /* 94 */this.alias1 = new StringParameter();
    /* 95 */this.alias2 = new StringParameter();
    /* 96 */bind(this.subset);
    /*     */}

  /*     */
  /*     */public final void bind(Subset2 subset) {
    /* 100 */super.bind(subset);
    /*     */
    /* 102 */this.alias1.bind(subset);
    /* 103 */this.alias2.bind(subset);
    /*     */}

  /*     */public final void unbind() {
    /* 106 */super.unbind();
    /*     */
    /* 108 */this.alias1.unbind();
    /* 109 */this.alias2.unbind();
    /*     */}

  /*     */
  /*     */public final void adapt(FilterSetting from) {
    /* 113 */if (!(from instanceof AliasFilterSetting))
      /* 114 */return;
    /* 115 */AliasFilterSetting setting = (AliasFilterSetting) from;
    /* 116 */StringParameter fromAlias = setting.alias1;
    /* 117 */this.alias1 = new StringParameter(fromAlias.getName());
    /* 118 */this.alias1.setValue(fromAlias.getValue());
    /*     */
    /* 120 */fromAlias = setting.alias2;
    /* 121 */this.alias2 = new StringParameter(fromAlias.getName());
    /* 122 */this.alias2.setValue(fromAlias.getValue());
    /*     */}
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name: org.palo.api.subsets.filter.settings.AliasFilterSetting
 * JD-Core Version: 0.5.4
 */