/*     */ package org.palo.api.subsets.filter.settings;
/*     */ 
/*     */ import org.palo.api.subsets.Subset2;
/*     */ 
/*     */ public class AttributeConstraint
/*     */ {
/*     */   public static final String NONE = "None";
/*     */   public static final String LESS = "<";
/*     */   public static final String LESS_EQUAL = "<=";
/*     */   public static final String GREATER = ">";
/*     */   public static final String GREATER_EQUAL = ">=";
/*     */   public static final String NOT_EQUAL = "<>";
/*     */   public static final String EQUAL = "=";
/*  61 */   public static final String[] ALL_OPERATORS = { "None", "<", "<=", ">", ">=", "=", "<>" };
/*     */   private final String attrId;
/*  65 */   private String operator = "None";
/*  66 */   private String value = "";
/*     */   private Subset2 subset;
/*     */ 
/*     */   public AttributeConstraint(String attrId)
/*     */   {
/*  76 */     this.attrId = attrId;
/*     */   }
/*     */ 
/*     */   public final String getAttributeId()
/*     */   {
/*  84 */     return this.attrId;
/*     */   }
/*     */ 
/*     */   public final String getOperator()
/*     */   {
/*  92 */     return this.operator;
/*     */   }
/*     */ 
/*     */   public final void setOperator(String operator)
/*     */   {
/* 102 */     if (!isOperator(operator))
/* 103 */       return;
/* 104 */     this.operator = operator;
/* 105 */     markDirty();
/*     */   }
/*     */ 
/*     */   public final String getValue()
/*     */   {
/* 113 */     return this.value;
/*     */   }
/*     */ 
/*     */   public final void setValue(String value)
/*     */   {
/* 121 */     this.value = value;
/* 122 */     markDirty();
/*     */   }
/*     */ 
/*     */   public final boolean accept(String attrValue, int attrType)
/*     */   {
/* 133 */     if (this.operator.equals("None")) {
/* 134 */       return true;
/*     */     }
/* 136 */     int result = 0;
/*     */ 
/* 138 */     if (attrType == 0)
/*     */       try {
/* 140 */         Double dbValue = ((this.value != null) && (this.value.length() > 0)) ? 
/* 141 */           Double.valueOf(this.value) : new Double(0.0D);
/* 142 */         Double dbAttrValue = ((this.value != null) && (this.value.length() > 0)) ? 
/* 143 */           Double.valueOf(attrValue) : new Double(0.0D);
/* 144 */         result = dbValue.compareTo(dbAttrValue);
/*     */       }
/*     */       catch (NumberFormatException e) {
/* 147 */         result = this.value.compareTo(attrValue);
/*     */       }
/*     */     else
/* 150 */       result = this.value.compareTo(attrValue);
/* 151 */     if (this.operator.equals("<"))
/* 152 */       return result > 0;
/* 153 */     if (this.operator.equals("<="))
/* 154 */       return result >= 0;
/* 155 */     if (this.operator.equals(">"))
/* 156 */       return result < 0;
/* 157 */     if (this.operator.equals(">="))
/* 158 */       return result <= 0;
/* 159 */     if (this.operator.equals("<>")) {
/* 160 */       return result != 0;
/*     */     }
/* 162 */     return result == 0;
/*     */   }
/*     */ 
/*     */   public final int getOperatorIndex()
/*     */   {
/* 171 */     for (int i = 0; i < ALL_OPERATORS.length; ++i)
/* 172 */       if (ALL_OPERATORS[i].equals(this.operator))
/* 173 */         return i;
/* 174 */     return -1;
/*     */   }
/*     */ 
/*     */   public final boolean isOperator(String operator)
/*     */   {
/* 184 */     for (int i = 0; i < ALL_OPERATORS.length; ++i)
/* 185 */       if (ALL_OPERATORS[i].equals(operator))
/* 186 */         return true;
/* 187 */     return false;
/*     */   }
/*     */ 
/*     */   public final boolean equals(Object obj) {
/* 191 */     if (obj instanceof AttributeConstraint) {
/* 192 */       AttributeConstraint other = (AttributeConstraint)obj;
/*     */ 
/* 195 */       return (this.attrId.equals(other.attrId)) && 
/* 194 */         (this.operator.equals(other.operator)) && 
/* 195 */         (this.value.equals(other.value));
/*     */     }
/* 197 */     return false;
/*     */   }
/*     */ 
/*     */   public final int hashCode() {
/* 201 */     int hc = 23;
/* 202 */     hc += 37 * this.attrId.hashCode();
/* 203 */     hc += 37 * this.operator.hashCode();
/* 204 */     hc += 37 * this.value.hashCode();
/* 205 */     return hc;
/*     */   }
/*     */ 
/*     */   public final void bind(Subset2 subset)
/*     */   {
/* 214 */     this.subset = subset;
/* 215 */     markDirty();
/*     */   }
/*     */ 
/*     */   public final void unbind()
/*     */   {
/* 222 */     this.subset = null;
/*     */   }
/*     */ 
/*     */   private final void markDirty() {
/* 226 */     if (this.subset != null)
/* 227 */       this.subset.modified();
/*     */   }
/*     */ }

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name:     org.palo.api.subsets.filter.settings.AttributeConstraint
 * JD-Core Version:    0.5.4
 */