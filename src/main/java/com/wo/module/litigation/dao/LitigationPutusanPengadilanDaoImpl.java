/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.litigation.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.primefaces.model.SortOrder;
import org.springframework.stereotype.Repository;

import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.litigation.model.LitigationPutusanPengadilan;

/**
 *
 * @author hendra
 */
@Repository("litigationPutusanPengadilanDao")
public class LitigationPutusanPengadilanDaoImpl extends GenericDAOHibernate<LitigationPutusanPengadilan, Long> implements LitigationPutusanPengadilanDao {
	
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(LitigationPutusanPengadilanDaoImpl.class);

	@SuppressWarnings("rawtypes")
	@Override
	public List<LitigationPutusanPengadilan> searchData(List<? extends SearchObject> searchCriteria, int first,
			int pageSize, String sortField, SortOrder sortOrder) throws Exception {
		return null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception {
		return null;
	}
}
