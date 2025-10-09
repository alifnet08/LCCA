package com.wo.module.socializationFE.bean;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

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
import com.wo.module.externalRegulation.model.RegulationAttachment;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.regulationSocialization.model.SocializationDocumentTrc;
import com.wo.module.regulationSocialization.model.SocializationPICFollowupAttachmentTrc;
import com.wo.module.regulationSocialization.model.SocializationPICFollowupTrc;
import com.wo.module.regulationSocialization.model.SocializationRegulationTrc;
import com.wo.module.regulationSocialization.model.SocializationTrc;
import com.wo.module.regulationSocialization.service.SocializationPICFollowupTrcService;
import com.wo.module.socializationFE.constant.SocializationFEConstant;
import com.wo.module.socializationFE.service.SocializationFEService;
import com.wo.module.user.service.UserService;

public class SocializationFEViewBean extends CommonBean implements Serializable{

	private static final long serialVersionUID = 8133080842004289952L;
	private static final Logger logger = Logger.getLogger(SocializationFEEditBean.class);
	private static final String NAVIGATE_BACK = SocializationFEConstant.NAVIGATE_SOCIALIZATION_FE;
	
	private SocializationFEService socializationFEService;
	private UserService userService;
	private SocializationPICFollowupTrcService socializationPICFollowupTrcService;
	
	private SocializationTrc socializationTrc;
	private SocializationPICFollowupTrc socializationPICFollowupTrc;
	
	private String viewId;
	private String jenisPeraturan;
	private String tanggalTerbitKetentuan;
	private String nomorDokumen;
	private String tanggalEfektif;
	private String judulPeraturan;
	private String tanggalReview;
	private String tipePeraturan;
	private String catatan;
	private String followupDate;
	private String followupNote;
	private String targetDate;
	
	private List<UploadedFileWO> uploadedFiles;
	private List<UploadedFileWO> uploadedFileAttachs;
	
	private FacesUtil facesUtil;
	private FileUtil fileUtil;

	private SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
	
	private Integer testFirst;
	
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
		facesUtil.setSessionAttribute("FIRST_SOCIAL_FE", testFirst);
		facesUtil.setSessionAttribute("BACK_SESSION", false);
		checkNewOrEdit();
		fileUtil = FileUtil.getInstance();
	}
	
	private void checkNewOrEdit() {
		this.viewId = facesUtil.retrieveRequestParam("viewId");
		
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
		
		Long viewIdLong = Long.parseLong(viewId);
		uploadedFileAttachs = new ArrayList<UploadedFileWO>();
		uploadedFiles = new ArrayList<UploadedFileWO>();
		
		socializationPICFollowupTrc = socializationPICFollowupTrcService.findById(viewIdLong);
		socializationTrc = socializationFEService.findById(socializationPICFollowupTrc.getSocializationTrc().getSocializationId());
		
		this.judulPeraturan = socializationTrc.getJenisKetentuan().getNameIn() != null ? socializationTrc.getJenisKetentuan().getNameIn() : "";
		this.catatan = socializationPICFollowupTrc.getNotes() != null ? socializationPICFollowupTrc.getNotes() : "";
		this.targetDate = socializationPICFollowupTrc.getTargetDate() != null ? sdf.format(socializationPICFollowupTrc.getTargetDate()) : ""; 
		
		if (socializationTrc.getSocializationRegulationTrcs() != null) {
			for (int i = 0; i < socializationTrc.getSocializationRegulationTrcs().size(); i++) {
				SocializationRegulationTrc vo = (SocializationRegulationTrc) socializationTrc.getSocializationRegulationTrcs().get(i);
				if(vo.getPrimaryFlag()!=null && vo.getPrimaryFlag().equals(Constants.CONSTANT_YES)){
				this.tanggalTerbitKetentuan = vo.getRegulation().getPublishedDate() != null ? sdf.format(vo.getRegulation().getPublishedDate()) : "";
				this.nomorDokumen = vo.getRegulation().getDocumentNo() != null ? vo.getRegulation().getDocumentNo() : "";
				this.tanggalEfektif = vo.getRegulation().getEffectiveDate() != null ? sdf.format(vo.getRegulation().getEffectiveDate()) : "";
				this.judulPeraturan = vo.getRegulation().getNameIn() != null ? vo.getRegulation().getNameIn() : "";
				this.tipePeraturan = vo.getRegulation().getJenisKetentuan().getName() != null ? vo.getRegulation().getJenisKetentuan().getName() : "";
				}
			}
		}
		
		if (socializationTrc.getSocializationDocumentTrcs() != null) {
			for(int i=0;i<socializationTrc.getSocializationDocumentTrcs().size();i++){
				SocializationDocumentTrc socializationDocumentTrc = socializationTrc.getSocializationDocumentTrcs().get(i);
				UploadedFileWO uf = new UploadedFileWO();
				uf.setFileName(socializationDocumentTrc.getAttachmentFile());
				uf.setFileId(socializationDocumentTrc.getFileId());
				uf.setIsNew(false);
				uf.setFileSize(socializationDocumentTrc.getFileSize());
				uploadedFileAttachs.add(uf);
				
			}
		}
		
		if (socializationTrc.getSocializationPICFollowupTrcs() != null) {
			for (int i = 0; i < socializationTrc.getSocializationPICFollowupTrcs().size(); i++) {
				SocializationPICFollowupTrc vo = (SocializationPICFollowupTrc) socializationTrc.getSocializationPICFollowupTrcs().get(i);
				this.followupDate = vo.getFollowupDate() != null ? sdf.format(vo.getFollowupDate()) : "";
				this.followupNote = vo.getFollowupNote() != null ? vo.getFollowupNote() : "";
				this.catatan = vo.getFollowupNote() != null ? vo.getFollowupNote() : "";
				this.targetDate = vo.getTargetDate() != null ? sdf.format(vo.getTargetDate()) : "";
				if (vo.getSocializationPICFollowupAttachmentTrcs() != null) {
					for (int j = 0; j < vo.getSocializationPICFollowupAttachmentTrcs().size(); j++) {
						SocializationPICFollowupAttachmentTrc vo1 = (SocializationPICFollowupAttachmentTrc) vo.getSocializationPICFollowupAttachmentTrcs().get(i);
						UploadedFileWO uf = new UploadedFileWO();
						uf.setFileName(vo1.getAttachmentFile());
						uf.setFileId(vo1.getFileId());
						uf.setIsNew(false);
						uf.setFileSize(vo1.getFileSize());
						uploadedFiles.add(uf);
					}
				}
			}
		}
		
	}

	public void cancel() {
		try {
			
			facesUtil.setSessionAttribute("BACK_SESSION", true);
			facesUtil.redirect("/pages/socializationFE/socializationFE.faces");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void downloadFile(String fileId, String fileName, byte[] content) throws Exception {
		CallApiManager.downloadFile(fileId, fileName, content, parameterDetailService);
	}
	
	public SocializationFEService getSocializationFEService() {
		return socializationFEService;
	}

	public void setSocializationFEService(SocializationFEService socializationFEService) {
		this.socializationFEService = socializationFEService;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public SocializationTrc getSocializationTrc() {
		return socializationTrc;
	}

	public void setSocializationTrc(SocializationTrc socializationTrc) {
		this.socializationTrc = socializationTrc;
	}

	public String getViewId() {
		return viewId;
	}

	public void setViewId(String viewId) {
		this.viewId = viewId;
	}

	public String getJenisPeraturan() {
		return jenisPeraturan;
	}

	public void setJenisPeraturan(String jenisPeraturan) {
		this.jenisPeraturan = jenisPeraturan;
	}

	public String getTanggalTerbitKetentuan() {
		return tanggalTerbitKetentuan;
	}

	public void setTanggalTerbitKetentuan(String tanggalTerbitKetentuan) {
		this.tanggalTerbitKetentuan = tanggalTerbitKetentuan;
	}

	public String getNomorDokumen() {
		return nomorDokumen;
	}

	public void setNomorDokumen(String nomorDokumen) {
		this.nomorDokumen = nomorDokumen;
	}

	public String getTanggalEfektif() {
		return tanggalEfektif;
	}

	public void setTanggalEfektif(String tanggalEfektif) {
		this.tanggalEfektif = tanggalEfektif;
	}

	public String getJudulPeraturan() {
		return judulPeraturan;
	}

	public void setJudulPeraturan(String judulPeraturan) {
		this.judulPeraturan = judulPeraturan;
	}

	public String getTanggalReview() {
		return tanggalReview;
	}

	public void setTanggalReview(String tanggalReview) {
		this.tanggalReview = tanggalReview;
	}

	public String getTipePeraturan() {
		return tipePeraturan;
	}

	public void setTipePeraturan(String tipePeraturan) {
		this.tipePeraturan = tipePeraturan;
	}

	public String getCatatan() {
		return catatan;
	}

	public void setCatatan(String catatan) {
		this.catatan = catatan;
	}

	public List<UploadedFileWO> getUploadedFiles() {
		return uploadedFiles;
	}

	public void setUploadedFiles(List<UploadedFileWO> uploadedFiles) {
		this.uploadedFiles = uploadedFiles;
	}

	public List<UploadedFileWO> getUploadedFileAttachs() {
		return uploadedFileAttachs;
	}

	public void setUploadedFileAttachs(List<UploadedFileWO> uploadedFileAttachs) {
		this.uploadedFileAttachs = uploadedFileAttachs;
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

	public String getFollowupNote() {
		return followupNote;
	}

	public void setFollowupNote(String followupNote) {
		this.followupNote = followupNote;
	}

	public String getTargetDate() {
		return targetDate;
	}

	public void setTargetDate(String targetDate) {
		this.targetDate = targetDate;
	}

	public SocializationPICFollowupTrc getSocializationPICFollowupTrc() {
		return socializationPICFollowupTrc;
	}

	public void setSocializationPICFollowupTrc(SocializationPICFollowupTrc socializationPICFollowupTrc) {
		this.socializationPICFollowupTrc = socializationPICFollowupTrc;
	}

	public SocializationPICFollowupTrcService getSocializationPICFollowupTrcService() {
		return socializationPICFollowupTrcService;
	}

	public void setSocializationPICFollowupTrcService(
			SocializationPICFollowupTrcService socializationPICFollowupTrcService) {
		this.socializationPICFollowupTrcService = socializationPICFollowupTrcService;
	}

	public Integer getTestFirst() {
		return testFirst;
	}

	public void setTestFirst(Integer testFirst) {
		this.testFirst = testFirst;
	}
	
	
}
