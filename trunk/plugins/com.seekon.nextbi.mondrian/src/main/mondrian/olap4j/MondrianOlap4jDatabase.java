/*
// $Id: //open/mondrian/src/main/mondrian/olap4j/MondrianOlap4jDatabase.java#4 $
// This software is subject to the terms of the Eclipse Public License v1.0
// Agreement, available at the following URL:
// http://www.eclipse.org/legal/epl-v10.html.
// Copyright (C) 2007-2011 Julian Hyde
// All Rights Reserved.
// You must accept the terms of that agreement to use this software.
 */
package mondrian.olap4j;

import java.util.Collections;
import java.util.List;

import org.olap4j.OlapConnection;
import org.olap4j.OlapException;
import org.olap4j.impl.Named;
import org.olap4j.impl.Olap4jUtil;
import org.olap4j.metadata.Catalog;
import org.olap4j.metadata.Database;
import org.olap4j.metadata.NamedList;

/**
 * Implementation of {@link org.olap4j.metadata.Database} for the Mondrian OLAP
 * engine.
 * 
 * @version $Id:
 *          //open/mondrian/src/main/mondrian/olap4j/MondrianOlap4jDatabase.
 *          java#4 $
 * @author LBoudreau
 */
class MondrianOlap4jDatabase implements Database, Named {
  private final NamedList<MondrianOlap4jCatalog> catalogs;

  private final MondrianOlap4jConnection olap4jConnection;

  private final String name;

  private final String description;

  private final String providerName;

  private final String url;

  private final String dataSourceInfo;

  private final List<ProviderType> providerType;

  private final List<AuthenticationMode> authenticationMode;

  /**
   * Creates a MondrianOlap4jDatabase.
   * 
   * @param olap4jConnection
   *          Connection
   * @param catalogs
   *          List of catalogs
   * @param name
   *          Name of database
   * @param description
   *          Description of database
   * @param providerName
   *          Provider name
   * @param url
   *          URL of provider
   * @param dataSourceInfo
   *          Data source info
   * @param providerType
   *          List of provider types supported by this database
   * @param authenticationMode
   *          Authentication modes
   */
  MondrianOlap4jDatabase(MondrianOlap4jConnection olap4jConnection,
    NamedList<MondrianOlap4jCatalog> catalogs, String name, String description,
    String providerName, String url, String dataSourceInfo,
    List<ProviderType> providerType, List<AuthenticationMode> authenticationMode) {
    this.olap4jConnection = olap4jConnection;
    this.name = name;
    this.description = description;
    this.providerName = providerName;
    this.url = url;
    this.dataSourceInfo = dataSourceInfo;
    this.providerType = Collections.unmodifiableList(providerType);
    this.authenticationMode = Collections.unmodifiableList(authenticationMode);
    this.catalogs = MondrianOlap4jConnection.unmodifiableNamedList(catalogs);
  }

  public List<AuthenticationMode> getAuthenticationModes() throws OlapException {
    return authenticationMode;
  }

  public NamedList<Catalog> getCatalogs() throws OlapException {
    return Olap4jUtil.cast(catalogs);
  }

  public String getDescription() throws OlapException {
    return this.description;
  }

  public String getName() {
    return this.name;
  }

  public OlapConnection getOlapConnection() {
    return this.olap4jConnection;
  }

  public String getProviderName() throws OlapException {
    return this.providerName;
  }

  public List<ProviderType> getProviderTypes() throws OlapException {
    return this.providerType;
  }

  public String getURL() throws OlapException {
    return this.url;
  }

  public String getDataSourceInfo() throws OlapException {
    return this.dataSourceInfo;
  }
}

// End MondrianOlap4jDatabase.java
