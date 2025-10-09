/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.counterType.service;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.counterType.dao.CounterTypeDao;
import com.wo.module.counterType.model.CounterType;
import com.wo.module.counterType.model.CounterTypeDtl;

@Transactional
@Service("counterTypeService")
public class CounterTypeServiceImpl implements CounterTypeService {
    @Autowired
    @Qualifier("counterTypeDao")
    private CounterTypeDao counterTypeDao;
    

	public CounterTypeDao getCounterTypeDao() {
		return counterTypeDao;
	}

	public void setCounterTypeDao(CounterTypeDao counterTypeDao) {
		this.counterTypeDao = counterTypeDao;
	}

	@SuppressWarnings("rawtypes")
	@Override
    @Transactional(readOnly=true)
	public List<CounterType> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize, String sortField, SortOrder sortOrder)
			throws Exception {
		return counterTypeDao.searchData(searchCriteria, first, pageSize, sortField, sortOrder);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
    @Transactional(readOnly=true)
	public Long searchCountData(List searchCriteria) throws Exception {
		return counterTypeDao.searchCountData(searchCriteria);
	}
	
	public void save(CounterType entity) {
		counterTypeDao.save(entity);
	}
	
	public void update(CounterType entity) {
		counterTypeDao.update(entity);
	}
	
	public void delete(CounterType entity) {
		counterTypeDao.delete(entity);
	}
  
    public CounterType findById(Long id) {
    	return counterTypeDao.getById(id);
    }
    
    public List<CounterType> getAllCounterType() throws Exception {
    	return counterTypeDao.getAllCounterType();
    }
    
    @Override
    public List<SelectItem> getAllCounterTypeLabelValue() throws Exception{
    	List<CounterType> list2 = counterTypeDao.getAllCounterType();
    	List<SelectItem> counterTypeList = new ArrayList<SelectItem>();
		for (CounterType vo : list2) {
			SelectItem si = new SelectItem();
			String nameDetail = "";
			int i = 1;
	
			for (CounterTypeDtl dtl : vo.getDetails()) {
				
				if (vo.getDetails().size() == i) {
					nameDetail = nameDetail + " H" + dtl.getSlaType() + dtl.getSla() + " ";
				} else {
					nameDetail = nameDetail + " H" + dtl.getSlaType() + dtl.getSla() + ",";
				}

				i++;
			}

			si.setLabel(vo.getCounterTypeName() + " (" + nameDetail + ")");
			si.setValue(vo.getCounterTypeId());
			
			counterTypeList.add(si);
		}
		
		return counterTypeList;
    }
    
    @Override
    public Boolean isDataDuplicate(CounterType entity) {
    	return counterTypeDao.isDataDuplicate(entity);
    }
    
    @Override
    public Boolean isUsedInTransaction(Long id) {
    	return counterTypeDao.isUsedInTransaction(id);
    }
        
}
