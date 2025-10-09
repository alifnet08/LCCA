package com.wo.module.cpsaApprovalFE.constant;

public interface CpsaApprovalFEConstant {

	public static final String NAVIGATE_CPSA_APPROVAL = "cpsaApprovalFE.faces";
	
	public static final String NAVIGATE_CPSA_APPROVAL_EDIT = "cpsaApprovalFEEdit.faces?";
	public static final String NAVIGATE_CPSA_APPROVAL_EDIT_UNUSED = "/pages/cpsaApprovalFE/cpsaApprovalFEEdit.faces?"
	+ "faces-redirect=true&"
	+ "first=#{internalRegulationFEBean.firstTemp}&"
	+ "token=#{internalRegulationFEBean.toEncrypt(vo.cpsaId)}";
	
	
	public static final String STRING_EMPTY = "";
	
	public static final String STATUS_CPSA_INPROGRESS = "CPSA_INPROGRESS";
	public static final String STATUS_CPSA_WAITING_APPROVAL = "CPSA_WAITING_APPROVAL";
	public static final String STATUS_CPSA_APPROVED = "CPSA_APPROVED";
	public static final String STATUS_CPSA_REJECTED = "CPSA_REJECTED";
	public static final String STATUS_CPSA_VERIFICATION = "CPSA_VERIFICATION";	
	
}
