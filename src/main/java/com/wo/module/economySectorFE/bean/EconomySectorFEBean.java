package com.wo.module.economySectorFE.bean;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;
import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.wo.module.common.bean.CommonPagingFEBean;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.economySectorFE.service.EconomySectorFEService;
import com.wo.module.economySectorFE.vo.EconomySectorFEVO;

public class EconomySectorFEBean extends CommonPagingFEBean<EconomySectorFEVO> implements Serializable {

	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(EconomySectorFEBean.class);
	
	//private Integer pageSize;
	
	//@Autowired
	//@Qualifier("economySectorFEService")
	private EconomySectorFEService economySectorFEService;

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
		//CLIENT WANT FCC TO HAVE 10 ROW
		setPageSize(10);
		super.init();
		
		if(facesUtil.getSessionAttribute("FIRST_ECONOMY_SECTOR_FE") != null) {
			Object dataInt = facesUtil.getSessionAttribute("FIRST_ECONOMY_SECTOR_FE");
			setInitFirst((Integer) dataInt);
		}
		System.out.println(facesUtil.retrieveUserLogin());
		
		String searchValParam = facesUtil.retrieveRequestParam("SEARCH_VAL");
		
		if(searchValParam != null) {
			if(!searchValParam.equals("null") && !searchValParam.isEmpty()) {
				searchValParam.replace("+", " ");
				setSearchVal(searchValParam);
			}
		}
		
		searchData();
		
		if (facesUtil.getSessionAttribute("FIRST_ECONOMY_SECTOR_FE") != null) {
			facesUtil.removeSessionAttribute("FIRST_ECONOMY_SECTOR_FE");
		}
	}

	@SuppressWarnings("rawtypes")
	public List<EconomySectorFEVO> searchData( List<? extends SearchObject> searchCriteria, int first, int pageSize, String sortField,
			SortOrder sortOrder) throws Exception {
		return economySectorFEService.searchData(getSearchCriteria(), getFirst(), getPageSize(), null, null);
	}
	
	@SuppressWarnings("rawtypes")
	public Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception{
		return economySectorFEService.searchCountData(getSearchCriteria());
	}
	
	/*public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}*/

	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		EconomySectorFEBean.logger = logger;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public EconomySectorFEService getEconomySectorFEService() {
		return economySectorFEService;
	}

	public void setEconomySectorFEService(EconomySectorFEService economySectorFEService) {
		this.economySectorFEService = economySectorFEService;
	}
}