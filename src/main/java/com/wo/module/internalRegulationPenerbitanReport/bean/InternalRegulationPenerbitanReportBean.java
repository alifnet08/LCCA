package com.wo.module.internalRegulationPenerbitanReport.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.apache.log4j.Logger;
import org.primefaces.PrimeFaces;
import org.primefaces.model.StreamedContent;

import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.paging.DBLazyDataModel;
import com.wo.module.common.paging.DefaultSearchObject;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.common.util.FileUtil;
import com.wo.module.internalRegulationPenerbitanReport.constant.InternalRegulationPenerbitanReportConstants;
import com.wo.module.internalRegulationPenerbitanReport.model.InternalRegulationPenerbitanReport;
import com.wo.module.internalRegulationPenerbitanReport.service.InternalRegulationPenerbitanReportService;
import com.wo.module.internalRegulationPenerbitanReport.vo.InternalRegulationPenerbitanPicIrgReportVo;
import com.wo.module.internalRegulationPenerbitanReport.vo.InternalRegulationPenerbitanReportVo;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.lov.bean.SelectorListener;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.user.model.Division;
import com.wo.module.user.service.UserService;

public class InternalRegulationPenerbitanReportBean extends CommonBean
		implements SelectorListener<Object>, Serializable {

	private static final long serialVersionUID = 346804904969309465L;

	static Logger logger = Logger.getLogger(InternalRegulationPenerbitanReportBean.class);

	public FacesUtil facesUtil;
	private FileUtil fileUtil;

	private InternalRegulationPenerbitanReport irgReport;

	private int paging;

	private String localLanguange;

	private List<SelectItem> direktoratTpgs;
	private List<SelectItem> unitKerjaTpgs;
	private List<SelectItem> statusFeedbacks;
	private List<SelectItem> noRegulations;
	private List<SelectItem> typeRegulations;
	private List<SelectItem> statusRegulations;
	private List<SelectItem> picIrgs;

	private DBLazyDataModel<InternalRegulationPenerbitanReportVo> tableModel;

	private InternalRegulationPenerbitanReportService internalRegulationPenerbitanReportService;

	private UserService userService;

	public void addMessage(String summary) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, null);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public void addErrMessage(String summary) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, null);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	@SuppressWarnings("static-access")
	@PostConstruct
	public void init() {
		super.init();
		irgReport = new InternalRegulationPenerbitanReport();
		paging = Constants.DEFAULT_PAGING_NUMBER;
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		localLanguange = "IN";
		if (locale != null && locale.equals(locale.ENGLISH)) {
			localLanguange = "EN";
		}
		fileUtil = FileUtil.getInstance();
		searchData();
		selectUnitKerja();
		tableModel = new DBLazyDataModel<InternalRegulationPenerbitanReportVo>(
				internalRegulationPenerbitanReportService, paging);
		dataDefault();
	}

	public void searchData() {
		typeRegulations = new ArrayList<SelectItem>();
		try {
			List<ParameterDetail> pd = parameterDetailService.getParameterDetailByParamCode("TYPE_REGULATION");
			for (int i = 0; i < pd.size(); i++) {
				SelectItem si = new SelectItem();
				si.setLabel(((ParameterDetail) pd.get(i)).getName());
				si.setValue(((ParameterDetail) pd.get(i)).getParameterDtlCode());
				typeRegulations.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		statusRegulations = new ArrayList<SelectItem>();
		try {
			List<ParameterDetail> pd = parameterDetailService
					.getParameterDetailByParamCode("STATUS_REGULATION_OBSOLETE");
			for (int i = 0; i < pd.size(); i++) {
				SelectItem si = new SelectItem();
				si.setLabel(((ParameterDetail) pd.get(i)).getName());
				si.setValue(((ParameterDetail) pd.get(i)).getParameterDtlCode());
				statusRegulations.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		statusFeedbacks = new ArrayList<SelectItem>();
		try {
			List<ParameterDetail> pd = parameterDetailService.getParameterDetailByParamCode("STATUS_PROCESS");
			for (int i = 0; i < pd.size(); i++) {
				SelectItem si = new SelectItem();
				si.setLabel(((ParameterDetail) pd.get(i)).getName());
				si.setValue(((ParameterDetail) pd.get(i)).getParameterDtlCode());
				statusFeedbacks.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		direktoratTpgs = new ArrayList<SelectItem>();
		try {
			List<String> dirStrList = userService.getAllDirectorate();
			for (String dirData : dirStrList) {
				SelectItem si = new SelectItem();
				si.setLabel(dirData);
				si.setValue(dirData);
				direktoratTpgs.add(si);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		picIrgs = new ArrayList<SelectItem>();
		try {
			List<InternalRegulationPenerbitanPicIrgReportVo> dataPicIrgs = internalRegulationPenerbitanReportService.getDataPicIrg();
			for (InternalRegulationPenerbitanPicIrgReportVo dataPicIrg : dataPicIrgs) {
				SelectItem si = new SelectItem();
				si.setLabel(dataPicIrg.getPicName1());
				si.setValue(dataPicIrg.getPicNik1());
				picIrgs.add(si);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void selectUnitKerja() {
		unitKerjaTpgs = new ArrayList<SelectItem>();
		try {
			List<Division> pd = userService.getAllDivision();
			for (int i = 0; i < pd.size(); i++) {
				SelectItem si = new SelectItem();
				si.setLabel(((Division) pd.get(i)).getDivisionName());
				si.setValue(((Division) pd.get(i)).getDivisionId());
				unitKerjaTpgs.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		PrimeFaces.current().executeScript("reInitSelect2();");
	}
	
	@SuppressWarnings("rawtypes")
	private void dataDefault() {
		List<SearchObject> searchCriteria = new ArrayList<SearchObject>();
		searchCriteria.add(new DefaultSearchObject(
					InternalRegulationPenerbitanReportConstants.SEARCH_BY_REFERENCE_NO, "REFRENCENODUMMY"));
		
		tableModel.setSearchCriteria(searchCriteria);
		PrimeFaces.current().executeScript("reInitSelect2();");
	}

	@SuppressWarnings({ "rawtypes" })
	public void search() {
		List<SearchObject> searchCriteria = new ArrayList<SearchObject>();
		if (irgReport.getReferenceNo() != null && !irgReport.getReferenceNo().isEmpty()
				&& !irgReport.getReferenceNo().equals(InternalRegulationPenerbitanReportConstants.STRING_EMPTY)) {
			searchCriteria.add(new DefaultSearchObject(
					InternalRegulationPenerbitanReportConstants.SEARCH_BY_REFERENCE_NO, irgReport.getReferenceNo()));
		}
		if (irgReport.getIrgTitle() != null && !irgReport.getIrgTitle().isEmpty()
				&& !irgReport.getIrgTitle().equals(InternalRegulationPenerbitanReportConstants.STRING_EMPTY)) {
			searchCriteria.add(new DefaultSearchObject(
					InternalRegulationPenerbitanReportConstants.SEARCH_BY_REGULATION_TITLE, irgReport.getIrgTitle()));
		}
		if (irgReport.getRegulationNo() != null && !irgReport.getRegulationNo().isEmpty()
				&& !irgReport.getRegulationNo().equals(InternalRegulationPenerbitanReportConstants.STRING_EMPTY)) {
			searchCriteria.add(new DefaultSearchObject(
					InternalRegulationPenerbitanReportConstants.SEARCH_BY_REGULATION_NO, irgReport.getRegulationNo()));
		}
		if (irgReport.getDirectorateTpg() != null && !irgReport.getDirectorateTpg().isEmpty()
				&& !irgReport.getDirectorateTpg().equals(InternalRegulationPenerbitanReportConstants.STRING_EMPTY)) {
			searchCriteria.add(new DefaultSearchObject(
					InternalRegulationPenerbitanReportConstants.SEARCH_BY_DIRECTORATE_TPG, irgReport.getDirectorateTpg()));
		}		
		if (irgReport.getRegulationTypeCode() != null &&  !irgReport.getRegulationTypeCode().isEmpty()
				&& !irgReport.getRegulationTypeCode().equals(InternalRegulationPenerbitanReportConstants.STRING_EMPTY)) {
			searchCriteria.add(new DefaultSearchObject(
					InternalRegulationPenerbitanReportConstants.SEARCH_BY_REGULATION_TYPE, irgReport.getRegulationTypeCode()));
		}
		if (irgReport.getWorkUnitTpgCode() != null &&  !irgReport.getWorkUnitTpgCode().isEmpty()
				&& !irgReport.getWorkUnitTpgCode().equals(InternalRegulationPenerbitanReportConstants.STRING_EMPTY)) {
			searchCriteria.add(new DefaultSearchObject(
					InternalRegulationPenerbitanReportConstants.SEARCH_BY_WORK_UNIT_TPG, irgReport.getWorkUnitTpgCode() ));
		}
		if (irgReport.getRegulationStatusCode() != null &&  !irgReport.getRegulationStatusCode().isEmpty()
				&& !irgReport.getRegulationStatusCode().equals(InternalRegulationPenerbitanReportConstants.STRING_EMPTY)) {
			searchCriteria.add(new DefaultSearchObject(
					InternalRegulationPenerbitanReportConstants.SEARCH_BY_REGULATION_STATUS, irgReport.getRegulationStatusCode()));
		}
		if (irgReport.getPicIrg() != null && !irgReport.getPicIrg().isEmpty()
				&& !irgReport.getPicIrg().equals(InternalRegulationPenerbitanReportConstants.STRING_EMPTY)) {
			searchCriteria.add(new DefaultSearchObject(
					InternalRegulationPenerbitanReportConstants.SEARCH_BY_PIC_IRG, irgReport.getPicIrg()));
		}				
		
		tableModel.setSearchCriteria(searchCriteria);
		
		PrimeFaces.current().executeScript("reInitSelect2();");
	}

	public void resetData(ActionEvent actionEvent) {
		irgReport = new InternalRegulationPenerbitanReport();
		dataDefault();
		
		PrimeFaces.current().executeScript("reInitSelect2();");
	}
	
	public StreamedContent getDownloadExcel() {
		StreamedContent downloadExcelSc = null;
		try {
			downloadExcelSc = internalRegulationPenerbitanReportService.generateDataExcel(irgReport);
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return downloadExcelSc;
	}

	@Override
	public void itemSelected(String clientId, String widgetVar, Object selectedItem) {
		// TODO Auto-generated method stub

	}

	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		InternalRegulationPenerbitanReportBean.logger = logger;
	}

	public FileUtil getFileUtil() {
		return fileUtil;
	}

	public void setFileUtil(FileUtil fileUtil) {
		this.fileUtil = fileUtil;
	}
	
	public InternalRegulationPenerbitanReport getIrgReport() {
		return irgReport;
	}

	public void setIrgReport(InternalRegulationPenerbitanReport irgReport) {
		this.irgReport = irgReport;
	}

	public int getPaging() {
		return paging;
	}

	public void setPaging(int paging) {
		this.paging = paging;
	}

	public String getLocalLanguange() {
		return localLanguange;
	}

	public void setLocalLanguange(String localLanguange) {
		this.localLanguange = localLanguange;
	}

	public List<SelectItem> getDirektoratTpgs() {
		return direktoratTpgs;
	}

	public void setDirektoratTpgs(List<SelectItem> direktoratTpgs) {
		this.direktoratTpgs = direktoratTpgs;
	}

	public List<SelectItem> getUnitKerjaTpgs() {
		return unitKerjaTpgs;
	}

	public void setUnitKerjaTpgs(List<SelectItem> unitKerjaTpgs) {
		this.unitKerjaTpgs = unitKerjaTpgs;
	}

	public List<SelectItem> getStatusFeedbacks() {
		return statusFeedbacks;
	}

	public void setStatusFeedbacks(List<SelectItem> statusFeedbacks) {
		this.statusFeedbacks = statusFeedbacks;
	}

	public List<SelectItem> getNoRegulations() {
		return noRegulations;
	}

	public void setNoRegulations(List<SelectItem> noRegulations) {
		this.noRegulations = noRegulations;
	}

	public List<SelectItem> getTypeRegulations() {
		return typeRegulations;
	}

	public void setTypeRegulations(List<SelectItem> typeRegulations) {
		this.typeRegulations = typeRegulations;
	}

	public List<SelectItem> getStatusRegulations() {
		return statusRegulations;
	}

	public void setStatusRegulations(List<SelectItem> statusRegulations) {
		this.statusRegulations = statusRegulations;
	}

	public DBLazyDataModel<InternalRegulationPenerbitanReportVo> getTableModel() {
		return tableModel;
	}

	public void setTableModel(DBLazyDataModel<InternalRegulationPenerbitanReportVo> tableModel) {
		this.tableModel = tableModel;
	}

	public InternalRegulationPenerbitanReportService getInternalRegulationPenerbitanReportService() {
		return internalRegulationPenerbitanReportService;
	}

	public void setInternalRegulationPenerbitanReportService(
			InternalRegulationPenerbitanReportService internalRegulationPenerbitanReportService) {
		this.internalRegulationPenerbitanReportService = internalRegulationPenerbitanReportService;
	}

	public FacesUtil getFacesUtil() {
		return facesUtil;
	}

	public void setFacesUtil(FacesUtil facesUtil) {
		this.facesUtil = facesUtil;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	public List<SelectItem> getPicIrgs() {
		return picIrgs;
	}

	public void setPicIrgs(List<SelectItem> picIrgs) {
		this.picIrgs = picIrgs;
	}
	

}