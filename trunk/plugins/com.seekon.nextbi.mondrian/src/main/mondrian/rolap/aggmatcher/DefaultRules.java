/*
// $Id: //open/mondrian/src/main/mondrian/rolap/aggmatcher/DefaultRules.java#18 $
// This software is subject to the terms of the Eclipse Public License v1.0
// Agreement, available at the following URL:
// http://www.eclipse.org/legal/epl-v10.html.
// Copyright (C) 2005-2009 Julian Hyde and others
// All Rights Reserved.
// You must accept the terms of that agreement to use this software.
 */

package mondrian.rolap.aggmatcher;

import mondrian.olap.*;
import mondrian.rolap.RolapStar;
import mondrian.recorder.*;
import mondrian.resource.MondrianResource;

import org.apache.log4j.Logger;
import org.eigenbase.xom.*;
import org.eigenbase.xom.Parser;
import org.eigenbase.util.property.*;
import org.eigenbase.util.property.Property;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Container for the default aggregate recognition rules. It is generated by
 * parsing the default rule xml information found in the
 * {@link MondrianProperties#AggregateRules} value which normally is a resource
 * in the jar file (but can be a url).
 * 
 * <p>
 * It is a singleton since it is used to recognize tables independent of
 * database connection (each {@link mondrian.rolap.RolapSchema} uses the same
 * instance).
 * 
 * @author Richard M. Emberson
 * @version $Id:
 *          //open/mondrian/src/main/mondrian/rolap/aggmatcher/DefaultRules.
 *          java#18 $
 */
public class DefaultRules {

  private static final Logger LOGGER = Logger.getLogger(DefaultRules.class);

  private static final MondrianResource mres = MondrianResource.instance();

  /**
   * There is a single instance of the {@link DefaultRecognizer} and the
   * {@link DefaultRules} class is a container of that instance.
   */
  public static synchronized DefaultRules getInstance() {
    if (instance == null) {
      InputStream inStream = getAggRuleInputStream();
      if (inStream == null) {
        return null;
      }

      DefaultDef.AggRules defs = makeAggRules(inStream);

      // validate the DefaultDef.AggRules object
      ListRecorder reclists = new ListRecorder();
      try {
        defs.validate(reclists);
      } catch (RecorderException e) {
        // ignore
      }

      reclists.logWarningMessage(LOGGER);
      reclists.logErrorMessage(LOGGER);

      if (reclists.hasErrors()) {
        reclists.throwRTException();
      }

      // make sure the tag name exists
      String tag = MondrianProperties.instance().AggregateRuleTag.get();
      DefaultDef.AggRule aggrule = defs.getAggRule(tag);
      if (aggrule == null) {
        throw mres.MissingDefaultAggRule.ex(tag);
      }

      DefaultRules rules = new DefaultRules(defs);
      rules.setTag(tag);
      instance = rules;
    }
    return instance;
  }

  private static InputStream getAggRuleInputStream() {
    String aggRules = MondrianProperties.instance().AggregateRules.get();

    InputStream inStream = DefaultRules.class.getResourceAsStream(aggRules);
    if (inStream == null) {
      try {
        URL url = new URL(aggRules);
        inStream = url.openStream();
      } catch (MalformedURLException e) {
        // ignore
      } catch (IOException e) {
        // ignore
      }
    }
    if (inStream == null) {
      LOGGER.warn(mres.CouldNotLoadDefaultAggregateRules.str(aggRules));
    }
    return inStream;
  }

  private static DefaultRules instance = null;

  static {
    // When the value of the AggregateRules property is changed, force
    // system to reload the DefaultRules.
    // There is no need to provide equals/hashCode methods for this
    // Trigger since it is a singleton and is never removed.
    Trigger trigger = new Trigger() {
      public boolean isPersistent() {
        return true;
      }

      public int phase() {
        return Trigger.PRIMARY_PHASE;
      }

      public void execute(Property property, String value) {
        synchronized (DefaultRules.class) {
          DefaultRules oldInstance = DefaultRules.instance;
          DefaultRules.instance = null;

          DefaultRules newInstance = null;
          Exception ex = null;
          try {
            newInstance = DefaultRules.getInstance();
          } catch (Exception e) {
            ex = e;
          }
          if (ex != null) {
            DefaultRules.instance = oldInstance;

            throw new Trigger.VetoRT(ex);

          } else if (newInstance == null) {
            DefaultRules.instance = oldInstance;

            String msg = mres.FailedCreateNewDefaultAggregateRules.str(property
              .getPath(), value);
            throw new Trigger.VetoRT(msg);

          } else {
            instance = newInstance;
          }
        }
      }
    };

    final MondrianProperties properties = MondrianProperties.instance();
    properties.AggregateRules.addTrigger(trigger);
    properties.AggregateRuleTag.addTrigger(trigger);
  }

  protected static DefaultDef.AggRules makeAggRules(final File file) {
    DOMWrapper def = makeDOMWrapper(file);
    try {
      DefaultDef.AggRules rules = new DefaultDef.AggRules(def);
      return rules;
    } catch (XOMException e) {
      throw mres.AggRuleParse.ex(file.getName(), e);
    }
  }

  protected static DefaultDef.AggRules makeAggRules(final URL url) {
    DOMWrapper def = makeDOMWrapper(url);
    try {
      DefaultDef.AggRules rules = new DefaultDef.AggRules(def);
      return rules;
    } catch (XOMException e) {
      throw mres.AggRuleParse.ex(url.toString(), e);
    }
  }

  protected static DefaultDef.AggRules makeAggRules(final InputStream inStream) {
    DOMWrapper def = makeDOMWrapper(inStream);
    try {
      DefaultDef.AggRules rules = new DefaultDef.AggRules(def);
      return rules;
    } catch (XOMException e) {
      throw mres.AggRuleParse.ex("InputStream", e);
    }
  }

  protected static DefaultDef.AggRules makeAggRules(final String text,
    final String name) {
    DOMWrapper def = makeDOMWrapper(text, name);
    try {
      DefaultDef.AggRules rules = new DefaultDef.AggRules(def);
      return rules;
    } catch (XOMException e) {
      throw mres.AggRuleParse.ex(name, e);
    }
  }

  protected static DOMWrapper makeDOMWrapper(final File file) {
    try {
      return makeDOMWrapper(file.toURL());
    } catch (MalformedURLException e) {
      throw mres.AggRuleParse.ex(file.getName(), e);
    }
  }

  protected static DOMWrapper makeDOMWrapper(final URL url) {
    try {
      final Parser xmlParser = XOMUtil.createDefaultParser();
      DOMWrapper def = xmlParser.parse(url);
      return def;
    } catch (XOMException e) {
      throw mres.AggRuleParse.ex(url.toString(), e);
    }
  }

  protected static DOMWrapper makeDOMWrapper(final InputStream inStream) {
    try {
      final Parser xmlParser = XOMUtil.createDefaultParser();
      DOMWrapper def = xmlParser.parse(inStream);
      return def;
    } catch (XOMException e) {
      throw mres.AggRuleParse.ex("InputStream", e);
    }
  }

  protected static DOMWrapper makeDOMWrapper(final String text, final String name) {
    try {
      final Parser xmlParser = XOMUtil.createDefaultParser();
      DOMWrapper def = xmlParser.parse(text);
      return def;
    } catch (XOMException e) {
      throw mres.AggRuleParse.ex(name, e);
    }
  }

  private final DefaultDef.AggRules rules;

  private final Map<String, Recognizer.Matcher> factToPattern;

  private final Map<String, Recognizer.Matcher> foreignKeyMatcherMap;

  private Recognizer.Matcher ignoreMatcherMap;

  private Recognizer.Matcher factCountMatcher;

  private String tag;

  private DefaultRules(final DefaultDef.AggRules rules) {
    this.rules = rules;
    this.factToPattern = new HashMap<String, Recognizer.Matcher>();
    this.foreignKeyMatcherMap = new HashMap<String, Recognizer.Matcher>();
    this.tag = MondrianProperties.instance().AggregateRuleTag.getDefaultValue();
  }

  public void validate(MessageRecorder msgRecorder) {
    rules.validate(msgRecorder);
  }

  /**
   * Sets the name (tag) of this rule.
   * 
   * @param tag
   */
  private void setTag(final String tag) {
    this.tag = tag;
  }

  /**
   * Gets the tag of this rule (this is the value of the
   * {@link MondrianProperties#AggregateRuleTag} property).
   */
  public String getTag() {
    return this.tag;
  }

  /**
   * Returns the {@link mondrian.rolap.aggmatcher.DefaultDef.AggRule} whose tag
   * equals this rule's tag.
   */
  public DefaultDef.AggRule getAggRule() {
    return getAggRule(getTag());
  }

  /**
   * Returns the {@link mondrian.rolap.aggmatcher.DefaultDef.AggRule} whose tag
   * equals the parameter tag, or null if not found.
   * 
   * @param tag
   * @return the AggRule with tag value equal to tag parameter, or null.
   */
  public DefaultDef.AggRule getAggRule(final String tag) {
    return this.rules.getAggRule(tag);
  }

  /**
   * Gets the {@link mondrian.rolap.aggmatcher.Recognizer.Matcher} for this
   * tableName.
   * 
   * @param tableName
   */
  public Recognizer.Matcher getTableMatcher(final String tableName) {
    Recognizer.Matcher matcher = factToPattern.get(tableName);
    if (matcher == null) {
      // get default AggRule
      DefaultDef.AggRule rule = getAggRule();
      DefaultDef.TableMatch tableMatch = rule.getTableMatch();
      matcher = tableMatch.getMatcher(tableName);
      factToPattern.put(tableName, matcher);
    }
    return matcher;
  }

  /**
   * Gets the {@link mondrian.rolap.aggmatcher.Recognizer.Matcher} for the fact
   * count column.
   */
  public Recognizer.Matcher getIgnoreMatcher() {
    if (ignoreMatcherMap == null) {
      // get default AggRule
      DefaultDef.AggRule rule = getAggRule();
      DefaultDef.IgnoreMap ignoreMatch = rule.getIgnoreMap();
      if (ignoreMatch == null) {
        ignoreMatcherMap = new Recognizer.Matcher() {
          public boolean matches(String name) {
            return false;
          }
        };
      } else {
        ignoreMatcherMap = ignoreMatch.getMatcher();
      }
    }
    return ignoreMatcherMap;
  }

  /**
   * Gets the {@link mondrian.rolap.aggmatcher.Recognizer.Matcher} for columns
   * that should be ignored.
   * 
   * @return the {@link mondrian.rolap.aggmatcher.Recognizer.Matcher} for
   *         columns that should be ignored.
   */
  public Recognizer.Matcher getFactCountMatcher() {
    if (factCountMatcher == null) {
      // get default AggRule
      DefaultDef.AggRule rule = getAggRule();
      DefaultDef.FactCountMatch factCountMatch = rule.getFactCountMatch();
      factCountMatcher = factCountMatch.getMatcher();
    }
    return factCountMatcher;
  }

  /**
   * Gets the {@link mondrian.rolap.aggmatcher.Recognizer.Matcher} for this
   * foreign key column name.
   * 
   * @param foreignKeyName
   *          Name of a foreign key column
   */
  public Recognizer.Matcher getForeignKeyMatcher(String foreignKeyName) {
    Recognizer.Matcher matcher = foreignKeyMatcherMap.get(foreignKeyName);
    if (matcher == null) {
      // get default AggRule
      DefaultDef.AggRule rule = getAggRule();
      DefaultDef.ForeignKeyMatch foreignKeyMatch = rule.getForeignKeyMatch();
      matcher = foreignKeyMatch.getMatcher(foreignKeyName);
      foreignKeyMatcherMap.put(foreignKeyName, matcher);
    }
    return matcher;
  }

  /**
   * Returns true if this candidate aggregate table name "matches" the
   * factTableName.
   * 
   * @param factTableName
   *          Name of the fact table
   * @param name
   *          candidate aggregate table name
   */
  public boolean matchesTableName(final String factTableName, final String name) {
    Recognizer.Matcher matcher = getTableMatcher(factTableName);
    return matcher.matches(name);
  }

  /**
   * Creates a {@link mondrian.rolap.aggmatcher.Recognizer.Matcher} for the
   * given measure name (symbolic name), column name and aggregate name (sum,
   * count, etc.).
   */
  public Recognizer.Matcher getMeasureMatcher(final String measureName,
    final String measureColumnName, final String aggregateName) {
    DefaultDef.AggRule rule = getAggRule();
    Recognizer.Matcher matcher = rule.getMeasureMap().getMatcher(measureName,
      measureColumnName, aggregateName);
    return matcher;
  }

  /**
   * Gets a {@link mondrian.rolap.aggmatcher.Recognizer.Matcher} for a given
   * level's hierarchy's name, level name and column name.
   */
  public Recognizer.Matcher getLevelMatcher(final String usagePrefix,
    final String hierarchyName, final String levelName, final String levelColumnName) {
    DefaultDef.AggRule rule = getAggRule();
    Recognizer.Matcher matcher = rule.getLevelMap().getMatcher(usagePrefix,
      hierarchyName, levelName, levelColumnName);
    return matcher;
  }

  /**
   * Uses the {@link DefaultRecognizer} Recognizer to determine if the given
   * aggTable's columns all match upto the dbFactTable's columns (where present)
   * making the column usages as a result.
   */
  public boolean columnsOK(final RolapStar star, final JdbcSchema.Table dbFactTable,
    final JdbcSchema.Table aggTable, final MessageRecorder msgRecorder) {
    Recognizer cb = new DefaultRecognizer(this, star, dbFactTable, aggTable,
      msgRecorder);
    return cb.check();
  }
}

// End DefaultRules.java
