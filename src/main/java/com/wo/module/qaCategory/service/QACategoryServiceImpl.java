/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.qaCategory.service;

import java.util.List;

import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.qaCategory.dao.QACategoryDAO;
import com.wo.module.qaCategory.model.QACategory;

@Transactional
@Service("qaCategoryService")
public class QACategoryServiceImpl implements QACategoryService {
    @Autowired
    @Qualifier("qaCategoryDAO")
    private QACategoryDAO qaCategoryDAO;

	@SuppressWarnings("rawtypes")
	@Override
	public List<QACategory> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize,
			String sortField, SortOrder sortOrder) throws Exception {
		return qaCategoryDAO.searchData(searchCriteria, first, pageSize, sortField, sortOrder);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception {
		return qaCategoryDAO.searchCountData(searchCriteria);
	}

	@Override
	public void save(QACategory qa) {
		qaCategoryDAO.save(qa);
	}

	@Override
	public void edit(QACategory qa) {
		qaCategoryDAO.update(qa);
	}

	@Override
	public void delete(QACategory qa) {
		qaCategoryDAO.delete(qa);
	}

	@Override
	public QACategory findById(Long id) {
		return qaCategoryDAO.getById(id);
	}

	public QACategoryDAO getQaCategoryDAO() {
		return qaCategoryDAO;
	}

	public void setQaCategoryDAO(QACategoryDAO qaCategoryDAO) {
		this.qaCategoryDAO = qaCategoryDAO;
	}

	@Override
	public List<QACategory> searchCategoryListByCode(String categoryCode) throws Exception {
		return qaCategoryDAO.searchCategoryListByCode(categoryCode);
	}

	@Override
	public Integer duplicate(String categoryCode, Long userId) {
		return qaCategoryDAO.duplicate(categoryCode, userId);
	}
}
