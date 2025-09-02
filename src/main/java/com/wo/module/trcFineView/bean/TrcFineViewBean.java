package com.wo.module.trcFineView.bean;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.apache.log4j.Logger;
import org.primefaces.PrimeFaces;

import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.paging.DBLazyDataModel;
import com.wo.module.common.paging.DefaultSearchObject;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.parameter.model.ParameterHeader;
import com.wo.module.parameter.service.ParameterDetailService;
import com.wo.module.trcFineView.constants.TrcFineViewConstants;
import com.wo.module.trcFineView.service.TrcFineViewService;
import com.wo.module.trcFineView.vo.TrcFineViewSearchVO;

public class TrcFineViewBean extends CommonBean implements Serializable {

	private static final long serialVersionUID = 2214671581602939933L;
	
	static Logger logger = Logger.getLogger(TrcFineViewBean.class);
	
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
	private DBLazyDataModel<TrcFineViewSearchVO> tableFine;
	private String navigateView = TrcFineViewConstants.NAVIGATE_VIEW_DETAIL;
//	private int paging;
	/*
	 * services
	 */
	private TrcFineViewService trcFineViewService;
//	private ParameterDetailService parameterDetailService;
	
	/*
	 * util
	 */
	private FacesUtil facesUtil;
	
	@PostConstruct
	public void init() {
		super.init();
		paging = getPaging();
		tableFine = new DBLazyDataModel<TrcFineViewSearchVO>(trcFineViewService, paging);
		
		populateSelect();
	}
	
	
	private void populateSelect() {
		try {
			selectSender = new ArrayList<SelectItem>();
			selectSender = parameterDetailService.getListLabelValue(ParameterHeader.PARAM_HEAD_CODE_SENDER, false, true, facesUtil.retrieveDefaultLocale());
			System.out.println("selectsender " + selectSender.get(0).getLabel());
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
	}
	
	@SuppressWarnings("rawtypes")
	public void search(ActionEvent actionEvent) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<SearchObject> searchCriteria = new ArrayList<SearchObject>();
		if (searchPengirim != null && !searchPengirim.isEmpty()) {
			searchCriteria.add(new DefaultSearchObject(TrcFineViewConstants.SEARCH_PENGIRIM, searchPengirim));
		}

		if (searchNoSurat != null && !searchNoSurat.isEmpty()) {
			searchCriteria.add(new DefaultSearchObject(TrcFineViewConstants.SEARCH_NO_SURAT, searchNoSurat));
		}

		if (searchTanggalTerimaSuratFrom != null) {
			searchCriteria.add(new DefaultSearchObject(TrcFineViewConstants.SEARCH_TANGGAL_TERIMA_SURAT_FROM,
					searchTanggalTerimaSuratFrom != null ? sdf.format(searchTanggalTerimaSuratFrom) : ""));
		}

		if (searchTanggalTerimaSuratTo != null) {
			searchCriteria.add(new DefaultSearchObject(TrcFineViewConstants.SEARCH_TANGGAL_TERIMA_SURAT_TO, 
					searchTanggalTerimaSuratTo != null ? sdf.format(searchTanggalTerimaSuratTo) : ""));
		}
		
		if (searchTanggalSuratFrom != null) {
			searchCriteria.add(new DefaultSearchObject(TrcFineViewConstants.SEARCH_TANGGAL_SURAT_FROM,
					searchTanggalSuratFrom != null ? sdf.format(searchTanggalSuratFrom) : "" ));
		}

		if (searchTanggalSuratTo != null) {
			searchCriteria.add(new DefaultSearchObject(TrcFineViewConstants.SEARCH_TANGGAL_SURAT_TO, 
					searchTanggalSuratTo != null ? sdf.format(searchTanggalSuratTo) : ""));
		}
		
		if (searchPerihal != null && !searchPerihal.isEmpty()) {
			searchCriteria.add(new DefaultSearchObject(TrcFineViewConstants.SEARCH_PERIHAL, searchPerihal));
		}

		if (searchTargetDateFrom != null) {
			searchCriteria.add(new DefaultSearchObject(TrcFineViewConstants.SEARCH_TARGET_DATE_FROM, 
					searchTargetDateFrom != null ? sdf.format(searchTargetDateFrom) : ""));
		}

		if (searchTargetDateTo != null) {
			searchCriteria.add(new DefaultSearchObject(TrcFineViewConstants.SEARCH_TARGET_DATE_TO,
					searchTargetDateTo != null ? sdf.format(searchTargetDateTo) : ""));
		}

		if (searchStatus != null && !searchStatus.isEmpty()) {
			searchCriteria.add(new DefaultSearchObject(TrcFineViewConstants.SEARCH_STATUS, searchStatus));
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


	public String getSearchStatus() {
		return searchStatus;
	}


	public void setSearchStatus(String searchStatus) {
		this.searchStatus = searchStatus;
	}


	public List<SelectItem> getSelectSender() {
		return selectSender;
	}


	public void setSelectSender(List<SelectItem> selectSender) {
		this.selectSender = selectSender;
	}


	public List<SelectItem> getStatusList() {
		return statusList;
	}


	public void setStatusList(List<SelectItem> statusList) {
		this.statusList = statusList;
	}


	public DBLazyDataModel<TrcFineViewSearchVO> getTableFine() {
		return tableFine;
	}


	public void setTableFine(DBLazyDataModel<TrcFineViewSearchVO> tableFine) {
		this.tableFine = tableFine;
	}


	public String getNavigateView() {
		return navigateView;
	}


	public void setNavigateView(String navigateView) {
		this.navigateView = navigateView;
	}


	public TrcFineViewService getTrcFineViewService() {
		return trcFineViewService;
	}


	public void setTrcFineViewService(TrcFineViewService trcFineViewService) {
		this.trcFineViewService = trcFineViewService;
	}


	public FacesUtil getFacesUtil() {
		return facesUtil;
	}


	public void setFacesUtil(FacesUtil facesUtil) {
		this.facesUtil = facesUtil;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	public static Logger getLogger() {
		return logger;
	}


	public static void setLogger(Logger logger) {
		TrcFineViewBean.logger = logger;
	}


//	public int getPaging() {
//		return paging;
//	}
//
//
//	public void setPaging(int paging) {
//		this.paging = paging;
//	}


//	public ParameterDetailService getParameterDetailService() {
//		return parameterDetailService;
//	}
//
//
//	public void setParameterDetailService(ParameterDetailService parameterDetailService) {
//		this.parameterDetailService = parameterDetailService;
//	}
}