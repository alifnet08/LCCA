package com.wo.module.login.constant;

public interface LoginConstants {
	
	final static String ERROR_LDAP_USER_NOT_FOUND = "AcceptSecurityContext error, data 525";
	
	final static String ERROR_LDAP_INVALID_CREDENTIALS = "AcceptSecurityContext error, data 52e";
	
	final static String ERROR_LDAP_NOT_PERMITTED_TO_LOGON_THIS_TIME = "AcceptSecurityContext error, data 530";
	
	final static String ERROR_LDAP_NOT_PERMITTED_TO_LOGON_THIS_WORKSTATION = "AcceptSecurityContext error, data 531";
	
	final static String ERROR_LDAP_PASSWORD_EXPIRED = "AcceptSecurityContext error, data 532";
	
	final static String ERROR_LDAP_ACCOUNT_DISABLED = "AcceptSecurityContext error, data 533";
	
	final static String ERROR_LDAP_ACCOUNT_EXPIRED = "AcceptSecurityContext error, data 701";
	
	final static String ERROR_LDAP_MUST_RESET_PASSWORD = "AcceptSecurityContext error, data 773";
	
	final static String ERROR_LDAP_ACCOUNTS_LOCKED = "AcceptSecurityContext error, data 775";
	
	final static String SESSION_KEY_USER_ID = "SESSION_KEY_USER_ID";

	final static String SESSION_KEY_USER_KEY = "SESSION_KEY_USER_KEY";
	
	
	final static String COMPLETE_HOME_URL_REDIRECT = "/faces/trc/worklist/worklist?faces-redirect=true";
	
	final static String COMPLETE_HOME_URL = "/faces/trc/worklist/worklist.jsf";
	
	final static String COMPLETE_LOGIN_REDIRECT = "/faces/portal/loginP.jsf";
	
	final static String COMPLETE_LOGIN = "/faces/portal/loginP.jsf";
	
	final static String COMPLETE_INVALID_ACCESS_REDIRECT = "/faces/errors/401?faces-redirect=true";
	
	final static String COMPLETE_INVALID_ACCESS = "/faces/errors/401.jsf";
	
	final static String COMPLETE_LOGIN_REDIRECT_EXPIRED_EXCEPTION = "/faces/portal/loginP?faces-redirect=true&facesExpired=true";
	
	
	final static String HOME_URL_DISPLAY = "HOME";
	
	final static String LOGOUT_URL_DISPLAY = "LOGOUT";
	
	final static String COMPLETE_LOGOUT_URL = "/faces/login/logout.jsf";
	
	final static String LOGIN_HOME_ADMIN_URL = "/faces/trc/worklist/worklist?faces-redirect=true";
	
	
}
