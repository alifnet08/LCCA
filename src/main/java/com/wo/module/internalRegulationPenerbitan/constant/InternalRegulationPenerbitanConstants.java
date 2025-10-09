package com.wo.module.internalRegulationPenerbitan.constant;

import java.util.Arrays;

import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.lov.bean.SelectorModel;
import com.wo.module.lov.bean.SelectorModel.SelectorInfo;

public abstract class InternalRegulationPenerbitanConstants {
	
	public final static String NAVIGATE_EDIT = "internalRegulationPenerbitanEdit.faces";
	public final static String NAVIGATE_SEARCH = "internalRegulationPenerbitan.faces";
	
	public final static String SEARCH_BY_REGULATION_TITLE = "REGULATION_TITLE";
	public final static String SEARCH_BY_REFERENCE_NO = "REFERENCE_NO";
	public final static String SEARCH_BY_REGULATION_TYPE = "REGULATION_TYPE";
	public final static String SEARCH_BY_REGULATION_IN_DATE_START = "REGULATION_IN_DATE_START";
	public final static String SEARCH_BY_REGULATION_IN_DATE_END = "REGULATION_IN_DATE_END";
	public final static String SEARCH_BY_WORK_UNIT_TPG = "WORK_UNIT_TPG";
	public final static String SEARCH_BY_REGULATION_STATUS = "REGULATION_STATUS";
	public final static String SEARCH_BY_PROCESS_STATUS = "PROCESS_STATUS";
	public final static String SEARCH_BY_PIC_IRG_NIK = "PIC_IRG_NIK";
	
	public final static String UPLOAD_TYPE_IRG_ATTACHMENT = "UPLOAD_TYPE_IRG_ATTACHMENT";
	
	public final static String STRING_EMPTY = "";
	
	public final static String RUNNING_NUMBER_TYPE = "IRG";
	
	public final static String EMAIL_IRG_REVIEW = "EMAIL_IRG_REVIEW";
	public final static String EMAIL_IRG_REMINDER = "EMAIL_IRG_REMINDER";
	
	public static SelectorInfo buildSelectorPIC(FacesUtil facesUtil) {
		SelectorModel.SelectorInfo info = new SelectorModel.SelectorInfo(
                " SELECT USER_ID, NIK, NAME, division_name from wo_mst_user " +
                "  WHERE 1=1 " +
                "        AND ( upper(NAME) like upper('%{0}%') or upper(NIK) like upper('%{0}%') " + 
                "                            or upper(division_name) like upper('%{0}%') ) " +
                "        AND ('{1}' = '0' OR division_id = '{1}') AND ('{1}' <> '0' " +
                "                         OR (division_id LIKE '%%' OR division_id IS NULL)) " +
                "        AND enabled_flag = 'Y' " +
                "  ORDER BY NAME ",
                " SELECT COUNT(1) from wo_mst_user " +
                "  WHERE 1=1 " +
                "        AND ( upper(NAME) like upper('%{0}%') or upper(NIK) like upper('%{0}%') " +
                "                            or upper(division_name) like upper('%{0}%') ) " +
                "        AND ('{1}' = '0' OR division_id = '{1}') AND ('{1}' <> '0' " +
                "                         OR (division_id LIKE '%%' OR division_id IS NULL)) " +
                "        AND enabled_flag = 'Y' ",
                Arrays.asList("NPK", "Name", "Division Name"),
                Arrays.asList("1", "2", "3"),false);
		return info;

	}
	
	public static SelectorInfo buildSelectorPICIrg(FacesUtil facesUtil) {
		SelectorModel.SelectorInfo info = new SelectorModel.SelectorInfo(
                " SELECT USER_ID, NIK, NAME, division_name from wo_mst_user " +
                "  WHERE 1=1 " +
                "        AND ( upper(NAME) like upper('%{0}%') or upper(NIK) like upper('%{0}%') ) " +
                "        AND enabled_flag = 'Y' " +
                "		 AND DIVISION_NAME = 'CORPORATE SECRETARY' "+
                "  ORDER BY NAME ",
                " SELECT COUNT(1) from wo_mst_user " +
                "  WHERE 1=1 " +
                "        AND ( upper(NAME) like upper('%{0}%') or upper(NIK) like upper('%{0}%') ) " +
                "        AND enabled_flag = 'Y' " +
                "		 AND DIVISION_NAME = 'CORPORATE SECRETARY' ",
                Arrays.asList("NPK", "Name"),
                Arrays.asList("1", "2"),false);
		return info;

	}
		
}
