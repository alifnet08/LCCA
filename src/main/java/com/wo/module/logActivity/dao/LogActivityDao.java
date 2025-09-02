package com.wo.module.logActivity.dao;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.logActivity.model.LogActivity;

public interface LogActivityDao extends GenericDAO<LogActivity, Long>, RetrieverDataPage<LogActivity> {

}
