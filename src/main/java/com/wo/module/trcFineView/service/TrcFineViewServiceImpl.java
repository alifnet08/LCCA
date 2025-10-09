package com.wo.module.trcFineView.service;

import java.util.List;

import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.trcFineApproval.model.TrcFine;
import com.wo.module.trcFineView.dao.TrcFineViewDAO;
import com.wo.module.trcFineView.vo.TrcFineViewSearchVO;

@Transactional
@Service("trcFineViewService")
public class TrcFineViewServiceImpl implements TrcFineViewService {
	@Autowired
	@Qualifier("trcFineViewDAO")
	private TrcFineViewDAO trcFineViewDAO;

	@SuppressWarnings("rawtypes")
	@Override
	@Transactional(readOnly = true)
	public List<TrcFineViewSearchVO> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize,
			String sortField, SortOrder sortOrder) throws Exception {
		return trcFineViewDAO.searchData(searchCriteria, first, pageSize, sortField, sortOrder);
	}

	@SuppressWarnings({ "rawtypes"})
	@Override
	@Transactional(readOnly = true)
	public Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception {
		return trcFineViewDAO.searchCountData(searchCriteria);
	}

	public TrcFineViewDAO getTrcFineViewDAO() {
		return trcFineViewDAO;
	}

	public void setTrcFineViewDAO(TrcFineViewDAO trcFineViewDAO) {
		this.trcFineViewDAO = trcFineViewDAO;
	}

	@Override
	public TrcFine findById(Long id) {
		return trcFineViewDAO.getById(id);
	}
}