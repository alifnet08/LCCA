/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.lov.service;

import java.util.List;
import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.lov.dao.SelectorDao;

/**
 *
 * @author hendra
 */
@Service("selectorNativeService")
public class SelectorNativeServiceImpl  implements SelectorNativeService {
    @Autowired
    @Qualifier("selectorDao")
    private SelectorDao selectorDao;
    

    
    @SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
    @Transactional(readOnly=true)
    public List searchData(List searchCriteria, int first, int pageSize, String sortField, SortOrder sortOrder) throws Exception {
        return selectorDao.searchDataUsingNative(searchCriteria, first, pageSize, sortField, sortOrder);
    }

    @SuppressWarnings("unchecked")
	@Override
    @Transactional(readOnly=true)
    public Long searchCountData(@SuppressWarnings("rawtypes") List  searchCriteria) throws Exception {
        return selectorDao.searchCountDataUsingNative(searchCriteria);
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
   	@Override
       @Transactional(readOnly=true)
       public List searchDataRegulation(List searchCriteria, int first, int pageSize, String sortField, SortOrder sortOrder) throws Exception {
           return selectorDao.searchData(searchCriteria, first, pageSize, sortField, sortOrder);
       }

    @SuppressWarnings("unchecked")
   	@Override
       @Transactional(readOnly=true)
       public Long searchCountDataRegulation(@SuppressWarnings("rawtypes") List  searchCriteria) throws Exception {
           return selectorDao.searchCountData(searchCriteria);
       }
    
    public void setSelectorDao(SelectorDao selectorDao) {
        this.selectorDao = selectorDao;
    }
    
}
