/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.header.dao;

import java.util.Date;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.qa.model.QA;

/**
 *
 * @author hendra
 * 
 *         Modification Alex
 */

@Repository("headerFrontEndDao")
public class HeaderFrontEndDaoImpl extends GenericDAOHibernate<QA, Long> implements HeaderFrontEndDao {

	
	@Override
	public Long getCountPertanyaanBelumDijawab(String nik) throws Exception {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		sb.append(" select count(1) from wo_mst_qna q, wo_mst_user u where q.enabled_flag = 'Y' and q.q_user_id = u.user_id and q.q_status ='QNA_STATUS_NEW' and q.from_qna_id is null and ((q.answer is null and u.nik = :nik) OR EXISTS(select 1 from wo_mst_qna q2 inner join wo_mst_user u2 on u2.user_id= q2.q_user_id where q2.answer is null and q2.from_qna_id is not null and q2.from_qna_id = q.qna_id and u2.nik = :nik))"
				+ "  ");

		Query query = getSession().createSQLQuery(sb.toString());


		query.setParameter("nik", nik);

		return ((Number) query.getSingleResult()).longValue();
	}
	
	public Date getMaxDatePertanyaanBelumDijawab(String nik) throws Exception {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		sb.append(" select max(q_date) from wo_mst_qna q, wo_mst_user u where q.enabled_flag = 'Y' and q.q_user_id = u.user_id and u.nik = :nik and q.answer is null  "
				+ "  ");

		Query query = getSession().createSQLQuery(sb.toString());


		query.setParameter("nik", nik);

		return ((Date) query.getSingleResult());
	}

	@Override
	public Long getCountPertanyaanSudahDijawab(String nik) throws Exception {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		sb.append(" select count(1) from wo_mst_qna q, wo_mst_user u where q.enabled_flag = 'Y' and q.q_user_id = u.user_id and u.nik = :nik and q.q_status ='QNA_STATUS_ANSWERED' and q.from_qna_id is null and ((q.answer is not null and u.nik = :nik and q.read_flag = 'N') OR EXISTS(select 1 from wo_mst_qna q2 inner join wo_mst_user u2 on u2.user_id= q2.q_user_id where q2.answer is not null and q2.from_qna_id is not null and q2.from_qna_id = q.qna_id and u2.nik = :nik and q2.read_flag = 'N'))"
				+ "  ");

		Query query = getSession().createSQLQuery(sb.toString());


		query.setParameter("nik", nik);

		return ((Number) query.getSingleResult()).longValue();
	}
	
	
	public Date getMaxDatePertanyaanSudahDijawab(String nik) throws Exception {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		sb.append(" select max(q_date) from wo_mst_qna q, wo_mst_user u where q.enabled_flag = 'Y' and q.q_user_id = u.user_id and u.nik = :nik and q.answer is not null and q.read_flag = 'N' "
				+ "  ");

		Query query = getSession().createSQLQuery(sb.toString());


		query.setParameter("nik", nik);

		return ((Date) query.getSingleResult());
	}

	@Override
	public Long getCountPertanyaanPerluDijawab(String nik) throws Exception {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		sb.append(" select count(1) from wo_mst_qna q where q.enabled_flag = 'Y' AND Q_STATUS <> 'QNA_STATUS_CLOSE' and q.from_qna_id is null and ((q.answer is null and exists (select 1 from wo_mst_qna_category_map c, wo_mst_user u where c.user_id = u.user_id and u.nik = :nik and q.category_type = c.qna_category_code)) OR EXISTS(select 1 from wo_mst_qna q2  where q2.answer is null and q2.from_qna_id is not null and q2.from_qna_id = q.qna_id and exists (select 1 from wo_mst_qna_category_map c, wo_mst_user u where c.user_id = u.user_id and u.nik = :nik and q2.category_type = c.qna_category_code) )) ");

		Query query = getSession().createSQLQuery(sb.toString());


		query.setParameter("nik", nik);

		return ((Number) query.getSingleResult()).longValue();
	}
	
	public Date getMaxDatePertanyaanPerluDijawab(String nik) throws Exception {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		sb.append(" select max(q_date) from wo_mst_qna q where q.enabled_flag = 'Y' and q.answer is null and exists (select 1 from wo_mst_qna_category_map c, wo_mst_user u where c.user_id = u.user_id and u.nik = :nik and q.category_type = c.qna_category_code) ");

		Query query = getSession().createSQLQuery(sb.toString());


		query.setParameter("nik", nik);

		return ((Date) query.getSingleResult());
	}

	@Override
	public Long getCountPertanyaanPerluDitutup(String nik) throws Exception {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		sb.append(" select count(1) from wo_mst_qna q where q.enabled_flag = 'Y'  and q_status = 'QNA_STATUS_ANSWERED' and q.from_qna_id is null and q.answer is not null and exists (select 1 from wo_mst_qna_category_map c, wo_mst_user u where c.user_id = u.user_id and u.nik = :nik and q.category_type = c.qna_category_code) and read_flag = 'Y'  and  sysdate >= (q.last_update_date +1) ");

		Query query = getSession().createSQLQuery(sb.toString());


		query.setParameter("nik", nik);

		return ((Number) query.getSingleResult()).longValue();
	}
	
	public Date getMaxDatePertanyaanPerluDitutup(String nik) throws Exception {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		sb.append(" select max(q.last_update_date) from wo_mst_qna q where q.enabled_flag = 'Y' and q.answer is not null and q_status = 'QNA_STATUS_ANSWERED' and q.from_qna_id is null and exists (select 1 from wo_mst_qna_category_map c, wo_mst_user u where c.user_id = u.user_id and u.nik = :nik and q.category_type = c.qna_category_code) and read_flag = 'Y' and sysdate >= (q.last_update_date +1) ");

		Query query = getSession().createSQLQuery(sb.toString());


		query.setParameter("nik", nik);

		return ((Date) query.getSingleResult());
	}

	@Override
	public Long getCountTindakLanjutSosialisasi(String nik) throws Exception {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		sb.append(" select count(1) from wo_trc_socialization s, wo_trc_socialization_pic_fp f where f.socialization_id = s.socialization_id and s.enabled_flag = 'Y' and s.status = 'DATA_ACTIVE' and s.follow_up = 'Y' and (f.followup_status is null OR f.followup_status <> 'PIC_DONE') and exists (select 1 from wo_mst_user u where u.nik = :nik and (u.user_id = f.user_id_1 or u.user_id = f.user_id_2 or u.user_id = f.user_id_3)) ");

		Query query = getSession().createSQLQuery(sb.toString());


		query.setParameter("nik", nik);

		return ((Number) query.getSingleResult()).longValue();
	}
	
	public Date getMaxDateTindakLanjutSosialisasi(String nik) throws Exception {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		sb.append(" select max(s.creation_date) from wo_trc_socialization s, wo_trc_socialization_pic_fp f where f.socialization_id = s.socialization_id and s.enabled_flag = 'Y' and s.status = 'DATA_ACTIVE' and s.follow_up = 'Y' and (f.followup_status is null OR f.followup_status <> 'PIC_DONE') and exists (select 1 from wo_mst_user u where u.nik = :nik and (u.user_id = f.user_id_1 or u.user_id = f.user_id_2 or u.user_id = f.user_id_3)) ");

		Query query = getSession().createSQLQuery(sb.toString());


		query.setParameter("nik", nik);

		return ((Date) query.getSingleResult());
	}

	@Override
	public Long getCountTindakLanjutDenda(String nik) throws Exception {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		sb.append(" select count(1) from wo_trc_fine s, wo_trc_fine_pic_followup f where f.fine_id = s.fine_id and s.enabled_flag = 'Y' and s.status = 'DATA_ACTIVE' and (f.followup_status is null OR f.followup_status = 'PIC_INPROGRESS') and exists (select 1 from wo_mst_user u where u.nik = :nik and (u.user_id = f.user_id_1 or u.user_id = f.user_id_2 or u.user_id = f.user_id_3)) ");

		Query query = getSession().createSQLQuery(sb.toString());


		query.setParameter("nik", nik);

		return ((Number) query.getSingleResult()).longValue();
	}
	
	public Date getMaxDateTindakLanjutDenda(String nik) throws Exception {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		sb.append(" select max(f.creation_date) from wo_trc_fine s, wo_trc_fine_pic_followup f where f.fine_id = s.fine_id and s.enabled_flag = 'Y' and s.status = 'DATA_ACTIVE' and s.follow_up = 'Y' and (f.followup_status is null OR f.followup_status = 'PIC_INPROGRESS') and exists (select 1 from wo_mst_user u where u.nik = :nik and (u.user_id = f.user_id_1 or u.user_id = f.user_id_2 or u.user_id = f.user_id_3)) ");

		Query query = getSession().createSQLQuery(sb.toString());


		query.setParameter("nik", nik);

		return ((Date) query.getSingleResult());
	}

	@Override
	public Long getCountTindakLanjutSuratMasuk(String nik) throws Exception {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		//sb.append(" select count(1) from wo_trc_correspondence s where s.enabled_flag = 'Y' and s.status = 'DATA_ACTIVE' and s.follow_up = 'Y' AND (CORRESPONDENCE_TYPE = 'INVITATION' OR (s.followup_status is null OR s.followup_status <> 'PIC_DONE')) AND (CORRESPONDENCE_TYPE = 'NONINVITATION' OR s.followup_status is null or s.pic_followup_status is null) and exists (select 1 from wo_mst_user u where u.nik = :nik and (u.user_id = s.user_id_1 or u.user_id = s.user_id_2 or u.user_id = s.user_id_3)) ");
		sb.append(" select count(1) ");
		sb.append("   from wo_trc_correspondence s where s.enabled_flag = 'Y' and s.status = 'DATA_ACTIVE' and s.follow_up = 'Y' ");
		sb.append("        AND (CORRESPONDENCE_TYPE = 'INVITATION' OR (s.followup_status is null OR s.followup_status <> 'PIC_DONE')) ");
		sb.append("        AND (CORRESPONDENCE_TYPE = 'NONINVITATION' OR s.followup_status is null or s.pic_followup_status is null) ");				
		sb.append("        AND EXISTS");
		sb.append("            (SELECT 1 ");
		sb.append("               FROM WO_TRC_CRPDC_PIC_CONFIRM ucpc ");
		sb.append("                    INNER JOIN WO_MST_USER us1 ON ucpc.user_id_1 = us1.USER_ID ");
		sb.append("              WHERE us1.nik = :nik ");
		sb.append("                    AND s.correspondence_id = ucpc.correspondence_id) ");
		
		Query query = getSession().createSQLQuery(sb.toString());


		query.setParameter("nik", nik);

		return ((Number) query.getSingleResult()).longValue();
	}
	
	public Date getMaxDateTindakLanjutSuratMasuk(String nik) throws Exception {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		sb.append(" select max(s.creation_date) from wo_trc_correspondence s where s.enabled_flag = 'Y' and s.status = 'DATA_ACTIVE' and s.follow_up = 'Y' and (s.followup_status is null OR s.followup_status <> 'PIC_DONE') and exists (select 1 from wo_mst_user u where u.nik = :nik and (u.user_id = s.user_id_1 or u.user_id = s.user_id_2 or u.user_id = s.user_id_3)) ");

		Query query = getSession().createSQLQuery(sb.toString());


		query.setParameter("nik", nik);

		return ((Date) query.getSingleResult());
	}

	@Override
	public Long getCountTindakLanjutRegulatoryReport(String nik) throws Exception {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		sb.append(" select count(1) from wo_trc_rmd s, wo_trc_rmd_pic_followup f where f.rmd_id = s.rmd_id and s.enabled_flag = 'Y' and f.enabled_flag = 'Y' and s.status = 'DATA_ACTIVE'  and (f.followup_status is null OR f.followup_status <> 'PIC_DONE') and exists (select 1 from wo_mst_user u where u.nik = :nik and (u.user_id = s.user_id_1 or u.user_id = s.user_id_2 or u.user_id = s.user_id_3)) ");

		Query query = getSession().createSQLQuery(sb.toString());


		query.setParameter("nik", nik);

		return ((Number) query.getSingleResult()).longValue();
	}
	
	public Date getMaxDateTindakLanjutRegulatoryReport(String nik) throws Exception {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		sb.append(" select max(s.creation_date) from wo_trc_rmd s, wo_trc_rmd_pic_followup f where f.rmd_id = s.rmd_id and s.enabled_flag = 'Y' and s.status = 'DATA_ACTIVE'  and (f.followup_status is null OR f.followup_status <> 'PIC_DONE') and exists (select 1 from wo_mst_user u where u.nik = :nik and (u.user_id = s.user_id_1 or u.user_id = s.user_id_2 or u.user_id = s.user_id_3)) ");

		Query query = getSession().createSQLQuery(sb.toString());


		query.setParameter("nik", nik);

		return ((Date) query.getSingleResult());
	}

	@Override
	public Long getCountTindakLanjutAudit(String nik) throws Exception {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT " + 
				"	count(1) " + 
				"FROM " + 
				"	wo_trc_audit_pic_followup f " + 
				"	INNER JOIN WO_TRC_AUDIT_PC_FP_BANK_CMITMT c ON c.AUD_PC_FP_BANK_CMITMT_ID = f.AUD_PC_FP_BANK_CMITMT_ID  " + 
				"	INNER JOIN WO_TRC_AUDIT_CHECK_POINT p ON p.AUDIT_CHECK_POINT_ID = c.AUDIT_CHECK_POINT_ID  " + 
				"	INNER JOIN WO_TRC_AUDIT s ON s.AUDIT_ID = p.AUDIT_ID  " + 
				"WHERE " + 
				"	s.enabled_flag = 'Y' " + 
				"	AND s.status = 'DATA_ACTIVE' " + 
				"	AND s.follow_up = 'Y' " + 
				"	AND (f.followup_status IS NULL " + 
				"	OR f.followup_status NOT IN ('PIC_DONE', 'PIC_EXTENSION')) " + 
				"	AND EXISTS ( " + 
				"	SELECT " + 
				"		1 " + 
				"	FROM " + 
				"		wo_mst_user u " + 
				"	WHERE " + 
				"		u.nik = :nik " + 
				"		AND (u.user_id = f.user_id_1 " + 
				"		OR u.user_id = f.user_id_2 " + 
				"		OR u.user_id = f.user_id_3)) ");

		Query query = getSession().createSQLQuery(sb.toString());


		query.setParameter("nik", nik);

		return ((Number) query.getSingleResult()).longValue();
	}
	
	public Date getMaxDateTindakLanjutAudit(String nik) throws Exception {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT " + 
				"	max(f.creation_date) " + 
				"FROM " + 
				"	wo_trc_audit_pic_followup f " + 
				"	INNER JOIN WO_TRC_AUDIT_PC_FP_BANK_CMITMT c ON c.AUD_PC_FP_BANK_CMITMT_ID = f.AUD_PC_FP_BANK_CMITMT_ID  " + 
				"	INNER JOIN WO_TRC_AUDIT_CHECK_POINT p ON p.AUDIT_CHECK_POINT_ID = c.AUDIT_CHECK_POINT_ID  " + 
				"	INNER JOIN WO_TRC_AUDIT s ON s.AUDIT_ID = p.AUDIT_ID  " + 
				"WHERE " + 
				"	s.enabled_flag = 'Y' " + 
				"	AND s.status = 'DATA_ACTIVE' " + 
				"	AND s.follow_up = 'Y' " + 
				"	AND (f.followup_status IS NULL " + 
				"	OR f.followup_status NOT IN ('PIC_DONE', 'PIC_EXTENSION')) " + 
				"	AND EXISTS ( " + 
				"	SELECT " + 
				"		1 " + 
				"	FROM " + 
				"		wo_mst_user u " + 
				"	WHERE " + 
				"		u.nik = :nik " + 
				"		AND (u.user_id = f.user_id_1 " + 
				"		OR u.user_id = f.user_id_2 " + 
				"		OR u.user_id = f.user_id_3)) ");
		//sb.append(" select max(f.creation_date) from wo_trc_audit s, wo_trc_audit_pic_followup f where f.audit_id = s.audit_id and s.enabled_flag = 'Y' and s.status = 'DATA_ACTIVE' and s.follow_up = 'Y' and (f.followup_status is null OR f.followup_status <> 'PIC_DONE') and exists (select 1 from wo_mst_user u where u.nik = :nik and (u.user_id = f.user_id_1 or u.user_id = f.user_id_2 or u.user_id = f.user_id_3)) ");

		Query query = getSession().createSQLQuery(sb.toString());


		query.setParameter("nik", nik);

		return ((Date) query.getSingleResult());
	}

	@Override
	public Long getCountTindakLanjutComplianceTesting(String nik) throws Exception {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		//sb.append(" select count(1) from wo_trc_compliance_review s, wo_trc_cmplc_review_pic_fp f where f.compliance_review_id = s.compliance_review_id and s.enabled_flag = 'Y' and s.status = 'DATA_ACTIVE' and s.follow_up = 'Y' and (f.followup_status is null OR f.followup_status <> 'PIC_DONE') and exists (select 1 from wo_mst_user u where u.nik = :nik and (u.user_id = f.user_id_1 or u.user_id = f.user_id_2 or u.user_id = f.user_id_3)) ");
		sb.append(" select count(1) from wo_trc_compliance_testing s, WO_TRC_COMPLIANCE_TESTING_DTL d, WO_TRC_COMP_TEST_PIC_FP p where d.COMPLIANCE_TESTING_ID = s.COMPLIANCE_TESTING_ID and d.COMPLIANCE_TESTING_DTL_ID = p.COMPLIANCE_TESTING_DTL_ID and s.enabled_flag = 'Y' and p.enabled_flag = 'Y' and s.status = 'DATA_ACTIVE' and d.followup = 'Y' and (p.followup_status is null OR p.followup_status not in ('PIC_DONE','PIC_EXTENSION')) and exists (select 1 from wo_mst_user u where u.nik = :nik and (u.user_id = p.user_id_1 or u.user_id = p.user_id_2 or u.user_id = p.user_id_3)) ");
		Query query = getSession().createSQLQuery(sb.toString());


		query.setParameter("nik", nik);

		return ((Number) query.getSingleResult()).longValue();
	}
	
	public Date getMaxDateTindakLanjutComplianceTesting(String nik) throws Exception {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		//sb.append(" select max(s.creation_date) from wo_trc_compliance_review s, wo_trc_cmplc_review_pic_fp f where f.compliance_review_id = s.compliance_review_id and s.enabled_flag = 'Y' and s.status = 'DATA_ACTIVE' and s.follow_up = 'Y' and (f.followup_status is null OR f.followup_status <> 'PIC_DONE') and exists (select 1 from wo_mst_user u where u.nik = :nik and (u.user_id = f.user_id_1 or u.user_id = f.user_id_2 or u.user_id = f.user_id_3)) ");
		sb.append(" select max(s.creation_date) from wo_trc_compliance_testing s, WO_TRC_COMPLIANCE_TESTING_DTL d, WO_TRC_COMP_TEST_PIC_FP p where d.COMPLIANCE_TESTING_ID = s.COMPLIANCE_TESTING_ID and d.COMPLIANCE_TESTING_DTL_ID = p.COMPLIANCE_TESTING_DTL_ID and s.enabled_flag = 'Y' and p.enabled_flag = 'Y' and s.status = 'DATA_ACTIVE' and d.followup = 'Y' and (p.followup_status is null OR p.followup_status not in ('PIC_DONE','PIC_EXTENSION')) and exists (select 1 from wo_mst_user u where u.nik = :nik and (u.user_id = p.user_id_1 or u.user_id = p.user_id_2 or u.user_id = p.user_id_3)) ");
		
		Query query = getSession().createSQLQuery(sb.toString());


		query.setParameter("nik", nik);

		return ((Date) query.getSingleResult());
	}

	@Override
	public Long getCountTindakLanjutRegulationMonitor(String nik) throws Exception {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		sb.append(" select count(1) "
				+ "from wo_trc_reg_monitoring s, wo_trc_reg_monitoring_pic_fp f, wo_tmp_reg_monitoring ts "
				+ "where f.reg_monitoring_id = s.reg_monitoring_id "
				+ "and s.reg_monitoring_id = ts.reg_monitoring_id "
				+ "and s.enabled_flag = 'Y' and s.status = 'DATA_ACTIVE' and ts.enabled_flag = 'Y' "
				+ "and s.follow_up = 'Y' and (f.followup_status is null OR f.followup_status <> 'PIC_DONE') "
				+ "and exists (select 1 from wo_mst_user u where u.nik = :nik and (u.user_id = f.user_id_1 or u.user_id = f.user_id_2 or u.user_id = f.user_id_3)) ");

		Query query = getSession().createSQLQuery(sb.toString());


		query.setParameter("nik", nik);

		return ((Number) query.getSingleResult()).longValue();
	}
	
	public Date getMaxDateTindakLanjutRegulationMonitor(String nik) throws Exception {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		sb.append(" select max(s.creation_date) from wo_trc_reg_monitoring s, wo_trc_reg_monitoring_pic_fp f where f.reg_monitoring_id = s.reg_monitoring_id and s.enabled_flag = 'Y' and s.status = 'DATA_ACTIVE' and s.follow_up = 'Y' and (f.followup_status is null OR f.followup_status <> 'PIC_DONE') and exists (select 1 from wo_mst_user u where u.nik = :nik and (u.user_id = f.user_id_1 or u.user_id = f.user_id_2 or u.user_id = f.user_id_3)) ");

		Query query = getSession().createSQLQuery(sb.toString());


		query.setParameter("nik", nik);

		return ((Date) query.getSingleResult());
	}

	@Override
	public Long getCountSosialisasiPerluVerifikasi(String nik,String action) throws Exception {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		sb.append(" select count(1) from wo_trc_socialization s, wo_trc_socialization_pic_fp f where f.socialization_id = s.socialization_id and s.enabled_flag = 'Y' and s.status = 'DATA_ACTIVE' and s.follow_up = 'Y' and f.followup_status = 'PIC_DONE' and (f.compliance_status <> 'COMPLIANCE_CLOSE' or f.compliance_status is null) and exists (select 1 from wo_mst_responsibility r, wo_mst_responsibility_dtl rd, wo_mst_menu m, wo_mst_user u where r.enabled_flag = 'Y' and r.responsibility_id = u.responsibility_id and r.responsibility_id = rd.responsibility_id and rd.menu_id = m.menu_id and u.enabled_flag = 'Y' and m.enabled_flag = 'Y' and u.nik = :nik and m.action = :action) ");

		Query query = getSession().createSQLQuery(sb.toString());


		query.setParameter("nik", nik);
		query.setParameter("action", action);

		return ((Number) query.getSingleResult()).longValue();
	}
	
	public Date getMaxDateSosialisasiPerluVerifikasi(String nik,String action) throws Exception {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		sb.append(" select max(s.creation_date) from wo_trc_socialization s, wo_trc_socialization_pic_fp f where f.socialization_id = s.socialization_id and s.enabled_flag = 'Y' and s.status = 'DATA_ACTIVE' and s.follow_up = 'Y' and f.followup_status = 'PIC_DONE' and (f.compliance_status <> 'COMPLIANCE_CLOSE' or f.compliance_status is null) and exists (select 1 from wo_mst_responsibility r, wo_mst_responsibility_dtl rd, wo_mst_menu m, wo_mst_user u where r.enabled_flag = 'Y' and r.responsibility_id = u.responsibility_id and r.responsibility_id = rd.responsibility_id and rd.menu_id = m.menu_id and u.enabled_flag = 'Y' and m.enabled_flag = 'Y' and u.nik = :nik and m.action = :action) ");

		Query query = getSession().createSQLQuery(sb.toString());


		query.setParameter("nik", nik);
		query.setParameter("action", action);

		return ((Date) query.getSingleResult());
	}

	@Override
	public Long getCountDendaPerluVerifikasi(String nik,String action) throws Exception {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT "
				+ "		count(1) "
				+ "	FROM "
				+ "		wo_trc_fine s "
				+ "	INNER JOIN wo_trc_fine_pic_followup f ON "
				+ "		s.FINE_ID = f.FINE_ID "
				+ "	WHERE "
				+ "		s.enabled_flag = 'Y' "
				+ "		AND s.status = 'DATA_ACTIVE' "
				+ "		AND s.follow_up = 'Y' "
				+ "		AND f.followup_status IN ('PIC_DONE', 'PIC_EXTENSION') "
				+ "		AND (f.compliance_status <> 'COMPLIANCE_CLOSE' "
				+ "			OR f.compliance_status IS NULL) "
				+ "		AND s.CREATED_BY = :nik "
				+ "		OR EXISTS ( "
				+ "		SELECT "
				+ "			1 "
				+ "		FROM "
				+ "			WO_TRC_FINE_PIC_COMPLIANCE wtfpc2 "
				+ "		INNER JOIN WO_MST_USER wmu2 ON "
				+ "			wtfpc2.USER_ID = wmu2.USER_ID "
				+ "		WHERE "
				+ "			wtfpc2.FINE_ID = s.FINE_ID "
				+ "			AND wmu2.NIK = :nik) "
				+ "		AND EXISTS ( "
				+ "		SELECT "
				+ "			1 "
				+ "		FROM "
				+ "			wo_mst_responsibility r, "
				+ "			wo_mst_responsibility_dtl rd, "
				+ "			wo_mst_menu m, "
				+ "			wo_mst_user u "
				+ "		WHERE "
				+ "			r.enabled_flag = 'Y' "
				+ "			AND r.responsibility_id = u.responsibility_id "
				+ "			AND r.responsibility_id = rd.responsibility_id "
				+ "			AND rd.menu_id = m.menu_id "
				+ "			AND u.enabled_flag = 'Y' "
				+ "			AND m.enabled_flag = 'Y' "
				+ "			AND u.nik = :nik "
				+ "			AND m.action = :action) ");

		Query query = getSession().createSQLQuery(sb.toString());


		query.setParameter("nik", nik);
		query.setParameter("action", action);

		return ((Number) query.getSingleResult()).longValue();
	}
	
	public Date getMaxDateDendaPerluVerifikasi(String nik,String action) throws Exception {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		sb.append(" 	SELECT "
				+ "		max(s.creation_date) "
				+ "	FROM "
				+ "		wo_trc_fine s, "
				+ "		wo_trc_fine_pic_followup f, "
				+ "		WO_TRC_FINE_PIC_COMPLIANCE pc, "
				+ "		WO_MST_USER wmu "
				+ "	WHERE "
				+ "		f.fine_id = s.fine_id "
				+ "		AND pc.FINE_ID = s.FINE_ID  "
				+ "		AND wmu.USER_ID = pc.USER_ID "
				+ "		AND s.enabled_flag = 'Y' "
				+ "		AND s.status = 'DATA_ACTIVE' "
				+ "		AND s.follow_up = 'Y' "
				+ "		AND f.followup_status = 'PIC_DONE' "
				+ "		AND (f.compliance_status <> 'COMPLIANCE_CLOSE' "
				+ "			OR f.compliance_status IS NULL) "
				+ "		AND (s.CREATED_BY = :nik OR wmu.NIK = :nik) "
				+ "		AND EXISTS ( "
				+ "		SELECT "
				+ "			1 "
				+ "		FROM "
				+ "			wo_mst_responsibility r, "
				+ "			wo_mst_responsibility_dtl rd, "
				+ "			wo_mst_menu m, "
				+ "			wo_mst_user u "
				+ "		WHERE "
				+ "			r.enabled_flag = 'Y' "
				+ "		AND r.responsibility_id = u.responsibility_id "
				+ "		AND r.responsibility_id = rd.responsibility_id "
				+ "		AND rd.menu_id = m.menu_id "
				+ "		AND u.enabled_flag = 'Y' "
				+ "		AND m.enabled_flag = 'Y' "
				+ "		AND u.nik = :nik "
				+ "		AND m.action = :action) ");

		Query query = getSession().createSQLQuery(sb.toString());


		query.setParameter("nik", nik);
		query.setParameter("action", action);

		return ((Date) query.getSingleResult());
	}

	@Override
	public Long getCountSuratMasukPerluVerifikasi(String nik,String action) throws Exception {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		sb.append(" select count(1) from wo_trc_correspondence s where s.enabled_flag = 'Y' and s.status = 'DATA_ACTIVE' and s.follow_up = 'Y' and s.followup_status = 'PIC_DONE' and (s.compliance_status <> 'COMPLIANCE_CLOSE' or s.compliance_status is null) and exists (select 1 from wo_mst_responsibility r, wo_mst_responsibility_dtl rd, wo_mst_menu m, wo_mst_user u where r.enabled_flag = 'Y' and r.responsibility_id = u.responsibility_id and r.responsibility_id = rd.responsibility_id and rd.menu_id = m.menu_id and u.enabled_flag = 'Y' and m.enabled_flag = 'Y' and u.nik = :nik and m.action = :action) ");

		Query query = getSession().createSQLQuery(sb.toString());


		query.setParameter("nik", nik);
		query.setParameter("action", action);

		return ((Number) query.getSingleResult()).longValue();
	}
	
	public Date getMaxDateSuratMasukPerluVerifikasi(String nik,String action) throws Exception {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		sb.append(" select max(s.creation_date) from wo_trc_correspondence s where s.enabled_flag = 'Y' and s.status = 'DATA_ACTIVE' and s.follow_up = 'Y' and s.followup_status = 'PIC_DONE' and (s.compliance_status <> 'COMPLIANCE_CLOSE' or s.compliance_status is null) and exists (select 1 from wo_mst_responsibility r, wo_mst_responsibility_dtl rd, wo_mst_menu m, wo_mst_user u where r.enabled_flag = 'Y' and r.responsibility_id = u.responsibility_id and r.responsibility_id = rd.responsibility_id and rd.menu_id = m.menu_id and u.enabled_flag = 'Y' and m.enabled_flag = 'Y' and u.nik = :nik and m.action = :action) ");

		Query query = getSession().createSQLQuery(sb.toString());


		query.setParameter("nik", nik);
		query.setParameter("action", action);

		return ((Date) query.getSingleResult());
	}

	@Override
	public Long getCountRegulatoryReportPerluVerifikasi(String nik,String action) throws Exception {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		sb.append(" select count(1) from wo_trc_rmd s, wo_trc_rmd_pic_followup f where f.rmd_id = s.rmd_id and s.enabled_flag = 'Y' and s.status = 'DATA_ACTIVE' and s.follow_up = 'Y' and f.followup_status = 'PIC_DONE' and (f.compliance_status <> 'COMPLIANCE_CLOSE' or f.compliance_status is null) and exists (select 1 from wo_mst_responsibility r, wo_mst_responsibility_dtl rd, wo_mst_menu m, wo_mst_user u where r.enabled_flag = 'Y' and r.responsibility_id = u.responsibility_id and r.responsibility_id = rd.responsibility_id and rd.menu_id = m.menu_id and u.enabled_flag = 'Y' and m.enabled_flag = 'Y' and u.nik = :nik and m.action = :action) ");

		Query query = getSession().createSQLQuery(sb.toString());


		query.setParameter("nik", nik);
		query.setParameter("action", action);

		return ((Number) query.getSingleResult()).longValue();
	}
	
	@Override
	public Long getCountAuditPerluVerifikasi(String nik,String action) throws Exception {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		sb.append(" select count(1) from wo_trc_audit_pic_followup f "
				+ " INNER JOIN WO_TRC_AUDIT_PC_FP_BANK_CMITMT c ON c.AUD_PC_FP_BANK_CMITMT_ID = f.AUD_PC_FP_BANK_CMITMT_ID "
				+ " INNER JOIN WO_TRC_AUDIT_CHECK_POINT p ON p.AUDIT_CHECK_POINT_ID = c.AUDIT_CHECK_POINT_ID "
				+ " INNER JOIN WO_TRC_AUDIT s ON s.AUDIT_ID = p.AUDIT_ID "
				+ " where  s.enabled_flag = 'Y' and s.status = 'DATA_ACTIVE' and s.follow_up = 'Y' "
				+ " and (f.followup_status = 'PIC_DONE' OR f.followup_status = 'PIC_EXTENSION') "
				+ " and (f.compliance_status <> 'COMPLIANCE_CLOSE' or f.compliance_status is null) "
				+ " and exists (select 1 from wo_mst_responsibility r, wo_mst_responsibility_dtl rd, wo_mst_menu m, wo_mst_user u where r.enabled_flag = 'Y' and r.responsibility_id = u.responsibility_id and r.responsibility_id = rd.responsibility_id and rd.menu_id = m.menu_id and u.enabled_flag = 'Y' and m.enabled_flag = 'Y' and u.nik = :nik and m.action = :action) ");

		Query query = getSession().createSQLQuery(sb.toString());


		query.setParameter("nik", nik);
		query.setParameter("action", action);

		return ((Number) query.getSingleResult()).longValue();
	}
	
	public Date getMaxDateAuditPerluVerifikasi(String nik,String action) throws Exception {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		//sb.append(" select max(s.creation_date) from wo_trc_audit s, wo_trc_audit_pic_followup f where f.audit_id = s.audit_id and s.enabled_flag = 'Y' and s.status = 'DATA_ACTIVE' and s.follow_up = 'Y' and f.followup_status = 'PIC_DONE' and (f.compliance_status <> 'COMPLIANCE_CLOSE' or f.compliance_status is null) and exists (select 1 from wo_mst_responsibility r, wo_mst_responsibility_dtl rd, wo_mst_menu m, wo_mst_user u where r.enabled_flag = 'Y' and r.responsibility_id = u.responsibility_id and r.responsibility_id = rd.responsibility_id and rd.menu_id = m.menu_id and u.enabled_flag = 'Y' and m.enabled_flag = 'Y' and u.nik = :nik and m.action = :action) ");
		sb.append(" select max(s.creation_date) from wo_trc_audit_pic_followup f "
				+ " INNER JOIN WO_TRC_AUDIT_PC_FP_BANK_CMITMT c ON c.AUD_PC_FP_BANK_CMITMT_ID = f.AUD_PC_FP_BANK_CMITMT_ID "
				+ " INNER JOIN WO_TRC_AUDIT_CHECK_POINT p ON p.AUDIT_CHECK_POINT_ID = c.AUDIT_CHECK_POINT_ID "
				+ " INNER JOIN WO_TRC_AUDIT s ON s.AUDIT_ID = p.AUDIT_ID "
				+ " where  s.enabled_flag = 'Y' and s.status = 'DATA_ACTIVE' and s.follow_up = 'Y' "
				+ " and (f.followup_status = 'PIC_DONE' OR f.followup_status = 'PIC_EXTENSION') "
				+ " and (f.compliance_status <> 'COMPLIANCE_CLOSE' or f.compliance_status is null) "
				+ " and exists (select 1 from wo_mst_responsibility r, wo_mst_responsibility_dtl rd, wo_mst_menu m, wo_mst_user u where r.enabled_flag = 'Y' and r.responsibility_id = u.responsibility_id and r.responsibility_id = rd.responsibility_id and rd.menu_id = m.menu_id and u.enabled_flag = 'Y' and m.enabled_flag = 'Y' and u.nik = :nik and m.action = :action) ");

		Query query = getSession().createSQLQuery(sb.toString());


		query.setParameter("nik", nik);
		query.setParameter("action", action);

		return ((Date) query.getSingleResult());
	}

	@Override
	public Long getCountComplianceTestingPerluVerifikasi(String nik,String action) throws Exception {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		sb.append(" select count(1) FROM WO_TRC_COMPLIANCE_TESTING ct LEFT JOIN WO_TRC_COMPLIANCE_TESTING_DTL ctd ON ct.COMPLIANCE_TESTING_ID = ctd.COMPLIANCE_TESTING_ID LEFT JOIN WO_TRC_COMP_TEST_PIC_FP fp ON ctd.COMPLIANCE_TESTING_DTL_ID = fp.COMPLIANCE_TESTING_DTL_ID LEFT JOIN wo_mst_user u ON u.USER_ID = fp.user_id_1 LEFT JOIN wo_mst_parameter_dtl pd ON pd.parameter_dtl_code = fp.FOLLOWUP_STATUS  where  ct.enabled_flag = 'Y' and ct.status = 'DATA_ACTIVE'  and (fp.followup_status = 'PIC_DONE' OR fp.followup_status = 'PIC_EXTENSION') and (fp.compliance_status <> 'COMPLIANCE_CLOSE' or fp.compliance_status is null) and exists (select 1 from wo_mst_responsibility r, wo_mst_responsibility_dtl rd, wo_mst_menu m, wo_mst_user u where r.enabled_flag = 'Y' and r.responsibility_id = u.responsibility_id and r.responsibility_id = rd.responsibility_id and rd.menu_id = m.menu_id and u.enabled_flag = 'Y' and m.enabled_flag = 'Y' and u.nik = :nik and m.action = :action) ");

		Query query = getSession().createSQLQuery(sb.toString());


		query.setParameter("nik", nik);
		query.setParameter("action", action);

		return ((Number) query.getSingleResult()).longValue();
	}
	
	public Date getMaxDateComplianceTestingPerluVerifikasi(String nik,String action) throws Exception {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		sb.append(" select max(ct.creation_date) FROM WO_TRC_COMPLIANCE_TESTING ct LEFT JOIN WO_TRC_COMPLIANCE_TESTING_DTL ctd ON ct.COMPLIANCE_TESTING_ID = ctd.COMPLIANCE_TESTING_ID LEFT JOIN WO_TRC_COMP_TEST_PIC_FP fp ON ctd.COMPLIANCE_TESTING_DTL_ID = fp.COMPLIANCE_TESTING_DTL_ID LEFT JOIN wo_mst_user u ON u.USER_ID = fp.user_id_1 LEFT JOIN wo_mst_parameter_dtl pd ON pd.parameter_dtl_code = fp.FOLLOWUP_STATUS  where  ct.enabled_flag = 'Y' and ct.status = 'DATA_ACTIVE' and (fp.followup_status = 'PIC_DONE' OR fp.followup_status = 'PIC_EXTENSION') and (fp.compliance_status <> 'COMPLIANCE_CLOSE' or fp.compliance_status is null)  and exists (select 1 from wo_mst_responsibility r, wo_mst_responsibility_dtl rd, wo_mst_menu m, wo_mst_user u where r.enabled_flag = 'Y' and r.responsibility_id = u.responsibility_id and r.responsibility_id = rd.responsibility_id and rd.menu_id = m.menu_id and u.enabled_flag = 'Y' and m.enabled_flag = 'Y' and u.nik = :nik and m.action = :action) ");

		Query query = getSession().createSQLQuery(sb.toString());


		query.setParameter("nik", nik);
		query.setParameter("action", action);

		return ((Date) query.getSingleResult());
	}

	@Override
	public Long getCountRegulationMoitorPerluVerifikasi(String nik,String action) throws Exception {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		sb.append(" select count(1) from wo_trc_reg_monitoring s, wo_trc_reg_monitoring_pic_fp f where f.reg_monitoring_id = s.reg_monitoring_id and s.enabled_flag = 'Y' and s.status = 'DATA_ACTIVE' and s.follow_up = 'Y' and f.followup_status = 'PIC_DONE' and (f.compliance_status <> 'COMPLIANCE_CLOSE' or f.compliance_status is null) and exists (select 1 from wo_mst_responsibility r, wo_mst_responsibility_dtl rd, wo_mst_menu m, wo_mst_user u where r.enabled_flag = 'Y' and r.responsibility_id = u.responsibility_id and r.responsibility_id = rd.responsibility_id and rd.menu_id = m.menu_id and u.enabled_flag = 'Y' and m.enabled_flag = 'Y' and u.nik = :nik and m.action = :action) ");

		Query query = getSession().createSQLQuery(sb.toString());


		query.setParameter("nik", nik);
		query.setParameter("action", action);

		return ((Number) query.getSingleResult()).longValue();
	}
	
	public Date getMaxDateRegulationMoitorPerluVerifikasi(String nik,String action) throws Exception {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		sb.append(" select max(s.creation_date) from wo_trc_reg_monitoring s, wo_trc_reg_monitoring_pic_fp f where f.reg_monitoring_id = s.reg_monitoring_id and s.enabled_flag = 'Y' and s.status = 'DATA_ACTIVE' and s.follow_up = 'Y' and f.followup_status = 'PIC_DONE' and (f.compliance_status <> 'COMPLIANCE_CLOSE' or f.compliance_status is null) and exists (select 1 from wo_mst_responsibility r, wo_mst_responsibility_dtl rd, wo_mst_menu m, wo_mst_user u where r.enabled_flag = 'Y' and r.responsibility_id = u.responsibility_id and r.responsibility_id = rd.responsibility_id and rd.menu_id = m.menu_id and u.enabled_flag = 'Y' and m.enabled_flag = 'Y' and u.nik = :nik and m.action = :action) ");

		Query query = getSession().createSQLQuery(sb.toString());


		query.setParameter("nik", nik);
		query.setParameter("action", action);

		return ((Date) query.getSingleResult());
	}
	
	public Long getCountPeraturanInternalBaru(String nik,String action) throws Exception {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		sb.append(" select count(1) from wo_mst_regulation r where r.enabled_flag = 'Y' and r.jenis_ketentuan = 'KETENTUAN_INTERNAL' and trunc(sysdate) >= r.published_date and r.status = 'DATA_ACTIVE' and not exists (select 1 from wo_log_access a, wo_mst_user u where u.user_id = a.user_id and a.access_id = r.regulation_id and u.nik = :nik and a.access_action = :access_action) ");

		Query query = getSession().createSQLQuery(sb.toString());


		query.setParameter("nik", nik);
		query.setParameter("access_action", action);

		return ((Number) query.getSingleResult()).longValue();
	}
	
	public Date getMaxDatePeraturanInternalBaru(String nik,String action) throws Exception {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		sb.append(" select max(r.creation_date) from wo_mst_regulation r where r.enabled_flag = 'Y' and r.jenis_ketentuan = 'KETENTUAN_INTERNAL' and trunc(sysdate) >= r.published_date and r.status = 'DATA_ACTIVE' and not exists (select 1 from wo_log_access a, wo_mst_user u where u.user_id = a.user_id and a.access_id = r.regulation_id and u.nik = :nik and a.access_action = :access_action) ");

		Query query = getSession().createSQLQuery(sb.toString());


		query.setParameter("nik", nik);
		query.setParameter("access_action", action);

		return ((Date) query.getSingleResult());
	}
	
	public Long getCountPeraturanEksternalBaru(String nik,String action) throws Exception {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		sb.append(" select count(1) from wo_mst_regulation r where r.enabled_flag = 'Y' and r.jenis_ketentuan = 'KETENTUAN_EKSTERNAL' and trunc(sysdate) >= r.published_date and r.status = 'DATA_ACTIVE' and not exists (select 1 from wo_log_access a, wo_mst_user u where u.user_id = a.user_id and a.access_id = r.regulation_id and u.nik = :nik and a.access_action = :access_action) ");

		Query query = getSession().createSQLQuery(sb.toString());


		query.setParameter("nik", nik);
		query.setParameter("access_action", action);

		return ((Number) query.getSingleResult()).longValue();
	}
	
	public Date getMaxDatePeraturanEksternalBaru(String nik,String action) throws Exception {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		sb.append(" select max(r.creation_date) from wo_mst_regulation r where r.enabled_flag = 'Y' and r.jenis_ketentuan = 'KETENTUAN_EKSTERNAL' and trunc(sysdate) >= r.published_date and r.status = 'DATA_ACTIVE' and not exists (select 1 from wo_log_access a, wo_mst_user u where u.user_id = a.user_id and a.access_id = r.regulation_id and u.nik = :nik and a.access_action = :access_action) ");

		Query query = getSession().createSQLQuery(sb.toString());


		query.setParameter("nik", nik);
		query.setParameter("access_action", action);

		return ((Date) query.getSingleResult());
	}
	public Long getCountOpiniBaru(String nik,String action,String divisionName) throws Exception {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		sb.append(" select count(1) from wo_mst_article r where r.enabled_flag = 'Y' and r.status = 'DATA_ACTIVE' and trunc(sysdate) >= r.publish_date and not exists (select 1 from wo_log_access a, wo_mst_user u where u.user_id = a.user_id and a.access_id = r.article_id and u.nik = :nik and a.access_action = :access_action) ");
		sb.append(" and article_type in ('LEGAL_OPINION','COMPLIANCE_OPINION') ");
		/*if(divisionName!=null && divisionName.equals("COMPLIANCE REGULATORY AFFAIRS")){
			sb.append(" and article_type in ('COMPLIANCE_FLASH','GENERAL','TRAINING_MATERIAL','WORKSHOP') ");
		}
		else if(divisionName!=null && divisionName.equals("CORPORATE LEGAL & LITIGATION")){
			sb.append(" and article_type in ('LEGAL_REVIEW','GENERAL','TRAINING_MATERIAL','WORKSHOP') ");
		}else{
			sb.append(" and article_type in ('GENERAL','TRAINING_MATERIAL','WORKSHOP') ");
		}*/
		Query query = getSession().createSQLQuery(sb.toString());


		query.setParameter("nik", nik);
		query.setParameter("access_action", action);

		return ((Number) query.getSingleResult()).longValue();
	}
	
	public Date getMaxDateOpiniBaru(String nik,String action,String divisionName) throws Exception {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		sb.append(" select max(r.creation_date) from wo_mst_article r where r.enabled_flag = 'Y' and r.status = 'DATA_ACTIVE' and trunc(sysdate) >= r.publish_date and not exists (select 1 from wo_log_access a, wo_mst_user u where u.user_id = a.user_id and a.access_id = r.article_id and u.nik = :nik and a.access_action = :access_action) ");
		sb.append(" and article_type in ('LEGAL_OPINION','COMPLIANCE_OPINION') ");
		/*if(divisionName!=null && divisionName.equals("COMPLIANCE REGULATORY AFFAIRS")){
			sb.append(" and article_type in ('COMPLIANCE_FLASH','GENERAL','TRAINING_MATERIAL','WORKSHOP') ");
		}
		else if(divisionName!=null && divisionName.equals("CORPORATE LEGAL & LITIGATION")){
			sb.append(" and article_type in ('LEGAL_REVIEW','GENERAL','TRAINING_MATERIAL','WORKSHOP') ");
		}else{
			sb.append(" and article_type in ('GENERAL','TRAINING_MATERIAL','WORKSHOP') ");
		}*/
		Query query = getSession().createSQLQuery(sb.toString());


		query.setParameter("nik", nik);
		query.setParameter("access_action", action);

		return ((Date) query.getSingleResult());
	}
	
	public Long getCountArtikelBaru(String nik,String action,String divisionName) throws Exception {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		sb.append(" select count(1) from wo_mst_article r where r.enabled_flag = 'Y' and r.status = 'DATA_ACTIVE' and trunc(sysdate) >= r.publish_date and not exists (select 1 from wo_log_access a, wo_mst_user u where u.user_id = a.user_id and a.access_id = r.article_id and u.nik = :nik and a.access_action = :access_action) ");
		sb.append(" and article_type in ('COMPLIANCE_FLASH','GENERAL','TRAINING_MATERIAL','WORKSHOP','LEGAL_REVIEW') ");
		/*if(divisionName!=null && divisionName.equals("COMPLIANCE REGULATORY AFFAIRS")){
			sb.append(" and article_type in ('COMPLIANCE_FLASH','GENERAL','TRAINING_MATERIAL','WORKSHOP') ");
		}
		else if(divisionName!=null && divisionName.equals("CORPORATE LEGAL & LITIGATION")){
			sb.append(" and article_type in ('LEGAL_REVIEW','GENERAL','TRAINING_MATERIAL','WORKSHOP') ");
		}else{
			sb.append(" and article_type in ('GENERAL','TRAINING_MATERIAL','WORKSHOP') ");
		}*/
		Query query = getSession().createSQLQuery(sb.toString());


		query.setParameter("nik", nik);
		query.setParameter("access_action", action);

		return ((Number) query.getSingleResult()).longValue();
	}
	
	public Date getMaxDateArtikelBaru(String nik,String action,String divisionName) throws Exception {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		sb.append(" select max(r.creation_date) from wo_mst_article r where r.enabled_flag = 'Y' and r.status = 'DATA_ACTIVE' and trunc(sysdate) >= r.publish_date and not exists (select 1 from wo_log_access a, wo_mst_user u where u.user_id = a.user_id and a.access_id = r.article_id and u.nik = :nik and a.access_action = :access_action) ");
		sb.append(" and article_type in ('COMPLIANCE_FLASH','GENERAL','TRAINING_MATERIAL','WORKSHOP','LEGAL_REVIEW') ");
		/*if(divisionName!=null && divisionName.equals("COMPLIANCE REGULATORY AFFAIRS")){
			sb.append(" and article_type in ('COMPLIANCE_FLASH','GENERAL','TRAINING_MATERIAL','WORKSHOP') ");
		}
		else if(divisionName!=null && divisionName.equals("CORPORATE LEGAL & LITIGATION")){
			sb.append(" and article_type in ('LEGAL_REVIEW','GENERAL','TRAINING_MATERIAL','WORKSHOP') ");
		}else{
			sb.append(" and article_type in ('GENERAL','TRAINING_MATERIAL','WORKSHOP') ");
		}*/
		Query query = getSession().createSQLQuery(sb.toString());


		query.setParameter("nik", nik);
		query.setParameter("access_action", action);

		return ((Date) query.getSingleResult());
	}
	
	public Long getCountDiskusiBaru(String nik,String action) throws Exception {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		sb.append(" select count(1) from wo_mst_discussion r where r.enabled_flag = 'Y' and r.thread_close_status = 'COMPLIANCE_OPEN' and not exists (select 1 from wo_log_access a, wo_mst_user u where u.user_id = a.user_id and a.access_id = r.discussion_id and u.nik = :nik and a.access_action = :access_action) ");

		Query query = getSession().createSQLQuery(sb.toString());


		query.setParameter("nik", nik);
		query.setParameter("access_action", action);

		return ((Number) query.getSingleResult()).longValue();
	}

	public Date getMaxDateDiskusiBaru(String nik,String action) throws Exception {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		sb.append(" select max(r.creation_date) from wo_mst_discussion r where r.enabled_flag = 'Y' and r.thread_close_status = 'COMPLIANCE_OPEN' and not exists (select 1 from wo_log_access a, wo_mst_user u where u.user_id = a.user_id and a.access_id = r.discussion_id and u.nik = :nik and a.access_action = :access_action) ");

		Query query = getSession().createSQLQuery(sb.toString());


		query.setParameter("nik", nik);
		query.setParameter("access_action", action);

		return ((Date) query.getSingleResult());
	}
	
	public Long getCountViewerLitigasi(String nik,String action) throws Exception {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		sb.append(" select count(1) from WO_MST_LITIGATION l inner join WO_MST_LITIGATION_PIC d on l.litigation_id = d.litigation_id inner join WO_MST_USER u2 on u2.user_id = d.user_id  where l.enabled_flag = 'Y' and u2.nik= :nik and not exists (select 1 from wo_log_access a, wo_mst_user u where u.user_id = a.user_id and a.access_id = l.litigation_id and u.nik = :nik and a.access_action = :access_action) ");

		Query query = getSession().createSQLQuery(sb.toString());


		query.setParameter("nik", nik);
		query.setParameter("access_action", action);

		return ((Number) query.getSingleResult()).longValue();
	}
	
	public Date getCountMaxDateViewerLitigasi(String nik,String action) throws Exception {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		sb.append(" select max(l.creation_date) from WO_MST_LITIGATION l inner join WO_MST_LITIGATION_PIC d on l.litigation_id = d.litigation_id inner join WO_MST_USER u2 on u2.user_id = d.user_id  where l.enabled_flag = 'Y' and u2.nik= :nik and not exists (select 1 from wo_log_access a, wo_mst_user u where u.user_id = a.user_id and a.access_id = l.litigation_id and u.nik = :nik and a.access_action = :access_action) ");

		Query query = getSession().createSQLQuery(sb.toString());


		query.setParameter("nik", nik);
		query.setParameter("access_action", action);

		return ((Date) query.getSingleResult());
	}
	
	public Boolean getIsAdmin(String nik) throws Exception {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		sb.append(" select count(1) from wo_mst_user where responsibility_id is not null and nik = :nik and enabled_flag = 'Y' ");

		Query query = getSession().createSQLQuery(sb.toString());


		query.setParameter("nik", nik);
		
		Long countData = ((Number) query.getSingleResult()).longValue();
		
		if(countData > 0){
			return true;
		}else{
			return false;
		}

		
	}
	
	public Long getCountCpsa(String nik) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT COUNT (1) "); 
		sb.append("   FROM WO_MST_CPSA cpsa "); 
		sb.append("        INNER JOIN WO_MST_CPSA_PIC pic ON cpsa.CPSA_ID = pic.CPSA_ID "); 
		sb.append("        LEFT JOIN WO_MST_PARAMETER_DTL param ON pic.STATUS_PIC_ID = param.PARAMETER_DTL_ID ");
		sb.append("  WHERE cpsa.enabled_flag = 'Y' ");
		sb.append("        AND pic.enabled_flag = 'Y' ");
		sb.append("        AND (param.PARAMETER_DTL_CODE is null or ");
		sb.append("               param.PARAMETER_DTL_CODE IN ('COMPLIANCE_OPEN','CPSA_INPROGRESS', ");
		sb.append("                                            'CPSA_COMPLETED', 'CPSA_REJECTED')) ");
		sb.append("        AND EXISTS ");
		sb.append("              (SELECT 1 ");
		sb.append("                 FROM wo_mst_user u "); 
		sb.append("                WHERE u.nik = :nik "); 
		sb.append("                      AND (u.user_id = pic.user_id_1 OR u.user_id = pic.user_id_2)) ");
		
		Query query = getSession().createSQLQuery(sb.toString());

		query.setParameter("nik", nik);
		Long countData = new Long(0);
		if(query.getSingleResult() !=null) {
			countData = ((Number) query.getSingleResult()).longValue();
		}

		return countData;
	}	
	
	public Date getMaxDateCpsa(String nik) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT MAX(cpsa.creation_date) "); 
		sb.append("   FROM WO_MST_CPSA cpsa "); 
		sb.append("        INNER JOIN WO_MST_CPSA_PIC pic ON cpsa.CPSA_ID = pic.CPSA_ID "); 
		sb.append("        LEFT JOIN WO_MST_PARAMETER_DTL param ON pic.STATUS_PIC_ID = param.PARAMETER_DTL_ID ");
		sb.append("  WHERE cpsa.enabled_flag = 'Y' ");
		sb.append("        AND pic.enabled_flag = 'Y' ");
		sb.append("        AND (param.PARAMETER_DTL_CODE is null or ");
		sb.append("               param.PARAMETER_DTL_CODE IN ('COMPLIANCE_OPEN','CPSA_INPROGRESS', ");
		sb.append("                                            'CPSA_COMPLETED', 'CPSA_REJECTED')) ");
		sb.append("        AND EXISTS ");
		sb.append("              (SELECT 1 ");
		sb.append("                 FROM wo_mst_user u "); 
		sb.append("                WHERE u.nik = :nik "); 
		sb.append("                      AND (u.user_id = pic.user_id_1 OR u.user_id = pic.user_id_2)) ");
		
		Query query = getSession().createSQLQuery(sb.toString());

		query.setParameter("nik", nik);

		return ((Date) query.getSingleResult());
	}

	@Override
	public Long getCountCpsaApproval(String nik) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT COUNT (1) "); 
		sb.append("   FROM WO_MST_CPSA_PIC pic "); 
		sb.append("        INNER JOIN WO_MST_CPSA cpsa ON pic.CPSA_ID = cpsa.CPSA_ID ");
		sb.append("        INNER JOIN WO_MST_PARAMETER_DTL param ON pic.STATUS_PIC_ID = param.PARAMETER_DTL_ID ");
		sb.append("  WHERE cpsa.enabled_flag = 'Y' ");
		sb.append("        AND pic.enabled_flag = 'Y' ");
		sb.append("        AND param.PARAMETER_DTL_CODE = 'CPSA_WAITING_APPROVAL' ");
		sb.append("        AND EXISTS ");
		sb.append("              (SELECT 1 ");
		sb.append("                 FROM wo_mst_user u "); 
		sb.append("                WHERE u.nik = :nik "); 
		sb.append("                      AND u.user_id = pic.user_id_3) ");
		
		Query query = getSession().createSQLQuery(sb.toString());

		query.setParameter("nik", nik);
		Long countData = new Long(0);
		if(query.getSingleResult() !=null) {
			countData = ((Number) query.getSingleResult()).longValue();
		}

		return countData;
	}

	@Override
	public Date getMaxDateCpsaApproval(String nik) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT MAX(NVL(pic.LAST_UPDATE_DATE, pic.creation_date)) "); 
		sb.append("   FROM WO_MST_CPSA_PIC pic "); 
		sb.append("        INNER JOIN WO_MST_CPSA cpsa ON pic.CPSA_ID = cpsa.CPSA_ID ");
		sb.append("        INNER JOIN WO_MST_PARAMETER_DTL param ON pic.STATUS_PIC_ID = param.PARAMETER_DTL_ID ");
		sb.append("  WHERE cpsa.enabled_flag = 'Y' ");
		sb.append("        AND pic.enabled_flag = 'Y' ");
		sb.append("        AND param.PARAMETER_DTL_CODE = 'CPSA_WAITING_APPROVAL' ");
		sb.append("        AND EXISTS ");
		sb.append("              (SELECT 1 ");
		sb.append("                 FROM wo_mst_user u "); 
		sb.append("                WHERE u.nik = :nik "); 
		sb.append("                      AND u.user_id = pic.user_id_3) ");
		
		Query query = getSession().createSQLQuery(sb.toString());

		query.setParameter("nik", nik);

		return ((Date) query.getSingleResult());
	}

	

	
}
