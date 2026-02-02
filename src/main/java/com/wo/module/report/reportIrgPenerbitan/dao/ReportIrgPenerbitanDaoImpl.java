package com.wo.module.report.reportIrgPenerbitan.dao;

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
import com.wo.module.report.reportIrgPenerbitan.constant.ReportIrgPenerbitanConstants;
import com.wo.module.report.reportIrgPenerbitan.model.ReportIrgPenerbitan;
import com.wo.module.report.reportIrgPenerbitan.model.ReportIrgPenerbitanTpg;
import com.wo.module.report.reportIrgPenerbitan.model.ReportIrgPenerbitanTpk;

@Repository("reportIrgPenerbitanDao")
public class ReportIrgPenerbitanDaoImpl extends GenericDAOHibernate<ReportGen, Long> 
	implements ReportIrgPenerbitanConstants, ReportIrgPenerbitanDao{

	@SuppressWarnings("rawtypes")
	private StringBuilder getQueryWhereString(StringBuilder sb, List<? extends SearchObject> searchCriteria) {
		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString() != null ? searchVal.getSearchValueAsString() : "";
				
				if (!StringUtils.isBlank(val)) {
					if (StringUtils.equals(WHERE_CREATED_DATE_FROM, col)) {
						sb.append(" AND TRUNC(irg.CREATION_DATE) >= TRUNC(TO_DATE('" + val + "', 'yyyy-MM-dd')) ");
					} else if (StringUtils.equals(WHERE_CREATED_DATE_TO, col)) {
						sb.append(" AND TRUNC(irg.CREATION_DATE) <= TRUNC(TO_DATE('" + val + "', 'yyyy-MM-dd')) ");
					} else if (StringUtils.equals(WHERE_REGULATION_TYPE, col)) {
						sb.append(" AND dtl1.PARAMETER_DTL_CODE = '" + val + "' ");
					} else if (StringUtils.equals(WHERE_UNIT_KERJA_TPG, col)) {
						sb.append(" AND div.DIVISION_NAME = '" + val + "' ");
					}					 	
				}
			}
		}
		
		return sb;
	}	
		
	@SuppressWarnings("rawtypes")
	public List<ReportIrgPenerbitan> getAllIrgDataHeader(List<? extends SearchObject> searchCriteria) {        
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
    	
    	sb = getQueryWhereString(sb, searchCriteria);
    	
        sb.append(" ORDER BY irg.IRG_ID DESC ");
        
        Query result = getSession().createSQLQuery(sb.toString());
            
        List resultList = result.getResultList();
        
        List<ReportIrgPenerbitan> dataVoList = new ArrayList<ReportIrgPenerbitan>();
        
        if(resultList!=null) {
            for(int i=0; i<resultList.size(); i++) {
                Object[] obj = (Object[]) resultList.get(i);
                ReportIrgPenerbitan data = new ReportIrgPenerbitan();
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
	private List<ReportIrgPenerbitanTpg> getDataPicTpg(Long irgId) {           
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
           
           List<ReportIrgPenerbitanTpg> dataVoList = new ArrayList<ReportIrgPenerbitanTpg>();
           
           if(resultList!=null) {
               for(int i=0; i<resultList.size(); i++) {
                   Object[] obj = (Object[]) resultList.get(i);
                   ReportIrgPenerbitanTpg data = new ReportIrgPenerbitanTpg();
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
	  private List<ReportIrgPenerbitanTpk> getDataPicTpk(Long irgId) {           
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
           
           List<ReportIrgPenerbitanTpk> dataVoList = new ArrayList<ReportIrgPenerbitanTpk>();
           
           if(resultList!=null) {
               for(int i=0; i<resultList.size(); i++) {
                   Object[] obj = (Object[]) resultList.get(i);
                   ReportIrgPenerbitanTpk data = new ReportIrgPenerbitanTpk();
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
	  
	@SuppressWarnings("unchecked")
	public String getReminderH10FromTpk(Long irgPicId) {
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT wtipte.EMAIL_DATE, TO_CHAR(wtipte.EMAIL_DATE, 'DD-Mon-YYYY') FROM WO_TRC_IRG_PIC_TPK_EMAIL wtipte WHERE 1 = 1 AND wtipte.ENABLED_FLAG = 'Y' AND wtipte.IRG_PIC_ID = :irgPicId AND rownum = 1 ");

		Query query = getSession().createSQLQuery(sb.toString());
		query.setParameter("irgPicId", irgPicId);

		List<Object[]> result = query.getResultList();

		if (result != null) {
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
