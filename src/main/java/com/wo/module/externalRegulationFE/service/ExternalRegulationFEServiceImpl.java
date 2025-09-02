package com.wo.module.externalRegulationFE.service;

import java.util.List;

import javax.transaction.Transactional;

import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.externalRegulationFE.dao.ExternalRegulationFEDAO;
import com.wo.module.externalRegulationFE.vo.ExternalRegulationFEVO;

@Transactional
@Service("externalRegulationFEService")
public class ExternalRegulationFEServiceImpl implements ExternalRegulationFEService {

	@Autowired
	@Qualifier("externalRegulationFEDAO")
	private ExternalRegulationFEDAO externalRegulationFEDAO;
	
	@Override
	public List<ExternalRegulationFEVO> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize,
			String sortField, SortOrder sortOrder) throws Exception {
		return externalRegulationFEDAO.searchData(searchCriteria, first, pageSize, sortField, sortOrder);
	}

	@Override
	public Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception {
		return externalRegulationFEDAO.searchCountData(searchCriteria);
	}
	
}