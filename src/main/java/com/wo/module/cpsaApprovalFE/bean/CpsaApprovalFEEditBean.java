package com.wo.module.cpsaApprovalFE.bean;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.util.FileUtil;
import com.wo.module.common.utility.CallApiManager;
import com.wo.module.cpsa.model.CompliancePlanSelfAssessment;
import com.wo.module.cpsaApprovalFE.constant.CpsaApprovalFEConstant;
import com.wo.module.cpsaApprovalFE.service.CpsaApprovalFEService;
import com.wo.module.cpsaApprovalFE.vo.CpsaApprovalFEVo;
import com.wo.module.cpsaApprovalFE.vo.CpsaApprovalQuestionFEVo;
import com.wo.module.cpsaFE.constant.CompliancePlanSelfAssessmentFEConstant;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.user.model.User;
import com.wo.module.user.service.UserService;

public class CpsaApprovalFEEditBean extends CommonBean implements Serializable{

	private static final long serialVersionUID = 9016395653731012795L;
	
	private static final Logger logger = Logger.getLogger(CpsaApprovalFEEditBean.class);
	
	private static final String NAVIGATE_BACK = CpsaApprovalFEConstant.NAVIGATE_CPSA_APPROVAL;
	
	private CpsaApprovalFEService cpsaApprovalFEService;
	private UserService userService;
	
	private CompliancePlanSelfAssessment compliancePlanSelfAssessment;
	private CpsaApprovalFEVo cpsaVo;
	private User userLogin;
	
	private String viewId;
	private String tanggalUpload;
	private String periode;
	private String cpsaKepatuhan;
	private String approvalNote;
	private String cpsaTypeName;
	
	private Long fileSizeKB;
	
	private FacesUtil facesUtil;
	
	private FileUtil fileUtil;

	private SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", new Locale("id", "ID"));
	
	private Integer testFirst;
	
	private List<SelectItem> cpsaKepatuhans;
	
	private List<CpsaApprovalQuestionFEVo> cpsaQuestions;
	
	private boolean isCanEdit;
	
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
		if (facesUtil.retrieveRequestParam("first") != null) {
			testFirst = Integer.parseInt(facesUtil.retrieveRequestParam("first"));
			facesUtil.setSessionAttribute("FIRST_CPSA_FE", testFirst);
			facesUtil.setSessionAttribute("BACK_SESSION", false);
		}
		userLogin = getUserLogin();
		checkNewOrEdit();
		dataaCpsaKepatuhan();
		fileUtil = FileUtil.getInstance();
	}
	
	private void dataaCpsaKepatuhan() {
		cpsaKepatuhans = new ArrayList<SelectItem>();
		try {
			List<ParameterDetail> pd = parameterDetailService.getParameterDetailByParamCode("CPSA_KEPATUHAN");
			for (int i = 0; i < pd.size(); i++) {
				SelectItem si = new SelectItem();
				si.setLabel(((ParameterDetail) pd.get(i)).getName());
				si.setValue(((ParameterDetail) pd.get(i)).getParameterDtlCode());
				cpsaKepatuhans.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private void checkNewOrEdit() {
		this.viewId = facesUtil.retrieveRequestParam("viewId");
		
		String token = facesUtil.retrieveRequestParam("token");
		if(StringUtils.isNotEmpty(token)) {
			viewId = Constants.decryptString(token);
		}
		facesUtil.setSessionAttribute("token", Constants.encryptString(viewId));
		
		cpsaVo = new CpsaApprovalFEVo();
		isCanEdit = true;
		if (StringUtils.isBlank(viewId)) {
			// do nothing
		} else {
			handleView(viewId);
		}
	}
	
	private void handleView(String viewId) {
		Long viewIdLong = Long.parseLong(viewId);		
		dataCpsa(viewIdLong);
	}
	
	public void cancel() {
		try {
			if (facesUtil.retrieveRequestParam("first") != null) {
				facesUtil.setSessionAttribute("BACK_SESSION", true);
			}
			facesUtil.redirect("/pages/cpsaApprovalFE/cpsaApprovalFE.faces");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void downloadFile(String fileId, String fileName, byte[] content) throws Exception {
		CallApiManager.downloadFile(fileId, fileName, content, parameterDetailService);
	}
	
	private void dataCpsa(Long cpsaId) {
		try {
			CompliancePlanSelfAssessment compliancePlanSelfAssessment = cpsaApprovalFEService.findById(cpsaId);
			cpsaVo = cpsaApprovalFEService.getDataCpsaPic(cpsaId, null, userLogin.getUserId());			
			User userData = userService.findById(cpsaVo.getUserId1());
			if (compliancePlanSelfAssessment.getCpsaType() != null 
					&& (compliancePlanSelfAssessment.getCpsaType().getParameterDtlCode().equals("COMPLIANCE_PLAN_SELF_ASSESSMENT_TYPE_SYARIAH")
							|| compliancePlanSelfAssessment.getCpsaType().getParameterDtlCode().equals("COMPLIANCE_PLAN_SELF_ASSESSMENT_TYPE_CPSA"))) {
				cpsaVo.setDirectorate(userService.getRegionByBranchCode(cpsaVo.getBranchCode()));
//				cpsaVo.setDirectorate(userService.getSubBranchNameByBranchCode(cpsaVo.getBranchCode()));
				cpsaVo.setDivisionName(userService.getSubBranchNameByBranchCode(cpsaVo.getBranchCode()));
			} else {
				cpsaVo.setDirectorate(userService.getDirectorateByDivisionId(cpsaVo.getDivisionId()));
			}
		
			if (cpsaVo != null 
					&& StringUtils.isNotBlank(cpsaVo.getStatusPic())
					&& cpsaVo.getStatusPic().equals(CompliancePlanSelfAssessmentFEConstant.STATUS_CPSA_REJECTED)) {
				isCanEdit = false;
			}
			
			cpsaQuestions = new ArrayList<>();
			cpsaQuestions = cpsaApprovalFEService.getDataQuestion(cpsaId, cpsaVo.getUserId1());
			
			if (compliancePlanSelfAssessment != null && compliancePlanSelfAssessment.getCpsaType() != null && StringUtils.isNotBlank(compliancePlanSelfAssessment.getCpsaType().getParameterDtlCode())) {
				if (compliancePlanSelfAssessment.getCpsaType().getParameterDtlCode().equals(ParameterDetail.PARAM_DET_COMPLIANCE_PLAN_SELF_ASSESSMENT_TYPE_SYARIAH)) {
					cpsaTypeName = parameterDetailService.getParamDtlNameByParamDtlCode(ParameterDetail.PARAM_DET_CPSA_HEADER_SYARIAH);
				} else if(compliancePlanSelfAssessment.getCpsaType().getParameterDtlCode().equals(ParameterDetail.PARAM_DET_COMPLIANCE_PLAN_SELF_ASSESSMENT_TYPE_CPSA)) {
					cpsaTypeName = parameterDetailService.getParamDtlNameByParamDtlCode(ParameterDetail.PARAM_DET_CPSA_HEADER_CPSA);
				} else {
					cpsaTypeName = parameterDetailService.getParamDtlNameByParamDtlCode(ParameterDetail.PARAM_DET_CPSA_HEADER_PUSAT);;
				}
			}
		}catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private boolean isValidate(String approvalDataNote) {
		boolean flag = true;		
		if(approvalDataNote == null || approvalDataNote.equals(CpsaApprovalFEConstant.STRING_EMPTY)) {
			facesUtil.addErrMessage(facesUtil.retrieveMessage("formCompliancePlanSelfAssessmentNote") + " "
					+ facesUtil.retrieveMessage("validateRequired"));
			flag = false;
		}
		
		return flag;
	}
	
	public void approval() {
		try {
			String approvalDataNote = facesUtil.retrieveRequestParam("APPROVAL_NOTE");
			cpsaVo.setNote(approvalDataNote);
			cpsaApprovalFEService.approval(cpsaVo, facesUtil.retrieveUserLogin());
			
			facesUtil.redirect("/pages/cpsaApprovalFE/cpsaApprovalFE.faces");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void reject() {
		try {
			String approvalDataNote = facesUtil.retrieveRequestParam("APPROVAL_NOTE");
			if(isValidate(approvalDataNote)) {
				cpsaVo.setNote(approvalDataNote);
				cpsaApprovalFEService.reject(cpsaVo, facesUtil.retrieveUserLogin());
				
				facesUtil.redirect("/pages/cpsaApprovalFE/cpsaApprovalFE.faces");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public CpsaApprovalFEService getCpsaApprovalFEService() {
		return cpsaApprovalFEService;
	}

	public void setCpsaApprovalFEService(CpsaApprovalFEService cpsaApprovalFEService) {
		this.cpsaApprovalFEService = cpsaApprovalFEService;
	}

	public CompliancePlanSelfAssessment getCompliancePlanSelfAssessment() {
		return compliancePlanSelfAssessment;
	}

	public void setCompliancePlanSelfAssessment(CompliancePlanSelfAssessment compliancePlanSelfAssessment) {
		this.compliancePlanSelfAssessment = compliancePlanSelfAssessment;
	}

	public CpsaApprovalFEVo getCpsaVo() {
		return cpsaVo;
	}

	public void setCpsaVo(CpsaApprovalFEVo cpsaVo) {
		this.cpsaVo = cpsaVo;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public String getViewId() {
		return viewId;
	}

	public void setViewId(String viewId) {
		this.viewId = viewId;
	}

	public String getTanggalUpload() {
		return tanggalUpload;
	}

	public void setTanggalUpload(String tanggalUpload) {
		this.tanggalUpload = tanggalUpload;
	}

	public String getPeriode() {
		return periode;
	}

	public void setPeriode(String periode) {
		this.periode = periode;
	}

	public String getCpsaKepatuhan() {
		return cpsaKepatuhan;
	}

	public void setCpsaKepatuhan(String cpsaKepatuhan) {
		this.cpsaKepatuhan = cpsaKepatuhan;
	}

	public Long getFileSizeKB() {
		return fileSizeKB;
	}

	public void setFileSizeKB(Long fileSizeKB) {
		this.fileSizeKB = fileSizeKB;
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

	public Integer getTestFirst() {
		return testFirst;
	}

	public void setTestFirst(Integer testFirst) {
		this.testFirst = testFirst;
	}

	public List<SelectItem> getCpsaKepatuhans() {
		return cpsaKepatuhans;
	}

	public void setCpsaKepatuhans(List<SelectItem> cpsaKepatuhans) {
		this.cpsaKepatuhans = cpsaKepatuhans;
	}

	public List<CpsaApprovalQuestionFEVo> getCpsaQuestions() {
		return cpsaQuestions;
	}

	public void setCpsaQuestions(List<CpsaApprovalQuestionFEVo> cpsaQuestions) {
		this.cpsaQuestions = cpsaQuestions;
	}

	public static Logger getLogger() {
		return logger;
	}

	public static String getNavigateBack() {
		return NAVIGATE_BACK;
	}

	public String getApprovalNote() {
		return approvalNote;
	}

	public void setApprovalNote(String approvalNote) {
		this.approvalNote = approvalNote;
	}

	public boolean isCanEdit() {
		return isCanEdit;
	}

	public void setCanEdit(boolean isCanEdit) {
		this.isCanEdit = isCanEdit;
	}

	public String getCpsaTypeName() {
		return cpsaTypeName;
	}

	public void setCpsaTypeName(String cpsaTypeName) {
		this.cpsaTypeName = cpsaTypeName;
	}
	
	
}
