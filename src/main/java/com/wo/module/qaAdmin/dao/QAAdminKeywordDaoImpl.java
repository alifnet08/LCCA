package com.wo.module.qaAdmin.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.common.util.MathUtil;
import com.wo.module.qa.model.QAKeyword;

@Repository("qaAdminKeywordDao")
public class QAAdminKeywordDaoImpl extends GenericDAOHibernate<QAKeyword, Long>
	implements QAAdminKeywordDao, Serializable{

	private static final long serialVersionUID = 1535235850595563171L;

	@SuppressWarnings("rawtypes")
	@Override
	public List<QAKeyword> getKeyword(Long qnaId, Long qnaKeywordId) {
		StringBuilder sb = new StringBuilder();
		
		sb.append(" SELECT QNA_KEYWORD_ID ");
		sb.append("       ,QNA_ID ");
		sb.append("       ,KEYWORD ");
		sb.append(" FROM WO_MST_QNA_KEYWORD ");
		sb.append(" WHERE 1 = 1 ");
		sb.append("     AND ENABLED_FLAG <> 'N' ");
		
		if (qnaId != null && qnaId > 0) {
			sb.append("     AND QNA_ID = :qnaId ");
		}
		if (qnaKeywordId != null && qnaKeywordId > 0) {
			sb.append("     AND QNA_KEYWORD_ID = :qnaKeywordId ");
		}
		
		Query query = getSession().createSQLQuery(sb.toString());
		if (qnaId != null && qnaId > 0) {
			query.setParameter("qnaId", qnaId);
		}
		if (qnaKeywordId != null && qnaKeywordId > 0) {
			query.setParameter("qnaKeywordId", qnaKeywordId);
		}
		
		List result = query.getResultList();
		List<QAKeyword> vo = new ArrayList<QAKeyword>();
		
		if (result != null) {
			for (int i = 0; i < result.size(); i++) {
				Object[] obj = (Object[]) result.get(i);
				QAKeyword data = new QAKeyword();
				
				data.setQnaKeywordId(obj[0] != null ? MathUtil.returnIdObjectToLong(obj[0]) : null);
				data.setQnaId(obj[1] != null ? MathUtil.returnIdObjectToLong(obj[1]) : null);
				data.setKeyword(obj[2] != null ? (String) obj[2] : null);
				
				vo.add(data);
			}
		}
		
		return vo;
	}

}
