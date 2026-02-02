package com.wo.module.internalRegulationView.bean;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.apache.log4j.Logger;

import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.paging.DBLazyDataModel;
import com.wo.module.common.paging.DefaultSearchObject;
import com.wo.module.common.util.FileUtil;
import com.wo.module.common.utility.CallApiManager;
import com.wo.module.documentCategory.model.DocumentCategory;
import com.wo.module.documentCategory.service.DocumentCategoryService;
import com.wo.module.documentTopic.service.DocumentTopicService;
import com.wo.module.documentType.constant.DocumentTypeConstants;
import com.wo.module.documentType.model.DocumentType;
import com.wo.module.documentType.service.DocumentTypeService;
import com.wo.module.externalRegulation.service.RegulationMstService;
import com.wo.module.internalRegulationView.constant.InternalRegulationViewConstants;
import com.wo.module.internalRegulationView.model.InternalRegulationView;
import com.wo.module.internalRegulationView.service.InternalRegulationViewService;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.lov.bean.SelectorListener;
import com.wo.module.lov.bean.SelectorModel.SelectorInfo;
import com.wo.module.parameter.model.ParameterDetail;

public class InternalRegulationViewBean extends CommonBean implements SelectorListener<Object>, Serializable {
 
	private static final long serialVersionUID = 1260235876785593120L;

	static Logger logger = Logger.getLogger(InternalRegulationViewBean.class);
	
	private String internalRegulationViewSearch;
	
	private int paging;
	
	private String searchVal;
	
	private Long deleteId;
	
	private Long documentTypeId;
	
	private Long documentCategoryId;
	
	private Long documentTopicId;
	
	private String documentNo;
	
	private String judulPeraturan;
	
	private String startDateStr;
	
	private String endDateStr;
	
	private String trackRecordCode;
	
	private String statusCode;
	
	private Long regulationId;
	
	private String publisherUnit;
	
	private List<SelectItem> docTypes;
	
	private List<SelectItem> docCategories;
	
	private List<SelectItem> docTopics;
	
	private List<SelectItem> trackRecords;
	
	private List<SelectItem> status;
	
	private SelectorInfo selectorRekamJejak;
	
	private DocumentTypeService documentTypeService;
	
	private DocumentCategoryService documentCategoryService;
	
	private DocumentTopicService documentTopicService;
	
	private InternalRegulationViewService internalRegulationViewService;
	
	private RegulationMstService regulationMstService;
	
	private List<InternalRegulationView> internalRegulationList;
	
	private DBLazyDataModel<InternalRegulationView> tableModel;
   
	public FacesUtil facesUtil;
	
	private FileUtil fileUtil;
	
	private String navigateView = InternalRegulationViewConstants.NAVIGATE_VIEW_DETAIL;
	
	private String localLanguange;
	
	private Date startDate;
	private Date endDate;
	
    public void addMessage(String summary) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, null);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
    
    public void addErrMessage(String summary) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, null);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
    
    public void selectStatus() {
    	status = new ArrayList<SelectItem>();
		try {
			List<ParameterDetail> pd = parameterDetailService.getParameterDetailByParamCode("DATA_STATUS");
			for (int i = 0; i < pd.size(); i++) {
				SelectItem si = new SelectItem();
				si.setLabel(((ParameterDetail) pd.get(i)).getName());
				si.setValue(((ParameterDetail) pd.get(i)).getParameterDtlCode());
				status.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    
    public void selectTrackCode() {
    	trackRecords = new ArrayList<SelectItem>();
		try {
			List<ParameterDetail> pd = parameterDetailService.getParameterDetailByParamCode("TRACK_RECORD");
			for (int i = 0; i < pd.size(); i++) {
				SelectItem si = new SelectItem();
				ParameterDetail paramDtl = (ParameterDetail) pd.get(i);
				if (paramDtl.getParameterDtlCode() != null && (paramDtl.getParameterDtlCode().equals("RECORD_CHANGE")
						|| paramDtl.getParameterDtlCode().equals("RECORD_NEW_REGULATION")
						|| paramDtl.getParameterDtlCode().equals("RECORD_REVOKE"))) {
					si.setLabel(paramDtl.getName());
					si.setValue(paramDtl.getParameterDtlCode());
					trackRecords.add(si);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    
    public void selectDocType() {
    	docTypes = new ArrayList<SelectItem>();
    	try {
			List<DocumentType> pd = documentTypeService.searchData(Arrays.asList(new DefaultSearchObject(InternalRegulationViewConstants.JENIS_KETENTUAN_INTERNAL, 
					InternalRegulationViewConstants.PARAM_DTL_CODE_KETENTUAN_INTERNAL)), 0, Integer.MAX_VALUE, null, null);
			for(int i=0;i<pd.size();i++) {
				SelectItem si = new SelectItem();
				si.setLabel(((DocumentType)pd.get(i)).getDocumentType());
				si.setValue(((DocumentType)pd.get(i)).getDocumentTypeId());
				docTypes.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    public void selectDocCategory() {
    	docCategories = new ArrayList<SelectItem>();
    	try {
			List<DocumentCategory> pd = documentCategoryService.searchData(
					Arrays.asList(new DefaultSearchObject(DocumentTypeConstants.WHERE_JENIS_KETENTUAN_CODE,
							InternalRegulationViewConstants.PARAM_DTL_CODE_KETENTUAN_INTERNAL)), 0, Integer.MAX_VALUE,
					null, null);
			for(int i=0;i<pd.size();i++) {
				SelectItem si = new SelectItem();
				si.setLabel(((DocumentCategory)pd.get(i)).getDocumentCategory());
				si.setValue(((DocumentCategory)pd.get(i)).getDocumentCategoryId());
				docCategories.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
   
    
   /* public void selectDocTopic() {
    	docTopics = new ArrayList<SelectItem>();
    	try {
			List<DocumentTopic> pd = documentTopicService.searchData(Arrays.asList(new DefaultSearchObject(InternalRegulationConstants.JENIS_KETENTUAN_INTERNAL, 
					documentCategoryId)), 0, Integer.MAX_VALUE, null, null);
			for(int i=0;i<pd.size();i++) {
				SelectItem si = new SelectItem();
				si.setLabel(((DocumentTopic)pd.get(i)).getDocumentTopic());
				si.setValue(((DocumentTopic)pd.get(i)).getDocumentTopicId());
				docCategories.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    }*/
    
    
    @SuppressWarnings("static-access")
	@PostConstruct
	public void init() {
    	super.init();
    	selectDocType();
    	selectDocCategory();
    	//selectDocTopic();
    	selectTrackCode();
    	selectStatus();
    	paging = Constants.DEFAULT_PAGING_NUMBER;
    	tableModel =  new DBLazyDataModel<InternalRegulationView>(internalRegulationViewService, paging);
    	tableModel.setSearchCriteria(
                Arrays.asList(
                		new DefaultSearchObject(InternalRegulationViewConstants.WHERE_JENIS_KETENTUAN, 
                				InternalRegulationViewConstants.PARAM_DTL_CODE_KETENTUAN_INTERNAL)));
    	
    	selectorRekamJejak = InternalRegulationViewConstants.buildSelectorRekamJejak(facesUtil);
    	
    	Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
    	localLanguange ="IN";
		if (locale != null && locale.equals(locale.ENGLISH)) {
			localLanguange = "EN";
		} 
		
		fileUtil = FileUtil.getInstance();
	}
	
    public void search(ActionEvent actionEvent) {
    	/*startDateStr = facesUtil.retrieveRequestParam("START_DATE");
    	endDateStr = facesUtil.retrieveRequestParam("END_DATE");*/
    	
    	SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
    	tableModel.setSearchCriteria(
                Arrays.asList(
                		new DefaultSearchObject(InternalRegulationViewConstants.WHERE_JENIS_KETENTUAN, 
                				InternalRegulationViewConstants.PARAM_DTL_CODE_KETENTUAN_INTERNAL),
                    new DefaultSearchObject(InternalRegulationViewConstants.WHERE_DOC_TYPE, documentTypeId),
                    new DefaultSearchObject(InternalRegulationViewConstants.WHERE_CATEGORY, documentCategoryId),
                    new DefaultSearchObject(InternalRegulationViewConstants.WHERE_PUBLISHER_UNIT, publisherUnit),
                    new DefaultSearchObject(InternalRegulationViewConstants.WHERE_DOC_NO, documentNo),
                    new DefaultSearchObject(InternalRegulationViewConstants.WHERE_NAME, judulPeraturan),
                    new DefaultSearchObject(InternalRegulationViewConstants.WHERE_PUBLISHED_DATE_START, startDate !=null?sdf.format(startDate):""),
                    new DefaultSearchObject(InternalRegulationViewConstants.WHERE_PUBLISHED_DATE_END, endDate !=null?sdf.format(endDate):""),
                    new DefaultSearchObject(InternalRegulationViewConstants.WHERE_REKAM_JEJAK, trackRecordCode),
                    new DefaultSearchObject(InternalRegulationViewConstants.WHERE_STATUS, statusCode)
                ));
	}
    
    public void reset(ActionEvent actionEvent) {
    	searchVal = "";
    	clearData();
    	
    	tableModel.setSearchCriteria(
                Arrays.asList(
                		new DefaultSearchObject(InternalRegulationViewConstants.WHERE_JENIS_KETENTUAN, 
                				InternalRegulationViewConstants.PARAM_DTL_CODE_KETENTUAN_INTERNAL)));
	}
     
    private void clearData() {
		documentTypeId = null;
		documentCategoryId = null;
		documentTopicId = null;

		documentNo = "";
		judulPeraturan = "";
		statusCode = "";
		publisherUnit = "";
		trackRecordCode = "";

		startDate = null;
		endDate = null;
	}
    
    public void downloadFile(String fileId, String fileName, byte[] content) throws Exception {
		CallApiManager.downloadFile(fileId, fileName, content, parameterDetailService);
	}
    
	public FacesUtil getFacesUtil() {
		return facesUtil;
	}

	public void setFacesUtil(FacesUtil facesUtil) {
		this.facesUtil = facesUtil;
	}

	public Boolean getIsLogin() {
		if(facesUtil.getSessionAttribute(Constants.SESSION_EMPLOYEE) != null 
				|| facesUtil.getSessionAttribute(Constants.SESSION_KANDIDAT) != null )
			return true;
		else
			return false;
	}

	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		InternalRegulationViewBean.logger = logger;
	}

	public String getInternalRegulationViewSearch() {
		return internalRegulationViewSearch;
	}

	public void setInternalRegulationViewSearch(String internalRegulationViewSearch) {
		this.internalRegulationViewSearch = internalRegulationViewSearch;
	}

	public int getPaging() {
		return paging;
	}

	public void setPaging(int paging) {
		this.paging = paging;
	}

	public String getSearchVal() {
		return searchVal;
	}

	public void setSearchVal(String searchVal) {
		this.searchVal = searchVal;
	}

	public Long getDeleteId() {
		return deleteId;
	}

	public void setDeleteId(Long deleteId) {
		this.deleteId = deleteId;
	}

	public Long getDocumentTypeId() {
		return documentTypeId;
	}

	public void setDocumentTypeId(Long documentTypeId) {
		this.documentTypeId = documentTypeId;
	}

	public Long getDocumentCategoryId() {
		return documentCategoryId;
	}

	public void setDocumentCategoryId(Long documentCategoryId) {
		this.documentCategoryId = documentCategoryId;
	}

	public Long getDocumentTopicId() {
		return documentTopicId;
	}

	public void setDocumentTopicId(Long documentTopicId) {
		this.documentTopicId = documentTopicId;
	}

	public String getDocumentNo() {
		return documentNo;
	}

	public void setDocumentNo(String documentNo) {
		this.documentNo = documentNo;
	}

	public String getJudulPeraturan() {
		return judulPeraturan;
	}

	public void setJudulPeraturan(String judulPeraturan) {
		this.judulPeraturan = judulPeraturan;
	}

	public String getStartDateStr() {
		return startDateStr;
	}

	public void setStartDateStr(String startDateStr) {
		this.startDateStr = startDateStr;
	}

	public String getEndDateStr() {
		return endDateStr;
	}

	public void setEndDateStr(String endDateStr) {
		this.endDateStr = endDateStr;
	}

	public String getTrackRecordCode() {
		return trackRecordCode;
	}

	public void setTrackRecordCode(String trackRecordCode) {
		this.trackRecordCode = trackRecordCode;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public Long getRegulationId() {
		return regulationId;
	}

	public void setRegulationId(Long regulationId) {
		this.regulationId = regulationId;
	}

	public String getPublisherUnit() {
		return publisherUnit;
	}

	public void setPublisherUnit(String publisherUnit) {
		this.publisherUnit = publisherUnit;
	}

	public List<SelectItem> getDocTypes() {
		return docTypes;
	}

	public void setDocTypes(List<SelectItem> docTypes) {
		this.docTypes = docTypes;
	}

	public List<SelectItem> getDocCategories() {
		return docCategories;
	}

	public void setDocCategories(List<SelectItem> docCategories) {
		this.docCategories = docCategories;
	}

	public List<SelectItem> getDocTopics() {
		return docTopics;
	}

	public void setDocTopics(List<SelectItem> docTopics) {
		this.docTopics = docTopics;
	}

	public List<SelectItem> getTrackRecords() {
		return trackRecords;
	}

	public void setTrackRecords(List<SelectItem> trackRecords) {
		this.trackRecords = trackRecords;
	}

	public List<SelectItem> getStatus() {
		return status;
	}

	public void setStatus(List<SelectItem> status) {
		this.status = status;
	}

	public SelectorInfo getSelectorRekamJejak() {
		return selectorRekamJejak;
	}

	public void setSelectorRekamJejak(SelectorInfo selectorRekamJejak) {
		this.selectorRekamJejak = selectorRekamJejak;
	}

	public DocumentTypeService getDocumentTypeService() {
		return documentTypeService;
	}

	public void setDocumentTypeService(DocumentTypeService documentTypeService) {
		this.documentTypeService = documentTypeService;
	}

	public DocumentCategoryService getDocumentCategoryService() {
		return documentCategoryService;
	}

	public void setDocumentCategoryService(DocumentCategoryService documentCategoryService) {
		this.documentCategoryService = documentCategoryService;
	}

	public DocumentTopicService getDocumentTopicService() {
		return documentTopicService;
	}

	public void setDocumentTopicService(DocumentTopicService documentTopicService) {
		this.documentTopicService = documentTopicService;
	}

	@Override
	public void itemSelected(String clientId, String widgetVar, Object selectedItem) {
		
	}

	public InternalRegulationViewService getInternalRegulationViewService() {
		return internalRegulationViewService;
	}

	public void setInternalRegulationViewService(InternalRegulationViewService internalRegulationViewService) {
		this.internalRegulationViewService = internalRegulationViewService;
	}

	public RegulationMstService getRegulationMstService() {
		return regulationMstService;
	}

	public void setRegulationMstService(RegulationMstService regulationMstService) {
		this.regulationMstService = regulationMstService;
	}

	public List<InternalRegulationView> getInternalRegulationList() {
		return internalRegulationList;
	}

	public void setInternalRegulationList(List<InternalRegulationView> internalRegulationList) {
		this.internalRegulationList = internalRegulationList;
	}

	public DBLazyDataModel<InternalRegulationView> getTableModel() {
		return tableModel;
	}

	public void setTableModel(DBLazyDataModel<InternalRegulationView> tableModel) {
		this.tableModel = tableModel;
	}

	public String getLocalLanguange() {
		return localLanguange;
	}

	public void setLocalLanguange(String localLanguange) {
		this.localLanguange = localLanguange;
	}

	public String getNavigateView() {
		return navigateView;
	}

	public void setNavigateView(String navigateView) {
		this.navigateView = navigateView;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public FileUtil getFileUtil() {
		return fileUtil;
	}

	public void setFileUtil(FileUtil fileUtil) {
		this.fileUtil = fileUtil;
	}
	
	
}