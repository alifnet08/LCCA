package com.wo.module.trcAuditView.service;

import java.util.List;

import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.trcAuditView.dao.TrcAuditViewDao;
import com.wo.module.trcAuditView.vo.TrcAuditViewVO;

@Transactional
@Service("trcAuditViewService")
public class TrcAuditViewServiceImpl implements TrcAuditViewService {

	@Autowired
	@Qualifier("trcAuditViewDao")
	private TrcAuditViewDao trcAuditViewDao;

	@SuppressWarnings("rawtypes")
	@Override
	@Transactional(readOnly = true)
	public List<TrcAuditViewVO> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize,
			String sortField, SortOrder sortOrder) throws Exception {
		return getTrcAuditViewDao().searchData(searchCriteria, first, pageSize, sortField, sortOrder);
	}

	@SuppressWarnings("rawtypes")
	@Override
	@Transactional(readOnly = true)
	public Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception {
		return getTrcAuditViewDao().searchCountData(searchCriteria);
	}

	public TrcAuditViewDao getTrcAuditViewDao() {
		return trcAuditViewDao;
	}

	public void setTrcAuditViewDao(TrcAuditViewDao trcAuditViewDao) {
		this.trcAuditViewDao = trcAuditViewDao;
	}

}
