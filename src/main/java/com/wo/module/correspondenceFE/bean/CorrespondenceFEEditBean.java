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
import com.wo.module.trcCorrespondence.model.TrcCrpdcPicConfirm;
import com.wo.module.trcCorrespondence.model.TrcCrpdcReffLetter;
import com.wo.module.trcCorrespondence.service.TrcCorrespondenceService;
import com.wo.module.trcCorrespondence.service.TrcCrpdcPicConfirmService;
import com.wo.module.user.model.User;
import com.wo.module.user.service.UserService;

public class CorrespondenceFEEditBean extends CommonBean implements SelectorListener<Object>,  Serializable {

	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(CorrespondenceFEEditBean.class);

	private TrcCorrespondence trcCorrespondence;
	private TrcCrpdcPicConfirm trcCrpdcPicConfirm;
	
	private List<UploadedFileWO> deletedFiles;
	
	private List<UploadedFileWO> uploadedFilesDocument;
	
	private List<UploadedFileWO> filesDocument;
	
	private List<UploadedFileWO> filesDocumentReferensi;
	
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
	
	private TrcCrpdcPicConfirmService trcCrpdcPicConfirmService;
	
	private UserService userService;
	
	private EmailTemplateService emailTemplateService;
	
	private SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", new Locale("id", "ID"));
	
	private String textWarningUpload;
	private String followupNote;
	private String followupStatus;
	private String nonFollowupNote;
	private String nonFollowupStatus;
	
	private Date followupDate;
	
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
		
		String editPicId = facesUtil.retrieveRequestParam("idPic");
		String tokenPic = facesUtil.retrieveRequestParam("tokenPic");
		if (StringUtils.isNotEmpty(tokenPic)) {
			editPicId = Constants.decryptString(tokenPic);
		}
		
		Long picConfirmId = Long.parseLong(editPicId);
		trcCrpdcPicConfirm = trcCrpdcPicConfirmService.findById(picConfirmId);
		
		followupNote = trcCrpdcPicConfirm.getFollowupNote();
		nonFollowupNote = trcCrpdcPicConfirm.getFollowupNote();
		followupDate = trcCrpdcPicConfirm.getFollowupDate();
		
		if(trcCrpdcPicConfirm.getStatusPic() !=null && trcCrpdcPicConfirm.getStatusPic().getParameterDtlCode() !=null) {
			followupStatus = trcCrpdcPicConfirm.getStatusPic().getParameterDtlCode();
			nonFollowupStatus = trcCrpdcPicConfirm.getStatusPic().getParameterDtlCode();
		}
						
		for(int i=0;i<trcCrpdcPicConfirm.getTrcCorrespondencePicFollowupAttendance().size();i++){
			TrcCorrespondencePicFollowupAttendance vo = trcCrpdcPicConfirm.getTrcCorrespondencePicFollowupAttendance().get(i);
			if(vo.getUserId()!=null){
				vo.setUserNIK(vo.getUserId().getNik()); 
				vo.setUserName(vo.getUserId().getName());
				vo.setUserEmail(vo.getUserId().getEmail());
			}
		}
		
		uploadedFilesEvidence = new ArrayList<UploadedFileWO>();
		for (int i = 0; i < trcCrpdcPicConfirm.getTrcCorrespondencePicFollowupAttachments().size(); i++) {
			TrcCorrespondencePicFollowupAttachment ra = trcCrpdcPicConfirm.getTrcCorrespondencePicFollowupAttachments().get(i);
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
		
		filesDocumentReferensi = new ArrayList<UploadedFileWO>();
		for (int i = 0; i < trcCorrespondence.getTrcCrpdcReffLetters().size(); i++) {
			TrcCrpdcReffLetter referensiLet = trcCorrespondence.getTrcCrpdcReffLetters().get(i);
			if(referensiLet.getReffLetterCorrespondence() !=null && 
					referensiLet.getReffLetterCorrespondence().getTrcCorrespondenceDocuments() !=null) {
				List<TrcCorrespondenceDocument> dataList = referensiLet.getReffLetterCorrespondence().getTrcCorrespondenceDocuments();
				for(int j=0; j<dataList.size(); j++) {
					TrcCorrespondenceDocument documentData = (TrcCorrespondenceDocument)dataList.get(j);
					UploadedFileWO uf = new UploadedFileWO();
					uf.setFileName(documentData.getAttachmentFile());
					uf.setFileId(documentData.getFileId());
					uf.setIsNew(false);
					uf.setFileSize(documentData.getFileSize());
					filesDocumentReferensi.add(uf);
				}
			}
		}
		
		tableAttedanceModel = new TrcCorrespondencePicFollowupAttendanceTableModel<TrcCorrespondencePicFollowupAttendance>(
				trcCrpdcPicConfirm.getTrcCorrespondencePicFollowupAttendance());
	
		
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
			trcCrpdcPicConfirm.getTrcCorrespondencePicFollowupAttendance().remove(selectedPicAttendanceData[i]);
		}
		
		if (trcCrpdcPicConfirm.getTrcCorrespondencePicFollowupAttendance() == null
				|| trcCrpdcPicConfirm.getTrcCorrespondencePicFollowupAttendance().size() == 0) {
			lastSequenceOfPicAttendance = 0;
		}
		
		tableAttedanceModel.setWrappedData(trcCrpdcPicConfirm.getTrcCorrespondencePicFollowupAttendance());
	}
	
	public void onAddNewPicAttendance() {
		if (trcCrpdcPicConfirm.getTrcCorrespondencePicFollowupAttendance() == null) {
			trcCrpdcPicConfirm.setTrcCorrespondencePicFollowupAttendance(new ArrayList<TrcCorrespondencePicFollowupAttendance>());
			lastSequenceOfPicAttendance = 0;
		} else {
			if(trcCrpdcPicConfirm.getTrcCorrespondencePicFollowupAttendance().size() == 0) {
				lastSequenceOfPicAttendance = 0;
			}			
		} 
		
		TrcCorrespondencePicFollowupAttendance d = new TrcCorrespondencePicFollowupAttendance();
		lastSequenceOfPicAttendance = lastSequenceOfPicAttendance + 1;
		d.setSequence(lastSequenceOfPicAttendance);
		trcCrpdcPicConfirm.getTrcCorrespondencePicFollowupAttendance().add(d);
		tableAttedanceModel.setWrappedData(trcCrpdcPicConfirm.getTrcCorrespondencePicFollowupAttendance());

	}
	
	public Boolean validate() {
		Boolean flag = false;
		
		if(trcCorrespondence.getCorrespondenceCode().getParameterDtlCode().equals(TrcCorrespondenceConstants.PARAM_DETAIL_CORRESPONDEN_TYPE_INVITATION)) {
			//if(trcCorrespondence.getFollowupStatus() == null || trcCorrespondence.getFollowupStatus().getParameterDtlCode() == null) {
			if(followupStatus == null || StringUtils.isEmpty(followupStatus)) { 
				facesUtil.addErrMessage(facesUtil.retrieveMessage("formTmpCorrespondenceAttedance") 
						+ facesUtil.retrieveMessage("validateRequired"));
				flag = true;
			}
			if(trcCorrespondence.getAttendance() != null && trcCorrespondence.getAttendance().getParameterDtlCode().equals(TrcCorrespondenceConstants.PARAM_DETAIL_ATTENDEE_I_ATTEND)) {
				if(trcCrpdcPicConfirm.getTrcCorrespondencePicFollowupAttendance() == null ||
						trcCrpdcPicConfirm.getTrcCorrespondencePicFollowupAttendance().size() == 0) {
					facesUtil.addErrMessage(facesUtil.retrieveMessage("formTmpCorrespondencePICAttendance") + " "
							+ facesUtil.retrieveMessage("validateRequired"));
					flag = true;
				}else {
					Set<Long> setUserAttendeeTemp = new HashSet<Long>();
					for (int i = 0; i < trcCrpdcPicConfirm.getTrcCorrespondencePicFollowupAttendance().size(); i++) {
						TrcCorrespondencePicFollowupAttendance dtl = (TrcCorrespondencePicFollowupAttendance) trcCrpdcPicConfirm
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
			if(followupStatus != null && StringUtils.isNotEmpty(followupStatus) &&
					followupStatus.equals(TrcCorrespondenceConstants.PARAM_DETAIL_ATTENDEE_NOT_ATTEND)) {
				//if(trcCorrespondence.getFollowupNote() == null || trcCorrespondence.getFollowupNote().equals("")) {
				if(followupNote == null || StringUtils.isEmpty(followupNote)) {
					facesUtil.addErrMessage(facesUtil.retrieveMessage("formTmpRmdInformation") + " " 
							+ facesUtil.retrieveMessage("validateRequired"));
					flag = true;
				}
			}
		}else{
			if (followupDate == null) {
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
				//trcCorrespondence.setFollowupNote(keterangan);
				followupNote =  keterangan;
				nonFollowupNote = keterangan;
			}
			
			String correspondenAttendanceCode = trcCorrespondence.getCorrespondenceCode().getParameterDtlCode();
			
			if (!validate()) {
				if (trcCrpdcPicConfirm.getCrpdcPicConfirmId() != null) {		
					if (correspondenAttendanceCode
							.equals(TrcCorrespondenceConstants.PARAM_DETAIL_CORRESPONDEN_TYPE_INVITATION)) {
						if (followupStatus.equals(ParameterDetail.PARAM_DET_I_ATTEND)) {
							ParameterDetail attendee = parameterDetailService
									.getParameterDetailByParamDtlCode(ParameterDetail.PARAM_DET_I_ATTEND);
							trcCrpdcPicConfirm.setStatusPic(attendee);
						} else {
							ParameterDetail notAttendee = parameterDetailService
									.getParameterDetailByParamDtlCode(ParameterDetail.PARAM_DET_NOT_ATTEND);
							trcCrpdcPicConfirm.setStatusPic(notAttendee);
						}

						trcCrpdcPicConfirm.setFollowupNote(followupNote);
						trcCrpdcPicConfirm.setFollowupDate(new Timestamp(new Date().getTime()));
						
						ParameterDetail complianceStatusClose = parameterDetailService.getParameterDetailByParamDtlCode(
								ParameterDetail.PARAM_DET_CODE_COMPLIANCE_CHECK_STATUS_COMPLIANCE_CLOSE);
						trcCrpdcPicConfirm.setComplianceBy(trcCrpdcPicConfirm.getUser1());
						trcCrpdcPicConfirm.setComplianceStatus(complianceStatusClose);
						trcCrpdcPicConfirm.setComplianceDate(new Timestamp(new Date().getTime()));

					} else {
						ParameterDetail followupStatus = parameterDetailService.getParameterDetailByParamDtlCode(
								ParameterDetail.PARAM_DET_CODE_PIC_FOLLOWUP_STATUS_PIC_DONE);
						trcCrpdcPicConfirm.setStatusPic(followupStatus);
						trcCrpdcPicConfirm.setFollowupDate(new Timestamp(followupDate.getTime()));
						trcCrpdcPicConfirm.setFollowupNote(nonFollowupNote);
					}
					
					if(trcCorrespondence.getCorrespondenceCode().getParameterDtlCode().equals(TrcCorrespondenceConstants.PARAM_DETAIL_CORRESPONDEN_TYPE_INVITATION)) {	
						if(trcCrpdcPicConfirm.getTrcCorrespondencePicFollowupAttendance() != null) {
							for (int i = 0; i < trcCrpdcPicConfirm.getTrcCorrespondencePicFollowupAttendance().size(); i++) {
								TrcCorrespondencePicFollowupAttendance dtl = (TrcCorrespondencePicFollowupAttendance) trcCrpdcPicConfirm
										.getTrcCorrespondencePicFollowupAttendance().get(i);
								
								dtl.setTrcCorrespondence(trcCorrespondence);
								dtl.setCrpdcPicConfirmId(trcCrpdcPicConfirm.getCrpdcPicConfirmId());							
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
					}else{
						if(uploadedFilesEvidence != null) {
							trcCrpdcPicConfirm.getTrcCorrespondencePicFollowupAttachments().clear();
							for (int i = 0; i < uploadedFilesEvidence.size(); i++) {
								TrcCorrespondencePicFollowupAttachment evidence = new TrcCorrespondencePicFollowupAttachment();
								UploadedFileWO uf = (UploadedFileWO) uploadedFilesEvidence.get(i);
								evidence.setTrcCorrespondence(trcCorrespondence);
								evidence.setCrpdcPicConfirmId(trcCrpdcPicConfirm.getCrpdcPicConfirmId());							 
								evidence.setAttachmentFile(uf.getFileName());
								evidence.setCreatedBy(facesUtil.retrieveUserLogin());
								evidence.setCreationDate(new Timestamp(new Date().getTime()));
								evidence.setDelId(new Long(0));
								evidence.setEnabledFlag(Constants.CONSTANT_YES);
								evidence.setFileId(uf.getFileId());
								evidence.setFileSize(uf.getFileSize());
								trcCrpdcPicConfirm.getTrcCorrespondencePicFollowupAttachments().add(evidence);
							}
						}
					}
					
					if (trcCrpdcPicConfirm.getCreatedBy() == null) {
						trcCrpdcPicConfirm.setCreatedBy(facesUtil.retrieveUserLogin());
						trcCrpdcPicConfirm.setCreationDate(new Timestamp(new Date().getTime()));
					}

					trcCrpdcPicConfirm.setLastUpdateBy(facesUtil.retrieveUserLogin());
					trcCrpdcPicConfirm.setLastUpdateDate(new Timestamp(new Date().getTime()));
					trcCrpdcPicConfirm.setDelId(new Long(0));
					trcCrpdcPicConfirm.setEnabledFlag(Constants.CONSTANT_YES);
					
					trcCrpdcPicConfirmService.update(trcCrpdcPicConfirm);
					
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
				trcCrpdcPicConfirm.getTrcCorrespondencePicFollowupAttendance().get(indexDtlPicAttendance)
						.setUserId(userPicAttendee);
				trcCrpdcPicConfirm.getTrcCorrespondencePicFollowupAttendance().get(indexDtlPicAttendance)
						.setUserNIK(userPicAttendee.getNik());
				trcCrpdcPicConfirm.getTrcCorrespondencePicFollowupAttendance().get(indexDtlPicAttendance)
						.setUserName(userPicAttendee.getName());
				trcCrpdcPicConfirm.getTrcCorrespondencePicFollowupAttendance().get(indexDtlPicAttendance)
						.setUserEmail(userPicAttendee.getEmail());

				tableAttedanceModel.setWrappedData(trcCrpdcPicConfirm.getTrcCorrespondencePicFollowupAttendance());
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

	public List<UploadedFileWO> getFilesDocumentReferensi() {
		return filesDocumentReferensi;
	}

	public void setFilesDocumentReferensi(List<UploadedFileWO> filesDocumentReferensi) {
		this.filesDocumentReferensi = filesDocumentReferensi;
	}

	public void setFilesDocument(List<UploadedFileWO> filesDocument) {
		this.filesDocument = filesDocument;
	}

	public String getFollowupNote() {
		return followupNote;
	}

	public void setFollowupNote(String followupNote) {
		this.followupNote = followupNote;
	}

	public String getFollowupStatus() {
		return followupStatus;
	}

	public void setFollowupStatus(String followupStatus) {
		this.followupStatus = followupStatus;
	}

	public String getNonFollowupNote() {
		return nonFollowupNote;
	}

	public void setNonFollowupNote(String nonFollowupNote) {
		this.nonFollowupNote = nonFollowupNote;
	}

	public String getNonFollowupStatus() {
		return nonFollowupStatus;
	}

	public void setNonFollowupStatus(String nonFollowupStatus) {
		this.nonFollowupStatus = nonFollowupStatus;
	}
	
	public TrcCrpdcPicConfirm getTrcCrpdcPicConfirm() {
		return trcCrpdcPicConfirm;
	}

	public void setTrcCrpdcPicConfirm(TrcCrpdcPicConfirm trcCrpdcPicConfirm) {
		this.trcCrpdcPicConfirm = trcCrpdcPicConfirm;
	}

	public TrcCrpdcPicConfirmService getTrcCrpdcPicConfirmService() {
		return trcCrpdcPicConfirmService;
	}

	public void setTrcCrpdcPicConfirmService(TrcCrpdcPicConfirmService trcCrpdcPicConfirmService) {
		this.trcCrpdcPicConfirmService = trcCrpdcPicConfirmService;
	}

	public Date getFollowupDate() {
		return followupDate;
	}

	public void setFollowupDate(Date followupDate) {
		this.followupDate = followupDate;
	}		

}