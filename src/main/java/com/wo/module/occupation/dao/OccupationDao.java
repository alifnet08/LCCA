package com.wo.module.occupation.dao;

import java.text.ParseException;
import java.util.List;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.occupation.model.Occupation;
import com.wo.module.occupation.vo.OccupationVO;

public interface OccupationDao extends GenericDAO<Occupation, Long>, 
RetrieverDataPage<OccupationVO>{

	List<Occupation> findAll() throws ParseException;

	String getFileName();

	void deleteAll();
	
}
