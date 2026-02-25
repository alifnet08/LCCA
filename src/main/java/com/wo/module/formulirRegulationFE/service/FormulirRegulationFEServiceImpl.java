package com.wo.module.formulirRegulationFE.service;

import java.util.List;

import javax.faces.model.SelectItem;
import javax.transaction.Transactional;

import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.formulirRegulationFE.dao.FormulirRegulationFEDao;
import com.wo.module.formulirRegulationFE.vo.FormulirRegulationFEVO;

@Transactional
@Service("formulirRegulationFEService")
public class FormulirRegulationFEServiceImpl implements FormulirRegulationFEService {
	
    @Autowired
    @Qualifier("formulirRegulationFEDao")
    private FormulirRegulationFEDao formulirRegulationFEDao;
    	

	public FormulirRegulationFEDao getFormulirRegulationFEDao() {
		return formulirRegulationFEDao;
	}

	public void setFormulirRegulationFEDao(FormulirRegulationFEDao formulirRegulationFEDao) {
		this.formulirRegulationFEDao = formulirRegulationFEDao;
	}

	@SuppressWarnings("rawtypes")
	@Override    
    public List<FormulirRegulationFEVO> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize, String sortField, SortOrder sortOrder) 
            throws Exception {
        return formulirRegulationFEDao.searchData(searchCriteria, first, pageSize, sortField, sortOrder);
    }
    
    @SuppressWarnings("rawtypes")
	@Override    
    public Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception {
        return formulirRegulationFEDao.searchCountData(searchCriteria);
    }

	@Override
	public List<SelectItem> getDataDirectorateList() {
		return formulirRegulationFEDao.getDataDirectorateList();
	}
	
    
	
	
}
