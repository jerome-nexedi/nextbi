/*
// $Id: //open/mondrian/src/main/mondrian/olap/HierarchyBase.java#35 $
// This software is subject to the terms of the Eclipse Public License v1.0
// Agreement, available at the following URL:
// http://www.eclipse.org/legal/epl-v10.html.
// Copyright (C) 2001-2002 Kana Software, Inc.
// Copyright (C) 2001-2011 Julian Hyde and others
// All Rights Reserved.
// You must accept the terms of that agreement to use this software.
 */
package mondrian.olap;

import mondrian.resource.MondrianResource;

/**
 * Skeleton implementation for {@link Hierarchy}.
 * 
 * @author jhyde
 * @since 6 August, 2001
 * @version $Id: //open/mondrian/src/main/mondrian/olap/HierarchyBase.java#35 $
 */
public abstract class HierarchyBase extends OlapElementBase implements Hierarchy {

  protected final Dimension dimension;

  /**
   * <code>name</code> and <code>subName</code> are the name of the hierarchy,
   * respectively containing and not containing dimension name. For example:
   * <table>
   * <tr>
   * <th>uniqueName</th>
   * <th>name</th>
   * <th>subName</th>
   * </tr>
   * <tr>
   * <td>[Time.Weekly]</td>
   * <td>Time.Weekly</td>
   * <td>Weekly</td>
   * </tr>
   * <tr>
   * <td>[Customers]</td>
   * <td>Customers</td>
   * <td>null</td>
   * </tr>
   * </table>
   * 
   * <p>
   * If {@link mondrian.olap.MondrianProperties#SsasCompatibleNaming} is true,
   * name and subName have the same value.
   */
  protected final String subName;

  protected final String name;

  protected final String uniqueName;

  protected String description;

  protected Level[] levels;

  protected final boolean hasAll;

  protected String allMemberName;

  protected String allLevelName;

  protected HierarchyBase(Dimension dimension, String subName, String caption,
    boolean visible, String description, boolean hasAll) {
    this.dimension = dimension;
    this.hasAll = hasAll;
    if (caption != null) {
      this.caption = caption;
    } else if (subName == null) {
      this.caption = dimension.getCaption();
    } else {
      this.caption = subName;
    }
    this.description = description;
    this.visible = visible;

    String name = dimension.getName();
    if (MondrianProperties.instance().SsasCompatibleNaming.get()) {
      if (subName == null) {
        // e.g. "Time"
        subName = name;
      }
      this.subName = subName;
      this.name = subName;
      // e.g. "[Time].[Weekly]" for dimension "Time", hierarchy "Weekly";
      // "[Time]" for dimension "Time", hierarchy "Time".
      this.uniqueName = subName.equals(name) ? dimension.getUniqueName() : Util
        .makeFqName(dimension, this.name);
    } else {
      this.subName = subName;
      if (this.subName != null) {
        // e.g. "Time.Weekly"
        this.name = name + "." + subName;
        if (this.subName.equals(name)) {
          this.uniqueName = dimension.getUniqueName();
        } else {
          // e.g. "[Time.Weekly]"
          this.uniqueName = Util.makeFqName(this.name);
        }
      } else {
        // e.g. "Time"
        this.name = name;
        // e.g. "[Time]"
        this.uniqueName = dimension.getUniqueName();
      }
    }
  }

  /**
   * Returns the name of the hierarchy sans dimension name.
   * 
   * @return name of hierarchy sans dimension name
   */
  public String getSubName() {
    return subName;
  }

  // implement MdxElement
  public String getUniqueName() {
    return uniqueName;
  }

  public String getUniqueNameSsas() {
    return Util.makeFqName(dimension, name);
  }

  public String getName() {
    return name;
  }

  public String getQualifiedName() {
    return MondrianResource.instance().MdxHierarchyName.str(getUniqueName());
  }

  public abstract boolean isRagged();

  public String getDescription() {
    return description;
  }

  public Dimension getDimension() {
    return dimension;
  }

  public Level[] getLevels() {
    return levels;
  }

  public Hierarchy getHierarchy() {
    return this;
  }

  public boolean hasAll() {
    return hasAll;
  }

  public boolean equals(OlapElement mdxElement) {
    // Use object identity, because a private hierarchy can have the same
    // name as a public hierarchy.
    return (this == mdxElement);
  }

  public OlapElement lookupChild(SchemaReader schemaReader, Id.Segment s,
    MatchType matchType) {
    OlapElement oe = Util.lookupHierarchyLevel(this, s.name);
    if (oe == null) {
      oe = Util.lookupHierarchyRootMember(schemaReader, this, s, matchType);
    }
    if (getLogger().isDebugEnabled()) {
      StringBuilder buf = new StringBuilder(64);
      buf.append("HierarchyBase.lookupChild: ");
      buf.append("name=");
      buf.append(getName());
      buf.append(", childname=");
      buf.append(s);
      if (oe == null) {
        buf.append(" returning null");
      } else {
        buf.append(" returning elementname=").append(oe.getName());
      }
      getLogger().debug(buf.toString());
    }
    return oe;
  }

  public String getAllMemberName() {
    return allMemberName;
  }

  /**
   * Returns the name of the 'all' level in this hierarchy.
   * 
   * @return name of the 'all' level
   */
  public String getAllLevelName() {
    return allLevelName;
  }
}

// End HierarchyBase.java
