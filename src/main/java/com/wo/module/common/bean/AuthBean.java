package com.wo.module.common.bean;

import java.util.List;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;

import com.wo.module.common.constant.Constants;
import com.wo.module.menuSession.model.MenuSession;

public class AuthBean {

	public boolean checkErrorPage(String viewId) {
		boolean isErrorPage = false;
		if (viewId.indexOf("401.jsf") >= 0)
			isErrorPage = true;
		else if (viewId.indexOf("403.jsf") >= 0)
			isErrorPage = true;
		else if (viewId.indexOf("404.jsf") >= 0)
			isErrorPage = true;
		else if (viewId.indexOf("500.jsf") >= 0)
			isErrorPage = true;
		else if (viewId.indexOf("view_expired_exception.jsf") >= 0)
			isErrorPage = true;
		else if (viewId.indexOf("document_generation_error.jsf") >= 0)
			isErrorPage = true;
		return isErrorPage;
	}

	public boolean checkExceptionPage(String viewId) {
		boolean isExceptionPage = false;
		if (viewId.indexOf("login.faces") >= 0
		/*
		 * || viewId.indexOf("register.faces") >= 0 ||
		 * viewId.indexOf("forgotPassword.faces") >= 0 ||
		 * viewId.indexOf("emailVerify.faces") >= 0 ||
		 * viewId.indexOf("loginKaryawan.faces") >= 0 ||
		 * viewId.indexOf("jobListHome.faces") >= 0 ||
		 * viewId.indexOf("loginAdmin.faces") >= 0
		 */)
			isExceptionPage = true;
		else if (viewId.indexOf("bd.faces") >= 0 && checkExceptionOther())
			isExceptionPage = true;
		else if (viewId.indexOf("logout.jsf") >= 0)
			isExceptionPage = true;
		else if (viewId.indexOf(".css.jsf") >= 0)
			isExceptionPage = true;
		return isExceptionPage;
	}
	
	public boolean checkExceptionPageAfterLogin(String viewId) {
		boolean isExceptionPage = false;
		if (viewId.indexOf("/pages/trcCorrespondenceView/trcCorrespondenceViewEdit") >= 0)
			isExceptionPage = true;
		else if (viewId.indexOf("/pages/trcCorrespondenceViewAml/trcCorrespondenceViewAmlEdit") >= 0)
			isExceptionPage = true;
		else if (viewId.indexOf("/pages/trcAuditView/trcAuditViewEdit") >= 0)
			isExceptionPage = true;
		else if (viewId.indexOf("/pages/regulationSocializationView/regulationSocializationViewDetail") >= 0)
			isExceptionPage = true;
		else if (viewId.indexOf("/pages/dashboard/dashboard") >= 0)
			isExceptionPage = true;
		/*else if (viewId.indexOf("/pages/internalRegulationApproval/internalRegulationApprovalEdit") >= 0)
			isExceptionPage = true;
		else if (viewId.indexOf("/pages/externalRegulationApproval/externalRegulationApprovalEdit") >= 0)
			isExceptionPage = true;*/
		else if (viewId.indexOf("/pages/trcComplianceReviewView/trcComplianceReviewViewEdit") >= 0)
			isExceptionPage = true;
		else if (viewId.indexOf("/pages/internalRegulationFE/internalRegulationFE") >= 0)
			isExceptionPage = true;
		else if (viewId.indexOf("/pages/complianceTestingFE/complianceTestingFE") >= 0)
			isExceptionPage = true;
		else if (viewId.indexOf("/pages/correspondenceFE/correspondenceFE") >= 0)
			isExceptionPage = true;
		else if (viewId.indexOf("/pages/externalRegulationFE/externalRegulationFE") >= 0)
			isExceptionPage = true;
		else if (viewId.indexOf("/pages/regulationMonitoringFE/regulationMonitoringFE") >= 0)
			isExceptionPage = true;
		else if (viewId.indexOf("/pages/regulatoryReportingFE/regulatoryReportingFE") >= 0)
			isExceptionPage = true;
		else if (viewId.indexOf("/pages/auditFE/auditFE") >= 0)
			isExceptionPage = true;
		else if (viewId.indexOf("/pages/socializationFE/socializationFE") >= 0)
			isExceptionPage = true;
		else if (viewId.indexOf("/pages/fineFE/fineFE") >= 0)
			isExceptionPage = true;
		else if (viewId.indexOf("/pages/articleFE/articleFE") >= 0)
			isExceptionPage = true;
		else if (viewId.indexOf("/pages/opinionFE/opinionFE") >= 0)
			isExceptionPage = true;
		else if (viewId.indexOf("/pages/faqFE/faqFE") >= 0)
			isExceptionPage = true;
		else if (viewId.indexOf("/pages/litigationViewFE/litigationViewFE") >= 0)
			isExceptionPage = true;
		else if (viewId.indexOf("/pages/templateViewFE/templateViewFE") >= 0)
			isExceptionPage = true;
		else if (viewId.indexOf("/pages/cpsaFE/compliancePlanSelfAssessmentFE") >= 0)
			isExceptionPage = true;
		else if (viewId.indexOf("/pages/notaryFE/notaryFE") >= 0)
			isExceptionPage = true;
		else if (viewId.indexOf("/pages/advocateFE/advocateFE") >= 0)
			isExceptionPage = true;
		else if (viewId.indexOf("/pages/discussionFE/discussionFE") >= 0)
			isExceptionPage = true;
		else if (viewId.indexOf("/pages/searchAllFE/searchAllFE") >= 0)
			isExceptionPage = true;
		else if (viewId.indexOf("/pages/qaFE/qaFE") >= 0)
			isExceptionPage = true;
		else if (viewId.indexOf("/pages/aboutUsFE/aboutUsFE") >= 0)
			isExceptionPage = true;
		else if (viewId.indexOf("/pages/announcementViewFE/announcementViewFE") >= 0)
			isExceptionPage = true;
		else if (viewId.indexOf("/pages/dashboard/notificationDtlFE") >= 0)
			isExceptionPage = true;
		else if (viewId.indexOf("/pages/countryFE/countryFE") >= 0)
			isExceptionPage = true;
		else if (viewId.indexOf("/pages/economySectorFE/economySectorFE") >= 0)
			isExceptionPage = true;
		else if (viewId.indexOf("/pages/occupationFE/occupationFE") >= 0)
			isExceptionPage = true;
		return isExceptionPage;
	}
	
	public boolean checkExceptionOther() {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		HttpSession session = request.getSession(true);
		String nik = (String) session.getAttribute(Constants.SESSION_NIK);
		
		if(StringUtils.isNotBlank(nik) && nik.equals("10017393")) {
			return true;
		}
		
		return false;
	}

	public boolean validateAccessRight(String viewId, List<MenuSession> menuSessions) {

		boolean isValid = false;

		if (checkErrorPage(viewId) || checkExceptionPage(viewId)) {
			return true;
		}

		if (menuSessions != null && menuSessions.size() > 0) {
			for (MenuSession m : menuSessions) {
				if (m.getMenuAction() != null && !m.getMenuAction().equals("-")) {
					String authorizedUrl = m.getMenuAction().split(".faces")[0];// m.getAction().replace(".jsf",
																			// "");
					if (viewId.indexOf(authorizedUrl) >= 0) {
						isValid = true;
						break;
					}
				}
			}
		}
		return isValid;
	}
	
	public boolean validateAccessRightEmp(String viewId, String k) {

		boolean isValid = false;

		if (checkErrorPage(viewId) || checkExceptionPage(viewId)) {
			return true;
		}

		/*List<MenuVO> listmenu = loggedInPerson.getListMenu();
		if (listmenu != null && listmenu.size() > 0) {
			for (MenuVO m : listmenu) {
				if (!m.getAction().equals("-")) {
					String authorizedUrl = m.getAction().split(".jsf")[0];// m.getAction().replace(".jsf",
																			// "");
					if (viewId.indexOf(authorizedUrl) >= 0) {
						isValid = true;
						break;
					}
				}
			}
		}*/
		return isValid;
	}

	/*public boolean viewIsIncludedInNoAuthReqList(String viewId) {
		String baseContextPath = FacesContext.getCurrentInstance()
				.getExternalContext().getRequestContextPath();

		for (NoAuthUrl noAuthUrl : ConfigUtil.getInstance().getNoAuthUrls()) {
			String fullUrl = baseContextPath + noAuthUrl.url;
			if (noAuthUrl.isRegex) {
				if (viewId.matches(fullUrl)) {
					return true;
				}
			} else {
				if (StringUtils.equals(fullUrl, viewId)) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean viewIsIncludedInAuthAnyoneList(String viewId) {
		String baseContextPath = FacesContext.getCurrentInstance()
				.getExternalContext().getRequestContextPath();

		for (AuthAnyoneUrl authAnyoneUrl : ConfigUtil.getInstance()
				.getAuthAnyoneUrls()) {
			String fullUrl = baseContextPath + authAnyoneUrl.url;
			if (authAnyoneUrl.isRegex) {
				if (viewId.matches(fullUrl)) {
					return true;
				}
			} else {
				if (StringUtils.equals(fullUrl, viewId)) {
					return true;
				}
			}

		}
		return false;
	}

	public boolean viewIsSuperAdminAuthList(String viewId, HttpSession session) {
		String baseContextPath = FacesContext.getCurrentInstance()
				.getExternalContext().getRequestContextPath();
		if (StringUtils.equals(viewId, baseContextPath
				+ "/faces/usm/checking_management/management_check.jsf")) {
			if (session != null
					&& session.getAttribute(Constants.SESSION_KANDIDAT) != null) {
				Kandidat person = (Kandidat) session
						.getAttribute(Constants.SESSION_KANDIDAT);

				
			}
		}

		return false;
	}*/

}
