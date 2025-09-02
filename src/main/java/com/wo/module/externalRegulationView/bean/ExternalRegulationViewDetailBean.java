package com.wo.module.externalRegulationView.bean;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.model.UploadedFileWO;
import com.wo.module.common.paging.DefaultSearchObject;
import com.wo.module.common.util.FileUtil;
import com.wo.module.common.utility.CallApiManager;
import com.wo.module.documentCategory.model.DocumentCategory;
import com.wo.module.documentCategory.service.DocumentCategoryService;
import com.wo.module.documentTopic.constant.DocumentTopicConstants;
import com.wo.module.documentTopic.model.DocumentTopic;
import com.wo.module.documentTopic.service.DocumentTopicService;
import com.wo.module.documentType.model.DocumentType;
import com.wo.module.documentType.service.DocumentTypeService;
import com.wo.module.externalRegulation.model.RegulationAttachmentMst;
import com.wo.module.externalRegulation.model.RegulationMst;
import com.wo.module.externalRegulation.model.RegulationTrackRecordMst;
import com.wo.module.externalRegulation.model.RegulationTrackRecordMstTableModel;
import com.wo.module.externalRegulation.service.RegulationMstService;
import com.wo.module.externalRegulationView.constant.ExternalRegulationViewConstants;
import com.wo.module.externalRegulationView.service.ExternalRegulationViewService;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.lov.bean.SelectorModel.SelectorInfo;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.parameter.model.ParameterHeader;
import com.wo.module.parameter.service.ParameterDetailService;

public class ExternalRegulationViewDetailBean extends CommonBean implements Serializable {
	
	private static final long serialVersionUID = 13353217835588379L;

	static Logger logger = Logger.getLogger(ExternalRegulationViewDetailBean.class);

	private RegulationMst regulationMst;

	private Boolean isViewOnly;

	private String actionMode;

	private String editedId;

	private Long docTypeId;

	private Long docCategoryId;

	private Long docTopicId;

	private List<SelectItem> provTypes;

	private List<SelectItem> trackCodes;

	private List<SelectItem> docTypes;

	private List<SelectItem> docCategories;

	private List<SelectItem> docTopics;

	private List<UploadedFileWO> uploadedFilesPeraturanId;

	private List<UploadedFileWO> uploadedFilesPeraturanEn;

	private List<UploadedFileWO> uploadedFilesSummary;

	private List<UploadedFileWO> uploadedFilesPenjelasan;

	private List<UploadedFileWO> uploadedFilesFAQ;

	private List<UploadedFileWO> uploadedFilesLampiran;

	private List<UploadedFileWO> uploadedFilesBuletin;

	private List<UploadedFileWO> uploadedFilesMateri;

	private SelectorInfo selectorJdlPeraturan;

	private RegulationTrackRecordMst[] selectedData;

	private RegulationTrackRecordMstTableModel<RegulationTrackRecordMst> tableModel;

	private RegulationTrackRecordMst selectedRow;
	
	private Integer indexDtl;

	private ExternalRegulationViewService externalRegulationViewService;

	private RegulationMstService regulationMstService;

	private DocumentTypeService documentTypeService;

	private DocumentCategoryService documentCategoryService;

	private DocumentTopicService documentTopicService;

	public FacesUtil facesUtil;
	
	private FileUtil fileUtil;

	private String navigateSearch = ExternalRegulationViewConstants.NAVIGATE_SEARCH;

	SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");

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
		selectTrackCode();
		selectDocType();
		selectDocCategory();
		selectDocTopic();

		checkNewOrEdit();
		fileUtil = FileUtil.getInstance();
	}

	public void selectProvType() {
		provTypes = new ArrayList<SelectItem>();
		try {
			List<ParameterDetail> pd = parameterDetailService.getParameterDetailByParamCode(ParameterHeader.PARAM_HEAD_CODE_JENIS_KETENTUAN);
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

	public void selectTrackCode() {
		trackCodes = new ArrayList<SelectItem>();
		try {
			List<ParameterDetail> pd = parameterDetailService.getParameterDetailByParamCode(ParameterHeader.PARAM_HEAD_CODE_TRACK_RECORD);
			for (int i = 0; i < pd.size(); i++) {
				SelectItem si = new SelectItem();
				si.setLabel(((ParameterDetail) pd.get(i)).getName());
				si.setValue(((ParameterDetail) pd.get(i)).getParameterDtlCode());
				trackCodes.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void selectDocType() {
		docTypes = new ArrayList<SelectItem>();
		try {
			List<DocumentType> pd = documentTypeService.searchData(
					Arrays.asList(new DefaultSearchObject("JENIS_KETENTUAN_EKSTERNAL", null)), 0, Integer.MAX_VALUE,
					null, null);
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
					Arrays.asList(new DefaultSearchObject("JENIS_KETENTUAN_EKSTERNAL", null)), 0, Integer.MAX_VALUE,
					null, null);
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

	public void selectDocTopic() {
		docTopics = new ArrayList<SelectItem>();
		try {
			List<DocumentTopic> pd = documentTopicService.searchData(
					Arrays.asList(new DefaultSearchObject(DocumentTopicConstants.WHERE_JENIS_KETENTUAN_CODE, ParameterDetail.PARAM_DET_CODE_KETENTUAN_EXTERNAL)), 0,
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
		try {
			regulationMst = new RegulationMst();
			List<RegulationTrackRecordMst> trList = new ArrayList<RegulationTrackRecordMst>();
			ParameterDetail pd = parameterDetailService.getParameterDetailByParamDtlCode(ParameterDetail.PARAM_DET_CODE_KETENTUAN_EXTERNAL);
			regulationMst.setJenisKetentuan(pd);

			RegulationTrackRecordMst rt = new RegulationTrackRecordMst();
			trList.add(rt);
			rt.setSequence(1);
			regulationMst.setRegulationTrackRecords(trList);

			actionMode = Constants.ACTION_ADD;
			tableModel = new RegulationTrackRecordMstTableModel<RegulationTrackRecordMst>(
					regulationMst.getRegulationTrackRecords());
			facesUtil.setSessionAttribute("token", null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void handleEdit(String editId) {
		String token = facesUtil.retrieveRequestParam("token");
		if(StringUtils.isNotEmpty(token)) {
			editId = Constants.decryptString(token);
		}
		facesUtil.setSessionAttribute("token", Constants.encryptString(editId));
		actionMode = Constants.ACTION_EDIT;
		Long idLong = Long.parseLong(editId);
		regulationMst = regulationMstService.findById(idLong);
		
		if(regulationMst.getPublishedDate()!=null) {
			regulationMst.setPublishedDateStr(sdf.format(regulationMst.getPublishedDate()));
		}
		
		if(regulationMst.getEffectiveDate()!=null) {
			regulationMst.setEffectiveDateStr(sdf.format(regulationMst.getEffectiveDate()));
		}
		
		if(regulationMst.getExpiredDate()!=null) {
			regulationMst.setExpiredDateStr(sdf.format(regulationMst.getExpiredDate()));
		}
		
		if(regulationMst.getDocumentType()!=null && regulationMst.getDocumentType().getDocumentTypeId()!=null) {
			docTypeId = regulationMst.getDocumentType().getDocumentTypeId();
		}
		if(regulationMst.getDocumentCategory()!=null && regulationMst.getDocumentCategory().getDocumentCategoryId()!=null) {
			docCategoryId = regulationMst.getDocumentCategory().getDocumentCategoryId();
		}
		if(regulationMst.getDocumentTopic()!=null && regulationMst.getDocumentTopic().getDocumentTopicId()!=null) {
			docTopicId = regulationMst.getDocumentTopic().getDocumentTopicId();
		}
		
		tableModel = new RegulationTrackRecordMstTableModel<RegulationTrackRecordMst>(regulationMst.getRegulationTrackRecords());		
		uploadedFilesPeraturanId = new ArrayList<UploadedFileWO>();
		uploadedFilesPeraturanEn = new ArrayList<UploadedFileWO>();
		uploadedFilesSummary = new ArrayList<UploadedFileWO>();
		uploadedFilesPenjelasan = new ArrayList<UploadedFileWO>();
		uploadedFilesFAQ = new ArrayList<UploadedFileWO>();
		uploadedFilesLampiran = new ArrayList<UploadedFileWO>();
		uploadedFilesBuletin = new ArrayList<UploadedFileWO>();
		uploadedFilesMateri = new ArrayList<UploadedFileWO>();
		
		for(int i=0; i<regulationMst.getRegulationAttachments().size();i++) {
			RegulationAttachmentMst ra= regulationMst.getRegulationAttachments().get(i);
			UploadedFileWO uf = new UploadedFileWO();
			uf.setFileName(ra.getAttachmentFile());
			uf.setFileId(ra.getFileId());
			uf.setFileSize(ra.getFileSize());
			
			if(ra.getAttachmentCode()!=null && ra.getAttachmentCode().equals(ParameterDetail.PARAM_DET_CODE_ATTACHMENT_REGULATIONS_IN)) {
				uploadedFilesPeraturanId.add(uf);
			}
			else if(ra.getAttachmentCode()!=null && ra.getAttachmentCode().equals(ParameterDetail.PARAM_DET_CODE_ATTACHMENT_SUMMARY)) {
				uploadedFilesSummary.add(uf);
			}
			else if(ra.getAttachmentCode()!=null && ra.getAttachmentCode().equals(ParameterDetail.PARAM_DET_CODE_ATTACHMENT_EXPLAINATION)) {
				uploadedFilesPenjelasan.add(uf);
			}
			else if(ra.getAttachmentCode()!=null && ra.getAttachmentCode().equals(ParameterDetail.PARAM_DET_CODE_ATTACHMENT_FAQ)) {
				uploadedFilesFAQ.add(uf);
			}
			else if(ra.getAttachmentCode()!=null && ra.getAttachmentCode().equals(ParameterDetail.PARAM_DET_CODE_ATTACHMENT_LAMPIRAN)) {
				uploadedFilesLampiran.add(uf);
			}
			else if(ra.getAttachmentCode()!=null && ra.getAttachmentCode().equals(ParameterDetail.PARAM_DET_CODE_ATTACHMENT_MATERIAL)) {
				uploadedFilesMateri.add(uf);
			}
		}
		
		for(int i=0; i<regulationMst.getRegulationTrackRecords().size();i++) {
			RegulationTrackRecordMst rtr = (RegulationTrackRecordMst)regulationMst.getRegulationTrackRecords().get(i);
			if(rtr.getRegulationLinkId()!=null) {
//				Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
//				String name= "";
				
				RegulationMst r = regulationMstService.findById(rtr.getRegulationLinkId());
//				if (locale != null && locale.equals(locale.ENGLISH)) {
//					name = r.getNameEn();
//				} else {
//					name = r.getNameIn();
//				}
//				rtr.setRegulationLinkName(name);
				rtr.setRegulationLinkName(r.getDocumentNo());
			}
		}

	}
	
	public void back() {
		try {
			facesUtil.redirect("/pages/externalRegulationView/externalRegulationView.faces");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void downloadFile(String fileId, String fileName, byte[] content) throws Exception {
		CallApiManager.downloadFile(fileId, fileName, content, parameterDetailService);
	}

	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		ExternalRegulationViewDetailBean.logger = logger;
	}

	public RegulationMst getRegulationMst() {
		return regulationMst;
	}

	public void setRegulationMst(RegulationMst regulationMst) {
		this.regulationMst = regulationMst;
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

	public Long getDocTypeId() {
		return docTypeId;
	}

	public void setDocTypeId(Long docTypeId) {
		this.docTypeId = docTypeId;
	}

	public Long getDocCategoryId() {
		return docCategoryId;
	}

	public void setDocCategoryId(Long docCategoryId) {
		this.docCategoryId = docCategoryId;
	}

	public Long getDocTopicId() {
		return docTopicId;
	}

	public void setDocTopicId(Long docTopicId) {
		this.docTopicId = docTopicId;
	}

	public List<SelectItem> getProvTypes() {
		return provTypes;
	}

	public void setProvTypes(List<SelectItem> provTypes) {
		this.provTypes = provTypes;
	}

	public List<SelectItem> getTrackCodes() {
		return trackCodes;
	}

	public void setTrackCodes(List<SelectItem> trackCodes) {
		this.trackCodes = trackCodes;
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

	

	public List<UploadedFileWO> getUploadedFilesPeraturanId() {
		return uploadedFilesPeraturanId;
	}

	public void setUploadedFilesPeraturanId(List<UploadedFileWO> uploadedFilesPeraturanId) {
		this.uploadedFilesPeraturanId = uploadedFilesPeraturanId;
	}

	public List<UploadedFileWO> getUploadedFilesPeraturanEn() {
		return uploadedFilesPeraturanEn;
	}

	public void setUploadedFilesPeraturanEn(List<UploadedFileWO> uploadedFilesPeraturanEn) {
		this.uploadedFilesPeraturanEn = uploadedFilesPeraturanEn;
	}

	public List<UploadedFileWO> getUploadedFilesSummary() {
		return uploadedFilesSummary;
	}

	public void setUploadedFilesSummary(List<UploadedFileWO> uploadedFilesSummary) {
		this.uploadedFilesSummary = uploadedFilesSummary;
	}

	public List<UploadedFileWO> getUploadedFilesPenjelasan() {
		return uploadedFilesPenjelasan;
	}

	public void setUploadedFilesPenjelasan(List<UploadedFileWO> uploadedFilesPenjelasan) {
		this.uploadedFilesPenjelasan = uploadedFilesPenjelasan;
	}

	public List<UploadedFileWO> getUploadedFilesFAQ() {
		return uploadedFilesFAQ;
	}

	public void setUploadedFilesFAQ(List<UploadedFileWO> uploadedFilesFAQ) {
		this.uploadedFilesFAQ = uploadedFilesFAQ;
	}

	public List<UploadedFileWO> getUploadedFilesLampiran() {
		return uploadedFilesLampiran;
	}

	public void setUploadedFilesLampiran(List<UploadedFileWO> uploadedFilesLampiran) {
		this.uploadedFilesLampiran = uploadedFilesLampiran;
	}

	public List<UploadedFileWO> getUploadedFilesBuletin() {
		return uploadedFilesBuletin;
	}

	public void setUploadedFilesBuletin(List<UploadedFileWO> uploadedFilesBuletin) {
		this.uploadedFilesBuletin = uploadedFilesBuletin;
	}

	public List<UploadedFileWO> getUploadedFilesMateri() {
		return uploadedFilesMateri;
	}

	public void setUploadedFilesMateri(List<UploadedFileWO> uploadedFilesMateri) {
		this.uploadedFilesMateri = uploadedFilesMateri;
	}

	public SelectorInfo getSelectorJdlPeraturan() {
		return selectorJdlPeraturan;
	}

	public void setSelectorJdlPeraturan(SelectorInfo selectorJdlPeraturan) {
		this.selectorJdlPeraturan = selectorJdlPeraturan;
	}

	public RegulationTrackRecordMst[] getSelectedData() {
		return selectedData;
	}

	public void setSelectedData(RegulationTrackRecordMst[] selectedData) {
		this.selectedData = selectedData;
	}

	public RegulationTrackRecordMstTableModel<RegulationTrackRecordMst> getTableModel() {
		return tableModel;
	}

	public void setTableModel(RegulationTrackRecordMstTableModel<RegulationTrackRecordMst> tableModel) {
		this.tableModel = tableModel;
	}

	public RegulationTrackRecordMst getSelectedRow() {
		return selectedRow;
	}

	public void setSelectedRow(RegulationTrackRecordMst selectedRow) {
		this.selectedRow = selectedRow;
	}

	public Integer getIndexDtl() {
		return indexDtl;
	}

	public void setIndexDtl(Integer indexDtl) {
		this.indexDtl = indexDtl;
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

	public FacesUtil getFacesUtil() {
		return facesUtil;
	}

	public void setFacesUtil(FacesUtil facesUtil) {
		this.facesUtil = facesUtil;
	}

	public String getNavigateSearch() {
		return navigateSearch;
	}

	public void setNavigateSearch(String navigateSearch) {
		this.navigateSearch = navigateSearch;
	}

	public SimpleDateFormat getSdf() {
		return sdf;
	}

	public void setSdf(SimpleDateFormat sdf) {
		this.sdf = sdf;
	}

	public FileUtil getFileUtil() {
		return fileUtil;
	}

	public void setFileUtil(FileUtil fileUtil) {
		this.fileUtil = fileUtil;
	}

	
}