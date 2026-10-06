package com.wo.module.notary.bean;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.apache.commons.lang3.StringUtils;

import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.util.FileUtil;
import com.wo.module.common.utility.CallApiManager;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.notary.constant.NotaryConstants;
import com.wo.module.notary.model.Notary;
import com.wo.module.notary.model.NotaryDocument;
import com.wo.module.notary.service.NotaryService;
import com.wo.module.responsibility.model.Responsibility;
import com.wo.module.responsibility.service.ResponsibilityService;
import com.wo.module.user.model.User;
import com.wo.module.user.service.UserService;

public class NotaryTaskEditBean extends CommonBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Notary notary;
	private NotaryService notaryService;
	private UserService userService;
	private ResponsibilityService responsibilityService;
	public FacesUtil facesUtil;
	private FileUtil fileUtil;
	private String currentResponsibilityName;
	private String catatanRevisi;
	private String catatanRevisiError;
	private String revisiTarget;
	private List<String> selectedRevisiDocuments;
	private boolean showLampiranTab;

	@PostConstruct
	public void init() {
		super.init();
		fileUtil = FileUtil.getInstance();
		selectedRevisiDocuments = new ArrayList<String>();
		revisiTarget = NotaryConstants.REVISI_TARGET_MAKER;
		resolveCurrentResponsibility();
		String editId = facesUtil.retrieveRequestParam("id");
		if (StringUtils.isNotBlank(editId)) {
			notary = notaryService.findById(Long.parseLong(editId));
		}
	}

	private void resolveCurrentResponsibility() {
		currentResponsibilityName = "";
		try {
			String nik = facesUtil.retrieveUserLogin();
			if (StringUtils.isBlank(nik) || userService == null || responsibilityService == null) {
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

	public boolean isChecker() {
		return StringUtils.equalsIgnoreCase(NotaryConstants.RESPONSIBILITY_CDU_CHECKER, currentResponsibilityName);
	}

	public boolean isLegal() {
		return StringUtils.equalsIgnoreCase(NotaryConstants.RESPONSIBILITY_LEGAL, currentResponsibilityName);
	}

	public boolean isSpvLegal() {
		return StringUtils.equalsIgnoreCase(NotaryConstants.RESPONSIBILITY_SPV_LEGAL, currentResponsibilityName);
	}

	public boolean isActionEnabled() {
		if (notary == null || StringUtils.equals(NotaryConstants.STATUS_REJECTED, notary.getStatus())) {
			return false;
		}
		if (isChecker() && StringUtils.equals(NotaryConstants.STATUS_WAITING_APPROVAL_CDU_CHECKER, notary.getStatus())) {
			return true;
		}
		if (isLegal() && StringUtils.equals(NotaryConstants.STATUS_WAITING_APPROVAL_LEGAL, notary.getStatus())) {
			return true;
		}
		if (isSpvLegal() && StringUtils.equals(NotaryConstants.STATUS_WAITING_APPROVAL_SPV_LEGAL, notary.getStatus())) {
			return true;
		}
		return false;
	}

	public void approve() {
		processAction("APPROVE");
	}

	public void reject() {
		processAction("REJECT");
	}

	public void revisi() {
		processAction("REVISI");
	}

	private void processAction(String actionType) {
		catatanRevisiError = null;
		try {
			if (notary == null || !isActionEnabled()) {
				addErr("Pengajuan tidak dapat diproses.");
				return;
			}
			if ("REVISI".equals(actionType) && StringUtils.isBlank(catatanRevisi)) {
				catatanRevisiError = "Catatan revisi harus diisi.";
				addErr(catatanRevisiError);
				return;
			}
			if ("REJECT".equals(actionType) && StringUtils.isBlank(catatanRevisi)) {
				catatanRevisiError = "Catatan revisi harus diisi.";
				addErr(catatanRevisiError);
				return;
			}
			String userLogin = facesUtil.retrieveUserLogin();
			String historyStatus;
			String nextStatus;
			String emailResponsibility = null;
			if ("APPROVE".equals(actionType)) {
				if (isChecker()) {
					nextStatus = NotaryConstants.STATUS_WAITING_APPROVAL_LEGAL;
					historyStatus = "Approve by CDU Checker";
					emailResponsibility = NotaryConstants.RESPONSIBILITY_LEGAL;
				} else if (isLegal()) {
					nextStatus = NotaryConstants.STATUS_WAITING_APPROVAL_SPV_LEGAL;
					historyStatus = "Approve by Legal";
					emailResponsibility = NotaryConstants.RESPONSIBILITY_SPV_LEGAL;
				} else {
					nextStatus = NotaryConstants.STATUS_COMPLETE;
					historyStatus = "Approve by SPV Legal";
				}
			} else if ("REJECT".equals(actionType)) {
				nextStatus = NotaryConstants.STATUS_REJECTED;
				historyStatus = "Reject";
			} else {
				nextStatus = NotaryConstants.STATUS_REVISION;
				if (isSpvLegal() && StringUtils.equals(NotaryConstants.REVISI_TARGET_LEGAL, revisiTarget)) {
					historyStatus = NotaryConstants.HISTORY_REVISION_SPV_TO_LEGAL;
					emailResponsibility = NotaryConstants.RESPONSIBILITY_LEGAL;
				} else if (isLegal()) {
					historyStatus = "Revision by Legal";
					emailResponsibility = NotaryConstants.RESPONSIBILITY_CDU_MAKER;
				} else {
					historyStatus = isSpvLegal() ? "Revision by SPV Legal" : "Revision by CDU Checker";
					emailResponsibility = NotaryConstants.RESPONSIBILITY_CDU_MAKER;
				}
			}
			String catatan = buildCatatan();
			notary.setStatus(nextStatus);
			notary.setLastUpdateBy(userLogin);
			notary.setLastUpdateDate(new Timestamp(new Date().getTime()));
			notaryService.update(notary);
			notaryService.saveHistory(notary, historyStatus, catatan, userLogin);
			if (StringUtils.isNotBlank(emailResponsibility)) {
				sendEmail(emailResponsibility, historyStatus);
			}
			facesUtil.redirect("/pages/notary/notaryTask.faces");
		} catch (Exception e) {
			addErr("Operation Failed : " + e.getMessage());
		}
	}

	private String buildCatatan() {
		StringBuilder sb = new StringBuilder();
		if (StringUtils.isNotBlank(catatanRevisi)) {
			sb.append(catatanRevisi);
		}
		if (selectedRevisiDocuments != null && !selectedRevisiDocuments.isEmpty()) {
			if (sb.length() > 0) {
				sb.append(" | Dokumen: ");
			} else {
				sb.append("Dokumen: ");
			}
			for (int i = 0; i < selectedRevisiDocuments.size(); i++) {
				if (i > 0) {
					sb.append("; ");
				}
				sb.append(selectedRevisiDocuments.get(i));
			}
		}
		return sb.length() == 0 ? null : sb.toString();
	}

	private void sendEmail(String responsibilityName, String subject) {
		try {
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
			String content = subject;
			if (notary != null && StringUtils.isNotBlank(notary.getNotaryName())) {
				content = content + ". Nama Notaris: " + notary.getNotaryName();
			}
			CallApiManager.sendEmailAPI(to.toString(), "", subject, content, "NOTARY", "true",
					parameterDetailService);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void addErr(String summary) {
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, null));
	}

	public void downloadFile(String fileId, String fileName, byte[] content) throws Exception {
		CallApiManager.downloadFile(fileId, fileName, content, parameterDetailService);
	}

	public void openFormTab() {
		showLampiranTab = false;
	}

	public void openLampiranTab() {
		showLampiranTab = true;
	}

	public void cancel() {
		try {
			facesUtil.redirect("/pages/notary/notaryTask.faces");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Notary getNotary() {
		return notary;
	}

	public void setNotary(Notary notary) {
		this.notary = notary;
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

	public String getCatatanRevisi() {
		return catatanRevisi;
	}

	public void setCatatanRevisi(String catatanRevisi) {
		this.catatanRevisi = catatanRevisi;
	}

	public String getCatatanRevisiError() {
		return catatanRevisiError;
	}

	public void setCatatanRevisiError(String catatanRevisiError) {
		this.catatanRevisiError = catatanRevisiError;
	}

	public String getRevisiTarget() {
		return revisiTarget;
	}

	public void setRevisiTarget(String revisiTarget) {
		this.revisiTarget = revisiTarget;
	}

	public List<String> getSelectedRevisiDocuments() {
		return selectedRevisiDocuments;
	}

	public void setSelectedRevisiDocuments(List<String> selectedRevisiDocuments) {
		this.selectedRevisiDocuments = selectedRevisiDocuments;
	}

	public boolean isShowLampiranTab() {
		return showLampiranTab;
	}

	public void setShowLampiranTab(boolean showLampiranTab) {
		this.showLampiranTab = showLampiranTab;
	}

}
