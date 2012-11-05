package org.springframework.ldap.core;

public abstract interface DnParserImplConstants
{
  public static final int EOF = 0;
  public static final int ALPHA = 1;
  public static final int DIGIT = 2;
  public static final int STRINGCHAR = 3;
  public static final int STRINGENDCHAR = 4;
  public static final int SPECIAL = 5;
  public static final int HEXCHAR = 6;
  public static final int HEXPAIR = 7;
  public static final int BACKSLASHCHAR = 8;
  public static final int PAIR = 9;
  public static final int ESCAPEDSPACE = 10;
  public static final int ESCAPEDSTART = 11;
  public static final int STRINGEND = 12;
  public static final int QUOTECHAR = 13;
  public static final int HASHCHAR = 14;
  public static final int ATTRIBUTE_TYPE_STRING = 15;
  public static final int LDAP_OID = 16;
  public static final int SPACE = 17;
  public static final int ATTRVALUE = 18;
  public static final int SPACED_EQUALS = 19;
  public static final int DEFAULT = 0;
  public static final int ATTRVALUE_S = 1;
  public static final int SPACED_EQUALS_S = 2;
  public static final String[] tokenImage = { "<EOF>", "<ALPHA>", "<DIGIT>", "<STRINGCHAR>", "<STRINGENDCHAR>", "<SPECIAL>", "<HEXCHAR>", "<HEXPAIR>", "\"\\\\\"", "<PAIR>", "<ESCAPEDSPACE>", "<ESCAPEDSTART>", "<STRINGEND>", "\"\\\"\"", "\"#\"", "<ATTRIBUTE_TYPE_STRING>", "<LDAP_OID>", "\" \"", "<ATTRVALUE>", "<SPACED_EQUALS>", "\",\"", "\";\"", "\"+\"" };
}
