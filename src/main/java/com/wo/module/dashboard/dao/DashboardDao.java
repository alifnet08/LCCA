package com.wo.module.dashboard.dao;

public interface DashboardDao {

	public Long searchCountDataExternalRegulationWaitingApproval(String nikLogin, String action);

	public Long searchCountDataInternalRegulationWaitingApproval(String nikLogin, String action);

	public Long searchCountDataReportMatrixDiaryWaitingApproval(String nikLogin, String action);

	public Long searchCountDataCorrespondenceWaitingApproval(String nikLogin, String action);

	public Long searchCountDataRegulationSocializationWaitingApproval(String nikLogin, String action);
	
	public Long searchCountDataAuditWaitingApproval(String nikLogin, String action);
	
	public Long searchCountDataComplianceOnsiteReviewWaitingApproval(String nikLogin, String action);
	
	public Long searchCountDataCorrespondenceAmlWaitingApproval(String nikLogin, String action);
	
	
	public Long searchCountDataExternalRegulationRevised(String nikLogin, String action); 
	
	public Long searchCountDataInternalRegulationRevised(String nikLogin, String action); 
	
	public Long searchCountDataReportMatrixDiaryRevised(String nikLogin, String action); 
	
	public Long searchCountDataCorrespondenceRevised(String nikLogin, String action); 
	
	public Long searchCountDataRegulationSocializationRevised(String nikLogin, String action); 
	
	public Long searchCountDataAuditRevised(String nikLogin, String action);
	
	public Long searchCountDataComplianceOnsiteReviewRevised(String nikLogin, String action);
	
	public Long searchCountDataCorrespondenceAmlRevised(String nikLogin, String action); 
	
	
	public Long searchCountDataCorrespondenceVerification(String nikLogin, String action); 
	
	public Long searchCountDataRegulationSocializationVerification(String nikLogin, String action); 

	public Long searchCountDataAuditVerification(String nikLogin, String action);
	
	public Long searchCountDataComplianceOnsiteReviewVerification(String nikLogin, String action);
	
	public Long searchCountDataCorrespondenceAmlVerification(String nikLogin, String action);
	
	
	public Long searchCountDataQAAdmin(String nikLogin, String action);
	
	public Long searchCountDataQAPIC(String nikLogin, String action);
	
	public Long searchCountDataQAView(String nikLogin, String action);
	
	public Long searchCountDataArticleWaitingApproval(String nikLogin, String action);
	
	public Long searchCountDataArticleRevised(String nikLogin, String action);
	
	public Long searchCountDataRegulationMonitoringWaitingApproval(String nikLogin, String action);
	
	public Long searchCountDataRegulationMonitoringRevised(String nikLogin, String action);
	
	public Long searchCountDataRegulationMonitoringVerification(String nikLogin, String action);
	
	public Long searchCountDataFineWaitingApproval(String nikLogin, String action);
	
	public Long searchCountDataFineRevised(String nikLogin, String action);
	
	public Long searchCountDataFineVerification(String nikLogin, String action);
	
	public Long searchCountDataFAQApproval(String nikLogin, String action);
	
	public Long searchCountDataCpsaVerification(String nikLogin, String action);
	
}
