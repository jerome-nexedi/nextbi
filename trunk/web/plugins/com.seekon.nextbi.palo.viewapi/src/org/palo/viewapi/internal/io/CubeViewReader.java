/*     */package org.palo.viewapi.internal.io;

/*     */
/*     */import java.io.IOException; /*     */
import java.io.InputStream; /*     */
import javax.xml.transform.stream.StreamSource; /*     */
import javax.xml.validation.Schema; /*     */
import javax.xml.validation.SchemaFactory; /*     */
import javax.xml.validation.Validator; /*     */
import javax.xml.validation.ValidatorHandler; /*     */
import org.palo.api.Cube; /*     */
import org.palo.api.exceptions.PaloIOException; /*     */
import org.palo.viewapi.AuthUser; /*     */
import org.palo.viewapi.CubeView; /*     */
import org.palo.viewapi.View; /*     */
import org.palo.viewapi.internal.io.xml.CubeViewXMLHandler; /*     */
import org.xml.sax.InputSource; /*     */
import org.xml.sax.SAXException; /*     */
import org.xml.sax.XMLReader; /*     */
import org.xml.sax.helpers.XMLReaderFactory;

/*     */
/*     */public class CubeViewReader
/*     */{
  /* 67 */public static boolean CHECK_RIGHTS = true;

  /*     */
  /* 72 */private static CubeViewReader instance = new CubeViewReader();

  /*     */
  /* 74 */static final CubeViewReader getInstance() {
    return instance;
  }

  /*     */
  /*     */
  /*     */public CubeView fromXML(AuthUser user, View view, Cube cube,
    InputStream input)
  /*     */throws PaloIOException
  /*     */{
    /* 86 */CubeView cView = null;
    /* 87 */boolean oldRights = CHECK_RIGHTS;
    /*     */try
    /*     */{
      /* 95 */CHECK_RIGHTS = false;
      /* 96 */StreamSource xml = new StreamSource(input);
      /* 97 */SchemaFactory sf =
      /* 98 */SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
      /* 99 */Schema xsd = sf.newSchema(
      /* 100 */new StreamSource(CubeViewReader.class
        .getResourceAsStream("xml/cubeview.xsd")));
      /* 101 */CubeViewReaderErrorHandler errHandler =
      /* 102 */new CubeViewReaderErrorHandler();
      /* 103 */ValidatorHandler vHandler = xsd.newValidatorHandler();
      /* 104 */vHandler.setErrorHandler(errHandler);
      /*     */
      /* 106 */CubeViewXMLHandler viewHandler = new CubeViewXMLHandler(user, view,
        cube);
      /* 107 */vHandler.setContentHandler(viewHandler);
      /*     */
      /* 109 */XMLReader parser = XMLReaderFactory.createXMLReader();
      /* 110 */parser.setContentHandler(vHandler);
      /* 111 */parser.parse(new InputSource(xml.getInputStream()));
      /* 112 */cView = viewHandler.getView();
      /*     */}
    /*     */catch (SAXException e) {
      /* 115 */throw new PaloIOException(
        "XML Exception during loading of cube view!", e);
      /*     */} catch (IOException e) {
      /* 117 */throw new PaloIOException(
        "IOException during loading of cube view!", e);
      /*     */} catch (Exception e) {
      /* 119 */throw new PaloIOException("Exception during loading of cube view!",
        e);
      /*     */} finally {
      /* 121 */CHECK_RIGHTS = oldRights;
      /*     */}
    /* 123 */return cView;
    /*     */}

  /*     */
  /*     */private final boolean isValid(InputStream input) throws SAXException,
    IOException
  /*     */{
    /* 128 */StreamSource xml = new StreamSource(input);
    /* 129 */SchemaFactory sf =
    /* 130 */SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
    /* 131 */Schema xsd = sf.newSchema(
    /* 132 */new StreamSource(CubeViewReader.class
      .getResourceAsStream("xml/cubeview.xsd")));
    /*     */
    /* 134 */CubeViewReaderErrorHandler errHandler = new CubeViewReaderErrorHandler();
    /* 135 */Validator validator = xsd.newValidator();
    /* 136 */validator.setErrorHandler(errHandler);
    /* 137 */validator.validate(xml);
    /* 138 */if (input.markSupported())
      /* 139 */input.reset();
    /* 140 */return !errHandler.hasErrors();
    /*     */}
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi
 * .jar Qualified Name: org.palo.viewapi.internal.io.CubeViewReader JD-Core
 * Version: 0.5.4
 */