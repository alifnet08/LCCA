package com.wo.module.engine.dao;

import java.util.Date;
import java.util.List;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.engine.vo.SendEmailIRGObsoleteVO;
import com.wo.module.complianceTestingMockup.model.ComplianceTestingPICFollowupEmail;
import com.wo.module.cpsa.vo.CompliancePlanSelfAssessmentPicEmailVo;
import com.wo.module.engine.vo.SendEmailVO;
import com.wo.module.internalRegulationPenerbitan.vo.InternalRegulationPenerbitanPicTpkVo;
import com.wo.module.log.model.LogHeader;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.qa.model.QA;
import com.wo.module.regulationMonitoring.model.RegMonitoringPICFollowUpEmailTrc;
import com.wo.module.regulationSocialization.model.SocializationPICFollowupEmailTrc;
import com.wo.module.trcAudit.model.TrcAuditPicFollowupEmail;
import com.wo.module.trcComplianceReview.model.TrcComplianceReviewPicFollowupEmail;
import com.wo.module.trcCorrespondence.model.TrcCorrespondencePicFollowupEmail;
import com.wo.module.trcFineApproval.model.TrcFinePicFollowupEmail;
import com.wo.module.trcRmd.model.TrcRmdPicFollowup;
import com.wo.module.trcRmd.model.TrcRmdPicFollowupEmail;

public interface SendEmailDao extends GenericDAO<LogHeader, Long> {

	public List<SendEmailVO> getListDataSocialization() throws Exception;
	public List<SendEmailVO> getListDataRegulationMonitoring() throws Exception;
	public List<SendEmailVO> getListDataDenda() throws Exception;
	public List<SendEmailVO> getListDataCorrespondence() throws Exception;
	public List<SendEmailVO> getListDataRMD() throws Exception;
	public List<SendEmailVO> getListDataComplianceReview() throws Exception;
	public List<SendEmailVO> getListDataAudit() throws Exception;
	public String getEmailSubject(String templateCode);
	public String getEmailContent(String templateCode);
	public SocializationPICFollowupEmailTrc findEmailSocializationById(Long id);
	public void updateEmailSocialization(SocializationPICFollowupEmailTrc entity);
	public TrcCorrespondencePicFollowupEmail findEmailCorrespondenceById(Long id);
	public void updateEmailCorrespondence(TrcCorrespondencePicFollowupEmail entity);
	public TrcRmdPicFollowupEmail findEmailRmdById(Long id);
	public void updateEmailRmd(TrcRmdPicFollowupEmail entity);
	public ComplianceTestingPICFollowupEmail findEmailComplianceReviewById(Long id);
	public void updateEmailComplianceReview(ComplianceTestingPICFollowupEmail entity);
	public TrcAuditPicFollowupEmail findEmailAuditById(Long id);
	public void updateEmailAudit(TrcAuditPicFollowupEmail entity);
	public RegMonitoringPICFollowUpEmailTrc findEmailRegMonitoringById(Long id);
	public void updateEmailRegMonitoring(RegMonitoringPICFollowUpEmailTrc entity);
	public TrcFinePicFollowupEmail findEmailDendaById(Long id);
	public void updateEmailDenda(TrcFinePicFollowupEmail entity);
	public String procedureUpdateEmailDate() throws Exception;
	public List<QA> getListDataQAPIC();
	public List<QA> getListDataQAClose();
	public List<SendEmailVO> getListDataExpiredPeraturanInternal();
	public List<SendEmailVO> getListEmailAdminByQnaCategory(String qaCategory);
	public List<SendEmailIRGObsoleteVO> getListDataIRGObsolete();
	public List<InternalRegulationPenerbitanPicTpkVo> getListDataIrgPenerbitanPIC();
	public List<CompliancePlanSelfAssessmentPicEmailVo> getListDataCpsa();
	public ParameterDetail getDataParameter(String parameterDtlCode);
	public void updateFollowupRmd(TrcRmdPicFollowup entity);
	public Boolean isAvailableDate(Date date);
}
