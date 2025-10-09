package com.wo.module.regulationMonitoring.constant;

import java.util.Arrays;
import java.util.Locale;

import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.lov.bean.SelectorModel;
import com.wo.module.lov.bean.SelectorModel.SelectorInfo;

public abstract class RegMonitoringConstants {
	public final static String NAVIGATE_EDIT = "regulationMonitoringEdit.faces";
	public final static String NAVIGATE_SEARCH = "regulationMonitoring.faces";

	public final static String WHERE_REGULATION_ID = "REGULATION_ID";
	public final static String WHERE_PROV_TYPE = "PROV_TYPE";
	public final static String WHERE_DOC_TYPE = "DOC_TYPE";
	public final static String WHERE_CATEGORY = "CATEGORY";
	public final static String WHERE_TOPIC = "TOPIC";
	public final static String WHERE_DOC_NO = "DOC_NO";
	public final static String WHERE_NAME = "NAME";
	public final static String WHERE_PUBLISHED_DATE_START = "PUBLISHED_DATE_START";
	public final static String WHERE_PUBLISHED_DATE_END = "PUBLISHED_DATE_END";
	public final static String WHERE_EFF_DATE_START = "EFF_DATE_START";
	public final static String WHERE_EFF_DATE_END = "EFF_DATE_END";
	public final static String WHERE_TARGET_DATE_START = "TARGET_DATE_START";
	public final static String WHERE_TARGET_DATE_END = "TARGET_DATE_END";
	public final static String WHERE_STATUS = "STATUS";
	
	public final static String RESPONSIBILITY_SUPER_ADMIN_ID = "RESPONSIBLITY_SUPER_ADMIN_ID";

//	public static SelectorInfo buildSelectorJdlPeraturan(FacesUtil facesUtil) {
//		SelectorModel.SelectorInfo info = new SelectorModel.SelectorInfo(
//				"select REGULATION_ID,NAME_IN,NAME_EN from wo_tmp_regulation where  upper(NAME_IN) like upper('%{0}%') and enabled_flag = 'Y' and status ='DATA_ACTIVE' and jenis_ketentuan = '{1}'  order by NAME_IN ",
//				"SELECT COUNT(1) from wo_tmp_regulation where  upper(NAME_IN) like upper('%{0}%') and enabled_flag = 'Y' and status ='DATA_ACTIVE' and jenis_ketentuan = '{1}' ",
//				Arrays.asList(facesUtil.retrieveMessage("formRegulationMonitoringRegTitle")), Arrays.asList("1"),
//				false);
//		return info;
//
//	}
	
	@SuppressWarnings("static-access")
	public static SelectorInfo buildSelectorJdlPeraturan(FacesUtil facesUtil) {
		Locale locale = facesUtil.retrieveDefaultLocale();
		String columnName;
		if (locale != null && locale.equals(locale.ENGLISH)) {
			columnName = "nameEn";
		} else {
			columnName = "nameIn";
		}
		SelectorModel.SelectorInfo info = new SelectorModel.SelectorInfo(
				" from Regulation where  (upper("+ columnName +") like upper('%{0}%') OR upper(nameEn) like upper('%{0}%') OR upper(documentNo) like upper('%{0}%')) and enabledFlag = 'Y' and status ='DATA_ACTIVE' and jenisKetentuan.parameterDtlCode = '{1}'  order by nameIn ",
				"SELECT COUNT(1) from Regulation where  (upper("+ columnName +") like upper('%{0}%') OR upper(nameEn) like upper('%{0}%') OR upper(documentNo) like upper('%{0}%')) and enabledFlag = 'Y' and status ='DATA_ACTIVE' and jenisKetentuan.parameterDtlCode = '{1}' ",
				
				Arrays.asList(
						facesUtil.retrieveMessage("textNumber"), 
						facesUtil.retrieveMessage("formRegulationMonitoringRegTitle"), 
						facesUtil.retrieveMessage("formRegulationMonitoringAttachment")
						), 
				Arrays.asList("1",
						locale.ENGLISH.equals(locale)?"3":"2"
						),
				true);
		return info;

	}

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
				Arrays.asList(facesUtil.retrieveMessage("formRegulationMonitoringNIK"),
						facesUtil.retrieveMessage("formRegulationMonitoringName"),
						facesUtil.retrieveMessage("formRegulationMonitoringEmail")),
				Arrays.asList("1", "2", "3"), false);
		return info;

	}

	public static SelectorInfo buildSelectorDivision(FacesUtil facesUtil) {
		SelectorModel.SelectorInfo info = new SelectorModel.SelectorInfo(
				"select division_id, division_name from wo_mst_user where upper(division_name) like upper('%{0}%') and enabled_flag = 'Y' order by nik ",
				"SELECT COUNT(1) from wo_mst_user where upper(division_name) like upper('%{0}%') and enabled_flag = 'Y' ",
				Arrays.asList(facesUtil.retrieveMessage("formRegulationMonitoringDivision")), Arrays.asList("1"),
				false);
		return info;

	}

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
