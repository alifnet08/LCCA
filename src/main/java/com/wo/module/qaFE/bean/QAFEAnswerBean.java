package com.wo.module.qaFE.bean;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.primefaces.PrimeFaces;

import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.exception.CustomAPIException;
import com.wo.module.common.utility.CallApiManager;
import com.wo.module.emailTemplate.model.EmailTemplate;
import com.wo.module.emailTemplate.service.EmailTemplateService;
import com.wo.module.engine.vo.SendEmailVO;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.qa.constant.QAConstants;
import com.wo.module.qa.model.QA;
import com.wo.module.qa.model.QAAttachment;
import com.wo.module.qaFE.constant.QAFEConstant;
import com.wo.module.qaFE.service.QAFEService;
import com.wo.module.qaFE.vo.QAFEAnswerVo;

public class QAFEAnswerBean extends CommonBean implements Serializable{

	private static final long serialVersionUID = -4157661570004470164L;
	private static final Logger logger = Logger.getLogger(QAFEAnswerBean.class);
	private static final String NAVIGATE_BACK = QAFEConstant.NAVIGATE_QA_FE;
	
	private QAFEService qaFEService;
	
	private EmailTemplateService emailTemplateService;
	
	private QA qa;

	private String question;
	private String editId;
	private String categoryType;
	private String qDateStr;
	private String answer;
	
	private boolean isPublishQnaTemp;
	
	private Long lastQnaId;
	
	private List<QAFEAnswerVo> qaFEAnswerLists;
	
	private List<QA> qaFEQuestionLists;
	
	private FacesUtil facesUtil;
	
	private Boolean flag;
	
	private SimpleDateFormat sdf = new SimpleDateFormat("dd MMM YYYY HH:mm");
	
	private Integer testFirst;
	
	private boolean testPublishFlag;
	private boolean isDisabledQna1;
	
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
			facesUtil.setSessionAttribute("FIRST_QA_FE", testFirst);
			facesUtil.setSessionAttribute("BACK_SESSION", false);
		}
		qaFEAnswerLists = new ArrayList<QAFEAnswerVo>();
		
		checkNewOrEdit();
	}

	private void checkNewOrEdit() {
		this.editId = facesUtil.retrieveRequestParam("editId");
		String token = facesUtil.retrieveRequestParam("token");
		if(StringUtils.isNotEmpty(token)) {
			editId = Constants.decryptString(token);
		}
		
		Number admin = qaFEService.getIsAdmin(facesUtil.getUserLogin().getUserId());
		
		if (admin == null || admin.intValue() == 0) {
			String baseContextPath = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
			try {
				if(StringUtils.isEmpty(token)){
					token = Constants.encryptString(editId);
				}
				FacesContext.getCurrentInstance().getExternalContext()
						.redirect(baseContextPath + "/pages/qaFE/qaFEEdit.faces?token"+token);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} 
		
		if (StringUtils.isBlank(editId)) {
			// do noting
		} else {
			handleEdit(editId);
		}
	}
	
	private void handleEdit(String editId) {
		try {
			isDisabledQna1 = false;
			Long editIdLong = Long.parseLong(editId);
			
			qa = qaFEService.findById(editIdLong);
			ParameterDetail pdCatType = parameterDetailService.getParameterDetailByParamDtlCode(qa.getCategoryType());
			if (pdCatType != null) {
				this.categoryType = pdCatType.getName();
			} else {
				this.categoryType = "";
			}
			
			if (qa.getqDate() != null) {
				this.qDateStr = sdf.format(qa.getqDate());
			} else {
				this.qDateStr = "";
			}
			
			if (qa.getaUser() != null) {
				QAFEAnswerVo vo = new QAFEAnswerVo();
				vo.setAnswer(qa.getAnswer());
				vo.setAnswerDate(qa.getaDate());
				vo.setAnswerId(qa.getaUser().getUserId());
				vo.setAnswerDateStr(sdf.format(qa.getaDate()));
				vo.setAnswerBy(qa.getaUser().getName());
				vo.setAnswerDivisionName(qa.getaUser().getDivisionName());
				isDisabledQna1 = true;
				
				qaFEAnswerLists.add(vo);
			}
			
			if (qa.getaUser2() != null) {
				QAFEAnswerVo vo = new QAFEAnswerVo();
				vo.setAnswer(qa.getAnswer2());
				vo.setAnswerDate(qa.getaDate2());
				vo.setAnswerId(qa.getaUser2().getUserId());
				vo.setAnswerDateStr(sdf.format(qa.getaDate2()));
				vo.setAnswerBy(qa.getaUser2().getName());
				vo.setAnswerDivisionName(qa.getaUser2().getDivisionName());
				isDisabledQna1 = true;
				
				qaFEAnswerLists.add(vo);
			}
			
			if (qa.getaUser3() != null) {
				QAFEAnswerVo vo = new QAFEAnswerVo();
				vo.setAnswer(qa.getAnswer3());
				vo.setAnswerDate(qa.getaDate3());
				vo.setAnswerId(qa.getaUser3().getUserId());
				vo.setAnswerDateStr(sdf.format(qa.getaDate3()));
				vo.setAnswerBy(qa.getaUser3().getName());
				vo.setAnswerDivisionName(qa.getaUser3().getDivisionName());
				isDisabledQna1 = true;
				
				qaFEAnswerLists.add(vo);
			}
			
			lastQnaId = qa.getQnaId();
			
			if (!StringUtils.isEmpty(qa.getPublishQna())) {
				if (qa.getPublishQna().equals(Constants.CONSTANT_YES)) {
					qa.setIsPublishTemp(true);
				} else {
					qa.setIsPublishTemp(false);
				}
			}
			
			if (qa.getQaAttachmentList() != null && !qa.getQaAttachmentList().isEmpty()) {
				for (int i = 0; i < qa.getQaAttachmentList().size(); i++) {
					QAAttachment dataQaAttach = qa.getQaAttachmentList().get(i);
					
					if (!StringUtils.isEmpty(dataQaAttach.getPublishAttachment())) {
						if (dataQaAttach.getPublishAttachment().equals(Constants.CONSTANT_YES)) {
							dataQaAttach.setPublishTemp(true);
						} else {
							dataQaAttach.setPublishTemp(false);
						}
					}
					
					qa.getQaAttachmentList().set(i, dataQaAttach);
				}
			}
			
			qaFEQuestionLists = qaFEService.getQAByParentId(qa.getQnaId());
			
			for(int i=0;i<qaFEQuestionLists.size();i++){
				QA qa = qaFEQuestionLists.get(i);
				if(lastQnaId < qa.getQnaId()){
					lastQnaId = qa.getQnaId();
				}
				qa.setDisabledQna(false);
				
				if (qa.getaUser() != null) {
					qa.setDisabledQna(true);
				}
				
				if (qa.getaUser2() != null) {
					qa.setDisabledQna(true);
				}
				
				if (qa.getaUser3() != null) {
					qa.setDisabledQna(true);
				}
				
				if (qa.getQaAttachmentList() != null && !qa.getQaAttachmentList().isEmpty()) {
					for (int j = 0; j < qa.getQaAttachmentList().size(); j++) {
						QAAttachment dataQaAttach = qa.getQaAttachmentList().get(j);
						
						if (!StringUtils.isEmpty(dataQaAttach.getPublishAttachment())) {
							if (dataQaAttach.getPublishAttachment().equals(Constants.CONSTANT_YES)) {
								dataQaAttach.setPublishTemp(true);
							} else {
								dataQaAttach.setPublishTemp(false);
							}
						}
						
						qa.getQaAttachmentList().set(j, dataQaAttach);
					}
				}
				
				if (StringUtils.isNotBlank(qa.getPublishQna())) {
					if (qa.getPublishQna().equals(Constants.CONSTANT_YES)) {
						qa.setIsPublishTemp(true);
					} else {
						qa.setIsPublishTemp(false);
					}
				}
				
				qaFEQuestionLists.set(i, qa);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void cancel() {
		try {
			facesUtil.redirect("/pages/qaFE/qaFE.faces");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void backSession() {
		if (facesUtil.retrieveRequestParam("first") != null) {
			facesUtil.setSessionAttribute("BACK_SESSION", true);
		}
	}
	
	public void closeThread(){
		try {
			String answer = facesUtil.retrieveRequestParam("ANSWER");
			
			if (StringUtils.isNotBlank(qa.getqStatus()) && qa.getqStatus().equalsIgnoreCase(ParameterDetail.PARAM_DET_CODE_QNA_STATUS_ANSWERED)) {
				qa.setThreadStatus("Y");
				qa.setqStatus("QNA_STATUS_CLOSE");
				qa.setReadFlag(Constants.CONSTANT_YES);
				qa.setLastUpdateBy(facesUtil.retrieveUserLogin());
				qa.setLastUpdateDate(new Timestamp(new Date().getTime()));
				qaFEService.update(qa);
				
				List<QA> qnaDetailList = qaFEService.getQAByParentId(qa.getQnaId());
				if (qnaDetailList != null && !qnaDetailList.isEmpty()) {
					for (QA dataQa : qnaDetailList) {
						dataQa.setThreadStatus("Y");
						dataQa.setqStatus("QNA_STATUS_CLOSE");
						dataQa.setReadFlag(Constants.CONSTANT_YES);
						dataQa.setLastUpdateBy(facesUtil.retrieveUserLogin());
						dataQa.setLastUpdateDate(new Timestamp(new Date().getTime()));
						qaFEService.update(dataQa);
					}
				}
				
				facesUtil.setSessionAttribute("BACK_SESSION", true);
				facesUtil.redirect("/pages/qaFE/qaFE.faces");
			} else {
				facesUtil.addErrMessage(facesUtil.retrieveMessage("formReportQnAAnswerNotAnswered"));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void save(){
		
		try {
			
			String answer = facesUtil.retrieveRequestParam("ANSWER");
			
			if (StringUtils.isEmpty(answer)) {
				facesUtil.addErrMessage(facesUtil.retrieveMessage("formReportQnAAnswerNotAnswered"));
			} else {
				List<QA> listData = qaFEService.getQANotAnswerByParentId(qa.getQnaId());
				if(listData!=null && listData.size()>0){
					Map<Long, String> qaAttachIdMap = new HashMap<>();
					Map<Long, String> qaIdMap = new HashMap<>();
					if (qaFEQuestionLists != null && !qaFEQuestionLists.isEmpty()) {
						for (QA dataQa : qaFEQuestionLists) {
							if (dataQa.getQaAttachmentList() != null && !dataQa.getQaAttachmentList().isEmpty()) {
								for (int i = 0; i < dataQa.getQaAttachmentList().size(); i++) {
									QAAttachment dataQaAttach= dataQa.getQaAttachmentList().get(i);
									
									if (Boolean.TRUE.equals(dataQaAttach.isPublishTemp())) {
										dataQaAttach.setPublishAttachment(Constants.CONSTANT_YES);
									} else {
										dataQaAttach.setPublishAttachment(Constants.CONSTANT_NO);
									}
									
									qaAttachIdMap.put(dataQaAttach.getQnaAttachmentId(), dataQaAttach.getPublishAttachment());
								}
							}
							
							if (Boolean.TRUE.equals(dataQa.getIsPublishTemp())) {
								dataQa.setPublishQna(Constants.CONSTANT_YES);
							} else {
								dataQa.setPublishQna(Constants.CONSTANT_NO);
							}
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																									
							qaIdMap.put(dataQa.getQnaId(), dataQa.getPublishQna());
						}
					}
					
					for(int i=0;i<listData.size();i++){
						QA qaNew = listData.get(i);
						
						if (qaIdMap.containsKey(qaNew.getQnaId())) {
							qaNew.setPublishQna(qaIdMap.get(qaNew.getQnaId()));
						}
						
						if(StringUtils.isEmpty(qaNew.getAnswer())){
							qaNew.setAnswer(answer);
							qaNew.setaDate(new Timestamp(System.currentTimeMillis()));
							qaNew.setaUser(facesUtil.getUserLogin());
						}
						else if(StringUtils.isEmpty(qaNew.getAnswer2())){
							qaNew.setAnswer2(answer);
							qaNew.setaDate2(new Timestamp(System.currentTimeMillis()));
							qaNew.setaUser2(facesUtil.getUserLogin());
						}
						else if(StringUtils.isEmpty(qaNew.getAnswer3())){
							qaNew.setAnswer3(answer);
							qaNew.setaDate3(new Timestamp(System.currentTimeMillis()));
							qaNew.setaUser3(facesUtil.getUserLogin());
						}
						
						if (qaNew.getQaAttachmentList() != null && !qaNew.getQaAttachmentList().isEmpty()) {
							for (int j = 0; j < qaNew.getQaAttachmentList().size(); j++) {
								QAAttachment dataQaAttach = qaNew.getQaAttachmentList().get(i);
								
								if (qaAttachIdMap.containsKey(dataQaAttach.getQnaAttachmentId())) {
									dataQaAttach.setPublishAttachment(qaAttachIdMap.get(dataQaAttach.getQnaAttachmentId()));
									
									qaNew.getQaAttachmentList().set(i, dataQaAttach);
								}
							}
						}
						
						if(qaNew.getFromQnaId()!=null){
							QA qaParent = qaNew.getFromQnaId();
							qaParent.setqStatus(ParameterDetail.PARAM_DET_CODE_QNA_STATUS_ANSWERED);
							qaParent.setLastUpdateBy(facesUtil.retrieveUserLogin());
							qaParent.setLastUpdateDate(new Timestamp(new Date().getTime()));
							qaFEService.update(qaParent);
							qaNew.setqStatus(ParameterDetail.PARAM_DET_CODE_QNA_STATUS_ANSWERED);
						}else{
							qaNew.setqStatus(ParameterDetail.PARAM_DET_CODE_QNA_STATUS_ANSWERED);
						}
						
						qaNew.setReadFlag(Constants.ENABLED_FLAG_FALSE);
						qaNew.setLastUpdateBy(facesUtil.retrieveUserLogin());
						qaNew.setLastUpdateDate(new Timestamp(new Date().getTime()));
						
						qaFEService.update(qaNew);
						
						
						// this should be a singleton
				        ExecutorService emailExecutor = Executors.newCachedThreadPool();

				        // from you sendEmail() method
				        emailExecutor.execute(new Runnable() {
				            @Override
				            public void run() {
				                try {
				                	sendEmail(qaNew);
				                } catch (Exception e) {
				                    logger.error("send email failed", e);
				                }
				            }
				        });
					}
					
				}else{
				QA qaNew = qaFEService.findById(lastQnaId);
				
				if (Boolean.TRUE.equals(qa.getIsPublishTemp())) {
					qaNew.setPublishQna(Constants.CONSTANT_YES);
				} else {
					qaNew.setPublishQna(Constants.CONSTANT_NO);
				}
				
				qaNew.setReadFlag(Constants.CONSTANT_YES);
				
				Map<Long, String> qaAttachIdMap = new HashMap<>();
				if (qaFEQuestionLists!= null && !qaFEQuestionLists.isEmpty()) {
					for (QA qa : qaFEQuestionLists) {
						if (qa.getQaAttachmentList() != null && !qa.getQaAttachmentList().isEmpty()) {
							for (int i = 0; i < qa.getQaAttachmentList().size(); i++) {
								QAAttachment dataQaAttach = qa.getQaAttachmentList().get(i);
								
								if (Boolean.TRUE.equals(dataQaAttach.isPublishTemp())) {
									dataQaAttach.setPublishAttachment(Constants.CONSTANT_YES);
								} else {
									dataQaAttach.setPublishAttachment(Constants.CONSTANT_NO);
								}
								
								qaAttachIdMap.put(dataQaAttach.getQnaAttachmentId(), dataQaAttach.getPublishAttachment());
							}
						}
					}
				}
				if (qa.getQaAttachmentList() != null && !qa.getQaAttachmentList().isEmpty()) {
					for (int i = 0; i < qa.getQaAttachmentList().size(); i++) {
						QAAttachment dataQaAttach = qa.getQaAttachmentList().get(i);
						
						if (Boolean.TRUE.equals(dataQaAttach.isPublishTemp())) {
							dataQaAttach.setPublishAttachment(Constants.CONSTANT_YES);
						} else {
							dataQaAttach.setPublishAttachment(Constants.CONSTANT_NO);
						}
						
						qaAttachIdMap.put(dataQaAttach.getQnaAttachmentId(), dataQaAttach.getPublishAttachment());
					}
				}
				
				if(StringUtils.isEmpty(qaNew.getAnswer())){
					qaNew.setAnswer(answer);
					qaNew.setaDate(new Timestamp(System.currentTimeMillis()));
					qaNew.setaUser(facesUtil.getUserLogin());
				}
				else if(StringUtils.isEmpty(qaNew.getAnswer2())){
					qaNew.setAnswer2(answer);
					qaNew.setaDate2(new Timestamp(System.currentTimeMillis()));
					qaNew.setaUser2(facesUtil.getUserLogin());
				}
				else if(StringUtils.isEmpty(qaNew.getAnswer3())){
					qaNew.setAnswer3(answer);
					qaNew.setaDate3(new Timestamp(System.currentTimeMillis()));
					qaNew.setaUser3(facesUtil.getUserLogin());
				}
				
				if(qaNew.getFromQnaId()!=null){
					QA qaParent = qaNew.getFromQnaId();
					qaParent.setqStatus(ParameterDetail.PARAM_DET_CODE_QNA_STATUS_ANSWERED);
					qaParent.setLastUpdateBy(facesUtil.retrieveUserLogin());
					qaParent.setLastUpdateDate(new Timestamp(new Date().getTime()));
					qaFEService.update(qaParent);
					qaNew.setqStatus(ParameterDetail.PARAM_DET_CODE_QNA_STATUS_ANSWERED);
				}else{
					qaNew.setqStatus(ParameterDetail.PARAM_DET_CODE_QNA_STATUS_ANSWERED);
				}
				
				if (!qaAttachIdMap.isEmpty()) {
					if (qaNew.getQaAttachmentList() != null && !qaNew.getQaAttachmentList().isEmpty()) {
						for (int i = 0; i < qaNew.getQaAttachmentList().size(); i++) {
							QAAttachment dataQaAttach = qaNew.getQaAttachmentList().get(i);
							
							if (qaAttachIdMap.containsKey(dataQaAttach.getQnaAttachmentId())) {
								dataQaAttach.setPublishAttachment(qaAttachIdMap.get(dataQaAttach.getQnaAttachmentId()));
								
								qaNew.getQaAttachmentList().set(i, dataQaAttach);
							}
						}
					}
				}
				
				qaNew.setReadFlag(Constants.ENABLED_FLAG_FALSE);
				qaNew.setLastUpdateBy(facesUtil.retrieveUserLogin());
				qaNew.setLastUpdateDate(new Timestamp(new Date().getTime()));
				
				qaFEService.update(qaNew);
				
				
				// this should be a singleton
		        ExecutorService emailExecutor = Executors.newCachedThreadPool();

		        // from you sendEmail() method
		        emailExecutor.execute(new Runnable() {
		            @Override
		            public void run() {
		                try {
		                	sendEmail(qaNew);
		                } catch (Exception e) {
		                    logger.error("send email failed", e);
		                }
		            }
		        });
				}
				
				
				facesUtil.redirect("/pages/qaFE/qaFE.faces");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public void sendEmail(QA qaUpd) {
		try {
			
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
			EmailTemplate emailTemplate = emailTemplateService.getEmailTemplateByEmailTemplateCode(QAConstants.EMAIL_QA_ANSWER);
			String emailSubject = emailTemplate.getEmailSubject();
			
			emailSubject = emailSubject.replaceAll("counter_type", "Notification");
			emailSubject = emailSubject.replaceAll("ticket_no", qaUpd.getTicketNo());			
	
			String emailContent = ""; 
			String emailTo  = "";
			String emailCc = "";
		    
			emailContent = emailTemplate.getEmailContent();
			emailContent = emailContent.replaceAll("question_date",sdf.format(qaUpd.getqDate()));
			emailContent = emailContent.replaceAll("ticket_no",qaUpd.getTicketNo());
			emailContent = emailContent.replaceAll("question_by",qaUpd.getqUser().getName());
			
			emailTo = qaUpd.getqUser().getEmail();
			
			CallApiManager.sendEmailAPI(emailTo,emailCc, emailSubject,
						emailContent, QAConstants.EMAIL_QA_ANSWER, "true", parameterDetailService);
				
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new CustomAPIException(ex.getMessage()); 
		}
		
	}

	public void downloadFile(String fileId, String fileName, byte[] content) throws Exception {
		String answer = facesUtil.retrieveRequestParam("FILEID");
		CallApiManager.downloadFile(fileId, fileName, content, parameterDetailService);
	}
	
	public QAFEService getQaFEService() {
		return qaFEService;
	}

	public void setQaFEService(QAFEService qaFEService) {
		this.qaFEService = qaFEService;
	}

	public SimpleDateFormat getSdf() {
		return sdf;
	}

	public void setSdf(SimpleDateFormat sdf) {
		this.sdf = sdf;
	}

	public QA getQa() {
		return qa;
	}

	public void setQa(QA qa) {
		this.qa = qa;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public FacesUtil getFacesUtil() {
		return facesUtil;
	}

	public void setFacesUtil(FacesUtil facesUtil) {
		this.facesUtil = facesUtil;
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

	public String getEditId() {
		return editId;
	}

	public void setEditId(String editId) {
		this.editId = editId;
	}

	public List<QAFEAnswerVo> getQaFEAnswerLists() {
		return qaFEAnswerLists;
	}

	public void setQaFEAnswerLists(List<QAFEAnswerVo> qaFEAnswerLists) {
		this.qaFEAnswerLists = qaFEAnswerLists;
	}

	public String getCategoryType() {
		return categoryType;
	}

	public void setCategoryType(String categoryType) {
		this.categoryType = categoryType;
	}

	public String getqDateStr() {
		return qDateStr;
	}

	public void setqDateStr(String qDateStr) {
		this.qDateStr = qDateStr;
	}

	public Long getLastQnaId() {
		return lastQnaId;
	}

	public void setLastQnaId(Long lastQnaId) {
		this.lastQnaId = lastQnaId;
	}

	public List<QA> getQaFEQuestionLists() {
		return qaFEQuestionLists;
	}

	public void setQaFEQuestionLists(List<QA> qaFEQuestionLists) {
		this.qaFEQuestionLists = qaFEQuestionLists;
	}

	public Boolean getFlag() {
		return flag;
	}

	public void setFlag(Boolean flag) {
		this.flag = flag;
	}

	public EmailTemplateService getEmailTemplateService() {
		return emailTemplateService;
	}

	public void setEmailTemplateService(EmailTemplateService emailTemplateService) {
		this.emailTemplateService = emailTemplateService;
	}

	public Integer getTestFirst() {
		return testFirst;
	}

	public void setTestFirst(Integer testFirst) {
		this.testFirst = testFirst;
	}

	public boolean isTestPublishFlag() {
		return testPublishFlag;
	}

	public void setTestPublishFlag(boolean testPublishFlag) {
		this.testPublishFlag = testPublishFlag;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public boolean isPublishQnaTemp() {
		return isPublishQnaTemp;
	}

	public void setPublishQnaTemp(boolean isPublishQnaTemp) {
		this.isPublishQnaTemp = isPublishQnaTemp;
	}

	public boolean isDisabledQna1() {
		return isDisabledQna1;
	}

	public void setDisabledQna1(boolean isDisabledQna1) {
		this.isDisabledQna1 = isDisabledQna1;
	}
	
}
