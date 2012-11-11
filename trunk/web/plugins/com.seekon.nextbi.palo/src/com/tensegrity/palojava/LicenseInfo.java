/*
*
* @file LicenseInfo.java
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
* @author ArndHouben
*
* @version $Id: LicenseInfo.java,v 1.2 2010/07/19 11:12:29 PhilippBouillon Exp $
*
*/

/*
 * (c) 2007 Tensegrity Software GmbH
 */
package com.tensegrity.palojava;

import java.util.Date;

/**
 * The <code>LicenseInfo</code> interface defines methods which provides
 * detailed information about the license of the used Palo Server. 
 * 
 * @author ArndHouben
 * @version $Id: LicenseInfo.java,v 1.2 2010/07/19 11:12:29 PhilippBouillon Exp $
 */
public interface LicenseInfo {
  /**
   * The name of the licensee of this palo server. 
   * @return name of the licensee
   */
  public String getLicensee();

  /**
   * Returns the number of users that may use this palo server version.
   * @return number of users
   */
  public int getNumberOfUsers();

  /**
   * Returns the expiration date of this license.
   * @return expiration date of the license
   */
  public Date getExpirationDate();

  /**
   * Returns the type of this license.
   * @return license type
   */
  public int getType();
}
