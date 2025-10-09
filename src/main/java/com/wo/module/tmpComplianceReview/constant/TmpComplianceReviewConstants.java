package com.wo.module.tmpComplianceReview.constant;

import java.util.Arrays;

import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.lov.bean.SelectorModel;
import com.wo.module.lov.bean.SelectorModel.SelectorInfo;

public abstract class TmpComplianceReviewConstants {
	public final static String NAVIGATE_EDIT = "tmpComplianceReviewEdit.faces";
	public final static String NAVIGATE_SEARCH = "tmpComplianceReview.faces";

	public final static String UPLOAD_TYPE_DOCUMENT = "UPLOAD_TYPE_DOCUMENT";
	public final static String UPLOAD_TYPE_FOLLOWUP_POINTS = "UPLOAD_TYPE_FOLLOWUP_POINTS";
	
	public final static String WHERE_REVIEW_CATEGORY = "REVIEW_CATEGORY";
	public final static String WHERE_REVIEWED_BRANCH = "REVIEWED_BRANCH";
	public final static String WHERE_DOC_NO = "DOC_NO";
	public final static String WHERE_DOC_DATE_START = "DOC_DATE_START";
	public final static String WHERE_DOC_DATE_END = "DOC_DATE_END";
	public final static String WHERE_REGARDING = "REGARDING";
	public final static String WHERE_COMPLIANCE_STATUS = "COMPLIANCE_STATUS";
	public final static String WHERE_TARGET_DATE_START = "TARGET_DATE_START";
	public final static String WHERE_TARGET_DATE_END = "TARGET_DATE_END";
	public final static String WHERE_STATUS = "STATUS";
	
	public final static String APPROVE_BY_SYSTEM = "APPROVE_BY_SYSTEM";
	
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
				Arrays.asList(facesUtil.retrieveMessage("formTmpComplianceReviewNIK"),
						facesUtil.retrieveMessage("formTmpComplianceReviewName"),
						facesUtil.retrieveMessage("formTmpComplianceReviewEmail")),
				Arrays.asList("1", "2", "3"), false);
		return info;

	}

	public static SelectorInfo buildSelectorDivision(FacesUtil facesUtil) {
		SelectorModel.SelectorInfo info = new SelectorModel.SelectorInfo(
				"select division_id, division_name from wo_mst_user where upper(division_name) like upper('%{0}%') and enabled_flag = 'Y' order by nik ",
				"SELECT COUNT(1) from wo_mst_user where upper(division_name) like upper('%{0}%') and enabled_flag = 'Y' ",
				Arrays.asList(facesUtil.retrieveMessage("formTmpComplianceReviewDivision")), Arrays.asList("1"),
				false);
		return info;

	}

	public static SelectorInfo buildSelectorPIC(FacesUtil facesUtil) {
		SelectorModel.SelectorInfo info = new SelectorModel.SelectorInfo(
                " select USER_ID, NIK, NAME, division_name from wo_mst_user "
                + " where 1=1 "
                + " and ( upper(NAME) like upper('%{0}%') or upper(NIK) like upper('%{0}%') or upper(division_name) like upper('%{0}%') ) "
                +" AND ('{1}' = '0' OR division_id LIKE '%{1}%') AND ('{1}' <> '0' OR (division_id LIKE '%%' OR division_id IS NULL))"
                + " and enabled_flag = 'Y' "
                + " order by NAME "  ,
                " SELECT COUNT(1) from wo_mst_user "
                + " where 1=1 "
                + " and ( upper(NAME) like upper('%{0}%') or upper(NIK) like upper('%{0}%') or upper(division_name) like upper('%{0}%') ) "
                +" AND ('{1}' = '0' OR division_id LIKE '%{1}%') AND ('{1}' <> '0' OR (division_id LIKE '%%' OR division_id IS NULL))"
                + " and enabled_flag = 'Y' ",
                Arrays.asList(facesUtil.retrieveMessage("formTmpComplianceReviewNIK"),
						facesUtil.retrieveMessage("formTmpComplianceReviewName"),
						facesUtil.retrieveMessage("formTmpComplianceReviewDivision")),
                Arrays.asList("1", "2", "3"),false);
		return info;

	}

}
