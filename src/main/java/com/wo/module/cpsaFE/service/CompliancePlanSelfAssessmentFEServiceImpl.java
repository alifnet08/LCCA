package com.wo.module.cpsaFE.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFDataValidation;
import org.apache.poi.xssf.usermodel.XSSFDataValidationHelper;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.SortOrder;
import org.primefaces.model.StreamedContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.common.constant.CommonConstants;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.exception.CustomAPIException;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.common.util.FileUtil;
import com.wo.module.common.utility.CallApiManager;
import com.wo.module.cpsa.constant.CompliancePlanSelfAssessmentConstant;
import com.wo.module.cpsa.dao.CompliancePlanSelfAssessmentPicDao;
import com.wo.module.cpsa.model.CompliancePlanSelfAssessment;
import com.wo.module.cpsa.model.CompliancePlanSelfAssessmentAnswer;
import com.wo.module.cpsa.model.CompliancePlanSelfAssessmentPic;
import com.wo.module.cpsa.model.CompliancePlanSelfAssessmentQuestion;
import com.wo.module.cpsaFE.constant.CompliancePlanSelfAssessmentFEConstant;
import com.wo.module.cpsaFE.dao.CompliancePlanSelfAssessmentFEDao;
import com.wo.module.cpsaFE.vo.CompliancePlanSelfAssessmentFEVo;
import com.wo.module.cpsaFE.vo.CompliancePlanSelfAssessmentQuestionFEVo;
import com.wo.module.cpsaPicLockHistory.dao.CpsaPicLockHistoryDao;
import com.wo.module.cpsaPicLockHistory.model.CpsaPicLockHistory;
import com.wo.module.emailTemplate.dao.EmailTemplateDao;
import com.wo.module.emailTemplate.model.EmailTemplate;
import com.wo.module.parameter.dao.ParameterDetailDao;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.parameter.service.ParameterDetailService;
import com.wo.module.user.dao.UserDao;
import com.wo.module.user.model.User;

@Transactional
@Service("compliancePlanSelfAssessmentFEService")
public class CompliancePlanSelfAssessmentFEServiceImpl implements CompliancePlanSelfAssessmentFEService, Serializable{

	private static final long serialVersionUID = 3130259185999496428L;

	@Autowired
	@Qualifier("compliancePlanSelfAssessmentFEDao")
	private CompliancePlanSelfAssessmentFEDao compliancePlanSelfAssessmentFEDao;
	
	@Autowired
	@Qualifier("compliancePlanSelfAssessmentPicDao")
	private CompliancePlanSelfAssessmentPicDao compliancePlanSelfAssessmentPicDao;
	
	@Autowired
	@Qualifier("cpsaPicLockHistoryDao")
	private CpsaPicLockHistoryDao cpsaPicLockHistoryDao;
	
	@Autowired
	@Qualifier("parameterDetailDao")
	private ParameterDetailDao parameterDetailDao;
	
	@Autowired
	@Qualifier("userDao")
	private UserDao userDao;
	
	@Autowired
	@Qualifier("emailTemplateDao")
	private EmailTemplateDao emailTemplateDao;
	
	@Autowired
	@Qualifier("parameterDetailService")
	private ParameterDetailService parameterDetailService;
	
	public CompliancePlanSelfAssessmentFEDao getCompliancePlanSelfAssessmentFEDao() {
		return compliancePlanSelfAssessmentFEDao;
	}

	public void setCompliancePlanSelfAssessmentFEDao(CompliancePlanSelfAssessmentFEDao compliancePlanSelfAssessmentFEDao) {
		this.compliancePlanSelfAssessmentFEDao = compliancePlanSelfAssessmentFEDao;
	}	

	public CompliancePlanSelfAssessmentPicDao getCompliancePlanSelfAssessmentPicDao() {
		return compliancePlanSelfAssessmentPicDao;
	}

	public void setCompliancePlanSelfAssessmentPicDao(
			CompliancePlanSelfAssessmentPicDao compliancePlanSelfAssessmentPicDao) {
		this.compliancePlanSelfAssessmentPicDao = compliancePlanSelfAssessmentPicDao;
	}
	
	public ParameterDetailDao getParameterDetailDao() {
		return parameterDetailDao;
	}

	public void setParameterDetailDao(ParameterDetailDao parameterDetailDao) {
		this.parameterDetailDao = parameterDetailDao;
	}

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<CompliancePlanSelfAssessmentFEVo> searchData(List<? extends SearchObject> searchCriteria, int first,
			int pageSize, String sortField, SortOrder sortOrder) throws Exception {
		return compliancePlanSelfAssessmentFEDao.searchData(searchCriteria, first, pageSize, sortField, sortOrder);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception {
		return compliancePlanSelfAssessmentFEDao.searchCountData(searchCriteria);
	}

	@Override
	public CompliancePlanSelfAssessment findById(long cpsaId) {
		return compliancePlanSelfAssessmentFEDao.getById(cpsaId);
	}
	
	@Override
	public CompliancePlanSelfAssessmentFEVo getDataCpsaPic(Long cpsaId, Long userAnswerId) throws Exception {		
		return compliancePlanSelfAssessmentFEDao.getDataCpsaPic(cpsaId, userAnswerId);
	}

	@Override
	public List<CompliancePlanSelfAssessmentQuestionFEVo> getDataQuestion(Long cpsaId, Long userAnswerId) throws Exception {
		List<CompliancePlanSelfAssessmentQuestionFEVo> questDtlList = compliancePlanSelfAssessmentFEDao.getDataQuestionDetail(cpsaId, null, userAnswerId);
				
		return questDtlList;	
	}

	@Override
	public Long totalDataNotAnswer(Long cpsaId, Long userAnswerId) throws Exception {
		return compliancePlanSelfAssessmentFEDao.totalDataNotAnswer(cpsaId, userAnswerId);
	}
	
	@SuppressWarnings({ "deprecation"})
	public void submitAnswer(CompliancePlanSelfAssessmentFEVo cpsaVo, List<CompliancePlanSelfAssessmentQuestionFEVo> questVoList, String userLogin)  throws Exception {
		   CompliancePlanSelfAssessmentPic cpsaPic = new CompliancePlanSelfAssessmentPic();
		   cpsaPic = compliancePlanSelfAssessmentPicDao.findById(cpsaVo.getCpsaQuestId());
		   		   
		   boolean flagStatus = false;
		   Integer totalAnswer = 0;
		   List<CompliancePlanSelfAssessmentAnswer> childAnswerList = cpsaPic.getCpsaAnswers();
			if (questVoList != null && questVoList.size() > 0) {
				CompliancePlanSelfAssessmentAnswer cpsaAnswer = null;
				CompliancePlanSelfAssessmentAnswer cpsaAnswerDatabase = null;
				CompliancePlanSelfAssessmentQuestionFEVo cpsaQuestVo = null;
				boolean exist = false;

				// inser data baru dan update data lama
				for (int x = 0; x < questVoList.size(); x++) {
					cpsaQuestVo = (CompliancePlanSelfAssessmentQuestionFEVo) questVoList.get(x);

					exist = false;
					for (int i = 0; i < childAnswerList.size(); i++) {
						cpsaAnswerDatabase = (CompliancePlanSelfAssessmentAnswer) childAnswerList.get(i);
						if ((cpsaAnswerDatabase.getCpsaPic().getCpsaPicId().equals(cpsaQuestVo.getCpsaPicId()))
								&& (cpsaAnswerDatabase.getCpsaQuestion().getCpsaQuestionId().equals(cpsaQuestVo.getCpsaQuestId()))) {
							exist = true;
							break;
						}
					}

					if (exist) { // update
						cpsaAnswerDatabase.setCpsaAnswer(cpsaQuestVo.getCpsaAnswer());
                        if (StringUtils.isNotBlank(cpsaAnswerDatabase.getCpsaAnswer()) && cpsaAnswerDatabase.getCpsaAnswer().equals("COMPLIANT")) {
                            cpsaAnswerDatabase.setCpsaNote(CompliancePlanSelfAssessmentConstant.STRING_EMPTY);
                        }else {
                            cpsaAnswerDatabase.setCpsaNote(cpsaQuestVo.getCpsaNote());
                        }
                        cpsaAnswerDatabase.setCpsaAnswerDate(new Date());                        
						
						cpsaAnswerDatabase.setCpsaParentQuestion(cpsaQuestVo.getCpsaParentQuestId());
						cpsaAnswerDatabase.setLastUpdateBy(userLogin);
						cpsaAnswerDatabase.setLastUpdateDate(new Timestamp(new Date().getTime()));
						cpsaAnswerDatabase.setDelId(new Long(0));
						cpsaAnswerDatabase.setEnabledFlag(Constants.CONSTANT_YES);

						if (cpsaQuestVo.getCpsaParentQuestId() != null && cpsaQuestVo.getCpsaParentQuestId() > 0) {
							if (cpsaQuestVo.getCpsaAnswer() == null || cpsaQuestVo.getCpsaAnswer()
									.equals(CompliancePlanSelfAssessmentFEConstant.STRING_EMPTY)) {
								flagStatus = true;
							} else {
								totalAnswer = totalAnswer + 1;
							}
						}

					} else { // insert
						//System.out.println("cpsaQuestVo.getCpsaPicId() insert =  " + cpsaQuestVo.getCpsaPicId());
						cpsaAnswer = new CompliancePlanSelfAssessmentAnswer();

						CompliancePlanSelfAssessmentQuestion cpsaQuestion = new CompliancePlanSelfAssessmentQuestion();
						cpsaQuestion.setCpsaQuestionId(cpsaQuestVo.getCpsaQuestId());

						cpsaAnswer.setCpsaPic(cpsaPic);
						cpsaAnswer.setCpsaQuestion(cpsaQuestion);
						cpsaAnswer.setCpsaAnswer(cpsaQuestVo.getCpsaAnswer());
                        if (StringUtils.isNotBlank(cpsaAnswer.getCpsaAnswer()) && cpsaAnswer.getCpsaAnswer().equals("COMPLIANT")) {
                            cpsaAnswer.setCpsaNote(CompliancePlanSelfAssessmentConstant.STRING_EMPTY);
                        }else {
                            cpsaAnswer.setCpsaNote(cpsaQuestVo.getCpsaNote());
                        }
						cpsaAnswer.setCpsaAnswerDate(new Date());
						cpsaAnswer.setCpsaParentQuestion(cpsaQuestVo.getCpsaParentQuestId());
						cpsaAnswer.setCreatedBy(userLogin);
						cpsaAnswer.setCreationDate(new Timestamp(new Date().getTime()));
						cpsaAnswer.setDelId(new Long(0));
						cpsaAnswer.setEnabledFlag(Constants.CONSTANT_YES);

						if (cpsaQuestVo.getCpsaParentQuestId() != null && cpsaQuestVo.getCpsaParentQuestId() > 0) {
							if (cpsaQuestVo.getCpsaAnswer() == null || cpsaQuestVo.getCpsaAnswer()
									.equals(CompliancePlanSelfAssessmentFEConstant.STRING_EMPTY)) {
								flagStatus = true;
							} else {
								totalAnswer = totalAnswer + 1;
							}
						}

						childAnswerList.add(cpsaAnswer);
					}
				}
			}
		   		   
		   cpsaPic.setLastUpdateBy(userLogin);
		   cpsaPic.setLastUpdateDate(new Timestamp(new Date().getTime()));
		   cpsaPic.setDelId(new Long(0));
		   cpsaPic.setEnabledFlag(Constants.CONSTANT_YES);
			
		   if(flagStatus) {
			  cpsaPic.setStatusPic(parameterDetailDao.getParameterDetailByParamDtlCode(
			  CompliancePlanSelfAssessmentFEConstant.STATUS_CPSA_INPROGRESS)); 
			} else {
				cpsaPic.setStatusPic(parameterDetailDao.getParameterDetailByParamDtlCode(CompliancePlanSelfAssessmentFEConstant.STATUS_CPSA_WAITING_APPROVAL));
				// send email line manger
				try {
					sendEmail(cpsaPic);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			 
		   cpsaPic.setTotalAnswer(totalAnswer);
		   
		   compliancePlanSelfAssessmentPicDao.update(cpsaPic);
	}
	
	@Override
	public void saveAnswer(CompliancePlanSelfAssessmentFEVo cpsaVo, List<CompliancePlanSelfAssessmentQuestionFEVo> questVoList, String userLogin) throws Exception {
		CompliancePlanSelfAssessmentPic cpsaPic = new CompliancePlanSelfAssessmentPic();
		cpsaPic = compliancePlanSelfAssessmentPicDao.findById(cpsaVo.getCpsaQuestId());
	   		   
		boolean flagStatus = false;
		Integer totalAnswer = 0;
		List<CompliancePlanSelfAssessmentAnswer> childAnswerList = cpsaPic.getCpsaAnswers();
		if (questVoList != null && questVoList.size() > 0) {
			CompliancePlanSelfAssessmentAnswer cpsaAnswer = null;
			CompliancePlanSelfAssessmentAnswer cpsaAnswerDatabase = null;
			CompliancePlanSelfAssessmentQuestionFEVo cpsaQuestVo = null;
			boolean exist = false;
	
			// inser data baru dan update data lama
			for (int x = 0; x < questVoList.size(); x++) {
			cpsaQuestVo = (CompliancePlanSelfAssessmentQuestionFEVo) questVoList.get(x);
		
			exist = false;
			for (int i = 0; i < childAnswerList.size(); i++) {
				cpsaAnswerDatabase = (CompliancePlanSelfAssessmentAnswer) childAnswerList.get(i);
				if ((cpsaAnswerDatabase.getCpsaPic().getCpsaPicId().equals(cpsaQuestVo.getCpsaPicId()))
						&& (cpsaAnswerDatabase.getCpsaQuestion().getCpsaQuestionId().equals(cpsaQuestVo.getCpsaQuestId()))) {
					exist = true;
					break;
				}
			}
	
			if (exist) { // update
				cpsaAnswerDatabase.setCpsaAnswer(cpsaQuestVo.getCpsaAnswer());
				cpsaAnswerDatabase.setCpsaAnswerDate(new Date());
				cpsaAnswerDatabase.setCpsaNote(cpsaQuestVo.getCpsaNote());
				cpsaAnswerDatabase.setCpsaParentQuestion(cpsaQuestVo.getCpsaParentQuestId());
				cpsaAnswerDatabase.setLastUpdateBy(userLogin);
				cpsaAnswerDatabase.setLastUpdateDate(new Timestamp(new Date().getTime()));
				cpsaAnswerDatabase.setDelId(new Long(0));
				cpsaAnswerDatabase.setEnabledFlag(Constants.CONSTANT_YES);
		
				if (cpsaQuestVo.getCpsaParentQuestId() != null && cpsaQuestVo.getCpsaParentQuestId() > 0) {
					if (cpsaQuestVo.getCpsaAnswer() == null || cpsaQuestVo.getCpsaAnswer()
							.equals(CompliancePlanSelfAssessmentFEConstant.STRING_EMPTY)) {
						flagStatus = true;
					} else {
						totalAnswer = totalAnswer + 1;
					}
				}
		
			} else { // insert
					//System.out.println("cpsaQuestVo.getCpsaPicId() insert =  " + cpsaQuestVo.getCpsaPicId());
					cpsaAnswer = new CompliancePlanSelfAssessmentAnswer();
	
					CompliancePlanSelfAssessmentQuestion cpsaQuestion = new CompliancePlanSelfAssessmentQuestion();
					cpsaQuestion.setCpsaQuestionId(cpsaQuestVo.getCpsaQuestId());
	
					cpsaAnswer.setCpsaPic(cpsaPic);
					cpsaAnswer.setCpsaQuestion(cpsaQuestion);
					cpsaAnswer.setCpsaAnswer(cpsaQuestVo.getCpsaAnswer());
					cpsaAnswer.setCpsaAnswerDate(new Date());
					cpsaAnswer.setCpsaNote(cpsaQuestVo.getCpsaNote());
					cpsaAnswer.setCpsaParentQuestion(cpsaQuestVo.getCpsaParentQuestId());
					cpsaAnswer.setCreatedBy(userLogin);
					cpsaAnswer.setCreationDate(new Timestamp(new Date().getTime()));
					cpsaAnswer.setDelId(new Long(0));
					cpsaAnswer.setEnabledFlag(Constants.CONSTANT_YES);
	
					if (cpsaQuestVo.getCpsaParentQuestId() != null && cpsaQuestVo.getCpsaParentQuestId() > 0) {
						if (cpsaQuestVo.getCpsaAnswer() == null || cpsaQuestVo.getCpsaAnswer()
								.equals(CompliancePlanSelfAssessmentFEConstant.STRING_EMPTY)) {
							flagStatus = true;
						} else {
							totalAnswer = totalAnswer + 1;
						}
					}
	
					childAnswerList.add(cpsaAnswer);
				}
			}
		}
		cpsaPic.setLastUpdateBy(userLogin);
		cpsaPic.setLastUpdateDate(new Timestamp(new Date().getTime()));
		cpsaPic.setDelId(new Long(0));
		cpsaPic.setEnabledFlag(Constants.CONSTANT_YES);
		
	   
		cpsaPic.setTotalAnswer(totalAnswer);
		compliancePlanSelfAssessmentPicDao.update(cpsaPic);
	}
	
	private void sendEmail(CompliancePlanSelfAssessmentPic cpsaPic) {
		try {
			EmailTemplate emailTemplate = emailTemplateDao.getEmailTemplateByEmailTemplateCode("EMAIL_CPSA_APPROVAL");
			ParameterDetail pdHostName = parameterDetailDao.getParameterDetailByParamDtlCode(Constants.HOST_NAME_APPLICATION);
			
			String token = Constants.encryptString(cpsaPic.getCpsa().getCpsaId().toString());
			String menuId = Constants.encryptString(Constants.MENU_ID_CPAS_APPROVAL_FE);
			String urlLink = pdHostName.getNameIn().concat("pages/cpsaApprovalFE/cpsaApprovalFEEdit.faces?token="+token+"&menuId="+menuId);
			
			String emailSubject = emailTemplate.getEmailSubject().replaceAll("perihal_surat", cpsaPic.getCpsa().getLetterAbout());
			String emailContent = emailTemplate.getEmailContent();
			
			if (cpsaPic != null) {
				String emailTo = "";
				String emailCc = "";
				
				emailContent = emailContent.replaceAll("nama_pic", cpsaPic.getUser3().getName());
				emailContent = emailContent.replaceAll("url_link", urlLink);
				
				emailTo = cpsaPic.getUser3().getEmail();
				
				if(cpsaPic.getUser1()!= null) {
					if(emailCc.isBlank()){
						emailCc = cpsaPic.getUser1().getEmail();
					}else {
						emailCc = emailCc+","+cpsaPic.getUser1().getEmail();
					}
				}
				
				if(cpsaPic.getUser2()!= null) {
					if(emailCc.isBlank()){
						emailCc = cpsaPic.getUser2().getEmail();
					}else {
						emailCc = emailCc+","+cpsaPic.getUser2().getEmail();
					}
				}
				
				User user = userDao.getUserByNik(cpsaPic.getCreatedBy());
				
				if(cpsaPic.getUser2()!= null) {
					if(emailCc.isBlank()){
						emailCc = user.getEmail();
					}else {
						emailCc = emailCc+","+user.getEmail();
					}
				}
				
				final String subject = emailSubject;
				final String content = emailContent;
				final String to = emailTo;
				final String cc = emailCc;
				
				if (StringUtils.isNotBlank(to)) {
					CallApiManager.sendEmailAPI(to,cc, subject, content, "EMAIL_CPSA_APPROVAL", "true", parameterDetailService);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new CustomAPIException(e.getMessage()); 
		}
	}
	
	@Override
	public void lockUnlockCpsa(CompliancePlanSelfAssessmentFEVo cpsaVo, User userLogin, boolean lockFlag) {
		try {
			CompliancePlanSelfAssessmentPic cpsaPic = new CompliancePlanSelfAssessmentPic();
			CompliancePlanSelfAssessmentFEVo cpsaVoDataPic = compliancePlanSelfAssessmentFEDao.getDataCpsaPic(cpsaVo.getCpsaId(), userLogin.getUserId());
			cpsaPic = compliancePlanSelfAssessmentPicDao.findById(cpsaVoDataPic.getCpsaQuestId());
			
			
			cpsaPic.setLockFlag(lockFlag ? CommonConstants.RECORD_FLAG_YES : CommonConstants.RECORD_FLAG_NO);
			cpsaPic.setUserNikLock(userLogin.getNik());
			
			compliancePlanSelfAssessmentPicDao.update(cpsaPic);
			
			CpsaPicLockHistory cpsaPicLockHistory = new CpsaPicLockHistory();
			
			cpsaPicLockHistory.setCpsaPicId(cpsaPic.getCpsaPicId());
			cpsaPicLockHistory.setCpsaId(cpsaPic.getCpsa().getCpsaId());
			cpsaPicLockHistory.setLockStatus(lockFlag ? CommonConstants.RECORD_FLAG_YES : CommonConstants.RECORD_FLAG_NO);
			cpsaPicLockHistory.setCreatedBy(userLogin.getNik());
			cpsaPicLockHistory.setCreationDate(new Timestamp(System.currentTimeMillis()));
			
			cpsaPicLockHistoryDao.save(cpsaPicLockHistory);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	public StreamedContent generateDataExcel(Long cpsaId, Long userId1) throws Exception { 
		CompliancePlanSelfAssessment compliancePlanSelfAssessment = compliancePlanSelfAssessmentFEDao.findById(cpsaId);
		CompliancePlanSelfAssessmentFEVo cpsaDataVo = compliancePlanSelfAssessmentFEDao.getDataCpsaPic(cpsaId, userId1);
		
		Workbook wb = null;
		ByteArrayInputStream bais = null;
		ByteArrayOutputStream baos = null;
		DefaultStreamedContent streamContent = null;
		
		if(cpsaDataVo.getStatusPic() == null || 
				cpsaDataVo.getStatusPic().equals(CompliancePlanSelfAssessmentFEConstant.STATUS_COMPLIANCE_OPEN) ||
				cpsaDataVo.getStatusPic().equals(CompliancePlanSelfAssessmentFEConstant.STATUS_CPSA_REJECTED)) {
			InputStream is = this.getClass().getClassLoader().getResourceAsStream(CompliancePlanSelfAssessmentConstant.TEMPLATE_FILE_EDITABLE_PATH);
			
			try {
				wb = new XSSFWorkbook(is);
			} catch (IOException e) {
				//logger.error("Error while trying to read template file", e);
				return null;
			} finally {
				try {
					is.close();
				} catch (IOException e) {
					// do nothing
				}
			}
			
		}else {
			InputStream is = this.getClass().getClassLoader().getResourceAsStream(CompliancePlanSelfAssessmentConstant.TEMPLATE_FILE_PATH);
			
			try {
				wb = new XSSFWorkbook(is);
			} catch (IOException e) {
				//logger.error("Error while trying to read template file", e);
				return null;
			} finally {
				try {
					is.close();
				} catch (IOException e) {
					// do nothing
				}
			}
		}

		Sheet sheet = wb.getSheet("Kertas Kerja");
		//creating template for dropdown data
		int countComplianceKepatuhan = 0;
		if(cpsaDataVo.getStatusPic() == null || 
				cpsaDataVo.getStatusPic().equals(CompliancePlanSelfAssessmentFEConstant.STATUS_COMPLIANCE_OPEN) ||
				cpsaDataVo.getStatusPic().equals(CompliancePlanSelfAssessmentFEConstant.STATUS_CPSA_REJECTED)) {
			Sheet sheetComplianceDtl = wb.createSheet("ListKepatuhan");
			List<ParameterDetail> complianceKepatuhan = parameterDetailService.getParameterDetailByParamCodeOrderById("CPSA_KEPATUHAN");
			countComplianceKepatuhan = complianceKepatuhan.size();
			templateFileWriteListCompliance(wb, sheetComplianceDtl, complianceKepatuhan);
		}
		//creating template for dropdown data - end
		
		Row row = null;
		Cell cell = null;
		int rowNum = 14;
		int indexData = 14;		
		User userData = userDao.findById(userId1);
		
		if (compliancePlanSelfAssessment.getCpsaType() != null 
				&& (compliancePlanSelfAssessment.getCpsaType().getParameterDtlCode().equals("COMPLIANCE_PLAN_SELF_ASSESSMENT_TYPE_SYARIAH")
						|| compliancePlanSelfAssessment.getCpsaType().getParameterDtlCode().equals("COMPLIANCE_PLAN_SELF_ASSESSMENT_TYPE_CPSA"))) {
			cpsaDataVo.setDirectorate(userDao.getRegionByBranchCode(cpsaDataVo.getBranchCode()));
//			cpsaDataVo.setDirectorate(userDao.getSubBranchNameByBranchCode(cpsaDataVo.getBranchCode()));
			cpsaDataVo.setDivisionName(userDao.getSubBranchNameByBranchCode(cpsaDataVo.getBranchCode()));
		} else {
//			cpsaDataVo.setDirectorate(userData.getDirectorate());
			
			cpsaDataVo.setDirectorate(userDao.getDirectorateByDivisionId(cpsaDataVo.getDivisionId()));
		}
		
		if (compliancePlanSelfAssessment.getCpsaType().getParameterDtlCode().equals("COMPLIANCE_PLAN_SELF_ASSESSMENT_TYPE_SYARIAH")) {
			ParameterDetail pdHeaderExcel = parameterDetailDao.getParameterDetailByParamDtlCode("CPSA_HEADER_SYARIAH");
			cpsaDataVo.setHeaderExcelCpsa(pdHeaderExcel.getNameIn());
		} else if (compliancePlanSelfAssessment.getCpsaType().getParameterDtlCode().equals("COMPLIANCE_PLAN_SELF_ASSESSMENT_TYPE_CPSA")) {
			ParameterDetail pdHeaderExcel = parameterDetailDao.getParameterDetailByParamDtlCode("CPSA_HEADER_CPSA");
			cpsaDataVo.setHeaderExcelCpsa(pdHeaderExcel.getNameIn());
		} else {
			ParameterDetail pdHeaderExcel = parameterDetailDao.getParameterDetailByParamDtlCode("CPSA_HEADER_PUSAT");
			cpsaDataVo.setHeaderExcelCpsa(pdHeaderExcel.getNameIn());
		}
		
		this.excelWriteDataPic(sheet, row, cell, cpsaDataVo);
		this.excelWriteDataDtl(wb, sheet, row, cell, indexData, rowNum, cpsaDataVo, countComplianceKepatuhan);
		try {
			baos = new ByteArrayOutputStream();						 
			wb.write(baos);
		} catch (IOException e) {
			if (baos != null) {
				try {
					baos.close();
				} catch (IOException e2) {
					// do nothing
				}
			}
			//logger.error("Error while trying to write to mem output stream", e);
			return null;
		}

		try {
			bais = new ByteArrayInputStream(baos.toByteArray());
			SimpleDateFormat sdfTemp = new SimpleDateFormat("yyyymmddHHmmss");
			String fileName = CompliancePlanSelfAssessmentFEConstant.FILE_NAME_CPSA + "_";
			fileName = fileName + userData.getNik()+ "_" + sdfTemp.format(new Date()) + ".xlsx";
			streamContent = new DefaultStreamedContent(bais, "application/xls", fileName);
		} catch (Exception e) {
			//logger.error("Error while trying to create stream content", e);
			e.printStackTrace();
		} finally {
			try {
				baos.close();
				bais.close();
			} catch (IOException e) {
				// do nothing
			}
		}

		return streamContent;
	}
	
	private void templateFileWriteListCompliance(Workbook wb, Sheet sheetComplianceDtl,
			List<ParameterDetail> complianceKepatuhan) {
		Row row = null;
		Cell cell = null;
		row = sheetComplianceDtl.createRow(0);
		cell = row.createCell(0);
		cell.setCellValue("Kepatuhan");
		
		int rowNum = 1;
		
		for(ParameterDetail kepatuhan : complianceKepatuhan) {
			row = sheetComplianceDtl.createRow(rowNum);
			cell = row.createCell(0);
			cell.setCellValue(kepatuhan.getName());
			
			rowNum ++;
		}
	}

	private void excelWriteDataPic(Sheet sheet, Row row, Cell cell, CompliancePlanSelfAssessmentFEVo picVo) {
		row = sheet.getRow(3);
		cell = row.getCell(11);
		cell.setCellValue(picVo.getDirectorate());
		
		cell = row.getCell(20);
		cell.setCellValue(picVo.getDivisionName());
		
		row = sheet.getRow(4);
		cell = row.getCell(11);
		cell.setCellValue(picVo.getPeriodStartDateStr() + " - " + picVo.getPeriodEndDateStr());

		cell = row.getCell(20);
		cell.setCellValue(picVo.getYearStr());
		
		cell = row.getCell(32);
		cell.setCellValue(picVo.getTotalAnswer() != null ? picVo.getTotalQuestion() - picVo.getTotalAnswer() : picVo.getTotalQuestion() - 0);
		
		row = sheet.getRow(11);
		cell = row.getCell(1);
		cell.setCellValue(picVo.getHeaderExcelCpsa());
	}

	@SuppressWarnings("static-access")
	private void excelWriteDataDtl(Workbook wb, Sheet sheet, Row row, Cell cell, Integer indexData, Integer indexRowNum,
			CompliancePlanSelfAssessmentFEVo picVo, int countComplianceKepatuhan) {
		try {
			List<CompliancePlanSelfAssessmentQuestionFEVo> dataList = compliancePlanSelfAssessmentFEDao.getDataQuestionDetail(picVo.getCpsaId(), null, picVo.getUserId1());
			if (dataList != null && dataList.size() > 0) {
				for (CompliancePlanSelfAssessmentQuestionFEVo headerVo : dataList) {
					if (indexRowNum == indexData) {
						row = sheet.getRow(indexRowNum);
					} else {
						row = FileUtil.excelCopyRow(sheet, indexData, indexRowNum, true);
					}
                     
					if(headerVo.getCpsaParentQuestId() == null) {
						cell = row.getCell(1);
						cell.setCellStyle(this.buildStyleForDataHeaderNo(wb));
						cell.setCellValue(headerVo.getCpsaQuestNo()+".");
	
						cell = row.getCell(2);
						cell.setCellStyle(this.buildStyleForDataHeader(wb));
						cell.setCellValue(headerVo.getCpsaQuestion());
						sheet.addMergedRegion(new CellRangeAddress(indexRowNum, indexRowNum, 2, 32));					
					}else {
						cell = row.getCell(1);
						cell.setCellStyle(this.buildStyleForDataDetailNo(wb));
						cell.setCellValue(headerVo.getCpsaQuestNo()+".");

						cell = row.getCell(2);				
						int heightSize = headerVo.getCpsaQuestion().trim().length();
						row.setHeightInPoints((heightSize/4)+25);
						cell.setCellStyle(this.buildStyleForDataDetail(wb));
						cell.setCellValue(headerVo.getCpsaQuestion());
						sheet.addMergedRegion(buildStyleForBorderMergedRegion(wb, sheet, indexRowNum, indexRowNum, 2, 25));		
													
						cell = row.getCell(26);
						cell.setCellStyle(this.buildStyleForDataDetailCenter(wb));
						if(countComplianceKepatuhan > 0) {
							templateCreateListCompliance(sheet, indexRowNum, countComplianceKepatuhan);
						}
						cell.setCellValue(headerVo.getCpsaAnswerName());
						

						cell = row.getCell(30);
						cell.setCellStyle(this.buildStyleForDataDetail(wb));
						cell.setCellValue(headerVo.getCpsaNote());
						sheet.addMergedRegion(buildStyleForBorderMergedRegion(wb, sheet, indexRowNum, indexRowNum, 30, 32));
						
					}
					indexRowNum++;
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void templateCreateListCompliance(Sheet sheet, Integer indexRowNum, int countComplianceKepatuhan) {
		//Create Formula for Dropdown
		Name namedRange = sheet.getWorkbook().createName();
		String formulaName = "listKepatuhan"+indexRowNum.toString();
		namedRange.setNameName(formulaName);
		String colName = "A";
		String formula = String.format("%s!$%s$%d:$%s$%d",
				"ListKepatuhan", colName, 2 , colName, 1 + countComplianceKepatuhan);
		namedRange.setRefersToFormula(formula);
		//Create Formula for Dropdown - END
		
		//Create Data Validation with created formula
		XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper((XSSFSheet) sheet);				
		DataValidationConstraint dvConstraint = (DataValidationConstraint) dvHelper
				.createFormulaListConstraint(formulaName);
		CellRangeAddressList addressList = new CellRangeAddressList(indexRowNum,
				indexRowNum, 26, 26);
		XSSFDataValidation validation = (XSSFDataValidation) dvHelper
				.createValidation(dvConstraint, addressList);
		// validation.setSuppressDropDownArrow(false);
		validation.setShowErrorBox(true);
		sheet.addValidationData(validation);
		//Create Data Validation with created formula - END
		
	}

	private static CellStyle buildStyleForDataHeaderNo(Workbook wb) {
		CellStyle headerStyle = wb.createCellStyle();
	    Font font = wb.createFont();
	    font.setColor(IndexedColors.WHITE.getIndex()); 
	    font.setBold(true);
	    font.setFontHeightInPoints((short)14);
	    headerStyle.setFont(font);
	    headerStyle.setFillForegroundColor(IndexedColors.BLACK.index);
	    headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
	    headerStyle.setAlignment(HorizontalAlignment.CENTER);
	    
		return headerStyle;
	}
	
	private static CellStyle buildStyleForDataHeader(Workbook wb) {
		CellStyle headerStyle = wb.createCellStyle();
	    Font font = wb.createFont();
	    font.setColor(IndexedColors.WHITE.getIndex()); 
	    font.setBold(true);
	    font.setFontHeightInPoints((short)12);
	    headerStyle.setFont(font);
	    headerStyle.setFillForegroundColor(IndexedColors.BLACK.index);
	    headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
	    
		return headerStyle;
	}
	
	private static CellStyle buildStyleForDataDetailNo(Workbook wb) {
		CellStyle headerStyle = wb.createCellStyle();
	    Font font = wb.createFont();
	    font.setColor(IndexedColors.BLACK.getIndex()); 	    
	    font.setFontHeightInPoints((short)11);
	    headerStyle.setFont(font);
	    headerStyle.setFillForegroundColor(IndexedColors.WHITE.index);
	    headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
	    headerStyle.setWrapText(true);  
	    headerStyle.setAlignment(HorizontalAlignment.CENTER);
	    headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
	    headerStyle.setBorderTop(BorderStyle.MEDIUM);
	    headerStyle.setBorderBottom(BorderStyle.MEDIUM);
	    headerStyle.setBorderLeft(BorderStyle.MEDIUM);
	    headerStyle.setBorderRight(BorderStyle.MEDIUM);

		return headerStyle;
	}
	
	private static CellStyle buildStyleForDataDetail(Workbook wb) {
		CellStyle headerStyle = wb.createCellStyle();
	    Font font = wb.createFont();
	    font.setColor(IndexedColors.BLACK.getIndex()); 
	    headerStyle.setFont(font);
	    font.setFontHeightInPoints((short)11);
	    headerStyle.setFillForegroundColor(IndexedColors.WHITE.index);
	    headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
	    headerStyle.setWrapText(true);  
	    headerStyle.setBorderTop(BorderStyle.MEDIUM);
	    headerStyle.setBorderBottom(BorderStyle.MEDIUM);
	    headerStyle.setBorderLeft(BorderStyle.MEDIUM);
	    headerStyle.setBorderRight(BorderStyle.MEDIUM);
	    headerStyle.setVerticalAlignment(VerticalAlignment.TOP);

		return headerStyle;
	}
	
	private static CellStyle buildStyleForDataDetailCenter(Workbook wb) {
		CellStyle headerStyle = wb.createCellStyle();
	    Font font = wb.createFont();
	    font.setColor(IndexedColors.BLACK.getIndex()); 
	    headerStyle.setFont(font);
	    font.setFontHeightInPoints((short)11);
	    headerStyle.setFillForegroundColor(IndexedColors.WHITE.index);
	    headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
	    headerStyle.setWrapText(true);  
	    headerStyle.setBorderTop(BorderStyle.MEDIUM);
	    headerStyle.setBorderBottom(BorderStyle.MEDIUM);
	    headerStyle.setBorderLeft(BorderStyle.MEDIUM);
	    headerStyle.setBorderRight(BorderStyle.MEDIUM);
	    headerStyle.setAlignment(HorizontalAlignment.CENTER);
	    headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);

		return headerStyle;
	}
	
	public static CellRangeAddress buildStyleForBorderMergedRegion(Workbook wb, Sheet sheet, int firstRow, int lastRow,
			int firstCol, int lastCol) {
		CellRangeAddress cellRangeAddress = new CellRangeAddress(firstRow, lastRow, firstCol, lastCol);
		RegionUtil.setBorderTop(BorderStyle.MEDIUM, cellRangeAddress, sheet);
		RegionUtil.setBorderBottom(BorderStyle.MEDIUM, cellRangeAddress, sheet);
		RegionUtil.setBorderLeft(BorderStyle.MEDIUM, cellRangeAddress, sheet);
		RegionUtil.setBorderRight(BorderStyle.MEDIUM, cellRangeAddress, sheet);

		return cellRangeAddress;
	}

	public EmailTemplateDao getEmailTemplateDao() {
		return emailTemplateDao;
	}

	public void setEmailTemplateDao(EmailTemplateDao emailTemplateDao) {
		this.emailTemplateDao = emailTemplateDao;
	}

	public ParameterDetailService getParameterDetailService() {
		return parameterDetailService;
	}

	public void setParameterDetailService(ParameterDetailService parameterDetailService) {
		this.parameterDetailService = parameterDetailService;
	}

	public CpsaPicLockHistoryDao getCpsaPicLockHistoryDao() {
		return cpsaPicLockHistoryDao;
	}

	public void setCpsaPicLockHistoryDao(CpsaPicLockHistoryDao cpsaPicLockHistoryDao) {
		this.cpsaPicLockHistoryDao = cpsaPicLockHistoryDao;
	}	
}
