package com.wo.module.regulationSocializationApproval.bean;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.exception.CustomAPIException;
import com.wo.module.common.model.UploadedFileWO;
import com.wo.module.common.paging.DefaultSearchObject;
import com.wo.module.common.utility.CallApiManager;
import com.wo.module.counterType.model.CounterTypeDtl;
import com.wo.module.counterType.service.CounterTypeService;
import com.wo.module.documentCategory.constant.DocumentCategoryConstants;
import com.wo.module.documentCategory.model.DocumentCategory;
import com.wo.module.documentCategory.service.DocumentCategoryService;
import com.wo.module.documentTopic.constant.DocumentTopicConstants;
import com.wo.module.documentTopic.model.DocumentTopic;
import com.wo.module.documentTopic.service.DocumentTopicService;
import com.wo.module.documentType.constant.DocumentTypeConstants;
import com.wo.module.documentType.model.DocumentType;
import com.wo.module.documentType.service.DocumentTypeService;
import com.wo.module.emailTemplate.model.EmailTemplate;
import com.wo.module.emailTemplate.service.EmailTemplateService;
import com.wo.module.externalRegulation.constant.ExternalRegulationConstants;
import com.wo.module.externalRegulation.model.Regulation;
import com.wo.module.externalRegulation.model.RegulationTrackRecord;
import com.wo.module.externalRegulation.service.RegulationService;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.lov.bean.SelectorModel.SelectorInfo;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.parameter.model.ParameterHeader;
import com.wo.module.parameter.service.ParameterDetailService;
import com.wo.module.regulationSocialization.constant.RegulationSocializationConstants;
import com.wo.module.regulationSocialization.model.SocializationDocumentTmp;
import com.wo.module.regulationSocialization.model.SocializationPICComplianceTableModel;
import com.wo.module.regulationSocialization.model.SocializationPICComplianceTmp;
import com.wo.module.regulationSocialization.model.SocializationPICFollowupTableModel;
import com.wo.module.regulationSocialization.model.SocializationPICFollowupTmp;
import com.wo.module.regulationSocialization.model.SocializationRegulationTableModel;
import com.wo.module.regulationSocialization.model.SocializationRegulationTmp;
import com.wo.module.regulationSocialization.model.SocializationTmp;
import com.wo.module.regulationSocialization.service.RegulationSocializationService;
import com.wo.module.regulationSocialization.service.SocializationTmpService;
import com.wo.module.regulationSocialization.vo.SocializationApprovalVO;
import com.wo.module.regulationSocialization.vo.StatusConfirmationVO;
import com.wo.module.regulationSocializationApproval.service.RegulationSocializationApprovalService;
import com.wo.module.user.model.Division;
import com.wo.module.user.model.User;
import com.wo.module.user.service.UserService;

public class RegulationSocializationApprovalEditBean extends CommonBean implements Serializable {

	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(RegulationSocializationApprovalBean.class);

	private SocializationTmp socializationTmp;

	private Boolean isViewOnly;

	private String actionMode;

	private String editedId;

	private Long docTypeId;

	private Long docCategoryId;

	private Long docTopicId;

	private Long counterTypeId;
	
	private String approvalStat;

	private List<SelectItem> divisions;

	private List<SelectItem> followUps;

	private List<SelectItem> provTypes;

	private List<SelectItem> trackCodes;

	private List<SelectItem> docTypes;

	private List<SelectItem> docCategories;

	private List<SelectItem> docTopics;

	private List<SelectItem> counterTypes;

	private List<SelectItem> reminderStatusList;
	
	private List<SelectItem> approvalStatus;

	private SelectorInfo selectorJdlPeraturan;

	private SelectorInfo selectorCompliance;

	private SelectorInfo selectorFollowup;

	private SelectorInfo selectorPic1;

	private SelectorInfo selectorPic2;

	private SelectorInfo selectorPic3;

	private SocializationRegulationTmp[] selectedData;

	private SocializationPICComplianceTmp[] selectedDataCompliance;

	private SocializationPICFollowupTmp[] selectedDataFollowup;

	private SocializationRegulationTableModel<SocializationRegulationTmp> tableModel;

	private SocializationPICComplianceTableModel<SocializationPICComplianceTmp> tableModelCompliance;

	private SocializationPICFollowupTableModel<SocializationPICFollowupTmp> tableModelFollowup;

	private List<StatusConfirmationVO> tableStatus;

	private List<SocializationApprovalVO> tableApproval;

	private RegulationTrackRecord selectedRow;

	private Integer indexDtl;

	private Integer indexDtlCompliance;

	private Integer indexDtlFollowup;

	private RegulationSocializationApprovalService regulationSocializationApprovalService;

	private RegulationSocializationService regulationSocializationService;

	private SocializationTmpService socializationTmpService;

	private DocumentTypeService documentTypeService;

	private DocumentCategoryService documentCategoryService;

	private DocumentTopicService documentTopicService;

	private RegulationService regulationService;

	private UserService userService;

	private CounterTypeService counterTypeService;
	
	private EmailTemplateService emailTemplateService;

	public FacesUtil facesUtil;

	private String navigateSearch = ExternalRegulationConstants.NAVIGATE_SEARCH;

	private String note;
	
	private List<UploadedFileWO> uploadFiles;

	SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");

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
		selectDivision();
		selectFollowup();
		selectProvType();
		selectTrackCode();
		selectDocType();
		selectDocCategory();
		selectDocTopic();
		selectCounterType();
		selectReminderStatus();
		selectApprovalStatus();
		
		selectorJdlPeraturan = RegulationSocializationConstants.buildSelectorJdlPeraturan(facesUtil);
		selectorCompliance = RegulationSocializationConstants.buildSelectorPICCompliance(facesUtil);
		selectorFollowup = RegulationSocializationConstants.buildSelectorDivision(facesUtil);
		selectorPic1 = RegulationSocializationConstants.buildSelectorPIC(facesUtil);
		selectorPic2 = RegulationSocializationConstants.buildSelectorPIC(facesUtil);
		selectorPic3 = RegulationSocializationConstants.buildSelectorPIC(facesUtil);

		tableApproval = new ArrayList<SocializationApprovalVO>();
		tableStatus = new ArrayList<StatusConfirmationVO>();
		checkNewOrEdit();
	}
	
	public void selectApprovalStatus() {
		approvalStatus = new ArrayList<SelectItem>();
		try {
			approvalStatus = parameterDetailService.getListLabelValue(ParameterHeader.PARAM_HEAD_CODE_APPROVAL_STATUS,
					false, true, facesUtil.retrieveDefaultLocale());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void selectReminderStatus() {
		reminderStatusList = new ArrayList<SelectItem>();
		try {
			List<ParameterDetail> listParamDtl = parameterDetailService
					.getParameterDetailByParamCode(ParameterHeader.PARAM_HEAD_CODE_REMINDER_STATUS);

			for (ParameterDetail vo : listParamDtl) {
				SelectItem si = new SelectItem();
				si.setLabel(vo.getName());
				si.setValue(vo.getParameterDtlCode());
				reminderStatusList.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void selectFollowup() {
		followUps = new ArrayList<SelectItem>();
		SelectItem si = new SelectItem();
		si.setLabel("Yes");
		si.setValue("Y");
		followUps.add(si);

		SelectItem si2 = new SelectItem();
		si2.setLabel("No");
		si2.setValue("N");
		followUps.add(si2);
	}

	public void selectDivision() {
		divisions = new ArrayList<SelectItem>();
		try {
			List<Division> pd = userService.getAllDivision();
			for (int i = 0; i < pd.size(); i++) {
				SelectItem si = new SelectItem();
				si.setLabel(((Division) pd.get(i)).getDivisionName());
				si.setValue(((Division) pd.get(i)).getDivisionId());
				divisions.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void selectProvType() {
		provTypes = new ArrayList<SelectItem>();
		try {
			List<ParameterDetail> pd = parameterDetailService.getParameterDetailByParamCode("JENIS_KETENTUAN");
			for (int i = 0; i < pd.size(); i++) {
				SelectItem si = new SelectItem();
				si.setLabel(((ParameterDetail) pd.get(i)).getName());
				si.setValue(((ParameterDetail) pd.get(i)).getParameterDtlCode());
				provTypes.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void selectTrackCode() {
		trackCodes = new ArrayList<SelectItem>();
		try {
			List<ParameterDetail> pd = parameterDetailService.getParameterDetailByParamCode("TRACK_RECORD");
			for (int i = 0; i < pd.size(); i++) {
				SelectItem si = new SelectItem();
				si.setLabel(((ParameterDetail) pd.get(i)).getName());
				si.setValue(((ParameterDetail) pd.get(i)).getParameterDtlCode());
				trackCodes.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void selectDocType() {
		docTypes = new ArrayList<SelectItem>();
		try {
			List<DocumentType> pd = documentTypeService.searchData(
					Arrays.asList(new DefaultSearchObject(DocumentTypeConstants.WHERE_JENIS_KETENTUAN_CODE, null)), 0,
					Integer.MAX_VALUE, null, null);
			for (int i = 0; i < pd.size(); i++) {
				SelectItem si = new SelectItem();
				si.setLabel(((DocumentType) pd.get(i)).getDocumentType());
				si.setValue(((DocumentType) pd.get(i)).getDocumentTypeId());
				docTypes.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void selectDocCategory() {
		docCategories = new ArrayList<SelectItem>();
		try {
			List<DocumentCategory> pd = documentCategoryService.searchData(
					Arrays.asList(new DefaultSearchObject(DocumentCategoryConstants.WHERE_JENIS_KETENTUAN_CODE, null)), 0,
					Integer.MAX_VALUE, null, null);
			for (int i = 0; i < pd.size(); i++) {
				SelectItem si = new SelectItem();
				si.setLabel(((DocumentCategory) pd.get(i)).getDocumentCategory());
				si.setValue(((DocumentCategory) pd.get(i)).getDocumentCategoryId());
				docCategories.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void selectDocTopic() {
		docTopics = new ArrayList<SelectItem>();
		try {
			List<DocumentTopic> pd = documentTopicService.searchData(
					Arrays.asList(new DefaultSearchObject(DocumentTopicConstants.WHERE_JENIS_KETENTUAN_CODE, null)), 0,
					Integer.MAX_VALUE, null, null);
			for (int i = 0; i < pd.size(); i++) {
				SelectItem si = new SelectItem();
				si.setLabel(((DocumentTopic) pd.get(i)).getDocumentTopic());
				si.setValue(((DocumentTopic) pd.get(i)).getDocumentTopicId());
				docCategories.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void selectCounterType() {
		try {
			counterTypes = counterTypeService.getAllCounterTypeLabelValue();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void checkNewOrEdit() {
		String editId = facesUtil.retrieveRequestParam("id");String token = facesUtil.retrieveRequestParam("token"); 
		
		String viewId = facesUtil.retrieveRequestParam("viewId");
		isViewOnly = false;
		if (viewId != null && !viewId.isEmpty()) {
			if (viewId.trim().equalsIgnoreCase("true")) {
				isViewOnly = true;
			}
		}
		if (StringUtils.isBlank(editId) && StringUtils.isBlank(token)) {
			this.handleNew();
		} else {
			this.handleEdit(editId);
		}
	}

	private void handleNew() {
		try {
			socializationTmp = new SocializationTmp();

			ParameterDetail pd = parameterDetailService.getParameterDetailByParamDtlCode("KETENTUAN_EKSTERNAL");
			socializationTmp.setJenisKetentuan(pd);

			List<SocializationRegulationTmp> srList = new ArrayList<SocializationRegulationTmp>();
			SocializationRegulationTmp sr = new SocializationRegulationTmp();
			Regulation reg = new Regulation();
			DocumentType dt = new DocumentType();
			DocumentCategory dc = new DocumentCategory();
			DocumentTopic dto = new DocumentTopic();
			reg.setDocumentType(dt);
			reg.setDocumentCategory(dc);
			reg.setDocumentTopic(dto);
			sr.setRegulation(reg);
			srList.add(sr);
			socializationTmp.setSocializationRegulationTmps(srList);

			List<SocializationPICComplianceTmp> listRt = new ArrayList<SocializationPICComplianceTmp>();
			SocializationPICComplianceTmp sp = new SocializationPICComplianceTmp();
			User user = (User) facesUtil.getSessionAttribute(Constants.SESSION_EMPLOYEE);
			sp.setUser(user);
			listRt.add(sp);
			socializationTmp.setSocializationPICComplianceTmps(listRt);

			List<SocializationPICFollowupTmp> listFp = new ArrayList<SocializationPICFollowupTmp>();
			SocializationPICFollowupTmp fp = new SocializationPICFollowupTmp();
			fp.setUser1(new User());
			fp.setUser2(new User());
			fp.setUser3(new User());
			listFp.add(fp);
			socializationTmp.setSocializationPICFollowupTmps(listFp);

			actionMode = Constants.ACTION_ADD;
			tableModel = new SocializationRegulationTableModel<SocializationRegulationTmp>(
					socializationTmp.getSocializationRegulationTmps());

			tableModelCompliance = new SocializationPICComplianceTableModel<SocializationPICComplianceTmp>(
					socializationTmp.getSocializationPICComplianceTmps());

			tableModelFollowup = new SocializationPICFollowupTableModel<SocializationPICFollowupTmp>(
					socializationTmp.getSocializationPICFollowupTmps());
			
			facesUtil.setSessionAttribute("token", null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void handleEdit(String editId) {
		String token = facesUtil.retrieveRequestParam("token");
		if(StringUtils.isNotEmpty(token)) {
			editId = Constants.decryptString(token);
		}
		facesUtil.setSessionAttribute("token", Constants.encryptString(editId));
		actionMode = Constants.ACTION_EDIT;
		Long idLong = Long.parseLong(editId);
		socializationTmp = socializationTmpService.findById(idLong);
		if(socializationTmp.getCounterType()!=null) {
		counterTypeId = socializationTmp.getCounterType().getCounterTypeId();
		}
		
		uploadFiles = new ArrayList<UploadedFileWO>();
		for (int i = 0; i < socializationTmp.getSocializationDocumentTmps().size(); i++) {
			SocializationDocumentTmp ra = socializationTmp.getSocializationDocumentTmps().get(i);
			UploadedFileWO uf = new UploadedFileWO();
			uf.setFileName(ra.getAttachmentFile());
			uf.setFileId(ra.getFileId());
			uf.setIsNew(false);
			uf.setFileSize(ra.getFileSize());
			uploadFiles.add(uf);
			
		}
		
		tableModel = new SocializationRegulationTableModel<SocializationRegulationTmp>(socializationTmp.getSocializationRegulationTmps());
		tableModelCompliance = new SocializationPICComplianceTableModel<SocializationPICComplianceTmp>(
				socializationTmp.getSocializationPICComplianceTmps());
		tableModelFollowup = new SocializationPICFollowupTableModel<SocializationPICFollowupTmp>(
				socializationTmp.getSocializationPICFollowupTmps());

		tableApproval = regulationSocializationService.getDataApprovalBySocializationId(idLong);
		tableStatus = regulationSocializationService.getDataConfirmStatusBySocializationId(idLong);

	}
	
	public String replaceAll(String INPUT,String REGEX,String REPLACE) {
		Pattern p = Pattern.compile(REGEX);
	      //get a matcher object
	      Matcher m = p.matcher(INPUT);
	      INPUT = m.replaceAll(REPLACE);
		return INPUT;
	}
	
	@SuppressWarnings("unused")
	public void sendEmail() {
		try {
			
			EmailTemplate emailTemplate = emailTemplateService.getEmailTemplateByEmailTemplateCode("EMAIL_SOSIALISASI");
			String emailSubject = replaceAll(emailTemplate.getEmailSubject(),"counter_type","NOTIFICATION");
				   emailSubject = replaceAll(emailSubject,"regulation_title_in",socializationTmp.getSocializationRegulationTmps().get(0).getRegulation().getNameIn());
				   emailSubject = replaceAll(emailSubject,"regulation_title_en",socializationTmp.getSocializationRegulationTmps().get(0).getRegulation().getNameEn());
			String emailContent = ""; 
			String emailTo  = "";
			String emailCc1 = "";
			String emailCc2 = "";
			String emailCc = "";
			String emailCcCompliance = "";
			
			ParameterDetail pdHostName = parameterDetailService.getParameterDetailByParamDtlCode("HOST_NAME_APPLICATION");
			if (socializationTmp.getReminderStatus()!=null && socializationTmp.getReminderStatus().equals("REMINDER_ACTIVE") && 
		    		//socializationTmp.getFollowUp() != null && socializationTmp.getFollowUp().equals("Y")
		    		socializationTmp.getSocializationPICFollowupTmps() != null && socializationTmp.getSocializationPICFollowupTmps().size() > 0
		    		) {
				for (int i = 0; i < socializationTmp.getSocializationPICFollowupTmps().size(); i++) {
					SocializationPICFollowupTmp tmp = socializationTmp.getSocializationPICFollowupTmps().get(i);
					emailContent = emailTemplate.getEmailContent().replaceAll("target_date", tmp.getTargetDate()!=null?sdf.format(tmp.getTargetDate()):"");
					emailContent = emailContent.replaceAll("regulation_title_in", socializationTmp.getSocializationRegulationTmps().get(0).getRegulation().getNameIn());
					emailContent = emailContent.replaceAll("regulation_title_en", socializationTmp.getSocializationRegulationTmps().get(0).getRegulation().getNameEn());
					emailContent = emailContent.replaceAll("document_number", socializationTmp.getSocializationRegulationTmps().get(0).getRegulation().getDocumentNo());
					emailContent = emailContent.replaceAll("published_date", socializationTmp.getSocializationRegulationTmps().get(0).getRegulation().getPublishedDate()!=null?sdf.format(socializationTmp.getSocializationRegulationTmps().get(0).getRegulation().getPublishedDate()):"");
					emailContent = emailContent.replaceAll("effective_date", socializationTmp.getSocializationRegulationTmps().get(0).getRegulation().getEffectiveDate()!=null?sdf.format(socializationTmp.getSocializationRegulationTmps().get(0).getRegulation().getEffectiveDate()):"");
					String token = "";
					String urlLink = "";
					String menuId = "";
					if (socializationTmp.getFollowUp() != null && socializationTmp.getFollowUp().equals("Y")) {
						token = Constants.encryptString(tmp.getSocializationPicFollowupId().toString());
						menuId = Constants.encryptString(Constants.MENU_ID_FOLLOWUP_CONFIRMATION_SOCIALIZATION);
						urlLink = pdHostName.getNameIn().concat("pages/picFollowupConfirmation/picFollowupConfirmationEdit.faces?token="+token+"&menuId="+menuId);
					} else if (socializationTmp.getFollowUp() != null && socializationTmp.getFollowUp().equals("N")) {
						token = Constants.encryptString(socializationTmp.getSocializationId().toString());
						menuId = Constants.encryptString(Constants.MENU_ID_SOCIALIZATION_VIEW);
						urlLink = pdHostName.getNameIn().concat("pages/regulationSocializationView/regulationSocializationViewDetail.faces?token="+token+"&menuId="+menuId);
					}
					emailContent = emailContent.replaceAll("url_link", urlLink);
					emailCc = "";
					emailCcCompliance = "";
					
					if (socializationTmp != null) {
						if (socializationTmp.getSocializationPICComplianceTmps() != null && socializationTmp.getSocializationPICComplianceTmps().size() > 0) {
							for (SocializationPICComplianceTmp spct : socializationTmp.getSocializationPICComplianceTmps()) {
								User ue = null;
								if (spct.getUser() != null) {
									ue = userService.findById(spct.getUser().getUserId());
									if (ue != null && StringUtils.isNotBlank(ue.getEmail())) {
										emailCcCompliance = StringUtils.isNotEmpty(emailCcCompliance)?emailCcCompliance.concat(",").concat(ue.getEmail()):emailCcCompliance.concat(ue.getEmail());
									}
								}
							}
						}
					}
					
//					for(int x=0;x<socializationTmp.getCounterType().getDetails().size();x++) {
					if (socializationTmp != null) {
						if (socializationTmp.getCounterType() != null && socializationTmp.getCounterType().getDetails() != null && socializationTmp.getCounterType().getDetails().size() > 0) {
							CounterTypeDtl cd = socializationTmp.getCounterType().getDetails().get(0);
							emailTo = cd.getEmailTo();
							emailCc1 = cd.getEmailCc1();
							emailCc2 = cd.getEmailCc2();
							
							if(emailTo.equals(Constants.REMINDER_PIC1)) {
								emailTo = tmp.getUser1()!=null?tmp.getUser1().getEmail():"";
							}
							else if(emailTo.equals(Constants.REMINDER_PIC2)) {
								emailTo = tmp.getUser2()!=null?tmp.getUser2().getEmail():"";
							}
							else if(emailTo.equals(Constants.REMINDER_PIC3)) {
								emailTo = tmp.getUser3()!=null?tmp.getUser3().getEmail():"";
							}
							
							if(emailCc1!=null && emailCc1.equals(Constants.REMINDER_PIC1)) {
								emailCc1 = tmp.getUser1()!=null?tmp.getUser1().getEmail():"";
							}
							else if(emailCc1!=null && emailCc1.equals(Constants.REMINDER_PIC2)) {
								emailCc1 = tmp.getUser2()!=null?tmp.getUser2().getEmail():"";
							}
							else if(emailCc1!=null && emailCc1.equals(Constants.REMINDER_PIC3)) {
								emailCc1 = tmp.getUser3()!=null?tmp.getUser3().getEmail():"";
							}
							
							if(emailCc2!=null && emailCc2.equals(Constants.REMINDER_PIC1)) {
								emailCc2 = tmp.getUser1()!=null?tmp.getUser1().getEmail():"";
							}
							else if(emailCc2!=null && emailCc2.equals(Constants.REMINDER_PIC2)) {
								emailCc2 = tmp.getUser2()!=null?tmp.getUser2().getEmail():"";
							}
							else if(emailCc2!=null && emailCc2.equals(Constants.REMINDER_PIC3)) {
								emailCc2 = tmp.getUser3()!=null?tmp.getUser3().getEmail():"";
							}
							
							
							
//					        emailExecutor.execute(new Runnable() {
//					            @Override
//					            public void run() {
//									
//					            	try {
//										//CallApiManager.sendEmail(subject, content, to,cc, parameterDetailService);
//										CallApiManager.sendEmailAPI(to,cc, subject,
//												content, "EMAIL_SOSIALISASI", "true", parameterDetailService);
//									} catch (Exception e) {
//										e.printStackTrace();
//									}
//					            }
//					        });
					        
							
							
						} else {
							emailTo = tmp.getUser1() != null ? tmp.getUser1().getEmail() : "";
							emailCc1 = tmp.getUser2() != null ? tmp.getUser2().getEmail() : "";
							emailCc2 = tmp.getUser3() != null ? tmp.getUser3().getEmail() : "";
						}
						
						if(StringUtils.isNotEmpty(emailCc1)) {
							emailCc = emailCc.concat(emailCc1);
						}
						if(StringUtils.isNotEmpty(emailCc2)) {
							emailCc = StringUtils.isNotEmpty(emailCc)?emailCc.concat(",").concat(emailCc2):emailCc.concat(emailCc2);
						}
						if (StringUtils.isNotEmpty(emailCcCompliance)) {
							emailCc = StringUtils.isNotEmpty(emailCc)?emailCc.concat(",").concat(emailCcCompliance):emailCc.concat(emailCcCompliance);
						}
						
						ExecutorService emailExecutor = Executors.newCachedThreadPool();

						final String subject = emailSubject;
						final String content = emailContent;
						final String to = emailTo;
						//final String to = "h3ndr407@gmail.com";
						final String cc = emailCc;
						
						CallApiManager.sendEmailAPI(to,cc, subject,
								content, "EMAIL_SOSIALISASI", "true", parameterDetailService);
					}
					
					
				}
			}
		    
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new CustomAPIException(ex.getMessage()); 
		}
		
	}

	public void save() {
		if (StringUtils.isBlank(approvalStat)) {
			facesUtil.addErrMessage(facesUtil.retrieveMessage("textApprovalStatus") + " "
					+ facesUtil.retrieveMessage("validateRequired"));
		} else if (ParameterDetail.PARAM_DET_CODE_STATUS_REVISE.equals(approvalStat)) {
			revise();
		} else if (ParameterDetail.PARAM_DET_CODE_STATUS_APPROVED.equals(approvalStat)) {
			approve();
		}
	}

	public void approve() {
		try {
			regulationSocializationApprovalService.processApprove(socializationTmp, note, facesUtil.retrieveUserLogin(),
					"DATA_ACTIVE", "STATUS_APPROVED");
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
			facesUtil.redirect("/pages/regulationSocializationApproval/regulationSocializationApproval.faces");
		} catch (Exception ex) {
			facesUtil.addFacesMsg(FacesMessage.SEVERITY_ERROR, null, "Operation Failed : " + ex.getMessage(), "");
			ex.printStackTrace();
		}
	}

	public void revise() {
		try {

			if (StringUtils.isEmpty(note)) {
				facesUtil.addErrMessage(facesUtil.retrieveMessage("formExternalRegulationApprovalNote") + " "
						+ facesUtil.retrieveMessage("validateRequired"));
			} else {
				regulationSocializationApprovalService.processApprove(socializationTmp, note,
						facesUtil.retrieveUserLogin(), "DATA_REVISE", "STATUS_REVISE");
				facesUtil.redirect("/pages/regulationSocializationApproval/regulationSocializationApproval.faces");
			}
		} catch (Exception ex) {
			facesUtil.addFacesMsg(FacesMessage.SEVERITY_ERROR, null, "Operation Failed : " + ex.getMessage(), "");
			ex.printStackTrace();
		}
	}
	
	public void downloadFile(String fileId, String fileName, byte[] content) throws Exception {
		CallApiManager.downloadFile(fileId, fileName, content, parameterDetailService);
	}

	public void cancel() {
		try {
			facesUtil.redirect("/pages/regulationSocializationApproval/regulationSocializationApproval.faces");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public FacesUtil getFacesUtil() {
		return facesUtil;
	}

	public void setFacesUtil(FacesUtil facesUtil) {
		this.facesUtil = facesUtil;
	}

	public RegulationSocializationService getRegulationSocializationService() {
		return regulationSocializationService;
	}

	public void setRegulationSocializationService(RegulationSocializationService regulationSocializationService) {
		this.regulationSocializationService = regulationSocializationService;
	}

	public String getNavigateSearch() {
		return navigateSearch;
	}

	public void setNavigateSearch(String navigateSearch) {
		this.navigateSearch = navigateSearch;
	}

	public Boolean getIsViewOnly() {
		return isViewOnly;
	}

	public void setIsViewOnly(Boolean isViewOnly) {
		this.isViewOnly = isViewOnly;
	}

	public String getActionMode() {
		return actionMode;
	}

	public void setActionMode(String actionMode) {
		this.actionMode = actionMode;
	}

	public String getEditedId() {
		return editedId;
	}

	public void setEditedId(String editedId) {
		this.editedId = editedId;
	}

	public List<javax.faces.model.SelectItem> getProvTypes() {
		return provTypes;
	}

	public void setProvTypes(List<javax.faces.model.SelectItem> provTypes) {
		this.provTypes = provTypes;
	}

	public List<SelectItem> getDocTypes() {
		return docTypes;
	}

	public void setDocTypes(List<SelectItem> docTypes) {
		this.docTypes = docTypes;
	}

	public List<SelectItem> getDocCategories() {
		return docCategories;
	}

	public void setDocCategories(List<SelectItem> docCategories) {
		this.docCategories = docCategories;
	}

	public List<SelectItem> getDocTopics() {
		return docTopics;
	}

	public void setDocTopics(List<SelectItem> docTopics) {
		this.docTopics = docTopics;
	}

	public SimpleDateFormat getSdf() {
		return sdf;
	}

	public void setSdf(SimpleDateFormat sdf) {
		this.sdf = sdf;
	}

	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		RegulationSocializationApprovalEditBean.logger = logger;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	

	public Long getDocTypeId() {
		return docTypeId;
	}

	public void setDocTypeId(Long docTypeId) {
		this.docTypeId = docTypeId;
	}

	public Long getDocCategoryId() {
		return docCategoryId;
	}

	public void setDocCategoryId(Long docCategoryId) {
		this.docCategoryId = docCategoryId;
	}

	public Long getDocTopicId() {
		return docTopicId;
	}

	public void setDocTopicId(Long docTopicId) {
		this.docTopicId = docTopicId;
	}

	public DocumentTypeService getDocumentTypeService() {
		return documentTypeService;
	}

	public void setDocumentTypeService(DocumentTypeService documentTypeService) {
		this.documentTypeService = documentTypeService;
	}

	public DocumentCategoryService getDocumentCategoryService() {
		return documentCategoryService;
	}

	public void setDocumentCategoryService(DocumentCategoryService documentCategoryService) {
		this.documentCategoryService = documentCategoryService;
	}

	public DocumentTopicService getDocumentTopicService() {
		return documentTopicService;
	}

	public void setDocumentTopicService(DocumentTopicService documentTopicService) {
		this.documentTopicService = documentTopicService;
	}

	public SelectorInfo getSelectorJdlPeraturan() {
		return selectorJdlPeraturan;
	}

	public void setSelectorJdlPeraturan(SelectorInfo selectorJdlPeraturan) {
		this.selectorJdlPeraturan = selectorJdlPeraturan;
	}

	public List<SelectItem> getTrackCodes() {
		return trackCodes;
	}

	public void setTrackCodes(List<SelectItem> trackCodes) {
		this.trackCodes = trackCodes;
	}

	public RegulationTrackRecord getSelectedRow() {
		return selectedRow;
	}

	public void setSelectedRow(RegulationTrackRecord selectedRow) {
		this.selectedRow = selectedRow;
	}

	public SocializationRegulationTmp[] getSelectedData() {
		return selectedData;
	}

	public void setSelectedData(SocializationRegulationTmp[] selectedData) {
		this.selectedData = selectedData;
	}

	public SocializationTmp getSocializationTmp() {
		return socializationTmp;
	}

	public void setSocializationTmp(SocializationTmp socializationTmp) {
		this.socializationTmp = socializationTmp;
	}

	public SocializationRegulationTableModel<SocializationRegulationTmp> getTableModel() {
		return tableModel;
	}

	public void setTableModel(SocializationRegulationTableModel<SocializationRegulationTmp> tableModel) {
		this.tableModel = tableModel;
	}

	public SocializationTmpService getSocializationTmpService() {
		return socializationTmpService;
	}

	public void setSocializationTmpService(SocializationTmpService socializationTmpService) {
		this.socializationTmpService = socializationTmpService;
	}

	public Integer getIndexDtl() {
		return indexDtl;
	}

	public void setIndexDtl(Integer indexDtl) {
		this.indexDtl = indexDtl;
	}

	public RegulationService getRegulationService() {
		return regulationService;
	}

	public void setRegulationService(RegulationService regulationService) {
		this.regulationService = regulationService;
	}

	public SocializationPICComplianceTableModel<SocializationPICComplianceTmp> getTableModelCompliance() {
		return tableModelCompliance;
	}

	public void setTableModelCompliance(
			SocializationPICComplianceTableModel<SocializationPICComplianceTmp> tableModelCompliance) {
		this.tableModelCompliance = tableModelCompliance;
	}

	public SocializationPICComplianceTmp[] getSelectedDataCompliance() {
		return selectedDataCompliance;
	}

	public void setSelectedDataCompliance(SocializationPICComplianceTmp[] selectedDataCompliance) {
		this.selectedDataCompliance = selectedDataCompliance;
	}

	public SelectorInfo getSelectorCompliance() {
		return selectorCompliance;
	}

	public void setSelectorCompliance(SelectorInfo selectorCompliance) {
		this.selectorCompliance = selectorCompliance;
	}

	public Integer getIndexDtlCompliance() {
		return indexDtlCompliance;
	}

	public void setIndexDtlCompliance(Integer indexDtlCompliance) {
		this.indexDtlCompliance = indexDtlCompliance;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public List<SelectItem> getFollowUps() {
		return followUps;
	}

	public void setFollowUps(List<SelectItem> followUps) {
		this.followUps = followUps;
	}

	public SelectorInfo getSelectorFollowup() {
		return selectorFollowup;
	}

	public void setSelectorFollowup(SelectorInfo selectorFollowup) {
		this.selectorFollowup = selectorFollowup;
	}

	public SocializationPICFollowupTmp[] getSelectedDataFollowup() {
		return selectedDataFollowup;
	}

	public void setSelectedDataFollowup(SocializationPICFollowupTmp[] selectedDataFollowup) {
		this.selectedDataFollowup = selectedDataFollowup;
	}

	public SocializationPICFollowupTableModel<SocializationPICFollowupTmp> getTableModelFollowup() {
		return tableModelFollowup;
	}

	public void setTableModelFollowup(
			SocializationPICFollowupTableModel<SocializationPICFollowupTmp> tableModelFollowup) {
		this.tableModelFollowup = tableModelFollowup;
	}

	public Integer getIndexDtlFollowup() {
		return indexDtlFollowup;
	}

	public void setIndexDtlFollowup(Integer indexDtlFollowup) {
		this.indexDtlFollowup = indexDtlFollowup;
	}

	public SelectorInfo getSelectorPic1() {
		return selectorPic1;
	}

	public void setSelectorPic1(SelectorInfo selectorPic1) {
		this.selectorPic1 = selectorPic1;
	}

	public SelectorInfo getSelectorPic2() {
		return selectorPic2;
	}

	public void setSelectorPic2(SelectorInfo selectorPic2) {
		this.selectorPic2 = selectorPic2;
	}

	public SelectorInfo getSelectorPic3() {
		return selectorPic3;
	}

	public void setSelectorPic3(SelectorInfo selectorPic3) {
		this.selectorPic3 = selectorPic3;
	}

	public List<SelectItem> getDivisions() {
		return divisions;
	}

	public void setDivisions(List<SelectItem> divisions) {
		this.divisions = divisions;
	}

	public CounterTypeService getCounterTypeService() {
		return counterTypeService;
	}

	public void setCounterTypeService(CounterTypeService counterTypeService) {
		this.counterTypeService = counterTypeService;
	}

	public List<SelectItem> getCounterTypes() {
		return counterTypes;
	}

	public void setCounterTypes(List<SelectItem> counterTypes) {
		this.counterTypes = counterTypes;
	}

	public List<StatusConfirmationVO> getTableStatus() {
		return tableStatus;
	}

	public void setTableStatus(List<StatusConfirmationVO> tableStatus) {
		this.tableStatus = tableStatus;
	}

	public Long getCounterTypeId() {
		return counterTypeId;
	}

	public void setCounterTypeId(Long counterTypeId) {
		this.counterTypeId = counterTypeId;
	}

	public List<SelectItem> getReminderStatusList() {
		return reminderStatusList;
	}

	public void setReminderStatusList(List<SelectItem> reminderStatusList) {
		this.reminderStatusList = reminderStatusList;
	}

	public List<SocializationApprovalVO> getTableApproval() {
		return tableApproval;
	}

	public void setTableApproval(List<SocializationApprovalVO> tableApproval) {
		this.tableApproval = tableApproval;
	}

	public RegulationSocializationApprovalService getRegulationSocializationApprovalService() {
		return regulationSocializationApprovalService;
	}

	public void setRegulationSocializationApprovalService(
			RegulationSocializationApprovalService regulationSocializationApprovalService) {
		this.regulationSocializationApprovalService = regulationSocializationApprovalService;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public EmailTemplateService getEmailTemplateService() {
		return emailTemplateService;
	}

	public void setEmailTemplateService(EmailTemplateService emailTemplateService) {
		this.emailTemplateService = emailTemplateService;
	}

	public String getApprovalStat() {
		return approvalStat;
	}

	public void setApprovalStat(String approvalStat) {
		this.approvalStat = approvalStat;
	}

	public List<SelectItem> getApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(List<SelectItem> approvalStatus) {
		this.approvalStatus = approvalStatus;
	}

	public List<UploadedFileWO> getUploadFiles() {
		return uploadFiles;
	}

	public void setUploadFiles(List<UploadedFileWO> uploadFiles) {
		this.uploadFiles = uploadFiles;
	}

	
}