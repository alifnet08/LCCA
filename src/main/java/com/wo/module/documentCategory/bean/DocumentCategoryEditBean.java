package com.wo.module.documentCategory.bean;

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
import com.wo.module.documentCategory.constant.DocumentCategoryConstants;
import com.wo.module.documentCategory.model.DocumentCategory;
import com.wo.module.documentCategory.service.DocumentCategoryService;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.parameter.model.ParameterHeader;

public class DocumentCategoryEditBean extends CommonBean implements Serializable {

	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(DocumentCategoryBean.class);

	private DocumentCategory documentCategory;

	private DocumentCategory dcSameValue;

	private String checkSameValue;

	private Boolean isViewOnly;

	private String actionMode;

	private String editedId;

	private List<SelectItem> provTypes;

	private List<SelectItem> categories;

	private DocumentCategoryService documentCategoryService;

	public FacesUtil facesUtil;

	private String navigateSearch = DocumentCategoryConstants.NAVIGATE_SEARCH;

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
		selectProvType();
		checkNewOrEdit();
		checkSameValue = "";
	}

	public void selectProvType() {
		provTypes = new ArrayList<SelectItem>();
		try {
			List<ParameterDetail> pd = parameterDetailService.getParameterDetailByParamCode(ParameterHeader.PARAM_HEAD_CODE_JENIS_KETENTUAN);
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
		documentCategory = new DocumentCategory();
		ParameterDetail pd = new ParameterDetail();
		documentCategory.setParameterDetail(pd);
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
		documentCategory = documentCategoryService.findById(idLong);
	}

	public Boolean validate() {
		Boolean flag = false;

		try {
			if (actionMode.equals(Constants.ACTION_ADD)) {
//				Integer validateSameValue = documentCategoryService.getDocumentCategoryByProvAndCategory(
//						documentCategory.getParameterDetail().getParameterDtlCode(), documentCategory.getDocumentCategoryIn());

				DocumentCategory validateSameValue = documentCategoryService.getDocumentCategoryBySameValue(
						documentCategory.getParameterDetail().getParameterDtlCode(),
						documentCategory.getDocumentCategoryIn());

				if (validateSameValue != null && validateSameValue.getDocumentCategoryId() != null
						&& validateSameValue.getDocumentCategoryId() > 0) {
					if (validateSameValue.getEnabledFlag().equals(Constants.CONSTANT_NO)) {
						dcSameValue = documentCategoryService.findById(validateSameValue.getDocumentCategoryId());

						checkSameValue = Constants.SAME_DATA_VALUE;
					} else {
						facesUtil.addErrMessage(facesUtil.retrieveMessage("formDocumentTypeProvType") + " & "
								+ facesUtil.retrieveMessage("formDocumentCategoryRegulation") /*+ " "
								+ facesUtil.retrieveMessage("indonesia")*/ + " "
								+ facesUtil.retrieveMessage("errorAlreadyExists"));
						flag = true;
					}
				}
			} else if (actionMode.equals(Constants.ACTION_EDIT)) {
				Integer validateSameValue = documentCategoryService.getEditDocumentCategoryByIdProvAndCategory(
						documentCategory.getDocumentCategoryId(),
						documentCategory.getParameterDetail().getParameterDtlCode(),
						documentCategory.getDocumentCategoryIn());

				if (validateSameValue > 0) {
					facesUtil.addErrMessage(facesUtil.retrieveMessage("formDocumentTypeProvType") + " & "
							+ facesUtil.retrieveMessage("formDocumentCategoryRegulation") + /*" "
							+ facesUtil.retrieveMessage("indonesia") +*/ " "
							+ facesUtil.retrieveMessage("errorAlreadyExists"));
					flag = true;
				}
			}

			if (StringUtils.isEmpty(documentCategory.getParameterDetail().getParameterDtlCode())) {
				facesUtil.addErrMessage(facesUtil.retrieveMessage("formDocumentTypeProvType") + " "
						+ facesUtil.retrieveMessage("validateRequired"));
				flag = true;
			} /*else if (StringUtils.isEmpty(documentCategory.getDocumentCategoryEn())) {
				facesUtil.addErrMessage(facesUtil.retrieveMessage("formDocumentCategoryTitle") + " en "
						+ facesUtil.retrieveMessage("validateRequired"));
				flag = true;
			}*/ else if (StringUtils.isEmpty(documentCategory.getDocumentCategoryIn())) {
				facesUtil.addErrMessage(facesUtil.retrieveMessage("formDocumentCategoryRegulation") + /*" in "*/ " "
						+ facesUtil.retrieveMessage("validateRequired"));
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
			if (!validate()) {
				if (checkSameValue != null && checkSameValue.equals(Constants.SAME_DATA_VALUE)) {				
					dcSameValue.setDocumentCategoryEn(documentCategory.getDocumentCategoryEn());
					dcSameValue.setLastUpdateBy(facesUtil.retrieveUserLogin());
					dcSameValue.setLastUpdateDate(new Timestamp(new Date().getTime()));
					dcSameValue.setDelId(new Long(0));
					dcSameValue.setEnabledFlag(Constants.CONSTANT_YES);
					documentCategoryService.update(dcSameValue);
				} else {
					if (documentCategory.getDocumentCategoryId() != null) {
						ParameterDetail pd = parameterDetailService.getParameterDetailByParamDtlCode(
								documentCategory.getParameterDetail().getParameterDtlCode());
						documentCategory.setParameterDetail(pd);
						documentCategory.setLastUpdateBy(facesUtil.retrieveUserLogin());
						documentCategory.setLastUpdateDate(new Timestamp(new Date().getTime()));
						documentCategory.setDelId(new Long(0));
						documentCategory.setEnabledFlag(Constants.CONSTANT_YES);
						documentCategoryService.update(documentCategory);
					} else {
						ParameterDetail pd = parameterDetailService.getParameterDetailByParamDtlCode(
								documentCategory.getParameterDetail().getParameterDtlCode());
						documentCategory.setParameterDetail(pd);
						documentCategory.setCreatedBy(facesUtil.retrieveUserLogin());
						documentCategory.setCreationDate(new Timestamp(new Date().getTime()));
						documentCategory.setDelId(new Long(0));
						documentCategory.setEnabledFlag(Constants.CONSTANT_YES);
						documentCategoryService.save(documentCategory);
					}
				}

				facesUtil.redirect("/pages/documentCategory/documentCategory.faces");
			}

		} catch (Exception ex) {
			facesUtil.addFacesMsg(FacesMessage.SEVERITY_ERROR, null, "Operation Failed : " + ex.getMessage(), "");
		}

	}

	public void cancel() {
		try {
			facesUtil.redirect("/pages/documentCategory/documentCategory.faces");
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

	public DocumentCategoryService getDocumentCategoryService() {
		return documentCategoryService;
	}

	public void setDocumentCategoryService(DocumentCategoryService documentCategoryService) {
		this.documentCategoryService = documentCategoryService;
	}

	public String getNavigateSearch() {
		return navigateSearch;
	}

	public void setNavigateSearch(String navigateSearch) {
		this.navigateSearch = navigateSearch;
	}

	public DocumentCategory getDocumentCategory() {
		return documentCategory;
	}

	public void setDocumentCategory(DocumentCategory documentCategory) {
		this.documentCategory = documentCategory;
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

	public DocumentCategory getDcSameValue() {
		return dcSameValue;
	}

	public void setDcSameValue(DocumentCategory dcSameValue) {
		this.dcSameValue = dcSameValue;
	}

	public String getCheckSameValue() {
		return checkSameValue;
	}

	public void setCheckSameValue(String checkSameValue) {
		this.checkSameValue = checkSameValue;
	}

}