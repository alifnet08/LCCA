package com.wo.module.parameter.service;

import java.util.List;

import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.parameter.model.ParameterHeader;

public interface ParameterHeaderService extends RetrieverDataPage<ParameterHeader> {
	
	public void save(ParameterHeader parameterHeader);
	
	public void update(ParameterHeader parameterHeader);
	
	public void delete(ParameterHeader parameterHeader);
	
	public ParameterHeader findById(Long id);
	
	public List<ParameterHeader> getListParameterAll() throws Exception;
	
	public ParameterHeader getParameterHeaderByParamCode(String paramCode) throws Exception;
	
	public List<ParameterHeader> getListParameterAllOrderByName() throws Exception;
	
}