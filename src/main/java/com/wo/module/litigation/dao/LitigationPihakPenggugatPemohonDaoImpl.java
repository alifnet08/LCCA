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
import com.wo.module.litigation.model.LitigationPihakPenggugatPemohon;

/**
 *
 * @author hendra
 */
@Repository("litigationPihakPenggugatPemohonDao")
public class LitigationPihakPenggugatPemohonDaoImpl extends GenericDAOHibernate<LitigationPihakPenggugatPemohon, Long> implements LitigationPihakPenggugatPemohonDao {
	
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(LitigationPihakPenggugatPemohonDaoImpl.class);

	@SuppressWarnings("rawtypes")
	@Override
	public List<LitigationPihakPenggugatPemohon> searchData(List<? extends SearchObject> searchCriteria, int first,
			int pageSize, String sortField, SortOrder sortOrder) throws Exception {
		return null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception {
		return null;
	}
}
