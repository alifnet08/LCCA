package com.wo.module.tmpFaqApproval.bean;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.model.SelectItem;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.exception.CustomAPIException;
import com.wo.module.common.model.UploadedFileWO;
import com.wo.module.common.utility.CallApiManager;
import com.wo.module.externalRegulation.service.RegulationService;
import com.wo.module.faq.model.TmpFaq;
import com.wo.module.faq.model.TmpFaqDocument;
import com.wo.module.faq.model.TmpFaqKeyword;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.parameter.model.ParameterHeader;
import com.wo.module.tmpFaqApproval.constant.TmpFaqApprovalConstant;
import com.wo.module.tmpFaqApproval.model.TmpFaqApproval;
import com.wo.module.tmpFaqApproval.service.TmpFaqApprovalService;
import com.wo.module.user.service.UserService;

public class TmpFaqApprovalEditBean extends CommonBean implements Serializable{

	private static final long serialVersionUID = -4184414808159864551L;
	private static final Logger logger = Logger.getLogger(TmpFaqApprovalEditBean.class);
	private static final String NAVIGATE_BACK = TmpFaqApprovalConstant.TMP_FAQ_APPROVAL;
	
	private TmpFaqApprovalService tmpFaqApprovalService;
	
	private RegulationService regulationService;
	
	private UserService userService;
	
	private TmpFaq tmpFaq;
	
	private String actionMode;
	private String editId;
	private String approvalStatus;
	private String approvalNote;
	private Integer hits;
	
	private List<String> keywords;
	private List<SelectItem> categoryList;
	private List<SelectItem> institutionList;
	private List<SelectItem> statusList;
	private List<UploadedFileWO> uploadFiles;
	
	private FacesUtil facesUtil;
	
	@PostConstruct
	public void init() {
		super.init();
		initComponent();
		handleEdit();
	}
	
	public void initComponent() {
		try {
			categoryList = new ArrayList<SelectItem>();
			List<ParameterDetail> listCategory = parameterDetailService.getParameterDetailByParamCode(ParameterHeader.PARAM_HEAD_FAQ_CATEGORY);
			for (ParameterDetail vo : listCategory) {
				SelectItem si = new SelectItem();
				si.setLabel(vo.getName());
				si.setValue(vo.getParameterDtlCode());
				categoryList.add(si);
			}
			
			institutionList = new ArrayList<SelectItem>();
			List<ParameterDetail> listInstitution = parameterDetailService.getParameterDetailByParamCode(ParameterHeader.PARAM_HEAD_INSTITUTION_NAME);
			for (ParameterDetail vo : listInstitution) {
				SelectItem si = new SelectItem();
				si.setLabel(vo.getName());
				si.setValue(vo.getParameterDtlCode());
				institutionList.add(si);
			}
			
			statusList = new ArrayList<SelectItem>();
			statusList = parameterDetailService.getListLabelValue(ParameterHeader.PARAM_HEAD_CODE_APPROVAL_STATUS, false, true, facesUtil.retrieveDefaultLocale());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void handleEdit() {
		actionMode = Constants.ACTION_EDIT;
		editId = facesUtil.retrieveRequestParam("id");
		Long idLong = Long.parseLong(editId);
		
		tmpFaq = tmpFaqApprovalService.findById(idLong);
		
		tmpFaq.setDate(new Date(tmpFaq.getCreationDate().getTime()));
		
		if (tmpFaq.getInstitution() == null) {
			ParameterDetail pd = new ParameterDetail();
			tmpFaq.setInstitution(pd);
		}
		
		List<String> listKey = new ArrayList<String>();
		for (int i = 0; i < tmpFaq.getTmpFaqKeywords().size(); i++) {
			TmpFaqKeyword data = tmpFaq.getTmpFaqKeywords().get(i);
			listKey.add(data.getKeyword());
		}
		
		if (tmpFaq.getTmpFaqKeywords().size() > 0) {
			keywords = listKey;
		}
		
		uploadFiles = new ArrayList<UploadedFileWO>();
		for (int i = 0; i < tmpFaq.getTmpFaqDocuments().size(); i++) {
			TmpFaqDocument ra = tmpFaq.getTmpFaqDocuments().get(i);
			UploadedFileWO uf = new UploadedFileWO();
			uf.setFileName(ra.getAttachmentFile());
			uf.setFileId(ra.getFileId());
			uf.setIsNew(false);
			uf.setFileSize(ra.getFileSize());
			uploadFiles.add(uf);
		}
		
		try {
			hits = regulationService.getCountHitRegulation(idLong, "/compliance/pages/faqFE/faqFE.faces");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Boolean isValidate() {
		boolean flag = true;
		
		if (StringUtils.isBlank(approvalStatus)) {
			facesUtil.addErrMessage(facesUtil.retrieveMessage("textApprovalStatus") + " "
					+ facesUtil.retrieveMessage("validateRequired"));
			flag = false;
		}
		
		return flag;
	}
	
	public void save() {
		try {
			if (isValidate()) {
				if (approvalStatus.equals(ParameterDetail.PARAM_DET_CODE_STATUS_APPROVED)) {
					approve();
				} else if (approvalStatus.equals(ParameterDetail.PARAM_DET_CODE_STATUS_REVISE)) {
					revise();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("deprecation")
	private void approve() {
		try {
			if (tmpFaq.getTmpFaqApprovals() == null || tmpFaq.getTmpFaqApprovals().size() == 0) {
				tmpFaq.setTmpFaqApprovals(new ArrayList<TmpFaqApproval>());
			}
			
			tmpFaq.setStatus(ParameterDetail.PARAM_DET_CODE_DATA_ACTIVE);
			tmpFaq.setActiveStatus(ParameterDetail.PARAM_DET_CODE_DATA_ACTIVE);
			
			TmpFaqApproval appr = new TmpFaqApproval();
			appr.setTmpFaq(tmpFaq);
			appr.setApprovalDate(new java.util.Date());
			appr.setApprovalNote(approvalNote);
			appr.setApprovalStatus(parameterDetailService.getParameterDetailByParamDtlCode(ParameterDetail.PARAM_DET_CODE_DATA_ACTIVE));
			appr.setUser(userService.getUserByNik(facesUtil.retrieveUserLogin()));
			
			if (appr.getCreatedBy() == null) {
				appr.setCreatedBy(facesUtil.retrieveUserLogin());
				appr.setCreationDate(new Timestamp(new java.util.Date().getTime()));
			}
			
			appr.setDelId(new Long(0));
			appr.setEnabledFlag(Constants.CONSTANT_YES);
			
			tmpFaq.getTmpFaqApprovals().add(appr);
			
			if (tmpFaq.getFaqId() != null) {
				tmpFaq.setLastUpdateBy(facesUtil.retrieveUserLogin());
				tmpFaq.setLastUpdateDate(new Timestamp(new java.util.Date().getTime()));
				tmpFaq.setDelId(new Long(0));
				tmpFaq.setEnabledFlag(Constants.CONSTANT_YES);
			}
			
			tmpFaqApprovalService.update(tmpFaq);
			
			// this should be a singleton
	        ExecutorService emailExecutor = Executors.newCachedThreadPool();
			
	        // from you sendEmail() method
	        emailExecutor.execute(new Runnable() {
				
				@Override
				public void run() {
					try {
						sendEmailApprove();
					} catch (Exception e) {
						logger.error("send email failed", e);
					}
				}
			});
	        
	        facesUtil.redirect("/pages/tmpFaqApproval/tmpFaqApproval.faces");
	        
		} catch (Exception e) {
			e.printStackTrace();
			facesUtil.addFacesMsg(FacesMessage.SEVERITY_ERROR, null, "Operation Failed : " + e.getMessage(), "");
		}
	}
	
	@SuppressWarnings("deprecation")
	private void revise() {
		try {
			if (tmpFaq.getTmpFaqApprovals() == null || tmpFaq.getTmpFaqApprovals().size() == 0) {
				tmpFaq.setTmpFaqApprovals(new ArrayList<TmpFaqApproval>());
			}
			
			tmpFaq.setStatus(ParameterDetail.PARAM_DET_CODE_STATUS_REVISE);
			
			TmpFaqApproval appr = new TmpFaqApproval();
			appr.setTmpFaq(tmpFaq);
			appr.setApprovalDate(new java.util.Date());
			appr.setApprovalNote(approvalNote);
			appr.setApprovalStatus(parameterDetailService.getParameterDetailByParamDtlCode(ParameterDetail.PARAM_DET_CODE_STATUS_REVISE));
			appr.setUser(userService.getUserByNik(facesUtil.retrieveUserLogin()));
			
			if (appr.getCreatedBy() == null) {
				appr.setCreatedBy(facesUtil.retrieveUserLogin());
				appr.setCreationDate(new Timestamp(new java.util.Date().getTime()));
			}
			
			appr.setDelId(new Long(0));
			appr.setEnabledFlag(Constants.CONSTANT_YES);
			
			tmpFaq.getTmpFaqApprovals().add(appr);
			
			if (tmpFaq.getFaqId() != null) {
				tmpFaq.setLastUpdateBy(facesUtil.retrieveUserLogin());
				tmpFaq.setLastUpdateDate(new Timestamp(new java.util.Date().getTime()));
				tmpFaq.setDelId(new Long(0));
				tmpFaq.setEnabledFlag(Constants.CONSTANT_YES);
			}
			
			tmpFaqApprovalService.update(tmpFaq);
			
			// this should be a singleton
	        ExecutorService emailExecutor = Executors.newCachedThreadPool();
			
	        // from you sendEmail() method
	        emailExecutor.execute(new Runnable() {
				
				@Override
				public void run() {
					try {
						sendEmailReject();
					} catch (Exception e) {
						logger.error("send email failed", e);
					}
				}
			});
	        
	        facesUtil.redirect("/pages/tmpFaqApproval/tmpFaqApproval.faces");
	        
		} catch (Exception e) {
			e.printStackTrace();
			facesUtil.addFacesMsg(FacesMessage.SEVERITY_ERROR, null, "Operation Failed : " + e.getMessage(), "");
		}
	}
	
	public void sendEmailApprove() {
		try {
			String emailSubject = "LCCA Approval Notification - FAQ";
			String emailContent = ""; 
			String emailTo  = "";
			String emailCc = "";
			
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < keywords.size(); i++) {
				sb.append(keywords.get(i));
			}
			
			emailContent = "("+sb.toString()+") telah disetujui <br/><br/>";
			if (tmpFaq.getTmpFaqApprovals() != null && tmpFaq.getTmpFaqApprovals().size() > 0) {
				emailContent = emailContent+"Catatan : <br/>";
				emailContent = emailContent+tmpFaq.getTmpFaqApprovals().get(tmpFaq.getTmpFaqApprovals().size()-1).getApprovalNote()+"";
			}
			emailTo = userService.getUserByNik(tmpFaq.getCreatedBy()).getEmail();
			
			final String subject = emailSubject;
			final String content = emailContent;
			final String to = emailTo;
			final String cc = emailCc;
			
			CallApiManager.sendEmailAPI(to, cc, subject, content, "EMAIL_FAQ", "true", parameterDetailService);
			
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new CustomAPIException(ex.getMessage()); 
		}
	}
	
	public void sendEmailReject() {
		try {
			String emailSubject = "LCCA Approval Notification - FAQ";
			String emailContent = ""; 
			String emailTo  = "";
			String emailCc = "";
			
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < keywords.size(); i++) {
				sb.append(keywords.get(i));
			}
			
			emailContent = "("+sb.toString()+") telah ditolak <br/><br/>";
			if (tmpFaq.getTmpFaqApprovals() != null && tmpFaq.getTmpFaqApprovals().size() > 0) {
				emailContent = emailContent+"Catatan : <br/>";
				emailContent = emailContent+tmpFaq.getTmpFaqApprovals().get(tmpFaq.getTmpFaqApprovals().size()-1).getApprovalNote()+"";
			}
			emailTo = userService.getUserByNik(tmpFaq.getCreatedBy()).getEmail();
			
			final String subject = emailSubject;
			final String content = emailContent;
			final String to = emailTo;
			final String cc = emailCc;
			
			CallApiManager.sendEmailAPI(to, cc, subject, content, "EMAIL_FAQ", "true", parameterDetailService);
			
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new CustomAPIException(ex.getMessage()); 
		}
	}
	
	public void cancel() {
		try {
			facesUtil.redirect("/pages/tmpFaqApproval/tmpFaqApproval.faces");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public TmpFaqApprovalService getTmpFaqApprovalService() {
		return tmpFaqApprovalService;
	}

	public void setTmpFaqApprovalService(TmpFaqApprovalService tmpFaqApprovalService) {
		this.tmpFaqApprovalService = tmpFaqApprovalService;
	}

	public TmpFaq getTmpFaq() {
		return tmpFaq;
	}

	public void setTmpFaq(TmpFaq tmpFaq) {
		this.tmpFaq = tmpFaq;
	}

	public String getActionMode() {
		return actionMode;
	}

	public void setActionMode(String actionMode) {
		this.actionMode = actionMode;
	}

	public String getEditId() {
		return editId;
	}

	public void setEditId(String editId) {
		this.editId = editId;
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

	public static String getNavigateBack() {
		return NAVIGATE_BACK;
	}

	public List<String> getKeywords() {
		return keywords;
	}

	public void setKeywords(List<String> keywords) {
		this.keywords = keywords;
	}

	public Integer getHits() {
		return hits;
	}

	public void setHits(Integer hits) {
		this.hits = hits;
	}

	public List<SelectItem> getCategoryList() {
		return categoryList;
	}

	public void setCategoryList(List<SelectItem> categoryList) {
		this.categoryList = categoryList;
	}

	public List<SelectItem> getInstitutionList() {
		return institutionList;
	}

	public void setInstitutionList(List<SelectItem> institutionList) {
		this.institutionList = institutionList;
	}

	public List<UploadedFileWO> getUploadFiles() {
		return uploadFiles;
	}

	public void setUploadFiles(List<UploadedFileWO> uploadFiles) {
		this.uploadFiles = uploadFiles;
	}

	public RegulationService getRegulationService() {
		return regulationService;
	}

	public void setRegulationService(RegulationService regulationService) {
		this.regulationService = regulationService;
	}

	public List<SelectItem> getStatusList() {
		return statusList;
	}

	public void setStatusList(List<SelectItem> statusList) {
		this.statusList = statusList;
	}

	public String getApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(String approvalStatus) {
		this.approvalStatus = approvalStatus;
	}

	public String getApprovalNote() {
		return approvalNote;
	}

	public void setApprovalNote(String approvalNote) {
		this.approvalNote = approvalNote;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

}
