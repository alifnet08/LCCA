
package com.wo.module.regulationSocialization.bean;

import java.io.Serializable;
import java.sql.Timestamp;
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
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.FillPatternType;
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
import com.wo.module.regulationSocialization.constant.RegulationSocializationConstants;
import com.wo.module.regulationSocialization.model.SocializationTmp;
import com.wo.module.regulationSocialization.service.RegulationSocializationService;
import com.wo.module.regulationSocialization.service.SocializationTmpService;
import com.wo.module.regulationSocialization.vo.RegulationSocializationVO;
import com.wo.module.regulationSocialization.vo.StatusConfirmationVO;

public class RegulationSocializationBean extends CommonBean implements Serializable {

	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(RegulationSocializationBean.class);

	private String regulationSocializationTmpSearch;

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

	//private ParameterDetailService parameterDetailService;

	private DocumentTypeService documentTypeService;

	private DocumentCategoryService documentCategoryService;

	private DocumentTopicService documentTopicService;

	private RegulationSocializationService regulationSocializationService;

	private SocializationTmpService socializationTmpService;

	private DBLazyDataModel<RegulationSocializationVO> tableModel;

	public FacesUtil facesUtil;

	private String navigateEdit = RegulationSocializationConstants.NAVIGATE_EDIT;

	@SuppressWarnings("unused")
	public void postProcessXLS(Object document) {
		try {
			
			Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
			
			HSSFWorkbook wb = (HSSFWorkbook) document;
			HSSFSheet sheet = wb.getSheetAt(0);
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			
			List<RegulationSocializationVO> listDataXls = regulationSocializationService.searchDataXLS(Arrays.asList(
					new DefaultSearchObject(RegulationSocializationConstants.WHERE_PROV_TYPE, provTypeCode),
					new DefaultSearchObject(RegulationSocializationConstants.WHERE_DOC_TYPE, documentTypeId),
					new DefaultSearchObject(RegulationSocializationConstants.WHERE_CATEGORY, documentCategoryId),
					new DefaultSearchObject(RegulationSocializationConstants.WHERE_TOPIC, documentTopicId),
					new DefaultSearchObject(RegulationSocializationConstants.WHERE_DOC_NO, documentNo),
					new DefaultSearchObject(RegulationSocializationConstants.WHERE_NAME, judulPeraturan),
					new DefaultSearchObject(RegulationSocializationConstants.WHERE_STATUS, statusCode),
					new DefaultSearchObject(RegulationSocializationConstants.WHERE_PUBLISHED_DATE_START, pubDateFrom != null ? sdf.format(pubDateFrom) : ""),
					new DefaultSearchObject(RegulationSocializationConstants.WHERE_PUBLISHED_DATE_END, pubDateTo != null ? sdf.format(pubDateTo) : ""),
					new DefaultSearchObject(RegulationSocializationConstants.WHERE_EFF_DATE_START, effDateFrom != null ? sdf.format(effDateFrom) : ""),
					new DefaultSearchObject(RegulationSocializationConstants.WHERE_EFF_DATE_END, effDateTo != null ? sdf.format(effDateTo) : ""),
					new DefaultSearchObject(RegulationSocializationConstants.WHERE_TARGET_DATE_START, targetDateFrom != null ? sdf.format(targetDateFrom) : ""),
					new DefaultSearchObject(RegulationSocializationConstants.WHERE_TARGET_DATE_END, targetDateTo != null ? sdf.format(targetDateTo) : "")));
			
			// create Header
			HSSFRow header = sheet.createRow(0);
			for (int i = 0; i < 18; i++) {
				HSSFCell cell = header.createCell((short) i);
				if (i == 0) {
					cell.setCellValue(facesUtil.retrieveMessage("formRegulationSocializationProvType"));
				} else if (i == 1) {
					cell.setCellValue(facesUtil.retrieveMessage("formRegulationSocializationRegNo"));
				} else if (i == 2) {
					cell.setCellValue(facesUtil.retrieveMessage("formRegulationSocializationRegTitle"));
				} else if (i == 3) {
					cell.setCellValue(facesUtil.retrieveMessage("formRegulationSocializationPublishedDate"));
				} else if (i == 4) {
					cell.setCellValue(facesUtil.retrieveMessage("formRegulationSocializationEffectiveDate"));
				} else if (i == 5) {
					cell.setCellValue(facesUtil.retrieveMessage("formRegulationSocializationStatus"));
				} else if (i == 6) {
					cell.setCellValue(facesUtil.retrieveMessage("formRegulationSocializationFollowup"));
				} else if (i == 7) {
					cell.setCellValue(facesUtil.retrieveMessage("formRegulationSocializationNote"));
				} else if (i == 8) {
					cell.setCellValue(facesUtil.retrieveMessage("formRegulationSocializationPICName"));
				} else if (i == 9) {
					cell.setCellValue(facesUtil.retrieveMessage("formRegulationSocializationTargetDate"));
				} else if (i == 10) {
					cell.setCellValue(facesUtil.retrieveMessage("formRegulationSocializationFollowupStatus"));
				} else if (i == 11) {
					cell.setCellValue(facesUtil.retrieveMessage("formRegulationSocializationPICConfirmation"));
				} else if (i == 12) {
					cell.setCellValue(facesUtil.retrieveMessage("formRegulationSocializationConfirmationDate"));
				} else if (i == 13) {
					cell.setCellValue(facesUtil.retrieveMessage("formRegulationSocializationFollowupDate"));
				} else if (i == 14){
					cell.setCellValue(facesUtil.retrieveMessage("formRegulationSocializationInformation"));
				} else if (i == 15){
					cell.setCellValue(facesUtil.retrieveMessage("formRegulationSocializationComplianceCheckStatus"));
				} else if (i == 16){
					cell.setCellValue(facesUtil.retrieveMessage("formRegulationSocializationCategory"));
				} else if (i == 17){
					cell.setCellValue(facesUtil.retrieveMessage("formRegulationSocializationTopic"));
				}

			}
			
			//kosongin data
			int rowNum = 1;
			for(int i=0;i<18;i++) {
				HSSFRow row = sheet.createRow(rowNum);
				for (int x = 0; x < 18; x++) {
					HSSFCell cell = row.createCell((short) x);
					cell.setCellValue("");
				}
				rowNum++;
			}
			//kosongin data

			// create Data
			rowNum = 1;
			String picName = "";
			String followUpStatus = "";
			String confirmationDate = "";
			String followupDate = "";
			String followupBy = "";
			String targetDate = "";
			String information = "";
			String compilanceStatus = "";
			String makerFollowupNote="";
			
			for (int i = 0; i < listDataXls.size(); i++) {
				RegulationSocializationVO er = (RegulationSocializationVO) listDataXls.get(i);				
				for (int j = 0; j < er.getStatusList().size(); j++) {
					StatusConfirmationVO vo = er.getStatusList().get(j);
				    picName = vo.getNamePic();
					followUpStatus = vo.getFollowupStatus();
					confirmationDate = vo.getConfirmationDate();
					followupDate = vo.getFollowupDate();
					followupBy = vo.getFollowupBy();					
					targetDate = vo.getTargetDate();
					information = vo.getFollowupNote();
					compilanceStatus = vo.getComplianceStatus();
					makerFollowupNote = vo.getMakerFollowupNote();
				}
				
				HSSFRow row = sheet.createRow(rowNum);
				for (int x = 0; x < 18; x++) {
					HSSFCell cell = row.createCell(x);
					if (x == 0) {
						cell.setCellValue(er.getJenisKetentuan() != null ? er.getJenisKetentuan() : "");
					} else if (x == 1) {
						cell.setCellValue(er.getNoDokumen() != null ? er.getNoDokumen() : "");
					} else if (x == 2) {
						cell.setCellValue(er.getJdlPeraturan() != null ? er.getJdlPeraturan() : "");
					} else if (x == 3) {
						cell.setCellValue(er.getPublishedDateStr() != null ? er.getPublishedDateStr() : "");
					} else if (x == 4) {
						cell.setCellValue(er.getEffectiveDateStr() != null ? er.getEffectiveDateStr() : "");
					} else if (x == 5) {
						cell.setCellValue(er.getStatus() != null ? er.getStatus() : "");
					} else if (x == 6) {
						cell.setCellValue(er.getStatusTindakLanjut() != null ? er.getStatusTindakLanjut() : "");
					} else if (x == 7) {
						cell.setCellValue(makerFollowupNote);
					} else if (x == 8) {
						cell.setCellValue(picName);
					} else if (x == 9) {
						cell.setCellValue(targetDate);
					} else if (x == 10) {
						cell.setCellValue(followUpStatus);
					} else if (x == 11) {
						cell.setCellValue(followupBy);
					} else if (x == 12) {
						cell.setCellValue(confirmationDate);
					} else if (x == 13) {
						cell.setCellValue(followupDate);
					} else if (x == 14) {
						cell.setCellValue(information);
					} else if (x == 15) {
						cell.setCellValue(compilanceStatus);
					} else if (x == 16) {
						cell.setCellValue(er.getKategori());
					} else if (x == 17) {
						cell.setCellValue(er.getTopik());
					}
					

				}
				rowNum++;
			}

			// style
			HSSFCellStyle cellStyle = wb.createCellStyle();
			cellStyle.setFillForegroundColor(HSSFColor.HSSFColorPredefined.GREEN.getIndex());
			cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			for (int i = 0; i < header.getPhysicalNumberOfCells(); i++) {
				HSSFCell cell = header.getCell(i);
				cell.setCellStyle(cellStyle);
				sheet.autoSizeColumn(i);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

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

	@PostConstruct
	public void init() {
		super.init();
		selectDocType();
		selectDocCategory();
		selectDocTopic();
		selectTrackCode();
		selectStatus();
		selectProvType();
		tableModel = new DBLazyDataModel<RegulationSocializationVO>(regulationSocializationService, paging);
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
				new DefaultSearchObject(RegulationSocializationConstants.WHERE_PROV_TYPE, provTypeCode),
				new DefaultSearchObject(RegulationSocializationConstants.WHERE_DOC_TYPE, documentTypeId),
				new DefaultSearchObject(RegulationSocializationConstants.WHERE_CATEGORY, documentCategoryId),
				new DefaultSearchObject(RegulationSocializationConstants.WHERE_TOPIC, documentTopicId),
				new DefaultSearchObject(RegulationSocializationConstants.WHERE_DOC_NO, documentNo),
				new DefaultSearchObject(RegulationSocializationConstants.WHERE_NAME, judulPeraturan),
				new DefaultSearchObject(RegulationSocializationConstants.WHERE_STATUS, statusCode),
				new DefaultSearchObject(RegulationSocializationConstants.WHERE_PUBLISHED_DATE_START, pubDateFrom != null ? sdf.format(pubDateFrom) : ""),
				new DefaultSearchObject(RegulationSocializationConstants.WHERE_PUBLISHED_DATE_END, pubDateTo != null ? sdf.format(pubDateTo) : ""),
				new DefaultSearchObject(RegulationSocializationConstants.WHERE_EFF_DATE_START, effDateFrom != null ? sdf.format(effDateFrom) : ""),
				new DefaultSearchObject(RegulationSocializationConstants.WHERE_EFF_DATE_END, effDateTo != null ? sdf.format(effDateTo) : ""),
				new DefaultSearchObject(RegulationSocializationConstants.WHERE_TARGET_DATE_START, targetDateFrom != null ? sdf.format(targetDateFrom) : ""),
				new DefaultSearchObject(RegulationSocializationConstants.WHERE_TARGET_DATE_END, targetDateTo != null ? sdf.format(targetDateTo) : "")));
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
			SocializationTmp dt = socializationTmpService.findById(deleteId);
			dt.setEnabledFlag(Constants.CONSTANT_NO);
			dt.setLastUpdateBy(facesUtil.retrieveUserLogin());
			dt.setLastUpdateDate(new Timestamp(new Date().getTime()));
			socializationTmpService.update(dt);
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

	public String getRegulationSocializationTmpSearch() {
		return regulationSocializationTmpSearch;
	}

	public void setRegulationSocializationTmpSearch(String regulationSocializationTmpSearch) {
		this.regulationSocializationTmpSearch = regulationSocializationTmpSearch;
	}

	public String getSearchVal() {
		return searchVal;
	}

	public void setSearchVal(String searchVal) {
		this.searchVal = searchVal;
	}

	public String getNavigateEdit() {
		return navigateEdit;
	}

	public void setNavigateEdit(String navigateEdit) {
		this.navigateEdit = navigateEdit;
	}

	public Long getDeleteId() {
		return deleteId;
	}

	public void setDeleteId(Long deleteId) {
		this.deleteId = deleteId;
	}

	public SocializationTmpService getSocializationTmpService() {
		return socializationTmpService;
	}

	public void setSocializationTmpService(SocializationTmpService socializationTmpService) {
		this.socializationTmpService = socializationTmpService;
	}

	public List<SelectItem> getDocTypes() {
		return docTypes;
	}

	public void setDocTypes(List<SelectItem> docTypes) {
		this.docTypes = docTypes;
	}

	/*public ParameterDetailService getParameterDetailService() {
		return parameterDetailService;
	}

	public void setParameterDetailService(ParameterDetailService parameterDetailService) {
		this.parameterDetailService = parameterDetailService;
	}*/

	public DocumentTypeService getDocumentTypeService() {
		return documentTypeService;
	}

	public void setDocumentTypeService(DocumentTypeService documentTypeService) {
		this.documentTypeService = documentTypeService;
	}

	public Long getDocumentTypeId() {
		return documentTypeId;
	}

	public void setDocumentTypeId(Long documentTypeId) {
		this.documentTypeId = documentTypeId;
	}

	public DocumentCategoryService getDocumentCategoryService() {
		return documentCategoryService;
	}

	public void setDocumentCategoryService(DocumentCategoryService documentCategoryService) {
		this.documentCategoryService = documentCategoryService;
	}

	public List<SelectItem> getDocCategories() {
		return docCategories;
	}

	public void setDocCategories(List<SelectItem> docCategories) {
		this.docCategories = docCategories;
	}

	public DocumentTopicService getDocumentTopicService() {
		return documentTopicService;
	}

	public void setDocumentTopicService(DocumentTopicService documentTopicService) {
		this.documentTopicService = documentTopicService;
	}

	public List<SelectItem> getDocTopics() {
		return docTopics;
	}

	public void setDocTopics(List<SelectItem> docTopics) {
		this.docTopics = docTopics;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public List<SelectItem> getTrackRecords() {
		return trackRecords;
	}

	public void setTrackRecords(List<SelectItem> trackRecords) {
		this.trackRecords = trackRecords;
	}

	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		RegulationSocializationBean.logger = logger;
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

	public List<SelectItem> getStatus() {
		return status;
	}

	public void setStatus(List<SelectItem> status) {
		this.status = status;
	}

	public SelectorInfo getSelectorRekamJejak() {
		// return
		// RegulationSocializationTmpConstants.buildSelectorRekamJejak(regulationId);
		return selectorRekamJejak;
	}

	public void setSelectorRekamJejak(SelectorInfo selectorRekamJejak) {
		this.selectorRekamJejak = selectorRekamJejak;
	}

	public Long getRegulationId() {
		return regulationId;
	}

	public void setRegulationId(Long regulationId) {
		this.regulationId = regulationId;
	}

	public List<SelectItem> getProvTypes() {
		return provTypes;
	}

	public void setProvTypes(List<SelectItem> provTypes) {
		this.provTypes = provTypes;
	}

	public RegulationSocializationService getRegulationSocializationService() {
		return regulationSocializationService;
	}

	public void setRegulationSocializationService(RegulationSocializationService regulationSocializationService) {
		this.regulationSocializationService = regulationSocializationService;
	}

	public DBLazyDataModel<RegulationSocializationVO> getTableModel() {
		return tableModel;
	}

	public void setTableModel(DBLazyDataModel<RegulationSocializationVO> tableModel) {
		this.tableModel = tableModel;
	}

	public String getProvTypeCode() {
		return provTypeCode;
	}

	public void setProvTypeCode(String provTypeCode) {
		this.provTypeCode = provTypeCode;
	}

}