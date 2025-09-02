package com.wo.module.fineFE.bean;

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
import org.primefaces.event.FileUploadEvent;

import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.exception.CustomAPIException;
import com.wo.module.common.model.UploadedFileWO;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.common.util.FileUtil;
import com.wo.module.common.utility.CallApiManager;
import com.wo.module.emailTemplate.model.EmailTemplate;
import com.wo.module.emailTemplate.service.EmailTemplateService;
import com.wo.module.fineFE.constant.FineFEConstant;
import com.wo.module.fineFE.service.FineFEService;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.parameter.model.ParameterHeader;
import com.wo.module.rc.model.RC;
import com.wo.module.rc.service.RCService;
import com.wo.module.regulationSocialization.model.SocializationDocumentTrc;
import com.wo.module.regulationSocialization.model.SocializationPICFollowupAttachmentTrc;
import com.wo.module.trcFineApproval.model.TrcFine;
import com.wo.module.trcFineApproval.model.TrcFinePicCompliance;
import com.wo.module.trcFineApproval.model.TrcFinePicFollowup;
import com.wo.module.trcFineApproval.model.TrcFinePicFollowupAttachment;
import com.wo.module.trcFineApproval.service.TrcFinePicFollowupService;
import com.wo.module.user.service.UserService;

public class FineFEEditBean extends CommonBean implements Serializable{

	private static final long serialVersionUID = 8110786868030897691L;
	private static final Logger logger = Logger.getLogger(FineFEEditBean.class);
	private static final String NAVIGATE_BACK = FineFEConstant.NAVIGATE_FINE_FE;
	
	private FineFEService fineFEService;
	private UserService userService;
	private EmailTemplateService emailTemplateService;
	private TrcFinePicFollowupService trcFinePicFollowupService;
	private RCService rcService;
	
	private TrcFine trcFine;
	private TrcFinePicFollowup trcFinePicFollowup;
	
	private String editId;
	private String pengirim;
	private String tanggalTerimaSurat;
	private String perihal;
	private String tanggalSurat;
	private String tipeSurat;
	private String catatanComplinace;
	private String tglTindakLanjut;
	private String keterangan;
	private String followupNotes;
	private String textWarningUpload;
	private String selectedRcId;
	private String isExtended;
	
	private List<SelectItem> rcList;
	private List<SelectItem> categoryList;
	private List<UploadedFileWO> deletedFiles;
	private List<UploadedFileWO> uploadedFiles;
	
	private FacesUtil facesUtil;
	private FileUtil fileUtil;
	
	private Integer testFirst;
	private String verifyStatusName;
	
	private Boolean isRevise;
	private Boolean verifyActive;
	
	private SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
	
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
		facesUtil.setSessionAttribute("FIRST_FINE_FE", testFirst);
		facesUtil.setSessionAttribute("BACK_SESSION", false);
		checkNewOrEdit();
		fileUtil = FileUtil.getInstance();
		
		try {
			ParameterDetail getText = parameterDetailService.getParameterDetailByParamDtlCode("UPLOAD_WARNING_TEXT");
			textWarningUpload = getText.getName();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void checkNewOrEdit() {
		this.editId = facesUtil.retrieveRequestParam("id");
		String token = facesUtil.retrieveRequestParam("token");
		if (StringUtils.isNotEmpty(token)) {
			editId = Constants.decryptString(token);
		}
		facesUtil.setSessionAttribute("token", Constants.encryptString(editId));
		
		if (StringUtils.isBlank(editId)) {
			// do nothing
		} else {
			handleEdit(editId);
		}
	}
	
	private void handleEdit(String editId) {
		
		isExtended = "N";
		
		rcList = new ArrayList<SelectItem>();
		List<SearchObject> searchCriteria = new ArrayList<SearchObject>();
		try {
			List<RC> listRC = rcService.searchData(searchCriteria, 0, Integer.MAX_VALUE, null, null);
			
			for (RC vo : listRC) {
				SelectItem si = new SelectItem();
				si.setLabel(vo.getRegionCode()+"-"+vo.getWorkingUnit()+"-"+vo.getRegion());
				si.setValue(vo.getRcId());
				rcList.add(si);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		categoryList = new ArrayList<SelectItem>();
		List<ParameterDetail> listCategory;
		try {
			listCategory = parameterDetailService
					.getParameterDetailByParamCode(ParameterHeader.PARAM_HEAD_FINE_CATEGORY);
			for (ParameterDetail vo : listCategory) {
				SelectItem si = new SelectItem();
				si.setLabel(vo.getName());
				si.setValue(vo.getParameterDtlCode());
				categoryList.add(si);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Long editIdLong = Long.parseLong(editId);
		
		trcFinePicFollowup =  trcFinePicFollowupService.findById(editIdLong);
		
		trcFine = fineFEService.findById(trcFinePicFollowup.getTrcFine().getFineId());
		
		this.pengirim = trcFine.getSenderCode() != null ? trcFine.getSenderCode().getNameIn() : "";
		this.tanggalTerimaSurat = trcFine.getLetterReceivedDate() != null ? sdf.format(trcFine.getLetterReceivedDate()) : "";
		this.perihal = trcFine.getPerihalIn() != null ? trcFine.getPerihalIn() : "";
		this.tanggalSurat = trcFine.getLetterDate() != null ? sdf.format(trcFine.getLetterDate()) : "";
		this.tipeSurat = trcFine.getFineCode()  !=  null ? trcFine.getFineCode().getNameIn() : "";
		this.verifyActive = (trcFinePicFollowup.getComplianceStatus() != null || trcFinePicFollowup.getTargetDateStatus() != null);
		if(trcFinePicFollowup.getComplianceStatus() != null) {
			this.verifyStatusName = trcFinePicFollowup.getComplianceStatus().getNameIn();
		}else if(trcFinePicFollowup.getTargetDateStatus() != null) {
			this.verifyStatusName = trcFinePicFollowup.getTargetDateStatus().getNameIn();
		}else {
			this.verifyStatusName = "";
		}
		
		this.catatanComplinace = trcFine.getNotes() != null ? trcFine.getNotesDecrypted() : "";
		this.followupNotes = trcFinePicFollowup.getFollowupNote() != null ? trcFinePicFollowup.getFollowupNote() : "";
		this.selectedRcId = trcFinePicFollowup.getRc() != null ? trcFinePicFollowup.getRc().getRcId().toString() : "";
		
		
		
		this.isRevise = true;
		if(trcFinePicFollowup.getTargetDateStatus() != null && trcFinePicFollowup.getTargetDateStatus()
				.getParameterDtlCode().equals(FineFEConstant.TARGET_DATE_APPROPRIATE)) {
			this.isRevise = false;
		}
		
		if(!isRevise) trcFinePicFollowup.setFollowupDate(new Date());
		
		
		if (trcFinePicFollowup.getTrcFinePicFollowupAttachments() != null) {			
//			long cExtend = trcFinePicFollowup.getTrcFinePicFollowupAttachments().stream()
//					.filter(fa -> fa.getAttachmentFrom().equals(ParameterDetail.FINE_FOLLOWUP_ATTACHMENT_TYPE_EXTENDED))
//					.count();
			
			uploadedFiles = uploadedFiles == null ? new ArrayList<UploadedFileWO>() : uploadedFiles;
//			if(trcFinePicFollowup.getComplianceStatus() == null) {
//				//rejected after extended
//				for (int i = 0; i < trcFinePicFollowup.getTrcFinePicFollowupAttachments().size(); i++) {
//					TrcFinePicFollowupAttachment vo = (TrcFinePicFollowupAttachment) trcFinePicFollowup.getTrcFinePicFollowupAttachments().get(i);
//					if(vo.getAttachmentFrom() != null && vo.getAttachmentFrom().equals(ParameterDetail.FINE_FOLLOWUP_ATTACHMENT_TYPE_EXTENDED)) {
//						UploadedFileWO uf = new UploadedFileWO();
//						uf.setFileName(vo.getAttachmentFile());
//						uf.setFileId(vo.getFileId());
//						uf.setIsNew(false);
//						uf.setFileSize(vo.getFileSize());
//						uploadedFiles.add(uf);
//					}
//
//				}
//			}else {
				//continue the process
				for (int i = 0; i < trcFinePicFollowup.getTrcFinePicFollowupAttachments().size(); i++) {
					TrcFinePicFollowupAttachment vo = (TrcFinePicFollowupAttachment) trcFinePicFollowup.getTrcFinePicFollowupAttachments().get(i);
					if(vo.getAttachmentFrom() == null || vo.getAttachmentFrom().equals(ParameterDetail.FINE_FOLLOWUP_ATTACHMENT_TYPE_FOLLOWUP)) {
						UploadedFileWO uf = new UploadedFileWO();
						uf.setFileName(vo.getAttachmentFile());
						uf.setFileId(vo.getFileId());
						uf.setIsNew(false);
						uf.setFileSize(vo.getFileSize());
						uploadedFiles.add(uf);
					}

				}
//			}
			
		}
		
		/*if (trcFine.getTrcFinePicFollowups() != null) {
			for (int i = 0; i < trcFine.getTrcFinePicFollowups().size(); i++) {
				TrcFinePicFollowup vo = (TrcFinePicFollowup) trcFine.getTrcFinePicFollowups().get(i);
				this.catatanComplinace = vo.getComplianceNote() != null ? vo.getComplianceNote() : "";
				this.followupNotes = vo.getFollowupNote() != null ? vo.getFollowupNote() : "";
			}
		}*/
	}
	
	public void handleFileUpload(FileUploadEvent event) throws Exception {
		try {
			logger.debug("extended selected == "+isExtended);
			uploadedFiles = uploadedFiles == null ? new ArrayList<UploadedFileWO>() : uploadedFiles;
			uploadedFiles.add(new UploadedFileWO(CallApiManager.callUploadAPI(event.getFile(),
					Constants.COMPLIANCE_DOC_TYPE_DOKUMEN_TINDAK_LANJUT_DARI_PIC, parameterDetailService, false, fileUtil),
					event.getFile().getFileName(), event.getFile().getContentType(), event.getFile().getSize(), true));
		} catch (Exception e) {
			e.printStackTrace();
			facesUtil.addErrMessage(e.getMessage());
		}
	}
	
	public void onExtendSelected() {
		logger.debug("extended selected == "+isExtended);
		List<TrcFinePicFollowupAttachment> listDtl = trcFinePicFollowup.getTrcFinePicFollowupAttachments();
		for(UploadedFileWO uf : uploadedFiles) {
			Long isExist = listDtl.stream()
					.filter(fa -> fa.getFileId().equals(uf.getFileId()))
					.count();
			if(uf.getIsNew() && isExist.equals(0l)) {
				TrcFinePicFollowupAttachment vo = new TrcFinePicFollowupAttachment();
				
				vo.setTrcFinePicFollowup(trcFinePicFollowup);
				vo.setAttachmentFile(uf.getFileName());
				vo.setCreatedBy(facesUtil.retrieveUserLogin());
				vo.setCreationDate(new Timestamp(new Date().getTime()));
				vo.setDelId(new Long(0));
				vo.setEnabledFlag(Constants.CONSTANT_YES);
				vo.setFileId(uf.getFileId());
				vo.setFileSize(uf.getFileSize());
				
				vo.setAttachmentFrom(isExtended.equals("Y") ? ParameterDetail.FINE_FOLLOWUP_ATTACHMENT_TYPE_EXTENDED 
						: ParameterDetail.FINE_FOLLOWUP_ATTACHMENT_TYPE_FOLLOWUP);
				
				
				listDtl.add(vo);
			}
		}
		
		
		uploadedFiles.clear();
		
		if(isExtended.equals("Y")) {
			for (int i = 0; i < trcFinePicFollowup.getTrcFinePicFollowupAttachments().size(); i++) {
				TrcFinePicFollowupAttachment vo = (TrcFinePicFollowupAttachment) trcFinePicFollowup.getTrcFinePicFollowupAttachments().get(i);
				if(vo.getAttachmentFrom() != null && vo.getAttachmentFrom().equals(ParameterDetail.FINE_FOLLOWUP_ATTACHMENT_TYPE_EXTENDED)) {
					UploadedFileWO uf = new UploadedFileWO();
					uf.setFileName(vo.getAttachmentFile());
					uf.setFileId(vo.getFileId());
					uf.setIsNew(vo.getFinePicFollowupAttachmentId() == null);
					uf.setFileSize(vo.getFileSize());
					uploadedFiles.add(uf);
				}
			}
		}else {
			for (int i = 0; i < trcFinePicFollowup.getTrcFinePicFollowupAttachments().size(); i++) {
				TrcFinePicFollowupAttachment vo = (TrcFinePicFollowupAttachment) trcFinePicFollowup.getTrcFinePicFollowupAttachments().get(i);
				if(vo.getAttachmentFrom() == null || vo.getAttachmentFrom().equals(ParameterDetail.FINE_FOLLOWUP_ATTACHMENT_TYPE_FOLLOWUP)) {
					UploadedFileWO uf = new UploadedFileWO();
					uf.setFileName(vo.getAttachmentFile());
					uf.setFileId(vo.getFileId());
					uf.setIsNew(vo.getFinePicFollowupAttachmentId() == null);
					uf.setFileSize(vo.getFileSize());
					uploadedFiles.add(uf);
				}
			}
		}
		
	}
	
	private boolean isValidate() {
		boolean flag = true;
		
		if (trcFinePicFollowup.getTargetDate() == null) {
			facesUtil.addErrMessage((isExtended.equals("Y") ? "Target Waktu Baru ":"Target Waktu Penyelesaian ")
					+ facesUtil.retrieveMessage("validateRequired"));
			flag = false;
		}
		
		if(isExtended.equals("Y")) {
			long cExtend = uploadedFiles.stream()
					.filter(uf -> uf.getIsNew())
					.count();
			if(cExtend == 0) {
				facesUtil.addErrMessage("Bukti Perpanjangan waktu "
						+ facesUtil.retrieveMessage("validateRequired"));
				flag = false;
			}
			
		}
		
		return flag;
	}
	
	public void save() {
		//String tglTindakLanjut = facesUtil.retrieveRequestParam("tglTindakLanjut");
		String keterangan = facesUtil.retrieveRequestParam("keterangan");
		
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		
		try {
			
			if(isValidate()) {
				
				ParameterDetail followupStatus = null;
				if(isExtended.equals("Y")) {
					followupStatus = parameterDetailService.getParameterDetailByParamDtlCode(
							ParameterDetail.PARAM_DET_CODE_PIC_FOLLOWUP_STATUS_PIC_EXTENSION);
				}else {
					followupStatus = parameterDetailService.getParameterDetailByParamDtlCode(
							ParameterDetail.PARAM_DET_CODE_PIC_FOLLOWUP_STATUS_PIC_DONE);
				}
				
				
				if (!StringUtils.isEmpty(keterangan)) {
					trcFinePicFollowup.setFollowupNote(keterangan);
				}
				
				
				for (int i = 0; i < trcFine.getTrcFinePicFollowups().size(); i++) {
					TrcFinePicFollowup trcFinePicFollowupNew = trcFine.getTrcFinePicFollowups().get(i);
				
					if(trcFinePicFollowupNew.getFinePicFollowupId().equals(trcFinePicFollowup.getFinePicFollowupId())){
						/*if(trcFinePicFollowupNew.getTrcFinePicFollowupAttachments() == null || trcFinePicFollowupNew.getTrcFinePicFollowupAttachments().size() <= 0){
							trcFinePicFollowupNew.setTrcFinePicFollowupAttachments(new ArrayList<TrcFinePicFollowupAttachment>());
						}*/
						trcFinePicFollowupNew.setFollowupStatus(followupStatus);
						if(!isRevise && isExtended.equals("N"))trcFinePicFollowupNew.setFollowupDate(trcFinePicFollowup.getFollowupDate());
						trcFinePicFollowupNew.setFollowupNote(trcFinePicFollowup.getFollowupNote());
						
						if(!StringUtils.isEmpty(selectedRcId)) {
							RC selectedRc = rcService.findById(Long.parseLong(selectedRcId));
							trcFinePicFollowupNew.setRc(selectedRc);
							
						}
						
						trcFinePicFollowupNew.setRootCause(trcFinePicFollowup.getRootCause());
						trcFinePicFollowupNew.setCategory(trcFinePicFollowup.getCategory());
						trcFinePicFollowupNew.setBreaches(trcFinePicFollowup.getBreaches());
						trcFinePicFollowupNew.setTargetDate(trcFinePicFollowup.getTargetDate());
						if(isRevise || isExtended.equals("Y")) trcFinePicFollowupNew.setConfirmationDate(null);
						if(isRevise || isExtended.equals("Y")) trcFinePicFollowupNew.setTargetDateStatus(null);
						trcFinePicFollowupNew.setFollowupBy(userService.getUserByNik(facesUtil.retrieveUserLogin()));
						
						trcFinePicFollowupNew.setLastUpdateBy(facesUtil.retrieveUserLogin());
						trcFinePicFollowupNew.setLastUpdateDate(new Timestamp(new Date().getTime()));
						
						//trcFinePicFollowupNew.getTrcFinePicFollowupAttachments().clear();
						
						List<TrcFinePicFollowupAttachment> listDtl = trcFinePicFollowupNew.getTrcFinePicFollowupAttachments();
						List<TrcFinePicFollowupAttachment> listDtlNew = null;
						
						if (uploadedFiles != null) {
							
							if(listDtl == null || listDtl.size() == 0) {
								listDtl = new ArrayList<>();
								
								for (int j = 0; j < uploadedFiles.size(); j++) {
									UploadedFileWO uf = (UploadedFileWO) uploadedFiles.get(j);
									
									TrcFinePicFollowupAttachment vo = new TrcFinePicFollowupAttachment();
									
									vo.setTrcFinePicFollowup(trcFinePicFollowupNew);
									vo.setAttachmentFile(uf.getFileName());
									vo.setCreatedBy(facesUtil.retrieveUserLogin());
									vo.setCreationDate(new Timestamp(new Date().getTime()));
									vo.setDelId(new Long(0));
									vo.setEnabledFlag(Constants.CONSTANT_YES);
									vo.setFileId(uf.getFileId());
									vo.setFileSize(uf.getFileSize());
									
									vo.setAttachmentFrom(isExtended.equals("Y") ? ParameterDetail.FINE_FOLLOWUP_ATTACHMENT_TYPE_EXTENDED 
											: ParameterDetail.FINE_FOLLOWUP_ATTACHMENT_TYPE_FOLLOWUP);
									
									
									listDtl.add(vo);
									
								}
								trcFinePicFollowupNew.setTrcFinePicFollowupAttachments(listDtl);
							}else {
								listDtlNew = new ArrayList<TrcFinePicFollowupAttachment>();
								
								for (int j = 0; j < uploadedFiles.size(); j++) {
									UploadedFileWO uf = (UploadedFileWO) uploadedFiles.get(j);
									boolean exist = false;
									
									for(int x=0; x < listDtl.size(); x++) {
										TrcFinePicFollowupAttachment at = listDtl.get(x);
										
										if(at.getFileId().equals(uf.getFileId())) {
											//already have
											if(at.getFinePicFollowupAttachmentId() != null) {
												at.setLastUpdateBy(facesUtil.retrieveUserLogin());
												at.setLastUpdateDate(new Timestamp(new Date().getTime()));
											}
											exist = true;
											break;
										}
									}
									
									if(!exist) {
										//new file
										TrcFinePicFollowupAttachment vo = new TrcFinePicFollowupAttachment();
										
										vo.setTrcFinePicFollowup(trcFinePicFollowupNew);
										vo.setAttachmentFile(uf.getFileName());
										vo.setCreatedBy(facesUtil.retrieveUserLogin());
										vo.setCreationDate(new Timestamp(new Date().getTime()));
										vo.setDelId(new Long(0));
										vo.setEnabledFlag(Constants.CONSTANT_YES);
										vo.setFileId(uf.getFileId());
										vo.setFileSize(uf.getFileSize());
										
										vo.setAttachmentFrom(isExtended.equals("Y") ? ParameterDetail.FINE_FOLLOWUP_ATTACHMENT_TYPE_EXTENDED 
												: ParameterDetail.FINE_FOLLOWUP_ATTACHMENT_TYPE_FOLLOWUP);
										
										listDtl.add(vo);
									}
								}
								
								for(int x=0; x < listDtl.size(); x++) {
									TrcFinePicFollowupAttachment at = listDtl.get(x);
									boolean exist = false;
									
									for (int j = 0; j < uploadedFiles.size(); j++) {
										UploadedFileWO uf = (UploadedFileWO) uploadedFiles.get(j);
										
										if(at.getFileId().equals(uf.getFileId())) {
											exist = true;
											break;
										}
									
									}
									
									if(!exist) {
										listDtl.remove(at);
										x--;
									}
									
								}
								
							}
								
						}
					}
				}
				
				trcFine.setLastUpdateBy(facesUtil.retrieveUserLogin());
				trcFine.setLastUpdateDate(new Timestamp(new Date().getTime()));
				trcFine.setDelId(new Long(0));
				trcFine.setEnabledFlag(Constants.CONSTANT_YES);
				fineFEService.update(trcFine);
				
				
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

				facesUtil.redirect("/pages/fineFE/fineFE.faces");
				
			}

		} catch (Exception ex) {
			facesUtil.addFacesMsg(FacesMessage.SEVERITY_ERROR, null, "Operation Failed : " + ex.getMessage(), "");
			ex.printStackTrace();
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
			emailContent = emailTemplate.getEmailContent().replace(Constants.CONFIRMATION_TYPE_AND_DOC_NUM, "Denda" + " - " + trcFine.getLetterNo());
			emailSubject = emailSubject.replace("letter_no","Denda" + " - " + trcFine.getLetterNo())
					.replace(Constants.CONFIRMATION_TYPE_AND_DOC_NUM, "Denda" + " - " + trcFine.getLetterNo())
					.replaceAll("counter_type","");

			for(int x=0;x<trcFine.getTrcFinePicCompliances().size();x++) {
				TrcFinePicCompliance cd = trcFine.getTrcFinePicCompliances().get(x);
				emailTo = cd.getUser().getEmail();
				
//						ExecutorService emailExecutor = Executors.newCachedThreadPool();

				final String subject = emailSubject;
				final String content = emailContent;
				final String to = emailTo;
				//final String to = "h3ndr407@gmail.com";
				final String cc = emailCc;
				
				CallApiManager.sendEmailAPI(to, cc, subject, content, "EMAIL_PIC_COMPLIANCE", "true", parameterDetailService);
				
		        
			}
		    
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new CustomAPIException(ex.getMessage()); 
		}
		
	}
	
	public void cancel() {
		try {
			if(uploadedFiles != null) {
				for (int i = 0; i < uploadedFiles.size(); i++) {
					UploadedFileWO uf = (UploadedFileWO) uploadedFiles.get(i);
					if(uf.getIsNew() == null) {
						CallApiManager.deleteFile(uf.getFileId(), parameterDetailService, fileUtil);
					}
				}
			}
			
			facesUtil.setSessionAttribute("BACK_SESSION", true);
			facesUtil.redirect("/pages/fineFE/fineFE.faces");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void downloadFile(String fileId, String fileName, byte[] content) throws Exception {
		CallApiManager.downloadFile(fileId, fileName, content, parameterDetailService);
	}
	
	public void deleteAttachment(String fileId,int index,String uploadType) throws Exception {
		deletedFiles = deletedFiles!=null?deletedFiles: new ArrayList<UploadedFileWO>();
		deletedFiles.add(new UploadedFileWO(fileId,null,null, null));
		if (uploadType != null && uploadType.equals(FineFEConstant.UPLOAD_TYPE_DOCUMENT))
			uploadedFiles.remove(uploadedFiles.get(index));
	}
	
	public FineFEService getFineFEService() {
		return fineFEService;
	}

	public void setFineFEService(FineFEService fineFEService) {
		this.fineFEService = fineFEService;
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

	public TrcFine getTrcFine() {
		return trcFine;
	}

	public void setTrcFine(TrcFine trcFine) {
		this.trcFine = trcFine;
	}

	public String getEditId() {
		return editId;
	}

	public void setEditId(String editId) {
		this.editId = editId;
	}

	public List<UploadedFileWO> getDeletedFiles() {
		return deletedFiles;
	}

	public void setDeletedFiles(List<UploadedFileWO> deletedFiles) {
		this.deletedFiles = deletedFiles;
	}

	public List<UploadedFileWO> getUploadedFiles() {
		return uploadedFiles;
	}

	public void setUploadedFiles(List<UploadedFileWO> uploadedFiles) {
		this.uploadedFiles = uploadedFiles;
	}

	public FacesUtil getFacesUtil() {
		return facesUtil;
	}

	public void setFacesUtil(FacesUtil facesUtil) {
		this.facesUtil = facesUtil;
	}

	public FileUtil getFileUtil() {
		return fileUtil;
	}

	public void setFileUtil(FileUtil fileUtil) {
		this.fileUtil = fileUtil;
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

	public String getPengirim() {
		return pengirim;
	}

	public void setPengirim(String pengirim) {
		this.pengirim = pengirim;
	}

	public String getTanggalTerimaSurat() {
		return tanggalTerimaSurat;
	}

	public void setTanggalTerimaSurat(String tanggalTerimaSurat) {
		this.tanggalTerimaSurat = tanggalTerimaSurat;
	}

	public String getPerihal() {
		return perihal;
	}

	public void setPerihal(String perihal) {
		this.perihal = perihal;
	}

	public String getTanggalSurat() {
		return tanggalSurat;
	}

	public void setTanggalSurat(String tanggalSurat) {
		this.tanggalSurat = tanggalSurat;
	}

	public String getTipeSurat() {
		return tipeSurat;
	}

	public void setTipeSurat(String tipeSurat) {
		this.tipeSurat = tipeSurat;
	}

	public String getCatatanComplinace() {
		return catatanComplinace;
	}

	public void setCatatanComplinace(String catatanComplinace) {
		this.catatanComplinace = catatanComplinace;
	}

	public SimpleDateFormat getSdf() {
		return sdf;
	}

	public void setSdf(SimpleDateFormat sdf) {
		this.sdf = sdf;
	}

	public String getTglTindakLanjut() {
		return tglTindakLanjut;
	}

	public void setTglTindakLanjut(String tglTindakLanjut) {
		this.tglTindakLanjut = tglTindakLanjut;
	}

	public String getKeterangan() {
		return keterangan;
	}

	public void setKeterangan(String keterangan) {
		this.keterangan = keterangan;
	}

	public String getFollowupNotes() {
		return followupNotes;
	}

	public void setFollowupNotes(String followupNotes) {
		this.followupNotes = followupNotes;
	}

	public TrcFinePicFollowup getTrcFinePicFollowup() {
		return trcFinePicFollowup;
	}

	public void setTrcFinePicFollowup(TrcFinePicFollowup trcFinePicFollowup) {
		this.trcFinePicFollowup = trcFinePicFollowup;
	}

	public TrcFinePicFollowupService getTrcFinePicFollowupService() {
		return trcFinePicFollowupService;
	}

	public void setTrcFinePicFollowupService(TrcFinePicFollowupService trcFinePicFollowupService) {
		this.trcFinePicFollowupService = trcFinePicFollowupService;
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

	public RCService getRcService() {
		return rcService;
	}

	public void setRcService(RCService rcService) {
		this.rcService = rcService;
	}

	public List<SelectItem> getRcList() {
		return rcList;
	}

	public void setRcList(List<SelectItem> rcList) {
		this.rcList = rcList;
	}

	public List<SelectItem> getCategoryList() {
		return categoryList;
	}

	public void setCategoryList(List<SelectItem> categoryList) {
		this.categoryList = categoryList;
	}

	public Boolean getIsRevise() {
		return isRevise;
	}

	public void setIsRevise(Boolean isRevise) {
		this.isRevise = isRevise;
	}

	public String getSelectedRcId() {
		return selectedRcId;
	}

	public void setSelectedRcId(String selectedRcId) {
		this.selectedRcId = selectedRcId;
	}

	public String getVerifyStatusName() {
		return verifyStatusName;
	}

	public void setVerifyStatusName(String verifyStatusName) {
		this.verifyStatusName = verifyStatusName;
	}

	public Boolean getVerifyActive() {
		return verifyActive;
	}

	public void setVerifyActive(Boolean verifyActive) {
		this.verifyActive = verifyActive;
	}

	public String getIsExtended() {
		return isExtended;
	}

	public void setIsExtended(String isExtended) {
		this.isExtended = isExtended;
	}
	
	
}
