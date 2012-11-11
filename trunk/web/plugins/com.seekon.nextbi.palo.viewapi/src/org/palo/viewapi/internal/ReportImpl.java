/*     */package org.palo.viewapi.internal;

/*     */
/*     */import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.palo.viewapi.Report;
import org.palo.viewapi.User;
import org.palo.viewapi.View;
import org.palo.viewapi.internal.dbmappers.MapperRegistry;

/*     */
/*     */public final class ReportImpl extends GuardedObjectImpl
/*     */implements Report
/*     */{
  /*     */private String name;

  /*     */private String description;

  /* 59 */private final Set<String> views = new HashSet();

  /*     */
  /*     */ReportImpl(String id) {
    /* 62 */this(id, null);
    /*     */}

  /*     */
  /*     */ReportImpl(String id, String name) {
    /* 66 */super(id);
    /* 67 */this.name = name;
    /*     */}

  /*     */
  /*     */private ReportImpl(Builder builder) {
    /* 71 */super(builder.id);
    /* 72 */this.name = builder.name;
    /* 73 */this.description = builder.description;
    /* 74 */this.owner = builder.owner;
    /* 75 */this.views.addAll(builder.views);
    /* 76 */setRoles(builder.roles);
    /*     */}

  /*     */
  /*     */public final String getDescription()
  /*     */{
    /* 81 */return this.description;
    /*     */}

  /*     */
  /*     */public final String getName() {
    /* 85 */return this.name;
    /*     */}

  /*     */
  /*     */public final List<View> getViews() {
    /* 89 */IViewManagement viewMgmt =
    /* 90 */MapperRegistry.getInstance().getViewManagement();
    /* 91 */List views = new ArrayList();
    /* 92 */for (String id : this.views)
      /*     */try {
        /* 94 */View view = (View) viewMgmt.find(id);
        /* 95 */if ((view != null) && (!views.contains(view)))
          /* 96 */views.add(view);
        /*     */}
      /*     */catch (SQLException localSQLException)
      /*     */{
        /*     */}
    /* 101 */return views;
    /*     */}

  /*     */public final List<String> getViewIDs() {
    /* 104 */return new ArrayList(this.views);
    /*     */}

  /*     */public final boolean contains(View view) {
    /* 107 */return this.views.contains(view.getId());
    /*     */}

  /*     */
  /*     */final void setDescription(String description)
  /*     */{
    /* 115 */this.description = description;
    /*     */}

  /*     */final void add(View view) {
    /* 118 */this.views.add(view.getId());
    /*     */}

  /*     */final void remove(View view) {
    /* 121 */this.views.remove(view.getId());
    /*     */}

  /*     */final void setName(String name) {
    /* 124 */this.name = name;
    /*     */}

  /*     */final void setViews(List<String> views) {
    /* 127 */this.views.clear();
    /* 128 */if (views != null)
      /* 129 */this.views.addAll(views);
    /*     */}

  /*     */
  /*     */public static final class Builder
  /*     */{
    /*     */private final String id;

    /*     */private String name;

    /*     */private String description;

    /*     */private User owner;

    /* 142 */private final Set<String> roles = new HashSet();

    /* 143 */private final Set<String> views = new HashSet();

    /*     */
    /*     */public Builder(String id) {
      /* 146 */AccessController.checkAccess(Report.class);
      /* 147 */this.id = id;
      /*     */}

    /*     */
    /*     */public Builder description(String description) {
      /* 151 */this.description = description;
      /* 152 */return this;
      /*     */}

    /*     */public Builder name(String name) {
      /* 155 */this.name = name;
      /* 156 */return this;
      /*     */}

    /*     */public Builder owner(User owner) {
      /* 159 */this.owner = owner;
      /* 160 */return this;
      /*     */}

    /*     */public Builder roles(List<String> roles) {
      /* 163 */this.roles.clear();
      /* 164 */if (roles != null)
        /* 165 */this.roles.addAll(roles);
      /* 166 */return this;
      /*     */}

    /*     */public Builder views(List<String> views) {
      /* 169 */this.views.clear();
      /* 170 */if (views != null)
        /* 171 */this.views.addAll(views);
      /* 172 */return this;
      /*     */}

    /*     */public Report build() {
      /* 175 */return new ReportImpl(this);
      /*     */}
    /*     */
  }
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi
 * .jar Qualified Name: org.palo.viewapi.internal.ReportImpl JD-Core Version:
 * 0.5.4
 */