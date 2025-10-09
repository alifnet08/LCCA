package com.wo.module.qaCategory.constant;

import java.util.Arrays;

import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.lov.bean.SelectorModel;
import com.wo.module.lov.bean.SelectorModel.SelectorInfo;

public abstract class QACategoryConstants {

	public final static String NAVIGATE_EDIT = "qaCategoryEdit.faces";
	public final static String NAVIGATE_SEARCH = "qaCategory.faces";

	public final static String WHERE_CATEGORY = "CATEGORY";
	
	public static SelectorInfo buildSelectorPICAssigned(FacesUtil facesUtil) {
		SelectorModel.SelectorInfo info = new SelectorModel.SelectorInfo(
				" select user_id,nik,name,email from wo_mst_user " + " where 1=1 "
						+ " and ( upper(NAME) like upper('%{0}%') or upper(NIK) like upper('%{0}%') or upper(division_name) like upper('%{0}%') ) "
						// + " and case when ('{1}' = '0') then (division_id like '%%' or division_id is
						// null) else division_id like '%{1}%' end "
						+ " AND ('{1}' = '0' OR division_id LIKE '%{1}%') AND ('{1}' <> '0' OR (division_id LIKE '%%' OR division_id IS NULL))"
						+ " and enabled_flag = 'Y' " + " order by NAME ",
				" SELECT COUNT(1) from wo_mst_user " + " where 1=1 "
						+ " and ( upper(NAME) like upper('%{0}%') or upper(NIK) like upper('%{0}%') or upper(division_name) like upper('%{0}%') ) "
						// + " and case when ('{1}' = '0') then (division_id like '%%' or division_id is
						// null) else division_id like '%{1}%' end "
						+ " AND ('{1}' = '0' OR division_id LIKE '%{1}%') AND ('{1}' <> '0' OR (division_id LIKE '%%' OR division_id IS NULL))"
						+ " and enabled_flag = 'Y' ",
				Arrays.asList(facesUtil.retrieveMessage("formRegulationSocializationNIK"),
						facesUtil.retrieveMessage("formRegulationSocializationName"),
						facesUtil.retrieveMessage("formRegulationSocializationEmail")),
				Arrays.asList("1", "2", "3"), false);
		return info;

	}
}