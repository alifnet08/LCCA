package com.wo.module.header.bean;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.wo.module.common.constant.Constants;
import com.wo.module.log.model.LogLogin;
import com.wo.module.log.service.LogLoginService;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.lov.bean.FacesUtil.FacesScope;
import com.wo.module.user.model.User;

public class HeaderBean implements Serializable {

	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(HeaderBean.class);

	public String classIn;
	public String classEn;
	public FacesUtil facesUtil;
	public String username;
	public String menu;
	private LogLoginService loginService;

	@PostConstruct
	public void init() {
		if (FacesContext.getCurrentInstance() != null && FacesContext.getCurrentInstance().getExternalContext() != null
				&& FacesContext.getCurrentInstance().getExternalContext().getRequest() != null) {
			HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
					.getRequest();
			if (request != null && request.getSession(true) != null) {
				HttpSession session = request.getSession(true);
				User user = (User) session.getAttribute(Constants.SESSION_EMPLOYEE);
				if (user != null) {
					username = user.getName();
				}
				String language = (String) session.getAttribute(Constants.SESSION_LANGUAGE);
				if (language != null && !language.isEmpty()) {
					if (language.equals("English")) {
						changeLocaleToEn();
					} else {
						changeLocaleToIn();
					}
				} else {
					changeLocaleToEn();
				}

				menu = (String) session.getAttribute(Constants.SESSION_MENU);
				if (menu == null) {
					showMenu();
				}
			}
		}
	}

	public void doHandleLogout() {
		logger.debug("loginBean doHandleLogout");
		try {
			HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
					.getRequest();
			HttpSession session = request.getSession(true);
			if (session.getAttribute(Constants.SESSION_LOGIN_ATTEMPT) != null) {
				LogLogin login = (LogLogin) session.getAttribute(Constants.SESSION_LOGIN_ATTEMPT);
				login.setLastLogout(new Timestamp(new Date().getTime()));
				loginService.update(login);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		facesUtil.removeAllSessionAttribute();
		facesUtil.removeAllManagedBeans(FacesScope.SESSION_SCOPE);

		/*Map<String, Object> requestCookieMap = FacesContext.getCurrentInstance()
				   .getExternalContext()
				   .getRequestCookieMap();
		
		if(requestCookieMap.get("userName") != null) {
			try {
				requestCookieMap.remove("userName");
			}catch(Exception e) {
				e.printStackTrace();	
			}
		}*/

		try {
			facesUtil.redirectScreen(Constants.NAVIGATE_LOGIN_REDIRECT_NEW);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String changeLocaleToEn() {
		FacesContext.getCurrentInstance().getViewRoot().setLocale(Locale.ENGLISH);
		facesUtil.setSessionAttribute(Constants.SESSION_LANGUAGE, "ENG");
		classEn = "disabled";
		classIn = "";
		String url = FacesContext.getCurrentInstance().getViewRoot().getViewId();
		// System.out.println("id=="+facesUtil.getSessionAttribute("id"));
		if (url != null && (url.contains("Edit") || url.contains("Detail"))) {
			try {
				Object token = facesUtil.getSessionAttribute("token");
				if (token != null) {
					facesUtil.redirect(url + "?token=" + token);
				} else {
					facesUtil.redirect(url);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			return url;
		} else {
			return url + "?faces-redirect=true";
		}
	}

	public String changeLocaleToIn() {
		Locale locale = new Locale("id", "ID");
		FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);
		facesUtil.setSessionAttribute(Constants.SESSION_LANGUAGE, "BAH");
		classEn = "";
		classIn = "disabled";
		String url = FacesContext.getCurrentInstance().getViewRoot().getViewId();

		if (url != null && (url.contains("Edit") || url.contains("Detail"))) {
			try {
				Object token = facesUtil.getSessionAttribute("token");
				if (token != null) {
					facesUtil.redirect(url + "?token=" + token);
				} else {
					facesUtil.redirect(url);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			return url;
		} else {
			return url + "?faces-redirect=true";
		}
	}

	public void hideMenu() {
		menu = "sidebar-collapse";
		facesUtil.setSessionAttribute(Constants.SESSION_MENU, menu);
	}

	public void showMenu() {
		menu = "";
		facesUtil.setSessionAttribute(Constants.SESSION_MENU, menu);
	}

	public void addMessage(String summary) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, null);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public void addErrMessage(String summary) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, null);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public FacesUtil getFacesUtil() {
		return facesUtil;
	}

	public void setFacesUtil(FacesUtil facesUtil) {
		this.facesUtil = facesUtil;
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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getMenu() {
		return menu;
	}

	public void setMenu(String menu) {
		this.menu = menu;
	}

	public LogLoginService getLoginService() {
		return loginService;
	}

	public void setLoginService(LogLoginService loginService) {
		this.loginService = loginService;
	}
}