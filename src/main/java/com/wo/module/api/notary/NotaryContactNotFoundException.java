package com.wo.module.api.notary;

public class NotaryContactNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public NotaryContactNotFoundException(Long notaryId) {
		super("Notary " + notaryId + " was not found");
	}
}
