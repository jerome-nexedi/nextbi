package com.seekon.bicp.web.deployer;

public class LoadException extends Exception {

	private static final long serialVersionUID = 2567752959983779614L;

	public LoadException() {
    super();
  }

  public LoadException(String message, Throwable cause) {
    super(message, cause);
  }

  public LoadException(String message) {
    super(message);
  }

  public LoadException(Throwable cause) {
    super(cause);
  }

}
