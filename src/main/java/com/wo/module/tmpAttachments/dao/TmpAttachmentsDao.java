package com.wo.module.tmpAttachments.dao;

import java.sql.SQLSyntaxErrorException;
import java.util.List;
import java.util.Map;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.tmpAttachments.model.TmpAttachments;

public interface TmpAttachmentsDao extends  GenericDAO<TmpAttachments, Long> {

	Integer executeUpdate(String query) throws SQLSyntaxErrorException, Exception;
	List<Map<String, Object>> executeSelect(String query) throws SQLSyntaxErrorException, Exception;
	List<String> getColumnNames(String query) throws SQLSyntaxErrorException, Exception;
	List<Object[]> executeSelectQuery(String query) throws SQLSyntaxErrorException, Exception;
}
