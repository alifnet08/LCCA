package com.wo.module.common.util;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Months;
import org.joda.time.Years;

import com.wo.module.common.constant.CommonConstants;

public class DateUtil {
	private static DateUtil SELF_INSTANCE;
	private static final SimpleDateFormat dateTimeFormatter = new SimpleDateFormat(
			CommonConstants.INPUT_DATE_TIME_FORMAT);
	private static final SimpleDateFormat dateFormatter = new SimpleDateFormat(
			CommonConstants.INPUT_DATE_FORMAT);
	private static final SimpleDateFormat globalformatter = new SimpleDateFormat(
			CommonConstants.GLOBALDATEFORMAT);
	private static final SimpleDateFormat reverseFormatter = new SimpleDateFormat(
			CommonConstants.DB_DATE_FORMAT2);
	private static final SimpleDateFormat reverseFormatter2 = new SimpleDateFormat(
			CommonConstants.COMMONDATEFORMAT);

	public static enum DateValue {
		DAY_VALUE, DATE_VALUE, MONTH_IN_MONTH_VALUE, YEAR_VALUE, DAY_IND_VALUE, MONTH_IN_MM_VALUE, MONTH_IN_MONTH_IND_VALUE, MONTH_IN_MON_VALUE, DATE_TERBILANG, MONTH_TERBILANG, YEAR_TERBILANG
	}

	public static enum DateType {
		DAYS, MONTH, YEAR
	}

	public static DateUtil getInstance() {
		if (SELF_INSTANCE == null) {
			SELF_INSTANCE = new DateUtil();
		}

		return SELF_INSTANCE;
	}

	public static String dateTimeToString(Date inputDate) {
		String result = "";
		if (inputDate != null)
			result = dateTimeFormatter.format(inputDate);
		return result;
	}

	public static String dateToString(Date inputDate) {
		String result = "";
		if (inputDate != null)
			result = dateFormatter.format(inputDate);
		return result;
	}

	public static String dateToString(Date inputDate, String dateFormat) {
		String result = "";
		if (inputDate != null) {
			if (dateFormat.equals(CommonConstants.DOCUMENT_DATE_FORMAT_IND)) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(inputDate);
				result = String.valueOf(cal.get(Calendar.DATE)) + " "
						+ getMonthInIndonesian(cal.get(Calendar.MONTH)) + " "
						+ String.valueOf(cal.get(Calendar.YEAR));
			} else if (dateFormat
					.equals(CommonConstants.DOCUMENT_DATE_DAY_FORMAT_IND)) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(inputDate);
				result = getDayInIndonesian(cal.get(Calendar.DAY_OF_WEEK))
						+ ", " + String.valueOf(cal.get(Calendar.DATE)) + " "
						+ getMonthInIndonesian(cal.get(Calendar.MONTH)) + " "
						+ String.valueOf(cal.get(Calendar.YEAR));
			} else if (dateFormat
					.equals(CommonConstants.DOCUMENT_DATE_FORMAT_HARI)) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(inputDate);
				result = getDayInIndonesian(cal.get(Calendar.DAY_OF_WEEK));
			} else if (dateFormat
					.equals(CommonConstants.DOCUMENT_DATE_FORMAT_BULAN)) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(inputDate);
				result = getMonthInIndonesian(cal.get(Calendar.MONTH));
			} else {
				SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
				result = sdf.format(inputDate);
			}
		}

		return result;
	}

	/**
	 * 
	 * @param input
	 *            should in format HH:mm:ss
	 * @param time
	 *            shoulde be either Calendar.HOUR_OF_DAY or Calendar.MINUTE or
	 *            Calendar.SECOND
	 * @return
	 * @throws Exception
	 */
	public static int getHourMinuteSecond(String input, int time)
			throws Exception {
		int result = 0;
		boolean isValidTime = true;
		if (input.indexOf(":") > 0) {
			switch (time) {
			case Calendar.HOUR_OF_DAY:
				result = Integer
						.valueOf(input.substring(0, input.indexOf(":")))
						.intValue();
				if (result > 23)
					isValidTime = false;
				break;
			case Calendar.MINUTE:
				result = Integer.valueOf(
						input.substring(input.indexOf(":") + 1,
								input.indexOf(":") + 3)).intValue();
				if (result > 59)
					isValidTime = false;
				break;
			case Calendar.SECOND:
				if (input.indexOf(":") < input.lastIndexOf(":"))
					result = Integer.valueOf(
							input.substring(input.lastIndexOf(":") + 1))
							.intValue();
				else
					result = 100;
				if (result > 59)
					isValidTime = false;
				break;
			default:
				result = 0;
				break;
			}
		} else
			isValidTime = false;
		if (!isValidTime)
			throw new Exception(CommonConstants.INVALID_TIME_FORMAT_CONSTANT);
		return result;
	}

	public static Date stringToDateFromDDMMYYYY(String date)
			throws ParseException {
		Date result = null;
		if (date != null && (date.trim().length() > 0)) {
			result = globalformatter.parse(date);
		}
		return result;
	}

	public static Date stringToDateFromDDMMMYYYY(String date)
			throws ParseException {
		Date result = null;
		if (date != null && (date.trim().length() > 0)) {
			result = dateFormatter.parse(date);
		}
		return result;
	}
	
	public static Date stringToDateFromYYYYMMDD(String date)
			throws ParseException {
		Date result = null;
		if (date != null && (date.trim().length() > 0)) {
			result = reverseFormatter.parse(date);
		}
		return result;
	}

	public static java.sql.Date dateToSqlDate(java.util.Date dateInput) {
		if (dateInput != null)
			return (new java.sql.Date(dateInput.getTime()));
		else
			return null;
	}

	public static java.sql.Timestamp dateToSqlTimestamp(java.util.Date dateInput) {
		if (dateInput != null)
			return (new java.sql.Timestamp(dateInput.getTime()));
		else
			return null;
	}

	public static java.sql.Timestamp currentSqlTimestamp() {
		return (new java.sql.Timestamp((new java.util.Date()).getTime()));
	}

	public static String getTimeStringFromDate(Date dateInput,
			boolean withSecond) {
		if (dateInput != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(dateInput);
			String hour = String.valueOf(cal.get(Calendar.HOUR_OF_DAY));
			if (hour.length() == 1)
				hour = "0" + hour;
			String minute = String.valueOf(cal.get(Calendar.MINUTE));
			if (minute.length() == 1)
				minute = "0" + minute;
			String second = "";
			if (withSecond) {
				second = String.valueOf(cal.get(Calendar.SECOND));
				if (second.length() == 1)
					second = "0" + second;
				second = ":".concat(second);
			}
			return hour.concat(":").concat(minute).concat(second);
		}
		return "";
	}

	// public static String currentDateToString(final String formatPattern) {
	// Date dt = getCurrentDateFromDb();
	// SimpleDateFormat sdf = new SimpleDateFormat();
	// if (formatPattern != null && !formatPattern.isEmpty())
	// sdf.applyLocalizedPattern(formatPattern);
	// else
	// sdf.applyLocalizedPattern(CommonConstants.INPUT_DATE_FORMAT);
	// return sdf.format(dt);
	// }
	//
	// public static long currentDateTimeToLong() {
	// return getCurrentDateTimeFromDb().getTime();
	// }

	public static Date stripTimePortion(Date dateTime) {
		Date result = null;
		if (dateTime != null) {
			Calendar cal = Calendar.getInstance(); // get calendar instance
			cal.setTime(dateTime); // set cal to date
			cal.set(Calendar.HOUR_OF_DAY, 0); // set hour to midnight
			cal.set(Calendar.MINUTE, 0); // set minute in hour
			cal.set(Calendar.SECOND, 0); // set second in minute
			cal.set(Calendar.MILLISECOND, 0);
			result = cal.getTime();
		}
		return result;
	}

	public static Date getCurrentDateWithoutTimePortion() {
		Calendar cal = Calendar.getInstance(); // get calendar instance
		cal.set(Calendar.HOUR_OF_DAY,
				cal.getActualMinimum(Calendar.HOUR_OF_DAY));
		cal.set(Calendar.MINUTE, cal.getActualMinimum(Calendar.MINUTE));
		cal.set(Calendar.SECOND, cal.getActualMinimum(Calendar.SECOND));
		cal.set(Calendar.MILLISECOND,
				cal.getActualMinimum(Calendar.MILLISECOND));
		return cal.getTime();
	}

	public static TimeZone getDefaultTimeZone() {
		return TimeZone.getDefault();
	}

	public static String getDefaultTimeZoneId() {
		return TimeZone.getDefault().getID();
	}

	public static String dateToStringYYYYMMDD(Date inputDate, boolean stripDash) {
		String result = "";
		if (inputDate != null)
			result = reverseFormatter.format(inputDate);
		if (stripDash)
			result = result.replaceAll("-", "");
		return result;
	}

	//
	// public static Date getCurrentDateFromDb() {
	// CommonDAO common = new CommonDAO();
	// java.sql.Timestamp sqlDate = common.getCurrentDBDate();
	// if (sqlDate != null)
	// return new Date(sqlDate.getTime());
	// else
	// return new Date();
	// }
	//
	// public static Date getCurrentDateFromDbForVendorSyn() {
	// CommonDAO common = new CommonDAO();
	// java.sql.Timestamp sqlDate = common.getCurrentDBDateForVendorSyn();
	// if (sqlDate != null)
	// return new Date(sqlDate.getTime());
	// else
	// return new Date();
	// }
	//
	// public static Date getCurrentDateTimeFromDb() {
	// CommonDAO common = new CommonDAO();
	// java.sql.Timestamp sqlDateTime = common.getCurrentDBDateTime();
	// if (sqlDateTime != null)
	// return new Date(sqlDateTime.getTime());
	// else
	// return new Date();
	// }

	public String getDateValue(final Date inputDate, final DateValue dateValue) {
		String result = "";
		if (inputDate != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(inputDate);
			DateFormatSymbols dfs = new DateFormatSymbols();
			switch (dateValue) {
			case DAY_VALUE:
				result = dfs.getWeekdays()[cal.get(Calendar.DAY_OF_WEEK)];
				break;
			case DATE_VALUE:
				result = String.valueOf(cal.get(Calendar.DATE));
				if (result.length() == 1)
					result = "0" + result;
				break;
			case MONTH_IN_MONTH_VALUE:
				result = dfs.getMonths()[cal.get(Calendar.MONTH)];
				break;
			case YEAR_VALUE:
				result = String.valueOf(cal.get(Calendar.YEAR));
				break;
			case MONTH_IN_MM_VALUE:
				result = String.valueOf(cal.get(Calendar.MONTH) + 1);
				if (result.length() == 1)
					result = "0" + result;
				break;
			case MONTH_IN_MON_VALUE:
				result = dfs.getShortMonths()[cal.get(Calendar.MONTH)];
				break;
			case DAY_IND_VALUE:
				result = getDayInIndonesian(cal.get(Calendar.DAY_OF_WEEK));
				break;
			case MONTH_IN_MONTH_IND_VALUE:
				result = getMonthInIndonesian(cal.get(Calendar.MONTH));
				break;
			case DATE_TERBILANG:
				result = MathUtil.terbilang(cal.get(Calendar.DATE));
				break;
			case MONTH_TERBILANG:
				result = MathUtil.terbilang(cal.get(Calendar.MONTH) + 1);
				break;
			case YEAR_TERBILANG:
				result = MathUtil.terbilang(cal.get(Calendar.YEAR));
				break;
			default:
				break;
			}
		}
		return result;
	}

	public static String getDayInIndonesian(final int dayOfWeek) {
		switch (dayOfWeek) {
		case 1:
			return "Minggu";
		case 2:
			return "Senin";
		case 3:
			return "Selasa";
		case 4:
			return "Rabu";
		case 5:
			return "Kamis";
		case 6:
			return "Jumat";
		case 7:
			return "Sabtu";
		default:
			return "";
		}
	}

	public static String getMonthInIndonesian(final int month) {
		switch (month) {
		case 0:
			return "Januari";
		case 1:
			return "Februari";
		case 2:
			return "Maret";
		case 3:
			return "April";
		case 4:
			return "Mei";
		case 5:
			return "Juni";
		case 6:
			return "Juli";
		case 7:
			return "Agustus";
		case 8:
			return "September";
		case 9:
			return "Oktober";
		case 10:
			return "November";
		case 11:
			return "Desember";
		default:
			return "";
		}
	}

	public static Date addDate(final Date date, final DateType dateType,
			final int param) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		switch (dateType) {
		case DAYS:
			cal.add(Calendar.DATE, param);
			break;
		case MONTH:
			cal.add(Calendar.MONTH, param);
			break;
		case YEAR:
			cal.add(Calendar.YEAR, param);
			break;
		default:
			break;
		}
		return cal.getTime();
	}

	public static int dateDifference(final Date startDate, final Date endDate,
			final DateType dateType) {
		DateTime start = new DateTime(startDate);
		DateTime end = new DateTime(endDate);
		int result = 0;
		switch (dateType) {
		case DAYS:
			result = Days.daysBetween(start, end).getDays();
			break;
		case MONTH:
			result = Months.monthsBetween(start, end).getMonths();
			break;
		case YEAR:
			result = Years.yearsBetween(start, end).getYears();
			break;
		default:
			break;
		}

		return result;
	}

	public static Date getEndOfDay(Date date) {
		if (date != null) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.set(Calendar.HOUR_OF_DAY, 23);
			calendar.set(Calendar.MINUTE, 59);
			calendar.set(Calendar.SECOND, 59);
			calendar.set(Calendar.MILLISECOND, 999);
			return calendar.getTime();
		}
		return null;
	}
	
	public static String dateToStringDDMMYYYY(Date inputDate, boolean stripDash) {
		String result = "";
		if (inputDate != null)
			result = reverseFormatter2.format(inputDate);
		if (stripDash)
			result = result.replaceAll("-", "");
		return result;
	}
}
