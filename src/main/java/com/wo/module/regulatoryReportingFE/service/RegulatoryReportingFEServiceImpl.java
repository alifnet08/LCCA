package com.wo.module.regulatoryReportingFE.service;

import java.io.Serializable;
import java.util.List;

import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.regulatoryReportingFE.dao.RegulatoryReportingFEDao;
import com.wo.module.regulatoryReportingFE.vo.RegulatoryReportingFEVo;
import com.wo.module.trcRmd.model.TrcRmd;

@Transactional
@Service("regulatoryReportingFEService")
public class RegulatoryReportingFEServiceImpl implements RegulatoryReportingFEService, Serializable{

	private static final long serialVersionUID = -301909351088490904L;
	
	@Autowired
	@Qualifier("regulatoryReportingFEDao")
	private RegulatoryReportingFEDao regulatoryReportingFEDao;

	@SuppressWarnings("rawtypes")
	@Override
	public List<RegulatoryReportingFEVo> searchData(List<? extends SearchObject> searchCriteria, int first,
			int pageSize, String sortField, SortOrder sortOrder) throws Exception {
		return regulatoryReportingFEDao.searchData(searchCriteria, first, pageSize, sortField, sortOrder);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception {
		return regulatoryReportingFEDao.searchCountData(searchCriteria);
	}

	@Override
	public void save(TrcRmd entity) {
		regulatoryReportingFEDao.save(entity);
	}

	@Override
	public void update(TrcRmd entity) {
		regulatoryReportingFEDao.update(entity);
	}

	@Override
	public void delete(TrcRmd entity) {
		regulatoryReportingFEDao.delete(entity);
	}

	@Override
	public TrcRmd findById(Long id) {
		return regulatoryReportingFEDao.getById(id);
	}
	
	public RegulatoryReportingFEDao getRegulatoryReportingFEDao() {
		return regulatoryReportingFEDao;
	}

	public void setRegulatoryReportingFEDao(RegulatoryReportingFEDao regulatoryReportingFEDao) {
		this.regulatoryReportingFEDao = regulatoryReportingFEDao;
	}

}
