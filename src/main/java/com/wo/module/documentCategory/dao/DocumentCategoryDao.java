/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.documentCategory.dao;

import java.util.List;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.documentCategory.model.DocumentCategory;
/**
 *
 * @author hendra
 * 
 * Modification Alex
 */
public interface DocumentCategoryDao extends  GenericDAO<DocumentCategory, Long>, RetrieverDataPage<DocumentCategory>{

	public Integer getDocumentCategoryByProvAndCategory(String provision, String categoryIn) throws Exception;
	
	public Integer getEditDocumentCategoryByProvAndCategory(Long id, String provision, String category) throws Exception;
	
	public List<DocumentCategory> getDocumentTopicByJenisKetentuan(String jenisKetentuan) throws Exception;
	
	public DocumentCategory getDocumentCategoryBySameValue(String provision, String categoryIn) throws Exception;
	
	public Boolean isUsedInTransaction(Long id);
}
