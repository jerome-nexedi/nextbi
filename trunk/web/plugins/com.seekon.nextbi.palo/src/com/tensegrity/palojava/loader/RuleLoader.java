/*     */package com.tensegrity.palojava.loader;

/*     */
/*     */import com.tensegrity.palojava.CubeInfo; /*     */
import com.tensegrity.palojava.DbConnection; /*     */
import com.tensegrity.palojava.ElementInfo; /*     */
import com.tensegrity.palojava.PaloException; /*     */
import com.tensegrity.palojava.PaloInfo; /*     */
import com.tensegrity.palojava.RuleInfo; /*     */
import com.tensegrity.palojava.ServerInfo; /*     */
import java.util.Map;

/*     */
/*     */public abstract class RuleLoader extends PaloInfoLoader
/*     */{
  /*     */private static final int MIN_RULES_MAJOR = 1;

  /*     */private static final int MIN_RULES_MINOR = 5;

  /*     */private static final int MIN_RULES_BUILD = 1646;

  /*     */protected final CubeInfo cube;

  /*     */
  /*     */public RuleLoader(DbConnection paloConnection, CubeInfo cube)
  /*     */{
    /* 68 */super(paloConnection);
    /* 69 */this.cube = cube;
    /*     */}

  /*     */
  /*     */public abstract String[] getAllRuleIds();

  /*     */
  /*     */public final RuleInfo create(String definition)
  /*     */{
    /* 84 */return create(definition, null, false, null, true);
    /*     */}

  /*     */
  /*     */public final RuleInfo create(String definition, String externalId,
    boolean useIt, String comment, boolean activate)
  /*     */{
    /* 100 */checkRuleSupport();
    /* 101 */RuleInfo rule = this.paloConnection.createRule(this.cube, definition,
      externalId,
      /* 102 */useIt, comment, activate);
    /* 103 */loaded(rule);
    /* 104 */return rule;
    /*     */}

  /*     */
  /*     */public final boolean delete(RuleInfo rule)
  /*     */{
    /* 115 */if (this.paloConnection.delete(rule)) {
      /* 116 */removed(rule);
      /* 117 */return true;
      /*     */}
    /* 119 */return false;
    /*     */}

  /*     */
  /*     */public final boolean delete(String ruleId)
  /*     */{
    /* 132 */if (this.paloConnection.delete(ruleId, this.cube))
    /*     */{
      /* 134 */this.loadedInfo.remove(ruleId);
      /* 135 */return true;
      /*     */}
    /* 137 */return false;
    /*     */}

  /*     */
  /*     */public final RuleInfo load(String id)
  /*     */{
    /* 148 */PaloInfo rule = (PaloInfo) this.loadedInfo.get(id);
    /* 149 */if (rule == null) {
      /* 150 */rule = this.paloConnection.getRule(this.cube, id);
      /* 151 */loaded(rule);
      /*     */}
    /* 153 */return (RuleInfo) rule;
    /*     */}

  /*     */
  /*     */public final RuleInfo load(ElementInfo[] coordinate)
  /*     */{
    /* 165 */String id = this.paloConnection.getRule(this.cube, coordinate);
    /* 166 */if ((id != null) && (id.trim().length() > 0)) {
      /* 167 */return load(id);
      /*     */}
    /*     */
    /* 171 */return null;
    /*     */}

  /*     */
  /*     */public static boolean supportsRules(DbConnection paloConnection)
  /*     */{
    /* 181 */ServerInfo server = paloConnection.getServerInfo();
    /* 182 */if (server.getMajor() < 1)
      /* 183 */return false;
    /* 184 */if (server.getMajor() == 1) {
      /* 185 */if (server.getMinor() < 5)
        /* 186 */return false;
      /* 187 */if ((server.getMinor() == 5) &&
      /* 188 */(server.getBuildNumber() <= 1646)) {
        /* 189 */return false;
        /*     */}
      /*     */}
    /*     */
    /* 193 */return true;
    /*     */}

  /*     */
  /*     */private final void checkRuleSupport() {
    /* 197 */if (!supportsRules(this.paloConnection)) {
      /* 198 */ServerInfo srvInfo = this.paloConnection.getServerInfo();
      /* 199 */int major = srvInfo.getMajor();
      /* 200 */int minor = srvInfo.getMinor();
      /* 201 */int build = srvInfo.getBuildNumber();
      /* 202 */String srvVersion = major + "." + minor + " (" + build + ")";
      /* 203 */throw new PaloException("Palo Server " + srvVersion +
      /* 204 */" does not support rules!");
      /*     */}
    /*     */}
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.paloapi\lib\palo.jar
 * Qualified Name: com.tensegrity.palojava.loader.RuleLoader JD-Core Version:
 * 0.5.4
 */