/* Generated By:JavaCC: Do not edit this line. MdxParserImplConstants.java */
package mondrian.parser;

/**
 * Token literal values and constants. Generated by
 * org.javacc.parser.OtherFilesGen#start()
 */
public interface MdxParserImplConstants {

	/** End of File. */
	int EOF = 0;
	/** RegularExpression Id. */
	int AND = 1;
	/** RegularExpression Id. */
	int AS = 2;
	/** RegularExpression Id. */
	int AXIS = 3;
	/** RegularExpression Id. */
	int CASE = 4;
	/** RegularExpression Id. */
	int CAST = 5;
	/** RegularExpression Id. */
	int CELL = 6;
	/** RegularExpression Id. */
	int CHAPTERS = 7;
	/** RegularExpression Id. */
	int COLUMNS = 8;
	/** RegularExpression Id. */
	int DIMENSION = 9;
	/** RegularExpression Id. */
	int DRILLTHROUGH = 10;
	/** RegularExpression Id. */
	int ELSE = 11;
	/** RegularExpression Id. */
	int EMPTY = 12;
	/** RegularExpression Id. */
	int END = 13;
	/** RegularExpression Id. */
	int EXPLAIN = 14;
	/** RegularExpression Id. */
	int FIRSTROWSET = 15;
	/** RegularExpression Id. */
	int FOR = 16;
	/** RegularExpression Id. */
	int FROM = 17;
	/** RegularExpression Id. */
	int IN = 18;
	/** RegularExpression Id. */
	int IS = 19;
	/** RegularExpression Id. */
	int MATCHES = 20;
	/** RegularExpression Id. */
	int MAXROWS = 21;
	/** RegularExpression Id. */
	int MEMBER = 22;
	/** RegularExpression Id. */
	int NON = 23;
	/** RegularExpression Id. */
	int NOT = 24;
	/** RegularExpression Id. */
	int NULL = 25;
	/** RegularExpression Id. */
	int ON = 26;
	/** RegularExpression Id. */
	int OR = 27;
	/** RegularExpression Id. */
	int PAGES = 28;
	/** RegularExpression Id. */
	int PLAN = 29;
	/** RegularExpression Id. */
	int PROPERTIES = 30;
	/** RegularExpression Id. */
	int RETURN = 31;
	/** RegularExpression Id. */
	int ROWS = 32;
	/** RegularExpression Id. */
	int SECTIONS = 33;
	/** RegularExpression Id. */
	int SELECT = 34;
	/** RegularExpression Id. */
	int SET = 35;
	/** RegularExpression Id. */
	int THEN = 36;
	/** RegularExpression Id. */
	int WHEN = 37;
	/** RegularExpression Id. */
	int WHERE = 38;
	/** RegularExpression Id. */
	int XOR = 39;
	/** RegularExpression Id. */
	int WITH = 40;
	/** RegularExpression Id. */
	int SINGLE_LINE_COMMENT = 50;
	/** RegularExpression Id. */
	int FORMAL_COMMENT = 51;
	/** RegularExpression Id. */
	int MULTI_LINE_COMMENT = 52;
	/** RegularExpression Id. */
	int ASTERISK = 54;
	/** RegularExpression Id. */
	int BANG = 55;
	/** RegularExpression Id. */
	int COLON = 56;
	/** RegularExpression Id. */
	int COMMA = 57;
	/** RegularExpression Id. */
	int CONCAT = 58;
	/** RegularExpression Id. */
	int DOT = 59;
	/** RegularExpression Id. */
	int EQ = 60;
	/** RegularExpression Id. */
	int GE = 61;
	/** RegularExpression Id. */
	int GT = 62;
	/** RegularExpression Id. */
	int LBRACE = 63;
	/** RegularExpression Id. */
	int LE = 64;
	/** RegularExpression Id. */
	int LPAREN = 65;
	/** RegularExpression Id. */
	int LT = 66;
	/** RegularExpression Id. */
	int MINUS = 67;
	/** RegularExpression Id. */
	int NE = 68;
	/** RegularExpression Id. */
	int PLUS = 69;
	/** RegularExpression Id. */
	int RBRACE = 70;
	/** RegularExpression Id. */
	int RPAREN = 71;
	/** RegularExpression Id. */
	int SOLIDUS = 72;
	/** RegularExpression Id. */
	int UNSIGNED_INTEGER_LITERAL = 73;
	/** RegularExpression Id. */
	int APPROX_NUMERIC_LITERAL = 74;
	/** RegularExpression Id. */
	int DECIMAL_NUMERIC_LITERAL = 75;
	/** RegularExpression Id. */
	int EXPONENT = 76;
	/** RegularExpression Id. */
	int SINGLE_QUOTED_STRING = 77;
	/** RegularExpression Id. */
	int DOUBLE_QUOTED_STRING = 78;
	/** RegularExpression Id. */
	int WHITESPACE = 79;
	/** RegularExpression Id. */
	int ID = 80;
	/** RegularExpression Id. */
	int QUOTED_ID = 81;
	/** RegularExpression Id. */
	int AMP_QUOTED_ID = 82;
	/** RegularExpression Id. */
	int LETTER = 83;
	/** RegularExpression Id. */
	int DIGIT = 84;

	/** Lexical state. */
	int DEFAULT = 0;
	/** Lexical state. */
	int IN_SINGLE_LINE_COMMENT = 1;
	/** Lexical state. */
	int IN_FORMAL_COMMENT = 2;
	/** Lexical state. */
	int IN_MULTI_LINE_COMMENT = 3;

	/** Literal token values. */
	String[] tokenImage = { "<EOF>", "\"AND\"", "\"AS\"", "\"AXIS\"", "\"CASE\"",
			"\"CAST\"", "\"CELL\"", "\"CHAPTERS\"", "\"COLUMNS\"", "\"DIMENSION\"",
			"\"DRILLTHROUGH\"", "\"ELSE\"", "\"EMPTY\"", "\"END\"", "\"EXPLAIN\"",
			"\"FIRSTROWSET\"", "\"FOR\"", "\"FROM\"", "\"IN\"", "\"IS\"",
			"\"MATCHES\"", "\"MAXROWS\"", "\"MEMBER\"", "\"NON\"", "\"NOT\"",
			"\"NULL\"", "\"ON\"", "\"OR\"", "\"PAGES\"", "\"PLAN\"",
			"\"PROPERTIES\"", "\"RETURN\"", "\"ROWS\"", "\"SECTIONS\"", "\"SELECT\"",
			"\"SET\"", "\"THEN\"", "\"WHEN\"", "\"WHERE\"", "\"XOR\"", "\"WITH\"",
			"\" \"", "\"\\t\"", "\"\\n\"", "\"\\r\"", "\"\\f\"",
			"<token of kind 46>", "\"//\"", "\"--\"", "\"/*\"",
			"<SINGLE_LINE_COMMENT>", "\"*/\"", "\"*/\"", "<token of kind 53>",
			"\"*\"", "\"!\"", "\":\"", "\",\"", "\"||\"", "\".\"", "\"=\"", "\">=\"",
			"\">\"", "\"{\"", "\"<=\"", "\"(\"", "\"<\"", "\"-\"", "\"<>\"", "\"+\"",
			"\"}\"", "\")\"", "\"/\"", "<UNSIGNED_INTEGER_LITERAL>",
			"<APPROX_NUMERIC_LITERAL>", "<DECIMAL_NUMERIC_LITERAL>", "<EXPONENT>",
			"<SINGLE_QUOTED_STRING>", "<DOUBLE_QUOTED_STRING>", "<WHITESPACE>",
			"<ID>", "<QUOTED_ID>", "<AMP_QUOTED_ID>", "<LETTER>", "<DIGIT>", };

}
