package com.wo.module.engine.service;

import java.util.Date;
import java.util.List;

import com.wo.module.complianceTestingMockup.model.ComplianceTestingPICFollowupEmail;
import com.wo.module.cpsa.vo.CompliancePlanSelfAssessmentPicEmailVo;
import com.wo.module.engine.vo.SendEmailIRGObsoleteVO;
import com.wo.module.engine.vo.SendEmailVO;
import com.wo.module.internalRegulationPenerbitan.vo.InternalRegulationPenerbitanPicTpkVo;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.qa.model.QA;
import com.wo.module.regulationMonitoring.model.RegMonitoringPICFollowUpEmailTrc;
import com.wo.module.regulationSocialization.model.SocializationPICFollowupEmailTrc;
import com.wo.module.trcAudit.model.TrcAuditPicFollowupEmail;
import com.wo.module.trcCorrespondence.model.TrcCorrespondencePicFollowupEmail;
import com.wo.module.trcFineApproval.model.TrcFinePicFollowupEmail;
import com.wo.module.trcRmd.model.TrcRmdPicFollowup;
import com.wo.module.trcRmd.model.TrcRmdPicFollowupEmail;

public interface SendEmailService {
	
	String getSystemProperty(String propertyCode) throws Exception;
	List<SendEmailVO> getListDataSocialization() throws Exception;
	List<SendEmailVO> getListDataRegulationMonitoring() throws Exception;
	List<SendEmailVO> getListDataDenda() throws Exception;
	List<SendEmailVO> getListDataCorrespondence() throws Exception;
	List<SendEmailVO> getListDataRMD() throws Exception;
	List<SendEmailVO> getListDataComplianceReview() throws Exception;
	List<SendEmailVO> getListDataAudit() throws Exception;
	 String getEmailSubject(String templateCode);
	 String getEmailContent(String templateCode);
	 SocializationPICFollowupEmailTrc findEmailSocializationById(Long id);
     void updateEmailSocialization(SocializationPICFollowupEmailTrc entity);
     TrcCorrespondencePicFollowupEmail findEmailCorrespondenceById(Long id);
 	 void updateEmailCorrespondence(TrcCorrespondencePicFollowupEmail entity);
 	 TrcRmdPicFollowupEmail findEmailRmdById(Long id);
 	 void updateEmailRmd(TrcRmdPicFollowupEmail entity);
 	ComplianceTestingPICFollowupEmail findEmailComplianceReviewById(Long id);
 	 void updateEmailComplianceReview(ComplianceTestingPICFollowupEmail entity);
 	TrcAuditPicFollowupEmail findEmailAuditById(Long id);
 	void updateEmailAudit(TrcAuditPicFollowupEmail entity);
 	RegMonitoringPICFollowUpEmailTrc findEmailRegMonitoringById(Long id);
	void updateEmailRegMonitoring(RegMonitoringPICFollowUpEmailTrc entity);
	TrcFinePicFollowupEmail findEmailDendaById(Long id);
	void updateEmailDenda(TrcFinePicFollowupEmail entity);
 	String procedureUpdateEmailDate() throws Exception;
 	List<QA> getListDataQAPIC();
 	List<SendEmailVO> getListDataExpiredPeraturanInternal() throws Exception;
 	List<SendEmailVO> getListEmailAdminByQnaCategory(String qaCategory);
 	List<QA> getListDataQAClose();
	List<SendEmailIRGObsoleteVO> getListDataIRGObsolete();
 	List<InternalRegulationPenerbitanPicTpkVo> getListDataIrgPenerbitanPIC();
 	List<CompliancePlanSelfAssessmentPicEmailVo> getListDataCpsa();
 	ParameterDetail getDataParameter(String parameterDtlCode);
	void updateFollowupRmd(TrcRmdPicFollowup entity);
	Boolean isAvailableDate(Date date);
	Integer getCountDataCounterType(Long id);
}