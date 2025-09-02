/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.externalRegulation.dao;


//import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.faces.context.FacesContext;
import javax.persistence.ParameterMode;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.TemporalType;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.common.util.MathUtil;
import com.wo.module.externalRegulation.constant.ExternalRegulationConstants;
import com.wo.module.externalRegulation.model.ExternalRegulation;

/**
 *
 * @author hendra
 */

@Repository("externalRegulationDao")
public class ExternalRegulationDaoImpl extends GenericDAOHibernate<ExternalRegulation, Long> 
    implements ExternalRegulationDao {
	
	@Autowired
    @Qualifier("regulationTrackRecordDao")
    private RegulationTrackRecordDao regulationTrackRecordDao;
	
	@Autowired
    @Qualifier("regulationAttachmentDao")
    private RegulationAttachmentDao regulationAttachmentDao;
	
	public RegulationTrackRecordDao getRegulationTrackRecordDao() {
		return regulationTrackRecordDao;
	}

	public void setRegulationTrackRecordDao(RegulationTrackRecordDao regulationTrackRecordDao) {
		this.regulationTrackRecordDao = regulationTrackRecordDao;
	}
	
	

	public RegulationAttachmentDao getRegulationAttachmentDao() {
		return regulationAttachmentDao;
	}

	public void setRegulationAttachmentDao(RegulationAttachmentDao regulationAttachmentDao) {
		this.regulationAttachmentDao = regulationAttachmentDao;
	}

	@SuppressWarnings({ "rawtypes", "static-access" })
    private StringBuilder getQueryWhereString(StringBuilder sb, List<? extends SearchObject> searchCriteria) {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValue()!=null?searchVal.getSearchValueAsString():"";
				if (!StringUtils.isBlank(val)) {
					if (StringUtils.equals(ExternalRegulationConstants.WHERE_JENIS_KETENTUAN, col)) {
						sb.append(" and r.jenis_ketentuan = '" + val + "' ");
					}	
					else if (StringUtils.equals(ExternalRegulationConstants.WHERE_DOC_TYPE, col)) {
						sb.append(" and dt.document_type_id = " + val + " ");
					}
					else if (StringUtils.equals(ExternalRegulationConstants.WHERE_CATEGORY, col)) {
						sb.append(" and dc.document_category_id = " + val + " ");
					}
					else if (StringUtils.equals(ExternalRegulationConstants.WHERE_TOPIC, col)) {
						sb.append(" and dp.document_topic_id = " + val + " ");
					}
					else if (StringUtils.equals(ExternalRegulationConstants.WHERE_DOC_NO, col)) {
						sb.append(" and upper(r.document_no) LIKE upper('%" + val + "%') ");
					}
					else if (StringUtils.equals(ExternalRegulationConstants.WHERE_NAME, col)) {
						if (locale != null && locale.equals(locale.ENGLISH)) {
							sb.append(" and upper(r.name_en) LIKE upper('%" + val + "%') ");
						} else {
							sb.append(" and upper(r.name_in) LIKE upper('%" + val + "%') ");
						}
					}
					else if (StringUtils.equals(ExternalRegulationConstants.WHERE_PUBLISHED_DATE_START, col)) {
						try {
							sb.append(" and TRUNC(r.published_date) >= TO_DATE('" + sdf2.format(sdf.parse(val)) + "','yyyy-MM-dd') ");
						} catch (ParseException e) {
							e.printStackTrace();
						}
					}
					else if (StringUtils.equals(ExternalRegulationConstants.WHERE_PUBLISHED_DATE_END, col)) {
						try {
							sb.append(" and TRUNC(r.published_date) <= TO_DATE('" + sdf2.format(sdf.parse(val)) + "','yyyy-MM-dd') ");
						} catch (ParseException e) {
							e.printStackTrace();
						}
					}
					else if (StringUtils.equals(ExternalRegulationConstants.WHERE_REKAM_JEJAK, col)) {
						sb.append(" and ( EXISTS( SELECT " + 
								"            1" + 
								"        FROM" + 
								"            wo_tmp_regulation_track_record rtr" + 
								"        WHERE" + 
								"            rtr.regulation_id = r.regulation_id" + 
								"                AND rtr.track_code = '" + val + "')" + 
								"        OR EXISTS( SELECT " + 
								"            1" + 
								"        FROM" + 
								"			wo_tmp_regulation tr," + 
								"            wo_tmp_regulation_track_record rtr" + 
								"        WHERE" + 
								"			rtr.regulation_id = tr.regulation_id AND tr.enabled_flag = 'Y' AND" + 
								"            rtr.regulation_link_id = r.regulation_id" + 
								"                AND rtr.track_code = '" + val + "') ) ");
					}
					else if (StringUtils.equals(ExternalRegulationConstants.WHERE_STATUS, col)) {
						sb.append(" and status = '" + val + "' ");
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
		sb.append("   FROM wo_tmp_regulation r  ");
		sb.append(" 	   LEFT JOIN wo_mst_document_type dt on r.document_type_id = dt.document_type_id  ");
		sb.append(" 	   LEFT JOIN wo_mst_document_category dc on dc.document_category_id = r.document_category_id  ");
		sb.append("        LEFT JOIN wo_mst_document_topic dp on dp.document_topic_id = r.document_topic_id  ");
		//sb.append(" left join wo_tmp_regulation_track_record tr on tr.regulation_id = r.regulation_id  ");
		sb.append("        LEFT JOIN wo_mst_parameter_dtl pd on pd.parameter_dtl_code = r.status ");
		sb.append(" WHERE 1=1 and r.enabled_flag = 'Y' ");
       
        sb = getQueryWhereString(sb, searchCriteria);
        
        Query result = getSession().createSQLQuery(sb.toString());
       
        
        return (Number) result.getSingleResult();
    }
    
    @SuppressWarnings("rawtypes")
    public List<ExternalRegulation> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize, String sortField, SortOrder sortOrder) 
            throws Exception {
        
        List<ExternalRegulation> voList = searchDataCriteria(searchCriteria, first, pageSize);

        return voList;
    }
    
    @SuppressWarnings("rawtypes")
    private List<ExternalRegulation> searchDataCriteria(List<? extends SearchObject> searchCriteria, int first, int pageSize) {
        
        StringBuilder sb = new StringBuilder();
		sb.append(" SELECT dt.document_type_in,dt.document_type_en, dc.document_category_in, dc.document_category_en, ");
		sb.append("        dp.document_topic_in, dp.document_topic_en, r.document_no, r.name_in, r.name_en, ");
		sb.append("        TO_CHAR(r.published_date, 'dd-Mon-yyyy') published_date, ");
		sb.append("        TO_CHAR(r.expired_date, 'dd-Mon-yyyy') expired_date, r.publisher_unit, r.status, ");
		sb.append("        r.regulation_id, pd.name_in status_name_in, pd.name_en status_name_en, ");
		sb.append("        TO_CHAR(r.effective_date, 'dd-Mon-yyyy') effective_date,r2.regulation_id regIdMst,r.directorate");
		sb.append("   FROM wo_tmp_regulation r  ");
		sb.append(" 	   LEFT JOIN wo_mst_document_type dt on r.document_type_id = dt.document_type_id  ");
		sb.append(" 	   LEFT JOIN wo_mst_document_category dc on dc.document_category_id = r.document_category_id  ");
		sb.append("        LEFT JOIN wo_mst_document_topic dp on dp.document_topic_id = r.document_topic_id  ");
		sb.append("        LEFT JOIN wo_mst_regulation r2 on r2.regulation_id = r.regulation_id  ");
		//sb.append(" left join wo_tmp_regulation_track_record tr on tr.regulation_id = r.regulation_id ");
		sb.append("        LEFT JOIN wo_mst_parameter_dtl pd on pd.parameter_dtl_code = r.status ");
		sb.append("  WHERE 1=1 and r.enabled_flag = 'Y'  ");
        
        sb = getQueryWhereString(sb, searchCriteria);
        sb.append(" ORDER BY r.published_date DESC, r.document_no asc ");
        
        
        Query result = getSession().createSQLQuery(sb.toString());
        result.setFirstResult(first);
        result.setMaxResults(pageSize);
        
        List resultList = result.getResultList();
        
        List<ExternalRegulation> vo = new ArrayList<ExternalRegulation>();
        
        if(resultList!=null) {
            for(int i=0; i<resultList.size(); i++) {
                Object[] obj = (Object[]) resultList.get(i);
                ExternalRegulation data = new ExternalRegulation();
                data.setDocumentTypeNameIn(obj[0]!=null?(String)obj[0]:null);
                data.setDocumentTypeNameEn(obj[1]!=null?(String)obj[1]:null);
                data.setDocumentCategoryNameIn(obj[2]!=null?(String)obj[2]:null);
                data.setDocumentCategoryNameEn(obj[3]!=null?(String)obj[3]:null);
                data.setDocumentTopicNameIn(obj[4]!=null?(String)obj[4]:null);
                data.setDocumentTopicNameEn(obj[5]!=null?(String)obj[5]:null);
                data.setDocumentNo(obj[6]!=null?(String)obj[6]:null);
                data.setNameIn(obj[7]!=null?(String)obj[7]:null);
                data.setNameEn(obj[8]!=null?(String)obj[8]:null);
                data.setPublishedDateStr(obj[9]!=null?(String)obj[9]:null);
                data.setExpiredDateStr(obj[10]!=null?(String)obj[10]:null);
                data.setPublisherUnit(obj[11]!=null?(String)obj[11]:null);
                data.setStatus(obj[12]!=null?(String)obj[12]:null);
                //data.setRegulationId(obj[13]!=null?((BigInteger)obj[13]).longValue():null);
                data.setRegulationId(obj[13]!=null?(MathUtil.returnIdObjectToLong(obj[13])):null);
                data.setStatusNameIn(obj[14]!=null?((String)obj[14]):null);
                data.setStatusNameEn(obj[15]!=null?((String)obj[15]):null);
                data.setEffectiveDateStr(obj[16] != null ?((String)obj[16]):null);
                data.setRegulationIdMst(obj[17]!=null?(MathUtil.returnIdObjectToLong(obj[17])):null);
                data.setDirectorate(obj[18]!= null ? ((String)obj[18]) : null);
                
                try {
					data.setRegulationTrackRecords(regulationTrackRecordDao.getRegulationTrackRecordByRegulationId(data.getRegulationId()));
					data.setRegulationAttachments(regulationAttachmentDao.getRegulationAttachmentByRegulationId(data.getRegulationId()));
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
    
    
    
    public Integer callSP() {
    	//String results = null;
    	String bookName = "Java Persistence with Hibernate";
    	String authorFirstname = "Christian";
    	String authorLastname = "Bauer";
    	
    	StoredProcedureQuery spq = this.getSession().createStoredProcedureQuery("addBook_sp");
    	spq.registerStoredProcedureParameter(1, String.class, ParameterMode.IN); 
    	spq.registerStoredProcedureParameter(2, Date.class, ParameterMode.IN); 
    	spq.registerStoredProcedureParameter(3, String.class, ParameterMode.IN); 
    	spq.registerStoredProcedureParameter(4, String.class, ParameterMode.IN); 
    	
    	spq.setParameter(1, bookName);
    	spq.setParameter(2, new Date(), TemporalType.DATE);
    	spq.setParameter(3, authorFirstname);
    	spq.setParameter(4, authorLastname);
    	 
    	// Stored procedure call
    	Integer createdBookId = (Integer) spq.getSingleResult();
    	return createdBookId;
    }
    
    public String getQueryTrackRecord(Long regulationId,String language){
    
    StoredProcedureQuery query = this.getSession()
    	    .createStoredProcedureQuery("WO_SP_GET_TRACK_RECORD")
    	    .registerStoredProcedureParameter(
    	        "reg_id", Long.class, ParameterMode.IN)
    	    .registerStoredProcedureParameter(
        	        "lang", String.class, ParameterMode.IN)
    	    .registerStoredProcedureParameter(
    	        "result", String.class, ParameterMode.OUT)
    	    .setParameter("reg_id", regulationId)
    	    .setParameter("lang", language);

    	query.execute();

    	String result = (String) query
    	    .getOutputParameterValue("result");
    	return result;
    }
}
