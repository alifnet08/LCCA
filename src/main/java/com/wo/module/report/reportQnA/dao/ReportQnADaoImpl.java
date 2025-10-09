package com.wo.module.report.reportQnA.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.common.util.MathUtil;
import com.wo.module.report.reportGen.model.ReportGen;
import com.wo.module.report.reportQnA.constant.ReportQnAConstant;
import com.wo.module.report.reportQnA.vo.ReportQnADetail;
import com.wo.module.report.reportQnA.vo.ReportQnARekap;

@Repository("reportQnADao")
public class ReportQnADaoImpl extends GenericDAOHibernate<ReportGen, Long> implements Serializable, ReportQnADao{

	private static final long serialVersionUID = 6239904470282843083L;

	@SuppressWarnings("rawtypes")
	private void setQueryWhereStringDetail(StringBuilder sb, List<? extends SearchObject> searchCriteria) {
		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString();
				
				if (StringUtils.isNotBlank(val)) {
					if (StringUtils.equals(ReportQnAConstant.SEARCH_BY_QUESTION_DATE_FROM, col)) {
						sb.append(" 	AND TRUNC(n.Q_DATE) >= TO_DATE(:questionDateFrom, 'yyyy-MM-dd') ");
					}
					if (StringUtils.equals(ReportQnAConstant.SEARCH_BY_QUESTION_DATE_TO, col)) {
						sb.append(" 	AND TRUNC(n.Q_DATE) <= TO_DATE(:questionDateTo, 'yyyy-MM-dd') ");
					}
					if (StringUtils.equals(ReportQnAConstant.SEARCH_BY_CATEGORY, col)) {
						sb.append(" 	AND n.CATEGORY_TYPE = :categoryType ");
					}
				}
			}
		}
	}
	
	@SuppressWarnings("rawtypes")
	private void setQueryWhereStringRekap(StringBuilder sb, List<? extends SearchObject> searchCriteria) {
		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString();
				
				if (StringUtils.isNotBlank(val)) {
					if (StringUtils.equals(ReportQnAConstant.SEARCH_BY_QUESTION_DATE_FROM, col)) {
						sb.append(" AND TRUNC(n.Q_DATE) >= TO_DATE(:questionDateFrom, 'yyyy-MM-dd') ");
					}
					if (StringUtils.equals(ReportQnAConstant.SEARCH_BY_QUESTION_DATE_TO, col)) {
						sb.append(" AND TRUNC(n.Q_DATE) <= TO_DATE(:questionDateTo, 'yyyy-MM-dd') ");
					}
					if (StringUtils.equals(ReportQnAConstant.SEARCH_BY_CATEGORY, col)) {
						sb.append(" AND n.CATEGORY_TYPE = :categoryType ");
					}
				}
			}
		}
	}
	
	@SuppressWarnings("rawtypes")
	private void getQueryWhereStringDetail(Query query, List<? extends SearchObject> searchCriteria) {
		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString();
				
				if (StringUtils.isNotBlank(val)) {
					if (StringUtils.equals(ReportQnAConstant.SEARCH_BY_QUESTION_DATE_FROM, col)) {
						query.setParameter("questionDateFrom", val);
					}
					if (StringUtils.equals(ReportQnAConstant.SEARCH_BY_QUESTION_DATE_TO, col)) {
						query.setParameter("questionDateTo", val);
					}
					if (StringUtils.equals(ReportQnAConstant.SEARCH_BY_CATEGORY, col)) {
						query.setParameter("categoryType", val);
					}
				}
			}
		}
	}
	
	@SuppressWarnings("rawtypes")
	private void getQueryWhereStringRekap(Query query, List<? extends SearchObject> searchCriteria) {
		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString();
				
				if (StringUtils.isNotBlank(val)) {
					if (StringUtils.equals(ReportQnAConstant.SEARCH_BY_QUESTION_DATE_FROM, col)) {
						query.setParameter("questionDateFrom", val);
					}
					if (StringUtils.equals(ReportQnAConstant.SEARCH_BY_QUESTION_DATE_TO, col)) {
						query.setParameter("questionDateTo", val);
					}
					if (StringUtils.equals(ReportQnAConstant.SEARCH_BY_CATEGORY, col)) {
						query.setParameter("categoryType", val);
					}
				}
			}
		}
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public List<ReportQnADetail> getReportQnADetailAsVo(List<? extends SearchObject> searchCriteria) {
		StringBuilder sb = new StringBuilder();
		
		sb.append(" SELECT n.TICKET_NO ");
		sb.append(" 	,q.NAME PENANYA ");
		sb.append(" 	,n.Q_DATE TGL_TANYA ");
		sb.append(" 	,TO_CHAR(n.Q_DATE, 'DD-MON-YYYY') TGL_TANYA_STR ");
		sb.append(" 	,c.NAME_IN KATEGORI ");
		sb.append(" 	,n.Q_TITLE JUDUL ");
		sb.append(" 	,n.QUESTION ");
		sb.append(" 	,a.NAME PENJAWAB1 ");
		sb.append(" 	,n.A_DATE TGL_JAWAB1 ");
		sb.append(" 	,TO_CHAR(n.A_DATE, 'DD-MON-YYYY') TGL_JAWAB1_STR ");
		sb.append(" 	,n.ANSWER JAWABAN1 ");
		sb.append(" 	,a2.NAME PENJAWAB2 ");
		sb.append(" 	,n.A_DATE_2 TGL_JAWAB2 ");
		sb.append(" 	,TO_CHAR(n.A_DATE_2, 'DD-MON-YYYY') TGL_JAWAB2_STR ");
		sb.append(" 	,n.ANSWER_2 JAWABAN2 ");
		sb.append(" 	,a3.NAME PENJAWAB3 ");
		sb.append(" 	,n.A_DATE_3 TGL_JAWAB3 ");
		sb.append(" 	,TO_CHAR(n.A_DATE_3, 'DD-MON-YYYY') TGL_JAWAB3_STR ");
		sb.append(" 	,n.ANSWER_3 JAWABAN3 ");
		sb.append(" 	,s.NAME_IN STATUS ");
		sb.append(" 	,n.READ_FLAG SUDAH_DIBACA ");
		sb.append(" 	,CASE WHEN n.A_DATE IS NOT NULL THEN ");
		sb.append(" 			CASE WHEN n.A_DATE <= n.Q_DATE + 1 THEN 'Meet SLA' ");
		sb.append(" 				WHEN n.A_DATE > n.Q_DATE + 1 THEN 'Over SLA' ");
		sb.append(" 			END ");
		sb.append(" 		ELSE CASE WHEN SYSDATE > n.Q_DATE + 1 THEN 'Overdue' ");
		sb.append(" 				ELSE 'Before SLA' ");
		sb.append(" 				END ");
		sb.append(" 	 END AS sla ");
		sb.append(" 	,n.CATEGORY_TYPE ");
		sb.append(" FROM WO_MST_QNA n ");
		sb.append(" 	LEFT JOIN WO_MST_USER q ON q.USER_ID = n.Q_USER_ID ");
		sb.append(" 	LEFT JOIN WO_MST_PARAMETER_DTL c ON c.PARAMETER_DTL_CODE = n.CATEGORY_TYPE ");
		sb.append(" 		AND c.PARAMETER_CODE = 'QNA_CATEGORY' ");
		sb.append(" 	LEFT JOIN WO_MST_USER a ON a.USER_ID = n.A_USER_ID ");
		sb.append(" 	LEFT JOIN WO_MST_USER a2 ON a2.USER_ID = n.A_USER_ID_2 ");
		sb.append(" 	LEFT JOIN WO_MST_USER a3 ON a3.USER_ID = n.A_USER_ID_3 ");
		sb.append(" 	LEFT JOIN WO_MST_PARAMETER_DTL s ON s.PARAMETER_DTL_CODE = n.Q_STATUS ");
		sb.append(" 		AND s.PARAMETER_CODE = 'QNA_STATUS' ");
		sb.append(" WHERE 1 = 1 ");
		
		this.setQueryWhereStringDetail(sb, searchCriteria);
		sb.append(" ORDER BY n.TICKET_NO DESC ");
		
		Query query = getSession().createSQLQuery(sb.toString());
		this.getQueryWhereStringDetail(query, searchCriteria);
		
		List result = query.getResultList();
		List<ReportQnADetail> vo = new ArrayList<ReportQnADetail>();
		
		if (result != null) {
			for (int i = 0; i < result.size(); i++) {
				Object[] obj = (Object[]) result.get(i);
				ReportQnADetail data = new ReportQnADetail();
				
				data.setTicketNo(obj[0] != null ? (String) obj[0] : null);
				data.setPenanya(obj[1] != null ? (String) obj[1] : null);
				data.setTanggalTanya(obj[2] != null ? (Date) obj[2] : null);
				data.setTanggalTanyaStr(obj[3] != null ? (String) obj[3] : null);
				data.setKategori(obj[4] != null ? (String) obj[4] : null);
				data.setJudul(obj[5] != null ? (String) obj[5] : null);
				data.setQuestion(obj[6] != null ? (String) obj[6] : null);
				data.setPenjawab1(obj[7] != null ? (String) obj[7] : null);
				data.setTanggalJawab1(obj[8] != null ? (Date) obj[8] : null);
				data.setTanggalJawab1Str(obj[9] != null ? (String) obj[9] : null);
				data.setJawaban1(obj[10] != null ? (String) obj[10] : null);
				data.setPenjawab2(obj[11] != null ? (String) obj[11] : null);
				data.setTanggalJawab2(obj[12] != null ? (Date) obj[12] : null);
				data.setTanggalJawab2Str(obj[13] != null ? (String) obj[13] : null);
				data.setJawaban2(obj[14] != null ? (String) obj[14] : null);
				data.setPenjawab3(obj[15] != null ? (String) obj[15] : null);
				data.setTanggalJawab3(obj[16] != null ? (Date) obj[16] : null);
				data.setTanggalJawab3Str(obj[17] != null ? (String) obj[17] : null);
				data.setJawaban3(obj[18] != null ? (String) obj[18] : null);
				data.setStatus(obj[19] != null ? (String) obj[19] : null);
				data.setSudahDibaca(obj[20] != null ? (String) obj[20] : null);
				data.setSLA(obj[21] != null ? (String) obj[21] : null);
				
				vo.add(data);
			}
		}
		
		return vo;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<ReportQnARekap> getReportQnARekapAsVo(List<? extends SearchObject> searchCriteria) {
		StringBuilder sb = new StringBuilder();
		
		sb.append(" SELECT c.NAME_IN KATEGORI_IN ");
		sb.append(" 	  ,c.NAME_EN KATEGORI_EN ");
		sb.append(" 	  ,COUNT ( ");
		sb.append(" 		CASE ");
		sb.append(" 			WHEN n.A_DATE IS NOT NULL AND n.A_DATE <= n.Q_DATE + 1 THEN 1 ");
		sb.append(" 			ELSE 0 ");
		sb.append(" 		END ) MEET_SLA ");
		sb.append(" 	  ,COUNT ( ");
		sb.append(" 		CASE ");
		sb.append(" 			WHEN n.A_DATE IS NOT NULL AND n.A_DATE > n.Q_DATE + 1 THEN 1 ");
		sb.append(" 			ELSE 0 ");
		sb.append(" 		END ) OVER_SLA ");
		sb.append(" 	  ,COUNT ( ");
		sb.append(" 		CASE ");
		sb.append(" 			WHEN n.A_DATE IS NULL AND SYSDATE > n.Q_DATE + 1 THEN 1 ");
		sb.append(" 			ELSE 0 ");
		sb.append(" 		END ) OVERDUE ");
		sb.append(" 	  ,COUNT ( ");
		sb.append(" 		CASE ");
		sb.append(" 			WHEN n.A_DATE IS NULL AND SYSDATE > n.Q_DATE + 1 THEN 1 ");
		sb.append(" 			ELSE 0 ");
		sb.append(" 		END ) BEFORE_SLA ");
		sb.append(" FROM WO_MST_QNA n ");
		sb.append(" 	LEFT JOIN WO_MST_PARAMETER_DTL c ");
		sb.append(" 		ON c.PARAMETER_DTL_CODE = n.CATEGORY_TYPE ");
		sb.append(" 			AND c.PARAMETER_CODE = 'QNA_CATEGORY' ");
		sb.append(" WHERE 1 = 1 ");
		
		this.setQueryWhereStringRekap(sb, searchCriteria);
		sb.append(" GROUP BY c.NAME_IN, c.NAME_EN ");
		
		
		Query query = getSession().createSQLQuery(sb.toString());
		this.getQueryWhereStringRekap(query, searchCriteria);
		
		List result = query.getResultList();
		List<ReportQnARekap> vo = new ArrayList<ReportQnARekap>();
		
		if (result != null) {
			for (int i = 0; i < result.size(); i++) {
				Object[] obj = (Object[]) result.get(i);
				ReportQnARekap data = new ReportQnARekap();
				
				data.setKategoriIn(obj[0] != null ? (String) obj[0] : null);
				data.setKategoriEn(obj[1] != null ? (String) obj[1] : null);
				data.setMeetSla(obj[2] != null ? MathUtil.returnIdObjectToLong(obj[2]) : null);
				data.setOverSla(obj[3] != null ? MathUtil.returnIdObjectToLong(obj[3]) : null);
				data.setOverDue(obj[4] != null ? MathUtil.returnIdObjectToLong(obj[4]) : null);
				data.setBeforeSla(obj[5] != null ? MathUtil.returnIdObjectToLong(obj[5]) : null);
				
				vo.add(data);
			}
		}
		
		return vo;
	}

}
