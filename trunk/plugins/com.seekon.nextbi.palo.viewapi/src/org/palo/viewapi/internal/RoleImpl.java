/*     */package org.palo.viewapi.internal;

/*     */
/*     */import org.palo.viewapi.Right; /*     */
import org.palo.viewapi.Role;

/*     */
/*     */public final class RoleImpl extends DomainObjectImpl
/*     */implements Role
/*     */{
  /*     */private Right right;

  /*     */private String name;

  /*     */private String descr;

  /*     */
  /*     */RoleImpl(String id)
  /*     */{
    /* 67 */super(id);
    /*     */
    /* 69 */this.right = Right.NONE;
    /*     */}

  /*     */
  /*     */private RoleImpl(Builder builder) {
    /* 73 */super(builder.id);
    /* 74 */this.name = builder.name;
    /* 75 */this.right = builder.right;
    /* 76 */this.descr = builder.descr;
    /*     */}

  /*     */
  /*     */public final String getDescription()
  /*     */{
    /* 87 */return this.descr;
    /*     */}

  /*     */
  /*     */public final String getName()
  /*     */{
    /* 136 */return this.name;
    /*     */}

  /*     */
  /*     */public final Right getPermission() {
    /* 140 */return this.right;
    /*     */}

  /*     */
  /*     */public final boolean hasPermission(Right right) {
    /* 144 */return this.right.getPriority() >= right.getPriority();
    /*     */}

  /*     */
  /*     */public final void setDescription(String descr)
  /*     */{
    /* 199 */this.descr = descr;
    /*     */}

  /*     */
  /*     */final void setName(String name)
  /*     */{
    /* 209 */this.name = name;
    /*     */}

  /*     */final void setPermission(Right right) {
    /* 212 */this.right = right;
    /*     */}

  /*     */
  /*     */public static final class Builder
  /*     */{
    /*     */private final String id;

    /*     */private Right right;

    /*     */private String name;

    /*     */private String descr;

    /*     */
    /*     */public Builder(String id)
    /*     */{
      /* 236 */AccessController.checkAccess(Role.class);
      /* 237 */this.id = id;
      /*     */}

    /*     */
    /*     */public final Builder name(String name) {
      /* 241 */this.name = name;
      /* 242 */return this;
      /*     */}

    /*     */public final Builder permission(Right right) {
      /* 245 */this.right = right;
      /* 246 */return this;
      /*     */}

    /*     */public final Builder description(String descr) {
      /* 249 */this.descr = descr;
      /* 250 */return this;
      /*     */}

    /*     */
    /*     */public final Role build()
    /*     */{
      /* 265 */return new RoleImpl(this);
      /*     */}
    /*     */
  }
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi
 * .jar Qualified Name: org.palo.viewapi.internal.RoleImpl JD-Core Version:
 * 0.5.4
 */