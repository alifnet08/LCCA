/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.parameter.service;

import java.util.List;
import java.util.Locale;

import javax.faces.model.SelectItem;

import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.parameter.model.ParameterDetail;

public interface ParameterDetailService extends RetrieverDataPage<ParameterDetail> {
    
	public ParameterDetail getParameterDetailByParamDtlCode(String paramDetailCode) throws Exception;
	public List<ParameterDetail> getParameterDetailByParamCode(String paramCode) throws Exception;
	List<SelectItem> getListLabelValue(String parameterCode, boolean showWithCode, boolean orderByCode, Locale locale)throws Exception;
	
	public Integer getParameterDetailByParamCodeAndParamDtlCode(String paramCode,String paramDtlCode) throws Exception;
	
	public void update(ParameterDetail parameterDetail);
	
	public void save(ParameterDetail parameterDetail);
	
	public void delete(ParameterDetail parameterDetail);
	
	public ParameterDetail findById(Long id);
	
	public List<ParameterDetail> getParameterDetailByParamCodeOrdered(String paramCode) throws Exception;
	
	public List<ParameterDetail> getParameterDetailByTwoParamCode(String paramCode1, String paramCode2) throws Exception;
	
	public List<ParameterDetail> getParameterDetailByParamCodeOrDtlCode(String paramCode, String paramDtlCode) throws Exception;
	
	public List<ParameterDetail> getParameterDetailByParamCodeOrderById(String paramCode) throws Exception;
	
	public ParameterDetail getParameterDetailByParamDtlNameIn(String paramDetailNameIn) throws Exception;
	
	public String getParamDtlNameByParamDtlCode(String parameterDtlCode);
}
