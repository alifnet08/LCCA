package com.wo.module.notary;

import java.util.ArrayList;
import java.util.List;

import com.wo.module.parameter.model.ParameterDetail;

public final class NotaryCategories {

	public static final int MAX_LENGTH = 1000;

	private NotaryCategories() {
	}

	public static String[] split(String stored) {
		if (stored == null || stored.trim().length() == 0) {
			return new String[0];
		}
		String[] raw = stored.split(",");
		List<String> codes = new ArrayList<String>();
		for (int i = 0; i < raw.length; i++) {
			String code = raw[i] == null ? "" : raw[i].trim();
			if (code.length() > 0 && !codes.contains(code)) {
				codes.add(code);
			}
		}
		return codes.toArray(new String[codes.size()]);
	}

	public static String join(String[] codes) {
		if (codes == null || codes.length == 0) {
			return "";
		}
		StringBuilder joined = new StringBuilder();
		for (int i = 0; i < codes.length; i++) {
			String code = codes[i] == null ? "" : codes[i].trim();
			if (code.length() == 0 || contains(joined, code)) {
				continue;
			}
			if (joined.length() > 0) {
				joined.append(',');
			}
			joined.append(code);
		}
		return joined.toString();
	}

	public static String primary(String stored) {
		String[] codes = split(stored);
		return codes.length == 0 ? "" : codes[0];
	}

	public static String labels(List<ParameterDetail> details, String stored) {
		String[] codes = split(stored);
		StringBuilder label = new StringBuilder();
		for (int i = 0; i < codes.length; i++) {
			String name = nameOf(details, codes[i]);
			if (label.length() > 0) {
				label.append(", ");
			}
			label.append(name);
		}
		return label.toString();
	}

	private static String nameOf(List<ParameterDetail> details, String code) {
		if (details != null) {
			for (int i = 0; i < details.size(); i++) {
				ParameterDetail detail = details.get(i);
				if (detail != null && code.equals(detail.getParameterDtlCode()) && detail.getNameIn() != null
						&& detail.getNameIn().trim().length() > 0) {
					return detail.getNameIn().trim();
				}
			}
		}
		return code;
	}

	private static boolean contains(StringBuilder joined, String code) {
		String[] existing = split(joined.toString());
		for (int i = 0; i < existing.length; i++) {
			if (code.equals(existing[i])) {
				return true;
			}
		}
		return false;
	}
}
