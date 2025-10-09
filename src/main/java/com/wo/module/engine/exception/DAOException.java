package com.wo.module.engine.exception;

/**
 * Copyright (c) WO
 *
 * @author WO
 */
public class DAOException extends RuntimeException {
	private static final long serialVersionUID = 2789934764783488157L;

	public DAOException() {
		super();
	}

	public DAOException(String message) {
		super(message);
	}

	public DAOException(String message, Throwable cause) {
		super(message, cause);
	}

	public DAOException(Throwable cause) {
		super(cause);
	}

}
