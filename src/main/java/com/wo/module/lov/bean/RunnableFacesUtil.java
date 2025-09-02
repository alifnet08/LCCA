package com.wo.module.lov.bean;

import java.util.Locale;
import java.util.ResourceBundle;

import org.apache.commons.lang3.StringUtils;

public class RunnableFacesUtil extends FacesUtil {
	private static final long serialVersionUID = -6910280254803835122L;
	private Locale defaultLocalizationBean;
	public static String BUNDLE_PROPERTIES_LOCATION = "com.wo.resources.ApplicationResources";
	
	public RunnableFacesUtil(Locale defaultLocalizationBean) {
		this.defaultLocalizationBean = defaultLocalizationBean;
	}
	
	public String retrieveMessage(final String key) {
		return ResourceBundle.getBundle(BUNDLE_PROPERTIES_LOCATION,
				this.defaultLocalizationBean).getString(key);
	}
	
	public String retrieveLocaleMessage(String enValue, String inValue) {
		if (defaultLocalizationBean != null && defaultLocalizationBean.equals(Locale.ENGLISH)) {
			return StringUtils.isBlank(enValue)? StringUtils.EMPTY : enValue;
		}
		
		return StringUtils.isBlank(inValue)? StringUtils.EMPTY : inValue;
	}
	
	public Locale getDefaultLocalizationBean() {
		return defaultLocalizationBean;
	}
	public void setDefaultLocalizationBean(Locale defaultLocalizationBean) {
		this.defaultLocalizationBean = defaultLocalizationBean;
	}

}
