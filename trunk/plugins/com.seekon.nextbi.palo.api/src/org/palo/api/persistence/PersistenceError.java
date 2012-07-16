/*    */ package org.palo.api.persistence;
/*    */ 
/*    */ public abstract interface PersistenceError
/*    */ {
/*    */   public static final int UNKNOWN_ERROR = -1;
/*    */   public static final int UNKNOWN_DIMENSION = 0;
/*    */   public static final int UNKNOWN_ELEMENT = 1;
/*    */   public static final int UNKNOWN_CUBE_VIEW = 2;
/*    */   public static final int UNKNOWN_AXIS = 4;
/*    */   public static final int UNKNOWN_SUBSET = 8;
/*    */   public static final int UNKNOWN_PATH = 16;
/*    */   public static final int LOADING_FAILED = 32;
/*    */   public static final int TARGET_UNKNOWN = -1;
/*    */   public static final int TARGET_GENERAL = 0;
/*    */   public static final int TARGET_SELECTED = 1;
/*    */   public static final int TARGET_EXPANDED_PATH = 2;
/*    */   public static final int TARGET_HIDDEN_PATH = 4;
/*    */   public static final int TARGET_SUBSET = 8;
/* 92 */   public static final int[] ALL_ERROR_TYPES = { 
/* 93 */     -1, 
/* 95 */     0, 1, 
/* 96 */     2, 
/* 97 */     4, 
/* 98 */     8, 
/* 99 */     16, 
/* 100 */     32 };
/*    */ 
/*    */   public abstract Object getSource();
/*    */ 
/*    */   public abstract String getSourceId();
/*    */ 
/*    */   public abstract Object getLocation();
/*    */ 
/*    */   public abstract String getCause();
/*    */ 
/*    */   public abstract Object getSection();
/*    */ 
/*    */   public abstract int getTargetType();
/*    */ 
/*    */   public abstract int getType();
/*    */ 
/*    */   public abstract String getMessage();
/*    */ }

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name:     org.palo.api.persistence.PersistenceError
 * JD-Core Version:    0.5.4
 */