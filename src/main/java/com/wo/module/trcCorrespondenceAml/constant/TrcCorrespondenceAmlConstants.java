package com.wo.module.trcCorrespondenceAml.constant;

import java.util.Arrays;

import com.wo.module.lov.bean.SelectorModel;
import com.wo.module.lov.bean.SelectorModel.SelectorInfo;

public interface TrcCorrespondenceAmlConstants {

	public final static String NAVIGATE_EDIT = "trcCorrespondenceAmlEdit.faces";
	public final static String NAVIGATE_SEARCH = "trcCorrespondenceAml.faces";
	
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
	
	public final static String SEARCH_BY_USER_LOGIN = "SEARCH_BY_USER_LOGIN";
	
	public final static String UPLOAD_TYPE_DOCUMENT = "UPLOAD_TYPE_DOCUMENT";
	public final static String UPLOAD_TYPE_EVIDENCE = "UPLOAD_TYPE_EVIDENCE";
	
	public final static String PARAM_DETAIL_CORRESPONDEN_TYPE_INVITATION = "INVITATION";
	public final static String PARAM_DETAIL_ATTENDEE_I_ATTEND = "I_ATTEND";
	public final static String PARAM_DETAIL_ATTENDEE_NOT_ATTEND = "NOT_ATTEND";
	
	public static SelectorInfo buildSelectorUserAttendace() {
		SelectorModel.SelectorInfo info = new SelectorModel.SelectorInfo(
                " select USER_ID, NIK, NAME, division_name from wo_mst_user "
                + " where 1=1 "
                + " and ( upper(NAME) like upper('%{0}%') or upper(NIK) like upper('%{0}%') or upper(division_name) like upper('%{0}%') ) "
                + " and enabled_flag = 'Y' "
                + " order by NAME "  ,
                " SELECT COUNT(1) from wo_mst_user "
                + " where 1=1 "
                + " and ( upper(NAME) like upper('%{0}%') or upper(NIK) like upper('%{0}%') or upper(division_name) like upper('%{0}%') ) "
                + " and enabled_flag = 'Y' ",
                Arrays.asList("NPK", "Name", "Division Name"),
                Arrays.asList("1", "2", "3"),false);
        return info;
	}
	
}
