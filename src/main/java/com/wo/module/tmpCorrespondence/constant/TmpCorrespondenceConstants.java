package com.wo.module.tmpCorrespondence.constant;

import java.util.Arrays;

import com.wo.module.lov.bean.SelectorModel;
import com.wo.module.lov.bean.SelectorModel.SelectorInfo;

public interface TmpCorrespondenceConstants {
	public final static String NAVIGATE_EDIT = "tmpCorrespondenceEdit.faces";
	public final static String NAVIGATE_SEARCH = "tmpCorrespondence.faces";	
	
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
	public final static String SEARCH_DIVISION = "SEARCH_DIVISION";
	
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
                + " AND ('{1}' = '0' OR division_id LIKE '%{1}%') AND ('{1}' <> '0' OR division_id LIKE '%%' OR division_id IS NULL) "
                + " and enabled_flag = 'Y' ",
                Arrays.asList("NPK", "Name", "Division Name"),
                Arrays.asList("1", "2", "3"),false);
        return info;
	}
	
	public static SelectorInfo buildSelectorUserCompliance(String divisionName) {
		SelectorModel.SelectorInfo info = new SelectorModel.SelectorInfo(
                " select USER_ID, NIK, NAME, division_name from wo_mst_user "
                + " where 1=1 "
                + " and UPPER(division_name) = upper('"+divisionName+"') "
                + " and ( upper(NAME) like upper('%{0}%') or upper(NIK) like upper('%{0}%') ) "
                + " and enabled_flag = 'Y' "
                + " order by NAME "  ,
                " SELECT COUNT(1) from wo_mst_user "
                + " where 1=1 "
                + " and UPPER(division_name) = upper('"+divisionName+"') "
                + " and ( upper(NAME) like upper('%{0}%') or upper(NIK) like upper('%{0}%')  ) "
                + " and enabled_flag = 'Y' ",
                Arrays.asList("NPK", "Name", "Division Name"),
                Arrays.asList("1", "2", "3"),false);
        return info;
	}
	
	public static SelectorInfo buildSelectorReferensiSurat() {
		SelectorModel.SelectorInfo info = new SelectorModel.SelectorInfo(
                " select CORRESPONDENCE_ID,  DBMS_LOB.SUBSTR(PERIHAL_IN, 4000, 1) PERIHAL, LETTER_NO, ''LAMPIRAN "
              + "   FROM WO_TMP_CORRESPONDENCE "
              + "  WHERE 1=1 "
              + "        AND ( upper(DBMS_LOB.SUBSTR(PERIHAL_IN, 4000, 1)) like upper('%{0}%') or upper(LETTER_NO) like upper('%{0}%') ) "
              + "        AND enabled_flag = 'Y' "
              + "  order by CORRESPONDENCE_ID "  ,
              "   SELECT COUNT(1) "
              + "   FROM WO_TMP_CORRESPONDENCE "
              + "  WHERE 1=1 "
              + "        AND ( upper(DBMS_LOB.SUBSTR(PERIHAL_IN, 4000, 1)) like upper('%{0}%') or upper(LETTER_NO) like upper('%{0}%') ) "
              + "        AND enabled_flag = 'Y' ",
            Arrays.asList("Perihal Surat", "Nomor Surat", "Lampiran"),
            Arrays.asList("1", "2", "3"),false);
				
        return info;
	}
	
}
