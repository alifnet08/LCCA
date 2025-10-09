package com.wo.module.documentTopic.bean;

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
import org.primefaces.PrimeFaces;

import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.Constants;
import com.wo.module.documentCategory.model.DocumentCategory;
import com.wo.module.documentCategory.service.DocumentCategoryService;
import com.wo.module.documentTopic.constant.DocumentTopicConstants;
import com.wo.module.documentTopic.model.DocumentTopic;
import com.wo.module.documentTopic.service.DocumentTopicService;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.parameter.model.ParameterHeader;

public class DocumentTopicEditBean extends CommonBean implements Serializable {
 
	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(DocumentTopicBean.class);
	
	private DocumentTopic documentTopic;
	
	private Boolean isViewOnly;
	
	private String actionMode;
	
	private String editedId;
	
	private List<SelectItem> provTypes;
	
	private List<SelectItem> categories;
	
	private DocumentTopicService documentTopicService;
	
	
	
	private DocumentCategoryService documentCategoryService;
	
	public FacesUtil facesUtil;
	
	private String navigateSearch = DocumentTopicConstants.NAVIGATE_SEARCH;
    
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
//    	selectCategory();
    	checkNewOrEdit();
	}
    
    public void onChangeProvType() {
    	categories = new ArrayList<SelectItem>();
    	try {
		if(documentTopic.getParameterDetail().getParameterDtlCode() != null) {
			List<DocumentCategory> dc = documentCategoryService.getDocumentCategoryByJenisKetentuan(documentTopic.getParameterDetail().getParameterDtlCode());
				for(int i=0;i<dc.size();i++) {
					SelectItem si = new SelectItem();
					si.setLabel(((DocumentCategory)dc.get(i)).getDocumentCategory());
					si.setValue(((DocumentCategory)dc.get(i)).getDocumentCategoryId());
					categories.add(si);
				}
			}
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    	
    	PrimeFaces.current().executeScript("reInitSelect2();");
	}
    
	public void selectProvType() {
    	provTypes = new ArrayList<SelectItem>();
    	try {
			List<ParameterDetail> pd = parameterDetailService.getParameterDetailByParamCode(ParameterHeader.PARAM_HEAD_CODE_JENIS_KETENTUAN);
			for(int i=0;i<pd.size();i++) {
				SelectItem si = new SelectItem();
				si.setLabel(((ParameterDetail)pd.get(i)).getName());
				si.setValue(((ParameterDetail)pd.get(i)).getParameterDtlCode());
				provTypes.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	PrimeFaces.current().executeScript("reInitSelect2();");
//    	RequestContext.getCurrentInstance().execute("reInitSelect2();");
    }
    
    public void selectCategory() {
    	categories = new ArrayList<SelectItem>();
    	try {
			List<DocumentCategory> dc = documentCategoryService.searchData(null, 0, Integer.MAX_VALUE, null, null);
			for(int i=0;i<dc.size();i++) {
				SelectItem si = new SelectItem();
				si.setLabel(((DocumentCategory)dc.get(i)).getDocumentCategory());
				si.setValue(((DocumentCategory)dc.get(i)).getDocumentCategoryId());
				categories.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	PrimeFaces.current().executeScript("reInitSelect2();");
//    	RequestContext.getCurrentInstance().execute("reInitSelect2();");
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
    	documentTopic = new DocumentTopic();
    	ParameterDetail pd = new ParameterDetail();
    	documentTopic.setParameterDetail(pd);
//    	DocumentCategory dc = new DocumentCategory();
//    	documentTopic.setDocumentCategory(dc);
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
		documentTopic = documentTopicService.findById(idLong);
    }
    public Boolean validate(){
		Boolean flag = false;
		try {
			if(actionMode.equals(Constants.ACTION_ADD)) {
				Integer validateSameValue = documentTopicService.getDocumentTopicByProvCategoryAndTopik(
						documentTopic.getParameterDetail().getParameterDtlCode()
						, ((documentTopic.getDocumentCategory() != null)? documentTopic.getDocumentCategory().getDocumentCategoryId() : null)
						, documentTopic.getDocumentTopicIn());
				
				if(validateSameValue > 0) {
					facesUtil.addErrMessage(facesUtil.retrieveMessage("formDocumentTopicProvTopic") + " & "
											//+ facesUtil.retrieveMessage("formDocumentTopicDocTopicCat") + " & "
											+ facesUtil.retrieveMessage("formDocumentTopicReg") + " "
//											+ facesUtil.retrieveMessage("indonesia") + " "
											+ facesUtil.retrieveMessage("errorAlreadyExists") );
					flag = true;
				}
				
				
			}else if(actionMode.equals(Constants.ACTION_EDIT)) {
				Integer validateSameValue = documentTopicService.getEditDocumentTopicByIdProvCategoryAndTopic(
						documentTopic.getDocumentTopicId()
						, documentTopic.getParameterDetail().getParameterDtlCode()
						, ((documentTopic.getDocumentCategory() != null)? documentTopic.getDocumentCategory().getDocumentCategoryId() : null)
						, documentTopic.getDocumentTopicIn());
				
				if(validateSameValue > 0) {
					facesUtil.addErrMessage(facesUtil.retrieveMessage("formDocumentTopicProvTopic") + " & "
											//+ facesUtil.retrieveMessage("formDocumentTopicDocTopicCat") + " & "
											+ facesUtil.retrieveMessage("formDocumentTopicReg") + " "
//											+ facesUtil.retrieveMessage("indonesia") + " "
											+ facesUtil.retrieveMessage("errorAlreadyExists") );
					flag = true;
				}
				
			}
			
			/*if(StringUtils.isEmpty(documentTopic.getParameterDetail().getParameterDtlCode())){
				   facesUtil.addErrMessage(facesUtil.retrieveMessage("formDocumentTopicProvTopic") + " " +facesUtil.retrieveMessage("validateRequired"));
				   flag = true;
				}
				else if(StringUtils.isEmpty(documentTopic.getDocumentTopicEn())){
					   facesUtil.addErrMessage(facesUtil.retrieveMessage("formDocumentTopicDocTopicCat")+" en "+facesUtil.retrieveMessage("validateRequired"));
					   flag = true;
				}*/
				else if(StringUtils.isEmpty(documentTopic.getDocumentTopicIn())){
					   facesUtil.addErrMessage(facesUtil.retrieveMessage("formDocumentTopicDocTopicCat")+" in "+facesUtil.retrieveMessage("validateRequired"));
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
    		if(!validate()){
    		if(documentTopic.getDocumentTopicId()!=null) {
    			ParameterDetail pd = parameterDetailService.getParameterDetailByParamDtlCode(documentTopic.getParameterDetail().getParameterDtlCode());
    			documentTopic.setParameterDetail(pd);
    			if (documentTopic.getDocumentCategory() != null) {
	    			DocumentCategory dc = documentCategoryService.findById(documentTopic.getDocumentCategory().getDocumentCategoryId());
	    			if (dc != null)
	    				documentTopic.setDocumentCategory(dc);
    			}
    			documentTopic.setLastUpdateBy(facesUtil.retrieveUserLogin());
	    		documentTopic.setLastUpdateDate(new Timestamp(new Date().getTime()));
	    		documentTopic.setDelId(new Long(0));
	    		documentTopic.setEnabledFlag(Constants.CONSTANT_YES);
	    		documentTopicService.update(documentTopic);
    		}else {
    			ParameterDetail pd = parameterDetailService.getParameterDetailByParamDtlCode(documentTopic.getParameterDetail().getParameterDtlCode());
    			documentTopic.setParameterDetail(pd);
    			if (documentTopic.getDocumentCategory() != null) {
	    			DocumentCategory dc = documentCategoryService.findById(documentTopic.getDocumentCategory().getDocumentCategoryId());
	    			if (dc != null)
	    				documentTopic.setDocumentCategory(dc);
    			}
	    		documentTopic.setCreatedBy(facesUtil.retrieveUserLogin());
	    		documentTopic.setCreationDate(new Timestamp(new Date().getTime()));
	    		documentTopic.setDelId(new Long(0));
	    		documentTopic.setEnabledFlag(Constants.CONSTANT_YES);
	    		documentTopicService.save(documentTopic);
    		}
    		
    		facesUtil.redirect("/pages/documentTopic/documentTopic.faces");
    		}
    		
    	}catch (Exception ex) {
            facesUtil.addFacesMsg(
                    FacesMessage.SEVERITY_ERROR, 
                    null, 
                    "Operation Failed : " + ex.getMessage(), "");
        }
    	
    }
    
    public void cancel() {
    	try {
			facesUtil.redirect("/pages/documentTopic/documentTopic.faces");
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

	
	public DocumentTopicService getDocumentTopicService() {
		return documentTopicService;
	}

	public void setDocumentTopicService(DocumentTopicService documentTopicService) {
		this.documentTopicService = documentTopicService;
	}

	

	public String getNavigateSearch() {
		return navigateSearch;
	}

	public void setNavigateSearch(String navigateSearch) {
		this.navigateSearch = navigateSearch;
	}

	public DocumentTopic getDocumentTopic() {
		return documentTopic;
	}

	public void setDocumentTopic(DocumentTopic documentTopic) {
		this.documentTopic = documentTopic;
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

	

	public DocumentCategoryService getDocumentCategoryService() {
		return documentCategoryService;
	}

	public void setDocumentCategoryService(DocumentCategoryService documentCategoryService) {
		this.documentCategoryService = documentCategoryService;
	}
	
	

	
   
}