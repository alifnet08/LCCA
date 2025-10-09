package com.wo.module.reportType.bean;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.Constants;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.parameter.model.ParameterHeader;
import com.wo.module.reportType.constant.ReportTypeConstants;
import com.wo.module.reportType.model.ReportType;
import com.wo.module.reportType.service.ReportTypeService;

public class ReportTypeEditBean extends CommonBean implements Serializable {

	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(ReportTypeBean.class);

	private ReportType reportType;

	private Boolean isViewOnly;

	private String actionMode;

	private String editedId;

	private List<SelectItem> provTypes;
	
	private List<SelectItem> recurringTypeList;

	private ReportTypeService reportTypeService;

	public FacesUtil facesUtil;

	private String navigateSearch = ReportTypeConstants.NAVIGATE_SEARCH;

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
		initComboBox();
		checkNewOrEdit();
	}
	
	private void initComboBox() {
		try {
		recurringTypeList = new ArrayList<SelectItem>();
		List<ParameterDetail> listParamDtl;
		
			listParamDtl = parameterDetailService
					.getParameterDetailByParamCode(ParameterHeader.PARAM_HEAD_CODE_RECURRING_TYPE);
		

		for (ParameterDetail vo : listParamDtl) {
			SelectItem si = new SelectItem();
			si.setLabel(vo.getNameIn());
			si.setValue(vo.getParameterDtlCode());
			recurringTypeList.add(si);
		}
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
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
		reportType = new ReportType();

		actionMode = Constants.ACTION_ADD;
		facesUtil.setSessionAttribute("token", null);
	}

	private void handleEdit(String editId) {
		String token = facesUtil.retrieveRequestParam("token");
		if(StringUtils.isNotEmpty(token)) {
			editId = Constants.decryptString(token);
		}
		facesUtil.setSessionAttribute("token", Constants.encryptString(editId));
		actionMode = Constants.ACTION_EDIT;
		Long idLong = Long.parseLong(editId);
		reportType = reportTypeService.findById(idLong);
	}

	public Boolean validate(String dueDay, String dueDate, String dueMonth, String dueYear) {
		Boolean flag = false;
		/*if (StringUtils.isEmpty(reportType.getReportTypeEn())) {
			facesUtil.addErrMessage(facesUtil.retrieveMessage("formReportTypeTitle") + " en "
					+ facesUtil.retrieveMessage("validateRequired"));
			flag = true;
		}*/

		if (StringUtils.isEmpty(reportType.getReportTypeIn())) {
			facesUtil.addErrMessage(facesUtil.retrieveMessage("formReportTypeTitle") + " in "
					+ facesUtil.retrieveMessage("validateRequired"));
			flag = true;
		}

		if (StringUtils.isEmpty(reportType.getReportTypeDescription())) {
			facesUtil.addErrMessage(facesUtil.retrieveMessage("formReportTypeDocTypeDesc") + " in "
					+ facesUtil.retrieveMessage("validateRequired"));
			flag = true;
		}

		/*if (dueDate != null && dueDate.equals("true") && dueMonth != null && dueMonth.equals("true") && dueYear != null
				&& dueYear.equals("true") && (reportType.getNumberOfDueDate() == null)) {
			facesUtil.addErrMessage(facesUtil.retrieveMessage("formReportTypeNumberOfDueDate") + "  "
					+ facesUtil.retrieveMessage("validateRequired"));
			flag = true;
		}*/

		if (dueDay != null && dueDay.equals("true")) {
			if ( (dueDate != null && dueDate.equals("true")) 
					|| (dueMonth != null && dueMonth.equals("true"))
					|| (dueYear != null && dueYear.equals("true"))  ) {
				facesUtil.addErrMessage(facesUtil.retrieveMessage("formReportTypeDayTypeErrorCombination"));
				flag = true;
			}
		}

		if (reportTypeService.isReportTypeDuplicate(reportType)) {
			facesUtil.addErrMessage(facesUtil.retrieveMessage("errorAlreadyExists"));
			flag = true;
		}

		return flag;
	}

	public void save() {
		try {

			String dueDay = facesUtil.retrieveRequestParam("DUE_DAY");
			String dueDate = facesUtil.retrieveRequestParam("DUE_DATE");
			String dueMonth = facesUtil.retrieveRequestParam("DUE_MONTH");
			String dueYear = facesUtil.retrieveRequestParam("DUE_YEAR");

			reportType.setDueDay(
					dueDay != null && dueDay.equals("true") ? Constants.CONSTANT_YES : Constants.CONSTANT_NO);
			reportType.setDueDate(
					dueDate != null && dueDate.equals("true") ? Constants.CONSTANT_YES : Constants.CONSTANT_NO);
			reportType.setDueMonth(
					dueMonth != null && dueMonth.equals("true") ? Constants.CONSTANT_YES : Constants.CONSTANT_NO);
			reportType.setDueYear(
					dueYear != null && dueYear.equals("true") ? Constants.CONSTANT_YES : Constants.CONSTANT_NO);

			if (!validate(dueDay, dueDate, dueMonth, dueYear)) {
				if (reportType.getReportTypeId() != null) {

					reportType.setLastUpdateBy(facesUtil.retrieveUserLogin());
					reportType.setLastUpdateDate(new Timestamp(new Date().getTime()));
					reportType.setDelId(new Long(0));
					reportType.setEnabledFlag(Constants.CONSTANT_YES);
					reportTypeService.update(reportType);
				} else {

					reportType.setCreatedBy(facesUtil.retrieveUserLogin());
					reportType.setCreationDate(new Timestamp(new Date().getTime()));
					reportType.setDelId(new Long(0));
					reportType.setEnabledFlag(Constants.CONSTANT_YES);
					reportTypeService.save(reportType);
				}

				facesUtil.redirect("/pages/reportType/reportType.faces");
			}

		} catch (Exception ex) {
			facesUtil.addFacesMsg(FacesMessage.SEVERITY_ERROR, null, "Operation Failed : " + ex.getMessage(), "");
		}

	}

	public void cancel() {
		try {
			facesUtil.redirect("/pages/reportType/reportType.faces");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void onChangeCheckBox() {
		//System.out.println("day==" + reportType.isDueDayBoolean());
	}

	public FacesUtil getFacesUtil() {
		return facesUtil;
	}

	public void setFacesUtil(FacesUtil facesUtil) {
		this.facesUtil = facesUtil;
	}

	public ReportTypeService getReportTypeService() {
		return reportTypeService;
	}

	public void setReportTypeService(ReportTypeService reportTypeService) {
		this.reportTypeService = reportTypeService;
	}

	public String getNavigateSearch() {
		return navigateSearch;
	}

	public void setNavigateSearch(String navigateSearch) {
		this.navigateSearch = navigateSearch;
	}

	public ReportType getReportType() {
		return reportType;
	}

	public void setReportType(ReportType reportType) {
		this.reportType = reportType;
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

	public List<SelectItem> getRecurringTypeList() {
		return recurringTypeList;
	}

	public void setRecurringTypeList(List<SelectItem> recurringTypeList) {
		this.recurringTypeList = recurringTypeList;
	}

	
}