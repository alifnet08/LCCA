package com.wo.module.common.utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.nio.file.Files;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.faces.context.FacesContext;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509ExtendedTrustManager;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Base64Utils;

import com.google.gson.Gson;
import com.wo.module.common.constant.CommonConstants;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.exception.CustomAPIException;
import com.wo.module.common.model.DownloadResponse;
import com.wo.module.common.model.SendEmailResponse;
import com.wo.module.common.model.UploadResponse;
import com.wo.module.common.util.FileUtil;
import com.wo.module.cpsa.constant.CompliancePlanSelfAssessmentConstant;
import com.wo.module.engine.service.SendEmailService;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.parameter.service.ParameterDetailService;

public class CallApiManager {
	
private static final Logger logger = LoggerFactory.getLogger(CallApiManager.class);

public static void deleteFile(String fileId,ParameterDetailService parameterDetailService,FileUtil fileUtil) throws Exception {
	if(fileId!=null) {
		ParameterDetail pdIsAPI = parameterDetailService.getParameterDetailByParamDtlCode("IS_USING_API");
		if(pdIsAPI.getNameIn()!=null && pdIsAPI.getNameIn().equals("true")) {
			callDeleteAPI(fileId,parameterDetailService);
		}else {
			ParameterDetail pdFilePath = parameterDetailService.getParameterDetailByParamDtlCode("ATTACHMENT_FILE_PATH");
			String filePath = pdFilePath.getNameIn().concat(fileId);
			fileUtil.deleteFile(filePath);
		}
	}
}


public static void downloadFile(String fileId, String fileName, byte[] content,ParameterDetailService parameterDetailService) throws Exception {
	
		String data = null;
		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext()
				.getResponse();
		if(fileName!=null && (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg"))) {
			response.setContentType("image/jpeg");
		}else if(fileName!=null && fileName.endsWith(".pdf")) {
			response.setContentType("application/pdf");
		}else if(fileName!=null && fileName.endsWith(".xls")) {
			response.setContentType("application/vnd.ms-excel");
		}else if(fileName!=null && fileName.endsWith(".xlsx")) {
			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		}else if(fileName!=null && fileName.endsWith(".png")) {
			response.setContentType("image/png");
		}else if(fileName!=null && fileName.endsWith(".zip")) {
			response.setContentType("application/zip");
		}else if(fileName!=null && fileName.endsWith(".gif")) {
			response.setContentType("image/gif");
		}else if(fileName!=null && fileName.endsWith(".doc")) {
			response.setContentType("application/msword");
		}else if(fileName!=null && fileName.endsWith(".docx")) {
			response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
		}else if(fileName!=null && fileName.endsWith(".ppt")) {
			response.setContentType("application/vnd.ms-powerpoint");
		}
		
		if(!StringUtils.isEmpty(fileId)) {
			ParameterDetail pdIsAPI = parameterDetailService.getParameterDetailByParamDtlCode("IS_USING_API");
			if(pdIsAPI.getNameIn()!=null && pdIsAPI.getNameIn().equals("true")) {
				data = callDownloadAPI(fileId, parameterDetailService);
				response.setHeader("Content-Disposition", "filename=\""+fileName+"\"");
				response.getOutputStream().write(Base64Utils.decodeFromString(data));
			}else {
				ParameterDetail pdFilePath = parameterDetailService.getParameterDetailByParamDtlCode("ATTACHMENT_FILE_PATH");
				String fullPath = pdFilePath.getNameIn().concat(fileId);
//				String fullPath = pdFilePath.getNameIn().concat(fileName);
				File file = new File(fullPath);
				byte[] fileByte = Files.readAllBytes(file.toPath());
				response.setHeader("Content-Disposition", "filename=\""+fileName+"\"");
				response.getOutputStream().write(fileByte);
			}
			
		}else {
			response.setHeader("Content-Disposition", "filename=\""+fileName+"\"");
			response.getOutputStream().write(content);
		}
			
			response.getOutputStream().flush();
			response.getOutputStream().close();
			FacesContext.getCurrentInstance().responseComplete();
	
	
}

public static void downloadFileCpsa(String fileId, String fileName, byte[] content,ParameterDetailService parameterDetailService) throws Exception {
	
	String data = null;
	HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext()
			.getResponse();
	if(fileName!=null && (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg"))) {
		response.setContentType("image/jpeg");
	}else if(fileName!=null && fileName.endsWith(".pdf")) {
		response.setContentType("application/pdf");
	}else if(fileName!=null && fileName.endsWith(".xls")) {
		response.setContentType("application/vnd.ms-excel");
	}else if(fileName!=null && fileName.endsWith(".xlsx")) {
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
	}else if(fileName!=null && fileName.endsWith(".png")) {
		response.setContentType("image/png");
	}else if(fileName!=null && fileName.endsWith(".zip")) {
		response.setContentType("application/zip");
	}else if(fileName!=null && fileName.endsWith(".gif")) {
		response.setContentType("image/gif");
	}else if(fileName!=null && fileName.endsWith(".doc")) {
		response.setContentType("application/msword");
	}else if(fileName!=null && fileName.endsWith(".docx")) {
		response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
	}else if(fileName!=null && fileName.endsWith(".ppt")) {
		response.setContentType("application/vnd.ms-powerpoint");
	}
	
	if(!StringUtils.isEmpty(fileId)) {
		ParameterDetail pdIsAPI = parameterDetailService.getParameterDetailByParamDtlCode("IS_USING_API");
		if(pdIsAPI.getNameIn()!=null && pdIsAPI.getNameIn().equals("true")) {
			data = callDownloadAPI(fileId, parameterDetailService);
			response.setHeader("Content-Disposition", "filename=\""+fileName+"\"");
			response.getOutputStream().write(Base64Utils.decodeFromString(data));
		}else {
			ParameterDetail pdFilePath = parameterDetailService.getParameterDetailByParamDtlCode("ATTACHMENT_FILE_PATH");
			String fullPath = pdFilePath.getNameIn().concat(CompliancePlanSelfAssessmentConstant.FOLDER_CPSA).concat(CommonConstants.FILE_SEPARATOR).concat(fileId);
			File file = new File(fullPath);
			byte[] fileByte = Files.readAllBytes(file.toPath());
			response.setHeader("Content-Disposition", "filename=\""+fileName+"\"");
			response.getOutputStream().write(fileByte);
		}
		
	}else {
		response.setHeader("Content-Disposition", "filename=\""+fileName+"\"");
		response.getOutputStream().write(content);
	}
		
		response.getOutputStream().flush();
		response.getOutputStream().close();
		FacesContext.getCurrentInstance().responseComplete();


}

@SuppressWarnings("unused")
public static String callDeleteAPI(String fileId,ParameterDetailService parameterDetailService) throws Exception {
	
	ParameterDetail pdURL = parameterDetailService.getParameterDetailByParamDtlCode("BODY_URL_DELETE");
    ParameterDetail pdPassword = parameterDetailService.getParameterDetailByParamDtlCode("BODY_PASSWORD");
    ParameterDetail pdUserName = parameterDetailService.getParameterDetailByParamDtlCode("BODY_USERNAME");
    ParameterDetail pdApiKey = parameterDetailService.getParameterDetailByParamDtlCode("HEADER_BTPN_KEY");
    ParameterDetail pdPostmanToken = parameterDetailService.getParameterDetailByParamDtlCode("HEADER_POSTMAN_TOKEN");
    ParameterDetail pdContentType = parameterDetailService.getParameterDetailByParamDtlCode("BODY_CONTENT_TYPE");
    ParameterDetail pdTerminalId = parameterDetailService.getParameterDetailByParamDtlCode("BODY_TERMINAL_ID");
    
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    
	TrustManager[] trustAllCerts = new TrustManager[] { new X509ExtendedTrustManager() {

		@Override
		public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
			
		}

		@Override
		public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
			
		}

		@Override
		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}

		@Override
		public void checkClientTrusted(X509Certificate[] chain, String authType, Socket socket)
				throws CertificateException {
			
		}

		@Override
		public void checkClientTrusted(X509Certificate[] chain, String authType, SSLEngine engine)
				throws CertificateException {
			
		}

		@Override
		public void checkServerTrusted(X509Certificate[] chain, String authType, Socket socket)
				throws CertificateException {
			
		}

		@Override
		public void checkServerTrusted(X509Certificate[] chain, String authType, SSLEngine engine)
				throws CertificateException {
			
		}
		
	} };

	// Install the all-trusting trust manager
	SSLContext sc = SSLContext.getInstance("TLS");
	sc.init(null, trustAllCerts, new java.security.SecureRandom());
	HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

	// Create all-trusting host name verifier
	HostnameVerifier allHostsValid = new HostnameVerifier() {
	    @Override
	    public boolean verify(String hostname, SSLSession session) {
	return true;
	    }
	};

	// Install the all-trusting host verifier
	HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
	
	URL url = new URL(pdURL.getNameIn());
	
	HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	
	
	conn.setRequestMethod("POST");
	conn.setRequestProperty("Content-Type", "application/json; utf-8");
	conn.setRequestProperty("Accept", "application/json");
	conn.setRequestProperty("Cache-Control","no-cache");
	conn.setRequestProperty("BTPN-ApiKey", pdApiKey.getNameIn());
	conn.setRequestProperty("Postman-Token",pdPostmanToken.getNameIn());
	conn.setDoOutput(true);
    
	String jsonInputString = "{\r\n    \"request\":\r\n    {\r\n        \"commonParam\":\r\n        {\r\n            \"amount\":\"\",\r\n            \"currencyfee\":\"\",\r\n            \"original\":\"\",\r\n            \"referenceNo\":\"\",\r\n            \"processingCode\":\"\",\r\n            \"fee\":\"\",\r\n            \"currencyAmount\":\"\",\r\n            \"channelType\":\"\",\r\n            \"terminalId\":\""+pdTerminalId.getNameIn()+"\",\r\n            \"userId\":\"\",\r\n            \"acqId\":\"\",\r\n            \"transmissionDateTime\":\"\",\r\n            \"node\":\"\",\r\n            \"terminalName\":\"\",\r\n            \"requestId\":\"\",\r\n            \"pan\":\"\",\r\n            \"channelId\":\"\"\r\n        },\r\n        \"contentType\":\""+pdContentType.getNameIn()+"\",\r\n        \"fileID\":\""+fileId+"\"\r\n    },\r\n    \"authentication\":\r\n    {\r\n        \"password\":\""+pdPassword.getNameIn()+"\",\r\n        \"username\":\""+pdUserName.getNameIn()+"\"\r\n    }\r\n}";
   
    //System.out.println("jsonInputString=="+jsonInputString);
    try(OutputStream os = conn.getOutputStream()) {
	    byte[] input = jsonInputString.getBytes("utf-8");
	    os.write(input, 0, input.length);           
	}

	String result = "";
	try(BufferedReader br = new BufferedReader(
			  new InputStreamReader(conn.getInputStream(), "utf-8"))) {
			    StringBuilder response = new StringBuilder();
			    String responseLine = null;
			    while ((responseLine = br.readLine()) != null) {
			        response.append(responseLine.trim());
			    }
			    System.out.println(response.toString());
			    result = response.toString();
	}
	
	conn.disconnect();
    //Gson gson = new Gson();  
    //DownloadResponse data = gson.fromJson(result, DownloadResponse.class);
    

    return null;

}

public static String callDownloadAPI(String fileId,ParameterDetailService parameterDetailService) throws Exception {
	
	ParameterDetail pdURL = parameterDetailService.getParameterDetailByParamDtlCode("BODY_URL_DOWNLOAD");
    ParameterDetail pdRequestId = parameterDetailService.getParameterDetailByParamDtlCode("BODY_REQUEST_ID");
    ParameterDetail pdChannelId = parameterDetailService.getParameterDetailByParamDtlCode("BODY_CHANNEL_ID");
    ParameterDetail pdChannelType = parameterDetailService.getParameterDetailByParamDtlCode("BODY_CHANNEL_TYPE");
    ParameterDetail pdPassword = parameterDetailService.getParameterDetailByParamDtlCode("BODY_PASSWORD");
    ParameterDetail pdUserName = parameterDetailService.getParameterDetailByParamDtlCode("BODY_USERNAME");
    ParameterDetail pdApiKey = parameterDetailService.getParameterDetailByParamDtlCode("HEADER_BTPN_KEY");
    ParameterDetail pdPostmanToken = parameterDetailService.getParameterDetailByParamDtlCode("HEADER_POSTMAN_TOKEN");
    ParameterDetail pdContentType = parameterDetailService.getParameterDetailByParamDtlCode("BODY_CONTENT_TYPE");
    
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    
	TrustManager[] trustAllCerts = new TrustManager[] { new X509ExtendedTrustManager() {

		@Override
		public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
			
		}

		@Override
		public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
			
		}

		@Override
		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}

		@Override
		public void checkClientTrusted(X509Certificate[] chain, String authType, Socket socket)
				throws CertificateException {
			
		}

		@Override
		public void checkClientTrusted(X509Certificate[] chain, String authType, SSLEngine engine)
				throws CertificateException {
			
		}

		@Override
		public void checkServerTrusted(X509Certificate[] chain, String authType, Socket socket)
				throws CertificateException {
			
		}

		@Override
		public void checkServerTrusted(X509Certificate[] chain, String authType, SSLEngine engine)
				throws CertificateException {
			
		}
		
	} };

	// Install the all-trusting trust manager
	SSLContext sc = SSLContext.getInstance("TLS");
	sc.init(null, trustAllCerts, new java.security.SecureRandom());
	HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

	// Create all-trusting host name verifier
	HostnameVerifier allHostsValid = new HostnameVerifier() {
	    @Override
	    public boolean verify(String hostname, SSLSession session) {
	return true;
	    }
	};

	// Install the all-trusting host verifier
	HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
	
	URL url = new URL(pdURL.getNameIn());
	
	HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	
	
	conn.setRequestMethod("POST");
	conn.setRequestProperty("Content-Type", "application/json; utf-8");
	conn.setRequestProperty("Accept", "application/json");
	conn.setRequestProperty("Cache-Control","no-cache");
	conn.setRequestProperty("BTPN-ApiKey", pdApiKey.getNameIn());
	conn.setRequestProperty("Postman-Token",pdPostmanToken.getNameIn());
	conn.setDoOutput(true);
    
	String jsonInputString = "{" + 
    		"	\"$path\": \"\"," + 
    		"	\"$resourceID\": \"\"," + 
    		"	\"requestDownload\": {" + 
    		"		\"Authentication\": {" + 
    		"			\"password\": \""+pdPassword.getNameIn()+"\"," + 
    		"			\"username\": \""+pdUserName.getNameIn()+"\"" + 
    		"			}," + 
    		"			\"commonParam\": {" + 
    		"				\"amount\": \"\"," + 
    		"				\"currencyfee\": \"\"," + 
    		"				\"original\": \"\"," + 
    		"				\"referenceNo\": \"\"," + 
    		"				\"processingCode\": \"\"," + 
    		"				\"fee\": \"\"," + 
    		"				\"currencyAmount\": \"\"," + 
    		"				\"channelType\": \""+pdChannelType.getNameIn()+"\"," + 
    		"				\"terminalId\": \"\"," + 
    		"				\"userId\": \"\"," + 
    		"				\"acqId\": \"\"," + 
    		"				\"transmissionDateTime\": \""+sdf.format(new Date())+"\"," + 
    		"				\"node\": \"\"," + 
    		"				\"terminalName\": \"\"," + 
    		"				\"requestId\": \""+pdRequestId.getNameIn()+"\"," + 
    		"				\"pan\": \"\"," + 
    		"				\"channelId\": \""+pdChannelId.getNameIn()+"\"" + 
    		"				}," + 
    		"				\"contentType\": \""+pdContentType.getNameIn()+"\"," + 
    		"				\"fileID\": \""+fileId+"\"" + 
    		"				}" + 
    		"	}";
   
    //System.out.println("jsonInputString=="+jsonInputString);
    try(OutputStream os = conn.getOutputStream()) {
	    byte[] input = jsonInputString.getBytes("utf-8");
	    os.write(input, 0, input.length);           
	}

	String result = "";
	try(BufferedReader br = new BufferedReader(
			  new InputStreamReader(conn.getInputStream(), "utf-8"))) {
			    StringBuilder response = new StringBuilder();
			    String responseLine = null;
			    while ((responseLine = br.readLine()) != null) {
			        response.append(responseLine.trim());
			    }
			    System.out.println(response.toString());
			    result = response.toString();
	}
	
	conn.disconnect();
    Gson gson = new Gson();  
    DownloadResponse data = gson.fromJson(result, DownloadResponse.class);
    

    return data.getFileBlob();

}

@SuppressWarnings("unused")
public static String callUploadAPI(UploadedFile uploadedFile,String complianceDocType,ParameterDetailService paramDtlService, Boolean isAPI, FileUtil fileUtil) throws Exception {
    	
	ParameterDetail pdIsAPI = paramDtlService.getParameterDetailByParamDtlCode("IS_USING_API");
	if(pdIsAPI.getNameIn()!=null && pdIsAPI.getNameIn().equals("true")) {
	//if(isAPI) {
	    ParameterDetail pdURL = paramDtlService.getParameterDetailByParamDtlCode("BODY_URL_UPLOAD");
	    ParameterDetail pdRequestId = paramDtlService.getParameterDetailByParamDtlCode("BODY_REQUEST_ID");
	    ParameterDetail pdChannelId = paramDtlService.getParameterDetailByParamDtlCode("BODY_CHANNEL_ID");
	    ParameterDetail pdChannelType = paramDtlService.getParameterDetailByParamDtlCode("BODY_CHANNEL_TYPE");
	    ParameterDetail pdPassword = paramDtlService.getParameterDetailByParamDtlCode("BODY_PASSWORD");
	    ParameterDetail pdUserName = paramDtlService.getParameterDetailByParamDtlCode("BODY_USERNAME");
	    ParameterDetail pdApiKey = paramDtlService.getParameterDetailByParamDtlCode("HEADER_BTPN_KEY");
	    ParameterDetail pdPostmanToken = paramDtlService.getParameterDetailByParamDtlCode("HEADER_POSTMAN_TOKEN");
	    ParameterDetail pdContentType = paramDtlService.getParameterDetailByParamDtlCode("BODY_CONTENT_TYPE");
	    
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMddhhmmss");
        
		TrustManager[] trustAllCerts = new TrustManager[] { new X509ExtendedTrustManager() {

			@Override
			public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				
			}

			@Override
			public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				
			}

			@Override
			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			@Override
			public void checkClientTrusted(X509Certificate[] chain, String authType, Socket socket)
					throws CertificateException {
				
			}

			@Override
			public void checkClientTrusted(X509Certificate[] chain, String authType, SSLEngine engine)
					throws CertificateException {
				
			}

			@Override
			public void checkServerTrusted(X509Certificate[] chain, String authType, Socket socket)
					throws CertificateException {
				
			}

			@Override
			public void checkServerTrusted(X509Certificate[] chain, String authType, SSLEngine engine)
					throws CertificateException {
				
			}
			
		} };

		// Install the all-trusting trust manager
		SSLContext sc = SSLContext.getInstance("TLS");
		sc.init(null, trustAllCerts, new java.security.SecureRandom());
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

		// Create all-trusting host name verifier
		HostnameVerifier allHostsValid = new HostnameVerifier() {
		    @Override
		    public boolean verify(String hostname, SSLSession session) {
		return true;
		    }
		};

		// Install the all-trusting host verifier
		HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
		
		URL url = new URL(pdURL.getNameIn());
		
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		
		
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/json; utf-8");
		conn.setRequestProperty("Accept", "application/json");
		conn.setRequestProperty("Cache-Control","no-cache");
		conn.setRequestProperty("BTPN-ApiKey", pdApiKey.getNameIn());
		conn.setRequestProperty("Postman-Token",pdPostmanToken.getNameIn());
		conn.setDoOutput(true);
    	
//		String fileName = "ComplianceDoc_"+complianceDocType.replace(" ", "")+"_"+sdf2.format(new Date()).replaceAll(" ", "")+uploadedFile.getFileName().replaceAll(" ", "");
		UUID uuid= UUID.randomUUID();
		String ext = FileUtil.getExtention(uploadedFile.getFileName());
		
		// add by dwi, limit uuid = 40
		String uuidStr = uuid.toString();
		if(uuid.toString().length() > 40)
			uuidStr = uuid.toString().substring(0, 40);
		
		String fileName = "ComplianceDoc_"+
				complianceDocType.replace(" ", "")+
				"_"+
				uuidStr+
				"."+ 
				ext;
		
		String jsonInputString = "{" + 
	    		"\"RequestUploadDocToECM\":" + 
	    		"{" + 
	    		"\"UploadDocument\":" + 
	    		"{" + 
	    		"\"fileName\":\""+fileName+"\"," + 
	    		"\"metadata\":\"[{\\\"ColumnName\\\":\\\"ReferenceNo\\\",\\\"ColumnDataType\\\":\\\"string\\\",\\\"ColumnValue\\\":\\\""+UUID.randomUUID().toString().substring(0, 6)+"\\\"},"
	    		              + "{\\\"ColumnName\\\":\\\"UploadDate\\\",\\\"ColumnDataType\\\":\\\"string\\\",\\\"ColumnValue\\\":\\\""+sdf.format(new Date())+"\\\"},"
	    		              + "{\\\"ColumnName\\\":\\\"UserUploadID\\\",\\\"ColumnDataType\\\":\\\"string\\\",\\\"ColumnValue\\\":\\\"optimus\\\"},"
	    		              + "{\\\"ColumnName\\\":\\\"DocStatus\\\",\\\"ColumnDataType\\\":\\\"string\\\",\\\"ColumnValue\\\":\\\"Active\\\"},"
	    		              + "{\\\"ColumnName\\\":\\\"ComplianceDocumentTypes\\\",\\\"ColumnDataType\\\":\\\"string\\\",\\\"ColumnValue\\\":\\\""+complianceDocType+"\\\"}]\"," + 
	    		"\"fileBlob\":\""+Base64Utils.encodeToString(uploadedFile.getContents())+"\"," + 
	    		"\"contentType\":\""+pdContentType.getNameIn()+"\"" + 
	    		"}," + 
	    		"\"Commonparam\":" + 
	    		"{" + 
	    		"\"amount\":\"\"," + 
	    		"\"currencyfee\":\"\"," + 
	    		"\"original\":\"\"," + 
	    		"\"referenceNo\":\"\"," + 
	    		"\"processingCode\":\"\"," + 
	    		"\"fee\":\"\"," + 
	    		"\"currencyAmount\":\"\"," + 
	    		"\"channelType\":\""+pdChannelType.getNameIn()+"\"," + 
	    		"\"terminalId\":\"\"," + 
	    		"\"userId\":\"\"," + 
	    		"\"acqId\":\"\"," + 
	    		"\"transmissionDateTime\":\""+sdf.format(new Date())+"\"," + 
	    		"\"node\":\"\"," + 
	    		"\"terminalName\":\"\"," + 
	    		"\"requestId\":\""+pdRequestId.getNameIn()+"\"," + 
	    		"\"pan\":\"\"," + 
	    		"\"channelId\":\""+pdChannelId.getNameIn()+"\"" + 
	    		"}," + 
	    		"\"authentication\":" + 
	    		"{" + 
	    		"\"password\":\""+pdPassword.getNameIn()+"\"," + 
	    		"\"username\":\""+pdUserName.getNameIn()+"\"" + 
	    		"}" + 
	    		"}," + 
	    		"\"$path\":\"\"," + 
	    		"\"$resourceID\":\"\"" + 
	    		"}";
        
		System.out.println(jsonInputString);
    	try(OutputStream os = conn.getOutputStream()) {
    	    byte[] input = jsonInputString.getBytes("utf-8");
    	    os.write(input, 0, input.length);           
    	}

    	String result = "";
    	try(BufferedReader br = new BufferedReader(
    			  new InputStreamReader(conn.getInputStream(), "utf-8"))) {
    			    StringBuilder response = new StringBuilder();
    			    String responseLine = null;
    			    while ((responseLine = br.readLine()) != null) {
    			        response.append(responseLine.trim());
    			    }
    			    System.out.println(response.toString());
    			    result = response.toString();
    	}
		
    	conn.disconnect();
        Gson gson = new Gson();  
        UploadResponse data = gson.fromJson(result, UploadResponse.class);
        
        if(data != null && !data.getResponseCode().equals("00")) {
        	throw new CustomAPIException(data.getResponseDesc());
        }
        
        return data.getFileId();
	}else {
		ParameterDetail pdFilePath = paramDtlService.getParameterDetailByParamDtlCode("ATTACHMENT_FILE_PATH");
		System.out.println("file Path =="+pdFilePath.getNameIn());
		UUID uuid= UUID.randomUUID();
		return fileUtil.handleFileUpload(uploadedFile.getInputstream(), uploadedFile.getFileName(), pdFilePath.getNameIn(),uuid.toString());
	}

	}

@SuppressWarnings("unused")
public static UploadResponse callUploadAPIReturnResponse(
		UploadedFile uploadedFile,
		String complianceDocType,
		ParameterDetailService paramDtlService, 
		Boolean isAPI, 
		FileUtil fileUtil
	) throws Exception {
	
	ParameterDetail pdIsAPI = paramDtlService.getParameterDetailByParamDtlCode("IS_USING_API");
	if(pdIsAPI.getNameIn()!=null && pdIsAPI.getNameIn().equals("true")) {
	//if(isAPI) {
	    ParameterDetail pdURL = paramDtlService.getParameterDetailByParamDtlCode("BODY_URL_UPLOAD");
	    ParameterDetail pdRequestId = paramDtlService.getParameterDetailByParamDtlCode("BODY_REQUEST_ID");
	    ParameterDetail pdChannelId = paramDtlService.getParameterDetailByParamDtlCode("BODY_CHANNEL_ID");
	    ParameterDetail pdChannelType = paramDtlService.getParameterDetailByParamDtlCode("BODY_CHANNEL_TYPE");
	    ParameterDetail pdPassword = paramDtlService.getParameterDetailByParamDtlCode("BODY_PASSWORD");
	    ParameterDetail pdUserName = paramDtlService.getParameterDetailByParamDtlCode("BODY_USERNAME");
	    ParameterDetail pdApiKey = paramDtlService.getParameterDetailByParamDtlCode("HEADER_BTPN_KEY");
	    ParameterDetail pdPostmanToken = paramDtlService.getParameterDetailByParamDtlCode("HEADER_POSTMAN_TOKEN");
	    ParameterDetail pdContentType = paramDtlService.getParameterDetailByParamDtlCode("BODY_CONTENT_TYPE");
	    
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMddhhmmss");
        
		TrustManager[] trustAllCerts = new TrustManager[] { new X509ExtendedTrustManager() {

			@Override
			public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				
			}

			@Override
			public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				
			}

			@Override
			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			@Override
			public void checkClientTrusted(X509Certificate[] chain, String authType, Socket socket)
					throws CertificateException {
				
			}

			@Override
			public void checkClientTrusted(X509Certificate[] chain, String authType, SSLEngine engine)
					throws CertificateException {
				
			}

			@Override
			public void checkServerTrusted(X509Certificate[] chain, String authType, Socket socket)
					throws CertificateException {
				
			}

			@Override
			public void checkServerTrusted(X509Certificate[] chain, String authType, SSLEngine engine)
					throws CertificateException {
				
			}
			
		} };

		// Install the all-trusting trust manager
		SSLContext sc = SSLContext.getInstance("TLS");
		sc.init(null, trustAllCerts, new java.security.SecureRandom());
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

		// Create all-trusting host name verifier
		HostnameVerifier allHostsValid = new HostnameVerifier() {
		    @Override
		    public boolean verify(String hostname, SSLSession session) {
		return true;
		    }
		};

		// Install the all-trusting host verifier
		HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
		
		URL url = new URL(pdURL.getNameIn());
		
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		
		
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/json; utf-8");
		conn.setRequestProperty("Accept", "application/json");
		conn.setRequestProperty("Cache-Control","no-cache");
		conn.setRequestProperty("BTPN-ApiKey", pdApiKey.getNameIn());
		conn.setRequestProperty("Postman-Token",pdPostmanToken.getNameIn());
		conn.setDoOutput(true);
    	
		//String fileName = "ComplianceDoc_"+complianceDocType.replace(" ", "")+"_"+sdf2.format(new Date()).replaceAll(" ", "")+uploadedFile.getFileName().replaceAll(" ", "");
		UUID uuid= UUID.randomUUID();
		String ext = FileUtil.getExtention(uploadedFile.getFileName());
		String fileName = "ComplianceDoc_"+complianceDocType.replace(" ", "")+"_"+uuid.toString()+"."+ ext;
		
		String jsonInputString = "{" + 
	    		"\"RequestUploadDocToECM\":" + 
	    		"{" + 
	    		"\"UploadDocument\":" + 
	    		"{" + 
	    		"\"fileName\":\""+fileName+"\"," + 
	    		"\"metadata\":\"[{\\\"ColumnName\\\":\\\"ReferenceNo\\\",\\\"ColumnDataType\\\":\\\"string\\\",\\\"ColumnValue\\\":\\\""+UUID.randomUUID().toString().substring(0, 6)+"\\\"},"
	    		              + "{\\\"ColumnName\\\":\\\"UploadDate\\\",\\\"ColumnDataType\\\":\\\"string\\\",\\\"ColumnValue\\\":\\\""+sdf.format(new Date())+"\\\"},"
	    		              + "{\\\"ColumnName\\\":\\\"UserUploadID\\\",\\\"ColumnDataType\\\":\\\"string\\\",\\\"ColumnValue\\\":\\\"optimus\\\"},"
	    		              + "{\\\"ColumnName\\\":\\\"DocStatus\\\",\\\"ColumnDataType\\\":\\\"string\\\",\\\"ColumnValue\\\":\\\"Active\\\"},"
	    		              + "{\\\"ColumnName\\\":\\\"ComplianceDocumentTypes\\\",\\\"ColumnDataType\\\":\\\"string\\\",\\\"ColumnValue\\\":\\\""+complianceDocType+"\\\"}]\"," + 
	    		"\"fileBlob\":\""+Base64Utils.encodeToString(uploadedFile.getContents())+"\"," + 
	    		"\"contentType\":\""+pdContentType.getNameIn()+"\"" + 
	    		"}," + 
	    		"\"Commonparam\":" + 
	    		"{" + 
	    		"\"amount\":\"\"," + 
	    		"\"currencyfee\":\"\"," + 
	    		"\"original\":\"\"," + 
	    		"\"referenceNo\":\"\"," + 
	    		"\"processingCode\":\"\"," + 
	    		"\"fee\":\"\"," + 
	    		"\"currencyAmount\":\"\"," + 
	    		"\"channelType\":\""+pdChannelType.getNameIn()+"\"," + 
	    		"\"terminalId\":\"\"," + 
	    		"\"userId\":\"\"," + 
	    		"\"acqId\":\"\"," + 
	    		"\"transmissionDateTime\":\""+sdf.format(new Date())+"\"," + 
	    		"\"node\":\"\"," + 
	    		"\"terminalName\":\"\"," + 
	    		"\"requestId\":\""+pdRequestId.getNameIn()+"\"," + 
	    		"\"pan\":\"\"," + 
	    		"\"channelId\":\""+pdChannelId.getNameIn()+"\"" + 
	    		"}," + 
	    		"\"authentication\":" + 
	    		"{" + 
	    		"\"password\":\""+pdPassword.getNameIn()+"\"," + 
	    		"\"username\":\""+pdUserName.getNameIn()+"\"" + 
	    		"}" + 
	    		"}," + 
	    		"\"$path\":\"\"," + 
	    		"\"$resourceID\":\"\"" + 
	    		"}";
        
		
    	try(OutputStream os = conn.getOutputStream()) {
    	    byte[] input = jsonInputString.getBytes("utf-8");
    	    os.write(input, 0, input.length);           
    	}

    	String result = "";
    	try(BufferedReader br = new BufferedReader(
    			  new InputStreamReader(conn.getInputStream(), "utf-8"))) {
    			    StringBuilder response = new StringBuilder();
    			    String responseLine = null;
    			    while ((responseLine = br.readLine()) != null) {
    			        response.append(responseLine.trim());
    			    }
    			    System.out.println(response.toString());
    			    result = response.toString();
    	}
		
    	conn.disconnect();
        Gson gson = new Gson();  
        UploadResponse data = gson.fromJson(result, UploadResponse.class);
        
        return data;
	}else {
		ParameterDetail pdFilePath = paramDtlService.getParameterDetailByParamDtlCode("ATTACHMENT_FILE_PATH");
		UUID uuid= UUID.randomUUID();
		UploadResponse data = new UploadResponse();
		data.setResult(Constants.TRUE);
		data.setFileId(fileUtil.handleFileUpload(uploadedFile.getInputstream(), uploadedFile.getFileName(), pdFilePath.getNameIn(),uuid.toString()));
		return data;
	}

}

@SuppressWarnings("unused")
private static void sendEmail(String subject, String body, String emailAddressTo,String emailAddressCc,ParameterDetailService paramDtlService) throws Exception {
	ParameterDetail pdHost = paramDtlService.getParameterDetailByParamDtlCode("MAIL_SMTP_HOST");
	ParameterDetail pdPort = paramDtlService.getParameterDetailByParamDtlCode("MAIL_SMTP_PORT");
	ParameterDetail pdUser = paramDtlService.getParameterDetailByParamDtlCode("MAIL_SMTP_USER");
	ParameterDetail pdPass = paramDtlService.getParameterDetailByParamDtlCode("MAIL_SMTP_PASSWORD");
	
	ParameterDetail pdAuth = paramDtlService.getParameterDetailByParamDtlCode("MAIL_SMTP_AUTH");
	ParameterDetail pdTls = paramDtlService.getParameterDetailByParamDtlCode("MAIL_SMTP_STARTTLS_ENABLE");
	String emailFrom = pdUser.getNameIn();

	EmailUtil emailUtil = new EmailUtil(pdHost.getNameIn(), Integer.valueOf(pdPort.getNameIn()),
			Boolean.valueOf(pdAuth.getNameIn()), Boolean.valueOf(pdTls.getNameIn()),
			pdUser.getNameIn(), pdPass.getNameIn(), emailFrom, emailAddressTo,emailAddressCc);
	
	
	emailUtil.sendEmail(subject.replaceAll("`", "").replaceAll("'", ""), body.replaceAll("`", "").replaceAll("'", ""),true);
	logger.info("Send email activation success");
	
}

@SuppressWarnings("unused")
private static void sendEmailScheduller(String subject, String body, String emailAddressTo,String emailAddressCc,SendEmailService sendEmailService) throws Exception {
	String host = sendEmailService.getSystemProperty("MAIL_SMTP_HOST");
	String port = sendEmailService.getSystemProperty("MAIL_SMTP_PORT");
	String user = sendEmailService.getSystemProperty("MAIL_SMTP_USER");
	String password = sendEmailService.getSystemProperty("MAIL_SMTP_PASSWORD");
	
	String auth = sendEmailService.getSystemProperty("MAIL_SMTP_AUTH");
	String tls = sendEmailService.getSystemProperty("MAIL_SMTP_STARTTLS_ENABLE");
	String emailFrom = user;

	EmailUtil emailUtil = new EmailUtil(host, Integer.valueOf(port),
			Boolean.valueOf(auth), Boolean.valueOf(tls),
			user, password, emailFrom, emailAddressTo,emailAddressCc);
	
	emailUtil.sendEmail(subject.replaceAll("`", "").replaceAll("'", ""), body.replaceAll("`", "").replaceAll("'", ""),true);
	logger.info("Send email activation success");
	
}
public static String sendEmailAPI(String to,String cc,String subject,String content,String refNo,String isHtml,ParameterDetailService paramDtlService) throws Exception {
	
	ParameterDetail pdIsAPI = paramDtlService.getParameterDetailByParamDtlCode("IS_EMAIL_USING_API");
	if(pdIsAPI.getNameIn()!=null && pdIsAPI.getNameIn().equals("true")) {
	
	    ParameterDetail pdUrl = paramDtlService.getParameterDetailByParamDtlCode("EMAIL_URL");
	    ParameterDetail pdApiKey = paramDtlService.getParameterDetailByParamDtlCode("EMAIL_HEADER_API_KEY");
	    ParameterDetail pdSource = paramDtlService.getParameterDetailByParamDtlCode("EMAIL_HEADER_SOURCE");
	    ParameterDetail pdUserName = paramDtlService.getParameterDetailByParamDtlCode("EMAIL_HEADER_USER_NAME");
	    ParameterDetail pdSenderName = paramDtlService.getParameterDetailByParamDtlCode("EMAIL_BODY_SENDER_NAME");
	    ParameterDetail pdEmailSender = paramDtlService.getParameterDetailByParamDtlCode("EMAIL_BODY_EMAIL_SENDER");
	    ParameterDetail pdPassword = paramDtlService.getParameterDetailByParamDtlCode("EMAIL_BODY_PASSWORD_SENDER");
	    
		TrustManager[] trustAllCerts = new TrustManager[] { new X509ExtendedTrustManager() {
	
			@Override
			public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				
			}
	
			@Override
			public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				
			}
	
			@Override
			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}
	
			@Override
			public void checkClientTrusted(X509Certificate[] chain, String authType, Socket socket)
					throws CertificateException {
				
			}
	
			@Override
			public void checkClientTrusted(X509Certificate[] chain, String authType, SSLEngine engine)
					throws CertificateException {
				
			}
	
			@Override
			public void checkServerTrusted(X509Certificate[] chain, String authType, Socket socket)
					throws CertificateException {
				
			}
	
			@Override
			public void checkServerTrusted(X509Certificate[] chain, String authType, SSLEngine engine)
					throws CertificateException {
				
			}
			
		} };
	
		// Install the all-trusting trust manager
		SSLContext sc = SSLContext.getInstance("TLS");
		sc.init(null, trustAllCerts, new java.security.SecureRandom());
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
	
		// Create all-trusting host name verifier
		HostnameVerifier allHostsValid = new HostnameVerifier() {
		    @Override
		    public boolean verify(String hostname, SSLSession session) {
		return true;
		    }
		};
	
		// Install the all-trusting host verifier
		HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
		
		URL url = new URL(pdUrl.getNameIn());
		
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		
		
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/json; utf-8");
		conn.setRequestProperty("Accept", "application/json");
		conn.setRequestProperty("Cache-Control","no-cache");
		conn.setRequestProperty("BTPN-ApiKey",pdApiKey.getNameIn());
		conn.setRequestProperty("source",pdSource.getNameIn());
		conn.setRequestProperty("username",pdUserName.getNameIn());
		conn.setRequestProperty("referenceNo",UUID.randomUUID().toString().substring(0, 6));
		//conn.setRequestProperty("device","web");
		//conn.setRequestProperty("uuid","333242552");
		
		conn.setDoOutput(true);
		
		String newEmailContent = content.replaceAll("\"", "\\\\\"")
				.replaceAll("\r\n", "")
				.replaceAll("\t", " ")
				.replaceAll("\n", " ");
//		newEmailContent = newEmailContent.replaceAll(System.getProperty("line.separator"), "");
//		// add by dwi, replace tab with empty spaces
//		newEmailContent = newEmailContent.replaceAll("\t", " ");
		
		// add by dwi, replace with ... if subject more than 100 char
		if(StringUtils.isNotBlank(subject) && subject.length() > 100) {
			subject = subject.substring(0, 97) + "...";
		}
			
		//testing only comment this
		//subject = "1923712830478123608djfadhasjfk91238921739812hdfadj128937987dfajdsfhjkh247812973981jafkdhkjadhfalj182739812798dfakh8129731";
		
		String jsonInputString = "{\r\n" + 
				"	\"senderName\":\""+pdSenderName.getNameIn()+"\",\r\n" + 
				"	\"emailSender\":\""+pdEmailSender.getNameIn()+"\",\r\n" + 
				"	\"passwordSender\":\""+pdPassword.getNameIn()+"\",\r\n" + 
				"	\"to\":\""+to+"\",\r\n" + 
				"	\"cc\":\""+cc+"\",\r\n" + 
				"	\"isHtml\":\""+isHtml+"\",\r\n" + 
				"	\"bodyEmail\":\""+newEmailContent+"\",\r\n" + 
				"	\"subject\":\""+subject.replaceAll("\"", "\\\"")+"\"\r\n" + 
				"}";
	    		
		System.out.println(jsonInputString);
	    		
		try(OutputStream os = conn.getOutputStream()) {
		    byte[] input = jsonInputString.getBytes("utf-8");
		    os.write(input, 0, input.length);           
		}
	
		String result = "";
		try(BufferedReader br = new BufferedReader(
				  new InputStreamReader(conn.getInputStream(), "utf-8"))) {
				    StringBuilder response = new StringBuilder();
				    String responseLine = null;
				    while ((responseLine = br.readLine()) != null) {
				        response.append(responseLine.trim());
				    }
				    System.out.println(response.toString());
				    result = response.toString();
		}
		
		conn.disconnect();
	    Gson gson = new Gson();  
	    SendEmailResponse data = gson.fromJson(result, SendEmailResponse.class);
	    
	    if(data != null && data.getGenericResponse() != null && !data.getGenericResponse().getResponseCode().equals("00")) {
        	throw new CustomAPIException(data.getGenericResponse().getResponseDesc());
        }
	    
	    return data.getGenericResponse().getResponseDesc();
	}else {
		ParameterDetail pdHost = paramDtlService.getParameterDetailByParamDtlCode("MAIL_SMTP_HOST");
		ParameterDetail pdPort = paramDtlService.getParameterDetailByParamDtlCode("MAIL_SMTP_PORT");
		ParameterDetail pdUser = paramDtlService.getParameterDetailByParamDtlCode("MAIL_SMTP_USER");
		ParameterDetail pdPass = paramDtlService.getParameterDetailByParamDtlCode("MAIL_SMTP_PASSWORD");
		
		ParameterDetail pdAuth = paramDtlService.getParameterDetailByParamDtlCode("MAIL_SMTP_AUTH");
		ParameterDetail pdTls = paramDtlService.getParameterDetailByParamDtlCode("MAIL_SMTP_STARTTLS_ENABLE");
		String emailFrom = pdUser.getNameIn();

		EmailUtil emailUtil = new EmailUtil(pdHost.getNameIn(), Integer.valueOf(pdPort.getNameIn()),
				Boolean.valueOf(pdAuth.getNameIn()), Boolean.valueOf(pdTls.getNameIn()),
				pdUser.getNameIn(), pdPass.getNameIn(), emailFrom, to,cc);
		
		//System.out.println("Subject awal =="+subject);
		subject = subject.replaceAll("`", "");
		subject = subject.replaceAll("'", "");
		//System.out.println("Subject akhir =="+subject);
		content = content.replaceAll("`", "");
		content = content.replaceAll("'", "");
		//System.out.println("content akhir =="+content);
		emailUtil.sendEmail(subject, content,true);
		return "SUCCESS";
	}

}

public static String sendEmailAPIScheduller(String to,String cc,String subject,String content,String refNo,String isHtml,SendEmailService sendEmailService) throws Exception {
	
	String isUsingApi = sendEmailService.getSystemProperty("IS_EMAIL_USING_API");
	if(isUsingApi!=null && isUsingApi.equals("true")) {
	    String emailUrl = sendEmailService.getSystemProperty("EMAIL_URL");
	    String apiKey = sendEmailService.getSystemProperty("EMAIL_HEADER_API_KEY");
	    String source = sendEmailService.getSystemProperty("EMAIL_HEADER_SOURCE");
	    String userName = sendEmailService.getSystemProperty("EMAIL_HEADER_USER_NAME");
	    String senderName = sendEmailService.getSystemProperty("EMAIL_BODY_SENDER_NAME");
	    String emailSender = sendEmailService.getSystemProperty("EMAIL_BODY_EMAIL_SENDER");
	    String password = sendEmailService.getSystemProperty("EMAIL_BODY_PASSWORD_SENDER");
	    
		TrustManager[] trustAllCerts = new TrustManager[] { new X509ExtendedTrustManager() {
	
			@Override
			public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				
			}
	
			@Override
			public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				
			}
	
			@Override
			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}
	
			@Override
			public void checkClientTrusted(X509Certificate[] chain, String authType, Socket socket)
					throws CertificateException {
				
			}
	
			@Override
			public void checkClientTrusted(X509Certificate[] chain, String authType, SSLEngine engine)
					throws CertificateException {
				
			}
	
			@Override
			public void checkServerTrusted(X509Certificate[] chain, String authType, Socket socket)
					throws CertificateException {
				
			}
	
			@Override
			public void checkServerTrusted(X509Certificate[] chain, String authType, SSLEngine engine)
					throws CertificateException {
				
			}
			
		} };
	
		// Install the all-trusting trust manager
		SSLContext sc = SSLContext.getInstance("TLS");
		sc.init(null, trustAllCerts, new java.security.SecureRandom());
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
	
		// Create all-trusting host name verifier
		HostnameVerifier allHostsValid = new HostnameVerifier() {
		    @Override
		    public boolean verify(String hostname, SSLSession session) {
		return true;
		    }
		};
	
		// Install the all-trusting host verifier
		HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
		
		URL url = new URL(emailUrl);
		
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		
		
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/json");
		conn.setRequestProperty("Accept", "application/json");
		conn.setRequestProperty("Cache-Control","no-cache");
		conn.setRequestProperty("BTPN-ApiKey",apiKey);
		conn.setRequestProperty("source",source);
		conn.setRequestProperty("username",userName);
		conn.setRequestProperty("referenceNo",UUID.randomUUID().toString().substring(0, 6));
		//conn.setRequestProperty("device","web");
		//conn.setRequestProperty("uuid","333242552");
		
		conn.setDoOutput(true);
		
//		String newEmailContent = content.replaceAll("\"", "\\\\\"");
//		newEmailContent = newEmailContent.replaceAll("\r\n", "");
		
		// add by dwi
		String newEmailContent = content.replaceAll("\"", "\\\\\"")
				.replaceAll("\r\n", "")
				.replaceAll("\t", " ")
				.replaceAll("\n", " ");
		
		
		
		// add by dwi, replace with ... if subject more than 100 char
		if(StringUtils.isNotBlank(subject) && subject.length() > 100) {
			subject = subject.substring(0, 97) + "...";
		}
		
		String jsonInputString = "{\r\n" + 
				"	\"senderName\":\""+senderName+"\",\r\n" + 
				"	\"emailSender\":\""+emailSender+"\",\r\n" + 
				"	\"passwordSender\":\""+password+"\",\r\n" + 
				"	\"to\":\""+to+"\",\r\n" + 
				"	\"cc\":\""+cc+"\",\r\n" + 
				"	\"isHtml\":\""+isHtml+"\",\r\n" + 
				"	\"bodyEmail\":\""+newEmailContent+"\",\r\n" + 
				"	\"subject\":\""+subject.replaceAll("\"", "\\\\\"")+"\"\r\n" + 
				"}";
	    		
	    //System.out.println("jsonInputString=="+jsonInputString);
	    
		try(OutputStream os = conn.getOutputStream()) {
		    byte[] input = jsonInputString.getBytes("utf-8");
		    os.write(input, 0, input.length);           
		}
	
		String result = "";
		try(BufferedReader br = new BufferedReader(
				  new InputStreamReader(conn.getInputStream(), "utf-8"))) {
				    StringBuilder response = new StringBuilder();
				    String responseLine = null;
				    while ((responseLine = br.readLine()) != null) {
				        response.append(responseLine.trim());
				    }
				   // System.out.println(response.toString());
				    result = response.toString();
		}
		
		conn.disconnect();
	    Gson gson = new Gson();  
	    SendEmailResponse data = gson.fromJson(result, SendEmailResponse.class);
	    
	    if(data != null && data.getGenericResponse() != null && !data.getGenericResponse().getResponseCode().equals("00")) {
        	throw new CustomAPIException(data.getGenericResponse().getResponseDesc());
        }
	    
	    return data.getGenericResponse().getResponseDesc();
	}else {
		String host = sendEmailService.getSystemProperty("MAIL_SMTP_HOST");
		String port = sendEmailService.getSystemProperty("MAIL_SMTP_PORT");
		String user = sendEmailService.getSystemProperty("MAIL_SMTP_USER");
		String password = sendEmailService.getSystemProperty("MAIL_SMTP_PASSWORD");
		
		String auth = sendEmailService.getSystemProperty("MAIL_SMTP_AUTH");
		String tls = sendEmailService.getSystemProperty("MAIL_SMTP_STARTTLS_ENABLE");
		String emailFrom = user;

		EmailUtil emailUtil = new EmailUtil(host, Integer.valueOf(port),
				Boolean.valueOf(auth), Boolean.valueOf(tls),
				user, password, emailFrom, to,cc);
		
		//emailUtil.sendEmail(subject.replaceAll("`", "").replaceAll("'", ""), content.replaceAll("`", "").replaceAll("'", ""),true);
		
		subject = subject.replaceAll("`", "");
		subject = subject.replaceAll("'", "");
			// System.out.println("Subject akhir =="+subject);
		content = content.replaceAll("`", "");
		content = content.replaceAll("'", "");
			// System.out.println("content akhir =="+content);
		emailUtil.sendEmail(subject, content, true);
		
		return "SUCCESS";
	}

}

@SuppressWarnings("unused")
public static String callUploadAPIByFolder(UploadedFile uploadedFile,String complianceDocType,ParameterDetailService paramDtlService, Boolean isAPI, FileUtil fileUtil, String folder) throws Exception {
    	
	ParameterDetail pdIsAPI = paramDtlService.getParameterDetailByParamDtlCode("IS_USING_API");
	if(pdIsAPI.getNameIn()!=null && pdIsAPI.getNameIn().equals("true")) {
	//if(isAPI) {
	    ParameterDetail pdURL = paramDtlService.getParameterDetailByParamDtlCode("BODY_URL_UPLOAD");
	    ParameterDetail pdRequestId = paramDtlService.getParameterDetailByParamDtlCode("BODY_REQUEST_ID");
	    ParameterDetail pdChannelId = paramDtlService.getParameterDetailByParamDtlCode("BODY_CHANNEL_ID");
	    ParameterDetail pdChannelType = paramDtlService.getParameterDetailByParamDtlCode("BODY_CHANNEL_TYPE");
	    ParameterDetail pdPassword = paramDtlService.getParameterDetailByParamDtlCode("BODY_PASSWORD");
	    ParameterDetail pdUserName = paramDtlService.getParameterDetailByParamDtlCode("BODY_USERNAME");
	    ParameterDetail pdApiKey = paramDtlService.getParameterDetailByParamDtlCode("HEADER_BTPN_KEY");
	    ParameterDetail pdPostmanToken = paramDtlService.getParameterDetailByParamDtlCode("HEADER_POSTMAN_TOKEN");
	    ParameterDetail pdContentType = paramDtlService.getParameterDetailByParamDtlCode("BODY_CONTENT_TYPE");
	    
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMddhhmmss");
        
		TrustManager[] trustAllCerts = new TrustManager[] { new X509ExtendedTrustManager() {

			@Override
			public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				
			}

			@Override
			public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				
			}

			@Override
			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			@Override
			public void checkClientTrusted(X509Certificate[] chain, String authType, Socket socket)
					throws CertificateException {
				
			}

			@Override
			public void checkClientTrusted(X509Certificate[] chain, String authType, SSLEngine engine)
					throws CertificateException {
				
			}

			@Override
			public void checkServerTrusted(X509Certificate[] chain, String authType, Socket socket)
					throws CertificateException {
				
			}

			@Override
			public void checkServerTrusted(X509Certificate[] chain, String authType, SSLEngine engine)
					throws CertificateException {
				
			}
			
		} };

		// Install the all-trusting trust manager
		SSLContext sc = SSLContext.getInstance("TLS");
		sc.init(null, trustAllCerts, new java.security.SecureRandom());
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

		// Create all-trusting host name verifier
		HostnameVerifier allHostsValid = new HostnameVerifier() {
		    @Override
		    public boolean verify(String hostname, SSLSession session) {
		return true;
		    }
		};

		// Install the all-trusting host verifier
		HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
		
		URL url = new URL(pdURL.getNameIn());
		
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		
		
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/json; utf-8");
		conn.setRequestProperty("Accept", "application/json");
		conn.setRequestProperty("Cache-Control","no-cache");
		conn.setRequestProperty("BTPN-ApiKey", pdApiKey.getNameIn());
		conn.setRequestProperty("Postman-Token",pdPostmanToken.getNameIn());
		conn.setDoOutput(true);
    	
//		String fileName = "ComplianceDoc_"+complianceDocType.replace(" ", "")+"_"+sdf2.format(new Date()).replaceAll(" ", "")+uploadedFile.getFileName().replaceAll(" ", "");
		UUID uuid= UUID.randomUUID();
		String ext = FileUtil.getExtention(uploadedFile.getFileName());
		
		// add by dwi, limit uuid = 40
		String uuidStr = uuid.toString();
		if(uuid.toString().length() > 40)
			uuidStr = uuid.toString().substring(0, 40);
		
		String fileName = "ComplianceDoc_"+
				complianceDocType.replace(" ", "")+
				"_"+
				uuidStr+
				"."+ 
				ext;
		
		String jsonInputString = "{" + 
	    		"\"RequestUploadDocToECM\":" + 
	    		"{" + 
	    		"\"UploadDocument\":" + 
	    		"{" + 
	    		"\"fileName\":\""+fileName+"\"," + 
	    		"\"metadata\":\"[{\\\"ColumnName\\\":\\\"ReferenceNo\\\",\\\"ColumnDataType\\\":\\\"string\\\",\\\"ColumnValue\\\":\\\""+UUID.randomUUID().toString().substring(0, 6)+"\\\"},"
	    		              + "{\\\"ColumnName\\\":\\\"UploadDate\\\",\\\"ColumnDataType\\\":\\\"string\\\",\\\"ColumnValue\\\":\\\""+sdf.format(new Date())+"\\\"},"
	    		              + "{\\\"ColumnName\\\":\\\"UserUploadID\\\",\\\"ColumnDataType\\\":\\\"string\\\",\\\"ColumnValue\\\":\\\"optimus\\\"},"
	    		              + "{\\\"ColumnName\\\":\\\"DocStatus\\\",\\\"ColumnDataType\\\":\\\"string\\\",\\\"ColumnValue\\\":\\\"Active\\\"},"
	    		              + "{\\\"ColumnName\\\":\\\"ComplianceDocumentTypes\\\",\\\"ColumnDataType\\\":\\\"string\\\",\\\"ColumnValue\\\":\\\""+complianceDocType+"\\\"}]\"," + 
	    		"\"fileBlob\":\""+Base64Utils.encodeToString(uploadedFile.getContents())+"\"," + 
	    		"\"contentType\":\""+pdContentType.getNameIn()+"\"" + 
	    		"}," + 
	    		"\"Commonparam\":" + 
	    		"{" + 
	    		"\"amount\":\"\"," + 
	    		"\"currencyfee\":\"\"," + 
	    		"\"original\":\"\"," + 
	    		"\"referenceNo\":\"\"," + 
	    		"\"processingCode\":\"\"," + 
	    		"\"fee\":\"\"," + 
	    		"\"currencyAmount\":\"\"," + 
	    		"\"channelType\":\""+pdChannelType.getNameIn()+"\"," + 
	    		"\"terminalId\":\"\"," + 
	    		"\"userId\":\"\"," + 
	    		"\"acqId\":\"\"," + 
	    		"\"transmissionDateTime\":\""+sdf.format(new Date())+"\"," + 
	    		"\"node\":\"\"," + 
	    		"\"terminalName\":\"\"," + 
	    		"\"requestId\":\""+pdRequestId.getNameIn()+"\"," + 
	    		"\"pan\":\"\"," + 
	    		"\"channelId\":\""+pdChannelId.getNameIn()+"\"" + 
	    		"}," + 
	    		"\"authentication\":" + 
	    		"{" + 
	    		"\"password\":\""+pdPassword.getNameIn()+"\"," + 
	    		"\"username\":\""+pdUserName.getNameIn()+"\"" + 
	    		"}" + 
	    		"}," + 
	    		"\"$path\":\"\"," + 
	    		"\"$resourceID\":\"\"" + 
	    		"}";
        
		System.out.println(jsonInputString);
    	try(OutputStream os = conn.getOutputStream()) {
    	    byte[] input = jsonInputString.getBytes("utf-8");
    	    os.write(input, 0, input.length);           
    	}

    	String result = "";
    	try(BufferedReader br = new BufferedReader(
    			  new InputStreamReader(conn.getInputStream(), "utf-8"))) {
    			    StringBuilder response = new StringBuilder();
    			    String responseLine = null;
    			    while ((responseLine = br.readLine()) != null) {
    			        response.append(responseLine.trim());
    			    }
    			    System.out.println(response.toString());
    			    result = response.toString();
    	}
		
    	conn.disconnect();
        Gson gson = new Gson();  
        UploadResponse data = gson.fromJson(result, UploadResponse.class);
        
        if(data != null && !data.getResponseCode().equals("00")) {
        	throw new CustomAPIException(data.getResponseDesc());
        }
        
        return data.getFileId();
	}else {
		ParameterDetail pdFilePath = paramDtlService.getParameterDetailByParamDtlCode("ATTACHMENT_FILE_PATH");
		UUID uuid= UUID.randomUUID();
		return fileUtil.handleFileUpload(uploadedFile.getInputstream(), uploadedFile.getFileName(), pdFilePath.getNameIn()+folder+"/",uuid.toString());
	}

}

	public static String sendEmailAPIWithAttachment(String to,String cc,String subject,String content,String refNo,String isHtml,ParameterDetailService paramDtlService, List<File> attachmentFile) throws Exception {
		
		ParameterDetail pdIsAPI = paramDtlService.getParameterDetailByParamDtlCode("IS_EMAIL_USING_API");
		if(pdIsAPI.getNameIn()!=null && pdIsAPI.getNameIn().equals("true")) {
		
		    ParameterDetail pdUrl = paramDtlService.getParameterDetailByParamDtlCode("EMAIL_URL");
		    ParameterDetail pdApiKey = paramDtlService.getParameterDetailByParamDtlCode("EMAIL_HEADER_API_KEY");
		    ParameterDetail pdSource = paramDtlService.getParameterDetailByParamDtlCode("EMAIL_HEADER_SOURCE");
		    ParameterDetail pdUserName = paramDtlService.getParameterDetailByParamDtlCode("EMAIL_HEADER_USER_NAME");
		    ParameterDetail pdSenderName = paramDtlService.getParameterDetailByParamDtlCode("EMAIL_BODY_SENDER_NAME");
		    ParameterDetail pdEmailSender = paramDtlService.getParameterDetailByParamDtlCode("EMAIL_BODY_EMAIL_SENDER");
		    ParameterDetail pdPassword = paramDtlService.getParameterDetailByParamDtlCode("EMAIL_BODY_PASSWORD_SENDER");
		    
			TrustManager[] trustAllCerts = new TrustManager[] { new X509ExtendedTrustManager() {
		
				@Override
				public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
					
				}
		
				@Override
				public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
					
				}
		
				@Override
				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}
		
				@Override
				public void checkClientTrusted(X509Certificate[] chain, String authType, Socket socket)
						throws CertificateException {
					
				}
		
				@Override
				public void checkClientTrusted(X509Certificate[] chain, String authType, SSLEngine engine)
						throws CertificateException {
					
				}
		
				@Override
				public void checkServerTrusted(X509Certificate[] chain, String authType, Socket socket)
						throws CertificateException {
					
				}
		
				@Override
				public void checkServerTrusted(X509Certificate[] chain, String authType, SSLEngine engine)
						throws CertificateException {
					
				}
				
			} };
		
			// Install the all-trusting trust manager
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		
			// Create all-trusting host name verifier
			HostnameVerifier allHostsValid = new HostnameVerifier() {
			    @Override
			    public boolean verify(String hostname, SSLSession session) {
			return true;
			    }
			};
		
			// Install the all-trusting host verifier
			HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
			
			URL url = new URL(pdUrl.getNameIn());
			
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			
			
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json; utf-8");
			conn.setRequestProperty("Accept", "application/json");
			conn.setRequestProperty("Cache-Control","no-cache");
			conn.setRequestProperty("BTPN-ApiKey",pdApiKey.getNameIn());
			conn.setRequestProperty("source",pdSource.getNameIn());
			conn.setRequestProperty("username",pdUserName.getNameIn());
			conn.setRequestProperty("referenceNo",UUID.randomUUID().toString().substring(0, 6));
			//conn.setRequestProperty("device","web");
			//conn.setRequestProperty("uuid","333242552");
			
			conn.setDoOutput(true);
			
			String newEmailContent = content.replaceAll("\"", "\\\\\"")
					.replaceAll("\r\n", "")
					.replaceAll("\t", " ")
					.replaceAll("\n", " ");
	//		newEmailContent = newEmailContent.replaceAll(System.getProperty("line.separator"), "");
	//		// add by dwi, replace tab with empty spaces
	//		newEmailContent = newEmailContent.replaceAll("\t", " ");
			
			// add by dwi, replace with ... if subject more than 100 char
			if(StringUtils.isNotBlank(subject) && subject.length() > 100) {
				subject = subject.substring(0, 97) + "...";
			}
				
			//testing only comment this
			//subject = "1923712830478123608djfadhasjfk91238921739812hdfadj128937987dfajdsfhjkh247812973981jafkdhkjadhfalj182739812798dfakh8129731";
			
			String jsonInputString = "{\r\n" + 
					"	\"senderName\":\""+pdSenderName.getNameIn()+"\",\r\n" + 
					"	\"emailSender\":\""+pdEmailSender.getNameIn()+"\",\r\n" + 
					"	\"passwordSender\":\""+pdPassword.getNameIn()+"\",\r\n" + 
					"	\"to\":\""+to+"\",\r\n" + 
					"	\"cc\":\""+cc+"\",\r\n" + 
					"	\"isHtml\":\""+isHtml+"\",\r\n" + 
					"	\"bodyEmail\":\""+newEmailContent+"\",\r\n" + 
					"	\"subject\":\""+subject.replaceAll("\"", "\\\"")+"\"\r\n" + 
					"}";
		    		
			System.out.println(jsonInputString);
		    		
			try(OutputStream os = conn.getOutputStream()) {
			    byte[] input = jsonInputString.getBytes("utf-8");
			    os.write(input, 0, input.length);           
			}
		
			String result = "";
			try(BufferedReader br = new BufferedReader(
					  new InputStreamReader(conn.getInputStream(), "utf-8"))) {
					    StringBuilder response = new StringBuilder();
					    String responseLine = null;
					    while ((responseLine = br.readLine()) != null) {
					        response.append(responseLine.trim());
					    }
					    System.out.println(response.toString());
					    result = response.toString();
			}
			
			conn.disconnect();
		    Gson gson = new Gson();  
		    SendEmailResponse data = gson.fromJson(result, SendEmailResponse.class);
		    
		    if(data != null && data.getGenericResponse() != null && !data.getGenericResponse().getResponseCode().equals("00")) {
	        	throw new CustomAPIException(data.getGenericResponse().getResponseDesc());
	        }
		    
		    return data.getGenericResponse().getResponseDesc();
		}else {
			ParameterDetail pdHost = paramDtlService.getParameterDetailByParamDtlCode("MAIL_SMTP_HOST");
			ParameterDetail pdPort = paramDtlService.getParameterDetailByParamDtlCode("MAIL_SMTP_PORT");
			ParameterDetail pdUser = paramDtlService.getParameterDetailByParamDtlCode("MAIL_SMTP_USER");
			ParameterDetail pdPass = paramDtlService.getParameterDetailByParamDtlCode("MAIL_SMTP_PASSWORD");
			
			ParameterDetail pdAuth = paramDtlService.getParameterDetailByParamDtlCode("MAIL_SMTP_AUTH");
			ParameterDetail pdTls = paramDtlService.getParameterDetailByParamDtlCode("MAIL_SMTP_STARTTLS_ENABLE");
			String emailFrom = pdUser.getNameIn();
	
			EmailUtil emailUtil = new EmailUtil(pdHost.getNameIn(), Integer.valueOf(pdPort.getNameIn()),
					Boolean.valueOf(pdAuth.getNameIn()), Boolean.valueOf(pdTls.getNameIn()),
					pdUser.getNameIn(), pdPass.getNameIn(), emailFrom, to,cc);
			
			//System.out.println("Subject awal =="+subject);
			subject = subject.replaceAll("`", "");
			subject = subject.replaceAll("'", "");
			//System.out.println("Subject akhir =="+subject);
			content = content.replaceAll("`", "");
			content = content.replaceAll("'", "");
			//System.out.println("content akhir =="+content);
			emailUtil.sendEmail(subject, content, true, attachmentFile);
			return "SUCCESS";
		}
	}
	
	public static String sendEmailAPISchedullerAttachment(String to,String cc,String subject,String content,String refNo,String isHtml,SendEmailService sendEmailService, List<File> attachmentFile) throws Exception {
		
		String isUsingApi = sendEmailService.getSystemProperty("IS_EMAIL_USING_API");
		if(isUsingApi!=null && isUsingApi.equals("true")) {
		    String emailUrl = sendEmailService.getSystemProperty("EMAIL_URL");
		    String apiKey = sendEmailService.getSystemProperty("EMAIL_HEADER_API_KEY");
		    String source = sendEmailService.getSystemProperty("EMAIL_HEADER_SOURCE");
		    String userName = sendEmailService.getSystemProperty("EMAIL_HEADER_USER_NAME");
		    String senderName = sendEmailService.getSystemProperty("EMAIL_BODY_SENDER_NAME");
		    String emailSender = sendEmailService.getSystemProperty("EMAIL_BODY_EMAIL_SENDER");
		    String password = sendEmailService.getSystemProperty("EMAIL_BODY_PASSWORD_SENDER");
		    
			TrustManager[] trustAllCerts = new TrustManager[] { new X509ExtendedTrustManager() {
		
				@Override
				public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
					
				}
		
				@Override
				public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
					
				}
		
				@Override
				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}
		
				@Override
				public void checkClientTrusted(X509Certificate[] chain, String authType, Socket socket)
						throws CertificateException {
					
				}
		
				@Override
				public void checkClientTrusted(X509Certificate[] chain, String authType, SSLEngine engine)
						throws CertificateException {
					
				}
		
				@Override
				public void checkServerTrusted(X509Certificate[] chain, String authType, Socket socket)
						throws CertificateException {
					
				}
		
				@Override
				public void checkServerTrusted(X509Certificate[] chain, String authType, SSLEngine engine)
						throws CertificateException {
					
				}
				
			} };
		
			// Install the all-trusting trust manager
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		
			// Create all-trusting host name verifier
			HostnameVerifier allHostsValid = new HostnameVerifier() {
			    @Override
			    public boolean verify(String hostname, SSLSession session) {
			return true;
			    }
			};
		
			// Install the all-trusting host verifier
			HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
			
			URL url = new URL(emailUrl);
			
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			
			
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Accept", "application/json");
			conn.setRequestProperty("Cache-Control","no-cache");
			conn.setRequestProperty("BTPN-ApiKey",apiKey);
			conn.setRequestProperty("source",source);
			conn.setRequestProperty("username",userName);
			conn.setRequestProperty("referenceNo",UUID.randomUUID().toString().substring(0, 6));
			//conn.setRequestProperty("device","web");
			//conn.setRequestProperty("uuid","333242552");
			
			conn.setDoOutput(true);
			
//			String newEmailContent = content.replaceAll("\"", "\\\\\"");
//			newEmailContent = newEmailContent.replaceAll("\r\n", "");
			
			// add by dwi
			String newEmailContent = content.replaceAll("\"", "\\\\\"")
					.replaceAll("\r\n", "")
					.replaceAll("\t", " ")
					.replaceAll("\n", " ");
			
			
			
			// add by dwi, replace with ... if subject more than 100 char
			if(StringUtils.isNotBlank(subject) && subject.length() > 100) {
				subject = subject.substring(0, 97) + "...";
			}
			
			String jsonInputString = "{\r\n" + 
					"	\"senderName\":\""+senderName+"\",\r\n" + 
					"	\"emailSender\":\""+emailSender+"\",\r\n" + 
					"	\"passwordSender\":\""+password+"\",\r\n" + 
					"	\"to\":\""+to+"\",\r\n" + 
					"	\"cc\":\""+cc+"\",\r\n" + 
					"	\"isHtml\":\""+isHtml+"\",\r\n" + 
					"	\"bodyEmail\":\""+newEmailContent+"\",\r\n" + 
					"	\"subject\":\""+subject.replaceAll("\"", "\\\\\"")+"\"\r\n" + 
					"}";
		    		
		    //System.out.println("jsonInputString=="+jsonInputString);
		    
			try(OutputStream os = conn.getOutputStream()) {
			    byte[] input = jsonInputString.getBytes("utf-8");
			    os.write(input, 0, input.length);           
			}
		
			String result = "";
			try(BufferedReader br = new BufferedReader(
					  new InputStreamReader(conn.getInputStream(), "utf-8"))) {
					    StringBuilder response = new StringBuilder();
					    String responseLine = null;
					    while ((responseLine = br.readLine()) != null) {
					        response.append(responseLine.trim());
					    }
					   // System.out.println(response.toString());
					    result = response.toString();
			}
			
			conn.disconnect();
		    Gson gson = new Gson();  
		    SendEmailResponse data = gson.fromJson(result, SendEmailResponse.class);
		    
		    if(data != null && data.getGenericResponse() != null && !data.getGenericResponse().getResponseCode().equals("00")) {
	        	throw new CustomAPIException(data.getGenericResponse().getResponseDesc());
	        }
		    
		    return data.getGenericResponse().getResponseDesc();
		}else {
			String host = sendEmailService.getSystemProperty("MAIL_SMTP_HOST");
			String port = sendEmailService.getSystemProperty("MAIL_SMTP_PORT");
			String user = sendEmailService.getSystemProperty("MAIL_SMTP_USER");
			String password = sendEmailService.getSystemProperty("MAIL_SMTP_PASSWORD");
			
			String auth = sendEmailService.getSystemProperty("MAIL_SMTP_AUTH");
			String tls = sendEmailService.getSystemProperty("MAIL_SMTP_STARTTLS_ENABLE");
			String emailFrom = user;

			EmailUtil emailUtil = new EmailUtil(host, Integer.valueOf(port),
					Boolean.valueOf(auth), Boolean.valueOf(tls),
					user, password, emailFrom, to,cc);
			
			//emailUtil.sendEmail(subject.replaceAll("`", "").replaceAll("'", ""), content.replaceAll("`", "").replaceAll("'", ""),true);
			
			subject = subject.replaceAll("`", "");
			subject = subject.replaceAll("'", "");
				// System.out.println("Subject akhir =="+subject);
			content = content.replaceAll("`", "");
			content = content.replaceAll("'", "");
				// System.out.println("content akhir =="+content);
			emailUtil.sendEmail(subject, content, true, attachmentFile);
			
			return "SUCCESS";
		}

	}	

}
