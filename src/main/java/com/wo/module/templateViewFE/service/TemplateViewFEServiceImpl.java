package com.wo.module.templateViewFE.service;

import java.io.Serializable;
import java.util.List;

import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.template.model.Template;
import com.wo.module.templateViewFE.dao.TemplateViewFEDao;
import com.wo.module.templateViewFE.vo.TemplateDocumentViewFEVo;
import com.wo.module.templateViewFE.vo.TemplateViewFEVo;

@Transactional
@Service("templateViewFEService")
public class TemplateViewFEServiceImpl implements TemplateViewFEService, Serializable{
	
	private static final long serialVersionUID = -5123790675153642871L;
	
	@Autowired
	@Qualifier("templateViewFEDao")
	private TemplateViewFEDao templateViewFEDao;

	@SuppressWarnings("rawtypes")
	@Override
	public List<TemplateViewFEVo> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize,
			String sortField, SortOrder sortOrder) throws Exception {
		return templateViewFEDao.searchData(searchCriteria, first, pageSize, sortField, sortOrder);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception {
		return templateViewFEDao.searchCountData(searchCriteria);
	}

	@Override
	public Template findById(Long templateId) {
		return templateViewFEDao.getById(templateId);
	}
	
	@Override
	public List<TemplateDocumentViewFEVo> getTemplateDocByType(Long id, String documentType) {
		return templateViewFEDao.getTemplateDocByType(id, documentType);
	}
	
	public TemplateViewFEDao getTemplateViewFEDao() {
		return templateViewFEDao;
	}

	public void setTemplateViewFEDao(TemplateViewFEDao templateViewFEDao) {
		this.templateViewFEDao = templateViewFEDao;
	}

}
