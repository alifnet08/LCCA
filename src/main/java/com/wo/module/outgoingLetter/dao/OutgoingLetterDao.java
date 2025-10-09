package com.wo.module.outgoingLetter.dao;

import java.util.List;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.outgoingLetter.model.OutgoingLetter;

public interface OutgoingLetterDao extends GenericDAO<OutgoingLetter, Long>, 
	RetrieverDataPage<OutgoingLetter>{
	
	@SuppressWarnings("rawtypes")
	public List<OutgoingLetter> searchDataXLS(List<? extends SearchObject> searchCriteria);
	
	public Integer getOutgoingLetterByLetterInAndNo(String letterIn, String letterNo) throws Exception;
	
	public Integer getOutgoingLetterByLetterInAndNo(Long id,String letterIn, String letterNo) throws Exception;
}