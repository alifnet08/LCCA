package com.wo.module.api.notary;

import org.apache.commons.lang3.StringUtils;

public final class NotaryContactValidator {

	static final int PHONE_MAX_LENGTH = 20;
	static final int EMAIL_MAX_LENGTH = 100;

	private NotaryContactValidator() {
	}

	public static String validate(NotaryContactUpdateRequest request) {
		if (request == null) {
			return "Request body is required";
		}
		if (request.getNotaryId() == null || request.getNotaryId().longValue() <= 0L) {
			return "notaryId must be a positive number";
		}
		String phoneNo = StringUtils.trimToEmpty(request.getPhoneNo());
		if (phoneNo.isEmpty()) {
			return "phoneNo is required";
		}
		if (phoneNo.length() > PHONE_MAX_LENGTH) {
			return "phoneNo must be at most " + PHONE_MAX_LENGTH + " characters";
		}
		if (containsControlCharacter(phoneNo) || !phoneNo.matches(".*\\d.*")) {
			return "phoneNo must contain a digit and no control characters";
		}
		String email = StringUtils.trimToEmpty(request.getEmail());
		if (email.isEmpty()) {
			return "email is required";
		}
		if (email.length() > EMAIL_MAX_LENGTH || containsControlCharacter(email) || email.indexOf(' ') >= 0) {
			return "email must be at most " + EMAIL_MAX_LENGTH + " characters and contain no spaces";
		}
		int at = email.indexOf('@');
		int dot = email.lastIndexOf('.');
		if (at <= 0 || dot < at + 2 || dot >= email.length() - 1 || email.indexOf('@', at + 1) >= 0) {
			return "email format is invalid";
		}
		request.setPhoneNo(phoneNo);
		request.setEmail(email);
		return null;
	}

	private static boolean containsControlCharacter(String value) {
		for (int i = 0; i < value.length(); i++) {
			if (Character.isISOControl(value.charAt(i))) {
				return true;
			}
		}
		return false;
	}
}
