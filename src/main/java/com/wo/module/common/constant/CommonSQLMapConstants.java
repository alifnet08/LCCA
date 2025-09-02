package com.wo.module.common.constant;

public interface CommonSQLMapConstants {

	final static String ELEMENT_ORDER = "order";

	final static String ELEMENT_SQL = "sql";

	final static String ELEMENT_WHERE = "where";
	
	final static String ELEMENT_GROUP_BY = "group-by";
	/*String GLOBAL_VAR_CREATED_BY = "<created_by>";

	String GLOBAL_VAR_UPDATED_BY = "<last_update_by>";
	
	String GLOBAL_VAR_ENABLED_FLAG = "<enabled_flag>";*/

	//String GLOBAL_VAR_OWNER = "<owner>";
	
	//String GLOBAL_VAR_UPDATED_PROCESS = "<updated_process>";
	
	final static String ATTRIBUTE_MODULE = "module";

	final static String ATTRIBUTE_NAME = "name";

	final static String BRACKET_CLOSE = ")";

	final static String BRACKET_OPEN = "(";

	final static String MODULE_COMMON = "COMMON";

	final static String SEPARATOR = "$$";

	final static String SQL_AND = " AND ";

	final static String SQL_OR = " OR ";

	final static String SQL_ORDER_BY = " ORDER BY ";
	
	final static String SQL_GROUP_BY = " GROUP BY ";

	final static String SQL_WHERE = " WHERE ";

	final static String SQL_WILDCHAR = "%";
	
	final static String SQL_LIKE = " LIKE ";
	
	final static String SQL_COLUMN_APPEND = "||";
	
	final static String SQL_DASH_IN_BETWEEN = "' - '";
	
	final static String SQL_COLUMN_CONNECTOR = SQL_COLUMN_APPEND + SQL_DASH_IN_BETWEEN + SQL_COLUMN_APPEND;
	
	
	final static String SQL_SELECT_LABEL_VALUE = "SQL_SELECT_LABEL_VALUE";
	
	final static String SQL_SELECT_LABEL_VALUE_VALUE = "<columnId>";
	
	final static String SQL_SELECT_LABEL_VALUE_LABEL = "<columnName>";
	
	final static String SQL_SELECT_LABEL_VALUE_CODE = "<columnCode>";
	
	final static String SQL_SELECT_LABEL_VALUE_SHOW_WITH_CODE = "<showWithCode>";
	
	final static String SQL_SELECT_LABEL_VALUE_TABLE = "<tableName>";
	
	final static String SQL_WHERE_SELECT_LABEL_VALUE = "SQL_WHERE_SELECT_LABEL_VALUE";
	
	final static String SQL_SELECT_LABEL_VALUE_PARENT_COLUMN = "<parentColumn>";
	
	final static String SQL_SELECT_LABEL_VALUE_PARENT_VALUE = "<parentValue>";
	
	final static String SQL_ORDER_SELECT_LABEL_VALUE = "SQL_ORDER_SELECT_LABEL_VALUE";
	
	final static String SQL_ORDER_LABEL_VALUE_COLUMN = "<orderedColumn>";
	
	final static String SQL_MAIL_TEMPLATE = "MAIL_TEMPLATE";
	
	final static String SQL_MAIL_TEMPLATE_SELECT_DATA = "SQL_SELECT_DATA";
	
	final static String SQL_SELECT_DATA_FOR_TENDER_APPROVAL = "SQL_SELECT_DATA_FOR_TENDER_APPROVAL";
	
	final static String SQL_MAIL_TEMPLATE_SELECT_DATA_BIDDING = "SQL_SELECT_DATA_FOR_BIDDING";
	
	final static String SQL_TENDER_STEP_REVERSE_AUCTION = "<reverse_auction>";
	
	final static String SQL_SELECT_TENDER_WINNER_TO_VENDOR = "SQL_SELECT_TENDER_WINNER_TO_VENDOR";
	
	final static String SQL_SELECT_TENDER_WINNER_TO_HEAD = "SQL_SELECT_TENDER_WINNER_TO_HEAD";
	
	
	final static String SQL_SELECT_ALL_TENDER_BRANCH = "SQL_SELECT_ALL_TENDER_BRANCH";
	
	
	final static String SQL_MAIL_JOIN_TENDER_VENDOR_LINK = "<JOIN_TENDER_VENDOR_LINK>";
	
	
	final static String SQL_GET_CURRENT_DB_DATETIME = "SQL_GET_CURRENT_DB_DATETIME";
	
	final static String SQL_GET_CURRENT_DB_DATE = "SQL_GET_CURRENT_DB_DATE";
}