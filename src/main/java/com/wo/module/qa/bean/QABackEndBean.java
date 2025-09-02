package com.wo.module.qa.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.event.ActionEvent;

import org.apache.log4j.Logger;

import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.paging.DBLazyDataModel;
import com.wo.module.common.paging.DefaultSearchObject;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.qa.constant.QAConstants;
import com.wo.module.qa.model.QA;
import com.wo.module.qa.service.QABackEndService;

public class QABackEndBean extends CommonBean implements Serializable{

	private static final long serialVersionUID = 3362811083429320200L;
	private static Logger logger = Logger.getLogger(QABackEndBean.class);
	private static final String NAVIGATE_EDIT = QAConstants.NAVIGATE_SEARCH_BACK_END_EDIT;
	
	private QABackEndService qaBackEndService;
	
	private String searchTicketNo;
	private String searchQuestion;
	private String searchQtitle;
	
	private DBLazyDataModel<QA> tableModel;
	
	private FacesUtil facesUtil;
	
	@PostConstruct
	public void init() {
		super.init();
		paging = Constants.DEFAULT_PAGING_NUMBER;
		tableModel = new DBLazyDataModel<QA>(qaBackEndService, paging);
	}
	
	@SuppressWarnings("rawtypes")
	public void search(ActionEvent actionEvent) {
		List<SearchObject> searchCriteria = new ArrayList<SearchObject>();
		
		if (searchTicketNo != null && !searchTicketNo.isEmpty()) {
			searchCriteria.add(new DefaultSearchObject(QAConstants.WHERE_TICKET_NO, searchTicketNo));
		}
		if (searchQuestion != null && !searchQuestion.isEmpty()) {
			searchCriteria.add(new DefaultSearchObject(QAConstants.WHERE_QUESTION, searchQuestion));
		}
		if (searchQtitle != null && !searchQtitle.isEmpty()) {
			searchCriteria.add(new DefaultSearchObject(QAConstants.WHERE_Q_TITLE, searchQtitle));
		}
		
		tableModel.setSearchCriteria(searchCriteria);
		
	}
	
	public void reset(ActionEvent actionEvent) {
		searchQtitle = "";
		searchQuestion = "";
		searchTicketNo = "";
		
		search(actionEvent);
	}

	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		QABackEndBean.logger = logger;
	}

	public static String getNavigateEdit() {
		return NAVIGATE_EDIT;
	}

	public QABackEndService getQaBackEndService() {
		return qaBackEndService;
	}

	public void setQaBackEndService(QABackEndService qaBackEndService) {
		this.qaBackEndService = qaBackEndService;
	}

	public String getSearchTicketNo() {
		return searchTicketNo;
	}

	public void setSearchTicketNo(String searchTicketNo) {
		this.searchTicketNo = searchTicketNo;
	}

	public String getSearchQuestion() {
		return searchQuestion;
	}

	public void setSearchQuestion(String searchQuestion) {
		this.searchQuestion = searchQuestion;
	}

	public String getSearchQtitle() {
		return searchQtitle;
	}

	public void setSearchQtitle(String searchQtitle) {
		this.searchQtitle = searchQtitle;
	}

	public DBLazyDataModel<QA> getTableModel() {
		return tableModel;
	}

	public void setTableModel(DBLazyDataModel<QA> tableModel) {
		this.tableModel = tableModel;
	}

	public FacesUtil getFacesUtil() {
		return facesUtil;
	}

	public void setFacesUtil(FacesUtil facesUtil) {
		this.facesUtil = facesUtil;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	
}
