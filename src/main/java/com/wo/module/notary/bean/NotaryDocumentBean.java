package com.wo.module.notary.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.apache.commons.lang3.StringUtils;

import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.paging.DBLazyDataModel;
import com.wo.module.common.paging.DefaultSearchObject;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.notary.constant.NotaryConstants;
import com.wo.module.notary.model.Notary;
import com.wo.module.notary.model.NotaryDocument;
import com.wo.module.notary.service.NotaryService;
import com.wo.module.responsibility.model.Responsibility;
import com.wo.module.responsibility.service.ResponsibilityService;
import com.wo.module.user.model.User;
import com.wo.module.user.service.UserService;
import com.wo.module.common.utility.CallApiManager;

public class NotaryDocumentBean extends CommonBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private String area;
	private String notaryName;
	private String categoryName;
	private int paging;
	private NotaryService notaryService;
	private UserService userService;
	private ResponsibilityService responsibilityService;
	public FacesUtil facesUtil;
	private DBLazyDataModel<Notary> tableModel;
	private String currentResponsibilityName;
	private List<NotaryDocument> lampiranList;
	private Notary selectedNotary;

	public void addErrMessage(String summary) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, null);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	@PostConstruct
	public void init() {
		super.init();
		paging = Constants.DEFAULT_PAGING_NUMBER;
		tableModel = new DBLazyDataModel<Notary>(notaryService, paging);
		lampiranList = new ArrayList<NotaryDocument>();
		resolveCurrentResponsibility();
		search(null);
	}

	private void resolveCurrentResponsibility() {
		currentResponsibilityName = "";
		try {
			if (userService == null || responsibilityService == null || facesUtil == null) {
				return;
			}
			String nik = facesUtil.retrieveUserLogin();
			if (StringUtils.isBlank(nik)) {
				return;
			}
			User user = userService.getUserByNik(nik);
			if (user == null || user.getResponsibilityId() == null) {
				return;
			}
			Responsibility responsibility = responsibilityService.findById(user.getResponsibilityId());
			if (responsibility != null) {
				currentResponsibilityName = responsibility.getName();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean isLegalAccess() {
		return StringUtils.equalsIgnoreCase(NotaryConstants.RESPONSIBILITY_LEGAL, currentResponsibilityName)
				|| StringUtils.equalsIgnoreCase(NotaryConstants.RESPONSIBILITY_SPV_LEGAL, currentResponsibilityName);
	}

	public void search(ActionEvent actionEvent) {
		List<SearchObject> searchCriteria = new ArrayList<SearchObject>();
		if (!isLegalAccess()) {
			searchCriteria.add(new DefaultSearchObject(NotaryConstants.SEARCH_BY_STATUS, "__NO_ACCESS__"));
			tableModel.setSearchCriteria(searchCriteria);
			return;
		}
		if (StringUtils.isNotBlank(area)) {
			searchCriteria.add(new DefaultSearchObject(NotaryConstants.SEARCH_BY_AREA, area));
		}
		if (StringUtils.isNotBlank(notaryName)) {
			searchCriteria.add(new DefaultSearchObject(NotaryConstants.SEARCH_BY_NOTARY_NAME, notaryName));
		}
		if (StringUtils.isNotBlank(categoryName)) {
			searchCriteria.add(new DefaultSearchObject(NotaryConstants.SEARCH_BY_CATEGORY, categoryName));
		}
		searchCriteria.add(new DefaultSearchObject(NotaryConstants.SEARCH_BY_STATUS, NotaryConstants.STATUS_COMPLETE));
		tableModel.setSearchCriteria(searchCriteria);
	}

	public void openLampiran(Notary notary) {
		lampiranList = new ArrayList<NotaryDocument>();
		selectedNotary = notary;
		if (notary == null || notary.getNotaryId() == null || !isLegalAccess()) {
			return;
		}
		Notary loaded = notaryService.findById(notary.getNotaryId());
		if (loaded != null && loaded.getNotaryDocuments() != null) {
			lampiranList = loaded.getNotaryDocuments();
		}
	}

	public void downloadFile(String fileId, String fileName, byte[] content) throws Exception {
		CallApiManager.downloadFile(fileId, fileName, content, parameterDetailService);
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getNotaryName() {
		return notaryName;
	}

	public void setNotaryName(String notaryName) {
		this.notaryName = notaryName;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public int getPaging() {
		return paging;
	}

	public void setPaging(int paging) {
		this.paging = paging;
	}

	public DBLazyDataModel<Notary> getTableModel() {
		return tableModel;
	}

	public void setTableModel(DBLazyDataModel<Notary> tableModel) {
		this.tableModel = tableModel;
	}

	public List<NotaryDocument> getLampiranList() {
		return lampiranList;
	}

	public void setLampiranList(List<NotaryDocument> lampiranList) {
		this.lampiranList = lampiranList;
	}

	public Notary getSelectedNotary() {
		return selectedNotary;
	}

	public void setSelectedNotary(Notary selectedNotary) {
		this.selectedNotary = selectedNotary;
	}

	public NotaryService getNotaryService() {
		return notaryService;
	}

	public void setNotaryService(NotaryService notaryService) {
		this.notaryService = notaryService;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public ResponsibilityService getResponsibilityService() {
		return responsibilityService;
	}

	public void setResponsibilityService(ResponsibilityService responsibilityService) {
		this.responsibilityService = responsibilityService;
	}

	public FacesUtil getFacesUtil() {
		return facesUtil;
	}

	public void setFacesUtil(FacesUtil facesUtil) {
		this.facesUtil = facesUtil;
	}

}
