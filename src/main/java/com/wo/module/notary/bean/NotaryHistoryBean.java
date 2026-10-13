package com.wo.module.notary.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.event.ActionEvent;

import org.apache.commons.lang3.StringUtils;

import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.Constants;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.notary.constant.NotaryConstants;
import com.wo.module.notary.model.NotaryHistory;
import com.wo.module.notary.service.NotaryService;
import com.wo.module.responsibility.model.Responsibility;
import com.wo.module.responsibility.service.ResponsibilityService;
import com.wo.module.user.model.User;
import com.wo.module.user.service.UserService;

public class NotaryHistoryBean extends CommonBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private String notaryName;
	private Date tanggalDari;
	private Date tanggalSampai;
	private int paging;
	private List<NotaryHistory> historyList;
	private NotaryService notaryService;
	private UserService userService;
	private ResponsibilityService responsibilityService;
	public FacesUtil facesUtil;
	private String currentResponsibilityName;

	@PostConstruct
	public void init() {
		super.init();
		paging = Constants.DEFAULT_PAGING_NUMBER;
		historyList = new ArrayList<NotaryHistory>();
		resolveCurrentResponsibility();
		search(null);
	}

	private void resolveCurrentResponsibility() {
		currentResponsibilityName = "";
		try {
			if (userService == null || responsibilityService == null || facesUtil == null) {
				return;
			}
			String nik = facesUtil.retrieveUserLogin();
			if (StringUtils.isBlank(nik)) {
				return;
			}
			User user = userService.getUserByNik(nik);
			if (user == null || user.getResponsibilityId() == null) {
				return;
			}
			Responsibility responsibility = responsibilityService.findById(user.getResponsibilityId());
			if (responsibility != null) {
				currentResponsibilityName = responsibility.getName();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean isExportAllowed() {
		return !StringUtils.equalsIgnoreCase(NotaryConstants.RESPONSIBILITY_CDU_MAKER, currentResponsibilityName)
				&& !StringUtils.equalsIgnoreCase(NotaryConstants.RESPONSIBILITY_CDU_CHECKER, currentResponsibilityName);
	}

	public void search(ActionEvent actionEvent) {
		Date dari = startOfDay(tanggalDari);
		Date sampai = endOfDay(tanggalSampai);
		historyList = notaryService.searchHistory(notaryName, dari, sampai);
	}

	private Date startOfDay(Date date) {
		if (date == null) {
			return null;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	private Date endOfDay(Date date) {
		if (date == null) {
			return null;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		return calendar.getTime();
	}

	public String getNotaryName() {
		return notaryName;
	}

	public void setNotaryName(String notaryName) {
		this.notaryName = notaryName;
	}

	public Date getTanggalDari() {
		return tanggalDari;
	}

	public void setTanggalDari(Date tanggalDari) {
		this.tanggalDari = tanggalDari;
	}

	public Date getTanggalSampai() {
		return tanggalSampai;
	}

	public void setTanggalSampai(Date tanggalSampai) {
		this.tanggalSampai = tanggalSampai;
	}

	public int getPaging() {
		return paging;
	}

	public void setPaging(int paging) {
		this.paging = paging;
	}

	public List<NotaryHistory> getHistoryList() {
		return historyList;
	}

	public void setHistoryList(List<NotaryHistory> historyList) {
		this.historyList = historyList;
	}

	public NotaryService getNotaryService() {
		return notaryService;
	}

	public void setNotaryService(NotaryService notaryService) {
		this.notaryService = notaryService;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public ResponsibilityService getResponsibilityService() {
		return responsibilityService;
	}

	public void setResponsibilityService(ResponsibilityService responsibilityService) {
		this.responsibilityService = responsibilityService;
	}

	public FacesUtil getFacesUtil() {
		return facesUtil;
	}

	public void setFacesUtil(FacesUtil facesUtil) {
		this.facesUtil = facesUtil;
	}

}
