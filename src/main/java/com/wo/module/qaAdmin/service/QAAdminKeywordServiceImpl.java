package com.wo.module.qaAdmin.service;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.qa.model.QAKeyword;
import com.wo.module.qaAdmin.dao.QAAdminKeywordDao;

@Transactional
@Service("qaAdminKeywordService")
public class QAAdminKeywordServiceImpl implements QAAdminKeywordService, Serializable{

	private static final long serialVersionUID = -8229912435875844922L;

	@Autowired
	@Qualifier("qaAdminKeywordDao")
	private QAAdminKeywordDao qaAdminKeywordDao;
	
	@Override
	public void delete(QAKeyword entity) {
		qaAdminKeywordDao.delete(entity);
	}

	@Override
	public QAKeyword findById(Long qnaKeywordId) {
		return qaAdminKeywordDao.getById(qnaKeywordId);
	}
	
	public QAAdminKeywordDao getQaAdminKeywordDao() {
		return qaAdminKeywordDao;
	}

	public void setQaAdminKeywordDao(QAAdminKeywordDao qaAdminKeywordDao) {
		this.qaAdminKeywordDao = qaAdminKeywordDao;
	}


}
