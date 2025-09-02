/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.trcCorrespondenceView.service;

import java.util.List;

import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.trcCorrespondenceView.dao.TrcCorrespondenceViewDao;
import com.wo.module.trcCorrespondenceView.vo.TrcCorrespondenceViewSearchVo;

@Transactional
@Service("trcCorrespondenceViewService")
public class TrcCorrespondenceViewServiceImpl implements TrcCorrespondenceViewService {
	@Autowired
	@Qualifier("trcCorrespondenceViewDao")
	private TrcCorrespondenceViewDao trcCorrespondenceViewDao;

	@SuppressWarnings("rawtypes")
	@Override
	@Transactional(readOnly = true)
	public List<TrcCorrespondenceViewSearchVo> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize, String sortField,
			SortOrder sortOrder) throws Exception {
		return trcCorrespondenceViewDao.searchData(searchCriteria, first, pageSize, sortField, sortOrder);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	@Transactional(readOnly = true)
	public Long searchCountData(List searchCriteria) throws Exception {
		return trcCorrespondenceViewDao.searchCountData(searchCriteria);
	}

	

}
