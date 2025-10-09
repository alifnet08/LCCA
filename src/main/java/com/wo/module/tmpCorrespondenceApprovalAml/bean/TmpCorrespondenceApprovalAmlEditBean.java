package com.wo.module.tmpCorrespondenceApprovalAml.bean;

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
import javax.faces.model.SelectItem;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.exception.CustomAPIException;
import com.wo.module.common.model.UploadedFileWO;
import com.wo.module.common.util.FileUtil;
import com.wo.module.common.utility.CallApiManager;
import com.wo.module.common.utility.EmailUtil;
import com.wo.module.counterType.model.CounterTypeDtl;
import com.wo.module.counterType.service.CounterTypeService;
import com.wo.module.emailTemplate.model.EmailTemplate;
import com.wo.module.emailTemplate.service.EmailTemplateService;
import com.wo.module.externalRegulation.service.RegulationMstService;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.lov.bean.SelectorModel.SelectorInfo;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.parameter.model.ParameterHeader;
import com.wo.module.reportType.service.ReportTypeService;
import com.wo.module.tmpCorrespondence.constant.TmpCorrespondenceConstants;
import com.wo.module.tmpCorrespondence.model.TmpCorrespondence;
import com.wo.module.tmpCorrespondence.model.TmpCorrespondenceApproval;
import com.wo.module.tmpCorrespondence.model.TmpCorrespondenceDocument;
import com.wo.module.tmpCorrespondence.model.TmpCorrespondencePicCompliance;
import com.wo.module.tmpCorrespondence.model.TmpCorrespondencePicComplianceTableModel;
import com.wo.module.tmpCorrespondence.model.TmpCorrespondenceSupportingUnit;
import com.wo.module.tmpCorrespondence.model.TmpCorrespondenceSupportingUnitTableModel;
import com.wo.module.tmpCorrespondenceAml.service.TmpCorrespondenceAmlService;
import com.wo.module.trcCorrespondenceAml.service.TrcCorrespondenceAmlService;
import com.wo.module.user.model.Division;
import com.wo.module.user.model.User;
import com.wo.module.user.service.UserService;

public class TmpCorrespondenceApprovalAmlEditBean extends CommonBean implements Serializable{

	private static final long serialVersionUID = -396082501922494578L;
	
	static Logger logger = Logger.getLogger(TmpCorrespondenceApprovalAmlEditBean.class);
	
	private TmpCorrespondence tmpCorrespondence;
	
	private Boolean isViewOnly;

	private String actionMode;

	private String editedId;

	private String dueDateType;
	private String approvalNote;

	private TmpCorrespondencePicCompliance[] selectedPicComplianceData;
	private TmpCorrespondenceSupportingUnit[] selectedSupportingUnitData;

	private SelectorInfo selectorUser1;
	private SelectorInfo selectorUser2;
	private SelectorInfo selectorUser3;
	private SelectorInfo selectorPicCompliance;

	private SelectorInfo selectorUserCc1;
	private SelectorInfo selectorUserCc2;
	private SelectorInfo selectorUserCc3;

	private List<UploadedFileWO> uploadedFilesDocument;

	private TmpCorrespondencePicComplianceTableModel<TmpCorrespondencePicCompliance> tablePicComplianceModel;
	private TmpCorrespondenceSupportingUnitTableModel<TmpCorrespondenceSupportingUnit> tableSupportingUnitModel;

	private Integer indexDtlPicCompliance;
	private Integer indexDtlCc;

	//private List<TrcRmd> trcRmdList;

	SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");

	private TmpCorrespondenceAmlService tmpCorrespondenceAmlService;
	private ReportTypeService reportTypeService;
	private CounterTypeService counterTypeService;
	//private ParameterDetailService parameterDetailService;
	private UserService userService;
	private TrcCorrespondenceAmlService trcCorrespondenceAmlService;
	private RegulationMstService regulationMstService;
	private EmailTemplateService emailTemplateService;

	public FacesUtil facesUtil;
	
	private FileUtil fileUtil;

	private String navigateSearch = TmpCorrespondenceConstants.NAVIGATE_SEARCH;

	private List<SelectItem> senderCodeList;
	private List<SelectItem> yesNoList;
	private List<SelectItem> counterTypeList;
	private List<SelectItem> complianceStatusList;
	private List<SelectItem> divisionList;

	private List<SelectItem> reminderStatusList;
	private List<SelectItem> correspondenceTypeCodeList;

	private String REMINDER_ACTIVE = ParameterDetail.PARAM_DET_CODE_REMINDER_ACTIVE;
	private String REMINDER_INACTIVE = ParameterDetail.PARAM_DET_CODE_REMINDER_INACTIVE;
	
	private String approvalStat;
	
	private List<SelectItem> approvalStatus;
	
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

		checkNewOrEdit();
		fileUtil = FileUtil.getInstance();
	}
	
	public void initList() {
		try {
			senderCodeList = new ArrayList<SelectItem>();
			List<ParameterDetail> listParamDtl = parameterDetailService
					.getParameterDetailByParamCode(ParameterHeader.PARAM_HEAD_CODE_SENDER_AML);

			for (ParameterDetail vo : listParamDtl) {
				SelectItem si = new SelectItem();
				si.setLabel(vo.getName());
				si.setValue(vo.getParameterDtlCode());
				senderCodeList.add(si);
			}
			
			reminderStatusList = new ArrayList<SelectItem>();
			List<ParameterDetail> listParamReminderDtl = parameterDetailService
					.getParameterDetailByParamCode(ParameterHeader.PARAM_HEAD_CODE_REMINDER_STATUS);

			for (ParameterDetail vo : listParamReminderDtl) {
				SelectItem si = new SelectItem();
				si.setLabel(vo.getName());
				si.setValue(vo.getParameterDtlCode());
				reminderStatusList.add(si);
			}
			
			counterTypeList = counterTypeService.getAllCounterTypeLabelValue();
			
			divisionList = new ArrayList<SelectItem>();
			List<Division> listDiv = userService.getAllDivision();
			for (Division vo : listDiv) {
				SelectItem si = new SelectItem();
				si.setLabel(vo.getDivisionName());
				si.setValue(vo.getDivisionId());
				divisionList.add(si);
			}

			yesNoList = new ArrayList<SelectItem>();
			yesNoList.add(new SelectItem(Constants.CONSTANT_YES, "Yes"));
			yesNoList.add(new SelectItem(Constants.CONSTANT_NO, "No"));
			
			correspondenceTypeCodeList = new ArrayList<SelectItem>();
			List<ParameterDetail> listCorrespondenceTypeDtl = parameterDetailService
					.getParameterDetailByParamCodeOrdered(ParameterHeader.PARAM_HEAD_CODE_CORRESPONDENCE_TYPE);

			for (ParameterDetail vo : listCorrespondenceTypeDtl) {
				SelectItem si = new SelectItem();
				si.setLabel(vo.getName());
				si.setValue(vo.getParameterDtlCode());
				correspondenceTypeCodeList.add(si);
			}
			
			selectApprovalStatus();
		} catch (Exception e) {
			e.printStackTrace();
		}
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
	
	public void onAddNewPicCompliance() {
		List<TmpCorrespondencePicCompliance> listD = new ArrayList<TmpCorrespondencePicCompliance>();
		TmpCorrespondencePicCompliance d = new TmpCorrespondencePicCompliance();
		d.setSequence(tmpCorrespondence.getTmpCorrespondencePicCompliances().size() + 1);
		if (tmpCorrespondence.getTmpCorrespondencePicCompliances() == null
				|| tmpCorrespondence.getTmpCorrespondencePicCompliances().size() == 0) {
			listD.add(d);
			tmpCorrespondence.setTmpCorrespondencePicCompliances(listD);
		} else {
			tmpCorrespondence.getTmpCorrespondencePicCompliances().add(d);
		}

		tablePicComplianceModel.setWrappedData(tmpCorrespondence.getTmpCorrespondencePicCompliances());

	}
	
	public void onDeleteRowPicCompliance() {
		for (int i = 0; i < selectedPicComplianceData.length; i++) {
			tmpCorrespondence.getTmpCorrespondencePicCompliances().remove(selectedPicComplianceData[i]);
		}

		tablePicComplianceModel.setWrappedData(tmpCorrespondence.getTmpCorrespondencePicCompliances());
	}

	public void onAddNewSupportingUnit() {
		
		if (tmpCorrespondence.getTmpCorrespondenceSupportingUnits() == null
				|| tmpCorrespondence.getTmpCorrespondenceSupportingUnits().size() == 0) {
			tmpCorrespondence.setTmpCorrespondenceSupportingUnits(new ArrayList<TmpCorrespondenceSupportingUnit>());
		} 
		
		TmpCorrespondenceSupportingUnit d = new TmpCorrespondenceSupportingUnit();
		d.setSequence(tmpCorrespondence.getTmpCorrespondenceSupportingUnits().size() + 1);
		
		tmpCorrespondence.getTmpCorrespondenceSupportingUnits().add(d);

		tableSupportingUnitModel.setWrappedData(tmpCorrespondence.getTmpCorrespondenceSupportingUnits());

	}

	public void onDeleteRowSupportingUnit() {
		for (int i = 0; i < selectedSupportingUnitData.length; i++) {
			tmpCorrespondence.getTmpCorrespondenceSupportingUnits().remove(selectedSupportingUnitData[i]);
		}

		tableSupportingUnitModel.setWrappedData(tmpCorrespondence.getTmpCorrespondenceSupportingUnits());
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
			//this.handleNew();
		} else {
			this.handleEdit(editId);
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
		tmpCorrespondence = tmpCorrespondenceAmlService.findById(idLong);
		
		
		if (tmpCorrespondence.getUserId1() != null) {
			tmpCorrespondence.setUserNameTemp1(tmpCorrespondence.getUserId1().getName());
		}

		if (tmpCorrespondence.getUserId2() != null) {
			tmpCorrespondence.setUserNameTemp2(tmpCorrespondence.getUserId2().getName());
		}

		if (tmpCorrespondence.getUserId3() != null) {
			tmpCorrespondence.setUserNameTemp3(tmpCorrespondence.getUserId3().getName());
		}
		
		if (tmpCorrespondence.getTmpCorrespondencePicCompliances() != null) {
			for (int i = 0; i < tmpCorrespondence.getTmpCorrespondencePicCompliances().size(); i++) {
				TmpCorrespondencePicCompliance dtl = (TmpCorrespondencePicCompliance) tmpCorrespondence.getTmpCorrespondencePicCompliances().get(i);
				if (dtl.getUser() != null) {
					dtl.setNikTemp(dtl.getUser().getNik());
					dtl.setNameTemp(dtl.getUser().getName());
					dtl.setEmailTemp(dtl.getUser().getEmail());
				}
			}
		}
		
		if (tmpCorrespondence.getTmpCorrespondenceSupportingUnits() != null) {
			for (int i = 0; i < tmpCorrespondence.getTmpCorrespondenceSupportingUnits().size(); i++) {
				TmpCorrespondenceSupportingUnit dtl = (TmpCorrespondenceSupportingUnit) tmpCorrespondence.getTmpCorrespondenceSupportingUnits().get(i);
				if (dtl.getEmailCc1() != null) {
					dtl.setEmailCcTemp1(dtl.getEmailCc1().getNik() + "-" + dtl.getEmailCc1().getName());
				}
				
				if (dtl.getEmailCc2() != null) {
					dtl.setEmailCcTemp2(dtl.getEmailCc2().getNik() + "-" + dtl.getEmailCc2().getName());
				}
				
				if (dtl.getEmailCc3() != null) {
					dtl.setEmailCcTemp3(dtl.getEmailCc3().getNik() + "-" + dtl.getEmailCc3().getName());
				}
			}
		}
		
		uploadedFilesDocument = new ArrayList<UploadedFileWO>();
		
		for (int i = 0; i < tmpCorrespondence.getTmpCorrespondenceDocuments().size(); i++) {
			TmpCorrespondenceDocument ra = tmpCorrespondence.getTmpCorrespondenceDocuments().get(i);
			UploadedFileWO uf = new UploadedFileWO();
			uf.setFileName(ra.getAttachmentFile());
			uf.setFileId(ra.getFileId());
			uf.setFileSize(ra.getFileSize());

			uploadedFilesDocument.add(uf);

		}
		
		tableSupportingUnitModel = new TmpCorrespondenceSupportingUnitTableModel<TmpCorrespondenceSupportingUnit>(
				tmpCorrespondence.getTmpCorrespondenceSupportingUnits());
		tablePicComplianceModel = new TmpCorrespondencePicComplianceTableModel<TmpCorrespondencePicCompliance>(tmpCorrespondence.getTmpCorrespondencePicCompliances());
	}
	
	public Boolean validate() {
		Boolean flag = false;

		if (StringUtils.isEmpty(approvalNote)) {
			facesUtil.addErrMessage(facesUtil.retrieveMessage("formTmpCorrespondenceApprovalNote") + " "
					+ facesUtil.retrieveMessage("validateRequired"));
			flag = true;
		}

		return flag;
	}
	
	@SuppressWarnings("unused")
	public void sendEmail() {
		try {
			
			EmailTemplate emailTemplate = emailTemplateService.getEmailTemplateByEmailTemplateCode("EMAIL_KORESPONDENSI");
			String emailSubject = emailTemplate.getEmailSubject().replaceAll("counter_type","NOTIFICATION");
				   emailSubject = emailSubject.replaceAll("perihal_in",tmpCorrespondence.getPerihalIn());
				   //emailSubject = emailSubject.replaceAll("perihal_en",tmpCorrespondence.getPerihalEn());
				   emailSubject = emailSubject.replaceAll("letter_no",tmpCorrespondence.getLetterNo());
			String emailContent = ""; 
			String emailTo  = "";
			String emailCc1 = "";
			String emailCc2 = "";
			String emailCc = "";
			String emailCcSupporting = "";
			
			ParameterDetail pdHostName = parameterDetailService.getParameterDetailByParamDtlCode("HOST_NAME_APPLICATION");
			
		    if (tmpCorrespondence.getReminderStatus()!=null && tmpCorrespondence.getReminderStatus().getParameterDtlCode().equals("REMINDER_ACTIVE") 
		    		//&& tmpCorrespondence.getFollowUp() != null && tmpCorrespondence.getFollowUp().equals("Y")
		    		//tmpCorrespondence.getUserId1() != null
		    		) {
			    	String token = Constants.encryptString(tmpCorrespondence.getCorrespondenceId().toString());
			    	String menuId = "";
			    	String urlLink = "";
			    	if (tmpCorrespondence.getFollowUp() != null && tmpCorrespondence.getFollowUp().equals("Y")) {
						menuId = Constants.encryptString(Constants.MENU_ID_FOLLOWUP_CONFIRMATION_CORRESPONDENCE_AML);
						urlLink = pdHostName.getNameIn().concat("pages/trcCorrespondenceAml/trcCorrespondenceAmlEdit.faces?token="+token+"&menuId="+menuId);
			    	} else if (tmpCorrespondence.getFollowUp() != null && tmpCorrespondence.getFollowUp().equals("N")) {
			    		menuId = Constants.encryptString(Constants.MENU_ID_CORRESPONDENCE_AML_VIEW);
						urlLink = pdHostName.getNameIn().concat("pages/trcCorrespondenceViewAml/trcCorrespondenceViewAmlEdit.faces?token="+token+"&menuId="+menuId);
			    	}
		    		
					emailContent = emailTemplate.getEmailContent().replaceAll("target_date", tmpCorrespondence.getTargetDate()!=null?sdf.format(tmpCorrespondence.getTargetDate()):"");
					emailContent = emailContent.replaceAll("perihal_in", tmpCorrespondence.getPerihalIn());
					//emailContent = emailContent.replaceAll("perihal_en", tmpCorrespondence.getPerihalEn());
					if (tmpCorrespondence.getSenderCode() != null && tmpCorrespondence.getSenderCode().getParameterDtlCode() != null) {
						emailContent = emailContent.replaceAll("sender_in", tmpCorrespondence.getSenderCode().getNameIn());
						emailContent = emailContent.replaceAll("sender_en", tmpCorrespondence.getSenderCode().getNameEn());
					} else {
						emailContent = emailContent.replaceAll("sender_in", "NA");
						emailContent = emailContent.replaceAll("sender_en", "NA");
					}
					emailContent = emailContent.replaceAll("division_name", (tmpCorrespondence.getDivisionId()!=null?userService.getDivisionNameByDivisionId(tmpCorrespondence.getDivisionId()):"NA"));
					emailContent = emailContent.replaceAll("pic_1_name", (tmpCorrespondence.getUserId1() != null?tmpCorrespondence.getUserId1().getName():"NA"));
					emailContent = emailContent.replaceAll("pic_2_name", (tmpCorrespondence.getUserId2()!=null?tmpCorrespondence.getUserId2().getName():"NA"));
					emailContent = emailContent.replaceAll("pic_3_name", (tmpCorrespondence.getUserId3()!=null?tmpCorrespondence.getUserId3().getName():"NA"));
					
					emailContent = emailContent.replaceAll("letter_no", tmpCorrespondence.getLetterNo());
					emailContent = emailContent.replaceAll("receive_letter_date", tmpCorrespondence.getLetterReceivedDate()!=null?sdf.format(tmpCorrespondence.getLetterReceivedDate()):"");
					emailContent = emailContent.replaceAll("letter_date", tmpCorrespondence.getLetterDate()!=null?sdf.format(tmpCorrespondence.getLetterDate()):"");
					emailContent = emailContent.replaceAll("url_link", urlLink);
					
					// add by dwi
					emailContent = emailContent.replaceAll("summary_in", tmpCorrespondence.getLetterSummary());
					
					if (tmpCorrespondence.getTmpCorrespondenceSupportingUnits() != null && tmpCorrespondence.getTmpCorrespondenceSupportingUnits().size() > 0) {
						if (tmpCorrespondence.getTmpCorrespondenceSupportingUnits().size() == 1) {
							TmpCorrespondenceSupportingUnit trsu = tmpCorrespondence.getTmpCorrespondenceSupportingUnits().get(0);
							User user1 = (trsu != null && trsu.getEmailCc1() != null ? trsu.getEmailCc1() : null);
							User user2 = (trsu != null && trsu.getEmailCc2() != null ? trsu.getEmailCc2() : null);
							User user3 = (trsu != null && trsu.getEmailCc3() != null ? trsu.getEmailCc3() : null);
							Long divisionId = (trsu != null && trsu.getDivisionId() != null ? trsu.getDivisionId() : null);
							String divisionName = "";
							
							if (user1 != null || user2 != null || user3 != null) {
								if (user3 != null) {
									user3 = userService.findById(user3.getUserId());
									if (user3.getDivisionId() != null && divisionId != null 
											&& user3.getDivisionId().longValue() == divisionId.longValue()) {
										divisionName = user3.getDivisionName();
									}
								}
								if (user2 != null) {
									user2 = userService.findById(user2.getUserId());
									if (user2.getDivisionId() != null && divisionId != null 
											&& user2.getDivisionId().longValue() == divisionId.longValue()) {
										divisionName = user2.getDivisionName();
									}
								}
								if (user1 != null) {
									user1 = userService.findById(user1.getUserId());
									if (user1.getDivisionId() != null && divisionId != null 
											&& user1.getDivisionId().longValue() == divisionId.longValue()) {
										divisionName = user1.getDivisionName();
									}
								}
							}
							
							emailContent = emailContent.replaceAll("supporting_name_division", StringUtils.isNotBlank(divisionName) ? divisionName : "NA");
							emailContent = emailContent.replaceAll("supporting_pic_name_1", (user1 != null ? user1.getName() : "NA"));
							emailContent = emailContent.replaceAll("supporting_pic_name_2", (user2 != null ? user2.getName() : "NA"));
							emailContent = emailContent.replaceAll("supporting_pic_name_3", (user3 != null ? user3.getName() : "NA"));
						} else {
							if (emailContent.contains("<tbody>") && emailContent.contains("</tbody>") && emailContent.contains("supporting_")) {
								for (int z = -1; (z = emailContent.indexOf("<tbody>", z + 1)) != -1; z++) {
									String partial = emailContent.substring(z+7, emailContent.indexOf("</tbody>", z+7));
									if (partial.contains("supporting_")) {
										
										TmpCorrespondenceSupportingUnit trsu = tmpCorrespondence.getTmpCorrespondenceSupportingUnits().get(0);
										User user1 = (trsu != null && trsu.getEmailCc1() != null ? trsu.getEmailCc1() : null);
										User user2 = (trsu != null && trsu.getEmailCc2() != null ? trsu.getEmailCc2() : null);
										User user3 = (trsu != null && trsu.getEmailCc3() != null ? trsu.getEmailCc3() : null);
										Long divisionId = (trsu != null && trsu.getDivisionId() != null ? trsu.getDivisionId() : null);
										String divisionName = "";
										
										if (user1 != null || user2 != null || user3 != null) {
											if (user3 != null) {
												user3 = userService.findById(user3.getUserId());
												if (user3.getDivisionId() != null && divisionId != null 
														&& user3.getDivisionId().longValue() == divisionId.longValue()) {
													divisionName = user3.getDivisionName();
												}
											}
											if (user2 != null) {
												user2 = userService.findById(user2.getUserId());
												if (user2.getDivisionId() != null && divisionId != null 
														&& user2.getDivisionId().longValue() == divisionId.longValue()) {
													divisionName = user2.getDivisionName();
												}
											}
											if (user1 != null) {
												user1 = userService.findById(user1.getUserId());
												if (user1.getDivisionId() != null && divisionId != null 
														&& user1.getDivisionId().longValue() == divisionId.longValue()) {
													divisionName = user1.getDivisionName();
												}
											}
										}
										emailContent = emailContent.replaceAll("supporting_name_division", StringUtils.isNotBlank(divisionName) ? divisionName : "NA");
										emailContent = emailContent.replaceAll("supporting_pic_name_1", (user1 != null ? user1.getName() : "NA"));
										emailContent = emailContent.replaceAll("supporting_pic_name_2", (user2 != null ? user2.getName() : "NA"));
										emailContent = emailContent.replaceAll("supporting_pic_name_3", (user3 != null ? user3.getName() : "NA"));
										String temp = "";
										for (int y = 1; y < tmpCorrespondence.getTmpCorrespondenceSupportingUnits().size(); y++) {
											temp += partial;
											
											trsu = tmpCorrespondence.getTmpCorrespondenceSupportingUnits().get(y);
										    user1 = (trsu != null && trsu.getEmailCc1() != null ? trsu.getEmailCc1() : null);
											user2 = (trsu != null && trsu.getEmailCc2() != null ? trsu.getEmailCc2() : null);
											user3 = (trsu != null && trsu.getEmailCc3() != null ? trsu.getEmailCc3() : null);
											divisionId = (trsu != null && trsu.getDivisionId() != null ? trsu.getDivisionId() : null);
											divisionName = "";
											
											if (user1 != null || user2 != null || user3 != null) {
												if (user3 != null) {
													user3 = userService.findById(user3.getUserId());
													if (user3.getDivisionId() != null && divisionId != null 
															&& user3.getDivisionId().longValue() == divisionId.longValue()) {
														divisionName = user3.getDivisionName();
													}
												}
												if (user2 != null) {
													user2 = userService.findById(user2.getUserId());
													if (user2.getDivisionId() != null && divisionId != null 
															&& user2.getDivisionId().longValue() == divisionId.longValue()) {
														divisionName = user2.getDivisionName();
													}
												}
												if (user1 != null) {
													user1 = userService.findById(user1.getUserId());
													if (user1.getDivisionId() != null && divisionId != null 
															&& user1.getDivisionId().longValue() == divisionId.longValue()) {
														divisionName = user1.getDivisionName();
													}
												}
											}
											
											temp = temp.replaceAll("supporting_name_division", StringUtils.isNotBlank(divisionName) ? divisionName : "NA");
											temp = temp.replaceAll("supporting_pic_name_1", (user1 != null ? user1.getName() : "NA"));
											temp = temp.replaceAll("supporting_pic_name_2", (user2 != null ? user2.getName() : "NA"));
											temp = temp.replaceAll("supporting_pic_name_3", (user3 != null ? user3.getName() : "NA"));
										}
										emailContent = EmailUtil.insertString(emailContent, temp, emailContent.indexOf("</tbody>", z+7) - 1);
										break;
									}
								}
							}
						}
					} else {
						emailContent = emailContent.replaceAll("supporting_name_division", "NA");
						emailContent = emailContent.replaceAll("supporting_pic_name_1", "NA");
						emailContent = emailContent.replaceAll("supporting_pic_name_2", "NA");
						emailContent = emailContent.replaceAll("supporting_pic_name_3", "NA");
					}
					
					for(int i=0;tmpCorrespondence.getTmpCorrespondenceSupportingUnits() != null && i<tmpCorrespondence.getTmpCorrespondenceSupportingUnits().size();i++) {
						TmpCorrespondenceSupportingUnit su = tmpCorrespondence.getTmpCorrespondenceSupportingUnits().get(i);
						if(su.getEmailCc1()!=null && su.getEmailCc1().getEmail()!=null) {
							emailCcSupporting = emailCcSupporting !="" ?(emailCcSupporting.concat(",").concat(su.getEmailCc1().getEmail())):emailCcSupporting.concat(su.getEmailCc1().getEmail());
						}
						if(su.getEmailCc2()!=null && su.getEmailCc2().getEmail()!=null) {
							emailCcSupporting = emailCcSupporting !="" ?(emailCcSupporting.concat(",").concat(su.getEmailCc2().getEmail())):emailCcSupporting.concat(su.getEmailCc2().getEmail());
						}
						if(su.getEmailCc3()!=null && su.getEmailCc3().getEmail()!=null) {
							emailCcSupporting = emailCcSupporting !="" ?(emailCcSupporting.concat(",").concat(su.getEmailCc3().getEmail())):emailCcSupporting.concat(su.getEmailCc3().getEmail());
						}
					}
					
//					for(int x=0;x<tmpCorrespondence.getCounterType().getDetails().size();x++) {
					if (tmpCorrespondence != null) {
						if (tmpCorrespondence.getCounterType() != null && tmpCorrespondence.getCounterType().getDetails() != null && tmpCorrespondence.getCounterType().getDetails().size() > 0) {
							CounterTypeDtl cd = tmpCorrespondence.getCounterType().getDetails().get(0);
							emailTo = cd.getEmailTo();
							emailCc1 = cd.getEmailCc1();
							emailCc2 = cd.getEmailCc2();
							
							if(emailTo.equals(Constants.REMINDER_PIC1)) {
								emailTo = tmpCorrespondence.getUserId1()!=null?tmpCorrespondence.getUserId1().getEmail():"";
							}
							else if(emailTo.equals(Constants.REMINDER_PIC2)) {
								emailTo = tmpCorrespondence.getUserId2()!=null?tmpCorrespondence.getUserId2().getEmail():"";
							}
							else if(emailTo.equals(Constants.REMINDER_PIC3)) {
								emailTo = tmpCorrespondence.getUserId3()!=null?tmpCorrespondence.getUserId3().getEmail():"";
							}
							
							if(emailCc1!=null && emailCc1.equals(Constants.REMINDER_PIC1)) {
								emailCc1 = tmpCorrespondence.getUserId1()!=null?tmpCorrespondence.getUserId1().getEmail():"";
							}
							else if(emailCc1!=null && emailCc1.equals(Constants.REMINDER_PIC2)) {
								emailCc1 = tmpCorrespondence.getUserId2()!=null?tmpCorrespondence.getUserId2().getEmail():"";
							}
							else if(emailCc1!=null && emailCc1.equals(Constants.REMINDER_PIC3)) {
								emailCc1 = tmpCorrespondence.getUserId3()!=null?tmpCorrespondence.getUserId3().getEmail():"";
							}
							
							if(emailCc2!=null && emailCc2.equals(Constants.REMINDER_PIC1)) {
								emailCc2 = tmpCorrespondence.getUserId1()!=null?tmpCorrespondence.getUserId1().getEmail():"";
							}
							else if(emailCc2!=null && emailCc2.equals(Constants.REMINDER_PIC2)) {
								emailCc2 = tmpCorrespondence.getUserId2()!=null?tmpCorrespondence.getUserId2().getEmail():"";
							}
							else if(emailCc2!=null && emailCc2.equals(Constants.REMINDER_PIC3)) {
								emailCc2 = tmpCorrespondence.getUserId3()!=null?tmpCorrespondence.getUserId3().getEmail():"";
							}
							
			
//					        emailExecutor.execute(new Runnable() {
//					            @Override
//					            public void run() {
//					            	try {
//					            		
//										//CallApiManager.sendEmail(subject, content, to,cc, parameterDetailService);
//										CallApiManager.sendEmailAPI(to,cc, subject,
//												content, "EMAIL_CORESPONDENCE", "true", parameterDetailService);
//									} catch (Exception e) {
//										e.printStackTrace();
//									}
//					            }
//					        });
					        
							
							
						} else {
							emailTo = tmpCorrespondence.getUserId1() != null ? tmpCorrespondence.getUserId1().getEmail() : "";
							emailCc1 = tmpCorrespondence.getUserId2() != null ? tmpCorrespondence.getUserId2().getEmail() : "";
							emailCc2 = tmpCorrespondence.getUserId3() != null ? tmpCorrespondence.getUserId3().getEmail() : "";
						}
						
						if(StringUtils.isNotEmpty(emailCc1)) {
							emailCc = emailCc.concat(emailCc1);
						}
						if(StringUtils.isNotEmpty(emailCc2)) {
							emailCc = StringUtils.isNotEmpty(emailCc)?emailCc.concat(",").concat(emailCc2):emailCc.concat(emailCc2);
						}
						if (StringUtils.isNotEmpty(emailCcSupporting)) {
							if(StringUtils.isNotEmpty(emailCc)) {
								emailCc= emailCc.concat(",").concat(emailCcSupporting);
							}else {
								emailCc = emailCcSupporting;
							}
						}
						
						if (tmpCorrespondence.getUserId1() == null && StringUtils.isNotEmpty(emailCcSupporting)) {
							emailTo = emailCcSupporting;
						}
						
						ExecutorService emailExecutor = Executors.newCachedThreadPool();

						final String subject = emailSubject;
						final String content = emailContent;
						final String to = emailTo;
						final String cc = emailCc;
						
						CallApiManager.sendEmailAPI(to,cc, subject,
								content, "EMAIL_CORESPONDENCE", "true", parameterDetailService);
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

			if (tmpCorrespondence.getTmpCorrespondenceApprovals() == null || tmpCorrespondence.getTmpCorrespondenceApprovals().size() == 0) {
				tmpCorrespondence.setTmpCorrespondenceApprovals(new ArrayList<TmpCorrespondenceApproval>());
			}

			TmpCorrespondenceApproval dtl = new TmpCorrespondenceApproval();
			dtl.setTmpCorrespondence(tmpCorrespondence);
			dtl.setApprovalDate(new Date());
			dtl.setApprovalNote(approvalNote);
			dtl.setApprovalStatus(parameterDetailService
					.getParameterDetailByParamDtlCode(ParameterDetail.PARAM_DET_CODE_STATUS_APPROVED));
			dtl.setUser(userService.getUserByNik(facesUtil.retrieveUserLogin()));
			if (dtl.getCreatedBy() == null) {
				dtl.setCreatedBy(facesUtil.retrieveUserLogin());
				dtl.setCreationDate(new Timestamp(new Date().getTime()));
			}

			dtl.setDelId(new Long(0));
			dtl.setEnabledFlag(Constants.CONSTANT_YES);

			tmpCorrespondence.getTmpCorrespondenceApprovals().add(dtl);

			if (tmpCorrespondence.getCorrespondenceId() != null) {
				tmpCorrespondence.setStatus(parameterDetailService
						.getParameterDetailByParamDtlCode(ParameterDetail.PARAM_DET_CODE_DATA_ACTIVE));
				tmpCorrespondence.setLastUpdateBy(facesUtil.retrieveUserLogin());
				tmpCorrespondence.setLastUpdateDate(new Timestamp(new Date().getTime()));
				tmpCorrespondence.setDelId(new Long(0));
				tmpCorrespondence.setEnabledFlag(Constants.CONSTANT_YES);
				tmpCorrespondenceAmlService.update(tmpCorrespondence);
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
			}
			
			

			facesUtil.redirect("/pages/tmpCorrespondenceApprovalAml/tmpCorrespondenceApprovalAml.faces");

		} catch (Exception ex) {
			facesUtil.addFacesMsg(FacesMessage.SEVERITY_ERROR, null, "Operation Failed : " + ex.getMessage(), "");
			ex.printStackTrace();
		}

	}
	
	public void revise() {
		try {
			if (!validate()) {
				if (tmpCorrespondence.getTmpCorrespondenceApprovals() == null || tmpCorrespondence.getTmpCorrespondenceApprovals().size() == 0) {
					tmpCorrespondence.setTmpCorrespondenceApprovals(new ArrayList<TmpCorrespondenceApproval>());
				}

				TmpCorrespondenceApproval dtl = new TmpCorrespondenceApproval();
				dtl.setTmpCorrespondence(tmpCorrespondence);
				dtl.setApprovalDate(new Date());
				dtl.setApprovalNote(approvalNote);
				dtl.setApprovalStatus(parameterDetailService
						.getParameterDetailByParamDtlCode(ParameterDetail.PARAM_DET_CODE_STATUS_REVISE));
				dtl.setUser(userService.getUserByNik(facesUtil.retrieveUserLogin()));
				if (dtl.getCreatedBy() == null) {
					dtl.setCreatedBy(facesUtil.retrieveUserLogin());
					dtl.setCreationDate(new Timestamp(new Date().getTime()));
				}

				dtl.setDelId(new Long(0));
				dtl.setEnabledFlag(Constants.CONSTANT_YES);

				tmpCorrespondence.getTmpCorrespondenceApprovals().add(dtl);

				if (tmpCorrespondence.getCorrespondenceId() != null) {
					tmpCorrespondence.setStatus(parameterDetailService
							.getParameterDetailByParamDtlCode(ParameterDetail.PARAM_DET_CODE_DATA_REVISE));
					tmpCorrespondence.setLastUpdateBy(facesUtil.retrieveUserLogin());
					tmpCorrespondence.setLastUpdateDate(new Timestamp(new Date().getTime()));
					tmpCorrespondence.setDelId(new Long(0));
					tmpCorrespondence.setEnabledFlag(Constants.CONSTANT_YES);
					tmpCorrespondenceAmlService.update(tmpCorrespondence);
				}

				facesUtil.redirect("/pages/tmpCorrespondenceApprovalAml/tmpCorrespondenceApprovalAml.faces");
			}

		} catch (Exception ex) {
			facesUtil.addFacesMsg(FacesMessage.SEVERITY_ERROR, null, "Operation Failed : " + ex.getMessage(), "");
		}

	}
	
	public void cancel() {
		try {
			facesUtil.redirect("/pages/tmpCorrespondenceApprovalAml/tmpCorrespondenceApprovalAml.faces");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void downloadFile(String fileId, String fileName, byte[] content) throws Exception {
		CallApiManager.downloadFile(fileId, fileName, content, parameterDetailService);
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
	public String getDueDateType() {
		return dueDateType;
	}
	public void setDueDateType(String dueDateType) {
		this.dueDateType = dueDateType;
	}
	public String getApprovalNote() {
		return approvalNote;
	}
	public void setApprovalNote(String approvalNote) {
		this.approvalNote = approvalNote;
	}
	public TmpCorrespondencePicCompliance[] getSelectedPicComplianceData() {
		return selectedPicComplianceData;
	}
	public void setSelectedPicComplianceData(TmpCorrespondencePicCompliance[] selectedPicComplianceData) {
		this.selectedPicComplianceData = selectedPicComplianceData;
	}
	public TmpCorrespondenceSupportingUnit[] getSelectedSupportingUnitData() {
		return selectedSupportingUnitData;
	}
	public void setSelectedSupportingUnitData(TmpCorrespondenceSupportingUnit[] selectedSupportingUnitData) {
		this.selectedSupportingUnitData = selectedSupportingUnitData;
	}
	public SelectorInfo getSelectorUser1() {
		return selectorUser1;
	}
	public void setSelectorUser1(SelectorInfo selectorUser1) {
		this.selectorUser1 = selectorUser1;
	}
	public SelectorInfo getSelectorUser2() {
		return selectorUser2;
	}
	public void setSelectorUser2(SelectorInfo selectorUser2) {
		this.selectorUser2 = selectorUser2;
	}
	public SelectorInfo getSelectorUser3() {
		return selectorUser3;
	}
	public void setSelectorUser3(SelectorInfo selectorUser3) {
		this.selectorUser3 = selectorUser3;
	}
	public SelectorInfo getSelectorPicCompliance() {
		return selectorPicCompliance;
	}
	public void setSelectorPicCompliance(SelectorInfo selectorPicCompliance) {
		this.selectorPicCompliance = selectorPicCompliance;
	}
	public SelectorInfo getSelectorUserCc1() {
		return selectorUserCc1;
	}
	public void setSelectorUserCc1(SelectorInfo selectorUserCc1) {
		this.selectorUserCc1 = selectorUserCc1;
	}
	public SelectorInfo getSelectorUserCc2() {
		return selectorUserCc2;
	}
	public void setSelectorUserCc2(SelectorInfo selectorUserCc2) {
		this.selectorUserCc2 = selectorUserCc2;
	}
	public SelectorInfo getSelectorUserCc3() {
		return selectorUserCc3;
	}
	public void setSelectorUserCc3(SelectorInfo selectorUserCc3) {
		this.selectorUserCc3 = selectorUserCc3;
	}
	public List<UploadedFileWO> getUploadedFilesDocument() {
		return uploadedFilesDocument;
	}
	public void setUploadedFilesDocument(List<UploadedFileWO> uploadedFilesDocument) {
		this.uploadedFilesDocument = uploadedFilesDocument;
	}
	public TmpCorrespondencePicComplianceTableModel<TmpCorrespondencePicCompliance> getTablePicComplianceModel() {
		return tablePicComplianceModel;
	}
	public void setTablePicComplianceModel(
			TmpCorrespondencePicComplianceTableModel<TmpCorrespondencePicCompliance> tablePicComplianceModel) {
		this.tablePicComplianceModel = tablePicComplianceModel;
	}
	public TmpCorrespondenceSupportingUnitTableModel<TmpCorrespondenceSupportingUnit> getTableSupportingUnitModel() {
		return tableSupportingUnitModel;
	}
	public void setTableSupportingUnitModel(
			TmpCorrespondenceSupportingUnitTableModel<TmpCorrespondenceSupportingUnit> tableSupportingUnitModel) {
		this.tableSupportingUnitModel = tableSupportingUnitModel;
	}
	public Integer getIndexDtlPicCompliance() {
		return indexDtlPicCompliance;
	}
	public void setIndexDtlPicCompliance(Integer indexDtlPicCompliance) {
		this.indexDtlPicCompliance = indexDtlPicCompliance;
	}
	public Integer getIndexDtlCc() {
		return indexDtlCc;
	}
	public void setIndexDtlCc(Integer indexDtlCc) {
		this.indexDtlCc = indexDtlCc;
	}
	public SimpleDateFormat getSdf() {
		return sdf;
	}
	public void setSdf(SimpleDateFormat sdf) {
		this.sdf = sdf;
	}
	public TmpCorrespondenceAmlService getTmpCorrespondenceAmlService() {
		return tmpCorrespondenceAmlService;
	}
	public void setTmpCorrespondenceAmlService(TmpCorrespondenceAmlService tmpCorrespondenceAmlService) {
		this.tmpCorrespondenceAmlService = tmpCorrespondenceAmlService;
	}
	public ReportTypeService getReportTypeService() {
		return reportTypeService;
	}
	public void setReportTypeService(ReportTypeService reportTypeService) {
		this.reportTypeService = reportTypeService;
	}
	public CounterTypeService getCounterTypeService() {
		return counterTypeService;
	}
	public void setCounterTypeService(CounterTypeService counterTypeService) {
		this.counterTypeService = counterTypeService;
	}
	public UserService getUserService() {
		return userService;
	}
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	public TrcCorrespondenceAmlService getTrcCorrespondenceAmlService() {
		return trcCorrespondenceAmlService;
	}
	public void setTrcCorrespondenceAmlService(TrcCorrespondenceAmlService trcCorrespondenceAmlService) {
		this.trcCorrespondenceAmlService = trcCorrespondenceAmlService;
	}
	public RegulationMstService getRegulationMstService() {
		return regulationMstService;
	}
	public void setRegulationMstService(RegulationMstService regulationMstService) {
		this.regulationMstService = regulationMstService;
	}
	public EmailTemplateService getEmailTemplateService() {
		return emailTemplateService;
	}
	public void setEmailTemplateService(EmailTemplateService emailTemplateService) {
		this.emailTemplateService = emailTemplateService;
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
	public String getNavigateSearch() {
		return navigateSearch;
	}
	public void setNavigateSearch(String navigateSearch) {
		this.navigateSearch = navigateSearch;
	}
	public List<SelectItem> getSenderCodeList() {
		return senderCodeList;
	}
	public void setSenderCodeList(List<SelectItem> senderCodeList) {
		this.senderCodeList = senderCodeList;
	}
	public List<SelectItem> getYesNoList() {
		return yesNoList;
	}
	public void setYesNoList(List<SelectItem> yesNoList) {
		this.yesNoList = yesNoList;
	}
	public List<SelectItem> getCounterTypeList() {
		return counterTypeList;
	}
	public void setCounterTypeList(List<SelectItem> counterTypeList) {
		this.counterTypeList = counterTypeList;
	}
	public List<SelectItem> getComplianceStatusList() {
		return complianceStatusList;
	}
	public void setComplianceStatusList(List<SelectItem> complianceStatusList) {
		this.complianceStatusList = complianceStatusList;
	}
	public List<SelectItem> getDivisionList() {
		return divisionList;
	}
	public void setDivisionList(List<SelectItem> divisionList) {
		this.divisionList = divisionList;
	}
	public List<SelectItem> getReminderStatusList() {
		return reminderStatusList;
	}
	public void setReminderStatusList(List<SelectItem> reminderStatusList) {
		this.reminderStatusList = reminderStatusList;
	}
	public List<SelectItem> getCorrespondenceTypeCodeList() {
		return correspondenceTypeCodeList;
	}
	public void setCorrespondenceTypeCodeList(List<SelectItem> correspondenceTypeCodeList) {
		this.correspondenceTypeCodeList = correspondenceTypeCodeList;
	}
	public String getREMINDER_ACTIVE() {
		return REMINDER_ACTIVE;
	}
	public void setREMINDER_ACTIVE(String rEMINDER_ACTIVE) {
		REMINDER_ACTIVE = rEMINDER_ACTIVE;
	}
	public String getREMINDER_INACTIVE() {
		return REMINDER_INACTIVE;
	}
	public void setREMINDER_INACTIVE(String rEMINDER_INACTIVE) {
		REMINDER_INACTIVE = rEMINDER_INACTIVE;
	}

	public TmpCorrespondence getTmpCorrespondence() {
		return tmpCorrespondence;
	}

	public void setTmpCorrespondence(TmpCorrespondence tmpCorrespondence) {
		this.tmpCorrespondence = tmpCorrespondence;
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
}
