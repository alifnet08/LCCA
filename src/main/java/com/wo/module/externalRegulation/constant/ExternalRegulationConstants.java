package com.wo.module.externalRegulation.constant;

import java.util.Arrays;
import java.util.Locale;

import javax.faces.context.FacesContext;

import com.wo.module.externalRegulation.service.ExternalRegulationService;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.lov.bean.SelectorModel;
import com.wo.module.lov.bean.SelectorModel.SelectorInfo;

public abstract class ExternalRegulationConstants {
	public final static String NAVIGATE_EDIT = "externalRegulationEdit.faces";
	public final static String NAVIGATE_SEARCH = "externalRegulation.faces";
	
	public final static String WHERE_REGULATION_ID = "REGULATION_ID";
	public final static String WHERE_JENIS_KETENTUAN = "JENIS_KETENTUAN";
	public final static String WHERE_DOC_TYPE = "DOC_TYPE";
	public final static String WHERE_CATEGORY = "CATEGORY";
	public final static String WHERE_TOPIC = "TOPIC";
	public final static String WHERE_DOC_NO = "DOC_NO";
	public final static String WHERE_NAME = "NAME";
	public final static String WHERE_PUBLISHED_DATE_START = "PUBLISHED_DATE_START";
	public final static String WHERE_PUBLISHED_DATE_END = "PUBLISHED_DATE_END";
	public final static String WHERE_REKAM_JEJAK = "REKAM_JEJAK";
	public final static String WHERE_STATUS = "STATUS";
	
	public final static String PARAMETER_DTL_CODE_KETENTUAN_EKSTERNAL = "KETENTUAN_EKSTERNAL";
	
	public final static String UPLOAD_TYPE_PERATURAN_ID = "UPLOAD_TYPE_PERATURAN_ID";
	public final static String UPLOAD_TYPE_PERATURAN_EN = "UPLOAD_TYPE_PERATURAN_EN";
	public final static String UPLOAD_TYPE_SUMMARY = "UPLOAD_TYPE_SUMMARY";
	public final static String UPLOAD_TYPE_PENJELASAN = "UPLOAD_TYPE_PENJELASAN";
	public final static String UPLOAD_TYPE_FAQ = "UPLOAD_TYPE_FAQ";
	public final static String UPLOAD_TYPE_LAMPIRAN = "UPLOAD_TYPE_LAMPIRAN";
	public final static String UPLOAD_TYPE_BULETIN = "UPLOAD_TYPE_BULETIN";
	public final static String UPLOAD_TYPE_MATERI = "UPLOAD_TYPE_MATERI";
	
	/*public static SelectorInfo buildSelectorJdlPeraturan() {
		SelectorModel.SelectorInfo info = new SelectorModel.SelectorInfo(
                "select REGULATION_ID,NAME_IN,NAME_EN " + 
		        "  from wo_tmp_regulation " + 
                " where upper(NAME_IN) like upper('%{0}%') and enabled_flag = 'Y' " +
		        "       and status ='DATA_ACTIVE'  " + 
                "       and jenis_ketentuan = 'KETENTUAN_EKSTERNAL' " +
                " order by NAME_IN ",
                "SELECT COUNT(1) " +
                "  from wo_tmp_regulation " + 
                " where upper(NAME_IN) like upper('%{0}%') " +
                "       and enabled_flag = 'Y' and status ='DATA_ACTIVE' " + 
                "       and jenis_ketentuan = 'KETENTUAN_EKSTERNAL' ",
                Arrays.asList("Regulation Name"),
                Arrays.asList("1"),true);
        return info;

	}*/
	
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
				" from Regulation where  (upper("+ columnName +") like upper('%{0}%') OR upper(documentNo) like upper('%{0}%')) and enabledFlag = 'Y' and status ='DATA_ACTIVE' and jenisKetentuan.parameterDtlCode = 'KETENTUAN_EKSTERNAL'  order by nameIn ",
				"SELECT COUNT(1) from Regulation where  (upper("+ columnName +") like upper('%{0}%')  OR upper(documentNo) like upper('%{0}%')) and enabledFlag = 'Y' and status ='DATA_ACTIVE' and jenisKetentuan.parameterDtlCode = 'KETENTUAN_EKSTERNAL' ",
				
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
	
	@SuppressWarnings("static-access")
	public static SelectorInfo buildSelectorRekamJejak(FacesUtil facesUtil) {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		String localeName ="IN";
		if (locale != null && locale.equals(locale.ENGLISH)) {
			localeName = "EN";
		} 
		SelectorModel.SelectorInfo info = new SelectorModel.SelectorInfo(
				"call wo_sp_get_track_record {1}",
				//externalRegulationService.getQueryTrackRecord('{1}', localeName),
				"select 5 from dual",
                Arrays.asList(facesUtil.retrieveMessage("formExternalRegulationDocNo"),facesUtil.retrieveMessage("formExternalRegulationMencabut"),facesUtil.retrieveMessage("formExternalRegulationDicabut"),facesUtil.retrieveMessage("formExternalRegulationMengubah"),facesUtil.retrieveMessage("formExternalRegulationDiubah")),
                Arrays.asList("0","1","2","3","4"),false);
        return info;

	}
	
}
