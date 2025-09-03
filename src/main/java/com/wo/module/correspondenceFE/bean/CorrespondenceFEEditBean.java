package com.wo.module.correspondenceFE.bean;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
import com.wo.module.common.util.MathUtil;
import com.wo.module.common.utility.CallApiManager;
import com.wo.module.emailTemplate.model.EmailTemplate;
import com.wo.module.emailTemplate.service.EmailTemplateService;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.lov.bean.SelectorListener;
import com.wo.module.lov.bean.SelectorModel;
import com.wo.module.lov.bean.SelectorModel.SelectorInfo;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.parameter.model.ParameterHeader;
import com.wo.module.trcCorrespondence.constant.TrcCorrespondenceConstants;
import com.wo.module.trcCorrespondence.model.TrcCorrespondence;
import com.wo.module.trcCorrespondence.model.TrcCorrespondenceDocument;
import com.wo.module.trcCorrespondence.model.TrcCorrespondencePicCompliance;
import com.wo.module.trcCorrespondence.model.TrcCorrespondencePicFollowupAttachment;
import com.wo.module.trcCorrespondence.model.TrcCorrespondencePicFollowupAttendance;
import com.wo.module.trcCorrespondence.model.TrcCorrespondencePicFollowupAttendanceTableModel;
import com.wo.module.trcCorrespondence.service.TrcCorrespondenceService;
import com.wo.module.user.model.User;
import com.wo.module.user.service.UserService;

public class CorrespondenceFEEditBean extends CommonBean implements SelectorListener<Object>,  Serializable {

	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(CorrespondenceFEEditBean.class);

	private TrcCorrespondence trcCorrespondence;
	
	private List<UploadedFileWO> deletedFiles;
	
	private List<UploadedFileWO> uploadedFilesDocument;
	
	private List<UploadedFileWO> filesDocument;
	
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
	
	private String textWarningUpload;
	
	private Integer testFirst;
	
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
		testFirst = Integer.parseInt(facesUtil.retrieveRequestParam("first")!=null?facesUtil.retrieveRequestParam("first"):"0");
		facesUtil.setSessionAttribute("FIRST_CORRES_FE", testFirst);
		facesUtil.setSessionAttribute("BACK_SESSION", false);
		initList();
		selectorPicAttendance = buildSelectorPICAttendace();
		handleEdit();
		fileUtil = FileUtil.getInstance();
		
		try {
			ParameterDetail getText = parameterDetailService.getParameterDetailByParamDtlCode("UPLOAD_WARNING_TEXT");
			textWarningUpload = getText.getName();
		} catch (Exception e) {
			e.printStackTrace();
		}
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
			yesNoList.add(new SelectItem(Constants.CONSTANT_YES, "Yes"));
			yesNoList.add(new SelectItem(Constants.CONSTANT_NO, "No"));
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	
	private void handleEdit() {
		try {
		String editId = facesUtil.retrieveRequestParam("id");
		String token = facesUtil.retrieveRequestParam("token");
		if (StringUtils.isNotEmpty(token)) {
			editId = Constants.decryptString(token);
		}
		Long idLong = Long.parseLong(editId);
		trcCorrespondence = trcCorrespondenceService.findById(idLong);
		trcCorrespondence.setNotes(trcCorrespondence.getNotesDecrypted());
		
		if(trcCorrespondence.getFollowupStatus() == null){trcCorrespondence.setFollowupStatus(new ParameterDetail());}
		
		for(int i=0;i<trcCorrespondence.getTrcCorrespondencePicFollowupAttendance().size();i++){
			TrcCorrespondencePicFollowupAttendance vo = trcCorrespondence.getTrcCorrespondencePicFollowupAttendance().get(i);
			if(vo.getUserId()!=null){
				vo.setUserNIK(vo.getUserId().getNik()); 
				vo.setUserName(vo.getUserId().getName());
				vo.setUserEmail(vo.getUserId().getEmail());
			}
		}
		
		uploadedFilesEvidence = new ArrayList<UploadedFileWO>();
		for (int i = 0; i < trcCorrespondence.getTrcCorrespondencePicFollowupAttachments().size(); i++) {
			TrcCorrespondencePicFollowupAttachment ra = trcCorrespondence.getTrcCorrespondencePicFollowupAttachments().get(i);
			UploadedFileWO uf = new UploadedFileWO();
			uf.setFileName(ra.getAttachmentFile());
			uf.setFileId(ra.getFileId());
			uf.setIsNew(false);
			uf.setFileSize(ra.getFileSize());
			uploadedFilesEvidence.add(uf);
			
		}
		
		filesDocument = new ArrayList<UploadedFileWO>();
		for (int i = 0; i < trcCorrespondence.getTrcCorrespondenceDocuments().size(); i++) {
			TrcCorrespondenceDocument ra = trcCorrespondence.getTrcCorrespondenceDocuments().get(i);
			UploadedFileWO uf = new UploadedFileWO();
			uf.setFileName(ra.getAttachmentFile());
			uf.setFileId(ra.getFileId());
			uf.setIsNew(false);
			uf.setFileSize(ra.getFileSize());
			filesDocument.add(uf);

		}
		
		tableAttedanceModel = new TrcCorrespondencePicFollowupAttendanceTableModel<TrcCorrespondencePicFollowupAttendance>(
				trcCorrespondence.getTrcCorrespondencePicFollowupAttendance());
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
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
                Arrays.asList("NPK", "Nama", "Divisi"),
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
			if(trcCorrespondence.getFollowupStatus() == null || trcCorrespondence.getFollowupStatus().getParameterDtlCode() == null) {
				facesUtil.addErrMessage(facesUtil.retrieveMessage("formTmpCorrespondenceAttedance") 
						+ facesUtil.retrieveMessage("validateRequired"));
				flag = true;
			}
			if(trcCorrespondence.getAttendance() != null && trcCorrespondence.getAttendance().getParameterDtlCode().equals(TrcCorrespondenceConstants.PARAM_DETAIL_ATTENDEE_I_ATTEND)) {
				if(trcCorrespondence.getTrcCorrespondencePicFollowupAttendance() == null ||
						trcCorrespondence.getTrcCorrespondencePicFollowupAttendance().size() == 0) {
					facesUtil.addErrMessage(facesUtil.retrieveMessage("formTmpCorrespondencePICAttendance") + " "
							+ facesUtil.retrieveMessage("validateRequired"));
					flag = true;
				}else {
					Set<Long> setUserAttendeeTemp = new HashSet<Long>();
					for (int i = 0; i < trcCorrespondence.getTrcCorrespondencePicFollowupAttendance().size(); i++) {
						TrcCorrespondencePicFollowupAttendance dtl = (TrcCorrespondencePicFollowupAttendance) trcCorrespondence
								.getTrcCorrespondencePicFollowupAttendance().get(i);
						
						if(dtl.getUserNIK() == null) {
							facesUtil.addErrMessage(facesUtil.retrieveMessage("formTmpCorrespondenceNIK")  + " "  
									+ facesUtil.retrieveMessage("validateRequired"));
							flag = true;
							break;
						}
						if(dtl.getUserId().getUserId() != null ) {
							if(!setUserAttendeeTemp.add(dtl.getUserId().getUserId())) {
								facesUtil.addErrMessage(facesUtil.retrieveMessage("formTmpCorrespondenceNIK") + " "
										+ facesUtil.retrieveMessage("errorDuplicate"));
								flag = true;
								break;
							}
						}
					}
				}
			}
			if(trcCorrespondence.getFollowupStatus() != null && trcCorrespondence.getFollowupStatus().getParameterDtlCode().equals(TrcCorrespondenceConstants.PARAM_DETAIL_ATTENDEE_NOT_ATTEND)) {
				if(trcCorrespondence.getFollowupNote() == null || trcCorrespondence.getFollowupNote().equals("")) {
					facesUtil.addErrMessage(facesUtil.retrieveMessage("formTmpRmdInformation") + " " 
							+ facesUtil.retrieveMessage("validateRequired"));
					flag = true;
				}
			}
		}else{
			if (trcCorrespondence.getFollowupDate() == null) {
				facesUtil.addErrMessage(facesUtil.retrieveMessage("formTmpCorrespondenceFollowupDate") + " "
						+ facesUtil.retrieveMessage("validateRequired"));
				flag = true;
			}
			
			if (uploadedFilesEvidence == null || uploadedFilesEvidence.size() <= 0) {
				facesUtil.addErrMessage(facesUtil.retrieveMessage("formTmpRmdPicConfirmationEvidence") + " File "
						+ facesUtil.retrieveMessage("validateRequired"));
				flag = true;
			}
		}

		return flag;
	}
	
	public void save() {
		
		
		try {
			
			String keterangan = facesUtil.retrieveRequestParam("keterangan");
			
			
			if(!StringUtils.isEmpty(keterangan)){
				trcCorrespondence.setFollowupNote(keterangan);
			}
			
			
		    ParameterDetail attendance = parameterDetailService.getParameterDetailByParamDtlCode(trcCorrespondence.getFollowupStatus().getParameterDtlCode());
			trcCorrespondence.setAttendance(attendance);
			

			if (!validate()) {
				if (trcCorrespondence.getCorrespondenceId() != null) {
					if(uploadedFilesEvidence != null) {
						trcCorrespondence.getTrcCorrespondencePicFollowupAttachments().clear();
						for (int i = 0; i < uploadedFilesEvidence.size(); i++) {
							TrcCorrespondencePicFollowupAttachment evidence = new TrcCorrespondencePicFollowupAttachment();
							UploadedFileWO uf = (UploadedFileWO) uploadedFilesEvidence.get(i);
							evidence.setTrcCorrespondence(trcCorrespondence);
	
							evidence.setAttachmentFile(uf.getFileName());
							evidence.setCreatedBy(facesUtil.retrieveUserLogin());
							evidence.setCreationDate(new Timestamp(new Date().getTime()));
							evidence.setDelId(new Long(0));
							evidence.setEnabledFlag(Constants.CONSTANT_YES);
							evidence.setFileId(uf.getFileId());
							evidence.setFileSize(uf.getFileSize());
							/*
							 * if(uf.getContents()!=null) {
							 * evidence.setFileId(CallApiManager.callUploadAPI(uf,
							 * Constants.COMPLIANCE_DOC_TYPE_DOKUMEN_TINDAK_LANJUT_DARI_PIC,
							 * parameterDetailService)); }else { evidence.setFileId(uf.getContentType()); }
							 */						
							trcCorrespondence.getTrcCorrespondencePicFollowupAttachments().add(evidence);
						}
					}
					
					if(trcCorrespondence.getTrcCorrespondencePicFollowupAttendance() != null) {
						for (int i = 0; i < trcCorrespondence.getTrcCorrespondencePicFollowupAttendance().size(); i++) {
							TrcCorrespondencePicFollowupAttendance dtl = (TrcCorrespondencePicFollowupAttendance) trcCorrespondence
									.getTrcCorrespondencePicFollowupAttendance().get(i);
							
							dtl.setTrcCorrespondence(trcCorrespondence);
							if (dtl.getCreatedBy() == null) {
								dtl.setCreatedBy(facesUtil.retrieveUserLogin());
								dtl.setCreationDate(new Timestamp(new Date().getTime()));
							}

							dtl.setLastUpdateBy(facesUtil.retrieveUserLogin());
							dtl.setLastUpdateDate(new Timestamp(new Date().getTime()));
							dtl.setDelId(new Long(0));
							dtl.setEnabledFlag(Constants.CONSTANT_YES);
						}
					}
					
					trcCorrespondence.setConfirmationDate(new Date());
					
					if(trcCorrespondence.getCorrespondenceCode().getParameterDtlCode().equals(TrcCorrespondenceConstants.PARAM_DETAIL_CORRESPONDEN_TYPE_INVITATION)) {
						if(  trcCorrespondence.getAttendance().getParameterDtlCode().equals(ParameterDetail.PARAM_DET_I_ATTEND)) {
							ParameterDetail attendee = parameterDetailService.getParameterDetailByParamDtlCode(
									ParameterDetail.PARAM_DET_I_ATTEND);
							trcCorrespondence.setFollowupStatus(attendee);
						}else {
							ParameterDetail notAttendee = parameterDetailService.getParameterDetailByParamDtlCode(
									ParameterDetail.PARAM_DET_NOT_ATTEND);
							trcCorrespondence.setFollowupStatus(notAttendee);
						}
					}else {
						ParameterDetail followupStatus = parameterDetailService.getParameterDetailByParamDtlCode(
								ParameterDetail.PARAM_DET_CODE_PIC_FOLLOWUP_STATUS_PIC_DONE);
						trcCorrespondence.setFollowupStatus(followupStatus);
					}
					
					User userData = userService.getUserByNik(facesUtil.retrieveUserLogin());
					trcCorrespondence.setFollowupBy(userData);
					trcCorrespondence.setUserId1(userData);
					
					ParameterDetail complianceStatusClose = parameterDetailService.getParameterDetailByParamDtlCode(
							ParameterDetail.PARAM_DET_CODE_COMPLIANCE_CHECK_STATUS_COMPLIANCE_CLOSE);
					
					Date currentDate = new Date();
					
					if(trcCorrespondence.getCorrespondenceCode().getParameterDtlCode().equals(TrcCorrespondenceConstants.PARAM_DETAIL_CORRESPONDEN_TYPE_INVITATION)) {
						trcCorrespondence.setComplianceStatus(complianceStatusClose);
						trcCorrespondence.setFollowupDate(currentDate);
					}else {
						trcCorrespondence.setComplianceStatus(null);
					}
					
					trcCorrespondence.setComplianceNote(null);
					trcCorrespondence.setComplianceBy(null);
					trcCorrespondence.setComplianceDate(null);
					
					trcCorrespondence.setLastUpdateBy(facesUtil.retrieveUserLogin());
					trcCorrespondence.setLastUpdateDate(new Timestamp(new Date().getTime()));
					trcCorrespondence.setDelId(new Long(0));
					trcCorrespondence.setEnabledFlag(Constants.CONSTANT_YES);
					trcCorrespondenceService.update(trcCorrespondence);
				}
				
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
				
				if(deletedFiles!=null) {
					for(int i=0;i<deletedFiles.size();i++) {
						UploadedFileWO uf = (UploadedFileWO) deletedFiles.get(i);
						CallApiManager.deleteFile(uf.getFileId(), parameterDetailService, fileUtil);
					}
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
			if(uploadedFilesDocument != null) {
				for (int i = 0; i < uploadedFilesDocument.size(); i++) {
					UploadedFileWO uf = (UploadedFileWO) uploadedFilesDocument.get(i);
					if(uf.getIsNew() == null) {
						CallApiManager.deleteFile(uf.getFileId(), parameterDetailService, fileUtil);
					}
				}
			}
			
			facesUtil.setSessionAttribute("BACK_SESSION", true);
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
	
	@Override
	public void itemSelected(String clientId, String widgetVar, Object selectedItem) {
		if (StringUtils.equals("picAttendanceDialog", widgetVar)) {
			Object[] objects = (Object[]) selectedItem;
			//User userPicCompliance = userService.findById(((BigInteger) objects[0]).longValue());
			User userPicAttendee = userService.findById(MathUtil.returnIdObjectToLong(objects[0]));
			if (userPicAttendee != null) {
				trcCorrespondence.getTrcCorrespondencePicFollowupAttendance().get(indexDtlPicAttendance)
						.setUserId(userPicAttendee);
				trcCorrespondence.getTrcCorrespondencePicFollowupAttendance().get(indexDtlPicAttendance)
						.setUserNIK(userPicAttendee.getNik());
				trcCorrespondence.getTrcCorrespondencePicFollowupAttendance().get(indexDtlPicAttendance)
						.setUserName(userPicAttendee.getName());
				trcCorrespondence.getTrcCorrespondencePicFollowupAttendance().get(indexDtlPicAttendance)
						.setUserEmail(userPicAttendee.getEmail());

				tableAttedanceModel.setWrappedData(trcCorrespondence.getTrcCorrespondencePicFollowupAttendance());
			}
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
	
	public List<UploadedFileWO> getFilesDocument() {
		return filesDocument;
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

	public String getTextWarningUpload() {
		return textWarningUpload;
	}

	public void setTextWarningUpload(String textWarningUpload) {
		this.textWarningUpload = textWarningUpload;
	}

	public Integer getTestFirst() {
		return testFirst;
	}

	public void setTestFirst(Integer testFirst) {
		this.testFirst = testFirst;
	}
	

	

	
	
	

}