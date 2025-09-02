package com.wo.module.email.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.model.SelectItem;

import org.apache.log4j.Logger;
import org.primefaces.PrimeFaces;
import org.springframework.util.StringUtils;

import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.js.JsUtil;
import com.wo.module.common.paging.DBLazyDataModel;
import com.wo.module.common.paging.DefaultSearchObject;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.email.constant.EmailConstant;
import com.wo.module.email.service.EmailService;
import com.wo.module.email.vo.EmailVO;
import com.wo.module.lov.bean.FacesUtil;

public class EmailBean extends CommonBean implements Serializable, EmailConstant {

	private static final long serialVersionUID = 302252065067447802L;

	static Logger logger = Logger.getLogger(EmailBean.class);

	private String searchVal;
	private String searchEmailStatus;
	private String searchEmailType;
	private Date searchEmailDateFrom;
	private Date searchEmailDateTo;
	private Date searchLastSentDateFrom;
	private Date searchLastSentDateTo;
	private EmailService emailService;
	private DBLazyDataModel<EmailVO> tableModel;
	private List<SelectItem> selectEmailTypes;
	private List<SelectItem> selectStatus;
	public FacesUtil facesUtil;

	@PostConstruct
	public void init() {
		super.init();
		tableModel = new DBLazyDataModel<EmailVO>(emailService, paging);
		constructSelectComponent();
	}

	@SuppressWarnings("rawtypes")
	public void search() {
		List<SearchObject> searchCriteria = new ArrayList<SearchObject>();
		if (StringUtils.hasText(searchEmailType)) {
			searchCriteria.add(new DefaultSearchObject(SEARCH_BY_EMAIL_TYPE, searchEmailType));
		}
		if (StringUtils.hasText(searchEmailStatus)) {
			searchCriteria.add(new DefaultSearchObject(SEARCH_BY_EMAIL_STATUS, searchEmailStatus));
		}
		if (searchEmailDateFrom != null) {
			searchCriteria.add(new DefaultSearchObject(SEARCH_BY_EMAIL_DATE_FROM, searchEmailDateFrom));
		}
		if (searchEmailDateTo != null) {
			searchCriteria.add(new DefaultSearchObject(SEARCH_BY_EMAIL_DATE_TO, searchEmailDateTo));
		}
		if (searchLastSentDateFrom != null) {
			searchCriteria.add(new DefaultSearchObject(SEARCH_BY_EMAIL_LAST_SENT_DATE_FROM, searchLastSentDateFrom));
		}
		if (searchLastSentDateTo != null) {
			searchCriteria.add(new DefaultSearchObject(SEARCH_BY_EMAIL_LAST_SENT_DATE_TO, searchLastSentDateTo));
		}

		tableModel.setSearchCriteria(searchCriteria);
	}
	
	private void constructSelectComponent() {
		setupEmailTypes();
		setupStatus();
	}
	
	private void setupEmailTypes() {
		selectEmailTypes = new ArrayList<SelectItem>();
		try {
			selectEmailTypes.add(new SelectItem(EMAIL_TYPE_AUDIT));
			selectEmailTypes.add(new SelectItem(EMAIL_TYPE_COMPLIANCE));
			selectEmailTypes.add(new SelectItem(EMAIL_TYPE_CORRESPONDENCE));
			selectEmailTypes.add(new SelectItem(EMAIL_TYPE_RMD));
			selectEmailTypes.add(new SelectItem(EMAIL_TYPE_SOCIALIZATION));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		JsUtil.reInitSelect2();
	}
	
	private void setupStatus() {
		selectStatus = new ArrayList<SelectItem>();
		try {
			selectStatus.add(new SelectItem(EMAIL_STATUS_SUCCESS));
			selectStatus.add(new SelectItem(EMAIL_STATUS_ERROR));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		JsUtil.reInitSelect2();
	}
	
	public void reset() {
		searchEmailStatus = null;
		searchEmailType = null;
		searchEmailDateFrom = null;
		searchEmailDateTo = null;
		searchLastSentDateFrom = null;
		searchLastSentDateTo = null;
		search();
		
		PrimeFaces.current().executeScript("reInitSelect2();");
	}	
	
	public void resend(Long emailId, String emailType) {
		try {
			emailService.updateEmailFollowupResend(emailId, emailType, facesUtil.retrieveUserLogin());
			search();
			facesUtil.addSuccessMsg(facesUtil.retrieveMessage("textResendEmailSuccess"));
		}catch (Exception ex) {
			facesUtil.addFacesMsg(FacesMessage.SEVERITY_ERROR, null, "Operation Failed : " + ex.getMessage(), "");
		}
	}

	public FacesUtil getFacesUtil() {
		return facesUtil;
	}

	public void setFacesUtil(FacesUtil facesUtil) {
		this.facesUtil = facesUtil;
	}

	public Boolean getIsLogin() {
		if (facesUtil.getSessionAttribute(Constants.SESSION_EMPLOYEE) != null
				|| facesUtil.getSessionAttribute(Constants.SESSION_KANDIDAT) != null)
			return true;
		else
			return false;
	}

	public DBLazyDataModel<EmailVO> getTableModel() {
		return tableModel;
	}

	public void setTableModel(DBLazyDataModel<EmailVO> tableModel) {
		this.tableModel = tableModel;
	}

	public String getSearchVal() {
		return searchVal;
	}

	public void setSearchVal(String searchVal) {
		this.searchVal = searchVal;
	}

	public EmailService getEmailService() {
		return emailService;
	}

	public void setEmailService(EmailService emailService) {
		this.emailService = emailService;
	}

	public List<SelectItem> getSelectEmailTypes() {
		return selectEmailTypes;
	}

	public void setSelectEmailTypes(List<SelectItem> selectEmailTypes) {
		this.selectEmailTypes = selectEmailTypes;
	}

	public List<SelectItem> getSelectStatus() {
		return selectStatus;
	}

	public void setSelectStatus(List<SelectItem> selectStatus) {
		this.selectStatus = selectStatus;
	}

	public String getSearchEmailStatus() {
		return searchEmailStatus;
	}

	public void setSearchEmailStatus(String searchEmailStatus) {
		this.searchEmailStatus = searchEmailStatus;
	}

	public String getSearchEmailType() {
		return searchEmailType;
	}

	public void setSearchEmailType(String searchEmailType) {
		this.searchEmailType = searchEmailType;
	}

	public Date getSearchEmailDateFrom() {
		return searchEmailDateFrom;
	}

	public void setSearchEmailDateFrom(Date searchEmailDateFrom) {
		this.searchEmailDateFrom = searchEmailDateFrom;
	}

	public Date getSearchEmailDateTo() {
		return searchEmailDateTo;
	}

	public void setSearchEmailDateTo(Date searchEmailDateTo) {
		this.searchEmailDateTo = searchEmailDateTo;
	}

	public Date getSearchLastSentDateFrom() {
		return searchLastSentDateFrom;
	}

	public void setSearchLastSentDateFrom(Date searchLastSentDateFrom) {
		this.searchLastSentDateFrom = searchLastSentDateFrom;
	}

	public Date getSearchLastSentDateTo() {
		return searchLastSentDateTo;
	}

	public void setSearchLastSentDateTo(Date searchLastSentDateTo) {
		this.searchLastSentDateTo = searchLastSentDateTo;
	}

}