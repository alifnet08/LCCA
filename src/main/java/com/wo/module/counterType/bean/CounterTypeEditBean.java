package com.wo.module.counterType.bean;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.primefaces.PrimeFaces;

import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.Constants;
import com.wo.module.counterType.constant.CounterTypeConstants;
import com.wo.module.counterType.model.CounterType;
import com.wo.module.counterType.model.CounterTypeDtl;
import com.wo.module.counterType.model.CounterTypeDtlTableModel;
import com.wo.module.counterType.service.CounterTypeService;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.parameter.model.ParameterHeader;

public class CounterTypeEditBean extends CommonBean implements Serializable {

	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(CounterTypeEditBean.class);

	private CounterType counterType;

	private Boolean isViewOnly;

	private String actionMode;

	private String editedId;

	private CounterTypeDtl[] selectedData;

	private CounterTypeDtlTableModel<CounterTypeDtl> tableModel;

	private CounterTypeService counterTypeService;
	
	public FacesUtil facesUtil;
	
	private Integer lastSequenceOfDtl;

	private List<SelectItem> nameList;
	private List<SelectItem> slaTypeList;

	private boolean checkAll;

	private String navigateSearch = CounterTypeConstants.NAVIGATE_SEARCH;

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

			nameList = new ArrayList<SelectItem>();
			List<ParameterDetail> listParamDtl = parameterDetailService
					.getParameterDetailByParamCode(ParameterHeader.PARAM_HEAD_CODE_REMINDER_PIC);

			for (ParameterDetail vo : listParamDtl) {
				SelectItem si = new SelectItem();
				si.setLabel(vo.getName());
				si.setValue(vo.getParameterDtlCode());
				nameList.add(si);
			}

			slaTypeList = new ArrayList<SelectItem>();
			slaTypeList.add(new SelectItem(CounterTypeConstants.SIGN_MINUS, CounterTypeConstants.SIGN_MINUS));
			slaTypeList.add(new SelectItem(CounterTypeConstants.SIGN_PLUS, CounterTypeConstants.SIGN_PLUS));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public void onAddNew() {
		if (counterType.getDetails() == null) {
			counterType.setDetails(new ArrayList<CounterTypeDtl>());
			lastSequenceOfDtl = 0;
		} else {
			if(counterType.getDetails().size() == 0) {
				lastSequenceOfDtl = 0;
			}			
		}

		CounterTypeDtl dtl = new CounterTypeDtl();
		lastSequenceOfDtl = lastSequenceOfDtl + 1;
		dtl.setSequence(lastSequenceOfDtl);
		counterType.getDetails().add(dtl);

		tableModel.setWrappedData(counterType.getDetails());
		
		PrimeFaces.current().executeScript("reInitSelect2();");
//		RequestContext.getCurrentInstance().execute("reInitSelect2();");
		
	}

	public void onDeleteRow() {
		for (int i = 0; i < selectedData.length; i++) {
			counterType.getDetails().remove(selectedData[i]);
		}
		
		if (counterType.getDetails() == null
				|| counterType.getDetails().size() == 0) {
			lastSequenceOfDtl = 0;
			counterType.getDetails().clear();
		}


		tableModel.setWrappedData(counterType.getDetails());
		
		PrimeFaces.current().executeScript("reInitSelect2();");
//		RequestContext.getCurrentInstance().execute("reInitSelect2();");
	}
	
	private void checkNewOrEdit() {
		try {
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
		} catch (Exception e) {

		}

	}

	private void handleNew() {
		counterType = new CounterType();
		lastSequenceOfDtl = 0;
		
		if (counterType.getDetails() == null
				|| counterType.getDetails().size() == 0) {
			counterType.setDetails(new ArrayList<CounterTypeDtl>());
			lastSequenceOfDtl = 0;
		}

		CounterTypeDtl dtl = new CounterTypeDtl();
		lastSequenceOfDtl = lastSequenceOfDtl + 1;
		dtl.setSequence(lastSequenceOfDtl);
		counterType.getDetails().add(dtl);

		actionMode = Constants.ACTION_ADD;
		tableModel = new CounterTypeDtlTableModel<CounterTypeDtl>(counterType.getDetails());
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
		counterType = counterTypeService.findById(idLong);
		lastSequenceOfDtl = 0;
		
		if (counterType.getDetails() != null) {
			lastSequenceOfDtl = counterType.getDetails().size();
			for (int i = 0; i < counterType.getDetails().size(); i++) {
				CounterTypeDtl dtl = counterType.getDetails().get(i);
				lastSequenceOfDtl = lastSequenceOfDtl + 1;
				dtl.setSequence(lastSequenceOfDtl);
			}
		}
		
		tableModel = new CounterTypeDtlTableModel<CounterTypeDtl>(counterType.getDetails());
	}

	public Boolean validate() {
		Boolean flag = false;

		if (StringUtils.isEmpty(counterType.getCounterTypeIn())) {
			facesUtil.addErrMessage(facesUtil.retrieveMessage("formCounterTypeCounterTypeName") + " in "
					+ facesUtil.retrieveMessage("validateRequired"));
			flag = true;
		}

		/*if (StringUtils.isEmpty(counterType.getCounterTypeEn())) {
			facesUtil.addErrMessage(facesUtil.retrieveMessage("formCounterTypeCounterTypeName") + " en "
					+ facesUtil.retrieveMessage("validateRequired"));
			flag = true;
		}*/

		if (counterTypeService.isDataDuplicate(counterType)) {
			facesUtil.addErrMessage(facesUtil.retrieveMessage("errorAlreadyExists"));
			flag = true;
		}
		
		if (counterType.getDetails() == null || counterType.getDetails().size() == 0) {
			facesUtil.addErrMessage("Detail" + " "
					+ facesUtil.retrieveMessage("validateRequired") + " min. 1 data");
			flag = true;
		}

		if (counterType.getDetails() != null && counterType.getDetails().size() > 0) {
			Set<String> setTemp = new HashSet<String>();
			
			for (int i = 0; i < counterType.getDetails().size(); i++) {
				CounterTypeDtl dtl = (CounterTypeDtl) counterType.getDetails().get(i);
				if (StringUtils.isEmpty(dtl.getSlaType()) || (dtl.getSla() == null)) {
					facesUtil.addErrMessage(facesUtil.retrieveMessage("formCounterTypeSla") + " "
							+ facesUtil.retrieveMessage("validateRequired"));
					flag = true;
				}
				
				if (StringUtils.isEmpty(dtl.getEmailTo()) || (dtl.getEmailTo() == null)) {
					facesUtil.addErrMessage("Email To" + " "
							+ facesUtil.retrieveMessage("validateRequired"));
					flag = true;
				}
				
				if(dtl.getSla() != null && dtl.getSlaType() != null 
						&& !StringUtils.isEmpty(dtl.getSlaType())) {
					String slaTemp = dtl.getSlaType() + dtl.getSla();
					if(!setTemp.add(slaTemp)) {
						facesUtil.addErrMessage(facesUtil.retrieveMessage("errJavascriptDuplicateData"));
						flag = true;
					}
					
					/*
					for (int j = i + 1; j < counterType.getDetails().size(); j++) {
						CounterTypeDtl dtl2 = (CounterTypeDtl) counterType.getDetails().get(j);
						if(dtl.getSla() != null && dtl2.getSla() != null)
							if(dtl.getSla().equals(dtl2.getSla())) {
							if(dtl.getSlaType().equals(dtl2.getSlaType())) {
								facesUtil.addErrMessage(facesUtil.retrieveMessage("errJavascriptDuplicateData"));
								flag = true;				
								break;
							}
						}						
					}
					*/
				}

			}
		} 

		return flag;
	}

	public void save() {
		try {
			if (!validate()) {
				if (counterType.getDetails() != null) {
					for (int i = 0; i < counterType.getDetails().size(); i++) {
						CounterTypeDtl dtl = (CounterTypeDtl) counterType.getDetails().get(i);
						dtl.setCounterType(counterType);
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

				if (counterType.getCounterTypeId() != null) {
					counterType.setLastUpdateBy(facesUtil.retrieveUserLogin());
					counterType.setLastUpdateDate(new Timestamp(new Date().getTime()));
					counterType.setDelId(new Long(0));
					counterType.setEnabledFlag(Constants.CONSTANT_YES);
					counterTypeService.update(counterType);

				} else {
					counterType.setCreatedBy(facesUtil.retrieveUserLogin());
					counterType.setCreationDate(new Timestamp(new Date().getTime()));
					counterType.setDelId(new Long(0));
					counterType.setEnabledFlag(Constants.CONSTANT_YES);
					counterTypeService.save(counterType);
				}

				facesUtil.redirect("/pages/counterType/counterType.faces");
			}

		} catch (Exception ex) {
			facesUtil.addFacesMsg(FacesMessage.SEVERITY_ERROR, null, "Operation Failed : " + ex.getMessage(), "");
		}

	}

	public void cancel() {
		try {
			facesUtil.redirect("/pages/counterType/counterType.faces");
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

	public CounterTypeService getCounterTypeService() {
		return counterTypeService;
	}

	public void setCounterTypeService(CounterTypeService counterTypeService) {
		this.counterTypeService = counterTypeService;
	}

	public String getNavigateSearch() {
		return navigateSearch;
	}

	public void setNavigateSearch(String navigateSearch) {
		this.navigateSearch = navigateSearch;
	}

	public CounterType getCounterType() {
		return counterType;
	}

	public void setCounterType(CounterType counterType) {
		this.counterType = counterType;
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

	public List<SelectItem> getNameList() {
		return nameList;
	}

	public void setNameList(List<SelectItem> nameList) {
		this.nameList = nameList;
	}

	public List<SelectItem> getSlaTypeList() {
		return slaTypeList;
	}

	public void setSlaTypeList(List<SelectItem> slaTypeList) {
		this.slaTypeList = slaTypeList;
	}

	public boolean isCheckAll() {
		return checkAll;
	}

	public void setCheckAll(boolean checkAll) {
		this.checkAll = checkAll;
	}

	public CounterTypeDtl[] getSelectedData() {
		return selectedData;
	}

	public void setSelectedData(CounterTypeDtl[] selectedData) {
		this.selectedData = selectedData;
	}

	public CounterTypeDtlTableModel<CounterTypeDtl> getTableModel() {
		return tableModel;
	}

	public void setTableModel(CounterTypeDtlTableModel<CounterTypeDtl> tableModel) {
		this.tableModel = tableModel;
	}

	public Integer getLastSequenceOfDtl() {
		return lastSequenceOfDtl;
	}

	public void setLastSequenceOfDtl(Integer lastSequenceOfDtl) {
		this.lastSequenceOfDtl = lastSequenceOfDtl;
	}

}