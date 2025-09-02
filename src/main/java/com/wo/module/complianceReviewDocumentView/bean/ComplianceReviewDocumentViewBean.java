package com.wo.module.complianceReviewDocumentView.bean;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.primefaces.PrimeFaces;

import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.paging.DBLazyDataModel;
import com.wo.module.common.paging.DefaultSearchObject;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.common.utility.CallApiManager;
import com.wo.module.complianceReviewDocument.constant.ComplianceReviewDocumentConstants;
import com.wo.module.complianceReviewDocumentView.constant.ComplianceReviewDocumentViewConstants;
import com.wo.module.complianceReviewDocumentView.model.ComplianceReviewDocumentView;
import com.wo.module.complianceReviewDocumentView.service.ComplianceReviewDocumentViewService;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.parameter.service.ParameterDetailService;

public class ComplianceReviewDocumentViewBean extends CommonBean implements Serializable{

	private static final long serialVersionUID = 3952966598311491207L;

	private String documentTypeCode;
	private String documentSubmitterCode;

	private Long deleteId;

	private String hukNo;
	private String remarks;
	private Date receivedDateFrom;
	private Date receivedDateTo;
	private Date completeDateFrom;
	private Date completeDateTo;

	private List<SelectItem> documentTypes;
	private List<SelectItem> documentSubmitter;

	private ComplianceReviewDocumentViewService complianceReviewDocumentViewService;

	private DBLazyDataModel<ComplianceReviewDocumentView> tableModel;

	public FacesUtil facesUtil;

	private String navigateEdit = ComplianceReviewDocumentViewConstants.NAVIGATE_EDIT;

	public void addMessage(String summary) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, null);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public void addErrMessage(String summary) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, null);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}
	
	public void selectDocumetType() {
		documentTypes = new ArrayList<SelectItem>();

		try {
			List<ParameterDetail> pd = parameterDetailService
					.getParameterDetailByParamCode(ParameterDetail.PARAM_DET_CODE_DOCUMENT_TYPE);
			for (int i = 0; i < pd.size(); i++) {
				SelectItem si = new SelectItem();
				si.setLabel(((ParameterDetail) pd.get(i)).getName());
				si.setValue(((ParameterDetail) pd.get(i)).getParameterDtlCode());
				documentTypes.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void selectDocumentSubmitter() {
		documentSubmitter = new ArrayList<SelectItem>();

		try {
			List<ParameterDetail> pd = parameterDetailService
					.getParameterDetailByParamCode(ParameterDetail.PARAM_DET_CODE_DOCUMENT_SUBMITTER);
			for (int i = 0; i < pd.size(); i++) {
				SelectItem si = new SelectItem();
				si.setLabel(((ParameterDetail) pd.get(i)).getName());
				si.setValue(((ParameterDetail) pd.get(i)).getParameterDtlCode());
				documentSubmitter.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@PostConstruct
	public void init() {
		super.init();
		selectDocumetType();
		selectDocumentSubmitter();
		tableModel = new DBLazyDataModel<ComplianceReviewDocumentView>(complianceReviewDocumentViewService, paging);
	}
	
	@SuppressWarnings("rawtypes")
	public void search(ActionEvent e) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<SearchObject> searchCriteria = new ArrayList<SearchObject>();
		
		if(documentTypeCode != null && !documentTypeCode.isEmpty()) {
			searchCriteria.add(new DefaultSearchObject(ComplianceReviewDocumentConstants.WHERE_DOCUMET_TYPE, documentTypeCode));
		}
		
		if(hukNo != null && !hukNo.isEmpty()) {
			searchCriteria.add(new DefaultSearchObject(ComplianceReviewDocumentConstants.WHERE_HUK_NO, hukNo));
		}
		
		if(remarks != null && !remarks.isEmpty()) {
			searchCriteria.add(new DefaultSearchObject(ComplianceReviewDocumentConstants.WHERE_MATERI, remarks));
		}
		
		if(receivedDateFrom != null) {
			searchCriteria.add(new DefaultSearchObject(ComplianceReviewDocumentConstants.WHERE_RECEIVED_DATE_START, 
					receivedDateFrom != null ? sdf.format(receivedDateFrom) : ""));
		}
		
		if(receivedDateTo != null) {
			searchCriteria.add(new DefaultSearchObject(ComplianceReviewDocumentConstants.WHERE_RECEIVED_DATE_END, 
					receivedDateTo != null ? sdf.format(receivedDateTo) : ""));
		}
		
		if (completeDateFrom != null) {
			searchCriteria.add(new DefaultSearchObject(ComplianceReviewDocumentConstants.WHERE_COMPLETE_DATE_START, 
					completeDateFrom != null ? sdf.format(completeDateFrom) : ""));
		}
		
		if(completeDateTo != null) {
			searchCriteria.add(new DefaultSearchObject(ComplianceReviewDocumentConstants.WHERE_COMPLETE_DATE_START, 
					completeDateTo != null ? sdf.format(completeDateTo) : ""));
		}
		
		if(documentSubmitterCode != null && !documentSubmitter.isEmpty()) {
			searchCriteria.add(new DefaultSearchObject(ComplianceReviewDocumentConstants.WHERE_DOCUMENT_SUBMITTER, documentSubmitterCode));
		}
		
		tableModel.setSearchCriteria(searchCriteria);
	}
	
	public void reset(ActionEvent e) {
		documentTypeCode = "";
		hukNo = "";
		remarks = "";
		receivedDateFrom = null;
		receivedDateTo = null;
		completeDateFrom = null;
		completeDateTo = null;
		documentSubmitterCode = "";
		search(e);
		
		PrimeFaces.current().executeScript("reInitSelect2();");
	}
	
	public void downloadFile (String fileId, String fileName, byte[] content) throws Exception {
		CallApiManager.downloadFile(fileId, fileName, content, parameterDetailService);
	}
	
	public Boolean getIsLogin() {
		if(facesUtil.getSessionAttribute(Constants.SESSION_EMPLOYEE) != null
				|| facesUtil.getSessionAttribute(Constants.SESSION_KANDIDAT) != null) {
			return true;
		}
		return false;
	}
	
	public String getDocumentTypeCode() {
		return documentTypeCode;
	}

	public void setDocumentTypeCode(String documentTypeCode) {
		this.documentTypeCode = documentTypeCode;
	}

	public String getDocumentSubmitterCode() {
		return documentSubmitterCode;
	}

	public void setDocumentSubmitterCode(String documentSubmitterCode) {
		this.documentSubmitterCode = documentSubmitterCode;
	}

	public Long getDeleteId() {
		return deleteId;
	}

	public void setDeleteId(Long deleteId) {
		this.deleteId = deleteId;
	}

	public String getHukNo() {
		return hukNo;
	}

	public void setHukNo(String hukNo) {
		this.hukNo = hukNo;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Date getReceivedDateFrom() {
		return receivedDateFrom;
	}

	public void setReceivedDateFrom(Date receivedDateFrom) {
		this.receivedDateFrom = receivedDateFrom;
	}

	public Date getReceivedDateTo() {
		return receivedDateTo;
	}

	public void setReceivedDateTo(Date receivedDateTo) {
		this.receivedDateTo = receivedDateTo;
	}

	public Date getCompleteDateFrom() {
		return completeDateFrom;
	}

	public void setCompleteDateFrom(Date completeDateFrom) {
		this.completeDateFrom = completeDateFrom;
	}

	public Date getCompleteDateTo() {
		return completeDateTo;
	}

	public void setCompleteDateTo(Date completeDateTo) {
		this.completeDateTo = completeDateTo;
	}

	public List<SelectItem> getDocumentTypes() {
		return documentTypes;
	}

	public void setDocumentTypes(List<SelectItem> documentTypes) {
		this.documentTypes = documentTypes;
	}

	public List<SelectItem> getDocumentSubmitter() {
		return documentSubmitter;
	}

	public void setDocumentSubmitter(List<SelectItem> documentSubmitter) {
		this.documentSubmitter = documentSubmitter;
	}

	public ComplianceReviewDocumentViewService getComplianceReviewDocumentViewService() {
		return complianceReviewDocumentViewService;
	}

	public void setComplianceReviewDocumentViewService(
			ComplianceReviewDocumentViewService complianceReviewDocumentViewService) {
		this.complianceReviewDocumentViewService = complianceReviewDocumentViewService;
	}

	public DBLazyDataModel<ComplianceReviewDocumentView> getTableModel() {
		return tableModel;
	}

	public void setTableModel(DBLazyDataModel<ComplianceReviewDocumentView> tableModel) {
		this.tableModel = tableModel;
	}

	public FacesUtil getFacesUtil() {
		return facesUtil;
	}

	public void setFacesUtil(FacesUtil facesUtil) {
		this.facesUtil = facesUtil;
	}

	public String getNavigateEdit() {
		return navigateEdit;
	}

	public void setNavigateEdit(String navigateEdit) {
		this.navigateEdit = navigateEdit;
	}
	
	
}
