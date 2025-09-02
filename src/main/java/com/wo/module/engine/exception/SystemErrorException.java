package com.wo.module.engine.exception;

/**
 *
 * This exception is used to mark failures in System and code error.
 *
 *
 *
 * @author WO
 *
 */

public class SystemErrorException extends RuntimeException {
	private static final long serialVersionUID = -46218102922895529L;

	public SystemErrorException() {
		super();
	}

	public SystemErrorException(String message) {
		super(message);
	}

	public SystemErrorException(String message, Throwable cause) {
		super(message, cause);
	}

	public SystemErrorException(Throwable cause) {
		super(cause);
	}

}
