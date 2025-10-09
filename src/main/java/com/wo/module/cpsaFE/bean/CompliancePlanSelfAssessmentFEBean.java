package com.wo.module.cpsaFE.bean;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.primefaces.PrimeFaces;
import org.primefaces.model.SortOrder;
import org.primefaces.model.StreamedContent;

import com.wo.module.common.bean.CommonPagingFEBean;
import com.wo.module.common.constant.CommonConstants;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.cpsaFE.constant.CompliancePlanSelfAssessmentFEConstant;
import com.wo.module.cpsaFE.service.CompliancePlanSelfAssessmentFEService;
import com.wo.module.cpsaFE.vo.CompliancePlanSelfAssessmentFEVo;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.parameter.model.ParameterHeader;
import com.wo.module.user.model.User;

public class CompliancePlanSelfAssessmentFEBean extends CommonPagingFEBean<CompliancePlanSelfAssessmentFEVo> implements Serializable{

	private static final long serialVersionUID = -6261375361895618730L;
	private static final Logger logger = Logger.getLogger(CompliancePlanSelfAssessmentFEBean.class);
	private static final String NAVIGATE_VIEW = CompliancePlanSelfAssessmentFEConstant.NAVIGATE_CPSA_EDIT;
	
	private CompliancePlanSelfAssessmentFEService compliancePlanSelfAssessmentFEService;
	
	private List<SelectItem> cpsaTypeList;
	
	private SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
	
	public void addMessage(String summary) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, null);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public void addErrMessage(String summary) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, null);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}
	
	@PostConstruct
	public void init() {
		super.init();
		if (facesUtil.getSessionAttribute("FIRST_CPSA_FE") != null) {
			Object dataInt = facesUtil.getSessionAttribute("FIRST_CPSA_FE");
			setInitFirst((Integer) dataInt);
		}
		initComponent();
		searchData();		
		if (facesUtil.getSessionAttribute("FIRST_CPSA_FE") != null) {
			facesUtil.removeSessionAttribute("FIRST_CPSA_FE");
		}
	}
	
	private void initComponent() {
		initSelectCpsaTypeList();
	}
	
	public String toEncrypt(Long cpsaId){
		try {
			return Constants.encryptString(cpsaId.toString());
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	private void initSelectCpsaTypeList() {
		cpsaTypeList = new ArrayList<SelectItem>();
		try {
			List<ParameterDetail> geteCpsaType = parameterDetailService.getParameterDetailByParamCode(ParameterHeader.PARAM_HEAD_CPSA_TYPE);
			for (ParameterDetail pd : geteCpsaType) {
				SelectItem si = new SelectItem();
				si.setLabel(pd.getName());
				si.setValue(pd.getParameterDtlCode());
				
				cpsaTypeList.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("deprecation")
	public StreamedContent getDownloadExcel() {
		StreamedContent downloadExcelSc = null;
		try {
			String cpsaId = facesUtil.retrieveRequestParam("cpsaId");
			String userId1 = facesUtil.retrieveRequestParam("userIdData1");
			downloadExcelSc = compliancePlanSelfAssessmentFEService.generateDataExcel(new Long(cpsaId), new Long(userId1));
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return downloadExcelSc;
	}

	@SuppressWarnings("rawtypes")
	public List<CompliancePlanSelfAssessmentFEVo> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize, String sortField,
			SortOrder sortOrder) throws Exception {
		return compliancePlanSelfAssessmentFEService.searchData(getSearchCriteria(), getFirst(), getPageSize(), null, null);
	}
	
	@SuppressWarnings("rawtypes") 
	public Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception {
		return compliancePlanSelfAssessmentFEService.searchCountData(getSearchCriteria());
	}
	
	public void lockCpsa(int rowIdx) {
		CompliancePlanSelfAssessmentFEVo data = getListData().get(rowIdx);
		
		
		if(validateAccess(rowIdx)) {
			compliancePlanSelfAssessmentFEService.lockUnlockCpsa(data, getUserLogin(), true);
			
			getListData().get(rowIdx).setLockFlag(CommonConstants.RECORD_FLAG_YES);
		}else {
			User userLock = getUserService().getUserByNik(data.getUserNikLock());
			
			addErrMessage(
					facesUtil.retrieveMessage("formCompliancePlanSelfAssessmentTitleShort") + 
					" is unlocked by " + 
					userLock.getName());
		}
		
		PrimeFaces.current().ajax().update("dataList");
	}
	
	public void unlockCpsa(int rowIdx) {
		CompliancePlanSelfAssessmentFEVo data = getListData().get(rowIdx);
		
		compliancePlanSelfAssessmentFEService.lockUnlockCpsa(data, getUserLogin(), false);
		getListData().get(rowIdx).setLockFlag(CommonConstants.RECORD_FLAG_NO);
		getListData().get(rowIdx).setUserNikLock(getUserLogin().getNik());
		
		PrimeFaces.current().ajax().update("dataList");
	}
	
	public boolean validateAccess(int rowIdx) {
		CompliancePlanSelfAssessmentFEVo data = getListData().get(rowIdx);
		try {
			if(data.getUserNikLock() != null) {
				if(data.getLockFlag().equals(CommonConstants.RECORD_FLAG_NO) && data.getUserNikLock().equals(facesUtil.getUserLogin().getNik())) {
					return true;
				}else {
					return false;
				}
			}else {
				return false;
			}
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
			
	}
	
	public boolean validateDisableLink(int rowIdx) {
		CompliancePlanSelfAssessmentFEVo data = getListData().get(rowIdx);
		//true = disable link
		//priority = statusPic > lockFlag > nikLock
		String statusPicTemp = "";
		
		if(data.getStatusPic() != null) {
			//to make sure null can also go through
			statusPicTemp = data.getStatusPic();
		}
		
		
		if(statusPicTemp.equals(CompliancePlanSelfAssessmentFEConstant.STATUS_COMPLIANCE_OPEN) || 
				statusPicTemp.isBlank()) {
			if(StringUtils.isNotBlank(data.getLockFlag()) && 
					data.getLockFlag().equals(CommonConstants.RECORD_FLAG_NO)) {
				if(data.getUserNikLock() != null) {
					if(data.getUserNikLock().equals(facesUtil.getUserLogin().getNik())) {
						return false;
					}else {
						return true;
					}
				}else {
					//it should be newly created if its null
					return true;
				}
			}else {
				//if it locked, its should disable on both PIC
				return true;
			}
		}else {
			//any cpsa status other than CPSA_OPEN should allow both PIC to access CPSA FE
			return false;
		}
	
		
	}
	
	public CompliancePlanSelfAssessmentFEService getCompliancePlanSelfAssessmentFEService() {
		return compliancePlanSelfAssessmentFEService;
	}

	public void setCompliancePlanSelfAssessmentFEService(
			CompliancePlanSelfAssessmentFEService compliancePlanSelfAssessmentFEService) {
		this.compliancePlanSelfAssessmentFEService = compliancePlanSelfAssessmentFEService;
	}

	public List<SelectItem> getCpsaTypeList() {
		return cpsaTypeList;
	}

	public void setCpsaTypeList(List<SelectItem> cpsaTypeList) {
		this.cpsaTypeList = cpsaTypeList;
	}

	public SimpleDateFormat getSdf() {
		return sdf;
	}

	public void setSdf(SimpleDateFormat sdf) {
		this.sdf = sdf;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public static Logger getLogger() {
		return logger;
	}
	
	public static String getNavigateView() {
		return NAVIGATE_VIEW;
	}	
}
