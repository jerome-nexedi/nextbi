/*    */package org.palo.api;

/*    */
/*    */public abstract class Property2Factory
/*    */{
  /*    */private static Property2Factory instance;
  /*    */
  /*    */static
  /*    */{
    /*    */try
    /*    */{
      /* 50 */instance =
      /* 51 */(Property2Factory) Class.forName(
        "org.palo.api.impl.Property2FactoryImpl").newInstance();
      /*    */}
    /*    */catch (Exception e) {
      /* 54 */e.printStackTrace();
      /*    */}
    /*    */}

  /*    */
  /*    */public static Property2Factory getInstance() {
    /* 59 */return instance;
    /*    */}

  /*    */
  /*    */public abstract Property2 newProperty(Connection paramConnection,
    String paramString1, String paramString2);

  /*    */
  /*    */public abstract Property2 newProperty(Connection paramConnection,
    String paramString1, String paramString2, Property2 paramProperty2);

  /*    */
  /*    */public abstract Property2 newProperty(Connection paramConnection,
    String paramString1, String paramString2, int paramInt);

  /*    */
  /*    */public abstract Property2 newProperty(Connection paramConnection,
    String paramString1, String paramString2, Property2 paramProperty2, int paramInt);

  /*    */
  /*    */public abstract Property2 newReadOnlyProperty(Connection paramConnection,
    String paramString1, String paramString2);

  /*    */
  /*    */public abstract Property2 newReadOnlyProperty(Connection paramConnection,
    String paramString1, String paramString2, Property2 paramProperty2);

  /*    */
  /*    */public abstract Property2 newReadOnlyProperty(Connection paramConnection,
    String paramString1, String paramString2, int paramInt);

  /*    */
  /*    */public abstract Property2 newReadOnlyProperty(Connection paramConnection,
    String paramString1, String paramString2, Property2 paramProperty2, int paramInt);
  /*    */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name: org.palo.api.Property2Factory JD-Core Version: 0.5.4
 */