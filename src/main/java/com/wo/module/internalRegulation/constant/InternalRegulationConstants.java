package com.wo.module.internalRegulation.constant;

import java.util.Arrays;
import java.util.Locale;

import javax.faces.context.FacesContext;

import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.lov.bean.SelectorModel;
import com.wo.module.lov.bean.SelectorModel.SelectorInfo;

public abstract class InternalRegulationConstants {
	
	public final static String NAVIGATE_EDIT = "internalRegulationEdit.faces";
	public final static String NAVIGATE_SEARCH = "internalRegulation.faces";
	
	public final static String WHERE_REGULATION_ID = "REGULATION_ID";
	public final static String WHERE_JENIS_KETENTUAN = "JENIS_KETENTUAN";
	public final static String WHERE_DOC_TYPE = "DOC_TYPE";
	public final static String WHERE_CATEGORY = "CATEGORY";
	public final static String WHERE_TOPIC = "TOPIC";
	public final static String WHERE_PUBLISHER_UNIT = "PUBLISHER_UNIT";
	public final static String WHERE_DOC_NO = "DOC_NO";
	public final static String WHERE_NAME = "NAME";
	public final static String WHERE_PUBLISHED_DATE_START = "PUBLISHED_DATE_START";
	public final static String WHERE_PUBLISHED_DATE_END = "PUBLISHED_DATE_END";
	public final static String WHERE_EXPIRED_DATE_START = "EXPIRED_DATE_START";
	public final static String WHERE_EXPIRED_DATE_END = "EXPIRED_DATE_END";
	public final static String WHERE_REKAM_JEJAK = "REKAM_JEJAK";
	public final static String WHERE_STATUS = "STATUS";
	public final static String WHERE_DOC_TYPE_IS_NOT_ANOUNCEMENT = "IS_NOT_ANOUNCEMENT";
	public final static String WHERE_YEAR = "YEAR";
	public final static String WHERE_DIRECTORATE = "DIRECTORATE";
	
	public final static String JENIS_KETENTUAN_INTERNAL = "JENIS_KETENTUAN_INTERNAL";
	
	public final static String PARAM_DTL_CODE_KETENTUAN_INTERNAL = "KETENTUAN_INTERNAL";
	
	public final static String UPLOAD_TYPE_PERATURAN_ID = "UPLOAD_TYPE_PERATURAN_ID";
	public final static String UPLOAD_TYPE_PERATURAN_EN = "UPLOAD_TYPE_PERATURAN_EN";
	public final static String UPLOAD_TYPE_FAQ = "UPLOAD_TYPE_FAQ";
	public final static String UPLOAD_TYPE_LAMPIRAN = "UPLOAD_TYPE_LAMPIRAN";
	public final static String UPLOAD_TYPE_OTHERS = "UPLOAD_TYPE_OTHERS";
	public final static String UPLOAD_TYPE_FROM_RINGKASAN = "UPLOAD_TYPE_FROM_RINGKASAN";
	
	public final static String DOCUMENT_TYPE_INFORMASI_LAINNYA = "PEMBERITAHUAN";
	
	
	/*public static SelectorInfo buildSelectorJdlPeraturan() {
		SelectorModel.SelectorInfo info = new SelectorModel.SelectorInfo(
                " select REGULATION_ID,NAME_IN, NAME_EN " +
				"   from wo_tmp_regulation " +
				"  where upper(NAME_IN) like upper('%{0}%') " +
				"        and enabled_flag = 'Y' " +
				"        and status ='DATA_ACTIVE' " +
				"        and jenis_ketentuan = 'KETENTUAN_INTERNAL' " +
				"  order by NAME_IN ",
                " SELECT COUNT(1) " +
                "   from wo_tmp_regulation " +
                "  where upper(NAME_IN) like upper('%{0}%') " +
                "        and enabled_flag = 'Y' " +
                "        and jenis_ketentuan = 'KETENTUAN_INTERNAL' " +
                "        and status ='DATA_ACTIVE' ",
                Arrays.asList("Regulation Name"),
                Arrays.asList("1"),false);
        return info;

	}*/
	
	@SuppressWarnings("static-access")
	public static SelectorInfo buildSelectorJdlPeraturan(FacesUtil facesUtil) {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();

		String columnName;
		if (locale != null && locale.equals(locale.ENGLISH)) {
			columnName = "nameEn";
		} else {
			columnName = "nameIn";
		}
		SelectorModel.SelectorInfo info = new SelectorModel.SelectorInfo(
				" from Regulation where  (upper("+ columnName +") like upper('%{0}%') OR upper(documentNo) like upper('%{0}%')) and enabledFlag = 'Y' and status ='DATA_ACTIVE' and jenisKetentuan.parameterDtlCode = 'KETENTUAN_INTERNAL'  order by nameIn ",
				"SELECT COUNT(1) from Regulation where  (upper("+ columnName +") like upper('%{0}%') OR upper(documentNo) like upper('%{0}%')) and enabledFlag = 'Y' and status ='DATA_ACTIVE' and jenisKetentuan.parameterDtlCode = 'KETENTUAN_INTERNAL' ",
				
				Arrays.asList(
						facesUtil.retrieveMessage("textNumber"), 
						facesUtil.retrieveMessage("formRegulationSocializationRegTitle"), 
						facesUtil.retrieveMessage("formRegulationSocializationAttachment")
						), 
				Arrays.asList("1",
						locale.ENGLISH.equals(locale)?"3":"2"
						),
				true);
		return info;

	}
	
	@SuppressWarnings({ "static-access", "unused" })
	public static SelectorInfo buildSelectorRekamJejak(FacesUtil facesUtil) {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		String localeName ="IN";
		if (locale != null && locale.equals(locale.ENGLISH)) {
			localeName = "EN";
		} 
		SelectorModel.SelectorInfo info = new SelectorModel.SelectorInfo(
				"call wo_sp_get_track_record {1}",
				//"select 'DOCUMENT_NO', 'MENCABUT', 'DICABUT', 'MENGUBAH','DIUBAH' from DUAL",
				"select 5  from dual",
                Arrays.asList(facesUtil.retrieveMessage("formExternalRegulationDocNo"), 
                		      facesUtil.retrieveMessage("formExternalRegulationMencabut"), 
                		      facesUtil.retrieveMessage("formExternalRegulationDicabut"), 
                		      facesUtil.retrieveMessage("formExternalRegulationMengubah"), 
                		      facesUtil.retrieveMessage("formExternalRegulationDiubah")),
                Arrays.asList("0","1","2","3","4"),false);
        return info;

	}
	
	public static SelectorInfo buildSelectorUser() {
		SelectorModel.SelectorInfo info = new SelectorModel.SelectorInfo(
                " select USER_ID, NIK, NAME, division_name from wo_mst_user "
                + " where 1=1 "
                + " and ( upper(NAME) like upper('%{0}%') or upper(NIK) like upper('%{0}%') or upper(division_name) like upper('%{0}%') ) "
               // + " AND ('{1}' = '0' OR division_id LIKE '%{1}%') AND ('{1}' <> '0' OR division_id LIKE '%%' OR division_id IS NULL) "
                + " and enabled_flag = 'Y' "
                + " order by NAME "  ,
                " SELECT COUNT(1) from wo_mst_user "
                + " where 1=1 "
                + " and ( upper(NAME) like upper('%{0}%') or upper(NIK) like upper('%{0}%') or upper(division_name) like upper('%{0}%') ) "
                //+ " AND ('{1}' = '0' OR division_id LIKE '%{1}%') AND ('{1}' <> '0' OR division_id LIKE '%%' OR division_id IS NULL) "
                + " and enabled_flag = 'Y' ",
                Arrays.asList("NPK", "Name", "Division Name"),
                Arrays.asList("1", "2", "3"),false);
        return info;
	}
	
	public static SelectorInfo buildSelectorProposerUnit() {
		SelectorModel.SelectorInfo info = new SelectorModel.SelectorInfo(
                " select distinct division_name, directorate_name from wo_mst_user "
                + " where 1=1 "
                + " and division_name is not null "
                + " and enabled_flag = 'Y' "
                + " and (upper(division_name) like upper('%{0}%')) "
                + " and (upper(directorate_name) = upper('{1}')) "
                + " order by division_name "  ,
                " SELECT COUNT(1) "
                + " FROM (select distinct division_name, directorate_name "
                + "         from wo_mst_user "
        		+ "        where 1=1 "
                + "              and division_name is not null "
                + " 		     and enabled_flag = 'Y' "
                + " 			 and (upper(division_name) like upper('%{0}%')) "
                + " 			 and (upper(directorate_name) = upper('{1}')))"
                + " WHERE 1=1 ",
                Arrays.asList("Division Name", "Directorate Name"),
                Arrays.asList("0", "1"),false);
        return info;
	}
	
}
