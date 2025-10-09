package com.wo.module.tmpFaqApproval.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;

import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.CommonConstants;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.paging.DBLazyDataModel;
import com.wo.module.common.paging.DefaultSearchObject;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.faq.model.TmpFaq;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.tmpFaqApproval.constant.TmpFaqApprovalConstant;
import com.wo.module.tmpFaqApproval.service.TmpFaqApprovalService;
import com.wo.module.user.model.User;

public class TmpFaqApprovalBean extends CommonBean implements Serializable{

	private static final long serialVersionUID = -3525037591801993449L;
	private static final Logger logger = Logger.getLogger(TmpFaqApprovalBean.class);
	private static final String NAVIGATE_EDIT = TmpFaqApprovalConstant.TMP_FAQ_APPROVAL_EDIT;
	
	private TmpFaqApprovalService tmpFaqApprovalService;
	
	private DBLazyDataModel<TmpFaq> tableModel;
	
	private FacesUtil facesUtil;
	
	@PostConstruct
	public void init() {
		super.init();
		paging = Constants.DEFAULT_PAGING_NUMBER;
		tableModel = new DBLazyDataModel<TmpFaq>(tmpFaqApprovalService, paging);
		
		User userLogin = facesUtil.getUserLogin();
		List<SearchObject> searchCriteria = new ArrayList<SearchObject>();
		if (userLogin != null) {
			searchCriteria.add(new DefaultSearchObject(CommonConstants.SEARCH_BY_USER_LOGIN, userLogin.getUserId()));
		}
		tableModel.setSearchCriteria(searchCriteria);
	}

	public TmpFaqApprovalService getTmpFaqApprovalService() {
		return tmpFaqApprovalService;
	}

	public void setTmpFaqApprovalService(TmpFaqApprovalService tmpFaqApprovalService) {
		this.tmpFaqApprovalService = tmpFaqApprovalService;
	}

	public DBLazyDataModel<TmpFaq> getTableModel() {
		return tableModel;
	}

	public void setTableModel(DBLazyDataModel<TmpFaq> tableModel) {
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

	public static Logger getLogger() {
		return logger;
	}

	public static String getNavigateEdit() {
		return NAVIGATE_EDIT;
	}
	
}
