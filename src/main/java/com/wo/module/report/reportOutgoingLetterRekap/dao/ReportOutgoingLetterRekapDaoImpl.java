package com.wo.module.report.reportOutgoingLetterRekap.dao;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.report.reportGen.model.ReportGen;
import com.wo.module.report.reportOutgoingLetterRekap.constant.ReportOutgoingLetterRekapConstants;
import com.wo.module.report.reportOutgoingLetterRekap.model.ReportOutgoingLetterRekap;

@Repository("reportOutgoingLetterRekapDao")
public class ReportOutgoingLetterRekapDaoImpl extends GenericDAOHibernate<ReportGen, Long>
	implements ReportOutgoingLetterRekapDao, ReportOutgoingLetterRekapConstants{

	@SuppressWarnings("rawtypes")
	private StringBuilder getWhereQueryString(StringBuilder sb, List<? extends SearchObject> searchCriteria) {
		
		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString() != null ? searchVal.getSearchValueAsString() : "";
				
				if (!StringUtils.isBlank(val)) {
					if (StringUtils.equals(WHERE_CREATION_DATE_FROM, col)) {
						sb.append(" and TRUNC(ol.creation_date) >= TO_DATE('" + val + "', 'yyyy-MM-dd') ");
					} else if (StringUtils.equals(WHERE_CREATION_DATE_TO, col)) {
						sb.append(" and TRUNC(ol.creation_date) <= TO_DATE('" + val + "', 'yyyy-MM-dd') ");
					} else if (StringUtils.equals(WHERE_LETTER_DATE_FROM, col)) {
						sb.append(" and TRUNC(ol.letter_date) >= TO_DATE('" + val + "', 'yyyy-MM-dd') ");
					} else if (StringUtils.equals(WHERE_LETTER_DATE_TO, col)) {
						sb.append(" and TRUNC(ol.letter_date) <= TO_DATE('" + val + "', 'yyyy-MM-dd') ");
					}
				}
			}
		}
		
		return sb;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public List<ReportOutgoingLetterRekap> getReportOutgoingLetterRekapByTujuanSuratData(
			List<? extends SearchObject> searchCriteria) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select ol.letter_purpose_in "
				+ "		,ol.letter_purpose_en "
				+ "		,COUNT(1) total "
				+ "	from wo_mst_outgoing_letter ol "
				+ "	where 1 = 1 ");
		
		sb = getWhereQueryString(sb, searchCriteria);
		
		sb.append("	group by ol.letter_purpose_in, ol.letter_purpose_en ");
		
		Query query = getSession().createSQLQuery(sb.toString());
		
		List resultList = query.getResultList();
		
		List<ReportOutgoingLetterRekap> vo = new ArrayList<ReportOutgoingLetterRekap>();
		
		if (resultList != null) {
			for (int i = 0; i < resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
				ReportOutgoingLetterRekap data = new ReportOutgoingLetterRekap();
				
				data.setTujuanSuratIn(obj[0] != null ? (String) obj[0] : null);
				data.setTujuanSuratEn(obj[1] != null ? (String) obj[1] : null);
				data.setTotal(obj[2] != null ? (((BigDecimal) obj[2]).toBigInteger()).intValue() : null);
				
				vo.add(data);
			}
		}
		
		return vo;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<Object[]> getReportOutgoingLetterRekapByTujuanSuratObj(List<? extends SearchObject> searchCriteria) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select ol.letter_purpose_in "
				+ "		,ol.letter_purpose_en "
				+ "		,COUNT(1) total "
				+ "	from wo_mst_outgoing_letter ol "
				+ "	where 1 = 1 ");
		
		sb = getWhereQueryString(sb, searchCriteria);
		
		sb.append("	group by ol.letter_purpose_in, ol.letter_purpose_en ");
		
		Query query = getSession().createSQLQuery(sb.toString());
		
		List resultList = query.getResultList();
		
		List<Object[]> vo = new ArrayList<Object[]>();
		
		if (resultList != null) {
			for (int i = 0; i < resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
				
				vo.add(obj);
			}
		}
		
		return vo;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<ReportOutgoingLetterRekap> getReportOutgoingLetterRekapByTanggalSuratData(
			List<? extends SearchObject> searchCriteria) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select ol.letter_date "
				+ "		,COUNT(1) total "
				+ "		,TO_CHAR(ol.letter_date, 'dd-Mon-yyyy') letter_date_str "
				+ "	from wo_mst_outgoing_letter ol "
				+ "	where 1 = 1 ");
		
		sb = getWhereQueryString(sb, searchCriteria);
		
		sb.append("	group by ol.letter_date ");
		
		Query query = getSession().createSQLQuery(sb.toString());
		
		List resultList = query.getResultList();
		
		List<ReportOutgoingLetterRekap> vo = new ArrayList<ReportOutgoingLetterRekap>();
		
		if (resultList != null) {
			for (int i = 0; i < resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
				ReportOutgoingLetterRekap data = new ReportOutgoingLetterRekap();
				
				data.setTanggalSurat(obj[0] != null ? (Date) obj[0] : null);
				data.setTotal(obj[1] != null ? (((BigDecimal) obj[1]).toBigInteger()).intValue() : null);
				data.setTanggalSuratStr(obj[2] != null ? (String) obj[2] : null);
				
				vo.add(data);
			}
		}
		
		return vo;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<Object[]> getReportOutgoingLetterRekapByTanggalSuratObj(List<? extends SearchObject> searchCriteria) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select ol.letter_date "
				+ "		,COUNT(1) total "
				+ "		,TO_CHAR(ol.letter_date, 'dd-Mon-yyyy') letter_date_str "
				+ "	from wo_mst_outgoing_letter ol "
				+ "	where 1 = 1 ");
		
		sb = getWhereQueryString(sb, searchCriteria);
		
		sb.append("	group by ol.letter_date ");
		
		Query query = getSession().createSQLQuery(sb.toString());
		
		List resultList = query.getResultList();
		
		List<Object[]> vo = new ArrayList<Object[]>();
		
		if (resultList != null) {
			for (int i = 0; i < resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
				
				vo.add(obj);
			}
		}
		
		return vo;
	}
}
