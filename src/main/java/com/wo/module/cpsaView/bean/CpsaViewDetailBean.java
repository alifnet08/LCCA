package com.wo.module.cpsaView.bean;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;

import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.model.UploadedFileWO;
import com.wo.module.common.util.FileUtil;
import com.wo.module.common.utility.CallApiManager;
import com.wo.module.counterType.service.CounterTypeService;
import com.wo.module.cpsa.constant.CompliancePlanSelfAssessmentConstant;
import com.wo.module.cpsa.model.CompliancePlanSelfAssessment;
import com.wo.module.cpsa.model.CompliancePlanSelfAssessmentPic;
import com.wo.module.cpsa.model.CompliancePlanSelfAssessmentPicTableModel;
import com.wo.module.cpsa.service.CompliancePlanSelfAssessmentService;
import com.wo.module.emailTemplate.service.EmailTemplateService;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.lov.bean.SelectorModel.SelectorInfo;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.parameter.model.ParameterHeader;
import com.wo.module.user.model.Division;
import com.wo.module.user.service.UserService;

public class CpsaViewDetailBean extends CommonBean{

	private static final long serialVersionUID = -141494243817324523L;

	private CompliancePlanSelfAssessmentService compliancePlanSelfAssessmentService;
	private UserService userService;
	private CounterTypeService counterTypeService;
	private EmailTemplateService emailTemplateService;
	
	private CompliancePlanSelfAssessment cpsa;
	
	private Integer lastSequenceOfCpsaPic;
	private Integer indexDtlFollowup;
	private Integer indexRowPicChange;
    private Integer indexDtlCompliancePiCFollowup;	
	private Integer indexDtlSubject;
	
	private Long counterTypeId;
	
	private Boolean isViewOnly;
	
	private String editId;
	private String actionMode;
	private String textWarningUpload;
	private String textWarningUploadCpsa;
	private String followUpRemainder;
	
	private List<SelectItem> cpsaTypeList;
	private List<SelectItem> unitKerjas;
	private List<SelectItem> counterTypes;
	
	private List<UploadedFileWO> uploadedFiles;
	private List<UploadedFileWO> uploadedFileCpsas;
	private List<UploadedFileWO> deleteFiles;
	private List<UploadedFileWO> deleteFileCpsas;
	
	private SelectorInfo selectorPic1;
	private SelectorInfo selectorPic2;
	private SelectorInfo selectorPic3;
	private SelectorInfo selectorBranch;
	
	private FacesUtil facesUtil;
	private FileUtil fileUtil;
	
	private FileUploadEvent fileUploadCpsa;
	
	private CompliancePlanSelfAssessmentPic[] selectedDataCpsaPic;
	private CompliancePlanSelfAssessmentPicTableModel<CompliancePlanSelfAssessmentPic> tableModelCpsaPic;
	
	SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
	
	private Boolean flagNewEdit;
			
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
				
		fileUtil = FileUtil.getInstance();
		
		selectorPic1 = CompliancePlanSelfAssessmentConstant.buildSelectorPIC(facesUtil);
		selectorPic2 = CompliancePlanSelfAssessmentConstant.buildSelectorPIC(facesUtil);
		selectorPic3 = CompliancePlanSelfAssessmentConstant.buildSelectorPIC(facesUtil);
		selectorBranch = CompliancePlanSelfAssessmentConstant.buildSelectorBranch(facesUtil);
		
		try {
			ParameterDetail getText = parameterDetailService.getParameterDetailByParamDtlCode("UPLOAD_WARNING_TEXT");
			textWarningUpload = getText.getName();
			
			getText = parameterDetailService.getParameterDetailByParamDtlCode("UPLOAD_WARNING_TEXT");
			textWarningUploadCpsa = getText.getName();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	private void initComponent() {
		initCpsaTypeList();
		selectUnitKerja();
		setupCpsaCounterType();
	}
	
	private void initCpsaTypeList() {
		try {
			cpsaTypeList = new ArrayList<>();
			
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
		unitKerjas = new ArrayList<>();
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
	
	private void checkNewOrEdit() {
		String editId = facesUtil.retrieveRequestParam("editId");
		String token = facesUtil.retrieveRequestParam("token"); 
		
		String viewId = facesUtil.retrieveRequestParam("viewId");
		isViewOnly = false;
		if (viewId != null && !viewId.isEmpty() &&  (viewId.trim().equalsIgnoreCase("true"))) {
				isViewOnly = true;
			
		}
		if (StringUtils.isBlank(editId) && StringUtils.isBlank(token)) {
//			this.handleNew();
		} else {
			this.handleEdit(editId);
		}
	}
	
	private void handleEdit(String editId) {
		String token = facesUtil.retrieveRequestParam("token");
		flagNewEdit = false;
		if (StringUtils.isNotBlank(token)) {
			editId = Constants.decryptString(token);
		}
		Long editIdLong = Long.parseLong(editId);
		actionMode = Constants.ACTION_EDIT;
		facesUtil.setSessionAttribute("token", Constants.encryptString(editId));
		
		cpsa = compliancePlanSelfAssessmentService.findById(editIdLong);		
		if (cpsa.getCpsaPics() != null) {
			lastSequenceOfCpsaPic = cpsa.getCpsaPics().size();
			for (int i = 0; i < cpsa.getCpsaPics().size(); i++) {
				CompliancePlanSelfAssessmentPic dtl = cpsa.getCpsaPics().get(i);
				lastSequenceOfCpsaPic = lastSequenceOfCpsaPic + 1;
				dtl.setSequence(lastSequenceOfCpsaPic);
				String branchName = userService.getBranchNameByBranchCode(dtl.getBranchCode());
				if(branchName !=null) {
					dtl.setBranchName(branchName);
				}
				dtl.setIsEditableTemp(false);
				cpsa.getCpsaPics().set(i, dtl);
			}
		}
		
		uploadedFiles = new ArrayList<>();
		UploadedFileWO uf = new UploadedFileWO();
		uf.setFileName(cpsa.getAttachmentFile());
		uf.setFileId(cpsa.getFileId());
		uf.setIsNew(false);
		uf.setFileSize(cpsa.getFileSize());
		uploadedFiles.add(uf);
		
		uploadedFileCpsas = new ArrayList<>();
		UploadedFileWO uf2 = new UploadedFileWO();
		uf2.setFileName(cpsa.getAttachmentQuestFile());
		uf2.setFileId(cpsa.getFileQuestId());
		uf2.setIsNew(false);
		uf2.setFileSize(cpsa.getFileQuestSize());
		uploadedFileCpsas.add(uf2);
		
		tableModelCpsaPic = new CompliancePlanSelfAssessmentPicTableModel<>(
				cpsa.getCpsaPics());
		
	}
	
	public void handleUploadFile(FileUploadEvent event) {
		try {
			if(uploadedFiles !=null) {
				uploadedFiles.remove(uploadedFiles);
			}
			
			uploadedFiles = new ArrayList<>();
			uploadedFiles.add( new UploadedFileWO(CallApiManager.callUploadAPIByFolder(event.getFile(), CompliancePlanSelfAssessmentConstant.CPSA_DOCUMENT, parameterDetailService, 
																		false, fileUtil, CompliancePlanSelfAssessmentConstant.FOLDER_CPSA),
					event.getFile().getFileName(), event.getFile().getContentType(), event.getFile().getSize()));
		
		} catch (Exception e) {
			e.printStackTrace();
			facesUtil.addErrMessage(e.getMessage());
		}
	}
	
	public void deleteAttachment(String fileId, int index, String uploadType) throws Exception {
		deleteFiles = new ArrayList<>();
		deleteFiles.add(new UploadedFileWO(fileId, null, null, null));
		
		uploadedFiles.remove(uploadedFiles.get(index));
	}
	
	public void downloadFile(String fileId, String fileName, byte[] content) throws Exception {
		CallApiManager.downloadFileCpsa(fileId, fileName, content, parameterDetailService);
	}
	
	@SuppressWarnings("unlikely-arg-type")
	public void handleUploadFileCpsa(FileUploadEvent event) {
		try {
			ParameterDetail pdHeaderQuestionCpsa = parameterDetailService.getParameterDetailByParamDtlCode(ParameterDetail.PARAM_DET_VALID_UPLOAD_QUESTION_CPSA);
			
			if(uploadedFileCpsas !=null) {
				uploadedFileCpsas.remove(uploadedFileCpsas);
			}
			
			if (StringUtils.isNotBlank(event.getFile().getFileName())) {
				String ext = FileUtil.getExtention(event.getFile().getFileName());
				
				if (!(ext.equalsIgnoreCase("xls") || ext.equalsIgnoreCase("xlsx"))) {
					facesUtil.addErrMessage(facesUtil.retrieveMessage("validFileMustBeExcel"));
					return;
				}
				
			}
			
			uploadedFileCpsas = new ArrayList<>();
			uploadedFileCpsas.add(new UploadedFileWO(CallApiManager.callUploadAPIByFolder(event.getFile(), CompliancePlanSelfAssessmentConstant.CPSA_DOCUMENT, parameterDetailService, 
						                                                                   false, fileUtil, CompliancePlanSelfAssessmentConstant.FOLDER_CPSA),
								                     event.getFile().getFileName(), event.getFile().getContentType(), event.getFile().getSize()));
			fileUploadCpsa = event;
			
			if (uploadedFileCpsas != null && !uploadedFileCpsas.isEmpty()) {
				for (int i = 0; i < uploadedFileCpsas.size(); i++) {
					ParameterDetail pdFilePath = parameterDetailService.getParameterDetailByParamDtlCode("ATTACHMENT_FILE_PATH");
					UploadedFileWO dataUpload = uploadedFileCpsas.get(i);
					
					if (dataUpload != null && StringUtils.isNotBlank(dataUpload.getFileId())) {
						Workbook workbook = createWorkbook(pdFilePath.getNameIn()+CompliancePlanSelfAssessmentConstant.FOLDER_CPSA+"/"+dataUpload.getFileId());
						Sheet sheet = workbook.getSheet("Kertas Kerja");
						Row row = sheet.getRow(11);
						Cell cell = row.getCell(1);
						
						if (!cell.getStringCellValue().contains(pdHeaderQuestionCpsa.getNameIn())) {
							facesUtil.addErrMessage(
									facesUtil.retrieveMessage("formCompliancePlanSelfAssessmentUploadCpsaHeaderContentFile", pdHeaderQuestionCpsa.getNameIn()));
							uploadedFileCpsas = new ArrayList<>();
							return;
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			facesUtil.addErrMessage(e.getMessage());
		}
	}
	
	private Workbook createWorkbook(String filepath) throws IOException {
		String ext = FileUtil.getExtention(filepath);
		if (StringUtils.equalsIgnoreCase("xls", ext)) {
			FileInputStream fis = new FileInputStream(filepath);
			return new HSSFWorkbook(fis);
		} else if (StringUtils.equalsIgnoreCase("xlsx", ext)) {
			try {
				return new XSSFWorkbook(new File(filepath));
			} catch (InvalidFormatException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			throw new RuntimeException("Extension not valid");
		}
		return null;
	}
	
	public void deleteAttachmentCpsa(String fileId, int index, String uploadType) throws Exception {
		deleteFileCpsas = new ArrayList<>();
		deleteFileCpsas.add(new UploadedFileWO(fileId, null, null, null));
		
		uploadedFileCpsas.remove(uploadedFileCpsas.get(index));
	}
	
	public void downloadFileCpsa(String fileId, String fileName, byte[] content) throws Exception {
		CallApiManager.downloadFileCpsa(fileId, fileName, content, parameterDetailService);
	}
	
	public void cancel() {
		try {
			facesUtil.redirect("/pages/cpsaView/"+CompliancePlanSelfAssessmentConstant.NAVIGATE_CPSA_VIEW);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	public CompliancePlanSelfAssessmentService getCompliancePlanSelfAssessmentService() {
		return compliancePlanSelfAssessmentService;
	}

	public void setCompliancePlanSelfAssessmentService(
			CompliancePlanSelfAssessmentService compliancePlanSelfAssessmentService) {
		this.compliancePlanSelfAssessmentService = compliancePlanSelfAssessmentService;
	}

	public CompliancePlanSelfAssessment getCpsa() {
		return cpsa;
	}

	public void setCpsa(CompliancePlanSelfAssessment cpsa) {
		this.cpsa = cpsa;
	}

	public Integer getLastSequenceOfCpsaPic() {
		return lastSequenceOfCpsaPic;
	}

	public void setLastSequenceOfCpsaPic(Integer lastSequenceOfCpsaPic) {
		this.lastSequenceOfCpsaPic = lastSequenceOfCpsaPic;
	}

	public Integer getIndexDtlFollowup() {
		return indexDtlFollowup;
	}

	public void setIndexDtlFollowup(Integer indexDtlFollowup) {
		this.indexDtlFollowup = indexDtlFollowup;
	}

	public Integer getIndexRowPicChange() {
		return indexRowPicChange;
	}

	public void setIndexRowPicChange(Integer indexRowPicChange) {
		this.indexRowPicChange = indexRowPicChange;
	}

	public Integer getIndexDtlCompliancePiCFollowup() {
		return indexDtlCompliancePiCFollowup;
	}

	public void setIndexDtlCompliancePiCFollowup(Integer indexDtlCompliancePiCFollowup) {
		this.indexDtlCompliancePiCFollowup = indexDtlCompliancePiCFollowup;
	}

	public Integer getIndexDtlSubject() {
		return indexDtlSubject;
	}

	public void setIndexDtlSubject(Integer indexDtlSubject) {
		this.indexDtlSubject = indexDtlSubject;
	}

	public Long getCounterTypeId() {
		return counterTypeId;
	}

	public void setCounterTypeId(Long counterTypeId) {
		this.counterTypeId = counterTypeId;
	}

	public String getEditId() {
		return editId;
	}

	public void setEditId(String editId) {
		this.editId = editId;
	}

	public String getActionMode() {
		return actionMode;
	}

	public void setActionMode(String actionMode) {
		this.actionMode = actionMode;
	}

	public String getTextWarningUpload() {
		return textWarningUpload;
	}

	public void setTextWarningUpload(String textWarningUpload) {
		this.textWarningUpload = textWarningUpload;
	}

	public String getTextWarningUploadCpsa() {
		return textWarningUploadCpsa;
	}

	public void setTextWarningUploadCpsa(String textWarningUploadCpsa) {
		this.textWarningUploadCpsa = textWarningUploadCpsa;
	}

	public String getFollowUpRemainder() {
		return followUpRemainder;
	}

	public void setFollowUpRemainder(String followUpRemainder) {
		this.followUpRemainder = followUpRemainder;
	}

	public List<SelectItem> getCpsaTypeList() {
		return cpsaTypeList;
	}

	public void setCpsaTypeList(List<SelectItem> cpsaTypeList) {
		this.cpsaTypeList = cpsaTypeList;
	}

	public List<SelectItem> getUnitKerjas() {
		return unitKerjas;
	}

	public void setUnitKerjas(List<SelectItem> unitKerjas) {
		this.unitKerjas = unitKerjas;
	}

	public List<SelectItem> getCounterTypes() {
		return counterTypes;
	}

	public void setCounterTypes(List<SelectItem> counterTypes) {
		this.counterTypes = counterTypes;
	}

	public List<UploadedFileWO> getUploadedFiles() {
		return uploadedFiles;
	}

	public void setUploadedFiles(List<UploadedFileWO> uploadedFiles) {
		this.uploadedFiles = uploadedFiles;
	}

	public List<UploadedFileWO> getUploadedFileCpsas() {
		return uploadedFileCpsas;
	}

	public void setUploadedFileCpsas(List<UploadedFileWO> uploadedFileCpsas) {
		this.uploadedFileCpsas = uploadedFileCpsas;
	}

	public List<UploadedFileWO> getDeleteFiles() {
		return deleteFiles;
	}

	public void setDeleteFiles(List<UploadedFileWO> deleteFiles) {
		this.deleteFiles = deleteFiles;
	}

	public List<UploadedFileWO> getDeleteFileCpsas() {
		return deleteFileCpsas;
	}

	public void setDeleteFileCpsas(List<UploadedFileWO> deleteFileCpsas) {
		this.deleteFileCpsas = deleteFileCpsas;
	}

	public SelectorInfo getSelectorPic1() {
		return selectorPic1;
	}

	public void setSelectorPic1(SelectorInfo selectorPic1) {
		this.selectorPic1 = selectorPic1;
	}

	public SelectorInfo getSelectorPic2() {
		return selectorPic2;
	}

	public void setSelectorPic2(SelectorInfo selectorPic2) {
		this.selectorPic2 = selectorPic2;
	}

	public SelectorInfo getSelectorPic3() {
		return selectorPic3;
	}

	public void setSelectorPic3(SelectorInfo selectorPic3) {
		this.selectorPic3 = selectorPic3;
	}

	public SelectorInfo getSelectorBranch() {
		return selectorBranch;
	}

	public void setSelectorBranch(SelectorInfo selectorBranch) {
		this.selectorBranch = selectorBranch;
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

	public FileUploadEvent getFileUploadCpsa() {
		return fileUploadCpsa;
	}

	public void setFileUploadCpsa(FileUploadEvent fileUploadCpsa) {
		this.fileUploadCpsa = fileUploadCpsa;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public CounterTypeService getCounterTypeService() {
		return counterTypeService;
	}

	public void setCounterTypeService(CounterTypeService counterTypeService) {
		this.counterTypeService = counterTypeService;
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

	public SimpleDateFormat getSdf() {
		return sdf;
	}

	public void setSdf(SimpleDateFormat sdf) {
		this.sdf = sdf;
	}

	public Boolean getFlagNewEdit() {
		return flagNewEdit;
	}

	public void setFlagNewEdit(Boolean flagNewEdit) {
		this.flagNewEdit = flagNewEdit;
	}

	public Boolean getIsViewOnly() {
		return isViewOnly;
	}

	public void setIsViewOnly(Boolean isViewOnly) {
		this.isViewOnly = isViewOnly;
	}

	public EmailTemplateService getEmailTemplateService() {
		return emailTemplateService;
	}

	public void setEmailTemplateService(EmailTemplateService emailTemplateService) {
		this.emailTemplateService = emailTemplateService;
	}
	
	
}
