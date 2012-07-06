package com.seekon.bicp.web.deployer.def;

public class IllegalConfigurationException extends RuntimeException {

	private static final long serialVersionUID = 4265965438940811330L;

	public IllegalConfigurationException() {
    super();
  }

  public IllegalConfigurationException(String message, Throwable cause) {
    super(message, cause);
  }

  public IllegalConfigurationException(String message) {
    super(message);
  }

  public IllegalConfigurationException(Throwable cause) {
    super(cause);
  }

}
