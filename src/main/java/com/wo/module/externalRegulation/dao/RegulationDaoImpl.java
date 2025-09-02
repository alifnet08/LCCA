/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.externalRegulation.dao;


import java.util.List;

import javax.persistence.ParameterMode;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;

import org.springframework.stereotype.Repository;

import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.externalRegulation.model.Regulation;
import com.wo.module.parameter.model.ParameterDetail;

/**
 *
 * @author hendra
 */

@Repository("regulationDao")
public class RegulationDaoImpl extends GenericDAOHibernate<Regulation, Long> 
    implements RegulationDao {
	
	
	
	public Integer getRegulationByDocNoAndDocName(String docNo,String docNameIn,String docNameEn,Long regulationId) throws Exception {

		String hql = "select count(1) FROM Regulation where documentNo = :docNo and nameIn = :docNameIn and nameEn = :docNameEn and enabledFlag = 'Y'";
		if(regulationId!=null) {
			hql = hql+" and regulationId <> :regulationId";
		}
		Query result = getSession().createQuery(hql);
		result.setParameter("docNo", docNo);
		result.setParameter("docNameIn", docNameIn);
		result.setParameter("docNameEn", docNameEn);
		if(regulationId!=null) {
			result.setParameter("regulationId", regulationId);
		}

		return ((java.lang.Long)result.getSingleResult()).intValue();
	}
	
	/*public String procedureUpdateTmpTrackRecord(Long internalId) throws Exception {
		String sql = " CALL wo_sp_update_tmp_track_record(?) ";
		Query query = getSession().createNativeQuery(sql);
		query.setParameter(1, internalId);
		query.getResultList();
		return null;
	}*/
    
	@SuppressWarnings("unused")
	public String procedureUpdateTmpTrackRecord(Long internalId) {
    	String results = null;
    	
    	StoredProcedureQuery spq = this.getSession().createStoredProcedureQuery("wo_sp_update_tmp_track_record");
    	spq.registerStoredProcedureParameter(1, Long.class, ParameterMode.IN); 
    	
    	spq.setParameter(1, internalId);
    	
    	// Stored procedure call
    	spq.execute();
    	return null;
    }
	
    public Integer getCheckDataRegulationSocialization(Long regulationId) throws Exception {    	
    	StringBuilder sb = new StringBuilder();
 		sb.append(" SELECT SUM(TOTAL) ");
 		sb.append("   FROM (SELECT COUNT(1) TOTAL ");
 		sb.append("           FROM wo_tmp_scialization_regulation ");
 		sb.append("  	     WHERE ENABLED_FLAG = 'Y' ");
 		sb.append("  	           AND REGULATION_ID = " +regulationId);
 		sb.append("          UNION ");
 		sb.append("  	     SELECT COUNT(1) TOTAL ");
 		sb.append("  	       FROM wo_tmp_rmd_regulation ");
 		sb.append("  	      WHERE ENABLED_FLAG = 'Y' ");
 		sb.append("  	            AND REGULATION_ID = " +regulationId + ") T ");
 		    
 		Query result = getSession().createSQLQuery(sb.toString());
 		
 		Number number = (Number) result.getSingleResult();
 		Integer count = 0;
 		if(number !=null) {
 			count = number.intValue();
 		}
 		 		
 		return count;
    
    }
    
    @SuppressWarnings("rawtypes")
	public Regulation getCheckDataRegulation(Long regulationId, String jenisRegulation, String docNo, String nameIn) throws Exception {
    	StringBuilder sb = new StringBuilder();
 		sb.append(" SELECT tmp.regulation_id, pd.parameter_dtl_id, tmp.jenis_ketentuan, pd.name_in parameter_name_in, ");
 		sb.append("        pd.name_en parameter_name_en, tmp.document_no, tmp.name_in, tmp.name_en, tmp.enabled_flag ");
 		sb.append("   FROM wo_tmp_regulation tmp ");
 		sb.append("        INNER JOIN wo_mst_parameter_dtl pd ON tmp.jenis_ketentuan = pd.parameter_dtl_code ");
 		sb.append("  WHERE tmp.jenis_ketentuan = :jenisKetentuan ");
 		sb.append("        AND UPPER(tmp.document_no) = UPPER(:document_no) ");
 		sb.append("        AND UPPER(tmp.NAME_IN) = UPPER(:nameIn) ");
 		
 		if(regulationId !=null && regulationId > 0) {
 			sb.append("    AND tmp.regulation_id <> " + regulationId);
 		}
 		 		
 		Query result = getSession().createSQLQuery(sb.toString());
 		result.setParameter("jenisKetentuan", jenisRegulation);
 		result.setParameter("document_no", docNo);
 		result.setParameter("nameIn", nameIn);
 		
 		result.setMaxResults(1);
 		
 		List resultList = result.getResultList();
 		
 		Regulation regulation = new Regulation();
 		if(resultList !=null && resultList.size() > 0) {
	 		for(int i=0; i<resultList.size(); i++) {
	 			Object[] obj = (Object[])resultList.get(i);
	 			
	 			if(obj[0] !=null) {
	 				regulation.setRegulationId(Long.parseLong(obj[0]+""));
	 			}
	 			
	 			ParameterDetail paramDtl = new ParameterDetail();
	 			if(obj[1] !=null) {
	 				paramDtl.setParameterDtlId(Long.parseLong(obj[1]+""));
	 			}	 			
	 			paramDtl.setParameterCode((String)obj[2]);	 		
	 			paramDtl.setNameIn((String)obj[3]);	 	
	 			paramDtl.setNameEn((String)obj[4]);	 	
	 			regulation.setJenisKetentuan(paramDtl);
	 			
	 			regulation.setDocumentNo((String)obj[5]);
	 			regulation.setNameIn((String)obj[6]);
	 			regulation.setNameEn((String)obj[7]);
	 			regulation.setEnabledFlag((String)obj[8]);
	 			
	 		}
 		}
	 		
 		return regulation;
    }
    
    public Integer getCountHitRegulation(Long regulationId, String accessAction) throws Exception {    	
    	StringBuilder sb = new StringBuilder();
 		sb.append(" select count(1) from wo_log_access a,  ");
 		sb.append("   wo_mst_user u where u.user_id = a.user_id  ");
 		sb.append("           and a.access_id = :access_id  and a.access_action = :access_action");
 		
 		    
 		Query result = getSession().createSQLQuery(sb.toString());
 		result.setParameter("access_id", regulationId);
 		result.setParameter("access_action", accessAction);
 		
 		Number number = (Number) result.getSingleResult();
 		Integer count = 0;
 		if(number !=null) {
 			count = number.intValue();
 		}
 		 		
 		return count;
    
    }
    
}
