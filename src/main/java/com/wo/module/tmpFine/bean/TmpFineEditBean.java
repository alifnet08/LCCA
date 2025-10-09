package com.wo.module.tmpFine.bean;

import java.io.Serializable;
//import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jfree.util.Log;
import org.primefaces.PrimeFaces;
//import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;

import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.exception.CustomAPIException;
import com.wo.module.common.model.UploadedFileWO;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.common.util.FileUtil;
import com.wo.module.common.util.MathUtil;
import com.wo.module.common.utility.CallApiManager;
import com.wo.module.counterType.model.CounterType;
import com.wo.module.counterType.model.CounterTypeDtl;
import com.wo.module.counterType.service.CounterTypeService;
import com.wo.module.emailTemplate.model.EmailTemplate;
import com.wo.module.emailTemplate.service.EmailTemplateService;
import com.wo.module.externalRegulation.service.RegulationMstService;
import com.wo.module.holiday.service.HolidayService;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.lov.bean.SelectorListener;
import com.wo.module.lov.bean.SelectorModel.SelectorInfo;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.parameter.model.ParameterHeader;
import com.wo.module.rc.model.RC;
import com.wo.module.rc.service.RCService;
import com.wo.module.reportType.service.ReportTypeService;
import com.wo.module.tmpFine.constant.TmpFineConstants;
import com.wo.module.tmpFine.model.TmpFine;
import com.wo.module.tmpFine.model.TmpFineApproval;
import com.wo.module.tmpFine.model.TmpFineDocument;
import com.wo.module.tmpFine.model.TmpFinePicCompliance;
import com.wo.module.tmpFine.model.TmpFinePicComplianceTableModel;
import com.wo.module.tmpFine.model.TmpFinePicFollowup;
import com.wo.module.tmpFine.model.TmpFinePicFollowupEmail;
import com.wo.module.tmpFine.model.TmpFinePicFollowupTableModel;
import com.wo.module.tmpFine.service.TmpFineService;
import com.wo.module.trcFineApproval.model.TrcFine;
import com.wo.module.trcFineApproval.service.TrcFineApprovalService;
import com.wo.module.user.model.Division;
import com.wo.module.user.model.User;
import com.wo.module.user.service.UserService;

public class TmpFineEditBean extends CommonBean implements SelectorListener<Object>, Serializable {

	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(TmpFineEditBean.class);

	private TmpFine tmpFine;

	private Boolean isViewOnly;

	private String actionMode;

	private String editedId;

	private String dueDateType;

	private Boolean disabledFollowUpStatus;

	private TmpFinePicCompliance[] selectedPicComplianceData;
	
	private TmpFinePicFollowup[] selectedPicFollowupData;
	
	private SelectorInfo selectorUser1;
	private SelectorInfo selectorUser2;
	private SelectorInfo selectorUser3;
	private SelectorInfo selectorPicCompliance;

	private SelectorInfo selectorUserCc1;
	private SelectorInfo selectorUserCc2;
	private SelectorInfo selectorUserCc3;

	private List<UploadedFileWO> deletedFiles;
	private List<UploadedFileWO> uploadedFilesDocument;

	private Integer lastSequenceOfPicCompliance;
	private Integer lastSequenceOfSupportingUnitModel;
	private Integer lastSequenceOfPicFollowup;

	private Integer indexDtlPicCompliance;
	private Integer indexDtlCc;
	private Integer indexDtlPicFollowup;
	
	private Date maxLetterReceiveDate;
	private Date maxLetterDate;
	private Date minResponseDate;
	
	SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");

	private TmpFineService tmpFineService;
	private TmpFineService tmpFineService2;
	private ReportTypeService reportTypeService;
	private CounterTypeService counterTypeService;
	// private ParameterDetailService parameterDetailService;
	private UserService userService;
	private RegulationMstService regulationMstService;
	private EmailTemplateService emailTemplateService;
	private RCService rcService;
	private TrcFineApprovalService trcFineApprovalService;
	private HolidayService holidayService;

	public FacesUtil facesUtil;

	private FileUtil fileUtil;
	
	private TmpFinePicComplianceTableModel<TmpFinePicCompliance> tablePicComplianceModel;
	private TmpFinePicFollowupTableModel<TmpFinePicFollowup> tablePicFollowupModel;

	private List<SelectItem> senderCodeList;
	private List<SelectItem> yesNoList;
	private List<SelectItem> counterTypeList;
	private List<SelectItem> complianceStatusList;
	private List<SelectItem> divisionList;

	private List<SelectItem> reminderStatusList;
	private List<SelectItem> fineTypeCodeList;
	private List<SelectItem> rcList;
	private List<SelectItem> categoryList;
	private List<SelectItem> reportNameList;
	
	private List<TrcFine> trcFineList;
	private String divNameLogin;
	
	private Boolean isOtherReportNameChosen = false;
	private Boolean isReminderResponseActive = false;
	private Boolean isValidAccess = true;

	private String REMINDER_ACTIVE = ParameterDetail.PARAM_DET_CODE_REMINDER_ACTIVE;
	private String REMINDER_INACTIVE = ParameterDetail.PARAM_DET_CODE_REMINDER_INACTIVE;
	private String textWarningUpload;

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
		this.divNameLogin = facesUtil.getUserLogin().getDivisionName();
		selectorUser1 = TmpFineConstants.buildSelectorUser();
		selectorUser2 = TmpFineConstants.buildSelectorUser();
		selectorUser3 = TmpFineConstants.buildSelectorUser();
		selectorPicCompliance = TmpFineConstants.buildSelectorUserCompliance(divNameLogin);
		selectorUserCc1 = TmpFineConstants.buildSelectorUser();
		selectorUserCc2 = TmpFineConstants.buildSelectorUser();
		selectorUserCc3 = TmpFineConstants.buildSelectorUser();

		checkNewOrEdit();

		fileUtil = FileUtil.getInstance();
		
		try {
			ParameterDetail getText = parameterDetailService.getParameterDetailByParamDtlCode("UPLOAD_WARNING_TEXT");
			textWarningUpload = getText != null ? getText.getName() : "";
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void initList() {
		try {
			
			maxLetterReceiveDate = new Date();
			maxLetterDate = new Date();
			minResponseDate = new Date();
			selectedPicFollowupData = new TmpFinePicFollowup[100];
			selectedPicComplianceData = new TmpFinePicCompliance[100];
			
			rcList = new ArrayList<SelectItem>();
			List<SearchObject> searchCriteria = new ArrayList<SearchObject>();
			List<RC> listRC = rcService.searchData(searchCriteria, 0, Integer.MAX_VALUE, null, null);
			
			for (RC vo : listRC) {
				SelectItem si = new SelectItem();
				si.setLabel(vo.getRegionCode()+"-"+vo.getWorkingUnit()+"-"+vo.getRegion());
				si.setValue(vo.getRcId());
				rcList.add(si);
			}
			
			categoryList = new ArrayList<SelectItem>();
			List<ParameterDetail> listCategory = parameterDetailService
					.getParameterDetailByParamCode(ParameterHeader.PARAM_HEAD_FINE_CATEGORY);

			for (ParameterDetail vo : listCategory) {
				SelectItem si = new SelectItem();
				si.setLabel(vo.getName());
				si.setValue(vo.getParameterDtlCode());
				categoryList.add(si);
			}
			
			senderCodeList = new ArrayList<SelectItem>();
			List<ParameterDetail> listParamDtl = parameterDetailService
					.getParameterDetailByParamCode(ParameterHeader.PARAM_HEAD_CODE_SENDER);

			for (ParameterDetail vo : listParamDtl) {
				SelectItem si = new SelectItem();
				si.setLabel(vo.getName());
				si.setValue(vo.getParameterDtlCode());
				senderCodeList.add(si);
			}

			reminderStatusList = new ArrayList<SelectItem>();
			List<ParameterDetail> listParamReminderDtl = parameterDetailService
					.getParameterDetailByParamCode(ParameterHeader.PARAM_HEAD_CODE_REMINDER_STATUS);

			for (ParameterDetail vo : listParamReminderDtl) {
				SelectItem si = new SelectItem();
				si.setLabel(vo.getName());
				si.setValue(vo.getParameterDtlCode());
				reminderStatusList.add(si);
			}

			complianceStatusList = new ArrayList<SelectItem>();
			List<ParameterDetail> listComplianceDtl = parameterDetailService
					.getParameterDetailByParamCode(ParameterHeader.PARAM_HEAD_CODE_COMPLIANCE_CHECK_STATUS);

			for (ParameterDetail vo : listComplianceDtl) {
				SelectItem si = new SelectItem();
				si.setLabel(vo.getName());
				si.setValue(vo.getParameterDtlCode());
				complianceStatusList.add(si);
			}

			counterTypeList = counterTypeService.getAllCounterTypeLabelValue();

			divisionList = new ArrayList<SelectItem>();
			List<Division> listDiv = userService.getAllDivision();
			for (Division vo : listDiv) {
				SelectItem si = new SelectItem();
				si.setLabel(vo.getDivisionName());
				si.setValue(vo.getDivisionId());
				divisionList.add(si);
			}

			yesNoList = new ArrayList<SelectItem>();
			yesNoList.add(new SelectItem(Constants.CONSTANT_YES, "Yes"));
			yesNoList.add(new SelectItem(Constants.CONSTANT_NO, "No"));

			fineTypeCodeList = new ArrayList<SelectItem>();
			List<ParameterDetail> listFineTypeDtl = parameterDetailService
					.getParameterDetailByParamCodeOrdered(ParameterHeader.PARAM_HEAD_CODE_CORRESPONDENCE_TYPE);

			for (ParameterDetail vo : listFineTypeDtl) {
				SelectItem si = new SelectItem();
				si.setLabel(vo.getName());
				si.setValue(vo.getParameterDtlCode());
				if(vo.getParameterDtlCode().equals("FINE")){
				fineTypeCodeList.add(si);
				}
			}
			
			reportNameList = new ArrayList<SelectItem>();
			List<ParameterDetail> reportNameDtl = parameterDetailService
					.getParameterDetailByParamCodeOrdered(ParameterHeader.PARAM_HEAD_FINE_REPORT_NAME);
			
			for (ParameterDetail vo : reportNameDtl) {
				SelectItem si = new SelectItem();
				si.setLabel(vo.getName());
				si.setValue(vo.getParameterDtlCode());
				reportNameList.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		PrimeFaces.current().executeScript("reInitSelect2();");
	}
	
	public void updatePanelDenda() {
		System.out.println("tipe Surat =="+tmpFine.getFineCode().getParameterDtlCode());
		
	}

	

	public void clearPicDetail2(int i) {
		//tmpFine.getTmpFineSupportingUnits().get(i).setEmailCc2(null);
		//tmpFine.getTmpFineSupportingUnits().get(i).setEmailCcTemp2(null);
		PrimeFaces.current().executeScript("reInitSelect2();");
	}

	public void clearPicDetail3(int i) {
		//tmpFine.getTmpFineSupportingUnits().get(i).setEmailCc3(null);
		//tmpFine.getTmpFineSupportingUnits().get(i).setEmailCcTemp3(null);
		PrimeFaces.current().executeScript("reInitSelect2();");
	}



	public void onChangeDivision(int i) {
		//TmpFineSupportingUnit data = tmpFine.getTmpFineSupportingUnits().get(i);
		/*data.setEmailCc1(null);
		data.setEmailCcTemp1(null);
		data.setEmailCc2(null);
		data.setEmailCcTemp2(null);
		data.setEmailCc3(null);
		data.setEmailCcTemp3(null);*/
		PrimeFaces.current().executeScript("reInitSelect2();");
	}

	public void onChangeFollowupStatus() {
		if (tmpFine.getFollowUp() != null) {
			if ("Y".equals(tmpFine.getFollowUp())) {
				

				if (tmpFine.getCounterType() == null) {
					tmpFine.setCounterType(new CounterType());
				}

			} else if ("N".equals(tmpFine.getFollowUp())) {
				

				tmpFine.setCounterType(null);
			}

		}

		PrimeFaces.current().executeScript("reInitSelect2();");
		// RequestContext.getCurrentInstance().execute("reInitSelect2();");
	}

	public void onAddNewPicCompliance() {
		if (tmpFine.getTmpFinePicCompliances() == null) {
			tmpFine.setTmpFinePicCompliances(new ArrayList<TmpFinePicCompliance>());
			lastSequenceOfPicCompliance = 0;
		} else {
			if (tmpFine.getTmpFinePicCompliances().size() == 0) {
				lastSequenceOfPicCompliance = 0;
			}
		}

		TmpFinePicCompliance d = new TmpFinePicCompliance();
		lastSequenceOfPicCompliance = lastSequenceOfPicCompliance + 1;
		d.setSequence(lastSequenceOfPicCompliance);
		tmpFine.getTmpFinePicCompliances().add(d);
		tablePicComplianceModel.setWrappedData(tmpFine.getTmpFinePicCompliances());

	}

	public void onDeleteRowPicCompliance() {
		for (int i = 0; i < selectedPicComplianceData.length; i++) {
			tmpFine.getTmpFinePicCompliances().remove(selectedPicComplianceData[i]);
		}
		
		
		selectedPicComplianceData = new TmpFinePicCompliance[100];

		if (tmpFine.getTmpFinePicCompliances() == null
				|| tmpFine.getTmpFinePicCompliances().size() == 0) {
			lastSequenceOfPicCompliance = 0;
		}

		tablePicComplianceModel.setWrappedData(tmpFine.getTmpFinePicCompliances());
	}

	public void onAddNewPicFollowup() {
		if (tmpFine.getTmpFinePicFollowups() == null) {
			tmpFine.setTmpFinePicFollowups(new ArrayList<TmpFinePicFollowup>());
			lastSequenceOfPicFollowup = 0;
		} else {
			if (tmpFine.getTmpFinePicFollowups().size() == 0) {
				lastSequenceOfPicFollowup = 0;
			}
		}

		TmpFinePicFollowup d = new TmpFinePicFollowup();
		d.setRc(new RC());
		d.setUserId1(new User());
		d.setUserId2(new User());
		d.setUserId3(new User());
		lastSequenceOfPicFollowup = lastSequenceOfPicFollowup + 1;
		d.setSequence(lastSequenceOfPicFollowup);
		tmpFine.getTmpFinePicFollowups().add(d);
		tablePicFollowupModel.setWrappedData(tmpFine.getTmpFinePicFollowups());

	}
	
	public void onDeleteRowPicFollowup() {
		for (int i = 0; i < selectedPicFollowupData.length; i++) {
			tmpFine.getTmpFinePicFollowups().remove(selectedPicFollowupData[i]);
		}
		
		selectedPicFollowupData = new TmpFinePicFollowup[100];

		if (tmpFine.getTmpFinePicFollowups() == null
				|| tmpFine.getTmpFinePicFollowups().size() == 0) {
			lastSequenceOfPicFollowup = 0;
		}

		tablePicFollowupModel.setWrappedData(tmpFine.getTmpFinePicFollowups());
	}
	
	public void onOtherReportName() {
		if (tmpFine.getReportName().getParameterDtlCode().equals(ParameterDetail.PARAM_DET_CODE_FINE_LHP_OTHER)) {
			isOtherReportNameChosen = true;
		} else {
			isOtherReportNameChosen = false;
		}
	}
	
	public void onSenderEqualsOJKAndDeadlineFilled() {
		if (tmpFine.getSenderCode().getParameterDtlCode().equals(ParameterDetail.PARAM_DET_CODE_SENDER_OJK) || tmpFine.getSenderCode().getParameterDtlCode().equals(ParameterDetail.PARAM_DET_CODE_SENDER_OJK_PASAR_MODAL)) {
			if (tmpFine.getLetterDate() != null) {
				Calendar c = Calendar.getInstance();
				try {
					ParameterDetail pd = parameterDetailService.getParameterDetailByParamDtlCode("TARGET_SETTLEMENT_DAYS");
					c.setTime(tmpFine.getLetterDate());
					c.add(Calendar.DAY_OF_MONTH, Integer.parseInt(pd.getNameIn()));
					
					tmpFine.setTargetSettlement(c.getTime());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
	}
	
	
	
	public void handleFileUploadDocument(FileUploadEvent event) throws Exception {
		try {
			uploadedFilesDocument = uploadedFilesDocument == null ? new ArrayList<UploadedFileWO>()
					: uploadedFilesDocument;
			uploadedFilesDocument.add(new UploadedFileWO(CallApiManager.callUploadAPI(event.getFile(),
					Constants.COMPLIANCE_DOC_TYPE_KORESPONDENSI_SURAT_MASUK, parameterDetailService, false, fileUtil),
					event.getFile().getFileName(), event.getFile().getContentType(), event.getFile().getSize()));
		} catch (Exception e) {
			e.printStackTrace();
			facesUtil.addErrMessage(e.getMessage());
		}
	}

	private void checkNewOrEdit() {
		String editId = facesUtil.retrieveRequestParam("id");
		String token = facesUtil.retrieveRequestParam("token");

		String viewId = facesUtil.retrieveRequestParam("viewId");
		isViewOnly = false;
		if (viewId != null && !viewId.isEmpty()) {
			if (viewId.trim().equalsIgnoreCase("true")) {
				isViewOnly = true;
			}
		}
		if (StringUtils.isBlank(editId) && StringUtils.isBlank(token)) {
			this.handleNew();
		} else {
			this.handleEdit(editId);
			onOtherReportName();
		}
	}

	private void handleNew() {
		tmpFine = new TmpFine();
		disabledFollowUpStatus = false;
		isValidAccess = true;
		tmpFine.setCounterType(new CounterType());
		tmpFine.setCounterTypeResponse(new CounterType());
		tmpFine.setSenderCode(new ParameterDetail());
		tmpFine.setReminderStatus(new ParameterDetail());
		tmpFine.getReminderStatus().setParameterDtlCode(REMINDER_ACTIVE);
		ParameterDetail pd = new ParameterDetail();
		pd.setParameterDtlCode("INVITATION");
		tmpFine.setFineCode(pd);
		tmpFine.setRc(new RC());
		tmpFine.setReportName(new ParameterDetail());

		lastSequenceOfPicCompliance = 0;
		lastSequenceOfSupportingUnitModel = 0;

		User user1 = userService.getUserByNik(facesUtil.retrieveUserLogin());
		if (user1 != null) {
			/*
			 * tmpFine.setUserId1(user1);
			 * tmpFine.setUserNameTemp1(user1.getNik() + "-" + user1.getName());
			 * tmpFine.setDivisionId(user1.getDivisionId());
			 */

			if (tmpFine.getTmpFinePicCompliances() == null
					|| tmpFine.getTmpFinePicCompliances().size() == 0) {
				tmpFine.setTmpFinePicCompliances(new ArrayList<TmpFinePicCompliance>());
				lastSequenceOfPicCompliance = 0;
			}

			TmpFinePicCompliance picCompliance = new TmpFinePicCompliance();
			lastSequenceOfPicCompliance = lastSequenceOfPicCompliance + 1;
			picCompliance.setSequence(lastSequenceOfPicCompliance);
			picCompliance.setUser(user1);
			picCompliance.setNikTemp(user1.getNik());
			picCompliance.setNameTemp(user1.getName());
			picCompliance.setEmailTemp(user1.getEmail());
			tmpFine.getTmpFinePicCompliances().add(picCompliance);
			
			if (tmpFine.getTmpFinePicFollowups() == null
					|| tmpFine.getTmpFinePicFollowups().size() == 0) {
				tmpFine.setTmpFinePicFollowups(new ArrayList<TmpFinePicFollowup>());
				lastSequenceOfPicFollowup = 0;
			}
			
			TmpFinePicFollowup picFollowup = new TmpFinePicFollowup();
			lastSequenceOfPicFollowup = lastSequenceOfPicFollowup + 1;
			picFollowup.setSequence(lastSequenceOfPicFollowup);
			picFollowup.setUserId1(new User());
			picFollowup.setUserId2(new User());
			picFollowup.setUserId3(new User());
			picFollowup.setRc(new RC());
			tmpFine.getTmpFinePicFollowups().add(picFollowup);

			
		}

		uploadedFilesDocument = new ArrayList<UploadedFileWO>();

		tablePicComplianceModel = new TmpFinePicComplianceTableModel<TmpFinePicCompliance>(
				tmpFine.getTmpFinePicCompliances());
		
		tablePicFollowupModel = new TmpFinePicFollowupTableModel<TmpFinePicFollowup>(
				tmpFine.getTmpFinePicFollowups());

		actionMode = Constants.ACTION_ADD;
		facesUtil.setSessionAttribute("token", null);
	}

	private void handleEdit(String editId) {
		try {
			String token = facesUtil.retrieveRequestParam("token");
			if (StringUtils.isNotEmpty(token)) {
				editId = Constants.decryptString(token);
			}
			facesUtil.setSessionAttribute("token", Constants.encryptString(editId));
			actionMode = Constants.ACTION_EDIT;
						
			Long idLong = Long.parseLong(editId);
			tmpFine = tmpFineService.findById(idLong);
			tmpFine.setNotes(tmpFine.getNotesDecrypted());
			lastSequenceOfPicCompliance = 0;
			lastSequenceOfSupportingUnitModel = 0;
			
			TrcFine trcFine = trcFineApprovalService.findById(idLong);
			if (trcFine != null) {
	
				if (trcFine.getFollowupStatus() != null) {
					disabledFollowUpStatus = true;
				} else {
					disabledFollowUpStatus = false;
				}
	
				trcFineList = new ArrayList<TrcFine>();
				trcFineList.add(trcFine);
	
				if (trcFineList.get(0).getComplianceStatus() == null) {
					trcFineList.get(0).setComplianceStatus(new ParameterDetail());
				}
			} else {
				disabledFollowUpStatus = false;
			}
	
			if (tmpFine.getTmpFinePicCompliances() != null) {
				lastSequenceOfPicCompliance = tmpFine.getTmpFinePicCompliances().size();
				for (int i = 0; i < tmpFine.getTmpFinePicCompliances().size(); i++) {
					TmpFinePicCompliance dtl = (TmpFinePicCompliance) tmpFine
							.getTmpFinePicCompliances().get(i);
	
					lastSequenceOfPicCompliance = lastSequenceOfPicCompliance + 1;
					dtl.setSequence(lastSequenceOfPicCompliance);
	
					if (dtl.getUser() != null) {
						dtl.setNikTemp(dtl.getUser().getNik());
						dtl.setNameTemp(dtl.getUser().getName());
						dtl.setEmailTemp(dtl.getUser().getEmail());
					}
				}
			}
	
			uploadedFilesDocument = new ArrayList<UploadedFileWO>();
	
			for (int i = 0; i < tmpFine.getTmpFineDocuments().size(); i++) {
				TmpFineDocument ra = tmpFine.getTmpFineDocuments().get(i);
				UploadedFileWO uf = new UploadedFileWO();
				uf.setFileName(ra.getAttachmentFile());
				uf.setFileId(ra.getFileId());
				uf.setIsNew(false);
				uf.setFileSize(ra.getFileSize());
				uploadedFilesDocument.add(uf);
	
			}
			
			for(int i =0;i<tmpFine.getTmpFinePicFollowups().size(); i++){
				TmpFinePicFollowup fp = tmpFine.getTmpFinePicFollowups().get(i);
				if(fp.getRc() == null){
					fp.setRc(new RC());
				}
			}
	
			tablePicComplianceModel = new TmpFinePicComplianceTableModel<TmpFinePicCompliance>(
					tmpFine.getTmpFinePicCompliances());
			
			tablePicFollowupModel = new TmpFinePicFollowupTableModel<TmpFinePicFollowup>(
					tmpFine.getTmpFinePicFollowups());
			
			this.isValidAccess = true;
			validateDel();			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void validateDel() {
		User userLogin = userService.getUserByNik(facesUtil.retrieveUserLogin());
		
		if(!tmpFine.getCreatedBy().equals(userLogin.getNik()) ) {
			this.isValidAccess = false;
			if(tmpFine.getTmpFinePicCompliances() != null && tmpFine.getTmpFinePicCompliances().size() > 0) {
				for(TmpFinePicCompliance picc: tmpFine.getTmpFinePicCompliances()) {
					User upicc = picc.getUser();
					if(upicc != null && upicc.getUserId().equals(userLogin.getUserId())) {
						this.isValidAccess = true;
						break;
					}
				}
				
			}
		}
	}

	public Boolean validate() {
		Boolean flag = false;

		if (StringUtils.isEmpty(tmpFine.getSenderCode().getParameterDtlCode())) {
			facesUtil.addErrMessage(facesUtil.retrieveMessage("formTmpFineSender") + " "
					+ facesUtil.retrieveMessage("validateRequired"));
			flag = true;
		}

		if (StringUtils.isEmpty(tmpFine.getFineCode().getParameterDtlCode())) {
			facesUtil.addErrMessage(facesUtil.retrieveMessage("formTmpFineFineType") + " "
					+ facesUtil.retrieveMessage("validateRequired"));
			flag = true;
		}

		if (tmpFine.getLetterReceivedDate() == null) {
			facesUtil.addErrMessage(facesUtil.retrieveMessage("formTmpFineLetterReceiveDate") + " "
					+ facesUtil.retrieveMessage("validateRequired"));
			flag = true;
		}

		if (StringUtils.isEmpty(tmpFine.getLetterNo())) {
			facesUtil.addErrMessage(facesUtil.retrieveMessage("formTmpFineLetterNo") + " "
					+ facesUtil.retrieveMessage("validateRequired"));
			flag = true;
		}

		if (StringUtils.isEmpty(tmpFine.getPerihalIn())) {
			facesUtil.addErrMessage(facesUtil.retrieveMessage("formTmpFinePerihal") + " "
//					+ facesUtil.retrieveMessage("indonesia") + " " 
					+ facesUtil.retrieveMessage("validateRequired"));
			flag = true;
		}

		if (StringUtils.isEmpty(tmpFine.getLetterSummary())) {
			facesUtil.addErrMessage(facesUtil.retrieveMessage("formTmpFineLetterSummary") + " "
					+ facesUtil.retrieveMessage("validateRequired"));
			flag = true;
		}
		
		if (StringUtils.isEmpty(tmpFine.getReportName().getParameterDtlCode())) {
			facesUtil.addErrMessage(facesUtil.retrieveMessage("formTmpFineReportName") + " "
					+ facesUtil.retrieveMessage("validateRequired"));
			flag = true;
		}

		if (tmpFine.getLetterDate() == null) {
			facesUtil.addErrMessage(facesUtil.retrieveMessage("formTmpFineLetterDate") + " "
					+ facesUtil.retrieveMessage("validateRequired"));
			flag = true;
		}

		if (tmpFine.getFineCode().getParameterDtlCode()
				.equals(TmpFineConstants.PARAM_DTL_CODE_NONINVITATION)) {
			if (uploadedFilesDocument == null || uploadedFilesDocument.size() <= 0) {
				facesUtil.addErrMessage(facesUtil.retrieveMessage("formTmpFineDocumentLetterSubTitle")
						+ " File " + facesUtil.retrieveMessage("validateRequired"));
				flag = true;
			}
		}

		Set<Long> userComplianceTemp = new HashSet<Long>();
		for (int i = 0; i < tmpFine.getTmpFinePicCompliances().size(); i++) {
			TmpFinePicCompliance dtl = (TmpFinePicCompliance) tmpFine
					.getTmpFinePicCompliances().get(i);
			if (dtl.getUser().getUserId() != null) {
				if (!userComplianceTemp.add(dtl.getUser().getUserId())) {
					facesUtil.addErrMessage(facesUtil.retrieveMessage("formTmpFineNIK") + " "
							+ facesUtil.retrieveMessage("errorDuplicate"));
					flag = true;
					break;
				}
			}
		}

		if (StringUtils.isEmpty(tmpFine.getFollowUp())) {
			facesUtil.addErrMessage(facesUtil.retrieveMessage("formTmpFineFollowup") + " "
					+ facesUtil.retrieveMessage("validateRequired"));
			flag = true;
		} else {
			if (tmpFine.getFollowUp().equals(Constants.CONSTANT_YES)) {
				/*if (tmpFine.getUserId1() == null) {
					facesUtil.addErrMessage(facesUtil.retrieveMessage("formTmpFinePic1") + " "
							+ facesUtil.retrieveMessage("validateRequired"));
					flag = true;
				}*/


				if (tmpFine.getCounterType().getCounterTypeId() == null
						|| StringUtils.isEmpty(tmpFine.getCounterType().getCounterTypeId().toString())) {
					facesUtil.addErrMessage(facesUtil.retrieveMessage("formTmpFineReminderType") + " "
							+ facesUtil.retrieveMessage("validateRequired"));
					flag = true;
				}

				/*if (tmpFine.getTargetDate() == null) {
					facesUtil.addErrMessage(facesUtil.retrieveMessage("formTmpFineTargetDate") + " "
							+ facesUtil.retrieveMessage("validateRequired"));
					flag = true;

				} else {
					if (tmpFineService.hasReachedMaximumReschedule(tmpFine.getFineId())) {
						if (!tmpFine.getTargetDate().equals(tmpFine.getOldTargetDate())) {
							facesUtil.addErrMessage(
									facesUtil.retrieveMessage("formTmpFineTargetDateErrorReachMaximum"));
							flag = true;
						}
					}

				}*/

			} else if (tmpFine.getFollowUp().equals(Constants.CONSTANT_NO)
					&& StringUtils.isEmpty(tmpFine.getNotes())) {
				facesUtil.addErrMessage(facesUtil.retrieveMessage("formTmpFineNote") + "  "
						+ facesUtil.retrieveMessage("validateRequired"));
				flag = true;

			}
		}

		/*
		 * if (StringUtils.isEmpty(tmpFine.getReportNameEn())) {
		 * facesUtil.addErrMessage(facesUtil.retrieveMessage(
		 * "formTmpFineReportName") + " EN " +
		 * facesUtil.retrieveMessage("validateRequired")); flag = true; }
		 */

		if (StringUtils.isEmpty(tmpFine.getReminderStatus().getParameterDtlCode())) {
			facesUtil.addErrMessage(facesUtil.retrieveMessage("formTmpFineStatus") + " "
					+ facesUtil.retrieveMessage("validateRequired"));
			flag = true;
		}

		/*
		 * if (tmpFine.getTmpFinePicCompliances() == null ||
		 * tmpFine.getTmpFinePicCompliances().size() == 0) {
		 * facesUtil.addErrMessage(facesUtil.retrieveMessage(
		 * "formTmpFinePicCompliance") + " " +
		 * facesUtil.retrieveMessage("validateRequired") + " min. 1 data"); flag = true;
		 * }
		 */

		if (isOtherReportNameChosen) {
			if (StringUtils.isEmpty(StringUtils.trim(tmpFine.getOtherReportName()))) {
				facesUtil.addErrMessage(facesUtil.retrieveMessage("formTmpFineOtherReportName") + " "
						+ facesUtil.retrieveMessage("validateRequired"));
				flag = true;
			}
		}
		
		return flag;
	}

	public void save() {
		try {

			if (!validate()) {

				if (tmpFine.getTmpFineDocuments() == null
						|| tmpFine.getTmpFineDocuments().size() == 0) {
					tmpFine.setTmpFineDocuments(new ArrayList<TmpFineDocument>());
				}
				tmpFine.getTmpFineDocuments().clear();

				if (uploadedFilesDocument != null) {
					for (int i = 0; i < uploadedFilesDocument.size(); i++) {
						TmpFineDocument doc = new TmpFineDocument();
						UploadedFileWO uf = (UploadedFileWO) uploadedFilesDocument.get(i);
						doc.setTmpFine(tmpFine);

						doc.setAttachmentFile(uf.getFileName());
						doc.setCreatedBy(facesUtil.retrieveUserLogin());
						doc.setCreationDate(new Timestamp(new Date().getTime()));
						doc.setDelId(new Long(0));
						doc.setEnabledFlag(Constants.CONSTANT_YES);

						doc.setFileId(uf.getFileId());
						doc.setFileSize(uf.getFileSize());
						tmpFine.getTmpFineDocuments().add(doc);
					}
				}

				if (tmpFine.getTmpFinePicCompliances() != null) {
					for (int i = 0; i < tmpFine.getTmpFinePicCompliances().size(); i++) {
						TmpFinePicCompliance dtl = (TmpFinePicCompliance) tmpFine
								.getTmpFinePicCompliances().get(i);
						dtl.setTmpFine(tmpFine);
						if (dtl.getCreatedBy() == null) {
							dtl.setCreatedBy(facesUtil.retrieveUserLogin());
							dtl.setCreationDate(new Timestamp(new Date().getTime()));
						}

						dtl.setLastUpdateBy(facesUtil.retrieveUserLogin());
						dtl.setLastUpdateDate(new Timestamp(new Date().getTime()));
						dtl.setDelId(new Long(0));
						dtl.setEnabledFlag(Constants.CONSTANT_YES);
					}
				}
				
				if (tmpFine.getTmpFinePicFollowups() != null) {
					for (int i = 0; i < tmpFine.getTmpFinePicFollowups().size(); i++) {
						TmpFinePicFollowup dtl = (TmpFinePicFollowup) tmpFine
								.getTmpFinePicFollowups().get(i);
						dtl.setTmpFine(tmpFine);
						if(dtl.getRc()!=null && dtl.getRc().getRcId()!=null){
							dtl.setRc(rcService.findById(dtl.getRc().getRcId()));
						}else{
							dtl.setRc(null);
						}
						
						if (dtl.getCreatedBy() == null) {
							dtl.setCreatedBy(facesUtil.retrieveUserLogin());
							dtl.setCreationDate(new Timestamp(new Date().getTime()));
						}

						dtl.setLastUpdateBy(facesUtil.retrieveUserLogin());
						dtl.setLastUpdateDate(new Timestamp(new Date().getTime()));
						dtl.setDelId(new Long(0));
						dtl.setEnabledFlag(Constants.CONSTANT_YES);
					}
				}


				ParameterDetail pdReminderStatus = parameterDetailService
						.getParameterDetailByParamDtlCode(tmpFine.getReminderStatus().getParameterDtlCode());
				tmpFine.setReminderStatus(pdReminderStatus);
				ParameterDetail pdReportName = parameterDetailService
						.getParameterDetailByParamDtlCode(tmpFine.getReportName().getParameterDtlCode());
				tmpFine.setReportName(pdReportName);
				tmpFine.setNotes(tmpFine.getNotesEncrypted());
				
				//APPROVE PROCESS
				TmpFineApproval dtl = new TmpFineApproval();
				dtl.setTmpFine(tmpFine);
				dtl.setApprovalDate(new Date());
				dtl.setApprovalNote("APPROVE BY SYSTEM");
				dtl.setApprovalStatus(parameterDetailService
						.getParameterDetailByParamDtlCode(ParameterDetail.PARAM_DET_CODE_STATUS_APPROVED));
				dtl.setUser(userService.getUserByNik(facesUtil.retrieveUserLogin()));
				if (dtl.getCreatedBy() == null) {
					dtl.setCreatedBy(facesUtil.retrieveUserLogin());
					dtl.setCreationDate(new Timestamp(new Date().getTime()));
				}

				dtl.setDelId(new Long(0));
				dtl.setEnabledFlag(Constants.CONSTANT_YES);
				
				List<TmpFineApproval> listApproval = new ArrayList<TmpFineApproval>();
				listApproval.add(dtl);
				
				/*if(tmpFine.getTmpFineApprovals()!=null && tmpFine.getTmpFineApprovals().size()>0){
					tmpFine.getTmpFineApprovals().clear();
					tmpFine.getTmpFineApprovals().add(dtl);
				}else{
					tmpFine.setTmpFineApprovals(listApproval);
				}*/
								
				//SAVE PROCESS
				if (tmpFine.getFineId() != null) {
					tmpFine.setStatus(parameterDetailService
							.getParameterDetailByParamDtlCode(ParameterDetail.PARAM_DET_CODE_DATA_INACTIVE));
					tmpFine.setLastUpdateBy(facesUtil.retrieveUserLogin());
					tmpFine.setLastUpdateDate(new Timestamp(new Date().getTime()));
					tmpFine.setDelId(new Long(0));
					tmpFine.setEnabledFlag(Constants.CONSTANT_YES);
					
					tmpFine.getTmpFineApprovals().clear();
					tmpFine.getTmpFineApprovals().add(dtl);
					tmpFineService.update(tmpFine);
				} else {
					tmpFine.setStatus(parameterDetailService
							.getParameterDetailByParamDtlCode(ParameterDetail.PARAM_DET_CODE_DATA_INACTIVE));
					tmpFine.setCreatedBy(facesUtil.retrieveUserLogin());
					tmpFine.setCreationDate(new Timestamp(new Date().getTime()));
					tmpFine.setDelId(new Long(0));
					tmpFine.setEnabledFlag(Constants.CONSTANT_YES);
					tmpFine.setTmpFineApprovals(listApproval);
					tmpFineService.save(tmpFine);
				}
				
				//TODO insert to table tmp_fine_followup_email
				if(tmpFine.getTmpFinePicFollowups() != null && tmpFine.getTmpFinePicFollowups().size() > 0) {
					
					for(TmpFinePicFollowup folup :tmpFine.getTmpFinePicFollowups()) {
						List<TmpFinePicFollowupEmail> tmpFolupEmails = folup.getTmpFinePicFollowupEmails();
						if(tmpFolupEmails == null) {
							tmpFolupEmails = new ArrayList<>();
							folup.setTmpFinePicFollowupEmails(tmpFolupEmails);
						}
						//CLEAR ALL
						tmpFolupEmails.clear();
						
						//ADD NEW
						tmpFolupEmails.addAll(getEmailQueueFollowup(tmpFine.getCounterTypeResponse(), folup)); 
						
						//SAVE
						tmpFineService.updateFolup(folup);
						
					}
										
				}
				
				//TRIGGER INSERT TO TRC_FINE
				TmpFine tmpFine2 = tmpFineService.findById(tmpFine.getFineId());
				tmpFine2.setStatus(parameterDetailService
						.getParameterDetailByParamDtlCode(ParameterDetail.PARAM_DET_CODE_DATA_ACTIVE));
				tmpFineService2.update(tmpFine2);

				// this should be a singleton
		        ExecutorService emailExecutor = Executors.newCachedThreadPool();

		        // from you sendEmail() method
		        emailExecutor.execute(new Runnable() {
		            @Override
		            public void run() {
		                try {
		                	sendEmail();
		                } catch (Exception e) {
		                    logger.error("send email failed", e);
		                }
		            }
		        });

				if (deletedFiles != null) {
					for (int i = 0; i < deletedFiles.size(); i++) {
						UploadedFileWO uf = (UploadedFileWO) deletedFiles.get(i);
						CallApiManager.deleteFile(uf.getFileId(), parameterDetailService, fileUtil);
					}
				}
				
				facesUtil.redirect("/pages/tmpFine/tmpFine.faces");
			}

		} catch (Exception ex) {
			facesUtil.addFacesMsg(FacesMessage.SEVERITY_ERROR, null, "Operation Failed : " + ex.getMessage(), "");
			ex.printStackTrace();
		}

	}

	public List<TmpFinePicFollowupEmail> getEmailQueueFollowup(CounterType counterType, TmpFinePicFollowup folup) {
		List<TmpFinePicFollowupEmail> emailList = new ArrayList<>();
		
		CounterType ctResponse = counterType;

		//PENGINGAT RESPON TARGET WAKTU
		if(ctResponse != null) {
			ctResponse = counterTypeService.findById(counterType.getCounterTypeId());
			if(ctResponse != null && ctResponse.getDetails() != null) {
				Calendar calendar = Calendar.getInstance();
				
				for (CounterTypeDtl dataCounterTypeDtl : ctResponse.getDetails()) {
					int counterDate = 0;
					Date targetDateTmp = folup.getTargetResponseDate();
					calendar.setTime(targetDateTmp);
					if (dataCounterTypeDtl.getSlaType().equals("+")) {
						while (counterDate <= dataCounterTypeDtl.getSla().intValue()) {
							int day = calendar.get(Calendar.DAY_OF_WEEK);
							
							if (day == 1 || day == 7) {
								// do nothing
							} else {
								if (Boolean.TRUE.equals(holidayService.isAvailableDate(targetDateTmp))) {
									counterDate++;
								}
							}
							
							if (counterDate <= dataCounterTypeDtl.getSla().intValue()) {
//								calendar.setTime(targetDateTmp);
								calendar.add(Calendar.DAY_OF_MONTH, 1);
								targetDateTmp = calendar.getTime();
							}
						}
					} else if (dataCounterTypeDtl.getSlaType().equals("-")) {
						while (counterDate <= dataCounterTypeDtl.getSla().intValue()) {
							int day = calendar.get(Calendar.DAY_OF_WEEK);
							
							if (day == 1 || day == 7) {
								// do nothing
							} else {
								if (Boolean.TRUE.equals(holidayService.isAvailableDate(targetDateTmp))) {
									counterDate++;
								}
							}
							
							if (counterDate <= dataCounterTypeDtl.getSla().intValue()) {
//								calendar.setTime(targetDateTmp);
								calendar.add(Calendar.DAY_OF_MONTH, -1);
								targetDateTmp = calendar.getTime();
							}
						}
					}
					
					Date emailDate = calendar.getTime();
					TmpFinePicFollowupEmail vo = new TmpFinePicFollowupEmail();
					vo.setTmpFinePicFollowup(folup);
					vo.setEmailDate(emailDate);
					vo.setSlaType(dataCounterTypeDtl.getSlaType());
					vo.setSla(dataCounterTypeDtl.getSla());
					vo.setEmailType(ParameterDetail.PARAM_DET_FINE_EMAIL_RESPONSE);
					
					vo.setCreatedBy(facesUtil.retrieveUserLogin());
					vo.setCreationDate(new Timestamp(new Date().getTime()));
					vo.setDelId(new Long(0));
					vo.setEnabledFlag(Constants.CONSTANT_YES);
					
					emailList.add(vo);
				}
			}
			
		}
		
		return emailList;
	}

	public void cancel() {
		try {
			for (int i = 0; i < uploadedFilesDocument.size(); i++) {
				UploadedFileWO uf = (UploadedFileWO) uploadedFilesDocument.get(i);
				if (uf.getIsNew() == null) {
					CallApiManager.deleteFile(uf.getFileId(), parameterDetailService, fileUtil);
				}
			}

			facesUtil.redirect("/pages/tmpFine/tmpFine.faces");
		} catch (Exception ex) {
			facesUtil.addFacesMsg(FacesMessage.SEVERITY_ERROR, null, "Operation Failed : " + ex.getMessage(), "");
		}
	}

	public void deleteAttachment(String fileId, int index) throws Exception {
		deletedFiles = deletedFiles != null ? deletedFiles : new ArrayList<UploadedFileWO>();
		deletedFiles.add(new UploadedFileWO(fileId, null, null, null));
		uploadedFilesDocument.remove(uploadedFilesDocument.get(index));
	}

	public void downloadFile(String fileId, String fileName, byte[] content) throws Exception {
		CallApiManager.downloadFile(fileId, fileName, content, parameterDetailService);
	}

	
	
	@SuppressWarnings("unused")
	public void sendEmail() {
		try {
			
			EmailTemplate emailTemplate = emailTemplateService.getEmailTemplateByEmailTemplateCode("EMAIL_DENDA");
			
			if(emailTemplate == null) {
				emailTemplate = emailTemplateService.getEmailTemplateByEmailTemplateCode("EMAIL_FINE_TO_PIC");
			}
			
			if(emailTemplate == null) {
				logger.info("Template Email not found..");
				return;
			}
			
			String emailSubject = emailTemplate.getEmailSubject().replaceAll("counter_type","NOTIFICATION");
				   emailSubject = emailSubject.replaceAll("letter_no",tmpFine.getLetterNo());
			String emailContent = ""; 
			String emailTo  = "";
			String emailCc1 = "";
			String emailCc2 = "";
			String emailCc3 = "";
			String emailCc = "";
			String emailCcCompliance = "";
			
			ParameterDetail pdHostName = parameterDetailService.getParameterDetailByParamDtlCode(Constants.HOST_NAME_APPLICATION);
			
			if (tmpFine.getReminderStatus()!=null && tmpFine.getReminderStatus().getParameterDtlCode().equals(Constants.REMINDER_ACTIVE) && 
					tmpFine.getFollowUp() != null && tmpFine.getFollowUp().equals(Constants.CONSTANT_YES) &&
					tmpFine.getTmpFinePicFollowups() != null && tmpFine.getTmpFinePicFollowups().size() > 0
		    		) {
				for (int i = 0; i < tmpFine.getTmpFinePicFollowups().size(); i++) {
					TmpFinePicFollowup tmp = tmpFine.getTmpFinePicFollowups().get(i);
					emailContent = emailTemplate.getEmailContent().replaceAll("target_date", tmp.getTargetDate()!=null?sdf.format(tmp.getTargetDate()):"");
					emailContent = emailContent.replaceAll("perihal_in", tmpFine.getPerihalIn());
					
					if (tmpFine.getSenderCode() != null && tmpFine.getSenderCode().getParameterDtlCode() != null) {
						ParameterDetail psSenderCode = parameterDetailService.getParameterDetailByParamDtlCode(tmpFine.getSenderCode().getParameterDtlCode());
						emailContent = emailContent.replaceAll("sender_in", psSenderCode.getNameIn()!=null?psSenderCode.getNameIn():"NA");
						//emailContent = emailContent.replaceAll("sender_en", psSenderCode.getNameEn()!=null?psSenderCode.getNameEn():"NA");
					} else {
						emailContent = emailContent.replaceAll("sender_in", "NA");
						//emailContent = emailContent.replaceAll("sender_en", "NA");
					}
					
					
					String token = "";
					String urlLink = "";
					String menuId = "";
					if (tmpFine.getFollowUp() != null && tmpFine.getFollowUp().equals("Y")) {
						token = Constants.encryptString(tmp.getFinePicFollowupId().toString());
						menuId = Constants.encryptString(Constants.MENU_ID_FOLLOWUP_CONFIRMATION_FINE);
						urlLink = pdHostName.getNameIn().concat("pages/fineFE/fineFEEdit.faces?token="+token+"&menuId="+menuId+"&first=0");
			    	} /*else if (tmpFine.getFollowUp() != null && tmpFine.getFollowUp().equals("N")) {
			    		token = Constants.encryptString(tmpFine.getFineId().toString());
			    		menuId = Constants.encryptString(Constants.MENU_ID_FINE_VIEW);
						urlLink = pdHostName.getNameIn().concat("pages/fineFE/fineFEView.faces?token="+token+"&menuId="+menuId);
			    	}*/
					
					emailContent = emailContent.replaceAll("letter_no", tmpFine.getLetterNo());
					emailContent = emailContent.replaceAll("letter_date", tmpFine.getLetterDate()!=null?sdf.format(tmpFine.getLetterDate()):"");
					
					emailContent = emailContent.replaceAll("url_link", urlLink);
					emailContent = emailContent.replaceAll("report_name", tmpFine.getReportName()!=null?tmpFine.getReportName().getNameIn():"");
					emailContent = emailContent.replaceAll("region_code", tmp.getRc()!=null?tmp.getRc().getRegionCode():"");
					emailContent = emailContent.replaceAll("debited", tmp.getFineDebitted()!=null?sdf.format(tmp.getFineDebitted()):"");
					emailContent = emailContent.replaceAll("nominal", ""+tmp.getFineAmount());
					emailContent = emailContent.replaceAll("breaches", ""+tmp.getBreaches());
					emailContent = emailContent.replaceAll("root_cause", ""+tmp.getRootCause());
					if(StringUtils.isNotEmpty(tmp.getCategory())) {
						ParameterDetail pd = parameterDetailService.getParameterDetailByParamDtlCode(tmp.getCategory());
						emailContent = emailContent.replaceAll("category", ""+pd != null ? pd.getNameIn():"");
					}else {
						emailContent = emailContent.replaceAll("category", "");
					}
					emailContent = emailContent.replaceAll("division_name", ""+tmp.getUserId1().getDivisionName());
					emailContent = emailContent.replaceAll("pic_1_name", ""+tmp.getUserId1().getName());
					emailContent = emailContent.replaceAll("pic_2_name", ""+(tmp.getUserId2()!=null?tmp.getUserId2().getName():""));
					emailContent = emailContent.replaceAll("pic_3_name", ""+(tmp.getUserId3()!=null?tmp.getUserId3().getName():""));
					
					emailCc = "";
					emailCcCompliance = "";
					
					if (tmpFine != null) {
						if (tmpFine.getTmpFinePicCompliances() != null && tmpFine.getTmpFinePicCompliances().size() > 0) {
							for (TmpFinePicCompliance spct : tmpFine.getTmpFinePicCompliances()) {
								User ue = null;
								if (spct.getUser() != null) {
									ue = userService.findById(spct.getUser().getUserId());
									if (ue != null && StringUtils.isNotBlank(ue.getEmail())) {
										emailCcCompliance = StringUtils.isNotEmpty(emailCcCompliance)?emailCcCompliance.concat(",").concat(ue.getEmail()):emailCcCompliance.concat(ue.getEmail());
									}
								}
							}
						}
					}
					
//					for(int x=0;x<socializationTmp.getCounterType().getDetails().size();x++) {
					if (tmpFine != null) {
						if (tmpFine.getCounterType() != null && tmpFine.getCounterType().getDetails() != null && tmpFine.getCounterType().getDetails().size() > 0) {
							CounterTypeDtl cd = tmpFine.getCounterType().getDetails().get(0);
							emailTo = cd.getEmailTo();
							emailCc1 = cd.getEmailCc1();
							emailCc2 = cd.getEmailCc2();
							
							if(emailTo.equals(Constants.REMINDER_PIC1)) {
								emailTo = tmp.getUserId1()!=null?tmp.getUserId1().getEmail():"";
							}
							else if(emailTo.equals(Constants.REMINDER_PIC2)) {
								emailTo = tmp.getUserId2()!=null?tmp.getUserId2().getEmail():"";
							}
							else if(emailTo.equals(Constants.REMINDER_PIC3)) {
								emailTo = tmp.getUserId3()!=null?tmp.getUserId3().getEmail():"";
							}
							
							if(emailCc1!=null && emailCc1.equals(Constants.REMINDER_PIC1)) {
								emailCc1 = tmp.getUserId1()!=null?tmp.getUserId1().getEmail():"";
							}
							else if(emailCc1!=null && emailCc1.equals(Constants.REMINDER_PIC2)) {
								emailCc1 = tmp.getUserId2()!=null?tmp.getUserId2().getEmail():"";
							}
							else if(emailCc1!=null && emailCc1.equals(Constants.REMINDER_PIC3)) {
								emailCc1 = tmp.getUserId3()!=null?tmp.getUserId3().getEmail():"";
							}
							
							if(emailCc2!=null && emailCc2.equals(Constants.REMINDER_PIC1)) {
								emailCc2 = tmp.getUserId1()!=null?tmp.getUserId1().getEmail():"";
							}
							else if(emailCc2!=null && emailCc2.equals(Constants.REMINDER_PIC2)) {
								emailCc2 = tmp.getUserId2()!=null?tmp.getUserId2().getEmail():"";
							}
							else if(emailCc2!=null && emailCc2.equals(Constants.REMINDER_PIC3)) {
								emailCc2 = tmp.getUserId3()!=null?tmp.getUserId3().getEmail():"";
							}
							
							
						} else {
							emailTo = tmp.getUserId1() != null ? tmp.getUserId1().getEmail() : "";
							emailCc1 = tmp.getUserId2() != null ? tmp.getUserId2().getEmail() : "";
							emailCc2 = tmp.getUserId3() != null ? tmp.getUserId3().getEmail() : "";
						}
						
						if(StringUtils.isNotEmpty(emailCc1)) {
							emailCc = emailCc.concat(emailCc1);
						}
						if(StringUtils.isNotEmpty(emailCc2)) {
							emailCc = StringUtils.isNotEmpty(emailCc)?emailCc.concat(",").concat(emailCc2):emailCc.concat(emailCc2);
						}
						if (StringUtils.isNotEmpty(emailCcCompliance)) {
							emailCc = StringUtils.isNotEmpty(emailCc)?emailCc.concat(",").concat(emailCcCompliance):emailCc.concat(emailCcCompliance);
						}
						
						ExecutorService emailExecutor = Executors.newCachedThreadPool();

						final String subject = emailSubject;
						final String content = emailContent;
						final String to = emailTo;
//						final String to = "malikabr47@gmail.com";
						final String cc = emailCc;
						
						CallApiManager.sendEmailAPI(to,cc, subject,
								content, "EMAIL_DENDA", "true", parameterDetailService);
					}
					
					
				}
			}
			
			
		    
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new CustomAPIException(ex.getMessage()); 
		}
		
	}
	
	public Boolean getIsReminderResponseActive() {
		logger.debug("isReminderResponseActive == "+isReminderResponseActive);
		isReminderResponseActive = false;
		if(tablePicFollowupModel != null && tmpFine.getTmpFinePicFollowups().size() > 0) {
			for(TmpFinePicFollowup followup : tmpFine.getTmpFinePicFollowups()) {
				if(followup.getTargetResponseDate() != null) {
					isReminderResponseActive = true;
					break;
				}
			}
		}
		logger.debug("isReminderResponseActive last == "+isReminderResponseActive);
		return isReminderResponseActive;
	}

	public FacesUtil getFacesUtil() {
		return facesUtil;
	}

	public void setFacesUtil(FacesUtil facesUtil) {
		this.facesUtil = facesUtil;
	}

	public TmpFineService getTmpFineService() {
		return tmpFineService;
	}

	public void setTmpFineService(TmpFineService tmpFineService) {
		this.tmpFineService = tmpFineService;
	}

	public TmpFine getTmpFine() {
		return tmpFine;
	}

	public void setTmpFine(TmpFine tmpFine) {
		this.tmpFine = tmpFine;
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

	/*
	 * public ParameterDetailService getParameterDetailService() { return
	 * parameterDetailService; }
	 * 
	 * public void setParameterDetailService(ParameterDetailService
	 * parameterDetailService) { this.parameterDetailService =
	 * parameterDetailService; }
	 */

	public ReportTypeService getReportTypeService() {
		return reportTypeService;
	}

	public void setReportTypeService(ReportTypeService reportTypeService) {
		this.reportTypeService = reportTypeService;
	}

	public CounterTypeService getCounterTypeService() {
		return counterTypeService;
	}

	public void setCounterTypeService(CounterTypeService counterTypeService) {
		this.counterTypeService = counterTypeService;
	}

	public TmpFinePicComplianceTableModel<TmpFinePicCompliance> getTablePicComplianceModel() {
		return tablePicComplianceModel;
	}

	public void setTablePicComplianceModel(
			TmpFinePicComplianceTableModel<TmpFinePicCompliance> tablePicComplianceModel) {
		this.tablePicComplianceModel = tablePicComplianceModel;
	}

	

	public String getDueDateType() {
		return dueDateType;
	}

	public void setDueDateType(String dueDateType) {
		this.dueDateType = dueDateType;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public List<SelectItem> getDivisionList() {
		return divisionList;
	}

	public void setDivisionList(List<SelectItem> divisionList) {
		this.divisionList = divisionList;
	}

	@Override
	public void itemSelected(String clientId, String widgetVar, Object selectedItem) {
		if (StringUtils.equals("pic1Dialog", widgetVar)) {
			Object[] objects = (Object[]) selectedItem;
			
			User user1 = userService.findById(MathUtil.returnIdObjectToLong(objects[0]));
			if (user1 != null) {
				
				tmpFine.getTmpFinePicFollowups().get(indexDtlPicFollowup).setUserId1(user1);

				User user2 = userService.getUserByNik(user1.getPukNik());
				if (user2 != null) {
					tmpFine.getTmpFinePicFollowups().get(indexDtlPicFollowup).setUserId2(user2);

					User user3 = userService.getUserByNik(user2.getPukNik());
					if (user3 != null) {
						tmpFine.getTmpFinePicFollowups().get(indexDtlPicFollowup).setUserId3(user3);
					}
				}
			}
		}

		if (StringUtils.equals("pic2Dialog", widgetVar)) {
			Object[] objects = (Object[]) selectedItem;

			User user2 = userService.findById(MathUtil.returnIdObjectToLong(objects[0]));
			if (user2 != null) {
				tmpFine.getTmpFinePicFollowups().get(indexDtlPicFollowup).setUserId2(user2);

				User user3 = userService.getUserByNik(user2.getPukNik());
				if (user3 != null) {
					tmpFine.getTmpFinePicFollowups().get(indexDtlPicFollowup).setUserId3(user3);
				}
			}
		}

		if (StringUtils.equals("pic3Dialog", widgetVar)) {
			Object[] objects = (Object[]) selectedItem;

			User user3 = userService.findById(MathUtil.returnIdObjectToLong(objects[0]));
			if (user3 != null) {
				tmpFine.getTmpFinePicFollowups().get(indexDtlPicFollowup).setUserId3(user3);
			}
		}
		
		if (StringUtils.equals("picComplianceDialog", widgetVar)) {
			Object[] objects = (Object[]) selectedItem;
			// User userPicCompliance = userService.findById(((BigInteger)
			// objects[0]).longValue());
			User userPicCompliance = userService.findById(MathUtil.returnIdObjectToLong(objects[0]));
			if (userPicCompliance != null) {
				tmpFine.getTmpFinePicCompliances().get(indexDtlPicCompliance)
						.setUser(userPicCompliance);
				tmpFine.getTmpFinePicCompliances().get(indexDtlPicCompliance)
						.setNikTemp(userPicCompliance.getNik());
				tmpFine.getTmpFinePicCompliances().get(indexDtlPicCompliance)
						.setNameTemp(userPicCompliance.getName());
				tmpFine.getTmpFinePicCompliances().get(indexDtlPicCompliance)
						.setEmailTemp(userPicCompliance.getEmail());

				//tableSupportingUnitModel.setWrappedData(tmpCorrespondence.getTmpCorrespondenceSupportingUnits());
			}
		}

		PrimeFaces.current().executeScript("reInitSelect2();");


	}

	public String getREMINDER_ACTIVE() {
		return REMINDER_ACTIVE;
	}

	public void setREMINDER_ACTIVE(String rEMINDER_ACTIVE) {
		REMINDER_ACTIVE = rEMINDER_ACTIVE;
	}

	public String getREMINDER_INACTIVE() {
		return REMINDER_INACTIVE;
	}

	public void setREMINDER_INACTIVE(String rEMINDER_INACTIVE) {
		REMINDER_INACTIVE = rEMINDER_INACTIVE;
	}

	public SelectorInfo getSelectorUser1() {
		return selectorUser1;
	}

	public void setSelectorUser1(SelectorInfo selectorUser1) {
		this.selectorUser1 = selectorUser1;
	}

	public SelectorInfo getSelectorUser2() {
		return selectorUser2;
	}

	public void setSelectorUser2(SelectorInfo selectorUser2) {
		this.selectorUser2 = selectorUser2;
	}

	public SelectorInfo getSelectorUser3() {
		return selectorUser3;
	}

	public void setSelectorUser3(SelectorInfo selectorUser3) {
		this.selectorUser3 = selectorUser3;
	}

	public RegulationMstService getRegulationMstService() {
		return regulationMstService;
	}

	public void setRegulationMstService(RegulationMstService regulationMstService) {
		this.regulationMstService = regulationMstService;
	}

	public List<SelectItem> getReminderStatusList() {
		return reminderStatusList;
	}

	public void setReminderStatusList(List<SelectItem> reminderStatusList) {
		this.reminderStatusList = reminderStatusList;
	}

	public SelectorInfo getSelectorUserCc1() {
		return selectorUserCc1;
	}

	public void setSelectorUserCc1(SelectorInfo selectorUserCc1) {
		this.selectorUserCc1 = selectorUserCc1;
	}

	public SelectorInfo getSelectorUserCc2() {
		return selectorUserCc2;
	}

	public void setSelectorUserCc2(SelectorInfo selectorUserCc2) {
		this.selectorUserCc2 = selectorUserCc2;
	}

	public SelectorInfo getSelectorUserCc3() {
		return selectorUserCc3;
	}

	public void setSelectorUserCc3(SelectorInfo selectorUserCc3) {
		this.selectorUserCc3 = selectorUserCc3;
	}

	public TmpFinePicCompliance[] getSelectedPicComplianceData() {
		return selectedPicComplianceData;
	}

	public void setSelectedPicComplianceData(TmpFinePicCompliance[] selectedPicComplianceData) {
		this.selectedPicComplianceData = selectedPicComplianceData;
	}

	

	public Integer getIndexDtlCc() {
		return indexDtlCc;
	}

	public void setIndexDtlCc(Integer indexDtlCc) {
		this.indexDtlCc = indexDtlCc;
	}

	public List<SelectItem> getSenderCodeList() {
		return senderCodeList;
	}

	public void setSenderCodeList(List<SelectItem> senderCodeList) {
		this.senderCodeList = senderCodeList;
	}

	public List<SelectItem> getYesNoList() {
		return yesNoList;
	}

	public void setYesNoList(List<SelectItem> yesNoList) {
		this.yesNoList = yesNoList;
	}

	public List<UploadedFileWO> getUploadedFilesDocument() {
		return uploadedFilesDocument;
	}

	public void setUploadedFilesDocument(List<UploadedFileWO> uploadedFilesDocument) {
		this.uploadedFilesDocument = uploadedFilesDocument;
	}

	public List<SelectItem> getCounterTypeList() {
		return counterTypeList;
	}

	public void setCounterTypeList(List<SelectItem> counterTypeList) {
		this.counterTypeList = counterTypeList;
	}

	public Integer getIndexDtlPicCompliance() {
		return indexDtlPicCompliance;
	}

	public void setIndexDtlPicCompliance(Integer indexDtlPicCompliance) {
		this.indexDtlPicCompliance = indexDtlPicCompliance;
	}

	public SelectorInfo getSelectorPicCompliance() {
		return selectorPicCompliance;
	}

	public void setSelectorPicCompliance(SelectorInfo selectorPicCompliance) {
		this.selectorPicCompliance = selectorPicCompliance;
	}

	public List<SelectItem> getComplianceStatusList() {
		return complianceStatusList;
	}

	public void setComplianceStatusList(List<SelectItem> complianceStatusList) {
		this.complianceStatusList = complianceStatusList;
	}


	public FileUtil getFileUtil() {
		return fileUtil;
	}

	public void setFileUtil(FileUtil fileUtil) {
		this.fileUtil = fileUtil;
	}

	public List<UploadedFileWO> getDeletedFiles() {
		return deletedFiles;
	}

	public void setDeletedFiles(List<UploadedFileWO> deletedFiles) {
		this.deletedFiles = deletedFiles;
	}

	public Integer getLastSequenceOfPicCompliance() {
		return lastSequenceOfPicCompliance;
	}

	public void setLastSequenceOfPicCompliance(Integer lastSequenceOfPicCompliance) {
		this.lastSequenceOfPicCompliance = lastSequenceOfPicCompliance;
	}

	public Integer getLastSequenceOfSupportingUnitModel() {
		return lastSequenceOfSupportingUnitModel;
	}

	public void setLastSequenceOfSupportingUnitModel(Integer lastSequenceOfSupportingUnitModel) {
		this.lastSequenceOfSupportingUnitModel = lastSequenceOfSupportingUnitModel;
	}

	public Boolean getDisabledFollowUpStatus() {
		return disabledFollowUpStatus;
	}

	public void setDisabledFollowUpStatus(Boolean disabledFollowUpStatus) {
		this.disabledFollowUpStatus = disabledFollowUpStatus;
	}

	public List<SelectItem> getFineTypeCodeList() {
		return fineTypeCodeList;
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

	public List<SelectItem> getRcList() {
		return rcList;
	}

	public void setRcList(List<SelectItem> rcList) {
		this.rcList = rcList;
	}

	public List<SelectItem> getCategoryList() {
		return categoryList;
	}

	public void setCategoryList(List<SelectItem> categoryList) {
		this.categoryList = categoryList;
	}

	public RCService getRcService() {
		return rcService;
	}

	public void setRcService(RCService rcService) {
		this.rcService = rcService;
	}

	public void setFineTypeCodeList(List<SelectItem> fineTypeCodeList) {
		this.fineTypeCodeList = fineTypeCodeList;
	}

	public TmpFinePicFollowupTableModel<TmpFinePicFollowup> getTablePicFollowupModel() {
		return tablePicFollowupModel;
	}

	public void setTablePicFollowupModel(TmpFinePicFollowupTableModel<TmpFinePicFollowup> tablePicFollowupModel) {
		this.tablePicFollowupModel = tablePicFollowupModel;
	}

	public TmpFinePicFollowup[] getSelectedPicFollowupData() {
		return selectedPicFollowupData;
	}

	public void setSelectedPicFollowupData(TmpFinePicFollowup[] selectedPicFollowupData) {
		this.selectedPicFollowupData = selectedPicFollowupData;
	}

	public Integer getIndexDtlPicFollowup() {
		return indexDtlPicFollowup;
	}

	public void setIndexDtlPicFollowup(Integer indexDtlPicFollowup) {
		this.indexDtlPicFollowup = indexDtlPicFollowup;
	}

	public Integer getLastSequenceOfPicFollowup() {
		return lastSequenceOfPicFollowup;
	}

	public void setLastSequenceOfPicFollowup(Integer lastSequenceOfPicFollowup) {
		this.lastSequenceOfPicFollowup = lastSequenceOfPicFollowup;
	}

	public List<TrcFine> getTrcFineList() {
		return trcFineList;
	}

	public void setTrcFineList(List<TrcFine> trcFineList) {
		this.trcFineList = trcFineList;
	}

	public TrcFineApprovalService getTrcFineApprovalService() {
		return trcFineApprovalService;
	}

	public void setTrcFineApprovalService(TrcFineApprovalService trcFineApprovalService) {
		this.trcFineApprovalService = trcFineApprovalService;
	}

	public List<SelectItem> getReportNameList() {
		return reportNameList;
	}

	public void setReportNameList(List<SelectItem> reportNameList) {
		this.reportNameList = reportNameList;
	}

	public TmpFineService getTmpFineService2() {
		return tmpFineService2;
	}

	public void setTmpFineService2(TmpFineService tmpFineService2) {
		this.tmpFineService2 = tmpFineService2;
	}

	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		TmpFineEditBean.logger = logger;
	}

	public Boolean getIsOtherReportNameChosen() {
		return isOtherReportNameChosen;
	}

	public void setIsOtherReportNameChosen(Boolean isOtherReportNameChosen) {
		this.isOtherReportNameChosen = isOtherReportNameChosen;
	}

	public String getDivNameLogin() {
		return divNameLogin;
	}

	public void setDivNameLogin(String divNameLogin) {
		this.divNameLogin = divNameLogin;
	}

	public String getTextWarningUpload() {
		return textWarningUpload;
	}

	public void setTextWarningUpload(String textWarningUpload) {
		this.textWarningUpload = textWarningUpload;
	}

	public void setIsReminderResponseActive(Boolean isReminderResponseActive) {
		this.isReminderResponseActive = isReminderResponseActive;
	}

	public Date getMaxLetterReceiveDate() {
		return maxLetterReceiveDate;
	}

	public void setMaxLetterReceiveDate(Date maxLetterReceiveDate) {
		this.maxLetterReceiveDate = maxLetterReceiveDate;
	}

	public Date getMaxLetterDate() {
		return maxLetterDate;
	}

	public void setMaxLetterDate(Date maxLetterDate) {
		this.maxLetterDate = maxLetterDate;
	}

	public Date getMinResponseDate() {
		return minResponseDate;
	}

	public void setMinResponseDate(Date minResponseDate) {
		this.minResponseDate = minResponseDate;
	}

	public HolidayService getHolidayService() {
		return holidayService;
	}

	public void setHolidayService(HolidayService holidayService) {
		this.holidayService = holidayService;
	}

	public Boolean getIsValidAccess() {
		return isValidAccess;
	}

	public void setIsValidAccess(Boolean isValidAccess) {
		this.isValidAccess = isValidAccess;
	}

	
	
}