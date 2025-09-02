package com.wo.module.qaFE.dao;

import java.util.List;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.qa.model.QA;
import com.wo.module.qaFE.vo.QAFEVo;

public interface QAFEDao extends GenericDAO<QA, Long>, RetrieverDataPage<QAFEVo>{

	public Number getTicketNo(); 
	public Number getIsAdmin(Long userId);
	public List<QA> getQAByParentId(Long fromQnaId);
	public String getLastTicketNo();
	public String getCategoryIsAdmin(Long userId);
	public List<QA> getQANotAnswerByParentId(Long fromQnaId);
	public Number getIsAdminByCategory(Long userId, String category);	
	public Number getCheckTiket(String ticketNo);
}
