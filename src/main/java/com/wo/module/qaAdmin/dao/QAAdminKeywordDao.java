package com.wo.module.qaAdmin.dao;

import java.util.List;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.qa.model.QAKeyword;

public interface QAAdminKeywordDao extends GenericDAO<QAKeyword, Long>{

	public List<QAKeyword> getKeyword(Long qnaId, Long qnaKeywordId);
}
