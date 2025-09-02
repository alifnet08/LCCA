package com.wo.module.socializationFE.constant;

public class SocializationFEConstant {

	public static final String WHERE_SEARCH_STATUS = "WHERE_SEARCH_STATUS";
	public static final String WHERE_SEARCH_REGULATION_NAME = "WHERE_SEARCH_REGULATION_NAME";
	public static final String WHERE_SEARCH_USER_LOGIN = "WHERE_SEARCH_USER_LOGIN";
	
	public static final String UPLOAD_TYPE_DOCUMENT = "UPLOAD_TYPE_DOCUMENT";
	
	public static final String NAVIGATE_SOCIALIZATION_FE = "socializationFE.faces";
	public static final String NAVIGATE_SOCIALIZATION_FE_EDIT = "socializationFEEdit.faces";
	public static final String NAVIGATE_SOCIALIZATION_FE_VIEW = "socializationFEView.faces";
	
	public static final String NAVIGATE_SOCIALIZATION_FE_EDIT_UNUSED = "/pages/socializationFE/socializationFEEdit?"
			+ "token=#{socializationFEBean.toEncrypt(vo.trcSocializationFollowupId)}";
	public static final String NAVIGATE_SOCIALIZATION_FE_VIEW_UNUSED = "/pages/socializationFE/socializationFEView?"
			+ "token=#{socializationFEBean.toEncrypt(vo.trcSocializationFollowupId)}";
}
