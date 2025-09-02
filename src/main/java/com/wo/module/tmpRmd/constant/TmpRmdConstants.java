package com.wo.module.tmpRmd.constant;

import java.util.Arrays;
import java.util.Locale;

import javax.faces.context.FacesContext;

import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.lov.bean.SelectorModel;
import com.wo.module.lov.bean.SelectorModel.SelectorInfo;

public abstract class TmpRmdConstants {
	public final static String NAVIGATE_EDIT = "tmpRmdEdit.faces";
	public final static String NAVIGATE_SEARCH = "tmpRmd.faces";
	
	public final static String SEARCH_BY_REPORT_TYPE = "SEARCH_BY_REPORT_TYPE";
	public final static String SEARCH_BY_REPORT_NAME = "SEARCH_BY_REPORT_NAME";
	public final static String SEARCH_BY_STATUS = "SEARCH_BY_STATUS";
	
	
	public static SelectorInfo buildSelectorUser() {
		SelectorModel.SelectorInfo info = new SelectorModel.SelectorInfo(
                " select USER_ID, NIK, NAME, division_name from wo_mst_user "
                + " where 1=1 "
                + " and ( upper(NAME) like upper('%{0}%') or upper(NIK) like upper('%{0}%') or upper(division_name) like upper('%{0}%') ) "
                + " AND ('{1}' = '0' OR division_id LIKE '%{1}%') AND ('{1}' <> '0' OR division_id LIKE '%%' OR division_id IS NULL) "
                + " and enabled_flag = 'Y' "
                + " order by NAME "  ,
                " SELECT COUNT(1) from wo_mst_user "
                + " where 1=1 "
                + " and ( upper(NAME) like upper('%{0}%') or upper(NIK) like upper('%{0}%') or upper(division_name) like upper('%{0}%') ) "
                + " AND ('{1}' = '0' OR division_id LIKE '%{1}%') AND ('{1}' <> '0' OR division_id LIKE '%%' OR division_id IS NULL) "
                + " and enabled_flag = 'Y' ",
                Arrays.asList("NPK", "Name", "Division Name"),
                Arrays.asList("1", "2", "3"),false);
        return info;
	}
	
	@SuppressWarnings("static-access")
	public static SelectorInfo buildSelectorRegulation(FacesUtil facesUtil) {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		
		String columnName;
		if (locale != null && locale.equals(locale.ENGLISH)) {
			columnName = "nameEn";
		} else {
			columnName = "nameIn";
		}
		SelectorModel.SelectorInfo info = new SelectorModel.SelectorInfo(
				" from Regulation "
				+ " where 1 = {1} and (upper("+ columnName +") like upper('%{0}%') "
						+ " OR upper(documentNo) like upper('%{0}%')) "
						+ " and enabledFlag = 'Y' "
						+ " and jenisKetentuan.parameterDtlCode like '%{2}%' "
						+ " and status = 'DATA_ACTIVE' "						
						+ " order by nameIn ",
				"SELECT COUNT(1) from Regulation "
				+ "where 1 = {1} and (upper("+ columnName +") like upper('%{0}%') "
						+ " OR upper(documentNo) like upper('%{0}%')) "
						+ " and enabledFlag = 'Y' "
						+ " and jenisKetentuan.parameterDtlCode like '%{2}%' "
						+ " and status ='DATA_ACTIVE'  ",				
				Arrays.asList(
						"", 
						"", 
						""
						), 
				Arrays.asList("1",
						locale.ENGLISH.equals(locale)?"3":"2"
						),
				true);
		return info;

	}
	
	@SuppressWarnings("static-access")
	public static SelectorInfo buildSelectorCorrespondence(FacesUtil facesUtil) {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		
		String columnName;
		if (locale != null && locale.equals(locale.ENGLISH)) {
			columnName = "perihalEn";
		} else {
			columnName = "perihalIn";
		}
		SelectorModel.SelectorInfo info = new SelectorModel.SelectorInfo(
				" from TrcCorrespondence "
				+ " where 1 = {1} and (upper(to_char("+ columnName +")) like upper('%{0}%') "
						+ " OR upper(letterNo) like upper('%{0}%')) "
						+ " and enabledFlag = 'Y' "
						+ " and status = 'DATA_ACTIVE' "						
						+ " order by to_char(perihalIn) ",
				"SELECT COUNT(1) from TrcCorrespondence "
				+ "where 1 = {1} and (upper(to_char("+ columnName +")) like upper('%{0}%') "
						+ " OR upper(letterNo) like upper('%{0}%')) "
						+ " and enabledFlag = 'Y' "
						+ " and status ='DATA_ACTIVE'  ",				
				Arrays.asList(
						"", 
						"", 
						""
						), 
				Arrays.asList("1",
						locale.ENGLISH.equals(locale)?"3":"2"
						),
				true);
		return info;

	}
}
