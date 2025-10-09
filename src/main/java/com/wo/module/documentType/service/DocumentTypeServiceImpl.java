/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.documentType.service;

import java.util.List;

import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.documentType.dao.DocumentTypeDao;
import com.wo.module.documentType.model.DocumentType;

@Transactional
@Service("documentTypeService")
public class DocumentTypeServiceImpl implements DocumentTypeService {
    @Autowired
    @Qualifier("documentTypeDao")
    private DocumentTypeDao documentTypeDao;

	public DocumentTypeDao getDocumentTypeDao() {
		return documentTypeDao;
	}

	public void setDocumentTypeDao(DocumentTypeDao documentTypeDao) {
		this.documentTypeDao = documentTypeDao;
	}
    
	@SuppressWarnings("rawtypes")
	@Override
    @Transactional(readOnly=true)
    public List<DocumentType> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize, String sortField, SortOrder sortOrder) 
            throws Exception {
        return documentTypeDao.searchData(searchCriteria, first, pageSize, sortField, sortOrder);
    }
    
    @SuppressWarnings("rawtypes")
	@Override
    @Transactional(readOnly=true)
    public Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception {
        return documentTypeDao.searchCountData(searchCriteria);
    }

	public void save(DocumentType documentType) {
		documentTypeDao.save(documentType);
	}
	
	public void update(DocumentType documentType) {
		documentTypeDao.update(documentType);
	}
	
	public void delete(DocumentType documentType) {
		documentTypeDao.delete(documentType);
	}
  
    public DocumentType findById(Long id) {
    	return documentTypeDao.getById(id);
    }

	@Override
	public Integer getDocumentTypeByProvAndType(String provision, String documentType) throws Exception{
		return documentTypeDao.getDocumentTypeByProvAndType(provision, documentType);
	}

	@Override
	public Integer getEditDocumentTypeByIdProvAndType(Long id, String provision, String type) throws Exception {
		return documentTypeDao.getEditDocumentTypeByIdProvAndType(id, provision, type);
	}
        
	@Override
    public Boolean isUsedInTransaction(Long id) {
    	return documentTypeDao.isUsedInTransaction(id);
    }
	
	 public Long getDocumentTypeIdByProvAndType(String provision, String documentType) throws Exception{
		 return documentTypeDao.getDocumentTypeIdByProvAndType(provision, documentType);
	 }
}
