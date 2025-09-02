package com.wo.module.qaFE.bean;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;

import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.exception.CustomAPIException;
import com.wo.module.common.model.UploadedFileWO;
import com.wo.module.common.util.FileUtil;
import com.wo.module.common.utility.CallApiManager;
import com.wo.module.emailTemplate.model.EmailTemplate;
import com.wo.module.emailTemplate.service.EmailTemplateService;
import com.wo.module.engine.service.SendEmailService;
import com.wo.module.engine.vo.SendEmailVO;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.outgoingLetter.constant.OutgoingLetterConstants;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.qa.constant.QAConstants;
import com.wo.module.qa.model.QA;
import com.wo.module.qa.model.QAAttachment;
import com.wo.module.qaFE.constant.QAFEConstant;
import com.wo.module.qaFE.service.QAFEService;
import com.wo.module.qaFE.vo.QAFEAnswerVo;
import com.wo.module.user.model.User;

public class QAFEEditBean extends CommonBean implements Serializable{

	private static final long serialVersionUID = -4157661570004470164L;
	private static final Logger logger = Logger.getLogger(QAFEEditBean.class);
	private static final String NAVIGATE_BACK = QAFEConstant.NAVIGATE_QA_FE;
	
	private QAFEService qaFEService;
	
	private EmailTemplateService emailTemplateService;
	
	private SendEmailService sendEmailService;
	
	private QA qa;

	private String question;
	private String editId;
	private String categoryType;
	private String qDateStr;
	private String textWarningUpload;
	
	private List<QAFEAnswerVo> qaFEAnswerLists;
	
	private List<QA> qaFEQuestionLists;
	
	private FacesUtil facesUtil;
	private FileUtil fileUtil;
	
	private SimpleDateFormat sdf = new SimpleDateFormat("dd MMM YYYY HH:mm");
	
	private List<UploadedFileWO> uploadFiles;
	private List<UploadedFileWO> deleteFiles;
	
	private QAFEService qafeService;
	
	private boolean isShowQna1;
	private boolean isAdmin;
	private boolean isCanQuestion;
	
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
		fileUtil = FileUtil.getInstance();
		qaFEAnswerLists = new ArrayList<QAFEAnswerVo>();
		isShowQna1 = true;
		isCanQuestion = true;
		
		checkNewOrEdit();
		
		try {
			ParameterDetail getText = parameterDetailService.getParameterDetailByParamDtlCode("UPLOAD_WARNING_TEXT");
			textWarningUpload = getText.getName();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void checkNewOrEdit() {
		this.editId = facesUtil.retrieveRequestParam("editId");
		String token = facesUtil.retrieveRequestParam("token");
		if(StringUtils.isNotEmpty(token)) {
			editId = Constants.decryptString(token);
		}
		
		if (StringUtils.isBlank(editId)) {
			// do noting
		} else {
			handleEdit(editId);
		}
	}
	
	private void handleEdit(String editId) {
		try {
			Long editIdLong = Long.parseLong(editId);
			User userLogin =  facesUtil.getUserLogin();
			qa = qaFEService.findById(editIdLong);
			isAdmin = true;
			
			if(userLogin.getUserId().equals(qa.getqUser().getUserId()) && qa.getAnswer() != null){
				qa.setReadFlag(Constants.ENABLED_FLAG_TRUE);
				qaFEService.update(qa);
			}
			
			List<QA> questionList = qaFEService.getQAByParentId(qa.getQnaId());
			
			if(questionList.size()>0){
				for(int i=0; i<questionList.size();i++){
				QA qaNew = questionList.get(i);
				if(userLogin.getUserId().equals(qaNew.getqUser().getUserId()) && qaNew.getAnswer() != null){
					qaNew.setReadFlag(Constants.ENABLED_FLAG_TRUE);
					qaFEService.update(qaNew);
				}
				}
			}
			
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
				
				qaFEAnswerLists.add(vo);
			}
			
			qaFEQuestionLists = qaFEService.getQAByParentId(qa.getQnaId());
			List<QA> questionListTemp = new ArrayList<>();
			
			if(qaFEQuestionLists != null && !qaFEQuestionLists.isEmpty()){
				for (QA qa : qaFEQuestionLists) {
					if(userLogin.getUserId().equals(qa.getqUser().getUserId())){
						questionListTemp.add(qa);
					} else {
						if (!StringUtils.isEmpty(qa.getPublishQna()) && qa.getPublishQna().equals(Constants.CONSTANT_YES)) {
							questionListTemp.add(qa);
						}
					}
				}
				
				if (questionListTemp != null && !questionListTemp.isEmpty()) {
					for (QA qa : questionListTemp) {
						if (qa.getQaAttachmentList() != null && !qa.getQaAttachmentList().isEmpty()) {
							List<QAAttachment> qaAttachmentList = new ArrayList<>();
							
							for (QAAttachment qaAttachment : qa.getQaAttachmentList()) {
								if(userLogin.getUserId().equals(qa.getqUser().getUserId())){
									qaAttachmentList.add(qaAttachment);
								} else {
									if (!StringUtils.isEmpty(qaAttachment.getPublishAttachment()) && qaAttachment.getPublishAttachment().equals(Constants.CONSTANT_YES)) {
										qaAttachmentList.add(qaAttachment);
									}
								}
							}
							qa.setQaAttachmentList(qaAttachmentList);
						}
					}
				}
				
				qaFEQuestionLists = questionListTemp;
			}
			
			if (qa.getQaAttachmentList() != null && !qa.getQaAttachmentList().isEmpty()) {
				List<QAAttachment> qaAttachmentList = new ArrayList<>();
				
				for (QAAttachment qaAttachment : qa.getQaAttachmentList()) {
					if(userLogin.getUserId().equals(qa.getqUser().getUserId())){
						qaAttachmentList.add(qaAttachment);
					} else {
						if (!StringUtils.isEmpty(qaAttachment.getPublishAttachment()) && qaAttachment.getPublishAttachment().equals(Constants.CONSTANT_YES)) {
							qaAttachmentList.add(qaAttachment);
						}
					}
				}
				qa.setQaAttachmentList(qaAttachmentList);
			}
			
			Number admin = qafeService.getIsAdmin(facesUtil.getUserLogin().getUserId());
			
			if (admin == null) {
				admin = 0;
			}
			
			if (admin.longValue() == 0) {
				if(userLogin.getUserId().equals(qa.getqUser().getUserId())){
					isShowQna1 = true;
					isCanQuestion = true;
				} else {
					isCanQuestion = false;
					
					if (!StringUtils.isEmpty(qa.getPublishQna()) && qa.getPublishQna().equals(Constants.CONSTANT_YES)) {
						isShowQna1 = true;
					} else {
						isShowQna1 = false;
					}
				}
			} else {
				if (pdCatType != null) {
					Number adminByCategory = qafeService.getIsAdminByCategory(facesUtil.getUserLogin().getUserId(), pdCatType.getParameterDtlCode());
					
					if (adminByCategory == null) {
						adminByCategory = 0;
					}
					
					if (adminByCategory.longValue() == 0) {
						isCanQuestion = false;
					} else {
						isCanQuestion = true;
					}
				}
				
				if (isCanQuestion) {
					if(userLogin.getUserId().equals(qa.getqUser().getUserId())){
						isShowQna1 = true;
					} else {
						if (!StringUtils.isEmpty(qa.getPublishQna()) && qa.getPublishQna().equals(Constants.CONSTANT_YES)) {
							isShowQna1 = true;
						} else {
							isShowQna1 = false;
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private String generateTicketNo() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		String year = sdf.format(new Date());
		Long seqNo = qaFEService.getTicketNo().longValue();
		return year.concat("-").concat(Long.toString(seqNo));
	}
	
	public void cancel() {
		try {
			facesUtil.redirect("/pages/qaFE/qaFE.faces");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void save(){
			
			try {
			String question = facesUtil.retrieveRequestParam("QUESTION");
			
			if (StringUtils.isBlank(question)) {
				facesUtil.addErrMessage(facesUtil.retrieveMessage("formQAAdminQuestion"));
				PrimeFaces.current().ajax().update("form:messages");
			} else {
			
				QA qaNew = new QA();
				qaNew.setqTitle(qa.getqTitle());
				qaNew.setQuestion(question);
				qaNew.setTicketNo(qa.getTicketNo());
				qaNew.setqUser(facesUtil.getUserLogin());
				qaNew.setqDate(new Date());
				
				
				qaNew.setCategoryType(qa.getCategoryType());
				qaNew.setFromQnaId(qaFEService.findById(qa.getQnaId()));
				
				if(qaNew.getFromQnaId()!=null){
					QA qaParent = qaNew.getFromQnaId();
					qaParent.setqStatus(ParameterDetail.PARAM_DET_CODE_QNA_STATUS_IP);
					qaParent.setLastUpdateBy(facesUtil.retrieveUserLogin());
					qaParent.setLastUpdateDate(new Timestamp(new Date().getTime()));
					qaFEService.update(qaParent);
					qaNew.setqStatus(ParameterDetail.PARAM_DET_CODE_QNA_STATUS_IP);
				}else{
					qaNew.setqStatus(ParameterDetail.PARAM_DET_CODE_QNA_STATUS_IP);
				}
				
				qaNew.setCreatedBy(facesUtil.retrieveUserLogin());
				qaNew.setCreationDate(new Timestamp(new Date().getTime()));
				qaNew.setDelId(new Long(0));
				qaNew.setEnabledFlag(Constants.CONSTANT_YES);
				
				if (uploadFiles != null && !uploadFiles.isEmpty()) {
					if (qaNew.getQaAttachmentList() == null) {
						qaNew.setQaAttachmentList(new ArrayList<>());
					}
					
					for (int i = 0; i < uploadFiles.size(); i++) {
						QAAttachment qaAttachment = new QAAttachment();
						UploadedFileWO uf = uploadFiles.get(i);
						
						qaAttachment.setQa(qaNew);
						qaAttachment.setAttachmentFile(uf.getFileName());
						qaAttachment.setFileId(uf.getFileId());
						qaAttachment.setFileSize(uf.getFileSize());
						
						qaAttachment.setCreatedBy(facesUtil.retrieveUserLogin());
						qaAttachment.setCreationDate(new Timestamp(System.currentTimeMillis()));
						qaAttachment.setEnabledFlag(Constants.CONSTANT_YES);
						qaAttachment.setDelId(0l);
						
						qaNew.getQaAttachmentList().add(qaAttachment);
					}
				}
				
				qaFEService.save(qaNew);
				
				// this should be a singleton
		        ExecutorService emailExecutor = Executors.newCachedThreadPool();
	
		        // from you sendEmail() method
		        emailExecutor.execute(new Runnable() {
		            @Override
		            public void run() {
		                try {
		                	sendEmail(question);
		                } catch (Exception e) {
		                    logger.error("send email failed", e);
		                }
		            }
		        });
		        PrimeFaces.current().ajax().update("dialogSuccess");
		        PrimeFaces.current().executeScript("PF('dlg1').show();");
			}
			
			
			//facesUtil.redirect("/pages/qaFE/qaFE.faces");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
	
	public void sendEmail(String question) {
		try {
			
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
			EmailTemplate emailTemplate = emailTemplateService.getEmailTemplateByEmailTemplateCode(QAConstants.EMAIL_QA_QUESTION_TO_PIC);
			String emailSubject = emailTemplate.getEmailSubject();
			
			emailSubject = emailSubject.replaceAll("counter_type", "Notification");
			emailSubject = emailSubject.replaceAll("ticket_no",qa.getTicketNo());
	
			String emailContent = ""; 
			String emailTo  = "";
			String emailCc = "";
		    
			emailContent = emailTemplate.getEmailContent();
			ParameterDetail pd = parameterDetailService.getParameterDetailByParamDtlCode(qa.getCategoryType());
			emailContent = emailContent.replaceAll("division_name",pd.getNameIn());
			emailContent = emailContent.replaceAll("q_date",sdf.format(qa.getqDate()));
			emailContent = emailContent.replaceAll("q_name",qa.getqUser().getName());
			emailContent = emailContent.replaceAll("ticket_no",qa.getTicketNo());
			emailContent = emailContent.replaceAll("q_title",qa.getqTitle());
			emailContent = emailContent.replaceAll("q_question",question);
			
			List<SendEmailVO> list =  sendEmailService.getListEmailAdminByQnaCategory(qa.getCategoryType());
			for(int x=0;x<list.size();x++){
				SendEmailVO vo2 = list.get(x);
				if(emailTo.equals("")){
						emailTo = vo2.getEmailTo();
				}else{
					if(emailCc.equals("")){
						emailCc = vo2.getEmailTo();
					}else{
						emailCc = emailCc.concat(",").concat(vo2.getEmailTo());
					}
				}
			}
			
			CallApiManager.sendEmailAPI(emailTo,emailCc, emailSubject,
						emailContent, QAConstants.EMAIL_QA_QUESTION_TO_PIC, "true", parameterDetailService);
				
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new CustomAPIException(ex.getMessage()); 
		}
		
	}
	
	public void handleFileUpload (FileUploadEvent event) {
		try {
			uploadFiles = uploadFiles == null ? new ArrayList<UploadedFileWO>() : uploadFiles;
			uploadFiles.add(new UploadedFileWO( CallApiManager.callUploadAPI(event.getFile(), Constants.ARTICLE, parameterDetailService, false, fileUtil),
					event.getFile().getFileName(),event.getFile().getContentType(),event.getFile().getSize()));
		} catch (Exception e) {
			e.printStackTrace();
			facesUtil.addErrMessage(e.getMessage());
		}
	}
	
	public void deleteAttachment (String fileId, int index, String uploadType) throws Exception {
		deleteFiles = deleteFiles != null ? deleteFiles : new ArrayList<UploadedFileWO>();
		deleteFiles.add(new UploadedFileWO(fileId,null,null,null));
		
		if(uploadType != null && uploadType.equals(OutgoingLetterConstants.UPLOAD_TYPE_DOCUMENT)) {
			uploadFiles.remove(uploadFiles.get(index));
		}
	}
	
	public void downloadFile (String fileId, String fileName, byte[] content) throws Exception {
		CallApiManager.downloadFile(fileId, fileName, content, parameterDetailService);
	}
	

	public QAFEService getQaFEService() {
		return qaFEService;
	}

	public void setQaFEService(QAFEService qaFEService) {
		this.qaFEService = qaFEService;
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

	public List<QA> getQaFEQuestionLists() {
		return qaFEQuestionLists;
	}

	public void setQaFEQuestionLists(List<QA> qaFEQuestionLists) {
		this.qaFEQuestionLists = qaFEQuestionLists;
	}

	public SimpleDateFormat getSdf() {
		return sdf;
	}

	public void setSdf(SimpleDateFormat sdf) {
		this.sdf = sdf;
	}

	public EmailTemplateService getEmailTemplateService() {
		return emailTemplateService;
	}

	public void setEmailTemplateService(EmailTemplateService emailTemplateService) {
		this.emailTemplateService = emailTemplateService;
	}

	public SendEmailService getSendEmailService() {
		return sendEmailService;
	}

	public void setSendEmailService(SendEmailService sendEmailService) {
		this.sendEmailService = sendEmailService;
	}

	public String getTextWarningUpload() {
		return textWarningUpload;
	}

	public void setTextWarningUpload(String textWarningUpload) {
		this.textWarningUpload = textWarningUpload;
	}

	public FileUtil getFileUtil() {
		return fileUtil;
	}

	public void setFileUtil(FileUtil fileUtil) {
		this.fileUtil = fileUtil;
	}

	public List<UploadedFileWO> getUploadFiles() {
		return uploadFiles;
	}

	public void setUploadFiles(List<UploadedFileWO> uploadFiles) {
		this.uploadFiles = uploadFiles;
	}

	public List<UploadedFileWO> getDeleteFiles() {
		return deleteFiles;
	}

	public void setDeleteFiles(List<UploadedFileWO> deleteFiles) {
		this.deleteFiles = deleteFiles;
	}

	public boolean isShowQna1() {
		return isShowQna1;
	}

	public void setShowQna1(boolean isShowQna1) {
		this.isShowQna1 = isShowQna1;
	}

	public QAFEService getQafeService() {
		return qafeService;
	}

	public void setQafeService(QAFEService qafeService) {
		this.qafeService = qafeService;
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public boolean isCanQuestion() {
		return isCanQuestion;
	}

	public void setCanQuestion(boolean isCanQuestion) {
		this.isCanQuestion = isCanQuestion;
	}
	
}
