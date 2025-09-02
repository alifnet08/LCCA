package com.wo.module.mstAudit.bean;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.model.SelectItem;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.primefaces.PrimeFaces;

import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.Constants;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.mstAudit.constant.MstAuditConstant;
import com.wo.module.mstAudit.model.MstAudit;
import com.wo.module.mstAudit.service.MstAuditService;
import com.wo.module.parameter.model.ParameterHeader;

public class MstAuditEditBean extends CommonBean implements Serializable{

	private static final long serialVersionUID = -6276772807117218544L;

	static Logger logger = Logger.getLogger(MstAuditEditBean.class);
	private static final String NAVIGATE_SEARCH = MstAuditConstant.NAVIGATE_MST_AUDIT_SEARCH;
	
	private MstAudit mstAudit;
	
	private String actionMode;
	private String editId;
	
	private List<SelectItem> selectAuditFollowUp;
	
	private MstAuditService mstAuditService;
	
	public FacesUtil facesUtil;
	
	private SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
	
	@PostConstruct
	public void init() {
		super.init();
		initComponent();
		
		checkNewOrEdit();
	}
	
	private void initComponent() {
		setupAuditFollowUp();
	}
	
	private void setupAuditFollowUp() {
		selectAuditFollowUp = new ArrayList<SelectItem>();
		try {
			selectAuditFollowUp = parameterDetailService.getListLabelValue(ParameterHeader.PARAM_HEAD_CODE_AUDITOR,
					false, true, facesUtil.retrieveDefaultLocale());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		PrimeFaces.current().executeScript("reInitSelect2();");
	}
	
	private void checkNewOrEdit() {
		String editId = facesUtil.retrieveRequestParam("id");
		String token = facesUtil.retrieveRequestParam("token"); 
		
		//String viewId = facesUtil.retrieveRequestParam("viewId");
		
		if (StringUtils.isBlank(editId) && StringUtils.isBlank(token)) {
			this.handleNew();
		} else {
			this.handleEdit(editId);
		}
	}
	
	private void handleNew() {
		actionMode = Constants.ACTION_ADD;
		mstAudit = new MstAudit();
	}
	
	private void handleEdit(String editId) {
		actionMode = Constants.ACTION_EDIT;
		String token = facesUtil.retrieveRequestParam("token");
		Long idLong = Long.parseLong(editId);
		
		if(StringUtils.isNotEmpty(token)) {
			editId = Constants.decryptString(token);
		}
		
		mstAudit = mstAuditService.findById(idLong);
		
		PrimeFaces.current().executeScript("reInitSelect2();");
	}

	private Boolean validate() {
		Boolean flag = false;
		
		if (actionMode == Constants.ACTION_ADD) {
			Integer validateSameValue = mstAuditService.countSameData(mstAudit.getAuditTemplateIn());
			
			if (validateSameValue > 0) {
				facesUtil.addErrMessage(facesUtil.retrieveMessage("formMstAuditTemplateIn") + " "
						+ facesUtil.retrieveMessage("errorAlreadyExists") );
				flag = true;
			}
		} else if (actionMode == Constants.ACTION_EDIT) {
			Integer validateSameValueById = mstAuditService.countSameDataById(
					mstAudit.getMstAuditId(),
					mstAudit.getAuditTemplateIn());
			
			if (validateSameValueById > 0) {
				facesUtil.addErrMessage(facesUtil.retrieveMessage("formMstAuditTemplateIn") + " "
						+ facesUtil.retrieveMessage("errorAlreadyExists") );
				flag = true;
			}
		}
		
		if (StringUtils.isBlank(mstAudit.getAuditor())) {
			facesUtil.addErrMessage(facesUtil.retrieveMessage("formMstAuditTipeAudit") + " "
					+ facesUtil.retrieveMessage("validateRequired"));
			flag = true;
		}
		
		if (StringUtils.isBlank(mstAudit.getAuditTemplateIn())) {
			facesUtil.addErrMessage(facesUtil.retrieveMessage("formMstAuditTemplateIn") + " "
					+ facesUtil.retrieveMessage("validateRequired"));
			flag = true;
		}
		
		if (mstAudit.getAuditDateFrom() == null) {
			facesUtil.addErrMessage(facesUtil.retrieveMessage("formMstAuditPeriodeFrom") + " "
					+ facesUtil.retrieveMessage("validateRequired"));
			flag = true;
		}
		
		if (mstAudit.getAuditDateTo() == null) {
			facesUtil.addErrMessage(facesUtil.retrieveMessage("formMstAuditPeriodeTo") + " "
					+ facesUtil.retrieveMessage("validateRequired"));
			flag = true;
		}
		
		if (mstAudit.getScope() == null) {
			facesUtil.addErrMessage(facesUtil.retrieveMessage("formMstAuditScope") + " "
					+ facesUtil.retrieveMessage("validateRequired"));
			flag = true;
		}
		
		if (mstAudit.getAuditDateTo() != null) {
			
			Calendar calenderAuditDateFrom = Calendar.getInstance();
			calenderAuditDateFrom.setTime(mstAudit.getAuditDateFrom());
			
			Calendar calenderAuditDateTo = Calendar.getInstance();
			calenderAuditDateTo.setTime(mstAudit.getAuditDateTo());
			
			int yearDateFrom = calenderAuditDateFrom.get(Calendar.YEAR);
			int yearDateTo = calenderAuditDateTo.get(Calendar.YEAR);
			
			int monthDateFrom = calenderAuditDateFrom.get(Calendar.MONTH);
			int monthDateTo = calenderAuditDateTo.get(Calendar.MONTH);
			
			int dayDateFrom = calenderAuditDateFrom.get(Calendar.DAY_OF_MONTH);
			int dayDateTo = calenderAuditDateTo.get(Calendar.DAY_OF_MONTH);
			
			if (yearDateFrom == yearDateTo) {
				if (monthDateFrom == monthDateTo) {
					if (dayDateTo < dayDateFrom) {
						facesUtil.addErrMessage(facesUtil.retrieveMessage("formMstAuditPeriodeTo") + " "
								+ facesUtil.retrieveMessage("validateDateByDaysMustBigger") + " "
								+ facesUtil.retrieveMessage("formMstAuditPeriodeFrom"));
						flag = true;
					}
				} else if (monthDateTo < monthDateFrom) {
					facesUtil.addErrMessage(facesUtil.retrieveMessage("formMstAuditPeriodeTo") + " "
							+ facesUtil.retrieveMessage("validateDateByMonthMustBigger") + " "
							+ facesUtil.retrieveMessage("formMstAuditPeriodeFrom"));
					flag = true;
				}
			} else if (yearDateTo < yearDateTo) {
				facesUtil.addErrMessage(facesUtil.retrieveMessage("formMstAuditPeriodeTo") + " "
						+ facesUtil.retrieveMessage("validateDateByYearMustBigger") + " "
						+ facesUtil.retrieveMessage("formMstAuditPeriodeFrom"));
				flag = true;
			}
		}
		
		return flag;
	}
	
	public void save() {
		try {
			if (!validate()) {
				if (mstAudit.getMstAuditId() != null) {
					mstAudit.setCreatedBy(facesUtil.retrieveUserLogin());
					mstAudit.setCreationDate(new Timestamp(new Date().getTime()));
					mstAudit.setDelId(new Long(0));
					mstAudit.setEnabledFlag(Constants.CONSTANT_YES);
					mstAuditService.update(mstAudit);
				} else {
					mstAudit.setCreatedBy(facesUtil.retrieveUserLogin());
					mstAudit.setCreationDate(new Timestamp(new Date().getTime()));
					mstAudit.setDelId(new Long(0));
					mstAudit.setEnabledFlag(Constants.CONSTANT_YES);
					mstAuditService.save(mstAudit);
				}
				
				facesUtil.redirect("/pages/mstAudit/mstAudit.faces");
			}
		} catch (Exception e) {
			e.printStackTrace();
			facesUtil.addFacesMsg(FacesMessage.SEVERITY_ERROR, null, "Operation Failed : " + e.getMessage(), "");
		}
	}
	
	public void cancel() {
		try {
			facesUtil.redirect("/pages/mstAudit/mstAudit.faces");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getActionMode() {
		return actionMode;
	}

	public void setActionMode(String actionMode) {
		this.actionMode = actionMode;
	}

	public String getEditId() {
		return editId;
	}

	public void setEditId(String editId) {
		this.editId = editId;
	}

	public List<SelectItem> getSelectAuditFollowUp() {
		return selectAuditFollowUp;
	}

	public void setSelectAuditFollowUp(List<SelectItem> selectAuditFollowUp) {
		this.selectAuditFollowUp = selectAuditFollowUp;
	}

	public MstAuditService getMstAuditService() {
		return mstAuditService;
	}

	public void setMstAuditService(MstAuditService mstAuditService) {
		this.mstAuditService = mstAuditService;
	}

	public FacesUtil getFacesUtil() {
		return facesUtil;
	}

	public void setFacesUtil(FacesUtil facesUtil) {
		this.facesUtil = facesUtil;
	}

	public SimpleDateFormat getSdf() {
		return sdf;
	}

	public void setSdf(SimpleDateFormat sdf) {
		this.sdf = sdf;
	}

	public static String getNavigateSearch() {
		return NAVIGATE_SEARCH;
	}

	public MstAudit getMstAudit() {
		return mstAudit;
	}

	public void setMstAudit(MstAudit mstAudit) {
		this.mstAudit = mstAudit;
	}
	
}
