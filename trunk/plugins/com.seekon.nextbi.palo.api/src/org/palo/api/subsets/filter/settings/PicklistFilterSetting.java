/*     */package org.palo.api.subsets.filter.settings;

/*     */
/*     */import java.util.HashSet; /*     */
import java.util.LinkedHashSet; /*     */
import org.palo.api.subsets.Subset2;

/*     */
/*     */public class PicklistFilterSetting extends AbstractFilterSettings
/*     */{
  /*     */public static final int INSERT_MODE_SUB = 3;

  /*     */public static final int INSERT_MODE_BACK = 1;

  /*     */public static final int INSERT_MODE_FRONT = 0;

  /*     */public static final int INSERT_MODE_MERGE = 2;

  /*     */private IntegerParameter insertMode;

  /*     */private ObjectParameter selection;

  /*     */
  /*     */public PicklistFilterSetting()
  /*     */{
    /* 66 */this.insertMode = new IntegerParameter(null);
    /* 67 */this.insertMode.setValue(0);
    /* 68 */this.selection = new ObjectParameter();
    /* 69 */this.selection.setValue(new LinkedHashSet());
    /*     */}

  /*     */
  /*     */public final void addElement(String id)
  /*     */{
    /* 77 */LinkedHashSet elements = (LinkedHashSet) this.selection.getValue();
    /* 78 */elements.add(id);
    /* 79 */markDirty();
    /*     */}

  /*     */
  /*     */public final void removeElement(String id)
  /*     */{
    /* 87 */LinkedHashSet elements = (LinkedHashSet) this.selection.getValue();
    /* 88 */elements.remove(id);
    /* 89 */markDirty();
    /*     */}

  /*     */
  /*     */public final void removeAllElements()
  /*     */{
    /* 95 */LinkedHashSet elements = (LinkedHashSet) this.selection.getValue();
    /* 96 */elements.clear();
    /* 97 */markDirty();
    /*     */}

  /*     */
  /*     */public final ObjectParameter getSelection()
  /*     */{
    /* 105 */return this.selection;
    /*     */}

  /*     */
  /*     */public final void setSelection(ObjectParameter selection)
  /*     */{
    /* 117 */Object value = selection.getValue();
    /* 118 */if (value instanceof HashSet)
      /* 119 */copySelection((HashSet) value);
    /* 120 */getSelection().bind(this.subset);
    /*     */}

  /*     */
  /*     */public IntegerParameter getInsertMode()
  /*     */{
    /* 128 */return this.insertMode;
    /*     */}

  /*     */
  /*     */public final void setInsertMode(int insertMode)
  /*     */{
    /* 136 */this.insertMode.setValue(insertMode);
    /*     */}

  /*     */
  /*     */public final void setInsertMode(IntegerParameter insertMode)
  /*     */{
    /* 145 */this.insertMode = insertMode;
    /* 146 */this.insertMode.bind(this.subset);
    /*     */}

  /*     */
  /*     */public final void reset() {
    /* 150 */removeAllElements();
    /* 151 */this.insertMode.setValue(2);
    /*     */}

  /*     */
  /*     */public final void bind(Subset2 subset) {
    /* 155 */super.bind(subset);
    /*     */
    /* 157 */this.insertMode.bind(subset);
    /* 158 */this.selection.bind(subset);
    /*     */}

  /*     */public final void unbind() {
    /* 161 */super.unbind();
    /*     */
    /* 163 */this.insertMode.unbind();
    /* 164 */this.selection.unbind();
    /*     */}

  /*     */
  /*     */public final void adapt(FilterSetting from) {
    /* 168 */if (!(from instanceof PicklistFilterSetting))
      /* 169 */return;
    /* 170 */PicklistFilterSetting setting = (PicklistFilterSetting) from;
    /* 171 */reset();
    /*     */
    /* 173 */setInsertMode(setting.getInsertMode().getValue().intValue());
    /* 174 */HashSet newSelection =
    /* 175 */(HashSet) setting.getSelection().getValue();
    /* 176 */copySelection(newSelection);
    /*     */}

  /*     */
  /*     */private final void copySelection(HashSet<String> newSelection)
  /*     */{
    /* 181 */HashSet _selection = (HashSet) this.selection.getValue();
    /* 182 */_selection.clear();
    /* 183 */_selection.addAll(newSelection);
    /* 184 */markDirty();
    /*     */}
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name: org.palo.api.subsets.filter.settings.PicklistFilterSetting
 * JD-Core Version: 0.5.4
 */