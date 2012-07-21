/*     */package com.tensegrity.palojava.http.handlers;

/*     */
/*     */import com.tensegrity.palojava.CubeInfo; /*     */
import com.tensegrity.palojava.DatabaseInfo; /*     */
import com.tensegrity.palojava.RuleInfo; /*     */
import com.tensegrity.palojava.http.HttpConnection; /*     */
import com.tensegrity.palojava.http.builders.InfoBuilderRegistry; /*     */
import com.tensegrity.palojava.http.builders.RuleInfoBuilder; /*     */
import com.tensegrity.palojava.impl.RuleImpl; /*     */
import java.io.IOException;

/*     */
/*     */public class RuleHandler extends HttpHandler
/*     */{
  /*     */private static final String CREATE_PREFIX = "/rule/create?database=";

  /*     */private static final String DELETE_PREFIX = "/rule/destroy?database=";

  /*     */private static final String PARSE_RULE_PREFIX = "/rule/parse?database=";

  /*     */private static final String LIST_FUNCTIONS_PREFIX = "/rule/functions?";

  /*     */private static final String MODIFY_PREFIX = "/rule/modify?database=";

  /*     */private static final String INFO_PREFIX = "/rule/info?database=";

  /*     */private static final String RULE_PREFIX = "&rule=";

  /*     */private static final String DEFINITION_PREFIX = "&definition=";

  /*     */private static final String FUNCTIONS_PREFIX = "&functions=";

  /* 73 */private static final RuleHandler instance = new RuleHandler();

  /*     */private final InfoBuilderRegistry builderReg;

  /*     */
  /*     */static final RuleHandler getInstance(HttpConnection connection)
  /*     */{
    /* 75 */instance.use(connection);
    /* 76 */return instance;
    /*     */}

  /*     */
  /*     */private RuleHandler()
  /*     */{
    /* 84 */this.builderReg = InfoBuilderRegistry.getInstance();
    /*     */}

  /*     */
  /*     */public final String parse(CubeInfo cube, String ruleDefinition,
    String functions)
  /*     */throws IOException
  /*     */{
    /* 91 */DatabaseInfo database = cube.getDatabase();
    /* 92 */StringBuffer query = new StringBuffer();
    /* 93 */query.append("/rule/parse?database=");
    /* 94 */query.append(database.getId());
    /* 95 */if ((functions == null) || (functions.length() == 0)) {
      /* 96 */query.append("&cube=");
      /* 97 */query.append(cube.getId());
      /*     */}
    /* 99 */query.append("&definition=");
    /* 100 */query.append(encode(ruleDefinition));
    /* 101 */if (functions != null) {
      /* 102 */query.append("&functions=");
      /* 103 */query.append(encode(functions));
      /*     */}
    /* 105 */String[][] response = request(query.toString());
    /* 106 */return response[0][0];
    /*     */}

  /*     */
  /*     */public final String listFunctions() throws IOException {
    /* 110 */String[][] response = request("/rule/functions?");
    /* 111 */StringBuffer functionStr = new StringBuffer();
    /* 112 */for (int i = 0; i < response.length; ++i)
      /* 113 */for (int j = 0; j < response[i].length; ++j)
        /* 114 */functionStr.append(response[i][j]);
    /* 115 */return functionStr.toString();
    /*     */}

  /*     */
  /*     */public final RuleInfo create(CubeInfo cube, String definition)
    throws IOException
  /*     */{
    /* 120 */return create(cube, definition, null, false, null);
    /*     */}

  /*     */
  /*     */public final RuleInfo create(CubeInfo cube, String definition,
    String externalIdentifier, boolean useIt, String comment)
  /*     */throws IOException
  /*     */{
    /* 126 */return create(cube, definition, externalIdentifier, useIt,
    /* 127 */comment, true);
    /*     */}

  /*     */
  /*     */public final RuleInfo create(CubeInfo cube, String definition,
    String externalIdentifier, boolean useIt, String comment, boolean activate)
  /*     */throws IOException
  /*     */{
    /* 133 */DatabaseInfo database = cube.getDatabase();
    /* 134 */StringBuffer query = new StringBuffer();
    /* 135 */query.append("/rule/create?database=");
    /* 136 */query.append(database.getId());
    /* 137 */query.append("&cube=");
    /* 138 */query.append(cube.getId());
    /* 139 */addRuleParameter(query, definition, externalIdentifier, useIt,
      comment,
      /* 140 */activate);
    /* 141 */String[][] response = request(query.toString());
    /* 142 */RuleInfoBuilder ruleBuilder = this.builderReg.getRuleBuilder();
    /* 143 */return ruleBuilder.create(cube, response[0]);
    /*     */}

  /*     */
  /*     */public final boolean delete(RuleInfo rule) throws IOException {
    /* 147 */CubeInfo cube = rule.getCube();
    /* 148 */DatabaseInfo database = cube.getDatabase();
    /* 149 */StringBuffer query = new StringBuffer();
    /* 150 */query.append("/rule/destroy?database=");
    /* 151 */query.append(database.getId());
    /* 152 */query.append("&cube=");
    /* 153 */query.append(cube.getId());
    /* 154 */query.append("&rule=");
    /* 155 */query.append(rule.getId());
    /* 156 */String[][] response = request(query.toString());
    /* 157 */return response[0][0].equals("1");
    /*     */}

  /*     */
  /*     */public final boolean delete(String ruleId, CubeInfo cube)
    throws IOException {
    /* 161 */DatabaseInfo database = cube.getDatabase();
    /* 162 */StringBuffer query = new StringBuffer();
    /* 163 */query.append("/rule/destroy?database=");
    /* 164 */query.append(database.getId());
    /* 165 */query.append("&cube=");
    /* 166 */query.append(cube.getId());
    /* 167 */query.append("&rule=");
    /* 168 */query.append(ruleId);
    /* 169 */String[][] response = request(query.toString());
    /* 170 */return response[0][0].equals("1");
    /*     */}

  /*     */
  /*     */public final RuleInfo getInfo(CubeInfo cube, String id)
    throws IOException {
    /* 174 */DatabaseInfo database = cube.getDatabase();
    /* 175 */StringBuffer query = new StringBuffer();
    /* 176 */query.append("/rule/info?database=");
    /* 177 */query.append(database.getId());
    /* 178 */query.append("&cube=");
    /* 179 */query.append(cube.getId());
    /* 180 */query.append("&rule=");
    /* 181 */query.append(id);
    /* 182 */if (id.trim().length() == 0) {
      /* 183 */return null;
      /*     */}
    /* 185 */String[][] response = request(query.toString());
    /* 186 */RuleInfoBuilder ruleBuilder = this.builderReg.getRuleBuilder();
    /* 187 */return ruleBuilder.create(cube, response[0]);
    /*     */}

  /*     */
  /*     */public final void update(RuleInfo rule, String definition,
    String externalIdentifier, boolean useIt, String comment, boolean activate)
  /*     */throws IOException
  /*     */{
    /* 193 */CubeInfo cube = rule.getCube();
    /* 194 */DatabaseInfo database = cube.getDatabase();
    /* 195 */StringBuffer query = new StringBuffer();
    /* 196 */query.append("/rule/modify?database=");
    /* 197 */query.append(database.getId());
    /* 198 */query.append("&cube=");
    /* 199 */query.append(cube.getId());
    /* 200 */query.append("&rule=");
    /* 201 */query.append(rule.getId());
    /* 202 */addRuleParameter(query, definition, externalIdentifier, useIt,
      comment,
      /* 203 */activate);
    /* 204 */String[][] response = request(query.toString());
    /* 205 */RuleInfoBuilder ruleBuilder = this.builderReg.getRuleBuilder();
    /* 206 */ruleBuilder.update((RuleImpl) rule, response[0]);
    /*     */}

  /*     */
  /*     */private final void addRuleParameter(StringBuffer query,
    String definition, String externalIdentifier, boolean useIt, String comment,
    boolean activate)
  /*     */{
    /* 212 */if ((definition != null) && (definition.length() > 0)) {
      /* 213 */query.append("&definition=");
      /* 214 */query.append(encode(definition));
      /*     */}
    /*     */
    /* 217 */query.append("&activate=");
    /* 218 */query.append((activate) ? "1" : "0");
    /*     */
    /* 226 */if ((externalIdentifier != null) && (externalIdentifier.length() > 0)) {
      /* 227 */query.append("&external_identifier=");
      /* 228 */query.append(encode(externalIdentifier));
      /*     */}
    /* 230 */if ((comment != null) && (comment.length() > 0)) {
      /* 231 */query.append("&comment=");
      /* 232 */query.append(encode(comment));
      /*     */}
    /* 234 */query.append("&use_identifier=");
    /* 235 */query.append((useIt) ? "1" : "0");
    /*     */}

  /*     */
  /*     */private final String getFunctions(String definition)
  /*     */{
    /* 244 */return null;
    /*     */}
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.paloapi\lib\palo.jar
 * Qualified Name: com.tensegrity.palojava.http.handlers.RuleHandler JD-Core
 * Version: 0.5.4
 */