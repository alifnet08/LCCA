package com.wo.module.tmpComplianceReview.bean;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import com.wo.module.common.util.EntityUtil;
import com.wo.module.common.util.FileUtil;
import com.wo.module.common.util.MathUtil;
import com.wo.module.common.utility.CallApiManager;
import com.wo.module.complianceTestingMockup.bean.ComplianceTestingMockupEditBean;
import com.wo.module.complianceTestingMockup.constant.ComplianceTestingMockupConstants;
import com.wo.module.complianceTestingMockup.model.ComplianceTesting;
import com.wo.module.complianceTestingMockup.model.ComplianceTestingDoc;
import com.wo.module.complianceTestingMockup.model.ComplianceTestingDtl;
import com.wo.module.complianceTestingMockup.model.ComplianceTestingPICFollowup;
import com.wo.module.complianceTestingMockup.model.ComplianceTestingPICFollowupEmail;
import com.wo.module.complianceTestingMockup.model.ComplianceTestingPICReview;
import com.wo.module.complianceTestingMockup.model.ComplianceTestingPICReviewTableModel;
import com.wo.module.complianceTestingMockup.model.PICTindakLanjutTableModel;
import com.wo.module.complianceTestingMockup.service.ComplianceTestingService;
import com.wo.module.complianceTestingMockup.vo.StatusKonfirmasiVO;
import com.wo.module.complianceTestingMockup.vo.SubjectVO;
import com.wo.module.counterType.model.CounterType;
import com.wo.module.counterType.model.CounterTypeDtl;
import com.wo.module.counterType.service.CounterTypeService;
import com.wo.module.emailTemplate.model.EmailTemplate;
import com.wo.module.emailTemplate.service.EmailTemplateService;
import com.wo.module.holiday.service.HolidayService;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.lov.bean.SelectorListener;
import com.wo.module.lov.bean.SelectorModel.SelectorInfo;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.parameter.model.ParameterHeader;
import com.wo.module.tmpAudit.model.TmpAuditPicFollowupEmail;
import com.wo.module.tmpAudit.model.TmpAuditPicFollowupRec;
import com.wo.module.tmpAudit.model.TmpAuditPicFollowupRecEmail;
import com.wo.module.tmpComplianceReview.constant.TmpComplianceReviewConstants;
import com.wo.module.tmpComplianceReviewApproval.constant.TmpComplianceReviewApprovalConstants;
import com.wo.module.tmpCorrespondence.model.TmpCorrespondencePicCompliance;
import com.wo.module.user.model.Branch;
import com.wo.module.user.model.Division;
import com.wo.module.user.model.User;
import com.wo.module.user.service.UserService;

public class TmpComplianceReviewEditBean extends CommonBean implements SelectorListener<Object>, Serializable {

	private static final long serialVersionUID = -7277046569343787246L;

	static Logger logger = Logger.getLogger(TmpComplianceReviewEditBean.class);

	private List<SelectItem> divisions;
	
	private List<SelectItem> branchs;
	
	private List<SubjectVO> subjects;
	
	private List<StatusKonfirmasiVO> tableStatus;
	
	private Long divisionId;
	
	private String noPemeriksaan;
	
	private String jdlPemeriksaan;
	
	private String ratingKeseluruhan;
	
	private List<SelectItem> counterTypes;

	public FacesUtil facesUtil;
	
	private FileUtil fileUtil;
	
	private SelectorInfo selectorDivision;
	
	private List<SelectItem> ratings;

	private String navigateSearch = TmpComplianceReviewConstants.NAVIGATE_SEARCH;

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
	private Integer lastSequenceOfPicPenguji;
	
	private UserService userService;
	private CounterTypeService counterTypeService;
	private ComplianceTestingService complianceTestingService;
	private EmailTemplateService emailTemplateService;
	
	private Boolean isPreliminary;
	private Boolean isFieldWork;
	private Boolean isReporting;
	
	private HolidayService holidayService;
	
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
		
		fileUtil = FileUtil.getInstance();
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
	
	public void onSelectDivision(int indexSbj,int indexFollowup) {
		Long divId = complianceTesting.getComplianceTestingDtls().get(indexSbj).getComplianceTestingPICFollowups().get(indexFollowup).getDivisionId();
		
		complianceTesting.getComplianceTestingDtls().get(indexSbj).getComplianceTestingPICFollowups().get(indexFollowup).setBranchCode(null);
		complianceTesting.getComplianceTestingDtls().get(indexSbj).getComplianceTestingPICFollowups().get(indexFollowup).setBranchName(null);
		complianceTesting.getComplianceTestingDtls().get(indexSbj).getComplianceTestingPICFollowups().get(indexFollowup).setSubBranchName(null);
		complianceTesting.getComplianceTestingDtls().get(indexSbj).getComplianceTestingPICFollowups().get(indexFollowup).setUser1(new User());
		complianceTesting.getComplianceTestingDtls().get(indexSbj).getComplianceTestingPICFollowups().get(indexFollowup).setUser2(new User());
		complianceTesting.getComplianceTestingDtls().get(indexSbj).getComplianceTestingPICFollowups().get(indexFollowup).setUser3(new User());
		
		PrimeFaces.current().ajax()
		.update("form:listSubjectRpt:"+indexSbj+":dataTableComplianceRpt");
		
	}
	
	public void onAddNewCompliance() {
		
		System.out.println("index=="+indexDtlSubject);
		if(complianceTesting.getComplianceTestingDtls().get(indexDtlSubject).getComplianceTestingPICFollowups() == null) {
			complianceTesting.getComplianceTestingDtls().get(indexDtlSubject).setComplianceTestingPICFollowups(new ArrayList<ComplianceTestingPICFollowup>());
		}
		ComplianceTestingPICFollowup dtlFollow = new ComplianceTestingPICFollowup();
		User user1 = new User();
		dtlFollow.setUser1(user1);
		User user2 = new User();
		dtlFollow.setUser2(user2);
		User user3 = new User();
		dtlFollow.setUser3(user3);
		complianceTesting.getComplianceTestingDtls().get(indexDtlSubject).getComplianceTestingPICFollowups().add(dtlFollow);
		
		System.out.println("size=="+complianceTesting.getComplianceTestingDtls().get(indexDtlSubject).getComplianceTestingPICFollowups().size());
		
		if(tableModelComplianceTestingFollowup ==  null) {
			tableModelComplianceTestingFollowup = new PICTindakLanjutTableModel<ComplianceTestingPICFollowup>(
					complianceTesting.getComplianceTestingDtls().get(indexDtlSubject).getComplianceTestingPICFollowups());
		}
		
		tableModelComplianceTestingFollowup.setWrappedData(complianceTesting.getComplianceTestingDtls().get(indexDtlSubject).getComplianceTestingPICFollowups());

		PrimeFaces.current().ajax()
		.update("form:listSubjectRpt:"+indexDtlSubject+":dataTableComplianceRpt");
	}

	public void onDeleteRowCompliance() {
		
		System.out.println("index=="+indexDtlSubject);
		complianceTesting.getComplianceTestingDtls().get(indexDtlSubject).getComplianceTestingPICFollowups().removeIf( f -> f.isChecked());
		
		tableModelComplianceTestingFollowup.setWrappedData(complianceTesting.getComplianceTestingDtls().get(indexDtlSubject).getComplianceTestingPICFollowups());
		PrimeFaces.current().ajax()
		.update("form:listSubjectRpt:"+indexDtlSubject+":dataTableComplianceRpt");
	}
	
	public void onAddNewPicPenguji() {
		if (complianceTesting.getComplianceTestingPICReviews() == null) {
			complianceTesting.setComplianceTestingPICReviews(new ArrayList<ComplianceTestingPICReview>());
			lastSequenceOfPicPenguji = 0;
		} else {
			if (complianceTesting.getComplianceTestingPICReviews().size() == 0) {
				lastSequenceOfPicPenguji = 0;
			}
		}

		ComplianceTestingPICReview d = new ComplianceTestingPICReview();
		lastSequenceOfPicPenguji = lastSequenceOfPicPenguji + 1;
		d.setSequence(lastSequenceOfPicPenguji);
		complianceTesting.getComplianceTestingPICReviews().add(d);
		tableModelComplianceTestingPICReview.setWrappedData(complianceTesting.getComplianceTestingPICReviews());
	}
	
	public void onDeleteRowPicPenguji() {
		for (int i = 0; i < selectedDataComplianceTestingPICReview.length; i++) {
			complianceTesting.getComplianceTestingPICReviews().remove(selectedDataComplianceTestingPICReview[i]);
		}

		if (complianceTesting.getComplianceTestingPICReviews() == null
				|| complianceTesting.getComplianceTestingPICReviews().size() == 0) {
			lastSequenceOfPicPenguji = 0;
		}

		tableModelComplianceTestingPICReview.setWrappedData(complianceTesting.getComplianceTestingPICReviews());
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
			facesUtil.redirect("/pages/tmpComplianceReview/tmpComplianceReview.faces");
		} catch (Exception ex) {
			ex.printStackTrace();
			facesUtil.addFacesMsg(FacesMessage.SEVERITY_ERROR, null, "Operation Failed : " + ex.getMessage(), "");
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
			
			User userAtasan = userService.getUserByNik(user.getPukNik());
			if (userAtasan != null) {
				complianceTestingPICReview.setNameAtasanTemp(userAtasan.getName());
			}
			
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
		
		if(complianceTesting.getComplianceTestingPICReviews()!=null && complianceTesting.getComplianceTestingPICReviews().size()>0) {
			for(int x=0;x<complianceTesting.getComplianceTestingPICReviews().size();x++) {
				ComplianceTestingPICReview ctpr = complianceTesting.getComplianceTestingPICReviews().get(x);
				User userAtasan = userService.getUserByNik(ctpr.getUser().getPukNik());
				if (userAtasan != null) {
					ctpr.setNameAtasanTemp(userAtasan.getName());
				}
			}
		}
		
		tableModelComplianceTestingPICReview.setWrappedData(complianceTesting.getComplianceTestingPICReviews());
		
		if(complianceTesting.getComplianceTestingDtls()!=null && complianceTesting.getComplianceTestingDtls().size()>0) {
			for(int i=0;i<complianceTesting.getComplianceTestingDtls().size();i++) {
				ComplianceTestingDtl ctd = complianceTesting.getComplianceTestingDtls().get(i);
				if(ctd!=null && ctd.getComplianceTestingPICFollowups()!=null && ctd.getComplianceTestingPICFollowups().size()>0) {
					for(int x=0;x<ctd.getComplianceTestingPICFollowups().size();x++) {
						ComplianceTestingPICFollowup ctp = ctd.getComplianceTestingPICFollowups().get(x);
						Branch branch = userService.getBranchByBranchCode(ctp.getBranchCode());
						ctp.setBranchName(branch.getBranchName());
						ctp.setSubBranchName(branch.getSubBranchName());
					}
				}
				if(tableModelComplianceTestingFollowup ==  null) {
					tableModelComplianceTestingFollowup = new PICTindakLanjutTableModel<ComplianceTestingPICFollowup>(
							complianceTesting.getComplianceTestingDtls().get(i).getComplianceTestingPICFollowups());
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
        	if(complianceTesting.getInspectionTitle()!=null && complianceTesting.getInspectionTitle().length()>150) {
        		facesUtil.addErrMessage("Judul Pemeriksaan harus lebih kecil dari 150 karakter");
        		PrimeFaces.current().ajax()
    			.update("form:messages");
        		return event.getOldStep();
        	}
        	else if(complianceTesting.getInspectionNo()!=null && complianceTesting.getInspectionNo().length()>150) {
        		facesUtil.addErrMessage("Nomor Pemeriksaan harus lebih kecil dari 150 karakter");
        		PrimeFaces.current().ajax()
    			.update("form:messages");
        		return event.getOldStep();
        	}
        	
        	save();
        	if(complianceTesting.getComplianceTestingDtls() == null || complianceTesting.getComplianceTestingDtls().size() == 0) {
        		List<ComplianceTestingDtl> list = new ArrayList<ComplianceTestingDtl>();
        		list.add(new ComplianceTestingDtl());
    			complianceTesting.setComplianceTestingDtls(list);
    		}
        }
        
        if(!StringUtils.isEmpty(event.getOldStep()) && event.getOldStep().equals("filedWork") && !StringUtils.isEmpty(event.getNewStep()) && event.getNewStep().equals("reporting")) {
        	save();
        	if(complianceTesting.getCounterType() == null) {
    			complianceTesting.setCounterType(new CounterType());
    		}
        }
        
        return event.getNewStep();
        
    }
	
	public Boolean validate() {
		Boolean flag = false;
		
		
		if (complianceTesting.getComplianceTestingDtls()!=null) {
			for(int i=0;i<complianceTesting.getComplianceTestingDtls().size();i++) {
				ComplianceTestingDtl complianceTestingDtl = complianceTesting.getComplianceTestingDtls().get(i);
				if(complianceTestingDtl.getIsFollowup()!=null && complianceTestingDtl.getIsFollowup().equals("Y") && (complianceTestingDtl.getComplianceTestingPICFollowups()==null || complianceTestingDtl.getComplianceTestingPICFollowups().size()==0) ) {
					facesUtil.addErrMessage("PIC Tindak Lanjut harus diisi minimal 1 row");
					flag = true;
				}
				
				if(complianceTestingDtl.getIsFollowup()!=null && complianceTestingDtl.getIsFollowup().equals("Y") && complianceTestingDtl.getComplianceTestingPICFollowups()!=null && complianceTestingDtl.getComplianceTestingPICFollowups().size()>0) {
				for(int x=0;x<complianceTestingDtl.getComplianceTestingPICFollowups().size();x++) {
					ComplianceTestingPICFollowup complianceTestingPICFollowup = complianceTestingDtl.getComplianceTestingPICFollowups().get(x);
					if(complianceTestingPICFollowup.getUser1() == null || StringUtils.isEmpty(complianceTestingPICFollowup.getUser1().getNik())) {
						facesUtil.addErrMessage("NPK & Nama PIC Tindak Lanjut harus diisi");
						flag = true;
					}
				}
				}
			}
			
		}
		
		return flag;
	}
	
	
	public void saveAndRedirect() {
		if(complianceTesting.getInspectionTitle()!=null && complianceTesting.getInspectionTitle().length()>150) {
    		facesUtil.addErrMessage("Judul Pemeriksaan harus lebih kecil dari 150 karakter");
    		
    	}
		else if(complianceTesting.getInspectionNo()!=null && complianceTesting.getInspectionNo().length()>150) {
    		facesUtil.addErrMessage("Nomor Pemeriksaan harus lebih kecil dari 150 karakter");
    		
    	}else {
			save();
			try {
				facesUtil.redirect("/pages/tmpComplianceReview/tmpComplianceReview.faces");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
	}
	
	public void saveSendMailAndRedirect() {
		if(!validate()) {
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
			facesUtil.redirect("/pages/tmpComplianceReview/tmpComplianceReview.faces");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
							
							// save email reminder
							if (dtlFollow.getTargetDate() != null) {
								if (complianceTesting.getCounterType() != null 
										&& complianceTesting.getCounterType().getDetails() != null 
										&& !complianceTesting.getCounterType().getDetails().isEmpty()) {
									
									
									Calendar calendar = Calendar.getInstance();
									Date targetDateTmp = dtlFollow.getTargetDate();
									Boolean flag = false;
									if(dtlFollow.getComplianceTestingPicFollowupEmails()!=null && dtlFollow.getComplianceTestingPicFollowupEmails().size()>0) {
										flag = true;
									}
									
									if(flag) {
										if(dtlFollow.getComplianceTestingPicFollowupEmails().size() > complianceTesting.getCounterType().getDetails().size()) {
											List<Object> listDeleted = new ArrayList<>();
											
											for(int z=0;z<dtlFollow.getComplianceTestingPicFollowupEmails().size();z++) {
												if(z>=complianceTesting.getCounterType().getDetails().size()-1) {
													//dtlFollow.getTmpAuditPicFollowupEmails().remove(i);
													listDeleted.add(dtlFollow.getComplianceTestingPicFollowupEmails().get(z));
												}
											}
											listDeleted.forEach(e-> dtlFollow.getComplianceTestingPicFollowupEmails().remove(e));
										}
									}
									
									int row = 0;
									for (CounterTypeDtl dataCounterTypeDtl : complianceTesting.getCounterType().getDetails()) {
										
										
										Boolean flagLoop = false;
										if (dataCounterTypeDtl.getSlaType().equals("+")) {
											
											calendar.setTime(targetDateTmp);
											calendar.add(Calendar.DAY_OF_MONTH, dataCounterTypeDtl.getSla().intValue());
											targetDateTmp = calendar.getTime();
											
											while(!flagLoop) {
												int day = calendar.get(Calendar.DAY_OF_WEEK);
												
												if (day == 1 || day == 7) {
													// do nothing
													calendar.add(Calendar.DAY_OF_MONTH, 1);
													targetDateTmp = calendar.getTime();
												} else {
													
														if (Boolean.TRUE.equals(holidayService.isAvailableDate(targetDateTmp))) {
															flagLoop = true;
														}else {
															calendar.add(Calendar.DAY_OF_MONTH, 1);
															targetDateTmp = calendar.getTime();
														}
													
													
												}
											}
											
											
										} else if (dataCounterTypeDtl.getSlaType().equals("-")) {
											
											calendar.setTime(targetDateTmp);
											calendar.add(Calendar.DAY_OF_MONTH, -dataCounterTypeDtl.getSla().intValue());
											targetDateTmp = calendar.getTime();
											
											while(!flagLoop) {
											int day = calendar.get(Calendar.DAY_OF_WEEK);
											
											if (day == 1 || day == 7) {
												// do nothing
												calendar.add(Calendar.DAY_OF_MONTH, -1);
												targetDateTmp = calendar.getTime();
											} else {
												
													if (Boolean.TRUE.equals(holidayService.isAvailableDate(targetDateTmp))) {
														flagLoop = true;
													}else {
														calendar.add(Calendar.DAY_OF_MONTH, -1);
														targetDateTmp = calendar.getTime();
													}
												
												
											}
											}
										}
										
										ComplianceTestingPICFollowupEmail complianceTestingPICFollowupEmail =  null;
										Boolean flagAdd = false;
										if(flag) {
											if(row<dtlFollow.getComplianceTestingPicFollowupEmails().size() && dtlFollow.getComplianceTestingPicFollowupEmails().get(row)!=null && dtlFollow.getComplianceTestingPicFollowupEmails().get(row).getSla()!=null) {
												complianceTestingPICFollowupEmail = dtlFollow.getComplianceTestingPicFollowupEmails().get(row);
											}else {
												complianceTestingPICFollowupEmail =  new ComplianceTestingPICFollowupEmail();
												flagAdd = true;
											}
										}else {
											complianceTestingPICFollowupEmail =  new ComplianceTestingPICFollowupEmail();
											 flagAdd = true;
										}
										complianceTestingPICFollowupEmail.setComplianceTestingPICFollowup(dtlFollow);
										
										complianceTestingPICFollowupEmail.setSla(dataCounterTypeDtl.getSla().longValue());
										complianceTestingPICFollowupEmail.setSlaType(dataCounterTypeDtl.getSlaType());
										complianceTestingPICFollowupEmail.setEmailDate(new Timestamp(targetDateTmp.getTime()));
										
										if(complianceTestingPICFollowupEmail.getComplianceTestingPICFollowEmailId() != null)
											EntityUtil.setUpdateInfo(complianceTestingPICFollowupEmail, facesUtil.retrieveUserLogin());
										else
											EntityUtil.setCreationInfo(complianceTestingPICFollowupEmail, facesUtil.retrieveUserLogin());
										
										
										targetDateTmp = dtlFollow.getTargetDate();
										row++;
										
										if(flagAdd) {
											if(dtlFollow.getComplianceTestingPicFollowupEmails() == null) {
												List<ComplianceTestingPICFollowupEmail> complianceTestingPICFollowupEmailList = new ArrayList<>();
												complianceTestingPICFollowupEmailList.add(complianceTestingPICFollowupEmail);
												dtlFollow.setComplianceTestingPicFollowupEmails(complianceTestingPICFollowupEmailList);
											}else {
												dtlFollow.getComplianceTestingPicFollowupEmails().add(complianceTestingPICFollowupEmail);
											}
										}
									}
									
								}
							}
							// save email reminder
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
					.getEmailTemplateByEmailTemplateCode("EMAIL_TEMPLATE_COMPLIANCE_TESTING");
			String emailSubject = emailTemplate.getEmailSubject();

			String emailContent = "";
			String emailTo = "";
			String emailCc = "";
			String atasanPicFollowupEmail="";
			
			String emailToPICReview = "";
			String emailCcPICReview = "";
			String atasanPicReviewEmail="";
			
			ParameterDetail pdHost = parameterDetailService.getParameterDetailByParamDtlCode("HOST_NAME_APPLICATION");
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
							
							User atasanPICFollowup = userService.getUserByNik(dtlFollow.getUser1().getPukNik());
							if (atasanPICFollowup != null) 
							{
								atasanPicFollowupEmail = atasanPICFollowup.getEmail();
							}
							if (StringUtils.isNotEmpty(atasanPicFollowupEmail)) {
								if (StringUtils.isNotEmpty(emailCc)) {
									emailCc = emailCc.concat(",").concat(atasanPicFollowupEmail);
								} else {
									emailCc = atasanPicFollowupEmail;
								}
							}

							String token = Constants.encryptString(dtlFollow.getComplianceTestingPICFollowupId().toString());
							String menuId = Constants.encryptString(Constants.MENU_ID_FOLLOWUP_CONFIRMATION_COMPLIANCE_REVIEW);
							String urlLink = pdHost.getNameIn().concat("pages/complianceTestingFE/complianceTestingFEEdit.faces?token="+token+"&menuId="+menuId);
							
							emailSubject = emailSubject.replaceAll(TmpComplianceReviewApprovalConstants.EMAIL_TEMPLATE_EMAIL_DOCUMENT_NO,complianceTesting.getInspectionNo()!=null?complianceTesting.getInspectionNo():"");
							emailSubject = emailSubject.replaceAll(TmpComplianceReviewApprovalConstants.EMAIL_TEMPLATE_EMAIL_COUNTER_TYPE,"");
							
							//emailContent = emailContent.replaceAll(TmpComplianceReviewApprovalConstants.EMAIL_TEMPLATE_EMAIL_TARGET_DATE, dtlFollow.getTargetDate()!=null?sdf.format(dtlFollow.getTargetDate()):"");
							//emailContent = emailContent.replaceAll(TmpComplianceReviewApprovalConstants.EMAIL_TEMPLATE_EMAIL_DOCUMENT_DATE, complianceTesting.get!=null?vo.getDocumentDateStr():"");
							//emailContent = emailContent.replaceAll(TmpComplianceReviewApprovalConstants.EMAIL_TEMPLATE_EMAIL_PERIHAL_IN, complianceTesting.getInspectionTitle()!=null?complianceTesting.getInspectionTitle():"");
							//emailContent = emailContent.replaceAll(TmpComplianceReviewApprovalConstants.EMAIL_TEMPLATE_EMAIL_PERIHAL_EN,  complianceTesting.getInspectionTitle()!=null? complianceTesting.getInspectionTitle():"");
							emailContent = emailContent.replaceAll(TmpComplianceReviewApprovalConstants.EMAIL_TEMPLATE_EMAIL_URL_LINK, urlLink);
							
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
				
				
				if(complianceTesting.getComplianceTestingPICReviews()!=null && complianceTesting.getComplianceTestingPICReviews().size()>0) {
					for(int x=0;x<complianceTesting.getComplianceTestingPICReviews().size();x++) {
						ComplianceTestingPICReview picReview = complianceTesting.getComplianceTestingPICReviews().get(x);
						if(StringUtils.isEmpty(picReview.getLastUpdateBy()) || (picReview.getLastUpdateDate()!=null && sdf.format(picReview.getLastUpdateDate()).equals(sdf.format(new Date())))) {
						emailToPICReview = picReview.getUser().getEmail();
						
						User atasanPICUser = userService.getUserByNik(picReview.getUser().getPukNik());
						if (atasanPICUser != null) 
						{
							atasanPicReviewEmail = atasanPICUser.getEmail();
						}
						if (StringUtils.isNotEmpty(atasanPicReviewEmail)) {
							if (StringUtils.isNotEmpty(emailCcPICReview)) {
								emailCcPICReview = emailCc.concat(",").concat(atasanPicReviewEmail);
							} else {
								emailCcPICReview = atasanPicReviewEmail;
							}
						}

						String token = Constants.encryptString(picReview.getComplianceTestingPICRvwId().toString());
						String menuId = Constants.encryptString(Constants.MENU_ID_FOLLOWUP_CONFIRMATION_COMPLIANCE_REVIEW);
						String urlLink = pdHost.getNameIn().concat("pages/complianceTestingFE/complianceTestingFEEdit.faces?token="+token+"&menuId="+menuId);
						
						emailSubject = emailSubject.replaceAll(TmpComplianceReviewApprovalConstants.EMAIL_TEMPLATE_EMAIL_DOCUMENT_NO,complianceTesting.getInspectionNo()!=null?complianceTesting.getInspectionNo():"");
						emailSubject = emailSubject.replaceAll(TmpComplianceReviewApprovalConstants.EMAIL_TEMPLATE_EMAIL_COUNTER_TYPE,"");
						emailContent = emailContent.replaceAll(TmpComplianceReviewApprovalConstants.EMAIL_TEMPLATE_EMAIL_URL_LINK, urlLink);
						
						final String subject = emailSubject;
						final String content = emailContent;
						final String to = emailToPICReview;
						final String cc = emailCcPICReview;
					

						CallApiManager.sendEmailAPI(to, cc, subject, content, "EMAIL_COMPLIANCE_ASSESSMENT", "true",
								parameterDetailService);
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
			
			User userAtasan = userService.getUserByNik(user.getPukNik());
			if (userAtasan != null) {
				complianceTesting.getComplianceTestingPICReviews().get(indexDtlComplianceTestingPICReview)
				.setNameAtasanTemp(userAtasan.getName());
			}
			
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
			complianceTesting.getComplianceTestingDtls().get(indexDtlSubject).getComplianceTestingPICFollowups().get(indexDtlCompliancePiCFollowup).setSubBranchName((String)objects[3]);
			tableModelComplianceTestingFollowup.setWrappedData(complianceTesting.getComplianceTestingDtls().get(indexDtlSubject).getComplianceTestingPICFollowups());
			
			PrimeFaces.current().ajax()
			.update("form:listSubjectRpt:"+indexDtlSubject+":dataTableComplianceRpt");
		
		}

	}
	

	public static Logger getLogger() {
		return logger;
	}


	public static void setLogger(Logger logger) {
		TmpComplianceReviewEditBean.logger = logger;
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

	public HolidayService getHolidayService() {
		return holidayService;
	}

	public void setHolidayService(HolidayService holidayService) {
		this.holidayService = holidayService;
	}

	public List<SelectItem> getRatings() {
		return ratings;
	}

	public void setRatings(List<SelectItem> ratings) {
		this.ratings = ratings;
	}
	
	
}