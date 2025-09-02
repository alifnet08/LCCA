package com.wo.module.trcAuditVerification.dao;


import java.sql.Clob;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.common.util.MathUtil;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.picFollowupConfirmation.constant.PICFollowupConfirmationConstants;
import com.wo.module.tmpAudit.dao.TmpAuditDao;
import com.wo.module.tmpAudit.vo.AuditConfirmationVO;
import com.wo.module.trcAudit.model.TrcAudit;
import com.wo.module.trcAuditVerification.vo.TrcAuditVerificationSearchVO;

@Repository("trcAuditVerificationDao")
public class TrcAuditVerificationDaoImpl extends GenericDAOHibernate<TrcAudit, Long> 
    implements TrcAuditVerificationDao {
	
	@Autowired
    @Qualifier("tmpAuditDao")
    private TmpAuditDao tmpAuditDao;
	
	@SuppressWarnings("rawtypes")
    private StringBuilder getQueryWhereString(StringBuilder sb, List<? extends SearchObject> searchCriteria) {
		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString();
				if (!StringUtils.isBlank(val)) {
					if (StringUtils.equals(PICFollowupConfirmationConstants.WHERE_USER_ID, col)) {
						
							//sb.append(" and exists (select 1 from wo_trc_audit_pic_compliance wtspc where wtspc.audit_id = f.audit_id and wtspc.user_id = "+val+")");
							sb.append(" and (exists (select 1 from wo_trc_audit_pic_compliance wtspc where wtspc.audit_id = s.audit_id and wtspc.user_id = "+val+") OR exists (select 1 from wo_mst_user u2 inner join wo_mst_responsibility r2 on u2.responsibility_id = r2.responsibility_id and u2.user_id = "+val+" and UPPER(r2.name) = UPPER('Super Administrator')))");
							
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

    	sb.append(" SELECT count(1) " + 
    			" FROM wo_trc_audit s" + 
				" INNER JOIN WO_TRC_AUDIT_CHECK_POINT p ON p.AUDIT_ID = s.AUDIT_ID " +
				" INNER JOIN WO_TRC_AUDIT_PC_FP_BANK_CMITMT c ON c.AUDIT_CHECK_POINT_ID = p.AUDIT_CHECK_POINT_ID " +
				" INNER join wo_trc_audit_pic_followup apf on apf.AUD_PC_FP_BANK_CMITMT_ID = c.AUD_PC_FP_BANK_CMITMT_ID " +
				" LEFT JOIN wo_mst_audit ma ON ma.mst_audit_id = s.mst_audit_id " +
				" LEFT JOIN wo_mst_parameter_dtl pd ON pd.parameter_dtl_code = s.status" +
				" LEFT JOIN wo_mst_parameter_dtl paud ON paud.parameter_dtl_code = s.auditor" + 
				" LEFT JOIN wo_mst_parameter_dtl pobj ON pobj.parameter_dtl_code = s.audit_object" +
				" LEFT JOIN wo_mst_parameter_dtl pcat ON pcat.parameter_dtl_code = s.audit_category" +
				" WHERE 1=1 " + 
				"	AND s.enabled_flag = 'Y' " + 
				"	AND s.status = 'DATA_ACTIVE' " + 
				"   AND (apf.followup_status = 'PIC_DONE' " + 
				"      OR apf.followup_status = 'PIC_EXTENSION') " + 
				"   AND (apf.compliance_status <> 'COMPLIANCE_CLOSE' " + 
				"      OR apf.compliance_status IS NULL) " + 
				"");
		sb = getQueryWhereString(sb, searchCriteria);
        
        Query result = getSession().createSQLQuery(sb.toString());
       
        
        return (Number) result.getSingleResult();
    }
    
    @SuppressWarnings("rawtypes")
    public List<TrcAuditVerificationSearchVO> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize, String sortField, SortOrder sortOrder) 
            throws Exception {
        
        List<TrcAuditVerificationSearchVO> voList = searchDataCriteria(searchCriteria, first, pageSize);

        return voList;
    }
    
    @SuppressWarnings("rawtypes")
    private List<TrcAuditVerificationSearchVO> searchDataCriteria(List<? extends SearchObject> searchCriteria, int first, int pageSize) {
        
        StringBuilder sb = new StringBuilder();
//		sb.append(" SELECT s.audit_topic_in, s.audit_topic_en, " + 
//				"		 s.status, TO_CHAR(f.target_date, 'dd-Mon-yyyy')target_date, " + 
//				"		 f.audit_pic_followup_id,pd.name_in statusIn,pd.name_en statusEn" + 
//				" FROM wo_trc_audit s" + 
//				" INNER JOIN wo_trc_audit_pic_followup f ON f.audit_id = s.audit_id" + 
//				" LEFT JOIN wo_mst_parameter_dtl pd ON pd.parameter_dtl_code = s.status" + 
//				" WHERE 1=1 " + 
//				"	AND s.enabled_flag = 'Y' " + 
//				"	AND s.status = 'DATA_ACTIVE' " + 
//				"	AND f.followup_status = 'PIC_DONE' " + 
//				"	AND (f.compliance_status <> 'COMPLIANCE_CLOSE' OR f.compliance_status IS NULL) ");
		
        sb.append(" SELECT s.audit_topic_in, s.audit_topic_en, s.status, s.audit_id,pd.name_in statusIn,pd.name_en statusEn, " +
				"		 paud.name_in auditorIn, paud.name_en auditorEn, pobj.name_in auditObjIn, pobj.name_en auditObjEn, pcat.name_in auditCatIn, pcat.name_en auditCatEn, " +
				"		 TO_CHAR(s.audit_date_from, 'dd-Mon-yyyy') auditDateFrom, TO_CHAR(s.audit_date_to, 'dd-Mon-yyyy') auditDateTo, s.scope " +
				"		 ,s.finding_name_in, s.finding_name_en, ma.audit_template_name_in, ma.audit_template_name_en,apf.audit_pic_followup_id " +
				" FROM wo_trc_audit s" + 
				" INNER JOIN WO_TRC_AUDIT_CHECK_POINT p ON p.AUDIT_ID = s.AUDIT_ID " +
				" INNER JOIN WO_TRC_AUDIT_PC_FP_BANK_CMITMT c ON c.AUDIT_CHECK_POINT_ID = p.AUDIT_CHECK_POINT_ID " +
				" INNER join wo_trc_audit_pic_followup apf on apf.AUD_PC_FP_BANK_CMITMT_ID = c.AUD_PC_FP_BANK_CMITMT_ID " +
				" LEFT JOIN wo_mst_audit ma ON ma.mst_audit_id = s.mst_audit_id " +
				" LEFT JOIN wo_mst_parameter_dtl pd ON pd.parameter_dtl_code = s.status" +
				" LEFT JOIN wo_mst_parameter_dtl paud ON paud.parameter_dtl_code = s.auditor" + 
				" LEFT JOIN wo_mst_parameter_dtl pobj ON pobj.parameter_dtl_code = s.audit_object" +
				" LEFT JOIN wo_mst_parameter_dtl pcat ON pcat.parameter_dtl_code = s.audit_category" +
				" WHERE 1=1 " + 
				"	AND s.enabled_flag = 'Y' " + 
				"	AND s.status = 'DATA_ACTIVE' " + 
				"   AND (apf.followup_status = 'PIC_DONE' " + 
				"      OR apf.followup_status = 'PIC_EXTENSION') " + 
				"   AND (apf.compliance_status <> 'COMPLIANCE_CLOSE' " + 
				"      OR apf.compliance_status IS NULL) " + 
				/*"	and EXISTS " + 
				"  ( " + 
				"    select * from wo_trc_audit_pic_followup f  " + 
				" 		INNER JOIN WO_TRC_AUDIT_PC_FP_BANK_CMITMT c ON c.AUD_PC_FP_BANK_CMITMT_ID = f.AUD_PC_FP_BANK_CMITMT_ID " +
				" 		INNER JOIN WO_TRC_AUDIT_CHECK_POINT p ON p.AUDIT_CHECK_POINT_ID = c.AUDIT_CHECK_POINT_ID " +
				" 		INNER JOIN WO_TRC_AUDIT s2 ON s2.AUDIT_ID = p.AUDIT_ID " +
				"      where s2.audit_id = s.audit_id " + 
				"      AND (f.followup_status = 'PIC_DONE' " + 
				"      OR f.followup_status = 'PIC_EXTENSION') " + 
				"      AND (f.compliance_status <> 'COMPLIANCE_CLOSE' " + 
				"      OR f.compliance_status IS NULL) " + 
				"  ) " +*/
				"");
        sb = getQueryWhereString(sb, searchCriteria);
        sb.append(" ORDER BY s.audit_id DESC ");
        
        
        Query result = getSession().createSQLQuery(sb.toString());
        result.setFirstResult(first);
        result.setMaxResults(pageSize);
        
        List resultList = result.getResultList();
        
        List<TrcAuditVerificationSearchVO> vo = new ArrayList<TrcAuditVerificationSearchVO>();
        
        if(resultList!=null) {
            for(int i=0; i<resultList.size(); i++) {
                Object[] obj = (Object[]) resultList.get(i);
                TrcAuditVerificationSearchVO data = new TrcAuditVerificationSearchVO();
                data.setAuditTopicIn(obj[0]!=null?(String)obj[0]:null);
                data.setAuditTopicEn(obj[1]!=null?(String)obj[1]:null);
                //data.setDueDate(obj[3]!=null?(String)obj[3]:null);
                //data.setStatus(obj[2]!=null?(String)obj[2]:null);
                //data.setSocializationId(obj[4]!=null?((java.math.BigInteger)obj[4]).longValue():null);
                data.setAuditId(obj[3]!=null?(MathUtil.returnIdObjectToLong(obj[3])):null);
                data.setStatusList(getAuditConfirmationByAuditId(data.getAuditId()));
                data.setStatusIn(obj[4]!=null?(String)obj[4]:null);
                data.setStatusEn(obj[5]!=null?(String)obj[5]:null);
                data.setAuditorIn(obj[6]!=null?(String)obj[6]:null);
                data.setAuditorEn(obj[7]!=null?(String)obj[7]:null);
                data.setAuditObjectIn(obj[8]!=null?(String)obj[8]:null);
                data.setAuditObjectEn(obj[9]!=null?(String)obj[9]:null);
                data.setAuditCategoryIn(obj[10]!=null?(String)obj[10]:null);
                data.setAuditCategoryEn(obj[11]!=null?(String)obj[11]:null);
                data.setAuditDateFrom(obj[12]!=null?(String)obj[12]:null);
                data.setAuditDateTo(obj[13]!=null?(String)obj[13]:null);
                data.setScope(obj[14]!=null?FacesUtil.convertClobToString((Clob)obj[14]):null);
                data.setFindingNameIn(obj[15] != null ? (String) obj[15] : null);
                data.setFindingNameEn(obj[16] != null ? (String) obj[16] : null);
                data.setAuditTemplateNameIn(obj[17] != null ? (String) obj[17] : null);
                data.setAuditTemplateNameEn(obj[18] != null ? (String) obj[18] : null);
                data.setAuditPicFollowupId(obj[19]!=null?(MathUtil.returnIdObjectToLong(obj[19])):null);
                vo.add(data);
            }
        }

        result.setFirstResult(first);
        result.setMaxResults(pageSize);
        
        return vo;
    }

	public TmpAuditDao getTmpAuditDao() {
		return tmpAuditDao;
	}

	public void setTmpAuditDao(TmpAuditDao tmpAuditDao) {
		this.tmpAuditDao = tmpAuditDao;
	}
    
	@SuppressWarnings({ "rawtypes", "unused" })
	private List<AuditConfirmationVO> getAuditConfiramtionByAuditFollowupId(Long auditFollowupId) {
		StringBuilder sb = new StringBuilder();
		
		sb.append(" select user1.name as pic1"
				+ "		,user2.name as pic2"
				+ "		,user3.name as pic3"
				+ "		,pd1.name_in as statusIn"
				+ "		,pd1.name_en as statusEn"
				+ " from wo_trc_audit s "
				+ " 	INNER JOIN WO_TRC_AUDIT_CHECK_POINT p ON p.AUDIT_ID = s.AUDIT_ID " 
				+ " 	INNER JOIN WO_TRC_AUDIT_PC_FP_BANK_CMITMT c ON c.AUDIT_CHECK_POINT_ID = p.AUDIT_CHECK_POINT_ID " 
				+ "		INNER join wo_trc_audit_pic_followup apf on apf.AUD_PC_FP_BANK_CMITMT_ID = c.AUD_PC_FP_BANK_CMITMT_ID "
				+ "		left join wo_mst_user user1 on user1.user_id = apf.user_id_1 "
				+ " 	left join wo_mst_user user2 on user2.user_id = apf.user_id_2 "
				+ " 	left join wo_mst_user user3 on user3.user_id = apf.user_id_3 "
				+ "		left join wo_mst_parameter_dtl pd1 on pd1.parameter_dtl_code = apf.followup_status"
				+ " where 1 = 1 "
				+ "		AND s.enabled_flag = 'Y' "
				+ "		AND s.status = 'DATA_ACTIVE' "
				+ "		AND (apf.followup_status = 'PIC_DONE' OR apf.followup_status = 'PIC_EXTENSION') "
				+ "		AND (apf.compliance_status <> 'COMPLIANCE_CLOSE' OR apf.compliance_status IS NULL) ");
		
		if (auditFollowupId != null) {
			sb.append("		and apf.audit_pic_followup_id = '" + auditFollowupId + "' ");
		}	
		
		 Query result = getSession().createSQLQuery(sb.toString());
	     List resultList = result.getResultList();
	     
	     List<AuditConfirmationVO> vo = new ArrayList<AuditConfirmationVO>();
	     
	     if (resultList != null) {
			for (int i = 0; i < resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
				AuditConfirmationVO data = new AuditConfirmationVO();
				
				data.setPic1(obj[0] != null ? (String) obj[0] : null);
				data.setPic2(obj[1] != null ? (String) obj[1] : null);
				data.setPic3(obj[2] != null ? (String) obj[2] : null);
				data.setFollowupStatusIn(obj[3] != null ? (String) obj[3] : null);
				data.setFollowupStatusEn(obj[4] != null ? (String) obj[4] : null);
				
				vo.add(data);
			}
		}
	     
	     return vo;
	}
    
	@SuppressWarnings("rawtypes")
	private List<AuditConfirmationVO> getAuditConfirmationByAuditId(Long auditId) {
		StringBuilder sb = new StringBuilder();
		
		sb.append(" select user1.name as pic1"
				+ "		,user2.name as pic2"
				+ "		,user3.name as pic3"
				+ "		,pd1.name_in as statusIn"
				+ "		,pd1.name_en as statusEn"
				+ " , TO_CHAR(apf.target_date, 'dd-Mon-yyyy')target_date "
				+ " , apf.followup_status "
				+ " from wo_trc_audit s "
				+ " 	INNER JOIN WO_TRC_AUDIT_CHECK_POINT p ON p.AUDIT_ID = s.AUDIT_ID " 
				+ " 	INNER JOIN WO_TRC_AUDIT_PC_FP_BANK_CMITMT c ON c.AUDIT_CHECK_POINT_ID = p.AUDIT_CHECK_POINT_ID " 
				+ "		INNER join wo_trc_audit_pic_followup apf on apf.AUD_PC_FP_BANK_CMITMT_ID = c.AUD_PC_FP_BANK_CMITMT_ID "
				+ "		left join wo_mst_user user1 on user1.user_id = apf.user_id_1 "
				+ " 	left join wo_mst_user user2 on user2.user_id = apf.user_id_2 "
				+ " 	left join wo_mst_user user3 on user3.user_id = apf.user_id_3 "
				+ "		left join wo_mst_parameter_dtl pd1 on pd1.parameter_dtl_code = apf.followup_status"
				+ " where 1 = 1 "
				+ "		AND s.enabled_flag = 'Y' "
				+ "		AND s.status = 'DATA_ACTIVE' "
				+ "		AND (apf.followup_status = 'PIC_DONE' OR apf.followup_status = 'PIC_EXTENSION') ");
		
		if (auditId != null) {
			sb.append("		and s.audit_id = '" + auditId + "' ");
			//sb.append("		and (select count(1) from wo_trc_audit_pic_followup apf1 where apf1.audit_id = '" + auditId + "' " + 
					//"                  and apf1.compliance_status = 'COMPLIANCE_CLOSE') < (select count(1) from wo_trc_audit_pic_followup apf1 where apf1.audit_id = '" + auditId + "') ");
		}	
		
		 Query result = getSession().createSQLQuery(sb.toString());
	     List resultList = result.getResultList();
	     
	     List<AuditConfirmationVO> vo = new ArrayList<AuditConfirmationVO>();
	     
	     if (resultList != null) {
			for (int i = 0; i < resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
				AuditConfirmationVO data = new AuditConfirmationVO();
				
				data.setPic1(obj[0] != null ? (String) obj[0] : null);
				data.setPic2(obj[1] != null ? (String) obj[1] : null);
				data.setPic3(obj[2] != null ? (String) obj[2] : null);
				data.setFollowupStatusIn(obj[3] != null ? (String) obj[3] : null);
				data.setFollowupStatusEn(obj[4] != null ? (String) obj[4] : null);
				data.setTargetDate(obj[5] != null ? (String) obj[5] : null);
				data.setFollowupStatusCd(obj[6] != null ? (String) obj[6] : null);
				vo.add(data);
			}
		}
	     
	     return vo;
	}
}
