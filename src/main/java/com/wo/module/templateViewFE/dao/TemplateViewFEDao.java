package com.wo.module.templateViewFE.dao;

import java.util.List;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.template.model.Template;
import com.wo.module.templateViewFE.vo.TemplateDocumentViewFEVo;
import com.wo.module.templateViewFE.vo.TemplateViewFEVo;

public interface TemplateViewFEDao extends GenericDAO<Template, Long>, RetrieverDataPage<TemplateViewFEVo>{

	public List<TemplateDocumentViewFEVo> getTemplateDocByType(Long id, String documentType);
	
}
