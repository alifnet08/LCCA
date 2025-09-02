package com.wo.module.common.util;

import java.util.Hashtable;
import javax.naming.directory.*;
import javax.naming.ldap.Control;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import javax.naming.*;

import org.apache.log4j.Logger;

import com.wo.module.common.constant.CommonConstants;
import com.wo.module.login.constant.LoginConstants;
import com.wo.module.lov.bean.FacesUtil;



@SuppressWarnings("unused")
public class ADConnectionUtil {
	static Logger logger = Logger.getLogger(ADConnectionUtil.class);
	private static ADConnectionUtil instance = null;
	private String ldapURL;
	private String ldapAuthentication;
	private String ldapDomain;
	private FacesUtil facesUtil;

	private String getErrorLdap(String errStr) {
		String result = "Error Login";
		try {
			if (errStr.indexOf(LoginConstants.ERROR_LDAP_USER_NOT_FOUND) >= 0) {
				result = facesUtil.retrieveMessage("errorLogin");
			} else if (errStr
					.indexOf(LoginConstants.ERROR_LDAP_INVALID_CREDENTIALS) >= 0) {
				result = facesUtil
						.retrieveMessage("errorInvalidCredentials");
			} else if (errStr
					.indexOf(LoginConstants.ERROR_LDAP_NOT_PERMITTED_TO_LOGON_THIS_TIME) >= 0) {
				result = facesUtil
						.retrieveMessage("errorLoginNotAllowedThisTime");
			} else if (errStr
					.indexOf(LoginConstants.ERROR_LDAP_NOT_PERMITTED_TO_LOGON_THIS_WORKSTATION) >= 0) {
				result = facesUtil
						.retrieveMessage("errorLoginNotAllowedThisWorkstation");
			} else if (errStr
					.indexOf(LoginConstants.ERROR_LDAP_PASSWORD_EXPIRED) >= 0) {
				result = facesUtil
						.retrieveMessage("errorPasswordExpired");
			} else if (errStr
					.indexOf(LoginConstants.ERROR_LDAP_ACCOUNT_DISABLED) >= 0) {
				result = facesUtil
						.retrieveMessage("errorDisabledLogin");
			} else if (errStr
					.indexOf(LoginConstants.ERROR_LDAP_ACCOUNT_EXPIRED) >= 0) {
				result = facesUtil
						.retrieveMessage("errorNonActiveLogin");
			} else if (errStr
					.indexOf(LoginConstants.ERROR_LDAP_MUST_RESET_PASSWORD) >= 0) {
				result = facesUtil
						.retrieveMessage("errorPasswordExpired");
			} else if (errStr
					.indexOf(LoginConstants.ERROR_LDAP_ACCOUNTS_LOCKED) >= 0) {
				result = facesUtil
						.retrieveMessage("errorAccountLockedOut");
			} else {
				result = facesUtil
						.retrieveMessage("errorConnectingToAD");
			}
		} catch (Exception e) {
		}
		return result;
	}

	private ADConnectionUtil() {
		try {
			ldapURL = ConfigUtil.getInstance().getLdapURL();
			logger.error("LDAP Url = " + ldapURL);
			ldapAuthentication = ConfigUtil.getInstance()
					.getLdapAuthentication();
			logger.error("ldapAuthentication = " + ldapAuthentication);
			ldapDomain = ConfigUtil.getInstance().getLdapDomain();
			logger.error("ldapDomain = " + ldapDomain);
			
			facesUtil = new FacesUtil();
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			System.out.print(e.getMessage());
			//facesUtil.retrieveMessage("errorAccountLockedOut");
		}
		if (ldapURL == null || ldapURL.equals(""))
			logger.error("LDAP Url empty");

		if (ldapAuthentication == null || ldapAuthentication.equals(""))
			logger.error("ldapAuthentication  empty");

		if (ldapDomain == null || ldapDomain.equals(""))
			logger.error("ldapDomain empty");
	}

	public static ADConnectionUtil getInstance() {
		if (instance == null)
			instance = new ADConnectionUtil();
		return instance;
	}

	@SuppressWarnings("all")
	public String connectToAD(String userName, String pwd) {
		String result = "Login Error!";
		try {
			Hashtable env = new Hashtable();

			// Control[] connCtls = new Control[] {new
			// FastBindConnectionControl()};

			env.put(Context.INITIAL_CONTEXT_FACTORY,
					"com.sun.jndi.ldap.LdapCtxFactory");

			// set security credentials
			env.put(Context.SECURITY_AUTHENTICATION, ldapAuthentication);
			env.put(Context.SECURITY_PRINCIPAL, userName + "@" + ldapDomain);
			env.put(Context.SECURITY_CREDENTIALS, pwd);

			// connect to my domain controller
			env.put(Context.PROVIDER_URL, ldapURL);

			// Hit ldap
			// LdapContext ctx = new InitialLdapContext(env,connCtls);
			LdapContext ctx = new InitialLdapContext(env, null);

			// // Create the search controls
			// SearchControls searchCtls = new SearchControls();
			//
			// //Specify the attributes to return
			// //For dirsync use null to return all changes
			// String returnedAtts[]={};
			// searchCtls.setReturningAttributes(returnedAtts);
			//
			// //Specify the search scope
			// searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
			//
			// //specify the LDAP search filter
			// String searchFilter = "(objectClass=domain)";
			//
			// //Specify the Base for the search
			// String searchBase = "DC=maxcrc,DC=co,DC=id";
			//
			// //initialize counter to total the results
			// //int totalResults = 0;
			//
			// // Search for objects using the filter
			// //NamingEnumeration answer = ctx.search(searchBase, searchFilter,
			// searchCtls);
			// ctx.search(searchBase, searchFilter, searchCtls);

			ctx.close();
			result = CommonConstants.MSG_LOGIN_SUCCESSFULL;

		} catch (NamingException e) {
			logger.error(e.getMessage());
			result = getErrorLdap(e.getExplanation());
		}
		return result;
	}

	public static void main(String[] args) {
		String strhasil = ADConnectionUtil.getInstance().connectToAD(
				"tmsdev01", "tmsdev02");
		logger.debug("Hasil : " + strhasil);
	}

	public FacesUtil getFacesUtil() {
		return facesUtil;
	}

	public void setFacesUtil(FacesUtil facesUtil) {
		this.facesUtil = facesUtil;
	}

	

}
