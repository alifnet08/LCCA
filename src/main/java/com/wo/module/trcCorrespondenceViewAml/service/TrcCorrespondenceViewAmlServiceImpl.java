package com.wo.module.trcCorrespondenceViewAml.service;

import java.util.List;

import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.trcCorrespondenceView.vo.TrcCorrespondenceViewSearchVo;
import com.wo.module.trcCorrespondenceViewAml.dao.TrcCorrespondenceViewAmlDao;

@Transactional
@Service("trcCorrespondenceViewAmlService")
public class TrcCorrespondenceViewAmlServiceImpl implements TrcCorrespondenceViewAmlService {
	@Autowired
	@Qualifier("trcCorrespondenceViewAmlDao")
	private TrcCorrespondenceViewAmlDao trcCorrespondenceViewAmlDao;

	@SuppressWarnings("rawtypes")
	@Override
	@Transactional(readOnly = true)
	public List<TrcCorrespondenceViewSearchVo> searchData(List<? extends SearchObject> searchCriteria, int first,
			int pageSize, String sortField, SortOrder sortOrder) throws Exception {
		return getTrcCorrespondenceViewAmlDao().searchData(searchCriteria, first, pageSize, sortField, sortOrder);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	@Transactional(readOnly = true)
	public Long searchCountData(List searchCriteria) throws Exception {
		return getTrcCorrespondenceViewAmlDao().searchCountData(searchCriteria);
	}

	public TrcCorrespondenceViewAmlDao getTrcCorrespondenceViewAmlDao() {
		return trcCorrespondenceViewAmlDao;
	}

	public void setTrcCorrespondenceViewAmlDao(TrcCorrespondenceViewAmlDao trcCorrespondenceViewAmlDao) {
		this.trcCorrespondenceViewAmlDao = trcCorrespondenceViewAmlDao;
	}

}
