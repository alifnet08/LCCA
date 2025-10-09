package com.wo.module.externalRegulationView.bean;

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
import com.wo.module.documentCategory.constant.DocumentCategoryConstants;
import com.wo.module.documentCategory.model.DocumentCategory;
import com.wo.module.documentCategory.service.DocumentCategoryService;
import com.wo.module.documentTopic.constant.DocumentTopicConstants;
import com.wo.module.documentTopic.model.DocumentTopic;
import com.wo.module.documentTopic.service.DocumentTopicService;
import com.wo.module.documentType.constant.DocumentTypeConstants;
import com.wo.module.documentType.model.DocumentType;
import com.wo.module.documentType.service.DocumentTypeService;
import com.wo.module.externalRegulation.constant.ExternalRegulationConstants;
import com.wo.module.externalRegulation.service.RegulationMstService;
import com.wo.module.externalRegulationView.constant.ExternalRegulationViewConstants;
import com.wo.module.externalRegulationView.model.ExternalRegulationView;
import com.wo.module.externalRegulationView.service.ExternalRegulationViewService;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.lov.bean.SelectorListener;
import com.wo.module.lov.bean.SelectorModel.SelectorInfo;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.parameter.model.ParameterHeader;
import com.wo.module.parameter.service.ParameterDetailService;

public class ExternalRegulationViewBean extends CommonBean implements SelectorListener<Object>, Serializable {
 
	private static final long serialVersionUID = -2466681904743359112L;

	static Logger logger = Logger.getLogger(ExternalRegulationViewBean.class);
	
	private String externalRegulationViewSearch;
	
	private int paging;
	
	private String searchVal;
	
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
	
	private List<SelectItem> docTypes;
	
	private List<SelectItem> docCategories;
	
	private List<SelectItem> docTopics;
	
	private List<SelectItem> trackRecords;
	
	private List<SelectItem> status;
	
	private SelectorInfo selectorRekamJejak;
	
	private DocumentTypeService documentTypeService;
	
	private DocumentCategoryService documentCategoryService;
	
	private DocumentTopicService documentTopicService;
	
	private ExternalRegulationViewService externalRegulationViewService;
	
	private RegulationMstService regulationMstService;
	
	private List<ExternalRegulationView> externalRegulationViewList;
	
	private DBLazyDataModel<ExternalRegulationView> tableModel;
   
	public FacesUtil facesUtil;
	
	private String navigateView = ExternalRegulationViewConstants.NAVIGATE_VIEW;
	
	private String localLanguange;
	
	private Date startDate;
	private Date endDate;
	
	private FileUtil fileUtil;
	
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
			List<ParameterDetail> pd = parameterDetailService.getParameterDetailByParamCode(ParameterHeader.PARAM_HEAD_CODE_DATA_STATUS);
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
			List<ParameterDetail> pd = parameterDetailService.getParameterDetailByParamCode(ParameterHeader.PARAM_HEAD_CODE_TRACK_RECORD);
			for (int i = 0; i < pd.size(); i++) {
				SelectItem si = new SelectItem();
				ParameterDetail paramDtl = (ParameterDetail) pd.get(i);
				if (paramDtl.getParameterDtlCode() != null && (paramDtl.getParameterDtlCode().equals(ParameterDetail.PARAM_DET_CODE_RECORD_CHANGE)
						|| paramDtl.getParameterDtlCode().equals(ParameterDetail.PARAM_DET_CODE_RECORD_REGULATION)
						|| paramDtl.getParameterDtlCode().equals(ParameterDetail.PARAM_DET_CODE_RECORD_REVOKE))) {
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
			List<DocumentType> pd = documentTypeService.searchData(
					Arrays.asList(new DefaultSearchObject(DocumentTypeConstants.WHERE_JENIS_KETENTUAN_CODE,
							ExternalRegulationConstants.PARAMETER_DTL_CODE_KETENTUAN_EKSTERNAL)),
					0, Integer.MAX_VALUE, null, null);
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
					Arrays.asList(new DefaultSearchObject(DocumentCategoryConstants.WHERE_JENIS_KETENTUAN_CODE,
							ExternalRegulationConstants.PARAMETER_DTL_CODE_KETENTUAN_EKSTERNAL)),
					0, Integer.MAX_VALUE, null, null);
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
    
   
    
    public void selectDocTopic() {
    	docTopics = new ArrayList<SelectItem>();
    	try {
    		List<DocumentTopic> pd = documentTopicService.searchData(
					Arrays.asList(new DefaultSearchObject(DocumentTopicConstants.WHERE_JENIS_KETENTUAN_CODE,
							ExternalRegulationConstants.PARAMETER_DTL_CODE_KETENTUAN_EKSTERNAL)),
					0, Integer.MAX_VALUE, null, null);
			for(int i=0;i<pd.size();i++) {
				SelectItem si = new SelectItem();
				si.setLabel(((DocumentTopic)pd.get(i)).getDocumentTopic());
				si.setValue(((DocumentTopic)pd.get(i)).getDocumentTopicId());
				docTopics.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    
    @SuppressWarnings("static-access")
	@PostConstruct
	public void init() {
    	selectDocType();
    	selectDocCategory();
    	selectDocTopic();
    	selectTrackCode();
    	selectStatus();
    	paging = Constants.DEFAULT_PAGING_NUMBER;
    	tableModel =  new DBLazyDataModel<ExternalRegulationView>(externalRegulationViewService, paging);
    	tableModel.setSearchCriteria(
                Arrays.asList(
                	new DefaultSearchObject(ExternalRegulationViewConstants.WHERE_JENIS_KETENTUAN, 
                			ExternalRegulationViewConstants.PARAMETER_DTL_CODE_KETENTUAN_EKSTERNAL)));
                
    	selectorRekamJejak = ExternalRegulationViewConstants.buildSelectorRekamJejak(facesUtil);
    	
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
                	new DefaultSearchObject(ExternalRegulationViewConstants.WHERE_JENIS_KETENTUAN, 
								ExternalRegulationViewConstants.PARAMETER_DTL_CODE_KETENTUAN_EKSTERNAL),
                    new DefaultSearchObject(ExternalRegulationViewConstants.WHERE_DOC_TYPE, documentTypeId),
                    new DefaultSearchObject(ExternalRegulationViewConstants.WHERE_CATEGORY, documentCategoryId),
                    new DefaultSearchObject(ExternalRegulationViewConstants.WHERE_TOPIC, documentTopicId),
                    new DefaultSearchObject(ExternalRegulationViewConstants.WHERE_DOC_NO, documentNo),
                    new DefaultSearchObject(ExternalRegulationViewConstants.WHERE_NAME, judulPeraturan),
                    new DefaultSearchObject(ExternalRegulationViewConstants.WHERE_PUBLISHED_DATE_START, startDate!=null?sdf.format(startDate):""),
                    new DefaultSearchObject(ExternalRegulationViewConstants.WHERE_PUBLISHED_DATE_END, endDate!=null?sdf.format(endDate):""),
                    new DefaultSearchObject(ExternalRegulationViewConstants.WHERE_REKAM_JEJAK, trackRecordCode),
                    new DefaultSearchObject(ExternalRegulationViewConstants.WHERE_STATUS, statusCode)
                ));
	}
    
	public void reset(ActionEvent actionEvent) {
    	searchVal = "";
    	clearData();
    	tableModel.setSearchCriteria(
                Arrays.asList(
                    new DefaultSearchObject(ExternalRegulationViewConstants.WHERE_JENIS_KETENTUAN, 
							ExternalRegulationViewConstants.PARAMETER_DTL_CODE_KETENTUAN_EKSTERNAL)
               ));
	}
	
	private void clearData() {
		documentTypeId = null;
		documentCategoryId = null;
		documentTopicId = null;

		documentNo = "";
		judulPeraturan = "";
		statusCode = "";
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
		ExternalRegulationViewBean.logger = logger;
	}

	public String getExternalRegulationViewSearch() {
		return externalRegulationViewSearch;
	}

	public void setExternalRegulationViewSearch(String externalRegulationViewSearch) {
		this.externalRegulationViewSearch = externalRegulationViewSearch;
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

	public ExternalRegulationViewService getExternalRegulationViewService() {
		return externalRegulationViewService;
	}

	public void setExternalRegulationViewService(ExternalRegulationViewService externalRegulationViewService) {
		this.externalRegulationViewService = externalRegulationViewService;
	}

	public RegulationMstService getRegulationMstService() {
		return regulationMstService;
	}

	public void setRegulationMstService(RegulationMstService regulationMstService) {
		this.regulationMstService = regulationMstService;
	}

	public List<ExternalRegulationView> getExternalRegulationViewList() {
		return externalRegulationViewList;
	}

	public void setExternalRegulationViewList(List<ExternalRegulationView> externalRegulationViewList) {
		this.externalRegulationViewList = externalRegulationViewList;
	}

	public DBLazyDataModel<ExternalRegulationView> getTableModel() {
		return tableModel;
	}

	public void setTableModel(DBLazyDataModel<ExternalRegulationView> tableModel) {
		this.tableModel = tableModel;
	}
	
	public String getLocalLanguange() {
		return localLanguange;
	}

	public void setLocalLanguange(String localLanguange) {
		this.localLanguange = localLanguange;
	}

	@Override
	public void itemSelected(String clientId, String widgetVar, Object selectedItem) {
		
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