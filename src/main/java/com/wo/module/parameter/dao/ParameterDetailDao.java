/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.parameter.dao;

import java.util.List;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.parameter.model.ParameterDetail;
/**
 *
 * @author hendra
 */
public interface ParameterDetailDao extends  GenericDAO<ParameterDetail, Long>, RetrieverDataPage<ParameterDetail>{

	public ParameterDetail getParameterDetailByParamDtlCode(String paramDetailCode) throws Exception;
	public List<ParameterDetail> getParameterDetailByParamCode(String paramCode) throws Exception;
	
	public Integer getParameterCodeByParamCodeAndParamDtlCode(String paramCode,String paramDtlCode) throws Exception;
	
	public List<ParameterDetail> getParameterDetailByParamCodeOrdered(String paramCode) throws Exception;
	
	public List<ParameterDetail> getParameterDetailByTwoParamCode(String paramCode1, String paramCode2) throws Exception;
	
	public List<ParameterDetail> getParameterDetailByParamCodeOrDtlCode(String paramCode, String paramDtlCode) throws Exception;
	
	public List<ParameterDetail> getParameterDetailByParamCodeOrderById(String paramCode) throws Exception;
	
	public ParameterDetail getParameterDetailByParamDtlNameIn(String paramDetailNameIn) throws Exception;
	
	public String getParamDtlNameByParamDtlCode(String parameterDtlCode);
	
}
