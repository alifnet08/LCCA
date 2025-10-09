package com.wo.module.regulationMonitoringView.dao;

//import java.text.ParseException;
//import java.text.SimpleDateFormat;
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
import com.wo.module.regulationMonitoring.dao.RegMonitoringPICFollowUpAttachmentTrcDAO;
import com.wo.module.regulationMonitoring.dao.RegMonitoringPICFollowUpTrcDAO;
import com.wo.module.regulationMonitoring.model.RegMonitoringTrc;
import com.wo.module.regulationMonitoring.vo.RegMonitoringApprovalVO;
import com.wo.module.regulationMonitoring.vo.StatusConfirmationVO;
import com.wo.module.regulationMonitoringView.constant.RegMonitoringViewConstants;
import com.wo.module.regulationMonitoringView.vo.RegMonitoringViewVO;

@Repository("regMonitoringViewDAO")
public class RegMonitoringViewDAOImpl extends GenericDAOHibernate<RegMonitoringTrc, Long> 
    implements RegMonitoringViewDAO {
	
	@Autowired
	@Qualifier("regMonitoringPICFollowUpAttachmentTrcDAO")
	private RegMonitoringPICFollowUpAttachmentTrcDAO regMonitoringPICFollowUpAttachmentTrcDAO;
	
	@Autowired
	@Qualifier("regMonitoringPICFollowUpTrcDAO")
	private RegMonitoringPICFollowUpTrcDAO regMonitoringPICFollowUpTrcDAO;

	
	@SuppressWarnings({ "rawtypes", "static-access", "unused" })
    private StringBuilder getQueryWhereString(StringBuilder sb, List<? extends SearchObject> searchCriteria) {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		//SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
		//SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValue()!=null?searchVal.getSearchValueAsString():"";
				Object valReal = searchVal.getSearchValue();
				
				if (!StringUtils.isBlank(val)) {
					
					if(StringUtils.equals(RegMonitoringViewConstants.WHERE_PROVISION_TYPE, col)) {
						sb.append(" and s.jenis_ketentuan = '" + val + "' ");
					}
					else if (StringUtils.equals(RegMonitoringViewConstants.WHERE_DOC_TYPE, col)) {
						sb.append(" and dt.document_type_id = " + val + " ");
					}
					else if (StringUtils.equals(RegMonitoringViewConstants.WHERE_CATEGORY, col)) {
						sb.append(" and dc.document_category_id = " + val + " ");
					}
					else if (StringUtils.equals(RegMonitoringViewConstants.WHERE_TOPIC, col)) {
						sb.append(" and dp.document_topic_id = " + val + " ");
					}
					else if (StringUtils.equals(RegMonitoringViewConstants.WHERE_DOC_NO, col)) {
						sb.append(" and r.document_no like '%" + val + "%' ");
					}
					else if (StringUtils.equals(RegMonitoringViewConstants.WHERE_NAME, col)) {
						if (locale != null && locale.equals(locale.ENGLISH)) {
							sb.append(" and r.name_en LIKE '%" + val + "%' ");
						} else {
							sb.append(" and r.name_in LIKE '%" + val + "%' ");
						}
					}
//					else if (StringUtils.equals(RegMonitoringViewConstants.WHERE_PUBLISHED_DATE_START, col)) {						
//						sb.append(" and DATE(r.published_date) >= DATE('" + (Date) valReal + "') ");						
//					}
//					else if (StringUtils.equals(RegMonitoringViewConstants.WHERE_PUBLISHED_DATE_END, col)) {						
//						sb.append(" and DATE(r.published_date) <= DATE('" + (Date) valReal + "') ");						
//					}
//					else if (StringUtils.equals(RegMonitoringViewConstants.WHERE_EFF_DATE_START, col)) {						
//						sb.append(" and DATE(r.effective_date) >= DATE('" + (Date) valReal + "') ");						
//					}
//					else if (StringUtils.equals(RegMonitoringViewConstants.WHERE_EFF_DATE_END, col)) {						
//						sb.append(" and DATE(r.effective_date) <= DATE('" + (Date) valReal + "') ");						
//					}
//					else if (StringUtils.equals(RegMonitoringViewConstants.WHERE_TARGET_DATE_START, col)) {							
//						sb.append(" and EXISTS( SELECT " + 
//								"            1" + 
//								"        FROM" + 
//								"            WO_TRC_REG_MONITORING_PIC_FP spf" + 
//								"        WHERE" + 
//								"            spf.REG_MONITORING_ID = s.REG_MONITORING_ID" + 
//								"                AND spf.target_date >= DATE('" + (Date) valReal + "') ) ");						
//					}
//					else if (StringUtils.equals(RegMonitoringViewConstants.WHERE_TARGET_DATE_END, col)) {						
//						sb.append(" and EXISTS( SELECT " + 
//								"            1" + 
//								"        FROM" + 
//								"            WO_TRC_REG_MONITORING_PIC_FP spf" + 
//								"        WHERE" + 
//								"            spf.REG_MONITORING_ID = s.REG_MONITORING_ID" + 
//								"                AND spf.target_date <= DATE('" + (Date) valReal + "') ) ");						
//					}
					else if (StringUtils.equals(RegMonitoringViewConstants.WHERE_PUBLISHED_DATE_START, col)) {						
						sb.append(" and TRUNC(r.published_date) >= TO_DATE('" + val + "', 'yyyy-MM-dd') ");						
					}
					else if (StringUtils.equals(RegMonitoringViewConstants.WHERE_PUBLISHED_DATE_END, col)) {						
						sb.append(" and TRUNC(r.published_date) <= TO_DATE('" + val + "', 'yyyy-MM-dd') ");						
					}
					else if (StringUtils.equals(RegMonitoringViewConstants.WHERE_EFF_DATE_START, col)) {						
						sb.append(" and TRUNC(r.effective_date) >= TO_DATE('" + val + "', 'yyyy-MM-dd') ");						
					}
					else if (StringUtils.equals(RegMonitoringViewConstants.WHERE_EFF_DATE_END, col)) {						
						sb.append(" and TRUNC(r.effective_date) <= TO_DATE('" + val + "', 'yyyy-MM-dd') ");						
					}
					else if (StringUtils.equals(RegMonitoringViewConstants.WHERE_TARGET_DATE_START, col)) {							
						sb.append(" and EXISTS( SELECT " + 
								"            1" + 
								"        FROM" + 
								"            WO_TRC_REG_MONITORING_PIC_FP spf" + 
								"        WHERE" + 
								"            spf.REG_MONITORING_ID = s.REG_MONITORING_ID" + 
								"                AND spf.target_date >= TO_DATE('" + val + "', 'yyyy-MM-dd') ) ");						
					}
					else if (StringUtils.equals(RegMonitoringViewConstants.WHERE_TARGET_DATE_END, col)) {						
						sb.append(" and EXISTS( SELECT " + 
								"            1" + 
								"        FROM" + 
								"            WO_TRC_REG_MONITORING_PIC_FP spf" + 
								"        WHERE" + 
								"            spf.REG_MONITORING_ID = s.REG_MONITORING_ID" + 
								"                AND spf.target_date <= TO_DATE('" + val + "', 'yyyy-MM-dd') ) ");						
					}
					else if (StringUtils.equals(RegMonitoringViewConstants.WHERE_STATUS, col)) {
						sb.append(" and s.status = '" + val + "' ");
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
		sb.append(" SELECT count(1) ");
		sb.append("   FROM WO_TRC_REG_MONITORING s ");
		sb.append("        INNER JOIN WO_TRC_REG_MONITOR_REGULATION sr on s.REG_MONITORING_ID = sr.REG_MONITORING_ID ");
		sb.append("        INNER JOIN wo_mst_parameter_dtl pd on pd.parameter_dtl_code = s.jenis_ketentuan  ");
		sb.append("        INNER JOIN wo_mst_regulation r on r.regulation_id = sr.regulation_id  ");
		sb.append("        LEFT JOIN wo_mst_document_type dt on r.document_type_id = dt.document_type_id  ");
		sb.append("        LEFT JOIN wo_mst_document_category dc on dc.document_category_id = r.document_category_id  ");
		sb.append("        LEFT JOIN wo_mst_document_topic dp on dp.document_topic_id = r.document_topic_id  ");
		sb.append("        INNER JOIN wo_mst_parameter_dtl pds on pds.parameter_dtl_code = s.status ");
		sb.append("  WHERE 1=1 and s.enabled_flag = 'Y' and sr.primary_flag = 'Y' ");
       
        sb = getQueryWhereString(sb, searchCriteria);
        
        Query result = getSession().createSQLQuery(sb.toString());       
        
        return (Number) result.getSingleResult();
    }
    
    @SuppressWarnings("rawtypes")
    public List<RegMonitoringViewVO> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize, String sortField, SortOrder sortOrder) 
            throws Exception {
        
        List<RegMonitoringViewVO> voList = searchDataCriteria(searchCriteria, first, pageSize);

        return voList;
    }
    
    @SuppressWarnings("rawtypes")
    private List<RegMonitoringViewVO> searchDataCriteria(List<? extends SearchObject> searchCriteria, int first, int pageSize) {
        
        StringBuilder sb = new StringBuilder();
		sb.append(" SELECT pd.name_in jnsKetentuanIn, pd.name_en jnsKetentuanEn, dt.document_type_in, dt.document_type_en, ");
		sb.append("        dc.document_category_in, dc.document_category_en, dp.document_topic_in, dp.document_topic_en, r.document_no, ");
		sb.append("        r.name_in, r.name_en, '' PICName, s.status, s.REG_MONITORING_ID, ");
		sb.append("        pds.name_in status_name_in, pds.name_en status_name_en, ");
		sb.append(" 	   TO_CHAR(r.published_date, 'dd-Mon-yyyy') published_date, TO_CHAR(r.expired_date, 'dd-Mon-yyyy') effective_date ");
		sb.append("   FROM WO_TRC_REG_MONITORING s ");
		sb.append("        INNER JOIN WO_TRC_REG_MONITOR_REGULATION sr on s.REG_MONITORING_ID = sr.REG_MONITORING_ID  ");
		sb.append("        INNER JOIN wo_mst_parameter_dtl pd on pd.parameter_dtl_code = s.jenis_ketentuan  ");
		sb.append("        INNER JOIN wo_mst_regulation r on r.regulation_id = sr.regulation_id  ");
		sb.append(" 	   LEFT JOIN wo_mst_document_type dt on r.document_type_id = dt.document_type_id  ");
		sb.append("        LEFT JOIN wo_mst_document_category dc on dc.document_category_id = r.document_category_id  ");
		sb.append("        LEFT JOIN wo_mst_document_topic dp on dp.document_topic_id = r.document_topic_id  ");
		sb.append("        INNER JOIN wo_mst_parameter_dtl pds on pds.parameter_dtl_code = s.status ");
		sb.append("  WHERE 1=1 and s.enabled_flag = 'Y' and sr.primary_flag = 'Y' ");
        
        sb = getQueryWhereString(sb, searchCriteria);
        sb.append(" ORDER BY REG_MONITORING_ID DESC ");
        
        
        Query result = getSession().createSQLQuery(sb.toString());
        result.setFirstResult(first);
        result.setMaxResults(pageSize);
        
        List resultList = result.getResultList();
        
        List<RegMonitoringViewVO> vo = new ArrayList<RegMonitoringViewVO>();
        
        if(resultList!=null) {
            for(int i=0; i<resultList.size(); i++) {
                Object[] obj = (Object[]) resultList.get(i);
                RegMonitoringViewVO data = new RegMonitoringViewVO();
                data.setJenisKetentuanIn(obj[0]!=null?(String)obj[0]:null);
                data.setJenisKetentuanEn(obj[1]!=null?(String)obj[1]:null);
                data.setTipeDokumenIn(obj[2]!=null?(String)obj[2]:null);
                data.setTipeDokumenEn(obj[3]!=null?(String)obj[3]:null);
                data.setKategoriIn(obj[4]!=null?(String)obj[4]:null);
                data.setKategoriEn(obj[5]!=null?(String)obj[5]:null);
                data.setTopikIn(obj[6]!=null?(String)obj[6]:null);
                data.setTopikEn(obj[7]!=null?(String)obj[7]:null);
                data.setNoDokumen(obj[8]!=null?(String)obj[8]:null);
                data.setJdlPeraturanIn(obj[9]!=null?(String)obj[9]:null);
                data.setJdlPeraturanEn(obj[10]!=null?(String)obj[10]:null);
                data.setNamaPIC(obj[11]!=null?(String)obj[11]:null);
                data.setStatus(obj[12]!=null?(String)obj[12]:null);
                //data.setSocializationId(obj[13]!=null?((java.math.BigInteger)obj[13]).longValue():null);
                data.setRegMonitoringId(obj[13]!=null?(MathUtil.returnIdObjectToLong(obj[13])):null);
                data.setStatusList(getDataConfirmStatusByRegMonitoringId(data.getRegMonitoringId()));                
                data.setStatusNameIn(obj[14]!=null?(String)obj[14]:null);
                data.setStatusNameEn(obj[15]!=null?(String)obj[15]:null);
                data.setStatusCode(obj[12]!=null?(String)obj[12]:null);
                data.setPublishedDateStr(obj[16] != null ? (String)obj[16] : null);
                data.setEffectiveDateStr(obj[17] != null ? (String)obj[17] : null);
                
                vo.add(data);
            }
        }

        result.setFirstResult(first);
        result.setMaxResults(pageSize);
        
        return vo;
    }
    
    @SuppressWarnings("rawtypes")
	public List<RegMonitoringApprovalVO> getDataApprovalByRegMonitoringId(Long regMonitoringId){
    	StringBuilder sb = new StringBuilder();
		sb.append(" select u.name,d.name_in,d.name_en,TO_CHAR(approval_date, 'dd-Mon-yyyy') approval_date, " + 
    	          "        approval_note " +
				  "   from WO_TMP_REG_MONITORING_APPROVAL a " + 
				  "        inner join wo_mst_user u on a.user_id =u.user_id " + 
				  "        inner join wo_mst_parameter_dtl d on a.approval_status = d.parameter_dtl_code " + 
				  "  where a.REG_MONITORING_ID = :regMonitoringId ");		
        
        sb.append(" ORDER BY REG_MONITORING_APPROVAL_ID ASC ");        
        
        Query result = getSession().createSQLQuery(sb.toString());
        result.setParameter("regMonitoringId", regMonitoringId);
        List resultList = result.getResultList();
        
        List<RegMonitoringApprovalVO> vo = new ArrayList<RegMonitoringApprovalVO>();
        
        if(resultList!=null) {
            for(int i=0; i<resultList.size(); i++) {
                Object[] obj = (Object[]) resultList.get(i);
                RegMonitoringApprovalVO data = new RegMonitoringApprovalVO();
                data.setApprovalBy(obj[0]!=null?(String)obj[0]:null);
                data.setApprovalStatusIn(obj[1]!=null?(String)obj[1]:null);
                data.setApprovalStatusEn(obj[2]!=null?(String)obj[2]:null);
                data.setApprovalDate(obj[3]!=null?(String)obj[3]:null);
                data.setApprovalNote(obj[4]!=null?(String)obj[4]:null);
                vo.add(data);
            }
        }

        
        return vo;
    }
    
    @SuppressWarnings("rawtypes")
	public List<StatusConfirmationVO> getDataConfirmStatusByRegMonitoringId(Long regMonitoringId) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select u1.name pic1, u2.name pic2,u3.name pic3, "
				+ "TO_CHAR(f2.confirmation_date, 'dd-Mon-yyyy')confirmation_date, "
				+ "TO_CHAR(f2.followup_date, 'dd-Mon-yyyy')followup_date, "
				+ "f2.followup_note, TO_CHAR(f2.compliance_date, 'dd-Mon-yyyy') compliance_date, "
				+ "d.name_in compliance_statusIn,d.name_en compliance_statusEn, "
				+ "compliance_note, "
				+ "u4.name followupBy,d2.name_in,d2.name_en, " 
				+ "TO_CHAR(f2.target_date, 'dd-Mon-yyyy')target_date "
				+ "from WO_TMP_REG_MONITORING_PIC_FP f "
				+ "left join WO_TRC_REG_MONITORING_PIC_FP f2 on f2.REG_MONITORING_PIC_FOLLOWUP_ID = f.REG_MONITORING_PIC_FOLLOWUP_ID "
				+ "left join wo_mst_parameter_dtl d on f2.compliance_status = d.parameter_dtl_code "
				+ "left join wo_mst_user u1 on u1.user_id = f.user_id_1  "
				+ "left join wo_mst_user u2 on u2.user_id = f.user_id_2 "
				+ "left join wo_mst_user u3 on u3.user_id = f.user_id_3 "
				+ "left join wo_mst_user u4 on u4.user_id = f2.followup_by_id  "
				+ "left join wo_mst_parameter_dtl d2 on f2.followup_status = d2.parameter_dtl_code "
				+ "where f.REG_MONITORING_ID = :regMonitoringId ");

		sb.append(" ORDER BY f.REG_MONITORING_PIC_FOLLOWUP_ID ASC ");

		Query result = getSession().createSQLQuery(sb.toString());
		result.setParameter("regMonitoringId", regMonitoringId);
		List resultList = result.getResultList();

		List<StatusConfirmationVO> vo = new ArrayList<StatusConfirmationVO>();

		if (resultList != null) {
			for (int i = 0; i < resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
				StatusConfirmationVO data = new StatusConfirmationVO();
				data.setPic1(obj[0] != null ? (String) obj[0] : null);
				data.setPic2(obj[1] != null ? (String) obj[1] : null);
				data.setPic3(obj[2] != null ? (String) obj[2] : null);
				data.setConfirmationDate(obj[3] != null ? (String) obj[3] : null);
				data.setFollowupDate(obj[4] != null ? (String) obj[4] : null);
				data.setFollowupNote(obj[5] != null ? (String) obj[5] : null);
				data.setComplianceDate(obj[6] != null ? (String) obj[6] : null);
				data.setComplianceStatusIn(obj[7] != null ? (String) obj[7] : null);
				data.setComplianceStatusEn(obj[8] != null ? (String) obj[8] : null);
				data.setComplianceNote(obj[9] != null ? (String) obj[9] : null);
				data.setFollowupBy(obj[10] != null ? (String) obj[10] : null);
				data.setFollowupStatusIn(obj[11] != null ? (String) obj[11] : null);
				data.setFollowupStatusEn(obj[12] != null ? (String) obj[12] : null);
				data.setTargetDate(obj[13] != null ? (String) obj[13] : null);
				try {
					data.setRegMonitoringPICFollowUpAttachmentTrcs(regMonitoringPICFollowUpAttachmentTrcDAO.getPICFollowUpAttachmentTrcByRegMonitoringId(regMonitoringId));
				} catch (Exception e) {
					e.printStackTrace();
				}
				vo.add(data);
			}
		}

		return vo;
	}

	public RegMonitoringPICFollowUpAttachmentTrcDAO getRegMonitoringPICFollowUpAttachmentTrcDAO() {
		return regMonitoringPICFollowUpAttachmentTrcDAO;
	}

	public void setRegMonitoringPICFollowUpAttachmentTrcDAO(
			RegMonitoringPICFollowUpAttachmentTrcDAO regMonitoringPICFollowUpAttachmentTrcDAO) {
		this.regMonitoringPICFollowUpAttachmentTrcDAO = regMonitoringPICFollowUpAttachmentTrcDAO;
	}

	public RegMonitoringPICFollowUpTrcDAO getRegMonitoringPICFollowUpTrcDAO() {
		return regMonitoringPICFollowUpTrcDAO;
	}

	public void setRegMonitoringPICFollowUpTrcDAO(RegMonitoringPICFollowUpTrcDAO regMonitoringPICFollowUpTrcDAO) {
		this.regMonitoringPICFollowUpTrcDAO = regMonitoringPICFollowUpTrcDAO;
	}
    
}
