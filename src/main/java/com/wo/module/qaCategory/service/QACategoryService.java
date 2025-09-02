package com.wo.module.qaCategory.service;

import java.util.List;

import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.qaCategory.model.QACategory;

public interface QACategoryService extends RetrieverDataPage<QACategory>{
	
	public void save(QACategory qa);
	
	public void edit(QACategory qa);
	
	public void delete(QACategory qa);
	
	public QACategory findById(Long id);
	
	public List<QACategory> searchCategoryListByCode(String categoryCode) throws Exception;
	
	public Integer duplicate(String categoryCode, Long userId);
}