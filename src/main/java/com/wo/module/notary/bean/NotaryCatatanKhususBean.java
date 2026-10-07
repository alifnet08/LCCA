package com.wo.module.notary.bean;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.event.ActionEvent;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;

import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.paging.DBLazyDataModel;
import com.wo.module.common.paging.DefaultSearchObject;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.common.util.FileUtil;
import com.wo.module.common.utility.CallApiManager;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.notary.constant.NotaryConstants;
import com.wo.module.notary.model.Notary;
import com.wo.module.notary.model.NotaryHistory;
import com.wo.module.notary.service.NotaryService;
import com.wo.module.responsibility.model.Responsibility;
import com.wo.module.responsibility.service.ResponsibilityService;
import com.wo.module.user.model.User;
import com.wo.module.user.service.UserService;

public class NotaryCatatanKhususBean extends CommonBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private String area;
	private String notaryName;
	private String areaCode;
	private String categoryName;
	private int paging;
	private NotaryService notaryService;
	private UserService userService;
	private ResponsibilityService responsibilityService;
	public FacesUtil facesUtil;
	private DBLazyDataModel<Notary> tableModel;
	private String currentResponsibilityName;
	private Long selectedNotaryId;
	private Notary selectedNotary;
	private String targetStatus;
	private String catatanKhususInput;
	private String errorMessage;
	private String pendingFileId;
	private String pendingFileName;
	private boolean openDialogOnLoad;
	private FileUtil fileUtil;

	@PostConstruct
	public void init() {
		super.init();
		paging = Constants.DEFAULT_PAGING_NUMBER;
		tableModel = new DBLazyDataModel<Notary>(notaryService, paging);
		fileUtil = FileUtil.getInstance();
		resolveCurrentResponsibility();
		String editId = facesUtil != null ? facesUtil.retrieveRequestParam("id") : null;
		if (StringUtils.isNotBlank(editId)) {
			prepareResubmit(editId);
		}
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

	public boolean isLegal() {
		return StringUtils.equalsIgnoreCase(NotaryConstants.RESPONSIBILITY_LEGAL, currentResponsibilityName);
	}

	public boolean isCduMaker() {
		return StringUtils.equalsIgnoreCase(NotaryConstants.RESPONSIBILITY_CDU_MAKER, currentResponsibilityName);
	}

	public boolean isCanChangeStatus() {
		return isLegal() || isCduMaker();
	}

	public void search(ActionEvent actionEvent) {
		List<SearchObject> searchCriteria = new ArrayList<SearchObject>();
		if (StringUtils.isNotBlank(categoryName)) {
			searchCriteria.add(new DefaultSearchObject(NotaryConstants.SEARCH_BY_CATEGORY, categoryName));
		}
		if (StringUtils.isNotBlank(notaryName)) {
			searchCriteria.add(new DefaultSearchObject(NotaryConstants.SEARCH_BY_NOTARY_NAME, notaryName));
		}
		if (StringUtils.isNotBlank(area)) {
			searchCriteria.add(new DefaultSearchObject(NotaryConstants.SEARCH_BY_AREA, area));
		}
		if (StringUtils.isNotBlank(areaCode)) {
			searchCriteria.add(new DefaultSearchObject(NotaryConstants.SEARCH_BY_AREA_CODE, areaCode));
		}
		searchCriteria.add(new DefaultSearchObject(NotaryConstants.SEARCH_BY_STATUS, NotaryConstants.STATUS_COMPLETE));
		tableModel.setSearchCriteria(searchCriteria);
	}

	public void reset(ActionEvent actionEvent) {
		area = "";
		notaryName = "";
		areaCode = "";
		categoryName = "";
		search(actionEvent);
	}

	public void openActive(ActionEvent event) {
		openDialog(event, NotaryConstants.LISTING_STATUS_ACTIVE);
	}

	public void openFreeze(ActionEvent event) {
		openDialog(event, NotaryConstants.LISTING_STATUS_FREEZE);
	}

	public void openDelisting(ActionEvent event) {
		openDialog(event, NotaryConstants.LISTING_STATUS_DELISTING);
	}

	private void openDialog(ActionEvent event, String target) {
		errorMessage = null;
		pendingFileId = null;
		pendingFileName = null;
		catatanKhususInput = null;
		targetStatus = target;
		selectedNotary = null;
		selectedNotaryId = null;
		Object rawId = event.getComponent().getAttributes().get("notaryId");
		if (rawId == null) {
			errorMessage = "Notaris tidak ditemukan.";
			return;
		}
		Long id = Long.valueOf(rawId.toString());
		Notary entity = notaryService.findById(id);
		if (entity == null) {
			errorMessage = "Notaris tidak ditemukan.";
			return;
		}
		selectedNotaryId = id;
		selectedNotary = entity;
		if (!isTransitionAllowed(entity.getListingStatusLabel(), target)) {
			errorMessage = "Perubahan status tidak diizinkan.";
			selectedNotaryId = null;
		}
	}

	private void prepareResubmit(String editId) {
		try {
			Notary entity = notaryService.findById(Long.valueOf(editId));
			if (entity == null || !StringUtils.equals(NotaryConstants.JENIS_PENGAJUAN_CATATAN_KHUSUS, entity.getJenisPengajuan())
					|| !StringUtils.equals(NotaryConstants.STATUS_REVISION, entity.getStatus())) {
				return;
			}
			if (!canResubmit(entity)) {
				return;
			}
			selectedNotary = entity;
			selectedNotaryId = entity.getNotaryId();
			targetStatus = entity.getPendingListingStatus();
			catatanKhususInput = entity.getCatatanKhusus();
			pendingFileName = entity.getCatatanKhususFileName();
			openDialogOnLoad = StringUtils.isNotBlank(targetStatus);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void handleUpload(FileUploadEvent event) {
		errorMessage = null;
		if (event == null || event.getFile() == null) {
			return;
		}
		if (event.getFile().getSize() > NotaryConstants.CATATAN_KHUSUS_MAX_FILE_BYTES) {
			errorMessage = "Ukuran lampiran maksimal 2MB.";
			return;
		}
		try {
			if (fileUtil == null) {
				fileUtil = FileUtil.getInstance();
			}
			pendingFileId = CallApiManager.callUploadAPI(event.getFile(), Constants.ARTICLE, parameterDetailService,
					false, fileUtil);
			pendingFileName = event.getFile().getFileName();
		} catch (Exception e) {
			errorMessage = e.getMessage();
		}
	}

	public void submit() {
		errorMessage = null;
		try {
			if (!isCanChangeStatus()) {
				errorMessage = "Hanya Legal atau CDU Maker yang dapat mengubah status.";
				return;
			}
			if (StringUtils.isBlank(catatanKhususInput)) {
				errorMessage = "Catatan Khusus wajib diisi.";
				return;
			}
			if (selectedNotaryId == null || StringUtils.isBlank(targetStatus)) {
				errorMessage = "Notaris tidak ditemukan.";
				return;
			}
			Notary entity = notaryService.findById(selectedNotaryId);
			if (entity == null) {
				errorMessage = "Notaris tidak ditemukan.";
				return;
			}
			boolean resubmit = StringUtils.equals(NotaryConstants.STATUS_REVISION, entity.getStatus())
					&& StringUtils.equals(NotaryConstants.JENIS_PENGAJUAN_CATATAN_KHUSUS, entity.getJenisPengajuan());
			boolean revisionForLegal = isRevisionForLegal(entity);
			if (resubmit) {
				if (!canResubmit(entity)) {
					errorMessage = "Pengajuan revisi ini tidak dapat disubmit.";
					return;
				}
				targetStatus = entity.getPendingListingStatus();
			} else if (!StringUtils.equals(NotaryConstants.STATUS_COMPLETE, entity.getStatus())) {
				errorMessage = "Notaris sedang dalam proses pengajuan.";
				return;
			}
			if (!isTransitionAllowed(entity.getListingStatusLabel(), targetStatus)) {
				errorMessage = "Perubahan status tidak diizinkan.";
				return;
			}
			String login = facesUtil.retrieveUserLogin();
			if (StringUtils.isNotBlank(pendingFileId)) {
				entity.setCatatanKhususFileId(pendingFileId);
				entity.setCatatanKhususFileName(pendingFileName);
			}
			entity.setCatatanKhusus(catatanKhususInput.trim());
			entity.setPendingListingStatus(targetStatus);
			if (StringUtils.isBlank(entity.getListingStatus())) {
				entity.setListingStatus(NotaryConstants.LISTING_STATUS_ACTIVE);
			}
			if (!resubmit && !StringUtils.equals(NotaryConstants.JENIS_PENGAJUAN_CATATAN_KHUSUS, entity.getJenisPengajuan())) {
				entity.setJenisBeforeCatatan(entity.getJenisPengajuan());
				entity.setJenisPengajuan(NotaryConstants.JENIS_PENGAJUAN_CATATAN_KHUSUS);
				entity.setNotaryNo(notaryService.generateNoPengajuan(NotaryConstants.PREFIX_NO_PENGAJUAN_CATATAN_KHUSUS));
				entity.setTanggalPengajuan(new Date());
			}
			if (!resubmit) {
				entity.setUserPengaju(login);
				entity.setCatatanKhususRole(isLegal() ? NotaryConstants.CATATAN_ROLE_LEGAL : NotaryConstants.CATATAN_ROLE_MAKER);
			}
			String nextRole;
			String historyStatus;
			boolean sendToSpv = isLegal() || (resubmit && revisionForLegal);
			if (sendToSpv) {
				entity.setStatus(NotaryConstants.STATUS_WAITING_APPROVAL_SPV_LEGAL);
				nextRole = NotaryConstants.RESPONSIBILITY_SPV_LEGAL;
				historyStatus = "Submit Catatan Khusus by Legal";
			} else {
				entity.setStatus(NotaryConstants.STATUS_WAITING_APPROVAL_CDU_CHECKER);
				nextRole = NotaryConstants.RESPONSIBILITY_CDU_CHECKER;
				historyStatus = "Submit Catatan Khusus by CDU Maker";
			}
			entity.setLastUpdateBy(login);
			entity.setLastUpdateDate(new Timestamp(new Date().getTime()));
			notaryService.update(entity);
			notaryService.saveHistory(entity, historyStatus, "Menjadi " + targetStatus + ". " + entity.getCatatanKhusus(),
					login);
			sendNotification(nextRole, "Pengajuan Catatan Khusus",
					"Pengajuan Catatan Khusus untuk " + entity.getNotaryName() + " menjadi " + targetStatus + ".");
			PrimeFaces.current().ajax().addCallbackParam("catatanSaved", true);
			openDialogOnLoad = false;
			search(null);
		} catch (Exception e) {
			errorMessage = "Operation Failed : " + e.getMessage();
		}
	}

	private boolean canResubmit(Notary entity) {
		if (entity == null || !StringUtils.equals(NotaryConstants.STATUS_REVISION, entity.getStatus())) {
			return false;
		}
		if (isRevisionForLegal(entity)) {
			return isLegal();
		}
		if (!isCduMaker()) {
			return false;
		}
		String login = facesUtil != null ? facesUtil.retrieveUserLogin() : null;
		return StringUtils.isNotBlank(login) && StringUtils.equalsIgnoreCase(login, entity.getUserPengaju());
	}

	private boolean isRevisionForLegal(Notary entity) {
		if (entity == null || entity.getNotaryId() == null
				|| !StringUtils.equals(NotaryConstants.STATUS_REVISION, entity.getStatus())) {
			return false;
		}
		List<NotaryHistory> historyList = notaryService.getHistoryByNotaryId(entity.getNotaryId());
		if (historyList == null || historyList.isEmpty() || historyList.get(0) == null) {
			return false;
		}
		return StringUtils.equals(NotaryConstants.HISTORY_REVISION_SPV_TO_LEGAL, historyList.get(0).getStatus());
	}

	private boolean isTransitionAllowed(String currentStatus, String target) {
		String current = StringUtils.defaultIfBlank(currentStatus, NotaryConstants.LISTING_STATUS_ACTIVE);
		if (StringUtils.equals(NotaryConstants.LISTING_STATUS_ACTIVE, target)) {
			return StringUtils.equals(NotaryConstants.LISTING_STATUS_FREEZE, current);
		}
		if (StringUtils.equals(NotaryConstants.LISTING_STATUS_FREEZE, target)) {
			return StringUtils.equals(NotaryConstants.LISTING_STATUS_ACTIVE, current);
		}
		if (StringUtils.equals(NotaryConstants.LISTING_STATUS_DELISTING, target)) {
			return StringUtils.equals(NotaryConstants.LISTING_STATUS_ACTIVE, current)
					|| StringUtils.equals(NotaryConstants.LISTING_STATUS_FREEZE, current);
		}
		return false;
	}

	private void sendNotification(String responsibilityName, String subject, String content) {
		try {
			if (userService == null || responsibilityService == null || parameterDetailService == null) {
				return;
			}
			List<Responsibility> responsibilityList = responsibilityService.getAllResponsibility();
			Long responsibilityId = null;
			if (responsibilityList != null) {
				for (Responsibility responsibility : responsibilityList) {
					if (responsibility != null
							&& StringUtils.equalsIgnoreCase(responsibilityName, responsibility.getName())) {
						responsibilityId = responsibility.getResponsibilityId();
						break;
					}
				}
			}
			if (responsibilityId == null) {
				return;
			}
			List<User> userList = userService.getAllUser();
			StringBuilder to = new StringBuilder();
			if (userList != null) {
				for (User user : userList) {
					if (user == null || user.getResponsibilityId() == null || StringUtils.isBlank(user.getEmail())) {
						continue;
					}
					if (!responsibilityId.equals(user.getResponsibilityId())) {
						continue;
					}
					if (StringUtils.isNotBlank(user.getEnabledFlag())
							&& !StringUtils.equalsIgnoreCase(Constants.CONSTANT_YES, user.getEnabledFlag())) {
						continue;
					}
					if (to.length() > 0) {
						to.append(";");
					}
					to.append(user.getEmail());
				}
			}
			if (to.length() == 0) {
				return;
			}
			CallApiManager.sendEmailAPI(to.toString(), "", subject, content, "NOTARY", "true", parameterDetailService);
		} catch (Exception e) {
			e.printStackTrace();
		}
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

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
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

	public Notary getSelectedNotary() {
		return selectedNotary;
	}

	public void setSelectedNotary(Notary selectedNotary) {
		this.selectedNotary = selectedNotary;
	}

	public String getTargetStatus() {
		return targetStatus;
	}

	public void setTargetStatus(String targetStatus) {
		this.targetStatus = targetStatus;
	}

	public String getCatatanKhususInput() {
		return catatanKhususInput;
	}

	public void setCatatanKhususInput(String catatanKhususInput) {
		this.catatanKhususInput = catatanKhususInput;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getPendingFileName() {
		return pendingFileName;
	}

	public void setPendingFileName(String pendingFileName) {
		this.pendingFileName = pendingFileName;
	}

	public boolean isOpenDialogOnLoad() {
		return openDialogOnLoad;
	}

	public void setOpenDialogOnLoad(boolean openDialogOnLoad) {
		this.openDialogOnLoad = openDialogOnLoad;
	}

}
