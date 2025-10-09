package com.wo.module.qaCategory.bean;

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

import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.util.MathUtil;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.lov.bean.SelectorListener;
import com.wo.module.lov.bean.SelectorModel.SelectorInfo;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.parameter.model.ParameterHeader;
import com.wo.module.parameter.service.ParameterDetailService;
import com.wo.module.qa.model.QA;
import com.wo.module.qa.service.QAService;
import com.wo.module.qaCategory.constant.QACategoryConstants;
import com.wo.module.qaCategory.model.QACategory;
import com.wo.module.qaCategory.model.QACategoryAssignTableModel;
import com.wo.module.qaCategory.service.QACategoryService;
import com.wo.module.user.model.User;
import com.wo.module.user.service.UserService;

public class QACategoryEditBean extends CommonBean implements Serializable, SelectorListener<Object> {

	private static final long serialVersionUID = 5790732680092896182L;

	private QACategory qaCategory;
	private QA qa;

	private String actionMode;
	private String editedId;

	private QACategoryAssignTableModel<QACategory> assignedTableModel;
	private QACategory[] selectedAssigned;
	private List<QACategory> assignedList;
	private List<QACategory> deletedAssignedList;

	private QAService qaService;
	private QACategoryService qaCategoryService;
	private QACategoryService qaCategoryService2;
	private QACategoryService qaCategoryService3;
	private UserService userService;

	private List<SelectItem> categoryList;

	private FacesUtil facesUtil;

	private Integer indexDtlAssigned;

	private SelectorInfo selectorAssigned;
	private Integer lastSequenceOfAssigned;

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
		addCategoryList();
		selectorAssigned = QACategoryConstants.buildSelectorPICAssigned(facesUtil);

		try {
			assignedTableModel = new QACategoryAssignTableModel<QACategory>(assignedList);
			deletedAssignedList = new ArrayList<QACategory>();
			
			checkNewOrEdit();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void checkNewOrEdit() throws Exception {
		String categoryCode = facesUtil.retrieveRequestParam("categoryCode");
		String token = facesUtil.retrieveRequestParam("token");

		if (StringUtils.isBlank(categoryCode) && StringUtils.isBlank(token)) {
			this.handleNew();
		} else {
			this.handleEdit(categoryCode);
		}
	}

	private void handleNew() {
		actionMode = Constants.ACTION_ADD;
		qaCategory = new QACategory();
		lastSequenceOfAssigned = 0;

		// Add one new car to the table:
		if (assignedList == null || assignedList.size() == 0) {
			assignedList = new ArrayList<QACategory>();
			lastSequenceOfAssigned = 0;
		}

		ParameterDetail pd = new ParameterDetail();

		qaCategory.setQnaCategoryCode(pd);

		QACategory qc = new QACategory();
		lastSequenceOfAssigned = lastSequenceOfAssigned + 1;
		qc.setSequence(lastSequenceOfAssigned);
		qc.setUser(new User());
		qc.setQnaCategoryCode(new ParameterDetail());
		
		assignedList.add(qc);
		assignedTableModel.setWrappedData(assignedList);
		
		facesUtil.setSessionAttribute("token", null);
	}

	private void handleEdit(String categoryCode) throws Exception {
		String token = facesUtil.retrieveRequestParam("token");
		if (StringUtils.isNotEmpty(token)) {
			categoryCode = Constants.decryptString(token);
		}
		facesUtil.setSessionAttribute("token", Constants.encryptString(categoryCode));
		actionMode = Constants.ACTION_EDIT;
		
		lastSequenceOfAssigned = 0;
		
		qaCategory = new QACategory();
		assignedList = qaCategoryService.searchCategoryListByCode(categoryCode);
		if (assignedList != null) {
			lastSequenceOfAssigned = assignedList.size();
			for (QACategory assgn : assignedList) {
				lastSequenceOfAssigned = lastSequenceOfAssigned + 1;
				assgn.setSequence(lastSequenceOfAssigned);
			}
		}
		assignedTableModel.setWrappedData(assignedList);
		
		ParameterDetail pd = parameterDetailService.getParameterDetailByParamDtlCode(categoryCode);
		qaCategory.setQnaCategoryCode(pd);
	}

	public void addCategoryList() {
		categoryList = new ArrayList<SelectItem>();

		try {
			List<ParameterDetail> pd = parameterDetailService
					.getParameterDetailByParamCode(ParameterHeader.PARAM_QNA_CATEGORY);
			for (ParameterDetail param : pd) {
				SelectItem si = new SelectItem();
				si.setLabel(param.getName());
				si.setValue(param.getParameterDtlCode());
				categoryList.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void onAddNewAssigned() {
		// Add one new car to the table:
		if (assignedList == null) {
			assignedList = new ArrayList<QACategory>();
			lastSequenceOfAssigned = 0;

		} else {
			if (assignedList.size() == 0) {
				lastSequenceOfAssigned = 0;
			}
		}

		QACategory qc = new QACategory();
		lastSequenceOfAssigned = lastSequenceOfAssigned + 1;
		qc.setSequence(lastSequenceOfAssigned);
		qc.setUser(new User());
		qc.setQnaCategoryCode(new ParameterDetail());
		
		assignedList.add(qc);
		assignedTableModel.setWrappedData(assignedList);
	}

	public void onDeleteRowAssigned() {
		for (int i = 0; i < selectedAssigned.length; i++) {
			if (selectedAssigned[i].getQnaCategoryMapId() != null &&
					selectedAssigned[i].getQnaCategoryMapId() > 0) {
				deletedAssignedList.add(selectedAssigned[i]);
			}
			
			assignedList.remove(selectedAssigned[i]);
		}

		if (assignedList == null || assignedList.size() == 0) {
			lastSequenceOfAssigned = 0;
		}

		assignedTableModel.setWrappedData(assignedList);
	}

	public boolean validate() {
		boolean valid = true;

		try {
			if (qaCategory.getQnaCategoryCode().getParameterDtlCode() == null
					|| StringUtils.isEmpty(qaCategory.getQnaCategoryCode().getParameterDtlCode())) {
				facesUtil.addErrMessage(facesUtil.retrieveMessage("formQACategoryCategory") + " "
						+ facesUtil.retrieveMessage("validateRequired"));
				valid = false;
			}

			if (assignedList.size() == 0 && assignedList == null) {
				facesUtil.addErrMessage(facesUtil.retrieveMessage("formQACategoryAssignedTo") + " "
						+ facesUtil.retrieveMessage("validateRequired"));
				valid = false;
			} else if (assignedList.size() > 0 && assignedList != null) {
				for (int i = 0; i < assignedList.size(); i++) {
					QACategory as = assignedList.get(i);
					
					if (as.getUser().getUserId() == null || as.getUser().getUserId() < 0) {
						facesUtil.addErrMessage(facesUtil.retrieveMessage("formQACategoryAssignedTo") + " "
								+ facesUtil.retrieveMessage("validateRequired"));
						valid = false;
					} else if (as.getUser().getUserId() > 0 && as.getQnaCategoryMapId() == null &&
							 	!StringUtils.isEmpty(qaCategory.getQnaCategoryCode().getParameterDtlCode())) {
						
						Integer duplicate = qaCategoryService.duplicate(qaCategory.getQnaCategoryCode().getParameterDtlCode(), 
											as.getUser().getUserId());
						
						if (duplicate > 0) {
							facesUtil.addErrMessage(facesUtil.retrieveMessage("formQACategoryAssignedTo") + " "
									+ as.getUser().getName() + " " + as.getUser().getNik() + " " 
									+ facesUtil.retrieveMessage("errorDuplicate"));
							valid = false;
						}
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return valid;
	}

	public void save() {
		if (validate()) {
			try {
				for (QACategory qat : assignedList) {
					if (qat.getQnaCategoryMapId() != null && qat.getQnaCategoryMapId() > 0) {

						qat.setLastUpdateBy(facesUtil.retrieveUserLogin());
						qat.setLastUpdateDate(new Timestamp(new Date().getTime()));
						qat.setDelId(new Long(0));
						qat.setEnabledFlag(Constants.CONSTANT_YES);

						qaCategoryService3.edit(qat);

					} else {
						QACategory newCat = new QACategory();

						if (!StringUtils.isEmpty(qaCategory.getQnaCategoryCode().getParameterDtlCode())) {
							ParameterDetail pd = parameterDetailService.getParameterDetailByParamDtlCode(
									qaCategory.getQnaCategoryCode().getParameterDtlCode());

							newCat.setQnaCategoryCode(pd);
						}

						newCat.setCreatedBy(facesUtil.retrieveUserLogin());
						newCat.setCreationDate(new Timestamp(new Date().getTime()));
						newCat.setDelId(new Long(0));
						newCat.setEnabledFlag(Constants.CONSTANT_YES);
						newCat.setUser(qat.getUser());

						qaCategoryService.save(newCat);
					}
				}

				if (deletedAssignedList.size() > 0) {
					for (int i = 0; i < deletedAssignedList.size(); i++) {
						qaCategoryService2.delete(deletedAssignedList.get(i));
					}
				}
				
				facesUtil.redirect("/pages/qaCategory/qaCategory.faces");

			} catch (Exception ex) {
				facesUtil.addFacesMsg(FacesMessage.SEVERITY_ERROR, null, "Operation Failed : " + ex.getMessage(), "");
				ex.printStackTrace();
			}
		}
	}

	public void cancel() {
		try {
			facesUtil.redirect("/pages/qaCategory/qaCategory.faces");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public QACategory getQaCategory() {
		return qaCategory;
	}

	public void setQaCategory(QACategory qaCategory) {
		this.qaCategory = qaCategory;
	}

	public QA getQa() {
		return qa;
	}

	public void setQa(QA qa) {
		this.qa = qa;
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

	public QAService getQaService() {
		return qaService;
	}

	public void setQaService(QAService qaService) {
		this.qaService = qaService;
	}

	public QACategoryService getQaCategoryService() {
		return qaCategoryService;
	}

	public void setQaCategoryService(QACategoryService qaCategoryService) {
		this.qaCategoryService = qaCategoryService;
	}

	public List<SelectItem> getCategoryList() {
		return categoryList;
	}

	public void setCategoryList(List<SelectItem> categoryList) {
		this.categoryList = categoryList;
	}

	public FacesUtil getFacesUtil() {
		return facesUtil;
	}

	public void setFacesUtil(FacesUtil facesUtil) {
		this.facesUtil = facesUtil;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Integer getIndexDtlAssigned() {
		return indexDtlAssigned;
	}

	public void setIndexDtlAssigned(Integer indexDtlAssigned) {
		this.indexDtlAssigned = indexDtlAssigned;
	}

	public SelectorInfo getSelectorAssigned() {
		return selectorAssigned;
	}

	public void setSelectorAssigned(SelectorInfo selectorAssigned) {
		this.selectorAssigned = selectorAssigned;
	}

	public QACategoryAssignTableModel<QACategory> getAssignedTableModel() {
		return assignedTableModel;
	}

	public void setAssignedTableModel(QACategoryAssignTableModel<QACategory> assignedTableModel) {
		this.assignedTableModel = assignedTableModel;
	}

	public QACategory[] getSelectedAssigned() {
		return selectedAssigned;
	}

	public void setSelectedAssigned(QACategory[] selectedAssigned) {
		this.selectedAssigned = selectedAssigned;
	}

	public List<QACategory> getAssignedList() {
		return assignedList;
	}

	public void setAssignedList(List<QACategory> assignedList) {
		this.assignedList = assignedList;
	}

	public Integer getLastSequenceOfAssigned() {
		return lastSequenceOfAssigned;
	}

	public void setLastSequenceOfAssigned(Integer lastSequenceOfAssigned) {
		this.lastSequenceOfAssigned = lastSequenceOfAssigned;
	}

	@Override
	public void itemSelected(String clientId, String widgetVar, Object selectedItem) {
		if (StringUtils.equals("personAssignedDialog", widgetVar)) {
			Object[] objects = (Object[]) selectedItem;
			// User user = userService.findById(((java.math.BigInteger)
			// objects[0]).longValue());
			User us = userService.findById(MathUtil.returnIdObjectToLong(objects[0]));
			assignedList.get(indexDtlAssigned).setUser(us);
			assignedTableModel.setWrappedData(assignedList);
		}
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public List<QACategory> getDeletedAssignedList() {
		return deletedAssignedList;
	}

	public void setDeletedAssignedList(List<QACategory> deletedAssignedList) {
		this.deletedAssignedList = deletedAssignedList;
	}

	public QACategoryService getQaCategoryService2() {
		return qaCategoryService2;
	}

	public void setQaCategoryService2(QACategoryService qaCategoryService2) {
		this.qaCategoryService2 = qaCategoryService2;
	}

	public QACategoryService getQaCategoryService3() {
		return qaCategoryService3;
	}

	public void setQaCategoryService3(QACategoryService qaCategoryService3) {
		this.qaCategoryService3 = qaCategoryService3;
	}
}