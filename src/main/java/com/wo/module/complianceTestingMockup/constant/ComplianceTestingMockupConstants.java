package com.wo.module.complianceTestingMockup.constant;

import java.util.Arrays;

import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.lov.bean.SelectorModel;
import com.wo.module.lov.bean.SelectorModel.SelectorInfo;

public abstract class ComplianceTestingMockupConstants {
	public final static String NAVIGATE_EDIT = "complianceTestingEdit.faces";
	public final static String NAVIGATE_SEARCH = "complianceTesting.faces";
	
	public final static String WHERE_INSPECTION_TITLE = "INSPECTION_TITLE";
	public final static String WHERE_INSPECTION_NO = "INSPECTION_NO";
	public final static String WHERE_STATUS = "STATUS";
	public final static String WHERE_COMPLIANCE_STATUS = "COMPLIANCE_STATUS";
	public final static String WHERE_FOLLOWUP_STATUS = "FOLLOWUP_STATUS";
	public final static String WHERE_PERIOD_FROM = "PERIOD_FROM";
	public final static String WHERE_PERIOD_TO = "PERIOD_TO";

	public static SelectorInfo buildSelectorPICCompliance(FacesUtil facesUtil) {
		SelectorModel.SelectorInfo info = new SelectorModel.SelectorInfo(
				" select user_id,nik,name,email from wo_mst_user "
				+ " where (upper(nik) like upper('%{0}%') OR upper(name) like upper('%{0}%')) "
				//+ " and division_name = (select name_in from wo_mst_parameter_dtl where parameter_dtl_code = 'COMPLIANCE_DIVISION') "
				+ " and enabled_flag = 'Y' "
				+ " order by nik ",
				" SELECT COUNT(1) from wo_mst_user "
				+ " where (upper(nik) like upper('%{0}%') OR upper(name) like upper('%{0}%')) "
				//+ " and division_name = (select name_in from wo_mst_parameter_dtl where parameter_dtl_code = 'COMPLIANCE_DIVISION') "
				+ " and enabled_flag = 'Y' ",
				Arrays.asList(facesUtil.retrieveMessage("formTmpComplianceReviewNIK"),
						facesUtil.retrieveMessage("formTmpComplianceReviewName"),
						facesUtil.retrieveMessage("formTmpComplianceReviewEmail")),
				Arrays.asList("1", "2", "3"), false);
		return info;

	}
	
//	public static SelectorInfo buildSelectorPIC(FacesUtil facesUtil) {
//		SelectorModel.SelectorInfo info = new SelectorModel.SelectorInfo(
//                " select USER_ID, NIK, NAME, division_name, BRANCH_CODE, BRANCH_NAME from wo_mst_user "
//                + " where 1=1 "
//                + " and ( upper(NAME) like upper('%{0}%') or upper(NIK) like upper('%{0}%') or upper(division_name) like upper('%{0}%') ) "
//                +" AND ('{1}' = '0' OR division_id LIKE '{1}') AND ('{1}' <> '0' OR (division_id LIKE '%' OR division_id IS NULL))"
//                +" AND ('{2}' = '' OR branch_code LIKE '{2}') AND ('{2}' <> '' OR (branch_code LIKE '%' OR branch_code IS NULL))"
//                + " and enabled_flag = 'Y' "
//                + " order by NAME "  ,
//                " SELECT COUNT(1) from wo_mst_user "
//                + " where 1=1 "
//                + " and ( upper(NAME) like upper('%{0}%') or upper(NIK) like upper('%{0}%') or upper(division_name) like upper('%{0}%') ) "
//                 +" AND ('{1}' = '0' OR division_id LIKE '{1}') AND ('{1}' <> '0' OR (division_id LIKE '%' OR division_id IS NULL))"
//                 +" AND ('{2}' = '' OR branch_code LIKE '{2}') AND ('{2}' <> '' OR (branch_code LIKE '%' OR branch_code IS NULL))"
//                + " and enabled_flag = 'Y' ",
//                Arrays.asList("NPK", "Name", "Division Name","Sub Branch Code","Branch Name"),
//                Arrays.asList("1", "2", "3", "4", "5"),false);
//		return info;
//
//	}
	
	public static SelectorInfo buildSelectorPIC(FacesUtil facesUtil) {
		SelectorModel.SelectorInfo info = new SelectorModel.SelectorInfo(
                " select USER_ID, NIK, NAME, division_name, BRANCH_CODE, BRANCH_NAME from wo_mst_user "
                + " where 1=1 "
                + " and ( upper(NAME) like upper('%{0}%') or upper(NIK) like upper('%{0}%') or upper(division_name) like upper('%{0}%') ) "
                + " and enabled_flag = 'Y' "
                + " order by NAME "  ,
                " SELECT COUNT(1) from wo_mst_user "
                + " where 1=1 "
                + " and ( upper(NAME) like upper('%{0}%') or upper(NIK) like upper('%{0}%') or upper(division_name) like upper('%{0}%') ) "
                + " and enabled_flag = 'Y' ",
                Arrays.asList("NPK", "Name", "Division Name","Sub Branch Code","Branch Name"),
                Arrays.asList("1", "2", "3", "4", "5"),false);
		return info;

	}
	
//	public static SelectorInfo buildSelectorBranch(FacesUtil facesUtil) {
//		SelectorModel.SelectorInfo info = new SelectorModel.SelectorInfo(
//                " select distinct division_name, BRANCH_CODE, BRANCH_NAME, SUB_BRANCH_NAME from wo_mst_user "
//                + " where 1=1 "
//                + " and ( upper(BRANCH_NAME) like upper('%{0}%') or upper(BRANCH_CODE) like upper('%{0}%') or upper(division_name) like upper('%{0}%') ) "
//                +" AND ('{1}' = '0' OR division_id LIKE '{1}') AND ('{1}' <> '0' OR (division_id LIKE '%' OR division_id IS NULL))"
//                + " and enabled_flag = 'Y' "
//                + " order by BRANCH_CODE "  ,
//                " SELECT COUNT(1) from ( "
//                +" select distinct division_name, BRANCH_CODE, BRANCH_NAME, SUB_BRANCH_NAME from wo_mst_user "
//                + " where 1=1 "
//                + " and ( upper(BRANCH_NAME) like upper('%{0}%') or upper(BRANCH_CODE) like upper('%{0}%') or upper(division_name) like upper('%{0}%') ) "
//                +" AND ('{1}' = '0' OR division_id LIKE '{1}') AND ('{1}' <> '0' OR (division_id LIKE '%' OR division_id IS NULL))"
//                + " and enabled_flag = 'Y') ",
//                Arrays.asList("Division Name", "Sub Branch Code", "Sub Branch Name", "Branch Name"),
//                Arrays.asList("0", "1", "3","2"),false);
//		return info;
//	}
	
	public static SelectorInfo buildSelectorBranch(FacesUtil facesUtil) {
		SelectorModel.SelectorInfo info = new SelectorModel.SelectorInfo(
                " select distinct division_name, BRANCH_CODE, BRANCH_NAME, SUB_BRANCH_NAME from wo_mst_user "
                + " where 1=1 "
                + " and ( upper(BRANCH_NAME) like upper('%{0}%') or upper(BRANCH_CODE) like upper('%{0}%')  ) "
                + " and enabled_flag = 'Y' "
                + " order by BRANCH_CODE "  ,
                " SELECT COUNT(1) from ( "
                +" select distinct division_name, BRANCH_CODE, BRANCH_NAME, SUB_BRANCH_NAME from wo_mst_user "
                + " where 1=1 "
                + " and ( upper(BRANCH_NAME) like upper('%{0}%') or upper(BRANCH_CODE) like upper('%{0}%')  ) "
                + " and enabled_flag = 'Y') ",
                Arrays.asList("Division Name", "Sub Branch Code", "Sub Branch Name", "Branch Name"),
                Arrays.asList("0", "1", "3","2"),false);
		return info;
	}
}
