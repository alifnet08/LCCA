package com.wo.module.mstProvinceLocation.bean;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.event.ActionEvent;

import org.apache.log4j.Logger;

import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.model.UploadedFileWO;
import com.wo.module.common.paging.DBLazyDataModel;
import com.wo.module.common.paging.DefaultSearchObject;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.common.util.FileUtil;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.mstProvince.constants.MstProvinceConstants;
import com.wo.module.mstProvince.model.MstProvinceLocation;
import com.wo.module.mstProvinceLocation.constants.MstProvinceLocationConstants;
import com.wo.module.mstProvinceLocation.service.MstProvinceLocationService;

public class MstProvinceLocationBean extends CommonBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private static Logger logger = Logger.getLogger(MstProvinceLocationBean.class);
	
	private DBLazyDataModel<MstProvinceLocation> tableModel;
	
	private MstProvinceLocationService mstProvinceLocationService;
	
	private String searchBranchCode;
	private String searchProvince;
	
	private int paging;
	
	private String navigateEdit = MstProvinceLocationConstants.NAVIGATE_EDIT;
	
	private FacesUtil facesUtil;
	
	private UploadedFileWO uploadFile;
	
	private FileUtil fileUtil;
	
	private NumberFormat decimalFormatter = new DecimalFormat("#.#");
	
	@PostConstruct
	public void init() {
		super.init();
		paging = Constants.DEFAULT_PAGING_NUMBER;
		tableModel = new DBLazyDataModel<MstProvinceLocation>(mstProvinceLocationService, paging);	
		fileUtil = FileUtil.getInstance();
	}
	
	@SuppressWarnings("rawtypes")
	public void search(ActionEvent actionEvent) {
		List<SearchObject> searchCriteria = new ArrayList<SearchObject>();
		
		if (searchBranchCode != null && !searchBranchCode.isEmpty()) {
			searchCriteria.add(new DefaultSearchObject(MstProvinceConstants.SEARCH_BY_BRANCH_CODE, searchBranchCode));
		}
		
		if (searchProvince != null && !searchProvince.isEmpty()) {
			searchCriteria.add(new DefaultSearchObject(MstProvinceConstants.SEARCH_BY_PROVINCE, searchProvince));
		}
		
		tableModel.setSearchCriteria(searchCriteria);
	}
	
	public void reset(ActionEvent actionEvent) {
		searchBranchCode = "";
		searchProvince = "";
		
		search(actionEvent);
	}
	
	
	
	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		MstProvinceLocationBean.logger = logger;
	}

	public DBLazyDataModel<MstProvinceLocation> getTableModel() {
		return tableModel;
	}

	public void setTableModel(DBLazyDataModel<MstProvinceLocation> tableModel) {
		this.tableModel = tableModel;
	}

	

	public MstProvinceLocationService getMstProvinceLocationService() {
		return mstProvinceLocationService;
	}

	public void setMstProvinceLocationService(MstProvinceLocationService mstProvinceLocationService) {
		this.mstProvinceLocationService = mstProvinceLocationService;
	}

	public String getSearchBranchCode() {
		return searchBranchCode;
	}

	public void setSearchBranchCode(String searchBranchCode) {
		this.searchBranchCode = searchBranchCode;
	}

	public String getSearchProvince() {
		return searchProvince;
	}

	public void setSearchProvince(String searchProvince) {
		this.searchProvince = searchProvince;
	}

	public int getPaging() {
		return paging;
	}

	public void setPaging(int paging) {
		this.paging = paging;
	}

	public String getNavigateEdit() {
		return navigateEdit;
	}

	public void setNavigateEdit(String navigateEdit) {
		this.navigateEdit = navigateEdit;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
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

	public NumberFormat getDecimalFormatter() {
		return decimalFormatter;
	}

	public void setDecimalFormatter(NumberFormat decimalFormatter) {
		this.decimalFormatter = decimalFormatter;
	}

	public UploadedFileWO getUploadFile() {
		return uploadFile;
	}

	public void setUploadFile(UploadedFileWO uploadFile) {
		this.uploadFile = uploadFile;
	}
}