package com.wo.module.complianceTestingMockup.bean;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.model.SelectItem;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.FlowEvent;

import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.exception.CustomAPIException;
import com.wo.module.common.js.JsUtil;
import com.wo.module.common.model.UploadedFileWO;
import com.wo.module.common.util.FileUtil;
import com.wo.module.common.util.MathUtil;
import com.wo.module.common.utility.CallApiManager;
import com.wo.module.complianceTestingMockup.constant.ComplianceTestingMockupConstants;
import com.wo.module.complianceTestingMockup.model.ComplianceTesting;
import com.wo.module.complianceTestingMockup.model.ComplianceTestingDoc;
import com.wo.module.complianceTestingMockup.model.ComplianceTestingDtl;
import com.wo.module.complianceTestingMockup.model.ComplianceTestingPICFollowup;
import com.wo.module.complianceTestingMockup.model.ComplianceTestingPICReview;
import com.wo.module.complianceTestingMockup.model.ComplianceTestingPICReviewTableModel;
import com.wo.module.complianceTestingMockup.model.PICTindakLanjutTableModel;
import com.wo.module.complianceTestingMockup.service.ComplianceTestingService;
import com.wo.module.complianceTestingMockup.vo.AttachmentVO;
import com.wo.module.complianceTestingMockup.vo.StatusKonfirmasiVO;
import com.wo.module.complianceTestingMockup.vo.SubjectVO;
import com.wo.module.counterType.model.CounterType;
import com.wo.module.counterType.service.CounterTypeService;
import com.wo.module.emailTemplate.model.EmailTemplate;
import com.wo.module.emailTemplate.service.EmailTemplateService;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.lov.bean.SelectorListener;
import com.wo.module.lov.bean.SelectorModel.SelectorInfo;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.parameter.model.ParameterHeader;
import com.wo.module.tmpAudit.model.TmpAuditDocument;
import com.wo.module.tmpComplianceReview.model.TmpComplianceReviewDocument;
import com.wo.module.trcComplianceReview.model.TrcComplianceReviewPicCompliance;
import com.wo.module.user.model.Branch;
import com.wo.module.user.model.Division;
import com.wo.module.user.model.User;
import com.wo.module.user.service.UserService;

public class ComplianceTestingMockupEditBean extends CommonBean implements SelectorListener<Object>, Serializable {

	private static final long serialVersionUID = -7277046569343787246L;

	static Logger logger = Logger.getLogger(ComplianceTestingMockupEditBean.class);

	private List<SelectItem> divisions;
	
	private List<SelectItem> branchs;
	
	private List<SubjectVO> subjects;
	
	private List<StatusKonfirmasiVO> tableStatus;
	
	private Long divisionId;
	
	private String noPemeriksaan;
	
	private String jdlPemeriksaan;
	
	private String ratingKeseluruhan;
	
	private List<SelectItem> counterTypes;
	
	private List<SelectItem> ratings;

	public FacesUtil facesUtil;
	
	private FileUtil fileUtil;
	
	private SelectorInfo selectorDivision;

	private String navigateSearch = ComplianceTestingMockupConstants.NAVIGATE_SEARCH;

	SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");

	private List<UploadedFileWO> uploadedFilesDocument;
	private List<UploadedFileWO> deletedFiles;

	private String REMINDER_ACTIVE = ParameterDetail.PARAM_DET_CODE_REMINDER_ACTIVE;
	private String REMINDER_INACTIVE = ParameterDetail.PARAM_DET_CODE_REMINDER_INACTIVE;

	private List<SelectItem> searchType;

	private Boolean isViewOnly;
	
	private ComplianceTesting complianceTesting;
	
	private String actionMode;
	
	private Boolean isNewData;
	
	private Boolean tindakLanjut;
	
	private Date startDate;
	
	private Date endDate;
	
	private String textWarningUpload;
	
	private String keterangan;
	
	private Long counterTypeId;
	
	private Integer indexDtlComplianceTestingPICReview;
	
	private Integer indexDtlComplianceFw;
	
	private Integer indexDtlCompliancePiCFollowup;
	
	private Integer indexDtlSubject;
	
	private List<SelectItem> complianceStatusList;
	
	private List<UploadedFileWO> uploadedFilesAttachment;
	private List<UploadedFileWO> uploadedFiles;
	private List<UploadedFileWO> deletedAttachment;
	
	private SelectorInfo selectorCompliance;
	
	private ComplianceTestingPICReview[] selectedDataComplianceTestingPICReview;
	private ComplianceTestingPICReviewTableModel<ComplianceTestingPICReview> tableModelComplianceTestingPICReview;
	private List<ComplianceTestingPICReview> complianceTestingPICReviews = new ArrayList<ComplianceTestingPICReview>();
	private Integer lastSequenceOfCompliance;
	
	private SelectorInfo selectorComplianceFw;
	
	private SelectorInfo selectorBranch;
	
	private SelectorInfo selectorComplianceRpt;
	private ComplianceTestingPICFollowup[] selectedDataComplianceRpt;
	private PICTindakLanjutTableModel<ComplianceTestingPICFollowup> tableModelComplianceTestingFollowup;
	private List<ComplianceTestingPICFollowup> complianceTestingPICFollowup = new ArrayList<ComplianceTestingPICFollowup>();
	private Integer lastSequenceOfComplianceRpt;
	
	private UserService userService;
	private CounterTypeService counterTypeService;
	private ComplianceTestingService complianceTestingService;
	private EmailTemplateService emailTemplateService;
	
	private Boolean isPreliminary;
	private Boolean isFieldWork;
	private Boolean isReporting;
	
	@PostConstruct
	public void init() {
		System.out.println("masuk init");
		super.init();
		
		selectDivision();
		selectBranch(null);
		selectComplianceStatus();
		setupCounterType();
		selectRating();
		selectorCompliance = ComplianceTestingMockupConstants.buildSelectorPICCompliance(facesUtil);
		
		selectorComplianceFw = ComplianceTestingMockupConstants.buildSelectorPIC(facesUtil);
		
		selectorBranch = ComplianceTestingMockupConstants.buildSelectorBranch(facesUtil);
		
		checkNewOrEdit();
		
		if(tableStatus == null)
			tableStatus = new ArrayList<StatusKonfirmasiVO>();
		
		StatusKonfirmasiVO vo = new StatusKonfirmasiVO();
		vo.setFollowupBy("Nadia");
		vo.setFollowupStatus("Selesai");
		vo.setFollowupDate("07-11-2022");
		vo.setFollowupStatus("Selesai");
		vo.setFollowupNote("Ok");
		AttachmentVO attachVo = new AttachmentVO();
		attachVo.setId("id");
		attachVo.setName("bukti.jpg");
		attachVo.setSize("100");
		List list2 = new ArrayList<AttachmentVO>();
		list2.add(attachVo);
		vo.setAttachments(list2);
		
		//list.add(vo);
		tableStatus.add(vo);
		
		StatusKonfirmasiVO vo2 = new StatusKonfirmasiVO();
		vo2.setFollowupBy("Nadia");
		vo2.setFollowupStatus("Perpanjangan");
		vo2.setNewTargetDate("07-11-2022");
		vo2.setPerpanjanganNote("Ok");
		vo2.setAttachmentsPerpanjangan(list2);
		//list.add(vo2);
		tableStatus.add(vo2);
		
		fileUtil = FileUtil.getInstance();
	}
	
	public void onSelectDivision(int indexSbj,int indexFollowup) {
		Long divId = complianceTesting.getComplianceTestingDtls().get(indexSbj).getComplianceTestingPICFollowups().get(indexFollowup).getDivisionId();
		
		complianceTesting.getComplianceTestingDtls().get(indexSbj).getComplianceTestingPICFollowups().get(indexFollowup).setBranchCode(null);
		complianceTesting.getComplianceTestingDtls().get(indexSbj).getComplianceTestingPICFollowups().get(indexFollowup).setBranchName(null);
		complianceTesting.getComplianceTestingDtls().get(indexSbj).getComplianceTestingPICFollowups().get(indexFollowup).setUser1(new User());
		complianceTesting.getComplianceTestingDtls().get(indexSbj).getComplianceTestingPICFollowups().get(indexFollowup).setUser2(new User());
		complianceTesting.getComplianceTestingDtls().get(indexSbj).getComplianceTestingPICFollowups().get(indexFollowup).setUser3(new User());
		
		PrimeFaces.current().ajax()
		.update("form:listSubjectRpt:"+indexSbj+":dataTableComplianceRpt");
		
	}
	
	public void onAddNewCompliance(int indexDtl) {
		
		System.out.println("index=="+indexDtl);
		if(complianceTesting.getComplianceTestingDtls().get(indexDtl).getComplianceTestingPICFollowups() == null) {
			complianceTesting.getComplianceTestingDtls().get(indexDtl).setComplianceTestingPICFollowups(new ArrayList<ComplianceTestingPICFollowup>());
		}
		ComplianceTestingPICFollowup picFollow = new ComplianceTestingPICFollowup();
		User user1 = new User();
		picFollow.setUser1(user1);
		User user2 = new User();
		picFollow.setUser2(user2);
		User user3 = new User();
		picFollow.setUser3(user3);
		complianceTesting.getComplianceTestingDtls().get(indexDtl).getComplianceTestingPICFollowups().add(picFollow);
		
		System.out.println("size=="+complianceTesting.getComplianceTestingDtls().get(indexDtl).getComplianceTestingPICFollowups().size());
		
		if(tableModelComplianceTestingFollowup ==  null) {
			tableModelComplianceTestingFollowup = new PICTindakLanjutTableModel<ComplianceTestingPICFollowup>(
					complianceTesting.getComplianceTestingDtls().get(indexDtl).getComplianceTestingPICFollowups());
		}
		
		tableModelComplianceTestingFollowup.setWrappedData(complianceTesting.getComplianceTestingDtls().get(indexDtl).getComplianceTestingPICFollowups());

		PrimeFaces.current().ajax()
		.update("form:listSubjectRpt:"+indexDtl+":dataTableComplianceRpt");
	}

	public void onDeleteRowCompliance(int indexDtl) {
		for (int i = 0; i < selectedDataComplianceRpt.length; i++) {
			complianceTesting.getComplianceTestingDtls().get(indexDtl).getComplianceTestingPICFollowups().remove(selectedDataComplianceRpt[i]);
		}
		
		tableModelComplianceTestingFollowup.setWrappedData(complianceTesting.getComplianceTestingDtls().get(indexDtl).getComplianceTestingPICFollowups());
		PrimeFaces.current().ajax()
		.update("form:listSubjectRpt:"+indexDtl+":dataTableComplianceRpt");
	}
	
	public void setupCounterType() {
		try {
			counterTypes = counterTypeService.getAllCounterTypeLabelValue();
			System.out.println("counterTypes=="+counterTypes.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void cancel() {
		try {
			facesUtil.redirect("/pages/complianceTestingMockup/complianceTesting.faces");
		} catch (Exception ex) {
			ex.printStackTrace();
			facesUtil.addFacesMsg(FacesMessage.SEVERITY_ERROR, null, "Operation Failed : " + ex.getMessage(), "");
		}
	}
	
	public void selectRating() {
		ratings = new ArrayList<SelectItem>();

		try {
			List<ParameterDetail> listComplianceDtl = parameterDetailService
					.getParameterDetailByParamCode(ParameterHeader.PARAM_HEAD_CODE_COMPLIANCE_TESTING_RATING);

			for (ParameterDetail vo : listComplianceDtl) {
				SelectItem si = new SelectItem();
				si.setLabel(vo.getName());
				si.setValue(vo.getParameterDtlCode());
				ratings.add(si);
			}
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
				SelectItem si = new SelectItem();
				si.setLabel(vo.getName());
				si.setValue(vo.getParameterDtlCode());
				complianceStatusList.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void selectDivision() {
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

		JsUtil.reInitSelect2();
	}
	
	public void selectBranch(Long divisionId) {
		branchs = new ArrayList<SelectItem>();
		try {
			List<Branch> pd = userService.getBranchByDivisionId(divisionId);
			for (int i = 0; i < pd.size(); i++) {
				SelectItem si = new SelectItem();
				si.setLabel(((Branch) pd.get(i)).getBranchName());
				si.setValue(((Branch) pd.get(i)).getBranchCode());
				branchs.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		JsUtil.reInitSelect2();
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
			isNewData = true;
		} else {
			this.handleEdit(editId);
			isNewData = false;
		}
	}

	private void handleNew() {
		try {
			

			actionMode = Constants.ACTION_ADD;
			facesUtil.setSessionAttribute("token", null);
			
			complianceTesting = new ComplianceTesting();
			
			if(complianceTesting.getCounterType() == null) {
				complianceTesting.setCounterType(new CounterType());
			}
			
			if(complianceTesting.getComplianceTestingPICReviews() == null) {
				complianceTesting.setComplianceTestingPICReviews(new ArrayList<ComplianceTestingPICReview>());
			}
			
			User user = (User) facesUtil.getSessionAttribute(Constants.SESSION_EMPLOYEE);
			ComplianceTestingPICReview complianceTestingPICReview = new ComplianceTestingPICReview();
			complianceTestingPICReview.setUser(user);
			complianceTesting.getComplianceTestingPICReviews().add(complianceTestingPICReview);
			tableModelComplianceTestingPICReview = new ComplianceTestingPICReviewTableModel<ComplianceTestingPICReview>(
					complianceTestingPICReviews);
			tableModelComplianceTestingPICReview.setWrappedData(complianceTesting.getComplianceTestingPICReviews());
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	private void handleEdit(String editId) {
		String token = facesUtil.retrieveRequestParam("token");
		if (StringUtils.isNotEmpty(token)) {
			editId = Constants.decryptString(token);
		}
		facesUtil.setSessionAttribute("token", Constants.encryptString(editId));
		actionMode = Constants.ACTION_EDIT;
		Long idLong = Long.parseLong(editId);
		complianceTesting = complianceTestingService.findById(idLong);
		
		if(complianceTesting.getCounterType() == null) {
			complianceTesting.setCounterType(new CounterType());
		}
		
		uploadedFilesAttachment = new ArrayList<UploadedFileWO>();
		for (ComplianceTestingDoc tad : complianceTesting.getComplianceTestingDocs()) {
			UploadedFileWO uf = new UploadedFileWO();
			uf.setFileName(tad.getAttachmentFile());
			uf.setFileId(tad.getFileId());
			uf.setIsNew(false);
			uf.setFileSize(tad.getFileSize());
			uploadedFilesAttachment.add(uf);
		}
		
		tableModelComplianceTestingPICReview = new ComplianceTestingPICReviewTableModel<ComplianceTestingPICReview>(
				complianceTesting.getComplianceTestingPICReviews());
		tableModelComplianceTestingPICReview.setWrappedData(complianceTesting.getComplianceTestingPICReviews());
		
		if(complianceTesting.getComplianceTestingDtls()!=null && complianceTesting.getComplianceTestingDtls().size()>0) {
			for(int i=0;i<complianceTesting.getComplianceTestingDtls().size();i++) {
				ComplianceTestingDtl ctd = complianceTesting.getComplianceTestingDtls().get(i);
				if(ctd!=null && ctd.getComplianceTestingPICFollowups()!=null && ctd.getComplianceTestingPICFollowups().size()>0) {
					for(int x=0;x<ctd.getComplianceTestingPICFollowups().size();x++) {
						ComplianceTestingPICFollowup ctp = ctd.getComplianceTestingPICFollowups().get(x);
						ctp.setBranchName(userService.getBranchByBranchCode(ctp.getBranchCode()).getBranchName());
					}
				}
			}
		}
		
		//JsUtil.hideTHeadFollowupPoints();
		if(complianceTesting.getComplianceTestingDtls() == null && complianceTesting.getComplianceTestingDtls().size()==0) {
			PrimeFaces.current().executeScript("wiz.loadStep (wiz.cfg.steps [0], true);");
		}else if(complianceTesting.getComplianceTestingDtls()!=null && complianceTesting.getComplianceTestingDtls().size()>0) {
			Boolean isReporting = false;
			for(int i=0;i<complianceTesting.getComplianceTestingDtls().size();i++) {
			ComplianceTestingDtl ctd = complianceTesting.getComplianceTestingDtls().get(i);
				if(ctd!=null && ctd.getComplianceTestingPICFollowups()!=null && ctd.getComplianceTestingPICFollowups().size()>0) {
					isReporting = true;
				}
			}
			if(isReporting) {
				PrimeFaces.current().executeScript("wiz.loadStep (wiz.cfg.steps [2], true);");
			}else {
				PrimeFaces.current().executeScript("wiz.loadStep (wiz.cfg.steps [1], true);");
			}
		}
		
	}
	
	public void deleteAttachment(String fileId,int index) throws Exception {
		deletedAttachment = deletedAttachment!=null?deletedAttachment: new ArrayList<UploadedFileWO>();
		deletedAttachment.add(new UploadedFileWO(fileId,null,null, null));
		uploadedFilesAttachment.remove(uploadedFilesAttachment.get(index));
	}

	public void downloadFile(String fileId, String fileName, byte[] content) throws Exception {
		CallApiManager.downloadFile(fileId, fileName, content, parameterDetailService);
	}
	
	public void handleFileUpload(FileUploadEvent event) throws Exception {
		try {
			if(uploadedFilesAttachment == null)
				uploadedFilesAttachment = new ArrayList<UploadedFileWO>();
				
			uploadedFilesAttachment.add(new UploadedFileWO(
				CallApiManager.callUploadAPI(event.getFile(), Constants.COMPLIANCE_DOC_TYPE_AUDIT_FOLLOWUP,
						parameterDetailService, false, getFileUtil()),
				event.getFile().getFileName(), event.getFile().getContentType(), event.getFile().getSize(), true));
		} catch (Exception e) {
			e.printStackTrace();
			facesUtil.addErrMessage(e.getMessage());
		}
	}
	
	
	public void onAddSubject() {
		
		System.out.println("add Subject");
		
		if(complianceTesting.getComplianceTestingDtls() == null) {
			complianceTesting.setComplianceTestingDtls(new ArrayList<ComplianceTestingDtl>());
		}
		ComplianceTestingDtl dtl = new ComplianceTestingDtl();
		complianceTesting.getComplianceTestingDtls().add(dtl);
		
		
	}
	
	public void onDeleteSubject() {
		complianceTesting.getComplianceTestingDtls().removeIf( f -> f.isChecked());
		
	}
	
	public String onFlowProcess(FlowEvent event) {
		System.out.println("old step =="+event.getOldStep());
        System.out.println("new step =="+event.getNewStep());
        
        if(!StringUtils.isEmpty(event.getOldStep()) && event.getOldStep().equals("preliminary") && !StringUtils.isEmpty(event.getNewStep()) && event.getNewStep().equals("filedWork")) {
        	save();
        }
        
        if(!StringUtils.isEmpty(event.getOldStep()) && event.getOldStep().equals("filedWork") && !StringUtils.isEmpty(event.getNewStep()) && event.getNewStep().equals("reporting")) {
        	save();
        	if(complianceTesting.getCounterType() == null) {
    			complianceTesting.setCounterType(new CounterType());
    		}
        }
        
        return event.getNewStep();
        
    }
	
	
	public void saveAndRedirect() {
		save();
		try {
			facesUtil.redirect("/pages/complianceTestingMockup/complianceTesting.faces");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void saveSendMailAndRedirect() {
		save();
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
        
		try {
			facesUtil.redirect("/pages/complianceTestingMockup/complianceTesting.faces");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void save() {
		try {
			
			if(StringUtils.isEmpty(complianceTesting.getReminderStatus())) {
				complianceTesting.setReminderStatus("REMINDER_ACTIVE");
			}
			
			if(StringUtils.isEmpty(complianceTesting.getStatus())) {
				complianceTesting.setStatus("DATA_ACTIVE");
			}
			
			if(complianceTesting.getCounterType()==null || complianceTesting.getCounterType().getCounterTypeId() == null) {
				complianceTesting.setCounterType(null);
			}else {
				complianceTesting.setCounterType(counterTypeService.findById(complianceTesting.getCounterType().getCounterTypeId()));
			}
			
			if(complianceTesting.getComplianceTestingDtls()!=null && complianceTesting.getComplianceTestingDtls().size()>0) {
				
				for(int i=0;i<complianceTesting.getComplianceTestingDtls().size();i++) {
					ComplianceTestingDtl dtl = complianceTesting.getComplianceTestingDtls().get(i);
					dtl.setComplianceTesting(complianceTesting);
					dtl.setDelId(new Long(0));
					dtl.setEnabledFlag(Constants.CONSTANT_YES);
					if(StringUtils.isEmpty(dtl.getCreatedBy())) {
						dtl.setCreatedBy(facesUtil.retrieveUserLogin());
						dtl.setCreationDate(new Timestamp(new Date().getTime()));
					}else {
						dtl.setLastUpdateBy(facesUtil.retrieveUserLogin());
						dtl.setLastUpdateDate(new Timestamp(new Date().getTime()));
					}
					
					if(dtl.getComplianceTestingPICFollowups()!=null && dtl.getComplianceTestingPICFollowups().size()>0) {
						for(int x=0;x<dtl.getComplianceTestingPICFollowups().size();x++) {
							ComplianceTestingPICFollowup dtlFollow = dtl.getComplianceTestingPICFollowups().get(x);
							dtlFollow.setComplianceTestingDtl(dtl);
							dtlFollow.setDelId(new Long(0));
							dtlFollow.setEnabledFlag(Constants.CONSTANT_YES);
							
							if(StringUtils.isEmpty(dtlFollow.getCreatedBy())) {
								dtlFollow.setCreatedBy(facesUtil.retrieveUserLogin());
								dtlFollow.setCreationDate(new Timestamp(new Date().getTime()));
							}else {
								dtlFollow.setLastUpdateBy(facesUtil.retrieveUserLogin());
								dtlFollow.setLastUpdateDate(new Timestamp(new Date().getTime()));
							}
						}
					}
					
				}
				
				
			}
			
			if(complianceTesting.getComplianceTestingPICReviews()!=null && complianceTesting.getComplianceTestingPICReviews().size()>0) {
				for(int i=0;i<complianceTesting.getComplianceTestingPICReviews().size();i++) {
					ComplianceTestingPICReview dtl = complianceTesting.getComplianceTestingPICReviews().get(i);
					dtl.setComplianceTesting(complianceTesting);
					dtl.setDelId(new Long(0));
					dtl.setEnabledFlag(Constants.CONSTANT_YES);
					if(StringUtils.isEmpty(dtl.getCreatedBy())) {
						dtl.setCreatedBy(facesUtil.retrieveUserLogin());
						dtl.setCreationDate(new Timestamp(new Date().getTime()));
					}else {
						dtl.setLastUpdateBy(facesUtil.retrieveUserLogin());
						dtl.setLastUpdateDate(new Timestamp(new Date().getTime()));
					}
				}
			}
			
			if (complianceTesting.getComplianceTestingDocs() == null
					|| complianceTesting.getComplianceTestingDocs().size() == 0) {
				complianceTesting.setComplianceTestingDocs(new ArrayList<ComplianceTestingDoc>());
			}
			
			complianceTesting.getComplianceTestingDocs().clear();

			if (uploadedFilesAttachment != null) {
				for (int i = 0; i < uploadedFilesAttachment.size(); i++) {
					ComplianceTestingDoc doc = new ComplianceTestingDoc();
					UploadedFileWO uf = (UploadedFileWO) uploadedFilesAttachment.get(i);
					doc.setComplianceTesting(complianceTesting);

					doc.setAttachmentFile(uf.getFileName());
					doc.setCreatedBy(facesUtil.retrieveUserLogin());
					doc.setCreationDate(new Timestamp(new Date().getTime()));
					doc.setDelId(new Long(0));
					doc.setEnabledFlag(Constants.CONSTANT_YES);

					doc.setFileId(uf.getFileId());
					doc.setFileSize(uf.getFileSize());
					complianceTesting.getComplianceTestingDocs().add(doc);
				}
			}
			
			if (complianceTesting.getComplianceTestingId() != null) {
				complianceTesting.setLastUpdateBy(facesUtil.retrieveUserLogin());
				complianceTesting.setLastUpdateDate(new Timestamp(new Date().getTime()));
				complianceTestingService.update(complianceTesting);
			} else {
				complianceTesting.setCreatedBy(facesUtil.retrieveUserLogin());
				complianceTesting.setCreationDate(new Timestamp(new Date().getTime()));
				complianceTesting.setDelId(new Long(0));
				complianceTesting.setEnabledFlag(Y);
				complianceTestingService.save(complianceTesting);
			}
			
			

		} catch (Exception ex) {
			ex.printStackTrace();
			facesUtil.addFacesMsg(FacesMessage.SEVERITY_ERROR, null, "Operation Failed : " + ex.getMessage(), "");
		}

	}
	
	public void sendEmail() {
		try {

			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			
			EmailTemplate emailTemplate = emailTemplateService
					.getEmailTemplateByEmailTemplateCode("EMAIL_COMPLIANCE_ASSESSMENT");
			String emailSubject = emailTemplate.getEmailSubject();

			String emailContent = "";
			String emailTo = "";
			String emailCc = "";
			
			String inspectionNo = complianceTesting.getInspectionNo() != null ? " - " + complianceTesting.getInspectionNo() : "";

			emailContent = emailTemplate.getEmailContent().replace(Constants.CONFIRMATION_TYPE_AND_DOC_NUM, "Compliance Testing" 
					+ inspectionNo);
			
			if(complianceTesting.getComplianceTestingDtls()!=null && complianceTesting.getComplianceTestingDtls().size()>0) {
				
				for(int i=0;i<complianceTesting.getComplianceTestingDtls().size();i++) {
					ComplianceTestingDtl dtl = complianceTesting.getComplianceTestingDtls().get(i);
					
					
					if(dtl.getComplianceTestingPICFollowups()!=null && dtl.getComplianceTestingPICFollowups().size()>0) {
						for(int x=0;x<dtl.getComplianceTestingPICFollowups().size();x++) {
							ComplianceTestingPICFollowup dtlFollow = dtl.getComplianceTestingPICFollowups().get(x);
							if(StringUtils.isEmpty(dtlFollow.getLastUpdateBy()) || (dtlFollow.getLastUpdateDate()!=null && sdf.format(dtlFollow.getLastUpdateDate()).equals(sdf.format(new Date())))) {
							emailTo = dtlFollow.getUser1().getEmail();

							final String subject = emailSubject;
							final String content = emailContent;
							final String to = emailTo;
							
							final String cc = emailCc;
							
							//final String to = "h3ndr407@gmail.com";

							CallApiManager.sendEmailAPI(to, cc, subject, content, "EMAIL_COMPLIANCE_ASSESSMENT", "true",
									parameterDetailService);
							}
						}
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
		System.out.println("clientId=="+clientId);
		System.out.println("widgetVar=="+widgetVar);
		if (StringUtils.equals("picComplianceDialog", widgetVar)) {
			Object[] objects = (Object[]) selectedItem;
			System.out.println("object selected=="+objects[0]);
			User user = userService.findById(MathUtil.returnIdObjectToLong(objects[0]));
			complianceTesting.getComplianceTestingPICReviews().get(indexDtlComplianceTestingPICReview).setUser(user);
			tableModelComplianceTestingPICReview.setWrappedData(complianceTesting.getComplianceTestingPICReviews());
		}
		else if (StringUtils.equals("picComplianceDialogFw", widgetVar)) {
			Object[] objects = (Object[]) selectedItem;
			System.out.println("object selected=="+objects[0]);
			User user = userService.findById(MathUtil.returnIdObjectToLong(objects[0]));
			complianceTesting.getComplianceTestingDtls().get(indexDtlSubject).getComplianceTestingPICFollowups().get(indexDtlCompliancePiCFollowup).setUser1(user);
			
			User user2 = userService.getUserByNik(user.getPukNik());
			if (user2 != null) {
				complianceTesting.getComplianceTestingDtls().get(indexDtlSubject).getComplianceTestingPICFollowups().get(indexDtlCompliancePiCFollowup).setUser2(user2);

				User user3 = userService.getUserByNik(user2.getPukNik());
				if (user3 != null) {
					complianceTesting.getComplianceTestingDtls().get(indexDtlSubject).getComplianceTestingPICFollowups().get(indexDtlCompliancePiCFollowup).setUser3(user3);
				}
			}
			
			tableModelComplianceTestingFollowup.setWrappedData(complianceTesting.getComplianceTestingDtls().get(indexDtlSubject).getComplianceTestingPICFollowups());
			
			PrimeFaces.current().ajax()
			.update("form:listSubjectRpt:"+indexDtlSubject+":dataTableComplianceRpt");
		
		}
		else if (StringUtils.equals("branchDialog", widgetVar)) {
			Object[] objects = (Object[]) selectedItem;
			System.out.println("object selected=="+objects[2]);
			
			complianceTesting.getComplianceTestingDtls().get(indexDtlSubject).getComplianceTestingPICFollowups().get(indexDtlCompliancePiCFollowup).setBranchCode((String)objects[1]);
			complianceTesting.getComplianceTestingDtls().get(indexDtlSubject).getComplianceTestingPICFollowups().get(indexDtlCompliancePiCFollowup).setBranchName((String)objects[2]);
			tableModelComplianceTestingFollowup.setWrappedData(complianceTesting.getComplianceTestingDtls().get(indexDtlSubject).getComplianceTestingPICFollowups());
			
			PrimeFaces.current().ajax()
			.update("form:listSubjectRpt:"+indexDtlSubject+":dataTableComplianceRpt");
		
		}

	}
	

	public static Logger getLogger() {
		return logger;
	}



	public static void setLogger(Logger logger) {
		ComplianceTestingMockupEditBean.logger = logger;
	}



	public List<SelectItem> getDivisions() {
		return divisions;
	}



	public void setDivisions(List<SelectItem> divisions) {
		this.divisions = divisions;
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



	public SimpleDateFormat getSdf() {
		return sdf;
	}



	public void setSdf(SimpleDateFormat sdf) {
		this.sdf = sdf;
	}



	public List<UploadedFileWO> getUploadedFilesDocument() {
		return uploadedFilesDocument;
	}



	public void setUploadedFilesDocument(List<UploadedFileWO> uploadedFilesDocument) {
		this.uploadedFilesDocument = uploadedFilesDocument;
	}



	public List<UploadedFileWO> getDeletedFiles() {
		return deletedFiles;
	}



	public void setDeletedFiles(List<UploadedFileWO> deletedFiles) {
		this.deletedFiles = deletedFiles;
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



	public List<SelectItem> getSearchType() {
		return searchType;
	}



	public void setSearchType(List<SelectItem> searchType) {
		this.searchType = searchType;
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



	public UserService getUserService() {
		return userService;
	}



	public void setUserService(UserService userService) {
		this.userService = userService;
	}



	public static long getSerialversionuid() {
		return serialVersionUID;
	}



	public Long getDivisionId() {
		return divisionId;
	}



	public void setDivisionId(Long divisionId) {
		this.divisionId = divisionId;
	}



	public SelectorInfo getSelectorDivision() {
		return selectorDivision;
	}



	public void setSelectorDivision(SelectorInfo selectorDivision) {
		this.selectorDivision = selectorDivision;
	}



	public Boolean getIsNewData() {
		return isNewData;
	}



	public void setIsNewData(Boolean isNewData) {
		this.isNewData = isNewData;
	}



	public String getNoPemeriksaan() {
		return noPemeriksaan;
	}



	public void setNoPemeriksaan(String noPemeriksaan) {
		this.noPemeriksaan = noPemeriksaan;
	}



	public Date getStartDate() {
		return startDate;
	}



	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}



	public Date getEndDate() {
		return endDate;
	}



	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}



	public String getTextWarningUpload() {
		return textWarningUpload;
	}



	public void setTextWarningUpload(String textWarningUpload) {
		this.textWarningUpload = textWarningUpload;
	}



	public List<UploadedFileWO> getUploadedFilesAttachment() {
		return uploadedFilesAttachment;
	}



	public void setUploadedFilesAttachment(List<UploadedFileWO> uploadedFilesAttachment) {
		this.uploadedFilesAttachment = uploadedFilesAttachment;
	}



	public FileUtil getFileUtil() {
		return fileUtil;
	}



	public void setFileUtil(FileUtil fileUtil) {
		this.fileUtil = fileUtil;
	}



	public String getKeterangan() {
		return keterangan;
	}



	public void setKeterangan(String keterangan) {
		this.keterangan = keterangan;
	}



	public List<UploadedFileWO> getUploadedFiles() {
		return uploadedFiles;
	}



	public void setUploadedFiles(List<UploadedFileWO> uploadedFiles) {
		this.uploadedFiles = uploadedFiles;
	}



	public List<UploadedFileWO> getDeletedAttachment() {
		return deletedAttachment;
	}



	public void setDeletedAttachment(List<UploadedFileWO> deletedAttachment) {
		this.deletedAttachment = deletedAttachment;
	}



	public SelectorInfo getSelectorCompliance() {
		return selectorCompliance;
	}



	public void setSelectorCompliance(SelectorInfo selectorCompliance) {
		this.selectorCompliance = selectorCompliance;
	}


	public ComplianceTestingPICReview[] getSelectedDataComplianceTestingPICReview() {
		return selectedDataComplianceTestingPICReview;
	}

	public void setSelectedDataComplianceTestingPICReview(
			ComplianceTestingPICReview[] selectedDataComplianceTestingPICReview) {
		this.selectedDataComplianceTestingPICReview = selectedDataComplianceTestingPICReview;
	}

	public ComplianceTestingPICReviewTableModel<ComplianceTestingPICReview> getTableModelComplianceTestingPICReview() {
		return tableModelComplianceTestingPICReview;
	}

	public void setTableModelComplianceTestingPICReview(
			ComplianceTestingPICReviewTableModel<ComplianceTestingPICReview> tableModelComplianceTestingPICReview) {
		this.tableModelComplianceTestingPICReview = tableModelComplianceTestingPICReview;
	}

	public List<ComplianceTestingPICReview> getComplianceTestingPICReviews() {
		return complianceTestingPICReviews;
	}

	public void setComplianceTestingPICReviews(List<ComplianceTestingPICReview> complianceTestingPICReviews) {
		this.complianceTestingPICReviews = complianceTestingPICReviews;
	}

	public Integer getLastSequenceOfCompliance() {
		return lastSequenceOfCompliance;
	}



	public void setLastSequenceOfCompliance(Integer lastSequenceOfCompliance) {
		this.lastSequenceOfCompliance = lastSequenceOfCompliance;
	}
	
	public Integer getIndexDtlComplianceTestingPICReview() {
		return indexDtlComplianceTestingPICReview;
	}

	public void setIndexDtlComplianceTestingPICReview(Integer indexDtlComplianceTestingPICReview) {
		this.indexDtlComplianceTestingPICReview = indexDtlComplianceTestingPICReview;
	}

	public Integer getIndexDtlComplianceFw() {
		return indexDtlComplianceFw;
	}

	public void setIndexDtlComplianceFw(Integer indexDtlComplianceFw) {
		this.indexDtlComplianceFw = indexDtlComplianceFw;
	}

	public List<SelectItem> getComplianceStatusList() {
		return complianceStatusList;
	}

	public void setComplianceStatusList(List<SelectItem> complianceStatusList) {
		this.complianceStatusList = complianceStatusList;
	}
	
	

	public List<SubjectVO> getSubjects() {
		return subjects;
	}

	public void setSubjects(List<SubjectVO> subjects) {
		this.subjects = subjects;
	}
	
	

	public SelectorInfo getSelectorComplianceFw() {
		return selectorComplianceFw;
	}

	public void setSelectorComplianceFw(SelectorInfo selectorComplianceFw) {
		this.selectorComplianceFw = selectorComplianceFw;
	}


	public ComplianceTestingPICFollowup[] getSelectedDataComplianceRpt() {
		return selectedDataComplianceRpt;
	}

	public void setSelectedDataComplianceRpt(ComplianceTestingPICFollowup[] selectedDataComplianceRpt) {
		this.selectedDataComplianceRpt = selectedDataComplianceRpt;
	}

	public PICTindakLanjutTableModel<ComplianceTestingPICFollowup> getTableModelComplianceTestingFollowup() {
		return tableModelComplianceTestingFollowup;
	}

	public void setTableModelComplianceTestingFollowup(
			PICTindakLanjutTableModel<ComplianceTestingPICFollowup> tableModelComplianceTestingFollowup) {
		this.tableModelComplianceTestingFollowup = tableModelComplianceTestingFollowup;
	}

	

	public Integer getLastSequenceOfComplianceRpt() {
		return lastSequenceOfComplianceRpt;
	}

	public void setLastSequenceOfComplianceRpt(Integer lastSequenceOfComplianceRpt) {
		this.lastSequenceOfComplianceRpt = lastSequenceOfComplianceRpt;
	}

	
	public SelectorInfo getSelectorComplianceRpt() {
		return selectorComplianceRpt;
	}

	public void setSelectorComplianceRpt(SelectorInfo selectorComplianceRpt) {
		this.selectorComplianceRpt = selectorComplianceRpt;
	}
	
	

	public Boolean getTindakLanjut() {
		return tindakLanjut;
	}

	public void setTindakLanjut(Boolean tindakLanjut) {
		this.tindakLanjut = tindakLanjut;
	}
	
	

	public List<StatusKonfirmasiVO> getTableStatus() {
		return tableStatus;
	}

	public void setTableStatus(List<StatusKonfirmasiVO> tableStatus) {
		this.tableStatus = tableStatus;
	}

	public String getJdlPemeriksaan() {
		return jdlPemeriksaan;
	}

	public void setJdlPemeriksaan(String jdlPemeriksaan) {
		this.jdlPemeriksaan = jdlPemeriksaan;
	}

	
	
	public String getRatingKeseluruhan() {
		return ratingKeseluruhan;
	}

	public void setRatingKeseluruhan(String ratingKeseluruhan) {
		this.ratingKeseluruhan = ratingKeseluruhan;
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
	
	

	public CounterTypeService getCounterTypeService() {
		return counterTypeService;
	}

	public void setCounterTypeService(CounterTypeService counterTypeService) {
		this.counterTypeService = counterTypeService;
	}
	
	

	public Integer getIndexDtlSubject() {
		return indexDtlSubject;
	}

	public void setIndexDtlSubject(Integer indexDtlSubject) {
		this.indexDtlSubject = indexDtlSubject;
	}
	
	

	public ComplianceTesting getComplianceTesting() {
		return complianceTesting;
	}

	public void setComplianceTesting(ComplianceTesting complianceTesting) {
		this.complianceTesting = complianceTesting;
	}
	
	

	public Integer getIndexDtlCompliancePiCFollowup() {
		return indexDtlCompliancePiCFollowup;
	}

	public void setIndexDtlCompliancePiCFollowup(Integer indexDtlCompliancePiCFollowup) {
		this.indexDtlCompliancePiCFollowup = indexDtlCompliancePiCFollowup;
	}

	public List<ComplianceTestingPICFollowup> getComplianceTestingPICFollowup() {
		return complianceTestingPICFollowup;
	}

	public void setComplianceTestingPICFollowup(List<ComplianceTestingPICFollowup> complianceTestingPICFollowup) {
		this.complianceTestingPICFollowup = complianceTestingPICFollowup;
	}

	public List<SelectItem> getBranchs() {
		return branchs;
	}

	public void setBranchs(List<SelectItem> branchs) {
		this.branchs = branchs;
	}
	

	public SelectorInfo getSelectorBranch() {
		return selectorBranch;
	}

	public void setSelectorBranch(SelectorInfo selectorBranch) {
		this.selectorBranch = selectorBranch;
	}

	public ComplianceTestingService getComplianceTestingService() {
		return complianceTestingService;
	}

	public void setComplianceTestingService(ComplianceTestingService complianceTestingService) {
		this.complianceTestingService = complianceTestingService;
	}

	public EmailTemplateService getEmailTemplateService() {
		return emailTemplateService;
	}

	public void setEmailTemplateService(EmailTemplateService emailTemplateService) {
		this.emailTemplateService = emailTemplateService;
	}

	public Boolean getIsPreliminary() {
		return isPreliminary;
	}

	public void setIsPreliminary(Boolean isPreliminary) {
		this.isPreliminary = isPreliminary;
	}

	public Boolean getIsFieldWork() {
		return isFieldWork;
	}

	public void setIsFieldWork(Boolean isFieldWork) {
		this.isFieldWork = isFieldWork;
	}

	public Boolean getIsReporting() {
		return isReporting;
	}

	public void setIsReporting(Boolean isReporting) {
		this.isReporting = isReporting;
	}

	public List<SelectItem> getRatings() {
		return ratings;
	}

	public void setRatings(List<SelectItem> ratings) {
		this.ratings = ratings;
	}

	
	
}