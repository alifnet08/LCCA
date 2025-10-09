package com.wo.module.correspondenceFE.constant;

public abstract class CorrespondenceFEConstants {
	
	public final static String NAVIGATE_EDIT = "correspondenceFEEdit.faces";
	
	public final static String NAVIGATE_VERIFY = "correspondenceFEVerification.faces";
	
	public final static String NAVIGATE_VIEW = "correspondenceFEView.faces";
	
	public final static String NAVIGATE_EDIT_UNUSED = "/pages/correspondenceFE/correspondenceFEEdit?"
			+ "token=#{correspondenceFEBean.toEncrypt(vo.correspondenceId)}";
	
	public final static String NAVIGATE_VERIFY_UNUSED = "/pages/correspondenceFE/correspondenceFEVerification?"
			+ "token=#{correspondenceFEBean.toEncrypt(vo.correspondenceId)}";
	
	public final static String NAVIGATE_VIEW_UNUSED = "/pages/correspondenceFE/correspondenceFEView?"
			+ "token=#{correspondenceFEBean.toEncrypt(vo.correspondenceId)}";
	
	public final static String SEARCH_BY_PERIHAL = "SEARCH_BY_PERIHAL";
	
	public final static String SEARCH_BY_STATUS = "SEARCH_BY_STATUS";
	
	public final static String SEARCH_BY_USER_LOGIN = "SEARCH_BY_USER_LOGIN";
	
}
