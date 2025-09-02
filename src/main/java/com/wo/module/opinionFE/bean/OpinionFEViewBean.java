package com.wo.module.opinionFE.bean;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.wo.module.article.model.Article;
import com.wo.module.article.model.ArticleDocument;
import com.wo.module.articleFE.service.ArticleFEService;
import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.model.UploadedFileWO;
import com.wo.module.common.util.FileUtil;
import com.wo.module.common.utility.CallApiManager;
import com.wo.module.externalRegulation.service.RegulationService;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.opinionFE.constant.OpinionFEConstants;

public class OpinionFEViewBean extends CommonBean implements Serializable {

	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(OpinionFEViewBean.class);

	private Article article;
	private Integer hits;
//	private TmpArticle tmpArticle;

	private List<String> tagName;

	private String editedId;

	private ArticleFEService articleService;
	
	private RegulationService regulationService;

	public FacesUtil facesUtil;
	
	private Integer lastSequenceOfDtl;

	private List<UploadedFileWO> uploadFiles;
	private FileUtil fileUtil;
	
	SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", new Locale("id", "ID"));
	
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
		if (facesUtil.retrieveRequestParam("first") != null) {
			testFirst = Integer.parseInt(facesUtil.retrieveRequestParam("first"));
			facesUtil.setSessionAttribute("FIRST_OPINION_FE", testFirst);
			facesUtil.setSessionAttribute("BACK_SESSION", false);
		}
		handleEdit();
		fileUtil = FileUtil.getInstance();
	}
	
	private void handleEdit() {
		String editId = facesUtil.retrieveRequestParam("id");
		String token = facesUtil.retrieveRequestParam("token");
		if(StringUtils.isNotEmpty(token))
			editId = Constants.decryptString(token);
		Long idLong = Long.parseLong(editId);
		article = articleService.findById(idLong);
		
		for (int i = 0; i < article.getArticleDocuments().size(); i++) {
			if (uploadFiles == null)
				uploadFiles = new ArrayList<UploadedFileWO>();
			ArticleDocument ra = article.getArticleDocuments().get(i);
			UploadedFileWO uf = new UploadedFileWO();
			uf.setFileName(ra.getAttachmentFile());
			uf.setFileId(ra.getFileId());
			uf.setIsNew(false);
			uf.setFileSize(ra.getFileSize());
			uploadFiles.add(uf);
			
		}
//		for (int i = 0; i < tmpArticle.getTmpArticleTagList().size(); i++) {
//			TmpArticleTag articleTag = tmpArticle.getTmpArticleTagList().get(i);
//			tmpArticle.setTagName(articleTag.getTag());
//		}
		lastSequenceOfDtl = 0;
		
//		List<String> listTag = new ArrayList<String>();
//		for (int i = 0; i < tmpArticle.getTmpArticleTagList().size(); i++) {
//			TmpArticleTag articleTag = tmpArticle.getTmpArticleTagList().get(i);
//			listTag.add(articleTag.getTag());
//		}
//
//		if (tmpArticle.getTmpArticleTagList().size() > 0) {
//			tagName = listTag;
//		}
		
		try {
			hits = regulationService.getCountHitRegulation(idLong, "/compliance/pages/opinionFE/opinionFEView.faces");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}

	public void downloadFile (String fileId, String fileName, byte[] content) throws Exception {
		CallApiManager.downloadFile(fileId, fileName, content, parameterDetailService);
	}

	public void backSession() {
		if (facesUtil.retrieveRequestParam("first") != null) {
			facesUtil.setSessionAttribute("BACK_SESSION", true);
		}
	}
	
	public void cancel() {
		try {
			facesUtil.redirect("/pages/opinionFE/opinionFE.faces");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		OpinionFEViewBean.logger = logger;
	}

//	public TmpArticle getTmpArticle() {
//		return tmpArticle;
//	}
//
//	public void setTmpArticle(TmpArticle tmpArticle) {
//		this.tmpArticle = tmpArticle;
//	}

	public List<String> getTagName() {
		return tagName;
	}

	public void setTagName(List<String> tagName) {
		this.tagName = tagName;
	}

	public String getEditedId() {
		return editedId;
	}

	public void setEditedId(String editedId) {
		this.editedId = editedId;
	}

	public ArticleFEService getArticleService() {
		return articleService;
	}

	public void setArticleService(ArticleFEService articleService) {
		this.articleService = articleService;
	}

	public FacesUtil getFacesUtil() {
		return facesUtil;
	}

	public void setFacesUtil(FacesUtil facesUtil) {
		this.facesUtil = facesUtil;
	}

	public Integer getLastSequenceOfDtl() {
		return lastSequenceOfDtl;
	}

	public void setLastSequenceOfDtl(Integer lastSequenceOfDtl) {
		this.lastSequenceOfDtl = lastSequenceOfDtl;
	}

	public List<UploadedFileWO> getUploadFiles() {
		return uploadFiles;
	}

	public void setUploadFiles(List<UploadedFileWO> uploadFiles) {
		this.uploadFiles = uploadFiles;
	}

	public FileUtil getFileUtil() {
		return fileUtil;
	}

	public void setFileUtil(FileUtil fileUtil) {
		this.fileUtil = fileUtil;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public SimpleDateFormat getSdf() {
		return sdf;
	}

	public void setSdf(SimpleDateFormat sdf) {
		this.sdf = sdf;
	}

	public Article getArticle() {
		return article;
	}

	public void setArticle(Article article) {
		this.article = article;
	}

	public Integer getHits() {
		return hits;
	}

	public void setHits(Integer hits) {
		this.hits = hits;
	}

	public RegulationService getRegulationService() {
		return regulationService;
	}

	public void setRegulationService(RegulationService regulationService) {
		this.regulationService = regulationService;
	}
	
	public Integer getTestFirst() {
		return testFirst;
	}

	public void setTestFirst(Integer testFirst) {
		this.testFirst = testFirst;
	}
}