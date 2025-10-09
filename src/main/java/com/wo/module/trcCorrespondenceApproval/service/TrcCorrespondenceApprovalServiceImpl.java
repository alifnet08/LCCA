/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.trcCorrespondenceApproval.service;

import java.util.List;

import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.trcCorrespondenceApproval.dao.TrcCorrespondenceApprovalDao;
import com.wo.module.trcCorrespondenceApproval.vo.TrcCorrespondenceApprovalSearchVo;

@Transactional
@Service("trcCorrespondenceApprovalService")
public class TrcCorrespondenceApprovalServiceImpl implements TrcCorrespondenceApprovalService {
	@Autowired
	@Qualifier("trcCorrespondenceApprovalDao")
	private TrcCorrespondenceApprovalDao trcCorrespondenceApprovalDao;

	@SuppressWarnings("rawtypes")
	@Override
	@Transactional(readOnly = true)
	public List<TrcCorrespondenceApprovalSearchVo> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize, String sortField,
			SortOrder sortOrder) throws Exception {
		return trcCorrespondenceApprovalDao.searchData(searchCriteria, first, pageSize, sortField, sortOrder);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	@Transactional(readOnly = true)
	public Long searchCountData(List searchCriteria) throws Exception {
		return trcCorrespondenceApprovalDao.searchCountData(searchCriteria);
	}

	

}
