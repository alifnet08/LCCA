package com.wo.module.report.reportLogUser.dao;

import java.io.Serializable;
import java.text.SimpleDateFormat;
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
import com.wo.module.report.reportLogUser.constant.ReportLogUserConstant;
import com.wo.module.report.reportLogUser.vo.ReportLogUserDiagramVo;
import com.wo.module.report.reportLogUser.vo.ReportLogUserVo;

@Repository("reportLogUserDao")
public class ReportLogUserDaoImpl extends GenericDAOHibernate<ReportGen, Long>
	implements ReportLogUserDao, Serializable{

	private static final long serialVersionUID = 3980849334582282447L;

	@SuppressWarnings("rawtypes")
	private void setQueryWhereString(StringBuilder sb, List<? extends SearchObject> searchCriteria) {
		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString();
				
				if (!StringUtils.isBlank(val)) {
					if (StringUtils.equals(ReportLogUserConstant.WHERE_CREATE_DATE_FROM, col)) {
						sb.append(" AND TRUNC(au.creation_date) >= TO_DATE(:startDate, 'yyyy-MM-dd') ");
					}
					if (StringUtils.equals(ReportLogUserConstant.WHERE_CREATE_DATE_TO, col)) {
						sb.append(" AND TRUNC(au.creation_date) <= TO_DATE(:endDate, 'yyyy-MM-dd') ");
					}
				}
				
			}
		}
	}
	
	@SuppressWarnings("rawtypes")
	private void setQueryWhereStringDiagram(StringBuilder sb, List<? extends SearchObject> searchCriteria) {
		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString();
				
				if (!StringUtils.isBlank(val)) {
					if (StringUtils.equals(ReportLogUserConstant.WHERE_CREATE_DATE_FROM, col)) {
						sb.append(" AND TRUNC(ACCESS_TIME) >= TO_DATE(:startDate, 'yyyy-MM-dd' ) ");
					}
					if (StringUtils.equals(ReportLogUserConstant.WHERE_CREATE_DATE_TO, col)) {
						sb.append(" AND TRUNC(ACCESS_TIME) <= TO_DATE(:endDate, 'yyyy-MM-dd' ) ");
					}
				}
				
			}
		}
	}
	
	@SuppressWarnings("rawtypes")
	private void setQuerySetString(Query query, List<? extends SearchObject> searchCriteria) {
		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString();
				
				if (!StringUtils.isBlank(val)) {
					if (StringUtils.equals(ReportLogUserConstant.WHERE_CREATE_DATE_FROM, col)) {
						query.setParameter("startDate", val);
					}
					if (StringUtils.equals(ReportLogUserConstant.WHERE_CREATE_DATE_TO, col)) {
						query.setParameter("endDate", val);
					}
				}
				
			}
		}
	}
	
	@SuppressWarnings("rawtypes")
	private void setQuerySetStringDiagram(Query query, List<? extends SearchObject> searchCriteria) {
		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString();
				
				if (!StringUtils.isBlank(val)) {
					if (StringUtils.equals(ReportLogUserConstant.WHERE_CREATE_DATE_FROM, col)) {
						query.setParameter("startDate", val);
					}
					if (StringUtils.equals(ReportLogUserConstant.WHERE_CREATE_DATE_TO, col)) {
						query.setParameter("endDate", val);
					}
				}
				
			}
		}
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public List<ReportLogUserVo> getReportLogUserDetailAsVo(List<? extends SearchObject> searchCriteria) {
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT * "
				+ "   FROM ( ");
		sb.append("SELECT 'Tentang LCCA' title, "
				+ "          au.our_name data, "
				+ "          au.created_by, "
				+ "          u1.name created_name, "
				+ "          au.creation_date created_date, "
				+ "          u1.position_name created_position, "
				+ "          u1.branch_name created_branch, "
				+ "          CASE "
				+ "             WHEN au.enabled_flag = 'Y' AND au.last_update_by IS NULL "
				+ "             THEN "
				+ "                'Dibuat' "
				+ "             WHEN au.enabled_flag = 'Y' AND au.last_update_by IS NOT NULL "
				+ "             THEN "
				+ "                'Diubah' "
				+ "             WHEN au.enabled_flag = 'N' AND au.last_update_by IS NOT NULL "
				+ "             THEN "
				+ "                'Dihapus' "
				+ "          END "
				+ "             last_status, "
				+ "          au.last_update_by, "
				+ "          u2.name last_update_name, "
				+ "          au.last_update_date, "
				+ "          u2.position_name last_update_position, "
				+ "          u2.branch_name last_update_branch "
				+ "     FROM wo_mst_about_us au "
				+ "          LEFT JOIN wo_mst_user u1 ON au.created_by = u1.nik "
				+ "          LEFT JOIN wo_mst_user u2 ON au.last_update_by = u2.nik "
				+ "    WHERE TRUNC(au.creation_date) >= TO_DATE(:startDate, 'yyyy-MM-dd') "
				+ "          AND TRUNC(au.creation_date) <= TO_DATE(:endDate, 'yyyy-MM-dd') "
				+ "   UNION ALL ");
		sb.append( "   SELECT 'Kantor Hukum' title, "
				+ "          au.advocate_name data, "
				+ "          au.created_by, "
				+ "          u1.name created_name, "
				+ "          au.creation_date created_date, "
				+ "          u1.position_name created_position, "
				+ "          u1.branch_name created_branch, "
				+ "          CASE "
				+ "             WHEN au.enabled_flag = 'Y' AND au.last_update_by IS NULL "
				+ "             THEN "
				+ "                'Dibuat' "
				+ "             WHEN au.enabled_flag = 'Y' AND au.last_update_by IS NOT NULL "
				+ "             THEN "
				+ "                'Diubah' "
				+ "             WHEN au.enabled_flag = 'N' AND au.last_update_by IS NOT NULL "
				+ "             THEN "
				+ "                'Dihapus' "
				+ "          END "
				+ "             last_status, "
				+ "          au.last_update_by, "
				+ "          u2.name last_update_name, "
				+ "          au.last_update_date, "
				+ "          u2.position_name last_update_position, "
				+ "          u2.branch_name last_update_branch "
				+ "     FROM wo_mst_advocate au "
				+ "          LEFT JOIN wo_mst_user u1 ON au.created_by = u1.nik "
				+ "          LEFT JOIN wo_mst_user u2 ON au.last_update_by = u2.nik "
				+ "     WHERE TRUNC(au.creation_date) >= TO_DATE(:startDate, 'yyyy-MM-dd') "
				+ "     AND TRUNC(au.creation_date) <= TO_DATE(:endDate, 'yyyy-MM-dd') "
				+ "   UNION ALL ");
		sb.append("   SELECT 'Pengumuman' title, "
				+ "          au.announcement_title data, "
				+ "          au.created_by, "
				+ "          u1.name created_name, "
				+ "          au.creation_date created_date, "
				+ "          u1.position_name created_position, "
				+ "          u1.branch_name created_branch, "
				+ "          CASE "
				+ "             WHEN au.enabled_flag = 'Y' AND au.last_update_by IS NULL "
				+ "             THEN "
				+ "                'Dibuat' "
				+ "             WHEN au.enabled_flag = 'Y' AND au.last_update_by IS NOT NULL "
				+ "             THEN "
				+ "                'Diubah' "
				+ "             WHEN au.enabled_flag = 'N' AND au.last_update_by IS NOT NULL "
				+ "             THEN "
				+ "                'Dihapus' "
				+ "          END "
				+ "             last_status, "
				+ "          au.last_update_by, "
				+ "          u2.name last_update_name, "
				+ "          au.last_update_date, "
				+ "          u2.position_name last_update_position, "
				+ "          u2.branch_name last_update_branch "
				+ "     FROM wo_mst_announcement au "
				+ "          LEFT JOIN wo_mst_user u1 ON au.created_by = u1.nik "
				+ "          LEFT JOIN wo_mst_user u2 ON au.last_update_by = u2.nik "
				+ "     WHERE TRUNC(au.creation_date) >= TO_DATE(:startDate, 'yyyy-MM-dd') "
				+ "     AND TRUNC(au.creation_date) <= TO_DATE(:endDate, 'yyyy-MM-dd') "
				+ "   UNION ALL ");
		sb.append("   SELECT 'Template Audit' title, "
				+ "          au.audit_template_name_in data, "
				+ "          au.created_by, "
				+ "          u1.name created_name, "
				+ "          au.creation_date created_date, "
				+ "          u1.position_name created_position, "
				+ "          u1.branch_name created_branch, "
				+ "          CASE "
				+ "             WHEN au.enabled_flag = 'Y' AND au.last_update_by IS NULL "
				+ "             THEN "
				+ "                'Dibuat' "
				+ "             WHEN au.enabled_flag = 'Y' AND au.last_update_by IS NOT NULL "
				+ "             THEN "
				+ "                'Diubah' "
				+ "             WHEN au.enabled_flag = 'N' AND au.last_update_by IS NOT NULL "
				+ "             THEN "
				+ "                'Dihapus' "
				+ "          END "
				+ "             last_status, "
				+ "          au.last_update_by, "
				+ "          u2.name last_update_name, "
				+ "          au.last_update_date, "
				+ "          u2.position_name last_update_position, "
				+ "          u2.branch_name last_update_branch "
				+ "     FROM wo_mst_audit au "
				+ "          LEFT JOIN wo_mst_user u1 ON au.created_by = u1.nik "
				+ "          LEFT JOIN wo_mst_user u2 ON au.last_update_by = u2.nik "
				+ "     WHERE TRUNC(au.creation_date) >= TO_DATE(:startDate, 'yyyy-MM-dd') "
				+ "     AND TRUNC(au.creation_date) <= TO_DATE(:endDate, 'yyyy-MM-dd') "
				+ "   UNION ALL ");
		sb.append("   SELECT 'Kalender' title, "
				+ "          au.calendar_event_in data, "
				+ "          au.created_by, "
				+ "          u1.name created_name, "
				+ "          au.creation_date created_date, "
				+ "          u1.position_name created_position, "
				+ "          u1.branch_name created_branch, "
				+ "          CASE "
				+ "             WHEN au.enabled_flag = 'Y' AND au.last_update_by IS NULL "
				+ "             THEN "
				+ "                'Dibuat' "
				+ "             WHEN au.enabled_flag = 'Y' AND au.last_update_by IS NOT NULL "
				+ "             THEN "
				+ "                'Diubah' "
				+ "             WHEN au.enabled_flag = 'N' AND au.last_update_by IS NOT NULL "
				+ "             THEN "
				+ "                'Dihapus' "
				+ "          END "
				+ "             last_status, "
				+ "          au.last_update_by, "
				+ "          u2.name last_update_name, "
				+ "          au.last_update_date, "
				+ "          u2.position_name last_update_position, "
				+ "          u2.branch_name last_update_branch "
				+ "     FROM wo_mst_calendar au "
				+ "          LEFT JOIN wo_mst_user u1 ON au.created_by = u1.nik "
				+ "          LEFT JOIN wo_mst_user u2 ON au.last_update_by = u2.nik "
				+ "     WHERE TRUNC(au.creation_date) >= TO_DATE(:startDate, 'yyyy-MM-dd') "
				+ "     AND TRUNC(au.creation_date) <= TO_DATE(:endDate, 'yyyy-MM-dd') "
				+ "   UNION ALL ");
		sb.append("   SELECT 'Tipe Reminder' title, "
				+ "          au.counter_type_in data, "
				+ "          au.created_by, "
				+ "          u1.name created_name, "
				+ "          au.creation_date created_date, "
				+ "          u1.position_name created_position, "
				+ "          u1.branch_name created_branch, "
				+ "          CASE "
				+ "             WHEN au.enabled_flag = 'Y' AND au.last_update_by IS NULL "
				+ "             THEN "
				+ "                'Dibuat' "
				+ "             WHEN au.enabled_flag = 'Y' AND au.last_update_by IS NOT NULL "
				+ "             THEN "
				+ "                'Diubah' "
				+ "             WHEN au.enabled_flag = 'N' AND au.last_update_by IS NOT NULL "
				+ "             THEN "
				+ "                'Dihapus' "
				+ "          END "
				+ "             last_status, "
				+ "          au.last_update_by, "
				+ "          u2.name last_update_name, "
				+ "          au.last_update_date, "
				+ "          u2.position_name last_update_position, "
				+ "          u2.branch_name last_update_branch "
				+ "     FROM wo_mst_counter_type au "
				+ "          LEFT JOIN wo_mst_user u1 ON au.created_by = u1.nik "
				+ "          LEFT JOIN wo_mst_user u2 ON au.last_update_by = u2.nik "
				+ "     WHERE TRUNC(au.creation_date) >= TO_DATE(:startDate, 'yyyy-MM-dd') "
				+ "     AND TRUNC(au.creation_date) <= TO_DATE(:endDate, 'yyyy-MM-dd') "
				+ "   UNION ALL ");
		sb.append("   SELECT 'CPSA' title, "
				+ "          au.cpsa_name data, "
				+ "          au.created_by, "
				+ "          u1.name created_name, "
				+ "          au.creation_date created_date, "
				+ "          u1.position_name created_position, "
				+ "          u1.branch_name created_branch, "
				+ "          CASE "
				+ "             WHEN au.enabled_flag = 'Y' AND au.last_update_by IS NULL "
				+ "             THEN "
				+ "                'Dibuat' "
				+ "             WHEN au.enabled_flag = 'Y' AND au.last_update_by IS NOT NULL "
				+ "             THEN "
				+ "                'Diubah' "
				+ "             WHEN au.enabled_flag = 'N' AND au.last_update_by IS NOT NULL "
				+ "             THEN "
				+ "                'Dihapus' "
				+ "          END "
				+ "             last_status, "
				+ "          au.last_update_by, "
				+ "          u2.name last_update_name, "
				+ "          au.last_update_date, "
				+ "          u2.position_name last_update_position, "
				+ "          u2.branch_name last_update_branch "
				+ "     FROM wo_mst_cpsa au "
				+ "          LEFT JOIN wo_mst_user u1 ON au.created_by = u1.nik "
				+ "          LEFT JOIN wo_mst_user u2 ON au.last_update_by = u2.nik "
				+ "     WHERE TRUNC(au.creation_date) >= TO_DATE(:startDate, 'yyyy-MM-dd') "
				+ "     AND TRUNC(au.creation_date) <= TO_DATE(:endDate, 'yyyy-MM-dd') "
				+ "   UNION ALL ");
		sb.append("   SELECT 'Database Compliance' title, "
				+ "          au.report_name_in data, "
				+ "          au.created_by, "
				+ "          u1.name created_name, "
				+ "          au.creation_date created_date, "
				+ "          u1.position_name created_position, "
				+ "          u1.branch_name created_branch, "
				+ "          CASE "
				+ "             WHEN au.enabled_flag = 'Y' AND au.last_update_by IS NULL "
				+ "             THEN "
				+ "                'Dibuat' "
				+ "             WHEN au.enabled_flag = 'Y' AND au.last_update_by IS NOT NULL "
				+ "             THEN "
				+ "                'Diubah' "
				+ "             WHEN au.enabled_flag = 'N' AND au.last_update_by IS NOT NULL "
				+ "             THEN "
				+ "                'Dihapus' "
				+ "          END "
				+ "             last_status, "
				+ "          au.last_update_by, "
				+ "          u2.name last_update_name, "
				+ "          au.last_update_date, "
				+ "          u2.position_name last_update_position, "
				+ "          u2.branch_name last_update_branch "
				+ "     FROM wo_mst_database_compliance au "
				+ "          LEFT JOIN wo_mst_user u1 ON au.created_by = u1.nik "
				+ "          LEFT JOIN wo_mst_user u2 ON au.last_update_by = u2.nik "
				+ "     WHERE TRUNC(au.creation_date) >= TO_DATE(:startDate, 'yyyy-MM-dd') "
				+ "     AND TRUNC(au.creation_date) <= TO_DATE(:endDate, 'yyyy-MM-dd') "
				+ "   UNION ALL ");
		sb.append("   SELECT 'Group Discussion' title, "
				+ "          au.thread_name_in data, "
				+ "          au.created_by, "
				+ "          u1.name created_name, "
				+ "          au.creation_date created_date, "
				+ "          u1.position_name created_position, "
				+ "          u1.branch_name created_branch, "
				+ "          CASE "
				+ "             WHEN au.enabled_flag = 'Y' AND au.last_update_by IS NULL "
				+ "             THEN "
				+ "                'Dibuat' "
				+ "             WHEN au.enabled_flag = 'Y' AND au.last_update_by IS NOT NULL "
				+ "             THEN "
				+ "                'Diubah' "
				+ "             WHEN au.enabled_flag = 'N' AND au.last_update_by IS NOT NULL "
				+ "             THEN "
				+ "                'Dihapus' "
				+ "          END "
				+ "             last_status, "
				+ "          au.last_update_by, "
				+ "          u2.name last_update_name, "
				+ "          au.last_update_date, "
				+ "          u2.position_name last_update_position, "
				+ "          u2.branch_name last_update_branch "
				+ "     FROM wo_mst_discussion au "
				+ "          LEFT JOIN wo_mst_user u1 ON au.created_by = u1.nik "
				+ "          LEFT JOIN wo_mst_user u2 ON au.last_update_by = u2.nik "
				+ "     WHERE TRUNC(au.creation_date) >= TO_DATE(:startDate, 'yyyy-MM-dd') "
				+ "     AND TRUNC(au.creation_date) <= TO_DATE(:endDate, 'yyyy-MM-dd') "
				+ "   UNION ALL ");
		sb.append("   SELECT 'Group Discussion Post' title, "
				+ "          d.thread_name_in data, "
				+ "          au.created_by, "
				+ "          u1.name created_name, "
				+ "          au.creation_date created_date, "
				+ "          u1.position_name created_position, "
				+ "          u1.branch_name created_branch, "
				+ "          CASE "
				+ "             WHEN au.enabled_flag = 'Y' AND au.last_update_by IS NULL "
				+ "             THEN "
				+ "                'Dibuat' "
				+ "             WHEN au.enabled_flag = 'Y' AND au.last_update_by IS NOT NULL "
				+ "             THEN "
				+ "                'Diubah' "
				+ "             WHEN au.enabled_flag = 'N' AND au.last_update_by IS NOT NULL "
				+ "             THEN "
				+ "                'Dihapus' "
				+ "          END "
				+ "             last_status, "
				+ "          au.last_update_by, "
				+ "          u2.name last_update_name, "
				+ "          au.last_update_date, "
				+ "          u2.position_name last_update_position, "
				+ "          u2.branch_name last_update_branch "
				+ "     FROM wo_mst_discussion_post au "
				+ "          INNER JOIN wo_mst_discussion d "
				+ "             ON au.discussion_id = d.discussion_id "
				+ "          LEFT JOIN wo_mst_user u1 ON au.created_by = u1.nik "
				+ "          LEFT JOIN wo_mst_user u2 ON au.last_update_by = u2.nik "
				+ "     WHERE TRUNC(au.creation_date) >= TO_DATE(:startDate, 'yyyy-MM-dd') "
				+ "     AND TRUNC(au.creation_date) <= TO_DATE(:endDate, 'yyyy-MM-dd') "
				+ "   UNION ALL ");
		sb.append("   SELECT 'Tipe Dokumen' title, "
				+ "          au.document_type_in data, "
				+ "          au.created_by, "
				+ "          u1.name created_name, "
				+ "          au.creation_date created_date, "
				+ "          u1.position_name created_position, "
				+ "          u1.branch_name created_branch, "
				+ "          CASE "
				+ "             WHEN au.enabled_flag = 'Y' AND au.last_update_by IS NULL "
				+ "             THEN "
				+ "                'Dibuat' "
				+ "             WHEN au.enabled_flag = 'Y' AND au.last_update_by IS NOT NULL "
				+ "             THEN "
				+ "                'Diubah' "
				+ "             WHEN au.enabled_flag = 'N' AND au.last_update_by IS NOT NULL "
				+ "             THEN "
				+ "                'Dihapus' "
				+ "          END "
				+ "             last_status, "
				+ "          au.last_update_by, "
				+ "          u2.name last_update_name, "
				+ "          au.last_update_date, "
				+ "          u2.position_name last_update_position, "
				+ "          u2.branch_name last_update_branch "
				+ "     FROM wo_mst_document_type au "
				+ "          LEFT JOIN wo_mst_user u1 ON au.created_by = u1.nik "
				+ "          LEFT JOIN wo_mst_user u2 ON au.last_update_by = u2.nik "
				+ "     WHERE TRUNC(au.creation_date) >= TO_DATE(:startDate, 'yyyy-MM-dd') "
				+ "     AND TRUNC(au.creation_date) <= TO_DATE(:endDate, 'yyyy-MM-dd') "
				+ "   UNION ALL ");
		sb.append("   SELECT 'Kategori Dokumen' title, "
				+ "          au.document_category_in data, "
				+ "          au.created_by, "
				+ "          u1.name created_name, "
				+ "          au.creation_date created_date, "
				+ "          u1.position_name created_position, "
				+ "          u1.branch_name created_branch, "
				+ "          CASE "
				+ "             WHEN au.enabled_flag = 'Y' AND au.last_update_by IS NULL "
				+ "             THEN "
				+ "                'Dibuat' "
				+ "             WHEN au.enabled_flag = 'Y' AND au.last_update_by IS NOT NULL "
				+ "             THEN "
				+ "                'Diubah' "
				+ "             WHEN au.enabled_flag = 'N' AND au.last_update_by IS NOT NULL "
				+ "             THEN "
				+ "                'Dihapus' "
				+ "          END "
				+ "             last_status, "
				+ "          au.last_update_by, "
				+ "          u2.name last_update_name, "
				+ "          au.last_update_date, "
				+ "          u2.position_name last_update_position, "
				+ "          u2.branch_name last_update_branch "
				+ "     FROM wo_mst_document_category au "
				+ "          LEFT JOIN wo_mst_user u1 ON au.created_by = u1.nik "
				+ "          LEFT JOIN wo_mst_user u2 ON au.last_update_by = u2.nik "
				+ "     WHERE TRUNC(au.creation_date) >= TO_DATE(:startDate, 'yyyy-MM-dd') "
				+ "     AND TRUNC(au.creation_date) <= TO_DATE(:endDate, 'yyyy-MM-dd') "
				+ "   UNION ALL ");
		sb.append("   SELECT 'Topik Dokumen' title, "
				+ "          au.document_topic_in data, "
				+ "          au.created_by, "
				+ "          u1.name created_name, "
				+ "          au.creation_date created_date, "
				+ "          u1.position_name created_position, "
				+ "          u1.branch_name created_branch, "
				+ "          CASE "
				+ "             WHEN au.enabled_flag = 'Y' AND au.last_update_by IS NULL "
				+ "             THEN "
				+ "                'Dibuat' "
				+ "             WHEN au.enabled_flag = 'Y' AND au.last_update_by IS NOT NULL "
				+ "             THEN "
				+ "                'Diubah' "
				+ "             WHEN au.enabled_flag = 'N' AND au.last_update_by IS NOT NULL "
				+ "             THEN "
				+ "                'Dihapus' "
				+ "          END "
				+ "             last_status, "
				+ "          au.last_update_by, "
				+ "          u2.name last_update_name, "
				+ "          au.last_update_date, "
				+ "          u2.position_name last_update_position, "
				+ "          u2.branch_name last_update_branch "
				+ "     FROM wo_mst_document_topic au "
				+ "          LEFT JOIN wo_mst_user u1 ON au.created_by = u1.nik "
				+ "          LEFT JOIN wo_mst_user u2 ON au.last_update_by = u2.nik "
				+ "     WHERE TRUNC(au.creation_date) >= TO_DATE(:startDate, 'yyyy-MM-dd') "
				+ "     AND TRUNC(au.creation_date) <= TO_DATE(:endDate, 'yyyy-MM-dd') "
				+ "   UNION ALL ");
		sb.append("   SELECT 'Template Email' title, "
				+ "          tc.NAME_IN data, "
				+ "          au.created_by, "
				+ "          u1.name created_name, "
				+ "          au.creation_date created_date, "
				+ "          u1.position_name created_position, "
				+ "          u1.branch_name created_branch, "
				+ "          CASE "
				+ "             WHEN au.enabled_flag = 'Y' AND au.last_update_by IS NULL "
				+ "             THEN "
				+ "                'Dibuat' "
				+ "             WHEN au.enabled_flag = 'Y' AND au.last_update_by IS NOT NULL "
				+ "             THEN "
				+ "                'Diubah' "
				+ "             WHEN au.enabled_flag = 'N' AND au.last_update_by IS NOT NULL "
				+ "             THEN "
				+ "                'Dihapus' "
				+ "          END "
				+ "             last_status, "
				+ "          au.last_update_by, "
				+ "          u2.name last_update_name, "
				+ "          au.last_update_date, "
				+ "          u2.position_name last_update_position, "
				+ "          u2.branch_name last_update_branch "
				+ "     FROM wo_mst_email_template au "
				+ "          LEFT JOIN wo_mst_user u1 ON au.created_by = u1.nik "
				+ "          LEFT JOIN wo_mst_user u2 ON au.last_update_by = u2.nik "
				+ "          LEFT JOIN wo_mst_parameter_dtl tc "
				+ "             ON     tc.parameter_code = 'EMAIL_TEMPLATE' "
				+ "                AND tc.parameter_dtl_code = au.email_template_code "
				+ "     WHERE TRUNC(au.creation_date) >= TO_DATE(:startDate, 'yyyy-MM-dd') "
				+ "     AND TRUNC(au.creation_date) <= TO_DATE(:endDate, 'yyyy-MM-dd') "
				+ "   UNION ALL ");
		sb.append("   SELECT 'FAQ' title, "
				+ "          au.question_in data, "
				+ "          au.created_by, "
				+ "          u1.name created_name, "
				+ "          au.creation_date created_date, "
				+ "          u1.position_name created_position, "
				+ "          u1.branch_name created_branch, "
				+ "          CASE "
				+ "             WHEN au.enabled_flag = 'Y' AND au.last_update_by IS NULL "
				+ "             THEN "
				+ "                'Dibuat' "
				+ "             WHEN au.enabled_flag = 'Y' AND au.last_update_by IS NOT NULL "
				+ "             THEN "
				+ "                'Diubah' "
				+ "             WHEN au.enabled_flag = 'N' AND au.last_update_by IS NOT NULL "
				+ "             THEN "
				+ "                'Dihapus' "
				+ "          END "
				+ "             last_status, "
				+ "          au.last_update_by, "
				+ "          u2.name last_update_name, "
				+ "          au.last_update_date, "
				+ "          u2.position_name last_update_position, "
				+ "          u2.branch_name last_update_branch "
				+ "     FROM wo_mst_faq au "
				+ "          LEFT JOIN wo_mst_user u1 ON au.created_by = u1.nik "
				+ "          LEFT JOIN wo_mst_user u2 ON au.last_update_by = u2.nik "
				+ "     WHERE TRUNC(au.creation_date) >= TO_DATE(:startDate, 'yyyy-MM-dd') "
				+ "     AND TRUNC(au.creation_date) <= TO_DATE(:endDate, 'yyyy-MM-dd') "
				+ "   UNION ALL ");
		sb.append("   SELECT 'Litigasi' title, "
				+ "   		  CASE "
				+ "   		  	WHEN aud1.CASE_NUMBER IS NOT NULL "
				+ "   		  	THEN  "
				+ "   		  		aud1.CASE_NUMBER "
				+ "   		  	WHEN aud2.CASE_NUMBER IS NOT NULL "
				+ "   		  	THEN  "
				+ "   		  		aud2.CASE_NUMBER "
				+ "   		  	WHEN aud3.CASE_NUMBER IS NOT NULL "
				+ "   		  	THEN  "
				+ "   		  		aud3.CASE_NUMBER "
				+ "   		  	WHEN aud4.CASE_NUMBER IS NOT NULL "
				+ "   		  	THEN  "
				+ "   		  		aud4.CASE_NUMBER "
				+ "   		  	WHEN aud5.CASE_NUMBER IS NOT NULL "
				+ "   		  	THEN  "
				+ "   		  		aud5.CASE_NUMBER "
				+ "   		  END AS data, "
				+ "          au.created_by, "
				+ "          u1.name created_name, "
				+ "          au.creation_date created_date, "
				+ "          u1.position_name created_position, "
				+ "          u1.branch_name created_branch, "
				+ "          CASE "
				+ "             WHEN au.enabled_flag = 'Y' AND au.last_update_by IS NULL "
				+ "             THEN "
				+ "                'Dibuat' "
				+ "             WHEN au.enabled_flag = 'Y' AND au.last_update_by IS NOT NULL "
				+ "             THEN "
				+ "                'Diubah' "
				+ "             WHEN au.enabled_flag = 'N' AND au.last_update_by IS NOT NULL "
				+ "             THEN "
				+ "                'Dihapus' "
				+ "          END "
				+ "             last_status, "
				+ "          au.last_update_by, "
				+ "          u2.name last_update_name, "
				+ "          au.last_update_date, "
				+ "          u2.position_name last_update_position, "
				+ "          u2.branch_name last_update_branch "
				+ "     FROM wo_mst_litigation au "
				+ "          LEFT JOIN wo_mst_user u1 ON au.created_by = u1.nik "
				+ "          LEFT JOIN wo_mst_user u2 ON au.last_update_by = u2.nik "
				+ "          INNER JOIN WO_MST_LITI_PERDATA_PENGGUGAT aud1 ON aud1.LITIGATION_ID = au.LITIGATION_ID "
				+ "          INNER JOIN WO_MST_LITI_PERDATA_TERGUGAT aud2 ON aud2.LITIGATION_ID = au.LITIGATION_ID "
				+ "          INNER JOIN WO_MST_LITI_PAILIT_PKPU aud3 ON aud3.LITIGATION_ID = au.LITIGATION_ID "
				+ "          INNER JOIN WO_MST_LITI_PIDANA_PELAPOR aud4 ON aud4.LITIGATION_ID = au.LITIGATION_ID "
				+ "          INNER JOIN WO_MST_LITI_PIDANA_TERLAPOR aud5 ON aud5.LITIGATION_ID = au.LITIGATION_ID "
				+ "     WHERE TRUNC(au.creation_date) >= TO_DATE(:startDate, 'yyyy-MM-dd') "
				+ "     AND TRUNC(au.creation_date) <= TO_DATE(:endDate, 'yyyy-MM-dd') "
				+ "   UNION ALL ");
		sb.append("   SELECT 'Notaris' title, "
				+ "          au.notary_name data, "
				+ "          au.created_by, "
				+ "          u1.name created_name, "
				+ "          au.creation_date created_date, "
				+ "          u1.position_name created_position, "
				+ "          u1.branch_name created_branch, "
				+ "          CASE "
				+ "             WHEN au.enabled_flag = 'Y' AND au.last_update_by IS NULL "
				+ "             THEN "
				+ "                'Dibuat' "
				+ "             WHEN au.enabled_flag = 'Y' AND au.last_update_by IS NOT NULL "
				+ "             THEN "
				+ "                'Diubah' "
				+ "             WHEN au.enabled_flag = 'N' AND au.last_update_by IS NOT NULL "
				+ "             THEN "
				+ "                'Dihapus' "
				+ "          END "
				+ "             last_status, "
				+ "          au.last_update_by, "
				+ "          u2.name last_update_name, "
				+ "          au.last_update_date, "
				+ "          u2.position_name last_update_position, "
				+ "          u2.branch_name last_update_branch "
				+ "     FROM wo_mst_notary au "
				+ "          LEFT JOIN wo_mst_user u1 ON au.created_by = u1.nik "
				+ "          LEFT JOIN wo_mst_user u2 ON au.last_update_by = u2.nik "
				+ "     WHERE TRUNC(au.creation_date) >= TO_DATE(:startDate, 'yyyy-MM-dd') "
				+ "     AND TRUNC(au.creation_date) <= TO_DATE(:endDate, 'yyyy-MM-dd') "
				+ "   UNION ALL ");
		sb.append("   SELECT 'Surat Keluar' title, "
				+ "          au.letter_no data, "
				+ "          au.created_by, "
				+ "          u1.name created_name, "
				+ "          au.creation_date created_date, "
				+ "          u1.position_name created_position, "
				+ "          u1.branch_name created_branch, "
				+ "          CASE "
				+ "             WHEN au.enabled_flag = 'Y' AND au.last_update_by IS NULL "
				+ "             THEN "
				+ "                'Dibuat' "
				+ "             WHEN au.enabled_flag = 'Y' AND au.last_update_by IS NOT NULL "
				+ "             THEN "
				+ "                'Diubah' "
				+ "             WHEN au.enabled_flag = 'N' AND au.last_update_by IS NOT NULL "
				+ "             THEN "
				+ "                'Dihapus' "
				+ "          END "
				+ "             last_status, "
				+ "          au.last_update_by, "
				+ "          u2.name last_update_name, "
				+ "          au.last_update_date, "
				+ "          u2.position_name last_update_position, "
				+ "          u2.branch_name last_update_branch "
				+ "     FROM wo_mst_outgoing_letter au "
				+ "          LEFT JOIN wo_mst_user u1 ON au.created_by = u1.nik "
				+ "          LEFT JOIN wo_mst_user u2 ON au.last_update_by = u2.nik "
				+ "     WHERE TRUNC(au.creation_date) >= TO_DATE(:startDate, 'yyyy-MM-dd') "
				+ "     AND TRUNC(au.creation_date) <= TO_DATE(:endDate, 'yyyy-MM-dd') "
				+ "   UNION ALL ");
		sb.append("   SELECT 'Parameter' title, "
				+ "          au.NAME_IN data, "
				+ "          au.created_by, "
				+ "          u1.name created_name, "
				+ "          au.creation_date created_date, "
				+ "          u1.position_name created_position, "
				+ "          u1.branch_name created_branch, "
				+ "          CASE "
				+ "             WHEN au.enabled_flag = 'Y' AND au.last_update_by IS NULL "
				+ "             THEN "
				+ "                'Dibuat' "
				+ "             WHEN au.enabled_flag = 'Y' AND au.last_update_by IS NOT NULL "
				+ "             THEN "
				+ "                'Diubah' "
				+ "             WHEN au.enabled_flag = 'N' AND au.last_update_by IS NOT NULL "
				+ "             THEN "
				+ "                'Dihapus' "
				+ "          END "
				+ "             last_status, "
				+ "          au.last_update_by, "
				+ "          u2.name last_update_name, "
				+ "          au.last_update_date, "
				+ "          u2.position_name last_update_position, "
				+ "          u2.branch_name last_update_branch "
				+ "     FROM wo_mst_parameter_dtl au "
				+ "          INNER JOIN wo_mst_user u1 ON au.created_by = u1.nik "
				+ "          LEFT JOIN wo_mst_user u2 ON au.last_update_by = u2.nik "
				+ "     WHERE TRUNC(au.creation_date) >= TO_DATE(:startDate, 'yyyy-MM-dd') "
				+ "     AND TRUNC(au.creation_date) <= TO_DATE(:endDate, 'yyyy-MM-dd') "
				+ "   UNION ALL ");
		sb.append("   SELECT 'Master Propinsi' title, "
				+ "          au.province || ',' || au.branch_code data, "
				+ "          au.created_by, "
				+ "          u1.name created_name, "
				+ "          au.creation_date created_date, "
				+ "          u1.position_name created_position, "
				+ "          u1.branch_name created_branch, "
				+ "          CASE "
				+ "             WHEN au.enabled_flag = 'Y' AND au.last_update_by IS NULL "
				+ "             THEN "
				+ "                'Dibuat' "
				+ "             WHEN au.enabled_flag = 'Y' AND au.last_update_by IS NOT NULL "
				+ "             THEN "
				+ "                'Diubah' "
				+ "             WHEN au.enabled_flag = 'N' AND au.last_update_by IS NOT NULL "
				+ "             THEN "
				+ "                'Dihapus' "
				+ "          END "
				+ "             last_status, "
				+ "          au.last_update_by, "
				+ "          u2.name last_update_name, "
				+ "          au.last_update_date, "
				+ "          u2.position_name last_update_position, "
				+ "          u2.branch_name last_update_branch "
				+ "     FROM wo_mst_province au "
				+ "          INNER JOIN wo_mst_user u1 ON au.created_by = u1.nik "
				+ "          LEFT JOIN wo_mst_user u2 ON au.last_update_by = u2.nik "
				+ "     WHERE TRUNC(au.creation_date) >= TO_DATE(:startDate, 'yyyy-MM-dd') "
				+ "     AND TRUNC(au.creation_date) <= TO_DATE(:endDate, 'yyyy-MM-dd') "
				+ "   UNION ALL ");
		sb.append("   SELECT 'Pin Propinsi' title, "
				+ "          au.province data, "
				+ "          au.created_by, "
				+ "          u1.name created_name, "
				+ "          au.creation_date created_date, "
				+ "          u1.position_name created_position, "
				+ "          u1.branch_name created_branch, "
				+ "          CASE "
				+ "             WHEN au.enabled_flag = 'Y' AND au.last_update_by IS NULL "
				+ "             THEN "
				+ "                'Dibuat' "
				+ "             WHEN au.enabled_flag = 'Y' AND au.last_update_by IS NOT NULL "
				+ "             THEN "
				+ "                'Diubah' "
				+ "             WHEN au.enabled_flag = 'N' AND au.last_update_by IS NOT NULL "
				+ "             THEN "
				+ "                'Dihapus' "
				+ "          END "
				+ "             last_status, "
				+ "          au.last_update_by, "
				+ "          u2.name last_update_name, "
				+ "          au.last_update_date, "
				+ "          u2.position_name last_update_position, "
				+ "          u2.branch_name last_update_branch "
				+ "     FROM wo_mst_province_location au "
				+ "          LEFT JOIN wo_mst_user u1 ON au.created_by = u1.nik "
				+ "          LEFT JOIN wo_mst_user u2 ON au.last_update_by = u2.nik "
				+ "     WHERE TRUNC(au.creation_date) >= TO_DATE(:startDate, 'yyyy-MM-dd') "
				+ "     AND TRUNC(au.creation_date) <= TO_DATE(:endDate, 'yyyy-MM-dd') "
				+ "   UNION ALL ");
		sb.append("   SELECT 'Konsultasi' title, "
				+ "          au.ticket_no data, "
				+ "          au.created_by, "
				+ "          u1.name created_name, "
				+ "          au.creation_date created_date, "
				+ "          u1.position_name created_position, "
				+ "          u1.branch_name created_branch, "
				+ "          CASE "
				+ "             WHEN au.enabled_flag = 'Y' AND au.last_update_by IS NULL "
				+ "             THEN "
				+ "                'Dibuat' "
				+ "             WHEN au.enabled_flag = 'Y' AND au.last_update_by IS NOT NULL "
				+ "             THEN "
				+ "                'Diubah' "
				+ "             WHEN au.enabled_flag = 'N' AND au.last_update_by IS NOT NULL "
				+ "             THEN "
				+ "                'Dihapus' "
				+ "          END "
				+ "             last_status, "
				+ "          au.last_update_by, "
				+ "          u2.name last_update_name, "
				+ "          au.last_update_date, "
				+ "          u2.position_name last_update_position, "
				+ "          u2.branch_name last_update_branch "
				+ "     FROM wo_mst_qna au "
				+ "          LEFT JOIN wo_mst_user u1 ON au.created_by = u1.nik "
				+ "          LEFT JOIN wo_mst_user u2 ON au.last_update_by = u2.nik "
				+ "     WHERE TRUNC(au.creation_date) >= TO_DATE(:startDate, 'yyyy-MM-dd') "
				+ "     AND TRUNC(au.creation_date) <= TO_DATE(:endDate, 'yyyy-MM-dd') "
				+ "   UNION ALL ");
		sb.append("   SELECT 'Mapping Konsultasi Admin' title, "
				+ "          tc.NAME_IN data, "
				+ "          au.created_by, "
				+ "          u1.name created_name, "
				+ "          au.creation_date created_date, "
				+ "          u1.position_name created_position, "
				+ "          u1.branch_name created_branch, "
				+ "          CASE "
				+ "             WHEN au.enabled_flag = 'Y' AND au.last_update_by IS NULL "
				+ "             THEN "
				+ "                'Dibuat' "
				+ "             WHEN au.enabled_flag = 'Y' AND au.last_update_by IS NOT NULL "
				+ "             THEN "
				+ "                'Diubah' "
				+ "             WHEN au.enabled_flag = 'N' AND au.last_update_by IS NOT NULL "
				+ "             THEN "
				+ "                'Dihapus' "
				+ "          END "
				+ "             last_status, "
				+ "          au.last_update_by, "
				+ "          u2.name last_update_name, "
				+ "          au.last_update_date, "
				+ "          u2.position_name last_update_position, "
				+ "          u2.branch_name last_update_branch "
				+ "     FROM wo_mst_qna_category_map au "
				+ "          LEFT JOIN wo_mst_user u1 ON au.created_by = u1.nik "
				+ "          LEFT JOIN wo_mst_user u2 ON au.last_update_by = u2.nik "
				+ "          LEFT JOIN wo_mst_parameter_dtl tc "
				+ "             ON     tc.parameter_code = 'QNA_CATEGORY' "
				+ "                AND tc.parameter_dtl_code = au.qna_category_code "
				+ "     WHERE TRUNC(au.creation_date) >= TO_DATE(:startDate, 'yyyy-MM-dd') "
				+ "     AND TRUNC(au.creation_date) <= TO_DATE(:endDate, 'yyyy-MM-dd') "
				+ "   UNION ALL ");
		sb.append("   SELECT 'Master RC' title, "
				+ "          au.region_code data, "
				+ "          au.created_by, "
				+ "          u1.name created_name, "
				+ "          au.creation_date created_date, "
				+ "          u1.position_name created_position, "
				+ "          u1.branch_name created_branch, "
				+ "          CASE "
				+ "             WHEN au.enabled_flag = 'Y' AND au.last_update_by IS NULL "
				+ "             THEN "
				+ "                'Dibuat' "
				+ "             WHEN au.enabled_flag = 'Y' AND au.last_update_by IS NOT NULL "
				+ "             THEN "
				+ "                'Diubah' "
				+ "             WHEN au.enabled_flag = 'N' AND au.last_update_by IS NOT NULL "
				+ "             THEN "
				+ "                'Dihapus' "
				+ "          END "
				+ "             last_status, "
				+ "          au.last_update_by, "
				+ "          u2.name last_update_name, "
				+ "          au.last_update_date, "
				+ "          u2.position_name last_update_position, "
				+ "          u2.branch_name last_update_branch "
				+ "     FROM wo_mst_rc au "
				+ "          LEFT JOIN wo_mst_user u1 ON au.created_by = u1.nik "
				+ "          LEFT JOIN wo_mst_user u2 ON au.last_update_by = u2.nik "
				+ "     WHERE TRUNC(au.creation_date) >= TO_DATE(:startDate, 'yyyy-MM-dd') "
				+ "     AND TRUNC(au.creation_date) <= TO_DATE(:endDate, 'yyyy-MM-dd') "
				+ "   UNION ALL ");
		sb.append("   SELECT 'Tipe Laporan' title, "
				+ "          au.report_type_in data, "
				+ "          au.created_by, "
				+ "          u1.name created_name, "
				+ "          au.creation_date created_date, "
				+ "          u1.position_name created_position, "
				+ "          u1.branch_name created_branch, "
				+ "          CASE "
				+ "             WHEN au.enabled_flag = 'Y' AND au.last_update_by IS NULL "
				+ "             THEN "
				+ "                'Dibuat' "
				+ "             WHEN au.enabled_flag = 'Y' AND au.last_update_by IS NOT NULL "
				+ "             THEN "
				+ "                'Diubah' "
				+ "             WHEN au.enabled_flag = 'N' AND au.last_update_by IS NOT NULL "
				+ "             THEN "
				+ "                'Dihapus' "
				+ "          END "
				+ "             last_status, "
				+ "          au.last_update_by, "
				+ "          u2.name last_update_name, "
				+ "          au.last_update_date, "
				+ "          u2.position_name last_update_position, "
				+ "          u2.branch_name last_update_branch "
				+ "     FROM wo_mst_report_type au "
				+ "          LEFT JOIN wo_mst_user u1 ON au.created_by = u1.nik "
				+ "          LEFT JOIN wo_mst_user u2 ON au.last_update_by = u2.nik "
				+ "     WHERE TRUNC(au.creation_date) >= TO_DATE(:startDate, 'yyyy-MM-dd') "
				+ "     AND TRUNC(au.creation_date) <= TO_DATE(:endDate, 'yyyy-MM-dd') "
				+ "   UNION ALL ");
		sb.append("   SELECT 'Tanggung Jawab' title, "
				+ "          au.name data, "
				+ "          au.created_by, "
				+ "          u1.name created_name, "
				+ "          au.creation_date created_date, "
				+ "          u1.position_name created_position, "
				+ "          u1.branch_name created_branch, "
				+ "          CASE "
				+ "             WHEN au.enabled_flag = 'Y' AND au.last_update_by IS NULL "
				+ "             THEN "
				+ "                'Dibuat' "
				+ "             WHEN au.enabled_flag = 'Y' AND au.last_update_by IS NOT NULL "
				+ "             THEN "
				+ "                'Diubah' "
				+ "             WHEN au.enabled_flag = 'N' AND au.last_update_by IS NOT NULL "
				+ "             THEN "
				+ "                'Dihapus' "
				+ "          END "
				+ "             last_status, "
				+ "          au.last_update_by, "
				+ "          u2.name last_update_name, "
				+ "          au.last_update_date, "
				+ "          u2.position_name last_update_position, "
				+ "          u2.branch_name last_update_branch "
				+ "     FROM wo_mst_responsibility au "
				+ "          LEFT JOIN wo_mst_user u1 ON au.created_by = u1.nik "
				+ "          LEFT JOIN wo_mst_user u2 ON au.last_update_by = u2.nik "
				+ "     WHERE TRUNC(au.creation_date) >= TO_DATE(:startDate, 'yyyy-MM-dd') "
				+ "     AND TRUNC(au.creation_date) <= TO_DATE(:endDate, 'yyyy-MM-dd') "
				+ "   UNION ALL ");
		sb.append("   SELECT 'Static Page' title, "
				+ "          au.title_in data, "
				+ "          au.created_by, "
				+ "          u1.name created_name, "
				+ "          au.creation_date created_date, "
				+ "          u1.position_name created_position, "
				+ "          u1.branch_name created_branch, "
				+ "          CASE "
				+ "             WHEN au.enabled_flag = 'Y' AND au.last_update_by IS NULL "
				+ "             THEN "
				+ "                'Dibuat' "
				+ "             WHEN au.enabled_flag = 'Y' AND au.last_update_by IS NOT NULL "
				+ "             THEN "
				+ "                'Diubah' "
				+ "             WHEN au.enabled_flag = 'N' AND au.last_update_by IS NOT NULL "
				+ "             THEN "
				+ "                'Dihapus' "
				+ "          END "
				+ "             last_status, "
				+ "          au.last_update_by, "
				+ "          u2.name last_update_name, "
				+ "          au.last_update_date, "
				+ "          u2.position_name last_update_position, "
				+ "          u2.branch_name last_update_branch "
				+ "     FROM wo_mst_static_page au "
				+ "          LEFT JOIN wo_mst_user u1 ON au.created_by = u1.nik "
				+ "          LEFT JOIN wo_mst_user u2 ON au.last_update_by = u2.nik "
				+ "     WHERE TRUNC(au.creation_date) >= TO_DATE(:startDate, 'yyyy-MM-dd') "
				+ "     AND TRUNC(au.creation_date) <= TO_DATE(:endDate, 'yyyy-MM-dd') "
				+ "   UNION ALL ");
		sb.append("   SELECT 'Template' title, "
				+ "          tc.NAME_IN data, "
				+ "          au.created_by, "
				+ "          u1.name created_name, "
				+ "          au.creation_date created_date, "
				+ "          u1.position_name created_position, "
				+ "          u1.branch_name created_branch, "
				+ "          CASE "
				+ "             WHEN au.enabled_flag = 'Y' AND au.last_update_by IS NULL "
				+ "             THEN "
				+ "                'Dibuat' "
				+ "             WHEN au.enabled_flag = 'Y' AND au.last_update_by IS NOT NULL "
				+ "             THEN "
				+ "                'Diubah' "
				+ "             WHEN au.enabled_flag = 'N' AND au.last_update_by IS NOT NULL "
				+ "             THEN "
				+ "                'Dihapus' "
				+ "          END "
				+ "             last_status, "
				+ "          au.last_update_by, "
				+ "          u2.name last_update_name, "
				+ "          au.last_update_date, "
				+ "          u2.position_name last_update_position, "
				+ "          u2.branch_name last_update_branch "
				+ "     FROM wo_mst_template au "
				+ "          LEFT JOIN wo_mst_user u1 ON au.created_by = u1.nik "
				+ "          LEFT JOIN wo_mst_user u2 ON au.last_update_by = u2.nik "
				+ "          LEFT JOIN wo_mst_parameter_dtl tc "
				+ "             ON     tc.parameter_code = 'TEMPLATE_SUBCATEGORY' "
				+ "                AND tc.parameter_dtl_code = au.template_subcategory "
				+ "     WHERE TRUNC(au.creation_date) >= TO_DATE(:startDate, 'yyyy-MM-dd') "
				+ "     AND TRUNC(au.creation_date) <= TO_DATE(:endDate, 'yyyy-MM-dd') "
				+ "   UNION ALL ");
		sb.append("   SELECT 'Pengguna' title, "
				+ "          au.nik data, "
				+ "          au.created_by, "
				+ "          u1.name created_name, "
				+ "          au.creation_date created_date, "
				+ "          u1.position_name created_position, "
				+ "          u1.branch_name created_branch, "
				+ "          CASE "
				+ "             WHEN au.enabled_flag = 'Y' AND au.last_update_by IS NULL "
				+ "             THEN "
				+ "                'Dibuat' "
				+ "             WHEN au.enabled_flag = 'Y' AND au.last_update_by IS NOT NULL "
				+ "             THEN "
				+ "                'Diubah' "
				+ "             WHEN au.enabled_flag = 'N' AND au.last_update_by IS NOT NULL "
				+ "             THEN "
				+ "                'Dihapus' "
				+ "          END "
				+ "             last_status, "
				+ "          au.last_update_by, "
				+ "          CASE WHEN au.last_update_by IS NOT NULL THEN u1.name ELSE NULL END "
				+ "             last_update_name, "
				+ "          au.last_update_date, "
				+ "          CASE "
				+ "             WHEN au.last_update_by IS NOT NULL THEN u1.position_name "
				+ "             ELSE NULL "
				+ "          END "
				+ "             last_update_position, "
				+ "          CASE "
				+ "             WHEN au.last_update_by IS NOT NULL THEN u1.branch_name "
				+ "             ELSE NULL "
				+ "          END "
				+ "             last_update_branch "
				+ "     FROM wo_mst_user au "
				+ "          INNER JOIN wo_mst_user u1 "
				+ "             ON (au.created_by = u1.nik OR au.last_update_by = u1.nik) "
				+ "     WHERE TRUNC(au.creation_date) >= TO_DATE(:startDate, 'yyyy-MM-dd') "
				+ "     AND TRUNC(au.creation_date) <= TO_DATE(:endDate, 'yyyy-MM-dd') "
				+ "   UNION ALL ");
		sb.append("   SELECT 'Artikel' title, "
				+ "          au.article_title_in data, "
				+ "          au.created_by, "
				+ "          u1.name created_name, "
				+ "          au.creation_date created_date, "
				+ "          u1.position_name created_position, "
				+ "          u1.branch_name created_branch, "
				+ "          CASE "
				+ "             WHEN au.enabled_flag = 'Y' AND au.last_update_by IS NULL "
				+ "             THEN "
				+ "                'Dibuat' "
				+ "             WHEN au.enabled_flag = 'Y' AND au.last_update_by IS NOT NULL "
				+ "             THEN "
				+ "                'Diubah' "
				+ "             WHEN au.enabled_flag = 'N' AND au.last_update_by IS NOT NULL "
				+ "             THEN "
				+ "                'Dihapus' "
				+ "          END "
				+ "             last_status, "
				+ "          au.last_update_by, "
				+ "          u2.name last_update_name, "
				+ "          au.last_update_date, "
				+ "          u2.position_name last_update_position, "
				+ "          u2.branch_name last_update_branch "
				+ "     FROM wo_tmp_article au "
				+ "          LEFT JOIN wo_mst_user u1 ON au.created_by = u1.nik "
				+ "          LEFT JOIN wo_mst_user u2 ON au.last_update_by = u2.nik "
				+ "     WHERE TRUNC(au.creation_date) >= TO_DATE(:startDate, 'yyyy-MM-dd') "
				+ "     AND TRUNC(au.creation_date) <= TO_DATE(:endDate, 'yyyy-MM-dd') "
				+ "   UNION ALL ");
		sb.append("   SELECT 'Persetujuan Artikel' title, "
				+ "          d.article_title_in data, "
				+ "          u1.nik, "
				+ "          u1.name created_name, "
				+ "          au.creation_date created_date, "
				+ "          u1.position_name created_position, "
				+ "          u1.branch_name created_branch, "
				+ "          u2.NAME_IN last_status, "
				+ "          NULL last_update_by, "
				+ "          NULL last_update_name, "
				+ "          NULL last_update_date, "
				+ "          NULL last_update_position, "
				+ "          NULL last_update_branch "
				+ "     FROM wo_tmp_article_approval au "
				+ "          INNER JOIN wo_tmp_article d ON au.article_id = d.article_id "
				+ "          LEFT JOIN wo_mst_user u1 ON au.user_id = u1.user_id "
				+ "          LEFT JOIN wo_mst_parameter_dtl u2 "
				+ "             ON     u2.parameter_code = 'AP' "
				+ "    WHERE TRUNC(au.creation_date) >= TO_DATE(:startDate, 'yyyy-MM-dd') "
				+ "          AND TRUNC(au.creation_date) <= TO_DATE(:endDate, 'yyyy-MM-dd') ");
		sb.append(" ) ");
		
		sb.append(" order by created_date desc ");
		
//		this.setQueryWhereString(sb, searchCriteria);
		Query query = getSession().createSQLQuery(sb.toString());
		this.setQuerySetString(query, searchCriteria);
		
		List result = query.getResultList();
		List<ReportLogUserVo> vo = new ArrayList<ReportLogUserVo>();
		SimpleDateFormat sdfTemp = new SimpleDateFormat("dd-MMM-yyyy");
		if (result != null) {
			for (int i = 0; i < result.size(); i++) {
				Object[] obj = (Object[]) result.get(i);
				ReportLogUserVo data = new ReportLogUserVo();
				
				data.setTitle(obj[0] != null ? (String) obj[0] : null);
				data.setData(obj[1] != null ? (String) obj[1] : null);
				data.setCreatedBy(obj[2] != null ? (String) obj[2] : null);
				data.setCreatedName(obj[3] != null ? (String) obj[3] : null);
				data.setCreatedDate(obj[4] != null ? (Date) obj[4] : null);
				data.setCreatedPosition(obj[5] != null ? (String) obj[5] : null);
				data.setCreatedBranch(obj[6] != null ? (String) obj[6] : null);
				data.setLastStatus(obj[7] != null ? (String) obj[7] : null);
				data.setLastUpdateBy(obj[8] != null ? (String) obj[8] : null);
				data.setLastUpdateName(obj[9] != null ? (String) obj[9] : null);
				data.setLastUpdateDate(obj[10] != null ? (Date) obj[10] : null);
				data.setLastUpdatePosition(obj[11] != null ? (String) obj[11] : null);
				data.setLastUpdateBranch(obj[12] != null ? (String) obj[12] : null);
				
				if(data.getCreatedDate() !=null) {
					data.setCreatedDateStr(sdfTemp.format(data.getCreatedDate()));
				}
				if(data.getLastUpdateDate() !=null) {
					data.setLastUpdateDateStr(sdfTemp.format(data.getLastUpdateDate()));
				}
				
				
				vo.add(data);
			}
		}
		
		return vo;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<ReportLogUserDiagramVo> getReportLogUserGrafikAsVo(List<? extends SearchObject> searchCriteria) {
		StringBuilder sb = new StringBuilder();
		
		sb.append(" SELECT CASE ");
		sb.append(" 	WHEN ACCESS_ACTION = '/compliance/pages/faqFE/faqFE.faces' ");
		sb.append(" 		THEN 'FAQ' ");
		sb.append(" 	WHEN ACCESS_ACTION = '/compliance/pages/qaFE/qaFE.faces' ");
		sb.append(" 		THEN 'Tanya Kami' ");
		sb.append(" 	WHEN ACCESS_ACTION = '/compliance/pages/internalRegulationFE/internalRegulationFE.faces' ");
		sb.append(" 		THEN 'Peraturan Internal' ");
		sb.append(" 	WHEN ACCESS_ACTION = '/compliance/pages/externalRegulationFE/externalRegulationFE.faces' ");
		sb.append(" 		THEN 'Peraturan Eksternal' ");
		sb.append(" 	WHEN ACCESS_ACTION = '/compliance/pages/opinionFE/opinionFE.faces' ");
		sb.append(" 		THEN 'Opini' ");
		sb.append(" 	WHEN ACCESS_ACTION = '/compliance/pages/articleFE/articleFE.faces' ");
		sb.append(" 		THEN 'Artikel' ");
		sb.append(" 	WHEN ACCESS_ACTION = '/compliance/pages/socializationFE/socializationFE.faces' ");
		sb.append(" 		THEN 'Sosialisasi' ");
		sb.append(" 	WHEN ACCESS_ACTION = '/compliance/pages/correspondenceFE/correspondenceFE.faces' ");
		sb.append(" 		THEN 'Surat Masuk' ");
		sb.append(" 	WHEN ACCESS_ACTION = '/compliance/pages/fineFE/fineFE.faces' ");
		sb.append(" 		THEN 'Denda' ");
		sb.append(" 	WHEN ACCESS_ACTION = '/compliance/pages/regulatoryReportingFE/regulatoryReportingFE.faces' ");
		sb.append(" 		THEN 'Regulatory Reporting' ");
		sb.append(" 	WHEN ACCESS_ACTION = '/compliance/pages/auditFE/auditFE.faces' ");
		sb.append(" 		THEN 'Audit' ");
		sb.append(" 	WHEN ACCESS_ACTION = '/compliance/pages/regulationMonitoringFE/regulationMonitoringFE.faces' ");
		sb.append(" 		THEN 'Regulation Reporting' ");
		sb.append(" 	WHEN ACCESS_ACTION = '/compliance/pages/complianceTestingFE/complianceTestingFE.faces' ");
		sb.append(" 		THEN 'Compliance Testing' ");
		sb.append(" 	WHEN ACCESS_ACTION = '/compliance/pages/discussionFE/discussionFE.faces' ");
		sb.append(" 		THEN 'Group Discussion' ");
		sb.append(" 	WHEN ACCESS_ACTION = '/compliance/pages/advocateFE/advocateFE.faces' ");
		sb.append(" 		THEN 'Daftar Kantor Hukum' ");
		sb.append(" 	WHEN ACCESS_ACTION = '/compliance/pages/notaryFE/notaryFE.faces' ");
		sb.append(" 		THEN 'Daftar Notaris' ");
		sb.append(" 	WHEN ACCESS_ACTION = '/compliance/pages/litigationViewFE/litigationViewFE.faces' ");
		sb.append(" 		THEN 'Litigasi' ");
		sb.append(" 	WHEN ACCESS_ACTION = '/compliance/pages/templateViewFE/templateViewFE.faces' ");
		sb.append(" 		THEN 'Template' ");
		sb.append(" 	WHEN ACCESS_ACTION = '/compliance/pages/cpsaFE/compliancePlanSelfAssessmentFE.faces' ");
		sb.append(" 		THEN 'Compliance Plan Self Assessment' ");
		sb.append(" 	WHEN ACCESS_ACTION = '/compliance/pages/aboutUsFE/aboutUsFE.faces' ");
		sb.append(" 		THEN 'Tentang LCCA' ");
		sb.append(" 	WHEN ACCESS_ACTION = '/compliance/pages/announcementViewFE/announcementViewFE.faces' ");
		sb.append(" 		THEN 'Notifikasi' ");
		sb.append(" 	WHEN ACCESS_ACTION = '/compliance/pages/searchAllFE/searchAllFE.faces' ");
		sb.append(" 		THEN 'Cari Semua' ");
		sb.append(" 	ELSE ACCESS_ACTION ");
		sb.append(" 	END PAGE, ");
		sb.append(" 	COUNT (1) ");
		sb.append(" FROM WO_LOG_ACCESS ");
		sb.append(" WHERE ACCESS_ACTION LIKE '%FE.faces' ");
//		sb.append(" --AND TRUNC(ACCESS_TIME) BETWEEN START_DATE AND END_DATE ");
		
		this.setQueryWhereStringDiagram(sb, searchCriteria);
		sb.append(" GROUP BY ACCESS_ACTION ");
		
		Query query = getSession().createSQLQuery(sb.toString());
		this.setQuerySetStringDiagram(query, searchCriteria);
		
		List result = query.getResultList();
		List<ReportLogUserDiagramVo> vo = new ArrayList<ReportLogUserDiagramVo>();
		
		if (result != null) {
			for (int i = 0; i < result.size(); i++) {
				Object[] obj = (Object[]) result.get(i);
				ReportLogUserDiagramVo data = new ReportLogUserDiagramVo();
				
				data.setPageName(obj[0] != null ? (String) obj[0] : null);
				data.setPageCount(obj[1] != null ? MathUtil.returnIdObjectToLong(obj[1]) : null);
				
				vo.add(data);
			}
		}
		
		return vo;
	}

}
