package com.wo.module.tmpCorrespondenceApprovalAml.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.paging.DBLazyDataModel;
import com.wo.module.common.paging.DefaultSearchObject;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.parameter.model.ParameterHeader;
import com.wo.module.tmpCorrespondenceApproval.vo.TmpCorrespondenceApprovalSearchVo;
import com.wo.module.tmpCorrespondenceApprovalAml.constant.TmpCorrespondenceApprovalAmlConstant;
import com.wo.module.tmpCorrespondenceApprovalAml.service.TmpCorrespondenceApprovalAmlService;

public class TmpCorrespondenceApprovalAmlBean extends CommonBean implements Serializable{

	private static final long serialVersionUID = -3214901735335791352L;

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
	private DBLazyDataModel<TmpCorrespondenceApprovalSearchVo> tableCorrespondenceAml;
	private String navigateEdit = TmpCorrespondenceApprovalAmlConstant.NAVIGATE_EDIT;
	
	private TmpCorrespondenceApprovalAmlService tmpCorrespondenceApprovalAmlService;
	
	private FacesUtil facesUtil;

	@PostConstruct
	public void construct() {
		super.init();
		tableCorrespondenceAml = new DBLazyDataModel<TmpCorrespondenceApprovalSearchVo>(tmpCorrespondenceApprovalAmlService, getPaging());
		
		populateSelect();
	}
	
	private void populateSelect() {
		try {
			selectSender = parameterDetailService.getListLabelValue(ParameterHeader.PARAM_HEAD_CODE_SENDER, false, true, facesUtil.retrieveDefaultLocale());
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
		List<SearchObject> searchCriteria = new ArrayList<SearchObject>();
		if (searchPengirim != null && !searchPengirim.isEmpty()) {
			searchCriteria.add(new DefaultSearchObject(TmpCorrespondenceApprovalAmlConstant.SEARCH_PENGIRIM, searchPengirim));
		}

		if (searchNoSurat != null && !searchNoSurat.isEmpty()) {
			searchCriteria.add(new DefaultSearchObject(TmpCorrespondenceApprovalAmlConstant.SEARCH_NO_SURAT, searchNoSurat));
		}

		if (searchTanggalTerimaSuratFrom != null) {
			searchCriteria.add(new DefaultSearchObject(TmpCorrespondenceApprovalAmlConstant.SEARCH_TANGGAL_TERIMA_SURAT_FROM, searchTanggalTerimaSuratFrom));
		}

		if (searchTanggalTerimaSuratTo != null) {
			searchCriteria.add(new DefaultSearchObject(TmpCorrespondenceApprovalAmlConstant.SEARCH_TANGGAL_TERIMA_SURAT_TO, searchTanggalTerimaSuratTo));
		}
		
		if (searchTanggalSuratFrom != null) {
			searchCriteria.add(new DefaultSearchObject(TmpCorrespondenceApprovalAmlConstant.SEARCH_TANGGAL_SURAT_FROM, searchTanggalSuratFrom));
		}

		if (searchTanggalSuratTo != null) {
			searchCriteria.add(new DefaultSearchObject(TmpCorrespondenceApprovalAmlConstant.SEARCH_TANGGAL_SURAT_TO, searchTanggalSuratTo));
		}
		
		if (searchPerihal != null && !searchPerihal.isEmpty()) {
			searchCriteria.add(new DefaultSearchObject(TmpCorrespondenceApprovalAmlConstant.SEARCH_PERIHAL, searchPerihal));
		}

		if (searchTargetDateFrom != null) {
			searchCriteria.add(new DefaultSearchObject(TmpCorrespondenceApprovalAmlConstant.SEARCH_TARGET_DATE_FROM, searchTargetDateFrom));
		}

		if (searchTargetDateTo != null) {
			searchCriteria.add(new DefaultSearchObject(TmpCorrespondenceApprovalAmlConstant.SEARCH_TARGET_DATE_TO, searchTargetDateTo));
		}

		if (searchStatus != null && !searchStatus.isEmpty()) {
			searchCriteria.add(new DefaultSearchObject(TmpCorrespondenceApprovalAmlConstant.SEARCH_STATUS, searchStatus));
		}

		tableCorrespondenceAml.setSearchCriteria(searchCriteria);
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

	public DBLazyDataModel<TmpCorrespondenceApprovalSearchVo> getTableCorrespondenceAml() {
		return tableCorrespondenceAml;
	}

	public void setTableCorrespondenceAml(DBLazyDataModel<TmpCorrespondenceApprovalSearchVo> tableCorrespondenceAml) {
		this.tableCorrespondenceAml = tableCorrespondenceAml;
	}

	public String getNavigateEdit() {
		return navigateEdit;
	}

	public void setNavigateEdit(String navigateEdit) {
		this.navigateEdit = navigateEdit;
	}

	public TmpCorrespondenceApprovalAmlService getTmpCorrespondenceApprovalAmlService() {
		return tmpCorrespondenceApprovalAmlService;
	}

	public void setTmpCorrespondenceApprovalAmlService(
			TmpCorrespondenceApprovalAmlService tmpCorrespondenceApprovalAmlService) {
		this.tmpCorrespondenceApprovalAmlService = tmpCorrespondenceApprovalAmlService;
	}

	public FacesUtil getFacesUtil() {
		return facesUtil;
	}

	public void setFacesUtil(FacesUtil facesUtil) {
		this.facesUtil = facesUtil;
	}
	
}
