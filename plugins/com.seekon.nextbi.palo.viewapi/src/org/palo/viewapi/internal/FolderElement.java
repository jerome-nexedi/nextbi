/*     */package org.palo.viewapi.internal;

/*     */
/*     */import java.util.HashMap; /*     */
import java.util.Set; /*     */
import org.palo.api.Connection; /*     */
import org.palo.api.Database; /*     */
import org.palo.api.Dimension; /*     */
import org.palo.api.Hierarchy; /*     */
import org.palo.api.PaloObject; /*     */
import org.palo.api.parameters.ParameterProvider; /*     */
import org.palo.api.parameters.ParameterReceiver; /*     */
import org.palo.api.subsets.Subset2; /*     */
import org.palo.viewapi.View;

/*     */
/*     */public class FolderElement extends AbstractExplorerTreeNode
/*     */implements ParameterProvider, ParameterReceiver
/*     */{
  /*     */public static final int FE_TYPE = 3;

  /*     */private String sourceObjectDescription;

  /*     */private final HashMap<PaloObject, String> variableMapper;

  /*     */private String[] parameterNames;

  /*     */private final HashMap<String, Object[]> possibleValues;

  /* 74 */private ParameterReceiver sourceObject = null;

  /*     */private final HashMap<String, Object> parameterValue;

  /*     */
  /*     */public static FolderElement internalCreate(ExplorerTreeNode parent,
    String id, String name)
  /*     */{
    /* 94 */return new FolderElement(parent, id, name);
    /*     */}

  /*     */
  /*     */FolderElement(ExplorerTreeNode parent, String id, String name)
  /*     */{
    /* 108 */super(parent, id, name);
    /* 109 */this.variableMapper = new HashMap();
    /* 110 */this.parameterNames = new String[0];
    /* 111 */this.possibleValues = new HashMap();
    /* 112 */this.parameterValue = new HashMap();
    /* 113 */if (parent != null)
      /* 114 */parent.addChild(this);
    /*     */}

  /*     */
  /*     */public FolderElement(ExplorerTreeNode parent)
  /*     */{
    /* 126 */super("fe_", parent);
    /* 127 */this.parameterNames = new String[0];
    /* 128 */this.possibleValues = new HashMap();
    /* 129 */this.parameterValue = new HashMap();
    /* 130 */this.variableMapper = new HashMap();
    /* 131 */if (parent != null)
      /* 132 */parent.addChild(this);
    /*     */}

  /*     */
  /*     */public FolderElement(ExplorerTreeNode parent, String name)
  /*     */{
    /* 146 */super("fe_", parent, name);
    /* 147 */this.parameterNames = new String[0];
    /* 148 */this.possibleValues = new HashMap();
    /* 149 */this.parameterValue = new HashMap();
    /* 150 */this.variableMapper = new HashMap();
    /* 151 */if (parent != null)
      /* 152 */parent.addChild(this);
    /*     */}

  /*     */
  /*     */FolderElement(String id, String name, DynamicFolder parent,
    FolderElement original)
  /*     */{
    /* 158 */super(parent, id, name);
    /* 159 */this.possibleValues = ((HashMap) original.possibleValues.clone());
    /* 160 */this.parameterValue = new HashMap();
    /* 161 */this.variableMapper = new HashMap();
    /* 162 */for (PaloObject key : original.variableMapper.keySet()) {
      /* 163 */this.variableMapper.put(key, (String) original.variableMapper
        .get(key));
      /*     */}
    /* 165 */for (String key : original.parameterValue.keySet()) {
      /* 166 */this.parameterValue.put(key, original.parameterValue.get(key));
      /*     */}
    /* 168 */this.sourceObject = original.sourceObject;
    /* 169 */this.sourceObjectDescription = original.sourceObjectDescription;
    /*     */}

  /*     */
  /*     */public String[] getParameterNames()
  /*     */{
    /* 176 */return this.parameterNames;
    /*     */}

  /*     */
  /*     */public Object[] getPossibleValuesFor(String parameterName)
  /*     */{
    /* 185 */if (!this.possibleValues.containsKey(parameterName)) {
      /* 186 */return new Object[0];
      /*     */}
    /* 188 */return (Object[]) this.possibleValues.get(parameterName);
    /*     */}

  /*     */
  /*     */public ParameterReceiver getSourceObject()
  /*     */{
    /* 195 */if (this.sourceObject != null) {
      /* 196 */for (String key : this.parameterValue.keySet()) {
        /* 197 */this.sourceObject.setParameter(key, this.parameterValue.get(key));
        /*     */}
      /*     */}
    /* 200 */return this.sourceObject;
    /*     */}

  /*     */
  /*     */public void setParameterNames(String[] parameterNames)
  /*     */{
    /* 208 */this.parameterNames = parameterNames;
    /*     */}

  /*     */
  /*     */public void setPossibleValuesFor(String parameterName,
    Object[] possibleValues)
  /*     */{
    /* 216 */this.possibleValues.put(parameterName, possibleValues);
    /*     */}

  /*     */
  /*     */public void setSourceObject(ParameterReceiver sourceObject)
  /*     */{
    /* 223 */this.sourceObject = sourceObject;
    /*     */}

  /*     */
  /*     */public Object getDefaultValue(String parameterName)
  /*     */{
    /* 230 */if (this.sourceObject == null) {
      /* 231 */return null;
      /*     */}
    /* 233 */return this.sourceObject.getDefaultValue(parameterName);
    /*     */}

  /*     */
  /*     */public Object getParameterValue(String parameterName)
  /*     */{
    /* 240 */return this.parameterValue.get(parameterName);
    /*     */}

  /*     */
  /*     */public boolean isParameterized()
  /*     */{
    /* 248 */if (this.sourceObject == null) {
      /* 249 */return false;
      /*     */}
    /* 251 */return this.sourceObject.isParameterized();
    /*     */}

  /*     */
  /*     */public void setParameter(String parameterName, Object parameterValue)
  /*     */{
    /* 258 */this.parameterValue.put(parameterName, parameterValue);
    /*     */}

  /*     */
  /*     */private final String encodeSubset2(Subset2 source)
  /*     */{
    /* 263 */StringBuffer result = new StringBuffer("subset:");
    /*     */
    /* 265 */Connection con = source.getDimHierarchy().getDimension()
    /* 266 */.getDatabase().getConnection();
    /* 267 */result.append(con.getServer());
    /* 268 */result.append(":");
    /* 269 */result.append(con.getService());
    /* 270 */result.append(":");
    /* 271 */result.append(source.getDimHierarchy().getDimension().getDatabase()
      .getId());
    /* 272 */result.append(":");
    /* 273 */result.append(source.getDimHierarchy().getDimension().getId());
    /* 274 */result.append(":");
    /* 275 */result.append(source.getDimHierarchy().getId());
    /* 276 */result.append(":");
    /* 277 */result.append(source.getId());
    /* 278 */return result.toString();
    /*     */}

  /*     */
  /*     */private final String encodeHierarchy(Hierarchy source) {
    /* 282 */StringBuffer result = new StringBuffer("hierarchy:");
    /*     */
    /* 284 */Connection con = source.getDimension().getDatabase().getConnection();
    /* 285 */result.append(con.getServer());
    /* 286 */result.append(":");
    /* 287 */result.append(con.getService());
    /* 288 */result.append(":");
    /* 289 */result.append(source.getDimension().getDatabase().getId());
    /* 290 */result.append(":");
    /* 291 */result.append(source.getDimension().getId());
    /* 292 */result.append(":");
    /* 293 */result.append(source.getId());
    /* 294 */return result.toString();
    /*     */}

  /*     */
  /*     */public String getPersistenceString()
  /*     */{
    /* 301 */StringBuffer result = new StringBuffer("<folderElement ");
    /* 302 */if (getId() != null) {
      /* 303 */result.append("id=\"");
      /* 304 */result.append(getId());
      /* 305 */result.append("\" ");
      /*     */}
    /* 307 */if (this.name != null) {
      /* 308 */result.append("name=\"");
      /* 309 */result.append(modify(this.name));
      /* 310 */result.append("\" ");
      /*     */}
    /* 312 */if ((this.sourceObject != null) && (this.sourceObject instanceof View)) {
      /* 313 */result.append("source=\"");
      /* 314 */View v = (View) this.sourceObject;
      /* 315 */result.append(StaticFolder.makePersistenceString(v));
      /* 316 */result.append("\" ");
      /*     */}
    /* 324 */else if (this.sourceObjectDescription != null) {
      /* 325 */if (this.sourceObjectDescription.startsWith("source"))
        /* 326 */result.append("source=\""
          + this.sourceObjectDescription.substring(6) + "\" ");
      /*     */else {
        /* 328 */result.append("book=\""
          + this.sourceObjectDescription.substring(4) + "\" ");
        /*     */}
      /*     */}
    /* 331 */if (this.variableMapper.size() > 0) {
      /* 332 */result.append("mappings=\"");
      /* 333 */for (PaloObject key : this.variableMapper.keySet()) {
        /* 334 */StringBuffer text = new StringBuffer();
        /* 335 */if (key instanceof Subset2) {
          /* 336 */text.append(encodeSubset2((Subset2) key));
        } else {
          /* 337 */if (!(key instanceof Hierarchy))
            continue;
          /* 338 */text.append(encodeHierarchy((Hierarchy) key));
          /*     */}
        /*     */
        /* 342 */result.append(text + ", ");
        /* 343 */result.append((String) this.variableMapper.get(key) + ", ");
        /*     */}
      /* 345 */result.append("\" ");
      /*     */}
    /* 347 */result.append(">\n");
    /* 348 */result.append("</folderElement>\n");
    /* 349 */return result.toString();
    /*     */}

  /*     */
  /*     */public int getType() {
    /* 353 */return 3;
    /*     */}

  /*     */
  /*     */public void setSourceObjectDescription(String text) {
    /* 357 */this.sourceObjectDescription = text;
    /*     */}

  /*     */
  /*     */public String getSourceObjectDescription() {
    /* 361 */return this.sourceObjectDescription;
    /*     */}

  /*     */
  /*     */public PaloObject[] getVariableMappingKeys() {
    /* 365 */return (PaloObject[]) this.variableMapper.keySet().toArray(
      new PaloObject[0]);
    /*     */}

  /*     */
  /*     */public String getVariableMapping(Hierarchy key) {
    /* 369 */return (String) this.variableMapper.get(key);
    /*     */}

  /*     */
  /*     */public String getVariableMapping(Subset2 key) {
    /* 373 */return (String) this.variableMapper.get(key);
    /*     */}

  /*     */
  /*     */public void setVariableMapping(Subset2 subset, String value) {
    /* 377 */this.variableMapper.put(subset, value);
    /*     */}

  /*     */
  /*     */public void setVariableMapping(Hierarchy hierarchy, String value) {
    /* 381 */this.variableMapper.put(hierarchy, value);
    /*     */}
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi
 * .jar Qualified Name: org.palo.viewapi.internal.FolderElement JD-Core Version:
 * 0.5.4
 */