package com.wo.module.trcCorrespondenceApprovalAml.service;

import java.util.List;

import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.trcCorrespondenceApproval.vo.TrcCorrespondenceApprovalSearchVo;
import com.wo.module.trcCorrespondenceApprovalAml.dao.TrcCorrespondenceApprovalAmlDao;

@Transactional
@Service("trcCorrespondenceApprovalAmlService")
public class TrcCorrespondenceApprovalAmlServiceImpl implements TrcCorrespondenceApprovalAmlService {
	@Autowired
	@Qualifier("trcCorrespondenceApprovalAmlDao")
	private TrcCorrespondenceApprovalAmlDao trcCorrespondenceApprovalAmlDao;

	@SuppressWarnings("rawtypes")
	@Override
	@Transactional(readOnly = true)
	public List<TrcCorrespondenceApprovalSearchVo> searchData(List<? extends SearchObject> searchCriteria, int first,
			int pageSize, String sortField, SortOrder sortOrder) throws Exception {
		return trcCorrespondenceApprovalAmlDao.searchData(searchCriteria, first, pageSize, sortField, sortOrder);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	@Transactional(readOnly = true)
	public Long searchCountData(List searchCriteria) throws Exception {
		return trcCorrespondenceApprovalAmlDao.searchCountData(searchCriteria);
	}

}
