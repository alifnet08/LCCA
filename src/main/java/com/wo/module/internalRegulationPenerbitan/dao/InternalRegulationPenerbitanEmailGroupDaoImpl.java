package com.wo.module.internalRegulationPenerbitan.dao;

import java.io.Serializable;
import java.util.List;

import org.primefaces.model.SortOrder;
import org.springframework.stereotype.Repository;

import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.internalRegulationPenerbitan.model.InternalRegulationPenerbitanEmailGroup;

@Repository("internalRegulationPenerbitanEmailGroupDao")
public class InternalRegulationPenerbitanEmailGroupDaoImpl extends GenericDAOHibernate<InternalRegulationPenerbitanEmailGroup, Long>
	implements InternalRegulationPenerbitanEmailGroupDao, Serializable{

	private static final long serialVersionUID = -842478090538637697L;

	@SuppressWarnings("rawtypes")
	@Override
	public List<InternalRegulationPenerbitanEmailGroup> searchData(List<? extends SearchObject> searchCriteria, int first,
			int pageSize, String sortField, SortOrder sortOrder) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}