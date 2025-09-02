package com.wo.module.report.reportRmdRekap.dao;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.report.reportGen.model.ReportGen;
import com.wo.module.report.reportRmdDetail.constant.ReportRmdDetailConstant;
import com.wo.module.report.reportRmdRekap.model.ReportRmdRekap;

@Repository("reportRmdRekapDao")
public class ReportRmdRekapDaoImpl extends GenericDAOHibernate<ReportGen, Long> 
	implements ReportRmdRekapDao, ReportRmdDetailConstant{

	@SuppressWarnings("rawtypes")
	private StringBuilder getQueryWhereString(StringBuilder sb, List<? extends SearchObject> searchCriteria) {
		
		if(searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString() != null ? searchVal.getSearchValueAsString() : "";
				
				if (!StringUtils.isBlank(val)) {
					if (StringUtils.equals(WHERE_CREATION_DATE_FROM, col)) {
						sb.append(" and TRUNC(r.creation_date) >= TO_DATE('" + val + "', 'yyyy-MM-dd') ");
					} else if (StringUtils.equals(WHERE_CREATION_DATE_TO, col)) {
						sb.append("	and TRUNC(r.creation_date) <= TO_DATE('" + val + "', 'yyyy-MM-dd') ");
					} else if (StringUtils.equals(WHERE_PROV_TYPE, col)) {
						sb.append(" and rrr.jenis_ketentuan = '" + val + "' ");
					} else if (StringUtils.equals(WHERE_DOC_TYPE, col)) {
						sb.append(" and rrr.document_type_id = '" + val + "' ");
					} else if (StringUtils.equals(WHERE_SENDER_CODE, col)) {
						sb.append(" and c.sender_code = '" + val + "' ");
					} else if (StringUtils.equals(WHERE_TARGET_DATE_FROM, col)) {
						sb.append(" and TRUNC(rpf.target_date) >= TO_DATE('" +  val + "', 'yyyy-MM-dd') ");
					} else if (StringUtils.equals(WHERE_TARGET_DATE_TO, col)) {
						sb.append(" and TRUNC(rpf.target_date) <= TO_DATE('" +  val + "', 'yyyy-MM-dd') ");
					}
				}
				
			}
		}
		
		return sb;
	}
	
	@SuppressWarnings({ "rawtypes", "unused" })
	private StringBuilder getQueryWhereStringByDateAndJenisKetentuan(StringBuilder sb, List<? extends SearchObject> searchCriteria) {
		
		if(searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString() != null ? searchVal.getSearchValueAsString() : "";
				
				if (!StringUtils.isBlank(val)) {
					if (StringUtils.equals(WHERE_CREATION_DATE_FROM, col)) {
						sb.append(" and TRUNC(r.creation_date) >= TO_DATE('" + val + "', 'yyyy-MM-dd') ");
					} else if (StringUtils.equals(WHERE_CREATION_DATE_TO, col)) {
						sb.append("	and TRUNC(r.creation_date) <= TO_DATE('" + val + "', 'yyyy-MM-dd') ");
					} else if (StringUtils.equals(WHERE_PROV_TYPE, col)) {
						sb.append(" and rrr.jenis_ketentuan = '" + val + "' ");
					}
				}
			}
		}
		
		return sb;
	}
	
	@SuppressWarnings({ "rawtypes", "unused" })
	private StringBuilder getQueryWhereStringByDateAndSenderCode(StringBuilder sb, List<? extends SearchObject> searchCriteria) {
		
		if(searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString() != null ? searchVal.getSearchValueAsString() : "";
				
				if (!StringUtils.isBlank(val)) {
					if (StringUtils.equals(WHERE_CREATION_DATE_FROM, col)) {
						sb.append(" and TRUNC(r.creation_date) >= TO_DATE('" + val + "', 'yyyy-MM-dd') ");
					} else if (StringUtils.equals(WHERE_CREATION_DATE_TO, col)) {
						sb.append("	and TRUNC(r.creation_date) <= TO_DATE('" + val + "', 'yyyy-MM-dd') ");
					} else if (StringUtils.equals(WHERE_DOC_TYPE, col)) {
						sb.append(" and c.sender_code = '" + val + "' ");
					}
				}
			}
		}
		
		return sb;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public List<ReportRmdRekap> getReportRmdRekapAsData(List<? extends SearchObject> searchCriteria) {
		StringBuilder sb = new StringBuilder();
		
		sb.append(" select dtr.document_type_in AS jenis_peraturan_in "
				+"		,dtr.document_type_en AS jenis_peraturan_en "
				+"		,sndr.name_in AS pengirim_surat_in "
				+"		,sndr.name_en AS pengirim_surat_en "
				+"		,coalesce(sum(case when lower(rt.report_type_in) like '%bulanan%' then 1 end), 0) as Bulanan "
				+"		,coalesce(sum(case when lower(rt.report_type_in) like '%tahunan%' then 1 end), 0) as Tahunan "
				+"		,coalesce(sum(case when lower(rt.report_type_in) like '%triwulanan%' then 1 end), 0) as Triwulanan "
				+"		,coalesce(sum(case when lower(rt.report_type_in) like '%semester%' then 1 end), 0) as Semester "
				+"		,coalesce(sum(case when lower(rt.report_type_in) like '%insidentil%' then 1 end), 0) as Insidentil "
				+"		,coalesce(sum(case when lower(rt.report_type_in) like '%mingguan%' then 1 end), 0) as Mingguan "
				+"		,coalesce(sum(case when lower(rt.report_type_in) like '%harian%' then 1 end), 0) as Harian "
				+"		,count(1) jumlah_laporan "
				+"		,SUM(CASE WHEN rpf.followup_status IS NULL THEN 1 ELSE 0 END) in_progress "
				+"		,SUM(CASE WHEN rpf.followup_status IS NOT NULL THEN 1 ELSE 0 END) closed "
				+"		,SUM(CASE WHEN rpf.followup_status IS NOT NULL AND rpf.followup_date = rpf.target_date THEN 1 ELSE 0 END) meet_sla "
				+"		,SUM(CASE WHEN rpf.followup_status IS NOT NULL AND rpf.followup_date < rpf.target_date THEN 1 ELSE 0 END) before_sla "
				+"		,SUM(CASE WHEN rpf.followup_status IS NOT NULL AND rpf.followup_date > rpf.target_date THEN 1 ELSE 0 END) over_sla "
				+" from wo_trc_rmd r "
				+ "		LEFT JOIN ( select * "
				+ "				    from wo_trc_rmd_correspondence rc "
				+ "					where 1 = 1 "
				+ "						and rc.rmd_correspondence_id =  ( select min(rc.rmd_correspondence_id) "
				+ "													  	  from wo_trc_rmd_correspondence rc1 "
				+ "													  	  where rc1.rmd_id = rc.rmd_id) ) rc "
				+ "			ON (rc.rmd_id = r.rmd_id) "
//				+"		LEFT JOIN wo_trc_rmd_correspondence rc ON (rc.rmd_id = r.rmd_id) and rc.rmd_correspondence_id = (select min(rc1.rmd_correspondence_id) from wo_trc_rmd_correspondence rc1 where rc1.rmd_id = rc.rmd_id) "
				+"		LEFT JOIN wo_trc_correspondence c ON (c.correspondence_id = rc.correspondence_id) "
				+"		LEFT JOIN wo_mst_parameter_dtl sndr ON (" //sndr.parameter_code = 'SENDER' AND "
				+"			sndr.parameter_dtl_code = c.sender_code) "
				+ "		LEFT JOIN ( select * "
				+ "					from wo_trc_rmd_regulation rr "
				+ "					where 1 = 1 "
				+ "						and rr.rmd_regulation_id = ( select min(rr1.rmd_regulation_id) "
				+ "						from wo_trc_rmd_regulation rr1 "
				+ "						where rr1.rmd_id = rr.rmd_id) ) rr "
				+ "			ON (rr.rmd_id = r.rmd_id) "
//				+"		LEFT JOIN wo_trc_rmd_regulation rr ON (rr.rmd_id = r.rmd_id) and rr.rmd_regulation_id = (select min(rr1.rmd_regulation_id) from wo_trc_rmd_regulation rr1 where rr1.rmd_id = rr.rmd_id) "
				+"		LEFT JOIN wo_mst_regulation rrr ON (rrr.regulation_id = rr.regulation_id) "
				+"		LEFT JOIN wo_mst_document_type dtr ON (dtr.document_type_id = rrr.document_type_id) "
				+"		LEFT JOIN wo_mst_report_type rt ON rt.report_type_id = r.report_type_id "
				+"		LEFT JOIN wo_trc_rmd_pic_followup rpf ON rpf.rmd_id = r.rmd_id "
				+ "	where 1=1 " ); 
		
		sb = getQueryWhereString(sb, searchCriteria);
		
		sb.append(" group by dtr.document_type_in "
				+ "		,dtr.document_type_en "
				+ "		,sndr.name_in "
				+ "		,sndr.name_en " );
		
		Query query = getSession().createSQLQuery(sb.toString());
		
		List resultList = query.getResultList();
		
		List<ReportRmdRekap> vo = new ArrayList<ReportRmdRekap>();
		
		if (resultList != null) {
			for (int i = 0; i < resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
				ReportRmdRekap data = new ReportRmdRekap();
				
				data.setJenisPeraturanIn(obj[0] != null ? (String) obj[0] : null);
				data.setJenisPeraturanEn(obj[1] != null ? (String) obj[1] : null);
				data.setPengirimSuratIn(obj[2] != null ? (String) obj[2] : null);
				data.setPengirimSuratEn(obj[3] != null ? (String) obj[3] : null);
				data.setBulanan(obj[4] != null ? (((BigDecimal) obj[4]).toBigInteger()).intValue() : null);
				data.setTahunan(obj[5] != null ? (((BigDecimal) obj[5]).toBigInteger()).intValue() : null);
				data.setTriwulanan(obj[6] != null ? (((BigDecimal) obj[6]).toBigInteger()).intValue() : null);
				data.setSemester(obj[7] != null ? (((BigDecimal) obj[7]).toBigInteger()).intValue() : null);
				data.setInsidentil(obj[8] != null ? (((BigDecimal) obj[8]).toBigInteger()).intValue() : null);
				data.setMingguan(obj[9] != null ? (((BigDecimal) obj[9]).toBigInteger()).intValue() : null);
				data.setHarian(obj[10] != null ? (((BigDecimal) obj[10]).toBigInteger()).intValue() : null);
				data.setJumlahLaporan(obj[11] != null ? (((BigDecimal) obj[11]).toBigInteger()).intValue() : null);
				data.setInProgress(obj[12] != null ? (((BigDecimal) obj[12]).toBigInteger()).intValue() : null);
				data.setClosed(obj[13] != null ? (((BigDecimal) obj[13]).toBigInteger()).intValue() : null);
				data.setMeetSla(obj[14] != null ? (((BigDecimal) obj[14]).toBigInteger()).intValue() : null);
				data.setBeforeSla(obj[15] != null ? (((BigDecimal) obj[15]).toBigInteger()).intValue() : null);
				data.setOverSla(obj[16] != null ? (((BigDecimal) obj[16]).toBigInteger()).intValue() : null);
				
				vo.add(data);
			}
		}
		
		return vo;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<Object[]> getReportRmdRekapAsObj(List<? extends SearchObject> searchCriteria) {
		StringBuilder sb = new StringBuilder();
		
		sb.append(" select dtr.document_type_in AS jenis_peraturan_in "
				+"		,dtr.document_type_en AS jenis_peraturan_en "
				+"		,sndr.name_in AS pengirim_surat_in "
				+"		,sndr.name_en AS pengirim_surat_en "
				+"		,coalesce(sum(case when lower(rt.report_type_in) like '%bulanan%' then 1 end), 0) as Bulanan "
				+"		,coalesce(sum(case when lower(rt.report_type_in) like '%tahunan%' then 1 end), 0) as Tahunan "
				+"		,coalesce(sum(case when lower(rt.report_type_in) like '%triwulanan%' then 1 end), 0) as Triwulanan "
				+"		,coalesce(sum(case when lower(rt.report_type_in) like '%semester%' then 1 end), 0) as Semester "
				+"		,coalesce(sum(case when lower(rt.report_type_in) like '%insidentil%' then 1 end), 0) as Insidentil "
				+"		,coalesce(sum(case when lower(rt.report_type_in) like '%mingguan%' then 1 end), 0) as Mingguan "
				+"		,coalesce(sum(case when lower(rt.report_type_in) like '%harian%' then 1 end), 0) as Harian "
				+"		,count(1) jumlah_laporan "
				+"		,SUM(CASE WHEN rpf.followup_status IS NULL THEN 1 ELSE 0 END) in_progress "
				+"		,SUM(CASE WHEN rpf.followup_status IS NOT NULL THEN 1 ELSE 0 END) closed "
				+"		,SUM(CASE WHEN rpf.followup_status IS NOT NULL AND rpf.followup_date = rpf.target_date THEN 1 ELSE 0 END) meet_sla "
				+"		,SUM(CASE WHEN rpf.followup_status IS NOT NULL AND rpf.followup_date < rpf.target_date THEN 1 ELSE 0 END) before_sla "
				+"		,SUM(CASE WHEN rpf.followup_status IS NOT NULL AND rpf.followup_date > rpf.target_date THEN 1 ELSE 0 END) over_sla "
				+" from wo_trc_rmd r "
				+ "		LEFT JOIN ( select * "
				+ "				    from wo_trc_rmd_correspondence rc "
				+ "					where 1 = 1 "
				+ "						and rc.rmd_correspondence_id =  ( select min(rc.rmd_correspondence_id) "
				+ "													  	  from wo_trc_rmd_correspondence rc1 "
				+ "													  	  where rc1.rmd_id = rc.rmd_id) ) rc "
				+ "			ON (rc.rmd_id = r.rmd_id) "
//				+"		LEFT JOIN wo_trc_rmd_correspondence rc ON (rc.rmd_id = r.rmd_id) and rc.rmd_correspondence_id = (select min(rc1.rmd_correspondence_id) from wo_trc_rmd_correspondence rc1 where rc1.rmd_id = rc.rmd_id) "
				+"		LEFT JOIN wo_trc_correspondence c ON (c.correspondence_id = rc.correspondence_id) "
				+"		LEFT JOIN wo_mst_parameter_dtl sndr ON (" //sndr.parameter_code = 'SENDER' AND "
				+"			sndr.parameter_dtl_code = c.sender_code) "
				+ "		LEFT JOIN ( select * "
				+ "					from wo_trc_rmd_regulation rr "
				+ "					where 1 = 1 "
				+ "						and rr.rmd_regulation_id = ( select min(rr1.rmd_regulation_id) "
				+ "						from wo_trc_rmd_regulation rr1 "
				+ "						where rr1.rmd_id = rr.rmd_id) ) rr "
				+ "			ON (rr.rmd_id = r.rmd_id) "
//				+"		LEFT JOIN wo_trc_rmd_regulation rr ON (rr.rmd_id = r.rmd_id) and rr.rmd_regulation_id = (select min(rr1.rmd_regulation_id) from wo_trc_rmd_regulation rr1 where rr1.rmd_id = rr.rmd_id) "
				+"		LEFT JOIN wo_mst_regulation rrr ON (rrr.regulation_id = rr.regulation_id) "
				+"		LEFT JOIN wo_mst_document_type dtr ON (dtr.document_type_id = rrr.document_type_id) "
				+"		LEFT JOIN wo_mst_report_type rt ON rt.report_type_id = r.report_type_id "
				+"		LEFT JOIN wo_trc_rmd_pic_followup rpf ON rpf.rmd_id = r.rmd_id "
				+ "	where 1=1 " ); 
		
		sb = getQueryWhereString(sb, searchCriteria);
		
		sb.append(" group by dtr.document_type_in "
				+ "		,dtr.document_type_en "
				+ "		,sndr.name_in "
				+ "		,sndr.name_en " );
		
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
	public List<ReportRmdRekap> getReportRmdRekapAsPeraturanOrJenisSameData(
			List<? extends SearchObject> searchCriteria, String docNameEn, String docNameIn, String senderNameEn, String senderNameIn) {
		StringBuilder sb = new StringBuilder();
		
		sb.append(" select t.jenis_peraturan_in,t.jenis_peraturan_en "
				+"		,sum(t.Bulanan) Bulanan "
				+"		,sum(t.Tahunan) Tahunan "
				+"		,sum(t.Triwulanan) Triwulanan "
				+"		,sum(t.Semester) Semester "
				+"		,sum(t.Insidentil) Insidentil "
				+"		,sum(t.Mingguan) Mingguan "
				+"		,sum(t.Harian) Harian "
				+"		,sum(t.jumlah_laporan) jumlah_laporan "
				+"		,sum(t.in_progress) in_progress "
				+"		,sum(t.closed) closed "
				+"		,sum(t.meet_sla) meet_sla "
				+"		,sum(t.before_sla) before_sla "
				+"		,sum(t.over_sla) over_sla "
				+"	from ( "
				+"		select dtr.document_type_in AS jenis_peraturan_in, "
				+"			dtr.document_type_en AS jenis_peraturan_en "
				+"			,coalesce(sum(case when lower(rt.report_type_in) like '%bulanan%' then 1 end), 0) as Bulanan "
				+"			,coalesce(sum(case when lower(rt.report_type_in) like '%tahunan%' then 1 end), 0) as Tahunan "
				+"			,coalesce(sum(case when lower(rt.report_type_in) like '%triwulanan%' then 1 end), 0) as Triwulanan "
				+"			,coalesce(sum(case when lower(rt.report_type_in) like '%semester%' then 1 end), 0) as Semester "
				+"			,coalesce(sum(case when lower(rt.report_type_in) like '%insidentil%' then 1 end), 0) as Insidentil "
				+"			,coalesce(sum(case when lower(rt.report_type_in) like '%mingguan%' then 1 end), 0) as Mingguan "
				+"			,coalesce(sum(case when lower(rt.report_type_in) like '%harian%' then 1 end), 0) as Harian "
				+"			,count(1) jumlah_laporan "
				+"			,SUM(CASE WHEN rpf.followup_status IS NULL THEN 1 ELSE 0 END) in_progress "
				+"			,SUM(CASE WHEN rpf.followup_status IS NOT NULL THEN 1 ELSE 0 END) closed "
				+"			,SUM(CASE WHEN rpf.followup_status IS NOT NULL AND rpf.followup_date = rpf.target_date THEN 1 ELSE 0 END) meet_sla "
				+"			,SUM(CASE WHEN rpf.followup_status IS NOT NULL AND rpf.followup_date < rpf.target_date THEN 1 ELSE 0 END) before_sla "
				+"			,SUM(CASE WHEN rpf.followup_status IS NOT NULL AND rpf.followup_date > rpf.target_date THEN 1 ELSE 0 END) over_sla "
				+"		from wo_trc_rmd r "
				+ "		LEFT JOIN ( select * "
				+ "					from wo_trc_rmd_regulation rr "
				+ "					where 1 = 1 "
				+ "						and rr.rmd_regulation_id = ( select min(rr1.rmd_regulation_id) "
				+ "						from wo_trc_rmd_regulation rr1 "
				+ "						where rr1.rmd_id = rr.rmd_id) ) rr "
				+ "			ON (rr.rmd_id = r.rmd_id) "
//				+"			JOIN wo_trc_rmd_regulation rr ON (rr.rmd_id = r.rmd_id) and rr.rmd_regulation_id = (select min(rr1.rmd_regulation_id) from wo_trc_rmd_regulation rr1 where rr1.rmd_id = rr.rmd_id) "
				+"			LEFT JOIN wo_trc_rmd_correspondence rc ON rc.rmd_id = r.rmd_id "
				+"			LEFT JOIN wo_trc_correspondence c ON (c.correspondence_id = rc.correspondence_id) "
				+"			LEFT JOIN wo_mst_regulation rrr ON (rrr.regulation_id = rr.regulation_id) "
				+"			LEFT JOIN wo_mst_document_type dtr ON (dtr.document_type_id = rrr.document_type_id) "
				+"			LEFT JOIN wo_mst_report_type rt ON rt.report_type_id = r.report_type_id "
				+"			LEFT JOIN wo_trc_rmd_pic_followup rpf ON rpf.rmd_id = r.rmd_id "
				+"		where (rc.rmd_id) IS NULL ");
		
//		sb = getQueryWhereStringByDateAndJenisKetentuan(sb, searchCriteria);
		sb = getQueryWhereString(sb, searchCriteria);

//		if (!(senderNameIn.isEmpty()) && senderNameIn != null) {
//			sb.append(" and dtr.document_type_in = '" + senderNameIn + "' ");
//		} else if (!(senderNameEn.isEmpty()) && senderNameEn != null) {
//			sb.append(" and dtr.document_type_en = '" + senderNameEn + "' ");
//		} else if (!(docNameIn.isEmpty()) && docNameIn != null) {
//			sb.append(" and dtr.document_type_in = '" + docNameIn + "' ");
//		} else if (!(docNameEn.isEmpty()) && docNameEn != null) {
//			sb.append(" and dtr.document_type_en = '" + docNameEn + "' ");
//		}
		
		sb.append("		group by dtr.document_type_in, dtr.document_type_en "
				+"	UNION ALL "
				+"		select sndr.name_in AS jenis_peraturan_in "
				+"			,sndr.name_en AS jenis_peraturan_en "
				+"			,coalesce(sum(case when lower(rt.report_type_in) like '%bulanan%' then 1 end), 0) as Bulanan "
				+"			,coalesce(sum(case when lower(rt.report_type_in) like '%tahunan%' then 1 end), 0) as Tahunan "
				+"			,coalesce(sum(case when lower(rt.report_type_in) like '%triwulanan%' then 1 end), 0) as Triwulanan "
				+"			,coalesce(sum(case when lower(rt.report_type_in) like '%semester%' then 1 end), 0) as Semester "
				+"			,coalesce(sum(case when lower(rt.report_type_in) like '%insidentil%' then 1 end), 0) as Insidentil "
				+"			,coalesce(sum(case when lower(rt.report_type_in) like '%mingguan%' then 1 end), 0) as Mingguan "
				+"			,coalesce(sum(case when lower(rt.report_type_in) like '%harian%' then 1 end), 0) as Harian "
				+"			,count(1) jumlah_laporan"
				+"			,SUM(CASE WHEN rpf.followup_status IS NULL THEN 1 ELSE 0 END) in_progress"
				+"			,SUM(CASE WHEN rpf.followup_status IS NOT NULL THEN 1 ELSE 0 END) closed"
				+"			,SUM(CASE WHEN rpf.followup_status IS NOT NULL AND rpf.followup_date = rpf.target_date THEN 1 ELSE 0 END) meet_sla "
				+"			,SUM(CASE WHEN rpf.followup_status IS NOT NULL AND rpf.followup_date < rpf.target_date THEN 1 ELSE 0 END) before_sla "
				+"			,SUM(CASE WHEN rpf.followup_status IS NOT NULL AND rpf.followup_date > rpf.target_date THEN 1 ELSE 0 END) over_sla "
				+"		from wo_trc_rmd r "
				+ "		LEFT JOIN ( select * "
				+ "				    from wo_trc_rmd_correspondence rc "
				+ "					where 1 = 1 "
				+ "						and rc.rmd_correspondence_id =  ( select min(rc.rmd_correspondence_id) "
				+ "													  	  from wo_trc_rmd_correspondence rc1 "
				+ "													  	  where rc1.rmd_id = rc.rmd_id) ) rc "
				+ "			ON (rc.rmd_id = r.rmd_id) "
//				+"			JOIN wo_trc_rmd_correspondence rc ON (rc.rmd_id = r.rmd_id) and rc.rmd_correspondence_id = (select min(rc1.rmd_correspondence_id) from wo_trc_rmd_correspondence rc1 where rc1.rmd_id = rc.rmd_id) "
				+"			LEFT JOIN wo_trc_rmd_regulation rr ON rr.rmd_id = r.rmd_id "
				+"			LEFT JOIN wo_mst_regulation rrr ON (rrr.regulation_id = rr.regulation_id) "
				+"			LEFT JOIN wo_trc_correspondence c ON (c.correspondence_id = rc.correspondence_id) "
				+"			LEFT JOIN wo_mst_parameter_dtl sndr ON (" //sndr.parameter_code = 'SENDER' AND "
				+"				sndr.parameter_dtl_code = c.sender_code) "
				+"			LEFT JOIN wo_mst_report_type rt ON rt.report_type_id = r.report_type_id "
				+"			LEFT JOIN wo_trc_rmd_pic_followup rpf ON rpf.rmd_id = r.rmd_id "
				+"		where (rr.rmd_id) IS NULL ");
		
//		sb = getQueryWhereStringByDateAndSenderCode(sb, searchCriteria);
		sb = getQueryWhereString(sb, searchCriteria);
		
//		if (!(senderNameIn.isEmpty()) && senderNameIn != null) {
//			sb.append(" and sndr.name_in = '" + senderNameIn + "' ");
//		} else if (!(senderNameEn.isEmpty()) && senderNameEn != null) {
//			sb.append(" and sndr.name_en = '" + senderNameEn + "' ");
//		} else if (!(docNameIn.isEmpty()) && docNameIn != null) {
//			sb.append(" and sndr.name_in = '" + docNameIn + "' ");
//		} else if (!(docNameEn.isEmpty()) && docNameEn != null) {
//			sb.append(" and sndr.name_en = '" + docNameEn + "' ");
//		}
		
		sb.append("		group by sndr.name_in, sndr.name_en "
				+"	UNION ALL "
				+"		select CONCAT(dtr.document_type_in, sndr.name_in) AS jenis_peraturan_in "
				+"			,CONCAT(dtr.document_type_en, sndr.name_en) AS jenis_peraturan_en "
//				+"		select CONCAT(dtr.document_type_in, '/', sndr.name_in) AS jenis_peraturan_in "
//				+"			,CONCAT(dtr.document_type_en, '/', sndr.name_en) AS jenis_peraturan_en "
				+"			,coalesce(sum(case when lower(rt.report_type_in) like '%bulanan%' then 1 end), 0) as Bulanan "
				+"			,coalesce(sum(case when lower(rt.report_type_in) like '%tahunan%' then 1 end), 0) as Tahunan "
				+"			,coalesce(sum(case when lower(rt.report_type_in) like '%triwulanan%' then 1 end), 0) as Triwulanan "
				+"			,coalesce(sum(case when lower(rt.report_type_in) like '%semester%' then 1 end), 0) as Semester "
				+"			,coalesce(sum(case when lower(rt.report_type_in) like '%insidentil%' then 1 end), 0) as Insidentil "
				+"			,coalesce(sum(case when lower(rt.report_type_in) like '%mingguan%' then 1 end), 0) as Mingguan "
				+"			,coalesce(sum(case when lower(rt.report_type_in) like '%harian%' then 1 end), 0) as Harian "
				+"			,count(1) jumlah_laporan"
				+"			,SUM(CASE WHEN rpf.followup_status IS NULL THEN 1 ELSE 0 END) in_progress"
				+"			,SUM(CASE WHEN rpf.followup_status IS NOT NULL THEN 1 ELSE 0 END) closed"
				+"			,SUM(CASE WHEN rpf.followup_status IS NOT NULL AND rpf.followup_date = rpf.target_date THEN 1 ELSE 0 END) meet_sla "
				+"			,SUM(CASE WHEN rpf.followup_status IS NOT NULL AND rpf.followup_date < rpf.target_date THEN 1 ELSE 0 END) before_sla "
				+"			,SUM(CASE WHEN rpf.followup_status IS NOT NULL AND rpf.followup_date > rpf.target_date THEN 1 ELSE 0 END) over_sla "
				+"		from wo_trc_rmd r "
				+ "		LEFT JOIN ( select * "
				+ "				    from wo_trc_rmd_correspondence rc "
				+ "					where 1 = 1 "
				+ "						and rc.rmd_correspondence_id =  ( select min(rc.rmd_correspondence_id) "
				+ "													  	  from wo_trc_rmd_correspondence rc1 "
				+ "													  	  where rc1.rmd_id = rc.rmd_id) ) rc "
				+ "			ON (rc.rmd_id = r.rmd_id) "
				+ "		LEFT JOIN ( select * "
				+ "					from wo_trc_rmd_regulation rr "
				+ "					where 1 = 1 "
				+ "						and rr.rmd_regulation_id = ( select min(rr1.rmd_regulation_id) "
				+ "						from wo_trc_rmd_regulation rr1 "
				+ "						where rr1.rmd_id = rr.rmd_id) ) rr "
				+ "			ON (rr.rmd_id = r.rmd_id) "
//				+"			JOIN wo_trc_rmd_correspondence rc ON (rc.rmd_id = r.rmd_id) and rc.rmd_correspondence_id = (select min(rc1.rmd_correspondence_id) from wo_trc_rmd_correspondence rc1 where rc1.rmd_id = rc.rmd_id) "
//				+"			JOIN wo_trc_rmd_regulation rr ON (rr.rmd_id = r.rmd_id) and rr.rmd_regulation_id = (select min(rr1.rmd_regulation_id) from wo_trc_rmd_regulation rr1 where rr1.rmd_id = rr.rmd_id) "
				+"			LEFT JOIN wo_mst_regulation rrr ON (rrr.regulation_id = rr.regulation_id) "
				+"			LEFT JOIN wo_mst_document_type dtr ON (dtr.document_type_id = rrr.document_type_id) "
				+"			LEFT JOIN wo_trc_correspondence c ON (c.correspondence_id = rc.correspondence_id) "
				+"			LEFT JOIN wo_mst_parameter_dtl sndr ON (" //sndr.parameter_code = 'SENDER' AND "
				+"				sndr.parameter_dtl_code = c.sender_code) "
				+"			LEFT JOIN wo_mst_report_type rt ON rt.report_type_id = r.report_type_id "
				+"			LEFT JOIN wo_trc_rmd_pic_followup rpf ON rpf.rmd_id = r.rmd_id "
				+"		where 1=1 ");
		
		sb = getQueryWhereString(sb, searchCriteria);
		
		sb.append("		group by dtr.document_type_in, dtr.document_type_en, sndr.name_in, sndr.name_en "
				+ "	) t "
				+ "group by t.jenis_peraturan_in, t.jenis_peraturan_en ");
		
		Query query = getSession().createSQLQuery(sb.toString());
		
		List resultList = query.getResultList();
		
		List<ReportRmdRekap> vo = new ArrayList<ReportRmdRekap>();
		
		if (resultList != null) {
			for (int i = 0; i < resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
				ReportRmdRekap data = new ReportRmdRekap();
				
				data.setJenisPeraturanIn(obj[0] != null ? (String) obj[0] : null);
				data.setJenisPeraturanEn(obj[1] != null ? (String) obj[1] : null);
				data.setBulanan(obj[2] != null ? (((BigDecimal) obj[2]).toBigInteger()).intValue() : null);
				data.setTahunan(obj[3] != null ? (((BigDecimal) obj[3]).toBigInteger()).intValue() : null);
				data.setTriwulanan(obj[4] != null ? (((BigDecimal) obj[4]).toBigInteger()).intValue() : null);
				data.setSemester(obj[5] != null ? (((BigDecimal) obj[5]).toBigInteger()).intValue() : null);
				data.setInsidentil(obj[6] != null ? (((BigDecimal) obj[6]).toBigInteger()).intValue() : null);
				data.setMingguan(obj[7] != null ? (((BigDecimal) obj[7]).toBigInteger()).intValue() : null);
				data.setHarian(obj[8] != null ? (((BigDecimal) obj[8]).toBigInteger()).intValue() : null);
				data.setJumlahLaporan(obj[9] != null ? (((BigDecimal) obj[9]).toBigInteger()).intValue() : null);
				data.setInProgress(obj[10] != null ? (((BigDecimal) obj[10]).toBigInteger()).intValue() : null);
				data.setClosed(obj[11] != null ? (((BigDecimal) obj[11]).toBigInteger()).intValue() : null);
				data.setMeetSla(obj[12] != null ? (((BigDecimal) obj[12]).toBigInteger()).intValue() : null);
				data.setBeforeSla(obj[13] != null ? (((BigDecimal) obj[13]).toBigInteger()).intValue() : null);
				data.setOverSla(obj[14] != null ? (((BigDecimal) obj[14]).toBigInteger()).intValue() : null);
				
				vo.add(data);
			}
		}
		
		return vo;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<Object[]> getReportRmdRekapAsPeraturanOrJenisSameObj(List<? extends SearchObject> searchCriteria, String docNameEn, String docNameIn, String senderNameEn, String senderNameIn) {
StringBuilder sb = new StringBuilder();
		
		sb.append(" select t.jenis_peraturan_in,t.jenis_peraturan_en "
				+"		,sum(t.Bulanan) Bulanan "
				+"		,sum(t.Tahunan) Tahunan "
				+"		,sum(t.Triwulanan) Triwulanan "
				+"		,sum(t.Semester) Semester "
				+"		,sum(t.Insidentil) Insidentil "
				+"		,sum(t.Mingguan) Mingguan "
				+"		,sum(t.Harian) Harian "
				+"		,sum(t.jumlah_laporan) jumlah_laporan "
				+"		,sum(t.in_progress) in_progress "
				+"		,sum(t.closed) closed "
				+"		,sum(t.meet_sla) meet_sla "
				+"		,sum(t.before_sla) before_sla "
				+"		,sum(t.over_sla) over_sla "
				+"	from ( "
				+"		select dtr.document_type_in AS jenis_peraturan_in, "
				+"			dtr.document_type_en AS jenis_peraturan_en "
				+"			,coalesce(sum(case when lower(rt.report_type_in) like '%bulanan%' then 1 end), 0) as Bulanan "
				+"			,coalesce(sum(case when lower(rt.report_type_in) like '%tahunan%' then 1 end), 0) as Tahunan "
				+"			,coalesce(sum(case when lower(rt.report_type_in) like '%triwulanan%' then 1 end), 0) as Triwulanan "
				+"			,coalesce(sum(case when lower(rt.report_type_in) like '%semester%' then 1 end), 0) as Semester "
				+"			,coalesce(sum(case when lower(rt.report_type_in) like '%insidentil%' then 1 end), 0) as Insidentil "
				+"			,coalesce(sum(case when lower(rt.report_type_in) like '%mingguan%' then 1 end), 0) as Mingguan "
				+"			,coalesce(sum(case when lower(rt.report_type_in) like '%harian%' then 1 end), 0) as Harian "
				+"			,count(1) jumlah_laporan "
				+"			,SUM(CASE WHEN rpf.followup_status IS NULL THEN 1 ELSE 0 END) in_progress "
				+"			,SUM(CASE WHEN rpf.followup_status IS NOT NULL THEN 1 ELSE 0 END) closed "
				+"			,SUM(CASE WHEN rpf.followup_status IS NOT NULL AND rpf.followup_date = rpf.target_date THEN 1 ELSE 0 END) meet_sla "
				+"			,SUM(CASE WHEN rpf.followup_status IS NOT NULL AND rpf.followup_date < rpf.target_date THEN 1 ELSE 0 END) before_sla "
				+"			,SUM(CASE WHEN rpf.followup_status IS NOT NULL AND rpf.followup_date > rpf.target_date THEN 1 ELSE 0 END) over_sla "
				+"		from wo_trc_rmd r "
				+"			JOIN wo_trc_rmd_regulation rr ON (rr.rmd_id = r.rmd_id) and rr.rmd_regulation_id = (select min(rr1.rmd_regulation_id) from wo_trc_rmd_regulation rr1 where rr1.rmd_id = rr.rmd_id) "
				+"			LEFT JOIN wo_trc_rmd_correspondence rc ON rc.rmd_id = r.rmd_id "
				+"			LEFT JOIN wo_trc_correspondence c ON (c.correspondence_id = rc.correspondence_id) "
				+"			LEFT JOIN wo_mst_regulation rrr ON (rrr.regulation_id = rr.regulation_id) "
				+"			LEFT JOIN wo_mst_document_type dtr ON (dtr.document_type_id = rrr.document_type_id) "
				+"			LEFT JOIN wo_mst_report_type rt ON rt.report_type_id = r.report_type_id "
				+"			LEFT JOIN wo_trc_rmd_pic_followup rpf ON rpf.rmd_id = r.rmd_id "
				+"		where ISNULL(rc.rmd_id) ");
		
//		sb = getQueryWhereStringByDateAndJenisKetentuan(sb, searchCriteria);
		sb = getQueryWhereString(sb, searchCriteria);

//		if (!(senderNameIn.isEmpty()) && senderNameIn != null) {
//			sb.append(" and dtr.document_type_in = '" + senderNameIn + "' ");
//		} else if (!(senderNameEn.isEmpty()) && senderNameEn != null) {
//			sb.append(" and dtr.document_type_en = '" + senderNameEn + "' ");
//		} else if (!(docNameIn.isEmpty()) && docNameIn != null) {
//			sb.append(" and dtr.document_type_in = '" + docNameIn + "' ");
//		} else if (!(docNameEn.isEmpty()) && docNameEn != null) {
//			sb.append(" and dtr.document_type_en = '" + docNameEn + "' ");
//		}
		
		sb.append("		group by dtr.document_type_in, dtr.document_type_en "
				+"	UNION ALL "
				+"		select sndr.name_in AS jenis_peraturan_in "
				+"			,sndr.name_en AS jenis_peraturan_en "
				+"			,coalesce(sum(case when lower(rt.report_type_in) like '%bulanan%' then 1 end), 0) as Bulanan "
				+"			,coalesce(sum(case when lower(rt.report_type_in) like '%tahunan%' then 1 end), 0) as Tahunan "
				+"			,coalesce(sum(case when lower(rt.report_type_in) like '%triwulanan%' then 1 end), 0) as Triwulanan "
				+"			,coalesce(sum(case when lower(rt.report_type_in) like '%semester%' then 1 end), 0) as Semester "
				+"			,coalesce(sum(case when lower(rt.report_type_in) like '%insidentil%' then 1 end), 0) as Insidentil "
				+"			,coalesce(sum(case when lower(rt.report_type_in) like '%mingguan%' then 1 end), 0) as Mingguan "
				+"			,coalesce(sum(case when lower(rt.report_type_in) like '%harian%' then 1 end), 0) as Harian "
				+"			,count(1) jumlah_laporan"
				+"			,SUM(CASE WHEN rpf.followup_status IS NULL THEN 1 ELSE 0 END) in_progress"
				+"			,SUM(CASE WHEN rpf.followup_status IS NOT NULL THEN 1 ELSE 0 END) closed"
				+"			,SUM(CASE WHEN rpf.followup_status IS NOT NULL AND rpf.followup_date = rpf.target_date THEN 1 ELSE 0 END) meet_sla "
				+"			,SUM(CASE WHEN rpf.followup_status IS NOT NULL AND rpf.followup_date < rpf.target_date THEN 1 ELSE 0 END) before_sla "
				+"			,SUM(CASE WHEN rpf.followup_status IS NOT NULL AND rpf.followup_date > rpf.target_date THEN 1 ELSE 0 END) over_sla "
				+"		from wo_trc_rmd r "
				+"			JOIN wo_trc_rmd_correspondence rc ON (rc.rmd_id = r.rmd_id) and rc.rmd_correspondence_id = (select min(rc1.rmd_correspondence_id) from wo_trc_rmd_correspondence rc1 where rc1.rmd_id = rc.rmd_id) "
				+"			LEFT JOIN wo_trc_rmd_regulation rr ON rr.rmd_id = r.rmd_id "
				+"			LEFT JOIN wo_mst_regulation rrr ON (rrr.regulation_id = rr.regulation_id) "
				+"			LEFT JOIN wo_trc_correspondence c ON (c.correspondence_id = rc.correspondence_id) "
				+"			LEFT JOIN wo_mst_parameter_dtl sndr ON (" //sndr.parameter_code = 'SENDER' AND "
				+"				sndr.parameter_dtl_code = c.sender_code) "
				+"			LEFT JOIN wo_mst_report_type rt ON rt.report_type_id = r.report_type_id "
				+"			LEFT JOIN wo_trc_rmd_pic_followup rpf ON rpf.rmd_id = r.rmd_id "
				+"		where ISNULL(rr.rmd_id) ");
		
//		sb = getQueryWhereStringByDateAndSenderCode(sb, searchCriteria);
		sb = getQueryWhereString(sb, searchCriteria);
		
//		if (!(senderNameIn.isEmpty()) && senderNameIn != null) {
//			sb.append(" and sndr.name_in = '" + senderNameIn + "' ");
//		} else if (!(senderNameEn.isEmpty()) && senderNameEn != null) {
//			sb.append(" and sndr.name_en = '" + senderNameEn + "' ");
//		} else if (!(docNameIn.isEmpty()) && docNameIn != null) {
//			sb.append(" and sndr.name_in = '" + docNameIn + "' ");
//		} else if (!(docNameEn.isEmpty()) && docNameEn != null) {
//			sb.append(" and sndr.name_en = '" + docNameEn + "' ");
//		}
		
		sb.append("		group by sndr.name_in, sndr.name_en "
				+"	UNION ALL "
				+"		select CONCAT(dtr.document_type_in, '/', sndr.name_in) AS jenis_peraturan_in "
				+"			,CONCAT(dtr.document_type_en, '/', sndr.name_en) AS jenis_peraturan_en "
				+"			,coalesce(sum(case when lower(rt.report_type_in) like '%bulanan%' then 1 end), 0) as Bulanan "
				+"			,coalesce(sum(case when lower(rt.report_type_in) like '%tahunan%' then 1 end), 0) as Tahunan "
				+"			,coalesce(sum(case when lower(rt.report_type_in) like '%triwulanan%' then 1 end), 0) as Triwulanan "
				+"			,coalesce(sum(case when lower(rt.report_type_in) like '%semester%' then 1 end), 0) as Semester "
				+"			,coalesce(sum(case when lower(rt.report_type_in) like '%insidentil%' then 1 end), 0) as Insidentil "
				+"			,coalesce(sum(case when lower(rt.report_type_in) like '%mingguan%' then 1 end), 0) as Mingguan "
				+"			,coalesce(sum(case when lower(rt.report_type_in) like '%harian%' then 1 end), 0) as Harian "
				+"			,count(1) jumlah_laporan"
				+"			,SUM(CASE WHEN rpf.followup_status IS NULL THEN 1 ELSE 0 END) in_progress"
				+"			,SUM(CASE WHEN rpf.followup_status IS NOT NULL THEN 1 ELSE 0 END) closed"
				+"			,SUM(CASE WHEN rpf.followup_status IS NOT NULL AND rpf.followup_date = rpf.target_date THEN 1 ELSE 0 END) meet_sla "
				+"			,SUM(CASE WHEN rpf.followup_status IS NOT NULL AND rpf.followup_date < rpf.target_date THEN 1 ELSE 0 END) before_sla "
				+"			,SUM(CASE WHEN rpf.followup_status IS NOT NULL AND rpf.followup_date > rpf.target_date THEN 1 ELSE 0 END) over_sla "
				+"		from wo_trc_rmd r "
				+"			JOIN wo_trc_rmd_correspondence rc ON (rc.rmd_id = r.rmd_id) and rc.rmd_correspondence_id = (select min(rc1.rmd_correspondence_id) from wo_trc_rmd_correspondence rc1 where rc1.rmd_id = rc.rmd_id) "
				+"			JOIN wo_trc_rmd_regulation rr ON (rr.rmd_id = r.rmd_id) and rr.rmd_regulation_id = (select min(rr1.rmd_regulation_id) from wo_trc_rmd_regulation rr1 where rr1.rmd_id = rr.rmd_id) "
				+"			LEFT JOIN wo_mst_regulation rrr ON (rrr.regulation_id = rr.regulation_id) "
				+"			LEFT JOIN wo_mst_document_type dtr ON (dtr.document_type_id = rrr.document_type_id) "
				+"			LEFT JOIN wo_trc_correspondence c ON (c.correspondence_id = rc.correspondence_id) "
				+"			LEFT JOIN wo_mst_parameter_dtl sndr ON (" //sndr.parameter_code = 'SENDER' AND "
				+"				sndr.parameter_dtl_code = c.sender_code) "
				+"			LEFT JOIN wo_mst_report_type rt ON rt.report_type_id = r.report_type_id "
				+"			LEFT JOIN wo_trc_rmd_pic_followup rpf ON rpf.rmd_id = r.rmd_id "
				+"		where 1=1 ");
		
		sb = getQueryWhereString(sb, searchCriteria);
		
		sb.append("		group by dtr.document_type_in, dtr.document_type_en, sndr.name_in, sndr.name_en "
				+ "	) t "
				+ "group by t.jenis_peraturan_in, t.jenis_peraturan_en ");
		
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
