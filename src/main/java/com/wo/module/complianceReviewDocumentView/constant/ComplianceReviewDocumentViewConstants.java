package com.wo.module.complianceReviewDocumentView.constant;

import java.util.Arrays;

import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.lov.bean.SelectorModel;
import com.wo.module.lov.bean.SelectorModel.SelectorInfo;

public abstract class ComplianceReviewDocumentViewConstants {

	public final static String NAVIGATE_EDIT = "complianceReviewDocumentViewEdit.faces";
	public final static String NAVIGATE_SEARCH = "complianceReviewDocumentView.faces";
	
	public final static String WHERE_DOCUMET_TYPE = "DOCUMENT_TYPE";
	public final static String WHERE_HUK_NO = "HUK_NO";
	public final static String WHERE_REMARKS = "REMARKS";
	public final static String WHERE_RECEIVED_DATE_START = "RECEIVED_DATE_START";
	public final static String WHERE_RECEIVED_DATE_END = "RECEIVED_DATE_END";
	public final static String WHERE_COMPLETE_DATE_START = "COMPLETE_DATE_START";
	public final static String WHERE_COMPLETE_DATE_END = "COMPLETE_DATE_END";
	public final static String WHERE_DOCUMENT_SUBMITTER = "DOCUMENT_SUBMITTER";
	public final static String UPLOAD_TYPE_DOCUMENT = "UPLOAD_TYPE_DOCUMENT";
	
	public static SelectorInfo buildSelectorPICCompliance(FacesUtil facesUtil) {
		SelectorModel.SelectorInfo info = new SelectorModel.SelectorInfo(
				" select user_id,nik,name,email from wo_mst_user "
				+ " where (upper(nik) like upper('%{0}%') OR upper(name) like upper('%{0}%')) "
				+ " and division_name = (select name_in from wo_mst_parameter_dtl where parameter_dtl_code = 'COMPLIANCE_DIVISION') "
				+ " and enabled_flag = 'Y' "
				+ " order by nik ",
				" SELECT COUNT(1) from wo_mst_user "
				+ " where (upper(nik) like upper('%{0}%') OR upper(name) like upper('%{0}%')) "
				+ " and division_name = (select name_in from wo_mst_parameter_dtl where parameter_dtl_code = 'COMPLIANCE_DIVISION') "
				+ " and enabled_flag = 'Y' ",
				Arrays.asList(facesUtil.retrieveMessage("formComplianceReviewDocumentNIK"),
						facesUtil.retrieveMessage("formComplianceReviewDocumentName"),
						facesUtil.retrieveMessage("formComplianceReviewDocumentEmail")),
				Arrays.asList("1", "2", "3"), false);
		return info;
	}
}
