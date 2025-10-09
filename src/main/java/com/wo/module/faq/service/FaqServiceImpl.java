/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.faq.service;

import java.util.List;

import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.faq.dao.FaqDao;
import com.wo.module.faq.model.Faq;
import com.wo.module.faq.model.TmpFaq;
import com.wo.module.common.paging.SearchObject;

@Transactional
@Service("faqService")
public class FaqServiceImpl implements FaqService {
    @Autowired
    @Qualifier("faqDao")
    private FaqDao faqDao;
    
	public FaqDao getFaqDao() {
		return faqDao;
	}

	public void setFaqDao(FaqDao faqDao) {
		this.faqDao = faqDao;
	}

	@SuppressWarnings("rawtypes")
	@Override
    @Transactional(readOnly=true)
	public List<TmpFaq> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize, String sortField, SortOrder sortOrder)
			throws Exception {
		return faqDao.searchData(searchCriteria, first, pageSize, sortField, sortOrder);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
    @Transactional(readOnly=true)
	public Long searchCountData(List searchCriteria) throws Exception {
		return faqDao.searchCountData(searchCriteria);
	}
	
	public void save(TmpFaq entity) {
		faqDao.save(entity);
	}
	
	public void update(TmpFaq entity) {
		faqDao.update(entity);
	}
	
	public void delete(TmpFaq entity) {
		faqDao.delete(entity);
	}
  
    public TmpFaq findById(Long id) {
    	return faqDao.getById(id);
    }
    
    public List<String[]> getInstitution(Long userId){
    	return faqDao.getInstitution(userId);
    }
	public List<String[]> getCategoryFAQByInstitution(String institution,String searchVal,String kategori){
		return faqDao.getCategoryFAQByInstitution(institution,kategori);
	}
    
	public List<Faq> getQuestionAndAnswerByCategory(String category,String searchVal,Long faqId,String institution){
		return faqDao.getQuestionAndAnswerByCategory(category,searchVal,faqId,institution);
	}
   
        
}
