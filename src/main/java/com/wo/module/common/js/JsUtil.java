package com.wo.module.common.js;

import org.primefaces.PrimeFaces;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsUtil {
	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(JsUtil.class);
	
	public static void reInitSelect2() {
		PrimeFaces.current().executeScript("reInitSelect2();");
	}
	
	public static void hideTHeadFollowupPoints() {
		PrimeFaces.current().executeScript("hideTHeadFollowup();");
	}
	
	public static void initSelect2() {
		PrimeFaces.current().executeScript("initSelect2();");
	}
}
