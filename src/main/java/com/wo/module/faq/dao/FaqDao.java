/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.faq.dao;

import java.util.List;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.faq.model.Faq;
import com.wo.module.faq.model.TmpFaq;

/**
 *
 * @author hendra
 */
public interface FaqDao extends GenericDAO<TmpFaq, Long>, RetrieverDataPage<TmpFaq> {
	public List<String[]> getInstitution(Long userId);
	public List<String[]> getCategoryFAQByInstitution(String institution,String kategori);
	public List<Faq> getQuestionAndAnswerByCategory(String category,String searchVal,Long faqId,String institution);
}
