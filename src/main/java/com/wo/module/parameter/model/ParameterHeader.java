package com.wo.module.parameter.model;

import java.io.Serializable;
import java.util.List;

import com.wo.module.common.model.BaseEntity;

public class ParameterHeader extends BaseEntity implements Serializable {
	private static final long serialVersionUID = -4002920880949216579L;
	public static final String PARAM_HEAD_CODE_SYSTEM_PROP_DATA_TYPE = "SYSTEM_PROPERTY_DATA_TYPE";
	public static final String PARAM_HEAD_CODE_COMPLIANCE_CHECK_STATUS = "COMPLIANCE_CHECK_STATUS";
	public static final String PARAM_HEAD_CODE_COMPLIANCE_TESTING_RATING = "COMPLIANCE_TESTING_RATING";
	public static final String PARAM_HEAD_CODE_DATA_STATUS = "DATA_STATUS";
	public static final String PARAM_HEAD_CODE_EMAIL_TEMPLATE = "EMAIL_TEMPLATE";
	public static final String PARAM_HEAD_CODE_REMINDER_PIC = "REMINDER_PIC";
	public static final String PARAM_HEAD_CODE_REMINDER_STATUS = "REMINDER_STATUS";
	public static final String PARAM_HEAD_CODE_RECURRING_TYPE = "RECURRING_TYPE";
	public static final String PARAM_HEAD_CODE_SENDER = "SENDER";
	public static final String PARAM_HEAD_CODE_SENDER_AML = "SENDER_AML";
	public static final String PARAM_HEAD_CODE_REVIEW_CATEGORY = "REVIEW_CATEGORY";
	
	public static final String PARAM_HEAD_CODE_ATTENDANCE = "ATTENDANCE";
	public static final String PARAM_HEAD_CODE_CORRESPONDENCE_TYPE = "CORRESPONDENCE_TYPE";
	
	public static final String PARAM_HEAD_CODE_AUDITOR = "AUDITOR";
	public static final String PARAM_HEAD_CODE_AUDIT_OBJECT = "AUDIT_OBJECT";
	public static final String PARAM_HEAD_CODE_AUDIT_CATEGORY = "AUDIT_CATEGORY";
	
	public static final String PARAM_HEAD_CODE_LOG_PROCESS_STATUS = "LOG_PROCESS_STATUS";
	public static final String PARAM_HEAD_CODE_LOG_FUNCTION_ID = "LOG_FUNCTION_ID";
	
	public static final String PARAM_HEAD_CODE_JENIS_KETENTUAN = "JENIS_KETENTUAN";
	
	public static final String PARAM_HEAD_CODE_DOCUMENT_TYPE = "DOCUMENT_TYPE";
	public static final String PARAM_HEAD_CODE_DOCUMENT_SUBMITTER = "DOCUMENT_SUBMITTER";
	
	public static final String PARAM_HEAD_CODE_TRACK_RECORD = "TRACK_RECORD";
	public static final String PARAM_HEAD_CODE_APPROVAL_STATUS = "APPROVAL_STATUS";
	public static final String PARAM_HEAD_CODE_PIC_FOLLOWUP_STATUS = "PIC_FOLLOWUP_STATUS";
	
	//##### LITIGATION #####
	//unused
	public static final String PARAM_HEAD_FINE_CATEGORY = "FINE_CATEGORY";
	public static final String PARAM_HEAD_FAQ_CATEGORY = "FAQ_CATEGORY";
	public static final String PARAM_HEAD_FAQ_STATUS = "FAQ_STATUS";
	public static final String PARAM_HEAD_ARTICLE_TYPE ="ARTICLE_TYPE";
	public static final String PARAM_HEAD_STATIC_PAGE_CATEGORY = "STATIC_PAGE_CATEGORY";
	public static final String PARAM_HEAD_TEMPLATE_CATEGORY = "TEMPLATE_CATEGORY";
	public static final String PARAM_HEAD_TEMPLATE_SUB_CATEGORY = "TEMPLATE_SUBCATEGORY";
	public static final String PARAM_HEAD_NOTARY_CATEGORY = "NOTARY_CATEGORY";
	public static final String PARAM_HEAD_LITIGATION_BRANCH_OFFICE = "LITIGATION_BRANCH_OFFICE";
	public static final String PARAM_HEAD_LITIGATION_PLACE = "LITIGATION_PLACE";
	public static final String PARAM_HEAD_LITIGATION_DEBITUR = "LITIGATION_DEBITUR";
	public static final String PARAM_HEAD_LITIGATION_COURT_NAME = "LITIGATION_COURT_NAME";
	public static final String PARAM_HEAD_LITIGATION_PROGRESS_PN = "LITIGATION_PROGRESS_PN";
	public static final String PARAM_HEAD_LITIGATION_PROGRESS_PT = "LITIGATION_PROGRESS_PT";
	public static final String PARAM_HEAD_LITIGATION_PROGRESS_MA = "LITIGATION_PROGRESS_MA";
	public static final String PARAM_HEAD_LITIGATION_DECISION = "LITIGATION_DECISION";
	public static final String PARAM_HEAD_LITIGATION_VIEWER_STATUS = "LITIGATION_VIEWER_STATUS";
	public static final String PARAM_HEAD_LITIGATION_LEGAL_EFFORT_PT = "LITIGATION_LEGAL_EFFORT_PT"; 
	public static final String PARAM_HEAD_LITIGATION_LEGAL_EFFORT_PN = "LITIGATION_LEGAL_EFFORT_PN"; 
	public static final String PARAM_HEAD_LITIGATION_LEGAL_EFFORT_MA = "LITIGATION_LEGAL_EFFORT_MA";
	
	//newly created and unused (?)
	public static final String PARAM_HEAD_LITIGATION_CASE_HANDLER = "LITIGATION_CASE_HANDLER";
	public static final String PARAM_HEAD_LITIGATION_RELIGIOUS_COURT = "LITIGATION_RELIGIOUS_COURT";
	public static final String PARAM_HEAD_LITIGATION_COUNTRY_COURT = "LITIGATION_COUNTRY_COURT";
	
	//currently used
	public static final String PARAM_HEAD_LITIGATION_CASE_TYPE = "LITIGATION_CASE_TYPE";
	public static final String PARAM_HEAD_LITIGATION_NO = "LITIGATION_NO";
	public static final String PARAM_HEAD_LITIGATION_COURT_TYPE = "LITIGATION_COURT_TYPE";
	public static final String PARAM_HEAD_LITIGATION_CASE_TYPE_DTL_PERDATA = "LITIGATION_CASE_TYPE_DTL_PERDATA";
	public static final String PARAM_HEAD_LITIGATION_CASE_TYPE_DTL_PIDANA = "LITIGATION_CASE_TYPE_DTL_PIDANA";
	public static final String PARAM_HEAD_LITIGATION_DECISION_STATUS = "LITIGATION_DECISION_STATUS";
	public static final String PARAM_HEAD_LITIGATION_JUDGEMENT_WARNING = "LITIGATION_JUDGEMENT_WARNING";
	public static final String PARAM_HEAD_LITIGATION_FOREIGN_CURRENCY_TYPE = "LITIGATION_FOREIGN_CURRENCY_TYPE";
	public static final String PARAM_HEAD_LITIGATION_CASE_TEAM_HANDLER = "LITIGATION_CASE_TEAM_HANDLER";
	public static final String PARAM_HEAD_LITIGATION_CASE_MAIN_TOPIC = "LITIGATION_CASE_MAIN_TOPIC";
	public static final String PARAM_HEAD_LITIGATION_CASE_SUB_TOPIC = "LITIGATION_CASE_SUB_TOPIC";
	public static final String PARAM_HEAD_LITIGATION_COMMERCIAL_COURT = "LITIGATION_COMMERCIAL_COURT";

	//##### LITIGATION - END #####
		
	public static final String PARAM_HEAD_FINE_REPORT_NAME = "FINE_REPORT_NAME";
	public static final String PARAM_HEAD_INSTITUTION_NAME = "INSTITUTION_NAME";
	public static final String PARAM_HEAD_CPSA_TYPE = "COMPLIANCE_PLAN_SELF_ASSESSMENT_TYPE";
	public static final String PARAM_HEAD_CPSA_STATUS = "CPSA_STATUS";
	
	public static final String PARAM_QNA_SLA = "QNA_SLA";
	public static final String PARAM_QNA_CATEGORY = "QNA_CATEGORY";
	public static final String PARAM_QNA_STATUS = "QNA_STATUS";
	
	public static final String PARAM_HEAD_CODE_SYSTEM = "SYSTEM";
	
	public static final String PARAM_HEAD_CODE_USER_CREATED_BY = "USER_CREATED_BY";
	
	public static final String PARAM_HEAD_OPEN_CLOSE_REGULATION_OBSOLETE = "OPEN_CLOSE_REGULATION_OBSOLETE";
	
	public static final String PARAM_HEAD_FINE_TARGET_DATE_STATUS = "FINE_TARGET_DATE_STATUS";
	
	public static final String PARAM_HEAD_RECURRING_TYPE = "RECURRING_TYPE";
	
	private Long parameterId;
	private String parameterCode;
	private String nameIn;
	private String nameEn;

	private List<ParameterDetail> listDetail;

	public Long getParameterId() {
		return parameterId;
	}

	public void setParameterId(Long parameterId) {
		this.parameterId = parameterId;
	}

	public String getParameterCode() {
		return parameterCode;
	}

	public void setParameterCode(String parameterCode) {
		this.parameterCode = parameterCode;
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

	public List<ParameterDetail> getListDetail() {
		return listDetail;
	}

	public void setListDetail(List<ParameterDetail> listDetail) {
		this.listDetail = listDetail;
	}

}
