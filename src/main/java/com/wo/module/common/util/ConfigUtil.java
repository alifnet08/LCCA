package com.wo.module.common.util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.wo.module.lov.bean.FacesUtil;

public class ConfigUtil {

	private static ConfigUtil me;

	private static Logger logger = Logger.getLogger(ConfigUtil.class);

	static {
		Document doc;

		try {
			if (FacesContext.getCurrentInstance() != null) {
				
				  ServletContext servletContext = (ServletContext) FacesContext
				  .getCurrentInstance().getExternalContext().getContext(); doc =
				  ConfigUtil.parseXML(servletContext
				  .getResourceAsStream(FacesUtil.COMPLIANCE_CONFIG_FILE));
				 
				//doc = ConfigUtil.parseXML(ConfigUtil.class.getClass().getResourceAsStream(FacesUtil.OSCAR_CONFIG_FILE));
				ConfigUtil.getInstance().buildConfiguration(doc);
			}
		} catch (Exception e) {
			logger.error(
					"Error when load Config file, error message : "
							+ e.getMessage(), e);
		}

		doc = null;
	}

	public static ConfigUtil getInstance() {
		if (ConfigUtil.me == null) {
			ConfigUtil.me = new ConfigUtil();
		}
		return ConfigUtil.me;
	}

	// Private
	private static final Document parseXML(InputStream is) throws Exception {
		BufferedInputStream bis = null;
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			factory.setValidating(false);

			DocumentBuilder parser = factory.newDocumentBuilder();
			// parser.setErrorHandler(new XMLErrorHandler(logger));
			// parser.setEntityResolver(new ConfigEntityResolver());
			bis = new BufferedInputStream(is);
			return parser.parse(bis);
		} catch (Throwable e) {
			throw new Exception(e);
		} finally {
			if (bis != null) {
				try {
					bis.close();
				} catch (IOException e) {
				}

				bis = null;
			}
		}
	}

	private String invoiceDateFormat;

	private String serverDestinationFolder;

	/** LDAP Connection */
	private String ldapURL;

	private String ldapAuthentication;

	private String ldapDomain;

	/** Email Connection */
	private String smtpServerHost;

	private Integer smtpServerPort;

	private boolean smtpAuth;

	private boolean smtpStartTlsEnable;

	private String defaultEmailUsername;

	private String defaultEmailPassword;

	private String defaultEmail;

	private String emailBackgroundImage;

	private String emailBackgroundImageWidth;

	private String emailBackgroundImageHeight;

	/** Open Office Connection */
	private String openOfficeSocketPorts;

	private String openOfficePipeNames;

	private boolean openOfficeUsingSocket;

	private boolean openOfficeUsingPipe;

	private Long openOfficeTaskExecTimeout;

	private String openOfficePath;

	/** Application */
	private boolean appTesting;
	private String noLdapAdminUser;

	private String appTestingEmail;
	private String appTestingMobileNo;

	private List<NoAuthUrl> noAuthUrls = new ArrayList<NoAuthUrl>();
	private List<AuthAnyoneUrl> authAnyoneUrls = new ArrayList<AuthAnyoneUrl>();

	private ConfigUtil() {

	}

	private final void buildConfiguration(Document doc) throws Exception {
		/*updateLdapConfig(doc);*/
		/*updateEmailConfig(doc);*/
		updateAppConfig(doc);
		/*updateNoAuthUrl(doc);
		updateAuthAnyoneUrl(doc);
		updateOpenOfficeConfiguration(doc);*/
	}

	@SuppressWarnings("unused")
	private void updateAuthAnyoneUrl(Document doc) {

		Node node = doc.getElementsByTagName("auth-anyone-urls").item(0);
		NodeList childs = node.getChildNodes();
		for (int i = 0, sum = childs.getLength(); i < sum; i++) {
			if (StringUtils.equals("auth-anyone-url", childs.item(i)
					.getNodeName())) {
				NamedNodeMap attrs = childs.item(i).getAttributes();
				String value = attrs.getNamedItem("value").getNodeValue();
				Node nodeIsRegex = attrs.getNamedItem("isRegex");
				String isRegex = nodeIsRegex == null ? "" : nodeIsRegex
						.getNodeValue();
				authAnyoneUrls.add(new AuthAnyoneUrl(value,
						new Boolean(isRegex)));
			}
		}

	}

	@SuppressWarnings("unused")
	private void updateNoAuthUrl(Document doc) {

		Node node = doc.getElementsByTagName("no-auth-urls").item(0);
		NodeList childs = node.getChildNodes();
		for (int i = 0, sum = childs.getLength(); i < sum; i++) {
			if (StringUtils.equals("no-auth-url", childs.item(i).getNodeName())) {
				NamedNodeMap attrs = childs.item(i).getAttributes();
				String value = attrs.getNamedItem("value").getNodeValue();
				Node nodeIsRegex = attrs.getNamedItem("isRegex");
				String isRegex = nodeIsRegex == null ? "" : nodeIsRegex
						.getNodeValue();
				noAuthUrls.add(new NoAuthUrl(value, new Boolean(isRegex)));
			}
		}
	}

	private void updateAppConfig(Document doc) {

		// tender-approval-app Config
		Node node = doc.getElementsByTagName("compliance-app").item(0);
		NamedNodeMap attributes = node.getAttributes();

		if (attributes.getNamedItem("app-testing") != null) {
			this.appTesting = "true".equalsIgnoreCase(attributes.getNamedItem(
					"app-testing").getNodeValue());
		} else {
			this.appTesting = false;
		}

		/*if (attributes.getNamedItem("app-testing-email") != null) {
			this.appTestingEmail = attributes.getNamedItem("app-testing-email")
					.getNodeValue();
		}
		
		if (attributes.getNamedItem("app-testing-mobile-no") != null) {
			this.setAppTestingMobileNo(attributes.getNamedItem("app-testing-mobile-no")
					.getNodeValue());
		}
		
		if (attributes.getNamedItem("no-ldap-admin-user") != null) {
			this.noLdapAdminUser = attributes
					.getNamedItem("no-ldap-admin-user").getNodeValue();
		}*/

	}

	@SuppressWarnings("unused")
	private void updateEmailConfig(Document doc) {
		// Email Config
		Node node = doc.getElementsByTagName("email").item(0);
		NamedNodeMap attributes = node.getAttributes();

		if (attributes.getNamedItem("smtp-server-host") != null) {
			this.smtpServerHost = attributes.getNamedItem("smtp-server-host")
					.getNodeValue();
		}
		if (attributes.getNamedItem("smtp-server-port") != null) {
			try {
				this.smtpServerPort = Integer.valueOf(attributes.getNamedItem(
						"smtp-server-port").getNodeValue());
			} catch (NumberFormatException nfe) {
				logger.error(
						"Unable to convert integer value of smtp-server-port property at file config "
								+ FacesUtil.COMPLIANCE_CONFIG_FILE + " !!", nfe);
			}
		}

		if (attributes.getNamedItem("smtp-auth") != null) {
			this.smtpAuth = "true".equalsIgnoreCase(attributes.getNamedItem(
					"smtp-auth").getNodeValue());
		} else {
			this.smtpAuth = false;
		}

		if (attributes.getNamedItem("smtp-starttls-enable") != null) {
			this.smtpStartTlsEnable = "true".equals(attributes.getNamedItem(
					"smtp-starttls-enable").getNodeValue());
		} else {
			this.smtpStartTlsEnable = false;
		}

		if (attributes.getNamedItem("default-email-username") != null) {
			this.defaultEmailUsername = attributes.getNamedItem(
					"default-email-username").getNodeValue();
		}

		if (attributes.getNamedItem("default-email-password") != null) {
			this.defaultEmailPassword = attributes.getNamedItem(
					"default-email-password").getNodeValue();
		}

		if (attributes.getNamedItem("default-email-dvs") != null) {
			this.defaultEmail = attributes.getNamedItem("default-email-dvs")
					.getNodeValue();
		}

		if (attributes.getNamedItem("email-background-image") != null) {
			this.emailBackgroundImage = attributes.getNamedItem(
					"email-background-image").getNodeValue();
		}

		if (attributes.getNamedItem("email-background-image-height") != null) {
			this.emailBackgroundImageHeight = attributes.getNamedItem(
					"email-background-image-height").getNodeValue();
		}

		if (attributes.getNamedItem("email-background-image-width") != null) {
			this.emailBackgroundImageWidth = attributes.getNamedItem(
					"email-background-image-width").getNodeValue();
		}

	}

	private void updateLdapConfig(Document doc) {
		// LDAP Config
		Node node = doc.getElementsByTagName("ldap").item(0);
		NamedNodeMap attributes = node.getAttributes();

		this.ldapURL = attributes.getNamedItem("url").getNodeValue();
		this.ldapAuthentication = attributes.getNamedItem("authentication")
				.getNodeValue();
		this.ldapDomain = attributes.getNamedItem("domain").getNodeValue();

	}

	@SuppressWarnings("unused")
	private void updateOpenOfficeConfiguration(Document doc) {
		// Open Office Connection Config
		NodeList nodeList = doc.getElementsByTagName("open-office-connection");
		if (nodeList != null && nodeList.getLength() > 0) {
			Node node = nodeList.item(0);
			NamedNodeMap attributes = node.getAttributes();

			if (attributes.getNamedItem("path") != null) {
				if (StringUtils.isNotBlank(attributes.getNamedItem("path")
						.getNodeValue())) {
					this.openOfficePath = attributes.getNamedItem("path")
							.getNodeValue();
				}
			}
			if (attributes.getNamedItem("task-exec-timeout") != null) {
				try {
					this.openOfficeTaskExecTimeout = Long.valueOf(attributes
							.getNamedItem("task-exec-timeout").getNodeValue());
				} catch (NumberFormatException nfe) {
					logger.error(
							"Unable to convert long value of task-exec-timeout property at file config "
									+ FacesUtil.COMPLIANCE_CONFIG_FILE + " !!", nfe);
				}
			}

			// read first child node only
			if (node.getChildNodes() != null) {
				Node childNode = null;
				for (int i = 0; i < node.getChildNodes().getLength(); i++) {
					childNode = node.getChildNodes().item(i);
					attributes = childNode.getAttributes();
					if ("tcp-socket".equals(childNode.getNodeName())) {
						this.openOfficeUsingSocket = true;
						this.openOfficeSocketPorts = attributes.getNamedItem(
								"ports").getNodeValue();
						break;
					} else if ("pipe".equals(childNode.getNodeName())) {
						this.openOfficeUsingPipe = true;
						this.openOfficePipeNames = attributes.getNamedItem(
								"names").getNodeValue();
						break;
					}
				}
			}
		}
	}

	public String getInvoiceDateFormat() {
		return this.invoiceDateFormat;
	}

	public String getServerDestinationFolder() {
		return this.serverDestinationFolder;
	}

	public void setInvoiceDateFormat(String invoiceDateFormat) {
		this.invoiceDateFormat = invoiceDateFormat;
	}

	public void setServerDestinationFolder(String serverDestinationFolder) {
		this.serverDestinationFolder = serverDestinationFolder;
	}

	public String getLdapURL() {
		return ldapURL;
	}

	public String getLdapAuthentication() {
		return ldapAuthentication;
	}

	public String getLdapDomain() {
		return ldapDomain;
	}

	public String getSmtpServerHost() {
		return smtpServerHost;
	}

	public void setSmtpServerHost(String smtpServerHost) {
		this.smtpServerHost = smtpServerHost;
	}

	public Integer getSmtpServerPort() {
		return smtpServerPort;
	}

	public void setSmtpServerPort(Integer smtpServerPort) {
		this.smtpServerPort = smtpServerPort;
	}

	public boolean isSmtpAuth() {
		return smtpAuth;
	}

	public void setSmtpAuth(boolean smtpAuth) {
		this.smtpAuth = smtpAuth;
	}

	public boolean isSmtpStartTlsEnable() {
		return smtpStartTlsEnable;
	}

	public void setSmtpStartTlsEnable(boolean smtpStartTlsEnable) {
		this.smtpStartTlsEnable = smtpStartTlsEnable;
	}

	public String getDefaultEmailUsername() {
		return defaultEmailUsername;
	}

	public String getDefaultEmailPassword() {
		return defaultEmailPassword;
	}

	public String getNoLdapAdminUser() {
		return noLdapAdminUser;
	}

	public boolean getAppTesting() {
		return appTesting;
	}

	public String getAppTestingEmail() {
		return appTestingEmail;
	}

	public String getDefaultEmail() {
		return defaultEmail;
	}

	public void setDefaultEmail(String defaultEmail) {
		this.defaultEmail = defaultEmail;
	}

	public List<NoAuthUrl> getNoAuthUrls() {
		return noAuthUrls;
	}

	public List<AuthAnyoneUrl> getAuthAnyoneUrls() {
		return authAnyoneUrls;
	}

	public static class NoAuthUrl {
		public String url;
		public boolean isRegex;

		public NoAuthUrl(String url, boolean isRegex) {
			super();
			this.url = url;
			this.isRegex = isRegex;
		}
	}

	public static class AuthAnyoneUrl {
		public String url;
		public boolean isRegex;

		public AuthAnyoneUrl(String url, boolean isRegex) {
			super();
			this.url = url;
			this.isRegex = isRegex;
		}
	}

	public String getEmailBackgroundImage() {
		return emailBackgroundImage;
	}

	public String getEmailBackgroundImageWidth() {
		return emailBackgroundImageWidth;
	}

	public String getEmailBackgroundImageHeight() {
		return emailBackgroundImageHeight;
	}

	public String getOpenOfficeSocketPorts() {
		return openOfficeSocketPorts;
	}

	public String getOpenOfficePipeNames() {
		return openOfficePipeNames;
	}

	public boolean isOpenOfficeUsingSocket() {
		return openOfficeUsingSocket;
	}

	public boolean isOpenOfficeUsingPipe() {
		return openOfficeUsingPipe;
	}

	public Long getOpenOfficeTaskExecTimeout() {
		return openOfficeTaskExecTimeout;
	}

	public String getOpenOfficePath() {
		return openOfficePath;
	}

	public String getAppTestingMobileNo() {
		return appTestingMobileNo;
	}

	public void setAppTestingMobileNo(String appTestingMobileNo) {
		this.appTestingMobileNo = appTestingMobileNo;
	}
}