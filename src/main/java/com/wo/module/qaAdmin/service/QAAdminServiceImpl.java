/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.qaAdmin.service;

import java.util.List;

import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.qa.model.QA;
import com.wo.module.qa.model.QAKeyword;
import com.wo.module.qaAdmin.dao.QAAdminDao;
import com.wo.module.qaAdmin.dao.QAAdminKeywordDao;
import com.wo.module.qaAdmin.vo.QAAdminVo;

@Transactional
@Service("qaAdminService")
public class QAAdminServiceImpl implements QAAdminService {
    @Autowired
    @Qualifier("qaAdminDao")
    private QAAdminDao qaAdminDao;

    @Autowired
    @Qualifier("qaAdminKeywordDao")
    private QAAdminKeywordDao qaAdminKeywordDao;;
    
	@SuppressWarnings("rawtypes")
	@Override
    @Transactional(readOnly=true)
    public List<QAAdminVo> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize, String sortField, SortOrder sortOrder) 
            throws Exception {
        return qaAdminDao.searchData(searchCriteria, first, pageSize, sortField, sortOrder);
    }
    
    @SuppressWarnings("rawtypes")
	@Override
    @Transactional(readOnly=true)
    public Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception {
        return qaAdminDao.searchCountData(searchCriteria);
    }
    
    @SuppressWarnings("rawtypes")
	public List<QA> searchDataXls(List<? extends SearchObject> searchCriteria){
    	return qaAdminDao.searchDataXls(searchCriteria);
    }

	public void save(QA qa) {
		qaAdminDao.save(qa);
	}
	
	public void update(QA qa) {
		qaAdminDao.update(qa);
	}
	
	public void delete(QA qa) {
		qaAdminDao.delete(qa);
	}
  
    public QA findById(Long id) {
    	return qaAdminDao.getById(id);
    }

	@Override
	public List<QAKeyword> getKeyword(Long qnaId, Long qnaKeywordId) {
		return qaAdminKeywordDao.getKeyword(qnaId, qnaKeywordId);
	}

	public QAAdminKeywordDao getQaAdminKeywordDao() {
		return qaAdminKeywordDao;
	}

	public void setQaAdminKeywordDao(QAAdminKeywordDao qaAdminKeywordDao) {
		this.qaAdminKeywordDao = qaAdminKeywordDao;
	}

}
