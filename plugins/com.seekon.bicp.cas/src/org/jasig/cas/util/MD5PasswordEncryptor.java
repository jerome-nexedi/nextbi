package org.jasig.cas.util;

import java.security.MessageDigest;

import sun.misc.BASE64Encoder;

public class MD5PasswordEncryptor {

  public static final String encrypt(String pass) {
    if ((pass == null) || (pass.equals(""))) {
      return "";
    }
    MessageDigest md = null;
    try {
      md = MessageDigest.getInstance("MD5");
      md.update(pass.getBytes("UTF-8"));
      byte[] raw = md.digest();
      String hash = new BASE64Encoder().encode(raw);
      return hash;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return pass;
  }
}
