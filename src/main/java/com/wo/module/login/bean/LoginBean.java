package com.wo.module.login.bean;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.CommonConstants;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.util.ConfigUtil;
import com.wo.module.common.util.LDAPApi;
import com.wo.module.log.model.LogLogin;
import com.wo.module.log.service.LogLoginService;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.menuSession.model.MenuSession;
import com.wo.module.menuSession.service.MenuSessionService;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.parameter.service.ParameterDetailService;
import com.wo.module.user.model.User;
import com.wo.module.user.service.UserService;


public class LoginBean implements Serializable {
	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(LoginBean.class);

	private String prevUrl;
	private String userName;
	private String password;
	private String token;
	private String menuId;
	private String nextUrl;
	public FacesUtil facesUtil;
	public UserService userService;
	private String classIn;
	private String classEn;
	public List<MenuSession> menuList;
	private String linkTentangMaybank;
	private String linkElearning;
	
	private MenuSessionService menuSessionService;
	private LogLoginService loginService;
	private ParameterDetailService parameterDetailService;

	@PostConstruct
	public void init() {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
	    String remoteUser = request.getRemoteUser();
	    //String userPrincipal = request.getUserPrincipal().getName();
	    String remoteAddr = request.getRemoteAddr();
	    String remoteHost = request.getRemoteHost();
	  
	      //String user = request.getLocalName();
	    //System.out.println("remoteUser=="+remoteUser);
	    //System.out.println("userPrincipal=="+userPrincipal);
	    //System.out.println("remoteAddr=="+remoteAddr);
	    //System.out.println("remoteHost=="+remoteHost);
	    
	    //String username = com.sun.jna.platform.win32.Advapi32Util.getUserName();
	    //System.out.println(username);
	    
	     
		 token = facesUtil.retrieveRequestParam("token");
		 menuId = facesUtil.retrieveRequestParam("menuId");
		
		if (classEn == null && classIn == null) {
			changeLocaleToIn();
		}
		
		try {
			ParameterDetail d = parameterDetailService.getParameterDetailByParamDtlCode(ParameterDetail.PARAM_DET_CODE_LINK_TENTANG_MAYBANK);
			linkTentangMaybank = d.getName();
		} catch (Exception ex) {
			linkTentangMaybank = "#";
		}
		try {
			ParameterDetail d = parameterDetailService.getParameterDetailByParamDtlCode(ParameterDetail.PARAM_DET_CODE_LINK_ELEARNING); 
			linkElearning = d.getName();
		} catch (Exception ex) {
			linkElearning = "#";
		}
		
		Map<String, Object> requestCookieMap = FacesContext.getCurrentInstance()
				   .getExternalContext()
				   .getRequestCookieMap();
		
		String isLogout = facesUtil.retrieveRequestParam("isLogout");
		
		if(isLogout!=null && isLogout.equals("true")) {
			
			setCookie("userName", null, 0);
			setCookie("password", null, 0);
		}
		
		//System.out.println("prevUrl=="+facesUtil.getSessionAttribute("prevUrl"));
		
		prevUrl = facesUtil.getSessionAttribute("prevUrl")!=null?facesUtil.getSessionAttribute("prevUrl").toString():null;
		if(prevUrl!=null) {
			facesUtil.removeSessionAttribute("prevUrl");
		}
		
		userName = requestCookieMap.get("userName")!=null && ((Cookie) requestCookieMap.get("userName")).getValue()!=null ?((Cookie) requestCookieMap.get("userName")).getValue().toString():null;

		//System.out.println("userName=="+userName);
		//System.out.println("isLogout=="+isLogout);
		
		if(userName!=null && (isLogout == null||!isLogout.equals("true"))) {
			password = requestCookieMap.get("password")!= null? ((Cookie)requestCookieMap.get("password")).getValue().toString() :  null;
			//System.out.println("password=="+password);
			doHandleLoginLdap();
		}
		
		
	}

	public void changeLocaleToEn() {
		FacesContext.getCurrentInstance().getViewRoot().setLocale(Locale.ENGLISH);
		classEn = "disabled";
		classIn = "";
	}

	public void changeLocaleToIn() {
		Locale locale = new Locale("in", "ID");
		FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);
		classEn = "";
		classIn = "disabled";
	}

	public void addMessage(String summary) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, null);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public void addErrMessage(String summary) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, null);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public Boolean validate() {
		Boolean result = false;
		if (StringUtils.isEmpty(userName)) {
			addErrMessage("Username harus diisi");
			result = true;
		} else if (StringUtils.isEmpty(password)) {
			addErrMessage("Password harus diisi");
			result = true;
		}
		return result;
	}
	
	public void setCookie(String name, String value, int expiry) {

	    FacesContext facesContext = FacesContext.getCurrentInstance();

	    HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
	    Cookie cookie = null;

	    Cookie[] userCookies = request.getCookies();
	    if (userCookies != null && userCookies.length > 0 ) {
	        for (int i = 0; i < userCookies.length; i++) {
	            if (userCookies[i].getName().equals(name)) {
	                cookie = userCookies[i];
	                break;
	            }
	        }
	    }

	    if (cookie != null) {
	        cookie.setValue(value);
	    } else {
	        cookie = new Cookie(name, value);
	        cookie.setPath(request.getContextPath());
	    }

	    cookie.setMaxAge(expiry);

	    HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
	    response.addCookie(cookie);
	  }

	  public Cookie getCookie(String name) {

	    FacesContext facesContext = FacesContext.getCurrentInstance();

	    HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
	    Cookie cookie = null;

	    Cookie[] userCookies = request.getCookies();
	    if (userCookies != null && userCookies.length > 0 ) {
	        for (int i = 0; i < userCookies.length; i++) {
	            if (userCookies[i].getName().equals(name)) {
	                cookie = userCookies[i];
	                return cookie;
	            }
	        }
	    }
	    return null;
	  }

	public String doHandleLoginLdap() {
		logger.debug("loginBean doHandleLoginLdap");
		String language = facesUtil.retrieveRequestParam("LANGUAGE");
		//String token = facesUtil.retrieveRequestParam("token");
		//String menuId = facesUtil.retrieveRequestParam("menuId");
		String result = null;
		HttpServletRequest request = null;
		HttpSession session = null;
		LogLogin login = null;
		try {
			request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
					.getRequest();
			session = request.getSession(true);
			
			if (session.getAttribute(Constants.SESSION_LOGIN_ATTEMPT) != null) {
				updateLoginInfo(request, session);
			} else {
				newLoginInfo(request, session);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			FacesMessage errorMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error:" + ex.getMessage(),
					ex.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, errorMessage);
			logger.error(ex.getMessage(), ex);
		}
		
		ConfigUtil cu = ConfigUtil.getInstance();
		try {
			//String message = doLdapAuth(cu, userName, password);
			String message = CommonConstants.MSG_LOGIN_SUCCESSFULL;
			if (message != null && message.equals(CommonConstants.MSG_LOGIN_SUCCESSFULL)) {
				logger.debug("login Successfull");
				
				
				//Object userNameNew = requestCookieMap.get("userName");
				
				Map<java.lang.String,java.lang.Object> properties = new HashMap<String, Object>();
				properties.put("maxAge", 31536000);//satu tahun
				FacesContext.getCurrentInstance().getExternalContext().addResponseCookie("userName", userName, properties);
				FacesContext.getCurrentInstance().getExternalContext().addResponseCookie("password", password, properties);
				
				//setCookie("userName", userName, 31536000);
				
				if (session != null && session.getAttribute(Constants.SESSION_LOGIN_ATTEMPT) != null) {
					login = (LogLogin) session.getAttribute(Constants.SESSION_LOGIN_ATTEMPT);
				}

				request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
						.getRequest();
				session = request.getSession(false);
				if (session!=null && !session.isNew()) {
				    session.invalidate();
				}
				session = request.getSession(true);
				User user = userService.getUserByUserLogin(userName);
				session.setAttribute(Constants.SESSION_EMPLOYEE, user);
				if(user!=null && user.getNik()!=null) {
					session.setAttribute(Constants.SESSION_NIK, user.getNik());
				}else {
					user = new User();
					user.setName(userName);
					user.setUserId(new Long(0));
					session.setAttribute(Constants.SESSION_NIK, userName);
					session.setAttribute(Constants.SESSION_EMPLOYEE, user);
				}
				
				session.setAttribute(Constants.SESSION_LANGUAGE, language);
				session.setAttribute(Constants.SESSION_LINK_CORPORATE_PORTAL, linkTentangMaybank);
				session.setAttribute(Constants.SESSION_LINK_ELEARNING, linkElearning);
				
				if (login != null) {
					login.setUser(user);
					login.setLastLogin(new Timestamp(new Date().getTime()));
					session.setAttribute(Constants.SESSION_LOGIN_ATTEMPT, login);
				}
				
				menuList = getMenuSessionService().getListMenuAllowed(userName);
				session.setAttribute(Constants.SESSION_ALLOWED_MENU, menuList);
				
				/*if(StringUtils.isNotEmpty(token) && StringUtils.isNotEmpty(menuId)) {
					
					String menuIdEncrypt = Constants.decryptString(menuId);
					if(menuIdEncrypt.equals(Constants.MENU_ID_FOLLOWUP_CONFIRMATION_SOCIALIZATION)) {
						//result = "/pages/picFollowupConfirmation/picFollowupConfirmationEdit.faces?token="+token;
						result = "/pages/socializationFE/socializationFEEdit.faces?token="+token;
						session.setAttribute(Constants.SESSION_NEED_REDIRECT, "Y");
						session.setAttribute("token", token);
					}
					else if(menuIdEncrypt.equals(Constants.MENU_ID_FOLLOWUP_CONFIRMATION_CORRESPONDENCE)) {
						result = "/pages/correspondenceFE/correspondenceFEEdit.faces?token="+token;
						session.setAttribute(Constants.SESSION_NEED_REDIRECT, "Y");
						session.setAttribute("token", token);
					}
					else if(menuIdEncrypt.equals(Constants.MENU_ID_FOLLOWUP_CONFIRMATION_FINE)) {
						result = "/pages/fineFE/fineFEEdit.faces?token="+token;
						session.setAttribute(Constants.SESSION_NEED_REDIRECT, "Y");
						session.setAttribute("token", token);
					}
					else if(menuIdEncrypt.equals(Constants.MENU_ID_APPROVAL_REQUEST_INTERNAL_REGULATION)) {
						result = "/pages/dashboard/dashboard.faces?menuId="+menuId+"&token="+token;
						session.setAttribute(Constants.SESSION_NEED_REDIRECT, "Y");
						session.setAttribute("token", token);
					}
					else if(menuIdEncrypt.equals(Constants.MENU_ID_APPROVAL_REQUEST_EXTERNAL_REGULATION)) {
						result = "/pages/dashboard/dashboard.faces?menuId="+menuId+"&token="+token;
						session.setAttribute(Constants.SESSION_NEED_REDIRECT, "Y");
						session.setAttribute("token", token);
					}
					else if(menuIdEncrypt.equals(Constants.MENU_ID_APPROVAL_REQUEST_ARTICLE)) {
						result = "/pages/dashboard/dashboard.faces?menuId="+menuId+"&token="+token;
						session.setAttribute(Constants.SESSION_NEED_REDIRECT, "Y");
						session.setAttribute("token", token);
					}
					else if(menuIdEncrypt.equals(Constants.MENU_ID_APPROVAL_REQUEST_AUDIT)) {
						result = "/pages/dashboard/dashboard.faces?menuId="+menuId+"&token="+token;
						session.setAttribute(Constants.SESSION_NEED_REDIRECT, "Y");
						session.setAttribute("token", token);
					}
					else if(menuIdEncrypt.equals(Constants.MENU_ID_FOLLOWUP_CONFIRMATION_CORRESPONDENCE_AML)) {
						result = "/pages/trcCorrespondenceAml/trcCorrespondenceAmlEdit.faces?token="+token;
						session.setAttribute(Constants.SESSION_NEED_REDIRECT, "Y");
						session.setAttribute("token", token);
					}
					else if(menuIdEncrypt.equals(Constants.MENU_ID_FOLLOWUP_CONFIRMATION_RMD)) {
						result = "/pages/trcRmd/trcRmdEdit.faces?token="+token;
						session.setAttribute(Constants.SESSION_NEED_REDIRECT, "Y");
						session.setAttribute("token", token);
					}
					else if(menuIdEncrypt.equals(Constants.MENU_ID_FOLLOWUP_CONFIRMATION_COMPLIANCE_REVIEW)) {
						result = "/pages/complianceTestingFE/complianceTestingFEEdit.faces?token="+token;
						session.setAttribute(Constants.SESSION_NEED_REDIRECT, "Y");
						session.setAttribute("token", token);
					}
					else if(menuIdEncrypt.equals(Constants.MENU_ID_FOLLOWUP_CONFIRMATION_REG_MONITORING)) {
						result = "/pages/regulationMonitoringFE/regulationMonitoringFEEdit.faces?token="+token;
						session.setAttribute(Constants.SESSION_NEED_REDIRECT, "Y");
						session.setAttribute("token", token);
					}
					else if(menuIdEncrypt.equals(Constants.MENU_ID_COMPLIANCE_REVIEW_VIEW)) {
						result = "/pages/complianceTestingFE/complianceTestingFEEdit.faces?token="+token;
						session.setAttribute(Constants.SESSION_NEED_REDIRECT, "Y");
						session.setAttribute("token", token);
					}
					else if(menuIdEncrypt.equals(Constants.MENU_ID_SOCIALIZATION_VIEW)) {
						result = "/pages/regulationSocializationView/regulationSocializationViewDetail.faces?token="+token;
						session.setAttribute(Constants.SESSION_NEED_REDIRECT, "Y");
						session.setAttribute("token", token);
					}
					else if(menuIdEncrypt.equals(Constants.MENU_ID_CORRESPONDENCE_VIEW)) {
						result = "/pages/trcCorrespondenceView/trcCorrespondenceViewEdit.faces?token="+token;
						session.setAttribute(Constants.SESSION_NEED_REDIRECT, "Y");
						session.setAttribute("token", token);
					}
					else if(menuIdEncrypt.equals(Constants.MENU_ID_CORRESPONDENCE_AML_VIEW)) {
						result = "/pages/trcCorrespondenceViewAml/trcCorrespondenceViewAmlEdit.faces?token="+token;
						session.setAttribute(Constants.SESSION_NEED_REDIRECT, "Y");
						session.setAttribute("token", token);
					}
					else if(menuIdEncrypt.equals(Constants.MENU_ID_FOLLOWUP_CONFIRMATION_AUDIT)) {
						result = "/pages/auditFE/auditFEEdit.faces?token="+token;
						session.setAttribute(Constants.SESSION_NEED_REDIRECT, "Y");
						session.setAttribute("token", token);
					}
					else if(menuIdEncrypt.equals(Constants.MENU_ID_AUDIT_VIEW)) {
						result = "/pages/trcAuditView/trcAuditViewEdit.faces?token="+token;
						session.setAttribute(Constants.SESSION_NEED_REDIRECT, "Y");
						session.setAttribute("token", token);
					}
					else if(menuIdEncrypt.equals(Constants.MENU_ID_LITIGATION_VIEW)) {
						result = "/pages/litigationViewFE/litigationViewFEView.faces?token="+token;
						session.setAttribute(Constants.SESSION_NEED_REDIRECT, "Y");
						session.setAttribute("token", token);
					} else if(menuIdEncrypt.equals(Constants.MENU_ID_CPAS_FE)) {
						result = "pages/cpsaFE/compliancePlanSelfAssessmentFEEdit.faces?token="+token;
						session.setAttribute(Constants.SESSION_NEED_REDIRECT, "Y");
						session.setAttribute("token", token);
					} else if(menuIdEncrypt.equals(Constants.MENU_ID_CPAS_APPROVAL_FE)) {
						result = "pages/cpsaApprovalFE/cpsaApprovalFEEdit.faces.faces?token="+token;
						session.setAttribute(Constants.SESSION_NEED_REDIRECT, "Y");
						session.setAttribute("token", token);
					} else if(menuIdEncrypt.equals(Constants.MENU_ID_CPAS_REJECTED_FE)) {
						result = "pages/cpsaFE/compliancePlanSelfAssessmentFEEdit.faces?token="+token;
						session.setAttribute(Constants.SESSION_NEED_REDIRECT, "Y");
						session.setAttribute("token", token);
					}
					else {
						result = "/pages/dashboard/dashboardFrontEnd.faces";
					}
					
				}else {
					   result = "/pages/dashboard/dashboardFrontEnd.faces";
				}*/
				
				
				if(prevUrl!=null) {
					if (CommonConstants.DEPLOY_SERVER_HTTPS != null && CommonConstants.DEPLOY_SERVER_HTTPS.equals("Y")) {
						prevUrl = prevUrl.replace(CommonConstants.URL_HTTP, CommonConstants.URL_HTTPS);
					}
					FacesContext.getCurrentInstance().getExternalContext()
					.redirect(prevUrl);
					
				}else {
					result = "/pages/dashboard/dashboardFrontEnd.faces";
					//String baseContextPath = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
					//FacesContext.getCurrentInstance().getExternalContext()
							//.redirect(baseContextPath + result);
					facesUtil.redirectScreen(result);
				}
				
				return null;

			} else {
				/*FacesMessage errorMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR,
						facesUtil.retrieveMessage("errorLoginAttemptFailNoParam") + " " + facesUtil.retrieveMessage("textBecause") + " " + message, result);*/
				FacesMessage errorMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR,
						facesUtil.retrieveMessage("errorLoginAttemptFailNoParam"), result);
				FacesContext.getCurrentInstance().addMessage(null, errorMessage);
				logger.error(message);
				
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			FacesMessage errorMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login Error:" + ex.getMessage(),
					ex.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, errorMessage);
			logger.error(ex.getMessage(), ex);
		} finally{
			
			try {
//				if (session != null && session.getAttribute(Constants.SESSION_LOGIN_ATTEMPT) != null) {
//					login = (LogLogin) session.getAttribute(Constants.SESSION_LOGIN_ATTEMPT);
//					if (login.isSave()) {
//						loginService.save(login);
//						login.setSave(false);
//						session.setAttribute(Constants.SESSION_LOGIN_ATTEMPT, login);
//					} else
//						loginService.update(login);
//				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			
		}
		return result;
	}
	
	private void newLoginInfo(HttpServletRequest request, HttpSession session) throws Exception {
		LogLogin login = new LogLogin(userName, getClientIpAddr(request), new Timestamp(new Date().getTime()), new Integer(1));
		session.setAttribute(Constants.SESSION_LOGIN_ATTEMPT, login);
		loginService.save(login);
	}
	
	private void updateLoginInfo(HttpServletRequest request, HttpSession session) throws Exception {
		LogLogin login = (LogLogin) session.getAttribute(Constants.SESSION_LOGIN_ATTEMPT);
		login.setUsername(userName);
		login.setAccessTime(new Timestamp(new Date().getTime()));
		login.setRetryAttempt((login.getRetryAttempt() != null) ? login.getRetryAttempt().intValue() + 1 : 1);
		login.setSave(false);
		session.setAttribute(Constants.SESSION_LOGIN_ATTEMPT, login);
		loginService.update(login);
	}

	private String doLdapAuth(ConfigUtil cu, String userNameInput, String passInput) throws SQLException {
		String message = null;
		if (cu.getAppTesting() && StringUtils.isNotBlank(userNameInput)) {
			message = CommonConstants.MSG_LOGIN_SUCCESSFULL;
		} else {
			boolean ldapResult = LDAPApi.performAuthentication(parameterDetailService,userName, password);
			if(ldapResult){
				message = CommonConstants.MSG_LOGIN_SUCCESSFULL;
			}else{
				message = "User Name atau Password Salah";
			}
			//message = ADConnectionUtil.getInstance().connectToAD(userNameInput, passInput);
		}
		return message;
	}
	
	public static String getClientIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("X-Forwarded-For");
		if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
			ip = request.getHeader("HTTP_X_FORWARDED");
		}
		if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
			ip = request.getHeader("HTTP_X_CLUSTER_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
			ip = request.getHeader("HTTP_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
			ip = request.getHeader("HTTP_FORWARDED");
		}
		if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
			ip = request.getHeader("HTTP_VIA");
		}
		if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
			ip = request.getHeader("REMOTE_ADDR");
		}
		if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public FacesUtil getFacesUtil() {
		return facesUtil;
	}

	public void setFacesUtil(FacesUtil facesUtil) {
		this.facesUtil = facesUtil;
	}

	public Boolean getIsLogin() {
		if (facesUtil.getSessionAttribute(Constants.SESSION_EMPLOYEE) != null
				|| facesUtil.getSessionAttribute(Constants.SESSION_KANDIDAT) != null)
			return true;
		else
			return false;
	}

	public String getNextUrl() {
		return nextUrl;
	}

	public void setNextUrl(String nextUrl) {
		this.nextUrl = nextUrl;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public String getClassIn() {
		return classIn;
	}

	public void setClassIn(String classIn) {
		this.classIn = classIn;
	}

	public String getClassEn() {
		return classEn;
	}

	public void setClassEn(String classEn) {
		this.classEn = classEn;
	}

	public MenuSessionService getMenuSessionService() {
		return menuSessionService;
	}

	public void setMenuSessionService(MenuSessionService menuSessionService) {
		this.menuSessionService = menuSessionService;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getMenuId() {
		return menuId;
	}

	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}

	public LogLoginService getLoginService() {
		return loginService;
	}

	public void setLoginService(LogLoginService loginService) {
		this.loginService = loginService;
	}

	public ParameterDetailService getParameterDetailService() {
		return parameterDetailService;
	}

	public void setParameterDetailService(ParameterDetailService parameterDetailService) {
		this.parameterDetailService = parameterDetailService;
	}

	public List<MenuSession> getMenuList() {
		return menuList;
	}

	public void setMenuList(List<MenuSession> menuList) {
		this.menuList = menuList;
	}

	public String getLinkTentangMaybank() {
		return linkTentangMaybank;
	}

	public void setLinkTentangMaybank(String linkTentangMaybank) {
		this.linkTentangMaybank = linkTentangMaybank;
	}

	public String getLinkElearning() {
		return linkElearning;
	}

	public void setLinkElearning(String linkElearning) {
		this.linkElearning = linkElearning;
	}

	public String getPrevUrl() {
		return prevUrl;
	}

	public void setPrevUrl(String prevUrl) {
		this.prevUrl = prevUrl;
	}

	
}