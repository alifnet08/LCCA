package com.wo.module.common.utility;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509ExtendedTrustManager;

import com.wo.module.common.util.FileUtil;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.parameter.service.ParameterDetailService;

public class SCMApiDeleteImpl implements SCMApiDelete{
	
	private ParameterDetailService parameterDetailService;
	private FileUtil fileUtil;
	
	@Override
	public void delete(String fileId) throws Exception {
		ParameterDetail pdIsAPI = parameterDetailService.getParameterDetailByParamDtlCode(ParameterDetail.IS_USING_API);
		if(pdIsAPI.getNameIn()!=null && pdIsAPI.getNameIn().equals("true")) {
			deleteFromApi(fileId,parameterDetailService);
		}else {
			ParameterDetail pdFilePath = parameterDetailService.getParameterDetailByParamDtlCode(ParameterDetail.PARAM_DET_CODE_ATTACHMENT_FILE_PATH);
			String filePath = pdFilePath.getNameIn().concat(fileId);
			fileUtil.deleteFile(filePath);
		}
	}
	
	@Override
	public void bulkDelete(List<String> fileIds) throws Exception {
		if(fileIds== null || fileIds.isEmpty())
			throw new Exception("At least one data to delete");
		
		for(String fileId : fileIds) {
			delete(fileId);
		}
	}

	private void deleteFromApi(String fileId, ParameterDetailService parameterDetailService) throws Exception{
		ParameterDetail pdURL = parameterDetailService.getParameterDetailByParamDtlCode(ParameterDetail.BODY_URL_DELETE);
	    ParameterDetail pdPassword = parameterDetailService.getParameterDetailByParamDtlCode(ParameterDetail.BODY_PASSWORD);
	    ParameterDetail pdUserName = parameterDetailService.getParameterDetailByParamDtlCode(ParameterDetail.BODY_USERNAME);
	    ParameterDetail pdApiKey = parameterDetailService.getParameterDetailByParamDtlCode(ParameterDetail.HEADER_BTPN_KEY);
	    ParameterDetail pdPostmanToken = parameterDetailService.getParameterDetailByParamDtlCode(ParameterDetail.HEADER_POSTMAN_TOKEN);
	    ParameterDetail pdContentType = parameterDetailService.getParameterDetailByParamDtlCode(ParameterDetail.BODY_CONTENT_TYPE);
	    ParameterDetail pdTerminalId = parameterDetailService.getParameterDetailByParamDtlCode(ParameterDetail.BODY_TERMINAL_ID);
	    
		HttpsURLConnection.setDefaultSSLSocketFactory(getSSLContext().getSocketFactory());
		HttpsURLConnection.setDefaultHostnameVerifier(byPassHostnameVerifier());
		
		URL url = new URL(pdURL.getNameIn());
		
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/json; utf-8");
		conn.setRequestProperty("Accept", "application/json");
		conn.setRequestProperty("Cache-Control","no-cache");
		conn.setRequestProperty("BTPN-ApiKey", pdApiKey.getNameIn());
		conn.setRequestProperty("Postman-Token",pdPostmanToken.getNameIn());
		conn.setDoOutput(true);
	    
		String jsonInputString = buildJsonInputString(fileId, pdTerminalId.getNameIn(), pdContentType.getNameIn()
				, pdUserName.getNameIn(), pdPassword.getNameIn());
		
		// System.out.println("jsonInputString=="+jsonInputString);
		try (OutputStream os = conn.getOutputStream()) {
			byte[] input = jsonInputString.getBytes("utf-8");
			os.write(input, 0, input.length);
		}

		
		try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
			StringBuilder response = new StringBuilder();
			String responseLine = null;
			while ((responseLine = br.readLine()) != null) {
				response.append(responseLine.trim());
			}
			System.out.println(response.toString());
		}
		
		conn.disconnect();
		
	}
	
	private String buildJsonInputString(String fileId, String terminalId, String contentType, String username, String password) {
		return "{\r\n    \"request\":\r\n    {\r\n        \"commonParam\":\r\n        {\r\n            \"amount\":\"\",\r\n            \"currencyfee\":\"\",\r\n            \"original\":\"\",\r\n            \"referenceNo\":\"\",\r\n            \"processingCode\":\"\",\r\n            \"fee\":\"\",\r\n            \"currencyAmount\":\"\",\r\n            \"channelType\":\"\",\r\n            \"terminalId\":\""
				+ terminalId
				+ "\",\r\n            \"userId\":\"\",\r\n            \"acqId\":\"\",\r\n            \"transmissionDateTime\":\"\",\r\n            \"node\":\"\",\r\n            \"terminalName\":\"\",\r\n            \"requestId\":\"\",\r\n            \"pan\":\"\",\r\n            \"channelId\":\"\"\r\n        },\r\n        \"contentType\":\""
				+ contentType + "\",\r\n        \"fileID\":\"" + fileId
				+ "\"\r\n    },\r\n    \"authentication\":\r\n    {\r\n        \"password\":\"" + password
				+ "\",\r\n        \"username\":\"" + username + "\"\r\n    }\r\n}";
		
	}
	
	private HostnameVerifier byPassHostnameVerifier() {
		HostnameVerifier allHostsValid = new HostnameVerifier() {
			@Override
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		};

		return allHostsValid;
	}
	
	private SSLContext getSSLContext() throws Exception {
		SSLContext sc = SSLContext.getInstance("TLS");
		sc.init(null, getTrustedCert(), new java.security.SecureRandom());

		return sc;
	}
	
	private TrustManager[] getTrustedCert() {
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

		return trustAllCerts;
	}

	public SCMApiDeleteImpl(ParameterDetailService parameterDetailService, FileUtil fileUtil) {
		super();
		this.parameterDetailService = parameterDetailService;
		this.fileUtil = fileUtil;
	}

	public ParameterDetailService getParameterDetailService() {
		return parameterDetailService;
	}

	public void setParameterDetailService(ParameterDetailService parameterDetailService) {
		this.parameterDetailService = parameterDetailService;
	}

	public FileUtil getFileUtil() {
		return fileUtil;
	}

	public void setFileUtil(FileUtil fileUtil) {
		this.fileUtil = fileUtil;
	}


}
