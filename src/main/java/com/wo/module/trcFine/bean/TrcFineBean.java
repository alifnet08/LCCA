package com.wo.module.trcFine.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.apache.log4j.Logger;
import org.primefaces.PrimeFaces;

import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.paging.DBLazyDataModel;
import com.wo.module.common.paging.DefaultSearchObject;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.parameter.model.ParameterHeader;
import com.wo.module.trcFine.constant.TrcFineConstants;
import com.wo.module.trcFine.service.TrcFineService;
import com.wo.module.trcFine.vo.TrcFineSearchVo;
import com.wo.module.user.model.User;
import com.wo.module.user.service.UserService;

public class TrcFineBean extends CommonBean implements Serializable {
	static Logger logger = Logger.getLogger(TrcFineBean.class);
	private static final long serialVersionUID = -7542280143731129467L;

	/*
	 * Search property
	 */
	private String searchPengirim;
	private String searchNoSurat;
	private Date searchTanggalTerimaSuratFrom;
	private Date searchTanggalTerimaSuratTo;
	private Date searchTanggalSuratFrom;
	private Date searchTanggalSuratTo;
	private String searchPerihal;
	private Date searchTargetDateFrom;
	private Date searchTargetDateTo;
	private String searchStatus;
	private List<SelectItem> selectSender;
	private List<SelectItem> statusList;
	private DBLazyDataModel<TrcFineSearchVo> tableFine;
	private String navigateEdit = TrcFineConstants.NAVIGATE_EDIT;


	private TrcFineService trcFineService;
	//private ParameterDetailService parameterDetailService;
	private UserService userService;

	
	private FacesUtil facesUtil;

	@SuppressWarnings("rawtypes")
	@PostConstruct
	public void construct() {
		super.init();
		tableFine = new DBLazyDataModel<TrcFineSearchVo>(trcFineService, getPaging());
		List<SearchObject> searchCriteria = new ArrayList<SearchObject>();

		User userLogin = userService.getUserByNik(facesUtil.retrieveUserLogin());
		if (userLogin != null) {
			searchCriteria.add(new DefaultSearchObject(TrcFineConstants.SEARCH_BY_USER_LOGIN, userLogin.getUserId()));
		}

		tableFine.setSearchCriteria(searchCriteria);
		populateSelect();

	}

	private void populateSelect() {
		try {
			selectSender = parameterDetailService.getListLabelValue(ParameterHeader.PARAM_HEAD_CODE_SENDER, false, true,
					facesUtil.retrieveDefaultLocale());
			statusList = new ArrayList<SelectItem>();
			List<ParameterDetail> listParamDtl = parameterDetailService
					.getParameterDetailByParamCode(ParameterHeader.PARAM_HEAD_CODE_DATA_STATUS);

			for (ParameterDetail vo : listParamDtl) {
				SelectItem si = new SelectItem();
				si.setLabel(vo.getName());
				si.setValue(vo.getParameterDtlCode());
				statusList.add(si);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		PrimeFaces.current().executeScript("reInitSelect2();");
//		RequestContext.getCurrentInstance().execute("reInitSelect2();");
	}

	@SuppressWarnings("rawtypes")
	public void search(ActionEvent actionEvent) {
		List<SearchObject> searchCriteria = new ArrayList<SearchObject>();
		if (searchPengirim != null && !searchPengirim.isEmpty()) {
			searchCriteria.add(new DefaultSearchObject(TrcFineConstants.SEARCH_PENGIRIM, searchPengirim));
		}

		if (searchNoSurat != null && !searchNoSurat.isEmpty()) {
			searchCriteria.add(new DefaultSearchObject(TrcFineConstants.SEARCH_NO_SURAT, searchNoSurat));
		}

		if (searchTanggalTerimaSuratFrom != null) {
			searchCriteria.add(new DefaultSearchObject(TrcFineConstants.SEARCH_TANGGAL_TERIMA_SURAT_FROM,
					searchTanggalTerimaSuratFrom));
		}

		if (searchTanggalTerimaSuratTo != null) {
			searchCriteria.add(new DefaultSearchObject(TrcFineConstants.SEARCH_TANGGAL_TERIMA_SURAT_TO,
					searchTanggalTerimaSuratTo));
		}

		if (searchTanggalSuratFrom != null) {
			searchCriteria.add(new DefaultSearchObject(TrcFineConstants.SEARCH_TANGGAL_SURAT_FROM,
					searchTanggalSuratFrom));
		}

		if (searchTanggalSuratTo != null) {
			searchCriteria.add(
					new DefaultSearchObject(TrcFineConstants.SEARCH_TANGGAL_SURAT_TO, searchTanggalSuratTo));
		}

		if (searchPerihal != null && !searchPerihal.isEmpty()) {
			searchCriteria.add(new DefaultSearchObject(TrcFineConstants.SEARCH_PERIHAL, searchPerihal));
		}

		if (searchTargetDateFrom != null) {
			searchCriteria.add(
					new DefaultSearchObject(TrcFineConstants.SEARCH_TARGET_DATE_FROM, searchTargetDateFrom));
		}

		if (searchTargetDateTo != null) {
			searchCriteria
					.add(new DefaultSearchObject(TrcFineConstants.SEARCH_TARGET_DATE_TO, searchTargetDateTo));
		}

		if (searchStatus != null && !searchStatus.isEmpty()) {
			searchCriteria.add(new DefaultSearchObject(TrcFineConstants.SEARCH_STATUS, searchStatus));
		}
		
		User userLogin = userService.getUserByNik(facesUtil.retrieveUserLogin());
		if (userLogin != null) {
			searchCriteria.add(new DefaultSearchObject(TrcFineConstants.SEARCH_BY_USER_LOGIN, userLogin.getUserId()));
		}

		tableFine.setSearchCriteria(searchCriteria);
	}

	public void reset(ActionEvent actionEvent) {
		searchPengirim = "";
		searchNoSurat = "";
		searchTanggalTerimaSuratFrom = null;
		searchTanggalTerimaSuratTo = null;
		searchTanggalSuratFrom = null;
		searchTanggalSuratTo = null;
		searchPerihal = null;
		searchTargetDateFrom = null;
		searchTargetDateTo = null;
		searchStatus = null;
		search(actionEvent);
		
		PrimeFaces.current().executeScript("reInitSelect2();");
//		RequestContext.getCurrentInstance().execute("reInitSelect2();");
	}

	public String getSearchPengirim() {
		return searchPengirim;
	}

	public void setSearchPengirim(String searchPengirim) {
		this.searchPengirim = searchPengirim;
	}

	public String getSearchNoSurat() {
		return searchNoSurat;
	}

	public void setSearchNoSurat(String searchNoSurat) {
		this.searchNoSurat = searchNoSurat;
	}

	public Date getSearchTanggalTerimaSuratFrom() {
		return searchTanggalTerimaSuratFrom;
	}

	public void setSearchTanggalTerimaSuratFrom(Date searchTanggalTerimaSuratFrom) {
		this.searchTanggalTerimaSuratFrom = searchTanggalTerimaSuratFrom;
	}

	public Date getSearchTanggalTerimaSuratTo() {
		return searchTanggalTerimaSuratTo;
	}

	public void setSearchTanggalTerimaSuratTo(Date searchTanggalTerimaSuratTo) {
		this.searchTanggalTerimaSuratTo = searchTanggalTerimaSuratTo;
	}

	public Date getSearchTanggalSuratFrom() {
		return searchTanggalSuratFrom;
	}

	public void setSearchTanggalSuratFrom(Date searchTanggalSuratFrom) {
		this.searchTanggalSuratFrom = searchTanggalSuratFrom;
	}

	public Date getSearchTanggalSuratTo() {
		return searchTanggalSuratTo;
	}

	public void setSearchTanggalSuratTo(Date searchTanggalSuratTo) {
		this.searchTanggalSuratTo = searchTanggalSuratTo;
	}

	public String getSearchPerihal() {
		return searchPerihal;
	}

	public void setSearchPerihal(String searchPerihal) {
		this.searchPerihal = searchPerihal;
	}

	public Date getSearchTargetDateFrom() {
		return searchTargetDateFrom;
	}

	public void setSearchTargetDateFrom(Date searchTargetDateFrom) {
		this.searchTargetDateFrom = searchTargetDateFrom;
	}

	public Date getSearchTargetDateTo() {
		return searchTargetDateTo;
	}

	public void setSearchTargetDateTo(Date searchTargetDateTo) {
		this.searchTargetDateTo = searchTargetDateTo;
	}

	public DBLazyDataModel<TrcFineSearchVo> getTableFine() {
		return tableFine;
	}

	public void setTableFine(DBLazyDataModel<TrcFineSearchVo> tableFine) {
		this.tableFine = tableFine;
	}

	public TrcFineService getTrcFineService() {
		return trcFineService;
	}

	public void setTrcFineService(TrcFineService trcFineService) {
		this.trcFineService = trcFineService;
	}

	public String getNavigateEdit() {
		return navigateEdit;
	}

	public void setNavigateEdit(String navigateEdit) {
		this.navigateEdit = navigateEdit;
	}

	/*public ParameterDetailService getParameterDetailService() {
		return parameterDetailService;
	}

	public void setParameterDetailService(ParameterDetailService parameterDetailService) {
		this.parameterDetailService = parameterDetailService;
	}*/

	public List<SelectItem> getSelectSender() {
		return selectSender;
	}

	public void setSelectSender(List<SelectItem> selectSender) {
		this.selectSender = selectSender;
	}

	public FacesUtil getFacesUtil() {
		return facesUtil;
	}

	public void setFacesUtil(FacesUtil facesUtil) {
		this.facesUtil = facesUtil;
	}

	public String getSearchStatus() {
		return searchStatus;
	}

	public void setSearchStatus(String searchStatus) {
		this.searchStatus = searchStatus;
	}

	public List<SelectItem> getStatusList() {
		return statusList;
	}

	public void setStatusList(List<SelectItem> statusList) {
		this.statusList = statusList;
	}
	
	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}
}
