package com.wo.module.tmpRmd.bean;

import java.io.IOException;
import java.io.Serializable;
//import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.primefaces.PrimeFaces;

import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.CommonConstants;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.exception.CustomAPIException;
import com.wo.module.common.util.EntityUtil;
import com.wo.module.common.util.MathUtil;
import com.wo.module.common.utility.CallApiManager;
import com.wo.module.common.utility.EmailUtil;
import com.wo.module.counterType.model.CounterType;
import com.wo.module.counterType.model.CounterTypeDtl;
import com.wo.module.counterType.service.CounterTypeService;
import com.wo.module.documentCategory.model.DocumentCategory;
import com.wo.module.documentCategory.service.DocumentCategoryService;
import com.wo.module.emailTemplate.model.EmailTemplate;
import com.wo.module.emailTemplate.service.EmailTemplateService;
import com.wo.module.externalRegulation.model.Regulation;
import com.wo.module.externalRegulation.model.RegulationMst;
import com.wo.module.externalRegulation.service.RegulationMstService;
import com.wo.module.holiday.service.HolidayService;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.lov.bean.SelectorListener;
import com.wo.module.lov.bean.SelectorModel.SelectorInfo;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.parameter.model.ParameterHeader;
import com.wo.module.reportType.model.ReportType;
import com.wo.module.reportType.service.ReportTypeService;
import com.wo.module.tmpAudit.model.TmpAuditPicFollowupRecEmail;
import com.wo.module.tmpRmd.constant.TmpRmdConstants;
import com.wo.module.tmpRmd.model.TmpRmd;
import com.wo.module.tmpRmd.model.TmpRmdApproval;
import com.wo.module.tmpRmd.model.TmpRmdCorrespondence;
import com.wo.module.tmpRmd.model.TmpRmdCorrespondenceTableModel;
import com.wo.module.tmpRmd.model.TmpRmdDueDate;
import com.wo.module.tmpRmd.model.TmpRmdDueDateTableModel;
import com.wo.module.tmpRmd.model.TmpRmdPicFollowupEmail;
import com.wo.module.tmpRmd.model.TmpRmdRegulation;
import com.wo.module.tmpRmd.model.TmpRmdRegulationTableModel;
import com.wo.module.tmpRmd.model.TmpRmdSupportingUnit;
import com.wo.module.tmpRmd.model.TmpRmdSupportingUnitTableModel;
import com.wo.module.tmpRmd.service.TmpRmdService;
import com.wo.module.trcCorrespondence.model.TrcCorrespondence;
import com.wo.module.trcCorrespondence.service.TrcCorrespondenceService;
import com.wo.module.trcRmd.model.TrcRmd;
import com.wo.module.trcRmd.model.TrcRmdPicFollowup;
import com.wo.module.trcRmd.service.TrcRmdPicFollowupService;
import com.wo.module.trcRmd.service.TrcRmdService;
import com.wo.module.user.model.Division;
import com.wo.module.user.model.User;
import com.wo.module.user.service.UserService;

public class TmpRmdEditBean extends CommonBean implements SelectorListener<Object>, Serializable {

	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(TmpRmdEditBean.class);

	private TmpRmd tmpRmd;

	private Boolean isViewOnly;

	private String actionMode;

	private String editedId;

	private String dueDateType;
	private String oldDueDateType;

	private TmpRmdDueDate[] selectedRmdDueDateData;
	private TmpRmdRegulation[] selectedRmdRegulationData;
	private TmpRmdCorrespondence[] selectedRmdCorrespondenceData;
	private TmpRmdSupportingUnit[] selectedRmdSupportingUnitData;

	private SelectorInfo selectorUser1;
	private SelectorInfo selectorUser2;
	private SelectorInfo selectorUser3;
	private SelectorInfo selectorRegulation;
	private SelectorInfo selectorCorrespondence;
	
	private SelectorInfo selectorPicCompliance;
	private SelectorInfo selectorPicComplianceSuperior;

	private SelectorInfo selectorUserCc1;
	private SelectorInfo selectorUserCc2;
	private SelectorInfo selectorUserCc3;

	private TmpRmdDueDateTableModel<TmpRmdDueDate> tableRmdDueDateModel;
	private TmpRmdRegulationTableModel<TmpRmdRegulation> tableRmdRegulationModel;
	private TmpRmdCorrespondenceTableModel<TmpRmdCorrespondence> tableRmdCorrespondenceModel;
	private TmpRmdSupportingUnitTableModel<TmpRmdSupportingUnit> tableRmdSupportingUnitModel;

	private Integer lastSequenceOfDueDate;
	private Integer lastSequenceOfRegulation;
	private Integer lastSequenceOfSupportingUnit;
	private Integer lastSequenceOfCorrespondence;

	private Integer indexDtl;
	private Integer indexCorrespondenceDtl;
	private Integer indexDtlCc;

	private List<TrcRmdPicFollowup> trcRmdPicFollowupList;

	SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");

	private TmpRmdService tmpRmdService;
	private TmpRmdService tmpRmdService2;
	private TrcRmdPicFollowupService trcRmdPicFollowupService;
	private ReportTypeService reportTypeService;
	private CounterTypeService counterTypeService;
	/* private ParameterDetailService parameterDetailService; */
	private UserService userService;
	private TrcRmdService trcRmdService;
	private TrcRmdService trcRmdService2;
	private RegulationMstService regulationMstService;
	private TrcCorrespondenceService trcCorrespondenceService;
	private EmailTemplateService emailTemplateService;
	private DocumentCategoryService documentCategoryService;
	private HolidayService holidayService;

	public FacesUtil facesUtil;

	private List<SelectItem> reportTypeList;
	private List<SelectItem> counterTypeList;

	private List<SelectItem> dayList;
	private List<SelectItem> dateList;
	private List<SelectItem> divisionList;

	private List<SelectItem> reminderStatusList;
	
	private List<SelectItem> recurringTypeList;

	private String REMINDER_ACTIVE = ParameterDetail.PARAM_DET_CODE_REMINDER_ACTIVE;
	private String REMINDER_INACTIVE = ParameterDetail.PARAM_DET_CODE_REMINDER_INACTIVE;

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
		selectorUser1 = TmpRmdConstants.buildSelectorUser();
		selectorUser2 = TmpRmdConstants.buildSelectorUser();
		selectorUser3 = TmpRmdConstants.buildSelectorUser();
		selectorRegulation = TmpRmdConstants.buildSelectorRegulation(facesUtil);
		selectorCorrespondence = TmpRmdConstants.buildSelectorCorrespondence(facesUtil);
		selectorUserCc1 = TmpRmdConstants.buildSelectorUser();
		selectorUserCc2 = TmpRmdConstants.buildSelectorUser();
		selectorUserCc3 = TmpRmdConstants.buildSelectorUser();
		selectorPicCompliance = TmpRmdConstants.buildSelectorUser();
		selectorPicComplianceSuperior = TmpRmdConstants.buildSelectorUser();
		
		

		checkNewOrEdit();

		indexDtl = 0;
		indexCorrespondenceDtl = 0;
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
			
			
			recurringTypeList = new ArrayList<SelectItem>();
			List<ParameterDetail> listParamDtl2 = parameterDetailService
							.getParameterDetailByParamCode(ParameterHeader.PARAM_HEAD_CODE_RECURRING_TYPE);
				

			for (ParameterDetail vo : listParamDtl2) {
				SelectItem si = new SelectItem();
				si.setLabel(vo.getNameIn());
				si.setValue(vo.getParameterDtlCode());
				recurringTypeList.add(si);
			}
				
				
		} catch (Exception e) {
			e.printStackTrace();
		}

		PrimeFaces.current().executeScript("reInitSelect2();");
	}

	public void onChangeDivisionSingle() {
		tmpRmd.setUser1(null);
		tmpRmd.setUserNameTemp1(null);
		tmpRmd.setUser2(null);
		tmpRmd.setUserNameTemp2(null);
		tmpRmd.setUser3(null);
		tmpRmd.setUserNameTemp3(null);
	}

	public void clearPic2() {
		tmpRmd.setUser2(null);
		tmpRmd.setUserNameTemp2(null);
	}

	public void clearPic3() {
		tmpRmd.setUser3(null);
		tmpRmd.setUserNameTemp3(null);
	}
	
	public void clearPicComplianceSuperior() {
		tmpRmd.setPicComplianceSuperior(null);
		tmpRmd.setPicComplianceSuperiorTemp(null);
	}


	public void clearPicDetail2(int i) {
		tmpRmd.getSupportingUnitDetails().get(i).setEmailCc2(null);
		tmpRmd.getSupportingUnitDetails().get(i).setEmailCcTemp2(null);
		PrimeFaces.current().executeScript("reInitSelect2();");
	}

	public void clearPicDetail3(int i) {
		tmpRmd.getSupportingUnitDetails().get(i).setEmailCc3(null);
		tmpRmd.getSupportingUnitDetails().get(i).setEmailCcTemp3(null);
		PrimeFaces.current().executeScript("reInitSelect2();");
	}

	public void onChangeDivision(int i) {

		TmpRmdSupportingUnit data = tmpRmd.getSupportingUnitDetails().get(i);
		data.setEmailCc1(null);
		data.setEmailCcTemp1(null);
		data.setEmailCc2(null);
		data.setEmailCcTemp2(null);
		data.setEmailCc3(null);
		data.setEmailCcTemp3(null);
		PrimeFaces.current().executeScript("reInitSelect2();");
	}

	public void onChangeReportType() {

		if (tmpRmd.getReportType().getReportTypeId() != null) {
			ReportType rt = reportTypeService.findById(tmpRmd.getReportType().getReportTypeId());
			if (CommonConstants.Y.equals(rt.getDueDay())) {
				dueDateType = "day";
			} else if (CommonConstants.Y.equals(rt.getDueDate()) && CommonConstants.N.equals(rt.getDueMonth())
					&& CommonConstants.N.equals(rt.getDueYear())) {
				dueDateType = "dateonly";
			} else {
				dueDateType = "date";
			}
		} else {
			dueDateType = "";
		}

	}

	public void onChangeReportTypeNow() {
		tmpRmd.getDueDateDetails().clear();
		if (tmpRmd.getReportType().getReportTypeId() != null) {
			ReportType rt = reportTypeService.findById(tmpRmd.getReportType().getReportTypeId());
			/*if (CommonConstants.Y.equals(rt.getDueDay())) {
				dueDateType = "day";
			} else if (CommonConstants.Y.equals(rt.getDueDate()) && CommonConstants.N.equals(rt.getDueMonth())
					&& CommonConstants.N.equals(rt.getDueYear())) {
				dueDateType = "dateonly";
			} else {
				dueDateType = "date";
			}

			if (rt.getNumberOfDueDate() != null) {
				for (int i = 1; i <= rt.getNumberOfDueDate(); i++) {
					onAddNewTmpRmdDueDate();
				}
			}*/
			tmpRmd.setRecurringType(rt.getRecurringType());

		} else {
			dueDateType = "";
		}
	}

	public void onAddNewTmpRmdDueDate() {
		if (tmpRmd.getDueDateDetails() == null) {
			tmpRmd.setDueDateDetails(new ArrayList<TmpRmdDueDate>());
			lastSequenceOfDueDate = 0;
		} else {
			if (tmpRmd.getDueDateDetails().size() == 0) {
				lastSequenceOfDueDate = 0;
			}
		}

		TmpRmdDueDate d = new TmpRmdDueDate();
		lastSequenceOfDueDate = lastSequenceOfDueDate + 1;
		d.setSequence(lastSequenceOfDueDate);

		tmpRmd.getDueDateDetails().add(d);

		tableRmdDueDateModel.setWrappedData(tmpRmd.getDueDateDetails());

		PrimeFaces.current().executeScript("reInitSelect2();");
	}

	public void onDeleteRowTmpRmdDueDate() {
		for (int i = 0; i < selectedRmdDueDateData.length; i++) {
			tmpRmd.getDueDateDetails().remove(selectedRmdDueDateData[i]);
		}

		if (tmpRmd.getDueDateDetails() == null || tmpRmd.getDueDateDetails().size() == 0) {
			lastSequenceOfDueDate = 0;
		}

		tableRmdDueDateModel.setWrappedData(tmpRmd.getDueDateDetails());

		PrimeFaces.current().executeScript("reInitSelect2();");
	}

	public void onAddNewTmpRmdRegulation() {
		if (tmpRmd.getRegulationDetails() == null) {
			tmpRmd.setRegulationDetails(new ArrayList<TmpRmdRegulation>());
			lastSequenceOfRegulation = 0;
		} else {
			if (tmpRmd.getRegulationDetails().size() == 0) {
				lastSequenceOfRegulation = 0;
			}
		}

		TmpRmdRegulation d = new TmpRmdRegulation();
		RegulationMst regulationMst = new RegulationMst();
		lastSequenceOfRegulation = lastSequenceOfRegulation + 1;
		d.setSequence(lastSequenceOfRegulation);
		d.setRegulationMst(regulationMst);
		tmpRmd.getRegulationDetails().add(d);
		tableRmdRegulationModel.setWrappedData(tmpRmd.getRegulationDetails());

		PrimeFaces.current().executeScript("reInitSelect2();");
	}

	public void onDeleteRowTmpRmdRegulation() {
		for (int i = 0; i < selectedRmdRegulationData.length; i++) {
			tmpRmd.getRegulationDetails().remove(selectedRmdRegulationData[i]);
		}

		if (tmpRmd.getRegulationDetails() == null || tmpRmd.getRegulationDetails().size() == 0) {
			lastSequenceOfRegulation = 0;
		}

		tableRmdRegulationModel.setWrappedData(tmpRmd.getRegulationDetails());

		PrimeFaces.current().executeScript("reInitSelect2();");
	}

	public void onAddNewTmpRmdCorrespondence() {
		if (tmpRmd.getCorrespondenceDetails() == null) {
			tmpRmd.setCorrespondenceDetails(new ArrayList<TmpRmdCorrespondence>());
			lastSequenceOfCorrespondence = 0;
		} else {
			if (tmpRmd.getCorrespondenceDetails().size() == 0) {
				lastSequenceOfCorrespondence = 0;
			}
		}

		TmpRmdCorrespondence d = new TmpRmdCorrespondence();
		TrcCorrespondence trcCorrespondence = new TrcCorrespondence();
		lastSequenceOfCorrespondence = lastSequenceOfCorrespondence + 1;
		d.setSequence(lastSequenceOfCorrespondence);
		d.setTrcCorrespondence(trcCorrespondence);
		tmpRmd.getCorrespondenceDetails().add(d);
		tableRmdCorrespondenceModel.setWrappedData(tmpRmd.getCorrespondenceDetails());

		PrimeFaces.current().executeScript("reInitSelect2();");
	}

	public void onDeleteRowTmpRmdCorrespondence() {
		for (int i = 0; i < selectedRmdCorrespondenceData.length; i++) {
			tmpRmd.getCorrespondenceDetails().remove(selectedRmdCorrespondenceData[i]);
		}

		if (tmpRmd.getCorrespondenceDetails() == null || tmpRmd.getCorrespondenceDetails().size() == 0) {
			lastSequenceOfCorrespondence = 0;
		}

		tableRmdCorrespondenceModel.setWrappedData(tmpRmd.getCorrespondenceDetails());

		PrimeFaces.current().executeScript("reInitSelect2();");
	}

	public void onAddNewTmpRmdSupportingUnit() {
		if (tmpRmd.getSupportingUnitDetails() == null) {
			tmpRmd.setSupportingUnitDetails(new ArrayList<TmpRmdSupportingUnit>());
			lastSequenceOfSupportingUnit = 0;
		} else {
			if (tmpRmd.getSupportingUnitDetails().size() == 0) {
				lastSequenceOfSupportingUnit = 0;
			}
		}

		TmpRmdSupportingUnit d = new TmpRmdSupportingUnit();
		lastSequenceOfSupportingUnit = lastSequenceOfSupportingUnit + 1;
		d.setSequence(lastSequenceOfSupportingUnit);
		tmpRmd.getSupportingUnitDetails().add(d);
		tableRmdSupportingUnitModel.setWrappedData(tmpRmd.getSupportingUnitDetails());

		PrimeFaces.current().executeScript("reInitSelect2();");
	}

	public void onDeleteRowTmpRmdSupportingUnit() {
		for (int i = 0; i < selectedRmdSupportingUnitData.length; i++) {
			tmpRmd.getSupportingUnitDetails().remove(selectedRmdSupportingUnitData[i]);
		}

		if (tmpRmd.getSupportingUnitDetails() == null || tmpRmd.getSupportingUnitDetails().size() == 0) {
			lastSequenceOfSupportingUnit = 0;
		}

		tableRmdSupportingUnitModel.setWrappedData(tmpRmd.getSupportingUnitDetails());

		PrimeFaces.current().executeScript("reInitSelect2();");
	}

	private void checkNewOrEdit() {
		String editId = facesUtil.retrieveRequestParam("id");
		String token = facesUtil.retrieveRequestParam("token");

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
		tmpRmd = new TmpRmd();

		lastSequenceOfDueDate = 0;
		lastSequenceOfRegulation = 0;
		lastSequenceOfSupportingUnit = 0;
		lastSequenceOfCorrespondence = 0;

		tmpRmd.setCounterType(new CounterType());
		tmpRmd.setReportType(new ReportType());
		tmpRmd.setReminderStatus(new ParameterDetail());
		tmpRmd.getReminderStatus().setParameterDtlCode(REMINDER_ACTIVE);
		tmpRmd.setRecurringStartDate(new Date());
		onChangeReportTypeNow();

		/*
		 * User user1 = userService.getUserByNik(facesUtil.retrieveUserLogin()); if
		 * (user1 != null) { tmpRmd.setUser1(user1);
		 * tmpRmd.setUserNameTemp1(user1.getNik() + "-" + user1.getName());
		 * tmpRmd.setDivisionId(user1.getDivisionId()); User user2 =
		 * userService.getUserByNik(user1.getPukNik()); if (user2 != null) {
		 * tmpRmd.setUser2(user2); tmpRmd.setUserNameTemp2(user2.getNik() + "-" +
		 * user2.getName());
		 * 
		 * User user3 = userService.getUserByNik(user2.getPukNik()); if (user3 != null)
		 * { tmpRmd.setUser3(user3); tmpRmd.setUserNameTemp3(user3.getNik() + "-" +
		 * user3.getName()); } } }
		 */

		if (tmpRmd.getDueDateDetails() == null || tmpRmd.getDueDateDetails().size() == 0) {
			tmpRmd.setDueDateDetails(new ArrayList<TmpRmdDueDate>());
			lastSequenceOfDueDate = 0;
		}
		
		User picCompliance = userService.getUserByNik(facesUtil.retrieveUserLogin());
		if (picCompliance != null) {
			tmpRmd.setPicCompliance(picCompliance);
			tmpRmd.setPicComplianceTemp(picCompliance.getNik() + "-" + picCompliance.getName());

			User picComplianceAtasan = userService.getUserByNik(picCompliance.getPukNik());
			if (picComplianceAtasan != null) {
				tmpRmd.setPicComplianceSuperior(picComplianceAtasan);
				tmpRmd.setPicComplianceSuperiorTemp(picComplianceAtasan.getNik() + "-" + picComplianceAtasan.getName());
			}
		}

		tableRmdDueDateModel = new TmpRmdDueDateTableModel<TmpRmdDueDate>(tmpRmd.getDueDateDetails());
		tableRmdSupportingUnitModel = new TmpRmdSupportingUnitTableModel<TmpRmdSupportingUnit>(
				tmpRmd.getSupportingUnitDetails());
		tableRmdRegulationModel = new TmpRmdRegulationTableModel<TmpRmdRegulation>(tmpRmd.getRegulationDetails());
		tableRmdCorrespondenceModel = new TmpRmdCorrespondenceTableModel<TmpRmdCorrespondence>(
				tmpRmd.getCorrespondenceDetails());

		actionMode = Constants.ACTION_ADD;
		facesUtil.setSessionAttribute("token", null);
	}

	private void handleEdit(String editId) {
		String token = facesUtil.retrieveRequestParam("token");
		if (StringUtils.isNotEmpty(token)) {
			editId = Constants.decryptString(token);
		}
		facesUtil.setSessionAttribute("token", Constants.encryptString(editId));
		actionMode = Constants.ACTION_EDIT;
		Long idLong = Long.parseLong(editId);
		tmpRmd = tmpRmdService.findById(idLong);

		lastSequenceOfDueDate = 0;
		lastSequenceOfRegulation = 0;
		lastSequenceOfSupportingUnit = 0;
		lastSequenceOfCorrespondence = 0;

		onChangeReportType();

		TrcRmd trcRmd = trcRmdService.findById(idLong);
		if (trcRmd != null) {
			trcRmdPicFollowupList = new ArrayList<TrcRmdPicFollowup>();
			try {
				List<TrcRmdPicFollowup> list = trcRmdPicFollowupService.getTrcRmdPicFollowupByRmdId(idLong);
				trcRmdPicFollowupList.addAll(list.stream().filter(e->e.getFollowupDate()!=null).collect(java.util.stream.Collectors.toList()));
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		if (tmpRmd.getUser1() != null) {
			tmpRmd.setUserNameTemp1(tmpRmd.getUser1().getName());
		}

		if (tmpRmd.getUser2() != null) {
			tmpRmd.setUserNameTemp2(tmpRmd.getUser2().getName());
		}

		if (tmpRmd.getUser3() != null) {
			tmpRmd.setUserNameTemp3(tmpRmd.getUser3().getName());
		}
		
		if (tmpRmd.getPicCompliance() != null) {
			tmpRmd.setPicComplianceTemp(tmpRmd.getPicCompliance().getName());
		}
		
		if (tmpRmd.getPicComplianceSuperior() != null) {
			tmpRmd.setPicComplianceSuperiorTemp(tmpRmd.getPicComplianceSuperior().getName());
		}
		
		User userInputer = userService.getUserByNik(tmpRmd.getCreatedBy());
		if (userInputer != null) {
			tmpRmd.setUserNameInputer(userInputer.getName());
			User atasanUserInputer = userService.getUserByNik(userInputer.getPukNik());
			if (atasanUserInputer != null) {
				tmpRmd.setUserNameAtasanInputer(atasanUserInputer.getName());
			}
		}


		if (tmpRmd.getDueDateDetails() != null) {
			lastSequenceOfDueDate = tmpRmd.getDueDateDetails().size();
			for (int i = 0; i < tmpRmd.getDueDateDetails().size(); i++) {
				TmpRmdDueDate dtl = (TmpRmdDueDate) tmpRmd.getDueDateDetails().get(i);

				lastSequenceOfDueDate = lastSequenceOfDueDate + 1;
				dtl.setSequence(lastSequenceOfDueDate);
			}
		}

		if (tmpRmd.getSupportingUnitDetails() != null) {
			lastSequenceOfSupportingUnit = tmpRmd.getSupportingUnitDetails().size();
			for (int i = 0; i < tmpRmd.getSupportingUnitDetails().size(); i++) {
				TmpRmdSupportingUnit dtl = (TmpRmdSupportingUnit) tmpRmd.getSupportingUnitDetails().get(i);

				lastSequenceOfSupportingUnit = lastSequenceOfSupportingUnit + 1;
				dtl.setSequence(lastSequenceOfSupportingUnit);
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

		if (tmpRmd.getRegulationDetails() != null) {
			lastSequenceOfRegulation = tmpRmd.getRegulationDetails().size();
			for (int i = 0; i < tmpRmd.getRegulationDetails().size(); i++) {
				TmpRmdRegulation dtl = (TmpRmdRegulation) tmpRmd.getRegulationDetails().get(i);

				lastSequenceOfRegulation = lastSequenceOfRegulation + 1;
				dtl.setSequence(lastSequenceOfRegulation);
			}
		}

		if (tmpRmd.getCorrespondenceDetails() != null) {
			lastSequenceOfCorrespondence = tmpRmd.getCorrespondenceDetails().size();
			for (int i = 0; i < tmpRmd.getCorrespondenceDetails().size(); i++) {
				TmpRmdCorrespondence dtl = (TmpRmdCorrespondence) tmpRmd.getCorrespondenceDetails().get(i);

				lastSequenceOfCorrespondence = lastSequenceOfCorrespondence + 1;
				dtl.setSequence(lastSequenceOfCorrespondence);
			}
		}

		tableRmdDueDateModel = new TmpRmdDueDateTableModel<TmpRmdDueDate>(tmpRmd.getDueDateDetails());
		tableRmdSupportingUnitModel = new TmpRmdSupportingUnitTableModel<TmpRmdSupportingUnit>(
				tmpRmd.getSupportingUnitDetails());
		tableRmdRegulationModel = new TmpRmdRegulationTableModel<TmpRmdRegulation>(tmpRmd.getRegulationDetails());
		tableRmdCorrespondenceModel = new TmpRmdCorrespondenceTableModel<TmpRmdCorrespondence>(
				tmpRmd.getCorrespondenceDetails());
		
		
	}

	public Boolean validate() {
		Boolean flag = false;

		try {
			if (tmpRmd.getReportType().getReportTypeId() == null
					|| StringUtils.isEmpty(tmpRmd.getReportType().getReportTypeId().toString())) {
				facesUtil.addErrMessage(facesUtil.retrieveMessage("formTmpRmdReportType") + " "
						+ facesUtil.retrieveMessage("validateRequired"));
				flag = true;
			}

			if (StringUtils.isEmpty(tmpRmd.getReportNameIn())) {
				facesUtil.addErrMessage(facesUtil.retrieveMessage("formTmpRmdReportName") + " "
//						+ facesUtil.retrieveMessage("indonesia") + " " 
						+ facesUtil.retrieveMessage("validateRequired"));
				flag = true;
			}

			/*
			 * if (StringUtils.isEmpty(tmpRmd.getReportNameEn())) {
			 * facesUtil.addErrMessage(facesUtil.retrieveMessage("formTmpRmdReportName") +
			 * " EN " + facesUtil.retrieveMessage("validateRequired")); flag = true; }
			 */

			if (tmpRmd.getCounterType().getCounterTypeId() == null
					|| StringUtils.isEmpty(tmpRmd.getCounterType().getCounterTypeId().toString())) {
				facesUtil.addErrMessage(facesUtil.retrieveMessage("formTmpRmdReminderType") + " "
						+ facesUtil.retrieveMessage("validateRequired"));
				flag = true;
			}

			if (StringUtils.isEmpty(tmpRmd.getReminderStatus().getParameterDtlCode())) {
				facesUtil.addErrMessage(facesUtil.retrieveMessage("formTmpRmdStatus") + " "
						+ facesUtil.retrieveMessage("validateRequired"));
				flag = true;
			}

			/*
			 * if (tmpRmd.getTargetDate() == null) {
			 * facesUtil.addErrMessage(facesUtil.retrieveMessage("formTmpRmdTargetDate") +
			 * "  " + facesUtil.retrieveMessage("validateRequired")); flag = true; }
			 */

			if (tmpRmd.getUser1() == null) {
				facesUtil.addErrMessage(facesUtil.retrieveMessage("formTmpRmdPic1") + " "
						+ facesUtil.retrieveMessage("validateRequired"));
				flag = true;
			}

			if (tmpRmd.getDueDateDetails() == null || tmpRmd.getDueDateDetails().size() == 0) {
				facesUtil.addErrMessage(facesUtil.retrieveMessage("formTmpRmdDueDate") + " "
						+ facesUtil.retrieveMessage("validateRequired") + " min. 1 data");
				flag = true;
			} else {
				Set<String> setTemp = new HashSet<String>();
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				for (int i = 0; i < tmpRmd.getDueDateDetails().size(); i++) {
					TmpRmdDueDate dtl = (TmpRmdDueDate) tmpRmd.getDueDateDetails().get(i);
					if ("date".equals(dueDateType)) {
						if (dtl.getDueDate() == null) {
							facesUtil.addErrMessage(facesUtil.retrieveMessage("formTmpRmdDueDate") + " "
									+ facesUtil.retrieveMessage("validateRequired"));
							flag = true;
							// break;
						}

						if (dtl.getDueDate() != null) {
							String dateString = dateFormat.format(dtl.getDueDate());
							if (!setTemp.add(dateString)) {
								facesUtil.addErrMessage(facesUtil.retrieveMessage("formTmpRmdDueDate") + " "
										+ facesUtil.retrieveMessage("errorDuplicate"));
								flag = true;
								// break;
							}
						}

					} else if ("dateonly".equals(dueDateType)) {
						if (!setTemp.add(dtl.getDueDateDateOnly().toString())) {
							facesUtil.addErrMessage(facesUtil.retrieveMessage("formTmpRmdDueDate") + " "
									+ facesUtil.retrieveMessage("errorDuplicate"));
							flag = true;
							// break;
						}
					} else if ("day".equals(dueDateType)) {
						if (!setTemp.add(dtl.getDueDay().toString())) {
							facesUtil.addErrMessage(facesUtil.retrieveMessage("formTmpRmdDueDate") + " "
									+ facesUtil.retrieveMessage("errorDuplicate"));
							flag = true;
							// break;
						}
					}
				}
			}

			if (tmpRmd.getSupportingUnitDetails() != null || tmpRmd.getSupportingUnitDetails().size() > 0) {
				for (int i = 0; i < tmpRmd.getSupportingUnitDetails().size(); i++) {
					TmpRmdSupportingUnit dtl = (TmpRmdSupportingUnit) tmpRmd.getSupportingUnitDetails().get(i);

					if (dtl.getEmailCc1() == null) {
						facesUtil.addErrMessage(facesUtil.retrieveMessage("formTmpRmdSupportingUnit") + " "
								+ facesUtil.retrieveMessage("formTmpRmdEmailCc1") + " "
								+ facesUtil.retrieveMessage("validateRequired"));
						flag = true;
					}

				}
			}

			if ((tmpRmd.getRegulationDetails() == null || tmpRmd.getRegulationDetails().size() <= 0)
					&& (tmpRmd.getCorrespondenceDetails() == null || tmpRmd.getCorrespondenceDetails().size() <= 0)) {
				/*facesUtil.addErrMessage(facesUtil.retrieveMessage("formTmpRmdRegulation") + " "
						+ facesUtil.retrieveMessage("or") + " " + facesUtil.retrieveMessage("formTmpRmdCorrespondence")
						+ " " + facesUtil.retrieveMessage("validateRequired") + " min. 1 data");
				flag = true;*/
			} else {
				Set<Long> setRegulationTemp = new HashSet<Long>();

				for (int i = 0; i < tmpRmd.getRegulationDetails().size(); i++) {
					TmpRmdRegulation dtl = (TmpRmdRegulation) tmpRmd.getRegulationDetails().get(i);
					if (dtl.getRegulationMst().getRegulationId() == null) {
						facesUtil.addErrMessage(facesUtil.retrieveMessage("formTmpRmdRegulationDocumentNo") + " "
								+ facesUtil.retrieveMessage("validateRequired"));
						flag = true;
					}

					if (dtl.getRegulationMst().getRegulationId() != null) {
						if (!setRegulationTemp.add(dtl.getRegulationMst().getRegulationId())) {
							facesUtil.addErrMessage(facesUtil.retrieveMessage("formTmpRmdRegulationDocumentNo") + " "
									+ facesUtil.retrieveMessage("errorDuplicate"));
							flag = true;
						}
					}
				}

				Set<Long> setCorrespondenceTemp = new HashSet<Long>();
				for (int i = 0; i < tmpRmd.getCorrespondenceDetails().size(); i++) {
					TmpRmdCorrespondence dtl = (TmpRmdCorrespondence) tmpRmd.getCorrespondenceDetails().get(i);

					if (dtl.getTrcCorrespondence().getCorrespondenceId() == null) {
						facesUtil.addErrMessage(facesUtil.retrieveMessage("formTmpRmdCorrespondenceLetterNo") + " "
								+ facesUtil.retrieveMessage("validateRequired"));
						flag = true;
					}

					if (dtl.getTrcCorrespondence().getCorrespondenceId() != null) {
						if (!setCorrespondenceTemp.add(dtl.getTrcCorrespondence().getCorrespondenceId())) {
							facesUtil.addErrMessage(facesUtil.retrieveMessage("formTmpRmdCorrespondenceLetterNo") + " "
									+ facesUtil.retrieveMessage("errorDuplicate"));
							flag = true;
						}
					}
				}
			}

//			if (!flag) {
//				if (tmpRmdService.isDataDuplicate(tmpRmd)) {
//					facesUtil.addErrMessage(facesUtil.retrieveMessage("errorAlreadyExists"));
//					flag = true;
//				}
//			}
			
			if (!flag && actionMode.equals(Constants.ACTION_ADD)) {
				Integer validateSameValue = tmpRmdService.getTmpRmdByNameIn(tmpRmd.getReportNameIn(),
						tmpRmd.getReportType().getReportTypeId());

				if (validateSameValue > 0) {
					facesUtil.addErrMessage(facesUtil.retrieveMessage("formTmpRmdReportName") + " "
//							+ facesUtil.retrieveMessage("indonesia") + " "
							+ facesUtil.retrieveMessage("errorAlreadyExists"));
					flag = true;
				}
			} else if (!flag && actionMode.equals(Constants.ACTION_EDIT)) {
				Integer validateSameValue = tmpRmdService.getTmpRmdByIdAndNameIn(tmpRmd.getRmdId(),
						tmpRmd.getReportNameIn(),
						tmpRmd.getReportType().getReportTypeId());

				if (validateSameValue > 0) {
					facesUtil.addErrMessage(facesUtil.retrieveMessage("formTmpRmdReportName") + " "
//							+ facesUtil.retrieveMessage("indonesia") + " "
							+ facesUtil.retrieveMessage("errorAlreadyExists"));
					flag = true;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return flag;
	}

	public void save() {
		try {

			if (!validate()) {
				if (tmpRmd.getDueDateDetails() != null) {
					for (int i = 0; i < tmpRmd.getDueDateDetails().size(); i++) {
						TmpRmdDueDate dtl = (TmpRmdDueDate) tmpRmd.getDueDateDetails().get(i);
						dtl.setTmpRmd(tmpRmd);
						if (dtl.getCreatedBy() == null) {
							dtl.setCreatedBy(facesUtil.retrieveUserLogin());
							dtl.setCreationDate(new Timestamp(new Date().getTime()));
						}

						if ("day".equals(dueDateType)) {
							// dtl.setDueDay(null);
							dtl.setDueDateDateOnly(null);
							dtl.setDueDate(null);
						} else if ("dateonly".equals(dueDateType)) {
							dtl.setDueDay(null);
							// dtl.setDueDateDateOnly(null);
							dtl.setDueDate(null);
						} else {
							dtl.setDueDay(null);
							dtl.setDueDateDateOnly(null);
							// dtl.setDueDate(null);
						}

						dtl.setLastUpdateBy(facesUtil.retrieveUserLogin());
						dtl.setLastUpdateDate(new Timestamp(new Date().getTime()));
						dtl.setDelId(new Long(0));
						dtl.setEnabledFlag(Constants.CONSTANT_YES);
					}
				}

				if (tmpRmd.getRegulationDetails() != null) {
					for (int i = 0; i < tmpRmd.getRegulationDetails().size(); i++) {
						TmpRmdRegulation dtl = (TmpRmdRegulation) tmpRmd.getRegulationDetails().get(i);
						dtl.setTmpRmd(tmpRmd);
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

				if (tmpRmd.getSupportingUnitDetails() != null) {
					for (int i = 0; i < tmpRmd.getSupportingUnitDetails().size(); i++) {
						TmpRmdSupportingUnit dtl = (TmpRmdSupportingUnit) tmpRmd.getSupportingUnitDetails().get(i);
						dtl.setTmpRmd(tmpRmd);
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

				if (tmpRmd.getCorrespondenceDetails() != null) {
					for (int i = 0; i < tmpRmd.getCorrespondenceDetails().size(); i++) {
						TmpRmdCorrespondence dtl = (TmpRmdCorrespondence) tmpRmd.getCorrespondenceDetails().get(i);
						dtl.setTmpRmd(tmpRmd);
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

				ParameterDetail pdReminderStatus = parameterDetailService
						.getParameterDetailByParamDtlCode(tmpRmd.getReminderStatus().getParameterDtlCode());
				tmpRmd.setReminderStatus(pdReminderStatus);
				
				if (tmpRmd.getCounterType().getCounterTypeId() != null) {
					CounterType ct = counterTypeService.findById(tmpRmd.getCounterType().getCounterTypeId());
					tmpRmd.setCounterType(ct);
				}
				
				if(tmpRmd.getPicFollowupEmailDetails()!=null && tmpRmd.getPicFollowupEmailDetails().size()>0) {
					tmpRmd.getPicFollowupEmailDetails().clear();
				}
				
				for(int i=0;i<tmpRmd.getDueDateDetails().size();i++) {
					TmpRmdDueDate tmpRmdDueDate = tmpRmd.getDueDateDetails().get(i);
					Calendar calendar = Calendar.getInstance();
					Date targetDateTmp = tmpRmdDueDate.getDueDate();
					int row = 0;
					Boolean flag = false;
					if(tmpRmd.getPicFollowupEmailDetails()!=null && tmpRmd.getPicFollowupEmailDetails().size()>0) {
						flag = true;
					}
					
					
					for (CounterTypeDtl dataCounterTypeDtl : tmpRmd.getCounterType().getDetails()) {
						
						Boolean flagLoop = false;
						if (dataCounterTypeDtl.getSlaType().equals("+")) {
							
							calendar.setTime(tmpRmdDueDate.getDueDate());
							calendar.add(Calendar.DAY_OF_MONTH, dataCounterTypeDtl.getSla().intValue());
							targetDateTmp = calendar.getTime();
							
							while(!flagLoop) {
								int day = calendar.get(Calendar.DAY_OF_WEEK);
								
								if (day == 1 || day == 7) {
									// do nothing
									calendar.add(Calendar.DAY_OF_MONTH, 1);
									targetDateTmp = calendar.getTime();
								} else {
									
										if (Boolean.TRUE.equals(holidayService.isAvailableDate(targetDateTmp))) {
											flagLoop = true;
										}else {
											calendar.add(Calendar.DAY_OF_MONTH, 1);
											targetDateTmp = calendar.getTime();
										}
									
									
								}
							}
							
							
						} else if (dataCounterTypeDtl.getSlaType().equals("-")) {
							
							calendar.setTime(targetDateTmp);
							calendar.add(Calendar.DAY_OF_MONTH, -dataCounterTypeDtl.getSla().intValue());
							targetDateTmp = calendar.getTime();
							
							while(!flagLoop) {
							int day = calendar.get(Calendar.DAY_OF_WEEK);
							
							if (day == 1 || day == 7) {
								// do nothing
								calendar.add(Calendar.DAY_OF_MONTH, -1);
								targetDateTmp = calendar.getTime();
							} else {
								
									if (Boolean.TRUE.equals(holidayService.isAvailableDate(targetDateTmp))) {
										flagLoop = true;
									}else {
										calendar.add(Calendar.DAY_OF_MONTH, -1);
										targetDateTmp = calendar.getTime();
									}
								
								
							}
							}
						}
						
						TmpRmdPicFollowupEmail tmpRmdPicFollowupEmail =  null;
						Boolean flagAddRec = false;
						if(flag) {
							if(row<tmpRmd.getPicFollowupEmailDetails().size() && tmpRmd.getPicFollowupEmailDetails().get(row)!=null && tmpRmd.getPicFollowupEmailDetails().get(row).getRmdPicFollowupEmailId()!=null) {
								tmpRmdPicFollowupEmail = tmpRmd.getPicFollowupEmailDetails().get(row);
							}else {
								tmpRmdPicFollowupEmail =  new TmpRmdPicFollowupEmail();
								flagAddRec = true;
							}
						}else {
							tmpRmdPicFollowupEmail =  new TmpRmdPicFollowupEmail();
							 flagAddRec = true;
						}
						
						
						tmpRmdPicFollowupEmail.setTmpRmd(tmpRmd);
						tmpRmdPicFollowupEmail.setSla(dataCounterTypeDtl.getSla().longValue());
						tmpRmdPicFollowupEmail.setSlaType(dataCounterTypeDtl.getSlaType());
						tmpRmdPicFollowupEmail.setEmailDate(new Timestamp(targetDateTmp.getTime()));
						tmpRmdPicFollowupEmail.setTargetDate(new Timestamp(tmpRmdDueDate.getDueDate().getTime()));
						
						if(tmpRmdPicFollowupEmail.getRmdPicFollowupEmailId() != null)
							EntityUtil.setUpdateInfo(tmpRmdPicFollowupEmail, facesUtil.retrieveUserLogin());
						else
							EntityUtil.setCreationInfo(tmpRmdPicFollowupEmail, facesUtil.retrieveUserLogin());
						
						
						targetDateTmp = tmpRmdDueDate.getDueDate();
						row++;
						
						if(flagAddRec) {
							if(tmpRmd.getPicFollowupEmailDetails() == null) {
								List<TmpRmdPicFollowupEmail> tmpPicFollowupEmailList = new ArrayList<>();
								tmpPicFollowupEmailList.add(tmpRmdPicFollowupEmail);
								tmpRmd.setPicFollowupEmailDetails(tmpPicFollowupEmailList);
							}else {
								tmpRmd.getPicFollowupEmailDetails().add(tmpRmdPicFollowupEmail);
							}
						}
					}
				}

				if (tmpRmd.getRmdId() != null) {
					tmpRmd.setStatus(parameterDetailService
							.getParameterDetailByParamDtlCode(ParameterDetail.PARAM_DET_CODE_DATA_ACTIVE));
					tmpRmd.setLastUpdateBy(facesUtil.retrieveUserLogin());
					tmpRmd.setLastUpdateDate(new Timestamp(new Date().getTime()));
					tmpRmd.setDelId(new Long(0));
					tmpRmd.setEnabledFlag(Constants.CONSTANT_YES);
					
					tmpRmdService.update(tmpRmd);
					approve(tmpRmd.getRmdId());
					
				} else {
					tmpRmd.setStatus(parameterDetailService
							.getParameterDetailByParamDtlCode(ParameterDetail.PARAM_DET_CODE_DATA_ACTIVE));
					tmpRmd.setCreatedBy(facesUtil.retrieveUserLogin());
					tmpRmd.setCreationDate(new Timestamp(new Date().getTime()));
					tmpRmd.setDelId(new Long(0));
					tmpRmd.setEnabledFlag(Constants.CONSTANT_YES);
					tmpRmdService.save(tmpRmd);
					approve(tmpRmd.getRmdId());
					
				}
				
				
				
					
					// this should be a singleton
			        ExecutorService emailExecutor = Executors.newCachedThreadPool();

			        // from you sendEmail() method
			        emailExecutor.execute(new Runnable() {
			            @Override
			            public void run() {
			                try {
			                	sendEmail(tmpRmd.getRmdId());
			                } catch (Exception e) {
			                    logger.error("send email failed", e);
			                }
			            }
			        });
				

				
				
				facesUtil.redirect("/pages/tmpRmd/tmpRmd.faces");
			}

		} catch (Exception ex) {
			facesUtil.addFacesMsg(FacesMessage.SEVERITY_ERROR, null, "Operation Failed : " + ex.getMessage(), "");
			ex.printStackTrace();
		}

	}
	
	/*public void approve() {
		
			TmpRmd tmpRmdApp = tmpRmdService2.findById(tmpRmd.getRmdId());
			
			if (tmpRmdApp.getApprovalDetails() == null || tmpRmdApp.getApprovalDetails().size() == 0) {
				tmpRmdApp.setApprovalDetails(new ArrayList<TmpRmdApproval>());
			}

			TmpRmdApproval dtl = new TmpRmdApproval();
			dtl.setTmpRmd(tmpRmdApp);
			dtl.setApprovalDate(new Date());
			dtl.setApprovalNote("Approve By System");
			dtl.setApprovalStatus(parameterDetailService
					.getParameterDetailByParamDtlCode(ParameterDetail.PARAM_DET_CODE_STATUS_APPROVED));
			dtl.setUser(userService.getUserByNik(facesUtil.retrieveUserLogin()));
			if (dtl.getCreatedBy() == null) {
				dtl.setCreatedBy(facesUtil.retrieveUserLogin());
				dtl.setCreationDate(new Timestamp(new Date().getTime()));
			}

			dtl.setDelId(new Long(0));
			dtl.setEnabledFlag(Constants.CONSTANT_YES);

			tmpRmdApp.getApprovalDetails().add(dtl);

			if (tmpRmdApp.getRmdId() != null) {
				tmpRmdApp.setStatus(parameterDetailService
						.getParameterDetailByParamDtlCode(ParameterDetail.PARAM_DET_CODE_DATA_ACTIVE));
				tmpRmdApp.setLastUpdateBy(facesUtil.retrieveUserLogin());
				tmpRmdApp.setLastUpdateDate(new Timestamp(new Date().getTime()));
				tmpRmdApp.setDelId(new Long(0));
				tmpRmdApp.setEnabledFlag(Constants.CONSTANT_YES);
				tmpRmdService2.update(tmpRmdApp);
				
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

			

		

	}*/
	public void approve(Long rmdId) {
		try {
			
			TmpRmd tmpRmdNew = tmpRmdService.findById(rmdId);
			
			if (tmpRmdNew.getApprovalDetails() == null || tmpRmdNew.getApprovalDetails().size() == 0) {
				tmpRmdNew.setApprovalDetails(new ArrayList<TmpRmdApproval>());
			}

			TmpRmdApproval dtl = new TmpRmdApproval();
			dtl.setTmpRmd(tmpRmdNew);
			dtl.setApprovalDate(new Date());
			dtl.setApprovalNote("Approve By System");
			dtl.setApprovalStatus(parameterDetailService
					.getParameterDetailByParamDtlCode(ParameterDetail.PARAM_DET_CODE_STATUS_APPROVED));
			dtl.setUser(userService.getUserByNik(facesUtil.retrieveUserLogin()));
			if (dtl.getCreatedBy() == null) {
				dtl.setCreatedBy(facesUtil.retrieveUserLogin());
				dtl.setCreationDate(new Timestamp(new Date().getTime()));
			}

			dtl.setDelId(new Long(0));
			dtl.setEnabledFlag(Constants.CONSTANT_YES);

			tmpRmdNew.getApprovalDetails().add(dtl);
			tmpRmdService2.update(tmpRmdNew);

		} catch (Exception ex) {
			facesUtil.addFacesMsg(FacesMessage.SEVERITY_ERROR, null, "Operation Failed : " + ex.getMessage(), "");
			ex.printStackTrace();
		}

	}

	@SuppressWarnings("unused")
	public void sendEmail(Long rmdId) {
		try {
			
			EmailTemplate emailTemplate = emailTemplateService.getEmailTemplateByEmailTemplateCode("EMAIL_RMD");
			
			TmpRmd tmpRmdNew = tmpRmdService.findById(rmdId);
			
			String emailSubject = emailTemplate.getEmailSubject().replaceAll("counter_type","NOTIFICATION");
				   emailSubject = emailSubject.replaceAll("report_name_in",tmpRmdNew.getReportNameIn());
				   emailSubject = emailSubject.replaceAll("report_name_en",tmpRmdNew.getReportNameEn());
			
			String emailContent = ""; 
			String emailTo  = "";
			String emailCc1 = "";
			String emailCc2 = "";
			String emailCc = "";
			String emailCcSupporting = "";
			String emailUserInputerCc = "";
			String emailAtasanUserInputerCc = "";
			
			ParameterDetail pdHostName = parameterDetailService.getParameterDetailByParamDtlCode(Constants.HOST_NAME_APPLICATION);
			
			TrcRmdPicFollowup picFollowupId = trcRmdService.getFirstPicFollowupId(rmdId);
			//TrcRmdPicFollowup picFollowupId = tmpRmdNew.getPicFollowupDetails().get(0);
			String token = "";
			String dueDate = "";
			if (picFollowupId != null && picFollowupId.getRmdPicFollowupId() != null) {
				token = Constants.encryptString(picFollowupId.getRmdPicFollowupId().toString());
				dueDate = (picFollowupId.getTargetDate() != null ? sdf.format(picFollowupId.getTargetDate()) : "");
			} else {
				//token = Constants.encryptString(rmdId.toString());
				dueDate = tmpRmdNew.getDueDateDetails()!=null && tmpRmdNew.getDueDateDetails().size()>0 ?sdf.format(tmpRmdNew.getDueDateDetails().get(0).getDueDate()):"";
			}
			String menuId = Constants.encryptString(Constants.MENU_ID_FOLLOWUP_CONFIRMATION_RMD);
			String urlLink = pdHostName.getNameIn().concat("pages/regulatoryReportingFE/regulatoryReportingFEEdit.faces?token="+token+"&menuId="+menuId);
			
		    if (tmpRmdNew.getReminderStatus()!=null && tmpRmdNew.getReminderStatus().getParameterDtlCode().equals(Constants.REMINDER_ACTIVE)) {
				
				emailContent = emailTemplate.getEmailContent().replaceAll("target_date", tmpRmdNew.getTargetDate()!=null?sdf.format(tmpRmdNew.getTargetDate()):"");
				emailContent = emailContent.replaceAll("report_name_in", tmpRmdNew.getReportNameIn());
				emailContent = emailContent.replaceAll("report_name_en", tmpRmdNew.getReportNameEn());
				emailContent = emailContent.replaceAll("due_date", dueDate);
				
				
				emailContent = emailContent.replaceAll("dedicated_to", tmpRmdNew.getDedicatedTo()!=null?tmpRmdNew.getDedicatedTo():"");
				emailContent = emailContent.replaceAll("regulation_title_in", tmpRmdNew.getRegulationDetails()!=null && tmpRmdNew.getRegulationDetails().size()>0 ? tmpRmdNew.getRegulationDetails().get(0).getRegulationMst().getNameIn():"");
				emailContent = emailContent.replaceAll("regulation_title_en", tmpRmdNew.getRegulationDetails()!=null && tmpRmdNew.getRegulationDetails().size()>0 ? tmpRmdNew.getRegulationDetails().get(0).getRegulationMst().getNameEn():"");
				emailContent = emailContent.replaceAll("document_number", tmpRmdNew.getRegulationDetails()!=null && tmpRmdNew.getRegulationDetails().size()>0 ? tmpRmdNew.getRegulationDetails().get(0).getRegulationMst().getDocumentNo():"");
				emailContent = emailContent.replaceAll("letter_no", tmpRmdNew.getCorrespondenceDetails()!=null && tmpRmdNew.getCorrespondenceDetails().size()>0 ? tmpRmdNew.getCorrespondenceDetails().get(0).getTrcCorrespondence().getLetterNo():"");
				emailContent = emailContent.replaceAll("publisher_unit", tmpRmdNew.getRegulationDetails()!=null && tmpRmdNew.getRegulationDetails().size()>0 ? (tmpRmdNew.getRegulationDetails().get(0).getRegulationMst().getPublisherUnit()!=null?tmpRmdNew.getRegulationDetails().get(0).getRegulationMst().getPublisherUnit():""):"");
				emailContent = emailContent.replaceAll("sanction", tmpRmdNew.getSanctions()!=null?tmpRmdNew.getSanctions():"");
				emailContent = emailContent.replaceAll("division_name", (tmpRmdNew.getDivisionId()!=null?userService.getDivisionNameByDivisionId(tmpRmdNew.getDivisionId()):"NA"));
				emailContent = emailContent.replaceAll("pic_1_name", (tmpRmdNew.getUser1() != null?tmpRmdNew.getUser1().getName():"NA"));
				emailContent = emailContent.replaceAll("pic_2_name", (tmpRmdNew.getUser2()!=null?tmpRmdNew.getUser2().getName():"NA"));
				emailContent = emailContent.replaceAll("pic_3_name", (tmpRmdNew.getUser3()!=null?tmpRmdNew.getUser3().getName():"NA"));
				emailContent = emailContent.replaceAll("url_link", urlLink);
				emailContent = emailContent.replaceAll("description", tmpRmdNew.getDescription()!=null?tmpRmdNew.getDescription():"");
				emailContent = emailContent.replaceAll("report_delivery", tmpRmdNew.getDedicatedTo()!=null?tmpRmdNew.getDedicatedTo():"");
				
				try {
					emailContent = emailContent.replaceAll("document_category_in",
							tmpRmdNew.getRegulationDetails() != null && tmpRmdNew.getRegulationDetails().size() > 0
							&& tmpRmdNew.getRegulationDetails().get(0).getRegulationMst() != null
							&& tmpRmdNew.getRegulationDetails().get(0).getRegulationMst().getDocumentCategory() != null
									? tmpRmdNew.getRegulationDetails().get(0).getRegulationMst().getDocumentCategory()
											.getDocumentCategoryIn()
									: "");
					emailContent = emailContent.replaceAll("document_category_en",
							tmpRmdNew.getRegulationDetails() != null && tmpRmdNew.getRegulationDetails().size() > 0
									&& tmpRmdNew.getRegulationDetails().get(0).getRegulationMst() != null
									&& tmpRmdNew.getRegulationDetails().get(0).getRegulationMst().getDocumentCategory() != null
											? tmpRmdNew.getRegulationDetails().get(0).getRegulationMst().getDocumentCategory()
													.getDocumentCategoryEn()
											: "");
				} catch (Exception ex) {
					if (tmpRmdNew.getRegulationDetails() != null  && tmpRmdNew.getRegulationDetails().size() > 0
							&& tmpRmdNew.getRegulationDetails().get(0).getRegulationMst() != null 
							&& tmpRmdNew.getRegulationDetails().get(0).getRegulationMst().getDocumentCategory() != null
							) {
						logger.debug("document category name is not fetched");
						DocumentCategory tmp = documentCategoryService.findById(tmpRmdNew.getRegulationDetails().get(0).getRegulationMst().getDocumentCategory().getDocumentCategoryId());
						emailContent = emailContent.replaceAll("document_category_in", tmp.getDocumentCategoryIn());
					} else {
						ex.printStackTrace();
					}
				}
	
				try {
					emailContent = emailContent.replaceAll("report_type_in", tmpRmdNew.getReportType() != null ? tmpRmdNew.getReportType().getReportTypeIn() : "");
					emailContent = emailContent.replaceAll("report_type_en", tmpRmdNew.getReportType() != null ? tmpRmdNew.getReportType().getReportTypeEn() : "");
				} catch (Exception ex) {
					if (tmpRmdNew.getReportType() != null) {
						logger.debug("report type name is not fetched");
						ReportType tmp = reportTypeService.findById(tmpRmdNew.getReportType().getReportTypeId());
						emailContent = emailContent.replaceAll("report_type_in", tmp.getReportTypeIn());
						emailContent = emailContent.replaceAll("report_type_en", tmp.getReportTypeEn());
					} else {
						ex.printStackTrace();
					}
				}
				
				if (tmpRmdNew.getSupportingUnitDetails() != null && tmpRmdNew.getSupportingUnitDetails().size() > 0) {
					if (tmpRmdNew.getSupportingUnitDetails().size() == 1) {
						TmpRmdSupportingUnit trsu = tmpRmdNew.getSupportingUnitDetails().get(0);
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
									
									TmpRmdSupportingUnit trsu = tmpRmdNew.getSupportingUnitDetails().get(0);
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
									for (int y = 1; y < tmpRmdNew.getSupportingUnitDetails().size(); y++) {
										temp += partial;
										
										trsu = tmpRmdNew.getSupportingUnitDetails().get(y);
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
				
				for(int i=0;tmpRmdNew.getSupportingUnitDetails() != null && i<tmpRmdNew.getSupportingUnitDetails().size();i++) {
					TmpRmdSupportingUnit su = tmpRmdNew.getSupportingUnitDetails().get(i);
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
				
//					for(int x=0;x<tmpRmdNew.getCounterType().getDetails().size();x++) {
				if (tmpRmdNew != null && tmpRmdNew.getCounterType() != null && tmpRmdNew.getCounterType().getDetails() != null && tmpRmdNew.getCounterType().getDetails().size() > 0) {
					CounterTypeDtl cd = tmpRmdNew.getCounterType().getDetails().get(0);
					emailTo = cd.getEmailTo();
					emailCc1 = cd.getEmailCc1();
					emailCc2 = cd.getEmailCc2();
					
					if(emailTo.equals(Constants.REMINDER_PIC1)) {
						emailTo = tmpRmdNew.getUser1()!=null?tmpRmdNew.getUser1().getEmail():"";
					}
					else if(emailTo.equals(Constants.REMINDER_PIC2)) {
						emailTo = tmpRmdNew.getUser2()!=null?tmpRmdNew.getUser2().getEmail():"";
					}
					else if(emailTo.equals(Constants.REMINDER_PIC3)) {
						emailTo = tmpRmdNew.getUser3()!=null?tmpRmdNew.getUser3().getEmail():"";
					}
					
					if(emailCc1!=null && emailCc1.equals(Constants.REMINDER_PIC1)) {
						emailCc1 = tmpRmdNew.getUser1()!=null?tmpRmdNew.getUser1().getEmail():"";
					}
					else if(emailCc1!=null && emailCc1.equals(Constants.REMINDER_PIC2)) {
						emailCc1 = tmpRmdNew.getUser2()!=null?tmpRmdNew.getUser2().getEmail():"";
					}
					else if(emailCc1!=null && emailCc1.equals(Constants.REMINDER_PIC3)) {
						emailCc1 = tmpRmdNew.getUser3()!=null?tmpRmdNew.getUser3().getEmail():"";
					}
					
					if(emailCc2!=null && emailCc2.equals(Constants.REMINDER_PIC1)) {
						emailCc2 = tmpRmdNew.getUser1()!=null?tmpRmdNew.getUser1().getEmail():"";
					}
					else if(emailCc2!=null && emailCc2.equals(Constants.REMINDER_PIC2)) {
						emailCc2 = tmpRmdNew.getUser2()!=null?tmpRmdNew.getUser2().getEmail():"";
					}
					else if(emailCc2!=null && emailCc2.equals(Constants.REMINDER_PIC3)) {
						emailCc2 = tmpRmdNew.getUser3()!=null?tmpRmdNew.getUser3().getEmail():"";
					}
					
					if(StringUtils.isNotEmpty(emailCc1)) {
						emailCc = emailCc.concat(emailCc1);
					}
					if(StringUtils.isNotEmpty(emailCc2)) {
						emailCc = StringUtils.isNotEmpty(emailCc)?emailCc.concat(",").concat(emailCc2):emailCc.concat(emailCc2);
					}
					
					emailUserInputerCc = tmpRmdNew.getPicCompliance().getEmail();
					if (StringUtils.isNotEmpty(emailUserInputerCc)) {
						if (StringUtils.isNotEmpty(emailCc)) {
							emailCc = emailCc.concat(",").concat(emailUserInputerCc);
						} else {
							emailCc = emailUserInputerCc;
						}
					}
					
					emailAtasanUserInputerCc = tmpRmdNew.getPicComplianceSuperior().getEmail();
					if (StringUtils.isNotEmpty(emailAtasanUserInputerCc)) {
						if (StringUtils.isNotEmpty(emailCc)) {
							emailCc = emailCc.concat(",").concat(emailAtasanUserInputerCc);
						} else {
							emailCc = emailAtasanUserInputerCc;
						}
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
					
					
					CallApiManager.sendEmailAPI(to,cc, subject,
							content, "EMAIL_RMD", "true", parameterDetailService);

			        
					
					
				}
				
				
			}
		    
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new CustomAPIException(ex.getMessage()); 
		}
		
	}
	/*public void sendEmail() {
		try {

			EmailTemplate emailTemplate = emailTemplateService
					.getEmailTemplateByEmailTemplateCode(Constants.EMAIL_APPROVAL);
			String emailSubject = emailTemplate.getEmailSubject();

			String emailContent = "";
			String emailTo = "";
			String emailCc = "";

			String reportNameTemp = tmpRmd.getReportNameIn() != null ? " - " + tmpRmd.getReportNameIn() : "";

			emailContent = emailTemplate.getEmailContent().replace(Constants.NOTIFICATION_TYPE_AND_DOC_NUM,
					"Report Matrix Diary" + reportNameTemp);

			ParameterDetail paramEmail = parameterDetailService
					.getParameterDetailByParamDtlCode(Constants.OSCAR_CHECKER);
			emailTo = paramEmail.getNameIn();

			final String subject = emailSubject;
			final String content = emailContent;
			final String to = emailTo;
			// final String to = "h3ndr407@gmail.com";
			final String cc = emailCc;

			CallApiManager.sendEmailAPI(to, cc, subject, content, "EMAIL_RMD", "true", parameterDetailService);

		} catch (Exception ex) {
			ex.printStackTrace();
			throw new CustomAPIException(ex.getMessage());
		}

	}*/

	public void downloadFile(String fileId, String fileName, byte[] content) throws Exception {
		CallApiManager.downloadFile(fileId, fileName, content, parameterDetailService);
	}

	public void cancel() {
		try {
			facesUtil.redirect("/pages/tmpRmd/tmpRmd.faces");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void hitungTargetDate() throws Exception {
		Calendar calendarRec = Calendar.getInstance();
		Date targetDateTmpRec = tmpRmd.getRecurringStartDate();
		Date endDateRec = tmpRmd.getRecurringEndDate();
		ParameterDetail pd = parameterDetailService.getParameterDetailByParamDtlCode(tmpRmd.getRecurringType());
		Integer recurringMonth = Integer.parseInt(pd.getNameEn());
		Boolean flagLoopMonthRec = false;
		calendarRec.setTime(targetDateTmpRec);
		List<Date> listTargetDate = new ArrayList<>();
		listTargetDate.add(targetDateTmpRec);
		
		while(!flagLoopMonthRec) {
			calendarRec.add(Calendar.MONTH, recurringMonth);
			if(calendarRec.getTime().before(endDateRec)) {
				listTargetDate.add(calendarRec.getTime());
			}else {
				flagLoopMonthRec = true;
			}
			
		}
		
		if (tmpRmd.getDueDateDetails() == null) {
			tmpRmd.setDueDateDetails(new ArrayList<TmpRmdDueDate>());
			lastSequenceOfDueDate = 0;
		} else {
			if (tmpRmd.getDueDateDetails().size() == 0) {
				lastSequenceOfDueDate = 0;
			}
			
			tmpRmd.getDueDateDetails().clear();
		}
		
		for(int i=0;i<listTargetDate.size();i++) {
			Date targetDate = listTargetDate.get(i);
			
			
			TmpRmdDueDate d = new TmpRmdDueDate();
			lastSequenceOfDueDate = lastSequenceOfDueDate + 1;
			d.setSequence(lastSequenceOfDueDate);
			d.setDueDate(targetDate);
			tmpRmd.getDueDateDetails().add(d);

			
		}
		
		tableRmdDueDateModel.setWrappedData(tmpRmd.getDueDateDetails());

		PrimeFaces.current().executeScript("reInitSelect2();");
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
	/*
	 * public ParameterDetailService getParameterDetailService() { return
	 * parameterDetailService; }
	 * 
	 * public void setParameterDetailService(ParameterDetailService
	 * parameterDetailService) { this.parameterDetailService =
	 * parameterDetailService; }
	 */

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

	@Override
	public void itemSelected(String clientId, String widgetVar, Object selectedItem) {
		if (StringUtils.equals("pic1Dialog", widgetVar)) {
			Object[] objects = (Object[]) selectedItem;
			// User user1 = userService.findById(((BigInteger) objects[0]).longValue());
			User user1 = userService.findById(MathUtil.returnIdObjectToLong(objects[0]));
			if (user1 != null) {
				tmpRmd.setUser1(user1);
				tmpRmd.setUserNameTemp1(user1.getNik() + "-" + user1.getName());

				User user2 = userService.getUserByNik(user1.getPukNik());
				if (user2 != null) {
					tmpRmd.setUser2(user2);
					tmpRmd.setUserNameTemp2(user2.getNik() + "-" + user2.getName());

					User user3 = userService.getUserByNik(user2.getPukNik());
					if (user3 != null) {
						tmpRmd.setUser3(user3);
						tmpRmd.setUserNameTemp3(user3.getNik() + "-" + user3.getName());
					}
				}
			}
		}

		if (StringUtils.equals("pic2Dialog", widgetVar)) {
			Object[] objects = (Object[]) selectedItem;

			// User user2 = userService.findById(((BigInteger) objects[0]).longValue());
			User user2 = userService.findById(MathUtil.returnIdObjectToLong(objects[0]));
			if (user2 != null) {
				tmpRmd.setUser2(user2);
				tmpRmd.setUserNameTemp2(user2.getNik() + "-" + user2.getName());

				User user3 = userService.getUserByNik(user2.getPukNik());
				if (user3 != null) {
					tmpRmd.setUser3(user3);
					tmpRmd.setUserNameTemp3(user3.getNik() + "-" + user3.getName());
				}
			}
		}

		if (StringUtils.equals("pic3Dialog", widgetVar)) {
			Object[] objects = (Object[]) selectedItem;

			// User user3 = userService.findById(((BigInteger) objects[0]).longValue());
			User user3 = userService.findById(MathUtil.returnIdObjectToLong(objects[0]));
			if (user3 != null) {
				tmpRmd.setUser3(user3);
				tmpRmd.setUserNameTemp3(user3.getNik() + "-" + user3.getName());
			}
		}
		
		if (StringUtils.equals("picComplianceDialog", widgetVar)) {
			Object[] objects = (Object[]) selectedItem;

			User picCompliance = userService.findById(MathUtil.returnIdObjectToLong(objects[0]));
			if (picCompliance != null) {
				tmpRmd.setPicCompliance(picCompliance);
				tmpRmd.setPicComplianceTemp(picCompliance.getNik() + "-" + picCompliance.getName());

				User picComplianceAtasan = userService.getUserByNik(picCompliance.getPukNik());
				if (picComplianceAtasan != null) {
					tmpRmd.setPicComplianceSuperior(picComplianceAtasan);
					tmpRmd.setPicComplianceSuperiorTemp(picComplianceAtasan.getNik() + "-" + picComplianceAtasan.getName());
				}
			}
		}
		
		if (StringUtils.equals("picComplianceSuperiorDialog", widgetVar)) {
			Object[] objects = (Object[]) selectedItem;
			
			User picComplianceSuperior = userService.findById(MathUtil.returnIdObjectToLong(objects[0]));
			if (picComplianceSuperior != null) {
				tmpRmd.setPicComplianceSuperior(picComplianceSuperior);
				tmpRmd.setPicComplianceSuperiorTemp(picComplianceSuperior.getNik() + "-" + picComplianceSuperior.getName());
			}
		}
		

		if (StringUtils.equals("regulationDialog", widgetVar)) {
			Regulation obj = (Regulation) selectedItem;
			// RegulationMst reg = regulationMstService.findById(((BigInteger)
			// objects[0]).longValue());
			RegulationMst reg = regulationMstService.findById(obj.getRegulationId());
			tmpRmd.getRegulationDetails().get(indexDtl).setRegulationMst(reg);

			tableRmdRegulationModel.setWrappedData(tmpRmd.getRegulationDetails());
		}

		if (StringUtils.equals("correspondenceDialog", widgetVar)) {
			TrcCorrespondence tc = (TrcCorrespondence) selectedItem;
			// RegulationMst reg = regulationMstService.findById(((BigInteger)
			// objects[0]).longValue());
			// RegulationMst reg = regulationMstService.findById(obj.getRegulationId());
			tmpRmd.getCorrespondenceDetails().get(indexCorrespondenceDtl).setTrcCorrespondence(tc);

			tableRmdCorrespondenceModel.setWrappedData(tmpRmd.getCorrespondenceDetails());
		}

		if (StringUtils.equals("emailCc1Dialog", widgetVar)) {
			Object[] objects = (Object[]) selectedItem;
			// User user1 = userService.findById(((BigInteger) objects[0]).longValue());
			User user1 = userService.findById(MathUtil.returnIdObjectToLong(objects[0]));
			if (user1 != null) {
				tmpRmd.getSupportingUnitDetails().get(indexDtlCc).setEmailCc1(user1);
				tmpRmd.getSupportingUnitDetails().get(indexDtlCc)
						.setEmailCcTemp1(user1.getNik() + "-" + user1.getName());

				User user2 = userService.getUserByNik(user1.getPukNik());
				if (user2 != null) {
					tmpRmd.getSupportingUnitDetails().get(indexDtlCc).setEmailCc2(user2);
					tmpRmd.getSupportingUnitDetails().get(indexDtlCc)
							.setEmailCcTemp2(user2.getNik() + "-" + user2.getName());

					User user3 = userService.getUserByNik(user2.getPukNik());
					if (user3 != null) {
						tmpRmd.getSupportingUnitDetails().get(indexDtlCc).setEmailCc3(user3);
						tmpRmd.getSupportingUnitDetails().get(indexDtlCc)
								.setEmailCcTemp3(user3.getNik() + "-" + user3.getName());
					}
				}

				tableRmdSupportingUnitModel.setWrappedData(tmpRmd.getSupportingUnitDetails());
			}
		}

		if (StringUtils.equals("emailCc2Dialog", widgetVar)) {
			Object[] objects = (Object[]) selectedItem;
			// User user2 = userService.findById(((BigInteger) objects[0]).longValue());
			User user2 = userService.findById(MathUtil.returnIdObjectToLong(objects[0]));
			if (user2 != null) {
				tmpRmd.getSupportingUnitDetails().get(indexDtlCc).setEmailCc2(user2);
				tmpRmd.getSupportingUnitDetails().get(indexDtlCc)
						.setEmailCcTemp2(user2.getNik() + "-" + user2.getName());

				User user3 = userService.getUserByNik(user2.getPukNik());
				if (user3 != null) {
					tmpRmd.getSupportingUnitDetails().get(indexDtlCc).setEmailCc3(user3);
					tmpRmd.getSupportingUnitDetails().get(indexDtlCc)
							.setEmailCcTemp3(user3.getNik() + "-" + user3.getName());
				}

				tableRmdSupportingUnitModel.setWrappedData(tmpRmd.getSupportingUnitDetails());
			}
		}

		if (StringUtils.equals("emailCc3Dialog", widgetVar)) {
			Object[] objects = (Object[]) selectedItem;
			// User user3 = userService.findById(((BigInteger) objects[0]).longValue());
			User user3 = userService.findById(MathUtil.returnIdObjectToLong(objects[0]));
			if (user3 != null) {
				tmpRmd.getSupportingUnitDetails().get(indexDtlCc).setEmailCc3(user3);
				tmpRmd.getSupportingUnitDetails().get(indexDtlCc)
						.setEmailCcTemp3(user3.getNik() + "-" + user3.getName());
				tableRmdSupportingUnitModel.setWrappedData(tmpRmd.getSupportingUnitDetails());
			}
		}

		PrimeFaces.current().executeScript("reInitSelect2();");
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

	public SelectorInfo getSelectorRegulation() {
		return selectorRegulation;
	}

	public void setSelectorRegulation(SelectorInfo selectorRegulation) {
		this.selectorRegulation = selectorRegulation;
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
	
	
	public SelectorInfo getselectorPicCompliance() {
		return selectorPicCompliance;
	}

	public void setSelectorPicCompliance(SelectorInfo selectorPicCompliance) {
		this.selectorPicCompliance = selectorPicCompliance;
	}
	
	
	public SelectorInfo getselectorPicComplianceSuperior() {
		return selectorPicComplianceSuperior;
	}

	public void selectorPicComplianceSuperior(SelectorInfo selectorPicComplianceSuperior) {
		this.selectorPicCompliance = selectorPicComplianceSuperior;
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

	public Integer getIndexDtlCc() {
		return indexDtlCc;
	}

	public void setIndexDtlCc(Integer indexDtlCc) {
		this.indexDtlCc = indexDtlCc;
	}

	public Integer getLastSequenceOfDueDate() {
		return lastSequenceOfDueDate;
	}

	public void setLastSequenceOfDueDate(Integer lastSequenceOfDueDate) {
		this.lastSequenceOfDueDate = lastSequenceOfDueDate;
	}

	public Integer getLastSequenceOfRegulation() {
		return lastSequenceOfRegulation;
	}

	public void setLastSequenceOfRegulation(Integer lastSequenceOfRegulation) {
		this.lastSequenceOfRegulation = lastSequenceOfRegulation;
	}

	public Integer getLastSequenceOfSupportingUnit() {
		return lastSequenceOfSupportingUnit;
	}

	public void setLastSequenceOfSupportingUnit(Integer lastSequenceOfSupportingUnit) {
		this.lastSequenceOfSupportingUnit = lastSequenceOfSupportingUnit;
	}

	public String getOldDueDateType() {
		return oldDueDateType;
	}

	public void setOldDueDateType(String oldDueDateType) {
		this.oldDueDateType = oldDueDateType;
	}

	public Integer getIndexCorrespondenceDtl() {
		return indexCorrespondenceDtl;
	}

	public void setIndexCorrespondenceDtl(Integer indexCorrespondenceDtl) {
		this.indexCorrespondenceDtl = indexCorrespondenceDtl;
	}

	public TrcCorrespondenceService getTrcCorrespondenceService() {
		return trcCorrespondenceService;
	}

	public void setTrcCorrespondenceService(TrcCorrespondenceService trcCorrespondenceService) {
		this.trcCorrespondenceService = trcCorrespondenceService;
	}

	public SelectorInfo getSelectorCorrespondence() {
		return selectorCorrespondence;
	}

	public void setSelectorCorrespondence(SelectorInfo selectorCorrespondence) {
		this.selectorCorrespondence = selectorCorrespondence;
	}

	public TmpRmdCorrespondenceTableModel<TmpRmdCorrespondence> getTableRmdCorrespondenceModel() {
		return tableRmdCorrespondenceModel;
	}

	public void setTableRmdCorrespondenceModel(
			TmpRmdCorrespondenceTableModel<TmpRmdCorrespondence> tableRmdCorrespondenceModel) {
		this.tableRmdCorrespondenceModel = tableRmdCorrespondenceModel;
	}

	public TmpRmdCorrespondence[] getSelectedRmdCorrespondenceData() {
		return selectedRmdCorrespondenceData;
	}

	public void setSelectedRmdCorrespondenceData(TmpRmdCorrespondence[] selectedRmdCorrespondenceData) {
		this.selectedRmdCorrespondenceData = selectedRmdCorrespondenceData;
	}

	public List<TrcRmdPicFollowup> getTrcRmdPicFollowupList() {
		return trcRmdPicFollowupList;
	}

	public void setTrcRmdPicFollowupList(List<TrcRmdPicFollowup> trcRmdPicFollowupList) {
		this.trcRmdPicFollowupList = trcRmdPicFollowupList;
	}

	public TrcRmdPicFollowupService getTrcRmdPicFollowupService() {
		return trcRmdPicFollowupService;
	}

	public void setTrcRmdPicFollowupService(TrcRmdPicFollowupService trcRmdPicFollowupService) {
		this.trcRmdPicFollowupService = trcRmdPicFollowupService;
	}

	public Integer getLastSequenceOfCorrespondence() {
		return lastSequenceOfCorrespondence;
	}

	public void setLastSequenceOfCorrespondence(Integer lastSequenceOfCorrespondence) {
		this.lastSequenceOfCorrespondence = lastSequenceOfCorrespondence;
	}

	public EmailTemplateService getEmailTemplateService() {
		return emailTemplateService;
	}

	public void setEmailTemplateService(EmailTemplateService emailTemplateService) {
		this.emailTemplateService = emailTemplateService;
	}

	public TmpRmdService getTmpRmdService2() {
		return tmpRmdService2;
	}

	public void setTmpRmdService2(TmpRmdService tmpRmdService2) {
		this.tmpRmdService2 = tmpRmdService2;
	}

	public DocumentCategoryService getDocumentCategoryService() {
		return documentCategoryService;
	}

	public void setDocumentCategoryService(DocumentCategoryService documentCategoryService) {
		this.documentCategoryService = documentCategoryService;
	}

	public TrcRmdService getTrcRmdService2() {
		return trcRmdService2;
	}

	public void setTrcRmdService2(TrcRmdService trcRmdService2) {
		this.trcRmdService2 = trcRmdService2;
	}

	public List<SelectItem> getRecurringTypeList() {
		return recurringTypeList;
	}

	public void setRecurringTypeList(List<SelectItem> recurringTypeList) {
		this.recurringTypeList = recurringTypeList;
	}

	public HolidayService getHolidayService() {
		return holidayService;
	}

	public void setHolidayService(HolidayService holidayService) {
		this.holidayService = holidayService;
	}
	
	

	
}