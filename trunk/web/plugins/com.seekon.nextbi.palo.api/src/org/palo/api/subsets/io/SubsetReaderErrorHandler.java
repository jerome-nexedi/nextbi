/*    */package org.palo.api.subsets.io;

/*    */
/*    */import java.io.PrintStream; /*    */
import org.xml.sax.ErrorHandler; /*    */
import org.xml.sax.SAXException; /*    */
import org.xml.sax.SAXParseException;

/*    */
/*    */class SubsetReaderErrorHandler
/*    */implements ErrorHandler
/*    */{
  /* 52 */private boolean isValid = true;

  /*    */
  /*    */public final void error(SAXParseException exception)
  /*    */throws SAXException
  /*    */{
    /* 58 */String msg = exception.getMessage();
    /* 59 */int valIndex = msg.indexOf("''");
    /* 60 */if ((valIndex > 0) && ((
    /* 61 */(msg.indexOf("'boolean'", valIndex) > 0) ||
    /* 62 */(msg.indexOf("'integer'", valIndex) > 0) ||
    /* 63 */(msg.indexOf("'decimal'", valIndex) > 0) ||
    /* 64 */(msg.indexOf("'string'", valIndex) > 0) ||
    /* 65 */(msg.indexOf("'value'", valIndex) > 0)))) {
      /* 66 */return;
      /*    */}
    /*    */
    /* 69 */this.isValid = false;
    /* 70 */System.err
      .println("SUBSET READER ERROR_MSG: " + exception.getMessage());
    /*    */}

  /*    */
  /*    */public final void fatalError(SAXParseException exception)
    throws SAXException {
    /* 74 */this.isValid = false;
    /*    */}

  /*    */
  /*    */public final void warning(SAXParseException exception)
  /*    */throws SAXException
  /*    */{
    /* 80 */System.err.println("SUBSET READER WARNING_MSG: "
      + exception.getMessage());
    /*    */}

  /*    */
  /*    */final boolean hasErrors()
  /*    */{
    /* 89 */return !this.isValid;
    /*    */}
  /*    */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name: org.palo.api.subsets.io.SubsetReaderErrorHandler JD-Core
 * Version: 0.5.4
 */