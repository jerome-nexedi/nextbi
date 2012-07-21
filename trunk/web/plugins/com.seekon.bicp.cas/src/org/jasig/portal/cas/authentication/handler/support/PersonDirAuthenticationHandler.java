package org.jasig.portal.cas.authentication.handler.support;

import org.jasig.cas.authentication.handler.AuthenticationException;
import org.jasig.cas.authentication.handler.support.AbstractUsernamePasswordAuthenticationHandler;
import org.jasig.cas.authentication.principal.UsernamePasswordCredentials;
import org.jasypt.digest.config.SimpleDigesterConfig;
import org.jasypt.util.password.ConfigurablePasswordEncryptor;
import org.slf4j.Logger;

public class PersonDirAuthenticationHandler extends
  AbstractUsernamePasswordAuthenticationHandler {
  private static final String MD5_PREFIX = "(MD5)";

  private static final String SHA256_PREFIX = "(SHA256)";

  private UserPasswordDao userPasswordDao;

  private ConfigurablePasswordEncryptor md5Encryptor;

  private ConfigurablePasswordEncryptor sha256Encryptor;

  public PersonDirAuthenticationHandler() {
    this.md5Encryptor = new ConfigurablePasswordEncryptor();
    SimpleDigesterConfig md5Config = new SimpleDigesterConfig();
    md5Config.setIterations(Integer.valueOf(1));
    md5Config.setAlgorithm("MD5");
    md5Config.setSaltSizeBytes(Integer.valueOf(8));
    this.md5Encryptor.setConfig(md5Config);

    this.sha256Encryptor = new ConfigurablePasswordEncryptor();
    SimpleDigesterConfig shaConfig = new SimpleDigesterConfig();
    shaConfig.setIterations(Integer.valueOf(1000));
    shaConfig.setAlgorithm("SHA-256");
    shaConfig.setSaltSizeBytes(Integer.valueOf(8));
    this.sha256Encryptor.setConfig(shaConfig);
  }

  public UserPasswordDao getUserPasswordDao() {
    return this.userPasswordDao;
  }

  public void setUserPasswordDao(UserPasswordDao userPasswordDao) {
    this.userPasswordDao = userPasswordDao;
  }

  protected boolean authenticateUsernamePasswordInternal(
    UsernamePasswordCredentials credentials) throws AuthenticationException {
    String username = credentials.getUsername();
    String cleartextPassword = credentials.getPassword();

    String expectedFullHash = this.userPasswordDao.getPasswordHash(username);

    if (expectedFullHash == null) {
      return false;
    }

    if (expectedFullHash.startsWith("(MD5)")) {
      String hashWithoutAlgorithmPrefix = expectedFullHash.substring(5);
      return this.md5Encryptor.checkPassword(cleartextPassword,
        hashWithoutAlgorithmPrefix);
    }
    if (expectedFullHash.startsWith("(SHA256)")) {
      String hashWithoutAlgorithmPrefix = expectedFullHash.substring(8);
      return this.sha256Encryptor.checkPassword(cleartextPassword,
        hashWithoutAlgorithmPrefix);
    }

    this.log
      .error("Existing password hash for user '"
        + username
        + "' is not a valid hash. It does not start with a supported algorithm prefix");
    return false;
  }
}