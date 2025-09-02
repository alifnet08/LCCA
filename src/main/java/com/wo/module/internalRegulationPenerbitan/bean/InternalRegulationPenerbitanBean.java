package com.wo.module.internalRegulationPenerbitan.bean;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import com.wo.module.common.util.FileUtil;
import com.wo.module.counterType.service.CounterTypeService;
import com.wo.module.internalRegulationPenerbitan.constant.InternalRegulationPenerbitanConstants;
import com.wo.module.internalRegulationPenerbitan.model.InternalRegulationPenerbitan;
import com.wo.module.internalRegulationPenerbitan.service.InternalRegulationPenerbitanService;
import com.wo.module.internalRegulationPenerbitan.vo.InternalRegulationPenerbitanVo;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.lov.bean.SelectorListener;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.user.model.Division;
import com.wo.module.user.model.User;
import com.wo.module.user.service.UserService;

public class InternalRegulationPenerbitanBean extends CommonBean implements SelectorListener<Object>, Serializable {

	private static final long serialVersionUID = 2700708349765517766L;

	static Logger logger = Logger.getLogger(InternalRegulationPenerbitanBean.class);

	private String navigateEdit = InternalRegulationPenerbitanConstants.NAVIGATE_EDIT;
	private String internalRegPenerbitanObsoleteSearch;

	private int paging;
	
	private Long statusRegulasiId;
	private Long tipeRegulasiId;
	private Long unitKerjaTpgId;
	private Long processStatusId;
	
	private String localLanguange;
	private String noReferensi;
	private String judulRegulasi;
	private String picIrgNik;

	public FacesUtil facesUtil;

	private FileUtil fileUtil;
	
	private Date startDate;
	private Date endDate;
	
	private DBLazyDataModel<InternalRegulationPenerbitanVo> tableModel;
	
	private List<SelectItem> statusRegulasis;
	private List<SelectItem> typeRegulasis;
	private List<SelectItem> unitKerjas;
	private List<SelectItem> processStatusList;
	private List<SelectItem> picsIrg;
	
	private CounterTypeService counterTypeService;
	private UserService userService;
	private InternalRegulationPenerbitanService internalRegulationPenerbitanService;
	
	private SimpleDateFormat sdfDateSearch = new SimpleDateFormat("yyyy-MM-dd");

	
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
		paging = Constants.DEFAULT_PAGING_NUMBER;
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		localLanguange = "IN";
		if (locale != null && locale.equals(locale.ENGLISH)) {
			localLanguange = "EN";
		}
		fileUtil = FileUtil.getInstance();
		searchData();
		selectUnitKerja();
		selectPicIrg();
		tableModel = new DBLazyDataModel<InternalRegulationPenerbitanVo>(internalRegulationPenerbitanService, paging);		
	}

	public void searchData() {
		typeRegulasis = new ArrayList<SelectItem>();
		try {
			List<ParameterDetail> pd = parameterDetailService.getParameterDetailByParamCode("TYPE_REGULATION");
			for (int i = 0; i < pd.size(); i++) {
				SelectItem si = new SelectItem();
				si.setLabel(((ParameterDetail) pd.get(i)).getName());
				si.setValue(((ParameterDetail) pd.get(i)).getParameterDtlId());
				typeRegulasis.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		statusRegulasis = new ArrayList<SelectItem>();
		try {
			List<ParameterDetail> pd = parameterDetailService.getParameterDetailByParamCode("STATUS_REGULATION");
			for (int i = 0; i < pd.size(); i++) {
				SelectItem si = new SelectItem();
				si.setLabel(((ParameterDetail) pd.get(i)).getName());
				si.setValue(((ParameterDetail) pd.get(i)).getParameterDtlId());
				statusRegulasis.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		processStatusList = new ArrayList<>();
		try {
			List<ParameterDetail> pd = parameterDetailService.getParameterDetailByParamCode("STATUS_PROCESS");
			for (int i = 0; i < pd.size(); i++) {
				SelectItem si = new SelectItem();
				si.setLabel(pd.get(i).getName());
				si.setValue(pd.get(i).getParameterDtlId());
				processStatusList.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void selectUnitKerja() {
		unitKerjas = new ArrayList<SelectItem>();
		try {
			List<Division> pd = userService.getAllDivision();
			for (int i = 0; i < pd.size(); i++) {
				SelectItem si = new SelectItem();
				si.setLabel(((Division) pd.get(i)).getDivisionName());
				si.setValue(((Division) pd.get(i)).getDivisionId());
				unitKerjas.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		PrimeFaces.current().executeScript("reInitSelect2();");
	}
	
	public void selectPicIrg() {
		picsIrg = new ArrayList<SelectItem>();
		try {
			List<User> createByUsers = internalRegulationPenerbitanService.getPicsIrg();
			for (int i = 0; i < createByUsers.size(); i++) {
				SelectItem si = new SelectItem();
				si.setLabel(createByUsers.get(i).getName());
				si.setValue(createByUsers.get(i).getNik());
				picsIrg.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		PrimeFaces.current().executeScript("reInitSelect2();");
	}

	@SuppressWarnings("rawtypes")
	public void search(ActionEvent event) {
		List<SearchObject> searchCriteria = new ArrayList<SearchObject>();
		if (judulRegulasi != null && !judulRegulasi.isEmpty() && !judulRegulasi.equals(InternalRegulationPenerbitanConstants.STRING_EMPTY)) {
			searchCriteria.add(new DefaultSearchObject(InternalRegulationPenerbitanConstants.SEARCH_BY_REGULATION_TITLE, judulRegulasi));
		}
		if (statusRegulasiId != null && statusRegulasiId > 0) {
			searchCriteria.add(new DefaultSearchObject(InternalRegulationPenerbitanConstants.SEARCH_BY_REGULATION_STATUS, statusRegulasiId));
		}
		if (tipeRegulasiId != null && tipeRegulasiId > 0) {
			searchCriteria.add(new DefaultSearchObject(InternalRegulationPenerbitanConstants.SEARCH_BY_REGULATION_TYPE, tipeRegulasiId));
		}
		if (unitKerjaTpgId != null && unitKerjaTpgId > 0) {
			searchCriteria.add(new DefaultSearchObject(InternalRegulationPenerbitanConstants.SEARCH_BY_WORK_UNIT_TPG, unitKerjaTpgId));
		}
		if (startDate != null) {
			searchCriteria.add(new DefaultSearchObject(InternalRegulationPenerbitanConstants.SEARCH_BY_REGULATION_IN_DATE_START, sdfDateSearch.format(startDate)));
		}
		if (endDate != null) {
			searchCriteria.add(new DefaultSearchObject(InternalRegulationPenerbitanConstants.SEARCH_BY_REGULATION_IN_DATE_END, sdfDateSearch.format(endDate)));
		}
		if (processStatusId != null && processStatusId > 0) {
			searchCriteria.add(new DefaultSearchObject(InternalRegulationPenerbitanConstants.SEARCH_BY_PROCESS_STATUS, processStatusId));
		}
		if (noReferensi != null && !noReferensi.isEmpty() && !noReferensi.equals(InternalRegulationPenerbitanConstants.STRING_EMPTY)) {
			searchCriteria.add(new DefaultSearchObject(InternalRegulationPenerbitanConstants.SEARCH_BY_REFERENCE_NO, noReferensi));
		}
		if (picIrgNik != null && !picIrgNik.isEmpty()) {
			searchCriteria.add(new DefaultSearchObject(InternalRegulationPenerbitanConstants.SEARCH_BY_PIC_IRG_NIK, picIrgNik));
		}
				
		tableModel.setSearchCriteria(searchCriteria);
		
		PrimeFaces.current().executeScript("initSelect2();");
	}
	
	public void reset(ActionEvent event) {
		noReferensi = InternalRegulationPenerbitanConstants.STRING_EMPTY;
		judulRegulasi = InternalRegulationPenerbitanConstants.STRING_EMPTY;
		statusRegulasiId = null;
		tipeRegulasiId = null;
		unitKerjaTpgId = null;
		startDate = null;
		endDate = null;
		processStatusId = null;
		
		search(event);
		
		PrimeFaces.current().executeScript("initSelect2();");
	}
	
	public void delete(Long irgId) {
		try {
			InternalRegulationPenerbitan irg = internalRegulationPenerbitanService.findById(irgId);
			irg.setEnabledFlag(Constants.CONSTANT_NO);
			irg.setLastUpdateBy(facesUtil.retrieveUserLogin());
			irg.setLastUpdateDate(new Timestamp(new Date().getTime()));
			internalRegulationPenerbitanService.update(irg);
			facesUtil.addSuccessMsg(facesUtil.retrieveMessage("deleteSuccess"));
		} catch (Exception e) {
			e.printStackTrace();
			facesUtil.addFacesMsg(FacesMessage.SEVERITY_ERROR, null, "Operation Failed : " + e.getMessage(), "");
		}
	}
	
	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		InternalRegulationPenerbitanBean.logger = logger;
	}

	public String getInternalRegPenerbitanObsoleteSearch() {
		return internalRegPenerbitanObsoleteSearch;
	}

	public void setInternalRegPenerbitanObsoleteSearch(String internalRegPenerbitanObsoleteSearch) {
		this.internalRegPenerbitanObsoleteSearch = internalRegPenerbitanObsoleteSearch;
	}

	public int getPaging() {
		return paging;
	}

	public void setPaging(int paging) {
		this.paging = paging;
	}

	public FacesUtil getFacesUtil() {
		return facesUtil;
	}

	public void setFacesUtil(FacesUtil facesUtil) {
		this.facesUtil = facesUtil;
	}

	public FileUtil getFileUtil() {
		return fileUtil;
	}

	public void setFileUtil(FileUtil fileUtil) {
		this.fileUtil = fileUtil;
	}

	public String getNavigateEdit() {
		return navigateEdit;
	}

	public void setNavigateEdit(String navigateEdit) {
		this.navigateEdit = navigateEdit;
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

	@Override
	public void itemSelected(String clientId, String widgetVar, Object selectedItem) {
		// TODO Auto-generated method stub
		
	}

	public String getNoReferensi() {
		return noReferensi;
	}

	public void setNoReferensi(String noReferensi) {
		this.noReferensi = noReferensi;
	}

	public String getJudulRegulasi() {
		return judulRegulasi;
	}

	public void setJudulRegulasi(String judulRegulasi) {
		this.judulRegulasi = judulRegulasi;
	}

	public DBLazyDataModel<InternalRegulationPenerbitanVo> getTableModel() {
		return tableModel;
	}

	public void setTableModel(DBLazyDataModel<InternalRegulationPenerbitanVo> tableModel) {
		this.tableModel = tableModel;
	}

	public List<SelectItem> getStatusRegulasis() {
		return statusRegulasis;
	}

	public void setStatusRegulasis(List<SelectItem> statusRegulasis) {
		this.statusRegulasis = statusRegulasis;
	}

	public List<SelectItem> getTypeRegulasis() {
		return typeRegulasis;
	}

	public void setTypeRegulasis(List<SelectItem> typeRegulasis) {
		this.typeRegulasis = typeRegulasis;
	}

	public List<SelectItem> getUnitKerjas() {
		return unitKerjas;
	}

	public void setUnitKerjas(List<SelectItem> unitKerjas) {
		this.unitKerjas = unitKerjas;
	}

	public CounterTypeService getCounterTypeService() {
		return counterTypeService;
	}

	public void setCounterTypeService(CounterTypeService counterTypeService) {
		this.counterTypeService = counterTypeService;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public InternalRegulationPenerbitanService getInternalRegulationPenerbitanService() {
		return internalRegulationPenerbitanService;
	}

	public void setInternalRegulationPenerbitanService(
			InternalRegulationPenerbitanService internalRegulationPenerbitanService) {
		this.internalRegulationPenerbitanService = internalRegulationPenerbitanService;
	}

	public Long getStatusRegulasiId() {
		return statusRegulasiId;
	}

	public void setStatusRegulasiId(Long statusRegulasiId) {
		this.statusRegulasiId = statusRegulasiId;
	}

	public Long getTipeRegulasiId() {
		return tipeRegulasiId;
	}

	public void setTipeRegulasiId(Long tipeRegulasiId) {
		this.tipeRegulasiId = tipeRegulasiId;
	}

	public Long getUnitKerjaTpgId() {
		return unitKerjaTpgId;
	}

	public void setUnitKerjaTpgId(Long unitKerjaTpgId) {
		this.unitKerjaTpgId = unitKerjaTpgId;
	}

	public SimpleDateFormat getSdfDateSearch() {
		return sdfDateSearch;
	}

	public void setSdfDateSearch(SimpleDateFormat sdfDateSearch) {
		this.sdfDateSearch = sdfDateSearch;
	}

	public Long getProcessStatusId() {
		return processStatusId;
	}

	public void setProcessStatusId(Long processStatusId) {
		this.processStatusId = processStatusId;
	}

	public List<SelectItem> getProcessStatusList() {
		return processStatusList;
	}

	public void setProcessStatusList(List<SelectItem> processStatusList) {
		this.processStatusList = processStatusList;
	}

	public String getPicIrgNik() {
		return picIrgNik;
	}

	public void setPicIrgNik(String picIrgNik) {
		this.picIrgNik = picIrgNik;
	}

	public List<SelectItem> getPicsIrg() {
		return picsIrg;
	}

	public void setPicsIrg(List<SelectItem> picsIrg) {
		this.picsIrg = picsIrg;
	}
}