package com.wo.module.trcAudit.dao;

import java.sql.Clob;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.SortOrder;
import org.springframework.stereotype.Repository;

import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.common.util.MathUtil;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.picFollowupConfirmation.constant.PICFollowupConfirmationConstants;
import com.wo.module.trcAudit.model.TrcAudit;
import com.wo.module.trcAudit.vo.TrcAuditVO;

@Repository("trcAuditDao")
public class TrcAuditDaoImpl extends GenericDAOHibernate<TrcAudit, Long> implements TrcAuditDao {

	@SuppressWarnings("rawtypes")
	private StringBuilder getQueryWhereString(StringBuilder sb, List<? extends SearchObject> searchCriteria) {
		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString();
				if (!StringUtils.isBlank(val)) {
					if (StringUtils.equals(PICFollowupConfirmationConstants.WHERE_USER_ID, col)) {

						sb.append(" and (f.user_id_1 = " + val + " or f.user_id_2 = " + val + " or f.user_id_3 = " + val
								+ ")");

					}
				}
			}
		}
		return sb;
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
		sb.append(" SELECT count(1)" + 
				" FROM wo_trc_audit s" + 
				" INNER JOIN wo_trc_audit_pic_followup f ON f.audit_id = s.audit_id" +
				" LEFT JOIN wo_mst_audit ma ON ma.mst_audit_id = s.mst_audit_id " +
				" WHERE 1=1 " + 
				"	AND s.enabled_flag = 'Y' " + 
				"	AND s.status = 'DATA_ACTIVE' " + 
				"	AND s.follow_up = 'Y' " + 
				"	AND (f.followup_status IS NULL OR f.followup_status <> 'PIC_DONE') ");

		sb = getQueryWhereString(sb, searchCriteria);

		Query result = getSession().createSQLQuery(sb.toString());

		return (Number) result.getSingleResult();
	}

	@SuppressWarnings("rawtypes")
	public List<TrcAuditVO> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize,
			String sortField, SortOrder sortOrder) throws Exception {

		List<TrcAuditVO> voList = searchDataCriteria(searchCriteria, first, pageSize);

		return voList;
	}

	@SuppressWarnings({ "rawtypes" })
	private List<TrcAuditVO> searchDataCriteria(List<? extends SearchObject> searchCriteria, int first, int pageSize) {

		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT s.audit_topic_in, s.audit_topic_en, " + 
				"		 s.status, TO_CHAR(f.target_date, 'dd-Mon-yyyy') target_date, " + 
				"		 s.audit_id, pd.name_in followupStatusIn, pd.name_en followupStatusEn, " + 
				"		 f.audit_pic_followup_id, " + 
				"		 u1.name PIC1, u2.name PIC2, u3.name PIC3, " + 
				"		 f.compliance_note" +
				"		 ,pdAuditor.name_in as auditorIn, pdAuditor.name_en as auditorEn,s.auditor" +
				"        ,pobj.name_in auditObjIn, pobj.name_en auditObjEn, pcat.name_in auditCatIn, pcat.name_en auditCatEn, " +
				"		 TO_CHAR(s.audit_date_from, 'dd-Mon-yyyy') auditDateFrom, TO_CHAR(s.audit_date_to, 'dd-Mon-yyyy') auditDateTo, s.scope " +
				"		 ,s.finding_name_in, s.finding_name_en, ma.audit_template_name_in, ma.audit_template_name_en " +
				" FROM wo_trc_audit s" + 
				" INNER JOIN wo_trc_audit_pic_followup f ON f.audit_id = s.audit_id" +
				" LEFT JOIN wo_mst_audit ma ON ma.mst_audit_id = s.mst_audit_id " +
				" LEFT JOIN wo_mst_parameter_dtl pd ON pd.parameter_dtl_code = f.followup_status" + 
				" LEFT JOIN wo_mst_user u1 ON u1.user_id = f.user_id_1" + 
				" LEFT JOIN wo_mst_user u2 ON u2.user_id = f.user_id_2" + 
				" LEFT JOIN wo_mst_user u3 ON u3.user_id = f.user_id_3" + 
				" LEFT JOIN wo_mst_parameter_dtl pdAuditor ON pdAuditor.parameter_dtl_code = s.auditor" + 
				" LEFT JOIN wo_mst_parameter_dtl pobj ON pobj.parameter_dtl_code = s.audit_object" +
				" LEFT JOIN wo_mst_parameter_dtl pcat ON pcat.parameter_dtl_code = s.audit_category" +
				" WHERE 1=1 " + 
				"	AND s.enabled_flag = 'Y' " + 
				"	AND s.status = 'DATA_ACTIVE' " + 
				"	AND s.follow_up = 'Y' " + 
				"	AND (f.followup_status IS NULL OR f.followup_status <> 'PIC_DONE') ");

		sb = getQueryWhereString(sb, searchCriteria);
		sb.append(" ORDER BY s.audit_id DESC ");
		

		Query result = getSession().createSQLQuery(sb.toString());
		result.setFirstResult(first);
        result.setMaxResults(pageSize);
        
		List resultList = result.getResultList();

		List<TrcAuditVO> vo = new ArrayList<TrcAuditVO>();

		if (resultList != null) {
			for (int i = 0; i < resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
				TrcAuditVO data = new TrcAuditVO();
				data.setAuditTopicIn(obj[0]!=null?(String)obj[0]:null);
				data.setAuditTopicEn(obj[1]!=null?(String)obj[1]:null);
                data.setDueDate(obj[3]!=null?(String)obj[3]:null);
                data.setAuditId(obj[4]!=null?(MathUtil.returnIdObjectToLong(obj[4])):null);
                data.setStatusIn(obj[5]!=null?(String)obj[5]:null);
                data.setStatusEn(obj[6]!=null?(String)obj[6]:null);
                data.setPicFollowupId(obj[7]!=null?(MathUtil.returnIdObjectToLong(obj[7])):null);
                data.setPicName1(obj[8]!=null?(String)obj[8]:null);
                data.setPicName2(obj[9]!=null?(String)obj[9]:null);
                data.setPicName3(obj[10]!=null?(String)obj[10]:null);
                data.setComplianceNote(obj[11]!=null?(String)obj[11]:null);
                data.setAuditorIn(obj[12] != null ? (String) obj[12] : null);
                data.setAuditorEn(obj[13] != null ? (String) obj[13] : null);
                data.setAuditorCd(obj[14] != null ? (String) obj[14] : null);

                data.setAuditObjectIn(obj[15]!=null?(String)obj[15]:null);
                data.setAuditObjectEn(obj[16]!=null?(String)obj[16]:null);
                data.setAuditCategoryIn(obj[17]!=null?(String)obj[17]:null);
                data.setAuditCategoryEn(obj[18]!=null?(String)obj[18]:null);
                data.setAuditDateFrom(obj[19]!=null?(String)obj[19]:null);
                data.setAuditDateTo(obj[20]!=null?(String)obj[20]:null);
                data.setScope(obj[21]!=null?FacesUtil.convertClobToString((Clob)obj[21]):null);
                data.setFindingNameIn(obj[22] != null ? (String) obj[22] : null);
                data.setFindingNameEn(obj[23] != null ? (String) obj[23] : null);
                data.setAuditTemplateNameIn(obj[24] != null ? (String) obj[24] : null);
                data.setAuditTemplateNameEn(obj[25] != null ? (String) obj[25] : null);
                
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

}
