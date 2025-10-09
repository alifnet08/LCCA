/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.faq.service;

import java.util.List;

import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.faq.model.Faq;
import com.wo.module.faq.model.TmpFaq;

public interface FaqService extends RetrieverDataPage<TmpFaq> {

	public void save(TmpFaq entity);

	public void update(TmpFaq entity);

	public void delete(TmpFaq entity);

	public TmpFaq findById(Long id);
	
	public List<String[]> getInstitution(Long userId);
	
	public List<String[]> getCategoryFAQByInstitution(String institution,String searchVal,String kategori);
	
	public List<Faq> getQuestionAndAnswerByCategory(String category,String searchVal,Long faqId,String institution);
	
}
