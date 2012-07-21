/*
// $Id: //open/mondrian/src/main/mondrian/parser/MdxParserValidatorImpl.java#2 $
// This software is subject to the terms of the Eclipse Public License v1.0
// Agreement, available at the following URL:
// http://www.eclipse.org/legal/epl-v10.html.
// Copyright (C) 2010-2011 Julian Hyde
// All Rights Reserved.
// You must accept the terms of that agreement to use this software.
 */
package mondrian.parser;

import mondrian.olap.*;
import mondrian.server.Statement;

/**
 * Default implementation of {@link mondrian.parser.MdxParserValidator}.
 * 
 * @version $Id:
 *          //open/mondrian/src/main/mondrian/parser/MdxParserValidatorImpl.
 *          java#2 $
 * @author jhyde
 */
public class MdxParserValidatorImpl implements MdxParserValidator {
  /**
   * Creates a MdxParserValidatorImpl.
   */
  public MdxParserValidatorImpl() {
  }

  public QueryPart parseInternal(Statement statement, String queryString,
    boolean debug, FunTable funTable, boolean strictValidation) {
    return new Parser().parseInternal(new Parser.FactoryImpl(), statement,
      queryString, debug, funTable, strictValidation);
  }

  public Exp parseExpression(Statement statement, String queryString, boolean debug,
    FunTable funTable) {
    return new Parser().parseExpression(new Parser.FactoryImpl(), statement,
      queryString, debug, funTable);
  }
}

// End MdxParserValidatorImpl.java
