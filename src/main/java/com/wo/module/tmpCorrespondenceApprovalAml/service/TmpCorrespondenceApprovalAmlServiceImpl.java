package com.wo.module.tmpCorrespondenceApprovalAml.service;

import java.util.List;

import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.tmpCorrespondenceApproval.vo.TmpCorrespondenceApprovalSearchVo;
import com.wo.module.tmpCorrespondenceApprovalAml.dao.TmpCorrespondenceApprovalAmlDao;

@Transactional
@Repository("tmpCorrespondenceApprovalAmlService")
public class TmpCorrespondenceApprovalAmlServiceImpl implements TmpCorrespondenceApprovalAmlService{

	@Autowired
	@Qualifier("tmpCorrespondenceApprovalAmlDao")
	private TmpCorrespondenceApprovalAmlDao tmpCorrespondenceApprovalAmlDao;
	
	public TmpCorrespondenceApprovalAmlDao getTmpCorrespondenceApprovalAmlDao() {
		return tmpCorrespondenceApprovalAmlDao;
	}

	public void setTmpCorrespondenceApprovalAmlDao(TmpCorrespondenceApprovalAmlDao tmpCorrespondenceApprovalAmlDao) {
		this.tmpCorrespondenceApprovalAmlDao = tmpCorrespondenceApprovalAmlDao;
	}

	@SuppressWarnings("rawtypes")
	@Override
	@Transactional(readOnly = true)
	public List<TmpCorrespondenceApprovalSearchVo> searchData(List<? extends SearchObject> searchCriteria, int first,
			int pageSize, String sortField, SortOrder sortOrder) throws Exception {
		return tmpCorrespondenceApprovalAmlDao.searchData(searchCriteria, first, pageSize, sortField, sortOrder);
	}

	@SuppressWarnings("rawtypes")
	@Override
	@Transactional(readOnly = true)
	public Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception {
		return tmpCorrespondenceApprovalAmlDao.searchCountData(searchCriteria);
	}

}
