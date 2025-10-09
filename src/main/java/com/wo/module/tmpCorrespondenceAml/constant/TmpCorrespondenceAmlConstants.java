package com.wo.module.tmpCorrespondenceAml.constant;

import java.util.Arrays;

import com.wo.module.lov.bean.SelectorModel;
import com.wo.module.lov.bean.SelectorModel.SelectorInfo;

public interface TmpCorrespondenceAmlConstants {
	public final static String NAVIGATE_EDIT = "tmpCorrespondenceAmlEdit.faces";
	public final static String NAVIGATE_SEARCH = "tmpCorrespondenceAml.faces";	
	
	public final static String SEARCH_PENGIRIM = "SEARCH_PENGIRIM";
	public final static String SEARCH_NO_SURAT = "SEARCH_NO_SURAT";
	public final static String SEARCH_TANGGAL_TERIMA_SURAT_FROM = "SEARCH_TANGGAL_TERIMA_SURAT_FROM";
	public final static String SEARCH_TANGGAL_TERIMA_SURAT_TO = "SEARCH_TANGGAL_TERIMA_SURAT_TO";
	public final static String SEARCH_TANGGAL_SURAT_FROM = "SEARCH_TANGGAL_SURAT_FROM";
	public final static String SEARCH_TANGGAL_SURAT_TO = "SEARCH_TANGGAL_SURAT_TO";
	public final static String SEARCH_PERIHAL = "SEARCH_PERIHAL";
	public final static String SEARCH_TARGET_DATE_FROM = "SEARCH_TARGET_DATE_FROM";
	public final static String SEARCH_TARGET_DATE_TO = "SEARCH_TARGET_DATE_TO";
	public final static String SEARCH_STATUS = "SEARCH_STATUS";
	
	public final static String PARAM_DTL_CODE_NONINVITATION = "NONINVITATION";
	
	public static SelectorInfo buildSelectorUser() {
		SelectorModel.SelectorInfo info = new SelectorModel.SelectorInfo(
                " select USER_ID, NIK, NAME, division_name from wo_mst_user "
                + " where 1=1 "
                + " and ( upper(NAME) like upper('%{0}%') or upper(NIK) like upper('%{0}%') or upper(division_name) like upper('%{0}%') ) "
                //+ " and case when ('{1}' = '0') then (division_id like '%%' or division_id is null) else division_id like '%{1}%' end "
                + " AND ('{1}' = '0' OR division_id LIKE '%{1}%') AND ('{1}' <> '0' OR division_id LIKE '%%' OR division_id IS NULL) "
                + " and enabled_flag = 'Y' "
                + " order by NAME "  ,
                " SELECT COUNT(1) from wo_mst_user "
                + " where 1=1 "
                + " and ( upper(NAME) like upper('%{0}%') or upper(NIK) like upper('%{0}%') or upper(division_name) like upper('%{0}%') ) "
                //+ " and case when ('{1}' = '0') then (division_id like '%%' or division_id is null) else division_id like '%{1}%' end "
                + " AND ('{1}' = '0' OR division_id LIKE '%{1}%') AND ('{1}' <> '0' OR division_id LIKE '%%' OR division_id IS NULL) "
                + " and enabled_flag = 'Y' ",
                Arrays.asList("NPK", "Name", "Division Name"),
                Arrays.asList("1", "2", "3"),false);
        return info;
	}
	
	public static SelectorInfo buildSelectorUserCompliance() {
		SelectorModel.SelectorInfo info = new SelectorModel.SelectorInfo(
                " select USER_ID, NIK, NAME, division_name from wo_mst_user "
                + " where 1=1 "
                + " and division_name = (select name_in from wo_mst_parameter_dtl where parameter_dtl_code = 'AML_DIVISION') "
                + " and ( upper(NAME) like upper('%{0}%') or upper(NIK) like upper('%{0}%') or upper(division_name) like upper('%{0}%') ) "
                + " and enabled_flag = 'Y' "
                + " order by NAME "  ,
                " SELECT COUNT(1) from wo_mst_user "
                + " where 1=1 "
                + " and division_name = (select name_in from wo_mst_parameter_dtl where parameter_dtl_code = 'AML_DIVISION') "
                + " and ( upper(NAME) like upper('%{0}%') or upper(NIK) like upper('%{0}%') or upper(division_name) like upper('%{0}%') ) "
                + " and enabled_flag = 'Y' ",
                Arrays.asList("NPK", "Name", "Division Name"),
                Arrays.asList("1", "2", "3"),false);
        return info;
	}
}
