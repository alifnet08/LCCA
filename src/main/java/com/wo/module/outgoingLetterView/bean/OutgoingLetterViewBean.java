package com.wo.module.outgoingLetterView.bean;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.apache.log4j.Logger;

import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.paging.DBLazyDataModel;
import com.wo.module.common.paging.DefaultSearchObject;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.common.util.FileUtil;
import com.wo.module.common.utility.CallApiManager;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.outgoingLetter.constant.OutgoingLetterConstants;
import com.wo.module.outgoingLetterView.constant.OutgoingLetterViewConstant;
import com.wo.module.outgoingLetterView.model.OutgoingLetterView;
import com.wo.module.outgoingLetterView.service.OutgoingLetterViewService;
import com.wo.module.parameter.service.ParameterDetailService;
import com.wo.module.user.model.User;

public class OutgoingLetterViewBean extends CommonBean implements Serializable{

	private static final long serialVersionUID = 8145796344237552284L;
	static Logger logger = Logger.getLogger(OutgoingLetterViewBean.class);
	
	private String tujuanSurat;
	private String perihal;
	private String nomorSurat;
	private String sampaikanKepada;
	private Date tanggalSuratFrom;
	private Date tanggalSuratTo;
	private String tembusanSurat;
	private Long userDivisionId;
	
	private String navigateEdit = OutgoingLetterViewConstant.NAVIGATE_EDIT;
	
	private DBLazyDataModel<OutgoingLetterView> tableModel;
	
	private ParameterDetailService parameterDetailService;
	private OutgoingLetterViewService outgoingLetterViewService;
	
	private FacesUtil facesUtil;
	private FileUtil fileUtil;
	
	@SuppressWarnings("rawtypes")
	@PostConstruct
	public void init() {
		super.init();
		User getUserLogin = facesUtil.getUserLogin();
		this.userDivisionId = getUserLogin.getDivisionId();
		tableModel = new DBLazyDataModel<OutgoingLetterView>(outgoingLetterViewService,paging);
		List<SearchObject> searchCriteria = new ArrayList<SearchObject>();
		if(userDivisionId != null && userDivisionId > 0) {
			searchCriteria.add(new DefaultSearchObject(OutgoingLetterConstants.WHERE_DIVISION, userDivisionId));
		}
		
		tableModel.setSearchCriteria(searchCriteria);
		
		fileUtil = FileUtil.getInstance();
	}
	
	public void addMessage(String summary) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, null);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public void addErrMessage(String summary) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, null);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}
	
	@SuppressWarnings("rawtypes")
	public void search(ActionEvent actionEvent) {
		List<SearchObject> searchCriteria = new ArrayList<SearchObject>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd");
		
		if(tujuanSurat != null && !tujuanSurat.isEmpty()) {
			searchCriteria.add(new DefaultSearchObject(OutgoingLetterConstants.WHERE_LETTER_PURPOSE, tujuanSurat));
		}
		
		if(nomorSurat != null && !nomorSurat.isEmpty()) {
			searchCriteria.add(new DefaultSearchObject(OutgoingLetterConstants.WHERE_LETTER_NO, nomorSurat));
		}
		
		if(perihal != null && !perihal.isEmpty()) {
			searchCriteria.add(new DefaultSearchObject(OutgoingLetterConstants.WHERE_PERIHAL, perihal));
		}
		
		if(tembusanSurat != null && !tembusanSurat.isEmpty()) {
			searchCriteria.add(new DefaultSearchObject(OutgoingLetterConstants.WHERE_TEMBUSAN, tembusanSurat));
		}
		
		if(sampaikanKepada != null && !sampaikanKepada.isEmpty()) {
			searchCriteria.add(new DefaultSearchObject(OutgoingLetterConstants.WHERE_DELIVERED_TO, sampaikanKepada));
		}
		
		if(tanggalSuratFrom != null) {
			searchCriteria.add(new DefaultSearchObject(OutgoingLetterConstants.WHERE_LETTER_DATE_FROM, 
					tanggalSuratFrom != null ? sdf.format(tanggalSuratFrom) : ""));
		}
		
		if (tanggalSuratTo != null) {
			searchCriteria.add(new DefaultSearchObject(OutgoingLetterConstants.WHERE_LETTER_DATE_TO, 
					tanggalSuratTo != null ? sdf.format(tanggalSuratTo) : ""));
		}
		
		if(userDivisionId != null && userDivisionId > 0) {
			searchCriteria.add(new DefaultSearchObject(OutgoingLetterConstants.WHERE_DIVISION, userDivisionId));
		}
		
		tableModel.setSearchCriteria(searchCriteria);
	}

	public void reset(ActionEvent actionEvent) {
		tujuanSurat = "";
		tanggalSuratFrom = null;
		tanggalSuratTo = null;
		sampaikanKepada = "";
		perihal = "";
		nomorSurat = "";
		tembusanSurat = "";
		userDivisionId = this.getUserDivisionId();
		search(actionEvent);
	}
	
	public void downloadFile(String fileId, String fileName, byte[] content) throws Exception {
		CallApiManager.downloadFile(fileId, fileName, content, parameterDetailService);
	}
	
	public Boolean getIsLogin() {
		if(facesUtil.getSessionAttribute(Constants.SESSION_EMPLOYEE) != null
				|| facesUtil.getSessionAttribute(Constants.SESSION_KANDIDAT) != null) {
			return true;
		}
		return false;
	}
	
	public String getTujuanSurat() {
		return tujuanSurat;
	}

	public void setTujuanSurat(String tujuanSurat) {
		this.tujuanSurat = tujuanSurat;
	}

	public String getPerihal() {
		return perihal;
	}

	public void setPerihal(String perihal) {
		this.perihal = perihal;
	}

	public String getNomorSurat() {
		return nomorSurat;
	}

	public void setNomorSurat(String nomorSurat) {
		this.nomorSurat = nomorSurat;
	}

	public String getSampaikanKepada() {
		return sampaikanKepada;
	}

	public void setSampaikanKepada(String sampaikanKepada) {
		this.sampaikanKepada = sampaikanKepada;
	}

	public Date getTanggalSuratFrom() {
		return tanggalSuratFrom;
	}

	public void setTanggalSuratFrom(Date tanggalSuratFrom) {
		this.tanggalSuratFrom = tanggalSuratFrom;
	}

	public Date getTanggalSuratTo() {
		return tanggalSuratTo;
	}

	public void setTanggalSuratTo(Date tanggalSuratTo) {
		this.tanggalSuratTo = tanggalSuratTo;
	}

	public String getTembusanSurat() {
		return tembusanSurat;
	}

	public void setTembusanSurat(String tembusanSurat) {
		this.tembusanSurat = tembusanSurat;
	}

	public String getNavigateEdit() {
		return navigateEdit;
	}

	public void setNavigateEdit(String navigateEdit) {
		this.navigateEdit = navigateEdit;
	}

	public ParameterDetailService getParameterDetailService() {
		return parameterDetailService;
	}

	public void setParameterDetailService(ParameterDetailService parameterDetailService) {
		this.parameterDetailService = parameterDetailService;
	}

	public OutgoingLetterViewService getOutgoingLetterViewService() {
		return outgoingLetterViewService;
	}

	public void setOutgoingLetterViewService(OutgoingLetterViewService outgoingLetterViewService) {
		this.outgoingLetterViewService = outgoingLetterViewService;
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

	public DBLazyDataModel<OutgoingLetterView> getTableModel() {
		return tableModel;
	}

	public void setTableModel(DBLazyDataModel<OutgoingLetterView> tableModel) {
		this.tableModel = tableModel;
	}

	public Long getUserDivisionId() {
		return userDivisionId;
	}

	public void setUserDivisionId(Long userDivisionId) {
		this.userDivisionId = userDivisionId;
	}
}
