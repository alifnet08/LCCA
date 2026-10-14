package com.wo.module.notary.service;

public class DuplicateNotaryPhoneException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public DuplicateNotaryPhoneException(String phoneNo) {
		super("More than one notary uses phone number " + phoneNo);
	}
}
