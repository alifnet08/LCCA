package com.wo.module.notary.bean;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.primefaces.event.FileUploadEvent;

import com.wo.module.article.model.ArticleTag;
import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.model.UploadedFileWO;
import com.wo.module.common.util.FileUtil;
import com.wo.module.common.utility.CallApiManager;
import com.wo.module.notary.constant.NotaryConstants;
import com.wo.module.notary.model.Notary;
import com.wo.module.notary.model.NotaryDocument;
import com.wo.module.notary.model.NotaryHistory;
import com.wo.module.notary.service.NotaryService;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.outgoingLetter.constant.OutgoingLetterConstants;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.parameter.model.ParameterHeader;
import com.wo.module.responsibility.model.Responsibility;
import com.wo.module.responsibility.service.ResponsibilityService;
import com.wo.module.user.model.User;
import com.wo.module.user.service.UserService;

public class NotaryEditBean extends CommonBean implements Serializable {

	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(NotaryEditBean.class);

	private Notary notary;

	private Boolean isViewOnly;

	private String actionMode;
	
	private List<String> keywords;

	private String editedId;

	private NotaryService notaryService;

	private UserService userService;

	private ResponsibilityService responsibilityService;

	public FacesUtil facesUtil;
	
	private Integer lastSequenceOfDtl;

	private List<SelectItem> nameList;
	private List<SelectItem> slaTypeList;
	private List<SelectItem> categoryList;
	
	private List<UploadedFileWO> uploadFiles;
	private List<UploadedFileWO> deleteFiles;
	
	private FileUtil fileUtil;

	private boolean checkAll;

	private boolean showLampiranTab;

	private boolean showLampiranPerpanjanganTab;

	private List<NotaryDocument> lampiranList = new ArrayList<NotaryDocument>();

	private List<NotaryDocument> lampiranPerpanjanganList = new ArrayList<NotaryDocument>();

	private boolean pengajuanPerpanjangan;
	private boolean pengajuanUpdateDokumen;

	private String navigateSeanotaryh = NotaryConstants.NAVIGATE_SEARCH;

	private String currentResponsibilityName;

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
		initList();
		checkNewOrEdit();
		fileUtil = FileUtil.getInstance();
		if (StringUtils.isBlank(standardAttachmentSize)) {
			standardAttachmentSize = "10485760";
		}
	}
	
	public void initList(){
		try {
		categoryList = new ArrayList<SelectItem>();
		List<ParameterDetail> listCategory = parameterDetailService
					.getParameterDetailByParamCode(ParameterHeader.PARAM_HEAD_NOTARY_CATEGORY);
		

		if (listCategory != null) {
		for (ParameterDetail vo : listCategory) {
			SelectItem si = new SelectItem();
			si.setLabel(vo.getNameIn());
			si.setValue(vo.getParameterDtlCode());
			categoryList.add(si);
		}
		}
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}


	
	private void checkNewOrEdit() {
		isViewOnly = false;
		pengajuanPerpanjangan = false;
		pengajuanUpdateDokumen = false;
		if (notary == null) {
			notary = new Notary();
			notary.setNotaryCategory(new ParameterDetail());
		}
		try {
			String editId = facesUtil.retrieveRequestParam("id");
			String token = facesUtil.retrieveRequestParam("token"); 
			
			String viewId = facesUtil.retrieveRequestParam("viewId");
			String jenisPengajuanParam = facesUtil.retrieveRequestParam("jenisPengajuan");
			if (viewId != null && !viewId.isEmpty()) {
				if (viewId.trim().equalsIgnoreCase("true")) {
					isViewOnly = true;
				}
			}
			if (StringUtils.isBlank(editId) && StringUtils.isBlank(token)) {
				this.handleNew();

			} else {
				this.handleEdit(editId);
				applyRequestedJenisPengajuan(jenisPengajuanParam);
			}
			if (notary != null && notary.getNotaryCategory() == null) {
				notary.setNotaryCategory(new ParameterDetail());
			}
			applyTaskAccess();
		} catch (Exception e) {
			logger.error("Failed to initialize notary edit form", e);
			if (notary == null) {
				this.handleNew();
			}
		}

	}

	private void handleNew() {
		notary = new Notary();
		ParameterDetail pd = new ParameterDetail();
		notary.setNotaryCategory(pd);
		notary.setJenisPengajuan(NotaryConstants.JENIS_PENGAJUAN_NOTARIS_BARU);
		pengajuanPerpanjangan = false;
		pengajuanUpdateDokumen = false;
		facesUtil.setSessionAttribute(NotaryConstants.SESSION_JENIS_PENGAJUAN,
				NotaryConstants.JENIS_PENGAJUAN_NOTARIS_BARU);
		lastSequenceOfDtl = 0;
		actionMode = Constants.ACTION_ADD;
		uploadFiles = new ArrayList<UploadedFileWO>();
		facesUtil.setSessionAttribute("token", null);
		initLampiranList();
		initLampiranPerpanjanganList();
	}

	private void handleEdit(String editId) {
		String token = facesUtil.retrieveRequestParam("token");
		if(StringUtils.isNotEmpty(token)) {
			editId = Constants.decryptString(token);
		}
		facesUtil.setSessionAttribute("token", Constants.encryptString(editId));
		actionMode = Constants.ACTION_EDIT;
		Long idLong = Long.parseLong(editId);
		notary = notaryService.findById(idLong);
		if (notary == null) {
			throw new IllegalStateException("Notary not found: " + editId);
		}
		if (notary.getNotaryCategory() == null) {
			notary.setNotaryCategory(new ParameterDetail());
		}
		lastSequenceOfDtl = 0;
		initLampiranList();
		initLampiranPerpanjanganList();
	}

	private void applyRequestedJenisPengajuan(String jenisPengajuanParam) {
		if (notary == null) {
			return;
		}
		String jenis = jenisPengajuanParam;
		if (StringUtils.isBlank(jenis) && facesUtil != null) {
			Object sessionJenis = facesUtil.getSessionAttribute(NotaryConstants.SESSION_JENIS_PENGAJUAN);
			if (sessionJenis != null) {
				jenis = sessionJenis.toString();
			}
		}
		if (StringUtils.equals(NotaryConstants.JENIS_PENGAJUAN_PERPANJANGAN, jenis)) {
			pengajuanPerpanjangan = true;
			pengajuanUpdateDokumen = false;
			notary.setJenisPengajuan(NotaryConstants.JENIS_PENGAJUAN_PERPANJANGAN);
			facesUtil.setSessionAttribute(NotaryConstants.SESSION_JENIS_PENGAJUAN,
					NotaryConstants.JENIS_PENGAJUAN_PERPANJANGAN);
		} else if (StringUtils.equals(NotaryConstants.JENIS_PENGAJUAN_UPDATE_DOKUMEN, jenis)) {
			pengajuanPerpanjangan = false;
			pengajuanUpdateDokumen = true;
			notary.setJenisPengajuan(NotaryConstants.JENIS_PENGAJUAN_UPDATE_DOKUMEN);
			facesUtil.setSessionAttribute(NotaryConstants.SESSION_JENIS_PENGAJUAN,
					NotaryConstants.JENIS_PENGAJUAN_UPDATE_DOKUMEN);
		} else {
			applyJenisPengajuanFromEntity();
		}
	}

	private void applyJenisPengajuanFromEntity() {
		if (notary == null) {
			return;
		}
		if (StringUtils.equals(NotaryConstants.JENIS_PENGAJUAN_PERPANJANGAN, notary.getJenisPengajuan())) {
			pengajuanPerpanjangan = true;
		} else if (StringUtils.equals(NotaryConstants.JENIS_PENGAJUAN_UPDATE_DOKUMEN, notary.getJenisPengajuan())) {
			pengajuanUpdateDokumen = true;
		}
	}

	public Boolean validate() {
		Boolean flag = false;

		if (StringUtils.isEmpty(notary.getNotaryCategory().getParameterDtlCode())) {
			facesUtil.addErrMessage(facesUtil.retrieveMessage("formNotaryCategory") 
					+ facesUtil.retrieveMessage("validateRequired"));
			flag = true;
		}
		if (StringUtils.isEmpty(notary.getArea())) {
			facesUtil.addErrMessage(facesUtil.retrieveMessage("formNotaryArea") 
					+ facesUtil.retrieveMessage("validateRequired"));
			flag = true;
		}
		if (StringUtils.isEmpty(notary.getNotaryName())) {
			facesUtil.addErrMessage(facesUtil.retrieveMessage("formNotaryNotarisName") 
					+ facesUtil.retrieveMessage("validateRequired"));
			flag = true;
		}
		if (StringUtils.isEmpty(notary.getAddress())) {
			facesUtil.addErrMessage(facesUtil.retrieveMessage("formNotaryOfficeAddr") 
					+ facesUtil.retrieveMessage("validateRequired"));
			flag = true;
		}
		if (!isPengajuanNotarisBaru() && StringUtils.isEmpty(notary.getAreaCode())) {
			facesUtil.addErrMessage(facesUtil.retrieveMessage("formNotaryAreaCode") 
					+ facesUtil.retrieveMessage("validateRequired"));
			flag = true;
		}
		if (StringUtils.isEmpty(notary.getPhoneNo())) {
			facesUtil.addErrMessage(facesUtil.retrieveMessage("formNotaryTelpNo") 
					+ facesUtil.retrieveMessage("validateRequired"));
			flag = true;
		}
		if (StringUtils.isEmpty(notary.getEmail())) {
			facesUtil.addErrMessage(facesUtil.retrieveMessage("formNotaryEmail") 
					+ facesUtil.retrieveMessage("validateRequired"));
			flag = true;
		}
		if (StringUtils.isEmpty(notary.getMobileNo())) {
			facesUtil.addErrMessage(facesUtil.retrieveMessage("formNotaryHPNo") 
					+ facesUtil.retrieveMessage("validateRequired"));
			flag = true;
		}
		if (notary.getTanggalPensiun() == null) {
			facesUtil.addErrMessage("Tanggal Pensiun"
					+ facesUtil.retrieveMessage("validateRequired"));
			flag = true;
		}
		if (notary.getTanggalBerakhirPks() == null) {
			facesUtil.addErrMessage("Tanggal Berakhir Kerjasama"
					+ facesUtil.retrieveMessage("validateRequired"));
			flag = true;
		}
		if (pengajuanPerpanjangan && !hasAllPerpanjanganDocuments()) {
			flag = true;
		}


		return flag;
	}

	private boolean hasAllPerpanjanganDocuments() {
		boolean complete = true;
		if (lampiranPerpanjanganList == null || lampiranPerpanjanganList.isEmpty()) {
			facesUtil.addErrMessage("Kelengkapan Dokumen Perpanjangan wajib diupload");
			return false;
		}
		for (int i = 0; i < lampiranPerpanjanganList.size(); i++) {
			NotaryDocument doc = lampiranPerpanjanganList.get(i);
			if (doc == null || StringUtils.isBlank(doc.getFileId())) {
				String label = doc != null ? doc.getAttachmentType() : "Dokumen Perpanjangan";
				facesUtil.addErrMessage(label + " wajib diupload");
				complete = false;
			}
		}
		return complete;
	}

	public boolean isRevisionSubmit() {
		return notary != null && notary.getNotaryId() != null
				&& StringUtils.equals(NotaryConstants.STATUS_REVISION, notary.getStatus());
	}

	private void resolveCurrentResponsibility() {
		currentResponsibilityName = "";
		try {
			if (facesUtil == null || userService == null || responsibilityService == null) {
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
			logger.error("Failed to resolve notary responsibility", e);
		}
	}

	private boolean isMakerRole() {
		return StringUtils.equalsIgnoreCase(NotaryConstants.RESPONSIBILITY_CDU_MAKER, currentResponsibilityName);
	}

	private boolean isLegalRole() {
		return StringUtils.equalsIgnoreCase(NotaryConstants.RESPONSIBILITY_LEGAL, currentResponsibilityName);
	}

	private boolean isRevisionForLegal(Notary target) {
		if (target == null || target.getNotaryId() == null || notaryService == null) {
			return false;
		}
		if (!StringUtils.equals(NotaryConstants.STATUS_REVISION, target.getStatus())) {
			return false;
		}
		List<NotaryHistory> historyList = notaryService.getHistoryByNotaryId(target.getNotaryId());
		if (historyList == null || historyList.isEmpty() || historyList.get(0) == null) {
			return false;
		}
		return StringUtils.equals(NotaryConstants.HISTORY_REVISION_SPV_TO_LEGAL, historyList.get(0).getStatus());
	}

	private void applyTaskAccess() {
		if (notary == null || notary.getNotaryId() == null) {
			return;
		}
		if (StringUtils.equals(NotaryConstants.STATUS_REJECTED, notary.getStatus())) {
			isViewOnly = true;
			return;
		}
		if (!StringUtils.equals(NotaryConstants.STATUS_REVISION, notary.getStatus())
				|| Boolean.TRUE.equals(isViewOnly)) {
			return;
		}
		resolveCurrentResponsibility();
		if (isRevisionForLegal(notary)) {
			if (!isLegalRole()) {
				isViewOnly = true;
			}
		} else if (!isMakerRole()) {
			isViewOnly = true;
		}
	}

	public void save() {
		try {
			if (Boolean.TRUE.equals(isViewOnly)) {
				facesUtil.addErrMessage("Pengajuan ini hanya dapat dilihat.");
				return;
			}
			boolean revisionForLegal = isRevisionForLegal(notary);
			if (notary != null && StringUtils.equals(NotaryConstants.STATUS_REJECTED, notary.getStatus())) {
				facesUtil.addErrMessage("Pengajuan yang ditolak hanya dapat dilihat.");
				return;
			}
			if (notary != null && StringUtils.equals(NotaryConstants.STATUS_REVISION, notary.getStatus())) {
				resolveCurrentResponsibility();
				if (revisionForLegal && !isLegalRole()) {
					facesUtil.addErrMessage("Revisi ini hanya dapat diubah oleh Legal.");
					return;
				}
				if (!revisionForLegal && !isMakerRole()) {
					facesUtil.addErrMessage("Revisi ini hanya dapat diubah oleh CDU Maker.");
					return;
				}
			}
			if (!validate()) {
				if (isPengajuanNotarisBaru()) {
					if (notary.getAreaCode() == null) {
						notary.setAreaCode("");
					}
					if (notary.getFaxNo() == null) {
						notary.setFaxNo("");
					}
				}

				ParameterDetail pd = parameterDetailService.getParameterDetailByParamDtlCode(notary.getNotaryCategory().getParameterDtlCode());
				notary.setNotaryCategory(pd);

				boolean isNewPengajuan = notary.getNotaryId() == null;
				boolean isResubmitRevisi = !isNewPengajuan
						&& StringUtils.equals(NotaryConstants.STATUS_REVISION, notary.getStatus());
				if (isNewPengajuan) {
					notary.setStatus(NotaryConstants.STATUS_WAITING_APPROVAL_CDU_CHECKER);
					notary.setJenisPengajuan(NotaryConstants.JENIS_PENGAJUAN_NOTARIS_BARU);
				} else if (isResubmitRevisi && revisionForLegal) {
					notary.setStatus(NotaryConstants.STATUS_WAITING_APPROVAL_SPV_LEGAL);
				} else if (isResubmitRevisi) {
					notary.setStatus(NotaryConstants.STATUS_WAITING_APPROVAL_CDU_CHECKER);
				} else if (pengajuanPerpanjangan) {
					notary.setStatus(NotaryConstants.STATUS_WAITING_APPROVAL_CDU_CHECKER);
					notary.setJenisPengajuan(NotaryConstants.JENIS_PENGAJUAN_PERPANJANGAN);
				} else if (pengajuanUpdateDokumen) {
					notary.setStatus(NotaryConstants.STATUS_WAITING_APPROVAL_CDU_CHECKER);
					notary.setJenisPengajuan(NotaryConstants.JENIS_PENGAJUAN_UPDATE_DOKUMEN);
				}

				boolean startNewFlow = isNewPengajuan
						|| ((pengajuanPerpanjangan || pengajuanUpdateDokumen) && !isResubmitRevisi);
				if (startNewFlow) {
					String login = facesUtil.retrieveUserLogin();
					notary.setUserPengaju(login);
					notary.setTanggalPengajuan(new Timestamp(new Date().getTime()));
					String prefix = NotaryConstants.PREFIX_NO_PENGAJUAN_BARU;
					if (pengajuanPerpanjangan) {
						prefix = NotaryConstants.PREFIX_NO_PENGAJUAN_PERPANJANGAN;
					} else if (pengajuanUpdateDokumen) {
						prefix = NotaryConstants.PREFIX_NO_PENGAJUAN_UPDATE_DOKUMEN;
					}
					notary.setNotaryNo(notaryService.generateNoPengajuan(prefix));
				} else if (isResubmitRevisi) {
					if (!revisionForLegal) {
						notary.setUserPengaju(facesUtil.retrieveUserLogin());
					}
					notary.setTanggalPengajuan(new Timestamp(new Date().getTime()));
				}

				prepareNotaryDocuments();

				if (notary.getNotaryId() != null) {
					notary.setLastUpdateBy(facesUtil.retrieveUserLogin());
					notary.setLastUpdateDate(new Timestamp(new Date().getTime()));
					notary.setDelId(new Long(0));
					notary.setEnabledFlag(Constants.CONSTANT_YES);
					notaryService.update(notary);

				} else {
					notary.setCreatedBy(facesUtil.retrieveUserLogin());
					notary.setCreationDate(new Timestamp(new Date().getTime()));
					notary.setDelId(new Long(0));
					notary.setEnabledFlag(Constants.CONSTANT_YES);
					notaryService.save(notary);
				}

				if (isNewPengajuan || pengajuanPerpanjangan || pengajuanUpdateDokumen || isResubmitRevisi) {
					String historyStatus = "Submit by CDU Maker";
					if (isResubmitRevisi && revisionForLegal) {
						historyStatus = "Submit by Legal";
						sendNotificationToSpvLegal();
					} else {
						sendNotificationToCduChecker();
						if (pengajuanPerpanjangan && !isResubmitRevisi) {
							historyStatus = "Submit Perpanjangan by CDU Maker";
						} else if (pengajuanUpdateDokumen && !isResubmitRevisi) {
							historyStatus = "Submit Update Dokumen by CDU Maker";
						}
					}
					notaryService.saveHistory(notary, historyStatus, null, facesUtil.retrieveUserLogin());
				}

				facesUtil.setSessionAttribute(NotaryConstants.SESSION_JENIS_PENGAJUAN, null);

				if (deleteFiles != null) {
					for (int i = 0; i < deleteFiles.size(); i++) {
						UploadedFileWO uf = (UploadedFileWO) deleteFiles.get(i);
						CallApiManager.deleteFile(uf.getFileId(), parameterDetailService, fileUtil);
					}
				}

				if (isResubmitRevisi) {
					facesUtil.redirect("/pages/notary/notaryTask.faces");
				} else {
					facesUtil.redirect("/pages/notary/notary.faces");
				}
			}

		} catch (Exception ex) {
			facesUtil.addFacesMsg(FacesMessage.SEVERITY_ERROR, null, "Operation Failed : " + ex.getMessage(), "");
		}

	}

	private void sendNotificationToCduChecker() {
		String subject = "Pengajuan Penambahan Notaris";
		String content = "Pengajuan Penambahan Notaris Anda telah berhasil disubmit.";
		if (pengajuanPerpanjangan) {
			subject = "Pengajuan Perpanjangan Notaris";
			content = "Pengajuan Perpanjangan Notaris Anda telah berhasil disubmit.";
		} else if (pengajuanUpdateDokumen) {
			subject = "Pengajuan Update Dokumen Notaris";
			content = "Pengajuan Update Dokumen Notaris Anda telah berhasil disubmit.";
		}
		if (notary != null && StringUtils.isNotBlank(notary.getNotaryName())) {
			content = content + " Nama Notaris: " + notary.getNotaryName();
		}
		sendNotification(NotaryConstants.RESPONSIBILITY_CDU_CHECKER, subject, content);
	}

	private void sendNotificationToSpvLegal() {
		String subject = "Revisi Notaris dari Legal";
		String content = "Revisi pengajuan notaris telah disubmit oleh Legal.";
		if (notary != null && StringUtils.isNotBlank(notary.getNotaryName())) {
			content = content + " Nama Notaris: " + notary.getNotaryName();
		}
		sendNotification(NotaryConstants.RESPONSIBILITY_SPV_LEGAL, subject, content);
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
					if (user == null || user.getResponsibilityId() == null
							|| StringUtils.isBlank(user.getEmail())) {
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
			CallApiManager.sendEmailAPI(to.toString(), "", subject, content, "NOTARY", "true",
					parameterDetailService);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void initLampiranList() {
		lampiranList = new ArrayList<NotaryDocument>();
		List<NotaryDocument> existingDocs = null;
		try {
			if (notary != null) {
				existingDocs = notary.getNotaryDocuments();
			}
		} catch (Exception e) {
			logger.error("Failed to read existing notary documents", e);
			existingDocs = null;
		}
		for (int i = 0; i < NotaryConstants.NOTARY_DOCUMENT_TYPES.length; i++) {
			String documentType = NotaryConstants.NOTARY_DOCUMENT_TYPES[i];
			NotaryDocument slot = null;
			if (existingDocs != null) {
				for (int j = 0; j < existingDocs.size(); j++) {
					NotaryDocument existing = existingDocs.get(j);
					if (existing != null && StringUtils.equals(documentType, existing.getAttachmentType())) {
						slot = existing;
						break;
					}
				}
			}
			if (slot == null) {
				slot = new NotaryDocument();
				slot.setAttachmentType(documentType);
			}
			lampiranList.add(slot);
		}
	}

	private void initLampiranPerpanjanganList() {
		lampiranPerpanjanganList = new ArrayList<NotaryDocument>();
		List<NotaryDocument> existingDocs = null;
		try {
			if (notary != null) {
				existingDocs = notary.getNotaryDocuments();
			}
		} catch (Exception e) {
			logger.error("Failed to read perpanjangan documents", e);
			existingDocs = null;
		}
		for (int i = 0; i < NotaryConstants.NOTARY_PERPANJANGAN_DOCUMENT_TYPES.length; i++) {
			String documentType = NotaryConstants.NOTARY_PERPANJANGAN_DOCUMENT_TYPES[i];
			NotaryDocument slot = null;
			if (existingDocs != null) {
				for (int j = 0; j < existingDocs.size(); j++) {
					NotaryDocument existing = existingDocs.get(j);
					if (existing != null && StringUtils.equals(documentType, existing.getAttachmentType())) {
						slot = existing;
						break;
					}
				}
			}
			if (slot == null) {
				slot = new NotaryDocument();
				slot.setAttachmentType(documentType);
			}
			slot.setDocumentNo(NotaryConstants.NOTARY_PERPANJANGAN_DOCUMENT_NOS[i]);
			lampiranPerpanjanganList.add(slot);
		}
	}

	private void prepareNotaryDocuments() {
		if (notary.getNotaryDocuments() == null) {
			notary.setNotaryDocuments(new ArrayList<NotaryDocument>());
		}
		notary.getNotaryDocuments().clear();
		appendPreparedDocuments(lampiranList);
		appendPreparedDocuments(lampiranPerpanjanganList);
	}

	private void appendPreparedDocuments(List<NotaryDocument> source) {
		if (source == null) {
			return;
		}
		for (int i = 0; i < source.size(); i++) {
			NotaryDocument doc = source.get(i);
			if (doc == null || StringUtils.isBlank(doc.getFileId())) {
				continue;
			}
			doc.setNotary(notary);
			if (doc.getNotaryDocumentId() == null) {
				doc.setCreatedBy(facesUtil.retrieveUserLogin());
				doc.setCreationDate(new Timestamp(new Date().getTime()));
			} else {
				doc.setLastUpdateBy(facesUtil.retrieveUserLogin());
				doc.setLastUpdateDate(new Timestamp(new Date().getTime()));
			}
			doc.setDelId(new Long(0));
			doc.setEnabledFlag(Constants.CONSTANT_YES);
			notary.getNotaryDocuments().add(doc);
		}
	}

	public void handleLampiranFileUpload(FileUploadEvent event) {
		if (!isLampiranUploadEnabled()) {
			return;
		}
		try {
			String attachmentType = (String) event.getComponent().getAttributes().get("attachmentType");
			String fileId = CallApiManager.callUploadAPI(event.getFile(), Constants.ARTICLE, parameterDetailService,
					false, fileUtil);
			if (lampiranList != null) {
				for (int i = 0; i < lampiranList.size(); i++) {
					NotaryDocument doc = lampiranList.get(i);
					if (doc != null && StringUtils.equals(attachmentType, doc.getAttachmentType())) {
						doc.setFileId(fileId);
						doc.setAttachmentFile(event.getFile().getFileName());
						doc.setFileSize(event.getFile().getSize());
						break;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			facesUtil.addErrMessage(e.getMessage());
		}
	}

	public void handleLampiranPerpanjanganFileUpload(FileUploadEvent event) {
		if (!isPerpanjanganUploadEnabled()) {
			facesUtil.addErrMessage("Upload dokumen perpanjangan hanya untuk jenis pengajuan Perpanjangan");
			return;
		}
		try {
			if (fileUtil == null) {
				fileUtil = FileUtil.getInstance();
			}
			String attachmentType = null;
			if (event.getComponent() != null) {
				Object attribute = event.getComponent().getAttributes().get("attachmentType");
				if (attribute != null) {
					attachmentType = attribute.toString();
				}
				if (StringUtils.isBlank(attachmentType)) {
					attachmentType = attachmentTypeFromUploadId(event.getComponent().getId());
				}
			}
			if (StringUtils.isBlank(attachmentType)) {
				facesUtil.addErrMessage("Jenis dokumen perpanjangan tidak dikenali");
				return;
			}
			String fileId = CallApiManager.callUploadAPI(event.getFile(), Constants.ARTICLE, parameterDetailService,
					false, fileUtil);
			boolean stored = false;
			if (lampiranPerpanjanganList == null || lampiranPerpanjanganList.isEmpty()) {
				initLampiranPerpanjanganList();
			}
			for (int i = 0; i < lampiranPerpanjanganList.size(); i++) {
				NotaryDocument doc = lampiranPerpanjanganList.get(i);
				if (doc != null && StringUtils.equals(attachmentType, doc.getAttachmentType())) {
					doc.setFileId(fileId);
					doc.setAttachmentFile(event.getFile().getFileName());
					doc.setFileSize(event.getFile().getSize());
					stored = true;
					break;
				}
			}
			if (!stored) {
				facesUtil.addErrMessage("Dokumen " + attachmentType + " tidak ditemukan pada daftar lampiran");
			}
		} catch (Exception e) {
			e.printStackTrace();
			facesUtil.addErrMessage(e.getMessage());
		}
	}

	private String attachmentTypeFromUploadId(String componentId) {
		if (StringUtils.isBlank(componentId)) {
			return null;
		}
		int number = -1;
		if (componentId.startsWith("perpUpload")) {
			try {
				number = Integer.parseInt(componentId.substring("perpUpload".length()));
			} catch (NumberFormatException e) {
				number = -1;
			}
		}
		for (int i = 0; i < NotaryConstants.NOTARY_PERPANJANGAN_DOCUMENT_NOS.length; i++) {
			if (NotaryConstants.NOTARY_PERPANJANGAN_DOCUMENT_NOS[i] == number) {
				return NotaryConstants.NOTARY_PERPANJANGAN_DOCUMENT_TYPES[i];
			}
		}
		return null;
	}

	public void deleteLampiranPerpanjanganAttachment(String fileId, String attachmentType) throws Exception {
		if (!isPerpanjanganUploadEnabled()) {
			return;
		}
		deleteFiles = deleteFiles != null ? deleteFiles : new ArrayList<UploadedFileWO>();
		if (StringUtils.isNotBlank(fileId)) {
			deleteFiles.add(new UploadedFileWO(fileId, null, null, null));
		}
		if (lampiranPerpanjanganList != null) {
			for (int i = 0; i < lampiranPerpanjanganList.size(); i++) {
				NotaryDocument doc = lampiranPerpanjanganList.get(i);
				if (doc != null && StringUtils.equals(attachmentType, doc.getAttachmentType())) {
					doc.setFileId(null);
					doc.setAttachmentFile(null);
					doc.setFileSize(null);
					break;
				}
			}
		}
	}

	public void deleteLampiranAttachment(String fileId, String attachmentType) throws Exception {
		if (!isLampiranUploadEnabled()) {
			return;
		}
		deleteFiles = deleteFiles != null ? deleteFiles : new ArrayList<UploadedFileWO>();
		if (StringUtils.isNotBlank(fileId)) {
			deleteFiles.add(new UploadedFileWO(fileId, null, null, null));
		}
		if (lampiranList != null) {
			for (int i = 0; i < lampiranList.size(); i++) {
				NotaryDocument doc = lampiranList.get(i);
				if (doc != null && StringUtils.equals(attachmentType, doc.getAttachmentType())) {
					doc.setFileId(null);
					doc.setAttachmentFile(null);
					doc.setFileSize(null);
					break;
				}
			}
		}
	}

	public void handleFileUpload (FileUploadEvent event) {
		try {
			uploadFiles = uploadFiles == null ? new ArrayList<UploadedFileWO>() : uploadFiles;
			uploadFiles.add(new UploadedFileWO( CallApiManager.callUploadAPI(event.getFile(), Constants.ARTICLE, parameterDetailService, false, fileUtil),
					event.getFile().getFileName(),event.getFile().getContentType(),event.getFile().getSize()));
		} catch (Exception e) {
			e.printStackTrace();
			facesUtil.addErrMessage(e.getMessage());
		}
	}
	
	public void deleteAttachment (String fileId, int index, String uploadType) throws Exception {
		deleteFiles = deleteFiles != null ? deleteFiles : new ArrayList<UploadedFileWO>();
		deleteFiles.add(new UploadedFileWO(fileId,null,null,null));
		
		if(uploadType != null && uploadType.equals(OutgoingLetterConstants.UPLOAD_TYPE_DOCUMENT)) {
			uploadFiles.remove(uploadFiles.get(index));
		}
	}
	
	public void downloadFile (String fileId, String fileName, byte[] content) throws Exception {
		CallApiManager.downloadFile(fileId, fileName, content, parameterDetailService);
	}

	public void cancel() {
		try {
			boolean fromTask = false;
			if (facesUtil != null) {
				Object marker = facesUtil.getSessionAttribute(NotaryConstants.SESSION_FROM_TASK);
				fromTask = marker != null && "Y".equalsIgnoreCase(marker.toString());
				facesUtil.setSessionAttribute(NotaryConstants.SESSION_FROM_TASK, null);
			}
			if (fromTask) {
				facesUtil.redirect("/pages/notary/notaryTask.faces");
			} else {
				facesUtil.redirect("/pages/notary/notary.faces");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public FacesUtil getFacesUtil() {
		return facesUtil;
	}

	public void setFacesUtil(FacesUtil facesUtil) {
		this.facesUtil = facesUtil;
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

	public String getNavigateSeanotaryh() {
		return navigateSeanotaryh;
	}

	public void setNavigateSeanotaryh(String navigateSeanotaryh) {
		this.navigateSeanotaryh = navigateSeanotaryh;
	}

	public Notary getNotary() {
		return notary;
	}

	public void setNotary(Notary notary) {
		this.notary = notary;
	}

	public boolean isPengajuanNotarisBaru() {
		return !pengajuanPerpanjangan && !pengajuanUpdateDokumen;
	}

	public boolean isPerpanjanganReadOnly() {
		return pengajuanPerpanjangan;
	}

	public boolean isFieldReadOnly() {
		return Boolean.TRUE.equals(isViewOnly) || isPerpanjanganReadOnly();
	}

	public boolean isLampiranUploadEnabled() {
		return !Boolean.TRUE.equals(isViewOnly) && !pengajuanPerpanjangan;
	}

	public boolean isPengajuanPerpanjangan() {
		return pengajuanPerpanjangan;
	}

	public void setPengajuanPerpanjangan(boolean pengajuanPerpanjangan) {
		this.pengajuanPerpanjangan = pengajuanPerpanjangan;
	}

	public Boolean getIsViewOnly() {
		return isViewOnly;
	}

	public void setIsViewOnly(Boolean isViewOnly) {
		this.isViewOnly = isViewOnly;
	}

	public String getActionMode() {
		return actionMode;
	}

	public void setActionMode(String actionMode) {
		this.actionMode = actionMode;
	}

	public String getEditedId() {
		return editedId;
	}

	public void setEditedId(String editedId) {
		this.editedId = editedId;
	}


	public List<SelectItem> getNameList() {
		return nameList;
	}

	public void setNameList(List<SelectItem> nameList) {
		this.nameList = nameList;
	}

	public List<SelectItem> getSlaTypeList() {
		return slaTypeList;
	}

	public void setSlaTypeList(List<SelectItem> slaTypeList) {
		this.slaTypeList = slaTypeList;
	}

	public boolean isCheckAll() {
		return checkAll;
	}

	public void setCheckAll(boolean checkAll) {
		this.checkAll = checkAll;
	}


	public Integer getLastSequenceOfDtl() {
		return lastSequenceOfDtl;
	}

	public void setLastSequenceOfDtl(Integer lastSequenceOfDtl) {
		this.lastSequenceOfDtl = lastSequenceOfDtl;
	}

	public Notary getRc() {
		return notary;
	}

	public void setRc(Notary notary) {
		this.notary = notary;
	}

	public NotaryService getRcService() {
		return notaryService;
	}

	public void setRcService(NotaryService notaryService) {
		this.notaryService = notaryService;
	}


	public List<SelectItem> getCategoryList() {
		return categoryList;
	}

	public void setCategoryList(List<SelectItem> categoryList) {
		this.categoryList = categoryList;
	}

	public List<UploadedFileWO> getUploadFiles() {
		return uploadFiles;
	}

	public void setUploadFiles(List<UploadedFileWO> uploadFiles) {
		this.uploadFiles = uploadFiles;
	}

	public FileUtil getFileUtil() {
		return fileUtil;
	}

	public void setFileUtil(FileUtil fileUtil) {
		this.fileUtil = fileUtil;
	}

	public List<UploadedFileWO> getDeleteFiles() {
		return deleteFiles;
	}

	public void setDeleteFiles(List<UploadedFileWO> deleteFiles) {
		this.deleteFiles = deleteFiles;
	}

	public List<String> getKeywords() {
		return keywords;
	}

	public void setKeywords(List<String> keywords) {
		this.keywords = keywords;
	}

	public boolean isShowLampiranTab() {
		return showLampiranTab;
	}

	public void setShowLampiranTab(boolean showLampiranTab) {
		this.showLampiranTab = showLampiranTab;
	}

	public void openFormTab() {
		showLampiranTab = false;
		showLampiranPerpanjanganTab = false;
	}

	public void openLampiranTab() {
		showLampiranTab = true;
		showLampiranPerpanjanganTab = false;
	}

	public void openLampiranPerpanjanganTab() {
		showLampiranTab = false;
		showLampiranPerpanjanganTab = true;
		if (lampiranPerpanjanganList == null || lampiranPerpanjanganList.isEmpty()) {
			initLampiranPerpanjanganList();
		}
	}

	public boolean isPerpanjanganUploadEnabled() {
		if (Boolean.TRUE.equals(isViewOnly)) {
			return false;
		}
		if (pengajuanPerpanjangan) {
			return true;
		}
		return notary != null && StringUtils.equals(NotaryConstants.JENIS_PENGAJUAN_PERPANJANGAN,
				notary.getJenisPengajuan());
	}

	public boolean isShowLampiranPerpanjanganTab() {
		return showLampiranPerpanjanganTab;
	}

	public void setShowLampiranPerpanjanganTab(boolean showLampiranPerpanjanganTab) {
		this.showLampiranPerpanjanganTab = showLampiranPerpanjanganTab;
	}

	public List<NotaryDocument> getLampiranPerpanjanganList() {
		if (lampiranPerpanjanganList == null || lampiranPerpanjanganList.isEmpty()) {
			initLampiranPerpanjanganList();
		}
		return lampiranPerpanjanganList;
	}

	public void setLampiranPerpanjanganList(List<NotaryDocument> lampiranPerpanjanganList) {
		this.lampiranPerpanjanganList = lampiranPerpanjanganList;
	}

	public List<NotaryDocument> getLampiranList() {
		if (lampiranList == null || lampiranList.isEmpty()) {
			initLampiranList();
		}
		return lampiranList;
	}

	public void setLampiranList(List<NotaryDocument> lampiranList) {
		this.lampiranList = lampiranList;
	}

}