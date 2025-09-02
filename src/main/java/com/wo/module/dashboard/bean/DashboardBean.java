package com.wo.module.dashboard.bean;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;
import org.primefaces.PrimeFaces;

import com.wo.module.common.constant.Constants;
import com.wo.module.dashboard.service.DashboardService;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.parameter.service.ParameterDetailService;

public class DashboardBean implements Serializable {

	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(DashboardBean.class);

	private Long totalExternalRegulationApproval;
	private Long totalInternalRegulationApproval;
	private Long totalReportMatrixDiaryApproval;
	private Long totalCorrespondenceApproval;
	private Long totalRegulationSocializationApproval;
	private Long totalAuditApproval;
	private Long totalComplianceOnsiteReviewApproval;
	private Long totalCorrespondenceAmlApproval;
	
	private Long totalArticleApproval;
	private Long totalRegulationMonitoringApproval;
	private Long totalFineApproval;

	private Long totalExternalRegulationRevised;
	private Long totalInternalRegulationRevised;
	private Long totalReportMatrixDiaryRevised;
	private Long totalCorrespondenceRevised;
	private Long totalRegulationSocializationRevised;	
	private Long totalAuditRevised;
	private Long totalComplianceOnsiteReviewRevised;
	private Long totalCorrespondenceAmlRevised;
	
	private Long totalArticleRevised;
	private Long totalRegulationMonitoringRevised;
	private Long totalFineRevised;
	
	private Long totalCorrespondenceVerification;
	private Long totalRegulationSocializationVerification;
	private Long totalAuditVerification;
	private Long totalComplianceOnsiteReviewVerification;
	private Long totalCorrespondenceAmlVerification;
	private Long totalCpsaVerification;
	
	private Long totalArticleVerification;
	private Long totalRegulationMonitoringVerification;
	private Long totalFineVerification;
	
	private Long totalQAAdmin;
	private Long totalQAPIC;
	private Long totalQAView;
	
	private Long totalFAQApproval;
	
	private String navigateExternalRegulationApproval = "/compliance/pages/externalRegulationApproval/externalRegulationApproval.faces";
	private String navigateInternalRegulationApproval = "/compliance/pages/internalRegulationApproval/internalRegulationApproval.faces";
	private String navigateRmdApproval = "/compliance/pages/tmpRmdApproval/tmpRmdApproval.faces";
	private String navigateCorrespondenceApproval = "/compliance/pages/tmpCorrespondenceApproval/tmpCorrespondenceApproval.faces";
	private String navigateSocializationRegulationApproval = "/compliance/pages/regulationSocializationApproval/regulationSocializationApproval.faces";
	private String navigateAuditApproval = "/compliance/pages/tmpAuditApproval/tmpAuditApproval.faces";
	private String navigateComplianceOnsiteReviewApproval = "/compliance/pages/tmpComplianceReviewApproval/tmpComplianceReviewApproval.faces";
	private String navigateCorrespondenceAmlApproval = "/compliance/pages/tmpCorrespondenceApprovalAml/tmpCorrespondenceApprovalAml.faces";
	
	private String navigateArticleApproval = "/compliance/pages/articleApproval/articleApproval.faces";
	private String navigateRegulationMonitoringApproval = "/compliance/pages/regulationMonitoringApproval/regulationMonitoringApproval.faces";
	private String navigateFineApproval = "/compliance/pages/tmpFineApproval/tmpFineApproval.faces";

	private String navigateExternalRegulationRevised = "/compliance/pages/externalRegulation/externalRegulation.faces";
	private String navigateInternalRegulationRevised = "/compliance/pages/internalRegulation/internalRegulation.faces";
	private String navigateRmdRevised = "/compliance/pages/tmpRmd/tmpRmd.faces";
	private String navigateCorrespondenceRevised = "/compliance/pages/tmpCorrespondence/tmpCorrespondence.faces";
	private String navigateSocializationRegulationRevised = "/compliance/pages/regulationSocialization/regulationSocialization.faces";
	private String navigateAuditRevised = "/compliance/pages/tmpAudit/tmpAudit.faces";
	private String navigateComplianceOnsiteReviewRevised = "/compliance/pages/tmpComplianceReview/tmpComplianceReview.faces";
	private String navigateCorrespondenceAmlRevised = "/compliance/pages/tmpCorrespondenceAml/tmpCorrespondenceAml.faces";
	
	private String navigateArticleRevised = "/compliance/pages/article/article.faces";
	private String navigateRegulationMonitoringRevised = "/compliance/pages/regulationMonitoring/regulationMonitoring.faces";
	private String navigateFineRevised = "/compliance/pages/tmpFine/tmpFine.faces";
	
	private String navigateCorrespondenceVerification = "/compliance/pages/trcCorrespondenceApproval/trcCorrespondenceApproval.faces";
	private String navigateSocializationRegulationVerification = "/compliance/pages/regulationSocializationVerification/regulationSocializationVerification.faces";
	private String navigateAuditVerification = "/compliance/pages/trcAuditVerification/trcAuditVerification.faces";
	private String navigateComplianceOnsiteReviewVerification = "/compliance/pages/trcComplianceReviewApproval/trcComplianceReviewApproval.faces";
	private String navigateCorrespondenceAmlVerification = "/compliance/pages/trcCorrespondenceApprovalAml/trcCorrespondenceApprovalAml.faces";
	private String navigateCpsaVerification = "/compliance/pages/cpsaVerification/cpsaVerification.faces";
	
	private String navigateRegulationMonitoringVerification = "/compliance/pages/regulationMonitoringVerification/regulationMonitoringVerification.faces";
	private String navigateFineVerification = "/compliance/pages/trcFineApproval/trcFineApproval.faces";
	
	private String navigateQAAdmin = "/compliance/pages/qaAdmin/qaAdmin.faces";
	private String navigateQAPIC = "/compliance/pages/qaPic/qaPic.faces";
	private String navigateQAView = "/compliance/pages/qa/qa.faces";
	
	private String navigateFAQApproval = "/compliance/pages/tmpFaqApproval/tmpFaqApproval.faces";
	
	public String url;
	
	private DashboardService dashboardService;
	public ParameterDetailService parameterDetailService;

	public FacesUtil facesUtil;

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
		String nikLogin = facesUtil.retrieveUserLogin();

		totalExternalRegulationApproval = dashboardService.searchCountDataExternalRegulationWaitingApproval(nikLogin,
				navigateExternalRegulationApproval);
		totalInternalRegulationApproval = dashboardService.searchCountDataInternalRegulationWaitingApproval(nikLogin,
				navigateInternalRegulationApproval);
		totalReportMatrixDiaryApproval = dashboardService.searchCountDataReportMatrixDiaryWaitingApproval(nikLogin,
				navigateRmdApproval);
		totalCorrespondenceApproval = dashboardService.searchCountDataCorrespondenceWaitingApproval(nikLogin,
				navigateCorrespondenceApproval);
		totalRegulationSocializationApproval = dashboardService.searchCountDataRegulationSocializationWaitingApproval(
				nikLogin, navigateSocializationRegulationApproval);
		totalAuditApproval = dashboardService.searchCountDataAuditWaitingApproval(nikLogin, 
				navigateAuditApproval);
		totalComplianceOnsiteReviewApproval = dashboardService.searchCountDataComplianceOnsiteReviewWaitingApproval(nikLogin, 
				navigateComplianceOnsiteReviewApproval);
		totalCorrespondenceAmlApproval = dashboardService.searchCountDataCorrespondenceAmlWaitingApproval(nikLogin,
				navigateCorrespondenceAmlApproval);
		
		totalArticleApproval = dashboardService.searchCountDataArticleWaitingApproval(nikLogin, 
				navigateArticleApproval);
		totalRegulationMonitoringApproval = dashboardService.searchCountDataRegulationMonitoringWaitingApproval(nikLogin, 
				navigateRegulationMonitoringApproval);
		totalFineApproval = dashboardService.searchCountDataFineWaitingApproval(nikLogin,
				navigateFineApproval);
		
		totalExternalRegulationRevised = dashboardService.searchCountDataExternalRegulationRevised(nikLogin,
				navigateExternalRegulationRevised);
		totalInternalRegulationRevised = dashboardService.searchCountDataInternalRegulationRevised(nikLogin,
				navigateInternalRegulationRevised);
		totalReportMatrixDiaryRevised = dashboardService.searchCountDataReportMatrixDiaryRevised(nikLogin,
				navigateRmdRevised);
		totalCorrespondenceRevised = dashboardService.searchCountDataCorrespondenceRevised(nikLogin,
				navigateCorrespondenceRevised);
		totalRegulationSocializationRevised = dashboardService.searchCountDataRegulationSocializationRevised(
				nikLogin, navigateSocializationRegulationRevised);
		totalAuditRevised = dashboardService.searchCountDataAuditRevised(nikLogin, 
				navigateAuditRevised);
		totalComplianceOnsiteReviewRevised = dashboardService.searchCountDataComplianceOnsiteReviewRevised(nikLogin, 
				navigateComplianceOnsiteReviewRevised);
		totalCorrespondenceAmlRevised = dashboardService.searchCountDataCorrespondenceAmlRevised(nikLogin, 
				navigateCorrespondenceAmlRevised);
		
		totalArticleRevised = dashboardService.searchCountDataArticleRevised(nikLogin, 
				navigateArticleRevised);
		totalRegulationMonitoringRevised = dashboardService.searchCountDataRegulationMonitoringRevised(nikLogin, 
				navigateComplianceOnsiteReviewRevised);
		totalFineRevised = dashboardService.searchCountDataFineRevised(nikLogin, 
				navigateFineRevised);
		
		totalCorrespondenceVerification = dashboardService.searchCountDataCorrespondenceVerification(nikLogin,
				navigateCorrespondenceVerification);
		totalRegulationSocializationVerification = dashboardService.searchCountDataRegulationSocializationVerification(
				nikLogin, navigateSocializationRegulationVerification);
		totalAuditVerification = dashboardService.searchCountDataAuditVerification(nikLogin, 
				navigateAuditVerification);
		totalComplianceOnsiteReviewVerification = dashboardService.searchCountDataComplianceOnsiteReviewVerification(nikLogin, 
				navigateComplianceOnsiteReviewVerification);
		totalCorrespondenceAmlVerification = dashboardService.searchCountDataCorrespondenceAmlRevised(nikLogin, 
				navigateCorrespondenceAmlVerification);
		totalCpsaVerification = dashboardService.searchCountDataCpsaVerification(nikLogin, navigateCpsaVerification);
		
		totalRegulationMonitoringVerification = dashboardService.searchCountDataRegulationMonitoringVerification(nikLogin, 
				navigateRegulationMonitoringVerification);
		totalFineVerification = dashboardService.searchCountDataFineVerification(nikLogin, 
				navigateFineVerification);
		
		totalQAAdmin = dashboardService.searchCountDataQAAdmin(nikLogin, navigateQAAdmin);
		totalQAPIC = dashboardService.searchCountDataQAPIC(nikLogin, navigateQAPIC);
		totalQAView = dashboardService.searchCountDataQAView(nikLogin, navigateQAView);
		
		totalFAQApproval = dashboardService.searchCountDataFAQApproval(nikLogin, navigateFAQApproval);
		
		String token = facesUtil.retrieveRequestParam("token");
		
		if(token!=null){
			ParameterDetail pdHostName = null;
			try {
				pdHostName = parameterDetailService.getParameterDetailByParamDtlCode(Constants.HOST_NAME_APPLICATION);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			String menuId = facesUtil.retrieveRequestParam("menuId");
			menuId = Constants.decryptString(menuId);
			String script = "window.location.href='";
			if(menuId.equals(Constants.MENU_ID_APPROVAL_REQUEST_INTERNAL_REGULATION)) {
				script = script +pdHostName.getNameIn()+"pages/internalRegulationApproval/internalRegulationApprovalEdit.faces?token="+token;
			}else if(menuId.equals(Constants.MENU_ID_APPROVAL_REQUEST_EXTERNAL_REGULATION)){
				script = script +pdHostName.getNameIn()+"pages/externalRegulationApproval/externalRegulationApprovalEdit.faces?token="+token;
			}else if(menuId.equals(Constants.MENU_ID_APPROVAL_REQUEST_ARTICLE)){
				script = script +pdHostName.getNameIn()+"pages/articleApproval/articleApprovalEdit.faces?token="+token;
			}else if(menuId.equals(Constants.MENU_ID_APPROVAL_REQUEST_AUDIT)){
				script = script +pdHostName.getNameIn()+"pages/tmpAuditApproval/tmpAuditApprovalEdit.faces?token="+token;
			}
			System.out.println("script=="+script);
			PrimeFaces.current().executeScript(script+"'");
			
		}
	}

	public FacesUtil getFacesUtil() {
		return facesUtil;
	}

	public void setFacesUtil(FacesUtil facesUtil) {
		this.facesUtil = facesUtil;
	}

	public DashboardService getDashboardService() {
		return dashboardService;
	}

	public void setDashboardService(DashboardService dashboardService) {
		this.dashboardService = dashboardService;
	}

	public Long getTotalExternalRegulationApproval() {
		return totalExternalRegulationApproval;
	}

	public void setTotalExternalRegulationApproval(Long totalExternalRegulationApproval) {
		this.totalExternalRegulationApproval = totalExternalRegulationApproval;
	}

	public Long getTotalInternalRegulationApproval() {
		return totalInternalRegulationApproval;
	}

	public void setTotalInternalRegulationApproval(Long totalInternalRegulationApproval) {
		this.totalInternalRegulationApproval = totalInternalRegulationApproval;
	}

	public Long getTotalReportMatrixDiaryApproval() {
		return totalReportMatrixDiaryApproval;
	}

	public void setTotalReportMatrixDiaryApproval(Long totalReportMatrixDiaryApproval) {
		this.totalReportMatrixDiaryApproval = totalReportMatrixDiaryApproval;
	}

	public Long getTotalCorrespondenceApproval() {
		return totalCorrespondenceApproval;
	}

	public void setTotalCorrespondenceApproval(Long totalCorrespondenceApproval) {
		this.totalCorrespondenceApproval = totalCorrespondenceApproval;
	}

	public Long getTotalRegulationSocializationApproval() {
		return totalRegulationSocializationApproval;
	}

	public void setTotalRegulationSocializationApproval(Long totalRegulationSocializationApproval) {
		this.totalRegulationSocializationApproval = totalRegulationSocializationApproval;
	}

	public Long getTotalExternalRegulationRevised() {
		return totalExternalRegulationRevised;
	}

	public void setTotalExternalRegulationRevised(Long totalExternalRegulationRevised) {
		this.totalExternalRegulationRevised = totalExternalRegulationRevised;
	}

	public Long getTotalInternalRegulationRevised() {
		return totalInternalRegulationRevised;
	}

	public void setTotalInternalRegulationRevised(Long totalInternalRegulationRevised) {
		this.totalInternalRegulationRevised = totalInternalRegulationRevised;
	}

	public Long getTotalReportMatrixDiaryRevised() {
		return totalReportMatrixDiaryRevised;
	}

	public void setTotalReportMatrixDiaryRevised(Long totalReportMatrixDiaryRevised) {
		this.totalReportMatrixDiaryRevised = totalReportMatrixDiaryRevised;
	}

	public Long getTotalCorrespondenceRevised() {
		return totalCorrespondenceRevised;
	}

	public void setTotalCorrespondenceRevised(Long totalCorrespondenceRevised) {
		this.totalCorrespondenceRevised = totalCorrespondenceRevised;
	}

	public Long getTotalRegulationSocializationRevised() {
		return totalRegulationSocializationRevised;
	}

	public void setTotalRegulationSocializationRevised(Long totalRegulationSocializationRevised) {
		this.totalRegulationSocializationRevised = totalRegulationSocializationRevised;
	}

	public String getNavigateExternalRegulationApproval() {
		return navigateExternalRegulationApproval;
	}

	public void setNavigateExternalRegulationApproval(String navigateExternalRegulationApproval) {
		this.navigateExternalRegulationApproval = navigateExternalRegulationApproval;
	}

	public String getNavigateInternalRegulationApproval() {
		return navigateInternalRegulationApproval;
	}

	public void setNavigateInternalRegulationApproval(String navigateInternalRegulationApproval) {
		this.navigateInternalRegulationApproval = navigateInternalRegulationApproval;
	}

	public String getNavigateRmdApproval() {
		return navigateRmdApproval;
	}

	public void setNavigateRmdApproval(String navigateRmdApproval) {
		this.navigateRmdApproval = navigateRmdApproval;
	}

	public String getNavigateCorrespondenceApproval() {
		return navigateCorrespondenceApproval;
	}

	public void setNavigateCorrespondenceApproval(String navigateCorrespondenceApproval) {
		this.navigateCorrespondenceApproval = navigateCorrespondenceApproval;
	}

	public String getNavigateSocializationRegulationApproval() {
		return navigateSocializationRegulationApproval;
	}

	public void setNavigateSocializationRegulationApproval(String navigateSocializationRegulationApproval) {
		this.navigateSocializationRegulationApproval = navigateSocializationRegulationApproval;
	}

	public Long getTotalCorrespondenceVerification() {
		return totalCorrespondenceVerification;
	}

	public void setTotalCorrespondenceVerification(Long totalCorrespondenceVerification) {
		this.totalCorrespondenceVerification = totalCorrespondenceVerification;
	}

	public Long getTotalRegulationSocializationVerification() {
		return totalRegulationSocializationVerification;
	}

	public void setTotalRegulationSocializationVerification(Long totalRegulationSocializationVerification) {
		this.totalRegulationSocializationVerification = totalRegulationSocializationVerification;
	}

	public String getNavigateExternalRegulationRevised() {
		return navigateExternalRegulationRevised;
	}

	public void setNavigateExternalRegulationRevised(String navigateExternalRegulationRevised) {
		this.navigateExternalRegulationRevised = navigateExternalRegulationRevised;
	}

	public String getNavigateInternalRegulationRevised() {
		return navigateInternalRegulationRevised;
	}

	public void setNavigateInternalRegulationRevised(String navigateInternalRegulationRevised) {
		this.navigateInternalRegulationRevised = navigateInternalRegulationRevised;
	}

	public String getNavigateRmdRevised() {
		return navigateRmdRevised;
	}

	public void setNavigateRmdRevised(String navigateRmdRevised) {
		this.navigateRmdRevised = navigateRmdRevised;
	}

	public String getNavigateCorrespondenceRevised() {
		return navigateCorrespondenceRevised;
	}

	public void setNavigateCorrespondenceRevised(String navigateCorrespondenceRevised) {
		this.navigateCorrespondenceRevised = navigateCorrespondenceRevised;
	}

	public String getNavigateSocializationRegulationRevised() {
		return navigateSocializationRegulationRevised;
	}

	public void setNavigateSocializationRegulationRevised(String navigateSocializationRegulationRevised) {
		this.navigateSocializationRegulationRevised = navigateSocializationRegulationRevised;
	}

	public String getNavigateCorrespondenceVerification() {
		return navigateCorrespondenceVerification;
	}

	public void setNavigateCorrespondenceVerification(String navigateCorrespondenceVerification) {
		this.navigateCorrespondenceVerification = navigateCorrespondenceVerification;
	}

	public String getNavigateSocializationRegulationVerification() {
		return navigateSocializationRegulationVerification;
	}

	public void setNavigateSocializationRegulationVerification(String navigateSocializationRegulationVerification) {
		this.navigateSocializationRegulationVerification = navigateSocializationRegulationVerification;
	}

	public Long getTotalAuditApproval() {
		return totalAuditApproval;
	}

	public void setTotalAuditApproval(Long totalAuditApproval) {
		this.totalAuditApproval = totalAuditApproval;
	}

	public String getNavigateAuditApproval() {
		return navigateAuditApproval;
	}

	public void setNavigateAuditApproval(String navigateAuditApproval) {
		this.navigateAuditApproval = navigateAuditApproval;
	}

	public Long getTotalComplianceOnsiteReviewApproval() {
		return totalComplianceOnsiteReviewApproval;
	}

	public void setTotalComplianceOnsiteReviewApproval(Long totalComplianceOnsiteReviewApproval) {
		this.totalComplianceOnsiteReviewApproval = totalComplianceOnsiteReviewApproval;
	}

	public String getNavigateComplianceOnsiteReviewApproval() {
		return navigateComplianceOnsiteReviewApproval;
	}

	public void setNavigateComplianceOnsiteReviewApproval(String navigateComplianceOnsiteReviewApproval) {
		this.navigateComplianceOnsiteReviewApproval = navigateComplianceOnsiteReviewApproval;
	}

	public Long getTotalAuditRevised() {
		return totalAuditRevised;
	}

	public void setTotalAuditRevised(Long totalAuditRevised) {
		this.totalAuditRevised = totalAuditRevised;
	}

	public Long getTotalComplianceOnsiteReviewRevised() {
		return totalComplianceOnsiteReviewRevised;
	}

	public void setTotalComplianceOnsiteReviewRevised(Long totalComplianceOnsiteReviewRevised) {
		this.totalComplianceOnsiteReviewRevised = totalComplianceOnsiteReviewRevised;
	}

	public String getNavigateAuditRevised() {
		return navigateAuditRevised;
	}

	public void setNavigateAuditRevised(String navigateAuditRevised) {
		this.navigateAuditRevised = navigateAuditRevised;
	}

	public String getNavigateComplianceOnsiteReviewRevised() {
		return navigateComplianceOnsiteReviewRevised;
	}

	public void setNavigateComplianceOnsiteReviewRevised(String navigateComplianceOnsiteReviewRevised) {
		this.navigateComplianceOnsiteReviewRevised = navigateComplianceOnsiteReviewRevised;
	}

	public Long getTotalAuditVerification() {
		return totalAuditVerification;
	}

	public void setTotalAuditVerification(Long totalAuditVerification) {
		this.totalAuditVerification = totalAuditVerification;
	}

	public Long getTotalComplianceOnsiteReviewVerification() {
		return totalComplianceOnsiteReviewVerification;
	}

	public void setTotalComplianceOnsiteReviewVerification(Long totalComplianceOnsiteReviewVerification) {
		this.totalComplianceOnsiteReviewVerification = totalComplianceOnsiteReviewVerification;
	}

	public String getNavigateAuditVerification() {
		return navigateAuditVerification;
	}

	public void setNavigateAuditVerification(String navigateAuditVerification) {
		this.navigateAuditVerification = navigateAuditVerification;
	}

	public String getNavigateComplianceOnsiteReviewVerification() {
		return navigateComplianceOnsiteReviewVerification;
	}

	public void setNavigateComplianceOnsiteReviewVerification(String navigateComplianceOnsiteReviewVerification) {
		this.navigateComplianceOnsiteReviewVerification = navigateComplianceOnsiteReviewVerification;
	}

	public Long getTotalCorrespondenceAmlApproval() {
		return totalCorrespondenceAmlApproval;
	}

	public void setTotalCorrespondenceAmlApproval(Long totalCorrespondenceAmlApproval) {
		this.totalCorrespondenceAmlApproval = totalCorrespondenceAmlApproval;
	}

	public Long getTotalCorrespondenceAmlRevised() {
		return totalCorrespondenceAmlRevised;
	}

	public void setTotalCorrespondenceAmlRevised(Long totalCorrespondenceAmlRevised) {
		this.totalCorrespondenceAmlRevised = totalCorrespondenceAmlRevised;
	}

	public Long getTotalCorrespondenceAmlVerification() {
		return totalCorrespondenceAmlVerification;
	}

	public void setTotalCorrespondenceAmlVerification(Long totalCorrespondenceAmlVerification) {
		this.totalCorrespondenceAmlVerification = totalCorrespondenceAmlVerification;
	}

	public String getNavigateCorrespondenceAmlApproval() {
		return navigateCorrespondenceAmlApproval;
	}

	public void setNavigateCorrespondenceAmlApproval(String navigateCorrespondenceAmlApproval) {
		this.navigateCorrespondenceAmlApproval = navigateCorrespondenceAmlApproval;
	}

	public String getNavigateCorrespondenceAmlRevised() {
		return navigateCorrespondenceAmlRevised;
	}

	public void setNavigateCorrespondenceAmlRevised(String navigateCorrespondenceAmlRevised) {
		this.navigateCorrespondenceAmlRevised = navigateCorrespondenceAmlRevised;
	}

	public String getNavigateCorrespondenceAmlVerification() {
		return navigateCorrespondenceAmlVerification;
	}

	public void setNavigateCorrespondenceAmlVerification(String navigateCorrespondenceAmlVerification) {
		this.navigateCorrespondenceAmlVerification = navigateCorrespondenceAmlVerification;
	}

	public Long getTotalQAAdmin() {
		return totalQAAdmin;
	}

	public void setTotalQAAdmin(Long totalQAAdmin) {
		this.totalQAAdmin = totalQAAdmin;
	}

	public String getNavigateQAAdmin() {
		return navigateQAAdmin;
	}

	public void setNavigateQAAdmin(String navigateQAAdmin) {
		this.navigateQAAdmin = navigateQAAdmin;
	}

	public Long getTotalQAPIC() {
		return totalQAPIC;
	}

	public void setTotalQAPIC(Long totalQAPIC) {
		this.totalQAPIC = totalQAPIC;
	}

	public Long getTotalQAView() {
		return totalQAView;
	}

	public void setTotalQAView(Long totalQAView) {
		this.totalQAView = totalQAView;
	}

	public String getNavigateQAPIC() {
		return navigateQAPIC;
	}

	public void setNavigateQAPIC(String navigateQAPIC) {
		this.navigateQAPIC = navigateQAPIC;
	}

	public String getNavigateQAView() {
		return navigateQAView;
	}

	public void setNavigateQAView(String navigateQAView) {
		this.navigateQAView = navigateQAView;
	}

	public Long getTotalArticleApproval() {
		return totalArticleApproval;
	}

	public void setTotalArticleApproval(Long totalArticleApproval) {
		this.totalArticleApproval = totalArticleApproval;
	}

	public Long getTotalRegulationMonitoringApproval() {
		return totalRegulationMonitoringApproval;
	}

	public void setTotalRegulationMonitoringApproval(Long totalRegulationMonitoringApproval) {
		this.totalRegulationMonitoringApproval = totalRegulationMonitoringApproval;
	}

	public Long getTotalFineApproval() {
		return totalFineApproval;
	}

	public void setTotalFineApproval(Long totalFineApproval) {
		this.totalFineApproval = totalFineApproval;
	}

	public Long getTotalArticleRevised() {
		return totalArticleRevised;
	}

	public void setTotalArticleRevised(Long totalArticleRevised) {
		this.totalArticleRevised = totalArticleRevised;
	}

	public Long getTotalRegulationMonitoringRevised() {
		return totalRegulationMonitoringRevised;
	}

	public void setTotalRegulationMonitoringRevised(Long totalRegulationMonitoringRevised) {
		this.totalRegulationMonitoringRevised = totalRegulationMonitoringRevised;
	}

	public Long getTotalFineRevised() {
		return totalFineRevised;
	}

	public void setTotalFineRevised(Long totalFineRevised) {
		this.totalFineRevised = totalFineRevised;
	}

	public Long getTotalArticleVerification() {
		return totalArticleVerification;
	}

	public void setTotalArticleVerification(Long totalArticleVerification) {
		this.totalArticleVerification = totalArticleVerification;
	}

	public Long getTotalRegulationMonitoringVerification() {
		return totalRegulationMonitoringVerification;
	}

	public void setTotalRegulationMonitoringVerification(Long totalRegulationMonitoringVerification) {
		this.totalRegulationMonitoringVerification = totalRegulationMonitoringVerification;
	}

	public Long getTotalFineVerification() {
		return totalFineVerification;
	}

	public void setTotalFineVerification(Long totalFineVerification) {
		this.totalFineVerification = totalFineVerification;
	}

	public String getNavigateArticleApproval() {
		return navigateArticleApproval;
	}

	public void setNavigateArticleApproval(String navigateArticleApproval) {
		this.navigateArticleApproval = navigateArticleApproval;
	}

	public String getNavigateRegulationMonitoringApproval() {
		return navigateRegulationMonitoringApproval;
	}

	public void setNavigateRegulationMonitoringApproval(String navigateRegulationMonitoringApproval) {
		this.navigateRegulationMonitoringApproval = navigateRegulationMonitoringApproval;
	}

	public String getNavigateFineApproval() {
		return navigateFineApproval;
	}

	public void setNavigateFineApproval(String navigateFineApproval) {
		this.navigateFineApproval = navigateFineApproval;
	}

	public String getNavigateArticleRevised() {
		return navigateArticleRevised;
	}

	public void setNavigateArticleRevised(String navigateArticleRevised) {
		this.navigateArticleRevised = navigateArticleRevised;
	}

	public String getNavigateRegulationMonitoringRevised() {
		return navigateRegulationMonitoringRevised;
	}

	public void setNavigateRegulationMonitoringRevised(String navigateRegulationMonitoringRevised) {
		this.navigateRegulationMonitoringRevised = navigateRegulationMonitoringRevised;
	}

	public String getNavigateFineRevised() {
		return navigateFineRevised;
	}

	public void setNavigateFineRevised(String navigateFineRevised) {
		this.navigateFineRevised = navigateFineRevised;
	}

	public String getNavigateRegulationMonitoringVerification() {
		return navigateRegulationMonitoringVerification;
	}

	public void setNavigateRegulationMonitoringVerification(String navigateRegulationMonitoringVerification) {
		this.navigateRegulationMonitoringVerification = navigateRegulationMonitoringVerification;
	}

	public String getNavigateFineVerification() {
		return navigateFineVerification;
	}

	public void setNavigateFineVerification(String navigateFineVerification) {
		this.navigateFineVerification = navigateFineVerification;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public ParameterDetailService getParameterDetailService() {
		return parameterDetailService;
	}

	public void setParameterDetailService(ParameterDetailService parameterDetailService) {
		this.parameterDetailService = parameterDetailService;
	}

	public Long getTotalFAQApproval() {
		return totalFAQApproval;
	}

	public void setTotalFAQApproval(Long totalFAQApproval) {
		this.totalFAQApproval = totalFAQApproval;
	}

	public String getNavigateFAQApproval() {
		return navigateFAQApproval;
	}

	public void setNavigateFAQApproval(String navigateFAQApproval) {
		this.navigateFAQApproval = navigateFAQApproval;
	}

	public Long getTotalCpsaVerification() {
		return totalCpsaVerification;
	}

	public void setTotalCpsaVerification(Long totalCpsaVerification) {
		this.totalCpsaVerification = totalCpsaVerification;
	}

	public String getNavigateCpsaVerification() {
		return navigateCpsaVerification;
	}

	public void setNavigateCpsaVerification(String navigateCpsaVerification) {
		this.navigateCpsaVerification = navigateCpsaVerification;
	}
	
	
}