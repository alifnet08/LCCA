/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.regulationSocializationApproval.dao;


import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.common.util.MathUtil;
import com.wo.module.regulationSocialization.dao.RegulationSocializationDao;
import com.wo.module.regulationSocialization.model.SocializationTmp;
import com.wo.module.regulationSocializationApproval.vo.RegulationSocializationApprovalVO;


/**
 *
 * @author hendra
 */

@Repository("regulationSocializationApprovalDao")
public class RegulationSocializationApprovalDaoImpl extends GenericDAOHibernate<SocializationTmp, Long> 
    implements RegulationSocializationApprovalDao {
	
	@Autowired
    @Qualifier("regulationSocializationDao")
    private RegulationSocializationDao regulationSocializationDao;
	
	public RegulationSocializationDao getRegulationSocializationDao() {
		return regulationSocializationDao;
	}

	public void setRegulationSocializationDao(RegulationSocializationDao regulationSocializationDao) {
		this.regulationSocializationDao = regulationSocializationDao;
	}

	@SuppressWarnings({ "rawtypes", "unused" })
    private StringBuilder getQueryWhereString(StringBuilder sb, List<? extends SearchObject> searchCriteria) {
		
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
		sb.append(" FROM wo_tmp_socialization s inner join wo_tmp_scialization_regulation sr on s.socialization_id = sr.socialization_id ");
		sb.append(" inner join wo_mst_parameter_dtl pd on pd.parameter_dtl_code = s.jenis_ketentuan  ");
		sb.append(" inner join wo_mst_regulation r on r.regulation_id = sr.regulation_id  ");
		sb.append(" left join wo_mst_document_type dt on r.document_type_id = dt.document_type_id  ");
		sb.append(" left join wo_mst_document_category dc on dc.document_category_id = r.document_category_id  ");
		sb.append(" left join wo_mst_document_topic dp on dp.document_topic_id = r.document_topic_id  ");
		sb.append(" WHERE 1=1 and s.enabled_flag = 'Y' and sr.primary_flag = 'Y' and s.status = 'DATA_NEW' ");
       
        //sb = getQueryWhereString(sb, searchCriteria);
        
        Query result = getSession().createSQLQuery(sb.toString());
       
        
        return (Number) result.getSingleResult();
    }
    
    @SuppressWarnings("rawtypes")
    public List<RegulationSocializationApprovalVO> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize, String sortField, SortOrder sortOrder) 
            throws Exception {
        
        List<RegulationSocializationApprovalVO> voList = searchDataCriteria(searchCriteria, first, pageSize);

        return voList;
    }
    
    @SuppressWarnings("rawtypes")
    private List<RegulationSocializationApprovalVO> searchDataCriteria(List<? extends SearchObject> searchCriteria, int first, int pageSize) {
        
        StringBuilder sb = new StringBuilder();
		sb.append(" SELECT dt.document_type_in,dt.document_type_en, dc.document_category_in, dc.document_category_en, ");
		sb.append("        dp.document_topic_in, dp.document_topic_en, r.document_no, r.name_in, r.name_en, ");
		sb.append("        '' picName, s.status, ");
		sb.append("        s.socialization_id, pd.name_in jnsKetentuanIn,pd.name_en jnsKetentuanEn,d.name_in statusIn,d.name_en statusEn ");
		sb.append(" FROM wo_tmp_socialization s inner join wo_tmp_scialization_regulation sr on s.socialization_id = sr.socialization_id ");
		sb.append(" inner join wo_mst_parameter_dtl pd on pd.parameter_dtl_code = s.jenis_ketentuan  ");
		sb.append(" inner join wo_mst_regulation r on r.regulation_id = sr.regulation_id  ");
		sb.append(" left join wo_mst_document_type dt on r.document_type_id = dt.document_type_id  ");
		sb.append(" left join wo_mst_document_category dc on dc.document_category_id = r.document_category_id  ");
		sb.append(" left join wo_mst_document_topic dp on dp.document_topic_id = r.document_topic_id  ");
		sb.append(" left join wo_mst_parameter_dtl d on s.status = d.parameter_dtl_code ");
		sb.append(" WHERE 1=1 and s.enabled_flag = 'Y' and sr.primary_flag = 'Y' and s.status = 'DATA_NEW' ");
		
        
        //sb = getQueryWhereString(sb, searchCriteria);
        sb.append(" ORDER BY s.socialization_id DESC ");
        
        
        Query result = getSession().createSQLQuery(sb.toString());
        result.setFirstResult(first);
        result.setMaxResults(pageSize);
        
        List resultList = result.getResultList();
        
        List<RegulationSocializationApprovalVO> vo = new ArrayList<RegulationSocializationApprovalVO>();
        
        if(resultList!=null) {
            for(int i=0; i<resultList.size(); i++) {
                Object[] obj = (Object[]) resultList.get(i);
                RegulationSocializationApprovalVO data = new RegulationSocializationApprovalVO();
                data.setDocumentTypeNameIn(obj[0]!=null?(String)obj[0]:null);
                data.setDocumentTypeNameEn(obj[1]!=null?(String)obj[1]:null);
                data.setDocumentCategoryNameIn(obj[2]!=null?(String)obj[2]:null);
                data.setDocumentCategoryNameEn(obj[3]!=null?(String)obj[3]:null);
                data.setDocumentTopicNameIn(obj[4]!=null?(String)obj[4]:null);
                data.setDocumentTopicNameEn(obj[5]!=null?(String)obj[5]:null);
                data.setDocumentNo(obj[6]!=null?(String)obj[6]:null);
                data.setNameIn(obj[7]!=null?(String)obj[7]:null);
                data.setNameEn(obj[8]!=null?(String)obj[8]:null);
                data.setPicName(obj[9]!=null?(String)obj[9]:null);
                //data.setStatus(obj[10]!=null?(String)obj[10]:null);
                //data.setSocializationId(obj[11]!=null?((java.math.BigInteger)obj[11]).longValue():null);
                data.setSocializationId(obj[11]!=null?(MathUtil.returnIdObjectToLong(obj[11])):null);
                data.setJenisKetentuanIn(obj[12]!=null?(String)obj[12]:null);
                data.setJenisKetentuanEn(obj[13]!=null?(String)obj[13]:null);
                data.setStatusList(regulationSocializationDao.getDataConfirmStatusBySocializationId(data.getSocializationId()));
                data.setStatusIn(obj[14]!=null?(String)obj[14]:null);
                data.setStatusEn(obj[15]!=null?(String)obj[15]:null);
                vo.add(data);
            }
        }

        result.setFirstResult(first);
        result.setMaxResults(pageSize);
        
        return vo;
    }
    
   
    
}
