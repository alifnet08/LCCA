package com.wo.module.correspondenceFE.bean;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.primefaces.event.FileUploadEvent;

import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.exception.CustomAPIException;
import com.wo.module.common.model.UploadedFileWO;
import com.wo.module.common.util.FileUtil;
import com.wo.module.common.utility.CallApiManager;
import com.wo.module.emailTemplate.model.EmailTemplate;
import com.wo.module.emailTemplate.service.EmailTemplateService;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.lov.bean.SelectorModel;
import com.wo.module.lov.bean.SelectorModel.SelectorInfo;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.parameter.model.ParameterHeader;
import com.wo.module.trcCorrespondence.constant.TrcCorrespondenceConstants;
import com.wo.module.trcCorrespondence.model.TrcCorrespondence;
import com.wo.module.trcCorrespondence.model.TrcCorrespondencePicCompliance;
import com.wo.module.trcCorrespondence.model.TrcCorrespondencePicFollowupAttendance;
import com.wo.module.trcCorrespondence.model.TrcCorrespondencePicFollowupAttendanceTableModel;
import com.wo.module.trcCorrespondence.service.TrcCorrespondenceService;
import com.wo.module.user.service.UserService;

public class CorrespondenceFEVerificationBean extends CommonBean implements  Serializable {

	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(CorrespondenceFEVerificationBean.class);

	private TrcCorrespondence trcCorrespondence;
	
	private List<UploadedFileWO> deletedFiles;
	
	private List<UploadedFileWO> uploadedFilesDocument;
	
	private List<SelectItem> attendanceList;
	
	private List<SelectItem> yesNoList;
	
	private Integer lastSequenceOfPicAttendance;
	
	private Integer indexDtlPicAttendance;
	
	private TrcCorrespondencePicFollowupAttendanceTableModel<TrcCorrespondencePicFollowupAttendance> tableAttedanceModel;
	
	private TrcCorrespondencePicFollowupAttendance[] selectedPicAttendanceData;
	
	private SelectorInfo selectorPicAttendance;
	
	private FacesUtil facesUtil;
	
	private List<UploadedFileWO> uploadedFilesEvidence;
	
	private FileUtil fileUtil;
	
	private TrcCorrespondenceService trcCorrespondenceService;
	
	private UserService userService;
	
	private EmailTemplateService emailTemplateService;
	
	private SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", new Locale("id", "ID"));
	
	
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
		initList();
		selectorPicAttendance = buildSelectorPICAttendace();
		handleEdit();
		fileUtil = FileUtil.getInstance();
	}
	
	public void initList(){
		try{
			attendanceList = new ArrayList<SelectItem>();
			List<ParameterDetail> listAttendance = parameterDetailService.getParameterDetailByParamCode(ParameterHeader.PARAM_HEAD_CODE_ATTENDANCE);
			for (ParameterDetail vo : listAttendance) {
				SelectItem si =  new SelectItem();
				si.setLabel(vo.getName());
				si.setValue(vo.getParameterDtlCode());
				attendanceList.add(si);
			}
			
			yesNoList = new ArrayList<SelectItem>();
			yesNoList.add(new SelectItem(Constants.CONSTANT_YES, "Ya"));
			yesNoList.add(new SelectItem(Constants.CONSTANT_NO, "Tidak"));
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	
	private void handleEdit() {
		try {
		String editId = facesUtil.retrieveRequestParam("id");
		String token = facesUtil.retrieveRequestParam("token");
		if(StringUtils.isNotEmpty(token)) {
			editId = Constants.decryptString(token);
		}
		Long idLong = Long.parseLong(editId);
		trcCorrespondence = trcCorrespondenceService.findById(idLong);
		trcCorrespondence.setPicFollowupStatus(new ParameterDetail());
		
		tableAttedanceModel = new TrcCorrespondencePicFollowupAttendanceTableModel<TrcCorrespondencePicFollowupAttendance>(
				trcCorrespondence.getTrcCorrespondencePicFollowupAttendance());
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}
	
	
	public static SelectorInfo buildSelectorPICAttendace() {
		SelectorModel.SelectorInfo info = new SelectorModel.SelectorInfo(
                " select USER_ID, NIK, NAME, division_name from wo_mst_user "
                + " where 1=1 "
                + " and ( upper(NAME) like upper('%{0}%') or upper(NIK) like upper('%{0}%') or upper(division_name) like upper('%{0}%') ) "
                + " and enabled_flag = 'Y' "
                + " order by NAME "  ,
                " SELECT COUNT(1) from wo_mst_user "
                + " where 1=1 "
                + " and ( upper(NAME) like upper('%{0}%') or upper(NIK) like upper('%{0}%') or upper(division_name) like upper('%{0}%') ) "
                + " and enabled_flag = 'Y' ",
                Arrays.asList("NPK", "Name", "Division Name"),
                Arrays.asList("1", "2", "3"),false);
        return info;
	}
	
	
	public void handleFileUploadEvidence(FileUploadEvent event) throws Exception {
		try {
		uploadedFilesEvidence = uploadedFilesEvidence == null ? new ArrayList<UploadedFileWO>() : uploadedFilesEvidence;
		uploadedFilesEvidence.add(new UploadedFileWO(CallApiManager.callUploadAPI(event.getFile(),
				Constants.COMPLIANCE_DOC_TYPE_DOKUMEN_TINDAK_LANJUT_DARI_PIC, parameterDetailService, false, fileUtil),
				event.getFile().getFileName(), event.getFile().getContentType(), event.getFile().getSize()));
		} catch (Exception e) {
			e.printStackTrace();
			facesUtil.addErrMessage(e.getMessage());
		}
	}
	
	public void deleteAttachment(String fileId,int index,String uploadType) throws Exception {
		deletedFiles = deletedFiles!=null?deletedFiles: new ArrayList<UploadedFileWO>();
		deletedFiles.add(new UploadedFileWO(fileId,null,null, null));
		if(uploadType!=null && uploadType.equals(TrcCorrespondenceConstants.UPLOAD_TYPE_DOCUMENT)) {
			uploadedFilesDocument.remove(uploadedFilesDocument.get(index));
		}else if(uploadType!=null && uploadType.equals(TrcCorrespondenceConstants.UPLOAD_TYPE_EVIDENCE)) {
			uploadedFilesEvidence.remove(uploadedFilesEvidence.get(index));
		}
	}
	
	public void downloadFile(String fileId, String fileName, byte[] content) throws Exception {
		CallApiManager.downloadFile(fileId, fileName, content, parameterDetailService);
	}
	
	public void onDeleteRowPicAttendance() {
		for (int i = 0; i < selectedPicAttendanceData.length; i++) {
			trcCorrespondence.getTrcCorrespondencePicFollowupAttendance().remove(selectedPicAttendanceData[i]);
		}
		
		if (trcCorrespondence.getTrcCorrespondencePicFollowupAttendance() == null
				|| trcCorrespondence.getTrcCorrespondencePicFollowupAttendance().size() == 0) {
			lastSequenceOfPicAttendance = 0;
		}
		
		tableAttedanceModel.setWrappedData(trcCorrespondence.getTrcCorrespondencePicFollowupAttendance());
	}
	
	public void onAddNewPicAttendance() {
		if (trcCorrespondence.getTrcCorrespondencePicFollowupAttendance() == null) {
			trcCorrespondence.setTrcCorrespondencePicFollowupAttendance(new ArrayList<TrcCorrespondencePicFollowupAttendance>());
			lastSequenceOfPicAttendance = 0;
		} else {
			if(trcCorrespondence.getTrcCorrespondencePicFollowupAttendance().size() == 0) {
				lastSequenceOfPicAttendance = 0;
			}			
		} 
		
		TrcCorrespondencePicFollowupAttendance d = new TrcCorrespondencePicFollowupAttendance();
		lastSequenceOfPicAttendance = lastSequenceOfPicAttendance + 1;
		d.setSequence(lastSequenceOfPicAttendance);
		trcCorrespondence.getTrcCorrespondencePicFollowupAttendance().add(d);
		tableAttedanceModel.setWrappedData(trcCorrespondence.getTrcCorrespondencePicFollowupAttendance());

	}
	
	public Boolean validate() {
		Boolean flag = false;
		
		if(trcCorrespondence.getCorrespondenceCode().getParameterDtlCode().equals(TrcCorrespondenceConstants.PARAM_DETAIL_CORRESPONDEN_TYPE_INVITATION)) {
			if(StringUtils.isEmpty(trcCorrespondence.getPicFollowupStatus().getParameterDtlCode())) {
				facesUtil.addErrMessage("Tindak Lanjut "+ facesUtil.retrieveMessage("validateRequired"));
				flag = true;
			}
			
		}

		return flag;
	}
	
	public void save() {
		
		
		try {
			
			
			String tindakLanjut = facesUtil.retrieveRequestParam("tindakLanjut");
			
		
			
			if(!StringUtils.isEmpty(tindakLanjut)){
				ParameterDetail picFollowupStatus = parameterDetailService.getParameterDetailByParamDtlCode(tindakLanjut);
				trcCorrespondence.setPicFollowupStatus(picFollowupStatus);
			}

			if (!validate()) {
				if (trcCorrespondence.getCorrespondenceId() != null) {
					trcCorrespondence.setLastUpdateBy(facesUtil.retrieveUserLogin());
					trcCorrespondence.setLastUpdateDate(new Timestamp(new Date().getTime()));
					trcCorrespondence.setDelId(new Long(0));
					trcCorrespondence.setEnabledFlag(Constants.CONSTANT_YES);
					trcCorrespondenceService.update(trcCorrespondence);
				}
				

				facesUtil.redirect("/pages/correspondenceFE/correspondenceFE.faces");
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			facesUtil.addFacesMsg(FacesMessage.SEVERITY_ERROR, null, "Operation Failed : " + ex.getMessage(), "");
		}

	}
	
	public void cancel() {
		try {
			
			facesUtil.redirect("/pages/correspondenceFE/correspondenceFE.faces");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void sendEmail() {
		try {
			
			EmailTemplate emailTemplate = emailTemplateService.getEmailTemplateByEmailTemplateCode("EMAIL_PIC_COMPLIANCE");
			String emailSubject = emailTemplate.getEmailSubject();
	
			String emailContent = ""; 
			String emailTo  = "";
			String emailCc = "";
		    
//					emailContent = emailTemplate.getEmailContent().replace("confirmation_type", "Korespondensi");
			emailContent = emailTemplate.getEmailContent().replace(Constants.CONFIRMATION_TYPE_AND_DOC_NUM, "Korespondensi" + " - " + trcCorrespondence.getLetterNo());
			emailSubject = emailSubject.replace(Constants.CONFIRMATION_TYPE_AND_DOC_NUM, "Korespondensi" + " - " + trcCorrespondence.getLetterNo());
					
					for(int x=0;x<trcCorrespondence.getTrcCorrespondencePicCompliances().size();x++) {
						TrcCorrespondencePicCompliance cd = trcCorrespondence.getTrcCorrespondencePicCompliances().get(x);
						emailTo = cd.getUser().getEmail();
						
//						ExecutorService emailExecutor = Executors.newCachedThreadPool();

						final String subject = emailSubject;
						final String content = emailContent;
						final String to = emailTo;
						//final String to = "h3ndr407@gmail.com";
						final String cc = emailCc;
						
						CallApiManager.sendEmailAPI(to, cc, subject, content, "EMAIL_SOSIALISASI", "true", parameterDetailService);
						
//				        emailExecutor.execute(new Runnable() {
//				            @Override
//				            public void run() {
//								
//				            	try {
//									CallApiManager.sendEmail(subject, content, to,cc, parameterDetailService);
//									 
//								} catch (Exception e) {
//									e.printStackTrace();
//								}
//				            }
//				        });
				        
					}
		    
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new CustomAPIException(ex.getMessage()); 
		}
		
	}
	
	
	public FacesUtil getFacesUtil() {
		return facesUtil;
	}

	public void setFacesUtil(FacesUtil facesUtil) {
		this.facesUtil = facesUtil;
	}

	

	public TrcCorrespondenceService getTrcCorrespondenceService() {
		return trcCorrespondenceService;
	}

	public void setTrcCorrespondenceService(TrcCorrespondenceService trcCorrespondenceService) {
		this.trcCorrespondenceService = trcCorrespondenceService;
	}

	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		CorrespondenceFEEditBean.logger = logger;
	}


	public TrcCorrespondence getTrcCorrespondence() {
		return trcCorrespondence;
	}

	public void setTrcCorrespondence(TrcCorrespondence trcCorrespondence) {
		this.trcCorrespondence = trcCorrespondence;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	
	public List<UploadedFileWO> getUploadedFilesEvidence() {
		return uploadedFilesEvidence;
	}

	public void setUploadedFilesEvidence(List<UploadedFileWO> uploadedFilesEvidence) {
		this.uploadedFilesEvidence = uploadedFilesEvidence;
	}

	public FileUtil getFileUtil() {
		return fileUtil;
	}

	public void setFileUtil(FileUtil fileUtil) {
		this.fileUtil = fileUtil;
	}

	public List<UploadedFileWO> getDeletedFiles() {
		return deletedFiles;
	}

	public void setDeletedFiles(List<UploadedFileWO> deletedFiles) {
		this.deletedFiles = deletedFiles;
	}

	public List<UploadedFileWO> getUploadedFilesDocument() {
		return uploadedFilesDocument;
	}

	public void setUploadedFilesDocument(List<UploadedFileWO> uploadedFilesDocument) {
		this.uploadedFilesDocument = uploadedFilesDocument;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public EmailTemplateService getEmailTemplateService() {
		return emailTemplateService;
	}

	public void setEmailTemplateService(EmailTemplateService emailTemplateService) {
		this.emailTemplateService = emailTemplateService;
	}

	public List<SelectItem> getAttendanceList() {
		return attendanceList;
	}

	public void setAttendanceList(List<SelectItem> attendanceList) {
		this.attendanceList = attendanceList;
	}

	public List<SelectItem> getYesNoList() {
		return yesNoList;
	}

	public void setYesNoList(List<SelectItem> yesNoList) {
		this.yesNoList = yesNoList;
	}

	public TrcCorrespondencePicFollowupAttendanceTableModel<TrcCorrespondencePicFollowupAttendance> getTableAttedanceModel() {
		return tableAttedanceModel;
	}

	public void setTableAttedanceModel(
			TrcCorrespondencePicFollowupAttendanceTableModel<TrcCorrespondencePicFollowupAttendance> tableAttedanceModel) {
		this.tableAttedanceModel = tableAttedanceModel;
	}

	public Integer getLastSequenceOfPicAttendance() {
		return lastSequenceOfPicAttendance;
	}

	public void setLastSequenceOfPicAttendance(Integer lastSequenceOfPicAttendance) {
		this.lastSequenceOfPicAttendance = lastSequenceOfPicAttendance;
	}

	public TrcCorrespondencePicFollowupAttendance[] getSelectedPicAttendanceData() {
		return selectedPicAttendanceData;
	}

	public void setSelectedPicAttendanceData(TrcCorrespondencePicFollowupAttendance[] selectedPicAttendanceData) {
		this.selectedPicAttendanceData = selectedPicAttendanceData;
	}

	
	

	public Integer getIndexDtlPicAttendance() {
		return indexDtlPicAttendance;
	}

	public void setIndexDtlPicAttendance(Integer indexDtlPicAttendance) {
		this.indexDtlPicAttendance = indexDtlPicAttendance;
	}

	public SelectorInfo getSelectorPicAttendance() {
		return selectorPicAttendance;
	}

	public void setSelectorPicAttendance(SelectorInfo selectorPicAttendance) {
		this.selectorPicAttendance = selectorPicAttendance;
	}

	public SimpleDateFormat getSdf() {
		return sdf;
	}

	public void setSdf(SimpleDateFormat sdf) {
		this.sdf = sdf;
	}
	

	

	
	
	

}