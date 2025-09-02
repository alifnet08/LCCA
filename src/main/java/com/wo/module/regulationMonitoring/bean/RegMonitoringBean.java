package com.wo.module.regulationMonitoring.bean;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
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
import com.wo.module.regulationMonitoring.constant.RegMonitoringConstants;
import com.wo.module.regulationMonitoring.model.RegMonitoringTmp;
import com.wo.module.regulationMonitoring.service.RegMonitoringTmpService;
import com.wo.module.regulationMonitoring.service.RegulationMonitoringService;
import com.wo.module.regulationMonitoring.vo.RegMonitoringVO;
import com.wo.module.responsibility.model.Responsibility;
import com.wo.module.responsibility.service.ResponsibilityService;


public class RegMonitoringBean extends CommonBean implements Serializable {

	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(RegMonitoringBean.class);

	private String regMonitoringTmpSearch;

	private String searchVal;

	private String provTypeCode;

	private Long deleteId;

	private Long documentTypeId;
	private Long documentCategoryId;
	private Long documentTopicId;
	private String documentNo;
	private String judulPeraturan;
	private Date pubDateFrom;
	private Date pubDateTo;
	private Date effDateFrom;
	private Date effDateTo;
	private Date targetDateFrom;
	private Date targetDateTo;
	
	private Boolean respSuperAdmFlag;

	private String trackRecordCode;

	private String statusCode;

	private Long regId;

	private List<SelectItem> docTypes;

	private List<SelectItem> docCategories;

	private List<SelectItem> docTopics;

	private List<SelectItem> trackRecords;

	private List<SelectItem> provTypes;

	private List<SelectItem> status;

	private SelectorInfo selectorRekamJejak;

	//private ParameterDetailService parameterDetailService;

	private DocumentTypeService documentTypeService;

	private DocumentCategoryService documentCategoryService;

	private DocumentTopicService documentTopicService;

	private RegulationMonitoringService regulationMonitoringService;

	private RegMonitoringTmpService regMonitoringTmpService;
	
	private ResponsibilityService responsibilityService;

	private DBLazyDataModel<RegMonitoringVO> tableModel;

	public FacesUtil facesUtil;

	private String navigateEdit = RegMonitoringConstants.NAVIGATE_EDIT;

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
		selectDocType();
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
			for (int i = 0; i < pd.size(); i++) {
				SelectItem si = new SelectItem();
				si.setLabel(((DocumentType) pd.get(i)).getDocumentType());
				si.setValue(((DocumentType) pd.get(i)).getDocumentTypeId());
				docTypes.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		PrimeFaces.current().executeScript("reInitSelect2();");
//		RequestContext.getCurrentInstance().execute("reInitSelect2();");
	}

	public void selectDocCategory() {
		docCategories = new ArrayList<SelectItem>();
		try {
			List<DocumentCategory> pd = documentCategoryService.searchData(
					Arrays.asList(new DefaultSearchObject(DocumentCategoryConstants.WHERE_JENIS_KETENTUAN_CODE, provTypeCode)),
					0, Integer.MAX_VALUE, null, null);
			for (int i = 0; i < pd.size(); i++) {
				SelectItem si = new SelectItem();
				si.setLabel(((DocumentCategory) pd.get(i)).getDocumentCategory());
				si.setValue(((DocumentCategory) pd.get(i)).getDocumentCategoryId());
				docCategories.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		PrimeFaces.current().executeScript("reInitSelect2();");
//		RequestContext.getCurrentInstance().execute("reInitSelect2();");
	}

	public void selectDocTopic() {
		docTopics = new ArrayList<SelectItem>();
		try {
			List<DocumentTopic> pd = documentTopicService.searchData(
					Arrays.asList(new DefaultSearchObject(DocumentTopicConstants.WHERE_JENIS_KETENTUAN_CODE, provTypeCode)), 0,
					Integer.MAX_VALUE, null, null);
			for (int i = 0; i < pd.size(); i++) {
				SelectItem si = new SelectItem();
				si.setLabel(((DocumentTopic) pd.get(i)).getDocumentTopic());
				si.setValue(((DocumentTopic) pd.get(i)).getDocumentTopicId());
				docTopics.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		PrimeFaces.current().executeScript("reInitSelect2();");
//		RequestContext.getCurrentInstance().execute("reInitSelect2();");
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

	public void checkSuperAdm() {
		
		String checkRespSuperAdmIdStr = parameterDetailService.getParamDtlNameByParamDtlCode(RegMonitoringConstants.RESPONSIBILITY_SUPER_ADMIN_ID);
		
		Responsibility responsibility = responsibilityService.findById(getUserLogin().getResponsibilityId());
		
//		Long checkRespSuperAdmId = Long.valueOf(checkRespSuperAdmIdStr);
		
//		if(getUserLogin().getResponsibilityId().equals(checkRespSuperAdmId)) {
//			respSuperAdmFlag = true;
//		}else {
//			respSuperAdmFlag = false;
//		}
		
		if(responsibility.getName().toUpperCase().equals(checkRespSuperAdmIdStr)) {
			respSuperAdmFlag = true;
		}else {
			respSuperAdmFlag = false;
		}
		
	}
	
	@PostConstruct
	public void init() {
		super.init();
		selectDocType();
		selectDocCategory();
		selectDocTopic();
		selectTrackCode();
		selectStatus();
		selectProvType();
		
		//to check whether user logged in have Super Admin Responsibility - Commented Because conflicting FSD & Schedule
		//checkSuperAdm();
		
		tableModel = new DBLazyDataModel<RegMonitoringVO>(regulationMonitoringService, paging);
	}

	public void search(ActionEvent actionEvent) {

		// pubDateFrom = facesUtil.retrieveRequestParam("PUBLISHED_DATE_FROM");
		// pubDateTo = facesUtil.retrieveRequestParam("PUBLISHED_DATE_TO");
		// effDateFrom = facesUtil.retrieveRequestParam("EFF_DATE_FROM");
		// effDateTo = facesUtil.retrieveRequestParam("EFF_DATE_TO");
		// targetDateFrom = facesUtil.retrieveRequestParam("TARGET_DATE_FROM");
		// targetDateTo = facesUtil.retrieveRequestParam("TARGET_DATE_TO");

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		tableModel.setSearchCriteria(Arrays.asList(
				new DefaultSearchObject(RegMonitoringConstants.WHERE_PROV_TYPE, provTypeCode),
				new DefaultSearchObject(RegMonitoringConstants.WHERE_DOC_TYPE, documentTypeId),
				new DefaultSearchObject(RegMonitoringConstants.WHERE_CATEGORY, documentCategoryId),
				new DefaultSearchObject(RegMonitoringConstants.WHERE_TOPIC, documentTopicId),
				new DefaultSearchObject(RegMonitoringConstants.WHERE_DOC_NO, documentNo),
				new DefaultSearchObject(RegMonitoringConstants.WHERE_NAME, judulPeraturan),
				new DefaultSearchObject(RegMonitoringConstants.WHERE_STATUS, statusCode),
				new DefaultSearchObject(RegMonitoringConstants.WHERE_PUBLISHED_DATE_START, pubDateFrom != null ? sdf.format(pubDateFrom) : ""),
				new DefaultSearchObject(RegMonitoringConstants.WHERE_PUBLISHED_DATE_END, pubDateTo != null ? sdf.format(pubDateTo) : ""),
				new DefaultSearchObject(RegMonitoringConstants.WHERE_EFF_DATE_START, effDateFrom != null ? sdf.format(effDateFrom) : ""),
				new DefaultSearchObject(RegMonitoringConstants.WHERE_EFF_DATE_END, effDateTo != null ? sdf.format(effDateTo) : ""),
				new DefaultSearchObject(RegMonitoringConstants.WHERE_TARGET_DATE_START, targetDateFrom != null ? sdf.format(targetDateFrom) : ""),
				new DefaultSearchObject(RegMonitoringConstants.WHERE_TARGET_DATE_END, targetDateTo != null ? sdf.format(targetDateTo) : "")));
	}

	public void reset(ActionEvent actionEvent) {
		provTypeCode = "";
		documentTypeId = null;
		documentCategoryId = null;
		documentTopicId = null;
		documentNo= "";
		judulPeraturan= "";
		statusCode = "";
		pubDateFrom = null;
		pubDateTo = null;
		effDateFrom = null;
		effDateTo = null;
		targetDateFrom = null;
		targetDateTo = null;
		clearData();
		tableModel.setSearchCriteria(Arrays.asList(new DefaultSearchObject(SearchObject.ALL_COLUMNS, searchVal)));
		
		PrimeFaces.current().executeScript("reInitSelect2();");
//		RequestContext.getCurrentInstance().execute("reInitSelect2();");
	}

	public void clearData() {
		provTypeCode = "";
		documentCategoryId = null;
		documentTypeId = null;
		documentTopicId = null;
		documentNo = "";
		judulPeraturan = "";
		statusCode = "";
		pubDateFrom = null;
		pubDateTo = null;
		effDateFrom = null;
		effDateTo = null;
		targetDateFrom = null;
		targetDateTo = null;
	}
	
	public void delete(Long deleteId) {
		try {
			RegMonitoringTmp dt = regMonitoringTmpService.findById(deleteId);
			dt.setEnabledFlag(Constants.CONSTANT_NO);
			dt.setLastUpdateBy(facesUtil.retrieveUserLogin());
			dt.setLastUpdateDate(new Timestamp(new Date().getTime()));
			regMonitoringTmpService.update(dt);
			facesUtil.addSuccessMsg(facesUtil.retrieveMessage("deleteSuccess"));
		} catch (Exception e) {
			facesUtil.addFacesMsg(FacesMessage.SEVERITY_ERROR, null, "Operation Failed : " + e.getMessage(), "");
		}
	}

	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		RegMonitoringBean.logger = logger;
	}

	public String getRegMonitoringTmpSearch() {
		return regMonitoringTmpSearch;
	}

	public void setRegMonitoringTmpSearch(String regMonitoringTmpSearch) {
		this.regMonitoringTmpSearch = regMonitoringTmpSearch;
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

	public Date getPubDateFrom() {
		return pubDateFrom;
	}

	public void setPubDateFrom(Date pubDateFrom) {
		this.pubDateFrom = pubDateFrom;
	}

	public Date getPubDateTo() {
		return pubDateTo;
	}

	public void setPubDateTo(Date pubDateTo) {
		this.pubDateTo = pubDateTo;
	}

	public Date getEffDateFrom() {
		return effDateFrom;
	}

	public void setEffDateFrom(Date effDateFrom) {
		this.effDateFrom = effDateFrom;
	}

	public Date getEffDateTo() {
		return effDateTo;
	}

	public void setEffDateTo(Date effDateTo) {
		this.effDateTo = effDateTo;
	}

	public Date getTargetDateFrom() {
		return targetDateFrom;
	}

	public void setTargetDateFrom(Date targetDateFrom) {
		this.targetDateFrom = targetDateFrom;
	}

	public Date getTargetDateTo() {
		return targetDateTo;
	}

	public void setTargetDateTo(Date targetDateTo) {
		this.targetDateTo = targetDateTo;
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

	public Long getRegId() {
		return regId;
	}

	public void setRegId(Long regId) {
		this.regId = regId;
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

	public RegMonitoringTmpService getRegMonitoringTmpService() {
		return regMonitoringTmpService;
	}

	public void setRegMonitoringTmpService(RegMonitoringTmpService regMonitoringTmpService) {
		this.regMonitoringTmpService = regMonitoringTmpService;
	}

	public DBLazyDataModel<RegMonitoringVO> getTableModel() {
		return tableModel;
	}

	public void setTableModel(DBLazyDataModel<RegMonitoringVO> tableModel) {
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public RegulationMonitoringService getRegulationMonitoringService() {
		return regulationMonitoringService;
	}

	public void setRegulationMonitoringService(RegulationMonitoringService regulationMonitoringService) {
		this.regulationMonitoringService = regulationMonitoringService;
	}

	public Boolean getRespSuperAdmFlag() {
		return respSuperAdmFlag;
	}

	public void setRespSuperAdmFlag(Boolean respSuperAdmFlag) {
		this.respSuperAdmFlag = respSuperAdmFlag;
	}

	public ResponsibilityService getResponsibilityService() {
		return responsibilityService;
	}

	public void setResponsibilityService(ResponsibilityService responsibilityService) {
		this.responsibilityService = responsibilityService;
	}

	/*public ParameterDetailService getParameterDetailService() {
		return parameterDetailService;
	}

	public void setParameterDetailService(ParameterDetailService parameterDetailService) {
		this.parameterDetailService = parameterDetailService;
	}*/
}