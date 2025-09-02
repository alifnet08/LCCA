package com.wo.module.common.bean;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import com.wo.module.common.constant.CommonConstants;
import com.wo.module.common.constant.Constants;
import com.wo.module.logAccess.model.LogAccess;
import com.wo.module.logAccess.service.LogAccessService;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.parameter.service.ParameterDetailService;
import com.wo.module.user.model.User;

public class CommonBean implements Serializable, CommonConstants {
	static Logger logger = Logger.getLogger(CommonBean.class);
	private static final long serialVersionUID = -6844095679623633531L;

	/*
	 * property
	 */
	protected int paging;
	protected String inputDateFormat = INPUT_DATE_FORMAT;
	protected String inputDateLocale = INPUT_DATE_LOCALE;
	
	public String maxAttachmentSize;
	public String standardAttachmentSize;
	
	public ParameterDetailService parameterDetailService;
	
	public LogAccessService logAccessService;
	
	public void init() {
		setPaging(Constants.DEFAULT_PAGING_NUMBER);
		try {
			if (parameterDetailService != null) {
				ParameterDetail pd = parameterDetailService.getParameterDetailByParamDtlCode("MAX_ATTACHMENT_SIZE");
				if (pd != null) {
					setMaxAttachmentSize(pd.getNameIn());
				}
				ParameterDetail pd2 = parameterDetailService.getParameterDetailByParamDtlCode("STANDARD_ATTACHMENT_SIZE");
				if (pd2 != null) {
					setStandardAttachmentSize(pd2.getNameIn());
				}
			}
			
			String editId = retrieveRequestParam("id");
			String token = retrieveRequestParam("token");
			if(!StringUtils.isEmpty(token)) {
						editId = Constants.decryptString(token);
			}
			if(StringUtils.isEmpty(editId)){
				editId = retrieveRequestParam("viewId");
			}
			if(StringUtils.isEmpty(editId)){
				editId = retrieveRequestParam("CHOSEN_ID");
			}
			LogAccess logAccess = new LogAccess();
			if(!StringUtils.isEmpty(editId)){
				logAccess.setAccessId(new Long(editId));
			}
			HttpServletRequest hreq = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
			String viewId = hreq.getRequestURI();
			//to get parameter value on url string
			String paramValue = hreq.getQueryString();
			
			//add the parameter to viewId url if do exist
			if(paramValue != null && !StringUtils.isEmpty(paramValue)) {
				if(!paramValue.equals("null")) { //HttpServletRequest.getQueryString will return String value "null" if it is empty
					//System.out.println("paramValue: "+ paramValue);
					
					viewId += "?"+paramValue;
					
					//System.out.println("new View Id: "+ viewId);
				}
			}
			
			logAccess.setAccessTime(new Timestamp(new Date().getTime()));
			logAccess.setAccessAction(viewId);
			logAccess.setUser(getUserLogin());
			logAccess.setSourceIp(getIPAddress());
			try{
			if(logAccessService!=null) {
				logAccessService.save(logAccess);
			}
			}catch(Exception e){
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*private String getIPAddress(){
		HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
		String ipAddress = request.getHeader("X-FORWARDED-FOR");
		if(ipAddress == null){
			ipAddress = request.getRemoteAddr();
		}else{
			ipAddress = ipAddress.replaceFirst(",.*", "");
		}
		return ipAddress;
	}*/
	
	public static String getIPAddress() {
		HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
		
		String ip = request.getHeader("X-Forwarded-For");
		if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
			ip = request.getHeader("HTTP_X_FORWARDED");
		}
		if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
			ip = request.getHeader("HTTP_X_CLUSTER_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
			ip = request.getHeader("HTTP_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
			ip = request.getHeader("HTTP_FORWARDED");
		}
		if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
			ip = request.getHeader("HTTP_VIA");
		}
		if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
			ip = request.getHeader("REMOTE_ADDR");
		}
		if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}
	
	public String retrieveRequestParam(String key) {
        return FacesContext.getCurrentInstance()
                .getExternalContext().getRequestParameterMap().get(key);
    }
	
	public User getUserLogin() {
		
    	FacesContext fc = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) fc.getExternalContext().getSession(
					true);
		if(session != null && (session.getAttribute(Constants.SESSION_EMPLOYEE) != null)){
			return  (User)session.getAttribute(Constants.SESSION_EMPLOYEE);
		}else {
			return null;
		}
    	 
    }
	
	public String replaceUserToLowerCaseAndFirstLetterUpper(String name) {
		String nameTemp = "";
		
		if (name.contains(" ")) {
			String[] split = name.split(" ");
			for (String string : split) {
				char firstChar = string.charAt(0);
				String afterChar = string.substring(1);
				
				if (org.apache.commons.lang3.StringUtils.isBlank(nameTemp)) {
					nameTemp = String.valueOf(firstChar).concat(afterChar.toLowerCase());
				} else {
					nameTemp = nameTemp.concat(" ").concat(String.valueOf(firstChar).concat(afterChar.toLowerCase()));
				}
			}
		} else {
			char firstChar = name.charAt(0);
			String afterChar = name.substring(1);
			
			nameTemp = String.valueOf(firstChar).concat(afterChar.toLowerCase());
		}
		
		return nameTemp;
	}
	
	public int getPaging() {
		return paging;
	}

	public void setPaging(int paging) {
		this.paging = paging;
	}

	public String getInputDateFormat() {
		return inputDateFormat;
	}

	public void setInputDateFormat(String inputDateFormat) {
		this.inputDateFormat = inputDateFormat;
	}

	public String getInputDateLocale() {
		return inputDateLocale;
	}

	public void setInputDateLocale(String inputDateLocale) {
		this.inputDateLocale = inputDateLocale;
	}

	public ParameterDetailService getParameterDetailService() {
		return parameterDetailService;
	}

	public void setParameterDetailService(ParameterDetailService parameterDetailService) {
		this.parameterDetailService = parameterDetailService;
	}

	public String getStandardAttachmentSize() {
		return standardAttachmentSize;
	}

	public void setStandardAttachmentSize(String standardAttachmentSize) {
		this.standardAttachmentSize = standardAttachmentSize;
	}

	public String getMaxAttachmentSize() {
		return maxAttachmentSize;
	}

	public void setMaxAttachmentSize(String maxAttachmentSize) {
		this.maxAttachmentSize = maxAttachmentSize;
	}

	public LogAccessService getLogAccessService() {
		return logAccessService;
	}

	public void setLogAccessService(LogAccessService logAccessService) {
		this.logAccessService = logAccessService;
	}
	
}
