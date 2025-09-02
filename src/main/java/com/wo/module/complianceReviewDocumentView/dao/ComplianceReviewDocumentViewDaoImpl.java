package com.wo.module.complianceReviewDocumentView.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.faces.context.FacesContext;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.common.util.MathUtil;
import com.wo.module.complianceReviewDocumentView.constant.ComplianceReviewDocumentViewConstants;
import com.wo.module.complianceReviewDocumentView.model.ComplianceReviewDocumentView;

@Repository("complianceReviewDocumentViewDao")
public class ComplianceReviewDocumentViewDaoImpl extends GenericDAOHibernate<ComplianceReviewDocumentView, Long> 
	implements ComplianceReviewDocumentViewDao{

	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(ComplianceReviewDocumentViewDaoImpl.class);
	
	@Autowired
    @Qualifier("complianceReviewDocumentAttachmentViewDao")
	private ComplianceReviewDocumentAttachmentViewDao complianceReviewDocumentAttachmentViewDao;
	
	@Autowired
    @Qualifier("complianceReviewDocumentPicComplianceViewDao")
	private ComplianceReviewDocumentPicComplianceViewDao complianceReviewDocumentPicComplianceViewDao;
	
	public ComplianceReviewDocumentAttachmentViewDao getComplianceReviewDocumentAttachmentViewDao() {
		return complianceReviewDocumentAttachmentViewDao;
	}

	public void setComplianceReviewDocumentAttachmentViewDao(
			ComplianceReviewDocumentAttachmentViewDao complianceReviewDocumentAttachmentViewDao) {
		this.complianceReviewDocumentAttachmentViewDao = complianceReviewDocumentAttachmentViewDao;
	}

	public ComplianceReviewDocumentPicComplianceViewDao getComplianceReviewDocumentPicComplianceViewDao() {
		return complianceReviewDocumentPicComplianceViewDao;
	}

	public void setComplianceReviewDocumentPicComplianceViewDao(
			ComplianceReviewDocumentPicComplianceViewDao complianceReviewDocumentPicComplianceViewDao) {
		this.complianceReviewDocumentPicComplianceViewDao = complianceReviewDocumentPicComplianceViewDao;
	}

	@SuppressWarnings({"rawtypes","unused"})
	private StringBuilder getQuerryWhereString(StringBuilder sb, List<? extends SearchObject> searchCriteria) {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		if(searchCriteria != null) {
			for (SearchObject searchObject : searchCriteria) {
				String col = searchObject.getSearchColumn();
				String val = searchObject.getSearchValue() != null ? searchObject.getSearchValueAsString() : "";
				Object valReal = searchObject.getSearchValue();
				
				if(!StringUtils.isBlank(val)) {
					if(StringUtils.equals(ComplianceReviewDocumentViewConstants.WHERE_DOCUMET_TYPE, col)) {
						sb.append("	and c.document_type like '%" + val + "%' ");
					} else if (StringUtils.equals(ComplianceReviewDocumentViewConstants.WHERE_HUK_NO, col)) {
						sb.append(" and c.document_no like '%" + val + "%' ");
					} else if (StringUtils.equals(ComplianceReviewDocumentViewConstants.WHERE_REMARKS, col)) {
						sb.append(" and c.remarks like '%" + val + "%' ");
					} else if (StringUtils.equals(ComplianceReviewDocumentViewConstants.WHERE_DOCUMENT_SUBMITTER, col)) {
						sb.append(" and c.document_submitter like '%" + val + "%' ");
					} else if (StringUtils.equals(ComplianceReviewDocumentViewConstants.WHERE_RECEIVED_DATE_START, col)) {
						sb.append(" and TRUNC(c.received_date) >= TO_DATE('" + val + "', 'yyyy-MM-dd') ");
					} else if (StringUtils.equals(ComplianceReviewDocumentViewConstants.WHERE_RECEIVED_DATE_END, col)) {
						sb.append("	and TRUNC(c.received_date) <= TO_DATE('" + val + "', 'yyyy-MM-dd') ");
					} else if (StringUtils.equals(ComplianceReviewDocumentViewConstants.WHERE_COMPLETE_DATE_START, col)) {
						sb.append("	and TRUNC(c.complete_date) >= TO_DATE('" + val + "', 'yyyy-MM-dd') ");
					} else if (StringUtils.equals(ComplianceReviewDocumentViewConstants.WHERE_COMPLETE_DATE_END, col)) {
						sb.append("	and TRUNC(c.complete_date) <= TO_DATE('" +val+ "', 'yyyy-MM-dd') ");
					}
				}
			}
		}
		return sb;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public List<ComplianceReviewDocumentView> searchData(List<? extends SearchObject> searchCriteria, int first,
			int pageSize, String sortField, SortOrder sortOrder) throws Exception {
		List<ComplianceReviewDocumentView> voList = searchDataCriteria(searchCriteria, first, pageSize);
		
		return voList;
	}
	
	@SuppressWarnings("rawtypes")
	private List<ComplianceReviewDocumentView> searchDataCriteria(List<? extends SearchObject> searchCriteria, int first,
			int pageSize){
		StringBuilder sb = new StringBuilder();
		sb.append("	select pd.name_in dokumenTypeIn, pd.name_en dokumenTypeEn "
				+ " 	,TO_CHAR(c.received_date, 'dd-Mon-yyyy') received_date "
				+ "		,TO_CHAR(c.complete_date, 'dd-Mon-yyyy') complete_date "
				+ "		,pd1.name_in dokumenSubmitterIn, pd1.name_en dokumenSubmitterEn "
				+ "		,c.remarks, c.document_no, c.compliance_review_document_id"
				+ "		,c.materi "
				+ " 	,(select u.division_name"
				+ "			from wo_mst_user u "
				+ "			where u.division_id = c.division_id and rownum = 1) division_name "
				+ " from wo_mst_cmplc_rvw_doc c "
				+ " 	left join wo_mst_parameter_dtl pd on pd.parameter_dtl_code = c.document_type "
				+ " 	left join wo_mst_parameter_dtl pd1 on pd1.parameter_dtl_code = c.document_submitter "
				+ " where 1=1 and c.enabled_flag = 'Y' ");
		
		sb = getQuerryWhereString(sb, searchCriteria);
		sb.append(" ORDER BY c.compliance_review_document_id DESC ");
		
		Query result = getSession().createSQLQuery(sb.toString());
		result.setFirstResult(first);
        result.setMaxResults(pageSize);
        
		List resultList = result.getResultList();
		
		List<ComplianceReviewDocumentView> vo = new ArrayList<ComplianceReviewDocumentView>();
		
		if(resultList != null) {
			for (int i = 0; i < resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
				ComplianceReviewDocumentView data = new ComplianceReviewDocumentView();
				data.setDocumentTypeNameIn(obj[0] != null ? (String) obj[0] : null);
				data.setDocumentTypeNameEn(obj[1] != null ? (String) obj[1] : null);
				data.setReceivedDateStr(obj[2] != null ? (String) obj[2] : null);
				data.setCompleteDateStr(obj[3] != null ? (String) obj[3] : null);
				data.setDocumentSubmitterNameIn(obj[4] != null ? (String) obj[4] : null);
				data.setDocumentSubmitterNameEn(obj[5] != null ? (String) obj[5] : null);
				data.setRemarks(obj[6] != null ? (String) obj[6] : null);
				data.setDocumentNo(obj[7] != null ? (String) obj[7] : null);
				data.setComplianceReviewDocumentId(MathUtil.returnIdObjectToLong(obj[8]));
				data.setMateri(obj[9] != null ? (String) obj[9] : null);
				data.setDivisionName(obj[10] != null ? (String) obj[10] : null);
				
				try {
					data.setComplianceReviewDocumentAttachmentViews(complianceReviewDocumentAttachmentViewDao.getComplianceReviewDocumentViewByComplianceDocument(data.getComplianceReviewDocumentId()));
					data.setComplianceReviewDocumentPicComplianceViews(complianceReviewDocumentPicComplianceViewDao.getComplianceReviewDocumentPicComplianceViewByComplianceReviewId(data.getComplianceReviewDocumentId()));
				} catch (Exception e) {
					e.printStackTrace();
				}
				
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
		if(results == null) {
			results = 0;
		}
		
		return results.longValue();
	}
	
	@SuppressWarnings("rawtypes")
	private Number searchCountDataCriteria(List<? extends SearchObject> searchCriteria) {
		StringBuilder sb = new StringBuilder();
		sb.append("	select count(1) "
				+ "	from wo_mst_cmplc_rvw_doc c "
				+ " left join wo_mst_parameter_dtl pd on pd.parameter_dtl_code = c.document_type "
				+ " where 1=1 "
				+ "	and c.enabled_flag = 'Y' ");
		
		sb = getQuerryWhereString(sb, searchCriteria);
		
		Query result = getSession().createSQLQuery(sb.toString());
		
		return (Number) result.getSingleResult();
	}

}
