/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.trcComplianceReviewView.service;

import java.util.List;

import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.complianceTestingMockup.vo.ComplianceTestingVO;
import com.wo.module.trcComplianceReviewView.dao.TrcComplianceReviewViewDao;
import com.wo.module.trcComplianceReviewView.vo.ComplianceReviewViewVO;

@Transactional
@Service("trcComplianceReviewViewService")
public class TrcComplianceReviewViewServiceImpl implements TrcComplianceReviewViewService {
	@Autowired
	@Qualifier("trcComplianceReviewViewDao")
	private TrcComplianceReviewViewDao trcComplianceReviewViewDao;

	public TrcComplianceReviewViewDao getTrcComplianceReviewViewDao() {
		return trcComplianceReviewViewDao;
	}

	public void setTrcComplianceReviewViewDao(TrcComplianceReviewViewDao trcComplianceReviewViewDao) {
		this.trcComplianceReviewViewDao = trcComplianceReviewViewDao;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<ComplianceTestingVO> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize,
			String sortField, SortOrder sortOrder) throws Exception {
		return trcComplianceReviewViewDao.searchData(searchCriteria, first, pageSize, sortField, sortOrder);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception {
		return trcComplianceReviewViewDao.searchCountData(searchCriteria);
	}


	

}
