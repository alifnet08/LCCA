package com.wo.module.internalRegulation.bean;

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

import org.apache.commons.lang3.StringUtils;
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
import com.wo.module.common.util.EntityUtil;
import com.wo.module.common.util.FileUtil;
import com.wo.module.common.utility.CallApiManager;
import com.wo.module.documentCategory.constant.DocumentCategoryConstants;
import com.wo.module.documentCategory.model.DocumentCategory;
import com.wo.module.documentCategory.service.DocumentCategoryService;
import com.wo.module.documentTopic.service.DocumentTopicService;
import com.wo.module.documentType.constant.DocumentTypeConstants;
import com.wo.module.documentType.model.DocumentType;
import com.wo.module.documentType.service.DocumentTypeService;
import com.wo.module.externalRegulation.model.Regulation;
import com.wo.module.externalRegulation.model.RegulationAttachment;
import com.wo.module.externalRegulation.model.RegulationMst;
import com.wo.module.externalRegulation.model.RegulationProposerUnit;
import com.wo.module.externalRegulation.service.RegulationMstService;
import com.wo.module.externalRegulation.service.RegulationService;
import com.wo.module.internalRegulation.constant.InternalRegulationConstants;
import com.wo.module.internalRegulation.model.InternalRegulation;
import com.wo.module.internalRegulation.service.InternalRegulationService;
import com.wo.module.logActivity.model.LogActivity;
import com.wo.module.logActivity.service.LogActivityService;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.lov.bean.SelectorListener;
import com.wo.module.lov.bean.SelectorModel.SelectorInfo;
import com.wo.module.parameter.model.ParameterDetail;

public class InternalRegulationBean extends CommonBean implements SelectorListener<Object>, Serializable {

	private static final long serialVersionUID = -6922297374980398656L;

	static Logger logger = Logger.getLogger(InternalRegulationBean.class);

	private String internalRegulationSearch;

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

	//private ParameterDetailService parameterDetailService;
	//add 16-11-23
	private LogActivity logActivity;
	
	private LogActivityService logActivityService;

	private DocumentTypeService documentTypeService;

	private DocumentCategoryService documentCategoryService;

	private DocumentTopicService documentTopicService;

	private InternalRegulationService internalRegulationService;

	private RegulationService regulationService;
	
	private RegulationMstService regulationMstService;

	private List<InternalRegulation> internalRegulationList;

	private DBLazyDataModel<InternalRegulation> tableModel;

	public FacesUtil facesUtil;

	private FileUtil fileUtil;

	private String navigateEdit = InternalRegulationConstants.NAVIGATE_EDIT;

	private String localLanguange;

	private Date startDate;
	private Date endDate;
	
	private String replaceExport(String kalimat, String users) {
		if (StringUtils.isNotBlank(users)) {
			kalimat = kalimat.replace("{user_name}", users);
		}
		return kalimat;
	}
	
	public void log() throws Exception {
		ParameterDetail pd = parameterDetailService.getParameterDetailByParamDtlCode("ACTIVITY_TYPE_PERATURAN_INTERNAL");
		LogActivity la = new LogActivity();
		la.setUser(getUserLogin());
		la.setActivityType(pd.getParameterDtlCode());
		la.setActivityDate(new Timestamp(new Date().getTime()));
		ParameterDetail activityDate = parameterDetailService
				.getParameterDetailByParamDtlCode("LOG_ACT_PER_INTERNAL_EXPORT");
		String str = activityDate.getNameIn();
		String hasil = replaceExport(str,
				facesUtil.getUserLogin().getName());
		la.setActivityNote(hasil);
		if (StringUtils.isBlank(la.getCreatedBy())) {
			EntityUtil.setCreationInfo(la, facesUtil.retrieveUserLogin());
		} else {
			EntityUtil.setUpdateInfo(la, facesUtil.retrieveUserLogin());
		}
		logActivityService.save(la);
	}

	public void postProcessXLS(Object document) {
		try {

			SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");

			HSSFWorkbook wb = (HSSFWorkbook) document;
			HSSFSheet sheet = wb.getSheetAt(0);

			List<InternalRegulation> listDataXls = internalRegulationService.searchData(
					Arrays.asList(
							new DefaultSearchObject(InternalRegulationConstants.WHERE_JENIS_KETENTUAN,
									InternalRegulationConstants.PARAM_DTL_CODE_KETENTUAN_INTERNAL),
							new DefaultSearchObject(InternalRegulationConstants.WHERE_DOC_TYPE, documentTypeId),
							new DefaultSearchObject(InternalRegulationConstants.WHERE_CATEGORY, documentCategoryId),
							new DefaultSearchObject(InternalRegulationConstants.WHERE_PUBLISHER_UNIT, publisherUnit),
							new DefaultSearchObject(InternalRegulationConstants.WHERE_DOC_NO, documentNo),
							new DefaultSearchObject(InternalRegulationConstants.WHERE_NAME, judulPeraturan),
							new DefaultSearchObject(InternalRegulationConstants.WHERE_PUBLISHED_DATE_START,
									startDate != null ? sdf.format(startDate) : ""),
							new DefaultSearchObject(InternalRegulationConstants.WHERE_PUBLISHED_DATE_END,
									endDate != null ? sdf.format(endDate) : ""),
							new DefaultSearchObject(InternalRegulationConstants.WHERE_REKAM_JEJAK, trackRecordCode),
							new DefaultSearchObject(InternalRegulationConstants.WHERE_STATUS, statusCode)),
					0, Integer.MAX_VALUE, null, null);
			// create Header
			HSSFRow header = sheet.createRow(0);
			for (int i = 0; i < 15; i++) {
				HSSFCell cell = header.createCell((short) i);
				if (i == 0) {
					cell.setCellValue(facesUtil.retrieveMessage("formExternalRegulationRegNo"));
				} else if (i == 1) {
					cell.setCellValue(facesUtil.retrieveMessage("formExternalRegulationRegTitle"));
				} else if (i == 2) {
					cell.setCellValue(facesUtil.retrieveMessage("formExternalRegulationAttachment"));
				} else if (i == 3) {
					cell.setCellValue(facesUtil.retrieveMessage("formExternalRegulationStatus"));
				} else if (i == 4) {
					cell.setCellValue(facesUtil.retrieveMessage("formExternalRegulationRegulationType"));
				} else if (i == 5) {
					cell.setCellValue(facesUtil.retrieveMessage("formInternalRegulationPublisherUnit"));
				} else if (i == 6) {
					cell.setCellValue(facesUtil.retrieveMessage("formExternalRegulationPublishedDate"));
				} else if (i == 7) {
					cell.setCellValue(facesUtil.retrieveMessage("formInternalRegulationEffDate"));
				} else if (i == 8) {
					cell.setCellValue(facesUtil.retrieveMessage("formExternalRegulationExpDate"));
				} else if (i == 9) {
					cell.setCellValue(facesUtil.retrieveMessage("formExternalRegulationRegulationCategory"));
				}else if (i == 10) {
					cell.setCellValue("Direktorat");
				}else if (i == 11) {
					cell.setCellValue("Topik Peraturan");
				}else if (i == 12) {
					cell.setCellValue(facesUtil.retrieveMessage("formInternalRegulationRekamJejak"));
				}else if (i == 13) {
					cell.setCellValue(facesUtil.retrieveMessage("formInternalRegulationPreviousRegulationRegNo"));
				}else if (i == 14) {
					cell.setCellValue(facesUtil.retrieveMessage("formInternalRegulationPreviousRegulationRegTitle"));
				}

			}

			//kosongin data
			int rowNum = 1;
			for(int i=0;i<14;i++) {
				HSSFRow row = sheet.createRow(rowNum);
				for (int x = 0; x < 15; x++) {
					HSSFCell cell = row.createCell((short) x);
					cell.setCellValue("");
				}
				rowNum++;
			}
			//kosongin data
			
			// create Data
			rowNum = 1;
			for (int i = 0; i < listDataXls.size(); i++) {
				InternalRegulation er = (InternalRegulation) listDataXls.get(i);
				HSSFRow row = sheet.createRow(rowNum);
				for (int x = 0; x < 15; x++) {
					HSSFCell cell = row.createCell((short) x);
					if (x == 0) {
						cell.setCellValue(er.getDocumentNo() != null ? er.getDocumentNo() : "");
					} else if (x == 1) {
						cell.setCellValue(er.getName() != null ? er.getName() : "");
					} else if (x == 2) {
						StringBuilder attachment = new StringBuilder();
						for (int j = 0; j < er.getRegulationAttachments().size(); j++) {
							RegulationAttachment ra = er.getRegulationAttachments().get(j);
							attachment.append(ra.getAttachmentFile()).append(",");
						}
						cell.setCellValue(attachment.toString());
//						cell.setCellValue("");
					} else if (x == 3) {
						cell.setCellValue(er.getStatusName() != null ? er.getStatusName() : "");
					} else if (x == 4) {
						cell.setCellValue(er.getDocumentTypeName() != null ? er.getDocumentTypeName() : "");
					} else if (x == 5) {
						StringBuilder publisherUnit = new StringBuilder();
						for (int j = 0; j < er.getRegulationProposerUnits().size(); j++) {
							RegulationProposerUnit rpu = er.getRegulationProposerUnits().get(j);
							publisherUnit.append(rpu.getPublisherUnit()).append(",");
						}
						cell.setCellValue(publisherUnit.toString());
						//cell.setCellValue(er.getPublisherUnit() != null ? er.getPublisherUnit() : "");
					} else if (x == 6) {
						cell.setCellValue(er.getPublishedDateStr() != null ? er.getPublishedDateStr() : "");
					} else if (x == 7) {
						cell.setCellValue(er.getEffectiveDateStr() != null ? er.getEffectiveDateStr() : "");
					} else if (x == 8) {
						cell.setCellValue(er.getExpiredDateStr() != null ? er.getExpiredDateStr() : "");
					} else if (x == 9) {
						cell.setCellValue(er.getDocumentCategoryName() != null ? er.getDocumentCategoryName() : "");
					}else if (x == 10) {
						cell.setCellValue(er.getDirectorate() != null ? er.getDirectorate() : "");
					}else if (x == 11) {
						cell.setCellValue(er.getDocumentTopicNameIn() != null ? er.getDocumentTopicNameIn() : "");
					}else if (x == 12){
						cell.setCellValue(er.getRekamJejak() != null ? er.getRekamJejak() : "");
					}else if (x == 13) {
						if(!er.getRekamJejakCode().equals(ParameterDetail.PARAM_DET_CODE_TRACK_RECORD_NEW_REGULATION)) cell.setCellValue(er.getPrevDocNo() != null ? er.getPrevDocNo() : "");
					}else if (x == 14) {
						if(!er.getRekamJejakCode().equals(ParameterDetail.PARAM_DET_CODE_TRACK_RECORD_NEW_REGULATION)) cell.setCellValue(er.getPrevRegTitle() != null ? er.getPrevRegTitle() : "");
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
			log();

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
			List<DocumentType> pd = documentTypeService.searchData(
					Arrays.asList(new DefaultSearchObject(DocumentTypeConstants.WHERE_JENIS_KETENTUAN_CODE,
							InternalRegulationConstants.PARAM_DTL_CODE_KETENTUAN_INTERNAL)),
					0, Integer.MAX_VALUE, null, null);
			for (int i = 0; i < pd.size(); i++) {
				SelectItem si = new SelectItem();
				si.setLabel(((DocumentType) pd.get(i)).getDocumentType());
				si.setValue(((DocumentType) pd.get(i)).getDocumentTypeId());
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
							InternalRegulationConstants.PARAM_DTL_CODE_KETENTUAN_INTERNAL)),
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
	}

	/*
	 * public void selectDocTopic() { docTopics = new ArrayList<SelectItem>(); try {
	 * List<DocumentTopic> pd = documentTopicService.searchData(Arrays.asList(new
	 * DefaultSearchObject(InternalRegulationConstants.JENIS_KETENTUAN_INTERNAL,
	 * documentCategoryId)), 0, Integer.MAX_VALUE, null, null); for(int
	 * i=0;i<pd.size();i++) { SelectItem si = new SelectItem();
	 * si.setLabel(((DocumentTopic)pd.get(i)).getDocumentTopic());
	 * si.setValue(((DocumentTopic)pd.get(i)).getDocumentTopicId());
	 * docTopics.add(si); } } catch (Exception e) {
	 * block e.printStackTrace(); } }
	 */

	@SuppressWarnings("static-access")
	@PostConstruct
	public void init() {
		super.init();
		selectDocType();
		selectDocCategory();
		// selectDocTopic();
		selectTrackCode();
		selectStatus();
		paging = Constants.DEFAULT_PAGING_NUMBER;
		tableModel = new DBLazyDataModel<InternalRegulation>(internalRegulationService, paging);
		tableModel.setSearchCriteria(
				Arrays.asList(new DefaultSearchObject(InternalRegulationConstants.WHERE_JENIS_KETENTUAN,
						InternalRegulationConstants.PARAM_DTL_CODE_KETENTUAN_INTERNAL)));

		selectorRekamJejak = InternalRegulationConstants.buildSelectorRekamJejak(facesUtil);

		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		localLanguange = "IN";
		if (locale != null && locale.equals(locale.ENGLISH)) {
			localLanguange = "EN";
		}
		fileUtil = FileUtil.getInstance();
	}

	public void search(ActionEvent actionEvent) {
		/*
		 * startDateStr = facesUtil.retrieveRequestParam("START_DATE"); endDateStr =
		 * facesUtil.retrieveRequestParam("END_DATE");
		 */

		SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
		tableModel.setSearchCriteria(Arrays.asList(
				new DefaultSearchObject(InternalRegulationConstants.WHERE_JENIS_KETENTUAN,
						InternalRegulationConstants.PARAM_DTL_CODE_KETENTUAN_INTERNAL),
				new DefaultSearchObject(InternalRegulationConstants.WHERE_DOC_TYPE, documentTypeId),
				new DefaultSearchObject(InternalRegulationConstants.WHERE_CATEGORY, documentCategoryId),
				new DefaultSearchObject(InternalRegulationConstants.WHERE_PUBLISHER_UNIT, publisherUnit),
				new DefaultSearchObject(InternalRegulationConstants.WHERE_DOC_NO, documentNo),
				new DefaultSearchObject(InternalRegulationConstants.WHERE_NAME, judulPeraturan),
				new DefaultSearchObject(InternalRegulationConstants.WHERE_PUBLISHED_DATE_START,
						startDate != null ? sdf.format(startDate) : ""),
				new DefaultSearchObject(InternalRegulationConstants.WHERE_PUBLISHED_DATE_END,
						endDate != null ? sdf.format(endDate) : ""),
				new DefaultSearchObject(InternalRegulationConstants.WHERE_REKAM_JEJAK, trackRecordCode),
				new DefaultSearchObject(InternalRegulationConstants.WHERE_STATUS, statusCode)));
	}

	public void reset(ActionEvent actionEvent) {
		searchVal = "";
		clearData();
		tableModel.setSearchCriteria(
				Arrays.asList(new DefaultSearchObject(InternalRegulationConstants.WHERE_JENIS_KETENTUAN,
						InternalRegulationConstants.PARAM_DTL_CODE_KETENTUAN_INTERNAL)));
		
		PrimeFaces.current().executeScript("reInitSelect2();");
//		RequestContext.getCurrentInstance().execute("reInitSelect2();");
	}

	public Boolean validateDeleted(Long deleteId) {
		Boolean flag = false;

		try {
			Integer checkDataReg = regulationService.getCheckDataRegulationSocialization(deleteId);

			if (checkDataReg != null && checkDataReg > 0) {
				facesUtil.addErrMessage(facesUtil.retrieveMessage("formInternalRegulationMessagesDeleted"));
				flag = true;
			}
		} catch (Exception e) {
			facesUtil.addFacesMsg(FacesMessage.SEVERITY_ERROR, null, "validate delete : " + e.getMessage(), "");
		}

		return flag;
	}
	
	//add 15-11-2023
	private String replaceDelete(String kalimat, String noDocument, String judul,String users) {
		if (StringUtils.isNotBlank(noDocument)) {
			kalimat = kalimat.replace("{nomor_document}", noDocument);
		}
		if (StringUtils.isNotBlank(judul)) {
			kalimat = kalimat.replace("{judul_peraturan}", judul);
		}
		if (StringUtils.isNotBlank(users)) {
			kalimat = kalimat.replace("{user_name}", users);
		}
		return kalimat;
		 
	}

	public void delete(Long deleteId) {
		try {
			if (!validateDeleted(deleteId)) {
				Regulation dt = regulationService.findById(deleteId);
				dt.setEnabledFlag(Constants.CONSTANT_YES);
				dt.setNameIn(dt.getNameIn());
				dt.setLastUpdateBy(facesUtil.retrieveUserLogin());
				dt.setLastUpdateDate(new Timestamp(new Date().getTime()));
				RegulationMst regMst = regulationMstService.findById(deleteId);
				if(regMst!=null && regMst.getRegulationId()!=null){
					dt.setNameIn(dt.getNameIn().concat(" (deleted)"));
					dt.setStatus(ParameterDetail.PARAM_DET_CODE_DATA_NEW);
				} 
				regulationService.update(dt);
				
				//add 15-11-2023
				ParameterDetail pd = parameterDetailService.getParameterDetailByParamDtlCode("ACTIVITY_TYPE_PERATURAN_INTERNAL");
				LogActivity la = new LogActivity();
				la.setUser(getUserLogin());
				la.setActivityType(pd.getParameterDtlCode());
				la.setActivityDate(new Timestamp(new Date().getTime()));
				ParameterDetail activityDate = parameterDetailService
						.getParameterDetailByParamDtlCode("LOG_ACT_PER_INTERNAL_DELETED");
				String str = activityDate.getNameIn();
				String hasil = replaceDelete(str, dt.getDocumentNo(), dt.getNameIn(),
						facesUtil.getUserLogin().getName());
				la.setActivityNote(hasil);
				if (StringUtils.isBlank(la.getCreatedBy())) {
					EntityUtil.setCreationInfo(la, facesUtil.retrieveUserLogin());
				} else {
					EntityUtil.setUpdateInfo(la, facesUtil.retrieveUserLogin());
				}
				logActivityService.save(la);
				
				facesUtil.addSuccessMsg(facesUtil.retrieveMessage("deleteSuccess"));
			}
		} catch (Exception e) {
			facesUtil.addFacesMsg(FacesMessage.SEVERITY_ERROR, null, "Operation Failed : " + e.getMessage(), "");
		}
	}

	public void downloadFile(String fileId, String fileName, byte[] content) throws Exception {
		CallApiManager.downloadFile(fileId, fileName, content, parameterDetailService);
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
	
	public String toEncrypt(Long id){
		try {
			ParameterDetail pd = parameterDetailService.getParameterDetailByParamDtlCode("HOST_NAME_APPLICATION");
			return pd.getNameIn().concat("pages/internalRegulationFE/internalRegulationFEView.faces?token=").concat(Constants.encryptString(id.toString()));
		} catch (Exception e) {
			e.printStackTrace();
			return "";
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

	public String getInternalRegulationSearch() {
		return internalRegulationSearch;
	}

	public void setInternalRegulationSearch(String internalRegulationSearch) {
		this.internalRegulationSearch = internalRegulationSearch;
	}

	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		InternalRegulationBean.logger = logger;
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

	public InternalRegulationService getInternalRegulationService() {
		return internalRegulationService;
	}

	public void setInternalRegulationService(InternalRegulationService internalRegulationService) {
		this.internalRegulationService = internalRegulationService;
	}

	public RegulationService getRegulationService() {
		return regulationService;
	}

	public void setRegulationService(RegulationService regulationService) {
		this.regulationService = regulationService;
	}

	public List<InternalRegulation> getInternalRegulationList() {
		return internalRegulationList;
	}

	public void setInternalRegulationList(List<InternalRegulation> internalRegulationList) {
		this.internalRegulationList = internalRegulationList;
	}

	public DBLazyDataModel<InternalRegulation> getTableModel() {
		return tableModel;
	}

	public void setTableModel(DBLazyDataModel<InternalRegulation> tableModel) {
		this.tableModel = tableModel;
	}

	public String getNavigateEdit() {
		return navigateEdit;
	}

	public void setNavigateEdit(String navigateEdit) {
		this.navigateEdit = navigateEdit;
	}

	@Override
	public void itemSelected(String clientId, String widgetVar, Object selectedItem) {

	}

	public String getPublisherUnit() {
		return publisherUnit;
	}

	public void setPublisherUnit(String publisherUnit) {
		this.publisherUnit = publisherUnit;
	}

	public String getLocalLanguange() {
		return localLanguange;
	}

	public void setLocalLanguange(String localLanguange) {
		this.localLanguange = localLanguange;
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

	public RegulationMstService getRegulationMstService() {
		return regulationMstService;
	}

	public void setRegulationMstService(RegulationMstService regulationMstService) {
		this.regulationMstService = regulationMstService;
	}

	public LogActivity getLogActivity() {
		return logActivity;
	}

	public void setLogActivity(LogActivity logActivity) {
		this.logActivity = logActivity;
	}

	public LogActivityService getLogActivityService() {
		return logActivityService;
	}

	public void setLogActivityService(LogActivityService logActivityService) {
		this.logActivityService = logActivityService;
	}
	
	
	

}