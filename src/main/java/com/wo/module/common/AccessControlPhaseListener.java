package com.wo.module.common;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.wo.module.common.bean.AuthBean;
import com.wo.module.common.constant.CommonConstants;
import com.wo.module.common.constant.Constants;
import com.wo.module.menuSession.model.MenuSession;

public class AccessControlPhaseListener implements PhaseListener {
	private static final long serialVersionUID = -407781932726331562L;
	/** Logger for this class */
	private static final Logger logger = Logger.getLogger(AccessControlPhaseListener.class);

	public PhaseId getPhaseId() {
		return PhaseId.RESTORE_VIEW;
	}

	public static String getFullURL(HttpServletRequest request) {
	    StringBuilder requestURL = new StringBuilder(request.getRequestURL().toString());
	    String queryString = request.getQueryString();

	    if (queryString == null) {
	        return requestURL.toString();
	    } else {
	        return requestURL.append('?').append(queryString).toString();
	    }
	}
	
	@SuppressWarnings("unchecked")
	public void afterPhase(PhaseEvent event) {
		FacesContext fc = event.getFacesContext();
		HttpSession session = (HttpSession) fc.getExternalContext().getSession(true);
		HttpServletRequest hreq = (HttpServletRequest) fc.getExternalContext().getRequest();
		String viewId = hreq.getRequestURI();
		String originalUrl = getFullURL(hreq);
		// LoginBean loginBean = new LoginBean();
		AuthBean authBean = new AuthBean();

		String baseContextPath = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
		
		if (CommonConstants.DEPLOY_SERVER_HTTPS != null && CommonConstants.DEPLOY_SERVER_HTTPS.equals("Y")) {
			if(originalUrl!=null && originalUrl.startsWith(CommonConstants.URL_HTTP)) {
				originalUrl = originalUrl.replace(CommonConstants.URL_HTTP, CommonConstants.URL_HTTPS);
				try {
					fc.getExternalContext().redirect(originalUrl);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}

		if (!sessionLoginIsExist(session)) {

			if (authBean.checkExceptionPage(viewId) || authBean.checkErrorPage(viewId)) {
				// allow this

			} else {
				logger.error("Session is not available or user not logged in");
				//logger.info("originalUrl=="+originalUrl);
				
				/*String [] urlWithParam = originalUrl.split("\\?");
				if(urlWithParam!=null && urlWithParam.length > 1 && urlWithParam[1].startsWith("token")) {
					//fc.getApplication().getNavigationHandler().handleNavigation(fc, null, Constants.LOGIN_HOME_ADMIN_URL+"?"+urlWithParam[1]);
					//fc.renderResponse();
					
					
						try {
							fc.getExternalContext().redirect(baseContextPath + Constants.LOGIN_HOME_ADMIN_URL+"?"+urlWithParam[1]);
						} catch (IOException e) {
							e.printStackTrace();
						}
					
				}else {
					//fc.getApplication().getNavigationHandler().handleNavigation(fc, null, Constants.LOGIN_HOME_ADMIN_URL);
					//fc.renderResponse();
					
						try {
							fc.getExternalContext().redirect(baseContextPath + Constants.LOGIN_HOME_ADMIN_URL);
						} catch (IOException e) {
							e.printStackTrace();
						}
					
				}*/
				session.setAttribute("prevUrl", originalUrl);
				
				try {
					if (CommonConstants.DEPLOY_SERVER_HTTPS != null && CommonConstants.DEPLOY_SERVER_HTTPS.equals("Y")) {
						HttpServletRequest origRequest = (HttpServletRequest)fc.getCurrentInstance().getExternalContext().getRequest();
						StringBuffer urlTemp = origRequest.getRequestURL();
						String urlContextTemp = origRequest.getContextPath();
						String urlClean = urlTemp.toString();
						String[] splitUrl = urlClean.split(urlContextTemp);
						String rcp = splitUrl[0];
						rcp = rcp.replace(CommonConstants.URL_HTTP, CommonConstants.URL_HTTPS);
						String finalUrl = rcp + origRequest.getContextPath() + Constants.LOGIN_HOME_ADMIN_URL;
						FacesContext.getCurrentInstance().getExternalContext().redirect(finalUrl);
					}else {
						fc.getExternalContext().redirect(baseContextPath + Constants.LOGIN_HOME_ADMIN_URL);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}

				return;
			}
		} else {

			/*String language = (String) session.getAttribute(Constants.SESSION_LANGUAGE);
			if (language != null && language.toUpperCase().contains("ENG")) {
				FacesContext.getCurrentInstance().getViewRoot().setLocale(Locale.ENGLISH);
			} else {
				Locale locale = new Locale("in", "ID");
				FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);
			}*/
			Locale locale = new Locale("in", "ID");
			FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);
			
			List<MenuSession> menuSessions = (List<MenuSession>) session.getAttribute(Constants.SESSION_ALLOWED_MENU);
			boolean isValid = authBean.validateAccessRight(viewId, menuSessions);
			if (!isValid) {
				//if (originalUrl.contains(".faces?token=")){
					isValid = authBean.checkExceptionPageAfterLogin(viewId);
				//}
			}
			if (!isValid) {
				logger.debug("User Logged in but trying to access unauthorized page");
				
//				if(menuSessions != null && menuSessions.get(0) != null){
//					fc.getApplication()
//							.getNavigationHandler()
//							.handleNavigation(
//									fc,
//									null,
//									LoginConstants.COMPLETE_INVALID_ACCESS_REDIRECT);
//					fc.renderResponse();
//				}else {
					for (MenuSession menuSession : menuSessions) {
						if(menuSession.getMenuAction() != null) {
							String url = menuSession.getMenuAction();
							try {
								fc.getExternalContext().redirect(baseContextPath+url);
							} catch (IOException e) {
								e.printStackTrace();
							}
//							fc.renderResponse();
							break;
						}
					}
//				}
			}
		}
	}

	private boolean sessionLoginIsExist(HttpSession session) {
		if(session != null && (session.getAttribute(Constants.SESSION_EMPLOYEE) != null)){
			return true;
		} else {
			return false;
		}
	}

	public void beforePhase(PhaseEvent event) {
	}
}