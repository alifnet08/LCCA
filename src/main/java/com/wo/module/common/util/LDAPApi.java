package com.wo.module.common.util;

import java.util.Properties;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import org.apache.log4j.Logger;

import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.parameter.service.ParameterDetailService;

public class LDAPApi {

	static Logger logger = Logger.getLogger(LDAPApi.class);

	public static String LDAP_IP = "LDAP_IP";
	public static String LDAP_PORT = "LDAP_PORT";
	public static String LDAP_SECURITY_PRINCIPAL = "LDAP_SECURITY_PRINCIPAL";
	public static String LDAP_CONTEXT_FACTORY = "LDAP_CONTEXT_FACTORY";
	public static String LDAP_LOCAL_TESTING = "LDAP_LOCAL_TESTING";
	public static String LDAP_BIND_DN = "LDAP_BIND_DN";
	public static String LDAP_PASSWORD = "LDAP_PASSWORD";
	public static String LDAP_USER_NAME = "LDAP_USER_NAME";
	public static String LDAP_ATTR_MAIL = "LDAP_ATTR_MAIL";
	public static String LDAP_FILTER_BY = "LDAP_FILTER_BY";
	public static String LDAP_BASE_DN = "LDAP_BASE_DN";
	public static String LDAP_DOMAIN = "LDAP_DOMAIN";

	public static boolean performAuthentication(ParameterDetailService parameterDetailService, String user,
			String pass) {
		ParameterDetail pdIp = null;
		ParameterDetail pdPort = null;
		ParameterDetail pdSecurityPrincipal = null;
		ParameterDetail pdContextFactory = null;
		ParameterDetail pdAppTesting = null;
		ParameterDetail pdBindDn = null;

		try {
			pdIp = parameterDetailService.getParameterDetailByParamDtlCode(LDAP_IP);
			pdPort = parameterDetailService.getParameterDetailByParamDtlCode(LDAP_PORT);
			pdSecurityPrincipal = parameterDetailService.getParameterDetailByParamDtlCode(LDAP_SECURITY_PRINCIPAL);
			pdContextFactory = parameterDetailService.getParameterDetailByParamDtlCode(LDAP_CONTEXT_FACTORY);
			pdAppTesting = parameterDetailService.getParameterDetailByParamDtlCode(LDAP_LOCAL_TESTING);
			pdBindDn = parameterDetailService.getParameterDetailByParamDtlCode(LDAP_BIND_DN);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		String ldap = pdIp.getNameIn(); // LdapEnum.IP_LDAP.message();
		int port = Integer.valueOf(pdPort.getNameIn());
		String ldapUrl = "ldap://" + ldap + ":" + port;

		DirContext serviceCtx = null;
		try {
			Properties serviceEnv = new Properties();
			serviceEnv.put(Context.INITIAL_CONTEXT_FACTORY, pdContextFactory.getNameIn());
			serviceEnv.put(Context.PROVIDER_URL, ldapUrl);
			serviceEnv.put(Context.SECURITY_AUTHENTICATION, "simple");
			if (pdAppTesting.getNameIn() != null && pdAppTesting.getNameIn().toUpperCase().equals("TRUE")) {
				serviceEnv.put(Context.SECURITY_PRINCIPAL, pdBindDn.getNameIn());
			} else {
				serviceEnv.put(Context.SECURITY_PRINCIPAL, user + "@" + pdSecurityPrincipal.getNameIn());
			}
			serviceEnv.put(Context.SECURITY_CREDENTIALS, pass);
			serviceCtx = new InitialDirContext(serviceEnv);

			return true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (serviceCtx != null) {
				try {
					serviceCtx.close();
				} catch (NamingException e) {
					e.printStackTrace();
				}
			}
		}
		logger.error("Authentication failed");
		return false;
	}

	/*
	 * public static String getUsernameByUserID(String username, String password,
	 * String userID) throws NamingException{ String displayName=""; String ldap =
	 * LdapEnum.IP_LDAP.message(); int port =
	 * Integer.valueOf(LdapEnum.PORT_LDAP.message()); String ldapUrl = "ldap://" +
	 * ldap + ":" + port; DirContext dirContext = null;
	 * 
	 * try { Properties serviceEnv = new Properties();
	 * serviceEnv.put(Context.INITIAL_CONTEXT_FACTORY,
	 * LdapEnum.CONTEXT_FACTORY.message()); serviceEnv.put(Context.PROVIDER_URL,
	 * ldapUrl); serviceEnv.put(Context.SECURITY_AUTHENTICATION, "simple");
	 * serviceEnv.put(Context.SECURITY_PRINCIPAL, username + "@" +
	 * LdapEnum.SECURITY_PRINCIPAL.message());
	 * serviceEnv.put(Context.SECURITY_CREDENTIALS, password);
	 * 
	 * dirContext = new InitialDirContext(serviceEnv); SearchControls searchControls
	 * = new SearchControls(); String[] attributes = {"memberOf","displayName"};
	 * searchControls.setReturningAttributes(attributes);
	 * searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE); String filter =
	 * "(sAMAccountName="+userID+")"; NamingEnumeration<SearchResult> nm =
	 * dirContext.search(LdapEnum.BASE_DN.message()+","+LdapEnum.DOMAIN.message(),
	 * filter, searchControls); while (nm.hasMoreElements()) { SearchResult sr =
	 * (SearchResult)nm.next();
	 * 
	 * Attributes att = sr.getAttributes(); NamingEnumeration<? extends Attribute> c
	 * = att.getAll(); while(c.hasMoreElements()){ Attribute attr = c.next();
	 * if(attr.size()!=0){ if("displayName".equals(attr.getID())){ displayName =
	 * attr.get().toString(); } } }
	 * 
	 * } } catch(Exception e){ //logger.error("Exception in " + getClass().getName()
	 * + ", method [getUsernameByUserID]" + ", error = ", e); e.printStackTrace();
	 * }finally { if(dirContext != null) dirContext.close(); } return displayName; }
	 */

	public static boolean performAuthentication(ParameterDetailService parameterDetailService) {

		ParameterDetail pdIp = null;
		ParameterDetail pdPort = null;
		ParameterDetail pdPassword = null;
		ParameterDetail pdUserName = null;
		ParameterDetail pdContextFactory = null;
		ParameterDetail pdBindDn = null;
		ParameterDetail pdAppTesting = null;
		ParameterDetail pdSecurityPrincipal = null;

		try {
			pdIp = parameterDetailService.getParameterDetailByParamDtlCode(LDAP_IP);
			pdPort = parameterDetailService.getParameterDetailByParamDtlCode(LDAP_PORT);
			pdPassword = parameterDetailService.getParameterDetailByParamDtlCode(LDAP_PASSWORD);
			pdContextFactory = parameterDetailService.getParameterDetailByParamDtlCode(LDAP_CONTEXT_FACTORY);
			pdBindDn = parameterDetailService.getParameterDetailByParamDtlCode(LDAP_BIND_DN);
			pdAppTesting = parameterDetailService.getParameterDetailByParamDtlCode(LDAP_LOCAL_TESTING);
			pdUserName = parameterDetailService.getParameterDetailByParamDtlCode(LDAP_USER_NAME);
			pdSecurityPrincipal = parameterDetailService.getParameterDetailByParamDtlCode(LDAP_SECURITY_PRINCIPAL);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		String ldap = pdIp.getNameIn();
		int port = Integer.valueOf(pdPort.getNameIn());
		String ldapUrl = "ldap://" + ldap + ":" + port;

		DirContext serviceCtx = null;
		try {
			String ldapBindDN = pdBindDn.getNameIn();
			Properties serviceEnv = new Properties();
			serviceEnv.put(Context.INITIAL_CONTEXT_FACTORY, pdContextFactory.getNameIn());
			serviceEnv.put(Context.PROVIDER_URL, ldapUrl);
			serviceEnv.put(Context.SECURITY_AUTHENTICATION, "simple");

			if (pdAppTesting.getNameIn() != null && pdAppTesting.getNameIn().toUpperCase().equals("TRUE")) {
				serviceEnv.put(Context.SECURITY_PRINCIPAL, ldapBindDN);
			} else {
				serviceEnv.put(Context.SECURITY_PRINCIPAL,
						pdUserName.getNameIn() + "@" + pdSecurityPrincipal.getNameIn());
			}

			serviceEnv.put(Context.SECURITY_CREDENTIALS, pdPassword.getNameIn());
			serviceCtx = new InitialDirContext(serviceEnv);

			return true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (serviceCtx != null) {
				try {
					serviceCtx.close();
				} catch (NamingException e) {
					e.printStackTrace();
				}
			}
		}
		logger.error("Authentication failed");
		return false;
	}

	public static String getEmailByUserID(ParameterDetailService parameterDetailService, String userID)
			throws NamingException {

		ParameterDetail pdIp = null;
		ParameterDetail pdPort = null;
		ParameterDetail pdPassword = null;
		ParameterDetail pdUserName = null;
		ParameterDetail pdContextFactory = null;
		ParameterDetail pdBindDn = null;
		ParameterDetail pdAppTesting = null;
		ParameterDetail pdSecurityPrincipal = null;
		ParameterDetail pdAttrMail = null;
		ParameterDetail pdFilterBy = null;
		ParameterDetail pdBaseDn = null;
		ParameterDetail pdDomain = null;

		try {
			pdIp = parameterDetailService.getParameterDetailByParamDtlCode(LDAP_IP);
			pdPort = parameterDetailService.getParameterDetailByParamDtlCode(LDAP_PORT);
			pdPassword = parameterDetailService.getParameterDetailByParamDtlCode(LDAP_PASSWORD);
			pdContextFactory = parameterDetailService.getParameterDetailByParamDtlCode(LDAP_CONTEXT_FACTORY);
			pdBindDn = parameterDetailService.getParameterDetailByParamDtlCode(LDAP_BIND_DN);
			pdAppTesting = parameterDetailService.getParameterDetailByParamDtlCode(LDAP_LOCAL_TESTING);
			pdUserName = parameterDetailService.getParameterDetailByParamDtlCode(LDAP_USER_NAME);
			pdSecurityPrincipal = parameterDetailService.getParameterDetailByParamDtlCode(LDAP_SECURITY_PRINCIPAL);
			pdAttrMail = parameterDetailService.getParameterDetailByParamDtlCode(LDAP_ATTR_MAIL);
			pdFilterBy = parameterDetailService.getParameterDetailByParamDtlCode(LDAP_FILTER_BY);
			pdBaseDn = parameterDetailService.getParameterDetailByParamDtlCode(LDAP_BASE_DN);
			pdDomain = parameterDetailService.getParameterDetailByParamDtlCode(LDAP_DOMAIN);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		String displayName = "";
		String ldap = pdIp.getNameIn();
		int port = Integer.valueOf(pdPort.getNameIn());
		String ldapUrl = "ldap://" + ldap + ":" + port;
		DirContext dirContext = null;

		try {
			String ldapBindDN = pdBindDn.getNameIn();

			Properties serviceEnv = new Properties();
			serviceEnv.put(Context.INITIAL_CONTEXT_FACTORY, pdContextFactory.getNameIn());
			serviceEnv.put(Context.PROVIDER_URL, ldapUrl);
			serviceEnv.put(Context.SECURITY_AUTHENTICATION, "simple");

			if (pdAppTesting.getNameIn() != null && pdAppTesting.getNameIn().toUpperCase().equals("TRUE")) {
				serviceEnv.put(Context.SECURITY_PRINCIPAL, ldapBindDN);
			} else {
				serviceEnv.put(Context.SECURITY_PRINCIPAL,
						pdUserName.getNameIn() + "@" + pdSecurityPrincipal.getNameIn());
			}

			serviceEnv.put(Context.SECURITY_CREDENTIALS, pdPassword.getNameIn());

			dirContext = new InitialDirContext(serviceEnv);
			SearchControls searchControls = new SearchControls();
			String[] attributes = { pdAttrMail.getNameIn() };
			searchControls.setReturningAttributes(attributes);
			searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
			// String filter = "(sAMAccountName="+userID+")";
			String filter = "(" + pdFilterBy.getNameIn() + "=" + userID + ")";
			// NamingEnumeration<SearchResult> nm =
			// dirContext.search(pdBaseDn.getNameIn()+","+pdDomain.getNameIn(), filter,
			// searchControls);
			NamingEnumeration<SearchResult> nm = dirContext.search(pdDomain.getNameIn(), filter, searchControls);
			while (nm.hasMoreElements()) {
				SearchResult sr = (SearchResult) nm.next();
				Attributes att = sr.getAttributes();

				NamingEnumeration<? extends Attribute> c = att.getAll();
				while (c.hasMoreElements()) {
					Attribute attr = c.next();
					if (attr.size() != 0) {
						if (pdAttrMail.getNameIn().equals(attr.getID())) {
							if (displayName.isEmpty()) {
								displayName = attr.get().toString();
							}
						}
					}
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (dirContext != null)
				dirContext.close();
		}
		return displayName;
	}

}
