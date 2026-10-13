package com.wo.module.notary.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.wo.module.notary.model.NotaryHistory;
import com.wo.module.notary.service.NotaryService;
import com.wo.module.responsibility.model.Responsibility;
import com.wo.module.responsibility.service.ResponsibilityService;
import com.wo.module.user.model.User;
import com.wo.module.user.service.UserService;

public class NotaryTaskBean extends CommonBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private String area;
	private String notaryName;
	private int paging;
	private NotaryService notaryService;
	private UserService userService;
	private ResponsibilityService responsibilityService;
	public FacesUtil facesUtil;
	private DBLazyDataModel<Notary> tableModel;
	private List<NotaryHistory> historyList;
	private Notary selectedHistoryNotary;
	private String currentResponsibilityName;

	public void addErrMessage(String summary) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, null);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	@PostConstruct
	public void init() {
		super.init();
		paging = Constants.DEFAULT_PAGING_NUMBER;
		tableModel = new DBLazyDataModel<Notary>(notaryService, paging);
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

	private String inboxStatus() {
		if (StringUtils.equalsIgnoreCase(NotaryConstants.RESPONSIBILITY_CDU_CHECKER, currentResponsibilityName)) {
			return NotaryConstants.STATUS_WAITING_APPROVAL_CDU_CHECKER;
		}
		if (StringUtils.equalsIgnoreCase(NotaryConstants.RESPONSIBILITY_LEGAL, currentResponsibilityName)) {
			return NotaryConstants.STATUS_WAITING_APPROVAL_LEGAL;
		}
		if (StringUtils.equalsIgnoreCase(NotaryConstants.RESPONSIBILITY_SPV_LEGAL, currentResponsibilityName)) {
			return NotaryConstants.STATUS_WAITING_APPROVAL_SPV_LEGAL;
		}
		if (StringUtils.equalsIgnoreCase(NotaryConstants.RESPONSIBILITY_CDU_MAKER, currentResponsibilityName)) {
			return NotaryConstants.STATUS_REVISION;
		}
		return null;
	}

	public boolean isCduMaker() {
		return StringUtils.equalsIgnoreCase(NotaryConstants.RESPONSIBILITY_CDU_MAKER, currentResponsibilityName);
	}

	public boolean isLegal() {
		return StringUtils.equalsIgnoreCase(NotaryConstants.RESPONSIBILITY_LEGAL, currentResponsibilityName);
	}

	public void search(ActionEvent actionEvent) {
		List<SearchObject> searchCriteria = new ArrayList<SearchObject>();
		if (StringUtils.isNotBlank(area)) {
			searchCriteria.add(new DefaultSearchObject(NotaryConstants.SEARCH_BY_AREA, area));
		}
		if (StringUtils.isNotBlank(notaryName)) {
			searchCriteria.add(new DefaultSearchObject(NotaryConstants.SEARCH_BY_NOTARY_NAME, notaryName));
		}
		if (isCduMaker()) {
			searchCriteria.add(new DefaultSearchObject(NotaryConstants.SEARCH_BY_MAKER_TASK, "Y"));
			String login = facesUtil != null ? facesUtil.retrieveUserLogin() : null;
			if (StringUtils.isBlank(login)) {
				login = "__NO_USER__";
			}
			searchCriteria.add(new DefaultSearchObject(NotaryConstants.SEARCH_BY_USER_PENGAJU, login));
		} else if (isLegal()) {
			searchCriteria.add(new DefaultSearchObject(NotaryConstants.SEARCH_BY_LEGAL_TASK, "Y"));
		} else {
			String inbox = inboxStatus();
			if (StringUtils.isNotBlank(inbox)) {
				searchCriteria.add(new DefaultSearchObject(NotaryConstants.SEARCH_BY_INBOX_OR_REJECTED, inbox));
			} else {
				searchCriteria.add(new DefaultSearchObject(NotaryConstants.SEARCH_BY_STATUS, NotaryConstants.STATUS_REJECTED));
			}
		}
		tableModel.setSearchCriteria(searchCriteria);
	}

	public void reset(ActionEvent actionEvent) {
		area = "";
		notaryName = "";
		search(actionEvent);
	}

	public String openTask() {
		return NotaryConstants.NAVIGATE_TASK_EDIT;
	}

	public String openMakerSubmit() {
		if (facesUtil != null) {
			facesUtil.setSessionAttribute(NotaryConstants.SESSION_JENIS_PENGAJUAN, null);
			facesUtil.setSessionAttribute(NotaryConstants.SESSION_FROM_TASK, "Y");
		}
		return NotaryConstants.NAVIGATE_EDIT;
	}

	public String openMakerView() {
		if (facesUtil != null) {
			facesUtil.setSessionAttribute(NotaryConstants.SESSION_JENIS_PENGAJUAN, null);
			facesUtil.setSessionAttribute(NotaryConstants.SESSION_FROM_TASK, "Y");
		}
		return NotaryConstants.NAVIGATE_EDIT;
	}

	public String openCatatanKhusus() {
		if (facesUtil != null) {
			facesUtil.setSessionAttribute(NotaryConstants.SESSION_JENIS_PENGAJUAN, null);
			facesUtil.setSessionAttribute(NotaryConstants.SESSION_FROM_TASK, "Y");
		}
		return NotaryConstants.NAVIGATE_CATATAN_KHUSUS;
	}

	public void openHistory(Notary notary) {
		selectedHistoryNotary = notary;
		historyList = new ArrayList<NotaryHistory>();
		if (notary != null && notary.getNotaryId() != null) {
			historyList = notaryService.getHistoryByNotaryId(notary.getNotaryId());
		}
		Map<String, String> actorCache = new HashMap<String, String>();
		if (historyList == null) {
			historyList = new ArrayList<NotaryHistory>();
			return;
		}
		for (int i = 0; i < historyList.size(); i++) {
			NotaryHistory hist = historyList.get(i);
			if (hist == null || !isDecisionHistory(hist.getStatus())) {
				continue;
			}
			hist.setActorLabel(resolveActorLabel(hist.getCreatedBy(), actorCache));
		}
	}

	private boolean isDecisionHistory(String status) {
		if (StringUtils.isBlank(status)) {
			return false;
		}
		String normalized = status.toLowerCase();
		return normalized.contains("approve") || normalized.contains("revision") || normalized.contains("reject");
	}

	private String resolveActorLabel(String nik, Map<String, String> actorCache) {
		if (StringUtils.isBlank(nik)) {
			return "";
		}
		if (actorCache.containsKey(nik)) {
			return actorCache.get(nik);
		}
		String label = nik;
		try {
			if (userService != null) {
				User user = userService.getUserByNik(nik);
				if (user != null && StringUtils.isNotBlank(user.getName())) {
					label = nik + "-" + user.getName();
				}
			}
		} catch (Exception e) {
			label = nik;
		}
		actorCache.put(nik, label);
		return label;
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

	public int getPaging() {
		return paging;
	}

	public void setPaging(int paging) {
		this.paging = paging;
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

	public DBLazyDataModel<Notary> getTableModel() {
		return tableModel;
	}

	public void setTableModel(DBLazyDataModel<Notary> tableModel) {
		this.tableModel = tableModel;
	}

	public List<NotaryHistory> getHistoryList() {
		return historyList;
	}

	public void setHistoryList(List<NotaryHistory> historyList) {
		this.historyList = historyList;
	}

	public Notary getSelectedHistoryNotary() {
		return selectedHistoryNotary;
	}

	public void setSelectedHistoryNotary(Notary selectedHistoryNotary) {
		this.selectedHistoryNotary = selectedHistoryNotary;
	}

}
