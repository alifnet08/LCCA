package com.wo.module.notary.dao;

import java.util.List;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.notary.model.NotaryDocument;

public interface NotaryDocumentDao extends GenericDAO<NotaryDocument, Long> {

	public List<NotaryDocument> getNotaryDocumentByNotaryId(Long notaryId) throws Exception;

}
