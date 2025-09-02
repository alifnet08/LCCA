package com.wo.module.report.reportComplianceDocumentRekap.dao;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.report.reportComplianceDocumentRekap.constant.ReportComplianceDocumentRekapConstants;
import com.wo.module.report.reportComplianceDocumentRekap.model.ReportComplianceDocumentRekap;
import com.wo.module.report.reportGen.model.ReportGen;

@Repository("reportComplianceDocumentRekapDao")
public class ReportComplianceDocumentRekapDaoImpl extends GenericDAOHibernate<ReportGen, Long> implements ReportComplianceDocumentRekapDao, ReportComplianceDocumentRekapConstants{

	@SuppressWarnings("rawtypes")
	private StringBuilder getQueryWhereStringByTipeDocument(StringBuilder sb, List<? extends SearchObject> searchCriteria) {
		
		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString() != null ? searchVal.getSearchValueAsString() : "";
				
				if (!StringUtils.isBlank(val)) {
					if (StringUtils.equals(WHERE_CREATION_DATE_FROM, col)) {
						sb.append("	and TRUNC(crd.creation_date) >= TO_DATE('" + val + "', 'yyyy-MM-dd') ");
					} else if (StringUtils.equals(WHERE_CREATION_DATE_TO, col)) {
						sb.append("	and TRUNC(crd.creation_date) <= TO_DATE('" + val +"', 'yyyy-MM-dd') ");
					} else if (StringUtils.equals(WHERE_DOCUMENT_DATE_FROM, col)) {
						sb.append("	and TRUNC(crd.complete_date) >= TO_DATE('" + val +"', 'yyyy-MM-dd') ");
					} else if (StringUtils.equals(WHERE_DOCUMENT_DATE_TO, col)) {
						sb.append("	and TRUNC(crd.complete_date) <= TO_DATE('" + val +"', 'yyyy-MM-dd') ");
					} else if (StringUtils.equals(WHERE_DOCUMENT_TYPE, col)) {
						sb.append("	and crd.document_type = '" + val +"' ");
					}
				}
			}
		}
		
		return sb;
	}
	
	@SuppressWarnings("rawtypes")
	private StringBuilder getQueryWhereStringByKelompokHuk(StringBuilder sb, List<? extends SearchObject> searchCriteria) {
		
		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString() != null ? searchVal.getSearchValueAsString() : "";
				
				if (!StringUtils.isBlank(val)) {
					if (StringUtils.equals(WHERE_CREATION_DATE_FROM, col)) {
						sb.append("	and TRUNC(crd.creation_date) >= TO_DATE('" + val + "', 'yyyy-MM-dd') ");
					} else if (StringUtils.equals(WHERE_CREATION_DATE_TO, col)) {
						sb.append("	and TRUNC(crd.creation_date) <= TO_DATE('" + val +"', 'yyyy-MM-dd') ");
					} else if (StringUtils.equals(WHERE_DOCUMENT_DATE_FROM, col)) {
						sb.append("	and TRUNC(crd.complete_date) >= TO_DATE('" + val +"', 'yyyy-MM-dd') ");
					} else if (StringUtils.equals(WHERE_DOCUMENT_DATE_TO, col)) {
						sb.append("	and TRUNC(crd.complete_date) <= TO_DATE('" + val +"', 'yyyy-MM-dd') ");
					} else if (StringUtils.equals(WHERE_DOCUMENT_SUBMITTER, col)) {
						sb.append("	and crd.document_submitter = '" + val +"' ");
					}
				}
			}
		}
		
		return sb;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public List<ReportComplianceDocumentRekap> getReportComplianceDocumentRekapByTipeDocumentData(
			List<? extends SearchObject> searchCriteria) {
		StringBuilder sb = new StringBuilder();
		sb.append("	select 	dtyp.name_in tipe_dokumen_in "
				+ "			,dtyp.name_en tipe_dokumen_en "
				+ "			,COUNT(1) total "
				+ "	from wo_mst_cmplc_rvw_doc crd "
				+ "		LEFT JOIN wo_mst_parameter_dtl dtyp On dtyp.parameter_code = 'DOCUMENT_TYPE' "
				+ "			AND dtyp.parameter_dtl_code = crd.document_type "
				+ "	where 1=1 ");
		
		sb = getQueryWhereStringByTipeDocument(sb, searchCriteria);
		
		sb.append(" GROUP BY dtyp.name_in, dtyp.name_en ");
		
		Query query = getSession().createSQLQuery(sb.toString());
		
		List resultList = query.getResultList();
		
		List<ReportComplianceDocumentRekap> vo = new ArrayList<ReportComplianceDocumentRekap>();
		
		if (resultList != null) {
			for (int i = 0; i < resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
				ReportComplianceDocumentRekap data = new ReportComplianceDocumentRekap();
				
				data.setTipeDokumenIn(obj[0] != null ? (String) obj[0] : null);
				data.setTipeDokumenEn(obj[1] != null ? (String) obj[1] : null);
				data.setTotal(obj[2] != null ? (((BigDecimal) obj[2]).toBigInteger()).intValue() : null);
				
				vo.add(data);
			}
		}
		
		return vo;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<Object[]> getReportComplianceDocumentRekapByTipeDocumentObj(
			List<? extends SearchObject> searchCriteria) {
		StringBuilder sb = new StringBuilder();
		sb.append("	select 	dtyp.name_in tipe_dokumen_in "
				+ "			,dtyp.name_en tipe_dokumen_en "
				+ "			,COUNT(1) total "
				+ "	from wo_mst_cmplc_rvw_doc crd "
				+ "		LEFT JOIN wo_mst_parameter_dtl dtyp On dtyp.parameter_code = 'DOCUMENT_TYPE' "
				+ "			AND dtyp.parameter_dtl_code = crd.document_type "
				+ "	where 1=1 ");
		
		sb = getQueryWhereStringByTipeDocument(sb, searchCriteria);
		
		sb.append(" GROUP BY dtyp.name_in, dtyp.name_en ");
		
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
	public List<ReportComplianceDocumentRekap> getReportComplianceDocumentRekapByKelompokHukData(
			List<? extends SearchObject> searchCriteria) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select 	huk.name_in tipe_dokumen_in "
				+ "			,huk.name_en tipe_dokumen_en "
				+ "			,COUNT(1) total "
				+ "	from wo_mst_cmplc_rvw_doc crd "
				+ "		LEFT JOIN wo_mst_parameter_dtl huk On huk.parameter_code = 'DOCUMENT_SUBMITTER' "
				+ "			AND huk.parameter_dtl_code = crd.document_submitter "
				+ "	where 1=1 " );
		
		sb = getQueryWhereStringByKelompokHuk(sb, searchCriteria);
		
		sb.append("	GROUP BY huk.name_in, huk.name_en ");
		
		Query query = getSession().createSQLQuery(sb.toString());
		
		List resultList = query.getResultList();
		
		List<ReportComplianceDocumentRekap> vo = new ArrayList<ReportComplianceDocumentRekap>();
		
		if (resultList != null) {
			for (int i = 0; i < resultList.size(); i++) {
				Object[] obj =(Object[]) resultList.get(i);
				ReportComplianceDocumentRekap data = new ReportComplianceDocumentRekap();
				
				data.setTipeDokumenIn(obj[0] != null ? (String) obj[0] : null);
				data.setTipeDokumenEn(obj[1] != null ? (String) obj[1] : null);
				data.setTotal(obj[2] != null ? (((BigDecimal) obj[2]).toBigInteger()).intValue() : null);
				
				vo.add(data);
			}
		}
		
		return vo;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<Object[]> getReportComplianceDocumentRekapByKelompokHukObj(
			List<? extends SearchObject> searchCriteria) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select 	huk.name_in tipe_dokumen_in "
				+ "			,huk.name_en tipe_dokumen_en "
				+ "			,COUNT(1) total "
				+ "	from wo_mst_cmplc_rvw_doc crd "
				+ "		LEFT JOIN wo_mst_parameter_dtl huk On huk.parameter_code = 'DOCUMENT_SUBMITTER' "
				+ "			AND huk.parameter_dtl_code = crd.document_submitter "
				+ "	where 1=1 " );
		
		sb = getQueryWhereStringByKelompokHuk(sb, searchCriteria);
		
		sb.append("	GROUP BY huk.name_in, huk.name_en ");
		
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
