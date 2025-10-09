/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.documentCategory.service;

import java.util.List;

import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.documentCategory.dao.DocumentCategoryDao;
import com.wo.module.documentCategory.model.DocumentCategory;

@Transactional
@Service("documentCategoryService")
public class DocumentCategoryServiceImpl implements DocumentCategoryService {
    @Autowired
    @Qualifier("documentCategoryDao")
    private DocumentCategoryDao documentCategoryDao;

	public DocumentCategoryDao getDocumentCategoryDao() {
		return documentCategoryDao;
	}

	public void setDocumentCategoryDao(DocumentCategoryDao documentCategoryDao) {
		this.documentCategoryDao = documentCategoryDao;
	}
    
	@SuppressWarnings("rawtypes")
	@Override
    @Transactional(readOnly=true)
    public List<DocumentCategory> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize, String sortField, SortOrder sortOrder) 
            throws Exception {
        return documentCategoryDao.searchData(searchCriteria, first, pageSize, sortField, sortOrder);
    }
    
    @SuppressWarnings("rawtypes")
	@Override
    @Transactional(readOnly=true)
    public Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception {
        return documentCategoryDao.searchCountData(searchCriteria);
    }

	public void save(DocumentCategory documentCategory) {
		documentCategoryDao.save(documentCategory);
	}
	
	public void update(DocumentCategory documentCategory) {
		documentCategoryDao.update(documentCategory);
	}
	
	public void delete(DocumentCategory documentCategory) {
		documentCategoryDao.delete(documentCategory);
	}
  
    public DocumentCategory findById(Long id) {
    	return documentCategoryDao.getById(id);
    }

	@Override
	@Transactional(readOnly=true)
	public Integer getDocumentCategoryByProvAndCategory(String provision, String categoryIn) throws Exception {
		return documentCategoryDao.getDocumentCategoryByProvAndCategory(provision, categoryIn);
	}

	@Override
	public Integer getEditDocumentCategoryByIdProvAndCategory(Long id, String provision, String category)
			throws Exception {
		return documentCategoryDao.getEditDocumentCategoryByProvAndCategory(id,provision, category);
	}

	@Override
	public List<DocumentCategory> getDocumentCategoryByJenisKetentuan(String jenisKetentuan) throws Exception {
		return documentCategoryDao.getDocumentTopicByJenisKetentuan(jenisKetentuan);
	}

	@Override
	public DocumentCategory getDocumentCategoryBySameValue(String provision, String categoryIn) throws Exception {
		return documentCategoryDao.getDocumentCategoryBySameValue(provision, categoryIn);
	}
	
	@Override
	public Boolean isUsedInTransaction(Long id) {
		return documentCategoryDao.isUsedInTransaction(id);
	}
        
}
