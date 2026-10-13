package com.wo.module.notary.bean;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.wo.module.common.constant.Constants;
import com.wo.module.common.util.LDAPApi;
import com.wo.module.common.utility.CallApiManager;
import com.wo.module.parameter.service.ParameterDetailService;
import com.wo.module.responsibility.model.Responsibility;
import com.wo.module.responsibility.service.ResponsibilityService;
import com.wo.module.user.model.User;
import com.wo.module.user.service.UserService;

public final class NotaryMailSender {

	private NotaryMailSender() {
	}

	public static void sendToRole(UserService userService, ResponsibilityService responsibilityService,
			ParameterDetailService parameterDetailService, String responsibilityName, String subject, String content) {
		try {
			if (userService == null || responsibilityService == null || parameterDetailService == null
					|| StringUtils.isBlank(responsibilityName)) {
				System.out.println("Notary email skipped: service or role is empty");
				return;
			}
			Long responsibilityId = findResponsibilityId(responsibilityService, responsibilityName);
			if (responsibilityId == null) {
				System.out.println("Notary email skipped: role not found " + responsibilityName);
				return;
			}
			List<User> userList = userService.getAllUser();
			Set<String> addresses = new HashSet<String>();
			if (userList != null) {
				for (int i = 0; i < userList.size(); i++) {
					collectAddress(addresses, userList.get(i), responsibilityId, parameterDetailService);
				}
			}
			if (addresses.isEmpty()) {
				System.out.println("Notary email skipped: no address for role " + responsibilityName);
				return;
			}
			String mailSubject = StringUtils.defaultIfBlank(subject, "Notifikasi Notaris");
			String mailContent = toHtml(content);
			for (String address : addresses) {
				try {
					CallApiManager.sendEmailAPI(address, "", mailSubject, mailContent, "NOTARY", "true",
							parameterDetailService);
				} catch (Exception sendError) {
					System.out.println("Notary email failed for " + address + ": " + sendError.getMessage());
					sendError.printStackTrace();
				}
			}
		} catch (Exception e) {
			System.out.println("Notary email failed: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private static Long findResponsibilityId(ResponsibilityService responsibilityService, String responsibilityName) {
		List<Responsibility> responsibilityList = responsibilityService.getAllResponsibility();
		if (responsibilityList == null) {
			return null;
		}
		String expected = normalizeRole(responsibilityName);
		Long fuzzyId = null;
		for (int i = 0; i < responsibilityList.size(); i++) {
			Responsibility responsibility = responsibilityList.get(i);
			if (responsibility == null || responsibility.getResponsibilityId() == null) {
				continue;
			}
			if (StringUtils.equalsIgnoreCase(responsibilityName, StringUtils.trimToEmpty(responsibility.getName()))) {
				return responsibility.getResponsibilityId();
			}
			if (fuzzyId == null && StringUtils.equalsIgnoreCase(expected, normalizeRole(responsibility.getName()))) {
				fuzzyId = responsibility.getResponsibilityId();
			}
		}
		return fuzzyId;
	}

	private static String normalizeRole(String name) {
		String value = StringUtils.trimToEmpty(name);
		if (StringUtils.startsWithIgnoreCase(value, "Role ")) {
			value = value.substring(5).trim();
		}
		return value;
	}

	private static void collectAddress(Set<String> addresses, User user, Long responsibilityId,
			ParameterDetailService parameterDetailService) {
		if (user == null || user.getResponsibilityId() == null || !responsibilityId.equals(user.getResponsibilityId())) {
			return;
		}
		if (user.getDelId() != null && user.getDelId().longValue() != 0L) {
			return;
		}
		if (StringUtils.isNotBlank(user.getEnabledFlag())
				&& !StringUtils.equalsIgnoreCase(Constants.CONSTANT_YES, user.getEnabledFlag())) {
			return;
		}
		String email = StringUtils.trimToEmpty(user.getEmail());
		if (!email.contains("@")) {
			email = lookupLdapEmail(user, parameterDetailService);
		}
		if (!email.contains("@")) {
			return;
		}
		String[] parts = email.split("[,;]");
		for (int i = 0; i < parts.length; i++) {
			String address = StringUtils.trimToEmpty(parts[i]);
			if (address.contains("@")) {
				addresses.add(address);
			}
		}
	}

	private static String lookupLdapEmail(User user, ParameterDetailService parameterDetailService) {
		try {
			String nik = user.getNik();
			if (StringUtils.isBlank(nik) || nik.length() < 5) {
				return "";
			}
			String email = LDAPApi.getEmailByUserID(parameterDetailService, "*" + nik.substring(nik.length() - 5));
			return StringUtils.trimToEmpty(email);
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	private static String toHtml(String content) {
		String text = StringUtils.defaultString(content);
		text = text.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
		text = text.replace("\r\n", "\n").replace("\n", "<br/>");
		return "<div>" + text + "</div>";
	}
}
