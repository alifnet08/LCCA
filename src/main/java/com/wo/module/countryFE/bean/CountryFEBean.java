package com.wo.module.countryFE.bean;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;
import org.primefaces.PrimeFaces;
import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.wo.module.common.bean.CommonPagingFEBean;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.countryFE.service.CountryFEService;
import com.wo.module.countryFE.vo.CountryFEVO;

public class CountryFEBean extends CommonPagingFEBean<CountryFEVO> implements Serializable {

	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(CountryFEBean.class);
	

	@Autowired
	@Qualifier("countryFEService")
	private CountryFEService countryFEService;

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
		setPageSize(10);
		super.init();
		if(facesUtil.getSessionAttribute("FIRST_COUNTRY_FE") != null) {
			Object dataInt = facesUtil.getSessionAttribute("FIRST_COUNTRY_FE");
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
		
		if (facesUtil.getSessionAttribute("FIRST_COUNTRY_FE") != null) {
			facesUtil.removeSessionAttribute("FIRST_COUNTRY_FE");
		}
	}
	
	@SuppressWarnings("rawtypes")
	public List<CountryFEVO> searchData( List<? extends SearchObject> searchCriteria, int first, int pageSize, String sortField,
			SortOrder sortOrder) throws Exception {
		return countryFEService.searchData(getSearchCriteria(), getFirst(), getPageSize(), null, null);
	}
	
	@SuppressWarnings("rawtypes")
	public Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception{
		return countryFEService.searchCountData(getSearchCriteria());
	}


	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		CountryFEBean.logger = logger;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public CountryFEService getCountryFEService() {
		return countryFEService;
	}

	public void setCountryFEService(CountryFEService countryFEService) {
		this.countryFEService = countryFEService;
	}
}