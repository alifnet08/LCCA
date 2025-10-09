package com.wo.module.common.exception;

import com.wo.module.common.model.UploadedFileWO;

/**
 *
 * This exception is used to mark failures in System and code error.
 *
 *
 *
 * @author WO
 *
 */

public class CustomAPIException extends RuntimeException {
	private UploadedFileWO ufw;
	private static final long serialVersionUID = -2910074616081427766L;

	public CustomAPIException() {
		super();
	}

	public CustomAPIException(String message) {
		super(message);
	}

	public CustomAPIException(String message, Throwable cause) {
		super(message, cause);
	}

	public CustomAPIException(Throwable cause) {
		super(cause);
	}
	
	public CustomAPIException(String message, UploadedFileWO ufw) {
		super(message);
		this.ufw = ufw;
	}

	public UploadedFileWO getUfw() {
		return ufw;
	}

	public void setUfw(UploadedFileWO ufw) {
		this.ufw = ufw;
	}

}
