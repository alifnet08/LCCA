package com.wo.module.qaFE.service;

import java.util.List;

import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.qa.model.QA;
import com.wo.module.qaFE.vo.QAFEVo;

public interface QAFEService extends RetrieverDataPage<QAFEVo>{

	public void save(QA qa);
	
	public void update(QA qa);
	
	public void delete(QA qa);
	
	public QA findById(Long qaId);
	
	public Number getTicketNo(); 
	
	public Number getIsAdmin(Long userId);
	
	public List<QA> getQAByParentId(Long fromQnaId);
	
	public String getLastTicketNo();
	
	public String getCategoryIsAdmin(Long userId);
	
	public List<QA> getQANotAnswerByParentId(Long fromQnaId);
	
	public Number getIsAdminByCategory(Long userId, String category);
	
	public Number getCheckTiket(String ticketNo);
	
}
