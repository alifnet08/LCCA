/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.tmpCorrespondenceApproval.service;

import java.util.List;

import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.tmpCorrespondenceApproval.dao.TmpCorrespondenceApprovalDao;
import com.wo.module.tmpCorrespondenceApproval.vo.TmpCorrespondenceApprovalSearchVo;

@Transactional
@Service("tmpCorrespondenceApprovalService")
public class TmpCorrespondenceApprovalServiceImpl implements TmpCorrespondenceApprovalService {
	@Autowired
	@Qualifier("tmpCorrespondenceApprovalDao")
	private TmpCorrespondenceApprovalDao tmpCorrespondenceApprovalDao;

	@SuppressWarnings("rawtypes")
	@Override
	@Transactional(readOnly = true)
	public List<TmpCorrespondenceApprovalSearchVo> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize, String sortField,
			SortOrder sortOrder) throws Exception {
		return tmpCorrespondenceApprovalDao.searchData(searchCriteria, first, pageSize, sortField, sortOrder);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	@Transactional(readOnly = true)
	public Long searchCountData(List searchCriteria) throws Exception {
		return tmpCorrespondenceApprovalDao.searchCountData(searchCriteria);
	}

	

}
