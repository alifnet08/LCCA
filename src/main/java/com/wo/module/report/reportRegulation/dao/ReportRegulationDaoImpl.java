package com.wo.module.report.reportRegulation.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.wo.module.common.constant.Constants;
import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.common.util.MathUtil;
import com.wo.module.externalRegulation.model.Regulation;
import com.wo.module.externalRegulation.model.RegulationTrackRecord;
import com.wo.module.report.reportGen.model.ReportGen;
import com.wo.module.report.reportRegulation.constant.ReportRegulationConstant;
import com.wo.module.report.reportRegulation.vo.RegulationTrackRecordVo;
import com.wo.module.report.reportRegulation.vo.ReportRegulationDetailVo;
import com.wo.module.report.reportRegulation.vo.ReportRegulationRekapVo;
import com.wo.module.report.reportRegulation.vo.ReportRegulationTrackRecordVo;

@Repository("reportRegulationDao")
public class ReportRegulationDaoImpl extends GenericDAOHibernate<ReportGen, Long>
	implements ReportRegulationDao, Serializable{

	Logger logger = Logger.getLogger(ReportRegulationDaoImpl.class);
	private static final long serialVersionUID = -9013636357539586228L;

	@SuppressWarnings("rawtypes")
	private void getQueryWhereString(StringBuilder sb, List<? extends SearchObject> searchCriteria) {
		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString();
				
				if (!StringUtils.isBlank(val)) {
					if (StringUtils.equals(ReportRegulationConstant.SEARCH_BY_JENIS_PERATURAN, col)) {
						sb.append(" and r.jenis_ketentuan = :jenisKetentuan ");
					}
					if (StringUtils.equals(ReportRegulationConstant.SEARCH_BY_TIPE_PERATURAN, col)) {
						sb.append(" and d.DOCUMENT_TYPE_IN = :tipePeraturan ");
					}
					if (StringUtils.equals(ReportRegulationConstant.SEARCH_BY_PUBLISH_DATE_FROM, col)) {
						sb.append(" and TRUNC(r.PUBLISHED_DATE) >= to_date(:publishDateFrom, 'yyyy-MM-dd') ");	
					}
					if (StringUtils.equals(ReportRegulationConstant.SEARCH_BY_PUBLISH_DATE_TO, col)) {
						sb.append(" and TRUNC(r.PUBLISHED_DATE) <= to_date(:publishDateTo, 'yyyy-MM-dd') ");
					}
					if (StringUtils.equals(ReportRegulationConstant.SEARCH_BY_EXPIRED_DATE_FROM, col)) {
						sb.append(" and TRUNC(r.EXPIRED_DATE) >= to_date(:expiredDateFrom, 'yyyy-MM-dd') ");
					}
					if (StringUtils.equals(ReportRegulationConstant.SEARCH_BY_EXPIRED_DATE_TO, col)) {
						sb.append(" and TRUNC(r.EXPIRED_DATE) <= to_date(:expiredDateTo, 'yyyy-MM-dd') ");
					}
					if (StringUtils.equals(ReportRegulationConstant.SEARCH_BY_STATUS, col)) {
						sb.append(" and r.status = :status ");
					}
					if (StringUtils.equals(ReportRegulationConstant.SEARCH_BY_UNIT_PENGUSUL, col)) {
						sb.append(" and r.DIRECTORATE like :unitPengusul ");
					}
				}
				
			}
		}
	}
	
	@SuppressWarnings("rawtypes")
	private void getQueryWhereStringRekap(StringBuilder sb, List<? extends SearchObject> searchCriteria) {
		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString();
				
				if (!StringUtils.isBlank(val)) {
					if (StringUtils.equals(ReportRegulationConstant.SEARCH_BY_JENIS_PERATURAN, col)) {
						sb.append(" and r.JENIS_KETENTUAN = :jenisKetentuan ");
					}
					if (StringUtils.equals(ReportRegulationConstant.SEARCH_BY_TIPE_PERATURAN, col)) {
						sb.append(" and d.document_type_in = :tipePeraturan  ");
					}
					if (StringUtils.equals(ReportRegulationConstant.SEARCH_BY_PUBLISH_DATE_FROM, col)) {
						sb.append(" and TRUNC(r.PUBLISHED_DATE) >= to_date(:publishDateFrom, 'yyyy-MM-dd') ");	
					}
					if (StringUtils.equals(ReportRegulationConstant.SEARCH_BY_PUBLISH_DATE_TO, col)) {
						sb.append(" and TRUNC(r.PUBLISHED_DATE) <= to_date(:publishDateTo, 'yyyy-MM-dd') ");
					}
					if (StringUtils.equals(ReportRegulationConstant.SEARCH_BY_EXPIRED_DATE_FROM, col)) {
						sb.append(" and TRUNC(r.EXPIRED_DATE) >= to_date(:expiredDateFrom, 'yyyy-MM-dd') ");
					}
					if (StringUtils.equals(ReportRegulationConstant.SEARCH_BY_EXPIRED_DATE_TO, col)) {
						sb.append(" and TRUNC(r.EXPIRED_DATE) <= to_date(:expiredDateTo, 'yyyy-MM-dd') ");
					}
					if (StringUtils.equals(ReportRegulationConstant.SEARCH_BY_STATUS, col)) {
						sb.append(" and r.STATUS = :status ");
					}
					if (StringUtils.equals(ReportRegulationConstant.SEARCH_BY_UNIT_PENGUSUL, col)) {
						sb.append(" and r.PUBLISHER_UNIT like :unitPengusul ");
					}
				}
				
			}
		}
	}
	
	@SuppressWarnings("rawtypes")
	private void setQueryWhereString(Query query, List<? extends SearchObject> searchCriteria)	{
		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString();
				
				if (!StringUtils.isBlank(val)) {
					if (StringUtils.equals(ReportRegulationConstant.SEARCH_BY_JENIS_PERATURAN, col)) {
						query.setParameter("jenisKetentuan",  val);
					}
					if (StringUtils.equals(ReportRegulationConstant.SEARCH_BY_TIPE_PERATURAN, col)) {
						query.setParameter("tipePeraturan",  val);
					}
					if (StringUtils.equals(ReportRegulationConstant.SEARCH_BY_PUBLISH_DATE_FROM, col)) {
						query.setParameter("publishDateFrom", val);
					}
					if (StringUtils.equals(ReportRegulationConstant.SEARCH_BY_PUBLISH_DATE_TO, col)) {
						query.setParameter("publishDateTo", val);
					}
					if (StringUtils.equals(ReportRegulationConstant.SEARCH_BY_EXPIRED_DATE_FROM, col)) {
						query.setParameter("expiredDateFrom", val);
					}
					if (StringUtils.equals(ReportRegulationConstant.SEARCH_BY_EXPIRED_DATE_TO, col)) {
						query.setParameter("expiredDateTo", val);
					}
					if (StringUtils.equals(ReportRegulationConstant.SEARCH_BY_STATUS, col)) {
						query.setParameter("status", val);
					}
					if (StringUtils.equals(ReportRegulationConstant.SEARCH_BY_UNIT_PENGUSUL, col)) {
						query.setParameter("unitPengusul", "%"+ val +"%");
					}
				}
				
			}
		}
	}
	
	@SuppressWarnings("rawtypes")
	private void setQueryWhereStringRekap(Query query, List<? extends SearchObject> searchCriteria)	{
		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString();
				
				if (!StringUtils.isBlank(val)) {
					if (StringUtils.equals(ReportRegulationConstant.SEARCH_BY_JENIS_PERATURAN, col)) {
						query.setParameter("jenisKetentuan",  val);
					}
					if (StringUtils.equals(ReportRegulationConstant.SEARCH_BY_TIPE_PERATURAN, col)) {
						query.setParameter("tipePeraturan",  val);
					}
					if (StringUtils.equals(ReportRegulationConstant.SEARCH_BY_PUBLISH_DATE_FROM, col)) {
						query.setParameter("publishDateFrom", val);
					}
					if (StringUtils.equals(ReportRegulationConstant.SEARCH_BY_PUBLISH_DATE_TO, col)) {
						query.setParameter("publishDateTo", val);
					}
					if (StringUtils.equals(ReportRegulationConstant.SEARCH_BY_EXPIRED_DATE_FROM, col)) {
						query.setParameter("expiredDateFrom", val);
					}
					if (StringUtils.equals(ReportRegulationConstant.SEARCH_BY_EXPIRED_DATE_TO, col)) {
						query.setParameter("expiredDateTo", val);
					}
					if (StringUtils.equals(ReportRegulationConstant.SEARCH_BY_STATUS, col)) {
						query.setParameter("status", val);
					}
					if (StringUtils.equals(ReportRegulationConstant.SEARCH_BY_UNIT_PENGUSUL, col)) {
						query.setParameter("unitPengusul", "%"+ val +"%");
					}
				}
				
			}
		}
	}


	@SuppressWarnings("rawtypes")
	@Override
	public List<ReportRegulationDetailVo> getReportRegulationDetailByAllDataAsVo(
			List<? extends SearchObject> searchCriteria) {
		StringBuilder sb = new StringBuilder();
		
		sb.append(" 	SELECT "
				+ "		j.NAME_IN AS peraturan_in, "
				+ "		j.name_en AS peraturan_en, "
				+ "		r.DIRECTORATE, "
				+ "		r.DOCUMENT_NO || ' : ' || r.NAME_IN JUDUL, "
				+ "		r.PUBLISHED_DATE, "
				+ "		to_char(r.PUBLISHED_DATE, 'DD-MON-YYYY') published_date_str, "
				+ "		l.NAME_IN "
				+ "	|| /*CASE "
				+ "	      WHEN r.jenis_ketentuan = 'KETENTUAN_EKSTERNAL' "
				+ "	      THEN "
				+ "	         'pages/externalRegulationFE/externalRegulationFEView.faces?id=' "
				+ "	      ELSE "
				+ "	         'pages/internalRegulationFE/internalRegulationFEView.faces?id=' "
				+ "	   END "
				+ "	|| r.REGULATION_ID*/ "
				+ "		CASE "
				+ "			WHEN r.jenis_ketentuan = 'KETENTUAN_EKSTERNAL' "
				+ "	  THEN "
				+ "	     'pages/externalRegulationFE/externalRegulationFEView.faces?token=' "
				+ "			ELSE "
				+ "	     'pages/internalRegulationFE/internalRegulationFEView.faces?token=' "
				+ "		END "
				+ "	           URL, "
				+ "		d.DOCUMENT_TYPE_IN, "
				+ "		d.DOCUMENT_TYPE_EN, "
				+ "		COUNT (a.LOG_ACCESS_ID) HITS, "
				+ "		r.EXPIRED_DATE, "
				+ "		to_char(r.EXPIRED_DATE, 'DD-MON-YYYY') expired_date_str, "
				+ "		CASE "
				+ "			WHEN r.EXPIRED_DATE IS NOT NULL "
				+ "			AND ADD_MONTHS (TRUNC (SYSDATE), "
				+ "			TO_NUMBER (x.NAME_IN)) >= "
				+ "	                       r.EXPIRED_DATE "
				+ "	           THEN "
				+ "	              1 "
				+ "			ELSE "
				+ "	              0 "
				+ "		END "
				+ "	           EXPIRED_WARNING, "
				+ "		s.NAME_IN AS status_in, "
				+ "		s.name_en AS status_en, "
				+ "		r.status, "
				+ "		r.jenis_ketentuan, "
				+ "		r.REGULATION_ID, "
				+ "		r.publisher_unit AS unit_pengusul,"
				+ "		r.TYPE_REVIEW_DATE, "
				+ "		typeRevDate.NAME_IN "
				+ "	FROM "
				+ "		WO_MST_REGULATION r "
				+ "	INNER JOIN WO_MST_PARAMETER_DTL l "
				+ "	           ON "
				+ "		l.PARAMETER_CODE = 'SYSTEM' "
				+ "		AND l.PARAMETER_DTL_CODE = 'HOST_NAME_APPLICATION' "
				+ "	INNER JOIN WO_MST_PARAMETER_DTL x "
				+ "	   ON "
				+ "		x.PARAMETER_CODE = 'SYSTEM' "
				+ "		AND x.PARAMETER_DTL_CODE = "
				+ "	         'INTERNAL_REGULATION_EMAIL_EXPIRED_MONTH' "
				+ "	INNER JOIN WO_MST_PARAMETER_DTL s "
				+ "	   ON "
				+ "		s.PARAMETER_CODE = 'DATA_STATUS' "
				+ "		AND s.PARAMETER_DTL_CODE = r.status"
				+ " LEFT JOIN WO_MST_PARAMETER_DTL typeRevDate"
				+ "		ON"
				+ "		 typeRevDate.PARAMETER_DTL_CODE = r.TYPE_REVIEW_DATE "
				+ "	LEFT JOIN WO_MST_PARAMETER_DTL j "
				+ "	   ON "
				+ "		j.parameter_code = 'JENIS_KETENTUAN' "
				+ "		AND j.parameter_dtl_code = r.jenis_ketentuan "
				+ "	LEFT JOIN WO_MST_DOCUMENT_TYPE d "
				+ "	   ON "
				+ "		d.DOCUMENT_TYPE_ID = r.document_type_id "
				+ "	LEFT JOIN WO_LOG_ACCESS a "
				+ "	   ON "
				+ "		( ( r.jenis_ketentuan = 'KETENTUAN_EKSTERNAL' "
				+ "			AND a.ACCESS_ACTION LIKE "
				+ "	          '/compliance/pages/externalRegulationFE/externalRegulationFEView.faces%') "
				+ "		OR ( r.jenis_ketentuan = 'KETENTUAN_INTERNAL' "
				+ "			AND a.ACCESS_ACTION LIKE "
				+ "	          '/compliance/pages/internalRegulationFE/internalRegulationFEView.faces%')) "
				+ "		AND a.ACCESS_ID = r.REGULATION_ID "
				+ "	WHERE "
				+ "		r.enabled_flag = 'Y' ");

		this.getQueryWhereString(sb, searchCriteria);
		
		sb.append(" GROUP BY "
				+ "		j.NAME_IN, "
				+ "		j.name_en, "
				+ "		r.DIRECTORATE, "
				+ "		r.DOCUMENT_NO, "
				+ "		r.NAME_IN, "
				+ "		r.PUBLISHED_DATE, "
				+ "		l.NAME_IN, "
				+ "		r.REGULATION_ID, "
				+ "		d.DOCUMENT_TYPE_IN, "
				+ "		d.DOCUMENT_TYPE_EN, "
				+ "		r.EXPIRED_DATE, "
				+ "		x.NAME_IN, "
				+ "		s.NAME_IN, "
				+ "		s.name_en, "
				+ "		r.status, "
				+ "		r.publisher_unit, "
				+ "		r.jenis_ketentuan,"
				+ "		r.TYPE_REVIEW_DATE,"
				+ "		typeRevDate.NAME_IN ");
		
		Query query = getSession().createSQLQuery(sb.toString());
		this.setQueryWhereString(query, searchCriteria);
		
		List result = query.getResultList();
		List<ReportRegulationDetailVo> vo = new ArrayList<ReportRegulationDetailVo>();
		
		if (result != null) {
			for (int i = 0; i < result.size(); i++) {
				Object[] obj = (Object[]) result.get(i);
				ReportRegulationDetailVo data = new ReportRegulationDetailVo();
				
				data.setPeraturanIn(obj[0] != null ? (String) obj[0] : null);
				data.setPeraturanEn(obj[1] != null ? (String) obj[1] : null);
				data.setDirectorate(obj[2] != null ? (String) obj[2] : null);
				data.setJudul(obj[3] != null ? (String) obj[3] : null);
				data.setPublishDate(obj[4] != null ? (Date) obj[4] : null);
				data.setPublishDateStr(obj[5] != null ? (String) obj[5] : null);
				data.setUrl(obj[6] != null ? (String) obj[6] : null);
				data.setDocumentTypeIn(obj[7] != null ? (String) obj[7] : null);
				data.setDocumentTypeEn(obj[8] != null ? (String) obj[8] : null);
				data.setHits(obj[9] != null ? MathUtil.returnIdObjectToLong(obj[9]) : null);
				data.setExpiredDate(obj[10] != null ? (Date) obj[10] : null);
				data.setExpiredDateStr(obj[11] != null ? (String) obj[11] : null);
				data.setExpiredWarning(obj[12] != null ? MathUtil.returnIdObjectToLong(obj[12]) : null);
				data.setStatusIn(obj[13] != null ? (String) obj[13] : null);
				data.setStatusEn(obj[14] != null ? (String) obj[14] : null);
				data.setStatus(obj[15] != null ? (String) obj[15] : null);
				data.setJenisKetentuan(obj[16] != null ? (String) obj[16] : null);
				data.setRegulationId(obj[17] != null ? MathUtil.returnIdObjectToLong(obj[17]) : null);
				data.setUrl(data.getUrl().concat(Constants.encryptString(data.getRegulationId().toString())));
				data.setTypeReviewDateCode(obj[18] != null ? (String) obj[18] : null);
				data.setTypeReviewDateName(obj[19] != null ? (String) obj[19] : null);
				data.setTrackRecordVos(getTrackRecordsByregulationId(data.getRegulationId()));
				data.setCountTrackRecord(data.getTrackRecordVos().size());
				vo.add(data);
				
			}
		}
		
		return vo;
	}

	@SuppressWarnings("rawtypes")
	private List<RegulationTrackRecordVo> getTrackRecordsByregulationId(long regulationId){
		StringBuilder sb = new StringBuilder();
		
		sb.append(" SELECT"
				+ "	a.REGULATION_TRACK_RECORD_ID,"
				+ "	a.REGULATION_ID,"
				+ "	a.TRACK_CODE,"
				+ "	a.REGULATION_LINK_ID,"
				+ "	a.REGULATION_LINK_NAME,"
				+ "	a.TRACK_NOTE,"
				+ "	b.NAME_IN,"
				+ " c.DOCUMENT_NO, c.NAME_IN as judul "
				+ "FROM"
				+ "		WO_MST_REGULATION_TRACK_RECORD a "
				+ "INNER JOIN WO_MST_PARAMETER_DTL b"
				+ "	ON b.PARAMETER_DTL_CODE = a.TRACK_CODE "
				+ "LEFT JOIN WO_MST_REGULATION c"
				+ "	ON c.REGULATION_ID = a.REGULATION_LINK_ID "
				+ "WHERE"
				+ "	1 = 1"
				+ " AND a.REGULATION_ID = :regId "
				+ "ORDER BY REGULATION_ID DESC ");
		Query query = getSession().createSQLQuery(sb.toString());
		query.setParameter("regId", regulationId);
		List result = query.getResultList();
		List<RegulationTrackRecordVo> vo = new ArrayList<>();
		
		if (result != null) {
			for (int i = 0; i < result.size(); i++) {
				Object[] obj = (Object[]) result.get(i);
				RegulationTrackRecordVo data = new RegulationTrackRecordVo();
				data.setRegulationTrackRecordId(obj[0] != null ? MathUtil.returnIdObjectToLong(obj[0]) : null);
				data.setRegulationId(obj[1] != null ? MathUtil.returnIdObjectToLong(obj[1]) : null);
				data.setTrackCode(obj[2] != null ? (String)(obj[2]) : null);
				data.setRegulationLinkId(obj[3] != null ? MathUtil.returnIdObjectToLong(obj[3]) : null);
				data.setRegulationLinkName(obj[4] != null ? (String)(obj[4]) : null);
				data.setTrackNote(obj[5] != null ? (String)(obj[5]) : null);
				data.setTrackName(obj[6] != null ? (String)(obj[6]) : null);
				data.setRegulationNoPrev(obj[7] != null ? (String)(obj[7]) : null);
				data.setRegulationNamePrev(obj[8] != null ? (String)(obj[8]) : null);
				vo.add(data);
			}
		}
		return vo;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public List<ReportRegulationRekapVo> getReportRegulationRekapByProvisionTypeAsVo(
			List<? extends SearchObject> searchCriteria) {
		StringBuilder sb = new StringBuilder();
		
		sb.append(" SELECT d.DOCUMENT_TYPE_IN ");
		sb.append("       ,COUNT (1) DOCUMENT_COUNT ");
		sb.append(" FROM WO_MST_REGULATION r ");
		sb.append("     LEFT JOIN WO_MST_DOCUMENT_TYPE d ");
		sb.append("         ON d.DOCUMENT_TYPE_ID = r.document_type_id ");
		sb.append(" WHERE     r.enabled_flag = 'Y' ");
		
		this.getQueryWhereStringRekap(sb, searchCriteria);
		sb.append(" GROUP BY d.DOCUMENT_TYPE_IN ");
		
		Query query = getSession().createSQLQuery(sb.toString());
		this.setQueryWhereStringRekap(query, searchCriteria);
		
		List result = query.getResultList();
		List<ReportRegulationRekapVo> vo = new ArrayList<ReportRegulationRekapVo>();
		
		if (result != null) {
			for (int i = 0; i < result.size(); i++) {
				Object[] obj = (Object[]) result.get(i);
				ReportRegulationRekapVo data = new ReportRegulationRekapVo();
				
				data.setDocumentTypeIn(obj[0] != null ? (String) obj[0] : null);
				data.setTotalRegulation(obj[1] != null ? MathUtil.returnIdObjectToLong(obj[1]) : null);
				
				vo.add(data);
			}
		}
		
		return vo;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<ReportRegulationRekapVo> getReportRegulationRekapByHitsAsVo(
			List<? extends SearchObject> searchCriteria) {
		StringBuilder sb = new StringBuilder();
		
		sb.append(" SELECT d.DOCUMENT_TYPE_IN ");
		sb.append("       ,COUNT (1) HITS_COUNT ");
		sb.append(" FROM WO_MST_REGULATION r ");
		sb.append("     LEFT JOIN WO_MST_PARAMETER_DTL j ");
		sb.append("         ON j.parameter_code = 'JENIS_KETENTUAN' ");
		sb.append("     LEFT JOIN WO_MST_DOCUMENT_TYPE d ");
		sb.append("         ON d.DOCUMENT_TYPE_ID = r.document_type_id ");
		sb.append("     LEFT JOIN WO_LOG_ACCESS a ");
		sb.append("         ON  ( ");
		sb.append("                 ( ");
		sb.append("                     r.jenis_ketentuan = 'KETENTUAN_EKSTERNAL' AND a.ACCESS_ACTION = '/compliance/pages/externalRegulationFE/externalRegulationFE.faces') ");
		sb.append("                     OR ( ");
		sb.append("                         r.jenis_ketentuan = 'KETENTUAN_INTERNAL' AND a.ACCESS_ACTION = '/compliance/pages/internalRegulationFE/internalRegulationFE.faces') ");
		sb.append("             ) ");
		sb.append("             AND a.ACCESS_ID = r.REGULATION_ID ");
		sb.append(" WHERE     r.enabled_flag = 'Y' ");

		this.getQueryWhereStringRekap(sb, searchCriteria);
		sb.append(" GROUP BY d.DOCUMENT_TYPE_IN ");
		
		Query query = getSession().createSQLQuery(sb.toString());
		this.setQueryWhereStringRekap(query, searchCriteria);
		
		List result = query.getResultList();
		List<ReportRegulationRekapVo> vo = new ArrayList<ReportRegulationRekapVo>();
		
		if (result != null) {
			for (int i = 0; i < result.size(); i++) {
				Object[] obj = (Object[]) result.get(i);
				ReportRegulationRekapVo data = new ReportRegulationRekapVo();
				
				data.setDocumentTypeIn(obj[0] != null ? (String) obj[0] : null);
				data.setTotalRegulation(obj[1] != null ? MathUtil.returnIdObjectToLong(obj[1]) : null);
				
				vo.add(data);
			}
		}
		
		return vo;
	}
	
}
