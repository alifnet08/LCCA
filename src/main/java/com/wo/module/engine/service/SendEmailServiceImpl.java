package com.wo.module.engine.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.complianceTestingMockup.model.ComplianceTestingPICFollowupEmail;
import com.wo.module.cpsa.vo.CompliancePlanSelfAssessmentPicEmailVo;
import com.wo.module.engine.dao.OscarJobDao;
import com.wo.module.engine.dao.SendEmailDao;
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
import com.wo.module.trcRmd.model.TrcRmdPicFollowupEmail;
import com.wo.module.trcRmd.model.TrcRmdPicFollowup;
import com.wo.module.user.dao.UserDao;

@Transactional
@Service("sendEmailService")
public class SendEmailServiceImpl implements SendEmailService {

	@Autowired
	@Qualifier("oscarJobDao")
	private OscarJobDao oscarJobDao;

	@Autowired
	@Qualifier("userDao")
	private UserDao userDao;

	@Autowired
	@Qualifier("sendEmailDao")
	private SendEmailDao sendEmailDao;

	@Override
	public String getSystemProperty(String propertyCode) throws Exception {
		return oscarJobDao.getSystemProperty(propertyCode);
	}

	public List<SendEmailVO> getListDataSocialization() throws Exception {
		return sendEmailDao.getListDataSocialization();
	}
	
	public List<SendEmailVO> getListDataRegulationMonitoring() throws Exception{
		return sendEmailDao.getListDataRegulationMonitoring();
	}
	
	public List<SendEmailVO> getListDataDenda() throws Exception{
		return sendEmailDao.getListDataDenda();
	}

	public List<SendEmailVO> getListDataCorrespondence() throws Exception {
		return sendEmailDao.getListDataCorrespondence();
	}

	public List<SendEmailVO> getListDataRMD() throws Exception {
		return sendEmailDao.getListDataRMD();
	}

	public String getEmailSubject(String templateCode) {
		return sendEmailDao.getEmailSubject(templateCode);
	}

	public String getEmailContent(String templateCode) {
		return sendEmailDao.getEmailContent(templateCode);
	}

	public SocializationPICFollowupEmailTrc findEmailSocializationById(Long id) {
		return sendEmailDao.findEmailSocializationById(id);
	}

	public void updateEmailSocialization(SocializationPICFollowupEmailTrc entity) {
		sendEmailDao.updateEmailSocialization(entity);
	}

	public TrcCorrespondencePicFollowupEmail findEmailCorrespondenceById(Long id) {
		return sendEmailDao.findEmailCorrespondenceById(id);
	}
	
	public List<QA> getListDataQAPIC(){
		return sendEmailDao.getListDataQAPIC();
	}

	public void updateEmailCorrespondence(TrcCorrespondencePicFollowupEmail entity) {
		sendEmailDao.updateEmailCorrespondence(entity);
	}

	public TrcRmdPicFollowupEmail findEmailRmdById(Long id) {
		return sendEmailDao.findEmailRmdById(id);
	}

	public void updateEmailRmd(TrcRmdPicFollowupEmail entity) {
		sendEmailDao.updateEmailRmd(entity);
	}
	
	public void updateFollowupRmd(TrcRmdPicFollowup entity) {
		sendEmailDao.updateFollowupRmd(entity);
	}
	
	public RegMonitoringPICFollowUpEmailTrc findEmailRegMonitoringById(Long id){
		return sendEmailDao.findEmailRegMonitoringById(id);
	}
	public void updateEmailRegMonitoring(RegMonitoringPICFollowUpEmailTrc entity){
		sendEmailDao.updateEmailRegMonitoring(entity);
	}
	
	public TrcFinePicFollowupEmail findEmailDendaById(Long id){
		return sendEmailDao.findEmailDendaById(id);
	}
	public void updateEmailDenda(TrcFinePicFollowupEmail entity){
		sendEmailDao.updateEmailDenda(entity);
	}

	public OscarJobDao getOscarJobDao() {
		return oscarJobDao;
	}

	public void setOscarJobDao(OscarJobDao oscarJobDao) {
		this.oscarJobDao = oscarJobDao;
	}

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public SendEmailDao getSendEmailDao() {
		return sendEmailDao;
	}

	public void setSendEmailDao(SendEmailDao sendEmailDao) {
		this.sendEmailDao = sendEmailDao;
	}

	public List<SendEmailVO> getListDataComplianceReview() throws Exception {
		return sendEmailDao.getListDataComplianceReview();
	}

	public ComplianceTestingPICFollowupEmail findEmailComplianceReviewById(Long id) {
		return sendEmailDao.findEmailComplianceReviewById(id);
	}

	

	@Override
	public List<SendEmailVO> getListDataAudit() throws Exception {
		return sendEmailDao.getListDataAudit();
	}

	@Override
	public TrcAuditPicFollowupEmail findEmailAuditById(Long id) {
		return sendEmailDao.findEmailAuditById(id);
	}

	@Override
	public void updateEmailAudit(TrcAuditPicFollowupEmail entity) {
		sendEmailDao.updateEmailAudit(entity);
	}
	
	@Override
	public String procedureUpdateEmailDate() throws Exception {
		return sendEmailDao.procedureUpdateEmailDate();
	}
	
	@Override
	public List<SendEmailVO> getListDataExpiredPeraturanInternal() throws Exception {
		return sendEmailDao.getListDataExpiredPeraturanInternal();
	}

	public List<SendEmailVO> getListEmailAdminByQnaCategory(String qaCategory){
		return sendEmailDao.getListEmailAdminByQnaCategory(qaCategory);
	}
	
	public List<QA> getListDataQAClose(){
		return sendEmailDao.getListDataQAClose();
	}

	@Override
	public List<SendEmailIRGObsoleteVO> getListDataIRGObsolete() {
		return sendEmailDao.getListDataIRGObsolete();
	}
		
	public List<InternalRegulationPenerbitanPicTpkVo> getListDataIrgPenerbitanPIC() {
		return sendEmailDao.getListDataIrgPenerbitanPIC();
	}

	@Override
	public List<CompliancePlanSelfAssessmentPicEmailVo> getListDataCpsa() {
		return sendEmailDao.getListDataCpsa();
	}

	@Override
	public ParameterDetail getDataParameter(String parameterDtlCode) {
		return sendEmailDao.getDataParameter(parameterDtlCode);

	}

	@Override
	public void updateEmailComplianceReview(ComplianceTestingPICFollowupEmail entity) {
		// TODO Auto-generated method stub
		 sendEmailDao.updateEmailComplianceReview(entity);
	}

	@Override
	public Boolean isAvailableDate(Date date) {
		return sendEmailDao.isAvailableDate(date);
	}

	@Override
	public Integer getCountDataCounterType(Long id) {
		// TODO Auto-generated method stub
		return sendEmailDao.getCountDataCounterType(id);
	}
}
