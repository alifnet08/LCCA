package com.wo.module.externalRegulationView.constant;

import java.util.Arrays;
import java.util.Locale;

import javax.faces.context.FacesContext;

import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.lov.bean.SelectorModel;
import com.wo.module.lov.bean.SelectorModel.SelectorInfo;

public abstract class ExternalRegulationViewConstants {
	
	public final static String NAVIGATE_VIEW = "externalRegulationViewDetail.faces";
	public final static String NAVIGATE_SEARCH = "externalRegulationView.faces";
	
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
	
	public static SelectorInfo buildSelectorJdlPeraturan() {
		SelectorModel.SelectorInfo info = new SelectorModel.SelectorInfo(
                "select REGULATION_ID,NAME_IN,NAME_EN from wo_mst_regulation where  upper(NAME_IN) like upper('%{0}%') and enabled_flag = 'Y' and status ='DATA_ACTIVE'  order by NAME_IN ",
                "SELECT COUNT(1) from wo_mst_regulation where  upper(NAME_IN) like upper('%{0}%') and enabled_flag = 'Y' and status ='DATA_ACTIVE' ",
                Arrays.asList("Regulation Name"),
                Arrays.asList("1"),false);
        return info;

	}
	
	@SuppressWarnings("static-access")
	public static SelectorInfo buildSelectorRekamJejak(FacesUtil facesUtil) {
		/*Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		String localeName = "IN";
		if (locale != null && locale.equals(locale.ENGLISH)) {
			localeName = "EN";
		}*/
		SelectorModel.SelectorInfo info = new SelectorModel.SelectorInfo(
				"call wo_sp_get_track_record_mst {1}", 
				//"select 'DOCUMENT_NO', 'MENCABUT', 'DICABUT', 'MENGUBAH','DIUBAH' from DUAL",
				" select 5 from dual",
				Arrays.asList(facesUtil.retrieveMessage("formExternalRegulationDocNo"),
						facesUtil.retrieveMessage("formExternalRegulationMencabut"),
						facesUtil.retrieveMessage("formExternalRegulationDicabut"),
						facesUtil.retrieveMessage("formExternalRegulationMengubah"),
						facesUtil.retrieveMessage("formExternalRegulationDiubah")),
				Arrays.asList("0", "1", "2", "3", "4"), false);
		return info;

	}
	
}
