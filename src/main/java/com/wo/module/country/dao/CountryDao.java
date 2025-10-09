package com.wo.module.country.dao;

import java.text.ParseException;
import java.util.List;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.country.model.Country;
import com.wo.module.country.vo.CountryVO;

public interface CountryDao extends GenericDAO<Country, Long>, 
RetrieverDataPage<CountryVO>{

	List<Country> findAll() throws ParseException;

	String getFileName();

	void deleteAll();
	
}
