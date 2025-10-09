package com.wo.module.regulationMonitoringView.bean;

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
import org.primefaces.PrimeFaces;

import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.paging.DBLazyDataModel;
import com.wo.module.common.paging.DefaultSearchObject;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.documentCategory.constant.DocumentCategoryConstants;
import com.wo.module.documentCategory.model.DocumentCategory;
import com.wo.module.documentCategory.service.DocumentCategoryService;
import com.wo.module.documentTopic.constant.DocumentTopicConstants;
import com.wo.module.documentTopic.model.DocumentTopic;
import com.wo.module.documentTopic.service.DocumentTopicService;
import com.wo.module.documentType.constant.DocumentTypeConstants;
import com.wo.module.documentType.model.DocumentType;
import com.wo.module.documentType.service.DocumentTypeService;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.lov.bean.SelectorModel.SelectorInfo;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.parameter.service.ParameterDetailService;
import com.wo.module.regulationMonitoring.service.RegMonitoringTrcService;
import com.wo.module.regulationMonitoringView.constant.RegMonitoringViewConstants;
import com.wo.module.regulationMonitoringView.service.RegMonitoringViewService;
import com.wo.module.regulationMonitoringView.vo.RegMonitoringViewVO;


public class RegMonitoringViewBean extends CommonBean implements  Serializable {
 
	private static final long serialVersionUID = -2515045547625166912L;

	static Logger logger = Logger.getLogger(RegMonitoringViewBean.class);
	
	private String regMonitoringTrcSearch;
	
	private int paging;
	
	private String searchVal;
	
	private String provTypeCode;
	
	private Long deleteId;
	
	private Long documentTypeId;	
	private Long documentCategoryId;	
	private Long documentTopicId;	
	private String documentNo;	
	private String judulPeraturan;	
	private Date startDateStr;	
	private Date endDateStr;	
	private Date publishedStartDate;
	private Date publishedEndDate;
	private Date effectiveStartDate;
	private Date effectiveEndDate;
	private Date targetStartDate;
	private Date targetEndDate;   
	
	private String trackRecordCode;
	
	private String statusCode;
	
	private Long regulationId;
	
	private List<SelectItem> docTypes;
	
	private List<SelectItem> docCategories;
	
	private List<SelectItem> docTopics;
	
	private List<SelectItem> trackRecords;
	
	private List<SelectItem> provTypes;
	
	private List<SelectItem> status;
	
	private SelectorInfo selectorRekamJejak;
	
	private DocumentTypeService documentTypeService;
	
	private DocumentCategoryService documentCategoryService;
	
	private DocumentTopicService documentTopicService;
	
	private RegMonitoringViewService regMonitoringViewService;
	
	private RegMonitoringTrcService regMonitoringTrcService;
	
	private DBLazyDataModel<RegMonitoringViewVO> tableModel;
   
	public FacesUtil facesUtil;
	
	private String navigateView = RegMonitoringViewConstants.NAVIGATE_VIEW_DETAIL;
	
	private String localLanguange;
	
	
    public void addMessage(String summary) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, null);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
    
    public void addErrMessage(String summary) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, null);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
    
    public void onChangeProvType() {
		selectDocType();
		selectDocCategory();
		selectDocTopic();
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
				si.setLabel(((ParameterDetail) pd.get(i)).getName());
				si.setValue(((ParameterDetail) pd.get(i)).getParameterDtlCode());
				trackRecords.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    
    public void selectDocType() {
    	docTypes = new ArrayList<SelectItem>();
    	try {
			List<DocumentType> pd = documentTypeService.searchData(
					Arrays.asList(new DefaultSearchObject(DocumentTypeConstants.WHERE_JENIS_KETENTUAN_CODE, provTypeCode)), 0,
					Integer.MAX_VALUE, null, null);
			for(int i=0;i<pd.size();i++) {
				SelectItem si = new SelectItem();
				si.setLabel(((DocumentType)pd.get(i)).getDocumentType());
				si.setValue(((DocumentType)pd.get(i)).getDocumentTypeId());
				docTypes.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	PrimeFaces.current().executeScript("reInitSelect2();");
//    	RequestContext.getCurrentInstance().execute("reInitSelect2();");
    }
    
    public void selectDocCategory() {
    	docCategories = new ArrayList<SelectItem>();
    	try {
			List<DocumentCategory> pd = documentCategoryService.searchData(
					Arrays.asList(new DefaultSearchObject(DocumentCategoryConstants.WHERE_JENIS_KETENTUAN_CODE, provTypeCode)), 0,
					Integer.MAX_VALUE, null, null);
			for(int i=0;i<pd.size();i++) {
				SelectItem si = new SelectItem();
				si.setLabel(((DocumentCategory)pd.get(i)).getDocumentCategory());
				si.setValue(((DocumentCategory)pd.get(i)).getDocumentCategoryId());
				docCategories.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	PrimeFaces.current().executeScript("reInitSelect2();");
//    	RequestContext.getCurrentInstance().execute("reInitSelect2();");
    }
    
   
    
    public void selectDocTopic() {
    	docTopics = new ArrayList<SelectItem>();
    	try {
    		List<DocumentTopic> pd = documentTopicService.searchData(
					Arrays.asList(new DefaultSearchObject(DocumentTopicConstants.WHERE_JENIS_KETENTUAN_CODE, provTypeCode)), 0,
					Integer.MAX_VALUE, null, null);
			for(int i=0;i<pd.size();i++) {
				SelectItem si = new SelectItem();
				si.setLabel(((DocumentTopic)pd.get(i)).getDocumentTopic());
				si.setValue(((DocumentTopic)pd.get(i)).getDocumentTopicId());
				docTopics.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	PrimeFaces.current().executeScript("reInitSelect2();");
//    	RequestContext.getCurrentInstance().execute("reInitSelect2();");
    }
    
    public void selectProvType() {
		provTypes = new ArrayList<SelectItem>();
		try {
			List<ParameterDetail> pd = parameterDetailService.getParameterDetailByParamCode("JENIS_KETENTUAN");
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
    
    
    @SuppressWarnings("static-access")
	@PostConstruct
	public void init() {
    	super.init();
    	selectDocType();
    	selectDocCategory();
    	selectDocTopic();
    	selectTrackCode();
    	selectStatus();
    	selectProvType();
    	paging = Constants.DEFAULT_PAGING_NUMBER;
    	tableModel =  new DBLazyDataModel<RegMonitoringViewVO>(regMonitoringViewService, paging);
    	
    	//selectorRekamJejak = RegulationSocializationConstants.buildSelectorRekamJejak(facesUtil);
    	
    	Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
    	localLanguange ="IN";
		if (locale != null && locale.equals(locale.ENGLISH)) {
			localLanguange = "EN";
		} 
    	
	}
	
    public void search(ActionEvent actionEvent) {
    	//startDateStr = facesUtil.retrieveRequestParam("START_DATE");
    	//endDateStr = facesUtil.retrieveRequestParam("END_DATE");
			
//    	SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    	    	
    	tableModel.setSearchCriteria(
                Arrays.asList(
                		new DefaultSearchObject(RegMonitoringViewConstants.WHERE_PROVISION_TYPE, provTypeCode),
                		new DefaultSearchObject(RegMonitoringViewConstants.WHERE_DOC_TYPE, documentTypeId),
	                    new DefaultSearchObject(RegMonitoringViewConstants.WHERE_CATEGORY, documentCategoryId),
	                    new DefaultSearchObject(RegMonitoringViewConstants.WHERE_TOPIC, documentTopicId),
	                    new DefaultSearchObject(RegMonitoringViewConstants.WHERE_DOC_NO, documentNo),
	                    new DefaultSearchObject(RegMonitoringViewConstants.WHERE_NAME, judulPeraturan),
	                    new DefaultSearchObject(RegMonitoringViewConstants.WHERE_STATUS, statusCode),
	                    new DefaultSearchObject(RegMonitoringViewConstants.WHERE_PUBLISHED_DATE_START, publishedStartDate !=null?sdf.format(publishedStartDate):""),
	                    new DefaultSearchObject(RegMonitoringViewConstants.WHERE_PUBLISHED_DATE_END, publishedEndDate !=null?sdf.format(publishedEndDate):""),
	                    new DefaultSearchObject(RegMonitoringViewConstants.WHERE_EFF_DATE_START, effectiveStartDate !=null?sdf.format(effectiveStartDate):""),
	                    new DefaultSearchObject(RegMonitoringViewConstants.WHERE_EFF_DATE_END, effectiveEndDate !=null?sdf.format(effectiveEndDate):""),
	                    new DefaultSearchObject(RegMonitoringViewConstants.WHERE_TARGET_DATE_START, targetStartDate !=null?sdf.format(targetStartDate):""),
	                    new DefaultSearchObject(RegMonitoringViewConstants.WHERE_TARGET_DATE_END, targetEndDate !=null?sdf.format(targetEndDate):"")
                ));
	}
    
    public void reset(ActionEvent actionEvent) {
    	searchVal = "";
    	clearData();
        
    	tableModel.setSearchCriteria(
                Arrays.asList(
                    new DefaultSearchObject(SearchObject.ALL_COLUMNS, searchVal)));
    	
    	PrimeFaces.current().executeScript("reInitSelect2();");
//    	RequestContext.getCurrentInstance().execute("reInitSelect2();");
	}
    
    private void clearData() {
    	provTypeCode = "";
    	documentTypeId = null;
    	documentCategoryId = null;
    	documentTopicId = null;
    	
    	documentNo = "";
    	judulPeraturan = "";    	
    	statusCode = "";
    	publishedStartDate = null;
    	publishedEndDate = null;
    	effectiveStartDate = null;
    	effectiveEndDate = null;
    	targetStartDate = null;
    	targetEndDate = null;
    }

	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		RegMonitoringViewBean.logger = logger;
	}

	public String getRegMonitoringTrcSearch() {
		return regMonitoringTrcSearch;
	}

	public void setRegMonitoringTrcSearch(String regMonitoringTrcSearch) {
		this.regMonitoringTrcSearch = regMonitoringTrcSearch;
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

	public String getProvTypeCode() {
		return provTypeCode;
	}

	public void setProvTypeCode(String provTypeCode) {
		this.provTypeCode = provTypeCode;
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

	public Date getStartDateStr() {
		return startDateStr;
	}

	public void setStartDateStr(Date startDateStr) {
		this.startDateStr = startDateStr;
	}

	public Date getEndDateStr() {
		return endDateStr;
	}

	public void setEndDateStr(Date endDateStr) {
		this.endDateStr = endDateStr;
	}

	public Date getPublishedStartDate() {
		return publishedStartDate;
	}

	public void setPublishedStartDate(Date publishedStartDate) {
		this.publishedStartDate = publishedStartDate;
	}

	public Date getPublishedEndDate() {
		return publishedEndDate;
	}

	public void setPublishedEndDate(Date publishedEndDate) {
		this.publishedEndDate = publishedEndDate;
	}

	public Date getEffectiveStartDate() {
		return effectiveStartDate;
	}

	public void setEffectiveStartDate(Date effectiveStartDate) {
		this.effectiveStartDate = effectiveStartDate;
	}

	public Date getEffectiveEndDate() {
		return effectiveEndDate;
	}

	public void setEffectiveEndDate(Date effectiveEndDate) {
		this.effectiveEndDate = effectiveEndDate;
	}

	public Date getTargetStartDate() {
		return targetStartDate;
	}

	public void setTargetStartDate(Date targetStartDate) {
		this.targetStartDate = targetStartDate;
	}

	public Date getTargetEndDate() {
		return targetEndDate;
	}

	public void setTargetEndDate(Date targetEndDate) {
		this.targetEndDate = targetEndDate;
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

	public List<SelectItem> getProvTypes() {
		return provTypes;
	}

	public void setProvTypes(List<SelectItem> provTypes) {
		this.provTypes = provTypes;
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

	public RegMonitoringViewService getRegMonitoringViewService() {
		return regMonitoringViewService;
	}

	public void setRegMonitoringViewService(RegMonitoringViewService regMonitoringViewService) {
		this.regMonitoringViewService = regMonitoringViewService;
	}

	public RegMonitoringTrcService getRegMonitoringTrcService() {
		return regMonitoringTrcService;
	}

	public void setRegMonitoringTrcService(RegMonitoringTrcService regMonitoringTrcService) {
		this.regMonitoringTrcService = regMonitoringTrcService;
	}

	public DBLazyDataModel<RegMonitoringViewVO> getTableModel() {
		return tableModel;
	}

	public void setTableModel(DBLazyDataModel<RegMonitoringViewVO> tableModel) {
		this.tableModel = tableModel;
	}

	public FacesUtil getFacesUtil() {
		return facesUtil;
	}

	public void setFacesUtil(FacesUtil facesUtil) {
		this.facesUtil = facesUtil;
	}

	public String getNavigateView() {
		return navigateView;
	}

	public void setNavigateView(String navigateView) {
		this.navigateView = navigateView;
	}

	public String getLocalLanguange() {
		return localLanguange;
	}

	public void setLocalLanguange(String localLanguange) {
		this.localLanguange = localLanguange;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}