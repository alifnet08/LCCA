/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.parameter.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.faces.model.SelectItem;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.parameter.dao.ParameterDetailDao;
import com.wo.module.parameter.model.ParameterDetail;

@Transactional
@Service("parameterDetailService")
public class ParameterDetailServiceImpl implements ParameterDetailService {
	@Autowired
	@Qualifier("parameterDetailDao")
	private ParameterDetailDao parameterDetailDao;

	public ParameterDetail getParameterDetailByParamDtlCode(String paramDetailCode) throws Exception {
		return parameterDetailDao.getParameterDetailByParamDtlCode(paramDetailCode);
	}

	public List<ParameterDetail> getParameterDetailByParamCode(String paramCode) throws Exception {
		return parameterDetailDao.getParameterDetailByParamCode(paramCode);
	}
	
	@Override
	public List<SelectItem> getListLabelValue(String parameterCode, boolean showWithCode, boolean orderByCode, Locale locale)
			throws Exception {
		List<SelectItem> listSelect = new ArrayList<SelectItem>();
		List<ParameterDetail> paramDetailList = parameterDetailDao.getParameterDetailByParamCode(parameterCode);
		for (ParameterDetail parameterDetail : paramDetailList) {
			listSelect.add(
					new SelectItem(
							parameterDetail.getParameterDtlCode()
							, Locale.ENGLISH.equals(locale) ? 
									showWithCode ? parameterDetail.getParameterDtlCode() + StringUtils.SPACE + parameterDetail.getNameEn() : parameterDetail.getNameEn()
									: showWithCode ? parameterDetail.getParameterDtlCode() + StringUtils.SPACE + parameterDetail.getNameIn() : parameterDetail.getNameIn()
					)
			);
		}
		return listSelect;
	}

	public ParameterDetailDao getParameterDetailDao() {
		return parameterDetailDao;
	}

	public void setParameterDetailDao(ParameterDetailDao parameterDetailDao) {
		this.parameterDetailDao = parameterDetailDao;
	}

	@SuppressWarnings("rawtypes")
    @Transactional(readOnly=true)
    public List<ParameterDetail> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize, String sortField, SortOrder sortOrder) 
            throws Exception {
        return parameterDetailDao.searchData(searchCriteria, first, pageSize, sortField, sortOrder);
    }
    
    @SuppressWarnings("rawtypes")
    @Transactional(readOnly=true)
    public Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception {
        return parameterDetailDao.searchCountData(searchCriteria);
    }
	
	@Override
	public void save(ParameterDetail parameterDetail) {
		parameterDetailDao.save(parameterDetail);
	}
	
	@Override
	public void update(ParameterDetail parameterDetail) {
		parameterDetailDao.update(parameterDetail);
	}
	
	@Override
	public void delete(ParameterDetail parameterDetail) {
		parameterDetailDao.delete(parameterDetail);
	}

	@Override
	public ParameterDetail findById(Long id) {
		return parameterDetailDao.getById(id);
	}

	@Override
	@Transactional(readOnly=true)
	public Integer getParameterDetailByParamCodeAndParamDtlCode(String paramCode, String paramDtlCode)
			throws Exception {
		return parameterDetailDao.getParameterCodeByParamCodeAndParamDtlCode(paramCode, paramDtlCode);
	}

	@Override
	public List<ParameterDetail> getParameterDetailByParamCodeOrdered(String paramCode) throws Exception {
		return parameterDetailDao.getParameterDetailByParamCodeOrdered(paramCode);
	}

	@Override
	public List<ParameterDetail> getParameterDetailByTwoParamCode(String paramCode1, String paramCode2)
			throws Exception {
		return parameterDetailDao.getParameterDetailByTwoParamCode(paramCode1, paramCode2);
	}

	@Override
	public List<ParameterDetail> getParameterDetailByParamCodeOrDtlCode(String paramCode, String paramDtlCode)
			throws Exception {
		return parameterDetailDao.getParameterDetailByParamCodeOrDtlCode(paramCode, paramDtlCode);
	}

	@Override
	public List<ParameterDetail> getParameterDetailByParamCodeOrderById(String paramCode) throws Exception {
		return parameterDetailDao.getParameterDetailByParamCodeOrderById(paramCode);
	}

	@Override
	public ParameterDetail getParameterDetailByParamDtlNameIn(String paramDetailNameIn) throws Exception {
		return parameterDetailDao.getParameterDetailByParamDtlNameIn(paramDetailNameIn);
	}

	@Override
	public String getParamDtlNameByParamDtlCode(String parameterDtlCode) {
		return parameterDetailDao.getParamDtlNameByParamDtlCode(parameterDtlCode);
	}

}
