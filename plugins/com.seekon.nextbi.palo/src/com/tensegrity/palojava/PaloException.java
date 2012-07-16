/*     */package com.tensegrity.palojava;

/*     */
/*     */public class PaloException extends RuntimeException
/*     */{
  /*     */static final long serialVersionUID = 1L;

  /*     */private String errorCode;

  /*     */private String errorMsg;

  /*     */private String errorReason;

  /*     */
  /*     */public PaloException(String msg)
  /*     */{
    /* 65 */super(msg);
    /*     */}

  /*     */
  /*     */public PaloException(Exception cause)
  /*     */{
    /* 74 */super(cause);
    /*     */}

  /*     */
  /*     */public PaloException(String msg, Throwable cause)
  /*     */{
    /* 84 */super(msg, cause);
    /*     */}

  /*     */
  /*     */public PaloException(String errorCode, String errorMsg,
    String errorReason)
  /*     */{
    /* 95 */this(errorCode, errorMsg, errorReason, null);
    /*     */}

  /*     */
  /*     */public PaloException(String errorCode, String errorMsg,
    String errorReason, Throwable cause)
  /*     */{
    /* 109 */super(stripErrorNumber(errorMsg)
      + (
      /* 108 */(errorReason == null) ? "" : new StringBuilder(" [").append(
        errorReason).append("]").toString()) +
      /* 109 */" (Palo Error " + errorCode + ")", cause);
    /* 110 */this.errorCode = errorCode;
    /* 111 */this.errorMsg = errorMsg;
    /* 112 */this.errorReason = errorReason;
    /*     */}

  /*     */
  /*     */private static final String stripErrorNumber(String txt) {
    /* 116 */if (txt == null) {
      /* 117 */return txt;
      /*     */}
    /* 119 */StringBuffer result = new StringBuffer();
    /* 120 */int index = 0;
    /* 121 */txt = txt.trim();
    /* 122 */for (char c : txt.toCharArray()) {
      /* 123 */if (!Character.isDigit(c))
        break;
      /* 124 */++index;
      /*     */}
    /*     */
    /* 129 */if (index < txt.length()) {
      /* 130 */result.append(txt.substring(index).trim());
      /*     */}
    /* 132 */return result.toString();
    /*     */}

  /*     */
  /*     */public final String getErrorCode()
  /*     */{
    /* 140 */return this.errorCode;
    /*     */}

  /*     */
  /*     */public final String getDescription()
  /*     */{
    /* 149 */if (this.errorMsg == null)
      /* 150 */this.errorMsg = getMessage();
    /* 151 */return this.errorMsg;
    /*     */}

  /*     */
  /*     */public final String getReason()
  /*     */{
    /* 160 */if (this.errorReason == null)
      /* 161 */this.errorReason = getDescription();
    /* 162 */return this.errorReason;
    /*     */}
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.paloapi\lib\palo.jar
 * Qualified Name: com.tensegrity.palojava.PaloException JD-Core Version: 0.5.4
 */