/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.lov.dao;


import java.io.Serializable;
import java.util.List;

import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.wo.module.lov.bean.SelectorModel;

import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.externalRegulation.dao.ExternalRegulationDao;

/**
 *
 * @author hendra
 */
@Repository("selectorDao")
public class SelectorDaoImpl extends GenericDAOHibernate<Object, Serializable> 
    implements SelectorDao {
    private static Logger logger = Logger.getLogger(SelectorDaoImpl.class);
    
    @Autowired
    @Qualifier("externalRegulationDao")
    private ExternalRegulationDao externalRegulationDao;
    
    @SuppressWarnings({ "rawtypes"})
	@Override
    public List searchData(List <? extends SearchObject> searchCriteria, int first, int pageSize, String sortField, SortOrder sortOrder) {
        String hqlStr = "";
        for (SearchObject so : searchCriteria) {
            if (StringUtils.equals(so.getSearchColumn(), SelectorModel.SEARCH_COL_QUERY_ALL)) {
                hqlStr = so.getSearchValueAsString();
            }
        }        
        
        Query query = getSession().createQuery(hqlStr);
        if (first >= 0 && pageSize > 0) {
            query.setFirstResult(first);
            query.setMaxResults(pageSize);            
        } else {
            //won't page anything, return all
        }
        try {
            List result = 
                    query.getResultList();         
            return result;
            
        } catch (Exception ex) {
            logger.error("Error querying search data lov [" + ex.getMessage() + "]", ex);
            throw new RuntimeException("Error querying search data lov [" + ex.getMessage() + "]", ex);
        }
    }

    @SuppressWarnings("rawtypes")
	@Override
    public Long searchCountData(List <? extends SearchObject> searchCriteria) {
        String hqlStr = "";
        for (SearchObject so : searchCriteria) {
            if (StringUtils.equals(so.getSearchColumn(), SelectorModel.SEARCH_COL_QUERY_ALL_COUNT)) {
                hqlStr = so.getSearchValueAsString();
            }
        }
        
		Query query = getSession().createQuery(hqlStr);
 
        try {
            Number res = (Number) query.getSingleResult();        
            return res.longValue();
        } catch (Exception ex) {
            logger.error("Error querying search count data lov [" + ex.getMessage() + "]", ex);
            throw new RuntimeException("Error querying search count data lov [" + ex.getMessage() + "]", ex);
        }
    }
    
    @SuppressWarnings("rawtypes")
	@Override
    public List searchDataUsingNative(List <? extends SearchObject> searchCriteria, int first, int pageSize, String sortField, SortOrder sortOrder) {
        String nativeQueryStr = "";
        for (SearchObject so : searchCriteria) {
            if (StringUtils.equals(so.getSearchColumn(), SelectorModel.SEARCH_COL_QUERY_ALL)) {
                nativeQueryStr = so.getSearchValueAsString();
            }
        }        
        
        Boolean isReturnAll = (nativeQueryStr.contains("call")?true:false);
        
        if(isReturnAll){
        	if(nativeQueryStr.contains("wo_sp_get_track_record_mst")){
        		nativeQueryStr = externalRegulationDao.getQueryTrackRecord(new Long(nativeQueryStr.replaceAll("call wo_sp_get_track_record_mst ", "")), "IN");
        	}else{
        		nativeQueryStr = externalRegulationDao.getQueryTrackRecord(new Long(nativeQueryStr.replaceAll("call wo_sp_get_track_record ", "")), "IN");
        	}
        }
       
        Query query = getSession().createNativeQuery(nativeQueryStr);
        
        if (!isReturnAll && first >= 0 && pageSize > 0) {
            query.setFirstResult(first);
            query.setMaxResults(pageSize);            
        } else {
            //won't page anything, return all
        }
        
        try {
            return query.getResultList(); 
        } catch (Exception ex) {
            logger.error("Error querying search data native lov [" + ex.getMessage() + "]", ex);
            throw new RuntimeException("Error querying search data native lov [" + ex.getMessage() + "]", ex);
        }
    }

    @SuppressWarnings("rawtypes")
	@Override
    public Long searchCountDataUsingNative(List <? extends SearchObject> searchCriteria) {
        String nativeQueryStr = "";
        for (SearchObject so : searchCriteria) {
            if (StringUtils.equals(so.getSearchColumn(), SelectorModel.SEARCH_COL_QUERY_ALL_COUNT)) {
                nativeQueryStr = so.getSearchValueAsString();
            }
        }
        
        Query query = getSession().createNativeQuery(nativeQueryStr);
 
        try {
            Number res = (Number) query.getSingleResult();        
            return res.longValue();
        } catch (Exception ex) {
            logger.error("Error querying search count data native lov [" + ex.getMessage() + "]", ex);
            throw new RuntimeException("Error querying search count data native lov [" + ex.getMessage() + "]", ex);
        }
    }

	public ExternalRegulationDao getExternalRegulationDao() {
		return externalRegulationDao;
	}

	public void setExternalRegulationDao(ExternalRegulationDao externalRegulationDao) {
		this.externalRegulationDao = externalRegulationDao;
	}
    
    
    
}
