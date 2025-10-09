package com.wo.module.documentType.bean;

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
import com.wo.module.documentType.constant.DocumentTypeConstants;
import com.wo.module.documentType.model.DocumentType;
import com.wo.module.documentType.service.DocumentTypeService;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.parameter.model.ParameterHeader;

public class DocumentTypeEditBean extends CommonBean implements Serializable {
 
	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(DocumentTypeBean.class);
	
	private DocumentType documentType;
	
	private Boolean isViewOnly;
	
	private String actionMode;
	
	private String editedId;
	
	private List<SelectItem> provTypes;
	
	private DocumentTypeService documentTypeService;
	
	public FacesUtil facesUtil;
	
	private String navigateSearch = DocumentTypeConstants.NAVIGATE_SEARCH;
    
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
    	documentType = new DocumentType();
    	ParameterDetail pd = new ParameterDetail();
    	documentType.setParameterDetail(pd);
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
		documentType = documentTypeService.findById(idLong);
    }
    
    public Boolean validate(){
		Boolean flag = false;
		try {
			
			if(actionMode.equals(Constants.ACTION_ADD)) {
				Integer validateSameValue = documentTypeService.getDocumentTypeByProvAndType(
						documentType.getParameterDetail().getParameterDtlCode(), documentType.getDocumentTypeIn());
				
				if(validateSameValue > 0) {
					facesUtil.addErrMessage(facesUtil.retrieveMessage("formDocumentTypeProvType") + " & "
											+ facesUtil.retrieveMessage("formDocumentTypeTitle") + " "
//											+ facesUtil.retrieveMessage("indonesia") + " "
											+ facesUtil.retrieveMessage("errorAlreadyExists") );
					flag = true;
				}
			}else if(actionMode.equals(Constants.ACTION_EDIT)) {
				
				Integer validateEditSameValue = documentTypeService.getEditDocumentTypeByIdProvAndType(
						documentType.getDocumentTypeId()
						, documentType.getParameterDetail().getParameterDtlCode()
						, documentType.getDocumentTypeIn());
				
				if(validateEditSameValue > 0) {
					facesUtil.addErrMessage(facesUtil.retrieveMessage("formDocumentTypeProvType") + " & "
							+ facesUtil.retrieveMessage("formDocumentTypeTitle") + " "
//							+ facesUtil.retrieveMessage("indonesia") + " "
							+ facesUtil.retrieveMessage("errorAlreadyExists") );
					flag = true;
				}
			}
			
			if(StringUtils.isEmpty(documentType.getParameterDetail().getParameterDtlCode())){
				   facesUtil.addErrMessage(facesUtil.retrieveMessage("formDocumentTypeProvType") + " " +facesUtil.retrieveMessage("validateRequired"));
				   flag = true;
			}
			/*else if(StringUtils.isEmpty(documentType.getDocumentTypeEn())){
				   facesUtil.addErrMessage(facesUtil.retrieveMessage("formDocumentTypeTitle")+" en "+facesUtil.retrieveMessage("validateRequired"));
				   flag = true;
			}*/
			else if(StringUtils.isEmpty(documentType.getDocumentTypeIn())){
				   facesUtil.addErrMessage(facesUtil.retrieveMessage("formDocumentTypeTitle")+" "+facesUtil.retrieveMessage("validateRequired"));
				   flag = true;
			}
			else if(StringUtils.isEmpty(documentType.getTypeDescription())){
				   facesUtil.addErrMessage(facesUtil.retrieveMessage("formDocumentTypeDocTypeDesc")+" in "+facesUtil.retrieveMessage("validateRequired"));
				   flag = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return flag;
    }
    public void save() {
    	try {
    		if(!validate()){
    		if(documentType.getDocumentTypeId()!=null) {
    			ParameterDetail pd = parameterDetailService.getParameterDetailByParamDtlCode(documentType.getParameterDetail().getParameterDtlCode());
    			documentType.setParameterDetail(pd);
    			documentType.setLastUpdateBy(facesUtil.retrieveUserLogin());
	    		documentType.setLastUpdateDate(new Timestamp(new Date().getTime()));
	    		documentType.setDelId(new Long(0));
	    		documentType.setEnabledFlag(Constants.CONSTANT_YES);
	    		documentTypeService.update(documentType);
    		}else {
    			ParameterDetail pd = parameterDetailService.getParameterDetailByParamDtlCode(documentType.getParameterDetail().getParameterDtlCode());
    			documentType.setParameterDetail(pd);
	    		documentType.setCreatedBy(facesUtil.retrieveUserLogin());
	    		documentType.setCreationDate(new Timestamp(new Date().getTime()));
	    		documentType.setDelId(new Long(0));
	    		documentType.setEnabledFlag(Constants.CONSTANT_YES);
	    		documentTypeService.save(documentType);
    		}
    		
    		facesUtil.redirect("/pages/documentType/documentType.faces");
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
			facesUtil.redirect("/pages/documentType/documentType.faces");
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

	
	public DocumentTypeService getDocumentTypeService() {
		return documentTypeService;
	}

	public void setDocumentTypeService(DocumentTypeService documentTypeService) {
		this.documentTypeService = documentTypeService;
	}

	

	public String getNavigateSearch() {
		return navigateSearch;
	}

	public void setNavigateSearch(String navigateSearch) {
		this.navigateSearch = navigateSearch;
	}

	public DocumentType getDocumentType() {
		return documentType;
	}

	public void setDocumentType(DocumentType documentType) {
		this.documentType = documentType;
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

	

	
	

	
   
}