package com.wo.module.tmpRmdApproval.bean;

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
import com.wo.module.common.constant.CommonConstants;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.exception.CustomAPIException;
import com.wo.module.common.utility.CallApiManager;
import com.wo.module.common.utility.EmailUtil;
import com.wo.module.counterType.model.CounterTypeDtl;
import com.wo.module.counterType.service.CounterTypeService;
import com.wo.module.documentCategory.model.DocumentCategory;
import com.wo.module.documentCategory.service.DocumentCategoryService;
import com.wo.module.emailTemplate.model.EmailTemplate;
import com.wo.module.emailTemplate.service.EmailTemplateService;
import com.wo.module.externalRegulation.service.RegulationMstService;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.parameter.model.ParameterHeader;
import com.wo.module.reportType.model.ReportType;
import com.wo.module.reportType.service.ReportTypeService;
import com.wo.module.tmpRmd.model.TmpRmd;
import com.wo.module.tmpRmd.model.TmpRmdApproval;
import com.wo.module.tmpRmd.model.TmpRmdCorrespondence;
import com.wo.module.tmpRmd.model.TmpRmdCorrespondenceTableModel;
import com.wo.module.tmpRmd.model.TmpRmdDueDate;
import com.wo.module.tmpRmd.model.TmpRmdDueDateTableModel;
import com.wo.module.tmpRmd.model.TmpRmdRegulation;
import com.wo.module.tmpRmd.model.TmpRmdRegulationTableModel;
import com.wo.module.tmpRmd.model.TmpRmdSupportingUnit;
import com.wo.module.tmpRmd.model.TmpRmdSupportingUnitTableModel;
import com.wo.module.tmpRmd.service.TmpRmdService;
import com.wo.module.trcRmd.model.TrcRmdPicFollowup;
import com.wo.module.trcRmd.service.TrcRmdService;
import com.wo.module.user.model.Division;
import com.wo.module.user.model.User;
import com.wo.module.user.service.UserService;

public class TmpRmdApprovalEditBean extends CommonBean implements Serializable {

	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(TmpRmdApprovalEditBean.class);

	private TmpRmd tmpRmd;

	private Boolean isViewOnly;

	private String actionMode;

	private String editedId;

	private String dueDateType;
	private String approvalNote;

	private TmpRmdDueDate[] selectedRmdDueDateData;
	private TmpRmdRegulation[] selectedRmdRegulationData;
	private TmpRmdSupportingUnit[] selectedRmdSupportingUnitData;
	private TmpRmdCorrespondence[] selectedRmdCorrespondenceData;

	private TmpRmdDueDateTableModel<TmpRmdDueDate> tableRmdDueDateModel;
	private TmpRmdRegulationTableModel<TmpRmdRegulation> tableRmdRegulationModel;
	private TmpRmdSupportingUnitTableModel<TmpRmdSupportingUnit> tableRmdSupportingUnitModel;
	private TmpRmdCorrespondenceTableModel<TmpRmdCorrespondence> tableRmdCorrespondenceModel;

	private Integer indexDtl;
	private Integer indexDtlCc;

	SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");

	private TmpRmdService tmpRmdService;
	private ReportTypeService reportTypeService;
	private CounterTypeService counterTypeService;
	//private ParameterDetailService parameterDetailService;
	private UserService userService;
	private TrcRmdService trcRmdService;
	private RegulationMstService regulationMstService;
	private EmailTemplateService emailTemplateService;
	private DocumentCategoryService documentCategoryService;

	public FacesUtil facesUtil;

	private List<SelectItem> reportTypeList;
	private List<SelectItem> counterTypeList;

	private List<SelectItem> dayList;
	private List<SelectItem> dateList;
	private List<SelectItem> divisionList;

	private List<SelectItem> reminderStatusList;

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
	}

	public void initList() {

		try {
			reportTypeList = new ArrayList<SelectItem>();
			List<ReportType> list1 = reportTypeService.getAllReportType();
			for (ReportType vo : list1) {
				SelectItem si = new SelectItem();
				si.setLabel(vo.getReportType());
				si.setValue(vo.getReportTypeId());
				reportTypeList.add(si);
			}

			counterTypeList = counterTypeService.getAllCounterTypeLabelValue();

			dayList = new ArrayList<SelectItem>();
			dayList.add(new SelectItem("1", facesUtil.getResource("formSunday")));			
			dayList.add(new SelectItem("2", facesUtil.getResource("formMonday")));
			dayList.add(new SelectItem("3", facesUtil.getResource("formTuesday")));
			dayList.add(new SelectItem("4", facesUtil.getResource("formWednesday")));
			dayList.add(new SelectItem("5", facesUtil.getResource("formThursday")));
			dayList.add(new SelectItem("6", facesUtil.getResource("formFriday")));
			dayList.add(new SelectItem("7", facesUtil.getResource("formSaturday")));

			dateList = new ArrayList<SelectItem>();
			for (int i = 1; i <= 31; i++) {
				dateList.add(new SelectItem(String.valueOf(i), String.valueOf(i)));
			}

			divisionList = new ArrayList<SelectItem>();
			List<Division> listDiv = userService.getAllDivision();
			for (Division vo : listDiv) {
				SelectItem si = new SelectItem();
				si.setLabel(vo.getDivisionName());
				si.setValue(vo.getDivisionId());
				divisionList.add(si);
			}

			reminderStatusList = new ArrayList<SelectItem>();
			List<ParameterDetail> listParamDtl = parameterDetailService
					.getParameterDetailByParamCode(ParameterHeader.PARAM_HEAD_CODE_REMINDER_STATUS);

			for (ParameterDetail vo : listParamDtl) {
				SelectItem si = new SelectItem();
				si.setLabel(vo.getName());
				si.setValue(vo.getParameterDtlCode());
				reminderStatusList.add(si);
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

	public void onChangeReportType() {
		if(tmpRmd.getReportType().getReportTypeId()!= null) {
			ReportType rt = reportTypeService.findById(tmpRmd.getReportType().getReportTypeId());
			if (CommonConstants.Y.equals(rt.getDueDay())) {
				dueDateType = "day";
			} else if (CommonConstants.Y.equals(rt.getDueDate()) && 
					CommonConstants.N.equals(rt.getDueMonth()) &&
					CommonConstants.N.equals(rt.getDueYear())  ) {
				dueDateType = "dateonly";
			} else {
				dueDateType = "date";
			}			
		} else {
			dueDateType = "";
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
		tmpRmd = tmpRmdService.findById(idLong);
		
		onChangeReportType();
		
		if (tmpRmd.getUser1() != null) {
			tmpRmd.setUserNameTemp1(tmpRmd.getUser1().getName());
		}

		if (tmpRmd.getUser2() != null) {
			tmpRmd.setUserNameTemp2(tmpRmd.getUser2().getName());
		}

		if (tmpRmd.getUser3() != null) {
			tmpRmd.setUserNameTemp3(tmpRmd.getUser3().getName());
		}

		if (tmpRmd.getSupportingUnitDetails() != null) {
			for (int i = 0; i < tmpRmd.getSupportingUnitDetails().size(); i++) {
				TmpRmdSupportingUnit dtl = (TmpRmdSupportingUnit) tmpRmd.getSupportingUnitDetails().get(i);
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

		tableRmdDueDateModel = new TmpRmdDueDateTableModel<TmpRmdDueDate>(tmpRmd.getDueDateDetails());
		tableRmdSupportingUnitModel = new TmpRmdSupportingUnitTableModel<TmpRmdSupportingUnit>(
				tmpRmd.getSupportingUnitDetails());
		tableRmdRegulationModel = new TmpRmdRegulationTableModel<TmpRmdRegulation>(tmpRmd.getRegulationDetails());
		tableRmdCorrespondenceModel = new TmpRmdCorrespondenceTableModel<TmpRmdCorrespondence>(tmpRmd.getCorrespondenceDetails());
	}

	public Boolean validate() {
		Boolean flag = false;

		if (StringUtils.isEmpty(approvalNote)) {
			facesUtil.addErrMessage(facesUtil.retrieveMessage("formTmpRmdApprovalNote") + "  "
					+ facesUtil.retrieveMessage("validateRequired"));
			flag = true;
		}

		return flag;
	}
	
	@SuppressWarnings("unused")
	public void sendEmail() {
		try {
			
			EmailTemplate emailTemplate = emailTemplateService.getEmailTemplateByEmailTemplateCode("EMAIL_RMD");
			
			String emailSubject = emailTemplate.getEmailSubject().replaceAll("counter_type","NOTIFICATION");
				   emailSubject = emailSubject.replaceAll("report_name_in",tmpRmd.getReportNameIn());
				   emailSubject = emailSubject.replaceAll("report_name_en",tmpRmd.getReportNameEn());
			
			String emailContent = ""; 
			String emailTo  = "";
			String emailCc1 = "";
			String emailCc2 = "";
			String emailCc = "";
			String emailCcSupporting = "";
			
			ParameterDetail pdHostName = parameterDetailService.getParameterDetailByParamDtlCode("HOST_NAME_APPLICATION");
			TrcRmdPicFollowup picFollowupId = trcRmdService.getFirstPicFollowupId(tmpRmd.getRmdId());
			String token = "";
			String dueDate = "";
			if (picFollowupId != null && picFollowupId.getRmdPicFollowupId() != null) {
				token = Constants.encryptString(picFollowupId.getRmdPicFollowupId().toString());
				dueDate = (picFollowupId.getTargetDate() != null ? sdf.format(picFollowupId.getTargetDate()) : "");
			} else {
				token = Constants.encryptString(tmpRmd.getRmdId().toString());
				dueDate = tmpRmd.getDueDateDetails()!=null && tmpRmd.getDueDateDetails().size()>0 ?sdf.format(tmpRmd.getDueDateDetails().get(0).getDueDate()):"";
			}
			String menuId = Constants.encryptString(Constants.MENU_ID_FOLLOWUP_CONFIRMATION_RMD);
			String urlLink = pdHostName.getNameIn().concat("pages/trcRmd/trcRmdEdit.faces?token="+token+"&menuId="+menuId);
			
		    if (tmpRmd.getReminderStatus()!=null && tmpRmd.getReminderStatus().getParameterDtlCode().equals("REMINDER_ACTIVE")) {
				
				emailContent = emailTemplate.getEmailContent().replaceAll("target_date", tmpRmd.getTargetDate()!=null?sdf.format(tmpRmd.getTargetDate()):"");
				emailContent = emailContent.replaceAll("report_name_in", tmpRmd.getReportNameIn());
				emailContent = emailContent.replaceAll("report_name_en", tmpRmd.getReportNameEn());
				emailContent = emailContent.replaceAll("due_date", dueDate);
				/*String supportingUnit = "";
				if(tmpRmd.getSupportingUnitDetails()!=null && tmpRmd.getSupportingUnitDetails().size()>0) {
					supportingUnit = supportingUnit + (tmpRmd.getSupportingUnitDetails().get(0).getEmailCc1()!=null?tmpRmd.getSupportingUnitDetails().get(0).getEmailCc1().getName():"");
					supportingUnit = (StringUtils.isNotEmpty(supportingUnit)?supportingUnit+",":supportingUnit) + (tmpRmd.getSupportingUnitDetails().get(0).getEmailCc2()!=null?tmpRmd.getSupportingUnitDetails().get(0).getEmailCc2().getName():"");
					supportingUnit = (StringUtils.isNotEmpty(supportingUnit)?supportingUnit+",":supportingUnit) + (tmpRmd.getSupportingUnitDetails().get(0).getEmailCc3()!=null?tmpRmd.getSupportingUnitDetails().get(0).getEmailCc3().getName():"");
				}
				emailContent = emailContent.replaceAll("supporting_unit", supportingUnit);*/
				
				emailContent = emailContent.replaceAll("dedicated_to", tmpRmd.getDedicatedTo());
				emailContent = emailContent.replaceAll("regulation_title_in", tmpRmd.getRegulationDetails()!=null && tmpRmd.getRegulationDetails().size()>0 ? tmpRmd.getRegulationDetails().get(0).getRegulationMst().getNameIn():"");
				emailContent = emailContent.replaceAll("regulation_title_en", tmpRmd.getRegulationDetails()!=null && tmpRmd.getRegulationDetails().size()>0 ? tmpRmd.getRegulationDetails().get(0).getRegulationMst().getNameEn():"");
				emailContent = emailContent.replaceAll("document_number", tmpRmd.getRegulationDetails()!=null && tmpRmd.getRegulationDetails().size()>0 ? tmpRmd.getRegulationDetails().get(0).getRegulationMst().getDocumentNo():"");
				emailContent = emailContent.replaceAll("letter_no", tmpRmd.getCorrespondenceDetails()!=null && tmpRmd.getCorrespondenceDetails().size()>0 ? tmpRmd.getCorrespondenceDetails().get(0).getTrcCorrespondence().getLetterNo():"");
				emailContent = emailContent.replaceAll("publisher_unit", tmpRmd.getRegulationDetails()!=null && tmpRmd.getRegulationDetails().size()>0 ? (tmpRmd.getRegulationDetails().get(0).getRegulationMst().getPublisherUnit()!=null?tmpRmd.getRegulationDetails().get(0).getRegulationMst().getPublisherUnit():""):"");
				emailContent = emailContent.replaceAll("sanction", tmpRmd.getSanctions());
				emailContent = emailContent.replaceAll("division_name", (tmpRmd.getDivisionId()!=null?userService.getDivisionNameByDivisionId(tmpRmd.getDivisionId()):"NA"));
				emailContent = emailContent.replaceAll("pic_1_name", (tmpRmd.getUser1() != null?tmpRmd.getUser1().getName():"NA"));
				emailContent = emailContent.replaceAll("pic_2_name", (tmpRmd.getUser2()!=null?tmpRmd.getUser2().getName():"NA"));
				emailContent = emailContent.replaceAll("pic_3_name", (tmpRmd.getUser3()!=null?tmpRmd.getUser3().getName():"NA"));
				emailContent = emailContent.replaceAll("url_link", urlLink);
				try {
					emailContent = emailContent.replaceAll("document_category_in",
							tmpRmd.getRegulationDetails() != null && tmpRmd.getRegulationDetails().size() > 0
							&& tmpRmd.getRegulationDetails().get(0).getRegulationMst() != null
							&& tmpRmd.getRegulationDetails().get(0).getRegulationMst().getDocumentCategory() != null
									? tmpRmd.getRegulationDetails().get(0).getRegulationMst().getDocumentCategory()
											.getDocumentCategoryIn()
									: "");
					emailContent = emailContent.replaceAll("document_category_en",
							tmpRmd.getRegulationDetails() != null && tmpRmd.getRegulationDetails().size() > 0
									&& tmpRmd.getRegulationDetails().get(0).getRegulationMst() != null
									&& tmpRmd.getRegulationDetails().get(0).getRegulationMst().getDocumentCategory() != null
											? tmpRmd.getRegulationDetails().get(0).getRegulationMst().getDocumentCategory()
													.getDocumentCategoryEn()
											: "");
				} catch (Exception ex) {
					if (tmpRmd.getRegulationDetails() != null  && tmpRmd.getRegulationDetails().size() > 0
							&& tmpRmd.getRegulationDetails().get(0).getRegulationMst() != null 
							&& tmpRmd.getRegulationDetails().get(0).getRegulationMst().getDocumentCategory() != null
							) {
						logger.debug("document category name is not fetched");
						DocumentCategory tmp = documentCategoryService.findById(tmpRmd.getRegulationDetails().get(0).getRegulationMst().getDocumentCategory().getDocumentCategoryId());
						emailContent = emailContent.replaceAll("document_category_in", tmp.getDocumentCategoryIn());
						emailContent = emailContent.replaceAll("document_category_en", tmp.getDocumentCategoryEn());
					} else {
						ex.printStackTrace();
					}
				}
	
				try {
					emailContent = emailContent.replaceAll("report_type_in", tmpRmd.getReportType() != null ? tmpRmd.getReportType().getReportTypeIn() : "");
					emailContent = emailContent.replaceAll("report_type_en", tmpRmd.getReportType() != null ? tmpRmd.getReportType().getReportTypeEn() : "");
				} catch (Exception ex) {
					if (tmpRmd.getReportType() != null) {
						logger.debug("report type name is not fetched");
						ReportType tmp = reportTypeService.findById(tmpRmd.getReportType().getReportTypeId());
						emailContent = emailContent.replaceAll("report_type_in", tmp.getReportTypeIn());
						emailContent = emailContent.replaceAll("report_type_en", tmp.getReportTypeEn());
					} else {
						ex.printStackTrace();
					}
				}
				
				if (tmpRmd.getSupportingUnitDetails() != null && tmpRmd.getSupportingUnitDetails().size() > 0) {
					if (tmpRmd.getSupportingUnitDetails().size() == 1) {
						TmpRmdSupportingUnit trsu = tmpRmd.getSupportingUnitDetails().get(0);
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
									
									TmpRmdSupportingUnit trsu = tmpRmd.getSupportingUnitDetails().get(0);
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
									for (int y = 1; y < tmpRmd.getSupportingUnitDetails().size(); y++) {
										temp += partial;
										
										trsu = tmpRmd.getSupportingUnitDetails().get(y);
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
				
				for(int i=0;tmpRmd.getSupportingUnitDetails() != null && i<tmpRmd.getSupportingUnitDetails().size();i++) {
					TmpRmdSupportingUnit su = tmpRmd.getSupportingUnitDetails().get(i);
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
				
//					for(int x=0;x<tmpRmd.getCounterType().getDetails().size();x++) {
				if (tmpRmd != null && tmpRmd.getCounterType() != null && tmpRmd.getCounterType().getDetails() != null && tmpRmd.getCounterType().getDetails().size() > 0) {
					CounterTypeDtl cd = tmpRmd.getCounterType().getDetails().get(0);
					emailTo = cd.getEmailTo();
					emailCc1 = cd.getEmailCc1();
					emailCc2 = cd.getEmailCc2();
					
					if(emailTo.equals(Constants.REMINDER_PIC1)) {
						emailTo = tmpRmd.getUser1()!=null?tmpRmd.getUser1().getEmail():"";
					}
					else if(emailTo.equals(Constants.REMINDER_PIC2)) {
						emailTo = tmpRmd.getUser2()!=null?tmpRmd.getUser2().getEmail():"";
					}
					else if(emailTo.equals(Constants.REMINDER_PIC3)) {
						emailTo = tmpRmd.getUser3()!=null?tmpRmd.getUser3().getEmail():"";
					}
					
					if(emailCc1!=null && emailCc1.equals(Constants.REMINDER_PIC1)) {
						emailCc1 = tmpRmd.getUser1()!=null?tmpRmd.getUser1().getEmail():"";
					}
					else if(emailCc1!=null && emailCc1.equals(Constants.REMINDER_PIC2)) {
						emailCc1 = tmpRmd.getUser2()!=null?tmpRmd.getUser2().getEmail():"";
					}
					else if(emailCc1!=null && emailCc1.equals(Constants.REMINDER_PIC3)) {
						emailCc1 = tmpRmd.getUser3()!=null?tmpRmd.getUser3().getEmail():"";
					}
					
					if(emailCc2!=null && emailCc2.equals(Constants.REMINDER_PIC1)) {
						emailCc2 = tmpRmd.getUser1()!=null?tmpRmd.getUser1().getEmail():"";
					}
					else if(emailCc2!=null && emailCc2.equals(Constants.REMINDER_PIC2)) {
						emailCc2 = tmpRmd.getUser2()!=null?tmpRmd.getUser2().getEmail():"";
					}
					else if(emailCc2!=null && emailCc2.equals(Constants.REMINDER_PIC3)) {
						emailCc2 = tmpRmd.getUser3()!=null?tmpRmd.getUser3().getEmail():"";
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
					
					ExecutorService emailExecutor = Executors.newCachedThreadPool();

					final String subject = emailSubject;
					final String content = emailContent;
					final String to = emailTo;
					final String cc = emailCc;
					
					//System.out.println("to=="+to);
					//System.out.println("cc=="+cc);
					
					CallApiManager.sendEmailAPI(to,cc, subject,
							content, "EMAIL_SOSIALISASI", "true", parameterDetailService);
//			        emailExecutor.execute(new Runnable() {
//			            @Override
//			            public void run() {
//			            	try {
//			            		
//								//CallApiManager.sendEmail(subject, content, to,cc, parameterDetailService);
//								CallApiManager.sendEmailAPI(to,cc, subject,
//										content, "EMAIL_SOSIALISASI", "true", parameterDetailService);
//							} catch (Exception e) {
//								e.printStackTrace();
//							}
//			            }
//			        });
			        
					
					
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

			if (tmpRmd.getApprovalDetails() == null || tmpRmd.getApprovalDetails().size() == 0) {
				tmpRmd.setApprovalDetails(new ArrayList<TmpRmdApproval>());
			}

			TmpRmdApproval dtl = new TmpRmdApproval();
			dtl.setTmpRmd(tmpRmd);
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

			tmpRmd.getApprovalDetails().add(dtl);

			if (tmpRmd.getRmdId() != null) {
				tmpRmd.setStatus(parameterDetailService
						.getParameterDetailByParamDtlCode(ParameterDetail.PARAM_DET_CODE_DATA_ACTIVE));
				tmpRmd.setLastUpdateBy(facesUtil.retrieveUserLogin());
				tmpRmd.setLastUpdateDate(new Timestamp(new Date().getTime()));
				tmpRmd.setDelId(new Long(0));
				tmpRmd.setEnabledFlag(Constants.CONSTANT_YES);
				tmpRmdService.update(tmpRmd);
				
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

			facesUtil.redirect("/pages/tmpRmdApproval/tmpRmdApproval.faces");

		} catch (Exception ex) {
			facesUtil.addFacesMsg(FacesMessage.SEVERITY_ERROR, null, "Operation Failed : " + ex.getMessage(), "");
			ex.printStackTrace();
		}

	}

	public void revise() {
		try {
			if (!validate()) {
				if (tmpRmd.getApprovalDetails() == null || tmpRmd.getApprovalDetails().size() == 0) {
					tmpRmd.setApprovalDetails(new ArrayList<TmpRmdApproval>());
				}

				TmpRmdApproval dtl = new TmpRmdApproval();
				dtl.setTmpRmd(tmpRmd);
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

				tmpRmd.getApprovalDetails().add(dtl);

				if (tmpRmd.getRmdId() != null) {
					tmpRmd.setStatus(parameterDetailService
							.getParameterDetailByParamDtlCode(ParameterDetail.PARAM_DET_CODE_DATA_REVISE));
					tmpRmd.setLastUpdateBy(facesUtil.retrieveUserLogin());
					tmpRmd.setLastUpdateDate(new Timestamp(new Date().getTime()));
					tmpRmd.setDelId(new Long(0));
					tmpRmd.setEnabledFlag(Constants.CONSTANT_YES);
					tmpRmdService.update(tmpRmd);
				}

				facesUtil.redirect("/pages/tmpRmdApproval/tmpRmdApproval.faces");
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
			facesUtil.redirect("/pages/tmpRmdApproval/tmpRmdApproval.faces");
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

	public TmpRmdService getTmpRmdService() {
		return tmpRmdService;
	}

	public void setTmpRmdService(TmpRmdService tmpRmdService) {
		this.tmpRmdService = tmpRmdService;
	}

	public TmpRmd getTmpRmd() {
		return tmpRmd;
	}

	public void setTmpRmd(TmpRmd tmpRmd) {
		this.tmpRmd = tmpRmd;
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

	/*public ParameterDetailService getParameterDetailService() {
		return parameterDetailService;
	}

	public void setParameterDetailService(ParameterDetailService parameterDetailService) {
		this.parameterDetailService = parameterDetailService;
	}*/

	public ReportTypeService getReportTypeService() {
		return reportTypeService;
	}

	public void setReportTypeService(ReportTypeService reportTypeService) {
		this.reportTypeService = reportTypeService;
	}

	public List<SelectItem> getReportTypeList() {
		return reportTypeList;
	}

	public void setReportTypeList(List<SelectItem> reportTypeList) {
		this.reportTypeList = reportTypeList;
	}

	public CounterTypeService getCounterTypeService() {
		return counterTypeService;
	}

	public void setCounterTypeService(CounterTypeService counterTypeService) {
		this.counterTypeService = counterTypeService;
	}

	public List<SelectItem> getCounterTypeList() {
		return counterTypeList;
	}

	public void setCounterTypeList(List<SelectItem> counterTypeList) {
		this.counterTypeList = counterTypeList;
	}

	public TmpRmdDueDateTableModel<TmpRmdDueDate> getTableRmdDueDateModel() {
		return tableRmdDueDateModel;
	}

	public void setTableRmdDueDateModel(TmpRmdDueDateTableModel<TmpRmdDueDate> tableRmdDueDateModel) {
		this.tableRmdDueDateModel = tableRmdDueDateModel;
	}

	public TmpRmdRegulationTableModel<TmpRmdRegulation> getTableRmdRegulationModel() {
		return tableRmdRegulationModel;
	}

	public void setTableRmdRegulationModel(TmpRmdRegulationTableModel<TmpRmdRegulation> tableRmdRegulationModel) {
		this.tableRmdRegulationModel = tableRmdRegulationModel;
	}

	public TmpRmdSupportingUnitTableModel<TmpRmdSupportingUnit> getTableRmdSupportingUnitModel() {
		return tableRmdSupportingUnitModel;
	}

	public void setTableRmdSupportingUnitModel(
			TmpRmdSupportingUnitTableModel<TmpRmdSupportingUnit> tableRmdSupportingUnitModel) {
		this.tableRmdSupportingUnitModel = tableRmdSupportingUnitModel;
	}

	public List<SelectItem> getDayList() {
		return dayList;
	}

	public void setDayList(List<SelectItem> dayList) {
		this.dayList = dayList;
	}

	public List<SelectItem> getDateList() {
		return dateList;
	}

	public void setDateList(List<SelectItem> dateList) {
		this.dateList = dateList;
	}

	public String getDueDateType() {
		return dueDateType;
	}

	public void setDueDateType(String dueDateType) {
		this.dueDateType = dueDateType;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public List<SelectItem> getDivisionList() {
		return divisionList;
	}

	public void setDivisionList(List<SelectItem> divisionList) {
		this.divisionList = divisionList;
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
	
	public TrcRmdService getTrcRmdService() {
		return trcRmdService;
	}

	public void setTrcRmdService(TrcRmdService trcRmdService) {
		this.trcRmdService = trcRmdService;
	}

	public Integer getIndexDtl() {
		return indexDtl;
	}

	public void setIndexDtl(Integer indexDtl) {
		this.indexDtl = indexDtl;
	}

	public RegulationMstService getRegulationMstService() {
		return regulationMstService;
	}

	public void setRegulationMstService(RegulationMstService regulationMstService) {
		this.regulationMstService = regulationMstService;
	}

	public List<SelectItem> getReminderStatusList() {
		return reminderStatusList;
	}

	public void setReminderStatusList(List<SelectItem> reminderStatusList) {
		this.reminderStatusList = reminderStatusList;
	}
	
	public TmpRmdDueDate[] getSelectedRmdDueDateData() {
		return selectedRmdDueDateData;
	}

	public void setSelectedRmdDueDateData(TmpRmdDueDate[] selectedRmdDueDateData) {
		this.selectedRmdDueDateData = selectedRmdDueDateData;
	}

	public TmpRmdRegulation[] getSelectedRmdRegulationData() {
		return selectedRmdRegulationData;
	}

	public void setSelectedRmdRegulationData(TmpRmdRegulation[] selectedRmdRegulationData) {
		this.selectedRmdRegulationData = selectedRmdRegulationData;
	}

	public TmpRmdSupportingUnit[] getSelectedRmdSupportingUnitData() {
		return selectedRmdSupportingUnitData;
	}

	public void setSelectedRmdSupportingUnitData(TmpRmdSupportingUnit[] selectedRmdSupportingUnitData) {
		this.selectedRmdSupportingUnitData = selectedRmdSupportingUnitData;
	}

	public String getApprovalNote() {
		return approvalNote;
	}

	public void setApprovalNote(String approvalNote) {
		this.approvalNote = approvalNote;
	}

	public Integer getIndexDtlCc() {
		return indexDtlCc;
	}

	public void setIndexDtlCc(Integer indexDtlCc) {
		this.indexDtlCc = indexDtlCc;
	}

	public EmailTemplateService getEmailTemplateService() {
		return emailTemplateService;
	}

	public void setEmailTemplateService(EmailTemplateService emailTemplateService) {
		this.emailTemplateService = emailTemplateService;
	}

	public DocumentCategoryService getDocumentCategoryService() {
		return documentCategoryService;
	}

	public void setDocumentCategoryService(DocumentCategoryService documentCategoryService) {
		this.documentCategoryService = documentCategoryService;
	}

	public TmpRmdCorrespondence[] getSelectedRmdCorrespondenceData() {
		return selectedRmdCorrespondenceData;
	}

	public void setSelectedRmdCorrespondenceData(TmpRmdCorrespondence[] selectedRmdCorrespondenceData) {
		this.selectedRmdCorrespondenceData = selectedRmdCorrespondenceData;
	}

	public TmpRmdCorrespondenceTableModel<TmpRmdCorrespondence> getTableRmdCorrespondenceModel() {
		return tableRmdCorrespondenceModel;
	}

	public void setTableRmdCorrespondenceModel(TmpRmdCorrespondenceTableModel<TmpRmdCorrespondence> tableRmdCorrespondenceModel) {
		this.tableRmdCorrespondenceModel = tableRmdCorrespondenceModel;
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