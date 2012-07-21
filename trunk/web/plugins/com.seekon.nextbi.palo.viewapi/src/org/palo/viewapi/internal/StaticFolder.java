/*     */package org.palo.viewapi.internal;

/*     */
/*     */import java.util.ArrayList; /*     */
import java.util.HashMap; /*     */
import org.palo.api.parameters.ParameterProvider; /*     */
import org.palo.api.parameters.ParameterReceiver; /*     */
import org.palo.viewapi.View;

/*     */
/*     */public class StaticFolder extends AbstractExplorerTreeNode
/*     */implements ParameterProvider, ParameterReceiver
/*     */{
  /*     */public static final int SF_TYPE = 2;

  /*     */private String[] parameterNames;

  /*     */private final HashMap<String, Object> parameterValue;

  /*     */private final HashMap<String, Object[]> possibleValues;

  /*     */
  /*     */public static StaticFolder internalCreate(ExplorerTreeNode parent,
    String id, String name)
  /*     */{
    /* 92 */return new StaticFolder(parent, id, name);
    /*     */}

  /*     */
  /*     */StaticFolder(ExplorerTreeNode parent, String id, String name)
  /*     */{
    /* 104 */super(parent, id, name);
    /* 105 */if (parent != null) {
      /* 106 */parent.addChild(this);
      /*     */}
    /* 108 */this.parameterNames = new String[0];
    /* 109 */this.possibleValues = new HashMap();
    /* 110 */this.parameterValue = new HashMap();
    /*     */}

  /*     */
  /*     */public StaticFolder(ExplorerTreeNode parent)
  /*     */{
    /* 120 */super("sf_", parent);
    /* 121 */if (parent != null) {
      /* 122 */parent.addChild(this);
      /*     */}
    /* 124 */this.parameterNames = new String[0];
    /* 125 */this.possibleValues = new HashMap();
    /* 126 */this.parameterValue = new HashMap();
    /*     */}

  /*     */
  /*     */public StaticFolder(ExplorerTreeNode parent, String name)
  /*     */{
    /* 137 */super("sf_", parent, name);
    /* 138 */if (parent != null) {
      /* 139 */parent.addChild(this);
      /*     */}
    /* 141 */this.parameterNames = new String[0];
    /* 142 */this.possibleValues = new HashMap();
    /* 143 */this.parameterValue = new HashMap();
    /*     */}

  /*     */
  /*     */StaticFolder(String id, String name, DynamicFolder parent,
    StaticFolder original) {
    /* 147 */super(parent, id, name);
    /* 148 */this.possibleValues = ((HashMap) original.possibleValues.clone());
    /* 149 */this.children = new ArrayList();
    /* 150 */for (ExplorerTreeNode kid : original.children) {
      /* 151 */if (kid instanceof FolderElement)
        /* 152 */this.children.add(new FolderElement(kid.getId(), kid.getName(),
          parent, (FolderElement) kid));
      /*     */else {
        /* 154 */this.children.add(new DynamicFolder(kid.getId(), kid.getName(),
          parent, (DynamicFolder) kid));
        /*     */}
      /*     */}
    /*     */
    /* 158 */this.parameterValue = new HashMap();
    /* 159 */for (String key : original.parameterValue.keySet()) {
      /* 160 */Object value = original.parameterValue.get(key);
      /* 161 */this.parameterValue.put(key, value);
      /* 162 */for (ExplorerTreeNode kid : this.children)
        /* 163 */kid.setParameter(key, value);
      /*     */}
    /*     */}

  /*     */
  /*     */public String[] getParameterNames()
  /*     */{
    /* 173 */return this.parameterNames;
    /*     */}

  /*     */
  /*     */public Object[] getPossibleValuesFor(String parameterName)
  /*     */{
    /* 182 */if (!this.possibleValues.containsKey(parameterName)) {
      /* 183 */return new Object[0];
      /*     */}
    /* 185 */return (Object[]) this.possibleValues.get(parameterName);
    /*     */}

  /*     */
  /*     */public ParameterReceiver getSourceObject()
  /*     */{
    /* 192 */return null;
    /*     */}

  /*     */
  /*     */public void setParameterNames(String[] parameterNames)
  /*     */{
    /* 200 */this.parameterNames = parameterNames;
    /*     */}

  /*     */
  /*     */public void setPossibleValuesFor(String parameterName,
    Object[] possibleValues)
  /*     */{
    /* 208 */this.possibleValues.put(parameterName, possibleValues);
    /*     */}

  /*     */
  /*     */public void setSourceObject(ParameterReceiver sourceObject)
  /*     */{
    /*     */}

  /*     */
  /*     */public Object getDefaultValue(String parameterName)
  /*     */{
    /* 228 */return null;
    /*     */}

  /*     */
  /*     */public Object getParameterValue(String parameterName)
  /*     */{
    /* 235 */return this.parameterValue.get(parameterName);
    /*     */}

  /*     */
  /*     */public boolean isParameterized()
  /*     */{
    /* 246 */return true;
    /*     */}

  /*     */
  /*     */public void setParameter(String parameterName, Object parameterValue)
  /*     */{
    /* 253 */this.parameterValue.put(parameterName, parameterValue);
    /* 254 */if (this.children.isEmpty()) {
      /* 255 */return;
      /*     */}
    /* 257 */for (ExplorerTreeNode node : this.children)
      /* 258 */node.setParameter(parameterName, parameterValue);
    /*     */}

  /*     */
  /*     */static final String makePersistenceString(View v)
  /*     */{
    /* 269 */String viewId = v.getId();
    /* 270 */return viewId;
    /*     */}

  /*     */
  /*     */public String getPersistenceString()
  /*     */{
    /* 277 */StringBuffer result = new StringBuffer("<staticFolder ");
    /* 278 */if (getId() != null) {
      /* 279 */result.append("id=\"");
      /* 280 */result.append(getId());
      /* 281 */result.append("\" ");
      /*     */}
    /* 283 */if (this.name != null) {
      /* 284 */result.append("name=\"");
      /* 285 */result.append(modify(this.name));
      /* 286 */result.append("\" ");
      /*     */}
    /*     */
    /* 294 */result.append(">\n");
    /* 295 */for (ExplorerTreeNode kid : getChildren()) {
      /* 296 */result.append(kid.getPersistenceString());
      /*     */}
    /* 298 */result.append("</staticFolder>\n");
    /* 299 */return result.toString();
    /*     */}

  /*     */
  /*     */public int getType() {
    /* 303 */return 2;
    /*     */}
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi
 * .jar Qualified Name: org.palo.viewapi.internal.StaticFolder JD-Core Version:
 * 0.5.4
 */