package org.osgi.service.http;

public class NamespaceException extends Exception {
	static final long serialVersionUID = 7235606031147877747L;
	private Throwable cause;

	public NamespaceException(String message) {
		super(message);
		this.cause = null;
	}

	public NamespaceException(String message, Throwable cause) {
		super(message);
		this.cause = cause;
	}

	public Throwable getException() {
		return this.cause;
	}

	public Throwable getCause() {
		return this.cause;
	}

	public Throwable initCause(Throwable cause) {
		throw new IllegalStateException();
	}
}