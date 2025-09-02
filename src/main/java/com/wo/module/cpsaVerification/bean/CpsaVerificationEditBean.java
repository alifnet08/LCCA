package com.wo.module.cpsaVerification.bean;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.primefaces.PrimeFaces;
import org.primefaces.model.StreamedContent;

import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.exception.CustomAPIException;
import com.wo.module.common.model.UploadedFileWO;
import com.wo.module.common.util.FileUtil;
import com.wo.module.common.utility.CallApiManager;
import com.wo.module.common.vo.SendEmailVo;
import com.wo.module.counterType.model.CounterType;
import com.wo.module.counterType.service.CounterTypeService;
import com.wo.module.cpsa.constant.CompliancePlanSelfAssessmentConstant;
import com.wo.module.cpsa.model.CompliancePlanSelfAssessment;
import com.wo.module.cpsa.model.CompliancePlanSelfAssessmentPic;
import com.wo.module.cpsa.model.CompliancePlanSelfAssessmentPicTableModel;
import com.wo.module.cpsa.service.CompliancePlanSelfAssessmentService;
import com.wo.module.cpsaVerification.constant.CpsaVerificationConstant;
import com.wo.module.cpsaVerification.service.CpsaVerificationService;
import com.wo.module.cpsaVerification.vo.CpsaVerificationPicVo;
import com.wo.module.cpsaVerification.vo.CpsaVerificationQuestionVo;
import com.wo.module.emailTemplate.model.EmailTemplate;
import com.wo.module.emailTemplate.service.EmailTemplateService;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.lov.bean.SelectorListener;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.parameter.model.ParameterHeader;
import com.wo.module.user.model.Division;
import com.wo.module.user.model.User;
import com.wo.module.user.service.UserService;

public class CpsaVerificationEditBean extends CommonBean implements SelectorListener<Object>, Serializable{
 
	private static final long serialVersionUID = 4629902276647665851L;

	private static final Logger logger = Logger.getLogger(CpsaVerificationEditBean.class);
	
	private CompliancePlanSelfAssessmentService compliancePlanSelfAssessmentService;
	private CpsaVerificationService cpsaVerificationService;
	private EmailTemplateService emailTemplateService;
	
	private CompliancePlanSelfAssessment cpsa;
	private CpsaVerificationPicVo cpsaPicVo;	
	
	private Integer lastSequenceOfCpsaPic;
	private Integer indexDtlFollowup;
	
	private Long counterTypeId;
	
	private String editId;
	private String actionMode;
	private String textWarningUpload;
	private String textWarningUploadCpsa;
	private String followUpRemainder;
	private String renderedShow;
	private String cpsaTypeName;
	
	private List<SelectItem> cpsaTypeList;
	private List<SelectItem> unitKerjas;
	private List<SelectItem> counterTypes;
	private List<SelectItem> complianceStatusList;
	
	private List<UploadedFileWO> uploadedFiles;
	private List<UploadedFileWO> uploadedFileCpsas;
	private List<UploadedFileWO> deleteFiles;
	private List<UploadedFileWO> deleteFileCpsas;
		
	private FacesUtil facesUtil;
	private FileUtil fileUtil;
	
	private UserService userService;
	private CounterTypeService counterTypeService;
		
	private CompliancePlanSelfAssessmentPic[] selectedDataCpsaPic;
	private CompliancePlanSelfAssessmentPicTableModel<CompliancePlanSelfAssessmentPic> tableModelCpsaPic;
	private List<CpsaVerificationQuestionVo> cpsaQuestionVos;
	
	SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
	
	private List<CompliancePlanSelfAssessmentPic> compliancePlanSelfAssessmentPicList = new ArrayList<>();
	
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
		initComponent();
		
		checkNewOrEdit();
		selectUnitKerja();
		setupCpsaCounterType();
		selectComplianceStatus();		
		
		fileUtil = FileUtil.getInstance();
		tableModelCpsaPic = new CompliancePlanSelfAssessmentPicTableModel<CompliancePlanSelfAssessmentPic>(
				cpsa.getCpsaPics());
				
		try {
			ParameterDetail getText = parameterDetailService.getParameterDetailByParamDtlCode("UPLOAD_DOCUMENT_WARNING_TEXT");
			textWarningUpload = getText.getName();
			
			getText = parameterDetailService.getParameterDetailByParamDtlCode("UPLOAD_CPSA_WARNING_TEXT");
			textWarningUploadCpsa = getText.getName();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	private void initComponent() {
		initCpsaTypeList();
	}
	
	private void initCpsaTypeList() {
		try {
			cpsaTypeList = new ArrayList<SelectItem>();
			
			List<ParameterDetail> getCpsaType = parameterDetailService.getParameterDetailByParamCode(ParameterHeader.PARAM_HEAD_CPSA_TYPE);
			for (ParameterDetail pd : getCpsaType) {
				SelectItem si = new SelectItem();
				si.setLabel(pd.getName());
				si.setValue(pd.getParameterDtlCode());
				
				cpsaTypeList.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void selectUnitKerja() {
		unitKerjas = new ArrayList<SelectItem>();
		try {
			List<Division> pd = userService.getAllDivision();
			for (int i = 0; i < pd.size(); i++) {
				SelectItem si = new SelectItem();
				si.setLabel(((Division) pd.get(i)).getDivisionName());
				si.setValue(((Division) pd.get(i)).getDivisionId());
				unitKerjas.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		PrimeFaces.current().executeScript("reInitSelect2();");
	}
	
	public void setupCpsaCounterType() {
		try {
			counterTypes = counterTypeService.getAllCounterTypeLabelValue();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void selectComplianceStatus() {
		complianceStatusList = new ArrayList<SelectItem>();

		try {
			List<ParameterDetail> listComplianceDtl = parameterDetailService
					.getParameterDetailByParamCode(ParameterHeader.PARAM_HEAD_CODE_COMPLIANCE_CHECK_STATUS);

			for (ParameterDetail vo : listComplianceDtl) {
				if (!(vo.getParameterDtlCode().equals("COMPLIANCE_NOT_APPROPRIATE") 
						|| vo.getParameterDtlCode().equals("COMPLIANCE_APPROPRIATE"))) {
					SelectItem si = new SelectItem();
					si.setLabel(vo.getName());
					si.setValue(vo.getParameterDtlCode());
					complianceStatusList.add(si);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void checkNewOrEdit() {
		this.editId = facesUtil.retrieveRequestParam("editId");
		String token = facesUtil.retrieveRequestParam("token");
		
		if (StringUtils.isBlank(editId) && StringUtils.isBlank(token)) {
			handleNew();
		} else {
			handleEdit(editId);
		}
	}
	
	private void handleNew() {
		try {
			actionMode = Constants.ACTION_ADD;
			facesUtil.setSessionAttribute("token", null);
			Calendar setUploadDate = Calendar.getInstance();
			
			cpsa = new CompliancePlanSelfAssessment();
			cpsa.setCpsaType(new ParameterDetail());
			cpsa.setUploadDate(setUploadDate.getTime());
			cpsa.setCounterType(new CounterType());
			ParameterDetail parameterDetail = new ParameterDetail();
			parameterDetail.setParameterDtlCode(CompliancePlanSelfAssessmentConstant.COMPLIANCE_PLAN_SELF_ASSESSMENT_TYPE_CPSA);
			cpsa.setCpsaType(parameterDetail);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void handleEdit(String editId) {
		String token = facesUtil.retrieveRequestParam("token");
		if (StringUtils.isNotBlank(token)) {
			editId = Constants.decryptString(token);
		}
		Long editIdLong = Long.parseLong(editId);
		actionMode = Constants.ACTION_EDIT;
		facesUtil.setSessionAttribute("token", Constants.encryptString(editId));
		
		cpsa = compliancePlanSelfAssessmentService.findById(editIdLong);
		lastSequenceOfCpsaPic = cpsa.getCpsaPics().size();
		
		uploadedFiles = new ArrayList<UploadedFileWO>();
		UploadedFileWO uf = new UploadedFileWO();
		uf.setFileName(cpsa.getAttachmentFile());
		uf.setFileId(cpsa.getFileId());
		uf.setIsNew(false);
		uf.setFileSize(cpsa.getFileSize());
		uploadedFiles.add(uf);
		
		uploadedFileCpsas = new ArrayList<UploadedFileWO>();
		UploadedFileWO uf2 = new UploadedFileWO();
		uf2.setFileName(cpsa.getAttachmentQuestFile());
		uf2.setFileId(cpsa.getFileQuestId());
		uf2.setIsNew(false);
		uf2.setFileSize(cpsa.getFileQuestSize());
		uploadedFileCpsas.add(uf2);	
		
		if (cpsa != null && cpsa.getCpsaType() != null && StringUtils.isNotBlank(cpsa.getCpsaType().getParameterDtlCode())) {
			if (cpsa.getCpsaType().getParameterDtlCode().equals(ParameterDetail.PARAM_DET_COMPLIANCE_PLAN_SELF_ASSESSMENT_TYPE_SYARIAH)) {
				setCpsaTypeName(parameterDetailService.getParamDtlNameByParamDtlCode(ParameterDetail.PARAM_DET_CPSA_HEADER_SYARIAH));
			} else if(cpsa.getCpsaType().getParameterDtlCode().equals(ParameterDetail.PARAM_DET_COMPLIANCE_PLAN_SELF_ASSESSMENT_TYPE_CPSA)) {
				setCpsaTypeName(parameterDetailService.getParamDtlNameByParamDtlCode(ParameterDetail.PARAM_DET_CPSA_HEADER_CPSA));
			} else {
				setCpsaTypeName(parameterDetailService.getParamDtlNameByParamDtlCode(ParameterDetail.PARAM_DET_CPSA_HEADER_PUSAT));;
			}
		}
		
		for(CompliancePlanSelfAssessmentPic pic : cpsa.getCpsaPics()) {
//			pic.setBranchName(userService.getBranchNameByBranchCode(pic.getBranchCode()));
			pic.setBranchName(userService.getSubBranchNameByBranchCode(pic.getBranchCode()));
			System.out.println(pic.getBranchName());
		}
		
		renderedShow = CpsaVerificationConstant.SHOW_APPROVAL;
	}
	
	public void downloadFile(String fileId, String fileName, byte[] content) throws Exception {
		CallApiManager.downloadFileCpsa(fileId, fileName, content, parameterDetailService);
	}
		
	public void downloadFileCpsa(String fileId, String fileName, byte[] content) throws Exception {
		CallApiManager.downloadFileCpsa(fileId, fileName, content, parameterDetailService);
	}
	
	public void cancel() {
		try {
			facesUtil.redirect("/pages/cpsaVerification/"+CpsaVerificationConstant.NAVIGATE_CPSA_VERIFICATION);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
	
	public void save() {
		try {			
			User user = (User) facesUtil.getUserLogin();
			cpsaVerificationService.save(cpsa, user);
			facesUtil.redirect("/pages/cpsaVerification/"+CpsaVerificationConstant.NAVIGATE_CPSA_VERIFICATION);
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private boolean isValidate() {
		boolean flag = true;
		if(cpsa.getCpsaPics() !=null && cpsa.getCpsaPics().size() > 0) {
			Integer indexRow = 1;
			for(CompliancePlanSelfAssessmentPic dataPic : cpsa.getCpsaPics()) {
				if (!dataPic.isDisabledStatusCompliance()) {
					if(dataPic.getCpsaStatusTemp() !=null && dataPic.getCpsaStatusTemp().equals(CpsaVerificationConstant.STATUS_COMPLIANCE_OPEN)) {
						if(dataPic.getCpsaNote() == null || dataPic.getCpsaNote().equals(CpsaVerificationConstant.STRING_EMPTY)) {
							facesUtil.addErrMessage(facesUtil.retrieveMessage("formCompliancePlanSelfAssessmentNoteRequired", indexRow+""));
							flag = false;
						}
					}else {
//						facesUtil.addErrMessage(facesUtil.retrieveMessage("formCompliancePlanSelfAssessmentCpsaStatusRequired", indexRow+""));
//						flag = false;
					}
				}
				
				indexRow++;
			}					
		}				
		
		return flag;
	}
	
	public void submit() {
		try {			
			if(isValidate()) {
				int cpsaPicClose = 0;
				int totalCpsaPicClose = 0;
				
				if (cpsa.getCpsaPics() != null && !cpsa.getCpsaPics().isEmpty()) {
					totalCpsaPicClose = cpsa.getCpsaPics().size();
					
					for (CompliancePlanSelfAssessmentPic dataPic : cpsa.getCpsaPics()) {
						if (!dataPic.isCanEdit()) {
							if (dataPic.getCpsaStatusTemp() != null 
									&& (dataPic.getCpsaStatusTemp().equals(CpsaVerificationConstant.STATUS_COMPLIANCE_CLOSE)
											|| dataPic.getCpsaStatusTemp().equals(CpsaVerificationConstant.STATUS_COMPLIANCE_OPEN))) {
								compliancePlanSelfAssessmentPicList.add(dataPic);
							}
						}
						if (StringUtils.isNotBlank(dataPic.getCpsaStatusTemp()) && dataPic.getCpsaStatusTemp().equals(CpsaVerificationConstant.STATUS_COMPLIANCE_CLOSE)) {
							cpsaPicClose++;
						}
					}
				}
				
				if (cpsaPicClose == totalCpsaPicClose) {
					cpsa.setCpsaStatus("CPSA_COMPLETED");
				} else {
					cpsa.setCpsaStatus("CPSA_INPROGRESS");
				}
				
				User user = (User) facesUtil.getUserLogin();
				cpsaVerificationService.submit(cpsa, user);
				facesUtil.redirect("/pages/cpsaVerification/"+CpsaVerificationConstant.NAVIGATE_CPSA_VERIFICATION);
				
				 // from you sendEmail() method
			    ExecutorService emailExecutor = Executors.newCachedThreadPool();
		        emailExecutor.execute(new Runnable() {
		            @Override
		            public void run() {
		                try {
		                	sendEmail(user);
		                } catch (Exception e) {
		                    logger.error("send email failed", e);
		                }
		            }
		        });
			
			}			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unused")
	public void sendEmail(User adminCmt) {
		try {
			ParameterDetail pdHostName = parameterDetailService.getParameterDetailByParamDtlCode(Constants.HOST_NAME_APPLICATION);
			List<SendEmailVo> sendEmailList = new ArrayList<>();
			
			if (compliancePlanSelfAssessmentPicList != null && !compliancePlanSelfAssessmentPicList.isEmpty()) {
				for (int i = 0; i < compliancePlanSelfAssessmentPicList.size(); i++) {
					CompliancePlanSelfAssessmentPic dataPic = compliancePlanSelfAssessmentPicList.get(i);
					if (dataPic.getCpsaStatus() != null) {
						EmailTemplate emailTemplate = new EmailTemplate();
						SendEmailVo sendEmail = new SendEmailVo();
						String emailSubject = "";
						String emailContent = "";
						String emailTo = "";
						String emailCc = "";
						
						if (dataPic.getCpsaStatus().getParameterDtlCode().equals(CpsaVerificationConstant.STATUS_COMPLIANCE_CLOSE)) {
							emailTemplate = emailTemplateService.getEmailTemplateByEmailTemplateCode("EMAIL_CPSA_CLOSE");
							
							emailSubject = emailTemplate.getEmailSubject();
							emailSubject = emailSubject.replaceAll("perihal_surat", cpsa.getLetterAbout());
							
							emailContent = emailTemplate.getEmailContent();
							emailContent = emailContent.replaceAll("nama_pic", dataPic.getUser1().getName());
							
							String emailCc1 = adminCmt.getEmail();
							if(StringUtils.isEmpty(emailCc)) {
								emailCc = StringUtils.isNotBlank(emailCc1) ? emailCc1 : "";
							}
						} else {
							emailTemplate = emailTemplateService.getEmailTemplateByEmailTemplateCode("EMAIL_CPSA_OPEN");
							
							String token = Constants.encryptString(cpsa.getCpsaId().toString());
							String menuId = Constants.encryptString(Constants.MENU_ID_CPAS_FE);
							String urlLink = pdHostName.getNameIn().concat("pages/cpsaFE/compliancePlanSelfAssessmentFEEdit.faces?token="+token+"&menuId="+menuId);
							
							emailSubject = emailTemplate.getEmailSubject();
							emailSubject = emailSubject.replaceAll("perihal_surat", cpsa.getLetterAbout());
							
							emailContent = emailTemplate.getEmailContent();
							emailContent = emailContent.replaceAll("nama_pic", dataPic.getUser1().getName());
							emailContent = emailContent.replaceAll("url_link", urlLink);
						}
						
						emailTo = StringUtils.isNotBlank(dataPic.getUser1().getEmail()) ? dataPic.getUser1().getEmail() : "";
						
						if(StringUtils.isNotBlank(emailCc)) {
							emailCc = StringUtils.isNotBlank(dataPic.getUser2().getEmail()) ? emailCc.concat(",").concat(dataPic.getUser2().getEmail()) : "";
						}else {
							emailCc = StringUtils.isNotBlank(dataPic.getUser2().getEmail()) ? dataPic.getUser2().getEmail() : "";
						}
						
						if(StringUtils.isNotBlank(emailCc)) {
							emailCc = StringUtils.isNotBlank(dataPic.getUser3().getEmail()) ? emailCc.concat(",").concat(dataPic.getUser3().getEmail()) : "";
						}
						
						sendEmail.setEmailTo(emailTo);
						sendEmail.setEmailCc(emailCc);
						sendEmail.setSubject(emailSubject);
						sendEmail.setContent(emailContent);
						
						sendEmailList.add(sendEmail);
					}
				}
			}
			
			if (!sendEmailList.isEmpty()) {
				for (SendEmailVo sendEmailVo : sendEmailList) {
					ExecutorService emailExecutor = Executors.newCachedThreadPool();
					final String subject = sendEmailVo.getSubject();
					final String content = sendEmailVo.getContent();
					final String to = sendEmailVo.getEmailTo();
					final String cc = sendEmailVo.getEmailCc();
					
					if (StringUtils.isNotBlank(to)) {
						CallApiManager.sendEmailAPI(to,cc, subject, content, "EMAIL_CPSA_VERIFICATION", "true", parameterDetailService);
					}
				}
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new CustomAPIException(ex.getMessage()); 
		}
		
	}
	
	@Override
	public void itemSelected(String clientId, String widgetVar, Object selectedItem) {
		// TODO Auto-generated method stub
		
	}
	
	@SuppressWarnings("deprecation")
	public void getShowAnswerPic() {
		renderedShow = CpsaVerificationConstant.SHOW_VIEW;
		String cpsaId = facesUtil.retrieveRequestParam("cpsaId");
		String userId1 = facesUtil.retrieveRequestParam("userId1");
		String userApprovalId = facesUtil.retrieveRequestParam("userApprovalId");
		try {
			cpsaPicVo = cpsaVerificationService.getDataCpsaPic(new Long(cpsaId), new Long(userId1), new Long(userApprovalId));			
			User userData = userService.findById(cpsaPicVo.getUserId1());
			cpsaPicVo.setDirectorate(userData !=null?userData.getDirectorate():null);
		
			if (cpsa.getCpsaType() != null 
					&& (cpsa.getCpsaType().getParameterDtlCode().equals("COMPLIANCE_PLAN_SELF_ASSESSMENT_TYPE_SYARIAH")
							|| cpsa.getCpsaType().getParameterDtlCode().equals("COMPLIANCE_PLAN_SELF_ASSESSMENT_TYPE_CPSA"))) {
				cpsaPicVo.setDirectorate(userService.getRegionByBranchCode(cpsaPicVo.getBranchCode()));
//				cpsaPicVo.setDirectorate(userService.getSubBranchNameByBranchCode(cpsaPicVo.getBranchCode()));
				cpsaPicVo.setDivisionName(userService.getSubBranchNameByBranchCode(cpsaPicVo.getBranchCode()));
			} else {
				cpsaPicVo.setDirectorate(userService.getDirectorateByDivisionId(cpsaPicVo.getDivisionId()));
			}
			
			cpsaQuestionVos = new ArrayList<CpsaVerificationQuestionVo>();
			cpsaQuestionVos = cpsaVerificationService.getDataQuestion(cpsaPicVo.getCpsaId(), cpsaPicVo.getUserId1());
			
			PrimeFaces.current().executeScript("reInitSelect2();");
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void getBackAnswerPic() {
		renderedShow = CpsaVerificationConstant.SHOW_APPROVAL;
	}
	
	public StreamedContent getDownloadExcel() {
		StreamedContent downloadExcelSc = null;
		try {
			String cpsaId = facesUtil.retrieveRequestParam("cpsaId");
			String userId1 = facesUtil.retrieveRequestParam("userIdData1");
			downloadExcelSc = cpsaVerificationService.generateDataExcel(new Long(cpsaId), new Long(userId1));
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return downloadExcelSc;
	}

	
	public CompliancePlanSelfAssessment getCpsa() {
		return cpsa;
	}

	public void setCpsa(CompliancePlanSelfAssessment cpsa) {
		this.cpsa = cpsa;
	}

	public String getEditId() {
		return editId;
	}

	public void setEditId(String editId) {
		this.editId = editId;
	}

	public List<SelectItem> getCpsaTypeList() {
		return cpsaTypeList;
	}

	public void setCpsaTypeList(List<SelectItem> cpsaTypeList) {
		this.cpsaTypeList = cpsaTypeList;
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

	public static Logger getLogger() {
		return logger;
	}

	public String getActionMode() {
		return actionMode;
	}

	public void setActionMode(String actionMode) {
		this.actionMode = actionMode;
	}

	public CompliancePlanSelfAssessmentService getCompliancePlanSelfAssessmentService() {
		return compliancePlanSelfAssessmentService;
	}

	public void setCompliancePlanSelfAssessmentService(CompliancePlanSelfAssessmentService compliancePlanSelfAssessmentService) {
		this.compliancePlanSelfAssessmentService = compliancePlanSelfAssessmentService;
	}

	public List<UploadedFileWO> getUploadedFiles() {
		return uploadedFiles;
	}

	public void setUploadedFiles(List<UploadedFileWO> uploadedFiles) {
		this.uploadedFiles = uploadedFiles;
	}

	public List<UploadedFileWO> getDeleteFiles() {
		return deleteFiles;
	}

	public void setDeleteFiles(List<UploadedFileWO> deleteFiles) {
		this.deleteFiles = deleteFiles;
	}

	public String getTextWarningUpload() {
		return textWarningUpload;
	}

	public void setTextWarningUpload(String textWarningUpload) {
		this.textWarningUpload = textWarningUpload;
	}

	public List<SelectItem> getUnitKerjas() {
		return unitKerjas;
	}

	public void setUnitKerjas(List<SelectItem> unitKerjas) {
		this.unitKerjas = unitKerjas;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Integer getLastSequenceOfCpsaPic() {
		return lastSequenceOfCpsaPic;
	}

	public void setLastSequenceOfCpsaPic(Integer lastSequenceOfCpsaPic) {
		this.lastSequenceOfCpsaPic = lastSequenceOfCpsaPic;
	}

	public CompliancePlanSelfAssessmentPic[] getSelectedDataCpsaPic() {
		return selectedDataCpsaPic;
	}

	public void setSelectedDataCpsaPic(CompliancePlanSelfAssessmentPic[] selectedDataCpsaPic) {
		this.selectedDataCpsaPic = selectedDataCpsaPic;
	}

	public CompliancePlanSelfAssessmentPicTableModel<CompliancePlanSelfAssessmentPic> getTableModelCpsaPic() {
		return tableModelCpsaPic;
	}

	public void setTableModelCpsaPic(
			CompliancePlanSelfAssessmentPicTableModel<CompliancePlanSelfAssessmentPic> tableModelCpsaPic) {
		this.tableModelCpsaPic = tableModelCpsaPic;
	}

	public Long getCounterTypeId() {
		return counterTypeId;
	}

	public void setCounterTypeId(Long counterTypeId) {
		this.counterTypeId = counterTypeId;
	}

	public List<SelectItem> getCounterTypes() {
		return counterTypes;
	}

	public void setCounterTypes(List<SelectItem> counterTypes) {
		this.counterTypes = counterTypes;
	}

	public String getTextWarningUploadCpsa() {
		return textWarningUploadCpsa;
	}

	public void setTextWarningUploadCpsa(String textWarningUploadCpsa) {
		this.textWarningUploadCpsa = textWarningUploadCpsa;
	}

	public List<UploadedFileWO> getUploadedFileCpsas() {
		return uploadedFileCpsas;
	}

	public void setUploadedFileCpsas(List<UploadedFileWO> uploadedFileCpsas) {
		this.uploadedFileCpsas = uploadedFileCpsas;
	}

	public List<UploadedFileWO> getDeleteFileCpsas() {
		return deleteFileCpsas;
	}

	public void setDeleteFileCpsas(List<UploadedFileWO> deleteFileCpsas) {
		this.deleteFileCpsas = deleteFileCpsas;
	}

	public Integer getIndexDtlFollowup() {
		return indexDtlFollowup;
	}

	public void setIndexDtlFollowup(Integer indexDtlFollowup) {
		this.indexDtlFollowup = indexDtlFollowup;
	}

	public CpsaVerificationService getCpsaVerificationService() {
		return cpsaVerificationService;
	}

	public void setCpsaVerificationService(CpsaVerificationService cpsaVerificationService) {
		this.cpsaVerificationService = cpsaVerificationService;
	}

	public EmailTemplateService getEmailTemplateService() {
		return emailTemplateService;
	}

	public void setEmailTemplateService(EmailTemplateService emailTemplateService) {
		this.emailTemplateService = emailTemplateService;
	}

	public String getFollowUpRemainder() {
		return followUpRemainder;
	}

	public void setFollowUpRemainder(String followUpRemainder) {
		this.followUpRemainder = followUpRemainder;
	}

	public List<SelectItem> getComplianceStatusList() {
		return complianceStatusList;
	}

	public void setComplianceStatusList(List<SelectItem> complianceStatusList) {
		this.complianceStatusList = complianceStatusList;
	}

	public CounterTypeService getCounterTypeService() {
		return counterTypeService;
	}

	public void setCounterTypeService(CounterTypeService counterTypeService) {
		this.counterTypeService = counterTypeService;
	}

	public SimpleDateFormat getSdf() {
		return sdf;
	}

	public void setSdf(SimpleDateFormat sdf) {
		this.sdf = sdf;
	}

	public CpsaVerificationPicVo getCpsaPicVo() {
		return cpsaPicVo;
	}

	public void setCpsaPicVo(CpsaVerificationPicVo cpsaPicVo) {
		this.cpsaPicVo = cpsaPicVo;
	}

	public List<CpsaVerificationQuestionVo> getCpsaQuestionVos() {
		return cpsaQuestionVos;
	}

	public void setCpsaQuestionVos(List<CpsaVerificationQuestionVo> cpsaQuestionVos) {
		this.cpsaQuestionVos = cpsaQuestionVos;
	}

	public String getRenderedShow() {
		return renderedShow;
	}

	public void setRenderedShow(String renderedShow) {
		this.renderedShow = renderedShow;
	}

	public List<CompliancePlanSelfAssessmentPic> getCompliancePlanSelfAssessmentPicList() {
		return compliancePlanSelfAssessmentPicList;
	}

	public void setCompliancePlanSelfAssessmentPicList(List<CompliancePlanSelfAssessmentPic> compliancePlanSelfAssessmentPicList) {
		this.compliancePlanSelfAssessmentPicList = compliancePlanSelfAssessmentPicList;
	}

	public String getCpsaTypeName() {
		return cpsaTypeName;
	}

	public void setCpsaTypeName(String cpsaTypeName) {
		this.cpsaTypeName = cpsaTypeName;
	}

}