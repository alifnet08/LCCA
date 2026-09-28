package com.wo.module.notary.bean;

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
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.model.UploadedFileWO;
import com.wo.module.common.paging.DBLazyDataModel;
import com.wo.module.common.paging.DefaultSearchObject;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.common.util.FileUtil;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.notary.constant.NotaryConstants;
import com.wo.module.notary.model.Notary;
import com.wo.module.notary.service.NotaryService;
import com.wo.module.notary.vo.NotaryVo;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.parameter.model.ParameterHeader;

public class NotaryBean extends CommonBean  implements Serializable {

	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(NotaryBean.class);

	private String area;	
	private String notaryName;	
	private String areaCode;

	private int paging;

	private boolean flagError;
	
	private NotaryService notaryService;

	private DBLazyDataModel<Notary> tableModel;
	
	private List<SelectItem> categoryList;	
	private List<SelectItem> statusList;
	private List<SelectItem> errorList;

	public FacesUtil facesUtil;
	
	private List<UploadedFileWO> uploadedFilesAttachment;	
	private List<UploadedFileWO> uploadedFiles;
	
	private FileUploadEvent fileUploadEvent;

	private String navigateEdit = NotaryConstants.NAVIGATE_EDIT;

	private List<Notary> perpanjanganList;
	private Notary selectedNotary;
	private String pickerJenisPengajuan;

	public void addMessage(String summary) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, null);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public void addErrMessage(String summary) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, null);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}	
	
	public StreamedContent getDataPostProcessXLS() {
		StreamedContent downloadExcelSc = null;
		try {
			List<Notary> listDataXls = notaryService.searchData(
					Arrays.asList(
							new DefaultSearchObject(NotaryConstants.SEARCH_BY_AREA, area),
							new DefaultSearchObject(NotaryConstants.SEARCH_BY_NOTARY_NAME, notaryName),
							new DefaultSearchObject(NotaryConstants.SEARCH_BY_AREA_CODE, areaCode)),
					0, Integer.MAX_VALUE, null, null);
			
			downloadExcelSc = notaryService.generateDataExcel(listDataXls, categoryList);
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return downloadExcelSc;
	}
	
	@PostConstruct
	public void init() {
		super.init();
		initList();
		paging = Constants.DEFAULT_PAGING_NUMBER;
		tableModel = new DBLazyDataModel<Notary>(notaryService, paging);
		flagError = false;
	}
	
	public void initList(){
		try {
		categoryList = new ArrayList<SelectItem>();
		List<ParameterDetail> listCategory = parameterDetailService
					.getParameterDetailByParamCode(ParameterHeader.PARAM_HEAD_NOTARY_CATEGORY);
		

		for (ParameterDetail vo : listCategory) {
			SelectItem si = new SelectItem();
			si.setLabel(vo.getNameIn());
			si.setValue(vo.getParameterDtlCode());
			categoryList.add(si);
		}
		
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@SuppressWarnings("rawtypes")
	public void search(ActionEvent actionEvent) {
	
		List<SearchObject> searchCriteria = new ArrayList<SearchObject>();
		if (area != null && !area.isEmpty()) {
			searchCriteria.add(new DefaultSearchObject(NotaryConstants.SEARCH_BY_AREA, area));
		}
		if (notaryName != null && !notaryName.isEmpty()) {
			searchCriteria.add(new DefaultSearchObject(NotaryConstants.SEARCH_BY_NOTARY_NAME, notaryName));
		}
		if (areaCode != null && !areaCode.isEmpty()) {
			searchCriteria.add(new DefaultSearchObject(NotaryConstants.SEARCH_BY_AREA_CODE, areaCode));
		}

		tableModel.setSearchCriteria(searchCriteria);
		
	}

	public void reset(ActionEvent actionEvent) {
		area = "";
		notaryName = "";
		areaCode = "";
		
		search(actionEvent);
	}

	public void delete(Long deleteId) {
		try {			
			
			Notary entity = notaryService.findById(deleteId);
			entity.setEnabledFlag(Constants.CONSTANT_NO);
			entity.setLastUpdateBy(facesUtil.retrieveUserLogin());
			entity.setLastUpdateDate(new Timestamp(new Date().getTime()));
			
			notaryService.update(entity);
			facesUtil.addSuccessMsg(facesUtil.retrieveMessage("deleteSuccess"));
			
			
		} catch (Exception e) {
			facesUtil.addFacesMsg(FacesMessage.SEVERITY_ERROR, null, "Operation Failed : " + e.getMessage(), "");
		}
	}
	
	public void handleFileUpload(FileUploadEvent event) throws Exception {
		try {
			if(uploadedFilesAttachment == null)
				uploadedFilesAttachment = new ArrayList<UploadedFileWO>();
			
			fileUploadEvent = event;
			String ext = FileUtil.getExtention(fileUploadEvent.getFile().getFileName()); 
			UploadedFile file = event.getFile();
			if (StringUtils.equalsIgnoreCase("xls", ext) || StringUtils.equalsIgnoreCase("xlsx", ext)) { 						
				NotaryVo notaryDataVo = notaryService.saveUpload(file, facesUtil);
				if(notaryDataVo.getErrorList() !=null && notaryDataVo.getErrorList().size() > 0) {					
					errorList = notaryDataVo.getErrorList();
					flagError = true;		
					PrimeFaces.current().ajax().update("form:outputPanelError");
					facesUtil.addErrMessage(facesUtil.retrieveMessage("formNotaryError"));
				}else {
					for(Notary notary : notaryDataVo.getNotaryList()) {
						if(notary.getNotaryId() !=null && notary.getNotaryId() > 0) {
							notaryService.update(notary);
						}else {
							notaryService.save(notary);
						}
					}
					
					search(null);
					PrimeFaces.current().ajax().update("form:tableSearch");		
					flagError = false;		
					PrimeFaces.current().ajax().update("form:outputPanelError");
					facesUtil.addSuccessMsg(facesUtil.retrieveMessage("formNotarySuccess"));
				}
				
			}else {
				facesUtil.addErrMessage(facesUtil.retrieveMessage("textInfoUploadExcel"));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			facesUtil.addErrMessage(e.getMessage());
		}
	}
	
	public StreamedContent getDataError() {
		StreamedContent dataErrorExcel = null;
		try {			
			dataErrorExcel = notaryService.generateDataError(errorList);
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return dataErrorExcel;
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

	

	public NotaryService getNotaryService() {
		return notaryService;
	}

	public void setNotaryService(NotaryService notaryService) {
		this.notaryService = notaryService;
	}

	public DBLazyDataModel<Notary> getTableModel() {
		return tableModel;
	}

	public void setTableModel(DBLazyDataModel<Notary> tableModel) {
		this.tableModel = tableModel;
	}

	public int getPaging() {
		return paging;
	}

	public void setPaging(int paging) {
		this.paging = paging;
	}


	public void openPerpanjangan() {
		pickerJenisPengajuan = NotaryConstants.JENIS_PENGAJUAN_PERPANJANGAN;
		selectedNotary = null;
		try {
			perpanjanganList = notaryService.searchData(new ArrayList<SearchObject>(), 0, Integer.MAX_VALUE, null, null);
		} catch (Exception e) {
			e.printStackTrace();
			addErrMessage("Operation Failed : " + e.getMessage());
		}
	}

	public void openUpdateDokumen() {
		pickerJenisPengajuan = NotaryConstants.JENIS_PENGAJUAN_UPDATE_DOKUMEN;
		selectedNotary = null;
		try {
			perpanjanganList = notaryService.searchData(new ArrayList<SearchObject>(), 0, Integer.MAX_VALUE, null, null);
		} catch (Exception e) {
			e.printStackTrace();
			addErrMessage("Operation Failed : " + e.getMessage());
		}
	}

	public void onPickerRowSelect(SelectEvent event) {
		if (event != null && event.getObject() instanceof Notary) {
			selectedNotary = (Notary) event.getObject();
		}
	}

	public String navigateTambahNotaris() {
		facesUtil.setSessionAttribute(NotaryConstants.SESSION_JENIS_PENGAJUAN, null);
		return navigateEdit + "?faces-redirect=true";
	}

	public void navigatePerpanjangan() {
		try {
			if (selectedNotary == null || selectedNotary.getNotaryId() == null) {
				addErrMessage("Pilih Notaris");
				return;
			}
			String jenisPengajuan = pickerJenisPengajuan;
			if (StringUtils.isBlank(jenisPengajuan)) {
				jenisPengajuan = NotaryConstants.JENIS_PENGAJUAN_PERPANJANGAN;
			}
			facesUtil.setSessionAttribute(NotaryConstants.SESSION_JENIS_PENGAJUAN, jenisPengajuan);
			facesUtil.redirect("/pages/notary/notaryEdit.faces?id=" + selectedNotary.getNotaryId()
					+ "&jenisPengajuan=" + jenisPengajuan.replace(" ", "%20"));
		} catch (Exception e) {
			addErrMessage("Operation Failed : " + e.getMessage());
		}
	}

	public String getNavigateEdit() {
		facesUtil.setSessionAttribute(NotaryConstants.SESSION_JENIS_PENGAJUAN, null);
		return navigateEdit;
	}

	public void setNavigateEdit(String navigateEdit) {
		this.navigateEdit = navigateEdit;
	}
	
	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		NotaryBean.logger = logger;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public List<SelectItem> getCategoryList() {
		return categoryList;
	}

	public void setCategoryList(List<SelectItem> categoryList) {
		this.categoryList = categoryList;
	}


	public List<SelectItem> getStatusList() {
		return statusList;
	}

	public void setStatusList(List<SelectItem> statusList) {
		this.statusList = statusList;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getNotaryName() {
		return notaryName;
	}

	public void setNotaryName(String notaryName) {
		this.notaryName = notaryName;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public List<UploadedFileWO> getUploadedFilesAttachment() {
		return uploadedFilesAttachment;
	}

	public void setUploadedFilesAttachment(List<UploadedFileWO> uploadedFilesAttachment) {
		this.uploadedFilesAttachment = uploadedFilesAttachment;
	}

	public List<UploadedFileWO> getUploadedFiles() {
		return uploadedFiles;
	}

	public void setUploadedFiles(List<UploadedFileWO> uploadedFiles) {
		this.uploadedFiles = uploadedFiles;
	}

	public FileUploadEvent getFileUploadEvent() {
		return fileUploadEvent;
	}

	public void setFileUploadEvent(FileUploadEvent fileUploadEvent) {
		this.fileUploadEvent = fileUploadEvent;
	}

	public List<SelectItem> getErrorList() {
		return errorList;
	}

	public void setErrorList(List<SelectItem> errorList) {
		this.errorList = errorList;
	}

	public boolean isFlagError() {
		return flagError;
	}

	public void setFlagError(boolean flagError) {
		this.flagError = flagError;
	}

	public List<Notary> getPerpanjanganList() {
		return perpanjanganList;
	}

	public void setPerpanjanganList(List<Notary> perpanjanganList) {
		this.perpanjanganList = perpanjanganList;
	}

	public Notary getSelectedNotary() {
		return selectedNotary;
	}

	public void setSelectedNotary(Notary selectedNotary) {
		this.selectedNotary = selectedNotary;
	}
	
}