package com.wo.module.opinionFE.constant;

public abstract class OpinionFEConstants {
	public final static String NAVIGATE_VIEW_OLD = "opinionFEView.faces";
	
	public final static String NAVIGATE_VIEW = "/pages/opinionFE/opinionFEView?"
			+ "token=#{opinionFEBean.toEncrypt(vo.articleId)}";
	
	public final static String SIGN_MINUS = "-";
	public final static String SIGN_PLUS = "+";
	
	public final static String SEARCH_BY_ARTICLE_TYPE = "SEARCH_BY_ARTICLE_TYPE";
}
