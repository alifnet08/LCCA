package com.wo.module.parameter.model;

import java.io.Serializable;
import java.util.Locale;

import javax.faces.context.FacesContext;

import com.wo.module.common.model.BaseEntity;

public class ParameterDetail extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 2140770585036881390L;

	public static final String PARAM_DET_CODE_SF_SUBMIT = "SF_SUBMIT";
	
	public static final String PARAM_DET_CODE_DATA_NEW = "DATA_NEW";
	public static final String PARAM_DET_CODE_DATA_REVISE = "DATA_REVISE";
	public static final String PARAM_DET_CODE_DATA_ACTIVE = "DATA_ACTIVE";	
	public static final String PARAM_DET_CODE_DATA_INACTIVE = "DATA_INACTIVE";	
	
	public static final String PARAM_DET_CODE_STATUS_APPROVED = "STATUS_APPROVED";
	public static final String PARAM_DET_CODE_STATUS_REVISE = "STATUS_REVISE";
	
	public static final String PARAM_DET_CODE_REMINDER_ACTIVE = "REMINDER_ACTIVE";	
	public static final String PARAM_DET_CODE_REMINDER_INACTIVE = "REMINDER_INACTIVE";
	
	public static final String PARAM_DET_CODE_PIC_FOLLOWUP_STATUS_PIC_INPROGRESS = "PIC_INPROGRESS";	
	public static final String PARAM_DET_CODE_PIC_FOLLOWUP_STATUS_PIC_DONE = "PIC_DONE";	
	public static final String PARAM_DET_CODE_PIC_FOLLOWUP_STATUS_PIC_EXTENSION = "PIC_EXTENSION";	
	
	public static final String PARAM_DET_CODE_COMPLIANCE_CHECK_STATUS_COMPLIANCE_OPEN = "COMPLIANCE_OPEN";	
	public static final String PARAM_DET_CODE_COMPLIANCE_CHECK_STATUS_COMPLIANCE_CLOSE = "COMPLIANCE_CLOSE";
	
	public static final String PARAM_DET_CODE_SFTP_HOST = "SFTP_HOST";
	public static final String PARAM_DET_CODE_SFTP_USER = "SFTP_USER";
	public static final String PARAM_DET_CODE_SFTP_PASS = "SFTP_PASS";
	public static final String PARAM_DET_CODE_SFTP_PATH = "SFTP_PATH";
	public static final String PARAM_DET_CODE_SFTP_LOCAL = "SFTP_LOCAL";
	public static final String PARAM_DET_CODE_SFTP_FILENAME = "SFTP_FILENAME";
	
	public static final String PARAM_DET_LOG_PROCESS_STATUS_S = "C";
	public static final String PARAM_DET_LOG_PROCESS_STATUS_I = "I";
	public static final String PARAM_DET_LOG_PROCESS_STATUS_E = "E";
	
	public static final String PARAM_DET_CODE_LOG_INBOUND_EXEC_TIME = "LOG_INBOUND_EXEC_TIME";
	
	public static final String PARAM_DET_CODE_LOG_SEND_EMAIL_EXEC_TIME = "LOG_SEND_EMAIL_EXEC_TIME";
	
	public static final String PARAM_DET_CODE_LOG_SEND_EMAIL_QA_EXEC_TIME = "LOG_SEND_EMAIL_QA_EXEC_TIME";
	
	public static final String PARAM_DET_CODE_LOG_SEND_EMAIL_QA_EVERY_HOUR_EXEC_TIME = "LOG_SEND_EMAIL_QA_EVERY_HOUR_EXEC_TIME";
	
	public static final String PARAM_DET_CODE_LOG_SEND_EMAIL_QA_FOR_CLOSE_EVERY_HOUR_EXEC_TIME = "LOG_SEND_EMAIL_QA_FOR_CLOSE_EVERY_HOUR_EXEC_TIME";

	public static final String PARAM_DET_CODE_LOG_SEND_EMAIL_IRG_OBS_EXEC_TIME = "LOG_SEND_EMAIL_IRG_OBS_EXEC_TIME";
	
	public static final String PARAM_DET_CODE_LOG_SEND_EMAIL_IRG_PENERBITAN_EXEC_TIME = "LOG_SEND_EMAIL_IRG_PENERBITAN_EXEC_TIME";
	
	public static final String PARAM_DET_CODE_LOG_SEND_EMAIL_CPSA_EXEC_TIME = "LOG_SEND_EMAIL_CPSA_EXEC_TIME";
	
	public static final String PARAM_DET_CODE_LOG_AUTO_ABSOLUTE_INTERNAL = "LOG_AUTO_ABSOLUTE_INTERNAL";

	public static final String PARAM_DET_I_ATTEND = "I_ATTEND";
	public static final String PARAM_DET_NOT_ATTEND = "NOT_ATTEND";
	public static final String PARAM_DET_PIC_INPROGRESS = "PIC_INPROGRESS";
	public static final String PARAM_DET_CODE_DOCUMENT_TYPE = "DOCUMENT_TYPE";
	public static final String PARAM_DET_CODE_DOCUMENT_SUBMITTER = "DOCUMENT_SUBMITTER";
	
	public static final String PARAM_DET_CODE_ATTACHMENT_FILE_PATH_TMP = "ATTACHMENT_FILE_PATH_TMP";
	public static final String PARAM_DET_CODE_ATTACHMENT_FILE_PATH = "ATTACHMENT_FILE_PATH";
	
	public static final String PARAM_DET_CODE_FOLLOWUP_ATTACH_DOC = "FOLLOWUP_ATTACH_DOC";
	public static final String PARAM_DET_CODE_LETTER_ATTACH_DOC = "LETTER_ATTACH_DOC";
	
	public final static String IS_USING_API = "IS_USING_API";
	public final static String BODY_URL_UPLOAD = "BODY_URL_UPLOAD";
	public final static String BODY_URL_DELETE = "BODY_URL_DELETE";
	public final static String BODY_TERMINAL_ID = "BODY_TERMINAL_ID";
	public final static String BODY_REQUEST_ID = "BODY_REQUEST_ID";
	public final static String BODY_CHANNEL_ID = "BODY_CHANNEL_ID";
	public final static String BODY_CHANNEL_TYPE = "BODY_CHANNEL_TYPE";
	public final static String BODY_PASSWORD = "BODY_PASSWORD";
	public final static String BODY_USERNAME = "BODY_USERNAME";
	public final static String HEADER_BTPN_KEY = "HEADER_BTPN_KEY";
	public final static String HEADER_POSTMAN_TOKEN = "HEADER_POSTMAN_TOKEN";
	public final static String BODY_CONTENT_TYPE = "BODY_CONTENT_TYPE";
	
	public static final String PARAM_DET_CODE_INBOUND_USER_VIEW = "INBOUND_USER_VIEW";
	
	public static final String PARAM_DET_CODE_HOST_NAME_APPLICATION = "HOST_NAME_APPLICATION";
	
	public static final String PARAM_DET_CODE_RECORD_REVOKE = "RECORD_REVOKE";
	public static final String PARAM_DET_CODE_RECORD_CHANGE = "RECORD_CHANGE";
	public static final String PARAM_DET_CODE_RECORD_REGULATION = "RECORD_NEW_REGULATION";
	
	public static final String PARAM_DET_CODE_KETENTUAN_EXTERNAL = "KETENTUAN_EKSTERNAL";
	public static final String PARAM_DET_CODE_KETENTUAN_INTERNAL = "KETENTUAN_INTERNAL";
	
	public static final String PARAM_DET_CODE_ATTACHMENT_REGULATIONS_IN = "ATTACHMENT_REGULATIONS_IN";
	public static final String PARAM_DET_CODE_ATTACHMENT_REGULATIONS_EN = "ATTACHMENT_REGULATIONS_EN";
	public static final String PARAM_DET_CODE_ATTACHMENT_SUMMARY = "ATTACHMENT_SUMMARY";
	public static final String PARAM_DET_CODE_ATTACHMENT_EXPLAINATION = "ATTACHMENT_EXPLAINATION";
	public static final String PARAM_DET_CODE_ATTACHMENT_FAQ = "ATTACHMENT_FAQ";
	public static final String PARAM_DET_CODE_ATTACHMENT_LAMPIRAN = "ATTACHMENT_LAMPIRAN";
	public static final String PARAM_DET_CODE_ATTACHMENT_BULETIN = "ATTACHMENT_BULETIN";
	public static final String PARAM_DET_CODE_ATTACHMENT_MATERIAL = "ATTACHMENT_MATERIAL";
	public static final String PARAM_DET_CODE_DOC_TYPE_HUK = "DOC_TYPE_HUK";
	public static final String PARAM_DET_CODE_SENDER_OJK_PASAR_MODAL = "SENDER_OJK_PASAR_MODAL";
	public static final String PARAM_DET_CODE_SENDER_OJK = "SENDER_OJK";
	
	public static final String PARAM_CODE_DIRECTORATE = "DIRECTORATE";
	public static final String PARAM_CODE_PUBLISHER_UNIT = "PUBLISHER_UNIT";
	
	public static final String PARAM_DET_CODE_FINE_LHP_OTHER = "FINE_LHP_OTHER";
	
	//unused litigation
	public static final String PARAM_DET_CODE_LITIGATION_COURT_TYPE_PN = "COURT_TYPE_PN";
	public static final String PARAM_DET_CODE_LITIGATION_COURT_TYPE_PT = "COURT_TYPE_PT";
	public static final String PARAM_DET_CODE_LITIGATION_COURT_TYPE_MA = "COURT_TYPE_MA";
	public static final String PARAM_DET_CODE_LITIGATION_COURT_NAME_MA_RI = "COURT_NAME_MA_RI";
	public static final String PARAM_DET_CODE_LITIGATION_LEGAL_EFFORT_INKRACHT = "LEGAL_EFFORT_INKRACHT";
	public static final String PARAM_DET_CODE_LITIGATION_LEGAL_EFFORT_INKRACHT_MA = "LEGAL_EFFORT_INKRACHT_MA";
	public static final String PARAM_DET_CODE_LITIGATION_EFFORT_KASASI = "LEGAL_EFFORT_KASASI";
	public static final String PARAM_DET_CODE_LITIGATION_DECISION_BANDING = "DECISION_BANDING";
	
	//unused newly created litigation
	public static final String PARAM_DET_CODE_LITIGATION_CASE_HANDLER_TEAM_1 = "CASE_HANDLER_TEAM_1";
	public static final String PARAM_DET_CODE_LITIGATION_CASE_HANDLER_TEAM_2 = "CASE_HANDLER_TEAM_2";
	
	//newly created litigation
	public static final String PARAM_DET_CODE_LITIGATION_CASE_TYPE_PERDATA= "CASE_TYPE_PERDATA";
	public static final String PARAM_DET_CODE_LITIGATION_CASE_TYPE_PAILIT= "CASE_TYPE_PAILIT";
	public static final String PARAM_DET_CODE_LITIGATION_CASE_TYPE_PKPU = "CASE_TYPE_PKPU";
	public static final String PARAM_DET_CODE_LITIGATION_CASE_TYPE_PIDANA = "CASE_TYPE_PIDANA";
	public static final String PARAM_DET_CODE_LITIGATION_CASE_TYPE_PAILIT_PKPU = "CASE_TYPE_PAILIT_PKPU";
	public static final String PARAM_DET_CODE_LITIGATION_CASE_TYPE_DTL_PERDATA_PENGGUGAT = "CASE_TYPE_DTL_PERDATA_PENGGUGAT";
	public static final String PARAM_DET_CODE_LITIGATION_CASE_TYPE_DTL_PERDATA_TERGUGAT = "CASE_TYPE_DTL_PERDATA_TERGUGAT";
	public static final String PARAM_DET_CODE_LITIGATION_CASE_TYPE_DTL_PIDANA_PELAPOR = "CASE_TYPE_DTL_PIDANA_PELAPOR";
	public static final String PARAM_DET_CODE_LITIGATION_CASE_TYPE_DTL_PIDANA_TERLAPOR = "CASE_TYPE_DTL_PIDANA_TERLAPOR";
	public static final String PARAM_DET_CODE_LITIGATION_CASE_HANDLER_INTERNAL = "CASE_HANDLER_INTERNAL";
	public static final String PARAM_DET_CODE_LITIGATION_CASE_HANDLER_EXTERNAL= "CASE_HANDLER_EXTERNAL";
	public static final String PARAM_DET_CODE_LITIGATION_COURT_TYPE_RELIGIOUS = "COURT_TYPE_RELIGIOUS";
	public static final String PARAM_DET_CODE_LITIGATION_COURT_TYPE_COUNTRY = "COURT_TYPE_COUNTRY";
	public static final String PARAM_DET_CODE_LITIGATION_FOREIGN_CURRENCY_USD = "FOREIGN_CURRENCY_USD";
	public static final String PARAM_DET_CODE_LITIGATION_FOREIGN_CURRENCY_AUD = "FOREIGN_CURRENCY_AUD";
	public static final String PARAM_DET_CODE_LITIGATION_FOREIGN_CURRENCY_SGD = "FOREIGN_CURRENCY_SGD";
	public static final String PARAM_DET_CODE_LITIGATION_CASE_TEAM_HANDLER_TEAM_1 = "CASE_TEAM_HANDLER_1";
	public static final String PARAM_DET_CODE_LITIGATION_CASE_TEAM_HANDLER_TEAM_2 = "CASE_TEAM_HANDLER_2";
	public static final String PARAM_DET_CODE_LITIGATION_CASE_MAIN_TOPIC_OTHER_LAWSUIT = "MAIN_TOPIC_OTHER_LAWSUIT";
	public static final String PARAM_DET_CODE_LITIGATION_CASE_MAIN_TOPIC_UNLAWFUL_CONDUCT = "MAIN_TOPIC_UNLAWFUL_CONDUCT";
	public static final String PARAM_DET_CODE_LITIGATION_CASE_MAIN_TOPIC_WANPRESTASI = "MAIN_TOPIC_WANPRESTASI";
	public static final String PARAM_DET_CODE_LITIGATION_CASE_MAIN_TOPIC_WARIS = "MAIN_TOPIC_WARIS";
	public static final String PARAM_DET_CODE_LITIGATION_CASE_MAIN_TOPIC_PERLAWANAN = "MAIN_TOPIC_PERLAWANAN";
	public static final String PARAM_DET_CODE_LITIGATION_CASE_SUB_TOPIC_CESSIE_SUBROGATION = "SUB_TOPIC_CESSIE_SUBROGATION";
	public static final String PARAM_DET_CODE_LITIGATION_CASE_SUB_TOPIC_OTHER_LAWSUIT = "SUB_TOPIC_OTHER_LAWSUIT";
	public static final String PARAM_DET_CODE_LITIGATION_CASE_SUB_TOPIC_OTHERS = "SUB_TOPIC_OTHERS";
	public static final String PARAM_DET_CODE_LITIGATION_CASE_SUB_TOPIC_PERLAWANAN = "SUB_TOPIC_PERLAWANAN";
	public static final String PARAM_DET_CODE_LITIGATION_CASE_SUB_TOPIC_THIRD_PARTY = "SUB_TOPIC_THIRD_PARTY";
	public static final String PARAM_DET_CODE_LITIGATION_CASE_SUB_TOPIC_RESTRUCTURE = "SUB_TOPIC_RESTRUCTURE";
	public static final String PARAM_DET_CODE_LITIGATION_CASE_SUB_TOPIC_WARIS = "SUB_TOPIC_WARIS";
	public static final String PARAM_DET_CODE_LITIGATION_COMMERCIAL_COURT_MEDAN = "COMMERCIAL_COURT_MEDAN";
	public static final String PARAM_DET_CODE_LITIGATION_COMMERCIAL_COURT_JAKARTA = "COMMERCIAL_COURT_JAKARTA";
	public static final String PARAM_DET_CODE_LITIGATION_COMMERCIAL_COURT_SEMARANG = "COMMERCIAL_COURT_SEMARANG";
	public static final String PARAM_DET_CODE_LITIGATION_COMMERCIAL_COURT_SURABAYA = "COMMERCIAL_COURT_SURABAYA";
	public static final String PARAM_DET_CODE_LITIGATION_COMMERCIAL_COURT_MAKASSAR = "COMMERCIAL_COURT_MAKASSAR";
	
			
	public static final String PARAM_DET_CODE_QNA_STATUS_NEW = "QNA_STATUS_NEW";
	public static final String PARAM_DET_CODE_QNA_STATUS_IP = "QNA_STATUS_IP";
	public static final String PARAM_DET_CODE_QNA_STATUS_ANSWERED = "QNA_STATUS_ANSWERED";
	public static final String PARAM_DET_CODE_QNA_STATUS_CLOSE = "QNA_STATUS_CLOSE";
	public static final String PARAM_DET_CODE_QNA_WARNING_TEXT = "QNA_WARNING_TEXT";
	
	public static final String PARAM_DET_CODE_TYPE_FINE = "FINE";
	
	public final static String PARAM_DET_CODE_FOLLOWUP_TYPE = "FOLLOWUP_TYPE";
	
	public final static String PARAM_DET_CODE_TEMPLATE_DOC_IN = "TEMPLATE_DOC_IN";
	public final static String PARAM_DET_CODE_TEMPLATE_DOC_EN = "TEMPLATE_DOC_EN";
	public final static String PARAM_DET_CODE_TEMPLATE_FILL_INSTRUCTION_IN = "FILL_INSTRUCTION_IN";
	public final static String PARAM_DET_CODE_TEMPLATE_FILL_INSTRUCTION_EN = "FILL_INSTRUCTION_EN";
	
	public final static String PARAM_DET_CODE_INTERNAL_REGULATION_EXPIRED_YEAR = "INTERNAL_REGULATION_EXPIRED_YEAR";
	public final static String PARAM_DET_CODE_LINK_ELEARNING = "LINK_ELEARNING";
	public final static String PARAM_DET_CODE_LINK_TENTANG_MAYBANK = "LINK_MAYBANK_CORPORATE_PORTAL";
	
	public final static String PARAM_DET_CODE_CREATED_BY_SYSTEM = "CREATED_BY_SYSTEM";
	public final static String PARAM_DET_CODE_CREATED_BY_MANUAL = "CREATED_BY_MANUAL";

	public final static String FINE_FOLLOWUP_ATTACHMENT_TYPE_EXTENDED	="FINE_ATTACHMENT_EXTENDED";
	public final static String FINE_FOLLOWUP_ATTACHMENT_TYPE_FOLLOWUP	="FINE_ATTACHMENT_FOLLOWUP";
	
	public static final String PARAM_DET_LOG_ARCHIVE_FILE_EXEC_TIME = "LOG_ARCHIVE_FILE_EXEC_TIME";
	public static final String PARAM_DET_DAYS_FILE_ARCHIVE_TIME = "DAYS_FILE_ARCHIVE_TIME";
	public static final String PARAM_DET_ARCHIVE_FILE_PATH = "ARCHIVE_FILE_PATH";

	public final static String PARAM_DET_FINE_EMAIL_RESPONSE	="FINE_EMAIL_RESPONSE";
	public final static String PARAM_DET_FINE_EMAIL_FINISHING	="FINE_EMAIL_FINISHING";
	
	public final static String PARAM_DET_VALID_UPLOAD_QUESTION_CPSA = "VALID_UPLOAD_QUESTION_CPSA";
	
	public final static String PARAM_DET_OPEN_REGULATION_OBSOLETE = "OPEN_REGULATION_OBSOLETE";
	public final static String PARAM_DET_CLOSE_REGULATION_OBSOLETE = "CLOSE_REGULATION_OBSOLETE";
	
	public final static String PARAM_DET_FINE_TARGET_DATE_APPROPRIATE = "FINE_TARGET_DATE_APPROPRIATE";
	public final static String PARAM_DET_FINE_TARGET_DATE_NOT_APPROPRIATE = "FINE_TARGET_DATE_NOT_APPROPRIATE";
	
	public final static String PARAM_DET_COMPLIANCE_PLAN_SELF_ASSESSMENT_TYPE_SYARIAH = "COMPLIANCE_PLAN_SELF_ASSESSMENT_TYPE_SYARIAH";
	public final static String PARAM_DET_COMPLIANCE_PLAN_SELF_ASSESSMENT_TYPE_CPSA = "COMPLIANCE_PLAN_SELF_ASSESSMENT_TYPE_CPSA";
	
	public final static String PARAM_DET_CPSA_HEADER_CPSA = "CPSA_HEADER_CPSA";
	public final static String PARAM_DET_CPSA_HEADER_PUSAT = "CPSA_HEADER_PUSAT";
	public final static String PARAM_DET_CPSA_HEADER_SYARIAH = "CPSA_HEADER_SYARIAH";
	
	public final static String PARAM_CODE_INTERNAL_REGULATION_REVIEW_DATE = "INTERNAL_REGULATION_REVIEW_DATE_TYPE";
	public final static String PARAM_DET_CODE_TRACK_RECORD_NEW_REGULATION = "RECORD_NEW_REGULATION";
	
	private Long parameterDtlId;
	private String parameterDtlCode;
	private String parameterCode;
	private String nameIn;
	private String nameEn;
	private Long passiveParameterId;
	private ParameterHeader parameterHeader;
	private String enabledFlag;
	
	private String name;

	public Long getParameterDtlId() {
		return parameterDtlId;
	}

	public void setParameterDtlId(Long parameterDtlId) {
		this.parameterDtlId = parameterDtlId;
	}

	public String getParameterDtlCode() {
		return parameterDtlCode;
	}

	public void setParameterDtlCode(String parameterDtlCode) {
		this.parameterDtlCode = parameterDtlCode;
	}

	public String getNameIn() {
		return nameIn;
	}

	public void setNameIn(String nameIn) {
		this.nameIn = nameIn;
	}

	public String getNameEn() {
		return nameEn;
	}

	public void setNameEn(String nameEn) {
		this.nameEn = nameEn;
	}

	public Long getPassiveParameterId() {
		return passiveParameterId;
	}

	public void setPassiveParameterId(Long passiveParameterId) {
		this.passiveParameterId = passiveParameterId;
	}

	public ParameterHeader getParameterHeader() {
		return parameterHeader;
	}

	public void setParameterHeader(ParameterHeader parameterHeader) {
		this.parameterHeader = parameterHeader;
	}

	@SuppressWarnings("static-access")
	public String getName() {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		if (locale != null && locale.equals(locale.ENGLISH)) {
			name = nameEn;
		} else {
			name = nameIn;
		}
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getParameterCode() {
		return parameterCode;
	}

	public void setParameterCode(String parameterCode) {
		this.parameterCode = parameterCode;
	}

	public String getEnabledFlag() {
		return enabledFlag;
	}

	public void setEnabledFlag(String enabledFlag) {
		this.enabledFlag = enabledFlag;
	}
	
	

}
