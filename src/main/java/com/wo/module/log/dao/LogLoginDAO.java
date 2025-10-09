package com.wo.module.log.dao;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.log.model.LogLogin;

public interface LogLoginDAO extends GenericDAO<LogLogin, Long>, RetrieverDataPage<LogLogin> {

}