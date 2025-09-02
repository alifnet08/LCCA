package com.wo.module.templateViewFE.bean;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.model.UploadedFileWO;
import com.wo.module.common.util.FileUtil;
import com.wo.module.common.utility.CallApiManager;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.template.model.Template;
import com.wo.module.templateViewFE.service.TemplateViewFEService;
import com.wo.module.templateViewFE.vo.TemplateDocumentViewFEVo;
import com.wo.module.user.service.UserService;

public class TemplateViewFEEditBean extends CommonBean implements Serializable{

	private static final long serialVersionUID = 1350148715357857833L;
	private static final Logger logger = Logger.getLogger(TemplateViewFEEditBean.class);

	private TemplateViewFEService templateViewFEService;
	private UserService userService;
	
	private Template template;
	
	private String viewId;
	private String docFileCode;
	private String fillFileCode;
	
	private SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");

	private FacesUtil facesUtil;
	private FileUtil fileUtil;
	
	private List<UploadedFileWO> uploadedFilesDoc;
	private List<UploadedFileWO> uploadedFilesFill;
	
	private Integer testFirst;
	
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
		testFirst = Integer.parseInt(facesUtil.retrieveRequestParam("first")!=null?facesUtil.retrieveRequestParam("first"):"0");
		facesUtil.setSessionAttribute("FIRST_TEMPLATE_FE", testFirst);
		facesUtil.setSessionAttribute("BACK_SESSION", false);
		checkNewOrEdit();
		fileUtil = FileUtil.getInstance();
	}
	
	private void checkNewOrEdit() {
		this.viewId = facesUtil.retrieveRequestParam("viewId");
		String token = facesUtil.retrieveRequestParam("token");
		if(StringUtils.isNotEmpty(token)) {
			viewId = Constants.decryptString(token);
		}
		if (StringUtils.isBlank(viewId)) {
			// do nothing
		} else {
			handleView(viewId);
		}
	}

	private void handleView(String viewId) {
		Long viewIdLong = Long.parseLong(viewId);
		
		uploadedFilesDoc = new ArrayList<UploadedFileWO>();
		uploadedFilesFill = new ArrayList<UploadedFileWO>();
		template = templateViewFEService.findById(viewIdLong);
		
		List<TemplateDocumentViewFEVo> templateDocument = templateViewFEService.getTemplateDocByType(template.getTemplateId(), ParameterDetail.PARAM_DET_CODE_TEMPLATE_DOC_IN);
		if (templateDocument != null) {
			this.docFileCode = ParameterDetail.PARAM_DET_CODE_TEMPLATE_DOC_IN;
			for (int i = 0; i < templateDocument.size(); i++) {
				TemplateDocumentViewFEVo vo = (TemplateDocumentViewFEVo) templateDocument.get(i);
				UploadedFileWO uf = new UploadedFileWO();
				uf.setFileName(vo.getAttachmentFile());
				uf.setFileId(vo.getFileId());
				uf.setIsNew(false);
				uf.setFileSize(vo.getFileSize());
				uploadedFilesDoc.add(uf);
			}
		}
		
		List<TemplateDocumentViewFEVo> templateFill = templateViewFEService.getTemplateDocByType(template.getTemplateId(), ParameterDetail.PARAM_DET_CODE_TEMPLATE_FILL_INSTRUCTION_IN);
		if (templateFill != null) {
			this.fillFileCode = ParameterDetail.PARAM_DET_CODE_TEMPLATE_FILL_INSTRUCTION_IN;
			for (int i = 0; i < templateFill.size(); i++) {
				TemplateDocumentViewFEVo vo = (TemplateDocumentViewFEVo) templateFill.get(i);
				UploadedFileWO uf = new UploadedFileWO();
				uf.setFileName(vo.getAttachmentFile());
				uf.setFileId(vo.getFileId());
				uf.setIsNew(false);
				uf.setFileSize(vo.getFileSize());
				uploadedFilesFill.add(uf);
			}
		}
	}
	
	public void cancel() {
		try {
			
			facesUtil.setSessionAttribute("BACK_SESSION", true);
			facesUtil.redirect("/pages/templateViewFE/templateViewFE.faces");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void downloadFile(String fileId, String fileName, byte[] content) throws Exception {
		CallApiManager.downloadFile(fileId, fileName, content, parameterDetailService);
	}
	
	public TemplateViewFEService getTemplateViewFEService() {
		return templateViewFEService;
	}

	public void setTemplateViewFEService(TemplateViewFEService templateViewFEService) {
		this.templateViewFEService = templateViewFEService;
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

	public Template getTemplate() {
		return template;
	}

	public void setTemplate(Template template) {
		this.template = template;
	}

	public String getViewId() {
		return viewId;
	}

	public void setViewId(String viewId) {
		this.viewId = viewId;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public FacesUtil getFacesUtil() {
		return facesUtil;
	}

	public void setFacesUtil(FacesUtil facesUtil) {
		this.facesUtil = facesUtil;
	}

	public List<UploadedFileWO> getUploadedFilesDoc() {
		return uploadedFilesDoc;
	}

	public void setUploadedFilesDoc(List<UploadedFileWO> uploadedFilesDoc) {
		this.uploadedFilesDoc = uploadedFilesDoc;
	}

	public List<UploadedFileWO> getUploadedFilesFill() {
		return uploadedFilesFill;
	}

	public void setUploadedFilesFill(List<UploadedFileWO> uploadedFilesFill) {
		this.uploadedFilesFill = uploadedFilesFill;
	}

	public FileUtil getFileUtil() {
		return fileUtil;
	}

	public void setFileUtil(FileUtil fileUtil) {
		this.fileUtil = fileUtil;
	}

	public String getDocFileCode() {
		return docFileCode;
	}

	public void setDocFileCode(String docFileCode) {
		this.docFileCode = docFileCode;
	}

	public String getFillFileCode() {
		return fillFileCode;
	}

	public void setFillFileCode(String fillFileCode) {
		this.fillFileCode = fillFileCode;
	}

	public Integer getTestFirst() {
		return testFirst;
	}

	public void setTestFirst(Integer testFirst) {
		this.testFirst = testFirst;
	}
	
}
