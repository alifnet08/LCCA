/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.trcRmdView.service;

import java.util.List;

import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.trcRmdView.dao.TrcRmdViewDao;
import com.wo.module.trcRmdView.vo.TrcRmdViewSearchVo;

@Transactional
@Service("trcRmdViewService")
public class TrcRmdViewServiceImpl implements TrcRmdViewService {
	@Autowired
	@Qualifier("trcRmdViewDao")
	private TrcRmdViewDao trcRmdViewDao;

	public TrcRmdViewDao getTrcRmdViewDao() {
		return trcRmdViewDao;
	}

	public void setTrcRmdViewDao(TrcRmdViewDao trcRmdViewDao) {
		this.trcRmdViewDao = trcRmdViewDao;
	}

	@SuppressWarnings("rawtypes")
	@Override
	@Transactional(readOnly = true)
	public List<TrcRmdViewSearchVo> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize, String sortField,
			SortOrder sortOrder) throws Exception {
		return trcRmdViewDao.searchData(searchCriteria, first, pageSize, sortField, sortOrder);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	@Transactional(readOnly = true)
	public Long searchCountData(List searchCriteria) throws Exception {
		return trcRmdViewDao.searchCountData(searchCriteria);
	}

	

}
