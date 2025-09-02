package com.wo.module.complianceReviewDocument.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.faces.context.FacesContext;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.common.util.MathUtil;
import com.wo.module.complianceReviewDocument.constant.ComplianceReviewDocumentConstants;
import com.wo.module.complianceReviewDocument.model.ComplianceReviewDocument;
import com.wo.module.division.dao.DivisionDao;
import com.wo.module.user.dao.UserDao;

@Repository("complianceReviewDocumentDao")
public class ComplianceReviewDocumentDaoImpl extends GenericDAOHibernate<ComplianceReviewDocument, Long>
	implements ComplianceReviewDocumentDao{
	
	@Autowired
    @Qualifier("complianceReviewDocumentAttachmentDao")
	private ComplianceReviewDocumentAttachmentDao complianceReviewDocumentAttachmentDao;
	
	@Autowired
    @Qualifier("complianceReviewDocumentPicComplianceDao")
	private ComplianceReviewDocumentPicComplianceDao complianceReviewDocumentPicComplianceDao;
	
	@Autowired
    @Qualifier("userDao")
	private UserDao userDao;
	
	@Autowired
	@Qualifier("divisionDao")
	private DivisionDao divisionDao;
	
	@SuppressWarnings({"rawtypes","unused"})
	private StringBuilder getQuerryWhereString(StringBuilder sb, List<? extends SearchObject> searchCriteria) {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		if(searchCriteria != null) {
			for (SearchObject searchObject : searchCriteria) {
				String col = searchObject.getSearchColumn();
				String val = searchObject.getSearchValue() != null ? searchObject.getSearchValueAsString() : "";
				Object valReal = searchObject.getSearchValue();
				
				if(!StringUtils.isBlank(val)) {
					if(StringUtils.equals(ComplianceReviewDocumentConstants.WHERE_DOCUMET_TYPE, col)) {
						sb.append("	and c.document_type = '" + val + "' ");
					} else if (StringUtils.equals(ComplianceReviewDocumentConstants.WHERE_HUK_NO, col)) {
						sb.append(" and upper(c.document_no) like upper('%" + val + "%') ");
					} else if (StringUtils.equals(ComplianceReviewDocumentConstants.WHERE_MATERI, col)) {
						sb.append(" and upper(c.materi) like upper('%" + val + "%') ");
					} else if (StringUtils.equals(ComplianceReviewDocumentConstants.WHERE_DOCUMENT_SUBMITTER, col)) {
						sb.append(" and c.document_submitter = '" + val + "' ");
					} else if (StringUtils.equals(ComplianceReviewDocumentConstants.WHERE_RECEIVED_DATE_START, col)) {
						sb.append(" and TRUNC(c.received_date) >= TO_DATE('" + val + "', 'yyyy-MM-dd') ");
					} else if (StringUtils.equals(ComplianceReviewDocumentConstants.WHERE_RECEIVED_DATE_END, col)) {
						sb.append("	and TRUNC(c.received_date) <= TO_DATE('" + val + "', 'yyyy-MM-dd') ");
					} else if (StringUtils.equals(ComplianceReviewDocumentConstants.WHERE_COMPLETE_DATE_START, col)) {
						sb.append("	and TRUNC(c.complete_date) >= TO_DATE('" + val + "', 'yyyy-MM-dd') ");
					} else if (StringUtils.equals(ComplianceReviewDocumentConstants.WHERE_COMPLETE_DATE_END, col)) {
						sb.append("	and TRUNC(c.complete_date) <= TO_DATE('" +val+ "', 'yyyy-MM-dd') ");
					}
				}
			}
		}
		return sb;
	}

	@SuppressWarnings("rawtypes")
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
	
	@SuppressWarnings("rawtypes")
	public List<ComplianceReviewDocument> searchData(List<? extends SearchObject> searchCriteria, int first,
			int pageSize, String sortField, SortOrder sortOrder) throws Exception {
		
		List<ComplianceReviewDocument> voList = searchDataCriteria(searchCriteria, first, pageSize);
		
		return voList;
	}
	
	@SuppressWarnings("rawtypes")
	private List<ComplianceReviewDocument> searchDataCriteria(List<? extends SearchObject> searchCriteria, int first,
			int pageSize){
		StringBuilder sb = new StringBuilder();
		sb.append("	select pd.name_in dokumenTypeIn, pd.name_en dokumenTypeEn "
				+ " 	,TO_CHAR(c.received_date, 'dd-Mon-yyyy') received_date "
				+ "		,TO_CHAR(c.complete_date, 'dd-Mon-yyyy') complete_date "
				+ "		,pd1.name_in dokumenSubmitterIn, pd1.name_en dokumenSubmitterEn "
				+ "		,c.remarks, c.document_no, c.compliance_review_document_id "
				+ "		,c.document_type, c.materi "
				+ "		,(select u.division_name"
				+ "			from wo_mst_user u "
				+ "			where u.division_id = c.division_id and rownum = 1) division_name "
				+ "		,c.division_multi_id "
				+ " from wo_mst_cmplc_rvw_doc c "
				+ " 	left join wo_mst_parameter_dtl pd on pd.parameter_dtl_code = c.document_type "
				+ " 	left join wo_mst_parameter_dtl pd1 on pd1.parameter_dtl_code = c.document_submitter "
				+ " where 1=1 and c.enabled_flag = 'Y' ");
		
		sb = getQuerryWhereString(sb, searchCriteria);
//		sb.append(" ORDER BY c.compliance_review_document_id DESC ");
		sb.append(" ORDER BY c.creation_date DESC ");
		
		Query result = getSession().createSQLQuery(sb.toString());
		result.setFirstResult(first);
        result.setMaxResults(pageSize);
		List resultList = result.getResultList();
		
		List<ComplianceReviewDocument> vo = new ArrayList<ComplianceReviewDocument>();
		
		if(resultList != null) {
			for (int i = 0; i < resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
				ComplianceReviewDocument data = new ComplianceReviewDocument();
				data.setDocumentTypeNameIn(obj[0] != null ? (String) obj[0] : null);
				data.setDocumentTypeNameEn(obj[1] != null ? (String) obj[1] : null);
				data.setReceivedDateStr(obj[2] != null ? (String) obj[2] : null);
				data.setCompleteDateStr(obj[3] != null ? (String) obj[3] : null);
				data.setDocumentSubmitterNameIn(obj[4] != null ? (String) obj[4] : null);
				data.setDocumentSubmitterNameEn(obj[5] != null ? (String) obj[5] : null);
				data.setRemarks(obj[6] != null ? (String) obj[6] : null);
				data.setDocumentNo(obj[7] != null ? (String) obj[7] : null);
				data.setComplianceReviewDocumentId(MathUtil.returnIdObjectToLong(obj[8]));
				data.setDocumentTypeCode(obj[9] != null ? (String) obj[9] : null);
				data.setMateri(obj[10] != null ? (String) obj[10] : null);
				if(obj[12] !=null) {
					String divIds = (String) obj[12];
					String[] arrayDivIds = divIds.split(";");
					
					List<String> divNames = new ArrayList<String>();
					
					for(int j=0; j < arrayDivIds.length; j++) {
						if(!arrayDivIds[j].equals("")) {
							String divName = divisionDao.getDivisionNameByDivisionId(
									Long.parseLong(arrayDivIds[j]));
							divNames.add(divName);
						}else {
							break;
						}
					}
					//System.out.println(divisionNames.toString());
					data.setDivisionNames(divNames);
				}else {
					String divName = obj[11] != null ? (String) obj[11] : null;
					
					data.setDivisionNames(new ArrayList<String>());
					
					if(divName != null)
						data.getDivisionNames().add(divName);
				}
				
				
				
				try {
					data.setComplianceReviewDocumentAttachments(complianceReviewDocumentAttachmentDao.getComplianceReviewDocumentByComplianceDocument(data.getComplianceReviewDocumentId()));
					data.setComplianceReviewDocumentPicCompliance(complianceReviewDocumentPicComplianceDao.getComplianceReviewDocumentPicComplianceByComplianceReviewId(data.getComplianceReviewDocumentId()));
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

	public ComplianceReviewDocumentAttachmentDao getComplianceReviewDocumentAttachmentDao() {
		return complianceReviewDocumentAttachmentDao;
	}

	public void setComplianceReviewDocumentAttachmentDao(ComplianceReviewDocumentAttachmentDao complianceReviewDocumentAttachmentDao) {
		this.complianceReviewDocumentAttachmentDao = complianceReviewDocumentAttachmentDao;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<ComplianceReviewDocument> searchDataXLS(List<? extends SearchObject> searchCriteria) {
		StringBuilder sb = new StringBuilder();
		sb.append("	select c.compliance_review_document_id "
				+ "	,pd.name_in dokumenTypeIN, pd.name_en dokumenTypeEn"
				+ "	,TO_CHAR(c.received_date, 'dd-Mon-yyyy') received_date "
				+ "	,TO_CHAR(c.complete_date, 'dd-Mon-yyyy') complete_date "
				+ "	,pd1.name_in dokumenSubmitterIn, pd1.name_en dokumenSubmitterEn"
				+ "	,c.remarks, c.document_no, c.materi "
				+ " ,(select u.division_name"
				+ "			from wo_mst_user u "
				+ "			where u.division_id = c.division_id and rownum = 1) division_name"
				+ " ,c.division_multi_id "
				+ "		from wo_mst_cmplc_rvw_doc c "
				+ "			left join wo_mst_parameter_dtl pd on pd.parameter_dtl_code = c.document_type "
				+ "			left join wo_mst_parameter_dtl pd1 on pd1.parameter_dtl_code = c.document_submitter "
				+ "		where 1 = 1 and c.enabled_flag = 'Y' ");
		
		sb = getQueryWhereXLSString(sb, searchCriteria);
		sb.append("	order by c.compliance_review_document_id desc ");
		
		Query query = getSession().createSQLQuery(sb.toString());
		List resuList = query.getResultList();
		
		List<ComplianceReviewDocument> vo = new ArrayList<ComplianceReviewDocument>();
		
		if(resuList != null) {
			for (int i = 0; i < resuList.size(); i++) {
				Object[] obj = (Object[]) resuList.get(i);
				ComplianceReviewDocument data = new ComplianceReviewDocument();
				data.setComplianceReviewDocumentId(MathUtil.returnIdObjectToLong(obj[0]));
				data.setDocumentTypeNameIn(obj[1] != null ? (String) obj[1] : null);
				data.setDocumentTypeNameEn(obj[2] != null ? (String) obj[2] : null);
				data.setReceivedDateStr(obj[3] != null ? (String) obj[3] : null);
				data.setCompleteDateStr(obj[4] != null ? (String) obj[4] : null);
				data.setDocumentSubmitterNameIn(obj[5] != null ? (String) obj[5] : null);
				data.setDocumentSubmitterNameEn(obj[6] != null ? (String) obj[6] : null);
				data.setRemarks(obj[7] != null ? (String) obj[7] : null);
				data.setDocumentNo(obj[8] != null ? (String) obj[8] : null);
				data.setMateri(obj[9] != null ? (String) obj[9] : null);
				
				if(obj[11] !=null) {
					StringBuilder divisionNames = new StringBuilder();
					String divIds = (String) obj[11];
					String[] arrayDivIds = divIds.split(";");
					for(int j=0; j < arrayDivIds.length; j++) {
						if(!arrayDivIds[j].equals("")) {
							String divName = userDao.getDivisionNameByDivisionId(
									Long.parseLong(arrayDivIds[j]));
							if(j != arrayDivIds.length-1) {
								divisionNames.append(divName+",");
							}else {
								divisionNames.append(divName);
							}
						}else {
							break;
						}
					}
					System.out.println(divisionNames.toString());
					data.setDivisionName(divisionNames.toString());
				}else {
					data.setDivisionName(obj[10] != null ? (String) obj[10] : null);
				}
				try {
					data.setComplianceReviewDocumentPicCompliance(complianceReviewDocumentPicComplianceDao.getComplianceReviewDocumentPicComplianceByComplianceReviewId(data.getComplianceReviewDocumentId()));
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				vo.add(data);
			}
		}
		
		return vo;
	}
	
	@SuppressWarnings("rawtypes")
	private StringBuilder getQueryWhereXLSString(StringBuilder sb, List<? extends SearchObject> searchCriteria) {
//		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		if(searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValue() != null ? searchVal.getSearchValueAsString() : "";
				
				if(!StringUtils.isBlank(val)) {
					if(StringUtils.equals(ComplianceReviewDocumentConstants.WHERE_DOCUMET_TYPE, col)) {
						sb.append("	and c.document_type like '%" + val + "%' ");
					} else if (StringUtils.equals(ComplianceReviewDocumentConstants.WHERE_HUK_NO, col)) {
						sb.append(" and c.document_no like '%" + val + "%' ");
					} else if (StringUtils.equals(ComplianceReviewDocumentConstants.WHERE_MATERI, col)) {
						sb.append(" and c.materi like '%" + val + "%' ");
					} else if (StringUtils.equals(ComplianceReviewDocumentConstants.WHERE_RECEIVED_DATE_START, col)) {
						sb.append(" and TRUNC(c.received_date) >= TO_DATE('" + val + "', 'yyyy-MM-dd') ");
					} else if (StringUtils.equals(ComplianceReviewDocumentConstants.WHERE_RECEIVED_DATE_END, col)) {
						sb.append("	and TRUNC(c.received_date) <= TO_DATE('" + val + "', 'yyyy-MM-dd') ");
					} else if (StringUtils.equals(ComplianceReviewDocumentConstants.WHERE_COMPLETE_DATE_START, col)) {
						sb.append("	and TRUNC(c.complete_date) >= TO_DATE('" + val + "', 'yyyy-MM-dd') ");
					} else if (StringUtils.equals(ComplianceReviewDocumentConstants.WHERE_COMPLETE_DATE_END, col)) {
						sb.append("	and TRUNC(c.complete_date) <= TO_DATE('" +val+ "', 'yyyy-MM-dd') ");
					} else if (StringUtils.equals(ComplianceReviewDocumentConstants.WHERE_DOCUMENT_SUBMITTER, col)) {
						sb.append(" and c.document_submitter like '%" + val + "%' ");
					}
				}
			}
		}
		return sb;
	}

	public ComplianceReviewDocumentPicComplianceDao getComplianceReviewDocumentPicComplianceDao() {
		return complianceReviewDocumentPicComplianceDao;
	}

	public void setComplianceReviewDocumentPicComplianceDao(ComplianceReviewDocumentPicComplianceDao complianceReviewDocumentPicComplianceDao) {
		this.complianceReviewDocumentPicComplianceDao = complianceReviewDocumentPicComplianceDao;
	}

	@Override
	public Integer getComplianceDocumentByTypeAndNo(String documentType, String documentNo) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append("	SELECT COUNT(1) ");
		sb.append("		FROM wo_mst_cmplc_rvw_doc ");
		sb.append("		WHERE document_type = '"+ documentType +"' ");
		sb.append("			AND document_no = '" + documentNo +"' ");
		sb.append("			AND enabled_flag = 'Y' ");
		
		Query result = getSession().createSQLQuery(sb.toString());
		
		Number count = (Number) result.getSingleResult();
		if(count == null) {
			count = 0;
		}
		
		return (Integer) count.intValue() ;
	}

	@Override
	public Integer getComplianceDocumentByIdTypeAndNo(Long id, String documentType, String documentNo)
			throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append("	SELECT COUNT(1)"
				+ "		FROM wo_mst_cmplc_rvw_doc ");
		sb.append("		WHERE compliance_review_document_id <> '" + id + "'");
		sb.append("			AND document_type = '" +documentType+ "' ");
		sb.append("			AND document_no = '" +documentNo+ "' ");
		sb.append("			AND enabled_flag = 'Y' ");
		
		Query result = getSession().createSQLQuery(sb.toString());
		
		Number count = (Number) result.getSingleResult();
		if(count == null) {
			count = 0;
		}
		
		return (Integer) count.intValue();
	}

	@Override
	public int countRowByCreationYear(int year) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT COUNT(*) "
				+ " FROM wo_mst_cmplc_rvw_doc wmcrd "
				+ " WHERE 1=1"
				+ " AND EXTRACT(YEAR FROM wmcrd.creation_date) = :year "
				+ " AND wmcrd.enabled_flag = 'Y'");
		
		Query result = getSession().createSQLQuery(sb.toString());
		result.setParameter("year",year);
		
		Number count = (Number) result.getSingleResult();
		if(count == null) {
			count = 0;
		}
		return count.intValue();
	}

	
}