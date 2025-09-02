package com.wo.module.qaAdmin.service;

import com.wo.module.qa.model.QAKeyword;

public interface QAAdminKeywordService {

	public void delete(QAKeyword entity);
	
	public QAKeyword findById(Long qnaKeywordId);
	
}
