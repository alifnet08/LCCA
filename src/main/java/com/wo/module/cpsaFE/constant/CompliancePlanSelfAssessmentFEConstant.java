package com.wo.module.cpsaFE.constant;

public interface CompliancePlanSelfAssessmentFEConstant {

	public static final String NAVIGATE_CPSA = "compliancePlanSelfAssessmentFE.faces";
	
	public static final String NAVIGATE_CPSA_EDIT = "compliancePlanSelfAssessmentFEEdit.faces";
	public static final String NAVIGATE_CPSA_EDIT_UNUSED = "/pages/cpsaFE/compliancePlanSelfAssessmentFEEdit.faces?"
			+ "faces-redirect=true&"//to show full link using p:commandLink - broke when deployed on Wildfly
			+ "first=#{compliancePlanSelfAssessmentFEBean.firstTemp}&"
			+ "token=#{compliancePlanSelfAssessmentFEBean.toEncrypt(vo.cpsaId)}";
	
	public static final String STRING_EMPTY = "";
	
	public static final String STATUS_CPSA_INPROGRESS = "CPSA_INPROGRESS";
	public static final String STATUS_CPSA_WAITING_APPROVAL = "CPSA_WAITING_APPROVAL";
	public static final String STATUS_CPSA_APPROVED = "CPSA_APPROVED";
	public static final String STATUS_CPSA_REJECTED = "CPSA_REJECTED";
	public static final String STATUS_CPSA_VERIFICATION = "CPSA_VERIFICATION";
	
	public static final String STATUS_COMPLIANCE_OPEN = "COMPLIANCE_OPEN";
	
	public static final String FLAG_HEADER_Y = "Y";
	public static final String FLAG_HEADER_N = "N";
		
	public static final String ANSWER_COMPLIANT = "COMPLIANT"; 
	
	public static final String FILE_NAME_CPSA = "CPSA";
	
}
