
package com.wo.module.dashboard.dao;

import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.wo.module.common.dao.GenericDAOHibernate;


@Repository("dashboardDao")
public class DashboardDaoImpl extends GenericDAOHibernate<Object, Long> implements DashboardDao {
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(DashboardDaoImpl.class);

	@Override
	public Long searchCountDataExternalRegulationWaitingApproval(String nikLogin, String action) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select count(1) ");
		sb.append(" from wo_tmp_regulation r1 ");
		sb.append(" where 1=1 ");
		sb.append(" and r1.enabled_flag = 'Y' ");
		sb.append(" and r1.status = 'DATA_NEW' ");
		sb.append(" AND r1.jenis_ketentuan = 'KETENTUAN_EKSTERNAL' ");
		sb.append(" AND EXISTS( ");
		
		queryUserResponsibility(sb, nikLogin, action);
		
		sb.append(" ) ");
		
		Query query = getSession().createSQLQuery(sb.toString());
		
		Number results = (Number) query.getSingleResult();
		
		if (results == null) {
			results = 0;
		}
		
		return results.longValue();
	}
	
	
	
	@Override
	public Long searchCountDataInternalRegulationWaitingApproval(String nikLogin, String action) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select count(1) ");
		sb.append(" from wo_tmp_regulation r1 ");
		sb.append(" where 1=1 ");
		sb.append(" and r1.enabled_flag = 'Y' ");
		sb.append(" and r1.status = 'DATA_NEW' ");
		sb.append(" AND r1.jenis_ketentuan = 'KETENTUAN_INTERNAL' ");
		sb.append(" AND EXISTS( ");
		
		queryUserResponsibility(sb, nikLogin, action);
		
		sb.append(" ) ");
		
		Query query = getSession().createSQLQuery(sb.toString());
		
		Number results = (Number) query.getSingleResult();
		
		if (results == null) {
			results = 0;
		}
		
		return results.longValue();
	}
	
	
	@Override
	public Long searchCountDataReportMatrixDiaryWaitingApproval(String nikLogin, String action) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select count(1) ");
		sb.append(" from wo_tmp_rmd r1 ");
		sb.append(" where 1=1 ");
		sb.append(" and r1.enabled_flag = 'Y' ");
		sb.append(" and r1.status = 'DATA_NEW' ");
		sb.append(" AND EXISTS( ");
		
		queryUserResponsibility(sb, nikLogin, action);
		
		sb.append(" ) ");
		
		Query query = getSession().createSQLQuery(sb.toString());
		
		Number results = (Number) query.getSingleResult();
		
		if (results == null) {
			results = 0;
		}
		
		return results.longValue();
	}
	
	
	@Override
	public Long searchCountDataCorrespondenceWaitingApproval(String nikLogin, String action) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select count(1) ");
//		sb.append(" from wo_tmp_correspondence r1 ");
		sb.append(" from wo_tmp_correspondence r1 "
				+ "		inner join ( select d.* "
				+ "			from wo_mst_parameter p left join wo_mst_parameter_dtl d"
				+ "				ON d.parameter_code = p.parameter_code "
				+ "				AND p.parameter_code = 'SENDER') pdSender on pdSender.parameter_dtl_code = r1.sender_code ");
		sb.append(" where 1=1 ");
		sb.append(" and r1.enabled_flag = 'Y' ");
		sb.append(" and r1.status = 'DATA_NEW' ");
		sb.append(" AND EXISTS( ");
		
		queryUserResponsibility(sb, nikLogin, action);
		
		sb.append(" ) ");
		
		Query query = getSession().createSQLQuery(sb.toString());
		
		Number results = (Number) query.getSingleResult();
		
		if (results == null) {
			results = 0;
		}
		
		return results.longValue();
	}
	
	@Override
	public Long searchCountDataRegulationSocializationWaitingApproval(String nikLogin, String action) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select count(1) ");
		sb.append(" from wo_tmp_socialization r1 ");
		sb.append(" where 1=1 ");
		sb.append(" and r1.enabled_flag = 'Y' ");
		sb.append(" and r1.status = 'DATA_NEW' ");
		sb.append(" AND EXISTS( ");
		
		queryUserResponsibility(sb, nikLogin, action);
		
		sb.append(" ) ");
		
		Query query = getSession().createSQLQuery(sb.toString());
		
		Number results = (Number) query.getSingleResult();
		
		if (results == null) {
			results = 0;
		}
		
		return results.longValue();
	}
	
	
	
	
	
	
	
	@Override
	public Long searchCountDataExternalRegulationRevised(String nikLogin, String action) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select count(1) ");
		sb.append(" from wo_tmp_regulation r1 ");
		sb.append(" where 1=1 ");
		sb.append(" and r1.enabled_flag = 'Y' ");
		sb.append(" and r1.status = 'DATA_REVISE' ");
		sb.append(" AND r1.jenis_ketentuan = 'KETENTUAN_EKSTERNAL' ");
		sb.append(" AND EXISTS( ");
		
		queryUserResponsibility(sb, nikLogin, action);
		
		sb.append(" ) ");
		
		Query query = getSession().createSQLQuery(sb.toString());
		
		Number results = (Number) query.getSingleResult();
		
		if (results == null) {
			results = 0;
		}
		
		return results.longValue();
	}
	
	
	
	@Override
	public Long searchCountDataInternalRegulationRevised(String nikLogin, String action) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select count(1) ");
		sb.append(" from wo_tmp_regulation r1 ");
		sb.append(" where 1=1 ");
		sb.append(" and r1.enabled_flag = 'Y' ");
		sb.append(" and r1.status = 'DATA_REVISE' ");
		sb.append(" AND r1.jenis_ketentuan = 'KETENTUAN_INTERNAL' ");
		sb.append(" AND EXISTS( ");
		
		queryUserResponsibility(sb, nikLogin, action);
		
		sb.append(" ) ");
		
		Query query = getSession().createSQLQuery(sb.toString());
		
		Number results = (Number) query.getSingleResult();
		
		if (results == null) {
			results = 0;
		}
		
		return results.longValue();
	}
	
	
	@Override
	public Long searchCountDataReportMatrixDiaryRevised(String nikLogin, String action) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select count(1) ");
		sb.append(" from wo_tmp_rmd r1 ");
		sb.append(" where 1=1 ");
		sb.append(" and r1.enabled_flag = 'Y' ");
		sb.append(" and r1.status = 'DATA_REVISE' ");
		sb.append(" AND EXISTS( ");
		
		queryUserResponsibility(sb, nikLogin, action);
		
		sb.append(" ) ");
		
		Query query = getSession().createSQLQuery(sb.toString());
		
		Number results = (Number) query.getSingleResult();
		
		if (results == null) {
			results = 0;
		}
		
		return results.longValue();
	}
	
	
	@Override
	public Long searchCountDataCorrespondenceRevised(String nikLogin, String action) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select count(1) ");
//		sb.append(" from wo_tmp_correspondence r1 ");
		sb.append(" from wo_tmp_correspondence r1 "
				+ "		inner join ( select d.* "
				+ "			from wo_mst_parameter p left join wo_mst_parameter_dtl d"
				+ "				ON d.parameter_code = p.parameter_code "
				+ "				AND p.parameter_code = 'SENDER')  pdSender on pdSender.parameter_dtl_code = r1.sender_code ");
		sb.append(" where 1=1 ");
		sb.append(" and r1.enabled_flag = 'Y' ");
		sb.append(" and r1.status = 'DATA_REVISE' ");
		sb.append(" AND EXISTS( ");
		
		queryUserResponsibility(sb, nikLogin, action);
		
		sb.append(" ) ");
		
		Query query = getSession().createSQLQuery(sb.toString());
		
		Number results = (Number) query.getSingleResult();
		
		if (results == null) {
			results = 0;
		}
		
		return results.longValue();
	}
	
	@Override
	public Long searchCountDataRegulationSocializationRevised(String nikLogin, String action) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select count(1) ");
		sb.append(" from wo_tmp_socialization r1 ");
		sb.append(" where 1=1 ");
		sb.append(" and r1.enabled_flag = 'Y' ");
		sb.append(" and r1.status = 'DATA_REVISE' ");
		sb.append(" AND EXISTS( ");
		
		queryUserResponsibility(sb, nikLogin, action);
		
		sb.append(" ) ");
		
		Query query = getSession().createSQLQuery(sb.toString());
		
		Number results = (Number) query.getSingleResult();
		
		if (results == null) {
			results = 0;
		}
		
		return results.longValue();
	}
	
	
	
	@Override
	public Long searchCountDataCorrespondenceVerification(String nikLogin, String action) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select count(1) ");
//		sb.append(" from wo_trc_correspondence r1 ");
		sb.append(" from wo_trc_correspondence r1 "
				+ "		inner join ( select d.* "
				+ "			from wo_mst_parameter p left join wo_mst_parameter_dtl d"
				+ "				ON d.parameter_code = p.parameter_code "
				+ "				AND p.parameter_code = 'SENDER')  pdSender on pdSender.parameter_dtl_code = r1.sender_code ");
		sb.append(" where 1=1 ");
		sb.append(" and r1.enabled_flag = 'Y' ");
		sb.append(" and r1.followup_status = 'PIC_DONE' ");
		sb.append(" and (r1.compliance_status <> 'COMPLIANCE_CLOSE' or r1.compliance_status is null) ");		
		sb.append(" AND (EXISTS( ");
		
		queryUserResponsibilityForVerification(sb, nikLogin, action);
		
		sb.append(" ) OR (exists (select 1 from WO_TRC_CORRESPDC_PIC_CMPLC wtspc where wtspc.correspondence_id = r1.correspondence_id and wtspc.user_id = (select user_id from wo_mst_user where nik = '"+nikLogin+"')))) ");
		
		Query query = getSession().createSQLQuery(sb.toString());
		
		Number results = (Number) query.getSingleResult();
		
		if (results == null) {
			results = 0;
		}
		
		return results.longValue();
	}
	
	@Override
	public Long searchCountDataRegulationSocializationVerification(String nikLogin, String action) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select count(1) ");
		sb.append(" from wo_trc_socialization r1 ");
		sb.append(" inner join wo_trc_socialization_rgltn sr on sr.socialization_id = r1.socialization_id ");
		sb.append(" inner join wo_trc_socialization_pic_fp f on f.socialization_id = r1.socialization_id  ");
		sb.append(" where 1=1 ");
		sb.append(" and r1.enabled_flag = 'Y' ");
		//sb.append(" and r1.status = 'DATA_ACTIVE' ");
		sb.append(" and sr.primary_flag = 'Y' ");
		sb.append(" and f.followup_status = 'PIC_DONE'  ");
		sb.append(" and (f.compliance_status <> 'COMPLIANCE_CLOSE' or f.compliance_status is null) ");
		sb.append(" AND (EXISTS( ");
		
		queryUserResponsibilityForVerification(sb, nikLogin, action);
		
		sb.append(" ) OR exists (select 1 from wo_trc_socialization_pic_cmplc wtspc where wtspc.socialization_id = r1.socialization_id and wtspc.user_id = (select user_id from wo_mst_user where nik = '"+nikLogin+"'))) ");
		
		
		Query query = getSession().createSQLQuery(sb.toString());
		
		Number results = (Number) query.getSingleResult();
		
		if (results == null) {
			results = 0;
		}
		
		return results.longValue();
	}
	
	
	
	
	public void queryUserResponsibility(StringBuilder sb, String nikLogin, String action) {
		sb.append(" select 1 ");
		sb.append(" from wo_mst_responsibility r,");
		sb.append(" wo_mst_responsibility_dtl rd, ");
		sb.append(" wo_mst_menu m, ");		
		sb.append(" wo_mst_user u ");
		sb.append(" WHERE 1=1  ");
		sb.append(" and r.enabled_flag = 'Y' ");
		sb.append(" AND r.responsibility_id = u.responsibility_id ");
		sb.append(" AND r.responsibility_id = rd.responsibility_id ");
		sb.append(" AND rd.menu_id = m.menu_id ");		
		sb.append(" AND u.nik = '"+ nikLogin + "' ");
		sb.append(" AND m.action = '"+ action.substring(11) + "' ");
		sb.append(" and u.enabled_flag = 'Y' ");
		sb.append(" and m.enabled_flag = 'Y' ");
	}
	
	public void queryUserResponsibilityForVerification(StringBuilder sb, String nikLogin, String action) {
		sb.append(" select 1 ");
		sb.append(" from wo_mst_responsibility r,");
		sb.append(" wo_mst_responsibility_dtl rd, ");
		sb.append(" wo_mst_menu m, ");		
		sb.append(" wo_mst_user u ");
		sb.append(" WHERE 1=1  ");
		sb.append(" and r.enabled_flag = 'Y' ");
		sb.append(" AND r.responsibility_id = u.responsibility_id ");
		sb.append(" AND r.responsibility_id = rd.responsibility_id ");
		sb.append(" AND rd.menu_id = m.menu_id ");		
		sb.append(" AND u.nik = '"+ nikLogin + "' ");
		sb.append(" AND UPPER(r.name) = UPPER('Super Administrator') ");
		//sb.append(" AND m.action = '"+ action.substring(11) + "' ");
		sb.append(" and u.enabled_flag = 'Y' ");
		sb.append(" and m.enabled_flag = 'Y' ");
	}

	@Override
	public Long searchCountDataAuditWaitingApproval(String nikLogin, String action) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select count(1) ");
		sb.append(" from wo_tmp_audit r1 ");
		sb.append(" where 1=1 ");
		sb.append(" and r1.enabled_flag = 'Y' ");
		sb.append(" and r1.status = 'DATA_NEW' ");
		sb.append(" AND EXISTS( ");
		
		queryUserResponsibility(sb, nikLogin, action);
		
		sb.append(" ) ");
		
		Query query = getSession().createSQLQuery(sb.toString());
		
		Number results = (Number) query.getSingleResult();
		
		if (results == null) {
			results = 0;
		}
		
		return results.longValue();
	}

	@Override
	public Long searchCountDataAuditRevised(String nikLogin, String action) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select count(1) ");
		sb.append(" from wo_tmp_audit r1 ");
		sb.append(" where 1=1 ");
		sb.append(" and r1.enabled_flag = 'Y' ");
		sb.append(" and r1.status = 'DATA_REVISE' ");
		sb.append(" AND EXISTS( ");
		
		queryUserResponsibility(sb, nikLogin, action);
		
		sb.append(" ) ");
		
		Query query = getSession().createSQLQuery(sb.toString());
		
		Number results = (Number) query.getSingleResult();
		
		if (results == null) {
			results = 0;
		}
		
		return results.longValue();
	}



	@Override
	public Long searchCountDataAuditVerification(String nikLogin, String action) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select count(1) "+
		" from wo_trc_audit r1 "+
		" INNER JOIN WO_TRC_AUDIT_CHECK_POINT p ON p.AUDIT_ID = r1.AUDIT_ID " +
		" INNER JOIN WO_TRC_AUDIT_PC_FP_BANK_CMITMT c ON c.AUDIT_CHECK_POINT_ID = p.AUDIT_CHECK_POINT_ID " +
		" INNER join wo_trc_audit_pic_followup f on f.AUD_PC_FP_BANK_CMITMT_ID = c.AUD_PC_FP_BANK_CMITMT_ID " + 
		" WHERE 1=1 " + 
		"	AND r1.enabled_flag = 'Y' " + 
		"	AND r1.status = 'DATA_ACTIVE' " + 
		"   AND (f.followup_status = 'PIC_DONE' " + 
		"      OR f.followup_status = 'PIC_EXTENSION') " + 
		"   AND (f.compliance_status <> 'COMPLIANCE_CLOSE' " + 
		"      OR f.compliance_status IS NULL) " + 
		"");
		//sb.append(" inner join wo_trc_audit_pic_followup f on f.audit_id = r1.audit_id  ");
		/*sb.append(" where 1=1 ");
		sb.append(" and r1.enabled_flag = 'Y' ");
		sb.append(" and f.followup_status = 'PIC_DONE'  ");
		sb.append(" and (f.compliance_status <> 'COMPLIANCE_CLOSE' or f.compliance_status is null) ");*/
		
		sb.append(" AND (EXISTS( ");
		
		queryUserResponsibilityForVerification(sb, nikLogin, action);
		
		sb.append(" ) OR exists (select 1 from wo_trc_audit_pic_compliance wtspc where wtspc.audit_id = r1.audit_id and wtspc.user_id = (select user_id from wo_mst_user where nik = '"+nikLogin+"'))) ");
		
		
		Query query = getSession().createSQLQuery(sb.toString());
		
		Number results = (Number) query.getSingleResult();
		
		if (results == null) {
			results = 0;
		}
		
		return results.longValue();
	}



	@Override
	public Long searchCountDataComplianceOnsiteReviewWaitingApproval(String nikLogin, String action) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select count(1) ");
		sb.append(" from wo_tmp_compliance_review r1 ");
		sb.append(" where 1=1 ");
		sb.append(" and r1.enabled_flag = 'Y' ");
		sb.append(" and r1.status = 'DATA_NEW' ");
		sb.append(" AND EXISTS( ");
		
		queryUserResponsibility(sb, nikLogin, action);
		
		sb.append(" ) ");
		
		Query query = getSession().createSQLQuery(sb.toString());
		
		Number results = (Number) query.getSingleResult();
		
		if (results == null) {
			results = 0;
		}
		
		return results.longValue();
	}



	@Override
	public Long searchCountDataComplianceOnsiteReviewRevised(String nikLogin, String action) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select count(1) ");
		sb.append(" from wo_tmp_compliance_review r1 ");
		sb.append(" where 1=1 ");
		sb.append(" and r1.enabled_flag = 'Y' ");
		sb.append(" and r1.status = 'DATA_REVISE' ");
		sb.append(" AND EXISTS( ");
		
		queryUserResponsibility(sb, nikLogin, action);
		
		sb.append(" ) ");
		
		Query query = getSession().createSQLQuery(sb.toString());
		
		Number results = (Number) query.getSingleResult();
		
		if (results == null) {
			results = 0;
		}
		
		return results.longValue();
	}



	@Override
	public Long searchCountDataComplianceOnsiteReviewVerification(String nikLogin, String action) {
		StringBuilder sb = new StringBuilder();
		/*sb.append(" select count(1) ");
		sb.append(" from wo_trc_compliance_review r1 ");
		sb.append(" inner join wo_trc_cmplc_review_pic_fp f on f.compliance_review_id = r1.compliance_review_id  ");
		sb.append(" where 1=1 ");
		sb.append(" and r1.enabled_flag = 'Y' ");
		sb.append(" and f.followup_status = 'PIC_DONE'  ");
		sb.append(" and (f.compliance_status <> 'COMPLIANCE_CLOSE' or f.compliance_status is null) ");*/
		sb.append(" SELECT count(1)" + 
				" FROM WO_TRC_COMPLIANCE_TESTING r1" +
				" LEFT JOIN WO_TRC_COMPLIANCE_TESTING_DTL ctd ON r1.COMPLIANCE_TESTING_ID = ctd.COMPLIANCE_TESTING_ID " +
				" LEFT JOIN WO_TRC_COMP_TEST_PIC_FP f ON ctd.COMPLIANCE_TESTING_DTL_ID = f.COMPLIANCE_TESTING_DTL_ID" + 
				" LEFT JOIN wo_mst_user u ON u.USER_ID = f.user_id_1" + 
				" LEFT JOIN wo_mst_parameter_dtl pd ON pd.parameter_dtl_code = f.FOLLOWUP_STATUS" + 
				//" LEFT JOIN wo_mst_parameter_dtl pd4 ON pd4.parameter_dtl_code = ta.STATUS" + 
				" WHERE 1=1 and r1.enabled_flag = 'Y' ");
		sb.append(" AND r1.status = 'DATA_ACTIVE' ");
		sb.append(" and (f.followup_status = 'PIC_DONE' OR f.followup_status = 'PIC_EXTENSION') and (f.compliance_status <> 'COMPLIANCE_CLOSE' or f.compliance_status is null) ");

		sb.append(" AND (EXISTS( ");
		
		queryUserResponsibilityForVerification(sb, nikLogin, action);
		
		sb.append(" ) OR exists (select 1 from WO_TRC_COMP_TEST_PIC_RVW trccrpc where trccrpc.COMPLIANCE_TESTING_ID = r1.COMPLIANCE_TESTING_ID and trccrpc.user_id = (select user_id from wo_mst_user where nik = '"+nikLogin+"'))) ");
		
		
		Query query = getSession().createSQLQuery(sb.toString());
		
		Number results = (Number) query.getSingleResult();
		
		if (results == null) {
			results = 0;
		}
		
		return results.longValue();
	}



	@Override
	public Long searchCountDataCorrespondenceAmlWaitingApproval(String nikLogin, String action) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select count(1) ");
//		sb.append(" from wo_tmp_correspondence r1 ");
		sb.append(" from wo_tmp_correspondence r1 "
				+ "		inner join ( select d.* "
				+ "			from wo_mst_parameter p left join wo_mst_parameter_dtl d"
				+ "				ON d.parameter_code = p.parameter_code "
				+ "				AND p.parameter_code = 'SENDER_AML') pdSender on pdSender.parameter_dtl_code = r1.sender_code ");
		sb.append(" where 1=1 ");
		sb.append(" and r1.enabled_flag = 'Y' ");
		sb.append(" and r1.status = 'DATA_NEW' ");
		sb.append(" AND EXISTS( ");
		
		queryUserResponsibility(sb, nikLogin, action);
		
		sb.append(" ) ");
		
		Query query = getSession().createSQLQuery(sb.toString());
		
		Number results = (Number) query.getSingleResult();
		
		if (results == null) {
			results = 0;
		}
		
		return results.longValue();
	}



	@Override
	public Long searchCountDataCorrespondenceAmlRevised(String nikLogin, String action) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select count(1) ");
//		sb.append(" from wo_tmp_correspondence r1 ");
		sb.append(" from wo_tmp_correspondence r1 "
				+ "		inner join ( select d.* "
				+ "			from wo_mst_parameter p left join wo_mst_parameter_dtl d"
				+ "				ON d.parameter_code = p.parameter_code "
				+ "				AND p.parameter_code = 'SENDER_AML')  pdSender on pdSender.parameter_dtl_code = r1.sender_code ");
		sb.append(" where 1=1 ");
		sb.append(" and r1.enabled_flag = 'Y' ");
		sb.append(" and r1.status = 'DATA_REVISE' ");
		sb.append(" AND EXISTS( ");
		
		queryUserResponsibility(sb, nikLogin, action);
		
		sb.append(" ) ");
		
		Query query = getSession().createSQLQuery(sb.toString());
		
		Number results = (Number) query.getSingleResult();
		
		if (results == null) {
			results = 0;
		}
		
		return results.longValue();
	}



	@Override
	public Long searchCountDataCorrespondenceAmlVerification(String nikLogin, String action) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select count(1) ");
//		sb.append(" from wo_trc_correspondence r1 ");
		sb.append(" from wo_trc_correspondence r1 "
				+ "		inner join ( select d.* "
				+ "			from wo_mst_parameter p left join wo_mst_parameter_dtl d"
				+ "				ON d.parameter_code = p.parameter_code "
				+ "				AND p.parameter_code = 'SENDER_AML')  pdSender on pdSender.parameter_dtl_code = r1.sender_code ");
		sb.append(" where 1=1 ");
		sb.append(" and r1.enabled_flag = 'Y' ");
		sb.append(" and r1.followup_status = 'PIC_DONE' ");
		sb.append(" and (r1.compliance_status <> 'COMPLIANCE_CLOSE' or r1.compliance_status is null) ");		
		sb.append(" AND EXISTS( ");
		
		queryUserResponsibilityForVerification(sb, nikLogin, action);
		
		sb.append(" ) ");
		
		Query query = getSession().createSQLQuery(sb.toString());
		
		Number results = (Number) query.getSingleResult();
		
		if (results == null) {
			results = 0;
		}
		
		return results.longValue();
	}



	@Override
	public Long searchCountDataQAAdmin(String nikLogin, String action) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select count(1) ");
		sb.append("	from wo_mst_qna r1 ");
		sb.append(" where 1=1 ");
		sb.append(" 	and r1.enabled_flag = 'Y' ");
		sb.append(" 	and r1.q_status = 'QNA_STATUS_NEW' ");
		sb.append(" 	AND EXISTS ( ");

		queryUserResponsibility(sb, nikLogin, action);

		sb.append(" ) ");

		Query query = getSession().createSQLQuery(sb.toString());

		Number results = (Number) query.getSingleResult();

		if (results == null) {
			results = 0;
		}

		return results.longValue();
	}



	@Override
	public Long searchCountDataQAPIC(String nikLogin, String action) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select count(1) ");
		sb.append("	from wo_mst_qna r1, ");
		sb.append(" 	 wo_mst_user u1 ");
		sb.append(" where 1=1 ");
		sb.append(" 	and r1.enabled_flag = 'Y' ");
		sb.append("     and r1.q_status = 'QNA_STATUS_IP' ");
		sb.append("     and r1.a_user_id = u1.user_id ");
		sb.append("     and u1.nik = :nikLogin ");
		sb.append(" 	AND EXISTS ( ");

		queryUserResponsibility(sb, nikLogin, action);

		sb.append(" ) ");

		Query query = getSession().createSQLQuery(sb.toString());
		query.setParameter("nikLogin", nikLogin);

		Number results = (Number) query.getSingleResult();

		if (results == null) {
			results = 0;
		}

		return results.longValue();
	}



	@Override
	public Long searchCountDataQAView(String nikLogin, String action) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select count(1) ");
		sb.append("	from wo_mst_qna r1, ");
		sb.append(" 	 wo_mst_user u1 ");
		sb.append(" where 1=1 ");
		sb.append(" 	and r1.enabled_flag = 'Y' ");
		sb.append(" 	and r1.q_status = 'QNA_STATUS_CLOSE' ");
		sb.append(" 	and r1.q_user_id = u1.user_id ");
		sb.append(" 	and (r1.read_flag is null or r1.read_flag = 'N') ");
		sb.append(" 	and u1.nik = :nikLogin ");
		sb.append(" 	AND EXISTS ( ");

		queryUserResponsibility(sb, nikLogin, action);

		sb.append(" ) ");

		Query query = getSession().createSQLQuery(sb.toString());
		query.setParameter("nikLogin", nikLogin);

		Number results = (Number) query.getSingleResult();

		if (results == null) {
			results = 0;
		}

		return results.longValue();
	}
	
	//Article
	@Override
	public Long searchCountDataArticleWaitingApproval(String nikLogin, String action) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select count(1) ");
		sb.append(" from wo_tmp_article r1 ");
		sb.append(" where 1=1 ");
		sb.append(" and r1.enabled_flag = 'Y' ");
		sb.append(" and r1.status = 'DATA_NEW' ");
		sb.append(" AND EXISTS( ");
		
		queryUserResponsibility(sb, nikLogin, action);
		
		sb.append(" ) ");
		
		Query query = getSession().createSQLQuery(sb.toString());
		
		Number results = (Number) query.getSingleResult();
		
		if (results == null) {
			results = 0;
		}
		
		return results.longValue();
	}



	@Override
	public Long searchCountDataArticleRevised(String nikLogin, String action) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select count(1) ");
		sb.append(" from wo_tmp_article r1 ");
		sb.append(" where 1=1 ");
		sb.append(" and r1.enabled_flag = 'Y' ");
		sb.append(" and r1.status = 'DATA_REVISE' ");
		sb.append(" AND EXISTS( ");
		
		queryUserResponsibility(sb, nikLogin, action);
		
		sb.append(" ) ");
		
		Query query = getSession().createSQLQuery(sb.toString());
		
		Number results = (Number) query.getSingleResult();
		
		if (results == null) {
			results = 0;
		}
		
		return results.longValue();
	}



	//Regulation Monitoring
	@Override
	public Long searchCountDataRegulationMonitoringWaitingApproval(String nikLogin, String action) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select count(1) ");
		sb.append(" from wo_tmp_reg_monitoring r1 ");
		sb.append(" where 1=1 ");
		sb.append(" and r1.enabled_flag = 'Y' ");
		sb.append(" and r1.status = 'DATA_NEW' ");
		sb.append(" AND EXISTS( ");
		
		queryUserResponsibility(sb, nikLogin, action);
		
		sb.append(" ) ");
		
		Query query = getSession().createSQLQuery(sb.toString());
		
		Number results = (Number) query.getSingleResult();
		
		if (results == null) {
			results = 0;
		}
		
		return results.longValue();
	}



	@Override
	public Long searchCountDataRegulationMonitoringRevised(String nikLogin, String action) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select count(1) ");
		sb.append(" from wo_tmp_reg_monitoring r1 ");
		sb.append(" where 1=1 ");
		sb.append(" and r1.enabled_flag = 'Y' ");
		sb.append(" and r1.status = 'DATA_REVISE' ");
		sb.append(" AND EXISTS( ");
		
		queryUserResponsibility(sb, nikLogin, action);
		
		sb.append(" ) ");
		
		Query query = getSession().createSQLQuery(sb.toString());
		
		Number results = (Number) query.getSingleResult();
		
		if (results == null) {
			results = 0;
		}
		
		return results.longValue();
	}



	@Override
	public Long searchCountDataRegulationMonitoringVerification(String nikLogin, String action) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select count(1) ");
		sb.append(" from wo_trc_reg_monitoring r1 ");
		sb.append(" inner join wo_trc_reg_monitoring_pic_fp f on f.reg_monitoring_id = r1.reg_monitoring_id  ");
		sb.append(" where 1=1 ");
		sb.append(" and r1.enabled_flag = 'Y' ");
		sb.append(" and f.followup_status = 'PIC_DONE'  ");
		sb.append(" and (f.compliance_status <> 'COMPLIANCE_CLOSE' or f.compliance_status is null) ");
		sb.append(" AND (EXISTS( ");
		
		queryUserResponsibilityForVerification(sb, nikLogin, action);
		
		sb.append(" ) OR exists (select 1 from WO_TRC_REG_MONITOR_PIC_CMPLC wtspc where wtspc.REG_MONITORING_ID = r1.REG_MONITORING_ID and wtspc.user_id = (select user_id from wo_mst_user where nik = '"+nikLogin+"'))) ");
		
		
		Query query = getSession().createSQLQuery(sb.toString());
		
		Number results = (Number) query.getSingleResult();
		
		if (results == null) {
			results = 0;
		}
		
		return results.longValue();
	}
	
	//Fine
	@Override
	public Long searchCountDataFineWaitingApproval(String nikLogin, String action) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select count(1) ");
		sb.append(" from wo_tmp_fine r1 ");
		sb.append(" where 1=1 ");
		sb.append(" and r1.enabled_flag = 'Y' ");
		sb.append(" and r1.status = 'DATA_NEW' ");
		sb.append(" AND EXISTS( ");
		
		queryUserResponsibility(sb, nikLogin, action);
		
		sb.append(" ) ");
		
		Query query = getSession().createSQLQuery(sb.toString());
		
		Number results = (Number) query.getSingleResult();
		
		if (results == null) {
			results = 0;
		}
		
		return results.longValue();
	}



	@Override
	public Long searchCountDataFineRevised(String nikLogin, String action) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select count(1) ");
		sb.append(" from wo_tmp_fine r1 ");
		sb.append(" where 1=1 ");
		sb.append(" and r1.enabled_flag = 'Y' ");
		sb.append(" and r1.status = 'DATA_REVISE' ");
		sb.append(" AND EXISTS( ");
		
		queryUserResponsibility(sb, nikLogin, action);
		
		sb.append(" ) ");
		
		Query query = getSession().createSQLQuery(sb.toString());
		
		Number results = (Number) query.getSingleResult();
		
		if (results == null) {
			results = 0;
		}
		
		return results.longValue();
	}



	@Override
	public Long searchCountDataFineVerification(String nikLogin, String action) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select count(1) ");
		sb.append(" from wo_trc_fine r1 ");
		sb.append(" inner join wo_trc_fine_pic_followup f on f.fine_id = r1.fine_id  ");
		sb.append(" where 1=1 ");
		sb.append(" and r1.enabled_flag = 'Y' ");
		sb.append(" and f.followup_status = 'PIC_DONE'  ");
		sb.append(" and (f.compliance_status <> 'COMPLIANCE_CLOSE' or f.compliance_status is null) ");
		sb.append(" AND (EXISTS( ");
		
		queryUserResponsibilityForVerification(sb, nikLogin, action);
		
		sb.append(" ) OR exists (select 1 from WO_TRC_FINE_PIC_COMPLIANCE wtspc where wtspc.fine_id = r1.fine_id and wtspc.user_id = (select user_id from wo_mst_user where nik = '"+nikLogin+"'))) ");
		
		
		Query query = getSession().createSQLQuery(sb.toString());
		
		Number results = (Number) query.getSingleResult();
		
		if (results == null) {
			results = 0;
		}
		
		return results.longValue();
	}



	@Override
	public Long searchCountDataFAQApproval(String nikLogin, String action) {
		StringBuilder sb = new StringBuilder();
		
		sb.append(" select count(1) ");
		sb.append(" from WO_TMP_FAQ tf ");
		sb.append(" where 1 = 1 ");
		sb.append(" and tf.ENABLED_FLAG = 'Y' ");
		sb.append(" and tf.STATUS = 'DATA_NEW' ");
		sb.append(" AND EXISTS( ");
		
		queryUserResponsibility(sb, nikLogin, action);
		
		sb.append(" ) ");
		
		Query query = getSession().createSQLQuery(sb.toString());
		
		Number results = (Number) query.getSingleResult();
		
		if (results == null) {
			results = 0;
		}
		
		return results.longValue();
	}



	@Override
	public Long searchCountDataCpsaVerification(String nikLogin, String action) {
		StringBuilder sb = new StringBuilder();
		
		sb.append(" SELECT COUNT(1) ");
		sb.append("   FROM WO_MST_CPSA c ");
		sb.append("        INNER JOIN WO_MST_PARAMETER_DTL pdCpsaType ");
		sb.append("                ON pdCpsaType.PARAMETER_DTL_CODE = c.CPSA_TYPE ");
		sb.append("        INNER JOIN (SELECT DISTINCT pic2.CPSA_ID ");
		sb.append("                      FROM WO_MST_CPSA_PIC pic2 ");
		sb.append("                           INNER JOIN WO_MST_PARAMETER_DTL dtl ON pic2.STATUS_PIC_ID = dtl.PARAMETER_DTL_ID ");
		sb.append("                     WHERE dtl.PARAMETER_DTL_CODE = 'CPSA_APPROVED' ");
		sb.append("                           AND dtl.PARAMETER_CODE = 'CPSA_STATUS') pic ON c.CPSA_ID = pic.CPSA_ID ");
		sb.append(" WHERE 1 = 1 ");
		sb.append("       AND c.ENABLED_FLAG <> 'N' ");
		sb.append(" AND EXISTS( ");
		
		queryUserResponsibility(sb, nikLogin, action);
		
		sb.append(" ) ");
		
		Query query = getSession().createSQLQuery(sb.toString());
		
		Number results = (Number) query.getSingleResult();
		
		if (results == null) {
			results = 0;
		}
		
		return results.longValue();
	}
}