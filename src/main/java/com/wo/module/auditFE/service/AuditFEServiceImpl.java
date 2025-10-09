/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.auditFE.service;

import java.util.List;

import javax.transaction.Transactional;

import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.wo.module.auditFE.dao.AuditFEDAO;
import com.wo.module.auditFE.vo.AuditFEVO;
import com.wo.module.common.paging.SearchObject;

@Transactional
@Service("auditFEService")
public class AuditFEServiceImpl implements AuditFEService {
	@Autowired
	@Qualifier("auditFEDAO")
	private AuditFEDAO auditFEDAO;

	@Override
	public List<AuditFEVO> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize,
			String sortField, SortOrder sortOrder) throws Exception {
		return auditFEDAO.searchData(searchCriteria, first, pageSize, sortField, sortOrder);
	}

	@Override
	public Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception {
		return auditFEDAO.searchCountData(searchCriteria);
	}

	public AuditFEDAO getAuditFEDAO() {
		return auditFEDAO;
	}

	public void setAuditFEDAO(AuditFEDAO auditFEDAO) {
		this.auditFEDAO = auditFEDAO;
	}

	@Override
	public AuditFEVO searchForDetail(Long id) {
		// TODO Auto-generated method stub
		return auditFEDAO.searchForDetail(id);
	}
}
