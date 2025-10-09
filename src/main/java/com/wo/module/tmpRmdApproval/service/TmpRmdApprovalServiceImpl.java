/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.tmpRmdApproval.service;

import java.util.List;

import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.tmpRmdApproval.dao.TmpRmdApprovalDao;
import com.wo.module.tmpRmdApproval.vo.TmpRmdApprovalSearchVo;

@Transactional
@Service("tmpRmdApprovalService")
public class TmpRmdApprovalServiceImpl implements TmpRmdApprovalService {
	@Autowired
	@Qualifier("tmpRmdApprovalDao")
	private TmpRmdApprovalDao tmpRmdApprovalDao;

	public TmpRmdApprovalDao getTmpRmdApprovalDao() {
		return tmpRmdApprovalDao;
	}

	public void setTmpRmdApprovalDao(TmpRmdApprovalDao tmpRmdApprovalDao) {
		this.tmpRmdApprovalDao = tmpRmdApprovalDao;
	}

	@SuppressWarnings("rawtypes")
	@Override
	@Transactional(readOnly = true)
	public List<TmpRmdApprovalSearchVo> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize, String sortField,
			SortOrder sortOrder) throws Exception {
		return tmpRmdApprovalDao.searchData(searchCriteria, first, pageSize, sortField, sortOrder);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	@Transactional(readOnly = true)
	public Long searchCountData(List searchCriteria) throws Exception {
		return tmpRmdApprovalDao.searchCountData(searchCriteria);
	}

	

}
