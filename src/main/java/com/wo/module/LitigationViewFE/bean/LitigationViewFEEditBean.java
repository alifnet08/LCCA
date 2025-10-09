package com.wo.module.LitigationViewFE.bean;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.wo.module.LitigationViewFE.constant.LitigationViewFEConstant;
import com.wo.module.LitigationViewFE.service.LitigationViewFEService;
import com.wo.module.LitigationViewFE.vo.LitigationAttachmentViewFEVo;
import com.wo.module.LitigationViewFE.vo.LitigationDetailViewFEVo;
import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.model.UploadedFileWO;
import com.wo.module.common.util.FileUtil;
import com.wo.module.common.utility.CallApiManager;
import com.wo.module.litigation.model.Litigation;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.user.service.UserService;

public class LitigationViewFEEditBean extends CommonBean implements Serializable{

	private static final long serialVersionUID = -4478773773838840947L;
	private static final Logger logger = Logger.getLogger(LitigationViewFEEditBean.class);
	private static final String NAVIGATE_BACK = LitigationViewFEConstant.NAVIGATE_LITIGATION_VIEW_FE;

	private LitigationViewFEService litigationViewFEService;
	private UserService userService;
	
	private Litigation litigation;
	
	private String viewId;
	
	private FacesUtil facesUtil;
	
	private FileUtil fileUtil;
	
	private List<UploadedFileWO> uploadedFilePN;
	private List<UploadedFileWO> uploadedFilePT;
	private List<UploadedFileWO> uploadedFileMA;
	
	private List<LitigationDetailViewFEVo> litigationDtlFELists;
	
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
		facesUtil.setSessionAttribute("FIRST_LITIG_FE", testFirst);
		facesUtil.setSessionAttribute("BACK_SESSION", false);
		checkNewOrEdit();
		fileUtil = FileUtil.getInstance();
	}

	private void checkNewOrEdit() {
		this.viewId = facesUtil.retrieveRequestParam("viewId");
		String token = facesUtil.retrieveRequestParam("token");
		if(StringUtils.isNotEmpty(token)) {
			viewId = Constants.decryptString(token);
		}
		
		if (StringUtils.isBlank(viewId)) {
			// do nothing
		} else {
			handleView(viewId);
		}
	}
	
	private void handleView(String viewId) {
		uploadedFilePN = new ArrayList<UploadedFileWO>();
		uploadedFilePT = new ArrayList<UploadedFileWO>();
		uploadedFileMA = new ArrayList<UploadedFileWO>();
		litigationDtlFELists = new ArrayList<LitigationDetailViewFEVo>();
		Long viewIdLong = Long.parseLong(viewId);
		
		litigation = litigationViewFEService.findById(viewIdLong);
		
		litigationDtlFELists = litigationViewFEService.getLitigationDtlListsByLitigationId(litigation.getLitigationId()) != null ?
				litigationViewFEService.getLitigationDtlListsByLitigationId(litigation.getLitigationId()) : new ArrayList<LitigationDetailViewFEVo>();
		
		for (int i = 0; i < litigationDtlFELists.size(); i++) {
			LitigationDetailViewFEVo vo = (LitigationDetailViewFEVo) litigationDtlFELists.get(i);
			if (vo.getCourtTypeCode().equals(ParameterDetail.PARAM_DET_CODE_LITIGATION_COURT_TYPE_PN)) {
				List<LitigationAttachmentViewFEVo> vo1 = litigationViewFEService.getLitigationAttachListsByLitigationIdAndCourtType(litigation.getLitigationId(), vo.getCourtTypeCode());
				if (vo1 != null && vo1.size() > 0) {
					LitigationAttachmentViewFEVo vo2 = (LitigationAttachmentViewFEVo) vo1.get(i);
					UploadedFileWO uf = new UploadedFileWO();
					uf.setFileName(vo2.getAttachmentFile());
					uf.setFileId(vo2.getFileId());
					uf.setIsNew(false);
					uf.setFileSize(vo2.getFileSize());
					uploadedFilePN.add(uf);
				}
			}
			
			if (vo.getCourtTypeCode().equals(ParameterDetail.PARAM_DET_CODE_LITIGATION_COURT_TYPE_PT)) {
				List<LitigationAttachmentViewFEVo> vo1 = litigationViewFEService.getLitigationAttachListsByLitigationIdAndCourtType(litigation.getLitigationId(), vo.getCourtTypeCode());
				if (vo1 != null && vo1.size() > 0) {
					LitigationAttachmentViewFEVo vo2 = (LitigationAttachmentViewFEVo) vo1.get(i);
					UploadedFileWO uf = new UploadedFileWO();
					uf.setFileName(vo2.getAttachmentFile());
					uf.setFileId(vo2.getFileId());
					uf.setIsNew(false);
					uf.setFileSize(vo2.getFileSize());
					uploadedFilePT.add(uf);
				}
			}
			
			if (vo.getCourtTypeCode().equals(ParameterDetail.PARAM_DET_CODE_LITIGATION_COURT_TYPE_MA)) {
				List<LitigationAttachmentViewFEVo> vo1 = litigationViewFEService.getLitigationAttachListsByLitigationIdAndCourtType(litigation.getLitigationId(), vo.getCourtTypeCode());
				if (vo1 != null && vo1.size() > 0) {
					LitigationAttachmentViewFEVo vo2 = (LitigationAttachmentViewFEVo) vo1.get(i);
					UploadedFileWO uf = new UploadedFileWO();
					uf.setFileName(vo2.getAttachmentFile());
					uf.setFileId(vo2.getFileId());
					uf.setIsNew(false);
					uf.setFileSize(vo2.getFileSize());
					uploadedFileMA.add(uf);
				}
			}
		}
	}
	
	public void cancel() {
		try {
			facesUtil.setSessionAttribute("BACK_SESSION", true);
			facesUtil.redirect("/pages/litigationViewFE/litigationViewFE.faces");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void downloadFile(String fileId, String fileName, byte[] content) throws Exception {
		CallApiManager.downloadFile(fileId, fileName, content, parameterDetailService);
	}
	
	public LitigationViewFEService getLitigationViewFEService() {
		return litigationViewFEService;
	}

	public void setLitigationViewFEService(LitigationViewFEService litigationViewFEService) {
		this.litigationViewFEService = litigationViewFEService;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public Litigation getLitigation() {
		return litigation;
	}

	public void setLitigation(Litigation litigation) {
		this.litigation = litigation;
	}

	public String getViewId() {
		return viewId;
	}

	public void setViewId(String viewId) {
		this.viewId = viewId;
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

	public List<UploadedFileWO> getUploadedFilePN() {
		return uploadedFilePN;
	}

	public void setUploadedFilePN(List<UploadedFileWO> uploadedFilePN) {
		this.uploadedFilePN = uploadedFilePN;
	}

	public List<UploadedFileWO> getUploadedFilePT() {
		return uploadedFilePT;
	}

	public void setUploadedFilePT(List<UploadedFileWO> uploadedFilePT) {
		this.uploadedFilePT = uploadedFilePT;
	}

	public List<UploadedFileWO> getUploadedFileMA() {
		return uploadedFileMA;
	}

	public void setUploadedFileMA(List<UploadedFileWO> uploadedFileMA) {
		this.uploadedFileMA = uploadedFileMA;
	}

	public List<LitigationDetailViewFEVo> getLitigationDtlFELists() {
		return litigationDtlFELists;
	}

	public void setLitigationDtlFELists(List<LitigationDetailViewFEVo> litigationDtlFELists) {
		this.litigationDtlFELists = litigationDtlFELists;
	}

	public Integer getTestFirst() {
		return testFirst;
	}

	public void setTestFirst(Integer testFirst) {
		this.testFirst = testFirst;
	}

}
