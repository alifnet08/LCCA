package com.wo.module.outgoingLetter.service;

import java.util.List;

import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.outgoingLetter.model.OutgoingLetter;

public interface OutgoingLetterService extends RetrieverDataPage<OutgoingLetter>{
	
	public void save(OutgoingLetter outgoingLetter);
	
	public void update(OutgoingLetter outgoingLetter);
	
	public void delete(OutgoingLetter outgoingLetter);
	
	public OutgoingLetter findById(Long id);
	
	@SuppressWarnings("rawtypes")
	public List<OutgoingLetter> searchDataXLS(List<? extends SearchObject> searchCriteria);
	
	public Integer getOutgoingLetterByLetterInAndNo(String letterIn, String letterNo) throws Exception;
	
	public Integer getOutgoingLetterByLetterInAndNo(Long id,String letterIn, String letterNo) throws Exception;
}