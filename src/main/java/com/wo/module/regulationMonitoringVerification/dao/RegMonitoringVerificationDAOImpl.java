/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.regulationMonitoringVerification.dao;


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
import com.wo.module.regulationMonitoring.dao.RegulationMonitoringDAO;
import com.wo.module.regulationMonitoring.model.RegMonitoringTrc;
import com.wo.module.regulationMonitoringVerification.vo.RegMonitoringVerificationSearchVO;



/**
 *
 * @author hendra
 */

@Repository("regMonitoringVerificationDAO")
public class RegMonitoringVerificationDAOImpl extends GenericDAOHibernate<RegMonitoringTrc, Long> 
    implements RegMonitoringVerificationDAO {
	
	@Autowired
    @Qualifier("regulationMonitoringDAO")
    private RegulationMonitoringDAO regulationMonitoringDAO;
	

	public RegulationMonitoringDAO getRegulationMonitoringDAO() {
		return regulationMonitoringDAO;
	}

	public void setRegulationMonitoringDAO(RegulationMonitoringDAO regulationMonitoringDAO) {
		this.regulationMonitoringDAO = regulationMonitoringDAO;
	}

	@SuppressWarnings("rawtypes")
    private StringBuilder getQueryWhereString(StringBuilder sb, List<? extends SearchObject> searchCriteria) {
		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString();
				if (!StringUtils.isBlank(val)) {
					if (StringUtils.equals("USER_ID", col)) {
						
							//sb.append(" and exists (select 1 from WO_TRC_REG_MONITOR_PIC_CMPLC wtspc where wtspc.REG_MONITORING_ID = f.REG_MONITORING_ID and wtspc.user_id = "+val+")");
							
							sb.append(" and (exists (select 1 from WO_TRC_REG_MONITOR_PIC_CMPLC wtspc where wtspc.REG_MONITORING_ID = f.REG_MONITORING_ID and wtspc.user_id = "+val+") OR exists (select 1 from wo_mst_user u2 inner join wo_mst_responsibility r2 on u2.responsibility_id = r2.responsibility_id and u2.user_id = "+val+" and UPPER(r2.name) = UPPER('Super Administrator')))");
							
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
    	sb.append(" select count(1) ");
    	sb.append(" FROM WO_TRC_REG_MONITORING s inner join WO_TRC_REG_MONITOR_REGULATION sr on s.REG_MONITORING_ID = sr.REG_MONITORING_ID ");
		sb.append(" inner join wo_mst_regulation r on r.regulation_id = sr.regulation_id  ");
		sb.append(" inner join WO_TRC_REG_MONITORING_PIC_FP f on f.REG_MONITORING_ID = s.REG_MONITORING_ID  ");
		sb.append(" WHERE 1=1 ");
		sb.append(" and s.enabled_flag = 'Y' ");
		sb.append(" and sr.primary_flag = 'Y' ");
		sb.append(" and s.status = 'DATA_ACTIVE' ");		
		sb.append(" and f.followup_status = 'PIC_DONE' ");
		sb.append(" and (f.compliance_status <> 'COMPLIANCE_CLOSE' or f.compliance_status is null) ");
       
		sb = getQueryWhereString(sb, searchCriteria);
        
        Query result = getSession().createSQLQuery(sb.toString());
       
        
        return (Number) result.getSingleResult();
    }
    
    @SuppressWarnings("rawtypes")
    public List<RegMonitoringVerificationSearchVO> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize, String sortField, SortOrder sortOrder) 
            throws Exception {
        
        List<RegMonitoringVerificationSearchVO> voList = searchDataCriteria(searchCriteria, first, pageSize);

        return voList;
    }
    
    @SuppressWarnings("rawtypes")
    private List<RegMonitoringVerificationSearchVO> searchDataCriteria(List<? extends SearchObject> searchCriteria, int first, int pageSize) {
        
        StringBuilder sb = new StringBuilder();
		sb.append(" SELECT  ");
		sb.append("        r.name_in, r.name_en, ");
		sb.append("        s.status,TO_CHAR(f.target_date, 'dd-Mon-yyyy')target_date, ");
		sb.append("        s.REG_MONITORING_ID,pd.name_in statusIn,pd.name_en statusEn ");
		sb.append(" FROM WO_TRC_REG_MONITORING s inner join WO_TRC_REG_MONITOR_REGULATION sr on s.REG_MONITORING_ID = sr.REG_MONITORING_ID ");
		sb.append(" inner join wo_mst_regulation r on r.regulation_id = sr.regulation_id  ");
		sb.append(" inner join WO_TRC_REG_MONITORING_PIC_FP f on f.REG_MONITORING_ID = s.REG_MONITORING_ID  ");
		sb.append(" left join wo_mst_parameter_dtl pd on pd.parameter_dtl_code = s.status  ");
		sb.append(" WHERE 1=1 ");
		sb.append(" and s.enabled_flag = 'Y' ");
		sb.append(" and sr.primary_flag = 'Y' ");
		sb.append(" and s.status = 'DATA_ACTIVE' ");
		sb.append(" and f.followup_status = 'PIC_DONE' ");
		sb.append(" and (f.compliance_status <> 'COMPLIANCE_CLOSE' or f.compliance_status is null) ");
		
        
        sb = getQueryWhereString(sb, searchCriteria);
        sb.append(" ORDER BY s.REG_MONITORING_ID DESC ");
        
        
        Query result = getSession().createSQLQuery(sb.toString());
        result.setFirstResult(first);
        result.setMaxResults(pageSize);
        
        List resultList = result.getResultList();
        
        List<RegMonitoringVerificationSearchVO> vo = new ArrayList<RegMonitoringVerificationSearchVO>();
        
        if(resultList!=null) {
            for(int i=0; i<resultList.size(); i++) {
                Object[] obj = (Object[]) resultList.get(i);
                RegMonitoringVerificationSearchVO data = new RegMonitoringVerificationSearchVO();
                data.setJudulPeraturanIn(obj[0]!=null?(String)obj[0]:null);
                data.setJudulPeraturanEn(obj[1]!=null?(String)obj[1]:null);
                data.setDueDate(obj[3]!=null?(String)obj[3]:null);
                //data.setStatus(obj[2]!=null?(String)obj[2]:null);
                //data.setSocializationId(obj[4]!=null?((java.math.BigInteger)obj[4]).longValue():null);
                data.setRegMonitoringId(obj[4]!=null?(MathUtil.returnIdObjectToLong(obj[4])):null);
                data.setStatusList(regulationMonitoringDAO.getDataConfirmStatusByRegMonitoringId(data.getRegMonitoringId()));
                data.setStatusIn(obj[5]!=null?(String)obj[5]:null);
                data.setStatusEn(obj[6]!=null?(String)obj[6]:null);
                vo.add(data);
            }
        }

        result.setFirstResult(first);
        result.setMaxResults(pageSize);
        
        return vo;
    }
    
   
    
}
