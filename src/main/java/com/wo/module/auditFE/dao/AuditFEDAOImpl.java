package com.wo.module.auditFE.dao;

import java.sql.Clob;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.SortOrder;
import org.springframework.stereotype.Repository;

import com.wo.module.auditFE.vo.AuditFEVO;
import com.wo.module.common.constant.CommonConstants;
import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.common.util.MathUtil;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.trcAudit.model.TrcAudit;

@Repository("auditFEDAO")
public class AuditFEDAOImpl extends GenericDAOHibernate<TrcAudit, Long> implements AuditFEDAO {

	@SuppressWarnings("rawtypes")
	private StringBuilder getQueryWhereString(StringBuilder sb, List<? extends SearchObject> searchCriteria) {
		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString();
				if (!StringUtils.isBlank(val)) {
					if (StringUtils.equals(CommonConstants.SEARCH_BY_COMBO_BOX, col)) {
						sb.append(" AND (:searchStatus  <> 'PIC_INPROGRESS' or f.followup_status = :searchStatus or f.followup_status is null) ");
						sb.append(" AND (:searchStatus  = 'PIC_INPROGRESS' or f.followup_status = :searchStatus)");
//						sb.append(" and f.followup_status = :searchStatus ");
					}

					if (StringUtils.equals(CommonConstants.SEARCH_BY_USER_LOGIN, col)) {
						sb.append(
								" and (f.user_id_1 = :userId or f.user_id_2 = :userId or f.user_id_3 = :userId ) ");
					}
					
					if (StringUtils.equals(CommonConstants.SEARCH_BY_TEXT_BOX, col)) {
						sb.append(" and UPPER(ma.audit_template_name_in) like UPPER(:searchNameIn)");
					}	
				}
			}
		}
		return sb;
	}
	
	@SuppressWarnings("rawtypes")
	private void getQuerySetValue(Query query, List<? extends SearchObject> searchCriteria) {
		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString();
//				Object valReal = searchVal.getSearchValue();

				if (!StringUtils.isBlank(val)) {
			
					if (StringUtils.equals(CommonConstants.SEARCH_BY_COMBO_BOX, col)) {
						query.setParameter("searchStatus", val);
					}

					if (StringUtils.equals(CommonConstants.SEARCH_BY_USER_LOGIN, col)) {
						query.setParameter("userId", val);
					}
					
					if (StringUtils.equals(CommonConstants.SEARCH_BY_TEXT_BOX, col)) {
						query.setParameter("searchNameIn", "%" + val + "%");
					}

				}
			}
		}
	}

	@SuppressWarnings("rawtypes")
	public Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception {

		Number results = searchCountDataCriteria(searchCriteria);
		if (results == null) {
			results = 0;
		}

		return results.longValue();
	}

	@SuppressWarnings("rawtypes")
	private Number searchCountDataCriteria(List<? extends SearchObject> searchCriteria) {

		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT\r\n" + 
				"	count(1)\r\n" + 
				"FROM\r\n" + 
				"	wo_trc_audit_pic_followup f\r\n" + 
				"	INNER JOIN WO_TRC_AUDIT_PC_FP_BANK_CMITMT c ON c.AUD_PC_FP_BANK_CMITMT_ID = f.AUD_PC_FP_BANK_CMITMT_ID \r\n" + 
				"	INNER JOIN WO_TRC_AUDIT_CHECK_POINT p ON p.AUDIT_CHECK_POINT_ID = c.AUDIT_CHECK_POINT_ID \r\n" + 
				"	INNER JOIN WO_TRC_AUDIT s ON s.AUDIT_ID = p.AUDIT_ID \r\n" + 
				"WHERE\r\n" + 
				"	s.enabled_flag = 'Y'\r\n" + 
				"	AND s.status = 'DATA_ACTIVE'\r\n" + 
				"	AND s.follow_up = 'Y' ");
		sb = getQueryWhereString(sb, searchCriteria);

		Query result = getSession().createSQLQuery(sb.toString());
		
		this.getQuerySetValue(result, searchCriteria);

		return (Number) result.getSingleResult();
	}

	@SuppressWarnings("rawtypes")
	public List<AuditFEVO> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize,
			String sortField, SortOrder sortOrder) throws Exception {

		List<AuditFEVO> voList = searchDataCriteria(searchCriteria, first, pageSize);

		return voList;
	}

	@SuppressWarnings({ "rawtypes" })
	private List<AuditFEVO> searchDataCriteria(List<? extends SearchObject> searchCriteria, int first, int pageSize) {

		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT s.audit_topic_in, s.audit_topic_en, " + 
				"		 s.status, TO_CHAR(f.target_date, 'dd FMMonth yyyy', 'nls_date_language=indonesian') target_date, " + 
				"		 s.audit_id, pd.name_in followupStatusIn, pd.name_en followupStatusEn, " + 
				"		 f.followup_status, f.audit_pic_followup_id, " + 
				"		 u1.name PIC1, u2.name PIC2, u3.name PIC3, " + 
				"		 f.compliance_note, f.compliance_status, pcomp.name_in comp_status_name " +
				"		 ,pdAuditor.name_in as auditorIn, pdAuditor.name_en as auditorEn,s.auditor" +
				"        ,pobj.name_in auditObjIn, pobj.name_en auditObjEn, pcat.name_in auditCatIn, pcat.name_en auditCatEn, " +
				"		 TO_CHAR(s.audit_date_from, 'dd FMMonth yyyy', 'nls_date_language=indonesian') auditDateFrom, TO_CHAR(s.audit_date_to, 'dd FMMonth yyyy', 'nls_date_language=indonesian') auditDateTo, s.scope " +
				"		 ,s.finding_name_in, s.finding_name_en, ma.audit_template_name_in, ma.audit_template_name_en, s.risk " +
				"	from wo_trc_audit_pic_followup f\r\n" + 
				"	INNER JOIN WO_TRC_AUDIT_PC_FP_BANK_CMITMT c ON c.AUD_PC_FP_BANK_CMITMT_ID = f.AUD_PC_FP_BANK_CMITMT_ID \r\n" + 
				"	INNER JOIN WO_TRC_AUDIT_CHECK_POINT p ON p.AUDIT_CHECK_POINT_ID = c.AUDIT_CHECK_POINT_ID \r\n" + 
				"	INNER JOIN WO_TRC_AUDIT s ON s.AUDIT_ID = p.AUDIT_ID \r\n" + 
				" LEFT JOIN wo_mst_audit ma ON ma.mst_audit_id = s.mst_audit_id " +
				" LEFT JOIN wo_mst_parameter_dtl pd ON pd.parameter_dtl_code = f.followup_status" + 
				" LEFT JOIN wo_mst_user u1 ON u1.user_id = f.user_id_1" + 
				" LEFT JOIN wo_mst_user u2 ON u2.user_id = f.user_id_2" + 
				" LEFT JOIN wo_mst_user u3 ON u3.user_id = f.user_id_3" + 
				" LEFT JOIN wo_mst_parameter_dtl pdAuditor ON pdAuditor.parameter_dtl_code = s.auditor" + 
				" LEFT JOIN wo_mst_parameter_dtl pobj ON pobj.parameter_dtl_code = s.audit_object" +
				" LEFT JOIN wo_mst_parameter_dtl pcat ON pcat.parameter_dtl_code = s.audit_category" +
				" LEFT JOIN wo_mst_parameter_dtl pcomp ON pcomp.parameter_dtl_code = f.compliance_status" +
				" WHERE 1=1 " + 
				"	AND s.enabled_flag = 'Y' " + 
				"	AND s.status = 'DATA_ACTIVE' " + 
				"	AND s.follow_up = 'Y' ");
				//"	AND (f.followup_status IS NULL OR f.followup_status <> 'PIC_DONE') ");

		sb = getQueryWhereString(sb, searchCriteria);
		sb.append(" ORDER BY s.audit_id DESC ");
		

		Query result = getSession().createSQLQuery(sb.toString());
		
		result.setFirstResult(first);
        result.setMaxResults(pageSize);
        
        this.getQuerySetValue(result, searchCriteria);
        
		List resultList = result.getResultList();

		List<AuditFEVO> vo = new ArrayList<AuditFEVO>();

		if (resultList != null) {
			for (int i = 0; i < resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
				AuditFEVO data = new AuditFEVO();
				data.setAuditTopicIn(obj[0]!=null?(String)obj[0]:null);
				data.setAuditTopicEn(obj[1]!=null?(String)obj[1]:null);
                data.setDueDate(obj[3]!=null?(String)obj[3]:null);
                data.setAuditId(obj[4]!=null?(MathUtil.returnIdObjectToLong(obj[4])):null);
                data.setStatusIn(obj[5]!=null?(String)obj[5]:null);
                data.setStatusEn(obj[6]!=null?(String)obj[6]:null);
                data.setStatusCd(obj[7]!=null?(String)obj[7]:null);
                
                data.setPicFollowupId(obj[8]!=null?(MathUtil.returnIdObjectToLong(obj[8])):null);
                data.setPicName1(obj[9]!=null?(String)obj[9]:null);
                data.setPicName2(obj[10]!=null?(String)obj[10]:null);
                data.setPicName3(obj[11]!=null?(String)obj[11]:null);
                data.setComplianceNote(obj[12]!=null?(String)obj[12]:null);
                data.setComplianceStatusCd(obj[13]!=null?(String)obj[13]:null);
                data.setComplianceStatus(obj[14]!=null?(String)obj[14]:null);
                
                data.setAuditorIn(obj[15] != null ? (String) obj[15] : null);
                data.setAuditorEn(obj[16] != null ? (String) obj[16] : null);
                data.setAuditorCd(obj[17] != null ? (String) obj[17] : null);

                data.setAuditObjectIn(obj[18]!=null?(String)obj[18]:null);
                data.setAuditObjectEn(obj[19]!=null?(String)obj[19]:null);
                data.setAuditCategoryIn(obj[20]!=null?(String)obj[20]:null);
                data.setAuditCategoryEn(obj[21]!=null?(String)obj[21]:null);
                data.setAuditDateFrom(obj[22]!=null?(String)obj[22]:null);
                data.setAuditDateTo(obj[23]!=null?(String)obj[23]:null);
                data.setScope(obj[24]!=null?FacesUtil.convertClobToString((Clob)obj[24]):null);
                data.setFindingNameIn(obj[25] != null ? (String) obj[25] : null);
                data.setFindingNameEn(obj[26] != null ? (String) obj[26] : null);
                data.setAuditTemplateNameIn(obj[27] != null ? (String) obj[27] : null);
                data.setAuditTemplateNameEn(obj[28] != null ? (String) obj[28] : null);
                
                data.setRisk(obj[29] != null ? (String) obj[29] : null);
                
                data.setPicNameList(new ArrayList<String>());
                data.getPicNameList().add(data!= null ? data.getPicName1() :null);
                data.getPicNameList().add(data!= null ? data.getPicName2() :null);
                data.getPicNameList().add(data!= null ? data.getPicName3() :null);

				vo.add(data);
			}
		}

		result.setFirstResult(first);
		result.setMaxResults(pageSize);

		return vo;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public AuditFEVO searchForDetail(Long id) {
		AuditFEVO data = new AuditFEVO();
		
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT s.audit_topic_in, s.audit_topic_en, " + 
				"		 s.status, TO_CHAR(f.target_date, 'dd FMMonth yyyy', 'nls_date_language=indonesian') target_date, " + 
				"		 s.audit_id, pd.name_in followupStatusIn, pd.name_en followupStatusEn, " + 
				"		 f.followup_status, f.audit_pic_followup_id, " + 
				"		 u1.name PIC1, u2.name PIC2, u3.name PIC3, " + 
				"		 f.compliance_note, f.compliance_status, pcomp.name_in comp_status_name " +
				"		 ,pdAuditor.name_in as auditorIn, pdAuditor.name_en as auditorEn,s.auditor" +
				"        ,pobj.name_in auditObjIn, pobj.name_en auditObjEn, pcat.name_in auditCatIn, pcat.name_en auditCatEn, " +
				"		 TO_CHAR(s.audit_date_from, 'dd FMMonth yyyy', 'nls_date_language=indonesian') auditDateFrom, TO_CHAR(s.audit_date_to, 'dd FMMonth yyyy', 'nls_date_language=indonesian') auditDateTo, s.scope " +
				"		 ,s.finding_name_in, s.finding_name_en, ma.audit_template_name_in, ma.audit_template_name_en, s.risk, p.audit_findings,p.bank_response,c.bank_commitment " +
				"	from wo_trc_audit_pic_followup f\r\n" + 
				"	INNER JOIN WO_TRC_AUDIT_PC_FP_BANK_CMITMT c ON c.AUD_PC_FP_BANK_CMITMT_ID = f.AUD_PC_FP_BANK_CMITMT_ID \r\n" + 
				"	INNER JOIN WO_TRC_AUDIT_CHECK_POINT p ON p.AUDIT_CHECK_POINT_ID = c.AUDIT_CHECK_POINT_ID \r\n" + 
				"	INNER JOIN WO_TRC_AUDIT s ON s.AUDIT_ID = p.AUDIT_ID \r\n" + 
				" LEFT JOIN wo_mst_audit ma ON ma.mst_audit_id = s.mst_audit_id " +
				" LEFT JOIN wo_mst_parameter_dtl pd ON pd.parameter_dtl_code = f.followup_status" + 
				" LEFT JOIN wo_mst_user u1 ON u1.user_id = f.user_id_1" + 
				" LEFT JOIN wo_mst_user u2 ON u2.user_id = f.user_id_2" + 
				" LEFT JOIN wo_mst_user u3 ON u3.user_id = f.user_id_3" + 
				" LEFT JOIN wo_mst_parameter_dtl pdAuditor ON pdAuditor.parameter_dtl_code = s.auditor" + 
				" LEFT JOIN wo_mst_parameter_dtl pobj ON pobj.parameter_dtl_code = s.audit_object" +
				" LEFT JOIN wo_mst_parameter_dtl pcat ON pcat.parameter_dtl_code = s.audit_category" +
				" LEFT JOIN wo_mst_parameter_dtl pcomp ON pcomp.parameter_dtl_code = f.compliance_status" +
				" WHERE 1=1 " + 
				"	AND s.enabled_flag = 'Y' AND f.audit_pic_followup_id = " + id +
				"	AND s.status = 'DATA_ACTIVE' " + 
				"	AND s.follow_up = 'Y' ");
				//"	AND (f.followup_status IS NULL OR f.followup_status <> 'PIC_DONE') ");

		Query result = getSession().createSQLQuery(sb.toString());
		
		List resultList = result.getResultList();

	
		if (resultList != null) {
			for (int i = 0; i < resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
				
				data.setAuditTopicIn(obj[0]!=null?(String)obj[0]:null);
				data.setAuditTopicEn(obj[1]!=null?(String)obj[1]:null);
                data.setDueDate(obj[3]!=null?(String)obj[3]:null);
                data.setAuditId(obj[4]!=null?(MathUtil.returnIdObjectToLong(obj[4])):null);
                data.setStatusIn(obj[5]!=null?(String)obj[5]:null);
                data.setStatusEn(obj[6]!=null?(String)obj[6]:null);
                data.setStatusCd(obj[7]!=null?(String)obj[7]:null);
                
                data.setPicFollowupId(obj[8]!=null?(MathUtil.returnIdObjectToLong(obj[8])):null);
                data.setPicName1(obj[9]!=null?(String)obj[9]:null);
                data.setPicName2(obj[10]!=null?(String)obj[10]:null);
                data.setPicName3(obj[11]!=null?(String)obj[11]:null);
                data.setComplianceNote(obj[12]!=null?(String)obj[12]:null);
                data.setComplianceStatusCd(obj[13]!=null?(String)obj[13]:null);
                data.setComplianceStatus(obj[14]!=null?(String)obj[14]:null);
                
                data.setAuditorIn(obj[15] != null ? (String) obj[15] : null);
                data.setAuditorEn(obj[16] != null ? (String) obj[16] : null);
                data.setAuditorCd(obj[17] != null ? (String) obj[17] : null);

                data.setAuditObjectIn(obj[18]!=null?(String)obj[18]:null);
                data.setAuditObjectEn(obj[19]!=null?(String)obj[19]:null);
                data.setAuditCategoryIn(obj[20]!=null?(String)obj[20]:null);
                data.setAuditCategoryEn(obj[21]!=null?(String)obj[21]:null);
                data.setAuditDateFrom(obj[22]!=null?(String)obj[22]:null);
                data.setAuditDateTo(obj[23]!=null?(String)obj[23]:null);
                data.setScope(obj[24]!=null?FacesUtil.convertClobToString((Clob)obj[24]):null);
                data.setFindingNameIn(obj[25] != null ? (String) obj[25] : null);
                data.setFindingNameEn(obj[26] != null ? (String) obj[26] : null);
                data.setAuditTemplateNameIn(obj[27] != null ? (String) obj[27] : null);
                data.setAuditTemplateNameEn(obj[28] != null ? (String) obj[28] : null);
                
                data.setRisk(obj[29] != null ? (String) obj[29] : null);
                data.setAuditFindings(obj[30]!=null?FacesUtil.convertClobToString((Clob)obj[30]):null);
                data.setBankResponse(obj[31]!=null?FacesUtil.convertClobToString((Clob)obj[31]):null);
                data.setBankCommitment(obj[32]!=null?FacesUtil.convertClobToString((Clob)obj[32]):null);
                
                data.setPicNameList(new ArrayList<String>());
                data.getPicNameList().add(data!= null ? data.getPicName1() :null);
                data.getPicNameList().add(data!= null ? data.getPicName2() :null);
                data.getPicNameList().add(data!= null ? data.getPicName3() :null);

			}
		}
		
		return data;
	}

}
