/*    */package org.palo.viewapi.internal.io;

/*    */
/*    */import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/*    */
/*    */public class CubeViewReaderErrorHandler
/*    */implements ErrorHandler
/*    */{
  /* 50 */private boolean isValid = true;

  /*    */
  /*    */public final void error(SAXParseException exception) throws SAXException {
    /* 53 */this.isValid = false;
    /* 54 */System.err.println("CUBE VIEW READER ERROR_MSG: "
      + exception.getMessage());
    /*    */}

  /*    */
  /*    */public final void fatalError(SAXParseException exception)
    throws SAXException {
    /* 58 */this.isValid = false;
    /* 59 */System.err.println("CUBE VIEW READER FATAL ERROR_MSG: " +
    /* 60 */exception.getMessage());
    /*    */}

  /*    */
  /*    */public final void warning(SAXParseException exception)
    throws SAXException {
    /* 64 */System.err.println("CUBE VIEW READER WARNING_MSG: "
      + exception.getMessage());
    /*    */}

  /*    */
  /*    */final boolean hasErrors()
  /*    */{
    /* 73 */return !this.isValid;
    /*    */}
  /*    */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi
 * .jar Qualified Name: org.palo.viewapi.internal.io.CubeViewReaderErrorHandler
 * JD-Core Version: 0.5.4
 */