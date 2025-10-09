package com.wo.module.economySector.dao;

import java.text.ParseException;
import java.util.List;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.economySector.model.EconomySector;
import com.wo.module.economySector.vo.EconomySectorVO;

public interface EconomySectorDao extends GenericDAO<EconomySector, Long>, 
RetrieverDataPage<EconomySectorVO>{

	List<EconomySector> findAll() throws ParseException;

	String getFileName();

	void deleteAll();
	
}
