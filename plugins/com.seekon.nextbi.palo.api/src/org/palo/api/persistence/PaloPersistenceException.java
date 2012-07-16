/*     */package org.palo.api.persistence;

/*     */
/*     */public class PaloPersistenceException extends Exception
/*     */{
  /*     */public static final int TYPE_UNDEFINED = -1;

  /*     */public static final int TYPE_LOAD_FAILED = 0;

  /*     */public static final int TYPE_LOAD_INCOMPLETE = 1;

  /*     */public static final int TYPE_SAVE_FAILED = 2;

  /*     */public static final int TYPE_SAVE_INCOMPLETE = 4;

  /*     */private static final long serialVersionUID = 20070510L;

  /*     */private final PersistenceError[] errors;

  /*     */private int type;

  /*     */
  /*     */public PaloPersistenceException(PersistenceError[] errors)
  /*     */{
    /* 71 */this(errors, "");
    /*     */}

  /*     */
  /*     */public PaloPersistenceException(PersistenceError[] errors, String msg)
  /*     */{
    /* 80 */super(msg);
    /* 81 */this.errors = errors;
    /* 82 */boolean failed = false;
    /* 83 */for (PersistenceError error : errors) {
      /* 84 */if (error.getType() == 32) {
        /* 85 */failed = true;
        /* 86 */break;
        /*     */}
      /*     */}
    /* 89 */if (failed)
      /* 90 */this.type = 0;
    /*     */else
      /* 92 */this.type = 1;
    /*     */}

  /*     */
  /*     */public PersistenceError[] getErrors()
  /*     */{
    /* 101 */return this.errors;
    /*     */}

  /*     */
  /*     */public final void setType(int type) {
    /* 105 */this.type = type;
    /*     */}

  /*     */
  /*     */public final int getType() {
    /* 109 */return this.type;
    /*     */}
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name: org.palo.api.persistence.PaloPersistenceException JD-Core
 * Version: 0.5.4
 */