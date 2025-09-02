/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.tmpComplianceReviewApproval.dao;

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
import com.wo.module.tmpComplianceReview.dao.ComplianceReviewDao;
import com.wo.module.tmpComplianceReview.model.TmpComplianceReview;
import com.wo.module.tmpComplianceReviewApproval.vo.ComplianceReviewApprovalVO;


@Repository("complianceReviewApprovalDao")
public class ComplianceReviewApprovalDaoImpl extends GenericDAOHibernate<TmpComplianceReview, Long>
		implements ComplianceReviewApprovalDao {

	@Autowired
    @Qualifier("complianceReviewDao")
    private ComplianceReviewDao complianceReviewDao;
	
	public ComplianceReviewDao getComplianceReviewDao() {
		return complianceReviewDao;
	}

	public void setComplianceReviewDao(ComplianceReviewDao complianceReviewDao) {
		this.complianceReviewDao = complianceReviewDao;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<ComplianceReviewApprovalVO> searchData(List<? extends SearchObject> searchCriteria, int first,
			int pageSize, String sortField, SortOrder sortOrder) throws Exception {
		List<ComplianceReviewApprovalVO> voList = searchDataCriteria(searchCriteria, first, pageSize);

        return voList;
	}
	
	 @SuppressWarnings("rawtypes")
    private List<ComplianceReviewApprovalVO> searchDataCriteria(List<? extends SearchObject> searchCriteria, int first, int pageSize) {
        
		 StringBuilder sb = new StringBuilder();

		 sb.append(" SELECT cr.compliance_review_id, pd.name_in reviewCategoryIn, pd.name_en reviewCategoryEn, cr.reviewed_branch, cr.document_no, ");
		 sb.append(" TO_CHAR(cr.document_date, 'dd-Mon-yyyy') document_date, cr.perihal_in, cr.perihal_en, pd2.name_in statusIn, pd2.name_en statusEn ");
		 sb.append(" FROM wo_tmp_compliance_review cr ");
		 sb.append(" INNER JOIN wo_mst_parameter_dtl pd ON pd.parameter_dtl_code = cr.review_category  ");
		 sb.append(" INNER JOIN wo_mst_parameter_dtl pd2 ON pd2.parameter_dtl_code = cr.status ");
		 sb.append(" WHERE 1=1 AND cr.enabled_flag = 'Y' AND cr.status = 'DATA_NEW' ");
		
        
        //sb = getQueryWhereString(sb, searchCriteria);
        sb.append(" ORDER BY cr.compliance_review_id DESC ");
        
        
        Query result = getSession().createSQLQuery(sb.toString());
        result.setFirstResult(first);
        result.setMaxResults(pageSize);
        
        List resultList = result.getResultList();
        
        List<ComplianceReviewApprovalVO> vo = new ArrayList<ComplianceReviewApprovalVO>();
        
        if(resultList!=null) {
            for(int i=0; i<resultList.size(); i++) {
                Object[] obj = (Object[]) resultList.get(i);
                ComplianceReviewApprovalVO data = new ComplianceReviewApprovalVO();
                data.setComplianceReviewId(obj[0]!=null?(MathUtil.returnIdObjectToLong(obj[0])):null);
                data.setReviewCategoryIn(obj[1]!=null?(String)obj[1]:null);
                data.setReviewCategoryEn(obj[2]!=null?(String)obj[2]:null);
                data.setReviewedBranch(obj[3]!=null?(String)obj[3]:null);
                data.setDocumentNo(obj[4]!=null?(String)obj[4]:null);
                data.setDocumentDateStr(obj[5]!=null?(String)obj[5]:null);
                data.setPerihalIn(obj[6]!=null?(String)obj[6]:null);
                data.setPerihalEn(obj[7]!=null?(String)obj[7]:null);
                data.setStatusIn(obj[8]!=null?(String)obj[8]:null);
                data.setStatusEn(obj[9]!=null?(String)obj[9]:null);
                data.setStatusList(complianceReviewDao.getDataPicFollowupComplianceReviewId(data.getComplianceReviewId()));
                
                vo.add(data);
            }
        }

        result.setFirstResult(first);
        result.setMaxResults(pageSize);
        
        return vo;
    }

	@SuppressWarnings("rawtypes")
	@Override
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

		sb.append(" SELECT COUNT(1) FROM wo_tmp_compliance_review cr ");
		sb.append(" INNER JOIN wo_mst_parameter_dtl pd ON pd.parameter_dtl_code = cr.review_category  ");
		sb.append(" INNER JOIN wo_mst_parameter_dtl pd2 ON pd2.parameter_dtl_code = cr.status ");
		sb.append(" WHERE 1=1 AND cr.enabled_flag = 'Y' AND cr.status = 'DATA_NEW' ");

       
        //sb = getQueryWhereString(sb, searchCriteria);
        
        Query result = getSession().createSQLQuery(sb.toString());
       
        
        return (Number) result.getSingleResult();
    }

}
