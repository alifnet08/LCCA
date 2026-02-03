package com.wo.module.internalRegulationObsolete.constant;

import java.util.Arrays;

import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.lov.bean.SelectorModel;
import com.wo.module.lov.bean.SelectorModel.SelectorInfo;

public abstract class InternalRegulationObsoleteConstants {
	
	public final static String NAVIGATE_EDIT = "internalRegulationObsoleteEdit.faces";
	public final static String NAVIGATE_SEARCH = "internalRegulationObsolete.faces";
	
	public final static String WHERE_REGULATION_ID = "REGULATION_ID";
	
	public final static String WHERE_JUDUL_OBSOLETE = "JUDUL_OBSOLETE";
	public final static String WHERE_NO_OBSOLETE = "NOMOR_OBSOLETE";
	public final static String WHERE_INFO_OBSOLETE = "INFO_OBSOLETE";
	public final static String WHERE_START_DATE_OBSOLETE = "START_DATE_OBSOLETE";
	public final static String WHERE_END_DATE_OBSOLETE = "END_DATE_OBSOLETE";
	public final static String WHERE_TIPE_OBSOLETE = "TIPE_OBSOLETE";
	public final static String WHERE_PUBLISHER_DIVISION = "PUBLISHER_DIVISION";
	public final static String WHERE_OPEN_CLOSE_OBS_REGULATION = "OPEN_CLOSE_REG_OBSOLETE";
	public final static String WHERE_PIC_IRG_NAME = "PIC_IRG_NAME";
		
	//Email SLA Type Difference
	public final static String REMINDER_H_MINUS_2 = "2";
	public final static String REMINDER_H_MINUS_30 = "30";
	public final static String REMINDER_H_PLUS_0 = "0";
	
	
	public final static SelectorInfo buildSelectorUser() {
		SelectorModel.SelectorInfo info = new SelectorModel.SelectorInfo(
                " select USER_ID, NIK, NAME, division_name from wo_mst_user "
                + " where 1=1 "
                + " and ( upper(NAME) like upper('%{0}%') or upper(NIK) like upper('%{0}%') or upper(division_name) like upper('%{0}%') ) "
                //+ " and case when ('{1}' = '0') then (division_id like '%%' or division_id is null) else division_id like '%{1}%' end "
                + " AND ('{1}' = '0' OR division_id = '{1}') AND ('{1}' <> '0' OR division_id LIKE '%%' OR division_id IS NULL) "
                + " and enabled_flag = 'Y' "
                + " order by NAME "  ,
                " SELECT COUNT(1) from wo_mst_user "
                + " where 1=1 "
                + " and ( upper(NAME) like upper('%{0}%') or upper(NIK) like upper('%{0}%') or upper(division_name) like upper('%{0}%') ) "
                + " AND ('{1}' = '0' OR division_id = '{1}') AND ('{1}' <> '0' OR division_id LIKE '%%' OR division_id IS NULL) "
                + " and enabled_flag = 'Y' ",
                Arrays.asList("NPK", "Name", "Division Name"),
                Arrays.asList("1", "2", "3"),false);
        return info;
	}
	//client want Division for PIC IRG is only specified to "CORPORATE SECRETARY"
	public static SelectorInfo buildSelectorPICIrg(FacesUtil facesUtil) {
		SelectorModel.SelectorInfo info = new SelectorModel.SelectorInfo(
                " SELECT USER_ID, NIK, NAME, division_name from wo_mst_user " +
                "  WHERE 1=1 " +
                "        AND ( upper(NAME) like upper('%{0}%') or upper(NIK) like upper('%{0}%') ) " +
                "        AND enabled_flag = 'Y' "+ 
                "		 AND UPPER(DIVISION_NAME) = UPPER('CORPORATE SECRETARY') "+
                "  ORDER BY NAME ",
                " SELECT COUNT(1) from wo_mst_user " +
                "  WHERE 1=1 " +
                "        AND ( upper(NAME) like upper('%{0}%') or upper(NIK) like upper('%{0}%') ) "+
                "        AND enabled_flag = 'Y' "+
                "		 AND UPPER(DIVISION_NAME) = UPPER('CORPORATE SECRETARY') ",
                Arrays.asList("NPK", "Name"),
                Arrays.asList("1", "2"),false);
		return info;

	}
	
}
