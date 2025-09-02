/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.lov.bean;

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.sql.Clob;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.wo.module.common.constant.CommonConstants;
import com.wo.module.common.constant.Constants;
import com.wo.module.user.model.User;

/**
 *
 * @author hendra
 */
public class FacesUtil implements Serializable {
    
   private static final long serialVersionUID = 8784966416727931077L;

	public static enum FacesScope {
		APPLICATION_SCOPE, FLASH_SCOPE, REQUEST_SCOPE, SESSION_SCOPE, VIEW_SCOPE
	}
	
	public static final String COMPLIANCE_CONFIG_FILE = "/WEB-INF/compliance-config.xml";
    
    public String retrieveRequestParam(String key) {
        return FacesContext.getCurrentInstance()
                .getExternalContext().getRequestParameterMap().get(key);
    }
    
    public UIComponent retrieveDataTable(String clientId,String formName,String tableName) {
        return FacesContext.getCurrentInstance().getViewRoot().findComponent(clientId)
                .findComponent(formName)
                .findComponent(tableName);
    }
    
    public Locale retrieveDefaultLocale() {
        return FacesContext.getCurrentInstance().getViewRoot().getLocale();
    }
    
    public String retrieveRequestQueryString(String key) {        
        return retrieveActiveServletRequest().getParameter(key);
    }
    
    public String retrieveRequestHeader(String key) {        
        return retrieveActiveServletRequest().getHeader(key);
    }
    
    public String retrieveContextPath() {
        return retrieveActiveServletRequest().getContextPath();
    }

    public String retrieveRequestURI() {
        return retrieveActiveServletRequest().getRequestURI();
    }
    
    public String retrieveServerName() {
        return retrieveActiveServletRequest().getServerName();
    }
    
    public Integer retrieveServerPort() {
        return retrieveActiveServletRequest().getServerPort();
    }
    
    public HttpServletRequest retrieveActiveServletRequest() {
        return (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
    } 
    
    public String retrieveMessage(String key) {
        System.out.println("locale " + FacesContext.getCurrentInstance().getViewRoot().getLocale());
        FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle bundle = context.getApplication().getResourceBundle(context, "msg");
        return bundle.getString(key);        
    }

    public String retrieveMessage(String key, String ... values ) {
        FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle bundle = context.getApplication().getResourceBundle(context, "msg");
        String msg = bundle.getString(key);        
        
        for (int i = 0 ; i < values.length ; i++) {
            msg = msg.replace("{" + (i) + "}", values[i]);
        }
        
        return msg;
    }
    
    public String retrieveUserLogin() {
		/*
		 * return ((HttpServletRequest)FacesContext.getCurrentInstance()
		 * .getExternalContext().getRequest()).getRemoteUser();
		 */
    	FacesContext fc = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) fc.getExternalContext().getSession(
					true);
		if(session != null && (session.getAttribute(Constants.SESSION_NIK) != null)){
			return  (String)session.getAttribute(Constants.SESSION_NIK);
		}else {
			return null;
		}
    	 
    }
    
  
    
    public String retrieveCorporatePortalLink() {
		/*
		 * return ((HttpServletRequest)FacesContext.getCurrentInstance()
		 * .getExternalContext().getRequest()).getRemoteUser();
		 */
    	FacesContext fc = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) fc.getExternalContext().getSession(
					true);
		if(session != null && (session.getAttribute(Constants.SESSION_LINK_CORPORATE_PORTAL) != null)){
			return  (String)session.getAttribute(Constants.SESSION_LINK_CORPORATE_PORTAL);
		}else {
			return null;
		}
    	 
    }
    
    public String retrieveElearningLink() {
		/*
		 * return ((HttpServletRequest)FacesContext.getCurrentInstance()
		 * .getExternalContext().getRequest()).getRemoteUser();
		 */
    	FacesContext fc = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) fc.getExternalContext().getSession(
					true);
		if(session != null && (session.getAttribute(Constants.SESSION_LINK_ELEARNING) != null)){
			return  (String)session.getAttribute(Constants.SESSION_LINK_ELEARNING);
		}else {
			return null;
		}
    	 
    }
    
    public User getUserLogin() {
		
    	FacesContext fc = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) fc.getExternalContext().getSession(
					true);
		if(session != null && (session.getAttribute(Constants.SESSION_EMPLOYEE) != null)){
			return  (User)session.getAttribute(Constants.SESSION_EMPLOYEE);
		}else {
			return null;
		}
    	 
    }
    
    public void addFacesMsg(
            FacesMessage.Severity severity, String forComp, String msg, String detail) {
        FacesMessage message = new FacesMessage(
                                        severity,
                                        msg,
                                        detail);
        FacesContext.getCurrentInstance().addMessage(forComp, message);
        
    }
    
    public void redirectToExternal(String externalAppUrl) throws IOException {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        externalContext.redirect("http://" + 
                retrieveServerName() + ":" + 
                retrieveServerPort() + externalAppUrl);
        
    }
    
    public void redirect(final String url) throws IOException {
		/* FacesContext.getCurrentInstance().getExternalContext().redirect(url); */
    	String baseContextPath = FacesContext.getCurrentInstance()
				  .getExternalContext().getRequestContextPath();
				FacesContext.getCurrentInstance().getExternalContext().redirect(baseContextPath+url);
	}
    
    public void redirectScreen(final String relativeUrl) throws IOException {
		if (CommonConstants.DEPLOY_SERVER_HTTPS != null && CommonConstants.DEPLOY_SERVER_HTTPS.equals("Y")) {
			HttpServletRequest origRequest = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
			StringBuffer urlTemp = origRequest.getRequestURL();
			String urlContextTemp = origRequest.getContextPath();
			String urlClean = urlTemp.toString();
			String[] splitUrl = urlClean.split(urlContextTemp);
			String rcp = splitUrl[0];
			rcp = rcp.replace(CommonConstants.URL_HTTP, CommonConstants.URL_HTTPS);
			String finalUrl = rcp + origRequest.getContextPath() + relativeUrl;
			FacesContext.getCurrentInstance().getExternalContext().redirect(finalUrl);
		} else {
		FacesContext
				.getCurrentInstance()
				.getExternalContext()
				.redirect(
						FacesContext.getCurrentInstance().getExternalContext()
								.getRequestContextPath()
								+ relativeUrl);
		}
	}
    
    public Object getSessionAttribute(String attributeName) {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(true);
		if (session != null)
			return session.getAttribute(attributeName);
		else
			return null;
	}
    
    public void setSessionAttribute(String attributeName, Object attributeValue) {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(true);
		if (session != null)
			session.setAttribute(attributeName, attributeValue);
	}
    
    public void removeSessionAttribute(String attributeName) {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);
		if (session != null)
			session.removeAttribute(attributeName);
	}
    
    public void removeAllSessionAttribute() {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);
		if (session != null)
			session.invalidate();
	}
   
	/*
	 * @SuppressWarnings({ "unchecked", "rawtypes" }) public Object
	 * getManagedBean(final String expression, final Class clazz) { FacesContext
	 * context = FacesContext.getCurrentInstance(); return
	 * context.getApplication().evaluateExpressionGet(context, expression, clazz); }
	 */
    
    public void addErrMessage(String summary) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, null);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
    
    public void addWarnMessage(String summary) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, null);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
    
    public void addSuccessMsg(String summary) {
    	FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, null);
        FacesContext.getCurrentInstance().addMessage(null, message);
        
    }
    
    public void removeAllManagedBeans(final FacesScope facesScope) {
		try {
			Set<String> keySet = null;

			switch (facesScope) {
			case FLASH_SCOPE:
				keySet = FacesContext.getCurrentInstance().getExternalContext()
						.getFlash().keySet();
				break;
			case REQUEST_SCOPE:
				keySet = FacesContext.getCurrentInstance().getExternalContext()
						.getRequestMap().keySet();
				break;
			case VIEW_SCOPE:
				keySet = FacesContext.getCurrentInstance().getViewRoot()
						.getViewMap().keySet();
				break;
			case SESSION_SCOPE:
				keySet = FacesContext.getCurrentInstance().getExternalContext()
						.getSessionMap().keySet();
				break;
			case APPLICATION_SCOPE:
				keySet = FacesContext.getCurrentInstance().getExternalContext()
						.getApplicationMap().keySet();
				break;
			default:
				keySet = null;
				break;
			}

			if (keySet != null) {
				for (final String key : keySet) {
					if (!key.startsWith("SESSION"))
						this.removeManagedBean(key, facesScope);
				}
			}
		} catch (final Exception ex) {
			/*if (FacesUtil.LOG.isDebugEnabled()) {
				FacesUtil.LOG
						.debug("Error removing bean on FacesUtil, cause: "
								+ ex.getMessage());
			}*/
		}
	}
    
    public boolean removeManagedBean(final String managedBeanName,
			final FacesScope facesScope) {
		boolean result = false;

		try {
			switch (facesScope) {
			case FLASH_SCOPE:
				if (FacesContext.getCurrentInstance().getExternalContext()
						.getFlash().remove(managedBeanName) != null) {
					result = true;
				}
				break;
			case REQUEST_SCOPE:
				if (FacesContext.getCurrentInstance().getExternalContext()
						.getRequestMap().remove(managedBeanName) != null) {
					result = true;
				}
				break;
			case VIEW_SCOPE:
				if (FacesContext.getCurrentInstance().getViewRoot()
						.getViewMap().remove(managedBeanName) != null) {
					result = true;
				}
				break;
			case SESSION_SCOPE:
				if (FacesContext.getCurrentInstance().getExternalContext()
						.getSessionMap().remove(managedBeanName) != null) {
					result = true;
				}
				break;
			case APPLICATION_SCOPE:
				if (FacesContext.getCurrentInstance().getExternalContext()
						.getApplicationMap().remove(managedBeanName) != null) {
					result = true;
				}
				break;
			default:
				result = false;
				break;
			}
		} catch (final Exception ex) {
			/*if (FacesUtils.LOG.isDebugEnabled()) {
				FacesUtils.LOG
						.debug("Error removing bean on FacesUtil, cause: "
								+ ex.getMessage());
			}*/
		}

		return result;
	}

	public boolean removeManagedBean(final String managedBeanName) {
		boolean result = false;

		for (final FacesScope fs : FacesScope.values()) {
			if (this.removeManagedBean(managedBeanName, fs)) {
				result = true;
				break;
			}
		}

		return result;
	}
	
	public String getResource(String resourceKey) {
		return retrieveMessage(resourceKey);
	}
	
	public String getContextRelativePageLocation(final String location) {
		return FacesContext.getCurrentInstance().getExternalContext()
				.getRequestContextPath()
				+ location;
	}
	
	public static String convertClobToString(Clob clob) {
		
	    StringBuffer buffer = new StringBuffer();
		try{
	    Reader r = clob.getCharacterStream();
        int ch;
        while ((ch = r.read())!=-1) {
           buffer.append(""+(char)ch);
        }
		}catch(Exception e){
			
		}
        return buffer.toString();
	}
	
	public Flash getFlash() {
		return FacesContext.getCurrentInstance().getExternalContext().getFlash();
	}
	
	public HttpServletResponse getResponse() {
		return  (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

	}
}
