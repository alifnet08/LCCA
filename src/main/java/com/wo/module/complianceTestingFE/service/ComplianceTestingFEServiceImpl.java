package com.wo.module.complianceTestingFE.service;

import java.util.List;

import javax.transaction.Transactional;

import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.complianceTestingFE.dao.ComplianceTestingFEDao;
import com.wo.module.complianceTestingFE.vo.ComplianceTestingFEDtlVO;
import com.wo.module.complianceTestingFE.vo.ComplianceTestingFEVO;

@Transactional
@Service("complianceTestingFEService")
public class ComplianceTestingFEServiceImpl implements ComplianceTestingFEService {
	
    @Autowired
    @Qualifier("complianceTestingFEDao")
    private ComplianceTestingFEDao complianceTestingFEDao;
    

	public ComplianceTestingFEDao getComplianceTestingFEDao() {
		return complianceTestingFEDao;
	}

	public void setComplianceTestingFEDao(ComplianceTestingFEDao complianceTestingFEDao) {
		this.complianceTestingFEDao = complianceTestingFEDao;
	}

	@SuppressWarnings("rawtypes")
	@Override    
    public List<ComplianceTestingFEDtlVO> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize, String sortField, SortOrder sortOrder) 
            throws Exception {
        return complianceTestingFEDao.searchData(searchCriteria, first, pageSize, sortField, sortOrder);
    }
    
    @SuppressWarnings("rawtypes")
	@Override    
    public Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception {
        return complianceTestingFEDao.searchCountData(searchCriteria);
    }
	
    
	
	
}
