/*     */package org.palo.api;

/*     */
/*     */import com.tensegrity.palojava.PaloException;

/*     */
/*     */public class PaloAPIException extends RuntimeException
/*     */{
  /*     */static final long serialVersionUID = 1L;

  /*     */private Object data;

  /*     */private String errorCode;

  /*     */private String errorMsg;

  /*     */private String errorReason;

  /*     */
  /*     */public PaloAPIException(String msg)
  /*     */{
    /* 88 */super(msg);
    /*     */}

  /*     */
  /*     */public PaloAPIException(String msg, Throwable cause)
  /*     */{
    /* 97 */super(msg, cause);
    /* 98 */if (cause instanceof PaloException) {
      /* 99 */PaloException ex = (PaloException) cause;
      /* 100 */this.errorCode = ex.getErrorCode();
      /* 101 */this.errorMsg = ex.getDescription();
      /* 102 */this.errorReason = ex.getReason();
      /*     */}
    /*     */}

  /*     */
  /*     */public PaloAPIException(Throwable cause)
  /*     */{
    /* 108 */super((cause.getMessage() == null) ? "PaloException" : cause
      .getMessage(), cause);
    /* 109 */if (cause instanceof PaloException) {
      /* 110 */PaloException ex = (PaloException) cause;
      /* 111 */this.errorCode = ex.getErrorCode();
      /* 112 */this.errorMsg = ex.getDescription();
      /* 113 */this.errorReason = ex.getReason();
      /*     */}
    /*     */}

  /*     */
  /*     */public final String getErrorCode()
  /*     */{
    /* 122 */return this.errorCode;
    /*     */}

  /*     */
  /*     */public final String getDescription()
  /*     */{
    /* 132 */return (this.errorMsg != null) ? this.errorMsg : getMessage();
    /*     */}

  /*     */
  /*     */public final String getReason()
  /*     */{
    /* 142 */return (this.errorReason != null) ? this.errorReason
      : getDescription();
    /*     */}

  /*     */
  /*     */public final void setData(Object data)
  /*     */{
    /* 151 */this.data = data;
    /*     */}

  /*     */
  /*     */public final Object getData()
  /*     */{
    /* 160 */return this.data;
    /*     */}
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name: org.palo.api.PaloAPIException JD-Core Version: 0.5.4
 */