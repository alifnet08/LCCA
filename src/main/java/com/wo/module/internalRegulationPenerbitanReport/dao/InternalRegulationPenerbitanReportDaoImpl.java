package com.wo.module.internalRegulationPenerbitanReport.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.faces.context.FacesContext;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.SortOrder;
import org.springframework.stereotype.Repository;

import com.wo.module.common.constant.Constants;
import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.common.util.MathUtil;
import com.wo.module.internalRegulationPenerbitan.model.InternalRegulationPenerbitan;
import com.wo.module.internalRegulationPenerbitanReport.constant.InternalRegulationPenerbitanReportConstants;
import com.wo.module.internalRegulationPenerbitanReport.model.InternalRegulationPenerbitanReport;
import com.wo.module.internalRegulationPenerbitanReport.vo.InternalRegulationPenerbitanPicIrgReportVo;
import com.wo.module.internalRegulationPenerbitanReport.vo.InternalRegulationPenerbitanPicTpgReportVo;
import com.wo.module.internalRegulationPenerbitanReport.vo.InternalRegulationPenerbitanPicTpkReportVo;
import com.wo.module.internalRegulationPenerbitanReport.vo.InternalRegulationPenerbitanReportVo;


@Repository("internalRegulationPenerbitanReportDao")
public class InternalRegulationPenerbitanReportDaoImpl extends GenericDAOHibernate<InternalRegulationPenerbitan, Long> 
    implements InternalRegulationPenerbitanReportDao {
	
	@SuppressWarnings({ "rawtypes", "unused" })
    private StringBuilder getQueryWhereString(StringBuilder sb, List<? extends SearchObject> searchCriteria) {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValue()!=null?searchVal.getSearchValueAsString():"";
				if (!StringUtils.isBlank(val)) {	
					if (StringUtils.equals(InternalRegulationPenerbitanReportConstants.SEARCH_BY_REFERENCE_NO, col)) {
						sb.append(" AND UPPER(irg.REFERENCE_NO) LIKE UPPER(:referenceNo) ");
					}
					
					if (StringUtils.equals(InternalRegulationPenerbitanReportConstants.SEARCH_BY_REGULATION_TITLE, col)) {
						sb.append(" AND UPPER(irg.IRG_TITLE) LIKE UPPER(:irgTitle) ");
					}
					
					if (StringUtils.equals(InternalRegulationPenerbitanReportConstants.SEARCH_BY_REGULATION_NO, col)) {
						sb.append(" AND UPPER(irg.REGULATION_NO) LIKE UPPER(:regulationNo) ");
					}
					
					if (StringUtils.equals(InternalRegulationPenerbitanReportConstants.SEARCH_BY_DIRECTORATE_TPG, col)) {
						sb.append(" AND UPPER(irg.DIRECTORATE_TPG) = UPPER(:directorateTpg) ");
					}
					
					if (StringUtils.equals(InternalRegulationPenerbitanReportConstants.SEARCH_BY_REGULATION_TYPE, col)) {
						sb.append(" AND dtl.PARAMETER_DTL_CODE = :regulationType ");
					}
					
					if (StringUtils.equals(InternalRegulationPenerbitanReportConstants.SEARCH_BY_WORK_UNIT_TPG, col)) {
						sb.append(" AND irg.WORK_UNIT_TPG = :workUnitTpg ");
					}
					
					if (StringUtils.equals(InternalRegulationPenerbitanReportConstants.SEARCH_BY_REGULATION_STATUS, col)) {
						sb.append(" AND dtl2.PARAMETER_DTL_CODE = :regulationStatus ");
					}			
					
					if (StringUtils.equals(InternalRegulationPenerbitanReportConstants.SEARCH_BY_PIC_IRG, col)) {
						sb.append(" AND user1.NIK = :picIrg ");;
					}	
				}
			}
		}
		
		return sb;
    }
	
	@SuppressWarnings("rawtypes")
	private void getQuerySetString(Query query, List<? extends SearchObject> searchCriteria, String dataType) {
		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString();
				
				if (!StringUtils.isBlank(val)) {
					if (StringUtils.equals(InternalRegulationPenerbitanReportConstants.SEARCH_BY_REFERENCE_NO, col)) {
						query.setParameter("referenceNo", "%" + val + "%");
					}
					
					if (StringUtils.equals(InternalRegulationPenerbitanReportConstants.SEARCH_BY_REGULATION_TITLE, col)) {
						query.setParameter("irgTitle", "%" + val + "%");
					}
					
					if (StringUtils.equals(InternalRegulationPenerbitanReportConstants.SEARCH_BY_REGULATION_NO, col)) {
						query.setParameter("regulationNo", "%" + val + "%");
					}
					
					if (StringUtils.equals(InternalRegulationPenerbitanReportConstants.SEARCH_BY_DIRECTORATE_TPG, col)) {
						query.setParameter("directorateTpg", val);
					}
					
					if (StringUtils.equals(InternalRegulationPenerbitanReportConstants.SEARCH_BY_REGULATION_TYPE, col)) {
						query.setParameter("regulationType", val);
					}
					
					if (StringUtils.equals(InternalRegulationPenerbitanReportConstants.SEARCH_BY_WORK_UNIT_TPG, col)) {
						query.setParameter("workUnitTpg", val);
					}					
					
					if (StringUtils.equals(InternalRegulationPenerbitanReportConstants.SEARCH_BY_REGULATION_STATUS, col)) {
						query.setParameter("regulationStatus", val);
					}	
					
					if (StringUtils.equals(InternalRegulationPenerbitanReportConstants.SEARCH_BY_PIC_IRG, col)) {
						query.setParameter("picIrg", val);
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
		sb.append("SELECT COUNT(1) ");
		sb.append("  FROM WO_TRC_IRG irg");
		sb.append("       INNER JOIN WO_TRC_IRG_PIC_IRG picIrg ON irg.IRG_ID = picIrg.IRG_ID ");
    	sb.append("       INNER JOIN WO_MST_USER user1 ON picIrg.USER_ID_1 = user1.USER_ID ");
    	sb.append("       INNER JOIN WO_MST_PARAMETER_DTL dtl");
    	sb.append("          ON irg.REGULATION_TYPE = dtl.PARAMETER_DTL_ID");
    	sb.append("       INNER JOIN WO_MST_DIVISION div ON irg.WORK_UNIT_TPG = div.DIVISION_ID");
    	sb.append("       LEFT JOIN WO_MST_PARAMETER_DTL dtl2");
    	sb.append("          ON irg.REGULATION_STATUS = dtl2.PARAMETER_DTL_ID");
    	sb.append("       LEFT JOIN WO_MST_PARAMETER_DTL dtl3 ON irg.PROCESS_STATUS = dtl3.PARAMETER_DTL_ID ");
    	sb.append(" WHERE 1 = 1 ");
    	sb.append("		  AND irg.ENABLED_FLAG = 'Y' ");
       
        sb = getQueryWhereString(sb, searchCriteria);
        
        Query result = getSession().createSQLQuery(sb.toString());
        getQuerySetString(result, searchCriteria, InternalRegulationPenerbitanReportConstants.SEARCH_COUNT);
       
        
        return (Number) result.getSingleResult();
    }
    
    @SuppressWarnings("rawtypes")
    public List<InternalRegulationPenerbitanReportVo> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize, String sortField, SortOrder sortOrder) 
            throws Exception {
        
        List<InternalRegulationPenerbitanReportVo> voList = searchDataCriteria(searchCriteria, first, pageSize);

        return voList;
    }
    
    @SuppressWarnings("rawtypes")
    private List<InternalRegulationPenerbitanReportVo> searchDataCriteria(List<? extends SearchObject> searchCriteria, int first, int pageSize) {
        
    	StringBuilder sb = new StringBuilder();
    	sb.append("SELECT irg.IRG_ID, irg.REFERENCE_NO, irg.IRG_TITLE, irg.REGULATION_NO, ");
    	sb.append("       irg.DIRECTORATE_TPG, irg.REGULATION_TYPE, dtl.NAME_IN REGULATION_TYPE_NAME, ");
    	sb.append("       irg.WORK_UNIT_TPG, div.DIVISION_NAME WORK_UNIT_TPG_NAME, ");
    	sb.append("       irg.REGULATION_STATUS, dtl2.NAME_IN REGULATION_STATUS_NAME, user1.NAME AS PIC_IRG, ");
    	sb.append("       dtl3.NAME_IN PROCESS_STATUS_NAME ");
    	sb.append("  FROM WO_TRC_IRG irg");
    	sb.append("       INNER JOIN WO_TRC_IRG_PIC_IRG picIrg ON irg.IRG_ID = picIrg.IRG_ID ");
    	sb.append("       INNER JOIN WO_MST_USER user1 ON picIrg.USER_ID_1 = user1.USER_ID ");
    	sb.append("       INNER JOIN WO_MST_PARAMETER_DTL dtl");
    	sb.append("          ON irg.REGULATION_TYPE = dtl.PARAMETER_DTL_ID");
    	sb.append("       INNER JOIN WO_MST_DIVISION div ON irg.WORK_UNIT_TPG = div.DIVISION_ID");
    	sb.append("       LEFT JOIN WO_MST_PARAMETER_DTL dtl2");
    	sb.append("          ON irg.REGULATION_STATUS = dtl2.PARAMETER_DTL_ID");
    	sb.append("       LEFT JOIN WO_MST_PARAMETER_DTL dtl3 ON irg.PROCESS_STATUS = dtl3.PARAMETER_DTL_ID ");
    	sb.append(" WHERE 1 = 1 ");
    	sb.append("		  AND irg.ENABLED_FLAG = 'Y' ");
        
        sb = getQueryWhereString(sb, searchCriteria);
        
        sb.append(" ORDER BY irg.IRG_ID DESC ");
        
        Query result = getSession().createSQLQuery(sb.toString());
        getQuerySetString(result, searchCriteria, InternalRegulationPenerbitanReportConstants.SEARCH_DATA);
        
        result.setFirstResult(first);
        result.setMaxResults(pageSize);
        
        List resultList = result.getResultList();
        
        List<InternalRegulationPenerbitanReportVo> dataVoList = new ArrayList<InternalRegulationPenerbitanReportVo>();
        
        if(resultList!=null) {
            for(int i=0; i<resultList.size(); i++) {
                Object[] obj = (Object[]) resultList.get(i);
                InternalRegulationPenerbitanReportVo data = new InternalRegulationPenerbitanReportVo();
                data.setIrgId(obj[0]!=null?(MathUtil.returnIdObjectToLong(obj[0])):null);
                data.setReferenceNo(obj[1]!=null?(String)obj[1]:null);
                data.setIrgTitle(obj[2]!=null?(String)obj[2]:null);
                data.setRegulationNo(obj[3]!=null?(String)obj[3]:null);
                data.setDirectorateTpg(obj[4]!=null?(String)obj[4]:null);                
                data.setRegulationTypeId(obj[5]!=null?(MathUtil.returnIdObjectToLong(obj[5])):null);                
                data.setRegulationTypeName(obj[6]!=null?(String)obj[6]:null);
                data.setWorkUnitTpgId(obj[7]!=null?(MathUtil.returnIdObjectToLong(obj[7])):null);                
                data.setWorkUnitTpgName(obj[8]!=null?(String)obj[8]:null);                
                data.setRegulationStatusId(obj[9]!=null?(MathUtil.returnIdObjectToLong(obj[9])):null);                
                data.setRegulationStatusName(obj[10]!=null?(String)obj[10]:null);
                data.setPicIrg(obj[11]!=null?(String)obj[11]:null);
                data.setProcessStatusName(obj[12]!=null?(String)obj[12]:null);
                dataVoList.add(data);
            }
        }

        result.setFirstResult(first);
        result.setMaxResults(pageSize);
        
        return dataVoList;
    }
    
    @SuppressWarnings("rawtypes")
	public List<InternalRegulationPenerbitanReportVo> getAllIrgDataHeader(InternalRegulationPenerbitanReport irgDataReport) {
        
    	StringBuilder sb = new StringBuilder();
    	sb.append("SELECT irg.IRG_ID, irg.REFERENCE_NO, irg.IRG_TITLE, ");
    	sb.append("       irg.REGULATION_TYPE, dtl1.NAME_IN REGULATION_TYPE_NAME, ");
    	sb.append("       irg.REGULATION_IN_DATE, TO_CHAR(REGULATION_IN_DATE, 'DD-Mon-YYYY') REGULATION_IN_DATE_STR, ");
    	sb.append("       irg.REGULATION_STATUS, dtl2.NAME_IN REGULATION_STATUS_NAME, ");
    	sb.append("       irg.WORK_UNIT_TPG, div.DIVISION_NAME WORK_UNIT_TPG_NAME, ");
    	sb.append("       irg.DIRECTORATE_TPG, irg.FINAL_IRG_DATE, TO_CHAR(irg.FINAL_IRG_DATE, 'DD-Mon-YYYY') FINAL_IRG_DATE_STR, ");
    	sb.append("       irg.APPROVAL_SPV_DATE, TO_CHAR(irg.APPROVAL_SPV_DATE, 'DD-Mon-YYYY') APPROVAL_SPV_DATE_STR, ");
    	sb.append("       irg.APPROVAL_PUK_DATE, TO_CHAR(irg.APPROVAL_PUK_DATE, 'DD-Mon-YYYY') APPROVAL_PUK_DATE_STR, ");
    	sb.append("       irg.SIGN_OFF_DATE, TO_CHAR(irg.SIGN_OFF_DATE, 'DD-Mon-YYYY') SIGN_OFF_DATE_STR, ");
    	sb.append("       irg.PROCESS_STATUS, dtl3.NAME_IN PROCESS_STATUS_NAME, irg.REGULATION_NO, ");
    	sb.append("       irg.EFFECTIVE_DATE, TO_CHAR(irg.EFFECTIVE_DATE, 'DD-Mon-YYYY') EFFECTIVE_DATE_STR, ");
    	sb.append("       irg.EMAIL_BLAST_DATE, TO_CHAR(irg.EMAIL_BLAST_DATE, 'DD-Mon-YYYY') EMAIL_BLAST_DATE_STR, ");
    	sb.append("       irg.UPLOAD_BLAST_DATE, TO_CHAR(irg.UPLOAD_BLAST_DATE, 'DD-Mon-YYYY') UPLOAD_BLAST_DATE_STR ,");
    	sb.append("       irg.REG_OBSOLETE_TYPE, dtl4.NAME_IN REG_OBSOLETE_TYPE_NAME, irg.OBSOLETE_INFO, ");
    	sb.append("       user1.NIK PIC_NIK1, user1.NAME PIC_NAME1, user2.NIK PIC_NIK2, user2.NAME PIC_NAME2, ");
    	sb.append("       (SELECT LISTAGG(EMAIL_GROUP, ', ') ");
    	sb.append("               WITHIN GROUP (ORDER BY EMAIL_GROUP) AS EMAIL_GROUPS ");
    	sb.append("          FROM WO_TRC_IRG_EMAIL_GROUP ");
    	sb.append("         WHERE IRG_ID = irg.IRG_ID) EMAIL_GROUP ");
    	sb.append("  FROM WO_TRC_IRG irg");
    	sb.append("       INNER JOIN WO_TRC_IRG_PIC_IRG picIrg ON irg.IRG_ID = picIrg.IRG_ID ");
    	sb.append("       INNER JOIN WO_MST_USER user1 ON picIrg.USER_ID_1 = user1.USER_ID ");
    	sb.append("       INNER JOIN WO_MST_USER user2 ON picIrg.USER_ID_2 = user2.USER_ID ");
    	sb.append("       INNER JOIN WO_MST_DIVISION div ON irg.WORK_UNIT_TPG = div.DIVISION_ID ");
    	sb.append("       INNER JOIN WO_MST_PARAMETER_DTL dtl1 ON irg.REGULATION_TYPE = dtl1.PARAMETER_DTL_ID ");
    	sb.append("       LEFT JOIN WO_MST_PARAMETER_DTL dtl2 ON irg.REGULATION_STATUS = dtl2.PARAMETER_DTL_ID ");
    	sb.append("       LEFT JOIN WO_MST_PARAMETER_DTL dtl3 ON irg.PROCESS_STATUS = dtl3.PARAMETER_DTL_ID ");
    	sb.append("       LEFT JOIN WO_MST_PARAMETER_DTL dtl4 ON irg.REG_OBSOLETE_TYPE = dtl4.PARAMETER_DTL_ID ");
    	sb.append(" WHERE 1 = 1 ");
    	sb.append("		  AND irg.ENABLED_FLAG = 'Y' ");
    	
    	if(irgDataReport !=null) {
    		if(irgDataReport.getReferenceNo() !=null && 
    				!irgDataReport.getReferenceNo().equals(InternalRegulationPenerbitanReportConstants.STRING_EMPTY)) {
    		   sb.append(" AND irg.REFERENCE_NO LIKE '%"+irgDataReport.getReferenceNo()+"%' ");
    		}
    		
    		if(irgDataReport.getDirectorateTpg() !=null && 
    				!irgDataReport.getDirectorateTpg().equals(InternalRegulationPenerbitanReportConstants.STRING_EMPTY)) {
    		   sb.append(" AND irg.DIRECTORATE_TPG = '"+irgDataReport.getDirectorateTpg()+"' ");
    		}
    		
    		if(irgDataReport.getWorkUnitTpgCode() !=null && 
    				!irgDataReport.getWorkUnitTpgCode().equals(InternalRegulationPenerbitanReportConstants.STRING_EMPTY)) {
    		   sb.append(" AND irg.WORK_UNIT_TPG = " + irgDataReport.getWorkUnitTpgCode());
    		}
    		
    		if(irgDataReport.getIrgTitle() !=null && 
    				!irgDataReport.getIrgTitle().equals(InternalRegulationPenerbitanReportConstants.STRING_EMPTY)) {
    		   sb.append(" AND irg.IRG_TITLE LIKE '%" + irgDataReport.getIrgTitle()+"%' ");
    		}
    		
    		if(irgDataReport.getRegulationStatusCode() !=null && 
    				!irgDataReport.getRegulationStatusCode().equals(InternalRegulationPenerbitanReportConstants.STRING_EMPTY)) {
    		   sb.append(" AND dtl2.PARAMETER_DTL_CODE = '" + irgDataReport.getRegulationStatusCode() + "'  ");
    		}
    		
    		if(irgDataReport.getRegulationNo() !=null && 
    				!irgDataReport.getRegulationNo().equals(InternalRegulationPenerbitanReportConstants.STRING_EMPTY)) {
    		   sb.append(" AND irg.REGULATION_NO LIKE '%" + irgDataReport.getRegulationNo() + "%' ");
    		}
    		
    		if(irgDataReport.getRegulationTypeCode() !=null && 
    				!irgDataReport.getRegulationTypeCode().equals(InternalRegulationPenerbitanReportConstants.STRING_EMPTY)) {
    		   sb.append(" AND dtl1.PARAMETER_DTL_CODE = '" + irgDataReport.getRegulationTypeCode() + "' ");
    		}    		
    		
    		if(irgDataReport.getPicIrg() !=null && 
    				!irgDataReport.getPicIrg().equals(InternalRegulationPenerbitanReportConstants.STRING_EMPTY)) {
    		   sb.append(" AND user1.NIK = '" + irgDataReport.getPicIrg() + "' ");
    		}
    		
    	}
                     
        sb.append(" ORDER BY irg.IRG_ID DESC ");
        
        Query result = getSession().createSQLQuery(sb.toString());
            
        List resultList = result.getResultList();
        
        List<InternalRegulationPenerbitanReportVo> dataVoList = new ArrayList<InternalRegulationPenerbitanReportVo>();
        
        if(resultList!=null) {
            for(int i=0; i<resultList.size(); i++) {
                Object[] obj = (Object[]) resultList.get(i);
                InternalRegulationPenerbitanReportVo data = new InternalRegulationPenerbitanReportVo();
                data.setIrgId(obj[0]!=null?(MathUtil.returnIdObjectToLong(obj[0])):null);
                data.setReferenceNo(obj[1]!=null?(String)obj[1]:null);
                data.setIrgTitle(obj[2]!=null?(String)obj[2]:null);
                data.setRegulationTypeId(obj[3]!=null?(MathUtil.returnIdObjectToLong(obj[3])):null);                
                data.setRegulationTypeName(obj[4]!=null?(String)obj[4]:null);                    
                data.setRegulationInDate(obj[5]!=null?(Date)obj[5]:null); 
                data.setRegulationInDateStr(obj[6]!=null?(String)obj[6]:null); 
                data.setRegulationStatusId(obj[7]!=null?(MathUtil.returnIdObjectToLong(obj[7])):null);         
                data.setRegulationStatusName(obj[8]!=null?(String)obj[8]:null); 
                data.setWorkUnitTpgId(obj[9]!=null?(MathUtil.returnIdObjectToLong(obj[9])):null);                
                data.setWorkUnitTpgName(obj[10]!=null?(String)obj[10]:null);
                data.setDirectorateTpg(obj[11]!=null?(String)obj[11]:null);                
                data.setFinalIrgDate(obj[12]!=null?(Date)obj[12]:null); 
                data.setFinalIrgDateStr(obj[13]!=null?(String)obj[13]:null);                 
                data.setApprovalSpvDate(obj[14]!=null?(Date)obj[14]:null); 
                data.setApprovalSpvDateStr(obj[15]!=null?(String)obj[15]:null);                 
                data.setApprovalPukDate(obj[16]!=null?(Date)obj[16]:null); 
                data.setApprovalPukDateStr(obj[17]!=null?(String)obj[17]:null);                
                data.setSignOffDate(obj[18]!=null?(Date)obj[18]:null); 
                data.setSignOffDateStr(obj[19]!=null?(String)obj[19]:null);                
                data.setProcessStatusId(obj[20]!=null?(MathUtil.returnIdObjectToLong(obj[20])):null);
                data.setProcessStatusName(obj[21]!=null?(String)obj[21]:null);                
                data.setRegulationNo(obj[22]!=null?(String)obj[22]:null);
                data.setEffectiveDate(obj[23]!=null?(Date)obj[23]:null); 
                data.setEffectiveDateStr(obj[24]!=null?(String)obj[24]:null);   
                data.setEmailBlastDate(obj[25]!=null?(Date)obj[25]:null); 
                data.setEmailBlastDateStr(obj[26]!=null?(String)obj[26]:null);
                data.setUploadBlastDate(obj[27]!=null?(Date)obj[27]:null); 
                data.setUploadBlastDateStr(obj[28]!=null?(String)obj[28]:null); 
                data.setRegObsoleteTypeId(obj[29]!=null?(MathUtil.returnIdObjectToLong(obj[29])):null);
                data.setRegObsoleteTypeName(obj[30]!=null?(String)obj[30]:null); 
                data.setObsoleteInfo(obj[31]!=null?(String)obj[31]:null); 
                data.setPicIrgNik1(obj[32]!=null?(String)obj[32]:null);
                data.setPicIrgName1(obj[33]!=null?(String)obj[33]:null);
                data.setPicIrgNik2(obj[34]!=null?(String)obj[34]:null);
                data.setPicIrgName2(obj[35]!=null?(String)obj[35]:null);
                data.setEmailGroupTpk(obj[36]!=null?(String)obj[36]:null);
                
                data.setPicTpgReportList(getDataPicTpg(data.getIrgId()));
                data.setPicTpkReportList(getDataPicTpk(data.getIrgId()));
                                
                dataVoList.add(data);
            }
        }
        
        return dataVoList;
    }   
    
    @SuppressWarnings("rawtypes")
   	public List<InternalRegulationPenerbitanPicTpgReportVo> getDataPicTpg(Long irgId) {           
	       StringBuilder sb = new StringBuilder();
	       sb.append(" SELECT tpg.IRG_PIC_ID, tpg.IRG_ID, tpg.DIVISION_ID, div.DIVISION_NAME, "); 
	       sb.append("  	  user1.NIK picNikTpk1, user1.NAME picNameTpk1, ");
	       sb.append(" 	      user2.NIK picNikTpk2, user2.NAME picNameTpk2, "); 
	       sb.append("        user3.NIK picNikTpk3, user3.NAME picNameTpk3, ");
	       sb.append("        tpg.TARGET_DATE, TO_CHAR(tpg.TARGET_DATE, 'DD-Mon-YYYY') TARGET_DATE_STR, "); 
	       sb.append("        tpg.NOTE ");
	       sb.append("   FROM WO_TRC_IRG_PIC_TPG tpg "); 
	       sb.append("        INNER JOIN WO_MST_DIVISION div ON tpg.DIVISION_ID = div.DIVISION_ID "); 
	       sb.append("        INNER JOIN WO_MST_USER user1 ON tpg.USER_ID_1 = user1.USER_ID "); 
	       sb.append("        INNER JOIN WO_MST_USER user2 ON tpg.USER_ID_2 = user2.USER_ID "); 
	       sb.append("        LEFT JOIN WO_MST_USER user3 ON tpg.USER_ID_3 = user3.USER_ID "); 
	       sb.append("  WHERE tpg.IRG_ID = :irgId ");
	       sb.append("		  AND tpg.ENABLED_FLAG = 'Y' ");
	                        
	       sb.append(" ORDER BY tpg.IRG_PIC_ID ASC ");
           
           Query query = getSession().createSQLQuery(sb.toString());
           query.setParameter("irgId", irgId);
               
           List resultList = query.getResultList();
           
           List<InternalRegulationPenerbitanPicTpgReportVo> dataVoList = new ArrayList<InternalRegulationPenerbitanPicTpgReportVo>();
           
           if(resultList!=null) {
               for(int i=0; i<resultList.size(); i++) {
                   Object[] obj = (Object[]) resultList.get(i);
                   InternalRegulationPenerbitanPicTpgReportVo data = new InternalRegulationPenerbitanPicTpgReportVo();
                   data.setIrgPicId(obj[0]!=null?(MathUtil.returnIdObjectToLong(obj[0])):null);
                   data.setIrgId(obj[1]!=null?(MathUtil.returnIdObjectToLong(obj[1])):null);
                   data.setDivisionId(obj[2]!=null?(MathUtil.returnIdObjectToLong(obj[2])):null);
                   data.setDivisionName(obj[3]!=null?(String)obj[3]:null);
                   data.setPicNik1(obj[4]!=null?(String)obj[4]:null);
                   data.setPicName1(obj[5]!=null?(String)obj[5]:null);            
                   data.setPicNik2(obj[6]!=null?(String)obj[6]:null);            
                   data.setPicName2(obj[7]!=null?(String)obj[7]:null); 
                   data.setPicNik3(obj[8]!=null?(String)obj[8]:null);
                   data.setPicName3(obj[9]!=null?(String)obj[9]:null);       
                   data.setTargetDate(obj[10]!=null?(Date)obj[10]:null);         
                   data.setTargetDateStr(obj[11]!=null?(String)obj[11]:null);
                   data.setNote(obj[12]!=null?(String)obj[12]:null);      
                   dataVoList.add(data);
               }
           }
           
           return dataVoList;
       }
    
    @SuppressWarnings("rawtypes")
   	public List<InternalRegulationPenerbitanPicTpkReportVo> getDataPicTpk(Long irgId) {           
	       StringBuilder sb = new StringBuilder();
	       sb.append(" SELECT tpk.IRG_PIC_ID, tpk.IRG_ID, tpk.DIVISION_ID, div.DIVISION_NAME, "); 
	       sb.append("  	  user1.NIK picNikTpk1, user1.NAME picNameTpk1, ");
	       sb.append(" 	      user2.NIK picNikTpk2, user2.NAME picNameTpk2, "); 
	       sb.append("        user3.NIK picNikTpk3, user3.NAME picNameTpk3, "); 
	       sb.append("        tpk.TARGET_DATE, TO_CHAR(tpk.TARGET_DATE, 'DD-Mon-YYYY') TARGET_DATE_STR, "); 
	       sb.append("        tpk.NOTE, tpk.REVIEW_APPROVAL_FLAG ");
	       sb.append("   FROM WO_TRC_IRG_PIC_TPK tpk "); 
	       sb.append("        INNER JOIN WO_MST_DIVISION div ON tpk.DIVISION_ID = div.DIVISION_ID "); 
	       sb.append("        INNER JOIN WO_MST_USER user1 ON tpk.USER_ID_1 = user1.USER_ID "); 
	       sb.append("        INNER JOIN WO_MST_USER user2 ON tpk.USER_ID_2 = user2.USER_ID "); 
	       sb.append("        LEFT JOIN WO_MST_USER user3 ON tpk.USER_ID_3 = user3.USER_ID "); 
	       sb.append("  WHERE tpk.IRG_ID = :irgId ");
	       sb.append("		  AND tpk.ENABLED_FLAG = 'Y' ");
	                        
	       sb.append(" ORDER BY tpk.IRG_PIC_ID ASC ");
           
           Query query = getSession().createSQLQuery(sb.toString());
           query.setParameter("irgId", irgId);
               
           List resultList = query.getResultList();
           
           List<InternalRegulationPenerbitanPicTpkReportVo> dataVoList = new ArrayList<InternalRegulationPenerbitanPicTpkReportVo>();
           
           if(resultList!=null) {
               for(int i=0; i<resultList.size(); i++) {
                   Object[] obj = (Object[]) resultList.get(i);
                   InternalRegulationPenerbitanPicTpkReportVo data = new InternalRegulationPenerbitanPicTpkReportVo();
                   data.setIrgPicId(obj[0]!=null?(MathUtil.returnIdObjectToLong(obj[0])):null);
                   data.setIrgId(obj[1]!=null?(MathUtil.returnIdObjectToLong(obj[1])):null);
                   data.setDivisionId(obj[2]!=null?(MathUtil.returnIdObjectToLong(obj[2])):null);
                   data.setDivisionName(obj[3]!=null?(String)obj[3]:null);
                   data.setPicNik1(obj[4]!=null?(String)obj[4]:null);
                   data.setPicName1(obj[5]!=null?(String)obj[5]:null);            
                   data.setPicNik2(obj[6]!=null?(String)obj[6]:null);            
                   data.setPicName2(obj[7]!=null?(String)obj[7]:null); 
                   data.setPicNik3(obj[8]!=null?(String)obj[8]:null);
                   data.setPicName3(obj[9]!=null?(String)obj[9]:null);                
                   data.setTargetDate(obj[10]!=null?(Date)obj[10]:null);         
                   data.setTargetDateStr(obj[11]!=null?(String)obj[11]:null);
                   data.setNote(obj[12]!=null?(String)obj[12]:null);      
                   data.setReviewApproval(obj[13]!=null?(String)obj[13]:null);
                   data.setReminderDateH10(getReminderH10FromTpk(data.getIrgPicId()));
                   dataVoList.add(data);
               }
           }
           
         return dataVoList;
    }
    
    @SuppressWarnings("rawtypes")
   	public List<InternalRegulationPenerbitanPicIrgReportVo> getDataPicIrg() {           
	       StringBuilder sb = new StringBuilder();	       
	       sb.append(" SELECT DISTINCT usr.NIK, usr.NAME ");
	       sb.append("   FROM WO_TRC_IRG_PIC_IRG picIrg ");
	       sb.append("        INNER JOIN WO_MST_USER usr ON picIrg.USER_ID_1 = usr.USER_ID ");
	       sb.append("  WHERE 1=1 ");
	       sb.append("        AND picIrg.ENABLED_FLAG = 'Y' ");	                        
	       sb.append("  ORDER BY usr.NAME ASC ");
           
           Query query = getSession().createSQLQuery(sb.toString());
               
           List resultList = query.getResultList();
           
           List<InternalRegulationPenerbitanPicIrgReportVo> dataVoList = new ArrayList<InternalRegulationPenerbitanPicIrgReportVo>();
           
           if(resultList!=null) {
               for(int i=0; i<resultList.size(); i++) {
                   Object[] obj = (Object[]) resultList.get(i);
                   InternalRegulationPenerbitanPicIrgReportVo data = new InternalRegulationPenerbitanPicIrgReportVo();
                   data.setPicNik1(obj[0]!=null?(String)obj[0]:InternalRegulationPenerbitanReportConstants.STRING_EMPTY);
                   data.setPicName1(obj[1]!=null?(String)obj[1]:InternalRegulationPenerbitanReportConstants.STRING_EMPTY);            
                   dataVoList.add(data);
               }
           }
           
           return dataVoList;
       }
    
		@SuppressWarnings("unchecked")
		public String getReminderH10FromTpk(Long irgPicId) {
			StringBuilder sb = new StringBuilder();
			sb.append(" SELECT wtipte.EMAIL_DATE, TO_CHAR(wtipte.EMAIL_DATE, 'DD-Mon-YYYY') FROM WO_TRC_IRG_PIC_TPK_EMAIL wtipte WHERE 1 = 1 AND wtipte.ENABLED_FLAG = 'Y' AND wtipte.IRG_PIC_ID = :irgPicId AND rownum = 1 ");

			Query query = getSession().createSQLQuery(sb.toString());
			query.setParameter("irgPicId", irgPicId);

			List<Object[]> result = query.getResultList();
			
			if(result != null) {
				for (Object[] obj : result) {
					if (obj[0] != null) {
						return (String) obj[1];
					} else {
						return null;
					}
				}
			}
			
			return null;
		}
}