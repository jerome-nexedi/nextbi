/*     */package org.palo.api;

/*     */
/*     */import java.io.InputStream; /*     */
import java.util.Iterator; /*     */
import java.util.Properties; /*     */
import java.util.Set;

/*     */
/*     */public abstract class ConnectionFactory
/*     */{
  /*     */private static ConnectionFactory instance;
  /*     */
  /*     */static
  /*     */{
    /*     */try
    /*     */{
      /* 76 */Properties props = new Properties(System.getProperties());
      /* 77 */InputStream propsStream = ConnectionFactory.class
      /* 78 */.getResourceAsStream("/paloapi.properties");
      /* 79 */if (propsStream != null) {
        /* 80 */props.load(propsStream);
        /*     */
        /* 83 */if (props.containsKey("wpalo"))
          /* 84 */System.setProperty("wpalo", props.getProperty("wpalo"));
        /* 85 */if (props.containsKey("xmla_ignoreVariableCubes")) {
          /* 86 */System.setProperty("xmla_ignoreVariableCubes", props
            .getProperty("xmla_ignoreVariableCubes"));
          /*     */}
        /*     */
        /* 93 */Properties sysProps = System.getProperties();
        /* 94 */for (Iterator localIterator = props.keySet().iterator(); localIterator
          .hasNext();) {
          Object key = localIterator.next();
          /* 95 */if (!sysProps.containsKey(key)) {
            /* 96 */sysProps.put(key, props.get(key));
            /*     */}
        }
        /*     */
        /* 99 */System.setProperties(sysProps);
        /*     */}
      /*     */
      /* 102 */instance =
      /* 103 */(ConnectionFactory) Class.forName(
        "org.palo.api.impl.ConnectionFactoryImpl").newInstance();
      /*     */}
    /*     */catch (Exception e)
    /*     */{
      /* 108 */e.printStackTrace();
      /*     */}
    /*     */}

  /*     */
  /*     */public static ConnectionFactory getInstance()
  /*     */{
    /* 114 */return instance;
    /*     */}

  /*     */
  /*     *//** @deprecated */
  /*     */public abstract Connection newConnection(String paramString1,
    String paramString2, String paramString3, String paramString4);

  /*     */
  /*     *//** @deprecated */
  /*     */public abstract Connection newConnection(String paramString1,
    String paramString2, String paramString3, String paramString4,
    boolean paramBoolean, int paramInt);

  /*     */
  /*     */public abstract ConnectionConfiguration getConfiguration(
    String paramString1, String paramString2);

  /*     */
  /*     */public abstract ConnectionConfiguration getConfiguration(
    String paramString1, String paramString2, String paramString3,
    String paramString4);

  /*     */
  /*     */public abstract Connection newConnection(
    ConnectionConfiguration paramConnectionConfiguration);
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name: org.palo.api.ConnectionFactory JD-Core Version: 0.5.4
 */