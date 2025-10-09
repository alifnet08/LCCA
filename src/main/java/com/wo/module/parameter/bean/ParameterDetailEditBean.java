package com.wo.module.parameter.bean;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.Constants;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.lov.bean.SelectorListener;
import com.wo.module.parameter.constant.ParameterDetailConstant;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.parameter.model.ParameterHeader;
import com.wo.module.parameter.service.ParameterDetailService;
import com.wo.module.parameter.service.ParameterHeaderService;

public class ParameterDetailEditBean extends CommonBean implements SelectorListener<Object>, Serializable {

	private static final long serialVersionUID = 2813532416350359515L;
	static Logger logger = Logger.getLogger(ParameterDetailBean.class);

	private ParameterDetail parameterDetail;

	private Boolean isViewOnly;

	private String actionMode;

	private String editedId;

	private Long paramId;

	private Long parameterId;

	private String parameterCode;

	private List<SelectItem> parameterList;

	private ParameterHeaderService parameterHeaderService;

	public FacesUtil facesUtil;

	private String navigateSearch = ParameterDetailConstant.NAVIGATE_SEARCH;

	SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");

	private String localLanguange;

	@Override
	public void itemSelected(String clientId, String widgetVar, Object selectedItem) {
	}

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
		selectParameterList();
		checkNewOrEdit();
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
		parameterDetail = new ParameterDetail();
		ParameterHeader ph = new ParameterHeader();
		parameterDetail.setParameterHeader(ph);
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
		Long longId = Long.parseLong(editId);
		parameterDetail = parameterDetailService.findById(longId);
	}

	public Boolean isValidate() {
		Boolean flag = false;
		try {
			if (actionMode.equals(Constants.ACTION_ADD)) {
				Integer validateCode = parameterDetailService.getParameterDetailByParamCodeAndParamDtlCode(
						parameterDetail.getParameterHeader().getParameterCode(), parameterDetail.getParameterDtlCode());
				
				
				if (validateCode > 0) {
					facesUtil.addErrMessage(facesUtil.retrieveMessage("formParameter") +" & " 
											+ facesUtil.retrieveMessage("formParameterDetailCode") + " " 
											+facesUtil.retrieveMessage("errorAlreadyExists"));
					flag = true;
				}
				
			}

			if (StringUtils.isEmpty(parameterDetail.getParameterHeader().getParameterCode())) {
				facesUtil.addErrMessage(
						facesUtil.retrieveMessage("formParameter") + " " + facesUtil.retrieveMessage("validateRequired"));
				flag = true;
			} else if (StringUtils.isEmpty(parameterDetail.getParameterDtlCode())) {
				facesUtil.addErrMessage(facesUtil.retrieveMessage("formParameterDetailCode") + " "
						+ facesUtil.retrieveMessage("validateRequired"));
				flag = true;
			} else if (StringUtils.isEmpty(parameterDetail.getNameIn())) {
				facesUtil.addErrMessage(facesUtil.retrieveMessage("formParameterTitleIn") + " "
						+ facesUtil.retrieveMessage("validateRequired"));
				flag = true;
			} /*else if (StringUtils.isEmpty(parameterDetail.getNameEn())) {
				facesUtil.addErrMessage(facesUtil.retrieveMessage("formParameterTitleEn") + " "
						+ facesUtil.retrieveMessage("validateRequired"));
				flag = true;
			}*/
		} catch (Exception e) {
			facesUtil.addErrMessage(facesUtil.retrieveMessage("errorGeneralCheckInput") + ":" + e.getMessage());
			e.printStackTrace();
			flag = true;
		}
		return flag;
	}

	public void save() {
		try {
			if (!isValidate()) {
				if (parameterDetail.getParameterDtlId() != null) {
					ParameterHeader ph = parameterHeaderService
							.getParameterHeaderByParamCode(parameterDetail.getParameterHeader().getParameterCode());
					parameterDetail.setParameterHeader(ph);
					parameterDetail.setLastUpdateBy(facesUtil.retrieveUserLogin());
					parameterDetail.setLastUpdateDate(new Timestamp(new Date().getTime()));
					parameterDetail.setDelId(new Long(0));
					parameterDetail.setEnabledFlag(Constants.CONSTANT_YES);
					parameterDetailService.update(parameterDetail);
				} else {
					ParameterHeader ph = parameterHeaderService
							.getParameterHeaderByParamCode(parameterDetail.getParameterHeader().getParameterCode());
					parameterDetail.setParameterHeader(ph);
					parameterDetail.setCreatedBy(facesUtil.retrieveUserLogin());
					parameterDetail.setCreationDate(new Timestamp(new Date().getTime()));
					parameterDetail.setDelId(new Long(0));
					parameterDetail.setEnabledFlag(Constants.CONSTANT_YES);
					parameterDetailService.save(parameterDetail);
				}
				facesUtil.redirect("/pages/parameter/parameter.faces");
			}
		} catch (Exception e) {
			facesUtil.addFacesMsg(FacesMessage.SEVERITY_ERROR, null, "Operation Failed : " + e.getMessage(), "");
		}
	}

	@SuppressWarnings("static-access")
	public void selectParameterList() {
		parameterList = new ArrayList<SelectItem>();

		try {
			List<ParameterHeader> pd = parameterHeaderService.getListParameterAllOrderByName();
			for (int i = 0; i < pd.size(); i++) {
				SelectItem si = new SelectItem();
				/*Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
				if (locale != null && locale.equals(locale.ENGLISH)) {*/
					si.setLabel(((ParameterHeader) pd.get(i)).getNameIn());
				/*} else {
					si.setLabel(((ParameterHeader) pd.get(i)).getNameIn());
				}*/
//				si.setLabel(((ParameterHeader)pd.get(i)).getParameterCode());
				si.setValue(((ParameterHeader) pd.get(i)).getParameterCode());
				parameterList.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void cancel() {
		try {
			facesUtil.redirect("/pages/parameter/parameter.faces");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public ParameterDetail getParameterDetail() {
		return parameterDetail;
	}

	public void setParameterDetail(ParameterDetail parameterDetail) {
		this.parameterDetail = parameterDetail;
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

	public Long getParamId() {
		return paramId;
	}

	public void setParamId(Long paramId) {
		this.paramId = paramId;
	}

	public List<SelectItem> getParameterList() {
		return parameterList;
	}

	public void setParameterList(List<SelectItem> parameterList) {
		this.parameterList = parameterList;
	}

	public ParameterHeaderService getParameterHeaderService() {
		return parameterHeaderService;
	}

	public void setParameterHeaderService(ParameterHeaderService parameterHeaderService) {
		this.parameterHeaderService = parameterHeaderService;
	}

	public FacesUtil getFacesUtil() {
		return facesUtil;
	}

	public void setFacesUtil(FacesUtil facesUtil) {
		this.facesUtil = facesUtil;
	}

	public String getNavigateSearch() {
		return navigateSearch;
	}

	public void setNavigateSearch(String navigateSearch) {
		this.navigateSearch = navigateSearch;
	}

	public String getLocalLanguange() {
		return localLanguange;
	}

	public void setLocalLanguange(String localLanguange) {
		this.localLanguange = localLanguange;
	}


	public Long getParameterId() {
		return parameterId;
	}

	public void setParameterId(Long parameterId) {
		this.parameterId = parameterId;
	}

	public String getParameterCode() {
		return parameterCode;
	}

	public void setParameterCode(String parameterCode) {
		this.parameterCode = parameterCode;
	}
}