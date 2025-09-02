package com.wo.module.parameter.dao;

import java.util.List;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.parameter.model.ParameterHeader;

public interface ParameterHeaderDao extends GenericDAO<ParameterHeader, Long>,RetrieverDataPage<ParameterHeader>{
	
	public ParameterHeader getListParameterDataAll() throws Exception;
	public List<ParameterHeader> getListParameterAll() throws Exception;
	
	public ParameterHeader getParameterHeaderByParamCode(String paramCode) throws Exception;
	
	public List<ParameterHeader> getListParameterAllOrderByName() throws Exception;
}