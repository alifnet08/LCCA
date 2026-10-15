package com.wo.module.api.notary;

public class NotaryContactNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public NotaryContactNotFoundException(String phoneNo) {
		super("No notary was found for phone number " + phoneNo);
	}
}
