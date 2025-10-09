package com.wo.module.trcAuditView.bean;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.model.SelectItem;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.primefaces.PrimeFaces;
//import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;

import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.model.ColumnModel;
import com.wo.module.common.model.UploadedFileWO;
import com.wo.module.common.util.DateUtil;
import com.wo.module.common.util.EntityUtil;
import com.wo.module.common.util.FileUtil;
import com.wo.module.common.utility.CallApiManager;
import com.wo.module.complianceTestingMockup.vo.AttachmentVO;
import com.wo.module.counterType.service.CounterTypeService;
import com.wo.module.emailTemplate.model.EmailTemplate;
import com.wo.module.emailTemplate.service.EmailTemplateService;
import com.wo.module.externalRegulation.model.RegulationTrackRecord;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.mstAudit.model.MstAudit;
import com.wo.module.mstAudit.service.MstAuditService;
import com.wo.module.mstAudit.vo.MstAuditVO;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.parameter.model.ParameterHeader;
import com.wo.module.tmpAudit.service.TmpAuditService;
import com.wo.module.tmpAudit.vo.AuditConfirmationVO;
import com.wo.module.trcAudit.model.TrcAudit;
import com.wo.module.trcAudit.model.TrcAuditCheckPoint;
import com.wo.module.trcAudit.model.TrcAuditDocument;
import com.wo.module.trcAudit.model.TrcAuditPICComplianceTableModel;
import com.wo.module.trcAudit.model.TrcAuditPICFollowupTableModel;
import com.wo.module.trcAudit.model.TrcAuditPicCompliance;
import com.wo.module.trcAudit.model.TrcAuditPicFollowup;
import com.wo.module.trcAudit.model.TrcAuditPicFollowupAttachment;
import com.wo.module.trcAudit.model.TrcAuditPicFollowupEmail;
import com.wo.module.trcAudit.model.TrcAuditPicFollowupExt;
import com.wo.module.trcAudit.model.TrcAuditPicFollowupRec;
import com.wo.module.trcAudit.model.TrcAuditPicFollowupRecAttachment;
import com.wo.module.trcAudit.model.TrcAuditPicFollowupRecEmail;
import com.wo.module.trcAudit.model.TrcAuditPicFollowupRecExt;
import com.wo.module.trcAudit.service.TrcAuditPICFollowupService;
import com.wo.module.trcAudit.service.TrcAuditService;
import com.wo.module.trcAuditVerification.service.TrcAuditVerificationService;
import com.wo.module.trcAuditView.constant.AuditViewConstant;
import com.wo.module.user.model.Division;
import com.wo.module.user.model.User;
import com.wo.module.user.service.UserService;

public class TrcAuditViewEditBean extends CommonBean implements Serializable, AuditViewConstant {

	private static final long serialVersionUID = -907978165268286689L;

	static Logger logger = Logger.getLogger(TrcAuditViewEditBean.class);
	
	private TrcAudit trcAudit;
	private TrcAuditPicFollowup trcAuditPicFollowup;

	private Boolean isViewOnly;
	private String viewOnly;

	private String actionMode;

	private String editedId;

	private Long oldCounterTypeId;
	private Long newCounterTypeId;

	private Integer indexDtlCompliance;
	private Long counterTypeId;

	private List<SelectItem> selectAuditFollowUp;
	private List<SelectItem> selectAuditObject;
	private List<SelectItem> selectAuditCategory;
	private List<SelectItem> counterTypes;
	private List<SelectItem> followUps;
	private List<SelectItem> complianceStatusList;

	private List<UploadedFileWO> uploadedFilesAuditFindings;
	private List<UploadedFileWO> uploadedFilesAuditBankResponse;
	private List<UploadedFileWO> uploadedFilesAuditBankCommitment;
	private List<UploadedFileWO> uploadedFilesAuditBankAttachment;
	private List<UploadedFileWO> uploadedFiles;
	private List<UploadedFileWO> deletedFiles;

	private FileUtil fileUtil;

	// services
	private TrcAuditService trcAuditService;
	private TmpAuditService tmpAuditService;
	private CounterTypeService counterTypeService;
	private UserService userService;
	
	private EmailTemplateService emailTemplateService;
	private TrcAuditPICFollowupService trcAuditPICFollowupService;
	private MstAuditService mstAuditService;
	
	private List<SelectItem> divisions;

	private List<SelectItem> reminderStatusList;
	
	private List<SelectItem> templateAuditList;

	private TrcAuditPicCompliance[] selectedDataCompliance;

	private TrcAuditPicFollowup[] selectedDataFollowup;

	private TrcAuditPICComplianceTableModel<TrcAuditPicCompliance> tableModelCompliance;

	private TrcAuditPICFollowupTableModel<TrcAuditPicFollowup> tableModelFollowup;

	private Integer lastSequenceOfCompliance;
	private Integer lastSequenceOfFollowup;

	private List<AuditConfirmationVO> tableStatus;

	private RegulationTrackRecord selectedRow;

	private Integer indexDtl;
	private Integer indexDtlFollowup;

	public FacesUtil facesUtil;

	private String navigateSearch = "trcAuditView.faces";

	private String note;

	SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
	private List<SelectItem> yesNo;

	private int idxFollowup;
	
	private String textWarningUpload;
	
	@PostConstruct
	public void init() {
		super.init();
		fileUtil = FileUtil.getInstance();

		constructSelectComponent();

		tableStatus = new ArrayList<AuditConfirmationVO>();
		checkNewOrEdit();
		
		try {
			ParameterDetail getText = parameterDetailService.getParameterDetailByParamDtlCode("UPLOAD_WARNING_TEXT");
			textWarningUpload = getText.getName();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void constructSelectComponent() {
		setupAuditFollowUp();
		setupAuditObject();
		setupAuditCategory();
		setupAuditCounterType();
		setupAuditPicFollowupDivision();
		setupReminderStatus();
		setupFollowup();
		setupComplianceStatus();
		setupYesNo();
		setupTemplateAuditList();
	}

	private void setupTemplateAuditList() {
		templateAuditList = new ArrayList<SelectItem>();
		try {
			List<MstAudit> allDataMstAudit = mstAuditService.getAllMstAuditData();
			for (int i = 0; i < allDataMstAudit.size(); i++) {
				SelectItem si = new SelectItem();
				si.setLabel((allDataMstAudit.get(i)).getAuditTemplate());
				si.setValue((allDataMstAudit.get(i)).getMstAuditId());
				templateAuditList.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void setupYesNo() {
		setYesNo(new ArrayList<SelectItem>());
		getYesNo().add(new SelectItem(Constants.CONSTANT_YES));
		getYesNo().add(new SelectItem(Constants.CONSTANT_NO));
	}
	
	private void setupAuditFollowUp() {
		setSelectAuditFollowUp(new ArrayList<SelectItem>());
		try {
			setSelectAuditFollowUp(parameterDetailService.getListLabelValue(ParameterHeader.PARAM_HEAD_CODE_AUDITOR,
					false, true, facesUtil.retrieveDefaultLocale()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setupAuditObject() {
		selectAuditObject = new ArrayList<SelectItem>();
		try {
			selectAuditObject = parameterDetailService.getListLabelValue(ParameterHeader.PARAM_HEAD_CODE_AUDIT_OBJECT,
					false, true, facesUtil.retrieveDefaultLocale());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setupAuditCounterType() {
		try {
			counterTypes = counterTypeService.getAllCounterTypeLabelValue();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setupAuditCategory() {
		selectAuditCategory = new ArrayList<SelectItem>();
		try {
			selectAuditCategory = parameterDetailService.getListLabelValue(
					ParameterHeader.PARAM_HEAD_CODE_AUDIT_CATEGORY, false, true, facesUtil.retrieveDefaultLocale());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setupAuditPicFollowupDivision() {
		divisions = new ArrayList<SelectItem>();
		try {
			List<Division> pd = userService.getAllDivision();
			for (int i = 0; i < pd.size(); i++) {
				SelectItem si = new SelectItem();
				si.setLabel(((Division) pd.get(i)).getDivisionName());
				si.setValue(((Division) pd.get(i)).getDivisionId());
				divisions.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		PrimeFaces.current().executeScript("reInitSelect2();");
	}

	public void setupFollowup() {
		followUps = new ArrayList<SelectItem>();
		SelectItem si = new SelectItem();
		si.setLabel("Yes");
		si.setValue(Y);
		followUps.add(si);

		SelectItem si2 = new SelectItem();
		si2.setLabel("No");
		si2.setValue(N);
		followUps.add(si2);
	}

	public void setupReminderStatus() {
		reminderStatusList = new ArrayList<SelectItem>();
		try {
			List<ParameterDetail> listParamDtl = parameterDetailService
					.getParameterDetailByParamCode(ParameterHeader.PARAM_HEAD_CODE_REMINDER_STATUS);

			for (ParameterDetail vo : listParamDtl) {
				SelectItem si = new SelectItem();
				si.setLabel(vo.getName());
				si.setValue(vo.getParameterDtlCode());
				reminderStatusList.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setupComplianceStatus() {
		setComplianceStatusList(new ArrayList<SelectItem>());

		try {
			List<ParameterDetail> listComplianceDtl = parameterDetailService
					.getParameterDetailByParamCode(ParameterHeader.PARAM_HEAD_CODE_COMPLIANCE_CHECK_STATUS);

			for (ParameterDetail vo : listComplianceDtl) {
				SelectItem si = new SelectItem();
				si.setLabel(vo.getName());
				si.setValue(vo.getParameterDtlCode());
				getComplianceStatusList().add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
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
//			this.handleNew();
		} else {
			this.handleEdit(editId);
		}
	}

	private void handleEdit(String editId) {
		String token = facesUtil.retrieveRequestParam("token");
		if (StringUtils.isNotEmpty(token)) {
			editId = Constants.decryptString(token);
		}
		facesUtil.setSessionAttribute("token", Constants.encryptString(editId));
		actionMode = Constants.ACTION_EDIT;
		Long idLong = Long.parseLong(editId);

		//trcAuditPicFollowup = trcAuditPICFollowupService.findById(idLong);
		
		//trcAudit = trcAuditPicFollowup.getTrcAuditPicFollowupBankCommitment().getTrcAuditCheckPoint().getTrcAudit();
		trcAudit =  trcAuditService.findById(idLong);
		
		for(TrcAuditCheckPoint tapf : trcAudit.getTrcAuditCheckPoints() ) {
			
			if(tapf.getColumnModel() == null) {
				int idx = trcAudit.getTrcAuditCheckPoints().indexOf(tapf);
				ColumnModel cm = new ColumnModel();
				cm.setRow(idx);
				cm.setColumnModels(new ArrayList<ColumnModel>());
				
				int i=0;
				while(i < tapf.getColumn()) {
					cm.getColumnModels().add(new ColumnModel("column"+(i+1),"column"+(i+1)));
					i++;
				}
				
				tapf.setColumnModel(cm);
			}
			
			tapf.getTrcAuditPicFollowupBankCommitments().forEach( bCommit -> {
			
			if (bCommit.getTrcAuditPicFollowups() != null) {
				lastSequenceOfFollowup = bCommit.getTrcAuditPicFollowups().size();
				for (int i = 0; i < bCommit.getTrcAuditPicFollowups().size(); i++) {
					TrcAuditPicFollowup dtl = bCommit.getTrcAuditPicFollowups().get(i);
					lastSequenceOfFollowup = lastSequenceOfFollowup + 1;
					dtl.setSequence(lastSequenceOfFollowup);
					dtl.setOldTargetDate(dtl.getTargetDate());

				}
			}
			
			bCommit.getTrcAuditPicFollowups().forEach(picFollowup -> {
			
			if(picFollowup.getTrcAuditPicFollowupSupportingUnits() != null
					&& !picFollowup.getTrcAuditPicFollowupSupportingUnits().isEmpty())
				picFollowup.getTrcAuditPicFollowupSupportingUnits().forEach(supp -> {
					if (supp.getEmailCc1() != null)
						supp.setEmailCcTemp1(supp.getEmailCc1().getNik() + "-" + supp.getEmailCc1().getName());
					if (supp.getEmailCc2() != null)
						supp.setEmailCcTemp2(supp.getEmailCc2().getNik() + "-" + supp.getEmailCc2().getName());
					if (supp.getEmailCc3() != null)
						supp.setEmailCcTemp3(supp.getEmailCc3().getNik() + "-" + supp.getEmailCc3().getName());
				});
			});
		});
		}

		lastSequenceOfCompliance = 0;
		lastSequenceOfFollowup = 0;

		if (trcAudit.getCounterType() != null) {
			counterTypeId = trcAudit.getCounterType().getCounterTypeId();
			oldCounterTypeId = new Long(counterTypeId);
		}

		if (trcAudit.getTrcAuditPicCompliances() != null) {
			lastSequenceOfCompliance = trcAudit.getTrcAuditPicCompliances().size();
			for (int i = 0; i < trcAudit.getTrcAuditPicCompliances().size(); i++) {
				TrcAuditPicCompliance dtl = trcAudit.getTrcAuditPicCompliances().get(i);
				lastSequenceOfCompliance = lastSequenceOfCompliance + 1;
				dtl.setSequence(lastSequenceOfCompliance);
			}
		}

		
		setUploadedFilesAuditBankAttachment(new ArrayList<UploadedFileWO>());
		for (TrcAuditDocument tad : trcAudit.getTrcAuditDocuments()) {
			UploadedFileWO uf = new UploadedFileWO();
			uf.setFileName(tad.getAttachmentFile());
			uf.setFileId(tad.getFileId());
			uf.setIsNew(false);
			uf.setFileSize(tad.getFileSize());
			getUploadedFilesAuditBankAttachment().add(uf);
		}

		
		tableModelCompliance = new TrcAuditPICComplianceTableModel<TrcAuditPicCompliance>(
				trcAudit.getTrcAuditPicCompliances());
		
		//tableStatus = tmpAuditService.getDataConfirmStatusByAuditId(idLong);
		
		/*if(tableStatus == null)
			tableStatus = new ArrayList<AuditConfirmationVO>();
		
		AuditConfirmationVO vo = new AuditConfirmationVO();
		List list2 = new ArrayList<AttachmentVO>();
		if(trcAuditPicFollowup.getIsExtension()!=null && trcAuditPicFollowup.getIsExtension().equals("Y")) {
			
			vo.setFollowupBy(trcAuditPicFollowup.getFollowupBy().getName());
			
			try {
				vo.setFollowupStatus(trcAuditPicFollowup.getFollowupStatus().getNameIn());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			vo.setNewTargetDate(new SimpleDateFormat(inputDateFormat).format(trcAuditPicFollowup.getTrcAuditPicFollowupExt().get(trcAuditPicFollowup.getTrcAuditPicFollowupExt().size()-1).getNewTargetDate()));
			vo.setPerpanjanganNote(trcAuditPicFollowup.getRescheduleReason());
			AttachmentVO attachVo = new AttachmentVO();
			attachVo.setId(trcAuditPicFollowup.getTrcAuditPicFollowupExt().get(trcAuditPicFollowup.getTrcAuditPicFollowupExt().size()-1).getFileId());
			attachVo.setName(trcAuditPicFollowup.getTrcAuditPicFollowupExt().get(trcAuditPicFollowup.getTrcAuditPicFollowupExt().size()-1).getAttachmentFile());
			list2.add(attachVo);
			vo.setAttachmentsPerpanjangan(list2);
			
		}else {
			
			vo.setFollowupBy(trcAuditPicFollowup.getFollowupBy().getName());
			try {
				vo.setFollowupStatus(trcAuditPicFollowup.getFollowupStatus().getNameIn());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			vo.setFollowupDate(new SimpleDateFormat(inputDateFormat).format(trcAuditPicFollowup.getFollowupDate()));
			vo.setFollowupNote(trcAuditPicFollowup.getFollowupNote());
			
			for(int i=0;i<trcAuditPicFollowup.getTrcAuditPicFollowupAttachment().size();i++) {
				TrcAuditPicFollowupAttachment attch = trcAuditPicFollowup.getTrcAuditPicFollowupAttachment().get(i);
				AttachmentVO attachVo = new AttachmentVO();
				attachVo.setId(attch.getFileId());
				attachVo.setName(attch.getAttachmentFile());
				list2.add(attachVo);
			}
			vo.setAttachments(list2);
		
			
		}
		
		tableStatus.add(vo);*/

	}
	
	public void handleFileUpload(FileUploadEvent event) throws Exception {
		try {
		uploadedFiles = uploadedFiles == null ? new ArrayList<UploadedFileWO>() : uploadedFiles;
		uploadedFiles.add(new UploadedFileWO(
				CallApiManager.callUploadAPI(event.getFile(), Constants.COMPLIANCE_DOC_TYPE_AUDIT_FOLLOWUP,
						parameterDetailService, false, fileUtil),
				event.getFile().getFileName(), event.getFile().getContentType(), event.getFile().getSize()));
		} catch (Exception e) {
			e.printStackTrace();
			facesUtil.addErrMessage(e.getMessage());
		}
	}
	
	public void handleFileAttachmentLetterUpload(FileUploadEvent event) throws Exception {
		try {
			int idx = (int) event.getComponent().getAttributes().get("idx");
			
			/*if (trcAudit.getTrcAuditPicFollowups().get(idx).getUploadedFilesAttachmentLetter() == null)
				trcAudit.getTrcAuditPicFollowups().get(idx).setUploadedFilesAttachmentLetter(new ArrayList<UploadedFileWO>()) ;

			trcAudit.getTrcAuditPicFollowups().get(idx).getUploadedFilesAttachmentLetter().add(new UploadedFileWO(
					CallApiManager.callUploadAPI(event.getFile(), Constants.COMPLIANCE_DOC_TYPE_AUDIT_FOLLOWUP_LETTER,
							parameterDetailService, false, fileUtil),
					event.getFile().getFileName(), event.getFile().getContentType(), event.getFile().getSize()));*/
		} catch (Exception e) {
			e.printStackTrace();
			facesUtil.addErrMessage(e.getMessage());
		}
	}
	
	public void deleteConfirmationLetter(String fileId,int idxFollowup, int idxUpload) throws Exception {
		//CallApiManager.deleteFile(fileId, parameterDetailService, fileUtil);
		deletedFiles = deletedFiles!=null?deletedFiles: new ArrayList<UploadedFileWO>();
		
		deletedFiles.add(new UploadedFileWO(fileId,null,null, null));
		
		/*trcAudit.getTrcAuditPicFollowups().get(idxFollowup).getUploadedFilesAttachmentLetter()
			.remove(trcAudit.getTrcAuditPicFollowups().get(idxFollowup).getUploadedFilesAttachmentLetter().get(idxUpload));*/
		
	}

	public void downloadFile(String fileId, String fileName, byte[] content) throws Exception {
		CallApiManager.downloadFile(fileId, fileName, content, parameterDetailService);
	}
	
	public void deleteAttachment(String fileId,int index) throws Exception {
		//CallApiManager.deleteFile(fileId, parameterDetailService, fileUtil);
		deletedFiles = deletedFiles!=null?deletedFiles: new ArrayList<UploadedFileWO>();
		
		deletedFiles.add(new UploadedFileWO(fileId,null,null, null));
		
		uploadedFiles.remove(uploadedFiles.get(index));
		
	}

	public void cancel() {
		try {
			facesUtil.redirect("/pages/trcAuditView/trcAuditView.faces");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void save() {
		try {
			if (!isFoundError()) {
				User user = (User) facesUtil.getUserLogin();

				if(tableStatus!=null && tableStatus.size()>0) {
					AuditConfirmationVO vo = tableStatus.get(0);
					if(trcAuditPicFollowup.getTrcAuditPicFollowupBankCommitment().getIsRecurr()!=null && trcAuditPicFollowup.getTrcAuditPicFollowupBankCommitment().getIsRecurr().equals("Y") && vo.getComplianceStatusNew()!=null && vo.getComplianceStatusNew().equals("COMPLIANCE_CLOSE")) {
						TrcAuditPicFollowupRec trcAuditPicFollowupRec = trcAuditPicFollowup.getTrcAuditPicFollowupRecs().stream().filter(a->a.getAuditPicFollowupRecId().equals(trcAuditPicFollowup.getTrcAuditPicFollowupRec().getAuditPicFollowupRecId())).findFirst().orElse(null);
						if(trcAuditPicFollowupRec!=null) {
							ParameterDetail complianceStatus = parameterDetailService.getParameterDetailByParamDtlCode(
									vo.getComplianceStatusNew());
							trcAuditPicFollowupRec.setComplianceStatus(complianceStatus);
							trcAuditPicFollowupRec.setComplianceBy(user);
							trcAuditPicFollowupRec.setComplianceDate(new Date());
							trcAuditPicFollowupRec.setComplianceNote(vo.getComplianceNote());
							trcAuditPicFollowupRec.setFollowupStatus(trcAuditPicFollowup.getFollowupStatus());
							trcAuditPicFollowupRec.setFollowupBy(trcAuditPicFollowup.getFollowupBy());
							trcAuditPicFollowupRec.setConfirmationDate(trcAuditPicFollowup.getConfirmationDate());
							trcAuditPicFollowupRec.setFollowupDate(trcAuditPicFollowup.getFollowupDate());
							trcAuditPicFollowupRec.setFollowupNote(trcAuditPicFollowup.getFollowupNote());
							trcAuditPicFollowupRec.setNotes(trcAuditPicFollowup.getNotes());
							trcAuditPicFollowupRec.setRescheduleReason(trcAuditPicFollowup.getRescheduleReason());
							
							trcAuditPicFollowupRec.setLastUpdateBy(facesUtil.retrieveUserLogin());
							trcAuditPicFollowupRec.setLastUpdateDate(new Timestamp(new Date().getTime()));
							
							trcAuditPicFollowup.setFollowupStatus(null);
							trcAuditPicFollowup.setFollowupBy(null);
							trcAuditPicFollowup.setConfirmationDate(null);
							trcAuditPicFollowup.setFollowupDate(null);
							trcAuditPicFollowup.setFollowupNote(null);
							trcAuditPicFollowup.setIsExtension(null);
							trcAuditPicFollowup.setComplianceStatus(null);
							trcAuditPicFollowup.setComplianceBy(null);
							trcAuditPicFollowup.setComplianceDate(null);
							trcAuditPicFollowup.setComplianceNote(null);
							trcAuditPicFollowup.setLastUpdateBy(null);
							trcAuditPicFollowup.setLastUpdateDate(null);
							trcAuditPicFollowup.setNotes(null);
							trcAuditPicFollowup.setRescheduleReason(null);
							
							
							if(trcAuditPicFollowup.getTrcAuditPicFollowupExt()!=null && trcAuditPicFollowup.getTrcAuditPicFollowupExt().size()>0) {
								for(int i=0;i<trcAuditPicFollowup.getTrcAuditPicFollowupExt().size();i++) {
									TrcAuditPicFollowupExt trcAuditPicFollowupExt = trcAuditPicFollowup.getTrcAuditPicFollowupExt().get(i);
									TrcAuditPicFollowupRecExt trcAuditPicFollowupRecExt = new TrcAuditPicFollowupRecExt();
									trcAuditPicFollowupRecExt.setTrcAuditPicFollowupRec(trcAuditPicFollowupRec);
						
									trcAuditPicFollowupRecExt.setAttachmentFile(trcAuditPicFollowupExt.getAttachmentFile());
									trcAuditPicFollowupRecExt.setAttachmentType(trcAuditPicFollowupExt.getAttachmentType());
									trcAuditPicFollowupRecExt.setFileId(trcAuditPicFollowupExt.getFileId());
									trcAuditPicFollowupRecExt.setFileSize(trcAuditPicFollowupExt.getFileSize());
									trcAuditPicFollowupRecExt.setNewTargetDate(trcAuditPicFollowupExt.getNewTargetDate());
									trcAuditPicFollowupRecExt.setOldTargetDate(trcAuditPicFollowupExt.getOldTargetDate());
									
									EntityUtil.setCreationInfo(trcAuditPicFollowupRecExt, facesUtil.retrieveUserLogin());
									
									if(trcAuditPicFollowupRec.getTrcAuditPicFollowupRecExts() == null) {
										List<TrcAuditPicFollowupRecExt> list = new ArrayList<TrcAuditPicFollowupRecExt>();
										list.add(trcAuditPicFollowupRecExt);
										trcAuditPicFollowupRec.setTrcAuditPicFollowupRecExts(list);
									}else {
										trcAuditPicFollowupRec.getTrcAuditPicFollowupRecExts().add(trcAuditPicFollowupRecExt);
									}
								}
								
								trcAuditPicFollowup.getTrcAuditPicFollowupExt().clear();
							}
							
							if(trcAuditPicFollowup.getTrcAuditPicFollowupEmails()!=null && trcAuditPicFollowup.getTrcAuditPicFollowupEmails().size()>0) {
								
								trcAuditPicFollowup.getTrcAuditPicFollowupEmails().clear();
							}
							
							if(trcAuditPicFollowup.getTrcAuditPicFollowupAttachment()!=null && trcAuditPicFollowup.getTrcAuditPicFollowupAttachment().size()>0) {
								for(int i=0;i<trcAuditPicFollowup.getTrcAuditPicFollowupAttachment().size();i++) {
									TrcAuditPicFollowupAttachment trcAuditPicFollowupAttachment = trcAuditPicFollowup.getTrcAuditPicFollowupAttachment().get(i);
									TrcAuditPicFollowupRecAttachment trcAuditPicFollowupRecAttachment = new TrcAuditPicFollowupRecAttachment();
									trcAuditPicFollowupRecAttachment.setTrcAuditPicFollowupRec(trcAuditPicFollowupRec);
						
									trcAuditPicFollowupRecAttachment.setAttachmentFile(trcAuditPicFollowupAttachment.getAttachmentFile());
									trcAuditPicFollowupRecAttachment.setAttachmentType(trcAuditPicFollowupAttachment.getAttachmentType());
									trcAuditPicFollowupRecAttachment.setFileId(trcAuditPicFollowupAttachment.getFileId());
									trcAuditPicFollowupRecAttachment.setFileSize(trcAuditPicFollowupAttachment.getFileSize());
									
									EntityUtil.setCreationInfo(trcAuditPicFollowupRecAttachment, facesUtil.retrieveUserLogin());
									
									if(trcAuditPicFollowupRec.getTrcAuditPicFollowupRecAttchs() == null) {
										List<TrcAuditPicFollowupRecAttachment> list = new ArrayList<TrcAuditPicFollowupRecAttachment>();
										list.add(trcAuditPicFollowupRecAttachment);
										trcAuditPicFollowupRec.setTrcAuditPicFollowupRecAttchs(list);
									}else {
										trcAuditPicFollowupRec.getTrcAuditPicFollowupRecAttchs().add(trcAuditPicFollowupRecAttachment);
									}
								}
								trcAuditPicFollowup.getTrcAuditPicFollowupAttachment().clear();
							}
							
							
							trcAuditPICFollowupService.update(trcAuditPicFollowup);
							
							trcAuditPicFollowup = trcAuditPICFollowupService.findById(trcAuditPicFollowup.getAuditPicFollowupId());
							
							TrcAuditPicFollowupRec trcAuditPicFollowupRecNew = trcAuditPicFollowup.getTrcAuditPicFollowupRecs().stream().filter(a->a.getComplianceDate() == null).findFirst().orElse(null);
							if(trcAuditPicFollowupRecNew!=null) {
							trcAuditPicFollowup.setTargetDate(trcAuditPicFollowupRecNew.getTargetDate());	
							trcAuditPicFollowup.setTrcAuditPicFollowupRec(trcAuditPicFollowupRecNew);
							for(int i=0;i<trcAuditPicFollowupRecNew.getTrcAuditPicFollowupRecEmails().size();i++) {
								TrcAuditPicFollowupRecEmail trcAuditPicFollowupRecEmail = trcAuditPicFollowupRecNew.getTrcAuditPicFollowupRecEmails().get(i);
								TrcAuditPicFollowupEmail trcAuditPicFollowupEmail = new TrcAuditPicFollowupEmail();
								trcAuditPicFollowupEmail.setTrcAuditPicFollowup(trcAuditPicFollowupRecEmail.getTrcAuditPicFollowupRec().getTrcAuditPicFollowup());
					
								trcAuditPicFollowupEmail.setEmailDate(trcAuditPicFollowupRecEmail.getEmailDate());
								trcAuditPicFollowupEmail.setSlaType(trcAuditPicFollowupRecEmail.getSlaType());
								trcAuditPicFollowupEmail.setSla(trcAuditPicFollowupRecEmail.getSla());
								
								EntityUtil.setCreationInfo(trcAuditPicFollowupEmail, facesUtil.retrieveUserLogin());
								
								trcAuditPicFollowup.getTrcAuditPicFollowupEmails().add(trcAuditPicFollowupEmail);
							}
							trcAuditPICFollowupService.update(trcAuditPicFollowup);
							}
							
						}else {
							ParameterDetail complianceStatus = parameterDetailService.getParameterDetailByParamDtlCode(
									vo.getComplianceStatusNew());
							trcAuditPicFollowup.setComplianceStatus(complianceStatus);
							trcAuditPicFollowup.setComplianceBy(user);
							trcAuditPicFollowup.setComplianceDate(new Date());
							trcAuditPicFollowup.setComplianceNote(vo.getComplianceNote());
							
							if(trcAuditPicFollowup.getIsExtension()!=null && trcAuditPicFollowup.getIsExtension().equals("Y")) {
								if("COMPLIANCE_APPROPRIATE".equals(vo.getComplianceStatus())) {
									trcAuditPicFollowup.setTargetDate(new SimpleDateFormat(inputDateFormat).parse(vo.getNewTargetDate()));
									trcAuditPicFollowup.setIsExtension(null);
								}
								
								ParameterDetail followupStatus = parameterDetailService.getParameterDetailByParamDtlCode(
										ParameterDetail.PARAM_DET_CODE_PIC_FOLLOWUP_STATUS_PIC_INPROGRESS);
								trcAuditPicFollowup.setFollowupStatus(followupStatus);
								
							}
							else if(vo.getComplianceStatusNew().equals("COMPLIANCE_OPEN")) {
								ParameterDetail followupStatus = parameterDetailService.getParameterDetailByParamDtlCode(
										ParameterDetail.PARAM_DET_CODE_PIC_FOLLOWUP_STATUS_PIC_INPROGRESS);
								trcAuditPicFollowup.setFollowupStatus(followupStatus);
							}
							
							
							trcAuditPicFollowup.setLastUpdateBy(facesUtil.retrieveUserLogin());
							trcAuditPicFollowup.setLastUpdateDate(new Timestamp(new Date().getTime()));
							trcAuditPICFollowupService.update(trcAuditPicFollowup);
						}
					}else {
						ParameterDetail complianceStatus = parameterDetailService.getParameterDetailByParamDtlCode(
								vo.getComplianceStatusNew());
						trcAuditPicFollowup.setComplianceStatus(complianceStatus);
						trcAuditPicFollowup.setComplianceBy(user);
						trcAuditPicFollowup.setComplianceDate(new Date());
						trcAuditPicFollowup.setComplianceNote(vo.getComplianceNote());
						
						if(trcAuditPicFollowup.getIsExtension()!=null && trcAuditPicFollowup.getIsExtension().equals("Y")) {
							if("COMPLIANCE_APPROPRIATE".equals(vo.getComplianceStatus())) {
								trcAuditPicFollowup.setTargetDate(new SimpleDateFormat(inputDateFormat).parse(vo.getNewTargetDate()));
								trcAuditPicFollowup.setIsExtension(null);
							}
							
							ParameterDetail followupStatus = parameterDetailService.getParameterDetailByParamDtlCode(
									ParameterDetail.PARAM_DET_CODE_PIC_FOLLOWUP_STATUS_PIC_INPROGRESS);
							trcAuditPicFollowup.setFollowupStatus(followupStatus);
							
						}
						else if(vo.getComplianceStatusNew().equals("COMPLIANCE_OPEN")) {
							ParameterDetail followupStatus = parameterDetailService.getParameterDetailByParamDtlCode(
									ParameterDetail.PARAM_DET_CODE_PIC_FOLLOWUP_STATUS_PIC_INPROGRESS);
							trcAuditPicFollowup.setFollowupStatus(followupStatus);
						}
						
						if(trcAuditPicFollowup.getTrcAuditPicFollowupBankCommitment().getIsRecurr()!=null && trcAuditPicFollowup.getTrcAuditPicFollowupBankCommitment().getIsRecurr().equals("Y")) {
							TrcAuditPicFollowupRec trcAuditPicFollowupRec = trcAuditPicFollowup.getTrcAuditPicFollowupRecs().stream().filter(a->a.getAuditPicFollowupRecId().equals(trcAuditPicFollowup.getTrcAuditPicFollowupRec().getAuditPicFollowupRecId())).findFirst().orElse(null);
							if(trcAuditPicFollowupRec!=null) {
								trcAuditPicFollowupRec.setComplianceStatus(complianceStatus);
								trcAuditPicFollowupRec.setComplianceBy(user);
								trcAuditPicFollowupRec.setComplianceDate(new Date());
								trcAuditPicFollowupRec.setComplianceNote(vo.getComplianceNote());
								trcAuditPicFollowupRec.setFollowupStatus(trcAuditPicFollowup.getFollowupStatus());
								trcAuditPicFollowupRec.setFollowupBy(trcAuditPicFollowup.getFollowupBy());
								trcAuditPicFollowupRec.setConfirmationDate(trcAuditPicFollowup.getConfirmationDate());
								trcAuditPicFollowupRec.setFollowupDate(trcAuditPicFollowup.getFollowupDate());
								trcAuditPicFollowupRec.setFollowupNote(trcAuditPicFollowup.getFollowupNote());
								trcAuditPicFollowupRec.setNotes(trcAuditPicFollowup.getNotes());
								trcAuditPicFollowupRec.setRescheduleReason(trcAuditPicFollowup.getRescheduleReason());
								
								trcAuditPicFollowupRec.setLastUpdateBy(facesUtil.retrieveUserLogin());
								trcAuditPicFollowupRec.setLastUpdateDate(new Timestamp(new Date().getTime()));
							}
						}
						
						trcAuditPicFollowup.setLastUpdateBy(facesUtil.retrieveUserLogin());
						trcAuditPicFollowup.setLastUpdateDate(new Timestamp(new Date().getTime()));
						trcAuditPICFollowupService.update(trcAuditPicFollowup);
						
						
					}
					
				}
				//trcAuditVerificationService.processConfirm(trcAudit, user);

				facesUtil.redirect(
						"/pages/trcAuditVerification/trcAuditVerification.faces");
			}

		} catch (Exception ex) {
			facesUtil.addFacesMsg(FacesMessage.SEVERITY_ERROR, null, "Operation Failed : " + ex.getMessage(), "");
			ex.printStackTrace();
		}
	}
	
	public Boolean isFoundError() {
		Boolean flag = false;
								
		/*if (trcAudit.getTrcAuditPicFollowups() != null) {
			for (int i = 0; i < trcAudit.getTrcAuditPicFollowups().size(); i++) {
				TrcAuditPicFollowup dtl = (TrcAuditPicFollowup) trcAudit.getTrcAuditPicFollowups().get(i);

				// jika followup selesai
				if (dtl.getFollowupStatus() != null 
						&& dtl.getFollowupStatus().getParameterDtlCode()
								.equals(ParameterDetail.PARAM_DET_CODE_PIC_FOLLOWUP_STATUS_PIC_DONE)) {
					
					if (dtl.getComplianceStatus() != null && dtl.getComplianceStatus().getParameterDtlCode() != null) {
						
						// jika status compliance selesai, maka mandatory
						if(dtl.getComplianceStatus().getParameterDtlCode()
									.equals(ParameterDetail.PARAM_DET_CODE_COMPLIANCE_CHECK_STATUS_COMPLIANCE_OPEN)) {
						
							if (StringUtils.isEmpty(dtl.getComplianceNote())) {
								facesUtil.addErrMessage(
										facesUtil.retrieveMessage("formAuditVerificationComplianceCheckerNote") + " "
												+ facesUtil.retrieveMessage("validateRequired"));
	
								flag = true;
							}
							
						}else if(StringUtils.isBlank(dtl.getComplianceStatus().getParameterDtlCode())) {
							facesUtil.addErrMessage(facesUtil.retrieveMessage("formAuditVerificationComplianceCheckerStatus")
									+ " " + facesUtil.retrieveMessage("validateRequired"));
							flag = true;
						}
						
					} else {
						facesUtil.addErrMessage(facesUtil.retrieveMessage("formAuditVerificationComplianceCheckerStatus")
								+ " " + facesUtil.retrieveMessage("validateRequired"));
						flag = true;
					}
				}
//				if ((dtl.getComplianceStatus() == null
//						|| StringUtils.isBlank(dtl.getComplianceStatus().getParameterDtlCode()))
//						&& dtl.getFollowupStatus() != null && dtl.getFollowupStatus().getParameterDtlCode()
//								.equals(ParameterDetail.PARAM_DET_CODE_PIC_FOLLOWUP_STATUS_PIC_DONE)) {
//					facesUtil.addErrMessage(facesUtil.retrieveMessage("formAuditVerificationComplianceCheckerStatus")
//							+ " " + facesUtil.retrieveMessage("validateRequired"));
//					flag = true;
//				} else {
				
//					if (dtl.getOldComplianceStatus() != null && dtl.getOldComplianceStatus().getParameterDtlCode() != null
//						&& dtl.getOldComplianceStatus().getParameterDtlCode()
//								.equals(ParameterDetail.PARAM_DET_CODE_COMPLIANCE_CHECK_STATUS_COMPLIANCE_CLOSE)
//						&& (dtl.getComplianceStatus() == null
//								|| StringUtils.isBlank(dtl.getComplianceStatus().getParameterDtlCode()))) {
//						facesUtil.addErrMessage(facesUtil.retrieveMessage("formAuditVerificationComplianceCheckerStatus")
//								+ " " + facesUtil.retrieveMessage("validateRequired"));
//						flag = true;
//					}
//				
//					if (dtl.getComplianceStatus() != null && dtl.getComplianceStatus().getParameterDtlCode() != null
//							&& dtl.getComplianceStatus().getParameterDtlCode()
//									.equals(ParameterDetail.PARAM_DET_CODE_COMPLIANCE_CHECK_STATUS_COMPLIANCE_OPEN)) {
//						if (StringUtils.isEmpty(dtl.getComplianceNote())) {
//							facesUtil.addErrMessage(
//									facesUtil.retrieveMessage("formAuditVerificationComplianceCheckerNote") + " "
//											+ facesUtil.retrieveMessage("validateRequired"));
//
//							flag = true;
//						}
//					}
//				}
			}
		}*/
		
//		if((trcAuditPicFollowup.getComplianceStatus() == null ||
//				StringUtils.isBlank(trcAuditPicFollowup.getComplianceStatus().getParameterDtlCode())) && trcAuditPicFollowup.getFollowupStatus() != null 
//				&& trcAuditPicFollowup.getFollowupStatus().getParameterDtlCode().equals(ParameterDetail.PARAM_DET_CODE_PIC_FOLLOWUP_STATUS_PIC_DONE)) {
//			facesUtil.addErrMessage(facesUtil.retrieveMessage("formAuditVerificationComplianceCheckerStatus") + " "
//					+ facesUtil.retrieveMessage("validateRequired"));
//			flag = true;
//		} else {				
//			if(trcAuditPicFollowup.getComplianceStatus() != null && trcAuditPicFollowup.getComplianceStatus().getParameterDtlCode() != null &&
//					trcAuditPicFollowup.getComplianceStatus().getParameterDtlCode().
//					equals(ParameterDetail.PARAM_DET_CODE_COMPLIANCE_CHECK_STATUS_COMPLIANCE_OPEN)) {
//				if (StringUtils.isEmpty(trcAuditPicFollowup.getComplianceNote())) {
//					facesUtil.addErrMessage(facesUtil.retrieveMessage("formAuditVerificationComplianceCheckerNote") + " "
//							+ facesUtil.retrieveMessage("validateRequired"));
//					
//					flag = true;
//				}
//			}
//		}

		return flag;
	}
	
	public void sendEmail() {
		try {

			if(tableStatus!=null && tableStatus.size()>0) {
				AuditConfirmationVO vo = tableStatus.get(0);
				
				String emailTemplateCode = null;
				
				if(vo.getComplianceStatus()!=null && vo.getComplianceStatus().equals("COMPLIANCE_CLOSE")) {
					emailTemplateCode = "EMAIL_TEMPLATE_AUDIT_CLOSED";
				}
				else if(vo.getComplianceStatus()!=null && vo.getComplianceStatus().equals("COMPLIANCE_OPEN")) {
					emailTemplateCode = "EMAIL_TEMPLATE_AUDIT_OPEN";
				}
				else if(vo.getComplianceStatus()!=null && vo.getComplianceStatus().equals("COMPLIANCE_APPROPRIATE")) {
					emailTemplateCode = "EMAIL_TEMPLATE_AUDIT_APPROPRIATE";
				}
				else if(vo.getComplianceStatus()!=null && vo.getComplianceStatus().equals("COMPLIANCE_NOT_APPROPRIATE")) {
					emailTemplateCode = "EMAIL_TEMPLATE_AUDIT_APPROPRIATE";
				}else {
					emailTemplateCode = "EMAIL_TEMPLATE_AUDIT";
				}
			
			ParameterDetail pdHost = parameterDetailService.getParameterDetailByParamDtlCode("HOST_NAME_APPLICATION");
			EmailTemplate emailTemplate = getEmailTemplateService()
					.getEmailTemplateByEmailTemplateCode(emailTemplateCode);
			String emailSubject = emailTemplate.getEmailSubject();

			String emailContent = "";
			String emailTo = "";
			String emailCc = "";

			emailTo = trcAuditPicFollowup.getUser1().getEmail();
			
			String token = Constants.encryptString(trcAuditPicFollowup.getAuditPicFollowupId().toString());
			String menuId = Constants.encryptString(Constants.MENU_ID_FOLLOWUP_CONFIRMATION_AUDIT);
			String urlLink = pdHost.getNameIn().concat("pages/auditFE/auditFEEdit.faces?token="+token+"&menuId="+menuId);
			emailContent = emailContent.replaceAll("url_link", urlLink);

				final String subject = emailSubject;
				final String content = emailContent;
				final String to = emailTo;
				// final String to = "h3ndr407@gmail.com";
				final String cc = emailCc;

				CallApiManager.sendEmailAPI(to, cc, subject, content, emailTemplateCode, "true",
						parameterDetailService);

			
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	public void onChangeAuditMaster() {
		MstAuditVO getSingleData = mstAuditService.getSingleDataMstAudit(trcAudit.getMstAudit().getMstAuditId());
		
		try {
			trcAudit.setAuditor(getSingleData.getAuditorCode());
			trcAudit.setAuditDateFrom(DateUtil.stringToDateFromDDMMMYYYY(getSingleData.getAuditDateFrom()));
			trcAudit.setAuditDateTo(DateUtil.stringToDateFromDDMMMYYYY(getSingleData.getAuditDateTo()));
			trcAudit.setScope(getSingleData.getScope());
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	public FacesUtil getFacesUtil() {
		return facesUtil;
	}

	public void setFacesUtil(FacesUtil facesUtil) {
		this.facesUtil = facesUtil;
	}

	public String getNavigateSearch() {
		return navigateSearch;
	}

	public void setNavigateSearch(String navigateSearch) {
		this.navigateSearch = navigateSearch;
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

	public SimpleDateFormat getSdf() {
		return sdf;
	}

	public void setSdf(SimpleDateFormat sdf) {
		this.sdf = sdf;
	}

	public static Logger getLogger() {
		return logger;
	}


	public RegulationTrackRecord getSelectedRow() {
		return selectedRow;
	}

	public void setSelectedRow(RegulationTrackRecord selectedRow) {
		this.selectedRow = selectedRow;
	}

	public Integer getIndexDtl() {
		return indexDtl;
	}

	public void setIndexDtl(Integer indexDtl) {
		this.indexDtl = indexDtl;
	}

	public Integer getIndexDtlCompliance() {
		return indexDtlCompliance;
	}

	public void setIndexDtlCompliance(Integer indexDtlCompliance) {
		this.indexDtlCompliance = indexDtlCompliance;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public Integer getIndexDtlFollowup() {
		return indexDtlFollowup;
	}

	public void setIndexDtlFollowup(Integer indexDtlFollowup) {
		this.indexDtlFollowup = indexDtlFollowup;
	}

	public List<SelectItem> getDivisions() {
		return divisions;
	}

	public void setDivisions(List<SelectItem> divisions) {
		this.divisions = divisions;
	}

	public CounterTypeService getCounterTypeService() {
		return counterTypeService;
	}

	public void setCounterTypeService(CounterTypeService counterTypeService) {
		this.counterTypeService = counterTypeService;
	}

	public List<SelectItem> getCounterTypes() {
		return counterTypes;
	}

	public void setCounterTypes(List<SelectItem> counterTypes) {
		this.counterTypes = counterTypes;
	}

	public List<AuditConfirmationVO> getTableStatus() {
		return tableStatus;
	}

	public void setTableStatus(List<AuditConfirmationVO> tableStatus) {
		this.tableStatus = tableStatus;
	}

	public Long getCounterTypeId() {
		return counterTypeId;
	}

	public void setCounterTypeId(Long counterTypeId) {
		this.counterTypeId = counterTypeId;
	}

	public List<SelectItem> getReminderStatusList() {
		return reminderStatusList;
	}

	public void setReminderStatusList(List<SelectItem> reminderStatusList) {
		this.reminderStatusList = reminderStatusList;
	}

	public Long getOldCounterTypeId() {
		return oldCounterTypeId;
	}

	public void setOldCounterTypeId(Long oldCounterTypeId) {
		this.oldCounterTypeId = oldCounterTypeId;
	}

	public Long getNewCounterTypeId() {
		return newCounterTypeId;
	}

	public void setNewCounterTypeId(Long newCounterTypeId) {
		this.newCounterTypeId = newCounterTypeId;
	}

	public Integer getLastSequenceOfCompliance() {
		return lastSequenceOfCompliance;
	}

	public void setLastSequenceOfCompliance(Integer lastSequenceOfCompliance) {
		this.lastSequenceOfCompliance = lastSequenceOfCompliance;
	}

	public Integer getLastSequenceOfFollowup() {
		return lastSequenceOfFollowup;
	}

	public void setLastSequenceOfFollowup(Integer lastSequenceOfFollowup) {
		this.lastSequenceOfFollowup = lastSequenceOfFollowup;
	}

	public List<SelectItem> getSelectAuditFollowUp() {
		return selectAuditFollowUp;
	}

	public void setSelectAuditFollowUp(List<SelectItem> selectAuditFollowUp) {
		this.selectAuditFollowUp = selectAuditFollowUp;
	}

	public List<SelectItem> getSelectAuditObject() {
		return selectAuditObject;
	}

	public void setSelectAuditObject(List<SelectItem> selectAuditObject) {
		this.selectAuditObject = selectAuditObject;
	}

	public List<SelectItem> getSelectAuditCategory() {
		return selectAuditCategory;
	}

	public void setSelectAuditCategory(List<SelectItem> selectAuditCategory) {
		this.selectAuditCategory = selectAuditCategory;
	}

	public List<UploadedFileWO> getUploadedFilesAuditFindings() {
		return uploadedFilesAuditFindings;
	}

	public void setUploadedFilesAuditFindings(List<UploadedFileWO> uploadedFilesAuditFindings) {
		this.uploadedFilesAuditFindings = uploadedFilesAuditFindings;
	}

	public FileUtil getFileUtil() {
		return fileUtil;
	}

	public void setFileUtil(FileUtil fileUtil) {
		this.fileUtil = fileUtil;
	}

	public List<UploadedFileWO> getUploadedFilesAuditBankResponse() {
		return uploadedFilesAuditBankResponse;
	}

	public void setUploadedFilesAuditBankResponse(List<UploadedFileWO> uploadedFilesAuditBankResponse) {
		this.uploadedFilesAuditBankResponse = uploadedFilesAuditBankResponse;
	}

	public List<UploadedFileWO> getUploadedFilesAuditBankCommitment() {
		return uploadedFilesAuditBankCommitment;
	}

	public void setUploadedFilesAuditBankCommitment(List<UploadedFileWO> uploadedFilesAuditBankCommitment) {
		this.uploadedFilesAuditBankCommitment = uploadedFilesAuditBankCommitment;
	}

	public TrcAuditPICComplianceTableModel<TrcAuditPicCompliance> getTableModelCompliance() {
		return tableModelCompliance;
	}

	public void setTableModelCompliance(TrcAuditPICComplianceTableModel<TrcAuditPicCompliance> tableModelCompliance) {
		this.tableModelCompliance = tableModelCompliance;
	}

	public TrcAuditPicCompliance[] getSelectedDataCompliance() {
		return selectedDataCompliance;
	}

	public void setSelectedDataCompliance(TrcAuditPicCompliance[] selectedDataCompliance) {
		this.selectedDataCompliance = selectedDataCompliance;
	}

	public TrcAuditPICFollowupTableModel<TrcAuditPicFollowup> getTableModelFollowup() {
		return tableModelFollowup;
	}

	public void setTableModelFollowup(TrcAuditPICFollowupTableModel<TrcAuditPicFollowup> tableModelFollowup) {
		this.tableModelFollowup = tableModelFollowup;
	}

	public TrcAuditPicFollowup[] getSelectedDataFollowup() {
		return selectedDataFollowup;
	}

	public void setSelectedDataFollowup(TrcAuditPicFollowup[] selectedDataFollowup) {
		this.selectedDataFollowup = selectedDataFollowup;
	}

	public List<SelectItem> getFollowUps() {
		return followUps;
	}

	public void setFollowUps(List<SelectItem> followUps) {
		this.followUps = followUps;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public TrcAudit getTrcAudit() {
		return trcAudit;
	}

	public void setTrcAudit(TrcAudit trcAudit) {
		this.trcAudit = trcAudit;
	}

	public TrcAuditService getTrcAuditService() {
		return trcAuditService;
	}

	public void setTrcAuditService(TrcAuditService trcAuditService) {
		this.trcAuditService = trcAuditService;
	}

	public String getViewOnly() {
		return viewOnly;
	}

	public void setViewOnly(String viewOnly) {
		this.viewOnly = viewOnly;
	}

	public List<UploadedFileWO> getUploadedFiles() {
		return uploadedFiles;
	}

	public void setUploadedFiles(List<UploadedFileWO> uploadedFiles) {
		this.uploadedFiles = uploadedFiles;
	}

	public List<UploadedFileWO> getDeletedFiles() {
		return deletedFiles;
	}

	public void setDeletedFiles(List<UploadedFileWO> deletedFiles) {
		this.deletedFiles = deletedFiles;
	}

	public EmailTemplateService getEmailTemplateService() {
		return emailTemplateService;
	}

	public void setEmailTemplateService(EmailTemplateService emailTemplateService) {
		this.emailTemplateService = emailTemplateService;
	}

	public TmpAuditService getTmpAuditService() {
		return tmpAuditService;
	}

	public void setTmpAuditService(TmpAuditService tmpAuditService) {
		this.tmpAuditService = tmpAuditService;
	}



	public List<SelectItem> getComplianceStatusList() {
		return complianceStatusList;
	}

	public void setComplianceStatusList(List<SelectItem> complianceStatusList) {
		this.complianceStatusList = complianceStatusList;
	}

	public List<SelectItem> getYesNo() {
		return yesNo;
	}

	public void setYesNo(List<SelectItem> yesNo) {
		this.yesNo = yesNo;
	}

	public TrcAuditPicFollowup getTrcAuditPicFollowup() {
		return trcAuditPicFollowup;
	}

	public void setTrcAuditPicFollowup(TrcAuditPicFollowup trcAuditPicFollowup) {
		this.trcAuditPicFollowup = trcAuditPicFollowup;
	}

	public TrcAuditPICFollowupService getTrcAuditPICFollowupService() {
		return trcAuditPICFollowupService;
	}

	public void setTrcAuditPICFollowupService(TrcAuditPICFollowupService trcAuditPICFollowupService) {
		this.trcAuditPICFollowupService = trcAuditPICFollowupService;
	}

	public int getIdxFollowup() {
		return idxFollowup;
	}

	public void setIdxFollowup(int idxFollowup) {
		this.idxFollowup = idxFollowup;
	}

	public List<UploadedFileWO> getUploadedFilesAuditBankAttachment() {
		return uploadedFilesAuditBankAttachment;
	}

	public void setUploadedFilesAuditBankAttachment(List<UploadedFileWO> uploadedFilesAuditBankAttachment) {
		this.uploadedFilesAuditBankAttachment = uploadedFilesAuditBankAttachment;
	}

	public MstAuditService getMstAuditService() {
		return mstAuditService;
	}

	public void setMstAuditService(MstAuditService mstAuditService) {
		this.mstAuditService = mstAuditService;
	}

	public List<SelectItem> getTemplateAuditList() {
		return templateAuditList;
	}

	public void setTemplateAuditList(List<SelectItem> templateAuditList) {
		this.templateAuditList = templateAuditList;
	}

	public String getTextWarningUpload() {
		return textWarningUpload;
	}

	public void setTextWarningUpload(String textWarningUpload) {
		this.textWarningUpload = textWarningUpload;
	}

	
}