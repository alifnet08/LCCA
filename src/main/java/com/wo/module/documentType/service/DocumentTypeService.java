/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.documentType.service;

import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.documentType.model.DocumentType;

public interface DocumentTypeService extends RetrieverDataPage<DocumentType> {
    
	public void save(DocumentType documentType); 
	
	public void update(DocumentType documentType);
	
	public void delete(DocumentType documentType);
  
    public DocumentType findById(Long id) ;
    
    public Integer getDocumentTypeByProvAndType(String provision, String documentType) throws Exception;
    
    public Integer getEditDocumentTypeByIdProvAndType(Long id, String provision, String type) throws Exception;
    
    public Boolean isUsedInTransaction(Long id);
    
    public Long getDocumentTypeIdByProvAndType(String provision, String documentType) throws Exception;
}
