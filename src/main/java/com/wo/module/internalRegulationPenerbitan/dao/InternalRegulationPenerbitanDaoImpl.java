package com.wo.module.internalRegulationPenerbitan.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.faces.context.FacesContext;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.SortOrder;
import org.springframework.stereotype.Repository;

import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.common.util.MathUtil;
import com.wo.module.internalRegulationPenerbitan.constant.InternalRegulationPenerbitanConstants;
import com.wo.module.internalRegulationPenerbitan.model.InternalRegulationPenerbitan;
import com.wo.module.internalRegulationPenerbitan.vo.InternalRegulationPenerbitanVo;
import com.wo.module.user.model.User;


@Repository("internalRegulationPenerbitanDao")
public class InternalRegulationPenerbitanDaoImpl extends GenericDAOHibernate<InternalRegulationPenerbitan, Long> 
    implements InternalRegulationPenerbitanDao {
	
	@SuppressWarnings({ "rawtypes", "unused" })
    private StringBuilder getQueryWhereString(StringBuilder sb, List<? extends SearchObject> searchCriteria) {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValue()!=null?searchVal.getSearchValueAsString():"";
				if (!StringUtils.isBlank(val)) {					
					if (StringUtils.equals(InternalRegulationPenerbitanConstants.SEARCH_BY_REGULATION_TITLE, col)) {
						sb.append(" AND UPPER(irg.IRG_TITLE) LIKE UPPER('%" + val + "%') ");
					}
					
					if (StringUtils.equals(InternalRegulationPenerbitanConstants.SEARCH_BY_REFERENCE_NO, col)) {
						sb.append(" AND UPPER(irg.REFERENCE_NO) LIKE UPPER('%" + val + "%') ");
					}
					
					if (StringUtils.equals(InternalRegulationPenerbitanConstants.SEARCH_BY_REGULATION_TYPE, col)) {
						sb.append(" AND irg.REGULATION_TYPE = " + val + " ");
					}
					
					if (StringUtils.equals(InternalRegulationPenerbitanConstants.SEARCH_BY_WORK_UNIT_TPG, col)) {
						sb.append(" AND irg.WORK_UNIT_TPG = " + val + " ");
					}					
					
					if (StringUtils.equals(InternalRegulationPenerbitanConstants.SEARCH_BY_REGULATION_IN_DATE_START, col)) {
						sb.append(" AND TRUNC(irg.REGULATION_IN_DATE) >= TO_DATE('"+val+"','yyyy-MM-dd') ");
					}
					
					if (StringUtils.equals(InternalRegulationPenerbitanConstants.SEARCH_BY_REGULATION_IN_DATE_END, col)) {
						sb.append(" AND TRUNC(irg.REGULATION_IN_DATE) <= TO_DATE('"+val+"','yyyy-MM-dd') ");
					}
					
					if (StringUtils.equals(InternalRegulationPenerbitanConstants.SEARCH_BY_REGULATION_STATUS, col)) {
						sb.append(" AND irg.REGULATION_STATUS = " + val + " ");
					}
					
					if (StringUtils.equals(InternalRegulationPenerbitanConstants.SEARCH_BY_PROCESS_STATUS, col)) {
						sb.append(" AND irg.PROCESS_STATUS = " + val + " ");
					}
					
					if (StringUtils.equals(InternalRegulationPenerbitanConstants.SEARCH_BY_PIC_IRG_NIK, col)) {
						sb.append(" AND irg.CREATED_BY = '"+ val +"' ");
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
		sb.append("SELECT COUNT(1) ");
		sb.append("  FROM WO_TRC_IRG irg");
		sb.append("       INNER JOIN WO_TRC_IRG_PIC_IRG picIrg ON irg.IRG_ID = picIrg.IRG_ID ");
	    sb.append("       INNER JOIN WO_MST_USER userIrg ON picIrg.USER_ID_1 = userIrg.USER_ID ");
    	sb.append("       INNER JOIN WO_MST_PARAMETER_DTL dtl ON irg.REGULATION_TYPE = dtl.PARAMETER_DTL_ID");
    	sb.append("       INNER JOIN WO_MST_DIVISION div ON irg.WORK_UNIT_TPG = div.DIVISION_ID");
    	sb.append("       INNER JOIN WO_MST_PARAMETER_DTL dtl2 ON irg.REGULATION_STATUS = dtl2.PARAMETER_DTL_ID");
    	sb.append("       LEFT JOIN WO_MST_PARAMETER_DTL dtl3 ON irg.PROCESS_STATUS = dtl3.PARAMETER_DTL_ID ");
    	sb.append(" WHERE 1=1 ");
    	sb.append("       AND irg.ENABLED_FLAG = 'Y'  ");
       
        sb = getQueryWhereString(sb, searchCriteria);
        
        Query result = getSession().createSQLQuery(sb.toString());
       
        
        return (Number) result.getSingleResult();
    }
    
    @SuppressWarnings("rawtypes")
    public List<InternalRegulationPenerbitanVo> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize, String sortField, SortOrder sortOrder) 
            throws Exception {
        
        List<InternalRegulationPenerbitanVo> voList = searchDataCriteria(searchCriteria, first, pageSize);

        return voList;
    }
    
    @SuppressWarnings("rawtypes")
    private List<InternalRegulationPenerbitanVo> searchDataCriteria(List<? extends SearchObject> searchCriteria, int first, int pageSize) {
        
    	StringBuilder sb = new StringBuilder();
    	sb.append("SELECT irg.IRG_ID, irg.REFERENCE_NO, irg.IRG_TITLE, irg.REGULATION_IN_DATE, ");
    	sb.append("       TO_CHAR(irg.REGULATION_IN_DATE, 'dd-Mon-yyyy') REGULATION_IN_DATE_STR, ");
    	sb.append("       irg.REGULATION_TYPE, dtl.NAME_IN REGULATION_TYPE_NAME, irg.WORK_UNIT_TPG, ");
    	sb.append("       div.DIVISION_NAME WORK_UNIT_TPG_NAME, irg.REGULATION_STATUS, dtl2.NAME_IN REGULATION_STATUS_NAME, ");
    	sb.append("       picIrg.USER_ID_1, userIrg.NIK, userIrg.NAME ");
    	sb.append("       ,dtl3.PARAMETER_DTL_CODE ");
    	sb.append("       ,dtl3.NAME_IN PROCESS_STATUS_NAME_IN ");
    	sb.append("       ,dtl3.NAME_EN PROCESS_STATUS_NAME_EN ");
    	sb.append("");	
        sb.append("  FROM WO_TRC_IRG irg ");
        sb.append("       INNER JOIN WO_TRC_IRG_PIC_IRG picIrg ON irg.IRG_ID = picIrg.IRG_ID ");
        sb.append("       INNER JOIN WO_MST_USER userIrg ON picIrg.USER_ID_1 = userIrg.USER_ID ");
    	sb.append("       INNER JOIN WO_MST_PARAMETER_DTL dtl ON irg.REGULATION_TYPE = dtl.PARAMETER_DTL_ID ");
    	sb.append("       INNER JOIN WO_MST_DIVISION div ON irg.WORK_UNIT_TPG = div.DIVISION_ID ");
    	sb.append("       INNER JOIN WO_MST_PARAMETER_DTL dtl2 ON irg.REGULATION_STATUS = dtl2.PARAMETER_DTL_ID ");
    	sb.append("       LEFT JOIN WO_MST_PARAMETER_DTL dtl3 ON irg.PROCESS_STATUS = dtl3.PARAMETER_DTL_ID ");
    	sb.append(" WHERE 1=1 ");
    	sb.append("       AND irg.ENABLED_FLAG = 'Y'  ");

        
        sb = getQueryWhereString(sb, searchCriteria);
        
        sb.append(" ORDER BY irg.IRG_ID DESC ");
        
        Query result = getSession().createSQLQuery(sb.toString());
        result.setFirstResult(first);
        result.setMaxResults(pageSize);
        
        List resultList = result.getResultList();
        
        List<InternalRegulationPenerbitanVo> dataVoList = new ArrayList<InternalRegulationPenerbitanVo>();
        
        if(resultList!=null) {
            for(int i=0; i<resultList.size(); i++) {
                Object[] obj = (Object[]) resultList.get(i);
                InternalRegulationPenerbitanVo data = new InternalRegulationPenerbitanVo();
                data.setIrgId(obj[0]!=null?(MathUtil.returnIdObjectToLong(obj[0])):null);
                data.setReferenceNo(obj[1]!=null?(String)obj[1]:null);
                data.setIrgTitle(obj[2]!=null?(String)obj[2]:null);
                data.setRegulationInDate(obj[3]!=null?(Date)obj[3]:null);
                data.setRegulationInDateStr(obj[4]!=null?(String)obj[4]:null);                
                data.setRegulationTypeId(obj[5]!=null?(MathUtil.returnIdObjectToLong(obj[5])):null);
                data.setRegulationTypeName(obj[6]!=null?(String)obj[6]:null);                
                data.setWorkUnitTpgId(obj[7]!=null?(MathUtil.returnIdObjectToLong(obj[7])):null);
                data.setWorkUnitTpgName(obj[8]!=null?(String)obj[8]:null);                
                data.setRegulationStatusId(obj[9]!=null?(MathUtil.returnIdObjectToLong(obj[9])):null);
                data.setRegulationStatusName(obj[10]!=null?(String)obj[10]:null); 
                data.setUserId1(obj[11]!=null?(MathUtil.returnIdObjectToLong(obj[11])):null);
                data.setUserNik1(obj[12]!=null?(String)obj[12]:null); 
                data.setUserName1(obj[13]!=null?(String)obj[13]:null);
                data.setProcessStatus(obj[14]!=null?(String)obj[14]:null);
                data.setProcessStatusNameIn(obj[15]!=null?(String)obj[15]:null);
                data.setProcessStatusNameEn(obj[16]!=null?(String)obj[16]:null);
                
                dataVoList.add(data);
            }
        }

        result.setFirstResult(first);
        result.setMaxResults(pageSize);
        
        return dataVoList;
    }
        
   
	@SuppressWarnings("rawtypes")
	@Override
	public List<String> getDataRegulationTitle(String regulationTitle) {
		StringBuilder sb = new StringBuilder();
    	sb.append("SELECT irg.REFERENCE_NO, irg.IRG_TITLE ");
        sb.append("  FROM WO_TRC_IRG irg ");
        sb.append(" WHERE 1=1 ");
        sb.append("       AND ENABLED_FLAG = 'Y' ");
        sb.append(" 	  AND LOWER(irg.IRG_TITLE) LIKE LOWER('%" + regulationTitle + "%') ");
        
 		Query result = getSession().createSQLQuery(sb.toString());
 		List resultList = result.getResultList();
 		
 		List<String> dataList = new ArrayList<String>();
 		if(resultList !=null) {
 			for(int i=0; i<resultList.size(); i++) {
 				Object[] obj = (Object[])resultList.get(i);
 				dataList.add((String)obj[1]);
 			}
 		}
 		 		
 		return dataList; 		
	}
	
	@Override
	public Boolean isCheckDataIrgByTitleAndNo(String regulationNo, String irgTitle, Long irgId) {
		StringBuilder sb = new StringBuilder();
		
		sb.append(" SELECT count(1) ");
		sb.append(" FROM WO_TRC_IRG wti ");
		sb.append(" WHERE 1 = 1 ");
		sb.append(" 	AND wti.ENABLED_FLAG = 'Y' ");
		sb.append(" 	AND wti.REGULATION_NO = :regulationNo ");
		sb.append(" 	AND wti.IRG_TITLE = :irgTitle ");
		
		if (irgId != null) {
			sb.append(" 	AND wti.IRG_ID <> :irgId ");
		}
		
		Query query = getSession().createSQLQuery(sb.toString());
		query.setParameter("regulationNo", regulationNo);
		query.setParameter("irgTitle", irgTitle);
		
		if (irgId != null) {
			query.setParameter("irgId", irgId);
		}
		
		Number result = (Number) query.getSingleResult();
		
		if (result == null) {
			return true;
		} else if (result.longValue() == 0) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public List<User> getPicsIrg() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("SELECT DISTINCT "
				+ "u.USER_ID, "
				+ "u.NIK, "
				+ "u.NAME ");
		sb.append("FROM WO_MST_USER u ");
		sb.append("INNER JOIN WO_TRC_IRG i ON u.NIK = i.CREATED_BY ");
		sb.append("WHERE i.ENABLED_FLAG = 'Y'");
		Query result = getSession().createSQLQuery(sb.toString());
		List resultList = result.getResultList();
		
		List<User> dataList = new ArrayList<User>();
		
		if(resultList !=null) {
			for(int i=0; i<resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
				
				User user = new User();
				
				user.setUserId(Long.valueOf(obj[0].toString()) );
				user.setNik(obj[1].toString());
				user.setName(obj[2].toString());
				
				dataList.add(user);
			}
		}
		
		return dataList;
	}
    
	@SuppressWarnings("rawtypes")
	public List<String> getEmailTargetDateListByIrgPenerbitId(Long irgId){
		List<String> targetDateEmailList = new ArrayList<String>();
		
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT DISTINCT TO_CHAR(tpke.EMAIL_DATE , 'dd-Mon-yyyy') REMINDER_DATE, '' AS EMPTY_STRING ");
		sb.append("FROM WO_TRC_IRG_PIC_TPK tpk ");
		sb.append("INNER JOIN WO_TRC_IRG_PIC_TPK_EMAIL tpke ON tpk.IRG_PIC_ID = tpke.IRG_PIC_ID ");
		sb.append("WHERE 1=1 ");
		sb.append("AND tpk.IRG_ID = "+irgId.toString()+" ");
		
		Query result = getSession().createSQLQuery(sb.toString());
		List resultList = result.getResultList();
 		
 		if(resultList != null) {
 			for(int i=0; i<resultList.size();i++) {
 				Object[] obj = (Object[]) resultList.get(i);
 				targetDateEmailList.add(obj[0].toString());
 			}
 		}
		
		return targetDateEmailList;
	}
	
}
