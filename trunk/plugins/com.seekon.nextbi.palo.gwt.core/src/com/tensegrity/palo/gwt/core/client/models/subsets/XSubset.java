/*
 *
 * @file XSubset.java
 *
 * Copyright (C) 2006-2009 Tensegrity Software GmbH
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License (Version 2) as published
 * by the Free Software Foundation at http://www.gnu.org/copyleft/gpl.html.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 *
 * If you are developing and distributing open source applications under the
 * GPL License, then you are free to use JPalo Modules under the GPL License.  For OEMs,
 * ISVs, and VARs who distribute JPalo Modules with their products, and do not license
 * and distribute their source code under the GPL, Tensegrity provides a flexible
 * OEM Commercial License.
 *
 * @author Philipp Bouillon <Philipp.Bouillon@tensegrity-software.com>
 *
 * @version $Id: XSubset.java,v 1.3 2009/12/17 16:14:29 PhilippBouillon Exp $
 *
 */

/*
 * (c) Tensegrity Software 2009
 * All rights reserved
 */
package com.tensegrity.palo.gwt.core.client.models.subsets;

import com.tensegrity.palo.gwt.core.client.models.XObject;
import com.tensegrity.palo.gwt.core.client.models.palo.XHierarchy;

/**
 * <code>XSubset</code> TODO DOCUMENT ME
 * 
 * @version $Id: XSubset.java,v 1.3 2009/12/17 16:14:29 PhilippBouillon Exp $
 **/
public class XSubset extends XObject {

  public static final String TYPE = XSubset.class.getName();

  private XHierarchy xHierarchy;

  private XSubsetType subsetType;

  /** <b>DO NOT USE! FOR SERIALIZATION ONLY!! </b> */
  public XSubset() {

  }

  public XSubset(String id, String name, XSubsetType subsetType) {
    setId(id);
    setName(name);
    this.subsetType = subsetType;
  }

  public XHierarchy getHierarchy() {
    return xHierarchy;
  }

  public XSubsetType getSubsetType() {
    return subsetType;
  }

  public String getType() {
    return TYPE;
  }

  public void setHierarchy(XHierarchy hierarchy) {
    this.xHierarchy = hierarchy;
  }

}
