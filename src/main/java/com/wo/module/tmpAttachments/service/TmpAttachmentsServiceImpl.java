/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.tmpAttachments.service;

import java.sql.SQLSyntaxErrorException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.common.constant.Constants;
import com.wo.module.tmpAttachments.dao.TmpAttachmentsDao;
import com.wo.module.tmpAttachments.model.TmpAttachments;

@Transactional
@Service("tmpAttachmentsService")
public class TmpAttachmentsServiceImpl implements TmpAttachmentsService {
	
	@Autowired
	@Qualifier("tmpAttachmentsDao")
	private TmpAttachmentsDao tmpAttachmentsDao;

	@Override
	public void saveUpload(TmpAttachments entity, String user) throws Exception {
		entity.setCreatedBy(user);
		entity.setCreationDate(new Timestamp(new Date().getTime()));
		entity.setDelId(new Long(0));
		entity.setEnabledFlag(Constants.CONSTANT_YES);
		tmpAttachmentsDao.save(entity);
	}

	@SuppressWarnings("unused")
	@Override
	public Object executeQuery(String query) throws SQLSyntaxErrorException, Exception{
		Object res = new Object();
		if(StringUtils.isNotBlank(query)
				&& query.toLowerCase().contains(Constants.QUERY_SELECT.toLowerCase())) {
			List<String> columnNames = tmpAttachmentsDao.getColumnNames(query);
			List<Object[]> datas = tmpAttachmentsDao.executeSelectQuery(query);
			
			//res= tmpAttachmentsDao.executeSelect(query);
		}else if (StringUtils.isNotBlank(query)
				&& query.toLowerCase().contains(Constants.QUERY_UPDATE.toLowerCase())){
			res= tmpAttachmentsDao.executeUpdate(query);
		}
		
		return res;
	}
	
	@Override
	public Integer executeUpdateQuery(String query) throws SQLSyntaxErrorException, Exception{
		return tmpAttachmentsDao.executeUpdate(query);
	}

	@Override
	public List<Object[]> executeSelectQuery(String query) throws SQLSyntaxErrorException, Exception{
		return tmpAttachmentsDao.executeSelectQuery(query);
	}
	
	@Override
	public List<String> getColumnNames(String query) throws SQLSyntaxErrorException, Exception{
		return tmpAttachmentsDao.getColumnNames(query);
	}
}
