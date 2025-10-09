package com.wo.module.staticPage.bean;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;

import com.wo.module.common.bean.CommonBean;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.staticPage.model.StaticPage;
import com.wo.module.staticPage.service.StaticPageService;

public class StaticPageBantuanBean extends CommonBean  implements Serializable {

	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(StaticPageBean.class);

	private String title;
	
	private String content;
	
	private String category;

	private StaticPageService staticPageService;
	
	private FacesUtil facesUtil;

	public void addMessage(String summary) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, null);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public void addErrMessage(String summary) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, null);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	@PostConstruct
	public void init() {
		super.init();
		
		StaticPage staticPage = staticPageService.getStaticPageByCategory("HELP");
		
		if(staticPage != null){
			this.content = staticPage.getContentIn();
			this.title = staticPage.getTitleIn();
		}
		
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public StaticPageService getStaticPageService() {
		return staticPageService;
	}

	public void setStaticPageService(StaticPageService staticPageService) {
		this.staticPageService = staticPageService;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public FacesUtil getFacesUtil() {
		return facesUtil;
	}

	public void setFacesUtil(FacesUtil facesUtil) {
		this.facesUtil = facesUtil;
	}
	
	
	
	

	

	
	

}