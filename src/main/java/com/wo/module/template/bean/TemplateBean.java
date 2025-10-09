package com.wo.module.template.bean;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.apache.log4j.Logger;

import com.wo.module.template.constant.TemplateConstants;
import com.wo.module.template.model.Template;
import com.wo.module.template.service.TemplateService;
import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.paging.DBLazyDataModel;
import com.wo.module.common.paging.DefaultSearchObject;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.parameter.model.ParameterHeader;

public class TemplateBean extends CommonBean  implements Serializable {

	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(TemplateBean.class);

	private String category;
	
	private String subCategory;
	
	private String subject;
	
	private String note;
	
	private Date date;
	
	private int paging;

	private TemplateService templateService;
	
	private List<Template> templateList;

	private DBLazyDataModel<Template> tableModel;
	
	private List<SelectItem> categoryList;
	
	private List<SelectItem> statusList;

	public FacesUtil facesUtil;

	private String navigateEdit = TemplateConstants.NAVIGATE_EDIT;

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
		paging = Constants.DEFAULT_PAGING_NUMBER;
		tableModel = new DBLazyDataModel<Template>(templateService, paging);
	}
	
	public void initList(){
		try {
		categoryList = new ArrayList<SelectItem>();
		List<ParameterDetail> listCategory = parameterDetailService
					.getParameterDetailByParamCode(ParameterHeader.PARAM_HEAD_TEMPLATE_CATEGORY);
		

		for (ParameterDetail vo : listCategory) {
			SelectItem si = new SelectItem();
			si.setLabel(vo.getName());
			si.setValue(vo.getParameterDtlCode());
			categoryList.add(si);
		}
		
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@SuppressWarnings("rawtypes")
	public void search(ActionEvent actionEvent) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<SearchObject> searchCriteria = new ArrayList<SearchObject>();
		if (category != null && !category.isEmpty()) {
			searchCriteria.add(new DefaultSearchObject(TemplateConstants.SEARCH_BY_CATEGORY, category));
		}
		if (subCategory != null && !subCategory.isEmpty()) {
			searchCriteria.add(new DefaultSearchObject(TemplateConstants.SEARCH_BY_CATEGORY, subCategory));
		}
		if (note != null && !note.isEmpty()) {
			searchCriteria.add(new DefaultSearchObject(TemplateConstants.SEARCH_BY_INFORMATION, note));
		}
		if (subject != null && !subject.isEmpty()) {
			searchCriteria.add(new DefaultSearchObject(TemplateConstants.SEARCH_BY_SUBJECT, subject));
		}
		if (date != null) {
			searchCriteria.add(new DefaultSearchObject(TemplateConstants.SEARCH_BY_DATE, sdf.format(date)));
		}
		

		tableModel.setSearchCriteria(searchCriteria);
		
	}

	public void reset(ActionEvent actionEvent) {
		category = "";
		subCategory = null;
		date = null;
		subject = null;
		note = null;
		
		search(actionEvent);
	}

	public void delete(Long deleteId) {
		try {			
			
				Template entity = templateService.findById(deleteId);
				entity.setEnabledFlag(Constants.CONSTANT_NO);
				entity.setLastUpdateBy(facesUtil.retrieveUserLogin());
				entity.setLastUpdateDate(new Timestamp(new Date().getTime()));
				
				templateService.update(entity);
				facesUtil.addSuccessMsg(facesUtil.retrieveMessage("deleteSuccess"));
			
			
		} catch (Exception e) {
			facesUtil.addFacesMsg(FacesMessage.SEVERITY_ERROR, null, "Operation Failed : " + e.getMessage(), "");
		}
	}

	public FacesUtil getFacesUtil() {
		return facesUtil;
	}

	public void setFacesUtil(FacesUtil facesUtil) {
		this.facesUtil = facesUtil;
	}

	public Boolean getIsLogin() {
		if (facesUtil.getSessionAttribute(Constants.SESSION_EMPLOYEE) != null
				|| facesUtil.getSessionAttribute(Constants.SESSION_KANDIDAT) != null)
			return true;
		else
			return false;
	}

	

	public TemplateService getTemplateService() {
		return templateService;
	}

	public void setTemplateService(TemplateService templateService) {
		this.templateService = templateService;
	}

	public DBLazyDataModel<Template> getTableModel() {
		return tableModel;
	}

	public void setTableModel(DBLazyDataModel<Template> tableModel) {
		this.tableModel = tableModel;
	}

	public int getPaging() {
		return paging;
	}

	public void setPaging(int paging) {
		this.paging = paging;
	}


	public String getNavigateEdit() {
		return navigateEdit;
	}

	public void setNavigateEdit(String navigateEdit) {
		this.navigateEdit = navigateEdit;
	}

	public List<Template> getTemplateList() {
		return templateList;
	}

	public void setTemplateList(List<Template> templateList) {
		this.templateList = templateList;
	}

	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		TemplateBean.logger = logger;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public List<SelectItem> getCategoryList() {
		return categoryList;
	}

	public void setCategoryList(List<SelectItem> categoryList) {
		this.categoryList = categoryList;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}


	public List<SelectItem> getStatusList() {
		return statusList;
	}

	public void setStatusList(List<SelectItem> statusList) {
		this.statusList = statusList;
	}

	public String getSubCategory() {
		return subCategory;
	}

	public void setSubCategory(String subCategory) {
		this.subCategory = subCategory;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	

	

	

	
	

}