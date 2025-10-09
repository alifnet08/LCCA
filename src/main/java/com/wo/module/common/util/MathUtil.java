package com.wo.module.common.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;

import com.wo.module.common.constant.CommonConstants;

public class MathUtil {
	private static MathUtil SELF_INSTANCE;
	private static Locale thisLocale = new Locale("en", "US");

	public static MathUtil getInstance() {
		if (SELF_INSTANCE == null) {
			SELF_INSTANCE = new MathUtil();
		}

		return SELF_INSTANCE;
	}

	public double round(double original, double fractionDigits) {
		double result = original;

		result = Math.round(original * Math.pow(10, fractionDigits));
		result /= Math.pow(10, fractionDigits);

		return result;
	}

	public double floor(double original, double fractionDigits) {
		double result = original;

		result = Math.floor(original * Math.pow(10, fractionDigits));
		result /= Math.pow(10, fractionDigits);

		return result;
	}

	public double ceil(double original, double fractionDigits) {
		double result = original;

		result = Math.ceil(original * Math.pow(10, fractionDigits));
		result /= Math.pow(10, fractionDigits);

		return result;
	}

	public static String formatToIndonesian(BigDecimal value) {
		if (value != null) {
			DecimalFormatSymbols indonesianSymbols = new DecimalFormatSymbols(thisLocale);
			indonesianSymbols.setDecimalSeparator(',');
			indonesianSymbols.setGroupingSeparator('.');
			String usPattern = CommonConstants.US_CURRENCY_PATTERN;
			DecimalFormat indonesianFormatter = new DecimalFormat(usPattern, indonesianSymbols);
			return indonesianFormatter.format(value);
		}
		return "";
	}

	public static String formatToUS(BigDecimal value) {
		if (value != null) {
			NumberFormat nf = NumberFormat.getNumberInstance(thisLocale);
			DecimalFormat df = (DecimalFormat) nf;
			df.applyPattern(CommonConstants.US_CURRENCY_PATTERN);
			return df.format(value);
		}
		return "";
	}

	private static String satuan(int value) {
		String satuan = "";
		switch (value) {
		case 0:
			satuan = "Nol";
			break;
		case 1:
			satuan = "Satu";
			break;
		case 2:
			satuan = "Dua";
			break;
		case 3:
			satuan = "Tiga";
			break;
		case 4:
			satuan = "Empat";
			break;
		case 5:
			satuan = "Lima";
			break;
		case 6:
			satuan = "Enam";
			break;
		case 7:
			satuan = "Tujuh";
			break;
		case 8:
			satuan = "Delapan";
			break;
		case 9:
			satuan = "Sembilan";
			break;
		default:
			break;
		}
		return satuan;
	}

	private static String puluhan(int value) {
		String msg = "";
		switch (value) {
		case 10:
			msg = "Sepuluh";
			break;
		case 11:
			msg = "Sebelas";
			break;
		case 12:
			msg = "Dua Belas";
			break;
		case 13:
			msg = "Tiga Belas";
			break;
		case 14:
			msg = "Empat Belas";
			break;
		case 15:
			msg = "Lima Belas";
			break;
		case 16:
			msg = "Enam Belas";
			break;
		case 17:
			msg = "Tujuh Belas";
			break;
		case 18:
			msg = "Delapan Belas";
			break;
		case 19:
			msg = "Sembilan Belas";
			break;
		case 20:
			msg = "Dua Puluh";
			break;
		case 30:
			msg = "Tiga Puluh";
			break;
		case 40:
			msg = "Empat Puluh";
			break;
		case 50:
			msg = "Lima Puluh";
			break;
		case 60:
			msg = "Enam Puluh";
			break;
		case 70:
			msg = "Tujuh Puluh";
			break;
		case 80:
			msg = "Delapan Puluh";
			break;
		case 90:
			msg = "Sembilan Puluh";
			break;
		}
		if (msg.isEmpty()) {
			// get puluhan
			if (value >= 10) {
				String puluhan = puluhan(((int) (value / 10)) * 10);
				if (!puluhan.isEmpty()) {
					msg = puluhan + " " + satuan(value % 10);
				} else {
					msg = satuan(value % 10);
				}
			} else {
				msg = satuan(value);
			}
		}
		return msg;
	}

	private static String ratusan(int value) {
		String msg = "";
		switch (value) {
		case 100:
			msg = "Seratus";
			break;
		case 200:
			msg = "Dua Ratus";
			break;
		case 300:
			msg = "Tiga Ratus";
			break;
		case 400:
			msg = "Empat Ratus";
			break;
		case 500:
			msg = "Lima Ratus";
			break;
		case 600:
			msg = "Enam Ratus";
			break;
		case 700:
			msg = "Tujuh Ratus";
			break;
		case 800:
			msg = "Delapan Ratus";
			break;
		case 900:
			msg = "Sembilan Ratus";
			break;
		}
		if (msg.isEmpty()) {
			if (value >= 100) {
				// get ratusan
				String ratusan = ratusan(((int) (value / 100)) * 100);
				if (!ratusan.isEmpty()) {
					msg = ratusan + " " + puluhan(value % 100);
				} else {
					msg = puluhan(value % 100);
				}
			} else {
				msg = puluhan(value);
			}
		}
		return msg;
	}

	public static String terbilang(long value) {
		StringBuffer sb = new StringBuffer();

		if (value < 10)
			return satuan((int) value);

		if (value < 100)
			return puluhan((int) value);

		// check for triliun, 1000000000000
		int temp = (int) (value / 1000000000000l);
		if (temp > 0) {
			if (sb.length() > 1)
				sb.append(" ");
			sb.append(ratusan(temp)).append(" ").append("Trilyun");
		}
		value = value % 1000000000000l;

		// check for milyar, 1000000000
		temp = (int) (value / 1000000000l);
		if (temp > 0) {
			if (sb.length() > 1)
				sb.append(" ");
			sb.append(ratusan(temp)).append(" ").append("Milyar");
		}
		value = value % 1000000000l;

		// check for juta, 1000000
		temp = (int) (value / 1000000l);
		if (temp > 0) {
			if (sb.length() > 1)
				sb.append(" ");
			sb.append(ratusan(temp)).append(" ").append("Juta");
		}
		value = value % 1000000l;

		// check for ribu, 1000
		temp = (int) (value / 1000l);
		if (temp == 1) {
			if (sb.length() > 1)
				sb.append(" ");
			sb.append("Seribu");
		} else if (temp > 0) {
			if (sb.length() > 1)
				sb.append(" ");
			sb.append(ratusan(temp)).append(" ").append("Ribu");
		}
		value = value % 1000l;

		// check for satuan, 1
		temp = (int) value;
		if (temp > 0) {
			if (sb.length() > 1)
				sb.append(" ");
			sb.append(ratusan(temp));
		}
		return sb.toString();
	}

	public static String terbilang(double value) {
		return terbilang(Math.round(Math.abs(value)));
	}

	public static String terbilang(BigDecimal value) {
		if (value != null)
			return terbilang(value.abs().longValue());
		else
			return "";
	}

	public static String fmt(Double d) {
		if (d != null) {
			if (d == (int) d.doubleValue())
				return String.format("%d", (int) d.doubleValue());
			else
				return String.format("%s", d);
		}
		return "";
	}

	public static String fmtCurrency(Double d) {
		if (d != null) {
			NumberFormat format = NumberFormat.getCurrencyInstance(Locale.GERMAN);
			String res = format.format(d);
			return StringUtils.removeEnd(res.substring(2), ",00");
		}
		return "";
	}

	public static Long returnIdObjectToLong(Object obj) {
		if (obj != null) {
			if (obj instanceof Number) {
				if (obj instanceof BigDecimal) {
					return ((BigDecimal) obj).longValue();
				} else if (obj instanceof BigInteger) {
					return ((BigInteger) obj).longValue();
				} else if (obj instanceof Double) {
					return ((Double) obj).longValue();
				} else if (obj instanceof Long) {
					return ((Long) obj).longValue();
				} else if (obj instanceof Float) {
					return ((Float) obj).longValue();
				} else if (obj instanceof Integer) {
					return ((Integer) obj).longValue();
				}
			}
		}
		return null;
	}
	
	public static Integer returnIdObjectToInteger(Object obj) {
		if (obj != null) {
			if (obj instanceof Number) {
				if (obj instanceof BigDecimal) {
					return ((BigDecimal) obj).intValue();
				} else if (obj instanceof BigInteger) {
					return ((BigInteger) obj).intValue();
				} else if (obj instanceof Double) {
					return ((Double) obj).intValue();
				} else if (obj instanceof Long) {
					return ((Long) obj).intValue();
				} else if (obj instanceof Float) {
					return ((Float) obj).intValue();
				} else if (obj instanceof Integer) {
					return ((Integer) obj).intValue();
				}
			}
		}
		return null;
	}
	
	public static boolean intContains(final int[] arr, final int key) {
	    return Arrays.stream(arr).anyMatch(i -> i == key);
	}
}
