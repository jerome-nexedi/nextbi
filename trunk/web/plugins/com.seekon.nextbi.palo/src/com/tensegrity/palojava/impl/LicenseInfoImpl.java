package com.tensegrity.palojava.impl;

import java.util.Date;

import com.tensegrity.palojava.LicenseInfo;
import com.tensegrity.palojava.PaloException;

public class LicenseInfoImpl implements LicenseInfo {
  private final Date expirationDate;

  private final String licensee;

  private final int numberOfUsers;

  private final int type;

  public LicenseInfoImpl(String[] data) {
    if (data == null || data.length < 4) {
      throw new PaloException("Not enough information to create LicenseInfo.");
    }
    licensee = data[0];
    try {
      numberOfUsers = Integer.parseInt(data[1]);
    } catch (NumberFormatException e) {
      throw new PaloException("Invalid format for number of users in license info.");
    }
    // Multiply with 1000 to convert from Unix timestamp!
    expirationDate = new Date(Long.parseLong(data[2]) * 1000l);
    try {
      type = Integer.parseInt(data[3]);
    } catch (NumberFormatException e) {
      throw new PaloException("Invalid format for type in license info.");
    }
  }

  public Date getExpirationDate() {
    return expirationDate;
  }

  public String getLicensee() {
    return licensee;
  }

  public int getNumberOfUsers() {
    return numberOfUsers;
  }

  public int getType() {
    return type;
  }

}
