package com.wo.module.mstAudit.bean;

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

import org.apache.log4j.Logger;
import org.primefaces.PrimeFaces;

import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.paging.DBLazyDataModel;
import com.wo.module.common.paging.DefaultSearchObject;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.mstAudit.constant.MstAuditConstant;
import com.wo.module.mstAudit.model.MstAudit;
import com.wo.module.mstAudit.service.MstAuditService;
import com.wo.module.mstAudit.vo.MstAuditVO;
import com.wo.module.parameter.model.ParameterHeader;

public class MstAuditBean extends CommonBean implements Serializable{
	
	private static final long serialVersionUID = 7230593881789969637L;

	private static final String NAVIGATE_EDIT = MstAuditConstant.NAVIGATE_MST_AUDIT_EDIT;
	private static final String NAVIGATE_FORWARD = MstAuditConstant.NAVIGATE_TMP_AUDIT_EDIT;
	
	static Logger logger = Logger.getLogger(MstAuditBean.class);
	
	private MstAuditVO mstAuditVo;
	
	private String searchAuditFollowup;
	private String searchAuditTemplateName;
	private String searchScope;
	private Date searchAuditDatePeriodFrom;
	private Date searchAuditDatePeriodTo;
	
	private List<SelectItem> selectAuditFollowUp;
	
	private MstAuditService mstAuditService;
	
	private DBLazyDataModel<MstAuditVO> tableModel;
	
	public FacesUtil facesUtil;

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	@PostConstruct
	public void init() {
		super.init();
		initComponent();
		tableModel = new DBLazyDataModel<MstAuditVO>(mstAuditService, paging);
	}
	
	private void initComponent() {
		setupAuditType();
	}
	
	private void setupAuditType() {
		selectAuditFollowUp = new ArrayList<SelectItem>();
		try {
			selectAuditFollowUp = parameterDetailService.getListLabelValue(ParameterHeader.PARAM_HEAD_CODE_AUDITOR,
					false, true, facesUtil.retrieveDefaultLocale());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void search(ActionEvent actionEvent) {
		
		tableModel.setSearchCriteria(Arrays.asList(
				new DefaultSearchObject(MstAuditConstant.WHERE_AUDIT_FOLLOWUP, searchAuditFollowup),
				new DefaultSearchObject(MstAuditConstant.WHERE_AUDIT_NAME, searchAuditTemplateName),
				new DefaultSearchObject(MstAuditConstant.WHERE_AUDIT_PERIOD_FROM, searchAuditDatePeriodFrom != null ? sdf.format(searchAuditDatePeriodFrom) : ""),
				new DefaultSearchObject(MstAuditConstant.WHERE_AUDIT_PERIOD_TO, searchAuditDatePeriodTo != null ? sdf.format(searchAuditDatePeriodTo) : ""),
				new DefaultSearchObject(MstAuditConstant.WHERE_AUDIT_SCOPE, searchScope)
				));
	}
	
	public void reset(ActionEvent actionEvent) {
		searchAuditDatePeriodFrom = null;
		searchAuditDatePeriodTo = null;
		searchAuditFollowup = "";
		searchAuditTemplateName = "";
		searchScope = "";
		
		search(actionEvent);
		
		PrimeFaces.current().executeScript("reInitSelect2();");
	}
	
	public void delete(Long deleteId) {
		try {
			if(mstAuditService.isUsedInTransaction(deleteId)) {
				facesUtil.addErrMessage(facesUtil.retrieveMessage("formMstAuditIsUsedInTransaction"));
			} else {
				MstAudit mstAudit = mstAuditService.findById(deleteId);
				mstAudit.setEnabledFlag(Constants.CONSTANT_NO);
				mstAudit.setLastUpdateBy(facesUtil.retrieveUserLogin());
				mstAudit.setLastUpdateDate(new Timestamp(new Date().getTime()));
				mstAuditService.update(mstAudit);
				facesUtil.addSuccessMsg(facesUtil.retrieveMessage("deleteSuccess"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			facesUtil.addFacesMsg(FacesMessage.SEVERITY_ERROR, null, "Operation Failed : " + e.getMessage(), "");
		}
	}
	
	public void addMessage(String summary) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, null);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public void addErrMessage(String summary) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, null);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}
	
	public MstAuditVO getMstAuditVo() {
		return mstAuditVo;
	}

	public void setMstAuditVo(MstAuditVO mstAuditVo) {
		this.mstAuditVo = mstAuditVo;
	}
	
	public String getSearchScope() {
		return searchScope;
	}

	public void setSearchScope(String searchScope) {
		this.searchScope = searchScope;
	}

	public Date getSearchAuditDatePeriodFrom() {
		return searchAuditDatePeriodFrom;
	}

	public void setSearchAuditDatePeriodFrom(Date searchAuditDatePeriodFrom) {
		this.searchAuditDatePeriodFrom = searchAuditDatePeriodFrom;
	}

	public Date getSearchAuditDatePeriodTo() {
		return searchAuditDatePeriodTo;
	}

	public void setSearchAuditDatePeriodTo(Date searchAuditDatePeriodTo) {
		this.searchAuditDatePeriodTo = searchAuditDatePeriodTo;
	}
	
	public MstAuditService getMstAuditService() {
		return mstAuditService;
	}

	public void setMstAuditService(MstAuditService mstAuditService) {
		this.mstAuditService = mstAuditService;
	}

	public DBLazyDataModel<MstAuditVO> getTableModel() {
		return tableModel;
	}

	public void setTableModel(DBLazyDataModel<MstAuditVO> tableModel) {
		this.tableModel = tableModel;
	}

	public FacesUtil getFacesUtil() {
		return facesUtil;
	}

	public void setFacesUtil(FacesUtil facesUtil) {
		this.facesUtil = facesUtil;
	}

	public static String getNavigateEdit() {
		return NAVIGATE_EDIT;
	}
	
	public static String getNavigateForward() {
		return NAVIGATE_FORWARD;
	}

	public SimpleDateFormat getSdf() {
		return sdf;
	}

	public void setSdf(SimpleDateFormat sdf) {
		this.sdf = sdf;
	}

	public String getSearchAuditFollowup() {
		return searchAuditFollowup;
	}

	public void setSearchAuditFollowup(String searchAuditFollowup) {
		this.searchAuditFollowup = searchAuditFollowup;
	}

	public List<SelectItem> getSelectAuditFollowUp() {
		return selectAuditFollowUp;
	}

	public void setSelectAuditFollowUp(List<SelectItem> selectAuditFollowUp) {
		this.selectAuditFollowUp = selectAuditFollowUp;
	}

	public String getSearchAuditTemplateName() {
		return searchAuditTemplateName;
	}

	public void setSearchAuditTemplateName(String searchAuditTemplateName) {
		this.searchAuditTemplateName = searchAuditTemplateName;
	}
	
}
