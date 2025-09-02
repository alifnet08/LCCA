package com.wo.module.menuSession.bean;

import java.io.Serializable;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.wo.module.common.constant.Constants;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.menuSession.service.MenuSessionService;
import com.wo.module.menuSession.model.MenuSession;

public class MenuSessionBean implements Serializable {
 
    
	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(MenuSessionBean.class);
	
	public List<MenuSession> menuList;
	
	public FacesUtil facesUtil;
	
	public MenuSessionService menuSessionService;
	
	public void initMenus() {
		 
		
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

	public List<MenuSession> getMenuList() {
		if(menuList == null) {
		FacesContext fc = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) fc.getExternalContext().getSession(
					true);
		if(session != null && (session.getAttribute(Constants.SESSION_NIK) != null)){
			String userLogin = (String)session.getAttribute(Constants.SESSION_NIK);
			menuList = menuSessionService.retrieveAllMenuAllowed(userLogin);
		}
		
		}
		return menuList;
	}

	public void setMenuList(List<MenuSession> menuList) {
		this.menuList = menuList;
	}

	public MenuSessionService getMenuSessionService() {
		return menuSessionService;
	}

	public void setMenuSessionService(MenuSessionService menuSessionService) {
		this.menuSessionService = menuSessionService;
	}

	
	
	
   
}