package com.wo.module.qaFE.bean;

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
import javax.faces.model.SelectItem;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;

import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.CommonConstants;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.exception.CustomAPIException;
import com.wo.module.common.model.UploadedFileWO;
import com.wo.module.common.paging.DefaultSearchObject;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.common.util.FileUtil;
import com.wo.module.common.utility.CallApiManager;
import com.wo.module.emailTemplate.model.EmailTemplate;
import com.wo.module.emailTemplate.service.EmailTemplateService;
import com.wo.module.engine.service.SendEmailService;
import com.wo.module.engine.vo.SendEmailVO;
import com.wo.module.log.service.LogService;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.outgoingLetter.constant.OutgoingLetterConstants;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.parameter.model.ParameterHeader;
import com.wo.module.qa.constant.QAConstants;
import com.wo.module.qa.model.QA;
import com.wo.module.qa.model.QAAttachment;
import com.wo.module.qaFE.constant.QAFEConstant;
import com.wo.module.qaFE.service.QABertanyaFEService;
import com.wo.module.qaFE.service.QAFEService;
import com.wo.module.user.model.User;

public class QAFEBertanyaBean extends CommonBean implements Serializable{

	private static final long serialVersionUID = 8494448970412266711L;
	private static final Logger logger = Logger.getLogger(QAFEBertanyaBean.class);
	private static final String NAVIGATE_BACK = QAFEConstant.NAVIGATE_QA_FE;
	
	private QAFEService qafeService;
	
	private QABertanyaFEService qaBertanyaFEService;
	
	private EmailTemplateService emailTemplateService;
	
	private SendEmailService sendEmailService;
	
	private LogService logService;
	
	private QA qa;
	private User user;
	
	private String categoryType;
	private String title;
	private String question;
	private String qnaWarning;
	private String textWarningUpload;
	
	private List<SelectItem> categoryTypeList;
	
	private List<UploadedFileWO> uploadFiles;
	private List<UploadedFileWO> deleteFiles;
	
	@SuppressWarnings("rawtypes")
	private List listData;
	
	private FacesUtil facesUtil;
	private FileUtil fileUtil;
	
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
		initComponent();
		
		checkNew();
		setWarningQnAText();
		fileUtil = FileUtil.getInstance();
		user = facesUtil.getUserLogin();
		
		try {
			ParameterDetail getText = parameterDetailService.getParameterDetailByParamDtlCode("UPLOAD_WARNING_TEXT");
			textWarningUpload = getText.getName();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void initComponent() {
		initSelectCategoryTypeList();
	}
	
	private void setWarningQnAText() {
		try {
			ParameterDetail pd = parameterDetailService.getParameterDetailByParamDtlCode(ParameterDetail.PARAM_DET_CODE_QNA_WARNING_TEXT);
			qnaWarning =  pd.getNameIn();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initSelectCategoryTypeList() {
		categoryTypeList = new ArrayList<SelectItem>();
		try {
			List<ParameterDetail> pd = parameterDetailService.getParameterDetailByParamCode(ParameterHeader.PARAM_QNA_CATEGORY);
			for (ParameterDetail pd1 : pd) {
				SelectItem si = new SelectItem();
				si.setLabel(pd1.getName());
				si.setValue(pd1.getParameterDtlCode());
				categoryTypeList.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("rawtypes")
	public void search(){
		String question = facesUtil.retrieveRequestParam("QUESTION");
		if (question != null && question.length()>=3) {
			List<SearchObject> searchCriteria = new ArrayList<SearchObject>();
			searchCriteria.add(new DefaultSearchObject(CommonConstants.SEARCH_BY_TEXT_BOX, question));
			try {
				listData = qaBertanyaFEService.searchData(searchCriteria, 0, 5, null, null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			listData = new ArrayList<>();
		}
		
	}
	
	private void checkNew() {
		qa = new QA();
	}
	
	public boolean isValidate() {
		boolean flag = true;
		
		return flag;
	}
	
	@SuppressWarnings("deprecation")
	public void save() {
		/*String functionId = "Save Tanya Kami";
		String userId = facesUtil.retrieveUserLogin();
		LogHeader logH = new LogHeader(DateUtil.currentSqlTimestamp(), functionId,
				ParameterDetail.PARAM_DET_LOG_PROCESS_STATUS_S, userId);
		List<LogDetail> list = new ArrayList<LogDetail>();
		int x = 1;*/
		try {
			
			/*String location = "Save Tanya Kami";
			String msg = " CategoryType : "+categoryType;
			
			LogDetail logD = new LogDetail(logH, new Long(x++), "INF", location, msg);
			list.add(logD);*/
				
				/*if (!StringUtils.isEmpty(categoryType)) {
					ParameterDetail pd = parameterDetailService.getParameterDetailByParamDtlCode(categoryType.trim());
					qa.setCategoryType(pd.getParameterDtlCode());
					
					msg = " categoryType from param Dtl :"+pd.getParameterDtlCode();
					LogDetail logD2 = new LogDetail(logH, new Long(x++), "INF", location, msg);
					list.add(logD2);
				}*/
				
					if (StringUtils.isBlank(categoryType)) {
						facesUtil.addErrMessage(facesUtil.retrieveMessage("formReportQnACategory") + " "
								+ facesUtil.retrieveMessage("validateRequired"));
						return;
					}
					
					if (StringUtils.isBlank(title)) {
						facesUtil.addErrMessage("Judul "
								+ facesUtil.retrieveMessage("validateRequired"));
						return;
					}
					
					if (StringUtils.isBlank(question)) {
						facesUtil.addErrMessage("Pertanyaan "
								+ facesUtil.retrieveMessage("validateRequired"));
						return;
					}
			
				qa.setCategoryType(categoryType);
			
				qa.setqTitle(title);
				
				qa.setQuestion(question);
				
				qa.setTicketNo(generateTicketNo());
				/*msg = " ticket No  :"+qa.getTicketNo();
				LogDetail logD3 = new LogDetail(logH, new Long(x++), "INF", location, msg);
				list.add(logD3);*/
				
				qa.setqUser(facesUtil.getUserLogin());
				
				qa.setqDate(new Date());
				qa.setqStatus(ParameterDetail.PARAM_DET_CODE_QNA_STATUS_NEW);
				
				qa.setCreatedBy(facesUtil.retrieveUserLogin());
				qa.setCreationDate(new Timestamp(new Date().getTime()));
				qa.setDelId(new Long(0));
				qa.setEnabledFlag(Constants.CONSTANT_YES);
				
				/*msg = " qUser  :"+qa.getqUser().getNik()+"\n qStatus :"+qa.getqStatus();
				LogDetail logD4 = new LogDetail(logH, new Long(x++), "INF", location, msg);
				list.add(logD4);*/
				
				if (uploadFiles != null && !uploadFiles.isEmpty()) {
					if (qa.getQaAttachmentList() == null) {
						qa.setQaAttachmentList(new ArrayList<>());
					}
					
					for (int i = 0; i < uploadFiles.size(); i++) {
						QAAttachment qaAttachment = new QAAttachment();
						UploadedFileWO uf = uploadFiles.get(i);
						
						qaAttachment.setQa(qa);
						qaAttachment.setAttachmentFile(uf.getFileName());
						qaAttachment.setFileId(uf.getFileId());
						qaAttachment.setFileSize(uf.getFileSize());
						
						qaAttachment.setCreatedBy(facesUtil.retrieveUserLogin());
						qaAttachment.setCreationDate(new Timestamp(System.currentTimeMillis()));
						qaAttachment.setEnabledFlag(Constants.CONSTANT_YES);
						qaAttachment.setDelId(0l);
						
						qa.getQaAttachmentList().add(qaAttachment);
					}
				}
				
				qafeService.save(qa);
				
				// this should be a singleton
		        ExecutorService emailExecutor = Executors.newCachedThreadPool();

		        // from you sendEmail() method
		        emailExecutor.execute(new Runnable() {
		            @Override
		            public void run() {
		                try {
		                	sendEmail();
		                } catch (Exception e) {
		                    logger.error("send email failed", e);
		                }
		            }
		        });
		        
				//facesUtil.redirect("/pages/qaFE/qaFE.faces");
		        PrimeFaces.current().executeScript("PF('dlg1').show();");
		} catch (Exception e) {
			
			/*String location = "Save Tanya Kami";
			
			String msg = ExceptionUtils.getStackTrace(e);
			
			if(msg.length()>1000) {
				msg = msg.substring(0,1000);
			}
			
			LogDetail logD = new LogDetail(logH, new Long(x++), "ERR", location, msg);
			list.add(logD);*/
			
			e.printStackTrace();
		}
		
		/*logH.setLogDetailList(list);
		
		try {
			logService.save(logH);
		} catch (ConstraintViolationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (HibernateException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}*/
	}
	
	public void sendEmail() {
		try {
			
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
			EmailTemplate emailTemplate = emailTemplateService.getEmailTemplateByEmailTemplateCode(QAConstants.EMAIL_QA_QUESTION_TO_PIC);
			String emailSubject = emailTemplate.getEmailSubject();
			
			emailSubject = emailSubject.replaceAll("counter_type", "Notification");
			emailSubject = emailSubject.replaceAll("ticket_no", qa.getTicketNo());
	
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
			emailContent = emailContent.replaceAll("q_question",qa.getQuestion());
			
			
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
	
	public void cancel() {
		try {
			facesUtil.redirect("/pages/qaFE/qaFE.faces");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private String numberToString(Integer num){
		
		if(num >0 && num <10){
			return "0"+num;
		}else {
			return num.toString();
		}
	}
	
	private String generateTicketNo() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
		String year = sdf.format(new Date());
		String seqNo = qafeService.getLastTicketNo();
		
		if(!StringUtils.isEmpty(seqNo)){
			String data[]= seqNo.split("-");
			if(data[0].equals(year)){
				String alpha = data[1].substring(0,1);		
				Integer num = Integer.parseInt(data[1].substring(1));
				
				Number rowFull = 0;
				if(num == 99) {
					rowFull = qafeService.getCheckTiket(year.concat("-").concat(alpha).concat(numberToString(num+1)));	
					if(rowFull.intValue() >= 1) {
						num = num+1;
						//return year.concat("-").concat(alpha).concat(numberToString(num)); 
					}
				}
				
				if(num < 100){
					num = num+1;
					return year.concat("-").concat(alpha).concat(numberToString(num)); 
				}else{
					if(alpha.equals("A")){
						alpha = "B";
					}
					else if(alpha.equals("B")){
						alpha = "C";
					}
					else if(alpha.equals("C")){
						alpha = "D";
					}
					else if(alpha.equals("D")){
						alpha = "E";
					}
					else if(alpha.equals("E")){
						alpha = "F";
					}
					else if(alpha.equals("F")){
						alpha = "G";
					}
					else if(alpha.equals("G")){
						alpha = "H";
					}
					else if(alpha.equals("H")){
						alpha = "I";
					}
					else if(alpha.equals("I")){
						alpha = "J";
					}
					else if(alpha.equals("J")){
						alpha = "K";
					}
					else if(alpha.equals("K")){
						alpha = "L";
					}
					else if(alpha.equals("L")){
						alpha = "M";
					}
					else if(alpha.equals("M")){
						alpha = "N";
					}
					else if(alpha.equals("N")){
						alpha = "O";
					}
					else if(alpha.equals("O")){
						alpha = "P";
					}
					else if(alpha.equals("P")){
						alpha = "Q";
					}
					else if(alpha.equals("Q")){
						alpha = "R";
					}
					else if(alpha.equals("R")){
						alpha = "S";
					}
					else if(alpha.equals("S")){
						alpha = "T";
					}
					else if(alpha.equals("T")){
						alpha = "U";
					}
					else if(alpha.equals("U")){
						alpha = "V";
					}
					else if(alpha.equals("V")){
						alpha = "W";
					}
					else if(alpha.equals("W")){
						alpha = "X";
					}
					else if(alpha.equals("X")){
						alpha = "Y";
					}
					else if(alpha.equals("Y")){
						alpha = "Z";
					}
					return year.concat("-").concat(alpha).concat("01");
				}
			}else{
				return year.concat("-").concat("A01");
			}
		}else{
				return year.concat("-").concat("A01");
		}
		
		
	}
	
	public String toEncrypt(Long qaid){
		try {
			return Constants.encryptString(qaid.toString());
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	public String toEncrypt(Long id,String dataType){
		try {
			ParameterDetail pd = parameterDetailService.getParameterDetailByParamDtlCode("HOST_NAME_APPLICATION");
			if(dataType.equals("FAQ")){
				return pd.getNameIn().concat("pages/faqFE/faqFE.faces?token=").concat(Constants.encryptString(id.toString()));
			}else{
				return pd.getNameIn().concat("pages/qaFE/qaFEEdit.faces?token=").concat(Constants.encryptString(id.toString()));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return "";
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
	
	
	public QAFEService getQafeService() {
		return qafeService;
	}

	public void setQafeService(QAFEService qafeService) {
		this.qafeService = qafeService;
	}

	public QA getQa() {
		return qa;
	}

	public void setQa(QA qa) {
		this.qa = qa;
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

	public List<SelectItem> getCategoryTypeList() {
		return categoryTypeList;
	}

	public void setCategoryTypeList(List<SelectItem> categoryTypeList) {
		this.categoryTypeList = categoryTypeList;
	}

	public String getCategoryType() {
		return categoryType;
	}

	public void setCategoryType(String categoryType) {
		this.categoryType = categoryType;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public QABertanyaFEService getQaBertanyaFEService() {
		return qaBertanyaFEService;
	}

	public void setQaBertanyaFEService(QABertanyaFEService qaBertanyaFEService) {
		this.qaBertanyaFEService = qaBertanyaFEService;
	}

	@SuppressWarnings("rawtypes")
	public List getListData() {
		return listData;
	}

	@SuppressWarnings("rawtypes")
	public void setListData(List listData) {
		this.listData = listData;
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

	public String getQnaWarning() {
		return qnaWarning;
	}

	public void setQnaWarning(String qnaWarning) {
		this.qnaWarning = qnaWarning;
	}

	public LogService getLogService() {
		return logService;
	}

	public void setLogService(LogService logService) {
		this.logService = logService;
	}

	public String getTextWarningUpload() {
		return textWarningUpload;
	}

	public void setTextWarningUpload(String textWarningUpload) {
		this.textWarningUpload = textWarningUpload;
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

	public FileUtil getFileUtil() {
		return fileUtil;
	}

	public void setFileUtil(FileUtil fileUtil) {
		this.fileUtil = fileUtil;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
