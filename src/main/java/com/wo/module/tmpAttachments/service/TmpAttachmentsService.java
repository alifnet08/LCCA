/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.tmpAttachments.service;

import java.util.List;

import com.wo.module.tmpAttachments.model.TmpAttachments;

public interface TmpAttachmentsService {
	Object executeQuery(String query) throws Exception;
	void saveUpload(TmpAttachments entity, String user) throws Exception;
	List<Object[]> executeSelectQuery(String query) throws Exception;
	List<String> getColumnNames(String query) throws Exception;
	Integer executeUpdateQuery(String query) throws Exception;
}
