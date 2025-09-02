package com.wo.module.cpsaVerification.constant;

import java.util.Arrays;

import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.lov.bean.SelectorModel;
import com.wo.module.lov.bean.SelectorModel.SelectorInfo;

public class CpsaVerificationConstant {
	
	public static final String TEMPLATE_FILE_PATH = "D:\\Document\\Maybank\\Document\\templateCpsaQuestion.xlsx";
	
	public static final String SHOW_APPROVAL = "APPROVAL";
	public static final String SHOW_VIEW = "VIEW";

	public static final String SEARCH_BY_CPSA_TYPE = "SEARCH_BY_CPSA_TYPE";
	public static final String SEARCH_BY_CPSA_NAME = "SEARCH_BY_CPSA_NAME";
	public static final String SEARCH_BY_UPLOAD_DATE = "SEARCH_BY_UPLOAD_DATE";	
	public static final String SEARCH_BY_LETTER_NO = "SEARCH_BY_LETTER_NO";
	public static final String SEARCH_BY_LETTER_DATE = "SEARCH_BY_LETTER_DATE";
	public static final String SEARCH_BY_PERIOD_START_FROM = "SEARCH_BY_PERIOD_START_FROM";
	public static final String SEARCH_BY_PERIOD_START_TO = "SEARCH_BY_PERIOD_START_TO";
	public static final String SEARCH_BY_PERIOD_END_FROM = "SEARCH_BY_PERIOD_END_FROM";
	public static final String SEARCH_BY_PERIOD_END_TO = "SEARCH_BY_PERIOD_END_TO";
	
	public static final String NAVIGATE_CPSA_VERIFICATION = "cpsaVerification.faces";
	public static final String NAVIGATE_CPSA_VERIFICATION_EDIT = "cpsaVerificationEdit.faces";
	
	public static final String STRING_EMPTY = "";
	
	public static final String CPSA_DOCUMENT = "CPSA_DOCUMENT";
	
	public static final String FOLDER_CPSA = "CPSA";
	
	public static final String FILE_NAME_CPSA = "CPSA";
	
	public static final String COMPLIANCE_PLAN_SELF_ASSESSMENT_TYPE_CPSA = "COMPLIANCE_PLAN_SELF_ASSESSMENT_TYPE_CPSA";
	
	public static final String STATUS_CPSA_INPROGRESS = "CPSA_INPROGRESS";
	public static final String STATUS_CPSA_WAITING_APPROVAL = "CPSA_WAITING_APPROVAL";
	public static final String STATUS_CPSA_APPROVED = "CPSA_APPROVED";
	public static final String STATUS_CPSA_REJECTED = "CPSA_REJECTED";
	public static final String STATUS_CPSA_VERIFICATION = "CPSA_VERIFICATION";
	
	public static final String STATUS_COMPLIANCE_OPEN = "COMPLIANCE_OPEN";
	public static final String STATUS_COMPLIANCE_CLOSE = "COMPLIANCE_CLOSE";
	
	public static final String STATUS_CPSA_NAME_INPROGRESS = "Inprogress";
	public static final String STATUS_CPSA_NAME_COMPLETED = "Completed";
	
	public static final String FLAG_Y = "Y";
	public static final String FLAG_N = "N";
	
	public static SelectorInfo buildSelectorPIC(FacesUtil facesUtil) {
		SelectorModel.SelectorInfo info = new SelectorModel.SelectorInfo(
                " select USER_ID, NIK, NAME, division_name from wo_mst_user "
                + " where 1=1 "
                + " and ( upper(NAME) like upper('%{0}%') or upper(NIK) like upper('%{0}%') or upper(division_name) like upper('%{0}%') ) "
                //+ " and case when ('{1}' = '0') then (division_id like '%%' or division_id is null) else division_id like '%{1}%' end "
                +" AND ('{1}' = '0' OR division_id LIKE '%{1}%') AND ('{1}' <> '0' OR (division_id LIKE '%%' OR division_id IS NULL))"
                + " and enabled_flag = 'Y' "
                + " order by NAME "  ,
                " SELECT COUNT(1) from wo_mst_user "
                + " where 1=1 "
                + " and ( upper(NAME) like upper('%{0}%') or upper(NIK) like upper('%{0}%') or upper(division_name) like upper('%{0}%') ) "
                //+ " and case when ('{1}' = '0') then (division_id like '%%' or division_id is null) else division_id like '%{1}%' end "
                +" AND ('{1}' = '0' OR division_id LIKE '%{1}%') AND ('{1}' <> '0' OR (division_id LIKE '%%' OR division_id IS NULL))"
                + " and enabled_flag = 'Y' ",
                Arrays.asList("NPK", "Name", "Division Name"),
                Arrays.asList("1", "2", "3"),false);
		return info;

	}
	
}
