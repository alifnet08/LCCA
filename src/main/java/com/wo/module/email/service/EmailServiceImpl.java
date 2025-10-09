package com.wo.module.email.service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.common.constant.Constants;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.common.utility.CallApiManager;
import com.wo.module.email.constant.EmailConstant;
import com.wo.module.email.dao.EmailDao;
import com.wo.module.email.vo.EmailVO;
import com.wo.module.engine.vo.SendEmailVO;
import com.wo.module.parameter.service.ParameterDetailService;
import com.wo.module.regulationSocialization.dao.SocializationPICFollowupEmailTrcDao;
import com.wo.module.regulationSocialization.model.SocializationPICFollowupEmailTrc;
import com.wo.module.trcAudit.dao.TrcAuditPICFollowupEmailDao;
import com.wo.module.trcAudit.model.TrcAuditPicFollowupEmail;
import com.wo.module.trcComplianceReview.dao.TrcComplianceReviewPicFollowupEmailDao;
import com.wo.module.trcComplianceReview.model.TrcComplianceReviewPicFollowupEmail;
import com.wo.module.trcCorrespondence.dao.TrcCorrespondencePicFollowupEmailDao;
import com.wo.module.trcCorrespondence.model.TrcCorrespondencePicFollowupEmail;
import com.wo.module.trcRmd.dao.TrcRmdPicFollowupEmailDao;
import com.wo.module.trcRmd.model.TrcRmdPicFollowupEmail;

@Transactional
@Service("emailService")
public class EmailServiceImpl implements EmailService, EmailConstant {

	@Autowired
	@Qualifier("emailDao")
	private EmailDao emailDao;
	
	@Autowired
	@Qualifier("socializationPICFollowupEmailTrcDao")
	private SocializationPICFollowupEmailTrcDao socializationPICFollowupEmailTrcDao;
	
	@Autowired
	@Qualifier("trcAuditPICFollowupEmailDao")
	private TrcAuditPICFollowupEmailDao trcAuditPICFollowupEmailDao;
	
	@Autowired
	@Qualifier("trcComplianceReviewPicFollowupEmailDao")
	private TrcComplianceReviewPicFollowupEmailDao trcComplianceReviewPicFollowupEmailDao;
	
	@Autowired
	@Qualifier("trcCorrespondencePicFollowupEmailDao")
	private TrcCorrespondencePicFollowupEmailDao trcCorrespondencePicFollowupEmailDao;
	
	@Autowired
	@Qualifier("trcRmdPicFollowupEmailDao")
	private TrcRmdPicFollowupEmailDao trcRmdPicFollowupEmailDao;
	
	@Autowired
	@Qualifier("parameterDetailService")
	private ParameterDetailService parameterDetailService;

	@SuppressWarnings("rawtypes")
	@Override
	public List<EmailVO> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize,
			String sortField, SortOrder sortOrder) throws Exception {
		return emailDao.searchData(searchCriteria, first, pageSize, sortField, sortOrder);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception {
		return emailDao.searchCountData(searchCriteria);
	}
	
	@Override
	public void updateEmailFollowupResend(Long emailFollowId, String emailType, String user) throws Exception {
		
		if (StringUtils.isNotBlank(emailType)) {
			switch (emailType) {
			case EMAIL_TYPE_AUDIT:
				updateAuditEmailResend(emailFollowId, user);
				break;
			case EMAIL_TYPE_COMPLIANCE:
				updateComplianceEmailResend(emailFollowId, user);
				break;
			case EMAIL_TYPE_CORRESPONDENCE:
				updateCorrespondenceEmailResend(emailFollowId, user);
				break;
			case EMAIL_TYPE_RMD:
				updateRmdEmailResend(emailFollowId, user);
				break;
			case EMAIL_TYPE_SOCIALIZATION:
				updateSocializationEmailResend(emailFollowId, user);
				break;
			default:
				break;
			}
		}
	}
	
	@Override
	public void updateEmailFollowup(EmailVO email) throws Exception {
		
		if (email != null) {
			switch (email.getEmailType()) {
			case EMAIL_TYPE_AUDIT:
				updateAuditEmail(email);
				break;
			case EMAIL_TYPE_COMPLIANCE:
				updateComplianceEmail(email);
				break;
			case EMAIL_TYPE_CORRESPONDENCE:
				updateCorrespondenceEmail(email);
				break;
			case EMAIL_TYPE_RMD:
				updateRmdEmail(email);
				break;
			case EMAIL_TYPE_SOCIALIZATION:
				updateSocializationEmail(email);
				break;
			default:
				break;
			}
		}
	}
	
	private void updateAuditEmailResend(Long emailFollowId, String user) throws Exception{
		TrcAuditPicFollowupEmail entity = trcAuditPICFollowupEmailDao.findById(emailFollowId);
		
		int resendCount = entity.getResendCount() == null ? 0 : entity.getResendCount() + 1;
		entity.setResendCount(resendCount);
		
		entity.setLastUpdateBy(user);
		entity.setLastUpdateDate(new Timestamp(new Date().getTime()));
		
		SendEmailVO vo = trcAuditPICFollowupEmailDao.getEmailPicByFollowupEmailId(emailFollowId);
		try {
			sendEmail(emailFollowId, entity.getEmailSubject(), entity.getEmailContent(), vo, "EMAIL_AUDIT");
			entity.setEmailStatus(EMAIL_STATUS_SUCCESS);
		} catch (Exception e) {
			entity.setEmailStatus(EMAIL_STATUS_ERROR);
			e.printStackTrace();
		}
		
		trcAuditPICFollowupEmailDao.update(entity);
	}

	private void sendEmail(Long emailFollowId, String subject, String content, SendEmailVO vo, String refNo) throws Exception {
		String emailTo  = "";
		String emailCc1 = "";
		String emailCc2 = "";
		String emailCc = "";
		String emailCcCompliance = "";
		
		emailTo = vo.getEmailTo();
		emailCc1 = vo.getEmailCc1();
		emailCc2 = vo.getEmailCc2();
		emailCcCompliance = vo.getEmailCcCompliance();
		
		if(emailTo.equals(Constants.REMINDER_PIC1)) {
			emailTo = vo.getPic1();
		}
		else if(emailTo.equals(Constants.REMINDER_PIC2)) {
			emailTo = vo.getPic2();
		}
		else if(emailTo.equals(Constants.REMINDER_PIC3)) {
			emailTo = vo.getPic3();
		}
		
		if(emailCc1!=null && emailCc1.equals(Constants.REMINDER_PIC1)) {
			emailCc1 = vo.getPic1();
		}
		else if(emailCc1!=null && emailCc1.equals(Constants.REMINDER_PIC2)) {
			emailCc1 = vo.getPic2();
		}
		else if(emailCc1!=null && emailCc1.equals(Constants.REMINDER_PIC3)) {
			emailCc1 = vo.getPic3();
		}
		
		if(emailCc2!=null && emailCc2.equals(Constants.REMINDER_PIC1)) {
			emailCc2 = vo.getPic1();
		}
		else if(emailCc2!=null && emailCc2.equals(Constants.REMINDER_PIC2)) {
			emailCc2 = vo.getPic2();
		}
		else if(emailCc2!=null && emailCc2.equals(Constants.REMINDER_PIC3)) {
			emailCc2 = vo.getPic3();
		}
		
		if(StringUtils.isNotEmpty(emailCc1)) {
			emailCc = emailCc.concat(emailCc1);
		}
		if(StringUtils.isNotEmpty(emailCc2)) {
			emailCc = StringUtils.isNotEmpty(emailCc)?emailCc.concat(",").concat(emailCc2):emailCc.concat(emailCc2);
		}
		if(StringUtils.isNotEmpty(emailCcCompliance)) {
			emailCc = StringUtils.isNotEmpty(emailCc)?emailCc.concat(",").concat(emailCcCompliance):emailCc.concat(emailCcCompliance);
		}
		
		CallApiManager.sendEmailAPI(
				emailTo, 
				emailCc, 
				subject, 
				content, 
				refNo, 
				"true", 
				parameterDetailService);
	}
	
	private void updateComplianceEmailResend(Long emailFollowId, String user) throws Exception{
		TrcComplianceReviewPicFollowupEmail entity = trcComplianceReviewPicFollowupEmailDao.findById(emailFollowId);
				
		int resendCount = entity.getResendCount() == null ? 1 : entity.getResendCount() + 1;
		entity.setResendCount(resendCount);
		
		entity.setLastUpdateBy(user);
		entity.setLastUpdateDate(new Timestamp(new Date().getTime()));
		
		SendEmailVO vo = trcComplianceReviewPicFollowupEmailDao.getEmailPicByFollowupEmailId(emailFollowId);
		
		try {
			sendEmail(emailFollowId, entity.getEmailSubject(), entity.getEmailContent(), vo, "EMAIL_COMPLIANCE_ASSESSMENT");
			entity.setEmailStatus(EMAIL_STATUS_SUCCESS);
		} catch (Exception e) {
			entity.setEmailStatus(EMAIL_STATUS_ERROR);
			e.printStackTrace();
		}
		
		trcComplianceReviewPicFollowupEmailDao.update(entity);
	}
	
	private void updateCorrespondenceEmailResend(Long emailFollowId, String user) throws Exception{
		TrcCorrespondencePicFollowupEmail entity = trcCorrespondencePicFollowupEmailDao.findById(emailFollowId);
		
		int resendCount = entity.getResendCount() == null ? 1 : entity.getResendCount() + 1;
		entity.setResendCount(resendCount);
		
		entity.setLastUpdateBy(user);
		entity.setLastUpdateDate(new Timestamp(new Date().getTime()));
		
		SendEmailVO vo = trcCorrespondencePicFollowupEmailDao.getEmailPicByFollowupEmailId(emailFollowId);
		
		try {
			sendEmail(emailFollowId, entity.getEmailSubject(), entity.getEmailContent(), vo, "EMAIL_KORESPONDENSI");
			entity.setEmailStatus(EMAIL_STATUS_SUCCESS);
		} catch (Exception e) {
			entity.setEmailStatus(EMAIL_STATUS_ERROR);
			e.printStackTrace();
		}
		
		trcCorrespondencePicFollowupEmailDao.update(entity);
	}
	
	private void updateRmdEmailResend(Long emailFollowId, String user) throws Exception{
		TrcRmdPicFollowupEmail entity = trcRmdPicFollowupEmailDao.findById(emailFollowId);
				
		Long resendCount = Long.valueOf(entity.getResendCount() == null ? 1 : entity.getResendCount() + 1);
		entity.setResendCount(resendCount);
		
		entity.setLastUpdateBy(user);
		entity.setLastUpdateDate(new Timestamp(new Date().getTime()));
		
		SendEmailVO vo = trcRmdPicFollowupEmailDao.getEmailPicByFollowupEmailId(emailFollowId);
		try {
			sendEmail(emailFollowId, entity.getEmailSubject(), entity.getEmailContent(), vo, "EMAIL_RMD");
			entity.setEmailStatus(EMAIL_STATUS_SUCCESS);
		} catch (Exception e) {
			entity.setEmailStatus(EMAIL_STATUS_ERROR);
			e.printStackTrace();
		}
		
		trcRmdPicFollowupEmailDao.update(entity);
	}

	private void updateSocializationEmailResend(Long emailFollowId, String user) throws Exception{
		SocializationPICFollowupEmailTrc entity = socializationPICFollowupEmailTrcDao.findById(emailFollowId);
		
		int resendCount = entity.getResendCount() == null ? 1 : entity.getResendCount() + 1;
		entity.setResendCount(resendCount);
		
		entity.setLastUpdateBy(user);
		entity.setLastUpdateDate(new Timestamp(new Date().getTime()));
		
		SendEmailVO vo = socializationPICFollowupEmailTrcDao.getEmailPicByFollowupEmailId(emailFollowId);
		try {
			sendEmail(emailFollowId, entity.getEmailSubject(), entity.getEmailContent(), vo, "EMAIL_SOSIALISASI");
			entity.setEmailStatus(EMAIL_STATUS_SUCCESS);
		} catch (Exception e) {
			entity.setEmailStatus(EMAIL_STATUS_ERROR);
			e.printStackTrace();
		}
		socializationPICFollowupEmailTrcDao.update(entity);
	}
	
	private void updateAuditEmail(EmailVO email) throws Exception{
		TrcAuditPicFollowupEmail entity = trcAuditPICFollowupEmailDao.findById(email.getEmailId());
		entity.setEmailStatus(email.getEmailStatus());
		entity.setEmailSubject(email.getEmailSubject());
		entity.setEmailContent(email.getEmailContent());
	
		trcAuditPICFollowupEmailDao.update(entity);
	}
	
	private void updateComplianceEmail(EmailVO email) throws Exception{
		TrcComplianceReviewPicFollowupEmail entity = trcComplianceReviewPicFollowupEmailDao.findById(email.getEmailId());
		entity.setEmailStatus(email.getEmailStatus());
		entity.setEmailSubject(email.getEmailSubject());
		entity.setEmailContent(email.getEmailContent());
				
		trcComplianceReviewPicFollowupEmailDao.update(entity);
	}
	
	private void updateCorrespondenceEmail(EmailVO email) throws Exception{
		TrcCorrespondencePicFollowupEmail entity = trcCorrespondencePicFollowupEmailDao.findById(email.getEmailId());
		entity.setEmailStatus(email.getEmailStatus());
		entity.setEmailSubject(email.getEmailSubject());
		entity.setEmailContent(email.getEmailContent());
				
		trcCorrespondencePicFollowupEmailDao.update(entity);
	}
	
	private void updateRmdEmail(EmailVO email) throws Exception{
		TrcRmdPicFollowupEmail entity = trcRmdPicFollowupEmailDao.findById(email.getEmailId());
		entity.setEmailStatus(email.getEmailStatus());
		entity.setEmailSubject(email.getEmailSubject());
		entity.setEmailContent(email.getEmailContent());
				
		trcRmdPicFollowupEmailDao.update(entity);
	}

	private void updateSocializationEmail(EmailVO email) throws Exception{
		SocializationPICFollowupEmailTrc entity = socializationPICFollowupEmailTrcDao.findById(email.getEmailId());
		entity.setEmailStatus(email.getEmailStatus());
		entity.setEmailSubject(email.getEmailSubject());
		entity.setEmailContent(email.getEmailContent());
		
		socializationPICFollowupEmailTrcDao.update(entity);
	}
	
	public EmailDao getEmailDao() {
		return emailDao;
	}

	public void setEmailDao(EmailDao emailDao) {
		this.emailDao = emailDao;
	}

	public SocializationPICFollowupEmailTrcDao getSocializationPICFollowupEmailTrcDao() {
		return socializationPICFollowupEmailTrcDao;
	}

	public void setSocializationPICFollowupEmailTrcDao(SocializationPICFollowupEmailTrcDao socializationPICFollowupEmailTrcDao) {
		this.socializationPICFollowupEmailTrcDao = socializationPICFollowupEmailTrcDao;
	}

	public TrcAuditPICFollowupEmailDao getTrcAuditPICFollowupEmailDao() {
		return trcAuditPICFollowupEmailDao;
	}

	public void setTrcAuditPICFollowupEmailDao(TrcAuditPICFollowupEmailDao trcAuditPICFollowupEmailDao) {
		this.trcAuditPICFollowupEmailDao = trcAuditPICFollowupEmailDao;
	}

	public TrcComplianceReviewPicFollowupEmailDao getTrcComplianceReviewPicFollowupEmailDao() {
		return trcComplianceReviewPicFollowupEmailDao;
	}

	public void setTrcComplianceReviewPicFollowupEmailDao(
			TrcComplianceReviewPicFollowupEmailDao trcComplianceReviewPicFollowupEmailDao) {
		this.trcComplianceReviewPicFollowupEmailDao = trcComplianceReviewPicFollowupEmailDao;
	}

	public TrcCorrespondencePicFollowupEmailDao getTrcCorrespondencePicFollowupEmailDao() {
		return trcCorrespondencePicFollowupEmailDao;
	}

	public void setTrcCorrespondencePicFollowupEmailDao(
			TrcCorrespondencePicFollowupEmailDao trcCorrespondencePicFollowupEmailDao) {
		this.trcCorrespondencePicFollowupEmailDao = trcCorrespondencePicFollowupEmailDao;
	}

	public TrcRmdPicFollowupEmailDao getTrcRmdPicFollowupEmailDao() {
		return trcRmdPicFollowupEmailDao;
	}

	public void setTrcRmdPicFollowupEmailDao(TrcRmdPicFollowupEmailDao trcRmdPicFollowupEmailDao) {
		this.trcRmdPicFollowupEmailDao = trcRmdPicFollowupEmailDao;
	}

	public ParameterDetailService getParameterDetailService() {
		return parameterDetailService;
	}

	public void setParameterDetailService(ParameterDetailService parameterDetailService) {
		this.parameterDetailService = parameterDetailService;
	}

	

}
