package com.wo.module.common.constant;

import org.apache.commons.lang3.StringUtils;

public interface CommonConstants {

	final String FILE_SEPARATOR = System.getProperty("file.separator");

	final String BOOLEAN_FALSE = "false";

	final String BOOLEAN_TRUE = "true";

	/** Constant for login success message */
	final String MSG_LOGIN_SUCCESSFULL = "LOGIN_SUCCESSFULL";
	final String MSG_LOGIN_INVALID_CREDENTIALS = "INVALID CREDENTIALS";

	/** Constant for database access */
	final String UPDATE_PROCESS_INSERT = "INSERT ";

	final String UPDATE_PROCESS_UPDATE = "UPDATE ";

	final String UPDATE_PROCESS_DELETE = "DELETE ";

	final String SUBMIT_PROCESS_INSERT = "SUBMIT ";

	/** Constant for enabled flag */
	final String ENABLED_FLAG_TRUE = "Y";

	final String ENABLED_FLAG_FALSE = "N";

	final String N = "N";

	final String Y = "Y";

	final String SPACE = " ";

	final String SEPARATOR_UNDERLINE = "_";

	final String SEPARATOR_DOT = ".";

	/** Constant for date */
	final static String GLOBALDATEFORMAT = "dd/MM/yyyy";

	final static String INPUT_DATE_FORMAT = "dd-MMM-yyyy";
	
	final static String INPUT_DATE_FORMAT_2 = "ddmmmyyyy";

	final static String INPUT_DATE_TIME_FORMAT = "dd-MMM-yyyy HH:mm:ss";
	
	final static String INPUT_DATE_TIME_FORMAT_2 = "yyyymmddHHmmss";

	final static String DB_DATE_FORMAT = "dd-MMM-yy";

	final static String DOCUMENT_DATE_FORMAT = "dd MMMM yyyy";

	final static String DB_DATE_FORMAT2 = "yyyy-MM-dd";

	final static String COMMONDATEFORMAT = "dd-MM-yyyy";

	final static String DOCUMENT_DATE_FORMAT_IND = "dd Bulan yyyy";

	final static String DOCUMENT_DATE_DAY_FORMAT_IND = "hari, dd Bulan yyyy";

	final static String DOCUMENT_DATE_FORMAT_HARI = "hari";

	final static String DOCUMENT_DATE_FORMAT_BULAN = "Bulan";

	final static String INPUT_DATE_LOCALE = "en";

	final static String INVALID_TIME_FORMAT_CONSTANT = "invalidTime";

	final static String DEFAULT_TIME_FORMAT = "00:00:00";

	final static String DB_ORCL_INPUT_DATE_FORMAT = "dd-Mon-yyyy";

	/** Constant for session */
	final static String SESSION_PAGING_NUMBER = "SESSION_PAGING_NUMBER";

	final static String SESSION_PERSON = "SESSION_PERSON";

	/** Constant for math */
	final static String US_CURRENCY_PATTERN = "###,###,###,###,###.##";

	/** Constant for Parameter */
	final static String SYSTEM_PROPERTY_NO_LDAP_ADMIN_PASS = "NO_LDAP_ADMIN_PASS";
	final static String SYSTEM_PROPERTY_PAGINATION_ROWS = "PAGINATION_ROW";

	final static String SYSTEM_PROPERTY_EXTENDED_PAGINATION_ROWS = "EXTENDED_PAGINATION_ROW";

	final static int DEFAULT_PAGINATION_ROWS = 10;

	final static int DEFAULT_EXTENDED_PAGINATION_ROWS = 50;

	final static String CURR_USD = "USD";

	final static String CURR_IDR = "IDR";

	/** Constant for parameter method */
	final static String METHOD_PARAMETER = "method";

	/** Constant for administrator responsibility, executive and non-executive */
	final static String ADMINISTRATOR_RESPONSIBILITY = "administrator";

	final static String EXECUTIVE_RESPONSIBILITY = "executive";

	final static String MASS_RESPONSIBILITY = "mass";
	final static String CHECKER_RESPONSIBILITY = "checker";

	final static String BI_CHECKING = "BI_CHECKING";
	final static String REFERENCE_CHECKING = "REFERENCE_CHECKING";

	// start additional constant for Report
	final static String REPORT_GEN_STATUS_ON_PROGRESS = "ON_PROGRESS";
	final static String REPORT_GEN_STATUS_COMPLETE_SUCCESS = "COMPLETE";
	final static String REPORT_GEN_STATUS_COMPLETE_ERR = "ERROR";

	final static String SEPARATOR_DASH = "-";

	final static String FILE_TYPE_XLSX = ".xlsx";
	final static String FILE_TYPE_JPEG = ".jpeg";

	final static String SYSTEM_PROPERTY_FILE_PATH = "FILE_PATH";

	final static String MAP_KEY_CELL_FORMAT_DATA_DTL_NUMBER = "cellFormatNumber";
	final static String MAP_KEY_CELL_FORMAT_DATA_DTL_DECIMAL = "cellFormatDecimal";
	final static String MAP_KEY_CELL_FORMAT_DATA_DTL_DATE = "cellFormatDate";
	final static String MAP_KEY_CELL_FORMAT_DATA_DTL_STRING = "cellFormatString";
	final static String MAP_KEY_CELL_FORMAT_DATA_DTL_WRAP_STRING = "cellFormatWrapString";
	final static String MAP_KEY_CELL_FORMAT_DATA_DTL_WRAP_NO_BOTTOM_STRING = "cellFormatWrapNoBottomString";
	final static String MAP_KEY_CELL_FORMAT_DATA_DTL_WRAP_BORDER_TOP_ONLY_STRING = "cellFormatWrapTopOnlyString";
	final static String MAP_KEY_CELL_FORMAT_HEADER_VALUE = "cellFormatHeaderValue";
	final static String MAP_KEY_CELL_FORMAT_HEADER_LABEL = "cellFormatHeaderLabel";
	final static String MAP_KEY_CELL_FORMAT_HEADER_TITLE = "cellFormatHeaderTitle";
	final static String MAP_KEY_CELL_FORMAT_COLUMN_HEADER = "cellFormatColumnHeader";
	final static String MAP_KEY_CELL_FORMAT_COLUMN_WRAP_HEADER = "cellFormatColumnWrapHeader";
	final static String MAP_KEY_CELL_FORMAT_COLUMN_HEADER_NUMBER = "cellFormatColumnHeaderNumber";
	final static String MAP_KEY_CELL_FORMAT_COLUMN_LABEL_WARNING = "cellFormatColumnLabelWarning";
	final static String MAP_KEY_CELL_FORMAT_COLUMN_HEADER_GREEN = "cellFormatColumnHeaderGreen";

	public static final String SEARCH_FILTER_BY_REPORT_CODE = "REPORT_CODE";
	public static final String SEARCH_FILTER_BY_NIK = "NIK";

	public static final String FND_LOOKUP_TYPE_REPORT_TYPE = "WO_CUSTOM_REPORT_WO";

	public static final String MAP_KEY_COLUMN_NAMES = "columnNames";
	public static final String MAP_KEY_COLUMN_NAME_ALIAS = "columnNameAlias";
	public static final String MAP_KEY_RESULTS = "results";

	public static final String ORGANIZATION_TYPE_DIREKTORAT = "DIREKTORAT";
	public static final String ORGANIZATION_TYPE_DIVISI = "DIVISI";

	public static final String CODE_BANK_WO_WOW = "201604";
	
	public final static String RECORD_FLAG_NEW = "N";
	
	public final static String DATA_NEW = "DATA_NEW";
	
	public final static String DATA_ACTIVE = "DATA_ACTIVE";
	public final static String DATA_REVISE = "DATA_REVISE";
	
	public final static String RECORD_FLAG_DELETE = "D";
	
	public final static String RECORD_FLAG_ACTIVE = "A";
	
	public final static String RECORD_FLAG_YES = "Y";
	
	public final static String RECORD_FLAG_NO = "N";
	
	public final static String LAINNYA = "lainnya";
	
	public final static String CSV_EXT = ".csv";
	
	public final static String STATUS_APPROVED = "STATUS_APPROVED";
	public final static String STATUS_REVISE = "STATUS_REVISE";
	
	public final static String REMINDER_ACTIVE = "REMINDER_ACTIVE";
	
	public final static String SEARCH_BY_TEXT_BOX = "SEARCH_BY_TEXT_BOX";
	public final static String SEARCH_BY_COMBO_BOX = "SEARCH_BY_COMBO_BOX";
	public final static String SEARCH_BY_USER_LOGIN = "SEARCH_BY_USER_LOGIN";

	// end additional constant for Report
	
	// system property
	public final static String SYSTEM_PROPERTY_CODE_LOCATION_LEVEL_USED = "LOCATION_LEVEL_USED";
	public final static String SYSTEM_PROPERTY_CODE_LOCATION_LEVEL_PARENT_USED = "LOCATION_LEVEL_PARENT_USED";
	
	public static final  String DEPLOY_SERVER_HTTPS = "N";
	public static final String URL_HTTP = "http://";
	public static final String URL_HTTPS = "https://";
	
	
	// additional
	public static final String SEARCH_CATEGORY = "DOCUMENT_CATEGORY_ID";
	
	
}