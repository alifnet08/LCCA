package com.wo.module.cpsa.bean;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.exception.CustomAPIException;
import com.wo.module.common.model.UploadedFileWO;
import com.wo.module.common.util.FileUtil;
import com.wo.module.common.util.MathUtil;
import com.wo.module.common.utility.CallApiManager;
import com.wo.module.common.vo.SendEmailVo;
import com.wo.module.counterType.model.CounterType;
import com.wo.module.counterType.service.CounterTypeService;
import com.wo.module.cpsa.constant.CompliancePlanSelfAssessmentConstant;
import com.wo.module.cpsa.model.CompliancePlanSelfAssessment;
import com.wo.module.cpsa.model.CompliancePlanSelfAssessmentPic;
import com.wo.module.cpsa.model.CompliancePlanSelfAssessmentPicTableModel;
import com.wo.module.cpsa.service.CompliancePlanSelfAssessmentService;
import com.wo.module.emailTemplate.model.EmailTemplate;
import com.wo.module.emailTemplate.service.EmailTemplateService;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.lov.bean.SelectorListener;
import com.wo.module.lov.bean.SelectorModel.SelectorInfo;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.parameter.model.ParameterHeader;
import com.wo.module.user.model.Division;
import com.wo.module.user.model.User;
import com.wo.module.user.service.UserService;

public class CompliancePlanSelfAssessmentEditBean extends CommonBean implements SelectorListener<Object>, Serializable{

	private static final long serialVersionUID = 7673727190722031028L;
	private static final Logger logger = Logger.getLogger(CompliancePlanSelfAssessmentEditBean.class);
	
	private CompliancePlanSelfAssessmentService compliancePlanSelfAssessmentService;
	
	private CompliancePlanSelfAssessment cpsa;
	
	private Integer lastSequenceOfCpsaPic;
	private Integer indexDtlFollowup;
	private Integer indexRowPicChange;
    private Integer indexDtlCompliancePiCFollowup;	
	private Integer indexDtlSubject;
	
	private Long counterTypeId;
	
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
	
	private UserService userService;
	private CounterTypeService counterTypeService;
	private EmailTemplateService emailTemplateService;
	
	private CompliancePlanSelfAssessmentPic[] selectedDataCpsaPic;
	private CompliancePlanSelfAssessmentPicTableModel<CompliancePlanSelfAssessmentPic> tableModelCpsaPic;
	
	private List<CompliancePlanSelfAssessmentPic> dataCpsaDeleteList;
	
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
		selectUnitKerja();
		setupCpsaCounterType();
				
		fileUtil = FileUtil.getInstance();
		tableModelCpsaPic = new CompliancePlanSelfAssessmentPicTableModel<CompliancePlanSelfAssessmentPic>(
				cpsa.getCpsaPics());
		
		selectorPic1 = CompliancePlanSelfAssessmentConstant.buildSelectorPIC(facesUtil);
		selectorPic2 = CompliancePlanSelfAssessmentConstant.buildSelectorPIC(facesUtil);
		selectorPic3 = CompliancePlanSelfAssessmentConstant.buildSelectorPIC(facesUtil);
		selectorBranch = CompliancePlanSelfAssessmentConstant.buildSelectorBranch(facesUtil);
		
		dataCpsaDeleteList = new ArrayList<CompliancePlanSelfAssessmentPic>();
		
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

	private void checkNewOrEdit() {
		this.editId = facesUtil.retrieveRequestParam("editId");
		String token = facesUtil.retrieveRequestParam("token");
		if (StringUtils.isBlank(editId) && StringUtils.isBlank(token)) {
			handleNew();
			flagNewEdit = true;
		} else {
			handleEdit(editId);
			flagNewEdit = false;
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
		if (cpsa.getCpsaPics() != null) {
			lastSequenceOfCpsaPic = cpsa.getCpsaPics().size();
			for (int i = 0; i < cpsa.getCpsaPics().size(); i++) {
				CompliancePlanSelfAssessmentPic dtl = cpsa.getCpsaPics().get(i);
				lastSequenceOfCpsaPic = lastSequenceOfCpsaPic + 1;
				dtl.setSequence(lastSequenceOfCpsaPic);
//				String branchName = userService.getBranchNameByBranchCode(dtl.getBranchCode());
				String subBranchName = userService.getSubBranchNameByBranchCode(dtl.getBranchCode());
				if(subBranchName !=null) {
					dtl.setBranchName(subBranchName);
				}
				dtl.setIsEditableTemp(false);
				
				dtl.setIsCanLock(false);
				if (!StringUtils.isEmpty(dtl.getLockFlag()) && dtl.getLockFlag().equals(Constants.CONSTANT_YES)) {
					dtl.setIsCanLock(true);
				}
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
		
	}
	
	public void lockCpsa(int index, Long cpsaPicId) {
		compliancePlanSelfAssessmentService.saveLockCPSAPic(cpsaPicId, facesUtil.retrieveUserLogin());
		cpsa.getCpsaPics().get(index).setIsCanLock(true);
		
		PrimeFaces.current().ajax().update("form:dataTablePicCpsa:"+index+":lock");
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public boolean isValidate() {
		boolean flag = true;
		
		Calendar periodFromCalendar = Calendar.getInstance();
		Calendar periodToCalendar = Calendar.getInstance();
		periodFromCalendar.setTime(cpsa.getPeriodFrom());
		periodToCalendar.setTime(cpsa.getPeriodTo());
		
		int periodFromYear = periodFromCalendar.get(Calendar.YEAR);
		int periodToYear = periodToCalendar.get(Calendar.YEAR);
		int periodFromMonth = periodFromCalendar.get(Calendar.MONTH);
		int periodToMonth = periodToCalendar.get(Calendar.MONTH);
		int periodFromDay = periodFromCalendar.get(Calendar.DAY_OF_MONTH);
		int periodToDay = periodToCalendar.get(Calendar.DAY_OF_MONTH);
		
		if (periodToYear < periodFromYear) {
			facesUtil.addErrMessage(facesUtil.retrieveMessage("formCompliancePlanSelfAssessmentPeriodTo") + " "
					+ facesUtil.retrieveMessage("validateDateByYearMustBigger") + " "
					+ facesUtil.retrieveMessage("formCompliancePlanSelfAssessmentPeriodFrom"));
			flag = false;
		} else {		
			if (periodToMonth == periodFromMonth) {
				facesUtil.addErrMessage(facesUtil.retrieveMessage("formCompliancePlanSelfAssessmentPeriodFrom") + " "
						+ facesUtil.retrieveMessage("and") + " "
						+ facesUtil.retrieveMessage("formCompliancePlanSelfAssessmentPeriodTo")
						+ facesUtil.retrieveMessage("validateDateByMonthMustSame"));
				flag = false;				
			} else if (periodToMonth < periodFromMonth) {
				facesUtil.addErrMessage(facesUtil.retrieveMessage("formCompliancePlanSelfAssessmentPeriodTo") + " "
						+ facesUtil.retrieveMessage("validateDateByMonthMustBigger") + " "
						+ facesUtil.retrieveMessage("formCompliancePlanSelfAssessmentPeriodFrom"));
				flag = false;
			} else {
				if (periodToYear == periodFromYear) {
					if (periodToMonth == periodFromMonth) {
						if (periodToDay < periodFromDay) {
							facesUtil.addErrMessage(facesUtil.retrieveMessage("formCompliancePlanSelfAssessmentPeriodTo") + " "
									+ facesUtil.retrieveMessage("validateDateByDaysMustBigger") + " "
									+ facesUtil.retrieveMessage("formCompliancePlanSelfAssessmentPeriodFrom"));
							flag = false;
						}
					}
				}
			}
		}
		
		//Add by Malik [18/02/2023]
		if (uploadedFiles == null || uploadedFiles.size() == 0) {
			facesUtil.addErrMessage(facesUtil.retrieveMessage("formCompliancePlanSelfAssessmentEntryDoc") + " "
					+ facesUtil.retrieveMessage("validateUploadMinOneData"));
			flag = false;
		}
		
		//Add by Malik [18/02/2023]
		if (uploadedFileCpsas == null || uploadedFileCpsas.size() == 0) {
			facesUtil.addErrMessage(facesUtil.retrieveMessage("formCompliancePlanSelfAssessmentCPSADoc") + " "
					+ facesUtil.retrieveMessage("validateUploadMinOneData"));
			flag = false;
		}
		
		//Add by Jovan [10/11/2023]
		if(cpsa.getCpsaBranchSubBranch() == null || cpsa.getCpsaBranchSubBranch().trim().equals(StringUtils.EMPTY)) {
			facesUtil.addErrMessage("Branch/ Sub Branch" + " "
					+ facesUtil.retrieveMessage("validateRequired"));
			flag = false;
		}
		
		Map mapCpsaPic = new HashMap();
		Integer indexRow = 1;
		if (cpsa.getCpsaPics() != null) {
			for (int i = 0; i < cpsa.getCpsaPics().size(); i++) {
				CompliancePlanSelfAssessmentPic dtl = (CompliancePlanSelfAssessmentPic) cpsa.getCpsaPics().get(i);
				
				if(dtl.getDivisionId() == null) {
					facesUtil.addErrMessage(facesUtil.retrieveMessage("formCompliancePlanSelfAssessmentWorkUnitRequired", indexRow+""));
					flag = false;
				}
				
				if(dtl.getUser1() !=null && dtl.getUser1().getUserId() !=null) {
					if(mapCpsaPic.get(dtl.getUser1().getName().trim()) == null){
						mapCpsaPic.put(dtl.getUser1().getName().trim(), dtl.getUser1().getName().trim());	
					}else{													
						facesUtil.addErrMessage(facesUtil.retrieveMessage("formCompliancePlanSelfAssessmentPIC1validateNotSame", dtl.getUser1().getName().trim()));
						flag = false;	
					}
				}else {
					facesUtil.addErrMessage(facesUtil.retrieveMessage("formCompliancePlanSelfAssessmentPIC1validateRequired", indexRow+""));
					flag = false;
				}
				
				if(dtl.getTargetDate() == null) {
					facesUtil.addErrMessage(facesUtil.retrieveMessage("formCompliancePlanSelfAssessmentTargetDateRequired", indexRow+""));
					flag = false;
				}
				
				indexRow++;
			}
		}else {
			// add 10 may 2023 ayu
			facesUtil.addErrMessage(facesUtil.retrieveMessage("formCompliancePlanSelfAssessmentPICFollowup") + " "
					+ facesUtil.retrieveMessage("validateDetailMinOneData"));
			flag = false;
		}
				
		return flag;
	}
	
	@SuppressWarnings("resource")
	public StreamedContent getTemplateUploadCPSA() {
		Workbook wb = null;
		ByteArrayInputStream bais = null;
		ByteArrayOutputStream baos = null;
		DefaultStreamedContent streamContent = null;
		
		InputStream is = this.getClass().getClassLoader().getResourceAsStream(CompliancePlanSelfAssessmentConstant.TEMPLATE_FILE_UPLOAD_CPSA);
		
		try {
			wb = new XSSFWorkbook(is);
		} catch (IOException e) {
			logger.error("Error while trying to read template file", e);
			return null;
		} finally {
			try {
				is.close();
			} catch (IOException e) {}
		}
		
		try {
			baos = new ByteArrayOutputStream();
			wb.write(baos);
		} catch (IOException e) {
			if (baos != null) {
				try {
					baos.close();
				} catch (IOException e2) {}
			}
			logger.error("Error while trying to write to mem output stream", e);
			return null;
		}

		try {
			bais = new ByteArrayInputStream(baos.toByteArray());
			streamContent = new DefaultStreamedContent(bais, "application/xls", "templateUploadCpsa.xlsx");
		} catch (Exception e) {
			logger.error("Error while trying to create stream content", e);
		} finally {
			try {
				baos.close();
				bais.close();
			} catch (IOException e) {
				// do nothing
			}
		}
		
		return streamContent;
	}
	
	@SuppressWarnings("unused")
	public void save() {
		try {
			if (isValidate()) {
				ParameterDetail pdCpsaType = parameterDetailService.getParameterDetailByParamDtlCode(cpsa.getCpsaType().getParameterDtlCode());
				cpsa.setCpsaType(pdCpsaType);
				
				if (uploadedFiles != null) {
					for (int i = 0; i < uploadedFiles.size(); i++) {
						UploadedFileWO uf = uploadedFiles.get(i);
						
						cpsa.setAttachmentFile(uf.getFileName());
						cpsa.setFileId(uf.getFileId());
						cpsa.setFileSize(uf.getFileSize());
					}
				}
				
				if(Boolean.TRUE.equals(flagNewEdit)) {
					if(uploadedFileCpsas !=null) {
						for (int i = 0; i < uploadedFileCpsas.size(); i++) {
							UploadedFileWO uf = uploadedFileCpsas.get(i);
							cpsa.setAttachmentQuestFile(uf.getFileName());
							cpsa.setFileQuestId(uf.getFileId());
							cpsa.setFileQuestSize(uf.getFileSize());						
						}					
						
						String ext = FileUtil.getExtention(fileUploadCpsa.getFile().getFileName()); 
						boolean flag = true;
						if (StringUtils.equalsIgnoreCase("xls", ext) || StringUtils.equalsIgnoreCase("xlsx", ext)) { 
							flag = true;
						}else {
							facesUtil.addErrMessage(facesUtil.retrieveMessage("textInfoUploadExcel"));
						}
					}
				}
				compliancePlanSelfAssessmentService.saveData(cpsa, fileUploadCpsa, facesUtil.retrieveUserLogin(), flagNewEdit, dataCpsaDeleteList);
				
				User adminCmt = userService.getUserByNik(facesUtil.getUserLogin().getNik());
				
				// this should be a singleton
		        ExecutorService emailExecutor = Executors.newCachedThreadPool();

		        // from you sendEmail() method
		        emailExecutor.execute(new Runnable() {
		            @Override
		            public void run() {
		                try {
		                	sendEmail(adminCmt);
		                } catch (Exception e) {
		                    logger.error("send email failed", e);
		                }
		            }
		        });
				
				
				facesUtil.redirect("/pages/cpsa/"+CompliancePlanSelfAssessmentConstant.NAVIGATE_CPSA);
			}
		} catch (Exception e) {
			facesUtil.addFacesMsg(FacesMessage.SEVERITY_ERROR, null, "Operation Failed : " + e.getMessage(), "");
		}
	}
		
	@SuppressWarnings("unlikely-arg-type")
	public void handleUploadFile(FileUploadEvent event) {
		try {
			if(uploadedFiles !=null) {
				uploadedFiles.remove(uploadedFiles);
			}
			
			uploadedFiles = new ArrayList<UploadedFileWO>();
			uploadedFiles.add( new UploadedFileWO(CallApiManager.callUploadAPIByFolder(event.getFile(), CompliancePlanSelfAssessmentConstant.CPSA_DOCUMENT, parameterDetailService, 
																		false, fileUtil, CompliancePlanSelfAssessmentConstant.FOLDER_CPSA),
					event.getFile().getFileName(), event.getFile().getContentType(), event.getFile().getSize()));
		
		} catch (Exception e) {
			e.printStackTrace();
			facesUtil.addErrMessage(e.getMessage());
		}
	}
	
	public void deleteAttachment(String fileId, int index, String uploadType) throws Exception {
		deleteFiles = new ArrayList<UploadedFileWO>();
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
			
			uploadedFileCpsas = new ArrayList<UploadedFileWO>();
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
		deleteFileCpsas = new ArrayList<UploadedFileWO>();
		deleteFileCpsas.add(new UploadedFileWO(fileId, null, null, null));
		
		uploadedFileCpsas.remove(uploadedFileCpsas.get(index));
	}
	
	public void downloadFileCpsa(String fileId, String fileName, byte[] content) throws Exception {
		CallApiManager.downloadFileCpsa(fileId, fileName, content, parameterDetailService);
	}
	
	public void cancel() {
		try {
			facesUtil.redirect("/pages/cpsa/"+CompliancePlanSelfAssessmentConstant.NAVIGATE_CPSA);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void onAddNewCpsaPic() {
		if (cpsa.getCpsaPics() == null
				|| cpsa.getCpsaPics().size() == 0) {
			cpsa.setCpsaPics(new ArrayList<>());
			lastSequenceOfCpsaPic = 0;
		}  else {
			if(cpsa.getCpsaPics().size() == 0) {
				lastSequenceOfCpsaPic = 0;
			}			
		} 

		CompliancePlanSelfAssessmentPic rt = new CompliancePlanSelfAssessmentPic();
		lastSequenceOfCpsaPic = lastSequenceOfCpsaPic + 1;
		rt.setSequence(lastSequenceOfCpsaPic);
		rt.setIsEditableTemp(true);
		rt.setIsCanLock(true);
		cpsa.getCpsaPics().add(rt);
		
		tableModelCpsaPic.setWrappedData(cpsa.getCpsaPics());
		
		PrimeFaces.current().executeScript("reInitSelect2();");
		//RequestContext.getCurrentInstance().execute("reInitSelect2();");

	}

	public void onDeleteRowCpsaPic() {
		for (int i = 0; i < selectedDataCpsaPic.length; i++) {
			dataCpsaDeleteList.add(selectedDataCpsaPic[i]);
			cpsa.getCpsaPics().remove(selectedDataCpsaPic[i]);
		}
		
		if (cpsa.getCpsaPics() == null
				|| cpsa.getCpsaPics().size() == 0) {
			lastSequenceOfCpsaPic = 0;
		}

		tableModelCpsaPic.setWrappedData(cpsa.getCpsaPics());
		
		PrimeFaces.current().executeScript("reInitSelect2();");
		//RequestContext.getCurrentInstance().execute("reInitSelect2();");
	}
	
	public void onChangeUnitKerjaPic(int i) {		
		CompliancePlanSelfAssessmentPic data = cpsa.getCpsaPics().get(i);
		data.setUser1(null);
		data.setUser2(null);
		data.setUser3(null);
				
		PrimeFaces.current().executeScript("initSelect2();");		
	}
	
	public void clearPicDetail2(int i) {	
		cpsa.getCpsaPics().get(i).setUser2(null);
		PrimeFaces.current().executeScript("initSelect2();");
	}
	
	public void clearPicDetail3(int i) {	
		cpsa.getCpsaPics().get(i).setUser3(null);
		PrimeFaces.current().executeScript("initSelect2();");
	}
	
	public void onTargetDateChange(int rowIdx) {
		followUpRemainder = "Y";
		PrimeFaces.current().executeScript("initSelect2();");
	}
			
	@SuppressWarnings("deprecation")
	@Override
	public void itemSelected(String clientId, String widgetVar, Object selectedItem) {
		 if (StringUtils.equals("pic1Dialog", widgetVar)) {
			Object[] objects = (Object[]) selectedItem;
			if (indexDtlFollowup == null) indexDtlFollowup = new Integer(0);
			User user = userService.findById(MathUtil.returnIdObjectToLong(objects[0])); 
			cpsa.getCpsaPics().get(indexDtlFollowup).setUser1(user);
			
			User user2 = userService.getUserByNik(user.getPukNik());
			if (user2 != null) {
				/*cpsa.getCpsaPics().get(indexDtlFollowup).setUser2(user2);

				User user3 = userService.getUserByNik(user2.getPukNik());
				if (user3 != null) {
					cpsa.getCpsaPics().get(indexDtlFollowup).setUser3(user3);
				}*/
				cpsa.getCpsaPics().get(indexDtlFollowup).setUser3(user2);
			}

			tableModelCpsaPic.setWrappedData(cpsa.getCpsaPics());
			
			PrimeFaces.current().ajax().update("form:dataTablePicCpsa:"+indexDtlFollowup+":userName1");
			//PrimeFaces.current().ajax().update("form:dataTablePicCpsa:"+indexDtlFollowup+":userName2");
			PrimeFaces.current().ajax().update("form:dataTablePicCpsa:"+indexDtlFollowup+":userName3");
		} else if (StringUtils.equals("pic2Dialog", widgetVar)) {
			Object[] objects = (Object[]) selectedItem;
			if (indexDtlFollowup == null) indexDtlFollowup = new Integer(0);			
			User user = userService.findById(MathUtil.returnIdObjectToLong(objects[0]));
			cpsa.getCpsaPics().get(indexDtlFollowup).setUser2(user);
			
			/*User user3 = userService.getUserByNik(user.getPukNik());
			if (user3 != null) {
				cpsa.getCpsaPics().get(indexDtlFollowup).setUser3(user3);
			}*/

			tableModelCpsaPic.setWrappedData(cpsa.getCpsaPics());
			PrimeFaces.current().ajax().update("form:dataTablePicCpsa:"+indexDtlFollowup+":userName2");
			//PrimeFaces.current().ajax().update("form:dataTablePicCpsa:"+indexDtlFollowup+":userName3");
		} else if (StringUtils.equals("pic3Dialog", widgetVar)) {
			Object[] objects = (Object[]) selectedItem;
			
			if (indexDtlFollowup == null) indexDtlFollowup = new Integer(0);
			User user = userService.findById(MathUtil.returnIdObjectToLong(objects[0]));
			cpsa.getCpsaPics().get(indexDtlFollowup).setUser3(user);

			tableModelCpsaPic.setWrappedData(cpsa.getCpsaPics());
			PrimeFaces.current().ajax().update("form:dataTablePicCpsa:"+indexDtlFollowup+":userName3");
		} else if (StringUtils.equals("branchDialog", widgetVar)) {
			Object[] objects = (Object[]) selectedItem;
			
			cpsa.getCpsaPics().get(indexDtlFollowup).setBranchCode((String)objects[1]);
			cpsa.getCpsaPics().get(indexDtlFollowup).setBranchName((String)objects[3]);
			tableModelCpsaPic.setWrappedData(cpsa.getCpsaPics());
			
			PrimeFaces.current().ajax().update("form:dataTablePicCpsa:"+indexDtlFollowup+":branchCode");
			PrimeFaces.current().ajax().update("form:dataTablePicCpsa:"+indexDtlFollowup+":branchName");
		
		}
		 
		PrimeFaces.current().executeScript("initSelect2();");
		//RequestContext.getCurrentInstance().execute("initSelect2();");
		
	}
	
	@SuppressWarnings("unused")
	public void sendEmail(User adminCmt) {
		try {
			List<CompliancePlanSelfAssessmentPic> cpsaPicEdit = new ArrayList<>();
			List<SendEmailVo> sendEmailList = new ArrayList<>();
			EmailTemplate emailTemplate = emailTemplateService.getEmailTemplateByEmailTemplateCode("EMAIL_CPSA");
			ParameterDetail pdHostName = parameterDetailService.getParameterDetailByParamDtlCode(Constants.HOST_NAME_APPLICATION);
			SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
			
			if (actionMode.equalsIgnoreCase(Constants.ACTION_EDIT)) {
				if (!cpsa.getCpsaPics().isEmpty()) {
					for (int i = 0; i < cpsa.getCpsaPics().size(); i++) {
						if (Boolean.TRUE.equals(cpsa.getCpsaPics().get(i).getIsEditableTemp())) {
							cpsaPicEdit.add(cpsa.getCpsaPics().get(i));
						}
					}
				}
			}
			
			String token = Constants.encryptString(cpsa.getCpsaId().toString());
			String menuId = Constants.encryptString(Constants.MENU_ID_CPAS_FE);
			String urlLink = pdHostName.getNameIn().concat("pages/cpsaFE/compliancePlanSelfAssessmentFEEdit.faces?token="+token+"&menuId="+menuId);
			
			String emailSubject = emailTemplate.getEmailSubject().replaceAll("perihal_surat", cpsa.getLetterAbout());
			String emailContent = emailTemplate.getEmailContent();
			
			if (actionMode.equalsIgnoreCase(Constants.ACTION_ADD)) {
				if (!cpsa.getCpsaPics().isEmpty()) {
					for (int i = 0; i < cpsa.getCpsaPics().size(); i++) {
						CompliancePlanSelfAssessmentPic data = cpsa.getCpsaPics().get(i);
						SendEmailVo sendEmail = new SendEmailVo();
						
						String emailTo = "";
						String emailCc = "";
						String emailCc1 = "";
						String emailCc2 = "";
						String emailCc3 = "";
						String targetDateStr = data.getTargetDate() != null ? sdf.format(data.getTargetDate()) : "N/A";
						
						emailContent = emailContent.replaceAll("nama_pic", data.getUser1().getName());
						emailContent = emailContent.replaceAll("target_date", targetDateStr);
						emailContent = emailContent.replaceAll("url_link", urlLink);
						
						emailTo = data.getUser1() != null && StringUtils.isNotBlank(data.getUser1().getEmail()) ? data.getUser1().getEmail() : "";
						// PIC 2 is now an Alternate/ Secondary PIC
						// PIC 3 is now an Approval/ PUK
						emailCc1 = adminCmt.getEmail() != null && StringUtils.isNotBlank(adminCmt.getEmail()) ? adminCmt.getEmail() : "" ;
						emailCc2 = data.getUser3() != null && StringUtils.isNotBlank(data.getUser3().getEmail()) ? data.getUser3().getEmail() : "";
						emailCc3 = data.getUser2() != null && StringUtils.isNotBlank(data.getUser2().getEmail()) ? data.getUser2().getEmail() : "";
						
						if(StringUtils.isEmpty(emailCc)) {
							emailCc = StringUtils.isNotBlank(emailCc1) ? emailCc1 : "";
						}else {
							emailCc = StringUtils.isNotBlank(emailCc1) ? emailCc.concat(",").concat(emailCc1) : emailCc.concat(emailCc1);
						}
						
						emailCc = StringUtils.isNotBlank(emailCc2) ? emailCc.concat(",").concat(emailCc2) : emailCc.concat(emailCc2);
						emailCc = StringUtils.isNotBlank(emailCc3) ? emailCc.concat(",").concat(emailCc3) : emailCc.concat(emailCc3);
//						System.out.println(emailCc);
						
						// Comment By Alex [23-08-2023]
//						if (cpsa.getCounterType() != null 
//								&& cpsa.getCounterType().getDetails() != null 
//								&& cpsa.getCounterType().getDetails().size() > 0) {
//							CounterTypeDtl dataCounterTypeDtl = cpsa.getCounterType().getDetails().get(0);
//							
//							emailTo = dataCounterTypeDtl.getEmailTo();
//							emailCc1 = dataCounterTypeDtl.getEmailCc1();
//							emailCc2 = dataCounterTypeDtl.getEmailCc2();
//							
//							if(emailTo.equals(Constants.REMINDER_PIC1)) {
//								emailTo = data.getUser1() !=null ? data.getUser1().getEmail() : null;
//							} else if(emailTo.equals(Constants.REMINDER_PIC2)) {
//								emailTo = data.getUser2() !=null ? data.getUser2().getEmail() : null;
//							} else if(emailTo.equals(Constants.REMINDER_PIC3)) {
//								emailTo = data.getUser3() !=null ? data.getUser3().getEmail() : null;
//							}
//							
//							if(emailCc1 != null && emailCc1.equals(Constants.REMINDER_PIC1)) {
//								emailCc1 = data.getUser1() !=null ? data.getUser1().getEmail() : null;
//							} else if(emailCc1 !=null && emailCc1.equals(Constants.REMINDER_PIC2)) {
//								emailCc1 = data.getUser2() !=null ? data.getUser2().getEmail() : null;
//							} else if(emailCc1 != null && emailCc1.equals(Constants.REMINDER_PIC3)) {
//								emailCc1 = data.getUser3() !=null ? data.getUser3().getEmail() : null;
//							}
//							
//							if(emailCc2 !=null && emailCc2.equals(Constants.REMINDER_PIC1)) {
//								emailCc2 = data.getUser1() !=null ? data.getUser1().getEmail() : null;
//							} else if(emailCc2 !=null && emailCc2.equals(Constants.REMINDER_PIC2)) {
//								emailCc2 = data.getUser2() !=null ? data.getUser2().getEmail() : null;
//							} else if(emailCc2!=null && emailCc2.equals(Constants.REMINDER_PIC3)) {
//								emailCc2 = data.getUser3() !=null ? data.getUser3().getEmail() : null;
//							}
//						} else {
//							emailTo = data.getUser1() !=null ? data.getUser1().getEmail() : null;
//							emailCc1 = data.getUser2() !=null ? data.getUser2().getEmail() : null;
//							emailCc2 = data.getUser3() !=null ? data.getUser3().getEmail() : null;
//						}
						// Comment By Alex [23-08-2023]
						
						sendEmail.setEmailTo(emailTo);
						sendEmail.setEmailCc(emailCc);
						sendEmail.setSubject(emailSubject);
						sendEmail.setContent(emailContent);
						
						emailContent = emailTemplate.getEmailContent();
						
						if (StringUtils.isNotBlank(emailTo)) {
							sendEmailList.add(sendEmail);							
						}
					}
				}
			} else {
				if (!cpsaPicEdit.isEmpty()) {
					for (int i = 0; i < cpsaPicEdit.size(); i++) {
						CompliancePlanSelfAssessmentPic data = cpsaPicEdit.get(i);
						SendEmailVo sendEmail = new SendEmailVo();
						
						String emailTo = "";
						String emailCc = "";
						String emailCc1 = "";
						String emailCc2 = "";
						String targetDateStr = data.getTargetDate() != null ? sdf.format(data.getTargetDate()) : "N/A";
						
						emailContent = emailContent.replaceAll("nama_pic", data.getUser1().getName());
						emailContent = emailContent.replaceAll("target_date", targetDateStr);
						emailContent = emailContent.replaceAll("url_link", urlLink);
						
						emailTo = data.getUser1() != null && StringUtils.isNotBlank(data.getUser1().getEmail()) ? data.getUser1().getEmail() : "";
						// PIC 2 is now an Alternate/ Secondary PIC
						if(data.getUser2() != null && StringUtils.isNotBlank(data.getUser2().getEmail())) {
							emailTo = emailTo.concat(",").concat(data.getUser2().getEmail());
						}
						//PIC 3 is now Approval PIC
						emailCc1 = data.getUser3() != null && StringUtils.isNotBlank(data.getUser3().getEmail()) ? data.getUser3().getEmail() : "";
						
//						emailCc1 = data.getUser2() != null && StringUtils.isNotBlank(data.getUser2().getEmail()) ? data.getUser2().getEmail() : "";
						
						// Comment By Alex [23-08-2023]
//						if (cpsa.getCounterType() != null 
//								&& cpsa.getCounterType().getDetails() != null 
//								&& cpsa.getCounterType().getDetails().size() > 0) {
//							CounterTypeDtl dataCounterTypeDtl = cpsa.getCounterType().getDetails().get(0);
//							
//							emailTo = dataCounterTypeDtl.getEmailTo();
//							emailCc1 = dataCounterTypeDtl.getEmailCc1();
//							emailCc2 = dataCounterTypeDtl.getEmailCc2();
//							
//							if(emailTo.equals(Constants.REMINDER_PIC1)) {
//								emailTo = data.getUser1() !=null ? data.getUser1().getEmail() : null;
//							} else if(emailTo.equals(Constants.REMINDER_PIC2)) {
//								emailTo = data.getUser2() !=null ? data.getUser2().getEmail() : null;
//							} else if(emailTo.equals(Constants.REMINDER_PIC3)) {
//								emailTo = data.getUser3() !=null ? data.getUser3().getEmail() : null;
//							}
//							
//							if(emailCc1 != null && emailCc1.equals(Constants.REMINDER_PIC1)) {
//								emailCc1 = data.getUser1() !=null ? data.getUser1().getEmail() : null;
//							} else if(emailCc1 !=null && emailCc1.equals(Constants.REMINDER_PIC2)) {
//								emailCc1 = data.getUser2() !=null ? data.getUser2().getEmail() : null;
//							} else if(emailCc1 != null && emailCc1.equals(Constants.REMINDER_PIC3)) {
//								emailCc1 = data.getUser3() !=null ? data.getUser3().getEmail() : null;
//							}
//							
//							if(emailCc2 !=null && emailCc2.equals(Constants.REMINDER_PIC1)) {
//								emailCc2 = data.getUser1() !=null ? data.getUser1().getEmail() : null;
//							} else if(emailCc2 !=null && emailCc2.equals(Constants.REMINDER_PIC2)) {
//								emailCc2 = data.getUser2() !=null ? data.getUser2().getEmail() : null;
//							} else if(emailCc2!=null && emailCc2.equals(Constants.REMINDER_PIC3)) {
//								emailCc2 = data.getUser3() !=null ? data.getUser3().getEmail() : null;
//							}
//						} else {
//							emailTo = data.getUser1() !=null ? data.getUser1().getEmail() : null;
//							emailCc1 = data.getUser2() !=null ? data.getUser2().getEmail() : null;
//							emailCc2 = data.getUser3() !=null ? data.getUser3().getEmail() : null;
//						}
						// Comment By Alex [23-08-2023]
						
						if(StringUtils.isNotEmpty(emailCc1)) {
							emailCc = emailCc.concat(emailCc1);
						}
						
						emailCc2 = adminCmt.getEmail();
						if(StringUtils.isNotEmpty(emailCc2)) {
							emailCc = StringUtils.isNotEmpty(emailCc) ? emailCc.concat(",").concat(emailCc2) : emailCc.concat(emailCc2);
						}
						
						String emailCc3 = data.getUser2() != null && StringUtils.isNotBlank(data.getUser2().getEmail()) ? data.getUser2().getEmail() : "";
						if(StringUtils.isNotEmpty(emailCc3)) {
							emailCc = StringUtils.isNotEmpty(emailCc) ? emailCc.concat(",").concat(emailCc3) : emailCc.concat(emailCc3);
						}
						
						sendEmail.setEmailTo(emailTo);
						sendEmail.setEmailCc(emailCc);
						sendEmail.setSubject(emailSubject);
						sendEmail.setContent(emailContent);
						
						emailContent = emailTemplate.getEmailContent();
						
						if (StringUtils.isNotBlank(emailTo)) {
							sendEmailList.add(sendEmail);							
						}
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
						CallApiManager.sendEmailAPI(to,cc, subject, content, "EMAIL_CPSA", "true", parameterDetailService);
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new CustomAPIException(ex.getMessage()); 
		}
		
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

	public CounterTypeService getCounterTypeService() {
		return counterTypeService;
	}

	public void setCounterTypeService(CounterTypeService counterTypeService) {
		this.counterTypeService = counterTypeService;
	}

	public String getFollowUpRemainder() {
		return followUpRemainder;
	}

	public void setFollowUpRemainder(String followUpRemainder) {
		this.followUpRemainder = followUpRemainder;
	}

	public FileUploadEvent getFileUploadCpsa() {
		return fileUploadCpsa;
	}

	public void setFileUploadCpsa(FileUploadEvent fileUploadCpsa) {
		this.fileUploadCpsa = fileUploadCpsa;
	}

	public EmailTemplateService getEmailTemplateService() {
		return emailTemplateService;
	}

	public void setEmailTemplateService(EmailTemplateService emailTemplateService) {
		this.emailTemplateService = emailTemplateService;
	}

	public SimpleDateFormat getSdf() {
		return sdf;
	}

	public void setSdf(SimpleDateFormat sdf) {
		this.sdf = sdf;
	}

	public Integer getIndexRowPicChange() {
		return indexRowPicChange;
	}

	public void setIndexRowPicChange(Integer indexRowPicChange) {
		this.indexRowPicChange = indexRowPicChange;
	}

	public Boolean getFlagNewEdit() {
		return flagNewEdit;
	}

	public void setFlagNewEdit(Boolean flagNewEdit) {
		this.flagNewEdit = flagNewEdit;
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

	public SelectorInfo getSelectorBranch() {
		return selectorBranch;
	}

	public void setSelectorBranch(SelectorInfo selectorBranch) {
		this.selectorBranch = selectorBranch;
	}

	public List<CompliancePlanSelfAssessmentPic> getDataCpsaDeleteList() {
		return dataCpsaDeleteList;
	}

	public void setDataCpsaDeleteList(List<CompliancePlanSelfAssessmentPic> dataCpsaDeleteList) {
		this.dataCpsaDeleteList = dataCpsaDeleteList;
	}
	
}
