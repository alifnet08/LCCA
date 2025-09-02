package com.wo.module.templateViewFE.service;

import java.util.List;

import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.template.model.Template;
import com.wo.module.templateViewFE.vo.TemplateDocumentViewFEVo;
import com.wo.module.templateViewFE.vo.TemplateViewFEVo;

public interface TemplateViewFEService extends RetrieverDataPage<TemplateViewFEVo>{

	public Template findById(Long templateId);
	
	public List<TemplateDocumentViewFEVo> getTemplateDocByType(Long id, String documentType);
	
}
