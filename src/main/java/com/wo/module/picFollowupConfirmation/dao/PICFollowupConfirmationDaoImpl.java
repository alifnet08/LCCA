/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.picFollowupConfirmation.dao;


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
import com.wo.module.picFollowupConfirmation.constant.PICFollowupConfirmationConstants;
import com.wo.module.picFollowupConfirmation.vo.PICFollowupConfirmationVO;
import com.wo.module.regulationSocialization.dao.RegulationSocializationDao;
import com.wo.module.regulationSocialization.model.SocializationTmp;



/**
 *
 * @author hendra
 */

@Repository("picFollowupConfirmationDao")
public class PICFollowupConfirmationDaoImpl extends GenericDAOHibernate<SocializationTmp, Long> 
    implements PICFollowupConfirmationDao {
	
	@Autowired
    @Qualifier("regulationSocializationDao")
    private RegulationSocializationDao regulationSocializationDao;
	
	public RegulationSocializationDao getRegulationSocializationDao() {
		return regulationSocializationDao;
	}

	public void setRegulationSocializationDao(RegulationSocializationDao regulationSocializationDao) {
		this.regulationSocializationDao = regulationSocializationDao;
	}

	@SuppressWarnings("rawtypes")
    private StringBuilder getQueryWhereString(StringBuilder sb, List<? extends SearchObject> searchCriteria) {
		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString();
				if (!StringUtils.isBlank(val)) {
					if (StringUtils.equals(PICFollowupConfirmationConstants.WHERE_USER_ID, col)) {
						
							sb.append(" and (f.user_id_1 = "+val+" or f.user_id_2 = "+val+" or f.user_id_3 = "+val);
							
							sb.append(" or exists (select 1 from wo_trc_socialization_pic_cmplc comp where comp.socialization_id = s.socialization_id and comp.user_id = "+val+"))");
						
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
    	sb.append(" FROM wo_trc_socialization s ");
		sb.append(" inner join wo_trc_socialization_rgltn sr on s.socialization_id = sr.socialization_id ");
		sb.append(" inner join wo_mst_regulation r on r.regulation_id = sr.regulation_id  ");
		sb.append(" inner join wo_trc_socialization_pic_fp f on f.socialization_id = s.socialization_id  ");
		sb.append(" left join wo_mst_parameter_dtl pd on pd.parameter_dtl_code = f.followup_status  ");
		sb.append(" left join wo_mst_user u1 on u1.user_id = f.user_id_1  ");
		sb.append(" left join wo_mst_user u2 on u2.user_id = f.user_id_2  ");
		sb.append(" left join wo_mst_user u3 on u3.user_id = f.user_id_3  ");
		sb.append(" WHERE 1=1 ");
		sb.append(" and s.enabled_flag = 'Y' ");
		sb.append(" and sr.primary_flag = 'Y' ");
		sb.append(" and s.status = 'DATA_ACTIVE' ");
		sb.append(" and s.follow_up = 'Y' ");
		sb.append(" and (f.followup_status is null OR f.followup_status <> 'PIC_DONE')  ");
       
        sb = getQueryWhereString(sb, searchCriteria);
        
        Query result = getSession().createSQLQuery(sb.toString());
       
        
        return (Number) result.getSingleResult();
    }
    
    @SuppressWarnings("rawtypes")
    public List<PICFollowupConfirmationVO> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize, String sortField, SortOrder sortOrder) 
            throws Exception {
        
        List<PICFollowupConfirmationVO> voList = searchDataCriteria(searchCriteria, first, pageSize);

        return voList;
    }
    
    @SuppressWarnings("rawtypes")
    private List<PICFollowupConfirmationVO> searchDataCriteria(List<? extends SearchObject> searchCriteria, int first, int pageSize) {
        
        StringBuilder sb = new StringBuilder();
		sb.append(" SELECT  ");
		sb.append("   r.name_in, r.name_en, ");
		sb.append("   s.status,TO_CHAR(f.target_date, 'dd-Mon-yyyy') target_date, ");
		sb.append("   s.socialization_id, pd.name_in statusIn, pd.name_en statusEn, ");
		sb.append("   socialization_pic_followup_id, ");
		sb.append("   u1.name PIC1, u2.name PIC2, u3.name PIC3, ");
		sb.append("   f.compliance_note ");
		sb.append(" FROM wo_trc_socialization s ");
		sb.append(" inner join wo_trc_socialization_rgltn sr on s.socialization_id = sr.socialization_id ");
		sb.append(" inner join wo_mst_regulation r on r.regulation_id = sr.regulation_id  ");
		sb.append(" inner join wo_trc_socialization_pic_fp f on f.socialization_id = s.socialization_id  ");
		sb.append(" left join wo_mst_parameter_dtl pd on pd.parameter_dtl_code = f.followup_status  ");
		sb.append(" left join wo_mst_user u1 on u1.user_id = f.user_id_1  ");
		sb.append(" left join wo_mst_user u2 on u2.user_id = f.user_id_2  ");
		sb.append(" left join wo_mst_user u3 on u3.user_id = f.user_id_3  ");
		sb.append(" WHERE 1=1 ");
		sb.append(" and s.enabled_flag = 'Y' ");
		sb.append(" and sr.primary_flag = 'Y' ");
		sb.append(" and s.status = 'DATA_ACTIVE' ");
		sb.append(" and s.follow_up = 'Y' ");
		sb.append(" and (f.followup_status is null OR f.followup_status <> 'PIC_DONE')  ");
        
        sb = getQueryWhereString(sb, searchCriteria);
        sb.append(" ORDER BY s.socialization_id DESC ");
        
        
        Query result = getSession().createSQLQuery(sb.toString());
        result.setFirstResult(first);
        result.setMaxResults(pageSize);
        
        List resultList = result.getResultList();
        
        List<PICFollowupConfirmationVO> vo = new ArrayList<PICFollowupConfirmationVO>();
        
        if(resultList!=null) {
            for(int i=0; i<resultList.size(); i++) {
                Object[] obj = (Object[]) resultList.get(i);
                PICFollowupConfirmationVO data = new PICFollowupConfirmationVO();
                data.setJudulPeraturanIn(obj[0]!=null?(String)obj[0]:null);
                data.setJudulPeraturanEn(obj[1]!=null?(String)obj[1]:null);
                data.setDueDate(obj[3]!=null?(String)obj[3]:null);
                //data.setStatus(obj[2]!=null?(String)obj[2]:null);
                //data.setSocializationId(obj[4]!=null?((java.math.BigInteger)obj[4]).longValue():null);
                data.setSocializationId(obj[4]!=null?(MathUtil.returnIdObjectToLong(obj[4])):null);
                //data.setStatusList(regulationSocializationDao.getDataConfirmStatusBySocializationId(data.getSocializationId()));
                data.setStatusIn(obj[5]!=null?(String)obj[5]:null);
                data.setStatusEn(obj[6]!=null?(String)obj[6]:null);
                data.setPicFollowupId(obj[7]!=null?(MathUtil.returnIdObjectToLong(obj[7])):null);
                data.setPicName1(obj[8]!=null?(String)obj[8]:null);
                data.setPicName2(obj[9]!=null?(String)obj[9]:null);
                data.setPicName3(obj[10]!=null?(String)obj[10]:null);
                data.setComplianceNote(obj[11]!=null?(String)obj[11]:null);
                
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
