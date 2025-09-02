/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.documentType.dao;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.documentType.model.DocumentType;
/**
 *
 * @author hendra
 * 
 * Modification Alex
 */
public interface DocumentTypeDao extends  GenericDAO<DocumentType, Long>, RetrieverDataPage<DocumentType>{

	public Integer getDocumentTypeByProvAndType(String provision, String documentType) throws Exception;
	
	public Integer getEditDocumentTypeByIdProvAndType(Long id,String provision, String type) throws Exception;
	
    public Boolean isUsedInTransaction(Long id);
    
    public Long getDocumentTypeIdByProvAndType(String provision, String documentType) throws Exception;
}
