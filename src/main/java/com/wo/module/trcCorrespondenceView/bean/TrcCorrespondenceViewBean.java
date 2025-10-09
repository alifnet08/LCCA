package com.wo.module.trcCorrespondenceView.bean;

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
import com.wo.module.parameter.service.ParameterDetailService;
import com.wo.module.trcCorrespondenceView.constant.TrcCorrespondenceViewConstants;
import com.wo.module.trcCorrespondenceView.service.TrcCorrespondenceViewService;
import com.wo.module.trcCorrespondenceView.vo.TrcCorrespondenceViewSearchVo;
import com.wo.module.user.model.User;
import com.wo.module.user.service.UserService;

public class TrcCorrespondenceViewBean extends CommonBean implements Serializable {
	static Logger logger = Logger.getLogger(TrcCorrespondenceViewBean.class);
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
	private Long userDivisionId;
	private List<SelectItem> selectSender;
	private List<SelectItem> statusList;
	private DBLazyDataModel<TrcCorrespondenceViewSearchVo> tableCorrespondence;
	private String navigateEdit = TrcCorrespondenceViewConstants.NAVIGATE_EDIT;

	/*
	 * services
	 */
	private TrcCorrespondenceViewService trcCorrespondenceViewService;
	private UserService userService;

	/*
	 * util
	 */
	private FacesUtil facesUtil;

	@SuppressWarnings("rawtypes")
	@PostConstruct
	public void construct() {
		super.init();
		User getUserLogin = facesUtil.getUserLogin();
		this.userDivisionId = getUserLogin.getDivisionId();

		tableCorrespondence = new DBLazyDataModel<TrcCorrespondenceViewSearchVo>(trcCorrespondenceViewService,
				getPaging());
		List<SearchObject> searchCriteria = new ArrayList<SearchObject>();
		if (userDivisionId != null && userDivisionId > 0) {
			searchCriteria.add(new DefaultSearchObject(TrcCorrespondenceViewConstants.SEARCH_DIVISION, userDivisionId));
		}
		tableCorrespondence.setSearchCriteria(searchCriteria);
		
		populateSelect();

	}

	private void populateSelect() {
		try {
			selectSender = parameterDetailService.getListLabelValue(ParameterHeader.PARAM_HEAD_CODE_SENDER, false, true,
					facesUtil.retrieveDefaultLocale());
			statusList = new ArrayList<SelectItem>();
//			List<ParameterDetail> listParamDtl = parameterDetailService
//					.getParameterDetailByParamCode(ParameterHeader.PARAM_HEAD_CODE_DATA_STATUS);
			
			List<ParameterDetail> listParamDtl = parameterDetailService
					.getParameterDetailByTwoParamCode(ParameterHeader.PARAM_HEAD_CODE_PIC_FOLLOWUP_STATUS, ParameterHeader.PARAM_HEAD_CODE_ATTENDANCE);

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
	}

	@SuppressWarnings("rawtypes")
	public void search(ActionEvent actionEvent) {
		List<SearchObject> searchCriteria = new ArrayList<SearchObject>();
		if (searchPengirim != null && !searchPengirim.isEmpty()) {
			searchCriteria
					.add(new DefaultSearchObject(TrcCorrespondenceViewConstants.SEARCH_PENGIRIM, searchPengirim));
		}

		if (searchNoSurat != null && !searchNoSurat.isEmpty()) {
			searchCriteria
					.add(new DefaultSearchObject(TrcCorrespondenceViewConstants.SEARCH_NO_SURAT, searchNoSurat));
		}

		if (searchTanggalTerimaSuratFrom != null) {
			searchCriteria.add(new DefaultSearchObject(
					TrcCorrespondenceViewConstants.SEARCH_TANGGAL_TERIMA_SURAT_FROM, searchTanggalTerimaSuratFrom));
		}

		if (searchTanggalTerimaSuratTo != null) {
			searchCriteria.add(new DefaultSearchObject(
					TrcCorrespondenceViewConstants.SEARCH_TANGGAL_TERIMA_SURAT_TO, searchTanggalTerimaSuratTo));
		}

		if (searchTanggalSuratFrom != null) {
			searchCriteria.add(new DefaultSearchObject(TrcCorrespondenceViewConstants.SEARCH_TANGGAL_SURAT_FROM,
					searchTanggalSuratFrom));
		}

		if (searchTanggalSuratTo != null) {
			searchCriteria.add(new DefaultSearchObject(TrcCorrespondenceViewConstants.SEARCH_TANGGAL_SURAT_TO,
					searchTanggalSuratTo));
		}

		if (searchPerihal != null && !searchPerihal.isEmpty()) {
			searchCriteria
					.add(new DefaultSearchObject(TrcCorrespondenceViewConstants.SEARCH_PERIHAL, searchPerihal));
		}

		if (searchTargetDateFrom != null) {
			searchCriteria.add(new DefaultSearchObject(TrcCorrespondenceViewConstants.SEARCH_TARGET_DATE_FROM,
					searchTargetDateFrom));
		}

		if (searchTargetDateTo != null) {
			searchCriteria.add(new DefaultSearchObject(TrcCorrespondenceViewConstants.SEARCH_TARGET_DATE_TO,
					searchTargetDateTo));
		}

//		if (searchStatus != null && !searchStatus.isEmpty()) {
//			searchCriteria.add(new DefaultSearchObject(TrcCorrespondenceViewConstants.SEARCH_STATUS, searchStatus));
//		}
		
		if (searchStatus != null && !searchStatus.isEmpty()) {
			searchCriteria.add(new DefaultSearchObject(TrcCorrespondenceViewConstants.SEARCH_FOLLOW_UP_STATUS, searchStatus));
		}
		
		if (userDivisionId != null && userDivisionId > 0) {
			searchCriteria.add(new DefaultSearchObject(TrcCorrespondenceViewConstants.SEARCH_DIVISION, userDivisionId));
		}

		tableCorrespondence.setSearchCriteria(searchCriteria);
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
		userDivisionId = this.getUserDivisionId();
		search(actionEvent);
		
		PrimeFaces.current().executeScript("reInitSelect2();");
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

	public DBLazyDataModel<TrcCorrespondenceViewSearchVo> getTableCorrespondence() {
		return tableCorrespondence;
	}

	public void setTableCorrespondence(DBLazyDataModel<TrcCorrespondenceViewSearchVo> tableCorrespondence) {
		this.tableCorrespondence = tableCorrespondence;
	}

	public TrcCorrespondenceViewService getTrcCorrespondenceViewService() {
		return trcCorrespondenceViewService;
	}

	public void setTrcCorrespondenceViewService(TrcCorrespondenceViewService trcCorrespondenceViewService) {
		this.trcCorrespondenceViewService = trcCorrespondenceViewService;
	}

	public String getNavigateEdit() {
		return navigateEdit;
	}

	public void setNavigateEdit(String navigateEdit) {
		this.navigateEdit = navigateEdit;
	}


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

	public Long getUserDivisionId() {
		return userDivisionId;
	}

	public void setUserDivisionId(Long userDivisionId) {
		this.userDivisionId = userDivisionId;
	}
}
