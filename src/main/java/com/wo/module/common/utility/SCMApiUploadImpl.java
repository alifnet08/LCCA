package com.wo.module.common.utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509ExtendedTrustManager;

import org.springframework.util.Base64Utils;

import com.google.gson.Gson;
import com.wo.module.common.exception.CustomAPIException;
import com.wo.module.common.model.UploadResponse;
import com.wo.module.common.model.UploadedFileWO;
import com.wo.module.common.util.FileUtil;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.parameter.service.ParameterDetailService;

public class SCMApiUploadImpl implements SCMApiUpload {
	private String absoluteFilePath;
	private ParameterDetailService paramDtlService;
	private FileUtil fileUtil;
	private File file;
	private String compDocType;
	private String encodedBase64File;
	private UploadedFileWO uploadedFileWO;

	public SCMApiUploadImpl() {
		fileUtil = FileUtil.getInstance();
	}

	public SCMApiUploadImpl(String absoluteFilePath, ParameterDetailService paramDtlService, String compDocType) {
		super();
		this.absoluteFilePath = absoluteFilePath;
		this.paramDtlService = paramDtlService;
		this.compDocType = compDocType;
		fileUtil = FileUtil.getInstance();
	}

	@Override
	public String upload() throws Exception {
		uploadedFileWO = new UploadedFileWO();
		file = new File(absoluteFilePath);
		uploadedFileWO.setFile(file);

		if (file == null)
			throw new FileNotFoundException("File not exists in " + absoluteFilePath);

		uploadedFileWO.setFileSize(file.length());
		uploadedFileWO.setFileName(file.getName());

		byte[] byteFile = FileUtil.readBytesFromFile(file);
		encodedBase64File = Base64Utils.encodeToString(byteFile);
		uploadedFileWO.setEncodedBase64(encodedBase64File);
		
		String result = "";
		ParameterDetail pdIsAPI = paramDtlService.getParameterDetailByParamDtlCode(ParameterDetail.IS_USING_API);
		if (pdIsAPI.getNameIn() != null && pdIsAPI.getNameIn().equals("true")) {
			result = uploadToApi();
		}else {
			uploadedFileWO.setFileId(uploadToLocal());
			return uploadedFileWO.getFileId();
//			return uploadToLocal();
		}

		return result;
	}

	private String uploadToLocal() throws Exception, IOException, FileNotFoundException {
		ParameterDetail pdFilePath = paramDtlService.getParameterDetailByParamDtlCode("ATTACHMENT_FILE_PATH");
		UUID uuid= UUID.randomUUID();
		return fileUtil.handleFileUpload(new FileInputStream(file), file.getName(), pdFilePath.getNameIn(),uuid.toString());
	}

	private String uploadToApi() throws Exception {
		String result;
		ParameterDetail pdURL = paramDtlService.getParameterDetailByParamDtlCode(ParameterDetail.BODY_URL_UPLOAD);
		ParameterDetail pdRequestId = paramDtlService.getParameterDetailByParamDtlCode(ParameterDetail.BODY_REQUEST_ID);
		ParameterDetail pdChannelId = paramDtlService.getParameterDetailByParamDtlCode(ParameterDetail.BODY_CHANNEL_ID);
		ParameterDetail pdChannelType = paramDtlService.getParameterDetailByParamDtlCode(ParameterDetail.BODY_CHANNEL_TYPE);
		ParameterDetail pdPassword = paramDtlService.getParameterDetailByParamDtlCode(ParameterDetail.BODY_PASSWORD);
		ParameterDetail pdUserName = paramDtlService.getParameterDetailByParamDtlCode(ParameterDetail.BODY_USERNAME);
		ParameterDetail pdApiKey = paramDtlService.getParameterDetailByParamDtlCode(ParameterDetail.HEADER_BTPN_KEY);
		ParameterDetail pdPostmanToken = paramDtlService.getParameterDetailByParamDtlCode(ParameterDetail.HEADER_POSTMAN_TOKEN);
		ParameterDetail pdContentType = paramDtlService.getParameterDetailByParamDtlCode(ParameterDetail.BODY_CONTENT_TYPE);

		HttpsURLConnection.setDefaultSSLSocketFactory(getSSLContext().getSocketFactory());
		HttpsURLConnection.setDefaultHostnameVerifier(byPassHostnameVerifier());

		result = doUploadToApi(pdURL.getNameIn(), pdApiKey.getNameIn(), pdPostmanToken.getNameIn(),
				pdContentType.getNameIn(), pdChannelType.getNameIn(), pdRequestId.getNameIn(),
				pdChannelId.getNameIn(), pdUserName.getNameIn(), pdPassword.getNameIn());
		uploadedFileWO.setFileId(result);
		return result;
	}

	private String doUploadToApi(String urlApi, String apiKey, String postmanToken, String contentType,
			String channelType, String requestId, String channelId, String username, String password) throws Exception {
		URL url = new URL(urlApi);

		HttpURLConnection conn = (HttpURLConnection) url.openConnection();

		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/json; utf-8");
		conn.setRequestProperty("Accept", "application/json");
		conn.setRequestProperty("Cache-Control", "no-cache");
		conn.setRequestProperty("BTPN-ApiKey", apiKey);
		conn.setRequestProperty("Postman-Token", postmanToken);
		conn.setDoOutput(true);

		UUID uuid = UUID.randomUUID();
		String ext = FileUtil.getExtention(file.getName());

		String uuidStr = uuid.toString();
		if (uuid.toString().length() > 40)
			uuidStr = uuid.toString().substring(0, 40);

		String fileName = "ComplianceDoc_" + compDocType.replace(" ", "") + "_" + uuidStr + "." + ext;

		uploadedFileWO.setFileName(fileName);

		String jsonInputString = buildJsonInputString(fileName, uuidStr, contentType, channelType, requestId, channelId,
				username, password);

//		System.out.println(jsonInputString);
		try (OutputStream os = conn.getOutputStream()) {
			byte[] input = jsonInputString.getBytes("utf-8");
			os.write(input, 0, input.length);
		}

		String result = "";
		try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
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

		if (data != null && !data.getResponseCode().equals("00")) {
			throw new CustomAPIException(data.getResponseDesc(), uploadedFileWO);
		}

		return data.getFileId();
	}

	private String buildJsonInputString(String fileName, String uuidStr, String contentType, String channelType,
			String requestId, String channelId, String username, String password) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String dateNow = sdf.format(new Date());
		return "{" + "\"RequestUploadDocToECM\":" + "{" + "\"UploadDocument\":" + "{" + "\"fileName\":\"" + fileName
				+ "\","
				+ "\"metadata\":\"[{\\\"ColumnName\\\":\\\"ReferenceNo\\\",\\\"ColumnDataType\\\":\\\"string\\\",\\\"ColumnValue\\\":\\\""
				+ uuidStr.substring(0, 6) + "\\\"},"
				+ "{\\\"ColumnName\\\":\\\"UploadDate\\\",\\\"ColumnDataType\\\":\\\"string\\\",\\\"ColumnValue\\\":\\\""
				+ dateNow + "\\\"},"
				+ "{\\\"ColumnName\\\":\\\"UserUploadID\\\",\\\"ColumnDataType\\\":\\\"string\\\",\\\"ColumnValue\\\":\\\"optimus\\\"},"
				+ "{\\\"ColumnName\\\":\\\"DocStatus\\\",\\\"ColumnDataType\\\":\\\"string\\\",\\\"ColumnValue\\\":\\\"Active\\\"},"
				+ "{\\\"ColumnName\\\":\\\"ComplianceDocumentTypes\\\",\\\"ColumnDataType\\\":\\\"string\\\",\\\"ColumnValue\\\":\\\""
				+ compDocType + "\\\"}]\"," + "\"fileBlob\":\"" + encodedBase64File + "\"," + "\"contentType\":\""
				+ contentType + "\"" + "}," + "\"Commonparam\":" + "{" + "\"amount\":\"\"," + "\"currencyfee\":\"\","
				+ "\"original\":\"\"," + "\"referenceNo\":\"\"," + "\"processingCode\":\"\"," + "\"fee\":\"\","
				+ "\"currencyAmount\":\"\"," + "\"channelType\":\"" + channelType + "\"," + "\"terminalId\":\"\","
				+ "\"userId\":\"\"," + "\"acqId\":\"\"," + "\"transmissionDateTime\":\"" + dateNow + "\","
				+ "\"node\":\"\"," + "\"terminalName\":\"\"," + "\"requestId\":\"" + requestId + "\"," + "\"pan\":\"\","
				+ "\"channelId\":\"" + channelId + "\"" + "}," + "\"authentication\":" + "{" + "\"password\":\""
				+ password + "\"," + "\"username\":\"" + username + "\"" + "}" + "}," + "\"$path\":\"\","
				+ "\"$resourceID\":\"\"" + "}";
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

	private SSLContext getSSLContext() throws Exception {
		SSLContext sc = SSLContext.getInstance("TLS");
		sc.init(null, getTrustedCert(), new java.security.SecureRandom());

		return sc;
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

	public FileUtil getFileUtil() {
		return fileUtil;
	}

	public void setFileUtil(FileUtil fileUtil) {
		this.fileUtil = fileUtil;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public String getAbsoluteFilePath() {
		return absoluteFilePath;
	}

	public void setAbsoluteFilePath(String absoluteFilePath) {
		this.absoluteFilePath = absoluteFilePath;
	}

	public ParameterDetailService getParamDtlService() {
		return paramDtlService;
	}

	public void setParamDtlService(ParameterDetailService paramDtlService) {
		this.paramDtlService = paramDtlService;
	}

	public String getCompDocType() {
		return compDocType;
	}

	public void setCompDocType(String compDocType) {
		this.compDocType = compDocType;
	}

	public String getEncodedBase64File() {
		return encodedBase64File;
	}

	public void setEncodedBase64File(String encodedBase64File) {
		this.encodedBase64File = encodedBase64File;
	}

	public UploadedFileWO getUploadedFileWO() {
		return uploadedFileWO;
	}

	public void setUploadedFileWO(UploadedFileWO uploadedFileWO) {
		this.uploadedFileWO = uploadedFileWO;
	}

	@Override
	public UploadedFileWO getAsUploadedFileWO() throws Exception {
		return getUploadedFileWO();
	}
}
