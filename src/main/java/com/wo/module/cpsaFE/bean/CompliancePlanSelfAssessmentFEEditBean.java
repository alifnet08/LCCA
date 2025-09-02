package com.wo.module.cpsaFE.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
import org.primefaces.PrimeFaces;

import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.CommonConstants;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.util.FileUtil;
import com.wo.module.common.utility.CallApiManager;
import com.wo.module.cpsa.model.CompliancePlanSelfAssessment;
import com.wo.module.cpsaFE.constant.CompliancePlanSelfAssessmentFEConstant;
import com.wo.module.cpsaFE.service.CompliancePlanSelfAssessmentFEService;
import com.wo.module.cpsaFE.vo.CompliancePlanSelfAssessmentFEVo;
import com.wo.module.cpsaFE.vo.CompliancePlanSelfAssessmentQuestionFEVo;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.user.model.User;
import com.wo.module.user.service.UserService;

public class CompliancePlanSelfAssessmentFEEditBean extends CommonBean implements Serializable{

	private static final long serialVersionUID = 3192654159089460027L;
	private static final Logger logger = Logger.getLogger(CompliancePlanSelfAssessmentFEEditBean.class);
	private static final String NAVIGATE_BACK = CompliancePlanSelfAssessmentFEConstant.NAVIGATE_CPSA;
	
	private CompliancePlanSelfAssessmentFEService compliancePlanSelfAssessmentFEService;
	
	private CompliancePlanSelfAssessment compliancePlanSelfAssessment;
	private CompliancePlanSelfAssessmentFEVo cpsaVo;
	private User userLogin;
	
	private UserService userService;
	
	private String viewId;
	private String tanggalUpload;
	private String periode;
	private String cpsaKepatuhan;
	private String cpsaTypeName;
	private boolean viewLockFlag;
	
	private Long fileSizeKB;
	
	private FacesUtil facesUtil;
	
	private FileUtil fileUtil;

	private SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", new Locale("id", "ID"));
	
	private Integer testFirst;
	
	private List<SelectItem> cpsaKepatuhans;
	
	private List<CompliancePlanSelfAssessmentQuestionFEVo> cpsaQuestions;
	
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
			List<ParameterDetail> pd = parameterDetailService.getParameterDetailByParamCodeOrderById("CPSA_KEPATUHAN");
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
		//this.viewId = facesUtil.retrieveRequestParam("viewId");
		
		String token = facesUtil.retrieveRequestParam("token");
		if(StringUtils.isNotEmpty(token)) {
			viewId = Constants.decryptString(token);
		}
		facesUtil.setSessionAttribute("token", Constants.encryptString(viewId));
		
		cpsaVo = new CompliancePlanSelfAssessmentFEVo();
		if (StringUtils.isBlank(viewId)) {
			// do nothing
		} else {
			handleView(viewId);
		}
	}
	
	@SuppressWarnings("deprecation")
	private void handleView(String viewId) {
		Long viewIdLong = Long.parseLong(viewId);
		
		compliancePlanSelfAssessment = compliancePlanSelfAssessmentFEService.findById(viewIdLong);
		tanggalUpload = compliancePlanSelfAssessment.getUploadDate() != null ? sdf.format(compliancePlanSelfAssessment.getUploadDate()) : "";
		if (compliancePlanSelfAssessment.getPeriodFrom() != null) {
			periode = sdf.format(compliancePlanSelfAssessment.getPeriodFrom());				
			if (compliancePlanSelfAssessment.getPeriodTo() != null) {
				periode = sdf.format(compliancePlanSelfAssessment.getPeriodFrom()) 
						+ " - " 
						+ sdf.format(compliancePlanSelfAssessment.getPeriodTo());
			}
		} else {
			if (compliancePlanSelfAssessment.getPeriodTo() != null) {
				periode = sdf.format(compliancePlanSelfAssessment.getPeriodTo());
			} else {
				periode = "";
			}
		}
		
		if (compliancePlanSelfAssessment.getFileSize() != null) {
			BigDecimal var = new BigDecimal(compliancePlanSelfAssessment.getFileSize()).divide(new BigDecimal(1024), RoundingMode.UP);
			this.fileSizeKB = var.longValue();
		} else {
			this.fileSizeKB = new Long(0);
		}
		
		dataCpsa(viewIdLong, compliancePlanSelfAssessment);
	}
	
	public void onChangeCpsaKepatuhan(int index) {
		if (cpsaQuestions != null && !cpsaQuestions.isEmpty()) {
			for (int i = 0; i < cpsaQuestions.size(); i++) {
				CompliancePlanSelfAssessmentQuestionFEVo dataQuestion = cpsaQuestions.get(i);
				
				if (i == index) {
					if (StringUtils.isNotBlank(dataQuestion.getCpsaAnswer()) && dataQuestion.getCpsaAnswer().equals("COMPLIANT")) {
						dataQuestion.setIsCanEdit(false);
						PrimeFaces.current().ajax().update("form:formTableData:dataTableCpsaQuestion:"+index+":panelCpsaNote");
					} else {
						dataQuestion.setIsCanEdit(true);
						PrimeFaces.current().ajax().update("form:formTableData:dataTableCpsaQuestion:"+index+":panelCpsaNote");
					}
				}
			}
		}
	}
	
	public void cancel() {
		try {
			if (facesUtil.retrieveRequestParam("first") != null) {
				facesUtil.setSessionAttribute("BACK_SESSION", true);
			}
			facesUtil.redirect("/pages/cpsaFE/compliancePlanSelfAssessmentFE.faces");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void downloadFile(String fileId, String fileName, byte[] content) throws Exception {
		CallApiManager.downloadFile(fileId, fileName, content, parameterDetailService);
	}
	
	private void dataCpsa(Long cpsaId, CompliancePlanSelfAssessment compliancePlanSelfAssessment) {
		try {			
			cpsaVo = compliancePlanSelfAssessmentFEService.getDataCpsaPic(cpsaId, userLogin.getUserId());
//			boolean lockFlag = validateUserLogin(cpsaVo, userLogin);
			
//			if(lockFlag) {
//				setViewLockFlag(true);
//			}else {
//				//add error message and set detail page to view only
//				setViewLockFlag(false);
//				
//				addErrMessage(facesUtil.retrieveMessage("formCompliancePlanSelfAssessmentTitleShort") + 
//					" dikunci, kertas kerja diset View Only");
//			}
			
			if (compliancePlanSelfAssessment.getCpsaType() != null 
					&& (compliancePlanSelfAssessment.getCpsaType().getParameterDtlCode().equals("COMPLIANCE_PLAN_SELF_ASSESSMENT_TYPE_SYARIAH")
							|| compliancePlanSelfAssessment.getCpsaType().getParameterDtlCode().equals("COMPLIANCE_PLAN_SELF_ASSESSMENT_TYPE_CPSA"))) {
				cpsaVo.setDirectorate(userService.getRegionByBranchCode(cpsaVo.getBranchCode()));
//				cpsaVo.setDirectorate(userService.getSubBranchNameByBranchCode(cpsaVo.getBranchCode()));
				cpsaVo.setDivisionName(userService.getSubBranchNameByBranchCode(cpsaVo.getBranchCode()));
			} else {
				cpsaVo.setDirectorate(userService.getDirectorateByDivisionId(cpsaVo.getDivisionId()));
			}
			
			if(cpsaVo.getStatusPic() == null || cpsaVo.getStatusPic().equals(CompliancePlanSelfAssessmentFEConstant.STRING_EMPTY) ||
					cpsaVo.getStatusPic().equals(CompliancePlanSelfAssessmentFEConstant.STATUS_CPSA_INPROGRESS) ||
					cpsaVo.getStatusPic().equals(CompliancePlanSelfAssessmentFEConstant.STATUS_CPSA_REJECTED) ||
					cpsaVo.getStatusPic().equals(CompliancePlanSelfAssessmentFEConstant.STATUS_COMPLIANCE_OPEN)) {
				cpsaVo.setFlagStatus(false);
			}else {
				cpsaVo.setFlagStatus(true);
			}
			
			//used if lockFlag still needed
//			if(viewLockFlag) {
//				
//			}else {
//				cpsaVo.setFlagStatus(false);
//			}
				
				
			cpsaQuestions = new ArrayList<>();
			
			
			//if(cpsaVo.getUserId2() == userLogin.getUserId()) {
			//	cpsaQuestions = compliancePlanSelfAssessmentFEService.getDataQuestion(cpsaId, cpsaVo.getUserId2());
			//} else {	
				cpsaQuestions = compliancePlanSelfAssessmentFEService.getDataQuestion(cpsaId, userLogin.getUserId());
			//}
			
			for (int i = 0; i < cpsaQuestions.size(); i++) {
				CompliancePlanSelfAssessmentQuestionFEVo dataQuestion = cpsaQuestions.get(i);
				
				if (StringUtils.isNotBlank(dataQuestion.getCpsaAnswer()) && dataQuestion.getCpsaAnswer().equals("COMPLIANT")) {
					dataQuestion.setIsCanEdit(false);
				} else {
					if(cpsaVo.getStatusPic() == null || cpsaVo.getStatusPic().equals(CompliancePlanSelfAssessmentFEConstant.STRING_EMPTY) ||
							cpsaVo.getStatusPic().equals(CompliancePlanSelfAssessmentFEConstant.STATUS_CPSA_INPROGRESS) ||
							cpsaVo.getStatusPic().equals(CompliancePlanSelfAssessmentFEConstant.STATUS_CPSA_REJECTED) ||
							cpsaVo.getStatusPic().equals(CompliancePlanSelfAssessmentFEConstant.STATUS_COMPLIANCE_OPEN)) {
						dataQuestion.setIsCanEdit(true);
					} else {
						dataQuestion.setIsCanEdit(false);
					}
				}
			}
			
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
	
	private boolean validateUserLogin(CompliancePlanSelfAssessmentFEVo cpsaFEVo, User userLogin) {
		try {
			if(cpsaFEVo.getLockFlag() != null) {
				if(cpsaFEVo.getLockFlag().equals(CommonConstants.RECORD_FLAG_YES)) {
					return true;
				}else {
					if(cpsaFEVo.getUserNikLock().equals(userLogin.getNik())) {
						return true;
					}else {
						return false;
					}
				}
			}else {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	private boolean isValidate() {
		boolean flag = true;
		if(cpsaQuestions !=null && cpsaQuestions.size() > 0) {
			Integer indexRow = 0;
			String headerQuest = CompliancePlanSelfAssessmentFEConstant.STRING_EMPTY;
			for(CompliancePlanSelfAssessmentQuestionFEVo quetVo : cpsaQuestions) {
				indexRow++;
				if(quetVo.getCpsaParentQuestId() == null || quetVo.getCpsaParentQuestId() <= 0) {
					headerQuest = quetVo.getCpsaQuestion();
				}
				
				if(quetVo.getCpsaAnswer() !=null && !quetVo.getCpsaAnswer().equals(CompliancePlanSelfAssessmentFEConstant.STRING_EMPTY) && 
						!quetVo.getCpsaAnswer().equals(CompliancePlanSelfAssessmentFEConstant.ANSWER_COMPLIANT)) {
				    if(quetVo.getCpsaNote() == null || quetVo.getCpsaNote().equals(CompliancePlanSelfAssessmentFEConstant.STRING_EMPTY)) {
				    	facesUtil.addErrMessage(facesUtil.retrieveMessage("formCompliancePlanSelfAssessmentNoteRequired",  headerQuest + " No. " + quetVo.getCpsaQuestNo()));
						flag = false;
				    }
				}
			}
		}
	
		return flag;
	}
	
	public void save() {
		try {
			compliancePlanSelfAssessmentFEService.saveAnswer(cpsaVo, cpsaQuestions, facesUtil.retrieveUserLogin());
			
			facesUtil.redirect("/pages/cpsaFE/compliancePlanSelfAssessmentFE.faces");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void submit() {
		try {
			if(isValidate()) {
				compliancePlanSelfAssessmentFEService.submitAnswer(cpsaVo, cpsaQuestions, facesUtil.retrieveUserLogin());
				
				facesUtil.redirect("/pages/cpsaFE/compliancePlanSelfAssessmentFE.faces");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public CompliancePlanSelfAssessmentFEService getCompliancePlanSelfAssessmentFEService() {
		return compliancePlanSelfAssessmentFEService;
	}

	public void setCompliancePlanSelfAssessmentFEService(
			CompliancePlanSelfAssessmentFEService compliancePlanSelfAssessmentFEService) {
		this.compliancePlanSelfAssessmentFEService = compliancePlanSelfAssessmentFEService;
	}

	public CompliancePlanSelfAssessment getCompliancePlanSelfAssessment() {
		return compliancePlanSelfAssessment;
	}

	public void setCompliancePlanSelfAssessment(CompliancePlanSelfAssessment compliancePlanSelfAssessment) {
		this.compliancePlanSelfAssessment = compliancePlanSelfAssessment;
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

	public Long getFileSizeKB() {
		return fileSizeKB;
	}

	public void setFileSizeKB(Long fileSizeKB) {
		this.fileSizeKB = fileSizeKB;
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

	public String getCpsaKepatuhan() {
		return cpsaKepatuhan;
	}

	public void setCpsaKepatuhan(String cpsaKepatuhan) {
		this.cpsaKepatuhan = cpsaKepatuhan;
	}

	public CompliancePlanSelfAssessmentFEVo getCpsaVo() {
		return cpsaVo;
	}

	public void setCpsaVo(CompliancePlanSelfAssessmentFEVo cpsaVo) {
		this.cpsaVo = cpsaVo;
	}

	public List<CompliancePlanSelfAssessmentQuestionFEVo> getCpsaQuestions() {
		return cpsaQuestions;
	}

	public void setCpsaQuestions(List<CompliancePlanSelfAssessmentQuestionFEVo> cpsaQuestions) {
		this.cpsaQuestions = cpsaQuestions;
	}

	public String getCpsaTypeName() {
		return cpsaTypeName;
	}

	public void setCpsaTypeName(String cpsaTypeName) {
		this.cpsaTypeName = cpsaTypeName;
	}

	public boolean isViewLockFlag() {
		return viewLockFlag;
	}

	public void setViewLockFlag(boolean viewLockFlag) {
		this.viewLockFlag = viewLockFlag;
	}
	
	
	
}
