package com.wo.module.cpsa.constant;

import java.util.Arrays;

import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.lov.bean.SelectorModel;
import com.wo.module.lov.bean.SelectorModel.SelectorInfo;

public class CompliancePlanSelfAssessmentConstant {
	
	public static final String TEMPLATE_FILE_PATH = "com/wo/template/templateCpsaQuestion.xlsx";
	public static final String TEMPLATE_FILE_EDITABLE_PATH = "com/wo/template/templateCpsaQuestionEdit.xlsx";
	public final static String TEMPLATE_FILE_UPLOAD_CPSA = "com/wo/template/templateUploadCpsaQuestion.xlsx";
	
	public static final String SEARCH_BY_CPSA_TYPE = "SEARCH_BY_CPSA_TYPE";
	public static final String SEARCH_BY_CPSA_NAME = "SEARCH_BY_CPSA_NAME";
	public static final String SEARCH_BY_UPLOAD_DATE = "SEARCH_BY_UPLOAD_DATE";	
	public static final String SEARCH_BY_LETTER_NO = "SEARCH_BY_LETTER_NO";
	public static final String SEARCH_BY_LETTER_DATE = "SEARCH_BY_LETTER_DATE";
	public static final String SEARCH_BY_PERIOD_START_FROM = "SEARCH_BY_PERIOD_START_FROM";
	public static final String SEARCH_BY_PERIOD_START_TO = "SEARCH_BY_PERIOD_START_TO";
	public static final String SEARCH_BY_PERIOD_END_FROM = "SEARCH_BY_PERIOD_END_FROM";
	public static final String SEARCH_BY_PERIOD_END_TO = "SEARCH_BY_PERIOD_END_TO";
	public static final String SEARCH_BY_BRANCH_SUB_BRANCH = "SEARCH_BY_BRANCH_SUB_BRANCH";
	public static final String SEARCH_BY_CPSA_STATUS = "SEARCH_BY_CPSA_STATUS";
	public static final String SEARCH_BY_WORKING_UNIT = "SEARCH_BY_WORKING_UNIT";
	public static final String SEARCH_BY_BRANCH = "SEARCH_BY_BRANCH";
	public static final String SEARCH_BY_PIC_1 = "SEARCH_BY_PIC_1";
	
	public static final String NAVIGATE_CPSA = "compliancePlanSelfAssessment.faces";
	public static final String NAVIGATE_CPSA_EDIT = "compliancePlanSelfAssessmentEdit.faces";
	public static final String NAVIGATE_CPSA_VIEW = "cpsaView.faces";
	public static final String NAVIGATE_CPSA_VIEW_DETAIL = "cpsaViewDetail.faces";
	
	public static final String STRING_EMPTY = "";
	
	public static final String CPSA_DOCUMENT = "CPSA_DOCUMENT";
	
	public static final String FOLDER_CPSA = "CPSA";
	
	public static final String FILE_NAME_CPSA = "CPSA";
	
	public static final String COMPLIANCE_PLAN_SELF_ASSESSMENT_TYPE_CPSA = "COMPLIANCE_PLAN_SELF_ASSESSMENT_TYPE_CPSA";
	
	public static final String STATUS_CPSA_CODE_INPROGRESS = "CPSA_INPROGRESS";
	public static final String STATUS_CPSA_CODE_COMPLETED = "CPSA_COMPLETED";
	
	public static final String STATUS_CPSA_NAME_INPROGRESS = "Inprogress";
	public static final String STATUS_CPSA_NAME_COMPLETED = "Completed";
	
	public final static String EMAIL_CPSA = "EMAIL_CPSA";
	
	
	public static SelectorInfo buildSelectorPIC(FacesUtil facesUtil) {
		SelectorModel.SelectorInfo info = new SelectorModel.SelectorInfo(
                " select USER_ID, NIK, NAME, DIVISION_NAME, BRANCH_CODE, BRANCH_NAME " + 
		        "   from wo_mst_user " +
                "  where 1=1 " +
                "   	 AND (upper(NAME) like upper('%{0}%') or upper(NIK) like upper('%{0}%') " + 
                "                           or upper(DIVISION_NAME) like upper('%{0}%') " + 
                "                           or upper(BRANCH_CODE) like upper('%{0}%') " +
                "                           or upper(BRANCH_NAME) like upper('%{0}%')) " +
//                "        AND ('{1}' = '0' OR division_id = {1}) " +
//                "        AND ('{1}' <> '0' OR (division_id LIKE '%%' OR division_id IS NULL)) " +
//                "        AND ('{2}' = '' OR branch_code LIKE '{2}') " +
//                "        AND ('{2}' <> '' OR (branch_code LIKE '%' OR branch_code IS NULL)) " +
                "        AND enabled_flag = 'Y' " +
                "  order by NAME "  ,
                " SELECT COUNT(1) " +
                "   FROM wo_mst_user " +
                "  WHERE 1=1 " +
                "        AND (upper(NAME) like upper('%{0}%') or upper(NIK) like upper('%{0}%') " + 
                "						    or upper(division_name) like upper('%{0}%') " + 
                "                           or upper(BRANCH_CODE) like upper('%{0}%') " +
                "                           or upper(BRANCH_NAME) like upper('%{0}%')) " +
//                " 		 AND ('{1}' = '0' OR division_id = {1}) " +
//                "        AND ('{1}' <> '0' OR (division_id LIKE '%%' OR division_id IS NULL)) " +
//                "        AND ('{2}' = '' OR branch_code LIKE '{2}') " +
//                "        AND ('{2}' <> '' OR (branch_code LIKE '%' OR branch_code IS NULL)) " +
                " 		 AND enabled_flag = 'Y' ",
                Arrays.asList("NPK", "Name", "Division Name", "Branch Code", "Branch Name"),
                Arrays.asList("1", "2", "3", "4", "5"),false);
		return info;

	}
	
	public static SelectorInfo buildSelectorBranch(FacesUtil facesUtil) {
		SelectorModel.SelectorInfo info = new SelectorModel.SelectorInfo(
                " select distinct division_name, branch_code, branch_name, sub_branch_name " + 
                "   from wo_mst_user " +
                "  where 1=1 " +
                "        and (upper(branch_name) like upper('%{0}%') or upper(branch_code) like upper('%{0}%') " + 
                "             or upper(sub_branch_name) like upper('%{0}%') or upper(division_name) like upper('%{0}%') ) " +
               // " 		 and ('{1}' = '0' OR division_id LIKE '{1}') " + 
               // "        and ('{1}' <> '0' OR (division_id LIKE '%' OR division_id IS NULL)) " +
                " 		 and enabled_flag = 'Y' " +
                "  order by branch_code "  ,
                " select count(1) " + 
                "   from (select distinct division_name, branch_code, branch_name, sub_branch_name " + 
                "           from wo_mst_user " +
                "          where 1=1 " +
                "                and (upper(branch_name) like upper('%{0}%') or upper(branch_code) like upper('%{0}%') " + 
                "                     or upper(sub_branch_name) like upper('%{0}%') or upper(division_name) like upper('%{0}%') ) " +
             //   "                and ('{1}' = '0' OR division_id LIKE '{1}') " +
             //   "                and ('{1}' <> '0' OR (division_id LIKE '%' OR division_id IS NULL)) " +
                "                and enabled_flag = 'Y') ",
                Arrays.asList("Division Name", "Sub Branch Code", "Branch Name", "Sub Branch Name"),
                Arrays.asList("0", "1", "2", "3"),false);
		return info;

	}
	
}
