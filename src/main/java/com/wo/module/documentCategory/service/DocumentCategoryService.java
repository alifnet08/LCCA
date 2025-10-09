/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.documentCategory.service;

import java.util.List;

import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.documentCategory.model.DocumentCategory;

public interface DocumentCategoryService extends RetrieverDataPage<DocumentCategory> {
    
	public void save(DocumentCategory documentCategory); 
	
	public void update(DocumentCategory documentCategory);
	
	public void delete(DocumentCategory documentCategory);
  
    public DocumentCategory findById(Long id) ;
    
    public Integer getDocumentCategoryByProvAndCategory(String provision, String categoryIn) throws Exception;
    
    public Integer getEditDocumentCategoryByIdProvAndCategory(Long id, String provision, String category) throws Exception;

    public List<DocumentCategory> getDocumentCategoryByJenisKetentuan(String jenisKetentuan) throws Exception;
    
    public DocumentCategory getDocumentCategoryBySameValue(String provision, String categoryIn) throws Exception;
    
    public Boolean isUsedInTransaction(Long id);
}
