package com.wo.module.fineFE.bean;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.model.UploadedFileWO;
import com.wo.module.common.util.FileUtil;
import com.wo.module.common.utility.CallApiManager;
import com.wo.module.fineFE.constant.FineFEConstant;
import com.wo.module.fineFE.service.FineFEService;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.trcFineApproval.model.TrcFine;
import com.wo.module.trcFineApproval.model.TrcFinePicFollowup;
import com.wo.module.trcFineApproval.model.TrcFinePicFollowupAttachment;
import com.wo.module.trcFineApproval.service.TrcFinePicFollowupService;
import com.wo.module.user.service.UserService;

public class FineFEViewBean extends CommonBean implements Serializable{

	private static final long serialVersionUID = -169780966133226377L;
	private static final Logger logger = Logger.getLogger(FineFEViewBean.class);
	private static final String NAVIGATE_BACK = FineFEConstant.NAVIGATE_FINE_FE;
	
	private FineFEService fineFEService;
	private UserService userService;
	private TrcFinePicFollowupService trcFinePicFollowupService;
	
	private TrcFine trcFine;
	private TrcFinePicFollowup trcFinePicFollowup;
	
	private String viewId;
	private String pengirim;
	private String tanggalTerimaSurat;
	private String perihal;
	private String tanggalSurat;
	private String tipeSurat;
	private String catatanComplinace;
	private String tglTindakLanjut;
	private String keterangan;
	private String followupNotes;
	private String followupDate;
	private String textWarningUpload;
	private String selectedRcId;
	private String isExtended;
	private String verifyStatusName;
	private String selectedRC;
	private String categoryName;
	private String targetDate;
	
	private List<UploadedFileWO> uploadedFiles;
	List<UploadedFileWO> trcFinePicExtendeds;
	List<UploadedFileWO> trcFinePicFollowups;
	
	private FacesUtil facesUtil;
	private FileUtil fileUtil;

	private SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", new Locale("id", "ID"));

	private Integer testFirst;
	
	private Boolean isRevise;
	private Boolean verifyActive;
	
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
		testFirst = Integer.parseInt(facesUtil.retrieveRequestParam("first")!=null?facesUtil.retrieveRequestParam("first"):"0");
		facesUtil.setSessionAttribute("FIRST_FINE_FE", testFirst);
		facesUtil.setSessionAttribute("BACK_SESSION", false);
		checkNewOrEdit();
		fileUtil = FileUtil.getInstance();
	}

	private void checkNewOrEdit() {
		this.viewId = facesUtil.retrieveRequestParam("id");
//		this.editId = facesUtil.retrieveRequestParam("id");
		String token = facesUtil.retrieveRequestParam("token");
		if (StringUtils.isNotEmpty(token)) {
			viewId = Constants.decryptString(token);
		}
		facesUtil.setSessionAttribute("token", Constants.encryptString(viewId));
		
		if (StringUtils.isBlank(viewId)) {
			// do nothing
		} else {
			handleView(viewId);
		}
	}
	
	private void handleView(String viewId) {
		String token = facesUtil.retrieveRequestParam("token");
		if (StringUtils.isNotEmpty(token)) {
			viewId = Constants.decryptString(token);
		}
//		Long viewIdLong = Long.parseLong(viewId);
		uploadedFiles = new ArrayList<UploadedFileWO>();
		trcFinePicExtendeds = new ArrayList<>();
		trcFinePicFollowups = new ArrayList<>();
		
		trcFinePicFollowup =  trcFinePicFollowupService.findById(Long.parseLong(viewId));
		
		trcFine = fineFEService.findById(trcFinePicFollowup.getTrcFine().getFineId());
		
		this.pengirim = trcFine.getSenderCode() != null ? trcFine.getSenderCode().getNameIn() : "";
		this.tanggalTerimaSurat = trcFine.getLetterReceivedDate() != null ? sdf.format(trcFine.getLetterReceivedDate()) : "";
		this.perihal = trcFine.getPerihalIn() != null ? trcFine.getPerihalIn() : "";
		this.tanggalSurat = trcFine.getLetterDate() != null ? sdf.format(trcFine.getLetterDate()) : "";
		this.tipeSurat = trcFine.getFineCode()  !=  null ? trcFine.getFineCode().getNameIn() : "";
		this.verifyActive = (trcFinePicFollowup.getComplianceStatus() != null || trcFinePicFollowup.getTargetDateStatus() != null);
		
		this.categoryName = "";
		if(trcFinePicFollowup.getCategory()  != null) {
			try {
				ParameterDetail pdCategory = parameterDetailService.getParameterDetailByParamDtlCode(trcFinePicFollowup.getCategory());
				this.categoryName = pdCategory != null ? pdCategory.getNameIn() : "";
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if(trcFinePicFollowup.getComplianceStatus() != null) {
			this.verifyStatusName = trcFinePicFollowup.getComplianceStatus().getNameIn();
		}else if(trcFinePicFollowup.getTargetDateStatus() != null) {
			this.verifyStatusName = trcFinePicFollowup.getTargetDateStatus().getNameIn();
		}else {
			this.verifyStatusName = "";
			if(trcFinePicFollowup.getFollowupStatus() != null) {
				verifyStatusName = trcFinePicFollowup.getFollowupStatus().getNameIn();
			}
		}
		
		this.catatanComplinace = trcFine.getNotes() != null ? trcFine.getNotesDecrypted() : "";
		this.followupNotes = trcFinePicFollowup.getFollowupNote() != null ? trcFinePicFollowup.getFollowupNote() : "";
		this.selectedRcId = trcFinePicFollowup.getRc() != null ? trcFinePicFollowup.getRc().getRcId().toString() : "";
		this.followupDate = trcFinePicFollowup.getFollowupDate() != null ? sdf.format(trcFinePicFollowup.getFollowupDate()) : "" ;
		this.selectedRC = trcFinePicFollowup.getRc() != null ? (trcFinePicFollowup.getRc().getRegionCode()+"-"+trcFinePicFollowup.getRc().getWorkingUnit()+"-"+trcFinePicFollowup.getRc().getRegion()) : "";
		this.targetDate = trcFinePicFollowup.getTargetDate() != null ? sdf.format(trcFinePicFollowup.getTargetDate()) : "" ;
		
		this.isRevise = true;
		if(trcFinePicFollowup.getTargetDateStatus() != null && trcFinePicFollowup.getTargetDateStatus()
				.getParameterDtlCode().equals(FineFEConstant.APPROVAL_STATUS_APPROVED)) {
			this.isRevise = false;
		}
		
		if(trcFinePicFollowup.getTrcFinePicFollowupAttachments() != null) {
			for (int j = 0; j < trcFinePicFollowup.getTrcFinePicFollowupAttachments().size() ; j++) {
				TrcFinePicFollowupAttachment pfa = trcFinePicFollowup.getTrcFinePicFollowupAttachments().get(j);
				if(pfa.getAttachmentFrom() != null && pfa.getAttachmentFrom().equals(ParameterDetail.FINE_FOLLOWUP_ATTACHMENT_TYPE_EXTENDED)) {
					UploadedFileWO uf = new UploadedFileWO();
					uf.setFileName(pfa.getAttachmentFile());
					uf.setFileId(pfa.getFileId());
					uf.setIsNew(false);
					uf.setFileSize(pfa.getFileSize());
					
					trcFinePicExtendeds.add(uf);
				}else {
					UploadedFileWO uf = new UploadedFileWO();
					uf.setFileName(pfa.getAttachmentFile());
					uf.setFileId(pfa.getFileId());
					uf.setIsNew(false);
					uf.setFileSize(pfa.getFileSize());
					
					trcFinePicFollowups.add(uf);
				}
			}
		}
	}
	
	public void cancel() {
		try {
			facesUtil.setSessionAttribute("BACK_SESSION", true);
			facesUtil.redirect("/pages/fineFE/fineFE.faces");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void downloadFile(String fileId, String fileName, byte[] content) throws Exception {
		CallApiManager.downloadFile(fileId, fileName, content, parameterDetailService);
	}
	
	public FineFEService getFineFEService() {
		return fineFEService;
	}

	public void setFineFEService(FineFEService fineFEService) {
		this.fineFEService = fineFEService;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public TrcFine getTrcFine() {
		return trcFine;
	}

	public void setTrcFine(TrcFine trcFine) {
		this.trcFine = trcFine;
	}

	public String getViewId() {
		return viewId;
	}

	public void setViewId(String viewId) {
		this.viewId = viewId;
	}

	public String getPengirim() {
		return pengirim;
	}

	public void setPengirim(String pengirim) {
		this.pengirim = pengirim;
	}

	public String getTanggalTerimaSurat() {
		return tanggalTerimaSurat;
	}

	public void setTanggalTerimaSurat(String tanggalTerimaSurat) {
		this.tanggalTerimaSurat = tanggalTerimaSurat;
	}

	public String getPerihal() {
		return perihal;
	}

	public void setPerihal(String perihal) {
		this.perihal = perihal;
	}

	public String getTanggalSurat() {
		return tanggalSurat;
	}

	public void setTanggalSurat(String tanggalSurat) {
		this.tanggalSurat = tanggalSurat;
	}

	public String getTipeSurat() {
		return tipeSurat;
	}

	public void setTipeSurat(String tipeSurat) {
		this.tipeSurat = tipeSurat;
	}

	public String getCatatanComplinace() {
		return catatanComplinace;
	}

	public void setCatatanComplinace(String catatanComplinace) {
		this.catatanComplinace = catatanComplinace;
	}

	public String getTglTindakLanjut() {
		return tglTindakLanjut;
	}

	public void setTglTindakLanjut(String tglTindakLanjut) {
		this.tglTindakLanjut = tglTindakLanjut;
	}

	public String getKeterangan() {
		return keterangan;
	}

	public void setKeterangan(String keterangan) {
		this.keterangan = keterangan;
	}

	public String getFollowupNotes() {
		return followupNotes;
	}

	public void setFollowupNotes(String followupNotes) {
		this.followupNotes = followupNotes;
	}

	public List<UploadedFileWO> getUploadedFiles() {
		return uploadedFiles;
	}

	public void setUploadedFiles(List<UploadedFileWO> uploadedFiles) {
		this.uploadedFiles = uploadedFiles;
	}

	public FacesUtil getFacesUtil() {
		return facesUtil;
	}

	public void setFacesUtil(FacesUtil facesUtil) {
		this.facesUtil = facesUtil;
	}

	public FileUtil getFileUtil() {
		return fileUtil;
	}

	public void setFileUtil(FileUtil fileUtil) {
		this.fileUtil = fileUtil;
	}

	public SimpleDateFormat getSdf() {
		return sdf;
	}

	public void setSdf(SimpleDateFormat sdf) {
		this.sdf = sdf;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public static Logger getLogger() {
		return logger;
	}

	public static String getNavigateBack() {
		return NAVIGATE_BACK;
	}

	public String getFollowupDate() {
		return followupDate;
	}

	public void setFollowupDate(String followupDate) {
		this.followupDate = followupDate;
	}

	public TrcFinePicFollowup getTrcFinePicFollowup() {
		return trcFinePicFollowup;
	}

	public void setTrcFinePicFollowup(TrcFinePicFollowup trcFinePicFollowup) {
		this.trcFinePicFollowup = trcFinePicFollowup;
	}

	public TrcFinePicFollowupService getTrcFinePicFollowupService() {
		return trcFinePicFollowupService;
	}

	public void setTrcFinePicFollowupService(TrcFinePicFollowupService trcFinePicFollowupService) {
		this.trcFinePicFollowupService = trcFinePicFollowupService;
	}

	public Integer getTestFirst() {
		return testFirst;
	}

	public void setTestFirst(Integer testFirst) {
		this.testFirst = testFirst;
	}

	public String getTextWarningUpload() {
		return textWarningUpload;
	}

	public void setTextWarningUpload(String textWarningUpload) {
		this.textWarningUpload = textWarningUpload;
	}

	public String getSelectedRcId() {
		return selectedRcId;
	}

	public void setSelectedRcId(String selectedRcId) {
		this.selectedRcId = selectedRcId;
	}

	public String getIsExtended() {
		return isExtended;
	}

	public void setIsExtended(String isExtended) {
		this.isExtended = isExtended;
	}

	public String getVerifyStatusName() {
		return verifyStatusName;
	}

	public void setVerifyStatusName(String verifyStatusName) {
		this.verifyStatusName = verifyStatusName;
	}

	public Boolean getIsRevise() {
		return isRevise;
	}

	public void setIsRevise(Boolean isRevise) {
		this.isRevise = isRevise;
	}

	public Boolean getVerifyActive() {
		return verifyActive;
	}

	public void setVerifyActive(Boolean verifyActive) {
		this.verifyActive = verifyActive;
	}

	public String getSelectedRC() {
		return selectedRC;
	}

	public void setSelectedRC(String selectedRC) {
		this.selectedRC = selectedRC;
	}

	

	public List<UploadedFileWO> getTrcFinePicExtendeds() {
		return trcFinePicExtendeds;
	}

	public void setTrcFinePicExtendeds(List<UploadedFileWO> trcFinePicExtendeds) {
		this.trcFinePicExtendeds = trcFinePicExtendeds;
	}

	public List<UploadedFileWO> getTrcFinePicFollowups() {
		return trcFinePicFollowups;
	}

	public void setTrcFinePicFollowups(List<UploadedFileWO> trcFinePicFollowups) {
		this.trcFinePicFollowups = trcFinePicFollowups;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getTargetDate() {
		return targetDate;
	}

	public void setTargetDate(String targetDate) {
		this.targetDate = targetDate;
	}
	
	
	
	
}
