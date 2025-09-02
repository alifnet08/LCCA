package com.wo.module.logActivity.service;

import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.logActivity.model.LogActivity;

public interface LogActivityService extends RetrieverDataPage<LogActivity> {

	public void save(LogActivity entity);
	
	public void update(LogActivity entity);

	public void delete(LogActivity entity);

	public LogActivity findById(Long id);
	//test
}
