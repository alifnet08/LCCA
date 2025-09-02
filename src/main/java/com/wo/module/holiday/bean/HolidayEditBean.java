package com.wo.module.holiday.bean;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.primefaces.event.SelectEvent;

import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.Constants;
import com.wo.module.holiday.constant.HolidayConstants;
import com.wo.module.holiday.model.Holiday;
import com.wo.module.holiday.service.HolidayService;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.parameter.service.ParameterDetailService;

public class HolidayEditBean extends CommonBean implements Serializable {

	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(HolidayBean.class);

	private Holiday holiday;

	private Holiday tempHoliday;

	private Boolean isViewOnly;

	private String actionMode;

	private String editedId;

	private String checkSameValue;

	private List<SelectItem> provTypes;

	private List<SelectItem> categories;

	private HolidayService holidayService;

	public FacesUtil facesUtil;

	private String navigateSearch = HolidayConstants.NAVIGATE_SEARCH;

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
		checkNewOrEdit();
		checkSameValue = "";
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
		holiday = new Holiday();

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
		holiday = holidayService.findById(idLong);

	}

	public Boolean validate() {
		Boolean flag = false;

		try {

			if (actionMode.equals(Constants.ACTION_ADD)) {
				Holiday validateSameValue = holidayService.getHolidayBySameData(holiday.getHolidayId(),
						holiday.getHolidayName());

				if (validateSameValue != null && validateSameValue.getHolidayId() != null
						&& validateSameValue.getHolidayId() > 0) {
					if (validateSameValue.getEnabledFlag().equals(Constants.CONSTANT_NO)) {
						tempHoliday = holidayService.findById(validateSameValue.getHolidayId());

						checkSameValue = Constants.SAME_DATA_VALUE;
					} else {
						facesUtil.addErrMessage(facesUtil.retrieveMessage("formHolidayName") + " "
								+ facesUtil.retrieveMessage("errorAlreadyExists"));
						flag = true;
					}
				}
			} else if (actionMode.equals(Constants.ACTION_EDIT)) {
				Integer validateSameValue = holidayService.getEditHolidayByIdAndName(holiday.getHolidayId(),
						holiday.getHolidayName());

				if (validateSameValue > 0) {
					facesUtil.addErrMessage(facesUtil.retrieveMessage("formHolidayName") + " "
							+ facesUtil.retrieveMessage("errorAlreadyExists"));
					flag = true;
				}
			}

			if (StringUtils.isEmpty(holiday.getHolidayName())) {
				facesUtil.addErrMessage(facesUtil.retrieveMessage("formHolidayName") + " "
						+ facesUtil.retrieveMessage("validateRequired"));
				flag = true;
			} else if (holiday.getHolidayDateFrom() == null) {
				facesUtil.addErrMessage(facesUtil.retrieveMessage("formHolidayDate") + " "
						+ facesUtil.retrieveMessage("validateRequired"));
				flag = true;
			} else if (holiday.getHolidayDateTo() == null) {
				facesUtil.addErrMessage(facesUtil.retrieveMessage("formHolidayDate") + " "
						+ facesUtil.retrieveMessage("validateRequired"));
				flag = true;
			} else if (holiday.getHolidayDateFrom() != null && holiday.getHolidayDateTo() != null
					&& holiday.getHolidayDateFrom().after(holiday.getHolidayDateTo())) {
				facesUtil.addErrMessage(facesUtil.retrieveMessage("errorTenderStepDateOverlap"));
				flag = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			flag = true;
		}

		return flag;
	}

	public void save() {
		try {

			/*
			 * String dateFrom = facesUtil.retrieveRequestParam("DATE_FROM"); String dateTo
			 * = facesUtil.retrieveRequestParam("DATE_TO");
			 */

			// if(!validate(dateFrom,dateTo)){
			if (!validate()) {

				/*
				 * holiday.setHolidayDateFrom(sdf.parse(dateFrom));
				 * holiday.setHolidayDateTo(sdf.parse(dateTo));
				 */

				if (checkSameValue != null && checkSameValue.equals(Constants.SAME_DATA_VALUE)) {
					tempHoliday.setCreatedBy(facesUtil.retrieveUserLogin());
					tempHoliday.setCreationDate(new Timestamp(new Date().getTime()));
					tempHoliday.setDelId(new Long(0));
					tempHoliday.setEnabledFlag(Constants.CONSTANT_YES);
					holidayService.save(tempHoliday);
				} else {
					if (holiday.getHolidayId() != null) {

						holiday.setLastUpdateBy(facesUtil.retrieveUserLogin());
						holiday.setLastUpdateDate(new Timestamp(new Date().getTime()));
						holiday.setDelId(new Long(0));
						holiday.setEnabledFlag(Constants.CONSTANT_YES);
						holidayService.update(holiday);
					} else {
						holiday.setCreatedBy(facesUtil.retrieveUserLogin());
						holiday.setCreationDate(new Timestamp(new Date().getTime()));
						holiday.setDelId(new Long(0));
						holiday.setEnabledFlag(Constants.CONSTANT_YES);
						holidayService.save(holiday);
					}
				}
				facesUtil.redirect("/pages/holiday/holiday.faces");
			}

		} catch (Exception ex) {
			facesUtil.addFacesMsg(FacesMessage.SEVERITY_ERROR, null, "Operation Failed : " + ex.getMessage(), "");
		}
	}

	public void cancel() {
		try {
			facesUtil.redirect("/pages/holiday/holiday.faces");
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}

	public void handleDateFromSelect(SelectEvent event) {
		Date dateFrom = (Date) event.getObject();
		if (dateFrom != null) {
			holiday.setHolidayDateTo(dateFrom);
		}
	}

	public FacesUtil getFacesUtil() {
		return facesUtil;
	}

	public void setFacesUtil(FacesUtil facesUtil) {
		this.facesUtil = facesUtil;
	}

	public HolidayService getHolidayService() {
		return holidayService;
	}

	public void setHolidayService(HolidayService holidayService) {
		this.holidayService = holidayService;
	}

	public String getNavigateSearch() {
		return navigateSearch;
	}

	public void setNavigateSearch(String navigateSearch) {
		this.navigateSearch = navigateSearch;
	}

	public Holiday getHoliday() {
		return holiday;
	}

	public void setHoliday(Holiday holiday) {
		this.holiday = holiday;
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

	public List<SelectItem> getCategories() {
		return categories;
	}

	public void setCategories(List<SelectItem> categories) {
		this.categories = categories;
	}


	public Holiday getTempHoliday() {
		return tempHoliday;
	}

	public void setTempHoliday(Holiday tempHoliday) {
		this.tempHoliday = tempHoliday;
	}

	public String getCheckSameValue() {
		return checkSameValue;
	}

	public void setCheckSameValue(String checkSameValue) {
		this.checkSameValue = checkSameValue;
	}

}